# ACE Target Architecture Views

Status: target-state design for the synthetic digital-banking reference application. The local Spring API is implemented; mobile, Kubernetes, cloud services, identity provider, event broker, and production telemetry are design boundaries, not deployed components.

## System context

```mermaid
flowchart LR
  C[Customer] -->|HTTPS| CH[Web and mobile channels]
  O[Operations analyst] -->|approved request| AS[AI banking assistant]
  CH --> AP[API gateway and identity boundary]
  AS --> AG[Agent policy and MCP tool gateway]
  AG -->|read-only by default| API[Banking service APIs]
  AG -->|payment action: approval token required| API
  AP --> API
  API --> CORE[Core banking and beneficiary services]
  API --> EVT[Event and notification platform]
  API --> TEL[Telemetry collector]
  CI[Quality pipeline] -->|synthetic tests and release evidence| AP
  CI --> TEL
  GOV[Risk, security, product and SRE owners] -->|gates and residual-risk decisions| CI
```

## Containers and trust boundaries

```mermaid
flowchart TB
  subgraph T1[Untrusted client boundary]
    WEB[Web/mobile client]
  end
  subgraph T2[Authenticated channel boundary]
    GW[Gateway: OIDC validation, rate limits]
    BFF[Channel API]
  end
  subgraph T3[Banking service boundary]
    TRANSFER[Transfer API]
    BENEF[Beneficiary API]
    LEDGER[Ledger adapter]
    OUTBOX[Transactional outbox]
  end
  subgraph T4[Agent restricted boundary]
    ASSIST[Assistant and retrieval]
    POLICY[Tool policy: schema, scope, limits]
    APPROVE[Independent human approval]
    AUDIT[Append-only tool audit]
  end
  subgraph T5[Quality and operations boundary]
    PIPE[CI quality gates]
    SYNTH[Synthetic data factory]
    OTEL[OTel collector and redaction]
    DASH[Dashboards and alerting]
  end
  WEB --> GW --> BFF --> TRANSFER
  BFF --> BENEF
  TRANSFER --> LEDGER
  TRANSFER --> OUTBOX
  ASSIST --> POLICY --> TRANSFER
  POLICY -. payment-changing action .-> APPROVE
  POLICY --> AUDIT
  PIPE --> SYNTH
  PIPE --> TRANSFER
  TRANSFER --> OTEL --> DASH
  PIPE --> OTEL
```

Trust-boundary controls: clients never connect directly to ledger or tools; workload identity is checked at service entry; agent arguments are schema-validated and bounded; approvals are bound to the exact action and expire; sensitive fields are removed before telemetry export. Production identity, approval, ledger and durable audit integrations remain deployment requirements.

## Transfer sequence and evidence join

```mermaid
sequenceDiagram
  participant T as CI/mobile test
  participant G as Gateway
  participant X as Transfer API
  participant L as Ledger
  participant E as Event outbox
  participant O as OTel/evidence store
  T->>T: Create synthetic run, test and release IDs
  T->>G: POST transfer + Idempotency-Key + traceparent
  G->>X: Authenticated request, propagated trace context
  X->>X: Validate beneficiary, amount, limits and idempotency
  X->>L: Apply one debit/credit transaction
  L-->>X: Stable transfer reference
  X->>E: Record committed transfer event
  X-->>T: 201 + transfer reference + trace ID
  G-->>O: Redacted span attributes and service outcome
  X-->>O: Correlated service spans
  T->>O: Publish test result, dataset version and release ID
  O-->>T: Joinable evidence for gate and RCA
```

The evidence join key is `trace_id` plus `test_run_id`, `test_case_id`, `release_id`, `service.name`, and synthetic `dataset_version`. Do not attach account numbers, beneficiary names, tokens, full prompts, or payment amounts to span attributes. The local example uses an in-process logging exporter; this diagram describes the target OTLP deployment.

## Deployment view

```mermaid
flowchart LR
  subgraph Build[Build and test plane]
    SRC[Source and pull request] --> CI[CI runner: JDK 25, Maven]
    CI --> TEST[Unit, API, contract, artifact tests]
    CI --> PKG[Evidence bundle and checksums]
  end
  subgraph Ephemeral[Ephemeral test plane]
    CI --> NS[Isolated namespace per run]
    NS --> APP[Banking services]
    NS --> DB[(Ephemeral database / seeded synthetic data)]
    APP --> COL[OTLP collector]
    COL --> EV[Restricted evidence store]
    NS --> CLEAN[TTL cleanup]
  end
  subgraph Release[Controlled release plane]
    PKG --> GATE[Policy gate and named approvals]
    GATE --> STAGE[Canary / staged rollout]
    STAGE -->|SLO breach| ROLLBACK[Automated rollback]
  end
```

The workflow in this repository verifies the local Maven project and uploads CI artifacts. Ephemeral namespace provisioning, cloud IAM, a database service, evidence-store retention, canary and rollback automation require platform implementation before production use.

## Architecture decisions to ratify

| Decision | Current direction | Required production decision |
| --- | --- | --- |
| Payment-changing agent tools | Separate approval, narrow allow-list, immutable audit | Select identity-bound approval and durable audit products; threat-model replay and expiry |
| Test environments | Isolated and synthetic, short-lived | Select Kubernetes namespace/cluster tenancy, secrets source, network policy, TTL controller |
| Telemetry | W3C trace context and OTel | Select authenticated collector, storage, retention, redaction and access policy |
| Release | Deterministic mandatory gates; AI advisory only | Integrate policy engine and signed evidence with deployment controller |

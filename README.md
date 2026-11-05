# Digital Banking Quality Architecture (Java)

Java 25 reference project for the capstone's technical-evidence checklist. It includes a runnable Spring Boot API and automated tests, plus portable artifacts for mobile, performance, AI/RAG, MCP governance, OpenTelemetry, and synthetic-data assurance.

All payment examples are isolated and synthetic. The transfer API uses an
in-memory reference ledger; it is not connected to a real bank, payment rail,
authentication service, or customer data.

## Architecture walkthrough

Use this sequence to explain the architecture, folder structure, API behavior,
quality evidence, and implementation boundaries.

### 1. Explain the architecture

Open [`docs/architecture-views.md`](docs/architecture-views.md) and explain:

- **System context:** customers and operations use web/mobile channels or the
  AI assistant; requests pass through governed API or tool boundaries.
- **Containers and trust boundaries:** clients cannot connect directly to
  banking data or tools. Authentication, schema validation, approval, audit,
  and telemetry controls sit at the appropriate boundary.
- **Transfer sequence:** a test run sends a synthetic transfer with an
  idempotency key and trace context. The API validates it, updates the
  reference ledger, and returns a traceable result.
- **Deployment view:** CI runs deterministic checks and publishes evidence;
  isolated environments and controlled release automation are production
  design requirements.

Use [`docs/architecture-pack.md`](docs/architecture-pack.md) to explain the
quality layers, mandatory release controls, and AI governance principles.

### 2. Explain the folder structure

```text
ACE/
├── pom.xml                         # Java 25 and Spring Boot build
├── README.md                       # Instructions and evidence map
├── capstone_requirements_and_plan.md
├── docs/
│   ├── architecture-*.md           # Context, flows, boundaries, deployment
│   ├── contracts/                  # Provider API contract
│   ├── mcp/                        # Governed tool contract
│   └── ...                         # Risk, NFR, AI, telemetry, governance
├── src/
│   ├── main/java/com/ace/banking/
│   │   ├── api/                   # HTTP endpoint, DTOs, error handling
│   │   ├── service/               # Transfer and assurance business logic
│   │   ├── agent/                 # Principal, approval, tool, audit types
│   │   ├── data/                  # Synthetic data and anomaly detection
│   │   ├── observability/          # Trace propagation and OTel setup
│   │   └── model/                 # Quality and governance domain models
│   └── test/
│       ├── java/                  # Unit, API, contract, AI, governance tests
│       └── resources/             # Datasets, rubrics, and contracts
├── scripts/                       # Evidence-manifest generation
├── tests/performance/             # Optional k6 workload
└── .github/workflows/             # CI quality-evidence workflow
```

Dependency direction:

```text
HTTP request -> api -> service -> model/data
                         |-> agent policy and audit
                         |-> observability
tests/resources -> tests -> target reports -> evidence manifest
```

`target/` is generated output and should not be treated as source. Extract
downloaded CI evidence outside the repository.

### 3. Run the architecture summary

From the repository root in PowerShell:

```powershell
mvn --% exec:java -Dexec.mainClass=com.ace.banking.DigitalBankingQualityApplication
```

Point out the printed quality layers, critical scenarios, mandatory release
gates, AI assurance checks, and payment risk owners.

### 4. Run and exercise the API

Start the API in a separate PowerShell terminal:

```powershell
mvn --% spring-boot:run -Dspring-boot.run.main-class=com.ace.banking.BankingApiApplication
```

In another terminal, submit the documented synthetic transfer:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/transfers `
  -Headers @{ "Idempotency-Key" = "manual-synthetic-001" } `
  -ContentType "application/json" `
  -Body '{"fromAccount":"SYN-CHK-001","beneficiaryId":"SYN-BEN-001","amount":25.50,"currency":"USD","idempotencyKey":"manual-synthetic-001"}'
```

Explain the request and response:

- `Idempotency-Key` must match the request body.
- Only the documented synthetic account, beneficiary, and USD scenario are
  enabled.
- A repeated request with the same key returns the original transfer.
- Reusing the key with different transfer details is rejected.
- The service starts with a synthetic balance of USD 2,500.00 and keeps state
  in memory for the running process.

The API is intentionally a small reference implementation of the transfer
control path, not a production ledger implementation.

## Use as a VS Code Agent Plugin

This repository is also an Agent Plugins 1.0 package for the Copilot harness.
It provides the **ACE Quality Engineer** agent and the
`quality-evidence-review` skill.

To install it directly from Git in VS Code:

1. Open **Chat: Open Customizations** from the Command Palette.
2. Open **Plugins**, select the `+` button next to **User** or **Workspace**,
   and choose **Install Plugin from Source**.
3. Enter `https://github.com/csrameshbabu/ACE_Repo`.
4. Start a new Copilot chat, select **ACE Quality Engineer**, and try a request
   such as `Review the transfer authorization evidence`.

The Git source is installed as an agent plugin; it is not a traditional
VS Code extension and does not require the VS Code Extension Marketplace.
Review the plugin files before trusting or installing them. The plugin
intentionally operates only on the synthetic reference project.

#### Install from the VS Code Marketplace

If this plugin is published to the VS Code Marketplace:

1. Open **Extensions: Marketplace** from the Command Palette or select the
   Extensions icon in VS Code.
2. Search for **ACE Digital Banking Quality**.
3. Select the publisher's listing and choose **Install**.
4. Open **Chat: Open Customizations**, select **Plugins**, and confirm that
   the ACE plugin is enabled for the current user or workspace.
5. Start a new Copilot chat, select **ACE Quality Engineer**, and try a
   request such as `Review the transfer authorization evidence`.

The current repository is distributed as an Agent Plugins 1.0 package and is
installed from Git using the instructions above. It is not currently a
traditional VS Code Marketplace extension. Verify the publisher and review
the plugin files before installing any marketplace listing.

### 5. Run the evidence suite

Run all unit, API, contract, and artifact validation tests:

```powershell
mvn test
```

The tests cover API/contract behavior, AI golden data, MCP transfer-tool
governance, synthetic-data anomaly rules, quality architecture, and release
governance.

After the tests complete, create and inspect the local evidence manifest:

```powershell
python scripts/build_evidence_manifest.py
Get-Content target/evidence-manifest.json
```

The manifest records test totals, tool versions, environment information, and
SHA-256 hashes for the AI and synthetic-data resources.

### 6. Use the evidence map

| Question | Open this file or folder |
| --- | --- |
| Where is the target architecture? | [`docs/architecture-views.md`](docs/architecture-views.md) |
| Where are quality layers and release gates defined? | [`docs/architecture-pack.md`](docs/architecture-pack.md) |
| Where is the API contract? | [`docs/contracts/transfers-v1.yaml`](docs/contracts/transfers-v1.yaml) |
| Where is the governed MCP tool? | [`docs/mcp/transfer-create-tool.json`](docs/mcp/transfer-create-tool.json) |
| Where is trace correlation described? | [`docs/opentelemetry-trace-correlation.md`](docs/opentelemetry-trace-correlation.md) |
| Where are AI datasets and rubric checks? | `src/test/resources/ai/` |
| Where are synthetic data and anomaly checks? | `src/test/resources/data/` and `src/main/java/com/ace/banking/data/` |
| Where is CI evidence published? | `.github/workflows/quality-evidence.yml` |

### 7. State the implementation boundaries

- `mvn test` verifies the deterministic local project and its evidence
  artifacts.
- The local OpenTelemetry setup uses a logging exporter.
- The mobile journey, k6 workload, production identity, approval service,
  durable audit store, cloud deployment, and live model evaluation require
  separate platform execution before production use.
- All payment examples and datasets in this repository are synthetic.

## Run and verify

Run all unit, API, contract, and artifact validation tests:

```powershell
mvn test
```

Run the architecture summary console application:

```powershell
mvn --% exec:java -Dexec.mainClass=com.ace.banking.DigitalBankingQualityApplication
```

Run the Spring Boot sample API in Windows PowerShell:

```powershell
mvn --% spring-boot:run -Dspring-boot.run.main-class=com.ace.banking.BankingApiApplication
```

Submit a synthetic test transfer from a second PowerShell terminal:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/transfers `
  -Headers @{ "Idempotency-Key" = "manual-synthetic-001" } `
  -ContentType "application/json" `
  -Body '{"fromAccount":"SYN-CHK-001","beneficiaryId":"SYN-BEN-001","amount":25.50,"currency":"USD","idempotencyKey":"manual-synthetic-001"}'
```

## Expected technical evidence

| Requirement | Evidence in this project |
| --- | --- |
| Java/Spring API and consumer/provider contracts | `src/main/java/com/ace/banking/api`, `docs/contracts/transfers-v1.yaml`, `src/test/resources/contracts/transfers-v1-consumer.json`, and `TransferApiContractTest` |
| Appium/equivalent mobile journey | `docs/mobile-journey.md` |
| k6/JMeter workload and resilience experiments | `tests/performance/k6-transfer.js` and `docs/performance-resilience-experiment.md` |
| OpenTelemetry trace correlation | `TraceCorrelationFilter`, `OpenTelemetryConfiguration`, API trace-propagation test, and `docs/opentelemetry-trace-correlation.md` |
| Prompt/RAG dataset and judge rubric | `src/test/resources/ai/prompt-rag-golden-set.jsonl`, `src/test/resources/ai/judge-rubric.json`, and `docs/ai-assurance-evidence.md` |
| MCP tool contract, authorization, and audit | `docs/mcp/transfer-create-tool.json`, `TransferToolService`, `TransferToolServiceTest`, and `docs/mcp-tool-security.md` |
| Synthetic data manifest and anomaly rule | `src/test/resources/data/synthetic-banking-manifest.json`, `src/test/resources/data/synthetic-transfers.csv`, and `SyntheticDataAnomalyDetector` |

## Review response and evidence map

The revised evaluation identified missing architecture views, end-to-end traceability, automated evidence publication, broader NFR evidence, operating controls, and a quantified roadmap. The following artifacts close those design and framework gaps:

| Review gap | Added framework evidence |
| --- | --- |
| Architecture context, flows, trust boundaries, deployment | [`docs/architecture-views.md`](docs/architecture-views.md) |
| Requirement-to-risk-to-test-to-evidence traceability | [`docs/traceability-matrix.md`](docs/traceability-matrix.md) |
| Repeatable CI execution, checksummed dataset/run manifest and retained reports | [`.github/workflows/quality-evidence.yml`](.github/workflows/quality-evidence.yml), [`scripts/build_evidence_manifest.py`](scripts/build_evidence_manifest.py) |
| Security, privacy, accessibility, capacity and recovery plan | [`docs/nfr-assurance-plan.md`](docs/nfr-assurance-plan.md) |
| SLOs, alerts, missing telemetry, RCA and rollback | [`docs/operational-quality.md`](docs/operational-quality.md) |
| RACI, approvals, retention, exception and risk records | [`docs/governance-pack.md`](docs/governance-pack.md) |
| ADRs, 30/60/90-day plan, cost assumptions and value targets | [`docs/roadmap-and-value.md`](docs/roadmap-and-value.md) |

The GitHub Actions workflow runs the deterministic Maven suite on pushes to `main` or `master` and on pull requests. It uploads Surefire reports, documentation, test resources, the k6 script, the Maven project file, and a run/dataset-hash manifest. Java implementation and test source files, and the workflow YAML itself, remain in the repository and are not included in the downloaded artifact. An opt-in k6 job is available from **Actions > ACE Quality Evidence > Run workflow**; select **Run the two-minute synthetic k6 workload** before starting it.

### Observed CI evidence

The downloaded artifact for [workflow run 38730125370](https://github.com/csrameshbabu/ACE_Repo/actions/runs/38730125370) records commit `372d1698947b17c5f6892202a5c64c62a9805368`, event `push`, and 13 tests across seven Surefire reports: 0 failures, 0 errors, and 0 skipped. The four synthetic/AI dataset hashes in the manifest matched the files in that artifact. This describes the reports in that run; it does not establish results for k6, mobile-device, security-scan, recovery, or live-model evaluation.

When downloading an artifact, extract it outside the repository or add its folder pattern (for example, `ace-quality-evidence-*/`) to `.gitignore`. An extracted artifact inside the project appears as untracked content and should not be committed as source.

The workflow only produces evidence after it has run in GitHub Actions. It does not claim that a mobile device farm, Kubernetes, a production-scale model, an OTLP collector, or a security scanner has been deployed. Thresholds and savings in the roadmap remain planning targets until measured against an agreed baseline.

## Important scope notes

- The consumer/provider contract is represented as a consumer expectation JSON file plus a provider OpenAPI contract and a Spring `MockMvc` verification. This is contract-style test evidence, not a Pact broker integration.
- The mobile document is a concrete Appium 2 journey/device test design; it is not wired to a physical device farm.
- For a local k6 run, install k6 separately and start the sample API; the optional hosted workflow installs k6 and starts a fresh isolated sample API for you. A push-triggered CI run does not run k6.
- OpenTelemetry exports to a logging exporter for local reference use. Production deployment requires an authenticated OTLP collector and approved retention/redaction settings.
- AI dataset/rubric tests validate the evidence artifacts. No external model or live LLM judge is invoked by `mvn test`.
- MCP authorization/audit is an executable policy example using in-memory audit events. Production must connect identity, approval verification, and durable audit storage to governed services.

# Operational Quality and Evidence

The values below are initial service objectives for a pilot and require confirmation against product baseline and service tier. They are targets, not observed production SLOs.

| Service indicator | Initial objective | Measurement | Alert / action |
| --- | --- | --- | --- |
| Transfer API availability | 99.9% monthly for valid authorized requests | Successful eligible requests / eligible requests; exclude client validation errors, keep exclusions visible | Burn rate alert; stop rollout and page on-call |
| Transfer latency | p95 <= 500 ms, p99 <= 1 s | Server duration histogram, excluding synthetic load-test traffic from customer SLI | Page on sustained breach; compare dependencies and saturation |
| Transfer integrity | 100% no duplicate debit and no unauthorized transfer | Ledger invariants, idempotency and authorization outcomes | Any violation is a severity-1 incident and immediate release stop |
| Evidence completeness | 100% of critical tests carry run, release, trace and dataset IDs | Required-field completeness check | Missing telemetry fails quality gate and emits pipeline alert |
| AI tool safety | 100% deny unauthorized or unapproved payment-changing actions | Tool decision/audit event counts by outcome | Any bypass is severity-1; disable tool route |

## Error budgets and release policy

For the 99.9% monthly availability objective, the initial error budget is 0.1% of eligible requests (about 43.8 minutes per 30-day month when expressed as time). Product and SRE must agree on the request-based definition. Freeze risky changes when budget is exhausted; continue mandatory integrity, security and recovery fixes. Predictive selection and AI recommendations cannot remove mandatory checks.

## Telemetry contract

Required span/log dimensions: `trace_id`, `test_run_id` when synthetic, `test_case_id`, `release_id`, `service.name`, `deployment.environment`, `dataset_version`, outcome and bounded error category. Enforce W3C trace context. Missing trace or release identifiers on a critical test must fail evidence publication. Sample high-volume routine spans only after preserving all errors and critical payment traces. Redact authorization headers, tokens, account/beneficiary identifiers, prompts containing customer data, and payment payloads before export.

## Dashboard and alert panels

1. Availability, p50/p95/p99 latency, throughput and error budget burn by service/release.
2. Payment invariant failures, idempotency conflicts, reversal outcomes and duplicate event count.
3. CI gate results, flaky-test rate, duration trend, skipped mandatory tests and artifact completeness.
4. Trace coverage and missing-field count by service and environment.
5. AI answer groundedness/abstention, policy violations, tool allow/deny, approval latency and judge disagreement; separate deterministic verdicts from advisory judge scores.
6. Synthetic data generation version, anomaly counts and environment freshness.

## RCA and rollback drill

For a failed critical transfer test, capture the release and test IDs, trace, sanitized request class, idempotency outcome, downstream spans, deployment diff, dataset hash, and owner. Identify detection time, customer impact estimate, containment and ledger reconciliation. Roll back when integrity is uncertain, a mandatory gate regresses, or the agreed SLO burn policy triggers. The release owner records the decision and links the rollback/deployment run. Conduct a quarterly tabletop, then a controlled staging drill; attach observed recovery time and integrity reconciliation. No drill result is claimed in this design document.

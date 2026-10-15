# Technology Decisions, Roadmap and Value Case

## Decision records

| ADR | Decision | Alternatives considered | Rationale and consequence |
| --- | --- | --- | --- |
| ADR-001 | Keep release-critical checks deterministic; AI outputs are advisory | AI-selected-only release gates; manual regression only | Prevents model outage or evaluator drift from weakening financial controls. Requires stable test ownership and maintained rules. |
| ADR-002 | Use synthetic, versioned data with explicit manifests | Masked production copy; shared mutable test data | Improves repeatability and reduces privacy exposure. Synthetic realism must be validated against approved distributions. |
| ADR-003 | Propagate W3C trace context and join evidence with run/release IDs | Team-local correlation IDs; log-only troubleshooting | Enables cross-service diagnosis and evidence joins. Requires redaction, access and retention controls. |
| ADR-004 | Payment-changing agent calls require separate human approval | Agent self-approval; unrestricted tool access | Preserves separation of duties. Adds latency and requires identity-bound, expiring approval service. |

## 30/60/90-day implementation plan

| Window | Deliverables | Exit evidence | Dependencies / adoption risks |
| --- | --- | --- | --- |
| Days 0–30: prove foundation | Run Maven suite on every pull request; publish Surefire reports and artifact manifest; complete payment traceability IDs; baseline test time/flakiness; approve threat model and data policy | Successful CI run links commit, tests, dataset hashes and sanitized reports; signed baseline | CI runner access, branch protection, owner availability; risk: documentation mistaken for deployed platform |
| Days 31–60: broaden assurance | Add concurrent/idempotency persistence tests; execute k6 baseline and stress; automate API security checks; execute mobile accessibility checks on agreed devices; implement SLO dashboard and telemetry completeness alert | k6/device/security reports with environment and trace IDs; agreed SLOs; defects triaged | Test environment, device farm, security tooling, telemetry collector; risk: noisy early thresholds |
| Days 61–90: operationalize | Run recovery/chaos and rollback drills; calibrate AI judge against human labels; test approval identity binding and durable audit; introduce ephemeral namespace cleanup; review value and exceptions | Recovery and rollback record, judge calibration, signed risk/exception review, measured value report | Platform, SRE, Risk, Privacy and Responsible AI sign-off; risk: integration dependencies delay automation |

## Cost model (planning worksheet)

No vendor pricing or existing baseline was supplied, so use local cost inputs rather than invented dollar amounts. Estimate monthly cost as:

`C_total = C_CI_minutes + C_device_farm + C_test_environment + C_observability_storage + C_security_tooling + C_model_evaluation + C_engineering_enablement`

For each line capture units, unit rate, monthly volume, owner, and source/date. Compare tool alternatives with a 12-month total-cost view, including integration, support, data egress, retention and operator effort. Candidate options: self-hosted versus managed CI; shared versus ephemeral Kubernetes namespaces; managed versus self-hosted telemetry; physical device farm versus focused in-house devices; deterministic evaluator versus LLM judge. Select only after security, resilience, retention and integration requirements are scored alongside cost.

## Baseline and measurable value targets

Baseline values must be collected during days 0–30. The following are proposed targets, not achieved results:

| Measure | Baseline source | 90-day target | Guardrail |
| --- | --- | --- | --- |
| Critical release evidence lead time | Timestamp from commit to complete signed evidence | Reduce by 50% from baseline | Mandatory controls and sign-offs remain |
| Regression runtime | CI duration by suite, p50/p95 | Reduce critical-path runtime by 30% | No material drop in critical scenario coverage |
| Escaped high-severity payment defects | Defect and incident system, normalized by release volume | Downward trend; target set after baseline | Report severity and exposure; do not optimize raw count alone |
| Flaky test rate | Retry outcomes / executed tests | Below 2% for blocking suite | Retries never convert a first-attempt failure into an unreported pass |
| Evidence completeness | Required evidence fields present / critical runs | 100% | Missing trace/artifact is a failed gate |
| Manual regression effort | QE hours per release | Reduce 25% from baseline | Reinvest savings in security, recovery and exploratory testing |

Benefit estimate: `hours_saved × loaded hourly rate + avoided incident loss (probability-weighted) − incremental platform and run cost`. Report assumptions, confidence range, and quarterly realized values; do not claim ROI until actual inputs are approved and measured.

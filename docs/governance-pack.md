# Governance Pack

## Responsibility model

| Activity | Product Owner | Payments Lead | Security/Risk | QE Platform | SRE | Responsible AI | Privacy | Release Manager |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Business outcome and release acceptance | A | C | C | R | C | C | C | R |
| Payment integrity controls and residual risk | C | A/R | C | R | C | C | I | C |
| CI tests, evidence manifest and synthetic data | I | C | C | A/R | C | C | C | I |
| Security threat model and security exceptions | I | C | A/R | C | C | C | C | I |
| SLO, dashboard, incident and rollback | C | C | C | R | A/R | I | I | C |
| AI evaluation and tool policy | C | C | C | R | C | A/R | C | I |
| Privacy assessment and retention | I | C | C | R | C | C | A/R | I |
| Final deployment and rollback decision | A | C | C | R | C | C | C | R |

R = Responsible, A = Accountable, C = Consulted, I = Informed. Each release record must replace role labels with named individuals and record approval timestamps. No person may approve their own payment-changing action or its residual risk.

## Approval matrix

| Decision | Required approval | Separation rule |
| --- | --- | --- |
| Routine release with all mandatory gates green | Product Owner and Release Manager | Executor cannot be sole approver |
| Payment integrity control failure | Payments Lead plus Product Owner; Security/Risk consulted | Never override with AI score or test waiver |
| Security/privacy exception | Security/Risk or Privacy owner plus Product Owner | Named approver must not be the exception requester |
| Agent payment action | Authenticated human approver bound to exact action and short expiry | Agent and tool service cannot approve |
| AI model/prompt change | Responsible AI reviewer plus product owner; security/privacy as applicable | Deterministic safety suite stays blocking |
| Rollback | On-call SRE may initiate immediately; notify Release Manager and Product Owner | Post-event review records evidence and reconciliation |

## Operating cadence
- Daily issue triage for failed checks and trace anomalies
- Weekly review of prompt regression results and red-team findings
- Monthly review of residual-risk posture and exception expiry
- Quarterly governance review of ROI, drift, and model improvements

## Exception register and expiry workflow

Every exception record includes ID, requirement/risk IDs, failed/missing evidence, business impact, compensating control, owner, requester, approver, created date, expiry date, affected releases, and closure evidence. Critical payment-integrity, authorization, privacy, or mandatory security controls are not waivable for release. Other exceptions expire within 30 calendar days unless a policy owner approves a shorter period; renewal requires fresh evidence and a new approval. Pipeline blocks expired exceptions and reports approaching expiry at 7 and 2 days.

| Example record | State | Required next action |
| --- | --- | --- |
| EX-001: production OTLP collector not present in local reference | Design limitation; not a production release exception | Before deployment, record collector, redaction, retention and access validation evidence; accountable SRE |
| EX-002: no mobile device-farm run included in this repo | Evidence gap | Attach agreed device/browser results before claiming mobile execution; accountable QE Platform |

These are transparent evidence gaps in the reference package, not approved production waivers.

## Evidence retention and audit

Retain sanitized test results, dataset manifests and hashes, approval records, tool allow/deny events, deployment decisions, exceptions and rollback/RCA records for 13 months as a proposed baseline. Security, Privacy, Legal and Records Management must ratify jurisdictional retention before production. Store immutable checksummed bundles in an access-controlled repository; restrict raw telemetry and redact payment/identity data before export. Record access and deletion events. Never store secrets, live customer payloads, or unrestricted prompts in the evidence bundle.

## Responsible AI, privacy and fairness review

Before a model or retrieval change, record intended use, prohibited use, source inventory, data classification, known limitations, human oversight, fallback behavior, abuse cases, and rollback owner. Evaluate answer quality and abstention across approved synthetic cohorts and scenario categories; compare error rates and harmful outcomes across relevant groups where legally and ethically permitted. Review retrieval poisoning, prompt injection, sensitive-data disclosure, tool misuse, feedback poisoning and judge disagreement. Human reviewers adjudicate critical cases. Do not use predictive quality signals to rank employees or to bypass mandatory controls.

## Residual-risk decision record template

`decision_id`, release/commit, risk IDs, evidence links, unresolved findings, impact/likelihood, compensating controls, decision (accept/reject/defer), named business owner, approver, rationale, approval timestamp, expiry/review date, and rollback trigger. An empty owner or expired approval blocks release.

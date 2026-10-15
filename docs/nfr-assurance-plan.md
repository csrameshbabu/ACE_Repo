# Non-Functional Assurance Plan

Targets below are proposed pilot acceptance thresholds, not measured performance or certification results. Product, Security, SRE and Accessibility owners must confirm applicability and baseline before using them as release commitments.

| Area | Scenarios and controls | Acceptance evidence | Gate / owner |
| --- | --- | --- | --- |
| API and web security | Threat model; authentication and authorization matrix; object-level access; input validation; rate limits; CSRF/CORS; dependency and secret scanning; SAST, DAST and API fuzzing | Threat model with abuse cases; scanner reports with commit, severity, remediation or expiring exception | No unresolved critical/high exploitable finding; Security and Risk |
| Mobile security | MASVS-aligned storage, transport, deep-link, session, screenshot/clipboard and tamper checks; test rooted/emulated and supported OS/device matrix | Sanitized device reports and reproducible defect references | No critical payment-path weakness; Security and Mobile QE |
| Privacy | Synthetic-only test data; data-flow inventory; minimization; retention and deletion checks; telemetry redaction; prompt/response access controls | Dataset manifest/hash, privacy review, redaction tests and retention configuration | No personal/production data in fixtures or logs; Privacy owner |
| Accessibility | WCAG 2.2 AA target for critical transfer journey; keyboard/focus, screen reader labels, contrast, text resize/reflow, error identification and timeout recovery | Automated axe report plus manual TalkBack/VoiceOver and keyboard checklist per supported platform | No critical-task blocker; accessibility owner |
| Performance and capacity | Baseline, ramp, stress, soak and burst; transfer, beneficiary and history endpoints; dependency latency and saturation | k6 JSON summary, environment size, data volume, commit, p50/p95/p99, throughput, errors and resource metrics | Proposed p95 < 500 ms at 100 req/s pilot profile, errors < 1%; SRE to ratify |
| Resilience and recovery | Timeout, 5xx, slow dependency, broker outage, duplicate delivery, pod loss, zone loss and recovery; deterministic fallback when AI unavailable | Fault injection log, recovery timeline, duplicate/ledger invariants, event lag and rollback record | Proposed RTO 30 min / RPO 5 min for service tier; architecture owners to confirm |
| Compatibility | Supported browser/OS/device combinations; low bandwidth, offline/reconnect, orientation and large text | Device/browser matrix with version and run links | All agreed P0 combinations pass; Product + QE |

## Execution evidence template

Record one row per run: `run_id`, `requirement_id`, `commit_sha`, `environment`, `tool/version`, `dataset_version`, `scenario`, `expected`, `actual`, `result`, `trace_or_report_link`, `owner`, `defect_or_exception`, `approval`, `expiry`. For security findings include CVE/rule, exploitability context and remediation commit. For recovery include injection start/end, detection, mitigation, service restored and integrity reconciliation.

## Current state

The repo contains a k6 workload and mobile/accessibility design. It does not contain an executed k6 result, device-farm run, security scan output, chaos run, production-scale capacity model or recovery drill. Those remain evidence tasks; proposed thresholds must not be reported as observed results.

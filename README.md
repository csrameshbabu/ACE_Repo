# Digital Banking Quality Architecture (Java)

Java 17 reference project for the capstone's technical-evidence checklist. It includes a runnable Spring Boot API and automated tests, plus portable artifacts for mobile, performance, AI/RAG, MCP governance, OpenTelemetry, and synthetic-data assurance.

All payment examples are isolated and synthetic. The transfer API uses an in-memory demonstration ledger; it is not connected to a real bank, payment rail, authentication service, or customer data.

## Run and verify

Run all unit, API, contract, and artifact validation tests:

```powershell
mvn test
```

Run the architecture summary console demo:

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

The GitHub Actions workflow runs the deterministic Maven suite on pushes and pull requests, uploads Surefire reports and a run/dataset-hash manifest, and offers an opt-in k6 job from **Actions → ACE Quality Evidence → Run workflow → Run the two-minute synthetic k6 workload**. These jobs produce evidence only after the workflow is enabled and executed in a GitHub repository. They do not claim that a mobile device farm, Kubernetes, a production-scale model, an OTLP collector, or a security scanner has already been deployed. Thresholds and savings in the roadmap are planning targets until measured against an agreed baseline.

## Important scope notes

- The consumer/provider contract is represented as a consumer expectation JSON file plus a provider OpenAPI contract and a Spring `MockMvc` verification. This is contract-style test evidence, not a Pact broker integration.
- The mobile document is a concrete Appium 2 journey/device test design; it is not wired to a physical device farm.
- For a local k6 run, install k6 separately and start the sample API; the optional hosted workflow installs k6 and starts a fresh isolated sample API for you.
- OpenTelemetry exports to a logging exporter for local demonstration. Production deployment requires an authenticated OTLP collector and approved retention/redaction settings.
- AI dataset/rubric tests validate the evidence artifacts. No external model or live LLM judge is invoked by `mvn test`.
- MCP authorization/audit is an executable policy example using in-memory audit events. Production must connect identity, approval verification, and durable audit storage to governed services.

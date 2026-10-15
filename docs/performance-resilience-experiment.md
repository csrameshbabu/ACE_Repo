# k6 Workload and Resilience Experiment

## Workload
`tests/performance/k6-transfer.js` applies a steady 5 transfer requests/second for two minutes against the isolated synthetic transfer API. It uses unique idempotency keys and a fixed, minimal synthetic amount. The response contract accepts `202`; `409` is treated as an expected guardrail outcome when the bounded synthetic balance is exhausted.

## Run
Start the API in one terminal:

```powershell
mvn --% spring-boot:run -Dspring-boot.run.main-class=com.ace.banking.BankingApiApplication
```

In another terminal, with k6 installed:

```powershell
k6 run -e BASE_URL=http://localhost:8080 -e RUN_ID=local-001 tests/performance/k6-transfer.js
```

## Resilience experiments
1. **Accepted response lost:** reset the client connection after the service accepts a request; retry with the same idempotency key; assert one transfer reference and one debit.
2. **Duplicate concurrent retries:** issue the same request concurrently using the same key; assert all accepted responses share a reference and ledger debit occurs once.
3. **Synthetic capacity exhausted:** submit beyond the available demo balance; assert deterministic `409`, bounded latency, and no negative balance.
4. **Dependency unavailable:** in a service-backed extension, stop the ledger adapter; assert bounded timeout, no success-shaped fallback, and traceable error evidence.

## Exit criteria
- `http_req_failed < 1%` and transfer latency p95 < 500 ms for the local baseline.
- No duplicate accepted transfer for any idempotency key.
- No negative balance or untraceable failure in the resilience cases.
- These local thresholds are demonstration targets, not production SLO commitments.

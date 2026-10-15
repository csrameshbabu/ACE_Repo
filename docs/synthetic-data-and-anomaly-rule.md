# Synthetic Data Manifest and Anomaly Rule

The versioned fixture manifest is `src/test/resources/data/synthetic-banking-manifest.json`; the sample records are `src/test/resources/data/synthetic-transfers.csv`. They use deterministic `SYN-` identifiers and synthetic-only values. No production-shaped customer data is copied.

## Quality rule
`SyntheticDataAnomalyDetector` flags:
- an idempotency key reused with a different request fingerprint;
- a missing or non-positive transfer amount;
- a missing or negative post-transfer balance.

The detector has a focused unit test demonstrating conflicting key reuse and a negative balance. Dataset changes should increment the manifest version and preserve its privacy and financial invariants.

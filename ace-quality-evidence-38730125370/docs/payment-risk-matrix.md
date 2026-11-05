# Payment Risk Matrix

| Flow | Impact | Likelihood | Controls | Residual risk |
| --- | --- | --- | --- | --- |
| Beneficiary management | High | Medium | Eligibility checks, OTP, duplicate detection, review for unusual patterns | Low after controls |
| Funds transfer | Critical | Medium | Balance validation, idempotency keys, replay protection, reversal, concurrent transfer tests | Medium after controls |
| AI policy assistant | Medium | Medium | Citation checks, abstention, allow-list tools, approval for money-changing actions | Low after controls |

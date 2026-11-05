---
name: quality-evidence-review
description: Review ACE quality evidence, test coverage, contracts, AI assurance, and release controls against documented banking risks
---

# ACE quality evidence review

Use this skill when the user asks whether a quality or assurance claim is
supported by the repository.

## Review method

1. Start with `README.md`, `capstone_requirements_and_plan.md`, and the relevant
   document in `docs/`.
2. Trace each claim to executable code, a contract, a test, a generated
   report, or a clearly marked production assumption.
3. Distinguish implemented evidence from design intent and unexecuted plans.
4. Check critical transfer and agentic-tool controls: synthetic data,
   authorization, approval separation, argument validation, idempotency,
   auditability, trace correlation, and deterministic fallback.
5. Check AI claims for golden data, groundedness, safety, red-team coverage,
   human adjudication, and reproducibility.
6. Run targeted Maven tests when the claim depends on executable behavior.

## Output

Return a concise table with:

- requirement or risk
- evidence location
- executable validation
- gap or residual risk
- owner/action

Never describe a planned document or placeholder as proof that a control is
implemented. State explicitly when evidence is unavailable or production
integration is still required.

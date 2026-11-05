# AI Assurance Pack

## Objectives
- Keep AI answers grounded in approved policy and retrieved evidence.
- Ensure prompt and retrieval changes are regression-tested.
- Prevent unauthorized or unsafe tool invocation.

## Mandatory controls
- Deterministic evaluation on critical policy prompts
- LLM-as-judge rubric for answer quality and groundedness
- Human adjudication for disputed or risky outputs
- Red-team scenarios covering prompt injection, unsafe instruction, and tool misuse

## Evidence model
The platform records:
- prompt and retrieval version
- policy and citation sources used
- tool invocation metadata
- final decision or abstention outcome
- reviewer approval when a tool action changes money movement

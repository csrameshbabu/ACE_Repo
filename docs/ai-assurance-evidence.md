# Prompt/RAG Evaluation Evidence

## Dataset
`src/test/resources/ai/prompt-rag-golden-set.jsonl` contains deterministic synthetic cases for grounded answers, missing evidence, conflicting retrieval, prompt injection, and approval-gated tool use. Each case specifies the expected behavior, required claims/citations, and prohibited behaviors.

## Judge rubric
`src/test/resources/ai/judge-rubric.json` defines five weighted dimensions: groundedness, citation correctness, policy correctness, safe tool behavior, and abstention/conflict handling. Release threshold is a weighted score of 3.5/4, with critical-case minimums, zero-tolerance behaviors, deterministic checks, and human adjudication.

The LLM judge is advisory; deterministic citation, abstention, and tool-call assertions remain independent gates. The repository tests validate the dataset and rubric structure but do not call an external model or claim live model evaluation.

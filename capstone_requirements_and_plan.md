# Capstone Project: Agentic Digital Banking Quality Transformation

## Requirement Summary

This project defines the requirement for a production-ready enterprise quality architecture for a digital banking platform that is being modernized with Java/Spring Boot microservices, mobile applications, event-driven integrations, and a new AI banking assistant.

### Business problem
- The platform is modernizing its digital banking services.
- A new AI banking assistant answers policy questions, summarizes service requests, and invokes bounded tools.
- Release cycles are slowed by long regression runs, brittle UI tests, unavailable test data, and weak cross-service diagnostics.

### Business objectives
- Reduce dependable release evidence from days to hours without removing required controls.
- Protect funds transfer, beneficiary, and identity flows with risk-based assurance.
- Establish AI/LLM and agentic testing for the new banking assistant.
- Improve production diagnostics and defect-prevention feedback loops.

### In-scope capabilities
- Customer onboarding and authentication
- Beneficiary management and funds transfer
- Transaction history and notifications
- Mobile and web channels
- AI policy assistant and bounded service-request tools
- CI/CD, Kubernetes, and observability platform

### Stakeholder concerns
- Digital Banking Product Owner: safe, useful, and timely releases
- Payments Domain Lead: transaction integrity and non-repudiation
- Security and Risk: access, privacy, abuse, and audit
- QE Platform Team: reusable automation and environments
- SRE: SLOs, resilience, and incident diagnosis
- Responsible AI Reviewer: groundedness, bias, privacy, and oversight

## Core Requirement

The capstone challenge is to design a production-ready enterprise quality architecture that balances:
- coverage,
- speed,
- reliability,
- security,
- AI assurance,
- governance,
- and cost.

Learners must make explicit assumptions and preserve accountable human decisions for material risks.

## Current-State Problems
- UI suites take 12 hours and contain locator-driven failures.
- Service dependencies are incompletely mapped; defects surface late in end-to-end testing.
- Teams manually copy production-shaped data, creating privacy and repeatability issues.
- The AI assistant has no stable prompt regression, groundedness, or red-team suite.
- Observability exists by team but traces are not connected to tests and releases.
- Release decisions depend on pass-rate summaries instead of residual business risk.

## Target Architecture Requirements

### Quality layers
Unit, component, contract, API, integration, UI, mobile, performance, security, resilience, and accessibility assurance.

### Agentic platform
- Document intake
- Test-design agents
- Orchestration agents
- Analysis and reporting agents
- Governed MCP tools

### AI evaluation
- Golden datasets
- LLM-as-judge rubrics
- Deterministic checks
- Human adjudication
- Prompt/RAG regression

### Data platform
Synthetic customers, accounts, beneficiaries, transfers, reversals, duplicates, and temporal scenarios.

### Observability
- Trace IDs from tests through services, tools, and business events
- SLO and error-budget evidence

### Governance
- Approval controls for payments, privacy, incident response, drift monitoring, and audit

## Core Business Scenarios and Required Assurance

| Scenario | Required Assurance |
|---|---|
| Add beneficiary | Eligibility, OTP, limits, duplicate and delayed activation |
| Funds transfer | Balance, fees, idempotency, reversal, concurrency, and audit |
| Transaction history | Completeness, ordering, filters, and data privacy |
| AI policy Q&A | Grounded answer, correct citations, and abstention |
| Agentic service request | Authorized tool, valid arguments, approval, and evidence |
| Mobile journey | Device/browser coverage, accessibility, and network degradation |

## AI and Agentic Workstream

The project includes the following workstreams:
- Create AGENTS.md standards and reusable skills for contract testing, accessibility, and evidence review.
- Expose approved test-management, repository, and pipeline actions through least-privilege MCP servers.
- Implement locator healing in propose-only mode before controlled runtime use.
- Evaluate prompts, retrieval, groundedness, safety, tool trajectories, and judge bias.
- Use predictive risk for technical artifacts only; never score individual employees.
- Create monitored feedback loops with reviewed labels, shadow evaluation, and rollback.

## Acceptance Criteria

The architecture must ensure:
- Critical golden cases pass deterministic and rubric gates.
- Datasets are synthetic, versioned, reproducible, and privacy reviewed.
- Traceable test-to-service evidence and tested rollback exist.
- Named owners, approved residual risk, and expiring exceptions are established.

## Recommended Execution Plan

### Phase 1: Discover
Assess business risks, architecture, current test maturity, data constraints, and delivery constraints.

### Phase 2: Architect
Create the target QE architecture, standards, toolchain, evidence model, and governance model.

### Phase 3: Build Foundation
Implement the framework skeleton, environments, test data, and CI quality gates.

### Phase 4: Expand Assurance
Add non-functional, AI/LLM, agentic, observability, and risk-based capabilities.

### Phase 5: Validate
Execute representative scenarios, red-team cases, performance and resilience experiments.

### Phase 6: Operationalize
Define SLOs, dashboards, operating model, adoption roadmap, and ROI.

Pilot with funds transfer and the AI policy assistant before extending to remaining domains after evidence-based review.

## Mandatory Deliverables

### Architecture Pack
- Context, container, component, data-flow, trust-boundary, and deployment diagrams

### Enterprise Test Strategy
- Scope, risks, layers, environments, data, entry/exit criteria, and residual-risk reporting

### Automation Framework
- Repository structure, coding standards, reusable libraries, and sample automated journeys

### CI/CD Quality Gates
- Pipeline design with deterministic fallback, evidence publication, and release controls

### AI Assurance Pack
- Prompt/RAG/agent evaluation, rubrics, red-team tests, bias/privacy/drift controls

### Observability Pack
- OpenTelemetry instrumentation, dashboards, SLOs, alerts, and RCA workflow

### Governance Pack
- RACI, standards, audit evidence, exceptions, incident response, and review cadence

### Value Case
- Baseline, targets, benefits, costs, risks, and phased roadmap

### Banking Scenario Pack
- At least twelve functional, failure, AI, and resilience scenarios with traceability

### Payment Risk Matrix
- Impact, likelihood, controls, evidence, and residual risk for critical flows

## Constraints and Assumptions
- Use synthetic banking data only.
- Payment-changing agent actions require human approval.
- Mandatory security and compliance evidence cannot be removed by predictive selection.
- The architecture must support web, mobile, APIs, and asynchronous events.
- A safe deterministic pipeline path must remain available if AI services fail.

## Expected Technical Evidence
- Java/Spring sample API tests and consumer/provider contracts
- Appium or equivalent mobile journey design
- k6/JMeter workload and resilience experiments
- OpenTelemetry trace correlation design
- Prompt/RAG evaluation dataset and judge rubric
- MCP tool contract, authorization, and audit example
- Synthetic data manifest and anomaly-detection rule

## Assessment Rubric

| Dimension | Weight | Evaluation Evidence |
|---|---:|---|
| Enterprise QE architecture | 15% | Layering, traceability, and target-state coherence |
| Automation and CI/CD | 15% | Framework, pipeline, and repeatability |
| AI and agentic assurance | 15% | Evaluation, tools, guardrails, and oversight |
| Non-functional assurance | 15% | Performance, security, resilience, and accessibility |
| Data and observability | 15% | Synthetic data, traces, SLOs, and RCA |
| Governance and value | 15% | Risk, Responsible AI, roadmap, and ROI |

Pass condition: the architecture must be internally consistent, testable, secure, observable, and traceable. A polished document without executable or reviewable evidence is insufficient.

## Review Questions
1. How does the architecture connect business risk to test evidence?
2. Which checks are mandatory regardless of AI recommendations?
3. How are test data privacy and reproducibility protected?
4. How are agent tools authenticated, authorized, and audited?
5. How are prompt regression and groundedness measured?
6. How will self-healing avoid masking defects?
7. How are performance, resilience, security, and accessibility integrated?
8. What happens when a model evaluator or orchestration service is unavailable?
9. How are model drift, bias, and feedback poisoning detected?
10. What measurable outcomes justify the investment?

## Submission Guidance
- Use explicit assumptions and identify missing information.
- Distinguish architecture decisions, implementation evidence, and future roadmap items.
- Use synthetic data only; do not include personal, confidential, or production information.
- Make AI-generated recommendations explainable and subject to deterministic policy.
- Include limitations, unresolved risks, and accountable owners.
- Provide a concise executive presentation supported by the detailed evidence pack.

## Final Architect Takeaway
This capstone is intended to demonstrate how an enterprise Test Architect unifies conventional QE, cloud-native assurance, and governed AI/agentic quality for a high-impact banking platform.

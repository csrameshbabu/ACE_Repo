# OpenTelemetry Trace Correlation Design

## Correlation path
1. A test runner creates a trace context and records `test.run.id`, `test.case.id`, `release.version`, and environment as non-sensitive attributes.
2. The API receives and extracts the W3C `traceparent` header. `TraceCorrelationFilter` starts a server span, continues a valid parent trace, and returns the propagated `traceparent`.
3. The API records `http.request.method`, `url.path`, `http.response.status_code`, and exceptions on the span. No account identifiers, prompts, OTPs, or payment values are span attributes.
4. Each downstream service/client propagates W3C Trace Context and adds service-specific spans. Business events use a generated opaque event/transfer reference to link to the trace without exposing PII.
5. Tool invocations propagate the same trace context and add `agent.tool.name`, `agent.request.id`, and approval outcome; the durable audit record separately retains its governed approval reference.
6. The collector exports spans to the observability backend. CI attaches trace links, test run IDs, commit SHA, and release version to test evidence.

## Demonstration in this repository
- Spring API filter: `TraceCorrelationFilter`
- W3C propagation and local span logging: `OpenTelemetryConfiguration`
- API contract test asserts the response contains a valid traceparent.
- k6 sends a fixed W3C traceparent for load-test correlation.

The logging exporter is a local demonstration. A deployed system should use an OTLP exporter to an authenticated OpenTelemetry Collector, with TLS, retention controls, sampling policy, and attribute redaction configured centrally.

## SLO and evidence join
Join test results and telemetry by `trace_id`, then correlate service span events by opaque business event ID. Preserve required security and compliance suites regardless of risk-based test selection. Alert on error-budget burn and missing trace coverage; do not treat absence of telemetry as success.

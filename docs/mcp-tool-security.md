# MCP Tool Contract, Authorization, and Audit Example

`docs/mcp/transfer-create-tool.json` defines the bounded `transfer.create` tool. The Java reference policy is implemented by `TransferToolService`.

## Security behavior
- Resolve the agent principal from authenticated MCP transport/workload identity; do not trust an `actor` field supplied in tool arguments.
- Require the narrowly scoped `payments:write` authority.
- Require explicit human approval carrying `payments:approve`, an approval reference, and a subject distinct from the initiating agent.
- Reject unexpected arguments and invalid/non-positive values.
- Append an audit event for both successful authorization and denied invocations.
- Audit only opaque synthetic tokens and references; never log full account numbers, credentials, OTP values, or PII.

The included Java service validates and audits a proposed transfer action; it deliberately does not call a payment rail or production ledger. In production, audit events must be durably emitted to the governed audit platform, and approval references must be verified against the bank's approval service rather than trusted from a tool caller.

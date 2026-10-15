package com.ace.banking.agent;

import java.time.Instant;

public record ToolAuditEvent(
        String requestId,
        String toolName,
        String actorSubject,
        String outcome,
        String approvalReference,
        Instant occurredAt
) {
}

package com.ace.banking.agent;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransferToolService {
    private static final String TOOL_NAME = "transfer.create";
    private static final String PAYMENT_WRITE = "payments:write";
    private static final String PAYMENT_APPROVE = "payments:approve";

    private final List<ToolAuditEvent> auditEvents = new CopyOnWriteArrayList<>();

    public ToolResult invoke(
            String requestId,
            AgentPrincipal principal,
            TransferToolArguments arguments,
            HumanApproval approval
    ) {
        String outcome = "DENIED";
        try {
            validate(requestId, principal, arguments, approval);
            outcome = "APPROVED_FOR_DEMONSTRATION";
            return new ToolResult(requestId, outcome, "Synthetic transfer request passed authorization policy.");
        } finally {
            auditEvents.add(new ToolAuditEvent(
                    requestId == null ? "missing-request-id" : requestId,
                    TOOL_NAME,
                    principal == null ? "unknown" : principal.subject(),
                    outcome,
                    approval == null ? null : approval.approvalReference(),
                    Instant.now()
            ));
        }
    }

    public List<ToolAuditEvent> auditEvents() {
        return List.copyOf(auditEvents);
    }

    private void validate(
            String requestId,
            AgentPrincipal principal,
            TransferToolArguments arguments,
            HumanApproval approval
    ) {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("A request ID is required for audit correlation.");
        }
        if (principal == null || !principal.authorities().contains(PAYMENT_WRITE)) {
            throw new SecurityException("The authenticated agent lacks payments:write.");
        }
        if (arguments == null
                || arguments.sourceAccountToken() == null
                || arguments.beneficiaryToken() == null
                || arguments.amount() == null
                || arguments.amount().compareTo(BigDecimal.ZERO) <= 0
                || !"USD".equals(arguments.currency())
                || arguments.idempotencyKey() == null
                || arguments.idempotencyKey().isBlank()) {
            throw new IllegalArgumentException("Transfer tool arguments are invalid for the synthetic example.");
        }
        if (approval == null
                || !approval.approved()
                || approval.approverSubject() == null
                || approval.approverSubject().equals(principal.subject())
                || approval.approvalReference() == null
                || approval.approvalReference().isBlank()
                || !approval.authorities().contains(PAYMENT_APPROVE)) {
            throw new SecurityException("A distinct authorized human approver is required.");
        }
    }

    public record ToolResult(String requestId, String outcome, String message) {
    }
}

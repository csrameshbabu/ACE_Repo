package com.ace.banking.agent;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransferToolServiceTest {
    private final TransferToolService service = new TransferToolService();

    @Test
    void requiresSeparateAuthorizedHumanApprovalAndAuditsDecision() {
        AgentPrincipal agent = new AgentPrincipal("agent-test-01", Set.of("payments:write"));
        HumanApproval approval = new HumanApproval(
                "reviewer-test-01",
                "APR-SYN-001",
                true,
                Set.of("payments:approve")
        );

        TransferToolService.ToolResult result = service.invoke("req-001", agent, arguments(), approval);

        assertEquals("APPROVED_FOR_DEMONSTRATION", result.outcome());
        assertEquals(1, service.auditEvents().size());
        assertEquals("APR-SYN-001", service.auditEvents().get(0).approvalReference());
    }

    @Test
    void deniesSelfApprovalAndStillAuditsTheAttempt() {
        AgentPrincipal agent = new AgentPrincipal("agent-test-01", Set.of("payments:write"));
        HumanApproval selfApproval = new HumanApproval(
                "agent-test-01",
                "APR-SYN-002",
                true,
                Set.of("payments:approve")
        );

        assertThrows(SecurityException.class,
                () -> service.invoke("req-002", agent, arguments(), selfApproval));
        assertEquals("DENIED", service.auditEvents().get(0).outcome());
    }

    @Test
    void rejectsAgentWithoutWriteAuthority() {
        AgentPrincipal reader = new AgentPrincipal("agent-reader", Set.of("accounts:read"));

        assertThrows(SecurityException.class,
                () -> service.invoke("req-003", reader, arguments(), null));
        assertEquals(1, service.auditEvents().size());
    }

    private TransferToolArguments arguments() {
        return new TransferToolArguments(
                "SYN-CHK-001",
                "SYN-BEN-001",
                new BigDecimal("25.50"),
                "USD",
                "tool-key-001"
        );
    }
}

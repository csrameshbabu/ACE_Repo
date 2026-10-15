package com.ace.banking;

import com.ace.banking.service.ReleaseGovernanceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReleaseGovernanceServiceTest {
    @Test
    void paymentRiskMatrixIncludesCriticalPaymentControls() {
        ReleaseGovernanceService service = new ReleaseGovernanceService();

        assertEquals(3, service.getPaymentRiskMatrix().size());
        assertTrue(service.getPaymentRiskMatrix().stream().anyMatch(item -> item.flow().equals("Funds transfer")));
        assertTrue(service.getPaymentRiskMatrix().stream().anyMatch(item -> item.residualRisk().contains("Medium")));
    }
}

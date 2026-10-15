package com.ace.banking;

import com.ace.banking.service.AiAssuranceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AiAssuranceServiceTest {
    @Test
    void groundedAnswersRequireCitations() {
        AiAssuranceService service = new AiAssuranceService();

        assertTrue(service.isGroundedAnswer("Approved answer based on policy", "Policy 1, Policy 2"));
        assertFalse(service.isGroundedAnswer("Answer without evidence", ""));
        assertFalse(service.isGroundedAnswer("", "Policy 1"));
    }

    @Test
    void promptSafetyScoreUsesDeterministicPassRatio() {
        AiAssuranceService service = new AiAssuranceService();

        assertEquals(80.0, service.evaluatePromptSafetyScore(8, 10), 0.0001);
        assertEquals(0.0, service.evaluatePromptSafetyScore(0, 0), 0.0001);
    }
}

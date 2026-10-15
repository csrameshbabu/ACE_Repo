package com.ace.banking.service;

import java.util.List;

public class AiAssuranceService {
    public List<String> getRequiredChecks() {
        return List.of(
                "Groundedness against approved policy and documentation",
                "Citations and answer traceability for each response",
                "Abstention when evidence is weak or missing",
                "Prompt and retrieval regression evaluation",
                "Red-team testing for prompt injection and unsafe tool arguments",
                "Human approval for payment-changing tool invocations"
        );
    }

    public boolean isGroundedAnswer(String answer, String citations) {
        return answer != null && !answer.isBlank() && citations != null && !citations.isBlank();
    }

    public double evaluatePromptSafetyScore(int passedDeterministicChecks, int totalChecks) {
        if (totalChecks <= 0) {
            return 0.0;
        }
        return (double) passedDeterministicChecks / totalChecks * 100.0;
    }
}

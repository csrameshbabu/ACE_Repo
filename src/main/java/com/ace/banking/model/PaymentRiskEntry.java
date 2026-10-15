package com.ace.banking.model;

public record PaymentRiskEntry(
        String flow,
        String impact,
        String likelihood,
        String controls,
        String evidence,
        String residualRisk,
        String owner
) {
}

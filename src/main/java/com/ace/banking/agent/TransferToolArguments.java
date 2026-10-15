package com.ace.banking.agent;

import java.math.BigDecimal;

public record TransferToolArguments(
        String sourceAccountToken,
        String beneficiaryToken,
        BigDecimal amount,
        String currency,
        String idempotencyKey
) {
}

package com.ace.banking.api;

import java.math.BigDecimal;

public record TransferResponse(
        String transferId,
        String status,
        BigDecimal amount,
        String currency
) {
}

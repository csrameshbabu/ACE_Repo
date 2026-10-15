package com.ace.banking.data;

import java.math.BigDecimal;

public record SyntheticTransferRecord(
        String transferId,
        String idempotencyKey,
        String requestFingerprint,
        BigDecimal amount,
        BigDecimal balanceAfter
) {
}

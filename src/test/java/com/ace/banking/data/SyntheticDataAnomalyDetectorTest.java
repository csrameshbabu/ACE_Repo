package com.ace.banking.data;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SyntheticDataAnomalyDetectorTest {
    private final SyntheticDataAnomalyDetector detector = new SyntheticDataAnomalyDetector();

    @Test
    void flagsConflictingIdempotencyReuseAndNegativeBalance() {
        List<String> anomalies = detector.detect(List.of(
                new SyntheticTransferRecord("SYN-TR-01", "SYN-KEY-01", "SYN-FP-A",
                        new BigDecimal("10.00"), new BigDecimal("90.00")),
                new SyntheticTransferRecord("SYN-TR-02", "SYN-KEY-01", "SYN-FP-B",
                        new BigDecimal("10.00"), new BigDecimal("-1.00"))
        ));

        assertEquals(List.of(
                "IDEMPOTENCY_KEY_REUSED_WITH_DIFFERENT_REQUEST:SYN-KEY-01",
                "NEGATIVE_OR_MISSING_BALANCE:SYN-TR-02"
        ), anomalies);
    }
}

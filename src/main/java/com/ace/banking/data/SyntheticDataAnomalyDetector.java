package com.ace.banking.data;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SyntheticDataAnomalyDetector {
    public List<String> detect(List<SyntheticTransferRecord> records) {
        List<String> anomalies = new ArrayList<>();
        Map<String, String> fingerprintsByIdempotencyKey = new HashMap<>();

        for (SyntheticTransferRecord record : records) {
            String existingFingerprint = fingerprintsByIdempotencyKey.putIfAbsent(
                    record.idempotencyKey(),
                    record.requestFingerprint()
            );
            if (existingFingerprint != null
                    && !existingFingerprint.equals(record.requestFingerprint())) {
                anomalies.add("IDEMPOTENCY_KEY_REUSED_WITH_DIFFERENT_REQUEST:" + record.idempotencyKey());
            }
            if (record.amount() == null || record.amount().compareTo(BigDecimal.ZERO) <= 0) {
                anomalies.add("NON_POSITIVE_TRANSFER_AMOUNT:" + record.transferId());
            }
            if (record.balanceAfter() == null || record.balanceAfter().compareTo(BigDecimal.ZERO) < 0) {
                anomalies.add("NEGATIVE_OR_MISSING_BALANCE:" + record.transferId());
            }
        }
        return List.copyOf(anomalies);
    }
}

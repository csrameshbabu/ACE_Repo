package com.ace.banking.service;

import com.ace.banking.api.TransferRequest;
import com.ace.banking.api.TransferResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SyntheticTransferService {
    private static final String SAMPLE_ACCOUNT = "SYN-CHK-001";
    private static final String SAMPLE_BENEFICIARY = "SYN-BEN-001";
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("2500.00");

    private final Map<String, AcceptedTransfer> acceptedTransfers = new HashMap<>();
    private BigDecimal availableBalance = INITIAL_BALANCE;

    public synchronized TransferResponse createTransfer(TransferRequest request) {
        AcceptedTransfer prior = acceptedTransfers.get(request.idempotencyKey());
        if (prior != null) {
            if (!sameRequest(prior.request(), request)) {
                throw new IdempotencyConflictException(
                        "The idempotency key was already used with a different transfer."
                );
            }
            return prior.response();
        }

        if (!SAMPLE_ACCOUNT.equals(request.fromAccount())
                || !SAMPLE_BENEFICIARY.equals(request.beneficiaryId())
                || !"USD".equals(request.currency())) {
            throw new IllegalArgumentException("Only the documented synthetic USD scenario is enabled.");
        }
        if (availableBalance.compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Synthetic account has insufficient funds.");
        }

        availableBalance = availableBalance.subtract(request.amount());
        TransferResponse accepted = new TransferResponse(
                "TR-" + UUID.randomUUID(),
                "ACCEPTED",
                request.amount(),
                request.currency()
        );
        acceptedTransfers.put(request.idempotencyKey(), new AcceptedTransfer(request, accepted));
        return accepted;
    }

    private boolean sameRequest(TransferRequest left, TransferRequest right) {
        return left.fromAccount().equals(right.fromAccount())
                && left.beneficiaryId().equals(right.beneficiaryId())
                && left.amount().compareTo(right.amount()) == 0
                && left.currency().equals(right.currency());
    }

    private record AcceptedTransfer(TransferRequest request, TransferResponse response) {
    }
}

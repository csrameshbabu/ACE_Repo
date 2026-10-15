package com.ace.banking.api;

import com.ace.banking.service.SyntheticTransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {
    private final SyntheticTransferService transferService;

    public TransferController(SyntheticTransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody TransferRequest request
    ) {
        if (!idempotencyKey.equals(request.idempotencyKey())) {
            throw new IllegalArgumentException("Idempotency-Key header must match the request body.");
        }
        TransferResponse response = transferService.createTransfer(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}

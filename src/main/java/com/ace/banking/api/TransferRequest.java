package com.ace.banking.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank String fromAccount,
        @NotBlank String beneficiaryId,
        @NotNull
        @DecimalMin(value = "0.01")
        @Digits(integer = 12, fraction = 2) BigDecimal amount,
        @NotNull
        @Pattern(regexp = "^[A-Z]{3}$") String currency,
        @NotBlank String idempotencyKey
) {
}

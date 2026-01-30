package com.disougie.virtual_bank;

import jakarta.validation.constraints.NotNull;

public record InvoicePaymentRequest(@NotNull Long id, @NotNull Double amount) {

}

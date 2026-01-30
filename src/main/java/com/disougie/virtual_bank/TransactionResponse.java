package com.disougie.virtual_bank;

public record TransactionResponse(long transaction_id, long invoice_id, double amount) {

}

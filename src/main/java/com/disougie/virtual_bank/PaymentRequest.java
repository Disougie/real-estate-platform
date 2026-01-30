package com.disougie.virtual_bank;

public record PaymentRequest(String owner_name, String type, Double amount) {

}

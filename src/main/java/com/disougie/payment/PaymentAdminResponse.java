package com.disougie.payment;

import java.time.LocalDateTime;

public record PaymentAdminResponse(
		long id,
		String owner,
		String property_title,
		double amount,
		String status,
		LocalDateTime created_at,
		long invoice_id,
		Long transaction_id	
) {

}

package com.disougie.payment;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.disougie.property.PropertyRepository;
import com.disougie.property.entity.Property;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentAdminResponseMapper implements Function<Payment, PaymentAdminResponse>{

	private final PropertyRepository propertyRepository;
	
	@Override
	public PaymentAdminResponse apply(Payment payment) {
		
		String propertyTitle = propertyRepository
				.findById(payment.getProperty_id())
				.orElse(Property.builder().title("Unknown Property").build())
				.getTitle();
		
		return new PaymentAdminResponse(
				payment.getId(), 
				payment.getOwner().getName(), 
				propertyTitle, 
				payment.getAmount(), 
				payment.getStatus().toString(), 
				payment.getCreated_at(), 
				payment.getInvoice_id(), 
				payment.getTransaction_id()
		);
	}

}

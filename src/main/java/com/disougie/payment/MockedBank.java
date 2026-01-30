package com.disougie.payment;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.disougie.app_user.AppUser;
import com.disougie.property.PropertyRepository;
import com.disougie.property.entity.Property;
import com.disougie.virtual_bank.InvoiceService;
import com.disougie.virtual_bank.PaymentRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MockedBank implements PaymentProvider {

	private final PaymentRepository paymentRepository;
	private final InvoiceService invoiceService;
	private final PropertyRepository propertyRepository;

	@Override
	@Async
	public PaymentResponse sendPaymentInformation(Property property, AppUser owner) {
		
		try {
			Payment payment = Payment.builder()
					.owner(owner)
					.property_id(property.getId())
					.amount(5000)
					.status(PaymentStatus.PENDING_PAYMENT)
					.created_at(LocalDateTime.now(ZoneId.of("Africa/Khartoum")))
					.build();
			
			PaymentResponse paymentInvoice = invoiceService.makePaymentInvoice(
					new PaymentRequest(
							owner.getName(), 
							property.getType().name(), 
							payment.getAmount()
							)
					);
			
			payment.setInvoice_id(paymentInvoice.invoice_id());
			
			paymentRepository.save(payment);
			
			return paymentInvoice;
			
		}
		catch(Exception e) {
			propertyRepository.delete(property);
			throw e;
		}

	}

}

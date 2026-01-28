package com.disougie.payment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disougie.exception.ResourceNotFoundException;
import com.disougie.notification.NotificationService;
import com.disougie.property.PropertyRepository;
import com.disougie.property.entity.Property;
import com.disougie.property.entity.PropertyStatus;
import com.disougie.virtual_bank.TransactionResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	
	private final PaymentRepository paymentRepository;
	private final PropertyRepository propertyRepository;
	private final NotificationService notificationService;

	@Transactional
	public void receivePayment(TransactionResponse transaction) {
		
		Payment payment = paymentRepository
				.findByInvoiceId(transaction.invoice_id())
				.orElseThrow(() -> new ResourceNotFoundException("payment not found"));
		
		Property property = propertyRepository
				.findById(payment.getProperty_id())
				.orElseThrow(() -> new ResourceNotFoundException("property not found"));
		
		property.setStatus(PropertyStatus.APPROVED);
		
		propertyRepository.save(property);
		
		payment.setStatus(PaymentStatus.PAID);
		payment.setTransaction_id(transaction.transaction_id());
		
		paymentRepository.save(payment);
		
		notificationService.sendNotification(
				payment.getOwner(), 
				"your property ad fees has been paid"
		);
		
	}

}

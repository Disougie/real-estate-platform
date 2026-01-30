package com.disougie.virtual_bank;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.disougie.exception.ResourceNotFoundException;
import com.disougie.payment.PaymentResponse;
import com.disougie.payment.PaymentService;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceService {
	
	private final InvoiceRepository invoiceRepository;
	private final TransationRepository transationRepository;
	private final PaymentService paymentService;
	
	public InvoiceResponse getInvoice(Long id) {
		
		Invoice invoice = invoiceRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("invoice not exists")
		);
		
		return new InvoiceResponse(
				invoice.getOwner_name(), 
				invoice.getType(), 
				invoice.getAmount()
		);
	}	
	
	public PaymentResponse makePaymentInvoice(PaymentRequest request) {
		
		Invoice invoice = new Invoice(
				null,
				request.owner_name(),
				request.type(),
				request.amount(),
				InvoiceStatus.PENDING_PAYMENT
		);
		
		invoice = invoiceRepository.save(invoice);
		
		return new PaymentResponse(invoice.getId());
	}

	@Transactional
	public Transaction payInvoice(InvoicePaymentRequest request) {
		
		Invoice invoice = invoiceRepository.findById(request.id()).orElseThrow(
				() -> new ResourceNotFoundException("invoice not found")
		);
		
		if(invoice.getAmount() != request.amount()) {
			throw new ConstraintViolationException(
					"The amount tou  want to pay does not equal the invoice amount",
					Set.of()
			);
		}
		
		invoice.setStatus(InvoiceStatus.PAID);
		
		invoice = invoiceRepository.save(invoice);
		
		Transaction transaction = new Transaction(
				null, invoice, LocalDateTime.now(ZoneId.of("Africa/Khartoum"))
		);
		
		transaction = transationRepository.save(transaction);
		
		//TODO: extract the method below to new Async function and later implement messaging queue 
		
		paymentService.receivePayment(
				new TransactionResponse(
						transaction.getId(),
						transaction.getInvoice().getId(),
						transaction.getInvoice().getAmount()
				)
		);
		
		return transaction;
	}
}

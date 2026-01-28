package com.disougie.virtual_bank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.disougie.payment.PaymentResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("bok/api/v1/invoice")
@RequiredArgsConstructor
public class BankOfKhartoumController {
	
	private final InvoiceService invoiceService;
	
	@GetMapping
	public ResponseEntity<InvoiceResponse> getInvoice(@RequestParam Long id){
		InvoiceResponse invoice = invoiceService.getInvoice(id);
		return ResponseEntity.status(HttpStatus.OK).body(invoice);
	}
	
	@PostMapping
	public ResponseEntity<PaymentResponse> makePaymentInvoice(PaymentRequest request){
		PaymentResponse response = invoiceService.makePaymentInvoice(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("pay")
	public ResponseEntity<Transaction> payInvoice(@Validated @RequestBody InvoicePaymentRequest request){
		Transaction transaction = invoiceService.payInvoice(request);
		return ResponseEntity.status(HttpStatus.OK).body(transaction);
	}
}

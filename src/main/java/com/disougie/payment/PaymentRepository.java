package com.disougie.payment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	@Query("from Payment where invoice_id = :id")
	Optional<Payment> findByInvoiceId(@Param("id") Long invoice_id);
	
}

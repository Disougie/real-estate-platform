package com.disougie.payment;

import java.time.LocalDateTime;

import com.disougie.app_user.AppUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", nullable = false)
	private AppUser owner;
	
	@Column(nullable = false)
	private String property_id;
	
	@Column(nullable = false)
	private double amount;
	
	@Column(nullable = false)
	private PaymentStatus status;
	
	@Column(nullable = false)
	private LocalDateTime created_at;
	
	private Long invoice_id;
	
	private Long transaction_id;
	
}

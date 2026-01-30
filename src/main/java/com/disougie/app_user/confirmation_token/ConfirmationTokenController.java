package com.disougie.app_user.confirmation_token;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/token")
@RequiredArgsConstructor
public class ConfirmationTokenController {	
	
	private final ConfirmationTokenService tokenService;
	
	@GetMapping("verify")
	public ResponseEntity<?> verifyToken(@RequestParam String token){
		tokenService.verifyToken(token);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("resend")
	public ResponseEntity<?> resendToken(@RequestParam Long id){
		tokenService.resendToken(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("verify-change")
	public ResponseEntity<?> verifyChangeEmail(@RequestParam String token){
		tokenService.verifyChangeEmail(token);
		return ResponseEntity.noContent().build();
	}
}

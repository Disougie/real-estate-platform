package com.disougie.app_user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class AppUserController {
	
	private final AppUserService appUserService;

	@DeleteMapping
	public ResponseEntity<?> disableAccount(){
		appUserService.disableAccount();
		return ResponseEntity.noContent().build();
	}
	
}

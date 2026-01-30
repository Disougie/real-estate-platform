package com.disougie.saved_property;

import java.util.List;

import org.springframework.stereotype.Service;

import com.disougie.app_user.AppUser;
import com.disougie.property.PropertyResponse;
import com.disougie.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedPropertyService {

	private final SavedPropertyRepository savedPropertyRepository;
	private final SavedPropertyResponseMapper savedPropertyResponseMapper;
	
	public List<PropertyResponse> getMySavedProperties() {
		AppUser currentUser = JwtService.getCurrentUser();
		return savedPropertyRepository
				.findByUser(currentUser)
				.stream()
				.map(savedPropertyResponseMapper)
				.toList();
	}

	public void saveProperty(SavePropertyRequest request) {
		AppUser currentUser = JwtService.getCurrentUser();
		SavedProperty savedProperty = new SavedProperty(
				new SavedPropertyId(
						currentUser.getId(), 
						request.property_id()
				),
				currentUser
		);
		savedPropertyRepository.save(savedProperty);
	}

	public void removeFromSavedProperties(String id) {
		SavedPropertyId savedPropertyId = new SavedPropertyId(
				JwtService.getCurrentUser().getId(), id
		);
		savedPropertyRepository.deleteById(savedPropertyId);
	}

}

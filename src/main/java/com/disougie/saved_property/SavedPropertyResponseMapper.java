package com.disougie.saved_property;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.disougie.property.PropertyRepository;
import com.disougie.property.PropertyResponse;
import com.disougie.property.PropertyResponseMapper;
import com.disougie.property.entity.Property;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SavedPropertyResponseMapper implements Function<SavedProperty, PropertyResponse>{
	
	private final PropertyRepository propertyRepository;
	private final PropertyResponseMapper responseMapper;
	
	@Override
	public PropertyResponse apply(SavedProperty savedProperty) {
		return responseMapper.apply(
				propertyRepository
					.findById(savedProperty.getId().getPropertyId())
					.orElse(new Property())
		);
	}

}

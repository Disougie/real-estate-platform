package com.disougie.property;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.disougie.property.entity.Property;

@Component
public class PropertyResponseMapper implements Function<Property, PropertyResponse> {

	@Override
	public PropertyResponse apply(Property property) {
				
		return new PropertyResponse(
				property.getId(),
				property.getTitle(), 
				property.getOwner_id(), 
				property.getDescription(), 
				property.getPrice(), 
				property.getType(),
				property.getStatus(),
				property.getFeatures(), 
				property.getMapsLocation().getCoordinates(), 
				property.getLocation().getCity(), 
				property.getLocation().getArea(), 
				property.getImages_urls(),
				property.getReview()
		);
	}

	
	
}

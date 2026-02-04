package com.disougie.property;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.disougie.property.entity.Property;

@Component
public class PropertyResponseMapper implements Function<Property, PropertyResponse> {

	@Override
	public PropertyResponse apply(Property property) {
		
		List<String> imagesUrls = property
				.getImages()
				.stream()
				.map(image -> image.getImageUrl())
				.toList();
				
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
				imagesUrls,
				property.getReview()
		);
	}

	
	
}

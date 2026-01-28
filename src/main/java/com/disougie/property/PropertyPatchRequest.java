package com.disougie.property;

import java.util.List;

public record PropertyPatchRequest(
		
		String title,
		String description,
		Double price,
		List<byte[]> images
		
) {
	
}

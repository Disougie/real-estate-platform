package com.disougie.property;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.disougie.app_user.AppUser;
import com.disougie.app_user.AppUserRepository;
import com.disougie.exception.ResourceNotFoundException;
import com.disougie.imagekit.ImageService;
import com.disougie.payment.PaymentProvider;
import com.disougie.payment.PaymentResponse;
import com.disougie.property.entity.Features;
import com.disougie.property.entity.Image;
import com.disougie.property.entity.Location;
import com.disougie.property.entity.Property;
import com.disougie.property.entity.PropertyStatus;
import com.disougie.property.entity.PropertyType;
import com.disougie.property.entity.Review;
import com.disougie.security.JwtService;
import com.disougie.util.PageResponse;
import com.disougie.util.PageResponseMapper;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {
	
	private final PropertyResponseMapper propertyResponseMapper;
	private final PageResponseMapper<PropertyResponse> pageResponseMapper;
	private final PropertyRepository propertyRepository;
	private final AppUserRepository appUserRepository;
	private final PaymentProvider paymentProvider;
	private final ImageService imageService;
	
	
	private void checkUserAuthorization(Long id) {
		
		AppUser owner = appUserRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("owner not found")
		); 
		
		AppUser requestedUser = JwtService.getCurrentUser();
		
		if(owner.getId() != requestedUser.getId()) {
			throw new ConstraintViolationException(
					"your are not authorized to access this property",
					Set.of()
			);
		}
		
	}
	
	public PageResponse<PropertyResponse> getProperties(int page, int size) {
		
		Page<PropertyResponse> pageOfProperties = propertyRepository
				.findAllApprovedProperties(PageRequest.of(page, size))
				.map(propertyResponseMapper);
		
		return pageResponseMapper.apply(pageOfProperties);
		
	}
	

	public PropertyResponse getProperty(String id) {
		
		AppUser owner = JwtService.getCurrentUser();
		Property property = propertyRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("property not found"));
		
		if(property.getOwner_id() != owner.getId()) {
			throw new AccessDeniedException("Not Authorize to view this property");
		}
		
		return propertyResponseMapper.apply(property);
	}
	
	
	public List<Property> getMyProperties(Long id) {
		
		checkUserAuthorization(id);
		
		return propertyRepository.findByOwnerId(id);
	}
	
	public PropertyAdCreationResponse addPropertyAd(PropertyAdPostRequest request) {
		
		AppUser owner = JwtService.getCurrentUser();
		
		List<Image> images  = imageService.uploadImages(request.images());
		
		Property property = Property.builder()
				.owner_id(owner.getId())
				.title(request.title())
				.description(request.description())
				.price(request.price())
				.type(request.type())
				.mapsLocation(new GeoJsonPoint(request.lng(), request.lat()))
				.location(
						new Location(
							request.city(), 
							request.area() 
						)
				)
				.features(
						new Features(
							request.rooms(), 
							request.baths(), 
							request.size()
						)
				)
				.images(images)
				.status(PropertyStatus.PENDING_PAYMENT)
				.build();
		
		property = propertyRepository.save(property);
		
		PaymentResponse paymentResponse = paymentProvider
				.sendPaymentInformation(property, owner);
		
		return new PropertyAdCreationResponse(
				property.getId(),
				paymentResponse.invoice_id()
		);
	}
	
	public void deletePropertyAd(String id) {
		
		Property property = propertyRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Property not found")
		);
		
		checkUserAuthorization(property.getOwner_id());
		
		propertyRepository.delete(property);
		
	}

	public void changePropertyAd(String id, PropertyPatchRequest request) {
		
		Property property = propertyRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Property not found")
		);
		
		checkUserAuthorization(property.getOwner_id());
		
		if(request.title() != null)
			property.setTitle(request.title());
		
		if(request.description() != null)
			property.setDescription(request.description());
		
		if(request.price() != null)
			property.setPrice(request.price());
		
		if(request.images() != null) {
			List<String> filesId = property
					.getImages()
					.stream()
					.map(image -> image.getFileId())
					.toList();
			
			for(String fileId: filesId)
				imageService.deleteImage(fileId);
			
			List<Image> newImages = imageService.uploadImages(request.images());
			property.setImages(newImages);
		}
		
		propertyRepository.save(property);
		
	}

	public PageResponse<PropertyResponse> searchByText(String text, int page, int size) {
		
		Page<PropertyResponse> response = propertyRepository
				.findByText(text, page, size)
				.map(propertyResponseMapper);
		
		return pageResponseMapper.apply(response);
	}

	public PageResponse<PropertyResponse> searchByFilters(PropertyType type,
			String city, String area, Integer minRooms, Integer maxRooms,Integer minBaths,
			Integer maxBaths, Double minPrice, Integer maxPrice, Integer minSize,
			Integer maxSize, int page, int size
	) {
		Page<PropertyResponse> response = propertyRepository
				.findByFilters(
					type, city, area, minRooms, maxRooms, 
					minBaths, maxBaths, minPrice, maxPrice, 
					minSize, maxSize, page, size
				)
				.map(propertyResponseMapper);
		
		return pageResponseMapper.apply(response);
	}

	public List<PropertyResponse> searchByCoordinates(double lng, double lat, Double maxDistance) {
		if(maxDistance == null)
			maxDistance = 1.0;
		return propertyRepository.findNearByCoordinates(lng, lat, maxDistance)
			.stream()
			.map(propertyResponseMapper)
			.toList();
	}

	public void ratePropertyAd(String id, ReviewRequest request) {
		
		Property property = propertyRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("property not found")
		);
		
		if(!property.getStatus().equals(PropertyStatus.APPROVED)) {
			throw new AccessDeniedException("not allowed to review this property");
		}
		
		if(property.getReview() == null)
			property.setReview(new Review(request.stars()));
		else
			property.getReview().mergeReviews(new Review(request.stars()));
		
		propertyRepository.save(property);
		
	}

}

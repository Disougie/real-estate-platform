package com.disougie.property;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.disougie.property.entity.Property;

@Repository
public interface PropertyRepository extends MongoRepository<Property, String>, SearchRepository {
	
	@Query("{status: APPROVED}")
	Page<Property> findAllApprovedProperties(Pageable pageable);
	
	@Query("{owner_id : ?0}")
	List<Property> findByOwnerId(Long owner_id);
}

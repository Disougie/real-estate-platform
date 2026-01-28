package com.disougie.property.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Setter
@Getter
public class Review {
	
	private double stars;
	private int no_of_review ;
	
	public Review(double stars) {
		this.stars = stars;
		this.no_of_review = 1;
	}
	
	public void mergeReviews(Review review) {
		double curReviewsStars = this.stars * no_of_review;
		this.no_of_review += 1;
		this.stars =  (curReviewsStars + review.stars) / no_of_review ;
	}
	
}

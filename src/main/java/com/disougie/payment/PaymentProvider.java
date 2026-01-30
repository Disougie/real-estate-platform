package com.disougie.payment;

import com.disougie.app_user.AppUser;
import com.disougie.property.entity.Property;

public interface PaymentProvider {
	
	PaymentResponse sendPaymentInformation(Property property, AppUser owner);

}

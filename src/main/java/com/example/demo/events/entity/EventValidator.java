package com.example.demo.events.entity;

import org.springframework.validation.Errors;

public class EventValidator {
	public void validate(EventDto eventDto, Errors errors) {
		if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
			errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
			errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
			
			// Global error
			errors.reject("wrongPrices", "Values to prices are wrong");
		}
		
		if(eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEventDateTime()) ||
				eventDto.getEndEventDateTime().isBefore(eventDto.getCloseEnrollmentDateTime()) ||
				eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime())
				) {
			errors.rejectValue("endEventDateTime", "wrongValue", "EndEventDateTime is wrong");
		}
		
	}
}

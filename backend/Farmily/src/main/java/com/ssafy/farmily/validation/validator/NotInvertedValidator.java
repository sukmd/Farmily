package com.ssafy.farmily.validation.validator;

import com.ssafy.farmily.utils.DateRange;
import com.ssafy.farmily.validation.annotation.NotInverted;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class NotInvertedValidator
	implements ConstraintValidator<NotInverted, DateRange>
{
	@Override
	public boolean isValid(DateRange dateRange, ConstraintValidatorContext constraintValidatorContext) {
		return !dateRange.getPeriod().isNegative();
	}
}

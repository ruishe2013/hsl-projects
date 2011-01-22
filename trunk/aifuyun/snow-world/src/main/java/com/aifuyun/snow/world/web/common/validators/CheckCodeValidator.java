package com.aifuyun.snow.world.web.common.validators;

import com.aifuyun.snow.world.biz.bo.captcha.CheckCodeService;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.splist.core.form.validator.AbstractValidator;

public class CheckCodeValidator extends AbstractValidator {

	@Override
	protected boolean validate(String fieldValue) {
		CheckCodeService checkCodeService = (CheckCodeService)SplistContext.getSplistBeanFactory().getBean("checkCodeService");
		return checkCodeService.check(fieldValue);
	}

}

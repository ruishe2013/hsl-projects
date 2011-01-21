package com.aifuyun.snow.world.biz.bo.captcha.impl;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.aifuyun.snow.world.biz.bo.captcha.BOException;
import com.aifuyun.snow.world.biz.bo.captcha.ValidateService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.zjuh.sweet.lang.StringUtil;

public class ValidateServiceImpl implements ValidateService {

	private ImageCaptchaService imageCaptchaService;
	
	private static final String DEFAULT_CAPTCHA_IMAGE_FORMAT = "jpeg";
	
	private String imageFormat = DEFAULT_CAPTCHA_IMAGE_FORMAT;
	
	@Override
	public boolean check(String sessionId, String value) {
		if (StringUtil.isEmpty(value)) {
			return false;
		}
		String question = imageCaptchaService.getQuestionForID(sessionId);
		if (StringUtil.isEmpty(question)) {
			return false;
		}
		return value.equalsIgnoreCase(question);
	}

	@Override
	public void generate(String sessionId, OutputStream os) {
		try {
			BufferedImage bi = imageCaptchaService.getImageChallengeForID(sessionId);
			ImageIO.write(bi, imageFormat, os);
			os.flush();
		} catch (Exception e) {
			throw new BOException("生成验证码出错", e);
		}
	}

	public void setImageCaptchaService(ImageCaptchaService imageCaptchaService) {
		this.imageCaptchaService = imageCaptchaService;
	}

	public String getImageFormat() {
		return imageFormat;
	}

	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

}

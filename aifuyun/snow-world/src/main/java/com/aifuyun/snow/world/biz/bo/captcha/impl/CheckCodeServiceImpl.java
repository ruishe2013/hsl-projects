package com.aifuyun.snow.world.biz.bo.captcha.impl;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.bo.captcha.BOException;
import com.aifuyun.snow.world.biz.bo.captcha.CheckCodeService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.sweet.lang.StringUtil;

public class CheckCodeServiceImpl implements CheckCodeService {

	private static final String DEFAULT_CAPTCHA_IMAGE_FORMAT = "jpeg";
	
	private static final Logger log = LoggerFactory.getLogger(CheckCodeServiceImpl.class);
	
	private ImageCaptchaService imageCaptchaService;
	
	private String imageFormat = DEFAULT_CAPTCHA_IMAGE_FORMAT;
	
	@Override
	public boolean check(String value) {
		return check(SplistContext.getSession().getId(), value);
	}

	@Override
	public boolean check(String sessionId, String value) {
		if (StringUtil.isEmpty(value)) {
			return false;
		}
		String inputValue = value.toLowerCase();
		try {
			return imageCaptchaService.validateResponseForID(sessionId, inputValue);
		} catch (Exception e) {
			log.error("校验验证码错误", e);
			return false;
		}
	}

	@Override
	public void generateNext(String sessionId, OutputStream os) {
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

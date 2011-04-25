package com.aifuyun.snow.world.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.lang.DateUtil;

/**
 * 百度开放平台数据转换工具类
 * 
 * @author LS
 * 
 */
public class BaiduOpenUtil {

	public static final boolean ENABLE_CHARSET_CONVERT = false;

	public static final String OUTPUT_CHARSET = "gbk";

	public static final int MAX_KEY_LENGTH = 76 / 2;

	public static final int MAX_SHOWURL_LENGTH = 42 / 2;

	public static final int MAX_TITLE_LENGTH = 63 / 2;

	public static final int MAX_CONTENT_LENGTH = 82 / 2;

	public static String getKey(OrderDO order) {
		if (order == null) {
			return charsetConvert("拼车");
		}
		StringBuilder text = new StringBuilder();
		text.append(order.getArriveCity());
		text.append(order.getArriveAddr());
		text.append("拼车");

		return charsetConvert(intercept(text.toString(), MAX_KEY_LENGTH, false));
	}

	public static String getTitle(OrderDO order) {
		if (order == null) {
			return charsetConvert("一辆车");
		}
		StringBuilder text = new StringBuilder();
		text.append(order.getFromCity());
		text.append(order.getFromAddr());
		text.append("至");
		text.append(order.getArriveCity());
		text.append(order.getArriveAddr());
		text.append(" ");
		text.append(order.getTogetherTypeEnum().getName());
		text.append(" 一辆车拼车网");

		return charsetConvert(intercept(text.toString(), MAX_TITLE_LENGTH, false));
	}

	public static String getArriveDetail(OrderDO order) {
		if (order == null) {
			return charsetConvert(" ");
		}
		StringBuilder text = new StringBuilder();
		text.append("目的地：");
		text.append(order.getArriveCity());
		text.append(order.getArriveAddr());
		text.append(" 预计到达时间：");
		text.append(DateTimeUtil.date2String(order.getArriveTime()));

		return charsetConvert(intercept(text.toString(), MAX_CONTENT_LENGTH, false));
	}

	public static String getFromDetail(OrderDO order) {
		if (order == null) {
			return charsetConvert(" ");
		}
		StringBuilder text = new StringBuilder();
		text.append("出发地：");
		text.append(order.getFromCity());
		text.append(order.getFromAddr());
		text.append(" 出发时间：");
		text.append(DateTimeUtil.date2String(order.getFromTime()));

		return charsetConvert(intercept(text.toString(), MAX_CONTENT_LENGTH, false));
	}

	public static String getStatusDetail(OrderDO order) {
		if (order == null) {
			return charsetConvert(" ");
		}
		StringBuilder text = new StringBuilder();
		text.append("座位数：");
		text.append(order.getTotalSeats());
		text.append(" 创建时间：");
		text.append(DateUtil.formatFullDate(order.getGmtCreate()));

		return charsetConvert(intercept(text.toString(), MAX_CONTENT_LENGTH, false));
	}

	public static String getShowurl(String url) {
		if (StringUtils.isBlank(url)) {
			return "";
		}
		int index = url.indexOf("http://");
		if (index >= 0) {
			url = url.substring(index + 7);
		}
		return charsetConvert(intercept(url, MAX_SHOWURL_LENGTH, false));
	}

	/**
	 * 字符集转换
	 * 
	 * @param text
	 * @return
	 */
	public static String charsetConvert(String text) {
		if (ENABLE_CHARSET_CONVERT) {
			try {
				return new String(text.getBytes(OUTPUT_CHARSET), "iso-8859-1");
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		} else {
			return text;
		}
	}

	/**
	 * 长度截取
	 * 
	 * @param text
	 * @param length
	 * @param appendEllipsis
	 * @return
	 */
	public static String intercept(String text, int length, boolean appendEllipsis) {
		if (text == null) {
			return "";
		}

		if (text.length() > length) {
			if (appendEllipsis) {
				text = text.substring(0, length - 4) + "...";
			} else {
				text = text.substring(0, length);
			}
		}
		return text;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String test = "abc一辆车<>";
		System.out.println(BaiduOpenUtil.charsetConvert(test));

		String url = "http://www.dianping.com/shop/2218060";
		System.out.println(BaiduOpenUtil.getShowurl(url));

		System.out.println(new String(StringEscapeUtils.escapeXml(test).getBytes("iso8859-1"), "gb2312"));
	}
}

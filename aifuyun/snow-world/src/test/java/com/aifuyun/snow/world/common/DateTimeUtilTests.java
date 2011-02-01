package com.aifuyun.snow.world.common;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DateTimeUtilTests extends TestCase {
	
	public void testComponentDateAndTime() throws ParseException {
		Date date1 = DateTimeUtil.parseDate("2010-12-25 12:34:56");
		Date date2 = DateTimeUtil.parseDate("1999-08-13 07:23:44");
		Date date3 = DateTimeUtil.componentDateAndTime(date1, date2);
		Assert.assertEquals(DateTimeUtil.parseDate("2010-12-25 07:23:44"), date3);
		Date date4 = DateTimeUtil.componentDateAndTime(date2, date1);
		Assert.assertEquals(DateTimeUtil.parseDate("1999-08-13 12:34:56"), date4);
	}

}

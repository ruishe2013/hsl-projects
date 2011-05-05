package com.zjuh.xiaobai.sync;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class ConfigServiceTest extends TestCase {
	
	public void testXX() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		ConfigService c = new ConfigService();
		c.parseFromXml();
	}

}

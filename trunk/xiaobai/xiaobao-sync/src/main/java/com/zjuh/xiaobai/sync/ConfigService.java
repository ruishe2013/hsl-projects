package com.zjuh.xiaobai.sync;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zjuh.xiaobai.XPathDemo;

public class ConfigService {
	
	private String configFileName = "db_config.xml";
	
	private Config config = null;
	
	public Config loadConfig() {
		if (config != null) {
			return config;
		}
		config = new Config();
		
		return config;
	}
	
	protected Config parseFromXml() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    domFactory.setNamespaceAware(true); // never forget this!
	    DocumentBuilder builder = domFactory.newDocumentBuilder();
	    InputStream is = XPathDemo.class.getClassLoader().getResourceAsStream(configFileName);
	    Document doc = builder.parse(is);
	    
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    
	    Config ret = new Config();
	    ret.setLogDir(getString(doc, "/config/@logDir", xpath));
	    ret.setScanDelay(getInt(doc, "/config/@scanDelay", xpath));
	    ret.setReConnectDb(getInt(doc, "/config/@reConnectDb", xpath));
	    ret.setStartDelay(getInt(doc, "/config/@startDelay", xpath));
	    
	    Node srcNode = (Node)xpath.evaluate("/config/db_config[@id='src']", doc, XPathConstants.NODE);
	    DbConfig srcDbConfig = new DbConfig();
	    srcDbConfig.setIp(getString(srcNode, "//@ip", xpath));
	    srcDbConfig.setDbName(getString(srcNode, "//@dbName", xpath));
	    srcDbConfig.setUser(getString(srcNode, "//@user", xpath));
	    srcDbConfig.setPwd(getString(srcNode, "//@pwd", xpath));
	    
	    NodeList tableNodeList = (NodeList)xpath.evaluate("//table", srcNode, XPathConstants.NODESET);
	    System.out.println(tableNodeList);
	    //TODO
	    return ret;
	}
	
	
	private String getString(Node doc, String expression, XPath xpath) throws XPathExpressionException {
		return (String)xpath.evaluate(expression, doc, XPathConstants.STRING);
	}
	
	private int getInt(Node doc, String expression, XPath xpath) throws XPathExpressionException {
		return ((Number)xpath.evaluate(expression, doc, XPathConstants.NUMBER)).intValue();
	}

}

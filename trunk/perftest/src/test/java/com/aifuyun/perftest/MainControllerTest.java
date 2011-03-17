package com.aifuyun.perftest;

public class MainControllerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainController mainController = new MainController();
		Config config = new Config();
		config.setThreadCount(10);
		config.addUrl("http://www.taobao.com");
		config.addUrl("http://www.qq.com");
		
		mainController.init(config);		
		mainController.testPerf();

	}

}

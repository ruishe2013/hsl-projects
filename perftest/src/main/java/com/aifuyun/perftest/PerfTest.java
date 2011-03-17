package com.aifuyun.perftest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PerfTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length <= 0) {
			System.out.println("usage:\r\n" +
					"\tjava PerfTest config.txt" +
					"");
			return;
		}
		String configFile = args[0];
		Config config = readConfig(configFile);
		if (config == null) {
			return;
		}
		
		MainController mainController = new MainController();
		mainController.init(config);
		
		System.out.println("start test ======================================>");
		
		mainController.testPerf();
		
	}
	
	private static Config readConfig(String configFile) throws IOException {
		Config config = new Config();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(configFile));
			String threadCountString = reader.readLine();
			String onTimesString = reader.readLine();
			int threadCount = 10;
			int onTimes = 10;
			try {
				threadCount = Integer.parseInt(threadCountString);
			} catch (Exception e) {
			}
			try {
				onTimes = Integer.parseInt(onTimesString);
			} catch (Exception e) {
			}
			String url = null;
			while ((url = reader.readLine()) != null) {
				config.addUrl(url);
			}
			
			config.setThreadCount(threadCount);
			config.setOnTimes(onTimes);
			
			
			System.out.println("threadCount: " + threadCount);
			System.out.println("onTimes: " + onTimes + " second.");
			System.out.println("test urls:");
			for (String u : config.getUrls()) {
				System.out.println(u);
			}
			return config;
		} catch (FileNotFoundException e) {
			System.out.println("file not found: " + configFile);
			return null;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		
	}

}

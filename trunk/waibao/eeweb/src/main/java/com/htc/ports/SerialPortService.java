package com.htc.ports;

import java.io.IOException;
import java.util.List;

import com.htc.model.seriaPort.Level_Final_Serial;

public class SerialPortService implements PortService {

	private Level_Final_Serial level_Final_Serial = new Level_Final_Serial();
	
	private PortServiceValues portServiceValues = new SerialServiceValues();
	
	@Override
	public PortServiceValues getPortServiceValues() {
		return portServiceValues;
	}

	@Override
	public void closePort() {
		level_Final_Serial.closePort();
	}

	@Override
	public List<String> getAllComPorts() {
		return Level_Final_Serial.getAllComPorts();
	}

	@Override
	public int getFrameInterval(int appendMillsec, int dataLen, int baudrate) {
		return Level_Final_Serial.getFrameInterval(appendMillsec, dataLen, baudrate);
	}

	@Override
	public boolean initialize(int timeOut, int baudrate, int dataBits, int stopBits, int parity) {
		return level_Final_Serial.initialize(timeOut, baudrate, dataBits, stopBits, parity);
	}

	@Override
	public boolean isPortOpen() {
		return level_Final_Serial.isPortOpen();
	}

	@Override
	public char[] readPackData() throws Exception {
		return level_Final_Serial.readPackData();
	}

	@Override
	public void setAppname(String appname) {
		level_Final_Serial.setAppname(appname);
	}

	@Override
	public void setPortName(int portName) {
		level_Final_Serial.setPortName(portName);
	}

	@Override
	public void setPortName(String portName) {
		level_Final_Serial.setPortName(portName);
	}

	@Override
	public void writePort(char[] bytes) throws IOException {
		level_Final_Serial.writePort(bytes);
	}

	@Override
	public void writePort(char b) throws IOException {
		level_Final_Serial.writePort(b);
	}

}

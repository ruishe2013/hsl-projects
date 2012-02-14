package com.htc.ports;

import java.io.IOException;
import java.util.List;

public interface PortService {

	PortServiceValues getPortServiceValues();
	
	/**
	 * @describe: 设置 串口程序名
	 * @date:2010-3-2
	 */
	public void setAppname(String appname);

	/**
	 * @describe: 设置串口字符串
	 * @date:2009-11-5
	 */
	public void setPortName(int portName);
	
	public void setPortName(String portName);

	/**
	 * @describe: 返回串口是否打开
	 * @return true : 打开 false: 没有打开
	 * @date:2009-11-5 
	 */
	public boolean isPortOpen();

	/**
	 * @describe: 获取需要帧之间需要间隔的时间(毫秒) 功能公式(1*12(位)*数据长度*1000/波特率 + 附加毫秒数)
	 * @param appendMillsec 附加毫秒数
	 * @param dataLen 数据区数据长度
	 * @param baudrate 波特率
	 * @return 得到合适的帧发送,间隔毫秒数
	 * @date:2009-11-5
	 */
	public int getFrameInterval(int appendMillsec, int dataLen, int baudrate);

	/**
	 * @describe: 列举全部串口名称	
	 * @date:2009-11-22
	 */
	public List<String> getAllComPorts();

	/**
	 * @describe: 初始化串口详细信息
	 * @return true : 初始化串口成功 false: 初始化串口失败 
	 * @date:2009-11-5
	 */
	public boolean initialize(int timeOut, int baudrate, int dataBits, int stopBits, int parity);

	/**
	 * @describe: 读取串口数据
	 * @date:2009-11-5
	 */
	public char[] readPackData() throws Exception;

	/**
	 * @describe: 向串口写数据 char[] bytes
	 * @date:2009-11-5
	 */
	public void writePort(char[] bytes) throws IOException;

	/**
	 * @describe: 向串口写数据 char bytes
	 * @date:2009-11-5
	 */
	public void writePort(char b) throws IOException;

	/**
	 * @throws IOException
	 * @describe: 关闭串口,释放资源
	 * @date:2009-11-5
	 */
	public void closePort();

}

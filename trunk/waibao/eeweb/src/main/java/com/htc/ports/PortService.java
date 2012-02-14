package com.htc.ports;

import java.io.IOException;
import java.util.List;

public interface PortService {

	PortServiceValues getPortServiceValues();
	
	/**
	 * @describe: ���� ���ڳ�����
	 * @date:2010-3-2
	 */
	public void setAppname(String appname);

	/**
	 * @describe: ���ô����ַ���
	 * @date:2009-11-5
	 */
	public void setPortName(int portName);
	
	public void setPortName(String portName);

	/**
	 * @describe: ���ش����Ƿ��
	 * @return true : �� false: û�д�
	 * @date:2009-11-5 
	 */
	public boolean isPortOpen();

	/**
	 * @describe: ��ȡ��Ҫ֮֡����Ҫ�����ʱ��(����) ���ܹ�ʽ(1*12(λ)*���ݳ���*1000/������ + ���Ӻ�����)
	 * @param appendMillsec ���Ӻ�����
	 * @param dataLen ���������ݳ���
	 * @param baudrate ������
	 * @return �õ����ʵ�֡����,���������
	 * @date:2009-11-5
	 */
	public int getFrameInterval(int appendMillsec, int dataLen, int baudrate);

	/**
	 * @describe: �о�ȫ����������	
	 * @date:2009-11-22
	 */
	public List<String> getAllComPorts();

	/**
	 * @describe: ��ʼ��������ϸ��Ϣ
	 * @return true : ��ʼ�����ڳɹ� false: ��ʼ������ʧ�� 
	 * @date:2009-11-5
	 */
	public boolean initialize(int timeOut, int baudrate, int dataBits, int stopBits, int parity);

	/**
	 * @describe: ��ȡ��������
	 * @date:2009-11-5
	 */
	public char[] readPackData() throws Exception;

	/**
	 * @describe: �򴮿�д���� char[] bytes
	 * @date:2009-11-5
	 */
	public void writePort(char[] bytes) throws IOException;

	/**
	 * @describe: �򴮿�д���� char bytes
	 * @date:2009-11-5
	 */
	public void writePort(char b) throws IOException;

	/**
	 * @throws IOException
	 * @describe: �رմ���,�ͷ���Դ
	 * @date:2009-11-5
	 */
	public void closePort();

}

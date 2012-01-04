package com.htc.model.seriaPort;

/**
 * @ CalcCRC.java
 * ���� : CRC��ع���.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
public class CalcCRC {

	/**
	 * @describe :crc�㷨
	 * @param StartAddress : ��ҪCRCУ���char����
	 * @param len : ��ҪУ��ĳ���
	 * @return : ����CRCУ���Ľ��(2���ֽ�)
	 * @date:2009-11-4
	 */
	private static char CRC16(char[] StartAddress, int len) {
		char crc = (char) 0xffff;
		char u8tmp;
		
		for (int i = 0; i < len; i++) {
			crc ^= StartAddress[i];

			for (int n = 0; n < 8; n++) {
				u8tmp = (char) (crc & 1);
				crc >>= 1;
				crc &= 0x7fff;
				if (u8tmp == 1) {
					crc ^= 0xa001;
				}
			}//end for-i
			
		}//end for-n
		
		return crc;
	}

	/**
	 * @describe: ���͵����ݽ���crcУ��
	 * @param crcValue : ҪcrcУ���char����
	 * @return crcУ���� char ����
	 * @date:2009-11-4
	 */
	public static char[] getCrc16(char[] crcValue){
		char[] sendArray = crcValue;
		int len = sendArray.length;
		int hexRs = CRC16(sendArray, len - 2);
		sendArray[len - 2] = (char) (hexRs & 0xFF);    
		sendArray[len - 1] = (char) ((hexRs >> 8) & 0xFF); 
		return sendArray;
	}
	
	/**
	 * @describe: �Խ������ݽ���crc��飬
	 * @param crcValue ��������
	 * @return ��ȷ��true,����Ϊfalse
	 * @date:2009-11-4
	 */
	public static boolean checkCrc16(char[] crcValue){
		if (crcValue.length < 2) {
			return false;
		}
		
		int hexRs = CRC16(crcValue,crcValue.length - 2);
		char highBit = (char) (hexRs & 0xFF);  
		char lowBit  = (char) ((hexRs >> 8) & 0xFF);
		boolean boolRs = crcValue[crcValue.length-2] == highBit? true:false;
		boolRs = boolRs || crcValue[crcValue.length-1] == lowBit? true:false;
		
		if (!boolRs){
//			System.out.println("-----------start-----------");
//			//System.out.print("����õ���CRC:" + Integer.toHexString(hexRs) + "--");
//			System.out.print("ת����õ���CRC:" + Integer.toHexString(highBit) +":"+ Integer.toHexString(lowBit) + "--");
//			System.out.print("Ŀ��CRC:" + Integer.toHexString(crcValue[crcValue.length-2]));
//			System.out.print(":" + Integer.toHexString(crcValue[crcValue.length-1]));
//			System.out.println();
		}
		return boolRs;
	}
	
//	public static void main (String agrs[]){
//		//01 03 06 16 59 75 75 71 DE 90 50 
//		char [] StartAddress = new char[]{0x01,0x03,0x06,0x16,0x59,0x75,0x75,0x71,0xDE};
//		System.out.println(TestCom.bytesToHexString(StartAddress));
//		
//		StartAddress = CalcCRC.getCrc16(StartAddress);
//		System.out.println(TestCom.bytesToHexString(StartAddress));
//	}
	
}
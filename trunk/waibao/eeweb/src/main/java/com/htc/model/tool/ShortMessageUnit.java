package com.htc.model.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.htc.common.FunctionUnit;
import com.htc.domain.SmsRecord;

public class ShortMessageUnit {
	/****************************************���Ͷ���****************************************************/
	/**
	 * ���� �������ĺ�,�ֻ���,���Ͷ��ŵ�����,������Ҫ����������ַ���
	 * @param centerNo �������ĺ�
	 * @param phoneNo �ֻ���
	 * @param msg ���Ͷ��ŵ�����
	 * @date:2010-4-28
	 */
	public static String code4sendMessage(String centerNo, String phoneNo, String msg){
		/**
		 * ʾ��:0891683108501705F011000B813137185940F80008A71E4F60597DFF0C00680065006C006C006F002E003100320033002100210021 
		 */
		String smsStr = "";
		
		StringBuffer strBuf = new StringBuffer();
		//1.08���������ĵ�ַ���ȼ���2����2(һ����8)
		//strBuf.append("08");(centerNo��:13800571500)
		int centerNolen = (centerNo.length() + (centerNo.length()%2==0?0:1) + 4)/2;// ��֤��ż��
		strBuf.append(formatIntToHexStr(centerNolen));//(һ����8)
		//2.91���������ĺ�������(���Թ̶�����) + 68���й���������(���Թ̶�����)
		strBuf.append("91");
		//3.68���й���������(���Թ̶�����)
		strBuf.append("68");
		//4.3108401505F0�����ŷ������ĺ��� 
		strBuf.append(changeOddEven4Send(centerNo));
		//5.1100�����Ͷ��ŵı��뷽ʽ(���Թ̶�����)
		strBuf.append("1100");
		//6.0B��Ŀ���ֻ����볤�� 
		strBuf.append(formatIntToHexStr(phoneNo.length()));
		//7.81��Ŀ�ĵ�ַ����(���Թ̶�����)
		strBuf.append("81");
		//8.Ŀ���ֻ����� --�����շ�����
		strBuf.append(changeOddEven4Send(phoneNo));
		//9.0008�����������ַ���ʽ�����Թ̶����䣩
		strBuf.append("0008");
		//10.A7�������Թ̶����䣩����Ϣ��Ч����ʱ��
		strBuf.append("A7");
		//11.�������ݳ���(�ֽ���)
		String str = UnicodeZhCN.toUnicode(msg);
		strBuf.append(formatIntToHexStr(str.length()/2));//���ص�unicode�볤�ȳ���2
		//12.���������ַ���UNICODE�루���ݷ������ݽ��б䶯��
		strBuf.append(str);
		//13.���ͽ�����־��ʮ������Ϊ0x1A������ʾ�����������(һ�������Ķ��ŷ�������Ӧ�õ�13,���ﲻ�ü�)
		return strBuf.toString().toUpperCase();
	}
	
	/****************************************���ܶ���****************************************************/
	/**
	 * �ѽ��������ַ��������ɣ��ֻ����룬ʱ�䣬��Ϣ���� 
	 * @param receviceInfo ���յ����ַ���
	 */
	public static SmsRecord receiveMessage(String receviceInfo){
		/**2��ʾ���Աȷ���:
		 * 08 91 68 3108501705F0 04 0D 91683137185940F8 0008 01407241901323 06 607060706070
		 * 08 91 68 3108501755F1 24 05 A10180F6         0008 01408011609323 5A ʡ��...
		 * 08(����������ĵ��ֽ���,һ��������ľ��Ǻ�08*2����)
		 * 91���������ĺ�������(���Թ̶�����) + 68���й���������(���Թ̶�����)
		 * �������ĺ�����żλ����:3108501755F1
		 * 04����24 (PDU����) 
		 * 05(�����˺���ĵ�λ��)
		 * 0180F6(�����˺��������Fȥ��F)---���������Ҫ(���﷢���˾���10086,��˷�����Ӧ�ô�0008������6(���������ͼ�1,��Ϊ����һ��F))������,
		 * 0008(�̶�)
		 * 01408011609323(���Ž��յ�ʱ��-��żλ����)--(10-04-08 11:06:39:32)---���������Ҫ
		 * 06(�������ݲ��ֵ��ֽ���,һ���Ǻ���0x06*2����)---���������Ҫ
		 */
		// ���ڶ��Ž������ʱ����ʾֻ�к�����λ,����:2010,ֻ��ʾ10,����perYear��Ҫ��ֵΪ20		
		String perYear = FunctionUnit.getPerYear();
		
		SmsRecord  bean = new SmsRecord();
		bean.setSmstype(2);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);		
			String str = "";
			int tempInt = 0;
			int index = 0;
			str = receviceInfo.substring(0,2); 	
			tempInt = Integer.parseInt(str, 16);							// ��ȡ�������ĺ�����ռ�ĳ���(����ʾ������ʱtempInt=8)
			index = tempInt * 2 + 4;										// (����ʾ������ʱindex=20)
			str = receviceInfo.substring(index,index+2);					// (����ʾ������ʱ:substring(20,22))
			tempInt = Integer.parseInt(str, 16);							// ��ȡ�����˺�����ռ�ĳ���(����ʾ������ʱtempInt=13)
			tempInt = tempInt + (tempInt%2==0?0:1);							// ��ȡ�ĳ�������������ͼ�1(����ʾ������ʱtempInt=14)
			index = index + 4;												// (����ʾ������ʱindex=24)
			// ��ȡ�������ֻ�����
			str = receviceInfo.substring(index,index+tempInt);				// (����ʾ������ʱ:substring(24,24+14))
			bean.setSmsphone(changeOddEven4Receive(str));					// **ҵ������**�������ֻ�����:(����ʾ������ʱ�ֻ�����Ϊ:8613738195048)
			// ��ȡ���ŷ���ʱ��(0008����14λ)
			index = index + tempInt + 4;									// (����ʾ������ʱindex=24+14+4=42)
			str = receviceInfo.substring(index,index+14);					// (����ʾ������ʱ:substring(42,42+14))
			bean.setSmsrectime(dateFormat.parse(perYear+packup2TimeString(changeOddEven4Receive(str))));// **ҵ������**����ʱ��:(����ʾ������ʱ����ʱ��Ϊ:10-04-27 14:09:31)
			// ��ȡ��������
			index = index + 14;												// (����ʾ������ʱindex=56)
			str = receviceInfo.substring(index,index+2);					// (����ʾ������ʱ:substring(56,58))
			tempInt = Integer.parseInt(str, 16);							// ��ȡ����������ռ�ĳ���(����ʾ������ʱtempInt=06���ֽ�)
			index = index + 2;												// (����ʾ������ʱindex=58)
			str = receviceInfo.substring(index,(index+tempInt*2));			// (����ʾ������ʱ:substring(58,70))
			bean.setSmscontent(UnicodeZhCN.tozhCN(str));					// **ҵ������**�������ݽ���:(����ʾ������ʱ�������ݽ���Ϊ:ǡǡǡ)
		} catch (Exception e) {
			bean = null;
			e.printStackTrace();
		}
		return bean;
	}
	
	/********************************���߷���**************************************/	
	/**
	 * ������:���ж�source�����Ƿ�������,�ǵĻ����ں����F,Ȼ���ٰ�source��żλ����,���ؽ�������ֵ
	 */
	private static String changeOddEven4Send(String source) {
		StringBuffer strBuf = new StringBuffer();
		String code = "";
		source = source + (source.length()%2==0?"":"F");
		int len = source.length();
		int beginIndex = 0;
		while (len > beginIndex) {
			code = source.substring(beginIndex+1, beginIndex+2) + 
			source.substring(beginIndex, beginIndex+1);
			strBuf.append(code);
			beginIndex += 2;
		}
		return strBuf.toString();
	}
	/**
	 * ������:��source��żλ����,���������һλ�����F,��ȥ�����F
	 */
	private static String changeOddEven4Receive(String source) {
		String rsStr = "";
		int len = source.length();
		int beginIndex = 0;
		while (len > beginIndex) {
			rsStr += source.substring(beginIndex+1, beginIndex+2) + 
					 source.substring(beginIndex, beginIndex+1);
			beginIndex += 2;
		}
		rsStr = rsStr.replace("F", "");
		return rsStr;
	}
	
	/**
	 * �ѴӶ����л�ȡ��ʱ���ַ�(��source)��ʽ��ʱ���ַ�<br>
	 * ��:source=10042714093132<br>
	 * ��ʽ����:10-04-27 14:09:31(�����32����)<br>
	 * ������������: source.length()>=12,���򷵻�nullֵ.
	 */
	private static String packup2TimeString(String source) {
		String rsStr = "";
		int len = source.length();
		if (len>=12){
		// ��ʽ:��-��-�� ʱ:��:��
		rsStr = source.substring(0, 2) + "-" +
				source.substring(2, 4) + "-" +
				source.substring(4, 6) + " " +
				source.substring(6, 8) + ":" +
				source.substring(8, 10) + ":" +
				source.substring(10, 12);
		}else{
			rsStr = null;
		}
		return rsStr;
	}
	/**
	 * ������ת����16���Ƹ�ʽ�ַ�,���ת������ַ�ֻռһλ,��ǰ�油0
	 */
	private static String formatIntToHexStr(int source){
		String formatStr = Integer.toHexString(source);
		formatStr = (formatStr.length() == 1?"0":"") + formatStr;
		return formatStr;
	}	
	
//	public static void main(String args[]) {
//		System.out.println(ShortMessageUnit.code4sendMessage("13800571500","13738195048","���죬��ã�hello.123!!!������"));
//		String receviceInfo = "0891683108501705F0040D91683137185940F80008015031412241230854C854C800310031";
//		SmsRecord recevice = ShortMessageUnit.receiveMessage(receviceInfo);
//		System.out.println("��������:" + recevice.getSmsphone());
//		System.out.println("��������:" + recevice.getSmscontent());
//		System.out.println("����ʱ��:" + recevice.getSmsrectime().toLocaleString());
//	}	
}

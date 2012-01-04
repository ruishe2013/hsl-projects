package com.htc.model.seriaPort;

import java.util.*;

import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.ICallNotification;
import org.smslib.IGatewayStatusNotification;
import org.smslib.IInboundMessageNotification;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.IOutboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Library;
import org.smslib.OutboundMessage;
import org.smslib.Service;
import org.smslib.AGateway.GatewayStatuses;
import org.smslib.AGateway.Protocols;
import org.smslib.Message.MessageEncodings;
import org.smslib.Message.MessageTypes;
import org.smslib.modem.SerialModemGateway;

/**
 * ������--���ڲ���
 * 
 * @ SmsService.java
 * ���� : smslib Ӧ��- ���Ͷ���(����Ⱥ��) ����<br>
 * ��ʹ�ý���:	��ʼ��: <code>initial</code><br>
 * 				��������:<code>startService</code>�Ѿ��������Ž����¼�<br>
 *				��������:<code>stopService</code><br>
 *				��ӡ������Ϣ:<code>smsInfo</code><br>
 *				
 *				���Ͷ���: <code>sendMessage</code><br>
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-1     YANGZHONLI       create
 */
public class Smslib_Service {

	private static String SEND_SMS_GROUP = "smsgruop";	// ���ڷ��Ͷ��ŵ�����
	private Service srv; 								// ���ŷ������
	private SerialModemGateway gateway; 				// ����
	private boolean rec_msg_remove = true;				// �յ�,�������֮��,�Ƿ�ɾ����ǰ����.Ĭ��ɾ��
	
	InboundNotification inbound = 
		new InboundNotification(); 						// ���ն��ŵļ���
	OrphanedMessageNotification Orphaned = 
		new OrphanedMessageNotification();				// �ж϶��Ŵ���(���������϶�ʱ,����2�������Ƕ���,��ʱ����õ������)-�����е�����,����Ҫ����

	/**
	 * @describe: ����:�յ�,�������֮��,�Ƿ�ɾ����ǰ����.Ĭ��ɾ��
	 * @param rec_msg_flag: true:ɾ�� false:��ɾ��
	 * @date:2010-3-2
	 */
	public void setRec_msg_remove(boolean rec_msg_remove) {
		this.rec_msg_remove = rec_msg_remove;
	}
		
	/**
	 * @describe: ��ʼ������ģ��
	 * @param com ���ں�
	 * @param baudRate ������
	 * @param pin pinֵ
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-1
	 */
	public boolean initial(String com, int baudRate, String pin) {
		boolean rsbool = true;

		this.srv = new Service();
		this.gateway = new SerialModemGateway("SMSLINK", com,	baudRate, "", "");
		this.gateway.setOutbound(true);
		this.gateway.setInbound(true);
		this.gateway.setProtocol(Protocols.PDU);
		this.gateway.setSimPin(pin);

		try {
			this.srv.addGateway(gateway);
		} catch (GatewayException e) {
			rsbool = false;
			e.printStackTrace();
		}
		return rsbool;
	}

	/**
	 * @describe: 
	 *	���������� 
	 * 	�ڴ���һ�����ڷ��Ͷ��ŵ���(��Ϊ: smsgruop) 
	 *	�۴����¼�����(���պ��ж϶��Ŵ���)
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-1
	 */
	public boolean startService() {
		boolean rsbool = true;
		try {
			this.srv.startService();
			this.srv.createGroup(SEND_SMS_GROUP);
			// ע���������Ž����¼� -- С����
			this.srv.setInboundMessageNotification(inbound);	
			// ע���������Ž����¼� -- �����
			this.srv.setOrphanedMessageNotification(Orphaned);
			// ... ������ע�������¼�
		} catch (Exception e) {
			rsbool = false;
			e.printStackTrace();
		}
		return rsbool;
	}

	/**
	 * @describe: ֹͣ����
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-1
	 */
	public boolean stopService() {
		boolean rsbool = false;
		try {
			if (this.srv != null){
				this.srv.removeGroup(SEND_SMS_GROUP);
				this.srv.stopService();
				rsbool = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsbool;
	}
	
	/**
	 * @describe: ��ӡsms��Ϣ	
	 * @date:2010-3-1
	 */
	public void smsInfo() throws Exception{
		System.out.println();
		System.out.println("smslib Version: " + Library.getLibraryVersion());		
		System.out.println("Modem Information:");
		System.out.println("  Manufacturer: " + gateway.getManufacturer());
		System.out.println("  Model: " + gateway.getModel());
		System.out.println("  Serial No: " + gateway.getSerialNo());
		System.out.println("  SIM IMSI: " + gateway.getImsi());
		System.out.println("  Signal Level: " + gateway.getSignalLevel() + "%");
		System.out.println("  Battery Level: " + gateway.getBatteryLevel() + "%");
		System.out.println("  SmscNumber: " + gateway.getSmscNumber());
		System.out.println();		
	}		

	/**
	 * @describe: ��ָ����һ���ֻ�����,���Ͷ���
	 * @param phoneList �ֻ������б�
	 * @param message ��Ϣ����
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-1
	 */
	public boolean sendMessage(List<String> phoneList, String message) {
		boolean rsbool = true;
		
		// ���ֻ�����������뵽���ŷ�������
		if ((phoneList == null)||(message==null)){
			return rsbool;
		}
		for (String phone : phoneList) {
			this.srv.addToGroup(SEND_SMS_GROUP, phone);
		}
		OutboundMessage msg = new OutboundMessage(SEND_SMS_GROUP, message);
		msg.setEncoding(MessageEncodings.ENCUCS2);
		
		try {
			this.srv.sendMessage(msg);
			
			// ���������,���ֻ���������Ӷ��ŷ��������Ƴ�
			for (String phone : phoneList) {
				this.srv.removeFromGroup(SEND_SMS_GROUP, phone);
			}
		} catch (Exception e) {
			rsbool = false;
			e.printStackTrace();
		}
		return rsbool;
	}
	
	/**
	 * @describe: ��ָ����һ���ֻ�����,���Ͷ���
	 * @param phoneList �ֻ������б�
	 * @param message ��Ϣ����
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-1
     */
	public boolean sendMessage(String phone, String message) {
		boolean rsbool = true;

		OutboundMessage msg = new OutboundMessage(phone, message);
		msg.setEncoding(MessageEncodings.ENCUCS2);
		
		try {
			this.srv.sendMessage(msg);
		} catch (Exception e) {
			rsbool = false;
			e.printStackTrace();
		}
		return rsbool;
	}
	
	/**
	 * ���� : �ն��ŵļ���,��ɾ���յ��Ķ���
	 */
	public class InboundNotification implements IInboundMessageNotification {
		public void process(AGateway gateway, MessageTypes msgType, InboundMessage msg){
			// TODO Auto-generated method stub
			if (msgType == MessageTypes.INBOUND){
				System.out.println(">>>�յ����Ţ� New Inbound message detected from Gateway: " + gateway.getGatewayId());
			}else if (msgType == MessageTypes.STATUSREPORT){
				System.out.println(">>>�յ����Ţ� New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
			}			
			System.out.println(msg);
//			if (msg.getOriginator().equals("10086")){
//				System.out.println("ʱ��:" +  msg.getDate().toLocaleString());
//				System.out.println("��������:" + msg.getText());
//				System.out.println("��������:" + msg.getOriginator());
//			}			
			try {
				if (rec_msg_remove){ // ɾ���յ��Ķ���
					srv.deleteMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void process(String arg0, MessageTypes arg1, InboundMessage arg2) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * ���� : ��������������һ������Ϣ�������Ϣ���Ĳ��֡���ʱ�������ԭ������<br>
	 * 	һ������Ϣ��ĳЩ����û�е�����ģ���������������Ϣ����ȫû���յ���<br>
	 *  ��Щ�¶�����Ϣ�������������ֻ��������ڴ档 <br>
	 *  ������յ�̫��ġ�������������Ϣ���֣���Щ��������Ҫ���ĵ��ƽ�������ڴ� <br> 
	 *  ��Ч�ؽ��ý��������κ�������Ϣ��<br>
	 *  
	 *  ����֪ͨ������Ҫ����<code>true��false</code> �����������true �����ʼ��Ĳ��ֽ���ɾ�� ���Զ��ʧȥ�ĵ��ƽ�����ڴ�<br>
	 */
	public class OrphanedMessageNotification implements	IOrphanedMessageNotification {
		public boolean process(AGateway gateway, InboundMessage msg) {
			// TODO Auto-generated method stub
			System.out.println(">>> ����Ϣ���� Orphaned message part detected from " + gateway.getGatewayId());
			System.out.println(msg);
			// Since we are just testing, return FALSE and keep the orphaned
			// message part.
			return false;
		}
	}
		
	/**
	 * ���� : �����ŵļ��
	 */
	public class OutboundNotification implements IOutboundMessageNotification{
		public void process(AGateway gateway, OutboundMessage msg) {
			// TODO Auto-generated method stub
			System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());
			System.out.println(msg);
		}
	}		
	
	/**
	 * ���� : �ӵ��绰�ļ���
	 */
	public class CallNotification implements ICallNotification {
		public void process(AGateway gateway, String callerId) {
			// TODO Auto-generated method stub
			System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
		}
	}

	/**
	 * ���� : ���ر䶯�ļ���
	 */
	public class GatewayStatusNotification implements IGatewayStatusNotification {
		public void process(AGateway gateway, GatewayStatuses oldStatus, GatewayStatuses newStatus) {
			// TODO Auto-generated method stub
			System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
		}
	}

}

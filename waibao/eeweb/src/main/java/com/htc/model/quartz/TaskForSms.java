package com.htc.model.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.domain.PhoneList;
import com.htc.domain.SmsRecord;
import com.htc.model.MainService;
import com.htc.model.seriaPort.SimCard_Unit;
import com.htc.model.tool.ShortMessageUnit;

/**
 * @ TaskForInstantData.java
 * 作用 : 读取短信作业任务.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//public class TaskForInstantData  implements Job {
public class TaskForSms  extends QuartzJobBean {
	
	private Log log = LogFactory.getLog(TaskForSms.class);
	private CommonDataUnit commonDataUnit;
	private SimCard_Unit simCard_Unit;
	private MainService mainService;
	
	private static int WAIT_MILLTIME = 5 * 1000; 	// 试图判断短信是否接收完整, 单次等待时间(5秒)
	private static int RETRY_TIMES = 3; 			// 试图判断短信是否接收完整, 重复次数(3次)
	
	//注册service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}	
	
	// 需要一个空的构造函数，调度器在调度的时候可以用这个构造函数初始化对象
	public TaskForSms() {
	}

	// 接收短信作业
	public void executeInternal(JobExecutionContext ctx) throws JobExecutionException {	
		// 预备第一步: 短信模块串口对应的串口有没有打开
		if (simCard_Unit.isRunFlag() == false){return;}
		// 预备第二步: 短信报警是否打开(1:关闭 2:打开)
		if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==1){return;}
		// 预备第三步:保证前面不存在正在处理短信的任务
		if (commonDataUnit.isSmsDoingData() == false){				// 正在处理短信任务
			commonDataUnit.setSmsDoingData(true);
		}else{														// 有任务在处理,退出
			log.info("③-0:运行时,前面的短信处理任务还没有结束...");	
			return;
		}
		//log.info("③:进入短信模块任务...");	
		// 预备工作完成,进入正式任务.  
		
		// *******************注意:   要增加短信接收处理功能:取消72行注释,然后在88行后面编写业务逻辑*********************
		
		// ① 读取缓冲里面的内容,等到下面处理 
		//perReadMemory();	// 为了把信息丢失降到最低,预读缓冲区
		String newMemory = simCard_Unit.freeAndReturnMemory();
		
		// ②处理需要发送的短信列队
		String centerNo = commonDataUnit.getSysArgsByKey(BeanForSysArgs.SMS_CENTER_NUMBER);//短信中心号码
		SmsRecord smsRecord = commonDataUnit.pollSimCard(); 
		while (smsRecord != null) {
			//发送短信
			sendMessage(centerNo, smsRecord);
			// 把短信发送信息保存到数据库
			mainService.insertSmsRec(smsRecord);	
			// 下一个需要发送的短信
			smsRecord = commonDataUnit.pollSimCard();
		}
		
		// ③处理缓冲信息-1.如果收到有效的信息->2.接收的短信保存到数据库->3.查询信息->4.发送短信->5.发送的短信保存到数据库
//		if (!newMemory.equals("")){
//			// 	这是一个接收短信显示的串口信息例子
//			//	\r\n+CMT: ,24\r\n0891683108501705F0040D91683137185940F80008015031904243230454C854C8\r\n
//			// System.out.println("newMemory:"+newMemory);
//			List<SmsRecord> smsList = packSmsRecord(newMemory);
//			for (SmsRecord smsrecord : smsList) {
//					// 这里是有效的接收内容,要在这里写具体的业务
//				System.out.println("接收号码:" + smsrecord.getSmsphone());
//				System.out.println("接收内容:" + smsrecord.getSmscontent());
//				System.out.println("接收时间:" + smsrecord.getSmsrectime().toLocaleString());
//				mainService.insertSmsRec(smsrecord);	
//			}
//		}
		
		// 设置可以运行标志
		commonDataUnit.setSmsDoingData(false);
	}
	
	/**
	 * 为了把信息丢失降到最低,预读缓冲区
	 */
	public void perReadMemory(){
		String memorystr = "";
		boolean isEndFlag = false;
		int retrytime = RETRY_TIMES; // 没到结尾,等待次数
		if (!memorystr.equals("")){//只有缓存不为空时,才处理
			while(!isEndFlag){
				try {
					if (retrytime < 1){break;}//超过等待次数,放弃
					memorystr = simCard_Unit.freeAndReturnMemory(2);//读取,但不删除缓存
					isEndFlag =	memorystr.substring(memorystr.length()-2, memorystr.length()).equals("\r\n");// 判断缓冲池里面最后的两个字符是否是\r\n
					if (isEndFlag){break;}
					Thread.sleep(WAIT_MILLTIME);// 发现最后一条短信没有发送完整,等待一段时间
					retrytime--;//等待次数减一
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
		//return memorystr;
	}
	
	public void sendMessage(String centerNo, SmsRecord smsRecord){
		//发送短信
		simCard_Unit.sendMessage(centerNo, smsRecord.getSmsphone(), smsRecord.getSmscontent());		
	}
	
	/**
	 * 把接收到的短信字符,解析成短信接收对象,并进行号码的有效性检测
	 */
	public List<SmsRecord> packSmsRecord(String smsStr){
		List<SmsRecord> smsList = new ArrayList<SmsRecord>();
		try {
			if(null!=smsStr && smsStr.length()>0){
				SmsRecord smsbean = new SmsRecord();
				int head_begin_index,head_middle_index,head_end_index;
				int smslen=0, sms_center_len=0, sms_body_len=0,tem_len=0;
				String head_str="",body_str="",temp_str="";
				
				// smsStr如:+CMT:,30\r0891683108501705F0040D91683137185940F80008015070412442230A53CD53CD590D590D53D1
				temp_str = smsStr.replaceAll(" ", "").replaceAll("\\n", "");			// 去掉所有空格和换行
				while (true) {
					// ****** 获取短信中除短信中心外的字节数
					tem_len = temp_str.length();
					head_begin_index = temp_str.indexOf("+CMT");						// 找到+CMT位置
					if(head_begin_index < 0) {break;}
					head_end_index = temp_str.indexOf("\r",head_begin_index);			// 找到换行符的位置
					if(head_end_index < 0) {break;}
					head_str = temp_str.substring(head_begin_index,head_end_index);		// 获取"+CMT"和"\r"之间的字符串.如:+CMT: ,30\r
					head_middle_index = head_str.lastIndexOf(",");						// 获取head_str中最后一个","
					if(head_middle_index < 0){break;}
					head_middle_index = head_begin_index + head_middle_index + 1;
					sms_body_len = Integer.parseInt(temp_str.substring(head_middle_index, head_end_index));	//得到短信中除短信中心外的字节数
					
					// ******获取短信中心的所占的字节数
					if ( tem_len < (head_end_index + 4) ){break;}						// 判断是否有足够的长度获取目标字节数
					sms_center_len = Integer.parseInt(temp_str.substring(head_end_index+1, head_end_index+3),16);//得到短信中短信中心的字节数
					// ******截取的长度应该=CMT头部长度+短信中心长度提示数字+(短信中心长度+短信内容长度)*2
					smslen = (head_end_index + 1) + 2 + (sms_center_len + sms_body_len)*2;// 判断是否有足够多的字节数来显示短信内容 
					if (tem_len < smslen){break;}
					body_str = temp_str.substring(head_end_index+1, smslen);//一条完整短信内容
					// ******生成一个短信对象
					smsbean = ShortMessageUnit.receiveMessage(body_str);
					if(smsbean != null){
						for (PhoneList phone : commonDataUnit.getPhoneList()) {			// 现有电话号码列表
							if (smsbean.getSmsphone().indexOf(phone.getPhone()) >= 0){	// 遍历现有号码,确定是否是有效是电话号码
								smsbean.setSmsphone(phone.getPhone());
								smsList.add(smsbean);
								break;
							}
						}	
					}
					// 进入下一个循环
					temp_str = temp_str.substring(smslen);
				}// end for while
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smsList;
	}
	
//	public static void main(String args[]) {
//		TaskForSms dd = new TaskForSms();
//		String smsStr = "\r\n+CMT: ,26\r\n0891683108501705F0040D91683137185940F800080150311180242306597D202854C8\r\n";
//		List<SmsRecord>  list = dd.packSmsRecord(smsStr);
//		System.out.println("解析结果个数" + list.size());
//		for (SmsRecord recevice : list) {
//			System.out.println("发件号码:" + recevice.getSmsphone());
//			System.out.println("发件内容:" + recevice.getSmscontent());
//			System.out.println("发件时间:" + recevice.getSmsrectime().toLocaleString());
//		}
//	}

}


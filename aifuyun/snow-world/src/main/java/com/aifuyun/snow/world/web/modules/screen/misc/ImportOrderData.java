package com.aifuyun.snow.world.web.modules.screen.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import org.hsqldb.lib.StringUtil;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.biz.bo.misc.ConfigurationService;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.CarOwnerTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.author.SimpleUser;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.lang.RandomStringUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultCode;

public class ImportOrderData extends BaseScreen {

	private OrderAO orderAO;
	
	private ConfigurationService configurationService;
	
	private static final String LOCK_FILENAME = "process.lock";
	
	private static final String BEGIN_TAG = "<doc>";
	
	private static final String END_TAG = "</doc>";
	
	private static final Random rand = new Random();
	
	private UserBO userBO;
	
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		String dataFilePath = configurationService.getImportOrderDataPath();
		File dirFile = new File(dataFilePath);
		File dir = dirFile.getParentFile();
		if (!tryLock(dir)) {
			templateContext.put("message", "�Ѿ��������ڴ�����...");
			return;
		}
		try {
			List<SimpleUser> users = initRandUser();
			if (CollectionUtil.isEmpty(users)) {
				templateContext.put("message", "�û����ݲ����ڣ��������û����ݣ�");
				return;
			}
			String message = processData(dataFilePath, users);
			if (!StringUtil.isEmpty(message)) {
				templateContext.put("message", message);
				return;
			}
			renameDataFile(dataFilePath);
			templateContext.put("message", "����ɹ�@" + new Date());
		} finally {
			unLock(dir);
		}
	}
	
	private void renameDataFile(String dataFilePath) {
		File dataFile = new File(dataFilePath);
		if (!dataFile.exists()) {
			return;
		}
		String postfix = DateUtil.formatDate(new Date(), "yyyy_MM_dd_HH_mm_ss");
		dataFile.renameTo(new File(dataFilePath + "_" + postfix + "_done"));
	}
	
	private List<SimpleUser> initRandUser() {
		String randomUsersPath = configurationService.getRandomUsersPath();
		File randomUsersDir = new File(randomUsersPath);
		if (!randomUsersDir.exists()) {
			return null;
		}
		List<SimpleUser> ret = CollectionUtil.newArrayList();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(randomUsersDir));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (log.isWarnEnabled()) {
					log.warn(line);
				}
				String username = line;
				BaseUserDO user = userBO.queryByUsername(username);
				if (user == null) {
					log.error("�û������ڣ�" + username);
					continue;
				}
				SimpleUser simpleUser = new SimpleUser();
				simpleUser.setUserId(user.getId()); 
				simpleUser.setUsername(username);
				ret.add(simpleUser);
			}
			
		} catch (IOException e) {
			this.log.error("��ȡ�û�����ʧ��", e);
		}
		return ret;
	}
	
	private String processData(String dataFilePath, List<SimpleUser> users) {
		try {
			File dataFile = new File(dataFilePath);
			if (!dataFile.exists()) {
				return "Ŀ���ļ�������";
			}
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			Stack<String> tags = new Stack<String>();
			String line = null;
			List<String> orderLines = null; 
			while ((line = reader.readLine()) != null) {
				if (StringUtil.isEmpty(line)) {
					continue;
				}
				line = line.trim();
				if (BEGIN_TAG.equals(line)) {
					tags.push(line);
					continue;
				} else if (END_TAG.equals(line)) {
					if (tags.isEmpty()) {
						log.error("δ�ҵ���ʼ��ǩ�����Դ�������");
						orderLines = null; 
						continue;
					}
					String startTag = tags.pop();
					if (!BEGIN_TAG.equals(startTag)) {
						log.error("δ�ҵ���ʼ��ǩ�����Դ�������");
						orderLines = null; 
						continue;
					}
					if (orderLines != null) {
						// �ύ����
						commitData(orderLines, users);
					}
				} else {
					if (orderLines == null) {
						orderLines = CollectionUtil.newArrayList();
					}
					orderLines.add(line);
				}
			}
			reader.close();
			return null;
		} catch (Exception e) {
			this.log.error("��������ʧ��", e);
			return "��������ʧ��";
		}
		
	}
	
	private static String fmts[] = new String[] {
		"yyyy-MM-dd HH:mm:ss",
		"HH:mm:ss",
		"HH:mm",
		"yyyy-MM-dd",
		"HH/mm",
		"HH/mm/ss",
	} ;
	
	private static Date guessDate(String str, Date defaultDate) {
		if (str == null) {
			return defaultDate;
		}
		for (String fmt : fmts) {
			Date d = tryDate(str, fmt);
			if (d != null) {
				return d;
			}
		}
		return defaultDate;
	}
	
	private static Date tryDate(String str, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}
	
	private void commitData(List<String> orderLines, List<SimpleUser> users) {
		Map<String, String> properties = CollectionUtil.newHashMap();
		for (String s : orderLines) {
			int pos = s.indexOf(":");
			if (pos < 0) {
				log.error("û��ð�ŷָ��������Դ������ݣ�" + s);
				continue;
			}
			String name = s.substring(0, pos).trim();
			String value = s.substring(pos + 1).trim();
			properties.put(name, value);
		}
		OrderDO orderDO = new OrderDO();
		OrderUserDO inputCreator = new OrderUserDO();
		
		orderDO.setFromCity(properties.get("���������С�"));
		orderDO.setArriveCity(properties.get("��Ŀ�ĳ��С�"));
		orderDO.setFromAddr(properties.get("����㡿"));
		orderDO.setArriveAddr(properties.get("��Ŀ�ĵء�")); 
		orderDO.setDescription(properties.get("����ע��")); 
		orderDO.setApproach(properties.get("��;����")); 
		orderDO.setFromTime(guessDate(properties.get("������ʱ�䡿"), new Date()));
		orderDO.setAfterWorkFromTime(guessDate(properties.get("���°�ʱ�䡿"), null));
		orderDO.setTotalSeats(3);
		orderDO.setType(randType(properties));
		orderDO.setCreatorCarOwnerType(carOwnerType(properties));
		
		inputCreator.setRealName(properties.get("����ϵ�ˡ�"));
		inputCreator.setBirthYear(BirthYearEnum.YEAR_80S.getValue());
		inputCreator.setPhone(randPhone(properties));
		inputCreator.setSex(randSex(inputCreator.getRealName()).getValue());
		inputCreator.setQq(randQQ(properties));
		inputCreator.setCareer("����");
		
		if (StringUtil.isEmpty(orderDO.getFromCity())) {
			log.warn("�޳�����Ϣ��");
		}
		
		randomUser(users);
		ResultCode resultCode = importOrderData(orderDO, inputCreator);
		if (resultCode != null) {
			log.error("error:" + resultCode.getMessage());
		} else {
			log.warn("�������ݳɹ�:" + inputCreator.getRealName());
			System.out.println("�������ݳɹ�:" + inputCreator.getRealName());
		}
		
	}
	
	private static final String[] PHONE_PREFIX = {"130","132", "131", "135", "136", "137", "138", "158", "159"};
	
	private int carOwnerType(Map<String, String> properties) {
		String typeName = properties.get("��ƴ�����ʡ�");
		CarOwnerTypeEnum carOwnerTypeEnum = CarOwnerTypeEnum.valueOfName(typeName);
		if (carOwnerTypeEnum == null) {
			carOwnerTypeEnum = CarOwnerTypeEnum.CAR_OWNER;
		}
		return carOwnerTypeEnum.getValue();
	}
	
	private String randPhone(Map<String, String> properties) {
		String phone = properties.get("����ϵ�绰��");
		if (!StringUtil.isEmpty(phone)) {
			return phone;
		}
		String prefix = PHONE_PREFIX[rand.nextInt(PHONE_PREFIX.length)];
		return prefix + RandomStringUtil.randomNumeric(8);
	}
	
	private SexEnum randSex(String realName) {
		if (StringUtil.isEmpty(realName)) {
			return (System.currentTimeMillis() % 2 == 0) ? SexEnum.FEMAILE: SexEnum.MALE;
		}
		if (realName.contains("����")) {
			return SexEnum.MALE;
		}
		if (realName.contains("Ůʿ") || realName.contains("С��")) {
			return SexEnum.FEMAILE;
		}
		return (System.currentTimeMillis() % 2 == 0) ? SexEnum.FEMAILE: SexEnum.MALE;
	}
		
	private int randType(Map<String, String> properties) {
		String typeName = properties.get("��ƴ�����͡�");
		if ("���°�".equals(typeName)) {
			return OrderTypeEnum.WORK.getValue();
		}
		OrderTypeEnum orderTypeEnum = OrderTypeEnum.valueOfName(typeName);
		if (orderTypeEnum  != null) {
			return orderTypeEnum.getValue();
		}
		return rand.nextInt(2) + 1;
	}
	
	private String randQQ(Map<String, String> properties) {
		String qq = properties.get("��QQ��");
		if (!StringUtil.isEmpty(qq)) {
			return qq;
		}
		int num = rand.nextInt(3) + 8;
		return RandomStringUtil.randomNumeric(num);
	}
	
	
	private void randomUser(List<SimpleUser> users) {
		SimpleUser simpleUser = users.get(rand.nextInt(users.size()));
		LoginContext.setUser(simpleUser.getUserId(), simpleUser.getUsername());
	}
	
	private synchronized boolean tryLock(File dir) {
		File lockFile = new File(dir, LOCK_FILENAME);
		if (lockFile.exists()) {
			return false;
		}
		try {
			RandomAccessFile raf = new RandomAccessFile(lockFile, "rw");
			raf.close();
			return true;
		} catch (IOException e) {
			this.log.error("�����ļ���ʧ��", e);
		}
		return false;
	}
	
	private void unLock(File dir) {
		File lockFile = new File(dir, LOCK_FILENAME);
		if (!lockFile.exists()) {
			return;
		}
		lockFile.delete();
	}
	
	
	private ResultCode importOrderData(OrderDO orderDO, OrderUserDO inputCreator) {
		Result result = orderAO.createOrder(orderDO);
		if (!result.isSuccess()) {
			return result.getResultCode();
		}
		long orderId = (Long)result.getModels().get("orderId");
		result = orderAO.fillCreatorInfo(inputCreator, orderId, false);
		if (!result.isSuccess()) {
			return result.getResultCode();
		}
		orderAO.confirmFinishOrder(orderId);
		if (!result.isSuccess()) {
			return result.getResultCode();
		}
		return null;
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

}

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
			templateContext.put("message", "已经有数据在处理中...");
			return;
		}
		try {
			List<SimpleUser> users = initRandUser();
			if (CollectionUtil.isEmpty(users)) {
				templateContext.put("message", "用户数据不存在，请配置用户数据！");
				return;
			}
			String message = processData(dataFilePath, users);
			if (!StringUtil.isEmpty(message)) {
				templateContext.put("message", message);
				return;
			}
			renameDataFile(dataFilePath);
			templateContext.put("message", "处理成功@" + new Date());
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
					log.error("用户不存在：" + username);
					continue;
				}
				SimpleUser simpleUser = new SimpleUser();
				simpleUser.setUserId(user.getId()); 
				simpleUser.setUsername(username);
				ret.add(simpleUser);
			}
			
		} catch (IOException e) {
			this.log.error("读取用户数据失败", e);
		}
		return ret;
	}
	
	private String processData(String dataFilePath, List<SimpleUser> users) {
		try {
			File dataFile = new File(dataFilePath);
			if (!dataFile.exists()) {
				return "目标文件不存在";
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
						log.error("未找到开始标签，忽略此条数据");
						orderLines = null; 
						continue;
					}
					String startTag = tags.pop();
					if (!BEGIN_TAG.equals(startTag)) {
						log.error("未找到开始标签，忽略此条数据");
						orderLines = null; 
						continue;
					}
					if (orderLines != null) {
						// 提交数据
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
			this.log.error("处理数据失败", e);
			return "处理数据失败";
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
				log.error("没有冒号分隔符，忽略词条数据：" + s);
				continue;
			}
			String name = s.substring(0, pos).trim();
			String value = s.substring(pos + 1).trim();
			properties.put(name, value);
		}
		OrderDO orderDO = new OrderDO();
		OrderUserDO inputCreator = new OrderUserDO();
		
		orderDO.setFromCity(properties.get("【出发城市】"));
		orderDO.setArriveCity(properties.get("【目的城市】"));
		orderDO.setFromAddr(properties.get("【起点】"));
		orderDO.setArriveAddr(properties.get("【目的地】")); 
		orderDO.setDescription(properties.get("【备注】")); 
		orderDO.setApproach(properties.get("【途径】")); 
		
		Date fromTime = guessDate(properties.get("【出发时间】"), new Date());
		orderDO.setFromTime(fromTime);
		
		orderDO.setArriveTime(DateUtil.addHour(fromTime, 1));
		
		orderDO.setAfterWorkFromTime(guessDate(properties.get("【下班时间】"), randomAfterWorkDate()));
		orderDO.setTotalSeats(3);
		orderDO.setFromWeek("1,2,3,4,5");
		orderDO.setType(randType(properties));
		orderDO.setCreatorCarOwnerType(carOwnerType(properties));
		
		inputCreator.setRealName(properties.get("【联系人】"));
		inputCreator.setBirthYear(BirthYearEnum.YEAR_80S.getValue());
		inputCreator.setPhone(randPhone(properties));
		inputCreator.setSex(randSex(inputCreator.getRealName()).getValue());
		inputCreator.setQq(randQQ(properties));
		inputCreator.setCareer("其他");
		
		if (StringUtil.isEmpty(orderDO.getFromCity())) {
			log.warn("无城市信息。");
		}
		
		randomUser(users);
		ResultCode resultCode = importOrderData(orderDO, inputCreator);
		if (resultCode != null) {
			log.error("error:" + resultCode.getMessage());
		} else {
			log.warn("导入数据成功:" + inputCreator.getRealName());
			System.out.println("导入数据成功:" + inputCreator.getRealName());
		}
		
	}
	
	private static final String[] PHONE_PREFIX = {"130","132", "131", "135", "136", "137", "138", "158", "159"};
	
	private static final Date[] AFTER_WORK_DATES = {
			DateUtil.parseDate("17:00", "HH:mm"),
			DateUtil.parseDate("17:30", "HH:mm"),
			DateUtil.parseDate("18:00", "HH:mm")
		};
	
	
	private Date randomAfterWorkDate() {
		return AFTER_WORK_DATES[rand.nextInt(AFTER_WORK_DATES.length)];
	}
	
	private int carOwnerType(Map<String, String> properties) {
		String typeName = properties.get("【拼客性质】");
		CarOwnerTypeEnum carOwnerTypeEnum = CarOwnerTypeEnum.valueOfName(typeName);
		if (carOwnerTypeEnum == null) {
			carOwnerTypeEnum = CarOwnerTypeEnum.CAR_OWNER;
		}
		return carOwnerTypeEnum.getValue();
	}
	
	private String randPhone(Map<String, String> properties) {
		String phone = properties.get("【联系电话】");
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
		if (realName.contains("先生")) {
			return SexEnum.MALE;
		}
		if (realName.contains("女士") || realName.contains("小姐")) {
			return SexEnum.FEMAILE;
		}
		return (System.currentTimeMillis() % 2 == 0) ? SexEnum.FEMAILE: SexEnum.MALE;
	}
		
	private int randType(Map<String, String> properties) {
		String typeName = properties.get("【拼车类型】");
		if ("上下班".equals(typeName)) {
			return OrderTypeEnum.WORK.getValue();
		}
		OrderTypeEnum orderTypeEnum = OrderTypeEnum.valueOfName(typeName);
		if (orderTypeEnum  != null) {
			return orderTypeEnum.getValue();
		}
		return rand.nextInt(2) + 1;
	}
	
	private String randQQ(Map<String, String> properties) {
		String qq = properties.get("【QQ】");
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
			this.log.error("创建文件锁失败", e);
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

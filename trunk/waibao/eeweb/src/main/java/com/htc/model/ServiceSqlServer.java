package com.htc.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForZjHisRec;
import com.htc.dao.iface.ZjHistroyDao;
import com.htc.dao.iface.ZjMonitorDao;
import com.htc.dao.impl.BaseSqlMapDao;
import com.htc.domain.EquipData;
import com.htc.domain.ZjHistory;
import com.htc.domain.ZjMonitors;

/**
 * @ UserService.java
 * 作用 : 用户管理
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class ServiceSqlServer {

	private Log log = LogFactory.getLog(ServiceSqlServer.class);
	
	private ZjMonitorDao zjMonitorDao;
	private ZjHistroyDao zjHistroyDao;

	// 构造方法
	public ServiceSqlServer() {
	}
	//注册service -- spring ioc
	public void setZjMonitorDao(ZjMonitorDao zjMonitorDao) {
		this.zjMonitorDao = zjMonitorDao;
	}
	public void setZjHistroyDao(ZjHistroyDao zjHistroyDao) {
		this.zjHistroyDao = zjHistroyDao;
	}

	/**
	 * @describe: 测试sqlServer数据库连接是否正常
	 * @return: true: 连接正常 false: 连接错误
	 * @date:2010-4-15
	 */
	public boolean testLink(){
		boolean rsbool = true;
		try {
			zjMonitorDao.test4Link();
		} catch (Exception e) {
			rsbool = false;
			log.error("sqlServer数据库测试连接失败:" + e.getMessage()+e.toString());
		}
		return rsbool;
	}
	
	/**
	 * @describe:	查询上传的历史纪录
	 * @param zjBean 查询条件的Bean
	 * @date:2010-4-13
	 */
	public List<ZjHistory> selectZjHisRec(BeanForZjHisRec zjBean){
		List<ZjHistory> rsList = null;
		try {
			rsList = zjHistroyDao.selectZjHisRec(zjBean);
		} catch (Exception e) {
			rsList = null;
			log.error("sqlServer数据库,获取数据列表时 出错:" + e.getMessage()+e.toString());
		}
		return rsList;
	}
	
	/**
	 * @describe:刷所有的仪器信息<br>
	 * @param zjMonitorList 需要更新的最新仪器列表<br>
	 * @return: true:更新成功  false:更新失败
	 * @date:2010-4-13
	 */
	public boolean flashAllMonitors(List<EquipData> newEqList){
		boolean rsBool = true;
		if ((newEqList == null)||newEqList.size()<=0){return rsBool;}
		List<ZjMonitors> newMonitorList = new ArrayList<ZjMonitors>();// 获取保存在数据库的新列表
		List<ZjMonitors> oldMonitorList = selectAllZjMonitor(); // 获取保存在数据库的旧列表
		if (oldMonitorList == null){return rsBool;}
		List<ZjMonitors> tempList = new ArrayList<ZjMonitors>();
		
		for (EquipData eqData : newEqList) {	//  填充数据
			if (eqData.getShowAccess() == 1){
				newMonitorList.add(fillMonitor(eqData));
			}
		}
		
		// 删除 旧数据
		tempList = compareMonitor(oldMonitorList, newMonitorList, 2);
		if (tempList !=null && tempList.size() > 0){
			rsBool &= doBatchMonitor(tempList, BaseSqlMapDao.BATCH_DELETE);
			// log.info("Sqlserver - 有配置数据删除");
		}
		// 更改 旧数据
		tempList = compareMonitor(newMonitorList, oldMonitorList, 1);
		if (tempList !=null && tempList.size() > 0){
			rsBool &= doBatchMonitor(tempList, BaseSqlMapDao.BATCH_UPDATE);
			// log.info("Sqlserver - 有配置数据修改");			
		}
		// 增加 新数据
		tempList = compareMonitor(newMonitorList, oldMonitorList, 2);
		if (tempList !=null && tempList.size() > 0){
			rsBool &= doBatchMonitor(tempList, BaseSqlMapDao.BATCH_INSERT);
			// log.info("Sqlserver - 有配置数据增加");			
		}
		
		return rsBool;
	}
	
	public ZjMonitors fillMonitor(EquipData eqData){
		ZjMonitors zjMonitor = new ZjMonitors();
		zjMonitor.setMonitorID(eqData.getEquipmentId());	// 对应仪器的主键ID
		zjMonitor.setMonitorName(eqData.getPlaceStr()+"-"+eqData.getMark());// 仪器名
		zjMonitor.setStoreID(Integer.parseInt(eqData.getDsrsn()));// 不知道什么含义,暂时 填写 LicenseNo一样的数据
		zjMonitor.setT_High(eqData.getTempUp());	// 最高的限制温度
		zjMonitor.setT_Low(eqData.getTempDown());	// 最低的限制温度
		zjMonitor.setH_High(eqData.getHumiUp());	// 最高的限制湿度
		zjMonitor.setH_Low(eqData.getHumiDown());	// 最低的限制湿度
		
		// 好像是要和Access中的 strDSRSN字段对应 -- 系统中strDSRSN和 药监局下发的编号 对应--->>>这里也填写 LicenseNo一样的数据
		zjMonitor.setNoID(Integer.parseInt(eqData.getDsrsn()));
		
		zjMonitor.setLicenseNo(Integer.parseInt(eqData.getDsrsn()));// 对应药监局下发的编号
		zjMonitor.setSendcmd("");	// 暂时不填
		zjMonitor.setZgCode("");	// 暂时不填
		return zjMonitor;
	}
	
	
	/**
	 * @describe:从left截取和right有相同或者id的元素集合
	 * @param type:
	 * 1:获取相同的元素
	 * 2:获取不相同的元素
	 * @date:2010-4-14
	 */
	private List<ZjMonitors> compareMonitor(List<ZjMonitors> left, List<ZjMonitors> right, int type){
		if (left == null){return null;}
		boolean foundFlag = false;
		List<ZjMonitors> tempList = new ArrayList<ZjMonitors>();
		for (ZjMonitors zjMonitors : left) {// 从old数据里面找数据
			
			if (right == null){
				foundFlag = false;
			}else{
				for (ZjMonitors zjMoni : right) {// 和new数据进行比较
					if (zjMonitors.getMonitorID() == zjMoni.getMonitorID()){
						foundFlag = true;
						break;
					}
				}// 内循环
			}
			
			if (type == 1){// 1:获取相同的元素
				if(foundFlag){
					tempList.add(zjMonitors);
				}				
			}else if (type ==2){// 2:获取不相同的元素
				if(!foundFlag){
					tempList.add(zjMonitors); 
				}				
			}
			foundFlag = false;
		}// 外循环			
		return tempList;
	}
	
	private List<ZjMonitors> selectAllZjMonitor(){
		List<ZjMonitors> oldMonitorList = null;		
		try {
			oldMonitorList = zjMonitorDao.selectAllZjMonitor();
		} catch (Exception e) {
			oldMonitorList = null;
			log.error("sqlServer列举仪器信息时 出错:" + e.getMessage()+e.toString());
		}		
		return oldMonitorList;
	}
	
	/**
	 * @describe: 批处理数据
	 * @param zjMoniList 批量处理数据
	 * @param batchType  批量处理类型
	 * @return: true:处理成功 false:处理错误
	 * @date:2010-4-14
	 */
	private boolean doBatchMonitor( List<ZjMonitors> zjMoniList, int batchType){
		boolean rsBool = true;
		try {
			zjMonitorDao.doBatchMonitor(zjMoniList, batchType);
		} catch (Exception e) {
			rsBool = false;
			switch (batchType) {
			case BaseSqlMapDao.BATCH_INSERT:
				log.error("sqlServer添加仪器信息时 出错:" + e.getMessage()+e.toString());
				break;
			case BaseSqlMapDao.BATCH_UPDATE:
				log.error("sqlServer修改仪器信息时 出错:" + e.getMessage()+e.toString());
				break;
			case BaseSqlMapDao.BATCH_DELETE:
				log.error("sqlServer修改仪器信息时 出错:" + e.getMessage()+e.toString());
				break;
			}
		}		
		return rsBool;
	}
	
}

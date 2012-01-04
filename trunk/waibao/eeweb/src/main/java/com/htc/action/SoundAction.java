package com.htc.action;

import com.htc.model.MainService;

/**
 * @ SoundAction.java
 * 作用 : 背景音乐属性获取action. 
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SoundAction extends AbstractAction {

	// 服务类
	private MainService mainService;
	
	// 设置声音开启状态设置声音开启状态 (1:不播放  2:播放 3:系统关闭播放)
	// 需要配合播放文件名和播放间隔
	private int palyFalg;							
	
	// 构造方法
	public SoundAction() {
	}
	//注册service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	/**
	 * @describe: 获取声音开启状态 	
	 * @return: (1:不播放  2:播放)
	 * @date:2010-1-6
	 */	
	public int getPalyFalg() {
		// 保证有报警数据
		if (mainService.selectUndoRec().size()> 0){
			palyFalg = commonDataUnit.getWarnOpenFlag();
			palyFalg = palyFalg == 2? 2:1;
		}else{
			palyFalg = 1;
		}
		return palyFalg;
	}	
	
}

package com.aifuyun.snow.world.biz.ao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.bo.area.CityBO;
import com.aifuyun.snow.world.biz.bo.area.CityIpBO;
import com.aifuyun.snow.world.biz.bo.misc.CookieBO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.common.CookieNames;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;
import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.lang.ConvertUtil;

/**
 * @author pister
 * 2011-1-22
 */
public abstract class BaseAO {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected CityBO cityBO;
	
	protected UserBO userBO;
	
	protected CookieBO cookieBO;
	
	protected CityIpBO cityIpBO;
	
	private Integer getUserLastSelect(long userId) {
		if (userId <= 0) {
			return null;
		}
		ExtUserDO extUser = userBO.queryExtUser(userId);
		if (extUser == null) {
			return null;
		}
		int cityId = extUser.getLastSelectCityId();
		if (cityId == 0) {
			return null;
		}
		return Integer.valueOf(cityId);
	}
	
	protected CityDO querySelectedCity(String ipAddress, int defaultCityId) {
		CityDO selectedCity = null;
		long userId = this.getLoginUserId();
		// 根据用户最后选中的查询
		Integer userSelectCity = getUserLastSelect(userId);
		if (userSelectCity != null) {
			selectedCity = cityBO.queryById(userSelectCity);
		}
		
		if (selectedCity == null) {
			// 从cookie中取
			String cityIdFromCookie = cookieBO.getCookieValue(CookieNames.LAST_SELECT_CITY_ID);
			int cityId = ConvertUtil.toInt(cityIdFromCookie, 0);
			if (cityId != 0) {
				selectedCity = cityBO.queryById(cityId);
			}
		}
		
		if (selectedCity == null) {
			// 根据用户当前的ip地址获取
			CityIpDO cityIpDO = cityIpBO.queryByIp(ipAddress);
			if (cityIpDO != null) {
				selectedCity = cityBO.queryById(cityIpDO.getCityId());
			}
		}
		
		if (selectedCity == null) {
			// 还是拿不到，给一个默认的城市
			selectedCity = cityBO.queryById(defaultCityId);
		}
		return selectedCity;
	}
	
	public final void setCityBO(CityBO cityBO) {
		this.cityBO = cityBO;
	}

	public final void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

	public final void setCookieBO(CookieBO cookieBO) {
		this.cookieBO = cookieBO;
	}

	public final void setCityIpBO(CityIpBO cityIpBO) {
		this.cityIpBO = cityIpBO;
	}

	protected long getLoginUserId() {
		return LoginContext.getUserId();
	}
	
	protected String getLoginUsername() {
		return LoginContext.getUsername();
	}
	
	protected boolean isUserLogin() {
		return LoginContext.isUserLogin();
	}
	
}

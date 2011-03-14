package com.aifuyun.snow.world.biz.ao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.snow.world.biz.bo.area.CityBO;
import com.aifuyun.snow.world.biz.bo.area.CityIpBO;
import com.aifuyun.snow.world.biz.bo.misc.CookieBO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.common.CookieNames;
import com.aifuyun.snow.world.common.IpUtil;
import com.aifuyun.snow.world.common.cache.CacheManager;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;
import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.lang.ConvertUtil;

/**
 * @author pister
 * 2011-1-22
 */
public class BaseAO {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	protected CityBO cityBO;
	
	protected UserBO userBO;
	
	protected CookieBO cookieBO;
	
	protected CityIpBO cityIpBO;
	
	protected CacheManager cacheManager;
	
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
	
	protected CityDO getSelectedCity(int defaultCityId) {
		String ipAddress = IpUtil.getRemoteIpAddress();
		CityDO selectedCity = null;
		long userId = this.getLoginUserId();
		// �����û����ѡ�еĲ�ѯ
		Integer userSelectCity = getUserLastSelect(userId);
		if (userSelectCity != null) {
			selectedCity = cityBO.queryById(userSelectCity);
		}
		
		if (selectedCity == null) {
			// ��cookie��ȡ
			String cityIdFromCookie = cookieBO.getCookieValue(CookieNames.LAST_SELECT_CITY_ID);
			int cityId = ConvertUtil.toInt(cityIdFromCookie, 0);
			if (cityId != 0) {
				selectedCity = cityBO.queryById(cityId);
			}
		}
		
		if (selectedCity == null) {
			// �����û���ǰ��ip��ַ��ȡ
			CityIpDO cityIpDO = cityIpBO.queryByIp(ipAddress);
			if (cityIpDO != null) {
				selectedCity = cityBO.queryById(cityIpDO.getCityId());
			}
		}
		
		if (selectedCity == null) {
			// �����ò�������һ��Ĭ�ϵĳ���
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

	public final void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
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

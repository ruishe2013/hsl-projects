package com.aifuyun.snow.world.biz.ao.area.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.area.CityAO;
import com.aifuyun.snow.world.biz.bo.area.CityBO;
import com.aifuyun.snow.world.biz.bo.area.CityIpBO;
import com.aifuyun.snow.world.biz.bo.area.ProvinceBO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;
import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;
import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.ConvertUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class CityAOImpl extends BaseAO implements CityAO {

	private CityIpBO cityIpBO;
	
	private CityBO cityBO;
	
	private UserBO userBO;
	
	private ProvinceBO provinceBO;
	
	private int defaultCityId = 1;
	
	@Override
	public Result queryAllProviceAndCities() {
		Result result = new ResultSupport(false);
		try {
			// TODO �����������䣬���Լ��뻺��
			
			Map<ProvinceDO, List<CityDO>> province2Cities = CollectionUtil.newTreeMap();
			List<ProvinceDO> provinces = provinceBO.queryAll();
			for (ProvinceDO province : provinces) {
				List<CityDO> cities = cityBO.queryByProvinceId(province.getId());
				
				Collections.sort(cities);
				
				province2Cities.put(province, cities);
			}
			
			result.getModels().put("province2Cities", province2Cities);			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("��ȡ����ʡ�ݺͳ���", e);
		}
		return result;
	}


	@Override
	public Result querySelectedCity(String ipAddress, String cityIdFromCookie) {
		Result result = new ResultSupport(false);
		try {
			CityDO selectedCity = null;
			long userId = this.getLoginUserId();
			// �����û����ѡ�еĲ�ѯ
			Integer userSelectCity = getUserLastSelect(userId);
			if (userSelectCity != null) {
				selectedCity = cityBO.queryById(userSelectCity);
			}
			
			if (selectedCity == null) {
				// ��cookie��ȡ
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
			
			result.getModels().put("selectedCity", selectedCity);			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("��ѯĬ��ѡ��ĳ��д���", e);
		}
		return result;
	}


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
	
	public void setCityIpBO(CityIpBO cityIpBO) {
		this.cityIpBO = cityIpBO;
	}

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

	public void setCityBO(CityBO cityBO) {
		this.cityBO = cityBO;
	}

	public void setDefaultCityId(int defaultCityId) {
		this.defaultCityId = defaultCityId;
	}

	public void setProvinceBO(ProvinceBO provinceBO) {
		this.provinceBO = provinceBO;
	}

}

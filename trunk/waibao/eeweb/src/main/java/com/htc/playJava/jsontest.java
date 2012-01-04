package com.htc.playJava;

import java.util.*;

import com.htc.bean.BeanForEnDeJson;
import com.htc.domain.User;
import com.htc.domain.Workplace;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class jsontest {

	private static boolean[] boolArray = { true, false };
	private static List list = new ArrayList();
	private static Map map = new HashMap();
	private static Object[] test = { new User(), new User() };

	public static void main(String[] args) {

		// 转换Array
		JSONArray jsonArray = JSONArray.fromObject(boolArray);
		System.out.println(jsonArray.toString());
		JSONArray jsonArray1 = JSONArray.fromObject("['1','2','3']");
		System.out.println(jsonArray1.toString());

		list.add("first");
		list.add("second");
		// list.add(new TestBean());
		// 转换List
		JSONArray jsonList = JSONArray.fromObject(list);
		System.out.println(jsonList);

		// 转换map
		map.put("name", "windfree");
		map.put("bool", Boolean.TRUE);
		map.put("int", new Integer(0));
		map.put("arr", new String[] { "a", "b" });
		map.put("func", "function (i){ return this.arr[i]; }");
		map.put("bean", test);
		JSONObject jsonMap = JSONObject.fromObject(map);
		System.out.println(jsonMap);

		// 转换日期
		JSONObject jsonObj = JSONObject.fromObject(new Date());
		System.out.println(jsonObj.toString());

		// 转换Bean
		User ddc = new User();
		ddc.setName("dd的dd");
		JSONObject jsonBean = JSONObject.fromObject(ddc);
		System.out.println(jsonBean);
		User dd = (User) JSONObject.toBean(jsonBean, User.class);
		System.out.println(dd.getName());

		// 转换多list
		BeanForEnDeJson bean = new BeanForEnDeJson();
		Workplace ww = new Workplace();
		ww.setPlaceName("ww");
		bean.addWorkPlace(ww);
		ww = new Workplace();
		ww.setPlaceName("wwc的");
		bean.addWorkPlace(ww);
		JSONObject jsonBeanList = JSONObject.fromObject(bean);
		System.out.println("-----------------------");
		System.out.println(jsonBeanList);
		
		jsonBeanList = JSONObject.fromObject(jsonBeanList.toString());
		Map<String, Class> m = new HashMap<String, Class>();
		m.put("workplaceList", Workplace.class);
		BeanForEnDeJson beand = (BeanForEnDeJson) JSONObject.toBean(jsonBeanList, BeanForEnDeJson.class, m);
		for (Workplace or : beand.getWorkplaceList()) {
			System.out.println(or.getPlaceName());
		}

	}

}

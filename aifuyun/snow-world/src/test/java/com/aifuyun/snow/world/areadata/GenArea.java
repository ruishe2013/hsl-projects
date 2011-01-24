package com.aifuyun.snow.world.areadata;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.area.CityIpDO;
import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.sql.RowProcessor;
import com.zjuh.sweet.sql.SqlExecutor;

public class GenArea {
	
	private static final String DATA_PATH = "E:/longyi/datas/IP_Address.mdb";
	
	private Map<String, ProvinceDO> provinces;
	
	private Map<String, CityDO> cities;
	
	private List<CityIpDO> cityIps;
	
	private int provinceCurrentId = 1;
	
	private int cityCurrentId = 1; 
	
	private static final Set<String> specProvinces = CollectionUtil.newHashSet();
	
	static {
		specProvinces.add("澳门");
		specProvinces.add("香港");
		specProvinces.add("台湾");
	}
	
	public static void main(String[] args) throws Exception {
		GenArea genArea = new GenArea();
		genArea.gen();
		genArea.export("d:/import.txt");
	}
	
	public void export(String filename) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(filename));
		for (ProvinceDO provinceDO : provinces.values()) {
			String provinceSql = genProvinceSql(provinceDO);
			out.println(provinceSql);
		}
		out.println();
		out.println();
		out.println();
		for (CityDO city : cities.values()) {
			if (StringUtil.isEmpty(city.getName())) {
				continue;
			}
			String citySql = genCitySql(city);
			out.println(citySql);
		}
		out.println();
		out.println();
		out.println();
		for (CityIpDO cityIpDO : cityIps) {
			String cityIpSql = genCityIpSql(cityIpDO);
			out.println(cityIpSql);
		}
		out.println();
		
		out.close();
	}
	
	private String genCityIpSql(CityIpDO cityIpDO) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into sw_area_city_ip (city_id, city_name, ip_end, ip_start, gmt_create, gmt_modified)");
		sb.append("values(");
		sb.append(cityIpDO.getCityId());
		sb.append(", ");
		sb.append("\'"+ cityIpDO.getCityName() +"\'");
		sb.append(", ");
		sb.append(cityIpDO.getIpEnd());
		sb.append(", ");
		sb.append(cityIpDO.getIpStart());
		sb.append(", now(), now());"); 
		return sb.toString();
	}
	
	private String genProvinceSql(ProvinceDO provinceDO) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into sw_area_province (id, name, gmt_create, gmt_modified)");
		sb.append("values(");
		sb.append(provinceDO.getId());
		sb.append(", ");
		sb.append("\'"+ provinceDO.getName() +"\'");
		sb.append(", now(), now());"); 
		return sb.toString();
	}

	private String genCitySql(CityDO cityDO) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into sw_area_city (id, name, pinyin, province_id, gmt_create, gmt_modified)");
		sb.append("values(");
		sb.append(cityDO.getId());
		sb.append(", ");
		sb.append("\'"+ cityDO.getName() +"\',");
		sb.append("\'"+ StringUtil.trimToEmpty(cityDO.getPinyin()) +"\',");
		sb.append(cityDO.getProvinceId());
		sb.append(", now(), now());"); 
		return sb.toString();
	}
	
	public void gen() throws Exception {
		initProvinces();
		initCities();
		initIps();
	}
	
	private Connection getConnection() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
		String url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=" + DATA_PATH;
		Connection conn = DriverManager.getConnection(url);
		return conn;
	}
	
	private void initProvinces() throws Exception {
		String sql = "select distinct province from city_py order by province asc";
		Connection conn = getConnection();
		ProvinceRowProcessor provinceRowProcessor = new ProvinceRowProcessor();
		SqlExecutor.executeQuery(conn, provinceRowProcessor, sql);
		provinces = provinceRowProcessor.getProvinces();
		
	}
	
	class ProvinceRowProcessor implements RowProcessor {

		private Map<String, ProvinceDO> provinces = CollectionUtil.newHashMap();
		
		@Override
		public void processRow(ResultSet rs) throws SQLException {
			ProvinceDO province = new ProvinceDO();
			String name = rs.getString("province");
			province.setId(provinceCurrentId++);
			province.setName(name);
			provinces.put(name, province);
		}

		public Map<String, ProvinceDO> getProvinces() {
			return provinces;
		}
		
	}
	
	
	class CityProcessor implements RowProcessor {

		private Map<String, CityDO> cities = CollectionUtil.newHashMap();
		
		private Map<String, ProvinceDO> provinces;
		
		private CityProcessor(Map<String, ProvinceDO> provinces) {
			super();
			this.provinces = provinces;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			String province = rs.getString("province");
			String name = rs.getString("city");
			String pinyin = rs.getString("pinyin");
			
			if (!isCityNameOK(name)) {
				return;
			}
			
			CityDO city = new CityDO();
			city.setId(cityCurrentId++);
			city.setName(name);
			city.setPinyin(pinyin);
			ProvinceDO provinceDO = provinces.get(province);
			if (provinceDO == null) {
				throw new RuntimeException("省份不存在：" + province);
			}
			city.setProvinceId(provinceDO.getId());
			cities.put(name, city);
		}

		public Map<String, CityDO> getCities() {
			return cities;
		}

	}
	
	private ProvinceDO addProvince(String name) {
		ProvinceDO province = new ProvinceDO();
		province.setId(provinceCurrentId++);
		province.setName(name);
		provinces.put(name, province);
		return province;
	}
	
	private boolean isCityNameOK(String name) {
		if (StringUtil.isEmpty(name)) {
			return false;
		}
		if (name.indexOf("网吧") >= 0) {
			return false;
		}
		return true;
	}
	
	private CityDO addCity(String name, String province) {
		if (!isCityNameOK(name)) {
			return null;
		}		
		CityDO city = new CityDO();
		city.setId(cityCurrentId++);
		city.setName(name);
		ProvinceDO provinceDO = provinces.get(province);
		if (provinceDO == null) {
			provinceDO = guessProvinceByName(province);
			if (provinceDO == null) {
				String msg = "省份不存在：" + province + ", name:" + name;
				System.out.println(msg);
				return null;
			}
		}
		city.setProvinceId(provinceDO.getId());
		cities.put(name, city);
		return city;
	}
	
	private ProvinceDO guessProvinceByName(String province) {
		if (StringUtil.isEmpty(province)) {
			return null;
		}
		ProvinceDO provinceDO = provinces.get(province);
		if (provinceDO != null) {
			return provinceDO;
		}
		if (specProvinces.contains(province)) {
			provinceDO = addProvince(province);
			return provinceDO;
		}
		if (province.length() < 2) {
			return null;
		}
		String try2 = province.substring(0, 2);
		provinceDO = provinces.get(try2);
		if (provinceDO != null) {
			return provinceDO;
		}
		if (province.length() < 3) {
			return null;
		}
		String try3 = province.substring(0, 3);
		provinceDO = provinces.get(try3);
		if (provinceDO != null) {
			return provinceDO;
		}
		
		return null;
	}
	
	private void initCities() throws Exception {
		String sql = "select province, city, pinyin from city_py";
		CityProcessor cityProcessor = new CityProcessor(provinces);
		Connection conn = getConnection();
		SqlExecutor.executeQuery(conn, cityProcessor, sql);
		cities = cityProcessor.getCities();
	}
	
	class IpProcessor implements RowProcessor {

		private Map<String, CityDO> cities;
		
		private List<CityIpDO> cityIps = CollectionUtil.newArrayList();
		
		private IpProcessor(Map<String, CityDO> cities) {
			super();
			this.cities = cities;
		}

		@Override
		public void processRow(ResultSet rs) throws SQLException {
			long ipStart = rs.getLong("ip1");
			long ipEnd = rs.getLong("ip2");
			String cityName = rs.getString("city");
			String province =  rs.getString("province");
			CityDO city = cities.get(cityName);
			if (city == null) {
				city = addCity(cityName, province);
				if (city == null) {
					return;
				}
			}
			CityIpDO cityIp = new CityIpDO();
			cityIp.setCityId(city.getId());
			cityIp.setCityName(cityName);
			cityIp.setIpEnd(ipEnd);
			cityIp.setIpStart(ipStart);
			cityIps.add(cityIp);
		}

		public List<CityIpDO> getCityIps() {
			return cityIps;
		}
		
	}
	
	private void initIps() throws Exception {
		String sql = "select ip1, ip2, city, province from ip_address";
		IpProcessor ipProcessor = new IpProcessor(cities);
		Connection conn = getConnection();
		SqlExecutor.executeQuery(conn, ipProcessor, sql);
		cityIps = ipProcessor.getCityIps();
	}
	
}

package com.aifuyun.snow.world.biz.dal.dataobject.area;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.bo.area.CityBO;
import com.aifuyun.snow.world.biz.bo.area.ProvinceBO;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.area.ProvinceDO;

public class ExportCities extends SnowWorldTest {

	private CityBO cityBO;
	
	private ProvinceBO provinceBO;
	
	public void testExport() throws IOException {
		Writer out = new FileWriter("E:/test/import_cities/cities_for_edit_seq_num.txt");
		export(out);
		out.close();
	}
	
	private void export(Writer out) throws IOException {
		List<ProvinceDO> provinces = provinceBO.queryAll();
		for (ProvinceDO province : provinces) {
			List<CityDO> cities = cityBO.queryByProvinceId(province.getId());
			out.write("---");
			out.write(province.getName());
			out.write("\r\n");
			for (CityDO city : cities) {
				String sql = "insert into sw_area_city (id, name, pinyin, seq_num, province_id, gmt_create, gmt_modified)values" +
						"(" + city.getId() +", '"+ city.getName() +"','"+ city.getPinyin() +"', 0, "+ city.getProvinceId() +", now(), now());";
				out.write(sql);
				out.write("\r\n");
			}
			out.write("\r\n\r\n");
			out.flush();
		}
	}

	public void setCityBO(CityBO cityBO) {
		this.cityBO = cityBO;
	}

	public void setProvinceBO(ProvinceBO provinceBO) {
		this.provinceBO = provinceBO;
	}
	
}

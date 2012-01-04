package com.htc.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.htc.bean.BeanForEnDeJson;
import com.htc.domain.*;
import com.htc.model.EnDeCode.DES3;

public class HtcFiles {
	
	/**
	 * 检查文件是否存在
	 * @param filePath 文件路径
	 * @return true:存在 false:不存在 
	 * @throws IOException
	 */
	public static boolean CheckFile(String filePath)throws IOException {
		boolean result = false;
		File file = new File(filePath);
		if (file.exists()) {
			result = true;
		}
		return result;
	}

	/**
	 * 创建或者检查目录
	 * @param folderName 文件目录名
	 * @return true:成功 false:失败
	 */
	public static boolean createOrCheckFolder(String folderName) throws IOException{
		boolean result = false;
		File file = new File(folderName);
		if (file.exists()) {
			result = true;
		} else {
			file.mkdirs();
			result = true;
		}
		return result;
	}
	
	/**
	 * @describe: 读取目标文件中的内容,并保存到缓存中
	 * @param src 目标文件 
	 * @date:2010-1-27
	 */
	private static String readBuffer(File src){
		StringBuffer sysBuf = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new FileReader(src)); 
			String line;
			while((line=br.readLine())!=null) {
				sysBuf.append(line);
			}
		} catch (Exception e) {
			return  null;
		} 
		return sysBuf.toString();
	}
	
	/**
	 * @describe: 把导出信息,[加密]后放入[输入流]
	 * @param jsonBean : [区域,仪器,系统参数,短信格式]封装成json格式的数据
	 * @throws IOException 
	 * @date:2010-1-27
	 */
	public static InputStream writeBuffer(BeanForEnDeJson jsonEnDeBean){
		InputStream inputStream = null;
		if (jsonEnDeBean != null){
			try {
				// 导出信息格式成json
				JSONObject jsonBean = JSONObject.fromObject(jsonEnDeBean);
				// 把json内容加密
				inputStream = new ByteArrayInputStream(DES3.encrypt(jsonBean.toString()).getBytes());
			} catch (Exception e) {
				inputStream = null;
			}
		}
		return inputStream;
	}
	
	
	/**
	 * @describe: 把导入的加密信息,解密,再转换成 系统配置类型[区域,仪器,系统参数,短信格式]
	 * @param jsonString 导入的加密信息串
	 * @date:2010-1-28
	 */
	@SuppressWarnings("unchecked")
	private static BeanForEnDeJson readBuffer(String jsonString){
		BeanForEnDeJson jsonEnDeBean = null;
		
		// 指定josn格式对象
		Map<String, Class> format = new HashMap<String, Class>();
		format.put("workplaceList", Workplace.class);
		format.put("equipDataList", EquipData.class);
		format.put("sysParamList", SysParam.class);
		format.put("gprsSetList", GprsSet.class);
		
		try {
			JSONObject jsonObject = JSONObject.fromObject(DES3.decrypt(jsonString));
			jsonEnDeBean = (BeanForEnDeJson) JSONObject.toBean(jsonObject, BeanForEnDeJson.class, format);		
		} catch (Exception e) {
			//e.printStackTrace();
			jsonEnDeBean = null;
		}
		return jsonEnDeBean;
	}
	
	/**
	 * @describe: 读取目标文件中的内容, 解密,再转换成 系统配置类型[区域,仪器,系统参数,短信格式]	
	 * @param src 目标文件
	 * @date:2010-1-29
	 */
	public static BeanForEnDeJson deCryptFile(File src){
		BeanForEnDeJson beanJson = null;
		String srcStr = readBuffer(src);
		if (srcStr != null){
			beanJson = readBuffer(srcStr);
		}
		return beanJson;
	}
//	public static void main(String[] args) throws Exception {
//		String folderName = "c:/cc/bb/cc";
//		System.out.println("文件目录" + HtcFiles.createOrCheckFolder(folderName));
//		String fileName = "C:/htcweb_log/info.log";
//		System.out.println("文件" + HtcFiles.CheckFile(fileName));
//		fileName = "C:/htcweb_log/info.log1";
//		System.out.println("文件" + HtcFiles.CheckFile(fileName));
//	}
	
}
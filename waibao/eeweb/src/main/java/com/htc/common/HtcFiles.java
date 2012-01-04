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
	 * ����ļ��Ƿ����
	 * @param filePath �ļ�·��
	 * @return true:���� false:������ 
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
	 * �������߼��Ŀ¼
	 * @param folderName �ļ�Ŀ¼��
	 * @return true:�ɹ� false:ʧ��
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
	 * @describe: ��ȡĿ���ļ��е�����,�����浽������
	 * @param src Ŀ���ļ� 
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
	 * @describe: �ѵ�����Ϣ,[����]�����[������]
	 * @param jsonBean : [����,����,ϵͳ����,���Ÿ�ʽ]��װ��json��ʽ������
	 * @throws IOException 
	 * @date:2010-1-27
	 */
	public static InputStream writeBuffer(BeanForEnDeJson jsonEnDeBean){
		InputStream inputStream = null;
		if (jsonEnDeBean != null){
			try {
				// ������Ϣ��ʽ��json
				JSONObject jsonBean = JSONObject.fromObject(jsonEnDeBean);
				// ��json���ݼ���
				inputStream = new ByteArrayInputStream(DES3.encrypt(jsonBean.toString()).getBytes());
			} catch (Exception e) {
				inputStream = null;
			}
		}
		return inputStream;
	}
	
	
	/**
	 * @describe: �ѵ���ļ�����Ϣ,����,��ת���� ϵͳ��������[����,����,ϵͳ����,���Ÿ�ʽ]
	 * @param jsonString ����ļ�����Ϣ��
	 * @date:2010-1-28
	 */
	@SuppressWarnings("unchecked")
	private static BeanForEnDeJson readBuffer(String jsonString){
		BeanForEnDeJson jsonEnDeBean = null;
		
		// ָ��josn��ʽ����
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
	 * @describe: ��ȡĿ���ļ��е�����, ����,��ת���� ϵͳ��������[����,����,ϵͳ����,���Ÿ�ʽ]	
	 * @param src Ŀ���ļ�
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
//		System.out.println("�ļ�Ŀ¼" + HtcFiles.createOrCheckFolder(folderName));
//		String fileName = "C:/htcweb_log/info.log";
//		System.out.println("�ļ�" + HtcFiles.CheckFile(fileName));
//		fileName = "C:/htcweb_log/info.log1";
//		System.out.println("�ļ�" + HtcFiles.CheckFile(fileName));
//	}
	
}
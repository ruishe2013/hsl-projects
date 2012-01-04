package com.htc.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @ BeanForFlashData.java
 * ���� : amline_1.6.4.0 Flash����Data��Ԫ-��̬flash��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-12-2     YANGZHONLI       create
 */
public class BeanForFlashData implements Serializable  {
	
	private static final long serialVersionUID = -795329269311529683L;
	
	// Ĭ�ϲ���,����Ҫ�޸�--------------------------------------------
	/**
	 * �����������ɫ
	 */
	public String guide_fill_color = "#ff0000";
	/**
	 * ����߿���ɫ
	 */
	public String guide_color = "#F3BFD7";
	/**
	 * �����ڵ�͸����
	 */
	public int guide_fill_alpha = 20;	
	/**
	 * ��������ʾ��Բ��Ĵ�С
	 */
	public int graph_bullet_size = 5;
	/**
	 * ���������ݽڵ����ʾ��ʽ
	 */
	public String graph_bullet = "round";//square, round, square_outlined, round_outlined, square_outline, round_outline
	
	// ��̬����,��Ҫ�޸�----------------------------------------------
	/**
	 * ����ʼֵ
	 */
	public String guide_start_value;
	/**
	 * �������ֵ
	 */
	public String guide_end_value;
	/**
	 * ���߽ڵ���,������ʾ����Ϣ�ĺ�׺.��:% [��ʾ{value}%]
	 */
	public String balloon_text_append = "";
	/**
	 * flash��seriesԪ��(x��)
	 */
	public List<String> headers;
	/**
	 * flash���߱��⼯��(eqid, ��ǩ��)
	 */
	public Map<Integer, String> titles = null;
	/**
	 * flash�������ݼ���(eqid,(�����б�))
	 */
	public Map<Integer, List<String>> graphs = null;
	
	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<chart>");
		strBuf.append("\n");
		
		// X����Ϣ 
		strBuf.append("<series>");
		strBuf.append("\n");
		//eg:<value xid="0">2007 07 01</value>
		for (int i = 0; i < headers.size(); i++) {
			strBuf.append("<value xid=\"" +i+ "\">" +headers.get(i)+ "</value>");
			strBuf.append("\n");
		}
		strBuf.append("</series>");
		strBuf.append("\n");
		
		// ��������Ϣ
		strBuf.append("<graphs>");
		strBuf.append("\n");
		int eqid ;
		for (Entry<Integer, String> entry : titles.entrySet()) {
			eqid = entry.getKey();
			strBuf.append("<graph title=\"" + entry.getValue() +
					"\" bullet_size=\"" + graph_bullet_size + 
					"\" balloon_text=\"{value}"+balloon_text_append+
					"\" bullet=\""+ graph_bullet + "\">");
			strBuf.append("\n");
			
			for (int j = 0; j < graphs.get(eqid).size(); j++) {
				strBuf.append("<value xid=\""+j+"\">"+ graphs.get(eqid).get(j) +"</value>");
				strBuf.append("\n");
			}
			strBuf.append("</graph>");
			strBuf.append("\n");
		}
		
		strBuf.append("</graphs>");
		strBuf.append("\n");
		
		// ����������Ϣ
		if (graphs.size() > 0){
			strBuf.append("<guides>");
			strBuf.append("\n");
			strBuf.append("<max_min>true</max_min>");
			strBuf.append("\n");			
			strBuf.append("<guide>");
			strBuf.append("\n");
			
			strBuf.append("<start_value>"+ guide_start_value +"</start_value>");
			strBuf.append("\n");
			strBuf.append("<end_value>" + guide_end_value + "</end_value>");
			strBuf.append("\n");
			strBuf.append("<fill_color>" + guide_fill_color + "</fill_color>");
			strBuf.append("\n");
			strBuf.append("<color>" + guide_color + "</color>");
			strBuf.append("\n");
			strBuf.append("<fill_alpha>" + guide_fill_alpha + "</fill_alpha>");
			strBuf.append("\n");
			
			strBuf.append("</guide>");
			strBuf.append("\n");
			strBuf.append("</guides>");
			strBuf.append("\n");
			
			strBuf.append("</chart>");
			strBuf.append("\n");
		}
		
		return strBuf.toString();
	}
	
//	public static void main(String[] args){
//		BeanForFlashData bean = new BeanForFlashData();
//		bean.guide_start_value = "20";
//		bean.guide_end_value = "100";
//		bean.balloon_text_append = "%";
//		
//		List<String> headers = new ArrayList<String>();
//		for (int i = 0; i < 20; i++) {
//			headers.add(String.valueOf(100+i));
//		}
//		bean.headers = headers;
//		headers = new ArrayList<String>();
//		headers.add("abc-����-1");
//		headers.add("abc-����-2");
//		headers.add("abc-����-3");
//		bean.titles = headers;
//		
//		List<List<String>> graphs = new ArrayList<List<String>>();
//		List<String> graph = null;
//		for (int i = 100; i < 400; i=i+100) {
//			graph = null;	graph = new ArrayList<String>();
//			for (int j = 0; j < 20; j++) {
//				graph.add(String.valueOf(i+j));
//			}
//			graphs.add(graph);
//		}
//		bean.graphs = graphs;
//		System.out.println(bean.toString());
//	}
	
}	

//----------------cmchart-Flash ���ݸ�ʽexample---------------------//
//<?xml version="1.0" encoding="UTF-8"?>
//<chart>
//	<series>
//		<value xid="0">2007 07 01</value>
//		<value xid="1">2007 07 02</value>
//	</series>
//	<graphs>
//		<graph title="One" bullet_size="5" bullet="round" balloon_text="{value}%">
//			<value xid="0">5</value>
//			<value xid="1">5.2</value>
//		</graph>
//		<graph title="Two" bullet_size="5" bullet="round">
//			<value xid="0">7.1</value>
//			<value xid="1">8.3</value>
//		</graph>
//		<graph title="Three" bullet_size="5" bullet="round">
//			<value xid="0">3.1</value>
//			<value xid="1">2.3</value>		
//		</graph>		
//	</graphs>
//	<guides>
//		<max_min>true</max_min>
//		<guide>
//			<start_value>0.09</start_value>
//			<end_value>60.3</end_value>
//			<fill_color>#ff0000</fill_color>
//			<color>#ff0000</color>
//			<fill_alpha>20</fill_alpha>
//		</guide>
//	</guides>
//</chart>

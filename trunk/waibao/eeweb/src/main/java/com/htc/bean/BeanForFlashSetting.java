package com.htc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ BeanForMakeFlash.java
 * ���� : amline_1.6.4.0 Flash����Setting��Ԫ
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-12-2     YANGZHONLI       create
 */
public class BeanForFlashSetting implements Serializable  {
	
	private static final long serialVersionUID = 6385408787125351951L;
	/**
	 * Falsh���ݸ�ʽ:""��ֵΪxml��ʽ����(��ʾ������,��������),"csv"Ϊcsv��ʽ����(��ʾ��һ,�е�����С).Ĭ��:"csv"
	 */
	public String data_type = "csv";
	/**
	 * Falsh�����ݵ�����.Ĭ��:"Georgia"
	 */
	public String falsh_font = "Georgia";
	/**
	 * Falshˢ�¼��
	 */
	public int reload_data_interval = 0;
	/**
	 * Falsh�����С
	 */
	public int falsh_font_size = 12;
	/**
	 * if your chart's width or height is set in percents, and redraw is set to true
	 */
	public boolean redraw = false;				//--��Ҫ�޸�
	/**
	 * �Ż�Flash,falseΪ�Ż�.Ĭ��:false
	 */
	public boolean js_enabled = false;
	/**
	 * X����ʾ��������.Ĭ����ʾ5��������.
	 */
	public int grid_x_show_count= 8;			//--��Ҫ�ʵ��޸�
	/**
	 * ���Y����ʾ��������.Ĭ����ʾ10��������.
	 */
	public int grid_y_show_count= 12;			//--��Ҫ�ʵ��޸�
	/**
	 * Y���ǩ��Ҫ��ӵ��ַ�.Ĭ��û�����
	 */
	public String y_value_append = "";			//--��Ҫ�޸�(���F��%)******
	/**
	 * ͼ����׺
	 */
	public String legend_value_append = "";		//--ͼ��
	/**
	 * Y���ǩ��Ҫ��ӵ��ַ���λ��.Ĭ��Ϊ:right
	 */
	public String y_value_append_position = "right";
	/**
	 * �Ƿ���ʾͼ��[��ʷ��������false,��ʱ��������true]
	 */
	public boolean legend_enabled = true;		//--��Ҫ�ʵ��޸�
	/**
	 * ͼ���Ŀ��
	 */
	public int legend_width = 60;				//--��Ҫ�ʵ��޸�
	/**
	 * �Ƿ���Ҫ���Ŵ�Ч��
	 */
	public boolean zoomable_use = true;			//--��Ҫ�ʵ��޸�
	/**
	 * �Ƿ���Ҫ���Ŵ�Ч��
	 */
	public boolean print_use = false;			//--��ӡ������,����false
	/**
	 * û������ʱ��ʾ���ַ�
	 */
	public String no_data_str = "��������";
	/**
	 * ����
	 */
	public String label_name = "";				//--��Ҫ�޸�
	/**
	 * ����-Y����λ��
	 */
	public int label_name_y_position = 30;		//--��Ҫ�ʵ��޸�
	/**
	 * ��������ʾ��Բ��Ĵ�С
	 */
	public int bullet_size = 5;					//--��Ҫ�ʵ��޸�
	
	/**
	 * �Ƿ���ʾ��������������.��ʵʱ������ʹ��
	 */
	public boolean guides_maxmin_show = false;	//--�����Ԫ������.ʵʱ�����������޿�����������������
	/**
	 * ����������ֵ
	 */
	public float guides_max_value = 40;			//--��Ҫ�޸�******
	/**
	 * ������������ɫ
	 */
	public String guides_max_color = "#00ff00";	//--��Ҫ�޸�******
	/**
	 * ���������Ŀ��
	 */
	public int guides_max_width = 2;
	/**
	 * ���������Ƿ���ʾ����.Ĭ��true,��ʾ��ʾ����
	 */
	public boolean guides_max_dashed = true;
	/**
	 * Y�ḽ��ֵ,��������Y�̶ȸߵ�
	 */
	public int Y_add_value = 0;
	/**
	 * ����������ֵ
	 */
	public float guides_min_value = 20;			//--��Ҫ�޸�******
	/**
	 * ������������ɫ
	 */
	public String guides_min_color = "#ff2571";	//--��Ҫ�޸�******
	/**
	 * ���������Ŀ��
	 */
	public int guides_min_width = 2;
	/**
	 * ���������Ƿ���ʾ����.Ĭ��true,��ʾ��ʾ����
	 */
	public boolean guides_min_dashed = true;
	
	/**
	 * flsh����ͼ����List�б�
	 */
	public List<String> flashdata = new ArrayList<String>();
	
	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<settings>");
		strBuf.append("\n");
		strBuf.append("<data_type>" + data_type + "</data_type>");		// ���ݸ�ʽ
		strBuf.append("\n");
		if (data_type.equals("xml")){									// ʵʱ������,flash�������ݼ��
			strBuf.append("<reload_data_interval>"+reload_data_interval+"</reload_data_interval>");
			strBuf.append("\n");
			strBuf.append("<add_time_stamp>true</add_time_stamp>");
			strBuf.append("\n");
		}
		strBuf.append("<font>" + falsh_font + "</font>"); 				// ����
		strBuf.append("\n");
		strBuf.append("<decimals_separator>.</decimals_separator>");	// С���ָ�����
		strBuf.append("\n");
		strBuf.append("<text_size>" + falsh_font_size + "</text_size>");
		strBuf.append("\n");
		strBuf.append("<redraw>" + redraw + "</redraw>");				// ����Ӧ���ڴ�С
		strBuf.append("\n");
		strBuf.append("<js_enabled>" + js_enabled + "</js_enabled>");	// �Ż�
		strBuf.append("\n");
		
		strBuf.append("<grid>");
		strBuf.append("\n");
		strBuf.append("<x><approx_count>" + grid_x_show_count + "</approx_count></x>");// X����ʾ��������
		strBuf.append("\n");
		strBuf.append("<y_left><approx_count>" + grid_y_show_count + "</approx_count></y_left>");// ���Y����ʾ��������
		strBuf.append("\n");
		strBuf.append("</grid>");
		strBuf.append("\n");
		
		strBuf.append("<values><y_left>");
		strBuf.append("\n");
		strBuf.append("<unit>" + y_value_append + "</unit>");							// Y���ǩ��Ҫ��ӵ��ַ�
		strBuf.append("\n");
		strBuf.append("<unit_position>"+y_value_append_position+"</unit_position>");	// Y���ǩ��Ҫ��ӵ��ַ���λ��.Ĭ��Ϊ:right
		strBuf.append("\n");
		strBuf.append("</y_left></values>  ");
		strBuf.append("\n");
		
		strBuf.append("<legend><values>");
		strBuf.append("\n");
		strBuf.append("<enabled>"+legend_enabled+"</enabled>");
		strBuf.append("\n");
		strBuf.append("<width>" + legend_width + "</width>");							// ͼ���Ŀ��
		strBuf.append("\n");
		strBuf.append("<align>left</align>");
		strBuf.append("\n");
		strBuf.append("<text><![CDATA[{value}"+ legend_value_append +"]]></text>");
		strBuf.append("\n");
		strBuf.append("</values></legend>");
		strBuf.append("\n");
		strBuf.append("<indicator><zoomable>" + zoomable_use + "</zoomable></indicator>");	// �Ƿ���Ҫ���Ŵ�Ч��
		strBuf.append("\n");
		
//		strBuf.append("<export_as_image>");									//����ͼƬ�Ĵ�����
//		strBuf.append("<file>/htcweb/ExportImage.servlet</file>");
//		strBuf.append("<target>_blank</target>");
//		strBuf.append("</export_as_image>");
		
		
		strBuf.append("<strings>");
		strBuf.append("\n");
		strBuf.append("<no_data>" + no_data_str + "</no_data>");			// û������ʱ��ʾ���ַ�
		strBuf.append("<error_in_data_file>" + no_data_str + "</error_in_data_file>");			// û������ʱ��ʾ���ַ�
		strBuf.append("\n");
//		strBuf.append("<export_as_image>����ͼƬ</export_as_image>");		// ����ͼƬ����
		strBuf.append("</strings>");
		strBuf.append("\n");
		
		strBuf.append("<context_menu>");
		strBuf.append("\n");
		strBuf.append("<default_items>");
		strBuf.append("\n");
		strBuf.append("<print>" + print_use + "</print>");
		strBuf.append("\n");
		strBuf.append("</default_items>");
		strBuf.append("\n");
		strBuf.append("</context_menu>");
		strBuf.append("\n");
		
		strBuf.append("<labels>");
		strBuf.append("\n");
		strBuf.append("<label lid=\"0\">");
		strBuf.append("\n");
		strBuf.append("<y>" + label_name_y_position + "</y>");
		strBuf.append("\n");
		strBuf.append("<width>100%</width>");
		strBuf.append("\n");
		strBuf.append("<align>center</align>");
		strBuf.append("\n");
		strBuf.append("<text><![CDATA[<B>" + label_name + "</B>]]></text>");
		strBuf.append("\n");
		strBuf.append("</label>");
		strBuf.append("\n");
		strBuf.append("</labels>");
		strBuf.append("\n");
		
		if (data_type.equals("csv")){
			strBuf.append("<graphs>");
			strBuf.append("\n");
			for (int i = 0; i < flashdata.size(); i++) {
				strBuf.append("<graph>");
				strBuf.append("\n");
				strBuf.append("<title>" + flashdata.get(i) + "</title>");
				strBuf.append("\n");
				strBuf.append("<bullet>round_outlined</bullet>");
				strBuf.append("\n");
				strBuf.append("<bullet_size>" + bullet_size + "</bullet_size>");
				strBuf.append("\n");
				strBuf.append("<balloon_text>");
				strBuf.append("<![CDATA[{value}" + legend_value_append + "]]>");
				strBuf.append("</balloon_text>");
				strBuf.append("\n");
				strBuf.append("</graph>");
				strBuf.append("\n");
			}
			strBuf.append("</graphs>");
			strBuf.append("\n");
		}
		
		if (guides_maxmin_show){				// �Ƿ���ʾ��������������
			strBuf.append("<guides>");
			strBuf.append("\n");
			strBuf.append("<max_min>true</max_min>");
			strBuf.append("\n");
			strBuf.append("<guide>");
			strBuf.append("\n");
			strBuf.append("<start_value>" + (guides_max_value + Y_add_value) + "</start_value>");		// ����������ֵ
			strBuf.append("\n");
			strBuf.append("<width>" + guides_max_width + "</width>");					// ���������Ŀ��
			strBuf.append("\n");
			strBuf.append("<color>" + guides_max_color + "</color>");					// ������������ɫ
			strBuf.append("\n");
			strBuf.append("<dashed>" + guides_max_dashed + "</dashed>");				// ���������Ƿ���ʾ����
			strBuf.append("\n");
			strBuf.append("</guide> ");
			strBuf.append("\n");
			strBuf.append("<guide>");
			strBuf.append("\n");
			strBuf.append("<start_value>" + (guides_min_value - Y_add_value) + "</start_value>");		// ����������ֵ
			strBuf.append("\n");
			strBuf.append("<width>" + guides_min_width + "</width>");					// ���������Ŀ��
			strBuf.append("\n");
			strBuf.append("<color>" + guides_min_color + "</color>");					// ������������ɫ
			strBuf.append("\n");
			strBuf.append("<dashed>" + guides_min_dashed + "</dashed>");				// ���������Ƿ���ʾ����
			strBuf.append("\n");
			strBuf.append("</guide> ");
			strBuf.append("\n");
			strBuf.append("</guides>");
		}
		
//		strBuf.append("<plugins>");														// ���
//		strBuf.append("<plugin file=\"../js/plugins/value_indicator.swf\" position=\"behind\">");
//		strBuf.append("<chart_type>line</chart_type>");
//		strBuf.append("<axis>left</axis>");
//		strBuf.append("</plugin>");
//		strBuf.append("</plugins>");
		
		strBuf.append("</settings>");
		
		return strBuf.toString();
	}
	
//	public static void main(String[] args){
//		BeanForFlashSetting bean = new BeanForFlashSetting();
//		List<String> aa = new ArrayList<String>();
//		aa.add("aa");
//		aa.add("bb");
//		bean.flashdata = aa;
//		bean.y_value_append = "%";
//		System.out.println(bean.toString());
//	}
	
}	


	
//----------------cmchart-Flash�����ʽ---------------------//
//	<settings> 
//	  <data_type>csv</data_type>			--���ݸ�ʽ
//	  <font>Georgia</font>					--����
//	  <js_enabled>false</js_enabled>		--�Ż�
//	  <grid>								--����
//	    <x>                                                       
//	      <approx_count>5</approx_count>	--��ʾ5��������
//	    </x>
//	    <y_left>                                                  
//	      <approx_count>10</approx_count>	--��ʾ10��������
//	    </y_left>        
//	  </grid>
	
//	  <values>                                                   
//	    <y_left>
//	      <unit>$</unit>								--Y���ǩ
//	      <unit_position>right</unit_position>			--��ǩ���ӵ�λ��(�������Ҳ�)
//	    </y_left>
//	  </values>  
//
//	  <legend>                                                    
//	    <values>                                                  
//	      <enabled>true</enabled>						--ͼ��
//	      <width>64</width>								--���
//	      <align>left</align>							--�����
//	      <text><![CDATA[: ${value}]]></text>           --��ʾ��ʽ
//	     </values>
//	  </legend>
//
//	  <indicator> 
//		<zoomable>true</zoomable>                       --���Ŵ��Ƿ���Ч
//	  </indicator>
//
//	  <strings>
//	    <no_data>aaaaaaaa</no_data>						--û������ʱ��ʾ�ı�ǩ
//	  </strings>
//
//	  <context_menu>
//		<menu function_name="printChart" title="aa chart"></menu>
//		<default_items>
//  	<print>false</print><!-- [true] (true / false) to show or not flash players print menu -->
//		</default_items>
//	  </context_menu>
//
//	  <labels>
//	  <label lid="0">
//	    <y>30</y> 
//	    <width>100%</width>
//	    <align>center</align>
//	    <text>
//	      <![CDATA[<b>Source: <a href="" target="_blank"><u>Historical Crude Oil Prices</u></a></b>]]>
//	    </text>        
//	  </label>     
//	  </labels>
//
//	  <graphs> 
//	    <graph gid="1"> 
//	      <title>Nominal</title>
//	      <bullet>round_outlined</bullet>
//	      <bullet_size>8</bullet_size>
//	      <balloon_text>
//	        <![CDATA[$<b>{value}]]>
//	      </balloon_text>
//	    </graph>
//	    
//	    <graph gid="2">                                                            
//	      <title>Inflation adjusted</title>       
//		  <bullet>round_outlined</bullet> 
//		  <bullet_size>8</bullet_size>       
//	      <balloon_text>
//	        <![CDATA[${value}]]>
//	      </balloon_text>
//	    </graph>
//	  </graphs>  
//	  
//		<guides>	
//		 <max_min>true</max_min>
//		 <guide>
//		   <start_value>-20</start_value>		--����ֵ
//		   <width></width>						--�������
//		   <color>#ff2571</color>				--������ɫ
//	     <dashed>true</dashed>					--�Ƿ�Ϊ����
//		 </guide>  
//
//		 <guide>
//		   <start_value>90</start_value>		--����ֵ
//		   <width></width>
//		   <color>#00ff00</color>
//	     <dashed>true</dashed>
//		 </guide> 
//		</guides>
//
//	   <plugins>								--���
//	    <plugin file="plugins/value_indicator.swf" position="behind"> 
//	      <chart_type>line</chart_type>
//	      <axis>left</axis>
//	    </plugin>
//	  </plugins>
//
//	</settings>

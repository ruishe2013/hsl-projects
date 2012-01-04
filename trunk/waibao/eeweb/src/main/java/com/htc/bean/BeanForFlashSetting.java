package com.htc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ BeanForMakeFlash.java
 * 作用 : amline_1.6.4.0 Flash生成Setting单元
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-12-2     YANGZHONLI       create
 */
public class BeanForFlashSetting implements Serializable  {
	
	private static final long serialVersionUID = 6385408787125351951L;
	/**
	 * Falsh数据格式:""空值为xml格式数据(显示多样化,数据量大),"csv"为csv格式数据(显示单一,有点数量小).默认:"csv"
	 */
	public String data_type = "csv";
	/**
	 * Falsh中数据的字体.默认:"Georgia"
	 */
	public String falsh_font = "Georgia";
	/**
	 * Falsh刷新间隔
	 */
	public int reload_data_interval = 0;
	/**
	 * Falsh字体大小
	 */
	public int falsh_font_size = 12;
	/**
	 * if your chart's width or height is set in percents, and redraw is set to true
	 */
	public boolean redraw = false;				//--需要修改
	/**
	 * 优化Flash,false为优化.默认:false
	 */
	public boolean js_enabled = false;
	/**
	 * X轴显示几个数据.默认显示5个横坐标.
	 */
	public int grid_x_show_count= 8;			//--需要适当修改
	/**
	 * 左边Y轴显示几个数据.默认显示10个横坐标.
	 */
	public int grid_y_show_count= 12;			//--需要适当修改
	/**
	 * Y轴标签需要添加的字符.默认没有添加
	 */
	public String y_value_append = "";			//--需要修改(℃或F或%)******
	/**
	 * 图例后缀
	 */
	public String legend_value_append = "";		//--图例
	/**
	 * Y轴标签需要添加的字符的位置.默认为:right
	 */
	public String y_value_append_position = "right";
	/**
	 * 是否显示图例[历史曲线设置false,即时曲线设置true]
	 */
	public boolean legend_enabled = true;		//--需要适当修改
	/**
	 * 图例的宽度
	 */
	public int legend_width = 60;				//--需要适当修改
	/**
	 * 是否需要鼠标放大效果
	 */
	public boolean zoomable_use = true;			//--需要适当修改
	/**
	 * 是否需要鼠标放大效果
	 */
	public boolean print_use = false;			//--打印有问题,设置false
	/**
	 * 没有数据时显示的字符
	 */
	public String no_data_str = "暂无数据";
	/**
	 * 标题
	 */
	public String label_name = "";				//--需要修改
	/**
	 * 标题-Y坐标位置
	 */
	public int label_name_y_position = 30;		//--需要适当修改
	/**
	 * 坐标上显示的圆点的大小
	 */
	public int bullet_size = 5;					//--需要适当修改
	
	/**
	 * 是否显示上限上下限线条.在实时曲线中使用
	 */
	public boolean guides_maxmin_show = false;	//--这个单元不用了.实时曲线中上下限控制在数据区域里面
	/**
	 * 上限线条的值
	 */
	public float guides_max_value = 40;			//--需要修改******
	/**
	 * 上限线条的颜色
	 */
	public String guides_max_color = "#00ff00";	//--需要修改******
	/**
	 * 上限线条的宽度
	 */
	public int guides_max_width = 2;
	/**
	 * 上限线条是否显示虚线.默认true,表示显示虚线
	 */
	public boolean guides_max_dashed = true;
	/**
	 * Y轴附加值,用来调整Y刻度高低
	 */
	public int Y_add_value = 0;
	/**
	 * 下限线条的值
	 */
	public float guides_min_value = 20;			//--需要修改******
	/**
	 * 下限线条的颜色
	 */
	public String guides_min_color = "#ff2571";	//--需要修改******
	/**
	 * 下限线条的宽度
	 */
	public int guides_min_width = 2;
	/**
	 * 下限线条是否显示虚线.默认true,表示显示虚线
	 */
	public boolean guides_min_dashed = true;
	
	/**
	 * flsh包含图例的List列表
	 */
	public List<String> flashdata = new ArrayList<String>();
	
	@Override
	public String toString() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<settings>");
		strBuf.append("\n");
		strBuf.append("<data_type>" + data_type + "</data_type>");		// 数据格式
		strBuf.append("\n");
		if (data_type.equals("xml")){									// 实时曲线用,flash更新数据间隔
			strBuf.append("<reload_data_interval>"+reload_data_interval+"</reload_data_interval>");
			strBuf.append("\n");
			strBuf.append("<add_time_stamp>true</add_time_stamp>");
			strBuf.append("\n");
		}
		strBuf.append("<font>" + falsh_font + "</font>"); 				// 字体
		strBuf.append("\n");
		strBuf.append("<decimals_separator>.</decimals_separator>");	// 小数分隔符号
		strBuf.append("\n");
		strBuf.append("<text_size>" + falsh_font_size + "</text_size>");
		strBuf.append("\n");
		strBuf.append("<redraw>" + redraw + "</redraw>");				// 自适应窗口大小
		strBuf.append("\n");
		strBuf.append("<js_enabled>" + js_enabled + "</js_enabled>");	// 优化
		strBuf.append("\n");
		
		strBuf.append("<grid>");
		strBuf.append("\n");
		strBuf.append("<x><approx_count>" + grid_x_show_count + "</approx_count></x>");// X轴显示几个数据
		strBuf.append("\n");
		strBuf.append("<y_left><approx_count>" + grid_y_show_count + "</approx_count></y_left>");// 左边Y轴显示几个数据
		strBuf.append("\n");
		strBuf.append("</grid>");
		strBuf.append("\n");
		
		strBuf.append("<values><y_left>");
		strBuf.append("\n");
		strBuf.append("<unit>" + y_value_append + "</unit>");							// Y轴标签需要添加的字符
		strBuf.append("\n");
		strBuf.append("<unit_position>"+y_value_append_position+"</unit_position>");	// Y轴标签需要添加的字符的位置.默认为:right
		strBuf.append("\n");
		strBuf.append("</y_left></values>  ");
		strBuf.append("\n");
		
		strBuf.append("<legend><values>");
		strBuf.append("\n");
		strBuf.append("<enabled>"+legend_enabled+"</enabled>");
		strBuf.append("\n");
		strBuf.append("<width>" + legend_width + "</width>");							// 图例的宽度
		strBuf.append("\n");
		strBuf.append("<align>left</align>");
		strBuf.append("\n");
		strBuf.append("<text><![CDATA[{value}"+ legend_value_append +"]]></text>");
		strBuf.append("\n");
		strBuf.append("</values></legend>");
		strBuf.append("\n");
		strBuf.append("<indicator><zoomable>" + zoomable_use + "</zoomable></indicator>");	// 是否需要鼠标放大效果
		strBuf.append("\n");
		
//		strBuf.append("<export_as_image>");									//导出图片的处理类
//		strBuf.append("<file>/htcweb/ExportImage.servlet</file>");
//		strBuf.append("<target>_blank</target>");
//		strBuf.append("</export_as_image>");
		
		
		strBuf.append("<strings>");
		strBuf.append("\n");
		strBuf.append("<no_data>" + no_data_str + "</no_data>");			// 没有数据时显示的字符
		strBuf.append("<error_in_data_file>" + no_data_str + "</error_in_data_file>");			// 没有数据时显示的字符
		strBuf.append("\n");
//		strBuf.append("<export_as_image>导出图片</export_as_image>");		// 导出图片文字
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
		
		if (guides_maxmin_show){				// 是否显示上限上下限线条
			strBuf.append("<guides>");
			strBuf.append("\n");
			strBuf.append("<max_min>true</max_min>");
			strBuf.append("\n");
			strBuf.append("<guide>");
			strBuf.append("\n");
			strBuf.append("<start_value>" + (guides_max_value + Y_add_value) + "</start_value>");		// 上限线条的值
			strBuf.append("\n");
			strBuf.append("<width>" + guides_max_width + "</width>");					// 上限线条的宽度
			strBuf.append("\n");
			strBuf.append("<color>" + guides_max_color + "</color>");					// 上限线条的颜色
			strBuf.append("\n");
			strBuf.append("<dashed>" + guides_max_dashed + "</dashed>");				// 上限线条是否显示虚线
			strBuf.append("\n");
			strBuf.append("</guide> ");
			strBuf.append("\n");
			strBuf.append("<guide>");
			strBuf.append("\n");
			strBuf.append("<start_value>" + (guides_min_value - Y_add_value) + "</start_value>");		// 下限线条的值
			strBuf.append("\n");
			strBuf.append("<width>" + guides_min_width + "</width>");					// 下限线条的宽度
			strBuf.append("\n");
			strBuf.append("<color>" + guides_min_color + "</color>");					// 下限线条的颜色
			strBuf.append("\n");
			strBuf.append("<dashed>" + guides_min_dashed + "</dashed>");				// 下限线条是否显示虚线
			strBuf.append("\n");
			strBuf.append("</guide> ");
			strBuf.append("\n");
			strBuf.append("</guides>");
		}
		
//		strBuf.append("<plugins>");														// 插件
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


	
//----------------cmchart-Flash定义格式---------------------//
//	<settings> 
//	  <data_type>csv</data_type>			--数据格式
//	  <font>Georgia</font>					--字体
//	  <js_enabled>false</js_enabled>		--优化
//	  <grid>								--网格
//	    <x>                                                       
//	      <approx_count>5</approx_count>	--显示5个横坐标
//	    </x>
//	    <y_left>                                                  
//	      <approx_count>10</approx_count>	--显示10个纵坐标
//	    </y_left>        
//	  </grid>
	
//	  <values>                                                   
//	    <y_left>
//	      <unit>$</unit>								--Y轴标签
//	      <unit_position>right</unit_position>			--标签附加的位置(这里是右侧)
//	    </y_left>
//	  </values>  
//
//	  <legend>                                                    
//	    <values>                                                  
//	      <enabled>true</enabled>						--图例
//	      <width>64</width>								--宽度
//	      <align>left</align>							--左对齐
//	      <text><![CDATA[: ${value}]]></text>           --显示格式
//	     </values>
//	  </legend>
//
//	  <indicator> 
//		<zoomable>true</zoomable>                       --鼠标放大是否有效
//	  </indicator>
//
//	  <strings>
//	    <no_data>aaaaaaaa</no_data>						--没有数据时显示的标签
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
//		   <start_value>-20</start_value>		--下限值
//		   <width></width>						--线条宽度
//		   <color>#ff2571</color>				--线条颜色
//	     <dashed>true</dashed>					--是否为虚线
//		 </guide>  
//
//		 <guide>
//		   <start_value>90</start_value>		--上限值
//		   <width></width>
//		   <color>#00ff00</color>
//	     <dashed>true</dashed>
//		 </guide> 
//		</guides>
//
//	   <plugins>								--插件
//	    <plugin file="plugins/value_indicator.swf" position="behind"> 
//	      <chart_type>line</chart_type>
//	      <axis>left</axis>
//	    </plugin>
//	  </plugins>
//
//	</settings>

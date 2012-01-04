<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="${pageContext.request.contextPath}/main/datepicker/WdatePicker.js"></script>	
	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/histroy.js"></script>

	<body>

	<s:include value="../common/includetop.jsp" />
	
	<%-- 用户选择的地址列表  --%>
	<s:hidden name="maxSelector" />
	<s:hidden name="showType" id="showType" />
	<s:hidden name="max_rs_now" id="max_rs_now"/>
	<s:hidden name="max_rs_day" id="max_rs_day"/>
	<s:hidden name="max_rs_month" id="max_rs_month"/>
	
	<%-- 搜索报表/曲线 --%>
	<div class="main_content1" >
		<div align="left" style="padding-left:20px; font-weight:bold;">历史数据--[历史<s:property value="showType == 1?'报表':'曲线'"/>]查询</div>
		<%-- 搜索条件 --%>
		<div class="historydata_check">
			<s:form namespace="/main" name="statForm" theme="simple" cssClass="formOutLine">
				<s:hidden name="timeFrom" id="timeFrom"/>
				<s:hidden name="timeTo" id="timeTo"/>
				<s:hidden name="placeListids" id="placeListids"/>
				<table cellspacing="0" cellpadding="0" width="100%" border="0">
					<tr>
						<td width="30%">
							<label><strong>[<s:property value="showType == 1?'报表':'曲线'"/>类型]</strong></label>
							<label for="report_type_A">
								<input type="radio" name="report_type" id="report_type_A" value="1" checked="checked"/>
								流水<s:property value="showType == 1?'报表':'曲线'"/>			  	
							</label>
							<label for="report_type_B">				
								<input type="radio" name="report_type" id="report_type_B" value="2"/>
								日<s:property value="showType == 1?'报表':'曲线'"/>			  	
							</label>
							<label for="report_type_C">				
								<input type="radio" name="report_type" id="report_type_C" value="3"/>			  	
								月<s:property value="showType == 1?'报表':'曲线'"/>
							</label>			
						</td>
						<td width="30%">
							<label><strong>[统计类型]</strong></label>
							<label for="statAvg" style="cursor: pointer">
								<input type="<s:property value="showType == 1?'checkbox':'radio'"/>" 
								name="statBox" id="statAvg" value="100" checked="checked" />平均值
							</label>
							<label for="statMax" style="cursor: pointer">
								<input type="<s:property value="showType == 1?'checkbox':'radio'"/>" 
									name="statBox" id="statMax" value="1000">最大值
							</label>
							<label for="statMix" style="cursor: pointer">
								<input type="<s:property value="showType == 1?'checkbox':'radio'"/>" 
								name="statBox" id="statMix" value="10" />最小值
							</label>
						</td>
						<td width="40%">
							<label id="areaTipShow" ><strong style="color:#FF3300;">
									有效查询时间范围:<label id="areaTipLabel"><s:property value="max_rs_now"/>分钟</label>
							</strong></label>
						</td>
					</tr>
					<tr>
						<td>
							<div class="fromTo_A">
								<label><strong>[开始时间]</strong></label>
								<input type="text" id="fromTime_A" class="Wdate" style="width: 200px"
									onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日 HH时mm分ss秒',
										maxDate:'%y-%M-%d %H:%m:%s',vel:'fromShow_A'})"/>
								<input type="hidden" name="timeFrom_A" id="fromShow_A"/>			
							</div>
							<div class="fromTo_B" style="float: left;">
								<label><strong>[开始时间]</strong></label>
								<input type="text" id="fromTime_B" class="Wdate" style="width: 120px" 
									onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日',maxDate:'%y-%M-%d',vel:'fromShow_B'})"/>						
								<input type="hidden" name="timeFrom_B" id="fromShow_B"/>			
							</div>
							<div class="fromTo_C" style="float: left;">
								<label><strong>[开始时间]</strong></label>
								<input type="text" id="fromTime_C" class="Wdate" style="width: 100px"
									onfocus="WdatePicker({dateFmt:'yyyy年MM月',maxDate:'%y-%M-1',vel:'fromShow_C'})" />
								<input type="hidden" name="timeFrom_C" id="fromShow_C"/>
							</div>			
						</td>
						<td>
							<div class="fromTo_A">
								<label><strong>[结束时间]</strong></label>
								<input type="text" id="toTime_A" class="Wdate" style="width: 200px"
									onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日 HH时mm分ss秒',
										minDate:'#F{$dp.$D(\'fromTime_A\');}',maxDate:'%y-%M-%d %H:%m:%s',vel:'toShow_A'})" />
								<input type="hidden" name="timeTo_A" id="toShow_A" />		
							</div>
							<div class="fromTo_B" style="float: left;">
								<label><strong>[结束时间]</strong></label>
								<input type="text" id="toTime_B" class="Wdate" style="width: 120px"
									onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日',minDate:'#F{$dp.$D(\'fromTime_B\');}',maxDate:'%y-%M-%d',vel:'toShow_B'})"/>							
								<input type="hidden" name="timeTo_B" id="toShow_B" />		
							</div>
							<div class="fromTo_C" style="float: left;">
								<label><strong>[结束时间]</strong></label>
								<input type="text" id="toTime_C" class="Wdate" style="width: 100px" name="timeToStr"
									onfocus="WdatePicker({dateFmt:'yyyy年MM月',minDate:'#F{$dp.$D(\'fromTime_C\');}',maxDate:'%y-%M-1',vel:'toShow_C'})" />
								<input type="hidden" name="timeTo_C" id="toShow_C" />		
							</div>														
						</td>
						<td class="user_list_01">
				  		<label id="interval_label"><strong>[间隔]</strong></label>
				  		<s:select list="searchInterval" name="interval" id="interval" listKey="key" listValue="value"/>
				  		<a href="#" onclick="submitForm();" id="submit_label">[查询流水<s:property value="showType == 1?'报表':'曲线'"/>]</a>
						</td>
					</tr>
				</table>				
			</s:form>
		</div>
		
		<%-- 搜索条件-地址列表 --%>		
		<s:if test="areaToEqList.size == 0">
			<div class="historydata_check">
				<div class="list_no_data">系统中,仪器列表为空...</div>
			</div>									
		</s:if>
		<s:else>
			<s:iterator value="areaToEqList" status="st" id="lists" >
			  <div class="historydata_data">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    	<thead>
				      <tr>
				        <td class="historydata_data_title">
									<a id="sel<s:property	value="#st.count"/>" style="cursor: pointer;">
										<s:property value="key"/></a>
				        </td>
				      </tr>
				    </thead>
				    <tbody>
				    	<tr>
				        <td>
				        	<div class="stat-box" id="_sel<s:property	value="#st.count"/>">
				        		<ul><s:property value="value" escape="false"/></ul>
				        	<div>
				        </td>
			        </tr>
				    </tbody>
			    </table>
			  </div>			
			</s:iterator>			
		</s:else>		
		
		<%-- 搜索验证显示区 --%>
		<div class="div_error" id="errorArea"  style="display:none;"></div>
	</div>	
		
	<!-- includtop最外层结束 -->		
	</div>	

	</body>
</html>

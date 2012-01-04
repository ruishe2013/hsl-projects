<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

	<%@ page language="java" contentType="text/html; charset=gbk"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<script type="text/javascript" src="../js/jquery-1.3.2.js"></script>
	<script type="text/javascript" src="../js/page/histroywarn.js"></script>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/main/datepicker/WdatePicker.js"></script>
	<style type="text/css">
		.label_space {text-align:left; margin-left: 30px;} 
	</style>

	<body>

	<s:include value="../common/includetop.jsp" />
	
	<%-- 用户选择的地址列表  --%>
	<s:hidden name="maxSelector" />
	<s:hidden name="max_rs_alarm" id="max_rs_alarm"/>
	
	<%-- 搜索历史报警 --%>
	<div class="main_content1">
		<div align="left" style="padding-left:20px; font-weight:bold;">报警数据查询</div>
		<%-- 搜索条件 --%>
		<div class="historydata_check">
		<s:form namespace="/main" name="warnForm" theme="simple" cssClass="formOutLine">
			<s:hidden name="placeListids" id="placeListids"/>
			<table cellspacing="0" cellpadding="0" width="100%" border="0">
				<tr>
					<td width="30%">
						<label><strong>[复位状态]</strong></label>
						<label for="do_reset" style="cursor: pointer">
							<input type="radio"	name="resetBox" id="do_reset" value="1" checked="checked" />已复位
						</label>
						<label for="undo_reset" style="cursor: pointer">						
							<input type="radio"	name="resetBox" id="undo_reset" value="2" />未复位
						</label>								
					</td>
					<td width="30%">
						<label><strong>[时间类型]</strong></label>
						<label for="statwarn" style="cursor: pointer">
							<input type="radio"	name="timeBox" id="statwarn" value="1" checked="checked" />报警时间
						</label>
						<label for="statreset" style="cursor: pointer" id="statresetLabel">
							<input type="radio"	name="timeBox" id="statreset" value="2" />复位时间
						</label>							
					</td>
					<td width="40%">
						<label><strong>[排序类型]</strong></label>
						<label for="orderB" style="cursor: pointer" title="最新的记录排在最前面">
							<input type="radio"	name="orderBox" id="orderB" value="2" checked="checked"/>降序
						</label>			
						<label for="orderA" style="cursor: pointer" title="最新的记录排在最后面">
							<input type="radio"	name="orderBox" id="orderA" value="1" />升序
						</label>
						<label class="label_space" id="areaTipShow">
								<strong style="color:#FF3300;">有效查询时间范围:<label><s:property value="max_rs_alarm"/>年</label></strong>
						</label>												
					</td>
				</tr>
				<tr>
					<td>
						<label><strong>[报警类型]</strong></label>
						<s:if test="systemEqType !=3">
							<label for="max_temp" style="cursor: pointer">
								<input type="checkbox"	name="warnBoxForTemp" id="max_temp" value="2" checked="checked"/>高温		
							</label>
							<label for="min_temp" style="cursor: pointer">				
								<input type="checkbox"	name="warnBoxForTemp" id="min_temp" value="1" checked="checked"/>低温		
							</label>				
						</s:if>
						<s:if test="systemEqType !=2">
							<label for="max_humi" style="cursor: pointer">
								<input type="checkbox"	name="warnBoxForHumi" id="max_humi" value="20" checked="checked"/>高湿		
							</label>
							<label for="min_humi" style="cursor: pointer">				
								<input type="checkbox"	name="warnBoxForHumi" id="min_humi" value="10" checked="checked"/>低湿		
							</label>
						</s:if>						
					</td>
					<td>
						<label><strong>[数量限制]</strong></label>
						前	<input type="text" id="maxcount" name="maxcount" maxlength="8" value="100" size="8"/>条记录
					</td>
					<td>
						<div id="doperson_label">
							<label for="doperson" style="cursor: pointer"><strong>[复位人员]</strong>
								<input type="checkbox" id="doperson" name="doperson" value="1"/>
							</label>						
							<input id="do_reset_person" name="doSetPerson" type="text" size="20" maxlength="20"/>
						</div> 
					</td>
				</tr>
				<tr>
					<td>
						<label><strong>[开始时间]</strong></label>
						<input type="text" id="fromTime" name="fromTime" class="Wdate" style="width: 200px"
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日 HH时mm分ss秒', 
							maxDate:'%y-%M-%d %H:%m:%s',vel:'fromShow'})"/>
						<input type="hidden" name="timeFrom" id="fromShow"/>								
					</td>
					<td>
						<label><strong>[结束时间]</strong></label>
						<input type="text" id="toTime" name="toTime" class="Wdate" style="width: 200px"
							onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy年MM月dd日 HH时mm分ss秒', 
							minDate:'#F{$dp.$D(\'fromTime\');}',maxDate:'%y-%M-%d %H:%m:%s',vel:'toShow'})" />
						<input type="hidden" name="timeTo" id="toShow" />		
					</td>
					<td class="user_list_01">
						<a href="#" onclick="submitForm();" id="submit_label">[查询 报警数据]</a>
					</td>
				</tr>
			</table>
		</s:form>
		</div>
	
		<%-- 搜索条件-地址列表 --%>		
		<s:if test="areaToEqList.size == 0">
			<div class="historydata_check">
				<div class="list_no_data">当前系统中,仪器列表为空...</div>
			</div>									
		</s:if>
		<s:else>
			<s:iterator value="areaToEqList" status="st" id="lists" >
			  <div class="historydata_data" id="_sel<s:property	value="#st.count"/>">
			    <table width="100%" border="0" cellspacing="0" cellpadding="0">
			    	<thead>
				      <tr>
				        <td class="historydata_data_title" colspan="5">
									<a id="sel<s:property	value="#st.count"/>" style="cursor: pointer;">
										<s:property value="key"/>
									</a>
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

package com.aifuyun.snow.world.web.modules.screen.search;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.aifuyun.snow.world.biz.ao.together.OrderSearchAO;
import com.aifuyun.snow.world.biz.bo.search.SearchOrderDO;
import com.aifuyun.snow.world.biz.query.SearchOrderQuery;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.web.common.base.BaseScreen;
import com.zjuh.splist.web.RunData;
import com.zjuh.splist.web.TemplateContext;
import com.zjuh.sweet.lang.ConvertUtil;
import com.zjuh.sweet.lang.DateUtil;
import com.zjuh.sweet.result.Result;

public class BaiduOpenData extends BaseScreen {

	private OrderSearchAO orderSearchAO;

	private static int MAX_COUNT = 5000;

	private static int SEARCH_PAGE_SIZE = 100;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(RunData rundata, TemplateContext templateContext) {
		SearchOrderQuery searchOrderQuery = new SearchOrderQuery();
		searchOrderQuery.setPageSize(SEARCH_PAGE_SIZE);
		Calendar calendar = Calendar.getInstance();
		long now = ConvertUtil.toLong(DateUtil.formatDate(calendar.getTime(), "yyyyMMdd"), 0L);
		searchOrderQuery.setMinArriveTime(now);
		List<OrderStatusEnum> list = new LinkedList<OrderStatusEnum>();
		list.add(OrderStatusEnum.FINISH);
		searchOrderQuery.setExcludeStatus(list);

		List<OrderDO> provideOrders = new LinkedList<OrderDO>();
		int provideCount = 0;
		for (int pageNo = 1;; pageNo++) {
			searchOrderQuery.setPageNo(pageNo);

			Result result = orderSearchAO.searchOrder(searchOrderQuery);
			Long numFound = (Long) result.getModels().get("numFound");
			if (numFound == null) {
				searchOrderQuery.setTotalResultCount(0);
				break;
			}

			if (result.isSuccess()) {
				List<SearchOrderDO> orders = (List<SearchOrderDO>) result.getModels().get("orders");
				for (SearchOrderDO order : orders) {
					if (order.getStatus() != OrderStatusEnum.FINISH.getValue()) {
						provideOrders.add(order);
						if ((++provideCount) == MAX_COUNT) {
							break;
						}
					}
				}

				if (pageNo * SEARCH_PAGE_SIZE >= numFound.intValue()) {
					break;
				}
			} else {
				break;
			}
		}

		templateContext.put("orders", provideOrders);
		templateContext.put("count", provideCount);
	}

	public void setOrderSearchAO(OrderSearchAO orderSearchAO) {
		this.orderSearchAO = orderSearchAO;
	}
}

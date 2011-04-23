package com.aifuyun.snow.world.dal.ibatis.together;

import java.util.List;
import java.util.Map;

import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;
import com.zjuh.sweet.lang.CollectionUtil;

public class OrderDAOIbatis extends BaseIbatisDAO implements OrderDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderDO> queryRecentOrders(OrderQuery orderQuery) {
		return (List<OrderDO>)getSqlMapClientTemplate().queryForList("OrderDAO.queryRecentOrders", orderQuery);
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderDO> queryRecentTypeOrders(OrderQuery orderQuery) {
		return (List<OrderDO>)getSqlMapClientTemplate().queryForList("OrderDAO.queryRecentTypeOrders", orderQuery);
	}

	@Override
	public long create(OrderDO orderDO) {
		return (Long)this.getSqlMapClientTemplate().insert("OrderDAO.create", orderDO);
	}

	@Override
	public void delete(long id) {
		this.getSqlMapClientTemplate().update("OrderDAO.delete", id);
	}

	@Override
	public OrderDO queryById(long id) {
		return (OrderDO)getSqlMapClientTemplate().queryForObject("OrderDAO.queryById", id);
	}

	@Override
	public void update(OrderDO orderDO) {
		getSqlMapClientTemplate().update("OrderDAO.update", orderDO);
	}
	
	public void updateStatus(long id, OrderStatusEnum orderStatus) {
		// 只更新一个status字段，比update方法高效
		Map<String, Object> param = CollectionUtil.newHashMap();
		param.put("id", id);
		param.put("orderStatus", orderStatus.getValue());
		getSqlMapClientTemplate().update("OrderDAO.updateStatus", param);
	}

}

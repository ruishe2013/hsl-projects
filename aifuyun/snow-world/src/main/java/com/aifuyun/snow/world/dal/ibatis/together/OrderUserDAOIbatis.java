package com.aifuyun.snow.world.dal.ibatis.together;

import java.util.List;
import java.util.Map;

import com.aifuyun.snow.world.dal.daointerface.together.OrderUserDAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;
import com.zjuh.sweet.lang.CollectionUtil;

public class OrderUserDAOIbatis extends BaseIbatisDAO implements OrderUserDAO {

	@Override
	public long create(OrderUserDO orderUserDO) {
		return (Long)this.getSqlMapClientTemplate().insert("OrderUserDAO.create", orderUserDO);
	}

	@Override
	public void delete(long id) {
		getSqlMapClientTemplate().update("OrderUserDAO.delete", id);
	}

	@Override
	public OrderUserDO queryById(long id) {
		return (OrderUserDO)getSqlMapClientTemplate().queryForObject("OrderUserDAO.queryById", id);
	}

	@Override
	public void update(OrderUserDO orderUserDO) {
		getSqlMapClientTemplate().update("OrderUserDAO.update", orderUserDO);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderUserDO> queryByOrderIdAndRole(long orderId, int role) {
		Map<String, Object> param = CollectionUtil.newHashMap();
		param.put("orderId", orderId);
		param.put("role", role);
		return (List<OrderUserDO>)getSqlMapClientTemplate().queryForList("OrderUserDAO.queryByOrderIdAndRole", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OrderUserDO> queryByOrderId(long orderId) {
		return (List<OrderUserDO>)getSqlMapClientTemplate().queryForList("OrderUserDAO.queryByOrderId", orderId);
	}

}

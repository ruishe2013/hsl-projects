package com.aifuyun.snow.world.dal.ibatis.together;

import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.ibatis.BaseIbatisDAO;

public class OrderDAOIbatis extends BaseIbatisDAO implements OrderDAO {

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

}

package com.htc.dao.iface;

import java.util.List;
import com.htc.domain.Equitype;

public interface EquiTypeDao {

	public List<Equitype> findAllEquitype() throws Exception;

}

package com.example.dao.impl;

import java.util.Date;
import java.util.List;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.Assignment;
import com.datastax.driver.core.querybuilder.Clause;
import com.example.been.CarAddress;

public interface CarAddressDao {
	ResultSet add(String[] keys,Object[] values);
	ResultSet delete(List<Clause> clauses);
	ResultSet update(List<Assignment> assignments,List<Clause> clauses);
	ResultSet query(Clause clause,String... columns);
	
	
	CarAddress add(CarAddress carAddress);
	List<CarAddress> queryAll();
	CarAddress queryById(String id);
	CarAddress queryById(String id,Date startTime,Date endTime);
	
	void deleteByPrimaryKey(String id);
	void deleteAllByPrimaryKey();
	
	void updateOneBaseInfo(CarAddress newCarAddress,CarAddress oldCarAddress);
	void batchUpdateOneBaseInfo(List<CarAddress> newCarAddresses);
	void updateOneAllInfo(CarAddress carAddress);
}

package com.example.dao;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.example.been.CarAddress;
//http://www.datastax.com/documentation/developer/java-driver/2.1/java-driver/reference/crudOperations.html
public class CarAddressMapping {
	private  Session session ;
	private  Mapper<CarAddress> mapper ;
	public CarAddressMapping(){
		session = ConnectSession.getInstance().getSession();
		mapper = new MappingManager(session).mapper(CarAddress.class);
	}
	
}

package com.example.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;

public class ConnectSession {
	private static ConnectSession instance ;

	private  Session session = null ; 
	private  Cluster cluster = null ;
	
	
	private static final String IP = "127.0.0.1" ;
	private static final String KEYSPACE = "addrkeyspace" ;
	
	private ConnectSession() {
		
		if(session==null || session.isClosed()){
			if(cluster==null || cluster.isClosed()){
				QueryOptions options = new QueryOptions();
				cluster = Cluster.builder()
		                .addContactPoint(IP)
//		                .withCredentials(username, password)
		                 .withQueryOptions(options)
		                .build();
			}
			session = cluster.connect(KEYSPACE) ;
		}
		
	};
	public static ConnectSession getInstance(){
		if(instance==null){
			instance = new ConnectSession();
		}
		return instance ;
	}
	public   Session getSession(){
		return session ;
	}
	public  void closeConn(){
		if(session!=null && !session.isClosed())
			session.close();
		if(cluster!=null && !cluster.isClosed())
			cluster.close();
	}
}

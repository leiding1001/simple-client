package com.example.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.querybuilder.Assignment;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.UDTMapper;
import com.example.been.CarAddress;
import com.example.been.Coordinate;
import com.example.been.PositionInfo;
import com.example.dao.impl.CarAddressDao;

public class CarAddressDaoImpl implements CarAddressDao{
	
	private static final String TABLE = "caraddress" ;
	private Session session ;
	private  UDTMapper<PositionInfo> baseparaMapper ;
	private  UDTMapper<Coordinate> coorMapper ;
	
	
	public CarAddressDaoImpl(){
		session = ConnectSession.getInstance().getSession();
		
		baseparaMapper = new MappingManager(session).udtMapper(PositionInfo.class) ;
		coorMapper = new MappingManager(session).udtMapper(Coordinate.class) ;
		
	}
//	RegularStatement insert = QueryBuilder.insertInto(TABLE).values(new String[] {"a", "b", "c"}, new Object[] {1, 2, 3});
	public ResultSet add(String[] keys, Object[] values) {
		RegularStatement insert = QueryBuilder.insertInto(TABLE).values(keys, values);
		ResultSet rs = session.execute(insert);
		return rs;
	}
	public CarAddress add(CarAddress carAddress){
		if(carAddress==null || carAddress.getId()==null)
			return null;
//		CarAddress oldCarAddress = queryById(carAddress.getId()) ;
//		if(oldCarAddress==null){//update
			Map<String, Object> map = new HashMap<String, Object>() ;
			map.put("id", carAddress.getId());
			if(carAddress.getStartTime()!=null)
				map.put("starttime", carAddress.getStartTime());
			if(carAddress.getMin_coordinate()!=null){
				map.put("min_coordinate", coorMapper.toUDT(carAddress.getMin_coordinate()));
			}
			if(carAddress.getMax_coordinate()!=null){
				map.put("max_coordinate", coorMapper.toUDT(carAddress.getMax_coordinate()));
			}
			if(carAddress.getData()!=null&&carAddress.getData().size()>0){
				Set<UDTValue>  data = new HashSet<UDTValue>();
				for(PositionInfo baseparameter:carAddress.getData()){
					data.add(baseparaMapper.toUDT(baseparameter));
				}
				map.put("data", data);
			}
			String[] keys = new String[map.keySet().size()] ;
			Object[] values = new Object[map.keySet().size()] ; 
			map.keySet().toArray(keys);
			map.values().toArray(values);
			add(keys,values) ;
//		}
		return carAddress ;
	}
	
    
//	RegularStatement delete = QueryBuilder.delete().from(TABLE).where(QueryBuilder.eq("a", 1));
	public ResultSet delete(List<Clause> clauses) {
		Delete delete = QueryBuilder.delete().from(TABLE);
		if(clauses!=null){
			for(Clause clause :clauses){
				delete.where().and(clause);
			}
		}
		ResultSet rs = session.execute(delete) ;
		return rs;
	}
	public void deleteByPrimaryKey(String id){
		List<Clause> clauses = new ArrayList<Clause>();
		if(id!=null && id.length()>0){
			clauses.add(QueryBuilder.eq("id",id));
		}
		delete(clauses);
		System.out.println("============>deleted  id="+id);
	}
	
//	RegularStatement update = QueryBuilder.update(TABLE).with(QueryBuilder.set("b", 6)).where(QueryBuilder.eq("a", 3));
	public ResultSet update(List<Assignment> assignments,List<Clause> clauses) {
		Update  update = QueryBuilder.update(TABLE);
		
		
		if(assignments!=null&&assignments.size()>0){
			for(Assignment assignment :assignments){
				update.with().and(assignment);
			}
		}
		if(clauses!=null && clauses.size()>0){
			for(Clause clause :clauses){
				update.where().and(clause);
			}
		}
		ResultSet rs = session.execute(update);
		return rs;
	}
	public Update getUpdate(List<Assignment> assignments,List<Clause> clauses) {
		Update  update = QueryBuilder.update(TABLE);
		if(assignments!=null&&assignments.size()>0){
			for(Assignment assignment :assignments){
				update.with().and(assignment);
			}
		}
		if(clauses!=null && clauses.size()>0){
			for(Clause clause :clauses){
				update.where().and(clause);
			}
		}
		return update;
	}
	public ResultSet batchUpdate(List<Update> updates){
		if(updates==null || updates.size()==0)
			return null;
		 Batch batch = QueryBuilder.batch();
		 for (int i = 0,len = updates.size(); i < len; i++) {
			 batch.add(updates.get(i));
		}
		 ResultSet rs = session.execute(batch);
		return rs;	 
	}
	public void updateOneAllInfo(CarAddress carAddress){//but id
		if(carAddress==null || carAddress.getId()==null)
			return ;
		
		List<Assignment> assignments = new ArrayList<Assignment>() ;
		List<Clause> clauses = new ArrayList<Clause>();
		clauses.add(QueryBuilder.eq("id", carAddress.getId()));
		if(carAddress.getStartTime()!=null)
			clauses.add(QueryBuilder.eq("starttime", carAddress.getStartTime()));
		if(carAddress.getMin_coordinate()!=null){
			assignments.add(QueryBuilder.set("min_coordinate", coorMapper.toUDT(carAddress.getMin_coordinate())));
		}
		if(carAddress.getMax_coordinate()!=null){
			assignments.add(QueryBuilder.set("max_coordinate", coorMapper.toUDT(carAddress.getMax_coordinate())));
		}
		if(carAddress.getData()!=null&&carAddress.getData().size()>0){
			Set<UDTValue>  data = new HashSet<UDTValue>();
			for(PositionInfo baseparameter:carAddress.getData()){
				data.add(baseparaMapper.toUDT(baseparameter));
			}
			assignments.add(QueryBuilder.addAll("data", data));
		}
		update(assignments, clauses) ;
	}
	public void batchUpdateOneBaseInfo(List<CarAddress> newCarAddresses){//but id ,startTime,
		if(newCarAddresses==null || newCarAddresses.size()==0)
			return ;
		CarAddress newCarAddress = null ;
		List<Update> updates = new ArrayList<Update>();
		List<Clause> clauses = new ArrayList<Clause>();
		List<Assignment> assignments = new ArrayList<Assignment>() ;
		for(int i=0,len=newCarAddresses.size();i<len;i++){
			newCarAddress = newCarAddresses.get(i);
			clauses.clear();
			assignments.clear();
			clauses.add(QueryBuilder.eq("id", newCarAddress.getId()));
			if(newCarAddress.getStartTime()!=null)
				clauses.add(QueryBuilder.eq("starttime", newCarAddress.getStartTime()));
			
			if(newCarAddress.getMin_coordinate()!=null){
				assignments.add(QueryBuilder.set("min_coordinate", coorMapper.toUDT(newCarAddress.getMin_coordinate())));
			}
			
			if(newCarAddress.getMax_coordinate()!=null){
				assignments.add(QueryBuilder.set("max_coordinate", coorMapper.toUDT(newCarAddress.getMax_coordinate())));
			}
			if(newCarAddress.getData()!=null&&newCarAddress.getData().size()>0){
				Set<UDTValue>  data = new HashSet<UDTValue>();
				for(PositionInfo baseparameter:newCarAddress.getData()){
					data.add(baseparaMapper.toUDT(baseparameter));
				}
				assignments.add(QueryBuilder.addAll("data", data));
			}
			
			updates.add(getUpdate(assignments, clauses)) ;
		}
		batchUpdate(updates);
	}
	public void updateOneBaseInfo(CarAddress newCarAddress,CarAddress oldCarAddress){//but id ,startTime,
		if(newCarAddress==null || newCarAddress.getId()==null)
			return ;
		
		List<Clause> clauses = new ArrayList<Clause>();
		clauses.add(QueryBuilder.eq("id", newCarAddress.getId()));
		if(newCarAddress.getStartTime()!=null)
			clauses.add(QueryBuilder.eq("starttime", newCarAddress.getStartTime()));
		
		List<Assignment> assignments = new ArrayList<Assignment>() ;
		
		if(newCarAddress.getMin_coordinate()!=null){
			Coordinate coordinate = null ;
			if(oldCarAddress.getMin_coordinate()==null)
				coordinate = newCarAddress.getMin_coordinate() ;
			else{
				coordinate = new Coordinate() ;
				if(newCarAddress.getMin_coordinate().getLatitude()<=oldCarAddress.getMin_coordinate().getLatitude())
					coordinate.setLatitude(newCarAddress.getMin_coordinate().getLatitude());
				else
					coordinate.setLatitude(oldCarAddress.getMin_coordinate().getLatitude());
				
				if(newCarAddress.getMin_coordinate().getLongitude()<=oldCarAddress.getMin_coordinate().getLongitude())
					coordinate.setLongitude(newCarAddress.getMin_coordinate().getLongitude());
				else
					coordinate.setLongitude(oldCarAddress.getMin_coordinate().getLongitude());
			}
			if(coordinate.getLatitude() !=oldCarAddress.getMin_coordinate().getLatitude() ||
						coordinate.getLatitude() != oldCarAddress.getMin_coordinate().getLongitude()){
				oldCarAddress.setMin_coordinate(coordinate);
				assignments.add(QueryBuilder.set("min_coordinate", coorMapper.toUDT(coordinate)));
			}
		}
		if(newCarAddress.getMax_coordinate()!=null){
			Coordinate coordinate = null ;
			if(oldCarAddress.getMax_coordinate()==null)
				coordinate = newCarAddress.getMax_coordinate() ;
			else{
				coordinate = new Coordinate() ;
				if(newCarAddress.getMax_coordinate().getLatitude()>=oldCarAddress.getMax_coordinate().getLatitude())
					coordinate.setLatitude(newCarAddress.getMax_coordinate().getLatitude());
				else
					coordinate.setLatitude(oldCarAddress.getMax_coordinate().getLatitude());
				
				if(newCarAddress.getMax_coordinate().getLongitude()>=oldCarAddress.getMax_coordinate().getLongitude())
					coordinate.setLongitude(newCarAddress.getMax_coordinate().getLongitude());
				else
					coordinate.setLongitude(oldCarAddress.getMax_coordinate().getLongitude());
			}
			if(coordinate.getLatitude() !=oldCarAddress.getMax_coordinate().getLatitude() ||
					coordinate.getLatitude() != oldCarAddress.getMax_coordinate().getLongitude()){
				oldCarAddress.setMax_coordinate(coordinate);
				assignments.add(QueryBuilder.set("max_coordinate", coorMapper.toUDT(coordinate)));
			}
		}
		if(newCarAddress.getData()!=null&&newCarAddress.getData().size()>0){
			Set<UDTValue>  data = new HashSet<UDTValue>();
			for(PositionInfo baseparameter:newCarAddress.getData()){
				data.add(baseparaMapper.toUDT(baseparameter));
			}
			assignments.add(QueryBuilder.addAll("data", data));
		}
		
		update(assignments, clauses) ;
	}
//	RegularStatement select = QueryBuilder.select().from(KEYSPACE, TABLE).where(QueryBuilder.eq("id", "Hu1102000"));
	public ResultSet query(Clause clause, String... columns) {
		
		Select select = QueryBuilder.select(columns).from(TABLE).limit(1000);
		if(clause!=null)
			select.where(clause);
		ResultSet rs = session.execute(select);
		return rs;
	}
	public  boolean  isExsist(String id){//"Hu1102000"
        ResultSet rs = query(QueryBuilder.eq("id", id), "id","startTime","min_coordinate","max_coordinate","data");
        Iterator<Row> iterator = rs.iterator();
        if (iterator.hasNext())
        	return true ;
        return false;
    }
	public  CarAddress queryById(String id){//"Hu1102000"
		CarAddress carAddress =null ;
        ResultSet rs = query(QueryBuilder.eq("id", id), "id","startTime","min_coordinate","max_coordinate","data");
        Iterator<Row> iterator = rs.iterator();
        if (iterator.hasNext())
        {
        	carAddress = new CarAddress();
        	Row row = iterator.next();
        	carAddress.setId(row.getString("id"));
        	carAddress.setStartTime(row.getDate("startTime"));
        	UDTValue min_UDTValue =  row.getUDTValue("min_coordinate") ;
            if(min_UDTValue!=null){
            	Coordinate min_coordinate = coorMapper.fromUDT(min_UDTValue);
            	carAddress.setMin_coordinate(min_coordinate);
            }
            UDTValue max_UDTValue =  row.getUDTValue("max_coordinate") ;
            if(max_UDTValue!=null){
            	Coordinate max_coordinate = coorMapper.fromUDT(max_UDTValue);
            	carAddress.setMax_coordinate(max_coordinate);
            }
            List<PositionInfo> positionInfos = new ArrayList<PositionInfo>();
            Set<UDTValue> data = row.getSet("data", UDTValue.class);
            
            if(data!=null){
            	Iterator<UDTValue> it = data.iterator();
            	
            	while(it.hasNext()){
            		positionInfos.add(baseparaMapper.fromUDT(it.next())); 
            	}
            	carAddress.setData(positionInfos);
            }
        }
        return carAddress;
    }
	
	public  CarAddress queryById(String id,Date startTime,Date endTime){//"Hu1102000"
		CarAddress carAddress =null ;
        ResultSet rs = query(QueryBuilder.eq("id", id), "id","startTime","min_coordinate","max_coordinate","data");
        Iterator<Row> iterator = rs.iterator();
        if (iterator.hasNext())
        {
        	carAddress = new CarAddress();
        	Row row = iterator.next();
        	carAddress.setId(row.getString("id"));
        	carAddress.setStartTime(row.getDate("startTime"));
        	UDTValue min_UDTValue =  row.getUDTValue("min_coordinate") ;
            if(min_UDTValue!=null){
            	Coordinate min_coordinate = coorMapper.fromUDT(min_UDTValue);
            	carAddress.setMin_coordinate(min_coordinate);
            }
            UDTValue max_UDTValue =  row.getUDTValue("max_coordinate") ;
            if(max_UDTValue!=null){
            	Coordinate max_coordinate = coorMapper.fromUDT(max_UDTValue);
            	carAddress.setMax_coordinate(max_coordinate);
            }
            List<PositionInfo> positionInfos = new ArrayList<PositionInfo>();
            Set<UDTValue> data = row.getSet("data", UDTValue.class);
            
            if(data!=null){
            	Iterator<UDTValue> it = data.iterator();
            	
            	while(it.hasNext()){
            		PositionInfo positionInfo = baseparaMapper.fromUDT(it.next()) ;
            		if(positionInfo.getDate().getTime()<startTime.getTime())
            			continue;
            		if(positionInfo.getDate().getTime()>endTime.getTime())
            			break;
            		positionInfos.add(positionInfo); 
            		
            	}
            	carAddress.setData(positionInfos);
            }
        }
        return carAddress;
    }
	
    public   List<CarAddress> queryAll(){//"Hu1102000"
    	ResultSet rs = query(null, "id","startTime","min_coordinate","max_coordinate","data");
        Iterator<Row> iterator = rs.iterator();
        List<CarAddress> carAddresses = new ArrayList<CarAddress>();
        while (iterator.hasNext())
        {
        	CarAddress carAddress = new CarAddress();
        	Row row = iterator.next();
        	carAddress.setId(row.getString("id"));
        	carAddress.setStartTime(row.getDate("startTime"));
        	UDTValue min_UDTValue =  row.getUDTValue("min_coordinate") ;
            if(min_UDTValue!=null){
            	Coordinate min_coordinate = coorMapper.fromUDT(min_UDTValue);
            	carAddress.setMin_coordinate(min_coordinate);
            }
            UDTValue max_UDTValue =  row.getUDTValue("max_coordinate") ;
            if(max_UDTValue!=null){
            	Coordinate max_coordinate = coorMapper.fromUDT(max_UDTValue);
            	carAddress.setMax_coordinate(max_coordinate);
            }
            List<PositionInfo> positionInfos = new ArrayList<PositionInfo>();
            Set<UDTValue> data = row.getSet("data", UDTValue.class);
            
            if(data!=null){
            	Iterator<UDTValue> it = data.iterator();
            	
            	while(it.hasNext()){
            		PositionInfo positionInfo = baseparaMapper.fromUDT(it.next()) ;
            		positionInfos.add(positionInfo); 
            	}
            	carAddress.setData(positionInfos);
            }
//            System.out.println(carAddress.toString());
            carAddresses.add(carAddress);
        }
        return carAddresses ;
    }
	public void deleteAllByPrimaryKey() {
		List<CarAddress> carAddresses = queryAll() ;
		if(carAddresses!=null&&carAddresses.size()>0){
			for (int i = 0; i < carAddresses.size(); i++) {
				deleteByPrimaryKey(carAddresses.get(i).getId());
			}
		}
	}

}

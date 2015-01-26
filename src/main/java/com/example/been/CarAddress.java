package com.example.been;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Frozen;
import com.datastax.driver.mapping.annotations.FrozenValue;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "addrkeyspace", name = "caraddress")
public class CarAddress {
	@PartitionKey
	private String id ;
	@Column (name="starttime")
	private Date startTime ;
	@FrozenValue
	private List<PositionInfo> data ;
	@Frozen
	private Coordinate max_coordinate ;
	@Frozen
	private Coordinate min_coordinate ;
	
	public CarAddress(){}
	
	public CarAddress(String id,  Date startTime,
			List<PositionInfo> data, Coordinate max_coordinate,
			Coordinate min_coordinate) {
		this.id = id;
		this.startTime = startTime;
		this.data = data;
		this.max_coordinate = max_coordinate;
		this.min_coordinate = min_coordinate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public List<PositionInfo> getData() {
		return data;
	}

	public void setData(List<PositionInfo> data) {
		this.data = data;
	}

	public Coordinate getMax_coordinate() {
		return max_coordinate;
	}

	public void setMax_coordinate(Coordinate max_coordinate) {
		this.max_coordinate = max_coordinate;
	}

	public Coordinate getMin_coordinate() {
		return min_coordinate;
	}

	public void setMin_coordinate(Coordinate min_coordinate) {
		this.min_coordinate = min_coordinate;
	}
	@Override
	public String toString() {
		String str = "id:"+id+",startTime:"+startTime+"\n"
				+ "\tmin_coordinate:"+min_coordinate.toString()+"\n"
				+ "\tmax_coordinate:"+max_coordinate.toString()+"\n";
		if(data!=null&&data.size()>0){
			Iterator<PositionInfo> it = data.iterator() ;
			while(it.hasNext()){
				str+="\tdata:"+it.next().toString() +"\n";
			}
		}
		return str ;
	}
	
}

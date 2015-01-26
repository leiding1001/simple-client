package com.example.been;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.mapping.annotations.UDT;

@UDT (keyspace = "addrkeyspace", name = "positionInfo")
public class PositionInfo {
	
	public PositionInfo() {
		
	}
	public PositionInfo(Date date, double longitude, double latitude,
			float accuracy, float gasoline, float speed)  {
		this.date = date;
		this.longitude = longitude;
		this.latitude = latitude;
		this.accuracy = accuracy;
		this.gasoline = gasoline;
		this.speed = speed;
	}
	public Date date ;
	public double longitude ;
	public double latitude ;
	public float accuracy ;
	public float gasoline ;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public float getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}
	public float getGasoline() {
		return gasoline;
	}
	public void setGasoline(float gasoline) {
		this.gasoline = gasoline;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public float speed ;
	
	
	//[
	//	{date:0, longitude:111.0, latitude:222.0, accuracy:1.0, gasoline:1.0, speed:100.0}, 
	//	{date:1, longitude:222.0, latitude:333.0, accuracy:1.0, gasoline:1.0, speed:100.0}]

	public String toString(){
		return "{date："+new SimpleDateFormat("yyyyMMDDHHmm").format(date)+"，longitude："+longitude+"，latitude："+latitude+"，accuracy："+accuracy+"，gasoline："+gasoline+"，speed："+speed+"}";
	}
}

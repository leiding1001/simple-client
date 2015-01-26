package com.example.been;

import com.datastax.driver.mapping.annotations.UDT;

@UDT (keyspace = "addrkeyspace", name = "coordinate")
public class Coordinate {
	public double	longitude;
	public double	latitude ; 
	
	public Coordinate(){}
	
	public Coordinate(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
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

	public String toString(){
		return "{longitude:"+longitude+",latitude:"+latitude+"}" ;
	}
}

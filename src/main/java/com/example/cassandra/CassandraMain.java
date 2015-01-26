package com.example.cassandra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

import com.example.been.CarAddress;
import com.example.been.Coordinate;
import com.example.been.PositionInfo;
import com.example.dao.CarAddressDaoImpl;
import com.example.dao.ConnectSession;
import com.example.dao.impl.CarAddressDao;


public class CassandraMain
{
	private static final long ONE_DAY = 24*60*60*1000L;
	private static final long ONE_SECOND = 60*1000L;
	private static final CarAddressDao carAddressDao = new CarAddressDaoImpl(); 
	
	public static void main(String[] args)
    {
        
//      session.execute("CREATE  KEYSPACE kp WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': 1};");

//		CarAddress carAddress = carAddressDao.queryById("Hu1102000");

//		carAddressDao.deleteByPrimaryKey("Hu000020150121");
//		carAddressDao.deleteAllByPrimaryKey();\
		try {
			for(int i= 0;i<40;i++){
				new CassandraMain().addCarBaseInfo(500,5 ,i*500);//1000,50
					Thread.sleep(1000*60);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			addCarAddress("Hu1101");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		carAddressDao.queryAll();
//		CarAddress carAddress = carAddressDao.queryById("Hu099920150926");
		
//		Calendar calendar = Calendar.getInstance() ;
//		calendar.setTime(new Date());
//		calendar.set(Calendar.MONTH, 8);
//		calendar.set(Calendar.DATE, 26);
//		Date startTime = calendar.getTime();
//		calendar.add(Calendar.DATE, 1);
//		Date endTime = calendar.getTime() ;
//		
//		System.out.println("startTime=>"+startTime);
//		System.out.println("endTime=>"+endTime);
//		CarAddress carAddress = carAddressDao.queryById("Hu099920150926",startTime,endTime);
//		System.out.println(carAddress.toString());
//		ConnectSession.getInstance().closeConn();
		
		
    }
	
	/*
	 	addCarAddress("Hu1101");
		addCarAddress("Hu1102");
		addCarAddress("Hu1103");
		addCarAddress("Hu1104");
		addCarAddress("Hu1105");
	*/
	public static void addCarAddress(String id) throws ParseException{
		Random random = new Random(10000) ;
		Date time = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		double longitude = random.nextLong();
		double latitude = random.nextLong();
		CarAddress carAddress = new CarAddress (id+sdf.format(time),sdf.parse(sdf.format(time)),null,
				new Coordinate(longitude, latitude),
				new Coordinate(longitude, latitude)
				);
		List<PositionInfo> positionInfos = new ArrayList<PositionInfo>();
		sdf.applyPattern("yyyyMMddHHmm ");
		positionInfos.add(new PositionInfo(sdf.parse(sdf.format(time)), longitude, latitude, 333, 11, 11));
		carAddress.setData(positionInfos);
		carAddressDao.add(carAddress);
	}
	

	public  void addCarBaseInfo(int total,int time,int startIndex){
		
		int carNumberPer = total / time ;
		
		for(int i=0;i<time;i++){
			new Timer().schedule(new MyTimeTask("timeTask"+i, startIndex+carNumberPer*i, startIndex+carNumberPer*(i+1)), 1000); 
		}
		
		if(total % time !=0){
			new Timer().schedule(new MyTimeTask("timeTask"+time, carNumberPer*time, total % time), 1000); 
		}
		
	}
	public static String autoFillZero(int i){
		if(i>=1000)
			return i+"";
		else if(100<=i)
			return "0"+i ;
		else if(10<=i)
			return "00"+i ;
		else 
			return "000"+i ;
	}
	public class MyTimeTask extends java.util.TimerTask{
		private String timerTaskName ;
		private int startIndex;
		private int endIndex ;
		private int totaldays = 250 ;//250 days
		private int totalHours = 8 ;//8 hours
		private int totalMins = 60 ;//60mins
		MyTimeTask( String timerTaskName , int startIndex, int endIndex){
			this.timerTaskName = timerTaskName ;
			this.startIndex = startIndex ;
			this.endIndex = endIndex ;
		}
		@Override
		public void run() {
			
			System.out.println("============>"+timerTaskName+".start....<==============");
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2= new SimpleDateFormat("yyyyMMddHHmm");
			Calendar calendar = Calendar.getInstance();
			
			Date now = new Date() ;
			double  baseNumber = 10000 ;
			Random random = new Random() ;
			String idHeader = "";
			String id = "" ;
			List<PositionInfo> positionInfosOneDay = new ArrayList<PositionInfo>();
			try {
				for(int i=startIndex;i<endIndex;i++){//1000 car
					idHeader="Hu"+autoFillZero(i);
					calendar.setTime(now);
//					calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
					calendar.add(Calendar.DATE, -1);
					
					for(int j=0;j<totaldays;j++){//250 day
						calendar.add(Calendar.DATE, 1);
						calendar.set(Calendar.AM_PM, Calendar.AM);
						calendar.set(Calendar.HOUR, 9);
						calendar.set(Calendar.MINUTE, 0);
						id = idHeader + sdf1.format(calendar.getTime()) ;
						positionInfosOneDay.clear();
						CarAddress carAddress = new CarAddress (id,sdf1.parse(sdf1.format(calendar.getTime())),null, null, null);
						carAddressDao.add(carAddress);
						for(int k=0;k<totalHours;k++){//8 hour start with 9p.m , end with 5a.m
//							calendar.add(Calendar.HOUR, 1);
							for(int p=0;p<totalMins;p++){//60 time per hour
//									carAddressDao.updateOneBaseInfo(carAddress,oldCarAddress);
									double longitude = random.nextDouble()*baseNumber;
									double latitude = random.nextLong()*baseNumber;
									
									Coordinate max_coordinate = carAddress.getMax_coordinate();
									Coordinate min_coordinate = carAddress.getMin_coordinate();
									if(max_coordinate==null){
										carAddress.setMax_coordinate(new Coordinate(longitude,latitude));
									}else {
										if(latitude>max_coordinate.getLatitude())
											max_coordinate.setLatitude(latitude);
										if(longitude>max_coordinate.getLongitude())
											max_coordinate.setLongitude(longitude);
									}
									if(min_coordinate==null){
										carAddress.setMin_coordinate(new Coordinate(longitude,latitude));
									}else{
										if(latitude<min_coordinate.getLatitude())
											min_coordinate.setLatitude(latitude);
										if(longitude<min_coordinate.getLongitude())
											min_coordinate.setLongitude(longitude);
									}
									
									positionInfosOneDay.add(new PositionInfo(sdf2.parse(sdf2.format(calendar.getTime())), longitude, latitude, 333, 11, 11));
									
//								System.out.println(carAddress.toString());
								calendar.add(Calendar.MINUTE, 1);
							}
						}
						carAddress.setData(positionInfosOneDay);
						carAddressDao.updateOneAllInfo(carAddress);
					}
				}
				System.out.println(timerTaskName+"[spend time=>"+(new Date().getTime()-now.getTime())+",startIndex=>"+startIndex+",endIndex=>"+endIndex+"]");
			} catch (ParseException e) {}
		}

		
	}
}

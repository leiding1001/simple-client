package com.example.cassandra;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		/*Date date = new Date() ;
		System.out.println(date.toString());
		System.out.println(date.getTime());
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(date);
		System.out.println(calendar.getTime().getTime());

		System.out.println(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE));
		System.out.println(calendar.get(Calendar.HOUR)+"-"+calendar.get(Calendar.MINUTE)+"-"+calendar.get(Calendar.SECOND));
		
		calendar.set(Calendar.DATE, 30);
		System.out.println(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE));
		calendar.add(Calendar.DATE, 1);
		System.out.println(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE));
		calendar.add(Calendar.DATE, 1);
		System.out.println(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DATE));*/
		
		/*Random random = new Random() ;
		for(int i=0;i<10;i++){
			System.out.println(random.nextLong());
		}*/
		
		double x1 = -3.5982e+22 ;
		double x2 =  9.1742e+22;
		System.out.println(x1>x2);
	}
}

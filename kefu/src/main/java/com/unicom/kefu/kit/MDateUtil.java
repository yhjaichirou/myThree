package com.unicom.kefu.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MDateUtil {
	public static final String format="yyyy-MM-dd HH:mm:ss";
	public static final String formatDate="yyyy-MM-dd";
	public static final String formatTime="HH:mm:ss";
	/**
	 * 
	 * @param date
	 * @param parv  yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String dateToString(Date date ,String parv) {
		parv = parv==null?format:parv;
		SimpleDateFormat sf = new SimpleDateFormat(parv);
		return sf.format(date);
	}
	
	public static Date stringToDate(String date ,String parv) {
		parv = parv==null?format:parv;
		SimpleDateFormat sf = new SimpleDateFormat(parv);
		try {
			return sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取今天开始的时间戳
	 * @return
	 */
	public static long getTodayStartTime() {
		long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
		return zero;
	}
	/**
	 * 获取今天结束的时间戳
	 * @return
	 */
	public static long getTodayFinishTime() {
		long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
		return twelve;
	}
	
	/**
	 * 日期 单数字 格式转变  
	 * @param date
	 * @return
	 */
	public static String coverSimpleDate(String date) {
		if(StrKit.isBlank(date)) {
			return null;
		}
		String[] s = date.split("-");
		String year = s[0],month = s[1],day = s[2];
		if(month.length()<=1) {
			month = "0"+month;
		}
		if(day.length()<=1) {
			day = "0"+day;
		}
		return year +"-"+ month +"-"+ day;
	}
	
	public static String coverSimpleTime(String date) {
		if(StrKit.isBlank(date)) {
			return null;
		}
		String[] s = date.split(":");
		String hour = s[0],minute = s[1],second = s[2];
		if(hour.length()<=1) {
			hour = "0"+hour;
		}
		if(minute.length()<=1) {
			minute = "0"+minute;
		}
		if(second.length()<=1) {
			second = "0"+second;
		}
		return hour +":"+ minute +":"+ second;
	}
	
	/**
	 * 获取 差值  ： 格式  xxx天  12:22:22
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getDiff(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		//时间差的毫秒数
		long dateDiff = endTime - startTime;
		//计算出相差天数
		long day=dateDiff/(24*60*60*1000);
		long hour=(dateDiff/(60*60*1000)-day*24);
		long min=((dateDiff/(60*1000))-day*24*60-hour*60);
		long second=(dateDiff/1000-day*24*60*60-hour*60*60-min*60);
		return day<=0? "-"+ hour + ":"+min+":"+second:day+"天-"  + hour + ":"+min+":"+second ; 
	}
	
	/**
	 * 获取 差值  ： 格式  xxx天  
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer getDiffDay(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		//时间差的毫秒数
		long dateDiff = endTime - startTime;
		//计算出相差天数
		Long day=dateDiff/(24*60*60*1000);
		return day.intValue();
	}
	
	/**
	 * 获取 差值  ： 格式  xxx 年
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Integer getDiffYear(Date startDate,Date endDate) {
		long startTime = startDate.getTime();
		long endTime = endDate.getTime();
		//时间差的毫秒数
		long dateDiff = endTime - startTime;
		//计算出相差天数
		Long day=dateDiff/(24*60*60*1000*365);
		return day.intValue();
	}
	
}

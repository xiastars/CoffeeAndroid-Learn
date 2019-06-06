package com.summer.demo.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class CommonUtils {
	/* 本软件的存储路径 */
	public static final String SDPATH = getSDPath()+"/summer";
	
	/**
	 * 计算星座
	 * 
	 * @param birth传入生日
	 *            ，格式：1900-00-00
	 * @return
	 */
	public static String getSign(String birth) {
		if (birth.length() != 10) {
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
			birth = format.format(new Date());
		}
		Integer month = Integer.valueOf(birth.substring(5, 7));
		Integer day = Integer.valueOf(birth.substring(8, 10));
		String s = "魔羯水瓶双鱼白羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		Integer[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		Integer num = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(num, num + 2) + "座";
	}

	/**
	 * 计算用户的年龄
	 * 
	 * @param birthDay 传入的生日，格式为1989-07-07
	 * @return
	 * @throws Exception
	 */
	public static int getAge(String birthDay) {
		/* 当传入的格式不正确时，就设置为0岁 */
		if ( birthDay.length() != 10) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();

		int year = Integer.valueOf(birthDay.substring(0, 4));
		int month = Integer.valueOf(birthDay.substring(5, 7));
		int day = Integer.valueOf(birthDay.substring(8, 10));

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        /* 年龄 = 当前的年份 - 设置的年份*/
		int age = yearBirth - year;
        /*
         * 情况一：当月份与当前月份相同，如果设置的天小于今天，则减一岁
         * 情况二：当月份小于当前月份时，直接减一岁
         */
		if (month <= monthBirth) {
			if (month == monthBirth) {
				if (day < dayOfMonthBirth) 
					age--;
			} else {
				age--;
			}
		} 
		return age;
	}
	
	/**
	 * 将时间转换成1900-00-00这种格式
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String properBirth(int year,int monthOfYear,int dayOfMonth){
		String month,day;
	     /*当月数小于10时在前面+0，因为月数会自动+1，所以判断<9就可以了*/	
		if(monthOfYear<9){
			month ="0"+Integer.toString(monthOfYear+1);
		}else{
			month = Integer.toString(monthOfYear+1);
		}	
		
		if(dayOfMonth<10){
			day ="0"+Integer.toString(dayOfMonth);
		}else{
			day =Integer.toString(dayOfMonth);
		}
		return year+"-"+month+"-"+day;
	}
	
	/**
	 * 获取SD路径
	 */
	public static String getSDPath() {
		File sdDir = null;
		try {
			boolean sdCardExist = Environment
					.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment
						.getExternalStorageDirectory();// 获取跟目录
			} else {
				File file = new File(Environment.getDataDirectory().getPath()
						+ "/SUMMER");
				if(!file.exists()){
					file.mkdir();
				}
				if (file.canRead()) {
					return file.toString();
				} else {
					return "";
				}
			}
			if (sdDir != null) {
				return sdDir.toString();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
		return "";
	}

}

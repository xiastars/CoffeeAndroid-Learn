package com.summer.demo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

public class Ac3TextUtils extends Activity {
	private TextView text1;
	private TextView text2,text3,text4,text5,text6,text7,text8,text9,text10;
	private String text1Str;
	private Context context ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_3_textutils);
		context = Ac3TextUtils.this;
		
		
		text1 = (TextView)findViewById(R.id.three_text1);
		text1Str =text1.getText().toString();
		String needle ="one";
		int s =TextUtils.indexOf(text1Str,'o');
		int s1 =TextUtils.indexOf(text1Str,'o',4);
		int s2 =TextUtils.indexOf(text1Str,'o',2,10);
		int s3 =TextUtils.lastIndexOf(text1Str,'o');
		int s4 =TextUtils.lastIndexOf(text1Str,'o',12);
		int s5 =TextUtils.lastIndexOf(text1Str,'o',2,21);
		int s6=TextUtils.indexOf(text1Str,needle);
		text2 =(TextView)findViewById(R.id.three_text2);
		text3 =(TextView)findViewById(R.id.three_text3);
		text4 =(TextView)findViewById(R.id.three_text4);
		text5 =(TextView)findViewById(R.id.three_text5);
		text6 =(TextView)findViewById(R.id.three_text6);
		text7 =(TextView)findViewById(R.id.three_text7);
		text8 =(TextView)findViewById(R.id.three_text8);
		text9 =(TextView)findViewById(R.id.three_text9);
		
		text2.setText("TextUtils.indexOf(第一段英文内容,'o'),得到'o'的位置是"+s);
		text3.setText("TextUtils.indexOf(第一段英文内容,'o', 4)得到'o'的位置是"+s1);
		text4.setText("TextUtils.indexOf(第一段英文内容,'o', 2,10)得到'o'的个数"+s2+"结果为-1就是当中没有'o'");
		text5.setText("TextUtils.lastIndexOf(第一段英文内容,'o'),得到'o'的位置是"+s3);
		text6.setText("TextUtils.lastIndexOf(第一段英文内容,'o',12),得到'o'的位置是"+s4);
		text7.setText("TextUtils.lastIndexOf(第一段英文内容,'o',2,21),得到'o'的位置是"+s5);
		text8.setText("TextUtils.indexOf(第一段英文内容，“one”),得到”one“的位置在"+s6);
		text9.setText("append(CharSequence text)");		
		text9.append("在文本后添加文字");
		double a = 3.3/5;
		int   scale  =   4;//设置位数
		int   roundingMode  =  4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等.
		BigDecimal   bd  =   new  BigDecimal(a);
		bd   =  bd.setScale(scale,roundingMode);
		text9.setText(bd.floatValue()+"---");
		
		
	}
	
	/**
	 * 转化当前时间
	 * @return
	 */
	public int getRealTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
		String time = format.format(new Date());
		return Integer.parseInt(time);
	}
	
	public int getPreTime(int type){
		String y = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
		Calendar c = Calendar.getInstance();  
		try {
			c.setTime(format.parse(Integer.toString(getRealTime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}   
		long mis =  c.getTimeInMillis();  
		long divider = 1000*60*60*24;
		long preTime = 0;
		if(type == 0){
			preTime = mis - divider ;
		}else if(type == 1){
			preTime = mis-divider*3;
		}else if(type == 2){
			preTime = mis-divider*6;
		}else if(type == 3){
			preTime = mis-divider*11;
		}else if(type == 4){
			preTime = mis-divider*18;
		}else if(type == 5){
			preTime = mis-divider*28;
		}else if(type == 6){
			preTime = mis-divider*43;
		}
		y = format.format(preTime);
		return TextUtils.isEmpty(y) ? 0 : Integer.parseInt(y);		
	}
  
}

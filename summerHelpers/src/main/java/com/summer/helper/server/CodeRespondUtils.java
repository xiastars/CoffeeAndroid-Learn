package com.summer.helper.server;

import android.content.Context;
import android.text.TextUtils;

import com.malata.summer.helper.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
/**
 * 根据返回状态码弹出相应提示
 * @author xiastars@vip.qq.com
 */
public class CodeRespondUtils {
	
	public static void codeResponde(Context context,String code){
		dealWithCode(context,code);
	}
	
	public static void codeResponde(Context context,int code){
		dealWithCode(context,String.valueOf(code));
	}
	
	private static void dealWithCode(Context context,String code){
		if(!SUtils.isNetworkAvailable(context) || TextUtils.isEmpty(code)){
			SUtils.makeToast(context, R.string.network_error);
			return;
		}
		if(code.contains("#")){
			code = code.replaceAll("#", "");
		}
		Logs.i("code:::"+code);
		if(code.equals("5")){
//			SUtils.makeToast(context, R.string.tips_login);
		}
	}
}

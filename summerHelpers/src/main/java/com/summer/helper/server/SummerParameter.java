package com.summer.helper.server;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.summer.helper.utils.Logs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Set;

public class SummerParameter {
	private static final String DEFAULT_CHARSET = "UTF-8";
	private LinkedHashMap<String, Object> mParams = new LinkedHashMap<String, Object>();

	public LinkedHashMap<String, Object> getParams() {
		return this.mParams;
	}

	public void setParams(LinkedHashMap<String, Object> params) {
		this.mParams = params;
	}
	
	public void put(String key, String val) {
		this.mParams.put(key, val);
	}

	public void put(String key, int value) {
		this.mParams.put(key, String.valueOf(value));
	}

	public void put(String key, long value) {
		this.mParams.put(key, String.valueOf(value));
	}

	public void put(String key, Bitmap bitmap) {
		this.mParams.put(key, bitmap);
	}
	
	public void put(String key, File file) {
		this.mParams.put(key, file);
	}

	public void put(String key, Object val) {
		this.mParams.put(key, val.toString());
	}

	public Object get(String key) {
		return this.mParams.get(key);
	}

	public void remove(String key) {
		if (this.mParams.containsKey(key)) {
			this.mParams.remove(key);
			this.mParams.remove(this.mParams.get(key));
		}
	}

	public Set<String> keySet() {
		return this.mParams.keySet();
	}

	public boolean containsKey(String key) {
		return this.mParams.containsKey(key);
	}

	public boolean containsValue(String value) {
		return this.mParams.containsValue(value);
	}

	public int size() {
		return this.mParams.size();
	}

	public String encodeUrl(String url) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String key : this.mParams.keySet()) {
			if(!key.equals("requestType")){
				if (first)
					first = false;
				else {
					sb.append("&");
				}
				Object value = this.mParams.get(key);
				if (value instanceof String) {
					String param = (String) value;
					try {
						sb.append(URLEncoder.encode(key, DEFAULT_CHARSET) + "="
								+ URLEncoder.encode(param, DEFAULT_CHARSET));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	
		String requestType = (String) mParams.get("requestType");
		String requestM = null;
//		if(!TextUtils.isEmpty(requestType)){
//			requestM = LogHelper.requestType.get(Integer.parseInt(requestType));
//		}
		if(requestType !=null){
			Logs.i(requestType+": "+url + "?" + sb.toString());
		}else{
			Logs.i("请求数据"+url + "?" + sb.toString());
		}
		return sb.toString();
	}
	
	public void putLog(String action){
		String requestType = (String) mParams.get("requestType");
		if(!TextUtils.isEmpty(requestType)){
			return;
		}
		this.mParams.put("requestType", action);
	}

	public boolean hasBinaryData() {
		Set<String> keys = this.mParams.keySet();
		for (String key : keys) {
			Object value = this.mParams.get(key);
			
			if ((value instanceof ByteArrayOutputStream)
					|| (value instanceof File)
					|| (value instanceof Bitmap)) {
				return true;
			}
		}
		return false;
	}
}

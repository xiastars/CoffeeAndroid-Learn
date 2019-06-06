package com.summer.helper.server;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.summer.helper.downloader.DownloadManager;
import com.summer.helper.downloader.DownloadStatus;
import com.summer.helper.downloader.DownloadTask;
import com.summer.helper.downloader.DownloadTaskListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class EasyHttp {
	
	public static final String METHOD = "POST"; 
	public static final String METHOD_GET = "GET"; 
	
	public static <T> void post(Context context, String url, final Class<T> clazz,
								final SummerParameter parameters, final RequestCallback<T> callBack) {
		request(context,url, clazz, parameters, callBack,METHOD);
	}
	
	public static <T> void get(Context context, String url, final Class<T> clazz,
							   final SummerParameter parameters, final RequestCallback<T> callBack) {
		if(!SUtils.isNetworkAvailable(context)){
			callBack.done(null);
			SUtils.makeToast(context, "网络未连接,请连接后重试!");
			return;
		}
		request(context,url, clazz, parameters, callBack,METHOD_GET);
	}
	
	public static <T> void get(Context context, String url, final Class<T> clazz,
							   final SummerParameter parameters, RequestListener listener) {
		if(!SUtils.isNetworkAvailable(context)){
			listener.onErrorException(new SummerException());
			return;
		}
		request(url, clazz, parameters, listener,METHOD_GET);
	}
	
	public static <T> void get(Context context , String action, String url, final Class<T> clazz,
							   final SummerParameter parameters, final RequestCallback<T> callBack) {
		if(null != parameters)parameters.putLog(action);
		request(context,url, clazz, parameters, callBack,METHOD_GET);
	}
	
	private static  <T> void request(final Context context, String url, final Class<T> clazz,
									 final SummerParameter parameters, final RequestCallback<T> callBack, String methodGet){
		if(methodGet.equals(METHOD_GET)){
			GetBuilder utils = OkHttpUtils.get().url(url);
			Set<String> params = parameters.keySet();
			for(String key : params){
				if(!key.equals("requestType")){
					utils.addParams(key, parameters.get(key)+"");
				}
			}
			parameters.encodeUrl(url);
			utils.build().execute(new StringCallback(){
	            @Override
	            public void onResponse(String response)
	            {
					Logs.i("xia","请求结果:"+response);
					if(clazz == String.class) {
						@SuppressWarnings("unchecked")
						T t = (T)response;
						callBack.done(t);
						return;
					}
					try {
						T t = JSON.parseObject(response, clazz);
						callBack.done(t);
					} catch (Exception e) {
						e.printStackTrace();
					}

	            }

				@Override
				public void onError(Call arg0, Exception arg1) {
					callBack.done(null);
					Logs.i("xia",arg0.toString()+",,"+arg1);
					SUtils.makeToast(context, "请求失败,请稍后重试!");
				}
		     });
		}else{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String postParam = parameters.encodeUrl(url);
			try {
				baos.write(postParam.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			OkHttpClient okHttpClient  = new OkHttpClient();
			RequestBody reqbody = RequestBody.create(MediaType.parse("Content-Type"),baos.toByteArray());  
			
			Request.Builder formBody = new Request.Builder().url(url).method("POST",reqbody).header("Content-Type", "application/x-www-form-urlencoded");
			okHttpClient.newCall(formBody.build()).enqueue(new Callback() {
				
				@Override
				public void onResponse(Call arg0, Response arg1) throws IOException {
					String response = arg1.body().string();
					Logs.i("xia","body:"+response);
					
					if(clazz == String.class) {
						@SuppressWarnings("unchecked")
						T t = (T)response;
						callBack.done(t);
						return;
					}
					Logs.i("xia","请求结果:"+response);
					try {
						T t = JSON.parseObject(response, clazz);
						callBack.done(t);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				
				@Override
				public void onFailure(Call arg0, IOException arg1) {
				}
			});
		}
		
	}
	
	/**
	 * 获取Bitmap
	 * @param url
	 * @param callback
	 */
	public static void getBitmap(String url,BitmapCallback callback){
		OkHttpUtils
        .get()
        .url(url)
        .build()
        .connTimeOut(20000)
        .readTimeOut(20000)
        .writeTimeOut(20000)
        .execute(callback);
	}
	
	private static  <T> void request(String url, final Class<T> clazz,
									 final SummerParameter parameters, final RequestListener listener, String methodGet){
		GetBuilder utils = OkHttpUtils.get().url(url);
		Set<String> params = parameters.keySet();
		for(String key : params){
			utils.addParams(key, parameters.get(key)+"");
		}
		parameters.encodeUrl(url);
		utils.build().execute(new StringCallback(){
            @Override
            public void onResponse(String response)
            {
				listener.onComplete(response);		
            }

			@Override
			public void onError(Call arg0, Exception arg1) {
			}
	     });
	}
	
	public static <T> void post(String url, final Class<T> clazz,
								final SummerParameter parameters, final RequestCallback<T> callBack, String mothod) {
	}
	
	/**
	 * 下载文件,使用URL作为ID
	 * @param url
	 * @param path
	 * @param fileName
	 */
	public static void download(Context context,String url,String path,String fileName,DownloadTaskListener callBack){
		DownloadManager manager = DownloadManager.getInstance(context);
		DownloadTask task = new DownloadTask(url,path,fileName);
		manager.addDownloadTask(task,callBack);
	}
	
	/**
	 * 下载文件,使用给定ID
	 * @param url
	 * @param path
	 * @param fileName
	 */
	public static void download(Context context,String id,String url,String path,String fileName,DownloadTaskListener callBack){
		DownloadManager manager = DownloadManager.getInstance(context);
		DownloadTask task = new DownloadTask(id,url,path,fileName);
		manager.addDownloadTask(task,callBack);
	}
	
	/**
	 * 检查是否在下载
	 * @param context
	 * @param url
	 */
	public static boolean existDownload(Context context,String url){
		DownloadManager manager = DownloadManager.getInstance(context);
		if(manager.getCurrentTaskById(url) != null){
			return true;
		}
		return false;
	}

	/**
	 * 删除下载
	 * @param context
	 * @param url
	 */
	public static void deleteDownload(Context context,String url){
		DownloadManager manager = DownloadManager.getInstance(context);
		if(manager.getCurrentTaskById(url) != null){
			manager.cancel(url);
		}
	}
	
	/**
	 * 暂停下载
	 * @param context
	 * @param url
	 */
	public static void pauseDownload(Context context,String url){
		DownloadManager manager = DownloadManager.getInstance(context);
		if(manager.getCurrentTaskById(url) != null){
			manager.pause(url);
		}
	}
	
	/**
	 * 继续下载
	 * @param context
	 * @param url
	 */
	public static void resumeDownload(Context context,String url){
		DownloadManager manager = DownloadManager.getInstance(context);
		if(manager.getCurrentTaskById(url) != null){
			manager.resume(url);
		}
	}
	
	/**
	 * 检查是否暂停,返回下载进度值
	 */
	public static float checkPaused(Context context,String url){
		DownloadManager manager = DownloadManager.getInstance(context);
		DownloadTask task = manager.getCurrentTaskById(url);
		if(task != null){
			if(task.getPercent() != 0 && task.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_PAUSE){
				return task.getPercent();
			}
		}
		return 0;
	}
	
}

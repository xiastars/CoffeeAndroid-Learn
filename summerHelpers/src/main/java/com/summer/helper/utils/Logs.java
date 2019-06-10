package com.summer.helper.utils;

import android.util.Log;

/**
 * 日志管理类，发布时屏蔽LOG
 * @author malata_summer
 * @Time 2016年5月24日
 */
public class Logs {
	public static boolean isDebug = true;
	
	public static void d(String tag, String msg) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at "+stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.d(tag, fileInfo + ": " + msg);
	}

	public static void i(String tag, String msg) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at "+stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.i(tag, fileInfo + ": " + msg);
	}

	public static void e(String tag, String msg) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at "+ stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.e(tag, fileInfo + ": " + msg);
	}

	public static void w(String tag, String msg) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at "+stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.w(tag, fileInfo + ": " + msg);
	}

	public static void v(String tag, String msg) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo ="at "+ stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.v(tag, fileInfo + ": " + msg);
	}

	public static String getStackTraceMsg() {
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at "+stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		return fileInfo;
	}
	
	public static void i(String msg) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = java.lang.Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at "+stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.i("creator", fileInfo + ": " + msg);
	}

	public static void t(long time) {
		if (!isDebug)
			return;
		StackTraceElement stackTrace = Thread.currentThread()
				.getStackTrace()[3];
		String fileInfo = "at " + stackTrace.getFileName() + "("
				+ stackTrace.getLineNumber() + ") "
				+ stackTrace.getMethodName();
		Log.i("跟踪时间", fileInfo + ": " + (System.currentTimeMillis() - time));
	}
}

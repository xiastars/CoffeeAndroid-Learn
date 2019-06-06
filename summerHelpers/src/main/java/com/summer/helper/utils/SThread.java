package com.summer.helper.utils;

public class SThread {
	
	public static final SThread commons = new SThread();
	//消息线程池
	private ThreadPool threadPool = new ThreadPool();
	
	public static SThread getIntances(){
		return commons;
	}
	
	/**
	 * 返回当前线程池
	 * @return
	 */
	private ThreadPool getThreadPool() {
		return threadPool;
	}
	
	/**
	 * 提交一个线程交给线程池处理 不用返回结果
	 * @param runnable 线程
	 */
	public void submit(Runnable runnable) {
		threadPool.submit(runnable);
	}
}

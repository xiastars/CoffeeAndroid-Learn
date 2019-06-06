package com.summer.helper.server;

public abstract class RequestCallback<T> {

	public abstract void done(T t);
	
}

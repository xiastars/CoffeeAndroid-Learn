package com.summer.demo.module.base.baserx;

import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 用于管理单个presenter的RxBus的事件和Rxjava相关代码的生命周期处理
 * Created by xsf
 * on 2016.08.14:50
 */
public class RxManager {
    public RxBus mRxBus = RxBus.getInstance();
    //管理rxbus订阅
    private Map<String, Observable<?>> mObservables = new HashMap<>();


    /**
     * RxBus注入监听
     *
     * @param eventName
     * @param action1
     */
    public <T> void on(String eventName, Consumer<T> action1) {
        Observable<T> mObservable = mRxBus.register(eventName);
        mObservables.put(eventName, mObservable);
        /*订阅管理*/

    }

    /**
     * 单纯的Observables 和 Subscribers管理
     *
     * @param m
     */
    public void add(Subscription m) {
        /*订阅管理*/

    }

    /**
     * 单个presenter生命周期结束，取消订阅和所有rxbus观察
     */
    public void clear() {

    }

    //发送rxbus
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}

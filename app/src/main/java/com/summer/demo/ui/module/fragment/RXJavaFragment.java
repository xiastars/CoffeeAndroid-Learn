package com.summer.demo.ui.module.fragment;


/**
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */


import android.view.View;

import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.Logs;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * RXJava练习,来自：https://github.com/liuyangbajin/android_framework/blob/master/rxjava2/src/main/java/com/bj/rxjava2/MainActivity.java#L20
 */
public class RXJavaFragment extends BaseFragment {

    @Override
    protected void initView(View view) {

        //threadOnOrder();

        //interval();
        map();
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return 0;
    }

    private void map() {


        Observable.just("你好")
                .map(new Function<String, Boolean>() {

                    @Override
                    public Boolean apply(String s) throws Exception {

                        return getDatas(s);
                    }
                })
                .subscribe(new Consumer<Boolean>() {

                    @Override
                    public void accept(Boolean s) throws Exception {

                        Logs.i("s" + s);
                    }
                });
    }

    private boolean getDatas(String data) {

        for (int i = 0; i < 100; i++) {
            Logs.i("count:" + i + data);
        }
        return true;
    }

    /**
     * 线程按顺序执行,跟threadOnOrder一样，不过是更简单的写法
     */
    private void threadOnOrderJust() {
        Observable.just(runnable1, runnable2, runnable3).subscribe(new Observer<Runnable>() {
            @Override
            public void onSubscribe(Disposable d) {
                Logs.i("--");
            }

            @Override
            public void onNext(Runnable runnable) {
                runnable.run();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Logs.i("结束调用");
            }
        });
    }


    /**
     * 线程按顺序执行
     */
    private void threadOnOrder() {
        // 创建被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<Runnable>() {

            @Override
            public void subscribe(ObservableEmitter<Runnable> emitter) throws Exception {

                emitter.onNext(runnable1);
                emitter.onNext(runnable2);
                emitter.onNext(runnable3);
                emitter.onComplete();
            }
        });
        //lambda写法
        Observable.create((ObservableOnSubscribe<Runnable>) emitter -> {

        });

        // 创建观察者
        Observer observer = new Observer<Runnable>() {

            @Override
            public void onSubscribe(Disposable d) {

                Logs.i("准备监听");
            }

            @Override
            public void onNext(Runnable s) {
                s.run();
            }

            @Override
            public void onError(Throwable e) {

                Logs.i("error");
            }

            @Override
            public void onComplete() {

                Logs.i("监听完毕");
            }
        };

        // 订阅
        observable.subscribe(observer);
    }

    /**
     * 倒计时结束后运行
     */
    private void delayFunc() {
        long time = System.currentTimeMillis();
        Observable.timer(5, TimeUnit.SECONDS).subscribe(aLong -> {
            Logs.t(time);
            runnable1.run();
        });
    }

    /**
     * interval: 每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字。
     * 类似于项目中的timer，做计时器
     */
    private void interval() {

        Observable.interval(3, TimeUnit.SECONDS).subscribe(aLong -> {
            Logs.i("time:" + aLong);
            runnable1.run();
        });
    }

    Runnable runnable1 = () -> Logs.i("这是第一个线程");

    Runnable runnable2 = () -> Logs.i("这是第二个线程");

    Runnable runnable3 = () -> Logs.i("这是第三个线程");
}

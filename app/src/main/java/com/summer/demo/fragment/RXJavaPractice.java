package com.summer.demo.fragment;


import android.graphics.Bitmap;

import com.summer.demo.R;
import com.summer.demo.view.DragLayer;
import com.summer.demo.view.DragView;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import rx.Subscriber;
import rx.functions.Func1;
/**     #                                                   #
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
         * #                                                   #*/

/**
 * View的拖动示例
 */
public class RXJavaPractice extends BaseFragment {
    DragLayer dragLayer;

    @Override
    protected void initView(){
        dragLayer = new DragLayer(context);
        llParent.addView(dragLayer);
        dragLayer.addBackgroundView();
        dragLayer.setBackgroundResource(R.drawable.background1);

        rx.Observable<String> observable = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("....");
                subscriber.onCompleted();
            }
        });

        Subscriber<String> mySubscribe = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.print(s);
            }
        };
        observable.subscribe(mySubscribe);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dragLayer.removeAllViews();
    }

}

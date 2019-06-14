package com.summer.demo.ui.fragment;


import com.summer.demo.R;
import com.summer.demo.view.DragLayer;

import rx.Subscriber;
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
public class RXJavaPractice extends BaseSimpleFragment {
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


package com.summer.demo.base.swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nufang.fsxq.ui.main.activity.MainActivity;

import cn.jzvd.Jzvd;

public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;
    boolean isActivityStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSwipe();
    }

    private void createSwipe(){
        if(this instanceof MainActivity == false && mHelper == null){
            mHelper = new SwipeBackActivityHelper(this);
            mHelper.onActivityCreate();
            mHelper.getSwipeBackLayout().setEnableGesture(true);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mHelper != null){
            mHelper.onPostCreate();
        }

    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createSwipe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityStop = true;
        mHelper = null;
        Jzvd.releaseAllVideos();
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}

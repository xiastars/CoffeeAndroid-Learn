package com.summer.demo.ui;

import android.graphics.PixelFormat;

import com.summer.demo.R;
import com.summer.demo.ui.fragment.sdk.AgoraFragment;
import com.summer.demo.ui.fragment.sdk.EasyARFragment;
import com.summer.helper.utils.Logs;

/**
 * @Description: 有关sdk对接
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/20 16:07
 */
public class SDKActivity extends BaseGridListActivity {


    @Override
    protected void initData() {
        super.initData();
        setTitle("SDK");
        Logs.i("-------------------");
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        int[] imgs = {R.drawable.alexa,R.drawable.agora,R.drawable.agora};
        setData(context.getResources().getStringArray(R.array.sdks), imgs);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("SDK");
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos){
            case 0:
                //showFragment(new AlexaFragment());
                break;
            case 1:
                setTitle("Agora");
                //JumpTo.getInstance().commonJump(context, AuraActivity.class);
                showFragment(new AgoraFragment());
                break;
            case 2:
                setTitle("EasyAR");
                showFragment(new EasyARFragment());
                break;
        }
    }

}

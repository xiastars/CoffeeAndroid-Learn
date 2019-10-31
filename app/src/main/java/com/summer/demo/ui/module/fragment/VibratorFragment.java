package com.summer.demo.ui.module.fragment;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;

import butterknife.OnClick;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/31 18:28
 */
public class VibratorFragment extends BaseFragment implements View.OnClickListener {
    Vibrator vibrator;

    long[] mVibratePattern1 = new long[]{0,90,10,90,10,90,10,90,10,90,10,90,10,90,10,90};
    long[] mVibratePattern2 = new long[]{0,80,20,80,20,80,20,80,20,80,20,80,20,80,20,80};
    long[] mVibratePattern3 = new long[]{0,70,30,70,30,70,30,70,30,70,30,70,30,70,30,70};
    long[] mVibratePattern4 = new long[]{0,60,40,60,40,60,40,60,40,60,40,60,40,60,40,60};
    long[] mVibratePattern5 = new long[]{0,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};
    long[] mVibratePattern6 = new long[]{0,40,60,40,60,40,60,40,60,40,60,40,60,40,60,40};
    long[] mVibratePattern7 = new long[]{0,30,70,30,70,30,70,30,70,30,70,30,70,30,70,30};
    long[] mVibratePattern8 = new long[]{0,20,80,20,80,20,80,20,80,20,80,20,80,20,80,20};
    long[] mVibratePattern9 = new long[]{0,10,90,10,90,10,90,10,90,10,90,10,90,10,90,10};

    long[] mVibratePattern10 = new long[]{0,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000,3000,2000};
    int[] mAmplitudes2 = new int[]{0, 255, 0, 255, 0, 255, 0, 255,0, 255, 0, 255, 0, 255, 0, 255,0, 255, 0, 255, 0, 255, 0, 255,0};

    int[] mAmplitudes  = new int[]{0, 255, 0, 255, 0, 255, 0, 255,0, 255, 0, 255, 0, 255, 0, 255};

    @Override
    protected void initView(View view) {
        vibrator = (Vibrator) AppContext.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_vibrate;
    }

    @OnClick({R.id.btn_virate, R.id.btn_cancel})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_virate:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(createWaveShot(true, mVibratePattern9));
                } else {
                    vibrator.vibrate(mVibratePattern1, 0);
                }
                break;
            case R.id.btn_cancel:
                vibrator.cancel();
                break;
        }
    }

    /**
     * 创建波形振动
     * @param repeat 是否重复，
     * @param timings
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect createWaveShot(boolean repeat, long[] timings) {
        return VibrationEffect.createWaveform(timings, repeat ? 0 : -1);
    }

    /**
     * 创建波形振动
     * @param repeat 是否重复，
     * @param timings
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect createWaveShot(boolean repeat, long[] timings,int[] amplitude) {
        return VibrationEffect.createWaveform(timings, amplitude,repeat ? 0 : -1);
    }

    /**
     * 创建一次振动
     *
     * @param secondTime 振动时间
     * @param amplitude  振动幅度1-255
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect createOneShot(int secondTime, int amplitude) {
        return VibrationEffect.createOneShot(secondTime, amplitude);
    }

    @Override
    public void onStop() {
        super.onStop();
        vibrator.cancel();
    }
}

package com.summer.demo.ui.module.fragment;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.view.View;

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

    @Override
    protected void initView(View view) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
                    vibrator.vibrate(createWaveShot(true, new long[]{100L, 2000L, 1000L, 1000L, 3000L}));
                } else {
                    vibrator.vibrate(new long[]{100L, 2000L, 1000L, 1000L, 3000L}, 0);
                }
                break;
            case R.id.btn_cancel:
                vibrator.cancel();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private VibrationEffect createWaveShot(boolean repeat, long[] timings) {
        return VibrationEffect.createWaveform(timings, repeat ? 0 : -1);
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
}

package com.summer.demo.ui.view.customfragment;

import android.view.View;
import android.widget.ProgressBar;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.view.NumberProgressBar;
import com.summer.demo.view.ProgressImageView;
import com.summer.demo.view.VerticalProgressBar;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

/**
 * @Description: 进度条演示
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/11 17:27
 */
public class ProgressBarFragment extends BaseFragment {

    @BindView(R.id.load_pb)
    ProgressBar loadPb;
    @BindView(R.id.pb_right)
    VerticalProgressBar pbRight;
    @BindView(R.id.pb_number)
    NumberProgressBar pbNumber;
    @BindView(R.id.pb_image)
    ProgressImageView pbImage;
    boolean startDownload = true;

    @Override
    protected void initView(View view) {

        SUtils.setPic(pbImage, "http://pic1.win4000.com/wallpaper/b/57fc5075c59ea.jpg");

        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {

                int index = 0;
                while (startDownload) {
                    index++;
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    final int finalIndex = index;
                    int finalIndex1 = index;
                    myHandlder.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!startDownload){
                                return;
                            }
                            loadPb.setProgress(finalIndex);
                            pbRight.setProgress(finalIndex);
                            pbNumber.setProgress(finalIndex);
                            pbImage.setProgress(finalIndex);

                        }
                    });

                    if (index == 101) {
                        startDownload = false;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
        Logs.i("-----------");
        startDownload = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Logs.i("-----------");
        startDownload = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logs.i("---------");
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_progress;
    }

}

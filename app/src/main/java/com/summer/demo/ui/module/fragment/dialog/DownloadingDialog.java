package com.summer.demo.ui.module.fragment.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseCenterDialog;

import butterknife.BindView;

/**
 * @Description: 视频下载
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/7/11 14:22
 */
public class DownloadingDialog extends BaseCenterDialog {

    @BindView(R.id.load_pb)
    ProgressBar loadPb;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    int index;
    int totalCount = 100;

    public DownloadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_downloading;
    }

    @Override
    public void initView(View view) {
        tvTitle.setText(context.getResources().getString(R.string.downloading)+"("+index+"/"+totalCount+")...");
    }

    public void setTotalCount(int totalCount){
        this.totalCount = totalCount;
        if(tvTitle != null){
            tvTitle.setText(context.getResources().getString(R.string.downloading)+"("+index+"/"+totalCount+")...");
        }
    }

    public void setPercent(int pos){
        tvTitle.setText(context.getResources().getString(R.string.downloading)+"("+pos+"/"+totalCount+")...");
        loadPb.setProgress((int) (100/ (float)totalCount * pos));
    }
}

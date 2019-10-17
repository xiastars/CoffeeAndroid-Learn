package com.summer.demo.ui.course.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.video.framepicker.MediaMetadataRetrieverCompat;
import com.summer.demo.view.CommonSureView5;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 视频获取封面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/14 9:31
 */
public class VideoGetCoverFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.btn_sure)
    CommonSureView5 btnSure;

    @Override
    protected void initView(View view) {
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnSure.changeStyle(s.length() > 0);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_video_frame;
    }

    @OnClick({R.id.btn_sure})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sure:
                String fileName = edtContent.getText().toString();
                String rootPath = SUtils.getSDPath()+fileName;
                File file = new File(rootPath);
                String[] files = file.list();
                break;
        }
    }

    private void getCover(String path,String name){
        MediaMetadataRetrieverCompat mmrc = MediaMetadataRetrieverCompat.create();
        mmrc.setDataSource(path);
        Bitmap bitmap = mmrc.getFrameAtTime(1000, MediaMetadataRetrieverCompat.OPTION_CLOSEST);
        SUtils.getandSaveCurrentImage((Activity) context,bitmap, SFileUtils.getImageViewDirectory()+"/"+name+".png");

    }
}

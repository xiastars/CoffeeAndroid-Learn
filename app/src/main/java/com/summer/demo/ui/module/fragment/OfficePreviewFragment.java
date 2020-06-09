package com.summer.demo.ui.module.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.bean.SelectAlumbType;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.album.listener.AlbumCallback;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.module.fragment.office.ReadView;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import java.util.List;

import butterknife.BindView;

/**
 * @Description: 浏览word, ppt, excel
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/9 13:40
 */
public class OfficePreviewFragment extends BaseFragment implements TbsReaderView.ReaderCallback {

    ReadView readView;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private String mFileName;

    @Override
    protected void initView(View view) {
        readView = new ReadView(context, this);
        rlRoot.addView(readView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //rlRoot.addView(new DragLayer(context), new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
       // String nameContent = "文档名称:" + SFileUtils.getFileName(mFileName);

        //tvName.setText(STextUtils.getSpannableString(nameContent, 5, nameContent.length(), getResColor(R.color.blue_56)));
        openFile();
    }

    private void openFile() {
        if (!PermissionUtils.checkReadPermission(context)) {
            return;
        }
        SelectOptions.Builder builder = new SelectOptions.Builder();
        builder.setSlectAlbumType(SelectAlumbType.File);
        builder.setCallback(new AlbumCallback() {
            @Override
            public void doSelected(List<ImageItem> images) {
               openFile(images.get(0).getImagePath());
            }
        });
        AlbumActivity.show(context, builder.build());
    }

    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    private void openFile(String path) {
        Logs.i("path;;;;;;;;;;;;", path);
        Bundle bundle = new Bundle();
        bundle.putString("filePath", path);
        bundle.putString("tempPath", path);
        String mFileName = SFileUtils.getFileName(path);
        Logs.i("fileName:"+mFileName);
        boolean result = readView.preOpen(mFileName, false);
        if (result) {
            readView.openFile(bundle);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_office;
    }


    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {
        Log.i("-------------", "intent:" + integer + ",," + o + ",,," + o1);
    }


}

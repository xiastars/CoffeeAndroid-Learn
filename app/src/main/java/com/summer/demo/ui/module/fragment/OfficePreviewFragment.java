package com.summer.demo.ui.module.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.bean.SelectAlumbType;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.BaseFragmentActivity;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.utils.SFileUtils;
import com.tencent.smtt.sdk.TbsReaderView;

import butterknife.BindView;

/**
 * @Description: 浏览word, ppt, excel
 * @Caution 此版本abs库不支持arm64, NDK里不要带arm64
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/9 13:40
 */
public class OfficePreviewFragment extends BaseFragment implements TbsReaderView.ReaderCallback {

    TbsReaderView readView;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    @Override
    protected void initView(View view) {
        readView = new TbsReaderView(context, this);
        rlRoot.addView(readView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        openFile();
    }

    private void openFile() {
        if (!PermissionUtils.checkReadPermission(context)) {
            return;
        }
        SelectOptions.Builder builder = new SelectOptions.Builder();
        builder.setSlectAlbumType(SelectAlumbType.File);
        builder.setCallback(images -> openFile(images.get(0).getImagePath()));
        AlbumActivity.show(context, builder.build());
    }

    /**
     * 打开文件
     * @param path
     */
    private void openFile(String path) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", path);
        //存储目录
        bundle.putString("tempPath", SFileUtils.getFileDirectory());
        ((BaseFragmentActivity) activity).setTitle(SFileUtils.getFileName(path));
        boolean result = readView.preOpen(parseFormat(path), false);
        if (result) {
            readView.openFile(bundle);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_office;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(readView != null){
            readView.onStop();
        }
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
}

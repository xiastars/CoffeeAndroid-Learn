package com.summer.demo.ui.module.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ghnor.flora.callback.Callback;
import com.summer.demo.R;
import com.summer.demo.helper.ImgHelper;
import com.summer.demo.module.album.util.SelectPhotoHelper;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.view.LoadingDialog;
import com.summer.helper.listener.OnResponseListener;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 上传图片
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/12/26 11:03
 */
public class UploadFileFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.btn_select_photo)
    Button btnSelectPhoto;
    @BindView(R.id.btn_upload)
    Button btnUpload;

    SelectPhotoHelper selectPhotoHelper;

    String filePath;

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_upload;
    }

    @OnClick({R.id.btn_select_photo, R.id.btn_upload})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_photo:

                selectPhotoHelper = new SelectPhotoHelper(context, new OnResponseListener() {
                    @Override
                    public void succeed(String url) {
                        filePath = url;
                        SUtils.setPic(ivCover, url);

                    }

                    @Override
                    public void failure() {

                    }
                });
                selectPhotoHelper.showSelectPhotoDialog();
                break;
            case R.id.btn_upload:
                ImgHelper.compressImg(context, false, filePath, new Callback<String>() {
                    @Override
                    public void callback(String s) {
                        uploadFile(s);
                    }
                });
                break;
        }
    }

    /**
     * 上传文件
     *
     * @param filePath
     */
    private void uploadFile(String filePath) {
        LoadingDialog.showDialogForLoading((Activity) context, "正在上传中", true);
        String userId = "KW7+jmw1MlwuATu5J8+JA4sGn8rT0lsvaOXIxQr7qrl6ZiXuM5cuNH3HawcyM43piDi/Hg68y9uARbQ4TdlmeS0wsLevz5pCCLWK5QzBQTaf+FGlUcYGCMjygpKx47axVALnq9AKd/O2n68adT2yYnu0sZwz34qMvkAjdG5PJFAZjnLGqs3R0jFBiH+3+yAsnFouseRDkbOceM9gIAMSpDwf/VlNn9EaYxTz7S6OhuJD/KK1FhLN8gpKuJmlFt+yN/sKWUF/dSYNP9UUXXfIf+w7/sPdXoGZnhK5Y2LeCZGEaJVomYaz9c9OSVCH00i65WbOto+prC9Fpb3x8jBr7w==";
        SummerParameter parameter = new SummerParameter();
        parameter.put("userId", userId);
        String url = "https://www.baifo.me/upload/api/UploadPhone";
        EasyHttp.uploadFile(url, SFileUtils.FileType.FILE_PNG, filePath, parameter, new OnResponseListener() {
            @Override
            public void succeed(String url) {

            }

            @Override
            public void failure() {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectPhotoHelper.handleRequestCode(requestCode,data);
    }
}

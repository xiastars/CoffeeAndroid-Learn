package com.summer.demo.ui.module.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.summer.demo.R;
import com.summer.demo.module.album.MediaHandleJavascriptInterface;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.web.WebContainerActivity;
import com.summer.demo.ui.module.fragment.web.ReWebChomeClient;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.web.CustomWebView;

import java.io.File;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: Webview访问测试
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/10 11:29
 */
public class WebLeanFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.webview)
    CustomWebView customWebView;
    //定义变量
    private ValueCallback<Uri> uploadMessage;
    private ValueCallback<Uri[]> uploadMessageAboveL;
    //用来判断是否需要给WebView返回null
    private int web_image = 0;
    private int IDENTITY_IMAGE_REQUEST_CODE_Album = 1;//相册
    private int IDENTITY_IMAGE_REQUEST_CODE_Photograph = 2;// 拍照
    private int FILE_CHOOSER_RESULT_CODE = 3;//图片选择

    @Override
    protected void initView(View view) {
        String data = "<p>31231111111</p>\\n<table style=\\\"border-collapse: collapse; width: 100%;\\\" border=\\\"1\\\">\\n<tbody>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">" +
                "&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"" +
                "width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\"" +
                ">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>" +
                "\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">" +
                "<img src='static/js/plugins/tinymce4.7.5/plugins/emoticons/img/smiley-innocent.gif' alt=\\\"innocent\\\" /></td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n</tbody>\\n</table>";
        setWebViewContent(customWebView, data);
    }

    /**
     * 设置统一富文本显示
     *
     * @param wbContainer
     * @param content
     */
    public void setWebViewContent(WebView wbContainer, String content) {
        wbContainer.addJavascriptInterface(new MediaHandleJavascriptInterface(context), "imagelistner");
        wbContainer.setWebChromeClient(new ReWebChomeClient(new ReWebChomeClient.OpenFileChooserCallBack() {
            @Override
            public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
                uploadMessage = uploadMsg;
                openImageChooserActivity();
            }

            @Override
            public void showFileChooserCallBack(ValueCallback<Uri[]> filePathCallback) {
                uploadMessageAboveL = filePathCallback;
                openImageChooserActivity();
            }
        }));
        String finalContent = "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">" +
                "<style>img{max-width:100%;height:auto;};video{width:100%;};</style> " + content.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") + "</html>";
        //wbContainer.loadDataWithBaseURL("http://120.79.56.152:8010/", finalContent, "text/html", "utf-8", "");
        wbContainer.loadUrl("https://www.in9ni.com/wap/detail/merRegister");
    }

    private void openImageChooserActivity() {
        web_image = 0;//判断是否已经选择了
        //自定义选择图片提示框
        String[] datas = {"你好","HI"};
        AlertDialog dialog = new AlertDialog.Builder(context).setItems(datas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //如果点击了dialog的选项，修改变量，不要在setOnDismissListener()方法中
                web_image = 1;
                selected(which);
            }
        }).create();
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (web_image == 0) {
                    getImageWebView(null);
                }
            }
        });
    }

    private void getImageWebView(String str_image) {//将图片路径返回给webview
        if (!TextUtils.isEmpty(str_image)) {
            Uri uri = getImageContentUri(context, new File(str_image));
            if (uploadMessageAboveL != null) {
                Uri[] uris = new Uri[]{uri};
                uploadMessageAboveL.onReceiveValue(uris);
                uploadMessageAboveL = null;
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(uri);
                uploadMessage = null;
            }
        } else {
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public void selected(int position) {
        switch (position) {
            case 0://相册
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, FILE_CHOOSER_RESULT_CODE);

                break;
            case 1:// 拍照
                 i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
                break;
            case 2://选择图片
                i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
                break;
        }
    }

    @Override
    public void loadData() {

    }


    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_learn_web;
    }

    @OnClick({R.id.tv_local, R.id.tv_url})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_url:
                String testUrl = "https://www.in9ni.com/wap/detail/merRegister";
                WebContainerActivity.show(context,testUrl,"网络链接");
                break;
            case R.id.tv_local:

                String url = "file:///android_asset/home.html";
                WebContainerActivity.show(context,url,"asset文件");
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logs.i("--------------");
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {//选择图片
            if (null == uploadMessage && null == uploadMessageAboveL) {
                return;
            }
            Uri result = data == null || resultCode != -1 ? null : data.getData();
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (uploadMessageAboveL == null) {
            return;
        }
        Uri selectedImage = intent.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        Logs.i(picturePath+"...");
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if(bundle != null){
                    Set<String> s = bundle.keySet();
                    for(String ss : s){
                        Logs.i(ss+",,,"+bundle.get(ss));
                    }
                }

                String dataString = intent.getDataString();
                Logs.i("dataStri:L"+intent.getStringExtra("images"));
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        for(Uri uri : results){

            Logs.i("result:"+  uri.getPath());
        }

        uploadMessageAboveL.onReceiveValue(results);
    }



    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}

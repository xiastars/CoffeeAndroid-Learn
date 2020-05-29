package com.summer.demo.module.album.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.summer.demo.dialog.SelectPhotoDialog;
import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.album.listener.AlbumCallback;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片，视频选择辅助
 * Created by xiastars on 2017/10/23.
 */

public class SelectPhotoHelper {

    String fileName;
    File imgFile;
    String cropedPath;//裁剪后的
    public static final int FROME_CAMERA = 1001;
    int FROME_ALBUM = 1002;
    final int CROP_IMG = 1003;

    int aspectY = 1;
    int aspectX = 1;

    Context context;
    ImageView imageView;
    ImageView targetView;
    Activity activity;
    OnReturnObjectClickListener listener;


    boolean croped;

    public SelectPhotoHelper(Context context, OnReturnObjectClickListener listener) {
        this.context = context;
        this.listener = listener;
        activity = (Activity) context;

    }

    /**
     * 点击View，选择照片,并加载选择的照片
     * @param imageView
     */
    public void setTargetView(ImageView imageView) {
        setTargetView(imageView,imageView,"");
    }

    /**
     * 点击View，选择照片
     * @param imageView 点击的View
     * @param targetView 加载图片的View
     * @param mark 埋点
     */
    public void setTargetView(ImageView imageView, final ImageView targetView, final String mark) {
        this.imageView = imageView;
        this.targetView = targetView;
        context = imageView.getContext();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.onClick(context,mark);
                if(!PermissionUtils.checkReadPermission(context)){
                    return;
                }
                enterToAlbumForOne();
            }
        });
    }

    /**
     * 进入相册，每次选择一张图片
     */
    public void enterToAlbumForOne(){
        enterToAlbumForMore(1);
    }

    /**
     * 进入相册，选择多张图片
     * @param count
     */
    public void enterToAlbumForMore(int count){
        if(!PermissionUtils.checkReadPermission(context)){
            return;
        }
        SelectOptions.Builder builder = new SelectOptions.Builder();
        builder.setSelectCount(count);
        builder.setCallback(new AlbumCallback() {
            @Override
            public void doSelected(List<ImageItem> images) {
                if(!SUtils.isEmptyArrays(images)){
                    String imgPaath = images.get(0).getImagePath();
                    if (croped) {
                        imgFile = new File(imgPaath);
                        cropImage();
                        return;
                    }

                    if (listener != null) {
                        listener.onClick(images);
                    }
                }
            }
        });
        AlbumActivity.show(context, builder.build());
    }

    /**
     * 选择图片
     */
    public void showSelectPhotoDialog() {
        if (!PermissionUtils.checkCameraPermission(context)) {
            return;
        }
        SelectPhotoDialog selectPhotoVideoDialog = new SelectPhotoDialog(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        setCroped(false);
                        enterToCamera();
                        break;
                    case 1:
                        enterToAlbumForOne();
                        break;
                }
            }
        });
        selectPhotoVideoDialog.show();
    }

    /**
     * 打开摄像头
     */
    public void enterToCamera(){
        fileName = "ec_" + System.currentTimeMillis() + ".png";
        if (!PermissionUtils.checkCameraPermission(context)) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照
        imgFile = new File(SFileUtils.getAvatarDirectory() + fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        activity.startActivityForResult(intent, FROME_CAMERA);
    }

    public void handleRequestCode(int requestCode, Intent data) {
        Logs.i("requestCode:"+requestCode+",,"+data);
        switch (requestCode) {
            case FROME_CAMERA:
                String path = imgFile.getPath();
                Logs.i(".." + path);

                if (croped) {
                    cropImage();
                } else {
                    if (listener != null) {
                        listener.onClick(buildListWithString(cropedPath));
                    }
                }
                break;
            case CROP_IMG:
                if (PostData.MODEL.equals("Meizu")) {
                    String imageData = data.getParcelableExtra("data");
                    if (imageData == null) {
                        cropedPath = data.getStringExtra("filePath");
                        if (!TextUtils.isEmpty(cropedPath)) {
                            listener.onClick(buildListWithString(cropedPath));
                           // SUtils.setPicWithHolder(targetView, cropedPath, R.drawable.ic_defaul_img);
                        }
                    }
                } else {
                    //SUtils.setPicWithHolder(targetView, cropedPath, R.drawable.ic_defaul_img);
                }
                Logs.i("fielP_ath"+cropedPath);
                if (listener != null) {
                    listener.onClick(buildListWithString(cropedPath));
                }
                break;
            case AlbumActivity.REQUEST_CODE:
                ArrayList<ImageItem> arrayList = (ArrayList<ImageItem>) data.getSerializableExtra(JumpTo.TYPE_OBJECT);
                if (arrayList != null && arrayList.size() > 0) {
                    ImageItem imageItem = arrayList.get(0);
                    if (croped) {
                        imgFile = new File(imageItem.getImagePath());
                        cropImage();
                        return;
                    }
                    if (listener != null) {
                        listener.onClick(arrayList);
                    }
                }
                break;
        }


    }

    BitmapFactory.Options options;

    private void cropImage() {
        if (options == null) {
            options = new BitmapFactory.Options();
        }
        Uri uri = getImageContentUri(activity, imgFile);
        if (uri != null) {
            cropedPath = SFileUtils.getAvatarDirectory() + "nf_" + System.currentTimeMillis() + "_croped.png";
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            intent.putExtra("noFaceDetection", true);
            intent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(cropedPath)));
            ((Activity) context).startActivityForResult(Intent.createChooser(intent, "裁剪图片"), CROP_IMG);
        }
    }

    /**
     * 将URI转为图片的路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 包成file文件转为URI
     */

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * 构造返回内容
     * @param path
     * @return
     */
    private List<ImageItem> buildListWithString(String path){
        List<ImageItem> imageItems = new ArrayList<>();
        ImageItem imageItem = new ImageItem();
        imageItem.setImagePath(path);
        imageItems.add(imageItem);
        return imageItems;
    }


    /**
     * 设置图片裁剪比例
     * @param x
     * @param y
     */
    public void setAspectY(int x, int y) {
        aspectX = x;
        aspectY = y;
    }

    public String filePath(){
        return imgFile.getPath();
    }

    public void setCroped(boolean croped) {
        this.croped = croped;
    }

}

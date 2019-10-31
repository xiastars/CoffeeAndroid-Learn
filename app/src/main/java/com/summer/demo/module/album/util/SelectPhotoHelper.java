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

import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.album.listener.AlbumCallback;
import com.summer.helper.listener.OnResponseListener;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;

import java.io.File;
import java.util.ArrayList;

/**
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
    OnResponseListener listener;


    boolean croped;

    public SelectPhotoHelper(Context context, OnResponseListener listener) {
        this.context = context;
        this.listener = listener;
        activity = (Activity) context;

    }

    public void setAspectY(int x, int y) {
        aspectX = x;
        aspectY = y;
    }

    public void setTargetView(ImageView imageView, final String mark) {
        this.imageView = imageView;
        targetView = imageView;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.onClick(mark);
               if(!PermissionUtils.checkReadPermission(context)){
                    return;
                }
                showSelectPhotoDialog();
            }
        });
    }

    public void setTargetView(ImageView imageView, final ImageView targetView, final String mark) {
        this.imageView = imageView;
        this.targetView = targetView;
        context = imageView.getContext();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.onClick(mark);
                //enterToAlbum();
                if(!PermissionUtils.checkReadPermission(context)){
                    return;
                }
                SelectOptions.Builder builder = new SelectOptions.Builder();
                builder.setCallback(new AlbumCallback() {
                    @Override
                    public void doSelected(String[] images) {
                        if (images != null && images.length>0) {
                            String imgPaath = images[0];
                            if (croped) {
                                imgFile = new File(imgPaath);
                                cropImage();
                                return;
                            }

                            if (listener != null) {
                                listener.succeed(imgPaath);
                            }
                        }
                    }

                });
                AlbumActivity.show(context, builder.build());
            }
        });
    }

    public void startSelectPhoto(){
        SelectOptions.Builder builder = new SelectOptions.Builder();
        builder.setCallback(new AlbumCallback() {
            @Override
            public void doSelected(String[] images) {
                if (images != null && images.length>0) {
                    String imgPaath = images[0];
                    if (croped) {
                        imgFile = new File(imgPaath);
                        cropImage();
                        return;
                    }

                    if (listener != null) {
                        listener.succeed(imgPaath);
                    }
                }
            }

        });
        AlbumActivity.show(context, builder.build());
    }

    public void startTakePhoto(){
        fileName = "tmy_" + System.currentTimeMillis() + ".png";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照
        imgFile = new File(SFileUtils.getAvatarDirectory() + fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
        activity.startActivityForResult(intent, FROME_CAMERA);
    }

    public void setTargetViewDisableTouch(ImageView imageView, final String mark) {
        this.imageView = imageView;
        targetView = imageView;
    }

    /**
     * 选择图片
     */
    public void showSelectPhotoDialog() {
/*
        final BottomListDialog selectTypeDialog = new BottomListDialog(context);
        String[] titles = {"拍照", "从手机相册选择"};
        selectTypeDialog.setDatas(titles);
        selectTypeDialog.setStringType();
        selectTypeDialog.showTopContent(View.GONE);
        selectTypeDialog.showBottomContent(View.GONE);
        selectTypeDialog.show();
        selectTypeDialog.setListener(new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                fileName = "nf_" + System.currentTimeMillis() + ".png";
                if (position == 0) {
                    if (!PermissionUtils.checkCameraPermission(context)) {
                        return;
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//拍照
                    imgFile = new File(SFileUtils.getAvatarDirectory() + fileName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgFile));
                    activity.startActivityForResult(intent, FROME_CAMERA);
                } else if (position == 1) {
                    enterToAlbum();
                }
                selectTypeDialog.cancelDialog();
            }
        });*/
    }

    /**
     * 进行摄像头
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

    public void enterToAlbum() {
        if (!PermissionUtils.checkReadPermission(context)) {
            return;
        }
        SelectOptions.Builder builder = new SelectOptions.Builder();
        builder.setCallback(new AlbumCallback() {
            @Override
            public void doSelected(String[] images) {
                if (images != null && images.length>0) {
                    String imgPaath = images[0];
                    Logs.i("file::"+imgPaath);
                    if (croped) {
                        imgFile = new File(imgPaath);
                        cropImage();
                        return;
                    }

                    if (listener != null) {
                        listener.succeed(imgPaath);
                    }
                }
            }

        });
        builder.setSelectCount(1);
        AlbumActivity.show(context, builder.build());
    }

    public void handleRequestCode(int requestCode, Intent data) {
        switch (requestCode) {
            case FROME_CAMERA:
                String path = imgFile.getPath();
                Logs.i(".." + path);

                if (croped) {
                    cropImage();
                } else {
                    if (listener != null) {
                        listener.succeed(path);
                    }
                }
                break;
            case CROP_IMG:
                if (PostData.MODEL.equals("Meizu")) {
                    String imageData = data.getParcelableExtra("data");
                    if (imageData == null) {
                        cropedPath = data.getStringExtra("filePath");
                        if (!TextUtils.isEmpty(cropedPath)) {
                           // SUtils.setPicWithHolder(targetView, cropedPath, R.drawable.ic_defaul_img);
                        }
                    }
                } else {
                    //SUtils.setPicWithHolder(targetView, cropedPath, R.drawable.ic_defaul_img);
                }
                Logs.i("fielP_ath"+cropedPath);
                if (listener != null) {
                    listener.succeed(cropedPath);
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

                    Logs.i(".." + imageItem.getImagePath());
                    if (listener != null) {
                        listener.succeed(imageItem.getImagePath());
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

    public String filePath(){
        return imgFile.getPath();
    }

    public void setCroped(boolean croped) {
        this.croped = croped;
    }

    public interface OnReturnImgListener {
        void returnImg(String path);
    }
}

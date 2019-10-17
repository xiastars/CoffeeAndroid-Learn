package com.summer.demo.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.summer.demo.R;
import com.summer.demo.dialog.BaseTipsDialog;
import com.summer.demo.helper.MainHelper;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.utils.BaseUtils;
import com.summer.demo.utils.CUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/17 10:05
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_mine_phone)
    TextView mTvPhone;

    @BindView(R.id.tv_mine_push)
    TextView mTvPush;

    @BindView(R.id.tv_mine_clear)
    TextView mTvClear;

    @Override
    protected void initData() {
        setBackTag("ac_user_setting");


        int pushState = MainHelper.PUSH_STATUS;
        String pushStr = getString(R.string.mine_set_push_open_night);
        switch (pushState) {
            case 3:
                pushStr = getString(R.string.mine_set_push_open);
                break;
            case 2:
                pushStr = getString(R.string.mine_set_push_open_night);
                break;
            case 1:
                pushStr = getString(R.string.mine_set_push_close);
                break;
        }
        mTvPush.setText(pushStr);
        getAppCache();
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @OnClick({R.id.mine_set_phone, R.id.mine_set_push, R.id.mine_set_clear, R.id.mine_set_logout, R.id.ll_about})
    public void onCLick(View view) {

        switch (view.getId()) {

            case R.id.mine_set_phone:

                break;
            case R.id.mine_set_push:

                break;
            case R.id.mine_set_clear:
                onClickCleanCache();
                break;
            case R.id.mine_set_logout:

                BaseUtils.showEasyDialog(context, "确定退出登录吗？", new BaseTipsDialog.DialogAfterClickListener() {
                    @Override
                    public void onSure() {
                 /*       //埋点
                        CUtils.onClick(context,"user_set_logout_sure");
                        //清除Token
                        FSXQSharedPreference.getInstance().setTokenEable("");
                        //重新进入登录页面
                        //LoginByWchatActivity.show(UserSetActivity.this);
                        //清除缓存
                        new CommonService(context).commonDeleteData();
                        //推送Tag清空
                        MainHelper.setEmtpyJpushTag();
                        //设置为游客模式
                        MarUser.isTourist = true;
                        ActivitysManager.finishAllActivity();
                        MainActivity.show(context);*/
                    }

                    @Override
                    public void onCancel() {
                        CUtils.onClick(context,"user_set_logout_cancel");
                    }
                });

                break;
            case R.id.ll_about:

                break;
        }

    }

    private void getAppCache() {
        mTvClear.setText(acquireSize());
    }

    private String acquireSize() {
        try {
            long size = getFolderSize(new File(this.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)) + getFolderSize(new File(SFileUtils.getFileDirectory()));
            return getFormatSize(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0Byte";
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) {
                return 0;
            }
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    File fileItem = fileList[i];
                    String fileName = fileItem.getName();
                    if (fileName.equals("armeabi-v7a_ffmpeg") || fileName.equals("x86_ffmpeg")) {
                        continue;
                    }
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        String fileName = file.getName();
                        Logs.i("fileName:" + fileName);
                        if (fileName.equals("armeabi-v7a_ffmpeg") || fileName.equals("x86_ffmpeg")) {
                            return;
                        }
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearCache() {
        String ceche = String.valueOf(getCacheDir());
        deleteFolderFile(ceche, true);
        deleteFolderFile(SFileUtils.getFileDirectory(), false);
    }

    private void onClickCleanCache() {
        BaseTipsDialog tipsDialog = new BaseTipsDialog(context, "是否清空缓存?", new BaseTipsDialog.DialogAfterClickListener() {
            @Override
            public void onSure() {
                clearCache();
                CUtils.onClick(context,"user_set_clear_sure");
                SUtils.makeToast(context, "已清除缓存!");
                mTvClear.setText(acquireSize());
            }

            @Override
            public void onCancel() {
                CUtils.onClick(context,"user_set_clear_cancel");
            }
        });
        tipsDialog.hideTitle();
        tipsDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        initData();
    }

    @Override
    protected int setTitleId() {
        return R.string.mine_set_title;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_setting;
    }
}

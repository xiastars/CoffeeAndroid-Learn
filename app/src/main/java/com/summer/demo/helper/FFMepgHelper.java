package com.summer.demo.helper;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;

import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.demo.dialog.BaseTipsDialog;
import com.summer.helper.downloader.DownloadManager;
import com.summer.helper.downloader.DownloadStatus;
import com.summer.helper.downloader.DownloadTask;
import com.summer.helper.downloader.DownloadTaskListener;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;
import com.summer.zipparser.OnProgressListener;
import com.summer.zipparser.ZipPaser;

import java.io.File;

/**
 * Created by xiastars on 2017/7/14.
 */

public class FFMepgHelper {

    //下载链接
    public static final String DOWNLOAD_URL = "https://file.fensixingqiu.com/Android/src/datas.zip";

    public static void initFFMepg() {
        init();
    }

    private static void init() {
        if (checkFFMepgInit()) {
            return;
        }
        if (SUtils.getNetWorkType(AppContext.getInstance()) != SUtils.NetState.WIFI) {
            return;
        }
        final String savePath = SFileUtils.getFileDirectory() + "datas.zip";
        EasyHttp.download(AppContext.getInstance(), DOWNLOAD_URL, SFileUtils.getFileDirectory(), "datas.zip", new DownloadTaskListener() {
            @Override
            public void onDownloading(DownloadTask downloadTask) {
                if (downloadTask.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
                    zipFile(savePath);
                }
            }

            @Override
            public void onPause(DownloadTask downloadTask) {

            }

            @Override
            public void onError(DownloadTask downloadTask, int errorCode) {

            }
        });

    }

    /**
     * 先弹窗再下载
     *
     * @param context
     * @return
     */
    public static boolean initWithDialog(final Context context) {
        if (checkFFMepgInit()) {
            return true;
        }
        final BaseTipsDialog tipDialog = new BaseTipsDialog(context, "您缺失视频压缩插件，请点击下载，建议在WiFi环境在下载哦", new BaseTipsDialog.DialogAfterClickListener() {

            @Override
            public void onSure() {
                showDownloadingDialog(context);
            }

            @Override
            public void onCancel() {

            }
        });
        tipDialog.hideTitle();
        tipDialog.setOkContent("下载");
        tipDialog.show();
        return false;
    }

    private static void showDownloadingDialog(final Context context) {
        final BaseTipsDialog tipDialog = new BaseTipsDialog(context, R.layout.dialog_downloading, new BaseTipsDialog.DialogAfterClickListener() {

            @Override
            public void onSure() {

            }

            @Override
            public void onCancel() {

            }
        });
        tipDialog.show();
        final ProgressBar load_pb = (ProgressBar) tipDialog.findViewById(R.id.load_pb);
        final String savePath = SFileUtils.getFileDirectory() + "datas.zip";
        boolean isDownling = EasyHttp.existDownload(context, DOWNLOAD_URL);
        Logs.i("isDonwloadiNg:" + isDownling);
        DownloadTaskListener downloadTaskListener = new DownloadTaskListener() {
            @Override
            public void onDownloading(DownloadTask downloadTask) {
                final float progress = downloadTask.getPercent();
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        load_pb.setProgress((int) progress);
                    }
                });
                Logs.i("progress:" + progress);
                if (downloadTask.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
                    zipFile(savePath);
                    tipDialog.cancel();
                }
            }

            @Override
            public void onPause(DownloadTask downloadTask) {

            }

            @Override
            public void onError(DownloadTask downloadTask, int errorCode) {

            }
        };
        tipDialog.setTitle("正在下载中...");
        //判断是否正在下载中
        if (isDownling) {
            Logs.i("xxxxx");
            DownloadManager manager = DownloadManager.getInstance(context);
            manager.addDownloadListener(manager.getCurrentTaskById(DOWNLOAD_URL), downloadTaskListener);
            return;
        }
        SFileUtils.deleteFile(savePath);
        EasyHttp.download(AppContext.getInstance(), DOWNLOAD_URL, SFileUtils.getFileDirectory(), "datas.zip", downloadTaskListener);

    }

    /**
     * 检查有没有初始化好
     *
     * @return
     */
    public static boolean checkFFMepgInit() {
        String path = SFileUtils.getFileDirectory() + "armeabi-v7a_ffmpeg";
        File file = new File(path);
        if (file.exists()) {
            Logs.i("FFMepg已加载");
            return true;
        }
        return false;
    }

    /**
     * 将下载的zip包解压
     *
     * @param filePath
     */
    private static void zipFile(String filePath) {
        ZipPaser zipPaser = new ZipPaser(AppContext.getInstance(), new OnProgressListener() {
            @Override
            public void onParse(int size) {
                Logs.i("ffmpeg加载完毕" + size);
            }
        });
        try {
            zipPaser.UnZipFolder(filePath, SFileUtils.getFileDirectory());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

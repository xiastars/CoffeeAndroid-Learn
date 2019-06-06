package com.summer.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.BookBean;
import com.summer.helper.db.CommonService;
import com.summer.helper.downloader.DownloadManager;
import com.summer.helper.downloader.DownloadStatus;
import com.summer.helper.downloader.DownloadTask;
import com.summer.helper.downloader.DownloadTaskListener;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.lang.ref.WeakReference;

public class AppBeansHelper {
    private Context context;

    private ProgressBar mProgressBar;
    private TextView mDownload;

    private BookBean mBookBean;
    public static boolean isOnAnim = false;
    String SAVE_VERSION = null;
    DownloadManager mDownloadManager = null;
    MyHandler myHandler;
    CommonService commonService;
    int downloadIndex = 0;
    /* 图书名称 */
    String bookName;
    /* 保存的目录名 */
    String directory;
    /* 下载链接 */
    String downloadUrl;

    boolean isFirst = false;

    BookDownloadedListener listener;
    int downloadCount = 0;

    public AppBeansHelper(final Context context, ProgressBar mProgressBar,
                          TextView mDownload, BookDownloadedListener listener) {
        this.listener = listener;
        this.mProgressBar = mProgressBar;
        this.mDownload = mDownload;
        this.context = context;
        myHandler = new MyHandler(this);
    }

    public void setDownloadManager(DownloadManager downloadManager) {
        this.mDownloadManager = downloadManager;
    }

    public void setEntity(BookBean appsEn) {
        this.mBookBean = appsEn;
        SAVE_VERSION = appsEn.getApk_url();
        bookName = appsEn.getName();
        mProgressBar.setMax(100);
        directory = SFileUtils.getBookDirectory() + bookName + "/";
        downloadUrl = appsEn.getApk_url();
        initData();
    }

    private void initData() {
        DownloadTask downloadTask = mDownloadManager.getCurrentTaskById(downloadUrl);
        if (downloadTask != null) {
            downloadTask.addDownloadListener(new DownloadTaskListener() {
                @Override
                public void onDownloading(DownloadTask downloadTask) {
                    myHandler.sendEmptyMessage(0);
                }

                @Override
                public void onPause(DownloadTask downloadTask) {

                }

                @Override
                public void onError(DownloadTask downloadTask, int errorCode) {

                }
            });
        } else {
            loadStatusChange();
        }
    }

    /**
     * 改变 界面显示下载状态 和文字
     */
    public void loadStatusChange() {
        if (null == mBookBean) return;
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                transAppBeanToAppEntity(mBookBean);
                mProgressBar.setProgress(mBookBean.getProgress());
                int status = mBookBean.getStatus();
                mDownload.setTextColor(context.getResources().getColor(R.color.black));
                switch (status) {
                    case DownloadStatus.DOWNLOAD_STATUS_ERROR: // 失败——》重新加载
                        setProgressDrawable(R.drawable.progress_downloading);
                        mDownload.setText("重试");
                        break;
                    case DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING:
                        setProgressDrawable(R.drawable.progress_downloading);
                        String content = mBookBean.getProgress()+"%";
                        mDownload.setText(content);
                        break;
                    case DownloadStatus.DOWNLOAD_STATUS_COMPLETED:
                        setProgressDrawable(R.drawable.progress_open);
                        mDownload.setTextColor(context.getResources().getColor(R.color.white));
                        mDownload.setText("打开");
                        break;
                    case DownloadStatus.DOWNLOAD_STATUS_PAUSE:
                        setProgressDrawable(R.drawable.progress_pause);
                        mDownload.setText("继续");
                        break;
                    case DownloadStatus.DOWNLOAD_STATUS_INIT:
                        setProgressDrawable(R.drawable.progress_pause);
                        mDownload.setText("下载");
                        break;
                }
            }

        });
    }

    private void setProgressDrawable(int id) {
        mProgressBar.setProgressDrawable(context.getResources().getDrawable(id));
    }

    /**
     * 将AppBean转为BookBean
     *
     * @return
     */
    private void transAppBeanToAppEntity(BookBean bookBean) {
        DownloadTask downloadTask = mDownloadManager.getCurrentTaskById(downloadUrl);
        if (downloadTask != null) {
            bookBean.setStatus(downloadTask.getDownloadStatus());
            bookBean.setProgress((int) downloadTask.getPercent());
        }
        if(checkFileExist()){
            bookBean.setStatus(DownloadStatus.DOWNLOAD_STATUS_COMPLETED);
        }
    }

    /**
     * 检查文件是否已存在在本地
     * @return
     */
    private boolean checkFileExist(){
        File file = new File( directory, bookName+ SFileUtils.FileType.FILE_APK);
        if(file.exists()){
            return true;
        }
        return false;
    }

    /**
     * @return
     */
    public boolean startDownload() {
        if(checkFileExist()){
            SUtils.makeToast(context,"文件已下载");
            return false;
        }
        DownloadTask downloadTask = mDownloadManager.getCurrentTaskById(downloadUrl);
        if (downloadTask != null) {
            if (downloadTask.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_DOWNLOADING) {
                EasyHttp.pauseDownload(context, downloadUrl);
                loadStatusChange();
                return false;
            }
        }
        downloadData();
        return false;
    }

    private void downloadData() {
        SUtils.makeToast(context,downloadUrl);
        EasyHttp.download(context, downloadUrl, downloadUrl,
                directory, bookName+ SFileUtils.FileType.FILE_APK, new DownloadTaskListener() {

                    @Override
                    public void onError(DownloadTask downloadTask, int errorCode) {
                        loadStatusChange();
                    }

                    @Override
                    public void onDownloading(final DownloadTask downloadTask) {
                        Logs.i("xia",".."+downloadTask.getPercent()+",status:"+downloadTask.getDownloadStatus());
                        myHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onPause(DownloadTask downloadTask) {
                        Logs.i("xia", downloadTask.getPercent() + "");
                        loadStatusChange();
                    }
                });
    }

    /**
     * rename 文件重命名
     *
     * @param to
     * @param from
     * @return
     */
    public static File rename(File from, File to) {
        try {
            String newPath = to.getPath();
            String oldPath = from.getPath();
            if (!oldPath.equals(newPath)) {
                if (!to.exists()) {
                    from.renameTo(to);
                }
            }
        } catch (Exception ex) {
            Logs.i("Exception:" + ex.toString());
        }
        return to;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    public static class MyHandler extends Handler {
        private final WeakReference<AppBeansHelper> mActivity;

        public MyHandler(AppBeansHelper activity) {
            mActivity = new WeakReference<AppBeansHelper>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AppBeansHelper activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        activity.loadStatusChange();
                        break;
                    case 1:
                        break;
                }
            }
        }
    }

    public interface BookDownloadedListener {
        void onCallback(BookBean bean);
    }


}

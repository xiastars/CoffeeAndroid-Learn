package com.summer.demo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.text.TextUtils;

import com.summer.demo.constant.SharePreConst;
import com.summer.demo.helper.FFMepgHelper;
import com.summer.demo.module.base.BaseApplication;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.QbSdk;

import java.util.ArrayList;
import java.util.List;


public class AppContext extends BaseApplication {

    private static AppContext instance;
    IWXAPI mWxApi;

    /**
     * 切换服务器
     */
    public static int SERVER_MODE = 2;//0.是开发，1是测试，2是正式，3是预发布
    public static final boolean DEBUGMODE = true;
    public static final String DEFAULT_TOKEN = "";//444392d5387d9415a5d8e7370b96645f
    public static final String WEIXIN_ID = "";//微信KEY
    public static final String TEXT_TIME = "2018.11.12 - 15：12";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        Logs.isDebug = DEBUGMODE;

        initApp();
        initUMLib();
        if (DEBUGMODE) {
            SERVER_MODE = SUtils.getIntegerData(this, "server_mode");
        }
        setDefault();
        initTBS();
    }

    private void initTBS() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logs.i("QBS onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Logs.i("QBS onViewInitFinished is ");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


    private void setDefault() {

    }

    public void initAll() {


        //true:代表捕捉错误 false:代表不捕捉  平常写代码设置为false  提交测试置为true
        CrashReport.initCrashReport(getApplicationContext(), "f258763397", true);
        //Bugly.init(this, "f258763397", false);
        initJPushAndShare();

        initCacheAndFFmeg();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        // 安装tinker
        //Beta.installTinker();
    }

    private void initCacheAndFFmeg() {
        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {
                SFileUtils.initCache(instance);//初始化文件缓存
                FFMepgHelper.initFFMepg();
            }
        });
    }

    /**
     * 读取本地channel，只需一次
     */
    private void readLocalChannel() {
        PostData.CHANNEL = SUtils.getStringData(this, SharePreConst.READ_LOCAL_CHANNEL);
        Logs.i("CHANNEL:" + PostData.CHANNEL);
        if (!TextUtils.isEmpty(PostData.CHANNEL)) {
            return;
        }
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                PostData.CHANNEL = appInfo.metaData.getString("JPUSH_CHANNEL");
                SUtils.saveStringData(this, SharePreConst.READ_LOCAL_CHANNEL, PostData.CHANNEL);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void initApp() {
        SFileUtils.DIR_NAME = "SummerDemo";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }

    public void initJPushAndShare() {
        //JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        //JPushInterface.init(this);

    }

    public void initUMLib() {

    }

    /**
     * 取消微信授权
     */
    public void unregisterWxApi() {
        if (mWxApi != null) {
            mWxApi.unregisterApp();
        }
    }

    public IWXAPI getWxApi() {
        return mWxApi;
    }

    /**
     * 微信是否安装
     *
     * @return
     */
    public boolean isWxInstall() {
        if (mWxApi != null) {
            return mWxApi.isWXAppInstalled();
        }
        return false;
    }

    public static AppContext getInstance() {
        return instance;
    }

    public List<String> imageIds = new ArrayList<>();


    private String myUserId;


    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }
}
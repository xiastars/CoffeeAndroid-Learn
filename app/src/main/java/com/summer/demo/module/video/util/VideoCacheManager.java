package com.summer.demo.module.video.util;

import android.content.Context;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.summer.helper.utils.SFileUtils;

import java.io.File;

public class VideoCacheManager {

    private static final long CACHE_SIZE = 1024 * 1024 * 1024;//缓存大小为1G

    private static VideoCacheManager manager;
    private HttpProxyCacheServer mProxyCacheServer;
    private Context mContext;
    private String mCachePath;
    private CacheListener mCacheListener;

    public static VideoCacheManager getManager(Context context) {
        if (manager == null) {
            synchronized (VideoCacheManager.class) {
                if (manager == null)
                    manager = new VideoCacheManager(context);
            }
        }
        return manager;
    }

    private VideoCacheManager(Context context) {
        mContext = context.getApplicationContext();
        mCachePath = SFileUtils.ROOT_VIDEO;
        File file = new File(mCachePath);
        if (!file.exists())
            file.mkdirs();
        mProxyCacheServer = new HttpProxyCacheServer.Builder(mContext)
                .cacheDirectory(file)//暂时与SFileUtils统一
                .maxCacheSize(CACHE_SIZE)
                .build();
    }

    public String getCacheVideoUrl(String videoUrl) {
        return mProxyCacheServer.getProxyUrl(videoUrl);
    }

    public boolean isCached(String videoUrl) {
        return mProxyCacheServer.isCached(videoUrl);
    }

    public void registerCacheListener(final OnCacheListener listener, final String videoUrl) {
        if (mCacheListener != null) {
            unregisterCacheListener();
        }
        mCacheListener = new CacheListener() {
            @Override
            public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
                if (listener != null)
                    listener.onCache(cacheFile, url, percentsAvailable);
            }
        };
        mProxyCacheServer.registerCacheListener(mCacheListener, videoUrl);
    }

    public void unregisterCacheListener() {
        if (mCacheListener != null)
            mProxyCacheServer.unregisterCacheListener(mCacheListener);
    }

    public void clearCache() {
        SFileUtils.clearDirectory(mCachePath);
    }

    public long getCacheSize() {
        return SFileUtils.getFolderSize(new File(mCachePath));
    }

    //方便日后变更兼容
    public interface OnCacheListener {
        void onCache(File cacheFile, String url, int percentsAvailable);
    }
}

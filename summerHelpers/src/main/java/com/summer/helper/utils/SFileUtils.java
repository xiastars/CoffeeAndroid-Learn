package com.summer.helper.utils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 处理数据相关
 *
 * @author xiaqiliang
 */
public class SFileUtils {
    /**
     * 根目录
     */
    public static String ROOT_PATH = SUtils.getSDPath() + "/";
    /**
     * 资源根目录
     */
    public static String SOURCE_PATH = ROOT_PATH + "同步点读/";
    /**
     * 我的录音根目录
     */
    public static String ROOT_AUDIO = SOURCE_PATH + "audio/";
    /**
     * 背景图根目录
     */
    public static String ROOT_BACKGROUND = SOURCE_PATH + "pics/";
    /**
     * 图书目录
     */
    public static String ROOT_BOOK = SOURCE_PATH + "books/";
    /**
     * 预览图根目录
     */
    public static String ROOT_AVATAR = SOURCE_PATH + ".avatar/";

    /**
     * 资源后缀
     */
    public class FileType {
        public static final String FILE_MP4 = ".mp4";
        public static final String FILE_PNG = ".png";
        public static final String FILE_APK = ".apk";
    }

    /**
     * 安全获取目录
     *
     * @param path
     * @return
     */
    public static String getDirectorySafe(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getBookDirectory() {
        String path = ROOT_BOOK;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getAudioDirectory() {
        String path = ROOT_AUDIO;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getAvatarDirectory() {
        String path = ROOT_AVATAR;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getVideoDirectory() {
        String path = SOURCE_PATH + "video/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 安全获取目录
     *
     * @return
     */
    public static String getImageViewDirectory() {
        String path = ROOT_BACKGROUND;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 根据名称获取该名称下的图片集合
     *
     * @param name
     * @return
     */
    public static String[] getResourceByName(String name) {
        File file = new File(name);
        if (file.exists() && file.isDirectory()) {
            String[] files = file.list(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    if (!filename.endsWith(".png") && !filename.endsWith(".jpg")) {
                        return false;
                    }
                    return true;
                }
            });
            for (int i = 0; i < files.length; i++) {
                files[i] = file.getAbsolutePath() + "/" + files[i];
            }
            return files;
        }
        return null;
    }

}

package com.summer.demo.module.album.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 与所有的图片选择相关
 *
 * @author xiastars
 * @version 2017年10月18日  下午11:50:49
 */
public class AlbumSet {
    public static List<Activity> activitys = new ArrayList<>();

    public static int MAX_SELECT_COUNT = 1;

    public static void finishAll() {
        for (int i = 0; i < activitys.size(); i++) {
            if (null != activitys.get(i)) {
                activitys.get(i).finish();
            }
        }
    }
}

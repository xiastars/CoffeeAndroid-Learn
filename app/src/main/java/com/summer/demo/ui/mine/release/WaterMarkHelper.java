package com.summer.demo.ui.mine.release;

import com.summer.demo.AppContext;
import com.summer.helper.utils.SUtils;

/**
 * 水印管理
 */
public class WaterMarkHelper {
    private static boolean supportWaterMark;
    private static boolean tempWatermark = true;//临时是否支持

    public final static String SUPPORT_WATER_MARK = "SUPPORT_WATER_MARK";

    public static void setSupportWatermark(boolean support) {
        supportWaterMark = support;
    }

    public static boolean isTempWatermark() {
        return tempWatermark;
    }

    public static void setTempWatermark(boolean tempWatermark) {
        WaterMarkHelper.tempWatermark = tempWatermark;
    }

    /**
     * 是否设置水印
     *
     * @return
     */
    public static boolean isWaterMarkSupport() {
        return supportWaterMark;
    }

    public static void setSupportWaterMark(int isSupport, String groupId) {
        SUtils.saveIntegerData(AppContext.getInstance(), SUPPORT_WATER_MARK + "_" + groupId, isSupport);
    }

    public static int getGroupSupportWaterMark(String groupId) {
        return SUtils.getIntegerData(AppContext.getInstance(), SUPPORT_WATER_MARK + "_" + groupId);
    }
}

package com.summer.helper.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式相关
 *
 * @author xiaqiliang
 */
public class PatternUtils {

    /**
     * 检测A在B里出现的次数
     *
     * @param a
     * @param b
     * @return
     */
    public static int countAInB(String a, String b) {
        Pattern pattern = Pattern.compile(a, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(b);
        // find会在整个输入中寻找是否有匹配的子字符串
        int index = 0;
        while (matcher.find()) {
            index++;
        }
        return index;
    }


    /**
     * 获取HTML标签里的元素，例:
     * source = "<a title=\"你好\">";
     * getContentInHtmlTag(content, "a", "title");
     * result -> 你好
     *
     * @param source
     * @param element
     * @param attr
     * @return
     */
    public static String getContentInHtmlTag(String source, String element, String attr) {
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            return r;
        }
        return "";
    }

    /**
     * 是否为纯汉字
     *
     * @param str
     * @return
     */
    public static boolean isPureHanzi(String str) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = pattern.matcher(str);
        return m.find() && m.group(0).equals(str);
    }

    /**
     * 是否为正确的邮箱
     *
     * @param email
     * @return
     */
    public static boolean isRightfulEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 替换一段文本中指定的文本的第一个
     * @param input 原文本
     * @param regex 被替换文本
     * @param replacement 替换文本
     * @return
     */
    public static String getReplaceFirst(final String input,
                                         final String regex,
                                         final String replacement) {
        if (input == null) return "";
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }



    /**
     * 替换一段文本中指定的文本
     * @param input 原文本
     * @param regex 被替换文本
     * @param replacement 替换文本
     * @return
     */
    public static String getReplaceAll(final String input,
                                       final String regex,
                                       final String replacement) {
        if (input == null) return "";
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }

}

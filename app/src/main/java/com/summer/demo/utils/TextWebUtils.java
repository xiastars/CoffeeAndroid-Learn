package com.summer.demo.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.SpannableInfo;
import com.summer.demo.bean.SubjectInfo;
import com.summer.demo.ui.ViewBigPhotoActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.web.WebContainerActivity;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextWebUtils {

    /**
     * 设置支持自定义标签与链接的Text
     *
     * @param contentStr
     * @param showName
     */
    public static void setHtmlText(TextView showName, String contentStr) {
        SpannableInfo nameContent = replaceEmoji(showName.getContext(), contentStr, 35);
        showName.setText(nameContent.getBuilder());
    }


    public static SpannableStringBuilder append(SpannableStringBuilder spannableString, String content, int color) {
        return append(spannableString, content, color, false);
    }

    public static SpannableStringBuilder append(SpannableStringBuilder spannableString, String content, int color, boolean isBold) {
        if(content == null){
            content = "";
        }
        if (color == 0) {
            spannableString.append(content);
        } else {
            SpannableString msp = new SpannableString(content);
            msp.setSpan(new ForegroundColorSpan(color), 0, content.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (isBold) {
                msp.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            spannableString.append(msp);
        }
        return spannableString;
    }

    public static SpannableStringBuilder append(String content) {
        return append(content, 0);
    }

    public static SpannableStringBuilder append(String content, int color) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (color == 0) {
            spannableString.append(content);
        } else {
            SpannableString msp = new SpannableString(content);
            int length = spannableString.toString().length();
            msp.setSpan(new ForegroundColorSpan(color), length, length + content.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.append(msp);
        }
        return spannableString;
    }


    /**
     * 将文本中的自定义标签与链接转换为特殊的标识
     *
     * @param contentStr
     * @return
     */
    public static SpannableInfo replaceEmoji(final Context context, String contentStr, int textSize) {
        return replaceEmoji(context, null, contentStr, textSize, false);
    }

    public static SpannableInfo replaceEmoji(final Context context, SpannableStringBuilder spannableString, String contentStr, int textSize) {
        return replaceEmoji(context, spannableString, contentStr, textSize, false);
    }

    /**
     * 将文本中的自定义标签与链接转换为特殊的标识
     *
     * @param contentStr
     * @return
     */
    public static SpannableInfo replaceEmoji(final Context context, SpannableStringBuilder spannableString, String contentStr, int textSize, final boolean onlyImg) {
        if (spannableString == null) {
            spannableString = new SpannableStringBuilder();
        }
        SpannableInfo spannableInfo = new SpannableInfo();
        if (!TextUtils.isEmpty(contentStr)) {
            contentStr = trim(contentStr);
            //如果包含多条链接和话题就分割了做，暂时没有想到好法子
            if (contentStr.contains(">")) {
                String[] datas = contentStr.split(">");
                int index = 0;
                for (String data : datas) {
                    index++;
                    if (index < datas.length) {
                        data += ">";
                    }
                    SpannableStringBuilder builder = replaceSingle(context, spannableInfo, data, textSize, onlyImg);
                    spannableString.append(builder);
                }
            } else {
                SpannableStringBuilder builder = replaceSingle(context, spannableInfo, contentStr, textSize, onlyImg);
                spannableString.append(builder);
            }
        } else {
         /*   SpannableStringBuilder builder = replaceSingle(context, spannableInfo, spannableString.toString(), textSize, onlyImg);
            spannableString.append(builder);*/
        }
        spannableInfo.setBuilder(spannableString);
        return spannableInfo;
    }


    /**
     * 将文本中的Tag转换
     *
     * @return
     */
    public static SpannableStringBuilder replaceTagsOnly(SpannableStringBuilder spannableString) {
        if (spannableString == null) {
            spannableString = new SpannableStringBuilder();
        }
        String regex_http = "<e type=\"hashtag\".*>";
        Pattern pattern = Pattern.compile(regex_http, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(spannableString);
        //find会在整个输入中寻找是否有匹配的子字符串
        while (matcher.find()) {
            try {
                String content = spannableString.subSequence(matcher.start(), matcher.end()).toString();
                final String url = getUrl(content);
                final String title = getWebTitle(content);
                final String hid = match(content, "e", "hid");
                String finalContent = "#" + title + "#";
                Logs.i("title:"+title);
                SpannableString imageSpan = new SpannableString(finalContent);
                if (!TextUtils.isEmpty(hid)) {
                    imageSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#5620F0")), 0, finalContent.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    imageSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, finalContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                spannableString.replace(matcher.start(), matcher.end(), imageSpan);
            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            }
        }
        return spannableString;
    }

    public static SpannableStringBuilder replaceSingle(final Context context, SpannableInfo spannableInfo, String contentData, int textSize, final boolean onlyImg) {
        String regex_http = "<e type=\"web\".*>";
        Pattern pattern = Pattern.compile(regex_http, Pattern.CASE_INSENSITIVE);
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append(contentData);
        Matcher matcher = pattern.matcher(spannableString);
        //find会在整个输入中寻找是否有匹配的子字符串
        while (matcher.find()) {
            try {
                String content = spannableString.subSequence(matcher.start(), matcher.end()).toString();
                final String url = getUrl(content);
                final String title =  getWebTitle(content);
                boolean showOnlyImg = false;
                if (!TextUtils.isEmpty(title) && title.equals("「查看图片」") && onlyImg) {
                    showOnlyImg = true;
                }
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), showOnlyImg ? R.drawable.trans : R.drawable.link_icon);
                //设置表情显示的大小
                if(showOnlyImg){
                    textSize = 5;
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, textSize, textSize, true);
                ImageSpan imageSpan = new ImageSpan(context, bitmap, DynamicDrawableSpan.ALIGN_BASELINE);
                spannableString.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString string = new SpannableString(TextUtils.isEmpty(title) ? "  网页链接" : title);
                if(matcher.start() >= 0){
                    string.setSpan(new StyleSpan(Typeface.BOLD), 0,string.toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                spannableString.insert(matcher.end(), string);
                Logs.i(title + "-----------------");
                NfClickableSpan clickableSpan = new NfClickableSpan(context.getResources().getColor(showOnlyImg ? R.color.blue_56 : R.color.blue_56)) {
                    @Override
                    public void onClick(View widget) {
                        if (!TextUtils.isEmpty(title) && title.equals("「查看图片」")) {
                            JumpTo.getInstance().commonJump(context, ViewBigPhotoActivity.class, url);
                        } else {
                            try {
                                if(isSymlink(new File(url))){
                                    SUtils.makeToast(context,"url"+url);
                                }else{
                                    Intent intent = new Intent(context, WebContainerActivity.class);
                                    intent.putExtra(JumpTo.TYPE_STRING, url);
                                    intent.putExtra("key_title", title);
                                    context.startActivity(intent);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                };
                int end = matcher.end() + string.toString().length();
                spannableString.setSpan(clickableSpan, matcher.end() - 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            }
        }
        return replaceTags(context,spannableInfo, spannableString);
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File must not be null");
        File canon;
        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }
        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    /**
     * 将文本中的Tag转换
     *
     * @return
     */
    public static SpannableStringBuilder replaceTags(final Context context, SpannableInfo spannableInfo, SpannableStringBuilder spannableString) {
        if (spannableString == null) {
            spannableString = new SpannableStringBuilder();
        }
        String regex_http = "<e type=\"hashtag\".*>";
        Pattern pattern = Pattern.compile(regex_http, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(spannableString);
        //find会在整个输入中寻找是否有匹配的子字符串
        while (matcher.find()) {
            try {
                final String content = spannableString.subSequence(matcher.start(), matcher.end()).toString();
                final String url = getUrl(content);
                final String title =  getWebTitle(content);
                final String hid = match(content, "e", "hid");
                String finalContent = "#" + title + "#";
                SpannableString imageSpan = new SpannableString(finalContent);
                if (spannableInfo != null && spannableInfo.getSubjectInfo() == null) {
                    SubjectInfo subjectInfo = new SubjectInfo();
                    subjectInfo.setId(hid);
                    subjectInfo.setTitle(title);
                    spannableInfo.setSubjectInfo(subjectInfo);
                }
                if (!TextUtils.isEmpty(hid)) {
                    imageSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#5620F0")), 0, finalContent.length(),
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    imageSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, finalContent.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }

                spannableString.replace(matcher.start(), matcher.end(), imageSpan);
                NfClickableSpan clickableSpan = new NfClickableSpan(Color.parseColor("#5620F0")) {
                    @Override
                    public void onClick(View widget) {
                        Logs.i("=-=-");
                        SUtils.makeToast(context,"点击了标签");

                    }
                };
                spannableString.setSpan(clickableSpan, matcher.start(), matcher.start()+finalContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            }
        }
        return spannableString;
    }

    /**
     * 生成一个Span
     *
     * @param spannableString
     * @param unSpanText
     */
    public static SpannableStringBuilder generateOneSpan(Context context, SpannableStringBuilder spannableString, String unSpanText, float textSize, int textColor) {
        unSpanText = unSpanText + " ";
        if (unSpanText.contains(">")) {
            String[] datas = unSpanText.split(">");
            int index = 0;
            for (String data : datas) {
                index++;
                if (index < datas.length) {
                    data += ">";
                }
                SpannableStringBuilder builder = changeTagToImg(context, new SpannableStringBuilder(data), textSize, textColor);
                spannableString.append(builder);
            }
        } else {
            SpannableStringBuilder builder = changeTagToImg(context, new SpannableStringBuilder(unSpanText), textSize, textColor);
            spannableString.append(builder);
        }
        return spannableString;
    }

    /**
     * 将文本中的Tag转换
     *
     * @return
     */
    public static SpannableStringBuilder changeTagToImg(Context context, SpannableStringBuilder spannableString, float textSize, int textColor) {
        if (spannableString == null) {
            spannableString = new SpannableStringBuilder();
        }
        String regex_http = "<e type=\"hashtag\".*>";
        Pattern pattern = Pattern.compile(regex_http, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(spannableString);
        //find会在整个输入中寻找是否有匹配的子字符串
        Logs.i("spa:" + spannableString);
        while (matcher.find()) {
            try {
                String content = spannableString.subSequence(matcher.start(), matcher.end()).toString();
                final String title = getWebTitle(content);
                final String hid = match(content, "e", "hid");
                String finalContent = "#" + title + "#";
                Logs.i("finalContent:" + finalContent);
                View spanView = getSpanView(context, finalContent, SUtils.screenWidth, textSize, textColor);

                BitmapDrawable bitmpaDrawable = (BitmapDrawable) convertViewToDrawable(spanView);
                bitmpaDrawable.setBounds(0, 0, bitmpaDrawable.getIntrinsicWidth(), bitmpaDrawable.getIntrinsicHeight());
                ImageSpan imageSpan = new ImageSpan(bitmpaDrawable);
                final int start = matcher.start();
                final int end = matcher.end();

                Logs.i("spa::" + spannableString.toString().length() + ",,," + start + ",,," + end);
                spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (NullPointerException e) {
                e.printStackTrace();
                continue;
            }
        }
        return spannableString;
    }


    /**
     * 生成一个Span
     *
     * @param spannableString
     * @param unSpanText
     */
    public static SpannableStringBuilder generateAllSpan(Context context, SpannableStringBuilder spannableString, String unSpanText, float textSize, int textColor) {
        final String title = getWebTitle(unSpanText);
        View spanView = getSpanView(context, "#" + title + "#", SUtils.screenWidth, textSize, textColor);

        BitmapDrawable bitmpaDrawable = (BitmapDrawable) convertViewToDrawable(spanView);
        bitmpaDrawable.setBounds(0, 0, bitmpaDrawable.getIntrinsicWidth(), bitmpaDrawable.getIntrinsicHeight());
        ImageSpan imageSpan = new ImageSpan(bitmpaDrawable);
        final int start = spannableString.toString().length();
        final int end = start + unSpanText.length();
        spannableString = spannableString.append(unSpanText);

        spannableString.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private final static int UPPER_LEFT_X = 0;
    private final static int UPPER_LEFT_Y = 0;

    public static Drawable convertViewToDrawable(View view) {
        int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        Logs.i("width:::" + view.getMeasuredWidth());
        view.layout(UPPER_LEFT_X, UPPER_LEFT_Y, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Logs.i("width:::" + view.getMeasuredWidth());
        Canvas c = new Canvas(b);
        //c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);

        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        cacheBmp.recycle();
        view.destroyDrawingCache();
        return new BitmapDrawable(viewBmp);
    }

    /**
     * 获得span视图
     *
     * @param context
     * @return
     */
    public static View getSpanView(Context context, String text, int maxWidth, float textSize, int textColor) {
        TextView view = new TextView(context);
        Paint paint = new Paint();

        paint.setTextSize(textSize);
        view.setMaxWidth(maxWidth * 2);
        view.setText(text);
        view.setSingleLine(true);
        view.setTextColor(textColor);
        view.setTextSize(textSize);
        return view;
    }

    public static int getTextWidth(Paint paint, String str) {
        int w = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                w += (int) Math.ceil(widths[j]);
            }
        }
        return w;
    }


    private static String getUrl(String var) {
        /**
         * 获取指定HTML标签的指定属性的值
         * @param source 要匹配的源文本
         * @param element 标签名称
         * @param attr 标签的属性名称
         * @return 属性值列表
         */

        return match(var, "e", "href");
    }

    /**
     * 清除链接里的换行符
     *
     * @param source
     * @return
     */
    public static String trim(String source) {
        String reg = "<.*?>";
        source = source.replaceAll("\n", "&#&");
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group();
            String rr = r.replaceAll("&#&", "");
            StringBuilder builder = new StringBuilder(source);
            builder.replace(m.start(), m.end(), rr);
            source = builder.toString();
            source = source.replaceAll("&#&", "\n");
            return source + " ";
        }
        return source.replaceAll("&#&", "\n") + " ";
    }

    public static String getWebTitle(String source){

        String reg = "title=\"(.*?)\"";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            Logs.i("source:"+r+".."+m.group());
            return r.replaceAll("&#&"," ");
        }
        return "";
    }

    public static String match(String source, String element, String attr) {
        //source = source.trim();

        Logs.i("source:"+source);
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?\\s?(.*?)\\s?['\"]?(\\s.*?)?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            Logs.i("source:"+r+".."+m.group());
            return r.replaceAll("&#&"," ");
        }
        return "";
    }

    public static String returnNewUrl(String source) {

        String URL_REGEX = "(((http|ftp|https)://)|(www\\.))[a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6}(:[0-9]{1,4})?(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        String reg = "(https:\\/\\/|http:\\/\\/)[a-zA-Z]+(\\.[a-z0-9A-Z]+)*\\.[a-zA-Z]+((\\/|\\?)(\\w|\\%|\\/|\\&|\\.|\\=|\\?|\\-|\\#)+)*/";
        Matcher m = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE).matcher(source);
        while (m.find()) {
            String r2 = m.group();
            String r3 = m.group(2);
            Logs.i("rs:" + r3);
            Logs.i(r2);
            if (source.contains("<e type=\"web\"")) {
                return source;
            }
            return source.replace(r2, "<e type=\"web\" title=\"网页链接\" href=\"" + r2 + "\" />");
        }
        return source;
    }

    /**
     * 将话题格式化
     *
     * @param info
     * @return
     */
    public static String returnTag(SubjectInfo info) {
        return "<e type=\"hashtag\" title=\"" + info.getTitle() + "\" hid=\"" + info.getId() + "\" />";
    }

}

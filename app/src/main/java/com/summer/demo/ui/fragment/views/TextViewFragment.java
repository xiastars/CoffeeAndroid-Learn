package com.summer.demo.ui.fragment.views;

import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.base.BaseFragment;
import com.summer.demo.utils.TextWebUtils;
import com.summer.helper.utils.STextUtils;

import java.math.BigDecimal;

import butterknife.BindView;

/**
 * @Description: TextView的基本用法
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/14 16:21
 */
public class TextViewFragment extends BaseFragment {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_super)
    TextView tvSuper;
    @BindView(R.id.tv_span)
    TextView tvSpan;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(View view) {
        tvContent.setText("有一些特殊的操作是xml里没有提供的，比如下划线，这里演示用代码来改变样式");
        //设置文本字体大小
        tvContent.setTextSize(20);
        tvContent.setLetterSpacing(1f);
        Paint paint = tvContent.getPaint();
        //给文本添加下划线
        paint.setUnderlineText(true);
        paint.setTextSkewX(-1);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append("TextView配合SpannableStringBuilder的一些基本用法：成功续费 ");
        String fee = "￥" + new BigDecimal(10000 / 100f).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        builder.append(STextUtils.getSpannableView(fee, 0, fee.length(), getResColor(R.color.blue_4c), 0, true));
        builder.append(" 继续关注 ");
        builder.append(STextUtils.getSpannableView("xiastars", 0, 8, getResColor(R.color.red_d3), 1.5f, true));
        tvSpan.setText(builder);

        String content = "TextView配合SpanningString的高级用法，试试点击以下三个标签：将链接转为短链<e type=\"web\" title='=\"图片链接\"" +
                " href=\"https://movie.douban.com/\">；自定义标签识别<e type=\"hashtag\" hid='=\"ViewPager\" " +
                " title=\"ViewPager\" >; 图片标签<e type=\"web\" title=\"「查看图片」\" href=\"https://img1.doubanio.com/view/photo/l/public/p2324017307.webp\" >；";

        TextWebUtils.setHtmlText(tvSuper,content);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_textview;
    }

}

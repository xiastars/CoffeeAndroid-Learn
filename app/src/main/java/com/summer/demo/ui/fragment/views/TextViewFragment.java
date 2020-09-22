package com.summer.demo.ui.fragment.views;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.SpannableStringBuilder;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.view.customfragment.text.MyJustifiedTextView;
import com.summer.demo.utils.TextWebUtils;
import com.summer.helper.utils.Logs;
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
    @BindView(R.id.tv_justify)
    MyJustifiedTextView tvJustify;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView(View view) {

        ActionMode.Callback2 textSelectionActionModeCallback;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textSelectionActionModeCallback = new ActionMode.Callback2() {
                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    MenuInflater menuInflater = actionMode.getMenuInflater();
                    menu.clear();
                    menuInflater.inflate(R.menu.selection_action_menu, menu);
                    return true;//返回false则不会显示弹窗
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    //根据item的ID处理点击事件
                    switch (menuItem.getItemId()) {
                        case R.id.Informal22:
                            Toast.makeText(context, "点击的是22", Toast.LENGTH_SHORT).show();
                            actionMode.finish();//收起操作菜单
                            break;
                        case R.id.Informal33:
                            Toast.makeText(context, "点击的是33", Toast.LENGTH_SHORT).show();
                            actionMode.finish();
                            break;
                    }
                    return false;//返回true则系统的"复制"、"搜索"之类的item将无效，只有自定义item有响应
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {

                }

                @Override
                public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
                    //可选  用于改变弹出菜单的位置
                    super.onGetContentRect(mode, view, outRect);
                }
            };
            tvJustify.setCustomSelectionActionModeCallback(textSelectionActionModeCallback);
        }

        tvContent.setText("有一些特殊的操作是xml里没有提供的，比如下划线，这里演示用代码来改变样式");
        //设置文本字体大小
        tvContent.setTextSize(20);
        tvContent.setLetterSpacing(1f);
        Paint paint = tvContent.getPaint();
        //给文本添加下划线
        paint.setUnderlineText(true);
        paint.setTextSkewX(-1);
        String jcontent = "英文文字排版：Like all pandemics, it started out small. A novel coronavirus emerged in Brazil, jumping from bats to pigs to farmers before making its way to a big city with an international airport. From there, infected travellers carried it to the United States, Portugal and China. Within 18 months, the coronavirus had spread around the world, 65 million people were dead and the global economy was in free fall.\n" +
                "\n" +
                "This fictitious scenario, dubbed Event 201, played out in a New York City conference centre before a panel of academics, government officials and business leaders last October. Those in attendance were shaken — which is what Ryan Morhard wanted. A biosecurity specialist at the World Economic Forum in Geneva, Switzerland, Morhard worried that world leaders weren’t taking the threat of a pandemic seriously enough. He wanted to force them to confront the potentially immense human and economic toll of a global outbreak. “We called it Event 201 because we’re seeing up to 200 epidemic events per year, and we knew that, eventually, one would cause a pandemic,” Morhard says.\n" +
                "\n" +
                "The timing, and the choice of a coronavirus, proved prescient. Just two months later, China reported a mysterious pneumonia outbreak in the city of Wuhan — the start of the COVID-19 pandemic that has so far killed around 650,000 people.\n" +
                "\n" +
                "Morhard was not the only one sounding the alarm. Event 201 was one of dozens of simulations and evaluations over the past two decades that have highlighted the risks of a pandemic and identified gaps in the ability of governments and organizations around the world to respond.\n" +
                "\n" ;
                tvJustify.setText(jcontent);

        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append("TextView配合SpannableStringBuilder的一些基本用法：成功续费 ");
        String fee = "￥" + new BigDecimal(10000 / 100f).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        builder.append(STextUtils.getSpannableView(fee, 0, fee.length(), getResColor(R.color.blue_4c), 0, true));
        builder.append(" 继续关注 ");
        builder.append(STextUtils.getSpannableView("xiastars", 0, 8, getResColor(R.color.red_d3), 1.5f, true));
        tvSpan.setText(builder);

        String content = "TextView配合SpanningString的高级用法，试试点击以下三个标签：将链接转为短链<e type=\"web\" title=\"刘强东，变身IPO收割机\"" +
                " href=\"https://news.pedaily.cn/202009/460097.shtml\">；自定义标签识别<e type=\"hashtag\" hid='=\"ViewPager\" " +
                " title=\"ViewPager\" >; 图片标签<e type=\"web\" title=\"「查看图片」\" href=\"https://img1.doubanio.com/view/photo/l/public/p2324017307.webp\" >；";

        TextWebUtils.setHtmlText(tvSuper, content);
        long time = System.currentTimeMillis();
        //尽量不要用这个format，特别是在列表里
        for (int i = 0; i < 1000; i++) {
            context.getString(R.string.test_string, "你好哈哈哈哈");

        }
        Logs.t(time);
        time = System.currentTimeMillis();
        //拼接字符串用这种
        for (int i = 0; i < 1000; i++) {
            STextUtils.spliceText("哈哈", "你好哈哈哈哈");

        }
        Logs.t(time);

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

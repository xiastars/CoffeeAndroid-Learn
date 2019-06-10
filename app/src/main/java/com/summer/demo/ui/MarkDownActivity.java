package com.summer.demo.ui;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.base.BaseActivity;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.zzhoujay.markdown.MarkDown;

import butterknife.BindView;

/**
 * @Description: 用来承载MarkDown内容
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 15:03
 */
public class MarkDownActivity extends BaseActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.ac_markdown;
    }

    @Override
    protected void initData() {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                return null;
            }
        };
        String content = SUtils.readAssetFileToString(context,"detail/learnjava1.txt");
        Logs.i("content:"+content);
        Spanned spanned = MarkDown.fromMarkdown(content, imageGetter, tvContent);
        tvContent.setText(spanned);
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }
}

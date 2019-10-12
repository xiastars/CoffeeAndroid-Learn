package com.summer.demo.ui.course;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.SUtils;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlLongClickListener;

import butterknife.BindView;

/**
 * @Description: 用来承载MarkDown内容
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 15:03
 */
public class MarkDownFragment extends BaseFragment {
    @BindView(R.id.tv_content)
    TextView tvContent;

    public static MarkDownFragment show(String path){
        MarkDownFragment markDownFragment = new MarkDownFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path",path);
        markDownFragment.setArguments(bundle);
        return markDownFragment;
    }


    @Override
    protected int setContentView() {
        return R.layout.ac_markdown;
    }

    @Override
    protected void initView(View view) {
        String path = getArguments().getString("path");
        String content = SUtils.readAssetFileToString(context,path);
        RichText.fromMarkdown(content).clickable(true).urlLongClick(new OnUrlLongClickListener() {
            @Override
            public boolean urlLongClick(String url) {
               // Toast.makeText(getApplicationContext(),url,Toast.LENGTH_SHORT).show();
                return true;
            }
        }).into(tvContent);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }
}

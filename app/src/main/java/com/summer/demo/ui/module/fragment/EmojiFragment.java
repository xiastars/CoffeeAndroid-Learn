package com.summer.demo.ui.module.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.module.comment.CommentHelper;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/11 17:16
 */
public class EmojiFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        CommentHelper commentHelper = new CommentHelper(view);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_emoji;
    }
}

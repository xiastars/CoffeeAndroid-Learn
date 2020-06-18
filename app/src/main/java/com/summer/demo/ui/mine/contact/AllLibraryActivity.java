package com.summer.demo.ui.mine.contact;

import com.summer.demo.R;

/**
 * 全部明星
 * Created by xiastars on 2017/11/8.
 */

public class AllLibraryActivity extends AllLibrarySelectActivity{

    @Override
    protected int setTitleId() {
        return R.string.title_all_library;
    }

    @Override
    protected AllLibraryAdapter getAdapter() {
        return new AllLibraryAdapter(context);
    }
}

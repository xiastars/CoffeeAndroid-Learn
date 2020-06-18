package com.summer.demo.ui.mine.contact;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.summer.demo.ui.mine.contact.bean.LibraryInfo;
import com.summer.helper.listener.OnReturnObjectClickListener;

/**
 * Created by xiastars on 2017/11/21.
 */

public class AllLibrarySelectAdapter extends AllLibraryAdapter {

    OnReturnObjectClickListener listener;

    public AllLibrarySelectAdapter(Context context, OnReturnObjectClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onItemClick(LibraryInfo info, LinearLayout llAskOther, int position, ImageView ivArrow) {
        listener.onClick(info);
    }
}

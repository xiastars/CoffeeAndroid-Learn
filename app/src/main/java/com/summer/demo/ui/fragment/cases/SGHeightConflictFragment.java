package com.summer.demo.ui.fragment.cases;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.ui.fragment.BaseFragment;
import com.summer.demo.view.CustomScrollView;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.NRecycleView;

/**
 * *Created by summer on 2016年12月14日 16:24.
 */

public class SGHeightConflictFragment extends BaseFragment implements View.OnClickListener {
    LinearLayout rlDownload;
    LinearLayout llDelete;
    CustomScrollView scrollView;
    LinearLayout llTop;
    NRecycleView svBooks;
    SGHeightConflictAdapter shelfAdapter;
    TextView tvCount;
    TextView tvDelete;
    int size = 102;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sgconflict,null);
        initView(view);
        return view;
    }

    public void onPause() {
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView(View view) {
        rlDownload = (LinearLayout) view.findViewById(R.id.bookshelf_download);
        SUtils.clickTransColor(rlDownload);
        tvCount = (TextView) view.findViewById(R.id.tv_book_count);
        rlDownload.setOnClickListener(this);
        scrollView = (CustomScrollView) view.findViewById(R.id.scrollview);
        llTop = (LinearLayout) view.findViewById(R.id.ll_top);
        svBooks = (NRecycleView) view.findViewById(R.id.bookshelf_gridview);
        svBooks.setGridView(4);
        shelfAdapter = new SGHeightConflictAdapter(context);
        svBooks.setAdapter(shelfAdapter);
        tvDelete = (TextView) view.findViewById(R.id.tv_delete);
        llDelete = (LinearLayout) view.findViewById(R.id.ll_delete);
        SUtils.clickTransColor(llDelete);
        llDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete:
                boolean downloadstate = shelfAdapter.getDeleteState();
                if (!downloadstate) {
                    downloadstate = true;
                    tvDelete.setText("取消");
                } else {
                    downloadstate = false;
                    tvDelete.setText("删除");
                }
                shelfAdapter.cancelDeleteState(downloadstate);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int addone = size % 4;
        int lines = size / 4;
        if (addone != 0) {
            lines++;
        }
        final LinearLayout.LayoutParams pa = (LinearLayout.LayoutParams) svBooks.getLayoutParams();
        final int lastHeight = (int) (lines * context.getResources().getDimension(R.dimen.size_355_5)+context.getResources().getDimension(R.dimen.size_25));
        /* 不让GridView自动滚动的方法就是先设置一个比较短的高度，再设置全部高度，测试方法，注释下面一行代码 */
        pa.height = 400;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pa.height = lastHeight;
                svBooks.requestLayout();
                svBooks.invalidate();
            }
        },20);

        shelfAdapter. notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        scrollView.setTopView(llTop);
    }
}

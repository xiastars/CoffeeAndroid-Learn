package com.summer.demo.ui.view.commonfragment.viewpager;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.view.adapter.HeightSizeAdapter;
import com.summer.demo.ui.view.commonfragment.recyclerview.GalleryRecyclerView;
import com.summer.demo.ui.view.commonfragment.viewpager.bean.HsizeInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/7/21 10:18
 */
public class UserHeightFragment extends BaseFragment {
    @BindView(R.id.gallery_view)
    GalleryRecyclerView galleryView;

    @Override
    protected void initView(View view) {
        HeightSizeAdapter mFunQuestionAdapter = new HeightSizeAdapter(context);
        galleryView.setAdapter(mFunQuestionAdapter);
        galleryView.setOnScrollStopListener(new GalleryRecyclerView.OnScrollStopListener() {
            @Override
            public void onScrollStop(int position) {
          /*      if (starQuestions == null) {
                    return;
                }
                LibraryInfo info = starQuestions.get(position);
                setQuestionBottomContent(info);*/
            }
        });
        List<HsizeInfo> sizes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            HsizeInfo hsizeInfo = new HsizeInfo();
            hsizeInfo.setSize(i);
            if (i % 5 == 0 || i % 10 == 0) {
                hsizeInfo.setBig(true);
            }
            sizes.add(hsizeInfo);
        }
        mFunQuestionAdapter.notifyDataSetChanged(sizes);

        galleryView.scrollToPosition(20);
    }

    @Override
    public void loadData() {


    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_hsize;

    }
}

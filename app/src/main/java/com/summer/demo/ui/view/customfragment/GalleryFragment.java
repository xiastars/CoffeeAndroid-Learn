package com.summer.demo.ui.view.customfragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.bean.BookBean;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.view.adapter.FunQuestionGalleryAdapter;
import com.summer.demo.view.GalleryView;
import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 横向滚动Gallery
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/10 17:07
 */
public class GalleryFragment extends BaseFragment {
    @BindView(R.id.gallery_view)
    GalleryView galleryView;

    @Override
    protected void initView(View view) {
        FunQuestionGalleryAdapter mFunQuestionAdapter = new FunQuestionGalleryAdapter(context);
        galleryView.setAdapter(mFunQuestionAdapter);
        galleryView.setOnScrollStopListener(new GalleryView.OnScrollStopListener() {
            @Override
            public void onScrollStop(int position) {
                Logs.d("zxc", "ppppppp  "+position);
          /*      if (starQuestions == null) {
                    return;
                }
                LibraryInfo info = starQuestions.get(position);
                setQuestionBottomContent(info);*/
            }
        });
        List<BookBean> bookBeans = new ArrayList<>();
        for(int i = 0; i < 10;i++){
            bookBeans.add(new BookBean());
        }
        mFunQuestionAdapter.notifyDataSetChanged(bookBeans);

        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                galleryView.scrollToPosition(2);
            }
        },300);
    }

    @Override
    public void loadData() {


    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_gallery;
    }

}

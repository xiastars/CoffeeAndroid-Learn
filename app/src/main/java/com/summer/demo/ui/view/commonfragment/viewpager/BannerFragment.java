package com.summer.demo.ui.view.commonfragment.viewpager;


import android.view.View;
import android.view.ViewGroup;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.viewpager.CBViewHolderCreator;
import com.summer.demo.module.base.viewpager.ConvenientBanner;
import com.summer.demo.ui.view.commonfragment.viewpager.bean.BannerInfo;
import com.summer.demo.ui.view.commonfragment.viewpager.holder.BannerHolderView;
import com.summer.demo.ui.view.commonfragment.viewpager.holder.HomeBannerHolder;
import com.summer.demo.ui.view.commonfragment.viewpager.holder.HomeBannerHolder2;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 一般用来显示Banner广告
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/16 10:54
 */
public class BannerFragment extends BaseFragment {
    @BindView(R.id.cb_container)
    ConvenientBanner cbContainer;
    @BindView(R.id.cb_container2)
    ConvenientBanner cbContainer2;
    @BindView(R.id.cb_container3)
    ConvenientBanner cbContainer3;

    @Override
    protected void initView(View view) {
        initBannerView();
        List<BannerInfo> banners = new ArrayList<>();
        banners.add(new BannerInfo("http://pic1.win4000.com/wallpaper/b/57fc5075c59ea.jpg"));
        banners.add(new BannerInfo("http://pic1.win4000.com/wallpaper/b/57fc507be76aa.jpg"));
        banners.add(new BannerInfo("http://pic1.win4000.com/wallpaper/b/57fc5075c59ea.jpg"));
        banners.add(new BannerInfo("http://pic1.win4000.com/wallpaper/b/57fc507be76aa.jpg"));
        cbContainer.setPages(new CBViewHolderCreator<BannerHolderView>() {
            @Override
            public BannerHolderView createHolder() {
                return new HomeBannerHolder();
            }
        }, banners).setTextPageIndicator(new int[]{R.drawable.so_white_oval, R.drawable.so_red04_oval}).setOnItemClickListener(new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                BannerInfo banner = banners.get(position);
                //onBannerClick(banner, "top_banner");
            }
        });
        cbContainer2.setPages(new CBViewHolderCreator<BannerHolderView>() {
            @Override
            public BannerHolderView createHolder() {
                return new HomeBannerHolder();
            }
        }, banners).setPageIndicator(new int[]{R.drawable.so_white_oval, R.drawable.so_red04_oval}).setOnItemClickListener(new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                BannerInfo banner = banners.get(position);
                //onBannerClick(banner, "top_banner");
            }
        });

        cbContainer3.setPageTransformer(new FunPagerTransformer(context));
        cbContainer3.setPages(new CBViewHolderCreator<BannerHolderView>() {
            @Override
            public BannerHolderView createHolder() {
                return new HomeBannerHolder2();
            }
        }, banners).setPageIndicator(new int[]{R.drawable.so_white_oval, R.drawable.so_red04_oval}).setOnItemClickListener(new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                BannerInfo banner = banners.get(position);
                //onBannerClick(banner, "top_banner");
            }
        });
        //自动翻页
        cbContainer2.startTurning(2000);
    }

    private void initBannerView() {
        int height = (int) ((SUtils.screenWidth - SUtils.getDip(context, 30)) * 0.52f);
        ViewGroup.LayoutParams param = cbContainer.getLayoutParams();
       // param.height = height;
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_viewpager_ad;
    }

}

package com.summer.demo.ui.module.fragment.share;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;

import butterknife.OnClick;

/**
 * @Description: 分享示例
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/17 15:08
 */
public class ShareFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected void initView(View view) {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_share;
    }

    @OnClick({R.id.btn_share1,R.id.btn_share2,R.id.btn_share3,R.id.btn_share4,R.id.btn_share5})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share1:
                ShareHelper shareHelper = new ShareHelper(context,buildShareInfo());
                shareHelper.showDialog();
                break;
            case R.id.btn_share2:
                ShareRule shareRule = new ShareRule();
                //设置显示复制链接
                shareRule.setSupportShareLink(true);
                //设置不显示QQ分享
                shareRule.setSupportShareQQ(false);
                ShareHelper shareHelper2 = new ShareHelper(context,buildShareInfo(),shareRule);
                shareHelper2.showDialog();
                break;
            case R.id.btn_share3:
                shareRule = new ShareRule();
                //设置分享微信内容为小程序
                shareRule.setSupportShareWechatMiniApp(true);
                shareHelper2 = new ShareHelper(context,buildShareInfo(),shareRule);
                shareHelper2.showDialog();
                break;
            case R.id.btn_share4:

                ViewBitmapHelper viewBitmapHelper = new ViewBitmapHelper(mView);
                viewBitmapHelper.share(new OnReturnBitmapListener() {
                    @Override
                    public void onBitmap(String path) {
                        ShareRule shareRule3 = new ShareRule();
                        ShareInfo shareInfo = buildShareInfo();
                        shareInfo.setShareImg(path);
                        //设置分享朋友圈为大图
                        shareRule3.setSupportShareWechatCircleWithBigPhoto(true);
                        ShareHelper shareHelper3 = new ShareHelper(context,shareInfo,shareRule3);
                        shareHelper3.showDialog();
                    }
                });

                break;
            case R.id.btn_share5:
                viewBitmapHelper = new ViewBitmapHelper(mView);
                viewBitmapHelper.share(path -> {
                    ShareRule shareRule1 = new ShareRule();
                    //设置支持一键分享
                    shareRule1.setSupportShareSystem(true);
                    ShareInfo shareInfo = buildShareInfo();
                    //设置要分享的图片
                    shareInfo.setShareImg(path);
                    ShareHelper shareHelper1 = new ShareHelper(context,buildShareInfo(), shareRule1);
                    shareHelper1.showDialog();

                });

                break;
        }
    }

    private ShareInfo buildShareInfo(){
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setUrl("https://www.iconfont.cn/search/index?q=%E5%88%86%E4%BA%AB&page=1");
        shareInfo.setShareTitle("这是标题");//标题是必需的
        shareInfo.setShareContent("这是内容这是内容这是内容");
        shareInfo.setShareImg("http://pic1.win4000.com/wallpaper/2/57e09630c7d32.jpg");//QQ空间必须的
        shareInfo.setDrawableId(R.drawable.ic_view_grid);
        return shareInfo;
    }
}

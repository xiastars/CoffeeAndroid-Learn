package com.summer.demo.ui.module.fragment.socket;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.BaseResp;
import com.summer.demo.module.base.BaseFragment;

import butterknife.BindView;

/**
 * @Description: socket使用演示
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/31 17:36
 */
public class SocketFragment extends BaseFragment {
    @BindView(R.id.iv_nav)
    ImageView ivNav;
    @BindView(R.id.tv_hint_content)
    TextView tvHintContent;
    @BindView(R.id.tv_reload)
    TextView tvReload;

    @Override
    protected void initView(View view) {
        SocketHelper socketHelper = SocketHelper.getInstance();
        socketHelper.setLedInfo(new BaseResp());//不一定需要
        socketHelper.connect("127.0.0.1", 8080);

        tvHintContent.setText("Socket连接，重连，收发；看代码");
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }

}

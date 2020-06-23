package com.summer.demo.ui.module.fragment.pay;

import com.summer.demo.bean.BaseResp;

public class WXPayResp extends BaseResp {

    WXBean info;

    @Override
    public WXBean getInfo() {
        return info;
    }

    public void setInfo(WXBean info) {
        this.info = info;
    }

}

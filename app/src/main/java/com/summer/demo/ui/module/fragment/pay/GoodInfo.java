package com.summer.demo.ui.module.fragment.pay;

import java.io.Serializable;

/**
 * @Description: 支付的商品信息
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/18 14:11
 */
public class GoodInfo implements Serializable {

    long id;
    float price;
    String recharge;
    int xingValue;
    boolean isSelect = false;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }

    public int getXingValue() {
        return xingValue;
    }

    public void setXingValue(int xingValue) {
        this.xingValue = xingValue;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

}

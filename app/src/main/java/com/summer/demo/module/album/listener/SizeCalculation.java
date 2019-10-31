package com.summer.demo.module.album.listener;

import com.ghnor.flora.spec.calculation.Calculation;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/21 17:45
 */
public class SizeCalculation extends Calculation {

    @Override
    public int calculateInSampleSize(int srcWidth, int srcHeight) {

        return 10;
    }
}

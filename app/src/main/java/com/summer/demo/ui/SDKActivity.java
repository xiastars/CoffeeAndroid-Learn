package com.summer.demo.ui;

import com.summer.demo.R;
import com.summer.demo.ui.fragment.sdk.AlexaFragment;

/**
 * @Description: 有关sdk对接
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/20 16:07
 */
public class SDKActivity extends BaseGridListActivity {

    @Override
    protected void initData() {
        super.initData();
        int[] imgs = {R.drawable.alexa};
        setData(context.getResources().getStringArray(R.array.sdks), imgs);
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos){
            case 0:
                showFragment(new AlexaFragment());
                break;
        }
    }
}

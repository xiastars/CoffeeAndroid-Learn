package com.summer.demo.ui.fragment.sdk;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.fragment.sdk.easyar.GLView;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;
import cn.easyar.CameraDevice;
import cn.easyar.Engine;
import cn.easyar.ImageTracker;

/**
 * @Description: EasyAR https://help.easyar.cn
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/3 17:11
 */
public class EasyARFragment extends BaseFragment {
    GLView glView;

    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    @Override
    protected void initView(View view) {
        String key = "uBlhnbwKeYGkbNfIw9uMNbt5okeMqUpkw7lJ5ogrV7a8O1GriDZG5sd6Sq2cK0aljytyspQoHLWMdlGrkHoe5pA5QbCYKnmhhBFW5sdpHuaRMVGhkytXt99iab/fOkeqmTRXjZkrEP6mBR7mizlArZw2RrffYmnmnjdfqYg2W7CEem/o3yhepYk+XbaQKxD+pnpFrZM8XbOOeh7mkDlR5qB0EKmSPEeomCsQ/qZ6QaGTK1fqtDVTo5gMQKWeM1uqmnoe5o49XLeYdnGoki1Wlpg7XaOTMUatkjYQ6N8rV6qOPRyWmDtdtpkxXKPfdBC3mDZBodMXUK6YO0aQjzlRr5Q2VebRekGhkytX6q4tQKKcO1eQjzlRr5Q2VebRekGhkytX6q4oU7aOPWG0nCxbpZEVU7TfdBC3mDZBodMVXbCUN1yQjzlRr5Q2VebRekGhkytX6rk9XLeYC0KliTFTqLA5QubRekGhkytX6r4ZdpCPOVGvlDZV5qB0EKGFKFu2mAxbqZgLRqWQKBD+ky1eqNF6W7exN1GlkXoIopw0QaGAdEnmny1coJE9e6COegif3zxXqZJ2QbGQNVe20ztdqdMrR6mQPUCgmDVd5qB0ELKcKlulkyxB5scDEKeSNV+xkzFGvd8FHuaNNFOwmzdAqY56CJ/fOVygjzdboN8FHuaQN1axkT1B5scDELeYNkGh0xFfpZo9ZracO1mtkz8Q6N8rV6qOPRyHkTdHoK89UauaNluwlDdc5tF6QaGTK1fqrz1Rq488W6qaeh7mjj1ct5h2faaXPVGwqSpTp5YxXKPfdBC3mDZBodMLR7abOVGhqSpTp5YxXKPfdBC3mDZBodMLQqWPK1eXjTlGrZw0f6WNeh7mjj1ct5h2f6uJMV2qqSpTp5YxXKPfdBC3mDZBodMcV6qOPWG0nCxbpZEVU7TfdBC3mDZBodMbc4CpKlOnljFco98FHuaYIEKtjz1mrZA9YbCcNULmxzZHqJF0EK2OFF2nnDQQ/ps5XreYJR6/3zpHqpk0V42ZKxD+pnoQmdF6RKWPMVOqiSsQ/qZ6UauQNUeqlCxL5qB0ELSROUaikipft99iaeaUN0HmoHQQqZI8R6iYKxD+pnpBoZMrV+q0NVOjmAxApZ4zW6qaeh7mjj1ct5h2caiSLVaWmDtdo5MxRq2SNhDo3ytXqo49HJaYO122mTFco990ELeYNkGh0xdQrpg7RpCPOVGvlDZV5tF6QaGTK1fqri1Aopw7V5CPOVGvlDZV5tF6QaGTK1fqrihTto49YbScLFulkRVTtN90ELeYNkGh0xVdsJQ3XJCPOVGvlDZV5tF6QaGTK1fquT1ct5gLQqWJMVOosDlC5tF6QaGTK1fqvhl2kI85Ua+UNlXmoHQQoYUoW7aYDFupmAtGpZAoEP6TLV6o0Xpbt7E3UaWRegiinDRBoYAFT1gdEUlQPaERMptfWl/Pih/HZ/D+xoqMP0mEKmZDNWI+5SI9fAB+C2jnkyVPLixgJnqrqMkCvSNmrys5v5f+jDvn2ZHYhbSTBM65gLcXsh4p77GoCcPAvVqZ/FECWBlgwPjXIwKyG23PYQMzrn7gZz4J4ns9N3T7O0GgnYi8V1EI8GJgg2XD5JX9NgpAzvEvGannFGmfg0fG0dwOpjB806tGx6sxA/eXp6bufZ+TwBxY9TYzLsOWBe/Bz+80lPmZgqlgtiTOMMBAQdPVJvsYm0Nnc7hiBLFqrNEUIDjej2ohuoBYdBJuK6dHNHv6KgYZLI9sTD8NDzF8f3i4kP1YMsQ=";
        if (!Engine.initialize(activity, key)) {
            Logs.i("HelloAR", "Initialization Failed.");
            SUtils.makeToast(context, Engine.errorMessage());
            return;
        }

        if (!CameraDevice.isAvailable()) {
            SUtils.makeToast(context, "CameraDevice not available.");
            return;
        }
        if (!ImageTracker.isAvailable()) {
            SUtils.makeToast(context, "ImageTracker not available.");
            return;
        }
        glView = new GLView(context);
        rlContainer.addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_easyar;
    }

}

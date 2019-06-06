package com.summer.demo;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.summer.demo.anim.DragLayer;
import com.summer.demo.anim.DragRelativeLayout;
import com.summer.demo.base.BaseFragment;
import com.summer.helper.db.CommonService;
import com.summer.helper.utils.Logs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xiaqiliang on 2017年03月17日 09:48.
 */

public class AcAnim extends BaseFragment {

    CommonService service;
    @BindView(R.id.draglyer)
    DragLayer dragLayer;

    private void createLittle() {

    }

    @Override
    protected void initView(View view) {
        service = new CommonService(context);
        createLittle();
        DragRelativeLayout controlLayout = new DragRelativeLayout(context);
        Bitmap[] bitmaps = new Bitmap[126];
        List<byte[]> bitmappath = new ArrayList<byte[]>();
        for (int i = 1; i < 100; i++) {
            try {
                String name = (40000 + i)+"";
                InputStream stream = context.getResources().openRawResource("kaiche"+ File.separator + name + ".png");
                // byte[] data = new byte[stream.available()];
                // stream.read(data);
                // stream.close();
                // bitmappath.add(data);
                BitmapFactory.Options OPTIONS_DECODE = new BitmapFactory.Options();
                OPTIONS_DECODE.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeStream(stream.createInputStream(), null, OPTIONS_DECODE);
                bitmaps[i - 1] = bitmap;
                Logs.i("xia", "bitmap:" + i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        controlLayout.setLayoutPosition(500, 300);
        dragLayer.addView(controlLayout);
        controlLayout.circlePlay(bitmaps);
        for (int i = 0; i < 5; i++) {
            DragRelativeLayout layout = new DragRelativeLayout(context);
            layout.setLayoutPosition(100 * i, i * 30);
            dragLayer.addView(layout);
            layout.circlePlay(bitmaps);
        }
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_anim;
    }

}

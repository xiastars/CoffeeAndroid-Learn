package com.summer.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.summer.demo.anim.DragLayer;
import com.summer.demo.anim.DragRelativeLayout;
import com.summer.helper.db.CommonService;
import com.summer.helper.utils.Logs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaqiliang on 2017年03月17日 09:48.
 */

public class AcAnim extends Activity {

    DragLayer dragLayer;
    CommonService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        service = new CommonService(this);
        dragLayer = (DragLayer) findViewById(R.id.draglyer);
        createLittle();
    }

    private void createLittle() {
        DragRelativeLayout controlLayout = new DragRelativeLayout(this);
        Bitmap[] bitmaps = new Bitmap[126];
        List<byte[]> bitmappath = new ArrayList<byte[]>();
        for (int i = 1; i < 127; i++) {
            try {
                int name = 40000 + i;
                InputStream stream = this.getAssets().open("kaiche/" + name + ".png");
                // byte[] data = new byte[stream.available()];
                // stream.read(data);
                // stream.close();
                // bitmappath.add(data);
                BitmapFactory.Options OPTIONS_DECODE = new BitmapFactory.Options();
                OPTIONS_DECODE.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeStream(stream, null, OPTIONS_DECODE);
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
            DragRelativeLayout layout = new DragRelativeLayout(this);
            layout.setLayoutPosition(100 * i, i * 30);
            dragLayer.addView(layout);
            layout.circlePlay(bitmaps);
        }
    }
}

package com.summer.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.summer.demo.anim.DragRelativeLayout;
import com.summer.demo.base.BaseFragment;
import com.summer.demo.view.DragLayer;
import com.summer.helper.db.CommonService;
import com.summer.helper.db.DBType;
import com.summer.helper.db.SerializeUtil;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 帧动画演示
 * Created by xiaqiliang on 2017年03月17日 09:48.
 */

public class FrameAnimFragment extends BaseFragment {

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
        final Bitmap[] bitmaps = new Bitmap[126];
        final String fileName = "kaiche";
        final long time = System.currentTimeMillis();

        final List<byte[]> bits = (List<byte[]>) service.getListData(DBType.COMMON_DATAS, fileName);
        //如果本地已缓存，就从本地读取，会更快一些,存数据库比存SD卡快一些
        if (bits != null) {
            final int count = bits.size();
            final int d = count % 10 + 1;
            for (int i = 0; i < d; i++) {
                SThread.getIntances().submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            int x = d * 10 + i;
                            if (x >= count) {
                                Logs.t(time);
                                startPlay(bitmaps);
                                break;
                            }
                            byte[] arrays = bits.get(d * 10 + i);
                            Bitmap bitma = BitmapFactory.decodeByteArray(arrays, 0, arrays.length);
                            bitmaps[d * 10 + i] = bitma;
                        }
                    }
                });
            }
            Logs.t(time);
        } else {
            for (int i = 1; i < 127; i++) {
                try {
                    String name = (40000 + i) + "";
                    //读取asset里的文件
                    InputStream stream = context.getAssets().open(fileName + File.separator + name + ".png");
                    BitmapFactory.Options OPTIONS_DECODE = new BitmapFactory.Options();
                    OPTIONS_DECODE.inSampleSize = 2;
                    Bitmap bitmap = BitmapFactory.decodeStream(stream, null, OPTIONS_DECODE);
                    bitmaps[i - 1] = bitmap;

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            startPlay(bitmaps);
            //缓存到本地
            SThread.getIntances().submit(new Runnable() {
                @Override
                public void run() {
                    List<byte[]> bitmappath = new ArrayList<>();
                    for (int i = 0; i < bitmaps.length; i++) {
                        Bitmap bitmap = bitmaps[i];
                        Logs.i("bitmap:" + bitmap);
                        int size = bitmap.getWidth() * bitmap.getHeight() * 4;

                        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        try {
                            out.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        byte[] arrays = out.toByteArray();
                        bitmappath.add(arrays);
                    }
                    byte[] lastData = SerializeUtil.serializeObject(bitmappath);
                    service.insert(DBType.COMMON_DATAS, fileName, lastData);
                }
            });

            Logs.t(time);
        }


    }

    private void startPlay(Bitmap[] bitmaps) {
        for (int i = 0; i < 8; i++) {
            DragRelativeLayout layout = new DragRelativeLayout(context);
            layout.setLayoutPosition(40 * i, i * 180);
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

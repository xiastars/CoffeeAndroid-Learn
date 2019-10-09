package com.summer.demo.ui.module.fragment;

import android.graphics.Bitmap;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.anim.DragRelativeLayout;
import com.summer.demo.anim.FrameImgBean;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.view.DragLayer;
import com.summer.helper.db.CommonService;
import com.summer.helper.db.DBType;
import com.summer.helper.db.SerializeUtil;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SThread;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

    List<DragRelativeLayout> dragRelativeLayouts;

    private void createLittle() {

    }

    @Override
    protected void initView(View view) {
        service = new CommonService(context);
        createLittle();
        DragRelativeLayout controlLayout = new DragRelativeLayout(context);
        final String fileName = "kaiche";
        final long time = System.currentTimeMillis();
        List<FrameImgBean> bitmaps = new ArrayList<>();
        for (int i = 1; i < 127; i++) {
            String name = (40000 + i) + "";

            String lastName = fileName + File.separator + name + ".png";
            FrameImgBean frameImgBean = new FrameImgBean();
            frameImgBean.setImgName(lastName);
            frameImgBean.setImgType(2);
            bitmaps.add(frameImgBean);
        }
        for(int i = 0;i < 2;i++){
            DragRelativeLayout dragRelativeLayout = new DragRelativeLayout(context);
            dragRelativeLayout.setLayoutPosition(40+(i+100) ,  180+(i+200));
            dragLayer.addView(dragRelativeLayout);
            List<FrameImgBean> datas = new ArrayList<FrameImgBean>();
            datas.addAll(bitmaps);
            dragRelativeLayout.initBitmaps(datas);
            dragRelativeLayout.circlePlay();
        }


    }

    @Override
    public void onHide() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if(dragRelativeLayouts != null){
            for(int i =0; i < dragRelativeLayouts.size();i++){
                dragRelativeLayouts.get(i).stopPlay();
            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Logs.i("check:ONPAUSE");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logs.i("check:ONPAUSE");
    }



    /**
     * 缓存Bitmap的办法
     */
    private void cacheBitmap(final Bitmap[] bitmaps,final String fileName){
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

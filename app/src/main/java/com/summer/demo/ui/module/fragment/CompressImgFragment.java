package com.summer.demo.ui.module.fragment;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ghnor.flora.Flora;
import com.ghnor.flora.callback.Callback;
import com.ghnor.flora.spec.decoration.Decoration;
import com.summer.demo.R;
import com.summer.demo.module.album.listener.SizeCalculation;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.SelectPhotoHelper;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/21 17:30
 */
public class CompressImgFragment extends BaseFragment {
    SelectPhotoHelper selectPhotoHelper;

    long preSize = 0;
    @BindView(R.id.tv_pre)
    TextView tvPre;
    @BindView(R.id.iv_pre)
    ImageView ivPre;
    @BindView(R.id.tv_cur)
    TextView tvCur;
    @BindView(R.id.iv_cur)
    ImageView ivCur;

    @Override
    protected void initView(View view) {

        selectPhotoHelper = new SelectPhotoHelper(context, new OnReturnObjectClickListener() {
            @Override
            public void onClick(Object object) {
                final List<ImageItem> items = (List<ImageItem>) object;
                preSize = items.get(0).getSize();
                STextUtils.setNotEmptText(tvPre,"原图片大小："+preSize/1000+"kb");
                compressFile(items.get(0).getImagePath());
            }
        });
        selectPhotoHelper.enterToAlbumForOne();
    }


    private void compressFile(final String url) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logs.i("url:"+url);
                SUtils.setPic(ivPre,url);
            }
        });
        // 配置inSample和quality的算法，内置了一套基于Luban的压缩算法
        Flora.with().calculation(new SizeCalculation() {
                    @Override
                    public int calculateInSampleSize(int srcWidth, int srcHeight) {
                        return super.calculateInSampleSize(srcWidth, srcHeight);
                    }

                    @Override
                    public int calculateQuality(int srcWidth, int srcHeight, int targetWidth, int targetHeight) {
                        return super.calculateQuality(srcWidth, srcHeight, targetWidth, targetHeight);
                    }
                })
                // 对压缩后的图片做个性化地处理，如：添加水印
                .addDecoration(new Decoration() {
                    @Override
                    public Bitmap onDraw(Bitmap bitmap) {
                        return super.onDraw(bitmap);
                    }
                })
                // 配置Bitmap的色彩格式
                .bitmapConfig(Bitmap.Config.RGB_565)
                // 同时可进行的最大压缩任务数量
                .compressTaskNum(1)
                // 安全内存，设置为2，表示此次压缩任务需要的内存小于1/2可用内存才进行压缩任务
                .safeMemory(2)
                // 压缩完成的图片在磁盘的存储目录
                .diskDirectory(new File(SFileUtils.getImageViewDirectory()))
                .load(url)
                .compress(new Callback<String>() {
                    @Override
                    public void callback(final String s) {

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int size = (int) (new File(s).length() / 1024);
                                STextUtils.setNotEmptText(tvCur,"原图片大小："+size+"kb");
                                SUtils.setPic(ivCur,s);

                            }
                        });

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
        return R.layout.fragment_compress;
    }

}

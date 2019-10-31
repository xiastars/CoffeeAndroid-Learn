package com.summer.demo.ui.module.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.ghnor.flora.Flora;
import com.ghnor.flora.callback.Callback;
import com.ghnor.flora.spec.decoration.Decoration;
import com.summer.demo.R;
import com.summer.demo.module.album.listener.SizeCalculation;
import com.summer.demo.module.album.util.SelectPhotoHelper;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.listener.OnResponseListener;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SThread;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/21 17:30
 */
public class CompressImgFragment extends BaseFragment {
    SelectPhotoHelper selectPhotoHelper;

    @Override
    protected void initView(View view) {

        selectPhotoHelper = new SelectPhotoHelper(context, new OnResponseListener() {
            @Override
            public void succeed(final String url) {
               //compressFile(url);
                SThread.getIntances().submit(new Runnable() {
                    @Override
                    public void run() {
                 /*       String urlStr = "http://39.108.57.190:8080/zswyHMS/easyCtl/upload.do";
                        Map<String, String> textMap = new HashMap<String, String>();
                        textMap.put("sessionID", "96f8a076-fdc6-4a4c-9ebc-82fe32dc21cd");
                        textMap.put("ext","png");
                        Map<String, String> fileMap = new HashMap<String, String>();
                        fileMap.put("userfile", url);
                        String ret = formUpload(urlStr, textMap, fileMap);
                        Logs.i("rest::"+ret);*/
                        uploadMedia(context,"25ffbd05-1217-4d98-a368-951a09e17e34","http://39.108.57.190:8080/zswyHMS/easyCtl/upload.do",url);


                    }
                });

            }

            @Override
            public void failure() {

            }
        });
        selectPhotoHelper.enterToAlbum();
    }

    /**
     * 上传文件测试
     * @param context
     * @param session
     * @param uploadUrl
     * @param oldFilePath
     */
    public static void uploadMedia(Context context, String session, String uploadUrl, String oldFilePath){
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            // 允许Input、Output，不使用Cache
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            con.setConnectTimeout(50000);
            con.setReadTimeout(50000);
            // 设置传送的method=POST
            con.setRequestMethod("POST");
            //在一次TCP连接中可以持续发送多份数据而不会断开连接
            con.setRequestProperty("Connection", "close");
            //设置编码

            con.setRequestProperty("Charset", "UTF-8");
            //text/plain能上传纯文本文件的编码格式
            //con.setRequestProperty("Content-Type", "text/plain");
            con.setRequestProperty("Content-Type", "application/octet-stream");
            con.addRequestProperty("sessionID", session);
            con.addRequestProperty("ext", oldFilePath.endsWith(SFileUtils.FileType.FILE_MP4) ? "mp4" : "png");
            con.addRequestProperty("file","");
            // 设置DataOutputStream
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());

            // 取得文件的FileInputStream
            FileInputStream fStream = new FileInputStream(oldFilePath);
            // 设置每次写入1024bytes
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int length = -1;
            // 从文件读取数据至缓冲区
            while ((length = fStream.read(buffer)) != -1) {
                // 将资料写入DataOutputStream中
                ds.write(buffer, 0, length);
            }
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            String res = strBuf.toString();
            Logs.i("res:::"+res);
            ds.flush();
            fStream.close();
            ds.close();

            reader.close();
            reader = null;
            Logs.i("con.get:"+con);
            if(con.getResponseCode() == 200){
                Logs.i("con:"+con.getResponseMessage()+",,"+con.getContent()+",,");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 上传图片
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @return
     */
    public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", PostData.getUserAgent());
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" +  "application/octet-stream");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            Logs.i("res::"+res);
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }


    private void compressFile(String url) {
        Flora.with()
                // 配置inSample和quality的算法，内置了一套基于Luban的压缩算法
                .calculation(new SizeCalculation() {
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
            public void callback(String s) {
                Logs.i("fileSize::::" + new File(s).length() / 1024+",,,file:"+s);
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
        return R.layout.view_empty;
    }
}

package com.summer.demo.ui.module.fragment.socket;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.demo.bean.BaseResp;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SThread;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * socket连接
 */
public class SocketHelper {

    private Socket client;
    private InputStream inputStream;
    private BufferedReader br = null;

    private String connectIp;
    int connectPort;

    BaseResp ledInfo;//如果服务器登录需要相关信息验证的话，在连接前，将该信息传过来

    SocketResponseListener socketResponseListener;

    final int MSG_HEATBEAT = 0;//心跳
    final int MSG_RECONNECT = 1;//重连
    final int MSG_CHECK_CONNECT = 2;//检测连接状态
    final int MSG_LOGIN = 3;//登录

    int cmdNo = 450;//指令序号起始值

    public static SocketHelper ledSocket;
    MyHandler myHandler;

    boolean waitFinish = false;

    boolean isLogining = false;

    public static SocketHelper getInstance() {
        if (ledSocket == null) {
            ledSocket = new SocketHelper();

        }
        return ledSocket;
    }

    public SocketHelper() {
        myHandler = new MyHandler(this);
    }

    /**
     * 开始连接,关闭，连接，与收消息必须在一个线程里，否则难以维护，可能引发问题
     *
     * @param ip
     * @param port
     */
    public void connect(final String ip, final int port) {
        waitFinish = false;
        this.connectIp = ip;
        this.connectPort = port;
        SThread.getIntances().clear();
        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    close(10);
                    client = null;
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                client = new Socket();
                try {
                    client.connect(new InetSocketAddress(ip, port), 10000);
                    Thread.sleep(200);
                    if (client.isConnected()) {
                        //当前连接成功后，如果该服务器需要登录，则立即发送登录指令
                        myHandler.sendEmptyMessage(MSG_LOGIN);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Logs.i("e::" + e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logs.i("e::" + e);
                }

                startHeartBean();
                receiveData();
                while (client.isConnected()) {
                    receiveData();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public void reconnect() {
        myHandler.removeMessages(MSG_CHECK_CONNECT);
        cancelHeatBeat();
        Logs.i("正在重连");
        connect(connectIp, connectPort);
    }


    public void receiveData() {
        InputStreamReader isr = null;
        try {
            inputStream = client.getInputStream();
            isr = new InputStreamReader(inputStream);
            br = new BufferedReader(isr);
            String info = null;
            while ((info = br.readLine()) != null) {
                waitFinish = false;
                Logs.i("i::" + info);
                myHandler.removeMessages(MSG_CHECK_CONNECT);
                handleResponse(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 处理回复
     */
    private void handleResponse(final String content) {
        BaseResp baseResp = JSON.parseObject(content, BaseResp.class);
        String msg = baseResp.getMsg();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        if (jsonObject != null) {
            final String error = jsonObject.getString("ErrorMessage");
            if (!TextUtils.isEmpty(error)) {
                //收到错误消息，及相关处理
                return;
            }
        }
        int fun = baseResp.getFun();
        switch (fun) {
            case SocketFun.FUN_LOGIN:
                isLogining = true;
                break;
            case SocketFun.FUN_TEST:
                //根据服务器返回的值，做相关的操作
                if (socketResponseListener != null) {
                    socketResponseListener.response(SocketFun.FUN_TEST, 0);
                }
                break;
        }
    }

    public void sendData(final String data, boolean isLogin) {
        if (!isLogin && !isLogining) {
            //return;
        }
        myHandler.sendEmptyMessageDelayed(MSG_CHECK_CONNECT, 2000);
        if (waitFinish) {
            return;
        }
        cancelHeatBeat();
        startHeartBean();
        waitFinish = true;
        if (client.isClosed()) {
            Logs.i("设备已关闭");
            myHandler.sendEmptyMessageDelayed(MSG_RECONNECT, 1000);
            myHandler.removeMessages(MSG_CHECK_CONNECT);
            return;
        }
        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                PrintWriter pw = null;

                try {
                    os = client.getOutputStream();
                    os.write(data.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 构建心跳包
     *
     * @return
     */
    private String buildHeatbeat() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Fun", "02");
        jsonObject.put("CmdNo", getCmdNo());
        JSONObject dataObject = new JSONObject();
        dataObject.put("SessionID", PostData.TOKEN);
        jsonObject.put("Data", dataObject.toJSONString());
        return jsonObject.toJSONString();
    }

    /**
     * 发送心跳包
     */
    public void sendHeatBeat() {
        Logs.i("当前设备是否在连接中：" + client.isConnected());
        String data = buildHeatbeat();
        sendData(data, false);
    }

    public void setLedInfo(BaseResp ledInfo) {
        this.ledInfo = ledInfo;
    }

    /**
     * 发送登录
     */
    public void sendLogin() {
        if (client == null) {
            return;
        }
        String data = buildLogin();
        Logs.i("当前设备是否在连接中：" + client.isConnected());
        sendData(data, true);
    }

    /**
     * 构建登录包
     *
     * @return
     */
    private String buildLogin() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Fun", "01");
        jsonObject.put("CmdNo", getCmdNo());
        JSONObject dataObject = new JSONObject();
        dataObject.put("Type", 6);
        //其它需要验证的
        jsonObject.put("Data", dataObject.toJSONString());
        return jsonObject.toJSONString();
    }


    /**
     * 上传文件
     * 开启新端口
     *
     * @param path
     */
    private Socket uploadFileSocket;

    /**
     * 开启另外一个端口写文件，如果服务器是这么定的话
     *
     * @param path
     */
    public void uploadFile(final String path) {
        closeUpload();
        uploadFileSocket = new Socket();
        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {
                try {

                    uploadFileSocket.connect(new InetSocketAddress(connectIp, 6698), 2000);
                    SThread.getIntances().submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            startUploadFile(path);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    Logs.i("e::" + e);
                }
            }
        });
    }

    /**
     * 开始上传
     *
     * @param path
     */
    DataOutputStream dos = null;

    private void startUploadFile(final String path) {
        SThread.getIntances().submit(new Runnable() {
            @Override
            public void run() {
                DataInputStream dis = null;

                File file = new File(path);
                try {
                    dis = new DataInputStream(new FileInputStream(file));
                    dos = new DataOutputStream(uploadFileSocket.getOutputStream());
                    long count = dis.available();
                    while (count == 0) {
                        count = dis.available();
                    }
                    //注意大小端的问题
                    dos.write(STextUtils.getBytes(count));
                    dos.flush();
                    Thread.sleep(200);
                    dos.write(SFileUtils.getFileName(path).getBytes("UTF-8"));
                    dos.flush();
                    Thread.sleep(200);
                    int packetSize = 1024 * 1000;
                    byte[] buffer = new byte[packetSize];
                    int len = 0;
                    Logs.i("上传文件");
                    while ((len = dis.read(buffer)) != -1) {
                        Logs.i("上传文件");
                        dos.write(buffer, 0, len);
                        Thread.sleep(200);
                    }
                    dos.flush();

                    //开始读
                    InputStreamReader isr = null;
                    BufferedReader br = null;
                    InputStream inputStream;
                    if (uploadFileSocket.isClosed()) {
                        return;
                    }
                    inputStream = uploadFileSocket.getInputStream();
                    isr = new InputStreamReader(inputStream);
                    br = new BufferedReader(isr);
                    String info = null;
                    while ((info = br.readLine()) != null) {
                        Logs.i("info:" + info);
                        handleUploadResponse(info);
                        try {
                            closeUpload();
                            if (isr != null) {
                                isr.close();
                            }
                            if (br != null) {
                                br.close();
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            Logs.i("e::" + e1);
                        }

                    }
                } catch (FileNotFoundException e) {
                    Logs.i("e:" + e);
                    closeUpload();
                    e.printStackTrace();
                } catch (IOException e) {
                    Logs.i("e:" + e);
                    closeUpload();
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    Logs.i("e:" + e);
                    e.printStackTrace();
                } finally {
                    if (dis != null) {
                        try {
                            dis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }


    private void closeUpload() {
        Logs.i("close:");
        try {
            if (dos != null) {

                dos.close();
            }
            if (uploadFileSocket != null) {
                uploadFileSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传文件后收到的回复
     *
     * @param content
     */
    private void handleUploadResponse(String content) {

    }

    /**
     * 开始发送心跳
     */
    public void startHeartBean() {
        myHandler.sendEmptyMessageDelayed(MSG_HEATBEAT, 10000);
    }

    public void cancelHeatBeat() {
        myHandler.removeMessages(MSG_HEATBEAT);
    }

    /**
     * 处理MyHandler派发的消息
     *
     * @param position
     * @param object
     */
    public void handleMsg(int position, Object object) {
        switch (position) {
            case MSG_HEATBEAT:
                sendHeatBeat();
                startHeartBean();
                break;
            case MSG_CHECK_CONNECT:
                if (waitFinish) {
                    reconnect();
                }
                break;
            case MSG_LOGIN:
                sendLogin();
                break;
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<SocketHelper> mActivity;

        public MyHandler(SocketHelper activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SocketHelper activity = mActivity.get();
            if (null != activity) {
                activity.handleMsg(msg.what, msg.obj);

            }
        }
    }

    public void close(int where) {
        Logs.i("where<<" + where);
        try {
            if (inputStream != null) {
                //inputStream.close();
            }

            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitFinish = false;
        myHandler.removeMessages(MSG_CHECK_CONNECT);
        cancelHeatBeat();
        if (client != null) {
            Logs.i("closed");
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 范围450-500，自动循环，每次发送命令序号递增
     *
     * @return
     */
    private int getCmdNo() {
        cmdNo++;
        if (cmdNo > 500) {
            cmdNo = 450;
        }
        return cmdNo;
    }
}

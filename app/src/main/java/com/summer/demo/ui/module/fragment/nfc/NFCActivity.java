package com.summer.demo.ui.module.fragment.nfc;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.utils.ByteArrayTohexHepler;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/11 18:29
 */
public class NFCActivity extends BaseActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private EditText board;
    public static byte[] cmd_sens = {0x55,0x03,0x21};
    public static float[] temp_p = {(float) 32.5,(float) 32.5};
    public static float   temp_p_once = 0;
    public static int temp_cnt = 0;
    public static int temp_done = 0;
    public static int model = 0;
    public static int ee_set = 0;
    public static String set_string="                                                        ";
    private String get_string="                                                          ";
    //public static String data="reading";
    private int sysVersion;


    @Override
    protected void initData() {

        board = (EditText) findViewById(R.id.editText1);

        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null) {
            SUtils.makeToast(context,"设备不支持NFC！");
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            SUtils.makeToast(context,"请在系统设置中先启用NFC功能");
            return;
        }
        pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        /////////////////////////////////////////////////////////////////////////////////

        /////////////////////////////////////////////////////////////////////////////////button_initial
        Button btn_init=(Button)(findViewById(R.id.button_initial));
        btn_init.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String dummy = "                     ";
                ee_set = 1;
                get_string=board.getText().toString();
                set_string = get_string + dummy;
            }
        });
        /////////////////////////////////////////////////////////////////////////////////
        Button btn_body=(Button)(findViewById(R.id.button_body));
        btn_body .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                temp_p[0] = (float) 32.5;
                temp_cnt = 0;
                temp_done = 0;
                model = 0;
                cmd_sens[0] = 0x55;
                cmd_sens[1] = 0x03;
                cmd_sens[2] = 0x21;
            }
        });
        Button btn_room=(Button)(findViewById(R.id.button_room));
        btn_room.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                temp_p[0] = (float) 32.5;
                temp_cnt = 0;
                temp_done = 0;
                model = 1;
                cmd_sens[0] = 0x55;
                cmd_sens[1] = 0x03;
                cmd_sens[2] = 0x21;
            }
        });
        /////////////////////////////////////////////////////////////////////////////////
        //����ϵͳ�汾
        sysVersion = Build.VERSION.SDK_INT;
        if(sysVersion<20){
            Logs.i( "onNewIntent(getIntent())");
            onNewIntent(getIntent());
        }

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_nfc;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
            disableReaderMode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null){
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    CardReader.FILTERS, CardReader.TECHLISTS);
            enableReaderMode();
        }
        Logs.i( NfcA.class.getName() +",,nfcAdapter"+nfcAdapter);
        refreshStatus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int i=0;
        final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //获取 Tag 读取 ID 得到字节数组 转字符串 转码 得到卡号（默认16进制 这请自便）
        Tag tag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Long cardNo = Long.parseLong(ByteArrayTohexHepler.flipHexStr(ByteArrayTohexHepler.ByteArrayToHexString(tag.getId())), 16);
        // if (cardNo.toString().getBytes().length == 10) {
        String num = cardNo.toString();
        Logs.i("id是："+num);
        if( (p != null ) & CardReader.data_right==1)
        {
            temp_p_once = CardReader.load(p);
            if(model==0)
            {
                if(temp_p_once >=32.5 & temp_done ==0)
                {
                    if(temp_p_once >= temp_p[0]) temp_p[0] = temp_p_once;
                }
            }
            else
            {
                temp_p[0] = temp_p_once;
            }


            if( model==0 & temp_cnt > 20) temp_done =1;
            else                          temp_done =0;
            Logs.i( "temp_done = "+temp_done);
            Logs.i("temp_cnt  = "+temp_cnt);
            Logs.i( "temp_p    = "+temp_p[0]);

            if(model==0)
            {
                if(temp_p[0] < 32.5)      board.setText("���� L ");
                else if(temp_p[0] > 43.5) board.setText("���� H ");
                else
                {
                    if(temp_done==1) board.setText("���£�"+temp_p[0]+"�� Fine");
                    else             board.setText("���£�"+temp_p[0]+"�� ");
                }


            }
            else board.setText("���£�"+temp_p[temp_cnt]+"��");


            //==========================
            if(model==0)
            {
                if(temp_done ==0)
                {
                    if(temp_p_once >= temp_p[0]) temp_cnt =0;
                    else if(temp_cnt < 30)       temp_cnt =temp_cnt+1;
                }
            }
            else
            {
                temp_cnt = 0;
            }


        }
        else
        {
            board.setText("waiting for a card");
        }


/*
		//////////////////////////////////////////////////////////////
		board.setText( ( (p != null ) & CardReader.data_right==1)? ((model==0)?"���£�"+CardReader.load(p)+"��"
				                               						:"���£�"+CardReader.load(p)+"��")
				                               						: "waiting for a card");
		//////////////////////////////////////////////////////////////
*/
    }



    @TargetApi(20)
    private void enableReaderMode() {
        if(sysVersion<20)
            return;

        int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, new MyReaderCallback(), READER_FLAGS, null);
        }
    }


    @TargetApi(20)
    private void disableReaderMode() {
        if(sysVersion<20)
            return;

        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }

    @TargetApi(20)
    public class MyReaderCallback implements NfcAdapter.ReaderCallback {

        @Override
        public void onTagDiscovered(final Tag arg0) {
            NFCActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshStatus();
    }

    private void refreshStatus() {
        final String tip;
        if (nfcAdapter == null)
            tip = this.getResources().getString(R.string.tip_nfc_notfound);
        else if (nfcAdapter.isEnabled())
            tip = this.getResources().getString(R.string.tip_nfc_enabled);
        else
            tip = this.getResources().getString(R.string.tip_nfc_disabled);

        final StringBuilder s = new StringBuilder(
                this.getResources().getString(R.string.app_name));

        s.append("  --  ").append(tip);
        setTitle(s);
    }
}

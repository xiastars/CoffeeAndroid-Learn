package com.summer.demo.ui.module.fragment.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.summer.demo.R;
import com.summer.demo.dialog.BaseTipsDialog;
import com.summer.demo.dialog.LoadingDialog;
import com.summer.demo.module.base.dialog.BaseCheckDialog;
import com.summer.demo.module.base.dialog.BaseSureDialog;
import com.summer.demo.module.base.dialog.TipDialog;
import com.summer.demo.module.base.listener.DialogAfterClickListener;
import com.summer.demo.ui.BaseTitleListFragment;
import com.summer.demo.ui.module.fragment.dialog.datepicker.SDatePickerDialog;
import com.summer.helper.listener.OnReturnStringContentListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog的用法
 *
 * @author Administrator
 */
public class MyDialogFragment extends BaseTitleListFragment implements View.OnClickListener {
    Dialog dialog;

    @Override
    protected List<String> setData() {

        List<String> datas = new ArrayList<>();
        datas.add("加载用的dialog示例1");
        datas.add("加载用的dialog示例2");
        datas.add("自定义一个Dialog,常用于提示");
        datas.add("自定义选项，从底部出来");
        datas.add("需要用户决定的选择框");
        datas.add("仅一个按钮，给用户提示，没有操作");
        datas.add("纯提示加载");
        datas.add("一个空的，全屏对话框展示");
        datas.add("下载进度对话框");
        datas.add("时间选择框");
        return datas;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case 0:

                break;
            case 1:
                //通常为了方便，我们把加载的Dialog封装出来
                LoadingDialog dialog1 = new LoadingDialog(context);
                dialog1.startLoading();
                break;
            case 2:
                //弹出提示dialog，这是经常使用的功能,这里封装了一个简单的，传入想要提示的内容
                BaseTipsDialog baseTipsDialog = new BaseTipsDialog(context, "没有网络哦!", new BaseTipsDialog.DialogAfterClickListener() {
                    @Override
                    public void onSure() {
                        SUtils.makeToast(context, "去设置里设置网络吧");
                    }

                    @Override
                    public void onCancel() {
                        SUtils.makeToast(context, "算了");
                    }
                });
                baseTipsDialog.show();
                break;
            case 3:
                List<GroupManageInfo> infos = new ArrayList<>();
                infos.add(new GroupManageInfo());
                infos.add(new GroupManageInfo());
                ManageUserDialog manageUserDialog = new ManageUserDialog(context, infos, new OnSimpleClickListener() {
                    @Override
                    public void onClick(int position) {

                    }
                });
                manageUserDialog.show();
                break;
            case 4:
                BaseCheckDialog baseCheckDialog = new BaseCheckDialog(context, "这是显示内容,传入指定时间，可以倒计时，并在倒计时结束后关闭", new DialogAfterClickListener() {
                    @Override
                    public void onSure() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                baseCheckDialog.setAutoClose(10);
                baseCheckDialog.setTitleContent("这是标题");
                baseCheckDialog.setOkContent("确定啦");
                baseCheckDialog.setCancelContent("左边啦");
                baseCheckDialog.show();

                break;
            case 5:
                BaseSureDialog baseSureDialog = new BaseSureDialog(context, "这是显示内容", () -> {

                });
                baseSureDialog.show();
                break;
            case 6:
                TipDialog tipDialog = new TipDialog(context);
                tipDialog.setLoadContent("这是加载内容");
                tipDialog.show();
                break;
            case 7:
                FullWidthDemoDialog fullWidthDemoDialog = new FullWidthDemoDialog(context);
                fullWidthDemoDialog.show();
                break;
            case 8:
                final DownloadingDialog dialog = new DownloadingDialog(context);
                dialog.show();

                SThread.getIntances().submit(new Runnable() {
                    @Override
                    public void run() {
                        boolean startDownload = true;
                        int index = 0;
                        while (startDownload) {
                            index++;
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            final int finalIndex = index;
                            myHandlder.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.setPercent(finalIndex);
                                }
                            });
                            if (index == 100) {
                                dialog.cancelDialog();
                                startDownload = false;
                            }
                        }
                    }
                });
                break;
            case 9:
                SDatePickerDialog datePickerDialog = new SDatePickerDialog(context, new OnReturnStringContentListener() {
                    @Override
                    public void returnContent(String content) {

                    }
                });
                datePickerDialog.setDefaultDate("1989-07-12");
                datePickerDialog.show();
                //原生的
                DatePickerDialog datePickerDialog1 = new DatePickerDialog(context);
                //datePickerDialog1.show();
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 长按一个按钮，显示一个Dialog在这个按钮正上方
     */
    private void showPupDialog(float left, float top, View view) {
        if (dialog != null) setCancelDialog();
        dialog = new Dialog(context, R.style.dialog_pup);
        dialog.setContentView(R.layout.dialog_pup);
        Window window = dialog.getWindow();
        //设置Dialog动画
        window.setWindowAnimations(R.anim.scale_with_alpha);
        window.setGravity(Gravity.BOTTOM);
        ImageView ivdelte = (ImageView) dialog.findViewById(R.id.iv_delete);
        SUtils.clickTransColor(ivdelte);
        int dialogWidth = (int) context.getResources().getDimension(R.dimen.size_80);
        left = left + (view.getWidth() - dialogWidth) / 2;
        top = top - dialogWidth * 2;
        dialog.findViewById(R.id.ll_parent).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setCancelDialog();
            }
        });
        ivdelte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setCancelDialog();
            }
        });
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ivdelte.setLayoutParams(mParams);
        mParams.leftMargin = (int) left;
        mParams.topMargin = (int) top;
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void setCancelDialog() {
        if (null != dialog) {
            dialog.cancel();
            dialog = null;
        }
    }

}

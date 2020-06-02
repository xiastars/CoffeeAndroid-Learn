package com.summer.helper.permission;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.malata.summer.helper.R;
import com.summer.helper.dialog.BaseCenterDialog;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 权限提示，部分权限的提示文案需要补充
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/19 15:42
 */
public class PermissionDialog extends BaseCenterDialog {

    TextView tvTitle;
    LinearLayout llContainer;
    TextView tvProtocal;
    AppCompatCheckBox ckProtocal;
    List<PermissionBean> permissionBeans;

    boolean stopCheckState = true;//是否是检查状态

    public PermissionDialog(@NonNull Context context, String... permissions) {
        super(context);
        permissionBeans = new ArrayList<>();
        for (String p : permissions) {
            PermissionBean bean = new PermissionBean();
            bean.setChecked(false);
            bean.setName(p);
            permissionBeans.add(bean);
        }
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_permission;
    }

    @Override
    public void initView(View view) {
        setCanceledOnTouchOutside(false);
        ckProtocal = view.findViewById(R.id.ck_protocal);
        tvTitle = view.findViewById(R.id.tv_title);
        llContainer = view.findViewById(R.id.ll_container);
        tvProtocal = view.findViewById(R.id.tv_protocol);
        myHandler.sendEmptyMessageDelayed(0, 1000);
        myHandler.postDelayed(() -> addPermissionView(), 400);
        ckProtocal.setOnCheckedChangeListener(((buttonView, isChecked) -> {

        }));
        ckProtocal.setOnCheckedChangeListener((buttonView, isChecked) -> SUtils.saveBooleanData(context, "protocal_checked", isChecked));
        tvProtocal.setOnClickListener(v -> {
            context.sendBroadcast(new Intent(("VIEW_PROTOCAL")));
            //WebContainerActivity.show(context, "http://www.balanxems.com/protocol/","用户协议");
        });

    }

    private void addPermissionView() {
        for (PermissionBean permissions : permissionBeans) {
            Switch switchP = addPermissionView(permissions.getName());
            permissions.setSwView(switchP);
        }
        notifyPermissionSwitch();
    }

    private Switch addPermissionView(final String permission) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_permission, null);
        llContainer.addView(view);
        TextView tvTitle = view.findViewById(R.id.tv_permission);
        tvTitle.setText(findTitleByPermission(permission));
        TextView tvDetail = view.findViewById(R.id.tv_detail);
        tvDetail.setText(findContentByPermission(permission));
        final Switch vSwitch = view.findViewById(R.id.view_switch);
        vSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Intent gpsIntent = new Intent();
                gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
                gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
                gpsIntent.setData(Uri.parse("custom:3"));
                try {
                    PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }

            AndPermission.with(context)
                    .permission(permission)
                    //.rationale(rationale)//大家讲道理
                    .onGranted(permissions -> {
                        Logs.i("打开权限" + permissions);
                        notifyPermissionSwitch();
                    })
                    .onDenied(permissions -> {
                        notifyPermissionSwitch();
                        Logs.i("打开权限" + permissions);
                    })
                    .start();
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, 123);
        });
        return vSwitch;

    }

    /**
     * 获取显示的标题，没有的权限需要补充，要不然报错
     *
     * @param permission
     * @return
     */
    private int findTitleByPermission(String permission) {
        switch (permission) {
            case Permission.ACCESS_COARSE_LOCATION:
                return R.string.permission_location_detail;
            case Permission.WRITE_EXTERNAL_STORAGE:
                return R.string.permission_name_write_storage;
            case Permission.CAMERA:
                return R.string.permission_name_camera;
        }
        return 0;
    }

    /**
     * 获取显示的内容，没有的权限需要补充，要不然报错
     *
     * @param permission
     * @return
     */
    private int findContentByPermission(String permission) {
        switch (permission) {
            case Permission.ACCESS_COARSE_LOCATION:
                return R.string.permission_name_location;
            case Permission.WRITE_EXTERNAL_STORAGE:
                return R.string.permission_storage_detail;
            case Permission.CAMERA:
                return R.string.permission_camera_detail;
        }
        return 0;
    }

    /**
     * 更新Switch状态
     */
    private void notifyPermissionSwitch() {
        boolean isAllChecked = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (PermissionBean p : permissionBeans) {
                if (ContextCompat.checkSelfPermission(context,
                        p.getName()) != PackageManager.PERMISSION_GRANTED) {
                    p.getSwView().setChecked(false);
                    isAllChecked = false;
                } else {
                    p.getSwView().setChecked(true);
                }
            }

        }
        if (isAllChecked) {
            cancelDialog();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        stopCheckState = hasFocus;
    }


}

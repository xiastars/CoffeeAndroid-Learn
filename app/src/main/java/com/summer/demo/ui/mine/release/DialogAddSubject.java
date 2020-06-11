package com.summer.demo.ui.mine.release;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.helper.EasyHttpHelper;
import com.summer.demo.ui.mine.release.bean.AddSubjectTopInfo;
import com.summer.helper.dialog.BaseCenterDialog;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

public class DialogAddSubject extends BaseCenterDialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_content)
    EditText tvContent;
    @BindView(R.id.ll_layout)
    LinearLayout llLayout;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.ll_cancel)
    LinearLayout llCancel;
    @BindView(R.id.ll_sure)
    LinearLayout llSure;
    @BindView(R.id.iamfather)
    LinearLayout iamfather;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    boolean isSureable;
    String groupId;

    OnReturnObjectClickListener onReturnObjectClickListener;

    public DialogAddSubject(@NonNull Context context, String groupId) {
        super(context);
        this.groupId = groupId;
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_add_subject;
    }

    @Override
    public void initView(View view) {
        tvContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isSureable = s.length() > 0;
                tvSure.setTextColor(isSureable ? getResourceColor(R.color.blue_56) : getResourceColor(R.color.grey_c5));
            }
        });
        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });

        llSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSureable) {
                    return;
                }
                EasyHttpHelper easyHttpHelper = new EasyHttpHelper(context);
                easyHttpHelper.startEasyPost(EasyHttpHelper.CREAT_SUBJECT, "groups/hash_tags",
                        "title", tvContent.getText().toString(), AddSubjectTopInfo.class);
                easyHttpHelper.setOnEasyHttpResultListener(new EasyHttpHelper.OnEasyHttpResultListener() {
                    @Override
                    public void onResult(int requestCode, Object obj, boolean succeed) {
                        if (succeed && requestCode == EasyHttpHelper.CREAT_SUBJECT) {
                            if (onReturnObjectClickListener != null) {
                                AddSubjectTopInfo info = (AddSubjectTopInfo) obj;
                                onReturnObjectClickListener.onClick(info.getHash_tag());
                            }
                            cancelDialog();
                        }
                    }
                });
            }
        });
        tvContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                SUtils.showSoftInpuFromWindow(tvContent);
            }
        },300);
    }

    public void setOnReturnObjectClickListener(OnReturnObjectClickListener onReturnObjectClickListener) {
        this.onReturnObjectClickListener = onReturnObjectClickListener;
    }
}

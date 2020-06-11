package com.summer.demo.ui.view.commonfragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.STextUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/20 17:51
 */
public class EditTextFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.edt_content)
    EditText edtContent;
    @BindView(R.id.btn_change_input)
    Button btnChangeInput;

    @Override
    protected void initView(View view) {
        edtContent.setInputType(EditorInfo.TYPE_CLASS_TEXT |  InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Logs.i("beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logs.i("onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Logs.i("afterTextChanged");
            }
        });
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_view_edit;
    }

    @OnClick({R.id.btn_change_input})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_change_input:
                Logs.i(".."+(EditorInfo.TYPE_CLASS_TEXT |  InputType.TYPE_TEXT_VARIATION_PASSWORD));
                if(edtContent.getInputType() ==  (EditorInfo.TYPE_CLASS_TEXT |  InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                    //设置密码可见

                    //edtContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtContent.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //光标置后
                    STextUtils.setSelection(edtContent);
                    btnChangeInput.setText("隐藏密码");
                    Logs.i((EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)+"");
                }else{
                    //设置密码不可见
                    //edtContent.setTransformationMethod( PasswordTransformationMethod.getInstance());
                    edtContent.setInputType(EditorInfo.TYPE_CLASS_TEXT |  InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnChangeInput.setText("显示密码");
                    //光标置后
                    STextUtils.setSelection(edtContent);
                }
                break;
        }
    }
}

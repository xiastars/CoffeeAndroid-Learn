package com.summer.demo.ui.module.comment;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.bean.UserInfo;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.view.CommonSureView5;
import com.summer.helper.server.PostData;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 聊天界面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/11 14:15
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.sv_container)
    NRecycleView svContainer;
    @BindView(R.id.btn_send)
    CommonSureView5 btnSend;
    @BindView(R.id.edt_comment)
    EditText edtComment;
    @BindView(R.id.rl_edit_parent)
    RelativeLayout rlEditParent;
    @BindView(R.id.ll_parent)
    RelativeLayout llParent;
    String userId = "";
    String groupId;

    ChatAdapter chatAdapter;

    final int REQUEST_CHAT = 0;

    @Override
    protected int setContentView() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView(View view) {
        userId = "1001";
        groupId = "1002";
        Logs.i("---------");
        setRecycleView();
    }

    public void setRecycleView() {
        svContainer.setList();
        // 上拉自动加载
        svContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView nvContainerView, int dx, int dy) {
                super.onScrolled(nvContainerView, dx, dy);

                LinearLayoutManager manager = (LinearLayoutManager) nvContainerView.getLayoutManager();
                // last >= totalCount - 2表示剩余2个item是自动加载，可自己设置
                // dy>0表示向下滑动
                if (!ViewCompat.canScrollVertically(svContainer, -1) && dy < 0) {
                    pageIndex++;
                    loadData();
                }
            }
        });
        initEditView();
        loadData();
    }

    int preTop;

    private void initEditView() {
        edtComment.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (preTop == 0) {
                    preTop = top;

                } else if (preTop < top) {

                } else {
                    scollToBottom();
                }
            }
        });
        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                btnSend.changeStyle(content.trim().length() > 0);
            }
        });
        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                SUtils.showSoftInpuFromWindow(edtComment);
            }
        }, 300);
    }

    @Override
    public void loadData() {
        Logs.i("lastID:" + lastId);
        if (pageIndex > 0 && TextUtils.isEmpty(lastId)) {
            return;
        }
        SummerParameter parameter = PostData.getPostParameters(context);
        parameter.put("group_id", groupId);
        parameter.put("limit", 20);
        parameter.put("message_id", lastId);
        parameter.putLog("聊天");
        parameter.setShowVirtualData();
        getData(REQUEST_CHAT, ChatTopInfo.class, parameter, "dynamics/chats/users/" + userId);
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        switch (requestCode) {
            case REQUEST_CHAT:
                ChatTopInfo chatTopInfo = (ChatTopInfo) obj;
                Logs.i("----------"+chatTopInfo);
                if (chatAdapter == null) {
                    UserInfo receiver = chatTopInfo.getReceiver();
                    if (receiver != null) {
                        chatAdapter = new ChatAdapter(context, chatTopInfo.getSender(), chatTopInfo.getReceiver());
                        svContainer.setAdapter(chatAdapter);
                    }
                }
                List<ChatInfo> newDatas = chatTopInfo.getList();

                if (!SUtils.isEmptyArrays(newDatas) && newDatas.size() <= 20) {
                    lastId = newDatas.get(0).getId();
                }
                int size = newDatas.size();
                if (pageIndex > 0) {
                    List<ChatInfo> infos = (List<ChatInfo>) chatAdapter.items;
                    if (infos == null) {
                        infos = new ArrayList<>();
                    }
                    if (newDatas != null && pageIndex > 0) {
                        for (int i = 0; i < newDatas.size(); i++) {
                            infos.add(0, newDatas.get(newDatas.size() - i - 1));
                        }
                        LinearLayoutManager manager = (LinearLayoutManager) svContainer.getLayoutManager();
                        int last = manager.findLastCompletelyVisibleItemPosition();
                        chatAdapter.notifyDataChanged(infos);
                        svContainer.scrollToPosition(size+last);
                    }

                } else {
                    chatAdapter.notifyDataChanged(newDatas);
                }
                break;
        }
        if (requestCode >= 10) {
            //cancelLoading();
            ChatInfo info = findChatInfoByCode(requestCode);
            info.setRequestCode(0);
            info.setSendError(false);
            chatAdapter.notifyDataSetChanged();
        }
        if (pageIndex == 0) {
            scollToBottom();
        }
    }

    @Override
    protected void dealErrors(int requstCode, String requestType, String errString) {
        super.dealErrors(requstCode, requestType, errString);
        //cancelLoading();
        if(requestType != null && requestType.equals("40306")){
     
            return;
        }
        if (requstCode >= 10) {
            ChatInfo info = findChatInfoByCode(requstCode);
            info.setSendError(true);
            info.setRequestCode(0);
            chatAdapter.notifyDataSetChanged();
        }
    }


    @OnClick(R.id.btn_send)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                sendChatMsg(edtComment.getText().toString());
                edtComment.setText("");
                btnSend.changeStyle(false);
                //SUtils.hideSoftInpuFromWindow(edtComment);
                //showLoading();
                break;
        }
    }

    private ChatInfo findChatInfoByCode(int requestCode) {
        List<ChatInfo> infos = (List<ChatInfo>) chatAdapter.items;
        if (infos == null) {
            return null;
        }
        for (ChatInfo info : infos) {
            if (info.getRequestCode() == requestCode) {
                return info;
            }
        }
        return null;

    }

    /**
     * 发布私聊
     *
     * @param s
     */
    int reqeustCommentCode = 10;

    public void sendChatMsg(String s) {
        if(chatAdapter == null){
            SUtils.makeToast(context,"初始化失败");
            return;
        }
        reqeustCommentCode++;
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setText(s);
        chatInfo.setType(1);
        UserInfo UserInfo = new UserInfo();
        UserInfo.setId("mine");
        chatInfo.setUser(UserInfo);
        chatInfo.setSend_time(System.currentTimeMillis() / 1000);
        chatInfo.setRequestCode(reqeustCommentCode);
        List<ChatInfo> infos = (List<ChatInfo>) chatAdapter.items;
        if (infos == null) {
            infos = new ArrayList<>();
        }
        infos.add(chatInfo);
        chatAdapter.notifyDataSetChanged();
        scollToBottom();
        SummerParameter parameter = PostData.getPostParameters(context);
        parameter.put("flag", System.currentTimeMillis());
        parameter.put("text", s);
        parameter.put("send_time", System.currentTimeMillis());
        parameter.put("group_id", groupId);
        parameter.putLog("发送聊天");
        getData(reqeustCommentCode, ChatTopInfo.class, parameter, "dynamics/chats/users/" + userId);
    }

    private void scollToBottom(int position) {
        if (chatAdapter == null || chatAdapter.items == null) {
            return;
        }
        List<ChatInfo> infos = (List<ChatInfo>) chatAdapter.items;
        if (infos == null) {
            infos = new ArrayList<>();
        }
        svContainer.scrollToPosition(infos.size() - 20 * pageIndex);

    }

    private void scollToBottom() {
        if (chatAdapter == null || chatAdapter.items == null) {
            return;
        }
        List<ChatInfo> infos = (List<ChatInfo>) chatAdapter.items;
        if (infos == null) {
            infos = new ArrayList<>();
        }
        svContainer.scrollToPosition(infos.size() - 1);

    }
}

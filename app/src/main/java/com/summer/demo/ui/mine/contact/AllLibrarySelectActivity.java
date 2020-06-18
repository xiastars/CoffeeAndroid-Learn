package com.summer.demo.ui.mine.contact;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.ui.mine.contact.bean.AskUserInfo;
import com.summer.demo.ui.mine.contact.bean.LibraryInfo;
import com.summer.demo.ui.mine.contact.view.LetterView;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.NRecycleView;

import java.util.List;

/**
 * 全部明星
 * Created by xiastars on 2017/11/8.
 */

public class AllLibrarySelectActivity extends BaseActivity implements View.OnClickListener {
    List<LibraryInfo> items;
    AskUserInfo userInfo;

    final int REQUEST_ALL_LIBRARY = 2;//全部明星
    NRecycleView nvContainer;
    LetterView letterView;
    AllLibraryAdapter adapter;
    long curUserId;

    @Override
    protected int setTitleId() {
        return R.string.title_choose_library;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_all_library_select;
    }

    @Override
    protected void initData() {
        nvContainer = (NRecycleView) findViewById(R.id.nv_container);
        letterView = (LetterView) findViewById(R.id.letter_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = getAdapter();
        nvContainer.setLayoutManager(layoutManager);
        nvContainer.setAdapter(adapter);

        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                layoutManager.scrollToPositionWithOffset(adapter.getScrollPosition(character), 0);
            }

            @Override
            public void clickArrow() {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });
        loadData();
    }

    protected AllLibraryAdapter getAdapter() {
        AllLibrarySelectAdapter adapter = new AllLibrarySelectAdapter(this, new OnReturnObjectClickListener() {
            @Override
            public void onClick(Object object) {
                Intent intent = new Intent();
                intent.putExtra(JumpTo.TYPE_OBJECT, (LibraryInfo) object);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        return adapter;
    }

    @Override
    public void loadData() {
        requestAllLibrary();
    }

    private void requestAllLibrary() {
        SummerParameter parameter = new SummerParameter();
        parameter.setShowVirtualData();
        parameter.put("count", 1000);
        parameter.putLog("联系人");
        getData(REQUEST_ALL_LIBRARY, LibraryInfo.class, parameter, "");
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {
        switch (requestType) {
            case REQUEST_ALL_LIBRARY:
                List<LibraryInfo> infos = (List<LibraryInfo>) obj;
                adapter.notifyDataChanged(infos);
                break;
        }

    }

    @Override
    public void onClick(View v) {

    }
}

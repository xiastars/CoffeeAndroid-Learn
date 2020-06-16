package com.summer.demo.ui.view.commonfragment.recyclerview;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.summer.demo.ui.BaseTitleListFragment;
import com.summer.demo.ui.FragmentContainerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView的用法
 *
 * @author xiastars
 */
public class RecyclerViewFragment extends BaseTitleListFragment implements View.OnClickListener {

    FragmentContainerActivity activity;
    final int POS_LIST = 0;
    final int POS_List_Refresh = 1;
    final int POS_GRID = 2;
    final int POS_Banner = 3;
    final int PS_LIST_HEADER = 4;

    @Override
    protected List<String> setData() {
        activity = (FragmentContainerActivity) getActivity();
        List<String> datas = new ArrayList<>();
        datas.add("普通的ListView样式");
        datas.add("可刷新的ListView样式");

        datas.add("普通的GridView样式");
        datas.add("可刷新的GridView样式");
        datas.add("RecyclerView加Header");
        return datas;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case POS_LIST:
                showFragment(new CommonListFragment());
                break;
            case POS_List_Refresh:
                showFragment(new ListRecyclerFragment());
                break;
            case POS_GRID:
                showFragment(new CommonGridFragment());
                break;
            case POS_Banner:
                showFragment(new GridRecyclerFragment());
                break;
            case PS_LIST_HEADER:
                showFragment(new HeaderRecyclerViewFragment());
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }


}

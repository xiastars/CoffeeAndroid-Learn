package com.summer.demo.ui.mine.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.ui.mine.contact.bean.LibraryInfo;
import com.summer.demo.ui.mine.contact.bean.LibrarySelectInfo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.SViewUtils;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AllLibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<LibraryInfo> preItems; // 联系人名称字符串数组
    private List<String> mContactList; // 联系人名称List（转换成拼音）
    private List<LibrarySelectInfo> resultList; // 最终结果（包含分组的字母）
    private List<String> characterList; // 字母List

    long curUserId;

    public enum ITEM_TYPE {
        ITEM_TYPE_CHARACTER,
        ITEM_TYPE_CONTACT
    }

    public AllLibraryAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);

    }

    private void handleContact() {
        if(preItems == null){
            return;
        }
        mContactList = new ArrayList<>();
        String charactor = "";
        resultList = new ArrayList<>();
        characterList = new ArrayList<>();
        List<LibraryInfo> smallLibrary = null;
        LibrarySelectInfo selectInfo;
        Collections.sort(preItems, new LibraryComarator());
        for (int i = 0; i < preItems.size(); i++) {
            LibraryInfo info = preItems.get(i);
            String pinyin = STextUtils.getPinYin(info.getRepositoryName());
            String firstLetter = (pinyin.charAt(0) + "").toUpperCase(Locale.ENGLISH);
            Logs.i("info.get:" + info.getRepositoryName() + ",,," + pinyin);
            if (!charactor.equals(firstLetter)) {
                charactor = firstLetter;
                //添加拼音序号
                resultList.add(new LibrarySelectInfo(firstLetter, AllLibraryAdapter.ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()));
                selectInfo = new LibrarySelectInfo();
                smallLibrary = new ArrayList<>();
                selectInfo.setInfos(smallLibrary);
                selectInfo.setCharacter(firstLetter);
                selectInfo.setType(AllLibraryAdapter.ITEM_TYPE.ITEM_TYPE_CONTACT.ordinal());
                smallLibrary.add(info);
                resultList.add(selectInfo);
            } else {
                smallLibrary.add(info);
            }
        }
        Collections.sort(resultList, new ContactComparator());
        notifyDataSetChanged();
    }

    class LibraryComarator implements Comparator<LibraryInfo> {

        @Override
        public int compare(LibraryInfo info1, LibraryInfo info2) {
            String o1 = info1.getRepositoryName();
            String o2 = info2.getRepositoryName();
            String pinyin1 = STextUtils.getPinYin(o1);
            String pinyin2 = STextUtils.getPinYin(o2);
            int c1 = (pinyin1.charAt(0) + "").toUpperCase().hashCode();
            int c2 = (pinyin2.charAt(0) + "").toUpperCase().hashCode();

            boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
            boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
            if (c1Flag && !c2Flag) {
                return 1;
            } else if (!c1Flag && c2Flag) {
                return -1;
            }

            return c1 - c2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AllLibraryAdapter.ITEM_TYPE.ITEM_TYPE_CHARACTER.ordinal()) {
            return new CharacterHolder(mLayoutInflater.inflate(R.layout.item_contact, parent, false));
        } else {
            return new ContactHolder(mLayoutInflater.inflate(R.layout.view_library_select, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CharacterHolder) {
            ((CharacterHolder) holder).mTextView.setText(resultList.get(position).getCharacter());
        } else if (holder instanceof ContactHolder) {
            //((ContactHolder) holder).mTextView.setText(resultList.get(position).getCharacter());
            ContactHolder hd = (ContactHolder) holder;
            handleAllLibaryView(resultList.get(position).getInfos(), hd.llContainer);
        }
    }

    protected void handleAllLibaryView(List<LibraryInfo> infos, LinearLayout llContainer) {
        if(infos == null){
            return;
        }
        if (SUtils.isEmptyArrays(infos)) {
            return;
        }
        llContainer.removeAllViews();
        int count = infos.size();
        int dCount = count / 3;
        int index = 0;
        if (count <= 3) {
            createAllLibraryView(infos, llContainer);
        } else {
            if (count % 3 > 0) {
                dCount++;
            }
            for (int i = 0; i < dCount; i++) {
                int end = index + 3;
                if (end > count) {
                    end = count;
                }
                List<LibraryInfo> items = infos.subList(index, end);
                index = end++;
                createAllLibraryView(items, llContainer);

            }
        }

    }

    List<LinearLayout> askViews = new ArrayList<>();

    private void createAllLibraryView(List<LibraryInfo> items, LinearLayout llContainer) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_library, null);
        llContainer.addView(view);
        SViewUtils.setViewMargin(view, 1, SViewUtils.SDirection.LEFT);
        SViewUtils.setViewMargin(view, 1, SViewUtils.SDirection.RIGHT);
        view.setBackgroundResource(R.drawable.trans);
        NRecycleView nvLibrary = (NRecycleView) view.findViewById(R.id.nv_new_library);
        final RelativeLayout rlLadder = (RelativeLayout) view.findViewById(R.id.rl_ask_ladder);
        rlLadder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final RelativeLayout rlInvite = (RelativeLayout) view.findViewById(R.id.rl_invite);
        rlInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final LinearLayout llAskOther = (LinearLayout) view.findViewById(R.id.ll_ask_other);
        if (!askViews.contains(llAskOther)) {
            askViews.add(llAskOther);
            if (askViews.size() > 100) {
                askViews.clear();//快发版本，先这样将就解决
            }
        }
        llAskOther.setBackgroundResource(R.drawable.trans);
        final ImageView ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        LibraryAdapter allLibraryAdapter = new LibraryAdapter(mContext, new OnReturnLibraryListener() {
            @Override
            public void onClick(LibraryInfo info, int position) {
                onItemClick(info, llAskOther, position, ivArrow);
            }
        });
        nvLibrary.setGridView(3);
        nvLibrary.setAdapter(allLibraryAdapter);
        allLibraryAdapter.notifyDataChanged(items);
    }

    LibraryInfo curLibraryInfo;

    protected void onItemClick(LibraryInfo info, LinearLayout llAskOther, int position, ImageView ivArrow) {
        for (LinearLayout layout : askViews) {
            layout.setVisibility(View.GONE);
        }
        if (llAskOther.getVisibility() == View.VISIBLE && info.getId() == curUserId) {
            llAskOther.setVisibility(View.GONE);
        } else {
            llAskOther.setVisibility(View.VISIBLE);
        }
        int status = position % 3;
        int divideMargin = 0;
        if (status != 0) {
            divideMargin = SUtils.getDip(mContext, 10);
        }
        SViewUtils.setViewMargin(ivArrow, SUtils.getDip(mContext, 40) + (SUtils.screenWidth / 3 * status - divideMargin), SViewUtils.SDirection.LEFT);
        ivArrow.invalidate();
        ivArrow.requestLayout();
        curLibraryInfo = info;
        curUserId = info.getId();
    }

    @Override
    public int getItemViewType(int position) {
        return resultList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return resultList == null ? 0 : resultList.size();
    }

    public class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        CharacterHolder(View view) {
            super(view);

            mTextView = (TextView) view.findViewById(R.id.contact_name);
        }
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        LinearLayout llContainer;

        ContactHolder(View view) {
            super(view);

            llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        }
    }

    public void notifyDataChanged(List<LibraryInfo> infos) {
        preItems = infos;

        handleContact();
    }

    public int getScrollPosition(String character) {
        if (resultList == null) {
            return -1;
        }
        for (int i = 0; i < resultList.size(); i++) {
            if (resultList.get(i).getCharacter().equals(character)) {
                return i;
            }
        }

        return -1; // -1不会滑动
    }
}

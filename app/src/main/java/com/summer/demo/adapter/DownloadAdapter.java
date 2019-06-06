package com.summer.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.BookBean;
import com.summer.demo.utils.AppBeansHelper;
import com.summer.helper.db.CommonService;
import com.summer.helper.downloader.DownloadManager;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.TipDialog;
import com.summer.helper.view.LoadingDialog;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<BookBean> mItems;
    private CommonService mService;
    LoadingDialog mLoading;
    DownloadManager downloadManager;

    public DownloadAdapter(Context context) {
        this.context = context;
        this.mService = new CommonService(context);
        //	mLoading = new LoadingDialog(context);
        downloadManager = DownloadManager.getInstance(context);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHodler = (MyViewHolder) holder;
        final BookBean book = mItems.get(position);
        SUtils.setPic(viewHodler.ivNav, book.getIcon(), 143, 202, R.drawable.trans, true);
        SUtils.setNotEmptText(viewHodler.tvName, book.getName());
        viewHodler.helper.setEntity(book);
        viewHodler.tvGrade.setText("开发商:" + book.getDeveloper());
        viewHodler.tvSize.setText("大小:" + book.getSize());
        viewHodler.tvTime.setText("更新时间:" + book.getPublished_at());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNav;
        TextView tvName;
        TextView tvGrade;
        TextView tvSize;
        TextView tvTime;
        TextView tvLoad;
        ProgressBar pbLoad;
        RelativeLayout rlDownload;
        AppBeansHelper helper;

        public MyViewHolder(View view) {
            super(view);
            ivNav = (ImageView) view.findViewById(R.id.iv_book_icon);
            tvName = (TextView) view.findViewById(R.id.tv_book_name);
            tvGrade = (TextView) view.findViewById(R.id.tv_grade);
            tvSize = (TextView) view.findViewById(R.id.tv_size);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvLoad = (TextView) view.findViewById(R.id.load_text);
            pbLoad = (ProgressBar) view.findViewById(R.id.load_pb);
            rlDownload = (RelativeLayout) view.findViewById(R.id.rl_download);
            SUtils.clickTransColor(rlDownload);
            helper = new AppBeansHelper(context, pbLoad, tvLoad, new AppBeansHelper.BookDownloadedListener() {

                @Override
                public void onCallback(BookBean bean) {
                    helper.setEntity(bean);
                }
            });
            helper.setCommonService(mService);
            helper.setDownloadManager(downloadManager);
            rlDownload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    SUtils.NetState state = SUtils.getNetWorkType(context);
                    if (state == SUtils.NetState.WIFI) {
                        helper.startDownload();
                    } else if (state == SUtils.NetState.MOBILE) {
                        new TipDialog(context, "当前处于数据网络情况下,继续下载吗?", new TipDialog.DialogAfterClickListener() {
                            @Override
                            public void onSure() {
                                helper.startDownload();
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).show();
                    } else {
                        SUtils.makeToast(context, R.string.network_error);
                    }

                }
            });
        }
    }

    public interface SourceSelectedListener {
        void afterClick(BookBean position);
    }

    @SuppressWarnings("unchecked")
    public void notifyDataChanged(List<?> arrsList) {
        this.mItems = (List<BookBean>) arrsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_download, parent,
                false));
        return holder;
    }

}

package com.ncgtelevision.net.home_screen.presenters;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;

import com.ncgtelevision.net.R;

public class CustomListRowPresenter extends ListRowPresenter {
    private Context mContext;

    public CustomListRowPresenter(Context mContext) {
        super();
        setHeaderPresenter(new CustomRowHeaderPresenter());
        this.mContext = mContext;
    }

    class CustomRowHeaderPresenter extends RowHeaderPresenter {
        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
            HeaderItem headerItem = item == null ? null : ((Row) item).getHeaderItem();
            RowHeaderPresenter.ViewHolder vh = (RowHeaderPresenter.ViewHolder) viewHolder;
            TextView title = vh.view.findViewById(R.id.row_header);
            if (!TextUtils.isEmpty(headerItem.getName())) {
                title.setText(headerItem.getName());
                title.setTextColor(ContextCompat.getColor(mContext,
                        R.color.redApp));
                title.setTypeface(ResourcesCompat.getFont(title.getContext(), R.font.product_sans_regular));
                title.setTextSize(16);
            }
        }
    }
}

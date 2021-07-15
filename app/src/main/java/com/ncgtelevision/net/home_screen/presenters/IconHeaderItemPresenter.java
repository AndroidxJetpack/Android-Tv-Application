package com.ncgtelevision.net.home_screen.presenters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;

import com.ncgtelevision.net.R;

public class IconHeaderItemPresenter extends RowHeaderPresenter {
    private float mUnselectedAlpha;
    private static View old;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
//        mUnselectedAlpha = viewGroup.getResources()
//                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.icon_header_item, null);
//        view.setAlpha(mUnselectedAlpha); // Initialize icons to be at half-opacity.

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        HeaderItem headerItem = ((PageRow) item).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(true);

//            ImageView iconView = (ImageView) rootView.findViewById(R.id.header_icon);
//            Drawable icon = rootView.getResources().getDrawable(R.drawable.android_header, null);
//            iconView.setImageDrawable(icon);

        TextView label = (TextView) rootView.findViewById(R.id.header_label);
        label.setText(headerItem.getName());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }

    // TODO: This is a temporary fix. Remove me when leanback onCreateViewHolder no longer sets the
    // mUnselectAlpha, and also assumes the xml inflation will return a RowHeaderView.
    @Override
    protected void onSelectLevelChanged(RowHeaderPresenter.ViewHolder holder) {
//        holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
//                (1.0f - mUnselectedAlpha));
        if(old != null){
            old.setBackgroundColor(holder.view.getContext().getResources().getColor(R.color.header_background));
            ((TextView) old.findViewById(R.id.header_label)).setTextColor(holder.view.getContext().getResources().getColor(R.color.text_color));
        }
        holder.view.setBackgroundColor(holder.view.getContext().getResources().getColor(R.color.redApp));
        ((TextView) holder.view.findViewById(R.id.header_label)).setTextColor(holder.view.getContext().getResources().getColor(R.color.white));
        old = holder.view;
    }
}

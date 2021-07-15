package com.ncgtelevision.net.channel;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.ncgtelevision.net.channel.presenter.ChannelPresenter;
import com.ncgtelevision.net.channel.presenter.MyListRowPresenter;
import com.ncgtelevision.net.home_screen.model.Category;
import com.ncgtelevision.net.home_screen.model.MoreInfo;
import com.ncgtelevision.net.home_screen.presenters.CardPresenter;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.utilities.CommonUtility;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class ChannelRow extends RowsSupportFragment {
    private static String ARG_PARAM1 = "ARG_PARAM1";
    private static String ARG_PARAM2 = "ARG_PARAM2";
    private final int COLUMN_SIZE = 11;

    private List<Category> homeData;
    private ArrayObjectAdapter rowsAdapter;
    private CardPresenter cardPresenter;
    private NavigationMenuCallback navigationMenuCallback;
    private String mTitle;

    public static ChannelRow getInstance(Serializable param1, String title) {
        ChannelRow fragment = new ChannelRow();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            homeData = (List<Category>) getArguments().getSerializable(ARG_PARAM1);
            mTitle = (String) getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
//        v.setBackgroundColor(getRandomColor());
        return v;
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (homeData != null) {
            loadRows();
            setOnItemViewClickedListener(new ItemViewClickedListener());
            setOnItemViewSelectedListener(new ItemViewSelectedListener());
        }
    }
//    @Override
//    public void setExpand(boolean expand) {
//        super.setExpand(false);
//    }
    private void loadRows() {
        setExpand(false);
        MyListRowPresenter rowPresenter = new MyListRowPresenter();
        //        rowPresenter.setExpandedRowHeight(140);
        rowPresenter.setShadowEnabled(false);
//        rowPresenter.setSelectEffectEnabled(false);
        rowsAdapter = new ArrayObjectAdapter(rowPresenter); //new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL)
        cardPresenter = new CardPresenter();

        for (int i = 0; i < homeData.size(); ) {
            int k = i;
            ChannelPresenter channelPresenter = new ChannelPresenter();
            channelPresenter.setNavigationMenuCallback(getNavigationMenuCallback());
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(channelPresenter);
            for (int j = 0; j <COLUMN_SIZE && j < homeData.size()-k; j ++){
                listRowAdapter.add(homeData.get(i));
                i++;
            }
//            if(k == 0 && !CommonUtility.isStringEmpty(mTitle)){
//                HeaderItem header = new HeaderItem(0, mTitle);
//                rowsAdapter.add(new ListRow(header, listRowAdapter));
//            }else {
//                rowsAdapter.add(new ListRow(listRowAdapter));
//            }
            rowsAdapter.add(new ListRow(listRowAdapter));
        }
        setAdapter(rowsAdapter);
    }

    public NavigationMenuCallback getNavigationMenuCallback() {
        return navigationMenuCallback;
    }

    public ArrayObjectAdapter getRowsAdapter() {
        return rowsAdapter;
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof Category) {
                navigationMenuCallback.selectedOption(item);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            int indexOfRow = rowsAdapter.indexOf(row);
            int indexOfItem =  ((ArrayObjectAdapter)((ListRow) row ).getAdapter()).indexOf(item);
            Log.d("TAG", "onItemSelected: " + indexOfItem + "  row=>>> " +indexOfRow);
                 if(itemViewHolder != null) {
                     itemViewHolder.view.setOnKeyListener(new View.OnKeyListener() {
                         @Override
                         public boolean onKey(View view, int i, KeyEvent keyEvent) {
                             if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                                 if (i == KeyEvent.KEYCODE_DPAD_LEFT  && indexOfItem == 0) {
                                     navigationMenuCallback.navMenuToggle(true);
                                     view.clearFocus();
                                 }else if(i == KeyEvent.KEYCODE_DPAD_UP && indexOfRow == 0){
                                     navigationMenuCallback.navMenuToggle(false);
                                     view.clearFocus();
                                 }
                             }
                             return false;
                         }
                     });
                 }
        }
    }

}

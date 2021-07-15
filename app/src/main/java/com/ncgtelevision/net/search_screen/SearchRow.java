package com.ncgtelevision.net.search_screen;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
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
import com.ncgtelevision.net.search_screen.presenters.SearchPresenter;
import com.ncgtelevision.net.search_screen.search_model.Video;

import java.io.Serializable;
import java.util.List;

public class SearchRow extends RowsSupportFragment {
    private static String ARG_PARAM1 = "ARG_PARAM1";
    private final int COLUMN_SIZE = 6;
    private List<Video> searchVideoData;
    private ArrayObjectAdapter rowsAdapter;
    private CardPresenter cardPresenter;
    private NavigationMenuCallback navigationMenuCallback;


    public static SearchRow getInstance(Serializable param1) {
        SearchRow fragment = new SearchRow();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchVideoData = (List<Video>) getArguments().getSerializable(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
//        v.setBackgroundColor(getRandomColor());
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (searchVideoData != null) {
            loadRows();
            setOnItemViewClickedListener(new ItemViewClickedListener());
            setOnItemViewSelectedListener(new ItemViewSelectedListener());
        }
    }

//    @Override
//    public void setExpand(boolean expand) {
//        super.setExpand(true);
//    }
    private void loadRows() {
        setExpand(false);
        MyListRowPresenter rowPresenter = new MyListRowPresenter();
        rowPresenter.setShadowEnabled(false);
        rowsAdapter = new ArrayObjectAdapter(rowPresenter);
        cardPresenter = new CardPresenter();
//        SearchPresenter searchPresenter = new SearchPresenter();
//        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(searchPresenter);
//        for (int i = 0; i < searchVideoData.size(); i++) {
//            listRowAdapter.add(searchVideoData.get(i));
//        }
////        HeaderItem header = new HeaderItem(i, homeData.get(i).getTitle());
//        rowsAdapter.add(new ListRow(listRowAdapter));
//        setAdapter(rowsAdapter);

        for (int i = 0; i < searchVideoData.size(); ) {
            int k = i;
            SearchPresenter searchPresenter = new SearchPresenter();
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(searchPresenter);
            for (int j = 0; j <COLUMN_SIZE && j < searchVideoData.size()-k; j ++){
                listRowAdapter.add(searchVideoData.get(i));
                i++;
            }
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
            if (item instanceof Video && navigationMenuCallback != null) {
                navigationMenuCallback.selectedOption(item);

            } else {
//                Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
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
                            }else if(i == KeyEvent.KEYCODE_DPAD_UP && indexOfItem == 0 && indexOfRow == 0){
                                navigationMenuCallback.navMenuToggle(false);
                                view.clearFocus();
                            }
                        }
                        return false;
                    }
                });
            }

//            if (item instanceof Movie) {
//                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
//                startBackgroundTimer();
//            }
        }
    }

}

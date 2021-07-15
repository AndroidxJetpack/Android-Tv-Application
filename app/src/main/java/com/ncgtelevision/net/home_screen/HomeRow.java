package com.ncgtelevision.net.home_screen;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;

import com.ncgtelevision.net.home_screen.model.Category;
import com.ncgtelevision.net.home_screen.model.MoreInfo;
import com.ncgtelevision.net.home_screen.presenters.MyListRowPresenter;
import com.ncgtelevision.net.home_screen.presenters.StringPresenter;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.interfaces.RowFragmentStateChangeCallback;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class HomeRow extends RowsSupportFragment {
    private static String ARG_PARAM1 = "ARG_PARAM1";
    private static String ARG_PARAM2 = "ARG_PARAM2";

    private static List<Category> homeData;
    private static List<MoreInfo> myList;

    private ArrayObjectAdapter rowsAdapter;
    private NavigationMenuCallback navigationMenuCallback;
    private RowFragmentStateChangeCallback rowFragmentStateChangeCallback;

    public static HomeRow getInstance(Serializable param1, Serializable param2) {
        HomeRow fragment = new HomeRow();
        homeData = (List<Category>) param1;
        myList = (List<MoreInfo>) param2;
    /*    Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         /*   homeData = (List<Category>) getArguments().getSerializable(ARG_PARAM1);
            myList = (List<MoreInfo>) getArguments().getSerializable(ARG_PARAM2);*/
        }
//        getMainFragmentAdapter().getFragmentHost().showTitleView(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
//        params.setMargins(0,0,0,0);

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
        enableRowScaling(false);
        if (homeData != null) {
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
        MyListRowPresenter rowPresenter = new MyListRowPresenter();
        rowPresenter.setShadowEnabled(false);
//        rowPresenter.setExpandedRowHeight(140);
        rowsAdapter = new ArrayObjectAdapter(rowPresenter);
//        rowsAdapter.setPresenterSelector(new PresenterSelector() {
//            @Override
//            public Presenter getPresenter(Object item) {
//                return null;
//            }
//        });
        StringPresenter stringPresenter = new StringPresenter();
        stringPresenter.setNavigationMenuCallback(navigationMenuCallback);
        int adder = 0;
        if (myList != null && myList.size() > 0) {
            ArrayObjectAdapter myListAdapter = new ArrayObjectAdapter(stringPresenter);
            for (int j = 0; j < myList.size(); j++) {
                myListAdapter.add(myList.get(j));
            }
            HeaderItem header = new HeaderItem(adder, "My List");
            rowsAdapter.add(new ListRow(header, myListAdapter));
            adder = 1;
        }

        for (int i = 0; i < homeData.size(); i++) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(stringPresenter);
            List<MoreInfo> moreInfos = homeData.get(i).getMoreInfo();
            for (int j = 0; j < moreInfos.size(); j++) {
                listRowAdapter.add(moreInfos.get(j));
            }
            String title = homeData.get(i).getTitle();
//            if(i == 0){
//                title = title + "© NCG Television 2021";
//            }
            HeaderItem header = new HeaderItem(i + adder, title);
            rowsAdapter.add(new ListRow(header, listRowAdapter));
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

    public void setRowFragmentStateChangeCallback(RowFragmentStateChangeCallback rowFragmentStateChangeCallback) {
        this.rowFragmentStateChangeCallback = rowFragmentStateChangeCallback;
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof MoreInfo) {
//                Toast.makeText(getActivity(), "MoreInfo", Toast.LENGTH_SHORT).show();
//                if(navigationMenuCallback != null)
//                navigationMenuCallback.selectedOption(item);
                try {
                    int indexOfRow = rowsAdapter.indexOf(row);
                    int indexOfItem = ((ArrayObjectAdapter) ((ListRow) row).getAdapter()).indexOf(item);
                    if (rowFragmentStateChangeCallback != null) {
                        rowFragmentStateChangeCallback.navMenuToggle(indexOfRow, indexOfItem);
                    }
                } catch (Exception e) {

                }
            } else {
//                Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
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
//            if (item instanceof MoreInfo) {
//                MoreInfo moreInfo = (MoreInfo) item;
//                String s [] = new String[3];
//                s[0] = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
//                s[1]= moreInfo.getTitle();
//                s[2] = moreInfo.getDescription();
//                navigationMenuCallback.selectedOption(s);
//            }
            int indexOfRow = rowsAdapter.indexOf(row);
            int indexOfItem = ((ArrayObjectAdapter) ((ListRow) row).getAdapter()).indexOf(item);
            if (itemViewHolder != null) {
                itemViewHolder.view.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                            if (i == KeyEvent.KEYCODE_DPAD_LEFT && indexOfItem == 0) {
                                navigationMenuCallback.navMenuToggle(true);
                                view.clearFocus();
                            } else if (i == KeyEvent.KEYCODE_DPAD_UP && indexOfRow == 0) {
                                navigationMenuCallback.navMenuToggle(false);
                                view.clearFocus();
                            }
                        }
                        return false;
                    }
                });
            }
//            CommonUtility.showToast( getContext(), indexOfRow + " Index " + indexOfItem);
        }
    }

}


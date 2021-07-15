package com.ncgtelevision.net.home_screen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.app.HeadersSupportFragment;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.CursorObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.PageRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.account.MembershipFragment;
import com.ncgtelevision.net.home_screen.model.CommonMenuItem;
import com.ncgtelevision.net.home_screen.model.HomePageModel;
import com.ncgtelevision.net.home_screen.model.HomePageRequest;
import com.ncgtelevision.net.home_screen.presenters.IconHeaderItemPresenter;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BrowseSupportFragment implements Callback<HomePageModel> {
    private String TAG = "HomeFragment";
    private ArrayObjectAdapter mRowsAdapter; //implements LoaderManager.LoaderCallbacks<Cursor>
    private HomePageModel mHomeData;
    private BackgroundManager mBackgroundManager;
    private int listLength;
    private RowsSupportFragment mRowFragment;
    private HeadersSupportFragment mHeaderFragment;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                 setupUi();
//                 loadData();
                 mBackgroundManager = BackgroundManager.getInstance(getActivity());
                 mBackgroundManager.attach(getActivity().getWindow());
                 getMainFragmentRegistry().registerFragment(PageRow.class,
                         new PageRowFragmentFactory(mBackgroundManager));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        callAPI();
        return view;
    }

    private void callAPI() {
        Log.d(TAG, "callAPI: ");
        HomePageRequest req = new HomePageRequest();
        req.setLang("en");
        ApiClient.getClient(getContext()).create(ApiInterface.class)
                .getHomePage(req).enqueue(this);
    }

    @Override
    public void onResponse(Call<HomePageModel> call, Response<HomePageModel> response) {
        Log.d(TAG, "onResponse: ");
        startEntranceTransition();
        if(response.body() != null){
                mHomeData = response.body();
                loadData();
            }
    }

    private void loadData() {
        List<CommonMenuItem> menuItem = mHomeData.getMenuItems().getCommonMenuItems();
        List<CommonMenuItem> menuItems = new ArrayList<CommonMenuItem>();
        CommonMenuItem item1 = new CommonMenuItem();
        item1.setMainMenu("Search");
        CommonMenuItem item2 = new CommonMenuItem();
        item2.setMainMenu("Home");
        menuItems.add(item1);
        menuItems.add(item2);
        menuItems.addAll(menuItem);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        int i =0;
        listLength = 0;
        for ( ; i < menuItems.size(); i++){
            HeaderItem headerItem1 = new HeaderItem(i, menuItems.get(i).getMainMenu());
            PageRow pageRow1 = new PageRow(headerItem1);
            mRowsAdapter.add(pageRow1);
            listLength++;
        }
        HeaderItem headerItem1 = new HeaderItem(i, "Account");
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);
           i++;
        HeaderItem headerItem2 = new HeaderItem(i, "Logout");
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

        setAdapter(mRowsAdapter);
        setSelectedPosition(listLength, true);
    }
    private void setupUi() {
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.header_background));
        mRowFragment = getRowsSupportFragment();
        mHeaderFragment = getHeadersSupportFragment();

//        startHeadersTransition(true);
        enableMainFragmentScaling(false);

//        showTitle(false);
//        setOnSearchClickedListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(
//                        getActivity(), getString(R.string.personal_settings), Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });

//        prepareEntranceTransition();
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });
    }

    @Override
    public void onFailure(Call<HomePageModel> call, Throwable t) {
                t.printStackTrace();
                int i=0;
        HeaderItem headerItem1 = new HeaderItem(i, "Account");
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        mRowsAdapter.add(pageRow1);
        i++;
        HeaderItem headerItem2 = new HeaderItem(i, "Logout");
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

        setAdapter(mRowsAdapter);
        setSelectedPosition(0, true);
    }

    private class PageRowFragmentFactory extends BrowseSupportFragment.FragmentFactory {
        private final BackgroundManager mBackgroundManager;

        PageRowFragmentFactory(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        @Override
        public Fragment createFragment(Object rowObj) {
            Row row = (Row)rowObj;
//            mBackgroundManager.setDrawable(null);

            if (row.getHeaderItem().getId() == listLength) {
                MembershipFragment fragment = new MembershipFragment();
//                fragment.getMainFragmentAdapter().setScalingEnabled(true);
//                fragment.getMainFragmentAdapter().setExpand(true);
                return fragment;
            }else{
                MyHomeFragment fragment = MyHomeFragment.newInstance(mHomeData);
                fragment.getMainFragmentAdapter().setScalingEnabled(false);
                return fragment;
            }

//            throw new IllegalArgumentException(String.format("Invalid row %s", rowObj));
        }
    }

}
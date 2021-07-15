package com.ncgtelevision.net.search_screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.channel.ChannelFragment;
import com.ncgtelevision.net.channel.ChannelRow;
import com.ncgtelevision.net.home_screen.model.Category;
import com.ncgtelevision.net.home_screen.model.HomePageModel;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.playback.PlaybackFragment;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;
import com.ncgtelevision.net.search_screen.search_model.SearchModel;
import com.ncgtelevision.net.search_screen.search_model.Video;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements NavigationMenuCallback, Callback<SearchModel> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG=SearchFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mMainFragment;
    private ProgressDialog mDialog;
    private Context mContext;
    private SearchModel mSearchData;
    private SearchRow searchRow;
    private NavigationMenuCallback navigationMenuCallback;
    private FrameLayout mRowFrame;
    private EditText edSearch;
    private Button btnSearch;
    private TextView textMostSeen,textMostWanted;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainFragment=inflater.inflate(R.layout.fragment_search, container, false);
        mRowFrame = (FrameLayout) mMainFragment.findViewById(R.id.row_frame);
        edSearch =  mMainFragment.findViewById(R.id.ed_search);
        btnSearch =  mMainFragment.findViewById(R.id.btn_search);
//        textMostWanted =  mMainFragment.findViewById(R.id.text_most_wanted);
//        textMostSeen =  mMainFragment.findViewById(R.id.text_most_seen);
        mDialog = custom_loader(getActivity());
        mContext = getContext();

//        textMostWanted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Todo action on most wanted movies
//            }
//        });
//
//
//        textMostSeen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Todo action on most Seen movies
//            }
//        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI();
            }
        });
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                Log.d(TAG, "onEditorAction1: " + event.getKeyCode());
                Log.d(TAG, "onEditorAction2: " + actionId);
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    //do stuff
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    return true;
                }else  if(actionId == EditorInfo.IME_ACTION_GO){
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    callAPI();
                    return true;
                }
//                if(event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//                    callAPI();
//                    return true;
//                }
                return false;
            }
        });

        edSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i== KeyEvent.KEYCODE_DPAD_LEFT){
                        if(navigationMenuCallback != null) {
                            navigationMenuCallback.navMenuToggle(true);
                            return true;
                        }
                    }
                    if(i == KeyEvent.KEYCODE_DPAD_DOWN && mSearchData != null &&  mSearchData.getResults() != null &&  mSearchData.getResults().size() >0 && mSearchData.getResults().get(0).getVideos()!= null && mSearchData.getResults().get(0).getVideos().size() >0 ){
                        if(searchRow != null){
                            searchRow.setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(0){
                                @Override
                                public void run(Presenter.ViewHolder holder) {
                                    super.run(holder);
                                    if(holder != null){
                                        holder.view.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                holder.view.requestFocus();
                                            }
                                        }, 10);
                                    }

                                }
                            });
                            return true;
                        }
                    }

                }
                return false;
            }
        });
        btnSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i == KeyEvent.KEYCODE_DPAD_DOWN && mSearchData != null &&  mSearchData.getResults() != null &&  mSearchData.getResults().size() >0 && mSearchData.getResults().get(0).getVideos()!= null && mSearchData.getResults().get(0).getVideos().size() >0 ){
                        if(searchRow != null){
                            searchRow.setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(0){
                                @Override
                                public void run(Presenter.ViewHolder holder) {
                                    super.run(holder);
                                    if(holder != null){
                                        holder.view.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                holder.view.requestFocus();
                                            }
                                        }, 10);
                                    }

                                }
                            });
                            return true;
                        }
                    }

                }
                return false;
            }
        });
        intializeFirstFocus();
        return mMainFragment;
    }

    private void callAPI() {
        Log.d(TAG, "callAPI: ");
        mDialog.show();

        SearchRequestBody req=new SearchRequestBody();
        req.setSearch_keyword(edSearch.getText().toString());
        ApiClient.getClient(mContext).create(ApiInterface.class)
                .postSearchVideos(req).enqueue(this);
            }

    @Override
    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
        mDialog.dismiss();
        if (response.body()!=null){
            mSearchData=response.body();
            loadData();
            edSearch.setText("");
        }
    }

    @Override
    public void onFailure(Call<SearchModel> call, Throwable t) {
        t.printStackTrace();
        mDialog.dismiss();
    }



    private void loadData() {
        for (int i=0;i<mSearchData.getResults().size();i++) {
            searchRow = SearchRow.getInstance((Serializable) mSearchData.getResults().get(i).getVideos());
        }

        searchRow.setNavigationMenuCallback(this);
        if( getActivity() != null)
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.row_frame, searchRow).commit();
    }

    private void loadVideo() {
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }



    @Override
    public void navMenuToggle(boolean toShow) {
        if(this.navigationMenuCallback != null) {
            this.navigationMenuCallback.navMenuToggle(toShow);
        }else {
            mRowFrame.clearFocus();
//            mSimpleExoPlayerView.requestFocus();
        }
    }

    public void intializeFirstFocus(){
        if(edSearch != null)
        edSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                edSearch.requestFocus();
            }
        }, 10);

    }

    @Override
    public void selectedOption(@NotNull Object any) {
        Log.d(TAG, "selectedOption: ");
        if(any instanceof Video){
            Video category = (Video) any;
            if(category.getVideoId() == 0){
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                ChannelFragment fragment = ChannelFragment.newInstance(category.getChannelName());
//                fragmentManager.beginTransaction().replace(R.id.main_FL, fragment).addToBackStack("amit").commit();
            }else{
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                PlaybackFragment fragment = PlaybackFragment.newInstance(category.getVideoId(), category.getVideoThumbnail());
                fragment.setNavigationMenuCallback(this.navigationMenuCallback);
                fragmentManager.beginTransaction().replace(R.id.main_FL, fragment)
                        .addToBackStack("amit")
                        .commit();
//                Intent intent = new Intent(mContext, PlaybackActivity.class);
//                intent.putExtra("VIDEO_ID", category.getVideoId());
//                intent.putExtra("Title", category.getTitle());
//                startActivity(intent);
            }
        }
    }
}
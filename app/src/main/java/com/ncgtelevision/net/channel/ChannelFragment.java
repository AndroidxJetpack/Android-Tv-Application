package com.ncgtelevision.net.channel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.channel.model.ChannelModel;
import com.ncgtelevision.net.home_screen.HomeRow;
import com.ncgtelevision.net.home_screen.model.Banner;
import com.ncgtelevision.net.home_screen.model.Category;
import com.ncgtelevision.net.home_screen.model.HomePageModel;
import com.ncgtelevision.net.home_screen.model.HomePageRequest;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.local_storage.TokenStorage;
import com.ncgtelevision.net.playback.PlaybackActivity;
import com.ncgtelevision.net.playback.PlaybackFragment;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;
import com.ncgtelevision.net.signin.SignInActivity;
import com.ncgtelevision.net.utilities.CommonUtility;
import com.ncgtelevision.net.utilities.ConstantUtility;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;
import static com.ncgtelevision.net.utilities.CommonUtility.isStringEmpty;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends Fragment implements Callback<HomePageModel>, NavigationMenuCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = "ChannelFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParentCategory;
    private View mMainFragment;
    private ProgressDialog mDialog;
    private Context mContext;
    private HomePageModel mHomeData;
    private ChannelRow channelRow;
    private NavigationMenuCallback navigationMenuCallback;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer player;
    private FrameLayout mRowFrame;
    private TextView mTitleTV;
    private TextView mDescriptionTV;
    private ImageView mLogo;
    private TextView headerSubLebel;
    private TextView headerLebel;
    private TextView subChannelLable;

    public ChannelFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Parameter 1.
     * @return A new instance of fragment ChannelFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChannelFragment newInstance(String category, String parentCategory) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, category);
        args.putString(ARG_PARAM2, parentCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParentCategory = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainFragment = inflater.inflate(R.layout.fragment_channel, container, false);
        // Inflate the layout for this fragment
         mSimpleExoPlayerView = (SimpleExoPlayerView) mMainFragment.findViewById(R.id.video_player_view);
         mRowFrame = (FrameLayout) mMainFragment.findViewById(R.id.row_frame);
        mTitleTV = (TextView) mMainFragment.findViewById(R.id.title_TV);
        mDescriptionTV = (TextView) mMainFragment.findViewById(R.id.description_TV);
        mLogo = (ImageView) mMainFragment.findViewById(R.id.logo_IB);
        headerLebel = (TextView) mMainFragment.findViewById(R.id.header_label);
        headerSubLebel = (TextView)mMainFragment.findViewById(R.id.sub_header_label);
        subChannelLable = (TextView)mMainFragment.findViewById(R.id.sub_channel_label);


        mDialog = custom_loader(getActivity());
        mContext = getContext();
        setUpKeyEvents();
        callAPI();
        mLogo.requestFocus();
        return mMainFragment;
    }

    private void setUpKeyEvents() {
        mSimpleExoPlayerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if(i == KeyEvent.KEYCODE_DPAD_UP){
                        mLogo.requestFocus();
                        return true;
                    }
                    if(i == KeyEvent.KEYCODE_DPAD_LEFT){
                        if(navigationMenuCallback != null) {
                            navigationMenuCallback.navMenuToggle(true);
                            return true;
                        }
                    }
                    if (i == KeyEvent.KEYCODE_DPAD_DOWN || i == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if(channelRow != null){
                            mRowFrame.requestFocus();
                            channelRow.setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(0){
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
        mLogo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if(i == KeyEvent.KEYCODE_DPAD_LEFT){
                        if(navigationMenuCallback != null) {
                            navigationMenuCallback.navMenuToggle(true);
                            return true;
                        }
                    }
                    if (i == KeyEvent.KEYCODE_DPAD_DOWN || i == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if(channelRow != null){
                            mRowFrame.requestFocus();
                            channelRow.setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(0){
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if(player != null && getActivity() !=null){
            player.setPlayWhenReady(true);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(player != null){
            player.setPlayWhenReady(false);
        }
        releasePlayer();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if(player != null){
            player.setPlayWhenReady(false);
            releasePlayer();
        }
    }
    private void releasePlayer() {
        if(player != null){
            player.release();
        }
    }
    private void callAPI() {
        mDialog.show();
        ChannelModel req = new ChannelModel();
        req.setChannelName(mParam1);
        Log.i(TAG, "callAPI: Channel "+req.toString());
        Log.i(TAG, "callAPI: Channel "+mParam1);
        ApiClient.getClient(mContext).create(ApiInterface.class)
                .getChannelPage(req).enqueue(this);
    }
    private void loadData() {
        if(mHomeData.isStatus()) {
            if(mHomeData.getParent() != null && mHomeData.getParent().size() > 0){
                channelRow = ChannelRow.getInstance((Serializable) mHomeData.getCategory(), mParam1);
                headerSubLebel.setVisibility(View.VISIBLE);
                subChannelLable.setVisibility(View.VISIBLE);
                subChannelLable.setText(mParam1);

                if(!isStringEmpty(mParentCategory) && !mHomeData.getParent().get(0).getChannelName().equalsIgnoreCase(mParentCategory)) {
                    headerSubLebel.setText(mHomeData.getParent().get(0).getTitle());
                    headerLebel.setText(mParentCategory + "/");
                }else{
                    headerLebel.setVisibility(View.GONE);
                    headerSubLebel.setText(mHomeData.getParent().get(0).getTitle());
                }
            }else{
                headerLebel.setText(mParentCategory);
                if(CommonUtility.isStringEmpty(mParentCategory)){
                    subChannelLable.setVisibility(View.VISIBLE);
                    subChannelLable.setText(mParam1);
                    channelRow = ChannelRow.getInstance((Serializable) mHomeData.getCategory(), mParam1);
                }else {
                    channelRow = ChannelRow.getInstance((Serializable) mHomeData.getCategory(), "");
                }
            }
            channelRow.setNavigationMenuCallback(this);
            if( getActivity() != null)
              getActivity().getSupportFragmentManager()
                      .beginTransaction()
                      .add(R.id.row_frame, channelRow)
                      .commit();
            loadVideo();
        }else{
            mDescriptionTV.setText(mHomeData.getMessage());
            mLogo.requestFocus();
        }
    }
    private void loadVideo() {
          List<Banner> bannerList= (List<Banner>) mHomeData.getBanner();
          String url = "";
          String title = "";
          String description = "";
        if(bannerList != null && bannerList.size() > 0){
            url = bannerList.get(0).getTrailer();
            title =  bannerList.get(0).getTitle();
            description = bannerList.get(0).getDescription();
            Log.d("main", "loadVideo: "+url);
        }

//        if(!bannerList.get(0).getTrailer().equals("")) {
//            url = bannerList.get(0).getTrailer();
//            title = bannerList.get(0).getTitle();
//            description = bannerList.get(0).getDescription();
//        }
//        else {
//            url = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
//            title = bannerList.get(0).getTitle();
//            description = bannerList.get(0).getDescription();
//        }

        Log.d(TAG, "loadVideo: ");
        if(isStringEmpty(url)){
            mMainFragment.findViewById(R.id.videoview_RL).setVisibility(View.GONE);
            if(player != null){
                player.release();
            }
            return;
        }
        loadChannelVideo(url, title, description);
////        MediaSource[] mediaSources = null;



//
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//
//        TrackSelection.Factory videoTrackSelectionFactory =
//                new AdaptiveTrackSelection.Factory(bandwidthMeter);
//
//        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
//
//         player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
//
//        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
//
//        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "NCG"));
//
//
//
//        if(isStringEmpty(url)){
//            mMainFragment.findViewById(R.id.videoview_RL).setVisibility(View.GONE);
//            return;
//        }
//        MediaSource mediaSource =  new ExtractorMediaSource(Uri.parse(url),
//                mediaDataSourceFactory, extractorsFactory, null, null);
//        player.prepare(mediaSource);
//        player.setPlayWhenReady(true);
//        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
//        mSimpleExoPlayerView.hideController();
//        player.addListener(new Player.EventListener() {
//            @Override
//            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
//
//            }
//
//            @Override
//            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
//
//                initializeTitle();
//            }
//
//            @Override
//            public void onLoadingChanged(boolean isLoading) {
//                Log.d(TAG, "onLoadingChanged: " + player.getCurrentWindowIndex());
//            }
//
//            @Override
//            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//
//            }
//
//            @Override
//            public void onRepeatModeChanged(int repeatMode) {
//
//            }
//
//            @Override
//            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
//
//            }
//
//            @Override
//            public void onPlayerError(ExoPlaybackException error) {
//                error.printStackTrace();
//                mSimpleExoPlayerView.hideController();
//                player.setPlayWhenReady(false);
//            }
//
//            @Override
//            public void onPositionDiscontinuity(int reason) {
//
//            }
//
//            @Override
//            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
//
//            }
//
//            @Override
//            public void onSeekProcessed() {
//
//            }
//        });
//        mSimpleExoPlayerView.setPlayer(player);

    }
    private void loadChannelVideo(String url, String title, String description) {
        if(player != null){
            player.release();
//            mMediaSource.clear();
        }
        mMainFragment.findViewById(R.id.videoview_RL).setVisibility(View.VISIBLE);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "NCG"));
//        url = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
        if(url == null){
            url="";
        }
        MediaSource mediaSource =  new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);  //new ConcatenatingMediaSource(mediaSources);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
        mSimpleExoPlayerView.hideController();
        initializeTitle(title, description);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.d(TAG, "onLoadingChanged: " + player.getCurrentWindowIndex());
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.i(TAG, "onPlayerError: channel fragment "+error.getMessage());

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        mSimpleExoPlayerView.setPlayer(player);
        mSimpleExoPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
            }
        });

    }
    private void initializeTitle(String title, String description) {
        mTitleTV.setText(title);
        mDescriptionTV.setText(description);
    }
    private void initializeTitle() {
        List<Banner> bannerList=  mHomeData.getBanner();
        Banner banner = bannerList.get(player.getCurrentWindowIndex());
        mTitleTV.setText(banner.getTitle());
        mDescriptionTV.setText(banner.getDescription());
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }

    @Override
    public void onResponse(Call<HomePageModel> call, Response<HomePageModel> response) {
        mDialog.dismiss();
        if(response.body() != null){
            mHomeData = response.body();
            if(mHomeData.isStatus() || mHomeData.isSuccess()) {
                loadData();
            }else if(response.body().getStatusCode() == 403){
                CommonUtility.showToast(mContext,response.body().getMessage());
                TokenStorage.clearSharedToken(mContext);
                Intent intent = new Intent(mContext, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                getActivity().finish();
            }else{
                loadData();
                CommonUtility.showToast(mContext,response.body().getMessage());
            }
        }else {
            CommonUtility.showToast(mContext, ConstantUtility.SERVER_ISSUE);
        }
//        if(response.body() != null){
//            mHomeData = response.body();
//            loadData();
//        }
    }

    @Override
    public void onFailure(Call<HomePageModel> call, Throwable t) {
        t.printStackTrace();
        mDialog.dismiss();
        CommonUtility.showToast(mContext, ConstantUtility.SERVER_ISSUE);
    }

    @Override
    public void navMenuToggle(boolean toShow) {
        loadVideo();
        if(toShow && this.navigationMenuCallback != null) {
            this.navigationMenuCallback.navMenuToggle(toShow);
        }else {
            mLogo.requestFocus();
        }
    }
    public void intializeFirstFocus(){
//        mSimpleExoPlayerView.requestFocus();
        if(mLogo != null)
            mLogo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLogo.requestFocus();
                }
            }, 10);
    }

    @Override
    public void selectedOption(@NotNull Object any) {
        Log.d(TAG, "selectedOption: ");
        if(any instanceof Category){
            Category category = (Category) any;
            if(category.getVideoId() == 0){
               FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
               ChannelFragment fragment = ChannelFragment.newInstance(category.getChannelName(), mParentCategory);
                //NewChannelFragment fragment = NewChannelFragment.newInstance(category.getChannelName(), mParentCategory);
               // fragmentManager.popBackStack("amit",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //getActivity().onBackPressed();
                //fragmentManager.popBackStack();
               fragmentManager.beginTransaction()
                       .replace(R.id.main_FL, fragment,"amit")
                       .addToBackStack("amit")
                       .commit();
            }else{
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                PlaybackFragment fragment = PlaybackFragment.newInstance(category.getVideoId(), category.getImage());
                fragment.setNavigationMenuCallback(this.navigationMenuCallback);
               //fragmentManager.popBackStack("amit",FragmentManager.POP_BACK_STACK_INCLUSIVE);
               // getActivity().onBackPressed();
                //fragmentManager
                fragmentManager.beginTransaction()
                        .replace(R.id.main_FL, fragment)
                        .addToBackStack("amit")
                        .commit();
            }
        }else if(any instanceof String[]){
            String[] detail = (String[]) any;
            loadChannelVideo(detail[0], detail[1], detail[2]);
        }
    }
}
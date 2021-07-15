package com.ncgtelevision.net.home_screen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.viewpager.widget.ViewPager;

import com.androijo.tvnavigation.interfaces.NavigationInitializeListener;
import com.androijo.tvnavigation.utils.Constants;
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
import com.google.gson.JsonArray;
import com.ncgtelevision.net.PlaybackVideoFragment;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.channel.ChannelFragment;
import com.ncgtelevision.net.home_screen.model.Banner;
import com.ncgtelevision.net.home_screen.model.Category;
import com.ncgtelevision.net.home_screen.model.CommonMenuItem;
import com.ncgtelevision.net.home_screen.model.HomePageModel;
import com.ncgtelevision.net.home_screen.model.HomePageRequest;
import com.ncgtelevision.net.home_screen.model.MoreInfo;
import com.ncgtelevision.net.home_screen.presenters.VideoViewPagerAdapter;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.interfaces.RowFragmentStateChangeCallback;
import com.ncgtelevision.net.local_storage.TokenStorage;
import com.ncgtelevision.net.playback.PlaybackActivity;
import com.ncgtelevision.net.playback.PlaybackFragment;
import com.ncgtelevision.net.playback.model.Datum;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;
import com.ncgtelevision.net.signin.SignInActivity;
import com.ncgtelevision.net.utilities.CommonUtility;
import com.ncgtelevision.net.utilities.ConstantUtility;
import com.ncgtelevision.net.viewpager.ViewPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;
import static com.ncgtelevision.net.utilities.CommonUtility.isStringEmpty;

public class NewHomeFragment extends Fragment implements Callback<HomePageModel>, NavigationMenuCallback, RowFragmentStateChangeCallback {
    private String TAG = NewHomeFragment.class.getName();
    private Context context;
    private FrameLayout mVideoFrame;
    private  FrameLayout mRowFrame;
    private View mMainFragment;
    private View mVideoview_container;
    private HomePageModel mHomeData;
    private NavigationMenuCallback navigationMenuCallback;
    private HomeRow homeRow;
    private ProgressDialog mDialog;
    private ViewPager mViewPager;
    private VideoViewPagerAdapter mViewPageAdapter;
    private Context mContext;
    private Button btnGoToChannel;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer player;
    private ConcatenatingMediaSource mMediaSource;
    private TextView mTitleTV;
    private TextView mDescriptionTV;
    private NavigationInitializeListener navigationInitializeListener;
    private View mViewRL;
    private int rowIndex =0;
    private View mLogo;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mMainFragment = inflater.inflate(R.layout.fragment_my_home, container, false);
        mVideoview_container = mMainFragment.findViewById(R.id.videoview_container);
//         mVideoFrame = (FrameLayout) mMainFragment.findViewById(R.id.videoview_frame);
        mRowFrame = (FrameLayout) mMainFragment.findViewById(R.id.row_frame);
        btnGoToChannel=mMainFragment.findViewById(R.id.go_to_channel);
//        btnMyList=mMainFragment.findViewById(R.id.my_list);
        mLogo = mMainFragment.findViewById(R.id.logo_IB);
        mDialog = custom_loader(getActivity());
        mContext = getContext();

        mSimpleExoPlayerView = (SimpleExoPlayerView) mMainFragment.findViewById(R.id.video_player_view);
        mTitleTV = (TextView) mMainFragment.findViewById(R.id.title_TV);
        mDescriptionTV = (TextView) mMainFragment.findViewById(R.id.description_TV);
//        mViewRL = mMainFragment.findViewById(R.id.videoview_RL);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int width =  metrics.widthPixels;
        float pixelRatio =  (float) width/ (float) heightPixels;
         Log.d(TAG, "onCreateView: height=> " + heightPixels + "  width- " + width );
        Log.d(TAG, "onCreateView:pixelRatio " + pixelRatio );



        callAPI();
        setKeyEvent();
//        mSimpleExoPlayerView.requestFocus();
//        btnGoToChannel.requestFocus();
        intializeFirstFocus();

        return mMainFragment;
    }

    private void loadDotIndicator(int selectedIndex){
       LinearLayout dotIndicator = mMainFragment.findViewById(R.id.dot_indicator_ll);
       dotIndicator.setVisibility(View.VISIBLE);

       LayoutInflater inflater = LayoutInflater.from(context);
       dotIndicator.removeAllViews();
       List<Banner> bannerList=  mHomeData.getBanner();
       Banner banner = bannerList.get(selectedIndex);
       mTitleTV.setText(banner.getTitle());
       mDescriptionTV.setText(banner.getDescription());

       if(isStringEmpty(banner.getChannelName())){
           btnGoToChannel.setVisibility(View.GONE);
//           if(selectedIndex == 0){
//               btnMyList.requestFocus();
//           }
       }else{
           btnGoToChannel.setVisibility(View.VISIBLE);
       }

        if(bannerList != null){
           for (int i=0; i <bannerList.size(); i++){
               if(selectedIndex == i){
                 View view = inflater.inflate(R.layout.item_selected_dot, dotIndicator, false);
                  dotIndicator.addView(view);
               }else{
                   View view = inflater.inflate(R.layout.item_unselected_dot, dotIndicator, false);
                   dotIndicator.addView(view);
               }
           }
       }
    }

    private void loadVideo() {
        if(player != null){
            player.release();
        }
        List<Banner> bannerList= mHomeData.getBanner();
        String url = "";
        MediaSource[] mediaSources = null;

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "NCG"));

        if(bannerList != null && bannerList.size() > 0){
            mediaSources = new MediaSource[bannerList.size()];
            for (int i=0; i<bannerList.size(); i++){
                url = bannerList.get(i).getVimeoUrl();//.replace("https", "http");
                mediaSources[i] = new ExtractorMediaSource(Uri.parse(url),
                        mediaDataSourceFactory, extractorsFactory, null, null);
                if(i==0){
                    initializeTitle(bannerList.get(i).getTitle(), bannerList.get(i).getDescription());
                }

            }
//            mediaSources[bannerList.size()] = new ExtractorMediaSource(Uri.parse("https://player.vimeo.com//external//489518156.sd.mp4?s=67025c676de77ca981fe38e1adf9dc95ff393c3f&profile_id=164&oauth2_token_id=1436689020"),
//                    mediaDataSourceFactory, extractorsFactory, null, null);
        }

        if(mediaSources == null){
            mediaSources = new MediaSource[0];
        }

         mMediaSource = new ConcatenatingMediaSource(mediaSources); // mediaSources.length == 1 ? mediaSources[0]
//                : new ConcatenatingMediaSource(mediaSources);
        player.prepare(mMediaSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
        mSimpleExoPlayerView.hideController();
        mSimpleExoPlayerView.setUseController(true);

        player.addVideoListener(new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

            }

            @Override
            public void onRenderedFirstFrame() {

            }
        });
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d(TAG, "onTracksChanged: " + player.getCurrentWindowIndex());
                loadDotIndicator(player.getCurrentWindowIndex());
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

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
                error.printStackTrace();
                Log.i(TAG, "onPlayerError: NewHomeFragment "+error.getMessage());
                Log.i(TAG, "onPlayerError: NewHomeFragment "+error.getLocalizedMessage());
                Log.i(TAG, "onPlayerError: NewHomeFragment "+error.toString());
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
    }

    private void loadChannelVideo(String url, String title, String description) {
        if(player != null){
            player.release();
            mMediaSource.clear();
        }
        mSimpleExoPlayerView.setUseController(false);
        mMainFragment.findViewById(R.id.dot_indicator_ll).setVisibility(View.GONE);
        btnGoToChannel.setVisibility(View.GONE);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "NCG"));
        if(url == null){
            url="";
        }
//        url = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
        MediaSource mediaSource =  new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);  //new ConcatenatingMediaSource(mediaSources);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(SimpleExoPlayer.REPEAT_MODE_ALL);
//        mSimpleExoPlayerView.hideController();
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
                Log.i(TAG, "onPlayerError: newHomeFragment "+error.getMessage());
                Log.i(TAG, "onPlayerError: newHomeFragment "+error.getLocalizedMessage());
                Log.i(TAG, "onPlayerError: newHomeFragment "+error.toString());

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
    private void setKeyEvent() {

//        btnMyList.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (i == KeyEvent.KEYCODE_DPAD_LEFT && btnGoToChannel.getVisibility() == View.GONE) {
//                        navigationMenuCallback.navMenuToggle(true);
//                        return true;
//                    }
//                    if(i == KeyEvent.KEYCODE_DPAD_RIGHT || i == KeyEvent.KEYCODE_DPAD_UP){
//                        mSimpleExoPlayerView.showController();
//                        mSimpleExoPlayerView.requestFocus();
//                        return true;
////                        mSimpleExoPlayerView.requestChildFocus(mSimpleExoPlayerView.findViewById(R.id.exo_next),mSimpleExoPlayerView.findViewById(R.id.exo_next));
//                    }
//                    if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
//                        if(homeRow != null){
//                            homeRow.setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(0){
//                                @Override
//                                public void run(Presenter.ViewHolder holder) {
//                                    super.run(holder);
//                                    if(holder != null){
//                                        holder.view.postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                holder.view.requestFocus();
//                                            }
//                                        }, 10);
//                                    }
//
//                                }
//                            });
//                            return true;
//                        }
//                    }
//                }
//                return false;
//            }
//        });
        btnGoToChannel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
                        navigationMenuCallback.navMenuToggle(true);
                        return true;
                    }
                    if(i == KeyEvent.KEYCODE_DPAD_UP){
                        mSimpleExoPlayerView.showController();
                        mSimpleExoPlayerView.requestFocus();
                        return true;
                    }
                    if (i == KeyEvent.KEYCODE_ENTER || i == KeyEvent.KEYCODE_DPAD_CENTER) {
                     try{
                         List<Banner> bannerList=  mHomeData.getBanner();
                         if(player.getCurrentWindowIndex() < bannerList.size()) {
                             Banner banner = bannerList.get(player.getCurrentWindowIndex());
                             if(!isStringEmpty(banner.getChannelName())) {
                                 FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                 ChannelFragment fragment = ChannelFragment.newInstance(banner.getChannelName(), "");
                                 fragment.setNavigationMenuCallback(navigationMenuCallback);
                                 fragmentManager.beginTransaction()
                                         .replace(R.id.main_FL, fragment)
                                         .addToBackStack("amit")
                                         .commit();
                                 return true;
                             }
                         }
                     }catch (Exception e){
                         e.printStackTrace();
                     }
                    }
                    if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if(homeRow != null){
                            homeRow.setSelectedPosition(0, true, new ListRowPresenter.SelectItemViewHolderTask(0){
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

        mSimpleExoPlayerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if(btnGoToChannel.getVisibility() == View.VISIBLE) {
                            btnGoToChannel.requestFocus();
                        }else{
//                            btnMyList.requestFocus();
                            btnGoToChannel.requestFocus();
                        }
                        return true;
                    }
                }
                return false;
            }
        });
//        if(mMainFragment instanceof ScrollView){
//
//            ((ScrollView)mMainFragment).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                @Override
//                public void onScrollChanged() {
//                    int movement = ((ScrollView)mMainFragment).getScrollY();
//                    Log.d(TAG, "onScrollChanged: " + movement);
////                    if(movement > 350 && movement<500){
////                        btnGoToChannel.setVisibility(View.GONE);
////                        btnMyList.setVisibility(View.GONE);
////                        mMainFragment.findViewById(R.id.dot_indicator_ll).setVisibility(View.GONE);
////                    }else{
////                        btnGoToChannel.setVisibility(View.VISIBLE);
////                        btnMyList.setVisibility(View.VISIBLE);
////                        mMainFragment.findViewById(R.id.dot_indicator_ll).setVisibility(View.VISIBLE);
////                    }
//                    int height = mSimpleExoPlayerView.getHeight();
//                    Log.d(TAG, "onScrollChanged: heignt" + height);
//                    if (movement >= 0 && movement <= height && player!= null) {
//                        /*for image parallax with scroll */
//                        mSimpleExoPlayerView.setTranslationY(-movement/2 );
//
//
////                        mRowFrame.setTranslationY(-movement/2);
//
////                        mViewRL.setTranslationY(-movement/2);
//                        /* set visibility */
////                        heading.setAlpha(alphaFactor);
////                        player.setPlayWhenReady(false);
//                    }
//
//                    if(movement > 0) {
//                        int top = mSimpleExoPlayerView.getHeight() - (movement / 2);
//                        Log.d(TAG, "onScrollChanged: topMarging " + top);
//                        if (movement > 0 && top < 440) {
//                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mRowFrame.getLayoutParams();
//
//                            params.setMargins(0, top, 0, 0);
//                            mRowFrame.setLayoutParams(params);
//                        }
//                    }
////                    if(movement == 0){
////                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mRowFrame.getLayoutParams();
////
////                        params.setMargins(0, 450, 0, 0);
////                        mRowFrame.setLayoutParams(params);
////                    }
////                    else if(movement < 40){
////                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mRowFrame.getLayoutParams();
////
////                        params.setMargins(0, 450, 0, 0);
////                        mRowFrame.setLayoutParams(params);
//
////                    }
//
//                    if(movement>164 && movement <= 500 ){
////                        Animation anim = new ScaleAnimation(
////                                1f, 1f, // Start and end values for the X axis scaling
////                                -movement/2, movement/2, // Start and end values for the Y axis scaling
////                                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
////                                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
////                        anim.setFillAfter(true); // Needed to keep the result of the animation
////                        anim.setDuration(1000);
////                        mRowFrame.startAnimation(anim);
////                        mRowFrame.setTranslationY(-movement/2);
////                        mRowFrame.setScaleY(movement/2);
////                        mRowFrame.setTranslationY(movement/2);
//                    }
//                }
//            });
//            ((ScrollView)mMainFragment).getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver.OnWindowFocusChangeListener() {
//                @Override
//                public void onWindowFocusChanged(boolean b) {
//                    Log.d(TAG, "onWindowFocusChanged: " + b);
//                }
//            });
//        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(player != null && getActivity()!= null){
            player.setPlayWhenReady(true);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(player != null){
            player.setPlayWhenReady(false);
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

    private void releasePlayer() {
        if(player != null){
            player.release();
        }
    }
    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        Log.d(TAG, "onAttachFragment: ");
        if(childFragment instanceof HomeRow){
            Log.d(TAG, "onAttachFragment: " + "HomeRow");
            ((HomeRow)childFragment).setNavigationMenuCallback(this);
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        if(player != null){
            player.setPlayWhenReady(false);
            releasePlayer();
        }
    }

    private void callAPI() {
        Log.d(TAG, "callAPI: ");
        mDialog.show();
        HomePageRequest req = new HomePageRequest();
        req.setLang("en");
        Log.i(TAG, "callAPI: "+req.toString());

        ApiClient.getClient(context).create(ApiInterface.class)
                .getHomePage(req).enqueue(this);
    }

    @Override
    public void onResponse(Call<HomePageModel> call, Response<HomePageModel> response) {
        mDialog.dismiss();
try{
    if(response.body() != null){
        mHomeData = response.body();
        if(mHomeData.isStatus() || mHomeData.isSuccess()) {
            loadData();
            loadVideo();
        }else if(response.body().getStatusCode() == 403){
            CommonUtility.showToast(mContext,response.body().getMessage());
            TokenStorage.clearSharedToken(mContext);
            Intent intent = new Intent(mContext, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            getActivity().finish();
        }else{
            CommonUtility.showToast(mContext,response.body().getMessage());
        }
    }else {
        CommonUtility.showToast(mContext, ConstantUtility.SERVER_ISSUE);
    }
    }catch (Exception e){
    e.printStackTrace();
    }
    }

    private void loadData() {
//        getFragmentManager().beginTransaction().add(R.id.videoview_frame, new PlaybackVideoFragment()).commit();
        homeRow = HomeRow.getInstance((Serializable) mHomeData.getCategory(), (Serializable) mHomeData.getMyList());
        homeRow.setNavigationMenuCallback(this);
        homeRow.setRowFragmentStateChangeCallback(this);
        if( getActivity() != null)
        try{
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.row_frame, homeRow).commit();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(mHomeData.getMenuItems() != null && mHomeData.getMenuItems().getCommonMenuItems() != null){
            List<CommonMenuItem> commonMenuItem = mHomeData.getMenuItems().getCommonMenuItems();
            for (int i=0; i<commonMenuItem.size(); i++){
                if(commonMenuItem.get(i).getMainMenu().equalsIgnoreCase("Free Channel") && commonMenuItem.get(i).getSubMenu() != null){
                    this.navigationInitializeListener.initializeSubMenu(commonMenuItem.get(i).getSubMenu().toArray(new String[0]));
                }
            }


        }

//        mViewPageAdapter = new VideoViewPagerAdapter(getContext(), mHomeData.getBanner(), getFragmentManager());
//        mViewPager.setAdapter(mViewPageAdapter);
//        mViewPager.setCurrentItem(0);
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
        if(toShow){
            mRowFrame.clearFocus();
            this.navigationMenuCallback.navMenuToggle(toShow);
        }else {
            mRowFrame.clearFocus();
            btnGoToChannel.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnGoToChannel.requestFocus();
                }
            }, 10);
        }
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }
    public void setNavigationInitializeListener(NavigationInitializeListener navigationInitializeListener){
        this.navigationInitializeListener = navigationInitializeListener;
    }

    @Override
    public void selectedOption(@NotNull Object object) {
        Log.d(TAG, "selectedOption: ");
        if(object instanceof MoreInfo){
            MoreInfo category = (MoreInfo) object;
            if(category.getVideoId() == 0){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                ChannelFragment fragment = ChannelFragment.newInstance(category.getChannelName(), "");
                fragment.setNavigationMenuCallback(this.navigationMenuCallback);
//                fragment.intializeFirstFocus();
                fragmentManager.beginTransaction()
                        .replace(R.id.main_FL, fragment)
                        .addToBackStack("amit")
                        .commit();
            }else{
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                PlaybackFragment fragment = PlaybackFragment.newInstance(category.getVideoId(), category.getImage());
                fragment.setNavigationMenuCallback(this.navigationMenuCallback);
                fragmentManager.beginTransaction()
                        .replace(R.id.main_FL, fragment)
                        .addToBackStack("amit")
                        .commit();
//
//                Intent intent = new Intent(mContext, PlaybackActivity.class);
//                intent.putExtra("VIDEO_ID", category.getVideoId());
//                intent.putExtra("Title", category.getTitle());
//                startActivity(intent);
            }
        }else if(object instanceof String[]){
            String detail[] = (String[]) object;
            loadChannelVideo(detail[0], detail[1], detail[2]);
        }
    }

    @Override
    public void navMenuToggle(int rowIndex, int columnIndex) {
        if(mHomeData != null)
        if(mHomeData.getMyList() != null && mHomeData.getMyList().size()>0){
            rowIndex = rowIndex-1;
        }
        if(rowIndex != -1 && mHomeData.getCategory() != null && mHomeData.getCategory().size() >rowIndex){
            Category category = mHomeData.getCategory().get(rowIndex);
            MoreInfo moreInfo = category.getMoreInfo().get(columnIndex);

           String channel = category.getChannelName();
            Log.d(TAG, "navMenuToggle: " + channel);
            Log.d(TAG, "navMenuToggle moreInfo: " + moreInfo);
            if(moreInfo.getVideoId() == 0) {
                switch (channel) {
                    case Constants.nav_menu_school:
//                        navigationMenuCallback.selectedOption(Constants.nav_menu_school);
                        navigationInitializeListener.changeMenu(moreInfo.getChannelName(), Constants.nav_menu_school);
                        break;
                    case Constants.nav_menu_thematic:
//                        navigationMenuCallback.selectedOption(Constants.nav_menu_thematic);
                        navigationInitializeListener.changeMenu(moreInfo.getChannelName(), Constants.nav_menu_thematic);
                        break;
                    default: {
                        if (channel.equalsIgnoreCase(Constants.nav_menu_free)) {
//                            navigationMenuCallback.selectedOption(Constants.nav_menu_free);
                            navigationInitializeListener.changeMenu(moreInfo.getChannelName(), Constants.nav_menu_free);
                        } else {
                            selectedOption(moreInfo);
                        }
                    }
                }
            }else{
                selectedOption(moreInfo);
            }
        }else if(rowIndex == -1 && mHomeData.getMyList() != null && mHomeData.getMyList().size()>0){
            MoreInfo moreInfo = mHomeData.getMyList().get(columnIndex);
            selectedOption(moreInfo);
        }
    }

    public void intializeFirstFocus() {
//        mSimpleExoPlayerView.requestFocus();
//        mLogo.requestFocus();
        if(btnGoToChannel != null){
            btnGoToChannel.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnGoToChannel.requestFocus();
                }
            }, 10);
        }
    }

    @Override
    public void navMenuToggleSelection(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
    }
}

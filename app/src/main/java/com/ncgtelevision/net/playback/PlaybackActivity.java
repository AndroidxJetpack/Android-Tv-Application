package com.ncgtelevision.net.playback;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.playback.model.Datum;
import com.ncgtelevision.net.playback.model.PlaybackModel;

import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;

public class PlaybackActivity extends FragmentActivity {

    private ProgressDialog mDialog;
    private PlaybackModel mPlaybackModel;
    private SimpleExoPlayer player;
    private String TAG = PlaybackActivity.class.getName();
    private PlayerView mSimpleExoPlayerView;
    private TextView mDescriptionTV;
    private TextView mTitleTV;
    private int controllerVisible = 0;
//    private PrefManager prefManager;
    private String YOUTUBE_VIDEO_ID = "bGt6nedm7Ug";
    private String BASE_URL = "https://www.youtube.com";
    private String mYoutubeLink = BASE_URL + "/watch?v=" + YOUTUBE_VIDEO_ID + "&key=AIzaSyDweVeWxR2A5EzhJDlM98uMs2VdTbkJD90";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback2);
        mDialog = custom_loader(this);
        mSimpleExoPlayerView = findViewById(R.id.video_player_view);
        mTitleTV = (TextView) findViewById(R.id.title_TV);
        mDescriptionTV = (TextView) findViewById(R.id.description_TV);
        callAPI();
//        prefManager = new PrefManager(this);
//        extractYoutubeUrl();
    }

    private void callAPI() {
//        mDialog.show();
        try {
//        PlaybackRequest req = new PlaybackRequest();
//        req.setVideoId(bundle.getInt("VIDEO_ID",0));
            //mPlaybackModel = (PlaybackModel) ;
            String url = getIntent().getStringExtra("VIDEO_URL");
            if (url == null)
                return;
            loadVideo(url);  // https://youtu.be/bGt6nedm7Ug
           /* if (mPlaybackModel.getData().size() > 0) {
                //     String url = mPlaybackModel.getData().get(0).getVimeoUrl();
//                if(CommonUtility.isStringEmpty(url)){  /// (MatroskaExtractor, FragmentedMp4Extractor, Mp4Extractor, Mp3Extractor, AdtsExtractor, Ac3Extractor, TsExtractor, FlvExtractor, OggExtractor, PsExtractor, WavExtractor, AmrExtractor)
//                    extractYoutubeUrl();
//                }else{
//
//                }
            } else {
                mDescriptionTV.setText(mPlaybackModel.getMessage());
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  loadVideo("https://player.vimeo.com/external/507906687.hd.mp4?s=0701f119a1931f674852587afc9db939a23816a4&profile_id=175&oauth2_token_id=1436689020");
        //loadVideo(prefManager.getVideoId());
//        ApiClient.getClient(this).create(ApiInterface.class)
//                .getVideo(req).enqueue(this);
    }


    private void loadVideo(String url) {
        Log.d(TAG, "loadVideo: " + url);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, new DefaultLoadControl());

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "NCG"));
//        url = "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);  //new ConcatenatingMediaSource(mediaSources);

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
//        mSimpleExoPlayerView.hideController();
//        initializeTitle();
        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

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
                Log.i(TAG, "onPlayerError: " + error.getMessage());
                 Toast.makeText(PlaybackActivity.this, "Server issue.. please try later :)", Toast.LENGTH_SHORT).show();
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
                Log.d(TAG, "onVisibilityChange: " + visibility);
                controllerVisible = visibility;
            }
        });

    }

    private void extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                Log.d(TAG, "onExtractionComplete: " + sparseArray);
                if (sparseArray != null) {
                    Log.d(TAG, "onExtractionComplete:  " + sparseArray);
                    for (int i = 0; i < sparseArray.size(); i++) {
                        Log.d(TAG, "onExtractionComplete: " + sparseArray.get(i).getUrl());
                    }
//                    loadVideo(sparseArray.get(17).getUrl());
                }
            }
        };
        mExtractor.extract("https://youtu.be/bGt6nedm7Ug", true, true);
    }
//    private void playVideo(String downloadUrl) {
//        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
//        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(MainActivity.this).getPlayerView().getPlayer());
//        ExoPlayerManager.getSharedInstance(MainActivity.this).playStream(downloadUrl);
//
//    }

    private void initializeTitle() {
        List<Datum> datumList = mPlaybackModel.getData();
        Datum data = datumList.get(0);
        mTitleTV.setText(data.getTitle());
        mDescriptionTV.setText(data.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
        }
    }

   /* @Override
    public void onResponse(Call<PlaybackModel> call, Response<PlaybackModel> response) {
        try {
            mDialog.dismiss();
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().isSuccess() || response.body().isStatus()) {
                    CommonUtility.showToast(PlaybackActivity.this, response.body().getMessage());
                    mPlaybackModel = response.body();
                    if (mPlaybackModel.getData().size() > 0) {
                        loadVideo("");
                    } else {
                        mDescriptionTV.setText(mPlaybackModel.getMessage());
                    }
                } else {
                    CommonUtility.showToast(PlaybackActivity.this, response.body().getMessage());
                    if (response.body().getStatusCode() == 403) {
                        Intent intent = new Intent(this, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        mDescriptionTV.setText(response.body().getMessage());
                    }
                }

            } else if (response.code() == 500) {
                CommonUtility.showToast(PlaybackActivity.this, ConstantUtility.SERVER_ISSUE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && mSimpleExoPlayerView != null && keyCode != KeyEvent.KEYCODE_BACK) {
            mSimpleExoPlayerView.showController();

//            mSimpleExoPlayerView.requestFocus();
        }
        return super.onKeyDown(keyCode, event);
    }

/*    @Override
    public void onFailure(Call<PlaybackModel> call, Throwable t) {
        t.printStackTrace();
    }*/

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }
}

package com.ncgtelevision.net.playback;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.playback.model.Datum;

import org.json.JSONArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IframePlaybackActivity2 extends FragmentActivity {
    //    WebView mWebView;
    PlayerView playerView;
    WebView mWebView;
    String TAG="IframePlaybackActivity2";
    String video;
    String video2;
    ExoPlayer player;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iframe_playback2);
        playerView = findViewById(R.id.video_player_view2);
        mWebView = findViewById(R.id.webview2);
        loadIframe();

    }

    private void loadIframe() {

        Intent intent = getIntent();
        video=   intent.getStringExtra("VIDEO_ID");
        video2=video;
        Log.i(TAG, "loadIframe: IframePlaybackActivity2 "+video);
        Pattern pattern = Pattern.compile("https(.*?)m3u8", Pattern.UNIX_LINES);
        Matcher matcher = pattern.matcher(video);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            video="https"+matcher.group(1).toString()+"m3u8";
            exoPlayer();
        }


        Pattern pattern2 = Pattern.compile("src=\"(.*?)\"", Pattern.DOTALL);
        Matcher matcher2 = pattern2.matcher(video);
        if (matcher2.find()) {
            System.out.println(matcher2.group(1));
            video=matcher2.group(1).toString();

            AndroidNetworking.get(video)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {

                        @Override
                        public void onResponse(String response) {
                            String responseData=response.toString();
                            Pattern pattern3 = Pattern.compile("https(.*?)m3u8", Pattern.COMMENTS);
                            Matcher matcher3 = pattern3.matcher(response);
                            if (matcher3.find()) {
                                System.out.println(matcher3.group(1));
                          String newVideoUrl=matcher3.group(1).toString();
                                video="https"+newVideoUrl+"m3u8";
                                exoPlayer();
                            }


                           else if (video2.startsWith("<iframe") || video2.startsWith("<div"))
                            {
                                mWebView.setVisibility(View.VISIBLE);
                                playerView.setVisibility(View.GONE);
                                mWebView.setInitialScale(1);
                                mWebView.setWebChromeClient(new WebChromeClient());
                                mWebView.getSettings().setAllowFileAccess(true);
                                mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                                mWebView.setWebViewClient(new WebViewClient());
                                mWebView.getSettings().setJavaScriptEnabled(true);
                                mWebView.getSettings().setLoadWithOverviewMode(true);
                                mWebView.getSettings().setUseWideViewPort(true);
                                mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                                DisplayMetrics displaymetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                int height = displaymetrics.heightPixels;
                                int width = displaymetrics.widthPixels;

                                String mVideo="<iframe allow=\"autoplay\"  autoplayoncanplay=\"this.muted=true\" width=\"100%\" height=\"100%\" src=\""+video+"\"></iframe>";


                                mWebView.setWebViewClient(new WebViewClient() {
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);
                                        return true;
                                    }

                                    public void onPageFinished(WebView view, String url) {

                                    }

                                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                        Log.e(TAG, "Error: " + description);

                                    }
                                });

                                Log.i(TAG, "loadIframe: change element "+video);

                                mWebView.loadDataWithBaseURL(null, mVideo , "text/html",  "UTF-8", null);
                            }

                        }

                        @Override
                        public void onError(ANError error) {
                            Log.i(TAG, "onResponse: iframe error "+error.getMessage());
                        }
                    });

        }
        else if (video2.startsWith("<iframe") || video2.startsWith("<div"))
        {
            Pattern pattern3 = Pattern.compile("src=\'(.*?)\'", Pattern.DOTALL);
            Matcher matcher3 = pattern3.matcher(video);
            if (matcher3.find()) {
                System.out.println(matcher3.group(1));
                video = matcher3.group(1).toString();

                mWebView.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                mWebView.setInitialScale(1);
                mWebView.setWebChromeClient(new WebChromeClient());
                mWebView.getSettings().setAllowFileAccess(true);
                mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
                mWebView.setWebViewClient(new WebViewClient());
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setLoadWithOverviewMode(true);
                mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                mWebView.getSettings().setUseWideViewPort(true);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;

                String mVideo="<iframe allow=\"autoplay\" autoplayoncanplay=\"this.muted=true\" width=\"100%\" height=\"100%\" src=\""+video+"\"></iframe>";


                mWebView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }

                    public void onPageFinished(WebView view, String url) {

                    }

                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        Log.e(TAG, "Error: " + description);

                    }
                });


                mWebView.loadDataWithBaseURL(null, mVideo , "text/html",  "UTF-8", null);
            }

        }

    }
    @Override
    public void onStart() {
        super.onStart();

    }
    public void exoPlayer()
    {

        TrackSelection.Factory adaptiveTrackSelection = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(adaptiveTrackSelection),
                new DefaultLoadControl());

        playerView.setPlayer(player);
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter);

        String hls_url =video;
        Uri uri = Uri.parse(hls_url);
        Handler mainHandler = new Handler();
        MediaSource mediaSource = new HlsMediaSource(uri,
                dataSourceFactory, mainHandler, null);
//                HlsMediaSource mediaSource =new HlsMediaSource.Factory(dataSourceFactory)
//
//                .createMediaSource(uri);
        player.prepare(mediaSource);
        player.setPlayWhenReady(playWhenReady);
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
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
//                        loading.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
//                        loading.setVisibility(View.VISIBLE);
                        break;
                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }
            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }
            @Override
            public void onPlayerError(ExoPlaybackException error) {
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
        player.seekTo(currentWindow, playbackPosition);
        player.prepare(mediaSource, true, false);

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

}
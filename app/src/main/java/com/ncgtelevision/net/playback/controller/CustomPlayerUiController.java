package com.ncgtelevision.net.playback.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.ncgtelevision.net.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBar;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.views.YouTubePlayerSeekBarListener;


public class CustomPlayerUiController extends AbstractYouTubePlayerListener implements YouTubePlayerFullScreenListener {

    private final View playerUi;
    private final String TAG = "CustomPlayerUiControlle";

    private Context context;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;

    // panel is used to intercept clicks on the WebView, I don't want the user to be able to click the WebView directly.
    private View panel;
    private View progressbar;
    private TextView videoCurrentTimeTextView;
    private TextView videoDurationTextView;

    private final YouTubePlayerTracker playerTracker;
    private boolean fullscreen = false;
    private ImageButton playButton;
    private ImageButton pauseButton;
    private SeekBar seekbar;
    private LinearLayout showProgressLL;
    private boolean isVisible;
    private Handler mHandler, handler;
    double current_pos, total_duration;
    boolean firstTimeLoadIntialize = false;

    public CustomPlayerUiController(Context context, View customPlayerUi, YouTubePlayer youTubePlayer, YouTubePlayerView youTubePlayerView) {
        this.playerUi = customPlayerUi;
        this.context = context;
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayerView = youTubePlayerView;

        playerTracker = new YouTubePlayerTracker();
        youTubePlayer.addListener(playerTracker);

        initViews(customPlayerUi);
    }

    private void initViews(View playerUi) {
//        panel = playerUi.findViewById(R.id.panel);
        progressbar = playerUi.findViewById(R.id.progressbar);
        videoCurrentTimeTextView = playerUi.findViewById(R.id.exo_position);
        videoDurationTextView = playerUi.findViewById(R.id.exo_duration);
         playButton = (ImageButton) playerUi.findViewById(R.id.exo_play);
         pauseButton = (ImageButton) playerUi.findViewById(R.id.exo_pause);
         pauseButton.setVisibility(View.GONE);
         seekbar  = (SeekBar) playerUi.findViewById(R.id.seekbar);
        showProgressLL = (LinearLayout) playerUi.findViewById(R.id.showProgress);
//         youTubePlayerSeekBar = (YouTubePlayerSeekBar)  pYouTubePlayerSeekBarlayerUi.findViewById(R.id.youtube_player_seekbar);
//        youTubePlayer.addListener(youTubePlayerSeekBar);
//        youTubePlayerSeekBar.setYoutubePlayerSeekBarListener(this);
//        youTubePlayerSeekBar.setShowBufferingProgress(false);
        mHandler = new Handler();
        handler = new Handler();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayer.play();
                view.setVisibility(View.GONE);
                pauseButton.setVisibility(View.VISIBLE);
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayer.pause();
                view.setVisibility(View.GONE);
                playButton.setVisibility(View.VISIBLE);
            }
        });
//        setVideoProgress();
//        hideLayout();

//        playPauseButton.setOnClickListener( (view) -> {
//            if(playerTracker.getState() == PlayerConstants.PlayerState.PLAYING) {
//                youTubePlayer.pause();
//            }else{
//                youTubePlayer.play();
//            }
//        });

//        enterExitFullscreenButton.setOnClickListener( (view) -> {
//            if(fullscreen) youTubePlayerView.exitFullScreen();
//            else youTubePlayerView.enterFullScreen();
//
//            fullscreen = !fullscreen;
//        });
    }

    // display video progress
    public void setVideoProgress() {
        //get the video duration
        if(firstTimeLoadIntialize){
            return;
        }
        current_pos = playerTracker.getCurrentSecond();
        total_duration = playerTracker.getVideoDuration();
        Log.d(TAG, "setVideoProgress: " +total_duration);

        //display video duration
        videoCurrentTimeTextView.setText(timeConversion((long) current_pos));
        videoDurationTextView.setText(timeConversion((long) total_duration));
        seekbar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = playerTracker.getCurrentSecond();
                    videoCurrentTimeTextView.setText(timeConversion((long) current_pos));
                    seekbar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed){
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

        //seekbar change listner
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                youTubePlayer.seekTo((int) current_pos);
            }
        });
    }

    //time conversion
    public String timeConversion(long value) {
        String songTime;
        int dur = (int) value;
        int hrs = (dur / 3600); // 1
        int mns = (dur % 3600) / 60; // 30
        int scs = (dur % 3600) % 60;

        if (hrs > 0) {
            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            songTime = String.format("%02d:%02d", mns, scs);
        }
        return songTime;
    }
    public void hideLayout() {

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                showProgressLL.setVisibility(View.GONE);
//                fullImage.setVisibility(View.GONE);
                isVisible = false;
            }
        };
        handler.postDelayed(runnable, 5000);
        youTubePlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                if (isVisible) {
                    showProgressLL.setVisibility(View.GONE);
                    isVisible = false;
                } else {
                    showProgressLL.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(runnable, 5000);
                    isVisible = true;
                }
            }
        });
    }

    @Override
    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
        Log.d(TAG, "onReady: ");
        progressbar.setVisibility(View.GONE);
    }

    @Override
    public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
        Log.d(TAG, "onStateChange: ");
//        if(state == PlayerConstants.PlayerState.PLAYING){
//            showProgressLL.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.GONE);
//            playButton.setVisibility(View.GONE);
//            pauseButton.setVisibility(View.VISIBLE);
//
//        }else if(state == PlayerConstants.PlayerState.PAUSED) {
//            showProgressLL.setVisibility(View.VISIBLE);
//            progressbar.setVisibility(View.GONE);
//            playButton.setVisibility(View.VISIBLE);
//            pauseButton.setVisibility(View.GONE);
//
//        } else
        if(state == PlayerConstants.PlayerState.BUFFERING){
            progressbar.setVisibility(View.VISIBLE);
        }else{
            progressbar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
//        long currentDuration = (long) second/1000;
//        videoCurrentTimeTextView.setText(currentDuration+"");
//        seekbar.setProgress((int) currentDuration);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float duration) {
//        long totalDuration = (long) (duration/1000);
//        videoDurationTextView.setText(totalDuration+"");
//        seekbar.setMax((int) totalDuration);
        Log.d(TAG, "onVideoDuration: ");
    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {
//        ViewGroup.LayoutParams viewParams = playerUi.getLayoutParams();
//        viewParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        playerUi.setLayoutParams(viewParams);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
//        ViewGroup.LayoutParams viewParams = playerUi.getLayoutParams();
//        viewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        viewParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        playerUi.setLayoutParams(viewParams);
    }

}
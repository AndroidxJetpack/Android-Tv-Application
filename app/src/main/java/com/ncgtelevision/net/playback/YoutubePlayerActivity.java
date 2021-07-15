//package com.ncgtelevision.net.playback;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentActivity;
//
//import android.app.FragmentTransaction;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.PersistableBundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.ncgtelevision.net.R;
//import com.ncgtelevision.net.playback.controller.CustomPlayerUiController;
//import com.ncgtelevision.net.playback.model.Datum;
//import com.ncgtelevision.net.utilities.CommonUtility;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
//import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;
//
//import org.jetbrains.annotations.NotNull;
//
//import fr.bmartel.youtubetv.YoutubeTvFragment;
//import fr.bmartel.youtubetv.YoutubeTvView;
//import fr.bmartel.youtubetv.listener.IPlayerListener;
//import fr.bmartel.youtubetv.model.VideoControls;
//import fr.bmartel.youtubetv.model.VideoInfo;
//import fr.bmartel.youtubetv.model.VideoState;
//
//public class YoutubePlayerActivity extends FragmentActivity {
//
//    private YouTubePlayerView youTubePlayerView;
//    private String TAG =  "YoutubePlayerActivity";
//    private CustomPlayerUiController customPlayerUiController;
//    private YoutubeTvView mYouTubePlayerViewNe;
//    private final static int POSITION_OFFSET = 5;
//    private long current_pos=0;
//    TextView videoCurrentTimeTextView;
//    TextView videoDurationTextView;
//    private SeekBar seekbar;
//    private boolean isFirstTime = false;
//    private LinearLayout showProgressLL;
//    private Handler handler;
//    private ImageButton playBtn, pauseBtn;
//    private Runnable runnable;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_youtube_player);
//         mYouTubePlayerViewNe = (YoutubeTvView) findViewById(R.id.youtube_video);
//        Bundle bundle = getIntent().getExtras();
//        String videoId;
//                videoId = bundle.getString("VIDEO_ID");
//
//
////        Bundle bundle = getIntent().getExtras();
////
////        String videoId;
////
////           Datum datum = (com.ncgtelevision.net.playback.model.Datum) bundle.getParcelable("playbackData");
////            videoId=datum.getYoutubeId();
//
//
//        mYouTubePlayerViewNe.playVideo(videoId);
//        videoCurrentTimeTextView = findViewById(R.id.exo_position);
//        videoDurationTextView = findViewById(R.id.exo_duration);
//        seekbar  = (SeekBar) findViewById(R.id.seekbar);
//        showProgressLL = (LinearLayout) findViewById(R.id.showProgress);
//        handler = new Handler();
//
//        mYouTubePlayerViewNe.addPlayerListener(new IPlayerListener() {
//            @Override
//            public void onPlayerReady(VideoInfo videoInfo) {
//
//            }
//
//            @Override
//            public void onPlayerStateChange(VideoState state, long position, float speed, float duration, VideoInfo videoInfo) {
//                Log.i(TAG, "onPlayerStateChange : " + state.toString() + " | position : " + position + " | speed : " + speed + "duration" + duration);
//                videoCurrentTimeTextView.setText(timeConversion((long) position/1000));
//                videoDurationTextView.setText(timeConversion((long) duration));
//                if(!isFirstTime) {
//                    seekbar.setMax((int) duration);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        seekbar.setProgress(0, true);
//                    }else{
//                        seekbar.setProgress(0);
//                    }
//                }
//                if(state == VideoState.PLAYING){
//                    setVideoProgress();
//
//                }
//            }
//        });
//         playBtn = (ImageButton) findViewById(R.id.exo_play);
//         pauseBtn = (ImageButton) findViewById(R.id.exo_pause);
//
//        playBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mYouTubePlayerViewNe.start();
//                view.setVisibility(View.GONE);
//                pauseBtn.setVisibility(View.VISIBLE);
//            }
//        });
//        playBtn.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if(i== KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER){
//                        mYouTubePlayerViewNe.start();
//                        view.setVisibility(View.GONE);
//                        pauseBtn.setVisibility(View.VISIBLE);
//                        pauseBtn.requestFocus();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//        pauseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mYouTubePlayerViewNe.pause();
//                view.setVisibility(View.GONE);
//                playBtn.setVisibility(View.VISIBLE);
//            }
//        });
//        pauseBtn.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if(i== KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER){
//                        mYouTubePlayerViewNe.pause();
//                        view.setVisibility(View.GONE);
//                        playBtn.setVisibility(View.VISIBLE);
//                        playBtn.requestFocus();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//
//        ImageButton backwardBtn = (ImageButton) findViewById(R.id.exo_rew);
//        backwardBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mYouTubePlayerViewNe.moveBackward(POSITION_OFFSET);
//            }
//        });
//        backwardBtn.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if(i== KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER){
//                        mYouTubePlayerViewNe.moveBackward(POSITION_OFFSET);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//        ImageButton forwardBtn = (ImageButton) findViewById(R.id.exo_ffwd);
//        forwardBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mYouTubePlayerViewNe.moveForward(POSITION_OFFSET);
//            }
//        });
//        forwardBtn.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
//                    if(i== KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER){
//                        mYouTubePlayerViewNe.moveForward(POSITION_OFFSET);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//        pauseBtn.requestFocus();
//        requestVisibility();
//    }
//
//    // display video progress
//    public void setVideoProgress() {
//        if(isFirstTime){
//            return;
//        }else{
//            isFirstTime = true;
//        }
//        Log.d(TAG, "setVideoProgress:  " + mYouTubePlayerViewNe.getDuration());
//
//        final Handler handler = new Handler();
//
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
////                Log.d(TAG, "run: setVideoProgress "+mYouTubePlayerViewNe.getCurrentPosition());
//
//                try {
//                    current_pos = (long) mYouTubePlayerViewNe.getCurrentPosition();
//                    videoCurrentTimeTextView.setText(timeConversion((long) current_pos));
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        seekbar.setProgress((int) current_pos);
//                    }else{
//                        seekbar.setProgress((int) current_pos);
//                    }
//                    handler.postDelayed(this, 300);
//                } catch (IllegalStateException ed){
//                    ed.printStackTrace();
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//
//        //seekbar change listner
//        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                current_pos = seekBar.getProgress();
//                mYouTubePlayerViewNe.seekTo((int) current_pos);
//            }
//        });
//    }
//
//
//    //time conversion
//    public String timeConversion(long value) {
//        String songTime;
//        int dur = (int) value;
//        int hrs = (dur / 3600); // 1
//        int mns = (dur % 3600) / 60; // 30
//        int scs = (dur % 3600) % 60;
//
//        if (hrs > 0) {
//            songTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
//        } else {
//            songTime = String.format("%02d:%02d", mns, scs);
//        }
//        return songTime;
//    }
//
//    private void loadPlayer() {
//        Bundle bundle = getIntent().getExtras();
//       final String videoId = bundle.getString("VIDEO_ID");
//
////    youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
//        getLifecycle().addObserver(youTubePlayerView);
////        View customPlayerUi = youTubePlayerView.inflateCustomPlayerUi(R.layout.youtbe_player_custom_controll_playback);
////        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
////            @Override
////            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//////                String videoId = "bGt6nedm7Ug"; //bGt6nedm7Ug
////                Log.d(TAG, "onReady: ");
////                customPlayerUiController = new CustomPlayerUiController(YoutubePlayerActivity.this, customPlayerUi, youTubePlayer, youTubePlayerView);
////                youTubePlayer.addListener(customPlayerUiController);
////                youTubePlayerView.addFullScreenListener(customPlayerUiController);
////                youTubePlayer.loadVideo(videoId, 0);
////                customPlayerUiController.hideLayout();
//////                customPlayerUiController.setVideoProgress();
////
//////                youTubePlayer.play();
////            }
////        });
//        PlayerUiController playerUiController =  youTubePlayerView.getPlayerUiController();
//        playerUiController.showVideoTitle(false);
//        playerUiController.showYouTubeButton(false);
//        playerUiController.showBufferingProgress(false);
//        playerUiController.showFullscreenButton(false);
//
//        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
//            @Override
//            public void onYouTubePlayer(@NotNull YouTubePlayer youTubePlayer) {
////                customPlayerUiController = new CustomPlayerUiController(YoutubePlayerActivity.this, customPlayerUi, youTubePlayer, youTubePlayerView);
////                youTubePlayer.addListener(customPlayerUiController);
////                youTubePlayerView.addFullScreenListener(customPlayerUiController);
//                youTubePlayer.loadVideo(videoId, 0);
////                customPlayerUiController.hideLayout();
////                customPlayerUiController.setVideoProgress();
//            }
//        });
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if(mYouTubePlayerViewNe != null &&  mYouTubePlayerViewNe.getMediaSession() != null) {
//            mYouTubePlayerViewNe.closePlayer();
//        }
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//    }
//    @Override
//    public boolean onKeyDown(int key, KeyEvent keyEvent){
//        if( showProgressLL.getVisibility() == View.GONE && key != KeyEvent.KEYCODE_BACK) {
//            requestVisibility();
//            if(playBtn.getVisibility() == View.VISIBLE){
//                playBtn.requestFocus();
//            }else{
//                pauseBtn.requestFocus();
//            }
//            return true;
//        }else if(key != KeyEvent.KEYCODE_BACK){
//            if(handler != null && runnable != null){
//                handler.removeCallbacks(runnable);
//            }
//            requestVisibility();
//        }
//        return super.onKeyDown(key, keyEvent);
//    }
//
//    private void requestVisibility() {
//        showProgressLL.setVisibility(View.VISIBLE);
//         runnable = new Runnable() {
//            @Override
//            public void run() {
//                showProgressLL.setVisibility(View.GONE);
//            }
//        };
//        handler.postDelayed(runnable, 5000);
//    }
//
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.clear();
//    }
//
//
//}
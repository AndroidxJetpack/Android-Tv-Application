package com.ncgtelevision.net.home_screen.presenters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.ncgtelevision.net.PlaybackVideoFragment;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.home_screen.model.Banner;

import java.util.List;

public class VideoViewPagerAdapter extends PagerAdapter {
    private final List<Banner> mVideo;
    private final Context mContext;
    private final FragmentManager mFragmentManager;

    public VideoViewPagerAdapter(Context context, List<Banner> videos, FragmentManager fragmentManager){
        mContext = context;
        mVideo = videos;
        mFragmentManager = fragmentManager;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.video_layout, container, false);

        Banner video = mVideo.get(position);

//        VideoView videoView = (VideoView) view.findViewById(R.id.video_view);
//        FrameLayout videoFrag = view.findViewById(R.id.videoview_frame);
//        mFragmentManager.beginTransaction().add(R.id.videoview_frame, new PlaybackVideoFragment()).commit();
        SimpleExoPlayerView simpleExoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.video_player_view);
        playerInitialize(simpleExoPlayerView, video.getVimeoUrl());

//        if(video.getVimeoUrl() != null) {
//            Uri videoUri = Uri.parse(video.getVimeoUrl());
//            videoView.setVideoURI(videoUri);
//            videoView.setMediaController(null);
////            videoView.requestFocus();
//            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                public void onPrepared(MediaPlayer mp) {
//                    videoView.start();
//                    mp.setLooping(true);
//                }
//            });
//        }

        container.addView(view);
        return view;
    }
    @Override
    public int getCount() {
        if(mVideo != null){
           return mVideo.size();
        }
        return 0;
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void playerInitialize(SimpleExoPlayerView simpleExoPlayerView, String url){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(mContext, Util.getUserAgent(mContext, "NCG"));

        MediaSource mediaSource;
        mediaSource = new ExtractorMediaSource(Uri.parse(url),
                    mediaDataSourceFactory, extractorsFactory, null, null);
            player.setPlayWhenReady(true);
            player.prepare(mediaSource);
        simpleExoPlayerView.hideController();

        if (simpleExoPlayerView != null) simpleExoPlayerView.setPlayer(player);
    }
}

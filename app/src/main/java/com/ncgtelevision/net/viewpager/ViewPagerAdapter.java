package com.ncgtelevision.net.viewpager;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
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
import com.google.android.exoplayer2.util.Util;
import com.ncgtelevision.net.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<Integer> videoLink;
    private ArrayList<String> sVideoLink;
    private LayoutInflater inflater;
    private Context context;

    public ViewPagerAdapter(Context context, ArrayList<String> sVideoLink) {
        this.sVideoLink = sVideoLink;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    public ViewPagerAdapter(ArrayList<Integer> videoLink, Context context) {
        this.videoLink = videoLink;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return videoLink.size();
        return sVideoLink.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);

        View v=inflater.inflate(R.layout.pager_item,container,false);

//        ImageView myImage=(ImageView)v.findViewById(R.id.image);
//        myImage.setImageResource(videoLink.get(position));

        SimpleExoPlayerView simpleExoPlayerView= (SimpleExoPlayerView) v.findViewById(R.id.player_view);
        playerInitialize(simpleExoPlayerView, String.valueOf(sVideoLink.get(position)));

        container.addView(v,0);

        return  v;

    }


    public void playerInitialize(SimpleExoPlayerView simpleExoPlayerView, String url){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        DataSource.Factory mediaDataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "NCG"));

        MediaSource[] mediaSources = new MediaSource[2];

        mediaSources[0] = new ExtractorMediaSource(Uri.parse(url),
                mediaDataSourceFactory, extractorsFactory, null, null);
        mediaSources[1] = new ExtractorMediaSource(Uri.parse("https://player.vimeo.com/external/314532728.hd.mp4?s=ce9c9b8d3db7104c75f72ae81d934ebc79dd9a25&profile_id=175&oauth2_token_id=1436689020"),
                mediaDataSourceFactory, extractorsFactory, null, null);

        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

//        player.seekTo(position,0);

//        MediaSource mediaSource;
//        mediaSource = new ExtractorMediaSource(Uri.parse(url),
//                mediaDataSourceFactory, extractorsFactory, null, null);
        player.setPlayWhenReady(true);
        player.prepare(mediaSource);
        simpleExoPlayerView.hideController();

        if (simpleExoPlayerView != null) simpleExoPlayerView.setPlayer(player);
    }
}

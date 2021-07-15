package com.ncgtelevision.net;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.leanback.app.VideoSupportFragment;
import androidx.leanback.app.VideoSupportFragmentGlueHost;
import androidx.leanback.media.MediaPlayerAdapter;
import androidx.leanback.media.PlaybackGlue;
import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.media.PlayerAdapter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackSeekDataProvider;

/**
 * Handles video playback with media controls.
 */
public class PlaybackVideoFragment extends VideoSupportFragment {
    private PlaybackControlsRow.RepeatAction repeatAction;
    private PlaybackControlsRow.PictureInPictureAction pipAction;
    private PlaybackControlsRow.ThumbsUpAction thumbsUpAction;
    private PlaybackControlsRow.ThumbsDownAction thumbsDownAction;
    private PlaybackControlsRow.SkipPreviousAction skipPreviousAction;
    private PlaybackControlsRow.SkipNextAction skipNextAction;
    private PlaybackControlsRow.FastForwardAction fastForwardAction;
    private PlaybackControlsRow.RewindAction rewindAction;

    private PlaybackTransportControlGlue<MediaPlayerAdapter> mTransportControlGlue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Ashwini", "onCreate: PlaybackVideoFragment");

        VideoSupportFragmentGlueHost glueHost =
                new VideoSupportFragmentGlueHost(PlaybackVideoFragment.this);

        MediaPlayerAdapter playerAdapter = new MediaPlayerAdapter(getActivity());
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE);

        mTransportControlGlue = new PlaybackTransportControlGlue<>(getActivity(), playerAdapter);
//        mTransportControlGlue = new MyPlaybackTransportControlGlue(getActivity(), playerAdapter);

        mTransportControlGlue.setHost(glueHost);
        mTransportControlGlue.setTitle("kjhkj");
        mTransportControlGlue.setSubtitle("hjgjh");
        mTransportControlGlue.playWhenPrepared();
//        playerAdapter.setDataSource(Uri.parse("https://player.vimeo.com/external/314532728.hd.mp4?s=ce9c9b8d3db7104c75f72ae81d934ebc79dd9a25&profile_id=175&oauth2_token_id=1436689020"));
        playerAdapter.setDataSource(Uri.parse("https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4"));

        mTransportControlGlue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
            @Override
            public void onPreparedStateChanged(PlaybackGlue glue) {
                if (glue.isPrepared()) {
//                    mTransportControlGlue.setSeekProvider(new PlaybackSeekDataProvider());
//                    mTransportControlGlue.play();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransportControlGlue != null) {
            mTransportControlGlue.pause();
        }
    }
    class MyPlaybackTransportControlGlue extends PlaybackTransportControlGlue {
        /**
         * Constructor for the glue.
         *
         * @param context
         * @param impl    Implementation to underlying media player.
         */
        public MyPlaybackTransportControlGlue(Context context, PlayerAdapter impl) {
            super(context, impl);
        }

        @Override
        protected void onCreatePrimaryActions(ArrayObjectAdapter primaryActionsAdapter) {
            // Order matters, super.onCreatePrimaryActions() will create the play / pause action.
            // Will display as follows:
            // play/pause, previous, rewind, fast forward, next
            //   > /||      |<        <<        >>         >|
            super.onCreatePrimaryActions(primaryActionsAdapter);
            primaryActionsAdapter.add(skipPreviousAction);
            primaryActionsAdapter.add(rewindAction);
            primaryActionsAdapter.add(fastForwardAction);
            primaryActionsAdapter.add(skipNextAction);
        }

        @Override
        protected void onCreateSecondaryActions(ArrayObjectAdapter adapter) {
            super.onCreateSecondaryActions(adapter);
            adapter.add(thumbsDownAction);
            adapter.add(thumbsUpAction);
        }
        @Override
        public void onActionClicked(Action action) {
            if (action == rewindAction) {
                // Handle Rewind
            } else if (action == fastForwardAction ) {
                // Handle FastForward
            } else if (action == thumbsDownAction) {
                // Handle ThumbsDown
            } else if (action == thumbsUpAction) {
                // Handle ThumbsUp
            } else {
                // The superclass handles play/pause and delegates next/previous actions to abstract methods,
                // so those two methods should be overridden rather than handling the actions here.
                super.onActionClicked(action);
            }
        }

        @Override
        public void next() {
            // Skip to next item in playlist.
        }

        @Override
        public void previous() {
            // Skip to previous item in playlist.
        }
    }
}

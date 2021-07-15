package com.ncgtelevision.net.playback;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.ncgtelevision.net.playback.model.PlaybackModel;

public class DataFragment extends Fragment {

    // data object we want to retain
    private PlaybackModel data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(PlaybackModel data) {
        this.data = data;
    }

    public PlaybackModel getData() {
        return data;
    }
}

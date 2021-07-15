package com.ncgtelevision.net.home_screen;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.leanback.app.BrowseSupportFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.VideoView;

import com.ncgtelevision.net.R;
import com.ncgtelevision.net.home_screen.model.HomePageModel;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyHomeFragment extends Fragment implements BrowseSupportFragment.MainFragmentAdapterProvider {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private BrowseSupportFragment.MainFragmentAdapter mMainFragmentAdapter = new BrowseSupportFragment.MainFragmentAdapter(this);

    // TODO: Rename and change types of parameters
    private HomePageModel homePageModel;
    private String mParam2;
    private VideoView videoView;

    public MyHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MyHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyHomeFragment newInstance(Serializable param1) {
        MyHomeFragment fragment = new MyHomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            homePageModel =(HomePageModel) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_home2, container, false);
//        videoView = (VideoView) view.findViewById(R.id.videoview);
       // ImageView imageView = view.findViewById(R.id.logo_IB);
         // Animation animation1 = AnimationUtils.loadAnimation(
           //     requireContext(),
             //   R.anim.fade_in
        //);
        //imageView.startAnimation(animation1);
          setVideo(view);
//        WebView webView1 = (WebView) view.findViewById(R.id.webView1);
//        webView1.loadData("<iframe src=\"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4"+"\" width=\"100%\" height=\"100%\" frameborder=\"1\" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>", "text/html", "utf-8");
//        getFragmentManager().beginTransaction().add(R.id.videoview_frame, new PlaybackVideoFragment()).commit();

//        getFragmentManager().beginTransaction().add(R.id.row_frame, HomeRow.getInstance((Serializable) homePageModel.getCategory())).commit();
        return view;
    }

    private void setVideo(View view) {

//          videoView.setVideoURI(Uri.parse("https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4"));
//          videoView.start();
    }

    @Override
    public BrowseSupportFragment.MainFragmentAdapter getMainFragmentAdapter() {
        return mMainFragmentAdapter;
    }
}
package com.ncgtelevision.net.playback;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.channel.ChannelFragment;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.local_storage.TokenStorage;
import com.ncgtelevision.net.playback.model.Datum;
import com.ncgtelevision.net.playback.model.PlaybackModel;
import com.ncgtelevision.net.playback.model.PlaybackRequest;
import com.ncgtelevision.net.retrofit_clients.ApiClient;
import com.ncgtelevision.net.retrofit_clients.ApiInterface;
import com.ncgtelevision.net.signin.SignInActivity;
import com.ncgtelevision.net.utilities.CommonUtility;
import com.ncgtelevision.net.utilities.ConstantUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ncgtelevision.net.utilities.CommonUtility.custom_loader;
import static com.ncgtelevision.net.utilities.CommonUtility.isStringEmpty;
import static com.ncgtelevision.net.utilities.CommonUtility.showToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaybackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaybackFragment extends Fragment implements Callback<PlaybackModel> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static int mVideoId;
    private String mImageUrl;
    private View mFragment;
    private Dialog mDialog;
    private Context mContext;
    private ImageView mVideoImage;
    private TextView mTitleTV;
    private TextView mDescriptionTV;
    private PlaybackModel mPlaybackModel;
    private Button mPlay;
    private Button go_to_channel1;
    private Button watch_other_episodes;
//    private PrefManager prefManager;
    private Button mMyList;
    private NavigationMenuCallback navigationMenuCallback;
//    private DataFragment dataFragment;
    private Datum mPlayBackData;

    public PlaybackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaybackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaybackFragment newInstance(int videoId, String param2) {
        PlaybackFragment fragment = new PlaybackFragment();
        Bundle args = new Bundle();
        mVideoId = videoId;
        if (args != null) {

        }
        args.putInt(ARG_PARAM1, videoId);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mVideoId = getArguments().getInt(ARG_PARAM1);
//            mImageUrl = getArguments().getString(ARG_PARAM2);
//        }
//        Bridge.restoreInstanceState(this,savedInstanceState);
//    }
//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Bridge.saveInstanceState(this,outState);
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Bridge.clear(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragment = inflater.inflate(R.layout.fragment_playback, container, false);
        mContext = getContext();
        mDialog = custom_loader(mContext);
        mVideoImage = (ImageView) mFragment.findViewById(R.id.video_image);
        mTitleTV = (TextView) mFragment.findViewById(R.id.title_TV);
        mDescriptionTV = (TextView) mFragment.findViewById(R.id.description_TV);
        mMyList = (Button) mFragment.findViewById(R.id.my_list);
        mPlay = (Button) mFragment.findViewById(R.id.go_to_channel);
        go_to_channel1 = (Button) mFragment.findViewById(R.id.go_to_channel1);
        watch_other_episodes = (Button) mFragment.findViewById(R.id.watch_other_episodes);
//        prefManager = new PrefManager(requireContext());
//        prefManager.saveVideoId(mPlaybackModel.getData().get(0).getVimeoUrl());


        callAPI();
        initializeKey();
        mPlay.requestFocus();
        return mFragment;
    }

    private void initializeKey() {
//        mPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, YoutubePlayerActivity.class);
//                intent.putExtra("VIDEO_ID", "bGt6nedm7Ug");
//                startActivity(intent);
//            }
//        });
        mPlay.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_DPAD_LEFT) {
                    navigationMenuCallback.navMenuToggle(true);
                    return true;
                }
                if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                    if (mPlaybackModel != null)
                        if (mPlayBackData == null) {
//                               showToast(mContext, mPlaybackModel.getMessage());
                            Intent intent = new Intent(mContext, SubscriptionMessageActivity.class);
                            startActivity(intent);
                        }
                    else if (!CommonUtility.isStringEmpty(mPlayBackData.getYoutubeId())) {
//                           Intent intent = new Intent(mContext, YoutubePlayerActivity.class);
//                               intent.putExtra("VIDEO_URL", mPlaybackModel);
//                               intent.putExtra("Title", mPlayBackData.getTitle());
//                               intent.putExtra("Description", mPlayBackData.getDescription());
//                               intent.putExtra("VIDEO_ID", mPlayBackData.getYoutubeId());
//                               startActivity(intent);
                             CommonUtility.showToast(mContext, "Sorry this video is not available");
                            //intent.putExtra("playbackData",mPlayBackData);

                        }
                    else if (!CommonUtility.isStringEmpty(mPlayBackData.getIframe())) {
                        Intent intent = new Intent(mContext, IframePlaybackActivity2.class);
                         intent.putExtra("VIDEO_ID", mPlayBackData.getIframe());
                         startActivity(intent);
                             //CommonUtility.showToast(mContext, "Sorry this video is not available");
                        }
                    else {
                            Intent intent = new Intent(mContext, PlaybackActivity.class);
                            Log.d("main", "initializeKey: "+ mPlaybackModel.getData().get(0).getVimeoUrl());
                            intent.putExtra("VIDEO_URL", mPlaybackModel.getData().get(0).getVimeoUrl());

//                            intent.putExtra("Title", mPlayBackData.getTitle());
//                            intent.putExtra("Description", mPlayBackData.getDescription());
//                               intent.putExtra("VIDEO_ID", mPlayBackData.getYoutubeId());
                            startActivity(intent);
                        }
                    // Toast.makeText(mContext, "Server issue.. please try later :)", Toast.LENGTH_SHORT).show();
                    //CommonUtility.showToast(mContext,"Server issue,please try later :)");
                }
            }
            return false;
        });
        mMyList.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        if (mPlaybackModel != null)
                            if (mPlayBackData == null) {
                                showToast(mContext, mPlaybackModel.getMessage());
                            } else {
                                handleMyList(mPlayBackData.isIsInMyList());
                            }
                    }
                }
                return false;
            }
        });

        go_to_channel1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        ChannelFragment fragment = ChannelFragment.newInstance("School Channel", "");
                        fragment.setNavigationMenuCallback(navigationMenuCallback);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_FL, fragment)
                                .addToBackStack("amit")
                                .commit();
                    }
                }
                return false;
            }
        });

        watch_other_episodes.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_DPAD_CENTER || i == KeyEvent.KEYCODE_ENTER) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        ChannelFragment fragment = ChannelFragment.newInstance("Balli Caraibici", "");
                        fragment.setNavigationMenuCallback(navigationMenuCallback);
                        fragmentManager.beginTransaction()
                                .replace(R.id.main_FL, fragment)
                                .addToBackStack("amit")
                                .commit();
                    }
                }
                return false;
            }
        });
    }

    private void handleMyList(boolean isInList) {
        mDialog.show();
        if (isInList) {
            PlaybackRequest req = new PlaybackRequest();
            req.setVideoId(mVideoId);
            ApiClient.getClient(mContext).create(ApiInterface.class).removeVideoInList(req).enqueue(new Callback<PlaybackModel>() {
                @Override
                public void onResponse(Call<PlaybackModel> call, Response<PlaybackModel> response) {
                    mDialog.dismiss();
                    if (response.body() != null) {
                        PlaybackModel model = response.body();
                        showToast(mContext, model.getMessage());
                        if (model.isStatus()) {
                            mPlayBackData.setIsInMyList(false);
                            changeMylistIcon();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PlaybackModel> call, Throwable t) {
                    mDialog.dismiss();
                }
            });
        } else {
            PlaybackRequest req = new PlaybackRequest();
            req.setVideoId(mVideoId);
            ApiClient.getClient(mContext).create(ApiInterface.class).addVideoInList(req).enqueue(new Callback<PlaybackModel>() {
                @Override
                public void onResponse(Call<PlaybackModel> call, Response<PlaybackModel> response) {
                    mDialog.dismiss();
                    if (response.body() != null) {
                        PlaybackModel model = response.body();
                        showToast(mContext, model.getMessage());
                        if (model.isStatus()) {
                            mPlayBackData.setIsInMyList(true);
                            changeMylistIcon();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PlaybackModel> call, Throwable t) {
                    mDialog.dismiss();
                }
            });
        }
    }

    private void changeMylistIcon() {
        if (mPlayBackData.isIsInMyList()) {
            mMyList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_btn_focused_with_icon_remove, 0, 0, 0);
        } else {
            mMyList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.home_list_btn_focused_with_icon, 0, 0, 0);
        }
    }

    private void callAPI() {
        mDialog.show();
        PlaybackRequest req = new PlaybackRequest();
        req.setVideoId(mVideoId);
        ApiClient.getClient(mContext).create(ApiInterface.class).getVideo(req).enqueue(this);
    }

    private void initializeView() {
        mPlayBackData = mPlaybackModel.getData().get(0);
        mTitleTV.setText(mPlayBackData.getTitle());
        mDescriptionTV.setText(mPlayBackData.getDescription());
        changeMylistIcon();
        if (mPlayBackData.getImage() != null) {
            Glide.with(mContext)
                    .load(mPlayBackData.getImage())
                    .transform(new CenterCrop())
                    .error(ContextCompat.getDrawable(mContext, R.drawable.movie))
                    .into(mVideoImage);
        }
    }

    @Override
    public void onResponse(Call<PlaybackModel> call, Response<PlaybackModel> response) {
        try {
            mDialog.dismiss();
            if (response.isSuccessful() && response.body() != null) {
                mPlaybackModel = response.body();
                if (response.body().isSuccess() || response.body().isStatus()) {
//                    CommonUtility.showToast(mContext, response.body().getMessage());
                    if (mPlaybackModel.getData().size() > 0) {
                        initializeView();
                    } else {
                        mDescriptionTV.setText(mPlaybackModel.getMessage());
                    }
                } else {
                    CommonUtility.showToast(mContext, response.body().getMessage());
                    if (response.body().getStatusCode() == 403) {
                        TokenStorage.clearSharedToken(mContext);
                        Intent intent = new Intent(mContext, SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        mDescriptionTV.setText(response.body().getMessage());
                        Glide.with(mContext)
                                .load(mImageUrl)
                                .transform(new CenterCrop())
                                .error(ContextCompat.getDrawable(mContext, R.drawable.movie))
                                .into(mVideoImage);
                    }
                }
            } else if (response.code() == 500) {
                CommonUtility.showToast(mContext, ConstantUtility.SERVER_ISSUE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlay != null) {
            mPlay.requestFocus();
        }
    }

    @Override
    public void onFailure(Call<PlaybackModel> call, Throwable t) {
        t.printStackTrace();
        mDialog.dismiss();
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }


}




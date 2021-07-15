package com.ncgtelevision.net.channel;

import android.os.Bundle;
import android.util.Log;

import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.leanback.widget.VerticalGridView;

public class ChannelVerticalFragment extends VerticalGridSupportFragment {
    private static final String TAG = ChannelVerticalFragment.class.getSimpleName();
    private static final int NUM_COLUMNS = 4;
    private ArrayObjectAdapter mAdapter;
//    private LinkedHashMap<String, List<Movie>> mVideoLists = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setTitle("My Beautifull Title");
        setupFragment();
    }

    private void setupFragment() {
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);

//        mAdapter = new ArrayObjectAdapter(new CardPresenter());

        try {
//            load();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        setAdapter(mAdapter);
    }

//    private void load() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://randomuser.me/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        TheRandomUserAPI service = retrofit.create(TheRandomUserAPI.class);
//        Call<RandomUser> repos = service.listUsers(15);
//        repos.enqueue(new Callback<RandomUser>() {
//            @Override
//            public void onResponse(Call<RandomUser> call, Response<RandomUser> response) {
//                List<RandomUser.Result> users = response.body().getResults();
//                for (int j = 0; j < users.size(); j++) {
//                    RandomUser.Result user = users.get(j);
//                    mAdapter.add(user);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<RandomUser> call, Throwable t) {
//                Log.e(TAG, t.toString());
//                load();
//            }
//        });
//    }
}

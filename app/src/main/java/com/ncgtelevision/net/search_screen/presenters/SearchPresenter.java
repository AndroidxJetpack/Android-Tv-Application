package com.ncgtelevision.net.search_screen.presenters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.search_screen.search_model.Result;
import com.ncgtelevision.net.search_screen.search_model.Video;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter extends Presenter {
    private static final String TAG = SearchPresenter.class.getSimpleName();
    private Drawable mDefaultCardImage;



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_row_presenter, parent, false);
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Video videoArr = (Video) item;
//        Result videoArr = (Result) item;
        RelativeLayout cardView = (RelativeLayout) viewHolder.view;
       final TextView tv = (TextView) cardView.findViewById(R.id.header_label);
       final ImageView imageView = (ImageView) cardView.findViewById(R.id.header_icon);
            if (videoArr.getVideoThumbnail() != null) {
                tv.setText(videoArr.getVideoTitle());
//            cardView.setContentText(category.getDescription());
                Glide.with(viewHolder.view.getContext())
                        .load(videoArr.getVideoThumbnail())
                        .transform(new CenterCrop())
                        .error(mDefaultCardImage)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                int height = resource.getIntrinsicHeight();
                                int width = resource.getIntrinsicWidth();
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                                if(width > height){
                                    layoutParams.width = 280;
                                    layoutParams.height = 160;
                                    layoutParams2.width = 280;
                                }else{
                                    layoutParams.width = 140;
                                    layoutParams.height = 240;
                                    layoutParams2.width =140;
                                }
                                tv.setLayoutParams(layoutParams2);
                                imageView.setLayoutParams(layoutParams);
                                imageView.setImageDrawable(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
//            }
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
    }
}

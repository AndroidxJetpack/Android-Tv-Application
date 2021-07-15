package com.ncgtelevision.net.channel.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ncgtelevision.net.home_screen.model.Category;
import com.ncgtelevision.net.home_screen.model.MoreInfo;
import com.ncgtelevision.net.interfaces.NavigationMenuCallback;
import com.ncgtelevision.net.utilities.CommonUtility;

public class ChannelPresenter extends Presenter {
    private static final String TAG = "ChannelPresenter";
    private Drawable mDefaultCardImage;
    private NavigationMenuCallback navigationMenuCallback;
    private ViewHolder previousFocusedViewHolder = null;
    private int previousFocusedPos=0;
    private int currentFocusedPos=0;

    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        final Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_row_presenter1, parent, false);
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Category category = (Category) item;
        RelativeLayout cardView = (RelativeLayout) viewHolder.view;
        final TextView tv = (TextView) cardView.findViewById(R.id.header_label1);
        final ImageView imageView = (ImageView) cardView.findViewById(R.id.header_icon1);

        final RelativeLayout container = (RelativeLayout) cardView.findViewById(R.id.row_container1);

        /*(container.setOnFocusChangeListener((v, hasFocus) -> {
           if(hasFocus){

           }
        });
         */

        viewHolder.view.setOnFocusChangeListener((view, b) -> {
            Log.e(TAG, "onBindViewHolder: Channel Prsenter " );

            if(b)
            {
                Log.e(TAG, "onBindViewHolder: Channel Prsenter 1 " );
                //Toast.makeText(viewHolder.view.getContext(), "Hello", Toast.LENGTH_SHORT).show();
                updateFocusPosition(viewHolder, b,0);
                startFocusAnimation(viewHolder, b);

            }

            //  Toast.makeText(viewHolder.view.getContext(), "Hello", Toast.LENGTH_SHORT).show();

            // view.getLayoutParams().height=250;
            //view.getLayoutParams().width=250;

            //View v = view.findViewById(R.id.header_icon);
            //v.getLayoutParams().width=250;
            //v.getLayoutParams().height=250;
            // if(b){
            //    view.getLayoutParams().height=250;
            //view.getLayoutParams().width=250;
            //}

            // imageView.getLayoutParams().width=imageView.getLayoutParams().width+100;
            //imageView.getLayoutParams().height=imageView.getLayoutParams().height+100;


            if(b && item != null && navigationMenuCallback != null){
                Log.e(TAG, "onBindViewHolder: Channel Prsenter 2 " );
                Category moreInfo = (Category) item;
                String s [] = new String[3];
                s[0] =  moreInfo.getShortVideo();//"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
                s[1]= moreInfo.getTitle();
                s[2] = moreInfo.getDescription();
                navigationMenuCallback.selectedOption(s);
                CommonUtility.focusIn(view, 0);
//                    RelativeLayout layout =(RelativeLayout) view.findViewById(R.id.mainContainer);
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
//                    params.setMargins(15, 15, 15, 15);
//                    layout.setBackground(ContextCompat.getDrawable(view.getContext() , R.drawable.item_rectangle_focus));
//                    layout.setLayoutParams(params);
//                    CommonUtility.focusIn(layout, 0);

            }else {
                Log.e(TAG, "onBindViewHolder: Channel Prsenter 3 " );
                CommonUtility.focusOut(view, 0);
//                    RelativeLayout layout =(RelativeLayout) view.findViewById(R.id.mainContainer);
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
//                    params.setMargins(0, 4, 0, 4);
//                    layout.setBackground(ContextCompat.getDrawable(view.getContext() , R.drawable.item_rectangle_normal));
//                    layout.setLayoutParams(params);
//                    CommonUtility.focusOut(layout, 0);
            }
//            if(b)
//            {
////                updateFocusPosition(viewHolder, b,0);
////                startFocusAnimation(viewHolder, b);
////                navigationMenuCallback.selectedOption(s);
//                CommonUtility.focusOut(view, 0);
//            }
//            else
//            {
//
//            }
//
//            //Toast.makeText(viewHolder.view.getContext(), "Hello world", Toast.LENGTH_SHORT).show();
//            if(b && item != null && navigationMenuCallback != null){
//
//                Category moreInfo = (Category) item;
//                String[] s = new String[3];
//                s[0] = moreInfo.getVideo();//"https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
//                s[1]= moreInfo.getTitle();
//                s[2] = moreInfo.getDescription();
//                navigationMenuCallback.selectedOption(s);
//            }
        });

        if (category.getImage() != null) {

            tv.setText(category.getTitle());
//            cardView.setContentText(category.getDescription());
            Glide.with(viewHolder.view.getContext())
                    .load(category.getImage())
                    .transform(new CenterCrop()) //,new RoundedCorners(6)
                    .error(mDefaultCardImage)
                    .placeholder(R.drawable.movie)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            int height = resource.getIntrinsicHeight();
                            int width = resource.getIntrinsicWidth();
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) tv.getLayoutParams();
                            if(width > height){
                                Log.i(TAG, "onResourceReady: if ");
                                layoutParams.width = 280;
                                layoutParams.height = 160;
                                layoutParams2.width = 280;
                            }else{
                                Log.i(TAG, "onResourceReady: else ");
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
        }
    }

    public void onUnbindViewHolder(ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
    }

    public NavigationMenuCallback getNavigationMenuCallback() {
        return navigationMenuCallback;
    }

    public void setNavigationMenuCallback(NavigationMenuCallback navigationMenuCallback) {
        this.navigationMenuCallback = navigationMenuCallback;
    }

    public void startFocusAnimation(ViewHolder holder , boolean hasFocus)
    {
        int moveOutAnimationSet ;
        int moveInAnimationSet;
        Animation moveOutAnim=null;
        Animation moveInAnim=null;
        if(hasFocus){
            if(currentFocusedPos > previousFocusedPos){
                moveOutAnimationSet=R.anim.zoom_in;
            }
            else{
                moveOutAnimationSet=R.anim.zoom_in;
            }
            moveOutAnim= AnimationUtils.loadAnimation(holder.view.getContext(),moveOutAnimationSet);
            moveOutAnim.setFillAfter(true);
            // moveOutAnim.setDuration(400);
            holder.view.startAnimation(moveOutAnim);

            if(currentFocusedPos > previousFocusedPos){
                moveInAnimationSet=R.anim.zoom_in;
            }
            else{
                moveInAnimationSet=R.anim.zoom_in;
            }
            moveInAnim= AnimationUtils.loadAnimation(holder.view.getContext(),moveInAnimationSet);
            moveInAnim.setFillAfter(true);
            //   moveInAnim.setDuration(400);
            holder.view.startAnimation(moveInAnim);
        }
        else{
        }
        previousFocusedViewHolder=holder;
    }

    public void updateFocusPosition(ViewHolder viewHolder,boolean focus, int position)
    {
        if(focus)
        {
            previousFocusedPos=currentFocusedPos;
            currentFocusedPos=position;
        }
        else{
            previousFocusedViewHolder=viewHolder;
        }

    }
}
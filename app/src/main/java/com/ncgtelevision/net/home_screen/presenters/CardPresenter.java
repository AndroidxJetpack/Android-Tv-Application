package com.ncgtelevision.net.home_screen.presenters;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.ncgtelevision.net.R;
import com.ncgtelevision.net.home_screen.model.MoreInfo;

public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    private static final int CARD_WIDTH = 270;
    private static final int CARD_HEIGHT = 150;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
//        view.setBackgroundColor(color);
//        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");

        sDefaultBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.fastlane_background);
        sSelectedBackgroundColor =
                ContextCompat.getColor(parent.getContext(), R.color.selected_background);
        /*
         * This template uses a default image in res/drawable, but the general case for Android TV
         * will require your resources in xhdpi. For more information, see
         * https://developer.android.com/training/tv/start/layouts.html#density-resources
         */
        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView =
                new ImageCardView(parent.getContext()) {
                    @Override
                    public void setSelected(boolean selected) {
                        updateCardBackgroundColor(this, selected);
                        super.setSelected(selected);
                    }
                };
        cardView.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.rectangle_focus));
        cardView.findViewById(R.id.info_field).setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.rectangle_focus));
        cardView.findViewById(R.id.main_image).setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.rectangle_focus));

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        MoreInfo category = (MoreInfo) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;

        Log.d(TAG, "onBindViewHolder" + category.getImage());
        if (category.getImage() != null) {
            cardView.setTitleText(category.getTitle());
//            cardView.setContentText(category.getDescription());
            cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT);
            Glide.with(viewHolder.view.getContext())
                    .load(category.getImage())
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}


package com.ncgtelevision.net.search_screen.search_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Video implements Serializable {

    @SerializedName("type")
    private String mType;
    @SerializedName("video_excerpt")
    private String mVideoExcerpt;
    @SerializedName("video_id")
    private int mVideoId;
    @SerializedName("video_link")
    private String mVideoLink;
    @SerializedName("video_thumbnail")
    private String mVideoThumbnail;
    @SerializedName("video_title")
    private String mVideoTitle;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getVideoExcerpt() {
        return mVideoExcerpt;
    }

    public void setVideoExcerpt(String videoExcerpt) {
        mVideoExcerpt = videoExcerpt;
    }

    public int getVideoId() {
        return mVideoId;
    }

    public void setVideoId(int videoId) {
        mVideoId = videoId;
    }

    public String getVideoLink() {
        return mVideoLink;
    }

    public void setVideoLink(String videoLink) {
        mVideoLink = videoLink;
    }

    public String getVideoThumbnail() {
        return mVideoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        mVideoThumbnail = videoThumbnail;
    }

    public String getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        mVideoTitle = videoTitle;
    }

}

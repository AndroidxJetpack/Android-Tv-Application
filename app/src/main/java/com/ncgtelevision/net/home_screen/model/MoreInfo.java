
package com.ncgtelevision.net.home_screen.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreInfo implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("short_video")
    @Expose
    private String shortVideo;
    @SerializedName("more_info")
    @Expose
    private List<MoreInfo> moreInfo = null;
    @SerializedName("video_id")
    @Expose
    private int videoId;
    @SerializedName("channel_name")
    @Expose
    private String channelName;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShortVideo() {
        return shortVideo;
    }

    public void setShortVideo(String shortVideo) {
        this.shortVideo = shortVideo;
    }

    public List<MoreInfo> getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(List<MoreInfo> moreInfo) {
        this.moreInfo = moreInfo;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

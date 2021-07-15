
package com.ncgtelevision.net.home_screen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreInfo_ {

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
    private List<Object> moreInfo = null;

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

    public List<Object> getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(List<Object> moreInfo) {
        this.moreInfo = moreInfo;
    }

}

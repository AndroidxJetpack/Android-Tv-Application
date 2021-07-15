package com.ncgtelevision.net.playback.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datum implements Serializable{
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("vimeo_url")
    @Expose
    private String vimeoUrl;
    @SerializedName("youtube_id")
    @Expose
    private String youtubeId;
    @SerializedName("iframe")
    @Expose
    private String iframe;
    @SerializedName("is_in_myList")
    @Expose
    private boolean isInMyList;

    public Datum(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readString();
        vimeoUrl = in.readString();
        youtubeId = in.readString();
        iframe = in.readString();
        isInMyList = in.readByte() != 0;
    }


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

    public String getVimeoUrl() {
        return vimeoUrl;
    }

    public void setVimeoUrl(String vimeoUrl) {
        this.vimeoUrl = vimeoUrl;
    }

    public boolean isIsInMyList() {
        return isInMyList;
    }

    public void setIsInMyList(boolean isInMyList) {
        this.isInMyList = isInMyList;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getIframe() {
        return iframe;
    }

    public void setIframe(String iframe) {
        this.iframe = iframe;
    }


}

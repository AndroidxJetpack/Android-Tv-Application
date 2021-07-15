
package com.ncgtelevision.net.home_screen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

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
    @SerializedName("channel_name")
    @Expose
    private String channelName;
    @SerializedName("vimeourl")
    @Expose
    private String vimeoUrl;
    @SerializedName("trailer")
    @Expose
    private String trailer;
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

    public String getVimeoUrl() {
//        return "https://player.vimeo.com/external/495677259.sd.mp4?s=966e59adfd5248af61f89a49b966992bec503787&profile_id=139&oauth2_token_id=1436689020";
//        https://player.vimeo.com/external/314532728.hd.mp4?s=ce9c9b8d3db7104c75f72ae81d934ebc79dd9a25&profile_id=175&oauth2_token_id=1436689020 // working
//        return "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
        return vimeoUrl;
    }

    public void setVimeoUrl(String vimeoUrl) {
        this.vimeoUrl = vimeoUrl;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTrailer() {
//        return "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4";
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}

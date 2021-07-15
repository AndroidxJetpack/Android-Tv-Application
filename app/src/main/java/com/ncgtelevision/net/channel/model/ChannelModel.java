package com.ncgtelevision.net.channel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelModel {
    @SerializedName("channel_name")
    @Expose
    private String channelName;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

/*

channel mode
app data...
 */
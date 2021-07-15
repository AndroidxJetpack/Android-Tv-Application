package com.ncgtelevision.net.account.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteChannelRequest {
    @SerializedName("plan")
    @Expose
    private String plan;

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}

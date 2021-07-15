package com.ncgtelevision.net.account.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteChannelResponse {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

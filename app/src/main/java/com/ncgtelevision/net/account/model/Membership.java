package com.ncgtelevision.net.account.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Membership {
    @SerializedName("plan")
    @Expose
    private String plan;
    @SerializedName("membership")
    @Expose
    private String membership;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("next_payment")
    @Expose
    private String nextPayment;
    @SerializedName("status")
    @Expose
    private String status;

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNextPayment() {
        return nextPayment;
    }

    public void setNextPayment(String nextPayment) {
        this.nextPayment = nextPayment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

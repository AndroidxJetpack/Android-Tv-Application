package com.ncgtelevision.net.account.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("new_first_name")
    @Expose
    private String newFirstName;
    @SerializedName("new_last_name")
    @Expose
    private String newLastName;
    @SerializedName("new_user_email")
    @Expose
    private String newUserEmail;
    @SerializedName("new_user_password")
    @Expose
    private String newUserPassword;
    @SerializedName("confirm_user_password")
    @Expose
    private String confirmUserPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public String getNewUserEmail() {
        return newUserEmail;
    }

    public void setNewUserEmail(String newUserEmail) {
        this.newUserEmail = newUserEmail;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    public String getConfirmUserPassword() {
        return confirmUserPassword;
    }

    public void setConfirmUserPassword(String confirmUserPassword) {
        this.confirmUserPassword = confirmUserPassword;
    }

}

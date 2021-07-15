package com.ncgtelevision.net.home_screen.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomePageModel implements Serializable {

    @SerializedName("status")
    @Expose
    private boolean status= false;
    @SerializedName("banner")
    @Expose
    private List<Banner> banner;
    @SerializedName("category")
    @Expose
    private List<Category> category = null;
    @SerializedName("MenuItems")
    @Expose
    private MenuItems menuItems;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("my_list")
    @Expose
    private List<MoreInfo> myList = null;

    @SerializedName("parent")
    @Expose
    private List<Banner> parent;

    @SerializedName("success")
    private boolean success = false;
    @SerializedName("statusCode")
    private int statusCode;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItems menuItems) {
        this.menuItems = menuItems;
    }

    public String getMessage() {
        if(message != null)
          return message;
        return "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MoreInfo> getMyList() {
        return myList;
    }

    public void setMyList(List<MoreInfo> myList) {
        this.myList = myList;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public List<Banner> getParent() {
        return parent;
    }

    public void setParent(List<Banner> parent) {
        this.parent = parent;
    }
}

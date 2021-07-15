
package com.ncgtelevision.net.home_screen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuItems {

    @SerializedName("commonMenuItems")
    @Expose
    private List<CommonMenuItem> commonMenuItems = null;
    @SerializedName("additionalMobileMenu")
    @Expose
    private List<AdditionalMobileMenu> additionalMobileMenu = null;
    @SerializedName("addnlTVMenuAvailable")
    @Expose
    private List<Object> addnlTVMenuAvailable = null;

    public List<CommonMenuItem> getCommonMenuItems() {
        return commonMenuItems;
    }

    public void setCommonMenuItems(List<CommonMenuItem> commonMenuItems) {
        this.commonMenuItems = commonMenuItems;
    }

    public List<AdditionalMobileMenu> getAdditionalMobileMenu() {
        return additionalMobileMenu;
    }

    public void setAdditionalMobileMenu(List<AdditionalMobileMenu> additionalMobileMenu) {
        this.additionalMobileMenu = additionalMobileMenu;
    }

    public List<Object> getAddnlTVMenuAvailable() {
        return addnlTVMenuAvailable;
    }

    public void setAddnlTVMenuAvailable(List<Object> addnlTVMenuAvailable) {
        this.addnlTVMenuAvailable = addnlTVMenuAvailable;
    }

}

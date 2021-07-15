
package com.ncgtelevision.net.home_screen.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalMobileMenu {

    @SerializedName("main_menu")
    @Expose
    private String mainMenu;
    @SerializedName("sub_menu")
    @Expose
    private List<String> subMenu = null;

    public String getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(String mainMenu) {
        this.mainMenu = mainMenu;
    }

    public List<String> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<String> subMenu) {
        this.subMenu = subMenu;
    }

}

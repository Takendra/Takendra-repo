package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tabs implements Serializable {

    @SerializedName("tabTitle")
    private String tabTitle;
    @SerializedName("tabIcon")
    private String tabIcon;

    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public String getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(String tabIcon) {
        this.tabIcon = tabIcon;
    }
}

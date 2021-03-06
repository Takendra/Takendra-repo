package com.viewlift.models.data.appcms.ui.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class NavigationUser extends NavigationLocalizationMap implements Serializable {

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("items")
    @Expose
    List<Object> items = null;

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("anchor")
    @Expose
    String anchor;

    @SerializedName("displayedPath")
    @Expose
    String displayedPath;

    @SerializedName("displayedName")
    @Expose
    String displayedName;

    @SerializedName("accessLevels")
    @Expose
    AccessLevels accessLevels;

    @SerializedName("icon")
    @Expose
    String icon;
    @SerializedName("addSeparator")
    @Expose
    boolean addSeparator;

    public boolean isAddSeparator() {
        return addSeparator;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public String getDisplayedPath() {
        return displayedPath;
    }

    public void setDisplayedPath(String displayedPath) {
        this.displayedPath = displayedPath;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public AccessLevels getAccessLevels() {
        return accessLevels;
    }

    public void setAccessLevels(AccessLevels accessLevels) {
        this.accessLevels = accessLevels;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}

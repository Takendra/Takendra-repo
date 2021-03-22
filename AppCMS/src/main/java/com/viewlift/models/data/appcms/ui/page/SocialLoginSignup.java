package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SocialLoginSignup implements Serializable {

    @SerializedName("enable")
    @Expose
    boolean enable;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("dragDropId")
    @Expose
    int dragDropId;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDragDropId() {
        return dragDropId;
    }

    public void setDragDropId(int dragDropId) {
        this.dragDropId = dragDropId;
    }
}

package com.viewlift.models.data.appcms.likes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeRequest {

    @SerializedName("contentType")
    @Expose
    String  contentType;

    @SerializedName("contentId")
    @Expose
    String  contentId;

    @SerializedName("title")
    @Expose
    String  title;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

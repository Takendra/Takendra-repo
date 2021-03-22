package com.viewlift.models.data.appcms.api;

/**
 * Created by anas.azeem on 5/15/2018.
 * Owned by ViewLift, NYC
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeaturedTag implements Serializable {
    @SerializedName("title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
package com.viewlift.models.data.appcms.likes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Likes {

    @SerializedName("Count")
    @Expose
    int count;

    public int getCount() {
        return count;
    }
}

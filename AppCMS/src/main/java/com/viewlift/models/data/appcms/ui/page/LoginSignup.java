package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class LoginSignup implements Serializable {

    @SerializedName("enable")
    @Expose
    boolean enable;

    @SerializedName("title")
    @Expose
    String title;

    public boolean isEnable() {
        return enable;
    }

    public String getTitle() {
        return title;
    }
}

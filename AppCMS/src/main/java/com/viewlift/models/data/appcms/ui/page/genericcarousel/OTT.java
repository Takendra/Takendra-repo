package com.viewlift.models.data.appcms.ui.page.genericcarousel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OTT implements Serializable {

    @SerializedName("enable")
    @Expose
    boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

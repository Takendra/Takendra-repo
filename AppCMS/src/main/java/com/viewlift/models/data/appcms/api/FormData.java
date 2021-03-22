package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class FormData implements Serializable {
    @SerializedName("titleInput")
    @Expose
    String titleInput;

    public String getTitleInput() {
        return titleInput;
    }
}

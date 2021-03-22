package com.viewlift.models.data.appcms.ui.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class TveSettings implements Serializable {

    @SerializedName("tve_provider")
    @Expose
    String tveProvider;
    @SerializedName("platform_id")
    @Expose
    String platformId;
    @SerializedName("resource_id")
    @Expose
    String resourceId;

    public String getTveProvider() {
        return tveProvider;
    }

    public String getPlatformId() {
        return platformId;
    }

    public String getResourceId() {
        return resourceId;
    }
}

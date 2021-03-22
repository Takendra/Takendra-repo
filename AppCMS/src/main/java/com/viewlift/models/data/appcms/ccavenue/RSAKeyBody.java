package com.viewlift.models.data.appcms.ccavenue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RSAKeyBody {
    @SerializedName("site")
    @Expose
    String site;

    @SerializedName("userId")
    @Expose
    String userId;

    @SerializedName("device")
    @Expose
    String device;

    @SerializedName("planId")
    @Expose
    String planId;

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}

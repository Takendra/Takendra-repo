package com.viewlift.models.data.appcms.sites;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

@UseStag
public class AppCMSSite {

    @SerializedName("siteInternalName")
    @Expose
    String siteInternalName;

    public String getSiteInternalName() {
        return siteInternalName;
    }

    public void setSiteInternalName(String siteInternalName) {
        this.siteInternalName = siteInternalName;
    }

}

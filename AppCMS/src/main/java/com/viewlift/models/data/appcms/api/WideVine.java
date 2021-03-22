package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.utils.Macros;

import java.io.Serializable;

public  class WideVine implements Serializable {
    @SerializedName("certificateUrl")
    @Expose
    String certificateUrl;
    @SerializedName("licenseUrl")
    @Expose
    String licenseUrl;
    @SerializedName("url")
    @Expose
    String url;

    public String getLicenseToken() {
        return licenseToken;
    }

    public void setLicenseToken(String licenseToken) {
        this.licenseToken = licenseToken;
    }

    @SerializedName("licenseToken")
    @Expose
    String licenseToken;


    public String getCertificateUrl() {
        return certificateUrl;
    }

    public void setCertificateUrl(String certificateUrl) {
        this.certificateUrl = certificateUrl;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getUrl() {
        if (url != null)
            return Macros.INSTANCE.replaceURl(url);
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.ArrayList;

@UseStag
public class Options implements Serializable {

    @SerializedName("socialLoginSignup")
    @Expose
    ArrayList<LoginSignup> socialLoginSignup;

    @SerializedName("emailLoginSignup")
    @Expose
    ArrayList<LoginSignup> emailLoginSignup;
    @SerializedName("authOptions")
    @Expose
    ArrayList<LoginSignup> entitlementOption;

    public ArrayList<LoginSignup> getSocialLoginSignup() {
        return socialLoginSignup;
    }

    public ArrayList<LoginSignup> getEmailLoginSignup() {
        return emailLoginSignup;
    }

    public ArrayList<LoginSignup> getEntitlementOption() {
        return entitlementOption;
    }
}

package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.ui.android.AccessLevels;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.HashMap;

@UseStag
public class PrimaryCta implements Serializable {
    @SerializedName("ctaText")
    @Expose
    String ctaText;


    @SerializedName("bannerText")
    @Expose
    String bannerText;


    @SerializedName("displayedPath")
    @Expose
    String displayedPath;

    @SerializedName("placement")
    @Expose
    String placement;

    @SerializedName("pageId")
    @Expose
    String pageId;

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("logoutIcon")
    @Expose
    String logoutIcon;

    @SerializedName("accessLevels")
    @Expose
    AccessLevels accessLevels;

    @SerializedName("displayBannerOnMobile")
    @Expose
    boolean displayBannerOnMobile=false;


    public AccessLevels getAccessLevels() {
        return accessLevels;
    }

    public void setAccessLevels(AccessLevels accessLevels) {
        this.accessLevels = accessLevels;
    }

    @SerializedName("loginCtaText")
    @Expose
    String loginCtaText;

    @SerializedName("logoutCtaText")
    @Expose
    String logoutCtaText;

    @SerializedName("geoTargetedPageIds")
    @Expose
    public HashMap<String,String> geoTargetedPageIdsMap;

    public HashMap<String,String> getGeoTargetedPageIdsMap() {
        return geoTargetedPageIdsMap;
    }
    public void setGeoTargetedPageIdsMap(HashMap<String,String> geoTargetedPageIdsMap) {
        this.geoTargetedPageIdsMap = geoTargetedPageIdsMap;
    }

    public String getBannerText() {
        return bannerText;
    }

    public void setBannerText(String bannerText) {
        this.bannerText = bannerText;
    }


    public String getCtaText() {
        return ctaText;
    }

    public void setCtaText(String ctaText) {
        this.ctaText = ctaText;
    }

    public String getDisplayedPath() {
        return displayedPath;
    }

    public void setDisplayedPath(String displayedPath) {
        this.displayedPath = displayedPath;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public String getPageId() {
        String _pageId = null;
        if(getGeoTargetedPageIdsMap() != null){
            _pageId = getGeoTargetedPageIdsMap().get(Utils.getCountryCode());
            if(_pageId == null){
                _pageId = getGeoTargetedPageIdsMap().get("default");
            }
        }
        if(_pageId == null){
            _pageId = pageId;
        }
        return _pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLoginCtaText() {
        return loginCtaText;
    }

    public void setLoginCtaText(String loginCtaText) {
        this.loginCtaText = loginCtaText;
    }

    public String getLogoutCtaText() {
        return logoutCtaText;
    }

    public void setLogoutCtaText(String logoutCtaText) {
        this.logoutCtaText = logoutCtaText;
    }

    public boolean isDisplayBannerOnMobile() {
        return displayBannerOnMobile;
    }

    public void setDisplayBannerOnMobile(boolean displayBannerOnMobile) {
        this.displayBannerOnMobile = displayBannerOnMobile;
    }

    public String getLogoutIcon() {
        return logoutIcon;
    }
}

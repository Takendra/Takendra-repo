package com.viewlift.models.data.appcms.ui.android;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.AppCMSApplication;
import com.viewlift.Utils;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;

import java.io.Serializable;
import java.util.HashMap;

public class NavigationLocalizationMap implements Serializable {

    @SerializedName("localizationMap")
    @Expose
    public HashMap<String, LocalizationResult> localizationMap;

    @SerializedName("geoTargetedPageIds")
    @Expose
    public HashMap<String, String> geoTargetedPageIdsMap;

    @SerializedName("pageId")
    @Expose
    public String pageId;

    public HashMap<String, LocalizationResult> getLocalizationMap() {
        return localizationMap;
    }

    public void setLocalizationMap(HashMap<String, LocalizationResult> localizationMap) {
        this.localizationMap = localizationMap;
    }

    public HashMap<String, String> getGeoTargetedPageIdsMap() {
        return geoTargetedPageIdsMap;
    }

    public void setGeoTargetedPageIdsMap(HashMap<String, String> geoTargetedPageIdsMap) {
        this.geoTargetedPageIdsMap = geoTargetedPageIdsMap;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageId() {
        String countryCode = !TextUtils.isEmpty(CommonUtils.getCountryCode()) ? CommonUtils.getCountryCode() : Utils.getCountryCode();
        String _pageId = null;
        if (getGeoTargetedPageIdsMap() != null) {
            _pageId = getGeoTargetedPageIdsMap().get(countryCode);
            if (_pageId == null) {
                _pageId = getGeoTargetedPageIdsMap().get("default");
            }
        }
        if (_pageId == null) {
            _pageId = pageId;
        }
        return _pageId;
    }

}

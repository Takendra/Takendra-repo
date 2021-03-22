package com.viewlift.models.data.appcms.ui.android;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.Utils;
import com.viewlift.utils.CommonUtils;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.HashMap;

@UseStag
public class MetaPage implements Serializable {

    @SerializedName("Page-Name")
    @Expose
    String pageName;

    @SerializedName("Page-Type")
    @Expose
    String pageType;

    @SerializedName("Page-ID")
    @Expose
    String pageId;

    @SerializedName("Page-UI")
    @Expose
    String pageUI;

    @SerializedName("Page-API")
    @Expose
    String pageAPI;

    @SerializedName("pageFn")
    @Expose
    String pageFunction;

    @SerializedName("version")
    @Expose
    String version;

    @SerializedName("geoTargetedPageIds")
    @Expose
    public HashMap<String, String> geoTargetedPageIdsMap;

    @SerializedName("supportExpandedView")
    @Expose
    public boolean supportExpandedView;

    @SerializedName("isFixedFocus")
    @Expose
    public boolean isFixedFocus;

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
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

    public String getPageIdForMap() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageUI() {
        return pageUI;
    }

    public void setPageUI(String pageUI) {
        this.pageUI = pageUI;
    }

    public String getPageAPI() {
        return pageAPI;
    }

    public void setPageAPI(String pageAPI) {
        this.pageAPI = pageAPI;
    }

    public String getPageFunction() {
        return pageFunction != null ? pageFunction.trim() : pageName;
    }

    public void setPageFunction(String pageFunction) {
        this.pageFunction = pageFunction;
    }

    public HashMap<String, String> getGeoTargetedPageIdsMap() {
        return geoTargetedPageIdsMap;
    }

    public void setGeoTargetedPageIdsMap(HashMap<String, String> geoTargetedPageIdsMap) {
        this.geoTargetedPageIdsMap = geoTargetedPageIdsMap;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isSupportExpandedView() {
        return supportExpandedView;
    }

    public void setSupportExpandedView(boolean supportExpandedView) {
        this.supportExpandedView = supportExpandedView;
    }

    public boolean isFixedFocus() {
        return isFixedFocus;
    }

    public void setFixedFocus(boolean fixedFocus) {
        isFixedFocus = fixedFocus;
    }
}

package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Settings implements Serializable {

    @SerializedName("lazyLoad")
    @Expose
    boolean lazyLoad;

    @SerializedName("hideTitle")
    @Expose
    boolean hideTitle;

    @SerializedName("hideDate")
    @Expose
    boolean hideDate;

    @SerializedName("displayDevices")
    @Expose
    Object displayDevices;

    @SerializedName("divClassName")
    @Expose
    Object divClassName;

    @SerializedName("seeAll")
    @Expose
    boolean seeAll;

    @SerializedName("seeAllCard")
    @Expose
    boolean seeAllCard;

    @SerializedName("seeAllPermalink")
    @Expose
    String seeAllPermalink;

    @SerializedName("showMore")
    @Expose
    boolean showMore;

    @SerializedName("activateDeviceUrl")
    @Expose
    String activateDeviceUrl;

    public boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public boolean getHideTitle() {
        return hideTitle;
    }

    public void setHideTitle(boolean hideTitle) {
        this.hideTitle = hideTitle;
    }

    public boolean getHideDate() {
        return hideDate;
    }

    public void setHideDate(boolean hideDate) {
        this.hideDate = hideDate;
    }

    public Object getDisplayDevices() {
        return displayDevices;
    }

    public void setDisplayDevices(Object displayDevices) {
        this.displayDevices = displayDevices;
    }

    public Object getDivClassName() {
        return divClassName;
    }

    public void setDivClassName(Object divClassName) {
        this.divClassName = divClassName;
    }

    public boolean isSeeAll() {
        return seeAll;
    }

    public void setSeeAll(boolean seeAll) {
        this.seeAll = seeAll;
    }

    public boolean isSeeAllCard() {
        return seeAllCard;
    }

    public void setSeeAllCard(boolean seeAllCard) {
        this.seeAllCard = seeAllCard;
    }

    public String getSeeAllPermalink() {
        return seeAllPermalink;
    }

    public void setSeeAllPermalink(String seeAllPermalink) {
        this.seeAllPermalink = seeAllPermalink;
    }

    public boolean getShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public String getActivateDeviceUrl() {
        return activateDeviceUrl;
    }
}

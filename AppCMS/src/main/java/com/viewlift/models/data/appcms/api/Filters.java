package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Filters implements Serializable {

    @SerializedName("recommendTrayType")
    @Expose
    String recommendTrayType;

    @SerializedName("contentType")
    @Expose
    String contentType;

    @SerializedName("historyType")
    @Expose
    String historyType;

    @SerializedName("limit")
    @Expose
    int limit;

    @SerializedName("offset")
    @Expose
    Object offset;

    @SerializedName("tags")
    @Expose
    Object tags;

    @SerializedName("categories")
    @Expose
    Object categories;

    @SerializedName("sortBy")
    @Expose
    String sortBy;

    @SerializedName("contentStatus")
    @Expose
    String contentStatus;

    @SerializedName("liveVideoFrequency")
    @Expose
    String liveVideoFrequency;

    @SerializedName("liveVideosOnly")
    @Expose
    boolean liveVideosOnly;

    @SerializedName("vodOnly")
    @Expose
    boolean vodOnly;

    public String getRecommendTrayType() {
        return recommendTrayType;
    }

    public void setRecommendTrayType(String recommendTrayType) {
        this.recommendTrayType = recommendTrayType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getHistoryType() {
        return historyType;
    }

    public void setHistoryType(String historyType) {
        this.historyType = historyType;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Object getOffset() {
        return offset;
    }

    public void setOffset(Object offset) {
        this.offset = offset;
    }

    public Object getTags() {
        return tags;
    }

    public void setTags(Object tags) {
        this.tags = tags;
    }

    public Object getCategories() {
        return categories;
    }

    public void setCategories(Object categories) {
        this.categories = categories;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
    }

    public String getLiveVideoFrequency() {
        return liveVideoFrequency;
    }

    public void setLiveVideoFrequency(String liveVideoFrequency) {
        this.liveVideoFrequency = liveVideoFrequency;
    }

    public boolean isLiveVideosOnly() {
        return liveVideosOnly;
    }

    public void setLiveVideosOnly(boolean liveVideosOnly) {
        this.liveVideosOnly = liveVideosOnly;
    }

    public boolean isVodOnly() {
        return vodOnly;
    }

    public void setVodOnly(boolean vodOnly) {
        this.vodOnly = vodOnly;
    }
}

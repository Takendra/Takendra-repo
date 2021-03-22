package com.viewlift.models.data.appcms.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.util.List;

@UseStag
public class AppCMSRecommendationGenreResult {

    @SerializedName("siteName")
    @Expose
    private String siteName;
    @SerializedName("records")
    @Expose
    private List<AppCMSRecommendationGenreRecord> records = null;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("moduleId")
    @Expose
    private String moduleId;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public List<AppCMSRecommendationGenreRecord> getRecords() {
        return records;
    }

    public void setRecords(List<AppCMSRecommendationGenreRecord> records) {
        this.records = records;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
}
package com.viewlift.models.data.appcms.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Ratings implements Serializable {

    public static int DEFAULT_VIDEO_WATCH_COUNT = 5;
    public static int DEFAULT_APP_VISIT_COUNT = 3;

    @SerializedName("ratings_enabled")
    @Expose
    boolean isRatingEnabled;

    @SerializedName("videoCount")
    @Expose
    String videoCount;

    @SerializedName("appVisit")
    @Expose
    String appVisit;

    public boolean isRatingEnabled() {
        return isRatingEnabled;
    }

    public void setRatingEnabled(boolean ratingEnabled) {
        isRatingEnabled = ratingEnabled;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(String videoCount) {
        this.videoCount = videoCount;
    }

    public String getAppVisit() {
        return appVisit;
    }


    public void setAppVisit(String appVisit) {
        this.appVisit = appVisit;
    }

    public int getVideoCountInt(){
        try {
            return Integer.parseInt(videoCount);
        } catch (NumberFormatException e) {
            return DEFAULT_VIDEO_WATCH_COUNT;
        }
    }

    public int getAppVisitInt(){
        try {
            return Integer.parseInt(appVisit);
        } catch (NumberFormatException e) {
            return DEFAULT_APP_VISIT_COUNT;
        }
    }
}

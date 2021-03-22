package com.viewlift.models.data.appcms.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class WatchedHistory implements Serializable {

    private static final int DEFAULT_INTERVAL = 90;
    @SerializedName("enable")
    @Expose
    boolean isEnabled;

    @SerializedName("interval")
    @Expose
    String interval;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int getIntervalInt() {
        try {
            return Integer.parseInt(interval);
        } catch (NumberFormatException e) {
            return DEFAULT_INTERVAL;
        }
    }
}

package com.viewlift.models.data.appcms.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class AnalyticsBeacon implements Serializable {
    private static final int DEFAULT_INTERVAL = 90;
    private static final int DEFAULT_BUFFER_INTERVAL = 5;

    @SerializedName("enable")
    @Expose
    boolean isEnabled;

    @SerializedName("interval")
    @Expose
    String interval;

    @SerializedName("bufferInterval")
    @Expose
    String bufferInterval;

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

    public String getBufferInterval() {
        return bufferInterval;
    }

    public void setBufferInterval(String bufferInterval) {
        this.bufferInterval = bufferInterval;
    }

    public int getIntervalInt(){
        try {
            return Integer.parseInt(interval);
        } catch (NumberFormatException e) {
            return DEFAULT_INTERVAL;
        }
    }

    public int getBufferIntervalInt(){
        try {
            return Integer.parseInt(bufferInterval);
        } catch (NumberFormatException e) {
            return DEFAULT_BUFFER_INTERVAL;
        }
    }

}

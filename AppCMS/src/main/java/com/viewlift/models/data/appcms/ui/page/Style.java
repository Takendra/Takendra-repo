package com.viewlift.models.data.appcms.ui.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Style implements Serializable {

    /**
     * backgroundColor : #cd198b
     * activeTextColor : #df0b39
     * activeBackgroundColor : #32a787
     * textColor : #b8a742
     */

    @SerializedName("backgroundColor")
    @Expose
    private String backgroundColor;

    @SerializedName("activeTextColor")
    @Expose
    private String activeTextColor;

    @SerializedName("activeBackgroundColor")
    @Expose
    private String activeBackgroundColor;

    @SerializedName("textColor")
    @Expose
    private String textColor;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getActiveTextColor() {
        return activeTextColor;
    }

    public void setActiveTextColor(String activeTextColor) {
        this.activeTextColor = activeTextColor;
    }

    public String getActiveBackgroundColor() {
        return activeBackgroundColor;
    }

    public void setActiveBackgroundColor(String activeBackgroundColor) {
        this.activeBackgroundColor = activeBackgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}

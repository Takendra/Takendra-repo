package com.viewlift.models.data.appcms.ui.page.genericcarousel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class CarouselMobileSettig implements Serializable {

    @SerializedName("enable")
    @Expose
    boolean enable;

    @SerializedName("portrait")
    @Expose
    boolean portrait;

    @SerializedName("landscape")
    @Expose
    boolean landscape;

    public boolean isEnable() {
        return enable;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public boolean isPortrait() {
        return portrait;
    }
}

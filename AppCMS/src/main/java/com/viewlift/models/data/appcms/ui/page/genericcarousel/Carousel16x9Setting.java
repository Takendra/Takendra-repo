package com.viewlift.models.data.appcms.ui.page.genericcarousel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Carousel16x9Setting implements Serializable {

    @SerializedName("tablet")
    @Expose
    CarouselTabletSetting carouselTabletSetting;

    @SerializedName("mobile")
    @Expose
    CarouselMobileSettig carouselMobileSettig;

    @SerializedName("ott")
    @Expose
    OTT ott;

    public CarouselMobileSettig getMobile() {
        return carouselMobileSettig;
    }

    public CarouselTabletSetting getTablet() {
        return carouselTabletSetting;
    }

    public OTT getOtt() {
        return ott;
    }
}

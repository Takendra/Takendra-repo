package com.viewlift.models.data.appcms.history;

/*
 * Created by Viewlift on 7/5/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.vimeo.stag.UseStag;

import java.util.List;

@UseStag
public class AppCMSRecommendationGenreRecord {


    @SerializedName("genreData")
    @Expose
    private List<ContentDatum> genreData = null;
    @SerializedName("genreValue")
    @Expose
    private String genreValue;

    public List<ContentDatum> getGenreData() {
        return genreData;
    }

    public void setGenreData(List<ContentDatum> genreData) {
        this.genreData = genreData;
    }

    public String getGenreValue() {
        return genreValue;
    }

    public void setGenreValue(String genreValue) {
        this.genreValue = genreValue;
    }
}
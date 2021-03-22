package com.viewlift.models.data.appcms.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.ContentDatum;

import java.util.List;

public class AppCMSSearchRelatedEpisode {
    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("episodes")
    @Expose
    List<ContentDatum> episodes;

    public List<ContentDatum> getEpisodes() {
        return episodes;
    }

    public String getTitle() {
        return title;
    }


}

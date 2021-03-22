package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class SeriesData implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("images")
    @Expose
    private Images images;

    @SerializedName("gist")
    @Expose
    Gist gist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Gist getGist() {
        return gist;
    }

    public void setGist(Gist gist) {
        this.gist = gist;
    }
}

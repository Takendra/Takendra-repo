package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class RelatedVideo implements Serializable {
    @SerializedName("gist")
    @Expose
    Gist gist;

    @SerializedName("tags")
    @Expose
    List<Tag> tags = null;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Gist getGist() {
        return gist;
    }

    public void setGist(Gist gist) {
        this.gist = gist;
    }
}
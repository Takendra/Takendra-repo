package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class VideoList implements Serializable {

    @SerializedName("gist")
    @Expose
    Gist gist;

    @SerializedName("contentDetails")
    @Expose
    ContentDetails contentDetails;

    @SerializedName("streamingInfo")
    @Expose
    StreamingInfo streamingInfo;

    public Gist getGist() {
        return gist;
    }

    public void setGist(Gist gist) {
        this.gist = gist;
    }

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public StreamingInfo getStreamingInfo() {
        return streamingInfo;
    }

    public void setStreamingInfo(StreamingInfo streamingInfo) {
        this.streamingInfo = streamingInfo;
    }

    public ContentDatum convertToContentDatum() {
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setContentDetails(this.contentDetails);
        contentDatum.setStreamingInfo(this.streamingInfo);
        contentDatum.setGist(this.gist);
        return contentDatum;
    }
}
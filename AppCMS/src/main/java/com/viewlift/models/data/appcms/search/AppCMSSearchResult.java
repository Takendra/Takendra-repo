package com.viewlift.models.data.appcms.search;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.ContentDetails;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Images;
import com.viewlift.models.data.appcms.api.Pricing;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.api.ShowDetails;
import com.viewlift.models.data.appcms.api.StreamingInfo;
import com.viewlift.models.data.appcms.playlist.AudioList;
import com.viewlift.models.data.appcms.api.Tag;
import com.vimeo.stag.UseStag;

import java.util.ArrayList;
import java.util.List;

@UseStag
public class AppCMSSearchResult {
    @SerializedName("gist")
    @Expose
    Gist gist;

    @SerializedName("contentDetails")
    ContentDetails contentDetails;


    @SerializedName("title")
    @Expose
    String title;


    @SerializedName("mediaType")
    @Expose
    String mediaType;

    @SerializedName("permalink")
    @Expose
    String permalink;

    @SerializedName("seasons")
    @Expose
    List<Season_> seasons = null;

    @SerializedName("showDetails")
    @Expose
    ShowDetails showDetails;

    @SerializedName("tags")
    @Expose
    List<Tag> tags = null;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public List<Season_> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season_> seasons) {
        this.seasons = seasons;
    }

    public List<AudioList> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<AudioList> audioList) {
        this.audioList = audioList;
    }

    @SerializedName("audioList")
    @Expose
    List<AudioList> audioList = null;

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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @SerializedName("streamingInfo")
    @Expose
    StreamingInfo streamingInfo;

    @SerializedName("images")
    @Expose
    private Images images;

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    @SerializedName("pricing")
    @Expose
    Pricing pricing;

    @SerializedName("series")
    @Expose
    List<ContentDatum> seriesData = null;

    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public ShowDetails getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(ShowDetails showDetails) {
        this.showDetails = showDetails;
    }

    public List<ContentDatum> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<ContentDatum> seriesData) {
        this.seriesData = seriesData;
    }

    public List<com.viewlift.models.data.appcms.api.Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public ContentDatum getContent(Context context) {
        ContentDatum contentDatum = new ContentDatum();

        if (getGist() == null && getMediaType() != null && getMediaType().equalsIgnoreCase(context.getString(R.string.app_cms_bundle_key_type))) {
            Gist gist = new Gist();
            gist.setMediaType(getMediaType());
            gist.setPermalink(getPermalink());
            gist.setImages(getImages());
            gist.setTitle(getTitle());

            contentDatum.setGist(gist);

        } else {
            contentDatum.setGist(getGist());
        }
        contentDatum.setContentDetails(getContentDetails());
        contentDatum.setStreamingInfo(getStreamingInfo());
        contentDatum.setSeason(getSeasons());
        contentDatum.setImages(getImages());
        contentDatum.setPricing(getPricing());
        contentDatum.setShowDetails(getShowDetails());
        contentDatum.setSeriesData(getSeriesData());
        contentDatum.setTags(tags);
        return contentDatum;
    }

    public List<ContentDatum> convertSearchData(Context context, List<AppCMSSearchResult> appCMSSearchResults) {
        List<ContentDatum> contentDatumList = new ArrayList<>();
        if (appCMSSearchResults != null && appCMSSearchResults.size() > 0) {
            for (int i = 0; i < appCMSSearchResults.size(); i++) {
                if (!(appCMSSearchResults.get(i).getTags() != null && appCMSSearchResults.get(i).getTags().size() > 0 &&
                        appCMSSearchResults.get(i).getTags().get(0).getTitle() != null &&
                        appCMSSearchResults.get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                    contentDatumList.add(appCMSSearchResults.get(i).getContent(context));
                }
            }
        }
        return contentDatumList;
    }

}

package com.viewlift.models.data.appcms.history;

/*
 * Created by Viewlift on 7/5/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.ContentDetails;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Images;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.api.ShowDetails;
import com.viewlift.models.data.appcms.api.StreamingInfo;
import com.vimeo.stag.UseStag;
import java.util.List;

import java.util.List;

@UseStag
public class Record {

    @SerializedName("contentResponse")
    @Expose
    ContentResponse contentResponse;

    @SerializedName("contentDetails")
    @Expose
    ContentDatum contentDatum;

    @SerializedName("userId")
    @Expose
    String userID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("showQueue")
    @Expose
    boolean showQueue;

    @SerializedName("addedDate")
    @Expose
    long addedDate;

    @SerializedName("updateDate")
    @Expose
    long updateDate;

    @SerializedName("gist")
    @Expose
    Gist gist;

    @SerializedName("streamingInfo")
    @Expose
    StreamingInfo streamingInfo;

    @SerializedName("series")
    @Expose
    List<ContentDatum> seriesData = null;

    @SerializedName("relatedVideos")
    @Expose
    List<ContentDatum> relatedVideos = null;

    @SerializedName("seasons")
    @Expose
    List<Season_> season = null;

    @SerializedName("images")
    @Expose
    Images images;

    @SerializedName("showDetails")
    @Expose
    ShowDetails showDetails;

    @SerializedName("permalink")
    @Expose
    String permalink;
    public ContentDatum recommendationToContentDatum(){
        ContentDetails contentDetails = new ContentDetails();
        if (this.contentDatum != null) {
            if (this.contentDatum.getTrailers() != null) {
                contentDetails.setTrailers(this.contentDatum.getTrailers());
            }
            if (this.contentDatum.getPromos() != null) {
                contentDetails.setPromos(this.contentDatum.getPromos());
            }
            if (this.contentDatum.getVideoImage() != null) {
                contentDetails.setVideoImage(this.contentDatum.getVideoImage());
            }
            if (this.contentDatum.getStatus() != null) {
                contentDetails.setStatus(this.contentDatum.getStatus());
            }
            if (this.contentDatum.getRelatedVideoIds() != null) {
                contentDetails.setRelatedVideoIds(this.contentDatum.getRelatedVideoIds());
            }
        }
        ContentDatum contentDatum = new ContentDatum();
        if(this.gist.getId()==null)
            this.gist.setId(this.id);
        if (this.gist != null && this.gist != null) {
            contentDatum.setGist(this.getGist());
        }
        if (this.streamingInfo != null) {
            contentDatum.setStreamingInfo(this.getStreamingInfo());
        }if (this.seriesData != null) {
            contentDatum.setSeriesData(this.getSeriesData());
        } if (this.relatedVideos != null) {
            contentDatum.setRelatedVideos(this.getRelatedVideos());
        }if (this.season!= null&&this.season.size()>0) {
            contentDatum.setSeason(this.getSeason());
        }if (this.images != null) {
            contentDatum.setImages(this.getImages());
        }if (this.showDetails != null) {
            contentDatum.setShowDetails(this.getShowDetails());
        }if (this.permalink != null) {
            contentDatum.setPermalink(this.getPermalink());
        }if (contentDetails != null) {
            contentDatum.setContentDetails(contentDetails);
        }
        return contentDatum;
    }
    public StreamingInfo getStreamingInfo() {
        return streamingInfo;
    }

    public void setStreamingInfo(StreamingInfo streamingInfo) {
        this.streamingInfo = streamingInfo;
    }

    public List<ContentDatum> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<ContentDatum> seriesData) {
        this.seriesData = seriesData;
    }
    public List<ContentDatum> getRelatedVideos() {
        return relatedVideos;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setSeason(List<Season_> season) {
        this.season = season;
    }
    public List<Season_> getSeason() {
        return season;
    }
    public void setRelatedVideos(List<ContentDatum> relatedVideos) {
        this.relatedVideos = relatedVideos;
    }
    public ShowDetails getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(ShowDetails showDetails) {
        this.showDetails = showDetails;
    }
    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }
    public ContentResponse getContentResponse() {
        return contentResponse;
    }

    public void setContentResponse(ContentResponse contentResponse) {
        this.contentResponse = contentResponse;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isShowQueue() {
        return showQueue;
    }

    public void setShowQueue(boolean showQueue) {
        this.showQueue = showQueue;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public Gist getGist() {
        return gist;
    }

    public void setGist(Gist gist) {
        this.gist = gist;
    }

    public ContentDatum getContentDatum() {
        return contentDatum;
    }

    public void setContentDatum(ContentDatum contentDatum) {
        this.contentDatum = contentDatum;
    }

    public ContentDatum convertToContentDatum() {
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setUserId(this.userID);
        contentDatum.setShowQueue(this.showQueue);
        contentDatum.setAddedDate(this.addedDate);
        contentDatum.setUpdateDate(this.updateDate);


        if (this.contentResponse != null && this.contentResponse.getGist() != null) {
            contentDatum.setGist(this.contentResponse.getGist());
        }
        if(this.contentResponse != null) {
            if (this.getStreamingInfo() != null) {
                contentDatum.setStreamingInfo(this.getStreamingInfo());
            }
            if (this.getSeriesData() != null) {
                contentDatum.setSeriesData(this.getSeriesData());
            }
            if (this.getRelatedVideos() != null) {
                contentDatum.setRelatedVideos(this.getRelatedVideos());
            }
            if (this.getSeason() != null && this.getSeason().size() > 0) {
                contentDatum.setSeason(this.getSeason());
            }
            if (this.getImages() != null) {
                contentDatum.setImages(this.getImages());
            }
            if (this.getShowDetails() != null) {
                contentDatum.setShowDetails(this.getShowDetails());
            }
            if (this.getPermalink() != null) {
                contentDatum.setPermalink(this.getPermalink());
            }

        }
        if (this.contentResponse != null) {
            contentDatum.setContentDetails(this.contentResponse.getContentDetails());
            contentDatum.setSeriesData(this.contentResponse.getSeriesData());
            contentDatum.setTags(this.contentResponse.getTags());
        }

        /**
         * In library reponse for season content type ,added title and description from show and season title .To show title and description.
         *
         */
        if(contentResponse!=null && contentResponse.getGist() !=null && contentResponse.getGist().getContentType()!=null && contentResponse.getGist().getContentType().equalsIgnoreCase("Season") && contentResponse.getGist().getSeriesTitle()!=null
                && contentResponse.getGist().getSeasonTitle()!=null){
            contentResponse.getGist().setTitle(contentResponse.getGist().getSeriesTitle());
            contentResponse.getGist().setDescription(contentResponse.getGist().getSeasonTitle());

        }
        if(contentResponse.getPricing()!=null) {
            contentDatum.setPricing(contentResponse.getPricing());
        }
        return contentDatum;
    }

    public ContentDatum convertToContentDatumPurchase() {
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setGist(this.gist);
        return contentDatum;
    }
}
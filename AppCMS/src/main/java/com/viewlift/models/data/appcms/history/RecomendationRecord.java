package com.viewlift.models.data.appcms.history;

/*
 * Created by Viewlift on 7/5/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.ContentDetails;
import com.viewlift.models.data.appcms.api.Gist;
import com.vimeo.stag.UseStag;

import java.util.List;

@UseStag
public class RecomendationRecord {

    @SerializedName("contentResponse")
    @Expose
    ContentResponse contentResponse;

    @SerializedName("contentDetails")
    @Expose
    ContentDetails contentDetails;

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

    @SerializedName("series")
    @Expose
    List<ContentDatum> seriesData = null;

    public ContentDatum recommendationToContentDatum(){
        ContentDatum contentDatum = new ContentDatum();
        if(this.gist.getId()==null)
            this.gist.setId(this.id);
        if (this.gist != null && this.gist != null) {
            contentDatum.setGist(this.getGist());
        }
        contentDatum.setContentDetails(getContentDetails());
        return contentDatum;
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

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
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
        if (this.contentResponse != null && this.contentResponse.getGrade() != null) {
            contentDatum.setGrade(this.contentResponse.getGrade());
        }
        return contentDatum;
    }

    public List<ContentDatum> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<ContentDatum> seriesData) {
        this.seriesData = seriesData;
    }
}
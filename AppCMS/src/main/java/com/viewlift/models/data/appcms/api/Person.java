package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.ui.page.SocialLinks;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class Person extends Object implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("gist")
    @Expose
    Gist gist;

    @SerializedName("lastName")
    @Expose
    String lastName;

    @SerializedName("metadata")
    @Expose
    List<MetaData> metadata;

    @SerializedName("addedDate")
    @Expose
    String addedDate;

    @SerializedName("publishDate")
    @Expose
    String publishDate;
    @SerializedName("updatedDate")
    @Expose
    String updatedDate;
    @SerializedName("body")
    @Expose
    String body;
    @SerializedName("isActive")
    @Expose
    boolean isActive;
    @SerializedName("contentStatus")
    @Expose
    String contentStatus;
    @SerializedName("lastUpdated")
    @Expose
    String lastUpdated;

    @SerializedName("number")
    @Expose
    float number;

    @SerializedName("PrimaryCategory")
    @Expose
    PrimaryCategory primaryCategory;

    @SerializedName("consumers")
    @Expose
    List<Object> consumers;

    @SerializedName("personType")
    @Expose
    String personType;

    @SerializedName("seo")
    @Expose
    Seo seo;

    @SerializedName("contentType")
    @Expose
    String contentType;

    @SerializedName("Images")
    @Expose
    Images ImagesObject;

    @SerializedName("teams")
    @Expose
    List<Team> teams;

    @SerializedName("leagues")
    @Expose
    List<Language> leagues;

    @SerializedName("social")
    @Expose
    SocialLinks SocialObject;

    @SerializedName("mediaType")
    @Expose
    String mediaType;

    @SerializedName("tags")
    @Expose
    List<Tag> tags;

    @SerializedName("firstName")
    @Expose
    String firstName;
    @SerializedName("site")
    @Expose
    String site;

    @SerializedName("createdBy")
    @Expose
    String createdBy;
    @SerializedName("permalink")
    @Expose
    String permalink;

    /*@SerializedName("integrationProvider")
    @Expose
    IntegrationProvider integrationProvider;*/

    public Gist getGist() {
        return gist;
    }

    public void setGist(Gist gist) {
        this.gist = gist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<MetaData> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetaData> metadata) {
        this.metadata = metadata;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(String contentStatus) {
        this.contentStatus = contentStatus;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public float getNumber() {
        return number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public PrimaryCategory getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(PrimaryCategory primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public List<Object> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<Object> consumers) {
        this.consumers = consumers;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public Seo getSeo() {
        return seo;
    }

    public void setSeo(Seo seo) {
        this.seo = seo;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Images getImagesObject() {
        return ImagesObject;
    }

    public void setImagesObject(Images imagesObject) {
        ImagesObject = imagesObject;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Language> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<Language> leagues) {
        this.leagues = leagues;
    }

    public SocialLinks getSocialObject() {
        return SocialObject;
    }

    public void setSocialObject(SocialLinks socialObject) {
        SocialObject = socialObject;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @SerializedName("streamingInfo")
    @Expose
    StreamingInfo streamingInfo;

    public StreamingInfo getStreamingInfo() {
        return streamingInfo;
    }

    public void setStreamingInfo(StreamingInfo streamingInfo) {
        this.streamingInfo = streamingInfo;
    }
}

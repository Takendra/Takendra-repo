package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class Season_ implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("publishDate")
    @Expose
    Object publishDate;

    @SerializedName("updateDate")
    @Expose
    Object updateDate;

    @SerializedName("addedDate")
    @Expose
    Object addedDate;

    @SerializedName("permalink")
    @Expose
    Object permalink;

    @SerializedName("siteOwner")
    @Expose
    Object siteOwner;

    @SerializedName("registeredDate")
    @Expose
    Object registeredDate;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("episodes")
    @Expose
    List<ContentDatum> episodes = null;

    @SerializedName("description")
    @Expose
    Object description;

    @SerializedName("isPromo")
    @Expose
    boolean isPromo;

    @SerializedName("seasonPlans")
    @Expose
    private List<ContentDatum> seasonPlans;

    @SerializedName("monetizationModels")
    @Expose
    List<MonetizationModels> monetizationModels;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Object publishDate) {
        this.publishDate = publishDate;
    }

    public Object getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Object updateDate) {
        this.updateDate = updateDate;
    }

    public Object getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Object addedDate) {
        this.addedDate = addedDate;
    }

    public Object getPermalink() {
        return permalink;
    }

    public void setPermalink(Object permalink) {
        this.permalink = permalink;
    }

    public Object getSiteOwner() {
        return siteOwner;
    }

    public void setSiteOwner(Object siteOwner) {
        this.siteOwner = siteOwner;
    }

    public Object getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Object registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContentDatum> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<ContentDatum> episodes) {
        this.episodes = episodes;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public boolean isPromo() {
        return isPromo;
    }

    public void setPromo(boolean promo) {
        isPromo = promo;
    }
    @SerializedName("number")
    @Expose
    int number;

    public int getNumber() {
        return number;
    }

    public List<ContentDatum> getSeasonPlans() {
        return seasonPlans;
    }

    public void setSeasonPlans(List<ContentDatum> seasonPlans) {
        this.seasonPlans = seasonPlans;
    }

    public List<MonetizationModels> getMonetizationModels() {
        return monetizationModels;
    }

    public void setMonetizationModels(List<MonetizationModels> monetizationModels) {
        this.monetizationModels = monetizationModels;
    }
}

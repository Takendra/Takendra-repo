package com.viewlift.models.data.appcms.likes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LikeResult {

    @SerializedName("totla")
    @Expose
    int  total;

    @SerializedName("skipped")
    @Expose
    int  skipped;

    @SerializedName("limit")
    @Expose
    int  limit;

    @SerializedName("records")
    @Expose
    List<Records> records;


    @SerializedName("contentType")
    @Expose
    String  contentType;

    @SerializedName("contentId")
    @Expose
    String  contentId;

    @SerializedName("userId")
    @Expose
    String  userId;

    @SerializedName("content")
    @Expose
    String  content;

    @SerializedName("site")
    @Expose
    String  site;

    @SerializedName("addedDate")
    @Expose
    String  addedDate;

    @SerializedName("updateDate")
    @Expose
    String  updateDate;

    @SerializedName("title")
    @Expose
    String  title;

    @SerializedName("objectKey")
    @Expose
    String  objectKey;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }
}

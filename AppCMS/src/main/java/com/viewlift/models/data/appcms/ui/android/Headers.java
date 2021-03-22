package com.viewlift.models.data.appcms.ui.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.ContentDatum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Headers implements Serializable {

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("logoURL")
    @Expose
    String logoURL;



    @SerializedName("icon")
    @Expose
    String icon;



    @SerializedName("type")
    @Expose
    boolean type;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("data")
    @Expose
    List<NavigationPrimary> data = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NavigationPrimary> getData() {
        return data;
    }

    public void setData(List<NavigationPrimary> data) {
        this.data = data;
    }

    public List<ContentDatum> convertHeaderListtoContentDatumList(){
        List<ContentDatum> contentDataList = new ArrayList<>();
        for(NavigationPrimary navigationPrimary : getData()){
            ContentDatum contentDatum = navigationPrimary.convertToContentDatum();
            contentDatum.setId(navigationPrimary.getPageId());

            if(navigationPrimary.getItems() != null && !navigationPrimary.getItems().isEmpty()){
                List<ContentDatum> subNavDataList = new ArrayList<>(navigationPrimary.getItems().size());
                for(NavigationPrimary subNavData : navigationPrimary.getItems()){
                    ContentDatum subNavData1 = subNavData.convertToContentDatum();
                    subNavDataList.add(subNavData1);
                }
                contentDatum.setContentData(subNavDataList);
            }

            contentDataList.add(contentDatum);
        }
        return contentDataList;
    }
}

package com.viewlift.models.data.appcms.ui.android;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.presenters.AppCMSPresenter;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class NavigationPrimary extends NavigationLocalizationMap implements Serializable {

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("items")
    @Expose
    List<NavigationPrimary> items = null;

    @SerializedName("url")
    @Expose
    String url;

    @SerializedName("anchor")
    @Expose
    String anchor;

    @SerializedName("displayedPath")
    @Expose
    String displayedPath;

    @SerializedName("accessLevels")
    @Expose
    AccessLevels accessLevels;

    @SerializedName("pagePath")
    @Expose
    String pagePath;

    @SerializedName("icon")
    @Expose
    String icon;

    @SerializedName("platforms")
    @Expose
    Platforms platforms;

    @SerializedName("addSeparator")
    @Expose
    boolean addSeparator;
    @SerializedName("chromeCustomTab")
    @Expose
    boolean openInChromeCustomTab;

    public boolean isAddSeparator() {
        return addSeparator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<NavigationPrimary> getItems() {
        return items;
    }

    public void setItems(List<NavigationPrimary> items) {
        this.items = items;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public String getDisplayedPath() {
        return displayedPath;
    }
    public boolean isOpenInChromeCustomTab() {
        return openInChromeCustomTab;
    }
    public void setDisplayedPath(String displayedPath) {
        this.displayedPath = displayedPath;
    }

    public AccessLevels getAccessLevels() {
        return accessLevels;
    }

    public void setAccessLevels(AccessLevels accessLevels) {
        this.accessLevels = accessLevels;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Platforms getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Platforms platforms) {
        this.platforms = platforms;
    }

  /*  public List<String> getGeoTargetedPageIds() {
        return geoTargetedPageIds;
    }

    public void setGeoTargetedPageIds(List<String> geoTargetedPageIds) {
        this.geoTargetedPageIds = geoTargetedPageIds;
    }

    @SerializedName("geoTargetedPageIds")
    @Expose
    List<String> geoTargetedPageIds;
*/


    public ContentDatum convertToContentDatum(AppCMSPresenter appCMSPresenter) {
        ContentDatum contentDatum = new ContentDatum();
        Gist gist = new Gist();
        gist.setId(this.getPageId());
        gist.setVideoImageUrl(this.getIcon());
        gist.setTitle(
                appCMSPresenter.getNavigationTitle(this.getLocalizationMap()) != null
                        ? appCMSPresenter.getNavigationTitle(this.getLocalizationMap())
                        : this.getTitle()
        );
        gist.setPermalink(this.getUrl());
        gist.setDisplayPath(this.getDisplayedPath());
        contentDatum.setGist(gist);
        return contentDatum;
    }

    public ContentDatum convertToContentDatum() {
        ContentDatum contentDatum = new ContentDatum();
        Gist gist = new Gist();
        gist.setId(this.getPageId());
        gist.setVideoImageUrl(this.getIcon());
        gist.setTitle(this.getTitle());
        gist.setPermalink(this.getUrl());
        gist.setDisplayPath(this.getDisplayedPath());
        contentDatum.setGist(gist);
        return contentDatum;
    }
}

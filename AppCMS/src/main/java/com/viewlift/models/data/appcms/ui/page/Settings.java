package com.viewlift.models.data.appcms.ui.page;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.Columns;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.android.NavigationLocalizationMap;
import com.viewlift.models.data.appcms.ui.page.genericcarousel.Carousel16x9Setting;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@UseStag
public class Settings extends NavigationLocalizationMap implements Serializable {

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("imageURL_32x9")
    @Expose
    String imageURL_32x9;

    @SerializedName("imageURL_16x9")
    @Expose
    String imageURL_16x9;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("loop")
    @Expose
    boolean loop;

    @SerializedName("showResubscribeFlow")
    @Expose
    boolean showResubscribeFlow;

    @SerializedName("columns")
    @Expose
    Columns columns;
    @SerializedName("options")
    @Expose
    Options options = null;

    @SerializedName("16x9")
    @Expose
    Carousel16x9Setting carousel16x9Setting;

    @SerializedName("recommendTrayType")
    @Expose
    String recommendTrayType;

    @SerializedName("images")
    @Expose
    Images images;

    @SerializedName("style")
    @Expose
    Styles style;

    @SerializedName("primaryCta")
    @Expose
    PrimaryCta primaryCta;

    @SerializedName("enableArticlePreview")
    @Expose
    boolean enableArticlePreview;

    @SerializedName("showPIP")
    @Expose
    boolean showPIP;

    @SerializedName("showTabs")
    @Expose
    boolean showTabs;

    @SerializedName("isStandaloneVideo")
    @Expose
    boolean standaloneVideo;

    @SerializedName("showPlaybackControls")
    @Expose
    boolean showPlaybackControls;

    @SerializedName("showTabBar")
    @Expose
    boolean showTabBar;

    @SerializedName("image")
    @Expose
    String image;
    @SerializedName("backgroundColor")
    @Expose
    String backgroundColor;

    @SerializedName("navigationId")
    @Expose
    String navigationId;

    @SerializedName("socialLinks")
    @Expose
    ArrayList<SocialLinks> socialLinks;

    @SerializedName("items")
    @Expose
    ArrayList<Items> items;

    @SerializedName("links")
    @Expose
    ArrayList<Links> links;

    @SerializedName("tabs")
    @Expose
    ArrayList<Tabs> tabs;

    @SerializedName("isHidden")
    @Expose
    boolean isHidden;

    @SerializedName("infoHover")
    @Expose
    boolean infoHover;

    @SerializedName("timeDelay")
    @Expose
    String timeDelay;

    @SerializedName("thumbnailType")
    @Expose
    String thumbnailType;

    @SerializedName("emailConsent")
    @Expose
    boolean emailConsent;

    @SerializedName("showBadgeImage")
    @Expose
    boolean showBadgeImage;

    @SerializedName("isEmailRequiredWithOTP")
    @Expose
    boolean isEmailRequiredWithOTP;

    @SerializedName("noInfo")
    @Expose
    boolean noInfo;

    @SerializedName("isFullWidth")
    @Expose
    boolean isFullWidth;

    @SerializedName("hideContentTitles")
    @Expose
    boolean hideContentTitles = false;

    @SerializedName("enableSwipeToDelete")
    @Expose
    boolean enableSwipeToDelete = false;
    @SerializedName("useSixteen")
    @Expose
    boolean use16x9OnMobile;

    @SerializedName("showAudienceAgeMessage")
    @Expose
    boolean showAudienceAgeMessage;

    @SerializedName("showBgImage")
    @Expose
    boolean showBgImage;

    @SerializedName("showLoginAgreementText")
    @Expose
    boolean showLoginAgreementText;

    @SerializedName("displaySeasonsInAscendingOrder")
    @Expose
    boolean displaySeasonsInAscendingOrder=false;

    @SerializedName("loginWithOTP")
    @Expose
    boolean loginWithOTP;

    @SerializedName("showSignupAgreementText")
    @Expose
    boolean showSignupAgreementText;

    @SerializedName("signupWithOTP")
    @Expose
    boolean signupWithOTP;

    @SerializedName("showActivateDevice")
    @Expose
    boolean showActivateDevice;

    @SerializedName("publishLabel")
    @Expose
    boolean publishLabel;

    @SerializedName("contentType")
    @Expose
    String contentType;

    @SerializedName("upiEnabled")
    @Expose
    boolean upiEnabled;

    @SerializedName("walletEnabled")
    @Expose
    boolean walletEnabled;

    @SerializedName("bankDropDownList")
    @Expose
    boolean bankDropDownList;

    @SerializedName("cardEnabled")
    @Expose
    boolean cardEnabled;

    @SerializedName("netbankingEnabled")
    @Expose
    boolean netbankingEnabled;

    @SerializedName("hideSocialSignup")
    @Expose
    boolean hideSocialSignup;


    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public Styles getStyle() {
        return style;
    }

    public void setStyle(Styles style) {
        this.style = style;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRecommendTrayType() {
        return recommendTrayType;
    }

    public void setRecommendTrayType(String recommendTrayType) {
        this.recommendTrayType = recommendTrayType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLoop() {
        return loop;
    }

    public void  setLoop(boolean loop)
    {
        this.loop = loop;
    }

    public Columns getColumns() {
        return columns;
    }

    public Options getOptions() {
        return options;
    }

    public PrimaryCta getPrimaryCta() {
        return primaryCta;
    }

    public boolean isShowPIP() {
        return showPIP;
    }

    public boolean isStandaloneVideo() {
        return standaloneVideo;
    }

    public boolean isShowPlaybackControls() {
        return showPlaybackControls;
    }

    public boolean isShowTabBar() {
        return showTabBar;
    }

    public String getImage() {
        return image;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public String getNavigationId() {
        return navigationId;
    }

    public ArrayList<SocialLinks> getSocialLinks() {
        return socialLinks;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public ArrayList<Links> getLinks() {
        return links;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isInfoHover() {
        return infoHover;
    }

    public String getTimeDelay() {
        return timeDelay;
    }

    public String getThumbnailType() {
        return thumbnailType;
    }

    public void setThumbnailType(String thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    public boolean isEmailConsent() {
        return emailConsent;
    }

    public boolean isShowBadgeImage() {
        return showBadgeImage;
    }

    public Carousel16x9Setting get16x9() {
        return carousel16x9Setting;
    }

    public boolean isNoInfo() {
        return noInfo;
    }

    public boolean isFullWidth() {
        return isFullWidth;
    }

    public boolean isHideContentTitles() {
        return hideContentTitles;
    }

    public boolean isEnableSwipeToDelete() {
        return enableSwipeToDelete;
    }

    public boolean isUse16x9OnMobile() {
        return use16x9OnMobile;
    }

    public boolean isShowAudienceAgeMessage() {
        return showAudienceAgeMessage;
    }

    public boolean isShowBgImage() {
        return showBgImage;
    }

    public boolean isShowLoginAgreementText() {
        return showLoginAgreementText;
    }

    public boolean isLoginWithOTP() {
        return loginWithOTP;
    }

    public boolean isShowSignupAgreementText() {
        return showSignupAgreementText;
    }

    public boolean isHideSocialSignup() {
        return hideSocialSignup;
    }

    public boolean isSignupWithOTP() {
        return signupWithOTP;
    }

    public boolean isShowActivateDevice() {
        return showActivateDevice;
    }
    public String getImageURL_32x9() {
        return imageURL_32x9;
    }

    public void setImageURL_32x9(String imageURL_32x9) {
        this.imageURL_32x9 = imageURL_32x9;
    }
    public String getImageURL_16x9() {
        return imageURL_16x9;
    }

    public void setImageURL_16x9(String imageURL_16x9) {
        this.imageURL_16x9 = imageURL_16x9;
    }


    public Carousel16x9Setting getCarousel16x9Setting() {
        return carousel16x9Setting;
    }

    public void setCarousel16x9Setting(Carousel16x9Setting carousel16x9Setting) {
        this.carousel16x9Setting = carousel16x9Setting;
    }
    public boolean isArticlePreviewEnabled() {
        return enableArticlePreview;
    }

    public void setArticlePreviewEnabled(boolean enableArticlePreview) {
        this.enableArticlePreview = enableArticlePreview;
    }

    public boolean isDisplaySeasonsInAscendingOrder() {
        return displaySeasonsInAscendingOrder;
    }

    public void setDisplaySeasonsInAscendingOrder(boolean displaySeasonsInAscendingOrder) {
        this.displaySeasonsInAscendingOrder = displaySeasonsInAscendingOrder;
    }

    public boolean isShowResubscribeFlow() {
        return showResubscribeFlow;
    }

    public void setShowResubscribeFlow(boolean showResubscribeFlow) {
        this.showResubscribeFlow = showResubscribeFlow;
    }
    public boolean isShowTabs() {
        return showTabs;
    }

    public List<Tabs> getTabs() {
        return tabs;
    }

    public List<ContentDatum> convertTabListToContentDatum(Module moduleAPI){
        List<ContentDatum> contentData = new ArrayList<>();
        if(getTabs() != null && getTabs().size() > 0) {
            for (int i = 0; i < getTabs().size(); i++) {
                if(getTabs().get(i) != null && !TextUtils.isEmpty(getTabs().get(i).getTabTitle())) {
                    ContentDatum contentDatum = new ContentDatum();
                    contentDatum.setTitle(getTabs().get(i).getTabTitle());
                    contentDatum.setImageUrl(getTabs().get(i).getTabIcon());
                    try {
                        contentDatum.setGist(moduleAPI.getContentData().get(i).getGist());
                        contentDatum.setId(moduleAPI.getContentData().get(i).getGist().getId());
                        contentDatum.setContentDetails(moduleAPI.getContentData().get(i).getContentDetails());
                        contentDatum.setPricing(moduleAPI.getContentData().get(i).getPricing());
                        contentDatum.setStreamingInfo(moduleAPI.getContentData().get(i).getStreamingInfo());
                        contentDatum.setTags(moduleAPI.getContentData().get(i).getTags());
                        contentDatum.setContentData(moduleAPI.getContentData());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contentData.add(contentDatum);
                }
            }
        }
        return contentData;
    }

    public void setPublishLabel(boolean publishLabel) {
        this.publishLabel = publishLabel;
    }

    public boolean isPublishLabel() {
        return publishLabel;
    }

    public boolean isUpiEnabled() {
        return upiEnabled;
    }

    public boolean isWalletEnabled() {
        return walletEnabled;
    }
    public boolean isBankDropDownList() {
        return bankDropDownList;
    }
    public boolean isCardEnabled() {
        return cardEnabled;
    }
    public boolean isNetbankingEnabled() {
        return netbankingEnabled;
    }

    public boolean isEmailRequiredWithOTP() {
        return isEmailRequiredWithOTP;
    }
}

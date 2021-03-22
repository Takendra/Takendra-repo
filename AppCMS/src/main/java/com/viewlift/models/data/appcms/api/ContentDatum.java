package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.audio.AudioAssets;
import com.viewlift.models.data.appcms.playlist.AudioList;
import com.viewlift.models.data.appcms.ui.authentication.FeatureSetting;
import com.viewlift.models.data.appcms.user.UserDescriptionResponse;
import com.viewlift.models.data.appcms.user.UserMeResponse;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@UseStag
public class ContentDatum implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("renewable")
    @Expose
    boolean renewable;

    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("identifier")
    @Expose
    String identifier;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName(value = "renewalCyclePeriodMultiplier", alternate = {"multiplier"})
    @Expose
    int renewalCyclePeriodMultiplier;

    @SerializedName("renewalCycleType")
    @Expose
    String renewalCycleType;

    @SerializedName("planDetails")
    @Expose
    List<PlanDetail> planDetails;
    private String grade;

    public List<CategoryPages> getPages() {
        return pages;
    }

    public void setPages(List<CategoryPages> pages) {
        this.pages = pages;
    }

    @SerializedName("pages")
    @Expose
    List<CategoryPages> pages;

    @SerializedName("gist")
    @Expose
    Gist gist;

    @SerializedName("userId")
    @Expose
    String userId;
    public Fights Fights;

    @SerializedName("showQueue")
    @Expose
    boolean showQueue;

    @SerializedName("airDateTime")
    @Expose
    long airDateTime;

    @SerializedName("addedDate")
    @Expose
    Object addedDate;

    @SerializedName("updateDate")
    @Expose
    Object updateDate;

    @SerializedName("contentDetails")
    @Expose
    ContentDetails contentDetails;

    @SerializedName("streamingInfo")
    @Expose
    StreamingInfo streamingInfo;

    @SerializedName("categories")
    @Expose
    List<Category> categories = null;

    @SerializedName("tags")
    @Expose
    List<Tag> tags = null;

    @SerializedName("optionalTags")
    @Expose
    List<Tag> optionalTags = null;

    @SerializedName("images")
    @Expose
    Images images;

    @SerializedName("imageUrl")
    @Expose
    String imageUrl;
    @SerializedName("monetizationModel")
    @Expose
    String planMonetizationModel;

    @SerializedName("languages")
    @Expose
    List<Language> languages = null;


    @SerializedName("relatedVideos")
    @Expose
    List<ContentDatum> relatedVideos = null;

    @SerializedName("seasons")
    @Expose
    List<Season_> season = null;

    @SerializedName("imageList")
    @Expose
    List<Image_1x1> imageList = null;

    @SerializedName("creditBlocks")
    @Expose
    List<CreditBlock> creditBlocks = null;

    @SerializedName("series")
    @Expose
    List<ContentDatum> seriesData = null;

    @SerializedName("showDetails")
    @Expose
    ShowDetails showDetails;

    @SerializedName("parentalRating")
    @Expose
    String parentalRating;

    @SerializedName("permalink")
    @Expose
    String permalink;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("categoryTitle")
    @Expose
    String categoryTitle;

    @SerializedName("trayTitle")
    @Expose
    String trayTitle;

    @SerializedName("seasonNumber")
    @Expose
    private int seasonNumber;

    @SerializedName("episodeNumber")
    @Expose
    private int episodeNumber;

    private boolean isTrailer;

    public boolean isTrailer() {
        return isTrailer;
    }

    public void setTrailer(boolean trailer) {
        isTrailer = trailer;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTrayTitle() {
        return trayTitle;
    }

    public void setTrayTitle(String trayTitle) {
        this.trayTitle = trayTitle;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @SerializedName("thumbnailURL")
    @Expose
    String thumbnailURL;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @SerializedName("dataId")
    @Expose
    String dataId;

    @SerializedName("contentType")
    @Expose
    String contentType;

    @SerializedName("mediaType")
    @Expose
    String mediaType;

    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("seriesId")
    @Expose
    String seriesId;

    private List<ContentDatum> subscriptionPlans;

    @SerializedName("seriesPlans")
    @Expose
    private List<ContentDatum> seriesPlans;

    @SerializedName("episodePlans")
    @Expose
    private List<ContentDatum> episodePlans;

    @SerializedName("bundlePlans")
    @Expose
    private List<ContentDatum> bundlePlans;

    @SerializedName("plans")
    @Expose
    List<ContentPlans> contentPlans;

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    @SerializedName("seasonId")
    @Expose
    String seasonId;

    HashMap<String, List<ContentDatum>> monthlySchedule;

    @SerializedName("drmEnabled")
    @Expose
    boolean isDRMEnabled = false;

    @SerializedName("pricing")
    @Expose
    Pricing pricing;

    @SerializedName("number")
    @Expose
    int number;
    @SerializedName("videoList")
    @Expose
    List<VideoList> videoList = null;
    @SerializedName("featureSetting")
    @Expose
    FeatureSetting featureSetting;
    @SerializedName("monetizationModels")
    @Expose
    List<MonetizationModels> monetizationModels=null;
    @SerializedName("rentIdentifier")
    @Expose
    String rentIdentifier;
    @SerializedName("purchaseIdentifier")
    @Expose
    String purchaseIdentifier;
    @SerializedName("tvodPricing")
    @Expose
    TvodPricing tvodPricing;
    @SerializedName("episode")
    @Expose
    int episode;

    @SerializedName("trailers")
    @Expose
    List<Trailer> trailers = null;

    @SerializedName("promos")
    @Expose
    List<Promos> promos = null;

    @SerializedName("relatedVideoIds")
    @Expose
    List<String> relatedVideoIds = null;

    @SerializedName("videoImage")
    @Expose
    VideoImage videoImage;

    @SerializedName("status")
    @Expose
    String status;
    public String getRentIdentifier() {
        return rentIdentifier;
    }

    public String getPurchaseIdentifier() {
        return purchaseIdentifier;
    }

    public boolean isTvodPricing() {
        return isTvodPricing;
    }

    public void setTvodPricing(boolean tvodPricing) {
        isTvodPricing = tvodPricing;
    }

    boolean isTvodPricing;


    Players players;

    private AppCMSSignedURLResult appCMSSignedURLResult;

    public AppCMSSignedURLResult getAppCMSSignedURLResult() {
        return appCMSSignedURLResult;
    }

    public void setAppCMSSignedURLResult(AppCMSSignedURLResult appCMSSignedURLResult) {
        this.appCMSSignedURLResult = appCMSSignedURLResult;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    boolean isSelected = false;

    public void setPlayersData(Players players) {
        this.players = players;
    }

    public Players getPlayersData() {
        return players;
    }


    List<Players> playersList;


    public List<Players> getPlayers() {
        return playersList;
    }

    public void setPlayers(List<Players> players) {
        this.playersList = players;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    Team team;


    private String playListName;
    private String seriesName;

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    public boolean isRenewable() {
        return renewable;
    }

    public long getAirDateTime() {
        return airDateTime;
    }

    public void setAirDateTime(long airDateTime) {
        this.airDateTime = airDateTime;
    }


    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }


    public Gist getGist() {
        return gist;
    }

    public void setGist(Gist gist) {
        this.gist = gist;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isShowQueue() {
        return showQueue;
    }

    public void setShowQueue(boolean showQueue) {
        this.showQueue = showQueue;
    }

    public Object getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Long addedDate) {
        this.addedDate = addedDate;
    }

    public Object getUpdateDate() {
        return updateDate;
    }


    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public Fights getFights() {
        return Fights;
    }

    public void setFights(Fights fights) {
        Fights = fights;
    }

    public StreamingInfo getStreamingInfo() {
        return streamingInfo;
    }

    public void setStreamingInfo(StreamingInfo streamingInfo) {
        this.streamingInfo = streamingInfo;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Season_> getSeason() {
        return season;
    }

    public void setSeason(List<Season_> season) {
        this.season = season;
    }

    public List<CreditBlock> getCreditBlocks() {
        return creditBlocks;
    }

    public void setCreditBlocks(List<CreditBlock> creditBlocks) {
        this.creditBlocks = creditBlocks;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getRenewable() {
        return renewable;
    }

    public void setRenewable(boolean renewable) {
        this.renewable = renewable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRenewalCyclePeriodMultiplier() {
        return renewalCyclePeriodMultiplier;
    }

    public void setRenewalCyclePeriodMultiplier(int renewalCyclePeriodMultiplier) {
        this.renewalCyclePeriodMultiplier = renewalCyclePeriodMultiplier;
    }

    public String getRenewalCycleType() {
        return renewalCycleType;
    }

    public void setRenewalCycleType(String renewalCycleType) {
        this.renewalCycleType = renewalCycleType;
    }

    public List<PlanDetail> getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(List<PlanDetail> planDetails) {
        this.planDetails = planDetails;
    }

    public ShowDetails getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(ShowDetails showDetails) {
        this.showDetails = showDetails;
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(String moduleType) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();
        module.setModuleType(moduleType);
        List<ContentDatum> data = new ArrayList<>();
        data.add(this);
        module.setContentData(data);
        appCMSPageAPI.setId(id);
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);
        return appCMSPageAPI;
    }


    List<AudioList> audioList = null;

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }

    List<Team> teamList = null;

    public List<AudioList> getAudioList() {
        return audioList;
    }

    public void setAudioList(List<AudioList> audioList) {
        this.audioList = audioList;
    }

    AudioAssets audioAssets = null;

    public AudioAssets getAudioAssets() {
        return audioAssets;
    }

    public void setAudioAssets(AudioAssets audioAssets) {
        this.audioAssets = audioAssets;
    }


    public boolean isDRMEnabled() {
        return isDRMEnabled;
    }

    public void setDRMEnabled(boolean DRMEnabled) {
        isDRMEnabled = DRMEnabled;
    }

    public HashMap<String, List<ContentDatum>> getMonthlySchedule() {
        return monthlySchedule;
    }

    public void setMonthlySchedule(HashMap<String, List<ContentDatum>> monthlySchedule) {
        this.monthlySchedule = monthlySchedule;
    }

    List<ContentDatum> contentData = null;

    public List<com.viewlift.models.data.appcms.api.LiveEvents> getLiveEvents() {
        return LiveEvents;
    }

    public void setLiveEvents(List<com.viewlift.models.data.appcms.api.LiveEvents> liveEvents) {
        LiveEvents = liveEvents;
    }

    List<LiveEvents> LiveEvents = null;

    public void setContentData(List<ContentDatum> contentData) {
        this.contentData = contentData;
    }

    public List<ContentDatum> getContentData() {
        return contentData;
    }


    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }


    List<FilterGroupsModel> filterGroupList;

    public List<FilterGroupsModel> getFilterGroupList() {
        return filterGroupList;
    }

    public void setFilterGroupList(List<FilterGroupsModel> filterGroupList) {
        this.filterGroupList = filterGroupList;
    }

    public int getNumOfClasses() {
        return numOfClasses;
    }

    public void setNumOfClasses(int numOfClasses) {
        this.numOfClasses = numOfClasses;
    }

    int numOfClasses;

    public List<Image_1x1> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image_1x1> imageList) {
        this.imageList = imageList;
    }

    public List<Tag> getOptionalTags() {
        return optionalTags;
    }

    public void setOptionalTags(List<Tag> optionalTags) {
        this.optionalTags = optionalTags;
    }

    public UserDescriptionResponse getUserDescriptionResponse() {
        return userDescriptionResponse;
    }

    public void setUserDescriptionResponse(UserDescriptionResponse userDescriptionResponse) {
        this.userDescriptionResponse = userDescriptionResponse;
    }

    UserDescriptionResponse userDescriptionResponse;

    public UserMeResponse getUserMeResponse() {
        return userMeResponse;
    }

    public void setUserMeResponse(UserMeResponse userMeResponse) {
        this.userMeResponse = userMeResponse;
    }

    UserMeResponse userMeResponse;

    public List<VideoList> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoList> videoList) {
        this.videoList = videoList;
    }

    String planImgUrl;

    public String getPlanImgUrl() {
        return planImgUrl;
    }

    public void setPlanImgUrl(String planImgUrl) {
        this.planImgUrl = planImgUrl;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }

    public List<Players> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<Players> playersList) {
        this.playersList = playersList;
    }

    public List<ContentDatum> getRelatedVideos() {
        return relatedVideos;
    }

    public void setRelatedVideos(List<ContentDatum> relatedVideos) {
        this.relatedVideos = relatedVideos;
    }

    public List<ContentDatum> getSeriesData() {
        return seriesData;
}

    public void setSeriesData(List<ContentDatum> seriesData) {
        this.seriesData = seriesData;
    }

    private String videoPlayError;

    public String getVideoPlayError() {
        return videoPlayError;
    }

    public void setVideoPlayError(String videoPlayError) {
        this.videoPlayError = videoPlayError;
    }

    public String getShowParentalRating() {
        return showParentalRating;
    }

    public void setShowParentalRating(String showParentalRating) {
        this.showParentalRating = showParentalRating;
    }

    String showParentalRating;

    String seasonEpisodeNum;

    public String getSeasonEpisodeNum() {
        return seasonEpisodeNum;
    }

    public void setSeasonEpisodeNum(String seasonEpisodeNum) {
        this.seasonEpisodeNum = seasonEpisodeNum;
    }

    public int getNumber() {
        return number;
    }

    Module moduleApi;

    public Module getModuleApi() {
        return moduleApi;
    }

    public void setModuleApi(Module moduleApi) {
        this.moduleApi = moduleApi;
    }

    private void removePromoData(List<ContentDatum> contentData) {
        if (contentData != null && contentData.size() > 0) {
            for (int i = 0; i < contentData.size(); i++) {
                if ((contentData.get(i).getTags() != null &&
                        contentData.get(i).getTags().size() > 0 &&
                        contentData.get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                    contentData.remove(i);
                }
            }
        }
    }

    private String walletKey;

    public String getWalletKey() {
        return walletKey;
    }

    public void setWalletKey(String walletKey) {
        this.walletKey = walletKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public String getType() {
        return type;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    @Deprecated
    public Pricing getPricing() {
        return pricing;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ContentDatum> getSubscriptionPlans() {
        return subscriptionPlans;
    }

    public void setSubscriptionPlans(List<ContentDatum> subscriptionPlans) {
        this.subscriptionPlans = subscriptionPlans;
    }

    public String getPlanMonetizationModel() {
        return planMonetizationModel;
    }

    public void setPlanMonetizationModel(String planMonetizationModel) {
        this.planMonetizationModel = planMonetizationModel;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public FeatureSetting getFeatureSetting() {
        return featureSetting;
    }

    String adUrl;

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    private boolean isEmailRequired;
    private boolean isLoginRequired;

    public boolean isEmailRequired() {
        return isEmailRequired;
    }

    public void setEmailRequired(boolean emailRequired) {
        isEmailRequired = emailRequired;
    }

    public boolean isLoginRequired() {
        return isLoginRequired;
    }

    public void setLoginRequired(boolean loginRequired) {
        isLoginRequired = loginRequired;
    }

    public List<ContentDatum> getSeriesPlans() {
        return seriesPlans;
    }

    public void setSeriesPlans(List<ContentDatum> seriesPlans) {
        this.seriesPlans = seriesPlans;
    }

    public List<MonetizationModels> getMonetizationModels() {
        return monetizationModels;
    }

    public List<ContentPlans> getContentPlans() {
        return contentPlans;
    }

    public List<ContentDatum> getEpisodePlans() {
        return episodePlans;
    }

    public void setEpisodePlans(List<ContentDatum> episodePlans) {
        this.episodePlans = episodePlans;
    }

    boolean fromStandalone = false;

    public boolean isFromStandalone() {
        return fromStandalone;
    }

    public void setFromStandalone(boolean fromStandalone) {
        this.fromStandalone = fromStandalone;
    }

    boolean fromEntitlement;

    public boolean isFromEntitlement() {
        return fromEntitlement;
    }

    public void setFromEntitlement(boolean fromEntitlement) {
        this.fromEntitlement = fromEntitlement;
    }

    public TvodPricing getTvodPricing() {
        return tvodPricing;
    }

    public List<ContentDatum> getBundlePlans() {
        return bundlePlans;
    }

    public void setBundlePlans(List<ContentDatum> bundlePlans) {
        this.bundlePlans = bundlePlans;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
    public List<Promos> getPromos() {
        return promos;
    }

    public void setPromos(List<Promos> promos) {
        this.promos = promos;
    }

    public List<String> getRelatedVideoIds() {
        return relatedVideoIds;
    }

    public void setRelatedVideoIds(List<String> relatedVideoIds) {
        this.relatedVideoIds = relatedVideoIds;
    }

    public VideoImage getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(VideoImage videoImage) {
        this.videoImage = videoImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }
}
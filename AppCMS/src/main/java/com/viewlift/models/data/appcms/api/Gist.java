package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Gist implements Serializable {

    private String displayPath;
    private long rentStartTime;
    private String transactionType;
    boolean selectedPosition;
    boolean isRentStartTimeSyncedWithServer;
    float rentalPeriod;
    boolean isRentedDialogShownAlready;
    String artistName;
    String directorName;

    String downloadStatus;
    boolean isAudioPlaying;
    boolean isVideoPlaying;
    long currentPlayingPosition;
    Boolean isCastingConnected;
    String subscriptionType;
    String landscapeImageUrl;
    /**
     * This is to store the url of the downloaded file
     */
    String localFileUrl;
    String episodeNum;
    String showName;
    String seasonNum;
    String genre;
    private String instructorTitle;
    private String durationCategory;

    @SerializedName("purchaseStatus")
    @Expose
    private String purchaseStatus;

    @SerializedName("fullDescription")
    @Expose
    boolean fullDescription = false;

    @SerializedName("contentId")
    @Expose
    private String contentId;

    @SerializedName("seriesPermalink")
    @Expose
    private String seriesPermalink;

    @SerializedName("bundlePricing")
    @Expose
    Pricing bundlePricing;

    @SerializedName("transactionDateEpoch")
    @Expose
    private long transactionDateEpoch;

    @SerializedName("transactionEndDate")
    @Expose
    private String transactionEndDate;

    @SerializedName("userId")
    @Expose
    private String userId;


    @SerializedName("videoId")
    @Expose
    private String videoId;

    @SerializedName("seasonId")
    @Expose
    private String seasonId;

    @SerializedName("paymentHandler")
    @Expose
    private String paymentHandler;

    @SerializedName("siteOwner")
    @Expose
    private String siteOwner;


    @SerializedName("gatewayChargeId")
    @Expose
    private String gatewayChargeId;

    @SerializedName("site")
    @Expose
    private String site;

    @SerializedName("longDescription")
    @Expose
    private String longDescription;

    @SerializedName("platform")
    @Expose
    private String platform;


    @SerializedName("currencyCode")
    @Expose
    private String currencyCode;


    @SerializedName("isLiveStream")
    @Expose
    private boolean isLiveStream;

    @SerializedName("purchaseType")
    @Expose
    private String purchaseType;

    @SerializedName("transactionStartDate")
    @Expose
    private String transactionStartDate;

    @SerializedName("countryCode")
    @Expose
    private String countryCode;

    @SerializedName("isTrailer")
    @Expose
    private String isTrailer;


    @SerializedName("videoQuality")
    @Expose
    private String videoQuality;


    @SerializedName("siteId")
    @Expose
    private String siteId;


    @SerializedName("seriesId")
    @Expose
    private String seriesId;

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("originalObjectId")
    @Expose
    String originalObjectId;

    @SerializedName("permalink")
    @Expose
    String permalink;

    @SerializedName("title")
    @Expose
    String title;

    @SerializedName("categoryTitle")
    @Expose
    String categoryTitle;

    @SerializedName("weight")
    @Expose
    String weight;

    @SerializedName("dob")
    @Expose
    String dob;

    @SerializedName("birthPlace")
    @Expose
    String birthPlace;

    @SerializedName("height")
    @Expose
    String height;

    @SerializedName("firstName")
    @Expose
    String firstName;

    @SerializedName("lastName")
    @Expose
    String lastName;

    @SerializedName("logLine")
    @Expose
    String logLine;

    @SerializedName("description")
    @Expose
    String description;

    @SerializedName("year")
    @Expose
    String year;

    @SerializedName("free")
    @Expose
    boolean free;

    @SerializedName("publishDate")
    @Expose
    String publishDate;

    @SerializedName("runtime")
    @Expose
    long runtime;

    @SerializedName("isFree")
    @Expose
    boolean isFree;

    @SerializedName("posterImageUrl")
    @Expose
    String posterImageUrl;

    @SerializedName("videoImageUrl")
    @Expose
    String videoImageUrl;

    @SerializedName("imageGist")
    @Expose
    ImageGist imageGist;

    @SerializedName("badgeImages")
    @Expose
    BadgeImages badgeImages;

    @SerializedName("images")
    @Expose
    Images images;

    @SerializedName("addedDate")
    @Expose
    String addedDate;

    @SerializedName("updateDate")
    @Expose
    String updateDate;

    @SerializedName("primaryCategory")
    @Expose
    PrimaryCategory primaryCategory;

    @SerializedName("watchedTime")
    @Expose
    long watchedTime;

    @SerializedName("contentType")
    @Expose
    String contentType;

    @SerializedName("averageGrade")
    @Expose
    String averageGrade;

    @SerializedName("averageStarRating")
    @Expose
    float averageStarRating;

    @SerializedName("watchedPercentage")
    @Expose
    int watchedPercentage;

    @SerializedName("kisweEventId")
    @Expose
    String kisweEventId;

    @SerializedName("mediaType")
    @Expose
    String mediaType;

    @SerializedName("readTime")
    @Expose
    String readTime;

    @SerializedName("summaryText")
    @Expose
    String summaryText;

    @SerializedName("shortName")
    @Expose
    String shortName;

    @SerializedName("email")
    @Expose
    String email;

    @SerializedName("leagues")
    @Expose
    List<GameTeamGist> leagues;

    @SerializedName("primaryCategoryTitle")
    @Expose
    String primaryCategoryTitle;

    @SerializedName("venues")
    @Expose
    List<GameTeamGist> venues;

    @SerializedName("dataId")
    @Expose
    String dataId;

    @SerializedName("website")
    @Expose
    String website;

    @SerializedName("body")
    @Expose
    String body;

    @SerializedName("featuredTag")
    @Expose
    FeaturedTag featuredTag;

    @SerializedName("ogDetails")
    @Expose
    private OgDetailsBean ogDetails;

    @SerializedName("updatedDate")
    @Expose
    String updatedDate;

    @SerializedName("ticketUrl")
    @Expose
    String ticketUrl;

    @SerializedName("rsvpUrl")
    @Expose
    String rsvpUrl;

    @SerializedName("bundleList")
    @Expose
    List<ContentDatum> bundleList;

    @SerializedName("externalUrl")
    @Expose
    String externalUrl;

    @SerializedName("livestreamUrl")
    @Expose
    String livestreamUrl;

    @SerializedName("webliveUrl")
    @Expose
    String webliveUrl;

    @SerializedName("gameSchedule")
    @Expose
    List<GameSchedule> gameSchedule = null;

    @SerializedName("eventSchedule")
    @Expose
    List<GameSchedule> eventSchedule = null;

    @SerializedName("homeTeam")
    @Expose
    GameTeamGist homeTeam;

    @SerializedName("awayTeam")
    @Expose
    GameTeamGist awayTeam;

    @SerializedName("metadata")
    @Expose
    List<MetaData> metadata = null;

    @SerializedName("seasonTitle")
    @Expose
    String seasonTitle;

    @SerializedName("pricing")
    @Expose
    Pricing pricing;

    @SerializedName("persons")
    @Expose
    List<Person> persons = null;

    @SerializedName("relatedVideos")
    @Expose
    List<RelatedVideo> relatedVideos = null;

    @SerializedName("parentalRating")
    @Expose
    String parentalRating;

    @SerializedName("seriesTitle")
    @Expose
    String seriesTitle;

    @SerializedName("facebookUrl")
    @Expose
    String facebookUrl;

    @SerializedName("twitterUrl")
    @Expose
    String twitterUrl;

    @SerializedName("instagramUrl")
    @Expose
    String instagramUrl;

    @SerializedName("contentModel")
    @Expose
    String contentModel;

    @SerializedName("playNextTime")
    @Expose
    int playNextTime;

    @SerializedName("skipIntroEndTime")
    @Expose
    int skipIntroEndTime;

    @SerializedName("skipIntroStartTime")
    @Expose
    int skipIntroStartTime;

    @SerializedName("skipRecapEndTime")
    @Expose
    int skipRecapEndTime;

    @SerializedName("skipRecapStartTime")
    @Expose
    int skipRecapStartTime;

    private long scheduleStartDate;

    private long scheduleEndDate;


    public String getSeriesPermalink() {
        return seriesPermalink;
    }

    public void setSeriesPermalink(String seriesPermalink) {
        this.seriesPermalink = seriesPermalink;
    }

    public Pricing getBundlePricing() {
        return bundlePricing;
    }

    public void setBundlePricing(Pricing bundlePricing) {
        this.bundlePricing = bundlePricing;
    }


    public long getTransactionDateEpoch() {
        return transactionDateEpoch;
    }

    public void setTransactionDateEpoch(long transactionDateEpoch) {
        this.transactionDateEpoch = transactionDateEpoch;
    }

    public long getTransactionEndDate() {
        long endDate = 0;
        if (transactionEndDate != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                Date d = f.parse(transactionEndDate);
                endDate = d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return endDate;
    }

    public void setTransactionEndDate(String transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }

    public long getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(long rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    public String getWeight() {

        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public long getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(long scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    public long getScheduleEndDate() {
        return scheduleEndDate;
    }

    public void setScheduleEndDate(long scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
    }


    public String getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(String purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
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

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getPaymentHandler() {
        return paymentHandler;
    }

    public void setPaymentHandler(String paymentHandler) {
        this.paymentHandler = paymentHandler;
    }

    public String getSiteOwner() {
        return siteOwner;
    }

    public void setSiteOwner(String siteOwner) {
        this.siteOwner = siteOwner;
    }

    public String getGatewayChargeId() {
        return gatewayChargeId;
    }

    public void setGatewayChargeId(String gatewayChargeId) {
        this.gatewayChargeId = gatewayChargeId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isLiveStream() {
        return isLiveStream;
    }

    public void setIsLiveStream(boolean isLiveStream) {
        this.isLiveStream = isLiveStream;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getTransactionStartDate() {
        return transactionStartDate;
    }

    public void setTransactionStartDate(String transactionStartDate) {
        this.transactionStartDate = transactionStartDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsTrailer() {
        return isTrailer;
    }

    public void setIsTrailer(String isTrailer) {
        this.isTrailer = isTrailer;
    }

    public String getVideoQuality() {
        return videoQuality;
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public boolean isFree() {
        return isFree;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public OgDetailsBean getOgDetails() {
        return ogDetails;
    }

    public void setOgDetails(OgDetailsBean ogDetails) {
        this.ogDetails = ogDetails;
    }

    public List<ContentDatum> getBundleList() {
        return bundleList;
    }

    public void setBundleList(List<ContentDatum> bundleList) {
        this.bundleList = bundleList;
    }



    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<RelatedVideo> getRelatedVideos() {
        return relatedVideos;
    }

    public void setRelatedVideos(List<RelatedVideo> relatedVideos) {
        this.relatedVideos = relatedVideos;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public float getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(float rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }



    public boolean isRentStartTimeSyncedWithServer() {
        return isRentStartTimeSyncedWithServer;
    }

    public void setRentStartTimeSyncedWithServer(boolean rentStartTimeSyncedWithServer) {
        isRentStartTimeSyncedWithServer = rentStartTimeSyncedWithServer;
    }




    public boolean isRentedDialogShownAlready() {
        return isRentedDialogShownAlready;
    }

    public void setRentedDialogShow(boolean rentedDialogShow) {
        isRentedDialogShownAlready = rentedDialogShow;
    }



    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public String getSeriesTitle() {
        return seriesTitle;
    }

    public void setSeriesTitle(String seriesTitle) {
        this.seriesTitle = seriesTitle;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Boolean getDataLoaded() {
        return isDataLoaded;
    }

    public void setDataLoaded(Boolean dataLoaded) {
        isDataLoaded = dataLoaded;
    }

    Boolean isDataLoaded = false;

    public String getLandscapeImageUrl() {
        return landscapeImageUrl;
    }

    public void setLandscapeImageUrl(String landscapeImageUrl) {
        this.landscapeImageUrl = landscapeImageUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getId() {
        return getOriginalObjectId() != null ? getOriginalObjectId() : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentModel() {
        return contentModel;
    }

    public void setContentModel(String contentModel) {
        this.contentModel = contentModel;
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

    public String getLogLine() {
        return logLine;
    }

    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean getFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public long getRuntime() {
        return runtime;
    }

    public void setRuntime(long runtime) {
        this.runtime = runtime;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public BadgeImages getBadgeImages() {
        return badgeImages;
    }

    public void setBadgeImages(BadgeImages badgeImages) {
        this.badgeImages = badgeImages;
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

    public PrimaryCategory getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(PrimaryCategory primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    public long getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(long watchedTime) {
        this.watchedTime = watchedTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(String averageGrade) {
        this.averageGrade = averageGrade;
    }

    public float getAverageStarRating() {
        return averageStarRating;
    }

    public void setAverageStarRating(float averageStarRating) {
        this.averageStarRating = averageStarRating;
    }

    public int getWatchedPercentage() {
        return watchedPercentage;
    }

    public void setWatchedPercentage(int watchedPercentage) {
        this.watchedPercentage = watchedPercentage;
    }

    public DownloadStatus getDownloadStatus() {
        if (downloadStatus != null) {
            return DownloadStatus.valueOf(downloadStatus);
        }
        return DownloadStatus.STATUS_PENDING;
    }

    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus.toString();
    }

    public String getLocalFileUrl() {
        return localFileUrl;
    }

    public void setLocalFileUrl(String localFileUrl) {
        this.localFileUrl = localFileUrl;
    }

    public ImageGist getImageGist() {
        return imageGist;
    }

    public void setImageGist(ImageGist imageGist) {
        this.imageGist = imageGist;
    }

    public String getKisweEventId() {
        return kisweEventId;
    }

    public void setKisweEventId(String kisweEventId) {
        this.kisweEventId = kisweEventId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public boolean isAudioPlaying() {
        return isAudioPlaying;
    }

    public void setAudioPlaying(boolean audioPlaying) {
        isAudioPlaying = audioPlaying;
    }

    public long getCurrentPlayingPosition() {
        return currentPlayingPosition;
    }

    public void setCurrentPlayingPosition(long currentPlayingPosition) {
        this.currentPlayingPosition = currentPlayingPosition;
    }

    public Boolean getCastingConnected() {
        return isCastingConnected;
    }

    public void setCastingConnected(Boolean castingConnected) {
        isCastingConnected = castingConnected;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public boolean isSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(boolean selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(String summaryText) {
        this.summaryText = summaryText;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public GameTeamGist getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(GameTeamGist homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<GameTeamGist> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<GameTeamGist> leagues) {
        this.leagues = leagues;
    }

    public String getPrimaryCategoryTitle() {
        return primaryCategoryTitle;
    }

    public void setPrimaryCategoryTitle(String primaryCategoryTitle) {
        this.primaryCategoryTitle = primaryCategoryTitle;
    }

    public List<GameTeamGist> getVenues() {
        return venues;
    }

    public void setVenues(List<GameTeamGist> venues) {
        this.venues = venues;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public FeaturedTag getFeaturedTag() {
        return featuredTag;
    }

    public void setFeaturedTag(FeaturedTag featuredTag) {
        this.featuredTag = featuredTag;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getRsvpUrl() {
        return rsvpUrl;
    }

    public void setRsvpUrl(String rsvpUrl) {
        this.rsvpUrl = rsvpUrl;
    }

    public String getLivestreamUrl() {
        return livestreamUrl;
    }

    public void setLivestreamUrl(String livestreamUrl) {
        this.livestreamUrl = livestreamUrl;
    }

    public String getWebliveUrl() {
        return webliveUrl;
    }

    public void setWebliveUrl(String webliveUrl) {
        this.webliveUrl = webliveUrl;
    }

    public List<GameSchedule> getGameSchedule() {
        return gameSchedule;
    }

    public List<GameSchedule> getEventSchedule() {
        return eventSchedule;
    }

    public void setGameSchedule(List<GameSchedule> gameSchedule) {
        this.gameSchedule = gameSchedule;
    }

    public void setEventSchedule(List<GameSchedule> eventSchedule) {
        this.eventSchedule = eventSchedule;
    }

    public GameTeamGist getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(GameTeamGist awayTeam) {
        this.awayTeam = awayTeam;
    }

    public List<MetaData> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<MetaData> metadata) {
        this.metadata = metadata;
    }

    public String getOriginalObjectId() {
        return originalObjectId;
    }

    public void setOriginalObjectId(String originalObjectId) {
        this.originalObjectId = originalObjectId;
    }

    public String getEpisodeNum() {
        return episodeNum;
    }

    public void setEpisodeNum(String episodeNum) {
        this.episodeNum = episodeNum;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(String seasonNum) {
        this.seasonNum = seasonNum;
    }

    @Deprecated
    public List<Map<String, AppCMSTransactionDataValue>> getObjTransactionDataValue() {
        return objTransactionDataValue;
    }

    @Deprecated
    public void setObjTransactionDataValue(List<Map<String, AppCMSTransactionDataValue>> objTransactionDataValue) {
        this.objTransactionDataValue = objTransactionDataValue;
    }

    @Deprecated
    List<Map<String, AppCMSTransactionDataValue>> objTransactionDataValue;

    @Deprecated
    AppCMSTransactionDataValue transactionDataValue;

    @Deprecated
    public void setTransactionDataValue(AppCMSTransactionDataValue transactionDataValue) {
        this.transactionDataValue = transactionDataValue;
    }

    @Deprecated
    public AppCMSTransactionDataValue getTransactionDataValue() {
        return transactionDataValue;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isVideoPlaying() {
        return isVideoPlaying;
    }

    public void setVideoPlaying(boolean videoPlaying) {
        isVideoPlaying = videoPlaying;
    }

    public String getInstructorTitle() {
        return instructorTitle;
    }

    public void setInstructorTitle(String instructorTitle) {
        this.instructorTitle = instructorTitle;
    }

    public String getDurationCategory() {
        return durationCategory;
    }

    public void setDurationCategory(String durationCategory) {
        this.durationCategory = durationCategory;
    }

    public void setDisplayPath(String displayPath) {
        this.displayPath = displayPath;
    }

    public String getDisplayPath() {
        return displayPath;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public boolean isFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(boolean fullDescription) {
        this.fullDescription = fullDescription;
    }

    public int getPlayNextTime() {
        //return 50;
        return playNextTime;
    }

    public int getSkipIntroEndTime() {
        //return 35;
        return skipIntroEndTime;
    }

    public int getSkipIntroStartTime() {
        //return 25;
        return skipIntroStartTime;
    }

    public int getSkipRecapEndTime() {
        //return 20;
        return skipRecapEndTime;
    }

    public int getSkipRecapStartTime() {
        //return 10;
        return skipRecapStartTime;
    }
}

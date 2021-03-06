package com.viewlift.offlinedrm;

import android.text.TextUtils;

import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.ContentDetails;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.annotations.PrimaryKey;

/**
 * Created by sandeep.singh on 7/18/2017.
 */

public class OfflineVideoData implements Serializable {
    @PrimaryKey
    private String videoIdDB;
    private String videoId;
    private long videoId_DM;
    private long videoThumbId_DM;
    private long posterThumbId_DM;
    private long subtitlesId_DM;
    private String videoTitle;
    private String videoDescription;
    private String downloadStatus;
    private String videoWebURL;
    private String videoFileURL;
    private String localURI;
    private long videoSize;
    private long video_Downloaded_so_far;
    private long downloadDate;
    private long lastWatchDate;
    private long videoPlayedDuration;
    private long videoDuration;
    private int bitRate;
    private String showId;
    private String showTitle;
    private String showDescription;
    private String videoNumber;
    private String permalink;
    private String videoImageUrl;
    private String posterFileURL;
    private String subtitlesFileURL;
    private String subtitlesFileLanguage;
    private String subtitlesFileFormat;
    private String userId;
    private long watchedTime;
    private boolean isSyncedWithServer;
    public String contentType;
    public String mediaType;
    public String artistName;
    public String directorName;
    public String songYear;
    private String playListName;

    public long getCcFileEnqueueId() {
        return ccFileEnqueueId;
    }

    public void setCcFileEnqueueId(long ccFileEnqueueId) {
        this.ccFileEnqueueId = ccFileEnqueueId;
    }

    private long ccFileEnqueueId;

    private String parentalRating;

    public String getSubtitlesFileLanguage() {
        return subtitlesFileLanguage;
    }

    public void setSubtitlesFileLanguage(String subtitlesFileLanguage) {
        this.subtitlesFileLanguage = subtitlesFileLanguage;
    }

    public String getSubtitlesFileFormat() {
        return subtitlesFileFormat;
    }

    public void setSubtitlesFileFormat(String subtitlesFileFormat) {
        this.subtitlesFileFormat = subtitlesFileFormat;
    }

    public long getVideoSizeLength() {
        return videoSizeLength;
    }

    public void setVideoSizeLength(long videoSizeLength) {
        this.videoSizeLength = videoSizeLength;
    }

    private long videoSizeLength;

    public boolean isDrmEnabled() {
        return isDrmEnabled;
    }

    public void setDrmEnabled(boolean drmEnabled) {
        isDrmEnabled = drmEnabled;
    }

    private boolean isDrmEnabled;

    String episodeNum;
    String showName;
    String seasonNum;

    private long endDate;
    private long transactionEndDate;
    private String subscriptionType;
    private String instructorTitle;
    private String durationCategory;
    private long rentStartWatchTime;

    public enum DownloadCache {
        PHONE_CACHE,
        SDCARD_CACHE
    }

    DownloadCache downloadCache = DownloadCache.PHONE_CACHE;

    public DownloadCache getDownloadCache() {
        return downloadCache;
    }

    public void setDownloadCache(DownloadCache downloadCache) {
        this.downloadCache = downloadCache;
    }

    public byte[] getOfflineLicenseKeySetId() {
        return offlineLicenseKeySetId;
    }

    public void setOfflineLicenseKeySetId(byte[] offlineLicenseKeySetId) {
        this.offlineLicenseKeySetId = offlineLicenseKeySetId;
    }

    byte[] offlineLicenseKeySetId;

    public long getRentStartWatchTime() {
        return rentStartWatchTime;
    }

    public void setRentStartWatchTime(long rentStartWatchTime) {
        this.rentStartWatchTime = rentStartWatchTime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    String genre;

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

    public long getTransactionEndDate() {
        return transactionEndDate;
    }

    public void setTransactionEndDate(long transactionEndDate) {
        this.transactionEndDate = transactionEndDate;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }


    public boolean isRentStartTimeSyncedWithServer() {
        return isRentStartTimeSyncedWithServer;
    }

    public void setRentStartTimeSyncedWithServer(boolean rentStartTimeSyncedWithServer) {
        isRentStartTimeSyncedWithServer = rentStartTimeSyncedWithServer;
    }

    private boolean isRentStartTimeSyncedWithServer;
    private float rentalPeriod;

    public float getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(float rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }


    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
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

    public String getSongYear() {
        return songYear;
    }

    public void setSongYear(String songYear) {
        this.songYear = songYear;
    }


    public String getVideoIdDB() {
        return videoIdDB;
    }

    public void setVideoIdDB(String videoIdDB) {
        this.videoIdDB = videoIdDB;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public long getVideoId_DM() {
        return videoId_DM;
    }

    public void setVideoId_DM(long videoId_DM) {
        this.videoId_DM = videoId_DM;
    }

    public long getVideoThumbId_DM() {
        return videoThumbId_DM;
    }

    public void setVideoThumbId_DM(long videoThumbId_DM) {
        this.videoThumbId_DM = videoThumbId_DM;
    }

    public long getPosterThumbId_DM() {
        return posterThumbId_DM;
    }

    public void setPosterThumbId_DM(long posterThumbId_DM) {
        this.posterThumbId_DM = posterThumbId_DM;
    }

    public long getSubtitlesId_DM() {
        return subtitlesId_DM;
    }

    public void setSubtitlesId_DM(long subtitlesId_DM) {
        this.subtitlesId_DM = subtitlesId_DM;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public int getOfflineVideoStatus() {
        return offlineVideoStatus;
    }

    public void setOfflineVideoStatus(int offlineVideoStatus) {
        this.offlineVideoStatus = offlineVideoStatus;
    }

    public int offlineVideoStatus;

    public DownloadStatus getDownloadStatus() {
        return DownloadStatus.valueOf(downloadStatus);
    }

    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus.toString();
    }

    public String getVideoWebURL() {
        return videoWebURL;
    }

    public void setVideoWebURL(String videoWebURL) {
        this.videoWebURL = videoWebURL;
    }

    public String getVideoFileURL() {
        return videoFileURL;
    }

    public void setVideoFileURL(String videoFileURL) {
        this.videoFileURL = videoFileURL;
    }

    public String getPosterFileURL() {
        return posterFileURL;
    }

    public void setPosterFileURL(String posterFileURL) {
        this.posterFileURL = posterFileURL;
    }

    public String getSubtitlesFileURL() {
        return subtitlesFileURL;
    }

    public void setSubtitlesFileURL(String subtitlesFileURL) {
        this.subtitlesFileURL = subtitlesFileURL;
    }

    public String getLocalURI() {
        return localURI;
    }

    public void setLocalURI(String localURI) {
        this.localURI = localURI;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public long getVideo_Downloaded_so_far() {
        return video_Downloaded_so_far;
    }

    public void setVideo_Downloaded_so_far(long video_Downloaded_so_far) {
        this.video_Downloaded_so_far = video_Downloaded_so_far;
    }

    public long getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(long downloadDate) {
        this.downloadDate = downloadDate;
    }

    public long getLastWatchDate() {
        return lastWatchDate;
    }

    public void setLastWatchDate(long lastWatchDate) {
        this.lastWatchDate = lastWatchDate;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public long getVideoPlayedDuration() {
        return videoPlayedDuration;
    }

    public void setVideoPlayedDuration(long videoPlayedDuration) {
        this.videoPlayedDuration = videoPlayedDuration;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public String getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(String showDescription) {
        this.showDescription = showDescription;
    }

    public String getVideoNumber() {
        return videoNumber;
    }

    public void setVideoNumber(String videoNumber) {
        this.videoNumber = videoNumber;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getWatchedTime() {
        return watchedTime;
    }

    public void setWatchedTime(long watchedTime) {
        this.watchedTime = watchedTime;
    }

    public boolean isSyncedWithServer() {
        return isSyncedWithServer;
    }

    public void setSyncedWithServer(boolean syncedWithServer) {
        isSyncedWithServer = syncedWithServer;
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

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public ContentDatum convertToContentDatum(String userId) {
        ContentDatum data = new ContentDatum();
        Gist gist = new Gist();
        gist.setId(getVideoId());
        gist.setTitle(getVideoTitle());
        gist.setDescription(getVideoDescription());
        gist.setPosterImageUrl(getPosterFileURL());
        gist.setVideoImageUrl(getVideoFileURL());
        gist.setYear(getSongYear());
        gist.setArtistName(getArtistName());
        gist.setDirectorName(getDirectorName());
        gist.setLocalFileUrl(getLocalURI());
        data.setDRMEnabled(isDrmEnabled);

        if (!TextUtils.isEmpty(getSubtitlesFileURL())) {
            ClosedCaptions closedCaption = new ClosedCaptions();
            closedCaption.setUrl(getSubtitlesFileURL());
            ArrayList<ClosedCaptions> closedCaptions = new ArrayList<>();
            closedCaptions.add(closedCaption);
            ContentDetails contentDetails = new ContentDetails();
            contentDetails.setClosedCaptions(closedCaptions);
            data.setContentDetails(contentDetails);
        }
        gist.setPermalink(getPermalink());
        //gist.setDownloadStatus(getDownloadStatus());
        gist.setRuntime(getVideoDuration());
        String endDate = "";
        Date date = new Date(getTransactionEndDate());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        endDate = df2.format(date);
        gist.setTransactionEndDate(endDate);
        gist.setRentStartTime(getRentStartWatchTime());
        gist.setRentalPeriod(getRentalPeriod());
        gist.setRentStartTimeSyncedWithServer(isRentStartTimeSyncedWithServer());
        gist.setSubscriptionType(getSubscriptionType());
        gist.setWatchedTime(getWatchedTime());
        data.setGist(gist);
        data.setShowQueue(true);
        data.setUserId(userId);
        data.setAddedDate(getDownloadDate());
        gist.setContentType(getContentType());
        gist.setMediaType(getMediaType());
        gist.setEpisodeNum(getEpisodeNum());
        gist.setShowName(getShowName());
        gist.setSeasonNum(getSeasonNum());
        gist.setGenre(getGenre());
        gist.setInstructorTitle(getInstructorTitle());
        gist.setDurationCategory(getDurationCategory());
        data.setParentalRating(parentalRating);
        return data;
    }

    public OfflineVideoData createCopy() {
        OfflineVideoData downloadVideoRealm = new OfflineVideoData();
        downloadVideoRealm.setEpisodeNum(getEpisodeNum());
        downloadVideoRealm.setShowName(getShowName());
        downloadVideoRealm.setSeasonNum(getSeasonNum());
        downloadVideoRealm.setVideoId(getVideoId());
        downloadVideoRealm.setDownloadStatus(getDownloadStatus());
        downloadVideoRealm.setSyncedWithServer(isSyncedWithServer);
        downloadVideoRealm.setVideoId_DM(getVideoId_DM());
        downloadVideoRealm.setVideoDuration(getVideoDuration());
        /**
         * added to check rent expire time
         */
        downloadVideoRealm.setTransactionEndDate(getTransactionEndDate());
        downloadVideoRealm.setRentalPeriod(getRentalPeriod());

        downloadVideoRealm.setSubscriptionType(getSubscriptionType());
        downloadVideoRealm.setVideo_Downloaded_so_far(getVideo_Downloaded_so_far());
        downloadVideoRealm.setVideoFileURL(getVideoFileURL());
        downloadVideoRealm.setVideoSize(getVideoSize());
        downloadVideoRealm.setVideoImageUrl(getVideoImageUrl());
        downloadVideoRealm.setVideoWebURL(getVideoWebURL());
        downloadVideoRealm.setVideoDescription(getVideoDescription());
        downloadVideoRealm.setVideoTitle(getVideoTitle());
        downloadVideoRealm.setVideoNumber(getVideoNumber());
        downloadVideoRealm.setVideoPlayedDuration(getVideoPlayedDuration());
        downloadVideoRealm.setVideoThumbId_DM(getVideoThumbId_DM());
        downloadVideoRealm.setShowId(getShowId());
        downloadVideoRealm.setShowDescription(getShowDescription());
        downloadVideoRealm.setShowTitle(getShowTitle());
        downloadVideoRealm.setSubtitlesFileURL(getSubtitlesFileURL());
        downloadVideoRealm.setSubtitlesId_DM(getSubtitlesId_DM());
        downloadVideoRealm.setWatchedTime(getWatchedTime());
        downloadVideoRealm.setBitRate(getBitRate());
        downloadVideoRealm.setLastWatchDate(getLastWatchDate());
        downloadVideoRealm.setDownloadDate(getDownloadDate());
        downloadVideoRealm.setLocalURI(getLocalURI());
        downloadVideoRealm.setUserId(getUserId());
        downloadVideoRealm.setPermalink(getPermalink());
        downloadVideoRealm.setPosterFileURL(getPosterFileURL());
        downloadVideoRealm.setPosterThumbId_DM(getPosterThumbId_DM());
        downloadVideoRealm.setContentType(getContentType());
        downloadVideoRealm.setMediaType(getMediaType());
        downloadVideoRealm.setGenre(getGenre());
        downloadVideoRealm.setInstructorTitle(getInstructorTitle());
        downloadVideoRealm.setDurationCategory(getDurationCategory());
        downloadVideoRealm.setParentalRating(parentalRating);
        return downloadVideoRealm;
    }
}
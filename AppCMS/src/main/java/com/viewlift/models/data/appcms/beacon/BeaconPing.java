package com.viewlift.models.data.appcms.beacon;

import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.VideoPlayerView;

public class BeaconPing extends Thread {
    public AppCMSPresenter appCMSPresenter;
    public String filmId;
    public String seriesId;
    public String permaLink;
    public VideoPlayerView videoPlayerView;
    public boolean runBeaconPing;
    public boolean sendBeaconPing;
    public boolean isTrailer;
    public int playbackState;
    private long beaconMsgTimeoutMsec;
    private String parentScreenName;
    private String streamId;
    private long liveSeekCounter;
    private static final long MILLISECONDS_PER_SECOND = 1L;
    private ContentDatum contentDatum;
    private int bufferCount;
    private int bufferTime;
    private String lastPlayType = "";
    private boolean sent2MinEvent, sent25pctEvent, sent50pctEvent, sent75pctEvent, sent80pctEvent, sent30secMusicEvent;
    private long lastEventStreamTime;
    private long lastEventStreamTimeMusic;

    private boolean PLAY_WHEN_READY = false;
    private long PLAYER_BITRATE;
    private long PLAYER_CURRENT_POSITION;
    private long PLAY_DURATION;
    private int PLAYER_STATUS;
    private int VIDEO_WIDTH;
    private int VIDEO_HEIGHT;
    private int interval = 90;

    public BeaconPing(long beaconMsgTimeoutMsec,
                      AppCMSPresenter appCMSPresenter,
                      String filmId,
                      String permaLink,
                      boolean isTrailer,
                      String parentScreenName,
                      VideoPlayerView videoPlayerView,
                      String streamId,
                      ContentDatum contentDatum) {
        this.beaconMsgTimeoutMsec = beaconMsgTimeoutMsec;
        this.appCMSPresenter = appCMSPresenter;
        this.filmId = filmId;
        this.permaLink = permaLink;
        this.parentScreenName = parentScreenName;
        this.videoPlayerView = videoPlayerView;
        this.isTrailer = isTrailer;
        this.streamId = streamId;
        this.contentDatum = contentDatum;
        if (contentDatum != null && contentDatum.getGist() != null
                && contentDatum.getGist().getSeriesId() != null
                && !TextUtils.isEmpty(contentDatum.getGist().getSeriesId())) {
            this.seriesId = contentDatum.getGist().getSeriesId();
        }
        this.liveSeekCounter = MILLISECONDS_PER_SECOND;
        sent2MinEvent = false;
        sent25pctEvent = false;
        sent50pctEvent = false;
        sent75pctEvent = false;
        sent80pctEvent = false;
        lastEventStreamTime = 0;
        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon() != null) {
            interval = appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().getIntervalInt();
        }
    }

    @Override
    public void run() {
        runBeaconPing = true;
        boolean isPlaying = false;
        while (runBeaconPing) {
            try {
                Thread.sleep(beaconMsgTimeoutMsec);
                if (sendBeaconPing) {
                    isPlaying = PLAY_WHEN_READY;
                    long currentTime = PLAYER_CURRENT_POSITION / 1000;
                    if (contentDatum != null &&
                            contentDatum.getGist() != null &&
                            !contentDatum.getGist().isLiveStream()) {
                        currentTime = PLAYER_CURRENT_POSITION / 1000;
                    } else if (contentDatum != null &&
                            contentDatum.getGist() != null &&
                            contentDatum.getGist().isLiveStream()) {
                        if (isPlaying) {
                            liveSeekCounter += MILLISECONDS_PER_SECOND;
                        }
                        currentTime = liveSeekCounter;

                    }
                    if (appCMSPresenter != null && isPlaying
                            && PLAYER_STATUS == Player.STATE_READY) {

                        if (interval <= currentTime &&
                                currentTime % interval == 0) {
                            if (contentDatum != null && contentDatum.getMediaType() == null) {
                                contentDatum.setMediaType("video");
                            }
                           // Log.d("BeaconPing", "Beacon Message Request position: " + currentTime);

                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().getAnalyticsBeacon().isEnabled()){
                            appCMSPresenter.sendBeaconMessage(filmId,
                                    permaLink,
                                    parentScreenName,
                                    currentTime,
                                    false,
                                    AppCMSPresenter.BeaconEvent.PING,
                                    contentDatum != null ? contentDatum.getMediaType() : "Video",
                                    PLAYER_BITRATE != 0 ?
                                            String.valueOf(PLAYER_BITRATE) : null,
                                    String.valueOf(VIDEO_HEIGHT),
                                    String.valueOf(VIDEO_WIDTH),
                                    streamId,
                                    0d,
                                    0,
                                    appCMSPresenter.isVideoDownloaded(filmId));
                            }
                        }
                        boolean isWatchHistoryUpdateEnabled = appCMSPresenter.getAppCMSMain().getFeatures() != null
                                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory() != null
                                && appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().isEnabled();
                        if (isWatchHistoryUpdateEnabled) {
                            int interval = appCMSPresenter.getAppCMSMain().getFeatures().getWatchedHistory().getIntervalInt();
                            if (currentTime > 0 && currentTime % interval == 0) {
                                if (!isTrailer && contentDatum != null &&
                                        contentDatum.getGist() != null &&
                                        !contentDatum.getGist().isLiveStream()) {
                                    appCMSPresenter.updateWatchedTime(filmId, seriesId,
                                            PLAYER_CURRENT_POSITION / 1000, updateHistoryResponse -> {
                                                if (updateHistoryResponse != null && updateHistoryResponse.getResponseCode() == 401 && updateHistoryResponse.getErrorCode() != null) {
                                                    if (!TextUtils.isEmpty(updateHistoryResponse.getErrorCode())
                                                            && updateHistoryResponse.getErrorCode().equalsIgnoreCase("MAX_STREAMS_ERROR")) {
                                                        if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)) {
                                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.MAX_STREAMS_ERROR, appCMSPresenter.getLocalisedStrings().getMaxStreamErrorText(),
                                                                    false, () -> {
                                                                        appCMSPresenter.getCurrentActivity().finish();
                                                                    }, null, null);
                                                            if (videoPlayerView != null)
                                                                videoPlayerView.pausePlayer();
                                                        } else {

                                                            appCMSPresenter.openTVMaxSimultaneousStreamDialog(updateHistoryResponse.getErrorMessage());

                                                            if (videoPlayerView != null) {
                                                                videoPlayerView.pausePlayer();
                                                            }
                                                        }
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                        if (currentTime == 120) {
                            lastEventStreamTime = currentTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "2mins", bufferCount, bufferTime);
                            sent2MinEvent = true;
                        }
                        if (currentTime == (int) ((PLAY_DURATION / 1000) * 0.25)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "25", bufferCount, bufferTime);
                            bufferCount = 0;
                            bufferTime = 0;
                            sent2MinEvent = true;
                            sent25pctEvent = true;
                        }
                        if (currentTime == (int) ((PLAY_DURATION / 1000) * 0.5)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "50", bufferCount, bufferTime);
                            bufferCount = 0;
                            bufferTime = 0;
                            sent25pctEvent = true;
                            sent50pctEvent = true;
                        }
                        if (currentTime == (int) ((PLAY_DURATION / 1000) * 0.75)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "75", bufferCount, bufferTime);
                            bufferCount = 0;
                            bufferTime = 0;
                            sent50pctEvent = true;
                            sent75pctEvent = true;
                        }
                        if (currentTime == (int) ((PLAY_DURATION / 1000) * 0.8)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "80", bufferCount, bufferTime);
                            sent75pctEvent = true;
                            sent80pctEvent = true;
                        }

                        if (!sent2MinEvent && currentTime > 120 && currentTime < (int) ((PLAY_DURATION / 1000) * 0.25)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "2mins", bufferCount, bufferTime);
                            sent2MinEvent = true;
                        }
                        if (!sent25pctEvent && currentTime > (int) ((PLAY_DURATION / 1000) * 0.25) && currentTime < (int) ((PLAY_DURATION / 1000) * 0.5)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "25", bufferCount, bufferTime);
                            sent2MinEvent = true;
                            sent25pctEvent = true;
                        }
                        if (!sent50pctEvent && currentTime > (int) ((PLAY_DURATION / 1000) * 0.5) && currentTime < (int) ((PLAY_DURATION / 1000) * 0.5)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "50", bufferCount, bufferTime);
                            sent25pctEvent = true;
                            sent50pctEvent = true;
                        }
                        if (!sent75pctEvent && currentTime > (int) ((PLAY_DURATION / 1000) * 0.75) && currentTime < (int) ((PLAY_DURATION / 1000) * 0.8)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "75", bufferCount, bufferTime);
                            sent50pctEvent = true;
                            sent75pctEvent = true;
                        }
                        if (!sent80pctEvent && currentTime > (int) ((PLAY_DURATION / 1000) * 0.8)) {
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "80", bufferCount, bufferTime);
                            sent75pctEvent = true;
                            sent80pctEvent = true;
                        }


                    }

                    if (appCMSPresenter != null && PLAYER_STATUS == ExoPlayer.STATE_ENDED) {
                        if (!sent80pctEvent && currentTime > (int) ((PLAY_DURATION / 1000) * 0.8)) {
                            System.out.println("Bit rate 80");
                            lastEventStreamTime = currentTime - lastEventStreamTime;
                            appCMSPresenter.sendWatchedEvent(contentDatum, lastEventStreamTime, "80", bufferCount, bufferTime);
                            sent75pctEvent = true;
                            sent80pctEvent = true;
                        }
                    }
                    if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null && contentDatum != null &&
                            contentDatum.getGist() != null && contentDatum.getGist().getMediaType() != null &&
                            contentDatum.getGist().getMediaType().toLowerCase().contains(appCMSPresenter.getCurrentActivity().getString(R.string.media_type_audio).toLowerCase()) &&
                            contentDatum.getGist().getContentType() != null &&
                            contentDatum.getGist().getContentType().toLowerCase().contains(appCMSPresenter.getCurrentActivity().getString(R.string.content_type_audio).toLowerCase())
                            && isPlaying) {
                        currentTime = contentDatum.getGist().getCurrentPlayingPosition() / 1000;
                        if (interval <= currentTime
                                && currentTime % interval == 0) {
                            appCMSPresenter.sendBeaconMessage(contentDatum.getGist().getId(),
                                    contentDatum.getGist().getPermalink(),
                                    null,
                                    currentTime,
                                    contentDatum.getGist().getCastingConnected(),
                                    AppCMSPresenter.BeaconEvent.PING,
                                    contentDatum.getGist().getMediaType(),
                                    null,
                                    null,
                                    null,
                                    getStreamId(),
                                    0d,
                                    0,
                                    appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId()));
                        }

                        if (currentTime == interval) {
                            appCMSPresenter.sendWatchedEvent(contentDatum, currentTime, "30", bufferCount, bufferTime);
                            sent30secMusicEvent = true;
                        }

                        if (!sent30secMusicEvent && currentTime > interval) {
                            appCMSPresenter.sendWatchedEvent(contentDatum, currentTime, "30", bufferCount, bufferTime);
                            sent30secMusicEvent = true;
                        }

                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                //Log.e(TAG, "BeaconPingThread sleep interrupted");
            } catch (InterruptedException e) {
                e.printStackTrace();
                //Log.e(TAG, "BeaconPingThread sleep interrupted");
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public synchronized void updatedPlayerValues(boolean playWhenReady,
                                                 long bitrate,
                                                 long currentPosition,
                                                 long duration,
                                                 int playerState,
                                                 int height,
                                                 int width) {
        PLAY_WHEN_READY = playWhenReady;
        PLAYER_BITRATE = bitrate;
        PLAYER_CURRENT_POSITION = currentPosition;
        PLAY_DURATION = duration;
        PLAYER_STATUS = playerState;
        VIDEO_HEIGHT = height;
        VIDEO_WIDTH = width;

    }

    public void setBeaconData(String videoId, String permaLink, String streamId) {
        this.filmId = videoId;
        this.permaLink = permaLink;
        this.streamId = streamId;
        this.liveSeekCounter = MILLISECONDS_PER_SECOND;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setContentDatum(ContentDatum contentDatum) {
        this.contentDatum = contentDatum;
        if (contentDatum != null && contentDatum.getGist() != null
                && contentDatum.getGist().getSeriesId() != null
                && !TextUtils.isEmpty(contentDatum.getGist().getSeriesId())) {
            this.seriesId = contentDatum.getGist().getSeriesId();
        }
    }

    public void setBufferCount(int bufferCount) {
        this.bufferCount = bufferCount;
    }

    public void setBufferTime(int bufferTime) {
        this.bufferTime = bufferTime;
    }

    public void setTrailer(boolean trailer) {
        isTrailer = trailer;
    }
}
package com.viewlift.casting;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.common.images.WebImage;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.ContentDetails;
import com.viewlift.models.data.appcms.api.Mpeg;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.BaseView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CastingUtils {

    private static final String TAG = "CastingUtils";

    public static String MEDIA_KEY = "media_key";
    public static String PARAM_KEY = "param_key";
    public static String VIDEO_TITLE = "video_title";
    public static final String ITEM_TYPE = "item_type";
    public static final String ITEM_TYPE_AUDIO = "item_type_audio";
    public static final String ITEM_TYPE_VIDEO = "item_type_video";
    public static final String MIME_TYPE_HLS = "application/x-mpegurl";
    public static final String MIME_TYPE_MP4 = "videos/mp4";
    public static String MIME_TYPE = MIME_TYPE_MP4;

    public static boolean isRemoteMediaControllerOpen = false;
    public static boolean isMediaQueueLoaded = true;
    public static boolean isLiveStream = false;
    public static String castingMediaId = "";

    public static int CASTING_MODE_CHROMECAST = 1;
    public static int CASTING_MODE_ROKU = 2;

    public static int routerDevices = 0;
    private static final int PRELOAD_TIME_S = 20;

    public static final boolean IS_CHROMECAST_ENABLE = true;


    /*public static MediaQueueItem[] BuildCastingQueueItems(List<ContentDatum> detailsRelatedVideoData,
                                                          String appName,
                                                          List<String> listCompareRelatedVideosId,
                                                          Context context) {

        MediaQueueItem[] queueItemsArray;
        String appPackageName=context.getPackageName();

        if (detailsRelatedVideoData != null && detailsRelatedVideoData.size() > 0) {

            queueItemsArray = new MediaQueueItem[detailsRelatedVideoData.size()];

            for (int i = 0; i < queueItemsArray.length; i++) {
                JSONObject seasonObj = new JSONObject();
                try {
                    seasonObj.put(MEDIA_KEY, detailsRelatedVideoData.get(i).getGist().getId());
                    seasonObj.put(PARAM_KEY, detailsRelatedVideoData.get(i).getGist().getPermalink());
                    seasonObj.put(ITEM_TYPE, appPackageName+""+CastingUtils.ITEM_TYPE_VIDEO);
                    if(detailsRelatedVideoData.get(i).getGist().getTitle()!=null) {
                        seasonObj.put(VIDEO_TITLE, detailsRelatedVideoData.get(i).getGist().getTitle());
                    }

                } catch (Exception e) {
                    //Log.e(TAG, "Error create session JSON object: " + e.getMessage());
                }

                int watchTime = (int) detailsRelatedVideoData.get(i).getGist().getWatchedTime();
                //Log.e(TAG, "Added watched Time: " + watchTime);
                String playingUrl= getPlayingUrl(detailsRelatedVideoData.get(i));

                if (playingUrl != null && !TextUtils.isEmpty(playingUrl)) {
                    int currentPlayingIndex = listCompareRelatedVideosId.indexOf(detailsRelatedVideoData.get(i).getGist().getId());

                    if (0 <= currentPlayingIndex && currentPlayingIndex < queueItemsArray.length) {
                        queueItemsArray[currentPlayingIndex] = new MediaQueueItem.Builder(buildMediaInfoFromList(detailsRelatedVideoData.get(i), appName, context,playingUrl))
                                .setAutoplay(true)
                                .setPreloadTime(PRELOAD_TIME_S)
                                .setCustomData(seasonObj).setStartTime(watchTime)
                                .build();
                    }
                }

            }

            return queueItemsArray;

        }
        return null;
    }*/

    public static MediaQueueItem buildSingleCastingQueueItem(ContentDatum detailsRelatedVideoData,
                                              String appName,
                                              List<String> listCompareRelatedVideosId,
                                              Context context){
        MediaQueueItem mediaQueueItem = null;
        String appPackageName=context.getPackageName();
        JSONObject seasonObj = new JSONObject();
        try {
            seasonObj.put(MEDIA_KEY, detailsRelatedVideoData.getGist().getId());
            seasonObj.put(PARAM_KEY, detailsRelatedVideoData.getGist().getPermalink());
            seasonObj.put(ITEM_TYPE, appPackageName+""+CastingUtils.ITEM_TYPE_VIDEO);
            if(detailsRelatedVideoData.getGist().getTitle()!=null) {
                seasonObj.put(VIDEO_TITLE, detailsRelatedVideoData.getGist().getTitle());
            }

        } catch (Exception e) {
        }

        int watchTime = (int) detailsRelatedVideoData.getGist().getWatchedTime();
        String playingUrl= getPlayingUrl(detailsRelatedVideoData);

        if (playingUrl != null && !TextUtils.isEmpty(playingUrl)) {
            int currentPlayingIndex = listCompareRelatedVideosId.indexOf(detailsRelatedVideoData.getGist().getId());

             mediaQueueItem = new MediaQueueItem.Builder(buildMediaInfoFromList(detailsRelatedVideoData, appName, context,playingUrl))
                        .setAutoplay(true)
                        .setPreloadTime(PRELOAD_TIME_S)
                        .setCustomData(seasonObj).setStartTime(watchTime)
                        .build();
        }
        return mediaQueueItem;
    }

    public static MediaInfo buildMediaInfoFromList(ContentDatum contentData,
                                                   String appName,
                                                   Context context,
                                                   String playingURL) {
        String titleMediaInfo = "";
        String subTitleMediaInfo = "";
        String imageMediaInfo = "";
        String urlMediaInfo = "";
        String appPackageName=context.getPackageName();

        JSONObject medoaInfoCustomData = new JSONObject();
        try {
            medoaInfoCustomData.put(MEDIA_KEY, contentData.getGist().getId());
            medoaInfoCustomData.put(PARAM_KEY, contentData.getGist().getPermalink());
            medoaInfoCustomData.put(ITEM_TYPE, appPackageName+""+CastingUtils.ITEM_TYPE_VIDEO);
            if(contentData.getGist().getTitle()!=null) {
                medoaInfoCustomData.put(VIDEO_TITLE, contentData.getGist().getTitle());
            }
        } catch (JSONException e) {
        }
        if (contentData != null) {
            titleMediaInfo = contentData.getGist().getTitle();
            subTitleMediaInfo = appName;
            if (contentData.getContentDetails().getVideoImage() != null && contentData.getContentDetails().getVideoImage().getSecureUrl() != null) {
                imageMediaInfo = contentData.getContentDetails().getVideoImage().getSecureUrl();
            }
        }
        urlMediaInfo = playingURL;
        return buildMediaInfo(titleMediaInfo,
                subTitleMediaInfo,
                imageMediaInfo,
                urlMediaInfo,
                medoaInfoCustomData,
                context, getMediaTracks(contentData));
    }

    public static String getPlayingUrl(ContentDatum contentData) {
        String playUrl = "";
        if (contentData != null && contentData.getStreamingInfo() != null && contentData.getStreamingInfo().getVideoAssets() != null) {

            if(contentData.getStreamingInfo().getVideoAssets().getWideVine()!=null && !TextUtils.isEmpty(contentData.getStreamingInfo().getVideoAssets().getWideVine().getUrl())){
                playUrl = contentData.getStreamingInfo().getVideoAssets().getWideVine().getUrl();

            }else if (contentData.getStreamingInfo().getVideoAssets().getHls() != null
                    && !TextUtils.isEmpty(contentData.getStreamingInfo().getVideoAssets().getHls())) {
                playUrl = contentData.getStreamingInfo().getVideoAssets().getHls();
                MIME_TYPE =MIME_TYPE_HLS;
            }else if (contentData.getStreamingInfo().getVideoAssets().getMpeg() != null
                    && contentData.getStreamingInfo().getVideoAssets().getMpeg().size() > 0) {
                   // playUrl = contentData.getStreamingInfo().getVideoAssets().getMpeg().get(0).getUrl();
                    playUrl = getMpedURL(contentData.getStreamingInfo().getVideoAssets().getMpeg());
                MIME_TYPE =MIME_TYPE_MP4;
            }

        }
        if (!CommonUtils.isIsCustomReceiverIdExist() &&
                contentData.getStreamingInfo()!=null
                && contentData.getStreamingInfo().getVideoAssets()!=null
                && contentData.getStreamingInfo().getVideoAssets().getHlsDetail() !=null
                && contentData.getStreamingInfo().getVideoAssets().getHlsDetail().getSignature() !=null
                && contentData.getStreamingInfo().getVideoAssets().getHlsDetail().getPolicy() !=null
                && contentData.getStreamingInfo().getVideoAssets().getHlsDetail().getKeypairId() !=null){
            playUrl = getMpedURL(contentData.getStreamingInfo().getVideoAssets().getMpeg());
            MIME_TYPE =MIME_TYPE_MP4;
        }

        /*if (playUrl != null && playUrl.contains("Policy=")
                && playUrl.contains("Key-Pair-Id=")
                && playUrl.contains("Signature=")
                && playUrl.contains("?")) {
            playUrl = playUrl.substring(0, playUrl.indexOf("?"));
        }*/
        return playUrl;
    }

    public static String getTitle(ContentDatum contentData, boolean isTrailer) {
        String videoTitle = "";
        if (!isTrailer && contentData != null && contentData.getGist() != null && contentData.getGist().getTitle() != null) {
            videoTitle = contentData.getGist().getTitle();
        } else if (isTrailer && contentData != null && contentData.getContentDetails() != null && contentData.getContentDetails().getTrailers() != null
                && contentData.getContentDetails().getTrailers().size() > 0 && contentData.getContentDetails().getTrailers().get(0) != null) {
            videoTitle = contentData.getContentDetails().getTrailers().get(0).getTitle();
        }

        return videoTitle;
    }

    public static MediaInfo buildMediaInfo(String Title,
                                           String subtitle,
                                           String image,
                                           String url,
                                           JSONObject medoaInfoCustomData,
                                           Context context, List<MediaTrack> tracks) {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, subtitle);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, Title);
        int imageWidth = BaseView.getDeviceWidth();
        int imageHeight = BaseView.getDeviceHeight();
        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                image,
                imageWidth,
                imageHeight);
        movieMetadata.addImage(new WebImage(Uri.parse(imageUrl),
                imageWidth,
                imageHeight));

        return new MediaInfo.Builder(url)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(MIME_TYPE)
                .setMetadata(movieMetadata)
                .setMediaTracks(tracks) /*Selected SubTitle*/
                .setCustomData(medoaInfoCustomData)
                .build();
    }

    public static String getRemoteMediaId(Context mContext) {
        JSONObject getRemoteObject = null;
        String remoteMediaId = "";
        try {
            getRemoteObject = CastContext.getSharedInstance(mContext).getSessionManager().getCurrentCastSession().getRemoteMediaClient().getCurrentItem().getCustomData();
            remoteMediaId = getRemoteObject.getString(CastingUtils.MEDIA_KEY);
        } catch (Exception e) {
        }

        try {
            getRemoteObject = CastContext.getSharedInstance(mContext).getSessionManager().getCurrentCastSession().getRemoteMediaClient().getMediaInfo().getCustomData();
            remoteMediaId = getRemoteObject.getString(CastingUtils.MEDIA_KEY);
        } catch (Exception e) {
        }
        return remoteMediaId;
    }

    public static String getCurrentPlayingVideoName(Context mContext) {
        JSONObject getRemoteObject = null;
        String remoteParamKey = "";
        try {
            getRemoteObject = CastContext.getSharedInstance(mContext).getSessionManager().getCurrentCastSession().getRemoteMediaClient().getMediaInfo().getCustomData();
            remoteParamKey = getRemoteObject.getString(CastingUtils.VIDEO_TITLE);
        } catch (Exception e) {
            remoteParamKey = mContext.getResources().getString(R.string.app_cms_touch_to_cast_msg);
        }
        return remoteParamKey;
    }


    public static String getRemoteParamKey(Context mContext) {
        JSONObject getRemoteObject = null;
        String remoteParamKey = "";
        try {
            getRemoteObject = CastContext.getSharedInstance(mContext).getSessionManager().getCurrentCastSession().getRemoteMediaClient().getMediaInfo().getCustomData();
            remoteParamKey = getRemoteObject.getString(CastingUtils.PARAM_KEY);
        } catch (Exception e) {
        }
        return remoteParamKey;
    }

    public static String getMpedURL(List<Mpeg> mpegs){
        String url;
        Comparator<Mpeg> compareByBitrate;
        /*if (mpegs.get(0).getBitrate() >= 0) {
            compareByBitrate = (Mpeg o1, Mpeg o2) -> (o1.getBitrate() > o2.getBitrate());
        }else if (mpegs.get(0).getRenditionValue() != null && !TextUtils.isEmpty(mpegs.get(0).getRenditionValue()) ){
            compareByBitrate = (Mpeg o1, Mpeg o2) -> o1.getRenditionValue().compareTo(o2.getRenditionValue());
        }*/
       // compareByBitrate = (Mpeg o1, Mpeg o2) -> o1.getRenditionValue().compareTo(o2.getRenditionValue());
        compareByBitrate = (Mpeg o1, Mpeg o2) -> o2.getRenditionValue().compareTo(o1.getRenditionValue());
        Collections.sort(mpegs, compareByBitrate);

       // int index= mpegs.size()/2;
       // url = mpegs.get(index).getUrl();
        url = mpegs.get(0).getUrl();
        return url;
    }

    public static List<MediaTrack> getMediaTracks(ContentDatum contentDatum) {
        List<MediaTrack> tracks = new ArrayList<>();
        ContentDetails details  = contentDatum.getContentDetails();
        if (details != null && details.getClosedCaptions() != null && !details.getClosedCaptions().isEmpty()) {
            for (ClosedCaptions cc : details.getClosedCaptions()) {
                if (cc.getUrl() != null && !cc.getUrl().equalsIgnoreCase("file:///") && cc.getFormat() != null && "VTT".equalsIgnoreCase(cc.getFormat())) {
                    tracks.add(new MediaTrack.Builder(1 /* ID */,
                            MediaTrack.TYPE_TEXT)
                            .setName(cc.getLanguage())
                            .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                            .setContentId(cc.getUrl())
                            /* language is required for subtitle type but optional otherwise */
                            .setLanguage(cc.getLanguage())
                            .build());
                }
            }
        }
        return tracks;
    }
}

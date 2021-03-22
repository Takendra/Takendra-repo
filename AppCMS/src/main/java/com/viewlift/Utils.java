package com.viewlift;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.beacon.IapEvent;
import com.viewlift.models.data.appcms.downloads.DownloadClosedCaptionRealm;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Utils {

    private static String AMAZON_FEATURE_FIRE_TV = "amazon.hardware.fire_tv";

    private static String addId;
    private static String countryCode;
    private static double latitude;
    public static boolean pullToRefresh = false;
    public static int position = 0;
    public static int catagoryPosition = 0;
    private static String ipAddress;

    public static String getProperty(String key, Context context) {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try {

            InputStream inputStream = assetManager.open("version.properties");
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String loadJsonFromAssets(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    /*The startService() method now throws an IllegalStateException if an app targeting Android 8.0 tries to use that method in a situation when it isn't permitted to create background services.
    The new Context.startForegroundService() method starts a foreground service. The system allows apps to call Context.startForegroundService() even while the app is in the background.*/
    public static void startService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intent);
        else
            context.startService(intent);
    }

    private static boolean hls;

    public static boolean isHLS() {
        return Utils.hls;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getNetworkType(Context context) {
        String type = "";
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    type = "WiFi";
                else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    type = "Cellular";
            }
        }
        return type;
    }

    public static void setHls(boolean hls) {
        Utils.hls = hls;
    }

    public static boolean isFireTVDevice(Context context) {
        return context.getPackageManager().hasSystemFeature(AMAZON_FEATURE_FIRE_TV);
    }

    public static List<String> getRelatedVideosInShow2(List<Season_> season, int showNumber, int episodeNumber, String episodeId) {
        List<String> relatedVids = new ArrayList<>();
        boolean foundEpisode = false;
        for (int i = showNumber; i < season.size(); i++) {
            if (i == showNumber) {
                for (int j = episodeNumber + 1; j < season.get(i).getEpisodes().size(); j++) {
                    String episode = season.get(i).getEpisodes().get(j).getId();
                    if (foundEpisode || episode.equalsIgnoreCase(episodeId)) {
                        foundEpisode = true;
                        relatedVids.add(episode);
                    }
                }
            } else {
                for (int j = 0; j < season.get(i).getEpisodes().size(); j++) {
                    String episode = season.get(i).getEpisodes().get(j).getId();
                    if (foundEpisode || episode.equalsIgnoreCase(episodeId)) {
                        foundEpisode = true;
                        relatedVids.add(episode);
                    }
                }
            }
        }
        return relatedVids;
    }

    public static DownloadClosedCaptionRealm convertClosedCaptionToDownloadClosedCaption(ClosedCaptions cc, String gistId) {
        DownloadClosedCaptionRealm downloadClosedCaptionRealm = new DownloadClosedCaptionRealm();
        downloadClosedCaptionRealm.setId(cc.getId());
        downloadClosedCaptionRealm.setAddedDate(cc.getAddedDate());
        downloadClosedCaptionRealm.setFormat(cc.getFormat());
        downloadClosedCaptionRealm.setLanguage(cc.getLanguage());
        downloadClosedCaptionRealm.setPermalink(cc.getPermalink());
        downloadClosedCaptionRealm.setPublishDate(cc.getPublishDate());
        downloadClosedCaptionRealm.setRegisteredDate(cc.getRegisteredDate());
        downloadClosedCaptionRealm.setSiteOwner(cc.getSiteOwner());
        downloadClosedCaptionRealm.setSize(cc.getSize());
        downloadClosedCaptionRealm.setUpdateDate(cc.getUpdateDate());
        downloadClosedCaptionRealm.setUrl(cc.getUrl());
        downloadClosedCaptionRealm.setGistId(gistId);
        return downloadClosedCaptionRealm;
    }

    public static ClosedCaptions convertDownloadClosedCaptionToClosedCaptions(DownloadClosedCaptionRealm dc) {
        ClosedCaptions closedCaptions = new ClosedCaptions();
        closedCaptions.setAddedDate(dc.getAddedDate());
        closedCaptions.setFormat(dc.getFormat());
        closedCaptions.setId(dc.getId());
        closedCaptions.setLanguage(dc.getLanguage());
        closedCaptions.setPermalink(dc.getPermalink());
        closedCaptions.setPublishDate(dc.getPublishDate());
        closedCaptions.setRegisteredDate(dc.getRegisteredDate());
        closedCaptions.setSiteOwner(dc.getSiteOwner());
        closedCaptions.setSize(dc.getSize());
        closedCaptions.setUpdateDate(dc.getUpdateDate());
        closedCaptions.setUrl(dc.getUrl());
        return closedCaptions;
    }

    public static float getFloatValue(Context context, int refId) {
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(refId, typedValue, true);
        return typedValue.getFloat();
    }

    public static String convertStringIntoCamelCase(String text) {
        try {
            String[] words = text.split(" ");
            StringBuilder sb = new StringBuilder();
            if (words[0].length() > 0) {
                sb.append(Character.toUpperCase(words[0].charAt(0)) + words[0].subSequence(1, words[0].length()).toString().toLowerCase());
                for (int i = 1; i < words.length; i++) {
                    sb.append(" ");
                    sb.append(Character.toUpperCase(words[i].charAt(0)) + words[i].subSequence(1, words[i].length()).toString().toLowerCase());
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static void setCountryCode(String countryCode) {
        Utils.countryCode = countryCode;
    }

    public static void setLatitude(double latitude) {
        Utils.latitude = latitude;
    }

    public static double getLatitude() {
        return latitude;
    }


    public static String getCountryCode() {
        return (!TextUtils.isEmpty(countryCode)) ? countryCode : "default";
    }

    public static void setAddId(String addId) {
        Utils.addId = addId;
    }

    public static String getAddId() {
        return addId;
    }


    public static GradientDrawable setBorder(Context context, AppCMSPresenter appCMSPresenter) {
        GradientDrawable ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.RECTANGLE);
        float[] outerRadii;

        if (BaseView.isTablet(context)) {
            outerRadii = new float[]{0, 15, 15, 15, 0, 15, 15, 15};
        } else {
            outerRadii = new float[]{0, 50, 50, 50, 0, 50, 50, 50};
        }

        ageBorder.setCornerRadii(outerRadii);
        ageBorder.setColor(appCMSPresenter.getBrandPrimaryCtaColor());

        return ageBorder;
    }

    public static String formatTimeAndDateVT2(long diff) {

        StringBuilder stringBuilder = new StringBuilder();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        if (elapsedDays > 0) {
            stringBuilder.append(elapsedDays);
            stringBuilder.append("d");
            stringBuilder.append(" ");
        }

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        if (elapsedHours > 0) {
            stringBuilder.append(elapsedHours);
            stringBuilder.append("h");
            stringBuilder.append(" ");
        }

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        if (elapsedMinutes > 0) {
            stringBuilder.append(elapsedMinutes);
            stringBuilder.append("m");
            stringBuilder.append(" ");
        }

        long elapsedSeconds = diff / secondsInMilli;

        stringBuilder.append(elapsedSeconds);
        stringBuilder.append("s");

        return stringBuilder.toString();

       /*if (diffInDays > 0)
           return diffInDays + ” Days Remaining”;
       else if (diffHours > 0)
           return diffHours + ” Hours Remaining”;
       else if (diffMinutes > 0)
           return diffMinutes + ” Minutes Remaining”;
       else
           return diffSeconds + ” Seconds Remaining”;*/
    }

    public enum FontWeight {
        REGULAR, BOLD, SEMI_BOLD, EXTRA_BOLD, ITALIC, BLACK_ITALIC, LIGHT
    }


    public static void setTypeFace(Context context,
                                   AppCMSPresenter appCMSPresenter,
                                   TextView textView, FontWeight fontWeight) {

        Typeface face;
        switch (fontWeight) {
            case LIGHT:
                face = appCMSPresenter.getLightTypeFace();
                if (face == null) {
                    face = ResourcesCompat.getFont(context, R.font.font_light);
                    appCMSPresenter.setLightTypeFace(face);
                }
                break;
            case BOLD:
                face = appCMSPresenter.getBoldTypeFace();
                if (face == null) {
                    face = ResourcesCompat.getFont(context, R.font.font_bold);
                    appCMSPresenter.setBoldTypeFace(face);
                }
                break;

            case SEMI_BOLD:
                face = appCMSPresenter.getSemiBoldTypeFace();
                if (face == null) {
                    face = ResourcesCompat.getFont(context, R.font.font_semi_bold);
                    appCMSPresenter.setSemiBoldTypeFace(face);
                }
                break;

            case EXTRA_BOLD:
                face = appCMSPresenter.getExtraBoldTypeFace();
                if (face == null) {
                    face = ResourcesCompat.getFont(context, R.font.font_extra_bold);
                    appCMSPresenter.setExtraBoldTypeFace(face);
                }
                break;

            case ITALIC:
                face = appCMSPresenter.getItalicTypeFace();
                if (face == null) {
                    face = ResourcesCompat.getFont(context, R.font.font_italic);
                    appCMSPresenter.setItalicTypeFace(face);
                }
                break;

            case BLACK_ITALIC:
                face = Typeface.defaultFromStyle(Typeface.ITALIC);
                break;
            default:
                face = appCMSPresenter.getRegularFontFace();
                if (face == null) {
                    face = ResourcesCompat.getFont(context, R.font.font_regular);
                    appCMSPresenter.setRegularFontFace(face);
                }
                break;
        }

        textView.setTypeface(face);
    }


    public static boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    public static String getAdvertisingID(Context context){
        String advertisingID = null;
        //boolean limitAdTracking = false;
        try {
            ContentResolver cr = context.getContentResolver();
            // Check whether the user has disabled tracking
            int limitAdTracking = Settings.Secure.getInt(cr, "limit_ad_tracking", 2);
            if (limitAdTracking == 0) {
                // Interest-based tracking is allowed, retrieve the Advertising ID
                advertisingID = Settings.Secure.getString(cr, "advertising_id");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return advertisingID;
    }

    public static String getIPAddress() {
        return ipAddress;
    }

    public static void setIPAddress(String ipAddress) {
        Utils.ipAddress = ipAddress;
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        // It's a dark color
        return !(darkness < 0.5); // It's a light color
    }


    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static List<String> getRelatedVideoId(Module moduleApi) {
        List<String> allEpisodes = new ArrayList<>();
        try {
            if (moduleApi != null && moduleApi.getContentData() != null
                    && moduleApi.getContentData().size() > 0 &&
                    moduleApi.getContentData().get(0).getSeason() != null
                    && moduleApi.getContentData().get(0).getSeason().size() > 0) {
                for (int index = 0; index < moduleApi.getContentData().get(0).getSeason().size(); index++) {
                    if (moduleApi.getContentData().get(0).getSeason().get(index).getEpisodes() != null) {
                        for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(index).getEpisodes().size(); j++) {
                            allEpisodes.add(moduleApi.getContentData().get(0).getSeason().get(index).getEpisodes().get(j).getGist().getId());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allEpisodes;
    }

    public static int findCurrentIndexOfEpisode(List<String> allEpisodes, String filmId) {
        try {
            if (allEpisodes != null && allEpisodes.size() > 0) {
                for (int index = 0; index < allEpisodes.size(); index++) {
                    if (filmId.equalsIgnoreCase(allEpisodes.get(index)))
                        return index;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static String findEpisodeFromSegment(Module moduleApi, String filmId) {
        if (moduleApi != null && moduleApi.getContentData() != null &&
                moduleApi.getContentData().size() > 0 &&
                moduleApi.getContentData().get(0).getSeason() != null &&
                moduleApi.getContentData().get(0).getSeason().size() > 0) {
            try {
                for (int i = 0; i < moduleApi.getContentData().get(0).getSeason().size(); i++) {
                    if (moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes() != null) {
                        for (int j = 0; j < moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().size(); j++) {
                            ContentDatum episode = moduleApi.getContentData().get(0).getSeason().get(i).getEpisodes().get(j);
                            if (episode.getRelatedVideos() != null && episode.getRelatedVideos().size() > 0) {
                                for (int k = 0; k < episode.getRelatedVideos().size(); k++) {
                                    if (episode.getRelatedVideos().get(k).getGist() != null && episode.getRelatedVideos().get(k).getGist().getId() != null && episode.getRelatedVideos().get(k).getGist().getId().equalsIgnoreCase(filmId)) {
                                        return episode.getGist().getId();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    public static boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static Bundle getIapEventBundle(String action,
                                           String category,
                                           String vlUser,
                                           String amazonUser,
                                           String planIdentifier,
                                           String receiptId, String errorMsg) {
        return new IapEvent(action, category, vlUser, amazonUser, planIdentifier, receiptId, errorMsg).getIapEventBundle();
    }
}

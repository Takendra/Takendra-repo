package com.viewlift.utils;

import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Base64;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.common.util.Strings;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.history.AppCMSRecommendationGenreResult;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.authentication.SignInResponse;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.CountryCodes;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.ModuleView;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.UI_MODE_SERVICE;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_ARTICLE_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_AUDIO_TRAY_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_RECOMENDATION_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_03;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_04;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_USER_PERSONALIZATION_01;

/**
 * App specific common utils
 */

public class CommonUtils {

    private static final ZoneId UTC_ZONE_ID = ZoneId.of("UTC+00:00");
    public static final String WHATSAPPP_PACKAGE_NAME = "com.whatsapp";

    //https://developers.google.com/pay/india/api/android/in-app-payments
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";

    public static HashMap<String, ModuleView> recommendationTraysModule= new HashMap<>();
    public static HashMap<String, Module> recommendationTraysData= new HashMap<>();
    public static HashMap<String, ModuleView> personalizationTraysModule= new HashMap<>();
    public static HashMap<String, AppCMSRecommendationGenreResult> personalizationTraysData= new HashMap<>();

    private static String ZENDESK_SUBDOMAIN_URL = "";
    private static String ZENDESK_APPLICATION_ID = "";
    private static String ZENDESK_OAUTH_CLIENT_ID = "";
    private static String countryCode;

    public static int seeAllColumnCount = 2;
    public static boolean isPlayFromBeginning;

    public static float THUMB_IMAGE_16x9_WIDTH_MOBILE = 227.62f;
    public static float THUMB_IMAGE_3x4_WIDTH_MOBILE = 111f;
    public static float THUMB_IMAGE_1x1_WIDTH_MOBILE = 130f;
    public static float THUMB_IMAGE_9x16_WIDTH_MOBILE = 175f;
    public static float THUMB_IMAGE_32x9_WIDTH_MOBILE = 285f;

    public static float THUMB_IMAGE_16x9_WIDTH_TABLET = 319f;
    public static float THUMB_IMAGE_3x4_WIDTH_TABLET = 140f;
    public static float THUMB_IMAGE_1x1_WIDTH_TABLET = 150f;
    public static float THUMB_IMAGE_9x16_WIDTH_TABLET = 175f;
    public static float THUMB_IMAGE_32x9_WIDTH_TABLET = 375;

    public static boolean isServiceInStartedState;

    /**
     * vlaue 5 is representationn of 50% of device volume in case of unmute
     */
    public static int UNMUTE_DEFAULT_VOLUME = 5;
    public static int DEFAULT_VOLUME_FROM_MINI_PLAYER = 0;

    /**
     * Description : Returns the date format according to time zone
     *
     * @param timeMilliSeconds milliseconds to convert
     * @param dateFormat       date format to apply
     * @return formatted date string of given time
     */

    public static String getDateFormatByTimeZone(long timeMilliSeconds, String dateFormat) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            dateFormat=dateFormat.replaceAll("Y","y");
        }
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilliSeconds);
        formatter.setTimeZone(TimeZone.getDefault());

        return formatter.format(calendar.getTime());
    }

    /**
     * Description : Convert and adds the given hour in given mills
     *
     * @param timeMilliSeconds milliseconds
     * @param hour             no of hours to add
     * @return milliseconds after adding hour time
     */
    public static long addHourToMs(long timeMilliSeconds, float hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilliSeconds);
        calendar.add(Calendar.HOUR, (int) hour);
        return calendar.getTimeInMillis();
    }

    /**
     * Description : Calculates the difference between current time and given time in mills
     *
     * @param timeMilliSecondsEvent time in milliseconds
     * @param dateFormat            date format to apply
     * @return time in milliseconds
     */
    public static long getTimeIntervalForEventSchedule(long timeMilliSecondsEvent, String dateFormat) {
        long timeDifference;
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilliSecondsEvent);
        formatter.setTimeZone(TimeZone.getDefault());

        String eventTime = formatter.format(calendar.getTime());

        SimpleDateFormat formatterCurrentTime = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis()
        );
        formatterCurrentTime.setTimeZone(TimeZone.getDefault());

        String currentTime = formatterCurrentTime.format(calendarCurrent.getTime());
        long eventTimeInMs = getMillisecondFromDateString(dateFormat, eventTime);
        long currentTimeInMs = getMillisecondFromDateString(dateFormat, currentTime);
        timeDifference = eventTimeInMs - currentTimeInMs;
        return timeDifference;
    }

    /**
     * Description : Calculates the millis in given date string
     *
     * @param dateFormat date format of input date string
     * @param date       date
     * @return time in milliseconds
     */
    public static long getMillisecondFromDateString(String dateFormat, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDatebyDefaultZone(String date, String returnDateFormat) {
        if (TextUtils.isEmpty(date))
            return "";
        try {
            ZonedDateTime parsed = ZonedDateTime.parse(date);
            ZonedDateTime z2 = parsed.withZoneSameInstant(ZoneId.systemDefault());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern(returnDateFormat, Locale.ENGLISH);
            return fmt.format(z2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Description : Calculate the difference between current time and event time in millis
     *
     * @param timeMilliSecondsEvent time in milliseconds of event
     * @param dateFormat            date format
     * @return Calculating dateFormat difference from current time
     */
    public static long getTimeIntervalForEvent(long timeMilliSecondsEvent, String dateFormat) {
        long timeDifferenceEvent;
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilliSecondsEvent);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        String eventTime = formatter.format(calendar.getTime());

        SimpleDateFormat formatterCurrentTime = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeInMillis(System.currentTimeMillis());
        formatterCurrentTime.setTimeZone(TimeZone.getTimeZone("UTC"));

        ZonedDateTime nowTime = ZonedDateTime.now(UTC_ZONE_ID);
        nowTime.toEpochSecond();

        timeDifferenceEvent = calendar.getTimeInMillis() - calendarCurrent.getTimeInMillis();
        return timeDifferenceEvent;
    }

    /**
     * Description : Get the device model along with manufacturer
     *
     * @return device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * Description : Capitalize the first character of given string
     *
     * @param s String to capitalize
     * @return Capitalized string object
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Description : Capitalize the first character of each word
     */
    public static String capitalizeAll(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Arrays.stream(str.split("\\s+"))
                    .map(t -> t.substring(0, 1).toUpperCase() + t.substring(1).toLowerCase())
                    .collect(Collectors.joining(" "));
        }
        return str;
    }


    /**
     * Description : This prepends a '#' symbol to beginning of color string if it missing from the string
     *
     * @param context The current Context
     * @param color   The color to prepend a '#' symbol
     * @return Returns the updated color string with a prepended '#'
     */
    public static String getColor(Context context, String color) {
        if (color == null) {
            // ConstraintViewCreator and ViewCreator class were returning this default color value.
            //other classes are not sending null color value
            return "#d8d8d8";
        }
        if (color.indexOf(context.getString(R.string.color_hash_prefix)) != 0) {
            color = context.getString(R.string.color_hash_prefix) + color;
        }
        return color;
    }

    public static boolean isPPVContent(Context mContext, ContentDatum onUpdatedContentDatum) {
        if ((onUpdatedContentDatum != null &&
                onUpdatedContentDatum.getPricing() != null &&
                onUpdatedContentDatum.getPricing().getType() != null) &&
                ((onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_TVOD))
                        || onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_PPV))) ||
                        (onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                || onUpdatedContentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))))) {
            return true;
        }
        return false;
    }

    public static String getFocusBorderColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBorder().getColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBorder().getColor();
        }
        return color;
    }

    public static String getFocusColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        //Log.d("Utils.java" , "getFocusColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor();
        }
        return color;
    }

    public static String getTextColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, android.R.color.white)));
        //Log.d("Utils.java" , "getTextColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor();
        }
        return color;
    }

    public static StateListDrawable getTrayBorder(Context context, String selectedColor, Component component) {
        boolean isEditText = false;
        boolean isWeatherWidget = false;
        if (null != component) {
            isEditText = context.getString(R.string.app_cms_page_textfield_key).equalsIgnoreCase(component.getType());
            isWeatherWidget = context.getString(R.string.weatherWidgetView).equalsIgnoreCase(component.getKey());
        }

        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, false));
        res.addState(new int[]{android.R.attr.state_pressed}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, false));
        res.addState(new int[]{android.R.attr.state_selected}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, false));
        if (isEditText || isWeatherWidget)
            res.addState(new int[]{}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, true));
        else
            res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(
                    context,
                    android.R.color.transparent
            )));
        return res;
    }

    private static GradientDrawable getBorder(Context context, String borderColor, boolean isEditText, boolean isWeatherWidget, Component component, boolean isNormalState) {
        GradientDrawable ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.RECTANGLE);

        if (isEditText)
            ageBorder.setCornerRadius(component.getCornerRadius());

        if (!isNormalState)
            ageBorder.setStroke(6, Color.parseColor(borderColor));

        if (isEditText && isNormalState) {
            ageBorder.setStroke(1, Color.parseColor(borderColor));
        }

        if (component != null && component.getStroke() != 0) {
            ageBorder.setStroke(component.getStroke(), Color.parseColor(borderColor));
        } else {
            //ageBorder.setStroke(6, Color.parseColor(borderColor));
        }

        ageBorder.setColor(ContextCompat.getColor(
                context,
                isEditText ? android.R.color.white : android.R.color.transparent
        ));

        if (isWeatherWidget) {
            ageBorder.setColor(Color.parseColor(component.getBackgroundColor()));
        }

        return ageBorder;
    }

    public static void setImageColorFilter(@NonNull ImageView button, Drawable drawable, AppCMSPresenter appCMSPresenter) {
        String focusStateTextColor = null;
        String unFocusStateTextColor = null;
        if (null != appCMSPresenter &&
                null != appCMSPresenter.getAppCMSMain() &&
                null != appCMSPresenter.getAppCMSMain().getBrand() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            focusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor();
            unFocusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getTextColor();
        }
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };
        int[] colors = new int[]{
                Color.parseColor(focusStateTextColor),
                Color.parseColor(focusStateTextColor),
                Color.parseColor(focusStateTextColor),
                Color.parseColor(unFocusStateTextColor)
        };
        ColorStateList myList = new ColorStateList(states, colors);

        if (button != null) {
            drawable = DrawableCompat.wrap(button.getDrawable());
            DrawableCompat.setTintList(drawable, myList);
            button.setImageDrawable(drawable);
        } else {
            DrawableCompat.setTintList(drawable, myList);
        }
    }

    /**
     * this method is use for setting the textCoo
     *
     * @param context
     * @param appCMSPresenter
     * @return
     */
    public static ColorStateList getTextColorDrawable(Context context, AppCMSPresenter appCMSPresenter) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };
        int[] colors = new int[]{
                Color.parseColor(getFocusBorderColor(context, appCMSPresenter)),
                Color.parseColor(getFocusColor(context, appCMSPresenter)),
                Color.parseColor(getFocusColor(context, appCMSPresenter)),
                Color.parseColor(getTextColor(context, appCMSPresenter))
        };
        ColorStateList myList = new ColorStateList(states, colors);
        return myList;
    }


    public static void launchSharingIntentApp(Context context, Intent sendIntent, String title) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resInfo;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            resInfo = pm.queryIntentActivities(sendIntent, PackageManager.MATCH_ALL);
        } else {
            resInfo = pm.queryIntentActivities(sendIntent, 0);
        }
        List<LabeledIntent> initialAppList = new ArrayList<>();
        List<ComponentName> removedAppList = new ArrayList<>();
        LabeledIntent whatsAppIntent = null;
        for (int i = 0; i < resInfo.size(); i++) {
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if (!packageName.contains("com.google.android.gm")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(sendIntent.getAction());
                intent.setType(sendIntent.getType());
                intent.putExtra(Intent.EXTRA_TEXT, sendIntent.getStringExtra(Intent.EXTRA_TEXT));
                if (packageName.contains(WHATSAPPP_PACKAGE_NAME)) {
                    whatsAppIntent = new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon);
                    initialAppList.add(0, whatsAppIntent);
                    removedAppList.add(new ComponentName(packageName, ri.activityInfo.name));
                } else {
                    initialAppList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
                }
            } else {
                removedAppList.add(new ComponentName(packageName, ri.activityInfo.name));
            }
        }

        Intent newIntent = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            newIntent = Intent.createChooser(sendIntent, title);
            if (whatsAppIntent != null)
                newIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{whatsAppIntent});
            newIntent.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, removedAppList.toArray(new ComponentName[removedAppList.size()]));
        } else {
            if (!initialAppList.isEmpty()) {
                newIntent = Intent.createChooser(initialAppList.remove(initialAppList.size() - 1), title);
                newIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, initialAppList.toArray(new LabeledIntent[initialAppList.size()]));
            }
        }

        if (newIntent != null) {
            try {
                context.startActivity(newIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static boolean isUnderAgeRestrictions(AppCMSPresenter appCMSPresenter, String contentRating) {
        if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().isParentalControlEnable() && appCMSPresenter.isUserLoggedIn()
                && appCMSPresenter.getParentalRatingMap() != null && appCMSPresenter.getParentalRatingMap().size() > 0
                && appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getAppPreference().isParentalControlsEnable()) {
            String preSelectedAge = appCMSPresenter.getAppPreference().getParentalRating();
            if (TextUtils.isEmpty(preSelectedAge) || TextUtils.isEmpty(contentRating)) {
                return false;
            }

            List<String> categoryList = appCMSPresenter.getParentalRatingMap().get(preSelectedAge);

            if (categoryList != null && categoryList.size() > 0) {
                for (String category : categoryList) {
                    if (category.toLowerCase().equalsIgnoreCase(contentRating))
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean checkURL(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        Pattern URL_PATTERN = Patterns.WEB_URL;
        boolean isURL = URL_PATTERN.matcher(input).matches();
        if (!isURL) {
            String urlString = input + "";
            if (URLUtil.isNetworkUrl(urlString)) {
                try {
                    new URL(urlString);
                    isURL = true;
                } catch (Exception e) {
                }
            }
        }
        return isURL;
    }

    public static boolean isFreeContent(Context mContext, ContentDatum contentDatum) {
        return contentDatum != null && contentDatum.getPricing() != null && contentDatum.getPricing().getType() != null && contentDatum.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_FREE));
    }

    private static boolean isRecommendation = false;

    public static boolean isRecommendation(AppCMSPresenter appCMSPresenter) {
        if (isRecommendation) {
            return isRecommendation;
        } else if (appCMSPresenter.getAppCMSMain() != null &&
                appCMSPresenter.getAppCMSMain().getRecommendation() != null &&
                appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation()) {
            isRecommendation = true;
        }
        return isRecommendation;
    }

    private static boolean isRecommendationSubscribed = false;

    public static boolean isRecommendationSubscribed(AppCMSPresenter appCMSPresenter) {
        if (isRecommendationSubscribed) {
            return isRecommendationSubscribed;
        } else if (appCMSPresenter.getAppCMSMain() != null &&
                appCMSPresenter.getAppCMSMain().getRecommendation() != null &&
                appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation() &&
                appCMSPresenter.getAppCMSMain().getRecommendation().isSubscribed()) {
            isRecommendationSubscribed = true;
        }
        return isRecommendationSubscribed;
    }

    public static boolean isBundleDataWithout3x4Image(Module moduleAPI) {
        if (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0
                && moduleAPI.getContentData().get(0).getGist() != null
                && moduleAPI.getContentData().get(0).getGist().getBundleList() != null
                && moduleAPI.getContentData().get(0).getGist().getBundleList().size() > 0
                && moduleAPI.getContentData().get(0).getGist().getBundleList().get(0).getGist() != null
                && moduleAPI.getContentData().get(0).getGist().getBundleList().get(0).getGist().getImageGist() != null
                && (moduleAPI.getContentData().get(0).getGist().getBundleList().get(0).getGist().getImageGist().get_3x4() == null
                || TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getBundleList().get(0).getGist().getImageGist().get_3x4()))) {
            return true;
        }
        return false;
    }

    private static Set<String> userPurchasedBundle = new HashSet<String>();

    public static void setUserPurchasedBundle(String videoId) {
        CommonUtils.userPurchasedBundle.add(videoId);
    }

    public static Set<String> getUserPurchasedBundle() {
        return userPurchasedBundle;
    }

    public static boolean isUserPurchaseItem(String videoId) {
        for (String id : userPurchasedBundle) {
            if (id.equalsIgnoreCase(videoId)) {
                return true;
            }
        }
        return false;
    }

    public static void clearUserPurchaseBundle() {
        userPurchasedBundle.clear();
    }

    public static void showBudlePurchaseDialog(AppCMSPresenter appCMSPresenter, Module moduleAPI) {
        if (appCMSPresenter.isPageABundlePage(appCMSPresenter.getCurrentAppCMSBinder().getPageId(), appCMSPresenter.getCurrentAppCMSBinder().getPageName())) {
            if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                ContentDatum contentDatum = moduleAPI.getContentData().get(0);
                contentDatum.setModuleApi(moduleAPI);
                appCMSPresenter.openEntitlementScreen(contentDatum, false);
            } else {

                Context context = appCMSPresenter.getCurrentActivity();
                appCMSPresenter.getCurrentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Handler handler = new Handler();
                        handler.getLooper();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                moduleAPI.getContentData().get(0).getGist().setRentedDialogShow(true);

                                String message = appCMSPresenter.getLanguageResourcesFile().getStringValue(context.getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName());
                                if (appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                                    message = appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName());
                                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(message);
                                if (appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                                    ssBuilder = new SpannableStringBuilder(appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));
                                /// Initialize a new StyleSpan to display bold italic text
                                StyleSpan boldItalicSpan = new StyleSpan(Typeface.BOLD_ITALIC);
                                // Initialize a new StyleSpan to display italic text
                                StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);

                                // Apply the bold italic text style span
                                ssBuilder.setSpan(
                                        italicSpan,
                                        message.indexOf(appCMSPresenter.getAppCMSMain().getDomainName()),
                                        message.indexOf(appCMSPresenter.getAppCMSMain().getDomainName()) + String.valueOf(appCMSPresenter.getAppCMSMain().getDomainName()).length(),
                                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.rental_title)), ssBuilder.toString());
                            }
                        }, 500);
                    }
                });
            }
        }
    }

    public static Map<String, String> paymentProviders;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("iOS", "iTunes");
        aMap.put("iTunes", "iTunes");
        aMap.put("Android", "Google Play");
        aMap.put("Google Play", "Google Play");
        aMap.put("PREPAID", "PREPAID");
        aMap.put("ccavenue", "CCAvenue");
        aMap.put("CCAvenue", "CCAvenue");
        aMap.put("sslcommerz", "SSLCOMMERZ");
        aMap.put("JUSPAY", "JusPay");
        aMap.put("PAYGATE", "PayGate");
        aMap.put("STRIPE", "Stripe");
        aMap.put("ROKU", "Roku");
        paymentProviders = Collections.unmodifiableMap(aMap);
    }

    public static boolean isFromBackground = false;

    public static void applyBorderToComponent(View view, int width, int Color) {
        GradientDrawable rectangleBorder = new GradientDrawable();
        rectangleBorder.setShape(GradientDrawable.RECTANGLE);
        rectangleBorder.setStroke(width, Color);
        view.setBackground(rectangleBorder);
    }

    public static boolean isSiteOTPEnabled(AppCMSPresenter appCMSPresenter) {
        return appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled();
    }

    public static boolean isEmailLoginNotPhone(AppCMSPresenter appCMSPresenter, String phoneNumber) {
        return isSiteOTPEnabled(appCMSPresenter) && !isValidPhoneNumber(phoneNumber);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return !Strings.isEmptyOrWhitespace(phoneNumber) && !Pattern.matches("[a-zA-Z]+", phoneNumber);
    }

    public static boolean isMobileUpdateRequired(AppCMSPresenter appCMSPresenter, String phoneNumber) {
        return appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isManageProfile() && !isValidPhoneNumber(phoneNumber);
    }

    public static String getCountryCode(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String simCountry = telephonyManager.getSimCountryIso();
                if (!TextUtils.isEmpty(simCountry)) { // SIM country code is available
                    return CountryCodes.getCode(simCountry);
                } else if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                    String networkCountry = telephonyManager.getNetworkCountryIso();
                    if (!TextUtils.isEmpty(networkCountry)) { // network country code is available
                        return CountryCodes.getCode(networkCountry);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String defaultCountry = Utils.getCountryCode();
        if (!TextUtils.isEmpty(defaultCountry))
            return CountryCodes.getCode(defaultCountry);
        else
            return null;
    }

    public static String getCountryCodeFromAuthToken(String token) {
        String countryCode = "";
        if (!TextUtils.isEmpty(token)) {
            try {
                if (token.contains(".")) {
                    String[] split = token.split("\\.");
                    JSONObject header = new JSONObject(getJson(split[0]));
                    JSONObject body = new JSONObject(getJson(split[1]));

                    countryCode = body.getString("countryCode");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = Utils.getCountryCode();
        }
        Log.d("countryCode ", "" + countryCode);
        return countryCode;
    }

    public static String getUserIdFromAuthToken(String token) {
        String userId = "";
        if (!TextUtils.isEmpty(token)) {
            try {
                if (token.contains(".")) {
                    String[] split = token.split("\\.");
                    JSONObject header = new JSONObject(getJson(split[0]));
                    JSONObject body = new JSONObject(getJson(split[1]));

                    userId = body.getString("userId");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.d("User Id ", "" + userId);
        return userId;
    }

    public static boolean isTokenExpired(String token) {
        Log.d("token ", "" + token);
        if (TextUtils.isEmpty(token))
            return true;
        try {
            if (token.contains(".")) {
                String[] split = token.split("\\.");
                JSONObject header = new JSONObject(getJson(split[0]));
                JSONObject body = new JSONObject(getJson(split[1]));

                if (body.getLong("exp") < System.currentTimeMillis() / 1000) {
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static String getZendeskSubdomainUrl() {
        return ZENDESK_SUBDOMAIN_URL;
    }

    public static void setZendeskSubdomainUrl(String zendeskSubdomainUrl) {
        ZENDESK_SUBDOMAIN_URL = zendeskSubdomainUrl;
    }

    public static String getZendeskApplicationId() {
        return ZENDESK_APPLICATION_ID;
    }

    public static void setZendeskApplicationId(String zendeskApplicationId) {
        ZENDESK_APPLICATION_ID = zendeskApplicationId;
    }

    public static String getZendeskOauthClientId() {
        return ZENDESK_OAUTH_CLIENT_ID;
    }

    public static void setZendeskOauthClientId(String zendeskOauthClientId) {
        ZENDESK_OAUTH_CLIENT_ID = zendeskOauthClientId;
    }

    public static String getDeviceId(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String sessionSignature;

    public static String getHMACSignature(Context context) {
        try {
            String secretKey = "048ac38afa142337272beffe80e1e534";
            String timestamp = String.valueOf(new Date().getTime());
            String nonce = UUID.randomUUID().toString().replace("-", "");
            String signature = nonce + timestamp + getDeviceId(context);

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeToString(sha256_HMAC.doFinal(signature.getBytes()), 0).
                    replace("+", "-").replace("/", "_").trim();
            String fedData = context.getString(R.string.verimatrix_shortcode_fed_data, CommonUtils.getDeviceId(context), nonce, timestamp, hash);
            sessionSignature = fedData;
            return fedData;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String getShortCodeHMACSignature() {
        return sessionSignature;
    }


    public static boolean isValidSubscription(String planEndDate) {
        long endDate = getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", planEndDate);
        return endDate - System.currentTimeMillis() > 0;
    }

    public static void setCountryCode(String country) {
        countryCode = country;
    }

    public static String getCountryCode() {
        return countryCode;
    }

    public static String getDateFormat(String strDate, String initDateFormat, String endDateFormat) {
        String dateResult = "";

        Date initDate = null;
        try {
            initDate = new SimpleDateFormat(initDateFormat).parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } /*finally {
            try {
                initDate = new SimpleDateFormat("dd MMM yyyy").parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        dateResult = formatter.format(initDate);
        return dateResult;
    }



    public static boolean isTrayModule(AppCMSUIKeyType trayType) {
        if (trayType  == UI_BLOCK_TRAY_01 ||
                trayType  == UI_BLOCK_TRAY_02||
                trayType  == UI_BLOCK_TRAY_03||
                trayType  == UI_BLOCK_TRAY_04||
                trayType  == UI_BLOCK_AUDIO_TRAY_01||
                trayType  == UI_BLOCK_ARTICLE_01){
            return true;
        }
        return false;
    }
    public static boolean isFrameTray(String blockName) {
        if (blockName.equalsIgnoreCase("tray03")||
                blockName.equalsIgnoreCase("articleTray01") ){
            return true;
        }
        return false;
    }
    public static boolean isTrayModule(String blockName) {
        if(blockName==null)
            return false;
        if (blockName.equalsIgnoreCase("tray01") ||
                blockName.equalsIgnoreCase("tray02")||
                blockName.equalsIgnoreCase("tray03")||
                blockName.equalsIgnoreCase("tray04")||
                blockName.equalsIgnoreCase("audioTray01")||
                blockName.equalsIgnoreCase("articleTray01")){
            return true;
        }
        return false;
    }

    public static boolean isRecommendationTrayModule(String blockName) {
        if (blockName.equalsIgnoreCase("recommendation01")
                || blockName.equalsIgnoreCase("userPersonalization01") ){
            return true;
        }
        return false;
    }

    public static boolean isRecommendationTrayModule(AppCMSUIKeyType trayType) {
        if (trayType  == UI_BLOCK_RECOMENDATION_01
            || trayType  == UI_BLOCK_USER_PERSONALIZATION_01){
            return true;
        }
        return false;
    }



    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String position=null;
    public static  void setpostion(String positionitem) {
        position = positionitem;
    }
    public static  String getPosition()
    {
        return    position;
    }
    public static boolean isFullDiscription=false;
    public static  void setFullDiscription(boolean discription) {
        isFullDiscription = discription;
    }
    public static  boolean getFullDiscription()
    {
        return    isFullDiscription;
    }
    public static void setMoreLinkInDescription(TextView textView, String description, int spanCount) {
        int descLength = description.length();
        if (descLength > spanCount) {
            String spanText = (description.substring(0, spanCount)) + "..." ;
            textView.setText(spanText);
        }
    }
    static boolean isShowDetailsPage=false;
    public static boolean isShowDetailsPage() {
        return isShowDetailsPage;
    }
    public static void setShowDetailsPage(boolean showDetailsPage) {
        isShowDetailsPage = showDetailsPage;
    }


    public static float getPlayerDefaultFontSize(AppCMSPresenter appCMSPresenter) {
        return appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV ?
                30f : 20f;
    }
    public static int playerPosition=0;
    public static  void setTabPostion(int tabosition)
    {
        playerPosition = tabosition;
    }
    public static  int getTabPosition()
    {
        return  playerPosition;
    }



    public static String getVerimatrixPartnerId(AppCMSAndroidUI appcmsAndroid, String provider) {
        String platformId = null;
        if (appcmsAndroid.getTveSettings() != null
                && appcmsAndroid.getTveSettings().getTveProvider().toLowerCase().equalsIgnoreCase(provider)
                && appcmsAndroid.getTveSettings().getPlatformId() != null)
            platformId = appcmsAndroid.getTveSettings().getPlatformId();
        return platformId;
    }

    public static String getVerimatrixResourceIds(AppCMSAndroidUI appcmsAndroid, String provider) {
        String resourceIds = null;
        if (appcmsAndroid.getTveSettings() != null
                && appcmsAndroid.getTveSettings().getTveProvider().toLowerCase().equalsIgnoreCase(provider)
                && appcmsAndroid.getTveSettings().getResourceId() != null)
            resourceIds = appcmsAndroid.getTveSettings().getResourceId();
        return resourceIds;
    }

    public static String getVerimatrixResourceAccessUrl(AppCMSPresenter appCMSPresenter,Context context) {
        String url = null;
        String resource = getVerimatrixResourceIds(appCMSPresenter.getAppCMSAndroid(), context.getString(R.string.tvprovider_verimatrix));
        if (resource != null) {
            String[] resourceIds = resource.split(",");
            if (resourceIds.length > 0) {
                StringBuilder resId = new StringBuilder();
                for (int i = 0 ; i < resourceIds.length ; i++) {
                    resId.append("resource_id=");
                    resId.append(resourceIds[i]);
                    if(i != resourceIds.length-1) resId.append("&");
                }
                url = context.getString(R.string.verimatrix_resource_access_list, context.getString(R.string.verimatrix__base_url),
                        CommonUtils.getVerimatrixPartnerId(appCMSPresenter.getAppCMSAndroid(), context.getString(R.string.tvprovider_verimatrix)), resId.toString());
            }

        }
        return url;
    }

    public static String getPlanPriceDetail(AppCMSPresenter appCMSPresenter, MetadataMap metadataMap) {
        StringBuilder priceInfo = new StringBuilder();
        try {
            double priceValueSecondry = 0;
            double priceValue = round((appCMSPresenter.getAppPreference().getActiveSubscriptionPrice() != null ? Double.parseDouble(appCMSPresenter.getAppPreference().getActiveSubscriptionPrice()) : appCMSPresenter.getUserSubscriptionPlanResult().getPlanDetails().get(0).getRecurringPaymentAmount()), 2);
            String currencyCode = appCMSPresenter.getAppPreference().getActiveSubscriptionPriceCurrencyCode() != null ? appCMSPresenter.getAppPreference().getActiveSubscriptionPriceCurrencyCode() : appCMSPresenter.getUserSubscriptionPlanResult().getPlanDetails().get(0).getRecurringPaymentCurrencyCode();
            String renewalCycleType = appCMSPresenter.getAppPreference().getActiveSubscriptionPlanCycle() != null ? appCMSPresenter.getAppPreference().getActiveSubscriptionPlanCycle() : appCMSPresenter.getUserSubscriptionPlanResult().getRenewalCycleType();
            int renewCycleMultiplier = appCMSPresenter.getAppPreference().getActiveSubscriptionPlanCyclePeriodMultiplier() > 0 ? appCMSPresenter.getAppPreference().getActiveSubscriptionPlanCyclePeriodMultiplier() : appCMSPresenter.getUserSubscriptionPlanResult().getRenewalCyclePeriodMultiplier();

            String dayType = appCMSPresenter.getLocalisedStrings().getDayUpper();
            String weekType = appCMSPresenter.getLocalisedStrings().getWeek();
            String monthType = metadataMap == null ? "MONTH" : metadataMap.getBillingHistoryMonthLocalization() != null ? metadataMap.getBillingHistoryMonthLocalization() : "MONTH";
            String yearType = metadataMap == null ? "YEAR" : metadataMap.getBillingHistoryYearLocalization() != null ? metadataMap.getBillingHistoryYearLocalization() : "YEAR";


            switch (renewalCycleType) {
                case "YEAR": {

                    priceInfo.append(currencyCode + " ");
                    priceInfo.append(priceValue + "/");
                    priceInfo.append(renewCycleMultiplier > 1 ?
                            renewCycleMultiplier + " " + yearType : yearType);
                    priceValueSecondry = round((priceValue / (12 * renewCycleMultiplier)), 2);
                    priceInfo.append(" (" + currencyCode + " " + priceValueSecondry + "/" + monthType + ")");

                }
                break;
                case "MONTH": {

                    priceInfo.append(currencyCode + " ");
                    priceInfo.append(priceValue + "/");
                    priceInfo.append(renewCycleMultiplier > 1 ?
                            renewCycleMultiplier + " " + monthType : monthType);
                    if (renewCycleMultiplier > 1) {
                        priceValueSecondry = round((priceValue / renewCycleMultiplier), 2);
                        priceInfo.append(" (" + currencyCode + " " + priceValueSecondry + "/" + monthType + ")");
                    }
                }
                break;
                case "WEEK": {

                    priceInfo.append(currencyCode + " ");
                    priceInfo.append(priceValue + "/");
                    priceInfo.append(renewCycleMultiplier > 1 ?
                            renewCycleMultiplier + " " + weekType : weekType);
                    priceValueSecondry = round((priceValue / (7 * renewCycleMultiplier)), 2);
                    priceInfo.append(" (" + currencyCode + " " + priceValueSecondry + "/" + dayType + ")");

                }
                break;
                case "DAY": {
                    priceInfo.append(currencyCode + " ");
                    priceInfo.append(priceValue + "/");
                    priceInfo.append(renewCycleMultiplier > 1 ?
                            renewCycleMultiplier + " " + dayType : dayType);
                    if (renewCycleMultiplier > 1) {
                        priceValueSecondry = round((priceValue / renewCycleMultiplier), 2);
                        priceInfo.append(" (" + currencyCode + " " + priceValueSecondry + "/" + dayType + ")");
                    }
                }
                break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return priceInfo.toString();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String Play_Store_Country_Code = "";

    public static String getPlay_Store_Country_Code(AppCMSPresenter appCMSPresenter,String countryCode) {
        if (appCMSPresenter.getAppPreference().getPlay_Store_Country_Code() != null ){
            return appCMSPresenter.getAppPreference().getPlay_Store_Country_Code();
        }
        return TextUtils.isEmpty(Play_Store_Country_Code)?countryCode:Play_Store_Country_Code;
    }

    public static void getAvailableCurrencies(String currencyCode) {
        Locale[] locales = Locale.getAvailableLocales();

        // We use TreeMap so that the order of the data in the map sorted
        // based on the country name.
        if (TextUtils.isEmpty(Play_Store_Country_Code)) {
            for (Locale locale : locales) {
                try {

                    //System.out.println("https locale.getISO3Country() " + locale.getISO3Country() + " locale.getDisplayCountry() " + locale.getDisplayCountry() + " locale.getCountry(): " + locale.getCountry() +" local currency:- "+ Currency.getInstance(locale).getCurrencyCode());
                    if (Currency.getInstance(locale).getCurrencyCode().equalsIgnoreCase(currencyCode)) {
                        Play_Store_Country_Code = locale.getCountry();
                        break;
                    }
                } catch (Exception e) {
                    // when the locale is not supported
                }
            }
        }

    }
    public static HashMap<String, SpannableString> planViewPriceList = new HashMap<>();


    public static String trayThumbnailRatio(String thumbnailType){

        switch (thumbnailType){
            case "portrait":
                return "3:4";
            case "landscape":
                return "16:9";
            case "_1*1":
                return "1:1";
            case "_9*16":
                return "9:16";
            case "_32*9":
                return "32:9";
            default:
                return "16:9";

        }

    }
    public static float getTrayWidth(String ratio, Context context){
        switch (ratio){
            case "16:9":
                return BaseView.isTablet(context)? THUMB_IMAGE_16x9_WIDTH_TABLET: THUMB_IMAGE_16x9_WIDTH_MOBILE;
            case "9:16":
                return BaseView.isTablet(context)? THUMB_IMAGE_9x16_WIDTH_TABLET: THUMB_IMAGE_9x16_WIDTH_MOBILE;
            case "1:1":
                return BaseView.isTablet(context)? THUMB_IMAGE_1x1_WIDTH_TABLET: THUMB_IMAGE_1x1_WIDTH_MOBILE;
            case "3:4":
                return BaseView.isTablet(context)? THUMB_IMAGE_3x4_WIDTH_TABLET: THUMB_IMAGE_3x4_WIDTH_MOBILE;
            case "32:9":
                return BaseView.isTablet(context)? THUMB_IMAGE_32x9_WIDTH_TABLET: THUMB_IMAGE_32x9_WIDTH_MOBILE;
            default:
                return BaseView.isTablet(context)? THUMB_IMAGE_16x9_WIDTH_TABLET: THUMB_IMAGE_16x9_WIDTH_MOBILE;
        }
    }

    public static List<String> recommendedIds;
    public static void recommendationPlay(AppCMSPresenter appCMSPresenter, Module moduleAPI){

        if (appCMSPresenter.getAppCMSMain().getRecommendation() != null &&
                (appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation()
                        || appCMSPresenter.getAppCMSMain().getRecommendation().isPersonalization())
                && appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendationAutoPlay()) {
            recommendedIds = new ArrayList<>();
            if (moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) {
                for (ContentDatum data : moduleAPI.getContentData()) {
                    if ((data.getGist().getContentType() != null
                            && !data.getGist().getContentType().toLowerCase().equalsIgnoreCase("series"))
                            || (data.getGist().getMediaType() != null
                            && !data.getGist().getMediaType().toLowerCase().equalsIgnoreCase("series"))) {
                        recommendedIds.add(data.getGist().getId());
                    }
                }
            }
        }
    }

    public static boolean isTVDevice(Context context) {
        // https://developer.android.com/training/tv/start/hardware.html#java
        if (context != null) {
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
            return uiModeManager != null && uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
        }
        return false;
    }

    public static String getUserAgent(AppCMSPresenter appCMSPresenter) {
        String appVersion = "";
        String appName    = "";
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            appVersion = appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_app_version);
            appName    = appCMSPresenter.getCurrentActivity().getString(R.string.app_name);
        }
        return System.getProperty("http.agent") + " " + appName + "/" + appVersion;
    }

    public static void clearPersonalizationModuleData(){
        personalizationTraysData.clear();
        personalizationTraysModule.clear();
    }

    public static boolean isValidDeepLink(Context context, Uri deepLinkQuery) {
        return deepLinkQuery != null
                && deepLinkQuery.getScheme() != null
                && deepLinkQuery.getScheme().equalsIgnoreCase(context.getString(R.string.video_player_deep_link))
                && !TextUtils.isEmpty(deepLinkQuery.getPath())
                && !Objects.requireNonNull(deepLinkQuery.getPath()).equalsIgnoreCase("*");
    }

    public static boolean isOnePlusTV() {
        return "oneplus".equalsIgnoreCase(Build.BRAND);
    }

    private static boolean isCustomReceiverIdExist;

    public static boolean isIsCustomReceiverIdExist() {
        return isCustomReceiverIdExist;
    }

    public static void setCustomReceiverId(String customReceiverId) {
        isCustomReceiverIdExist = !isEmpty(customReceiverId);
    }

    public static int getNotificationId(){
        //int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        //android.util.Log.d(TAG, moduleId+" CarouselItemAdapter:  m time " + m);
        Random random = new Random();
        int notificationId = random.nextInt(9999 - 1000) + 1000;
        //android.util.Log.d(TAG, moduleId+" CarouselItemAdapter:  m random " + m1);
        return notificationId;
    }

    public static String getLanguageNamefromCode(String lang) {
        Locale loc = new Locale(lang);
        String name = loc.getDisplayLanguage(loc);
        return name;
    }
    public static boolean isSuggestedPaymentType(List<String> paymentTypeList, String paymentType) {
        if (paymentTypeList == null || paymentTypeList.isEmpty()) {
            return true;
        }

        for (String type : paymentTypeList) {
            if (type.contains(paymentType))
                return true;
        }
        return false;
    }
    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

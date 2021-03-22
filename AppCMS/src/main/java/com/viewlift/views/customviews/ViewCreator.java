package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MetricAffectingSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.casting.CastHelper;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.models.data.appcms.EquipmentElement;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.BaseInterface;
import com.viewlift.models.data.appcms.api.CategoryPages;
import com.viewlift.models.data.appcms.api.ConceptData;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.CreditBlock;
import com.viewlift.models.data.appcms.api.Fights;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.ImageGist;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.api.Rounds;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.audio.AppCMSAudioDetailResult;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.downloads.UserVideoDownloadStatus;
import com.viewlift.models.data.appcms.history.Record;
import com.viewlift.models.data.appcms.history.UserVideoStatusResponse;
import com.viewlift.models.data.appcms.photogallery.PhotoGalleryGridInsetDecoration;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.EmailConsent;
import com.viewlift.models.data.appcms.ui.main.EmailConsentMessage;
import com.viewlift.models.data.appcms.ui.main.WhatsAppConsent;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.models.network.background.tasks.SearchQuery;
import com.viewlift.offlinedrm.FetchOffineDownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.utils.FileUtils;
import com.viewlift.utils.ImageGetterFromHTMLText;
import com.viewlift.utils.LocalModuleList;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.adapters.AppCMSArticleFeedViewAdapter;
import com.viewlift.views.adapters.AppCMSBenefitPlanPageAdapter;
import com.viewlift.views.adapters.AppCMSBrowseAllAdapter;
import com.viewlift.views.adapters.AppCMSBrowseCategoryAdapter;
import com.viewlift.views.adapters.AppCMSCarouselItemAdapter;
import com.viewlift.views.adapters.AppCMSCarouselTimeAdapter;
import com.viewlift.views.adapters.AppCMSDownloadQualityAdapter;
import com.viewlift.views.adapters.AppCMSFightSelectionAdapter;
import com.viewlift.views.adapters.AppCMSLiveWeekViewAdapter;
import com.viewlift.views.adapters.AppCMSPageLinkHorizontalGridAdapter;
import com.viewlift.views.adapters.AppCMSPlansAdapter;
import com.viewlift.views.adapters.AppCMSPlaylistAdapter;
import com.viewlift.views.adapters.AppCMSRosterAdapter;
import com.viewlift.views.adapters.AppCMSSeeAllAdapter;
import com.viewlift.views.adapters.AppCMSSubNavAdapter;
import com.viewlift.views.adapters.AppCMSTraySeasonItemAdapter;
import com.viewlift.views.adapters.AppCMSTrayViewAdapter;
import com.viewlift.views.adapters.AppCMSUserWatHisDowAdapter;
import com.viewlift.views.adapters.AppCMSViewAdapter;
import com.viewlift.views.adapters.EquipmentGridAdapter;
import com.viewlift.views.adapters.ExpandableFilterAdapter;
import com.viewlift.views.adapters.ExpandableGridAdapter;
import com.viewlift.views.adapters.SearchSuggestionsAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.binders.AppCMSVideoPageBinder;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.customviews.listdecorator.SeparatorDecoration;
import com.viewlift.views.customviews.moduleview.constraintview.ConstraintModule;
import com.viewlift.views.customviews.moduleview.constraintview.VideoPlayerInfo04;
import com.viewlift.views.customviews.plans.SubscriptionMetaDataView;
import com.viewlift.views.customviews.plans.ViewPlansMetaDataView;
import com.viewlift.views.customviews.season.ConceptDetailModule;
import com.viewlift.views.customviews.season.PersonDetailModule;
import com.viewlift.views.customviews.season.SeasonModule;
import com.viewlift.views.dialog.CustomShape;
import com.viewlift.views.listener.PaginationScrollListener;
import com.viewlift.views.rxbus.CustomSubscriber;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;
import com.viewlift.views.utilities.ImageLoader;
import com.viewlift.views.utilities.ImageUtils;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import rx.functions.Action1;

import static android.view.ViewGroup.FOCUS_BEFORE_DESCENDANTS;
import static com.viewlift.Utils.loadJsonFromAssets;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.*;
import static com.viewlift.presenters.AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE_ALERT;
import static com.viewlift.views.customviews.CustomWebView.mFbLiveView;

/**
 * This class will utilize ingested AppCMS UI JSON and API data JSON files to dynamically create the views for entire pages,
 * including the modules and component child views.  It is used to create and return a PageView and will descend through
 * to inspect all the child elements in the input AppCMS UI JSON responses.  It will currently ignore the masthead and footer
 * modules.
 */
public class ViewCreator {
    private static final String TAG = "ViewCreator";
    private static final long SECS_TO_MSECS = 1000L;
    private static final int PAGE_START = 1;
    public static String searchQueryText = "";
    private static AppCMSVideoPageBinder videoPlayerViewBinder;
    private static VideoPlayerContent videoPlayerContent = new VideoPlayerContent();
    public static CountDownTimer countDownTimer = null;
    private final int countDownIntervalInMillis = 1000;
    AppCMSPlaylistAdapter appCMSPlaylistAdapter;
    PhotoGalleryNextPreviousListener photoGalleryNextPreviousListener;
    LinearLayoutManager datesManager, weeksManager;
    boolean datesScrollListener;
    boolean weeksScrollListener;
    AppCMSUserWatHisDowAdapter appCMSUserWatHisDowAdapter = null;
    AppCMSFightSelectionAdapter appcmsFightSelectionAdapter = null;
    AppCMSRosterAdapter appcmsRosterAdapter = null;
    AppCMSViewAdapter appCMSViewAdapter;
    AppCMSSubNavAdapter appcmsSubNavAdapter = null;
    ExpandableFilterAdapter expandableFilterAdapter = null;
    ExpandableGridAdapter expandableGridAdapter = null;
    SearchView appCMSSearchView;
    private CustomVideoPlayerView videoPlayerView;
    private boolean ignoreBinderUpdate;
    private ComponentViewResult componentViewResult;
    private boolean isCastConnected;
    private int position = 0;
    private RecyclerView recyclerViewDates, recyclerViewWeeks;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private GridLayoutManager gridLayoutManager;
    private ConstraintViewCreator constraintViewCreator;
    private String pageId;
    ContentTypeChecker contentTypeChecker;
    boolean isTVOD = false;


    public ViewCreator() {

    }

    public static void setViewWithShowSubtitle(AppCMSPresenter appCMSPresenter, Context context, ContentDatum data, View view,
                                               boolean isJumbotron) {
        StringBuilder subtitleSb;

        if (isJumbotron) {
            subtitleSb = new StringBuilder();
            String primaryCategory = data.getGist().getPrimaryCategory() != null ?
                    data.getGist().getPrimaryCategory().getTitle() : null;
            if (!TextUtils.isEmpty(primaryCategory)) {
                subtitleSb.append(primaryCategory.toUpperCase());
            }
        } else {
            int totalEpisodes = getTotalNumberOfEpisodes(data);
            subtitleSb = new StringBuilder(String.valueOf(totalEpisodes));
            subtitleSb.append(context.getString(R.string.blank_separator));
            if (totalEpisodes == 1)
                subtitleSb.append(appCMSPresenter.getLocalisedStrings().getEpisodeText().toUpperCase());
            else
                subtitleSb.append(appCMSPresenter.getLocalisedStrings().getEpisodesHeaderText());

            String primaryCategory = data.getGist().getPrimaryCategory() != null ?
                    data.getGist().getPrimaryCategory().getTitle() : null;
            if (!TextUtils.isEmpty(primaryCategory)) {
                subtitleSb.append(context.getString(R.string.text_separator));
                subtitleSb.append(primaryCategory.toUpperCase());
            }
        }
        ((TextView) view).setText(subtitleSb.toString());
        view.setAlpha(0.6f);
    }

    public static void setViewWithBundleSubtitle(AppCMSPresenter appCMSPresenter, Context context, ContentDatum data, View view,
                                                 boolean isJumbotron) {
        StringBuilder subtitleSb;

        if (isJumbotron) {
            subtitleSb = new StringBuilder();
            String primaryCategory = data.getGist().getPrimaryCategory() != null ?
                    data.getGist().getPrimaryCategory().getTitle() : null;

            if (!TextUtils.isEmpty(primaryCategory)) {
                subtitleSb.append(primaryCategory.toUpperCase());
            }
        } else {
            int totalEpisodes = data.getGist().getBundleList().size();
            subtitleSb = new StringBuilder(String.valueOf(totalEpisodes));
            subtitleSb.append(context.getString(R.string.blank_separator));
            if (totalEpisodes > 1) {
                subtitleSb.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_films_title)));
            } else {
                subtitleSb.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_film_title)));
            }
            /*subtitleSb.append(context.getResources().getQuantityString(R.plurals.film_subtitle_text,
                    totalEpisodes));*/


            String primaryCategory = data.getGist().getPrimaryCategory() != null ?
                    data.getGist().getPrimaryCategory().getTitle() : null;


            if (!TextUtils.isEmpty(primaryCategory)) {
                subtitleSb.append(context.getString(R.string.text_separator));
                subtitleSb.append(primaryCategory.toUpperCase());
            }
        }

        ((TextView) view).setText(subtitleSb.toString());
        view.setAlpha(0.6f);
    }

    public static int getTotalNumberOfEpisodes(ContentDatum data) {
        int totalEpisodes = 0;
        List<Season_> seasons = data.getSeason();
        int numSeasons = seasons.size();
        for (int i = 0; i < numSeasons; i++) {
            if (seasons.get(i).getEpisodes() != null) {
                totalEpisodes += seasons.get(i).getEpisodes().size();
            }
        }

        return totalEpisodes;
    }

    /**
     * Fix for JM-26
     */
    public static void setViewWithSubtitle(Context context, ContentDatum data, View view, AppCMSPresenter appCMSPresenter) {

        long durationInSeconds = data.getGist().getRuntime();

        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;

        String appendDivider = "";
        String year = data.getGist().getYear();
        String primaryCategory =
                data.getGist().getPrimaryCategory() != null ?
                        data.getGist().getPrimaryCategory().getTitle() :
                        null;

        StringBuilder infoText = new StringBuilder();
        String time_abberviation = appCMSPresenter.getLocalisedStrings().getMinText();
        if (minutes == 1) {
            infoText.append("0").append(minutes).append(" ").append(time_abberviation);
        } else if (minutes > 1 && minutes < 10) {
            infoText.append("0").append(minutes).append(" ").append(time_abberviation);
        } else if (minutes >= 10) {
            infoText.append(minutes).append(" ").append(time_abberviation);
        } else if (minutes == 0) {
            infoText.append(minutes).append(" ").append(time_abberviation);
        }

        if (durationInSeconds == 0) {
            infoText.delete(0, infoText.length());
            infoText.append("");
        }

        if (!TextUtils.isEmpty(year)) {
            appendDivider = infoText.toString().length() > 0 ? context.getString(R.string.text_separator) : "";
            infoText.append(appendDivider);
            infoText.append(year);
        }

        if (!TextUtils.isEmpty(primaryCategory)) {
            appendDivider = infoText.toString().length() > 0 ? context.getString(R.string.text_separator) : "";
            infoText.append(appendDivider);
            infoText.append(primaryCategory.toUpperCase());
        }

        ((TextView) view).setText(infoText.toString());
        view.setAlpha(0.6f);
    }

    public static boolean isScheduleContentVisible(final ContentDatum data, AppCMSPresenter appCMSPresenter) {

        boolean isVisible = true;
        if (data.getGist() != null) {
            long scheduleStartDate = (data.getGist().getScheduleStartDate());
            long scheduleEndDate = (data.getGist().getScheduleEndDate());

            //calculate remaining time from event date and current date
            long timeToStart = CommonUtils.getTimeIntervalForEvent(scheduleStartDate, "yyyy MMM dd HH:mm:ss");
            long endTimeMillis = CommonUtils.getTimeIntervalForEvent(scheduleEndDate, "yyyy MMM dd HH:mm:ss");
            /**
             * if user is purchased video but video is not started i.e. video start time is greater than current time
             * .Then hide the play icon
             */
            if ((data.getGist().getPurchaseStatus() != null && data.getGist().getPurchaseStatus().equalsIgnoreCase("PURCHASED"))) {
                isVisible = true;

            } else if ((data.getGist().getPurchaseType() != null && data.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")) && ((scheduleStartDate > 0 && timeToStart > 0) || (scheduleEndDate > 0 && endTimeMillis < 0))) {
                isVisible = false;

            } else if (((data.getGist().getPurchaseType() != null && !data.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")) &&
                    ((scheduleStartDate > 0 && timeToStart > 0) || (scheduleEndDate > 0 && endTimeMillis < 0))) || ((data.getGist().getPurchaseType() == null)
                    && ((scheduleStartDate > 0 && timeToStart > 0) || (scheduleEndDate > 0 && endTimeMillis < 0)))) {

                /**
                 * if user is not purchased video or rented then we will check its end time also with start time  i.e. video start
                 * time is greater than current time and end time is less than current time
                 * .Then hide the play icon
                 */
                isVisible = false;

            } else if ((data != null &&
                    data.getGist() != null &&
                    data.getGist().getContentType() != null) &&
                    (data.getGist().getContentType().equalsIgnoreCase("SERIES") || data.getGist().getContentType().equalsIgnoreCase("SEASON"))
            ) {
                isVisible = false;
            }

            if (data.getGist() != null && data.getGist().isLiveStream())
                isVisible = false;
        }

        return isVisible;
    }

    /**
     * check if content has both start date and end date to
     * check if video is scheduled or not
     *
     * @param data
     * @param appCMSPresenter
     * @return
     */
    public static boolean isVideoIsSchedule(final ContentDatum data, AppCMSPresenter appCMSPresenter) {

        boolean isSchedule = false;
        long scheduleStartDate = 0;
        long scheduleEndDate = 0;
        if (data.getGist() != null) {
            scheduleStartDate = (data.getGist().getScheduleStartDate());
            scheduleEndDate = (data.getGist().getScheduleEndDate());
        }

        if (scheduleStartDate > 0 && scheduleEndDate > 0) {
            isSchedule = true;
        }

        return /*isSchedule;*/false;//fixed for MOTVWA-1139
    }

    public static long adjustColor1(long color1, long color2) {
        double ratio = (double) color1 / (double) color2;
        if (1.0 <= ratio && ratio <= 1.1) {
            color1 *= 0.8;
        }
        return color1;
    }


    public static void setTypeFace(Context context,
                                   AppCMSPresenter appCMSPresenter,
                                   Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                   Component component,
                                   View textView) {
        AppCMSUIKeyType fontWeight = null;

        if (component != null)
            fontWeight = jsonValueKeyMap.get(component.getFontWeight());

        if (fontWeight == null) {
            fontWeight = PAGE_EMPTY_KEY;
        }
        Typeface face = null;
        boolean dinpro = false;
        boolean proximaNova = false;
        boolean titillium_web = false;
        boolean cabin = false;
        boolean montserrat = false;
        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null
                && appCMSPresenter.getAppCMSMain().getBrand().getGeneral() != null
                && appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getFontFamily() != null) {
            if (appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getFontFamily().equalsIgnoreCase(context.getString(R.string.font_din_pro))) {
                dinpro = true;
            } else if (appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getFontFamily().equalsIgnoreCase(context.getString(R.string.font_proxima_nova))) {
                proximaNova = true;
            } else if (appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getFontFamily().equalsIgnoreCase(context.getString(R.string.font_titillium_web))) {
                titillium_web = true;
            } else if (appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getFontFamily().equalsIgnoreCase(context.getString(R.string.font_cabin))) {
                cabin = true;
            } else if (appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getFontFamily().equalsIgnoreCase(context.getString(R.string.font_montserrat))) {
                montserrat = true;
            }
        }


        switch (fontWeight) {
            case PAGE_TEXT_LIGHT_KEY:
                face = appCMSPresenter.getLightTypeFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_light);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_light);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_regular);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_regular);
                    else if (montserrat)
                        face = ResourcesCompat.getFont(context, R.font.montserrat_regular);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_light);
                    appCMSPresenter.setLightTypeFace(face);
                }
                break;
            case PAGE_TEXT_BOLD_KEY:
                face = appCMSPresenter.getBoldTypeFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_bold);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_bold);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_bold);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_bold);
                    else if (montserrat)
                        face = ResourcesCompat.getFont(context, R.font.montserrat_bold);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_bold);
                    appCMSPresenter.setBoldTypeFace(face);
                }
                break;

            case PAGE_TEXT_SEMIBOLD_KEY:
                face = appCMSPresenter.getSemiBoldTypeFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_semi_bold);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_semi_bold);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_semi_bold);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_semi_bold);
                    else if (montserrat)
                        face = ResourcesCompat.getFont(context, R.font.montserrat_semi_bold);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_semi_bold);
                    appCMSPresenter.setSemiBoldTypeFace(face);
                }
                break;

            case PAGE_TEXT_EXTRABOLD_KEY:
                face = appCMSPresenter.getExtraBoldTypeFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_extra_bold);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_bold_italic);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_extra_bold);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_extra_bold);
                    else if (montserrat)
                        face = ResourcesCompat.getFont(context, R.font.montserrat_extra_bold);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_extra_bold);
                    appCMSPresenter.setExtraBoldTypeFace(face);
                }
                break;

            case PAGE_TEXT_BLACK_ITALIC_KEY:
                face = appCMSPresenter.getItalicTypeFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_italic);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_regular_italic);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_italic);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_italic);
                    else if (montserrat)
                        face = ResourcesCompat.getFont(context, R.font.montserrat_italic);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_italic);
                    appCMSPresenter.setItalicTypeFace(face);
                }
                break;

            case PAGE_EMPTY_KEY:
                face = appCMSPresenter.getRegularFontFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_regular);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_regular);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_regular);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_regular);
                    else if (montserrat)
                        face = ResourcesCompat.getFont(context, R.font.montserrat_regular);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_regular);
                    appCMSPresenter.setRegularFontFace(face);
                }
                break;

            default:
                face = appCMSPresenter.getRegularFontFace();
                if (face == null) {
                    if (dinpro)
                        face = ResourcesCompat.getFont(context, R.font.dinpro_font_regular);
                    else if (proximaNova)
                        face = ResourcesCompat.getFont(context, R.font.proxima_nova_regular);
                    else if (titillium_web)
                        face = ResourcesCompat.getFont(context, R.font.titillum_web_regular);
                    else if (cabin)
                        face = ResourcesCompat.getFont(context, R.font.cabin_font_regular);
                    else
                        face = ResourcesCompat.getFont(context, R.font.font_regular);
                    appCMSPresenter.setRegularFontFace(face);
                }
                break;
        }


        if (textView instanceof TextView)
            ((TextView) textView).setTypeface(face);
        if (textView instanceof AppCompatEditText)
            ((AppCompatEditText) textView).setTypeface(face);
        if (textView instanceof Button)
            ((Button) textView).setTypeface(face);
        if (textView instanceof TextInputLayout)
            ((TextInputLayout) textView).setTypeface(face);
    }


    /**
     * This will prepend a '#' character and a hex value of the alpha or opacity value to a color string if the '#' character is missing
     *
     * @param context          This is the context value that created UI components should use
     * @param baseColorCode    This is the color string to prepend the '#' value and the alpha or opacity value
     * @param opacityColorCode This is the opacity value to prepend to the color value
     * @return The original color value or a the color prepended with a '#' character followed by the alpha value
     */
    public static String getColorWithOpacity(Context context, String baseColorCode, int opacityColorCode) {
        if (baseColorCode.indexOf(context.getString(R.string.color_hash_prefix)) != 0) {
            return context.getString(R.string.color_hash_prefix) + opacityColorCode + baseColorCode;
        }
        return baseColorCode;
    }

    public static void updateForDownload(ContentDatum contentDatum, AppCMSPresenter appCMSPresenter, ImageButton imageButton, UpdateDownloadImageIconAction updateDownloadImageIconAction, View.OnClickListener addClickListener) {
        if (appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserSubscribed() && contentDatum.getGist() != null &&
                contentDatum.getGist().getMediaType() != null &&
                contentDatum.getGist().getMediaType().toLowerCase().contains(imageButton.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(imageButton.getContext().getString(R.string.content_type_audio).toLowerCase())) {
            if (contentDatum.getStreamingInfo() == null ||
                    contentDatum.getStreamingInfo().getAudioAssets() == null ||
                    contentDatum.getStreamingInfo().getAudioAssets().getMp3() == null ||
                    contentDatum.getStreamingInfo().getAudioAssets().getMp3().getUrl() == null ||
                    TextUtils.isEmpty(contentDatum.getStreamingInfo().getAudioAssets().getMp3().getUrl())) {
                appCMSPresenter.getAudioDetail(updateDownloadImageIconAction.contentDatum.getGist().getId(),
                        0, null, false, false, 0,
                        new AppCMSPresenter.AppCMSAudioDetailAPIAction(false,
                                false,
                                false,
                                null,
                                updateDownloadImageIconAction.contentDatum.getGist().getId(),
                                updateDownloadImageIconAction.contentDatum.getGist().getId(),
                                null,
                                updateDownloadImageIconAction.contentDatum.getGist().getId(),
                                false, null) {
                            @Override
                            public void call(AppCMSAudioDetailResult appCMSAudioDetailResult) {
                                AppCMSPageAPI audioApiDetail = appCMSAudioDetailResult.convertToAppCMSPageAPI(updateDownloadImageIconAction.contentDatum.getGist().getId());
                                if (audioApiDetail.getModules().get(0).getContentData().get(0) != null) {

                                    appCMSPresenter.editDownload(null, audioApiDetail.getModules().get(0).getContentData().get(0), updateDownloadImageIconAction, null, null);
                                }
                            }
                        });
            } else {
                appCMSPresenter.editDownload(null, updateDownloadImageIconAction.contentDatum, updateDownloadImageIconAction, new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean)
                            imageButton.setOnClickListener(addClickListener);
                    }
                }, null);
            }
        } else {
            appCMSPresenter.videoEntitlementDownload(contentDatum, new Action1<ContentDatum>() {
                @Override
                public void call(ContentDatum entitlementContentDatum) {
                    ContentTypeChecker contentTypeChecker = new ContentTypeChecker(imageButton.getContext());
                    boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && (appCMSPresenter.getAppPreference().getUserPurchases() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                            && (contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), entitlementContentDatum.getGist().getId())
                            || (entitlementContentDatum.getSeasonId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), entitlementContentDatum.getSeasonId()))
                            || (entitlementContentDatum.getSeriesId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), entitlementContentDatum.getSeriesId()))));
                    boolean isTveContentDownload = appCMSPresenter.getAppPreference().getTVEUserId() != null && entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentTVE(entitlementContentDatum.getSubscriptionPlans());
                    boolean isFreeContent = entitlementContentDatum.getSubscriptionPlans() != null && (contentTypeChecker.isContentAVOD(entitlementContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentFree(entitlementContentDatum.getSubscriptionPlans()));
                    if ((appCMSPresenter.isAppAVOD() && appCMSPresenter.isDownloadEnable())
                            || isContentPurchased
                            || isTveContentDownload
                            || isFreeContent
                            || CommonUtils.isUserPurchaseItem(entitlementContentDatum.getGist().getId())
                            || (appCMSPresenter.isUserSubscribed() /*&& checkContentType.isContentSVOD(entitlementContentDatum)*/)) {
                        if (entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(entitlementContentDatum.getSubscriptionPlans()) && !entitlementContentDatum.getSubscriptionPlans().get(0).getFeatureSetting().isDownloadAllowed()) {
                            appCMSPresenter.showDialog(VIDEO_NOT_AVAILABLE_ALERT, appCMSPresenter.getLocalisedStrings().getDownloadUnavailableMsg(), false, null, null, appCMSPresenter.getLocalisedStrings().getAlertTitle());
                        } else if (!appCMSPresenter.getAppPreference().isUserAllowedDownload() && entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentSVOD(entitlementContentDatum.getSubscriptionPlans())) {
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PLAN_UPGRADE,
                                    () -> {
                                        imageButton.setOnClickListener(addClickListener);
                                    }, null);
                        } else if (!appCMSPresenter.isUserLoggedIn() && (contentTypeChecker.isContentAVOD(entitlementContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentFree(entitlementContentDatum.getSubscriptionPlans()))) {
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                                    () -> {
                                        appCMSPresenter.setAfterLoginAction(() -> {
                                        });
                                    }, null);
                        } else if (appCMSPresenter.isDownloadQualityScreenShowBefore()) {
                            appCMSPresenter.editDownload(entitlementContentDatum, updateDownloadImageIconAction.contentDatum, updateDownloadImageIconAction, new Action1<Boolean>() {
                                @Override
                                public void call(Boolean aBoolean) {
                                    if (!aBoolean)
                                        imageButton.setOnClickListener(addClickListener);
                                }
                            }, null);
                        } else {
                            appCMSPresenter.showDownloadQualityScreen(entitlementContentDatum, updateDownloadImageIconAction);
                        }
                    } else if (!appCMSPresenter.isUserLoggedIn()) {
                        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled())
                            appCMSPresenter.openEntitlementScreen(contentDatum, false);
                        else {
                            AppCMSPresenter.DialogType dialogType = AppCMSPresenter.DialogType.LOGIN_AND_SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED;
                            if ((entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentAVOD(entitlementContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentFree(entitlementContentDatum.getSubscriptionPlans()))
                                    || (appCMSPresenter.isAppAVOD() && appCMSPresenter.isDownloadEnable())) {
                                dialogType = AppCMSPresenter.DialogType.LOGIN_REQUIRED;
                            }
                            appCMSPresenter.showEntitlementDialog(dialogType,
                                    () -> {
                                        appCMSPresenter.setAfterLoginAction(() -> {
                                        });
                                    }, null);
                        }
                    } else if (!appCMSPresenter.isUserSubscribed()) {
                        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled())
                            appCMSPresenter.openEntitlementScreen(contentDatum, false);
                        else
                            appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED,
                                    () -> {
                                        appCMSPresenter.setAfterLoginAction(() -> {
                                        });
                                    }, null);
                    } else {
                        appCMSPresenter.openEntitlementScreen(entitlementContentDatum, false);
                    }
                }
            });
        }
    }

    //stop other countdown timer if running
    public static void stopCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public static CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public static void setSearchText(String queryText) {
        searchQueryText = queryText;
    }

    public static Module removePastEventsAndShowSpecificDayEvents(Module moduleAPI, int day) {
        Module newModuleApi = new Module();
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < adapterData.size(); i++) {
                String[] next7Days = next7Days();
                if (next7Days[day].equals(millisToDate(adapterData.get(i).getGist().getScheduleStartDate()))
                        && adapterData.get(i).getGist().getScheduleEndDate() >= System.currentTimeMillis()) {
                    tempData.add(adapterData.get(i));
                }
            }
            newModuleApi.setContentData(tempData);
        }
        return newModuleApi;
    }

    public static Module setPageLinkCategoryDataInList(Module moduleAPI, int position) {
        Module newModuleApi = new Module();
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            ContentDatum cDatum = adapterData.get(position);
            List<CategoryPages> categoryPages = new ArrayList<>();
            for (int index = 0; index < cDatum.getPages().size(); index++) {
                //if (cDatum.getPages().get(index).getOgDetails() != null) {
                ContentDatum contentDatum = new ContentDatum();
                categoryPages.add(cDatum.getPages().get(index));
                //cDatum.getPages().get(index).getOgDetails().setThumbnailUrl("https://snagfilms-a.akamaihd.net/7fa0ea9a-9799-4417-99f5-cbb5343c551d/images/21/97/66bea5a94ab4bbee644ee06ce7f0/1565812057251_600x3381_16x9Images.jpg");
                contentDatum.setPages(categoryPages);
                tempData.add(contentDatum);
                //}
            }
            newModuleApi.setContentData(tempData);
        }
        return newModuleApi;
    }

    public static String[] next7Days() {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Calendar date = Calendar.getInstance();
        String[] calendarDays = new String[30];

        for (int i = 0; i < 30; i++) {
            calendarDays[i] = format.format(date.getTime());
            date.add(Calendar.DAY_OF_WEEK, 1);
        }
        return calendarDays;
    }

    public static String millisToDate(long millis) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(millis);

    }

    public static Module getSpecificNumberOfObjects(Module moduleAPI, int num) {
        Module newModuleApi = new Module();
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < adapterData.size(); i++) {
                if (i < num) {
                    tempData.add(adapterData.get(i));
                }
            }
            newModuleApi.setContentData(tempData);
        }
        return newModuleApi;
    }

    public CustomWebView getWebViewComponent(Context context, Module moduleAPI, Component component, String key, AppCMSPresenter appCMSPresenter, AppCMSUIKeyType moduleType) {
        CustomWebView webView = new CustomWebView(context, appCMSPresenter);

        String webViewUrl, html;
        if (moduleType == PAGE_AC_WEB_FRAME_03_KEY) {
            webViewUrl = moduleAPI.getRawText();
            webView.loadWebVideoUrl(appCMSPresenter, webViewUrl);
        } else if (moduleAPI != null && moduleAPI.getRawText() != null) {
            int height = ((int) component.getLayout().getMobile().getHeight()) - 55;
            webViewUrl = moduleAPI.getRawText();
            html = "<iframe width=\"" + "100%" + "\" height=\"" + height + "px\" style=\"border: 0px solid #cccccc;\" src=\"" + webViewUrl + "\" ></iframe>";
            webView.loadURLData(context, appCMSPresenter, html, key);
        } else if (moduleAPI != null && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0
                && moduleAPI.getContentData().get(0).getGist() != null
                && moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
            webViewUrl = context.getString(R.string.app_cms_article_api,
                    appCMSPresenter.getAppCMSMain().getDomainName(),
                    moduleAPI.getContentData().get(0).getGist().getPermalink(), appCMSPresenter.getLanguage().getLanguageCode());
            webView.loadURL(context, appCMSPresenter, webViewUrl.replaceAll("\\s+", ""), key);
        }
        return webView;
    }

    private void enablePhotoGalleryButtons(Boolean prevButton, boolean nextButton, PageView pageView, AppCMSPresenter appCMSPresenter, String position) {
        if (pageView.findChildViewById(R.id.photo_gallery_next_button) != null) {
            pageView.findChildViewById(R.id.photo_gallery_next_button).setBackgroundColor(nextButton ? appCMSPresenter.getBrandPrimaryCtaColor() : Color.parseColor("#c8c8c8"));
            pageView.findChildViewById(R.id.photo_gallery_next_button).setEnabled(nextButton);
        }
        if (pageView.findChildViewById(R.id.photo_gallery_prev_button) != null) {
            pageView.findChildViewById(R.id.photo_gallery_prev_button).setBackgroundColor(prevButton ? appCMSPresenter.getBrandPrimaryCtaColor() : Color.parseColor("#c8c8c8"));
            pageView.findChildViewById(R.id.photo_gallery_prev_button).setEnabled(prevButton);
        }
        if (pageView.findChildViewById(R.id.photo_gallery_image_count) != null) {
            ((TextView) pageView.findChildViewById(R.id.photo_gallery_image_count)).setText("" + position);
        }
    }

    private void updateVideoPlayerBinder(AppCMSPresenter appCMSPresenter,
                                         ContentDatum contentDatum) {
        if (!ignoreBinderUpdate ||
                (videoPlayerViewBinder != null &&
                        videoPlayerViewBinder.getContentData() != null &&
                        videoPlayerViewBinder.getContentData().getGist() != null &&
                        videoPlayerViewBinder.getContentData().getGist().getId() != null &&
                        contentDatum != null &&
                        contentDatum.getGist() != null &&
                        !videoPlayerViewBinder.getContentData().getGist().getId().equals(contentDatum.getGist().getId()))) {
            if (videoPlayerViewBinder == null) {
                videoPlayerViewBinder =
                        appCMSPresenter.getDefaultAppCMSVideoPageBinder(contentDatum,
                                -1,
                                contentDatum.getContentDetails().getRelatedVideoIds(),
                                false,
                                false,  /** TODO: Replace with a value that is true if the video is a trailer */
                                !appCMSPresenter.isAppSVOD(),
                                appCMSPresenter.getAppAdsURL(contentDatum),
                                appCMSPresenter.getAppBackgroundColor(), null);
            } else {
                int currentlyPlayingIndex = -1;
                if (videoPlayerViewBinder.getRelateVideoIds() != null &&
                        videoPlayerViewBinder.getRelateVideoIds().contains(contentDatum.getGist().getId())) {
                    currentlyPlayingIndex = videoPlayerViewBinder.getRelateVideoIds().indexOf(contentDatum.getGist().getId());
                } else {
                    videoPlayerViewBinder.setPlayerState(Player.STATE_IDLE);
                    videoPlayerViewBinder.setRelateVideoIds(contentDatum.getContentDetails().getRelatedVideoIds());
                }
                if (videoPlayerViewBinder.getContentData().getGist().getId().equals(contentDatum.getGist().getId())) {
                    currentlyPlayingIndex = videoPlayerViewBinder.getCurrentPlayingVideoIndex();
                }
                videoPlayerViewBinder.setCurrentPlayingVideoIndex(currentlyPlayingIndex);
                videoPlayerViewBinder.setContentData(contentDatum);
            }
        }
        ignoreBinderUpdate = false;
    }

    public void setIgnoreBinderUpdate(boolean ignoreBinderUpdate) {
        this.ignoreBinderUpdate = ignoreBinderUpdate;
    }

    private void forceRedrawOfAllChildren(ViewGroup viewGroup) {
        viewGroup.invalidate();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                forceRedrawOfAllChildren((ViewGroup) v);
            } else {
                v.invalidate();
            }
        }
    }

    /**
     * This is the entry point for the creation of a new page that can be generated by ViewCreator.
     *
     * @param context              This is the context value that created UI components should use
     * @param appCMSPageUI         This is the Page UI response used for the creation of the UI
     * @param appCMSPageAPI        This is the Page API data used to populate the UI fields specified by appCMSPageUI
     * @param appCMSAndroidModules This contains the individual block module definitions used for creating each UI element
     * @param screenName           This is the screen name used to demarcate specific page landing events as well as distinguish this page from other types of screens
     * @param jsonValueKeyMap      This is a hashmap that associates UI string values with value enumerations
     * @param appCMSPresenter      This is a reference to the presenter class which handles all UI events including click events
     * @param modulesToIgnore      This is a list of block modules that ViewCreator should ignore
     * @return Returns a reference to a PageView View that may be rendered on the screen
     */
    public PageView generatePage(Context context,
                                 String pageId,
                                 AppCMSPageUI appCMSPageUI,
                                 AppCMSPageAPI appCMSPageAPI,
                                 AppCMSAndroidModules appCMSAndroidModules,
                                 String screenName,
                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                 AppCMSPresenter appCMSPresenter,
                                 List<String> modulesToIgnore, ConstraintViewCreator constraintViewCreator) {
        if (appCMSPageUI == null) {
            return null;
        }
        this.constraintViewCreator = constraintViewCreator;
        this.pageId = pageId;
        PageView pageView = new PageView(context, appCMSPageUI, appCMSPresenter, pageId);
        pageView.setUserLoggedIn(appCMSPresenter.isUserLoggedIn());
        if (appCMSPresenter.isPageAVideoPage(screenName)) {
            appCMSPresenter.getPageViewLruCache().put(screenName + BaseView.isLandscape(context), new WeakReference<>(pageView));
        } else {
            appCMSPresenter.getPageViewLruCache().put(screenName
                    + BaseView.isLandscape(context), new WeakReference<>(pageView));
        }
        pageView.setReparentChromecastButton(true);

        pageView.setUserLoggedIn(appCMSPresenter.isUserLoggedIn());
        pageView.removeAllAddOnViews();
        pageView.getChildrenContainer().removeAllViews();
        componentViewResult = new ComponentViewResult();
        createPageView(context,
                appCMSPageUI,
                appCMSPageAPI,
                appCMSAndroidModules,
                pageView,
                jsonValueKeyMap,
                appCMSPresenter,
                modulesToIgnore);
        if (appCMSPageAPI != null) {
            try {
                CastServiceProvider.getInstance(appCMSPresenter.getCurrentActivity()).setPageName(appCMSPageAPI.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pageView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

        if (pageView.shouldReparentChromecastButton()) {
            if (appCMSPresenter.getCurrentMediaRouteButton() != null &&
                    appCMSPresenter.getCurrentMediaRouteButton().getParent() != null &&
                    appCMSPresenter.getCurrentMediaRouteButton().getParent() instanceof ViewGroup &&
                    appCMSPresenter.getCurrentMediaRouteButton().getParent() != appCMSPresenter.getCurrentMediaRouteButtonParent()) {
                if (appCMSPresenter.getCurrentMediaRouteButton().getParent() != null) {
                    ((ViewGroup) appCMSPresenter.getCurrentMediaRouteButton().getParent()).removeView(appCMSPresenter.getCurrentMediaRouteButton());
                }
                appCMSPresenter.getCurrentMediaRouteButtonParent().addView(appCMSPresenter.getCurrentMediaRouteButton());
            }
        }

        return pageView;
    }

    public ComponentViewResult getComponentViewResult() {
        return componentViewResult;
    }

    private void createCompoundTopModule(Context context,
                                         List<ModuleList> modulesList,
                                         AppCMSPageAPI appCMSPageAPI,
                                         AppCMSAndroidModules appCMSAndroidModules,
                                         PageView pageView,
                                         Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                         AppCMSPresenter appCMSPresenter,
                                         List<String> modulesToIgnore) {
        List<ModuleView> topViewList = new ArrayList<>();
        float moduleMobileHeight = 0f;

        for (ModuleList moduleInfo : modulesList) {

            ModuleList module = null;
            try {
                if (moduleInfo.getSettings() != null &&
                        moduleInfo.getSettings().isHidden()) {
                    AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                            loadJsonFromAssets(context, "article_hub.json"),
                            AppCMSPageUI.class);

                    if (moduleInfo.getBlockName().contains("eventCarousel01")) {
                        module = appCMSPageUI1.getModuleList().get(7);
                    } else if (moduleInfo.getBlockName().contains("list01")) {
                        module = appCMSPageUI1.getModuleList().get(8);
                    } else if (moduleInfo.getBlockName().contains("mediumRectangleAd01")) {
                        module = appCMSPageUI1.getModuleList().get(10);
                    }
                }
                //module = appCMSAndroidModules.getModuleListMap().get(moduleInfo.getBlockName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (module != null) {
                module.setId(moduleInfo.getId());
                module.setSettings(moduleInfo.getSettings());
                module.setSvod(moduleInfo.isSvod());
                module.setType(moduleInfo.getType());
                module.setView(moduleInfo.getView());
                module.setBlockName(moduleInfo.getBlockName());
                Module moduleAPI = matchModuleAPIToModuleUI(module, appCMSPageAPI, jsonValueKeyMap, appCMSPresenter);
                if (moduleAPI != null) {
                    View childView = createModuleView(context, module, moduleAPI,
                            appCMSAndroidModules,
                            pageView,
                            jsonValueKeyMap,
                            appCMSPresenter, module.isConstrainteView(), module.getSettings());
                    if (childView != null && childView instanceof ModuleView) {
                        moduleMobileHeight = module.getLayout().getMobile().getHeight() + moduleMobileHeight;
                        topViewList.add((ModuleView) childView);
                    }
                }
            }
        } //End of for (ModuleList moduleInfo : modulesList)

        if (topViewList.size() > 0) {
            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                    loadJsonFromAssets(context, "article_hub.json"),
                    AppCMSPageUI.class);
            ModuleList moduleTop = appCMSPageUI1.getModuleList().get(9);
            moduleTop.getLayout().getMobile().setHeight(moduleMobileHeight);
            ModuleView moduleView = new CompoundTopModule(context,
                    moduleTop,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    topViewList);
            pageView.addModuleViewWithModuleId("CompoundTopModule", moduleView, false, jsonValueKeyMap.get(moduleTop.getBlockName()));
        }
    }

    /**
     * This creates the individual UI modules that make a PageView
     *
     * @param context              This is the context value that created UI components should use
     * @param appCMSPageUI         This is the Page UI response used for the creation of the UI
     * @param appCMSPageAPI        This is the Page API data used to populate the UI fields specified by appCMSPageUI
     * @param appCMSAndroidModules This contains the individual block module definitions used for creating each UI element
     * @param pageView             The PageView to use as the parent View Group for the created block modules
     * @param jsonValueKeyMap      This is a hashmap that associates UI string values with value enumerations
     * @param appCMSPresenter      This is a reference to the presenter class which handles all UI events including click events
     * @param modulesToIgnore      This is a list of block modules that ViewCreator should ignore
     */
    private void createPageView(Context context,
                                AppCMSPageUI appCMSPageUI,
                                AppCMSPageAPI appCMSPageAPI,
                                AppCMSAndroidModules appCMSAndroidModules,
                                PageView pageView,
                                Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                AppCMSPresenter appCMSPresenter,
                                List<String> modulesToIgnore) {
        appCMSPresenter.clearOnInternalEvents();
        pageView.clearExistingViewLists();
        List<ModuleList> modulesList = appCMSPageUI.getModuleList();
        ViewGroup childrenContainer = pageView.getChildrenContainer();
        boolean isTopModuleCreated = false;
        if (modulesList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                modulesList.removeIf(module -> modulesToIgnore.contains(module.getType()));
            }
            if (contentTypeChecker == null)
                contentTypeChecker = new ContentTypeChecker(context);

            List<ModuleList> appCMSPageUILocal = LocalModuleList.getModuleList(context);

            for (ModuleList moduleInfo : modulesList) {
                ModuleList module = null;
                if (moduleInfo.getBlockName() != null) {
                    try {
                        if (moduleInfo.getBlockName() != null
                                && moduleInfo.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_tray_01))
                                && moduleInfo.getSettings().getThumbnailType().equalsIgnoreCase(context.getString(R.string.app_cms_ui_block_tray_setting_thumbnail_type_landscape))) {
                            //module = appCMSAndroidModules.getModuleListMap().get(context.getString(R.string.ui_block_tray_04));
                            module = appCMSPageUILocal.get(3);
                        } else if (moduleInfo.getBlockName().contains("carousel01")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "carousel01.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("carousel04")) {
                            module = appCMSPageUILocal.get(27);
                        } else if (moduleInfo.getBlockName().contains("carousel08")) {
                            module = appCMSPageUILocal.get(26);
                        } else if (moduleInfo.getBlockName().contains("recommend") || moduleInfo.getBlockName().contains("userPersonalization01")) {
                            module = appCMSPageUILocal.get(24);
                        } else if (moduleInfo.getBlockName().contains("grid01")) {
                            module = appCMSPageUILocal.get(21);
                        } else if (moduleInfo.getBlockName().contains("grid02")) {
                            module = appCMSPageUILocal.get(22);
                        } else if (moduleInfo.getBlockName().contains("grid03")) {
                            module = appCMSPageUILocal.get(23);
                        } else if (moduleInfo.getBlockName().contains("audioTray")) {
                            module = appCMSPageUILocal.get(10);
                        } else if (moduleInfo.getBlockName().contains("continueWatching01")) {
                            module = appCMSPageUILocal.get(17);
                        } else if (moduleInfo.getBlockName().contains("continueWatching02")) {
                            module = appCMSPageUILocal.get(18);
                        } else if (moduleInfo.getBlockName().contains("continueWatching03")) {
                            module = appCMSPageUILocal.get(19);
                        } else if (moduleInfo.getBlockName().contains("tray01")) {
                            module = appCMSPageUILocal.get(0);
                        } else if (moduleInfo.getBlockName().contains("tray02")) {
                            module = appCMSPageUILocal.get(1);
                        } else if (moduleInfo.getBlockName().contains("tray03")) {
                            module = appCMSPageUILocal.get(2);
                        } else if (moduleInfo.getBlockName().contains("tray04")) {
                            module = appCMSPageUILocal.get(3);
                        } else if (moduleInfo.getBlockName().contains("articleTray01")) {
                            module = appCMSPageUILocal.get(9);
                        } else if (moduleInfo.getBlockName().contains("search01")) {
                            module = appCMSPageUILocal.get(14);
                        }  /*else if (moduleInfo.getBlockName().contains("standaloneVideoPlayer01")) {
                            module = appCMSPageUILocal.get(28);
                        }  *//*else if (moduleInfo.getBlockName() != null && moduleInfo.getBlockName().contains("tray12")) {
                        AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                loadJsonFromAssets(context, "tray12.json"),
                                AppCMSPageUI.class);
                        module = appCMSPageUI1.getModuleList().get(0);
                    } */ else if (moduleInfo.getBlockName() != null && moduleInfo.getBlockName().contains("grid03")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "grid03.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName() != null && moduleInfo.getBlockName().contains("newsRecommendation01")) {
                            if ((appCMSPageAPI != null && appCMSPageAPI.getTitle() != null && appCMSPageAPI.getTitle().toLowerCase().contains("show page"))) {

                            } else {
                                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                        loadJsonFromAssets(context, "news_recommendation.json"),
                                        AppCMSPageUI.class);
                                module = appCMSPageUI1.getModuleList().get(0);
                            }

                        } else if (moduleInfo.getBlockName().contains("downloads01")) {
                            module = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "rta_download.json"),
                                    ModuleList.class);
                        } else if (moduleInfo.getBlockName().contains("showDetail06")) {
                            module = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "showDetail06.json"),
                                    ModuleList.class);
                        }else if (moduleInfo.getBlockName().contains("tray11")) {
                            module = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "tray11.json"),
                                    ModuleList.class);
                        }else if (moduleInfo.getBlockName().contains("newsTray02")) {
                            module = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "newsTray02.json"),
                                    ModuleList.class);
                        }
                        else if (moduleInfo.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_tray_01))
                                && moduleInfo.getSettings().getThumbnailType().equalsIgnoreCase(context.getString(R.string.app_cms_ui_block_tray_setting_thumbnail_type_landscape))) {
                            module = appCMSAndroidModules.getModuleListMap().get(context.getString(R.string.ui_block_tray_04));
                        } else if (moduleInfo.getBlockName().contains("showDetail01") || moduleInfo.getBlockName().contains("showDetail03")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "showdetail_constraint.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("referralPlans")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "referralPlan.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("videoPlayerInfo02") || moduleInfo.getBlockName().contains("videoPlayerInfo07")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "video_player_info2.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(1);
                        } else if ((moduleInfo.getBlockName().contains("videoPlayerInfo01__TEMP__NO_DOWNLOADS") ||
                                moduleInfo.getBlockName().contains("videoPlayerInfo01")) &&
                                (appCMSPresenter.getAppCMSMain().getInternalName().equalsIgnoreCase("failarmy") ||
                                        appCMSPresenter.getAppCMSMain().getInternalName().equalsIgnoreCase("pet-collective") ||
                                        appCMSPresenter.getAppCMSMain().getInternalName().equalsIgnoreCase("people-awesome") ||
                                        appCMSPresenter.getAppCMSMain().getInternalName().equalsIgnoreCase("jukin-video"))) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "failarmy_videoplayerinfo.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("videoPlayerInfo01")) {
                            // if (appCMSPresenter.isHoichoiApp()) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "videoPlayerInfo01.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                            /* }else {
                                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                        loadJsonFromAssets(context, "video_page_timer.json"),
                                        AppCMSPageUI.class);
                                module = appCMSPageUI1.getModuleList().get(0);
                            }*/
                        } else if (moduleInfo.getBlockName().contains("selectPlan02")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "plan_page_new_ui.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("userManagement01") || moduleInfo.getBlockName().contains("userManagement04")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "account_setting1.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(2);
                        } else if (moduleInfo.getBlockName().contains("userManagement05")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "userManagement05.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("userManagement03")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "user_management03.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("userManagement02")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "user_management02.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(1);
                        } else if (moduleInfo.getBlockName().contains("paymentGateway01")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(loadJsonFromAssets(context, "juspaypament.json"), AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("bankList")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(loadJsonFromAssets(context, "bank_list.json"), AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("cardPayment01")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(loadJsonFromAssets(context, "card.json"), AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_01))) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(loadJsonFromAssets(context, "autoPlay01.json"), AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (appCMSPresenter.isHoichoiApp() && moduleInfo.getBlockName().contains("imageTextRow02")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(loadJsonFromAssets(context, "view_plans.json"), AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(1);
                        } else if (appCMSPresenter.isHoichoiApp() && moduleInfo.getBlockName().contains("selectPlan03")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(loadJsonFromAssets(context, "view_plans.json"), AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(2);

                        } else if (moduleInfo.getBlockName().contains("selectPlan06")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "mobileselectPlan06.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("standaloneVideoPlayer04")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "standaloneVideoPlayer04.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("showDetail08")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "showDetail08.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("videoPlayerInfo06")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "videoPlayerInfo06.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getBlockName().contains("eventCarousel01")) {
                            module = appCMSPageUILocal.get(25);
                        } else if (moduleInfo.getBlockName().contains("languageSettings01")) {
                            module = appCMSPageUILocal.get(30);
                        } else if (moduleInfo.getBlockName().contains("downloadSettings01")) {
                            module = appCMSPageUILocal.get(31);
                        } else if (moduleInfo.getBlockName().contains("downloads01")) {
                            module = appCMSPageUILocal.get(32);
                        } else if (moduleInfo.getBlockName().contains("bundleDetail01")) {
                            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                                    loadJsonFromAssets(context, "mobile_bundle.json"),
                                    AppCMSPageUI.class);
                            module = appCMSPageUI1.getModuleList().get(0);
                        } else if (moduleInfo.getSettings() != null &&
                                moduleInfo.getSettings().isHidden()) { // Done for Tampabay Top Module
                            if (isTopModuleCreated) {
                                continue;
                            } else {
                                createCompoundTopModule(context,
                                        modulesList,
                                        appCMSPageAPI,
                                        appCMSAndroidModules,
                                        pageView,
                                        jsonValueKeyMap,
                                        appCMSPresenter,
                                        modulesToIgnore);
                                isTopModuleCreated = true;
                            }
                        } else {
                            module = appCMSAndroidModules.getModuleListMap().get(moduleInfo.getBlockName());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (module == null) {
                        module = moduleInfo;
                    } else if (moduleInfo != null) {
                        module.setId(moduleInfo.getId());
                        module.setSettings(moduleInfo.getSettings());
                        module.setSvod(moduleInfo.isSvod());
                        module.setType(moduleInfo.getType());
                        module.setView(moduleInfo.getView());
                        module.setBlockName(moduleInfo.getBlockName());

                    }
                    if (moduleInfo.getBlockName().toLowerCase().contains("selectplan05")) {
                        module.setComponents(appCMSAndroidModules.getModuleListMap().get("selectPlan02").getComponents());
                        module.setBlockName("selectPlan02");
                        module.setType("AC SelectPlan 02");
                        module.setView("AC SelectPlan 02");
                    }
                    if (moduleInfo.getBlockName().toLowerCase().contains("categorydetail01")) {
                        module.setComponents(appCMSAndroidModules.getModuleListMap().get("categoryDetail02").getComponents());
                        module.setBlockName("categoryDetail02");
                        module.setType("AC CategoryDetail 02");
                        module.setView("AC CategoryDetail 02");
                        module.setSettings(moduleInfo.getSettings());
                    }

                    if (module.getBlockName().contains(context.getResources().getString(R.string.ui_block_grid_01))) {
                        module.getSettings().getColumns().setMobile(1);
                        module.getSettings().getColumns().setTablet(2);
                    }

                    boolean createModule = !modulesToIgnore.contains(module.getType());
                    Module moduleAPI = matchModuleAPIToModuleUI(module, appCMSPageAPI, jsonValueKeyMap, appCMSPresenter);
                    if (appCMSPageAPI != null && createModule && appCMSPresenter.isViewPlanPage(appCMSPageAPI.getId()) &&
                            (jsonValueKeyMap.get(module.getType()) == PAGE_CAROUSEL_MODULE_KEY ||
                                    jsonValueKeyMap.get(module.getType()) == PAGE_EVENT_CAROUSEL_MODULE_KEY ||
                                    jsonValueKeyMap.get(module.getType()) == PAGE_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(module.getType()) == PAGE_AUDIO_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(module.getType()) == PAGE_CAROUSEL_LANDSACPE_MODULE_KEY ||
                                    jsonValueKeyMap.get(module.getType()) == PAGE_VIDEO_PLAYER_MODULE_KEY ||
                                    jsonValueKeyMap.get(module.getType()) == PAGE_VIDEO_PLAYER_02_MODULE_KEY)) {
                        createModule = false;
                    }

                    if (jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_SEARCH_04 && appCMSPageAPI.getId() == null) {
                        appCMSPageAPI.setId(module.getId());
                    }
                    if (createModule) {

                        if (jsonValueKeyMap.get(module.getType()) == PAGE_VIDEO_PLAYLIST_MODULE_KEY) {
                            moduleAPI = appCMSPresenter.convertToVideoPlaylistAppCMSPageAPI(moduleAPI);
                        }
                        if (jsonValueKeyMap.get(module.getType()) == PAGE_SUBSCRIPTION_IMAGEROW_02_KEY) {
                            if (moduleAPI == null) {
                                moduleAPI = new Module();
                            }
                            moduleAPI.setId(module.getId());
                        }
                        boolean viewNotToCreate = false;
                        if (moduleAPI != null) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().isEmpty()) {
                                if (jsonValueKeyMap.get(module.getType()) == PAGE_SCHEDULE_CAROUSEL_MODULE_KEY ||
                                        jsonValueKeyMap.get(module.getType()) == PAGE_BRAND_TRAY_MODULE_KEY) {
                                    viewNotToCreate = true;
                                }
                            }

                            if (!viewNotToCreate) {
                                AppCMSUIKeyType viewType = jsonValueKeyMap.get(module.getView());
                                if (viewType == null) {
                                    viewType = PAGE_EMPTY_KEY;
                                }
                                try {
                                    ArrayList<ContentDatum> allUserHistoryforContinueWatching = appCMSPresenter.getAllUserHistory();
                                    if ((viewType == PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                                            viewType == PAGE_CONTINUE_WATCHING_02_MODULE_KEY) &&
                                            (allUserHistoryforContinueWatching != null &&
                                                    !allUserHistoryforContinueWatching.isEmpty())) {
                                        if (allUserHistoryforContinueWatching.size() > 1) {
                                            Collections.sort(allUserHistoryforContinueWatching, (o1, o2) -> {
                                                return Long.parseLong(o1.getAddedDate().toString()) > Long.parseLong(o2.getAddedDate().toString()) ? 1 : -1;
                                            });
                                        }
                                        moduleAPI.setContentData(allUserHistoryforContinueWatching);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                /*try {

                                    if (moduleAPI != null && (module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_recommendation_01)) ||
                                            module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_news_recommendation_01))) &&
                                            CommonUtils.isRecommendation(appCMSPresenter)) {
                                        ArrayList<ContentDatum> allUserRecommendation = appCMSPresenter.getRecommendationList();
                                        if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {

                                                if ((allUserRecommendation != null &&
                                                        !allUserRecommendation.isEmpty())) {
                                                    moduleAPI.setContentData(allUserRecommendation);
                                                    //Rendering
                                                    appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                            context.getResources().getString(R.string.recommend_video_category), "true");
                                                }
                                            }
                                        } else {
                                            if (allUserRecommendation != null &&
                                                    !allUserRecommendation.isEmpty() && allUserRecommendation.size() != 0) {
                                                moduleAPI.setContentData(allUserRecommendation);

                                                //Rendering
                                                appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                        context.getResources().getString(R.string.recommend_video_category), "true");
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {

                                    if (moduleAPI != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_recommendPopular_01)) &&
                                            CommonUtils.isRecommendation(appCMSPresenter)) {
                                        ArrayList<ContentDatum> allUserRecommendPopuler = appCMSPresenter.getRecommendationPopularList();
                                        if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {

                                                if ((allUserRecommendPopuler != null &&
                                                        !allUserRecommendPopuler.isEmpty())) {
                                                    if (allUserRecommendPopuler != null && allUserRecommendPopuler.size() != 0) {
                                                        moduleAPI.setContentData(allUserRecommendPopuler);
                                                    }
                                                    //Rendering
                                                    appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                            context.getResources().getString(R.string.recommend_video_category), "true");
                                                }
                                            }
                                        } else {

                                            if ((allUserRecommendPopuler != null &&
                                                    !allUserRecommendPopuler.isEmpty())) {
                                                moduleAPI.setContentData(allUserRecommendPopuler);
                                                //Rendering
                                                appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                        context.getResources().getString(R.string.recommend_video_category), "true");
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                */

                            }
                        }


                        View childView = createModuleView(context, module, moduleAPI,
                                appCMSAndroidModules,
                                pageView,
                                jsonValueKeyMap,
                                appCMSPresenter, module.isConstrainteView(), module.getSettings());

                        if (moduleAPI == null && childView != null) {
                            childView.setVisibility(View.GONE);
                        }
                        if (jsonValueKeyMap.get(module.getView()) == PAGE_SUBSCRIPTION_IMAGEROW_02_KEY ||
                                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_RESET_PASSWORD_01 ||
                                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_USER_MANAGEMENT_01 ||
                                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_USER_MANAGEMENT_05 ||
                                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_SEARCH_04 ||
                                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_BANK_LIST ||
                                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_CARD_PAYMENT_01) {
                            childView.setVisibility(View.VISIBLE);
                        }
                    }
                } //end of if(moduleInfo.getBlockName() != null)
            }
        }
        pageView.notifyAdapterDataSetChanged();

        List<OnInternalEvent> presenterOnInternalEvents = appCMSPresenter.getOnInternalEvents();
        if (presenterOnInternalEvents != null) {
            for (OnInternalEvent onInternalEvent : presenterOnInternalEvents) {
                for (OnInternalEvent receiverInternalEvent : presenterOnInternalEvents) {
                    if (receiverInternalEvent != onInternalEvent) {
                        if (!TextUtils.isEmpty(onInternalEvent.getModuleId()) &&
                                onInternalEvent.getModuleId().equals(receiverInternalEvent.getModuleId())) {
                            onInternalEvent.addReceiver(receiverInternalEvent);
                        }
                    }
                }
            }
        }
    }

    /**
     * This creates an individual Module View that is
     *
     * @param context              This is the context value that created UI components should use
     * @param module               This is UI definition for the module that should be created
     * @param moduleAPI            This is the API data that should be used to populate the UI fields
     * @param appCMSAndroidModules This contains the block modules definition
     * @param pageView             This is the PageView that should be used as the ViewGroup container for the created module
     * @param jsonValueKeyMap      This is a hashmap that associates UI string values with value enumerations
     * @param appCMSPresenter      This is a reference to the presenter class which handles all UI events including click events
     * @param constrainteView
     * @return Returns a ModuleView that will be added as a child view within the PageView ViewGroup
     */
    @SuppressWarnings("ConstantConditions")
    private <T extends ModuleWithComponents> View createModuleView(final Context context,
                                                                   final T module,
                                                                   final Module moduleAPI,
                                                                   AppCMSAndroidModules appCMSAndroidModules,
                                                                   PageView pageView,
                                                                   Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                                                   AppCMSPresenter appCMSPresenter,
                                                                   boolean constrainteView, Settings settings) {
        pageView.enableSwipe();
        ModuleView moduleView = null;
        appCMSPresenter.setModuleApi(moduleAPI);
        if (moduleAPI != null && module.isConstrainteView()
                && module.getBlockName().contains(context.getString(R.string.ui_block_recommendation_01))) {
            try {
                if (CommonUtils.isRecommendation(appCMSPresenter)) {
                    if (CommonUtils.recommendationTraysModule.get(module.getId()) != null
                            && CommonUtils.recommendationTraysData.get(module.getId()) != null) {
                        ModuleView moduleViewRecommended = CommonUtils.recommendationTraysModule.get(module.getId());
                        Module moduleAPIRecommendation = CommonUtils.recommendationTraysData.get(module.getId());
                        ((RecommendationModule) moduleViewRecommended).setModuleAPI(moduleAPIRecommendation);
                        ((RecommendationModule) moduleViewRecommended).initViewConstraint();
                        moduleViewRecommended.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                pageView.addModuleViewWithModuleId(module.getId(), moduleViewRecommended, false, jsonValueKeyMap.get(module.getBlockName()));
                            }
                        } else {
                            pageView.addModuleViewWithModuleId(module.getId(), moduleViewRecommended, false, jsonValueKeyMap.get(module.getBlockName()));
                        }

                    } else {

                        final Module finalModuleAPI = moduleAPI;
                        moduleView = new RecommendationModule(context,
                                module,
                                moduleAPI,
                                jsonValueKeyMap,
                                appCMSPresenter,
                                pageView,
                                this,
                                constraintViewCreator,
                                appCMSAndroidModules);
                        String contentType = "Video";
                        if (module.getSettings().getContentType() != null)
                            contentType = module.getSettings().getContentType();
                        if (moduleAPI.getFilters().getContentType() != null)
                            contentType = moduleAPI.getFilters().getContentType();
                        CommonUtils.recommendationTraysModule.put(module.getId(), moduleView);
                        appCMSPresenter.showLoadingDialog(true);
                        appCMSPresenter.populateRecommendedList(appCMSHistoryResult -> {
                                    appCMSPresenter.showLoadingDialog(false);
                                    if (appCMSHistoryResult != null) {
                                        ModuleView moduleViewRecommended = null;

                                        List<Record> recommendedRecords = appCMSHistoryResult.getRecords();
                                        ArrayList<ContentDatum> recommendationContents = new ArrayList<>();

                                        for (Record record : recommendedRecords) {
                                            ContentDatum recordContentDatum = record.recommendationToContentDatum();
                                            recommendationContents.add(recordContentDatum);
                                        }

                                        if (recommendationContents != null &&
                                                !recommendationContents.isEmpty() && recommendationContents.size() > 0) {
                                            moduleAPI.setContentData(recommendationContents);

                                            if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                                                if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                                    try {

                                                        if (moduleAPI != null
                                                                && moduleAPI.getContentData() != null
                                                                && moduleAPI.getContentData().size() > 0) {
                                                            moduleViewRecommended = CommonUtils.recommendationTraysModule.get(appCMSHistoryResult.getModuleId());
                                                            CommonUtils.recommendationTraysData.put(appCMSHistoryResult.getModuleId(), moduleAPI);
                                                            ((RecommendationModule) moduleViewRecommended).setModuleAPI(moduleAPI);
                                                            ((RecommendationModule) moduleViewRecommended).initViewConstraint();
                                                            moduleViewRecommended.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    //Rendering
                                                    appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                            context.getResources().getString(R.string.recommend_video_category), "true");
                                                }
                                            } else {
                                                try {

                                                    if (moduleAPI != null
                                                            && moduleAPI.getContentData() != null
                                                            && moduleAPI.getContentData().size() > 0) {
                                                        moduleViewRecommended = CommonUtils.recommendationTraysModule.get(appCMSHistoryResult.getModuleId());
                                                        CommonUtils.recommendationTraysData.put(appCMSHistoryResult.getModuleId(), moduleAPI);
                                                        ((RecommendationModule) moduleViewRecommended).setModuleAPI(moduleAPI);
                                                        ((RecommendationModule) moduleViewRecommended).initViewConstraint();
                                                        moduleViewRecommended.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //Rendering
                                                appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                        context.getResources().getString(R.string.recommend_video_category), "true");
                                            }


                                        }

                                    }//end of if (appCMSHistoryResult != null )

                                },
                                module.getSettings().getRecommendTrayType(),
                                contentType,
                                module.getId());
                        if (moduleAPI != null
                                && moduleAPI.getContentData() != null
                                && moduleAPI.getContentData().size() > 0) {
                            moduleView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        } else {
                            moduleView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
                        }

                        pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (moduleAPI != null && module.isConstrainteView()
                && module.getBlockName().contains(context.getString(R.string.ui_block_userPersonalization_01))) {
            try {
                String userGenres = appCMSPresenter.getPersonalizedGenresPreference();
                if (appCMSPresenter.isPersonalizationEnabled()
                        && appCMSPresenter.isUserLoggedIn()
                        && userGenres != null) {
                    if (CommonUtils.personalizationTraysModule.get(module.getId()) != null
                            && CommonUtils.personalizationTraysData.get(module.getId()) != null
                    ) {
                        ModuleView moduleViewPersonalizatio = CommonUtils.personalizationTraysModule.get(module.getId());
                        //AppCMSRecommendationGenreResult appCMSRecommendationGenreResult = CommonUtils.personalizationTraysData.get(module.getId());
                        //((PersonalizatioModule) moduleViewRecommended).setModuleAPI(moduleAPI);
                        //((PersonalizatioModule) moduleViewRecommended).setAppCMSRecommendationGenreResult(appCMSRecommendationGenreResult);
                        //((PersonalizatioModule) moduleViewRecommended).initViewConstraint();
                        //moduleViewRecommended.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                pageView.addModuleViewWithModuleId(module.getId(), moduleViewPersonalizatio, false, jsonValueKeyMap.get(module.getBlockName()));
                            }
                        } else {
                            pageView.addModuleViewWithModuleId(module.getId(), moduleViewPersonalizatio, false, jsonValueKeyMap.get(module.getBlockName()));
                        }

                    } else {

                        final Module finalModuleAPI = moduleAPI;
                        moduleView = new PersonalizatioModule(context,
                                module,
                                moduleAPI,
                                jsonValueKeyMap,
                                appCMSPresenter,
                                pageView,
                                this,
                                constraintViewCreator,
                                appCMSAndroidModules);

                        String[] genres = userGenres.split("\\|");
                        System.out.println("genres: " + userGenres + ",  moduleid:- " + module.getId());
                        CommonUtils.personalizationTraysModule.put(module.getId(), moduleView);
                        appCMSPresenter.showLoadingDialog(true);
                        appCMSPresenter.getRecommendedGenreContent(
                                module.getSettings().getRecommendTrayType(),
                                module.getSettings().getContentType(),
                                module.getId(),
                                genres,
                                appCMSRecommendationGenreResult -> {
                                    appCMSPresenter.showLoadingDialog(false);
                                    if (appCMSRecommendationGenreResult != null
                                            && appCMSRecommendationGenreResult.getRecords() != null
                                            && appCMSRecommendationGenreResult.getRecords().size() > 0) {
                                        System.out.println("appCMSRecommendationGenreResult.getRecords().size() :- " + appCMSRecommendationGenreResult.getRecords().size());
                                        CommonUtils.personalizationTraysData.put(appCMSRecommendationGenreResult.getModuleId(), appCMSRecommendationGenreResult);
                                        ModuleView moduleViewRecommended = null;
                                        if (appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                                try {
                                                    moduleViewRecommended = CommonUtils.personalizationTraysModule.get(appCMSRecommendationGenreResult.getModuleId());
                                                    ((PersonalizatioModule) moduleViewRecommended).setModuleAPI(moduleAPI);
                                                    ((PersonalizatioModule) moduleViewRecommended).setAppCMSRecommendationGenreResult(appCMSRecommendationGenreResult);
                                                    ((PersonalizatioModule) moduleViewRecommended).initViewConstraint();
                                                    moduleViewRecommended.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //Rendering Events in Analytic
                                                appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                        context.getResources().getString(R.string.personalization_video_category), "true");
                                            }
                                        } else {
                                            try {
                                                moduleViewRecommended = CommonUtils.personalizationTraysModule.get(appCMSRecommendationGenreResult.getModuleId());
                                                ((PersonalizatioModule) moduleViewRecommended).setModuleAPI(moduleAPI);
                                                ((PersonalizatioModule) moduleViewRecommended).setAppCMSRecommendationGenreResult(appCMSRecommendationGenreResult);
                                                ((PersonalizatioModule) moduleViewRecommended).initViewConstraint();
                                                moduleViewRecommended.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            //Rendering Events in Analytic
                                            appCMSPresenter.sendGaEvent(context.getResources().getString(R.string.play_video_action),
                                                    context.getResources().getString(R.string.personalization_video_category), "true");
                                        }
                                    }

                                });

                        if (moduleAPI != null
                                && moduleAPI.getContentData() != null
                                && moduleAPI.getContentData().size() > 0) {
                            moduleView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        } else {
                            moduleView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
                        }

                        pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (CommonUtils.isTrayModule(module.getBlockName())) {
            moduleView = new TrayModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    pageView,
                    this,
                    constraintViewCreator,
                    appCMSAndroidModules);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }

        } else if (jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_AUTHENTICATION_05 ||
                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_PROFILE_02 ||
                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_PROFILE_03 ||
                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_RESET_PASSWORD_01 ||
                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_PAYMENT_01 ||
                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_BANK_LIST ||
                jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_CARD_PAYMENT_01 ||
                module.isConstrainteView()) {
            if (moduleAPI != null && moduleAPI.getContentData() != null
                    && moduleAPI.getContentData().size() == 0
                    && !module.getBlockName().toLowerCase().contains("history")
                    && jsonValueKeyMap.get(module.getBlockName()) != UI_BLOCK_PAYMENT_01
                    && jsonValueKeyMap.get(module.getBlockName()) != UI_BLOCK_BANK_LIST
                    && jsonValueKeyMap.get(module.getBlockName()) != UI_BLOCK_CARD_PAYMENT_01) {
                return null;
            }
            if (module.getSettings() != null &&
                    module.getSettings().isUse16x9OnMobile() &&
                    module.getComponents() != null && !module.getComponents().isEmpty()) {
                module.getComponents().get(0).getComponents().get(0).getLayout().getMobile().setRatio("16:9");
                module.getComponents().get(0).getComponents().get(1).getLayout().getMobile().setRatio("16:9");
                module.getComponents().get(0).getComponents().get(2).getLayout().getMobile().setRatio("16:9");
            }
            moduleView = new ConstraintModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    pageView, constraintViewCreator, appCMSAndroidModules);
            ViewGroup childrenContainer = moduleView.getChildrenContainer();
            if (jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_AUTHENTICATION_05 ||
                    jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_RESET_PASSWORD_01 ||
                    jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_SELECTPLAN_02) {
                childrenContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
            }
            if (jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_SEARCH_04
                    || jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_SELECTPLAN_06) {
                pageView.disableSwipe();
            }
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
            if (module.getBlockName().contains(context.getString(R.string.ui_block_recommendation_01))
                    || module.getBlockName().contains(context.getString(R.string.ui_block_recommendPopular_01))
                    || module.getBlockName().contains(context.getString(R.string.ui_block_continueWatching_01))
                    || module.getBlockName().contains(context.getString(R.string.ui_block_continueWatching_02))
                    || module.getBlockName().contains(context.getString(R.string.ui_block_news_recommendation_01))) {
                if (moduleAPI != null
                        && moduleAPI.getContentData() != null
                        && moduleAPI.getContentData().size() > 0) {
                    pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
                }
            } else {
                pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            }


            if (module.getBlockName() != null && jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_CONTENT_BLOCK_01) {
                moduleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getButtonLink() != null) {

                            Log.e("jumpWithUsbuttonlink", moduleAPI.getMetadataMap().getButtonLink());
                            appCMSPresenter.openChromeTab(moduleAPI.getMetadataMap().getButtonLink());
                        }
                    }
                });
            }
        } else if ((jsonValueKeyMap.get(module.getView()) == PAGE_AUTHENTICATION_MODULE_KEY &&
                jsonValueKeyMap.get(module.getBlockName()) != UI_BLOCK_AUTHENTICATION_05) ||
                (module.getView() != null && module.getView().contains("Authentication"))) {
            if (appCMSPresenter.getTrailerPlayerView() != null)
                appCMSPresenter.getTrailerPlayerView().stopPlayer();
            moduleView = new LoginModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    this,
                    appCMSAndroidModules);
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
        } else if (((module.getBlockName() != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_01))) ||
                (module.getBlockName() != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_02))) ||
                (module.getBlockName() != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_03))) ||
                (module.getBlockName() != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_04))) ||
                (module.getBlockName() != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04))) || // uncomment for ML-883
                //(module.getBlockName() != null && module.getBlockName().equalsIgnoreCase(context.getString(R.string.app_cms_ui_block_carousel_01))) ||
                (module.isConstrainteView()) ||
                //Todo bellow should not be used untill all show details are coverted for Constraint
                //(module.getBlockName() != null && module.getBlockName().contains("showDetail")) ||
                //(module.getBlockName() != null && module.getBlockName().contains("videoPlayerInfo01")) ||
                jsonValueKeyMap.get(module.getView()) == PAGE_DOWNLOAD_SETTING_MODULE_KEY ||
                jsonValueKeyMap.get(module.getView()) == PAGE_LANGUAGE_SETTING_MODULE_KEY)) {
            if (moduleAPI != null && moduleAPI.getContentData() != null &&
                    moduleAPI.getContentData().size() == 0) {
                return null;
            }
            if (module.getLayout() != null) {
                module.getLayout().getMobile().setHeight(0);
                module.getLayout().getTabletLandscape().setHeight(0);
                module.getLayout().getTabletPortrait().setHeight(0);
            }
            if (module.getSettings() != null &&
                    module.getSettings().isUse16x9OnMobile() &&
                    module.getComponents() != null && !module.getComponents().isEmpty()) {
                module.getComponents().get(0).getComponents().get(0).getLayout().getMobile().setRatio("16:9");
                module.getComponents().get(0).getComponents().get(1).getLayout().getMobile().setRatio("16:9");
                module.getComponents().get(0).getComponents().get(2).getLayout().getMobile().setRatio("16:9");
            }
            moduleView = new ConstraintModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    pageView,
                    constraintViewCreator,
                    appCMSAndroidModules);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } /*else if (module.getBlockName().contains(context.getString(R.string.ui_block_standalone_player_01))){
            moduleView = new StandAlonPlayerModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    pageView,
                    constraintViewCreator,
                    appCMSAndroidModules);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } */ else if (jsonValueKeyMap.get(module.getView()) == PAGE_VIDEO_DETAIL_INFO_04_MODULE_KEY) {
            moduleView = new VideoPlayerInfo04(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView, constraintViewCreator);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }

        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_PERSON_DETAIL_03_MODULE_KEY) {
            moduleView = new PersonDetailModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView, constraintViewCreator);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
            }
        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_SHOW_DETAIL_04_MODULE_KEY) {

            moduleView = new ConceptDetailModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView);
            //pageView.reorderViews(moduleView);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);

            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_SEASON_TRAY_MODULE_KEY) {
            if (moduleAPI.getContentData() != null
                    && moduleAPI.getContentData().size() > 0
                    && moduleAPI.getContentData().get(0).getSeason().size() == 0)
                return null;
            if (moduleAPI.getContentData() != null
                    && moduleAPI.getContentData().size() > 0
                    && moduleAPI.getContentData().get(0).getSeason().size() == 1 &&
                    moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() == 0) {
                return null;
            }
            moduleView = new SeasonModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);

//            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,300));
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_EVENT_DETAIL_MODULE_KEY) {
            moduleView = new EventModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }

        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_AC_ROSTER_MODULE_KEY) {

            moduleView = new MultiTableWithSameItemsModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_AC_SEARCH_MODULE_KEY ||
                jsonValueKeyMap.get(module.getView()) == PAGE_AC_SEARCH02_MODULE_KEY ||
                jsonValueKeyMap.get(module.getView()) == PAGE_AC_SEARCH03_MODULE_KEY) {

            moduleView = new SearchViewModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    constraintViewCreator,
                    appCMSAndroidModules, pageView);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } else if (jsonValueKeyMap.get(module.getBlockName()) == UI_BLOCK_FILTER_02) {
            moduleView = new BrowseViewModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSAndroidModules, pageView);

            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }
        } else if (jsonValueKeyMap.get(module.getView()) == PAGE_LIBRARY_01_MODULE_KEY || jsonValueKeyMap.get(module.getView()) == PAGE_LIBRARY_02_MODULE_KEY) {

            moduleView = new SearchViewModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    constraintViewCreator,
                    appCMSAndroidModules, pageView);
            pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
            }

        } else {
            if (module.getComponents() != null) {
                moduleView = new ModuleView<>(context, module, true);
                ViewGroup childrenContainer = moduleView.getChildrenContainer();
                boolean hideModule = false;
                boolean modulesHasHiddenComponent = false;

                AdjustOtherState adjustOthers = AdjustOtherState.IGNORE;
                if (module.getSettings() != null && !module.getSettings().isHidden()) {
                    pageView.addModuleViewWithModuleId(module.getId(), moduleView, false, jsonValueKeyMap.get(module.getBlockName()));
                }
                if (module.getComponents() != null) {
                    if (moduleAPI != null) {
                        updateUserHistory(appCMSPresenter,
                                moduleAPI.getContentData());

                    }

                    int size = module.getComponents().size();
                    for (int i = 0; i < size; i++) {
                        Component component = module.getComponents().get(i);

//                        String viewType="";
//                        if( TextUtils.isEmpty(module.getView())){
//                            viewType=module.getType();
//                        }
                        try {
                            if (component.getView() != null && component.getView().equalsIgnoreCase(context.getString(R.string.app_cms_page_season_tray_module_key))) {
                                component.setBlockName(module.getBlockName());
                            }
                            if (component.getView() != null && component.getView().equalsIgnoreCase(context.getString(R.string.app_cms_page_tray_bundle_module_key))
                                    && CommonUtils.isBundleDataWithout3x4Image(moduleAPI)) {
                                ModuleList moduleTray02 = appCMSAndroidModules.getModuleListMap().get("tray02");
                                component.getComponents().remove(2);
                                component.getComponents().add(moduleTray02.getComponents().get(2));
                            }
                            createComponentView(context,
                                    component,
                                    module.getLayout(),
                                    moduleAPI,
                                    appCMSAndroidModules,
                                    pageView,
                                    settings,
                                    jsonValueKeyMap,
                                    appCMSPresenter,
                                    false,
                                    module.getView(),
                                    module.getId(),
                                    module.getBlockName(),
                                    constrainteView);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                        if (adjustOthers == AdjustOtherState.INITIATED) {
                            adjustOthers = AdjustOtherState.ADJUST_OTHERS;
                        }

                        if (!appCMSPresenter.isAppSVOD() && component.isSvod() && componentViewResult.componentView != null) {
                            componentViewResult.shouldHideComponent = true;
                            if (componentViewResult.componentView != null) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                            adjustOthers = AdjustOtherState.INITIATED;
                        } else if (/*!appCMSPresenter.isAppSVOD() &&*/ jsonValueKeyMap.get(component.getKey()) != null
                                && jsonValueKeyMap.get(component.getKey()) == PAGE_USER_MANAGEMENT_DOWNLOADS_MODULE_KEY
                                && appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                                !appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads() && componentViewResult.componentView != null) {
                            componentViewResult.shouldHideComponent = true;
                            if (componentViewResult.componentView != null) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                            adjustOthers = AdjustOtherState.INITIATED;
                        }

                        if (componentViewResult.shouldHideModule) {
                            hideModule = true;
                        }

                        if (componentViewResult.onInternalEvent != null) {
                            appCMSPresenter.addInternalEvent(componentViewResult.onInternalEvent);
                        }

                        if (componentViewResult.shouldHideComponent) {
                            ModuleView.HeightLayoutAdjuster heightLayoutAdjuster =
                                    new ModuleView.HeightLayoutAdjuster();
                            modulesHasHiddenComponent = true;
                            if (BaseView.isTablet(context)) {
                                if (BaseView.isLandscape(context)) {
                                    heightLayoutAdjuster.heightAdjustment =
                                            (int) (component.getLayout().getTabletLandscape().getHeight() * 0.6);
                                    heightLayoutAdjuster.topMargin =
                                            (int) component.getLayout().getTabletLandscape().getTopMargin();
                                    heightLayoutAdjuster.yAxis =
                                            (int) component.getLayout().getTabletLandscape().getYAxis();
                                    heightLayoutAdjuster.component = component;
                                } else {
                                    heightLayoutAdjuster.heightAdjustment =
                                            (int) (component.getLayout().getTabletPortrait().getHeight() * 0.8);
                                    heightLayoutAdjuster.topMargin =
                                            (int) component.getLayout().getTabletPortrait().getTopMargin();
                                    heightLayoutAdjuster.yAxis =
                                            (int) component.getLayout().getTabletPortrait().getYAxis();
                                    heightLayoutAdjuster.component = component;
                                }
                            } else {
                                heightLayoutAdjuster.heightAdjustment =
                                        (int) (component.getLayout().getMobile().getHeight() * 0.6);
                                heightLayoutAdjuster.topMargin =
                                        (int) component.getLayout().getMobile().getTopMargin();
                                heightLayoutAdjuster.yAxis =
                                        (int) component.getLayout().getMobile().getYAxis();
                                heightLayoutAdjuster.component = component;
                            }
                            moduleView.addHeightAdjuster(heightLayoutAdjuster);
                        }

                        View componentView = componentViewResult.componentView;

                        if (componentView != null) {
                            if (componentViewResult.addToPageView) {
                                pageView.addView(componentView);
                            } else {
                                if (componentView.getParent() != null)
                                    ((ViewGroup) componentView.getParent()).removeView(componentView);

                                if (component.isHeaderView()
                                        && !module.getBlockName().contains("richText")) {
                                    pageView.addToHeaderView(componentView);
                                } else {
                                    childrenContainer.addView(componentView);
                                }
                                moduleView.setComponentHasView(i, true);
                                moduleView.setViewMarginsFromComponent(component,
                                        componentView,
                                        moduleView.getLayout(),
                                        childrenContainer,
                                        false,
                                        jsonValueKeyMap,
                                        componentViewResult.useMarginsAsPercentagesOverride,
                                        componentViewResult.useWidthOfScreen,
                                        module.getView(), module.getBlockName());
                                if ((adjustOthers == AdjustOtherState.IGNORE &&
                                        componentViewResult.shouldHideComponent) ||
                                        adjustOthers == AdjustOtherState.ADJUST_OTHERS) {
                                    moduleView.addChildComponentAndView(component, componentView);
                                } else {
                                    moduleView.setComponentHasView(i, false);
                                }
                            }
                        }
                    }
                }

                if (hideModule) {
                    moduleView.setVisibility(View.GONE);
                }

                if (modulesHasHiddenComponent) {
                    moduleView.verifyHeightAdjustments();
                    ViewGroup.LayoutParams moduleLayoutParams = moduleView.getLayoutParams();
                    for (int i = 0; i < moduleView.getHeightAdjusterListSize(); i++) {
                        ModuleView.HeightLayoutAdjuster heightLayoutAdjuster = moduleView.getHeightLayoutAdjuster(i);

                        moduleLayoutParams.height -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                        List childComponentAndViewList = moduleView.getChildComponentAndViewList();

                        int componentViewListSize = childComponentAndViewList.size();
                        for (int j = 0; j < componentViewListSize; j++) {
                            ModuleView.ChildComponentAndView childComponentAndView = (ModuleView.ChildComponentAndView) childComponentAndViewList.get(j);

                            ViewGroup.MarginLayoutParams childLayoutParams =
                                    (ViewGroup.MarginLayoutParams) childComponentAndView.childView.getLayoutParams();
                            if (BaseView.isTablet(context)) {
                                if (BaseView.isLandscape(context)) {
                                    if (childComponentAndView.component.getLayout().getTabletLandscape().getYAxis() > 0 &&
                                            heightLayoutAdjuster.yAxis <
                                                    childComponentAndView.component.getLayout().getTabletLandscape().getYAxis()) {
                                        childLayoutParams.topMargin -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                                    } else if (childComponentAndView.component.getLayout().getTabletLandscape().getTopMargin() > 0 &&
                                            heightLayoutAdjuster.topMargin <
                                                    childComponentAndView.component.getLayout().getTabletLandscape().getTopMargin()) {
                                        childLayoutParams.topMargin -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                                    }
                                } else {
                                    if (childComponentAndView.component.getLayout().getTabletPortrait().getYAxis() > 0 &&
                                            heightLayoutAdjuster.yAxis <
                                                    childComponentAndView.component.getLayout().getTabletPortrait().getYAxis()) {
                                        childLayoutParams.topMargin -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                                    } else if (childComponentAndView.component.getLayout().getTabletPortrait().getTopMargin() > 0 &&
                                            heightLayoutAdjuster.topMargin <
                                                    childComponentAndView.component.getLayout().getTabletPortrait().getTopMargin()) {
                                        childLayoutParams.topMargin -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                                    }
                                }
                            } else {
                                if (childComponentAndView.component.getLayout().getMobile().getYAxis() > 0 &&
                                        heightLayoutAdjuster.yAxis <
                                                childComponentAndView.component.getLayout().getMobile().getYAxis()) {
                                    childLayoutParams.topMargin -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                                } else if (childComponentAndView.component.getLayout().getMobile().getTopMargin() > 0 &&
                                        heightLayoutAdjuster.topMargin <
                                                childComponentAndView.component.getLayout().getMobile().getTopMargin()) {
                                    childLayoutParams.topMargin -= BaseView.convertDpToPixel(heightLayoutAdjuster.heightAdjustment, context);
                                }
                            }
                            childComponentAndView.childView.setLayoutParams(childLayoutParams);
                        }
                    }
                    moduleView.setLayoutParams(moduleLayoutParams);
                }
            }
        }
        if (moduleView != null) {
            moduleView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        }
        return moduleView;
    }

    private void updateUserHistory(AppCMSPresenter appCMSPresenter,
                                   List<ContentDatum> contentData) {
        try {
            int contentDatumLength = contentData.size();
            for (int i = 0; i < contentDatumLength; i++) {
                ContentDatum currentContentDatum = contentData.get(i);
                ContentDatum userHistoryContentDatum = appCMSPresenter.getUserHistoryContentDatum(contentData.get(i).getGist().getId());
                if (userHistoryContentDatum != null) {
                    currentContentDatum.getGist().setWatchedTime(userHistoryContentDatum.getGist().getWatchedTime());
                }
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * This method is used by Adapters for creating a component view.
     *
     * @param context                             This is the context value that created UI components should use
     * @param parentLayout                        This is the UI JSON layout object used by the parent ViewGroup
     * @param useParentLayout                     This flag should be set to true if this component should use the parent layout
     * @param component                           This is UI JSON component object that contains the information from AppCMS needed to create the component
     * @param appCMSPresenter                     This is the Presenter class used for handling all UI events including click events
     * @param moduleAPI                           This is the API data for the module that contains the data used for this component
     * @param appCMSAndroidModules                This is the list of the Android block modules definitions used for creating each UI block type
     * @param settings                            This is the settings object from the module object that contains this object
     * @param jsonValueKeyMap                     This is a hashmap that associates UI string values with value enumerations
     * @param defaultWidth                        This is a default width for the component if the view width is not specified in the UI JSON response
     * @param defaultHeight                       This is a default height for the component if the view height is not specified in the UI JSON response
     * @param useMarginsAsPercentages             This flag should be set to true if this component should interpret the margin as percentages instead of pixel values
     * @param gridElement                         This flag should be set to true if this component is a grid component and its values from come an array of values instead of a single element
     * @param viewType                            This is the component view type of the parent view and is used to infer additional view properties
     * @param createMultipleContainersForChildren This flag should be set to true if the parent view has multiple viewgroups as child layouts that a component may be placed in
     * @param createRoundedCorners                This flag should be set to true if this component should have rounded corners (e.g. round corners in a CardView)
     * @return Returns a CollectionGridItemView that may be bound to a view in a view adapter
     */
    public CollectionGridItemView createCollectionGridItemView(final Context context,
                                                               final Layout parentLayout,
                                                               final boolean useParentLayout,
                                                               final Component component,
                                                               final AppCMSPresenter appCMSPresenter,
                                                               final Module moduleAPI,
                                                               final AppCMSAndroidModules appCMSAndroidModules,
                                                               Settings settings,
                                                               Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                                               int defaultWidth,
                                                               int defaultHeight,
                                                               boolean useMarginsAsPercentages,
                                                               boolean gridElement,
                                                               String viewType,
                                                               boolean createMultipleContainersForChildren,
                                                               boolean createRoundedCorners,
                                                               AppCMSUIKeyType viewTypeKey, String blockName) {
        CollectionGridItemView collectionGridItemView = new CollectionGridItemView(context,
                parentLayout,
                useParentLayout,
                component,
                moduleAPI.getId(),
                defaultWidth,
                defaultHeight,
                createMultipleContainersForChildren,
                createRoundedCorners,
                viewTypeKey);

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();

        int size = component.getComponents().size();
        for (int i = 0; i < size; i++) {
            Component childComponent = component.getComponents().get(i);
            createComponentView(context,
                    childComponent,
                    parentLayout,
                    moduleAPI,
                    appCMSAndroidModules,
                    null,
                    settings,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    gridElement,
                    viewType,
                    component.getId(),
                    blockName,
                    childComponent.isConstrainteView());

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }
            if (viewType != null &&
                    viewType.equalsIgnoreCase(PAGE_EVENT_CAROUSEL_MODULE_KEY.toString())) {
                childComponent.setView(viewType);
            }

            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                CollectionGridItemView.ItemContainer itemContainer =
                        new CollectionGridItemView.ItemContainer.Builder()
                                .childView(componentView)
                                .component(childComponent)
                                .build();
                collectionGridItemView.addChild(itemContainer);
                collectionGridItemView.setComponentHasView(i, true);
                collectionGridItemView.setViewMarginsFromComponent(childComponent,
                        componentView,
                        collectionGridItemView.getLayout(),
                        collectionGridItemView.getChildrenContainer(),
                        false,
                        jsonValueKeyMap,
                        useMarginsAsPercentages,
                        componentViewResult.useWidthOfScreen,
                        viewType, blockName);
            } else {
                collectionGridItemView.setComponentHasView(i, false);
            }
        }

        return collectionGridItemView;
    }

    public TrayCollectionGridItemView createTrayCollectionGridItemView(final Context context,
                                                                       final Layout parentLayout,
                                                                       final boolean useParentLayout,
                                                                       final Component component,
                                                                       final AppCMSPresenter appCMSPresenter,
                                                                       final Module moduleAPI,
                                                                       final AppCMSAndroidModules appCMSAndroidModules,
                                                                       Settings settings,
                                                                       Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                                                       int defaultWidth,
                                                                       int defaultHeight,
                                                                       boolean useMarginsAsPercentages,
                                                                       boolean gridElement,
                                                                       String viewType,
                                                                       boolean createMultipleContainersForChildren,
                                                                       boolean createRoundedCorners,
                                                                       AppCMSUIKeyType viewTypeKey,
                                                                       String blockName) {
        TrayCollectionGridItemView collectionGridItemView = new TrayCollectionGridItemView(context,
                parentLayout,
                useParentLayout,
                component,
                moduleAPI.getId(),
                defaultWidth,
                defaultHeight,
                createMultipleContainersForChildren,
                createRoundedCorners,
                viewTypeKey,
                blockName);

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();

        int size = component.getComponents().size();
        for (int i = 0; i < size; i++) {
            Component childComponent = component.getComponents().get(i);
            createComponentView(context,
                    childComponent,
                    parentLayout,
                    moduleAPI,
                    appCMSAndroidModules,
                    null,
                    settings,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    gridElement,
                    viewType,
                    component.getId(),
                    blockName,
                    childComponent.isConstrainteView());

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }
            if (viewType != null &&
                    viewType.equalsIgnoreCase(PAGE_EVENT_CAROUSEL_MODULE_KEY.toString())) {
                childComponent.setView(viewType);
            }

            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                TrayCollectionGridItemView.ItemContainer itemContainer =
                        new TrayCollectionGridItemView.ItemContainer.Builder()
                                .childView(componentView)
                                .component(childComponent)
                                .build();
                collectionGridItemView.addChild(itemContainer);
                if (CommonUtils.isFrameTray(blockName)) {
                    collectionGridItemView.setViewMarginsFromComponent(childComponent,
                            componentView,
                            childComponent.getLayout(),
                            collectionGridItemView.getFrameChildContainer(),
                            false,
                            jsonValueKeyMap,
                            useMarginsAsPercentages,
                            componentViewResult.useWidthOfScreen,
                            viewType, blockName);
                }

            }
        }

        return collectionGridItemView;
    }

    /**
     * This method is used to create an individual component view, which may by a recycler view,
     * text view, button, carousel_gradient view, etc.  The result is stored in the componentViewResult member object.
     *
     * @param context              This is the context value that created UI components should use
     * @param component            This is UI JSON component object that contains the information from AppCMS needed to create the component
     * @param parentLayout         This is the UI JSON layout object used by the parent ViewGroup
     * @param moduleAPI            This is the API data for the module that contains the data used for this component
     * @param appCMSAndroidModules This is the list of the Android block modules definitions used for creating each UI block type
     * @param pageView             This is view used by the entire page and is used as a parent viewgroup for specific components that may be positioned anywhere on the page
     * @param settings             This is the settings object from the module object that contains this object
     * @param jsonValueKeyMap      This is a hashmap that associates UI string values with value enumerations
     * @param appCMSPresenter      This is the Presenter class used for handling all UI events including click events
     * @param gridElement          This flag should be set to true if this component is a grid component and its values from come an array of values instead of a single element
     * @param viewType             This is the component view type of the parent view and is used to infer additional view properties
     * @param moduleId             This is the module ID that associates
     * @param constrainteView
     */
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @SuppressWarnings({"StringBufferReplaceableByString", "ConstantConditions"})
    public void createComponentView(final Context context,
                                    final Component component,
                                    final Layout parentLayout,
                                    final Module moduleAPI,
                                    final AppCMSAndroidModules appCMSAndroidModules,
                                    @Nullable final PageView pageView,
                                    final Settings settings,
                                    Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                    final AppCMSPresenter appCMSPresenter,
                                    boolean gridElement,
                                    final String viewType,
                                    String moduleId,
                                    String blockName,
                                    boolean constrainteView) {
        componentViewResult.componentView = null;
        componentViewResult.useMarginsAsPercentagesOverride = true;
        componentViewResult.useWidthOfScreen = false;
        componentViewResult.shouldHideModule = false;
        componentViewResult.addToPageView = false;
        componentViewResult.shouldHideComponent = false;
        componentViewResult.onInternalEvent = null;

        AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());

        if (componentType == null) {
            componentType = PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType uiBlockName = jsonValueKeyMap.get(blockName);
        if (uiBlockName != null && uiBlockName == UI_BLOCK_PAGE_LINK_MODULE) {
            appCMSPresenter.setstyle(settings);
        }

        if (uiBlockName == null) {
            uiBlockName = PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());

        if (componentKey == null) {
            componentKey = PAGE_EMPTY_KEY;
        }

        if (moduleId == null && moduleAPI != null) {
            moduleId = moduleAPI.getId();
        }

        AppCMSUIKeyType moduleType = jsonValueKeyMap.get(viewType);

        if (moduleType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }

        if (moduleType == PAGE_SEASON_TRAY_MODULE_KEY ||
                moduleType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
            componentViewResult.useMarginsAsPercentagesOverride = false;
        }

        AppCMSUIKeyType parentViewType = jsonValueKeyMap.get(viewType);

        if (parentViewType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }
        MetadataMap metadataMap = null;
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
            metadataMap = moduleAPI.getMetadataMap();
        int tintColor = appCMSPresenter.getBrandPrimaryCtaColor();

        switch (componentType) {
            case PAGE_COUNTDOWN_TIMER_VIEW: {

                TextView titleText = new TextView(context);
                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&

                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null) {

                    String message = appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS ?
                            appCMSPresenter.getLocalisedStrings().getFaceOffText() :
                            appCMSPresenter.getLocalisedStrings().getContentAvailableText();
                    titleText.setText(message);
                    titleText.setId(R.id.timer_until_face_off);
                } else if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getPricing() != null &&
                        moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0
                ) {

                    boolean isTimerShow = true;
                    if ((moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {
                        isTimerShow = true;
                    } else if (((moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() > 0) ||
                            moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase("FREE")
                    )) {
                        isTimerShow = true;
                    }
                    if (isTimerShow) {
                        String message = appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS ?
                                appCMSPresenter.getLocalisedStrings().getFaceOffText() :
                                appCMSPresenter.getLocalisedStrings().getContentAvailableText();
                        titleText.setText(message);
                        titleText.setId(R.id.timer_until_face_off);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    titleText.setTextAppearance(android.R.style.TextAppearance_Medium);
                } else {
                    titleText.setTextAppearance(context, android.R.style.TextAppearance_Medium);
                }
                titleText.setTextColor(appCMSPresenter.getGeneralTextColor());
                titleText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null) {
                    componentViewResult.componentView = new LinearLayout(context);
                    componentViewResult.componentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.VERTICAL);
                    ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                    componentViewResult.componentView.setId(R.id.timer_id_root);
                    LinearLayout timerLayout = new LinearLayout(context);
                    timerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    timerLayout.setOrientation(LinearLayout.HORIZONTAL);
                    timerLayout.setGravity(Gravity.CENTER);
                    timerLayout.setId(R.id.timer_id);

                    for (int count = 0; count < 4; count++) {
                        LinearLayout linearLayout = new LinearLayout(context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.setMargins(3, 0, 3, 0);
                        linearLayout.setLayoutParams(params);
                        linearLayout.setGravity(Gravity.CENTER);
                        linearLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                        linearLayout.setAlpha(0.9f);
                        for (int textView = 0; textView < 2; textView++) {
                            TextView text = new TextView(context);
                            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            text.setGravity(Gravity.CENTER);
                            text.setPadding(8, 0, 8, 0);
                            int textAppearance = textView == 0
                                    ? android.R.style.TextAppearance_Large
                                    : android.R.style.TextAppearance_Small;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                text.setTextAppearance(textAppearance);
                            } else {
                                text.setTextAppearance(context, textAppearance);
                            }
                            text.setTextColor(textView == 0
                                    ? appCMSPresenter.getGeneralTextColor()
                                    : (appCMSPresenter.getGeneralTextColor() + 0x92000000));
                            linearLayout.addView(text);
                        }
                        timerLayout.addView(linearLayout);
                    }
                    ((LinearLayout) componentViewResult.componentView).addView(titleText);
                    ((LinearLayout) componentViewResult.componentView).addView(timerLayout);
                    long eventDate = moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTime();
                    //calculate remaining time from event date and current date
                    long remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                    //if event date is greater than current date then start the timer
                    if (remainingTime > 0) {
                        stopCountdownTimer();
                        startTimerEvents(context, appCMSPresenter, eventDate, remainingTime);
                    } else {
                        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                            if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                                TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                                timerTile.setVisibility(View.GONE);
                            }
                            if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root) != null) {
                                LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root);
                                linearLayout.setVisibility(View.GONE);
                            }
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                    }
                }
                /**
                 * if transaction response as data and pricing is not null and schedule start date is greater than show the timer
                 */
                else if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getPricing() != null &&
                        moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0) {

                    /**
                     * For svod+tvod or svod+ppv check if video purchased or use subscribed
                     * then show timer
                     */
                    boolean isTimerShow = true;
                    if ((moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {
                        isTimerShow = true;
                    } else if (((moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() > 0) ||
                            moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase("FREE")
                    )) {
                        isTimerShow = true;
                    }
                    if (isTimerShow) {
                        componentViewResult.componentView = new LinearLayout(context);
                        componentViewResult.componentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.VERTICAL);
                        ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        componentViewResult.componentView.setId(R.id.timer_id_root);

                        LinearLayout timerLayout = new LinearLayout(context);
                        timerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        timerLayout.setOrientation(LinearLayout.HORIZONTAL);
                        timerLayout.setGravity(Gravity.CENTER);
                        timerLayout.setId(R.id.timer_id);

                        for (int count = 0; count < 4; count++) {
                            LinearLayout linearLayout = new LinearLayout(context);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            params.setMargins(10, 0, 10, 0);
                            linearLayout.setPadding(25, 0, 25, 0);

                            linearLayout.setLayoutParams(params);

                            linearLayout.setGravity(Gravity.CENTER);
                            linearLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                            linearLayout.setAlpha(0.9f);
                            for (int textView = 0; textView < 2; textView++) {
                                TextView text = new TextView(context);
                                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                text.setGravity(Gravity.CENTER);
                                text.setPadding(3, 0, 3, 0);
                                int textAppearance = textView == 0
                                        ? android.R.style.TextAppearance_Large
                                        : android.R.style.TextAppearance_Small;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    text.setTextAppearance(textAppearance);
                                } else {
                                    text.setTextAppearance(context, textAppearance);
                                }
                                text.setTextColor(textView == 0
                                        ? appCMSPresenter.getGeneralTextColor()
                                        : (appCMSPresenter.getGeneralTextColor() + 0x92000000));
                                linearLayout.addView(text);
                            }
                            timerLayout.addView(linearLayout);
                        }
                        ((LinearLayout) componentViewResult.componentView).addView(titleText);
                        ((LinearLayout) componentViewResult.componentView).addView(timerLayout);
                        long eventDate = (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate());

                        //calculate remaining time from event date and current date
                        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");

                        //if event date is greater than current date then start the timer
                        if (remainingTime > 0) {
                            stopCountdownTimer();
                            startTimer(context, appCMSPresenter, eventDate, remainingTime);
                        } else {
                            if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                                    TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                                    timerTile.setVisibility(View.GONE);
                                }
                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root) != null) {
                                    LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root);
                                    linearLayout.setVisibility(View.GONE);
                                }
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                            TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                            timerTile.setVisibility(View.GONE);
                        }
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root) != null) {
                            LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root);
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
            break;
            case PAGE_TABLE_VIEW_KEY:
                if (moduleType == PAGE_DOWNLOAD_SETTING_MODULE_KEY ||
                        moduleType == PAGE_LANGUAGE_SETTING_MODULE_KEY) {

                    componentViewResult.componentView = new RecyclerView(context);

                    ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    List<BaseInterface> mpegs = new ArrayList<>();
                    if (moduleAPI != null &&
                            moduleAPI.getContentData() != null &&
                            !moduleAPI.getContentData().isEmpty() &&
                            moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                            moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets() != null &&
                            moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets().getMpeg() != null) {
                        mpegs.addAll(moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets().getMpeg());
                    } else if (moduleAPI != null &&
                            moduleAPI.getContentData() != null &&
                            !moduleAPI.getContentData().isEmpty() &&
                            moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getLanguages() != null &&
                            moduleAPI.getContentData().get(0).getLanguages().size() > 0) {
                        mpegs.addAll(moduleAPI.getContentData().get(0).getLanguages());
                    } else {
                        mpegs = new ArrayList<>();
                    }

                    List<Component> components;
                    if (component.getComponents() != null) {
                        components = component.getComponents();
                    } else {
                        components = new ArrayList<>();
                    }

                    AppCMSDownloadQualityAdapter radioAdapter = new AppCMSDownloadQualityAdapter(context,
                            mpegs,
                            components,
                            appCMSPresenter,
                            jsonValueKeyMap,
                            metadataMap);

                    ((RecyclerView) componentViewResult.componentView).setAdapter(radioAdapter);
                    componentViewResult.componentView.setId(R.id.download_quality_selection_list);

                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                            .adapter(radioAdapter)
                            .listview((RecyclerView) componentViewResult.componentView)
                            .id(moduleId + component.getKey())
                            .build());
                } else if (moduleType == PAGE_EVENT_DETAIL_MODULE_KEY) {
                    componentViewResult.componentView = new RecyclerView(context);
                    /*
                     * get cache of recycler view if already created . need to clear cached  when opening first time this screen using same adapter and recycler view
                     * Currenlty cleared cache on following method navigateTODownloadPAge() , navigateToWatchlistPage() , navigateToHistoryPage()
                     */
                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL,
                                    false));

                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 0);
                    componentViewResult.componentView.setLayoutParams(params);
                    componentViewResult.componentView.setPadding(0, 0, 0, 0);
                    appcmsFightSelectionAdapter = new AppCMSFightSelectionAdapter(context,
                            this,
                            appCMSPresenter,
                            component.getLayout(),
                            false,
                            component,
                            jsonValueKeyMap,
                            moduleAPI,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            viewType,
                            appCMSAndroidModules, blockName);

                    ((RecyclerView) componentViewResult.componentView).setAdapter(appcmsFightSelectionAdapter);
                    ((RecyclerView) componentViewResult.componentView).setDescendantFocusability(RecyclerView.FOCUS_BLOCK_DESCENDANTS);
                    componentViewResult.onInternalEvent = appcmsFightSelectionAdapter;
                    componentViewResult.onInternalEvent.setModuleId(moduleId);
                    if (pageView != null) {
                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                .adapter(appcmsFightSelectionAdapter)
                                .listview((RecyclerView) componentViewResult.componentView)
                                .id(moduleId + component.getKey())
                                .build());
                    }
                } else if (moduleType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                    componentViewResult.componentView = new RecyclerView(context);
                    ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context));
                    SeparatorDecoration separatorDecoration = new SeparatorDecoration(componentViewResult.componentView.getContext(), Color.DKGRAY, 1f);
                    ((RecyclerView) componentViewResult.componentView).addItemDecoration(separatorDecoration);
                    if (moduleAPI.getContentData() != null &&
                            moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getGist() != null &&
                            moduleAPI.getContentData().get(0).getGist().getPersons() != null) {
                        List<ConceptData> conceptDataList = new ArrayList<>();
                        List<Person> person = new ArrayList<>();
                        person.add(moduleAPI.getContentData().get(0).getGist().getPersons().get(0));

                        conceptDataList.add(new ConceptData(metadataMap.getAboutThisConceptLabel(), person));
                        conceptDataList.add(new ConceptData(metadataMap.getMeetYourInstructorCTA(), moduleAPI.getContentData().get(0).getGist().getPersons()));

                        expandableGridAdapter = new ExpandableGridAdapter(conceptDataList, component, appCMSPresenter, moduleAPI.getContentData().get(0), context);
                        ((RecyclerView) componentViewResult.componentView).setAdapter(expandableGridAdapter);

                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(expandableGridAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    }
                } else if (moduleType == PAGE_AC_ROSTER_MODULE_KEY) {
                    componentViewResult.componentView = new RecyclerView(context);

                    /*
                     * get cache of recycler view if already created . need to clear cached  when opening first time this screen using same adapter and recycler view
                     * Currenlty cleared cache on following method navigateTODownloadPAge() , navigateToWatchlistPage() , navigateToHistoryPage()
                     */
                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL,
                                    false));

                    appcmsRosterAdapter = new AppCMSRosterAdapter(context,
                            this,
                            appCMSPresenter,
                            component.getLayout(),
                            false,
                            component,
                            jsonValueKeyMap,
                            moduleAPI,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            viewType,
                            appCMSAndroidModules, blockName);

                    ((RecyclerView) componentViewResult.componentView).setAdapter(appcmsRosterAdapter);
                    componentViewResult.onInternalEvent = appcmsRosterAdapter;
                    componentViewResult.onInternalEvent.setModuleId(moduleId);
                    if (pageView != null) {
                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                .adapter(appcmsRosterAdapter)
                                .listview((RecyclerView) componentViewResult.componentView)
                                .id(moduleId + component.getKey())
                                .build());
                    }
                } else if (moduleType == PAGE_SUB_NAV_MODULE_KEY) {
                    componentViewResult.componentView = new RecyclerView(context);

                    /*
                     * get cache of recycler view if already created . need to clear cached  when opening first time this screen using same adapter and recycler view
                     * Currenlty cleared cache on following method navigateTODownloadPAge() , navigateToWatchlistPage() , navigateToHistoryPage()
                     */
                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL,
                                    false));
                    appcmsSubNavAdapter = new AppCMSSubNavAdapter(context,
                            this,
                            appCMSPresenter,
                            component.getLayout(),
                            false,
                            component,
                            jsonValueKeyMap,
                            moduleAPI,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            viewType,
                            appCMSAndroidModules, blockName);
                    ((RecyclerView) componentViewResult.componentView).setAdapter(appcmsSubNavAdapter);
                    componentViewResult.onInternalEvent = appcmsSubNavAdapter;
                    componentViewResult.onInternalEvent.setModuleId(moduleId);
                    if (pageView != null) {
                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                .adapter(appcmsSubNavAdapter)
                                .listview((RecyclerView) componentViewResult.componentView)
                                .id(moduleId + component.getKey())
                                .build());
                    }
                } else if (moduleType == PAGE_PLAYLIST_MODULE_KEY || moduleType == PAGE_VIDEO_PLAYLIST_MODULE_KEY ||
                        uiBlockName == UI_BLOCK_PLAYLIST) {
                    componentViewResult.componentView = new RecyclerView(context);
                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL,
                                    false));
                    appCMSPlaylistAdapter = new AppCMSPlaylistAdapter(context,
                            this,
                            settings,
                            component.getLayout(),
                            false,
                            component,
                            jsonValueKeyMap,
                            moduleAPI,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            viewType,
                            appCMSAndroidModules, ((RecyclerView) componentViewResult.componentView), blockName);
                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSPlaylistAdapter);
                    componentViewResult.onInternalEvent = appCMSPlaylistAdapter;
                    componentViewResult.onInternalEvent.setModuleId(moduleId);
                    if (pageView != null) {
                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                .adapter(appCMSPlaylistAdapter)
                                .listview((RecyclerView) componentViewResult.componentView)
                                .id(moduleId + component.getKey())
                                .build());
                    }
                } else {
                    componentViewResult.componentView = new RecyclerView(context);
                    /*
                     * get cache of recycler view if already created . need to clear cached  when opening first time this screen using same adapter and recycler view
                     * Currenlty cleared cache on following method navigateTODownloadPAge() , navigateToWatchlistPage() , navigateToHistoryPage()
                     */
                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.VERTICAL,
                                    false));
                    appCMSUserWatHisDowAdapter = new AppCMSUserWatHisDowAdapter(context,
                            this,
                            component.getLayout(),
                            false,
                            component,
                            jsonValueKeyMap,
                            moduleAPI,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            viewType,
                            appCMSAndroidModules,
                            blockName,
                            false,
                            null);
                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSUserWatHisDowAdapter);
                    //for team schedule module .scroll list to first position item in which event time is greater than currrent time
                    if (moduleType == PAGE_AC_TEAM_SCHEDULE_MODULE_KEY) {
                        for (int i = 0; i < moduleAPI.getContentData().size(); i++) {
                            long eventDate = moduleAPI.getContentData().get(i).getGist().getEventSchedule().get(0).getEventTime();

                            long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                            if (remainingTime > 0) {
                                int selectedPostion = i;
                                appCMSPresenter.setSelectedSchedulePosition(i);
                                break;
                            }
                        }
                        ((RecyclerView) componentViewResult.componentView).scrollToPosition(appCMSPresenter.getSelectedSchedulePosition());

                    }
                    DefaultItemAnimator animator = new DefaultItemAnimator() {
                        @Override
                        public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                            return true;
                        }
                    };
                    ((RecyclerView) componentViewResult.componentView).setItemAnimator(animator);
                    componentViewResult.onInternalEvent = appCMSUserWatHisDowAdapter;
                    componentViewResult.onInternalEvent.setModuleId(moduleId);
                    if (pageView != null) {
                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                .adapter(appCMSUserWatHisDowAdapter)
                                .listview((RecyclerView) componentViewResult.componentView)
                                .id(moduleId + component.getKey())
                                .build());
                    }
                }
                break;
            case PAGE_LIST_VIEW_KEY:
            case PAGE_COLLECTIONGRID_KEY:
                if (moduleType == null) {
                    moduleType = PAGE_EMPTY_KEY;
                }
                if (moduleType == PAGE_SUBSCRIPTION_IMAGEROW_KEY) {
                    componentViewResult.componentView = new ImageView(context);
                    if (BaseView.isTablet(context)) {
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.features_tablet);
                    } else {
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.features_mobile);
                    }
                } else {
                    if (moduleType == PAGE_SEASON_TRAY_MODULE_KEY ||
                            moduleType == PAGE_SEASON_TAB_MODULE_KEY ||
                            moduleType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                        componentViewResult.componentView = new AdjustableRecyclerView(context);
                    } else if (moduleType == PAGE_SUBSCRIPTION_IMAGEROW_02_KEY) {
                        componentViewResult.componentView = new RecyclerView(context);
                    } else
                        componentViewResult.componentView = new RecyclerView(context);

                    AppCMSViewAdapter appCMSViewAdapter;

                    if (componentKey == PAGE_LIST_VIEW_KEY) {
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        appCMSUserWatHisDowAdapter = new AppCMSUserWatHisDowAdapter(context,
                                this,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules,
                                blockName,
                                false,
                                null);

                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSUserWatHisDowAdapter);
                        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                int action = e.getAction();
                                switch (action) {
                                    case MotionEvent.ACTION_MOVE:
                                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                                        break;
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        };

                        ((RecyclerView) componentViewResult.componentView).addOnItemTouchListener(mScrollTouchListener);

                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSUserWatHisDowAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (moduleType == PAGE_AC_SEARCH_MODULE_KEY ||
                            moduleType == PAGE_AC_SEARCH03_MODULE_KEY) {
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        appCMSViewAdapter = new AppCMSViewAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules,
                                blockName,
                                constrainteView, constraintViewCreator);

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }

                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSViewAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (moduleType == PAGE_AC_SEARCH02_MODULE_KEY) {

                        int coloumn = 2;
                        if (BaseView.isTablet(context)) {
                            if (BaseView.isLandscape(context)) {
                                coloumn = 4;
                            } else {
                                coloumn = 3;

                            }

                        } else {
                            coloumn = 2;

                        }
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new GridLayoutManager(context, coloumn, GridLayoutManager.VERTICAL, false));

                        appCMSViewAdapter = new AppCMSViewAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }

                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSViewAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) {

                        if (BaseView.isTablet(context) && BaseView.isLandscape(context))
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));
                        else
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                        appCMSViewAdapter = new AppCMSViewAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);

                        if (!BaseView.isTablet(context))
                            componentViewResult.useWidthOfScreen = true;
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSViewAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        AppCMSPlansAdapter appCMSPlansAdapter = new AppCMSPlansAdapter(context,
                                this,
                                constrainteView ? constraintViewCreator : null,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules,
                                blockName,
                                metadataMap);


                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }

                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSPlansAdapter);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSPlansAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (moduleType == PAGE_SUBSCRIPTION_IMAGEROW_02_KEY) {
                        GridLayoutManager gridLayoutManager;
                        if (BaseView.isLandscape(context)) {
                            gridLayoutManager = new GridLayoutManager(context, 3,
                                    GridLayoutManager.VERTICAL, false);
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(gridLayoutManager);
                        } else {
                            gridLayoutManager = new GridLayoutManager(context, 2,
                                    GridLayoutManager.VERTICAL, false);
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(gridLayoutManager);
                        }
                        componentViewResult.componentView.setBackgroundColor(Color.parseColor(
                                CommonUtils.getColor(context, component.getBackgroundColor())));
                        AppCMSBenefitPlanPageAdapter appCMSBenefitPlanPageAdapter = new AppCMSBenefitPlanPageAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules, blockName);

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }
                        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                switch (appCMSBenefitPlanPageAdapter.getItemViewType(position)) {
                                    case AppCMSBenefitPlanPageAdapter.TYPE_FOOTER:
                                    case AppCMSBenefitPlanPageAdapter.TYPE_HEADER:
                                        if (BaseView.isLandscape(context))
                                            return 3;
                                        else
                                            return 2;
                                    case AppCMSBenefitPlanPageAdapter.TYPE_ITEM:
                                        return 1;
                                    default:
                                        return -1;
                                }
                            }
                        });
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSBenefitPlanPageAdapter);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSBenefitPlanPageAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }

                    } else if (moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY) {

                        if (BaseView.isTablet(context)) {
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.HORIZONTAL,
                                            false));
                        } else {
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.VERTICAL,
                                            false));
                        }
                        appCMSViewAdapter = new AppCMSViewAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSViewAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (componentKey == PAGE_PHOTOGALLERY_GRID_KEY) {

                        LinearLayoutManager layoutManager = null;
                        if (BaseView.isTablet(context)) {
                            layoutManager = BaseView.isLandscape(context) ?
                                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) :
                                    new GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false);
                        } else {
                            layoutManager = new GridLayoutManager(context, 3,
                                    GridLayoutManager.VERTICAL, false);
                        }

                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(layoutManager);
                        appCMSViewAdapter = new AppCMSViewAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                context.getResources().getString(R.string.app_cms_photo_tray_module_key),
                                appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);

                        componentViewResult.componentView.setId(R.id.photo_gallery_grid_recyclerview);
                        ((RecyclerView) componentViewResult.componentView).addItemDecoration(new PhotoGalleryGridInsetDecoration(5, 15, 0));
                        photoGalleryNextPreviousListener = appCMSViewAdapter.setPhotoGalleryImageSelectionListener(photoGalleryNextPreviousListener);

                        appCMSViewAdapter.setPhotoGalleryImageSelectionListener((url, selectedPosition) -> {
                            ImageView imageView = pageView.findViewById(R.id.photo_gallery_selectedImage);
                            Glide.with(imageView.getContext()).load(url).into(imageView);
                            int photoGallerySize = (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) ?
                                    ((moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                                            moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets() != null) ?
                                            moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size() : 0) : 0;
                            String position = photoGallerySize == 0 ? "0/0" : (selectedPosition + 1) + "/" + photoGallerySize;
                            if (pageView.findChildViewById(R.id.photo_gallery_grid_recyclerview) != null) {
                                ((RecyclerView) pageView.findChildViewById(R.id.photo_gallery_grid_recyclerview)).scrollToPosition(selectedPosition);
                            }

                            if (selectedPosition == 0) {
                                enablePhotoGalleryButtons(false, true, pageView, appCMSPresenter, position);
                            } else if (selectedPosition > 0 && selectedPosition < photoGallerySize - 1) {
                                enablePhotoGalleryButtons(true, true, pageView, appCMSPresenter, position);
                            } else {
                                enablePhotoGalleryButtons(true, false, pageView, appCMSPresenter, position);
                            }

                        });

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }

                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                int action = e.getAction();
                                switch (action) {
                                    case MotionEvent.ACTION_MOVE:
                                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                                        break;
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        };
                        ((RecyclerView) componentViewResult.componentView).addOnItemTouchListener(mScrollTouchListener);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSViewAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else if (componentKey == PAGE_TRAY_05_MODULE_KEY) {

                        LinearLayoutManager layoutManager = null;
                        if (BaseView.isTablet(context)) {
                            layoutManager = BaseView.isLandscape(context) ?
                                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) :
                                    new GridLayoutManager(context, 6, GridLayoutManager.VERTICAL, false);
                        } else {
                            layoutManager = new GridLayoutManager(context, 3,
                                    GridLayoutManager.VERTICAL, false);
                        }

                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(layoutManager);
                        appCMSViewAdapter = new AppCMSViewAdapter(context,
                                this,
                                settings,
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);

                        componentViewResult.componentView.setId(R.id.photo_gallery_grid_recyclerview);
                        ((RecyclerView) componentViewResult.componentView).addItemDecoration(new PhotoGalleryGridInsetDecoration(5, 15, 0));
                        photoGalleryNextPreviousListener = appCMSViewAdapter.setPhotoGalleryImageSelectionListener(photoGalleryNextPreviousListener);

                        appCMSViewAdapter.setPhotoGalleryImageSelectionListener((url, selectedPosition) -> {
                            ImageView imageView = pageView.findViewById(R.id.photo_gallery_selectedImage);
                            Glide.with(imageView.getContext()).load(url).into(imageView);
                            int photoGallerySize = (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) ?
                                    ((moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                                            moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets() != null) ?
                                            moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size() : 0) : 0;
                            String position = photoGallerySize == 0 ? "0/0" : (selectedPosition + 1) + "/" + photoGallerySize;
                            if (pageView.findChildViewById(R.id.photo_gallery_grid_recyclerview) != null) {
                                ((RecyclerView) pageView.findChildViewById(R.id.photo_gallery_grid_recyclerview)).scrollToPosition(selectedPosition);
                            }

                            if (selectedPosition == 0) {
                                enablePhotoGalleryButtons(false, true, pageView, appCMSPresenter, position);
                            } else if (selectedPosition > 0 && selectedPosition < photoGallerySize - 1) {
                                enablePhotoGalleryButtons(true, true, pageView, appCMSPresenter, position);
                            } else {
                                enablePhotoGalleryButtons(true, false, pageView, appCMSPresenter, position);
                            }
                        });

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }

                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                            @Override
                            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                int action = e.getAction();
                                switch (action) {
                                    case MotionEvent.ACTION_MOVE:
                                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                                        break;
                                }
                                return false;
                            }

                            @Override
                            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                            }

                            @Override
                            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                            }
                        };

                        ((RecyclerView) componentViewResult.componentView).addOnItemTouchListener(mScrollTouchListener);
                        if (pageView != null) {
                            pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                    .adapter(appCMSViewAdapter)
                                    .listview((RecyclerView) componentViewResult.componentView)
                                    .id(moduleId + component.getKey())
                                    .build());
                        }
                    } else {
                        parentViewType = jsonValueKeyMap.get(viewType);
                        if (parentViewType == PAGE_GRID_MODULE_KEY ||
                                parentViewType == PAGE_LIST_MODULE_KEY ||
                                parentViewType == PAGE_ARTICLE_FEED_MODULE_KEY) {
                            appCMSPresenter.setMoreIconAvailable();
                            int numCols = 1;
                            if (settings != null && settings.getColumns() != null) {
                                if (BaseView.isTablet(context)) {
                                    numCols = settings.getColumns().getTablet();
                                } else {
                                    numCols = settings.getColumns().getMobile();
                                }
                            }
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(new GridLayoutManager(context,
                                            numCols,
                                            LinearLayoutManager.VERTICAL,
                                            false));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                componentViewResult.componentView.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        } else if (parentViewType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.VERTICAL,
                                            false));
                        } else if (parentViewType == PAGE_SEASON_TRAY_MODULE_KEY) {
                            if (BaseView.isTablet(context)) {
                                int columnCount = 2;
                                if (blockName.equalsIgnoreCase(context.getString(R.string.ui_block_showDetail_05))) {
                                    columnCount = BaseView.isLandscape(context) ? 4 : 3;
                                }

                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new GridLayoutManager(context,
                                                columnCount,
                                                LinearLayoutManager.VERTICAL,
                                                false));
                            } else {
                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new LinearLayoutManager(context,
                                                LinearLayoutManager.VERTICAL,
                                                false));
                            }
                        } else if (parentViewType == PAGE_SEASON_TAB_MODULE_KEY) {
                            if (BaseView.isTablet(context)) {
                                if (BaseView.isLandscape(context)) {
                                    ((RecyclerView) componentViewResult.componentView)
                                            .setLayoutManager(new GridLayoutManager(context,
                                                    4,
                                                    LinearLayoutManager.VERTICAL,
                                                    false));
                                } else {
                                    ((RecyclerView) componentViewResult.componentView)
                                            .setLayoutManager(new GridLayoutManager(context,
                                                    3,
                                                    LinearLayoutManager.VERTICAL,
                                                    false));
                                }
                            } else {
                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new LinearLayoutManager(context,
                                                LinearLayoutManager.VERTICAL,
                                                false));
                            }
                        } else if (componentKey == PAGE_SEE_ALL_COLLECTIONGRID_KEY) {
                            int columns = CommonUtils.seeAllColumnCount;
                            if (settings != null && settings.getColumns() != null
                                    && settings.getColumns().getMobile() < 2) {
                                columns = settings.getColumns().getMobile();
                            }
                            if (BaseView.isTablet(context))
                                columns = 3;
                            if (BaseView.isLandscape(context))
                                columns = 4;
                            gridLayoutManager = new GridLayoutManager(context, columns,
                                    GridLayoutManager.VERTICAL, false);
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(gridLayoutManager);

                            ((RecyclerView) componentViewResult.componentView).setId(R.id.see_all_recycler_view);
                        } else if (componentKey == PAGE_LIVE_SCHEDULE_GRID_KEY) {
                            if (BaseView.isTablet(context)) {
                                if (BaseView.isLandscape(context)) {
                                    ((RecyclerView) componentViewResult.componentView)
                                            .setLayoutManager(new GridLayoutManager(context,
                                                    3,
                                                    LinearLayoutManager.VERTICAL,
                                                    false));

                                } else {
                                    ((RecyclerView) componentViewResult.componentView)
                                            .setLayoutManager(new GridLayoutManager(context,
                                                    2,
                                                    LinearLayoutManager.VERTICAL,
                                                    false));
                                }
                            } else {
                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new LinearLayoutManager(context,
                                                LinearLayoutManager.VERTICAL,
                                                false));
                            }
                            componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                        } else if (componentKey == PAGE_SCHEDULE_GRID_NO_DATA_KEY ||
                                componentKey == PAGE_BROWSE_ALL_GRID_KEY ||
                                componentKey == PAGE_BROWSE_TOP_GRID_KEY) {
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.HORIZONTAL,
                                            false));
                        } else if (componentKey == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_DATE_GRID_KEY) {
                            datesManager = new LinearLayoutManager(context,
                                    LinearLayoutManager.HORIZONTAL,
                                    false);
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(datesManager);
                            componentViewResult.componentView.setNestedScrollingEnabled(true);
                            recyclerViewDates = ((RecyclerView) componentViewResult.componentView);
                            ((RecyclerView) componentViewResult.componentView)
                                    .setLayoutManager(datesManager);
                            recyclerViewWeeks = ((RecyclerView) componentViewResult.componentView);
                            RecyclerView.OnItemTouchListener mScrollItemTouchListener = new RecyclerView.OnItemTouchListener() {
                                @Override
                                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                    appCMSPresenter.setItemViewClicked(false);
                                    recyclerViewDates.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            if (!appCMSPresenter.isItemViewClicked()) {
                                                CustomSubscriber customSubscriber = new CustomSubscriber();
                                                customSubscriber.setHorizontalScroll(true);
                                                if (dx < 0) {
                                                    customSubscriber.setPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                                                    SeasonTabSelectorBus.instanceOf().setCustomSubscriber(customSubscriber);
                                                } else if (dx > 0) {
                                                    customSubscriber.setPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition());
                                                    SeasonTabSelectorBus.instanceOf().setCustomSubscriber(customSubscriber);
                                                }
                                            }
                                        }
                                    });
                                    return false;
                                }

                                @Override
                                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                                    System.out.println("sfsfd");
                                }

                                @Override
                                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                                }
                            };
                            recyclerViewDates.addOnItemTouchListener(mScrollItemTouchListener);


                        } else if (componentKey == AppCMSUIKeyType.PAGE_LINK_MODULE_HORIZONTAL_GRID_KEY) {
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            recyclerViewDates = ((RecyclerView) componentViewResult.componentView);
                            RecyclerView.OnItemTouchListener mScrollItemTouchListener = new RecyclerView.OnItemTouchListener() {
                                @Override
                                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                    appCMSPresenter.setItemViewClicked(true);
                                    recyclerViewDates.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            if (!appCMSPresenter.isItemViewClicked()) {
                                                CustomSubscriber customSubscriber = new CustomSubscriber();
                                                customSubscriber.setHorizontalScroll(true);

                                                if (dx < 0) {
                                                    customSubscriber.setPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                                                    SeasonTabSelectorBus.instanceOf().setCustomSubscriber(customSubscriber);
                                                } else if (dx > 0) {
                                                    customSubscriber.setPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition());
                                                    SeasonTabSelectorBus.instanceOf().setCustomSubscriber(customSubscriber);
                                                }

                                            }
                                        }
                                    });
                                    return false;
                                }

                                @Override
                                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                                }

                                @Override
                                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                                }
                            };
                            recyclerViewDates.addOnItemTouchListener(mScrollItemTouchListener);
                        } else if (componentKey == PAGE_WEEK_SCHEDULE_VIEW_GRID_KEY) {
                            weeksManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

                            weeksManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            weeksManager.setSmoothScrollbarEnabled(true);
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(weeksManager);
                            recyclerViewWeeks = ((RecyclerView) componentViewResult.componentView);
                            RecyclerView.OnItemTouchListener mScrollItemTouchListener = new RecyclerView.OnItemTouchListener() {
                                @Override
                                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                                    appCMSPresenter.setItemViewClicked(false);
                                    recyclerViewWeeks.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                        @Override
                                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                            super.onScrollStateChanged(recyclerView, newState);
                                        }

                                        @Override
                                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                            super.onScrolled(recyclerView, dx, dy);
                                            if (!appCMSPresenter.isItemViewClicked()) {
                                                CustomSubscriber customSubscriber = new CustomSubscriber();
                                                customSubscriber.setHorizontalScroll(false);

                                                LinearLayoutManager linearLayoutManager1 = (LinearLayoutManager) recyclerView.getLayoutManager();
                                                if (dy < 0) {
                                                    customSubscriber.setPosition(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition());
                                                    SeasonTabSelectorBus.instanceOf().setCustomSubscriber(customSubscriber);
                                                } else if (dy > 0) {

                                                    int totalItemCount = linearLayoutManager1.getItemCount();
                                                    int LastCompletelyVisibleItemPosition = linearLayoutManager1.findLastCompletelyVisibleItemPosition();
                                                    if (totalItemCount > 0 && LastCompletelyVisibleItemPosition == totalItemCount - 1) {
                                                        customSubscriber.setPosition(LastCompletelyVisibleItemPosition);
                                                    } else {
                                                        customSubscriber.setPosition(linearLayoutManager1.findFirstVisibleItemPosition());
                                                    }
                                                    SeasonTabSelectorBus.instanceOf().setCustomSubscriber(customSubscriber);
                                                }

                                            }
                                        }
                                    });
                                    rv.getParent().requestDisallowInterceptTouchEvent(true);
                                    return false;
                                }

                                @Override
                                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                                    if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                                    } else {
                                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                                    }
                                }

                                @Override
                                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                                }
                            };
                            recyclerViewWeeks.addOnItemTouchListener(mScrollItemTouchListener);
                        } else if (componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_RECENT_GRID_KEY) {
                            if (BaseView.isLandscape(context))
                                ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            else
                                ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        } else if (componentKey == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY
                                || componentKey == AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY
                                || componentKey == AppCMSUIKeyType.PAGE_CLASS_GRID_KEY) {
                        } else {
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context,
                                    LinearLayoutManager.HORIZONTAL, false));
                            ((RecyclerView) componentViewResult.componentView).setDescendantFocusability(RecyclerView.FOCUS_BLOCK_DESCENDANTS);
                        }

                        if (parentViewType == PAGE_SEASON_TRAY_MODULE_KEY ||
                                parentViewType == PAGE_SEASON_TAB_MODULE_KEY ||
                                parentViewType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                            if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    !moduleAPI.getContentData().isEmpty() &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getSeason() != null &&
                                    !moduleAPI.getContentData().get(0).getSeason().isEmpty() &&
                                    moduleAPI.getContentData().get(0).getSeason().get(0) != null) {

                                CollectionGridItemViewCreator collectionGridItemViewCreator =
                                        new CollectionGridItemViewCreator(this,
                                                parentLayout,
                                                false,
                                                component,
                                                appCMSPresenter,
                                                moduleAPI,
                                                appCMSAndroidModules,
                                                settings,
                                                jsonValueKeyMap,
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                false,
                                                true,
                                                viewType,
                                                false,
                                                false, blockName);

                                List<String> allEpisodeIds = new ArrayList<>();
                                List<Season_> seasons = moduleAPI.getContentData().get(0).getSeason();
                                List<ContentDatum> allEpisodes = new ArrayList<>();
                                int numSeasons = seasons.size();
                                for (int i = 0; i < numSeasons; i++) {
                                    Season_ season = seasons.get(i);
                                    List<ContentDatum> episodes = season.getEpisodes();
                                    int numEpisodes = episodes.size();
                                    if (season.getEpisodes() != null) {
                                        for (int j = 0; j < numEpisodes; j++) {
                                            ContentDatum episodeContentDatum = episodes.get(j);
                                            allEpisodes.add(episodeContentDatum);
                                            if (episodeContentDatum != null &&
                                                    episodeContentDatum.getGist() != null &&
                                                    episodeContentDatum.getGist().getId() != null) {
                                                allEpisodeIds.add(episodeContentDatum.getGist().getId());
                                            }
                                        }
                                    }
                                }

                                AppCMSTraySeasonItemAdapter appCMSTraySeasonItemAdapter =
                                        new AppCMSTraySeasonItemAdapter(context,
                                                collectionGridItemViewCreator,
                                                appCMSAndroidModules,
                                                parentLayout,
                                                settings,
                                                component,
                                                parentViewType,
                                                moduleAPI,
                                                component.getComponents(),
                                                allEpisodeIds,
                                                allEpisodes,
                                                jsonValueKeyMap,
                                                viewType,
                                                ((RecyclerView) componentViewResult.componentView),
                                                blockName,
                                                component.getTrayClickAction(),
                                                constrainteView,
                                                constraintViewCreator);
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSTraySeasonItemAdapter);
                                componentViewResult.onInternalEvent = appCMSTraySeasonItemAdapter;
                                componentViewResult.onInternalEvent.setModuleId(moduleId);
                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSTraySeasonItemAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            }
                        } else if (parentViewType == PAGE_ARTICLE_FEED_MODULE_KEY) {
                            AppCMSArticleFeedViewAdapter appCMSArticleFeedViewAdapter = new AppCMSArticleFeedViewAdapter(context,
                                    this,
                                    appCMSPresenter,
                                    settings,
                                    parentLayout,
                                    false,
                                    component,
                                    jsonValueKeyMap,
                                    moduleAPI,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    viewType,
                                    appCMSAndroidModules);
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSArticleFeedViewAdapter);
                            if (pageView != null) {
                                pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                        .adapter(appCMSArticleFeedViewAdapter)
                                        .listview((RecyclerView) componentViewResult.componentView)
                                        .id(moduleId + component.getKey())
                                        .build());
                            }
                        } else if (componentKey == PAGE_SEE_ALL_COLLECTIONGRID_KEY) {
                            if (moduleAPI != null && moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().size() > 0) {
                                RecyclerView recyclerViewSeeAll = ((RecyclerView) componentViewResult.componentView);
                                AppCMSSeeAllAdapter appCMSSeeAllAdapter = new AppCMSSeeAllAdapter(context,
                                        this,
                                        appCMSPresenter,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        moduleAPI,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules,
                                        blockName,
                                        pageView,
                                        recyclerViewSeeAll);

                                recyclerViewSeeAll.setAdapter(appCMSSeeAllAdapter);

                                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                    @Override
                                    public int getSpanSize(int position) {
                                        switch (appCMSSeeAllAdapter.getItemViewType(position)) {
                                            case AppCMSSeeAllAdapter.ITEM:
                                                return 1;
                                            case AppCMSSeeAllAdapter.LOADING:
                                                int columns = CommonUtils.seeAllColumnCount;
                                                if (settings != null && settings.getColumns() != null
                                                        && settings.getColumns().getMobile() < 2) {
                                                    columns = settings.getColumns().getMobile();
                                                }
                                                if (BaseView.isTablet(context))
                                                    columns = 3;
                                                if (BaseView.isLandscape(context))
                                                    columns = 4;
                                                return columns; //number of columns of the grid
                                            default:
                                                return -1;
                                        }
                                    }
                                });

                                recyclerViewSeeAll.addOnScrollListener(new PaginationScrollListener(context, gridLayoutManager, recyclerViewSeeAll) {
                                    @Override
                                    protected void loadMoreItems() {
                                        isLoading = true;

                                        appCMSSeeAllAdapter.setClickable(false);
                                        if (!appCMSPresenter.isLastPage())
                                            appCMSSeeAllAdapter.addLoadingFooter();
                                        new Handler().postDelayed(() -> {
                                            if (moduleAPI.getFilters() != null) {
                                                appCMSPresenter.getSeeAllCategory(moduleAPI.getFilters(), moduleAPI.getId(), new Action1<AppCMSPageAPI>() {
                                                    @Override
                                                    public void call(AppCMSPageAPI appCMSPageAPI) {
                                                        if (appCMSPageAPI != null) {
                                                            for (Module module : appCMSPageAPI.getModules()) {
                                                                if (module.getModuleType().equalsIgnoreCase("CategoryDetailModule")
                                                                        || module.getModuleType().equalsIgnoreCase("GeneratedTrayModule")) {
                                                                    appCMSPresenter.setLastPage(!module.hasNext());
                                                                    if (module.getContentData() != null && module.getContentData().size() > 0) {
                                                                        isLoading = false;
                                                                        appCMSPresenter.setLastPage(!module.hasNext());
                                                                        appCMSSeeAllAdapter.removeLoadingFooter();
                                                                        appCMSSeeAllAdapter.addAll(module.getContentData());
                                                                        TextView seeAllTitle = appCMSPresenter.getCurrentActivity().findViewById(R.id.see_all_title_text_view);
                                                                        if (seeAllTitle != null &&
                                                                                appCMSPresenter.getSeeAllModule() != null &&
                                                                                appCMSPresenter.getSeeAllModule().getTitle() != null) {
                                                                            seeAllTitle.setText(appCMSPresenter.getSeeAllModule().getTitle() + " " + "(" + appCMSSeeAllAdapter.getItemCount() + ")");
                                                                        }
                                                                    } else {
                                                                        appCMSSeeAllAdapter.removeLoadingFooter();
                                                                        appCMSSeeAllAdapter.setClickable(true);
                                                                    }
                                                                }
                                                            }

                                                            if (gridLayoutManager.getItemCount() < 12 && appCMSPageAPI.getModules().get(0).hasNext()) {
                                                                loadMoreItems();
                                                            }

                                                        } else {
                                                            appCMSSeeAllAdapter.removeLoadingFooter();
                                                            appCMSSeeAllAdapter.setClickable(true);
                                                        }
                                                    }
                                                });
                                            }
                                        }, 100);
                                    }


                                    @Override
                                    public int getTotalPageCount() {
                                        return 0;
                                    }

                                    @Override
                                    public boolean isLastPage() {
                                        return appCMSPresenter.isLastPage();
                                    }

                                    @Override
                                    public boolean isLoading() {
                                        return isLoading;
                                    }
                                });

                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSSeeAllAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            }
                        } else {
                            if (parentViewType == PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY) {
                                appCMSViewAdapter = new AppCMSViewAdapter(context,
                                        this,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        moduleAPI,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);

                                componentViewResult.useWidthOfScreen = true;
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSViewAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            } else if (parentViewType == PAGE_SCHEDULE_CAROUSEL_MODULE_KEY ||
                                    parentViewType == PAGE_BRAND_CAROUSEL_MODULE_TYPE) {
                                if (!BaseView.isLandscape(context)) {
                                    componentViewResult.componentView.setBackgroundColor(Color.parseColor(
                                            CommonUtils.getColor(context, component.getBackgroundColor())));
                                    AppCMSCarouselTimeAdapter appCMSCarouselTimeAdapter = new AppCMSCarouselTimeAdapter(context,
                                            this,
                                            appCMSPresenter,
                                            settings,
                                            parentLayout,
                                            false,
                                            component,
                                            jsonValueKeyMap,
                                            moduleAPI,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            viewType,
                                            appCMSAndroidModules, blockName);
                                    componentViewResult.useWidthOfScreen = true;
                                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSCarouselTimeAdapter);
                                    if (pageView != null) {
                                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                                .adapter(appCMSCarouselTimeAdapter)
                                                .listview((RecyclerView) componentViewResult.componentView)
                                                .id(moduleId + component.getKey())
                                                .build());
                                    }
                                }
                            } else if (componentKey == PAGE_INSTRUCTOR_RECENT_GRID_KEY) {
                                appCMSViewAdapter = new AppCMSViewAdapter(context,
                                        this,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        moduleAPI,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSViewAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            } else if (componentKey == PAGE_WEEK_SCHEDULE_VIEW_GRID_KEY) {

                                Module mAp = moduleAPI;
                                if (AppCMSPageActivity.isDailyView) {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                } else {
                                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                                }
                                AppCMSLiveWeekViewAdapter appCMSWeekViewAdapter = new AppCMSLiveWeekViewAdapter(context,
                                        this,
                                        appCMSPresenter,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        mAp,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules, blockName,
                                        constrainteView, constraintViewCreator);

                                componentViewResult.useWidthOfScreen = true;
                                if (componentKey != PAGE_LIVE_SCHEDULE_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                }
                                componentViewResult.componentView.setId(R.id.scheduleWeekViewGrid);
                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new LinearLayoutManager(context,
                                                LinearLayoutManager.VERTICAL,
                                                false));
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSWeekViewAdapter);
                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSWeekViewAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            } else if (componentKey == PAGE_LIVE_SCHEDULE_GRID_KEY ||
                                    componentKey == PAGE_WEEK_SCHEDULE_GRID_KEY
                                    || componentKey == PAGE_EDIT_PROFILE_GRID_KEY) {
                                Module mAp = moduleAPI;

                                if (AppCMSPageActivity.isDailyView) {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                } else {
                                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                                }
                                if (componentKey == PAGE_LIVE_SCHEDULE_GRID_KEY) {
                                    mAp = removePastEventsAndShowSpecificDayEvents(moduleAPI, 0);
                                    componentViewResult.componentView.setId(R.id.liveScheduleGrid);
                                    if (AppCMSPageActivity.isDailyView) {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    }
                                } else if (component.getId() != null && component.getId().equals(context.getString(R.string.publicProfileGrid)))
                                    mAp = getSpecificNumberOfObjects(moduleAPI, 2);
                                else if (component.getId() != null && component.getId().equals(context.getString(R.string.privateProfileGrid)))
                                    mAp = getSpecificNumberOfObjects(moduleAPI, 8);
                                appCMSViewAdapter = new AppCMSViewAdapter(context,
                                        this,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        mAp,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules,
                                        blockName,
                                        constrainteView, constraintViewCreator);

                                componentViewResult.useWidthOfScreen = true;
                                if (componentKey != PAGE_LIVE_SCHEDULE_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                }
                                ((RecyclerView) componentViewResult.componentView).addItemDecoration(new SeparatorDecoration(componentViewResult.componentView.getContext(), Color.DKGRAY, 1f));
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);


                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSViewAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            } else if (componentKey == PAGE_CLASS_GRID_KEY) {
                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new LinearLayoutManager(context,
                                                LinearLayoutManager.VERTICAL,
                                                false));
                                ((RecyclerView) componentViewResult.componentView).setDescendantFocusability(RecyclerView.FOCUS_BLOCK_DESCENDANTS);
                                appCMSViewAdapter = new AppCMSViewAdapter(context,
                                        this,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        moduleAPI,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules,
                                        blockName,
                                        false, constraintViewCreator);
                                componentViewResult.useWidthOfScreen = true;
                                if (componentKey != PAGE_LIVE_SCHEDULE_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                }
                                ((RecyclerView) componentViewResult.componentView).addItemDecoration(new SeparatorDecoration(componentViewResult.componentView.getContext(), Color.DKGRAY, 1f));
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSViewAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            } else if (componentKey == PAGE_WEEK_SCHEDULE_DATE_GRID_KEY ||
                                    componentKey == PAGE_SCHEDULE_GRID_NO_DATA_KEY ||
                                    componentKey == PAGE_LIVE_SCHEDULE_GRID_NO_DATA_KEY ||
                                    componentKey == PAGE_LINK_MODULE_HORIZONTAL_GRID_KEY) {
                                AppCMSPageLinkHorizontalGridAdapter appCMSPageLinkHorizontalGridAdapter = new AppCMSPageLinkHorizontalGridAdapter(context,
                                        this,
                                        appCMSPresenter,
                                        settings,
                                        parentLayout,
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        moduleAPI,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        viewType,
                                        appCMSAndroidModules, blockName, constrainteView, pageView, ((RecyclerView) componentViewResult.componentView));
                                if (componentKey == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_DATE_GRID_KEY ||
                                        componentKey == AppCMSUIKeyType.PAGE_LINK_MODULE_HORIZONTAL_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(Color.parseColor(appCMSPresenter.getSettings().getStyle().getBackgroundColor()));
                                    componentViewResult.componentView.setId(R.id.scheduleGridDate);
                                    if (AppCMSPageActivity.isDailyView) {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    }
                                } else if (componentKey == PAGE_SCHEDULE_GRID_NO_DATA_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.weekviewGridNodata));

                                    componentViewResult.componentView.setId(R.id.scheduleGridNoData);
                                    if (AppCMSPageActivity.isDailyView) {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    }
                                } else if (componentKey == PAGE_LIVE_SCHEDULE_GRID_NO_DATA_KEY) {
                                    componentViewResult.componentView.setId(R.id.liveScheduleGridNoData);

                                    if (AppCMSPageActivity.isDailyView) {
                                        if (removePastEventsAndShowSpecificDayEvents(moduleAPI, 0).getContentData().size() > 0) {
                                            componentViewResult.componentView.setVisibility(View.GONE);
                                        } else {
                                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    }
                                }
                                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSPageLinkHorizontalGridAdapter);
                                if (pageView != null) {
                                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                            .adapter(appCMSPageLinkHorizontalGridAdapter)
                                            .listview((RecyclerView) componentViewResult.componentView)
                                            .id(moduleId + component.getKey())
                                            .build());
                                }
                            } else {
                                if (componentKey == PAGE_BROWSE_CONCEPTS_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                }

                                if (componentKey == PAGE_BROWSE_ALL_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                    componentViewResult.addToPageView = true;
                                    double width = 0.143; //mobile
                                    if (BaseView.isTablet(context))
                                        width = BaseView.isLandscape(context) ? 0.107 : 0.142;
                                    FrameLayout.LayoutParams param =
                                            new FrameLayout.LayoutParams((int) (BaseView.getDeviceWidth() * width),
                                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                    param.setMargins(BaseView.dpToPx(0), 0, 0, 0);
                                    param.gravity = Gravity.TOP | Gravity.START;
                                    componentViewResult.componentView.setLayoutParams(param);
                                    ((RecyclerView) componentViewResult.componentView).addItemDecoration(new SeparatorDecoration(componentViewResult.componentView.getContext(), Color.DKGRAY, 1f));
                                    Module mAp = moduleAPI;
                                    mAp = addItems(mAp, 1);
                                    if (mAp != null && mAp.getContentData() != null
                                            && moduleAPI.getClassessData() != null
                                            && moduleAPI.getClassessData().size() > 0)
                                        mAp.getContentData().get(0).setNumOfClasses(moduleAPI.getClassessData().size());
                                    AppCMSBrowseAllAdapter appCMSBrowseAllAdapter = new AppCMSBrowseAllAdapter(context,
                                            this,
                                            appCMSPresenter,
                                            settings,
                                            parentLayout,
                                            false,
                                            component,
                                            jsonValueKeyMap,
                                            mAp,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            viewType,
                                            appCMSAndroidModules, blockName, constrainteView);

                                    componentViewResult.useWidthOfScreen = true;
                                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSBrowseAllAdapter);
                                    if (pageView != null) {
                                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                                .adapter(appCMSBrowseAllAdapter)
                                                .listview((RecyclerView) componentViewResult.componentView)
                                                .id(moduleId + component.getKey())
                                                .build());
                                    }
                                } else if (componentKey == PAGE_BROWSE_TOP_GRID_KEY) {
                                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                    componentViewResult.addToPageView = true;
                                    double width = 0.858; //mobile
                                    if (BaseView.isTablet(context))
                                        width = BaseView.isLandscape(context) ? 0.894 : 0.859;
                                    FrameLayout.LayoutParams param =
                                            new FrameLayout.LayoutParams((int) (BaseView.getDeviceWidth() * width),
                                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                    param.setMargins(BaseView.dpToPx(0), 0, 0, 0);
                                    param.gravity = Gravity.TOP | Gravity.END;
                                    componentViewResult.componentView.setLayoutParams(param);
                                    AppCMSBrowseCategoryAdapter appCMSBrowseCategoryAdapter = new AppCMSBrowseCategoryAdapter(context,
                                            this,
                                            appCMSPresenter,
                                            settings,
                                            parentLayout,
                                            false,
                                            component,
                                            jsonValueKeyMap,
                                            moduleAPI,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            viewType,
                                            appCMSAndroidModules, blockName, constrainteView);

                                    componentViewResult.useWidthOfScreen = true;
                                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSBrowseCategoryAdapter);
                                    if (pageView != null) {
                                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                                .adapter(appCMSBrowseCategoryAdapter)
                                                .listview((RecyclerView) componentViewResult.componentView)
                                                .id(moduleId + component.getKey())
                                                .build());
                                    }
                                } else if (CommonUtils.isTrayModule(uiBlockName) || CommonUtils.isRecommendationTrayModule(uiBlockName)) {
                                    AppCMSTrayViewAdapter appCMSTrayViewAdapter = new AppCMSTrayViewAdapter(context,
                                            this,
                                            settings,
                                            parentLayout,
                                            false,
                                            component,
                                            jsonValueKeyMap,
                                            moduleAPI,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            viewType,
                                            appCMSAndroidModules,
                                            blockName,
                                            true,
                                            constraintViewCreator);

                                    componentViewResult.useWidthOfScreen = true;
                                    ((RecyclerView) componentViewResult.componentView).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSTrayViewAdapter);

                                    if (pageView != null) {
                                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                                .adapter(appCMSTrayViewAdapter)
                                                .listview((RecyclerView) componentViewResult.componentView)
                                                .id(moduleId + component.getKey())
                                                .build());
                                    }
                                } else {
                                    appCMSViewAdapter = new AppCMSViewAdapter(context,
                                            this,
                                            settings,
                                            parentLayout,
                                            false,
                                            component,
                                            jsonValueKeyMap,
                                            moduleAPI,
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT,
                                            viewType,
                                            appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);
                                    componentViewResult.useWidthOfScreen = true;
                                    ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                                    if (pageView != null) {
                                        pageView.addListWithAdapter(new ListWithAdapter.Builder()
                                                .adapter(appCMSViewAdapter)
                                                .listview((RecyclerView) componentViewResult.componentView)
                                                .id(moduleId + component.getKey())
                                                .build());
                                    }
                                }
                            }
                        }
                    }
                    if (moduleAPI != null && (moduleAPI.getContentData() == null ||
                            moduleAPI.getContentData().isEmpty())) {
                        componentViewResult.shouldHideModule = true;
                    }
                }
                break;

            case PAGE_FIGHT_LIST_PARENT_MODULE_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParamsFightView =
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                componentViewResult.componentView.setLayoutParams(layoutParamsFightView);
                ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.VERTICAL);
                if (component.getBackgroundColor() != null && !TextUtils.isEmpty(component.getBackgroundColor())) {
                    componentViewResult.componentView.setBackgroundColor(Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                }
                break;

            case PAGE_VIDEO_PLAYER_VIEW_KEY: {
                String videoId = null;
                componentViewResult.componentView = new FrameLayout(context);
                componentViewResult.componentView.setId(R.id.video_player_id);
                if (moduleAPI != null &&
                        moduleAPI.getContentData() != null &&
                        !moduleAPI.getContentData().isEmpty() &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getId() != null) {
                    if (settings != null && settings.isShowTabs() && moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null &&
                            moduleAPI.getContentData().size() >= CommonUtils.getTabPosition() &&
                            moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null &&
                            moduleAPI.getContentData().get(CommonUtils.getTabPosition()).getGist() != null &&
                            moduleAPI.getContentData().get(CommonUtils.getTabPosition()).getGist().getId() != null) {
                        videoId = moduleAPI.getContentData().get(CommonUtils.getTabPosition()).getGist().getOriginalObjectId();
                        if (videoId == null) {
                            videoId = moduleAPI.getContentData().get(CommonUtils.getTabPosition()).getGist().getId();
                        }
                    } else {
                        videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                        if (videoId == null) {
                            videoId = moduleAPI.getContentData().get(0).getGist().getId();
                        }
                    }
                    CustomVideoPlayerView videoPlayerViewSingle = null;
                    if (appCMSPresenter.videoPlayerView != null)
                        appCMSPresenter.videoPlayerView.pausePlayer();
                    ContentDatum contentData = null;
                    if (moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null &&
                            moduleAPI.getContentData().size() > 0 &&
                            moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null) {
                        contentData = moduleAPI.getContentData().get(CommonUtils.getTabPosition());
                    }
                    if (appCMSPresenter.getVideoPlayerViewCache(moduleId + component.getKey()) != null) {
                        videoPlayerViewSingle = appCMSPresenter.getVideoPlayerViewCache(moduleId + component.getKey());
                        videoPlayerViewSingle.pausePlayer();
                    }
                    if (videoPlayerViewSingle != null) {
                        if (videoPlayerViewSingle.getParent() != null) {
                            ((ViewGroup) videoPlayerViewSingle.getParent()).removeView(videoPlayerViewSingle);
                        }
                        if (videoId != null && (!videoId.equalsIgnoreCase(appCMSPresenter.getLocalisedStrings().getContentNotAvailable())
                                && !videoPlayerViewSingle.getVideoId().equalsIgnoreCase(appCMSPresenter.getLocalisedStrings().getContentNotAvailable()))
                                && !videoPlayerViewSingle.getVideoId().equalsIgnoreCase(videoId) || contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), videoId)) {
                            videoPlayerViewSingle.setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, moduleAPI.getContentData().get(0));
                        } else if (CommonUtils.isPPVContent(context, moduleAPI.getContentData().get(0))) {
                            if (videoPlayerViewSingle.getVideoId().equalsIgnoreCase(videoId)) {
                                videoPlayerViewSingle.resumePlayerLastState();
                            } else {
                                videoPlayerViewSingle.setVideoUri(videoId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, moduleAPI.getContentData().get(0));
                            }
                        } else {
                            videoPlayerViewSingle.resumePlayerLastState();
                        }
                        if (moduleType == PAGE_VIDEO_PLAYLIST_MODULE_KEY) {
                            //Currently there Video Playlist Response is not coming  from backend so unable to fecth VideoID of first Item of VideoPlaylist. Due to which currently hardcoded a videoID.
                            if (moduleAPI != null && moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getVideoList() != null) {
                                //AutoPlay Condition
                                if (appCMSPresenter.getAutoplayEnabledUserPref(context)) {
                                    List<String> autoVideoPlayList = new ArrayList<>();

                                    for (int i = 0; i < moduleAPI.getContentData().get(0).getVideoList().size(); i++) {
                                        autoVideoPlayList.add(moduleAPI.getContentData().get(0).getVideoList().get(i).getGist().getId());
                                    }
                                    videoPlayerViewSingle.isFromPlayListPage = true;
                                    videoPlayerViewSingle.videoPlayListIds = autoVideoPlayList;
                                    appCMSPresenter.currentVideoPlayListIndex = 0;
                                }
                                moduleAPI.getContentData().get(1).getGist().setVideoPlaying(true);
                                videoPlayerViewSingle.setVideoUri(moduleAPI.getContentData().get(0).getVideoList().get(0).getGist().getId(), appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, moduleAPI.getContentData().get(0));
                            }
                        }
                    } else {
                        if (moduleType == PAGE_VIDEO_PLAYLIST_MODULE_KEY) {
                            if (moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getContentData().get(1) != null) {
                                moduleAPI.getContentData().get(1).getGist().setVideoPlaying(true);
                                videoId = moduleAPI.getContentData().get(0).getVideoList().get(0).getGist().getId();
                            }
                        }
                        videoPlayerViewSingle = playerView(context, videoId, moduleId + component.getKey(), appCMSPresenter, moduleAPI, settings);
                        appCMSPresenter.videoPlayerView = videoPlayerViewSingle;
                        if (appCMSPresenter.isHomePage(pageView.getPageId())) {
                            appCMSPresenter.videoPlayerViewHome = videoPlayerViewSingle;
                        }
                        if (uiBlockName == PAGE_VIDEO_PLAYLIST_MODULE_KEY && moduleAPI != null) {
                            List<String> autoVideoPlayList = new ArrayList<>();
                            for (int i = 0; i < moduleAPI.getContentData().get(0).getVideoList().size(); i++) {
                                autoVideoPlayList.add(moduleAPI.getContentData().get(0).getVideoList().get(i).getGist().getId());
                            }
                            videoPlayerViewSingle.isFromPlayListPage = true;
                            videoPlayerViewSingle.videoPlayListIds = autoVideoPlayList;
                            appCMSPresenter.currentVideoPlayListIndex = 0;
                        }
                    }
                    videoPlayerViewSingle.releasePreviousAdsPlayer();
                    videoPlayerViewSingle.setUseController(true);
                    videoPlayerViewSingle.enableController();
                    videoPlayerViewSingle.setEpisodePlay(false);
                    int viewWidth = BaseView.getDeviceWidth();

                    int viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
                    if (BaseView.isTablet(context) && BaseView.isLandscape(context)) {
                        viewHeight = (int) BaseView.getViewHeight(context, component.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
//                    FrameLayout.LayoutParams videoPlayerParentLP = new FrameLayout.LayoutParams(viewWidth, viewHeight);
//                    videoPlayerViewSingle.setLayoutParams(videoPlayerParentLP);
                    if (settings != null && settings.isShowTabs() && moduleAPI != null && moduleAPI.getContentData() != null &&
                            moduleAPI.getContentData().size() >= CommonUtils.getTabPosition() &&
                            moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null) {
                        //videoPlayerView.tabSelectedScreen.setVisibility(VISIBLE);
                        videoPlayerViewSingle.setOnUpdatedContentDatum(moduleAPI.getContentData().get(CommonUtils.getTabPosition()));
                    } else {
                        videoPlayerViewSingle.setOnUpdatedContentDatum(moduleAPI.getContentData().get(0));
                    }
                    ((FrameLayout) componentViewResult.componentView).addView(videoPlayerViewSingle);
                    appCMSPresenter.videoPlayerViewParent = (ViewGroup) componentViewResult.componentView;
                    appCMSPresenter.videoPlayerView = videoPlayerViewSingle;
                    if (appCMSPresenter.isHomePage(pageView.getPageId())) {
                        appCMSPresenter.videoPlayerViewHomeParent = (ViewGroup) componentViewResult.componentView;
                        appCMSPresenter.videoPlayerViewHome = videoPlayerViewSingle;
                    }
                    if (settings != null && settings.isShowPIP())
                        if (appCMSPresenter.relativeLayoutPIP == null) {
                            appCMSPresenter.relativeLayoutPIP = new MiniPlayerView(context, appCMSPresenter, pageView.findViewById(R.id.home_nested_scroll_view), pageId);
                        }
                    videoPlayerViewSingle.checkVideoStatus(videoId, moduleAPI.getContentData().get(0));
                    componentViewResult.componentView.setId(R.id.video_player_id);
                } else {
                    componentViewResult.componentView.setVisibility(View.GONE);
                    componentViewResult.shouldHideModule = true;
                    componentViewResult.shouldHideComponent = true;
                }
            }
            break;

            case PAGE_UPCOMING_TIMER_KEY: {
                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null) {
                    componentViewResult.componentView = new LinearLayout(context);
                    componentViewResult.componentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);
                    ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                    componentViewResult.componentView.setId(R.id.timer_id);

                    for (int count = 0; count < 4; count++) {
                        LinearLayout linearLayout = new LinearLayout(context);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.setMargins(3, 0, 3, 0);
                        linearLayout.setLayoutParams(params);
                        linearLayout.setGravity(Gravity.CENTER);
                        linearLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                        linearLayout.setAlpha(0.9f);
                        for (int textView = 0; textView < 2; textView++) {
                            TextView text = new TextView(context);
                            text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            text.setGravity(Gravity.CENTER);
                            text.setPadding(8, 0, 8, 0);
                            int textAppearance = textView == 0
                                    ? android.R.style.TextAppearance_Large
                                    : android.R.style.TextAppearance_Small;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                text.setTextAppearance(textAppearance);
                            } else {
                                text.setTextAppearance(context, textAppearance);
                            }
                            text.setTextColor(textView == 0
                                    ? appCMSPresenter.getGeneralTextColor()
                                    : (appCMSPresenter.getGeneralTextColor() + 0x92000000));
                            linearLayout.addView(text);
                        }
                        ((LinearLayout) componentViewResult.componentView).addView(linearLayout);
                    }
                    long eventDate = moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTime();
                    //calculate remaining time from event date and current date
                    long remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                    //if event date is greater than current date then start the timer
                    if (remainingTime > 0) {
                        stopCountdownTimer();
                        startTimerEvents(context, appCMSPresenter, eventDate, remainingTime);
                    } else {
                        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                            if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                                TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                                timerTile.setVisibility(View.GONE);
                            }
                            if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id) != null) {
                                LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                                linearLayout.setVisibility(View.GONE);
                            }
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                    }
                }
                /**
                 * if transaction response as data and pricing is not null and schedule start date is greater than show the timer
                 */
                else if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getPricing() != null &&
                        moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0) {
                    /**
                     * For svod+tvod or svod+ppv check if video purchased or use subscribed
                     * then show timer
                     */
                    boolean isTimerShow = true;
                    if ((moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {
                        isTimerShow = true;
                    } else if (((moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() > 0) ||
                            moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase("FREE"))) {
                        isTimerShow = true;
                    }
                    if (isTimerShow) {
                        componentViewResult.componentView = new LinearLayout(context);
                        componentViewResult.componentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);
                        ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        componentViewResult.componentView.setId(R.id.timer_id);

                        for (int count = 0; count < 4; count++) {
                            LinearLayout linearLayout = new LinearLayout(context);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            params.setMargins(10, 0, 10, 0);
                            linearLayout.setPadding(25, 0, 25, 0);

                            linearLayout.setLayoutParams(params);

                            linearLayout.setGravity(Gravity.CENTER);
                            linearLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                            linearLayout.setAlpha(0.9f);
                            for (int textView = 0; textView < 2; textView++) {
                                TextView text = new TextView(context);
                                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                text.setGravity(Gravity.CENTER);
                                text.setPadding(3, 0, 3, 0);
                                int textAppearance = textView == 0
                                        ? android.R.style.TextAppearance_Large
                                        : android.R.style.TextAppearance_Small;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    text.setTextAppearance(textAppearance);
                                } else {
                                    text.setTextAppearance(context, textAppearance);
                                }
                                text.setTextColor(textView == 0
                                        ? appCMSPresenter.getGeneralTextColor()
                                        : (appCMSPresenter.getGeneralTextColor() + 0x92000000));
                                linearLayout.addView(text);
                            }
                            ((LinearLayout) componentViewResult.componentView).addView(linearLayout);
                        }
                        long eventDate = (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate());
                        //calculate remaining time from event date and current date
                        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");
                        //if event date is greater than current date then start the timer
                        if (remainingTime > 0) {
                            stopCountdownTimer();
                            startTimer(context, appCMSPresenter, eventDate, remainingTime);
                        } else {
                            if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                                    TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                                    timerTile.setVisibility(View.GONE);
                                }
                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id) != null) {
                                    LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                                    linearLayout.setVisibility(View.GONE);
                                }
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }
                    }
                } else {
                    if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                            TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                            timerTile.setVisibility(View.GONE);
                        }
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id) != null) {
                            LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
            break;

            case PAGE_WEB_VIEW_KEY: {
                CustomWebView webView = null;
                componentViewResult.componentView = new FrameLayout(context);
                componentViewResult.componentView.setId(R.id.web_view_id);
                String url = "";
                if (moduleAPI.getRawText() != null) {
                    url = moduleAPI.getRawText();
                } else if (moduleAPI != null && moduleAPI.getContentData() != null
                        && moduleAPI.getContentData().get(0).getGist() != null
                        && moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                    url = moduleAPI.getContentData().get(0).getGist().getPermalink();
                }
                if (appCMSPresenter.getWebViewCache(moduleId + component.getKey() + url) != null) {
                    webView = appCMSPresenter.getWebViewCache(moduleId + component.getKey() + url);
                }
                if (mFbLiveView != null && componentViewResult.componentView != null && moduleType == PAGE_AC_WEB_FRAME_03_KEY) {
                    if (mFbLiveView.getParent() != null)
                        ((ViewGroup) mFbLiveView.getParent()).removeView(mFbLiveView);
                    ((FrameLayout) componentViewResult.componentView).addView(mFbLiveView);

                } else if (webView != null) {
                    if (webView.getParent() != null)
                        ((ViewGroup) webView.getParent()).removeView(webView);
                    ((FrameLayout) componentViewResult.componentView).addView(webView);
                } else {
                    webView = getWebViewComponent(context, moduleAPI, component, moduleId + component.getKey() + url, appCMSPresenter, moduleType);
                    ((FrameLayout) componentViewResult.componentView).addView(webView);
                }
            }
            break;
            case PAGE_ARTICLE_WEB_VIEW_KEY: {
                CustomWebView articleWebView = null;
                componentViewResult.componentView = new FrameLayout(context);
                String urlWeb = "";
                if (moduleAPI.getRawText() != null) {
                    urlWeb = moduleAPI.getRawText();
                } else if (moduleAPI != null && moduleAPI.getContentData() != null
                        && moduleAPI.getContentData().get(0).getGist() != null
                        && moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                    urlWeb = moduleAPI.getContentData().get(0).getGist().getPermalink();
                }
                if (appCMSPresenter.getWebViewCache(moduleId + component.getKey() + urlWeb) != null) {
                    articleWebView = appCMSPresenter.getWebViewCache(moduleId + component.getKey() + urlWeb);
                }
                if (articleWebView != null) {
                    if (articleWebView.getParent() != null)
                        ((ViewGroup) articleWebView.getParent()).removeView(articleWebView);
                    ((FrameLayout) componentViewResult.componentView).addView(articleWebView);
                } else {
                    articleWebView = getWebViewComponent(context, moduleAPI, component, moduleId + component.getKey() + urlWeb, appCMSPresenter, moduleType);
                    ((FrameLayout) componentViewResult.componentView).addView(articleWebView);
                    articleWebView.setId(R.id.article_web_view);
                }
            }
            break;
            case PAGE_BRANDS_CAROUSEL_KEY:
            case PAGE_CAROUSEL_VIEW_KEY:
                componentViewResult.componentView = new RecyclerView(context);
                ((RecyclerView) componentViewResult.componentView)
                        .setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL,
                                false));
                boolean loop = false;
                if (settings.isLoop()) {
                    loop = settings.isLoop();
                }
                if (viewType.equalsIgnoreCase(context.getResources().getString(R.string.app_cms_page_event_carousel_module_key))) {
                    component.setView(viewType);
                }
                AppCMSCarouselItemAdapter appCMSCarouselItemAdapter = new AppCMSCarouselItemAdapter(context,
                        this,
                        settings,
                        parentLayout,
                        component,
                        jsonValueKeyMap,
                        moduleAPI,
                        (RecyclerView) componentViewResult.componentView,
                        loop,
                        appCMSAndroidModules,
                        viewType,
                        blockName, constrainteView, constraintViewCreator);
                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSCarouselItemAdapter);
                if (pageView != null) {
                    pageView.addListWithAdapter(new ListWithAdapter.Builder()
                            .adapter(appCMSCarouselItemAdapter)
                            .listview((RecyclerView) componentViewResult.componentView)
                            .id(moduleId + component.getKey())
                            .build());
                }
                componentViewResult.onInternalEvent = appCMSCarouselItemAdapter;
                componentViewResult.onInternalEvent.setModuleId(moduleId);

                if (moduleAPI != null && (moduleAPI.getContentData() == null ||
                        moduleAPI.getContentData().isEmpty())) {
                    componentViewResult.shouldHideModule = true;
                }
                break;

            case PAGE_PAGE_CONTROL_VIEW_KEY:
                try {
                    long selectedColor = Long.parseLong(appCMSPresenter.getAppCMSMain().getBrand()
                                    .getCta()
                                    .getPrimary().getBackgroundColor().replace("#", ""),
                            16);
                    long deselectedColor = component.getUnSelectedColor() != null ?
                            Long.valueOf(component.getUnSelectedColor(), 16) : 0L;
                    deselectedColor = adjustColor1(deselectedColor, selectedColor);
                    componentViewResult.componentView = new DotSelectorView(context,
                            component,
                            0xff000000 + (int) selectedColor,
                            0xff000000 + (int) deselectedColor);
                    int numDots = moduleAPI != null ? moduleAPI.getContentData() != null ? moduleAPI.getContentData().size() : 0 : 0;
                    ((DotSelectorView) componentViewResult.componentView).addDots(numDots);
                    if (0 < numDots) {
                        ((DotSelectorView) componentViewResult.componentView).select(0);
                    }
                    componentViewResult.onInternalEvent = (DotSelectorView) componentViewResult.componentView;
                    componentViewResult.onInternalEvent.setModuleId(moduleId);
                    componentViewResult.useMarginsAsPercentagesOverride = false;

                    if (numDots <= 1) {
                        componentViewResult.componentView.setVisibility(View.GONE);
                    } else {
                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case PAGE_PAGE_CONTROL_VIEW_03_KEY:
                componentViewResult.componentView = new DotSelectorView03(context,
                        component,
                        Color.parseColor(CommonUtils.getColor(context, component.getSelectedColor())),
                        Color.parseColor(CommonUtils.getColor(context, component.getUnSelectedColor()))
                );

                Module pageControlModuleAPI = moduleAPI;
                if (moduleType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                    pageControlModuleAPI = getModuleAPI(pageControlModuleAPI);
                }
                int numDots1 = pageControlModuleAPI != null ? pageControlModuleAPI.getContentData() != null ? pageControlModuleAPI.getContentData().size() : 0 : 0;
                ((DotSelectorView03) componentViewResult.componentView).addDots(numDots1);
                if (0 < numDots1) {
                    ((DotSelectorView03) componentViewResult.componentView).select(0);
                }
                componentViewResult.onInternalEvent = (DotSelectorView03) componentViewResult.componentView;
                componentViewResult.onInternalEvent.setModuleId(moduleId);
                componentViewResult.useMarginsAsPercentagesOverride = false;

                if (numDots1 <= 1) {
                    componentViewResult.componentView.setVisibility(View.GONE);
                } else {
                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                }

                break;
            case PAGE_PAGE_CONTROL_VIEW_02_KEY:
                Module mApTimeBar = moduleAPI;
                mApTimeBar = removePastEventsAndShowNext7Days(mApTimeBar);
                componentViewResult.componentView = new TimeSelectorView(context,
                        component,
                        mApTimeBar,
                        jsonValueKeyMap,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        Color.GRAY);
                int numDot = moduleAPI != null ? moduleAPI.getContentData() != null ? moduleAPI.getContentData().size() : 0 : 0;
                if (0 < numDot) {
                    ((TimeSelectorView) componentViewResult.componentView).select(0);
                }
                componentViewResult.onInternalEvent = (TimeSelectorView) componentViewResult.componentView;
                componentViewResult.onInternalEvent.setModuleId(moduleId);
                componentViewResult.useMarginsAsPercentagesOverride = false;

                if (numDot <= 0) {
                    componentViewResult.componentView.setVisibility(View.GONE);
                } else {
                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                }

                break;
            case PAGE_TABLAYOUT_KEY:
                componentViewResult.componentView = new TabLayout(context);
                break;
            case PAGE_LINEAR_LAYOUT_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                break;
            case PAGE_WHATSAPP_CONSENT_CHECKBOX_KEY:
                componentViewResult.componentView = new AppCompatCheckBox(context);
                AppCompatCheckBox whatsappcheckBox = ((AppCompatCheckBox) componentViewResult.componentView);
                whatsappcheckBox.setId(R.id.whatsappConsentCheckbox);
                whatsappcheckBox.setVisibility(View.INVISIBLE);

                if (CommonUtils.isSiteOTPEnabled(appCMSPresenter) && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getWhatsAppConsent() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getWhatsAppConsent().isEnableWhatsappConsent()) {
                    WhatsAppConsent whatsAppConsent = appCMSPresenter.getAppCMSMain().getFeatures().getWhatsAppConsent();
                    whatsappcheckBox.setSingleLine(false);
                    whatsappcheckBox.setMaxLines(component.getNumberOfLines());
                    whatsappcheckBox.setEllipsize(TextUtils.TruncateAt.END);
                    whatsappcheckBox.setTextColor(appCMSPresenter.getGeneralTextColor());
                    if (whatsAppConsent.getWhatsappConsentMessage() != null) {
                        String formattedText = whatsAppConsent.getWhatsappConsentMessage();
                        ImageGetterFromHTMLText imageGetter = new ImageGetterFromHTMLText(whatsappcheckBox, appCMSPresenter.getCurrentContext());
                        Spannable htmlText;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            htmlText = (Spannable) Html.fromHtml(formattedText, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
                        } else {
                            htmlText = (Spannable) Html.fromHtml(formattedText, imageGetter, null);
                        }
                        whatsappcheckBox.setText(htmlText);
                    }
                    whatsappcheckBox.setChecked(whatsAppConsent.isWhatsappChecked());
                    whatsappcheckBox.setVisibility(View.VISIBLE);
                } else {
                    whatsappcheckBox.setVisibility(View.GONE);
                }

                int[][] statesWhatsApp = {{android.R.attr.state_checked}, {}};
                int[] colorsWhatsApp = {appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getGeneralTextColor()};
                CompoundButtonCompat.setButtonTintList(whatsappcheckBox, new ColorStateList(statesWhatsApp, colorsWhatsApp));

                break;
            case PAGE_EMAIL_CONSENT_CHECKBOX_KEY:
                componentViewResult.componentView = new AppCompatCheckBox(context);
                AppCompatCheckBox checkBox = ((AppCompatCheckBox) componentViewResult.componentView);
                checkBox.setId(R.id.emailConsentCheckbox);
                checkBox.setVisibility(View.INVISIBLE);

                String countryCode = Utils.getCountryCode();
                EmailConsent emailConsent = appCMSPresenter.getAppCMSMain().getEmailConsent();
                if (emailConsent != null && emailConsent.isEnableEmailConsent()) {
                    EmailConsentMessage emailConsentMessage = emailConsent.getLocalizationMap().get(countryCode);
                    if (!CommonUtils.isEmpty(emailConsent.getDefaultMessage()) || (emailConsentMessage != null && !CommonUtils.isEmpty(emailConsentMessage.getMessage()))) {
                        checkBox.setSingleLine(false);
                        checkBox.setMaxLines(component.getNumberOfLines());
                        checkBox.setEllipsize(TextUtils.TruncateAt.END);
                        (checkBox).setTextColor(appCMSPresenter.getGeneralTextColor());
                        checkBox.setText(emailConsent.getDefaultMessage());
                        checkBox.setChecked(emailConsent.isDefaultChecked());

                        ViewCreator.setTypeFace(context,
                                appCMSPresenter,
                                jsonValueKeyMap,
                                component,
                                componentViewResult.componentView);

                        if (component.getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                        } else if (BaseView.isTablet(appCMSPresenter.getCurrentContext()) && component.getLayout().getTabletPortrait().getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getLayout().getTabletPortrait().getFontSize());
                        } else if (component.getLayout().getMobile().getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getLayout().getMobile().getFontSize());
                        }
                        if (emailConsentMessage != null) {
                            checkBox.setChecked(emailConsentMessage.isChecked());
                            if (emailConsentMessage.getMessage() != null && !CommonUtils.isEmpty(emailConsentMessage.getMessage())) {
                                checkBox.setText(emailConsentMessage.getMessage());
                            }
                        }
                        checkBox.setVisibility(View.VISIBLE);
                    } else {
                        checkBox.setVisibility(View.GONE);
                    }
                } else {
                    checkBox.setVisibility(View.GONE);
                }
                int[][] states1 = {{android.R.attr.state_checked}, {}};
                int[] colors1 = {appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getGeneralTextColor()};
                CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states1, colors1));

                break;
            case PAGE_AGE_CONSENT_CHECKBOX_KEY:

                componentViewResult.componentView = new AppCompatCheckBox(context);
                AppCompatCheckBox agecheckBox = ((AppCompatCheckBox) componentViewResult.componentView);
                agecheckBox.setId(R.id.ageConsentCheckbox);

                agecheckBox.setSingleLine(false);
                agecheckBox.setMaxLines(component.getNumberOfLines());
                agecheckBox.setEllipsize(TextUtils.TruncateAt.END);
                (agecheckBox).setTextColor(appCMSPresenter.getGeneralTextColor());
                agecheckBox.setText(component.getText());
                agecheckBox.setVisibility(View.VISIBLE);
                if (component.getBackgroundColor() != null) {
                    agecheckBox.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                }

                ViewCreator.setTypeFace(context,
                        appCMSPresenter,
                        jsonValueKeyMap,
                        component,
                        componentViewResult.componentView);

                if (component.getFontSize() > 0) {
                    ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                } else if (BaseView.isTablet(appCMSPresenter.getCurrentContext()) && component.getLayout().getTabletPortrait().getFontSize() > 0) {
                    ((TextView) componentViewResult.componentView).setTextSize(component.getLayout().getTabletPortrait().getFontSize());
                } else if (component.getLayout().getMobile().getFontSize() > 0) {
                    ((TextView) componentViewResult.componentView).setTextSize(component.getLayout().getMobile().getFontSize());
                }

                int[][] states2 = {{android.R.attr.state_checked}, {}};
                int[] colors2 = {appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getGeneralTextColor()};
                CompoundButtonCompat.setButtonTintList(agecheckBox, new ColorStateList(states2, colors2));

                break;
            case PAGE_BUTTON_KEY:
                if (componentKey == PAGE_VIDEO_CLOSE_KEY ||
                        componentKey == PAGE_BACK_ARROW_KEY ||
                        componentKey == PAGE_VIDEO_SHARE_KEY ||
                        componentKey == PAGE_VIDEO_WHATSAPP_KEY ||
                        componentKey == PAGE_AUDIO_DOWNLOAD_BUTTON_KEY ||
                        componentKey == PAGE_PLAYLIST_DOWNLOAD_BUTTON_KEY ||
                        componentKey == PAGE_VIDEO_DOWNLOAD_BUTTON_KEY
                ) {
                    componentViewResult.componentView = new ResponsiveButton(context);
                } else if (componentKey != PAGE_BUTTON_SWITCH_KEY &&
                        componentKey != PAGE_CHECKBOX_KEY &&
                        componentKey != PAGE_ADD_TO_WATCHLIST_KEY &&
                        componentKey != PAGE_BOOKMARK_FLAG_KEY &&
                        componentKey != PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY &&
                        componentKey != PAGE_WATCHLIST_DELETE_ITEM_BUTTON &&
                        componentKey != PAGE_DELETE_HISTORY_KEY &&
                        componentKey != PAGE_DELETE_WATCHLIST_KEY &&
                        componentKey != PAGE_LAUNCH_LOGIN_BUTTON_KEY &&
                        componentKey != PAGE_DELETE_DOWNLOAD_KEY &&
                        componentKey != PAGE_BROWSE_ARROWS_BUTTON_KEY) {
                    componentViewResult.componentView = new Button(context);
                } else if (componentKey == PAGE_CHECKBOX_KEY) {
                    componentViewResult.componentView = new AppCompatCheckBox(context);
                } else if (componentKey == PAGE_BUTTON_SWITCH_KEY) {
                    componentViewResult.componentView = new Switch(context);
                } else {
                    componentViewResult.componentView = new ImageButton(context);
                }

                if (!gridElement) {
                    if (!TextUtils.isEmpty(component.getText()) && componentKey != PAGE_PLAY_KEY) {

                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                    } else if (moduleAPI != null && moduleAPI.getSettings() != null &&
                            !moduleAPI.getSettings().getHideTitle() &&
                            !TextUtils.isEmpty(moduleAPI.getTitle()) &&
                            componentKey != PAGE_BUTTON_SWITCH_KEY &&
                            componentKey != PAGE_CHECKBOX_KEY &&
                            componentKey != PAGE_BACK_ARROW_KEY &&
                            componentKey != PAGE_VIDEO_SHARE_KEY &&
                            componentKey != PAGE_VIDEO_CLOSE_KEY) {
                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                    }
                }

                if (!TextUtils.isEmpty(appCMSPresenter.getAppTextColor())) {
                    if (componentViewResult.componentView instanceof TextView) {
                        ((TextView) componentViewResult.componentView).setTextColor(
                                Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppTextColor())));
                    }
                }

                if (!TextUtils.isEmpty(appCMSPresenter.getAppBackgroundColor())) {
                    if (componentViewResult.componentView instanceof TextView) {
                        componentViewResult.componentView.setBackgroundColor(
                                Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppBackgroundColor())));
                    }
                }
                if (componentKey == BUTTON_EDIT_PROFILE || componentKey == BUTTON_CHANGE_PASSWORD
                        || componentKey == PAGE_SETTINGS_CANCEL_PLAN_PROFILE_KEY || componentKey == PAGE_SETTINGS_UPGRADE_PLAN_PROFILE_KEY
                        || componentKey == BUTTON_MANAGE_DOWNLOAD)
                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                if (appCMSPresenter.isActionFacebook(component.getAction())) {

                    if (appCMSPresenter.isLoginActionFacebook(component.getAction()) && moduleAPI != null
                            && metadataMap != null && metadataMap.getFacebookLoginCta() != null)
                        ((Button) componentViewResult.componentView).setText(metadataMap.getFacebookLoginCta().toUpperCase());
                    if (appCMSPresenter.isSignupActionFacebook(component.getAction()) && moduleAPI != null
                            && metadataMap != null && metadataMap.getFacebookSignUpCta() != null)
                        if (settings != null && settings.isHideSocialSignup()) {
                            ((Button) componentViewResult.componentView).setVisibility(View.GONE);
                        } else {
                            ((Button) componentViewResult.componentView).setVisibility(View.VISIBLE);
                            ((Button) componentViewResult.componentView).setText(metadataMap.getFacebookSignUpCta().toUpperCase());
                        }
                    applyBorderToComponent(context, componentViewResult.componentView, component,
                            ContextCompat.getColor(context, R.color.facebookBlue));
                    ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());

                    if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                            || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                        if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getLoginType() != null
                                && appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_facebook))) {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                    }

                } else if (appCMSPresenter.isActionGoogle(component.getAction())) {
                    if (appCMSPresenter.getAppCMSMain().getSocialMedia() != null &&
                            appCMSPresenter.getAppCMSMain().getSocialMedia().getGooglePlus() != null &&
                            appCMSPresenter.getAppCMSMain().getSocialMedia().getGooglePlus().isSignin()
                    ) {
                        if (appCMSPresenter.isActionSignUpGoogle(component.getAction()) && moduleAPI != null
                                && metadataMap != null && metadataMap.getGoogleSignUpCta() != null)
                            if (settings != null && settings.isHideSocialSignup()) {
                                ((Button) componentViewResult.componentView).setVisibility(View.GONE);
                            } else {
                                ((Button) componentViewResult.componentView).setVisibility(View.VISIBLE);
                                ((Button) componentViewResult.componentView).setText(metadataMap.getGoogleSignUpCta().toUpperCase());
                            }

                        if (appCMSPresenter.isActionSignInGoogle(component.getAction()) && moduleAPI != null
                                && metadataMap != null && metadataMap.getGoogleSignInCta() != null)
                            ((Button) componentViewResult.componentView).setText(metadataMap.getGoogleSignInCta().toUpperCase());

                        applyBorderToComponent(context, componentViewResult.componentView, component,
                                ContextCompat.getColor(context, R.color.googleRed));
                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());

                        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                                || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                            if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getLoginType() != null
                                    && appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_google))) {
                                componentViewResult.componentView.setVisibility(View.VISIBLE);
                            } else {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                } else if (moduleAPI != null && (jsonValueKeyMap.get(moduleAPI.getModuleType())
                        == PAGE_AUTOPLAY_MODULE_KEY_01 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) == PAGE_AUTOPLAY_MODULE_KEY_02 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) == PAGE_AUTOPLAY_MODULE_KEY_03 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) == PAGE_AUTOPLAY_MODULE_KEY_04 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) == PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY)
                        && componentKey == PAGE_DOWNLOAD_QUALITY_CANCEL_BUTTON_KEY
                        && component.getBorderWidth() != 0) {
                    ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                    applyBorderToComponent(
                            context,
                            componentViewResult.componentView,
                            component,
                            -1);
                } else {
                    if (componentKey != PAGE_SIGNUP_BUTTON && componentKey != BUTTON_EDIT_PROFILE && componentKey != BUTTON_CHANGE_PASSWORD
                            && componentKey != PAGE_SETTINGS_CANCEL_PLAN_PROFILE_KEY && componentKey != PAGE_SETTINGS_UPGRADE_PLAN_PROFILE_KEY &&
                            componentKey != BUTTON_MANAGE_DOWNLOAD) {
                        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null && appCMSPresenter.getAppCMSMain().getBrand().getCta() != null
                                && appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary() != null
                                && !TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand().getCta()
                                .getPrimary().getBackgroundColor())) {
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor(
                                    CommonUtils.getColor(context, appCMSPresenter.getAppCMSMain().getBrand()
                                            .getCta().getPrimary().getBackgroundColor())));

                            applyBorderToComponent(context, componentViewResult.componentView, component,
                                    Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                                            .getGeneral().getBlockTitleColor()));
                        } else {
                            applyBorderToComponent(context, componentViewResult.componentView, component, -1);
                        }
                    }
                }

                switch (componentKey) {
                    case PAGE_SETTINGS_UPGRADE_PLAN_PROFILE_KEY:
                        if (!appCMSPresenter.isUserSubscribed()) {
                            if (appCMSPresenter.isAppAVOD()) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                            if (metadataMap != null && metadataMap.getSubscribeNowButtonText() != null)
                                ((TextView) componentViewResult.componentView)
                                        .setText(metadataMap.getSubscribeNowButtonText());
                            else
                                ((TextView) componentViewResult.componentView)
                                        .setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_page_upgrade_subscribe_button_text)));
                        } else if (!appCMSPresenter.upgradesAvailableForUser()) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        } else {
                            if (metadataMap != null && metadataMap.getUpgradePlanButtonText() != null)
                                ((TextView) componentViewResult.componentView).setText(metadataMap.getUpgradePlanButtonText().toUpperCase());
                        }

                        String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();
                        if (paymentProcessor != null && paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_sslcommerz_payment_processor))) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(
                                    null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                    case BUTTON_EDIT_PROFILE:
                    case EDIT_PROFLE_PAGE:
                        if (metadataMap != null && metadataMap.getProfileText() != null)
                            ((TextView) componentViewResult.componentView).setText(metadataMap.getProfileText().toUpperCase());
                        if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                        appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                            componentViewResult.shouldHideComponent = true;
                        }

                        if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                        appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                            componentViewResult.shouldHideComponent = true;
                        }

                        if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }

                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(
                                    null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                    case BUTTON_CHANGE_PASSWORD:
                    case PAGE_SETTINGS_CHANGE_PASSWORD_KEY:
                        if (metadataMap != null && metadataMap.getChangePassword() != null)
                            ((TextView) componentViewResult.componentView).setText(metadataMap.getChangePassword().toUpperCase());
                        if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                        appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                            componentViewResult.shouldHideComponent = true;
                        }
                        if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                        appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                            componentViewResult.shouldHideComponent = true;
                        }

                        if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }

                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(
                                    null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                    case BUTTON_MANAGE_DOWNLOAD:
                        if (!appCMSPresenter.isDownloadable())
                            ((TextView) componentViewResult.componentView).setVisibility(View.GONE);
                        if (metadataMap != null && metadataMap.getChangeLabel() != null)
                            ((TextView) componentViewResult.componentView).setText(metadataMap.getChangeLabel().toUpperCase());
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                                appCMSPresenter.launchButtonSelectedAction(
                                        null,
                                        component.getAction(),
                                        null,
                                        extraData,
                                        null,
                                        false,
                                        0,
                                        null);
                            }
                        });
                        break;
                    case PAGE_BROWSE_CONCEPT_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.browseConcept);
                        componentViewResult.componentView.setVisibility(View.GONE);

                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralBackgroundColor());
                        PaintDrawable gdDefault = new PaintDrawable(appCMSPresenter.getGeneralTextColor());
                        gdDefault.setCornerRadius(25f);
                        componentViewResult.componentView.setBackground(gdDefault);
                        setTypeFace(context,
                                appCMSPresenter,
                                jsonValueKeyMap,
                                component,
                                componentViewResult.componentView);
                        componentViewResult.componentView.setOnClickListener(view -> appCMSPresenter.navigateToPage(appCMSPresenter.getConceptPage().getPageId(),
                                appCMSPresenter.getConceptPage().getPageFunction(),
                                appCMSPresenter.getConceptPage().getPageUI(),
                                false,
                                true,
                                false,
                                false,
                                false,
                                null));
                        break;
                    case PAGE_WATCH_LIVE_BUTTON_KEY:
                        if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&
                                moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null) {
                            componentViewResult.componentView.setId(R.id.watch_live_button);

                            long eventDate = moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTime();
                            long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                            if (remainingTime > 0) {
                                (componentViewResult.componentView).setVisibility(View.GONE);
                            } else if ((moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent().equalsIgnoreCase("1")) || (remainingTime <= 0 && moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent().equalsIgnoreCase("1"))) {
                                (componentViewResult.componentView).setBackgroundResource(R.drawable.watch_live_button);
                                ((Button) componentViewResult.componentView).setGravity(Gravity.CENTER);
                            } else {
                                (componentViewResult.componentView).setVisibility(View.GONE);
                            }
                        } else {
                            (componentViewResult.componentView).setVisibility(View.GONE);
                        }
                        (componentViewResult.componentView).setOnClickListener(view -> {
                            String url = "";
                            if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getExternalUrl() != null) {
                                url = moduleAPI.getContentData().get(0).getGist().getExternalUrl();
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            appCMSPresenter.getCurrentActivity().startActivity(browserIntent);
                        });
                        break;
                    case PAGE_PHOTOGALLERY_PRE_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.photo_gallery_prev_button);
                        componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                        ((Button) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        componentViewResult.componentView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                        componentViewResult.componentView.setEnabled(false);
                        componentViewResult.componentView.setOnClickListener(view -> {
                            if (photoGalleryNextPreviousListener != null) {
                                pageView.findChildViewById(R.id.photo_gallery_next_button).setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                pageView.findChildViewById(R.id.photo_gallery_next_button).setEnabled(true);
                                photoGalleryNextPreviousListener.previousPhoto(((Button) view));
                            }
                        });
                        break;
                    case PAGE_PHOTOGALLERY_NEXT_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.photo_gallery_next_button);
                        componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                        ((Button) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        if (moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets() == null || moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size() == 0) {
                            componentViewResult.componentView.setEnabled(false);
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                        }
                        componentViewResult.componentView.setOnClickListener(v -> {
                            if (photoGalleryNextPreviousListener != null) {
                                enablePhotoGalleryButtons(true, true, pageView, appCMSPresenter, "1");
                                photoGalleryNextPreviousListener.nextPhoto(((Button) v));

                            }
                        });
                        break;

                    case PAGE_CHECKBOX_KEY:
                        AppCompatCheckBox checkBoxTCP = ((AppCompatCheckBox) componentViewResult.componentView);
                        checkBoxTCP.setChecked(false);
                        checkBoxTCP.setId(R.id.appCMS_tcp_check);
                        if (component.getText() != null) {
                            // checkBoxTCP.setText(component.getText());
                            (checkBoxTCP).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());

                        }
                        if (component.getBackgroundColor() != null) {
                            checkBoxTCP.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                        }
                        int[][] states = {{android.R.attr.state_checked}, {}};
                        int[] colors = {appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getGeneralTextColor()};
                        CompoundButtonCompat.setButtonTintList(checkBoxTCP, new ColorStateList(states, colors));
                        break;
                    case PAGE_BUTTON_SWITCH_KEY:
                        if (appCMSPresenter.isPreferredStorageLocationSDCard()) {
                            ((Switch) componentViewResult.componentView).setChecked(true);
                        } else {
                            ((Switch) componentViewResult.componentView).setChecked(false);
                        }

                        ((Switch) componentViewResult.componentView).setOnCheckedChangeListener((buttonView, isChecked) -> {
                            if (isChecked) {
                                if (FileUtils.isRemovableSDCardAvailable(appCMSPresenter.getCurrentActivity())) {
                                    appCMSPresenter.setPreferredStorageLocationSDCard(true);
                                } else {
                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SD_CARD_NOT_AVAILABLE, null, false, null, null, null);
                                    buttonView.setChecked(false);
                                }
                            } else {
                                appCMSPresenter.setPreferredStorageLocationSDCard(false);
                            }
                        });
                        break;


                    case PAGE_AUTOPLAY_BACK_KEY:
                        componentViewResult.componentView.setVisibility(View.GONE);
                        break;

                    case PAGE_INFO_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.info_icon));
                        break;
                    case PAGE_WATCHLIST_DELETE_ITEM_BUTTON:
                    case PAGE_DELETE_DOWNLOAD_KEY:
                    case PAGE_DELETE_WATCHLIST_KEY:
                    case PAGE_DELETE_HISTORY_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_deleteicon));
                        componentViewResult.componentView.getBackground().setTint(tintColor);
                        componentViewResult.componentView.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        break;

                    case PAGE_BOOKMARK_FLAG_DELETE_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_bookmark_selected));
                        break;

                    case PAGE_RIGHT_ARROW_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_right_arrow));
                        break;

                    case PAGE_GRID_OPTION_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.dots_more));
                        componentViewResult.componentView.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
                        componentViewResult.componentView.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        appCMSPresenter.setMoreIconAvailable();
                        if (metadataMap != null)
                            appCMSPresenter.setMetadataMap(metadataMap);
                        componentViewResult.componentView.setOnClickListener(view -> {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                                moduleAPI.getContentData().get(0).setModuleApi(moduleAPI);
                                appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getGist().getPermalink(),
                                        component.getAction(),
                                        moduleAPI.getContentData().get(0).getGist().getTitle(),
                                        null,
                                        moduleAPI.getContentData().get(0),
                                        false,
                                        -1,
                                        null);
                            }
                        });
                        break;
                    case PAGE_BANNER_DETAIL_BUTTON:
                        componentViewResult.componentView.setBackground(context.getDrawable(R.drawable.dots_more));
                        componentViewResult.componentView.setId(View.generateViewId());
                        if (settings != null && settings.getImage() == null && settings.getLinks() == null && settings.getSocialLinks() == null) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        if (settings.getLinks() != null || settings.getSocialLinks() != null) {
                            if (settings.getLinks().size() < 1 || settings.getSocialLinks().size() < 1) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }
                        componentViewResult.componentView.setOnClickListener(view -> {
                            if (settings != null) {
                                appCMSPresenter.showPopUpMenuSports(settings.getLinks(), settings.getSocialLinks());
                            }
                        });
                        break;
                    case PAGE_PLAYLIST_DOWNLOAD_BUTTON_KEY:
                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.ic_download_big);
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        componentViewResult.componentView.setId(R.id.playlist_download_id);
                        if (moduleAPI != null && moduleAPI.getContentData() != null && appCMSPresenter.isAllPlaylistAudioDownloaded(moduleAPI.getContentData())) {
                            ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.ic_downloaded);
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        componentViewResult.componentView.setOnClickListener(view -> {
                            if (!appCMSPresenter.isNetworkConnected()) {
                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null,
                                        false,
                                        null,
                                        null, null);
                                return;
                            }
                            if (!appCMSPresenter.isUserLoggedIn()) {
                                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_AND_SUBSCRIPTION_REQUIRED_AUDIO,
                                        () -> {
                                            appCMSPresenter.setAfterLoginAction(() -> {
                                            });
                                        }, null);
                            } else if (!appCMSPresenter.isUserSubscribed()) {
                                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_REQUIRED_AUDIO,
                                        () -> {
                                            appCMSPresenter.setAfterLoginAction(() -> {
                                            });
                                        }, null);
                            } else {
                                if (!appCMSPresenter.isAllPlaylistAudioDownloaded(moduleAPI.getContentData())) {

                                    if (!appCMSPresenter.getDownloadOverCellularEnabled() && appCMSPresenter.getActiveNetworkType() == ConnectivityManager.TYPE_MOBILE) {
                                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_VIA_MOBILE_DISABLED,
                                                appCMSPresenter.getLocalisedStrings().getCellularDisableText(),
                                                false,
                                                null,
                                                null, null);
                                        return;
                                    }
                                    appCMSPlaylistAdapter.startDownloadPlaylist();

                                }
                            }
                        });
                        break;
                    case PAGE_AUDIO_DOWNLOAD_BUTTON_KEY:
                    case PAGE_VIDEO_DOWNLOAD_BUTTON_KEY:
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        componentViewResult.componentView.setBackgroundResource(android.R.color.transparent);
                        if (!gridElement &&
                                moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null) {
                            String userId = appCMSPresenter.getLoggedInUser();
                            int radiusDifference = 7;
                            if (BaseView.isTablet(context)) {
                                radiusDifference = 4;
                            }
                            if (moduleAPI.getContentData().get(0).getGist().getMediaType() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.media_type_audio).toLowerCase())) {
                                radiusDifference = 5;
                                if (BaseView.isTablet(context)) {
                                    radiusDifference = 3;
                                }
                            }
                            componentViewResult.componentView.setTag(moduleAPI.getContentData().get(0).getGist().getId());

                            /**
                             * if video is not purchased then hide download option
                             */
                            if ((moduleAPI.getContentData().get(0).getPricing() != null &&
                                    moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                                    (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_TVOD)) || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_PPV))))) {
                                if (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0) {
                                    if (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() == 0) {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                        break;
                                    }
                                }
                            }
                            /**
                             * if schedule start date is not 0 and greater than current time Or
                             *  End date is expired then hide download button as video is not started
                             */

                            if (!ViewCreator.isScheduleContentVisible(moduleAPI.getContentData().get(0), appCMSPresenter)) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                                break;

                            } else if (ViewCreator.isVideoIsSchedule(moduleAPI.getContentData().get(0), appCMSPresenter)) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                                break;
                            }
                            if (appCMSPresenter.isDownloadEnable()) {
                                moduleAPI.getContentData().get(0).setModuleApi(moduleAPI);
                                if (moduleAPI.getContentData().get(0).isDRMEnabled()) {
                                    appCMSPresenter.getUserOfflineVideoDownloadStatus(moduleAPI.getContentData().get(0).getGist().getId(), new FetchOffineDownloadStatus((ImageButton) componentViewResult.componentView, appCMSPresenter,
                                            moduleAPI.getContentData().get(0), userId, radiusDifference,
                                            moduleAPI.getId()), userId);
                                } else {
                                    appCMSPresenter.getUserVideoDownloadStatus(moduleAPI.getContentData().get(0).getGist().getId(), new UpdateDownloadImageIconAction((ImageButton) componentViewResult.componentView, appCMSPresenter,
                                            moduleAPI.getContentData().get(0), userId, radiusDifference,
                                            moduleAPI.getId()), userId);
                                }
                                componentViewResult.componentView.setVisibility(View.VISIBLE);
                            } else {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().size() > 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                                moduleAPI.getContentData().get(0).getStreamingInfo().isLiveStream()) {
                            componentViewResult.componentView.setVisibility(View.INVISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                        }
                        break;

                    case PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY:
                    case PAGE_ADD_TO_WATCHLIST_KEY:
                        componentViewResult.componentView.setBackgroundResource(android.R.color.transparent);
                        List<String> filmIds = new ArrayList<>();
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null) {

                            filmIds.add(moduleAPI.getContentData().get(0).getGist().getId());
                        }
                        boolean filmsAdded = true;
                        for (String filmId : filmIds) {
                            if (componentKey == PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY) {
                                filmsAdded &= appCMSPresenter.isFilmAddedToFollowlist(filmId);
                            } else {
                                filmsAdded &= appCMSPresenter.isFilmAddedToWatchlist(filmId);
                            }
                        }
                        if (moduleAPI != null && moduleAPI.getContentData() != null) {
                            UpdateImageIconAction updateImageIconAction =
                                    new UpdateImageIconAction((ImageButton) componentViewResult.componentView,
                                            appCMSPresenter,
                                            filmIds,
                                            moduleAPI.getContentData().get(0),
                                            componentKey, metadataMap, component.getTintColor());
                            updateImageIconAction.updateWatchlistResponse(filmsAdded);
                        }
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                                moduleAPI.getContentData().get(0).getStreamingInfo().isLiveStream()) {
                            componentViewResult.componentView.setVisibility(View.INVISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                        }
                        if ((!appCMSPresenter.getAppCMSMain().getFeatures().isLoginModuleEnable() &&
                                !appCMSPresenter.getAppCMSMain().getFeatures().isSignupModuleEnable()) ||
                                appCMSPresenter.getAppCMSMain().getFeatures().isWatchlistHidden())
                            componentViewResult.componentView.setVisibility(View.INVISIBLE);
                        break;

                    case PAGE_VIDEO_WATCH_TRAILER_KEY:
                        if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().get(0) != null) {
                            if (metadataMap != null && metadataMap.getWatchTrailerCTA() != null) {
                                ((Button) componentViewResult.componentView).setText(metadataMap.getWatchTrailerCTA());
                                applyBorderToComponent(
                                        context,
                                        componentViewResult.componentView,
                                        component,
                                        appCMSPresenter.getGeneralTextColor());
                                ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                            }
                            if (moduleAPI.getContentData().get(0).getContentDetails() != null &&
                                    moduleAPI.getContentData().get(0).getContentDetails().getTrailers() != null &&
                                    !moduleAPI.getContentData().get(0).getContentDetails().getTrailers().isEmpty() &&
                                    moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getPermalink() != null &&
                                    moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getId() != null /*&&
                                    moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getVideoAssets() != null*/) {
                                final String watchTrailerAction = component.getAction();
                                componentViewResult.componentView.setOnClickListener(v -> {
                                    String[] extraData = new String[3];
                                    extraData[0] = moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getPermalink();
                                    extraData[2] = moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getId();
                                    appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getPermalink(),
                                            watchTrailerAction,
                                            moduleAPI.getContentData().get(0).getGist().getTitle(),
                                            extraData,
                                            moduleAPI.getContentData().get(0),
                                            false,
                                            -1,
                                            null);
                                });
                            } else if (moduleAPI.getContentData().get(0).getShowDetails() != null &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers() != null &&
                                    !moduleAPI.getContentData().get(0).getShowDetails().getTrailers().isEmpty() &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getPermalink() != null &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId() != null /*&&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getVideoAssets() != null*/) {
                                final String watchTrailerAction = component.getAction();

                                componentViewResult.componentView.setOnClickListener(v -> {
                                    String[] extraData = new String[3];
                                    extraData[0] = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getPermalink();
                                    extraData[2] = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId();
                                    appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getPermalink(),
                                            watchTrailerAction,
                                            moduleAPI.getContentData().get(0).getGist().getTitle(),
                                            extraData,
                                            moduleAPI.getContentData().get(0),
                                            false,
                                            -1,
                                            null);
                                });
                            } else {
                                componentViewResult.shouldHideComponent = true;
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        } else {
                            componentViewResult.shouldHideComponent = true;
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        break;
                    case PAGE_VIDEO_DETAIL_CLASS_PLAY_BUTTON_KEY:
                    case PAGE_VIDEO_PLAY_BUTTON_KEY:
                        componentKeyVideoPlayButton(moduleAPI, componentKey, context,
                                tintColor, appCMSPresenter, component);
                        break;

                    case PAGE_PLAY_IMAGE1_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.neo_video_detail_play));
                        componentViewResult.componentView.getBackground().setTint(tintColor);
                        componentViewResult.componentView.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        componentViewResult.componentView.setVisibility(View.VISIBLE);

                        if (moduleId != null && moduleId.contains(context.getResources().getString(R.string.app_cms_page_brand_carousel_key))) {
                            if (moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getShowDetails() != null &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers() != null &&
                                    !moduleAPI.getContentData().get(0).getShowDetails().getTrailers().isEmpty() &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId() != null) {
                                componentViewResult.componentView.setVisibility(View.VISIBLE);
                            } else {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        } else {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                        }
                        break;


                    case PAGE_BPM_ICON_KEY:
                        componentViewResult.componentView.setBackground(context.getDrawable(R.drawable.ic_activity_squiet));
                        break;

                    case PAGE_FITNESS_TIME_ICON_KEY:
                        componentViewResult.componentView.setBackground(context.getDrawable(R.drawable.ic_clock));
                        componentViewResult.componentView.getBackground().setTint(tintColor);
                        componentViewResult.componentView.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        break;

                    case PAGE_PLAY_KEY:
                    case PAGE_PLAY_IMAGE_KEY:
                        componentViewResult.componentView.setPadding(40, 40, 40, 40);
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                        componentViewResult.componentView.getBackground().setTint(getTransparentColor(tintColor, 0.75f));
                        break;

                    case PAGE_PLAY_LIVE_IMAGE_KEY:
                        componentViewResult.componentView.setVisibility(View.GONE);
                        break;

                    case PAGE_BACK_ARROW_KEY:
                    case PAGE_VIDEO_CLOSE_KEY:
                        if (componentKey == PAGE_BACK_ARROW_KEY) {
                            ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.ic_back_arrow);
                        } else {
                            ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.cancel);
                        }
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        int width = (int) context.getResources().getDimension(R.dimen.close_button_size);
                        int height = (int) context.getResources().getDimension(R.dimen.close_button_size);
                        int leftMargin = (int) context.getResources().getDimension(R.dimen.close_button_margin);
                        int topMargin = (int) context.getResources().getDimension(R.dimen.close_button_margin);
                        int rightMargin = (int) context.getResources().getDimension(R.dimen.close_button_margin);
                        int bottomMargin = (int) context.getResources().getDimension(R.dimen.close_button_margin);

                        ViewGroup.MarginLayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                        layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                        componentViewResult.componentView.setLayoutParams(layoutParams);

                        int fillColor = appCMSPresenter.getGeneralTextColor();
                        ((ImageButton) componentViewResult.componentView).getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                        final String closeAction = component.getAction();

                        componentViewResult.componentView.setOnClickListener(v -> {
                            if (appCMSPresenter.getCurrentActivity() != null)
                                appCMSPresenter.getCurrentActivity().onBackPressed();
                            else
                                appCMSPresenter.launchButtonSelectedAction(null,
                                        closeAction,
                                        null,
                                        null,
                                        null,
                                        false,
                                        0,
                                        null);
                        });

                        if (blockName.contains("richText01")
                                && appCMSPresenter.getCurrentActivity() instanceof AppCMSPageActivity
                                && ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getAppCMSBinderMap() != null
                                && ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getAppCMSBinderMap().size() > 0
                                && ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getAppCMSBinderMap().get(pageView.getPageId()) != null) {
                            AppCMSBinder appCMSBinder = ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getAppCMSBinderMap().get(pageView.getPageId());
                            if (appCMSBinder != null && appCMSBinder.isAppbarPresent()) {
                                ((ImageButton) componentViewResult.componentView).setVisibility(View.GONE);
                            }

                        }

                        break;

                    case PAGE_VIDEO_CAST_KEY:

                        componentViewResult.componentView = new LinearLayout(context);
                        ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);

                        ImageButton mMediaRouteButton = appCMSPresenter.getCurrentMediaRouteButton();
                        if (mMediaRouteButton != null) {
                            LinearLayout.LayoutParams mMediaRouteButtonLayoutParams =
                                    new LinearLayout.LayoutParams((int) BaseView.convertDpToPixel(28, context),
                                            (int) BaseView.convertDpToPixel(28, context));
                            mMediaRouteButtonLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                            mMediaRouteButton.setLayoutParams(mMediaRouteButtonLayoutParams);
                            mMediaRouteButton.setPadding(8, 8, 8, 8);
                            mMediaRouteButton.setBackgroundResource(android.R.color.transparent);
                            boolean allowFreePlay = !appCMSPresenter.isAppSVOD();
                            setCasting(allowFreePlay, //** TODO: Replace with actual value from API response *//*
                                    appCMSPresenter,
                                    mMediaRouteButton,
                                    moduleAPI.getContentData().get(0).getGist().getWatchedTime());

                            pageView.setReparentChromecastButton(false);

                            if (mMediaRouteButton.getParent() != null &&
                                    mMediaRouteButton.getParent() instanceof ViewGroup) {
                                ((ViewGroup) mMediaRouteButton.getParent()).removeView(mMediaRouteButton);
                            }

                            ((LinearLayout) componentViewResult.componentView).addView(mMediaRouteButton);
                        }
                        componentViewResult.componentView.requestLayout();


                        break;
                    case PAGE_VIDEO_SHARE_KEY:
                        ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.ic_share);
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        ((ImageButton) componentViewResult.componentView).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.SRC_IN));
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                        final String shareAction = component.getAction();

                        componentViewResult.componentView.setOnClickListener(v -> {
                            AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
                            if (appCMSMain != null &&
                                    moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    !moduleAPI.getContentData().isEmpty() &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getTitle() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                                StringBuilder filmUrl = new StringBuilder();
                                filmUrl.append(appCMSMain.getDomainName());
                                filmUrl.append(moduleAPI.getContentData().get(0).getGist().getPermalink());
                                String[] extraData = new String[1];
                                extraData[0] = filmUrl.toString();
                                appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getGist().getPermalink(),
                                        shareAction,
                                        moduleAPI.getContentData().get(0).getGist().getTitle(),
                                        extraData,
                                        moduleAPI.getContentData().get(0),
                                        false,
                                        0,
                                        null);
                            }
                        });
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null &&
                                moduleAPI.getContentData().get(0).getGist().getContentType() != null &&
                                moduleAPI.getContentData().get(0).getGist().getContentType().equalsIgnoreCase("AUDIO")) {
                            componentViewResult.componentView.setVisibility(View.GONE);

                        }
                        if (appCMSPresenter != null && (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        break;
                    case PAGE_VIDEO_WHATSAPP_KEY: {
                        ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.ic_whatsapp);
                        ((ImageButton) componentViewResult.componentView).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.SRC_IN));
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                        componentViewResult.componentView.setOnClickListener(v -> {
                            AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
                            if (appCMSMain != null &&
                                    moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    !moduleAPI.getContentData().isEmpty() &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getTitle() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                                String[] extraData = new String[1];
                                String filmUrl = appCMSMain.getDomainName() + moduleAPI.getContentData().get(0).getGist().getPermalink();
                                extraData[0] = filmUrl;
                                appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getGist().getPermalink(),
                                        component.getAction(),
                                        moduleAPI.getContentData().get(0).getGist().getTitle(),
                                        extraData,
                                        moduleAPI.getContentData().get(0),
                                        false,
                                        0,
                                        null);
                            }
                        });
                        if (!CommonUtils.isAppInstalled(context, CommonUtils.WHATSAPPP_PACKAGE_NAME)
                                || !appCMSPresenter.isHoichoiApp()
                                || (moduleAPI.getContentData().get(0) != null && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getContentType() != null
                                && moduleAPI.getContentData().get(0).getGist().getContentType().equalsIgnoreCase("AUDIO"))) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }

                    } // end of case PAGE_VIDEO_WHATSAPP_KEY:
                    break;
                    case PAGE_FORGOTPASSWORD_KEY:
                        if (metadataMap != null && metadataMap.getForgotPasswordCtaText() != null)
                            ((Button) componentViewResult.componentView).setText(metadataMap.getForgotPasswordCtaText());
                        componentViewResult.componentView.setBackgroundColor(
                                ContextCompat.getColor(context, android.R.color.transparent));
                        ((Button) componentViewResult.componentView)
                                .setTextSize(12);
                        ((Button) componentViewResult.componentView)
                                .setTextColor(appCMSPresenter.getGeneralTextColor());
                        break;

                    case PAGE_REMOVEALL_KEY:
                        componentViewResult.addToPageView = true;
                        FrameLayout.LayoutParams removeAllLayoutParams =
                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        removeAllLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

                        switch (jsonValueKeyMap.get(viewType)) {
                            case PAGE_HISTORY_01_MODULE_KEY:
                            case PAGE_HISTORY_02_MODULE_KEY:
                                if (metadataMap != null && metadataMap.getRemoveAllHistoryLabel() != null)
                                    ((Button) componentViewResult.componentView).setText(metadataMap.getRemoveAllHistoryLabel());
                                break;

                            case PAGE_DOWNLOAD_01_MODULE_KEY:
                            case PAGE_DOWNLOAD_02_MODULE_KEY:
                                if (metadataMap != null && metadataMap.getRemoveAllDownloadCta() != null)
                                    ((Button) componentViewResult.componentView).setText(metadataMap.getRemoveAllDownloadCta());
                                break;

                            case PAGE_WATCHLIST_01_MODULE_KEY:
                            case PAGE_WATCHLIST_02_MODULE_KEY:
                                if (metadataMap != null && metadataMap.getRemoveAllWatchlistLabel() != null)
                                    ((Button) componentViewResult.componentView).setText(metadataMap.getRemoveAllWatchlistLabel());
                                break;

                            default:
                                break;
                        }
                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                        componentViewResult.componentView.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
//                        componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        componentViewResult.componentView.setLayoutParams(removeAllLayoutParams);
                        componentViewResult.componentView.setId(R.id.remove_all_download_id);
                        componentViewResult.onInternalEvent = new OnRemoveAllInternalEvent(moduleId,
                                componentViewResult.componentView);
                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            OnInternalEvent onInternalEvent = componentViewResult.onInternalEvent;

                            @Override
                            public void onClick(final View v) {
                                boolean deleteAllFiles = true;
                                appCMSPresenter.showLoadingDialog(true);

                                switch (jsonValueKeyMap.get(viewType)) {
                                    case PAGE_HISTORY_01_MODULE_KEY:
                                    case PAGE_HISTORY_02_MODULE_KEY:
                                        appCMSPresenter.clearHistory(appCMSDeleteHistoryResult -> {
                                            onInternalEvent.sendEvent(null);
                                            v.setVisibility(View.GONE);
                                            appCMSPresenter.showLoadingDialog(false);
                                        }, moduleAPI);
                                        break;

                                    case PAGE_DOWNLOAD_01_MODULE_KEY:
                                    case PAGE_DOWNLOAD_02_MODULE_KEY:
                                        appCMSPresenter.clearDownload(appCMSDownloadStatusResult -> {
                                            onInternalEvent.sendEvent(null);
                                            v.setVisibility(View.GONE);
                                            appCMSPresenter.showLoadingDialog(false);

                                        }, deleteAllFiles, moduleAPI);
                                        break;

                                    case PAGE_WATCHLIST_01_MODULE_KEY:
                                    case PAGE_WATCHLIST_02_MODULE_KEY:
                                        appCMSPresenter.clearWatchlist(appCMSAddToWatchlistResult -> {
                                            onInternalEvent.sendEvent(null);
                                            v.setVisibility(View.GONE);
                                            appCMSPresenter.showLoadingDialog(false);
                                        }, moduleAPI);
                                        break;

                                    default:
                                        break;
                                }
                            }
                        });
                        break;

                    case PAGE_ARTICLE_PREVIOUS_BUTTON_KEY:
                        componentViewResult.addToPageView = true;
                        FrameLayout.LayoutParams paramsPreviousButton =
                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsPreviousButton.setMargins(20, 0, 0, 0);
                        paramsPreviousButton.gravity = Gravity.BOTTOM | Gravity.START;
                        View previousButtonView = componentViewResult.componentView;
                        previousButtonView.setLayoutParams(paramsPreviousButton);
                        previousButtonView.setPadding(20, 0, 20, 0);
                        previousButtonView.setId(R.id.article_prev_button);
                        ((Button) previousButtonView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());

                        if (appCMSPresenter.getCurrentArticleIndex() < 0) {
                            previousButtonView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                        } else {
                            previousButtonView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        }
                        previousButtonView.setOnClickListener(v -> {
                            if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getContentDetails() != null &&
                                    moduleAPI.getContentData().get(0).getContentDetails().getRelatedArticleIds() != null &&
                                    appCMSPresenter.getRelatedArticleIds() != null) {
                                int currentIndex = appCMSPresenter.getCurrentArticleIndex();
                                currentIndex = currentIndex - 1;
                                if (currentIndex < -1) {
                                    return;
                                }
                                appCMSPresenter.setCurrentArticleIndex(currentIndex);
                                appCMSPresenter.navigateToArticlePage(
                                        appCMSPresenter.getRelatedArticleIds().get(currentIndex + 1),
                                        moduleAPI.getContentData().get(0).getGist().getTitle(),
                                        false,
                                        () -> {
                                            if (appCMSPresenter.getCurrentArticleIndex() < 0) {
                                                previousButtonView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                                                previousButtonView.setEnabled(false);
                                            } else {
                                                previousButtonView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                                previousButtonView.setEnabled(true);
                                            }
                                            CustomWebView customWebView = pageView.findViewById(R.id.article_web_view);
                                            if (customWebView != null) {
                                                customWebView.loadUrl("about:blank");
                                            }
                                        }, false);
                            }
                        });

                        break;
                    case PAGE_BOTTOM_BACKGROUND_ARTICLE_KEY:
                        componentViewResult.addToPageView = true;
                        FrameLayout.LayoutParams bottomBg =
                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT);
                        bottomBg.gravity = Gravity.BOTTOM;
                        componentViewResult.componentView.setLayoutParams(bottomBg);
                        componentViewResult.componentView.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                        break;
                    case PAGE_ARTICLE_NEXT_BUTTON_KEY:
                        componentViewResult.addToPageView = true;

                        FrameLayout.LayoutParams paramsNextButton =
                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsNextButton.setMargins(0, 0, 20, 0);
                        paramsNextButton.gravity = Gravity.BOTTOM | Gravity.END;
                        componentViewResult.componentView.setLayoutParams(paramsNextButton);
                        componentViewResult.componentView.setPadding(30, 0, 30, 0);
                        componentViewResult.componentView.setId(R.id.article_next_button);
                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                        componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getContentDetails() != null &&
                                moduleAPI.getContentData().get(0).getContentDetails().getRelatedArticleIds() != null &&
                                appCMSPresenter.getRelatedArticleIds() != null) {

                            List<String> articleIDs = appCMSPresenter.getRelatedArticleIds();
                            if (appCMSPresenter.getCurrentArticleIndex() == appCMSPresenter.getRelatedArticleIds().size() - 2) {
                                componentViewResult.componentView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                                componentViewResult.componentView.setEnabled(false);
                            }
                            if (appCMSPresenter.getCurrentArticleIndex() == -1) {
                                articleIDs.add(0, moduleAPI.getContentData().get(0).getGist().getId());
                                appCMSPresenter.setRelatedArticleIds(articleIDs);
                            }

                            componentViewResult.componentView.setOnClickListener(v -> {
                                int currentIndex = appCMSPresenter.getCurrentArticleIndex();
                                if (appCMSPresenter.getRelatedArticleIds() != null &&
                                        currentIndex < appCMSPresenter.getRelatedArticleIds().size() - 2) {
                                    currentIndex = currentIndex + 1;
                                    appCMSPresenter.setCurrentArticleIndex(currentIndex);
                                    appCMSPresenter.navigateToArticlePage(appCMSPresenter.getRelatedArticleIds().get(currentIndex + 1),
                                            moduleAPI.getContentData().get(0).getGist().getTitle(), false,
                                            () -> {
                                                if (appCMSPresenter.getCurrentArticleIndex() == appCMSPresenter.getRelatedArticleIds().size() - 2) {
                                                    componentViewResult.componentView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                                                    componentViewResult.componentView.setEnabled(false);
                                                }

                                                if (appCMSPresenter.getCurrentArticleIndex() > -1) {
                                                    View prevButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.article_prev_button);
                                                    if (prevButton != null) {
                                                        prevButton.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                                        prevButton.setEnabled(true);
                                                    }
                                                }
                                                CustomWebView customWebView = pageView.findViewById(R.id.article_web_view);
                                                if (customWebView != null) {
                                                    customWebView.loadUrl("about:blank");
                                                }
                                            }, false);
                                }
                            });
                        }
                        break;

                    case PAGE_AUTOPLAY_MOVIE_PLAY_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.autoplay_play_button);
                        ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getPlayText());
                        break;

                    case PAGE_AUTOPLAY_MOVIE_CANCEL_BUTTON_KEY:
                        ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getCancelText());
                        componentViewResult.componentView.setOnClickListener(v -> {
                            appCMSPresenter.sendCloseOthersAction(null,
                                    true,
                                    false);
                        });
                        break;

                    case PAGE_DOWNLOAD_QUALITY_CONTINUE_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.download_quality_continue_button);
                        ((Button) componentViewResult.componentView).setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        if (metadataMap != null && metadataMap.getContinueButtonLabel() != null)
                            ((Button) componentViewResult.componentView).setText(metadataMap.getContinueButtonLabel().toUpperCase());
                        break;

                    case PAGE_DOWNLOAD_QUALITY_CANCEL_BUTTON_KEY:
                        if (moduleAPI != null && (jsonValueKeyMap.get(moduleAPI.getModuleType())
                                == PAGE_AUTOPLAY_MODULE_KEY_01 ||
                                jsonValueKeyMap.get(moduleAPI.getModuleType())
                                        == PAGE_AUTOPLAY_MODULE_KEY_02 ||
                                jsonValueKeyMap.get(moduleAPI.getModuleType())
                                        == PAGE_AUTOPLAY_MODULE_KEY_03 ||
                                jsonValueKeyMap.get(moduleAPI.getModuleType())
                                        == PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY)) {
                            componentViewResult.componentView.setId(R.id.autoplay_cancel_button);
                            componentViewResult.componentView.setOnClickListener(v -> {
                                appCMSPresenter.sendCloseAutoplayAction(null,
                                        true,
                                        false);
                            });
                        } else {
                            componentViewResult.componentView.setId(R.id.download_quality_cancel_button);
                            if (metadataMap != null && metadataMap.getCancelButton() != null)
                                ((Button) componentViewResult.componentView).setText(metadataMap.getCancelButton().toUpperCase());
                        }
                        applyBorderToComponent(
                                context,
                                componentViewResult.componentView,
                                component,
                                -1);
                        break;

                    case PAGE_LIVE_SCHEDULE_BACKGROUND_KEY: {
                        View child = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.live_schedule_button, null);
                        componentViewResult.componentView = child;
                        TextView buttonText = child.findViewById(R.id.live_schedule_textview);
                        if (AppCMSPageActivity.isDailyView) {
                            buttonText.setText(context.getString(R.string.weeklyViewBtnText));
                            child.setTag(context.getString(R.string.dailyView));
                        } else {
                            buttonText.setText(context.getString(R.string.dailyViewBtnText));
                            child.setTag(context.getString(R.string.weeklyView));
                        }
                        componentViewResult.addToPageView = true;
                        FrameLayout.LayoutParams paramsBackground =
                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramsBackground.setMargins(0, 0, 0, 0);
                        paramsBackground.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                        componentViewResult.componentView.setLayoutParams(paramsBackground);
                        child.setId(R.id.scheduleButton);
                        child.setOnClickListener(view -> {
                            try {
                                View scheduleButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.scheduleButton);
                                View liveScheduleGrid = appCMSPresenter.getCurrentActivity().findViewById(R.id.liveScheduleGrid);
                                View scheduleGridDate = appCMSPresenter.getCurrentActivity().findViewById(R.id.scheduleGridDate);
                                View scheduleWeekViewGrid = appCMSPresenter.getCurrentActivity().findViewById(R.id.scheduleWeekViewGrid);

                                if (scheduleButton.getTag().equals(context.getString(R.string.dailyView))) {
                                    AppCMSPageActivity.isDailyView = false;
                                    scheduleButton.setTag(context.getString(R.string.weeklyView));
                                    buttonText.setText(R.string.dailyViewBtnText);
                                    liveScheduleGrid.setVisibility(View.GONE);
                                    scheduleGridDate.setVisibility(View.VISIBLE);
                                    scheduleWeekViewGrid.setVisibility(View.VISIBLE);
                                } else {
                                    AppCMSPageActivity.isDailyView = true;
                                    scheduleButton.setTag(context.getString(R.string.dailyView));
                                    buttonText.setText(R.string.weeklyViewBtnText);
                                    liveScheduleGrid.setVisibility(View.VISIBLE);
                                    if (pageView != null) {
                                        pageView.scrollToPosition(0, 0);
                                    }
                                    scheduleGridDate.setVisibility(View.GONE);
                                    scheduleWeekViewGrid.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    break;

                    case PAGE_SUBSCRIBE_EMAIL_GO_BUTTON_KEY:
                        ((Button) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        ((Button) componentViewResult.componentView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;

                    case PAGE_BROWSE_ARROWS_BUTTON_KEY:
                        ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.ic_swap_white);
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        break;

                    case PAGE_INSTRUCTOR_TWITTER_ICON_KEY:
                        componentViewResult.componentView = new LinearLayout(context);
                        componentViewResult.componentView.setPadding(38, 38, 38, 38);
                        ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        ImageButton twitterButton = new ImageButton(context);
                        twitterButton.setImageResource(R.drawable.brand_twitter);
                        twitterButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        PaintDrawable twitterDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.color_white));
                        twitterDefault.setCornerRadius(20f);
                        twitterDefault.setAlpha(75);
                        twitterButton.setBackground(twitterDefault);
                        ((LinearLayout) componentViewResult.componentView).addView(twitterButton);
                        if (moduleAPI.getContentData().get(0).getGist() == null
                                || moduleAPI.getContentData().get(0).getGist().getTwitterUrl() == null)
                            componentViewResult.componentView.setVisibility(View.GONE);
                        twitterButton.setOnClickListener(v -> {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getTwitterUrl() != null)
                                appCMSPresenter.openChromeTab(moduleAPI.getContentData().get(0).getGist().getTwitterUrl());
                        });
                        break;

                    case PAGE_INSTRUCTOR_FACEBOOK_ICON_KEY:
                        componentViewResult.componentView = new LinearLayout(context);
                        componentViewResult.componentView.setPadding(100, 75, 100, 75);
                        ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        ImageButton facebookButton = new ImageButton(context);
                        facebookButton.setImageResource(R.drawable.brand_facebook);
                        facebookButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        PaintDrawable facebookDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.color_white));
                        facebookDefault.setCornerRadius(20f);
                        facebookDefault.setAlpha(75);
                        facebookButton.setBackground(facebookDefault);
                        ((LinearLayout) componentViewResult.componentView).addView(facebookButton);
                        if (moduleAPI.getContentData().get(0).getGist() == null
                                || moduleAPI.getContentData().get(0).getGist().getFacebookUrl() == null)
                            componentViewResult.componentView.setVisibility(View.GONE);
                        facebookButton.setOnClickListener(v -> {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getFacebookUrl() != null)
                                appCMSPresenter.openChromeTab(moduleAPI.getContentData().get(0).getGist().getFacebookUrl());
                        });
                        break;

                    case PAGE_INSTRUCTOR_INSTAGRAM_ICON_KEY:
                        componentViewResult.componentView = new LinearLayout(context);
                        componentViewResult.componentView.setPadding(44, 44, 44, 44);
                        ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        ImageButton instaButton = new ImageButton(context);
                        instaButton.setImageResource(R.drawable.brand_instagram);
                        instaButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        PaintDrawable instaDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.color_white));
                        instaDefault.setCornerRadius(20f);
                        instaDefault.setAlpha(75);
                        instaButton.setBackground(instaDefault);

                        ((LinearLayout) componentViewResult.componentView).addView(instaButton);
                        if (moduleAPI.getContentData().get(0).getGist() == null
                                || moduleAPI.getContentData().get(0).getGist().getInstagramUrl() == null)
                            componentViewResult.componentView.setVisibility(View.GONE);
                        instaButton.setOnClickListener(v -> {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getInstagramUrl() != null)
                                appCMSPresenter.openChromeTab(moduleAPI.getContentData().get(0).getGist().getInstagramUrl());
                        });
                        break;

                    case PAGE_EQUIPMENT_SKIP_BUTTON_KEY:
                    case PAGE_EQUIPMENT_NEXT_BUTTON_KEY:
                        ((Button) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                        componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                    case PAGE_SIGNIN_BUTTON_KEY:
                        componentViewResult.componentView.setBackgroundResource(R.drawable.rectangle);
                        componentViewResult.componentView.setOnClickListener(v -> {
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    component.getAction(),
                                    null,
                                    null,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                    case PAGE_LAUNCH_LOGIN_BUTTON_KEY:
                        ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.lets_go_button);
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        componentViewResult.componentView.setOnClickListener(v -> {
                            pageView.enableSwipe();
                            appCMSPresenter.sendCloseOthersAction(null, true, false);
                            appCMSPresenter.navigateToLoginPage(false);
                        });
                        componentViewResult.componentView.setVisibility(View.GONE);
                        break;
                    case PAGE_SETTINGS_CANCEL_PLAN_PROFILE_KEY:
                        ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getCancelText());
                        if (appCMSPresenter.shouldDisplaySubscriptionCancelButton() &&
                                appCMSPresenter.isUserSubscribed() &&
                                !appCMSPresenter.isExistingGooglePlaySubscriptionSuspended() &&
                                appCMSPresenter.isSubscriptionCompleted()) {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String action = component.getAction();
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    action,
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                    case PAGE_REDEEM_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.redeem_button_key);
                        ((Button) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                        componentViewResult.componentView.setBackgroundColor(Color.GRAY);
                        componentViewResult.componentView.setEnabled(false);
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;

                    default:
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String action = component.getAction();
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    action,
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;
                }

                if (jsonValueKeyMap.get(viewType) == PAGE_SETTINGS_KEY) {
                    componentViewResult.componentView.setBackgroundColor(
                            ContextCompat.getColor(context, android.R.color.transparent));
                    if (componentViewResult.componentView instanceof Button) {
                        ((Button) componentViewResult.componentView)
                                .setTextColor(appCMSPresenter.getGeneralTextColor());
                    }
                }
                break;

            case PAGE_ADS_KEY:
                //todo need to work for managing Subscribed User case scanerio
                componentViewResult.componentView = new LinearLayout(context);
                AdView adView = new AdView(context);
                adView.setFocusable(false);
                adView.setEnabled(false);
                adView.setClickable(false);
                switch (jsonValueKeyMap.get(viewType)) {
                    case PAGE_BANNER_AD_MODULE_KEY:
                        adView.setAdSize(AdSize.BANNER);
                        break;
                    case PAGE_MEDIAM_RECTANGLE_AD_MODULE_KEY:
                        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                        break;
                }

                if (moduleAPI != null && moduleAPI.getMetadataMap() != null) {
                    MobileAds.initialize(context, moduleAPI.getMetadataMap().getAdTag());
                    adView.setAdUnitId(moduleAPI.getMetadataMap().getAdTag());

                    AdRequest adRequest = new AdRequest.Builder().build();
                    adView.loadAd(adRequest);
                    ((LinearLayout) componentViewResult.componentView).addView(adView);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) adView.getLayoutParams();
                    if (params != null) {
                        params.weight = 1.0f;
                        params.gravity = Gravity.CENTER;
                        adView.setLayoutParams(params);
                    }
                } else {
                    componentViewResult.componentView.setVisibility(View.GONE);
                }
                break;

            case PAGE_SEARCH_BLOCK_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParamsMain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                componentViewResult.componentView.setLayoutParams(layoutParamsMain);

                ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);
                ((LinearLayout) componentViewResult.componentView).setWeightSum(10f);
                appCMSSearchView = new SearchView(context);

                float searchWeight;
                float goBtnWeight;
                if (BaseView.isTablet(context)) {
                    searchWeight = 1f;
                    goBtnWeight = 9f;
                } else {
                    searchWeight = 2f;
                    goBtnWeight = 8f;
                }
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, searchWeight);
                appCMSSearchView.setLayoutParams(layoutParams1);

                Button btnGo = new Button(context);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, goBtnWeight);
                btnGo.setLayoutParams(layoutParams2);
                btnGo.setBackgroundColor(0xff000000 + appCMSPresenter.getBrandPrimaryCtaColor());
                btnGo.setTextColor(0xff000000 + (int) ViewCreator.adjustColor1(appCMSPresenter.getBrandPrimaryCtaTextColor(), appCMSPresenter.getBrandPrimaryCtaColor()));
                btnGo.setText(appCMSPresenter.getLocalisedStrings().getGoText());


                btnGo.setOnClickListener(v -> {
                    if (appCMSSearchView.getQuery().toString().trim().length() == 0) {
                        appCMSPresenter.showEmptySearchToast();
                        return;
                    }
                    appCMSPresenter.showLoadingDialog(true);
                    String queryTerm = appCMSSearchView.getQuery().toString().trim();
                    appCMSSearchView.setSuggestionsAdapter(null);
                    SearchQuery objSearchQuery = new SearchQuery();
                    objSearchQuery.searchInstance(appCMSPresenter);
                    objSearchQuery.searchQuery(queryTerm);
                    appCMSPresenter.sendSearchEvent(queryTerm);
                });

                SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
                SearchSuggestionsAdapter searchSuggestionsAdapter = new SearchSuggestionsAdapter(appCMSPresenter, appCMSPresenter.getCurrentActivity(),
                        null,
                        searchManager.getSearchableInfo(appCMSPresenter.getCurrentActivity().getComponentName()),
                        true);
                componentViewResult.addToPageView = true;
                ((LinearLayout) componentViewResult.componentView).addView(appCMSSearchView);
                ((LinearLayout) componentViewResult.componentView).addView(btnGo);

                (appCMSSearchView).setBackgroundResource(android.R.color.white);
                (appCMSSearchView).
                        setQueryHint(appCMSPresenter.getLocalisedStrings().getSearchLabelText());
                appCMSSearchView.setSearchableInfo(searchManager.getSearchableInfo(appCMSPresenter.getCurrentActivity().getComponentName()));
                appCMSSearchView.setSuggestionsAdapter(searchSuggestionsAdapter);
                appCMSSearchView.setIconifiedByDefault(false);
                appCMSSearchView.requestFocus();
                TextView searchText = appCMSSearchView.findViewById(androidx.appcompat.R.id.search_src_text);
                appCMSPresenter.setCursorDrawableColor((EditText) searchText, 0);
                String searchQuery = searchQueryText;
                if (searchQuery != null) {
                    appCMSSearchView.setQuery(searchQuery, false);
                }
                appCMSSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        setSearchText(newText);
                        if (newText.trim().isEmpty()) {
                            SearchQuery objSearchQuery = new SearchQuery();
                            objSearchQuery.searchInstance(appCMSPresenter);
                            objSearchQuery.searchEmptyQuery("", appCMSPresenter.isNavbarPresent(), appCMSPresenter.isAppbarPresent());
                        }
                        return false;
                    }
                });

                appCMSSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    @Override
                    public boolean onSuggestionSelect(int position) {
                        return true;
                    }

                    @Override
                    public boolean onSuggestionClick(int position) {
                        new SearchQuery().updateMessage(appCMSPresenter, false, appCMSPresenter.getLocalisedStrings().getNoSearchResultText());
                        Cursor cursor = (Cursor) appCMSSearchView.getSuggestionsAdapter().getItem(position);
                        String[] searchHintResult = cursor.getString(cursor.getColumnIndex("suggest_intent_data")).split(",");
                        appCMSPresenter.searchSuggestionClick(searchHintResult);
                        return true;
                    }
                });
                break;

            case PAGE_LABEL_KEY:
            case PAGE_TEXTVIEW_KEY:
                boolean resizeText = false;
                int textColor = ContextCompat.getColor(context, R.color.colorAccent);
                if (jsonValueKeyMap.get(component.getKey()) == PAGE_FIGHT_SELECTION_TXT_KEY) {
                    if (moduleAPI != null && moduleAPI.getContentData() != null &&
                            moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null && moduleAPI.getContentData().get(0).getLiveEvents() != null &&
                            moduleAPI.getContentData().get(0).getLiveEvents().get(0) != null && moduleAPI.getContentData().get(0).getLiveEvents().get(0).getFights() != null) {
                        List<Fights> fights = moduleAPI.getContentData().get(0).getLiveEvents().get(0).getFights();
                        componentViewResult.componentView = new LinearLayout(context);
                        LinearLayout.LayoutParams layoutParamsDetail =
                                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                        componentViewResult.componentView.setLayoutParams(layoutParamsDetail);
                        ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);
                        ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.START);
                        componentViewResult.componentView.setBackgroundColor(Color.parseColor(CommonUtils.getColor(context, "#d6202d")));

                        Spinner spinnerFight = new Spinner(context, Spinner.MODE_DROPDOWN);
                        spinnerFight.setLayoutParams(layoutParamsDetail);
                        spinnerFight.getBackground().setColorFilter(Color.parseColor(
                                CommonUtils.getColor(context,
                                        "#ffffff")),
                                PorterDuff.Mode.SRC_ATOP);
                        spinnerFight.setPopupBackgroundDrawable(new ColorDrawable(Color.parseColor(
                                CommonUtils.getColor(context, "#d6202d"))));
                        ArrayAdapter<String> FightTrayAdapter = new FightSmmaryAdapterView(context,
                                appCMSPresenter,
                                component,
                                jsonValueKeyMap);
                        ArrayList<Fights> listFight = new ArrayList<Fights>();
                        for (int i = 0; i < fights.size(); i++) {
                            listFight.add(fights.get(0));

                            if (!TextUtils.isEmpty(fights.get(i).getFighter1_LastName())) {
                                String fighter1Name = fights.get(i).getFighter1_LastName();
                                String fighter2Name = fights.get(i).getFighter2_LastName();
                                if (fights.get(i).getWinnerId() != null && !TextUtils.isEmpty(fights.get(i).getWinnerId())) {
                                    if (fights.get(i).getWinnerId().equalsIgnoreCase(fights.get(i).getFighter1_Id())) {
                                        fighter1Name = fighter1Name + "(Won)";
                                    } else if (fights.get(i).getWinnerId().equalsIgnoreCase(fights.get(i).getFighter2Id())) {
                                        fighter2Name = fighter2Name + "(Won)";
                                    }
                                }
                                FightTrayAdapter.add(i + 1 + " " + fighter1Name + "/" + fighter2Name);
                            }
                        }
                        OnFightSelectedListener onItemSelectListener = new OnFightSelectedListener(listFight, appCMSPresenter, context, moduleAPI, component, jsonValueKeyMap);
                        componentViewResult.onInternalEvent.setModuleId(moduleId);
                        spinnerFight.setEnabled(true);
                        spinnerFight.setOnItemSelectedListener(onItemSelectListener);
                        try {
                            FightTrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerFight.setAdapter(FightTrayAdapter);
                            spinnerFight.setSelection(0);
                            FightTrayAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((LinearLayout) componentViewResult.componentView).addView(spinnerFight);
                    }
                } else if (jsonValueKeyMap.get(component.getKey()) == PAGE_RECORD_TYPE_KEY) {
                    componentViewResult.componentView = new LinearLayout(context);
                    ((LinearLayout) componentViewResult.componentView).setGravity(Gravity.BOTTOM);
                    ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);
                } else {
                    componentViewResult.componentView = new TextView(context);
                    if (!TextUtils.isEmpty(component.getText())) {
                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                    }
                    if (componentKey == TEXT_VIEW_OR && (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                            || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE && settings != null && settings.isHideSocialSignup()) {
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }

                    if (componentKey == PAGE_FACEBOOK_SIGNUP_KEY) {
                        componentViewResult.componentView.setOnClickListener(view -> appCMSPresenter.launchButtonSelectedAction(null,
                                component.getAction(),
                                null,
                                null,
                                null,
                                true,
                                0,
                                null));

                    }

                    if (componentKey == LABEL_SHOW_CLASSES) {
                        componentViewResult.componentView.setBackgroundResource(R.drawable.red_rounded_bg);
                    }
                    if (componentKey == PAGE_CLOSE) {
                        componentViewResult.componentView.setOnClickListener(view -> appCMSPresenter.sendCloseOthersAction(null, true, false));
                    }
                    if (componentKey == LABEL_CLEAR_FILTER) {
                        componentViewResult.componentView.setOnClickListener(view -> {
                            if (expandableFilterAdapter != null)
                                expandableFilterAdapter.resetFilter();
                        });
                    }
                    if (componentKey == PAGE_PHOTO_GALLERY_TITLE_TXT_KEY) {
                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                        ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#000000"));
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER_VERTICAL);
                        ((TextView) componentViewResult.componentView).setSingleLine(true);
                        ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    if (componentKey == PAGE_SEARCH_RESULT_COUNT_KEY) {
                        if (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) {
                            ((TextView) componentViewResult.componentView).setText("Results  (" + moduleAPI.getContentData().size() + ")");
                        } else {
                            component.setFontWeight(context.getResources().getString(R.string.app_cms_page_font_italic_key));
                            setTypeFace(context,
                                    appCMSPresenter,
                                    jsonValueKeyMap,
                                    component,
                                    componentViewResult.componentView);
                            component.setTextColor("#9b9b9b");
                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.neou_no_search_result)));
                            String errorMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.neou_no_search_result));
                            String errorMessageSecond = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.neou_no_search_result_second));

                            ((TextView) componentViewResult.componentView).setText(errorMessage);
                            ((TextView) componentViewResult.componentView).setLinkTextColor(ContextCompat.getColor(context, R.color.link));
                            ClickableSpan browseClick = new ClickableSpan() {
                                @Override
                                public void onClick(View view) {
                                    appCMSPresenter.navigateToBrowsePage(null, null);
                                }
                            };
                            appCMSPresenter.makeTextViewLinks(((TextView) componentViewResult.componentView), new String[]{errorMessageSecond}, new ClickableSpan[]{browseClick}, true);
                        }
                    }
                    if (jsonValueKeyMap.get(component.getKey()) == PAGE_PHOTO_GALLERY_AUTH_TXT_KEY) {
                        if (moduleAPI.getContentData().get(0).getContentDetails() != null) {
                            StringBuilder authDateAndPhotoCount = new StringBuilder();
                            if (moduleAPI.getContentData().get(0).getContentDetails().getAuthor() != null && moduleAPI.getContentData().get(0).getContentDetails().getAuthor().getName() != null) {
                                authDateAndPhotoCount.append(moduleAPI.getContentData().get(0).getContentDetails().getAuthor().getName());
                            }
                            if (moduleAPI.getContentData().get(0).getGist().getPublishDate() != null) {
                                authDateAndPhotoCount.append(" | ")
                                        .append(appCMSPresenter.getDateFormat(Long.parseLong(moduleAPI.getContentData().get(0).getGist().getPublishDate()), "MMM dd"));
                            }
                            if (moduleAPI.getContentData().get(0).getStreamingInfo() != null && moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets() != null) {
                                authDateAndPhotoCount.append(" | ")
                                        .append(moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size() + " Photos");
                            }
                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getByText() + authDateAndPhotoCount.toString());
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#000000"));
                        }
                    }

                    if (jsonValueKeyMap.get(component.getKey()) == PAGE_PHOTO_GALLERY_SUBTITLE_TXT_KEY) {

                        StringBuilder tags = new StringBuilder();
                        String tagsName = "";
                        tags.append("<b>TAGGED:</b> ");
                        if (moduleAPI.getContentData().get(0).getTags() != null && moduleAPI.getContentData().get(0).getTags().size() > 0) {
                            for (Tag tag : moduleAPI.getContentData().get(0).getTags())
                                tags.append("" + tag.getTitle().toUpperCase() + ",");
                            tagsName = tags.length() > 0 ? tags.substring(0, tags.length() - 1) : "";
                        }
                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(tagsName));
                        ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#000000"));
                    }

                    if (jsonValueKeyMap.get(component.getKey()) == PAGE_SD_CARD_FOR_DOWNLOADS_TEXT_KEY && !appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                        componentViewResult.componentView.setVisibility(View.GONE);
                        componentViewResult.shouldHideComponent = true;
                    } else if (jsonValueKeyMap.get(component.getKey()) == PAGE_USER_MANAGEMENT_AUTOPLAY_TEXT_KEY &&
                            !appCMSPresenter.isAutoPlayEnable()) {
                        componentViewResult.componentView.setVisibility(View.GONE);
                        componentViewResult.shouldHideComponent = true;
                    }
                    if (!TextUtils.isEmpty(appCMSPresenter.getAppTextColor())) {
                        textColor = Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppTextColor()));
                    } else if (component.getStyles() != null) {
                        if (!TextUtils.isEmpty(component.getStyles().getColor())) {
                            textColor = Color.parseColor(CommonUtils.getColor(context, component.getStyles().getColor()));
                        } else if (appCMSPresenter.getGeneralTextColor() != 0) {
                            textColor = Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppTextColor()));
                        }
                    }

                    if (!TextUtils.isEmpty(component.getTextAlignment()) &&
                            component.getTextAlignment().equals(context.getString(R.string.app_cms_page_text_alignment_right_key))) {
                        componentViewResult.componentView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    }

                    if (componentKey == PAGE_TRAY_BOTTOM_TEXT1_KEY ||
                            componentKey == PAGE_TRAY_BOTTOM_TEXT2_KEY) {
                        componentViewResult.componentView.setBackgroundResource(R.drawable.tray_bottom_text_bg);
                    }
                    if (componentKey == PAGE_PLAYLIST_TITLE) {

                        int textFontColor = appCMSPresenter.getGeneralTextColor();
                        if (!TextUtils.isEmpty(component.getTextColor())) {
                            textFontColor = Color.parseColor(CommonUtils.getColor(context, component.getTextColor()));
                        }
                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.START);
                        setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, componentViewResult.componentView);
                        if (component.getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                        } else if (BaseView.getFontSize(context, component.getLayout()) > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(BaseView.getFontSize(context, component.getLayout()));
                        }
                        if (moduleAPI != null
                                && moduleAPI.getContentData() != null
                                && moduleAPI.getContentData().get(0) != null
                                && moduleAPI.getContentData().get(0).getGist() != null
                                && moduleAPI.getContentData().get(0).getGist().getTitle() != null) {
                            ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                        }
                    }
                    if (componentKey == PAGE_PLAYLIST_SUB_TITLE) {

                        int textFontColor = appCMSPresenter.getGeneralTextColor();
                        if (!TextUtils.isEmpty(component.getTextColor())) {
                            textFontColor = Color.parseColor(CommonUtils.getColor(context, component.getTextColor()));
                        }
                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.START);


                        if (component.getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                        } else if (BaseView.getFontSize(context, component.getLayout()) > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(BaseView.getFontSize(context, component.getLayout()));
                        }
                        if (moduleType == PAGE_VIDEO_PLAYLIST_MODULE_KEY) {
                            if (moduleAPI != null
                                    && moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().size() >= 1
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getVideoList() != null) {
                                int videoNum = moduleAPI.getContentData().get(0).getVideoList().size();
                                ((TextView) componentViewResult.componentView).setText((moduleAPI.getContentData().get(0).getVideoList().size() > 1) ? " " + videoNum + " VIDEOS" : " " + videoNum + " VIDEO");
                            }
                        } else {
                            if (moduleAPI != null
                                    && moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().size() >= 1
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getAudioList() != null) {
                                int songNum = moduleAPI.getContentData().get(0).getAudioList().size();
                                ((TextView) componentViewResult.componentView).setText((moduleAPI.getContentData().get(0).getAudioList().size() > 1) ? " " + songNum + " " + appCMSPresenter.getLocalisedStrings().getSongsHeaderText() : " " + songNum + " " + appCMSPresenter.getLocalisedStrings().getSongsHeaderText());
                            }
                        }
                        break;
                    }
                    if (componentKey == PAGE_BANNER_DETAIL_TITLE) {
                        int textFontColor = appCMSPresenter.getGeneralTextColor();
                        if (!TextUtils.isEmpty(component.getTextColor()))
                            textFontColor = Color.parseColor(CommonUtils.getColor(context, component.getTextColor()));
                        componentViewResult.componentView.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.START);
                        setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, componentViewResult.componentView);
                        if (component.getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                        } else if (BaseView.getFontSize(context, component.getLayout()) > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(BaseView.getFontSize(context, component.getLayout()));
                        }
                        if (settings != null && settings.getTitle() != null)
                            ((TextView) componentViewResult.componentView).setText(settings.getTitle());
                    }
                    if (componentKey == PAGE_GRID_THUMBNAIL_INFO
                            || componentKey == PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO) {
                        int textBgColor = appCMSPresenter.getGeneralTextColor();
                        if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                            textBgColor = Color.parseColor(getColorWithOpacity(context, component.getBackgroundColor(), component.getOpacity()));
                        }
                        int textFontColor = appCMSPresenter.getGeneralTextColor();
                        if (!TextUtils.isEmpty(component.getTextColor())) {
                            textFontColor = Color.parseColor(CommonUtils.getColor(context, component.getTextColor()));
                        }
                        componentViewResult.componentView.setBackgroundColor(textBgColor);
                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.START);
                        setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, componentViewResult.componentView);
                        if (component.getFontSize() > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                        } else if (BaseView.getFontSize(context, component.getLayout()) > 0) {
                            ((TextView) componentViewResult.componentView).setTextSize(BaseView.getFontSize(context, component.getLayout()));
                        }
                        break;
                    } else if (componentKey == PAGE_AUTOPLAY_FINISHED_UP_TITLE_KEY
                            || componentKey == PAGE_AUTOPLAY_MOVIE_TITLE_KEY
                            || componentKey == PAGE_AUTOPLAY_MOVIE_SUBHEADING_KEY
                            || componentKey == PAGE_AUTOPLAY_MOVIE_DESCRIPTION_KEY
                            || componentKey == PAGE_VIDEO_AGE_LABEL_KEY) {
                        textColor = appCMSPresenter.getGeneralTextColor();
                        ((TextView) componentViewResult.componentView).setTextColor(textColor);
                    } else if (componentKey != PAGE_TRAY_TITLE_KEY) {
                        ((TextView) componentViewResult.componentView).setTextColor(textColor);
                    } else {
                        ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(CommonUtils.getColor(context,
                                appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor())));
                    }
                    if (componentKey == VIEW_FILTER_BUTTON) {
                        componentViewResult.addToPageView = true;
                        componentViewResult.componentView.setBackgroundResource(R.drawable.rectangle_white_round_corners);
                        ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.filter_icon, 0, 0, 0);
                        ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                        componentViewResult.componentView.setOnClickListener(v -> appCMSPresenter.launchFilterPage());
                        if (moduleAPI.getClassessData() != null && moduleAPI.getClassessData().size() == 0) {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                    }

                    if ((BaseView.getFontSize(context, component.getLayout()) > 0) && (moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY && componentKey != PAGE_PLAN_PRICEINFO_KEY)) {
                        ((TextView) componentViewResult.componentView).setTextSize(BaseView.getFontSize(context, component.getLayout()));
                    }
                    if (componentKey == PAGE_SINGLE_PLAN_SUBSCRIBE_TEXT_KEY)
                        componentViewResult.componentView.setVisibility(View.GONE);

                    if (moduleType == PAGE_PLAYER_DETAIL_MODULE_KEY) {
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_WEIGHT_DIVISION_VALUE_TXT_KEY) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getMetadata() != null) {
                                for (int i = 0; i < moduleAPI.getContentData().get(0).getGist().getMetadata().size(); i++) {
                                    if (moduleAPI.getContentData().get(0).getGist().getMetadata().get(i).getName().equalsIgnoreCase("weight_division")) {
                                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getMetadata().get(i).getValue());

                                    }
                                }
                            }
                        }
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_PLAYER_NAME_KEY) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getFirstName() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getFirstName());

                            }
                        }
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_PLAYER_SCORE_TEXT) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getFirstName() != null) {
                                for (int i = 0; i < moduleAPI.getContentData().get(0).getGist().getMetadata().size(); i++) {
                                    if (moduleAPI.getContentData().get(0).getGist().getMetadata().get(i).getName().equalsIgnoreCase("record")) {
                                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getMetadata().get(i).getValue());

                                    }
                                }
                            }
                        }
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_WEIGHT_VALUE_TEXT) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getWeight() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getWeight());

                            }
                        }

                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_HEIGHT_VALUE_TEXT) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getHeight() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getHeight());

                            }
                        }
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_HEIGHT_VALUE_TEXT) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getHeight() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getHeight());

                            }
                        }
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_BIRTHDATE_VALUE_TEXT) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getDob() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getDob());

                            }
                        }
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_HOMETOWN_VALUE_TEXT) {
                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getBirthPlace() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getBirthPlace());

                            }
                        }
                        ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(CommonUtils.getColor(context, component.getTextColor())));
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER_VERTICAL);
                        ((TextView) componentViewResult.componentView).setSingleLine(true);
                        ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    if (jsonValueKeyMap.get(component.getKey()) == PAGE_PHOTO_GALLERY_AUTH_TXT_KEY) {
                        if (moduleAPI.getContentData().get(0).getContentDetails() != null) {
                            StringBuilder authDateAndPhotoCount = new StringBuilder();
                            if (moduleAPI.getContentData().get(0).getContentDetails().getAuthor() != null && moduleAPI.getContentData().get(0).getContentDetails().getAuthor().getName() != null)
                                authDateAndPhotoCount.append(moduleAPI.getContentData().get(0).getContentDetails().getAuthor().getName());
                            if (moduleAPI.getContentData().get(0).getGist().getPublishDate() != null)
                                authDateAndPhotoCount.append(" | ").append(appCMSPresenter.getDateFormat(Long.parseLong(moduleAPI.getContentData().get(0).getGist().getPublishDate()), "MMM dd"));
                            if (moduleAPI.getContentData().get(0).getStreamingInfo() != null && moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets() != null)
                                authDateAndPhotoCount.append(" | ").append(moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size() + " Photos");
                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getByText() + authDateAndPhotoCount.toString());
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#80000000"));
                        }
                    }
                    if (componentKey == PAGE_STATE_LABEL_TXT_KEY || componentKey == PAGE_TABEL_LABEL_HEADER_TXT_KEY) {
                        int textFontColor = 0;
                        if (!TextUtils.isEmpty(component.getTextColor()))
                            textFontColor = Color.parseColor(CommonUtils.getColor(context, component.getTextColor()));
                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) : component.getText());
                    }
                    if (componentKey == PAGE_ROSTER_TITLE_TXT_KEY) {
                        int textFontColor = 0;
                        if (!TextUtils.isEmpty(component.getTextColor()))
                            textFontColor = Color.parseColor(CommonUtils.getColor(context, "#000000"));
                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.title)));
                    }
                    if (componentKey == PAGE_PLAN_DESCRIPTION_KEY) {
                        if (moduleAPI != null && moduleAPI.getDescription() != null)
                            ((TextView) componentViewResult.componentView).setText(moduleAPI.getDescription());
                        else
                            componentViewResult.shouldHideComponent = true;
                    }

                    if (componentKey == PAGE_SCHEDULE_NO_DATA_KEY) {
                        String noDataText = "NO CLASSES SCHEDULED";
                        ((TextView) componentViewResult.componentView).setText(noDataText);
                        ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, R.color.noClassesScheduled));
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER);
                        ((TextView) componentViewResult.componentView).setSingleLine(true);
                    }

                    if (componentKey == PAGE_LIVE_SCHEDULE_ITEM_TITLE_KEY) {
                        String text = "";
                        SimpleDateFormat format = new SimpleDateFormat("dd");
                        Calendar date = Calendar.getInstance();
                        String[] calendarDays = new String[30];
                        for (int i = 0; i < 30; i++) {
                            calendarDays[i] = format.format(date.getTime());
                            date.add(Calendar.DATE, 1);
                        }
                        if (position >= appCMSPresenter.next7Days().length) {
                            position = 0;
                        }

                        if (position < calendarDays.length) {
                            String day = appCMSPresenter.next7Days()[position].substring(0, 3) + ", ";
                            String month = appCMSPresenter.next7DaysMonth()[position].substring(0, 3) + " ";
                            String dateS = calendarDays[position];
                            if ((dateS.charAt(0)) == '0') {
                                dateS = dateS.substring(1, 2);
                            }
                            text = day + month + dateS;
                        }
                        position++;
                        ((TextView) componentViewResult.componentView).setText(text);
                        ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, android.R.color.white));
                        ((TextView) componentViewResult.componentView).setGravity(Gravity.START);
                        ((TextView) componentViewResult.componentView).setSingleLine(true);
                    } else if (componentKey == PAGE_EDIT_PROFILE_PUBLIC_PROFILE_KEY) {
                        ((TextView) componentViewResult.componentView).setText("PUBLIC PROFILE");
                        ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.color_white));
                        gdDefault.setCornerRadius(20f);
                        componentViewResult.componentView.setBackground(gdDefault);
                    } else if (componentKey == PAGE_EDIT_PROFILE_PRIVATE_PROFILE_KEY) {
                        ((TextView) componentViewResult.componentView).setText("PRIVATE PROFILE");
                        ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                        PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.color_white));
                        gdDefault.setCornerRadius(20f);
                        componentViewResult.componentView.setBackground(gdDefault);
                    }

                    if (!gridElement) {
                        switch (componentKey) {
                            case PAGE_TERMS:
                                ((TextView) componentViewResult.componentView).setTypeface(((TextView) componentViewResult.componentView).getTypeface(), Typeface.ITALIC);
                                ((TextView) componentViewResult.componentView).setText(context.getResources().getString(R.string.signup_terms));
                                ((TextView) componentViewResult.componentView).setLinkTextColor(ContextCompat.getColor(context, R.color.link));
                                ClickableSpan tosClick = new ClickableSpan() {
                                    @Override
                                    public void onClick(View view) {
                                        appCMSPresenter.navigatToTOSPage(null, null);
                                    }
                                };
                                appCMSPresenter.makeTextViewLinks(((TextView) componentViewResult.componentView), new String[]{
                                        context.getResources().getString(R.string.terms_of_service)}, new ClickableSpan[]{tosClick}, true);
                                break;
                            case PAGE_API_TITLE:
                                if (uiBlockName == UI_BLOCK_HISTORY_01 || uiBlockName == UI_BLOCK_HISTORY_02) {
                                    if (metadataMap != null && metadataMap.getHistoryPageTitle() != null)
                                        ((TextView) componentViewResult.componentView).setText(metadataMap.getHistoryPageTitle());
                                    else
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.app_cms_page_history_title)));
                                } else if (uiBlockName == UI_BLOCK_DOWNLOADS_01 || uiBlockName == UI_BLOCK_DOWNLOADS_02 || uiBlockName == UI_BLOCK_DOWNLOADS_03) {
                                    if (metadataMap != null && metadataMap.getMyDownloadLowerCase() != null)
                                        ((TextView) componentViewResult.componentView).setText(metadataMap.getMyDownloadLowerCase());
                                    else
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.app_cms_page_download_title)));
                                } else if (uiBlockName == UI_BLOCK_WATCHLIST_01 || uiBlockName == UI_BLOCK_WATCHLIST_02 || uiBlockName == UI_BLOCK_WATCHLIST_03 ||
                                        uiBlockName == UI_BLOCK_WATCHLIST_04) {
                                    if (metadataMap != null && metadataMap.getWatchlistPageTitle() != null)
                                        ((TextView) componentViewResult.componentView).setText(metadataMap.getWatchlistPageTitle());
                                    else
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.app_cms_page_watchlist_title)));
                                } else if (uiBlockName == UI_BLOCK_USER_MANAGEMENT_01 || uiBlockName == UI_BLOCK_USER_MANAGEMENT_02) {
                                    if (metadataMap != null && metadataMap.getSettingsLabel() != null)
                                        ((TextView) componentViewResult.componentView).setText(metadataMap.getSettingsLabel());
                                    else
                                        ((TextView) componentViewResult.componentView).setText(context.getString(R.string.page_title_settings));
                                } else if (moduleAPI != null && !TextUtils.isEmpty(moduleAPI.getTitle())) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                                    if (uiBlockName == UI_BLOCK_SELECTPLAN_02) {
                                        setTypeFace(context,
                                                appCMSPresenter,
                                                jsonValueKeyMap,
                                                component,
                                                componentViewResult.componentView);
                                    }
                                    if (component.getNumberOfLines() != 0) {
                                        ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                    }
                                    ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                } else if (jsonValueKeyMap.get(viewType) == PAGE_LIBRARY_01_MODULE_KEY ||
                                        jsonValueKeyMap.get(viewType) == PAGE_LIBRARY_02_MODULE_KEY) {
                                    ((TextView) componentViewResult.componentView).setText(R.string.app_cms_page_mylibrary_title);
                                } else if (moduleAPI != null &&
                                        moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().size() > 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getTitle() != null) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                                    if (component.getNumberOfLines() != 0) {
                                        ((TextView) componentViewResult.componentView).setSingleLine(false);
                                        ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                    }
                                    ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                    if (moduleAPI.getContentData().get(0).getGist().getContentType() != null &&
                                            moduleAPI.getContentData().get(0).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_event))) {
                                        ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                    } else if (jsonValueKeyMap.get(viewType) == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                                        ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                                    }
                                }

                                break;
                            case PAGE_SHOW_SUBTITLE_KEY:
                                StringBuilder stringSubTitle = new StringBuilder();
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getSeason() != null) {
                                    if (moduleAPI.getContentData().get(0).getSeason().size() >= 1) {
                                        if (metadataMap != null && metadataMap.getSeasonsLabel() != null)
                                            stringSubTitle.append(moduleAPI.getContentData().get(0).getSeason().size() + " " + metadataMap.getSeasonsLabel());
                                        else
                                            stringSubTitle.append(moduleAPI.getContentData().get(0).getSeason().size() + " " + appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.seasons)));

                                    } else {
                                        if (moduleAPI.getContentData().get(0).getSeason().size() > 0
                                                && moduleAPI.getContentData().get(0).getSeason().get(0) != null &&
                                                moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes() != null) {
                                            if (appCMSPresenter.isMovieSpreeApp()) {
                                                if (metadataMap != null && metadataMap.getProgramsLabel() != null)
                                                    stringSubTitle.append(moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() + " " + metadataMap.getProgramsLabel());
                                                else
                                                    stringSubTitle.append(moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() + " " + "PROGRAMS");
                                            } else {
                                                if (metadataMap != null && metadataMap.getEpisodesLabel() != null)
                                                    stringSubTitle.append(moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() + " " + metadataMap.getEpisodesLabel());
                                                else
                                                    stringSubTitle.append(moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() + " " + appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.runtime_episodes_abbreviation)));
                                            }
                                        }
                                    }
                                    if (moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getPrimaryCategory() != null
                                            && moduleAPI.getContentData().get(0).getGist().getPrimaryCategory().getTitle() != null) {
                                        if (!TextUtils.isEmpty(stringSubTitle.toString())) {
                                            stringSubTitle.append(" | ");
                                        }
                                        stringSubTitle.append(moduleAPI.getContentData().get(0).getGist().getPrimaryCategory().getTitle().toUpperCase());
                                    }
                                    ((TextView) componentViewResult.componentView).setText(stringSubTitle.toString());

                                }
                                break;

                            case PAGE_RECENT_CLASS_TITLE_KEY:
                                TextView recentTitleText = ((TextView) componentViewResult.componentView);
                                recentTitleText.setPadding(25, 25, 0, 25);
                                recentTitleText.setTextSize(26);
                                if (moduleAPI.getContentData().get(0).getSeason().size() > 0
                                        && moduleAPI.getContentData().get(0).getSeason().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes() != null) {
                                    List<Season_> seasons = moduleAPI.getContentData().get(0).getSeason();
                                    int numSeasons = seasons.size();
                                    int sizeOfEpisodes = 0;
                                    for (int i = 0; i < numSeasons; i++) {
                                        Season_ season = seasons.get(i);
                                        List<ContentDatum> episodes = season.getEpisodes();
                                        int numEpisodes = episodes.size();
                                        if (season.getEpisodes() != null) {
                                            for (int j = 0; j < numEpisodes; j++) {
                                                sizeOfEpisodes++;
                                            }
                                        }
                                    }
                                    recentTitleText.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.recent_class)) + " (" +
                                            sizeOfEpisodes + ")");
                                }
                                break;
                            case PAGE_API_DESCRIPTION:
                            case PAGE_API_EPISODE_DESCRIPTION:
                                if (moduleAPI != null && !TextUtils.isEmpty(moduleAPI.getRawText())) {
                                    String htmlStyleRegex = "<style([\\s\\S]+?)</style>";
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(moduleAPI.getRawText().replaceAll(htmlStyleRegex, "")));
                                    ((TextView) componentViewResult.componentView).setMovementMethod(LinkMovementMethod.getInstance());
                                    ((TextView) componentViewResult.componentView).setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                }
                                break;

                            case PAGE_PHOTO_GALLERY_IMAGE_COUNT_TXT_KEY:
                                if (moduleAPI.getContentData().get(0).getStreamingInfo() != null) {
                                    componentViewResult.componentView.setId(R.id.photo_gallery_image_count);
                                    ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#FFFFFF"));
                                    int photoGallerySize = moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size();
                                    String position = photoGallerySize == 0 ? "0/0" : 1 + "/" + photoGallerySize;
                                    ((TextView) componentViewResult.componentView).setText(position);
                                    ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER);
                                }
                                break;

                            case PAGE_BRANDS_TRAY_TITLE_BRAND_KEY:
                                if (moduleAPI != null && moduleAPI.getSettings() != null && !moduleAPI.getSettings().getHideTitle() &&
                                        !TextUtils.isEmpty(moduleAPI.getTitle())) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle().toUpperCase());
                                }
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppTextColor())));
                                break;

                            case PAGE_BROWSE_CONCEPTS_TITLE_KEY:
                                String conceptTitle = "";
                                if (component.getText() != null) {
                                    conceptTitle = component.getText();
                                } else if (moduleAPI.getContentData() != null) {
                                    if (moduleAPI.getTitle() != null)
                                        conceptTitle = moduleAPI.getTitle() + " (" + moduleAPI.getContentData().size() + ")";
                                    else
                                        conceptTitle = "CONCEPTS (" + moduleAPI.getContentData().size() + ")";
                                }
                                ((TextView) componentViewResult.componentView).setText(conceptTitle);
                                break;

                            case PAGE_BROWSE_CLASSES_TITLE_KEY:
                                String classTitle = "";
                                if (moduleAPI.getContentData() != null)
                                    classTitle = "CLASSES (" + moduleAPI.getContentData().size() + ")";
                                ((TextView) componentViewResult.componentView).setText(classTitle);
                                break;

                            case PAGE_FOLLOW_EMPTY_LABEL_KEY:
                                componentViewResult.componentView.setVisibility(View.GONE);
                                componentViewResult.componentView.setId(R.id.follow_Empty_Label_Id);
                                if (component.getText() != null)
                                    ((TextView) componentViewResult.componentView).setText(component.getText());
                                if (!TextUtils.isEmpty(component.getTextColor()))
                                    ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(CommonUtils.getColor(context, component.getTextColor())));
                                break;

                            case PAGE_HOME_TRAY_TITLE_KEY:
                                if (moduleAPI != null && moduleAPI.getSettings() != null && !moduleAPI.getSettings().getHideTitle() && !TextUtils.isEmpty(moduleAPI.getTitle()))
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                                break;

                            case PAGE_TRAY_TITLE_KEY:
                                if ((moduleType == PAGE_AC_SEARCH_MODULE_KEY || moduleType == PAGE_AC_SEARCH03_MODULE_KEY) && !TextUtils.isEmpty(moduleAPI.getTitle()))
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle().toUpperCase());

                                //In Case of Library Page Title
                                if (moduleType == PAGE_LIBRARY_01_MODULE_KEY && !TextUtils.isEmpty(moduleAPI.getTitle()))
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle().toUpperCase());

                                if (moduleType == PAGE_LIBRARY_02_MODULE_KEY && !TextUtils.isEmpty(moduleAPI.getTitle()))
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle().toUpperCase());

                                if (!TextUtils.isEmpty(component.getText())) {
                                    if (jsonValueKeyMap.get(viewType) == PAGE_PERSON_DETAIL_03_MODULE_KEY) {
                                        if (moduleAPI != null &&
                                                moduleAPI.getContentData() != null &&
                                                moduleAPI.getContentData().size() > 0 &&
                                                moduleAPI.getContentData().get(0) != null &&
                                                moduleAPI.getContentData().get(0).getGist() != null &&
                                                moduleAPI.getContentData().get(0).getGist().getRelatedVideos() != null &&
                                                moduleAPI.getContentData().get(0).getGist().getRelatedVideos().size() > 0) {
                                            boolean vodsInRelatedVideos = true;
                                            for (int i = 0; i < moduleAPI.getContentData().get(0).getGist().getRelatedVideos().size(); i++) {
                                                if (!moduleAPI.getContentData().get(0).getGist().getRelatedVideos().get(i).getGist().isLiveStream()) {
                                                    vodsInRelatedVideos = true;
                                                    break;
                                                } else {
                                                    vodsInRelatedVideos = false;
                                                }
                                            }
                                            if (vodsInRelatedVideos) {
                                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()).toUpperCase() : component.getText().toUpperCase());
                                            }
                                        } else {
                                            componentViewResult.componentView.setVisibility(View.GONE);
                                        }
                                    }
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()).toUpperCase() : component.getText().toUpperCase());

                                } else if (moduleType == PAGE_SEE_ALL_CATEGORY_01_KEY || moduleType == PAGE_SEE_ALL_CATEGORY_02_KEY) {
                                    if (appCMSPresenter.getSeeAllModule() != null &&
                                            appCMSPresenter.getSeeAllModule().getTitle() != null
                                            && moduleAPI != null && moduleAPI.getContentData() != null) {
                                        int dimen = (int) context.getResources().getDimension(R.dimen.see_all_header_height);
                                        FrameLayout.LayoutParams paramsSellAllTitle =
                                                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dimen);
                                        View seeAllTitleView = componentViewResult.componentView;
                                        seeAllTitleView.setLayoutParams(paramsSellAllTitle);
                                        seeAllTitleView.setPadding(20, 0, 20, 0);
                                        seeAllTitleView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                        componentViewResult.componentView.setId(R.id.see_all_title_text_view);
                                        ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER);
                                        ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                        ((TextView) componentViewResult.componentView).setMaxLines(1);
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getSeeAllModule().getTitle() + " " + "(" + moduleAPI.getContentData().size() + ")");
                                    }
                                } else if (moduleAPI != null && moduleAPI.getSettings() != null && !moduleAPI.getSettings().getHideTitle() &&
                                        !TextUtils.isEmpty(moduleAPI.getTitle())) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle().toUpperCase());
                                } else if (moduleType == PAGE_SEASON_TRAY_MODULE_KEY
                                        || moduleType == PAGE_SEASON_TAB_MODULE_KEY
                                        || moduleType == PAGE_SHOW_DETAIL_04_MODULE_KEY) {

                                    if (moduleAPI != null &&
                                            moduleAPI.getContentData() != null &&
                                            moduleAPI.getContentData().size() != 0 &&
                                            moduleAPI.getContentData().get(0) != null &&
                                            moduleAPI.getContentData().get(0).getSeason() != null &&
                                            !moduleAPI.getContentData().get(0).getSeason().isEmpty() &&
                                            moduleAPI.getContentData().get(0).getSeason().get(0) != null &&
                                            !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getSeason().get(0).getTitle())) {
                                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getSeason().get(0).getTitle());
                                    } else {
                                        StringBuilder seasonTitleSb = new StringBuilder(metadataMap.getSeasonLabel().toUpperCase());
                                        seasonTitleSb.append(context.getString(R.string.blank_separator));
                                        seasonTitleSb.append(1);
                                        ((TextView) componentViewResult.componentView).setText(seasonTitleSb.toString());
                                    }

                                } else if (moduleType == PAGE_AC_ROSTER_MODULE_KEY) {
                                    if (moduleAPI != null &&
                                            moduleAPI.getContentData() != null &&
                                            moduleAPI.getContentData().size() != 0 &&
                                            moduleAPI.getContentData().get(0) != null &&
                                            moduleAPI.getContentData().get(0).getTeam() != null &&
                                            moduleAPI.getContentData().get(0).getTeam().getName() != null) {
                                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getTeam().getName());
                                        int textFontColor = 0;
                                        if (!TextUtils.isEmpty(component.getTextColor()))
                                            textFontColor = Color.parseColor(CommonUtils.getColor(context, component.getTextColor()));
                                        ((TextView) componentViewResult.componentView).setTextColor(textFontColor);
                                    }
                                }
                                break;
                            case PAGE_TRAY_TITLE_UNDERLINE_KEY:
                                if (moduleAPI != null && moduleAPI.getTitle() != null) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                                    ((TextView) componentViewResult.componentView).setPaintFlags(((TextView) componentViewResult.componentView).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                    ((TextView) componentViewResult.componentView).setTypeface(((TextView) componentViewResult.componentView).getTypeface(), Typeface.ITALIC);
                                }
                                break;
                            case PAGE_AUTOPLAY_MOVIE_DESCRIPTION_KEY:
                                String autoplayVideoDescription = null;
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getDescription() != null) {
                                    autoplayVideoDescription = moduleAPI.getContentData().get(0).getGist().getDescription();
                                }
                                if (autoplayVideoDescription != null)
                                    autoplayVideoDescription = autoplayVideoDescription.trim();
                                if (!TextUtils.isEmpty(autoplayVideoDescription)) {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(autoplayVideoDescription));
                                    else
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(autoplayVideoDescription, Html.FROM_HTML_MODE_COMPACT));
                                } else if (!BaseView.isLandscape(context))
                                    componentViewResult.shouldHideComponent = true;
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getTitle())) {
                                    ViewTreeObserver viewTreeObserver = componentViewResult.componentView.getViewTreeObserver();
                                    ViewCreatorMultiLineLayoutListener viewCreatorMultiLineLayoutListener =
                                            new ViewCreatorMultiLineLayoutListener(((TextView) componentViewResult.componentView),
                                                    moduleAPI.getContentData().get(0).getGist().getTitle(),
                                                    autoplayVideoDescription,
                                                    appCMSPresenter,
                                                    true,
                                                    appCMSPresenter.getBrandPrimaryCtaColor(),
                                                    appCMSPresenter.getGeneralTextColor(),
                                                    false);
                                    viewTreeObserver.addOnGlobalLayoutListener(viewCreatorMultiLineLayoutListener);
                                }
                                break;
                            case PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_VALUE_KEY:
                                String musicType = "    -";
                                if (BaseView.isTablet(context)) {
                                    musicType = "         -";
                                } else if (BaseView.isLandscape(context)) {
                                    musicType = "         -";
                                }
                                List<Tag> tagsM = appCMSPresenter.getTags(moduleAPI);
                                if (tagsM != null) {
                                    for (int i = 0; i < tagsM.size(); i++) {
                                        if (tagsM.get(i).getTagType() != null) {
                                            if (tagsM.get(i).getTagType().equals("musicType")) {
                                                musicType = tagsM.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                ((TextView) componentViewResult.componentView).setText(musicType);
                                break;

                            case PAGE_VIDEO_DETAIL_LANGUAGE_LABEL_VALUE_KEY:
                                String languageL = "-";
                                if (BaseView.isTablet(context)) {
                                    languageL = "   -";
                                } else if (BaseView.isLandscape(context)) {
                                    languageL = "   -";
                                }
                                List<Tag> tagsV = appCMSPresenter.getTags(moduleAPI);
                                if (tagsV != null) {
                                    for (int i = 0; i < tagsV.size(); i++) {
                                        if (tagsV.get(i).getTagType() != null) {
                                            if (tagsV.get(i).getTagType().equals("language")) {
                                                languageL = tagsV.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                ((TextView) componentViewResult.componentView).setText(languageL);
                                break;
                            case PAGE_VIDEO_DETAIL_RATING_LABEL_VALUE_KEY:
                                String rating = "  -";
                                if (BaseView.isTablet(context)) {
                                    rating = "   -";
                                } else if (BaseView.isLandscape(context)) {
                                    rating = "   -";
                                }
                                List<Tag> tagsR = appCMSPresenter.getTags(moduleAPI);
                                if (tagsR != null) {
                                    for (int i = 0; i < tagsR.size(); i++) {
                                        if (tagsR.get(i).getTagType() != null) {
                                            if (tagsR.get(i).getTagType().equals("rating")) {
                                                rating = tagsR.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                ((TextView) componentViewResult.componentView).setText(rating);
                                break;

                            case PAGE_VIDEO_DETAIL_AIR_DATE_TIME_KEY:
                                if (moduleAPI != null &&
                                        moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getAirDateTime() >= 0) {

                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.millisToDate(moduleAPI.getContentData().get(0).getAirDateTime()));
                                }
                                break;

                            case PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_VALUE_KEY:
                                String difficulty = "-";
                                List<Tag> tagsD = appCMSPresenter.getTags(moduleAPI);
                                if (tagsD != null) {
                                    for (int i = 0; i < tagsD.size(); i++) {
                                        if (tagsD.get(i).getTagType() != null) {
                                            if (tagsD.get(i).getTagType().equals("difficulty")) {
                                                difficulty = tagsD.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                ((TextView) componentViewResult.componentView).setText(difficulty);
                                break;

                            case PAGE_VIDEO_DETAIL_CATEGORY_TITLE_KEY:
                                String caTitle = null;
                                Gist gistTi = appCMSPresenter.gist(moduleAPI);
                                if (gistTi != null &&
                                        gistTi.getTitle() != null) {

                                    caTitle = gistTi.getTitle();
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(caTitle));
                                    } else {
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(caTitle, Html.FROM_HTML_MODE_COMPACT));
                                    }
                                }

                                break;

                            case PAGE_INSTRUCTOR_INSTRACTOR_TEXT_KEY:
                                if (TextUtils.isEmpty(((TextView) componentViewResult.componentView).getText()))
                                    ((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
                                break;

                            case PAGE_INSTRUCTOR_DESCRIPTION_KEY:
                                String instructorDetails = null;
                                if (moduleAPI != null && moduleAPI.getContentData() != null
                                        && moduleAPI.getContentData().get(0) != null
                                        && moduleAPI.getContentData().get(0).getGist() != null
                                        && moduleAPI.getContentData().get(0).getGist().getBody() != null
                                        && !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getBody())) {
                                    instructorDetails = moduleAPI.getContentData().get(0).getGist().getBody();

                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(instructorDetails));
                                    } else {
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(instructorDetails, Html.FROM_HTML_MODE_COMPACT));
                                    }
                                    ((TextView) componentViewResult.componentView).setTextColor(Color.WHITE);
                                    componentViewResult.componentView.setSelected(true);
                                }
                                break;

                            case PAGE_INSTRUCTOR_TITLE_KEY:
                                String title = null;
                                Gist gistIn = appCMSPresenter.gist(moduleAPI);
                                if (gistIn != null &&
                                        gistIn.getFirstName() != null) {
                                    if (gistIn.getLastName() != null)
                                        title = gistIn.getFirstName() + " " + gistIn.getLastName();
                                    else
                                        title = gistIn.getFirstName() + " ";
                                    ((TextView) componentViewResult.componentView).setText("WITH " + title.toUpperCase());
                                    ((TextView) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                                }
                                break;

                            case PAGE_INSTRUCTOR_EQUIPMENT_TITLE_KEY:
                                break;
                            case PAGE_INSTRUCTOR_EQUIPMENT_DESCRIPTION_KEY:
                            case PAGE_VIDEO_DETAIL_CLASS_DURATION_KEY:
                                String classDuration = "0 min";
                                List<Tag> tags = appCMSPresenter.getTags(moduleAPI);
                                if (tags != null) {
                                    for (int i = 0; i < tags.size(); i++) {
                                        if (tags.get(i).getTagType() != null) {
                                            if (tags.get(i).getTagType().equals("classDuration")) {
                                                classDuration = tags.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(classDuration));
                                } else {
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(classDuration, Html.FROM_HTML_MODE_COMPACT));
                                }
                                break;

                            case PAGE_INSTRUCTOR_VIDEO_DESCRIPTION_KEY:
                            case PAGE_VIDEO_DESCRIPTION_KEY:
                                String videoDescription = null;
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getDescription() != null) {
                                    videoDescription = moduleAPI.getContentData().get(0).getGist().getDescription();
                                }
                                if (videoDescription != null) {
                                    videoDescription = videoDescription.trim();
                                }
                                if (!TextUtils.isEmpty(videoDescription)) {
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(videoDescription));
                                    } else {
                                        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(videoDescription, Html.FROM_HTML_MODE_COMPACT));
                                    }
                                } else if (!BaseView.isLandscape(context)) {
                                    componentViewResult.shouldHideComponent = true;
                                }
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getTitle() != null) {
                                    ViewTreeObserver textVto = componentViewResult.componentView.getViewTreeObserver();
                                    ViewCreatorMultiLineLayoutListener viewCreatorLayoutListener =
                                            new ViewCreatorMultiLineLayoutListener(((TextView) componentViewResult.componentView),
                                                    moduleAPI.getContentData().get(0).getGist().getTitle(),
                                                    videoDescription,
                                                    appCMSPresenter,
                                                    false,
                                                    appCMSPresenter.getBrandPrimaryCtaColor(),
                                                    appCMSPresenter.getGeneralTextColor(),
                                                    false);
                                    textVto.addOnGlobalLayoutListener(viewCreatorLayoutListener);
                                }
                                break;

                            case PAGE_AUTOPLAY_MOVIE_TITLE_KEY:
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getTitle())) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                                }
                                ViewTreeObserver titleTextVto = componentViewResult.componentView.getViewTreeObserver();
                                ViewCreatorTitleLayoutListener viewCreatorTitleLayoutListener =
                                        new ViewCreatorTitleLayoutListener((TextView) componentViewResult.componentView);
                                titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);
                                ((TextView) componentViewResult.componentView).setSingleLine(true);
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                componentViewResult.componentView.setSelected(true);
                                break;

                            case PAGE_VIDEO_TITLE_KEY:
                            case PAGE_SHOW_TITLE_KEY:
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getTitle())) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                                }
                                titleTextVto = componentViewResult.componentView.getViewTreeObserver();
                                viewCreatorTitleLayoutListener =
                                        new ViewCreatorTitleLayoutListener((TextView) componentViewResult.componentView);

                                if (component.getKey() != null &&
                                        !component.getKey().equals(context.getString(R.string.app_cms_page_show_image_video_key)) &&
                                        !BaseView.isTablet(context)) {
                                    viewCreatorTitleLayoutListener.setSpecifiedMaxWidthRatio(0.7f);
                                }
                                titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);
                                ((TextView) componentViewResult.componentView).setSingleLine(true);

                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                break;
                            case PAGE_VIDEO_SUBTITLE_KEY:
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null) {
                                    if (moduleAPI.getContentData().get(0).getGist() != null &&
                                            !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getContentType()) &&
                                            moduleAPI.getContentData().get(0).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.app_cms_video_content_type)) &&
                                            appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT) {
                                        setViewWithSubtitle(context, moduleAPI.getContentData().get(0),
                                                componentViewResult.componentView, appCMSPresenter);
                                    } else if (moduleAPI.getContentData().get(0).getSeason() != null) {
                                        setViewWithShowSubtitle(appCMSPresenter, context, moduleAPI.getContentData().get(0),
                                                componentViewResult.componentView, false);
                                    } else if (moduleAPI.getContentData().get(0) != null && moduleAPI.getContentData().get(0).getGist() != null &&
                                            moduleAPI.getContentData().get(0).getGist().getBundleList() != null) {
                                        setViewWithBundleSubtitle(appCMSPresenter, context, moduleAPI.getContentData().get(0),
                                                componentViewResult.componentView, false);
                                    }
                                }
                                break;

                            case PAGE_AUTOPLAY_MOVIE_SUBHEADING_KEY:
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().get(0) != null) {
                                    setViewWithSubtitle(context,
                                            moduleAPI.getContentData().get(0),
                                            componentViewResult.componentView, appCMSPresenter);
                                }
                                break;

                            case PAGE_VIDEO_PUBLISHDATE_KEY:
                                if (moduleAPI != null &&
                                        moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getPublishDate() != null) {
                                    long publishDateMillseconds = Long.parseLong(moduleAPI.getContentData().get(0).getGist().getPublishDate());
                                    long runtime = moduleAPI.getContentData().get(0).getGist().getRuntime();
                                    String secondsToTime = appCMSPresenter.convertSecondsToTime(runtime);
                                    StringBuilder builder = new StringBuilder(secondsToTime);
                                    if (moduleAPI.getContentData().get(0).getGist().getPublishDate() != null) {
                                        publishDateMillseconds = Long.parseLong(moduleAPI.getContentData().get(0).getGist().getPublishDate());
                                        String publishDate = appCMSPresenter.getLocalisedStrings().getPublishedText() + " " + appCMSPresenter.getDateFormat(publishDateMillseconds, "MMM dd, yyyy");
                                        if (runtime == 0) {
                                            builder.replace(0, secondsToTime.length(), publishDate);
                                        } else {
                                            builder.append(" | ");
                                            builder.append(publishDate);
                                        }
                                    }
                                    ((TextView) componentViewResult.componentView).setText(builder);
                                }
                                break;

                            case PAGE_GAME_DATE_KEY:
                                if (moduleAPI != null &&
                                        moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null) {
                                    long date = moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTime();
                                    String eventdate = CommonUtils.getDateFormatByTimeZone(date * 1000L, "MM/dd");
                                    StringBuilder builder = new StringBuilder(eventdate);
                                    builder.append(" - ");
                                    builder.append(CommonUtils.getDateFormatByTimeZone(date * 1000L, "hh:mm aa"));
                                    builder.append(" ");
                                    builder.append(moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTimeZone());
                                    ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                    ((TextView) componentViewResult.componentView).setText(builder);
                                    ((TextView) componentViewResult.componentView).setSingleLine(true);
                                    ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                }
                                //Now check if device is phone and watchlive button is visible than hide gaem datae and game time view
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null && !BaseView.isTablet(context)) {
                                    long eventDate = moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTime();
                                    long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                                    if (remainingTime > 0) {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    } else if ((moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent().equalsIgnoreCase("1")) || (remainingTime <= 0 && moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent().equalsIgnoreCase("1"))) {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    }
                                }
                                break;

                            case PAGE_VENUE_LABEL_KEY:
                                if (moduleAPI != null &&
                                        moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getVenue() != null) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0)
                                            .getGist().getEventSchedule().get(0).getVenue().toString());
                                    ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                }
                                //Now check if device is phone and watchlive button is visible than hide gaem datae and game time view
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null && !BaseView.isTablet(context)) {
                                    long eventDate = moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getEventTime();
                                    long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                                    if (remainingTime > 0)
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    else if ((moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent().equalsIgnoreCase("1")) || (remainingTime <= 0 && moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent().equalsIgnoreCase("1")))
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    else
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                }
                                break;

                            case PAGE_TIMER_TITLE_KEY:
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule() != null &&

                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist().getEventSchedule().get(0).getIsLiveEvent() != null) {

                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getFaceOffText());
                                    componentViewResult.componentView.setId(R.id.timer_until_face_off);
                                } else if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getPricing() != null &&
                                        moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0) {

                                    boolean isTimerShow = true;
                                    if ((moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {
                                        isTimerShow = true;
                                    } else if (((moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                                            moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() > 0) ||
                                            moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase("FREE"))) {
                                        isTimerShow = true;
                                    }
                                    if (isTimerShow) {
                                        String message = appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS ?
                                                appCMSPresenter.getLocalisedStrings().getFaceOffText() :
                                                appCMSPresenter.getLocalisedStrings().getContentAvailableText();
                                        ((TextView) componentViewResult.componentView).setText(message);
                                        componentViewResult.componentView.setId(R.id.timer_until_face_off);
                                    }
                                }

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                    ((TextView) componentViewResult.componentView).setTextAppearance(android.R.style.TextAppearance_Medium);
                                else
                                    ((TextView) componentViewResult.componentView).setTextAppearance(context, android.R.style.TextAppearance_Medium);
                                ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                                componentViewResult.componentView.setVisibility(View.GONE);
                                break;

                            case PAGE_VIDEO_AGE_LABEL_KEY:
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getParentalRating())) {
                                    String parentalRating = moduleAPI.getContentData().get(0).getParentalRating();
                                    ((TextView) componentViewResult.componentView).setText(parentalRating);
                                    ((TextView) componentViewResult.componentView).setSingleLine(true);
                                    componentViewResult.componentView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER);
                                    if (parentalRating.length() > 2)
                                        resizeText = true;
                                    applyBorderToComponent(context, componentViewResult.componentView, component, -1);
                                }
                                break;

                            case PAGE_AUTOPLAY_MOVIE_TIMER_LABEL_KEY:
                                componentViewResult.componentView.setId(R.id.countdown_id);
                                ((TextView) componentViewResult.componentView).setShadowLayer(20, 0, 0, Color.parseColor(CommonUtils.getColor(context, component.getTextColor())));
                                break;

                            case PAGE_ACTIONLABEL_KEY:
                            case PAGE_SETTINGS_NAME_VALUE_KEY:
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLoggedInUserName());
                                break;

                            case PAGE_SETTINGS_PHONE_VALUE_KEY:
                                if (!appCMSPresenter.getAppPreference().getLoggedInUserPhone().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.null_value)))
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getAppPreference().getLoggedInUserPhone());
                                break;

                            case PAGE_SETTINGS_EMAIL_VALUE_KEY:
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLoggedInUserEmail());
                                break;

                            case PAGE_SETTINGS_EMAIL_TITLE_KEY:
                                if (TextUtils.isEmpty(appCMSPresenter.getLoggedInUserEmail())) {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                    componentViewResult.shouldHideComponent = true;
                                }
                                break;

                            case PAGE_SETTINGS_PLAN_VALUE_KEY:
                                if (appCMSPresenter.isUserSubscribed() &&
                                        !TextUtils.isEmpty(appCMSPresenter.getActiveSubscriptionPlanName())) {
                                    if (appCMSPresenter.getUserSubscriptionPlanTitle() != null)
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getUserSubscriptionPlanTitle());
                                    else
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getActiveSubscriptionPlanName());
                                } else if (!appCMSPresenter.isUserSubscribed()) {
                                    if (appCMSPresenter.isAppAVOD()) {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    } else {
                                        if (metadataMap != null && metadataMap.getkNotSubscribed() != null)
                                            ((TextView) componentViewResult.componentView).setText(metadataMap.getkNotSubscribed());
                                        else
                                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.subscription_unsubscribed_plan_value)));
                                    }
                                } else if (!appCMSPresenter.isUserSubscribed()) {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                }
                                ((TextView) componentViewResult.componentView).setSingleLine();
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                break;

                            case PAGE_SETTINGS_PLAN_PROCESSOR_TITLE_KEY:
                                if (appCMSPresenter.isUserSubscribed() &&
                                        !TextUtils.isEmpty(appCMSPresenter.getActiveSubscriptionProcessor())) {
                                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    if (component.getNumberOfLines() != 0) {
                                        ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                    }
                                    ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                } else {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                    componentViewResult.shouldHideComponent = true;
                                }
                                if (metadataMap != null && metadataMap.getPaymentProcessorHeader() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getPaymentProcessorHeader());
                                else if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                    ((TextView) componentViewResult.componentView).setSingleLine(true);

                                }
                                break;

                            case PAGE_SETTINGS_PLAN_PROCESSOR_VALUE_KEY:
                                String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();
                                if (paymentProcessor != null && appCMSPresenter.isUserSubscribed())
                                    ((TextView) componentViewResult.componentView).setText(paymentProcessor);
                                else
                                    ((TextView) componentViewResult.componentView).setText("");
                                break;

                            case PAGE_SETTINGS_DOWNLOAD_QUALITY_PROFILE_KEY:
                                if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                                        appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                                    if (appCMSPresenter.getUserDownloadQualityPref() != null)
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getUserDownloadQualityPref());
                                    else
                                        ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getDownloadHighText());
                                }
                                break;

                            case PAGE_SETTINGS_APP_VERSION_VALUE_KEY:
                                ((TextView) componentViewResult.componentView).setText(context.getString(R.string.app_cms_app_version));
                                break;

                            case PAGE_SETTINGS_TITLE_KEY:
                                ((TextView) componentViewResult.componentView)
                                        .setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                                                .getBrand()
                                                .getGeneral()
                                                .getBlockTitleColor()));
                                if (metadataMap != null && metadataMap.getAccountHeader() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getAccountHeader());
                                else if (!TextUtils.isEmpty(component.getText()))
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                break;

                            case EDIT_PERSONALIZATION:
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                                        .getBrand()
                                        .getGeneral()
                                        .getBlockTitleColor()));

                                if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                }

                                if (appCMSPresenter.getAppCMSMain().getRecommendation() != null
                                        && appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation()
                                        && appCMSPresenter.getAppCMSMain().getRecommendation().isPersonalization()) {
                                    componentViewResult.componentView.setOnClickListener(v -> {
                                        appCMSPresenter.showLoader();
                                        AppCMSPresenter.isFromSettings = true;
                                        String currentUserId = appCMSPresenter.getLoggedInUser() != null ? appCMSPresenter.getLoggedInUser() : "guest-user-id";
                                        appCMSPresenter.getUserRecommendedGenres(currentUserId, s -> {
                                            appCMSPresenter.setSelectedGenreString(s);
                                            if (appCMSPresenter.getAppCMSMain().getRecommendation() != null &&
                                                    appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation() &&
                                                    appCMSPresenter.getAppCMSMain().getRecommendation().isPersonalization() &&
                                                    appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories() != null &&
                                                    appCMSPresenter.getAppCMSMain().getRecommendation().getRecommendCategories().size() > 0) {

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            appCMSPresenter.stopLoader();
                                                            appCMSPresenter.showRecommendationGenreDialog(null);
                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                }, 10);
                                            }
                                        }, false, true);
                                    });
                                } else {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                }
                                break;

                            case EDIT_PERSONALIZATION_HEADER:
                                if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                }

                                if (appCMSPresenter.isPersonalizationEnabled()) {
                                    componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            appCMSPresenter.showLoader();
                                            AppCMSPresenter.isFromSettings = true;
                                            String currentUserId = appCMSPresenter.getLoggedInUser() != null ? appCMSPresenter.getLoggedInUser() : "guest-user-id";
                                            appCMSPresenter.getUserRecommendedGenres(currentUserId, s -> {
                                                appCMSPresenter.setSelectedGenreString(s);
                                                if (appCMSPresenter.isPersonalizationEnabled()) {

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            try {
                                                                appCMSPresenter.stopLoader();
                                                                appCMSPresenter.showRecommendationGenreDialog(null);
                                                            } catch (Exception e) {
                                                            }
                                                        }
                                                    }, 10);
                                                } else {
                                                    appCMSPresenter.stopLoader();
                                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RECOMMENDATION_CATEGORY_EMPTY,
                                                            appCMSPresenter.getLocalisedStrings().getContentNotAvailable(),
                                                            false,
                                                            null,
                                                            null,
                                                            appCMSPresenter.getLocalisedStrings().getRecommendationTitle());
                                                }
                                            }, false, true);
                                        }
                                    });
                                } else {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                }
                                break;

                            case PAGE_PHOTOGALLERY_PREV_GALLERY_LABEL_KEY:
                                if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                }
                                componentViewResult.componentView.setId(R.id.photo_gallery_prev_label);
                                ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow_black, 0, 0, 0);

                                if (appCMSPresenter.getCurrentPhotoGalleryIndex() == 0) {
                                    ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#c8c8c8"));
                                    ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow_disable, 0, 0, 0);
                                } else {
                                    ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                    ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow_black, 0, 0, 0);
                                }
                                componentViewResult.componentView.setOnClickListener(v -> {
                                    int currentIndex = appCMSPresenter.getCurrentPhotoGalleryIndex();
                                    if (currentIndex == 0) {
                                        return;
                                    }
                                    if (appCMSPresenter.getRelatedPhotoGalleryIds() != null) {
                                        currentIndex--;
                                        appCMSPresenter.setCurrentPhotoGalleryIndex(currentIndex);
                                        appCMSPresenter.navigateToPhotoGalleryPage(appCMSPresenter.getRelatedPhotoGalleryIds().get(currentIndex),
                                                null, null, false);
                                    }
                                });
                                break;

                            case PAGE_PHOTOGALLERY_NEXT_GALLERY_LABEL_KEY:
                                if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());

                                }
                                componentViewResult.componentView.setId(R.id.photo_gallery_next_label);
                                ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);

                                if (appCMSPresenter.getRelatedPhotoGalleryIds() != null) {
                                    if (appCMSPresenter.getCurrentPhotoGalleryIndex() == appCMSPresenter.getRelatedPhotoGalleryIds().size() - 1) {
                                        ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor("#c8c8c8"));
                                        ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow_disable, 0);
                                        componentViewResult.componentView.setEnabled(false);
                                    } else {
                                        ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                        ((TextView) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0);
                                    }
                                    componentViewResult.componentView.setOnClickListener(v -> {
                                        int currentIndex = appCMSPresenter.getCurrentPhotoGalleryIndex();
                                        if (appCMSPresenter.getRelatedPhotoGalleryIds() != null &&
                                                currentIndex < appCMSPresenter.getRelatedPhotoGalleryIds().size()) {
                                            currentIndex = currentIndex + 1;
                                            appCMSPresenter.setCurrentPhotoGalleryIndex(currentIndex);
                                            appCMSPresenter.navigateToPhotoGalleryPage(appCMSPresenter.getRelatedPhotoGalleryIds().get(currentIndex),
                                                    null, null, false);
                                        }

                                    });
                                }
                                break;

                            case PAGE_DOWNLOAD_SETTING_TITLE_KEY:
                                if (moduleAPI != null &&
                                        moduleAPI.getContentData() != null &&
                                        !moduleAPI.getContentData().isEmpty() &&
                                        moduleAPI.getContentData().size() != 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getLanguages() != null) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getSelectLanguageText());
                                } else {
                                    if (metadataMap != null && metadataMap.getDownloadQualityTitle() != null)
                                        ((TextView) componentViewResult.componentView).setText(metadataMap.getDownloadQualityTitle().toUpperCase());
                                }
                                break;
                            case PAGE_REDEMPTION_ERROR_LABEL_KEY:

                                componentViewResult.componentView.setId(R.id.redemption_error_text_id);
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(CommonUtils.getColor(
                                        context,
                                        component.getTextColor())));
                                componentViewResult.componentView.setVisibility(View.INVISIBLE);
                            case PAGE_AGE_ERROR_LABEL_KEY:
                                if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setSingleLine(false);
                                    componentViewResult.componentView.setId(R.id.ageConsentError);
                                    ((TextView) componentViewResult.componentView).setMaxLines(2);
                                    ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(CommonUtils.getColor(context, component.getTextColor())));
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                    componentViewResult.componentView.setVisibility(View.INVISIBLE);

                                }
                                break;
                            case PAGE_AUTOPLAY_FINISHED_UP_TITLE_KEY:
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getFinishedText(component.getText()));
                                break;
                            case PAGE_AUTOPLAY_MOVIE_PLAYING_IN_LABEL_KEY:
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getPlayInText(component.getText()));
                                break;
                            case TEXT_VIEW_LANGUAGE:
                                if (metadataMap != null && metadataMap.getLanguageSettingsLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getLanguageSettingsLabel());
                                break;
                            case TEXT_VIEW_SUBSCRIPTION_AND_BILLING:
                                if (metadataMap != null && metadataMap.getSubscriptionHeader() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getSubscriptionHeader());
                                break;

                            case TEXT_VIEW_OR:
                                if (metadataMap != null && metadataMap.getOrSeparator() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getOrSeparator());
                                break;
                            case TEXT_VIEW_NAME:
                                if (metadataMap != null && metadataMap.getName() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getName());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                break;
                            case TEXT_VIEW_EMAIL:
                                if (metadataMap != null && metadataMap.getEmail() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getEmail());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                break;
                            case TEXT_VIEW_PLAN:
                                if (appCMSPresenter.isAppAVOD()) {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                } else {
                                    if (metadataMap != null && metadataMap.getSubscriptionPlanHeader() != null)
                                        ((TextView) componentViewResult.componentView).setText(metadataMap.getSubscriptionPlanHeader());
                                    if (component.getNumberOfLines() != 0) {
                                        ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                    }
                                }
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                break;
                            case TEXT_VIEW_APP_SETTINGS:
                                if (metadataMap != null && metadataMap.getAppSettingsLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getAppSettingsLabel().toUpperCase());
                                break;
                            case PAGE_USER_MANAGEMENT_AUTOPLAY_TEXT_KEY:
                                if (metadataMap != null && metadataMap.getAutoplayLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getAutoplayLabel());
                                if (!appCMSPresenter.isAutoPlayEnable()) {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                    componentViewResult.shouldHideComponent = true;
                                }
                                break;
                            case PAGE_SD_CARD_FOR_DOWNLOADS_TEXT_KEY:
                                if (metadataMap != null && metadataMap.getUseSdCardForDownloadsLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getUseSdCardForDownloadsLabel());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                                break;
                            case TEXT_VIEW_APP_VERSION:
                                if (metadataMap != null && metadataMap.getAppVersionLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getAppVersionLabel());
                                break;
                            case TEXT_VIEW_DOWNLOAD_SETTINGS:
                                if (metadataMap != null && metadataMap.getDownloadSettingsLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getDownloadSettingsLabel().toUpperCase());
                                if (!appCMSPresenter.isDownloadable())
                                    ((TextView) componentViewResult.componentView).setVisibility(View.GONE);
                                break;
                            case TEXT_VIEW_DOWNLOAD_QUALITY:
                                if (metadataMap != null && metadataMap.getDownloadQualityLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getDownloadQualityLabel());
                                if (!appCMSPresenter.isDownloadable())
                                    ((TextView) componentViewResult.componentView).setVisibility(View.GONE);
                                break;
                            case TEXT_VIEW_CELLULAR_DATA:
                                if (metadataMap != null && metadataMap.getCellularDataLabel() != null)
                                    ((TextView) componentViewResult.componentView).setText(metadataMap.getCellularDataLabel());
                                if (!appCMSPresenter.isDownloadable())
                                    ((TextView) componentViewResult.componentView).setVisibility(View.GONE);
                                break;
                            default:
                                if (!TextUtils.isEmpty(component.getText())) {
                                    ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());

                                }
                                break;
                        }
                    } else {
                        if (moduleType != PAGE_SEASON_TRAY_MODULE_KEY &&
                                moduleType != PAGE_SHOW_DETAIL_04_MODULE_KEY &&
                                moduleType != PAGE_SEASON_TAB_MODULE_KEY &&
                                componentKey != PAGE_HISTORY_WATCHED_TIME_KEY &&
                                moduleType != PAGE_SUBSCRIPTION_IMAGEROW_02_KEY) {
                            ((TextView) componentViewResult.componentView).setSingleLine(true);
                            ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                        }
                    }

                    if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                        componentViewResult.componentView.setBackgroundColor(
                                Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                    }
                    setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, componentViewResult.componentView);
                    if (component.getFontSize() > 0) {
                        int fontSize = component.getFontSize();
                        if (resizeText) {
                            fontSize = (int) (0.66 * fontSize);
                        }
                        ((TextView) componentViewResult.componentView).setTextSize(fontSize);
                    } else if (BaseView.getFontSize(context, component.getLayout()) > 0) {
                        int fontSize = (int) BaseView.getFontSize(context, component.getLayout());
                        if (resizeText) {
                            fontSize = (int) (0.66 * fontSize);
                        }
                        ((TextView) componentViewResult.componentView).setTextSize(fontSize);
                    }

                    if (componentKey == PAGE_EQUIPMENT_USER_TITLE_KEY) {
                        String welcome = appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText();
                        welcome = welcome.replaceAll("\\buser_name\\b", appCMSPresenter.getLoggedInUserName());
                        if (BaseView.isTablet(context) || BaseView.isLandscape(context)) {
                            welcome = welcome.replaceAll("\\bsec_line\\b", "do");
                        } else {
                            welcome = welcome.replaceAll("\\bsec_line\\b", "\n             do");
                        }
                        ((TextView) componentViewResult.componentView).setText(welcome);
                    }
                }

                break;

            case PAGE_IMAGE_KEY: {
                componentViewResult.componentView = ImageUtils.createImageView(context);

                if (componentViewResult.componentView == null) {
                    componentViewResult.componentView = new ImageView(context);
                }
                switch (componentKey) {
                    case VIEW_BROWSE_SORT:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.ic_sort);
                        componentViewResult.componentView.setOnClickListener(v -> appCMSPresenter.showBrowseSortDialog(v));
                        break;
                    case PAGE_IMAGE_FACEBOOK:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.ic_facebook);
                        break;
                    case PAGE_RIGHT_ARROW_KEY:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.ic_right_arrow);
                        if (moduleAPI != null && moduleAPI.getSettings() != null
                                && moduleAPI.getSettings().getShowMore()) {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(View.GONE);
                        }
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String action = component.getAction();
                            if (action != null && action.equalsIgnoreCase(context.getResources().getString(R.string.app_cms_action_browse_shows_key))) {
                                if (moduleAPI.getTitle() != null) {
                                    if (appCMSPresenter.getBrowsePage() != null)
                                        ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).selectNavItem(appCMSPresenter.getBrowsePage().getPageId());
                                    appCMSPresenter.navigateToBrowsePage(moduleAPI.getTitle(), null);
                                }
                            } else if (action != null && action.equalsIgnoreCase(context.getResources().getString(R.string.app_cms_action_concept_landing_page_key))) {
                                appCMSPresenter.navigateToPage(appCMSPresenter.getConceptPage().getPageId(),
                                        appCMSPresenter.getConceptPage().getPageFunction(),
                                        appCMSPresenter.getConceptPage().getPageUI(),
                                        false,
                                        true,   // TODO: 20/02/19  It should be handle as dynamic way for neou passing it by default.
                                        false,
                                        false,
                                        false,
                                        null);
                            }

                        });
                        break;
                    case PAGE_SPLASH_LETS_GO_BUTTON_KEY:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.lets_go);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchButtonSelectedAction(null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null);
                        });
                        break;

                    case PAGE_PHOTO_GALLERY_SELECTED_IMAGE:
                        String selectedImgUrl = "";
                        ImageView selectedImg = (ImageView) componentViewResult.componentView;
                        selectedImg.setId(R.id.photo_gallery_selectedImage);
                        selectedImg.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (moduleAPI.getContentData().get(0).getStreamingInfo() != null && moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets() != null) {
                            if (moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().size() > 0 && moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().get(0).getUrl() != null) {
                                selectedImgUrl = moduleAPI.getContentData().get(0).getStreamingInfo().getPhotogalleryAssets().get(0).getSecureUrl();
                            }
                        }
                        Glide.with(selectedImg.getContext()).load(selectedImgUrl).into(selectedImg);
                        break;

                    case PAGE_PHOTO_PLAYER_IMAGE:
                        String playerImgUrl = "";
                        ImageView playerImageView = (ImageView) componentViewResult.componentView;
                        playerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null &&
                                moduleAPI.getContentData().get(0).getGist().getImageGist() != null &&
                                moduleAPI.getContentData().get(0).getGist().getImageGist().get_3x4() != null) {
                            playerImgUrl = moduleAPI.getContentData().get(0).getGist().getImageGist().get_3x4();
                        }
                        Glide.with(playerImageView.getContext()).load(playerImgUrl).into(playerImageView);
                        ColorMatrix colorMatrix = new ColorMatrix();
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
                        playerImageView.setColorFilter(filter);

                        break;
                    case PAGE_AUTOPLAY_MOVIE_IMAGE_KEY:
                        if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null &&
                                !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())) {
                            int viewWidth = (int) BaseView.getViewWidth(context,
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            int viewHeight = (int) BaseView.getViewHeight(context,
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            String imageUrl = null;
                            if (!TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())) {
                                imageUrl = moduleAPI.getContentData().get(0).getGist().getVideoImageUrl();
                            } else if (moduleAPI.getContentData().get(0).getGist().getImageGist() != null &&
                                    !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9())) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9(),
                                        viewWidth,
                                        viewHeight);
                            }
                            if (!TextUtils.isEmpty(imageUrl)) {
                                if (viewHeight > 0 && viewWidth > 0 && viewHeight > viewWidth) {
                                    if (!ImageUtils.loadImage(
                                            (ImageView) componentViewResult.componentView,
                                            imageUrl,
                                            ImageLoader.ScaleType.START)) {
                                        Glide.with(context)
                                                .load(imageUrl)
                                                .apply(new RequestOptions().override(viewWidth, viewHeight))
                                                .into((ImageView) componentViewResult.componentView);
                                    }
                                } else if (viewWidth > 0) {
                                    if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                            imageUrl,
                                            ImageLoader.ScaleType.START)) {
                                        Glide.with(context)
                                                .load(imageUrl)
                                                .apply(new RequestOptions().override(viewWidth, viewHeight).centerCrop())
                                                .into((ImageView) componentViewResult.componentView);
                                    }
                                } else {
                                    if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                            imageUrl,
                                            ImageLoader.ScaleType.START)) {
                                        Glide.with(context)
                                                .load(imageUrl)
                                                .into((ImageView) componentViewResult.componentView);
                                    }
                                }
                            }
                            componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context,
                                    android.R.color.transparent));
                            componentViewResult.useWidthOfScreen = false;
                        } else {
                            ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.vid_image_placeholder_16x9);
                        }
                        break;

                    case PAGE_BADGE_IMAGE_KEY:
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                    case PAGE_VIDEO_DETAIL_BADGE_IMAGE_KEY:
                        if (moduleAPI != null && moduleAPI.getContentData() != null
                                && !moduleAPI.getContentData().isEmpty()
                                && moduleAPI.getContentData().get(0) != null
                                && moduleAPI.getContentData().get(0).getGist() != null
                                && moduleAPI.getContentData().get(0).getGist().getBadgeImages() != null
                                && !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getBadgeImages().get_16x9())) {
                            int viewWidth = (int) BaseView.getViewWidth(context,
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            int viewHeight = (int) BaseView.getViewHeight(context,
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);

                            ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                            if (viewHeight > 0 && viewWidth > 0 && viewWidth > viewHeight) {
                                try {
                                    String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            moduleAPI.getContentData().get(0).getGist().getBadgeImages().get_16x9(),
                                            viewWidth,
                                            viewHeight);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().override(viewWidth, viewHeight))
                                            .into((ImageView) componentViewResult.componentView);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            componentViewResult.useWidthOfScreen = !BaseView.isLandscape(context);
                        }
                        break;

                    case PAGE_BANNER_IMAGE:
                        ImageView imageView1 = (ImageView) componentViewResult.componentView;
                        imageView1.setImageResource(R.drawable.logo);
                        break;

                    case PAGE_LOGO_IMAGE_KEY:
                        ImageView logoImage = (ImageView) componentViewResult.componentView;
                        logoImage.setImageResource(R.drawable.logo_icon);
                        break;

                    case PAGE_THUMBNAIL_BADGE_IMAGE:
                        componentViewResult.componentView.setVisibility(View.GONE);
                        break;

                    case PAGE_BANNER_DETAIL_ICON:
                        componentViewResult.componentView = new ImageView(context);
                        String bannerUrl = null;
                        if (settings != null && settings.getImage() != null) {
                            bannerUrl = settings.getImage();
                        }
                        Glide.with(context)
                                .load(bannerUrl)
                                .into((ImageView) componentViewResult.componentView);
                        break;
                    case PAGE_SHOW_IMAGE_KEY:
                    case PAGE_VIDEO_IMAGE_KEY:
                        int placeHolderImage = R.drawable.vid_image_placeholder_land;/*BaseView.isLandscape(context) ? R.drawable.vid_image_placeholder_land : R.drawable.vid_image_placeholder_port;*/

                        if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().size() != 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null &&
                                (!TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getPosterImageUrl()) ||
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl()) ||
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getLandscapeImageUrl()))) {
                            int viewWidth = (int) BaseView.getViewWidth(context,
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            int viewHeight = (int) BaseView.getViewHeight(context,
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT);

                            ((ImageView) componentViewResult.componentView).setImageResource(placeHolderImage);
                            ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                            if (viewHeight > 0 && viewWidth > 0 && viewHeight > viewWidth) {
                                String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(0).getGist().getLandscapeImageUrl(),
                                        viewWidth,
                                        viewHeight);
                                if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                        imageUrl,
                                        ImageLoader.ScaleType.CENTER)) {
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().override(viewWidth, viewHeight)
                                                    .placeholder(placeHolderImage))
                                            .into((ImageView) componentViewResult.componentView);
                                }
                            } else if (viewWidth > 0) {
                                String videoImageUrl = "";
                                if (moduleAPI.getContentData().get(0).getGist().getVideoImageUrl() != null || moduleAPI.getContentData().get(0).getGist().getLandscapeImageUrl() != null) {
                                    videoImageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            moduleAPI.getContentData().get(0).getGist().getVideoImageUrl() == null ?
                                                    moduleAPI.getContentData().get(0).getGist().getLandscapeImageUrl() :
                                                    moduleAPI.getContentData().get(0).getGist().getVideoImageUrl(),
                                            viewWidth,
                                            viewHeight);
                                } else if (moduleAPI.getContentData().get(0).getGist().getImageGist() != null) {

                                    if (moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9() != null && !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9())) {
                                        videoImageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9(),
                                                viewWidth,
                                                viewHeight);
                                    }
                                }
                                if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                        videoImageUrl,
                                        ImageLoader.ScaleType.CENTER)) {
                                    Glide.with(context)
                                            .load(videoImageUrl)
                                            .apply(new RequestOptions().override(viewWidth, viewHeight)
                                                    .placeholder(placeHolderImage))
                                            .into((ImageView) componentViewResult.componentView);
                                }
                            } else {
                                if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                        moduleAPI.getContentData().get(0).getGist().getVideoImageUrl(),
                                        ImageLoader.ScaleType.CENTER)) {
                                    String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            moduleAPI.getContentData().get(0).getGist().getVideoImageUrl(),
                                            viewWidth,
                                            viewHeight);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().fitCenter().placeholder(placeHolderImage))
                                            .into((ImageView) componentViewResult.componentView);
                                }
                            }

                            componentViewResult.useWidthOfScreen = !BaseView.isLandscape(context);
                        } else {
                            Glide.with(context.getApplicationContext())
                                    .load(placeHolderImage)
                                    .into(((ImageView) componentViewResult.componentView));
                        }

                        break;

                    case PAGE_VIDEO_DETAIL_LANGUAGE_IMAGE_KEY:
                        List<Tag> tagsI = appCMSPresenter.getTags(moduleAPI);
                        if (tagsI != null) {
                            for (int i = 0; i < tagsI.size(); i++) {
                                if (tagsI.get(i).getTagType() != null) {
                                    if (tagsI.get(i).getTagType().equals("language") &&
                                            tagsI.get(i).getImages() != null &&
                                            tagsI.get(i).getImages().get_1x1() != null &&
                                            tagsI.get(i).getImages().get_1x1().getUrl() != null) {
                                        Glide.with(context)
                                                .load(tagsI.get(i).getImages().get_1x1().getUrl())
                                                .into((ImageView) (componentViewResult.componentView));
                                    }
                                }
                            }
                        }
                        break;

                    case PAGE_HOME_LIVE_AND_UPCOMING:
                        if (BaseView.isTablet(context)) {
                            ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.ic_live_upcoming_tab);
                        } else {
                            ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.live_upcoming);
                        }
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                    case PAGE_EDIT_PROFILE_IMAGE_KEY:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.profile);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                    case PAGE_PROFILE_RIGHT_ARROW_KEY:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.profile);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                    case PAGE_EDIT_PROFILE_CAMERA_BUTTON_KEY:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.ic_camera_black);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                        break;
                    default:
                        if (!TextUtils.isEmpty(component.getImageName())) {
                            if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                    component.getImageName(),
                                    ImageLoader.ScaleType.CENTER)) {
                                Glide.with(context)
                                        .load(component.getImageName())
                                        .into((ImageView) componentViewResult.componentView);
                            }
                        }

                        if (componentKey == PAGE_THUMBNAIL_IMAGE_KEY) {
                            ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        } else {
                            ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                        break;
                }
                if ((CommonUtils.isTrayModule(blockName) || CommonUtils.isRecommendationTrayModule(blockName)) && (componentKey == PAGE_THUMBNAIL_IMAGE_KEY
                        || componentKey == PAGE_THUMBNAIL_BADGE_IMAGE)) {
                    String ratio = BaseView.getViewRatio(context, component.getLayout(), "16:9");
                    int childViewWidth = (int) BaseView.getViewWidth(context, CommonUtils.getTrayWidth(ratio, context), ViewGroup.LayoutParams.WRAP_CONTENT);
                    int childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(childViewWidth, childViewHeight);
                    ((ImageView) componentViewResult.componentView).setLayoutParams(lp);
                }
                break;
            }
            case PAGE_BACKGROUND_IMAGE_TYPE_KEY:
                componentViewResult.componentView = new ImageView(context);
                if (jsonValueKeyMap.get(component.getView()) == PAGE_BACKGROUND_IMAGE_KEY) {
                    ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.logo);
                    ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                if (componentKey == PAGE_SPLASH_LOGO_KEY) {
                    ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.logo_icon);
                    ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                break;

            case PAGE_PROGRESS_VIEW_KEY:
                int color = ContextCompat.getColor(context, R.color.colorAccent);

                if (!TextUtils.isEmpty(appCMSPresenter.getAppCtaBackgroundColor())) {
                    color = Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppCtaBackgroundColor()));
                }

                final int progressColor = color;
                componentViewResult.componentView = new ProgressBar(context,
                        null,
                        android.R.attr.progressBarStyleHorizontal) {
                    Paint paint = new Paint();

                    @Override
                    public void onDraw(Canvas canvas) {
                        super.onDraw(canvas);
                        int count = canvas.save();
                        int toX = (int) ((float) canvas.getWidth() * ((float) getProgress() / 100.0f));

                        paint.setColor(progressColor);
                        canvas.drawRect(0, 0, toX, canvas.getHeight(), paint);
                        getBackground().draw(canvas);
                        canvas.restoreToCount(count);
                    }
                };

                ((ProgressBar) componentViewResult.componentView).getProgressDrawable()
                        .setColorFilter(color, PorterDuff.Mode.SRC_IN);
                componentViewResult.componentView.setBackgroundColor(color & 0x44ffffff);

                if (appCMSPresenter.isUserLoggedIn()) {
                    ((ProgressBar) componentViewResult.componentView).setMax(100);
                    ((ProgressBar) componentViewResult.componentView).setProgress(0);
                    if (moduleAPI != null && moduleAPI.getContentData() != null &&
                            !moduleAPI.getContentData().isEmpty() &&
                            moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getGist() != null) {
                        if (moduleAPI.getContentData()
                                .get(0).getGist().getWatchedPercentage() > 0) {
                            componentViewResult.componentView.setVisibility(View.VISIBLE);
                            ((ProgressBar) componentViewResult.componentView)
                                    .setProgress(moduleAPI.getContentData()
                                            .get(0).getGist().getWatchedPercentage());
                        } else {
                            long watchedTime =
                                    moduleAPI.getContentData().get(0).getGist().getWatchedTime();
                            long runTime =
                                    moduleAPI.getContentData().get(0).getGist().getRuntime();
                            if (watchedTime > 0 && runTime > 0) {
                                long percentageWatched = (long) (((double) watchedTime / (double) runTime) * 100.0);
                                ((ProgressBar) componentViewResult.componentView)
                                        .setProgress((int) percentageWatched);
                                componentViewResult.componentView.setVisibility(View.VISIBLE);
                            } else {
                                componentViewResult.componentView.setVisibility(View.INVISIBLE);
                                ((ProgressBar) componentViewResult.componentView).setProgress(0);
                            }
                        }
                    } else {
                        componentViewResult.componentView.setVisibility(View.INVISIBLE);
                        ((ProgressBar) componentViewResult.componentView).setProgress(0);
                    }
                } else {
                    componentViewResult.componentView.setVisibility(View.GONE);
                }


                break;
            case PAGE_BANNER_DETAIL_BACKGROUND:
                componentViewResult.componentView = new View(context);
                if (component.getBackgroundColor() != null && !TextUtils.isEmpty(component.getBackgroundColor())) {
                    componentViewResult.componentView.
                            setBackgroundColor(Color.parseColor(CommonUtils.getColor(context,
                                    component.getBackgroundColor())));
                }
                if (settings != null && settings.getImage() == null) {
                    componentViewResult.componentView.setVisibility(View.GONE);
                }
                break;

            case PAGE_CONCEPT_SEPARATOR_VIEW_KEY:
            case PAGE_SEPARATOR_VIEW_KEY:
            case PAGE_SEGMENTED_VIEW_KEY:
                componentViewResult.componentView = new View(context);

                if (moduleType == PAGE_SHOW_DETAIL_04_MODULE_KEY)
                    componentViewResult.componentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));


                if (componentKey == PAGE_SEARCH_SEPARATOR_VIEW_KEY) {
                    componentViewResult.addToPageView = true;
                }
                if (componentKey == DOWNLOAD_SEPARATE_KEY && !appCMSPresenter.isDownloadable()) {
                    componentViewResult.componentView.setVisibility(View.GONE);
                }

                if (componentKey == PAGE_RESULT_SEPARATOR_VIEW_KEY) {
                    if (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) {
                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                    } else {
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }

                }

                if (component.getBackgroundColor() != null && !TextUtils.isEmpty(component.getBackgroundColor())) {
                    componentViewResult.componentView.
                            setBackgroundColor(Color.parseColor(CommonUtils.getColor(context,
                                    component.getBackgroundColor())));
                } else if (!TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand().getGeneral()
                        .getTextColor())) {
                    componentViewResult.componentView.
                            setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                }
                if (moduleType != PAGE_EVENT_DETAIL_MODULE_KEY && moduleType != PAGE_AC_TEAM_SCHEDULE_MODULE_KEY) {

                    componentViewResult.componentView.setAlpha(0.6f);
                }

                if (componentKey == PAGE_VIDEO_DETAIL_EQUIPMENT_NEEDED_LIST_KEY) {
                    componentViewResult.componentView = new RecyclerView(context);
                    GridLayoutManager gridLayoutManager;
                    gridLayoutManager = new GridLayoutManager(context, 1,
                            GridLayoutManager.HORIZONTAL, false);
                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(gridLayoutManager);
                    List<EquipmentElement> list = new ArrayList<>();

                    List<Tag> tagsE = appCMSPresenter.getTags(moduleAPI);
                    if (tagsE != null) {
                        for (int i = 0; i < tagsE.size(); i++) {
                            if (tagsE.get(i).getTagType() != null) {
                                if (tagsE.get(i).getTagType().equals("equipment")) {
                                    EquipmentElement element = new EquipmentElement();
                                    if (tagsE.get(i).getImages() != null &&
                                            tagsE.get(i).getImages().get_1x1() != null &&
                                            tagsE.get(i).getImages().get_1x1().getUrl() != null) {
                                        element.setEquipment_needed(tagsE.get(i).getImages().get_1x1().getUrl());
                                    }
                                    if (tagsE.get(i).getTitle() != null) {
                                        element.setName(tagsE.get(i).getTitle());
                                    }
                                    list.add(element);
                                }
                            }
                        }
                    }
                    EquipmentGridAdapter equipmentGridAdapter = new EquipmentGridAdapter(list, componentKey, appCMSPresenter);
                    ((RecyclerView) componentViewResult.componentView).setAdapter(equipmentGridAdapter);
                }


                if (componentKey == PAGE_INSTRUCTOR_BACKGROUND_SEPARATOR_COLOR_KEY) {
                    componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));

                }

                if (componentKey == PAGE_INSTRUCTOR_SEPARATOR_KEY) {
                    if (moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getGist() != null &&
                            moduleAPI.getContentData().get(0).getGist().getRelatedVideos() == null ||
                            moduleAPI.getContentData().get(0).getGist().getRelatedVideos().size() == 0) {
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }

                }
                if (componentKey == PAGE_EQUIPMENT_GRID_KEY) {
                    //TODO change
                    componentViewResult.componentView = new RecyclerView(context);

                    int numOfColumn = BaseView.isTablet(context) ? (BaseView.isLandscape(context) ? 5 : 4) : 3;
                    GridLayoutManager gridLayoutManager;
                    gridLayoutManager = new GridLayoutManager(context, numOfColumn,
                            GridLayoutManager.VERTICAL, false);

                    ((RecyclerView) componentViewResult.componentView)
                            .setLayoutManager(gridLayoutManager);
                    List<EquipmentElement> list = new ArrayList<>();
                    String[] names = new String[]{"8 KILO KETTLE BELLS", "12 KILO KETTLE BELLS", "16 KILO KETTLE BELLS", "SKIERS", "JUMP ROPE", "BOXING GLOVES", "BOWERS", "BOXES", "MINI BANDS", "5 LB DUMBBELLS", "10 LB DUMBBELLS", "20 LB DUMBBELLS", "RESISTANCE BANDS", "RESISTANCE BANDS", "TREADMILL"};

                    for (int i = 0; i < 14; i++) {
                        String urlE = "drawable/" + "equipment_" + i;
                        int imageKey = context.getResources().getIdentifier(urlE, "drawable", context.getPackageName());

                        EquipmentElement element = new EquipmentElement();
                        element.setName(names[i]);
                        element.setImage(imageKey);
                        list.add(element);
                    }
                    EquipmentGridAdapter equipmentGridAdapter = new EquipmentGridAdapter(list, componentKey, appCMSPresenter);
                    ((RecyclerView) componentViewResult.componentView).setAdapter(equipmentGridAdapter);
                    RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                        @Override
                        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                            if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                                rv.requestDisallowInterceptTouchEvent(false);
                            } else {
                                rv.requestDisallowInterceptTouchEvent(true);
                            }
                            return false;
                        }

                        @Override
                        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                            if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                                rv.requestDisallowInterceptTouchEvent(false);
                            } else {
                                rv.requestDisallowInterceptTouchEvent(true);
                            }
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                        }
                    };
                    ((RecyclerView) componentViewResult.componentView).addOnItemTouchListener(mScrollTouchListener);
                }

                break;

            case PAGE_MULTICOLUMN_TABLE_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                componentViewResult.componentView.setId(R.id.fight_stats_id);
                break;

            case PAGE_CASTVIEW_VIEW_KEY:
                if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT) {
                    String fontFamilyKey = null;
                    String fontFamilyKeyTypeParsed = null;
                    if (!TextUtils.isEmpty(component.getFontFamilyKey())) {
                        String[] fontFamilyKeyArr = component.getFontFamilyKey().split("-");
                        if (fontFamilyKeyArr.length == 2) {
                            fontFamilyKey = fontFamilyKeyArr[0];
                            fontFamilyKeyTypeParsed = fontFamilyKeyArr[1];
                        }
                    }

                    int fontFamilyKeyType = Typeface.NORMAL;
                    AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(fontFamilyKeyTypeParsed);
                    if (fontWeight == PAGE_TEXT_BOLD_KEY ||
                            fontWeight == PAGE_TEXT_SEMIBOLD_KEY ||
                            fontWeight == PAGE_TEXT_EXTRABOLD_KEY) {
                        fontFamilyKeyType = Typeface.BOLD;
                    }

                    String fontFamilyValue = null;
                    String fontFamilyValueTypeParsed = null;
                    if (!TextUtils.isEmpty(component.getFontFamilyValue())) {
                        String[] fontFamilyValueArr = component.getFontFamilyValue().split("-");
                        if (fontFamilyValueArr.length == 2) {
                            fontFamilyValue = fontFamilyValueArr[0];
                            fontFamilyValueTypeParsed = fontFamilyValueArr[1];
                        }
                    }

                    int fontFamilyValueType = Typeface.NORMAL;
                    fontWeight = jsonValueKeyMap.get(fontFamilyValueTypeParsed);

                    if (fontWeight == PAGE_TEXT_BOLD_KEY ||
                            fontWeight == PAGE_TEXT_SEMIBOLD_KEY ||
                            fontWeight == PAGE_TEXT_EXTRABOLD_KEY) {
                        fontFamilyValueType = Typeface.BOLD;
                    }

                    textColor = Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppCMSMain()
                            .getBrand().getGeneral().getTextColor()));

                    String directorTitle = null;
                    StringBuilder directorListSb = new StringBuilder();
                    String starringTitle = null;
                    StringBuilder starringListSb = new StringBuilder();

                    if (moduleAPI != null && moduleAPI.getContentData() != null &&
                            !moduleAPI.getContentData().isEmpty() &&
                            moduleAPI.getContentData().size() != 0 &&
                            moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getCreditBlocks() != null) {
                        for (CreditBlock creditBlock : moduleAPI.getContentData().get(0).getCreditBlocks()) {
                            AppCMSUIKeyType creditBlockType = jsonValueKeyMap.get(creditBlock.getTitle());
                            if (creditBlockType != null &&
                                    (creditBlockType == PAGE_VIDEO_CREDITS_DIRECTEDBY_KEY ||
                                            creditBlockType == PAGE_VIDEO_CREDITS_DIRECTOR_KEY ||
                                            creditBlockType == PAGE_VIDEO_CREDITS_DIRECTORS_KEY)) {
                                if (metadataMap != null && metadataMap.getDirectorLabel() != null)
                                    directorTitle = metadataMap.getDirectorLabel();
                                else if (!TextUtils.isEmpty(creditBlock.getTitle())) {
                                    directorTitle = creditBlock.getTitle().toUpperCase();
                                }
                                if (creditBlock != null && creditBlock.getCredits() != null) {
                                    for (int i = 0; i < creditBlock.getCredits().size(); i++) {
                                        directorListSb.append(creditBlock.getCredits().get(i).getTitle());
                                        if (i < creditBlock.getCredits().size() - 1) {
                                            directorListSb.append(", ");
                                        }
                                    }
                                }
                            } else if (creditBlockType != null &&
                                    creditBlockType == PAGE_VIDEO_CREDITS_STARRING_KEY) {
                                if (metadataMap != null && metadataMap.getStarringLabel() != null)
                                    starringTitle = metadataMap.getStarringLabel();
                                else if (!TextUtils.isEmpty(creditBlock.getTitle())) {
                                    starringTitle = creditBlock.getTitle().toUpperCase();
                                }
                                if (creditBlock != null && creditBlock.getCredits() != null) {
                                    for (int i = 0; i < creditBlock.getCredits().size(); i++) {
                                        if (!TextUtils.isEmpty(creditBlock.getCredits().get(i).getTitle())) {
                                            starringListSb.append(creditBlock.getCredits().get(i).getTitle());
                                            if (i < creditBlock.getCredits().size() - 1) {
                                                starringListSb.append(", ");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (directorListSb.length() == 0 && starringListSb.length() == 0) {
                        if (!BaseView.isLandscape(context)) {
                            componentViewResult.shouldHideComponent = true;
                        }
                    }

                    componentViewResult.componentView = new CreditBlocksView(context,
                            fontFamilyKey,
                            fontFamilyKeyType,
                            fontFamilyValue,
                            fontFamilyValueType,
                            directorTitle,
                            directorListSb.toString(),
                            starringTitle,
                            starringListSb.toString(),
                            textColor,
                            appCMSPresenter.getBrandPrimaryCtaColor(),
                            BaseView.getFontSizeKey(context, component.getLayout()),
                            BaseView.getFontSizeValue(context, component.getLayout()));

                    if (moduleAPI != null && !BaseView.isTablet(context)
                            && moduleAPI.getModuleType() != null
                            && (jsonValueKeyMap.get(moduleAPI.getModuleType())
                            == PAGE_AUTOPLAY_MODULE_KEY_01 ||
                            jsonValueKeyMap.get(moduleAPI.getModuleType())
                                    == PAGE_AUTOPLAY_MODULE_KEY_02 ||
                            jsonValueKeyMap.get(moduleAPI.getModuleType())
                                    == PAGE_AUTOPLAY_MODULE_KEY_03 ||
                            jsonValueKeyMap.get(moduleAPI.getModuleType())
                                    == PAGE_AUTOPLAY_MODULE_KEY_04 ||
                            jsonValueKeyMap.get(moduleAPI.getModuleType())
                                    == PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY)) {
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                }
                break;

            case PAGE_TEXTBOX:
                componentViewResult.componentView = new AppCompatEditText(context);
                if (component.getHint() != null) {
                    ((AppCompatEditText) componentViewResult.componentView).setHint(component.getHint());
                }
                if (component.getHintColor() != null) {
                    ((AppCompatEditText) componentViewResult.componentView).setHintTextColor(Color.parseColor(CommonUtils.getColor(context, component.getHintColor())));
                }
                componentViewResult.componentView.setBackgroundResource(R.drawable.rectangle);
                switch (componentKey) {
                    case PAGE_EMAIL_INPUT:
                        ((AppCompatEditText) componentViewResult.componentView).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        componentViewResult.componentView.setId(R.id.emailAddress);
                        break;
                    case PAGE_FIRST_NAME_INPUT:
                        componentViewResult.componentView.setId(R.id.firstName);
                        break;
                    case PAGE_LAST_NAME_INPUT:
                        componentViewResult.componentView.setId(R.id.lastName);
                        break;
                    case PAGE_PASSWORD_INPUT:
                        componentViewResult.componentView.setId(R.id.password);
                        ((AppCompatEditText) componentViewResult.componentView)
                                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                break;
            case PAGE_TEXTFIELD_KEY:
                componentViewResult.componentView = new TextInputLayout(context);
                TextInputEditText textInputEditText = new TextInputEditText(context);
                textInputEditText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                textInputEditText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
                setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, textInputEditText);
                int loginInputHorizontalMargin = context.getResources().getInteger(
                        R.integer.app_cms_login_input_horizontal_margin);
                textInputEditText.setPadding(loginInputHorizontalMargin, 0, loginInputHorizontalMargin, 0);
                textInputEditText.setTextSize(context.getResources().getInteger(R.integer.app_cms_login_input_textsize));
                TextInputLayout.LayoutParams textInputEditTextLayoutParams = new TextInputLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                switch (componentKey) {
                    case PAGE_EMAILTEXTFIELD_KEY:
                    case PAGE_EMAILTEXTFIELD2_KEY: {
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getEmailInput() != null) {
                            textInputEditText.setHint(moduleAPI.getMetadataMap().getEmailInput());
                        } else {
                            textInputEditText.setHint(context.getString(R.string.app_cms_email_input_text_hint));
                        }
                        textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        break;
                    }
                    case PAGE_PHONETEXTFIELD_KEY:
                        textInputEditText.setId(R.id.phone_input_box);
                        textInputEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getPhoneInput() != null)
                            textInputEditText.setHint(moduleAPI.getMetadataMap().getPhoneInput());
                        else
                            textInputEditText.setHint(context.getString(R.string.app_cms_phone_input_text_hint));
                        if (appCMSPresenter.unformattedPhone != null)
                            textInputEditText.setText(appCMSPresenter.unformattedPhone);
                        if (component.getMaxLength() > 0)
                            textInputEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(component.getMaxLength())});
                        break;
                    case PAGE_PHONE_COUNTRY_KEY:
                        textInputEditText.setId(R.id.phone_country_code);
                        textInputEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                        if (appCMSPresenter.unformattedPhoneCountryCode != null)
                            textInputEditText.setText(appCMSPresenter.unformattedPhoneCountryCode);
                        break;
                    case PAGE_PASSWORDTEXTFIELD_KEY:
                    case PAGE_PASSWORDTEXTFIELD2_KEY:
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getPasswordInput() != null) {
                            textInputEditText.setHint(moduleAPI.getMetadataMap().getPasswordInput());
                        } else {
                            textInputEditText.setHint(context.getString(R.string.app_cms_password_input_text_hint));
                        }
                        textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        ((TextInputLayout) componentViewResult.componentView).setPasswordVisibilityToggleEnabled(true);
                        break;

                    case PAGE_MOBILETEXTFIELD_KEY:
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getPhoneInput() != null) {
                            textInputEditText.setHint(moduleAPI.getMetadataMap().getPhoneInput());
                        } else {
                            textInputEditText.setHint(context.getString(R.string.app_cms_phone_input_text_hint));
                        }
                        textInputEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                        break;
                    case PAGE_SUBSCRIBE_EMAIL_KEY:
                        textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        textInputEditText.setId(R.id.subscribe_edit_text_id);
                        RecyclerView view = appCMSPresenter.getCurrentActivity().findViewById(R.id.home_nested_scroll_view);
                        if (view != null) {
                            view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
                        } else {
                            textInputEditText.requestFocus();
                        }
                        textInputEditText.setHintTextColor(ContextCompat.getColor(context, android.R.color.white));
                        textInputEditText.setTextColor(ContextCompat.getColor(context, android.R.color.white));
                        textInputEditText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));

                        if (BaseView.isTablet(context)) {
                            textInputEditTextLayoutParams.setMargins(0, 0, 100, 0);
                        }
                        break;

                    case PAGE_REDEMPTIONTEXTFIELD_KEY:
                        textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        textInputEditText.setId(R.id.redemption_edit_text_id);
                        textInputEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                        RecyclerView view1 = pageView.findViewById(R.id.home_nested_scroll_view);
                        if (view1 != null) {
                            view1.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
                        }
                        Button buttonRedeem = appCMSPresenter.getCurrentActivity().findViewById(R.id.redeem_button_key);

                        textInputEditText.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.redeem_button_key) != null) {
                                    if (!TextUtils.isEmpty(s.toString().trim())) {
                                        appCMSPresenter.getCurrentActivity().findViewById(R.id.redeem_button_key).setBackgroundColor(appCMSPresenter.getButtonBorderColor());
                                        appCMSPresenter.getCurrentActivity().findViewById(R.id.redeem_button_key).setEnabled(true);
                                    } else {
                                        appCMSPresenter.getCurrentActivity().findViewById(R.id.redeem_button_key).setBackgroundColor(Color.GRAY);
                                        appCMSPresenter.getCurrentActivity().findViewById(R.id.redeem_button_key).setEnabled(false);

                                        TextView errorText = appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_error_text_id);
                                        if (errorText != null)
                                            errorText.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        break;
                    default:
                        break;
                }

                textInputEditText.setLayoutParams(textInputEditTextLayoutParams);

                ((TextInputLayout) componentViewResult.componentView).addView(textInputEditText);

                ((TextInputLayout) componentViewResult.componentView).setHintEnabled(false);
                break;

            case PAGE_PLAN_META_DATA_VIEW_KEY: {
                if (moduleAPI != null) {
                    if (moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                            moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        component.getLayout().getMobile().setHeight(0);
                        component.getLayout().getMobile().setWidth(0);
                        component.getLayout().getTabletPortrait().setWidth(0);
                        component.getLayout().getTabletLandscape().setWidth(0);
                        if (!appCMSPresenter.isSinglePlanFeatureAvailable()) {
                            componentViewResult.componentView = new ViewPlansMetaDataView(context,
                                    component,
                                    component.getLayout(),
                                    this,
                                    moduleAPI,
                                    jsonValueKeyMap,
                                    appCMSPresenter,
                                    settings);
                        }
                        if (appCMSPresenter.isMOTVApp()) {
                            componentViewResult.componentView = new ViewPlansMetaDataView(context,
                                    component,
                                    component.getLayout(),
                                    this,
                                    moduleAPI,
                                    jsonValueKeyMap,
                                    appCMSPresenter,
                                    settings);
                        }
                    }
                    if (moduleType == PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY) {
                        componentViewResult.componentView = new SubscriptionMetaDataView(context,
                                component,
                                component.getLayout(),
                                this,
                                moduleAPI,
                                jsonValueKeyMap,
                                appCMSPresenter,
                                settings,
                                appCMSAndroidModules, blockName);
                    }
                }
            }
            break;

            case PAGE_SETTINGS_KEY:
                if (moduleAPI != null) {
                    componentViewResult.componentView = createModuleView(context,
                            component,
                            moduleAPI,
                            appCMSAndroidModules,
                            pageView,
                            jsonValueKeyMap,
                            appCMSPresenter, component.isConstrainteView(), settings);
                }
                break;

            case PAGE_TOGGLE_BUTTON_KEY: {
                componentViewResult.componentView = new Switch(context);
                ((Switch) componentViewResult.componentView).getTrackDrawable().setTint(
                        appCMSPresenter.getGeneralTextColor());
                int switchOnColor = appCMSPresenter.getBrandPrimaryCtaColor();
                int switchOffColor = appCMSPresenter.getBrandSecondaryCtaTextColor();
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        }, new int[]{
                        switchOnColor,
                        switchOffColor
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ((Switch) componentViewResult.componentView).setTrackTintMode(PorterDuff.Mode.MULTIPLY);
                    ((Switch) componentViewResult.componentView).setThumbTintList(colorStateList);
                } else {
                    ((Switch) componentViewResult.componentView).setButtonTintList(colorStateList);
                }

                if (componentKey == PAGE_AUTOPLAY_TOGGLE_BUTTON_KEY) {
                    if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            appCMSPresenter.isAutoPlayEnable()) {
                        ((Switch) componentViewResult.componentView)
                                .setChecked(appCMSPresenter.getAutoplayEnabledUserPref(context));
                        ((Switch) componentViewResult.componentView)
                                .setOnCheckedChangeListener((buttonView, isChecked)
                                        -> appCMSPresenter.setAutoplayEnabledUserPref(isChecked));
                    } else {
                        ((Switch) componentViewResult.componentView)
                                .setChecked(false);
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                }

                if (componentKey == PAGE_SD_CARD_FOR_DOWNLOADS_TOGGLE_BUTTON_KEY) {

                    if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                        ((Switch) componentViewResult.componentView)
                                .setChecked(appCMSPresenter.getUserDownloadLocationPref());
                        ((Switch) componentViewResult.componentView)
                                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    if (isChecked) {
                                        if (FileUtils.isRemovableSDCardAvailable(appCMSPresenter.getCurrentActivity())) {
                                            appCMSPresenter.setUserDownloadLocationPref(true);
                                        } else {
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SD_CARD_NOT_AVAILABLE,
                                                    null,
                                                    false,
                                                    null,
                                                    null, null);
                                            buttonView.setChecked(false);
                                        }
                                    } else {
                                        appCMSPresenter.setUserDownloadLocationPref(false);
                                    }
                                });
                        if (FileUtils.isExternalStorageAvailable(appCMSPresenter.getCurrentActivity())) {
                            componentViewResult.componentView.setEnabled(true);
                        } else {
                            componentViewResult.componentView.setEnabled(false);
                            ((Switch) componentViewResult.componentView).setChecked(false);
                        }
                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                    } else {
                        componentViewResult.componentView.setEnabled(false);
                        ((Switch) componentViewResult.componentView).setChecked(false);
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                }

                if (componentKey == PAGE_DOWNLOAD_VIA_CELLULAR_NETWORK_KEY) {
                    if (!appCMSPresenter.isDownloadable()) {
                        ((Switch) componentViewResult.componentView).setVisibility(View.GONE);
                    }
                    if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                        ((Switch) componentViewResult.componentView)
                                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    if (isChecked) {
                                        appCMSPresenter.setDownloadOverCellularEnabled(true);
                                    } else {
                                        appCMSPresenter.setDownloadOverCellularEnabled(false);
                                    }
                                });

                        ((Switch) componentViewResult.componentView).setChecked(appCMSPresenter
                                .getDownloadOverCellularEnabled());
                        componentViewResult.componentView.setEnabled(true);
                        ((Switch) componentViewResult.componentView).setChecked(appCMSPresenter.getDownloadOverCellularEnabled());
                    }
                }
            }
            break;
            case PAGE_VIDEO_LAYER:
                if (componentKey == PAGE_SPLASH_BACKGROUND_VIDEO_KEY) {
                    componentViewResult.componentView = new PlayerView(context);
                    DefaultTrackSelector trackSelector = new DefaultTrackSelector();
                    trackSelector.setTunnelingAudioSessionId(C.generateAudioSessionIdV21(context));
                    SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
                    appCMSPresenter.setLaunchingVideoPlayerView(player);
                    PlayerView splashPlayerView = ((PlayerView) componentViewResult.componentView);
                    splashPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    splashPlayerView.setPlayer(player);
                    //   System.out.println("pause"+appCMSPresenter.getPlayerView().hashCode());
                    final RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(context);
                    DataSpec dataSpec = new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.splashvideo));
                    pageView.disableSwipe();
                    try {
                        rawResourceDataSource.open(dataSpec);

                        DataSource.Factory factory = () -> rawResourceDataSource;
                        MediaSource videoSource = new ExtractorMediaSource.Factory(factory).createMediaSource(rawResourceDataSource.getUri());
                        player.prepare(videoSource);

                    } catch (RawResourceDataSource.RawResourceDataSourceException e) {
                        e.printStackTrace();
                    }
                    splashPlayerView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
                    splashPlayerView.hideController();
                    splashPlayerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                        @Override
                        public void onVisibilityChange(int visibility) {
                            if (visibility == View.VISIBLE) {
                                splashPlayerView.hideController();
                            }
                        }
                    });

                    player.setPlayWhenReady(true);
                    player.addListener(new Player.EventListener() {
                        @Override
                        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                        }

                        @Override
                        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                        }

                        @Override
                        public void onLoadingChanged(boolean isLoading) {

                        }

                        @Override
                        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                            switch (playbackState) {
                                case Player.STATE_ENDED:
                                    player.release();
                                    if (appCMSPresenter.getCurrentAppCMSBinder().getPageId() != null && appCMSPresenter.getCurrentAppCMSBinder().getPageId().equalsIgnoreCase(pageId))
                                        appCMSPresenter.navigateToHomePage(false);
                                    break;
                                case Player.STATE_READY:
                                    appCMSPresenter.dismissPopupWindowPlayer(true);
                                    if (!((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).isActive) {
                                        player.release();
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onRepeatModeChanged(int repeatMode) {

                        }

                        @Override
                        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                        }

                        @Override
                        public void onPlayerError(ExoPlaybackException error) {

                        }

                        @Override
                        public void onPositionDiscontinuity(int reason) {

                        }

                        @Override
                        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                        }

                        @Override
                        public void onSeekProcessed() {

                        }
                    });
                }
                break;

            default:
                if (moduleAPI != null && component.getComponents() != null &&
                        pageView != null &&
                        !component.getComponents().isEmpty()) {
                    if (component.getBlockName() == null)
                        component.setBlockName(blockName);
                    componentViewResult.componentView = createModuleView(context,
                            component,
                            moduleAPI,
                            appCMSAndroidModules,
                            pageView,
                            jsonValueKeyMap,
                            appCMSPresenter, component.isConstrainteView(), settings);
                    componentViewResult.useWidthOfScreen = true;
                }
                break;
        }

        if (pageView != null) {
            pageView.addViewWithComponentId(new ViewWithComponentId.Builder()
                    .id(moduleId + component.getKey())
                    .view(componentViewResult.componentView)
                    .build());
        }
    }

    private void componentKeyVideoPlayButton(Module moduleAPI, AppCMSUIKeyType componentKey, Context context,
                                             int tintColor, AppCMSPresenter appCMSPresenter, Component component) {
        componentViewResult.componentView.setId(R.id.video_play_icon);
        componentViewResult.componentView.setVisibility(View.VISIBLE);
        if (componentKey == PAGE_VIDEO_DETAIL_CLASS_PLAY_BUTTON_KEY) {
            componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.neo_video_detail_play));
        } else {
            componentViewResult.componentView.setPadding(8, 8, 8, 8);
            componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
            if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && moduleAPI != null) {
                boolean isUserSubscribed = appCMSPresenter.isUserSubscribed();
                boolean isTveUser = appCMSPresenter.getAppPreference().getTVEUserId() != null;
                boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && (appCMSPresenter.getAppPreference().getUserPurchases() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                        && (contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getGist().getId())
                        || (moduleAPI.getContentData().get(0).getSeasonId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getSeasonId()))
                        || (moduleAPI.getContentData().get(0).getSeriesId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getSeriesId()))));

                if (contentTypeChecker.isContentSVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) &&
                        contentTypeChecker.isContentTVEMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) &&
                        contentTypeChecker.isContentTVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels())) {
                    if (!isContentPurchased && !isUserSubscribed && !isTveUser)
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                } else if (contentTypeChecker.isContentSVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) &&
                        contentTypeChecker.isContentTVEMonetization(moduleAPI.getContentData().get(0).getMonetizationModels())) {
                    if (!isUserSubscribed && !isTveUser)
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                } else if (contentTypeChecker.isContentTVEMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) &&
                        contentTypeChecker.isContentTVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels())) {
                    if (!isTveUser && !isContentPurchased)
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                } else if (contentTypeChecker.isContentSVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) &&
                        contentTypeChecker.isContentTVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels())) {
                    if (!isContentPurchased && !isUserSubscribed)
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                } else if (contentTypeChecker.isContentSVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) &&
                        contentTypeChecker.isContentFREEMonetization(moduleAPI.getContentData().get(0).getMonetizationModels())) {
                    componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                } else if (contentTypeChecker.isContentSVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) && !isUserSubscribed) {
                    componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                } else if (contentTypeChecker.isContentTVEMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) && !isTveUser) {
                    componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                } else if (contentTypeChecker.isContentTVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels()) && !isContentPurchased) {
                    componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                }
            }
            componentViewResult.componentView.getBackground().setTint(getTransparentColor(tintColor, 0.75f));
        }
        if (moduleAPI != null && moduleAPI.getModuleType() != null && moduleAPI.getModuleType().equalsIgnoreCase("BundleDetailModule")) {
            componentViewResult.componentView.setVisibility(View.GONE);
        }

        /**
         * if content has pricing info and content is tvod or ppv type
         * or content id bundle type than check prchased status from transactiondata obj. If it has not response then show no purchase dialog
         */
        if (moduleAPI != null && (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() != 0 &&
                moduleAPI.getContentData().get(0) != null) && ((moduleAPI.getContentData().get(0).getPricing() != null &&
                moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                ((moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_TVOD))
                        || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_PPV))) ||
                        (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))
                )) ||
                (moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().getBundlePricing() != null &&
                        moduleAPI.getContentData().get(0).getGist().getContentType().equalsIgnoreCase("BUNDLE")))) {
            boolean isTveContent = moduleAPI.getContentData().get(0).getSubscriptionPlans() != null && appCMSPresenter.getAppPreference().getTVEUserId() != null
                    && contentTypeChecker.isContentTVE(moduleAPI.getContentData().get(0).getSubscriptionPlans());
            boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getUserPurchases() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases()) &&
                    ((moduleAPI.getContentData().get(0).getSeriesId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getSeriesId()))
                            || ((moduleAPI.getContentData().get(0).getSeasonId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getSeasonId()))
                            || contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleAPI.getContentData().get(0).getGist().getId())));


            /**
             * check for price type svod+PPV and svod +tvod
             */

            if ((moduleAPI.getContentData().get(0).getPricing() != null && moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                    (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) || isTveContent || isContentPurchased) {
                componentViewResult.componentView.setVisibility(View.VISIBLE);

            }
           /* if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && moduleAPI.getContentData().get(0).getMonetizationModels() != null && contentTypeChecker.isContentTVODMonetization(moduleAPI.getContentData().get(0).getMonetizationModels())) {
                if (isContentPurchased)
                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                else
                    componentViewResult.componentView.setVisibility(View.GONE);

            } else*/
            if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                String contentType = moduleAPI.getContentData().get(0).getGist().getContentType();
                if (contentType.equalsIgnoreCase(context.getString(R.string.content_type_bundle))) {

                    appCMSPresenter.getTransactionDataResponse(moduleAPI.getContentData().get(0).getGist().getId(), appCMSTransactionDataResponse -> {
                        appCMSPresenter.stopLoader();
                        if (appCMSTransactionDataResponse != null
                                && appCMSTransactionDataResponse.getRecords() != null
                                && appCMSTransactionDataResponse.getRecords().size() > 0) {

                            Map<String, AppCMSTransactionDataValue> map = new HashMap<String, AppCMSTransactionDataValue>();
                            for (AppCMSTransactionDataValue item : appCMSTransactionDataResponse.getRecords()) {
                                map.put(item.getVideoId(), item);
                            }

                            List<Map<String, AppCMSTransactionDataValue>> list = new ArrayList<>();
                            list.add(map);
                            moduleAPI.getContentData().get(0).getGist().setObjTransactionDataValue(list);
                            moduleAPI.getContentData().get(0).getGist().setRentedDialogShow(false);
                        }

                        /***
                         * If getObjTransactionDataValue value does not have any data then user has not purchased this item
                         * hide the play button and show No Purchase dialog
                         *
                         * TODO Hot Fix for ML-740 ## This block must optimized  after fix of ML-757
                         */
                        if (!appCMSPresenter.isUserSubscribed() && (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() == null || (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                                moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                                moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() == 0))) {

                            componentViewResult.componentView.setVisibility(View.GONE);

//
                            if (!moduleAPI.getContentData().get(0).getGist().isRentedDialogShownAlready()) {
                                CommonUtils.showBudlePurchaseDialog(appCMSPresenter, moduleAPI);
                            }

                        } else if (moduleAPI.getContentData().get(0) != null
                                && moduleAPI.getContentData().get(0).getGist() != null
                                && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0
                                || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

                            /**
                             * If content is purchased but  has schedule start time which is less than current time or
                             * schedule end time which is greater than current time than hide the play icon
                             */
                            long timeToStart = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), "EEE MMM dd HH:mm:ss");
                            long timeEnd = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), "EEE MMM dd HH:mm:ss");
                            if ((moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 && timeToStart > 0)/* || (moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0 && timeEnd < 0)*/) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }

                    }, contentType);
                } else if (contentType.equalsIgnoreCase(context.getString(R.string.content_type_video))) {
                    isTVOD = false;
                    appCMSPresenter.getTransactionData(moduleAPI.getContentData().get(0).getGist().getId(), updatedContentDatum -> {
                        appCMSPresenter.stopLoader();
                        moduleAPI.getContentData().get(0).getGist().setObjTransactionDataValue(updatedContentDatum);
                        moduleAPI.getContentData().get(0).getGist().setRentedDialogShow(false);

                        isTVOD = true;
                        /***
                         * If getObjTransactionDataValue value does not have any data then user has not purchased this item
                         * hide the play button and show No Purchase dialog
                         *
                         * TODO Hot Fix for ML-740 ## This block must optimized  after fix of ML-757
                         */
                        if (!appCMSPresenter.isUserSubscribed()
                                && (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() == null
                                || (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null
                                && moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0
                                && moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() == 0))) {
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.video_play_icon).setVisibility(View.GONE);
//                                        componentViewResult.componentView.setVisibility(View.GONE);
//
                            if (!moduleAPI.getContentData().get(0).getGist().isRentedDialogShownAlready()) {
                                CommonUtils.showBudlePurchaseDialog(appCMSPresenter, moduleAPI);
                            }

                        } else if (moduleAPI.getContentData().get(0) != null
                                && moduleAPI.getContentData().get(0).getGist() != null
                                && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0
                                || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

                            /**
                             * If content is purchased but  has schedule start time which is less than current time or
                             * schedule end time which is greater than current time than hide the play icon
                             */
                            long timeToStart = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), "EEE MMM dd HH:mm:ss");
                            long timeEnd = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), "EEE MMM dd HH:mm:ss");
                            if ((moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 && timeToStart > 0)/* || (moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0 && timeEnd < 0)*/) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }
                        }


                    }, contentType);
                }
            }


        }
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() != 0
                && moduleAPI.getContentData().get(0) != null
                && moduleAPI.getContentData().get(0) != null &&
                moduleAPI.getContentData().get(0).getGist() != null
                && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

            /**
             * If content has not pricing info  but has schedule start time which is less than current time
             */
            long timeToStart = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), "EEE MMM dd HH:mm:ss");

            if ((moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 && timeToStart > 0)) {
                componentViewResult.componentView.setVisibility(View.GONE);
            }
        }

        componentViewResult.componentView.setOnClickListener(v -> {

            if (moduleAPI != null &&
                    moduleAPI.getContentData() != null &&
                    !moduleAPI.getContentData().isEmpty() &&
                    moduleAPI.getContentData().size() != 0 &&
                    moduleAPI.getContentData().get(0) != null && moduleAPI.getContentData().get(0).getPricing() != null
                    && moduleAPI.getContentData().get(0).getPricing().getType() != null) {
                if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && isTVOD && !CommonUtils.getUserPurchasedBundle().contains(moduleAPI.getContentData().get(0).getGist().getId())) {
                    CommonUtils.showBudlePurchaseDialog(appCMSPresenter, moduleAPI);
                    return;
                }
            }
            if (moduleAPI != null &&
                    moduleAPI.getContentData() != null &&
                    !moduleAPI.getContentData().isEmpty() &&
                    moduleAPI.getContentData().size() != 0 &&
                    moduleAPI.getContentData().get(0) != null) {
                VideoAssets videoAssets = null;
                if (moduleAPI.getContentData().get(0) != null && moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                        moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets() != null) {
                    videoAssets = moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets();
                }
                String vidUrl = "";
                if (videoAssets != null) {
                    vidUrl = videoAssets.getHls();
                }
                if (TextUtils.isEmpty(vidUrl) && (videoAssets != null && videoAssets.getMpeg() != null)) {
                    for (int i = 0; i < videoAssets.getMpeg().size() && TextUtils.isEmpty(vidUrl); i++) {
                        vidUrl = videoAssets.getMpeg().get(i).getUrl();
                    }
                }
                if (moduleAPI.getContentData() != null &&
                        !moduleAPI.getContentData().isEmpty() &&
                        moduleAPI.getContentData().size() != 0 &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getContentDetails() != null) {

                    List<String> relatedVideoIds = null;
                    if (moduleAPI.getContentData().get(0).getContentDetails() != null &&
                            moduleAPI.getContentData().get(0).getContentDetails().getRelatedVideoIds() != null) {
                        relatedVideoIds = moduleAPI.getContentData().get(0).getContentDetails().getRelatedVideoIds();
                    }
                    int currentPlayingIndex = -1;
                    if (relatedVideoIds == null) {
                        currentPlayingIndex = 0;
                    }


                    /**
                     * if pricing type is TVOD than first call rental API and check video end date
                     * if video end date is greater than current date than play video else show message
                     */
                    if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && moduleAPI.getContentData().get(0).getPricing() != null &&
                            moduleAPI.getContentData().get(0).getPricing().getType() != null &&


                            ((moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_TVOD))
                                    || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_PPV))) ||
                                    (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))
                            )
                    ) {
                        boolean isShowRentalPeriodDialog = false;

                        int finalCurrentPlayingIndex1 = currentPlayingIndex;
                        List<String> finalRelatedVideoIds1 = relatedVideoIds;

                        String rentalPeriod = "";
                        boolean isPurchased = false;
                        boolean isPlayable = false;

                        if (moduleAPI.getContentData().get(0).getPricing().getRent() != null &&
                                moduleAPI.getContentData().get(0).getPricing().getRent().getRentalPeriod() > 0) {
                            rentalPeriod = String.valueOf(moduleAPI.getContentData().get(0).getPricing().getRent().getRentalPeriod());
                        }
                        if (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                                moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                                moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() > 0) {
                            AppCMSTransactionDataValue obj = moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).get(moduleAPI.getContentData().get(0).getGist().getId());
                            isPurchased = true;
                            if (obj.getRentalPeriod() > 0) {
                                rentalPeriod = String.valueOf(obj.getRentalPeriod());
                            }


                            /**
                             * if transaction getdata api containf transaction end date .It means Rent API called before
                             * and we have shown rent period dialog before so dont need to show rent dialog again. else sow rent period dilaog
                             */
                            isShowRentalPeriodDialog = (obj.getPurchaseStatus() != null && obj.getPurchaseStatus().equalsIgnoreCase("RENTED"));

                        }

                        /**
                         * If pricing type is svod+tvod or svod+tvod then check if user is login
                         * ,if  condition is false then show subscribe message.
                         */
                        if (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {
                            if (appCMSPresenter.isUserSubscribed() || isPurchased) {
                                isPlayable = true;
                            } else if (CommonUtils.isPPVContent(appCMSPresenter.getCurrentContext(), moduleAPI.getContentData().get(0))) {
                                if (appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null) {
                                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentContext().getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getStringValue(appCMSPresenter.getCurrentContext().getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName()));
                                } else {
                                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentContext().getString(R.string.rental_title)), appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));
                                }
                                return;
                            } else {
                                if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                                    appCMSPresenter.showSubscribeMessage();
                                    isPlayable = false;
                                    return;
                                }

                            }
                        }

                        if (isShowRentalPeriodDialog) {

                            if (rentalPeriod == null || TextUtils.isEmpty(rentalPeriod)) {
                                rentalPeriod = "0" +
                                        "";
                            }
                            String msg = appCMSPresenter.getLanguageResourcesFile().getStringValue(appCMSPresenter.getCurrentActivity().getString(R.string.rent_time_dialog_mssg),
                                    rentalPeriod);
                            if (appCMSPresenter.getLocalisedStrings().getRentTimeDialogMsg(rentalPeriod) != null)
                                msg = appCMSPresenter.getLocalisedStrings().getRentTimeDialogMsg(rentalPeriod);
                            appCMSPresenter.showRentTimeDialog(retry -> {
                                if (retry) {
                                    appCMSPresenter.getRentalData(moduleAPI.getContentData().get(0).getGist().getId(), updatedRentalData -> {

                                        /**
                                         * upate transaction end time to download video if any
                                         */
                                        if (updatedRentalData != null && updatedRentalData.getTransactionEndDate() > 0) {
                                            appCMSPresenter.updateVideoTransactionEndTime(moduleAPI.getContentData().get(0).getGist().getId(), updatedRentalData.getTransactionEndDate());
                                        } else if (moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue() != null &&
                                                moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().size() > 0 &&
                                                moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).size() > 0) {
                                            AppCMSTransactionDataValue obj = moduleAPI.getContentData().get(0).getGist().getObjTransactionDataValue().get(0).get(moduleAPI.getContentData().get(0).getGist().getId());

                                            if (obj.getTransactionEndDate() > 0)
                                                appCMSPresenter.updateVideoTransactionEndTime(moduleAPI.getContentData().get(0).getGist().getId(), obj.getTransactionEndDate());
                                        }
                                        appCMSPresenter.launchVideoPlayer(moduleAPI.getContentData().get(0),
                                                moduleAPI.getContentData().get(0).getGist().getId(),
                                                finalCurrentPlayingIndex1, finalRelatedVideoIds1,
                                                moduleAPI.getContentData().get(0).getGist().getWatchedTime(),
                                                component.getAction());
                                    }, false, 0);
                                }
                            }, msg, "", "", "", true, true);
                        } else if (appCMSPresenter.isScheduleVideoPlayable(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                            appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getPermalink(),
                                    component.getAction(),
                                    moduleAPI.getContentData().get(0).getTitle(),
                                    null,
                                    moduleAPI.getContentData().get(0),
                                    false,
                                    currentPlayingIndex,
                                    relatedVideoIds);
                        }


                    } else if (moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getGist() != null && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

                        if (appCMSPresenter.isStartTimeAvailable(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                            appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getPermalink(),
                                    component.getAction(),
                                    moduleAPI.getContentData().get(0).getTitle(),
                                    null,
                                    moduleAPI.getContentData().get(0),
                                    false,
                                    currentPlayingIndex,
                                    relatedVideoIds);
                        }

                    } else {
                        try {
                            String customReceiverId = appCMSPresenter.getAppCMSMain().getFeatures().getCustomReceiverId();
                            boolean shouldCastDRMVideo = (!TextUtils.isEmpty(customReceiverId) && !customReceiverId.equalsIgnoreCase(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID));
                            if (moduleAPI.getContentData().get(0).isDRMEnabled() && !shouldCastDRMVideo) {
                                if (appCMSPresenter.isCastConnected()) {
                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DRM_NOT_CAST,
                                            appCMSPresenter.getLocalisedStrings().getCastDRMMessage(),
                                            false,
                                            appCMSPresenter::launchBlankPage,
                                            null, null);
                                    return;
                                }
                            }
                        } catch (Exception e) {
                        }
                        appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getPermalink(),
                                component.getAction(),
                                moduleAPI.getContentData().get(0).getTitle(),
                                null,
                                moduleAPI.getContentData().get(0),
                                false,
                                currentPlayingIndex,
                                relatedVideoIds);
                    }
                }
            }
        });
    }

    /**
     * This will match a module from a UI JSON response to a module in a API data JSON response.
     *
     * @param module          Module from a UI JSON response
     * @param appCMSPageAPI   Module from an API data JSON response
     * @param jsonValueKeyMap This is a hashmap that associates UI string values with value enumerations
     * @return A matching module UI JSON response to the input module API data JSON response
     */

    private Module matchModuleAPIToModuleUI(ModuleList module, AppCMSPageAPI appCMSPageAPI,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap, AppCMSPresenter appCMSPresenter) {

        if (appCMSPageAPI != null && appCMSPageAPI.getModules() != null) {
            if (appCMSPageAPI.getModuleIds().indexOf(module.getId()) != -1
                    && appCMSPageAPI.getModuleIds().size() == appCMSPageAPI.getModules().size()) {
                return appCMSPageAPI.getModules().get(appCMSPageAPI.getModuleIds().indexOf(module.getId()));
            }
            for (Module moduleAPI : appCMSPageAPI.getModules()) {
                if (module.getId().equals(moduleAPI.getId())) {
                    return moduleAPI;
                } else if (jsonValueKeyMap.get(module.getBlockName()) != null &&
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) != null &&
                        moduleAPI.getModuleType() != null &&
                        moduleAPI.getModuleType().equalsIgnoreCase("ShowDetailModule")) {
                    return moduleAPI;
                } else if (jsonValueKeyMap.get(module.getType()) != null &&
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) != null &&
                        jsonValueKeyMap.get(module.getType()) ==
                                jsonValueKeyMap.get(moduleAPI.getModuleType())) {
                    return moduleAPI;
                } else if (jsonValueKeyMap.get(module.getBlockName()) != null &&
                        moduleAPI.getModuleType() != null &&
                        moduleAPI.getModuleType().equalsIgnoreCase("ViewPlanModule")) {
                    return moduleAPI;
                } else if (jsonValueKeyMap.get(module.getType()) != null &&
                        jsonValueKeyMap.get(moduleAPI.getModuleType()) != null &&
                        (module.getType() != null && module.getType().equalsIgnoreCase("AC AutoPlayLandscape 01"))) {
                    return moduleAPI;
                }
            }

            if (jsonValueKeyMap.get(module.getBlockName()) != null) {
                switch (Objects.requireNonNull(jsonValueKeyMap.get(module.getBlockName()))) {
                    case UI_BLOCK_FILTER_02:
                    case UI_BLOCK_PROFILE_02:
                    case UI_BLOCK_PROFILE_03:
                    case UI_BLOCK_DOWNLOADS_03:
                    case UI_BLOCK_RESET_PASSWORD_03:
                    case UI_BLOCK_PLAYLIST:
                    case UI_BLOCK_SEARCH_04:
                    case UI_BLOCK_WATCHLIST_03:
                    case UI_BLOCK_HISTORY_04:
                        if (appCMSPageAPI.getModules() != null
                                && !appCMSPageAPI.getModules().isEmpty()) {
                            return appCMSPageAPI.getModules().get(0);
                        }
                        break;

                }
            }
            if (jsonValueKeyMap.get(module.getView()) != null) {
                switch (Objects.requireNonNull(jsonValueKeyMap.get(module.getView()))) {
                    case PAGE_ARTICLE_MODULE_KEY:
                    case PAGE_PHOTO_TRAY_MODULE_KEY:
                    case PAGE_HISTORY_01_MODULE_KEY:
                    case PAGE_HISTORY_02_MODULE_KEY:
                    case PAGE_PLAYLIST_MODULE_KEY:
                    case PAGE_SEE_ALL_CATEGORY_01_KEY:
                    case PAGE_SEE_ALL_CATEGORY_02_KEY:
                    case PAGE_WATCHLIST_01_MODULE_KEY:
                    case PAGE_FOLLOW_01_MODULE_KEY:
                    case PAGE_WATCHLIST_02_MODULE_KEY:
                    case PAGE_AUTOPLAY_MODULE_KEY_01:
                    case PAGE_AUTOPLAY_MODULE_KEY_02:
                    case PAGE_AUTOPLAY_MODULE_KEY_03:
                    case PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY:
                    case PAGE_DOWNLOAD_SETTING_MODULE_KEY:
                    case PAGE_LANGUAGE_SETTING_MODULE_KEY:
                    case PAGE_DOWNLOAD_01_MODULE_KEY:
                    case PAGE_DOWNLOAD_02_MODULE_KEY:
//                    case PAGE_AUTHENTICATION_MODULE_KEY:
                    case PAGE_AC_TEAM_SCHEDULE_MODULE_KEY:
                    case PAGE_API_TEAMDETAIL_MODULE_KEY:
                    case PAGE_GAME_DETAIL_MODULE_KEY:
                    case PAGE_PERSON_DETAIL_MODULE_KEY:
                    case PAGE_PERSON_DETAIL_01_MODULE_KEY:
                    case PAGE_PERSON_DETAIL_02_MODULE_KEY:
                    case PAGE_PERSON_DETAIL_03_MODULE_KEY:
                    case PAGE_SHOW_DETAIL_04_MODULE_KEY:
                    case PAGE_AC_ROSTER_MODULE_KEY:
                    case PAGE_LIBRARY_01_MODULE_KEY:
                    case PAGE_LIBRARY_02_MODULE_KEY:
                    case PAGE_AC_SEARCH_MODULE_KEY:
                    case PAGE_AC_SEARCH02_MODULE_KEY:
                    case PAGE_AC_SEARCH03_MODULE_KEY:
                    case PAGE_EVENT_CAROUSAL_04_MODULE_KEY:
                        if (appCMSPageAPI.getModules() != null
                                && !appCMSPageAPI.getModules().isEmpty()) {
                            return appCMSPageAPI.getModules().get(0);
                        }
                        break;
                    case PAGE_FIGHT_SUMMARY_MODULE_KEY:
                        if (appCMSPageAPI.getModules() != null
                                && !appCMSPageAPI.getModules().isEmpty()) {
                            return appCMSPageAPI.getModules().get(appCMSPageAPI.getModules().size() - 1);
                        }
                        break;
                    case PAGE_BUNDLEDETAIL_01_MODULE_KEY:
                    case PAGE_BUNDLEDETAIL_02_MODULE_KEY:
                        if (appCMSPageAPI.getModules() != null
                                && !appCMSPageAPI.getModules().isEmpty()) {
                            return appCMSPageAPI.getModules().get(1);
                        }
                        break;
                    case PAGE_EVENT_DETAIL_MODULE_KEY:
                        if (appCMSPageAPI.getModules() != null
                                && !appCMSPageAPI.getModules().isEmpty()) {
                            return appCMSPageAPI.getModules().get(1);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return null;
    }

    public View setFightSummaryModule(Context context, Module moduleAPI, Component component, Map<String, AppCMSUIKeyType> jsonValueKeyMap, Fights fights, AppCMSPresenter appCMSPresenter) {
        boolean isHeader = false;
//        LinearLayout ll = new LinearLayout(context);
        HorizontalScrollView scrollView = new HorizontalScrollView(context);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
        LinearLayout tableParentLayout = new LinearLayout(context);
        tableParentLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tableParentLayout.setOrientation(LinearLayout.VERTICAL);
        NestedScrollView nestedSCrollView = new NestedScrollView(context);
        nestedSCrollView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));


        TableLayout table = new TableLayout(context);
        table.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        TableLayout tableHeader = new TableLayout(context);
        tableHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        tableHeader.setGravity(Gravity.CENTER);
        RecyclerView homeRec = appCMSPresenter.getCurrentActivity().findViewById(R.id.home_nested_scroll_view);
        table.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    homeRec.requestDisallowInterceptTouchEvent(false);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    homeRec.requestDisallowInterceptTouchEvent(true);

                }
                return false;
            }
        });
        if (fights != null && fights.getRounds().size() > 0) {
            for (int i = -1; i < fights.getRounds().size(); i++) {
                TableRow row = new TableRow(context);
                row.setWeightSum(2.0f);
                row.setPadding(1, 1, 1, 1);

                TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

                for (EventDetailsColumnsName colTitle : EventDetailsColumnsName.values()) {
                    String cellValue = "";
                    if (i == -1) {
                        cellValue = colTitle.toString();
                        isHeader = true;
                    } else {
                        isHeader = false;

                        Rounds rounds = fights.getRounds().get(i);
                        if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.ROUND.toString())) {

                            if (fights != null) {
                                if (fights.getRounds().get(i).getFighterId().equalsIgnoreCase(fights.getFighter1_Id())) {
                                    cellValue = rounds.getRound();
                                } else {
                                    cellValue = "";
                                }
                            }

                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.TIME.toString())) {
                            if (fights.getRounds().get(i).getFighterId().equalsIgnoreCase(fights.getFighter1_Id())) {
                                cellValue = rounds.getRoundTime();
                            } else {
                                cellValue = "";
                            }
                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.FIGHTER.toString())) {
                            try {

                                if (fights != null) {
                                    if (fights.getRounds().get(i).getFighterId().equalsIgnoreCase(fights.getFighter1_Id())) {
                                        cellValue = fights.getFighter1_LastName();
                                    } else {
                                        cellValue = fights.getFighter2_LastName();
                                    }
                                }
                            } catch (Exception e) {
                                cellValue = " ";
                                Log.e(i + "-" + colTitle.toString(), e.toString());
                            }
                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.AS.toString())) {
                            try {
                                int as = Integer.parseInt(rounds.getPowerArmStrikesLanded()) + Integer.parseInt(rounds.getNonPowerArmStrikesLanded());
                                cellValue = String.valueOf(as);
                            } catch (Exception e) {
                                cellValue = "0";
                                Log.e(i + "-" + colTitle.toString(), e.toString());
                            }
                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.AS1.toString())) {
                            try {
                                int as = Integer.parseInt(rounds.getPowerArmStrikesLanded()) + Integer.parseInt(rounds.getNonPowerArmStrikesLanded());
                                int as1 = as * 100 / (Integer.parseInt(rounds.getTotalArmStrikesThrown()));
                                cellValue = String.valueOf(as1);
                            } catch (Exception e) {
                                cellValue = "0";
                                Log.e(i + "-" + colTitle.toString(), e.toString());
                            }
                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.LS.toString())) {
                            int ls = Integer.parseInt(rounds.getNonPowerLegStrikesLanded()) + Integer.parseInt(rounds.getPowerLegStrikesLanded());
                            cellValue = String.valueOf(ls);

                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.LS1.toString())) {
                            try {
                                int ls = Integer.parseInt(rounds.getNonPowerLegStrikesLanded()) + Integer.parseInt(rounds.getPowerLegStrikesLanded());

                                int ls1 = ls * 100 / Integer.parseInt(rounds.getTotalLegStrikesThrown());

                                cellValue = String.valueOf(ls1);
                            } catch (Exception e) {
                                cellValue = "0";

                                Log.e(i + "-" + colTitle.toString(), e.toString());
                            }
                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.GS.toString())) {
                            int gs = Integer.parseInt(rounds.getPowerGroundStrikesLanded()) + Integer.parseInt(rounds.getNonPowerGroundStrikesLanded());
                            cellValue = String.valueOf(gs);
                        } else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.GS1.toString())) {
                            try {
                                int gs = Integer.parseInt(rounds.getPowerGroundStrikesLanded()) + Integer.parseInt(rounds.getNonPowerGroundStrikesLanded());
                                int gs1 = gs * 100 / Integer.parseInt(rounds.getGroundStrikesThrown());

                                cellValue = String.valueOf(gs1);
                            } catch (Exception e) {
                                cellValue = "0";
                                Log.e(i + "-" + colTitle.toString(), e.toString());
                            }
                        } /*else if (colTitle.toString().equalsIgnoreCase(EventDetailsColumnsName.PLS.toString())) {

                            cellValue = String.valueOf("Unknown");
                        }*/
                    }
                    addTableRowCell(context, cellValue, row, isHeader, component, appCMSPresenter, jsonValueKeyMap);
                }
                row.setLayoutParams(params);


                //if row is for header then keep it outside nested scrollview
//                if (i == -1) {
//                    tableHeader.addView(row, params);
//                    View seperatorView = new View(context);
//                    seperatorView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
//                    seperatorView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
//                    tableHeader.setStretchAllColumns(true);
//
//                    tableHeader.addView(seperatorView);
//                    tableParentLayout.addView(tableHeader);
//                } else
                {
                    table.addView(row, params);
                    table.setGravity(Gravity.CENTER);
                    View seperatorView = new View(context);
                    seperatorView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1));
                    seperatorView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
                    table.addView(seperatorView);
                }

            }
        } else {
            TextView cell = new TextView(context);
            cell.setTextSize(15);

            cell.setTextColor(ContextCompat.getColor(context, R.color.color_grey));
            cell.setGravity(Gravity.CENTER);
            cell.setText(appCMSPresenter.getLocalisedStrings().getNoFightFoundText());
            tableParentLayout.addView(cell);
            tableParentLayout.setGravity(Gravity.CENTER);
            return tableParentLayout;

        }
        table.setStretchAllColumns(true);
        nestedSCrollView.addView(table);
//        tableParentLayout.addView(nestedSCrollView);
        nestedSCrollView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

//        scrollView.setBackgroundColor(Color.parseColor(getColor(context, component.getBackgroundColor())));
        scrollView.addView(nestedSCrollView);
//        nestedSCrollViewData.addView(nestedSCrollView);
        return scrollView;
    }

    /**
     * This applies a visual border around an input view.
     *
     * @param context     This is the context value that created UI components should use
     * @param view        This is the view to apply the border
     * @param component   This is the component object from the UI JSON response associated with the input view
     * @param forcedColor If this value does not equal -1 than this color will be applied to the border instead of the color specified in the component
     */
    private void applyBorderToComponent(Context context, View view, Component component, int forcedColor) {
        if (component.getBorderWidth() != 0 && component.getBorderColor() != null) {
            if (component.getBorderWidth() > 0 && !TextUtils.isEmpty(component.getBorderColor())) {
                GradientDrawable viewBorder = new GradientDrawable();
                viewBorder.setShape(GradientDrawable.RECTANGLE);
                if (forcedColor == -1) {
                    viewBorder.setStroke(component.getBorderWidth(),
                            Color.parseColor(CommonUtils.getColor(context, component.getBorderColor())));
                } else {
                    viewBorder.setStroke(4, forcedColor);
                }
                viewBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
                view.setBackground(viewBorder);
            }
        }
    }

    private void setCasting(boolean allowFreePlay,
                            AppCMSPresenter appCMSPresenter,
                            ImageButton mMediaRouteButton,
                            long watchedTime) {
        try {
            CastServiceProvider castProvider = CastServiceProvider.getInstance(appCMSPresenter.getCurrentActivity());
            castProvider.setAllowFreePlay(allowFreePlay);

            CastServiceProvider.ILaunchRemoteMedia callBackRemotePlayback = castingModeChromecast -> {
                CastHelper castHelper = CastHelper.getInstance(appCMSPresenter.getCurrentActivity());
                if ((castHelper.getRemoteMediaClient() != null &&
                        !castHelper.getRemoteMediaClient().isPlaying()) ||
                        (castHelper.getStartingFilmId() != null &&
                                videoPlayerViewBinder != null &&
                                videoPlayerViewBinder.getContentData() != null &&
                                videoPlayerViewBinder.getContentData().getGist() != null &&
                                videoPlayerViewBinder.getContentData().getGist().getId() != null &&
                                !castHelper.getStartingFilmId().equals(videoPlayerViewBinder.getContentData().getGist().getId()))) {

                    if (videoPlayerViewBinder != null) {
                        if (videoPlayerView != null) {
                            videoPlayerView.pausePlayer();
                        }
                        long castPlayPosition = watchedTime * 1000;
                        if (!isCastConnected) {
                            castPlayPosition = videoPlayerView.getCurrentPosition();
                        }

                        castHelper.launchRemoteMedia(appCMSPresenter,
                                videoPlayerViewBinder.getRelateVideoIds(),
                                videoPlayerViewBinder.getContentData().getGist().getId(),
                                castPlayPosition,
                                videoPlayerViewBinder,
                                true,
                                null);

                        if (!BaseView.isTablet(appCMSPresenter.getCurrentActivity())) {
                            appCMSPresenter.restrictPortraitOnly();
                        }

                        // appCMSPresenter.sendExitFullScreenAction(false);
                    }
                }
            };

            castProvider.setActivityInstance(appCMSPresenter.getCurrentActivity(),
                    mMediaRouteButton);
            castProvider.onActivityResume();

            castProvider.setRemotePlaybackCallback(callBackRemotePlayback);
            isCastConnected = castProvider.isCastingConnected();
            castProvider.playChromeCastPlaybackIfCastConnected();
            if (isCastConnected) {

            } else {
                castProvider.setActivityInstance(appCMSPresenter.getCurrentActivity(),
                        mMediaRouteButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e(TAG, "Error initializing cast provider: " + e.getMessage());
        }
    }

    public CustomVideoPlayerView playerView(Context context, String videoId, String key, AppCMSPresenter appCmsPresenter, Module moduleAPI, Settings settings) {
        //CustomVideoPlayerView videoPlayerView = new CustomVideoPlayerView(context, appCmsPresenter);
        String title = null;
        if (moduleAPI != null && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() != 0
                && moduleAPI.getContentData().get(0) != null
                && moduleAPI.getContentData().get(0).getGist() != null
                && moduleAPI.getContentData().get(0).getGist().getTitle() != null
                && !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getTitle())) {
            title = moduleAPI.getContentData().get(0).getGist().getTitle();
        }
        videoPlayerView = new CustomVideoPlayerView(context, appCmsPresenter, settings, moduleAPI);
        ContentDatum contentData = null;
        if (moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null &&
                moduleAPI.getContentData().size() > 0 &&
                moduleAPI.getContentData().get(CommonUtils.getTabPosition()) != null) {
            contentData = moduleAPI.getContentData().get(CommonUtils.getTabPosition());
        }

        if (videoId != null) {
            videoPlayerView.setVideoUri(videoId, appCmsPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, moduleAPI.getContentData().get(0));
            videoPlayerView.setVideoTitle(title);
            appCmsPresenter.setVideoPlayerViewCache(key, videoPlayerView);
            videoPlayerContent = videoPlayerView.getVideoPlayerContent();
        }
        return videoPlayerView;
    }

    void setAutoPlayImage(Context context, Component component, String imgUrl) {
        int viewWidth = (int) BaseView.getViewWidth(context,
                component.getLayout(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int viewHeight = (int) BaseView.getViewHeight(context,
                component.getLayout(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewHeight > 0 && viewWidth > 0 && viewHeight > viewWidth) {
            if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView, imgUrl, ImageLoader.ScaleType.CENTER)) {
                Glide.with(context)
                        .load(imgUrl)
                        .apply(new RequestOptions().override(viewWidth, viewHeight))
                        .into((ImageView) componentViewResult.componentView);
            }
        } else if (viewWidth > 0) {
            if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView, imgUrl, ImageLoader.ScaleType.CENTER)) {
                Glide.with(context)
                        .load(imgUrl)
                        .apply(new RequestOptions().override(viewWidth, viewHeight).centerCrop())
                        .into((ImageView) componentViewResult.componentView);
            }
        } else {
            if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView, imgUrl,
                    ImageLoader.ScaleType.CENTER)) {
                Glide.with(context)
                        .load(imgUrl)
                        .apply(new RequestOptions().override(viewWidth, viewHeight).centerCrop())
                        .into((ImageView) componentViewResult.componentView);
            }
        }
        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context,
                android.R.color.transparent));
        componentViewResult.useWidthOfScreen = false;
    }

    public void createFightStateRecorsView(Context context, AppCMSPresenter appCMSPresenter, Module moduleAPI, Component component, Map<String, AppCMSUIKeyType> jsonValueKeyMap, Fights fights) {

        LinearLayout fightStatsView = appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_stats_id);
        fightStatsView.setOrientation(LinearLayout.VERTICAL);
        if (fights != null) {
            fightStatsView.removeAllViews();

            View tableView = setFightSummaryModule(context, moduleAPI, component, jsonValueKeyMap, fights, appCMSPresenter);

            if (tableView.getParent() != null)
                ((ViewGroup) tableView.getParent()).removeAllViews();
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            fightStatsView.addView(tableView, p);
            fightStatsView.postInvalidate();
            fightStatsView.bringToFront();
            fightStatsView.setVisibility(View.VISIBLE);
        }
    }

    private void addTableRowCell(Context context, String colValue, TableRow row, boolean isHeader, Component component, AppCMSPresenter appCMSPresenter, Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams();
        TextView cell = new TextView(context);

        if (isHeader) {
            cell.setPadding(20, 4, 20, 4);
            cell.setTypeface(cell.getTypeface(), Typeface.ITALIC);
            cell.setTextSize(17);
            cell.setText(colValue + "  ");

        } else {
            cell.setPadding(20, 20, 20, 20);
            cell.setTextSize(15);
            cell.setText(colValue + "  ");
            setTypeFace(context,
                    appCMSPresenter,
                    jsonValueKeyMap,
                    component,
                    cell);

        }
        cell.setTextColor(ContextCompat.getColor(context, R.color.color_grey));
        cell.setGravity(Gravity.CENTER_VERTICAL);
        cell.setLayoutParams(textViewParams);
        row.addView(cell);
    }

    private void startTimer(Context context, AppCMSPresenter appCMSPresenter, long eventDate, long remainingTime) {

        countDownTimer = new CountDownTimer(remainingTime, countDownIntervalInMillis) {
            public void onTick(long millisUntilFinished) {
                long different = CommonUtils.getTimeIntervalForEvent(eventDate, "EEE MMM dd HH:mm:ss");

                if (different < 0 && countDownTimer != null) {

                    onFinish();
                } else if (different > 0) {
                    String[] scheduleTime = appCMSPresenter.geTimeFormat(different, false).split(":");
                    String[] timerText = context.getResources().getStringArray(R.array.timer_text);
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                        TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                        timerTile.setVisibility(View.VISIBLE);
                    }
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.video_play_icon) != null) {
                        Button video_play_icon = appCMSPresenter.getCurrentActivity().findViewById(R.id.video_play_icon);
                        video_play_icon.setVisibility(View.GONE);
                    }

                    if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id) != null) {
                        LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                        for (int i = 0; i < linearLayout.getChildCount(); i++) {
                            LinearLayout childLinearLayout = (LinearLayout) linearLayout.getChildAt(i);
                            TextView time = ((TextView) childLinearLayout.getChildAt(0));
                            TextView timeFormat = ((TextView) childLinearLayout.getChildAt(1));

                            time.setText(scheduleTime[i]);
                            time.setTypeface(time.getTypeface(), Typeface.BOLD);

                            timeFormat.setText(timerText[i]);
                            //timeFormat.setTextSize(14);
                        }
                    } else {
                        if (countDownTimer != null)
                            countDownTimer.onFinish();
                    }
                }
            }

            public void onFinish() {
                if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.watch_live_button) != null) {
                    Button watchLive = appCMSPresenter.getCurrentActivity().findViewById(R.id.watch_live_button);
                    watchLive.setVisibility(View.VISIBLE);
                }
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_summary_module_id) != null) {
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_summary_module_id).setVisibility(View.VISIBLE);
                }
                if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                    TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                    LinearLayout linearLayoutRoot = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id_root);
                    LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                    timerTile.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    if (linearLayoutRoot != null)
                        linearLayoutRoot.setVisibility(View.GONE);
                }
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                //By refreshing the page ,It will check all conditions again and set the data
                appCMSPresenter.sendRefreshPageAction();

            }
        }.start();
    }

    private void startTimerEvents(Context context, AppCMSPresenter appCMSPresenter, long eventDate, long remainingTime) {


        countDownTimer = new CountDownTimer(remainingTime, countDownIntervalInMillis) {
            public void onTick(long millisUntilFinished) {
                long different = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");

                if (different < 0) {

                    onFinish();
                } else {
                    String[] scheduleTime = appCMSPresenter.geTimeFormat(different, false).split(":");
                    String[] timerText = context.getResources().getStringArray(R.array.timer_text);
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                        TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                        timerTile.setVisibility(View.VISIBLE);
                    }

                    if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id) != null) {
                        LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                        for (int i = 0; i < linearLayout.getChildCount(); i++) {
                            LinearLayout childLinearLayout = (LinearLayout) linearLayout.getChildAt(i);
                            TextView time = ((TextView) childLinearLayout.getChildAt(0));
                            TextView timeFormat = ((TextView) childLinearLayout.getChildAt(1));

                            time.setText(scheduleTime[i]);
                            time.setTypeface(time.getTypeface(), Typeface.BOLD);

                            timeFormat.setText(timerText[i]);
                            // timeFormat.setTextSize(14);
                        }
                    } else {
                        if (countDownTimer != null)
                            countDownTimer.onFinish();
                    }
                }
            }

            public void onFinish() {
                if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.watch_live_button) != null) {
                    Button watchLive = appCMSPresenter.getCurrentActivity().findViewById(R.id.watch_live_button);
                    watchLive.setVisibility(View.VISIBLE);
                }
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_summary_module_id) != null) {
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_summary_module_id).setVisibility(View.VISIBLE);
                }
                if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                    TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                    LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                    timerTile.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                }
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                //By refreshing the page ,It will check all conditions again and set the data
                appCMSPresenter.sendRefreshPageAction();

            }
        }.start();
    }

    private Module removePastEventsAndShowNext24HoursEvents(Module moduleAPI) {
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < adapterData.size(); i++) {
                if (adapterData.get(i).getGist().getScheduleEndDate() >= System.currentTimeMillis() &&
                        millisToHours(adapterData.get(i).getGist().getScheduleStartDate() - System.currentTimeMillis()) <= 24) {
                    tempData.add(adapterData.get(i));
                }
            }
            moduleAPI.setContentData(tempData);
        }
        return moduleAPI;
    }

    private Module removePastEventsAndShowTodayEvents(Module moduleAPI) {
        Module newModuleApi = new Module();
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < adapterData.size(); i++) {
                if (adapterData.get(i).getGist().getScheduleEndDate() >= System.currentTimeMillis() &&
                        DateUtils.isToday(adapterData.get(i).getGist().getScheduleStartDate())) {
                    tempData.add(adapterData.get(i));
                }
            }
            newModuleApi.setContentData(tempData);
        }

        return newModuleApi;
    }

    private Module removePastEventsAndShowNext7Days(Module moduleAPI) {
        Module newModuleApi = new Module();
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < adapterData.size(); i++) {
                String[] next7Days = next7Days();

                if ((ArrayUtils.contains(next7Days, millisToDate(adapterData.get(i).getGist().getScheduleStartDate()))) &&
                        adapterData.get(i).getGist().getScheduleEndDate() >= System.currentTimeMillis()) {
                    tempData.add(adapterData.get(i));
                }
            }
            newModuleApi.setContentData(tempData);
        }

        return newModuleApi;
    }

    private Module getModuleAPI(Module moduleAPI) {
        Module newModuleApi = new Module();
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0) {
            ContentDatum contentDatum = moduleAPI.getContentData().get(0);
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < contentDatum.getImageList().size(); i++) {
                ContentDatum updatedContentDatum = new ContentDatum();

                Gist gist = new Gist();
                updatedContentDatum.setGist(gist);
                updatedContentDatum.setShowDetails(moduleAPI.getContentData().get(0).getShowDetails());

                ImageGist imageGist = new ImageGist();
                updatedContentDatum.getGist().setImageGist(imageGist);

                updatedContentDatum.getGist().getImageGist().set_16x9(contentDatum.getImageList().get(i).getUrl());

                tempData.add(updatedContentDatum);
            }
            newModuleApi.setContentData(tempData);
        }

        return newModuleApi;
    }

    private Module addItems(Module moduleAPI, int numberItems) {
        Module moduleItems = new Module();
        if (moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() > 0) {
            List<ContentDatum> adapterData = moduleAPI.getContentData();
            List<ContentDatum> tempData = new ArrayList<>();
            for (int i = 0; i < numberItems; i++) {
                tempData.add(adapterData.get(0));
            }

            moduleItems.setContentData(tempData);
        }
        return moduleItems;
    }

    private long millisToHours(long millis) {
        return (millis / (60 * 60 * 1000));

    }

    public enum AdjustOtherState {
        IGNORE,
        INITIATED,
        ADJUST_OTHERS
    }

    public enum EventDetailsColumnsName {
        ROUND("Round"),
        TIME("Time"),
        FIGHTER("Fighter"),
        AS("AS"),
        AS1("AS%"),
        LS("LS"),
        LS1("LS%"),
        GS("GS"),
        GS1("GS%");
//        PLS("PLS");

//        PLS("PLS");

        private final String text;

        /**
         * @param text
         */
        EventDetailsColumnsName(final String text) {
            this.text = text;
        }

        /* (non-Javadoc)
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return text;
        }


    }

    public static class VideoPlayerContent {
        long videoPlayTime = 0;
        String videoUrl;
        String ccUrl;
        boolean fullScreenEnabled;
    }

    static public class ComponentViewResult {
        public View componentView;
        public OnInternalEvent onInternalEvent;
        public boolean useMarginsAsPercentagesOverride;
        public boolean useWidthOfScreen;
        public boolean addToPageView;
        public boolean addBottomFloatingButton;
        public boolean shouldHideComponent;
        public boolean shouldHideModule;
    }

    /**
     * This class is used to handle events when a season is selected from the dropdown box.  It will
     * send an event message to repopulate the view with a list of episodes from the given season.
     */
    private static class OnSeasonSelectedListener implements
            AdapterView.OnItemSelectedListener,
            OnInternalEvent {

        private List<Season_> seasonData;
        private List<OnInternalEvent> onInternalEvents;
        private String moduleId;

        public OnSeasonSelectedListener(List<Season_> seasonData) {
            this.seasonData = seasonData;
            this.onInternalEvents = new ArrayList<>();
        }

        @Override
        public void addReceiver(OnInternalEvent e) {
            if (onInternalEvents != null) {
                onInternalEvents.add(e);
            }
        }

        @Override
        public void sendEvent(InternalEvent<?> event) {
            int internalEventsSize = onInternalEvents != null ? onInternalEvents.size() : 0;
            for (int i = 0; i < internalEventsSize; i++) {
                onInternalEvents.get(i).receiveEvent(event);
            }
        }

        @Override
        public void receiveEvent(InternalEvent<?> event) {

        }

        @Override
        public void cancel(boolean cancel) {

        }

        @Override
        public String getModuleId() {
            return moduleId;
        }

        @Override
        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (0 <= position && position < seasonData.size()) {
                sendEvent(new InternalEvent<>(seasonData.get(position).getEpisodes()));
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private static class FightSmmaryAdapterView extends ArrayAdapter<String> {
        private AppCMSPresenter appCMSPresenter;
        private Component component;
        private Map<String, AppCMSUIKeyType> jsonValueKeyMap;

        public FightSmmaryAdapterView(Context context,
                                      AppCMSPresenter appCMSPresenter,
                                      Component component,
                                      Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
            super(context, R.layout.season_title_dropdown);
            this.appCMSPresenter = appCMSPresenter;
            this.component = component;
            this.jsonValueKeyMap = jsonValueKeyMap;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View result = null;
            if (convertView != null && convertView instanceof TextView) {
                result = convertView;
            } else if (parent != null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_title_dropdown,
                        parent,
                        false);

                try {
                    if (!TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand()
                            .getCta().getPrimary().getTextColor())) {
                        ((TextView) result).setTextColor(
                                Color.parseColor(
                                        CommonUtils.getColor(parent.getContext(), appCMSPresenter.getAppCMSMain().getBrand()
                                                .getGeneral()
                                                .getBackgroundColor())));
                    }
                } catch (Exception e) {
                    //
                }

//                try {
//                    result.setBackgroundColor(Color.parseColor(
//                            getColor(parent.getContext(), appCMSPresenter.getAppCMSMain()
//                                    .getBrand().getCta().getPrimary().getBackgroundColor())));
//                } catch (Exception e) {
//                    //
//                }

                if (component.getFontSize() > 0) {
                    ((TextView) result).setTextSize(component.getFontSize());
                } else if (BaseView.getFontSize(parent.getContext(), component.getLayout()) > 0) {
                    ((TextView) result).setTextSize(BaseView.getFontSize(parent.getContext(), component.getLayout()));
                }

                setTypeFace(parent.getContext(),
                        appCMSPresenter,
                        jsonValueKeyMap,
                        component,
                        result);

                result.setPadding(8, 8, 8, 8);
            }

            ((TextView) result).setText(getItem(position));
//            ((TextView) result).setTextColor(R.color.color_white);

            return result;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View result = null;
            if (convertView != null && convertView instanceof TextView) {
                result = convertView;
            } else if (parent != null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_title_dropdown,
                        parent,
                        false);

                try {
                    if (!TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand()
                            .getCta().getPrimary().getTextColor())) {
                        ((TextView) result).setTextColor(
                                Color.parseColor(
                                        CommonUtils.getColor(parent.getContext(), appCMSPresenter.getAppCMSMain().getBrand()
                                                .getGeneral()
                                                .getBackgroundColor())));
                    }
                } catch (Exception e) {
                    //
                }

//                try {
//                    result.setBackgroundColor(Color.parseColor(
//                            getColor(parent.getContext(), appCMSPresenter.getAppCMSMain()
//                                    .getBrand().getCta().getPrimary().getBackgroundColor())));
//                } catch (Exception e) {
//                    //
//                }

                if (component.getFontSize() > 0) {
                    ((TextView) result).setTextSize(component.getFontSize());
                } else if (BaseView.getFontSize(parent.getContext(), component.getLayout()) > 0) {
                    ((TextView) result).setTextSize(BaseView.getFontSize(parent.getContext(), component.getLayout()));
                }

                setTypeFace(parent.getContext(),
                        appCMSPresenter,
                        jsonValueKeyMap,
                        component,
                        result);

                result.setPadding(8, 30, 8, 30);
            }

            if (result != null) {
                ((TextView) result).setText(getItem(position));
                System.out.println("on selection-" + getItem(position));
//                ((TextView) result).setTextColor(R.color.color_white);
            }

            return result;
        }
    }

    private static class SeasonsAdapterView extends ArrayAdapter<String> {
        private AppCMSPresenter appCMSPresenter;
        private Component component;
        private Map<String, AppCMSUIKeyType> jsonValueKeyMap;

        public SeasonsAdapterView(Context context,
                                  AppCMSPresenter appCMSPresenter,
                                  Component component,
                                  Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
            super(context, R.layout.season_title_dropdown);
            this.appCMSPresenter = appCMSPresenter;
            this.component = component;
            this.jsonValueKeyMap = jsonValueKeyMap;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View result = null;
            if (convertView != null && convertView instanceof TextView) {
                result = convertView;
            } else if (parent != null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_title_dropdown,
                        parent,
                        false);

                try {
                    if (!TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand()
                            .getCta().getPrimary().getTextColor())) {
                        ((TextView) result).setTextColor(
                                Color.parseColor(
                                        CommonUtils.getColor(parent.getContext(), appCMSPresenter.getAppCMSMain()
                                                .getBrand().getCta().getPrimary().getBackgroundColor())));
                    }
                } catch (Exception e) {
                    //
                }

                try {
                    result.setBackgroundColor(Color.parseColor(
                            CommonUtils.getColor(parent.getContext(), appCMSPresenter.getAppCMSMain().getBrand()
                                    .getGeneral()
                                    .getBackgroundColor())));
                } catch (Exception e) {
                    //
                }

                if (component.getFontSize() > 0) {
                    ((TextView) result).setTextSize(component.getFontSize());
                } else if (BaseView.getFontSize(parent.getContext(), component.getLayout()) > 0) {
                    ((TextView) result).setTextSize(BaseView.getFontSize(parent.getContext(), component.getLayout()));
                }

                setTypeFace(parent.getContext(),
                        appCMSPresenter,
                        jsonValueKeyMap,
                        component,
                        result);

                result.setPadding(8, 0, 8, 0);
            }

            ((TextView) result).setText(getItem(position));

            return result;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View result = null;
            if (convertView != null && convertView instanceof TextView) {
                result = convertView;
            } else if (parent != null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_title_dropdown,
                        parent,
                        false);

                try {
                    if (!TextUtils.isEmpty(appCMSPresenter.getAppCMSMain().getBrand()
                            .getCta().getPrimary().getTextColor())) {
                        ((TextView) result).setTextColor(
                                Color.parseColor(
                                        CommonUtils.getColor(parent.getContext(), appCMSPresenter.getAppCMSMain()
                                                .getBrand().getCta().getPrimary().getBackgroundColor())));
                    }
                } catch (Exception e) {
                    //
                }

                try {
                    result.setBackgroundColor(Color.parseColor(
                            CommonUtils.getColor(parent.getContext(), appCMSPresenter.getAppCMSMain().getBrand()
                                    .getGeneral()
                                    .getBackgroundColor())));
                } catch (Exception e) {
                    //
                }

                if (component.getFontSize() > 0) {
                    ((TextView) result).setTextSize(component.getFontSize());
                } else if (BaseView.getFontSize(parent.getContext(), component.getLayout()) > 0) {
                    ((TextView) result).setTextSize(BaseView.getFontSize(parent.getContext(), component.getLayout()));
                }

                setTypeFace(parent.getContext(),
                        appCMSPresenter,
                        jsonValueKeyMap,
                        component,
                        result);

                result.setPadding(8, 8, 8, 8);
            }

            if (result != null) {
                ((TextView) result).setText(getItem(position));
            }

            return result;
        }
    }

    /**
     * This class is used to associate the Watchlist icon with the user's watchlist status for
     * a specific video ID.  This also creates the click listener that responds with the correct
     * action for adding/removing an video from a user's watchlist.  This will also handle events
     * corresponding to the entitlement status of the user and the type of the app (AVOD or SVOD).  An
     * AVOD application will ask a user to login before adding a film to the watchlist if a user is not logged
     * in.  An SVOD application will also ask a user to subscribe first before adding a video to the watchlist
     * if the user is logged in but not subscribed.
     */
    public static class UpdateImageIconAction implements Action1<UserVideoStatusResponse> {
        private final ImageButton imageButton;
        private final AppCMSPresenter appCMSPresenter;
        private final List<String> filmIds;
        MetadataMap metadataMap;
        IEvent eventListener;
        private View.OnClickListener addClickListener;
        private View.OnClickListener removeClickListener;
        private ContentDatum contentDatum;
        private AppCMSUIKeyType componentKey;
        private int addResId, removeResId;
        private String tintcolor;

        public UpdateImageIconAction(ImageButton imageButton,
                                     AppCMSPresenter presenter,
                                     List<String> filmIds,
                                     ContentDatum contentDatum,
                                     AppCMSUIKeyType componentKey, MetadataMap metadataMap, String tintcolor) {
            this.imageButton = imageButton;
            this.appCMSPresenter = presenter;
            this.filmIds = filmIds;
            this.metadataMap = metadataMap;
            this.contentDatum = contentDatum;
            this.componentKey = componentKey;
            this.tintcolor = tintcolor;
            if (componentKey == PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY) {
                addResId = R.drawable.ic_heart_outline;
                removeResId = R.drawable.ic_heart;
            } else if (componentKey == PAGE_BOOKMARK_FLAG_KEY) {
                addResId = R.drawable.ic_bookmark_shape;
                removeResId = R.drawable.ic_bookmark_selected;
            } else {
                addResId = R.drawable.ic_add_to_watchlist;
                removeResId = R.drawable.ic_remove_from_watchlist;
            }
            addClickListener = v -> {
                if (appCMSPresenter.isUserLoggedIn()) {
                    for (String filmId : UpdateImageIconAction.this.filmIds) {
                        contentDatum.getGist().setId(filmId);
                        appCMSPresenter.editWatchlist(metadataMap, contentDatum,
                                addToWatchlistResult -> {
                                    appCMSPresenter.sendAddWatchlistEvent(contentDatum);
                                    UpdateImageIconAction.this.imageButton.setImageResource(
                                            removeResId);
                                    updateDrawableTint(UpdateImageIconAction.this.imageButton.getDrawable());
                                    UpdateImageIconAction.this.imageButton.setOnClickListener(removeClickListener);
                                    if (tintcolor != null)
                                        ((ImageButton) imageButton).setColorFilter(Color.parseColor(tintcolor), android.graphics.PorterDuff.Mode.SRC_IN);
                                    if (eventListener != null) {
                                        eventListener.isAdded(true);
                                    }
                                },
                                true,
                                UpdateImageIconAction.this.filmIds.indexOf(filmId) == UpdateImageIconAction.this.filmIds.size() - 1, null, componentKey);
                    }
                } else {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.ADD_TO_WATCHLIST,
                            () -> {
                                appCMSPresenter.setAfterLoginAction(() -> {
                                    //
                                });
                            }, metadataMap);
                }
            };
            removeClickListener = v -> {
                for (String filmId : UpdateImageIconAction.this.filmIds) {
                    this.contentDatum.getGist().setId(filmId);
                    appCMSPresenter.editWatchlist(metadataMap, this.contentDatum,
                            addToWatchlistResult -> {
                                appCMSPresenter.sendRemoveWatchlistEvent(contentDatum);
                                UpdateImageIconAction.this.imageButton.setImageResource(
                                        addResId);
                                updateDrawableTint(UpdateImageIconAction.this.imageButton.getDrawable());
                                if (eventListener != null) {
                                    eventListener.isAdded(false);
                                }
                                UpdateImageIconAction.this.imageButton.setOnClickListener(addClickListener);
                                if (tintcolor != null)
                                    ((ImageButton) imageButton).setColorFilter(Color.parseColor(tintcolor), android.graphics.PorterDuff.Mode.SRC_IN);
                            },
                            false,
                            UpdateImageIconAction.this.filmIds.indexOf(filmId) == UpdateImageIconAction.this.filmIds.size() - 1, null, componentKey);
                }
            };
        }

        public void updateDrawableTint(Drawable drawable) {
            int fillColor = appCMSPresenter.getGeneralTextColor();
            if (drawable instanceof VectorDrawable) {
                drawable.setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.SRC_IN));
            } else {

                drawable.setTint(fillColor);
                drawable.setTintMode(PorterDuff.Mode.MULTIPLY);
            }

        }

        public void updateWatchlistResponse(boolean filmQueued) {
            if (filmQueued) {
                imageButton.setImageResource(removeResId);
                imageButton.setOnClickListener(removeClickListener);
            } else {
                imageButton.setImageResource(addResId);
                imageButton.setOnClickListener(addClickListener);
            }
            imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            updateDrawableTint(imageButton.getDrawable());
        }

        public void setEvent(IEvent eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void call(final UserVideoStatusResponse userVideoStatusResponse) {
            if (userVideoStatusResponse != null) {
                if (userVideoStatusResponse.isQueued()) {
                    imageButton.setImageResource(removeResId);
                    imageButton.setOnClickListener(removeClickListener);
                } else {
                    imageButton.setImageResource(addResId);
                    imageButton.setOnClickListener(addClickListener);
                }
            } else {
                imageButton.setImageResource(addResId);
                imageButton.setOnClickListener(addClickListener);
            }
        }

        public interface IEvent {
            void isAdded(boolean isAdd);
        }
    }

    public static class UpdateWatchlistTextIconAction implements Action1<UserVideoStatusResponse> {
        private final Button watchListButton;
        private final AppCMSPresenter appCMSPresenter;
        private final List<String> filmIds;
        IEvent eventListener;
        private View.OnClickListener addClickListener;
        private View.OnClickListener removeClickListener;
        private ContentDatum contentDatum;
        private AppCMSUIKeyType componentKey;
        private MetadataMap metadataMap;

        public UpdateWatchlistTextIconAction(Button watchListButton,
                                             AppCMSPresenter presenter,
                                             List<String> filmIds,
                                             ContentDatum contentDatum,
                                             AppCMSUIKeyType componentKey,
                                             MetadataMap metadataMap) {
            this.watchListButton = watchListButton;
            this.appCMSPresenter = presenter;
            this.filmIds = filmIds;
            this.contentDatum = contentDatum;
            this.componentKey = componentKey;
            this.metadataMap = metadataMap;

            addClickListener = v -> {
                if (appCMSPresenter.isUserLoggedIn()) {
                    for (String filmId : UpdateWatchlistTextIconAction.this.filmIds) {
                        contentDatum.getGist().setId(filmId);
                        appCMSPresenter.editWatchlist(metadataMap, contentDatum,
                                addToWatchlistResult -> {
                                    appCMSPresenter.sendAddWatchlistEvent(contentDatum);
                                    UpdateWatchlistTextIconAction.this.watchListButton.setText(
                                            appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                                    UpdateWatchlistTextIconAction.this.watchListButton.setOnClickListener(removeClickListener);
                                    if (eventListener != null) {
                                        eventListener.isAdded(true);
                                    }
                                    UpdateWatchlistTextIconAction.this.watchListButton.invalidate();
                                },
                                true,
                                UpdateWatchlistTextIconAction.this.filmIds.indexOf(filmId) == UpdateWatchlistTextIconAction.this.filmIds.size() - 1, null, componentKey);
                    }
                } else {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                            () -> {
                                appCMSPresenter.setAfterLoginAction(() -> {
                                    //
                                });
                            }, metadataMap);
                }
            };
            removeClickListener = v -> {
                for (String filmId : UpdateWatchlistTextIconAction.this.filmIds) {
                    this.contentDatum.getGist().setId(filmId);
                    appCMSPresenter.editWatchlist(metadataMap, this.contentDatum,
                            addToWatchlistResult -> {
                                appCMSPresenter.sendRemoveWatchlistEvent(contentDatum);
                                UpdateWatchlistTextIconAction.this.watchListButton.setText(
                                        appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                                //metadataMap.getAddToWatchlistCTA());
                                if (eventListener != null) {
                                    eventListener.isAdded(false);
                                }
                                UpdateWatchlistTextIconAction.this.watchListButton.setOnClickListener(addClickListener);
                                UpdateWatchlistTextIconAction.this.watchListButton.invalidate();
                            },
                            false,
                            UpdateWatchlistTextIconAction.this.filmIds.indexOf(filmId) == UpdateWatchlistTextIconAction.this.filmIds.size() - 1, null, componentKey);
                }
            };
        }

        public void updateWatchlistResponse(boolean filmQueued) {
            if (filmQueued) {
                watchListButton.setText(appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                watchListButton.setOnClickListener(removeClickListener);
            } else {
                watchListButton.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                watchListButton.setOnClickListener(addClickListener);
            }
            //watchListButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        public void updatesavelistResponse(boolean filmQueued) {
            if (filmQueued)
                watchListButton.setOnClickListener(removeClickListener);
            else
                watchListButton.setOnClickListener(addClickListener);
        }

        public void setEvent(IEvent eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void call(final UserVideoStatusResponse userVideoStatusResponse) {
            if (userVideoStatusResponse != null) {
                if (userVideoStatusResponse.isQueued()) {
                    watchListButton.setText(appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                    watchListButton.setOnClickListener(removeClickListener);
                } else {
                    watchListButton.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                    watchListButton.setOnClickListener(addClickListener);
                }
            } else {
                watchListButton.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                watchListButton.setOnClickListener(addClickListener);
            }
        }

        public interface IEvent {
            void isAdded(boolean isAdd);
        }
    }

    /**
     * This class has been created to updated the Download Image Action and Status.  This will associate
     * the download status icon with the current download status of the video.  This will also add the click
     * handler that will handle events when the user clicks on the download icon.  This will also handle events
     * corresponding to the entitlement status of the user and the type of the app (AVOD or SVOD).  An
     * AVOD application will ask a user to login before downloading a video if a user is not logged
     * in.  An SVOD application will also ask a user to subscribe first before downloading a video
     * if the user is logged in but not subscribed.
     */
    public static class UpdateDownloadImageIconAction implements Action1<UserVideoDownloadStatus> {
        private final AppCMSPresenter appCMSPresenter;
        private final String userId;
        private final int radiusDifference;
        private final String id;
        private ContentDatum contentDatum;
        private ImageButton imageButton;
        private View.OnClickListener addClickListener;

        public UpdateDownloadImageIconAction(ImageButton imageButton, AppCMSPresenter presenter,
                                             ContentDatum contentDatum, String userId, int radiusDifference,
                                             String id) {
            this.imageButton = imageButton;
            this.appCMSPresenter = presenter;
            this.contentDatum = contentDatum;
            this.userId = userId;
            this.radiusDifference = radiusDifference;
            this.id = id;

            addClickListener = v -> {
                if (!appCMSPresenter.isNetworkConnected()) {
                    if (!appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false,
                                appCMSPresenter::launchBlankPage,
                                null, null);
                        return;
                    }
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                            appCMSPresenter.getLocalisedStrings().getInternetConnectionMsg(),
                            true,
                            () -> appCMSPresenter.navigateToDownloadPage(appCMSPresenter.getDownloadPageId()),
                            null, null);
                    return;
                }

//                if (contentDatum.isDRMEnabled()) {
//                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DRM_NOT_DOWNLOAD,
//                            appCMSPresenter.getLocalisedStrings().getDRMMessage(),
//                            false,
//                            appCMSPresenter::launchBlankPage,
//                            null, null);
//                    return;
//                }
//                contentDatum.getGist().setRentalPeriod(48);
//                contentDatum.getGist().setTransactionEndDate(1534247577000L);

                /**
                 * First check if content is TVOD type and it have pricing info. Then check it purchased info
                 * by calling getData API.IF it has purchased info than play else show rental message.Else for SVOd
                 * Content ,Run the flow same as before.
                 */
                if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && (!contentDatum.getGist().isFree() && contentDatum.getPricing() != null &&
                        contentDatum.getPricing().getType() != null &&
                        ((contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_TVOD))
                                || contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_PPV))) ||
                                (contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                        || contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_PPV)))
                        )) ||
                        (contentDatum.getGist() != null && contentDatum.getGist().getPurchaseType() != null
                                && (contentDatum.getGist().getPurchaseType().equalsIgnoreCase("Rent")
                                || contentDatum.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")))) {


                    contentDatum.setTvodPricing(true);

                    appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), updatedContentDatum -> {

                        boolean isPlayable = true;
                        boolean isPurchased = false;


                        if (updatedContentDatum != null &&
                                updatedContentDatum.size() > 0 &&
                                updatedContentDatum.get(0).size() > 0) {
                            AppCMSTransactionDataValue objTransactionData = updatedContentDatum.get(0).get(contentDatum.getGist().getId());
                            contentDatum.getGist().setRentalPeriod(objTransactionData.getRentalPeriod());
                            String endDate = "";
                            Date date = new Date(contentDatum.getGist().getTransactionEndDate());
                            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                            endDate = df2.format(date);
                            contentDatum.getGist().setTransactionEndDate(endDate);
                        } else {
                            isPlayable = false;
                        }

                        /**
                         * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
                         * content is purchased by user ,if both conditions are false then show subscribe message.
                         */

                        if (contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                || contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {

                            if (appCMSPresenter.isUserSubscribed() || isPurchased) {
                                isPlayable = true;
                            } else {

                                if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                                    appCMSPresenter.showSubscribeMessage();
                                    isPlayable = false;
                                    return;
                                }

                            }

                        }
                        /**
                         * get the transaction end date and compare with current time if end date is less than current date
                         * playable will be false
                         */
                        long expirationDate = contentDatum.getGist().getTransactionEndDate();
                        long remainingTime = CommonUtils.getTimeIntervalForEvent(expirationDate * 1000, "EEE MMM dd HH:mm:ss");


                        /**
                         * if end time is expired then show no purchase dialog message
                         */
                        if (expirationDate > 0 && remainingTime < 0) {
                            isPlayable = false;
                        }
                        if (!isPlayable) {
                            if (appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                                appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(imageButton.getContext().getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getStringValue(appCMSPresenter.getCurrentActivity().getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName()));
                            else
                                appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getCurrentContext().getString(R.string.rental_title), appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));

                        } else {
                            updateForDownload(contentDatum, appCMSPresenter, imageButton, this, addClickListener);

                        }
                    }, "Video");
                }

                /**
                 * Check if event start date is in future date
                 */
                else if (contentDatum != null &&
                        contentDatum.getGist() != null && (contentDatum.getGist().getScheduleStartDate() > 0 || contentDatum.getGist().getScheduleEndDate() > 0)) {

                    /**
                     * if schedule start date is in fiture than show dialog else sstart download
                     */
                    if (appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(), contentDatum.getGist().getScheduleEndDate(), null)) {
                        updateForDownload(contentDatum, appCMSPresenter, imageButton, this, addClickListener);
                    }


                } else {
                    updateForDownload(contentDatum, appCMSPresenter, imageButton, this, addClickListener);

                }


//                if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed())
//                    imageButton.setOnClickListener(null);
            };
//            imageButton.setOnClickListener(addClickListener);

        }

        @Override
        public void call(UserVideoDownloadStatus userVideoDownloadStatus) {
            if (userVideoDownloadStatus != null) {

                switch (userVideoDownloadStatus.getDownloadStatus()) {
                    case STATUS_FAILED:
                        imageButton.setImageResource(android.R.drawable.stat_sys_warning);
                        appCMSPresenter.setDownloadInProgress(false);
                        appCMSPresenter.startNextDownload();
                        break;

                    case STATUS_PAUSED:
                        imageButton.setImageResource(R.drawable.ic_download_queued);
                        // Uncomment to allow for Pause/Resume functionality
//                        imageButton.setOnClickListener(addClickListener);
                        imageButton.setOnClickListener(null);
                        break;

                    case STATUS_PENDING:
                        appCMSPresenter.setDownloadInProgress(false);
                        imageButton.setImageResource(R.drawable.ic_download_queued);
                        appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                                UpdateDownloadImageIconAction.this.imageButton, appCMSPresenter, this, userId, false,
                                radiusDifference,
                                id);
                        imageButton.setOnClickListener(null);
                        break;

                    case STATUS_RUNNING:
                        appCMSPresenter.setDownloadInProgress(true);
//                        imageButton.setImageResource(0);
                        appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                                UpdateDownloadImageIconAction.this.imageButton, appCMSPresenter, this, userId, false,
                                radiusDifference,
                                id);
                        // Uncomment to allow for Pause/Resume functionality
//                        imageButton.setOnClickListener(addClickListener);
                        imageButton.setOnClickListener(null);
                        break;

                    case STATUS_SUCCESSFUL:
                        imageButton.setImageResource(R.drawable.ic_downloaded_big);
                        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageButton.setOnClickListener(null);
                        if (appCMSPresenter.downloadTaskRunning(contentDatum.getGist().getId())) {
                            appCMSPresenter.setDownloadInProgress(false);
                            appCMSPresenter.cancelDownloadIconTimerTask(contentDatum.getGist().getId());
                            contentDatum.getGist().setDownloadStatus(DownloadStatus.STATUS_COMPLETED);
                            appCMSPresenter.notifyDownloadHasCompleted();
                        }
                        break;

                    case STATUS_INTERRUPTED:
                        appCMSPresenter.setDownloadInProgress(false);
                        imageButton.setImageResource(android.R.drawable.stat_sys_warning);
                        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageButton.setOnClickListener(null);
                        break;

                    default:
                        //Log.d(TAG, "No download Status available ");
                        break;
                }
                int fillColor = appCMSPresenter.getGeneralTextColor();
                imageButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));

            } else {
                appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                        UpdateDownloadImageIconAction.this.imageButton, appCMSPresenter, this, userId, false,
                        radiusDifference,
                        id);

                if (appCMSPresenter.isVideoDownloading(contentDatum.getGist().getId())) {
                    imageButton.setImageResource(R.drawable.ic_download_queued);

                } else if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId())) {
                    imageButton.setImageResource(R.drawable.ic_downloaded_big);

                } else {
                    imageButton.setImageResource(R.drawable.ic_download_40x);
                    imageButton.setOnClickListener(addClickListener);
                }
                imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                int fillColor = appCMSPresenter.getGeneralTextColor();
                imageButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.SRC_IN));
                imageButton.requestLayout();

            }
        }

        public void updateDownloadImageButton(ImageButton imageButton) {
            this.imageButton = imageButton;
        }

        public void updateContentData(final ContentDatum data) {
            this.contentDatum = data;
        }

        public View.OnClickListener getAddClickListener() {
            return addClickListener;
        }
    }

    private static class OnRemoveAllInternalEvent implements OnInternalEvent {
        final View removeAllButton;
        private final String moduleId;
        private List<OnInternalEvent> receivers;
        private String internalEventModuleId;

        OnRemoveAllInternalEvent(String moduleId, View removeAllButton) {
            this.moduleId = moduleId;
            this.removeAllButton = removeAllButton;
            receivers = new ArrayList<>();
            internalEventModuleId = moduleId;
        }

        @Override
        public void addReceiver(OnInternalEvent e) {
            receivers.add(e);
        }

        @Override
        public void sendEvent(InternalEvent<?> event) {
            for (OnInternalEvent internalEvent : receivers) {
                internalEvent.receiveEvent(null);
            }
            removeAllButton.setVisibility(View.GONE);
        }

        @Override
        public void receiveEvent(InternalEvent<?> event) {
            if (event != null && event.getEventData() != null
                    && event.getEventData() instanceof Integer) {
                int buttonStatus = (Integer) event.getEventData();
                if (buttonStatus == View.VISIBLE) {
                    removeAllButton.setVisibility(View.VISIBLE);
                } else if (buttonStatus == View.GONE) {
                    removeAllButton.setVisibility(View.GONE);
                }

                removeAllButton.requestLayout();
            }
        }

        @Override
        public void cancel(boolean cancel) {
            //
        }

        @Override
        public String getModuleId() {
            return internalEventModuleId;
        }

        @Override
        public void setModuleId(String moduleId) {
            internalEventModuleId = moduleId;
        }
    }

    /**
     * This class encapsulates the data necessary for a tray item to create all the required UI components.
     * This may be used for tray adapters so that each individual item in a RecyclerView may be created
     * by the ViewCreate.createComponentView() method.
     */
    public static class CollectionGridItemViewCreator {
        final ViewCreator viewCreator;
        final Layout parentLayout;
        final boolean useParentLayout;
        final Component component;
        final AppCMSPresenter appCMSPresenter;
        final Module moduleAPI;
        final AppCMSAndroidModules appCMSAndroidModules;
        Settings settings;
        Map<String, AppCMSUIKeyType> jsonValueKeyMap;
        int defaultWidth;
        int defaultHeight;
        boolean useMarginsAsPercentages;
        boolean gridElement;
        String viewType;
        boolean createMultipleContainersForChildren;
        boolean createRoundedCorners;
        String blockName;

        public CollectionGridItemViewCreator(final ViewCreator viewCreator,
                                             final Layout parentLayout,
                                             final boolean useParentLayout,
                                             final Component component,
                                             final AppCMSPresenter appCMSPresenter,
                                             final Module moduleAPI,
                                             final AppCMSAndroidModules appCMSAndroidModules,
                                             Settings settings,
                                             Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                             int defaultWidth,
                                             int defaultHeight,
                                             boolean useMarginsAsPercentages,
                                             boolean gridElement,
                                             String viewType,
                                             boolean createMultipleContainersForChildren,
                                             boolean createRoundedCorners, String blockName) {
            this.viewCreator = viewCreator;
            this.parentLayout = parentLayout;
            this.useParentLayout = useParentLayout;
            this.component = component;
            this.appCMSPresenter = appCMSPresenter;
            this.blockName = blockName;
            this.moduleAPI = moduleAPI;
            this.appCMSAndroidModules = appCMSAndroidModules;
            this.settings = settings;
            this.jsonValueKeyMap = jsonValueKeyMap;
            this.defaultWidth = defaultWidth;
            this.defaultHeight = defaultHeight;
            this.useMarginsAsPercentages = useMarginsAsPercentages;
            this.gridElement = gridElement;
            this.viewType = viewType;
            this.createMultipleContainersForChildren = createMultipleContainersForChildren;
            this.createRoundedCorners = createRoundedCorners;
        }

        public View createView(Context context) {
            try {
                return viewCreator.createCollectionGridItemView(context,
                        parentLayout,
                        useParentLayout,
                        component,
                        appCMSPresenter,
                        moduleAPI,
                        appCMSAndroidModules,
                        settings,
                        jsonValueKeyMap,
                        defaultWidth,
                        defaultHeight,
                        useMarginsAsPercentages,
                        gridElement,
                        viewType,
                        createMultipleContainersForChildren,
                        createRoundedCorners,
                        null, blockName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class CustomCharacterSpan extends MetricAffectingSpan {
        double ratio = 0.3;

        public CustomCharacterSpan() {
        }

        public CustomCharacterSpan(double ratio) {
            this.ratio = ratio;
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.baselineShift += (int) (paint.ascent() * ratio);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            paint.baselineShift += (int) (paint.ascent() * ratio);
        }

    }

    private class OnFightSelectedListener implements
            AdapterView.OnItemSelectedListener,
            OnInternalEvent {

        Fights fights;
        Component component;
        AppCMSPresenter appCMSPresenter;
        Map<String, AppCMSUIKeyType> jsonValueKeyMap;
        private List<Fights> fightData;
        private List<OnInternalEvent> onInternalEvents;
        private String moduleId;
        private Context context;
        private Module moduleAPI;

        public OnFightSelectedListener(List<Fights> fightData, AppCMSPresenter appCMSPresenter, Context context, Module moduleAPI, Component component, Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
            this.fightData = fightData;
            this.onInternalEvents = new ArrayList<>();
            this.appCMSPresenter = appCMSPresenter;
            this.context = context;
            this.moduleAPI = moduleAPI;
            this.component = component;
            this.jsonValueKeyMap = jsonValueKeyMap;

        }

        @Override
        public void addReceiver(OnInternalEvent e) {

        }

        @Override
        public void sendEvent(InternalEvent<?> event) {
            int internalEventsSize = onInternalEvents != null ? onInternalEvents.size() : 0;
//            for (int i = 0; i < internalEventsSize; i++) {
//                onInternalEvents.get(i).receiveEvent(event);
//            }
        }

        @Override
        public void receiveEvent(InternalEvent<?> event) {

        }

        @Override
        public void cancel(boolean cancel) {

        }

        @Override
        public String getModuleId() {
            return moduleId;
        }

        @Override
        public void setModuleId(String moduleId) {
            this.moduleId = moduleId;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            System.out.println("on item selection");

            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() != 0 && moduleAPI.getContentData().get(0) != null && moduleAPI.getContentData().get(0).getLiveEvents() != null) {

                fights = moduleAPI.getContentData().get(0).getLiveEvents().get(0).getFights().get(position);
            }

            createFightStateRecorsView(context, appCMSPresenter, moduleAPI, component, jsonValueKeyMap, fights);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

    }

    public static void setMoreLinkInDescription(AppCMSPresenter appCMSPresenter, TextView textView, String title, String description, int spanCount, int foreColor) {
        try {
            int descLength = description.length();


            if (descLength > spanCount) {
                String spanText = (description.substring(0, spanCount)) + "... " + appCMSPresenter.getLocalisedStrings().getMoreLabelText();
                spanCount += 4;
                SpannableString spannableString = new SpannableString(spanText);

                spannableString.setSpan(new ForegroundColorSpan(foreColor),
                        0, spanCount, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);
           /* spannableString.setSpan(new ForegroundColorSpan(appCMSPresenter.getGeneralTextColor()),
                    spanCount, spanText.length(), SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);*/
                spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                        spanCount, spanText.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

                //adding click span
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        //what happens whe i click
                        appCMSPresenter.showMoreDialog(title, description);
                    }
                };
                spannableString.setSpan(clickableSpan,
                        spanCount,
                        spanText.length(),
                        SpannableString.SPAN_EXCLUSIVE_INCLUSIVE);


                textView.setText(spannableString, TextView.BufferType.SPANNABLE);
                textView.setClickable(true);
                textView.setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                textView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getTransparentColor(int color, float transparency) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        alpha *= transparency;
        return Color.argb(alpha, red, green, blue);
    }

}

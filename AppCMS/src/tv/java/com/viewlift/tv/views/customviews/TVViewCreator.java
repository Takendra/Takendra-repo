package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ClosedCaptions;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.CreditBlock;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.Language;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.MonetizationModels;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.api.Trailer;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.history.AppCMSRecommendationGenreRecord;
import com.viewlift.models.data.appcms.history.AppCMSRecommendationGenreResult;
import com.viewlift.models.data.appcms.history.Record;
import com.viewlift.models.data.appcms.subscriptions.AppCMSUserSubscriptionPlanResult;
import com.viewlift.models.data.appcms.subscriptions.PlanDetail;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.Resources;
import com.viewlift.models.data.appcms.ui.android.Headers;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.EmailConsent;
import com.viewlift.models.data.appcms.ui.main.EmailConsentMessage;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.LoginSignup;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.models.data.appcms.ui.tv.FireTV;
import com.viewlift.models.data.appcms.weather.Day;
import com.viewlift.models.data.appcms.weather.Hour;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.model.SeeAllCard;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsBaseActivity;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.tv.views.fragment.SwitchSeasonsDialogFragment;
import com.viewlift.tv.views.presenter.AppCmsListRowPresenter;
import com.viewlift.tv.views.presenter.BrowseFragmentLoaderCardPresenter;
import com.viewlift.tv.views.presenter.BrowseFragmentShimmerCardPresenter;
import com.viewlift.tv.views.presenter.CardPresenter;
import com.viewlift.tv.views.presenter.JumbotronPresenter;
import com.viewlift.tv.views.presenter.PlayerPresenter;
import com.viewlift.tv.views.presenter.SeeAllCardPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.utils.IdUtils;
import com.viewlift.views.binders.AppCMSSwitchSeasonBinder;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.StarRating;
import com.viewlift.views.customviews.ViewCreatorMultiLineLayoutListener;
import com.viewlift.views.customviews.ViewCreatorTitleLayoutListener;
import com.viewlift.views.customviews.listdecorator.ItemDecoration;
import com.viewlift.views.listener.PaginationScrollListener;
import com.viewlift.views.listener.TrailerCompletedCallback;
import com.viewlift.views.utilities.ImageLoader;
import com.viewlift.views.utilities.ImageUtils;

import org.jsoup.Jsoup;
import org.xml.sax.XMLReader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;

import rx.functions.Action1;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.viewlift.Utils.loadJsonFromAssets;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ADD_TO_WATCHLIST_WITH_ICON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_DESCRIPTION;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_SHOWDETAIL_08_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_VIDEO_PLAYER_INFO_06_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_VIDEO_PLAYER_INFO_07_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BUNDLEDETAIL_01_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_07_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EMPTY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ENTITLEMENT_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EPISODE_BACKGROUND_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EPISODE_TABLE_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EXPANDED_VIEW_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HISTORY_04_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LINK_YOUR_ACOOUNT_BTN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LIVE_PLAYER_COMPONENT_TABLE_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LIVE_PLAYER_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LIVE_PLAYER_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LOGIN_MODULE_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PARENT_LINEAR_ACTIVATE_DEVICE_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_BUY_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEASON_BACKGROUND_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEASON_TABLE_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEGMENT_BACKGROUND_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEGMENT_TABLE_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEGMENT_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SIGN_UP_MODULE_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_STAND_ALONE_VIDEO_PLAYER02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY3;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY4;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_CLOSE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTEDBY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTORS_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTOR_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_CREDITS_STARRING_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WATCHLIST_03_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.RENT_OPTION_BTN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.WEATHER_TIME_LABEL;
import static com.viewlift.presenters.AppCMSPresenter.DIALOG_FRAGMENT_TAG;
import static com.viewlift.utils.CommonUtils.getColor;

public class TVViewCreator {
    private static final String TAG = "ViewCreator";

    private static LruCache<String, TVPageView> pageViewLruCache;
    private static int PAGE_LRU_CACHE_SIZE = 10;
    private static int DEFAULT_GRID_COLUMN = 3;
    public AppCMSArrayObjectAdaptor mRowsAdapter;
    ComponentViewResult componentViewResult;
    int trayIndex = -1;
    CustomHeaderItem customHeaderItem = null;
    private boolean isLoading = false;
    private boolean isSupportExpandedView = false;
    private ContentTypeChecker contentTypeChecker;
    private int userPersonalizationTrayIndex;
    //    private int userPersonalizationTrayIndex2;
    final private HashMap<String, int[]> moduleIdToRowIndexMap = new HashMap<>();
    final private HashMap<String, HashMap<String,Integer>> moduleIdToGenreToRowIndexMap = new HashMap<>();
//    private HashMap<String, Integer> moduleIdToTrayIndexMap = new HashMap<>();

    private static LruCache<String, TVPageView> getPageViewLruCache() {
        if (pageViewLruCache == null) {
            pageViewLruCache = new LruCache<>(PAGE_LRU_CACHE_SIZE);
        }
        return pageViewLruCache;
    }

    public void removeLruCacheItem(Context context, String pageId) {
        if (getPageViewLruCache().get(pageId + BaseView.isLandscape(context)) != null) {
            getPageViewLruCache().remove(pageId + BaseView.isLandscape(context));
        }
    }

    Resources resources;
    Resources resourcesHindi;
    Resources resourcesEnglish;
    Resources resourcesSpanish;

    public TVPageView generatePage(Context context,
                                   AppCMSPageUI appCMSPageUI,
                                   AppCMSPageAPI appCMSPageAPI,
                                   Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                   AppCMSPresenter appCMSPresenter,
                                   List<String> modulesToIgnore,
                                   boolean isFromLoginPage,
                                   String pageId) {

        resources = appCMSPresenter.getLanguageResourcesFile();

        if (appCMSPageUI == null || appCMSPageAPI == null) {
            return null;
        }

        TVPageView pageView = null;
        if (appCMSPageAPI.getId() != null) {
            pageView = getPageViewLruCache().get(appCMSPageAPI.getId());
        }
        boolean newView = false;
        if (pageView == null || pageView.getContext() != context) {
            pageView = new TVPageView(context, appCMSPageUI);
            getPageViewLruCache().put(appCMSPageAPI.getId(), pageView);
            newView = true;
        }

        if (pageId != null && appCMSPresenter.getMetaPage(pageId) != null) {
            isSupportExpandedView = appCMSPresenter.getMetaPage(pageId).isSupportExpandedView();
        }

        if (true/*newView || !appCMSPresenter.isPagePrimary(appCMSPageAPI.getId())*/) {
            pageView.getChildrenContainer().removeAllViews();
            Runtime.getRuntime().gc();
            componentViewResult = new ComponentViewResult();
            createPageView(context,
                    appCMSPageUI,
                    appCMSPageAPI,
                    pageView,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    modulesToIgnore,
                    isFromLoginPage);
            if (appCMSPageAPI.getId() != null) {
                getPageViewLruCache().put(appCMSPageAPI.getId(), pageView);
            }

        } /*else {
            pageView.
        }*/
        return pageView;
    }

    public ComponentViewResult getComponentViewResult() {
        return componentViewResult;
    }

    public ModuleList moduleList;

    protected void createPageView(Context context,
                                  AppCMSPageUI appCMSPageUI,
                                  AppCMSPageAPI appCMSPageAPI,
                                  TVPageView pageView,
                                  Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                  AppCMSPresenter appCMSPresenter,
                                  List<String> modulesToIgnore,
                                  boolean isFromLoginDialog) {
        appCMSPresenter.clearOnInternalEvents();
        List<ModuleList> modulesList = appCMSPageUI.getModuleList();
        ViewGroup childrenContainer = pageView.getChildrenContainer();
        trayIndex = -1;
        for (int i = 0; i < modulesList.size(); i++) {
            ModuleList moduleInfo = modulesList.get(i);

            if (!modulesToIgnore.contains(moduleInfo.getView())) {
                if (appCMSPresenter.getAppCMSAndroidModules() != null
                        && appCMSPresenter.getAppCMSAndroidModules().getModuleListMap() != null
                        && appCMSPresenter.getAppCMSAndroidModules().getModuleListMap().size() > 0) {
                    Module moduleAPI = matchModuleAPIToModuleUI(moduleInfo, appCMSPageAPI, jsonValueKeyMap);

                    ModuleList module = null;

                if (appCMSPresenter.getAppCMSAndroidModules() != null && appCMSPresenter.getAppCMSAndroidModules().getModuleListMap() != null
                        && appCMSPresenter.getAppCMSAndroidModules().getModuleListMap().size() > 0) {
                    module = appCMSPresenter.getAppCMSAndroidModules().getModuleListMap().get(moduleInfo.getBlockName());
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

                    View childView = createModuleView(context,
                            module,
                            moduleAPI,
                            pageView,
                            jsonValueKeyMap,
                            appCMSPresenter,
                            appCMSPageAPI,
                            isFromLoginDialog);

                    if (childView != null) {
                        childrenContainer.addView(childView);
                        pageView.addModuleViewWithModuleId(module.getId(), (TVModuleView) childView);
                    }
                }
            }

            if (i == modulesList.size() - 1) {
                //now check the Rows Adapter.
                if (mRowsAdapter != null) {
                    FrameLayout browseFrame = new FrameLayout(pageView.getContext());
                    LinearLayout.LayoutParams browseParam = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    browseFrame.setLayoutParams(browseParam);
                    browseFrame.setId(R.id.appcms_browsefragment);
                    childrenContainer.addView(browseFrame);
                }
            }
        }
        List<OnInternalEvent> presenterOnInternalEvents = appCMSPresenter.getOnInternalEvents();
        if (presenterOnInternalEvents != null) {
            for (OnInternalEvent onInternalEvent : presenterOnInternalEvents) {
                for (OnInternalEvent receiverInternalEvent : presenterOnInternalEvents) {
                    onInternalEvent.addReceiver(receiverInternalEvent);
                }
            }
        }
    }

    TVModuleView moduleView = null;
    AppCMSPageAPI globalAppCMSPageApi;

    public View createModuleView(final Context context,
                                 ModuleList module,
                                 final Module moduleAPI,
                                 TVPageView pageView,
                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                 AppCMSPresenter appCMSPresenter, AppCMSPageAPI appCMSPageAPI,
                                 boolean isFromLoginDialog) {
        moduleView = null;
        boolean isCaurosel = false;
        boolean isGrid = false;
        if (!(module.getBlockName() != null && module.getBlockName().contains("authentication"))) {
            appCMSPresenter.setModuleApi(moduleAPI);
        }
        if (module.getBlockName() != null && (module.getBlockName().contains("library01") ||
                module.getBlockName().contains("library02"))) {
            moduleList = module;
            globalAppCMSPageApi = appCMSPageAPI;
        }

        if (module.getView() != null && (/*module.getView().equalsIgnoreCase("AC Grid 01") || */module.getView().equalsIgnoreCase("AC CategoryDetail 02"))) {
            //isGrid = true;
            module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "grid01.json"), ModuleList.class);
        }
        if (module.getBlockName() != null
                && !(module.getBlockName().equalsIgnoreCase("categoryDetail01") || module.getBlockName().equalsIgnoreCase("categoryDetail02"))
                && (Arrays.asList(context.getResources().getStringArray(R.array.app_cms_tray_modules)).contains(module.getType())
                || (module.getBlockName().equalsIgnoreCase("standaloneVideoPlayer02") && !isSupportExpandedView))) {
            if (module.getView().equalsIgnoreCase(context.getResources().getString(R.string.carousel_nodule_four))) {
                isCaurosel = true;
                if (moduleAPI != null && moduleAPI.getContentData() != null
                        && moduleAPI.getContentData().size() > 1) {
                    module.getLayout().getTv().setPadding("130");
                }
            } else if (module.getView().equalsIgnoreCase(context.getResources().getString(R.string.carousel_module_eight))) {
                Settings settings = module.getSettings();
                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "carousel08.json"), ModuleList.class);
                if (settings.getCarousel16x9Setting() != null && settings.getCarousel16x9Setting().getOtt() != null && settings.getCarousel16x9Setting().getOtt().isEnable()) {
                    module = module.getSubModuleList().get(0);
                } else {
                    module = module.getSubModuleList().get(1);
                    if (moduleAPI != null && moduleAPI.getContentData() != null
                            && moduleAPI.getContentData().size() > 1) {
                        module.getLayout().getTv().setPadding("130");
                    }
                }
                module.setSettings(settings);
                isCaurosel = true;

            } else {
                isCaurosel = false;
            }
            if (null != module && module.getView().equalsIgnoreCase("AC CategoryDetail 02")) {
                isGrid = true;
            }

            if (null != module && module.getView().equalsIgnoreCase("AC Grid 01")) {
                isGrid = true;
                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "ac_grid_01.json"), ModuleList.class);
            }
            if (null != module && module.getBlockName().equalsIgnoreCase("userPersonalizatio01")) {
                if (appCMSPresenter.isUserLoggedIn()
                        && appCMSPresenter.isPersonalizationEnabled()
                        && !(moduleAPI != null && moduleAPI.getFilters().getContentType().equalsIgnoreCase("Article"))
                        && !(moduleAPI != null && moduleAPI.getFilters().getContentType().equalsIgnoreCase("Audio"))) {
                    if (appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                        if (appCMSPresenter.isUserSubscribed()) {
                            String type = module.getType();
                            String id = module.getId();
                            Settings settings = module.getSettings();
                            String view = module.getView();
                            module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "userPersonalizatio01.json"), ModuleList.class);
                            if (settings.getThumbnailType().equalsIgnoreCase("landscape")) {
                                module = module.getSubModuleList().get(0);
                            } else if (settings.getThumbnailType().equalsIgnoreCase("portrait")) {
                                module = module.getSubModuleList().get(1);
                            } else if (settings.getThumbnailType().equalsIgnoreCase("_32*9")) {
                                module = module.getSubModuleList().get(2);
                            } else if (settings.getThumbnailType().equalsIgnoreCase("_9*16")) {
                                module = module.getSubModuleList().get(3);
                            } else {
                                module = module.getSubModuleList().get(0);
                            }
                            module.setType(type);
                            module.setId(id);
                            module.setSettings(settings);
                            module.setView(view);
                        } else return null;
                    } else {
                        String type = module.getType();
                        String id = module.getId();
                        Settings settings = module.getSettings();
                        String view = module.getView();
                        module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "userPersonalizatio01.json"), ModuleList.class);
                        if (settings.getThumbnailType().equalsIgnoreCase("landscape")) {
                            module = module.getSubModuleList().get(0);
                        } else if (settings.getThumbnailType().equalsIgnoreCase("portrait")) {
                            module = module.getSubModuleList().get(1);
                        } else if (settings.getThumbnailType().equalsIgnoreCase("_32*9")) {
                            module = module.getSubModuleList().get(2);
                        } else if (settings.getThumbnailType().equalsIgnoreCase("_9*16")) {
                            module = module.getSubModuleList().get(3);
                        } else {
                            module = module.getSubModuleList().get(0);
                        }
                        module.setType(type);
                        module.setId(id);
                        module.setSettings(settings);
                        module.setView(view);
                    }
                } else return null;
            } else if (module.getBlockName().equalsIgnoreCase("userPersonalization01")) {
                if (appCMSPresenter.isUserLoggedIn()
                        && appCMSPresenter.isPersonalizationEnabled()
                        && !(moduleAPI != null && moduleAPI.getFilters().getContentType().equalsIgnoreCase("Article"))
                        && !(moduleAPI != null && moduleAPI.getFilters().getContentType().equalsIgnoreCase("Audio"))) {
                    if (appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                        if (appCMSPresenter.isUserSubscribed()) {
                            String type = module.getType();
                            String id = module.getId();
                            Settings settings = module.getSettings();
                            String view = module.getView();
//                            module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "userPersonalization01.json"), ModuleList.class);
                            if (settings.getThumbnailType().equalsIgnoreCase("landscape")) {
                                module = module.getSubModuleList().get(0);
                            } else if (settings.getThumbnailType().equalsIgnoreCase("portrait")) {
                                module = module.getSubModuleList().get(1);
                            } else if (settings.getThumbnailType().equalsIgnoreCase("_32*9")) {
                                module = module.getSubModuleList().get(2);
                            } else if (settings.getThumbnailType().equalsIgnoreCase("_9*16")) {
                                module = module.getSubModuleList().get(3);
                            } else {
                                module = module.getSubModuleList().get(0);
                            }
                            module.setType(type);
                            module.setId(id);
                            module.setSettings(settings);
                            module.setView(view);
                        } else return null;
                    } else {
                        String type = module.getType();
                        String id = module.getId();
                        Settings settings = module.getSettings();
                        String view = module.getView();
//                        module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "userPersonalization01.json"), ModuleList.class);
                        if (settings.getThumbnailType().equalsIgnoreCase("landscape")) {
                            module = module.getSubModuleList().get(0);
                        } else if (settings.getThumbnailType().equalsIgnoreCase("portrait")) {
                            module = module.getSubModuleList().get(1);
                        } else if (settings.getThumbnailType().equalsIgnoreCase("_32*9")) {
                            module = module.getSubModuleList().get(2);
                        } else if (settings.getThumbnailType().equalsIgnoreCase("_9*16")) {
                            module = module.getSubModuleList().get(3);
                        } else {
                            module = module.getSubModuleList().get(0);
                        }
                        module.setType(type);
                        module.setId(id);
                        module.setSettings(settings);
                        module.setView(view);
                    }
                } else return null;
            }

            if (module.getBlockName().equals("recommendation01")) {
                if (!appCMSPresenter.isRecommendationEnabled()) {
                    return null;
                }
                if (appCMSPresenter.isRecommendationOnlyForSubscribedEnabled() && !appCMSPresenter.isUserSubscribed()) {
                    return null;
                }
                String type = module.getType();
                String id = module.getId();
                Settings settings = module.getSettings();
                String view = module.getView();
//                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "recommendation01.json"), ModuleList.class);
                if (settings.getThumbnailType().equalsIgnoreCase("landscape")) {
                    module = module.getSubModuleList().get(0);
                } else if (settings.getThumbnailType().equalsIgnoreCase("portrait")) {
                    module = module.getSubModuleList().get(1);
                } else if (settings.getThumbnailType().equalsIgnoreCase("_32*9")) {
                    module = module.getSubModuleList().get(2);
                } else if (settings.getThumbnailType().equalsIgnoreCase("_9*16")) {
                    module = module.getSubModuleList().get(3);
                } else {
                    module = module.getSubModuleList().get(0);
                }
                module.setType(type);
                module.setId(id);
                module.setSettings(settings);
                module.setView(view);
            }


            if (null != module.getComponents() && module.getComponents().size() > 0) {
                for (Component component : module.getComponents()) {
                    if (moduleAPI != null
                            && ((moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0)
                            || (module.getBlockName().equals("weatherTray01") && appCMSPageAPI.getWeatherWidgetData() != null)
                            || module.getBlockName().equals("recommendation01")
                            || module.getBlockName().equals("userPersonalizatio01")
                            || module.getBlockName().contains("continueWatching")
                            || module.getBlockName().contains("newsRecommendation01")
                            || module.getBlockName().equals("userPersonalization01")))
                        if (appCMSPresenter.isPromoValidationRequired() && moduleAPI.getContentData().size() == 1) {
                            if (!(moduleAPI.getContentData().get(0).getTags() != null &&
                                    moduleAPI.getContentData().get(0).getTags().size() > 0 &&
                                    moduleAPI.getContentData().get(0).getTags().get(0).getTitle() != null &&
                                    moduleAPI.getContentData().get(0).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                                createTrayModule(context, component, module.getLayout(), module, moduleAPI,
                                        pageView, jsonValueKeyMap, appCMSPresenter, appCMSPageAPI, isCaurosel, isGrid);
                            }
                        } else {
                            createTrayModule(context, component, module.getLayout(), module, moduleAPI,
                                    pageView, jsonValueKeyMap, appCMSPresenter, appCMSPageAPI, isCaurosel, isGrid);
                        }
                }
            }
            return null;
        } else if (context.getResources().getString(R.string.app_cms_page_show_detail_module_key).equalsIgnoreCase(module.getView()) ||
                context.getResources().getString(R.string.app_cms_page_show_detail_module_key8).equalsIgnoreCase(module.getView()) ||
                context.getResources().getString(R.string.app_cms_page_entitlement_module_key).equalsIgnoreCase(module.getView())) {
            if (context.getResources().getString(R.string.app_cms_page_show_detail_module_key8).equalsIgnoreCase(module.getView())) {
//                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "showdetail08.json"), ModuleList.class);
            } else if (context.getResources().getString(R.string.app_cms_page_entitlement_module_key).equalsIgnoreCase(module.getView())) {
                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "entitlement01.json"), ModuleList.class);
            } else {
                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "showdetail.json"), ModuleList.class);
            }
            //Reversing the Order of Seasons List
            if (module.getBlockName() != null
                    && !module.getBlockName().equalsIgnoreCase("entitlement 01")
                    && !moduleAPI.hasSeasonListReversed()
                    && moduleAPI.getContentData() != null
                    && moduleAPI.getContentData().size() > 0
                    && moduleAPI.getContentData().get(0).getSeason() != null
                    && moduleAPI.getContentData().get(0).getSeason().size() > 0) {
                moduleAPI.setHasSeasonListReversed(true);
                Collections.reverse(moduleAPI.getContentData().get(0).getSeason());
            }
            moduleView = new ShowDetailModuleView(
                    context,
                    module,
                    moduleAPI,
                    appCMSPageAPI,
                    this,
                    appCMSPresenter,
                    jsonValueKeyMap);
            if (module.getSettings().isShowBgImage()) {
                final TVPageView finalPageView = pageView;
                if (null != moduleAPI.getContentData()
                        && null != moduleAPI.getContentData().get(0)
                        && null != moduleAPI.getContentData().get(0).getGist()
                        && null != moduleAPI.getContentData().get(0).getGist().getVideoImageUrl()) {
                    try {
                        Glide.with(context).asBitmap().load(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())
                                .into(new SimpleTarget<Bitmap>(TVBaseView.DEVICE_WIDTH,
                                        TVBaseView.DEVICE_HEIGHT) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                                        finalPageView.setBackground(drawable);
                                        finalPageView.getChildrenContainer().setBackgroundColor(Color.parseColor("#DD000000"));
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (context.getResources().getString(R.string.app_cms_page_bundle_detail_01_module_key).equalsIgnoreCase(module.getView())) {
//            module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "bundelDetail01.json"), ModuleList.class);
            moduleView = new ShowDetailModuleView(
                    context,
                    module,
                    moduleAPI,
                    appCMSPageAPI,
                    this,
                    appCMSPresenter,
                    jsonValueKeyMap);
        } else if ("AC TickerFeed 01".equalsIgnoreCase(module.getView())) {
            moduleView = new TickerView(
                    context,
                    module,
                    moduleAPI,
                    appCMSPageAPI,
                    this,
                    appCMSPresenter,
                    jsonValueKeyMap);
        } else if (context.getResources().getString(R.string.app_cms_page_module_key_showdetail_06).equalsIgnoreCase(module.getView())) {
            if (moduleAPI != null && moduleAPI.getContentData() != null) {
                if (moduleAPI.getContentData().get(0).getSeason().size() == 0)
                    return null;
                if (moduleAPI.getContentData().get(0).getSeason().size() == 1 &&
                        moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() == 0) {
                    return null;
                }
                moduleView = new TVSeasonModule(context,
                        module,
                        moduleAPI,
                        jsonValueKeyMap,
                        appCMSPresenter,
                        this,
                        appCMSPresenter.getAppCMSAndroidModules(), pageView);
            }

        } else if (context.getResources().getString(R.string.carousel_nodule_seven).equalsIgnoreCase(module.getView())) {
            if (moduleAPI.getContentData().get(0).getSeason().size() == 0)
                return null;
            if (moduleAPI.getContentData().get(0).getSeason().size() == 1 &&
                    moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() == 0) {
                return null;
            }
            moduleView = new TVShowModule(context,
                    module,
                    moduleAPI,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    this,
                    appCMSPresenter.getAppCMSAndroidModules(), pageView);

        } else if (Arrays.asList(context.getResources().getStringArray(R.array.app_cms_modules)).contains(module.getType())) {
            moduleView = new TVModuleView<>(context, module);
            ViewGroup childrenContainer = moduleView.getChildrenContainer();
            if (context.getResources().getString(R.string.appcms_detail_module).equalsIgnoreCase(module.getView()) ||
                    "AC VideoPlayerWithInfo 02".equalsIgnoreCase(module.getView()) ||
                    context.getResources().getString(R.string.appcms_detail_module_six).equalsIgnoreCase(module.getView())) {
                if (null == moduleAPI
                        || moduleAPI.getContentData() == null) {
                    TextView textView = new TextView(context);
                    textView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.no_data_available)));
                    textView.setGravity(Gravity.CENTER);
                    Component component = new Component();
                    component.setFontFamily(appCMSPresenter.getFontFamily());
                    component.setFontWeight(context.getString(R.string.app_cms_page_font_semibold_key));
                    textView.setTypeface(Utils.getTypeFace(
                            context,
                            appCMSPresenter,
                            component));
                    textView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                    childrenContainer.addView(textView);
                    return moduleView;
                }

                if (context.getResources().getString(R.string.appcms_detail_module).equalsIgnoreCase(module.getView())) {
                    final TVPageView finalPageView = pageView;
                    if (null != moduleAPI.getContentData()
                            && null != moduleAPI.getContentData().get(0)
                            && null != moduleAPI.getContentData().get(0).getGist()
                            && null != moduleAPI.getContentData().get(0).getGist().getVideoImageUrl()) {
                        try {
                            Glide.with(context).asBitmap().load(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())
                                    .into(new SimpleTarget<Bitmap>(TVBaseView.DEVICE_WIDTH,
                                            TVBaseView.DEVICE_HEIGHT) {
                                        @Override
                                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                            Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                                            finalPageView.setBackground(drawable);
                                            String appBackgroundColor = appCMSPresenter.getAppBackgroundColor();
                                            finalPageView.getChildrenContainer().setBackgroundColor(Color.parseColor("#DD" + appBackgroundColor.replace("#", "")));
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (null != module && module.getView().equalsIgnoreCase(context.getString(R.string.app_cms_contact_us_module))) {
                if (null != appCMSPresenter
                        && null != appCMSPresenter.getCurrentActivity()
                        && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                    ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(true);
                }
            }


            if (appCMSPresenter.isMovieSpreeApp() && module.getBlockName().contains("authentication02")) {
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication_age_tv.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(0);
            } else if (module.getBlockName() != null && module.getBlockName().contains("expanded01")) {
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "rta_expanded.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(0);
            } else if (module.getBlockName().contains("customNavigation")) {
                ModuleList module1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "custom_navigation.json"),
                        ModuleList.class);
                module1.setSettings(module.getSettings());
                module = module1;
            }


            if (module.getBlockName().equalsIgnoreCase("watchlist01")) {
//                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "watchlist01.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("watchlist03")) {
//                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "rta_watchlist.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("userManagement04")) {
//                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "user_mgt_motv.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("videoPlayerInfo06") ||
                    module.getBlockName().equalsIgnoreCase("videoPlayerInfo07")
                    || module.getBlockName().equalsIgnoreCase("videoPlayerInfo05")
            ) {
                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "videoPlayerInfo01.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("videoPlayerInfo01")) {
                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "tv_videoDetail.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("selectPlan06")) {
//                module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "tvSelectPlan02.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("authentication17")) {
                ModuleList loginModule = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication17.json"),
                        ModuleList.class);
                loginModule.setSettings(module.getSettings());
                module = loginModule;
                //module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "authentication17.json"), ModuleList.class);
            } else if (module.getBlockName().equalsIgnoreCase("authentication01_activate_device")) {
                ModuleList loginModule = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication.json"),
                        ModuleList.class);
                loginModule.setSettings(module.getSettings());
                module = loginModule;
                //module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "authentication17.json"), ModuleList.class);
            }
            if (module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    createComponentView(context,
                            component,
                            module.getLayout(),
                            moduleAPI,
                            pageView,
                            module.getSettings(),
                            jsonValueKeyMap,
                            appCMSPresenter,
                            false,
                            module.getView(),
                            isFromLoginDialog);
                    if (componentViewResult.onInternalEvent != null) {
                        appCMSPresenter.addInternalEvent(componentViewResult.onInternalEvent);
                    }

                    View componentView = componentViewResult.componentView;
                    if (componentView != null && moduleView != null) {
                        childrenContainer.addView(componentView);
                        moduleView.addChildComponentAndView(componentView, component);
                        try {
                            moduleView.setComponentHasView(i, true);
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage().toString());
                        }
                        moduleView.setViewMarginsFromComponent(component,
                                componentView,
                                moduleView.getLayout(),
                                childrenContainer,
                                jsonValueKeyMap,
                                componentViewResult.useMarginsAsPercentagesOverride,
                                componentViewResult.useWidthOfScreen,
                                module.getView());

                    } else if (moduleView != null) {
                        moduleView.setComponentHasView(i, false);
                    }
                }
            }
        }
        return moduleView;
    }


    public void createTrayModule(final Context context,
                                 final Component component,
                                 final Layout parentLayout,
                                 final ModuleList moduleUI,
                                 final Module moduleData,
                                 @Nullable TVPageView pageView,
                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                 final AppCMSPresenter appCMSPresenter,
                                 AppCMSPageAPI appCMSPageAPI,
                                 boolean isCarousel,
                                 boolean isGrid) {

        if (null == mRowsAdapter) {
            AppCmsListRowPresenter appCmsListRowPresenter;
            //TODO:- Comment below condition for player. Need to look some solution in some other way.
            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS ||
                    appCMSPresenter.isNewsTemplate()) {
                appCmsListRowPresenter = new AppCmsListRowPresenter(context, appCMSPresenter, FocusHighlight.ZOOM_FACTOR_NONE, component);
            } else {
                appCmsListRowPresenter = new AppCmsListRowPresenter(context, appCMSPresenter);
            }

            mRowsAdapter = new AppCMSArrayObjectAdaptor(appCmsListRowPresenter);
        }

        // Sort the data in case of continue watching tray
        if (jsonValueKeyMap.get(moduleUI.getType()) == PAGE_CONTINUE_WATCHING_MODULE_KEY) {
            if (moduleData != null && moduleData.getContentData() != null) {
                try {
                    Collections.sort(moduleData.getContentData(), (o1, o2) -> {
                        if (o1.getUpdateDate() == null)
                            return (o2.getUpdateDate() == null) ? 0 : -1;
                        if (o2.getUpdateDate() == null)
                            return 1;
                        return Long.compare((long) o1.getUpdateDate(), (long) o2.getUpdateDate());
                    });
                    Collections.reverse(moduleData.getContentData());
                } catch (Exception e) {

                }
            }
        }
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());
        if (componentType == null) {
            componentType = PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
        if (componentKey == null) {
            componentKey = PAGE_EMPTY_KEY;
        }
        switch (componentType) {
            case PAGE_LABEL_KEY:
                switch (componentKey) {
                    case PAGE_TRAY_TITLE_KEY:
                        if (moduleData != null) {
                            customHeaderItem = null;
                            if (moduleUI.getBlockName().equalsIgnoreCase("weatherTray01")) {
                                Log.d(TAG, "inside weather Trays blockname is3 === " + moduleUI.getBlockName());
                                if (moduleData.getWeatherInterval().equalsIgnoreCase("hourly")) {
                                    createHeaderItem(component, context, moduleUI, moduleData, "Hourly ForeCast".toUpperCase(), isCarousel);
                                } else {
                                    createHeaderItem(component, context, moduleUI, moduleData, "7-Day ForeCast".toUpperCase(), isCarousel);
                                }
                            } else {
                                createHeaderItem(component, context, moduleUI, moduleData, moduleData.getTitle() != null ? moduleData.getTitle() : "", isCarousel);
                            }
                        }
                        break;
                    case PAGE_CATEGORY_TRAY_TITLE_KEY:
                        if (moduleData != null) {
                            customHeaderItem = null;
                            String title;
                            if (!TextUtils.isEmpty(moduleData.getTitle())) {
                                title = moduleData.getTitle();
                            } else if (appCMSPresenter.getSeeAllModule() != null
                                    && !TextUtils.isEmpty(appCMSPresenter.getSeeAllModule().getTitle())) {
                                title = appCMSPresenter.getSeeAllModule().getTitle();
                            } else {
                                title = "This Category";
                            }
                            String string = appCMSPresenter.getLanguageResourcesFile().getStringValue(context.getString(R.string.items_in_category, moduleData.getContentData().size(), title));
                            createHeaderItem(component, context, moduleUI, moduleData, !TextUtils.isEmpty(string) ? string : "", isCarousel);
                        }
                        break;
                }
                break;
            case PAGE_CAROUSEL_VIEW_KEY: {
                createHeaderItem(component, context, moduleUI, moduleData, "", true);
            }

            if (moduleData != null) {
                CardPresenter cardPresenter = new JumbotronPresenter(
                        context,
                        appCMSPresenter,
                        component,
                        jsonValueKeyMap,
                        moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover(),
                        moduleUI.getSettings() != null && moduleUI.getSettings().isNoInfo(),
                        moduleData);
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                    List<ContentDatum> contentData1 = moduleData.getContentData();
                    List<Component> components = component.getComponents();
                    for (int i = 0; i < contentData1.size(); i++) {
                        ContentDatum contentData = contentData1.get(i);
                        if (contentData != null && contentData.getGist() != null
                                && contentData.getGist().getContentType() != null //video, series/show and episodic
                                && (contentData.getGist().getContentType().equalsIgnoreCase("video")
                                || contentData.getGist().getContentType().equalsIgnoreCase("series")
                                || contentData.getGist().getContentType().equalsIgnoreCase("show")
                                || contentData.getGist().getContentType().equalsIgnoreCase("episodic")
                                || contentData.getGist().getContentType().equalsIgnoreCase("bundle"))
                        ) {
                            BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                            rowData.contentData = contentData;
                            rowData.uiComponentList = components;
                            rowData.action = component.getTrayClickAction();
                            rowData.blockName = moduleUI.getBlockName();
                            rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                            rowData.rowNumber = trayIndex;
                            rowData.itemPosition = i;
                            rowData.moduleApi = moduleData;
                            listRowAdapter.add(rowData);
                        }
                    }
                    if (listRowAdapter.size() > 0)
                        mRowsAdapter.add(new ListRow(customHeaderItem, listRowAdapter));
                }
            }
            break;
            case PAGE_COLLECTIONGRID_KEY:
                /*for(Component component1 : component.getComponents()){*/

                if (moduleUI.getBlockName().equalsIgnoreCase("library01") || moduleUI.getBlockName().equalsIgnoreCase("library02")) {
                    createHeaderItem(moduleUI.getComponents().get(0), context, moduleUI, moduleData, "Library".toUpperCase(), isCarousel);
                }

                if (customHeaderItem == null) {
                    createHeaderItem(component, context, moduleUI, moduleData, moduleData != null ? moduleData.getTitle() : "", false);
                }

                if (null != moduleData) {
                    CardPresenter trayCardPresenter = new CardPresenter(context, appCMSPresenter,
                            Integer.valueOf(component.getLayout().getTv().getHeight() != null ? component.getLayout().getTv().getHeight() : "0"),
                            Integer.valueOf(component.getLayout().getTv().getWidth() != null ? component.getLayout().getTv().getWidth() : "0"),
                            moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover(),
                            moduleUI.getSettings() != null && moduleUI.getSettings().isNoInfo(),
                            component,
                            jsonValueKeyMap,
                            moduleData,
                            isSupportExpandedView
                    );

                    if (isGrid) {
                        ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
                        classPresenterSelector.addClassPresenter(BrowseFragmentRowData.class, trayCardPresenter);
                        classPresenterSelector.addClassPresenter(SeeAllCard.class, new SeeAllCardPresenter(context, moduleUI, appCMSPresenter));
                        ArrayObjectAdapter traylistRowAdapter = new ArrayObjectAdapter(classPresenterSelector);
                        if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                            List<ContentDatum> contentData1 = moduleData.getContentData();
                            List<Component> components = component.getComponents();

                            int noOfGridItem = DEFAULT_GRID_COLUMN;
                            if (null != moduleUI.getSettings()
                                    && null != moduleUI.getSettings().getColumns()
                                    && moduleUI.getSettings().getColumns().getOtt() > 0) {
                                noOfGridItem = moduleUI.getSettings().getColumns().getOtt();
                            }
                            boolean seeAllCardRequired = moduleData.getSettings() != null
                                    && moduleData.getSettings().isSeeAll();

                            for (int i = 0; i < contentData1.size(); i++) {
                                ContentDatum contentData = contentData1.get(i);
                                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                rowData.contentData = contentData;
                                rowData.uiComponentList = components;
                                rowData.action = component.getTrayClickAction();
                                rowData.blockName = moduleUI.getBlockName();
                                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                rowData.rowNumber = trayIndex;
                                rowData.trayCardPresenter = trayCardPresenter;
                                rowData.customHeaderItem = customHeaderItem;
                                rowData.moduleApi = moduleData;
                                traylistRowAdapter.add(rowData);

                                System.out.println("Anas: RowNumern: " + (traylistRowAdapter.size() - 1) % noOfGridItem);
                                rowData.itemPosition = (traylistRowAdapter.size() - 1) % noOfGridItem;
                                if ((traylistRowAdapter.size() % noOfGridItem == 0)
                                        || i == contentData1.size() - 1 /*Reached the last item*/) {
                                    mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                                    customHeaderItem = null;
                                    createHeaderItem(component, context, moduleUI, moduleData, "", false);
                                    traylistRowAdapter = null;
                                    traylistRowAdapter = new ArrayObjectAdapter(classPresenterSelector);
                                }
                            }
                            if (seeAllCardRequired) {
                                if (((ListRow) mRowsAdapter.get(mRowsAdapter.size() - 1)).getAdapter().size() % noOfGridItem == 0) {
                                    traylistRowAdapter = new ArrayObjectAdapter(classPresenterSelector);
                                    traylistRowAdapter.add(new SeeAllCard(moduleData));
                                    mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                                } else {
                                    ((ArrayObjectAdapter) ((ListRow) mRowsAdapter.get(mRowsAdapter.size() - 1)).getAdapter()).add(new SeeAllCard(moduleData));
                                }
                            }
                        }
                    } else if (moduleData.getModuleType() != null
                            && moduleData.getModuleType().equalsIgnoreCase("ShowDetailModule")
                            && moduleData.getContentData() != null
                            && moduleData.getContentData().get(0) != null) {
                        Log.d(TAG, "It's a series");
                        List<Component> components = component.getComponents();
                        ArrayObjectAdapter traylistRowAdapter = null;
                        List<Season_> seasonList = moduleData.getContentData().get(0).getSeason();
                        if (seasonList != null) {
                            for (int seasonIndex = 0; seasonIndex < seasonList.size(); seasonIndex++) {
                                Season_ season = seasonList.get(seasonIndex);
                                traylistRowAdapter = new ArrayObjectAdapter(trayCardPresenter);
                                List<ContentDatum> episodes = season.getEpisodes();

                                customHeaderItem = null;
                                createHeaderItemForSeason(component, context,
                                        season.getTitle()/*appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.season)) + " " + (seasonIndex + 1)*/);
                                int index = trayIndex;
                                index = index - 1;
                                for (int i = 0; i < episodes.size(); i++) {
                                    List<String> relatedVids = Utils.getRelatedVideosInShow(
                                            moduleData.getContentData().get(0).getSeason(),
                                            seasonIndex,
                                            i - 1);
                                    ContentDatum contentDatum = episodes.get(i);
                                    contentDatum.setSeason(moduleData.getContentData().get(0).getSeason());
                                    BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                    rowData.contentData = contentDatum;
                                    rowData.relatedVideoIds = relatedVids;
                                    rowData.uiComponentList = components;
                                    rowData.action = component.getTrayClickAction();
                                    rowData.blockName = moduleUI.getBlockName();
                                    rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                    rowData.rowNumber = index;
                                    rowData.moduleApi = moduleData;
                                    traylistRowAdapter.add(rowData);
                                }
                                mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                            }
                        }
                    } else if (moduleData.getModuleType() != null
                            && moduleData.getModuleType().equalsIgnoreCase("BundleDetailModule")
                            && moduleData.getContentData() != null
                            && moduleData.getContentData().get(0) != null) {
                        Log.d(TAG, "It's a bundle");
                        List<Component> bundleComponents = component.getComponents();
                        ArrayObjectAdapter bundleTraylistRowAdapter = null;
                        bundleTraylistRowAdapter = new ArrayObjectAdapter(trayCardPresenter);
                        customHeaderItem = null;
                        createHeaderItemForSeason(component, context, "INSIDE THIS BUNDLE");
                        for (int i = 0; i < moduleData.getContentData().get(0).getGist().getBundleList().size(); i++) {
                            ContentDatum contentDatum = moduleData.getContentData().get(0).getGist().getBundleList().get(i);
//                                if (contentDatum.getGist() != null) {
                            BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                            rowData.contentData = contentDatum;
//                                rowData.relatedVideoIds = relatedVids;
                            rowData.uiComponentList = bundleComponents;
                            rowData.action = component.getTrayClickAction();
                            rowData.blockName = moduleUI.getBlockName();
                            rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                            rowData.rowNumber = 0;
                            rowData.moduleApi= moduleData;
                            bundleTraylistRowAdapter.add(rowData);
                        }
//                            }
                        mRowsAdapter.add(new ListRow(customHeaderItem, bundleTraylistRowAdapter));
//                        }
                    } else {

                        ClassPresenterSelector classPresenterSelector = new ClassPresenterSelector();
                        classPresenterSelector.addClassPresenter(BrowseFragmentRowData.class, trayCardPresenter);
                        classPresenterSelector.addClassPresenter(SeeAllCard.class, new SeeAllCardPresenter(context, moduleUI, appCMSPresenter));
                        classPresenterSelector.addClassPresenter(BrowseFragmentLoaderCard.class, new BrowseFragmentLoaderCardPresenter(moduleUI, appCMSPresenter));
                        classPresenterSelector.addClassPresenter(BrowseFragmentShimmerCard.class, new BrowseFragmentShimmerCardPresenter(moduleUI, appCMSPresenter));
                        ArrayObjectAdapter traylistRowAdapter = new ArrayObjectAdapter(classPresenterSelector);

                        if ((moduleUI.getBlockName().equalsIgnoreCase("library01") || moduleUI.getBlockName().equalsIgnoreCase("library02")) && moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                            List<ContentDatum> contentData1 = moduleData.getContentData();
                            List<Component> components = component.getComponents();
                            int loopCount = contentData1.size()/*Math.min(contentData1.size(), seeAllItemCountThreshold)*/;
                            // boolean seeAllCardRequired = moduleData.getSettings().isSeeAll() && contentData1.size() > seeAllItemCountThreshold;
                            boolean seeAllCardRequired = moduleData.getSettings() != null
                                    && moduleData.getSettings().isSeeAll()
                                    && moduleData.getSettings().getSeeAllPermalink() != null
                                    /*&& contentData1.size() >= seeAllItemCountThreshold*/;
                            for (int i = 0; i < (seeAllCardRequired ? loopCount : contentData1.size()); i++) {
                                ContentDatum contentData = contentData1.get(i);
                                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                rowData.contentData = contentData;
                                rowData.uiComponentList = components;
                                rowData.action = component.getTrayClickAction();
                                rowData.blockName = moduleUI.getBlockName();
                                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                rowData.rowNumber = trayIndex;
                                rowData.itemPosition = i;
                                rowData.moduleApi = moduleData;
                                rowData.contentDatumList = contentData1;
                                traylistRowAdapter.add(rowData);
                            }
                            if (seeAllCardRequired) {
                                traylistRowAdapter.add(new SeeAllCard(moduleData));
                            }
                            mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                        } else if (moduleUI.getBlockName().equalsIgnoreCase("weatherTray01")
                                && appCMSPageAPI.getWeatherWidgetData() != null) {
                            List<Component> components = component.getComponents();
                            if (moduleData.getWeatherInterval() != null && moduleData.getWeatherInterval().equalsIgnoreCase("hourly")) {
                                List<Hour> hourList = appCMSPageAPI.getWeatherWidgetData().getCity().get(0).getHourlyForecast().getHour();
                                for (int i = 0; i < hourList.size(); i++) {
                                    Hour contentData = hourList.get(i);
                                    BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                    rowData.contentData = null;
                                    rowData.weatherHour = contentData;
                                    rowData.uiComponentList = components;
                                    rowData.action = component.getTrayClickAction();
                                    rowData.blockName = moduleUI.getBlockName();
                                    rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                    rowData.rowNumber = trayIndex;
                                    rowData.itemPosition = i;
                                    rowData.moduleApi = moduleData;
                                    rowData.weatherInterval = moduleData.getWeatherInterval();
                                    traylistRowAdapter.add(rowData);
                                }
                                mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                            } else if (moduleData.getWeatherInterval() != null && moduleData.getWeatherInterval().equalsIgnoreCase("weekly")) {
                                List<Day> dayList = appCMSPageAPI.getWeatherWidgetData().getCity().get(0).getDailyForecast().getDay();
                                int dataOptions = dayList.size() > 7 ? 7 : dayList.size();
                                for (int i = 0; i < dataOptions; i++) {
                                    Day contentData = dayList.get(i);
                                    BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                    rowData.contentData = null;
                                    rowData.weatherHour = contentData;
                                    rowData.uiComponentList = components;
                                    rowData.action = component.getTrayClickAction();
                                    rowData.blockName = moduleUI.getBlockName();
                                    rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                    rowData.rowNumber = trayIndex;
                                    rowData.itemPosition = i;
                                    rowData.moduleApi = moduleData;
                                    rowData.weatherInterval = moduleData.getWeatherInterval();
                                    traylistRowAdapter.add(rowData);
                                }
                                mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                            }
                        }  else {
                            final boolean recommendationAutoPlayEnabled = appCMSPresenter.getAppCMSMain().getRecommendation() != null
                                    && appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendationAutoPlay();
                            if (moduleData.getContentData() != null && (moduleUI.getBlockName().equalsIgnoreCase("recommendation01")
                                    || moduleUI.getBlockName().equalsIgnoreCase("newsRecommendation01"))) {
                                createHeaderItem(getDummyComponent(moduleUI.getLayout().getTv().getFontSize()), context, moduleUI, moduleData, moduleData.getTitle(), isCarousel);

                                if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                                    int contentDatumLength = moduleData.getContentData().size();
                                    ArrayList<ContentDatum> recommendationContents = new ArrayList<>();

                                    List<String> autoPlayVideoIdList = new ArrayList<>();

                                    for (int i = 0; i < contentDatumLength; i++) {
                                        ContentDatum recordContentDatum = moduleData.getContentData().get(i);
                                        recommendationContents.add(recordContentDatum);
                                        if (recommendationAutoPlayEnabled) {
                                            autoPlayVideoIdList.add(recordContentDatum.getGist().getId());
                                        }
                                    }

                                    if (!recommendationContents.isEmpty()) {
                                        List<Component> components = component.getComponents();
                                        int loopCount = ((List<ContentDatum>) recommendationContents).size()/*Math.min(contentData1.size(), seeAllItemCountThreshold)*/;
                                        // boolean seeAllCardRequired = moduleData.getSettings().isSeeAll() && contentData1.size() > seeAllItemCountThreshold;
                                        boolean seeAllCardRequired = moduleData.getSettings() != null
                                                && moduleData.getSettings().isSeeAll()
                                                /*&& contentData1.size() >= seeAllItemCountThreshold*/;
                                        for (int i = 0; i < (seeAllCardRequired ? loopCount : ((List<ContentDatum>) recommendationContents).size()); i++) {
                                            if (!(((List<ContentDatum>) recommendationContents).get(i).getTags() != null &&
                                                    ((List<ContentDatum>) recommendationContents).get(i).getTags().size() > 0 &&
                                                    ((List<ContentDatum>) recommendationContents).get(i).getTags().get(0).getTitle() != null &&
                                                    ((List<ContentDatum>) recommendationContents).get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                                                ContentDatum contentData = ((List<ContentDatum>) recommendationContents).get(i);
                                                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                                rowData.moduleTitle = moduleData.getTitle();
                                                rowData.contentData = contentData;
                                                rowData.uiComponentList = components;
                                                rowData.action = component.getTrayClickAction();
                                                rowData.blockName = moduleUI.getBlockName();
                                                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                                rowData.rowNumber = trayIndex;
                                                rowData.itemPosition = i;
                                                rowData.moduleApi = moduleData;
                                                rowData.contentDatumList = recommendationContents;
                                                rowData.relatedVideoIds = recommendationAutoPlayEnabled
                                                        ? new ArrayList<>(autoPlayVideoIdList.subList(i, autoPlayVideoIdList.size()))
                                                        : null;
                                                traylistRowAdapter.add(rowData);
                                            }
                                        }
                                        if (seeAllCardRequired) {
                                            traylistRowAdapter.add(new SeeAllCard(moduleData));
                                        }
                                        moduleData.setContentData(recommendationContents);
                                        if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                                if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                                                    mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                                                }
                                            }
                                        } else {
                                            if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                                                mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                                            }
                                        }
                                    } else {
                                        // Check it again
                                        mRowsAdapter.removeItems(trayIndex, 1);
                                        mRowsAdapter.notifyItemRangeChanged(trayIndex, 1);
                                        trayIndex--;
                                    }
                                } else {
                                    traylistRowAdapter.add(new BrowseFragmentLoaderCard(moduleData));

                                    ListRow listRow = new ListRow(customHeaderItem, traylistRowAdapter);
                                    Utils.recommendationTraysData.put(moduleData.getId(), listRow);
                                    Utils.recommendationTraysDataInndex.put(moduleData.getId(), mRowsAdapter.size());
                                    System.out.println(trayIndex + " ListRow trayIndex " + mRowsAdapter.size());
                                    mRowsAdapter.add(listRow);

                                    appCMSPresenter.populateRecommendedList(appCMSHistoryResult -> {
                                                int recommendationTraysIndex = Utils.recommendationTraysDataInndex.get(moduleData.getId());
                                                if (appCMSHistoryResult != null && appCMSHistoryResult.getRecords() != null) {
                                                    int contentDatumLength = appCMSHistoryResult.getRecords().size();
                                                    List<Record> recommendedRecords = appCMSHistoryResult.getRecords();
                                                    ArrayList<ContentDatum> recommendationContents = new ArrayList<>();

                                                    ListRow recommendationList = Utils.recommendationTraysData.get(appCMSHistoryResult.getModuleId());
                                                    ArrayObjectAdapter traylistRowAdapter1 = (ArrayObjectAdapter) recommendationList.getAdapter();
                                                    traylistRowAdapter1.clear();
                                                    List<String> autoPlayVideoIdList = new ArrayList<>();

                                                    for (int i = 0; i < contentDatumLength; i++) {
                                                        ContentDatum recordContentDatum = recommendedRecords.get(i).recommendationToContentDatum();
                                                        recommendationContents.add(recordContentDatum);
                                                        if (recommendationAutoPlayEnabled) {
                                                            autoPlayVideoIdList.add(recordContentDatum.getGist().getId());
                                                        }
                                                    }

                                                    if (!recommendationContents.isEmpty()) {
                                                        List<Component> components = component.getComponents();
                                                        int loopCount = ((List<ContentDatum>) recommendationContents).size()/*Math.min(contentData1.size(), seeAllItemCountThreshold)*/;
                                                        // boolean seeAllCardRequired = moduleData.getSettings().isSeeAll() && contentData1.size() > seeAllItemCountThreshold;
                                                        boolean seeAllCardRequired = moduleData.getSettings() != null
                                                                && moduleData.getSettings().isSeeAll()
                                                                /*&& contentData1.size() >= seeAllItemCountThreshold*/;
                                                        for (int i = 0; i < (seeAllCardRequired ? loopCount : ((List<ContentDatum>) recommendationContents).size()); i++) {
                                                            if (!(((List<ContentDatum>) recommendationContents).get(i).getTags() != null &&
                                                                    ((List<ContentDatum>) recommendationContents).get(i).getTags().size() > 0 &&
                                                                    ((List<ContentDatum>) recommendationContents).get(i).getTags().get(0).getTitle() != null &&
                                                                    ((List<ContentDatum>) recommendationContents).get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                                                                ContentDatum contentData = ((List<ContentDatum>) recommendationContents).get(i);
                                                                BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                                                rowData.moduleTitle = moduleData.getTitle();
                                                                rowData.contentData = contentData;
                                                                rowData.uiComponentList = components;
                                                                rowData.action = component.getTrayClickAction();
                                                                rowData.blockName = moduleUI.getBlockName();
                                                                rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                                                rowData.rowNumber = trayIndex;
                                                                rowData.itemPosition = i;
                                                                rowData.contentDatumList = recommendationContents;
                                                                rowData.relatedVideoIds = recommendationAutoPlayEnabled
                                                                        ? new ArrayList<>(autoPlayVideoIdList.subList(i, autoPlayVideoIdList.size()))
                                                                        : null;
                                                                traylistRowAdapter1.add(rowData);
                                                            }
                                                        }
                                                        if (seeAllCardRequired) {
                                                            traylistRowAdapter1.add(new SeeAllCard(moduleData));
                                                        }
                                                        moduleData.setContentData(recommendationContents);
                                                        if (CommonUtils.isRecommendationSubscribed(appCMSPresenter)) {
                                                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                                                if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                                                                    mRowsAdapter.replace(recommendationTraysIndex, recommendationList);
                                                                }
                                                            } else {
                                                                // Check it again
                                                                mRowsAdapter.removeItems(recommendationTraysIndex, 1);
                                                                mRowsAdapter.notifyItemRangeChanged(recommendationTraysIndex, 1);
                                                                trayIndex--;
                                                            }
                                                        } else {
                                                            if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                                                                mRowsAdapter.replace(recommendationTraysIndex, recommendationList);
                                                            }
                                                        }
                                                    } else {
                                                        // Check it again
                                                        mRowsAdapter.removeItems(recommendationTraysIndex, 1);
                                                        mRowsAdapter.notifyItemRangeChanged(recommendationTraysIndex, 1);
                                                        trayIndex--;
                                                    }
                                                } else {
                                                    mRowsAdapter.removeItems(recommendationTraysIndex, 1);
                                                    mRowsAdapter.notifyItemRangeChanged(recommendationTraysIndex, 1);
                                                    trayIndex--;
                                                }
                                            },
                                            moduleData.getFilters().getRecommendTrayType(),
                                            moduleData.getFilters().getContentType(),
                                            moduleData.getId());
                                }
                            }  else if (moduleData.getContentData() != null && (moduleUI.getBlockName().equalsIgnoreCase("userPersonalizatio01")
                                    || moduleUI.getBlockName().equalsIgnoreCase("userPersonalization01"))) {
                                System.out.println("moduleUI.getId(): " + moduleUI.getId());
                                userPersonalizationTrayIndex = mRowsAdapter.size();
                                System.out.println("trayIndex: " + trayIndex + ", mRowsAdapter.size: " + mRowsAdapter.size() + ", userPersonalizationTrayIndex: " + userPersonalizationTrayIndex);
                                if (appCMSPresenter.getPersonalizedGenresPreference() != null) {
                                    System.out.println("mRowsAdapter size: " + mRowsAdapter.size() + ", trayIndex: " + trayIndex);
                                    String userGenres = appCMSPresenter.getPersonalizedGenresPreference();
                                    String[] genres = userGenres.split("\\|");
                                    HashMap<String, Integer> genreToIndexMap = new HashMap(genres.length);

                                    System.out.println("genres: " + genres);
                                    int indexes[] = new int[genres.length];
                                    if (moduleData.getRecommendationRecords() == null || moduleIdToRowIndexMap.get(moduleUI.getId()) == null) {
                                        for (int i = 0, genresLength = genres.length; i < genresLength; i++) {
                                            String genre = genres[i];
                                            ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(classPresenterSelector);
                                            createHeaderItem(getDummyComponent(moduleUI.getLayout().getTv().getFontSize()), context, moduleUI, moduleData, moduleData.getTitle() + " " + genre, isCarousel);
                                            arrayObjectAdapter.add(new BrowseFragmentLoaderCard(moduleData));
                                            System.out.println("userPersonalizationTrayIndex: " + userPersonalizationTrayIndex + ", trayIndex: " + trayIndex);
                                            mRowsAdapter.add(userPersonalizationTrayIndex, new ListRow(customHeaderItem, arrayObjectAdapter));
                                            mRowsAdapter.notifyItemRangeChanged(userPersonalizationTrayIndex, 1);
                                            indexes[i] = userPersonalizationTrayIndex;
                                            genreToIndexMap.put(genre, userPersonalizationTrayIndex);
                                            userPersonalizationTrayIndex++;
                                        }
                                        moduleIdToRowIndexMap.put(moduleUI.getId(), indexes);
                                        moduleIdToGenreToRowIndexMap.put(moduleUI.getId(), genreToIndexMap);
                                        String recommendationTrayType = "personal";
                                        if (moduleUI != null && moduleUI.getSettings() != null
                                                && moduleUI.getSettings().getRecommendTrayType() != null) {
                                            recommendationTrayType = moduleUI.getSettings().getRecommendTrayType();
                                        }
                                        appCMSPresenter.getRecommendedGenreContent(
                                                recommendationTrayType,
                                                moduleData.getFilters().getContentType(),
                                                moduleData.getId(),
                                                genres,
                                                appCMSRecommendationGenreResult -> {
                                                    moduleData.setRecommendationRecords(appCMSRecommendationGenreResult);
                                                    populateUserPersonalizationTrays(context,
                                                            component,
                                                            moduleUI,
                                                            moduleData,
                                                            isCarousel,
                                                            classPresenterSelector,
                                                            getDummyComponent(moduleUI.getLayout().getTv().getFontSize()),
                                                            appCMSRecommendationGenreResult,
                                                            true,
                                                            recommendationAutoPlayEnabled);
                                                });
                                    } else {
                                        populateUserPersonalizationTrays(context,
                                                component,
                                                moduleUI,
                                                moduleData,
                                                isCarousel,
                                                classPresenterSelector,
                                                getDummyComponent(moduleUI.getLayout().getTv().getFontSize()),
                                                moduleData.getRecommendationRecords(),
                                                false,
                                                recommendationAutoPlayEnabled);
                                    }
                                }
                            } else if (moduleUI.getBlockName().contains("continueWatching")
                                    && appCMSPresenter.getAllUserHistory() != null
                                    && appCMSPresenter.getAllUserHistory().size() > 0
                                    && appCMSPresenter.isUserLoggedIn()) {
                                int continueWatchingTrayIndex = mRowsAdapter.size();
                                List<ContentDatum> contentData1 = appCMSPresenter.getAllUserHistory();
                                List<Component> components = component.getComponents();
                                int loopCount = contentData1.size();
                                boolean seeAllCardRequired = moduleData.getSettings() != null
                                        && moduleData.getSettings().isSeeAll();
                                for (int i = 0; i < (seeAllCardRequired ? loopCount : contentData1.size()); i++) {
                                    if (!(contentData1.get(i).getTags() != null &&
                                            contentData1.get(i).getTags().size() > 0 &&
                                            contentData1.get(i).getTags().get(0).getTitle() != null &&
                                            contentData1.get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                                        ContentDatum contentData = contentData1.get(i);
                                        BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                        rowData.moduleTitle = moduleData.getTitle();
                                        rowData.contentData = contentData;
                                        rowData.uiComponentList = components;
                                        rowData.action = component.getTrayClickAction();
                                        rowData.blockName = moduleUI.getBlockName();
                                        rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                        rowData.rowNumber = trayIndex;
                                        rowData.itemPosition = i;
                                        rowData.contentDatumList = contentData1;
                                        traylistRowAdapter.add(rowData);
                                    }
                                }
                                if (seeAllCardRequired) {
                                    traylistRowAdapter.add(new SeeAllCard(moduleData));
                                }
                                if (traylistRowAdapter.size() > 0) {
                                    createHeaderItem(getDummyComponent(moduleUI.getLayout().getTv().getFontSize()), context, moduleUI, moduleData, moduleData.getTitle(), isCarousel);
                                    mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                                }
                            } else if (moduleData.getContentData() != null && moduleData.getContentData().size() > 0) {
                                List<ContentDatum> contentData1 = moduleData.getContentData();
                                List<Component> components = component.getComponents();
                                int loopCount = contentData1.size()/*Math.min(contentData1.size(), seeAllItemCountThreshold)*/;
                                // boolean seeAllCardRequired = moduleData.getSettings().isSeeAll() && contentData1.size() > seeAllItemCountThreshold;
                                boolean seeAllCardRequired = moduleData.getSettings() != null
                                        && moduleData.getSettings().isSeeAll()
                                        /*&& contentData1.size() >= seeAllItemCountThreshold*/;
                                for (int i = 0; i < (seeAllCardRequired ? loopCount : contentData1.size()); i++) {
                                    if (!(contentData1.get(i).getTags() != null &&
                                            contentData1.get(i).getTags().size() > 0 &&
                                            contentData1.get(i).getTags().get(0).getTitle() != null &&
                                            contentData1.get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                                        ContentDatum contentData = contentData1.get(i);
                                        BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                                        rowData.moduleTitle = moduleData.getTitle();
                                        rowData.contentData = contentData;
                                        rowData.uiComponentList = components;
                                        rowData.action = component.getTrayClickAction();
                                        rowData.blockName = moduleUI.getBlockName();
                                        rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                                        rowData.rowNumber = trayIndex;
                                        rowData.itemPosition = i;
                                        rowData.contentDatumList = contentData1;
                                        traylistRowAdapter.add(rowData);
                                    }
                                }
                                if (seeAllCardRequired) {
                                    traylistRowAdapter.add(new SeeAllCard(moduleData));
                                }
                                mRowsAdapter.add(new ListRow(customHeaderItem, traylistRowAdapter));
                            }/* else {
                                trayIndex--;
                            }*/
                        }
                    }
                }
                break;

            case PAGE_VIDEO_PLAYER_VIEW_KEY:
                if (null != moduleData
                        && moduleData.getContentData() != null
                        && !moduleData.getContentData().isEmpty()) {
                    CustomTVVideoPlayerView videoPlayerView = (CustomTVVideoPlayerView) appCMSPresenter.getPlayerLruCache().get(appCMSPageAPI.getId());

                    createHeaderItem(component, context, moduleUI, moduleData, "", false);
                    customHeaderItem.setmIsLivePlayer(true);
                    PlayerPresenter playerPresenter = new PlayerPresenter(context, appCMSPresenter,
                            Integer.valueOf(component.getLayout().getTv().getHeight()),
                            Integer.valueOf(component.getLayout().getTv().getWidth()), component, jsonValueKeyMap, moduleUI);

                    if (videoPlayerView == null) {
                        videoPlayerView = playerPresenter.playerView(context);
                        playerPresenter.setVideoPlayerView(videoPlayerView, true);
                        appCMSPresenter.getPlayerLruCache().put(appCMSPageAPI.getId(), videoPlayerView);
                    } else {
                        playerPresenter.setVideoPlayerView(videoPlayerView, true);
                    }
                    ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(playerPresenter);
                    BrowseFragmentRowData browseFragmentRowData = new BrowseFragmentRowData();
                    browseFragmentRowData.blockName = moduleUI.getBlockName();
                    browseFragmentRowData.isPlayerComponent = true;
                    browseFragmentRowData.contentData = moduleData.getContentData().get(0);
                    browseFragmentRowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                    browseFragmentRowData.rowNumber = trayIndex;
                    browseFragmentRowData.itemPosition = 0;
                    listRowAdapter.add(browseFragmentRowData);
                    pageView.setIsStandAlonePlayerEnabled(true);
                    mRowsAdapter.add(new ListRow(customHeaderItem, listRowAdapter));
                }
                break;

        }
    }

    private Component getDummyComponent(int fontSize) {
        Component trayTitleComponent = new Component();
        trayTitleComponent.setTextAlignment("left");
        trayTitleComponent.setTextCase("caps");
        trayTitleComponent.setType("label");
        trayTitleComponent.setTextColor("#008EE0");
        trayTitleComponent.setFontWeight("Semibold");
        trayTitleComponent.setKey("trayTitle");
        Layout trayTitleComponentLayout = new Layout();
        FireTV trayTitleComponentLayoutFireTV = new FireTV();
        trayTitleComponentLayoutFireTV.setFontSize(fontSize != 0 ? fontSize : 20);
        trayTitleComponentLayout.setTv(trayTitleComponentLayoutFireTV);
        trayTitleComponent.setLayout(trayTitleComponentLayout);
        return trayTitleComponent;
    }

    private void populateUserPersonalizationTrays(Context context,
                                                  Component component,
                                                  ModuleList moduleUI,
                                                  Module moduleData,
                                                  boolean isCarousel,
                                                  ClassPresenterSelector classPresenterSelector,
                                                  Component trayTitleComponent,
                                                  AppCMSRecommendationGenreResult appCMSRecommendationGenreResult,
                                                  boolean replaceModule,
                                                  boolean recommendationAutoPlayEnabled) {
        if (appCMSRecommendationGenreResult != null) {
            int[] indexes2 = moduleIdToRowIndexMap.get(appCMSRecommendationGenreResult.getModuleId());
            HashMap<String, Integer> stringIntegerHashMap = moduleIdToGenreToRowIndexMap.get(appCMSRecommendationGenreResult.getModuleId());
            List<AppCMSRecommendationGenreRecord> appCMSRecommendationGenreResultRecords = appCMSRecommendationGenreResult.getRecords();
            for (int recordIndex = 0; recordIndex < appCMSRecommendationGenreResultRecords.size(); recordIndex++) {
                AppCMSRecommendationGenreRecord records = appCMSRecommendationGenreResultRecords.get(recordIndex);
                if (stringIntegerHashMap != null) {
                    stringIntegerHashMap.remove(records.getGenreValue());
                }
                ArrayObjectAdapter arrayObjectAdapter = new ArrayObjectAdapter(classPresenterSelector);
                createHeaderItem(trayTitleComponent, context, moduleUI, moduleData, moduleData.getTitle() + " " + records.getGenreValue(), isCarousel);
                List<ContentDatum> contentData1 = records.getGenreData();

                List<String> autoPlayVideoIdList = new ArrayList<>();
                if (recommendationAutoPlayEnabled) {
                    for (ContentDatum contentDatum : contentData1) {
                        autoPlayVideoIdList.add(contentDatum.getGist().getId());
                    }
                }

                    List<Component> components = component.getComponents();
                int loopCount = contentData1.size();
                boolean seeAllCardRequired = moduleData.getSettings() != null
                        && moduleData.getSettings().isSeeAll();
                for (int i = 0; i < (seeAllCardRequired ? loopCount : contentData1.size()); i++) {
                    if (!(contentData1.get(i).getTags() != null &&
                            contentData1.get(i).getTags().size() > 0 &&
                            contentData1.get(i).getTags().get(0).getTitle() != null &&
                            contentData1.get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                        ContentDatum contentData = contentData1.get(i);
                        BrowseFragmentRowData rowData = new BrowseFragmentRowData();
                        rowData.moduleTitle = moduleData.getTitle();
                        rowData.contentData = contentData;
                        rowData.uiComponentList = components;
                        rowData.action = component.getTrayClickAction();
                        rowData.blockName = moduleUI.getBlockName();
                        rowData.infoHover = moduleUI.getSettings() != null && moduleUI.getSettings().isInfoHover();
                        rowData.rowNumber = indexes2[recordIndex];
                        rowData.itemPosition = i;
                        rowData.relatedVideoIds = recommendationAutoPlayEnabled
                                ? new ArrayList<>(autoPlayVideoIdList.subList(i, autoPlayVideoIdList.size()))
                                : null;
                        rowData.contentDatumList = contentData1;
                        arrayObjectAdapter.add(rowData);
                    }
                }
                if (seeAllCardRequired) {
                    arrayObjectAdapter.add(new SeeAllCard(moduleData));
                }
                if (arrayObjectAdapter.size() > 0) {
                    if (replaceModule)
                        mRowsAdapter.replace(indexes2[recordIndex], new ListRow(customHeaderItem, arrayObjectAdapter));
                    else
                        mRowsAdapter.add(indexes2[recordIndex], new ListRow(customHeaderItem, arrayObjectAdapter));
                    mRowsAdapter.notifyItemRangeChanged(indexes2[recordIndex], 1);
                } else {
                    mRowsAdapter.removeItems(indexes2[recordIndex], 1);
                    System.out.println("mRowsAdapter size r: " + mRowsAdapter.size() + ", trayIndex: " + trayIndex);
                    mRowsAdapter.notifyMe();
                    trayIndex--;
                }
            }

            // the below is written to delete the rows for the genres which were selected by the
            // user, but the API didn't give response for that genre. Therefore, to avoid blank
            // row with loader spinning forever, we remove the whole tray.
            if (stringIntegerHashMap != null && stringIntegerHashMap.size() > 0) {
                for (Map.Entry<String, Integer> entry : stringIntegerHashMap.entrySet()){
                    if (entry.getValue() != null) {
                        mRowsAdapter.removeItems(entry.getValue(), 1);
                        System.out.println("mRowsAdapter size r: " + mRowsAdapter.size() + ", trayIndex: " + trayIndex);
                        mRowsAdapter.notifyMe();
                        trayIndex--;
                    }
                }
            }
        }
    }

    private void createHeaderItem(Component component, Context context, ModuleList moduleUI, Module moduleData, String name, boolean mIsCarousal) {
        String textCase = component.getTextCase();
        if (textCase != null) {
            if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_caps))) {
                name = name.toUpperCase();
            } else if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_small))) {
                name = name.toLowerCase();
            } else if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_sentence))) {
                String text = com.viewlift.Utils.convertStringIntoCamelCase(name);
                if (text != null) {
                    name = text;
                }
            }
        }
        trayIndex = trayIndex + 1;
        customHeaderItem = new CustomHeaderItem(context, trayIndex, name);
        customHeaderItem.setmIsCarousal(mIsCarousal);
        String padding = moduleUI.getLayout().getTv().getPadding();
        String topMargin = moduleUI.getLayout().getTv().getTopMargin();
        customHeaderItem.setmListRowLeftMargin(Integer.valueOf(padding != null ? padding : "0"));
        customHeaderItem.setmListRowRightMargin(Integer.valueOf(padding != null ? padding : "0"));
        customHeaderItem.setmListRowTopMargin(Integer.valueOf(topMargin != null ? topMargin : "-1"));
        customHeaderItem.setItemSpacing(moduleUI.getLayout().getTv().getItemSpacing());
        customHeaderItem.setmBackGroundColor(moduleUI.getLayout().getTv().getBackgroundColor());
        if (null != moduleUI.getLayout().getTv().getHeight()) {
            customHeaderItem.setmListRowHeight(Integer.valueOf(moduleUI.getLayout().getTv().getHeight()));
        }
        if (null != moduleUI.getLayout().getTv().getWidth()) {
            customHeaderItem.setmListRowWidth(Integer.valueOf(moduleUI.getLayout().getTv().getWidth()));
        }
        customHeaderItem.setFontFamily(component.getFontFamily());
        customHeaderItem.setFontWeight(component.getFontWeight());
        customHeaderItem.setFontSize(component.getLayout().getTv().getFontSize());
        customHeaderItem.setTextAlignment(component.getTextAlignment());
        customHeaderItem.setmModuleId((moduleData != null) ? moduleData.getId() : null);
    }

    private void createHeaderItemForSeason(Component component,
                                           Context context,
                                           String name) {
        String textCase = component.getTextCase();
        if (textCase != null) {
            if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_caps))) {
                name = name.toUpperCase();
            } else if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_small))) {
                name = name.toLowerCase();
            } else if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_sentence))) {
                String text = com.viewlift.Utils.convertStringIntoCamelCase(name);
                if (text != null) {
                    name = text;
                }
            }
        }
        trayIndex = trayIndex + 1;
        customHeaderItem = new CustomHeaderItem(context, trayIndex, name);
        customHeaderItem.setItemSpacing(component.getLayout().getTv().getItemSpacing());
        customHeaderItem.setmBackGroundColor(component.getLayout().getTv().getBackgroundColor());
        if (null != component.getLayout().getTv().getHeight()) {
            customHeaderItem.setmListRowHeight(Integer.valueOf(component.getLayout().getTv().getHeight()));
        }
        if (null != component.getLayout().getTv().getWidth()) {
            customHeaderItem.setmListRowWidth(Integer.valueOf(component.getLayout().getTv().getWidth()));
        }
        customHeaderItem.setFontFamily(component.getFontFamily());
        customHeaderItem.setFontWeight(component.getFontWeight());
        customHeaderItem.setFontSize(component.getLayout().getTv().getFontSize());
    }

    class ListTagHandler implements Html.TagHandler {
        boolean first = true;

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            try {
                if (tag.equals("li")) {
                    char lastChar = 0;
                    if (output.length() > 0)
                        lastChar = output.charAt(output.length() - 1);
                    if (first) {
                        if (lastChar == '\n')
                            output.append("•  ");
                        else
                            output.append("\n•  ");
                        first = false;
                    } else {
                        first = true;
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void createComponentView(final Context context,
                                    final Component component,
                                    final Layout parentLayout,
                                    final Module moduleAPI,
                                    @Nullable final TVPageView pageView,
                                    final Settings settings,
                                    Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                    final AppCMSPresenter appCMSPresenter,
                                    boolean gridElement,
                                    String viewType,
                                    boolean isFromLoginDialog) {
        componentViewResult.componentView = null;
        componentViewResult.useMarginsAsPercentagesOverride = true;
        componentViewResult.useWidthOfScreen = false;
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(context);

        if (moduleAPI == null) {
            return;
        }
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());
        if (componentType == null) {
            componentType = PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
        if (componentKey == null) {
            componentKey = PAGE_EMPTY_KEY;
        }

        switch (componentType) {

            case PAGE_COLLECTIONGRID_KEY:
                Log.d(TAG, "PAGE_COLLECTIONGRID_KEY");
                moduleAPI.setContentType("VIDEO");
                moduleAPI.setTitle("VIDEO");

//                List<ContentDatum> seriesFilterList = appCMSPresenter.filterData(moduleAPI, "shows" );
//                List<ContentDatum> bundleFilterList = appCMSPresenter.filterData(moduleAPI, "bundle" );
//                List<ContentDatum> videosFilterList = appCMSPresenter.filterData(moduleAPI, "video" );

                mRowsAdapter = null;
                createTrayModule(context, component, parentLayout, moduleList, moduleAPI,
                        pageView, jsonValueKeyMap, appCMSPresenter, null, false, false);

                createTrayModule(context, component, parentLayout, moduleList, moduleAPI,
                        pageView, jsonValueKeyMap, appCMSPresenter, null, false, false);

                createTrayModule(context, component, parentLayout, moduleList, moduleAPI,
                        pageView, jsonValueKeyMap, appCMSPresenter, null, false, false);


                break;

            case PAGE_UPCOMING_TIMER_KEY:
                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getPricing() != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 ||
                                "FREE".equalsIgnoreCase(moduleAPI.getContentData().get(0).getPricing().getType()))) {
                    VisualTimer visualTimer = new VisualTimer(context, moduleAPI, component, appCMSPresenter);
                    visualTimer.setId(R.id.visualTimer);

                    /*VisualTimer is just created here, where the start watching button is created,
                     * the visual timer's visibility is handled there.*/
                    visualTimer.setVisibility(GONE);
                    componentViewResult.componentView = visualTimer;
                }
                break;

            case PAGE_RENT_ACTIVE_COMPONENT_KEY:
                if (moduleAPI.getContentData().get(0).getPricing() != null
                        && "TVOD".equalsIgnoreCase(moduleAPI.getContentData().get(0).getPricing().getType())) {
                    RentActiveComponent rentActiveComponent = new RentActiveComponent(context, appCMSPresenter, moduleAPI.getContentData().get(0));
                    rentActiveComponent.setVisibility(GONE);

                    appCMSPresenter.getRentalData(moduleAPI.getContentData().get(0).getGist().getId(), updatedContentDatum -> {
                        appCMSPresenter.showLoadingDialog(false);

                        long transactionEndDateMillis = updatedContentDatum.getTransactionEndDate() * 1000L;
                        Log.d(TAG, "ANAS RENTAL RESPONSE: " + transactionEndDateMillis + ", current: " + System.currentTimeMillis());

                        if (transactionEndDateMillis > System.currentTimeMillis()) {
                            componentViewResult.componentView.setVisibility(VISIBLE);
                            rentActiveComponent.setRentExpirationMillis(transactionEndDateMillis);
                        } else {

                        }

                    }, false, 0);
                    // TODO: 27/08/18 ANAS: start rent time to be updated here
                    componentViewResult.componentView = rentActiveComponent;
                }
                break;
            case PAGE_AUTOPLAY_ROTATING_LOADER_VIEW_KEY:
                componentViewResult.componentView = new AppCMSTVAutoplayCustomLoader(context, component);
                componentViewResult.componentView.setId(R.id.autoplay_rotating_loader_view_id);
                break;
            case PAGE_VERTICAL_GRID_VIEW_KEY:

                componentViewResult.componentView = new VerticalGridView(context);
                componentViewResult.componentView.setFocusable(true);

                if (component.getLayout().getTv().getItemSpacing() != null) {
                    ((RecyclerView) componentViewResult.componentView).addItemDecoration(new RecyclerViewItemDecoration(Integer.parseInt(component.getLayout().getTv().getItemSpacing()), RecyclerViewItemDecoration.VERTICAL));
                }
                if (jsonValueKeyMap.get(component.getKey()) == PAGE_EPISODE_TABLE_VIEW_KEY) {
                    (componentViewResult.componentView).setId(R.id.episode_table_view);
                } else if (jsonValueKeyMap.get(component.getKey()) == PAGE_SEASON_TABLE_VIEW_KEY) {
                    (componentViewResult.componentView).setId(R.id.season_table_view);
                } else if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_SEGMENT_TABLE_VIEW_KEY) {
                    (componentViewResult.componentView).setId(R.id.segmentTableView);
                    componentViewResult.componentView.setNextFocusLeftId(R.id.episode_table_view);
                } else {
                    componentViewResult.componentView.setId(R.id.tv_recycler_view);
                }
                AppCMSTVTrayAdapter appCMSTVTrayAdapter = new AppCMSTVTrayAdapter(context,
                        moduleAPI.getContentData(),
                        component,
                        component.getLayout(),
                        appCMSPresenter,
                        jsonValueKeyMap,
                        viewType,
                        this,
                        moduleAPI, settings);

                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSTVTrayAdapter);
                componentViewResult.onInternalEvent = appCMSTVTrayAdapter;
                break;
            case PAGE_TABLE_VIEW_KEY:
                componentViewResult.componentView = new RecyclerView(context);
                componentViewResult.componentView.setFocusable(true);

                componentViewResult.componentView.setId(R.id.tv_recycler_view);
                componentViewResult.componentView.setNextFocusDownId(R.id.tv_recycler_view);

                switch (componentKey) {
                    case PAGE_SEE_ALL_COLLECTIONGRID_KEY:

                        RecyclerView recyclerViewSeeAll = ((RecyclerView) componentViewResult.componentView);

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 4,
                                LinearLayoutManager.VERTICAL, false);
                        recyclerViewSeeAll
                                .setLayoutManager(gridLayoutManager);

                        recyclerViewSeeAll.setClipToPadding(false);

                        AppCMSTVSeeAllAdapter appCMSTVSeeAllAdapter = new AppCMSTVSeeAllAdapter(context,
                                moduleAPI.getContentData(),
                                component,
                                component.getLayout(),
                                appCMSPresenter,
                                jsonValueKeyMap,
                                viewType,
                                this,
                                moduleAPI, settings);

                        recyclerViewSeeAll
                                .setAdapter(appCMSTVSeeAllAdapter);

                        appCMSPresenter.setLastPage(!moduleAPI.hasNext());


                        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                            @Override
                            public int getSpanSize(int position) {
                                switch (appCMSTVSeeAllAdapter.getItemViewType(position)) {
                                    case AppCMSTVSeeAllAdapter.ITEM:
                                        return 1;
                                    case AppCMSTVSeeAllAdapter.LOADING:
                                        int coloumns = 2;
                                        if (BaseView.isTablet(context))
                                            coloumns = 3;
                                        if (BaseView.isLandscape(context))
                                            coloumns = 4;
                                        return coloumns; //number of columns of the grid
                                    default:
                                        return -1;
                                }
                            }
                        });

                        recyclerViewSeeAll.addOnScrollListener(new PaginationScrollListener(context, gridLayoutManager, recyclerViewSeeAll) {
                            @Override
                            protected void loadMoreItems() {
                                isLoading = true;
//                                appCMSTVSeeAllAdapter.setClickable(false);
                                if (!appCMSPresenter.isLastPage())
                                    appCMSTVSeeAllAdapter.addLoadingFooter();
                                new Handler().postDelayed(() -> {
                                    appCMSPresenter.getTVSeeAllCategory(
                                            moduleAPI.getFilters() != null
                                                    ? moduleAPI.getFilters().getLimit()
                                                    : 0,
                                            moduleAPI.getId(),
                                            appCMSPageAPI -> {
                                                if (appCMSPageAPI != null) {
                                                    for (Module module : appCMSPageAPI.getModules()) {
                                                        if (module.getModuleType().equalsIgnoreCase("CategoryDetailModule")
                                                                ||module.getModuleType().equalsIgnoreCase("GeneratedTrayModule")) {
                                                            appCMSPresenter.setLastPage(!module.hasNext());
                                                            if (module.getContentData() != null && module.getContentData().size() > 0) {
                                                                isLoading = false;
                                                                appCMSTVSeeAllAdapter.removeLoadingFooter();
                                                                int appCMSTVSeeAllAdapterItemCount = appCMSTVSeeAllAdapter.getItemCount();
                                                                appCMSTVSeeAllAdapter.addAll(module.getContentData());
                                                                appCMSTVSeeAllAdapter.notifyItemRangeInserted(appCMSTVSeeAllAdapterItemCount, module.getContentData().size());
                                                                TextView seeAllTitle = appCMSPresenter.getCurrentActivity().findViewById(R.id.see_all_title_text_view);
                                                                if (seeAllTitle != null &&
                                                                        appCMSPresenter.getSeeAllModule() != null &&
                                                                        appCMSPresenter.getSeeAllModule().getTitle() != null) {
                                                                    String title;
                                                                    if (!TextUtils.isEmpty(moduleAPI.getTitle())) {
                                                                        title = moduleAPI.getTitle();
                                                                    } else if (appCMSPresenter.getSeeAllModule() != null
                                                                            && !TextUtils.isEmpty(appCMSPresenter.getSeeAllModule().getTitle())) {
                                                                        title = appCMSPresenter.getSeeAllModule().getTitle();
                                                                    } else {
                                                                        title = "This Category";
                                                                    }
                                                                    seeAllTitle.setText(appCMSPresenter.getLanguageResourcesFile().getStringValue(context.getString(R.string.items_in_category, appCMSTVSeeAllAdapter.getItemCount(), title)));
                                                                }
                                                            }
                                                            break;
                                                        }
                                                    }

                                                } else {
                                                    appCMSTVSeeAllAdapter.removeLoadingFooter();
//                                                appCMSTVSeeAllAdapter.setClickable(true);
                                                    //appCMSTVTrayItemAdapter.showRetry(true, com.viewlift.Utils.fetchErrorMessage(context,null));
                                                }
                                            });
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

                        componentViewResult.onInternalEvent = appCMSTVSeeAllAdapter;
                        break;

                    case PAGE_GRID_VIEW_COLLECTIONGRID_KEY:

                        RecyclerView gridView = ((RecyclerView) componentViewResult.componentView);
                        componentViewResult.componentView.setId(R.id.PAGE_GRID_VIEW_COLLECTIONGRID_KEY);

                        gridView.setFocusable(false);
                        if (component.getBackgroundColor() != null) {
                            gridView.setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, appCMSPresenter.getGeneralTextColor(),0,0, component.getCornerRadius()));
                        }

                        GridLayoutManager gridViewLayoutManager = new GridLayoutManager(context, 4,
                                LinearLayoutManager.VERTICAL, false);
                        gridView.setLayoutManager(gridViewLayoutManager);

                        if (component.getLayout().getTv().getItemSpacing() != null) {
                            gridView.addItemDecoration(new RecyclerViewItemDecoration(Integer.parseInt(component.getLayout().getTv().getItemSpacing()), RecyclerViewItemDecoration.GRID));
                        }

                        gridView.setClipToPadding(false);
                        if (TextUtils.isEmpty(appCMSPresenter.getSelectedGenreString())) {
                            return;
                        }

                        if (appCMSPresenter.getSelectedGenreString() != null) {
                            List<String> userRecommendedList = Arrays.asList(appCMSPresenter.getSelectedGenreString().split("[|]"));
                            if (userRecommendedList != null && userRecommendedList.size() > 0) {
                                List<ContentDatum> contentData = new ArrayList<>();
                                for (int i = 0; i < userRecommendedList.size(); i++) {
                                    ContentDatum contentDatum = new ContentDatum();
                                    contentDatum.setTitle(userRecommendedList.get(i));
                                    contentDatum.setId("personalizationView");
                                    contentData.add(contentDatum);
                                }
                                moduleAPI.setContentData(contentData);
                            }
                        }

                        AppCMSTVSeeAllAdapter gridViewAdapter = new AppCMSTVSeeAllAdapter(context,
                                moduleAPI.getContentData(),
                                component,
                                component.getLayout(),
                                appCMSPresenter,
                                jsonValueKeyMap,
                                viewType,
                                this,
                                moduleAPI, settings);

                        gridView.setAdapter(gridViewAdapter);
                        componentViewResult.onInternalEvent = gridViewAdapter;
                        break;

                    case PAGE_LINK_TABLE_VIEW:
                        componentViewResult.componentView = new RecyclerView(context);
                        componentViewResult.componentView.setFocusable(true);
                        RecyclerView pageLinkRecyclerView = ((RecyclerView) componentViewResult.componentView);
                        pageLinkRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL,
                                false));

                        pageLinkRecyclerView.setClipToPadding(false);

                        List<ContentDatum> contentDatumList = new ArrayList<>();
                        for (ContentDatum contentDatum : moduleAPI.getContentData()) {
                            ContentDatum titleData = new ContentDatum();
                            Gist gist = new Gist();
                            String title1 = contentDatum.getTrayTitle();
                            if (title1 == null) {
                                title1 = contentDatum.getCategoryTitle();
                            }
                            gist.setTitle(title1);
                            titleData.setGist(gist);
                            contentDatumList.add(titleData);

                            ContentDatum contentDatum1 = new ContentDatum();
                            contentDatum1.setPages(contentDatum.getPages());
                            contentDatumList.add(contentDatum1);

                        }

                        AppCMSTVPageLinkAdapter appCMSTVPageLinkAdapter = new AppCMSTVPageLinkAdapter(context,
                                contentDatumList,
                                component,
                                component.getLayout(),
                                appCMSPresenter,
                                jsonValueKeyMap,
                                viewType,
                                this,
                                moduleAPI, settings);
                        pageLinkRecyclerView.setAdapter(appCMSTVPageLinkAdapter);
                        componentViewResult.componentView.setId(R.id.pagelink_recycler_view);
                        break;


                    case PAGE_LINK_TABLE_GRID_VIEW:

                        componentViewResult.componentView = new RecyclerView(context);
                        componentViewResult.componentView.setFocusable(true);
                        RecyclerView pageLinkTableView = ((RecyclerView) componentViewResult.componentView);


                        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(context, 3,
                                LinearLayoutManager.VERTICAL, false);

                        pageLinkTableView.setLayoutManager(gridLayoutManager1);


                        ItemDecoration itemDecoration = new ItemDecoration(context, R.dimen.grid_padding);

                        pageLinkTableView.addItemDecoration(itemDecoration);


                        pageLinkTableView.setClipToPadding(false);

                        break;

                    case NAVIGATION_TABLE_VIEW:
                        componentViewResult.componentView = null;
                        RecyclerView navigationRecylerView = new RecyclerView(context);
                        //componentViewResult.componentView.setFocusable(true);


                        navigationRecylerView.setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL,
                                false));

                        navigationRecylerView.setClipToPadding(false);
                        navigationRecylerView.setFocusable(false);
                        navigationRecylerView.setId(IdUtils.getID(component.getId()));

                        List<ContentDatum> contentDatumList1 = appCMSPresenter.getDataFromHeaderNav(settings.getNavigationId());


                        ImageView imageView = appCMSPresenter.getCurrentActivity().findViewById(R.id.nav_module_logo);
                        Headers header = appCMSPresenter.getHeaderObj(settings.getNavigationId());
                        if (header != null && header.getLogoURL() != null) {
                            Glide.with(context)
                                    .load(header.getLogoURL())

//                                .centerCrop()
                                    .into((ImageView) imageView);
                        }
                        RelativeLayout frameLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.left_navigation_layouts_container);
                        if (contentDatumList1.size() > 0) {
                            AppCMSTVTrayAdapter appCMSTVTrayItemAdapter = new AppCMSTVTrayAdapter(context,
                                    contentDatumList1,
                                    component,
                                    component.getLayout(),
                                    appCMSPresenter,
                                    jsonValueKeyMap,
                                    viewType,
                                    this,
                                    moduleAPI,
                                    settings);
                            navigationRecylerView
                                    .setAdapter(appCMSTVTrayItemAdapter);
                            if (appCMSPresenter.getCurrentActivity() != null
                                    && appCMSPresenter.getCurrentActivity().findViewById(R.id.left_navigation_parent_container) != null
                                    && appCMSPresenter.getCurrentActivity().findViewById(R.id.left_navigation_parent_container).getBackground() != null) {
                                appCMSPresenter.getCurrentActivity().findViewById(R.id.left_navigation_parent_container).getBackground().setTint(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
                            }
                            if (frameLayout != null) {
                                frameLayout.removeAllViews();
                                frameLayout.addView(navigationRecylerView);
                            }
                            frameLayout.setVisibility(VISIBLE);
                        } else {
                            frameLayout.setVisibility(View.INVISIBLE);
                        }
                        //  new Handler().postDelayed(()->((AppCmsHomeActivity)appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true),1000 );//

                        break;

                    default:

                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.VERTICAL,
                                        false));
                        ((RecyclerView) componentViewResult.componentView).setFocusable(false);
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_SEASON_TABLE_VIEW_KEY) {
                            (componentViewResult.componentView).setId(R.id.season_table_view);
                        }

                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_SEGMENT_TABLE_VIEW_KEY) {
                            (componentViewResult.componentView).setId(R.id.segmentTableView);
                            if (component.getLayout().getTv().getItemSpacing() != null) {
                                ((RecyclerView) componentViewResult.componentView).addItemDecoration(new RecyclerViewItemDecoration(Integer.parseInt(component.getLayout().getTv().getItemSpacing()), RecyclerViewItemDecoration.VERTICAL));
                            }
                        }

                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_EPISODE_TABLE_VIEW_KEY) {
                            (componentViewResult.componentView).setId(R.id.episode_table_view);
                            if (component.getLayout().getTv().getItemSpacing() != null) {
                                ((RecyclerView) componentViewResult.componentView).addItemDecoration(new RecyclerViewItemDecoration(Integer.parseInt(component.getLayout().getTv().getItemSpacing()), RecyclerViewItemDecoration.VERTICAL));
                            }
                        }

                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_LIVE_PLAYER_COMPONENT_TABLE_VIEW_KEY) {
                            if(settings != null && settings.isShowTabs()) {
                                moduleAPI.setContentData(settings.convertTabListToContentDatum(moduleAPI));
                            }else{
                                return;
                            }
                        }

                        ((RecyclerView) componentViewResult.componentView).setClipToPadding(false);
                        if (null != component.getLayout().getTv().getOrientation()) {
                            String orientation = component.getLayout().getTv().getOrientation();
                            if (orientation.equalsIgnoreCase("horizontal")) {
                                ((RecyclerView) componentViewResult.componentView)
                                        .setLayoutManager(new LinearLayoutManager(context,
                                                LinearLayoutManager.HORIZONTAL,
                                                false));
                                if (component.getLayout().getTv().getItemSpacing() != null)
                                    ((RecyclerView) componentViewResult.componentView).addItemDecoration(new RecyclerViewItemDecoration(Integer.parseInt(component.getLayout().getTv().getItemSpacing()), RecyclerViewItemDecoration.HORIZONTAL));
                            }
                        }

                        AppCMSTVTrayAdapter appCMSTVTrayItemAdapter = new AppCMSTVTrayAdapter(context,
                                moduleAPI.getContentData(),
                                component,
                                component.getLayout(),
                                appCMSPresenter,
                                jsonValueKeyMap,
                                viewType,
                                this,
                                moduleAPI,
                                settings);
                        ((RecyclerView) componentViewResult.componentView)
                                .setAdapter(appCMSTVTrayItemAdapter);
                        componentViewResult.onInternalEvent = appCMSTVTrayItemAdapter;
                }

                break;

            case PAGE_CHECKBOX_KEY:
                componentViewResult.componentView = new CheckBox(context);
                switch (componentKey) {
                    case PAGE_EMAIL_CONSENT_CHECKBOX_KEY:
                        CheckBox checkBox = (CheckBox) componentViewResult.componentView;
                        checkBox.setId(R.id.emailConsentCheckbox);
                        checkBox.setTypeface(Utils.getTypeFace(context,
                                appCMSPresenter,
                                component));

                        checkBox.setVisibility(GONE);
                        String countryCode = com.viewlift.Utils.getCountryCode();

                        EmailConsent emailConsent = appCMSPresenter.getAppCMSMain().getEmailConsent();
                        if(emailConsent != null && emailConsent.isEnableEmailConsent()) {
                            EmailConsentMessage emailConsentMessage = emailConsent.getLocalizationMap().get(countryCode);
                            if (!CommonUtils.isEmpty(emailConsent.getDefaultMessage()) || (emailConsentMessage != null && !CommonUtils.isEmpty(emailConsentMessage.getMessage()))) {
                                checkBox.setSingleLine(false);
                                //checkBox.setMaxLines(component.getNumberOfLines());
                                checkBox.setEllipsize(TextUtils.TruncateAt.END);
                                (checkBox).setTextColor(appCMSPresenter.getGeneralTextColor());
                                checkBox.setText(emailConsent.getDefaultMessage());
                                checkBox.setChecked(emailConsent.isDefaultChecked());

                                if (!TextUtils.isEmpty(component.getFontFamily())) {
                                    Typeface tf = com.viewlift.tv.utility.Utils.getTypeFace(context, appCMSPresenter, component);
                                    if (tf != null) {
                                        checkBox.setTypeface(tf);
                                    }
                                }

                                if (component.getFontSize() > 0) {
                                    ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                                }else if(component.getLayout().getTv().getFontSize() > 0){
                                    ((TextView) componentViewResult.componentView).setTextSize(component.getLayout().getTv().getFontSize());
                                }
                                if (emailConsentMessage != null) {
                                    checkBox.setChecked(emailConsentMessage.isChecked());
                                    if (emailConsentMessage.getMessage()!=null && !CommonUtils.isEmpty(emailConsentMessage.getMessage())) {
                                        checkBox.setText(emailConsentMessage.getMessage());
                                    }
                                }
                                checkBox.setVisibility(View.VISIBLE);
                            }else {
                                checkBox.setVisibility(GONE);
                            }
                        }else{
                            checkBox.setVisibility(GONE);
                        }
                        /*appCMSPresenter.getEmailConsentAPI(appCMSEmailConsentValueMap -> {
                            if (appCMSEmailConsentValueMap != null && appCMSEmailConsentValueMap.size() > 0) {
                                System.out.println(appCMSEmailConsentValueMap);
                                if (appCMSEmailConsentValueMap.keySet().contains(countryCode)) {
                                    for (Map.Entry<String, AppCMSEmailConsentValue> entry : appCMSEmailConsentValueMap.entrySet()) {
                                        System.out.println("Key = " + entry.getKey() +
                                                ", Value = " + entry.getValue());

                                        if (entry.getKey().equalsIgnoreCase(countryCode)) {
                                            System.out.println("Found the right match");
                                            AppCMSEmailConsentValue appCMSEmailConsentValue = entry.getValue();
                                            if (appCMSEmailConsentValue.getVisible()) {
                                                checkBox.setText(appCMSEmailConsentValue.getCopy());
                                                checkBox.setChecked(appCMSEmailConsentValue.getChecked());
                                                checkBox.setVisibility(VISIBLE);
                                            } else {
                                                checkBox.setVisibility(View.GONE);
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    AppCMSEmailConsentValue defaultAppCMSEmailConsentValue =
                                            appCMSEmailConsentValueMap.get("default");
                                    if (defaultAppCMSEmailConsentValue != null
                                            && defaultAppCMSEmailConsentValue.getVisible()) {
                                        checkBox.setText(defaultAppCMSEmailConsentValue.getCopy());
                                        checkBox.setChecked(defaultAppCMSEmailConsentValue.getChecked());
                                        checkBox.setVisibility(VISIBLE);
                                    } else {
                                        checkBox.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                checkBox.setVisibility(View.GONE);
                            }
                        });*/
                        break;
                }
                break;
            case PAGE_AGE_CONSENT_CHECKBOX_KEY:
                componentViewResult.componentView = new CheckBox(context);
                CheckBox checkBox = (CheckBox) componentViewResult.componentView;
                checkBox.setId(R.id.ageConsentCheckbox);
                checkBox.setVisibility(VISIBLE);
                checkBox.setText(component.getText());
                break;
            case PAGE_BUTTON_KEY:
                if (componentKey != PAGE_VIDEO_CLOSE_KEY) {
                    componentViewResult.componentView = new Button(context);
                    ((Button) componentViewResult.componentView).setStateListAnimator(null);
                } else {
                    componentViewResult.componentView = new ImageButton(context);
                }
                if (!gridElement) {
                    if (!TextUtils.isEmpty(component.getText()) && componentKey != PAGE_PLAY_KEY) {
                        String title = resources.getUIresource(component.getText());
                        ((TextView) componentViewResult.componentView).setText(title != null ? title : component.getText());
                        if (component.getFontSize() != 0)
                            ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                    } else if (moduleAPI.getSettings() != null &&
                            !moduleAPI.getSettings().getHideTitle() &&
                            !TextUtils.isEmpty(moduleAPI.getTitle()) &&
                            componentKey != PAGE_VIDEO_CLOSE_KEY) {
                        ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                    }
                }

                if (!TextUtils.isEmpty(component.getTextColor())) {
                    ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(getColor(context, component.getTextColor())));
                    if (!TextUtils.isEmpty(component.getBorderColor())) {
                        ((TextView) componentViewResult.componentView).setTextColor(Utils.getButtonTextColorDrawable(
                                getColor(context, component.getBorderColor()),
                                getColor(context, component.getTextColor()), appCMSPresenter));
                    }
                }
                if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                    componentViewResult.componentView.setBackgroundColor(Color.parseColor(getColor(context, component.getBackgroundColor())));
                } else {
                    componentViewResult.componentView.setBackground(
                            Utils.setButtonBackgroundSelector(context,
                                    Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)),
                                    component,
                                    appCMSPresenter));
                }

                if (component.getLetetrSpacing() != 0) {
                    ((TextView) componentViewResult.componentView).
                            setLetterSpacing(component.getLetetrSpacing());
                }

                int tintColor = Color.parseColor(getColor(context,
                        Utils.getFocusColor(context, appCMSPresenter)));

                switch (componentKey) {
                    case PAGE_WATCHLIST_DELETE_ITEM_BUTTON:
                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getDeleteItemButton() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getDeleteItemButton());
                        }
                        break;
                    case CHANGE_LANGUAGE_KEY:
                        ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getSelectCta());
                        break;
                    case OPEN_SIGN_UP_PAGE_BUTTON_KEY:
                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getSignUpCtaText() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getSignUpCtaText());
                        }
                        componentViewResult.componentView.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((false));
                            }

                        });
                        componentViewResult.componentView.setOnClickListener(v -> {
                            //check restore purchase first.
                            if (appCMSPresenter.getNavigation() != null
                                    && appCMSPresenter.getSubscriptionPage() != null
                                    && !appCMSPresenter.isUserLoggedIn()
                                    && appCMSPresenter.isAppSVOD()
                                    && appCMSPresenter.isFireTVSubscriptionEnabled()) {

                                Fragment prev = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
                                boolean isDialogVisible = prev != null && prev instanceof DialogFragment;
                                System.out.println("isDialogVisible = " + isDialogVisible);
                                String pageId = appCMSPresenter.getSubscriptionPage().getPageId();
                                String title = context.getResources().getString(R.string.view_plans_label);

                                if (appCMSPresenter.getGenericMessages() != null
                                        && appCMSPresenter.getGenericMessages().getViewPlansCta() != null) {
                                    title = appCMSPresenter.getGenericMessages().getViewPlansCta();
                                }
                                appCMSPresenter.setViewPlanPageOpenFromADialog(isDialogVisible);

                                appCMSPresenter.navigateToTVPage(
                                        pageId,
                                        title,
                                        null,
                                        false,
                                        Uri.EMPTY,
                                        true,
                                        false,
                                        isDialogVisible, true, false, false);
                            } else {
                                appCMSPresenter.navigateToSignUpPage(isFromLoginDialog);
                            }
                        });
                        break;
                    case PAGE_SHOW_SWITCH_SEASONS_KEY:
                        SwitchSeasonsDialogFragment.setSelectedSeasonIndex(0);
                        componentViewResult.componentView.setOnClickListener(v -> {
                            AppCMSSwitchSeasonBinder appCMSSwitchSeasonBinder =
                                    new AppCMSSwitchSeasonBinder(
                                            moduleAPI.getContentData().get(0).getSeason(),
                                            null,
                                            component,
                                            component.getAction(),
                                            component.getBlockName(),
                                            0,
                                            0);
                            Utils.showSwitchSeasonsDialog(appCMSSwitchSeasonBinder, appCMSPresenter);
                        });
                        break;
                    case PAGE_INFO_KEY:
                        componentViewResult.componentView.setBackground(context.getDrawable(R.drawable.info_icon));
                        componentViewResult.componentView.setFocusable(false);
                        break;

                    case PAGE_VIDEO_WATCH_TRAILER_KEY:
                        if (isVideoTrailerAvailable(moduleAPI) ) {
                            View btnWatchTrailer = componentViewResult.componentView;
                            componentViewResult.componentView.setFocusable(true);
                            componentViewResult.componentView.setId(R.id.btn_watch_trailer);
                            componentViewResult.componentView.setTag("WATCH_TRAILER");
                            componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    appCMSPresenter.showLoadingDialog(true);
                                    playTrailer(moduleAPI, appCMSPresenter, component,false);

                                    // Disable the button for 1 second and enable it back in handler
                                    btnWatchTrailer.setClickable(false);

                                    // enable the button after 1 second
                                    new Handler().postDelayed(() ->
                                            btnWatchTrailer.setClickable(true), 1000);
                                }
                            });
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getWatchTrailerCTA() != null) {
                                ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getWatchTrailerCTA());
                            }
                        } else {
                            componentViewResult.componentView = null;
                        }
                        break;


                    case PAGE_SHOW_WATCH_TRAILER_KEY:
                        if (isShowTrailerAvailable(moduleAPI) /*&&
                                moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getVideoAssets() != null*/) {
                            View btnWatchTrailer = componentViewResult.componentView;
                            componentViewResult.componentView.setFocusable(true);
                            componentViewResult.componentView.setId(R.id.btn_watch_trailer);
                            componentViewResult.componentView.setTag("WATCH_TRAILER");
                            componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    appCMSPresenter.showLoadingDialog(true);
                                    playTrailer(moduleAPI, appCMSPresenter, component,true);

                                    // Disable the button for 1 second and enable it back in handler
                                    btnWatchTrailer.setClickable(false);

                                    // enable the button after 1 second
                                    new Handler().postDelayed(() ->
                                            btnWatchTrailer.setClickable(true), 1000);
                                }
                            });

                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getWatchTrailerCTA() != null) {
                                ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getWatchTrailerCTA());
                            }

                        } else {
                            componentViewResult.componentView = null;
                        }
                        break;


                    case PAGE_BUNDLE_WATCH_TRAILER_KEY:
                        if (isVideoTrailerAvailable(moduleAPI)) {
                            View btnWatchTrailer = componentViewResult.componentView;
                            componentViewResult.componentView.setFocusable(true);
                            componentViewResult.componentView.setId(R.id.btn_watch_trailer);
                            componentViewResult.componentView.setTag("WATCH_TRAILER");
                            componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    appCMSPresenter.showLoadingDialog(true);
                                    playTrailer(moduleAPI, appCMSPresenter, component,false);
                                    // Disable the button for 1 second and enable it back in handler
                                    btnWatchTrailer.setClickable(false);

                                    // enable the button after 1 second
                                    new Handler().postDelayed(() ->
                                            btnWatchTrailer.setClickable(true), 1000);
                                }
                            });
                        } else {
                            componentViewResult.componentView = null;
                        }
                        break;

                    case PAGE_ADD_TO_WATCHLIST_WITH_ICON_KEY:
                    case PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY:
                        if (appCMSPresenter.isNewsTemplate()) {
                            componentViewResult.componentView.setBackground(
                                    Utils.setButtonBackgroundSelectorForNewsTemplate(context,
                                            Utils.getFocusBorderColor(context, appCMSPresenter),
                                            Utils.getFocusColor(context, appCMSPresenter),
                                            component,
                                            appCMSPresenter));
                        }
                        componentViewResult.componentView.setFocusable(true);
                        componentViewResult.componentView.setId(R.id.btn_add_to_watchlist);
                        componentViewResult.componentView.setTag("WATCHLIST");
                        Button btn = (Button) componentViewResult.componentView;
                        Drawable addToWatchlistImageDrawable = null, removeToWatchlistImageDrawable = null;

                        if (appCMSPresenter.isNewsTemplate() && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0 &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType().equalsIgnoreCase("SERIES")) {
                            btn.setVisibility(GONE);
                        } else {
                                btn.setVisibility(VISIBLE);
                                if (jsonValueKeyMap.get(component.getKey()) == PAGE_ADD_TO_WATCHLIST_WITH_ICON_KEY) {
                                    addToWatchlistImageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add_to_watchlist);
                                    Utils.setImageColorFilter(null, addToWatchlistImageDrawable, appCMSPresenter);

                                    removeToWatchlistImageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_remove_from_watchlist);
                                    Utils.setImageColorFilter(null, removeToWatchlistImageDrawable, appCMSPresenter);
                                }
                                updateAddToWatchlistButtonStatus(context, appCMSPresenter, btn, moduleAPI, addToWatchlistImageDrawable, removeToWatchlistImageDrawable);
                        }
                        break;

                    case PAGE_ADD_TO_WATCHLIST_KEY:
                        componentViewResult.componentView.setFocusable(true);
                        componentViewResult.componentView.setTag("WATCHLIST");
                        break;

                    case PAGE_START_WATCHING_FROM_BEGINNING_BUTTON_KEY: {
                        Button startFromBeginning = (Button) componentViewResult.componentView;
                        View componentView = componentViewResult.componentView;
                        componentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        startFromBeginning.setId(R.id.btn_start_watching_from_beginning);
                        startFromBeginning.setVisibility(View.GONE);
                        if (appCMSPresenter.isUserLoggedIn()) {
                            if (null != moduleAPI.getContentData() && moduleAPI.getContentData().size() > 0) {
                                String videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                                if (videoId == null) {
                                    videoId = moduleAPI.getContentData().get(0).getGist().getId();
                                }

                                appCMSPresenter.getUserVideoStatus(
                                        videoId,
                                        userVideoStatusResponse -> {
                                            if (null != userVideoStatusResponse) {
                                                Log.d(TAG, "time = " + userVideoStatusResponse.getWatchedTime());
                                                if (userVideoStatusResponse.getWatchedTime() > 0 && userVideoStatusResponse.getWatchedPercentage() < 98) {
                                                    startFromBeginning.setText(appCMSPresenter.getLocalisedStrings().getStartFromBeginningText());
                                                    startFromBeginning.setVisibility(VISIBLE);
                                                    startFromBeginning.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play_from_beginning_24,0,0,0);
                                                    startFromBeginning.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                                    startFromBeginning.setAllCaps(false);


                                                    componentView.setEnabled(true);
                                                    componentView.setFocusable(true);
                                                    componentView.requestFocus();

                                                } else {
                                                    startFromBeginning.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                            }

                        } else {
                            startFromBeginning.setVisibility(View.GONE);
                        }

                    }
                    break;
                    case PAGE_START_WATCHING_BUTTON_KEY:
                        Button startWatchingButton = (Button) componentViewResult.componentView;
                        View componentView = componentViewResult.componentView;
                        startWatchingButton.setId(R.id.btn_start_watching);
                        startWatchingButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.start_watching)));
                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getStartWatchingCTA() != null) {
                            startWatchingButton.setText(moduleAPI.getMetadataMap().getStartWatchingCTA());
                        }
                        if(isContentPlayable(appCMSPresenter,moduleAPI)){
                            startWatchingButton.setVisibility(VISIBLE);
                        }else{
                            startWatchingButton.setVisibility(GONE);
                        }

                        if (appCMSPresenter.isUserLoggedIn()) {
                            if (null != moduleAPI.getContentData() && moduleAPI.getContentData().size() > 0) {
                                String videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                                if (videoId == null) {
                                    videoId = moduleAPI.getContentData().get(0).getGist().getId();
                                }
                                appCMSPresenter.getUserVideoStatus(
                                        videoId,
                                        userVideoStatusResponse -> {
                                            if (null != userVideoStatusResponse) {
                                                Log.d(TAG, "time = " + userVideoStatusResponse.getWatchedTime());
                                                if (userVideoStatusResponse.getWatchedTime() > 0) {
                                                    startWatchingButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.resume_watching)));
                                                    if (moduleAPI.getMetadataMap() != null
                                                            && moduleAPI.getMetadataMap().getResumeWatchingCTA() != null) {
                                                        startWatchingButton.setText(moduleAPI.getMetadataMap().getResumeWatchingCTA());
                                                    }
                                                }
                                                if (userVideoStatusResponse.getWatchedPercentage() >= 98) {
                                                    startWatchingButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.start_watching)));
                                                    if (moduleAPI.getMetadataMap() != null
                                                            && moduleAPI.getMetadataMap().getStartWatchingCTA() != null) {
                                                        startWatchingButton.setText(moduleAPI.getMetadataMap().getStartWatchingCTA());
                                                    }
                                                }
                                            }
                                        });
                            }

                        }

                        long remainingTime = 0;

                        if(moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0){
                            long eventDate = (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate());
                            // eventDate = 1534306500L;

                            //calculate remaining time from event date and current date
                            remainingTime = eventDate - System.currentTimeMillis();

                        }


                        if (remainingTime > 0) {
                            VisualTimer visualTimer = moduleView != null ? moduleView.findViewById(R.id.visualTimer) : null;
                            if (visualTimer != null) {
                                visualTimer.setVisibility(VISIBLE);
                                visualTimer.startTimer(remainingTime);
                                componentView.setEnabled(false);
                                componentView.setFocusable(false);
                            }
                        } else {
                            componentView.setEnabled(true);
                            componentView.setFocusable(true);
                            componentView.requestFocus();
                        }

                        componentView.setOnClickListener(v -> {
                            playVideo(appCMSPresenter, context, component, moduleAPI);
                            componentView.setClickable(false);

                            new Handler().postDelayed(() -> {
                                componentView.setClickable(true);
                            }, 2000);
                        });
                        break;

                    case PAGE_SHOW_START_WATCHING_BUTTON_KEY:
                        //SVFA-3320
                        startWatchingButton = (Button) componentViewResult.componentView;
                        startWatchingButton.setId(R.id.btn_start_watching);
                        if(isContentPlayable(appCMSPresenter,moduleAPI)){
                            startWatchingButton.setVisibility(VISIBLE);
                        }else{
                            startWatchingButton.setVisibility(GONE);
                        }

                        if (null != moduleAPI.getContentData()
                                && moduleAPI.getContentData().size() > 0
                                && null != moduleAPI.getContentData().get(0).getSeason()
                                && moduleAPI.getContentData().get(0).getSeason().size() > 0) {

                            String watchNowButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(
                                    appCMSPresenter.getCurrentActivity().getString(R.string.watch_now));
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getWatchNowCTA() != null) {
                                watchNowButtonText = moduleAPI.getMetadataMap().getWatchNowCTA();
                            }
                            startWatchingButton.setText(watchNowButtonText);

                            componentView = componentViewResult.componentView;
                            componentView.setOnClickListener(v -> {
                                playEpisode(appCMSPresenter, context, component, moduleAPI);
                                componentView.setClickable(false);

                                new Handler().postDelayed(() -> {
                                    componentView.setClickable(true);
                                }, 3000);
                            });
                        } else {
                            componentViewResult.componentView = null;
                        }
                        break;

                    case PAGE_PLAY_EPISODE_BUTTON_KEY:
                        ((Button) componentViewResult.componentView).setText(component.getText());
                        ((Button) componentViewResult.componentView).setFocusable(true);
                        ((Button) componentViewResult.componentView).requestFocus();
                        componentViewResult.componentView.setBackground(
                                Utils.setButtonBackgroundSelectorForNewsTemplate(context,
                                        Utils.getFocusBorderColor(context, appCMSPresenter),
                                        Utils.getFocusColor(context, appCMSPresenter),
                                        component,
                                        appCMSPresenter));
                        Drawable playImageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_play_news);
                        Utils.setImageColorFilter(null, playImageDrawable, appCMSPresenter);
                        playImageDrawable.setBounds(0, 0, playImageDrawable.getIntrinsicWidth(), playImageDrawable.getIntrinsicHeight());
                        ((Button) componentViewResult.componentView).setCompoundDrawables(playImageDrawable, null, null, null);

                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appCMSPresenter.showLoadingDialog(true);
                                appCMSPresenter.launchTVVideoPlayer(moduleAPI.getContentData().get(moduleAPI.getItemPosition()),
                                        0,
                                        null,
                                        0,
                                        null);
                            }
                        });
                        break;

                    case PAGE_BUNDLE_START_WATCHING_BUTTON_KEY:
                        //SVFA-3320
                        if (null != moduleAPI
                                && null != moduleAPI.getContentData()
                                && moduleAPI.getContentData().size() > 0
                                && null != moduleAPI.getContentData().get(0).getGist()
                                && null != moduleAPI.getContentData().get(0).getGist().getBundleList()
                                && moduleAPI.getContentData().get(0).getGist().getBundleList().size() > 0) {
                            componentView = componentViewResult.componentView;
                            componentView.setId(R.id.btn_bundle_start_watching);
                            startWatchingButton = (Button) componentViewResult.componentView;
                            if(isContentPlayable(appCMSPresenter,moduleAPI)){
                                startWatchingButton.setVisibility(VISIBLE);
                            }else{
                                startWatchingButton.setVisibility(GONE);
                            }
                            if (appCMSPresenter.isUserLoggedIn()) {
                                appCMSPresenter.getUserVideoStatus(
                                        moduleAPI.getContentData().get(0).getGist().getId(),
                                        userVideoStatusResponse -> {
                                            if (null != userVideoStatusResponse) {
                                                Log.d(TAG, "time = " + userVideoStatusResponse.getWatchedTime()
                                                );
                                                if (userVideoStatusResponse.getWatchedTime() > 0) {
                                                    startWatchingButton.setText(context.getString(R.string.resume_watching));
                                                }
                                                if (userVideoStatusResponse.getWatchedPercentage() >= 98) {
                                                    startWatchingButton
                                                            .setText(appCMSPresenter.getCurrentActivity().getString(R.string.start_watching));
                                                }
                                            }
                                        });
                            }
                            componentView.setOnClickListener(v -> {
                                playBundle(appCMSPresenter, context, component, moduleAPI);
                                componentView.setClickable(false);

                                new Handler().postDelayed(() -> {
                                    componentView.setClickable(true);
                                }, 3000);
                            });
                        } else {
                            componentViewResult.componentView = null;
                        }
                        break;

                    case PAGE_TRAILER_PLAY_BUTTON_KEY:
                        Button trailerButton = (Button) componentViewResult.componentView;
                        ContentDatum data = moduleAPI.getContentData().get(0);
                        boolean isSeries = Utils.isContentTypeSeries(context,data);

                        if( (isSeries && !isShowTrailerAvailable(moduleAPI))
                          || (!isSeries && !isVideoTrailerAvailable(moduleAPI))){
                            trailerButton.setVisibility(GONE);
                            return;
                        }

                        trailerButton.setBackground(ContextCompat.getDrawable(context, R.drawable.baseline_play_arrow_white_48));
                       // trailerButton.getBackground().setTint(tintColor);
                        trailerButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        trailerButton.setFocusable(true);
                        trailerButton.setTag("PLAY_BUTTON");
                        final boolean isSeries_ = isSeries;
                        trailerButton.setOnClickListener(view -> {
                            playTrailer(moduleAPI, appCMSPresenter, component,isSeries_);
                        });

                        trailerButton.setOnFocusChangeListener((view, hasFocus) -> {
                            if (view != null && ((Button) view).getBackground() != null) {
                                if (hasFocus) {
                                     trailerButton.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                                } else {
                                    trailerButton.getBackground().setTint(ContextCompat.getColor(context, R.color.white));
                                }
                            }
                        });
                        break;

                    case PAGE_VIDEO_PLAY_BUTTON_KEY:
                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().size() > 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                                        moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets() != null) {
                                    VideoAssets videoAssets = moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets();
                                    String videoUrl = videoAssets.getHls();
                                    if (TextUtils.isEmpty(videoUrl)) {
                                        for (int i = 0; i < videoAssets.getMpeg().size() && TextUtils.isEmpty(videoUrl); i++) {
                                            videoUrl = videoAssets.getMpeg().get(i).getUrl();
                                        }
                                    }
                                    if (moduleAPI.getContentData() != null &&
                                            moduleAPI.getContentData().size() > 0 &&
                                            moduleAPI.getContentData().get(0) != null &&
                                            moduleAPI.getContentData().get(0).getGist() != null &&
                                            moduleAPI.getContentData().get(0).getGist().getId() != null &&
                                            moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                                        String[] extraData = new String[4];
                                        extraData[0] = moduleAPI.getContentData().get(0).getGist().getPermalink();
                                        extraData[1] = videoUrl;
                                        extraData[2] = moduleAPI.getContentData().get(0).getGist().getId();
                                        if (moduleAPI.getContentData().get(0).getContentDetails() != null &&
                                                moduleAPI.getContentData().get(0).getContentDetails().getClosedCaptions() != null) {
                                            for (ClosedCaptions closedCaption :
                                                    moduleAPI.getContentData().get(0).getContentDetails().getClosedCaptions()) {
                                                if (closedCaption.getFormat().equalsIgnoreCase("SRT")) {
                                                    extraData[3] = closedCaption.getUrl();
                                                    break;
                                                }
                                            }
                                        }

                                        if (!appCMSPresenter.launchTVButtonSelectedAction(moduleAPI.getContentData().get(0).getGist().getPermalink(),
                                                component.getAction(),
                                                moduleAPI.getContentData().get(0).getGist().getTitle(),
                                                extraData,
                                                moduleAPI.getContentData().get(0),
                                                false,
                                                -1,
                                                null,
                                                null)) {
                                        }
                                    }
                                }
                            }
                        });
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                        componentViewResult.componentView.getBackground().setTint(tintColor);
                        componentViewResult.componentView.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        componentViewResult.componentView.setFocusable(false);
                        componentViewResult.componentView.setTag("PLAY_BUTTON");
                        // componentViewResult.componentView = null;

                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getStartWatchingCTA() != null) {
                            ((Button) componentViewResult.componentView)
                                    .setText(moduleAPI.getMetadataMap().getStartWatchingCTA());
                        }
                        break;

                    case PAGE_SHOW_DETAIL_BUTTON_KEY:

                        if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType() != null &&
                                (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType().equalsIgnoreCase("SERIES")
                                        || moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType().equalsIgnoreCase("SEASON"))) {
                            componentViewResult.componentView.setVisibility(VISIBLE);
                        } else if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().size() > 0 &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getPermalink() != null) {
                            componentViewResult.componentView.setVisibility(VISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(GONE);
                        }
                        componentViewResult.componentView.setBackground(
                                Utils.setButtonBackgroundSelectorForNewsTemplate(context,
                                        Utils.getFocusBorderColor(context, appCMSPresenter),
                                        Utils.getFocusColor(context, appCMSPresenter),
                                        component,
                                        appCMSPresenter));

                        Drawable moreImageDrawable = ContextCompat.getDrawable(context, R.drawable.ic_more_episode);
                        Utils.setImageColorFilter(null, moreImageDrawable, appCMSPresenter);
                        ((Button) componentViewResult.componentView).setCompoundDrawablesWithIntrinsicBounds(moreImageDrawable, null, null, null);
                        ((Button) componentViewResult.componentView).setCompoundDrawablePadding(5);
                        componentViewResult.componentView.setOnClickListener(view -> {
                            String permalink = null;
                            String action = "showDetailPage";
                            if (component.getAction() != null) {
                                action = component.getAction();
                            }
                            String[] extraData = new String[1];

                            if (moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().size() > 0 &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getPermalink() != null) {
                                permalink = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getPermalink();
                                extraData[0] = permalink;
                            } else if (moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getPermalink() != null) {
                                permalink = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getPermalink();
                                extraData[0] = permalink;
                            }

                            if (permalink != null) {
                                appCMSPresenter.launchTVButtonSelectedAction(
                                        permalink,
                                        action,
                                        null,
                                        extraData,
                                        null,
                                        false,
                                        0,
                                        null,
                                        null);
                            }
                        });
                        break;

                    case PAGE_PLAY_KEY:
                    case PAGE_PLAY_IMAGE_KEY:
                        componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.baseline_play_arrow_white_48));
                        componentViewResult.componentView.getBackground().setTint(tintColor);
                        componentViewResult.componentView.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        break;

                    case PAGE_VIDEO_CLOSE_KEY:
                        ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.cancel);
                        ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!appCMSPresenter.launchTVButtonSelectedAction(null,
                                        component.getAction(),
                                        null,
                                        null,
                                        null,
                                        false,
                                        -1,
                                        null,
                                        null)) {
                                }
                            }
                        });
                        break;

                    case PAGE_VIDEO_SHARE_KEY:
                        Drawable shareDrawable = ContextCompat.getDrawable(context, R.drawable.ic_share);
                        componentViewResult.componentView.setBackground(shareDrawable);
                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
                                if (appCMSMain != null &&
                                        moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().size() > 0 &&
                                        moduleAPI.getContentData().get(0) != null &&
                                        moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getTitle() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {
                                    StringBuilder filmUrl = new StringBuilder();
                                    filmUrl.append(appCMSMain.getDomainName());
                                    filmUrl.append(moduleAPI.getContentData().get(0).getGist().getPermalink());
                                    String[] extraData = new String[1];
                                    extraData[0] = filmUrl.toString();
                                }
                            }
                        });
                        break;

                    case PAGE_FORGOTPASSWORD_KEY:

                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getForgotPasswordCtaText() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getForgotPasswordCtaText());
                        }

                        componentViewResult.componentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean focus) {
                                if (null != appCMSPresenter
                                        && null != appCMSPresenter.getCurrentActivity()
                                        && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                    if (focus) {
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((false));
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(false);
                                    } else {
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                                    }
                                }

                            }
                        });
                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String[] extraData = new String[1];
                                extraData[0] = component.getKey();
                                appCMSPresenter.launchTVButtonSelectedAction(
                                        null,
                                        component.getAction(),
                                        null,
                                        extraData,
                                        null,
                                        false,
                                        0,
                                        null,
                                        null);
                            }
                        });
                        break;

                    case PAGE_LINK_YOUR_ACOOUNT_BTN_KEY:
                    case PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY:

                        boolean isTvProviderButtonEnable = false;
                        if (settings != null && settings.getOptions() != null &&
                                settings.getOptions().getSocialLoginSignup() != null &&
                                settings.getOptions().getSocialLoginSignup().size() > 0) {
                            List<LoginSignup> socialLoginSignUpList = settings.getOptions().getSocialLoginSignup();
                            int index = Utils.findIndexOfStringFromList(socialLoginSignUpList, "TVE");
                            if (!(index != -1 && socialLoginSignUpList.get(index).isEnable())) {
                                isTvProviderButtonEnable = true;
                            }
                        }

                        if (componentKey == PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY) {
                            if (isTvProviderButtonEnable
                                    || ((jsonValueKeyMap.get(viewType) == PAGE_ENTITLEMENT_MODULE_KEY
                                    || jsonValueKeyMap.get(viewType) == PAGE_API_SHOWDETAIL_08_MODULE_KEY
                                    || jsonValueKeyMap.get(viewType) == PAGE_API_VIDEO_PLAYER_INFO_06_MODULE_KEY
                                    || jsonValueKeyMap.get(viewType) == PAGE_API_VIDEO_PLAYER_INFO_07_MODULE_KEY
                                    || jsonValueKeyMap.get(viewType) == PAGE_BUNDLEDETAIL_01_MODULE_KEY)
                                            && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0
                                            && !contentTypeChecker.checkPlanExist(moduleAPI.getContentData().get(0).getMonetizationModels(),context.getString(R.string.pricing_model_TVE)))) {
                                componentViewResult.componentView.setVisibility(GONE);
                            }
                            if (settings != null && !settings.isShowActivateDevice()) {
                                if (component.getLayout() != null && component.getLayout().getTv() != null) {
                                    component.getLayout().getTv().setLeftMargin("0");
                                    component.getLayout().getTv().setWidth(String.valueOf(component.getLayout().getTv().getSwipeLeftMargin()));
                                }
                            }
                        } else if (componentKey == PAGE_LINK_YOUR_ACOOUNT_BTN_KEY) {
                            if (isTvProviderButtonEnable) {
                                if (component.getLayout() != null && component.getLayout().getTv() != null) {
                                    component.getLayout().getTv().setWidth(String.valueOf(component.getLayout().getTv().getSwipeLeftMargin()));
                                }
                            }
                            //commenting this in HOICHOI Prod need to activate this flag.
                           if (settings != null && !settings.isShowActivateDevice()) {
                                componentViewResult.componentView.setVisibility(GONE);
                                if(pageView.findViewById(R.id.linear_activate_device_view) != null)
                                    pageView.findViewById(R.id.linear_activate_device_view).setVisibility(GONE);
                            }
                        }

                        componentViewResult.componentView.setId(R.id.btn_activate_device);
                        if (componentKey == PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY) {
                            ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getTveLoginText());
                        } else if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getActivateDeviceCta() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getActivateDeviceCta());
                        }

                        if (componentKey == PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY) {
                            componentViewResult.componentView.setOnFocusChangeListener((view, focus) -> {
                                if (null != appCMSPresenter
                                        && null != appCMSPresenter.getCurrentActivity()
                                        && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                    if (focus) {
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((false));
                                    } else {
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(true);
                                    }
                                }

                            });
                        }

                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String[] extraData = new String[1];
                                extraData[0] = component.getKey();
                                if (appCMSPresenter.getLaunchType() != AppCMSPresenter.LaunchType.LOGIN_FROM_MINI_PLAYER
                                        && appCMSPresenter.getLaunchType() != (AppCMSPresenter.LaunchType.NAVIGATE_TO_HOME_FROM_LOGIN_DIALOG)
                                        && appCMSPresenter.getLaunchType() != (AppCMSPresenter.LaunchType.AUTOPLAY_SCREEN)) {
                                    appCMSPresenter.setLaunchType(isFromLoginDialog ? AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP : AppCMSPresenter.LaunchType.HOME);
                                }

                                appCMSPresenter.launchTVButtonSelectedAction(
                                        null,
                                        component.getAction(),
                                        null,
                                        extraData,
                                        null,
                                        false,
                                        0,
                                        null,
                                        null);
                            }
                        });

                        break;

                    case CANCEL_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.dialog_cancel_button);
                        break;

                    case REQUEST_NEW_CODE:
                        componentViewResult.componentView.setId(R.id.request_new_code);
                        break;

                    case RESET_PASSWORD_CANCEL_BUTTON_KEY:
                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getCancelCta() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getCancelCta());
                        }
                        componentViewResult.componentView.setId(R.id.reset_password_cancel_button);
                        break;
                    case PAGE_DOWNLOAD_QUALITY_CANCEL_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.autoplay_cancel_countdown_button);
                        componentViewResult.componentView.requestFocus();
                        break;
                    case RESET_PASSWORD_CONTINUE_BUTTON_KEY:
                        if (moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getContinueCta() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getContinueCta());
                        }
                        componentViewResult.componentView.setId(R.id.reset_password_continue_button);
                        break;

                    case PAGE_LOGIN_BUTTON_KEY:
                    case PAGE_SIGNUP_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.btn_login);
                        appCMSPresenter.setModuleApi(moduleAPI);
                        if ("login".equalsIgnoreCase(component.getAction())
                                && moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getLoginCta() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getLoginCta());
                        } else if ("signup".equalsIgnoreCase(component.getAction())
                                && moduleAPI.getMetadataMap() != null
                                && moduleAPI.getMetadataMap().getSignUpCtaText() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getSignUpCtaText());
                        }

                        componentViewResult.componentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean focus) {
                                if (focus && null != appCMSPresenter
                                        && null != appCMSPresenter.getCurrentActivity()
                                        && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                    ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((true));
                                }

                            }
                        });

                        componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (pageView.getChildrenContainer().getChildAt(0) instanceof TVModuleView) {
                                    TVModuleView tvModuleView = (TVModuleView) pageView.getChildrenContainer().getChildAt(0);
                                    String emailId = ((EditText) tvModuleView.findViewById(R.id.email_edit_box)).getEditableText().toString();
                                    String password = ((EditText) tvModuleView.findViewById(R.id.password_edit_box)).getEditableText().toString();
                                    CheckBox emailConsentCheckbox = (CheckBox) tvModuleView.findViewById(R.id.emailConsentCheckbox);
                                    boolean emailConsent = false;
                                    if (emailConsentCheckbox != null) {
                                        emailConsent = emailConsentCheckbox.isChecked();
                                    }
                                    //Log.d(TAG, "emailid = " + emailId + "password = " + password);
                                    String errorDialogHeader = appCMSPresenter.getLocalisedStrings().getSignInText();

                                    if (component.getAction().trim().equalsIgnoreCase("signup")) {
                                        errorDialogHeader = appCMSPresenter.getLocalisedStrings().getSignUpText();
                                    }
                                    if (emailId.length() == 0) {
                                        String errorDialogMessage = appCMSPresenter.getLocalisedStrings().getEmptyEmailValidationText();
                                        appCMSPresenter.openTVErrorDialog(
                                                errorDialogMessage,
                                                errorDialogHeader, false);
                                        return;
                                    }
                                    if (!com.viewlift.Utils.isEmailValid(emailId)) {
                                        String errorDialogMessage = appCMSPresenter.getLocalisedStrings().getEmailFormatValidationMsg();
                                        appCMSPresenter.openTVErrorDialog(
                                                errorDialogMessage,
                                                errorDialogHeader, false);
                                        return;
                                    }
                                    if (password.contains(" ")) {
                                        String errorDialogMessage = appCMSPresenter.getLocalisedStrings().getPasswordFormatValidationText();
                                        appCMSPresenter.openTVErrorDialog(
                                                errorDialogMessage,
                                                errorDialogHeader, false);
                                        return;
                                    }
                                    if (password.length() == 0) {
                                        String errorMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.blank_password_error_msg));
                                            errorMessage = appCMSPresenter.getLocalisedStrings().getEmptyPasswordValidationText();
                                        appCMSPresenter.openTVErrorDialog(errorMessage,
                                                errorDialogHeader, false);
                                        return;
                                    }

                                    CheckBox ageConsentCheckbox = (CheckBox) tvModuleView.findViewById(R.id.ageConsentCheckbox);
                                    if (ageConsentCheckbox != null) {
                                        boolean isAgeChecked = ageConsentCheckbox.isChecked();
                                        if (!isAgeChecked) {
                                            ((TextView) moduleView.findViewById(R.id.ageConsentError)).setVisibility(VISIBLE);
                                            return;
                                        }
                                        ((TextView) moduleView.findViewById(R.id.ageConsentError)).setVisibility(View.INVISIBLE);
                                    }


                                    String[] authData = new String[4];
                                    authData[0] = emailId;
                                    authData[1] = password;
                                    authData[2] = String.valueOf(emailConsent);
                                    if (emailConsentCheckbox != null && emailConsentCheckbox.getVisibility() == VISIBLE)
                                        authData[3] = "Y";
                                    else
                                        authData[3] = "N";
                                    if (appCMSPresenter.getLaunchType() != (AppCMSPresenter.LaunchType.NAVIGATE_TO_HOME_FROM_LOGIN_DIALOG)
                                            && appCMSPresenter.getLaunchType() != (AppCMSPresenter.LaunchType.LOGIN_FROM_MINI_PLAYER)
                                            && appCMSPresenter.getLaunchType() != (AppCMSPresenter.LaunchType.AUTOPLAY_SCREEN)) {
                                        appCMSPresenter.setLaunchType(isFromLoginDialog ? AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP : AppCMSPresenter.LaunchType.HOME);
                                    }


                                    appCMSPresenter.launchTVButtonSelectedAction(null,
                                            component.getAction(),
                                            null,
                                            authData,
                                            null,
                                            false,
                                            0,
                                            null, null);
                                }
                            }
                        });
                        break;
                    case PAGE_SETTING_LOGOUT_BUTTON_KEY:
                        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLogoutCta() != null) {
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getLogoutCta());
                        } else if (component.getText() != null) {
                            ((Button) componentViewResult.componentView).setText(component.getText());
                        }

                        componentViewResult.componentView.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((focus));
                            }

                        });
                        componentViewResult.componentView.setOnClickListener(v -> {
                            appCMSPresenter.showLoader();
                            appCMSPresenter.logoutTV();
                        });
                        break;
                    case PAGE_REDEEM_BUTTON_KEY:

                        componentViewResult.componentView.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((focus));
                            }

                        });

                        ((Button) componentViewResult.componentView).setId(R.id.redemption_button_tv);
//                        ((Button) componentViewResult.componentView).setTextColor(ContextCompat.getColor(context, android.R.color.black));
//                        ((Button) componentViewResult.componentView).setBackgroundColor(Color.GRAY);
                        ((Button) componentViewResult.componentView).setFocusable(true);

                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();

                            appCMSPresenter.launchTVButtonSelectedAction(
                                    null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null,
                                    null);

                        });
                        break;

                    case PAGE_REMOVEALL_KEY:

                        if (viewType.contains("AC History")) {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getClearAllHistoryButton() != null) {
                                ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getClearAllHistoryButton());
                            }
                        } else if (viewType.contains("AC Watchlist")) {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getClearWatchlist() != null) {
                                ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getClearWatchlist());
                            }
                        }

                        if (moduleAPI.getContentData() != null
                                && moduleAPI.getContentData().size() > 0) {
                            Button buttonRemoveAll = (Button) componentViewResult.componentView;
                            buttonRemoveAll.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    if (null != appCMSPresenter
                                            && null != appCMSPresenter.getCurrentActivity()
                                            && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                                        } else {
                                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(true);
                                        }
                                    }
                                }
                            });
                            buttonRemoveAll.setId(R.id.appcms_removeall);
                            buttonRemoveAll.setOnClickListener(v -> {
                                OnInternalEvent onInternalEvent = componentViewResult.onInternalEvent;
                                String yesText = appCMSPresenter.getLocalisedStrings().getOkText();
                                if (viewType.contains("AC History")) {
                                    String clearHistoryMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.clear_history_message));
                                    if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getClearHistoryAlertTitle() != null) {
                                        clearHistoryMessage = moduleAPI.getMetadataMap().getClearHistoryAlertTitle();
                                    }
                                    ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                            context,
                                            appCMSPresenter,
                                            context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                            null,
                                            clearHistoryMessage,
                                            yesText,
                                            appCMSPresenter.getLocalisedStrings().getCancelCta(),
                                            22.5f

                                    );
                                    newFragment.setOnPositiveButtonClicked(s -> {
                                                appCMSPresenter.showLoader();
                                                appCMSPresenter.makeClearHistoryRequest(
                                                        appCMSDeleteHistoryResult -> {
                                                            appCMSPresenter.stopLoader();
                                                            onInternalEvent.sendEvent(null);
                                                            buttonRemoveAll.setFocusable(false);
                                                            buttonRemoveAll.setVisibility(View.INVISIBLE);

                                                        }
                                                );
                                            }
                                    );
                                } else if (viewType.contains("AC Watchlist")) {
                                    String clearWatchlistMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.clear_watchlist_message));
                                    if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getClearWatchlistAlertTitle() != null) {
                                        clearWatchlistMessage = moduleAPI.getMetadataMap().getClearWatchlistAlertTitle();
                                    }
                                    ClearDialogFragment newFragment1 = Utils.getClearDialogFragment(
                                            context,
                                            appCMSPresenter,
                                            context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                            null,
                                            clearWatchlistMessage,
                                            yesText,
                                            appCMSPresenter.getLocalisedStrings().getCancelCta(),
                                              22.5f
                                    );
                                    newFragment1.setOnPositiveButtonClicked(s ->
                                            appCMSPresenter.makeClearWatchlistRequest(
                                                    appCMSAddToWatchlistResult -> {
                                                        onInternalEvent.sendEvent(null);
                                                        buttonRemoveAll.setFocusable(false);
                                                        buttonRemoveAll.setVisibility(View.INVISIBLE);
                                                    }
                                            )
                                    );
                                }
                            });
                        } else {
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case PAGE_SETTINGS_MANAGE_RECOMMEND_BUTTON_KEY:
                        ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getManagePersonalizationText());

                        if (appCMSPresenter.isPersonalizationEnabled()){
                            if (appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                                if (appCMSPresenter.isUserSubscribed()) {
                                    componentViewResult.componentView.setOnClickListener(v -> {
                                        appCMSPresenter.setIsFromSetting(true);
                                        appCMSPresenter.showLoader();
                                        String currentUserId = appCMSPresenter.getLoggedInUser() != null ? appCMSPresenter.getLoggedInUser() : "guest-user-id";
                                        appCMSPresenter.getUserRecommendedGenres(currentUserId, s -> {
                                            appCMSPresenter.setSelectedGenreString(s);
                                            appCMSPresenter.stopLoader();
                                            appCMSPresenter.openRecommendationDialog(true);
                                        },true, true);
                                    });
                                } else {
                                    ((Button) componentViewResult.componentView).setVisibility(View.GONE);
                                }
                            } else {
                                componentViewResult.componentView.setOnClickListener(v -> {
                                    appCMSPresenter.setIsFromSetting(true);
                                    appCMSPresenter.showLoader();
                                 //   appCMSPresenter.openRecommendationDialog(true);
                                String currentUserId = appCMSPresenter.getLoggedInUser() != null ? appCMSPresenter.getLoggedInUser() : "guest-user-id";
                                    appCMSPresenter.getUserRecommendedGenres(currentUserId, s -> {
                                        appCMSPresenter.setSelectedGenreString(s);
                                        appCMSPresenter.stopLoader();
                                        appCMSPresenter.openRecommendationDialog(true);
                                    },true, true);
                                });
                            }

                        } else {
                            ((Button) componentViewResult.componentView).setVisibility(GONE);
                        }
                        break;

                    case BTN_SKIP_PLAN:
                        Button skipBtn = (Button) componentViewResult.componentView;
                        if (appCMSPresenter.isUserLoggedIn()) {
                            skipBtn.setVisibility(VISIBLE);
                        } else {
                            skipBtn.setVisibility(View.INVISIBLE);
                        }

                        skipBtn.setOnClickListener((view) -> {
                            appCMSPresenter.navigateToHomePage(true);
                        });
                        break;

                    case PAGE_PLAN_SUBSCRIBE_NOW_BUTTON_KEY:
                        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSubscribeNowCta() != null)
                            ((Button) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getSubscribeNowCta());
                        else
                            ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getSubscribeNowText());
                        componentViewResult.componentView.setOnClickListener((view) -> {
                            if(appCMSPresenter.isUserSubscribed()){
                                String platform = appCMSPresenter.getActiveSubscriptionPlatform();
                                String errorMessage = getErrorMessage(context, appCMSPresenter, platform);
                                appCMSPresenter.openTVErrorDialog(errorMessage,null,false);
                            }else if(!appCMSPresenter.isFireTVSubscriptionEnabled()){
                                appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
                            }else{
                                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData() .size() > 0 &&
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null) {
                                    ContentDatum contentDatum = moduleAPI.getContentData().get(moduleAPI.getItemPosition());
                                    ((AppCmsBaseActivity) context).initiateAmazonPurchase(contentDatum.getIdentifier(), contentDatum);
                                }
                            }
                        });
                        break;

                    case PAGE_PLAN_BECOME_MEMBER_BUTTON_KEY:
                        componentViewResult.componentView.setId(R.id.btn_become_a_member);
                        // Check for Android TV, don't open the plans page for Android TV, ask the user to purchase subscription from website.
                        if (!com.viewlift.Utils.isFireTVDevice(context)) {
                            componentViewResult.componentView.setOnClickListener((view) -> {
                                appCMSPresenter.openTVErrorDialog(appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText(), "", false);

                            });
                        } else {
                            if (!appCMSPresenter.isUserSubscribed() && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0
                                    && (contentTypeChecker.checkPlanExist(moduleAPI.getContentData().get(0).getMonetizationModels(), context.getString(R.string.pricing_model_SVOD))
                                    || (moduleAPI.getContentData().get(0).getSubscriptionPlans() != null
                                    && contentTypeChecker.isSvodContentExist(moduleAPI.getContentData().get(0).getSubscriptionPlans())))
                                    && !isContentPlayable(appCMSPresenter, moduleAPI)) {
                                (componentViewResult.componentView).setVisibility(VISIBLE);
                                ((Button) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getBecomeMemberText());

                                componentViewResult.componentView.setOnClickListener((view) -> {
                                    ContentDatum subscriptionPlan = moduleAPI.getContentData().get(0);
                                    if (subscriptionPlan != null && subscriptionPlan.getGist() != null
                                            && subscriptionPlan.getGist().getContentType() != null
                                            && (subscriptionPlan.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_series))
                                            || subscriptionPlan.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_season))
                                            || subscriptionPlan.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_show)))
                                            && subscriptionPlan.getContentPlans() != null && subscriptionPlan.getContentPlans().size() > 0
                                            && contentTypeChecker.contentPlansId(subscriptionPlan.getContentPlans(), context.getString(R.string.pricing_model_SVOD)) != null) {
                                        String contentPlans = TextUtils.join(",", contentTypeChecker.contentPlansId(subscriptionPlan.getContentPlans(), context.getString(R.string.pricing_model_SVOD)));
                                        appCMSPresenter.fetchSubscriptionPlansById(contentPlans, contentData -> appCMSPresenter.navigateToContentSubscription(contentData), true);
                                    } else if (subscriptionPlan != null
                                            && subscriptionPlan.getSubscriptionPlans() != null
                                            && contentTypeChecker.isSvodContentExist(subscriptionPlan.getSubscriptionPlans())) {
                                        appCMSPresenter.navigateToContentSubscription(subscriptionPlan.getSubscriptionPlans());
                                    } else {
                                        //PrimaryCta primaryCta = appCMSPresenter.getNavigation().getSettings().getPrimaryCta();
                                        MetaPage viewPlanPage = appCMSPresenter.getSubscriptionPage();
                                        appCMSPresenter.navigateToTVPage(viewPlanPage.getPageId(),
                                                viewPlanPage.getPageName(),
                                                null,
                                                false,
                                                Uri.EMPTY,
                                                false,
                                                true, true, true, false, false);
                                    }
                                    appCMSPresenter.setViewPlanPageOpenFromADialog(true);
                                });
                            } else {
                                (componentViewResult.componentView).setVisibility(GONE);
                            }
                        }
                        break;

                    case PAGE_PLAN_BUY_BUTTON_KEY:
                        Button buyOptBtn = (Button) componentViewResult.componentView;
                        componentViewResult.componentView.setId(R.id.btn_buy_option);
                        if(!appCMSPresenter.isUserLoggedIn()){
                            buyOptBtn.setNextFocusUpId(R.id.login_text);
                        }
                        ContentDatum buyContentDatum = ( moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) ?
                                moduleAPI.getContentData().get(0) : null;

                        if(isTVODButtonEnabled(context, appCMSPresenter, moduleAPI,buyContentDatum,false)){
                            buyOptBtn.setVisibility(VISIBLE);
                            appCMSPresenter.setModuleApi(moduleAPI);
                            updateTVODButtonText(context, appCMSPresenter,buyContentDatum,buyOptBtn,false);
                        }else{
                            buyOptBtn.setVisibility(GONE);
                        }

                        buyOptBtn.setOnClickListener(v-> {
                            appCMSPresenter.openTVErrorDialog(
                                    appCMSPresenter.getLocalisedStrings().getTVODContentError(appCMSPresenter.getAppCMSMain().getDomainName()),
                                    null, false);

                            // TODO: 24/2/21 Anas, removing TVOD purchase on Fire TV for MovieSpree Submission
                            /*appCMSPresenter.setModuleApi(moduleAPI);
                            if (Utils.isContentTypeSeries(context, buyContentDatum)) {
                                appCMSPresenter.openAccountDetailsEditInfoDialog(context.getString(R.string.buy_options), null,
                                        context.getString(R.string.buy_options), AppCMSPresenter.TVOD_PURCHASE_DIALOG);
                            }else if (!appCMSPresenter.isUserLoggedIn()) {
                                openLoginDialog(appCMSPresenter);
                            }else if(!appCMSPresenter.isFireTVSubscriptionEnabled()){
                                appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
                            }else {
                                if (buyContentDatum.getContentPlans() != null
                                        && buyContentDatum.getContentPlans().size() > 0
                                        && (Utils.isContentTypeVideo(context, buyContentDatum) || Utils.isContentTypeBundle(context, buyContentDatum))) {
                                    appCMSPresenter.fetchSubscriptionPlansById(contentTypeChecker.contentPlansId(buyContentDatum.getContentPlans(), context.getString(R.string.pricing_model_TVOD)).get(0), contentData -> {
                                        if(contentData != null && contentData.size() > 0) {
                                            TvodPurchaseData tvodPurchaseData = new TvodPurchaseData(null,
                                                    null,
                                                    buyContentDatum.getGist().getId(),
                                                    Utils.isContentTypeSeries(context, buyContentDatum),
                                                    false,
                                                    Utils.isContentTypeBundle(context, buyContentDatum), false, contentData.get(0), buyContentDatum.getGist().getId() , buyContentDatum.getGist().getTitle());
                                            appCMSPresenter.setContentToPurchase(tvodPurchaseData);
                                            appCMSPresenter.initiateTvodPurchase();
                                        }
                                    },true);
                                }else{
                                    ContentDatum videoPlan = contentTypeChecker.tvodPlan(Utils.isContentTypeBundle(context, buyContentDatum) ? buyContentDatum.getBundlePlans() : buyContentDatum.getSubscriptionPlans());
                                    TvodPurchaseData tvodPurchaseData = new TvodPurchaseData(null,
                                            null,
                                            buyContentDatum.getGist().getId(),
                                            Utils.isContentTypeSeries(context, buyContentDatum),
                                            false,
                                            Utils.isContentTypeBundle(context, buyContentDatum), false, videoPlan,buyContentDatum.getGist().getId(), buyContentDatum.getGist().getTitle());
                                    appCMSPresenter.setContentToPurchase(tvodPurchaseData);
                                    appCMSPresenter.initiateTvodPurchase();
                                }
                            }*/
                        });

                        break;

                    case PAGE_SETTINGS_MANAGE_SUBSCRIPTION_BUTTON_KEY:
                        Button button = (Button) componentViewResult.componentView;
                        button.setVisibility(View.INVISIBLE);
                        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getManageSubscriptionCta() != null) {
                            button.setText(moduleAPI.getMetadataMap().getManageSubscriptionCta());
                        }
                        if (appCMSPresenter.isAppSVOD()) {
                            if (!appCMSPresenter.isFireTVSubscriptionEnabled()) {
                                button.setVisibility(View.VISIBLE);
                                componentViewResult.componentView.setOnClickListener(v -> {
                                    if (appCMSPresenter.getActiveSubscriptionPlatform() == null) {
                                        appCMSPresenter.getSubscriptionData(
                                                appCMSUserSubscriptionPlanResult -> {
                                                    String platform;
                                                    String errorMsg = "";
                                                    if (appCMSUserSubscriptionPlanResult != null
                                                            && appCMSUserSubscriptionPlanResult.getSubscriptionInfo() != null
                                                            && appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform() != null) {
                                                        platform = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform();
                                                        errorMsg = getErrorMessage(context, appCMSPresenter, platform);
                                                        appCMSPresenter.setActiveSubscriptionPlatform(platform);
                                                    } else {
                                                        errorMsg = appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText();
                                                    }
                                                    String errorDialogHeader = appCMSPresenter.getLocalisedStrings().getSubscriptionMsgHeaderText();
                                                    appCMSPresenter.openTVErrorDialog(errorMsg, errorDialogHeader, false);
                                                }, false);
                                    } else {
                                        String platform = appCMSPresenter.getActiveSubscriptionPlatform();
                                        String errorMsg = getErrorMessage(context, appCMSPresenter, platform);
                                        String errorDialogHeader = appCMSPresenter.getLocalisedStrings().getSubscriptionMsgHeaderText();
                                        appCMSPresenter.openTVErrorDialog(errorMsg, errorDialogHeader, false);
                                    }
                                });
                            } else {
                                //commented because in case of Suspended subscription status we have to show Manage Subscription button.
                                /* if (appCMSPresenter.getActiveSubscriptionPlatform() == null)*/ {
                                    appCMSPresenter.getSubscriptionData(
                                            appCMSUserSubscriptionPlanResult -> {
                                                button.setVisibility(VISIBLE);
                                                String platform;
                                                if (appCMSUserSubscriptionPlanResult != null
                                                        && appCMSUserSubscriptionPlanResult.getSubscriptionInfo() != null
                                                        && ( appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus().equalsIgnoreCase("COMPLETED")
                                                        || appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus().equalsIgnoreCase("DEFERRED_CANCELLATION"))) {
                                                    platform = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform();
                                                    appCMSPresenter.setActiveSubscriptionPlatform(platform);
//                                                    button.setVisibility(View.INVISIBLE);
                                                    button.setOnClickListener(v -> {
                                                        String errorMsg = getErrorMessage(context, appCMSPresenter, platform);
                                                        String errorDialogHeader = appCMSPresenter.getLocalisedStrings().getSubscriptionMsgHeaderText();
                                                        appCMSPresenter.openTVErrorDialog(errorMsg, errorDialogHeader, false);

                                                    });
                                                } else {
                                                    button.setText(appCMSPresenter.getLocalisedStrings().getSubscribeNowText());
                                                    button.setOnClickListener(v -> {
                                                        String pageId = appCMSPresenter.getSubscriptionPage().getPageId();
                                                        String title = context.getResources().getString(R.string.view_plans_label);

                                                        appCMSPresenter.navigateToTVPage(
                                                                pageId,
                                                                title,
                                                                null,
                                                                false,
                                                                Uri.EMPTY,
                                                                false,
                                                                true,
                                                                false,
                                                                true,
                                                                false, false);


                                                    });
                                                }
                                            }, false);
                                } /*else {
                                    button.setVisibility(View.INVISIBLE);
                                }*/


                            }
                        } else {
                            componentViewResult.componentView.setVisibility(View.INVISIBLE);
                        }

                        button.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((focus));
                            }

                        });

                        break;
                    case MANAGE_LANGUAGE_KEY:
                        componentViewResult.componentView.setOnClickListener(v -> {
                            String[] extraData = new String[1];
                            extraData[0] = component.getKey();
                            appCMSPresenter.launchTVButtonSelectedAction(
                                    null,
                                    component.getAction(),
                                    null,
                                    extraData,
                                    null,
                                    false,
                                    0,
                                    null,
                                    null);
                        });

                        break;
                    case PAGE_SETTINGS_PARENTAL_CONTROLS_KEY:
                        Button parentalBtn = (Button)componentViewResult.componentView;
                        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getParentalControlHeader() != null) {
                            parentalBtn.setText(moduleAPI.getMetadataMap().getParentalControlHeader());
                        }

                        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                && appCMSPresenter.getAppCMSMain().getFeatures().isParentalControlEnable()) {
                            parentalBtn.setVisibility(View.VISIBLE);
                        } else {
                            parentalBtn.setVisibility(GONE);
                        }

                        parentalBtn.setOnClickListener(v -> {
                            String errorDialogHeader = component.getText();
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getParentalControlHeader() != null) {
                                parentalBtn.setText(moduleAPI.getMetadataMap().getParentalControlHeader());
                                errorDialogHeader = moduleAPI.getMetadataMap().getParentalControlHeader();
                            }

                            String errorMsg = context.getString(R.string.manage_parental_from_mobile_or_web);
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getManageParentalFromMobileOrWeb() != null) {
                                errorMsg = moduleAPI.getMetadataMap().getManageParentalFromMobileOrWeb();
                            }
                            appCMSPresenter.openTVErrorDialog(errorMsg, errorDialogHeader, false);
                        });
                    break;
                    case RENT_OPTION_BTN_KEY:
                        Button rentOptBtn = (Button) componentViewResult.componentView;
                        componentViewResult.componentView.setId(R.id.btn_rent_option);
                        if(!appCMSPresenter.isUserLoggedIn()){
                            rentOptBtn.setNextFocusUpId(R.id.login_text);
                        }
                        ContentDatum contentDatum = ( moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0) ?
                                moduleAPI.getContentData().get(0) : null;
                        if(isTVODButtonEnabled(context, appCMSPresenter, moduleAPI,contentDatum,true)){
                            rentOptBtn.setVisibility(VISIBLE);
                            appCMSPresenter.setModuleApi(moduleAPI);
                            updateTVODButtonText(context, appCMSPresenter,contentDatum,rentOptBtn,true);
                        }else{
                            rentOptBtn.setVisibility(GONE);
                        }

                        rentOptBtn.setOnClickListener(v-> {
                            appCMSPresenter.openTVErrorDialog(
                                    appCMSPresenter.getLocalisedStrings().getTVODContentError(appCMSPresenter.getAppCMSMain().getDomainName()),
                                    null, false);
                            // TODO: 24/2/21 Anas, removing TVOD purchase on Fire TV for MovieSpree Submission
                            /*appCMSPresenter.setModuleApi(moduleAPI);
                            if (Utils.isContentTypeSeries(context, contentDatum)) {
                                appCMSPresenter.openAccountDetailsEditInfoDialog(context.getString(R.string.rent_options),null,
                                        context.getString(R.string.rent_options), AppCMSPresenter.TVOD_PURCHASE_DIALOG);
                            }else if (!appCMSPresenter.isUserLoggedIn()) {
                                openLoginDialog(appCMSPresenter);
                            }else if(!appCMSPresenter.isFireTVSubscriptionEnabled()){
                                appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
                            }else {
                                if (contentDatum.getContentPlans() != null
                                        && contentDatum.getContentPlans().size() > 0
                                        && (Utils.isContentTypeVideo(context, contentDatum) || Utils.isContentTypeBundle(context, contentDatum))) {
                                    appCMSPresenter.fetchSubscriptionPlansById(contentTypeChecker.contentPlansId(contentDatum.getContentPlans(), context.getString(R.string.pricing_model_TVOD)).get(0), contentData -> {
                                        if(contentData != null && contentData.size() > 0) {
                                            TvodPurchaseData tvodPurchaseData = new TvodPurchaseData(null,
                                                    null,
                                                    contentDatum.getGist().getId(),
                                                    Utils.isContentTypeSeries(context, contentDatum),
                                                    false,
                                                    Utils.isContentTypeBundle(context, contentDatum), true, contentData.get(0),contentDatum.getGist().getId(), contentDatum.getGist().getTitle());
                                            appCMSPresenter.setContentToPurchase(tvodPurchaseData);
                                            appCMSPresenter.initiateTvodPurchase();
                                        }
                                    }, true);
                                }else{
                                    try {
                                        ContentDatum videoPlan = contentTypeChecker.tvodPlan(Utils.isContentTypeBundle(context, contentDatum)
                                                ? contentDatum.getBundlePlans() : contentDatum.getSubscriptionPlans());
                                        TvodPurchaseData tvodPurchaseData = new TvodPurchaseData(null,
                                                null,
                                                contentDatum.getGist().getId(),
                                                Utils.isContentTypeSeries(context, contentDatum),
                                                false,
                                                Utils.isContentTypeBundle(context, contentDatum), true, videoPlan,contentDatum.getGist().getId(), contentDatum.getGist().getTitle());
                                        appCMSPresenter.setContentToPurchase(tvodPurchaseData);
                                        appCMSPresenter.initiateTvodPurchase();
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }*/
                        });
                    break;
                }
                if (componentViewResult.componentView != null) {
                    ((TextView) componentViewResult.componentView).setTypeface(
                            Utils.getTypeFace(context,
                                    appCMSPresenter,
                                    component));
                }
                break;

            case PAGE_LABEL_KEY:
            case PAGE_TEXTVIEW_KEY:
                if (componentKey == PAGE_API_DESCRIPTION) {
                    componentViewResult.componentView = new ScrollView(context);
                } else if (componentKey == WEATHER_TIME_LABEL) {
                    componentViewResult.componentView = new TextClock(context);
                } else {
                    componentViewResult.componentView = new TextView(context);
                    (componentViewResult.componentView).setScrollBarStyle(context.getResources().getColor(android.R.color.holo_red_dark));
                    if (component.getId() != null) {
                        (componentViewResult.componentView).setId(IdUtils.getID(component.getId()));
                    }

                    if(component.getId() != null && component.getId().equalsIgnoreCase("watchtrailerlabel")){
                        ContentDatum data = moduleAPI.getContentData().get(0);
                        boolean isSeries = Utils.isContentTypeSeries(context,data);

                        if( (isSeries && !isShowTrailerAvailable(moduleAPI))
                                || (!isSeries && !isVideoTrailerAvailable(moduleAPI))){
                            TextView textView1 = (TextView)componentViewResult.componentView;
                            if(textView1 != null) {
                                textView1.setVisibility(GONE);
                            }
                        }
                    }

                    if (component.getNumberOfLines() != 0) {
                        ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                        ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                    }
                }

                int textColor = ContextCompat.getColor(context, R.color.colorAccent);
                String txtColor = appCMSPresenter.getAppTextColor();
                if (null != txtColor) {
                    textColor = Color.parseColor(txtColor);
                }/*else if (!TextUtils.isEmpty(component.getTextColor())) {
                    textColor = Color.parseColor(getColor(context, component.getTextColor()));
                } else if (component.getStyles() != null) {
                    if (!TextUtils.isEmpty(component.getStyles().getColor())) {
                        textColor = Color.parseColor(getColor(context, component.getStyles().getColor()));
                    } else if (!TextUtils.isEmpty(component.getStyles().getTextColor())) {
                        textColor =
                                Color.parseColor(getColor(context, component.getStyles().getTextColor()));
                    }
                }*/
                if (componentKey != PAGE_API_DESCRIPTION)
                    ((TextView) componentViewResult.componentView).setTextColor(textColor);
                if (!gridElement) {
                    switch (componentKey) {
                        case PAGE_LINK_YOUR_ACCOUNT_TEXT_KEY: {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLinkDeviceToAccountLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getLinkDeviceToAccountLabel());
                            } else if (component.getText() != null) {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                        }
                        break;
                        case PAGE_SELECT_YOUR_PLAN_TEXT_KEY: {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSelectPlanLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getSelectPlanLabel());
                            } else if (component.getText() != null) {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                        }
                        break;
                        case PAGE_SUBSCRIPTION_METADATA_TEXT_KEY: {
                            StringBuffer stringBuffer = new StringBuffer();
                            if (moduleAPI.getMetadataMap() != null) {
                                if (!TextUtils.isEmpty(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata1())) {
                                    stringBuffer.append(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata1());
                                }
                                if (!TextUtils.isEmpty(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata2())) {
                                    stringBuffer.append("\n");
                                    stringBuffer.append(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata2());
                                }
                                if (!TextUtils.isEmpty(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata3())) {
                                    stringBuffer.append("\n");
                                    stringBuffer.append(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata3());
                                }
                                if (!TextUtils.isEmpty(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata4())) {
                                    stringBuffer.append("\n");
                                    stringBuffer.append(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata4());
                                }
                                if (!TextUtils.isEmpty(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata5())) {
                                    stringBuffer.append("\n");
                                    stringBuffer.append(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata5());
                                }
                                if (!TextUtils.isEmpty(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata6())) {
                                    stringBuffer.append("\n");
                                    stringBuffer.append(moduleAPI.getMetadataMap().getKAmazonSubscriptionMetadata6());
                                }
                            }
                            if (!TextUtils.isEmpty(stringBuffer)) {
                                ((TextView) componentViewResult.componentView).setText(stringBuffer);
                            } else if (component.getText() != null) {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                        }
                        break;
                        case PAGE_DONT_HAVE_AN_ACCOUNT_TEXT_KEY: {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getNoAccountLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getNoAccountLabel());
                            } else if (component.getText() != null) {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                        }
                        break;
                        case PAGE_FORGOT_PASSWORD_DETAIL_LABEL_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEnterEmailToResetPasswordLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getEnterEmailToResetPasswordLabel());
                            } else if (component.getText() != null) {
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()));
                            }

                         break;
                        case PAGE_FORGOT_PASSWORD_LABEL_KEY:
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getForgotPasswordCtaText() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getForgotPasswordCtaText());
                            }
                            ((TextView) componentViewResult.componentView).setFocusable(true);
                            ((TextView) componentViewResult.componentView).setTextColor(Utils.setPageLinkButtonTextColor(Utils.getTextColor(context,appCMSPresenter),Utils.getFocusColor(context,appCMSPresenter)));
                            componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String[] extraData = new String[1];
                                    extraData[0] = component.getKey();
                                    appCMSPresenter.launchTVButtonSelectedAction(
                                            null,
                                            component.getAction(),
                                            null,
                                            extraData,
                                            null,
                                            false,
                                            0,
                                            null,
                                            null);
                                }
                            });

                            break;
                        case PAGE_OR_SEPARATOR_LABEL_KEY: {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getOrSeparator() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getOrSeparator().toUpperCase());
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(getColor(
                                        context,
                                        Utils.getSecondaryTextColor(context,appCMSPresenter))));
                            }
                        }
                        break;
                        case PAGE_API_TITLE:
                            if (!TextUtils.isEmpty(moduleAPI.getTitle())) {
                                String title = moduleAPI.getTitle();
                                if (jsonValueKeyMap.get(viewType) == PAGE_EXPANDED_VIEW_MODULE_KEY)
                                    title = title.toUpperCase();

                                ((TextView) componentViewResult.componentView).setText(title);
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                ((TextView) componentViewResult.componentView).setText(title != null ? title.toUpperCase() : component.getText().toUpperCase());

                                // ((TextView) componentViewResult.componentView).setText(component.getText().toUpperCase());
                            }

                            ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                            //((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));

                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TITLE");
                            break;

                        /*case PAGE_CATEGORY_TRAY_TITLE_KEY:
                            String string = appCMSPresenter.getLanguageResourcesFile().getStringValue( context.getString(R.string.items_in_category, moduleAPI.getContentData().size(), (moduleAPI.getTitle() != null ? moduleAPI.getTitle() : "this category")));
                            ((TextView) componentViewResult.componentView).setText(string);
                            ((TextView) componentViewResult.componentView).setId(R.id.see_all_title_text_view);

                            break;*/

                        case WATCHLIST_PAGE_API_TITLE: {
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getWatchlistPageTitle() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getWatchlistPageTitle());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                String text = title != null ? title.toUpperCase() : component.getText().toUpperCase();
                                ((TextView) componentViewResult.componentView).setText(text);

                                // ((TextView) componentViewResult.componentView).setText(component.getText().toUpperCase());
                            }
                            if (jsonValueKeyMap.get(viewType) == PAGE_WATCHLIST_03_MODULE_KEY) {
                                ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                            } else {
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            }
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TITLE");
                        }
                        break;
                        case HISTORY_PAGE_API_TITLE: {
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getHistoryTitleLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getHistoryTitleLabel());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                String text = title != null ? title.toUpperCase() : component.getText().toUpperCase();
                                ((TextView) componentViewResult.componentView).setText(text);

                                // ((TextView) componentViewResult.componentView).setText(component.getText().toUpperCase());
                            }
                            //((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            if (jsonValueKeyMap.get(viewType) == PAGE_HISTORY_04_MODULE_KEY) {
                                ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                            } else {
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            }
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TITLE");
                        }
                        break;
                        case CONTACT_US_PAGE_API_TITLE: {

                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getContactUsLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getContactUsLabel());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                String text = title != null ? title.toUpperCase() : component.getText().toUpperCase();
                                ((TextView) componentViewResult.componentView).setText(text);

                                // ((TextView) componentViewResult.componentView).setText(component.getText().toUpperCase());
                            }
                            //((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TITLE");
                        }
                        break;
                        case LANGUAGE_SETTINGS_PAGE_API_TITLE: {
                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getSelectLanguageText());
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TITLE");
                        }
                        break;
                        case ACCOUNT_PAGE_API_TITLE: {

                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getAccountLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getAccountLabel());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                String text = title != null ? title.toUpperCase() : component.getText().toUpperCase();
                                ((TextView) componentViewResult.componentView).setText(text);

                                // ((TextView) componentViewResult.componentView).setText(component.getText().toUpperCase());
                            }
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TITLE");
                        }
                        break;
                        case YOU_ARE_SIGNED_IN_AS_LABEL_KEY: {

                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getYouAreSignInAsLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getYouAreSignInAsLabel());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                String text = title != null ? title.toUpperCase() : component.getText().toUpperCase();
                                ((TextView) componentViewResult.componentView).setText(text);
                            }
                            componentViewResult.componentView.setFocusable(false);
                        }
                        break;

                        case PAGE_CATEGORY_TRAY_TITLE_KEY:
                            try {
                                ((TextView) componentViewResult.componentView).setId(R.id.see_all_title_text_view);
                                String title;
                                if (!TextUtils.isEmpty(moduleAPI.getTitle())) {
                                    title = moduleAPI.getTitle();
                                } else if (appCMSPresenter.getSeeAllModule() != null
                                        && !TextUtils.isEmpty(appCMSPresenter.getSeeAllModule().getTitle())) {
                                    title = appCMSPresenter.getSeeAllModule().getTitle();
                                } else {
                                    title = "This Category";
                                }
                                String string = appCMSPresenter.getLanguageResourcesFile().getStringValue(context.getString(R.string.items_in_category, moduleAPI.getContentData().size(), title));
                                ((TextView) componentViewResult.componentView).setText(string);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;
                        case PAGE_REDEMPTION_ERROR_LABEL_KEY:
                            ((TextView) componentViewResult.componentView).setId(R.id.redemption_error_text_id);
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(getColor(
                                    context,
                                    component.getTextColor())));
                            ((TextView) componentViewResult.componentView).setText(component.getText());
//                            ((TextView) componentViewResult.componentView).setVisibility(View.INVISIBLE);
                            break;
                        case PAGE_AGE_ERROR_LABEL_KEY:
                            ((TextView) componentViewResult.componentView).setId(R.id.ageConsentError);
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(getColor(
                                    context,
                                    component.getTextColor())));
                            ((TextView) componentViewResult.componentView).setText(component.getText());
                            ((TextView) componentViewResult.componentView).setVisibility(View.INVISIBLE);
                            break;
                        case PAGE_API_DESCRIPTION:

                            if (!TextUtils.isEmpty(moduleAPI.getRawText())) {
                                TextView textView = new TextView(context);
                                moduleAPI.setRawText(moduleAPI.getRawText().replaceAll("</?a[^>]*>", ""));
                                String htmlStyleRegex = "<style([\\s\\S]+?)</style>";
                                textView.setText(Html.fromHtml(moduleAPI.getRawText().replaceAll(htmlStyleRegex, ""), null, new ListTagHandler()), TextView.BufferType.SPANNABLE);

                                textView.setFocusable(true);
                                //  componentViewResult.componentView.setTag("API_DSECRIPTION");

                                if (null != appCMSPresenter.getAppCtaTextColor()) {
                                    textColor = Color.parseColor(txtColor);
                                } else if (!TextUtils.isEmpty(component.getTextColor())) {
                                    textColor = Color.parseColor(getColor(context, component.getTextColor()));
                                } else if (component.getStyles() != null) {
                                    if (!TextUtils.isEmpty(component.getStyles().getColor())) {
                                        textColor = Color.parseColor(getColor(context, component.getStyles().getColor()));
                                    } else if (!TextUtils.isEmpty(component.getStyles().getTextColor())) {
                                        textColor =
                                                Color.parseColor(getColor(context, component.getStyles().getTextColor()));
                                    }
                                }
                                textView.setTextColor(textColor);

                                textView.setTypeface(Utils.getTypeFace(context,
                                        appCMSPresenter,
                                        component));

                                ((ScrollView) componentViewResult.componentView).addView(textView);


                                if (null != viewType && viewType.equalsIgnoreCase(context.getString(R.string.app_cms_ancillary_pages_module))) {
                                    View.OnFocusChangeListener onFocusChangeListener = (view, b) -> {
                                        if (null != appCMSPresenter
                                                && null != appCMSPresenter.getCurrentActivity()
                                                && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(b);
                                        }
                                    };
                                    textView.setOnFocusChangeListener(onFocusChangeListener);
                                    componentViewResult.componentView.setOnFocusChangeListener(onFocusChangeListener);
                                }
                            }


                            break;

                        case RESET_PASSWORD_TITLE_KEY:
                            if (appCMSPresenter.isNewsTemplate()) {
                                ((TextView) componentViewResult.componentView).setTextColor(appCMSPresenter.getGeneralTextColor());
                            } else {
                                ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            }
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getForgotPasswordCtaText() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getForgotPasswordCtaText());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                ((TextView) componentViewResult.componentView).setText(title != null ? title : component.getText());
                                // ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) componentViewResult.componentView).setSelected(true);
                            break;

                        case CONTACT_US_EMAIL_LABEL:
                            String emailUsAtText = component.getText();
                            if (!TextUtils.isEmpty(emailUsAtText)
                                    && appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getCustomerService() != null) {
                                String title = resources.getUIresource(component.getText());
                                // ((TextView) componentViewResult.componentView).setText(title != null ? title : component.getText());

                                    title = appCMSPresenter.getLocalisedStrings().getEmailUsAtLabel();
                                String text = (title != null ? title : component.getText()) + " "
                                        + appCMSPresenter.getAppCMSMain().getCustomerService().getEmail();
                                ((TextView) componentViewResult.componentView).setText(text);
                            }
                            break;

                        case CONTACT_US_PHONE_LABEL:
                            String callUsAtText = component.getText();
                            if (!TextUtils.isEmpty(callUsAtText)) {
                                String phone = "";
                                String phoneNumber = "";
                                String text = "";
                                if (null != appCMSPresenter.getAppCMSMain()
                                        && null != appCMSPresenter.getAppCMSMain().getCustomerService()) {
                                    phoneNumber = appCMSPresenter.getAppCMSMain().getCustomerService().getPhoneNumber();
                                    phone = appCMSPresenter.getAppCMSMain().getCustomerService().getPhone();
                                }
                                if (TextUtils.isEmpty(phoneNumber)) {
                                    text = phone;
                                } else {
                                    text = phoneNumber;
                                }
                                if (!TextUtils.isEmpty(text)) {

                                    String title = resources.getUIresource(component.getText());
                                    title = appCMSPresenter.getLocalisedStrings().getCallUsAtLabel();
                                    ((TextView) componentViewResult.componentView).setText(
                                            (title != null ? title : component.getText()) + " " + text
                                    );

                                } else {
                                    componentViewResult.componentView.setVisibility(GONE);
                                }
                            }
                            break;
                        case PAGE_SEGMENT_TITLE_KEY:
                        case PAGE_TRAY_TITLE_KEY:
                            if (!TextUtils.isEmpty(component.getText())) {
                                String title = resources.getUIresource(component.getText());
                                ((TextView) componentViewResult.componentView).setText(title != null ? title.toUpperCase() : component.getText().toUpperCase());
                                // ((TextView) componentViewResult.componentView).setText(component.getText().toUpperCase());
                            } else if (moduleAPI.getSettings() != null && !moduleAPI.getSettings().getHideTitle() &&
                                    !TextUtils.isEmpty(moduleAPI.getTitle())) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle().toUpperCase());
                            }
                            if (componentKey == PAGE_SEGMENT_TITLE_KEY)
                                componentViewResult.componentView.setId(R.id.segment_tray_title);
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("TRAY_TITLE");
                            break;

                        case PAGE_LIVE_PLAYER_TITLE_KEY:
                        case PAGE_EXPANDED_VIEW_TITLE_KEY:
                            int selectedIndex = 0;
                            TextView livePlayerTitle = ((TextView) componentViewResult.componentView);
                            livePlayerTitle.setFocusable(false);
                            if(componentKey == PAGE_LIVE_PLAYER_TITLE_KEY){
                                componentViewResult.componentView.setId(R.id.live_player_view_title);
                                selectedIndex = appCMSPresenter.getSelectedSchedulePosition();
                                if(settings != null && settings.getTabs() != null && settings.getTabs().size() >0) {
                                    Glide.with(context)
                                            .load(settings.getTabs().get(selectedIndex).getTabIcon())
                                            .into(new CustomTarget<Drawable>(48, 48) {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    DrawableCompat.setTint(resource, appCMSPresenter.getBrandPrimaryCtaTextColor());
                                                    resource.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                                                    livePlayerTitle.setCompoundDrawables(resource, null, null, null);
                                                    livePlayerTitle.setCompoundDrawablePadding(15);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                }

                                            });
                                }
                            }else{
                                componentViewResult.componentView.setId(R.id.expanded_view_title);
                                selectedIndex = moduleAPI.getItemPosition();
                            }

                            if (moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                    !TextUtils.isEmpty(moduleAPI.getContentData().get(selectedIndex).getGist().getTitle())) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(selectedIndex).getGist().getTitle().trim());
                            }

                            if (component.getNumberOfLines() != 0) {
                                ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                            }
                            ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);

                            break;

                        case PAGE_LIVE_PLAYER_DESCRIPTION_KEY:
                        case PAGE_EXPANDED_VIEW_DESCRIPTION_KEY:
                            int selectedPosition = 0;
                            if(componentKey == PAGE_LIVE_PLAYER_DESCRIPTION_KEY) {
                                componentViewResult.componentView.setId(R.id.live_player_view_description);
                                selectedPosition = appCMSPresenter.getSelectedSchedulePosition();
                            }
                            else {
                                componentViewResult.componentView.setId(R.id.expanded_view_description);
                                selectedPosition = moduleAPI.getItemPosition();
                            }

                            componentViewResult.componentView.setFocusable(false);
                            if (moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                    !TextUtils.isEmpty(moduleAPI.getContentData().get(selectedPosition).getGist().getDescription())) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(selectedPosition).getGist().getDescription());
                            }

                            if (component.getNumberOfLines() != 0) {
                                ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                            }
                            ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                            break;

                        case PAGE_LOGIN_TEXT_KEY:
                            if (!appCMSPresenter.isUserLoggedIn()) {
                                componentViewResult.componentView.setId(R.id.login_text);
                                TextView loginTextView = ((TextView) componentViewResult.componentView);
                                loginTextView.setFocusable(true);
                                String loginText = appCMSPresenter.getLocalisedStrings().getLoginText();
                                String haveAccountText = appCMSPresenter.getLocalisedStrings().getHaveAccountText() + " " + loginText;
                                loginTextView.setText(haveAccountText);

                                loginTextView.setOnClickListener(view -> openLoginDialog(appCMSPresenter));
                                ClickableSpan loginClick = new ClickableSpan() {
                                    @Override
                                    public void onClick(@Nonnull View view) {
                                        openLoginDialog(appCMSPresenter);
                                    }
                                };
                                loginTextView.setHighlightColor(Color.TRANSPARENT); // prevent TextView change background when highlight
                                Spannable wordToSpan = new SpannableString(loginTextView.getText().toString());

                                int startIndexOfLink = loginTextView.getText().toString().toLowerCase().indexOf(loginText.toLowerCase());
                                if (startIndexOfLink != -1)
                                    wordToSpan.setSpan(loginClick, startIndexOfLink, startIndexOfLink + appCMSPresenter.getLocalisedStrings().getLoginText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//                                loginTextView.setMovementMethod(LinkMovementMethod.getInstance());
                                loginTextView.setText(wordToSpan, TextView.BufferType.SPANNABLE);
                                loginTextView.setOnFocusChangeListener((view, hasFocus) -> {
                                    loginTextView.setLinkTextColor(hasFocus ? appCMSPresenter.getBrandPrimaryCtaColor() : appCMSPresenter.getGeneralTextColor());
                                    wordToSpan.setSpan(new StyleSpan(hasFocus ? Typeface.BOLD : Typeface.NORMAL), startIndexOfLink, startIndexOfLink + loginText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                });
                            }

                            break;

                        case PAGE_VIDEO_DESCRIPTION_KEY:
                            String videoDescription = null;
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getDescription() != null) {
                                videoDescription = moduleAPI.getContentData().get(0).getGist().getDescription();
                            }
                            String title = "";
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getTitle() != null) {
                                title = moduleAPI.getContentData().get(0).getGist().getTitle();
                            }
                            if (null == videoDescription) {
                                videoDescription = title;
                            }
                            if (!TextUtils.isEmpty(videoDescription)) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(videoDescription));
                                } else {
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(videoDescription, Html.FROM_HTML_MODE_COMPACT));
                                }
                            }
                            ViewTreeObserver textVto = componentViewResult.componentView.getViewTreeObserver();

                            final ViewCreatorMultiLineLayoutListener viewCreatorLayoutListener =
                                    new ViewCreatorMultiLineLayoutListener(((TextView) componentViewResult.componentView),
                                            title,
                                            videoDescription,
                                            appCMSPresenter,
                                            false,
                                            appCMSPresenter.getBrandPrimaryCtaColor(),
                                            appCMSPresenter.getGeneralTextColor(),
                                            false);
                            textVto.addOnGlobalLayoutListener(viewCreatorLayoutListener);

                            final String fullText = videoDescription;
                            componentViewResult.componentView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    appCMSPresenter.showMoreDialog(moduleAPI.getContentData().get(0).getGist().getTitle(),
                                            fullText);
                                }
                            });

                            // componentViewResult.componentView.setFocusable(true);
                            final int _textColor = textColor;
                            componentViewResult.componentView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    viewCreatorLayoutListener.setSpanOnFocus((TextView) view, b, _textColor);
                                }
                            });
                            componentViewResult.componentView.setTag("VIDEO_DESC_KEY");
                            break;
                        case PAGE_AUTOPLAY_MOVIE_DESCRIPTION_KEY:

                            String autoplayVideoDescription = moduleAPI.getContentData().get(0).getGist().getDescription();

                            if (null == autoplayVideoDescription) {
                                autoplayVideoDescription = moduleAPI.getContentData().get(0).getGist().getTitle();
                            }
                            if (!TextUtils.isEmpty(autoplayVideoDescription)) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(autoplayVideoDescription));
                                } else {
                                    ((TextView) componentViewResult.componentView).setText(Html.fromHtml(autoplayVideoDescription, Html.FROM_HTML_MODE_COMPACT));
                                }
                            }
                            ViewTreeObserver Vto = componentViewResult.componentView.getViewTreeObserver();
                            final ViewCreatorMultiLineLayoutListener layoutListener =
                                    new ViewCreatorMultiLineLayoutListener(((TextView) componentViewResult.componentView),
                                            moduleAPI.getContentData().get(0).getGist().getTitle(),
                                            autoplayVideoDescription,
                                            appCMSPresenter,
                                            true,
                                            appCMSPresenter.getBrandPrimaryCtaColor(),
                                            appCMSPresenter.getGeneralTextColor(), false);
                            Vto.addOnGlobalLayoutListener(layoutListener);
                            break;
                        case PAGE_VIDEO_TITLE_KEY:
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getTitle() != null) {
                                if (!TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getTitle())) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                                }
                            }
                            ((TextView) componentViewResult.componentView).setMaxLines(2);
                            ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                            break;
                        case RAW_HTML_TITLE_KEY:
                            txtColor = Utils.getTitleColorForST(context, appCMSPresenter);
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(txtColor));
                            if (!TextUtils.isEmpty(moduleAPI.getTitle())) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                                ((TextView) componentViewResult.componentView).setMaxLines(1);
                                ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                            } else {
                                componentViewResult.componentView = null;
                                moduleView = null;
                            }

                            break;
                        case PAGE_AUTOPLAY_MOVIE_TITLE_KEY:
                            if (!TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getTitle())) {
                                System.out.println("JKL PAGE_AUTOPLAY_MOVIE_TITLE_KEY movieId = "+moduleAPI.getContentData().get(0).getGist().getId() );
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getContentData().get(0).getGist().getTitle());
                            }
                            ViewTreeObserver titleTextVto = componentViewResult.componentView.getViewTreeObserver();

                            ViewCreatorTitleLayoutListener viewCreatorTitleLayoutListener =
                                    new ViewCreatorTitleLayoutListener((TextView) componentViewResult.componentView);
                            titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);
                            ((TextView) componentViewResult.componentView).setSingleLine(true);
                            ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            ((TextView) componentViewResult.componentView).setMarqueeRepeatLimit(-1);
                            componentViewResult.componentView.setSelected(true);
                            componentViewResult.componentView.setId(R.id.autoplay_up_next_movie_title);
                            break;
                        case PAGE_AUTOPLAY_FINISHED_MOVIE_TITLE_KEY:
                            componentViewResult.componentView.setId(R.id.autoplay_finished_movie_title);
                            break;

                        case PAGE_VIDEO_SUBTITLE_KEY:
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null) {

                                if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                                    String text = "";
                                    if (moduleAPI.getContentData().get(0).getGist().getRuntime() > 0) {
                                        text = Utils.convertSecondsToTime(moduleAPI.getContentData().get(0).getGist().getRuntime());
                                    }
                                    if (null != moduleAPI.getContentData().get(0).getGist().getPublishDate()) {
                                        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", new Locale(appCMSPresenter.getLanguage().getLanguageCode()));
                                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                                        Calendar calendar = Calendar.getInstance();
                                        calendar.setTimeInMillis(Long.parseLong(moduleAPI.getContentData().get(0).getGist().getPublishDate()));
                                        String publishDate = formatter.format(calendar.getTime());

                                        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(publishDate)) {
                                            text = text + " | ";
                                        }
                                        if (!TextUtils.isEmpty(publishDate)) {
                                            text = text + publishDate;
                                        }
                                    }
                                    ((TextView) componentViewResult.componentView).setText(text.toString());
                                    componentViewResult.componentView.setAlpha(0.6f);
                                } else {
                                    setViewWithSubtitle(context,
                                            moduleAPI.getContentData().get(0),
                                            componentViewResult.componentView,
                                            appCMSPresenter);
                                }
                            }
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("SUBTITLE");
                            break;

                        case PAGE_VIDEO_AGE_LABEL_KEY:
                            if (!TextUtils.isEmpty(moduleAPI.getContentData().get(0).getParentalRating())) {
                                String parentalRating = moduleAPI.getContentData().get(0).getParentalRating();
                                String convertedRating = context.getString(R.string.age_rating_converted_default);
                                if (parentalRating.contains(context.getString(R.string.age_rating_y7))) {
                                    convertedRating = context.getString(R.string.age_rating_converted_y7);
                                } else if (parentalRating.contains(context.getString(R.string.age_rating_y))) {
                                    convertedRating = context.getString(R.string.age_rating_converted_y);
                                } else if (parentalRating.contains(context.getString(R.string.age_rating_g))) {
                                    convertedRating = context.getString(R.string.age_rating_converted_g);
                                } else if (parentalRating.contains(context.getString(R.string.age_rating_pg))) {
                                    convertedRating = context.getString(R.string.age_rating_converted_pg);
                                } else if (parentalRating.contains(context.getString(R.string.age_rating_fourteen))) {
                                    convertedRating = context.getString(R.string.age_rating_converted_fourteen);
                                }
                                ((TextView) componentViewResult.componentView).setText(convertedRating);
                                ((TextView) componentViewResult.componentView).setGravity(Gravity.CENTER);
                                applyBorderToComponent(context,
                                        componentViewResult.componentView,
                                        component);
                            }
                            componentViewResult.componentView.setFocusable(false);
                            componentViewResult.componentView.setTag("AGE_LABEL");
                            break;


                        case PAGE_SIGNUP_FOOTER_LABEL_KEY: {
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getSignUpTermsAgreementLabel() != null
                                    && appCMSPresenter.getGenericMessagesLocalizationMap() != null
                                    && appCMSPresenter.getGenericMessagesLocalizationMap().getTermsOfUseLabel() != null
                                    && appCMSPresenter.getGenericMessagesLocalizationMap().getAndLabel() != null
                                    && appCMSPresenter.getGenericMessagesLocalizationMap().getPrivacyPolicyLabel() != null) {
                                String termsOfUseLabel = appCMSPresenter.getLocalisedStrings().getTermsOfUsesText();
                                String privacyPolicyLabel = appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText();
                                String andLabel = appCMSPresenter.getLocalisedStrings().getAndLabel();

                                String finalString = moduleAPI.getMetadataMap().getSignUpTermsAgreementLabel() + " "
                                        + termsOfUseLabel + " "
                                        + andLabel + " "
                                        + privacyPolicyLabel;

                                int tosStartIndex = 0;
                                int tosEndIndex = 0;
                                tosStartIndex = finalString.indexOf(termsOfUseLabel);
                                tosEndIndex = termsOfUseLabel.length() + tosStartIndex;

                                int ppStartIndex = 0;
                                int ppEndIndex = 0;
                                ppStartIndex = finalString.indexOf(privacyPolicyLabel);
                                ppEndIndex = privacyPolicyLabel.length() + ppStartIndex;

                                SpannableString spannableString = new SpannableString(finalString);

                                if (finalString.contains(termsOfUseLabel)) {

                                    ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View textView) {

                                            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                                                MetaPage tosPage = appCMSPresenter.getTosPage();
                                                if (null != tosPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            tosPage.getPageId(),
                                                            tosPage.getPageName(),
                                                            tosPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false,
                                                            false, false, false);
                                                }
                                            } else {
                                                MetaPage tosPage = appCMSPresenter.getTosPage();
                                                if (null != tosPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            tosPage.getPageId(),
                                                            tosPage.getPageName(),
                                                            tosPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            }

                                        }
                                    };
                                    spannableString.setSpan(clickableSpan, tosStartIndex, tosEndIndex,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableString.setSpan(
                                            new ForegroundColorSpan(Color.parseColor(appCMSPresenter
                                                    .getAppCMSMain().getBrand().getGeneral()
                                                    .getBlockTitleColor())),
                                            tosStartIndex,
                                            tosEndIndex,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                }
                                if (finalString.contains(privacyPolicyLabel)) {
                                    ClickableSpan clickableSpan1 = new ClickableSpan() {
                                        @Override
                                        public void onClick(View textView) {
                                            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                                                MetaPage privacyPolicyPage = appCMSPresenter.getPrivacyPolicyPage();
                                                if (null != privacyPolicyPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            privacyPolicyPage.getPageId(),
                                                            privacyPolicyPage.getPageName(),
                                                            privacyPolicyPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            } else {

                                                MetaPage privacyPolicyPage = appCMSPresenter.getPrivacyPolicyPage();

                                                if (null != privacyPolicyPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            privacyPolicyPage.getPageId(),
                                                            privacyPolicyPage.getPageName(),
                                                            privacyPolicyPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            }
                                        }
                                    };
                                    spannableString.setSpan(clickableSpan1, ppStartIndex, ppEndIndex,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableString.setSpan(
                                            new ForegroundColorSpan(Color.parseColor(appCMSPresenter
                                                    .getAppCMSMain().getBrand().getGeneral()
                                                    .getBlockTitleColor())),
                                            ppStartIndex,
                                            ppEndIndex,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                }
                                TextView textView = (TextView) componentViewResult.componentView;
                                textView.setText(spannableString);
                                textView.setMovementMethod(LinkMovementMethod.getInstance());
                            } else {
                                String text = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.sign_up_tos_and_pp_text));
                                SpannableString spannableString = new SpannableString(text);
                                String tosText = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.terms_of_use));
                                String tosTextHindi = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.terms_of_uses));
                                if (text.contains(tosText) || text.contains(tosTextHindi)) {
                                    int tosStartIndex = 0;
                                    int tosEndIndex = 0;
                                    if (text.contains(tosText)) {
                                        tosStartIndex = text.indexOf(tosText);
                                        tosEndIndex = tosText.length() + tosStartIndex;
                                    } else if (text.contains(tosTextHindi)) {
                                        tosStartIndex = text.indexOf(tosTextHindi);
                                        tosEndIndex = tosTextHindi.length() + tosStartIndex;
                                    }

                                    ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View textView) {

                                            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                                                MetaPage tosPage = appCMSPresenter.getTosPage();
                                                if (null != tosPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            tosPage.getPageId(),
                                                            tosPage.getPageName(),
                                                            tosPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            } else {
                                                MetaPage tosPage = appCMSPresenter.getTosPage();
                                                if (null != tosPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            tosPage.getPageId(),
                                                            tosPage.getPageName(),
                                                            tosPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            }

                                        }
                                    };
                                    spannableString.setSpan(clickableSpan, tosStartIndex, tosEndIndex,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableString.setSpan(
                                            new ForegroundColorSpan(Color.parseColor(appCMSPresenter
                                                    .getAppCMSMain().getBrand().getGeneral()
                                                    .getBlockTitleColor())),
                                            tosStartIndex,
                                            tosEndIndex,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                }
                                String ppText = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.privacy_policy)).toLowerCase();
                                if (text.contains(ppText)) {
                                    int ppStartIndex = text.indexOf(ppText);
                                    int ppEndIndex = ppText.length() + ppStartIndex;
                                    ClickableSpan clickableSpan1 = new ClickableSpan() {
                                        @Override
                                        public void onClick(View textView) {
                                            if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                                                MetaPage privacyPolicyPage = appCMSPresenter.getPrivacyPolicyPage();
                                                if (null != privacyPolicyPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            privacyPolicyPage.getPageId(),
                                                            privacyPolicyPage.getPageName(),
                                                            privacyPolicyPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            } else {
                                                MetaPage privacyPolicyPage = appCMSPresenter.getPrivacyPolicyPage();
                                                if (null != privacyPolicyPage) {
                                                    appCMSPresenter.navigateToTVPage(
                                                            privacyPolicyPage.getPageId(),
                                                            privacyPolicyPage.getPageName(),
                                                            privacyPolicyPage.getPageUI(),
                                                            false,
                                                            Uri.EMPTY,
                                                            false,
                                                            true,
                                                            false, false, false, false);
                                                }
                                            }
                                        }
                                    };
                                    spannableString.setSpan(clickableSpan1, ppStartIndex, ppEndIndex,
                                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    spannableString.setSpan(
                                            new ForegroundColorSpan(Color.parseColor(appCMSPresenter
                                                    .getAppCMSMain().getBrand().getGeneral()
                                                    .getBlockTitleColor())),
                                            ppStartIndex,
                                            ppEndIndex,
                                            Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                                }

                                TextView textView = (TextView) componentViewResult.componentView;
                                textView.setText(spannableString);
                                textView.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                        }
                        break;
                        case PAGE_AUTOPLAY_MOVIE_PLAYING_IN_LABEL_KEY:

                            // ((TextView) componentViewResult.componentView).setText(component.getText());
                            String title1 = resources.getUIresource(component.getText());
                            ((TextView) componentViewResult.componentView).setText(title1 != null ? title1 : component.getText());

                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getPlayInText(component.getText()));

                            componentViewResult.componentView.setId(R.id.up_next_text_view_id);
                            break;

                        case PAGE_AUTOPLAY_FINISHED_UP_TITLE_KEY: {

                            // ((TextView) componentViewResult.componentView).setText(component.getText());
                            String title2 = resources.getUIresource(component.getText());
                            ((TextView) componentViewResult.componentView).setText(title2 != null ? title2 : component.getText());

                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getJustFinishedLabel());

                        }
                        break;

                        case PAGE_AUTOPLAY_MOVIE_COUNTDOWN_CANCELLED_LABEL_KEY:
                            //    ((TextView) componentViewResult.componentView).setText(component.getText());
                            String title2 = resources.getUIresource(component.getText());
                            ((TextView) componentViewResult.componentView).setText(title2 != null ? title2 : component.getText());

                            ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLocalisedStrings().getCountdownCancelledLabel());

                            componentViewResult.componentView.setId(R.id.countdown_cancelled_text_view_id);
                            componentViewResult.componentView.setVisibility(View.INVISIBLE);
                            break;

                        case PAGE_SETTINGS_SUBSCRIPTION_END_DATE_LABEL_KEY:
                            if (appCMSPresenter.isAppSVOD()) {
                                TextView tvEndDate = (TextView) componentViewResult.componentView;
                                if (appCMSPresenter.getActiveSubscriptionEndDate() != null) {
                                    String strDate = appCMSPresenter.getActiveSubscriptionEndDate().substring(0, 10);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    try {
                                        Date date = format.parse(strDate);
                                        System.out.println("Date ->" + date);
                                        String label = "";
                                        if (moduleAPI.getMetadataMap() != null
                                                && moduleAPI.getMetadataMap().getDeferredSubscriptionCancelDateLabel() != null) {
                                            label = moduleAPI.getMetadataMap().getDeferredSubscriptionCancelDateLabel();
                                        }
                                        if (!TextUtils.isEmpty(label)) {
                                            tvEndDate.setText(new StringBuilder().append(label).append(" ").append(android.text.format.DateFormat.format("MM-dd-yyyy", date)).toString());
                                        } else {
                                            tvEndDate.setText(context.getString(R.string.subscription_cancelled_in, android.text.format.DateFormat.format("MM-dd-yyyy", date)));
                                        }
                                        //if sheduled subscription date has been expired then dont show subscription end date.
                                        if( (new Date().getTime() > date.getTime())
                                                || appCMSPresenter.isSubscribedPlanRenewable() ){
                                            tvEndDate.setVisibility(View.INVISIBLE);
                                        }
                                    } catch (ParseException e) {
                                        tvEndDate.setText(context.getString(R.string.subscription_cancelled_in, strDate));
                                    }
                                } else {
                                    appCMSPresenter.getSubscriptionData(new Action1<AppCMSUserSubscriptionPlanResult>() {
                                        @Override
                                        public void call(AppCMSUserSubscriptionPlanResult appCMSUserSubscriptionPlanResult) {
                                            if (appCMSUserSubscriptionPlanResult != null
                                                    && appCMSUserSubscriptionPlanResult.getSubscriptionInfo() != null
                                                    && appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionEndDate() != null) {
                                                appCMSPresenter.setActiveSubscriptionEndDate(appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionEndDate());
                                                appCMSPresenter.setActiveSubscriptionStatus(appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus());
                                                appCMSPresenter.setActiveSubscriptionPlanName(appCMSUserSubscriptionPlanResult.getSubscriptionPlanInfo().getName());
                                                appCMSPresenter.setSubscriptionPlanRenewable(appCMSUserSubscriptionPlanResult.getSubscriptionPlanInfo().isRenewable());
                                                appCMSPresenter.setActiveSubscriptionPlatform(appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform());

                                                String strDate = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionEndDate().substring(0, 10);
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                                try {
                                                    Date date = format.parse(strDate);
                                                    System.out.println("Date ->" + date);
                                                    String label = moduleAPI.getMetadataMap().getDeferredSubscriptionCancelDateLabel();
                                                    if (!TextUtils.isEmpty(label)) {
                                                        tvEndDate.setText(new StringBuilder().append(label).append(" ").append(android.text.format.DateFormat.format("MM-dd-yyyy", date)).toString());
                                                    } else {
                                                        tvEndDate.setText(context.getString(R.string.subscription_cancelled_in, android.text.format.DateFormat.format("MM-dd-yyyy", date)));
                                                    }
                                                    //if sheduled subscription date has been expired then dont show subscription end date.
                                                    if(new Date().getTime() > date.getTime() || appCMSPresenter.isSubscribedPlanRenewable()){
                                                        tvEndDate.setVisibility(View.INVISIBLE);
                                                    }

                                                } catch (ParseException e) {
                                                    tvEndDate.setText(context.getString(R.string.subscription_cancelled_in, strDate));
                                                }
                                            }
                                        }
                                    }, false);
                                }
                            }
                            break;
                        case PAGE_SETTINGS_SUBSCRIPTION_DURATION_LABEL_KEY:
                            if (appCMSPresenter.isAppSVOD()) {
                                TextView tv = (TextView) componentViewResult.componentView;
                                tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                tv.setSelected(true);
                                tv.setSingleLine();
                                if (appCMSPresenter.getActiveSubscriptionStatus() == null
                                        || appCMSPresenter.getActiveSubscriptionPlanName() == null
                                        || appCMSPresenter.getActiveSubscriptionPlanTitle() == null) {
                                    appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                                        String noActiveSubscriptionText = appCMSPresenter.getLocalisedStrings().getNoSubscriptionMessage();
                                        try {
                                            if (appCMSUserSubscriptionPlanResult != null) {
                                                String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                                                if (subscriptionStatus.equalsIgnoreCase("COMPLETED")
                                                        || subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
                                                    String planName = appCMSUserSubscriptionPlanResult.getSubscriptionPlanInfo().getName();
                                                    String planTitle = null;

                                                    List<PlanDetail> planDetails = appCMSUserSubscriptionPlanResult.getSubscriptionPlanInfo().getPlanDetails();

                                                    if (planDetails.size() > 0
                                                            && planDetails.get(0) != null
                                                            && !TextUtils.isEmpty(planDetails.get(0).getTitle())) {
                                                        appCMSPresenter.setActiveSubscriptionPlanTitle(planDetails.get(0).getTitle());
                                                        planTitle = planDetails.get(0).getTitle();
                                                    }

                                                    appCMSPresenter.setActiveSubscriptionStatus(subscriptionStatus);
                                                    appCMSPresenter.setActiveSubscriptionPlanName(planName);
                                                    appCMSPresenter.setSubscriptionPlanRenewable(appCMSUserSubscriptionPlanResult.getSubscriptionPlanInfo().isRenewable());
                                                    appCMSPresenter.setActiveSubscriptionPlatform(appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform());
                                                    appCMSPresenter.setActiveSubscriptionEndDate(appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionEndDate());
                                                    tv.setText(!TextUtils.isEmpty(planTitle) ? planTitle : planName);
                                                } else {
                                                    tv.setText(noActiveSubscriptionText);
                                                }
                                            } else {
                                                tv.setText(noActiveSubscriptionText);
                                            }
                                        } catch (Exception e) {
                                            tv.setText(noActiveSubscriptionText);
                                        }
                                    }, false);
                                } else {
                                    try {
                                        String noActiveSubscriptionText = appCMSPresenter.getLocalisedStrings().getNoSubscriptionMessage();
                                        String activeSubscriptionStatus = appCMSPresenter.getActiveSubscriptionStatus();
                                        if (activeSubscriptionStatus.equalsIgnoreCase("COMPLETED")
                                                || activeSubscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
                                            String planName = appCMSPresenter.getActiveSubscriptionPlanName();
                                            String planTitle = appCMSPresenter.getActiveSubscriptionPlanTitle();
                                            tv.setText(!TextUtils.isEmpty(planTitle) ? planTitle : planName);
                                        } else {
                                            tv.setText(noActiveSubscriptionText);
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            }
                            break;

                        case PAGE_SETTINGS_SUBSCRIPTION_PROCESSOR_LABEL_KEY: {
                            String errorMsg = "";
                            if (appCMSPresenter.isUserSubscribed()) {
                                String platform = appCMSPresenter.getAppPreference().getActiveSubscriptionPlatform();
                                errorMsg = getErrorMessage(context,appCMSPresenter, platform);
                            } else {
                                errorMsg = appCMSPresenter.getLanguageResourcesFile().getStringValue(context.getString(R.string.subscription_not_purchased));
                                if (appCMSPresenter.getGenericMessagesLocalizationMap() != null
                                        && appCMSPresenter.getGenericMessagesLocalizationMap().getGuestUserSubscriptionMessage() != null) {
                                    errorMsg = appCMSPresenter.getGenericMessagesLocalizationMap().getGuestUserSubscriptionMessage();
                                }
                            }
                            ((TextView) componentViewResult.componentView).setText(errorMsg);
                            break;
                        }

                        case PAGE_SETTINGS_TITLE_KEY: {
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSettingsLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getSettingsLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                        }
                        break;
                        case PAGE_SETTING_ACCOUNT_HEADER_VIEW_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getAccountDetailsLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getAccountDetailsLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTING_PASSWORD_LABEL_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getPasswordLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getPasswordLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                        case PAGE_SETTING_PASSWORD_VALUE_KEY:
                            ((TextView) componentViewResult.componentView).setText(component.getText());
                            break;
                        case PAGE_SETTING_CONNECTED_ACCOUNT_LABEL_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getConnectedAccountPlaceHolder() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getConnectedAccountPlaceHolder());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTING_YOUR_INTEREST_VALUE_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getYourInterests() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getYourInterests());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTING_EMAIL_LABEL_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEmailLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getEmailLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            //((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                            break;
                        case TEXT_VIEW_NAME:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getNameLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getNameLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTINGS_NAME_VALUE_KEY:
                            (componentViewResult.componentView).setId(R.id.user_name);
                            if(!TextUtils.isEmpty(appCMSPresenter.getLoggedInUserName())) {
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLoggedInUserName());
                            }
                            break;
                        case PAGE_SETTINGS_PHONE_LABEL_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getPhoneLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getPhoneLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTINGS_PHONE_VALUE_KEY:
                            if(!TextUtils.isEmpty(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getAppPreference().getLoggedInUserPhone());
                            }
                            break;
                        case PAGE_EDIT_TEXT_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEditLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getEditLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTING_ACCOUNT_EDIT_PHONE_FROM_WEBSITE_KEY:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEditPhoneNumberLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getEditPhoneNumberLabel());
                            } else {
                                ((TextView) componentViewResult.componentView).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTING_ACCOUNT_EDIT_TEXT_FROM_WEBSITE_KEY:
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(context.getResources().getString(R.string.edit_email_from_website));
                            stringBuilder.append(" ");
                            stringBuilder.append(appCMSPresenter.getAppCMSMain().getDomainName());
                            stringBuilder.append("/user ");
                            ((TextView) componentViewResult.componentView).setText(stringBuilder);
                            break;
                        case PAGE_SETTINGS_PLAN_PROCESSOR_VALUE_KEY:
                            String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();
                            if (paymentProcessor != null && appCMSPresenter.isUserSubscribed()) {
                                if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_ios_payment_processor)) ||
                                        paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_ios_payment_processor_friendly))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_ios_payment_processor_friendly));
                                } else if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_web_payment_processor_friendly))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_web_payment_processor_friendly));
                                } else if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_android_payment_processor)) ||
                                        paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_android_payment_processor_friendly))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_android_payment_processor_friendly));
                                } else if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_prepaid_payment_processor))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_prepaid_payment_processor));
                                } else if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_ccavenue_payment_processor))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_ccavenue_payment_processor_friendly));
                                } else if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_sslcommerz_payment_processor))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_sslcommerz_payment_processor_friendly));
                                } else if (paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_juspay_payment_processor))) {
                                    ((TextView) componentViewResult.componentView).setText(context.getString(R.string.subscription_juspay_payment_processor_friendly));
                                }
                            } else {
                                ((TextView) componentViewResult.componentView).setText("");
                            }
                            break;
                        case PAGE_SETTINGS_SUBSCRIPTION_LABEL_KEY:
                            if (appCMSPresenter.isAppSVOD()) {
                                if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSubscriptionHeader() != null) {
                                    ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getSubscriptionHeader());
                                } else if (!TextUtils.isEmpty(component.getText())) {
                                    // ((TextView) componentViewResult.componentView).setText(component.getText());
                                    ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                                }
                            }
                            break;

                        case PAGE_SETTINGS_USER_EMAIL_LABEL_KEY:
                            (componentViewResult.componentView).setId(R.id.user_email);
                            if ((appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.ENTERTAINMENT) ||
                                    appCMSPresenter.isNewsTemplate()) &&
                                    !(jsonValueKeyMap.get(viewType) == PAGE_USER_MANAGEMENT_MODULE_KEY4)) {
                                String text = null;
                                if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLoggedInAsLabel() != null) {
                                    text = moduleAPI.getMetadataMap().getLoggedInAsLabel() + " " + appCMSPresenter.getLoggedInUserEmail();
                                } else {
                                    text = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.logged_in_as)) + " " + appCMSPresenter.getLoggedInUserEmail();
                                }
                                if (jsonValueKeyMap.get(viewType) == PAGE_USER_MANAGEMENT_MODULE_KEY3)
                                    Utils.setSpanColor(((TextView) componentViewResult.componentView), text, appCMSPresenter.getLoggedInUserEmail(), appCMSPresenter.getBrandPrimaryCtaTextColor());
                                else
                                    ((TextView) componentViewResult.componentView).setText(text);
                            } else {
                                ((TextView) componentViewResult.componentView).setText(appCMSPresenter.getLoggedInUserEmail());
                            }
                            break;
                        case CODE_SYNC_TEXT_LINE_1:
                            componentViewResult.componentView.setId(R.id.code_sync_text_line_1);
                            if (!TextUtils.isEmpty(component.getText())) {
                                //((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                            }
                            break;
                        case CODE_SYNC_TEXT_LINE_2:
                            componentViewResult.componentView.setId(R.id.code_sync_text_line_2);
                            if (!TextUtils.isEmpty(component.getText())) {
                                // ((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                            }
                            break;
                        case CODE_SYNC_TEXT_LINE_3:
                            componentViewResult.componentView.setId(R.id.code_sync_text_line_3);
                            if (!TextUtils.isEmpty(component.getText())) {
                                // ((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                            }
                            break;
                        case CODE_SYNC_TEXT_LINE_HEADER:
                            componentViewResult.componentView.setId(R.id.code_sync_text_line_header);

                            /*headerText = headerTextLine.getText().toString()
                                    .replace("$App$", getResources().getString(R.string.app_name))
                                    .replace("$app_web_url$", appCMSPresenter.getAppCMSMain().getDomainName());*/

                            String activateDeviceUrl = appCMSPresenter.getAppCMSMain().getDomainName() + (appCMSPresenter.isLoginWithTVProvider ? "/registerdevice " : "/user ");
                            if (moduleAPI.getSettings() != null && !TextUtils.isEmpty(moduleAPI.getSettings().getActivateDeviceUrl())) {
                                activateDeviceUrl = moduleAPI.getSettings().getActivateDeviceUrl();
                            }


                            StringBuilder sb = new StringBuilder();
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getEnterCodeToLinkDeviceLabel_1() != null
                                    && moduleAPI.getMetadataMap().getEnterCodeToLinkDeviceLabel_2() != null
                                    && moduleAPI.getMetadataMap().getEnterCodeToLinkDeviceLabel_3() != null) {
                                sb.append(moduleAPI.getMetadataMap().getEnterCodeToLinkDeviceLabel_1());
                                sb.append(" ");
                                sb.append(context.getResources().getString(R.string.app_name));
                                sb.append(" ");
                                sb.append(moduleAPI.getMetadataMap().getEnterCodeToLinkDeviceLabel_2());
                                sb.append(" ");
                                sb.append(activateDeviceUrl);
                                sb.append(" ");
                                sb.append(moduleAPI.getMetadataMap().getEnterCodeToLinkDeviceLabel_3());
                            }

                            if (!TextUtils.isEmpty(sb.toString())) {
                                SpannableString spannableString = new SpannableString(sb);
                                int startIndex = sb.indexOf(activateDeviceUrl);
                                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE),
                                        startIndex, startIndex + activateDeviceUrl.length(),
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ((TextView) componentViewResult.componentView).setText(spannableString);
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                // ((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                            }
                            break;
                        case CODE_LINK_ACCOUNT_ACTIVATE_DEVICE_LABEL:
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getActivateDeviceLabel() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getMetadataMap().getActivateDeviceLabel());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                // ((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                            }
                            break;
                        case LANGUAGE_LABEL_KEY:
                            Language language = appCMSPresenter.getLanguage();
                            if (!TextUtils.isEmpty(component.getText())) {
                                ((TextView) componentViewResult.componentView).setText(language.getLanguageName());
                            }
                            break;
                        case WEATHER_CITY_LABEL:
                            if (moduleAPI != null &&
                                    moduleAPI.getTickerFeed() != null &&
                                    moduleAPI.getTickerFeed().getTitle() != null) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getTickerFeed().getTitle().replace("Today Forecast", ""));
                            }
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                            break;
                        case WEATHER_DATE_LABEL:
                            Date date = new Date();
                            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                            String formattedDate = df.format(date);
                            ((TextView) componentViewResult.componentView).setText(formattedDate);
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                            break;
                        case WEATHER_CDATA:
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                            String tickertext = resources.getUIresource(context.getString(R.string.no_data_available));
                            if (moduleAPI != null &&
                                    moduleAPI.getTickerFeed() != null &&
                                    moduleAPI.getTickerFeed().getEntry() != null &&
                                    moduleAPI.getTickerFeed().getEntry().getSummary() != null &&
                                    moduleAPI.getTickerFeed().getEntry().getSummary().length() > 0) {
                                Spanned spanned = null;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    spanned = Html.fromHtml(moduleAPI.getTickerFeed().getEntry().getSummary());
                                } else {
                                    spanned = Html.fromHtml(moduleAPI.getTickerFeed().getEntry().getSummary(), Html.FROM_HTML_MODE_COMPACT);
                                }
                                if (spanned != null) {
                                    tickertext = spanned.toString();
                                }
                            }
                            ((TextView) componentViewResult.componentView).setText(tickertext);
                            break;
                        case WEATHER_TIME_LABEL:
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                            break;
                        case WEATHER_TEMP_TICKER_LABEL:
                            ((TextView) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                            ((TextView) componentViewResult.componentView).setText(
                                    resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) + Utils.DEGREE
                                            : component.getText() + Utils.DEGREE
                            );

                            break;
                        default:
                            if (viewType != null && viewType.contains("AC SubNav")) {
                                ((TextView) componentViewResult.componentView).setText(moduleAPI.getTitle());
                            } else if (!TextUtils.isEmpty(component.getText())) {
                                // ((TextView) componentViewResult.componentView).setText(component.getText());
                                ((TextView) componentViewResult.componentView).setText(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                            }
                    }
                } else {
                    ((TextView) componentViewResult.componentView).setSingleLine(true);
                    ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                }

                if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                    componentViewResult.componentView.setBackgroundColor(Color.parseColor(getColor(context, component.getBackgroundColor())));
                }
                if (/*!TextUtils.isEmpty(component.getFontFamily())
                        && */componentViewResult.componentView instanceof TextView) {
                    ((TextView) componentViewResult.componentView).setTypeface(
                            Utils.getTypeFace(context,
                                    appCMSPresenter,
                                    component));
                }
                if (component.getLineSpacingMultiplier() != 0
                        && componentViewResult.componentView instanceof TextView) {
                    ((TextView) componentViewResult.componentView).setLineSpacing(0, component.getLineSpacingMultiplier());
                    ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                }

                if (component.getNumberOfLines() != 0 && componentViewResult.componentView instanceof TextView) {
                    ((TextView) componentViewResult.componentView).setMaxLines(component.getNumberOfLines());
                }
                break;

            case PAGE_VIDEO_PLAYER_VIEW_KEY:
                componentViewResult.componentView = new FrameLayout(context);
                switch (componentKey) {
                    case PAGE_VIDEO_PLAYER_VIEW_KEY_VALUE:

                        FrameLayout parentFrameLayout = (FrameLayout) componentViewResult.componentView;
                        parentFrameLayout.setId(R.id.video_player_id);
                        //parentFrameLayout.setFocusable(true);
                        CustomTVVideoPlayerView mCustomVideoPlayerView = (CustomTVVideoPlayerView) appCMSPresenter.getPlayerLruCache().get(moduleAPI.getId());
                        if (mCustomVideoPlayerView == null) {
                            mCustomVideoPlayerView = playerView(context, appCMSPresenter);
                            appCMSPresenter.getPlayerLruCache().put(moduleAPI.getId(), mCustomVideoPlayerView);
                            Utils.setCustomTVVideoPlayerView(null);
                            Utils.setCustomTVVideoPlayerView(mCustomVideoPlayerView);

                            if (mCustomVideoPlayerView != null && mCustomVideoPlayerView.getParent() != null) {
                                ((ViewGroup) mCustomVideoPlayerView.getParent()).removeView(mCustomVideoPlayerView);
                            }

                            //parentFrameLayout.addView(mCustomVideoPlayerView);
                            mCustomVideoPlayerView.setClosedCaptionSelectorCreated(false);
                            String videoId = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getOriginalObjectId();
                            String videoTitle = moduleAPI.getContentData().get(appCMSPresenter.getSelectedSchedulePosition()).getGist().getTitle();
                            int contentPosition = moduleAPI.getItemPosition();
                            String promoID = null,trailerTitle=null;
                            if (moduleAPI.getContentData().get(contentPosition) != null &&
                                    moduleAPI.getContentData().get(contentPosition).getContentDetails() != null &&
                                    moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos() != null &&
                                    moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos().size() > 0 &&
                                    moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos().get(0).getId() != null) {
                                promoID = moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos().get(0).getId();
                            }
                            if (videoId == null)
                                videoId = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getId();
                          //  mCustomVideoPlayerView.setVideoUri(videoId, videoTitle, moduleAPI.getContentData().get(appCMSPresenter.getSelectedSchedulePosition()), false,false);
                            setDefaultPlayerData(mCustomVideoPlayerView,moduleAPI,promoID,videoId,videoTitle,appCMSPresenter);

                        }

                        parentFrameLayout.setBackground(Utils.getTrayBorder(context, Utils.getFocusBorderColor(context, appCMSPresenter), component));
                        mCustomVideoPlayerView.setPadding(component.getPadding(), component.getPadding(), component.getPadding(), component.getPadding());

                        parentFrameLayout.setOnClickListener(v -> {
                            appCMSPresenter.setTVVideoPlayerView(null);
                            appCMSPresenter.setTVVideoPlayerView(Utils.getCustomTVVideoPlayerView());
                            appCMSPresenter.tvVideoPlayerView.getPlayerView().setUseController(true);
                            Utils.getCustomTVVideoPlayerView().hideControlsForLiveStream();
                            appCMSPresenter.showFullScreenTVPlayer();
                            appCMSPresenter.tvVideoPlayerView.getPlayerView().getPlayer().seekTo(appCMSPresenter.tvVideoPlayerView.getPlayer().getContentPosition() + 1000);
                            Utils.getCustomTVVideoPlayerView().disableLiveRight();
                        });


                        parentFrameLayout.setOnKeyListener((v, keyCode, event) -> {
                            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                                Utils.isPlayerSelected = false;
                                appCMSPresenter.sendBroadcastToHandleMiniPlayer(true);
                                parentFrameLayout.setNextFocusDownId(R.id.browse_first_row);
                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id) != null) {
                                    appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id).setVisibility(VISIBLE);
                                    ((TVExpandedViewModule) appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id)).updateTrailerID();
                                }
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                            }
                            return false;
                        });
                        parentFrameLayout.setOnFocusChangeListener((v, hasFocus) -> {
                            //((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(!hasFocus);
                            if (hasFocus) {
                                Utils.isPlayerSelected = true;

                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id) != null) {
                                    appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id).setVisibility(GONE);
                                }
                                appCMSPresenter.sendBroadcastToHandleMiniPlayer(false);
                                if (pageView.findViewById(R.id.expanded_view_id) != null) {
                                    if (null != appCMSPresenter
                                            && null != appCMSPresenter.getCurrentActivity()
                                            && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {

                                        if(settings != null && settings.isShowTabs() && settings.getTabs() != null && !settings.getTabs().isEmpty()){
                                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(false);
                                        }else {
                                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                                        }
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).removeHandlerCallBacks();
                                    }
                                }
                            }
                        });
                        break;

                    case PAGE_TRAILER_VIDEO_PLAYER_VIEW_KEY_VALUE:
                        (componentViewResult.componentView).setId(R.id.trailer_view_id);
                        String trailerID = null, trailerTitle = null,promoID = null;
                        int contentPosition = moduleAPI.getItemPosition();
                        if (moduleAPI.getContentData().get(contentPosition) != null &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails() != null &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails().getTrailers() != null &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails().getTrailers().size() > 0 &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails().getTrailers().get(0).getId() != null) {
                            trailerID = moduleAPI.getContentData().get(contentPosition).getContentDetails().getTrailers().get(0).getId();
                            trailerTitle = moduleAPI.getContentData().get(contentPosition).getContentDetails().getTrailers().get(0).getTitle();
                        } else if (moduleAPI.getContentData().get(contentPosition).getShowDetails() != null &&
                                moduleAPI.getContentData().get(contentPosition).getShowDetails().getTrailers() != null &&
                                moduleAPI.getContentData().get(contentPosition).getShowDetails().getTrailers().size() > 0 &&
                                moduleAPI.getContentData().get(contentPosition).getShowDetails().getTrailers().get(0).getId() != null) {
                            trailerID = moduleAPI.getContentData().get(contentPosition).getShowDetails().getTrailers().get(0).getId();
                            trailerTitle = moduleAPI.getContentData().get(contentPosition).getShowDetails().getTrailers().get(0).getTitle();
                        }
                        if (moduleAPI.getContentData().get(contentPosition) != null &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails() != null &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos() != null &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos().size() > 0 &&
                                moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos().get(0).getId() != null) {
                            promoID = moduleAPI.getContentData().get(contentPosition).getContentDetails().getPromos().get(0).getId();
                        }
                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).setCarouselPlayerTask(context, appCMSPresenter,
                                (FrameLayout) componentViewResult.componentView, trailerID, trailerTitle,null);
                        break;
                }
                break;

            case PAGE_IMAGE_KEY:
                if (componentKey == PAGE_VIDEO_IMAGE_KEY) {
                    componentViewResult.componentView = new FrameLayout(context);
                } else {
                    componentViewResult.componentView = new ImageView(context);
                }
                switch (componentKey) {
                    case PAGE_SHOW_IMAGE_KEY:
                        int placeHolderImage = R.drawable.vid_image_placeholder_land;/*BaseView.isLandscape(context) ? R.drawable.vid_image_placeholder_land : R.drawable.vid_image_placeholder_port;*/
                        componentViewResult.componentView.setId(R.id.expanded_view_show_image);
                        if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null
                                && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData() != null
                                && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().size() > 0
                                && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist() != null
                                && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist() != null) {
                            int viewWidth = Integer.parseInt(component.getLayout().getTv().getWidth());
                            int viewHeight = Integer.parseInt(component.getLayout().getTv().getHeight());
                            String imageUrl = "";
                            if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_32x9() != null) {
                                viewWidth = (int) Utils.getViewWidthByRatio("32:9", viewHeight);
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_32x9(),
                                        viewWidth,
                                        viewHeight);
                            } else if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_16x9() != null) {
                                viewWidth = (int) Utils.getViewWidthByRatio("16:9", viewHeight);
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_16x9(),
                                        viewWidth,
                                        viewHeight);
                            }

                            System.out.println("TVVC imageUrl: " + imageUrl);
                            if (!ImageUtils.loadImage((ImageView) componentViewResult.componentView,
                                    imageUrl,
                                    ImageLoader.ScaleType.CENTER)) {
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(new RequestOptions().override(viewWidth, viewHeight))
                                        .into((ImageView) componentViewResult.componentView);
                            }
                        }
                        break;
                        case APP_LOGO_KEY:
                        ((ImageView) componentViewResult.componentView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.footer_logo));
                        break;
                    case PAGE_BACKGROUND_IMAGE_KEY:
                        componentViewResult.componentView = new ImageView(context);
                        ((ImageView) componentViewResult.componentView).setBackground(ContextCompat.getDrawable(context, R.drawable.gradient_background));

                        break;
                    case PAGE_SEASON_BACKGROUND_IMAGE_KEY:
                    case PAGE_EPISODE_BACKGROUND_IMAGE_KEY:
                    case PAGE_SEGMENT_BACKGROUND_IMAGE_KEY:
                        componentViewResult.componentView = new ImageView(context);
                        (componentViewResult.componentView).setBackground(ContextCompat.getDrawable(context, R.drawable.show_background));
                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_SEASON_BACKGROUND_IMAGE_KEY)
                            (componentViewResult.componentView).setId(R.id.season_table_view_background);

                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_EPISODE_BACKGROUND_IMAGE_KEY)
                            (componentViewResult.componentView).setId(R.id.episode_table_view_background);

                        if (jsonValueKeyMap.get(component.getKey()) == PAGE_SEGMENT_BACKGROUND_IMAGE_KEY)
                            (componentViewResult.componentView).setId(R.id.segment_table_view_background);

                        break;
                    case PAGE_AUTOPLAY_MOVIE_IMAGE_KEY:
                        String movieBorderColor = Utils.getFocusColor(context, appCMSPresenter);
                        componentViewResult.componentView.setBackground(Utils.getTrayBorder(context, movieBorderColor, component));
                        int imageViewWidth = (int) Utils.getViewWidth(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        int imageViewHeight = (int) Utils.getViewHeight(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);

                        if (imageViewHeight > 0 && imageViewWidth > 0 && imageViewHeight > imageViewWidth) {
                            String imageUrl = "";
                            if (moduleAPI.getContentData().get(0).getGist().getPosterImageUrl() != null) {
                                imageUrl = moduleAPI.getContentData().get(0).getGist().getPosterImageUrl() + "?impolicy=resize&w=" + imageViewWidth + "&h=" + imageViewHeight;
                            } else if (moduleAPI.getContentData().get(0).getImages() != null && moduleAPI.getContentData().get(0).getImages().get_3x4() != null
                                    && moduleAPI.getContentData().get(0).getImages().get_3x4().getUrl() != null) {
                                imageUrl = moduleAPI.getContentData().get(0).getImages().get_3x4().getUrl() + "?impolicy=resize&w=" + imageViewWidth + "&h=" + imageViewHeight;
                            } else if (moduleAPI.getContentData().get(0).getImages() != null && moduleAPI.getContentData().get(0).getImages().get_3x4Image() != null
                                    && moduleAPI.getContentData().get(0).getImages().get_3x4Image().getUrl() != null) {
                                imageUrl = moduleAPI.getContentData().get(0).getImages().get_3x4Image().getUrl() + "?impolicy=resize&w=" + imageViewWidth + "&h=" + imageViewHeight;
                            } else if (moduleAPI.getContentData().get(0).getGist().getVideoImageUrl() != null) {
                                imageUrl = moduleAPI.getContentData().get(0).getGist().getVideoImageUrl();
                                ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }


                            Glide.with(context)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder)))
                                    .into(((ImageView) componentViewResult.componentView));
                        } else if (imageViewWidth > 0) {
                            Glide.with(context)
                                    .load(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into(((ImageView) componentViewResult.componentView));
                        } else {
                            Glide.with(context)
                                    .load(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into(((ImageView) componentViewResult.componentView));
                        }
                        //setTag is causing crash here. We can not setTag on a View which Glide is targeting.
                        //componentViewResult.componentView.setTag(context.getString(R.string.video_image_key));
                        componentViewResult.componentView.setFocusable(true);


                        componentViewResult.componentView.setBackground(Utils.getTrayBorder(context, Utils.getFocusColor(context, appCMSPresenter), component));
                        componentViewResult.componentView.setId(R.id.autoplay_play_movie_image);
                        break;
                    case PAGE_SHOW_IMAGE_THUMBNAIL_KEY:
                        ImageView showImage = (ImageView) componentViewResult.componentView;
                        String showImageUrl = "";
                        int showViewWidth = 0;
                        int showViewHeight = (int) Utils.getViewHeight(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        showImage.setScaleType(ImageView.ScaleType.FIT_XY);

                        int contentDataPosition = moduleAPI.getItemPosition();
                        if (moduleAPI.getContentData().get(contentDataPosition).getGist().getImageGist() != null && moduleAPI.getContentData().get(contentDataPosition).getGist().getImageGist().get_32x9() != null) {
                            showViewWidth = (int) Utils.getViewWidthByRatio("32:9", showViewHeight);
                            showImageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    moduleAPI.getContentData().get(contentDataPosition).getGist().getImageGist().get_32x9(),
                                    showViewWidth,
                                    showViewHeight);
                            Glide.with(context)
                                    .load(showImageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into(showImage);
                        } else {
                            showViewWidth = (int) Utils.getViewWidthByRatio("16:9", showViewHeight);
                            showImageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    moduleAPI.getContentData().get(contentDataPosition).getGist().getVideoImageUrl(),
                                    showViewWidth,
                                    showViewHeight);
                            Glide.with(context)
                                    .load(showImageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into(showImage);
                        }

                        showImage.setOnClickListener(view -> {
                            String permalink = null;
                            String action = "showDetailPage";
                            if (component.getAction() != null) {
                                action = component.getAction();
                            }
                            String[] extraData = new String[1];
                            if (moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getPermalink() != null) {
                                permalink = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getPermalink();
                                extraData[0] = permalink;
                            }

                            if (permalink != null) {
                                appCMSPresenter.launchTVButtonSelectedAction(
                                        permalink,
                                        action,
                                        null,
                                        extraData,
                                        null,
                                        false,
                                        0,
                                        null,
                                        null);
                            }
                        });
                        break;
                    case PAGE_VIDEO_IMAGE_KEY:
                        ImageView imageView = new ImageView(componentViewResult.componentView.getContext());
                        if (component.getLayout().getTv().getPadding() != null) {
                            String pad = component.getLayout().getTv().getPadding();
                            int padding = Integer.valueOf(pad != null ? pad : "0");
                            imageView.setPadding(padding + 1, padding, padding + 1, padding);
                        }

                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);
                        imageView.setLayoutParams(params);


                        ((FrameLayout) componentViewResult.componentView).addView(imageView);
                        componentViewResult.componentView.setFocusable(false);

                        int viewWidth = (int) Utils.getViewWidth(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        int viewHeight = (int) Utils.getViewHeight(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                        if (jsonValueKeyMap.get(viewType) == PAGE_EXPANDED_VIEW_MODULE_KEY) {
                            String imageUrl = "";

                            if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().size() > 0 &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist() != null) {
                                if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_32x9() != null) {
                                    viewWidth = (int) Utils.getViewWidthByRatio("32:9", viewHeight);
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_32x9(),
                                            viewWidth,
                                            viewHeight);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                                    .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                            .into(imageView);
                                } else if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_16x9() != null) {
                                    viewWidth = (int) Utils.getViewWidthByRatio("16:9", viewHeight);
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getSeriesData().get(0).getGist().getImageGist().get_16x9(),
                                            viewWidth,
                                            viewHeight);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                    .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                                    .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                            .into(imageView);
                                }
                            } else if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages().get_32x9Image() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages().get_32x9Image().getUrl() != null) {
                                viewWidth = (int) Utils.getViewWidthByRatio("32:9", viewHeight);
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages().get_32x9Image().getUrl(),
                                        viewWidth,
                                        viewHeight);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                                .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                        .into(imageView);
                            } else if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages().get_16x9Image() != null &&
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages().get_16x9Image().getUrl() != null) {
                                viewWidth = (int) Utils.getViewWidthByRatio("16:9", viewHeight);
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getImages().get_16x9Image().getUrl(),
                                        viewWidth,
                                        viewHeight);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                                .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                        .into(imageView);
                            }
                        } else {
                            if (viewHeight > 0 && viewWidth > 0 && viewHeight > viewWidth) {
                                Glide.with(context)
                                        .load(moduleAPI.getContentData().get(0).getGist().getPosterImageUrl())
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .error(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder))
                                                .placeholder(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder)))
                                        .into(imageView);
                            } else if (viewWidth > 0) {
                                Glide.with(context)
                                        .load(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                                .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                        .into(imageView);
                            } else {
                                Glide.with(context)
                                        .load(moduleAPI.getContentData().get(0).getGist().getVideoImageUrl())
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                                .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                        .into(imageView);
                            }
                            imageView.setFocusable(true);

                            String borderColor = Utils.getFocusColor(context, appCMSPresenter);
                            imageView.setBackground(Utils.getTrayBorder(context, borderColor, component));

                            componentViewResult.componentView.setTag(context.getString(R.string.video_image_key));
                            imageView.setOnClickListener(view -> {
                                playVideo(appCMSPresenter, context, component, moduleAPI);
                                imageView.setClickable(false);

                                new Handler().postDelayed(() -> {
                                    imageView.setClickable(true);
                                }, 3000);


                            });
                            final boolean[] clickable = {true};
                            imageView.setOnKeyListener((view, i, keyEvent) -> {
                                switch (keyEvent.getAction()) {
                                    case KeyEvent.ACTION_DOWN:
                                        switch (keyEvent.getKeyCode()) {
                                            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:

                                                if (clickable[0]) {
                                                    appCMSPresenter.showLoadingDialog(true);

                                                    if (moduleAPI.getContentData() != null &&
                                                            moduleAPI.getContentData().size() > 0 &&
                                                            moduleAPI.getContentData().get(0) != null &&
                                                            moduleAPI.getContentData().get(0).getGist() != null &&
                                                            moduleAPI.getContentData().get(0).getGist().getId() != null &&
                                                            moduleAPI.getContentData().get(0).getGist().getPermalink() != null) {

                                                        String filmId = moduleAPI.getContentData().get(0).getGist().getId();
                                                        String permaLink = moduleAPI.getContentData().get(0).getGist().getPermalink();
                                                        String title = moduleAPI.getContentData().get(0).getGist().getTitle();

                                                        appCMSPresenter.launchTVVideoPlayer(
                                                                moduleAPI.getContentData().get(0),
                                                                0,
                                                                moduleAPI.getContentData().get(0).getContentDetails().getRelatedVideoIds(),
                                                                moduleAPI.getContentData().get(0).getGist().getWatchedTime(),
                                                                null);

                                                        break;
                                                    }
                                                }
                                        }
                                        break;
                                }
                                clickable[0] = false;
                                new android.os.Handler().postDelayed(() -> clickable[0] = true, 3000);
                                return false;
                            });
                        }
                        break;

                    case RAW_HTML_IMAGE_KEY:
                        String imgUrl = "";
                        try {
                            imgUrl = Jsoup.parse(moduleAPI.getRawText()).body().getElementsByAttribute("src").attr("src");
                        } catch (Exception e) {
                        }
                        if (null != imgUrl && (imgUrl.endsWith("png") || imgUrl.endsWith("jpeg") || imgUrl.endsWith("jpg"))) {
                            Glide.with(context)
                                    .load(imgUrl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                    .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                    .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        } else {
                            componentViewResult.componentView = null;
                            moduleView = null;
                        }
                        break;

                    case PAGE_THUMBNAIL_VIDEO_IMAGE_KEY:
                        int imageWidth = (int) Utils.getViewWidth(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        int imageHeight = (int) Utils.getViewHeight(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        (componentViewResult.componentView).setId(R.id.expanded_view_video_image);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                        if (jsonValueKeyMap.get(viewType) == PAGE_CAROUSEL_07_MODULE_KEY) {
                            ((ImageView) componentViewResult.componentView).setFocusable(true);
                            ((ImageView) componentViewResult.componentView).requestFocus();
                            ((ImageView) componentViewResult.componentView).setOnClickListener(view -> {
                                String permalink = null;
                                String action = "showDetailPage";
                                if (component.getAction() != null) {
                                    action = component.getAction();
                                }
                                String[] extraData = new String[1];
                                if (moduleAPI.getContentData() != null &&
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                        moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getPermalink() != null) {
                                    permalink = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getPermalink();
                                    extraData[0] = permalink;
                                }

                                if (permalink != null) {
                                    appCMSPresenter.launchTVButtonSelectedAction(
                                            permalink,
                                            action,
                                            null,
                                            extraData,
                                            null,
                                            false,
                                            0,
                                            null,
                                            null);
                                }
                            });
                        }
                        if (imageHeight > 0 && imageWidth > 0 && imageHeight > imageWidth) {
                            String imageUrl = "";
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getPosterImageUrl() != null) {

                                imageUrl = moduleAPI.getContentData().get(0).getGist().getPosterImageUrl();
                            }
                            Glide.with(context)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        } else if (imageWidth > 0) {
                            String videoImageUrl = "";
                            if (jsonValueKeyMap.get(viewType) == PAGE_EXPANDED_VIEW_MODULE_KEY) {
                                if (moduleAPI.getContentData() != null
                                        && moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null
                                        && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null
                                        && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getVideoImageUrl() != null) {
                                    videoImageUrl = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getVideoImageUrl();
                                }
                            } else if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null
                                    && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null
                                    && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getVideoImageUrl() != null) {
                                videoImageUrl = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getVideoImageUrl();
                            }
                            Glide.with(context)
                                    .load(videoImageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        } else {
                            String videoImageUrl = "";
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getVideoImageUrl() != null) {
                                videoImageUrl = moduleAPI.getContentData().get(0).getGist().getVideoImageUrl();
                            }
                            Glide.with(context)
                                    .load(videoImageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        }
                        break;

                    case PAGE_THUMBNAIL_BUNDLE_IMAGE_KEY:
                        imageWidth = (int) Utils.getViewWidth(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);

                        imageHeight = (int) Utils.getViewHeight(context,
                                component.getLayout(),
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        ((ImageView) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);

                        if (imageHeight > 0 && imageWidth > 0 && imageHeight > imageWidth) {
                            String imageUrl = "";
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getPosterImageUrl() != null) {

                                imageUrl = moduleAPI.getContentData().get(0).getGist().getPosterImageUrl();
                            }
                            Glide.with(context)
                                    .load(imageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.poster_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        } else if (imageWidth > 0) {
                            String videoImageUrl = "";
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getImageGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9() != null) {
                                videoImageUrl = moduleAPI.getContentData().get(0).getGist().getImageGist().get_16x9();
                            }
                            Glide.with(context)
                                    .load(videoImageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        } else {
                            String videoImageUrl = "";
                            if (moduleAPI.getContentData() != null
                                    && moduleAPI.getContentData().get(0) != null
                                    && moduleAPI.getContentData().get(0).getGist() != null
                                    && moduleAPI.getContentData().get(0).getGist().getVideoImageUrl() != null) {
                                videoImageUrl = moduleAPI.getContentData().get(0).getGist().getVideoImageUrl();
                            }
                            Glide.with(context)
                                    .load(videoImageUrl)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into((ImageView) componentViewResult.componentView);
                        }
                        break;

                    case CONTACT_US_PHONE_IMAGE:
                        String phone = "";
                        String phoneNumber = "";
                        String text = "";
                        if (null != appCMSPresenter.getAppCMSMain()
                                && null != appCMSPresenter.getAppCMSMain().getCustomerService()) {
                            phoneNumber = appCMSPresenter.getAppCMSMain().getCustomerService().getPhoneNumber();
                            phone = appCMSPresenter.getAppCMSMain().getCustomerService().getPhone();
                        }
                        if (TextUtils.isEmpty(phoneNumber)) {
                            text = phone;
                        } else {
                            text = phoneNumber;
                        }
                        if (!TextUtils.isEmpty(text)) {
                            componentViewResult.componentView.setBackgroundResource(R.drawable.call_icon);
                        } else {
                            componentViewResult.componentView.setVisibility(GONE);
                        }
                        break;

                    case CONTACT_US_EMAIL_IMAGE:
                        componentViewResult.componentView.setBackgroundResource(R.drawable.email_icon);
                        break;

                    case PAGE_VIDEO_DETAIL_APP_LOGO_KEY:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.footer_logo);
                        break;

                    case PAGE_AUTOPLAY_FINISHED_MOVIE_IMAGE_KEY:
                        componentViewResult.componentView.setId(R.id.autoplay_finished_movie_image);
                        break;
                    case WEATHER_LOCATION_IMAGE:
                        ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.location_pin);
                        break;
                    case PAGE_LOGO_IMAGE_KEY:
                        ImageView logoImage = (ImageView) componentViewResult.componentView;
                        logoImage.setImageResource(R.drawable.footer_logo);
                        break;
                    case PAGE_RIGHT_ARROW_KEY:
                        (componentViewResult.componentView).setId(R.id.right_arrow_id);
                        if (jsonValueKeyMap.get(viewType) == PAGE_USER_MANAGEMENT_MODULE_KEY4) {
                            ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.ic_play_tv);
                        } else if (jsonValueKeyMap.get(viewType) == PAGE_STAND_ALONE_VIDEO_PLAYER02) {
                            if(settings != null && settings.isShowTabs() && settings.getTabs() != null && !settings.getTabs().isEmpty()) {
                                ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.right_arrow);
                                DrawableCompat.setTint(((ImageView) componentViewResult.componentView).getDrawable(), appCMSPresenter.getGeneralTextColor());
                            }
                        } else {
                            ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.right_arrow_news);
                            if (moduleAPI.getContentData().size() - 1 == moduleAPI.getItemPosition()) {
                                ((ImageView) componentViewResult.componentView).setVisibility(GONE);
                            } else {
                                ((ImageView) componentViewResult.componentView).setVisibility(VISIBLE);
                            }
                        }
                        break;
                    case PAGE_LEFT_ARROW_KEY:
                        (componentViewResult.componentView).setId(R.id.left_arrow_id);
                        if (jsonValueKeyMap.get(viewType) == PAGE_STAND_ALONE_VIDEO_PLAYER02) {
                            if(settings != null && settings.isShowTabs() && settings.getTabs() != null && !settings.getTabs().isEmpty()) {
                                ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.left_arrow_black);
                                DrawableCompat.setTint(((ImageView) componentViewResult.componentView).getDrawable(), appCMSPresenter.getGeneralTextColor());
                            }
                        }else{
                            ((ImageView) componentViewResult.componentView).setImageResource(R.drawable.left_arrow_news);
                            if (moduleAPI.getItemPosition() == 0) {
                                ((ImageView) componentViewResult.componentView).setVisibility(GONE);
                            } else {
                                ((ImageView) componentViewResult.componentView).setVisibility(VISIBLE);
                            }
                        }
                        break;
                    case PAGE_TV_PROVIDER_LOGO:
                        Glide.with(context)
                                .load(appCMSPresenter.getAppPreference().getTvProviderLogo())
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                .into((ImageView) componentViewResult.componentView);
                        break;

                    default:
                        if (!TextUtils.isEmpty(component.getImageName())) {
                            Glide.with(context)
                                    .load(component.getImageName())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into((ImageView) componentViewResult.componentView);
                        }
                }
                break;

            case PAGE_PROGRESS_VIEW_KEY:
                componentViewResult.componentView = new ProgressBar(context,
                        null,
                        R.style.Widget_AppCompat_ProgressBar_Horizontal);
                if (!TextUtils.isEmpty(component.getProgressColor())) {
                    int color = Color.parseColor(getColor(context, component.getProgressColor()));
                    ((ProgressBar) componentViewResult.componentView).setProgressDrawable(new ColorDrawable(color));
                }
                componentViewResult.componentView.setFocusable(false);
                break;

            case PAGE_SEPARATOR_VIEW_KEY:
            case PAGE_SEGMENTED_VIEW_KEY:
                componentViewResult.componentView = new View(context);
                if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                    componentViewResult.componentView.
                            setBackgroundColor(Color.parseColor(getColor(context, component.getBackgroundColor())));
                } else {
                    componentViewResult.componentView.
                            setBackgroundColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                }
                componentViewResult.componentView.setFocusable(false);
                break;

            case PAGE_CASTVIEW_VIEW_KEY:
                if (moduleAPI.getContentData().get(0).getCreditBlocks() == null) {
                    componentViewResult.componentView = null;
                    return;
                }
                String fontFamilyKey = null, fontFamilyKeyTypeParsed = null;
                if (!TextUtils.isEmpty(component.getFontFamilyKey())) {
                    String[] fontFamilyKeyArr = component.getFontFamilyKey().split("-");
                    if (fontFamilyKeyArr.length == 2) {
                        fontFamilyKey = fontFamilyKeyArr[0];
                        fontFamilyKeyTypeParsed = fontFamilyKeyArr[1];
                    }
                }

                String fontFamilyValue = null, fontFamilyValueTypeParsed = null;
                if (!TextUtils.isEmpty(component.getFontFamilyValue())) {
                    String[] fontFamilyValueArr = component.getFontFamilyValue().split("-");
                    if (fontFamilyValueArr.length == 2) {
                        fontFamilyValue = fontFamilyValueArr[0];
                        fontFamilyValueTypeParsed = fontFamilyValueArr[1];
                    }
                }

                textColor = Color.parseColor(getColor(context, component.getTextColor()));
                String directorTitle = null;
                StringBuilder directorListSb = new StringBuilder();
                String starringTitle = null;
                StringBuilder starringListSb = new StringBuilder();

                for (CreditBlock creditBlock : moduleAPI.getContentData().get(0).getCreditBlocks()) {
                    AppCMSUIKeyType creditBlockType = jsonValueKeyMap.get(creditBlock.getTitle());
                    if ((creditBlockType == PAGE_VIDEO_CREDITS_DIRECTEDBY_KEY ||
                            creditBlockType == PAGE_VIDEO_CREDITS_DIRECTOR_KEY ||
                            creditBlockType == PAGE_VIDEO_CREDITS_DIRECTORS_KEY)) {
                        if (!TextUtils.isEmpty(creditBlock.getTitle())) {
                            directorTitle = creditBlock.getTitle();
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getDirectorLabel() != null) {
                                directorTitle = moduleAPI.getMetadataMap().getDirectorLabel();
                            }
                        }
                        if (creditBlock.getCredits() != null) {
                            for (int i = 0; i < creditBlock.getCredits().size(); i++) {
                                directorListSb.append(creditBlock.getCredits().get(i).getTitle());
                                if (i == creditBlock.getCredits().size() - 2) {
                                    directorListSb.append(" & ");
                                } else if (i < creditBlock.getCredits().size() - 1) {
                                    directorListSb.append(", ");
                                }
                            }
                        }
                    } else if (creditBlockType == PAGE_VIDEO_CREDITS_STARRING_KEY) {
                        if (!TextUtils.isEmpty(creditBlock.getTitle())) {
                            starringTitle = creditBlock.getTitle();
                            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getStarringLabel() != null) {
                                starringTitle = moduleAPI.getMetadataMap().getStarringLabel();
                            }
                        }
                        if (creditBlock.getCredits() != null) {
                            for (int i = 0; i < creditBlock.getCredits().size(); i++) {
                                starringListSb.append(creditBlock.getCredits().get(i).getTitle());
                                if (i == creditBlock.getCredits().size() - 2) {
                                    starringListSb.append(" & ");
                                } else if (i < creditBlock.getCredits().size() - 1) {
                                    starringListSb.append(", ");
                                }
                            }
                        }
                    }
                }

                componentViewResult.componentView = new TVCreditBlocksView(
                        context,
                        appCMSPresenter,
                        jsonValueKeyMap,
                        fontFamilyKey,
                        fontFamilyKeyTypeParsed,
                        fontFamilyValue,
                        fontFamilyValueTypeParsed,
                        directorTitle,
                        directorListSb.toString(),
                        starringTitle,
                        starringListSb.toString(),
                        textColor,
                        Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor()),
                        Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor()),
                        Utils.getFontSizeKey(context, component.getLayout()),
                        Utils.getFontSizeValue(context, component.getLayout()));
                componentViewResult.componentView.setFocusable(false);

                try {
                    if (TextUtils.isEmpty(directorListSb.toString())) {
                        ((TVCreditBlocksView) componentViewResult.componentView).getChildAt(0).setVisibility(GONE);
                        ((TVCreditBlocksView) componentViewResult.componentView).getChildAt(1).setVisibility(GONE);
                    }


                    if (TextUtils.isEmpty(starringListSb.toString())) {
                        ((TVCreditBlocksView) componentViewResult.componentView).getChildAt(2).setVisibility(GONE);
                        ((TVCreditBlocksView) componentViewResult.componentView).getChildAt(3).setVisibility(GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PAGE_TEXTFIELD_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                EditText textInputEditText = new EditText(context);
                if(component.isBorderEnable()){
                    textInputEditText.setBackground(Utils.getTrayBorder(context, Utils.getFocusColor(context, appCMSPresenter), component));
                }else{
                    textInputEditText.setBackground(Utils.getTrayBorder(context, Utils.getTextColor(context, appCMSPresenter), component));
                }

                if (!TextUtils.isEmpty(component.getText())) {
                    // textInputEditText.setHint(component.getText());
                    (textInputEditText).setHint(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                }
                switch (componentKey) {
                    case PAGE_EMAILTEXTFIELD_KEY:
                    case PAGE_EMAILTEXTFIELD2_KEY:
                        /*textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);*/
                        textInputEditText.setId(R.id.email_edit_box);
                        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEmailInput() != null) {
                            textInputEditText.setHint(moduleAPI.getMetadataMap().getEmailInput());
                        }
                        textInputEditText.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((focus));
                            }

                        });
                        textInputEditText.setSingleLine(true);

                        //textInputEditText.setNextFocusRightId(R.id.password_edit_box);
                        break;
                    case PAGE_PASSWORDTEXTFIELD_KEY:
                    case PAGE_PASSWORDTEXTFIELD2_KEY:
                        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getPasswordInput() != null) {
                            textInputEditText.setHint(moduleAPI.getMetadataMap().getPasswordInput());
                        }
                        textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        textInputEditText.setId(R.id.password_edit_box);
                        textInputEditText.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                        // textInputEditText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                        textInputEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

                        textInputEditText.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((focus));
                            }

                        });

                        //textInputEditText.setNextFocusLeftId(R.id.email_edit_box);
                        // ((TextInputLayout) componentViewResult.componentView).setPasswordVisibilityToggleEnabled(true);
                        break;
                    case PAGE_REDEMPTIONTEXTFIELD_KEY:

                        textInputEditText.setId(R.id.redemption_edit_box);
                        textInputEditText.setOnFocusChangeListener((view, focus) -> {
                            if (focus && null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation((focus));
                            }

                        });
                        textInputEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_button_tv) != null) {
//                                    if (!TextUtils.isEmpty(s.toString().trim())) {
//                                        ((Button) appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_button_tv)).setBackgroundColor(appCMSPresenter.getButtonBorderColor());
//                                        ((Button) appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_button_tv)).setFocusable(false);
//                                    } else {
//                                        ((Button) appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_button_tv)).setBackgroundColor(Color.GRAY);
//                                        ((Button) appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_button_tv)).setFocusable(true);
//
//
//                                        TextView errorText = appCMSPresenter.getCurrentActivity().findViewById(R.id.redemption_error_text_id);
//                                        if (errorText != null)
//                                            errorText.setVisibility(View.INVISIBLE);
//                                    }
//                                }
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        break;

                    case PAGE_MOBILETEXTFIELD_KEY:
                        textInputEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                        break;
                    default:
                }
                if (!TextUtils.isEmpty(component.getText())) {
                    // textInputEditText.setHint(component.getText());
//                    (textInputEditText).setHint(resources.getUIresource(component.getText()) != null ? resources.getUIresource(component.getText()) : component.getText());

                }
                if (!TextUtils.isEmpty(component.getBackgroundColor()) && appCMSPresenter.getTemplateType().equals(AppCMSPresenter.TemplateType.NEWS)) {
                    textInputEditText.setBackgroundColor(Color.parseColor(getColor(context, component.getBackgroundColor())));
                }
                if (!TextUtils.isEmpty(component.getTextColor())) {
                    textInputEditText.setTextColor(Color.parseColor(getColor(context, component.getTextColor())));
                }
                if (!TextUtils.isEmpty(component.getHintColor())) {
                    textInputEditText.setHintTextColor(Color.parseColor(getColor(context, component.getHintColor())));
                } else {
                    textInputEditText.setHintTextColor(Color.parseColor("#A6000000"));
                }

                if (component.getFontSize() != 0) {
                    textInputEditText.setTextSize(component.getFontSize());
                } else {
                    textInputEditText.setTextSize(context.getResources().getInteger(R.integer.app_cms_login_input_textsize));
                }
                textInputEditText.setHintTextColor(Color.parseColor("#A6000000"));
                textInputEditText.setTypeface(Utils.getTypeFace(
                        context,
                        appCMSPresenter,
                        component));
                int loginInputHorizontalMargin = context.getResources().getInteger(R.integer.app_cms_tv_login_input_horizontal_margin);
                textInputEditText.setPadding(loginInputHorizontalMargin,
                        0,
                        loginInputHorizontalMargin,
                        0);

                LinearLayout.LayoutParams textInputEditTextLayoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                textInputEditText.setLayoutParams(textInputEditTextLayoutParams);
                ((LinearLayout) componentViewResult.componentView).addView(textInputEditText);
                break;
            case PAGE_VIDEO_STARRATING_KEY:
            case PAGE_AUTOPLAY_MOVIE_STAR_RATING_KEY:
                int starBorderColor = Color.parseColor(getColor(context, component.getBorderColor()));
                int starFillColor = Color.parseColor(getColor(context, component.getFillColor()));
                float starRating = moduleAPI.getContentData().get(0).getGist().getAverageStarRating();
                componentViewResult.componentView = new StarRating(context,
                        starBorderColor,
                        starFillColor,
                        starRating);
                break;


            case PAGE_HEADER_KEY:
                componentViewResult.componentView = new HeaderView(
                        context,
                        component,
                        jsonValueKeyMap,
                        moduleAPI,
                        appCMSPresenter,
                        settings != null && settings.isPublishLabel());
                componentViewResult.componentView.setFocusable(false);
                break;
            case PAGE_SETTING_TOGGLE_SWITCH_TYPE:
                componentViewResult.componentView = new ToggleSwitchView(
                        context,
                        component,
                        jsonValueKeyMap,
                        appCMSPresenter,
                        moduleAPI.getMetadataMap(),
                        viewType
                );
                break;

            case PAGE_VIDEO_SUBTITLE_KEY:
                setViewWithSubtitle(context, moduleAPI.getContentData().get(0), componentViewResult.componentView, appCMSPresenter);
                break;

            case PAGE_SIGN_UP_MODULE_VIEW:
            case PAGE_LOGIN_MODULE_VIEW:
                if (settings != null) {
                    boolean isLoginModule = false;
                    List<LoginSignup> loginList = settings.getOptions() != null ? settings.getOptions().getEmailLoginSignup() : null;
                    int loginIndex = Utils.findIndexOfStringFromList(loginList, "Login");

                    List<LoginSignup> tveList = settings.getOptions() != null ? settings.getOptions().getSocialLoginSignup() : null;
                    int tveIndex = Utils.findIndexOfStringFromList(tveList, "TVE");

                    if ((loginIndex != -1 && loginList != null && loginList.size() > 0 && loginList.get(loginIndex).isEnable())/* ||
                            ((settings.isShowActivateDevice()) ||
                                    (tveIndex != -1 && tveList != null && tveList.size() > 0 && tveList.get(tveIndex).isEnable())*/) {
                        isLoginModule = true;
                    }
                    if ((componentKey == PAGE_LOGIN_MODULE_VIEW && isLoginModule) ||
                            (componentKey == PAGE_SIGN_UP_MODULE_VIEW && !isLoginModule)) {
                        componentViewResult.componentView = new TVLoginModuleView(
                                moduleView,
                                context,
                                component,
                                moduleAPI,
                                this,
                                jsonValueKeyMap,
                                appCMSPresenter,
                                moduleAPI.getMetadataMap(),
                                pageView,
                                settings,
                                isFromLoginDialog
                        );
                    }
                }
                break;

            case PAGE_PARENT_LINEAR_VIEW:
            case PAGE_LOGIN_MODULE_COMPONENT_VIEW:
                switch (componentKey) {
                    case PAGE_LOGIN_VIEW_KEY:
                        if (settings != null && settings.getOptions() != null &&
                                settings.getOptions().getEmailLoginSignup() != null &&
                                settings.getOptions().getEmailLoginSignup().size() > 0) {

                            List<LoginSignup> emailLoginSignUpList = settings.getOptions().getEmailLoginSignup();
                            int index = Utils.findIndexOfStringFromList(emailLoginSignUpList, "Login");
                            int signUpIndex = Utils.findIndexOfStringFromList(emailLoginSignUpList, "Signup");
                            if (index != -1 && emailLoginSignUpList.get(index).isEnable() || (signUpIndex != -1 && emailLoginSignUpList.get(signUpIndex).isEnable())) {
                                componentViewResult.componentView = new TVLinearLayoutView(
                                        moduleView,
                                        context,
                                        component,
                                        moduleAPI,
                                        this,
                                        jsonValueKeyMap,
                                        appCMSPresenter,
                                        moduleAPI.getMetadataMap(),
                                        pageView,
                                        settings,
                                        viewType,
                                        isFromLoginDialog
                                );
                            }
                        }
                        break;

                    case PAGE_LOGIN_MODULE_COMPONENT_OR_SEPARATOR_VIEW_KEY:
                        if (settings != null && settings.getOptions() != null) {
                            List<LoginSignup> emailLoginSignUpList = settings.getOptions().getEmailLoginSignup();
                            int index = Utils.findIndexOfStringFromList(emailLoginSignUpList, "Login");

                            List<LoginSignup> socialLoginSignUpList = settings.getOptions().getSocialLoginSignup();
                            int tveIndex = Utils.findIndexOfStringFromList(socialLoginSignUpList, "TVE");

                            if ((index != -1 && emailLoginSignUpList != null &&
                                    emailLoginSignUpList.size() > 0 &&
                                    emailLoginSignUpList.get(index).isEnable()) &&
                                    (settings.isShowActivateDevice() || tveIndex != -1 &&
                                            socialLoginSignUpList != null && socialLoginSignUpList.size() > 0 &&
                                            socialLoginSignUpList.get(tveIndex).isEnable())) {
                                componentViewResult.componentView = new TVLinearLayoutView(
                                        moduleView,
                                        context,
                                        component,
                                        moduleAPI,
                                        this,
                                        jsonValueKeyMap,
                                        appCMSPresenter,
                                        moduleAPI.getMetadataMap(),
                                        pageView,
                                        settings,
                                        viewType,
                                        isFromLoginDialog
                                );
                            }
                        }
                        break;

                    default:
                        componentViewResult.componentView = new TVLinearLayoutView(
                                moduleView,
                                context,
                                component,
                                moduleAPI,
                                this,
                                jsonValueKeyMap,
                                appCMSPresenter,
                                moduleAPI.getMetadataMap(),
                                pageView,
                                settings,
                                viewType,
                                isFromLoginDialog
                        );
                        if(componentKey == PAGE_PARENT_LINEAR_ACTIVATE_DEVICE_VIEW){
                            componentViewResult.componentView.setId(R.id.linear_activate_device_view);
                        }
                }
                break;

            case PAGE_EXPANDED_VIEW_MODULE_KEY:
                if (isSupportExpandedView) {
                    componentViewResult.componentView = new TVExpandedViewModule(
                            moduleView,
                            context,
                            component,
                            moduleAPI,
                            this,
                            jsonValueKeyMap,
                            appCMSPresenter,
                            moduleAPI.getMetadataMap(),
                            pageView
                    );
                    componentViewResult.componentView.setId(R.id.expanded_view_id);
                    componentViewResult.componentView.setVisibility(GONE);
                }
                break;

            case LIVE_PLAYER_COMPONENT:
                componentViewResult.componentView = new TVStandAlonePlayer02(
                        moduleView,
                        context,
                        component,
                        moduleAPI,
                        this,
                        jsonValueKeyMap,
                        appCMSPresenter,
                        moduleAPI.getMetadataMap(),
                        pageView,
                        settings,
                        viewType
                );
                (componentViewResult.componentView).setVisibility(View.GONE);
                (componentViewResult.componentView).setId(R.id.live_player_component_id);

                break;

            case PAGE_SETTING_MODULE_VIEW:
                componentViewResult.componentView = new TVSettingModuleView(
                        moduleView,
                        context,
                        component,
                        moduleAPI,
                        this,
                        jsonValueKeyMap,
                        appCMSPresenter,
                        moduleAPI.getMetadataMap(),
                        pageView,
                        viewType,
                        isFromLoginDialog
                );
                break;
            case PAGE_SETTING_MODULE_COMPONENT_VIEW:
                componentViewResult.componentView = new SettingModuleComponentView(
                        moduleView,
                        context,
                        component,
                        moduleAPI,
                        this,
                        jsonValueKeyMap,
                        appCMSPresenter,
                        moduleAPI.getMetadataMap(),
                        pageView,
                        viewType,
                        isFromLoginDialog
                );
                break;
            case VIEW_TABLE_LAYOUT:
                switch (componentKey) {
                    case TABLE_PLAN_FEATURES:

                        componentViewResult.componentView = new TVSubscriptionPlanFeaturesLayout(context, moduleAPI, appCMSPresenter);
                        componentViewResult.componentView.setId(R.id.subscription_plan_view);
                        break;
                }
                break;

            default:
        }
    }


    private void playTrailer(Module moduleAPI, AppCMSPresenter appCMSPresenter, Component component, boolean isSeries) {
        String[] extraData = new String[4];
        Trailer trailerInfo = null;
        if(moduleAPI != null
                && moduleAPI.getContentData() != null
        && moduleAPI.getContentData().size() >0
        && moduleAPI.getContentData().get(0).getContentDetails() != null
                && moduleAPI.getContentData().get(0).getContentDetails().getTrailers() != null){
            trailerInfo = moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0);
            extraData[0] = trailerInfo.getPermalink();
            extraData[2] = moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getId();
        }
        if(isSeries && moduleAPI != null
                && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().size() >0
                && moduleAPI.getContentData().get(0).getShowDetails() != null
                && moduleAPI.getContentData().get(0).getShowDetails().getTrailers() != null){
            trailerInfo = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0);
            extraData[0] = trailerInfo.getPermalink();
            extraData[2] = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId();
        }


        if (!appCMSPresenter.launchTVButtonSelectedAction(moduleAPI.getContentData().get(0).getGist().getPermalink(),
                component.getAction(),
                moduleAPI.getContentData().get(0).getGist().getTitle(),
                extraData,
                moduleAPI.getContentData().get(0),
                false,
                -1,
                null,
                null)) {
            appCMSPresenter.showLoadingDialog(false);
        }
    }

    private boolean isVideoTrailerAvailable(Module moduleAPI) {
        return moduleAPI.getContentData().get(0).getContentDetails() != null &&
                moduleAPI.getContentData().get(0).getContentDetails().getTrailers() != null &&
                moduleAPI.getContentData().get(0).getContentDetails().getTrailers().size() > 0 &&
                moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0) != null &&
                moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getId() != null;
    }

    private boolean isShowTrailerAvailable(Module moduleAPI) {
        return moduleAPI.getContentData().get(0).getShowDetails() != null &&
                moduleAPI.getContentData().get(0).getShowDetails().getTrailers() != null &&
                moduleAPI.getContentData().get(0).getShowDetails().getTrailers().size() > 0 &&
                moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0) != null &&
                /*moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getPermalink() != null &&*/
                moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId() != null;
    }

    private String getErrorMessage(Context context, AppCMSPresenter appCMSPresenter, String platform) {
        String errorMsg = context.getString(R.string.subscription_purchased_from_unknown_msg);
        if(platform != null) {
            if (platform.equalsIgnoreCase("web_browser")) {
                errorMsg = appCMSPresenter.getLocalisedStrings().getSubscriptionWebsiteMsg();
            } else if (platform.equalsIgnoreCase("android") || platform.contains("android")) {
                errorMsg = appCMSPresenter.getLocalisedStrings().getSubscriptionAndroidMsg();
            } else if (platform.contains("iOS") || platform.contains("ios_phone") || platform.contains("ios_ipad") || platform.contains("tvos") || platform.contains("ios_apple_tv")) {
                errorMsg = appCMSPresenter.getLocalisedStrings().getSubscriptionAppleMsg();
            } else if (platform.toLowerCase().contains("roku")) {
                errorMsg = appCMSPresenter.getLocalisedStrings().getSubscriptionRokuMsg();
            } else if (platform.toLowerCase().contains("fire_tv")) {
                errorMsg = appCMSPresenter.getLocalisedStrings().getSubscriptionFireTVMsg();
            }
        }
        return errorMsg;
    }

    /**
     * Fix for JM-26
     */
    static void setViewWithSubtitle(Context context, ContentDatum data, View view, AppCMSPresenter appCMSPresenter) {

        long durationInSeconds = data.getGist().getRuntime();

        long minutes = durationInSeconds / 60;
        long seconds = durationInSeconds % 60;

        String year = data.getGist().getYear();
        String primaryCategory =
                data.getGist().getPrimaryCategory() != null ?
                        data.getGist().getPrimaryCategory().getTitle() :
                        null;
//        boolean appendFirstSep = minutes > 0
//                && (!TextUtils.isEmpty(year) || !TextUtils.isEmpty(primaryCategory));
//        boolean appendSecondSep = (minutes > 0 || !TextUtils.isEmpty(year))
//                && !TextUtils.isEmpty(primaryCategory);

        StringBuilder infoText = new StringBuilder();

        if (minutes == 1) {
            infoText.append("0").append(minutes).append(" ").append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.min_abbreviation)));
        } else if (minutes > 1 && minutes < 10) {
            infoText.append("0").append(minutes).append(" ").append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.mins_abbreviation)));
        } else if (minutes >= 10) {
            infoText.append(minutes).append(" ").append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.mins_abbreviation)));
        }

        if (seconds == 1) {
            infoText.append(" ").append("0").append(seconds).append(" ").append(context.getString(R.string.sec_abbreviation));
        } else if (seconds > 1 && seconds < 10) {
            infoText.append(" ").append("0").append(seconds).append(" ").append(context.getString(R.string.secs_abbreviation));
        } else if (seconds >= 10) {
            infoText.append(" ").append(seconds).append(" ").append(context.getString(R.string.secs_abbreviation));
        }

        if (!TextUtils.isEmpty(year)) {
            infoText.append(context.getString(R.string.text_separator));
            infoText.append(year);
        }

        if (!TextUtils.isEmpty(primaryCategory)) {
            infoText.append(context.getString(R.string.text_separator));
            infoText.append(primaryCategory.toUpperCase());
        }

        ((TextView) view).setText(infoText.toString());
        view.setAlpha(0.6f);
    }


    private Module matchModuleAPIToModuleUI(ModuleList module,
                                            AppCMSPageAPI appCMSPageAPI,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
        if (appCMSPageAPI != null && appCMSPageAPI.getModules() != null) {
            for (Module moduleAPI : appCMSPageAPI.getModules()) {
                if (module.getId().equals(moduleAPI.getId())) {

                    return moduleAPI;
                }
            }
        }
        if (appCMSPageAPI != null && appCMSPageAPI.getModules() != null
                && jsonValueKeyMap.get(module.getView()) != null) {
            switch (jsonValueKeyMap.get(module.getView())) {
                case PAGE_HISTORY_01_MODULE_KEY:
                case PAGE_HISTORY_02_MODULE_KEY:
                case PAGE_HISTORY_04_MODULE_KEY:
                case PAGE_WATCHLIST_01_MODULE_KEY:
                case PAGE_WATCHLIST_02_MODULE_KEY:
                case PAGE_WATCHLIST_03_MODULE_KEY:
                case PAGE_LIBRARY_01_MODULE_KEY:
                case PAGE_LIBRARY_02_MODULE_KEY:
                case PAGE_AUTHENTICATION_MODULE_KEY:
                case PAGE_AUTOPLAY_MODULE_KEY_01:
                case PAGE_AUTOPLAY_MODULE_KEY_02:
                case PAGE_AUTOPLAY_MODULE_KEY_03:
                case PAGE_AUTOPLAY_MODULE_KEY_04:
                case PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY:
                case PAGE_AUTOPLAY_PORTRAIT_MODULE_KEY:
                case PAGE_LINK_YOUR_ACCOUNT_MODULE_KEY:
                case PAGE_AUTHENTICATION_17_MODULE_KEY:
                case PAGE_ENTITLEMENT_MODULE_KEY:
//                case PAGE_RESET_PASSWORD_MODULE_KEY:
                    if (appCMSPageAPI.getModules() != null
                            && !appCMSPageAPI.getModules().isEmpty()) {
                        return appCMSPageAPI.getModules().get(0);
                    }
                    break;
//                case PAGE_RESET_PASSWORD_MODULE_KEY:
                case PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY:
                case PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY:
                case PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY:
                    if (appCMSPageAPI.getModules() != null
                            && !appCMSPageAPI.getModules().isEmpty()) {
                        for (Module module1 : appCMSPageAPI.getModules()) {
                            if (module1.getModuleType() != null &&
                                    module1.getModuleType().equalsIgnoreCase("ViewPlanModule")) {
                                return module1;
                            }
                        }
                    }
                    break;
                case PAGE_CONTACT_US_MODULE_KEY:
//                case PAGE_LINK_YOUR_ACCOUNT_MODULE_KEY:
                    return new Module();
                case PAGE_SEE_ALL_CATEGORY_01_KEY:
                case PAGE_SEE_ALL_CATEGORY_02_KEY:
                    if (appCMSPageAPI.getModules() != null) {
                        if (module.getType().contains("CategoryDetail") && appCMSPageAPI.getModules().size() > 1) {
                            for (Module tModule: appCMSPageAPI.getModules()) {
                                if (tModule.getModuleType().equalsIgnoreCase("GeneratedTrayModule")) {
                                    return tModule;
                                }
                            }
                            return appCMSPageAPI.getModules().get(1);
                        } else if (!appCMSPageAPI.getModules().isEmpty()) {
                            return appCMSPageAPI.getModules().get(0);
                        }
                    }
                    return null;
            }
        }
        return null;
    }

    private void applyBorderToComponent(Context context, View view, Component component) {
        if (component.getBorderColor() != null) {
            if (component.getBorderWidth() > 0 && !TextUtils.isEmpty(component.getBorderColor())) {
                GradientDrawable ageBorder = new GradientDrawable();
                ageBorder.setShape(GradientDrawable.RECTANGLE);
                ageBorder.setStroke(component.getBorderWidth(),
                        Color.parseColor(getColor(context, component.getBorderColor())));
                ageBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
                view.setBackground(ageBorder);
            }
        }
    }


    public void makeTextViewResizable(final TextView tv, boolean hasFocus) {
        Spannable wordToSpan = new SpannableString(tv.getText().toString());
        int length = wordToSpan.length();
        if (hasFocus) {
            wordToSpan.setSpan(new BackgroundColorSpan(ContextCompat.getColor(tv.getContext(), android.R.color.holo_red_dark)), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new ForegroundColorSpan(Color.BLACK), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            wordToSpan.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(tv.getContext(), android.R.color.transparent)), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        wordToSpan.setSpan(new AbsoluteSizeSpan(18), length - 6, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(wordToSpan);
    }


    private void playEpisode(AppCMSPresenter appCMSPresenter,
                             Context context,
                             Component component,
                             Module moduleAPI) {

        if (moduleAPI.getContentData() != null &&
                moduleAPI.getContentData().size() > 0 &&
                moduleAPI.getContentData().get(0) != null &&
                moduleAPI.getContentData().get(0).getSeason() != null &&
                moduleAPI.getContentData().get(0).getSeason().get(0) != null &&
                moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes() != null &&
                moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() > 0 &&
                moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().get(0) != null) {
            appCMSPresenter.showLoadingDialog(true);
            List<String> relatedVideosIds = Utils.getRelatedVideosInShow(
                    moduleAPI.getContentData().get(0).getSeason(),
                    0,
                    -1);

            ContentDatum contentDatum = moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().get(0);
            appCMSPresenter.setModuleApi(moduleAPI);
            contentDatum.setSeason(moduleAPI.getContentData().get(0).getSeason());
            appCMSPresenter.launchTVVideoPlayer(
                    contentDatum,
                    0,
                    relatedVideosIds,
                    0,
                    null);
        }
    }

    private void playBundle(AppCMSPresenter appCMSPresenter,
                            Context context,
                            Component component,
                            Module moduleAPI) {
        appCMSPresenter.showLoadingDialog(true);
        if (moduleAPI.getContentData() != null &&
                moduleAPI.getContentData().size() > 0 &&
                moduleAPI.getContentData().get(0) != null &&
                moduleAPI.getContentData().get(0).getGist() != null &&
                moduleAPI.getContentData().get(0).getGist().getBundleList() != null &&
                moduleAPI.getContentData().get(0).getGist().getBundleList().get(0) != null) {

            /*List<String> relatedVideosIds = Utils.getRelatedVideosInShow(
                    moduleAPI.getContentData().get(0).getSeason(),
                    0,
                    -1);*/
            appCMSPresenter.setModuleApi(moduleAPI);
            ContentDatum contentDatum = moduleAPI.getContentData().get(0).getGist().getBundleList().get(0);
            if (contentDatum.getGist() != null) {
                contentDatum.setSeason(moduleAPI.getContentData().get(0).getSeason());
                appCMSPresenter.launchTVVideoPlayer(
                        contentDatum,
                        0,
                        null,
                        0,
                        null);
            } else {
                appCMSPresenter.showLoadingDialog(false);
            }
        }
    }
    public void makeLinks(TextView textView, String[] links, ClickableSpan[] clickableSpans) {
        SpannableString spannableString = new SpannableString(textView.getText());
        for (int i = 0; i < links.length; i++) {
            ClickableSpan clickableSpan = clickableSpans[i];
            String link = links[i];

            int startIndexOfLink = textView.getText().toString().indexOf(link);
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    private void playVideo(AppCMSPresenter appCMSPresenter, Context context, Component component, Module moduleAPI) {
        appCMSPresenter.showLoadingDialog(true);
        appCMSPresenter.setModuleApi(moduleAPI);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                appCMSPresenter.launchTVVideoPlayer(
                        moduleAPI.getContentData().get(0),
                        0,
                        null/*moduleAPI.getContentData().get(0).getContentDetails().getRelatedVideoIds()*/,
                        0,
                        null);
            }

        }, 300);
    }

    private void updateAddToWatchlistButtonStatus(Context context, AppCMSPresenter appCMSPresenter, Button btn, Module moduleAPI,
                                                  Drawable addToWatchlistImageDrawable, Drawable removeToWatchlistImageDrawable) {

        final boolean[] queued = new boolean[1];

        btn.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
        if (addToWatchlistImageDrawable != null) {
            btn.setCompoundDrawablesWithIntrinsicBounds(addToWatchlistImageDrawable, null, null, null);
            btn.setCompoundDrawablePadding(5);
        }
        if (appCMSPresenter.isUserLoggedIn()) {
            try {
                String videoId = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getOriginalObjectId();
                if (videoId == null) {
                    videoId = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getId();
                }
                appCMSPresenter.getUserVideoStatus(
                        videoId,
                        userVideoStatusResponse -> {
                            if (null != userVideoStatusResponse) {
                                queued[0] = userVideoStatusResponse.isQueued();
                                //Log.d(TAG, "appCMSAddToWatchlistResult: qued: " + queued[0]);
                                if (queued[0]) {
                                    btn.setText(appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                                    if (removeToWatchlistImageDrawable != null)
                                        btn.setCompoundDrawablesWithIntrinsicBounds(removeToWatchlistImageDrawable, null, null, null);
                                } else {
                                    btn.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                                    if (addToWatchlistImageDrawable != null)
                                        btn.setCompoundDrawablesWithIntrinsicBounds(addToWatchlistImageDrawable, null, null, null);
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                btn.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
            }
        }

        btn.setOnClickListener(v -> {
                    if (appCMSPresenter.isNetworkConnected()) {
                        if (appCMSPresenter.isUserLoggedIn()) {
                            appCMSPresenter.editWatchlist(
                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()),
                                    appCMSAddToWatchlistResult -> {
                                        //Log.d(TAG, "appCMSAddToWatchlistResult");
                                        queued[0] = !queued[0];
                                        appCMSPresenter.sendUpdateWatchListAction();
                                        if (queued[0]) {
                                            appCMSPresenter.sendAddWatchlistEvent(moduleAPI.getContentData().get(moduleAPI.getItemPosition()));
                                            btn.setText(appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                                            if (removeToWatchlistImageDrawable != null)
                                                btn.setCompoundDrawablesWithIntrinsicBounds(removeToWatchlistImageDrawable, null, null, null);
                                        } else {
                                            appCMSPresenter.sendRemoveWatchlistEvent(moduleAPI.getContentData().get(moduleAPI.getItemPosition()));
                                            btn.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                                            if (addToWatchlistImageDrawable != null)
                                                btn.setCompoundDrawablesWithIntrinsicBounds(addToWatchlistImageDrawable, null, null, null);
                                        }
                                    }, !queued[0], false, moduleAPI);
                        } else /*User is not logged in*/ {

                            String loginDialogMessage = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.add_to_watchlist_dialog_text));
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getAddToWatchlistDialogMessage() != null) {
                                loginDialogMessage = moduleAPI.getMetadataMap().getAddToWatchlistDialogMessage();
                            }

                            String addToWatchlistDialogHeader = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.add_to_watchlist));
                            if (moduleAPI.getMetadataMap() != null
                                    && moduleAPI.getMetadataMap().getAddToWatchlistDialogHeader() != null) {
                                addToWatchlistDialogHeader = moduleAPI.getMetadataMap().getAddToWatchlistDialogHeader();
                            }

                            String loginCTA = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.sign_in_text));
                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getSignInText() != null) {
                                loginCTA = appCMSPresenter.getLocalisedStrings().getSignInText();
                            }

                            ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                    context,
                                    appCMSPresenter,
                                    context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                    context.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                    addToWatchlistDialogHeader,
                                    loginDialogMessage,
                                    loginCTA,
                                    appCMSPresenter.getLocalisedStrings().getCancelCta(), 14

                            );
                            newFragment.setOnPositiveButtonClicked(s -> {
                                openLoginDialog(appCMSPresenter);
                            });
                        }
                    } else {
                        appCMSPresenter.openErrorDialog(
                                moduleAPI.getContentData().get(0),
                                queued[0],
                                appCMSAddToWatchlistResult -> {
                                    queued[0] = !queued[0];
                                    if (queued[0]) {
                                        btn.setText(appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                                    } else {
                                        btn.setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                                    }
                                });
                    }
                }
        );

        if (moduleAPI != null &&
                moduleAPI.getContentData() != null &&
                moduleAPI.getContentData().get(0) != null &&
                moduleAPI.getContentData().get(0).getGist() != null &&
                moduleAPI.getContentData().get(0).getGist().isLiveStream()) {
            btn.setVisibility(View.INVISIBLE);
        } else {
            btn.setVisibility(VISIBLE);
        }
    }

    private void openLoginDialog(AppCMSPresenter appCMSPresenter) {
        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
        NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
        appCMSPresenter.navigateToTVPage(
                navigationUser.getPageId(),
                navigationUser.getTitle(),
                navigationUser.getUrl(),
                false,
                Uri.EMPTY,
                false,
                false,
                true,
                false, false, false);
    }


    public void refreshComponentView(Context context,
                                     TVModuleView moduleView,
                                     ModuleWithComponents module,
                                     Module moduleAPI,
                                     TVViewCreator tvViewCreator,
                                     AppCMSPresenter appCMSPresenter,
                                     Map<String, AppCMSUIKeyType> jsonValueKeyMap) {


        List<TVModuleView.ChildComponentAndView> childComponentAndViews = moduleView.getChildViewList();
        if (null != childComponentAndViews && childComponentAndViews.size() > 0) {
            for (TVModuleView.ChildComponentAndView childComponentAndView : childComponentAndViews) {
                AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponentAndView.component.getKey());
                if (componentKey == null) {
                    componentKey = PAGE_EMPTY_KEY;
                }
                switch (componentKey) {
                    case WEATHER_CDATA:
                        String tickertext = resources.getUIresource(context.getString(R.string.no_data_available));
                        if (moduleAPI != null &&
                                moduleAPI.getTickerFeed() != null &&
                                moduleAPI.getTickerFeed().getEntry() != null &&
                                moduleAPI.getTickerFeed().getEntry().getSummary() != null &&
                                moduleAPI.getTickerFeed().getEntry().getSummary().length() > 0) {
                            Spanned spanned = null;
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                spanned = Html.fromHtml(moduleAPI.getTickerFeed().getEntry().getSummary());
                            } else {
                                spanned = Html.fromHtml(moduleAPI.getTickerFeed().getEntry().getSummary(), Html.FROM_HTML_MODE_COMPACT);
                            }
                            if (spanned != null) {
                                tickertext = spanned.toString();
                            }
                        }
                        ((TextView) componentViewResult.componentView).setText(tickertext);
                        break;
                    case WEATHER_CITY_LABEL:
                        if (moduleAPI != null &&
                                moduleAPI.getTickerFeed() != null &&
                                moduleAPI.getTickerFeed().getTitle() != null) {
                            ((TextView) componentViewResult.componentView).setText(moduleAPI.getTickerFeed().getTitle().replace("Today Forecast", ""));
                        }
                        break;
                    case PAGE_THUMBNAIL_VIDEO_IMAGE_KEY:
                        String videoImageUrl = null;
                        //((ImageView)childComponentAndView.childView).setScaleType(ImageView.ScaleType.FIT_XY);
                        if (moduleAPI.getContentData() != null
                                && moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null) {
                            videoImageUrl = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getVideoImageUrl();
                        }
                        Glide.with(context)
                                .load(videoImageUrl)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                        .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                .into(((ImageView) childComponentAndView.childView));

                        break;
                    case PAGE_SHOW_IMAGE_THUMBNAIL_KEY:
                        // ((ImageView)childComponentAndView.childView).setScaleType(ImageView.ScaleType.FIT_XY);
                        if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getImageGist() != null && moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getImageGist().get_32x9() != null) {
                            Glide.with(context)
                                    .load(moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getImageGist().get_32x9())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder))
                                            .placeholder(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                    .into(((ImageView) childComponentAndView.childView));
                        }
                        break;
                    case PAGE_EXPANDED_VIEW_TITLE_KEY:
                        if (moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                !TextUtils.isEmpty(moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getTitle())) {
                            ((TextView) childComponentAndView.childView).setText(moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getTitle().trim());
                        }
                        break;

                    case PAGE_EXPANDED_VIEW_DESCRIPTION_KEY:
                        if (moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()) != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                !TextUtils.isEmpty(moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getDescription())) {
                            ((TextView) childComponentAndView.childView).setText(moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getDescription());
                        }
                        break;

                    case PAGE_TRAILER_VIDEO_PLAYER_VIEW_KEY_VALUE:
                        String trailerID = null, trailerTitle = null;
                        if (moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getShowDetails() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getShowDetails().getTrailers() != null &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getShowDetails().getTrailers().size() > 0 &&
                                moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getShowDetails().getTrailers().get(0).getId() != null) {
                            trailerID = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getShowDetails().getTrailers().get(0).getId();
                            trailerTitle = moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getShowDetails().getTrailers().get(0).getTitle();
                        }
                        if (trailerID != null) {
                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).setCarouselPlayerTask(context, appCMSPresenter, (FrameLayout) childComponentAndView.childView, trailerID, trailerTitle,null);
                        }
                        break;

                }
            }

        }
    }

    public <T> void refreshPageView(TVPageView pageView,
                                    AppCMSPageUI appCMSPageUI,
                                    AppCMSPageAPI appCMSPageAPI,
                                    Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                    AppCMSPresenter appCMSPresenter, List<T> ts,
                                    ContentDatum contentDatum) {
        if (appCMSPageUI == null) {
            return;
        }

        for (ModuleList moduleInfo : appCMSPageUI.getModuleList()) {
            ModuleList module = moduleInfo;
            try {
                if (!ts.contains(moduleInfo.getView())) {
                    if (module.getBlockName().contains("videoPlayerInfo")) {
                        Module moduleAPI = matchModuleAPIToModuleUI(module, appCMSPageAPI, jsonValueKeyMap);
                        TVModuleView moduleView = pageView.getModuleViewWithModuleId(module.getId());
                        if (moduleView != null) {
                            List<TVModuleView.ChildComponentAndView> childComponentAndViews = moduleView.getChildViewList();

                            if (null != childComponentAndViews && childComponentAndViews.size() > 0) {
                                for (TVModuleView.ChildComponentAndView childComponentAndView : childComponentAndViews) {
                                    AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponentAndView.component.getKey());
                                    System.out.println("componentKey: " + componentKey);
                                    if (componentKey == null) {
                                        componentKey = PAGE_EMPTY_KEY;
                                    }
                                    switch (componentKey) {
                                        case PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY:

                                            if (appCMSPresenter.isNewsTemplate()) {
                                                childComponentAndView.childView.setBackground(
                                                        Utils.setButtonBackgroundSelectorForNewsTemplate(appCMSPresenter.getCurrentContext(),
                                                                Utils.getFocusBorderColor(appCMSPresenter.getCurrentContext(), appCMSPresenter),
                                                                Utils.getFocusColor(appCMSPresenter.getCurrentContext(), appCMSPresenter),
                                                                childComponentAndView.component,
                                                                appCMSPresenter));
                                            }
                                            childComponentAndView.childView.setFocusable(true);
                                            Button btn = (Button) childComponentAndView.childView;
                                            Drawable addToWatchlistImageDrawable = null, removeToWatchlistImageDrawable = null;

                                            if (appCMSPresenter.isNewsTemplate() && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0 &&
                                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist() != null &&
                                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType() != null &&
                                                    moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getContentType().equalsIgnoreCase("SERIES")) {
                                                btn.setVisibility(GONE);
                                            } else {
                                                //btn.setVisibility(VISIBLE);
                                                if (jsonValueKeyMap.get(childComponentAndView.component.getKey()) == PAGE_ADD_TO_WATCHLIST_WITH_ICON_KEY) {
                                                    addToWatchlistImageDrawable = ContextCompat.getDrawable(appCMSPresenter.getCurrentContext(), R.drawable.ic_add_to_watchlist);
                                                    Utils.setImageColorFilter(null, addToWatchlistImageDrawable, appCMSPresenter);

                                                    removeToWatchlistImageDrawable = ContextCompat.getDrawable(appCMSPresenter.getCurrentContext(), R.drawable.ic_remove_from_watchlist);
                                                    Utils.setImageColorFilter(null, removeToWatchlistImageDrawable, appCMSPresenter);
                                                }
                                                updateAddToWatchlistButtonStatus(appCMSPresenter.getCurrentContext(), appCMSPresenter, btn, moduleAPI, addToWatchlistImageDrawable, removeToWatchlistImageDrawable);
                                            }
                                            break;
                                        case PAGE_START_WATCHING_FROM_BEGINNING_BUTTON_KEY: {
                                            Button startFromBeginning = (Button) componentViewResult.componentView;
                                            View componentView = componentViewResult.componentView;
                                            componentView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                            startFromBeginning.setId(R.id.btn_start_watching_from_beginning);
                                            startFromBeginning.setVisibility(View.GONE);
                                            if (appCMSPresenter.isUserLoggedIn()) {
                                                if (null != moduleAPI.getContentData() && moduleAPI.getContentData().size() > 0) {
                                                    String videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                                                    if (videoId == null) {
                                                        videoId = moduleAPI.getContentData().get(0).getGist().getId();
                                                    }
                                                    appCMSPresenter.getUserVideoStatus(
                                                            videoId,
                                                            userVideoStatusResponse -> {
                                                                if (null != userVideoStatusResponse) {
                                                                    Log.d(TAG, "time = " + userVideoStatusResponse.getWatchedTime());
                                                                    if (userVideoStatusResponse.getWatchedTime() > 0 && userVideoStatusResponse.getWatchedPercentage() < 98) {
                                                                        startFromBeginning.setVisibility(VISIBLE);
                                                                        startFromBeginning.setText(appCMSPresenter.getLocalisedStrings().getStartFromBeginningText());
                                                                        startFromBeginning.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_play_from_beginning_24, 0, 0, 0);
                                                                        startFromBeginning.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                                                                        startFromBeginning.setAllCaps(false);

                                                                        componentView.setEnabled(true);
                                                                        componentView.setFocusable(true);
                                                                        componentView.requestFocus();
                                                                    } else {
                                                                        startFromBeginning.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            });
                                                }

                                            } else {
                                                startFromBeginning.setVisibility(View.GONE);
                                            }

                                        }
                                        break;
                                        case PAGE_START_WATCHING_BUTTON_KEY:
                                            if (appCMSPresenter.isUserLoggedIn()) {
                                                if (null != moduleAPI && null != moduleAPI.getContentData()
                                                        && moduleAPI.getContentData().size() > 0) {
                                                    String videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                                                    if (videoId == null) {
                                                        videoId = moduleAPI.getContentData().get(0).getGist().getId();
                                                    }
                                                    appCMSPresenter.getUserVideoStatus(
                                                            videoId,
                                                            userVideoStatusResponse -> {
                                                                if (null != userVideoStatusResponse) {
                                                                    Log.d(TAG, "time = " + userVideoStatusResponse.getWatchedTime());

                                                                    if (userVideoStatusResponse.getWatchedTime() > 0) {
                                                                        ((Button) childComponentAndView.childView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.resume_watching)));

                                                                        if (userVideoStatusResponse.getWatchedPercentage() >= 98) {
                                                                            ((Button) childComponentAndView.childView).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.start_watching)));
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }
                                            }

                                            break;

                                        case PAGE_WATCHLIST_TITLE_LABEL:
                                            ((TextView) childComponentAndView.childView).setText(contentDatum.getGist().getTitle());
                                            break;

                                        case PAGE_WATCHLIST_DESCRIPTION_LABEL:
                                            ((TextView) childComponentAndView.childView).setText(contentDatum.getGist().getDescription());
                                            break;

                                        case PAGE_LOGIN_TEXT_KEY:
                                            if (appCMSPresenter.isUserLoggedIn()) {
                                                ((TextView) childComponentAndView.childView).setVisibility(GONE);
                                                Button rentButton = pageView.findViewById(R.id.btn_rent_option);
                                                Button buyButton = pageView.findViewById(R.id.btn_buy_option);
                                                if (rentButton != null) {
                                                    rentButton.setNextFocusUpId(View.NO_ID);
                                                }
                                                if (buyButton != null) {
                                                    buyButton.setNextFocusUpId(View.NO_ID);
                                                }
                                            }
                                            break;

                                        case PAGE_PARENT_LINEAR_VIEW:
                                            for (Component component : childComponentAndView.component.getComponents()) {
                                                try {
                                                    AppCMSUIKeyType key = jsonValueKeyMap.get(component.getKey());
                                                    if (componentKey == null) {
                                                        componentKey = PAGE_EMPTY_KEY;
                                                    }
                                                    if (key == PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY) {
                                                        if (appCMSPresenter.isUserLoggedIn() || appCMSPresenter.isUserLoggedInByTVProvider()) {
                                                            Button button = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_add_to_watchlist);
                                                            button.setVisibility(VISIBLE);
                                                            updateAddToWatchlistButtonStatus(appCMSPresenter.getCurrentContext(), appCMSPresenter, button, moduleAPI, null, null);
                                                        }
                                                    } else if (key == RENT_OPTION_BTN_KEY) {
                                                        Button rentButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_rent_option);
                                                        if (isTVODButtonEnabled(appCMSPresenter.getCurrentContext(), appCMSPresenter, moduleAPI, moduleAPI.getContentData().get(0), true) /*&& !isContentPlayable(appCMSPresenter, moduleAPI)*/) {
                                                            rentButton.setVisibility(VISIBLE);
                                                        } else {
                                                            rentButton.setVisibility(GONE);
                                                        }
                                                    } else if (key == PAGE_PLAN_BUY_BUTTON_KEY) {
                                                        Button buyButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_buy_option);
                                                        if (isTVODButtonEnabled(appCMSPresenter.getCurrentContext(), appCMSPresenter, moduleAPI, moduleAPI.getContentData().get(0), false)) {
                                                            buyButton.setVisibility(VISIBLE);
                                                        } else {
                                                            buyButton.setVisibility(GONE);
                                                        }
                                                    } else if (key == AppCMSUIKeyType.PAGE_SHOW_START_WATCHING_BUTTON_KEY) {
                                                        if (isContentPlayable(appCMSPresenter, moduleAPI)) {
                                                            Button startWatchingButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_start_watching);
                                                            startWatchingButton.setVisibility(VISIBLE);
                                                        }
                                                    } else if (key == AppCMSUIKeyType.PAGE_START_WATCHING_BUTTON_KEY) {
                                                        if (isContentPlayable(appCMSPresenter, moduleAPI)) {
                                                            Button startWatchingButton = pageView.findViewById(R.id.btn_start_watching);
                                                            startWatchingButton.setVisibility(VISIBLE);
                                                            if (appCMSPresenter.isUserLoggedIn()) {
                                                                if (null != moduleAPI && null != moduleAPI.getContentData()
                                                                        && moduleAPI.getContentData().size() > 0) {
                                                                    String videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                                                                    if (videoId == null) {
                                                                        videoId = moduleAPI.getContentData().get(0).getGist().getId();
                                                                    }
                                                                    appCMSPresenter.getUserVideoStatus(
                                                                            videoId,
                                                                            userVideoStatusResponse -> {
                                                                                if (null != userVideoStatusResponse) {
                                                                                    Log.d(TAG, "time = " + userVideoStatusResponse.getWatchedTime());

                                                                                    if (userVideoStatusResponse.getWatchedTime() > 0) {
                                                                                        startWatchingButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.resume_watching)));

                                                                                        if (userVideoStatusResponse.getWatchedPercentage() >= 98) {
                                                                                            startWatchingButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.start_watching)));
                                                                                        }
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (key == AppCMSUIKeyType.PAGE_BUNDLE_START_WATCHING_BUTTON_KEY) {
                                                            if (isContentPlayable(appCMSPresenter, moduleAPI)) {
                                                                Button startWatchingBundleButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_bundle_start_watching);
                                                                startWatchingBundleButton.setVisibility(VISIBLE);
                                                            }
                                                        } else if (key == AppCMSUIKeyType.PAGE_PLAN_BECOME_MEMBER_BUTTON_KEY) {
                                                            Button button = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_become_a_member);
                                                            if (!appCMSPresenter.isUserSubscribed() && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0
                                                                    && contentTypeChecker.checkPlanExist(moduleAPI.getContentData().get(0).getMonetizationModels(), appCMSPresenter.getCurrentContext().getString(R.string.pricing_model_SVOD))
                                                                    && !isContentPlayable(appCMSPresenter, moduleAPI)) {
                                                                button.setVisibility(VISIBLE);
                                                            } else {
                                                                button.setVisibility(GONE);
                                                            }
                                                        } else if (key == PAGE_LINK_YOUR_ACOOUNT_WITH_TV_PROVIDER_BTN_KEY) {
                                                            Button activateDeviceButton = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_activate_device);
                                                            if (appCMSPresenter.isUserLoggedInByTVProvider()) {
                                                                activateDeviceButton.setVisibility(VISIBLE);
                                                            } else {
                                                                activateDeviceButton.setVisibility(GONE);
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                }
                                            }
                                            break;

                                    }
                                }
                            }
                        }
                    } else if (module.getBlockName().contains("userManagement04")) {
                        TVModuleView moduleView = pageView.getModuleViewWithModuleId(module.getId());
                        if (moduleView.findViewById(R.id.PAGE_GRID_VIEW_COLLECTIONGRID_KEY) instanceof RecyclerView) {
                            RecyclerView recyclerView = moduleView.findViewById(R.id.PAGE_GRID_VIEW_COLLECTIONGRID_KEY);
                            if (appCMSPresenter.getSelectedGenreString() != null) {
                                List<String> userRecommendedList = Arrays.asList(appCMSPresenter.getPersonalizedGenresPreference().split("[|]"));
                                AppCMSTVSeeAllAdapter adapter = (AppCMSTVSeeAllAdapter) recyclerView.getAdapter();
                                if (adapter != null) {
                                    adapter.clear();
                                    if (userRecommendedList != null && userRecommendedList.size() > 0) {
                                        List<ContentDatum> contentData = new ArrayList<>();
                                        for (int i = 0; i < userRecommendedList.size(); i++) {
                                            ContentDatum childContentDatum = new ContentDatum();
                                            childContentDatum.setTitle(userRecommendedList.get(i));
                                            childContentDatum.setId("personalizationView");
                                            contentData.add(childContentDatum);
                                        }
                                        adapter.addAll(contentData);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static class ComponentViewResult {
        public View componentView;
        public OnInternalEvent onInternalEvent;
        public boolean useMarginsAsPercentagesOverride;
        public boolean useWidthOfScreen;
    }

    public CustomTVVideoPlayerView playerView(Context context, AppCMSPresenter appCMSPresenter) {
        CustomTVVideoPlayerView videoPlayerView = new CustomTVVideoPlayerView(context,
                appCMSPresenter);
        videoPlayerView.init(context);
        videoPlayerView.getPlayerView().hideController();
        return videoPlayerView;
    }

    public boolean isContentPlayable(AppCMSPresenter appCMSPresenter, Module moduleApi) {
        try {
            if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                return true;
            }
            List<MonetizationModels> monetizationModelsList = moduleApi.getContentData().get(0).getMonetizationModels();
             if (contentTypeChecker.isContentTVEMonetization(monetizationModelsList) /*&& appCMSPresenter.isUserLoggedInByTVProvider()*/
                    && contentTypeChecker.isContentSVODMonetization(monetizationModelsList) /*&& appCMSPresenter.isUserSubscribed()*/
                    && contentTypeChecker.isContentTVODMonetization(monetizationModelsList) /*&& isContentPurchased(appCMSPresenter, moduleApi) */
                    && (appCMSPresenter.isUserLoggedInByTVProvider() || appCMSPresenter.isUserSubscribed() || appCMSPresenter.isUserSubscribed())) {
                return true;
            } else if (contentTypeChecker.isContentTVEMonetization(monetizationModelsList) /*&& appCMSPresenter.isUserLoggedInByTVProvider()*/
                    && contentTypeChecker.isContentSVODMonetization(monetizationModelsList)/* && appCMSPresenter.isUserSubscribed()*/
                     && (appCMSPresenter.isUserLoggedInByTVProvider() || appCMSPresenter.isUserSubscribed())) {
                return true;
            } else if (contentTypeChecker.isContentTVEMonetization(monetizationModelsList) /*&& appCMSPresenter.isUserLoggedInByTVProvider()*/
                     && contentTypeChecker.isContentTVODMonetization(monetizationModelsList) /*&& isContentPurchased(appCMSPresenter, moduleApi) */
                     && (appCMSPresenter.isUserLoggedInByTVProvider() || isContentPurchased(appCMSPresenter, moduleApi))) {
                 return true;
            } else if (contentTypeChecker.isContentSVODMonetization(monetizationModelsList) /*&& appCMSPresenter.isUserSubscribed()*/
                     && contentTypeChecker.isContentTVODMonetization(monetizationModelsList) /*&& isContentPurchased(appCMSPresenter, moduleApi)*/
                     && (appCMSPresenter.isUserSubscribed() || isContentPurchased(appCMSPresenter, moduleApi))) {
                 return true;
            } else if (contentTypeChecker.isContentSVODMonetization(monetizationModelsList) && appCMSPresenter.isUserSubscribed()){
                return true;
            } else if (contentTypeChecker.isContentTVODMonetization(monetizationModelsList) && isContentPurchased(appCMSPresenter, moduleApi)) {
                return true;
            } else if (contentTypeChecker.isContentTVEMonetization(monetizationModelsList) && appCMSPresenter.isUserLoggedInByTVProvider()) {
                 return true;
            } else if (contentTypeChecker.isContentFREEMonetization(monetizationModelsList)
                     || contentTypeChecker.isContentAVODMonetization(monetizationModelsList)) {
                 return true;
            }
            System.out.println("isContentPlayable "+contentTypeChecker.isContentSVODMonetization(monetizationModelsList));
        }catch (Exception ex){
            System.out.println("isContentPlayable "+ex.toString());
            ex.printStackTrace();
        }
        return false;
    }

    public boolean isContentPurchased(AppCMSPresenter appCMSPresenter, Module moduleApi) {
        boolean isEveryContentPurchased = false;
        boolean isSeriesPurchased = false;
        boolean isAllSeasonsPurchased = false;
        boolean isAllEpisodesPurchased = false;
        try {
            ContentDatum contentDatum = moduleApi.getContentData().get(0);
            if (Utils.isContentTypeVideo(appCMSPresenter.getCurrentContext(),contentDatum)) {
                if (moduleApi.getContentData().get(0).getSeriesId() != null) {
                    isSeriesPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getSeriesId());
                }
                if (moduleApi.getContentData().get(0).getSeasonId() != null) {
                    isAllSeasonsPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getSeasonId());
                }
                if (moduleApi.getContentData().get(0).getGist().getId() != null) {
                    isAllEpisodesPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getGist().getId());
                }
                isEveryContentPurchased = isSeriesPurchased || isAllSeasonsPurchased || isAllEpisodesPurchased;
            } else if (Utils.isContentTypeBundle(appCMSPresenter.getCurrentContext(),contentDatum)) {
                if (moduleApi.getContentData().get(0).getGist().getBundleList() != null) {
                    isEveryContentPurchased = contentTypeChecker.isBundlePurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getGist().getBundleList(),moduleApi.getContentData().get(0).getGist().getId());
                }
            } else if (Utils.isContentTypeSeries(appCMSPresenter.getCurrentContext(),contentDatum)) {
                if (moduleApi.getContentData().get(0).getGist().getId() != null) {
                    isSeriesPurchased = contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getGist().getId());
                }
                if (moduleApi.getContentData() != null && moduleApi.getContentData().size() > 0 && moduleApi.getContentData().get(0).getSeason() != null) {
                    isAllSeasonsPurchased = contentTypeChecker.isAllSeasonsPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getSeason());
                    isAllEpisodesPurchased = contentTypeChecker.isAllEpisodesPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), moduleApi.getContentData().get(0).getSeason());
                }
                isEveryContentPurchased = isSeriesPurchased || isAllSeasonsPurchased || isAllEpisodesPurchased;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isEveryContentPurchased;
    }

    private boolean isTVODButtonEnabled(Context context , AppCMSPresenter appCMSPresenter, Module moduleAPI, ContentDatum contentDatum, boolean isRent){
        try {
            if ((Utils.isContentTypeVideo(context, contentDatum) || Utils.isContentTypeBundle(context, contentDatum))
                    && !isContentPlayable(appCMSPresenter, moduleAPI)
                    && ((contentTypeChecker.checkPlanExist(contentDatum.getMonetizationModels(), context.getString(R.string.pricing_model_TVOD))
                    && contentDatum.getTvodPricing() != null
                    && ((isRent && contentDatum.getTvodPricing().getRentAmount() != 0)
                    || (!isRent && contentDatum.getTvodPricing().getBuyAmount() != 0)))
                    || !contentTypeChecker.fetchPlanPrice(Utils.isContentTypeBundle(context, contentDatum)
                    ? contentDatum.getBundlePlans() : contentDatum.getSubscriptionPlans(),
                    isRent ? context.getString(R.string.rent) : context.getString(R.string.buy)).contains("N/A"))) {
                return true;
            }else if (!isContentPlayable(appCMSPresenter, moduleAPI)
                    && contentTypeChecker.checkPlanExist(contentDatum.getMonetizationModels(), context.getString(R.string.pricing_model_TVOD))
                    && ((isRent && contentTypeChecker.isRentAvailableForSeriesOrSeasonOrEpisode(contentDatum))
                    || (!isRent && contentTypeChecker.isPurchaseAvailableForSeriesOrSeasonOrEpisode(contentDatum)))) {
                return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void updateTVODButtonText(Context context, AppCMSPresenter appCMSPresenter, ContentDatum contentDatum, Button tvodButton, boolean isRent) {
        StringBuilder buyPrice = new StringBuilder();
        try {
            if ((Utils.isContentTypeVideo(context, contentDatum) || Utils.isContentTypeBundle(context, contentDatum))) {
                if(contentDatum.getTvodPricing() != null){
                    buyPrice.append(isRent ? appCMSPresenter.getLocalisedStrings().getRentLabel() : appCMSPresenter.getLocalisedStrings().getBuyLabel())
                            .append(" ")
                            .append(Currency.getInstance(contentDatum.getTvodPricing().getCurrencyCode()).getSymbol())
                            .append(" ")
                            .append(isRent ? contentDatum.getTvodPricing().getRentAmount() : contentDatum.getTvodPricing().getBuyAmount());
                }else if(contentDatum.getBundlePlans() != null && contentDatum.getBundlePlans().size() > 0){
                    buyPrice.append(isRent ? appCMSPresenter.getLocalisedStrings().getRentLabel() : appCMSPresenter.getLocalisedStrings().getBuyLabel())
                            .append(" ")
                            .append(contentTypeChecker.fetchPlanPrice(contentDatum.getBundlePlans(),
                                    isRent ? context.getString(R.string.rent) : context.getString(R.string.buy)));
                }else{
                    buyPrice.append(isRent ? appCMSPresenter.getLocalisedStrings().getRentLabel() : appCMSPresenter.getLocalisedStrings().getBuyLabel())
                            .append(" ")
                            .append(contentTypeChecker.fetchPlanPrice(contentDatum.getSubscriptionPlans(),
                                    isRent ? context.getString(R.string.rent) : context.getString(R.string.buy)));
                }

            } else {
                if (Utils.isContentTypeSeries(context, contentDatum)) {
                    buyPrice.append(isRent ? appCMSPresenter.getLocalisedStrings().getRentOptionsLabel() : appCMSPresenter.getLocalisedStrings().getBuyOptionsLabel());
                } else {
                    buyPrice.append(appCMSPresenter.getLocalisedStrings().getBuyLabel())
                            .append(" ")
                            .append(contentTypeChecker.fetchPlanPrice(contentDatum.getSubscriptionPlans(),
                                    isRent ? context.getString(R.string.rent) : context.getString(R.string.buy)));
                }
            }
            tvodButton.setText(buyPrice);
            if(buyPrice.toString().contains("N/A"))
                tvodButton.setVisibility(GONE);
            else
                tvodButton.setVisibility(VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
            tvodButton.setText(isRent ? appCMSPresenter.getLocalisedStrings().getRentLabel() : appCMSPresenter.getLocalisedStrings().getBuyLabel());
        }
    }

    CustomTVVideoPlayerView mcustomTVVideoPlayerView=null;
    public void setDefaultPlayerData(CustomTVVideoPlayerView customVideoPlayerView, Module moduleAPI,final String promoID,final String videoId,final String videoTitle,AppCMSPresenter appCMSPresenter){
        mcustomTVVideoPlayerView=customVideoPlayerView;
        if(mcustomTVVideoPlayerView!= null) {
            mcustomTVVideoPlayerView.setTabIsClicked(true);
                mcustomTVVideoPlayerView.setVideoUri(promoID,
                        videoTitle, moduleAPI.getContentData().get(appCMSPresenter.getSelectedSchedulePosition()), true,true);
                mcustomTVVideoPlayerView.setTrailerCompletedCallback(new TrailerCompletedCallback() {
                    @Override
                    public void videoCompleted() {
                        {
                            mcustomTVVideoPlayerView.setVideoUri(videoId, videoTitle, moduleAPI.getContentData().get(appCMSPresenter.getSelectedSchedulePosition()), false,false);
                        }
                    }

                    @Override
                    public void videoStarted() {
                    }
                });
            }
            else {
                mcustomTVVideoPlayerView.setVideoUri(videoId, videoTitle, moduleAPI.getContentData().get(appCMSPresenter.getSelectedSchedulePosition()), false,false);
            }
        }
    private TrailerCompletedCallback trailerCompletedCallback;
    public void setTrailerCompletedCallback(TrailerCompletedCallback trailerCompletedCallback) {
        this.trailerCompletedCallback = trailerCompletedCallback;
    }
}

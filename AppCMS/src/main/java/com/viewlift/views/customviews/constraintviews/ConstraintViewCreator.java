package com.viewlift.views.customviews.constraintviews;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Barrier;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.gms.common.util.Strings;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.viewlift.CMSColorUtils;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.EquipmentElement;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.BaseInterface;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.CreditBlock;
import com.viewlift.models.data.appcms.api.ExpandableItem;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.api.VideoAssets;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.history.SeriesHistory;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.android.NavigationFooter;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.EmailConsent;
import com.viewlift.models.data.appcms.ui.main.EmailConsentMessage;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.models.data.appcms.user.Question;
import com.viewlift.models.data.appcms.user.UserDescriptionResponse;
import com.viewlift.models.network.background.tasks.GetAppCMSStreamingInfoAsyncTask;
import com.viewlift.models.network.background.tasks.SearchQuery;
import com.viewlift.offlinedrm.FetchOffineDownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CardValidator;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.utils.FileUtils;
import com.viewlift.utils.IdUtils;
import com.viewlift.utils.ShowDetailsPromoHandler;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.adapters.AppCMSCarouselItemAdapter;
import com.viewlift.views.adapters.AppCMSConstraintViewAdapter;
import com.viewlift.views.adapters.AppCMSDownloadQualityAdapter;
import com.viewlift.views.adapters.AppCMSSavedCardsAdapter;
import com.viewlift.views.adapters.AppCMSTraySeasonItemAdapter;
import com.viewlift.views.adapters.AppCMSTrayViewAdapter;
import com.viewlift.views.adapters.AppCMSUserPagesAdapter;
import com.viewlift.views.adapters.AppCMSUserWatHisDowAdapter;
import com.viewlift.views.adapters.AppCMSViewAdapter;
import com.viewlift.views.adapters.BankListAdapter;
import com.viewlift.views.adapters.ContentCarouselAdapter;
import com.viewlift.views.adapters.EquipmentGridAdapter;
import com.viewlift.views.adapters.ExpandableGridAdapter;
import com.viewlift.views.adapters.PaginationAdapter;
import com.viewlift.views.adapters.RecentClassesAdapter;
import com.viewlift.views.adapters.SearchSuggestionsAdapter;
import com.viewlift.views.adapters.SwipeView;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CategoryCompTray07;
import com.viewlift.views.customviews.CreditBlocksView;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.DotSelectorView;
import com.viewlift.views.customviews.DotSelectorView03;
import com.viewlift.views.customviews.DownloadComponent;
import com.viewlift.views.customviews.LikeComponent;
import com.viewlift.views.customviews.ListWithAdapter;
import com.viewlift.views.customviews.MiniPlayerView;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.PlanFeaturesLayout;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.ViewCreatorTitleLayoutListener;
import com.viewlift.views.customviews.expandable.ExpandableLayout;
import com.viewlift.views.customviews.listdecorator.SeparatorDecoration;
import com.viewlift.views.customviews.moduleview.constraintview.SeasonTabModule;
import com.viewlift.views.customviews.plans.SubscriptionMetaDataView;
import com.viewlift.views.customviews.plans.ViewPlansMetaDataView;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;
import com.viewlift.views.dialog.CustomShape;
import com.viewlift.views.fragments.AppCMSBillingHistoryFragment;
import com.viewlift.views.listener.PaginationScrollListener;
import com.viewlift.views.listener.TrailerCompletedCallback;
import com.viewlift.views.rxbus.AppBus;
import com.viewlift.views.utilities.ImageUtils;
import com.zendesk.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.viewlift.models.data.appcms.downloads.DownloadStatus.STATUS_RUNNING;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.CARD_CHECKBOX_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AUTOPLAY_TOGGLE_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BADGE_DETAIL_PAGE_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BRAND_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_BADGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_DOWNLOAD_VIA_CELLULAR_NETWORK_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EDIT_PROFILE_CHAIN_BACKGROUND_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EMPTY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_FILL_BACKGROUND;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_PLAYER_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SD_CARD_FOR_DOWNLOADS_TOGGLE_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_CLOSED_CAPTION_TOGGLE_SWITCH_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_PARENTAL_CONTROL_SWITCH;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_SUBSCRIPTION_GROUP_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SHOW_DETAIL_04_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_LEFT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_RIGHT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_BADGE_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_VIDEO_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TRAY_08_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_DETAIL_CLASS_PLAY_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIEW_DETAILS_BACKGROUND;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIEW_TIMER_BACKGROUND;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.TRAY_ARTICLE_SEPARATOR_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_AUTHENTICATION_14;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_CARD_PAYMENT_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_CAROUSEL_07;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_03;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_HISTORY_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_HISTORY_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_HISTORY_04;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_NEWS_TRAY_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_07;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_11;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_WATCHLIST_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_WATCHLIST_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_WATCHLIST_03;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_WATCHLIST_04;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_START;
import static com.viewlift.utils.CommonUtils.GOOGLE_PAY_PACKAGE_NAME;
import static com.viewlift.utils.CommonUtils.getColor;
import static com.viewlift.utils.CommonUtils.isSiteOTPEnabled;
import static com.viewlift.utils.CommonUtils.isValidPhoneNumber;
import static com.viewlift.views.customviews.BaseView.convertDpToPixel;
import static com.viewlift.views.customviews.BaseView.getDeviceHeight;
import static com.viewlift.views.customviews.BaseView.getDeviceWidth;
import static com.viewlift.views.customviews.BaseView.getFontSize;
import static com.viewlift.views.customviews.BaseView.getViewHeight;
import static com.viewlift.views.customviews.BaseView.getViewWidth;
import static com.viewlift.views.customviews.BaseView.isTablet;
import static com.viewlift.views.customviews.ViewCreator.setTypeFace;

public class ConstraintViewCreator {

    private ViewCreator.ComponentViewResult componentViewResult;
    private Module moduleAPIPub;
    private AppCMSAndroidModules appCMSAndroidModule;
    static Boolean traierPlayComplete = false;
    private boolean isAdded, isDownloaded, isPending;
    ContentDatum finalEpisodeData;
    ImageView thumbnailImage;
    ConstraintLayout.LayoutParams lpViewNoVisibility = new ConstraintLayout.LayoutParams(0, 0);
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    @Inject
    public ConstraintViewCreator(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
    }

    public ConstraintCollectionGridItemView createConstraintCollectionGridItemView(final Context context,
                                                                                   final Layout parentLayout,
                                                                                   final boolean useParentLayout,
                                                                                   final Component component,
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
                                                                                   AppCMSUIKeyType viewTypeKey, String blockName,
                                                                                   MetadataMap metadataMap) {
        if (componentViewResult == null) {
            componentViewResult = new ViewCreator.ComponentViewResult();
        }
        ConstraintCollectionGridItemView collectionGridItemView = new ConstraintCollectionGridItemView(context,
                parentLayout,
                useParentLayout,
                component,
                moduleAPI.getId(),
                defaultWidth,
                defaultHeight,
                jsonValueKeyMap,
                createRoundedCorners,
                viewTypeKey,
                blockName);
        collectionGridItemView.setId(R.id.collectionGridItemView);

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();

        int size = component.getComponents().size();

        for (int i = 0; i < size; i++) {
            Component childComponent = component.getComponents().get(i);
            if (component.getSettings() != null && component.getSettings().isNoInfo()
                    && viewType.contains("carousel")
                    && !(childComponent.getKey().equalsIgnoreCase("carouselImage")
                    || childComponent.getKey().equalsIgnoreCase("carouselBadgeImage"))) {
                continue;
            }
            createComponentView(context,
                    childComponent,
                    parentLayout,
                    moduleAPI,
                    appCMSAndroidModules,
                    null,
                    settings,
                    jsonValueKeyMap,
                    gridElement,
                    viewType,
                    component.getId(),
                    collectionGridItemView,
                    blockName);

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }
            if (viewType != null &&
                    viewType.equalsIgnoreCase(PAGE_EVENT_CAROUSEL_MODULE_KEY.toString())) {
                childComponent.setView(viewType);
            }

            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                ConstraintCollectionGridItemView.ItemContainer itemContainer =
                        new ConstraintCollectionGridItemView.ItemContainer.Builder()
                                .childView(componentView)
                                .component(childComponent)
                                .build();
                collectionGridItemView.addChild(itemContainer);
            }
        }
        for (int i = 0; i < collectionGridItemView.getNumberOfChildren(); i++) {
            ContentDatum data = null;
            if (moduleAPI != null && moduleAPI.getContentData() != null
                    && i < moduleAPI.getContentData().size()
                    && moduleAPI.getContentData().get(i) != null) {
                data = moduleAPI.getContentData().get(i);
            }
            Component childComponent = collectionGridItemView.matchComponentToView(collectionGridItemView.getChild(i));
            setComponentViewRelativePosition(context,
                    collectionGridItemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    viewType,
                    childComponent,
                    collectionGridItemView, blockName, null, metadataMap);
        }

        return collectionGridItemView;
    }

    int[] chainViews;
    float[] chainWeights = {0, 0};

    public void createComponentView(final Context context,
                                    final Component component,
                                    final Layout parentLayout,
                                    final Module moduleAPI,
                                    final AppCMSAndroidModules appCMSAndroidModules,
                                    @Nullable final PageView pageView,
                                    final Settings settings,
                                    Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                    boolean gridElement,
                                    final String viewType,
                                    String moduleId,
                                    ConstraintCollectionGridItemView collectionGridItemView, String blockName) {
        componentViewResult.componentView = null;
        componentViewResult.useMarginsAsPercentagesOverride = true;
        componentViewResult.useWidthOfScreen = false;
        //componentViewResult.shouldHideModule = false;
        componentViewResult.addToPageView = false;
        componentViewResult.shouldHideComponent = false;
        componentViewResult.onInternalEvent = null;
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());

        if (componentType == null) {
            componentType = PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());

        if (componentKey == null) {
            componentKey = PAGE_EMPTY_KEY;
        }

        if (moduleId == null && moduleAPI != null) {
            moduleId = moduleAPI.getId();
        }

        if (moduleAPI != null) {
            moduleAPIPub = moduleAPI;
        }
        if (appCMSAndroidModules != null) {
            appCMSAndroidModule = appCMSAndroidModules;
        }

        String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();

        AppCMSUIKeyType moduleType = jsonValueKeyMap.get(viewType);

        if (moduleType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }
        int viewWidth = (int) getViewWidth(context, component.getLayout(), ViewGroup.LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(context, component.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewWidth == -1)
            viewWidth = BaseView.getDeviceWidth();
        ConstraintLayout.LayoutParams zeroWidthParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams widthHeightParams = new ConstraintLayout.LayoutParams(viewWidth, viewHeight);

        String ratio = BaseView.getViewRatio(context, component.getLayout(), "16:9");
        switch (componentType) {
            case PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY: {
                componentViewResult.componentView = new DownloadComponent(context, appCMSPresenter, component, moduleAPI.getContentData().get(0), jsonValueKeyMap);
            }
            break;
            case PAGE_PROGRESS_VIEW_KEY: {
                if (appCMSPresenter.isUserLoggedIn()) {
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
//                        canvas.clipRect(0, 0, toX, canvas.getHeight());
                            getBackground().draw(canvas);
                            canvas.restoreToCount(count);
                        }
                    };

                    ((ProgressBar) componentViewResult.componentView).getProgressDrawable()
                            .setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    componentViewResult.componentView.setBackgroundColor(color & 0x44ffffff);
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0, 10);
                    componentViewResult.componentView.setLayoutParams(lp);
                }
            }
            break;
            case PAGE_BARRIER_KEY: {
                componentViewResult.componentView = new Barrier(context);
            }
            break;
            case PAGE_BUTTON_KEY: {
                componentViewResult.componentView = new Button(context);
                ((Button) componentViewResult.componentView).setTextSize(getFontSize(context, component.getLayout()));
                ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, componentViewResult.componentView);
                if (component.getText() != null) {
                    ((Button) componentViewResult.componentView).setText(component.getText());
                }
                if (component.getTextColor() != null) {
                    ((Button) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                }
                switch (componentKey) {
                    case PAGE_PLAY_KEY:
                    case PAGE_PLAY_IMAGE_KEY:
                    case PAGE_WATCHLIST_DELETE_ITEM_BUTTON:
                    case PAGE_EXPAND_DETAILS_KEY:
                    case PAGE_GRID_OPTION_KEY:
                        componentViewResult.componentView = new ImageButton(context);
                        break;
                    case PAGE_PLAN_PURCHASE_BUTTON_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                }
            }
            break;
            case PAGE_LABEL_KEY:
            case PAGE_TEXTVIEW_KEY: {
                componentViewResult.componentView = new TextView(context);
                switch (componentKey) {
                    case PAGE_PLAN_TITLE_KEY:
                        //componentViewResult.componentView.setLayoutParams(widthHeightParams);
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                    case PAGE_ARTICLE_TITLE_KEY:
                    case PAGE_ARTICLE_DESCRIPTION_KEY:
                    case WALLET_TITLE_KEY:
                    case CARD_TITLE_ERROR_KEY:
                    case UPI_TITLE_ERROR_KEY:
                    case NETBANKING_TITLE_ERROR_KEY:
                    case WALLET_OFFER_TEXT:
                    case PAGE_HOME_TITLE_THUMBNAIL:
                    case PAGE_THUMBNAIL_DESCRIPTION_KEY:
                    case PAGE_HOME_TITLE_DESCRIPTION:
                    case PAGE_HOME_DURATION_CAROUSEL:
                    case PAGE_THUMBNAIL_TIME_AND_CATEGORY:
                    case PAGE_THUMBNAIL_BRAND_AND_TITLE:
                    case PAGE_LIVE_SCHEDULE_ITEM_TITLE_KEY:
                    case PAGE_NO_CLASSES_SCHEDULED:
                    case PAGE_EDIT_PROFILE_USERNAME_KEY:
                    case PAGE_EDIT_PROFILE_USERNAME_VALUE_KEY:
                    case PAGE_EDIT_PROFILE_PUBLIC_PROFILE_KEY:
                    case PAGE_EDIT_PROFILE_PRIVATE_PROFILE_KEY:
                    case PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY:
                    case PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY:
                    case PAGE_THUMBNAIL_TITLE_KEY:
                    case PAGE_EPISODE_TITLE_KEY:
                    case PAGE_CAROUSEL_TITLE_KEY:
                    case PAGE_CAROUSEL_INFO_KEY:
                    case PAGE_API_TITLE:
                    case PAGE_API_DESCRIPTION:
                    case PAGE_API_EPISODE_DESCRIPTION:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                }
            }// End of case PAGE_LABEL_KEY :
            break;
            case PAGE_LINEAR_LAYOUT_KEY: {
                componentViewResult.componentView = new CategoryCompTray07(context, appCMSPresenter, moduleAPI.getContentData().get(0));
                switch (componentKey) {
                    case CATEGORY_TRAY_LAYOUT_07:
                        ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.VERTICAL);
                        break;
                }
            }
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
                                    null,
                                    moduleAPI,
                                    jsonValueKeyMap,
                                    appCMSPresenter,
                                    settings);
                        }
                        if (appCMSPresenter.isMOTVApp()) {
                            componentViewResult.componentView = new ViewPlansMetaDataView(context,
                                    component,
                                    component.getLayout(),
                                    null,
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
                                null,
                                moduleAPI,
                                jsonValueKeyMap,
                                appCMSPresenter,
                                settings,
                                appCMSAndroidModules, blockName);
                    }
                }
            }
            break;
            case PAGE_EMAIL_CONSENT_CHECKBOX_KEY: {
                componentViewResult.componentView = new AppCompatCheckBox(context);
                AppCompatCheckBox checkBox = ((AppCompatCheckBox) componentViewResult.componentView);
                checkBox.setId(R.id.emailConsentCheckbox);
                checkBox.setVisibility(View.INVISIBLE);

                String countryCode = Utils.getCountryCode();
                EmailConsent emailConsent = appCMSPresenter.getAppCMSMain().getEmailConsent();
                if (emailConsent != null && emailConsent.isEnableEmailConsent()) {
                    EmailConsentMessage emailConsentMessage = emailConsent.getLocalizationMap().get(countryCode);
                    if (!TextUtils.isEmpty(emailConsent.getDefaultMessage().trim()) || (emailConsentMessage != null && !TextUtils.isEmpty(emailConsentMessage.getMessage().trim()))) {
                        checkBox.setSingleLine(false);
                        checkBox.setMaxLines(component.getNumberOfLines());
                        checkBox.setEllipsize(TextUtils.TruncateAt.END);
                        (checkBox).setTextColor(appCMSPresenter.getGeneralTextColor());
                        checkBox.setText(emailConsent.getDefaultMessage());
                        checkBox.setChecked(emailConsent.isDefaultChecked());

                        if (emailConsentMessage != null) {
                            checkBox.setChecked(emailConsentMessage.isChecked());
                            if (!TextUtils.isEmpty(emailConsentMessage.getMessage())) {
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

                if (component.getBackgroundColor() != null) {
                    checkBox.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                }
                int[][] states1 = {{android.R.attr.state_checked}, {}};
                int[] colors1 = {appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getGeneralTextColor()};
                CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(states1, colors1));
            } // end of case PAGE_EMAIL_CONSENT_CHECKBOX_KEY
            break;

            case PAGE_CAROUSEL_GRADIENT_LAYOUT_KEY: {
                componentViewResult.componentView = new ConstraintLayout(context);
                componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                componentViewResult.componentView = new ConstraintLayout(context);
                componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                int childViewWidth = (int) getViewWidth(context,
                        component.getLayout(),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                int childViewHeight = (int) getViewHeight(context,
                        component.getLayout(),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if (blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04))) {
                    childViewWidth = getDeviceWidth();
                }
                childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(childViewWidth, childViewHeight));
                componentViewResult.componentView.setBackgroundResource(R.drawable.carousel_gradient);
            }
            break;
            case PAGE_IMAGE_KEY: {

                switch (componentKey) {
                    case PAGE_THUMBNAIL_IMAGE_KEY:
                        if (component.isCircular()) {
                            componentViewResult.componentView = new CircularImageView(context);
                        } else {
                            componentViewResult.componentView = new ImageView(context);
                        }
                        viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        break;

                    default:
                        componentViewResult.componentView = new ImageView(context);
                        break;

                }
                if (blockName != null && (CommonUtils.isTrayModule(blockName) || CommonUtils.isRecommendationTrayModule(blockName)) && (componentKey == PAGE_THUMBNAIL_IMAGE_KEY
                        || componentKey == PAGE_BADGE_IMAGE_KEY
                        || componentKey == PAGE_THUMBNAIL_BADGE_IMAGE)) {
                    viewWidth = (int) BaseView.getViewWidth(context, CommonUtils.getTrayWidth(ratio, context), ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
            } //End of case PAGE_IMAGE_KEY: {
            break;
            case PAGE_BRAND_CAROUSEL_MODULE_TYPE:
                componentViewResult.componentView = new RecyclerView(context);
                ((RecyclerView) componentViewResult.componentView)
                        .setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL,
                                false));
                break;
            case PAGE_COLLECTIONGRID_KEY: {
                componentViewResult.componentView = new RecyclerView(context);
                ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            }//End of case PAGE_COLLECTIONGRID_KEY:
            break;
            case PAGE_SEPARATOR_VIEW_KEY: {
                componentViewResult.componentView = new View(context);
                switch (componentKey) {
                    case TRAY_ARTICLE_SEPARATOR_VIEW_KEY: {
                        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(viewWidth, 2);
                        componentViewResult.componentView.setLayoutParams(lp);
                    }
                    break;
                    case PAGE_CONTENT_SEPARATOR_VIEW_KEY: {
                        componentViewResult.componentView.setLayoutParams(widthHeightParams);
                    }
                    break;
                    case VIEW_SELECTION: {
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, 0));
                    }
                    break;
                    default: {
                        if (!isTablet(context)) {
                            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                            componentViewResult.componentView.setLayoutParams(lp);
                        } else {
                            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0, 1);
                            componentViewResult.componentView.setLayoutParams(lp);
                        }
                    }
                    break;
                }

            }//End of case PAGE_SEPARATOR_VIEW_KEY:
            break;
            case DATE_SEPARATOR_VIEW_KEY:
                componentViewResult.componentView = new View(context);
                int viewwidth = (int) BaseView.getViewWidth(context,
                        component.getLayout(),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                int viewheight = (int) BaseView.getViewHeight(context,
                        component.getLayout(),
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(viewwidth, viewheight);
                componentViewResult.componentView.setLayoutParams(lp);
                break;
        } // seperator view for adapter {
        if (componentViewResult.componentView != null && component.getId() != null) {
            componentViewResult.componentView.setId(IdUtils.getID(component.getId()));
            int lp = 0, rp = 0, tp = 0, bp = 0;
            if (component.getLeftpadding() != 0) {
                lp = (int) convertDpToPixel(component.getLeftpadding(), context);
            }
            if (component.getTopPadding() != 0) {
                tp = (int) convertDpToPixel(component.getTopPadding(), context);
            }
            if (component.getRightPadding() != 0) {
                rp = (int) convertDpToPixel(component.getRightPadding(), context);
            }
            if (component.getBottomPadding() != 0) {
                bp = (int) convertDpToPixel(component.getBottomPadding(), context);
            }
            componentViewResult.componentView.setPadding(lp, tp, rp, bp);
        }

    }

    String moduleId;
    boolean isTVOD = false;
    ContentTypeChecker contentTypeChecker;

    public synchronized View createComponentView(final Context context,
                                                 final Component component,
                                                 final Module moduleAPI,
                                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                                 final String viewType,
                                                 String moduleId, String blockName,
                                                 PageView pageView, ModuleWithComponents moduleInfo, AppCMSAndroidModules appCMSAndroidModules) {
        if (componentViewResult == null) {
            componentViewResult = new ViewCreator.ComponentViewResult();
        }
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(context);

        this.moduleId = moduleId;
        componentViewResult.componentView = null;
        componentViewResult.useMarginsAsPercentagesOverride = true;
        componentViewResult.useWidthOfScreen = false;
        //componentViewResult.shouldHideModule = false;
        componentViewResult.addToPageView = false;
        componentViewResult.shouldHideComponent = false;
        componentViewResult.onInternalEvent = null;
        MetadataMap metadataMap = null;
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
            metadataMap = moduleAPI.getMetadataMap();
        int viewWidth = (int) getViewWidth(context, component.getLayout(), ViewGroup.LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(context, component.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewWidth == -1)
            viewWidth = BaseView.getDeviceWidth();
        String ratio = BaseView.getViewRatio(context, component.getLayout(), "16:9");

        AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());

        if (componentType == null) {
            componentType = PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());

        if (componentKey == null) {
            componentKey = PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType uiBlockName = jsonValueKeyMap.get(blockName);

        if (blockName == null) {
            uiBlockName = PAGE_EMPTY_KEY;
        }

        if (moduleId == null && moduleAPI != null) {
            moduleId = moduleAPI.getId();
        }
        if (moduleAPI != null) {
            moduleAPIPub = moduleAPI;
        }
        String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();

        AppCMSUIKeyType moduleType = jsonValueKeyMap.get(viewType);

        if (moduleType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }

        ConstraintLayout.LayoutParams matchParentWidthParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams wrapWidthWrapContentParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ConstraintLayout.LayoutParams matchParentWidthZeroHeightParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0);
        ConstraintLayout.LayoutParams zeroWidthParams = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (componentType) {
            case PAGE_PROGRESS_VIEW_KEY: {
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
//                        canvas.clipRect(0, 0, toX, canvas.getHeight());
                        getBackground().draw(canvas);
                        canvas.restoreToCount(count);
                    }
                };

                ((ProgressBar) componentViewResult.componentView).getProgressDrawable()
                        .setColorFilter(color, PorterDuff.Mode.SRC_IN);
                componentViewResult.componentView.setBackgroundColor(color & 0x44ffffff);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0, 10);
                componentViewResult.componentView.setLayoutParams(lp);
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
                    if (appCMSPresenter.isAutoPlayEnable()) {
                        ((Switch) componentViewResult.componentView)
                                .setChecked(appCMSPresenter.getAutoplayEnabledUserPref(context));
                        ((Switch) componentViewResult.componentView)
                                .setOnCheckedChangeListener((buttonView, isChecked)
                                        -> appCMSPresenter.setAutoplayEnabledUserPref(isChecked));
                    } else {
                        ((Switch) componentViewResult.componentView)
                                .setChecked(false);
//                        ((Switch) componentViewResult.componentView).setEnabled(false);
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                }
                if (componentKey == PAGE_SETTING_CLOSED_CAPTION_TOGGLE_SWITCH_KEY) {
                    if (appCMSPresenter != null && appCMSPresenter.getAppPreference() != null) {
                        ((Switch) componentViewResult.componentView)
                                .setOnCheckedChangeListener((buttonView, isChecked) -> {
                                    if (isChecked) {
                                        appCMSPresenter.setClosedCaptionPreference(true);
                                        appCMSPresenter.videoPlayerView.getPlayerView().getSubtitleView().setVisibility(VISIBLE);
                                    } else {
                                        appCMSPresenter.setClosedCaptionPreference(false);
                                        appCMSPresenter.videoPlayerView.getPlayerView().getSubtitleView().setVisibility(GONE);
                                    }
                                });
                        ((Switch) componentViewResult.componentView).setChecked(appCMSPresenter.getAppPreference().getClosedCaptionPreference());
                        componentViewResult.componentView.setEnabled(true);

                    } else {
                        componentViewResult.componentView.setEnabled(false);
                        ((Switch) componentViewResult.componentView).setChecked(false);
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                }

                if (componentKey == PAGE_SD_CARD_FOR_DOWNLOADS_TOGGLE_BUTTON_KEY) {
                    if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                        ((Switch) componentViewResult.componentView).setChecked(appCMSPresenter.getUserDownloadLocationPref());
                        ((Switch) componentViewResult.componentView).setOnCheckedChangeListener((buttonView, isChecked) -> {
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
                    } else {
                        componentViewResult.componentView.setEnabled(false);
                        ((Switch) componentViewResult.componentView).setChecked(false);
                        componentViewResult.componentView.setVisibility(View.GONE);
                    }
                }

                if (componentKey == PAGE_SETTING_PARENTAL_CONTROL_SWITCH) {
                    if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().isParentalControlsEnable()) {
                        ((Switch) componentViewResult.componentView).setChecked(true);
                    } else {
                        ((Switch) componentViewResult.componentView).setChecked(false);
                    }

                    ((Switch) componentViewResult.componentView).setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (buttonView.isEnabled() && appCMSPresenter.getAppPreference() != null) {
                            if (!isChecked && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getParentalPin())) {
                                AppCMSVerifyVideoPinDialog.newInstance(isVerified -> {
                                    if (isVerified) {
                                        appCMSPresenter.launchButtonSelectedAction(null, component.getAction(), null,
                                                new String[]{Boolean.toString(isChecked)}, null, false, -1, null);
                                    } else {
                                        buttonView.setEnabled(false);
                                        buttonView.setChecked(!isChecked);
                                        buttonView.setEnabled(true);
                                    }
                                }, true).show(appCMSPresenter.getCurrentActivity().getSupportFragmentManager(), AppCMSVerifyVideoPinDialog.class.getSimpleName());
                            } else {
                                appCMSPresenter.launchButtonSelectedAction(null, component.getAction(), null,
                                        new String[]{Boolean.toString(isChecked)}, null, false, -1, null);
                            }
                        }
                    });
                }
            }
            break;
            case PAGE_SEARCH_BLOCK_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParamsMain = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                componentViewResult.componentView.setLayoutParams(layoutParamsMain);
                ((LinearLayout) componentViewResult.componentView).setOrientation(LinearLayout.HORIZONTAL);
                ((LinearLayout) componentViewResult.componentView).setWeightSum(10f);
                SearchView appCMSSearchView = new SearchView(context);
                float searchWeight;
                float goBtnWeight;
                if (BaseView.isTablet(context)) {
                    searchWeight = 1f;
                    goBtnWeight = 9f;
                } else {
                    searchWeight = 2f;
                    goBtnWeight = 8f;
                }
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, searchWeight);
                appCMSSearchView.setLayoutParams(layoutParams1);

                Button btnGo = new Button(context);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, goBtnWeight);
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
                    appCMSPresenter.closeSoftKeyboard();
                });

                SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
                SearchSuggestionsAdapter searchSuggestionsAdapter = new SearchSuggestionsAdapter(appCMSPresenter, appCMSPresenter.getCurrentActivity(),
                        null,
                        searchManager.getSearchableInfo(appCMSPresenter.getCurrentActivity().getComponentName()),
                        true);
                ((LinearLayout) componentViewResult.componentView).addView(appCMSSearchView);
                ((LinearLayout) componentViewResult.componentView).addView(btnGo);

                (appCMSSearchView).setBackgroundResource(android.R.color.white);
                (appCMSSearchView).
                        setQueryHint(appCMSPresenter.getLocalisedStrings().getSearchLabelText());
                appCMSSearchView.setSearchableInfo(searchManager.getSearchableInfo(appCMSPresenter.getCurrentActivity().getComponentName()));
                appCMSSearchView.setSuggestionsAdapter(null);
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
                        System.out.println(" Search ConnstraintViewCreator" + newText);
                        setSearchText(newText);
                        if (newText.trim().isEmpty()) {
                            SearchQuery objSearchQuery = new SearchQuery();
                            objSearchQuery.searchInstance(appCMSPresenter);
                            objSearchQuery.searchEmptyQuery("", appCMSPresenter.isNavbarPresent(), appCMSPresenter.isAppbarPresent());
                        } else
                            appCMSSearchView.setSuggestionsAdapter(searchSuggestionsAdapter);
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
            case PAGE_TABLAYOUT_KEY:
                componentViewResult.componentView = new TabLayout(context);
                break;
            case PAGE_SEASON_TAB_MODULE_KEY: {
                if (moduleAPI != null &&
                        moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getSeason() != null &&
                        moduleAPI.getContentData().get(0).getSeason().size() > 0 &&
                        moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes() != null &&
                        moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes().size() > 0) {

                   /* componentViewResult.componentView  = new SeasonModule(context,
                            module,
                            moduleAPI,
                            jsonValueKeyMap,
                            appCMSPresenter,
                            viewCreator,
                            appCMSAndroidModules,
                            pageView);
*/
                    componentViewResult.componentView = new SeasonTabModule(context,
                            moduleInfo,
                            moduleAPI,
                            jsonValueKeyMap,
                            appCMSPresenter,
                            pageView,
                            null,
                            appCMSAndroidModules,
                            this);

                    /*
                    pageView.addModuleViewWithModuleId(module.getId(), (ModuleView) componentViewResult.componentView, false, jsonValueKeyMap.get(module.getBlockName()));
                    RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
                    if (view != null) {
                        view.setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
                    }
                    */
                    ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                    componentViewResult.componentView.setLayoutParams(lp);

                }
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
                    videoId = moduleAPI.getContentData().get(0).getGist().getOriginalObjectId();
                    if (videoId == null) {
                        videoId = moduleAPI.getContentData().get(0).getGist().getId();
                    }


                    CustomVideoPlayerView videoPlayerViewSingle = null;
                    if (appCMSPresenter.videoPlayerView != null)
                        appCMSPresenter.videoPlayerView.pausePlayer();

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
                                && !videoPlayerViewSingle.getVideoId().equalsIgnoreCase(videoId)) {
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
                        videoPlayerViewSingle = playerView(context, videoId, moduleId + component.getKey(), appCMSPresenter, moduleAPI);
                        appCMSPresenter.videoPlayerView = videoPlayerViewSingle;

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
                    viewWidth = BaseView.getPlayerWidth();
                    viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
                    if (BaseView.isTablet(context) && BaseView.isLandscape(context)) {
                        viewHeight = (int) BaseView.getViewHeight(context, component.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    FrameLayout.LayoutParams videoPlayerParentLP = new FrameLayout.LayoutParams(viewWidth, viewHeight);
                    videoPlayerViewSingle.setLayoutParams(videoPlayerParentLP);
                    ((FrameLayout) componentViewResult.componentView).addView(videoPlayerViewSingle);
                    appCMSPresenter.videoPlayerViewParent = (ViewGroup) componentViewResult.componentView;
                    appCMSPresenter.videoPlayerView = videoPlayerViewSingle;
                    if (component.getSettings() != null && component.getSettings().isShowPIP())
                        if (appCMSPresenter.relativeLayoutPIP == null) {
                            appCMSPresenter.relativeLayoutPIP = new MiniPlayerView(context, appCMSPresenter, pageView.findViewById(R.id.home_nested_scroll_view), pageView.getPageId());
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
            case PAGE_CAROUSEL_VIEW_KEY: {
                componentViewResult.componentView = new RecyclerView(context);
                ((RecyclerView) componentViewResult.componentView)
                        .setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL,
                                false));
                boolean loop = false;
                if (component.getSettings().isLoop()) {
                    loop = component.getSettings().isLoop();
                }
                if (viewType.equalsIgnoreCase(context.getResources().getString(R.string.app_cms_page_event_carousel_module_key))) {
                    component.setView(viewType);
                }
                AppCMSCarouselItemAdapter appCMSCarouselItemAdapter = new AppCMSCarouselItemAdapter(context,
                        null,
                        component.getSettings(),
                        component.getLayout(),
                        component,
                        jsonValueKeyMap,
                        moduleAPI,
                        (RecyclerView) componentViewResult.componentView,
                        loop,
                        appCMSAndroidModule,
                        viewType,
                        component.getBlockName() == null ? blockName : component.getBlockName(),
                        true, this);
                ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSCarouselItemAdapter);
                componentViewResult.onInternalEvent = appCMSCarouselItemAdapter;
                componentViewResult.onInternalEvent.setModuleId(moduleId);
            }
            break;
            case PAGE_CONTENT_CAROUSEL_VIEW_KEY: {
                componentViewResult.componentView = new RecyclerView(context);
                componentViewResult.componentView.setId(IdUtils.getID(component.getId()));
                ((RecyclerView) componentViewResult.componentView)
                        .setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL,
                                false));
                boolean loop = true;
                if (component.getSettings().isLoop()) {
                    loop = component.getSettings().isLoop();
                }
                ContentCarouselAdapter contentCarouselAdapter = new ContentCarouselAdapter(context,
                        null,
                        component.getSettings(),
                        component.getLayout(),
                        component,
                        jsonValueKeyMap,
                        moduleAPI,
                        (RecyclerView) componentViewResult.componentView,
                        loop,
                        appCMSAndroidModule,
                        viewType,
                        component.getBlockName() == null ? blockName : component.getBlockName(),
                        true, this);
                ((RecyclerView) componentViewResult.componentView).setAdapter(contentCarouselAdapter);
                componentViewResult.onInternalEvent = contentCarouselAdapter;
                componentViewResult.onInternalEvent.setModuleId(moduleId);
            }
            break;
            case PAGE_PAGE_CONTROL_VIEW_KEY: {
                long selectedColor = Long.parseLong(appCMSPresenter.getAppCMSMain().getBrand()
                                .getCta()
                                .getPrimary().getBackgroundColor().replace("#", ""),
                        16);

                long deselectedColor = component.getUnSelectedColor() != null ?
                        Long.valueOf(component.getUnSelectedColor(), 16) : 0L;
//                selectedColor = component.getSelectedColor() != null ?
//                        Long.valueOf(component.getSelectedColor(), 16) : 0L;

                deselectedColor = ViewCreator.adjustColor1(deselectedColor, selectedColor);
                componentViewResult.componentView = new DotSelectorView(context,
                        component,
                        0xff000000 + (int) selectedColor,
                        0xff000000 + (int) deselectedColor);
                int numDots = moduleAPI != null ? moduleAPI.getContentData() != null ? moduleAPI.getContentData().size() : 0 : 0;

                if (blockName.equals("contentBlock02")) {
                    numDots = moduleInfo.getSettings().getItems().size();
                }

                ((DotSelectorView) componentViewResult.componentView).addDots(numDots);
                if (0 < numDots) {
                    ((DotSelectorView) componentViewResult.componentView).select(0);
                }
                componentViewResult.onInternalEvent = (DotSelectorView) componentViewResult.componentView;
                componentViewResult.onInternalEvent.setModuleId(moduleId);
                componentViewResult.useMarginsAsPercentagesOverride = false;

                if (numDots <= 1) {
                    componentViewResult.componentView.setVisibility(GONE);
                } else {
                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                }

                break;
            }
            case PAGE_CONSTRAINT_GUIDE_LINE_KEY: {
                componentViewResult.componentView = new Guideline(context);
            }
            break;
            case PAGE_VOD_MORE_RELATED_KEY: {
                ConstraintLayout constraintLayoutGroup = new ConstraintLayout(context);
                if (component.getId() != null) {
                    constraintLayoutGroup.setId(IdUtils.getID(component.getId()));
                }
                if (moduleAPI != null && moduleAPI.getRelatedVODModule() != null
                        && moduleAPI.getRelatedVODModule().getContentData() != null
                        && moduleAPI.getRelatedVODModule().getContentData().size() > 0) {


                    Module moduleAPIVOD = moduleAPI.getRelatedVODModule() != null ? moduleAPI.getRelatedVODModule() : moduleAPI;
                    int size = component.getComponents().size();
                    for (int i = 0; i < size; i++) {
                        Component childComponent = component.getComponents().get(i);
                        View chieldView = createComponentView(context,
                                childComponent,
                                moduleAPI,
                                jsonValueKeyMap,
                                childComponent.getType(),
                                moduleAPI.getId(), blockName, pageView, moduleInfo, appCMSAndroidModules);
                        if (chieldView != null) {
                            /**
                             * View should added to parent before creating constant Value.
                             */
                            constraintLayoutGroup.addView(chieldView);
                            setComponentViewRelativePosition(context,
                                    chieldView,
                                    moduleAPIVOD.getContentData().get(0),
                                    jsonValueKeyMap,
                                    childComponent.getType(),
                                    childComponent,
                                    constraintLayoutGroup, blockName, moduleInfo,
                                    metadataMap);
                            constraintLayoutGroup.setLayoutParams(matchParentWidthParams);
                        }
                    }
                }
                componentViewResult.componentView = constraintLayoutGroup;
            }
            break;
            case PAGE_VOD_YOU_MAY_LIKE_KEY: {
                ConstraintLayout constraintLayoutGroup = new ConstraintLayout(context);
                if (component.getId() != null) {
                    constraintLayoutGroup.setId(IdUtils.getID(component.getId()));
                }
                if (moduleAPI != null && moduleAPI.getConceptModule() != null
                        && moduleAPI.getConceptModule().getContentData() != null
                        && moduleAPI.getConceptModule().getContentData().size() > 0) {

                    Module moduleAPIVOD = moduleAPI.getConceptModule() != null ? moduleAPI.getConceptModule() : moduleAPI;
                    int size = component.getComponents().size();
                    for (int i = 0; i < size; i++) {
                        Component childComponent = component.getComponents().get(i);
                        View chieldView = createComponentView(context,
                                childComponent,
                                moduleAPI,
                                jsonValueKeyMap,
                                childComponent.getType(),
                                moduleAPI.getId(), blockName, pageView, moduleInfo, appCMSAndroidModules);
                        if (chieldView != null) {
                            /**
                             * View should added to parent before creating constant Value.
                             */
                            constraintLayoutGroup.addView(chieldView);
                            setComponentViewRelativePosition(context,
                                    chieldView,
                                    moduleAPI.getContentData().get(0),
                                    jsonValueKeyMap,
                                    childComponent.getType(),
                                    childComponent,
                                    constraintLayoutGroup, blockName, moduleInfo,
                                    metadataMap);
                            constraintLayoutGroup.setLayoutParams(matchParentWidthParams);
                        }


                    }


                }
                componentViewResult.componentView = constraintLayoutGroup;
            }// End of case PAGE_VOD_YOU_MAY_LIKE_KEY :
            break;
            case PAGE_CHAIN_KEY: {
                ConstraintLayout constraintLayoutGroup = new ConstraintLayout(context);
                if (component.getId() != null) {
                    constraintLayoutGroup.setId(IdUtils.getID(component.getId()));
                }
                if (uiBlockName == UI_BLOCK_SHOW_DETAIL_06)
                    constraintLayoutGroup.setLayoutParams(new ConstraintLayout.LayoutParams(0, viewHeight));
                else
                    constraintLayoutGroup.setLayoutParams(matchParentWidthParams);
                int size = component.getComponents().size();
                chainViews = new int[size];
                for (int i = 0; i < size; i++) {
                    Component childComponent = component.getComponents().get(i);
                    View chieldView = createComponentView(context,
                            childComponent,
                            moduleAPI,
                            jsonValueKeyMap,
                            childComponent.getType(),
                            moduleAPI.getId(), blockName, pageView, moduleInfo, appCMSAndroidModules);
                    if (chieldView != null) {
                        /**
                         * View should added to parent before creating constant Value.
                         */
                        constraintLayoutGroup.addView(chieldView);
                        chainViews[i] = chieldView.getId();
                        ContentDatum contentDatum = null;
                        if (moduleAPI.getContentData() != null && moduleAPI.getContentData().get(0) != null)
                            contentDatum = moduleAPI.getContentData().get(0);
                        setComponentViewRelativePosition(context,
                                chieldView,
                                contentDatum,
                                jsonValueKeyMap,
                                childComponent.getType(),
                                childComponent,
                                constraintLayoutGroup, blockName, moduleInfo,
                                metadataMap);

                    }


                }

                componentViewResult.componentView = constraintLayoutGroup;
            }// End of case PAGE_CHAIN_KEY :
            break;
            case PAGE_LIKE_COMPONENT_KEY: {
                componentViewResult.componentView = new LikeComponent(context, appCMSPresenter, component, moduleAPI.getContentData().get(0), jsonValueKeyMap);
                if (moduleAPI != null &&
                        moduleAPI.getContentData() != null &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getGist() != null &&
                        moduleAPI.getContentData().get(0).getGist().isLiveStream()) {
                    componentViewResult.componentView.setVisibility(INVISIBLE);
                } else {
                    componentViewResult.componentView.setVisibility(VISIBLE);
                }

            }
            break;
            case PAGE_LABEL_KEY:
            case PAGE_TEXTVIEW_KEY: {
                componentViewResult.componentView = new TextView(context);
                switch (componentKey) {
                    case WALLET_TITLE_KEY:
                    case UPI_TITLE_ERROR_KEY:
                    case CARD_TITLE_ERROR_KEY:
                    case NETBANKING_TITLE_ERROR_KEY:
                    case PAGE_INSTRUCTOR_VIDEO_DESCRIPTION_KEY:
                    case PAGE_REST_TIME_KEY:
                    case PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_VALUE_KEY:
                    case PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_VALUE_KEY:
                    case PAGE_CLASS_FORMAT_LABLE:
                    case PAGE_CLASS_FORMAT_VALUE:
                    case PAGE_THUMBNAIL_CATEGORY_TITLE:
                    case PAGE_VIDEO_DETAIL_AIR_DATE_TIME_LABEL_KEY:
                    case PAGE_VIDEO_DETAIL_TIMER_FITNESS_KEY:
                    case PAGE_HOME_DURATION_CAROUSEL:
                    case PAGE_TRAY_TITLE_KEY:
                    case PAGE_BRANDS_TRAY_TITLE_BRAND_KEY:
                    case PAGE_VOD_MORE_TRAY_TITLE_KEY:
                    case PAGE_EDIT_PROFILE_USERNAME_KEY:
                    case PAGE_EDIT_PROFILE_USERNAME_VALUE_KEY:
                    case PAGE_THUMBNAIL_TITLE_KEY:
                    case PAGE_VIDEO_DESCRIPTION_KEY:
                    case PAGE_VIDEO_TITLE_KEY:
                    case PAGE_VIDEO_SUBTITLE_KEY:
                    case PAGE_VIDEO_PUBLISHDATE_KEY:
                    case PAGE_SHOW_TITLE_KEY:
                    case PAGE_SHOW_SUBTITLE_KEY:
                    case PAGE_CAROUSEL_TITLE_KEY:
                    case PAGE_CAROUSEL_INFO_KEY:
                    case PAGE_API_TITLE:
                    case PAGE_API_DESCRIPTION:
                    case TEXT_VIEW_CELLULAR_DATA:
                    case PAGE_SD_CARD_FOR_DOWNLOADS_TEXT_KEY:
                    case PAGE_USER_MANAGEMENT_AUTOPLAY_TEXT_KEY:
                    case PAGE_USER_MANAGEMENT_CAPTION_TEXT_KEY:
                    case PAGE_USER_MANAGEMENT_DOWNLOAD_VIDEO_QUALITY_TEXT_KEY:
                    case PAGE_USER_MANAGEMENT_CELL_DATA_TEXT_KEY:
                    case PAGE_SETTINGS_DOWNLOAD_QUALITY_PROFILE_KEY:
                    case PAGE_SETTINGS_PLAN_VALUE_KEY:
                    case PAGE_SETTINGS_PLAN_PRICE_VALUE_KEY:
                    case PAGE_SETTINGS_NEXT_BILLING_DUE_DATE_VALUE_KEY:
                    case PAGE_SETTINGS_NEXT_BILLING_DUE_DATE_KEY:
                    case PAGE_SETTINGS_EMAIL_VALUE_KEY:
                    case PAGE_SETTINGS_PHONE_LABEL_KEY:
                    case PAGE_SETTINGS_PHONE_VALUE_KEY:
                    case PAGE_TERM_AND_CONDITION_PLAN:
                    case PAGE_SETTINGS_NAME_VALUE_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                    case PAGE_AUTOPLAY_MOVIE_DESCRIPTION_KEY:
                        if (blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_04))) {
                            if (!BaseView.isTablet(context)) {
                                LinearLayout.LayoutParams movieDiscriptionparams = new LinearLayout.LayoutParams(900, 400);
                                componentViewResult.componentView.setLayoutParams(movieDiscriptionparams);
                            } else {
                                LinearLayout.LayoutParams movieDiscriptionparams = new LinearLayout.LayoutParams(1000, 700);
                                componentViewResult.componentView.setLayoutParams(movieDiscriptionparams);
                            }
                        } else
                            componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                    case SEASON_KEY:
                    case SEGMENTS_KEY:
                    case EPISODES_KEY:
                        if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                            if (moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() > 0 &&
                                    moduleAPI.getContentData().get(0).getSeason() != null && moduleAPI.getContentData().get(0).getSeason().size() > 0)
                                componentViewResult.componentView.setVisibility(VISIBLE);
                            else
                                componentViewResult.componentView.setVisibility(INVISIBLE);
                            componentViewResult.addToPageView = true;
                        }
                        break;
                    case TEXT_VIEW_CONTENT_TITLE:
                        componentViewResult.addToPageView = true;
                        break;
                    case PAGE_PLAN_TITLE:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        componentViewResult.addToPageView = true;
                        componentViewResult.componentView.setVisibility(VISIBLE);
                        break;
                    case TEXT_VIEW_CONTENT_DESCRIPTION:
                        if (BaseView.isTablet(context)) {
                            LinearLayout.LayoutParams detailsparams = new LinearLayout.LayoutParams(950, ViewGroup.LayoutParams.WRAP_CONTENT);
                            componentViewResult.componentView.setLayoutParams(detailsparams);
                        } else if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
//                            LinearLayout.LayoutParams detailsparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 220);
//                            componentViewResult.componentView.setLayoutParams(detailsparams);
                            componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                            componentViewResult.componentView.setVerticalScrollBarEnabled(true);
                            ((TextView) componentViewResult.componentView).setMovementMethod(new ScrollingMovementMethod());
                        }
                        componentViewResult.addToPageView = true;
                        break;
                    case PAGE_SETTINGS_MANAGE_RECOMMEND_BUTTON_KEY:
                        if (appCMSPresenter.getAppCMSMain().getRecommendation() != null
                                && appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation()
                                && appCMSPresenter.getAppCMSMain().getRecommendation().isPersonalization()) {
                            componentViewResult.componentView.setVisibility(VISIBLE);
                        } else {
                            componentViewResult.componentView.setVisibility(GONE);
                        }
                        break;
                    case TEXT_VIEW_SAVE:
                        if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                            componentViewResult.addToPageView = true;
                            if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06 && !appCMSPresenter.getPlayshareControl())
                                componentViewResult.componentView.setVisibility(INVISIBLE);
                        }
                        break;
                    case TEXT_VIEW_PLAY:
                    case TEXT_VIEW_SHARE:
                        componentViewResult.addToPageView = true;
                        if (appCMSPresenter.getPlayshareControl())
                            componentViewResult.componentView.setVisibility(VISIBLE);
                        else
                            componentViewResult.componentView.setVisibility(INVISIBLE);
                        break;
                    case TEXT_VIEW_DOWNLOAD:
                        componentViewResult.addToPageView = true;
                        if (appCMSPresenter.getPlayshareControl() && appCMSPresenter.isDownloadEnable())
                            componentViewResult.componentView.setVisibility(VISIBLE);
                        else
                            componentViewResult.componentView.setVisibility(INVISIBLE);
                        break;
                    case INVALID_DETAILS_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        componentViewResult.componentView.setVisibility(INVISIBLE);
                        break;
                    case PAGE_VOD_CONCEPT_TRAY_TITLE_KEY: {
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        if (component != null && component.getText() != null) {
                            String title = appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()).toUpperCase() : component.getText().toUpperCase();
                            if (moduleAPI != null && moduleAPI.getConceptModule() != null
                                    && moduleAPI.getConceptModule().getContentData() != null) {
                                title += " (@####@)";
                                title = title.replace("@####@", String.valueOf(moduleAPI.getConceptModule().getContentData().size()));
                            }
                            ((TextView) componentViewResult.componentView).setText(title);
                        }
                        ((TextView) componentViewResult.componentView).setSingleLine();
                        ((TextView) componentViewResult.componentView).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    break;
                    case PAGE_VIDEO_AGE_LABEL_KEY: {
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                    }
                    break;
                }
            }// End of case PAGE_LABEL_KEY :
            break;
            case PAGE_EDIT_TEXT_KEY:
                componentViewResult.componentView = new EditText(context);
                componentViewResult.componentView.setLayoutParams(matchParentWidthParams);
                switch (componentKey) {
                    case CARD_EDIT_TEXT_KEY: {
                        int height = (int) getViewHeight(context, component.getLayout(), 1);
                        ConstraintLayout.LayoutParams editText = new ConstraintLayout.LayoutParams(0, height > 0 ? height : ViewGroup.LayoutParams.WRAP_CONTENT);
                        componentViewResult.componentView.setLayoutParams(editText);
                        break;
                    }
                    case UPI_EDIT_TEXT_KEY: {
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                    }

                    default: {
                        ConstraintLayout.LayoutParams editText = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        componentViewResult.componentView.setLayoutParams(editText);
                    }
                }

                break;
            case VIEW_TYPE_TEXT_INPUT_LAYOUT:
                componentViewResult.componentView = new TextInputLayout(context);
                componentViewResult.componentView.setLayoutParams(matchParentWidthParams);
                EditText input = new EditText(context);
                ((TextInputLayout) componentViewResult.componentView).addView(input);
                break;

            case PAGE_TEXTFIELD_KEY:
                componentViewResult.componentView = new TextInputLayout(context);
                TextInputEditText textInputEditText = new TextInputEditText(context);

                if (!TextUtils.isEmpty(component.getText())) {
                    //textInputEditText.setHint(component.getText());
                    textInputEditText.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                            appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());

                }
                textInputEditText.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                textInputEditText.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
                textInputEditText.setTextSize(context.getResources().getInteger(R.integer.app_cms_login_input_textsize));
                ConstraintLayout.LayoutParams textInputEditTextLayoutParams = new ConstraintLayout.LayoutParams(viewWidth, viewHeight);
                componentViewResult.componentView.setLayoutParams(textInputEditTextLayoutParams);
                EditText inputtype = new EditText(context);
                ((TextInputLayout) componentViewResult.componentView).addView(inputtype);
                textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ((TextInputLayout) componentViewResult.componentView).setPasswordVisibilityToggleEnabled(true);
                ((TextInputLayout) componentViewResult.componentView).setHintEnabled(false);
//                if (uiBlockName == UI_BLOCK_AUTHENTICATION_14)
//                    ((TextInputLayout) componentViewResult.componentView).setVisibility(INVISIBLE);
                break;
            case PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY: {
                if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06)
                    componentViewResult.addToPageView = true;
                componentViewResult.componentView = new DownloadComponent(context, appCMSPresenter, component, moduleAPI.getContentData().get(0), jsonValueKeyMap);
            }
            break;
            case PAGE_BARRIER_KEY: {
                componentViewResult.componentView = new Barrier(context);
            }
            break;
            case PAGE_GROUP_KEY: {
                componentViewResult.componentView = new Group(context);
            }
            break;
            case BUTTON_FLOATING: {
                componentViewResult.componentView = new ExtendedFloatingActionButton(context);
                switch (componentKey) {
                    case BUTTON__FLOATING_SUBSCRIBE:
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        componentViewResult.addBottomFloatingButton = true;
                        break;
                }
            }
            break;

            case PAGE_BUTTON_KEY: {
                switch (componentKey) {
                    case PAGE_EXPAND_SHOWS_DESCRIPTION:
                    case CANCEL_BUTTON_KEY:
                    case PAGE_VIDEO_CLOSE_KEY:
                        if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                            componentViewResult.addToPageView = true;
                        }
                    case PAGE_AUTOPLAY_BACK_KEY:
                    case PAGE_HOME_TRAY_THUMBNAIL_PLAY:
                    case PAGE_HOME_VOD_THUMBNAIL_PLAY:
                    case PAGE_FIRST_SEASON_FIRST_EPISODE_PLAY:
                    case PAGE_VIDEO_SHARE_KEY:
                    case PAGE_VIDEO_WHATSAPP_KEY:
                    case PAGE_ADD_TO_WATCHLIST_KEY:
                    case PAGE_BOOKMARK_FLAG_KEY:
                    case PAGE_PLAY_IMAGE_KEY:
                    case PAGE_PLAY_KEY:
                    case PAGE_GRID_OPTION_KEY:
                        componentViewResult.componentView = new ImageButton(context);
                        break;
                    case PAGE_VIDEO_DOWNLOAD_BUTTON_KEY: {
                        componentViewResult.componentView = new ImageButton(context);
                        if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_VIDEO_PLAYER_INFO_01) {
                            componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        }
                        break;
                    }
                    case PAGE_REMOVEALL_KEY:
                    case PAGE_START_WORKOUT:
                    case PAGE_DOWNLOAD_QUALITY_CONTINUE_BUTTON_KEY:
                    case PAGE_DOWNLOAD_QUALITY_CANCEL_BUTTON_KEY:
                    case PAGE_MORE_BANKS_TITLE_KEY:
                    case PAGE_ADD_NEW_CARD_KEY:
                        componentViewResult.componentView = new Button(context);
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                    case BUTTON_SUBSCRIBE:
                    case BUTTON_BROWSE:
                        componentViewResult.componentView = new Button(context);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        break;
                    case PAGE_AUTOPLAY_CANCEL_BUTTON_KEY:
                    case PAGE_AUTOPLAY_MOVIE_PLAY_BUTTON_KEY:
                        if (blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_autoplay_04))) {
                            if (!BaseView.isTablet(context)) {
                                componentViewResult.componentView = new Button(context);
                                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                            } else {
                                componentViewResult.componentView = new Button(context);
                                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
                            }
                        } else {
                            componentViewResult.componentView = new Button(context);
                            componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));
                        }
                        break;
                    case PAGE_UPI_PAY_BUTTON_KEY:
                        componentViewResult.componentView = new Button(context);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0));
                        break;

                    case PAGE_CARD_PAY_BUTTON_KEY:
                    case PAGE_SETTINGS_REFERRAL_PROCEED_TO_PAYMENT_KEY:
                    case PAGE_SETTINGS_EDIT_PHONE_VALUE_KEY:
                    case PAGE_SETTINGS_BILLING_HISTORY_KEY:
                    case BUTTON_CHANGE_PASSWORD:
                    case BUTTON_EDIT_PROFILE:
                    case BUTTON_MANAGE_DOWNLOAD:
                    case PAGE_SETTINGS_UPGRADE_PLAN_PROFILE_KEY:
                    case PAGE_SETTINGS_CANCEL_PLAN_PROFILE_KEY:
                        componentViewResult.componentView = new Button(context);
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        break;
                    case PAGE_SETTINGS_RE_SUBSCRIBE_PLAN_PROFILE_KEY:
                    case RESET_PASSWORD_CONTINUE_BUTTON_KEY:
                    case RESET_PASSWORD_TITLE_KEY:
                    case PAGE_REGISTER_KEY:
                    case PAGE_BACK_ARROW_KEY:
                    case PAGE_REST_KEY:
                    case LOGIN_WITH_FACEBOOK:
                    case LOGIN_WITH_GOOGLE:
                    case PAGE_LOGIN_KEY:
                        componentViewResult.componentView = new Button(context);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        break;
                    case PAGE_VIDEO_PLAY_BUTTON_KEY: {
                        componentViewResult.componentView = new ImageButton(context);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        //componentViewResult.componentView.setId(R.id.video_play_icon);

                        componentViewResult.componentView.setVisibility(View.VISIBLE);

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

                            /**
                             * check for price type svod+PPV and svod +tvod
                             */

                            if (moduleAPI.getContentData().get(0).getPricing() != null && moduleAPI.getContentData().get(0).getPricing().getType() != null &&
                                    (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                            || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {
                                componentViewResult.componentView.setVisibility(View.VISIBLE);

                            }

                            String contentType = moduleAPI.getContentData().get(0).getGist().getContentType();
                            if (contentType.equalsIgnoreCase("BUNDLE")) {

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

                                    } else if (moduleAPI.getContentData().get(0) != null &&
                                            moduleAPI.getContentData().get(0).getGist() != null && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

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
                            } else if (contentType.equalsIgnoreCase("VIDEO")) {
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
                            }


                        } else if ((moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getContentData().size() != 0 && moduleAPI.getContentData().get(0) != null) && moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

                            /**
                             * If content has not pricing info  but has schedule start time which is less than current time
                             */
                            long timeToStart = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), "EEE MMM dd HH:mm:ss");
                            long timeEnd = CommonUtils.getTimeIntervalForEvent(moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), "EEE MMM dd HH:mm:ss");
                            if ((moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 && timeToStart > 0) /*|| (moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0 && timeEnd < 0)*/) {
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


//                                if (moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
//                                        || moduleAPI.getContentData().get(0).getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {
//                                    if (appCMSPresenter.isUserLoggedIn() && !appCMSPresenter.isUserSubscribed()) {
//                                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED,
//                                                () -> {
//                                                    appCMSPresenter.setAfterLoginAction(() -> {
//                                                    });
//                                                });
//                                        return;
//                                    } else if (!appCMSPresenter.isUserLoggedIn() && !appCMSPresenter.isUserSubscribed()) {
//                                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_AND_SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED,
//                                                () -> {
//                                                    appCMSPresenter.setAfterLoginAction(() -> {
//                                                    });
//                                                });
//                                        return;
//
//                                    }
//                                }
                            }
                            if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    !moduleAPI.getContentData().isEmpty() &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null /*&&
                                    moduleAPI.getContentData().get(0).getStreamingInfo() != null &&
                                    (moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets() != null ||
                                            moduleAPI.getContentData().get(0).getGist().getMediaType().equalsIgnoreCase("EPISODIC"))*/) {
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
                                    if (moduleAPI.getContentData().get(0).getPricing() != null &&
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

//                                            if (!appCMSPresenter.isUserLoggedIn()) {
//                                                appCMSPresenter.showSubscribeMessage();
//                                                return;
//                                            }else{
//                                                appCMSPresenter.launchVideoPlayer(moduleAPI.getContentData().get(0),
//                                                        moduleAPI.getContentData().get(0).getGist().getId(),
//                                                        currentPlayingIndex, relatedVideoIds,
//                                                        moduleAPI.getContentData().get(0).getGist().getWatchedTime(),
//                                                        component.getAction());
//                                                return;
//                                            }
                                            if (appCMSPresenter.isUserSubscribed() || isPurchased) {
                                                isPlayable = true;
                                            } else {
                                                if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                                                    appCMSPresenter.showSubscribeMessage();
                                                    isPlayable = false;
                                                    return;
                                                }

                                            }
//                                            if(isPlayable){
//                                                appCMSPresenter.launchVideoPlayer(moduleAPI.getContentData().get(0),
//                                                        moduleAPI.getContentData().get(0).getGist().getId(),
//                                                        currentPlayingIndex, relatedVideoIds,
//                                                        moduleAPI.getContentData().get(0).getGist().getWatchedTime(),
//                                                        component.getAction());
//                                                return;
//                                            }
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
                                                } else {
//                                                appCMSPresenter.sendCloseOthersAction(null, true, false);
                                                }
                                            }, msg, "", "", "", true, true);
                                        } else if (appCMSPresenter.isScheduleVideoPlayable(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                                            appCMSPresenter.launchVideoPlayer(moduleAPI.getContentData().get(0),
                                                    moduleAPI.getContentData().get(0).getGist().getId(),
                                                    currentPlayingIndex, relatedVideoIds,
                                                    moduleAPI.getContentData().get(0).getGist().getWatchedTime(),
                                                    component.getAction());
                                        }


                                    } else if (moduleAPI.getContentData().get(0) != null &&
                                            moduleAPI.getContentData().get(0).getGist() != null && (moduleAPI.getContentData().get(0).getGist().getScheduleStartDate() > 0 || moduleAPI.getContentData().get(0).getGist().getScheduleEndDate() > 0)) {

                                        if (appCMSPresenter.isStartTimeAvailable(moduleAPI.getContentData().get(0).getGist().getScheduleStartDate(), moduleAPI.getContentData().get(0).getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                                            appCMSPresenter.launchVideoPlayer(moduleAPI.getContentData().get(0),
                                                    moduleAPI.getContentData().get(0).getGist().getId(),
                                                    currentPlayingIndex, relatedVideoIds,
                                                    moduleAPI.getContentData().get(0).getGist().getWatchedTime(),
                                                    component.getAction());
                                        }

                                    } else {
/*
                                        try {
                                            if (moduleAPI.getContentData().get(0).isDRMEnabled()) {
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
*/


                                        appCMSPresenter.launchButtonSelectedAction(moduleAPI.getContentData().get(0).getGist().getPermalink(),
                                                component.getAction(),
                                                moduleAPI.getContentData().get(0).getGist().getTitle(),
                                                null,
                                                moduleAPI.getContentData().get(0),
                                                false,
                                                0,
                                                relatedVideoIds);
                                    }
                                }
                            }
                        });

                        if (componentKey == PAGE_VIDEO_DETAIL_CLASS_PLAY_BUTTON_KEY) {
                            componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.neo_video_detail_play));
                        } else {
                            // componentViewResult.componentView.setPadding(8, 8, 8, 8);
                            ((ImageButton) componentViewResult.componentView).setImageResource(R.drawable.play_icon);
                            //componentViewResult.componentView.setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                            ((ImageButton) componentViewResult.componentView).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(), PorterDuff.Mode.SRC_IN));
                            componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.FIT_XY);
                            //componentViewResult.componentView.getBackground().setTint(getTransparentColor(tintColor, 0.75f));
                        }
                    } //case PAGE_VIDEO_PLAY_BUTTON_KEY:
                    break;
                    default:
                        componentViewResult.componentView = new Button(context);
                        break;
                }

            }// End of case PAGE_BUTTON_KEY :
            break;
            case PAGE_CAROUSEL_GRADIENT_LAYOUT_KEY: {
                componentViewResult.componentView = new ConstraintLayout(context);
                componentViewResult.componentView.setLayoutParams(zeroWidthParams);

                if (blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04))) {
                    viewWidth = getDeviceWidth();
                }
                viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);

                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                componentViewResult.componentView.setBackgroundResource(R.drawable.carousel_gradient);

            }
            break;
            case PAGE_COUNTDOWN_TIMER_VIEW: {
                componentViewResult.componentView = new TimerViewFutureContent(context);
            }
            break;
            case PAGE_IMAGE_KEY: {
                switch (componentKey) {
                    case PAGE_SERIES_IMAGE_KEY:
                        componentViewResult.addToPageView = true;
                        componentViewResult.componentView = new ImageView(context);
                        if (BaseView.isLandscape(context) && uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                            viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                            componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        } else
                            componentViewResult.componentView.setVisibility(GONE);
                        break;
                    case PAGE_BADGE_IMAGE_KEY:
                    case PAGE_SHOW_IMAGE_KEY:
                    case PAGE_VIDEO_IMAGE_KEY: {
                        componentViewResult.componentView = new ImageView(context);
                        viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                    }
                    break;
                    case PAGE_RIGHT_ARROW_KEY:
                        componentViewResult.componentView = new ImageView(context);
                        break;
                    case PAGE_THUMBNAIL_IMAGE_KEY:
                        componentViewResult.componentView = new ImageView(context);
                        break;
                    case TRAILER_THUMBNAIL_IMAGE_KEY:
                        componentViewResult.addToPageView = true;
                        componentViewResult.componentView = new ImageView(context);
                        viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        thumbnailImage = ((ImageView) componentViewResult.componentView);
                        break;
                    case PAGE_SETTINGS_OFFER_IMAGE_KEY:
                        componentViewResult.componentView = new ImageView(context);
                        viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                        break;
                    case APP_LOGO_KEY:
                        componentViewResult.componentView = ImageUtils.createImageView(context);
                        if (componentViewResult.componentView == null) {
                            componentViewResult.componentView = new ImageView(context);
                            if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                                componentViewResult.addToPageView = true;
                                int height = (int) getViewHeight(context, component.getLayout(), 1);
                                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
                            }
                        }
                        break;
                    default:
                        componentViewResult.componentView = ImageUtils.createImageView(context);
                        if (componentViewResult.componentView == null) {
                            componentViewResult.componentView = new ImageView(context);
                        }
                        break;
                }
            } //End of case PAGE_IMAGE_KEY: {
            break;
            case PAGE_BRAND_CAROUSEL_MODULE_TYPE:
                componentViewResult.componentView = new RecyclerView(context);
                ((RecyclerView) componentViewResult.componentView)
                        .setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.HORIZONTAL,
                                false));
                break;

            case PAGE_PAGE_CONTROL_VIEW_03_KEY:
                componentViewResult.componentView = new DotSelectorView03(context,
                        component,
                        Color.parseColor(CommonUtils.getColor(context, component.getSelectedColor())),
                        Color.parseColor(CommonUtils.getColor(context, component.getUnSelectedColor()))
                );
                int numDots1 = moduleAPI != null ? moduleAPI.getContentData() != null ? moduleAPI.getContentData().size() : 0 : 0;
                ((DotSelectorView03) componentViewResult.componentView).addDots(numDots1);
                if (0 < numDots1) {
                    ((DotSelectorView03) componentViewResult.componentView).select(0);
                }
                componentViewResult.onInternalEvent = (DotSelectorView03) componentViewResult.componentView;
                componentViewResult.onInternalEvent.setModuleId(moduleId);
                componentViewResult.useMarginsAsPercentagesOverride = false;

                if (numDots1 <= 1) {
                    componentViewResult.componentView.setVisibility(INVISIBLE);
                } else {
                    componentViewResult.componentView.setVisibility(VISIBLE);
                }

                break;

            case PAGE_CASTVIEW_VIEW_KEY: {
                //if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT) {
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
                if (fontWeight == AppCMSUIKeyType.PAGE_TEXT_BOLD_KEY ||
                        fontWeight == AppCMSUIKeyType.PAGE_TEXT_SEMIBOLD_KEY ||
                        fontWeight == AppCMSUIKeyType.PAGE_TEXT_EXTRABOLD_KEY) {
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

                if (fontWeight == AppCMSUIKeyType.PAGE_TEXT_BOLD_KEY ||
                        fontWeight == AppCMSUIKeyType.PAGE_TEXT_SEMIBOLD_KEY ||
                        fontWeight == AppCMSUIKeyType.PAGE_TEXT_EXTRABOLD_KEY) {
                    fontFamilyValueType = Typeface.BOLD;
                }

                int textColor = Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppCMSMain()
                        .getBrand().getGeneral().getTextColor()));

                String directorTitle = null;
                StringBuilder directorListSb = new StringBuilder();
                String starringTitle = null;
                StringBuilder starringListSb = new StringBuilder();

                if (moduleAPI != null && moduleAPI.getContentData() != null &&
                        !moduleAPI.getContentData().isEmpty() &&
                        moduleAPI.getContentData().get(0) != null &&
                        moduleAPI.getContentData().get(0).getCreditBlocks() != null) {
                    for (CreditBlock creditBlock : moduleAPI.getContentData().get(0).getCreditBlocks()) {
                        AppCMSUIKeyType creditBlockType = jsonValueKeyMap.get(creditBlock.getTitle());
                        if (creditBlockType != null &&
                                (creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTEDBY_KEY ||
                                        creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTOR_KEY ||
                                        creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_DIRECTORS_KEY)) {
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
                                creditBlockType == AppCMSUIKeyType.PAGE_VIDEO_CREDITS_STARRING_KEY) {
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
                componentViewResult.componentView.setLayoutParams(zeroWidthParams);

                if (TextUtils.isEmpty(directorListSb.toString())
                        && TextUtils.isEmpty(starringListSb.toString())) {
                    componentViewResult.componentView.setLayoutParams(lpViewNoVisibility);
                    component.getLayout().setConstraintMarginTop(0);
                }
                if (moduleAPI != null && !BaseView.isTablet(context)
                        && moduleAPI.getModuleType() != null
                        && (jsonValueKeyMap.get(moduleAPI.getModuleType())
                        == AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_01 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType())
                                == AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_02 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType())
                                == AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_03 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType())
                                == AppCMSUIKeyType.PAGE_AUTOPLAY_MODULE_KEY_04 ||
                        jsonValueKeyMap.get(moduleAPI.getModuleType())
                                == AppCMSUIKeyType.PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY
                )) {
                    componentViewResult.componentView.setVisibility(INVISIBLE);
                }
                /*}else{
                    componentViewResult.componentView.setVisibility(INVISIBLE);
                }*/
                break;
            }
            case VIEW_TABLE_LAYOUT:
                switch (componentKey) {
                    case TABLE_PLAN_FEATURES:
                        componentViewResult.componentView = new PlanFeaturesLayout(context);
                        ((PlanFeaturesLayout) componentViewResult.componentView).setLayoutParams(zeroWidthParams);
                        break;
                }
                break;
            case PAGE_COLLAPSIBLE_VIEW:
                ExpandableLayout expandableLayout = new ExpandableLayout(context);
                if (BaseView.isTablet(context) && uiBlockName == UI_BLOCK_SHOW_DETAIL_06)
                    expandableLayout.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                else
                    expandableLayout.setLayoutParams(matchParentWidthParams);
                ConstraintLayout constraintLayout = new ConstraintLayout(context);
                constraintLayout.setLayoutParams(matchParentWidthParams);
                expandableLayout.setExpanded(true);
                // expandableLayout.setParallax(0.5f);
                // expandableLayout.setDuration(1000);
                if (component.getId() != null) {
                    expandableLayout.setId(IdUtils.getID(component.getId()));
                }

                int size = component.getComponents().size();
                for (int i = 0; i < size; i++) {
                    Component childComponent = component.getComponents().get(i);
                    View chieldView = createComponentView(context,
                            childComponent,
                            moduleAPI,
                            jsonValueKeyMap,
                            childComponent.getType(),
                            moduleAPI.getId(), blockName, pageView, moduleInfo, appCMSAndroidModules);
                    if (chieldView != null) {
                        /**
                         * View should added to parent before creating constant Value.
                         */
                        constraintLayout.addView(chieldView);
                        setComponentViewRelativePosition(context,
                                chieldView,
                                moduleAPI.getContentData().get(0),
                                jsonValueKeyMap,
                                childComponent.getType(),
                                childComponent,
                                constraintLayout, blockName, moduleInfo,
                                metadataMap);
                        constraintLayout.setLayoutParams(matchParentWidthParams);
                        // expandableLayout.setLayoutParams(matchParentWidthParams);
                    }
                }
                expandableLayout.addView(constraintLayout);

                componentViewResult.componentView = expandableLayout;
                break;
            case PAGE_COLLECTIONGRID_KEY:
            case PAGE_TABLE_VIEW_KEY: {
                componentViewResult.componentView = new RecyclerView(context);
                AppCMSViewAdapter appCMSViewAdapter;
                AppCMSConstraintViewAdapter appCMSConstraintViewAdapter;
                switch (componentKey) {
                    case GRID_COLLECTIONGRID_KEY:
                        GridLayoutManager gridLayoutManager;
                        int columns = 2;
                        if (component.getSettings() != null && component.getSettings().getColumns() != null) {
                            if (BaseView.isTablet(context))
                                columns = component.getSettings().getColumns().getTablet();
                            else
                                columns = component.getSettings().getColumns().getMobile();
                        } else {
                            if (BaseView.isTablet(context)) {
                                columns = 3;
                                if (BaseView.isLandscape(context))
                                    columns = 4;
                            }
                        }

                        gridLayoutManager = new GridLayoutManager(context, columns,
                                GridLayoutManager.VERTICAL, false);
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(gridLayoutManager);
                        PaginationAdapter paginationAdapter = new PaginationAdapter(context, this, appCMSPresenter, component.getLayout(),
                                component, jsonValueKeyMap, moduleAPI, ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT, viewType,
                                appCMSAndroidModules,
                                blockName, ((RecyclerView) componentViewResult.componentView));
                        ((RecyclerView) componentViewResult.componentView).setAdapter(paginationAdapter);
                        final boolean[] isLoading = {true};
                        ((RecyclerView) componentViewResult.componentView).addOnScrollListener(new PaginationScrollListener(context, gridLayoutManager, ((RecyclerView) componentViewResult.componentView)) {
                            @Override
                            protected void loadMoreItems() {
                                if (!appCMSPresenter.isLastPage())
                                    paginationAdapter.addLoadingFooter();
                                new Handler().postDelayed(() -> {
                                    if (moduleAPI.getFilters() != null) {
                                        appCMSPresenter.getPaginatedModuleData(pageView.getPageId(), moduleAPI.getFilters(), moduleAPI.getId(), new Consumer<AppCMSPageAPI>() {
                                            @Override
                                            public void accept(AppCMSPageAPI appCMSPageAPI) throws Throwable {

                                                if (appCMSPageAPI != null) {
                                                    for (Module module : appCMSPageAPI.getModules()) {
                                                        if (module.getModuleType().equalsIgnoreCase("CategoryDetailModule")
                                                                || module.getModuleType().equalsIgnoreCase("GeneratedTrayModule")) {
                                                            appCMSPresenter.setLastPage(!module.hasNext());
                                                            if (module.getContentData() != null && module.getContentData().size() > 0) {
                                                                isLoading[0] = false;
                                                                appCMSPresenter.setLastPage(!module.hasNext());
                                                                paginationAdapter.removeLoadingFooter();
                                                                paginationAdapter.addAll(module.getContentData());
                                                            } else {
                                                                paginationAdapter.removeLoadingFooter();
                                                                paginationAdapter.setClickable(true);
                                                            }
                                                        }
                                                    }

                                                    if (gridLayoutManager.getItemCount() < 12 && appCMSPageAPI.getModules().get(0).hasNext()) {
                                                        loadMoreItems();
                                                    }

                                                } else {
                                                    paginationAdapter.removeLoadingFooter();
                                                    paginationAdapter.setClickable(true);
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
                                return isLoading[0];
                            }
                        });





                       /* appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context, component.getSettings(),
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModule,
                                blockName,
                                this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);*/
                        break;
                    case COLLECTIONGRID_HORIZONTAL:
                        componentViewResult.componentView.setLayoutParams(matchParentWidthParams);
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false));
                        appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                component.getSettings(),
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModule,
                                blockName,
                                this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);
                        break;
                    case COLLECTIONGRID_VERTICAL:
                        if (uiBlockName == UI_BLOCK_CAROUSEL_07)
                            componentViewResult.componentView.setLayoutParams(wrapWidthWrapContentParams);
                        else if (BaseView.isLandscape(context) && uiBlockName == UI_BLOCK_SHOW_DETAIL_06) {
                            componentViewResult.addToPageView = true;
                            int height = ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getModuleHeight() - ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getTabbarHeight() * 3;
                            componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
                        } else
                            componentViewResult.componentView.setLayoutParams(matchParentWidthParams);
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.VERTICAL,
                                        false));
                        appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                component.getSettings(),
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModule,
                                blockName,
                                this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);
                        break;
                    case VERTICAL_COLLECTION_GRID_VIEWPLAN:
                        appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                component.getSettings(),
                                component.getLayout(),
                                false,
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                (BaseView.getDeviceHeight() * 23 / 100),
                                viewType,
                                appCMSAndroidModule,
                                blockName,
                                this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);
                        if (uiBlockName == UI_BLOCK_CAROUSEL_07)
                            componentViewResult.componentView.setLayoutParams(wrapWidthWrapContentParams);
                        else if (BaseView.isLandscape(context) && uiBlockName == UI_BLOCK_SHOW_DETAIL_06) {
                            componentViewResult.addToPageView = true;
                            int height = ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getModuleHeight() - ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getTabbarHeight() * 3;
                            componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
                        } else {
                            if (moduleAPIPub != null && moduleAPIPub.getDescription() != null) {
                                if (appCMSConstraintViewAdapter.getItemCount() == 0 || appCMSConstraintViewAdapter.getItemCount() == 1) {
                                    componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(((BaseView.getDeviceWidth() * 80) / 100), ViewGroup.LayoutParams.WRAP_CONTENT));
                                } else if (appCMSConstraintViewAdapter.getItemCount() >= 2) {
                                    componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(((BaseView.getDeviceWidth() * 80) / 100), ((BaseView.getDeviceHeight() * 50) / 100)));
                                }
                            } else {
                                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(((BaseView.getDeviceWidth() * 80) / 100), ViewGroup.LayoutParams.WRAP_CONTENT));
                            }
                        }
                        componentViewResult.addToPageView = true;
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.VERTICAL,
                                        false));

                        if (!BaseView.isTablet(context)) {
                            componentViewResult.useWidthOfScreen = true;
                        }


                        break;
                    case PAGE_COLLAPSIBLE_VIEW:
                        expandableLayout = new ExpandableLayout(context);
                        expandableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        if (component.getId() != null) {
                            expandableLayout.setId(IdUtils.getID(component.getId()));
                        }

                        size = component.getComponents().size();
                        for (int i = 0; i < size; i++) {
                            Component childComponent = component.getComponents().get(i);
                            View chieldView = createComponentView(context,
                                    childComponent,
                                    moduleAPI,
                                    jsonValueKeyMap,
                                    childComponent.getType(),
                                    moduleAPI.getId(), blockName, pageView, moduleInfo, appCMSAndroidModules);
                            if (chieldView != null) {
                                /**
                                 * View should added to parent before creating constant Value.
                                 */
                                expandableLayout.addView(chieldView);
                                setComponentViewRelativePosition(context,
                                        chieldView,
                                        new ContentDatum(),
                                        jsonValueKeyMap,
                                        childComponent.getType(),
                                        childComponent,
                                        (ConstraintLayout) expandableLayout.getParent(), blockName, moduleInfo,
                                        metadataMap);
                                expandableLayout.setLayoutParams(matchParentWidthParams);
                            }
                        }

                        componentViewResult.componentView = expandableLayout;

                        break;
                    case PAGE_COLLECTIONGRID_KEY:
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false));
                        if (CommonUtils.isTrayModule(uiBlockName) || CommonUtils.isRecommendationTrayModule(uiBlockName)) {
                            AppCMSTrayViewAdapter appCMSTrayViewAdapter = new AppCMSTrayViewAdapter(context,
                                    null,
                                    component.getSettings(),
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
                                    true,
                                    this);

                            componentViewResult.useWidthOfScreen = true;
                            //((RecyclerView) componentViewResult.componentView).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSTrayViewAdapter);
                        } else {
                            appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                    component.getSettings(),
                                    component.getLayout(),
                                    false,
                                    component,
                                    jsonValueKeyMap,
                                    moduleAPI,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    viewType,
                                    appCMSAndroidModule,
                                    blockName,
                                    this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);
                        }
                        break;
                    case COLLECTIONGRID_WATCHLIST:
                        componentViewResult.componentView.setLayoutParams(matchParentWidthParams);
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        AppCMSUserPagesAdapter appCMSUserPagesAdapter = new AppCMSUserPagesAdapter(context,
                                this,
                                component.getLayout(),
                                component,
                                jsonValueKeyMap,
                                moduleAPI,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                viewType,
                                appCMSAndroidModule, blockName, pageView);
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSUserPagesAdapter);
                        if (moduleInfo.getSettings() != null && moduleInfo.getSettings().isEnableSwipeToDelete()) {
                            ItemTouchHelper itemTouchHelper = new
                                    ItemTouchHelper(new SwipeView(appCMSUserPagesAdapter, ContextCompat.getColor(context, android.R.color.holo_red_dark)));
                            itemTouchHelper.attachToRecyclerView(((RecyclerView) componentViewResult.componentView));
                        }
                        break;
                    case PAGE_COLLECTIONGRID_SEASON_KEY: {
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        int coloumns = BaseView.isTablet(componentViewResult.componentView.getContext()) ? 2 : 1;
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new GridLayoutManager(context,
                                        coloumns,
                                        GridLayoutManager.VERTICAL,
                                        false));
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getSeason() != null &&
                                !moduleAPI.getContentData().get(0).getSeason().isEmpty() &&
                                moduleAPI.getContentData().get(0).getSeason().get(0) != null) {

                            /*ViewCreator.CollectionGridItemViewCreator collectionGridItemViewCreator =
                                    new ViewCreator.CollectionGridItemViewCreator(this,
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
                                            false, blockName);*/

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
                                        if (season.getNumber() != 0 && episodeContentDatum.getNumber() != 0) {
                                            StringBuilder seasonEpisodeNum = new StringBuilder();
                                            seasonEpisodeNum.append("S");
                                            seasonEpisodeNum.append(season.getNumber());
                                            seasonEpisodeNum.append(" ");
                                            seasonEpisodeNum.append("E");
                                            seasonEpisodeNum.append(episodeContentDatum.getNumber());
                                            episodeContentDatum.setSeasonEpisodeNum(seasonEpisodeNum.toString());
                                        }
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
                                            null,
                                            appCMSAndroidModules,
                                            null,
                                            component.getSettings(),
                                            component,
                                            null,
                                            moduleAPI,
                                            component.getComponents(),
                                            allEpisodeIds,
                                            allEpisodes,
                                            jsonValueKeyMap,
                                            viewType,
                                            ((RecyclerView) componentViewResult.componentView),
                                            blockName,
                                            component.getTrayClickAction(),
                                            true,
                                            this
                                    );
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSTraySeasonItemAdapter);
                        }
                    }
                    break;
                    case PAGE_EXPENDABLE_LIST:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context));
                        SeparatorDecoration separatorDecoration = new SeparatorDecoration(componentViewResult.componentView.getContext(), Color.DKGRAY, 1f);
                        ((RecyclerView) componentViewResult.componentView).addItemDecoration(separatorDecoration);
                        break;
                    case PAGE_VOD_MORE_COLLECTIONGRID_KEY:// VOD More tray
                        if (moduleAPI.getRelatedVODModule() != null) {
                            Module moduleMoreLikeThis = moduleAPI.getRelatedVODModule();
                            appCMSViewAdapter = new AppCMSViewAdapter(context,
                                    null,
                                    null,
                                    component.getLayout(),
                                    false,
                                    component,
                                    jsonValueKeyMap,
                                    moduleMoreLikeThis,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    "",
                                    appCMSAndroidModule,
                                    blockName,
                                    true, this);

                            componentViewResult.useWidthOfScreen = true;
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        }
                        break;
                    case PAGE_VOD_CONCEPT_COLLECTIONGRID_KEY://VOD Concept tray
                        if (moduleAPI.getConceptModule() != null) {
                            Module moduleConcept = moduleAPI.getConceptModule();
                            appCMSViewAdapter = new AppCMSViewAdapter(context,
                                    null,
                                    null,
                                    component.getLayout(),
                                    false,
                                    component,
                                    jsonValueKeyMap,
                                    moduleConcept,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    "",
                                    appCMSAndroidModule,
                                    blockName,
                                    true,
                                    this);

                            componentViewResult.useWidthOfScreen = true;
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSViewAdapter);
                        }
                        break;
                    case PAGE_INSTRUCTOR_RECENT_GRID_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        GridLayoutManager gridLayout = new GridLayoutManager(context, 1,
                                GridLayoutManager.VERTICAL, false);
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(gridLayout);
                        break;

                    case PAGE_TABLE_VIEW_SETTING_LANGUAGE_DOWNLOAD_KEY:
                        componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0));
                        break;

                    case WALLET_LIST_VIEW_KEY: {
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getWallets() != null) {
                            List<ContentDatum> contentDatumList = new ArrayList<>();
                            for (MetadataMap.Wallet wallet : moduleAPI.getMetadataMap().getWallets()) {
                                if (!TextUtils.isEmpty(wallet.getKey())) {
                                    if (wallet.getKey().contains(context.getString(R.string.google_pay)) && !CommonUtils.isAppInstalled(context, GOOGLE_PAY_PACKAGE_NAME)) {
                                        continue;
                                    }

                                    if (appCMSPresenter.getAllowedPayMethods() != null && appCMSPresenter.getAllowedPayMethods().size() > 0) {
                                        boolean isValidPaymentMethod = false;
                                        for (String planPaymentMethod : appCMSPresenter.getAllowedPayMethods()) {
                                            if (planPaymentMethod.contains(wallet.getTitle().toUpperCase())) {
                                                isValidPaymentMethod = true;
                                                break;
                                            }
                                        }

                                        if (!isValidPaymentMethod) continue;
                                    }
                                    ContentDatum contentDatum = new ContentDatum();
                                    contentDatum.setId(moduleAPI.getId());
                                    contentDatum.setTitle(wallet.getTitle());
                                    contentDatum.setWalletKey(wallet.getKey());
                                    contentDatum.setDescription(wallet.getOffer());
                                    contentDatum.setImageUrl(wallet.getImageUrl());
                                    contentDatumList.add(contentDatum);
                                }
                            }

                            moduleAPI.setContentData(contentDatumList);
                            appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                    null, component.getLayout(), false, component, jsonValueKeyMap, moduleAPI,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    "", appCMSAndroidModules, blockName, this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());

                            componentViewResult.useWidthOfScreen = true;
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);
                        }

                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context));
                        break;
                    }
                    case NETBANKING_LIST_VIEW_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getBanks() != null) {
                            List<ContentDatum> contentDatumList = new ArrayList<>();
                            for (MetadataMap.Bank bank : moduleAPI.getMetadataMap().getBanks()) {
                                if (!TextUtils.isEmpty(bank.getTitle())) {
                                    ContentDatum contentDatum = new ContentDatum();
                                    contentDatum.setId(moduleAPI.getId());
                                    contentDatum.setTitle(bank.getTitle().toUpperCase());
                                    contentDatum.setImageUrl(bank.getImageUrl());
                                    contentDatumList.add(contentDatum);
                                }
                            }

                            moduleAPI.setContentData(contentDatumList);

                            appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                    null, component.getLayout(), false, component, jsonValueKeyMap, moduleAPI,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    "", appCMSAndroidModules, blockName, this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());

                            componentViewResult.useWidthOfScreen = false;
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);
                        }

                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        if (!TextUtils.isEmpty(component.getBackgroundColor()))
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));

                        break;

                    case CARD_ICONS_LIST_VIEW_KEY: {
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        Module module = moduleAPI != null ? moduleAPI : new Module();
                        if (uiBlockName == UI_BLOCK_CARD_PAYMENT_01)
                            module.setMetadataMap(appCMSPresenter.getMetadataMap());
                        else
                            appCMSPresenter.setMetadataMap(moduleAPI.getMetadataMap());

                        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getCards() != null) {
                            appCMSPresenter.setModuleApi(moduleAPI);
                            List<ContentDatum> contentDatumList = new ArrayList<>();
                            for (MetadataMap.Card card : module.getMetadataMap().getCards()) {
                                if (!TextUtils.isEmpty(card.getImageUrl())) {
                                    ContentDatum contentDatum = new ContentDatum();
                                    contentDatum.setId(module.getId());
                                    contentDatum.setImageUrl(card.getImageUrl());
                                    contentDatumList.add(contentDatum);
                                }
                            }

                            module.setContentData(contentDatumList);

                            appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                    null, component.getLayout(), false, component, jsonValueKeyMap, module,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    "", appCMSAndroidModules, blockName, this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());

                            componentViewResult.useWidthOfScreen = false;
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);

                        }
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        if (!TextUtils.isEmpty(component.getBackgroundColor()))
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));

                        break;
                    }

                    case UPI_ICONS_LIST_VIEW_KEY: {
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getUPI() != null) {
                            List<ContentDatum> contentDatumList = new ArrayList<>();
                            for (MetadataMap.UPI upi : moduleAPI.getMetadataMap().getUPI()) {
                                if (!TextUtils.isEmpty(upi.getImageUrl())) {
                                    ContentDatum contentDatum = new ContentDatum();
                                    contentDatum.setId(moduleAPI.getId());
                                    contentDatum.setImageUrl(upi.getImageUrl());
                                    contentDatumList.add(contentDatum);
                                }
                            }

                            moduleAPI.setContentData(contentDatumList);

                            appCMSConstraintViewAdapter = new AppCMSConstraintViewAdapter(context,
                                    null, component.getLayout(), false, component, jsonValueKeyMap, moduleAPI,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    "", appCMSAndroidModules, blockName, this, ((RecyclerView) componentViewResult.componentView), pageView, component.getTrayClickAction());

                            componentViewResult.useWidthOfScreen = false;
                            ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSConstraintViewAdapter);

                        }
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        if (!TextUtils.isEmpty(component.getBackgroundColor()))
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));

                        break;
                    }

                    case SAVED_CARD_LIST_VIEW_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        List<MetadataMap.Card> cards = new ArrayList<>();
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getCards() != null)
                            cards.addAll(moduleAPI.getMetadataMap().getCards());
                        AppCMSSavedCardsAdapter appCMSSavedCardsAdapter = new AppCMSSavedCardsAdapter(context, new ArrayList<>(), cards);
                        ((RecyclerView) componentViewResult.componentView).setAdapter(appCMSSavedCardsAdapter);
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context));
                        if (!TextUtils.isEmpty(component.getBackgroundColor()))
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                        if (appCMSPresenter.getJusPayUtils() != null)
                            appCMSPresenter.getJusPayUtils().getSavedCardList(appCMSSavedCardsAdapter);
                        break;

                    case BANK_LIST_VIEW_KEY:
                        componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                        ((RecyclerView) componentViewResult.componentView).setAdapter(new BankListAdapter(appCMSPresenter));
                        ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        if (!TextUtils.isEmpty(component.getBackgroundColor()))
                            componentViewResult.componentView.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));

                        break;
                    case PAGE_TABLE_VIEW_KEY: {
                        ((RecyclerView) componentViewResult.componentView)
                                .setLayoutManager(new LinearLayoutManager(context,
                                        LinearLayoutManager.VERTICAL,
                                        false));
                        componentViewResult.componentView.setLayoutParams(matchParentWidthZeroHeightParams);

                        AppCMSUserWatHisDowAdapter appCMSUserWatHisDowAdapter = new AppCMSUserWatHisDowAdapter(context,
                                null,
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
                                true,
                                this);


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

                    } //end of case PAGE_TABLE_VIEW_KEY:{
                    break;
                    default:
                        if (isTablet(context)) {
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                        } else {
                            componentViewResult.componentView.setLayoutParams(zeroWidthParams);
                            GridLayoutManager gridLayoutManage = new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false);
                            ((RecyclerView) componentViewResult.componentView).setLayoutManager(gridLayoutManage);
                        }
                        break;
                }
                break;
            }
            case DETAIL_PAGE_TRAILER:
                viewHeight = BaseView.getViewHeightByRatio(ratio, viewWidth);
                CustomVideoPlayerView trailerPlayerView = null;
                componentViewResult.addToPageView = true;
                if (appCMSPresenter.getVideoPlayerViewCache(moduleId + component.getKey()) != null) {
                    trailerPlayerView = appCMSPresenter.getVideoPlayerViewCache(moduleId + component.getKey());
                } else {
                    trailerPlayerView = new CustomVideoPlayerView(context, appCMSPresenter, null, null);
                    appCMSPresenter.setVideoPlayerViewCache(moduleId + component.getKey(), trailerPlayerView);
                }
                appCMSPresenter.setShowDeatil06TrailerPlayerKey(moduleId + component.getKey());
                componentViewResult.componentView = trailerPlayerView;
                appCMSPresenter.setTrailerPlayerView(trailerPlayerView);
                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth, viewHeight));
                break;
            case BOTTOM_SHEET_KEY:
                componentViewResult.componentView = new LinearLayout(context);
                LinearLayout.LayoutParams layoutParamsBottomSheet = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
                componentViewResult.componentView.setLayoutParams(layoutParamsBottomSheet);
                componentViewResult.componentView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case PAGE_VIEW_DETAILS_BACKGROUND:
//                componentViewResult.componentView = new CardView(context);
//                ConstraintLayout.LayoutParams cardParm;
//                cardParm = new ConstraintLayout.LayoutParams(0, getDeviceHeight());
//                ((CardView) componentViewResult.componentView).setLayoutParams(cardParm);
                ConstraintLayout.LayoutParams backGroundParam;
                componentViewResult.componentView = new View(context);
                backGroundParam = new ConstraintLayout.LayoutParams(0, BaseView.getDeviceHeight() / 3);
                ((View) componentViewResult.componentView).setLayoutParams(backGroundParam);
                break;
            case PAGE_SEPARATOR_VIEW_KEY:
                componentViewResult.componentView = new View(context);
                ConstraintLayout.LayoutParams lp1;
                int deviceheight = BaseView.getDeviceHeight() / 2;
                if (!isTablet(context)) {
                    if (uiBlockName == UI_BLOCK_AUTHENTICATION_14)
                        lp1 = new ConstraintLayout.LayoutParams(0, 0);
                    else if (componentKey == PAGE_FILL_BACKGROUND)
                        lp1 = new ConstraintLayout.LayoutParams(0, getDeviceHeight());
                    else if (componentKey == PAGE_VIEW_DETAILS_BACKGROUND || componentKey == PAGE_VIEW_TIMER_BACKGROUND) {
                        Log.e("deviceheght", String.valueOf(deviceheight));
                        lp1 = new ConstraintLayout.LayoutParams(0, deviceheight);
                    } else
                        lp1 = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getViewHeight(context, component.getLayout(), 1));
                    componentViewResult.componentView.setLayoutParams(lp1);
                } else {
                    ConstraintLayout.LayoutParams lp2;
                    if (uiBlockName == UI_BLOCK_AUTHENTICATION_14)
                        lp1 = new ConstraintLayout.LayoutParams(20, (int) getViewHeight(context, component.getLayout(), 1));
//                            else if(uiBlockName==UI_BLOCK_AUTHENTICATION_01)
//                              lp1 = new ConstraintLayout.LayoutParams(0, getDeviceHeight());
                    else if (componentKey == PAGE_FILL_BACKGROUND)
                        lp1 = new ConstraintLayout.LayoutParams(0, getDeviceHeight());
                    else if (componentKey == PAGE_VIEW_DETAILS_BACKGROUND || componentKey == PAGE_VIEW_TIMER_BACKGROUND) {
                        Log.e("deviceheght", String.valueOf(deviceheight));
                        lp1 = new ConstraintLayout.LayoutParams(0, deviceheight);
                    } else
                        lp1 = new ConstraintLayout.LayoutParams(0, (int) getViewHeight(context, component.getLayout(), 1));

                    componentViewResult.componentView.setLayoutParams(lp1);
                }
                break;

            case PAGE_BLOCK_VIEW_KEY:
                componentViewResult.componentView = new View(context);
                int width = (int) getViewWidth(context, component.getLayout(), 0);
                int height = (int) getViewHeight(context, component.getLayout(), 0);
                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(width > 0 ? width : 0, height > 0 ? height : 0));
                break;

            case PAGE_CHECKBOX_KEY: {
                componentViewResult.componentView = new CheckBox(context);
                componentViewResult.componentView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (componentKey == CARD_CHECKBOX_KEY) {
                    ((CheckBox) componentViewResult.componentView).setChecked(true);
                    if (appCMSPresenter.getAllowedPayMethods() != null) {
                        ((CheckBox) componentViewResult.componentView).setClickable(false);
                    }
                }
                break;
            }

        } // End of switch (componentType) {
        if (componentViewResult.componentView != null && component.getId() != null) {
            componentViewResult.componentView.setId(IdUtils.getID(component.getId()));
            int lp = 0, rp = 0, tp = 0, bp = 0;
            if (component.getLeftpadding() != 0) {
                lp = (int) convertDpToPixel(component.getLeftpadding(), context);
            }
            if (component.getTopPadding() != 0) {
                tp = (int) convertDpToPixel(component.getTopPadding(), context);
            }
            if (component.getRightPadding() != 0) {
                rp = (int) convertDpToPixel(component.getRightPadding(), context);
            }
            if (component.getBottomPadding() != 0) {
                bp = (int) convertDpToPixel(component.getBottomPadding(), context);
            }
            componentViewResult.componentView.setPadding(lp, tp, rp, bp);
        }

        if (componentViewResult.onInternalEvent != null) {
            appCMSPresenter.addInternalEvent(componentViewResult.onInternalEvent);
        }
        return componentViewResult.componentView;

    }

    public synchronized void setComponentViewRelativePosition(Context context,
                                                              View view,
                                                              final ContentDatum data,
                                                              Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                                              final String componentViewType,
                                                              Component component,
                                                              ConstraintLayout constraintLayout, String blockName, ModuleWithComponents moduleInfo,
                                                              MetadataMap metadataMap) {
        AppCMSUIKeyType moduleType = jsonValueKeyMap.get(componentViewType);
        final Component childComponent = component;
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);
        if (moduleType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }
        int childViewWidth = (int) com.viewlift.views.customviews.BaseView.getViewWidth(context,
                childComponent.getLayout(),
                ViewGroup.LayoutParams.MATCH_PARENT);
        if (childViewWidth == -1)
            childViewWidth = BaseView.getDeviceWidth();
        int childViewHeight = (int) com.viewlift.views.customviews.BaseView.getViewHeight(context,
                childComponent.getLayout(),
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (childComponent != null) {
            AppCMSUIKeyType appCMSUIcomponentViewType = jsonValueKeyMap.get(componentViewType);
            AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
            AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
            AppCMSUIKeyType uiBlockName = jsonValueKeyMap.get(blockName);

            if (blockName == null) {
                uiBlockName = PAGE_EMPTY_KEY;
            }
            if (appCMSUIcomponentViewType == null) {
                appCMSUIcomponentViewType = PAGE_EMPTY_KEY;
            }
            if (componentType == null) {
                componentType = PAGE_EMPTY_KEY;
            }
            if (componentKey == null) {
                componentKey = PAGE_EMPTY_KEY;
            }

            switch (componentType) {
                case VIEW_TABLE_LAYOUT:
                    switch (componentKey) {
                        case TABLE_PLAN_FEATURES:
                            ((PlanFeaturesLayout) view).initializeView(moduleAPIPub);
                            break;
                    }
                    break;

                case DETAIL_PAGE_TRAILER: {
                    //  if (appCMSPresenter.getTrailerPlayerView().isVideoPaused())
                    {
                        set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                        thumbnailImage.setVisibility(VISIBLE);
                    }
//                    else {
//                        set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
//                        thumbnailImage.setVisibility(INVISIBLE);
//                    }

                    String trailerId = null;
                    String promoId = null;
                    if (appCMSPresenter.getEpisodePromoID() != null && !appCMSPresenter.getEpisodePromoID().isEmpty()) {
                        promoId = appCMSPresenter.getEpisodePromoID();
                    } else
                        promoId = null;
                    /* this code comment for to show thumbnail insted of trailer*/
//                                if (appCMSPresenter.getPlayshareControl()) {
//                                    if (appCMSPresenter.getseriesID() != null && appCMSPresenter.getSeasonEpisodeAdapterData() != null &&
//                                            appCMSPresenter.getSeasonEpisodeAdapterData().size() > 0 && appCMSPresenter.getSeasonEpisodeAdapterData().get(0).getShowDetails() != null &&
//                                            appCMSPresenter.getSeasonEpisodeAdapterData().get(0).getShowDetails().getTrailers() != null &&
//                                            !appCMSPresenter.getSeasonEpisodeAdapterData().get(0).getShowDetails().getTrailers().isEmpty())
//                                        trailerId = appCMSPresenter.getSeasonEpisodeAdapterData().get(0).getShowDetails().getTrailers().get(0).getId();
//                                    else
//                                        trailerId = null;
//                                }
                    if (appCMSPresenter.getPlayshareControl()) {
                        if (appCMSPresenter.getEpisodeTrailerID() != null) {
                            trailerId = appCMSPresenter.getEpisodeTrailerID();
                        } else
                            trailerId = null;
                    } else {
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getId() != null &&
                                data.getShowDetails() != null &&
                                data.getShowDetails().getTrailers() != null &&
                                !data.getShowDetails().getTrailers().isEmpty()) {
                            trailerId = data.getShowDetails().getTrailers().get(0).getId();
                        }
                    }
                    if (trailerId != null && !trailerId.isEmpty() && trailerId.equalsIgnoreCase(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_unable_to_play_video_error_title))) ||
                            (((CustomVideoPlayerView) view).getVideoId() != null && ((CustomVideoPlayerView) view).getVideoId().equalsIgnoreCase(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_unable_to_play_video_error_title))))) {
                        if (!((CustomVideoPlayerView) view).getVideoId().equalsIgnoreCase(trailerId)) {
                            traierPlayComplete = false;
                            // ((CustomVideoPlayerView) view).setVideoUri(trailerId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), true);
                        }
                    } else {
                        // if (trailerId != null)
                        //   ((CustomVideoPlayerView) view).setVideoUri(trailerId, appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), true);
                    }
                    ContentDatum contentDatum = appCMSPresenter.getshowdetailsContenData();
                    boolean isLiveStream = contentDatum != null && data != null &&
                            contentDatum.getGist() != null &&
                            contentDatum.getGist().isLiveStream();
                    if (appCMSPresenter.getseriesID() == null) {
                        thumbnailImage.setVisibility(INVISIBLE);
                        appCMSPresenter.getTrailerPlayerView().setVisibility(VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (appCMSPresenter.getTrailerPlayerView() != null && appCMSPresenter.getPlayshareControl()) {
                                    thumbnailImage.setVisibility(INVISIBLE);
                                    appCMSPresenter.getTrailerPlayerView().setVisibility(VISIBLE);
                                }
                            }
                        }, 1100);

                    }
                    if (((trailerId != null && !trailerId.isEmpty())) && isLiveStream && promoId == null) {
                        playVideo(((CustomVideoPlayerView) view), true, promoId);
                    } else if ((trailerId != null && !trailerId.isEmpty()) || (promoId != null && !promoId.isEmpty())) {
                        ShowDetailsPromoHandler.getInstance().openTrailer(view, thumbnailImage, trailerId, promoId, isLiveStream, moduleAPIPub, appCMSPresenter, data);
                        //openTrailer(view, thumbnailImage, trailerId, promoId, isLiveStream);
                    } else if (trailerId == null && promoId == null && appCMSPresenter.getseriesID() != null) {
                        ShowDetailsPromoHandler.getInstance().playVideoIfPromoTrailerUnavailable(view, thumbnailImage, moduleAPIPub, appCMSPresenter, contentDatum);
                    }
                    //  TrailerPlayBack.trailerPlayBack().openTrailer(view, trailerId,promoId,appCMSPresenter);
                    view.setOnClickListener(null);

                }
                break;
                case PAGE_SEASON_TAB_MODULE_KEY: {
                    view.setLayoutParams(new Constraints.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                break;
                case PAGE_VOD_MORE_RELATED_KEY:
                case PAGE_VOD_YOU_MAY_LIKE_KEY: {
                    view.setLayoutParams(new Constraints.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50));
                }
                break;
                case PAGE_CONSTRAINT_GUIDE_LINE_KEY: {
                    switch (componentKey) {
                        case PAGE_CONSTRAINT_GUIDE_LINE_TOP:
                        case PAGE_CONSTRAINT_GUIDE_LINE_BOTTOM: {
                            // set.create(view.getId(),ConstraintSet.HORIZONTAL_GUIDELINE);
                            //  set.setGuidelinePercent(view.getId(),0.15f);
                            //set.setGuidelineBegin(view.getId(),BaseView.dpToPx(R.dimen.nav_image_height));
                        }
                        break;
                        case PAGE_CONSTRAINT_GUIDE_LINE_LEFT:
                        case PAGE_CONSTRAINT_GUIDE_LINE_RIGHT: {
                            //set.create(view.getId(),ConstraintSet.VERTICAL_GUIDELINE);
                            //set.setGuidelineBegin(view.getId(),50);
                        }
                        break;
                        case PAGE_CONSTRAINT_GUIDE_LINE_CENTER:
                            break;
                    }
                } //end case PAGE_CONSTRAINT_GUIDE_LINE_KEY:
                break;
                case PAGE_COUNTDOWN_TIMER_VIEW: {
                    if (data.getGist() != null) {
                        long eventDate = (data.getGist().getScheduleStartDate());
                        //calculate remaining time from event date and current date
                        //System.out.println("eventDate :- "+eventDate+ ""+System.currentTimeMillis());
                        //eventDate = System.currentTimeMillis()+ 10000;

                        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");
                        //System.out.println("eventDate remainingTime:- "+remainingTime);
                        boolean isEventSchedule = false;
                        String coverImage = null;

                        if (data != null &&
                                data.getGist() != null
                                && data.getGist().getEventSchedule() != null
                                && data.getGist().getEventSchedule().size() > 0
                                && data.getGist().getEventSchedule().get(0) != null
                                && data.getGist().getEventSchedule().get(0).getEventTime() != 0) {
                            eventDate = data.getGist().getEventSchedule().get(0).getEventTime();

                            //calculate remaining time from event date and current date
                            remainingTime = CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss");
                            isEventSchedule = true;
                        }

                        if (remainingTime > 0) {
                            ((TimerViewFutureContent) view).initializeView(null, "");
                            ((TimerViewFutureContent) view).startTimer(context, eventDate, remainingTime, isEventSchedule);
                        }
                    }
                }
                break;
                case PAGE_IMAGE_KEY: {
                    if (!(view instanceof ImageView))
                        break;
                    String ratio = BaseView.getViewRatio(context, childComponent.getLayout(), "16:9");
                    int placeholder = ImageUtils.getPlaceHolderByRatio(ratio, false);
                    if (component.getTintColor() != null) {
                        ((ImageView) view).setColorFilter(Color.parseColor(component.getTintColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                    }
                    RequestOptions requestOptions;
                    switch (componentKey) {
                        case APP_LOGO_KEY:
                            ((ImageView) view).setImageResource(R.drawable.logo_icon);
                            break;
                        case WALLET_IMAGE_KEY:
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            placeholder = R.drawable.vid_image_placeholder_square;
                            if (moduleInfo != null && moduleInfo.getSettings() != null && moduleInfo.getSettings().isNetbankingEnabled() && !moduleInfo.getSettings().isBankDropDownList()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else if (TextUtils.isEmpty(childComponent.getIcon_url())) {
                                requestOptions = new RequestOptions()
                                        .placeholder(placeholder)
                                        .override(childViewWidth, childViewHeight);
                                Glide.with(context)
                                        .load(ImageUtils.getImageResourceId(context, childComponent.getImageName()))
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            } else {
                                requestOptions = new RequestOptions()
                                        .placeholder(placeholder)
                                        .override(childViewWidth, childViewHeight);
                                Glide.with(context)
                                        .load(childComponent.getIcon_url())
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            }
                            //((ImageView) view).setOnClickListener(v -> appCMSPresenter.sendCloseOthersAction(null, true, false));
                            break;

                        case PAGE_BACK_BUTTON: {
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            requestOptions = new RequestOptions()
                                    .override(childViewWidth, childViewHeight);
                            Glide.with(context)
                                    .load(TextUtils.isEmpty(childComponent.getImageName()) ? R.drawable.left_arrow_black : ImageUtils.getImageResourceId(context, childComponent.getImageName()))
                                    .apply(requestOptions)
                                    .into((ImageView) view);
                            (view).setOnClickListener(v -> appCMSPresenter.sendCloseOthersAction(null, true, false));
                            break;
                        }

                        case PAGE_CLOSE:
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            requestOptions = new RequestOptions()
                                    .override(childViewWidth, childViewHeight);
                            Glide.with(context)
                                    .load(R.drawable.ic_close_black)
                                    .apply(requestOptions)
                                    .into((ImageView) view);
                            view.setOnClickListener(v -> appCMSPresenter.sendCloseOthersAction(null, true, false));
                            break;
                        case PAGE_RIGHT_ARROW_KEY:
                            ((ImageView) view).setImageResource(R.drawable.ic_right_arrow);
                            if (moduleAPIPub != null && moduleAPIPub.getSettings() != null
                                    && moduleAPIPub.getSettings().getShowMore()) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }
                            view.setOnClickListener(v -> {
                                String action = component.getAction();
                                if (action != null && action.equalsIgnoreCase(context.getResources().getString(R.string.app_cms_action_browse_shows_key))) {
                                    if (moduleAPIPub.getTitle() != null) {
                                        appCMSPresenter.navigateToBrowsePage(moduleAPIPub.getTitle(), null);
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
                        case PAGE_PROFILE_RIGHT_ARROW_KEY:
                            view.setBackgroundResource(R.drawable.ic_right_arrow);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            view.setOnClickListener(v -> {
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
                        case PAGE_SHOW_GRADIENT_IMAGE_KEY: {
                            set.setDimensionRatio(view.getId(), ratio);
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            view.setLayoutParams(new ConstraintLayout.LayoutParams(childViewWidth, childViewHeight));
                            // ((ImageView) view).setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            //((ImageView) view).setImageResource(R.drawable.ic_gradient);
                            //((ImageView) view).setBackgroundResource(R.drawable.ic_gradient);
                            view.setBackgroundColor(Color.BLUE);


                            /*try {

                                requestOptions = new RequestOptions()
                                        // .transform(gradientTransform)
                                        .override(childViewWidth, childViewHeight)
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(placeholder);
                                //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                                Glide.with(context)
                                        .load(R.drawable.ic_gradient)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            } catch (IllegalArgumentException e) {
                                //Log.e(TAG, "Failed to load carousel_gradient with Glide: " + e.toString());
                            }*/
                        }
                        break;
                        case PAGE_SHOW_EPISODE_IMAGE_KEY: {
                            set.setDimensionRatio(view.getId(), ratio);

                            String imageUrl = null;
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            requestOptions = new RequestOptions()
                                    // .transform(gradientTransform)
                                    .override(childViewWidth, childViewHeight)
                                    .fitCenter()
                                    .error(ContextCompat.getDrawable(context, R.drawable.vid_image_placeholder_16x9))
                                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                            //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                            if (appCMSPresenter.isUserLoggedIn() &&
                                    data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getId() != null) {

                                int finalChildViewHeight = childViewHeight;
                                int finalChildViewWidth = childViewWidth;
                                appCMSPresenter.getLastWatchedSeriesData(data.getGist().getId(), appCMSSeriesHistoryList -> {
                                    SeriesHistory seriesHistory = null;
                                    String episodeImageUrl = null;
                                    if (appCMSSeriesHistoryList != null) {
                                        for (SeriesHistory tempSeriesHistory : appCMSSeriesHistoryList) {
                                            if (tempSeriesHistory.isLatestEpisode()) {
                                                seriesHistory = tempSeriesHistory;
                                                break;
                                            }
                                        }

                                        for (Season_ season : data.getSeason()) {

                                            for (ContentDatum episode : season.getEpisodes()) {
                                                if (episode.getGist().getId().equalsIgnoreCase(seriesHistory.getVideoId())) {

                                                    if (episode.getGist() != null
                                                            && episode.getGist().getImageGist() != null
                                                            && episode.getGist().getImageGist().get_16x9() != null) {
                                                        episodeImageUrl = episode.getGist().getImageGist().get_16x9();
                                                        finalEpisodeData = episode;
                                                        break;
                                                    }

                                                }
                                            }
                                        }
                                    } else {
                                        //if (seriesHistory == null || episodeImageUrl == null) {
                                        if (data != null &&
                                                data.getSeason() != null &&
                                                data.getSeason().size() > 0 &&
                                                data.getSeason().get(0).getEpisodes() != null &&
                                                data.getSeason().get(0).getEpisodes().size() > 0 &&
                                                data.getSeason().get(0).getEpisodes().get(0) != null &&
                                                data.getSeason().get(0).getEpisodes().get(0).getGist() != null &&
                                                data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist() != null &&
                                                data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist().get_16x9() != null &&
                                                !TextUtils.isEmpty(data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist().get_16x9())) {

                                            finalEpisodeData = data.getSeason().get(0).getEpisodes().get(0);

                                            if (ratio.contains("16:9")) {
                                                episodeImageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        //data.getGist().getImageGist().get_16x9(), // To be remove
                                                        data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist().get_16x9(),
                                                        finalChildViewWidth,
                                                        finalChildViewHeight);

                                            } else {
                                                episodeImageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getSeason().get(0).getEpisodes().get(0).getGist().getVideoImageUrl(),
                                                        finalChildViewWidth,
                                                        finalChildViewHeight);
                                            }
                                        }
                                    }

                                    if (episodeImageUrl != null) {
                                        try {

                                            Glide.with(context)
                                                    .load(episodeImageUrl)
                                                    .apply(requestOptions)
                                                    .into((ImageView) view);
                                        } catch (IllegalArgumentException e) {
                                            //Log.e(TAG, "Failed to load carousel_gradient with Glide: " + e.toString());
                                        }
                                    } else {
                                        ((ImageView) view).setImageResource(R.drawable.vid_image_placeholder_16x9);
                                    }

                                });
                            } else if (data != null &&
                                    data.getSeason() != null &&
                                    data.getSeason().size() > 0 &&
                                    data.getSeason().get(0).getEpisodes() != null &&
                                    data.getSeason().get(0).getEpisodes().size() > 0 &&
                                    data.getSeason().get(0).getEpisodes().get(0) != null &&
                                    data.getSeason().get(0).getEpisodes().get(0).getGist() != null &&
                                    data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist() != null &&
                                    data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist().get_16x9() != null &&
                                    !TextUtils.isEmpty(data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist().get_16x9())) {

                                finalEpisodeData = data.getSeason().get(0).getEpisodes().get(0);

                                if (ratio.contains("16:9")) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            //data.getGist().getImageGist().get_16x9(), // To be remove
                                            data.getSeason().get(0).getEpisodes().get(0).getGist().getImageGist().get_16x9(),
                                            childViewWidth,
                                            childViewHeight);

                                } else {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getSeason().get(0).getEpisodes().get(0).getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }

                                view.setBackgroundColor(ContextCompat.getColor(context,
                                        android.R.color.transparent));

                                try {


                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(requestOptions)
                                            .into((ImageView) view);
                                } catch (IllegalArgumentException e) {
                                    //Log.e(TAG, "Failed to load carousel_gradient with Glide: " + e.toString());
                                }


                            } else {
                                ((ImageView) view).setImageResource(R.drawable.vid_image_placeholder_16x9);
                            }
                        }
                        break;
                        case PAGE_SERIES_IMAGE_KEY:
                            set.setDimensionRatio(view.getId(), ratio);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            if (data != null &&
                                    data.getGist() != null &&
                                    !TextUtils.isEmpty(data.getGist().getVideoImageUrl())) {
                                String imageUrl = null;
                                if (ratio.contains("32:9")
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_32x9() != null) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_32x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }
                                view.setBackgroundColor(ContextCompat.getColor(context,
                                        android.R.color.transparent));
                                try {
                                    if (!TextUtils.isEmpty(imageUrl)) {

                                        requestOptions = new RequestOptions()
                                                .override(childViewWidth, childViewHeight)
                                                .fitCenter()
                                                .error(ContextCompat.getDrawable(context, R.drawable.vid_image_placeholder_16x9))
                                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                                .placeholder(placeholder);
                                        Glide.with(context)
                                                .load(imageUrl)
                                                .apply(requestOptions)
                                                .into((ImageView) view);
                                    }
                                } catch (IllegalArgumentException e) {
                                }
                            } else {
                                ((ImageView) view).setImageResource(R.drawable.vid_image_placeholder_32x9);
                            }
                            break;

                        case PAGE_BANNER_IMAGE:
                            set.setDimensionRatio(view.getId(), ratio);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            if (moduleInfo != null && moduleInfo.getSettings() != null && moduleInfo.getSettings().getImageURL_16x9() != null) {
                                String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleInfo.getSettings().getImageURL_16x9(),
                                        childViewWidth,
                                        childViewHeight);

                                view.setBackgroundColor(ContextCompat.getColor(context,
                                        android.R.color.transparent));
                                try {
                                    requestOptions = new RequestOptions()
                                            .override(childViewWidth, childViewHeight)
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(requestOptions)
                                            .into((ImageView) view);
                                } catch (IllegalArgumentException e) {
                                }
                            } else {
                                ((ImageView) view).setImageResource(R.drawable.vid_image_placeholder_16x9);
                            }
                            break;
                        case PAGE_AUTOPLAY_MOVIE_IMAGE_KEY:
                            set.setDimensionRatio(view.getId(), ratio);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            if (data != null &&
                                    data.getGist() != null &&
                                    !TextUtils.isEmpty(data.getGist().getVideoImageUrl())) {
                                String imageUrl = null;
                                if (ratio.contains("16:9")
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_16x9() != null) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_16x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("3:4")
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_3x4() != null) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_3x4(),
                                            childViewWidth,
                                            childViewHeight);
                                } else {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }
                                view.setBackgroundColor(ContextCompat.getColor(context,
                                        android.R.color.transparent));
                                try {
                                    requestOptions = new RequestOptions()
                                            .override(childViewWidth, childViewHeight)
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(requestOptions)
                                            .into((ImageView) view);
                                } catch (IllegalArgumentException e) {
                                }
                            } else {
                                ((ImageView) view).setImageResource(R.drawable.vid_image_placeholder_16x9);
                            }
                            break;
                        case PAGE_CAROUSEL_IMAGE_KEY:
                        case PAGE_CAROUSEL_BADGE_IMAGE_KEY:
                        case PAGE_THUMBNAIL_IMAGE_KEY:
                        case PAGE_THUMBNAIL_VIDEO_IMAGE_KEY:
                        case PAGE_VIDEO_IMAGE_KEY:
                        case PAGE_SHOW_IMAGE_KEY:
                        case PAGE_BADGE_IMAGE_KEY:
                        case PAGE_THUMBNAIL_BADGE_IMAGE:
                        case PAGE_BADGE_DETAIL_PAGE_IMAGE:
                        case PAGE_PHOTO_PLAYER_IMAGE: {

                                /*set.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                                set.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

                                if (componentKey == PAGE_CAROUSEL_IMAGE_KEY
                                        || componentKey == PAGE_CAROUSEL_BADGE_IMAGE_KEY) {
                                    set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
                                    set.connect(((ImageView) view).getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                                }*/
                            // ((ImageView) view).setAdjustViewBounds(true);
                            set.setDimensionRatio(view.getId(), ratio);
                            if (componentViewType != null &&
                                    !componentViewType.isEmpty() &&
                                    ((blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04)))
                                            || componentViewType.contains("AC FullWidthCarousel 01"))) {

                                childViewWidth = BaseView.getDeviceWidth();

                            } else if (componentKey == PAGE_PHOTO_PLAYER_IMAGE) {
                                childViewWidth = com.viewlift.views.customviews.BaseView.getDeviceWidth();
                            } else if (componentKey == PAGE_THUMBNAIL_VIDEO_IMAGE_KEY) {
                                if (BaseView.isTablet(context)) {
                                    childViewWidth = com.viewlift.views.customviews.BaseView.getDeviceWidth() * 60 / 100;
                                }
                            } else if (componentKey == PAGE_BADGE_DETAIL_PAGE_IMAGE) {
                                //childViewWidth = com.viewlift.views.customviews.BaseView.getDeviceWidth() * 20 / 100;
                                //placeholder = R.drawable.vid_image_placeholder_square;
                            }
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            String imageUrl = "";
                            if (data != null) {
                                if ((appCMSUIcomponentViewType == PAGE_TRAY_08_MODULE_KEY ||
                                        appCMSUIcomponentViewType == PAGE_BRAND_TRAY_MODULE_KEY)
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_3x4() != null) {
                                    childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_3x4(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("3:4")
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_3x4() != null) {
                                    childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_3x4(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("1:1")
                                        && data.getGist() != null
                                        && data.getGist().getBadgeImages() != null
                                        && data.getGist().getBadgeImages().get_1x1() != null
                                        && componentKey == PAGE_BADGE_DETAIL_PAGE_IMAGE) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            TextUtils.isEmpty(data.getGist().getBadgeImages().get_1x1()) ? brandImage1x1 : data.getGist().getBadgeImages().get_1x1(),
                                            childViewWidth,
                                            childViewHeight);
                                    view.setBackgroundResource(R.drawable.outer_background);
                                } else if (ratio.contains("1:1")
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_1x1() != null) {
                                    childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_1x1(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (((moduleType == PAGE_SCHEDULE_CAROUSEL_MODULE_KEY &&
                                        BaseView.isTablet(context))
                                        || (moduleType == PAGE_BRAND_CAROUSEL_MODULE_TYPE &&
                                        BaseView.isLandscape(context)))
                                        && data.getGist() != null &&
                                        data.getGist().getImageGist() != null &&
                                        data.getGist().getImageGist().get_32x9() != null) {
                                    // placeholder = R.drawable.vid_image_placeholder_32x9;
                                    // placeholder = R.drawable.vid_image_placeholder_16x9;
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_32x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("32:9")
                                        && data.getGist() != null && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_32x9() != null
                                        && componentKey == PAGE_CAROUSEL_IMAGE_KEY) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_32x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("32:9")
                                        && data.getGist() != null && data.getGist().getBadgeImages() != null
                                        && data.getGist().getBadgeImages().get_32x9() != null
                                        && componentKey == PAGE_CAROUSEL_BADGE_IMAGE_KEY) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getBadgeImages().get_32x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("16:9")
                                        && data.getGist() != null && data.getGist().getBadgeImages() != null
                                        && data.getGist().getBadgeImages().get_16x9() != null
                                        && (componentKey == PAGE_BADGE_IMAGE_KEY)) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getBadgeImages().get_16x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("1:1")
                                        && data.getGist() != null && data.getGist().getBadgeImages() != null
                                        && data.getGist().getBadgeImages().get_1x1() != null
                                        && componentKey == PAGE_BADGE_IMAGE_KEY) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getBadgeImages().get_1x1(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (ratio.contains("16:9")
                                        && data.getGist() != null && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_16x9() != null
                                        && componentKey == PAGE_VIDEO_IMAGE_KEY) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_16x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (data.getGist() != null && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_16x9() != null
                                        && (componentKey != PAGE_CAROUSEL_BADGE_IMAGE_KEY
                                        && componentKey != PAGE_BADGE_DETAIL_PAGE_IMAGE)) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_16x9(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (data.getGist() != null && data.getGist().getVideoImageUrl() != null
                                        && componentKey != PAGE_CAROUSEL_BADGE_IMAGE_KEY
                                        && componentKey != PAGE_BADGE_DETAIL_PAGE_IMAGE) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }
                            }
                            try {
                                if (component.isCircular() && blockName != null && appCMSPresenter.getCurrentActivity() != null &&
                                        blockName.equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.ui_block_contentBlock_01)) &&
                                        moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getImage() != null) {
                                    imageUrl = moduleAPIPub.getMetadataMap().getImage();
                                }
                                if (!TextUtils.isEmpty(imageUrl)) {
                                    requestOptions = new RequestOptions()
                                            .override(childViewWidth, childViewHeight)
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                            .placeholder(placeholder);
                                    if (component.isCircular()) {
                                        requestOptions.circleCrop();
                                        ((ImageView) view).setAdjustViewBounds(true);
                                    }

                                    Glide.with(context)
                                            .asBitmap()
                                            .load(imageUrl)
                                            .apply(requestOptions)
                                            .into((ImageView) view);


                                }
                            } catch (IllegalArgumentException e) {
                            }

                        }
                        break;
                        case PAGE_SETTINGS_OFFER_IMAGE_KEY:
                            set.setDimensionRatio(view.getId(), ratio);
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            String r_imageUrl = moduleInfo.getSettings().getImages().getPlanBackgroundImage();
                            try {
                                Drawable placeholderDrawable = resizePlaceholder(context, placeholder, childViewWidth,
                                        childViewHeight);
                                requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .placeholder(placeholder);

                                Glide.with(context)
                                        .load(r_imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);

                            } catch (IllegalArgumentException e) {
                            }
                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            break;
                        case OFFER_CROSS_IMAGE:
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            String r_image = moduleInfo.getSettings().getImages().getCrossImage();

                            try {
                                requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .placeholder(placeholder);

                                Glide.with(context)
                                        .load(r_image)
                                        .apply(requestOptions)
                                        .into((ImageView) view);

                            } catch (IllegalArgumentException e) {
                            }
                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            break;
                        case TRAILER_THUMBNAIL_IMAGE_KEY:
                            set.setDimensionRatio(view.getId(), ratio);
                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            String imageUrl = "";
                            if (appCMSPresenter.getShowDetailsGist() != null &&
                                    appCMSPresenter.getShowDetailsGist().getImageGist() != null && appCMSPresenter.getShowDetailsGist().getImageGist().get_16x9() != null)
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        appCMSPresenter.getShowDetailsGist().getImageGist().get_16x9(),
                                        childViewWidth,
                                        childViewHeight);
                            else if (data != null && data.getGist() != null && data.getGist().getImageGist() != null && data.getGist().getImageGist().get_16x9() != null)
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getImageGist().get_16x9(),
                                        childViewWidth,
                                        childViewHeight);
                            try {
                                requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .fitCenter()
                                        .error(ContextCompat.getDrawable(context, R.drawable.vid_image_placeholder_16x9))
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .placeholder(placeholder);

                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);

                            } catch (IllegalArgumentException e) {
                            }
                            CustomVideoPlayerView customVideoPlayerView = null;
                            customVideoPlayerView = constraintLayout.findViewById(R.id.videoTrailer);
                            if (customVideoPlayerView != null && uiBlockName == UI_BLOCK_SHOW_DETAIL_06 && view.getVisibility() == VISIBLE)
                                set.setVisibility(view.getId(), INVISIBLE);
                            view.setOnClickListener(null);
//                            view.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    /*TextView tv_Play = constraintLayout.findViewById(R.id.play);
//                                    if (tv_Play != null && tv_Play.getVisibility() == VISIBLE) {
//                                        ContentDatum data = appCMSPresenter.getshowdetailsContenData();
//                                        data.setModuleApi(moduleAPIPub);
//                                        if (data != null && data.getGist() != null) {
//                                            //    if(appCMSPresenter.getseriesID()==null)
//                                            appCMSPresenter.setShowDetailsGist(data.getGist());
//                                            appCMSPresenter.setPlayshareControl(true);
//                                        }
//                                        appCMSPresenter.setseriesID(null);
//                                        if (data != null)
//                                            appCMSPresenter.launchButtonSelectedAction(data.getGist().getPermalink(),
//                                                    context.getString(R.string.app_cms_action_videopage_key),
//                                                    data.getGist().getTitle(),
//                                                    null,
//                                                    data,
//                                                    false,
//                                                    0,
//                                                    null);
//                                    }*/
//                                }
//                            });


                            break;
                        case PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY: {
                            if (data != null && data.getGist() != null) {
                                view.setMinimumWidth(50);
                                view.setMinimumHeight(50);
                                long remainingTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleStartDate(), "yyyy MMM dd HH:mm:ss");
                                long remainingEndTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleEndDate(), "yyyy MMM dd HH:mm:ss");
                                if (remainingTime > 0 && data.getGist().getScheduleStartDate() > 0) {
                                    ((ImageView) view).setImageResource(R.drawable.ic_add_to_cal);
                                    view.setOnClickListener(v -> appCMSPresenter.getAppCalendarEvent().checkCalendarEventExistsAndAddEvent(data, appCMSPresenter));
                                } else if (remainingEndTime < 0 && data.getGist().getScheduleEndDate() > 0) {
                                    ((ImageView) view).setImageResource(R.drawable.ic_add_to_cal);
                                    view.setOnClickListener(v -> appCMSPresenter.getAppCalendarEvent().checkCalendarEventExistsAndAddEvent(data, appCMSPresenter));
                                }
                                if (data.getGist().isLiveStream()) {
                                    set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                }
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }
//                            ((ImageView) view).setImageResource(R.drawable.ic_add_to_cal);

                        }
                        break;
                    }  // end of switch (componentKey) {

                } // end of case PAGE_IMAGE_KEY:
                break;

                case PAGE_TEXTFIELD_KEY:
                    if (component.getHint() != null)
                        ((TextInputLayout) view).setHint(component.getHint());
                    if (component.getBackgroundColor() != null)
                        ((TextInputLayout) view).getEditText().setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    if (component.getTextColor() != null)
                        ((TextInputLayout) view).getEditText().setTextColor(Color.parseColor(component.getTextColor()));
                    else
                        ((TextInputLayout) view).getEditText().setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                    ((TextInputLayout) view).getEditText().setTextSize(getFontSize(context, childComponent.getLayout()));
                    view.setPadding(20, 0, 0, 0);

                    setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, ((TextInputLayout) view).getEditText());
                    setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, view);
                    if (!TextUtils.isEmpty(component.getHint())) {
                        ((TextInputLayout) view).getEditText().setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getHint()) != null ?
                                appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getHint()) : component.getHint());
                    }
                    appCMSPresenter.setCursorDrawableColor(((TextInputLayout) view).getEditText(), Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor()));
                    if (component.getNumberOfLines() != 0) {
                        ((TextInputLayout) view).getEditText().setSingleLine(true);
                        ((TextInputLayout) view).getEditText().setMaxLines(component.getNumberOfLines());
                    }
                    if (component.getHintColor() != null)
                        ((TextInputLayout) view).getEditText().setHintTextColor(Color.parseColor(component.getHintColor()));
                    switch (componentKey) {
                        case PAGE_EMAILTEXTFIELD_KEY:
                            if (component.getBackgroundColor() != null) {
                                view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                            } else
                                view.setBackgroundResource(android.R.color.white);
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getEmailInput() != null)
                                ((TextInputLayout) view).getEditText().setHint(moduleAPIPub.getMetadataMap().getEmailInput());
                            if (component.getMaxLenght() != 0)
                                ((TextInputLayout) view).getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(component.getMaxLenght())});
                            break;
                        case PAGE_NUMBERTEXTFIELD_KEY:
                            view.setBackgroundResource(android.R.color.white);
                            ((TextInputLayout) view).getEditText()
                                    .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                            break;
                        case PAGE_PASSWORD_INPUT:
                            //   textInputEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            if (component.getBackgroundColor() != null)
                                view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                            else
                                view.setBackgroundResource(android.R.color.white);
                            ((TextInputLayout) view).setPasswordVisibilityToggleEnabled(true);
                            ((TextInputLayout) view).getEditText()
                                    .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            ((TextInputLayout) view).getEditText()
                                    .setTransformationMethod(PasswordTransformationMethod.getInstance());
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getPasswordInput() != null)
                                ((TextInputLayout) view).getEditText().setHint(moduleAPIPub.getMetadataMap().getPasswordInput());
                            if (component.getMaxLenght() != 0)
                                ((TextInputLayout) view).getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(component.getMaxLenght())});
                            break;
                        case PAGE_EDIT_PROFILE_USERNAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getUsername() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getUsername());
                                ((TextInputLayout) view).getEditText().setEnabled(false);
                            }
                            break;
                        case PAGE_EDIT_PROFILE_LOCATION_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getLocation() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getLocation());
                            }
                            break;
                        case PAGE_EDIT_PROFILE_FIRST_NAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getName() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getName());
                            }
                            break;
                        case PAGE_EDIT_PROFILE_LAST_NAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getLastName() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getLastName());
                            }
                            break;
                        case PAGE_EDIT_PROFILE_ZIPCODE_VALUE_KEY:
                            ((TextInputLayout) view).getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                            ((TextInputLayout) view).getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getZipCode() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getZipCode());
                            }
                            break;

                    }
                    break;
                case VIEW_TYPE_TEXT_INPUT_LAYOUT:
                    if (component.getHint() != null)
                        ((TextInputLayout) view).setHint(component.getHint());
//                    ((TextInputLayout) view).setHintTextAppearance(R.style.TextAppearence_App_TextInputLayout_Profile);
                    if (component.getBackgroundColor() != null)
                        ((TextInputLayout) view).getEditText().setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    if (component.getTextColor() != null)
                        ((TextInputLayout) view).getEditText().setTextColor(Color.parseColor(component.getTextColor()));
                    else
                        ((TextInputLayout) view).getEditText().setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                    ((TextInputLayout) view).getEditText().setTextSize(getFontSize(context, childComponent.getLayout()));
                    ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, ((TextInputLayout) view).getEditText());
                    ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, view);
                    appCMSPresenter.setCursorDrawableColor(((TextInputLayout) view).getEditText(), Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor()));
                    switch (componentKey) {
                        case PAGE_EDIT_PROFILE_USERNAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getUsername() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getUsername());
                                ((TextInputLayout) view).getEditText().setEnabled(false);
                            }
                            break;
                        case PAGE_EDIT_PROFILE_LOCATION_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getLocation() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getLocation());
                            }
                            break;
                        case PAGE_EDIT_PROFILE_FIRST_NAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getName() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getName());
                            }
                            break;
                        case PAGE_EDIT_PROFILE_LAST_NAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getLastName() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getLastName());
                            }
                            break;
                        case PAGE_EDIT_PROFILE_ZIPCODE_VALUE_KEY:
                            ((TextInputLayout) view).getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                            ((TextInputLayout) view).getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getZipCode() != null) {
                                ((TextInputLayout) view).getEditText().setText(data.getUserMeResponse().getProfile().getZipCode());
                            }
                            break;
                    }
                    break;
                case PAGE_EDIT_TEXT_KEY:
                    if (component.getHint() != null)
                        ((EditText) view).setHint(component.getHint());
                    if (component.getHintColor() != null)
                        ((EditText) view).setHintTextColor(Color.parseColor(component.getHintColor()));
                    if (component.getTextColor() != null)
                        ((EditText) view).setTextColor(Color.parseColor(component.getTextColor()));
                    else
                        ((EditText) view).setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                    ((EditText) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                    ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, childComponent, view);
                    appCMSPresenter.setCursorDrawableColor(((EditText) view), appCMSPresenter.getBrandPrimaryCtaTextColor());

                    switch (componentKey) {
                        case CARD_EDIT_TEXT_KEY: {
                            EditText editText = ((EditText) view);
                            int padding = BaseView.dpToPx(component.getPadding());
                            editText.setPadding(padding, 0, padding, 0);
                            editText.setCursorVisible(true);
                            editText.setLongClickable(false);
                            editText.setTextIsSelectable(false);
                            appCMSPresenter.setCursorDrawableColor(editText, 0);

                            if (component.getMaxLength() > 0)
                                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(component.getMaxLength())});

                            if (!TextUtils.isEmpty(component.getBackgroundColor()))
                                editText.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));

                            if (IdUtils.getID(component.getId()) != R.id.cardNameEditText)
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            else {
                                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                                editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                            }

                            if (IdUtils.getID(component.getId()) == R.id.cardCVVEditText) {
                                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                                editText.setTransformationMethod(new PasswordTransformationMethod());
                            }

                            if (IdUtils.getID(component.getId()) == R.id.cardNoEditText) {
                                editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                        new Handler().postDelayed(editText::requestFocus, 100);
                                    }
                                });
                            }

                            editText.setOnTouchListener((view1, motionEvent) -> {
                                focusAndShowKeyboard(view1.getContext(), editText);
                                return true;
                            });


                            editText.setOnEditorActionListener((exampleView, actionId, event) -> {
                                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_GO || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                                    EditText nextEditText = null;
                                    switch (editText.getId()) {
                                        case R.id.cardNoEditText:
                                            nextEditText = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardMMEditText);
                                            break;
                                        case R.id.cardMMEditText:
                                            nextEditText = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardYYEditText);
                                            break;
                                        case R.id.cardYYEditText:
                                            nextEditText = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardCVVEditText);
                                            break;
                                        case R.id.cardCVVEditText:
                                            nextEditText = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardNameEditText);
                                            break;
                                    }

                                    if (nextEditText != null) {
                                        nextEditText.requestFocus();
                                        return true;
                                    }
                                    return false;
                                } else {
                                    return false;
                                }
                            });

                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    EditText etMonth = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardMMEditText);
                                    EditText etYear = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardYYEditText);
                                    String addedNo = editText.getText().toString();
                                    if (editText.getId() == R.id.cardMMEditText && addedNo.length() == 2) {
                                        int number = Integer.parseInt(addedNo);
                                        int mm = Calendar.getInstance().get(Calendar.MONTH) + 1;
                                        int yy = Calendar.getInstance().get(Calendar.YEAR) % 100;
                                        if (!TextUtils.isEmpty(etYear.getText().toString())) {
                                            int eYear = Integer.parseInt(etYear.getText().toString());
                                            if (eYear == yy && number < mm) {
                                                appCMSPresenter.showToast(appCMSPresenter.getCurrentActivity().getString(R.string.please_fill_valid_details), Toast.LENGTH_SHORT);
                                                etMonth.setText("");
                                                return;
                                            }
                                        }
                                        if (number > 12) {
                                            appCMSPresenter.showToast(appCMSPresenter.getCurrentActivity().getString(R.string.please_fill_valid_details), Toast.LENGTH_SHORT);
                                            editText.setText("");
                                            return;
                                        }
                                    } else if (editText.getId() == R.id.cardYYEditText && addedNo.length() == 2) {
                                        int number = Integer.parseInt(addedNo);
                                        int mm = Calendar.getInstance().get(Calendar.MONTH) + 1;
                                        int yy = Calendar.getInstance().get(Calendar.YEAR) % 100;

                                        if (!TextUtils.isEmpty(etMonth.getText().toString())) {
                                            int eMonth = Integer.parseInt(etMonth.getText().toString());
                                            if (eMonth < mm && number == yy) {
                                                appCMSPresenter.showToast(appCMSPresenter.getCurrentActivity().getString(R.string.please_fill_valid_details), Toast.LENGTH_SHORT);
                                                etMonth.setText("");
                                                return;
                                            }
                                        }
                                        if (number < yy || number > (yy + 10)) {
                                            appCMSPresenter.showToast(appCMSPresenter.getCurrentActivity().getString(R.string.please_fill_valid_details), Toast.LENGTH_SHORT);
                                            editText.setText("");
                                            return;
                                        }
                                    }

                                    EditText etCardNo = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardNoEditText);
                                    EditText etCvv = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardCVVEditText);
                                    int maxLength = component.getMaxLength();
                                    if (editText != null && editText.getId() == R.id.cardNoEditText) {
                                        maxLength = CardValidator.getCardNumberLength(s.toString());
                                        etCardNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                                    } else if (editText != null && editText.getId() == R.id.cardCVVEditText) {
                                        maxLength = CardValidator.getCvvNumberLength(etCardNo.getText().toString());
                                        etCvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                                    }

                                    if (maxLength > 0 && editText != null && editText.getText().toString().length() == maxLength) {
                                        EditText nextEditText = null;
                                        switch (editText.getId()) {
                                            case R.id.cardNoEditText:
                                                nextEditText = etMonth;
                                                break;
                                            case R.id.cardMMEditText:
                                                nextEditText = etYear;
                                                break;
                                            case R.id.cardYYEditText:
                                                nextEditText = etCvv;
                                                break;
                                            case R.id.cardCVVEditText:
                                                nextEditText = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardNameEditText);
                                                break;
                                        }

                                        if (nextEditText != null)
                                            nextEditText.requestFocus();
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                    for (UnderlineSpan span : s.getSpans(0, s.length(), UnderlineSpan.class)) {
                                        s.removeSpan(span);
                                    }
                                }
                            });
                        }

                        break;

                        case UPI_EDIT_TEXT_KEY: {
                            EditText editText = ((EditText) view);
                            editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    editText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    new Handler().postDelayed(() -> {
                                        if (component.getNumberOfLines() > 0) {
                                            editText.setMaxLines(component.getNumberOfLines());
                                        }
                                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                                        editText.setImeOptions(EditorInfo.IME_ACTION_SEND);
                                    }, 100);
                                }
                            });

                            int padding = (int) BaseView.convertDpToPixel(component.getPadding(), context);
                            view.setPadding(padding, padding, padding, padding);
                            appCMSPresenter.setCursorDrawableColor(editText, 0);
                            applyBorderToComponent(context, view, component, -1);
                            editText.setOnEditorActionListener((v, actionId, event) -> {
                                if (actionId == EditorInfo.IME_ACTION_SEND) {
                                    Button btnPay = appCMSPresenter.getCurrentActivity().findViewById(R.id.upiPayBtn);
                                    if (btnPay != null) {
                                        btnPay.performClick();
                                        return true;
                                    }
                                    return false;
                                } else {
                                    return false;
                                }
                            });

                            editText.addTextChangedListener(new TextWatcher() {

                                List<String> upiEndPoints = Arrays.asList(appCMSPresenter.getCurrentActivity().getResources().getStringArray(R.array.app_cms_upi_end_point));

                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
                                        Button upiBtn = appCMSPresenter.getCurrentActivity().findViewById(R.id.upiPayBtn);
                                        String upi = s.toString();
                                        if (upiBtn == null) {
                                            return;
                                        }
                                        if (!TextUtils.isEmpty(upi) && upi.length() > 3 && upi.indexOf("@") > 0 && upiEndPoints.contains(upi.substring(upi.indexOf("@") + 1).toLowerCase())) {
                                            upiBtn.setAlpha(1);
                                            upiBtn.setEnabled(true);
                                        } else {
                                            upiBtn.setAlpha(0.5f);
                                            upiBtn.setEnabled(false);
                                        }
                                    }
                                }
                            });
                        }

                        break;

                        default:
                            break;
                    }

                case PAGE_LABEL_KEY: {
                    if (childComponent.getText() != null) {
                        ((TextView) view).setText(childComponent.getText());
                    }
                    if (childComponent.getTextAlignment() != null) {
                        setTextGravity(((TextView) view), jsonValueKeyMap.get(childComponent.getTextAlignment()));
                    }
                    ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, childComponent, view);
                    ((TextView) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                    if (childComponent.getBackgroundColor() != null && childComponent.getBackgroundColor().contains("#")) {
                        view.setBackgroundColor(Color.parseColor(childComponent.getBackgroundColor()));
                    }
                    if (childComponent.getTextColor() != null && childComponent.getTextColor().contains("#")) {
                        ((TextView) view).setTextColor(Color.parseColor(childComponent.getTextColor()));
                    } else {
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                    }
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    switch (componentKey) {
                        case TEXT_VIEW_PLAN_TERMS:
                            ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getTnCext());
                            ((TextView) view).setLinkTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                            ClickableSpan tosClick = new ClickableSpan() {
                                @Override
                                public void onClick(@Nonnull View view) {
                                    appCMSPresenter.navigatToTOSPage(null, null);
                                }
                            };
                            ClickableSpan privacyClick = new ClickableSpan() {
                                @Override
                                public void onClick(@Nonnull View view) {
                                    appCMSPresenter.navigateToPrivacyPolicy(null, null);
                                }
                            };

                            appCMSPresenter.makeTextViewLinks(((TextView) view), new String[]{
                                    appCMSPresenter.getLocalisedStrings().getTermsOfUsesText(), appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText()}, new ClickableSpan[]{tosClick, privacyClick}, true);
                            break;
                        case PAGE_PLAN_BENEFIT1:
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getDescription() != null) {
                                set.constrainWidth(((TextView) view).getId(), (BaseView.getDeviceWidth() - (BaseView.getDeviceWidth() * 20 / 100)));

                                if (moduleAPIPub.getDescription().contains(" HD ")) {
                                    Spannable buttonLabel = new SpannableString(moduleAPIPub.getDescription());
                                    int startIndexOfLink = moduleAPIPub.getDescription().indexOf("HD");
                                    buttonLabel.setSpan(new ImageSpan(context, R.drawable.hd_ico,
                                            ImageSpan.ALIGN_BOTTOM), startIndexOfLink, startIndexOfLink + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    ((TextView) view).setText(buttonLabel);

                                } else
                                    ((TextView) view).setText(moduleAPIPub.getDescription());
                            }
                            break;
                        case PAGE_TERM_AND_CONDITION_PLAN:
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getTnCext());
                            ((TextView) view).setLinkTextColor(appCMSPresenter.getButtonBorderColor());
                            ((TextView) view).setPadding(10, 20, 0, 20);
                            set.constrainWidth(((TextView) view).getId(), (BaseView.getDeviceWidth() - (BaseView.getDeviceWidth() * 20 / 100)));
                            ClickableSpan tosClick1 = new ClickableSpan() {
                                @Override
                                public void onClick(@Nonnull View view) {
                                    if (!Strings.isEmptyOrWhitespace(context.getResources().getString(R.string.terms_of_service)) && appCMSPresenter.getNavigation().getNavigationFooter() != null) {
                                        for (int i = 0; i < appCMSPresenter.getNavigation().getNavigationFooter().size(); i++) {
                                            NavigationFooter navigationFooter = appCMSPresenter.getNavigation().getNavigationFooter().get(i);
                                            if (navigationFooter.getTitle().equalsIgnoreCase(context.getResources().getString(R.string.terms_of_service))) {
                                                if (appCMSPresenter.isAncillaryPage(navigationFooter.getPageId())) {
                                                    appCMSPresenter.openFooterPage(navigationFooter, navigationFooter.getTitle());
                                                } else {
                                                    appCMSPresenter.navigatToTOSPage(null, null);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            };
                            ClickableSpan privacyClick1 = new ClickableSpan() {
                                @Override
                                public void onClick(@Nonnull View view) {
                                    if (!Strings.isEmptyOrWhitespace(appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText()) && appCMSPresenter.getNavigation().getNavigationFooter() != null) {
                                        for (int i = 0; i < appCMSPresenter.getNavigation().getNavigationFooter().size(); i++) {
                                            NavigationFooter navigationFooter = appCMSPresenter.getNavigation().getNavigationFooter().get(i);
                                            if (navigationFooter.getTitle().equalsIgnoreCase(appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText())) {
                                                if (appCMSPresenter.isAncillaryPage(navigationFooter.getPageId())) {
                                                    appCMSPresenter.openFooterPage(navigationFooter, navigationFooter.getTitle());
                                                } else {
                                                    appCMSPresenter.navigateToPrivacyPolicy(null, null);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                }
                            };

                            appCMSPresenter.makeTextViewLinks(((TextView) view), new String[]{
                                    appCMSPresenter.getLocalisedStrings().getTermsOfUsesText(), appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText()}, new ClickableSpan[]{tosClick1, privacyClick1}, true);
                            componentViewResult.addToPageView = true;
                            break;
                        case PAGE_SETTINGS_NAME_VALUE_KEY:
                            //((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            ((TextView) view).setText(appCMSPresenter.getLoggedInUserName());
                            break;
                        case PAGE_OFFER_PLAN_NAME_KEY:
                            if (moduleAPIPub != null && moduleAPIPub.getContentData() != null && moduleAPIPub.getContentData().size() > 0) {
                                ((TextView) view).setText("" + moduleAPIPub.getContentData().get(0).getName());
                                ((TextView) view).setTextSize(component.getFontSize());
                            }
                            break;
                        case PAGE_OFFER_REFERRED_BY_KEY:
                            ((TextView) view).setTextSize(component.getFontSize());
                            ((TextView) view).setText(moduleAPIPub.getDescription());
                            ((TextView) view).setMaxLines(2);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            break;
                        case PAGE_OFFER_PRICE_KEY:
                            if (moduleAPIPub != null && moduleAPIPub.getContentData() != null && moduleAPIPub.getContentData().size() > 0) {
                                int price1 = (int) moduleAPIPub.getContentData().get(0).getPlanDetails().get(0).getRecurringPaymentAmount();
                                Currency currency = Currency.getInstance(data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode());
                                ((TextView) view).setText("" + currency.getSymbol() + price1);
                                ((TextView) view).setTextSize(component.getFontSize());
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            }
                            break;
                        case STRIKE_THROUGH_PRICE:
                            if (moduleAPIPub != null && moduleAPIPub.getContentData() != null && moduleAPIPub.getContentData().size() > 0) {
                                Currency currency = Currency.getInstance(data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode());
                                int price1 = (int) moduleAPIPub.getContentData().get(0).getPlanDetails().get(0).getStrikeThroughPrice();
                                ((TextView) view).setText("" + currency.getSymbol() + price1);
                            }
                            ((TextView) view).setTextSize(component.getFontSize());
                            break;

                        case TEXT_VIEW_PLAN:
                            if (appCMSPresenter.isAppAVOD()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else {
                                if (metadataMap != null && metadataMap.getSubscriptionPlanHeader() != null)
                                    ((TextView) view).setText(metadataMap.getSubscriptionPlanHeader());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) view).setMaxLines(component.getNumberOfLines());
                                }
                            }
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            break;
                        case PAGE_SETTINGS_PLAN_PRICE_VALUE_KEY: {
                            if (moduleInfo != null && moduleInfo.getSettings() != null
                                    && moduleInfo.getSettings().isShowResubscribeFlow()
                                    && appCMSPresenter.getAppPreference().getActiveSubscriptionPrice() != null) {
                                String priceInfo = CommonUtils.getPlanPriceDetail(appCMSPresenter, metadataMap);
                                ((TextView) view).setText(priceInfo);
                                ((TextView) view).setSingleLine();
                                if (appCMSPresenter.getActiveSubscriptionStatus() != null &&
                                        (appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_deferred_cancellation))
                                                || appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_cancelled))
                                                || appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_suspended)))) {
                                    ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                }
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                        }
                        break;
                        case PAGE_SETTINGS_PLAN_VALUE_KEY: {
                            if (appCMSPresenter.isUserSubscribed() &&
                                    !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getActiveSubscriptionPlanName())) {
                                if (appCMSPresenter.getUserSubscriptionPlanTitle() != null)
                                    ((TextView) view).setText(appCMSPresenter.getUserSubscriptionPlanTitle());
                                else
                                    ((TextView) view).setText(appCMSPresenter.getAppPreference().getActiveSubscriptionPlanName());
                            } else if (!appCMSPresenter.isUserSubscribed()) {
                                if (metadataMap != null && metadataMap.getkNotSubscribed() != null)
                                    ((TextView) view).setText(metadataMap.getkNotSubscribed());
                                else
                                    ((TextView) view).setText(context.getString(R.string.subscription_unsubscribed_plan_value));
                            }
                            ((TextView) view).setId(R.id.planValue);
                            if (moduleInfo != null && moduleInfo.getSettings() != null
                                    && moduleInfo.getSettings().isShowResubscribeFlow()
                                    && appCMSPresenter.getActiveSubscriptionStatus() != null
                                    && (appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_deferred_cancellation))
                                    || appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_cancelled))
                                    || appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_suspended)))) {
                                ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() | (Paint.STRIKE_THRU_TEXT_FLAG));
                                if (appCMSPresenter.getAppPreference().getActiveSubscriptionPlanName() != null) {
                                    ((TextView) view).setText(appCMSPresenter.getAppPreference().getActiveSubscriptionPlanName());
                                }
                            }
                        }
                        break;
                        case PAGE_SETTINGS_NEXT_BILLING_DUE_DATE_VALUE_KEY:
                            if (appCMSPresenter.getActiveSubscriptionEndDate() != null && appCMSPresenter.isUserSubscribed()) {
                                if (appCMSPresenter.getAppPreference().getActiveSubscriptionEndDate().contains("T"))
                                    ((TextView) view).setText(CommonUtils.getDatebyDefaultZone(appCMSPresenter.getAppPreference().getActiveSubscriptionEndDate(), "MM/dd/yyyy"));
                                else
                                    ((TextView) view).setText(appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("dd-MM-yyyy", appCMSPresenter.getAppPreference().getActiveSubscriptionEndDate()), "MM/dd/yyyy"));
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                            break;
                        case PAGE_SETTINGS_NEXT_BILLING_DUE_DATE_KEY:
                            if (!appCMSPresenter.isUserSubscribed()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else {
                                if (moduleAPIPub.getMetadataMap() != null) {
                                    if (appCMSPresenter.getAppPreference().getActiveSubscriptionStatus() != null) {
                                        if (appCMSPresenter.getAppPreference().getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_completed)))
                                            ((TextView) view).setText(moduleAPIPub.getMetadataMap().getNextDateLabel());
                                        if (appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_refund))) {
                                            ((TextView) view).setText(moduleAPIPub.getMetadataMap().getNextDateLabel());
                                        }
                                        if (appCMSPresenter.getAppPreference().getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_deferred_cancellation))) {
                                            ((TextView) view).setText(moduleAPIPub.getMetadataMap().getDeferredSubscriptionCancelDateLabel());
                                        }
                                    } else {
                                        ((TextView) view).setText(moduleAPIPub.getMetadataMap().getNextDateLabel());
                                    }
                                }
                            }
                            break;

                        case PAGE_SETTINGS_PLAN_PROCESSOR_TITLE_KEY: {
                            if (appCMSPresenter.isUserSubscribed() &&
                                    !TextUtils.isEmpty(appCMSPresenter.getActiveSubscriptionProcessor())) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) view).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);

                            }
                            if (metadataMap != null && metadataMap.getPaymentProcessorHeader() != null)
                                ((TextView) view).setText(metadataMap.getPaymentProcessorHeader());
                            else if (!TextUtils.isEmpty(component.getText())) {
                                ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) != null ?
                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()) : component.getText());
                                ((TextView) view).setSingleLine(true);

                            }
                        }
                        break;
                        case PAGE_SETTINGS_PHONE_LABEL_KEY:
                            if (metadataMap != null && metadataMap.getPhone() != null) {
                                ((TextView) view).setText(metadataMap.getPhone());
                            } else {
                                ((TextView) view).setText(component.getText());
                            }
                            break;
                        case PAGE_SETTINGS_PHONE_VALUE_KEY:
                            if (isSiteOTPEnabled(appCMSPresenter) && isValidPhoneNumber(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
                                ((TextView) view).setText(appCMSPresenter.getAppPreference().getLoggedInUserPhone());
                            }

                            break;
                        case TEXT_VIEW_NAME:
                            if (metadataMap != null && metadataMap.getName() != null) {
                                ((TextView) view).setText(metadataMap.getName());
                            }
                            break;
                        case TEXT_VIEW_EMAIL:
                            if (metadataMap != null && metadataMap.getEmail() != null) {
                                ((TextView) view).setText(metadataMap.getEmail());
                            }
                            break;
                        case PAGE_SETTINGS_EMAIL_VALUE_KEY:
                            ((TextView) view).setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail());
                            break;
                        case PAGE_SETTINGS_PLAN_CANCEL_SCHEDULE_LABEL_KEY: {
                            if (moduleInfo != null && moduleInfo.getSettings() != null
                                    && moduleInfo.getSettings().isShowResubscribeFlow()
                                    && appCMSPresenter.getActiveSubscriptionStatus() != null
                                    && appCMSPresenter.getActiveSubscriptionStatus().equalsIgnoreCase(context.getString(R.string.subscription_status_deferred_cancellation))) {

                                String msg = appCMSPresenter.getLocalisedStrings().getSchedulToCancelMSG();
                                if (appCMSPresenter.getActiveSubscriptionEndDate() != null) {
                                    msg += " " + CommonUtils.getDatebyDefaultZone(appCMSPresenter.getActiveSubscriptionEndDate(), "MM/dd/YYYY");
                                }
                                try {
                                    TextView textViewPlan = constraintLayout.findViewById(R.id.planValue);
                                    textViewPlan.setPaintFlags(textViewPlan.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    View paymentProcess = constraintLayout.findViewById(R.id.paymentProcess);
                                    View paymentProcessorValue = constraintLayout.findViewById(R.id.paymentProcessorValue);
                                    set.setVisibility(paymentProcess.getId(), ConstraintSet.GONE);
                                    set.setVisibility(paymentProcessorValue.getId(), ConstraintSet.GONE);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                ((TextView) view).setText(msg.toUpperCase());
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                        }
                        break;
                        case PAGE_SETTINGS_PLAN_PROCESSOR_VALUE_KEY: {
                            String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();
                            if (paymentProcessor != null && appCMSPresenter.isUserSubscribed()) {
                                ((TextView) view).setText(paymentProcessor);
                            } else {
                                ((TextView) view).setText("");
                            }
                        }
                        break;

                        case PAGE_SETTINGS_DOWNLOAD_QUALITY_PROFILE_KEY:
                            if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                                    appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                                if (appCMSPresenter.getUserDownloadQualityPref() != null) {
                                    if (appCMSPresenter.getUserDownloadQualityPref().equalsIgnoreCase(context.getString(R.string.default_video_resolution))) {
                                        appCMSPresenter.getAppPreference().setUserDownloadQualityPositionref(0);
                                        ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadHighText());
                                    } else if (appCMSPresenter.getUserDownloadQualityPref().equalsIgnoreCase(context.getString(R.string.default_low_video_resolution))) {
                                        appCMSPresenter.getAppPreference().setUserDownloadQualityPositionref(1);
                                        ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDowloadMediumText());
                                    } else {
                                        ((TextView) view).setText(appCMSPresenter.getUserDownloadQualityPref());
                                    }
                                } else
                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadHighText());
                            }
                            break;
                        case PAGE_SETTINGS_APP_VERSION_VALUE_KEY:
                            ((TextView) view).setText(context.getString(R.string.app_cms_app_version));
                            break;

                        case PAGE_SETTINGS_TITLE_KEY:
                            ((TextView) view)
                                    .setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                                            .getBrand()
                                            .getGeneral()
                                            .getBlockTitleColor()));
                            if (metadataMap != null && metadataMap.getAccountHeader() != null)
                                ((TextView) view).setText(metadataMap.getAccountHeader());
                            break;
                        case TEXT_VIEW_APP_VERSION:
                            if (metadataMap != null && metadataMap.getAppVersionLabel() != null)
                                ((TextView) view).setText(metadataMap.getAppVersionLabel());
                            break;

                        case TEXT_VIEW_SUBSCRIPTION_AND_BILLING:
                            ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                            if (appCMSPresenter.isUserSubscribed()) {
                                if (metadataMap != null && metadataMap.getSubscriptionHeader() != null)
                                    ((TextView) view).setText(metadataMap.getSubscriptionHeader());
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                            break;

                        case TEXT_VIEW_APP_SETTINGS:
                            ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                            if (metadataMap != null && metadataMap.getAppSettingsLabel() != null)
                                ((TextView) view).setText(metadataMap.getAppSettingsLabel().toUpperCase());
                            break;

                        case TEXT_VIEW_DOWNLOAD_SETTINGS:
                            ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                            if (metadataMap != null && metadataMap.getDownloadSettingsLabel() != null)
                                ((TextView) view).setText(metadataMap.getDownloadSettingsLabel().toUpperCase());
                            break;
                        case TEXT_VIEW_DOWNLOAD_QUALITY:
                            if (metadataMap != null && metadataMap.getDownloadQualityLabel() != null)
                                ((TextView) view).setText(metadataMap.getDownloadQualityLabel());
                            if (!appCMSPresenter.isDownloadable())
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            break;
                        case TEXT_VIEW_CELLULAR_DATA:
                            if (metadataMap != null && metadataMap.getCellularDataLabel() != null)
                                ((TextView) view).setText(metadataMap.getCellularDataLabel());
                            if (!appCMSPresenter.isDownloadable())
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            break;

                        case PAGE_SD_CARD_FOR_DOWNLOADS_TEXT_KEY:
                            if (metadataMap != null && metadataMap.getUseSdCardForDownloadsLabel() != null)
                                ((TextView) view).setText(metadataMap.getUseSdCardForDownloadsLabel());
                            if (component.getNumberOfLines() != 0) {
                                ((TextView) view).setMaxLines(component.getNumberOfLines());
                            }
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            break;
                        case PAGE_USER_MANAGEMENT_AUTOPLAY_TEXT_KEY: {
                            if (metadataMap != null && metadataMap.getAutoplayLabel() != null)
                                ((TextView) view).setText(metadataMap.getAutoplayLabel());
                            if (!appCMSPresenter.isAutoPlayEnable()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                                componentViewResult.shouldHideComponent = true;
                            }
                        }
                        break;
                        case PAGE_USER_MANAGEMENT_CAPTION_TEXT_KEY: {
                            if (metadataMap != null && metadataMap.getCloseCaptioningLabel() != null)
                                ((TextView) view).setText(metadataMap.getCloseCaptioningLabel());

                        }
                        break;
                        case PAGE_USER_MANAGEMENT_DOWNLOAD_VIDEO_QUALITY_TEXT_KEY: {
                            if (metadataMap != null && metadataMap.getDownloadQualityTitle() != null)
                                ((TextView) view).setText(metadataMap.getDownloadQualityTitle());
                            if (!appCMSPresenter.isDownloadable()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                                componentViewResult.shouldHideComponent = true;
                            }
                        }
                        break;
                        case PAGE_USER_MANAGEMENT_CELL_DATA_TEXT_KEY: {
                            if (metadataMap != null && metadataMap.getCellularDataLabel() != null)
                                ((TextView) view).setText(metadataMap.getCellularDataLabel());
                        }
                        break;
                        case PAGE_SETTING_PERSONALIZATION_KEY:
                            ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                            if (appCMSPresenter != null && appCMSPresenter.getLocalisedStrings() != null) {
                                ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getPersonalizeSettingsHeader());
                            }
                            break;
                        case EDIT_PERSONALIZATION_HEADER:
                            if (appCMSPresenter != null && appCMSPresenter.getLocalisedStrings() != null) {
                                ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getManagePersonalizationText());
                            }

                            if (appCMSPresenter.isPersonalizationEnabled()) {
                                view.setOnClickListener(v -> {
                                    appCMSPresenter.showLoader();
                                    AppCMSPresenter.isFromSettings = true;
                                    String currentUserId = appCMSPresenter.getLoggedInUser() != null ? appCMSPresenter.getLoggedInUser() : "guest-user-id";
                                    appCMSPresenter.getUserRecommendedGenres(currentUserId, s -> {
                                        appCMSPresenter.setSelectedGenreString(s);
                                        if (appCMSPresenter.isPersonalizationEnabled()) {

                                            Handler handler = new Handler();
                                            handler.postDelayed(() -> {
                                                try {
                                                    appCMSPresenter.stopLoader();
                                                    appCMSPresenter.showRecommendationGenreDialog(null);
                                                } catch (Exception e) {
                                                }
                                            }, 10);
                                        }
                                    }, false, true);
                                });
                            } else {
                                set.setVisibility(view.getId(), GONE);
                            }
                            break;
                        case PAGE_SETTINGS_PARENTAL_CONTROLS_HEADER_KEY: {
                            ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                            if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getParentalControlHeader())) {
                                ((TextView) view).setText(metadataMap.getParentalControlHeader());
                            } else {
                                ((TextView) view).setText(component.getText());
                            }
                            break;
                        }
                        case TEXT_VIEW_SAVE:
                            if (appCMSPresenter.getPlayshareControl())
                                ((ConstraintLayout) (view.getParent())).setBackground(CustomShape.createRoundedRectangleDrawable(CMSColorUtils.INSTANCE.lightenColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()), 0.1f)));
                            ContentDatum contentDat = appCMSPresenter.getshowdetailsContenData();
                            if (contentDat != null && contentDat.getGist() != null && contentDat.getGist().getId() != null) {
                                if (appCMSPresenter.isFilmAddedToWatchlist(contentDat.getGist().getId())) {
                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getRemoveFromWatchlistText());
                                } else {
                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getAddToWatchlistText());
                                }
                            }
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_add, 0);
                            Drawable[] saveDrawables = ((TextView) view).getCompoundDrawables();
                            saveDrawables[2].setColorFilter(new PorterDuffColorFilter(Color.parseColor(childComponent.getTextColor()), PorterDuff.Mode.SRC_IN));
                            ((TextView) view).setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.nav_item_left_right_padding));
                            ((TextView) view).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ContentDatum contentDatum = appCMSPresenter.getshowdetailsContenData();
                                    if (contentDatum != null && contentDatum.getGist() != null && contentDatum.getGist().getId() != null) {
                                        String videoId = contentDatum.getGist().getOriginalObjectId();
                                        if (videoId == null) {
                                            videoId = contentDatum.getGist().getId();
                                        }
                                        if (appCMSPresenter.isFilmAddedToWatchlist(videoId)) {
                                            appCMSPresenter.showLoader();
                                            appCMSPresenter.addToWatchList(false, contentDatum, constraintLayout, jsonValueKeyMap.get(childComponent.getKey()));
                                        } else {
                                            appCMSPresenter.showLoader();
                                            appCMSPresenter.addToWatchList(true, contentDatum, constraintLayout, jsonValueKeyMap.get(childComponent.getKey()));
                                        }
                                    }

                                }
                            });
                            break;
                        case TEXT_VIEW_PLAY:
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_news_play, 0);
                            if (appCMSPresenter.getPlayshareControl())
                                ((ConstraintLayout) (view.getParent())).setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                            ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getPlayText());
                            ((TextView) view).setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.nav_item_left_right_padding));
                            Drawable[] playDrawables = ((TextView) view).getCompoundDrawables();
                            playDrawables[2].setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, android.R.color.black), PorterDuff.Mode.SRC_IN));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (appCMSPresenter.getTrailerPlayerView() != null && appCMSPresenter.getTrailerPlayerView().getVisibility() == VISIBLE) {
                                        appCMSPresenter.getTrailerPlayerView().checkVideoStatus(data.getGist().getId(), data);
                                        appCMSPresenter.dismissPopupWindowPlayer(true);
                                    }
                                }
                            }, 1000);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ContentDatum data = appCMSPresenter.getshowdetailsContenData();

                                    if (data != null && data.getGist() != null) {
                                        //    if(appCMSPresenter.getseriesID()==null)
                                        data.setModuleApi(moduleAPIPub);
                                        appCMSPresenter.setShowDetailsGist(data.getGist());
                                        appCMSPresenter.setPlayshareControl(true);
                                    }
                                    appCMSPresenter.setEpisodeId(null);
                                    appCMSPresenter.setEpisodeTrailerID(null);
                                    appCMSPresenter.setEpisodePromoID(null);
                                    if (data != null) {
                                        if (thumbnailImage.getVisibility() == VISIBLE) {
                                            appCMSPresenter.getTrailerPlayerView().setVisibility(VISIBLE);
                                            thumbnailImage.setVisibility(INVISIBLE);
                                        }
                                        appCMSPresenter.dismissPopupWindowPlayer(true);
                                        data.setModuleApi(moduleAPIPub);
                                        appCMSPresenter.getTrailerPlayerView().setVideoContentData(data);
                                        appCMSPresenter.getTrailerPlayerView().setUseAdUrl(false);
                                        appCMSPresenter.getTrailerPlayerView().setVideoUri(data.getGist().getId(),
                                                appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, data);
                                        appCMSPresenter.getTrailerPlayerView().releasePreviousAdsPlayer();
                                        appCMSPresenter.getTrailerPlayerView().enableController();
                                        appCMSPresenter.getTrailerPlayerView().setUseController(true);
                                        appCMSPresenter.getTrailerPlayerView().setEpisodePlay(true);

                                    }
                                }
                            });

                            break;

                        case TEXT_VIEW_SHARE:
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_news_share, 0);
                            ((TextView) view).setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.nav_item_left_right_padding));
                            if (appCMSPresenter.getPlayshareControl())
                                ((ConstraintLayout) (view.getParent())).setBackground(CustomShape.createRoundedRectangleDrawable(CMSColorUtils.INSTANCE.lightenColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()), 0.1f)));
                            if (!TextUtils.isEmpty(metadataMap.getShareLabel()))
                                ((TextView) view).setText(metadataMap.getShareLabel());
                            Drawable[] shareDrawables = ((TextView) view).getCompoundDrawables();
                            shareDrawables[2].setColorFilter(new PorterDuffColorFilter(Color.parseColor(childComponent.getTextColor()), PorterDuff.Mode.SRC_IN));
                            view.setOnClickListener(v -> {
                                StringBuilder filmUrl = new StringBuilder();
                                filmUrl.append(appCMSPresenter.getAppCMSMain().getDomainName());

                                if (appCMSPresenter.getshowdetailsContenData() != null && appCMSPresenter.getshowdetailsContenData().getGist() != null &&
                                        appCMSPresenter.getshowdetailsContenData().getGist().getTitle() != null &&
                                        appCMSPresenter.getshowdetailsContenData().getGist().getPermalink() != null) {
                                    filmUrl.append(appCMSPresenter.getshowdetailsContenData().getGist().getPermalink());
                                }
                                String[] extraData = new String[1];
                                extraData[0] = filmUrl.toString();
                                data.setModuleApi(moduleAPIPub);
                                if (appCMSPresenter.getshowdetailsContenData() != null) {
                                    appCMSPresenter.setShowDetailsGist(appCMSPresenter.getshowdetailsContenData().getGist());
                                    appCMSPresenter.setPlayshareControl(true);
                                }
                                appCMSPresenter.setEpisodeId(null);
                                appCMSPresenter.launchButtonSelectedAction(moduleAPIPub.getContentData().get(0).getGist().getPermalink(),
                                        component.getAction(),
                                        moduleAPIPub.getContentData().get(0).getGist().getTitle(),
                                        extraData,
                                        moduleAPIPub.getContentData().get(0),
                                        false,
                                        0,
                                        null);
                            });
                            break;
                        case TEXT_VIEW_DOWNLOAD:
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_news_download, 0);
                            ((TextView) view).setCompoundDrawablePadding(context.getResources().getDimensionPixelSize(R.dimen.nav_item_left_right_padding));
                            ((TextView) view).setSingleLine();
                            if (appCMSPresenter.getPlayshareControl())
                                ((ConstraintLayout) (view.getParent())).setBackground(CustomShape.createRoundedRectangleDrawable(CMSColorUtils.INSTANCE.lightenColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()), 0.1f)));
                            Drawable[] downloadDrawables = ((TextView) view).getCompoundDrawables();
                            downloadDrawables[2].setColorFilter(new PorterDuffColorFilter(Color.parseColor(childComponent.getTextColor()), PorterDuff.Mode.SRC_IN));
                            ContentDatum contentDatum = appCMSPresenter.getshowdetailsContenData();
                            TextView downloadText = ((TextView) view);
                            /*if (appCMSPresenter.getPlayshareControl() && !appCMSPresenter.getDownloadStatus().isEmpty())
                                ((TextView) view).setText(appCMSPresenter.getDownloadStatus());
                            else {*/
                            if (contentDatum != null && contentDatum.getGist() != null && contentDatum.getGist().getId() != null) {
                                updateDownloadText(contentDatum, downloadText, view);
                            }

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!appCMSPresenter.getUserAbleToDownload()) {
                                        ContentDatum contentDatum = appCMSPresenter.getshowdetailsContenData();
                                        if (contentDatum != null && contentDatum.getGist() != null) {
                                            appCMSPresenter.setShowDetailsGist(contentDatum.getGist());
                                            appCMSPresenter.setPlayshareControl(true);
                                        }
                                        Handler downloadHandler = new Handler();
                                        downloadHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                                                        view,
                                                        appCMSPresenter,
                                                        userVideoDownloadStatus -> {
                                                            if (userVideoDownloadStatus != null) {
                                                                if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_SUCCESSFUL) {

                                                                    contentDatum.getGist().setLocalFileUrl(userVideoDownloadStatus.getVideoUri());
                                                                    try {
                                                                        if (userVideoDownloadStatus.getSubtitlesUri().trim().length() > 10 &&
                                                                                contentDatum.getContentDetails() != null &&
                                                                                contentDatum.getContentDetails().getClosedCaptions().get(0) != null) {
                                                                            contentDatum.getContentDetails().getClosedCaptions().get(0).setUrl(userVideoDownloadStatus.getSubtitlesUri());
                                                                        }
                                                                    } catch (Exception e) {
                                                                        //Log.e(TAG, e.getMessage());
                                                                    }
                                                                    contentDatum.getGist().setDownloadStatus(userVideoDownloadStatus.getDownloadStatus());
                                                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadedLabelText());

                                                                } else if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_INTERRUPTED) {
                                                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadLowerCaseText());
                                                                } else if (userVideoDownloadStatus.getDownloadStatus() == STATUS_RUNNING) {
                                                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadingLabelText());
                                                                } else if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING) {
                                                                    ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadingLabelText());
                                                                }

                                                            }
                                                        },
                                                        appCMSPresenter.getLoggedInUser(), true, 5, appCMSPresenter.getAppPreference().getDownloadPageId());

                                            }
                                        }, 4000);
                                        if (contentDatum != null)
                                            appCMSPresenter.startVideoDownload(contentDatum, null);
                                    }
                                }
                            });
                            break;
                        case SEASON_KEY:
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
                            ((TextView) view).setCompoundDrawablePadding(18);
                            if (metadataMap.getSeasonsLabel() != null)
                                ((TextView) view).setText(metadataMap.getSeasonsLabel());
                            setTextViewDrawableColor(((TextView) view), Color.parseColor(childComponent.getTextColor()));
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.setpostion(null);
                                    CommonUtils.setFullDiscription(false);
                                    appCMSPresenter.setSegmentListSelcted(false);
                                    appCMSPresenter.setBottomSheetDialog(context, constraintLayout);
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                    setTextViewDrawableColor(((TextView) view), appCMSPresenter.getBrandPrimaryCtaColor());
                                    TextView segments = constraintLayout.findViewById(R.id.segments);
                                    segments.setTextColor(Color.parseColor(childComponent.getTextColor()));
                                    TextView episodes = constraintLayout.findViewById(R.id.episodes);
                                    episodes.setTextColor(Color.parseColor(childComponent.getTextColor()));
                                }
                            });
                            break;
                        case SEGMENTS_KEY:
                            view.setPadding(5, 5, 5, 5);
                            if (appCMSPresenter.getSegmentListSelcted()) {
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            }
                            if (metadataMap.getSegmentsLabel() != null)
                                ((TextView) view).setText(metadataMap.getSegmentsLabel());
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.setpostion(null);
                                    CommonUtils.setFullDiscription(false);
                                    appCMSPresenter.setSegmentListSelcted(true);
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                    TextView season = constraintLayout.findViewById(R.id.season);
                                    season.setTextColor(Color.parseColor(childComponent.getTextColor()));
                                    TextView episodes = constraintLayout.findViewById(R.id.episodes);
                                    episodes.setTextColor(Color.parseColor(childComponent.getTextColor()));
                                    setTextViewDrawableColor(season, Color.parseColor(childComponent.getTextColor()));
                                    appCMSPresenter.getRelatedVideos();
                                }
                            });
                            break;
                        case EPISODES_KEY:
                            view.setPadding(5, 5, 5, 5);
                            if (metadataMap.getEpisodesLabel() != null)
                                ((TextView) view).setText(metadataMap.getEpisodesLabel());
                            if (!appCMSPresenter.getSegmentListSelcted()) {
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            }
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CommonUtils.setpostion(null);
                                    CommonUtils.setFullDiscription(false);
                                    appCMSPresenter.setSegmentListSelcted(false);
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                    setTextViewDrawableColor(((TextView) view), appCMSPresenter.getBrandPrimaryCtaTextColor());
                                    TextView season = constraintLayout.findViewById(R.id.season);
                                    season.setTextColor(Color.parseColor(childComponent.getTextColor()));
                                    setTextViewDrawableColor(season, Color.parseColor(childComponent.getTextColor()));
                                    TextView segments = constraintLayout.findViewById(R.id.segments);
                                    segments.setTextColor(Color.parseColor(childComponent.getTextColor()));
                                    appCMSPresenter.getseasonEpisodeRefreshList();
                                }
                            });
                            break;
                        case PAGE_API_DESCRIPTION:
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getShortParagraph() != null) {
                                ((TextView) view).setText(moduleAPIPub.getMetadataMap().getShortParagraph());
                                ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            }
                            break;
                        case PAGE_API_TITLE:
                            if (uiBlockName == UI_BLOCK_HISTORY_01 || uiBlockName == UI_BLOCK_HISTORY_02 || uiBlockName == UI_BLOCK_HISTORY_04) {
                                if (metadataMap != null && metadataMap.getHistoryPageTitle() != null)
                                    ((TextView) view).setText(metadataMap.getHistoryPageTitle());
                                else
                                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.app_cms_page_history_title)));
                            } else if (uiBlockName == UI_BLOCK_DOWNLOADS_01 || uiBlockName == UI_BLOCK_DOWNLOADS_02 || uiBlockName == UI_BLOCK_DOWNLOADS_03) {
                                if (metadataMap != null && metadataMap.getMyDownloadLowerCase() != null)
                                    ((TextView) view).setText(metadataMap.getMyDownloadLowerCase());
                                else
                                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.app_cms_page_download_title)));
                            } else if (uiBlockName == UI_BLOCK_WATCHLIST_01 || uiBlockName == UI_BLOCK_WATCHLIST_02 || uiBlockName == UI_BLOCK_WATCHLIST_03 ||
                                    uiBlockName == UI_BLOCK_WATCHLIST_04) {
                                if (metadataMap != null && metadataMap.getWatchlistPageTitle() != null)
                                    ((TextView) view).setText(metadataMap.getWatchlistPageTitle());
                                else
                                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.app_cms_page_watchlist_title)));
                            } else if (uiBlockName == UI_BLOCK_USER_MANAGEMENT_01 || uiBlockName == UI_BLOCK_USER_MANAGEMENT_02) {
                                if (metadataMap != null && metadataMap.getSettingsLabel() != null) {
                                    ((TextView) view).setText(metadataMap.getSettingsLabel());
                                } else {
                                    ((TextView) view).setText(context.getString(R.string.page_title_settings));
                                }
                            } else if (moduleAPIPub != null && !TextUtils.isEmpty(moduleAPIPub.getTitle())) {
                                if (uiBlockName == UI_BLOCK_NEWS_TRAY_02 || uiBlockName == UI_BLOCK_TRAY_11) {
                                    ((TextView) view).setText(CommonUtils.capitalizeAll(moduleAPIPub.getTitle()));
                                } else
                                    ((TextView) view).setText(moduleAPIPub.getTitle());
                                ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                                if (uiBlockName == UI_BLOCK_SELECTPLAN_02) {
                                    setTypeFace(context,
                                            appCMSPresenter,
                                            jsonValueKeyMap,
                                            component,
                                            view);
                                }
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) view).setMaxLines(component.getNumberOfLines());
                                }
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            } else if (jsonValueKeyMap.get(componentViewType) == PAGE_LIBRARY_01_MODULE_KEY ||
                                    jsonValueKeyMap.get(componentViewType) == PAGE_LIBRARY_02_MODULE_KEY) {
                                ((TextView) view).setText(R.string.app_cms_page_mylibrary_title);
                            } else if (moduleAPIPub != null &&
                                    moduleAPIPub.getContentData() != null &&
                                    moduleAPIPub.getContentData().size() > 0 &&
                                    moduleAPIPub.getContentData().get(0) != null &&
                                    moduleAPIPub.getContentData().get(0).getGist() != null &&
                                    moduleAPIPub.getContentData().get(0).getGist().getTitle() != null) {
                                ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getGist().getTitle());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) view).setSingleLine(false);
                                    ((TextView) view).setMaxLines(component.getNumberOfLines());
                                }
                                //((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());

                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                if (moduleAPIPub.getContentData().get(0).getGist().getContentType() != null &&
                                        moduleAPIPub.getContentData().get(0).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_event))) {
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                } else if (jsonValueKeyMap.get(componentViewType) == PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                                }
                            }
                            ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                            break;


                        case PAGE_PLAN_TITLE:
                            if (moduleAPIPub != null && !TextUtils.isEmpty(moduleAPIPub.getTitle())) {
                                ((TextView) view).setText(moduleAPIPub.getTitle());
                                ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                                if (uiBlockName == UI_BLOCK_SELECTPLAN_02) {
                                    setTypeFace(context,
                                            appCMSPresenter,
                                            jsonValueKeyMap,
                                            component,
                                            view);
                                    componentViewResult.addToPageView = true;
                                }
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) view).setMaxLines(component.getNumberOfLines());
                                }
                                //((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            } else if (moduleAPIPub != null &&
                                    moduleAPIPub.getContentData() != null &&
                                    moduleAPIPub.getContentData().size() > 0 &&
                                    moduleAPIPub.getContentData().get(0) != null &&
                                    moduleAPIPub.getContentData().get(0).getGist() != null &&
                                    moduleAPIPub.getContentData().get(0).getGist().getTitle() != null) {
                                ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getGist().getTitle());
                                if (component.getNumberOfLines() != 0) {
                                    ((TextView) view).setSingleLine(false);
                                    ((TextView) view).setMaxLines(component.getNumberOfLines());
                                }
                                //((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());

                                // ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            }
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            //    ((TextView) view).setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());

                            break;
                        case TEXT_VIEW_CONTENT_TITLE:
                            if (appCMSPresenter.getShowDetailsGist() != null &&
                                    appCMSPresenter.getShowDetailsGist().getTitle() != null)
                                ((TextView) view).setText(appCMSPresenter.getShowDetailsGist().getTitle());
                            else if (moduleAPIPub != null && moduleAPIPub.getContentData() != null && !moduleAPIPub.getContentData().isEmpty()
                                    && moduleAPIPub.getContentData().get(0).getGist() != null && moduleAPIPub.getContentData().get(0).getGist().getTitle() != null)
                                ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getGist().getTitle());
                            ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            view.setOnClickListener(null);
                            break;
                        case TEXT_VIEW_CONTENT_DESCRIPTION:
                            ((TextView) view).setPadding(0, -7, 0, 3);
                            if (childComponent.getNumberOfLines() != 0) {
                                ((TextView) view).setSingleLine(false);
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            }
                            int dis_max_length;
                            if (BaseView.isTablet(context))
                                dis_max_length = context.getResources().getInteger(R.integer.app_cms_beacon_buffering_timeout_msec);
                            else
                                dis_max_length = context.getResources().getInteger(R.integer.app_cms_mobile_show_details_discription);
                            ((TextView) view).setLineSpacing(0, 0.9f);
                            if (appCMSPresenter.getShowDetailsGist() != null &&
                                    appCMSPresenter.getShowDetailsGist().getDescription() != null) {
                                int descLength = appCMSPresenter.getShowDetailsGist().getDescription().length();
                                if (descLength > dis_max_length) {
                                    CommonUtils.setMoreLinkInDescription((TextView) view, appCMSPresenter.getShowDetailsGist().getDescription(), dis_max_length);
                                } else
                                    ((TextView) view).setText(appCMSPresenter.getShowDetailsGist().getDescription());

                            } else if (moduleAPIPub != null && moduleAPIPub.getContentData() != null && !moduleAPIPub.getContentData().isEmpty()
                                    && moduleAPIPub.getContentData().get(0).getGist() != null && moduleAPIPub.getContentData().get(0).getGist().getDescription() != null) {
                                ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getGist().getDescription());
                                int descLength = moduleAPIPub.getContentData().get(0).getGist().getDescription().length();
                                if (descLength > dis_max_length) {
                                    CommonUtils.setMoreLinkInDescription((TextView) view, moduleAPIPub.getContentData().get(0).getGist().getDescription(), dis_max_length);
                                } else
                                    ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getGist().getDescription());
                            }
                            view.setOnClickListener(null);
                            break;
                        case PAGE_EDIT_PROFILE_GENDER_LABEL_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getGender() != null)
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            else
                                set.setVisibility(view.getId(), INVISIBLE);
                            break;
                        case PAGE_EDIT_PROFILE_BIRTHDAY_LABEL_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getDOB() != null)
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            else
                                set.setVisibility(view.getId(), INVISIBLE);
                            break;
                        case PAGE_EDIT_PROFILE_BIRTHDAY_VALUE_KEY:
                            if (data.getUserMeResponse() != null &&
                                    data.getUserMeResponse().getProfile().getDOB() != null) {
                                ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                                ((TextView) view).setText(data.getUserMeResponse().getProfile().getDOB());
                            }
                            view.setOnClickListener(v -> {
                                DialogFragment datePickerFragment = new AppCMSPresenter.DatePickerFragment();
                                datePickerFragment.show(appCMSPresenter.getCurrentActivity().getSupportFragmentManager(), "datePicker");
                                ((AppCMSPresenter.DatePickerFragment) datePickerFragment).setSelectedDate(s -> {
                                    ((TextView) v).setText(s);
                                    ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.black));
                                    appCMSPresenter.getCurrentActivity().findViewById(R.id.birthdayLabel).setVisibility(VISIBLE);
                                });
                            });
                            break;
                        case PAGE_MORE_ACCOUNT_TITLE_KEY:
                            if (appCMSPresenter.getAppPreference().getLoggedInUserName() != null)
                                ((TextView) view).setText(appCMSPresenter.getAppPreference().getLoggedInUserName());
                            else
                                ((TextView) view).setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail().split("@")[0]);
                            break;
                        case PAGE_PROFILE_NAME_VALUE_KEY:
                            if (data.getUserMeResponse() != null && data.getUserMeResponse().getProfile() != null && data.getUserMeResponse().getProfile().getUsername() != null)
                                ((TextView) view).setText(data.getUserMeResponse().getProfile().getUsername());
                            else if (data.getUserMeResponse() != null && data.getUserMeResponse().getProfile() != null && data.getUserMeResponse().getProfile().getName() != null)
                                ((TextView) view).setText(data.getUserMeResponse().getProfile().getName());
                            else if (appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null)
                                ((TextView) view).setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail().split("@")[0]);
                            break;
                        case PAGE_PROFILE_LOCATION_VALUE_KEY:
                            if (data.getUserMeResponse() != null && data.getUserMeResponse().getProfile() != null && data.getUserMeResponse().getProfile().getLocation() != null)
                                ((TextView) view).setText(data.getUserMeResponse().getProfile().getLocation());
                            break;
                        case PAGE_PROFILE_TITLE_KEY:
                            ((TextView) view).setText("PROFILE");
                            break;
                        case PAGE_EDIT_PROFILE_SAVE_BUTTON_KEY:
                            view.setOnClickListener(v -> {
                                TextInputLayout etUsername = appCMSPresenter.getCurrentActivity().findViewById(R.id.usernameValue);
                                TextInputLayout location = appCMSPresenter.getCurrentActivity().findViewById(R.id.locationValue);
                                TextInputLayout firstName = appCMSPresenter.getCurrentActivity().findViewById(R.id.firstNameValue);
                                TextInputLayout lastName = appCMSPresenter.getCurrentActivity().findViewById(R.id.lastNameValue);
                                TextInputLayout zipcode = appCMSPresenter.getCurrentActivity().findViewById(R.id.zipCodeValue);

                                TextView gender = appCMSPresenter.getCurrentActivity().findViewById(R.id.genderValue);
                                TextView birthday = appCMSPresenter.getCurrentActivity().findViewById(R.id.birthdayValue);
                                TextView fitnessGoal = appCMSPresenter.getCurrentActivity().findViewById(R.id.fitnessGoalLabel);
                                TextView fitnessGoalValue = appCMSPresenter.getCurrentActivity().findViewById(R.id.fitnessGoalValue);
                                TextView bodyType = appCMSPresenter.getCurrentActivity().findViewById(R.id.bodyTypeLabel);
                                TextView bodyTypeValue = appCMSPresenter.getCurrentActivity().findViewById(R.id.bodyTypeValue);
                                UserDescriptionResponse userData = new UserDescriptionResponse();
                                if (etUsername.isEnabled() && !TextUtils.isEmpty(etUsername.getEditText().getText().toString()))
                                    userData.setUsername(etUsername.getEditText().getText().toString());
                                if (!TextUtils.isEmpty(location.getEditText().getText().toString()))
                                    userData.setLocation(location.getEditText().getText().toString());
                                if (!TextUtils.isEmpty(firstName.getEditText().getText().toString()))
                                    userData.setName(firstName.getEditText().getText().toString());
                                if (!TextUtils.isEmpty(lastName.getEditText().getText().toString()))
                                    userData.setLastName(lastName.getEditText().getText().toString());
                                if (!gender.getText().toString().equalsIgnoreCase(context.getString(R.string.gender)))
                                    userData.setGender(gender.getText().toString().toUpperCase());
                                if (!birthday.getText().toString().equalsIgnoreCase(v.getContext().getString(R.string.birthday)))
                                    userData.setDOB(birthday.getText().toString());
                                if (!TextUtils.isEmpty(zipcode.getEditText().getText().toString()))
                                    userData.setZipCode(zipcode.getEditText().getText().toString());
                                ArrayList<Question> questions = new ArrayList<>();
                                questions.add(new Question(fitnessGoal.getText().toString(), fitnessGoalValue.getText().toString()));
                                questions.add(new Question(bodyType.getText().toString(), bodyTypeValue.getText().toString()));
                                userData.setQuestions(questions);
                                if (data != null && data.getUserMeResponse() != null && data.getUserMeResponse().getProfile() != null && data.getUserMeResponse().getProfile().getAvatarUrl() != null)
                                    userData.setAvatarUrl(data.getUserMeResponse().getProfile().getAvatarUrl());
                                appCMSPresenter.closeSoftKeyboard();
                                appCMSPresenter.submitProfile(userData);
                            });
                            break;

                        case PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY: {
                            if (BaseView.isTablet(context)) {
                                if (BaseView.isLandscape(context)) {
                                    if (childComponent.getLayout().getTabletLandscape().getLeftDrawable() != null) {
                                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_time, 0, 0, 0);
                                        ((TextView) view).setCompoundDrawablePadding(10);
                                    }
                                } else {
                                    if (childComponent.getLayout().getTabletPortrait().getLeftDrawable() != null) {
                                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_time, 0, 0, 0);
                                        ((TextView) view).setCompoundDrawablePadding(10);
                                    }
                                }
                            } else {
                                if (childComponent.getLayout().getMobile().getLeftDrawable() != null) {
                                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_filter_time, 0, 0, 0);
                                    ((TextView) view).setCompoundDrawablePadding(10);
                                }
                            }
                            if (data != null && data.getTags() != null) {
                                String classDuration = "0 min";
                                List<Tag> tags = data.getTags();
                                if (tags != null) {
                                    for (int i = 0; i < tags.size(); i++) {
                                        if (tags.get(i).getTagType() != null) {
                                            if (tags.get(i).getTagType().equals(context.getString(R.string.app_cms_page_video_detail_class_duration_key))) {
                                                classDuration = tags.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                ((TextView) view).setText(classDuration);

                            } else {
//                                ((TextView) view).setText("Sample Text");
                            }

                            //
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        } // end of case PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY:
                        break;
                        case PAGE_VIDEO_DETAIL_AIR_DATE_TIME_LABEL_KEY: {
                            ((TextView) view).setGravity(Gravity.START);
                            if (data != null && data.getAirDateTime() > 0) {
                                String remainingTime = CommonUtils.getDateFormatByTimeZone(data.getAirDateTime(), "MMM dd, yyyy hh:mm a");
                                ((TextView) view).setText(remainingTime);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }

                        } // end of   case PAGE_VIDEO_DETAIL_AIR_DATE_TIME_LABEL_KEY:
                        break;
                        case PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_KEY: {
                            ((TextView) view).setText("DIFFICULTY");
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        } // end of case PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_KEY:
                        break;
                        case PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_KEY: {
                            ((TextView) view).setText("MUSIC");
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        } // end of  case PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_KEY:
                        break;
                        case PAGE_VIDEO_DETAIL_INTENSITY_LABEL_KEY: {
                            ((TextView) view).setText("INTENSITY".toUpperCase());
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        } // end of  case PAGE_VIDEO_DETAIL_INTENSITY_LABEL_KEY:
                        break;
                        case PAGE_BODY_FOCUS_LABLE: {
                            ((TextView) view).setText("Body Focus  ".toUpperCase());
                        }
                        break;

                        case PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_VALUE_KEY: {

                            if (data != null && data.getTags() != null) {
                                String difficulty = "-";
                                List<Tag> tagsD = data.getTags();
                                /*if (tagsD != null) {
                                    for (int i = 0; i < tagsD.size(); i++) {
                                        if (tagsD.get(i).getTagType() != null) {
                                            if (tagsD.get(i).getTagType().equals("difficulty")) {
                                                difficulty = tagsD.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
*/
                                if (TextUtils.isEmpty(difficultyVals)) {
                                    getTagInfo(data);
                                }
                                ((TextView) view).setText(difficultyVals);

                                ((TextView) view).setSingleLine();
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                ((TextView) view).setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                            }
                        } // end of  case PAGE_VIDEO_DETAIL_DIFFICULTY_LABEL_VALUE_KEY:
                        break;
                        case PAGE_VIDEO_DETAIL_INTENSITY_VALUE_KEY: {
                            //
                            if (TextUtils.isEmpty(intensityVals)) {
                                getTagInfo(data);
                            }
                            //String intensityText = "@####@/10";
                            // intensityText = TextUtils.isEmpty(intensityVals) ? intensityText.replace("@####@", "-") : intensityText.replace("@####@", intensityVals);
                            ((TextView) view).setText(intensityVals);
                            ((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        } // end of  case PAGE_VIDEO_DETAIL_INTENSITY_VALUE_KEY:
                        break;
                        case PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_VALUE_KEY: {

                            if (TextUtils.isEmpty(musicTypeVal)) {
                                getTagInfo(data);
                            }
                            ((TextView) view).setText(musicTypeVal);
                            ((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                        } // end of  case PAGE_VIDEO_DETAIL_PLAYLIST_LABEL_VALUE_KEY:
                        break;
                        case PAGE_BODY_FOCUS_LABLE_VALUE:

                            if (TextUtils.isEmpty(bodyFocusVals)) {
                                getTagInfo(data);
                            }
                            ((TextView) view).setText(bodyFocusVals);
                            //((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

                            break;
                        case PAGE_VIDEO_DETAIL_RATING_LABEL_VALUE_KEY: {
                            if (BaseView.isTablet(context)) {
                                if (BaseView.isLandscape(context)) {
                                    if (childComponent.getLayout().getTabletLandscape().getLeftDrawable() != null) {
                                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rating_shape, 0, 0, 0);
                                        ((TextView) view).setCompoundDrawablePadding(10);
                                    }
                                } else {
                                    if (childComponent.getLayout().getTabletPortrait().getLeftDrawable() != null) {
                                        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rating_shape, 0, 0, 0);
                                        ((TextView) view).setCompoundDrawablePadding(10);
                                    }
                                }
                            } else {
                                if (childComponent.getLayout().getMobile().getLeftDrawable() != null) {
                                    ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_rating_shape, 0, 0, 0);
                                    ((TextView) view).setCompoundDrawablePadding(10);
                                }
                            }
                            if (data != null && data.getTags() != null) {
                                String rating = "-";
                                List<Tag> tagsR = data.getTags();
                                if (tagsR != null) {
                                    for (int i = 0; i < tagsR.size(); i++) {
                                        if (tagsR.get(i).getTagType() != null) {
                                            if (tagsR.get(i).getTagType().equals("rating")) {
                                                rating = tagsR.get(i).getTitle();
                                            }
                                        }
                                    }
                                }
                                ((TextView) view).setText(rating);
                                if (rating.contains("-")) {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                }
                            }
                            //
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                        } //end of case PAGE_VIDEO_DETAIL_RATING_LABEL_VALUE_KEY:
                        break;
                        case PAGE_HOME_TITLE_DESCRIPTION: {
                            //
                            ((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        } //end of PAGE_HOME_TITLE_DESCRIPTION:
                        break;
                        case PAGE_DOWNLOAD_SETTING_TITLE_KEY:
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            view.setBackgroundColor(Color.TRANSPARENT);
                            if (data != null &&
                                    data.getLanguages() != null)
                                ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getSelectLanguageText());
                            else
                                ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadSettingsText());
                            break;
                        case PAGE_HOME_TITLE_THUMBNAIL:
                        case PAGE_HOME_DURATION_CAROUSEL: {

                            if (TextUtils.isEmpty(brandTitle)) {
                                getTagInfo(data);
                                ((TextView) view).setText(getBrand(data));
                            } else {
                                ((TextView) view).setText(brandTitle);
                            }
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                            if (componentKey == PAGE_HOME_DURATION_CAROUSEL) {
                                ((TextView) view).setMaxWidth(getDeviceWidth() / 2);
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                ((TextView) view).setSingleLine();
                                if (moduleType != PAGE_WEEK_SCHEDULE_GRID_KEY) {
                                    PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.backgroundTextViewColor));
                                    gdDefault.setCornerRadius(5f);
                                    view.setBackground(gdDefault);
                                }
                            }

                        } // end of case PAGE_HOME_DURATION_CAROUSEL:
                        break;


                        case PAGE_GRID_THUMBNAIL_INFO:
                        case PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO:
                            ((TextView) view).setGravity(Gravity.START);
                            view.setPadding(10, 5, 10, 5);
                            ((TextView) view).setMaxLines(1);
                            break;
                        case TEXT_VIEW_VIDEO_DURATION:
                            if (uiBlockName == UI_BLOCK_TRAY_11) {
                                ((TextView) view).setGravity(Gravity.TOP);
                                view.setPadding(0, -4, 8, 0);
                                ((TextView) view).setMaxLines(1);
                            }
                            break;
                        case INVALID_DETAILS_KEY:
                            ((TextView) view).setText(component.getText());
                            break;
                        case PAGE_THUMBNAIL_TITLE_KEY: {
                            if (data != null && data.getGist() != null &&
                                    data.getGist().getPersons() != null &&
                                    !data.getGist().getPersons().isEmpty() &&
                                    data.getGist().getPersons().get(0) != null &&
                                    data.getGist().getPersons().get(0).getTitle() != null) {
                                ((TextView) view).setText(data.getGist().getTitle());
                            }
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        } // end of case PAGE_THUMBNAIL_TITLE_KEY:
                        break;
                        case PAGE_INSTRUCTOR_VIDEO_DESCRIPTION_KEY:
                            if (data.getGist() != null && data.getGist().getDescription() != null)
                                ((TextView) view).setText(data.getGist().getDescription());
                            else
                                ((TextView) view).setText(component.getText());
                            break;
                        case PAGE_REST_TIME_KEY:
                            ((TextView) view).setText(component.getText());
                            break;
                        case PAGE_VIDEO_DETAIL_LANGUAGE_LABEL_KEY:
                            ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.ITALIC);
                            if (isExplicitLanguage)
                                ((TextView) view).setText("Contains explicit language  ");
                            else
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);

                            break;


                        case PAGE_SET_UP_LABLE:
                            ((TextView) view).setText("Set - Up");
                            break;
                        case PAGE_SET_UP_REQUIRED_LABLE:
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_required, 0, 0, 0);
                            ((TextView) view).setCompoundDrawablePadding(10);
                            ((TextView) view).setText("required");
                            break;
                        case PAGE_SET_UP_OPTIONAL_LABLE:
                            ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_optional, 0, 0, 0);
                            ((TextView) view).setCompoundDrawablePadding(10);
                            ((TextView) view).setText("optional");
                            break;
                        case PAGE_CLASS_FORMAT_LABLE:
                            if (data != null
                                    && data.getGist() != null
                                    && data.getGist().getLongDescription() != null) {

                                ((TextView) view).setGravity(Gravity.START);
                                ((TextView) view).setText("Class Format");
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }
                            break;
                        case PAGE_CLASS_FORMAT_VALUE:
                            ((TextView) view).setGravity(Gravity.START);
                            // TODO: 01/03/19  Added static text until it come from API

                            String str = context.getString(R.string.view_all_detail);
                            if (data.getGist() != null
                                    && data.getGist().getLongDescription() != null) {
                                str = data.getGist().getLongDescription();
                                if (isTablet(context)) {
                                    ((TextView) view).setMaxLines(2);
                                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                    ((TextView) view).setText(Html.fromHtml(str));

                                } else {
                                    //
                                    ((TextView) view).setText(str);
                                }
                            } else {
                                view.setVisibility(View.INVISIBLE);
                                //appCMSPresenter.getCurrentActivity().findViewById(R.id.classFormatLabel).setVisibility(View.INVISIBLE);
                            }


                            break;
                        case PAGE_VIDEO_DETAIL_TIMER_FITNESS_KEY: {
//                            0x9f000000 + appCMSPresenter.getGeneralBackgroundColor()
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            ((TextView) view).setGravity(Gravity.CENTER_HORIZONTAL);
                            final int countDown24HrInMillis = (1000 * 60 * 60 * 24);
                            //calculate remaining time from event date and current date
                            long remainingTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleStartDate(), "yyyy MMM dd HH:mm:ss");
                            long remainingEndTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleEndDate(), "yyyy MMM dd HH:mm:ss");
                            ImageButton imagePlay = constraintLayout.findViewById(R.id.vodThumbnailPlayImage);

                            if (remainingTime > 0 && data.getGist().getScheduleStartDate() > 0) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                imagePlay.setVisibility(View.INVISIBLE);
                                if (remainingTime > countDown24HrInMillis) {
                                    ((TextView) view).setText(String.format("LIVE this %s ", appCMSPresenter.getDateFormat(data.getGist().getScheduleStartDate(), "E hh:mm a")));
                                    view.setBackgroundColor(0x9f000000 + appCMSPresenter.getGeneralBackgroundColor());
                                } else {
                                    startTimer(remainingTime, ((TextView) view), imagePlay, data);
                                }
                            } else if (remainingEndTime < 0 && data.getGist().getScheduleEndDate() > 0) {
//                                imageAddToCalendar.setVisibility(View.INVISIBLE);
                                imagePlay.setVisibility(View.INVISIBLE);
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                ((TextView) view).setText("This event is over. ");
                                view.setBackgroundColor(0x9f000000 + appCMSPresenter.getGeneralBackgroundColor());

                            } else {
                                if (data != null
                                        && data.getGist() != null
                                        && data.getGist().getId() != null
                                        && !TextUtils.isEmpty(data.getGist().getId())
                                        && data.getGist().isLiveStream()
                                        && data.getStreamingInfo() == null) {
                                    updateVideoStreamingInfo(data.getGist().getId(), data, imagePlay, ((TextView) view));
                                }
                                if (imagePlay != null)
                                    imagePlay.setVisibility(View.VISIBLE);

//                                imageAddToCalendar.setVisibility(View.INVISIBLE);
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            }
                        } // end of case PAGE_VIDEO_DETAIL_TIMER_FITNESS_KEY:
                        break;
                        case PAGE_VIEW_ALL_DETAIL:
                            if (isTablet(context) && data != null
                                    && data.getGist() != null
                                    && data.getGist().getLongDescription() != null) {
                                view.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                                ((TextView) view).setTextColor(Color.BLACK);
                                ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                                view.setPadding(10, 10, 10, 10);
                                ((TextView) view).setText("VIEW ALL DETAILS");
                                PopupWindow popUp = createPopupWindowForClassFormat(context, constraintLayout, view, data);


                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }

                            break;
                        case PAGE_THUMBNAIL_CATEGORY_TITLE:
                            ((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setGravity(Gravity.START);
                            set.setHorizontalChainStyle(view.getId(), ConstraintSet.CHAIN_SPREAD);

                            if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getPrimaryCategory() != null &&
                                    data.getGist().getPrimaryCategory().getTitle() != null) {
                                ((TextView) view).setText(data.getGist().getPrimaryCategory().getTitle());
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }


                            break;
                        case PAGE_VOD_MORE_TRAY_TITLE_KEY: {
                            if (component != null && component.getText() != null) {
                                ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()).toUpperCase() : component.getText().toUpperCase());
                            }
                            String classTitle = "More @####@ Classes";
                            if (data != null && data.getTags() != null) {
                                getTagInfo(data);
                                classTitle = classTitle.replace("@####@", brandTitle);
                                ((TextView) view).setText(classTitle.toUpperCase());
                            }

                            ((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }
                        break;

                        case PAGE_BRANDS_TRAY_TITLE_BRAND_KEY:
                            if (moduleAPIPub != null && moduleAPIPub.getSettings() != null && !moduleAPIPub.getSettings().getHideTitle() &&
                                    !TextUtils.isEmpty(moduleAPIPub.getTitle())) {
                                ((TextView) view).setText(moduleAPIPub.getTitle().toUpperCase());
                            }
                            ((TextView) view).setTextColor(Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppTextColor())));

                            break;
                        case PAGE_TRAY_TITLE_KEY:
                            if (blockName.equals("contentBlock01")) {
                                if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && !CommonUtils.isEmpty(moduleAPIPub.getTitle())) {
                                    ((TextView) view).setText(moduleAPIPub.getTitle());
                                    ((TextView) view).setWidth(Utils.getDeviceWidth(appCMSPresenter.getCurrentContext()) / 2);
                                    ((TextView) view).setMaxLines(1);
                                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                }
                            } else {
                                if (data != null &&
                                        data.getGist() != null &&
                                        data.getGist().getRelatedVideos() != null &&
                                        data.getGist().getRelatedVideos().size() > 0) {
                                    boolean vodsInRelatedVideos = true;
                                    for (int j = 0; j < data.getGist().getRelatedVideos().size(); j++) {
                                        if (!data.getGist().getRelatedVideos().get(j).getGist().isLiveStream()) {
                                            vodsInRelatedVideos = true;
                                            break;
                                        } else {
                                            vodsInRelatedVideos = false;
                                        }
                                    }
                                    if (vodsInRelatedVideos) {
                                        ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                                appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()).toUpperCase() : component.getText().toUpperCase());
                                    }
                                } else {
                                    if (moduleAPIPub != null && moduleAPIPub.getTitle() != null)
                                        ((TextView) view).setText(moduleAPIPub.getTitle());
                                }

                                ((TextView) view).setTextColor(appCMSPresenter.getBlockTitleTextColor());
                                ((TextView) view).setGravity(Gravity.START);
                            }
                            break;
                        case PAGE_NO_CLASSES_SCHEDULED:
                            ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_grey));
                            ((TextView) view).setGravity(Gravity.CENTER);
                            view.setPadding(10, 50, 10, 50);
                            RecyclerView v = constraintLayout.findViewById(R.id.pageLinkScheduleGrid);
                            if (v != null) {
                                if (v.getAdapter() == null || v.getAdapter().getItemCount() <= 0) {
                                    ((TextView) view).setText(R.string.text_no_page_linked);
                                }
                            }
                            break;

                        case PAGE_AUTOPLAY_MOVIE_TITLE_KEY:
                            if (data != null &&
                                    data.getGist() != null &&
                                    !TextUtils.isEmpty(data.getGist().getTitle())) {
                                ((TextView) view).setText(data.getGist().getTitle());
                            }
                            ViewTreeObserver titleTextVto = view.getViewTreeObserver();
                            ViewCreatorTitleLayoutListener viewCreatorTitleLayoutListener =
                                    new ViewCreatorTitleLayoutListener((TextView) view);
                            titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);
                            ((TextView) view).setSingleLine(true);
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.MARQUEE);
                            view.setSelected(true);
                            break;
                        case PAGE_AUTOPLAY_MOVIE_SUBHEADING_KEY:
                            if (data != null) {
                                ViewCreator.setViewWithSubtitle(context,
                                        data,
                                        view, appCMSPresenter);

                                ((TextView) view).setTextColor(0x9f000000 + appCMSPresenter.getGeneralTextColor());
                            }
                            break;
                        case PAGE_AUTOPLAY_MOVIE_DESCRIPTION_KEY:
                            if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getDescription() != null) {
                                ((TextView) view).setText(data.getGist().getDescription());
                            }
                            break;
                        case PAGE_AUTOPLAY_FINISHED_UP_TITLE_KEY:
                            view.setBackgroundColor(Color.TRANSPARENT);
                            ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getFinishedText(component.getText()));

                            break;
                        case PAGE_AUTOPLAY_MOVIE_PLAYING_IN_LABEL_KEY:
                            ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getPlayInText(component.getText()));
                            break;
                        case PAGE_VIDEO_AGE_LABEL_KEY:
                            if (data != null &&
                                    data.getGist() != null &&
                                    !TextUtils.isEmpty(data.getParentalRating())) {
                                String parentalRating = data.getParentalRating();
                                ((TextView) view).setText(parentalRating);
                                ((TextView) view).setSingleLine(true);
                                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                //view.setPadding(8, 8, 8, 8);
                                ((TextView) view).setGravity(Gravity.CENTER);

                                applyBorderToComponent(context,
                                        view,
                                        component,
                                        appCMSPresenter.getGeneralTextColor());
                            }
                            break;
                        case PAGE_AUTOPLAY_MOVIE_TIMER_LABEL_KEY:
                            //view.setId(R.id.countdown_id);
                            ((TextView) view)
                                    .setShadowLayer(
                                            20,
                                            0,
                                            0,
                                            Color.parseColor(getColor(
                                                    context,
                                                    component.getTextColor())));
                            break;
                        case PAGE_VIDEO_DESCRIPTION_KEY: {
                            String videoDescription = null;
                            if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getDescription() != null) {
                                videoDescription = data.getGist().getDescription();
                                if (videoDescription != null) {
                                    videoDescription = videoDescription.trim();
                                    ((TextView) view).setText(videoDescription);
                                    if (jsonValueKeyMap.get(moduleInfo.getBlockName()) == UI_BLOCK_SHOW_DETAIL_07 && moduleAPIPub.getContentData() != null && moduleAPIPub.getContentData().size() > 0 &&
                                            moduleAPIPub.getContentData().get(0).getSeason() != null && moduleAPIPub.getContentData().get(0).getSeason().size() > 0 &&
                                            moduleAPIPub.getContentData().get(0).getSeason().get(0).getTitle() != null && moduleAPIPub.getContentData().get(0).getSeason().get(0).getDescription() != null) {
                                        if (moduleInfo.getSettings() != null && !moduleInfo.getSettings().isDisplaySeasonsInAscendingOrder() &&
                                                moduleAPIPub.getContentData().get(0).getSeason().size() > 1) {
                                            List<Season_> seasonArrayList = new ArrayList<>();
                                            seasonArrayList = moduleAPIPub.getContentData().get(0).getSeason();
                                            ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getSeason().get(seasonArrayList.size() - 1).getDescription().toString());
                                            ViewCreator.setMoreLinkInDescription(appCMSPresenter, (TextView) view, moduleAPIPub.getContentData().get(0).getSeason().get(0).getTitle(), moduleAPIPub.getContentData().get(0).getSeason().get(seasonArrayList.size() - 1).getDescription().toString(), 200, appCMSPresenter.getGeneralTextColor());

                                        } else {
                                            ((TextView) view).setText(moduleAPIPub.getContentData().get(0).getSeason().get(0).getDescription().toString());
                                            ViewCreator.setMoreLinkInDescription(appCMSPresenter, (TextView) view, moduleAPIPub.getContentData().get(0).getSeason().get(0).getTitle(), moduleAPIPub.getContentData().get(0).getSeason().get(0).getDescription().toString(), 200, appCMSPresenter.getGeneralTextColor());
                                        }
                                    } else {
                                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                                        ViewCreator.setMoreLinkInDescription(appCMSPresenter, (TextView) view, data.getGist().getTitle(), videoDescription, 200, appCMSPresenter.getGeneralTextColor());
                                    }
                                }
                            } else if (moduleAPIPub != null && !TextUtils.isEmpty(moduleAPIPub.getDescription())) {
                                ((TextView) view).setText(moduleAPIPub.getDescription());
                                // ViewCreator.setMoreLinkInDescription(appCMSPresenter, (TextView) view, moduleAPIPub.getTitle(), moduleAPIPub.getDescription(), 200, appCMSPresenter.getGeneralTextColor());
                            }
                             /*if (!TextUtils.isEmpty(videoDescription)) {
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                    ((TextView) view).setText(Html.fromHtml(videoDescription));
                                } else {
                                    ((TextView) view).setText(Html.fromHtml(videoDescription, Html.FROM_HTML_MODE_COMPACT));
                                }
                            } else if (!BaseView.isLandscape(context)) {
                                componentViewResult.shouldHideComponent = true;
                            }
                           if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getTitle() != null) {
                                ViewTreeObserver textVto = view.getViewTreeObserver();
                                ViewCreatorMultiLineLayoutListener viewCreatorLayoutListener =
                                        new ViewCreatorMultiLineLayoutListener(((TextView) view),
                                                data.getGist().getTitle(),
                                                videoDescription,
                                                appCMSPresenter,
                                                false,
                                                appCMSPresenter.getBrandPrimaryCtaColor(),
                                                appCMSPresenter.getGeneralTextColor(),
                                                false);
                                textVto.addOnGlobalLayoutListener(viewCreatorLayoutListener);
                            }*/
                        }
                        break;
                        case PAGE_VIDEO_TITLE_KEY:
                        case PAGE_SHOW_TITLE_KEY: {
                            if (data != null &&
                                    data.getGist() != null &&
                                    !TextUtils.isEmpty(data.getGist().getTitle())) {
                                ((TextView) view).setText(data.getGist().getTitle());
                            } else if (data != null && data.getPages() != null && data.getPages().size() > 0) {
                                for (int i = 0; i < data.getPages().size(); i++) {
                                    if (data.getPages().get(i) != null && data.getPages().get(i).getOgDetails() != null && data.getPages().get(i).getOgDetails().getTitle() != null) {
                                        ((TextView) view).setText(data.getPages().get(i).getOgDetails().getTitle());
                                    }

                                }
                            } else {
                                ((TextView) view).setText(component.getText());
                            }
                           /*  titleTextVto = view.getViewTreeObserver();
                           viewCreatorTitleLayoutListener =
                                    new ViewCreatorTitleLayoutListener((TextView) view);

                            if (component.getKey() != null &&
                                    !component.getKey().equals(context.getString(R.string.app_cms_page_show_image_video_key)) &&
                                    !BaseView.isTablet(context)) {
                                viewCreatorTitleLayoutListener.setSpecifiedMaxWidthRatio(0.7f);
                            }
                            titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);*/
                            ((TextView) view).setSingleLine(true);
                            if (uiBlockName == UI_BLOCK_SHOW_DETAIL_07)
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                            else
                                ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }
                        break;
                        case PAGE_VIDEO_SUBTITLE_KEY: {
                            if (data != null) {
                                if (data.getGist() != null &&
                                        !TextUtils.isEmpty(data.getGist().getContentType()) &&
                                        data.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.app_cms_video_content_type)) &&
                                        appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT) {
                                    ViewCreator.setViewWithSubtitle(context, data,
                                            view, appCMSPresenter);
                                } else if (data.getSeason() != null) {
                                    ViewCreator.setViewWithShowSubtitle(appCMSPresenter, context, data,
                                            view, false);
                                } else if (data != null && data.getGist() != null &&
                                        data.getGist().getBundleList() != null) {
                                    ViewCreator.setViewWithBundleSubtitle(appCMSPresenter, context, data,
                                            view, false);
                                }
                                ((TextView) view).setTextColor(0x92000000 + appCMSPresenter.getGeneralTextColor());
                            }
                        }
                        break;
                        case PAGE_VIDEO_PUBLISHDATE_KEY: {
                            if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getPublishDate() != null &&
                                    !data.getGist().isLiveStream()) {
                                if (constraintLayout != null && constraintLayout.findViewById(R.id.progressView) != null &&
                                        constraintLayout.findViewById(R.id.progressView).getVisibility() == VISIBLE) {
                                    childComponent.getLayout().getMobile().setBottomTo_bottomOf("");
                                    childComponent.getLayout().getTabletLandscape().setBottomTo_bottomOf("");
                                    childComponent.getLayout().getTabletPortrait().setBottomTo_bottomOf("");

                                    childComponent.getLayout().getMobile().setBottomTo_topOf("progressView");
                                    childComponent.getLayout().getTabletLandscape().setBottomTo_topOf("progressView");
                                    childComponent.getLayout().getTabletPortrait().setBottomTo_topOf("progressView");
                                }
                                if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT &&
                                        moduleInfo != null &&
                                        moduleInfo.getSettings() != null &&
                                        moduleInfo.getSettings().isPublishLabel()) {
                                    long publishDateMillseconds = Long.parseLong(data.getGist().getPublishDate());
                                    String publishDate = appCMSPresenter.getLocalisedStrings().getPublishedText() + " " + appCMSPresenter.getDateFormat(publishDateMillseconds, "MMMM dd, yyyy");

                                    ((TextView) view).setText(publishDate);

                                    ((TextView) view).setTextColor(Color.WHITE);
                                    ((TextView) view).setBackgroundColor(0x92000000 + Color.BLACK);
                                    ((TextView) view).setGravity(Gravity.START);
                                    ((TextView) view).setPadding(20, 5, 20, 5);
                                    ((TextView) view).setMaxLines(1);

                                } else if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS) {
                                    long runtime = data.getGist().getRuntime();
                                    String secondsToTime = appCMSPresenter.convertSecondsToTime(runtime);
                                    StringBuilder builder = new StringBuilder(secondsToTime);
                                    long publishDateMillseconds = Long.parseLong(data.getGist().getPublishDate());
                                    String publishDate = appCMSPresenter.getLocalisedStrings().getPublishedText() + " " + appCMSPresenter.getDateFormat(publishDateMillseconds, "MMMM dd, yyyy");
                                    if (runtime == 0) {
                                        builder.replace(0, secondsToTime.length(), publishDate);
                                    } else {
                                        builder.append(" | ");
                                        builder.append(publishDate);
                                    }
                                    ((TextView) view).setText(builder);
                                } else {
                                    set.setVisibility(((TextView) view).getId(), ConstraintSet.GONE);
                                }
                                ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            } else {
                                set.setVisibility(((TextView) view).getId(), ConstraintSet.GONE);
                            }
                        }
                        break;
                        case PAGE_SHOW_SUBTITLE_KEY: {
                            StringBuilder stringSubTitle = new StringBuilder();
                            if (data != null &&
                                    data.getSeason() != null) {
                                if (data.getSeason().size() >= 1 && blockName.equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.ui_block_showDetail_07))) {
                                    stringSubTitle.append(data.getSeason().size() + " " + appCMSPresenter.getLocalisedStrings().getHoverSeasonsLabelText().toUpperCase());
//                                        ((TextView) view).setText();
                                } else if (data.getSeason().size() > 1) {
                                    stringSubTitle.append(data.getSeason().size() + " " + appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.seasons)));
//                                        ((TextView) view).setText();
                                } else {
                                    if (data.getSeason().size() > 0
                                            && data.getSeason().get(0) != null &&
                                            data.getSeason().get(0).getEpisodes() != null) {
                                        stringSubTitle.append(data.getSeason().get(0).getEpisodes().size() + " " + (appCMSPresenter.isMovieSpreeApp() ? "PROGRAMS" : appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.runtime_episodes_abbreviation))));

                                    }
                                }
                                if (data.getGist() != null && data.getGist().getPrimaryCategory() != null
                                        && data.getGist().getPrimaryCategory().getTitle() != null) {
                                    if (!TextUtils.isEmpty(stringSubTitle.toString())) {
                                        stringSubTitle.append(" | ");
                                    }
                                    stringSubTitle.append(data.getGist().getPrimaryCategory().getTitle().toUpperCase());
                                }
                                if (uiBlockName == UI_BLOCK_SHOW_DETAIL_07)
                                    ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                else
                                    ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                                ((TextView) view).setText(stringSubTitle.toString());

                            }
                        }
                        break;


                        case WALLET_TITLE_KEY: {
                            if (!TextUtils.isEmpty(component.getBackgroundColor()))
                                view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                            if (component.getPadding() > 0) {
                                int padding = (int) BaseView.convertDpToPixel(component.getPadding(), context);
                                view.setPadding(padding, padding, padding, padding);
                            }
                            break;
                        }
                        case UPI_TITLE_ERROR_KEY:
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor() != null) {
                                ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor()));
                            }
                            if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getUpiGenericMessage())) {
                                String value = metadataMap.getUpiGenericMessage();
                                value = value.replaceAll("\"", "");
                                ((TextView) view).setText(value);
                                set.setVisibility(view.getId(), View.VISIBLE);
                            } else
                                set.setVisibility(view.getId(), View.GONE);
                            break;
                        case CARD_TITLE_ERROR_KEY:
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor() != null) {
                                ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor()));
                            }
                            if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getCardGenericMessage())) {
                                String value = metadataMap.getCardGenericMessage();
                                value = value.replaceAll("\"", "");
                                ((TextView) view).setText(value);
                                set.setVisibility(view.getId(), View.VISIBLE);
                            } else
                                set.setVisibility(view.getId(), View.GONE);
                            break;
                        case NETBANKING_TITLE_ERROR_KEY:
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor() != null) {
                                ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor()));
                            }
                            if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getNetbankingGenericMessage())) {
                                String value = metadataMap.getNetbankingGenericMessage();
                                value = value.replaceAll("\"", "");
                                ((TextView) view).setText(value);
                                set.setVisibility(view.getId(), View.VISIBLE);
                            } else
                                set.setVisibility(view.getId(), View.GONE);
                            break;


                    }


                } // end of PAGE_LABEL_KEY
                break;
                case PAGE_BARRIER_KEY: {
                    set.createBarrier(view.getId(),
                            IdUtils.getBarrierDirection(component.getBarrierDirection()), 0,
                            IdUtils.getReferenceIds(component.getReferenceIds()));
                }
                break;
                case PAGE_GROUP_KEY: {
                    if (componentKey == PAGE_SETTING_SUBSCRIPTION_GROUP_KEY) {
                        if (appCMSPresenter.isAppAVOD() || appCMSPresenter.getAppCMSMain().getServiceType().equalsIgnoreCase("TVOD")) {
                            ((Group) view).setReferencedIds(IdUtils.getReferenceIds(component.getReferenceIds()));
                            set.setVisibility(view.getId(), GONE);
                        }
                        break;
                    }
                    ((Group) view).setReferencedIds(IdUtils.getReferenceIds(component.getReferenceIds()));
                    switch (componentKey) {
                        case PAGE_SETTING_MOBILE_INFO_GROUP_KEY: {
                            if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                                if (isSiteOTPEnabled(appCMSPresenter) && isValidPhoneNumber(appCMSPresenter.getLoggedInPhone())) {
                                    set.setVisibility(view.getId(), VISIBLE);
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                }
                            } else if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                                if (isSiteOTPEnabled(appCMSPresenter) && isValidPhoneNumber(appCMSPresenter.getLoggedInPhone())) {
                                    set.setVisibility(view.getId(), VISIBLE);
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                }
                            } else {
                                if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                                    set.setVisibility(view.getId(), VISIBLE);
                                } else {
                                    set.setVisibility(view.getId(), GONE);
                                }
                            }
                        }
                        break;
                        case PAGE_SETTING_AUTO_PLAY_GROUP_KEY: {
                            if (appCMSPresenter.isAutoPlayEnable()) {
                                set.setVisibility(view.getId(), VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), GONE);
                            }
                        }
                        break;
                        case PAGE_SETTING_DOWNLOAD_GROUP_KEY: {
                            if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
                                set.setVisibility(view.getId(), VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), GONE);
                            }
                        }
                        break;

                        case PAGE_SETTING_PERSONALIZATION_GROUP_KEY: {
                            if (appCMSPresenter.isPersonalizationEnabled()) {
                                if (appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                                    /**
                                     * If it is active for subscribed user only,
                                     * this option should be invisible for the other user
                                     *
                                     */
                                    if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                        set.setVisibility(view.getId(), VISIBLE);
                                    } else {
                                        set.setVisibility(view.getId(), GONE);
                                    }
                                } else {
                                    set.setVisibility(view.getId(), VISIBLE);
                                }
                            } else {
                                set.setVisibility(view.getId(), GONE);
                            }
                        }
                        break;

                        case PAGE_SETTING_PARENTAL_CONTROL_GROUP_KEY: {
                            if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().isParentalControlEnable()) {
                                set.setVisibility(view.getId(), VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), GONE);
                            }
                        }
                        break;

                        case PAGE_JUSPAY_WALLET_GROUP_KEY: {
                            boolean isWalletListEmpty = true;
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getWallets() != null) {
                                for (MetadataMap.Wallet wallet : moduleAPIPub.getMetadataMap().getWallets()) {
                                    if (!TextUtils.isEmpty(wallet.getKey())) {
                                        if (wallet.getKey().contains(context.getString(R.string.google_pay)) && !CommonUtils.isAppInstalled(context, GOOGLE_PAY_PACKAGE_NAME)) {
                                            continue;
                                        }
                                        isWalletListEmpty = false;
                                        break;
                                    }
                                }
                            }
                            if (isWalletListEmpty || !CommonUtils.isSuggestedPaymentType(appCMSPresenter.getAllowedPayMethods(), appCMSPresenter.getCurrentActivity().getString(R.string.wallet))) {
                                set.setVisibility(view.getId(), GONE);
                            } else if (!moduleInfo.getSettings().isWalletEnabled()) {
                                set.setVisibility(view.getId(), GONE);
                            } else {
                                set.setVisibility(view.getId(), VISIBLE);
                            }
                        }
                        break;

                        case PAGE_JUSPAY_NETBANKING_GROUP_KEY: {
                            boolean isBankListEmpty = true;
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getBanks() != null) {
                                for (MetadataMap.Bank bank : moduleAPIPub.getMetadataMap().getBanks()) {
                                    if (!TextUtils.isEmpty(bank.getTitle())) {
                                        isBankListEmpty = false;
                                        break;
                                    }
                                }
                            }

                            if (isBankListEmpty || !CommonUtils.isSuggestedPaymentType(appCMSPresenter.getAllowedPayMethods(), appCMSPresenter.getCurrentActivity().getString(R.string.net_banking))) {
                                set.setVisibility(view.getId(), GONE);
                            } else if (!moduleInfo.getSettings().isNetbankingEnabled()) {
                                set.setVisibility(view.getId(), GONE);
                            } else {
                                set.setVisibility(view.getId(), VISIBLE);
                            }
                        }
                        break;

                        case PAGE_JUSPAY_CARD_GROUP_KEY: {
                            boolean isCardListEmpty = true;
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getCards() != null) {
                                for (MetadataMap.Card card : moduleAPIPub.getMetadataMap().getCards()) {
                                    if (!TextUtils.isEmpty(card.getImageUrl())) {
                                        isCardListEmpty = false;
                                        break;
                                    }
                                }
                            }

                            if (isCardListEmpty || !CommonUtils.isSuggestedPaymentType(appCMSPresenter.getAllowedPayMethods(), appCMSPresenter.getCurrentActivity().getString(R.string.cards))) {
                                set.setVisibility(view.getId(), GONE);
                            } else if (!moduleInfo.getSettings().isCardEnabled()) {
                                set.setVisibility(view.getId(), GONE);
                            } else {
                                set.setVisibility(view.getId(), VISIBLE);
                            }
                        }
                        break;

                        case PAGE_JUSPAY_UPI_GROUP_KEY: {
                            boolean isUPIListEmpty = true;
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getUPI() != null) {
                                for (MetadataMap.UPI upi : moduleAPIPub.getMetadataMap().getUPI()) {
                                    if (!TextUtils.isEmpty(upi.getImageUrl())) {
                                        isUPIListEmpty = false;
                                        break;
                                    }
                                }
                            }

                            if (isUPIListEmpty || !CommonUtils.isSuggestedPaymentType(appCMSPresenter.getAllowedPayMethods(), appCMSPresenter.getCurrentActivity().getString(R.string.upi))) {
                                set.setVisibility(view.getId(), GONE);
                            } else if (!moduleInfo.getSettings().isUpiEnabled()) {
                                set.setVisibility(view.getId(), GONE);
                            } else {
                                set.setVisibility(view.getId(), VISIBLE);
                            }
                        }
                        break;
                    }

                }
                break;
                case BUTTON_FLOATING: {
                    if (childComponent.getText() != null)
                        ((ExtendedFloatingActionButton) view).setText(childComponent.getText());
                    if (childComponent.getTextAlignment() != null)
                        setTextGravity(((ExtendedFloatingActionButton) view), jsonValueKeyMap.get(childComponent.getTextAlignment()));
                    ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, childComponent, view);
                    ((ExtendedFloatingActionButton) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                    if (childComponent.getBackgroundColor() != null)
                        view.setBackgroundColor(Color.parseColor(childComponent.getBackgroundColor()));
                    else
                        view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                    if (childComponent.getTextColor() != null)
                        ((ExtendedFloatingActionButton) view).setTextColor(Color.parseColor(getColor(context, childComponent.getTextColor())));
                    else
                        ((ExtendedFloatingActionButton) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                    switch (componentKey) {
                        case BUTTON__FLOATING_SUBSCRIBE:
                            view.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
//                            ((ExtendedFloatingActionButton) view).setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                            break;
                    }
                }
                case PAGE_BUTTON_KEY: {
                    if (view instanceof Button) {
                        if (childComponent.getText() != null)
                            ((Button) view).setText(childComponent.getText());
                        if (childComponent.getTextAlignment() != null)
                            setTextGravity(((Button) view), jsonValueKeyMap.get(childComponent.getTextAlignment()));
                        ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, childComponent, view);
                        ((Button) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                        if (childComponent.getBackgroundColor() != null)
                            view.setBackgroundColor(Color.parseColor(childComponent.getBackgroundColor()));
                        else
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        if (childComponent.getTextColor() != null)
                            ((Button) view).setTextColor(Color.parseColor(getColor(context, childComponent.getTextColor())));
                        else
                            ((Button) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        if (childComponent.getBorderColor() != null) {
                            childComponent.setBorderColor(component.getBorderColor());
                            if ((childComponent.getBorderWidth() > 0))
                                childComponent.setBorderWidth(childComponent.getBorderWidth());
                            applyBorderToComponent(
                                    context,
                                    view,
                                    childComponent,
                                    Color.parseColor(CommonUtils.getColor(context, childComponent.getBorderColor())));
                        }
                    }
                    switch (componentKey) {
                        case PAGE_VIDEO_PLAY_BUTTON_KEY: {
                            if (data.getGist() != null && data.getGist().getContentType() != null) {
                                String contentType = data.getGist().getContentType();
                                if (contentType.equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.content_type_video)) && !appCMSPresenter.isUserSubscribed()
                                        && ((data.getGist().getObjTransactionDataValue() != null
                                        && data.getGist().getObjTransactionDataValue().size() > 0
                                        && data.getGist().getObjTransactionDataValue().get(0).size() == 0))) {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                }

                            }
                        }
                        break;
                        case BUTTON_SUBSCRIBE: {
                            if (metadataMap != null && metadataMap.getSubscribeNowCta() != null)
                                ((Button) view).setText(metadataMap.getSubscribeNowCta());
                            else
                                ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getSubscribeNowText());
                            ((Button) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                            view.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.SUBSCRIBE);
                                    appCMSPresenter.navigateToLoginPage(false);
                                }
                            });
                        }
                        break;
                        case BUTTON_BROWSE: {
                            if (metadataMap != null && !CommonUtils.isEmpty(metadataMap.getButtonText()))
                                ((Button) view).setText(metadataMap.getButtonText());
                            ((Button) view).setPadding(20, 5, 20, 5);
                            view.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (metadataMap != null && !CommonUtils.isEmpty(metadataMap.getButtonLink())) {
                                        appCMSPresenter.openChromeTab(metadataMap.getButtonLink());
                                    }
                                }
                            });
                        }
                        break;
                        case PAGE_SETTINGS_RE_SUBSCRIBE_PLAN_PROFILE_KEY: {

                            if (moduleInfo != null && moduleInfo.getSettings() != null
                                    && moduleInfo.getSettings().isShowResubscribeFlow()
                                    && appCMSPresenter.isReSubscriptionVisible()) {
                                ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getRe_subscribe());
                                ((Button) view).setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                                ((Button) view).setTextColor(appCMSPresenter.getGeneralBackgroundColor());
                                ((Button) view).setAllCaps(false);

                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                try {
                                    TextView textViewPlan = constraintLayout.findViewById(R.id.planValue);
                                    textViewPlan.setPaintFlags(textViewPlan.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    View paymentProcess = constraintLayout.findViewById(R.id.paymentProcess);
                                    View paymentProcessorValue = constraintLayout.findViewById(R.id.paymentProcessorValue);
                                    set.setVisibility(paymentProcess.getId(), ConstraintSet.GONE);
                                    set.setVisibility(paymentProcessorValue.getId(), ConstraintSet.GONE);
                                    if (appCMSPresenter.getActiveSubscriptionStatus().toLowerCase().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.subscription_status_cancelled))) {
                                        View planLabel = constraintLayout.findViewById(R.id.planLabel);
                                        View planValue = constraintLayout.findViewById(R.id.planValue);
                                        // set.setVisibility(planLabel.getId(),ConstraintSet.GONE);
                                        // set.setVisibility(planValue.getId(),ConstraintSet.GONE);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                view.setOnClickListener(v -> {
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
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                        }
                        break;
                        case PAGE_SETTINGS_CANCEL_PLAN_PROFILE_KEY: {
                            ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getCancelText());
                            if (appCMSPresenter.shouldDisplaySubscriptionCancelButton() &&
                                    appCMSPresenter.isUserSubscribed() &&
                                    !appCMSPresenter.isExistingGooglePlaySubscriptionSuspended() &&
                                    appCMSPresenter.isSubscriptionCompleted()) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }

                            view.setOnClickListener(v -> {
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
                        }
                        break;
                        case PAGE_SETTINGS_UPGRADE_PLAN_PROFILE_KEY: {
                            if (!appCMSPresenter.isUserSubscribed()) {
                                if (metadataMap != null && metadataMap.getSubscribeNowButtonText() != null) {
                                    ((TextView) view)
                                            .setText(metadataMap.getSubscribeNowButtonText());
                                } else {
                                    ((TextView) view)
                                            .setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_page_upgrade_subscribe_button_text)));
                                }
                            } else if (!appCMSPresenter.upgradesAvailableForUser()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else {
                                if (metadataMap != null && metadataMap.getUpgradePlanButtonText() != null)
                                    ((TextView) view).setText(metadataMap.getUpgradePlanButtonText().toUpperCase());
                                if (appCMSPresenter.getActiveSubscriptionProcessor() == null
                                        || appCMSPresenter.getActiveSubscriptionProcessor().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.subscription_juspay_payment_processor))) {
                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                }
                            }

                            String paymentProcessor = appCMSPresenter.getActiveSubscriptionProcessor();
                            if ((moduleInfo != null && moduleInfo.getSettings() != null
                                    && moduleInfo.getSettings().isShowResubscribeFlow()
                                    && appCMSPresenter.isReSubscriptionVisible())
                                    || (paymentProcessor != null
                                    && paymentProcessor.equalsIgnoreCase(context.getString(R.string.subscription_sslcommerz_payment_processor)))) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                            view.setOnClickListener(v -> {
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
                        }
                        break;
                        case BUTTON_EDIT_PROFILE:
                            if (metadataMap != null && metadataMap.getProfileText() != null)
                                ((Button) view).setText(metadataMap.getProfileText().toUpperCase());
                            if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            } else if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            } else if (isSiteOTPEnabled(appCMSPresenter)) {
                                if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isManageProfile()) {
                                    set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                }
                            }
                            view.setOnClickListener(v -> {
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
                        case EDIT_PROFLE_PAGE: {
                            if (metadataMap != null && metadataMap.getProfileText() != null)
                                ((Button) view).setText(metadataMap.getProfileText().toUpperCase());
                            if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }

                            if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }

                            if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }

                            if (appCMSPresenter.isHoichoiApp())
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);

                            view.setOnClickListener(v -> {
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

                        }
                        break;

                        case PAGE_SETTINGS_BILLING_HISTORY_KEY: {
                            if (appCMSPresenter.isUserSubscribed() &&
                                    !TextUtils.isEmpty(appCMSPresenter.getActiveSubscriptionProcessor())) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                if (moduleAPIPub.getMetadataMap() != null) {
                                    ((Button) view).setText(metadataMap.getBillingHistoryButtonText().toUpperCase());
                                }
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                            view.setOnClickListener(v -> {
                                FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.add(AppCMSBillingHistoryFragment.newInstance(moduleAPIPub.getMetadataMap()), "AppCMSBillingHistoryFragmentFragment");
                                fragmentTransaction.commitAllowingStateLoss();
                            });
                        }
                        break;

                        case PAGE_SETTINGS_EDIT_PHONE_VALUE_KEY: {
                            if (isValidPhoneNumber(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
                                if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null
                                        && moduleAPIPub.getMetadataMap().getProfilePopupUpdate() != null) {
                                    ((TextView) view).setText(moduleAPIPub.getMetadataMap().getProfilePopupUpdate());
                                } else {
                                    ((TextView) view).setText(component.getText());
                                }
                            } else {
                                if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null
                                        && moduleAPIPub.getMetadataMap().getAddPhoneNo() != null) {
                                    ((TextView) view).setText(moduleAPIPub.getMetadataMap().getAddPhoneNo());
                                } else {
                                    ((TextView) view).setText(appCMSPresenter.getCurrentContext().getString(R.string.addNumber));
                                }
                            }

                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else {
                                if (!appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                } else {
                                    if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isManageProfile()) {
                                        set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                    } else {
                                        set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                    }
                                }
                            }

                            // ((Button) view).setText(metadataMap.getProfileText().toUpperCase());
                            view.setOnClickListener(v -> {
                                if (CommonUtils.isValidPhoneNumber(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
                                    appCMSPresenter.showLoader();
                                    appCMSPresenter.phoneObjectRequest.setEmail(null);
                                    appCMSPresenter.phoneObjectRequest.setName(null);
                                    appCMSPresenter.phoneObjectRequest.setScreenName("update");
                                    appCMSPresenter.phoneObjectRequest.setRequestType("send");
                                    appCMSPresenter.phoneObjectRequest.setFragmentName("phoneUpdationLoginFragment");
                                    appCMSPresenter.phoneObjectRequest.setPhone(appCMSPresenter.getAppPreference().getLoggedInUserPhone());
                                    appCMSPresenter.sendPhoneOTP(appCMSPresenter.phoneObjectRequest, null);
                                } else {
                                    appCMSPresenter.openMobileUpdationScreen();
                                }

//                                appCMSPresenter.phoneObjectRequest.setEmail(appPreference.);
////                                appPreference
//                                FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
//                                fragmentTransaction.add(PhoneUpdationLoginFragment.newInstance(appCMSPresenter.getCurrentActivity(), appCMSPresenter.getPhoneObjectRequest(), true), "PhoneUpdationLoginFragment");
//                                fragmentTransaction.commitAllowingStateLoss();

                            });
                        }
                        break;
                        case PAGE_SETTINGS_REFERRAL_PROCEED_TO_PAYMENT_KEY:

                            ((Button) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            view.setBackgroundColor(appCMSPresenter.getButtonBorderColor());
                            ((Button) view).setGravity(Gravity.CENTER);
                            view.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                            if (moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getProceedButton() != null)
                                ((Button) view).setText(moduleAPIPub.getMetadataMap().getProceedButton());

                            view.setOnClickListener(v -> {
                                if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
                                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.V2_SUBSCRIPTION_FLOW);
                                    appCMSPresenter.navigateToLoginPage(false);
                                    appCMSPresenter.setReferralPlanPurchase(true);
                                    appCMSPresenter.setSelectedPlan(data.getId(), Collections.singletonList(data));
                                } else {
                                    if (CommonUtils.getPlay_Store_Country_Code(appCMSPresenter, Utils.getCountryCode()).equalsIgnoreCase("IN")) {
                                        ContentDatum dt = moduleAPIPub.getContentData().get(0);
                                        if (moduleAPIPub.getContentData().size() > 0) {
                                            double price = dt.getPlanDetails().get(0).getStrikeThroughPrice();
                                            if (price == 0) {
                                                price = dt.getPlanDetails().get(0).getRecurringPaymentAmount();
                                            }
                                            double discountedPrice = dt.getPlanDetails().get(0).getRecurringPaymentAmount();
                                            double discountedAmount = dt.getPlanDetails().get(0).getDiscountedPrice();
                                            appCMSPresenter.initiateSignUpAndSubscription(dt.getIdentifier(),
                                                    dt.getId(),
                                                    dt.getPlanDetails().get(0).getCountryCode(),
                                                    dt.getName(),
                                                    price,
                                                    discountedPrice,
                                                    dt.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                                                    dt.getPlanDetails().get(0).getCountryCode(),
                                                    dt.getRenewable(),
                                                    dt.getRenewalCycleType(),
                                                    false, discountedAmount,
                                                    dt.getPlanDetails().get(0).getAllowedPayMethods(),
                                                    dt.getPlanDetails().get(0).getCarrierBillingProviders());
                                        }
                                    } else {
                                        if (!Strings.isEmptyOrWhitespace(metadataMap.getPopupDescription()) && !Strings.isEmptyOrWhitespace(metadataMap.getPopupHeadingText())) {
                                            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.US_PLAN_VALIDATION, metadataMap.getPopupDescription(), false, () -> {
                                                appCMSPresenter.getCurrentActivity().finish();
                                            }, null, metadataMap.getPopupHeadingText());

                                        }
                                    }
                                }
                            });

                            break;
                        case BUTTON_CHANGE_PASSWORD:
                        case PAGE_SETTINGS_CHANGE_PASSWORD_KEY: {
                            if (metadataMap != null && metadataMap.getChangePassword() != null)
                                ((Button) view).setText(metadataMap.getChangePassword().toUpperCase());
                            if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.facebook_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            } else if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                                    (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                                            appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(context.getString(R.string.google_auth_provider_name_key)))) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            } else {
                                if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                                    if (appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_otp))) {
                                        set.setVisibility(view.getId(), INVISIBLE);
                                    } else if (appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_email))) {
                                        if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isManageProfile()) {
                                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                        } else {
                                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                        }
                                    }
                                } else {
                                    if (appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_otp))) {
                                        set.setVisibility(view.getId(), INVISIBLE);
                                    } else {
                                        set.setVisibility(view.getId(), VISIBLE);
                                    }

                                }
                            }
                            view.setOnClickListener(v -> {
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
                        }
                        break;
                        case LOGIN_WITH_GOOGLE:
                            Button googleButton = constraintLayout.findViewById(R.id.loginWithGoogle);
                            googleButton.setBackgroundColor(ContextCompat.getColor(context, R.color.googleRed));
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getSocialMedia() != null
                                    && appCMSPresenter.getAppCMSMain().getSocialMedia().getGooglePlus() != null)
                                googleButton.setVisibility(VISIBLE);
                            else
                                googleButton.setVisibility(GONE);
                            view.setOnClickListener(v -> {
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
                        case LOGIN_WITH_FACEBOOK:
                            Button facebookButton = constraintLayout.findViewById(R.id.facebookIcon);
                            facebookButton.setBackgroundColor(ContextCompat.getColor(context, R.color.facebookBlue));
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getSocialMedia() != null
                                    && appCMSPresenter.getAppCMSMain().getSocialMedia().getFacebook() != null)
                                facebookButton.setVisibility(VISIBLE);
                            else
                                facebookButton.setVisibility(GONE);
                            view.setOnClickListener(v -> {
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
                        case BUTTON_MANAGE_DOWNLOAD: {
                            if (appCMSPresenter.getAppPreference().getUserDownloadQualityPref() != null
                                    && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserDownloadQualityPref())) {
                                ((TextView) view).setText(component.getText());
                            }
                            ((Button) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            view.setOnClickListener(v -> {
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
                        }
                        break;
                        case RESET_PASSWORD_CONTINUE_BUTTON_KEY:
                            ((Button) view).setText(metadataMap.getEmailPasswordCta() == null ?
                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.reset_password_text))
                                    : metadataMap.getEmailPasswordCta().toUpperCase());
                            final MetadataMap tempMetadataMap = metadataMap;
                            view.setOnClickListener(v -> {
                                TextInputLayout etemailAddress = constraintLayout.findViewById(R.id.emailAddress);
                                Button resetPassword = constraintLayout.findViewById(R.id.resetPasswordButton);
                                resetPassword.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                applyBorderToComponent(
                                        context,
                                        resetPassword,
                                        component,
                                        appCMSPresenter.getBrandPrimaryCtaTextColor());
                                if (etemailAddress.getEditText().getText().toString().length() == 0) {
                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RESET_PASSWORD,
                                            appCMSPresenter.getLocalisedStrings().getEmptyEmailValidationText(),
                                            false,
                                            null,
                                            null, tempMetadataMap.getMobileHeaderText() == null ?
                                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_reset_password_title))
                                                    : tempMetadataMap.getMobileHeaderText());
                                } else if (!appCMSPresenter.isValidEmail(etemailAddress.getEditText().getText().toString())) {
                                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RESET_PASSWORD,
                                            appCMSPresenter.getLocalisedStrings().getEmailFormatValidationMsg(),
                                            false,
                                            null,
                                            null, tempMetadataMap.getMobileHeaderText() == null ?
                                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_reset_password_title))
                                                    : tempMetadataMap.getMobileHeaderText());
                                } else {
                                    appCMSPresenter.resetPassword(etemailAddress.getEditText().getText().toString(), tempMetadataMap.getMobileHeaderText() == null ?
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_reset_password_title))
                                            : tempMetadataMap.getMobileHeaderText(), tempMetadataMap);
                                }
                            });
                            break;
                        case PAGE_LOGIN_KEY:
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getLoginCta() != null)
                                (((Button) view)).setText(moduleAPIPub.getMetadataMap().getLoginCta().toUpperCase());
                            view.setOnClickListener(v -> {
                                TextInputLayout etpassword = constraintLayout.findViewById(R.id.password);
                                TextInputLayout etemailAddress = constraintLayout.findViewById(R.id.emailAddress);
                                String[] authData = new String[2];
                                authData[0] = etemailAddress.getEditText().getText().toString();
                                authData[1] = etpassword.getEditText().getText().toString();
                                appCMSPresenter.launchButtonSelectedAction(null,
                                        component.getAction(),
                                        null,
                                        authData,
                                        null,
                                        false,
                                        0,
                                        null);
                                etemailAddress.setVisibility(VISIBLE);
                                etpassword.setVisibility(VISIBLE);
                                Button login = constraintLayout.findViewById(R.id.login_btn);
                                login.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                applyBorderToComponent(
                                        context,
                                        login,
                                        component,
                                        appCMSPresenter.getBrandPrimaryCtaTextColor());
                                Button register = constraintLayout.findViewById(R.id.register);
                                register.setTextColor(Color.parseColor(component.getBackgroundColor()));
                                applyBorderToComponent(
                                        context,
                                        register,
                                        component,
                                        Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                                Button forgotPassword = constraintLayout.findViewById(R.id.forgotPassword);
                                forgotPassword.setTextColor(Color.parseColor(component.getBackgroundColor()));
                                applyBorderToComponent(
                                        context,
                                        forgotPassword,
                                        component,
                                        Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                                String action = component.getAction();
                                String[] extraData = new String[1];
                                extraData[0] = component.getKey();

                            });
                            break;
                        case PAGE_REGISTER_KEY:
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getSignUpCtaText() != null)
                                ((Button) view).setText(moduleAPIPub.getMetadataMap().getSignUpCtaText().toUpperCase());
                            view.setOnClickListener(v -> {
                                TextInputLayout etpassword = constraintLayout.findViewById(R.id.password);
                                TextInputLayout etemailAddress = constraintLayout.findViewById(R.id.emailAddress);
                                String[] authData = new String[3];
                                authData[0] = etemailAddress.getEditText().getText().toString();
                                authData[1] = etpassword.getEditText().getText().toString();
                                authData[2] = "true";
                                appCMSPresenter.launchButtonSelectedAction(null,
                                        component.getAction(),
                                        null,
                                        authData,
                                        null,
                                        false,
                                        0,
                                        null);
                                etemailAddress.setVisibility(VISIBLE);
                                etpassword.setVisibility(VISIBLE);
                                Button register = constraintLayout.findViewById(R.id.register);
                                register.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                applyBorderToComponent(
                                        context,
                                        register,
                                        component,
                                        appCMSPresenter.getBrandPrimaryCtaTextColor());
                                Button login = constraintLayout.findViewById(R.id.login_btn);
                                login.setTextColor(Color.parseColor(component.getBackgroundColor()));
                                applyBorderToComponent(
                                        context,
                                        login,
                                        component,
                                        Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                                Button forgotPassword = constraintLayout.findViewById(R.id.forgotPassword);
                                forgotPassword.setTextColor(Color.parseColor(component.getBackgroundColor()));
                                applyBorderToComponent(
                                        context,
                                        forgotPassword,
                                        component,
                                        Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                            });
                            break;
                        case RESET_PASSWORD_TITLE_KEY:
                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getForgotPasswordCtaText() != null)
                                ((Button) view).setText(moduleAPIPub.getMetadataMap().getForgotPasswordCtaText().toUpperCase());
                            view.setOnClickListener(v -> {
                                TextInputLayout etemailAddress = constraintLayout.findViewById(R.id.emailAddress);
                                TextInputLayout etpassword = constraintLayout.findViewById(R.id.password);
                                appCMSPresenter.launchButtonSelectedAction(null,
                                        component.getAction(),
                                        null,
                                        null,
                                        null,
                                        false,
                                        0,
                                        null);
                                etpassword.setVisibility(INVISIBLE);
                                etemailAddress.setVisibility(INVISIBLE);
                                Button register = constraintLayout.findViewById(R.id.register);
                                register.setTextColor(Color.parseColor(component.getBackgroundColor()));
                                applyBorderToComponent(
                                        context,
                                        register,
                                        component,
                                        Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));
                                Button login = constraintLayout.findViewById(R.id.login_btn);
                                login.setTextColor(Color.parseColor(component.getBackgroundColor()));
                                applyBorderToComponent(
                                        context,
                                        login,
                                        component,
                                        Color.parseColor(CommonUtils.getColor(context, component.getBackgroundColor())));

                                Button forgotPassword = constraintLayout.findViewById(R.id.forgotPassword);
                                forgotPassword.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                applyBorderToComponent(
                                        context,
                                        forgotPassword,
                                        component,
                                        appCMSPresenter.getBrandPrimaryCtaColor());

                            });
                            break;
                        case PAGE_AUTOPLAY_BACK_KEY: {

                            set.setVisibility(view.getId(), GONE);
                            /*
                            ((ImageButton) view).setImageResource(R.drawable.ic_back_arrow);
                            ((ImageButton) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            ((ImageButton) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.SRC_IN));
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                            final String closeAction = component.getAction();
                            view.setOnClickListener(v -> {

                                if (appCMSPresenter.getCurrentActivity() != null) {
                                    appCMSPresenter.getCurrentActivity().onBackPressed();
                                } else if (!appCMSPresenter.launchButtonSelectedAction(null,
                                        closeAction,
                                        null,
                                        null,
                                        null,
                                        false,
                                        0,
                                        null)) {

                                }
                            });
                            */
                        }
                        case PAGE_AUTOPLAY_CANCEL_BUTTON_KEY:
                            ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getCancelText());
                            view.setOnClickListener(v -> {
                                appCMSPresenter.sendCloseOthersAction(null,
                                        true,
                                        false);
                            });
                            break;
                        case PAGE_AUTOPLAY_MOVIE_PLAY_BUTTON_KEY:
                            if (view instanceof Button) {
                                ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getPlayText().toUpperCase());
                                ((Button) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                component.setBorderColor(appCMSPresenter.getAppCtaBackgroundColor());
                                component.setBorderWidth(1);
                                applyBorderToComponent(
                                        context,
                                        view,
                                        component,
                                        appCMSPresenter.getBrandPrimaryCtaColor());
                                view.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            }
                            break;
                        case PAGE_DOWNLOAD_QUALITY_CONTINUE_BUTTON_KEY: {

                            component.setBorderColor(appCMSPresenter.getAppCtaBackgroundColor());
                            view.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            component.setBorderWidth(1);
                            applyBorderToComponent(
                                    context,
                                    view,
                                    component,
                                    appCMSPresenter.getBrandPrimaryCtaColor());
                            ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getContinueCtaText());
                            ((Button) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                            ((Button) view).setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                        }
                        break;
                        case PAGE_START_WORKOUT: {
                            ((Button) view).setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            if (metadataMap != null && metadataMap.getStartWorkoutCTA() != null)
                                ((Button) view).setText(metadataMap.getStartWorkoutCTA());

                            appCMSPresenter.getLastWatchedSeriesData(data.getGist().getId(), appCMSSeriesHistoryList -> {
                                        SeriesHistory seriesHistory = null;
                                        String episodeImageUrl = null;
                                        if (appCMSSeriesHistoryList != null) {
                                            for (SeriesHistory tempSeriesHistory : appCMSSeriesHistoryList) {
                                                if (tempSeriesHistory.isLatestEpisode()) {
                                                    seriesHistory = tempSeriesHistory;
                                                    if (metadataMap != null && metadataMap.getResumeWorkoutCTA() != null)
                                                        ((Button) view).setText(metadataMap.getResumeWorkoutCTA());
                                                    break;
                                                }
                                            }

                                            for (Season_ season : data.getSeason()) {

                                                for (ContentDatum episode : season.getEpisodes()) {
                                                    if (episode.getGist().getId().equalsIgnoreCase(seriesHistory.getVideoId())) {

                                                        if (episode.getGist() != null
                                                                && episode.getGist().getImageGist() != null
                                                                && episode.getGist().getImageGist().get_16x9() != null) {
                                                            episodeImageUrl = episode.getGist().getImageGist().get_16x9();
                                                            finalEpisodeData = episode;
                                                            break;
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                            );
                            view.setOnClickListener(v -> {
                                int selectedSeason = appCMSPresenter.getSelectedSeason();
                                String action = "play";
                                if (moduleAPIPub != null &&
                                        moduleAPIPub.getContentData() != null &&
                                        !moduleAPIPub.getContentData().isEmpty() &&
                                        moduleAPIPub.getContentData().get(0) != null &&
                                        moduleAPIPub.getContentData().get(0).getSeason() != null &&
                                        !moduleAPIPub.getContentData().get(0).getSeason().isEmpty() &&
                                        moduleAPIPub.getContentData().get(0).getSeason().get(0) != null) {
                                    if (data != null && data.getGist() != null) {
                                        data.setModuleApi(moduleAPIPub);
                                    }
                                    List<String> allEpisodeIds = new ArrayList<>();
                                    List<Season_> seasons = moduleAPIPub.getContentData().get(0).getSeason();
                                    List<ContentDatum> allEpisodes = new ArrayList<>();
                                    int numSeasons = seasons.size();
                                    for (int i = 0; i < numSeasons; i++) {
                                        Season_ season = seasons.get(i);

                                        List<ContentDatum> episodes = season.getEpisodes();
                                        int numEpisodes = episodes.size();
                                        if (season.getEpisodes() != null) {
                                            for (int j = 0; j < numEpisodes; j++) {
                                                ContentDatum episodeContentDatum = episodes.get(j);
                                                if (season.getNumber() != 0 && episodeContentDatum.getNumber() != 0) {
                                                    StringBuilder seasonEpisodeNum = new StringBuilder();
                                                    seasonEpisodeNum.append("S");
                                                    seasonEpisodeNum.append(season.getNumber());
                                                    seasonEpisodeNum.append(" ");
                                                    seasonEpisodeNum.append("E");
                                                    seasonEpisodeNum.append(episodeContentDatum.getNumber());
                                                    episodeContentDatum.setSeasonEpisodeNum(seasonEpisodeNum.toString());
                                                }
                                                allEpisodes.add(episodeContentDatum);
                                                if (episodeContentDatum != null &&
                                                        episodeContentDatum.getGist() != null &&
                                                        episodeContentDatum.getGist().getId() != null) {
                                                    allEpisodeIds.add(episodeContentDatum.getGist().getId());
                                                }
                                            }
                                        }
                                    }
                                    List<String> relatedVideoIds = allEpisodeIds;
                                    int currentPlayingIndex = -1;
                                    if (moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes() != null &&
                                            moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().size() > 0 &&
                                            moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0).getGist() != null &&
                                            moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0).getGist().getId() != null) {


                                        if (allEpisodeIds != null) {
                                            int currentEpisodeIndex = allEpisodeIds.indexOf(moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0).getGist().getId());
                                            if (currentEpisodeIndex < allEpisodeIds.size()) {
                                                currentPlayingIndex = currentEpisodeIndex;
                                            }
                                        }
                                        if (relatedVideoIds == null) {
                                            currentPlayingIndex = 0;
                                        }
                                        final int currentIndex = currentPlayingIndex;
                                        String permalink = moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0).getGist().getPermalink();
                                        String title = moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0).getGist().getTitle();


                                        if (appCMSPresenter.isUserLoggedIn() &&
                                                data != null &&
                                                data.getGist() != null &&
                                                data.getGist().getId() != null) {

                                            if (finalEpisodeData != null && finalEpisodeData.getGist() != null && relatedVideoIds != null && relatedVideoIds.size() > 0) {
                                                int postion = 0;
                                                postion = relatedVideoIds.indexOf(finalEpisodeData.getGist().getId());
                                                if (!appCMSPresenter.launchButtonSelectedAction(finalEpisodeData.getGist().getPermalink(),
                                                        "watchVideo",
                                                        finalEpisodeData.getGist().getTitle(),
                                                        null,
                                                        finalEpisodeData,
                                                        false,
                                                        postion,
                                                        relatedVideoIds)) {

                                                }
                                            } else {
                                                if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                                                        action,
                                                        title,
                                                        null,
                                                        moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0),
                                                        false,
                                                        currentIndex,
                                                        relatedVideoIds)) {

                                                }
                                            }


                                        } else {
                                            if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                                                    action,
                                                    title,
                                                    null,
                                                    moduleAPIPub.getContentData().get(0).getSeason().get(selectedSeason).getEpisodes().get(0),
                                                    false,
                                                    currentIndex,
                                                    relatedVideoIds)) {

                                            }
                                        }
                                    }
                                }
                            });

                        }
                        break;
                        case PAGE_BACK_ARROW_KEY:
                        case PAGE_REST_KEY: {
//                            if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getLoginCta() != null)
//                                (((Button) view)).setText(moduleAPIPub.getMetadataMap().getLoginCta().toUpperCase());
                            (((Button) view)).setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                        }
                        break;
                        case PAGE_VIDEO_WATCH_TRAILER_KEY: {
                            if (data != null) {
                                if (metadataMap != null && metadataMap.getWatchTrailerCTA() != null) {
                                    ((Button) view).setText(metadataMap.getWatchTrailerCTA());
                                    applyBorderToComponent(
                                            context,
                                            view,
                                            component,
                                            appCMSPresenter.getGeneralTextColor());

                                }
                                ((Button) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                                ((Button) view).setPadding(80, 0, 80, 0);
                                if (data.getContentDetails() != null &&
                                        data.getContentDetails().getTrailers() != null &&
                                        !data.getContentDetails().getTrailers().isEmpty() &&
                                        data.getContentDetails().getTrailers().get(0) != null &&
                                        data.getContentDetails().getTrailers().get(0).getPermalink() != null &&
                                        data.getContentDetails().getTrailers().get(0).getId() != null /*&&
                                    moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getVideoAssets() != null*/) {
                                    final String watchTrailerAction = component.getAction();
                                    view.setOnClickListener(v -> {
                                        String[] extraData = new String[3];
                                        extraData[0] = data.getContentDetails().getTrailers().get(0).getPermalink();
//                                    extraData[1] = moduleAPI.getContentData().get(0).getContentDetails().getTrailers().get(0).getVideoAssets().getHls();
                                        extraData[2] = data.getContentDetails().getTrailers().get(0).getId();
                                        if (!appCMSPresenter.launchButtonSelectedAction(data.getContentDetails().getTrailers().get(0).getPermalink(),
                                                watchTrailerAction,
                                                data.getGist().getTitle(),
                                                extraData,
                                                data,
                                                false,
                                                -1,
                                                null)) {
                                            //Log.e(TAG, "Could not launch action: " +
//                                            " permalink: " +
//                                            moduleAPI.getContentData().get(0).getGist().getPermalink() +
//                                            " action: " +
//                                            component.getAction() +
//                                            " hls URL: " +
//                                            moduleAPI.getContentData().get(0).getStreamingInfo().getVideoAssets().getHls());
                                        }
                                    });
                                } else if (data.getShowDetails() != null &&
                                        data.getShowDetails().getTrailers() != null &&
                                        !data.getShowDetails().getTrailers().isEmpty() &&
                                        data.getShowDetails().getTrailers().get(0) != null &&
                                        data.getShowDetails().getTrailers().get(0).getPermalink() != null &&
                                        data.getShowDetails().getTrailers().get(0).getId() != null /*&&
                                    moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getVideoAssets() != null*/) {
                                    final String watchTrailerAction = component.getAction();

                                    view.setOnClickListener(v -> {
                                        String[] extraData = new String[3];
                                        extraData[0] = data.getShowDetails().getTrailers().get(0).getPermalink();
//                                    extraData[1] = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getVideoAssets().getHls();
                                        extraData[2] = data.getShowDetails().getTrailers().get(0).getId();

                                        if (!appCMSPresenter.launchButtonSelectedAction(data.getShowDetails().getTrailers().get(0).getPermalink(),
                                                watchTrailerAction,
                                                data.getGist().getTitle(),
                                                extraData,
                                                data,
                                                false,
                                                -1,
                                                null)) {
                                        }
                                    });

                                } else {
                                    set.setVisibility(view.getId(), GONE);

                                }

                            } else {
                                set.setVisibility(view.getId(), GONE);
                            }
                        }
                        break;
                        case PAGE_ADD_TO_WATCHLIST_KEY: {
                            view.setBackgroundResource(android.R.color.transparent);
                            List<String> filmIds = new ArrayList<>();
                            //TODO- below is to add episodes of shows/series
                        /*if (parentViewType == AppCMSUIKeyType.PAGE_API_SHOWDETAIL_MODULE_KEY &&
                                moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                !moduleAPI.getContentData().isEmpty() &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getSeason() != null &&
                                !moduleAPI.getContentData().get(0).getSeason().isEmpty()) {
                            List<Season_> seasons = moduleAPI.getContentData().get(0).getSeason();
                            int numSeasons = seasons.size();
                            for (int i = 0; i < numSeasons; i++) {
                                if (seasons.get(i).getEpisodes() != null &&
                                        !seasons.get(i).getEpisodes().isEmpty()) {
                                    List<ContentDatum> episodes = seasons.get(i).getEpisodes();
                                    int numEpisodes = episodes.size();
                                    for (int j = 0; j < numEpisodes; j++) {
                                        if (episodes.get(j).getGist() != null &&
                                                episodes.get(j).getGist().getId() != null) {
                                            filmIds.add(episodes.get(j).getGist().getId());
                                        }
                                    }
                                }
                            }
                        } else */
                            if (data != null &&
                                    data.getGist() != null) {

                                filmIds.add(data.getGist().getId());
                            }
                            boolean filmsAdded = true;
                            for (String filmId : filmIds) {
                                if (componentKey == AppCMSUIKeyType.PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY) {
                                    filmsAdded &= appCMSPresenter.isFilmAddedToFollowlist(filmId);
                                } else {
                                    filmsAdded &= appCMSPresenter.isFilmAddedToWatchlist(filmId);
                                }
                            }
                            if (data != null) {
                                ViewCreator.UpdateImageIconAction updateImageIconAction =
                                        new ViewCreator.UpdateImageIconAction((ImageButton) view,
                                                appCMSPresenter,
                                                filmIds,
                                                data,
                                                componentKey, metadataMap, component.getTintColor());
                                updateImageIconAction.updateWatchlistResponse(filmsAdded);
                            }
                            if (component.getTintColor() != null) {
                                ((ImageButton) view).setColorFilter(Color.parseColor(component.getTintColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                            if (data != null &&
                                    data.getStreamingInfo() != null &&
                                    data.getStreamingInfo().isLiveStream()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            }
                            if ((!appCMSPresenter.getAppCMSMain().getFeatures().isLoginModuleEnable() &&
                                    !appCMSPresenter.getAppCMSMain().getFeatures().isSignupModuleEnable()) ||
                                    appCMSPresenter.getAppCMSMain().getFeatures().isWatchlistHidden())
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                        }
                        break;
                        case PAGE_DOWNLOAD_QUALITY_CANCEL_BUTTON_KEY:
                          /*  if (moduleAPI != null && (jsonValueKeyMap.get(moduleAPI.getModuleType())
                                    == PAGE_AUTOPLAY_MODULE_KEY_01 ||
                                    jsonValueKeyMap.get(moduleAPI.getModuleType())
                                            == PAGE_AUTOPLAY_MODULE_KEY_02 ||
                                    jsonValueKeyMap.get(moduleAPI.getModuleType())
                                            == PAGE_AUTOPLAY_MODULE_KEY_03 ||
                                    jsonValueKeyMap.get(moduleAPI.getModuleType())
                                            == PAGE_AUTOPLAY_LANDSCAPE_MODULE_KEY
                            )) {
                                view.setId(R.id.autoplay_cancel_button);
                                view.setOnClickListener(v -> {
                                    if (!appCMSPresenter.sendCloseAutoplayAction(null,
                                            true,
                                            false)) {
                                        //Log.e(TAG, "Could not perform close action: " +
//                                            " action: " +
//                                            component.getAction());
                                    }
                                });
                            } else {*/
                            // view.setId(R.id.download_quality_cancel_button);
                            //}
                            applyBorderToComponent(
                                    context,
                                    view,
                                    component,
                                    appCMSPresenter.getGeneralTextColor());
                            ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getCancelText());
                            ((Button) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            break;
                        case PAGE_GRID_OPTION_KEY: {
                            view.setBackground(context.getDrawable(R.drawable.dots_more));
                            view.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
                            view.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        }
                        break;
                        case PAGE_FIRST_SEASON_FIRST_EPISODE_PLAY: {
                            ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                            ((ImageButton) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            ((ImageButton) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(), PorterDuff.Mode.SRC_IN));
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            view.setOnClickListener(v -> {
                                if (finalEpisodeData != null) {
                                    if (!appCMSPresenter.launchButtonSelectedAction(finalEpisodeData.getGist().getPermalink(),
                                            "watchVideo",
                                            finalEpisodeData.getGist().getTitle(),
                                            null,
                                            finalEpisodeData,
                                            false,
                                            0,
                                            null)) {

                                    }
                                }
                            });
                        }
                        break;
                        case PAGE_HOME_VOD_THUMBNAIL_PLAY: {

                            ((ImageButton) view).setImageResource(R.drawable.neo_video_detail_play);
                            ((ImageButton) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            ((ImageButton) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.MULTIPLY));
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            view.setVisibility(View.GONE);
                            view.setOnClickListener(v -> {

                                if (!appCMSPresenter.launchButtonSelectedAction(data.getGist().getPermalink(),
                                        childComponent.getAction(),
                                        data.getGist().getTitle(),
                                        null,
                                        data,
                                        false,
                                        0,
                                        null)) {

                                }

                            });
                        }
                        break;
                        case PAGE_VIDEO_CLOSE_KEY: {

                            ((ImageButton) view).setImageResource(R.drawable.ic_close_black);
                            ((ImageButton) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            if (component.getTintColor() != null) {
                                ((ImageButton) view).setColorFilter(Color.parseColor(component.getTintColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else
                                ((ImageButton) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.SRC_IN));
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                            /*ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) ((ImageButton) view).getLayoutParams();
                            lp.height = 80;
                            ((ImageButton) view).setLayoutParams(lp);*/

                            final String closeAction = component.getAction();
                            view.setOnClickListener(v -> {

                                if (appCMSPresenter.getCurrentActivity() != null) {
                                    appCMSPresenter.getCurrentActivity().onBackPressed();
                                } else if (!appCMSPresenter.launchButtonSelectedAction(null,
                                        closeAction,
                                        null,
                                        null,
                                        null,
                                        false,
                                        0,
                                        null)) {

                                }
                            });
                        }
//
                        break;
                        case PAGE_EXPAND_SHOWS_DESCRIPTION:
                            view.setPadding(1, 40, 10, 0);
                            final int dis_max_length;
                            if (BaseView.isTablet(context))
                                dis_max_length = context.getResources().getInteger(R.integer.app_cms_show_details_discription_length);
                            else
                                dis_max_length = context.getResources().getInteger(R.integer.app_cms_mobile_show_details_discription);
                            if (appCMSPresenter.getShowDetailsGist() != null &&
                                    appCMSPresenter.getShowDetailsGist().getDescription() != null) {
                                int descLength = appCMSPresenter.getShowDetailsGist().getDescription().length();
                                if (descLength > dis_max_length) {
                                    ((ImageButton) view).setImageResource(R.drawable.ic_arrow_down);
                                    ((ImageButton) view).setBackground(null);
                                }
                            } else if (moduleAPIPub != null && moduleAPIPub.getContentData() != null && !moduleAPIPub.getContentData().isEmpty()
                                    && moduleAPIPub.getContentData().get(0).getGist() != null && moduleAPIPub.getContentData().get(0).getGist().getDescription() != null) {
                                int descLength = moduleAPIPub.getContentData().get(0).getGist().getDescription().length();
                                if (descLength > dis_max_length) {
                                    ((ImageButton) view).setImageResource(R.drawable.ic_arrow_down);
                                    ((ImageButton) view).setBackground(null);
                                }
                            }
                            TextView descriptiontextView = constraintLayout.findViewById(R.id.contentDescription);
                            ImageButton moreImageButton = constraintLayout.findViewById(R.id.expandDescription);
                            moreImageButton.setColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(), android.graphics.PorterDuff.Mode.SRC_IN);
                            //  descriptiontextView.setMaxLines(6);
                            view.setOnClickListener(v -> {
                                if (appCMSPresenter.getshowdetailsContenData() != null &&
                                        appCMSPresenter.getshowdetailsContenData().getGist() != null &&
                                        appCMSPresenter.getshowdetailsContenData().getGist().getDescription() != null &&
                                        descriptiontextView != null) {
                                    String showsDescription = "";
                                    showsDescription = appCMSPresenter.getshowdetailsContenData().getGist().getDescription();
                                    if (appCMSPresenter.getshowdetailsContenData().getGist().isFullDescription() == true) {
                                        moreImageButton.setImageResource(R.drawable.ic_arrow_down);
                                        appCMSPresenter.getshowdetailsContenData().getGist().setFullDescription(false);
                                        CommonUtils.setMoreLinkInDescription(descriptiontextView, showsDescription, dis_max_length);
                                        descriptiontextView.scrollTo(0, 0);
                                    } else {
                                        moreImageButton.setImageResource(R.drawable.ic_arrow_up);
                                        appCMSPresenter.getshowdetailsContenData().getGist().setFullDescription(true);
                                        descriptiontextView.setText(showsDescription);
                                        if (!BaseView.isTablet(context) && showsDescription.length() > context.getResources().getInteger(R.integer.app_cms_max_text_length))
                                            CommonUtils.setMoreLinkInDescription(descriptiontextView, showsDescription, showsDescription.length());
//                                        else
//                                            descriptiontextView.setText(showsDescription);
                                    }
                                }
                            });
//
                            break;
                        //case PAGE_VIDEO_DETAIL_FAVOURITE_BUTTON_KEY:
                        case PAGE_BOOKMARK_FLAG_KEY: {
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            if (data != null && data.getGist() != null
                                    && data.getGist().isLiveStream()) {
                                view.setVisibility(View.INVISIBLE);
                            } else if (data != null && data.getGist() != null) {
                                view.setVisibility(View.VISIBLE);

                                List<String> filmIds = new ArrayList<>();
                                if (data != null && data.getGist() != null) {
                                    filmIds.add(data.getGist().getId());
                                }
                                boolean filmsAdded = true;
                                for (String filmId : filmIds) {
                                    filmsAdded &= appCMSPresenter.isFilmAddedToWatchlist(filmId);
                                }
                                if (data != null && data.getGist() != null) {
                                    ViewCreator.UpdateImageIconAction updateImageIconAction =
                                            new ViewCreator.UpdateImageIconAction((ImageButton) view,
                                                    appCMSPresenter,
                                                    filmIds,
                                                    data,
                                                    componentKey, metadataMap, component.getTintColor());
                                    updateImageIconAction.updateWatchlistResponse(filmsAdded);
                                }

                            }
                        }// end of PAGE_BOOKMARK_FLAG_KEY
                        break;
                        case PAGE_VIDEO_SHARE_KEY: {
                            ((ImageButton) view).setImageResource(R.drawable.ic_share);
                            ((ImageButton) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                            if (component.getTintColor() != null) {
                                ((ImageButton) view).setColorFilter(Color.parseColor(component.getTintColor()), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else
                                ((ImageButton) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.SRC_IN));
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                            final String shareAction = component.getAction();

                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                            layoutParams.height = 80;
                            view.setLayoutParams(layoutParams);

                            view.setOnClickListener(v -> {
                                AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
                                if (appCMSMain != null &&
                                        data != null &&
                                        data.getGist() != null &&
                                        data.getGist().getTitle() != null &&
                                        data.getGist().getPermalink() != null) {
                                    StringBuilder filmUrl = new StringBuilder();
                                    filmUrl.append(appCMSMain.getDomainName());
                                    filmUrl.append(data.getGist().getPermalink());
                                    String[] extraData = new String[1];
                                    extraData[0] = filmUrl.toString();
                                    if (!appCMSPresenter.launchButtonSelectedAction(data.getGist().getPermalink(),
                                            shareAction,
                                            data.getGist().getTitle(),
                                            extraData,
                                            data,
                                            false,
                                            0,
                                            null)) {
                                        //Log.e(TAG, "Could not launch action: " +
//                                            " permalink: " +
//                                            moduleAPI.getContentData().get(0).getGist().getPermalink() +
//                                            " action: " +
//                                            component.getAction() +
//                                            " film URL: " +
//                                            filmUrl.toString());
                                    }
                                }
                            });
                            if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().getContentType() != null &&
                                    data.getGist().getContentType().equalsIgnoreCase("AUDIO")) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);

                            }
                        } // end of case PAGE_VIDEO_SHARE_KEY:
                        break;
                        case PAGE_VIDEO_WHATSAPP_KEY: {
                            ((ImageButton) view).setImageResource(R.drawable.ic_whatsapp);
                            ((ImageButton) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.SRC_IN));
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                            view.setOnClickListener(v -> {
                                AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
                                if (appCMSMain != null &&
                                        data != null &&
                                        data.getGist() != null &&
                                        data.getGist().getTitle() != null &&
                                        data.getGist().getPermalink() != null) {
                                    String[] extraData = new String[1];
                                    String filmUrl = appCMSMain.getDomainName() + data.getGist().getPermalink();
                                    extraData[0] = filmUrl;
                                    appCMSPresenter.launchButtonSelectedAction(data.getGist().getPermalink(),
                                            component.getAction(),
                                            data.getGist().getTitle(),
                                            extraData,
                                            data,
                                            false,
                                            0,
                                            null);
                                }
                            });
                            if (!CommonUtils.isAppInstalled(context, CommonUtils.WHATSAPPP_PACKAGE_NAME)
                                    || !appCMSPresenter.isHoichoiApp()
                                    || (data != null && data.getGist() != null && data.getGist().getContentType() != null
                                    && data.getGist().getContentType().equalsIgnoreCase("AUDIO"))) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }

                        } // end of case PAGE_VIDEO_WHATSAPP_KEY:
                        break;
                        case PAGE_REMOVEALL_KEY: {
                            ((Button) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                            view.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                           /* componentViewResult.addToPageView = true;
                            FrameLayout.LayoutParams removeAllLayoutParams =
                                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                            removeAllLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

                            switch (jsonValueKeyMap.get(componentKey)) {
                                case PAGE_HISTORY_01_MODULE_KEY:
                                case PAGE_HISTORY_02_MODULE_KEY:
                                    if (metadataMap != null && metadataMap.getRemoveAllHistoryLabel() != null)
                                        ((Button) view).setText(metadataMap.getRemoveAllHistoryLabel());
                                    break;

                                case PAGE_DOWNLOAD_01_MODULE_KEY:
                                case PAGE_DOWNLOAD_02_MODULE_KEY:
                                    if (metadataMap != null && metadataMap.getRemoveAllDownloadCta() != null)
                                        ((Button) view).setText(metadataMap.getRemoveAllDownloadCta());
                                    break;

                                case PAGE_WATCHLIST_01_MODULE_KEY:
                                case PAGE_WATCHLIST_02_MODULE_KEY:
                                    if (metadataMap != null && metadataMap.getRemoveAllWatchlistLabel() != null)
                                        ((Button) view).setText(metadataMap.getRemoveAllWatchlistLabel());
                                    break;

                                default:
                                    break;
                            }


                            view.setLayoutParams(removeAllLayoutParams);
                            view.setId(R.id.remove_all_download_id);
                            componentViewResult.onInternalEvent = new ViewCreator.OnRemoveAllInternalEvent(moduleId,
                                    view);
                            view.setOnClickListener(new View.OnClickListener() {
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
                            });*/
                        }
                        break;
                        case PAGE_VIDEO_DOWNLOAD_BUTTON_KEY: {
                            ((ImageButton) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                            view.setBackgroundResource(android.R.color.transparent);
                            if (data != null &&
                                    data.getGist() != null) {
                                String userId = appCMSPresenter.getLoggedInUser();
                                int radiusDifference = 7;
                                if (BaseView.isTablet(context)) {
                                    radiusDifference = 4;
                                }
                                if (data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().toLowerCase().contains(context.getString(R.string.media_type_audio).toLowerCase())) {
                                    radiusDifference = 5;
                                    if (BaseView.isTablet(context)) {
                                        radiusDifference = 3;
                                    }
                                }

                                view.setTag(data.getGist().getId());
                                /* *
                                 * if video is not purchased then hide download option
                                 */

                                if ((data.getPricing() != null &&
                                        data.getPricing().getType() != null &&
                                        (data.getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_TVOD)) || data.getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_PPV))))) {

                                    //if transaction data from getAPI() is empty then hide download button

                                    if (data.getGist().getObjTransactionDataValue() != null &&
                                            data.getGist().getObjTransactionDataValue().size() > 0) {
                                        if (data.getGist().getObjTransactionDataValue().get(0).size() == 0) {
                                            AppCMSTransactionDataValue obj = data.getGist().getObjTransactionDataValue().get(0).get(data.getGist().getId());
                                            set.setVisibility(view.getId(), ConstraintSet.GONE);
                                            break;
//
                                        }
                                    }

                                }
                                /**
                                 * if schedule start date is not 0 and greater than current time Or
                                 *  End date is expired then hide download button as video is not started

                                 */
                                if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter)) {
                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                    break;

                                } else if (ViewCreator.isVideoIsSchedule(data, appCMSPresenter)) {
                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                    break;
                                }


                                if (appCMSPresenter.isDownloadEnable()) {
                                    if (data.isDRMEnabled()) {
                                        appCMSPresenter.getUserOfflineVideoDownloadStatus(data.getGist().getId(), new FetchOffineDownloadStatus((ImageButton) view, appCMSPresenter,
                                                data, userId, radiusDifference,
                                                data.getGist().getId()), userId);
                                    } else {
                                        appCMSPresenter.getUserVideoDownloadStatus(
                                                data.getGist().getId(), new ViewCreator.UpdateDownloadImageIconAction((ImageButton) view, appCMSPresenter,
                                                        data, userId, radiusDifference,
                                                        data.getGist().getId()), userId);
                                    }
                                    set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                }
                            }
                            if (data != null &&
                                    data.getGist() != null &&
                                    data.getGist().isLiveStream()) {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            }
                        }
                        break;

                        case PAGE_ADD_NEW_CARD_KEY: {
                            if (!TextUtils.isEmpty(component.getTextColor()))
                                ((Button) view).setTextColor(Color.parseColor(component.getTextColor()));
                            ((Button) view).setText(component.getText());
                            ((Button) view).setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                            view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, view);
                            view.setOnClickListener(v -> {
                                appCMSPresenter.navigateToCardPage();
                            });
                        }
                        break;

                        case PAGE_MORE_BANKS_TITLE_KEY: {
                            if (!TextUtils.isEmpty(component.getTextColor()))
                                ((Button) componentViewResult.componentView).setTextColor(Color.parseColor(component.getTextColor()));
                            //((Button) componentViewResult.componentView).setText(component.getText());
                            ((Button) componentViewResult.componentView).setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                            componentViewResult.componentView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, componentViewResult.componentView);
                            if (moduleInfo.getSettings().isNetbankingEnabled() && !moduleInfo.getSettings().isBankDropDownList()) {
                                ((Button) componentViewResult.componentView).setText("");
                            } else {
                                ((Button) componentViewResult.componentView).setText(component.getText());
                            }
                            componentViewResult.componentView.setOnClickListener(v -> {
                                if (moduleInfo.getSettings().isBankDropDownList()) {
                                    appCMSPresenter.navigateToBankListPage();
                                }
                            });
                            if (moduleInfo.getSettings().isBankDropDownList()) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                        }

                        break;

                        case PAGE_UPI_PAY_BUTTON_KEY: {
                            if (!TextUtils.isEmpty(component.getTextColor()))
                                ((Button) view).setTextColor(Color.parseColor(component.getTextColor()));
                            ((Button) view).setText(component.getText());
                            ((Button) view).setGravity(Gravity.CENTER);
                            if (!TextUtils.isEmpty(component.getBackgroundColor()))
                                view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                            else
                                view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                            ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, view);

                            view.setAlpha(0.5f);
                            view.setEnabled(false);

                            view.setOnClickListener(v -> {
                                EditText upiEditText = appCMSPresenter.getCurrentActivity().findViewById(R.id.upiEditText);
                                if (upiEditText == null) return;
                                String upiId = upiEditText.getText().toString();
                                if (!TextUtils.isEmpty(upiId)) {
                                    appCMSPresenter.closeSoftKeyboard();
                                    if (appCMSPresenter.getJusPayUtils() != null)
                                        appCMSPresenter.getJusPayUtils().upiTransaction(upiId);
                                } else {
                                    appCMSPresenter.showToast(context.getString(R.string.please_fill_valid_details), Toast.LENGTH_SHORT);
                                }
                            });
                        }

                        break;

                        case PAGE_CARD_PAY_BUTTON_KEY: {
                            if (!TextUtils.isEmpty(component.getTextColor()))
                                ((Button) view).setTextColor(Color.parseColor(component.getTextColor()));
                            ((Button) view).setText(component.getText());
                            ((Button) view).setGravity(Gravity.CENTER);
                            if (!TextUtils.isEmpty(component.getBackgroundColor()))
                                view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                            else
                                view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                            ViewCreator.setTypeFace(context, appCMSPresenter, jsonValueKeyMap, component, view);


                            view.setOnClickListener(v -> {
                                EditText cardNo = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardNoEditText);
                                EditText cardMM = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardMMEditText);
                                EditText cardYY = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardYYEditText);
                                EditText cardCVV = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardCVVEditText);
                                EditText cardName = appCMSPresenter.getCurrentActivity().findViewById(R.id.cardNameEditText);
                                CheckBox saveCard = appCMSPresenter.getCurrentActivity().findViewById(R.id.chkBoxSaveCard);

                                if (cardNo == null || cardMM == null || cardYY == null || cardCVV == null || cardName == null || saveCard == null)
                                    return;

                                String number = cardNo.getText().toString();
                                String mm = cardMM.getText().toString();
                                String yy = cardYY.getText().toString();
                                String cvv = cardCVV.getText().toString();
                                String name = cardName.getText().toString();

                                if (TextUtils.isEmpty(number) || TextUtils.isEmpty(mm) || TextUtils.isEmpty(yy) || TextUtils.isEmpty(cvv) || TextUtils.isEmpty(name)) {
                                    appCMSPresenter.showToast(context.getString(R.string.please_fill_valid_details), Toast.LENGTH_SHORT);
                                } else {
                                    appCMSPresenter.closeSoftKeyboard();
                                    if (appCMSPresenter.getJusPayUtils() != null)
                                        appCMSPresenter.getJusPayUtils().onCardPayment(number, mm, yy, cvv, name, saveCard.isChecked());
                                }
                            });
                        }

                        break;

                        case PAGE_SETTINGS_PARENTAL_CONTROLS_KEY: {
                            String buttonTxt = component.getText();
                            if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getParentalControlHeader())) {
                                buttonTxt = metadataMap.getParentalControlHeader();
                            }

                            SpannableString content = new SpannableString(buttonTxt);
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            ((Button) view).setText(content);

                            if (appCMSPresenter != null && appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().isParentalControlsEnable()) {
                                view.setEnabled(true);
                                set.setAlpha(view.getId(), 1f);
                                ((Button) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            } else {
                                view.setEnabled(false);
                                set.setAlpha(view.getId(), 0.5f);
                                ((Button) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            }

                            view.setOnClickListener(v -> {
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
                        }
                        break;

                    } // End of switch (componentKey) PAGE_BUTTON_KEY:

                }// End of case PAGE_BUTTON_KEY :
                break;
                case PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY: {

                    //  ((ImageButton) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    view.setBackgroundResource(android.R.color.transparent);
                    if (data != null &&
                            data.getGist() != null) {
                        String userId = appCMSPresenter.getLoggedInUser();
                        view.setTag(data.getGist().getId());

                        /* *
                         * if video is not purchased then hide download option

                         */

                        if ((data.getPricing() != null &&
                                data.getPricing().getType() != null &&
                                (data.getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_TVOD)) || data.getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_PPV))))) {

                            //if transaction data from getAPI() is empty then hide download button

                            if (data.getGist().getObjTransactionDataValue() != null &&
                                    data.getGist().getObjTransactionDataValue().size() > 0) {
                                if (data.getGist().getObjTransactionDataValue().get(0).size() == 0) {
                                    AppCMSTransactionDataValue obj = data.getGist().getObjTransactionDataValue().get(0).get(data.getGist().getId());

                                    set.setVisibility(view.getId(), ConstraintSet.GONE);
                                    break;
//
                                }
                            }

                        }
                        /**
                         * if schedule start date is not 0 and greater than current time Or
                         *  End date is expired then hide download button as video is not started

                         */
                        if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter)) {
                            //view.setVisibility(View.INVISIBLE);
                            set.setVisibility(view.getId(), ConstraintSet.GONE);
                            //break;

                        } else if (ViewCreator.isVideoIsSchedule(data, appCMSPresenter)) {
                            //view.setVisibility(View.INVISIBLE);
                            set.setVisibility(view.getId(), ConstraintSet.GONE);
                            // break;

                        }

                        if (appCMSPresenter.isDownloadEnable()) {
                            appCMSPresenter.getUserVideoDownloadStatus(
                                    data.getGist().getId(), new DownloadComponent.UpdateDownloadImageIconAction((DownloadComponent) view, appCMSPresenter,
                                            data, userId), userId);
                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                        } else {
                            set.setVisibility(view.getId(), ConstraintSet.GONE);

                        }

                    }
                    if (data != null &&
                            data.getGist() != null &&
                            data.getGist().isLiveStream()) {
                        set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                    } else {
                        set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                    }
                }// end PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY
                break;
                case PAGE_PROGRESS_VIEW_KEY:
                    if (appCMSPresenter.isUserLoggedIn() && view instanceof ProgressBar) {
                        int color = ContextCompat.getColor(context, R.color.colorAccent);

                        if (!TextUtils.isEmpty(appCMSPresenter.getAppCtaBackgroundColor())) {
                            color = Color.parseColor(CommonUtils.getColor(context, appCMSPresenter.getAppCtaBackgroundColor()));
                        }
                        ((ProgressBar) view).getProgressDrawable()
                                .setColorFilter(color, PorterDuff.Mode.SRC_IN);
                        view.setBackgroundColor(color & 0x44ffffff);

                        ((ProgressBar) view).setMax(100);
                        ((ProgressBar) view).setProgress(0);
                        if (data != null &&
                                data.getGist() != null) {
                            if (data.getGist().getWatchedPercentage() > 0) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                ((ProgressBar) view)
                                        .setProgress(data.getGist().getWatchedPercentage());
                            } else if (data.getGist().getWatchedTime() > 0) {
                                long watchedTime =
                                        data.getGist().getWatchedTime();
                                long runTime =
                                        data.getGist().getRuntime();
                                if (watchedTime > 0 && runTime > 0) {
                                    long percentageWatched = (long) (((double) watchedTime / (double) runTime) * 100.0);
                                    ((ProgressBar) view)
                                            .setProgress((int) percentageWatched);

                                    set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                } else {

                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                    ((ProgressBar) view).setProgress(0);
                                }
                            } else {
                                ContentDatum userHistoryContentDatum = appCMSPresenter.getUserHistoryContentDatum(data.getGist().getId());
                                if (userHistoryContentDatum != null) {
                                    long watchedTime = userHistoryContentDatum.getGist().getWatchedTime();
                                    long runTime = userHistoryContentDatum.getGist().getRuntime();
                                    if (watchedTime > 0 && runTime > 0) {
                                        long percentageWatched = (long) (((double) watchedTime / (double) runTime) * 100.0);
                                        ((ProgressBar) view)
                                                .setProgress((int) percentageWatched);

                                        set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                    } else {

                                        set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                        ((ProgressBar) view).setProgress(0);
                                    }
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                    ((ProgressBar) view).setProgress(0);
                                }
                            }
                        } else {

                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            ((ProgressBar) view).setProgress(0);
                        }

                    } else {
                        set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                    }
                    break;
                case PAGE_TABLE_VIEW_KEY: {
                    switch (componentKey) {
                        case PAGE_TABLE_VIEW_SETTING_LANGUAGE_DOWNLOAD_KEY:
                            ((RecyclerView) view)
                                    .setLayoutManager(new LinearLayoutManager(context,
                                            LinearLayoutManager.VERTICAL,
                                            false));
                            List<BaseInterface> mpegs = new ArrayList<>();
                            if (data != null &&
                                    data.getStreamingInfo() != null &&
                                    data.getStreamingInfo().getVideoAssets() != null &&
                                    data.getStreamingInfo().getVideoAssets().getMpeg() != null) {
                                mpegs.addAll(data.getStreamingInfo().getVideoAssets().getMpeg());
                            } else if (data != null &&
                                    data.getLanguages() != null &&
                                    data.getLanguages().size() > 0) {
                                mpegs.addAll(data.getLanguages());
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

                            ((RecyclerView) view).setAdapter(radioAdapter);

                            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                            lp.height = 0;
                            view.setLayoutParams(lp);
                            break;
                        case PAGE_VIDEO_DETAIL_EQUIPMENT_NEEDED_LIST_KEY:
                            if (BaseView.isTablet(context)) {
                                ViewGroup.LayoutParams params = view.getLayoutParams();
                                params.width = com.viewlift.views.customviews.BaseView.getDeviceWidth() * 40 / 100;
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                view.setLayoutParams(params);
                            } else {

                            }
                            List<EquipmentElement> list = new ArrayList<>();
                         /*   EquipmentElement topItem = new EquipmentElement();
                            topItem.setName("SET - UP ");
                            list.add(topItem);*/
                            if (data != null && data.getTags() != null) {
                                List<Tag> tagsE = data.getTags();
                                if (tagsE != null) {
                                    for (int i = 0; i < tagsE.size(); i++) {
                                        if (tagsE.get(i).getTagType() != null &&
                                                tagsE.get(i).getTagType().equals("equipment")) {
                                            EquipmentElement element = new EquipmentElement();
                                            if (tagsE.get(i).getTitle() != null
                                                    && (tagsE.get(i).getTitle().contains("No equipment")
                                                    || tagsE.get(i).getTitle().contains("No Equipment")
                                                    || tagsE.get(i).getTitle().contains("No EQUIPMENT")
                                                    || tagsE.get(i).getTitle().equalsIgnoreCase("No equipment necessary")
                                                    || tagsE.get(i).getTitle().equalsIgnoreCase(context.getString(R.string.no_equipment_needed)))) {
                                                //element.setName(context.getString(R.string.no_equipment_needed));
                                                //list.add(element);
                                                break;

                                            } else if (tagsE.get(i).getTitle() != null) {
                                                element.setName(tagsE.get(i).getTitle().toUpperCase());
                                                element.setRequired(true);
                                            }

                                            if (tagsE.get(i).getImages() != null &&
                                                    tagsE.get(i).getImages().get_1x1() != null &&
                                                    tagsE.get(i).getImages().get_1x1().getUrl() != null) {
                                                element.setEquipment_needed(tagsE.get(i).getImages().get_1x1().getUrl());
                                            }

                                            list.add(element);
                                        }

                                    }
                                }
                                List<Tag> tagsO = data.getOptionalTags();
                                if (tagsO != null) {
                                    for (int i = 0; i < tagsO.size(); i++) {
                                        if (tagsO.get(i).getTagType() != null &&
                                                tagsO.get(i).getTagType().equals("equipment")) {
                                            EquipmentElement element = new EquipmentElement();
                                            if (tagsO.get(i).getTitle() != null
                                                    && (tagsO.get(i).getTitle().contains("No equipment")
                                                    || tagsO.get(i).getTitle().contains("No Equipment")
                                                    || tagsO.get(i).getTitle().contains("No EQUIPMENT")
                                                    || tagsO.get(i).getTitle().equalsIgnoreCase("No equipment necessary")
                                                    || tagsO.get(i).getTitle().equalsIgnoreCase(context.getString(R.string.no_equipment_needed)))) {

                                                break;

                                            } else if (tagsO.get(i).getTitle() != null) {
                                                element.setName(tagsO.get(i).getTitle().toUpperCase());
                                                element.setRequired(false);
                                            }

                                            if (tagsO.get(i).getImages() != null &&
                                                    tagsO.get(i).getImages().get_1x1() != null &&
                                                    tagsO.get(i).getImages().get_1x1().getUrl() != null) {
                                                element.setEquipment_needed(tagsO.get(i).getImages().get_1x1().getUrl());
                                            }
                                            if (!TextUtils.isEmpty(element.getName())) {
                                                list.add(element);
                                            }


                                        }
                                    }
                                }

                                if (list.size() == 0) {
                                    EquipmentElement noEquipment = new EquipmentElement();
                                    noEquipment.setName(context.getString(R.string.no_equipment_needed));
                                    list.add(noEquipment);
                                }

                                EquipmentGridAdapter equipmentGridAdapter = new EquipmentGridAdapter(list, componentKey, appCMSPresenter);
                                ((RecyclerView) view).setAdapter(equipmentGridAdapter);
                            }
                            break;
                        case PAGE_EXPENDABLE_LIST:
                            List<ExpandableItem> emptyList = new ArrayList<>();
                            emptyList.add(null);
                            ExpandableItem expandableItem = new ExpandableItem(null, emptyList);
                            List<ExpandableItem> expandableItemList = new ArrayList<>();
                            expandableItemList.add(expandableItem);
                            expandableItemList.add(expandableItem);
                            ExpandableGridAdapter adapter = new ExpandableGridAdapter(expandableItemList, component, appCMSPresenter, data, context);
                            ((RecyclerView) view).setAdapter(adapter);
                            break;
                        case PAGE_INSTRUCTOR_RECENT_GRID_KEY:
                            RecentClassesAdapter recentClassesAdapter = new RecentClassesAdapter(component, appCMSPresenter, data);
                            ((RecyclerView) view).setAdapter(recentClassesAdapter);
                            break;
                    }
                    break;
                }
                case PAGE_CARD_VIEW:
                    if (component.getBackgroundColor() != null)
                        view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    else
                        view.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                    ((CardView) view).setCardElevation(30);
                    break;
                case PAGE_VIEW_DETAILS_BACKGROUND:
                    view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    break;
                case PAGE_SEPARATOR_VIEW_KEY:
                    if (componentKey == PAGE_EDIT_PROFILE_CHAIN_BACKGROUND_KEY) {
                        PaintDrawable gdDefault = new PaintDrawable(Color.parseColor(component.getBackgroundColor()));
                        gdDefault.setCornerRadius(20f);
                        view.setBackground(gdDefault);
                    } else if (componentKey == PAGE_SEPARATOR_VIEW_KEY) {
                        if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && !CommonUtils.isEmpty(moduleAPIPub.getMetadataMap().getBackgroundColor())) {
                            ((View) view).setBackgroundColor(Color.parseColor(moduleAPIPub.getMetadataMap().getBackgroundColor()));
                        } else if (!CommonUtils.isEmpty(component.getBackgroundColor())) {
                            ((View) view).setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                        } else {
                            ((View) view).setBackgroundColor(Color.parseColor(getColor(appCMSPresenter.getCurrentContext(), appCMSPresenter.getAppBackgroundColor())));
                        }
                    } else if (componentKey == PAGE_FILL_BACKGROUND) {
                        if (moduleAPIPub != null && moduleAPIPub.getMetadataMap() != null && moduleAPIPub.getMetadataMap().getBackgroundColor() != null)
                            ((View) view).setBackgroundColor(Color.parseColor(moduleAPIPub.getMetadataMap().getBackgroundColor()));
                        else
                            ((View) view).setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    } else if (componentKey == TRAY_ARTICLE_SEPARATOR_VIEW_KEY) {
                        ((View) view).setBackgroundColor(appCMSPresenter.getGeneralTextColor());
                    } else if (componentKey == PAGE_VIEW_TIMER_BACKGROUND) {
                        ((View) view).setBackgroundColor((appCMSPresenter.getBlockTitleTextColor()));
                    } else if (component.getBackgroundColor() != null) {
                        view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    }

                    break;
                case DATE_SEPARATOR_VIEW_KEY:
                    view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                    break;
                case PAGE_BLOCK_VIEW_KEY: {
                    if (!TextUtils.isEmpty(childComponent.getBorderColor())) {
                        applyBorderToComponent(context, view, component, -1);
                    } else {
                        if (!TextUtils.isEmpty(component.getBackgroundColor())) {
                            view.setBackgroundColor(Color.parseColor(component.getBackgroundColor()));
                        }
                    }

                    if (component.getRotation() > 0) {
                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                view.setRotation(component.getRotation());
                            }
                        });
                    }

                    break;
                }
                case PAGE_BRAND_CAROUSEL_MODULE_TYPE:
                    if (Utils.position >= appCMSPresenter.next7Days().length || Utils.position < 0) {
                        Utils.position = 0;
                    }
                    Utils.position++;


                    AppCMSCarouselItemAdapter appCMSCarouselItemAdapter = new AppCMSCarouselItemAdapter(context,
                            null,
                            component.getSettings(),
                            component.getLayout(),
                            component,
                            jsonValueKeyMap,
                            moduleAPIPub,
                            (RecyclerView) view,
                            true,
                            appCMSAndroidModule,
                            componentViewType,
                            childComponent.getBlockName(), true, this);
                    ((RecyclerView) view).setAdapter(appCMSCarouselItemAdapter);

                    break;
                case PAGE_COLLECTIONGRID_KEY: {
                    switch (componentKey) {
                        case PAGE_VOD_MORE_COLLECTIONGRID_KEY:// VOD More tray
                        case PAGE_VOD_CONCEPT_COLLECTIONGRID_KEY://VOD Concept tray
                        {
                            ((RecyclerView) view).setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            view.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            set.setHorizontalChainStyle(view.getId(), ConstraintSet.CHAIN_SPREAD);


                        }
                        break;

                        case PAGE_WEEK_SCHEDULE_GRID_KEY: {
                            try {
                                //Log.e("***","pagesarray"+data.getPages().size());
                                ConstraintLayout.LayoutParams lpe = new ConstraintLayout.LayoutParams(0, 0);
                                view.setLayoutParams(lpe);
                                int columns = 0;
                                if (BaseView.isTablet(view.getContext())) {
                                    if (!BaseView.isLandscape(view.getContext())) {
                                        columns = BaseView.isTablet(view.getContext()) ? 3 : 1;
                                    } else {
                                        columns = BaseView.isTablet(view.getContext()) ? 5 : 1;
                                    }

                                } else {
                                    columns = BaseView.isTablet(view.getContext()) ? 2 : 2;
                                }

                                ((RecyclerView) view)
                                        .setLayoutManager(new GridLayoutManager(context,
                                                columns,
                                                GridLayoutManager.VERTICAL,
                                                false));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }


                            Module mAp = moduleAPIPub;


                            if (componentKey == PAGE_WEEK_SCHEDULE_GRID_KEY) {

                                if (Utils.position == 0 || Utils.position < 0) {
                                    Utils.position = 0;
                                }
                                mAp = ViewCreator.setPageLinkCategoryDataInList(moduleAPIPub, 0);
                                Utils.position++;
                            }
                            if (mAp.getContentData() != null) {
//                                AppCMSViewAdapter appCMSViewAdapter = new AppCMSViewAdapter(context,
//                                        null,
//                                        appCMSPresenter,
//                                        null,
//                                        component.getLayout(),
//                                        false,
//                                        component,
//                                        jsonValueKeyMap,
//                                        mAp,
//                                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                                        componentViewType,
//                                        appCMSAndroidModule,
//                                        childComponent.getBlockName(),
//                                        true, this);


                                AppCMSViewAdapter appCMSViewAdapter = new AppCMSViewAdapter(context,
                                        null,
                                        null,
                                        component.getLayout(),
                                        false,
                                        component,
                                        jsonValueKeyMap,
                                        mAp,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        "",
                                        appCMSAndroidModule,
                                        blockName,
                                        true, this);
                                //componentViewResult.useWidthOfScreen = true;
                                if (componentKey != AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_KEY) {
                                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                                }
                                //((RecyclerView) view).addItemDecoration(new SeparatorDecoration(((RecyclerView) view).getContext(), Color.DKGRAY, 1f));
                                ((RecyclerView) view).setAdapter(appCMSViewAdapter);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.GONE);
                            }
                        }
                        break;
                        default: {

                        }
                    }
                }
                break;

            }// end of switch (componentType)
            setViewPotion(context, set, childComponent, view);
        }
        set.applyTo(constraintLayout);

       /* if (viewId!=0){
            View view1 = collectionGridItemView.findViewById(viewId);
            if (view1!=null)
            view1.setVisibility(View.INVISIBLE);
            viewId = 0;
        }*/

    }


    public void startTimer(long remainingTime, TextView timerTextView, ImageButton
            imageView, ContentDatum datum) {
        final long[] mRemainingTime = new long[1];
        final int countDownIntervalInMillis = 1000;
        //remainingTime = 10000;
        mRemainingTime[0] = remainingTime;

        CountDownTimer countDownTimer = new CountDownTimer(remainingTime, countDownIntervalInMillis) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 0) {
                    onFinish();
                } else {
                    mRemainingTime[0] = mRemainingTime[0] - 1000;
                    timerTextView.setVisibility(VISIBLE);
                    timerTextView.setText(String.format("Starts in %s", Utils.formatTimeAndDateVT2(mRemainingTime[0])));
                    timerTextView.setBackgroundColor(0x9f000000 + appCMSPresenter.getGeneralBackgroundColor());
                    imageView.setVisibility(INVISIBLE);

                }
            }

            public void onFinish() {

                if (datum != null
                        && datum.getStreamingInfo() != null
                        && datum.getStreamingInfo().getVideoAssets() != null
                        && datum.getStreamingInfo().getVideoAssets().getHls() != null) {
                    showPlayForLive(imageView, timerTextView);
                } else if (datum != null
                        && datum.getGist() != null
                        && datum.getGist().getId() != null) {
                    updateVideoStreamingInfo(datum.getGist().getId(), datum, imageView, timerTextView);
                        /*appCMSPresenter.refreshVideoData(moduleAPI.getContentData().get(0).getGist().getId(), contentDatum -> {
                            if (contentDatum != null && contentDatum.getStreamingInfo() != null) {

                                moduleAPI.getContentData().get(0).setStreamingInfo(contentDatum.getStreamingInfo());
                                showPlayForLive();
                            } else {
                                timerTextView.setText("Something went wrong please visit this page again!");
                            }
                        }, null, true, false);*/
                }
            }
        }.start();
    }

    private void updateVideoStreamingInfo(String videoId, ContentDatum datum,
                                          final ImageButton imagePlay, TextView timerTextView) {
        String url = appCMSPresenter.getStreamingInfoURL(videoId);

        GetAppCMSStreamingInfoAsyncTask.Params param = new GetAppCMSStreamingInfoAsyncTask.Params.Builder()
                .url(url)
                .authToken(appCMSPresenter.getAuthToken())
                .xApiKey(appCMSPresenter.getApiKey())
                .build();

        new GetAppCMSStreamingInfoAsyncTask(appCMSPresenter.getAppCMSStreamingInfoCall(), appCMSStreamingInfo -> {
            if (appCMSStreamingInfo != null) {
                datum.setStreamingInfo(appCMSStreamingInfo.getStreamingInfo());
                showPlayForLive(imagePlay, timerTextView);
            } else {
//                appCMSPresenter.showToast("Currently streaming info is not available for Live video. ", Toast.LENGTH_SHORT);
                updateVideoStreamingInfo(videoId, datum, imagePlay, timerTextView);
                //appCMSPresenter.showDialog(AppCMSPresenter.DialogType.STREAMING_INFO_MISSING, null, false, null, null);
                return;
            }
        }).execute(param);
    }

    private void showPlayForLive(final ImageButton imagePlay, TextView timerTextView) {
        imagePlay.setVisibility(VISIBLE);
        timerTextView.setVisibility(INVISIBLE);
    }


    private synchronized void setViewPotion(Context context, ConstraintSet set, Component
            component, View view) {

        int constraint_MarginRight = BaseView.getConstraintMarginRight(context, component.getLayout(), 0);
        int constraint_MarginLeft = BaseView.getConstraintMarginLeft(context, component.getLayout(), 0);
        int constraint_MarginTop = BaseView.getConstraintMarginTop(context, component.getLayout(), 0);
        int constraint_MarginBottom = BaseView.getConstraintMarginBottom(context, component.getLayout(), 0);

        String topToBottomOf = BaseView.getTopToBottomOf(context, component.getLayout(), null);
        String topToTopOf = BaseView.getTopToTopOf(context, component.getLayout(), null);
        String bottomToBottomOf = BaseView.getBottomToBottomOf(context, component.getLayout(), null);
        String bottomToTopOf = BaseView.getBottomToTopOf(context, component.getLayout(), null);
        String rightToRightOf = BaseView.getRightToRightOf(context, component.getLayout(), null);
        String rightToLeftOf = BaseView.getRightToLeftOf(context, component.getLayout(), null);
        String leftToLeftOf = BaseView.getLeftToLeftOf(context, component.getLayout(), null);
        String leftToRightOf = BaseView.getLeftToRightOf(context, component.getLayout(), null);
        String baselineToBaselineOf = BaseView.getBaselineToBaselineOf(context, component.getLayout(), null);
        String startToEndOf = BaseView.getStartToEndOf(context, component.getLayout(), null);
        String startToStartOf = BaseView.getStartToStartOf(context, component.getLayout(), null);
        String endToStartOf = BaseView.getEndToStartOf(context, component.getLayout(), null);
        String endToEndOf = BaseView.getEndToEndOf(context, component.getLayout(), null);
        float guideLinePositionPercent = BaseView.getGuideLinePositionPercent(context, component.getLayout(), 0f);

        int guideline = BaseView.getGuidline(context, component.getLayout(), -1);


        if (guideline != -1) {
            set.create(view.getId(), guideline);
            set.setGuidelinePercent(view.getId(), guideLinePositionPercent);
        }
        if (topToBottomOf != null && !TextUtils.isEmpty(topToBottomOf)) {
            set.connect(view.getId(), ConstraintSet.TOP, IdUtils.getID(topToBottomOf), ConstraintSet.BOTTOM, constraint_MarginTop);
        }
        if (topToTopOf != null && !TextUtils.isEmpty(topToTopOf)) {
            set.connect(view.getId(), ConstraintSet.TOP, IdUtils.getID(topToTopOf), ConstraintSet.TOP, constraint_MarginTop);
        }
        if (bottomToBottomOf != null && !TextUtils.isEmpty(bottomToBottomOf)) {
            set.connect(view.getId(), ConstraintSet.BOTTOM, IdUtils.getID(bottomToBottomOf), ConstraintSet.BOTTOM, constraint_MarginBottom);
        }
        if (bottomToTopOf != null && !TextUtils.isEmpty(bottomToTopOf)) {
            set.connect(view.getId(), ConstraintSet.BOTTOM, IdUtils.getID(bottomToTopOf), ConstraintSet.TOP, constraint_MarginBottom);
        }
        if (rightToRightOf != null && !TextUtils.isEmpty(rightToRightOf)) {
            set.connect(view.getId(), ConstraintSet.END, IdUtils.getID(rightToRightOf), ConstraintSet.END, constraint_MarginRight);
        }
        if (rightToLeftOf != null && !TextUtils.isEmpty(rightToLeftOf)) {
            set.connect(view.getId(), ConstraintSet.END, IdUtils.getID(rightToLeftOf), ConstraintSet.START, constraint_MarginRight);
        }
        if (leftToLeftOf != null && !TextUtils.isEmpty(leftToLeftOf)) {
            set.connect(view.getId(), ConstraintSet.START, IdUtils.getID(leftToLeftOf), ConstraintSet.START, constraint_MarginLeft);
        }
        if (leftToRightOf != null && !TextUtils.isEmpty(leftToRightOf)) {
            set.connect(view.getId(), ConstraintSet.START, IdUtils.getID(leftToRightOf), ConstraintSet.END, constraint_MarginLeft);
        }
        if (baselineToBaselineOf != null && !TextUtils.isEmpty(baselineToBaselineOf)) {
            set.connect(view.getId(), ConstraintSet.BASELINE, IdUtils.getID(baselineToBaselineOf), ConstraintSet.BASELINE, 10);
        }
        if (startToEndOf != null && !TextUtils.isEmpty(startToEndOf)) {
            set.connect(view.getId(), ConstraintSet.START, IdUtils.getID(startToEndOf), ConstraintSet.END, constraint_MarginLeft);
        }
        if (startToStartOf != null && !TextUtils.isEmpty(startToStartOf)) {
            set.connect(view.getId(), ConstraintSet.START, IdUtils.getID(startToStartOf), ConstraintSet.START, constraint_MarginLeft);
        }
        if (endToStartOf != null && !TextUtils.isEmpty(endToStartOf)) {
            set.connect(view.getId(), ConstraintSet.END, IdUtils.getID(endToStartOf), ConstraintSet.START, constraint_MarginRight);
        }
        if (endToEndOf != null && !TextUtils.isEmpty(endToEndOf)) {
            set.connect(view.getId(), ConstraintSet.END, IdUtils.getID(endToEndOf), ConstraintSet.END, constraint_MarginRight);
        }
    }


    private PopupWindow createPopupWindowForClassFormat(Context context, ConstraintLayout parent, View actionView, ContentDatum data) {
        PopupWindow popUp;
        RelativeLayout mlayout;
        RelativeLayout.LayoutParams lpTv, lpButton;
        TextView tv;
        ImageButton imageButton = new ImageButton(context);
        imageButton.setId(R.id.popupCloseButton);
        imageButton.setImageResource(R.drawable.crossicon);
        imageButton.getBackground().setTint(appCMSPresenter.getGeneralBackgroundColor());
        imageButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
        imageButton.setBackgroundColor(context.getResources().getColor(R.color.transparentColor));


        popUp = new PopupWindow(context);
        popUp.setOutsideTouchable(true);
        popUp.setFocusable(true);

        mlayout = new RelativeLayout(context);
        mlayout.setBackgroundColor(appCMSPresenter.getGeneralTextColor());

        tv = new TextView(context);
        tv.setId(R.id.popupTextView);
        tv.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));


        imageButton.setOnClickListener(v -> popUp.dismiss());

        lpButton = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpButton.setMargins(0, 50, 30, 0);
        lpButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpButton.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        lpTv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpTv.setMargins(30, 50, 0, 0);
        lpTv.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpTv.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lpTv.addRule(RelativeLayout.LEFT_OF, R.id.popupCloseButton);
        ((TextView) actionView).setTextColor(Color.BLACK);


        String str = context.getString(R.string.view_all_detail);
        if (data.getGist() != null
                && data.getGist().getLongDescription() != null) {
            str = data.getGist().getLongDescription();

            actionView.setOnClickListener(v -> {

                View view = appCMSPresenter.getCurrentActivity().findViewById(R.id.fullTopSeparatorView);
                View viewOffset = appCMSPresenter.getCurrentActivity().findViewById(R.id.difficultyLabel);
                int[] location = new int[2];
                viewOffset.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                //popUp.showAtLocation(mlayout, Gravity.RIGHT | Gravity.TOP, 10, 10);
                //popUp.showAsDropDown(view,x,y,Gravity.RIGHT | Gravity.TOP);
                // popUp.showAsDropDown(viewOffset, x, y);
                popUp.showAtLocation(viewOffset, Gravity.RIGHT | Gravity.TOP, 0, 50);
                /// popUp.update(50, 50, 300, 900);

            });
        }

//        String htmlStyleRegex = "<style([\\s\\S]+?)</style>";
//        ((TextView) componentViewResult.componentView).setText(Html.fromHtml(str.replaceAll(htmlStyleRegex, "")));

        tv.setText(str);
        tv.setLayoutParams(lpTv);

        imageButton.setLayoutParams(lpButton);

        mlayout.addView(tv, lpTv);
        mlayout.addView(imageButton, lpButton);
        popUp.setContentView(mlayout);
        popUp.setOutsideTouchable(true);
        popUp.setFocusable(true);

        return popUp;
    }

    private String brandTitle = "";
    private String brandImage1x1 = "";
    private String ratingVal = "";
    private String difficultyVals = "";
    private String musicTypeVal = "";
    private String intensityVals = "";
    private String bodyFocusVals = "";
    private boolean isExplicitLanguage;

    public void clearText() {
        brandTitle = "";
        brandImage1x1 = "";
        ratingVal = "";
        difficultyVals = "";
        musicTypeVal = "";
        intensityVals = "";
        bodyFocusVals = "";
        isExplicitLanguage = false;
    }

    private void getTagInfo(ContentDatum data) {

        if (data != null && data.getTags() != null) {
            List<Tag> tagsR = data.getTags();
            for (int i = 0; i < tagsR.size(); i++) {
                if (tagsR.get(i).getTagType() != null) {
                    if (tagsR.get(i).getTagType().equals("brand")) {
                        brandTitle = tagsR.get(i).getTitle();
                        if (tagsR.get(i).getImages() != null
                                && tagsR.get(i).getImages().get_1x1() != null) {
                            brandImage1x1 = tagsR.get(i).getImages().get_1x1().getUrl();
                        } else if (tagsR.get(i).getImages() != null
                                && tagsR.get(i).getImages().get_16x9() != null) {
                            brandImage1x1 = tagsR.get(i).getImages().get_16x9().getUrl();
                        }

                    } else if (tagsR.get(i).getTagType().equals("difficulty")) {
                        difficultyVals = setTagsValues(difficultyVals, tagsR.get(i).getTitle());
                    } else if (tagsR.get(i).getTagType().equals("bodyFocus")) {
                        bodyFocusVals = setTagsValues(bodyFocusVals, tagsR.get(i).getTitle());
                    } else if (tagsR.get(i).getTagType().equals("intensity")) {
                        intensityVals = setTagsValues(intensityVals, tagsR.get(i).getTitle());
                    } else if (tagsR.get(i).getTagType().equals("rating")) {
                        ratingVal = setTagsValues(ratingVal, tagsR.get(i).getTitle());
                    } else if (tagsR.get(i).getTagType().equals("musicType")) {
                        musicTypeVal = setTagsValues(musicTypeVal, tagsR.get(i).getTitle());
                    } else if (tagsR.get(i).getTagType().equals("language")) {
                        isExplicitLanguage = tagsR.get(i).getTitle().equalsIgnoreCase("Explicit");
                    }
                }
            }
        }
    }

    private String setTagsValues(String valContainer, String newVal) {
        if (newVal != null && !TextUtils.isEmpty(newVal)
                && !valContainer.contains(newVal)) {
            if (TextUtils.isEmpty(valContainer)) {
                valContainer = newVal;
            } else {
                valContainer = valContainer + ", " + newVal;
            }
        }
        return valContainer;
    }

    public String getBrand(ContentDatum data) {
        String brand = "";
        if (data != null && data.getTags() != null) {
            List<Tag> tagsR = data.getTags();
            for (int i = 0; i < tagsR.size(); i++) {
                if (tagsR.get(i).getTagType() != null) {
                    if (tagsR.get(i).getTagType().equals("brand")) {
                        brand = tagsR.get(i).getTitle();
                        break;
                    }
                }
            }
        }
        return brand;
    }

    private void setTextGravity(TextView textView, AppCMSUIKeyType gravity) {
        if (gravity == PAGE_TEXTALIGNMENT_CENTER_KEY)
            textView.setGravity(Gravity.CENTER);
        if (gravity == PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY)
            textView.setGravity(Gravity.CENTER_VERTICAL);
        if (gravity == PAGE_TEXTALIGNMENT_LEFT_KEY || gravity == VIEW_GRAVITY_START)
            textView.setGravity(Gravity.START);
        if (gravity == PAGE_TEXTALIGNMENT_RIGHT_KEY)
            textView.setGravity(Gravity.END);

    }

    public void applyBorderToComponent(Context context, View view, Component component, int forcedColor) {
        if (component.getBorderWidth() != 0 && component.getBorderColor() != null) {
            if (component.getBorderWidth() > 0 && !TextUtils.isEmpty(component.getBorderColor())) {
                GradientDrawable viewBorder = new GradientDrawable();
                viewBorder.setShape(GradientDrawable.RECTANGLE);
                if (forcedColor == -1) {
                    viewBorder.setStroke(component.getBorderWidth(),
                            Color.parseColor(CommonUtils.getColor(context, component.getBorderColor())));
                } else {
                    viewBorder.setStroke(component.getBorderWidth(), forcedColor);
                }
                viewBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
                view.setBackground(viewBorder);
            }
        }
    }

    public void applyBorderToAdapterView(Context context, View view, int forcedColor) {
        GradientDrawable viewBorder = new GradientDrawable();
        viewBorder.setShape(GradientDrawable.RECTANGLE);
        if (forcedColor == -1) {
            viewBorder.setStroke(8, ContextCompat.getColor(context, R.color.googleRed));
        } else {
            viewBorder.setStroke(8, forcedColor);
        }
        viewBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
        view.setBackground(viewBorder);

    }

    private Drawable resizePlaceholder(Context context, int placeholder, int width, int height) {
        Drawable image = ResourcesCompat.getDrawable(context.getResources(), placeholder, null);
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
        return d;
    }

    public ViewCreator.ComponentViewResult getComponentViewResult() {
        return componentViewResult;
    }

    public static String searchQueryText = "";

    public static void setSearchText(String queryText) {
        searchQueryText = queryText;
    }

    private void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
            }
        }
    }


    private static void focusAndShowKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public Module getModuleApiData() {
        return moduleAPIPub;
    }

    public void playVideo(CustomVideoPlayerView view, Boolean trailerAvalible, String episodePromoID) {
        ContentDatum data = appCMSPresenter.getshowdetailsContenData();
        if (data != null && data.getGist() != null) {
            //    if(appCMSPresenter.getseriesID()==null)
            data.setModuleApi(moduleAPIPub);
            appCMSPresenter.setShowDetailsGist(data.getGist());
            appCMSPresenter.setPlayshareControl(true);
        }
        appCMSPresenter.setEpisodeId(null);
        appCMSPresenter.setEpisodeTrailerID(null);
        appCMSPresenter.setEpisodePromoID(null);
        if (data != null) {
            if (thumbnailImage.getVisibility() == VISIBLE) {
                appCMSPresenter.getTrailerPlayerView().setVisibility(VISIBLE);
                thumbnailImage.setVisibility(INVISIBLE);
            }
            appCMSPresenter.dismissPopupWindowPlayer(true);
            data.setModuleApi(moduleAPIPub);
            appCMSPresenter.getTrailerPlayerView().setVideoContentData(data);
            ((CustomVideoPlayerView) view).setUseAdUrl(false);
            if ((trailerAvalible) && ((episodePromoID == null) || (episodePromoID.isEmpty()))) {
                ((CustomVideoPlayerView) view).setUseAdUrl(true);
            }
            appCMSPresenter.getTrailerPlayerView().setVideoUri(data.getGist().getId(),
                    appCMSPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, data);
            appCMSPresenter.getTrailerPlayerView().releasePreviousAdsPlayer();
            appCMSPresenter.getTrailerPlayerView().enableController();
            appCMSPresenter.getTrailerPlayerView().setUseController(true);
            appCMSPresenter.getTrailerPlayerView().setEpisodePlay(true);

        }

    }

    public void openPromo(View view, ImageView thumbnailImage, String trailerId) {

    }

    /////////////////////// OFFLINE DRM ////////////////////////////////////////////////////////////////////
    public static class OfflineVideoStatusHandler {
        public int getOfflineVideoDownloadStatus() {
            return offlineVideoDownloadStatus;
        }

        public void setOfflineVideoDownloadStatus(int offlineVideoDownloadStatus) {
            this.offlineVideoDownloadStatus = offlineVideoDownloadStatus;
        }

        int offlineVideoDownloadStatus;
        String videoId;
        Download downloadObject;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public Download getDownloadObject() {
            return downloadObject;
        }

        public void setDownloadObject(Download downloadObject) {
            this.downloadObject = downloadObject;
        }
    }

    private CustomVideoPlayerView videoPlayerView;
    private ViewCreator.VideoPlayerContent videoPlayerContent;

    public CustomVideoPlayerView playerView(Context context, String videoId, String key, AppCMSPresenter appCmsPresenter, Module moduleAPI) {
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
        videoPlayerView = new CustomVideoPlayerView(context, appCmsPresenter, null, null);


        if (videoId != null) {
            videoPlayerView.setVideoUri(videoId, appCmsPresenter.getLocalisedStrings().getLoadingVideoText(), false, false, moduleAPI.getContentData().get(0));
            videoPlayerView.setVideoTitle(title);
            appCmsPresenter.setVideoPlayerViewCache(key, videoPlayerView);
            videoPlayerContent = videoPlayerView.getVideoPlayerContent();
        }
        return videoPlayerView;
    }

    private void updateDownloadText(ContentDatum contentDatum, TextView downloadText, View view) {
        appCMSPresenter.getUserVideoDownloadStatus(contentDatum.getGist().getId(),
                videoDownloadStatus -> {
                    boolean isDownloaded = false, isPending = false;
                    if (videoDownloadStatus != null) {
                        if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_COMPLETED ||
                                videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_SUCCESSFUL) {
                            isDownloaded = true;
                            downloadText.setText(appCMSPresenter.getLocalisedStrings().getDownloadedLabelText());
                            appCMSPresenter.setUserAbleToDownload(isDownloaded);
                        }
                        if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_RUNNING ||
                                videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PAUSED ||
                                videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING) {
                            isPending = true;
                            appCMSPresenter.setUserAbleToDownload(isDownloaded);
                            downloadText.setText(appCMSPresenter.getLocalisedStrings().getDownloadingLabelText());
                        }
                        if (!isDownloaded && !isPending) {
                            appCMSPresenter.setUserAbleToDownload(isDownloaded);
                        } else {
                            ((TextView) view).setText(isDownloaded ? appCMSPresenter.getLocalisedStrings().getDownloadedLabelText() : appCMSPresenter.getLocalisedStrings().getDownloadingLabelText());
                            view.setActivated(false);
                            appCMSPresenter.setUserAbleToDownload(isDownloaded);
                            appCMSPresenter.setDownloadStatus(isDownloaded ? appCMSPresenter.getLocalisedStrings().getDownloadedLabelText() : appCMSPresenter.getLocalisedStrings().getDownloadingLabelText());
                        }
                    } else {
                        ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getDownloadLowerCaseText());
                        appCMSPresenter.setUserAbleToDownload(false);
                    }
                },
                appCMSPresenter.getAppPreference().getLoggedInUser());

    }
}

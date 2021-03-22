package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.util.Strings;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.BillingHelper;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.plans.SubscriptionMetaDataView;
import com.viewlift.views.customviews.plans.ViewPlansMetaDataView;
import com.viewlift.views.utilities.ImageLoader;
import com.viewlift.views.utilities.ImageUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AC_ROSTER_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AC_TEAM_SCHEDULE_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_DESCRIPTION;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_SUMMARY_TEXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_TITLE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_TRAY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AUDIO_DOWNLOAD_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AUDIO_DURATION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AUDIO_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_INFO_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_DELETE_DOWNLOAD_VIDEO_SIZE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_DOWNLOAD_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_DOWNLOAD_DURATION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EMPTY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EPISODE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EXPIRE_TIME_TITLE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_FIGHTER_LABEL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_FIGHTER_SELECTOR_ARROW_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_FIGHTER_SELECTOR_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GAME_DATE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GAME_TICKETS_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GAME_TIME_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_OPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_THUMBNAIL_INFO;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HEIGHT_LABEL_TEXT;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HEIGHT_VALUE_TEXT;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HISTORY_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HISTORY_DURATION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HISTORY_WATCHED_TIME_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LABEL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_GALLERY_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_PLAYER_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_TEAM_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_FEATURE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_FEATURE_TEXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_FEATURE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_META_DATA_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_PRICEINFO_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_PURCHASE_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAYER_RECORD_LABEL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAYER_SCORE_TEXT;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAYER_TEAM_TITLE_TXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAYLIST_AUDIO_ARTIST_TITLE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAYLIST_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAY_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PROGRESS_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_RECORD_TYPE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SINGLE_PLAN_SUBSCRIBE_TEXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIBE_BTN_FOR_PLAN_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_BADGE_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_READ_MORE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TRAY_SCHEDULE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_DOWNLOAD_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WATCHLIST_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_KEY_BG;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_UNIT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WEIGHT_LABEL_TEXT;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WEIGHT_VALUE_TEXT;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_03;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_02;
import static com.viewlift.views.customviews.ViewCreator.setTypeFace;

@SuppressLint("ViewConstructor")
public class CollectionGridItemView extends BaseView {
    private static final String TAG = "CollectionItemView";

    private final Layout parentLayout;
    private final boolean useParentLayout;
    private final Component component;
    private final String moduleId;
    protected int defaultWidth;
    protected int defaultHeight;
    AppCMSUIKeyType viewTypeKey;
    private List<ItemContainer> childItems;
    private List<View> viewsToUpdateOnClickEvent;
    private boolean selectable;
    private boolean createMultipleContainersForChildren;
    private boolean createRoundedCorners;

    @Inject
    public CollectionGridItemView(Context context,
                                  Layout parentLayout,
                                  boolean useParentLayout,
                                  Component component,
                                  String moduleId,
                                  int defaultWidth,
                                  int defaultHeight,
                                  boolean createMultipleContainersForChildren,
                                  boolean createRoundedCorners,
                                  AppCMSUIKeyType viewTypeKey) {
        super(context);
        this.parentLayout = parentLayout;
        this.useParentLayout = useParentLayout;
        this.component = component;
        this.moduleId = moduleId;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.viewsToUpdateOnClickEvent = new ArrayList<>();
        this.createMultipleContainersForChildren = createMultipleContainersForChildren;
        this.createRoundedCorners = createRoundedCorners;
        this.viewTypeKey = viewTypeKey;
        init();
    }

    @Override
    public void init() {
        int width = (int) getGridWidth(getContext(),
                component.getLayout(),
                (int) getViewWidth(getContext(),
                        component.getLayout(),
                        defaultWidth));
        int height = (int) getGridHeight(getContext(),
                component.getLayout(),
                (int) getViewHeight(getContext(),
                        component.getLayout(),
                        defaultHeight));

        FrameLayout.LayoutParams layoutParams;
        int paddingHorizontal = 0;
        int paddingVertical = 0;
        if (component.getStyles() != null) {
            paddingHorizontal = (int) convertHorizontalValue(getContext(), component.getStyles().getPadding());
        } else if (getTrayPadding(getContext(), component.getLayout()) != -1.0f) {
            int trayPadding = (int) getTrayPadding(getContext(), component.getLayout());
            paddingHorizontal = (int) convertHorizontalValue(getContext(), trayPadding);
        } else if (getTrayPaddingVertical(getContext(), component.getLayout()) != -1.0f) {
            int trayPaddingVertical = (int) getTrayPaddingVertical(getContext(), component.getLayout());
            paddingVertical = (int) convertVerticalValue(getContext(), trayPaddingVertical);
        }
        int horizontalMargin = paddingHorizontal;
        int verticalMargin = paddingVertical;
        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(width, height);
        marginLayoutParams.setMargins(horizontalMargin,
                verticalMargin,
                horizontalMargin,
                verticalMargin);
        layoutParams = new FrameLayout.LayoutParams(marginLayoutParams);
        setLayoutParams(layoutParams);
        childItems = new ArrayList<>();
        if (component.getComponents() != null) {
            initializeComponentHasViewList(component.getComponents().size());
        }
    }

    @Override
    protected Component getChildComponent(int index) {
        if (component.getComponents() != null &&
                0 <= index &&
                index < component.getComponents().size()) {
            return component.getComponents().get(index);
        }
        return null;
    }

    @Override
    protected Layout getLayout() {
        return component.getLayout();
    }

    @Override
    protected ViewGroup createChildrenContainer() {
        if (createMultipleContainersForChildren && BaseView.isTablet(getContext()) && BaseView.isLandscape(getContext())) {
            if (component != null &&
                    component.getView() != null &&
                    component.getView().equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_event_carousel_module_key))) {
                childrenContainer = new CardView(getContext());
                CardView.LayoutParams childContainerLayoutParams =
                        new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                childrenContainer.setLayoutParams(childContainerLayoutParams);

                if (createRoundedCorners) {
                    ((CardView) childrenContainer).setRadius(14);
                    setBackgroundResource(android.R.color.transparent);
                    if (!component.getAction().equalsIgnoreCase("purchasePlan")) {
                        childrenContainer.setBackgroundResource(android.R.color.transparent);
                    }
                } else {
                    childrenContainer.setBackgroundResource(android.R.color.transparent);
                }
            } else {
                childrenContainer = new LinearLayout(getContext());
                ((LinearLayout) childrenContainer).setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams childContainerLayoutParams =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                childrenContainer.setLayoutParams(childContainerLayoutParams);
                CardView imageChildView = new CardView(getContext());
                LinearLayout.LayoutParams imageChildViewLayoutParams =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                imageChildViewLayoutParams.weight = 2;
                imageChildView.setLayoutParams(imageChildViewLayoutParams);
                imageChildView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                childrenContainer.addView(imageChildView);
                CardView detailsChildView = new CardView(getContext());
                LinearLayout.LayoutParams detailsChildViewLayoutParams =
                        new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                detailsChildViewLayoutParams.weight = 1;
                detailsChildView.setLayoutParams(detailsChildViewLayoutParams);
                detailsChildView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                childrenContainer.addView(detailsChildView);
            }
        } else {
            childrenContainer = new CardView(getContext());
            CardView.LayoutParams childContainerLayoutParams =
                    new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
            childrenContainer.setLayoutParams(childContainerLayoutParams);

            if (createRoundedCorners) {
                int cornerRadius = 34;
                if (BaseView.isTablet(getContext()))
                    cornerRadius = 24;
                ((CardView) childrenContainer).setRadius(cornerRadius);
                setBackgroundResource(android.R.color.transparent);
                if (component.getAction() != null && !component.getAction().equalsIgnoreCase("purchasePlan")) {
                    childrenContainer.setBackgroundResource(android.R.color.transparent);
                }
            } else {
                childrenContainer.setBackgroundResource(android.R.color.transparent);
            }
        }
        addView(childrenContainer);
        return childrenContainer;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void addChild(ItemContainer itemContainer) {
        if (childrenContainer == null) {
            createChildrenContainer();
        }
        childItems.add(itemContainer);

        if (createMultipleContainersForChildren && BaseView.isTablet(getContext()) && BaseView.isLandscape(getContext())) {
            if (component != null &&
                    component.getView() != null &&
                    component.getView().equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_event_carousel_module_key))) {
                childrenContainer.addView(itemContainer.childView);
            } else if (getContext().getString(R.string.app_cms_page_carousel_image_key).equalsIgnoreCase(itemContainer.component.getKey())) {
                ((ViewGroup) childrenContainer.getChildAt(0)).addView(itemContainer.childView);
            } else {
                ((ViewGroup) childrenContainer.getChildAt(1)).addView(itemContainer.childView);
            }
        } else {
            childrenContainer.addView(itemContainer.childView);
        }
    }

    public View getChild(int index) {
        if (0 <= index && index < childItems.size()) {
            return childItems.get(index).childView;
        }
        return null;
    }

    public int getNumberOfChildren() {
        return childItems.size();
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    /**
     * @param context
     * @param view
     * @param data
     * @param jsonValueKeyMap
     * @param onClickHandler
     * @param componentViewType
     * @param themeColor
     * @param appCMSPresenter
     * @param position
     */
    public void bindChild(Context context,
                          View view,
                          final ContentDatum data,
                          Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                          final OnClickHandler onClickHandler,
                          final String componentViewType,
                          int themeColor,
                          AppCMSPresenter appCMSPresenter, int position, Settings settings, String blockName, MetadataMap metadataMap) {

        final Component childComponent = matchComponentToView(view);

        AppCMSUIKeyType moduleType = jsonValueKeyMap.get(componentViewType);

        if (moduleType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }

        Map<String, ViewCreator.UpdateDownloadImageIconAction> updateDownloadImageIconActionMap =
                appCMSPresenter.getUpdateDownloadImageIconActionMap();

        if (childComponent != null) {
            if (data != null && onClickHandler != null) {
                view.setOnClickListener(v -> onClickHandler.click(CollectionGridItemView.this,
                        childComponent, data, position));
            }
            boolean bringToFront = true;
            AppCMSUIKeyType appCMSUIcomponentViewType = jsonValueKeyMap.get(componentViewType);
            AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
            AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
            AppCMSUIKeyType uiBlockName = null;
            if (blockName != null) {
                uiBlockName = jsonValueKeyMap.get(blockName);
            }
            if (uiBlockName == null) {
                uiBlockName = PAGE_EMPTY_KEY;
            }
            if (componentType == PAGE_IMAGE_KEY) {
                if (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                        componentKey == PAGE_CAROUSEL_IMAGE_KEY ||
                        componentKey == PAGE_VIDEO_IMAGE_KEY ||
                        componentKey == PAGE_PLAN_FEATURE_IMAGE_KEY ||
                        componentKey == PAGE_BADGE_IMAGE_KEY ||
                        componentKey == PAGE_PLAY_IMAGE_KEY ||
                        componentKey == PAGE_PHOTO_PLAYER_IMAGE ||
                        componentKey == PAGE_PHOTO_TEAM_IMAGE ||
                        componentKey == PAGE_THUMBNAIL_BADGE_IMAGE ||
                        componentKey == PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY ||
                        componentKey == PAGE_PHOTO_GALLERY_IMAGE_KEY) {
                    int placeholder = R.drawable.vid_image_placeholder_land;
                    int childViewWidth = (int) getViewWidth(getContext(),
                            childComponent.getLayout(),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    int childViewHeight = (int) getViewHeight(getContext(),
                            childComponent.getLayout(),
                            getViewHeight(getContext(), component.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (useParentLayout) {
                        childViewWidth = (int) getGridWidth(getContext(),
                                parentLayout,
                                (int) getViewWidth(getContext(),
                                        parentLayout,
                                        defaultWidth));
                        childViewHeight = (int) getGridHeight(getContext(),
                                parentLayout,
                                (int) getViewHeight(getContext(),
                                        parentLayout,
                                        defaultHeight));
                    }

                    if (childViewWidth < 0 &&
                            componentKey == PAGE_CAROUSEL_IMAGE_KEY) {
                        childViewWidth = (16 * childViewHeight) / 9;
                    }
                    if (0 < childViewWidth && 0 < childViewHeight && appCMSUIcomponentViewType != PAGE_GRID_MODULE_KEY && appCMSUIcomponentViewType != PAGE_PLAYLIST_MODULE_KEY) {
                        if (childViewWidth < childViewHeight) {
                            childViewHeight = (int) ((float) childViewWidth * 4.0f / 3.0f);
                        } else {
                            childViewHeight = (int) ((float) childViewWidth * 9.0f / 16.0f);
                        }
                    }
                    if (0 < childViewWidth && 0 < childViewHeight && (appCMSUIcomponentViewType == PAGE_PLAYLIST_MODULE_KEY ||
                            (blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_audioTray_01))))) {
                        if (childViewWidth < childViewHeight) {
                            childViewHeight = childViewWidth;
                        } else {
                            childViewWidth = childViewHeight;
                        }
                    }

                    if (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                            componentKey == PAGE_CAROUSEL_IMAGE_KEY ||
                            componentKey == PAGE_PLAN_FEATURE_IMAGE_KEY ||
                            componentKey == PAGE_VIDEO_IMAGE_KEY) {
                        if (childViewHeight > childViewWidth) {
                            placeholder = R.drawable.vid_image_placeholder_port;
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(view).load(R.drawable.vid_image_placeholder_port);
                        } else {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            try {
                                if (appCMSUIcomponentViewType == PAGE_AUDIO_TRAY_MODULE_KEY ||
                                        (data != null && data.getGist() != null &&
                                                data.getGist().getContentType() != null &&
                                                data.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_audio)))) {
                                    Glide.with(view).load(R.drawable.vid_image_placeholder_square);
                                    placeholder = R.drawable.vid_image_placeholder_square;
                                } else {
                                    Glide.with(view).load(R.drawable.vid_image_placeholder_16x9);
                                    placeholder = R.drawable.vid_image_placeholder_16x9;
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }


                        Glide.with(context.getApplicationContext())
                                .load(placeholder)
                                .into(((ImageView) view));
                    }

                    if (data != null && data.getGist() != null &&
                            data.getGist().getContentType() != null &&
                            data.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_audio))
                            && data.getGist().getPosterImageUrl() != null &&
                            (appCMSUIcomponentViewType != PAGE_PLAYLIST_MODULE_KEY ||
                                    appCMSUIcomponentViewType != PAGE_VIDEO_PLAYLIST_MODULE_KEY ||
                                    appCMSUIcomponentViewType != PAGE_DOWNLOAD_01_MODULE_KEY) &&
                            (appCMSPresenter.isVideoDownloaded(data.getGist().getId()) || appCMSPresenter.isVideoDownloading(data.getGist().getId()))) {
                        int size = childViewWidth;
                        if (data.getGist() != null &&
                                data.getGist().getPosterImageUrl() != null && (componentKey == PAGE_THUMBNAIL_IMAGE_KEY)) {
                            String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getPosterImageUrl(), size, size);
                            /*
                            if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                                if (data.getGist().getPosterImageUrl() != null) {
                                    imageUrl = data.getGist().getPosterImageUrl();
                                }
                            }*/
                            RequestOptions requestOptions = new RequestOptions()
                                    .override(size, size).placeholder(placeholder)
                                    .fitCenter();
                            Glide.with(context.getApplicationContext())
                                    .load(imageUrl)
                                    .apply(requestOptions)
                                    .into(((ImageView) view));
                        } else {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(view).load(R.drawable.vid_image_placeholder_square);
                        }
                    } else if (componentKey == PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY) {
                        int size = childViewWidth;
                        /*if (childViewHeight< childViewWidth ) {
                            size = childViewHeight;
                        }*/
                        int horizontalMargin = 0;
                        horizontalMargin = (int) getHorizontalMargin(getContext(), childComponent.getLayout());
                        int verticalMargin = (int) getVerticalMargin(getContext(), parentLayout, size, 0);
                        if (verticalMargin < 0) {
                            verticalMargin = (int) convertVerticalValue(getContext(), getYAxis(getContext(), getLayout(), 0));
                        }
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                        LayoutParams llParams = new LayoutParams(size, size);
                        llParams.setMargins(horizontalMargin,
                                verticalMargin,
                                horizontalMargin,
                                verticalMargin);
                        view.setLayoutParams(llParams);
                        String imageUrl = "";

                        if (data.getGist() != null &&
                                data.getGist().getVideoImageUrl() != null) {
                            if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                                if (data.getGist().getVideoImageUrl() != null) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(), size, size);
                                }
                            }

                            if (data.getGist().getVideoImageUrl().contains("http://") || data.getGist().getVideoImageUrl().contains("https://")) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getVideoImageUrl(), size, size);
                            } else {
                                int resID = appCMSPresenter.getCurrentActivity().getResources().getIdentifier(data.getGist().getVideoImageUrl().replace("-", "_"), "drawable", appCMSPresenter.getCurrentActivity().getPackageName());
                                if (resID != 0) {
                                    ((ImageView) view).setImageDrawable(ContextCompat.getDrawable(context, resID));
                                    view.setVisibility(View.VISIBLE);
                                } else
                                    ((ImageView) view).setImageDrawable(ContextCompat.getDrawable(context, placeholder));
                            }

                            if (!TextUtils.isEmpty(imageUrl)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(size, size).placeholder(placeholder)
                                        .fitCenter();
                                Glide.with(context.getApplicationContext())
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into(((ImageView) view));
                            }
                        } else {

//                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
//                            RequestOptions requestOptions = new RequestOptions()
//                                    .override(size, size).placeholder(placeholder)
//                                    .fitCenter();
////                            RequestOptions requestOptions = new RequestOptions().placeholder(placeholder);
//                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START) && context != null && appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null && !appCMSPresenter.getCurrentActivity().isFinishing()) {
//
//
//                                Glide.with(context.getApplicationContext())
//                                        .load(imageUrl)
//                                        .apply(requestOptions)
////                                        .override(size,size)
//                                        .into(((ImageView) view));
//                            }
//                            ((ImageView) view).bringToFront();
                        }
                    } else if (data != null &&
                            data.getGist() != null &&
                            componentKey == PAGE_CAROUSEL_IMAGE_KEY) {
                        System.out.println("image dimen3- width" + childViewHeight + " width" + childViewWidth);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                        bringToFront = false;
                        int deviceWidth = getContext().getResources().getDisplayMetrics().widthPixels;
                        String imageUrl = "";
                        if (data.getGist() != null &&
                                data.getGist().getContentType() != null &&
                                ((data.getGist().getContentType().toLowerCase().contains(context.getString(R.string.content_type_audio).toLowerCase())) ||
                                        (data.getGist().getContentType().toLowerCase().contains(context.getString(R.string.app_cms_article_key_type).toLowerCase())))
                                && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_16x9() != null) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_16x9(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (data.getGist() != null && data.getGist().getVideoImageUrl() != null) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getVideoImageUrl(),
                                    childViewWidth,
                                    childViewHeight);
//                            imageUrl = data.getGist().getImageGist().get_16x9();

                        } else if (data.getGist() != null && data.getGist().getImageGist() != null && data.getGist().getImageGist().get_16x9() != null) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_16x9(),
                                    childViewWidth,
                                    childViewHeight);
                        }

                        try {
                            final int imageWidth = childViewWidth;
                            final int imageHeight = childViewHeight;

                            if (!ImageUtils.loadImageWithLinearGradient((ImageView) view,
                                    imageUrl,
                                    imageWidth,
                                    imageHeight)) {

                                Transformation gradientTransform = new GradientTransformation(imageWidth,
                                        imageHeight,
                                        appCMSPresenter,
                                        imageUrl);

                                RequestOptions requestOptions = new RequestOptions()
                                        .transform(gradientTransform)
                                        .placeholder(placeholder).fitCenter()
                                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .override(imageWidth, imageHeight);
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            }
                        } catch (IllegalArgumentException e) {
                            //Log.e(TAG, "Failed to load image with Glide: " + e.toString());
                        }
                    } else if (data != null && data.getPlanImgUrl() != null &&
                            (componentKey == PAGE_PLAN_FEATURE_IMAGE_KEY)) {
                        bringToFront = false;
                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                data.getPlanImgUrl(),
                                childViewWidth,
                                childViewHeight);
                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                                ((ImageView) view).setAdjustViewBounds(true);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .into((ImageView) view);
                            } else {
                                view.setBackgroundResource(placeholder);
                            }
                        } catch (Exception e) {
                            //
                        }

                    }/*else if (settings != null && settings.getItems() != null &&
                            !TextUtils.isEmpty(settings.getItems().get(position).getImageUrl()) &&
                            (componentKey == PAGE_PLAN_FEATURE_IMAGE_KEY)) {
                        bringToFront = false;
                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                settings.getItems().get(position).getImageUrl(),
                                childViewWidth,
                                childViewHeight);

                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                                ((ImageView) view).setAdjustViewBounds(true);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .into((ImageView) view);
                            } else {
                                ((ImageView) view).setBackgroundResource(placeholder);
                            }
                        } catch (Exception e) {
                            //
                        }

                    } */ else if (childViewHeight > childViewWidth &&
                            childViewHeight > 0 &&
                            childViewWidth > 0 && data != null && data.getGist() != null && data.getGist().getPosterImageUrl() != null &&
                            !TextUtils.isEmpty(data.getGist().getPosterImageUrl()) &&
                            (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                                    componentKey == PAGE_VIDEO_IMAGE_KEY)) {
                        bringToFront = false;
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                data.getGist().getPosterImageUrl(),
                                childViewWidth,
                                childViewHeight);

//                        if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
//                            if (data.getGist().getVideoImageUrl() != null) {
//                                imageUrl = data.getGist().getVideoImageUrl();
//                            }
//                        }
                        try {
//                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START))
                            {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight).placeholder(placeholder)
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            } /*else {
                                ((ImageView) view).setBackgroundResource(placeholder);
                            }*/
                        } catch (Exception e) {
                            //
                        }
                    } else if (childViewHeight > 0 &&
                            childViewWidth > 0 && data != null && data.getGist() != null && data.getGist().getImages() != null &&
                            data.getGist().getImages().get_16x9Image() != null && data.getGist().getImages().get_16x9Image().getUrl() != null &&
                            !TextUtils.isEmpty(data.getGist().getImages().get_16x9Image().getUrl()) &&
                            (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                                    componentKey == PAGE_VIDEO_IMAGE_KEY)) {
                        bringToFront = false;
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                data.getGist().getImages().get_16x9Image().getUrl(),
                                childViewWidth,
                                childViewHeight);

                        if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                            if (data.getGist().getVideoImageUrl() != null) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getVideoImageUrl(),
                                        childViewWidth,
                                        childViewHeight);
                            }
                        }
                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight).placeholder(placeholder)
                                        .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            } else {
                                view.setBackgroundResource(placeholder);
                            }
                        } catch (Exception e) {
                            //
                        }
                    } else if (childViewHeight > 0 && childViewWidth > 0 &&
                            childViewHeight > childViewWidth && data != null && data.getGist() != null
                            && ((data.getGist().getImages() != null
                            && data.getGist().getImages().get_3x4Image() != null
                            && data.getGist().getImages().get_3x4Image().getUrl() != null
                            && !TextUtils.isEmpty(data.getGist().getImages().get_3x4Image().getUrl()))
                            || (data.getGist().getImageGist() != null
                            && data.getGist().getImageGist().get_3x4() != null
                            && data.getGist().getImageGist().get_3x4() != null
                            && !TextUtils.isEmpty(data.getGist().getImageGist().get_3x4())))
                            && componentKey == PAGE_THUMBNAIL_IMAGE_KEY
                    ) {
                        bringToFront = false;
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                (data.getGist().getImages() != null ?
                                        data.getGist().getImages().get_3x4Image().getUrl() : data.getGist().getImageGist().get_3x4()),
                                childViewWidth,
                                childViewHeight);

                        if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                            if (data.getGist().getVideoImageUrl() != null) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getVideoImageUrl(),
                                        childViewWidth,
                                        childViewHeight);
                            }
                        }
                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight).placeholder(placeholder)
                                        .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            } else {
                                view.setBackgroundResource(placeholder);
                            }
                        } catch (Exception e) {
                            //
                        }
                    } /*else if (childViewHeight > 0 &&
                            childViewWidth > 0 &&
                            data != null &&
                            data.getGist() != null &&
                            ((data.getGist().getVideoImageUrl() != null &&
                                    !TextUtils.isEmpty(data.getGist().getVideoImageUrl())) ||
                                    (data.getGist().getImageGist() != null &&
                                            data.getGist().getImageGist().get_16x9() != null &&
                                            !TextUtils.isEmpty(data.getGist().getImageGist().get_16x9()))) &&
                            (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                                    componentKey == PAGE_VIDEO_IMAGE_KEY) &&
                            appCMSUIcomponentViewType == PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY) {

                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

                        if (moduleType == PAGE_AC_TEAM_SCHEDULE_MODULE_KEY) {
                            bringToFront = true;
                        } else {
                            bringToFront = false;
                        }

                        String imageUrl = null;
                        imageUrl=data.getGist().getImageGist().get_16x9();

                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
//                                        .override(childViewWidth, childViewHeight)
                                        .placeholder(placeholder)
                                        .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);

//                                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                        } catch (Exception e) {
                            //
                        }
                    }*/ else if (childViewHeight > 0 &&
                            childViewWidth > 0 &&
                            childViewHeight == childViewWidth &&
                            data != null &&
                            data.getGist() != null &&
                            ((data.getGist().getVideoImageUrl() != null &&
                                    !TextUtils.isEmpty(data.getGist().getVideoImageUrl())) ||
                                    (data.getGist().getImageGist() != null &&
                                            data.getGist().getImageGist().get_1x1() != null &&
                                            !TextUtils.isEmpty(data.getGist().getImageGist().get_1x1()))) &&
                            (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                                    componentKey == PAGE_VIDEO_IMAGE_KEY)
                            && (appCMSUIcomponentViewType == PAGE_PLAYLIST_MODULE_KEY ||
                            blockName.equalsIgnoreCase(context.getString(R.string.ui_block_audioTray_01)))) {

                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

                        bringToFront = moduleType == PAGE_AC_TEAM_SCHEDULE_MODULE_KEY;

                        String imageUrl = null;
                        if (data.getGist().getVideoImageUrl() != null) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getVideoImageUrl(),
                                    childViewWidth,
                                    childViewHeight);
                            if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                                if (data.getGist().getVideoImageUrl().equalsIgnoreCase("file:///")) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getPosterImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                } else {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }
                            }
                        } else {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_1x1(),
                                    childViewWidth,
                                    childViewHeight);
                        }
                        //Log.d(TAG, "Loading image: " + imageUrl);
                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .placeholder(placeholder)
                                        .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);

                                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                        } catch (Exception e) {
                            //
                        }
                    } else if (childViewHeight > 0 &&
                            childViewWidth > 0 &&
                            data != null &&
                            data.getGist() != null &&
                            ((data.getGist().getVideoImageUrl() != null &&
                                    !TextUtils.isEmpty(data.getGist().getVideoImageUrl())) ||
                                    (data.getGist().getImageGist() != null &&
                                            data.getGist().getImageGist().get_16x9() != null &&
                                            !TextUtils.isEmpty(data.getGist().getImageGist().get_16x9()))) &&
                            (componentKey == PAGE_THUMBNAIL_IMAGE_KEY ||
                                    componentKey == PAGE_VIDEO_IMAGE_KEY)) {

                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

                        bringToFront = moduleType == PAGE_AC_TEAM_SCHEDULE_MODULE_KEY;

                        String imageUrl = null;
                        if (data.getGist().getVideoImageUrl() != null) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getVideoImageUrl(),
                                    childViewWidth,
                                    childViewHeight);
                            if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                                if (data.getGist().getVideoImageUrl().equalsIgnoreCase("file:///")) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getPosterImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                } else {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }
                            }
                        } else {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_16x9(),
                                    childViewWidth,
                                    childViewHeight);
                        }
                        //Log.d(TAG, "Loading image: " + imageUrl);
                        try {
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .placeholder(placeholder)
                                        .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);

                                ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                    /*else if (data != null && data.getGist() != null &&
                                data.getGist().getImageGist() != null &&
                            componentKey == PAGE_MYLIBRARY_01_MODULE_KEY &&
                            0 < childViewWidth &&
                            0 < childViewHeight) {
                        if (childViewWidth < childViewHeight &&
                                data.getGist().getImageGist().get_3x4() != null &&
                                data.getGist().getBadgeImages().get_3x4() != null &&
                                componentKey == PAGE_BADGE_IMAGE_KEY &&
                                0 < childViewWidth &&
                                0 < childViewHeight) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                            String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_3x4(),
                                    childViewWidth,
                                    childViewHeight);

                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .fitCenter()
                                        .placeholder(placeholder);
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            }
                        }
                        view.setVisibility(VISIBLE);
                        bringToFront = true;
                    }*/
                    else if (data != null && data.getGist() != null &&
                            data.getGist().getImageGist() != null &&
                            data.getGist().getBadgeImages() != null &&
                            componentKey == PAGE_BADGE_IMAGE_KEY &&
                            0 < childViewWidth &&
                            0 < childViewHeight) {
                        if (childViewWidth < childViewHeight &&
                                data.getGist().getImageGist().get_3x4() != null &&
                                data.getGist().getBadgeImages().get_3x4() != null &&
                                componentKey == PAGE_BADGE_IMAGE_KEY &&
                                0 < childViewWidth &&
                                0 < childViewHeight) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                            String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_3x4(),
                                    childViewWidth,
                                    childViewHeight);

                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .fitCenter()
                                        .placeholder(placeholder);
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            }
                        } else if (data.getGist().getImageGist().get_16x9() != null &&
                                data.getGist().getBadgeImages().get_16x9() != null) {
                            String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_16x9(),
                                    childViewWidth,
                                    childViewHeight);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

                            if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                                if (data.getGist().getVideoImageUrl() != null) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                }
                            }
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .override(childViewWidth, childViewHeight)
                                        .placeholder(R.drawable.vid_image_placeholder_16x9)
                                        .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            }
                        }
                        view.setVisibility(VISIBLE);
                        bringToFront = true;
                    } else if (componentKey == PAGE_BADGE_IMAGE_KEY) {
                        view.setVisibility(GONE);
                        bringToFront = false;
                    } else if (componentKey == PAGE_PHOTO_GALLERY_IMAGE_KEY) {

                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                data.getGist().getVideoImageUrl(),
                                childViewWidth,
                                childViewHeight);
                        ImageView imageView = (ImageView) view;
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        RequestOptions requestOptions = new RequestOptions()
                                .override(childViewWidth, childViewHeight)
                                .placeholder(placeholder);

                        Glide.with(context)
                                .load(imageUrl)
                                .apply(requestOptions)
                                .into(imageView);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                    } else if (componentKey == PAGE_PHOTO_PLAYER_IMAGE && moduleType == PAGE_AC_ROSTER_MODULE_KEY) {
                        String imageUrl = "";
                        if (data != null && data.getPlayersData() != null && data.getPlayersData().getData() != null) {
                            if (data.getPlayersData().getData().getImages() != null && data.getPlayersData().getData().getImages().get_3x4() != null) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getPlayersData().getData().getImages().get_3x4().getUrl(),
                                        childViewWidth,
                                        childViewHeight);
                            }
                        }
                        ImageView imageView = (ImageView) view;
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageView.setAdjustViewBounds(true);
                        RequestOptions requestOptions = new RequestOptions()
//                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)

//                                .override(childViewWidth, childViewHeight)
                                .placeholder(placeholder);

                        Glide.with(context)
                                .load(imageUrl)
                                .apply(requestOptions)

                                .into(imageView);
//                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                    } else if (componentKey == PAGE_PHOTO_TEAM_IMAGE && moduleType == PAGE_AC_ROSTER_MODULE_KEY) {
                        String imageUrl = "";
                        if (data != null && data.getPlayersData() != null && data.getPlayersData().getData() != null) {
                            if (data.getPlayersData().getData().getImages() != null && data.getPlayersData().getData().getImages().get_1x1() != null) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getPlayersData().getData().getImages().get_1x1().getUrl(),
                                        childViewWidth,
                                        childViewHeight);
                            }
                        }
                        ImageView imageView = (ImageView) view;
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        RequestOptions requestOptions = new RequestOptions()
                                .override(childViewWidth, childViewHeight)
                                .placeholder(placeholder);

                        Glide.with(context)
                                .load(imageUrl)
                                .apply(requestOptions)
                                .into(imageView);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                    } else if (data != null && data.getGist() != null && data.getGist().getLandscapeImageUrl() != null) {
                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                data.getGist().getLandscapeImageUrl(),
                                childViewWidth,
                                childViewHeight);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);

                        if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                            if (data.getGist().getVideoImageUrl() != null) {
                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getVideoImageUrl(),
                                        childViewWidth,
                                        childViewHeight);
                            }
                        }
                        if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START)) {
                            RequestOptions requestOptions = new RequestOptions()
                                    .override(childViewWidth, childViewHeight)
                                    .placeholder(R.drawable.vid_image_placeholder_16x9)
                                    .fitCenter();
//                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                            Glide.with(context)
                                    .load(imageUrl)
                                    .apply(requestOptions)
                                    .into((ImageView) view);
                        }
                    } else if (moduleType == PAGE_VIDEO_PLAYLIST_MODULE_KEY && data != null && data.getGist() != null && data.getGist().getImageGist().get_16x9() != null) {
                        String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                data.getGist().getImageGist().get_16x9(),
                                childViewWidth,
                                childViewHeight);
                        ImageView imageView = (ImageView) view;
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        RequestOptions requestOptions = new RequestOptions()
                                .override(childViewWidth, childViewHeight);

                        Glide.with(context)
                                .load(imageUrl)
                                .apply(requestOptions)
                                .into(imageView);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    if (appCMSUIcomponentViewType == PAGE_AUDIO_TRAY_MODULE_KEY) {
                        placeholder = R.drawable.vid_image_placeholder_square;
                        int size = (int) getViewWidth(context, childComponent.getLayout(), ViewGroup.LayoutParams.MATCH_PARENT);

                        int horizontalMargin = 0;
                        horizontalMargin = (int) getHorizontalMargin(getContext(), childComponent.getLayout());
                        int verticalMargin = (int) getVerticalMargin(getContext(), parentLayout, size, 0);
                        if (verticalMargin < 0) {
                            verticalMargin = (int) convertVerticalValue(getContext(), getYAxis(getContext(), getLayout(), 0));
                        }
                        LayoutParams llParams = new LayoutParams(size, size);
                        llParams.setMargins(horizontalMargin,
                                verticalMargin,
                                horizontalMargin,
                                verticalMargin);
                        view.setLayoutParams(llParams);

                        if (data.getGist() != null &&
                                data.getGist().getImageGist() != null &&
                                data.getGist().getImageGist().get_1x1() != null && (componentKey == PAGE_THUMBNAIL_IMAGE_KEY)) {
                            String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_1x1(),
                                    size,
                                    size);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                            if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                                if (data.getGist().getVideoImageUrl() != null) {
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getVideoImageUrl(),
                                            size,
                                            size);
                                }
                            }

                            RequestOptions requestOptions = new RequestOptions()
                                    .override(size, size)
                                    .placeholder(placeholder)
                                    .fitCenter();
                            if (!ImageUtils.loadImage((ImageView) view, imageUrl, ImageLoader.ScaleType.START) && context != null && appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null && !appCMSPresenter.getCurrentActivity().isFinishing()) {
                                Glide.with(context.getApplicationContext())
                                        .load(imageUrl).apply(requestOptions)
//                                        .override(size,size)
                                        .into(((ImageView) view));
                            }
                        } else {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            Glide.with(view).load(placeholder);
                        }

                    }
                    if (moduleType == PAGE_SEASON_TRAY_MODULE_KEY) {
                        if (onClickHandler != null) {
                            view.setOnClickListener(v -> onClickHandler.click(CollectionGridItemView.this,
                                    childComponent, data, position));
                        }
                    }
                } else if (componentKey == PAGE_PLAN_IMAGE_KEY) {
                    view.setVisibility(View.GONE);
                    if (data.getPlanDetails() != null && !data.getPlanDetails().isEmpty() && data.getPlanDetails().get(0) != null
                            && data.getPlanDetails().get(0).isHidePlanPrice() && data.getPlanDetails().get(0).getCarrierBillingProviders() != null
                            && !data.getPlanDetails().get(0).getCarrierBillingProviders().isEmpty()) {
                        if (data.getPlanDetails().get(0).getCarrierBillingProviders().contains(appCMSPresenter.getCurrentActivity().getString(R.string.gp_data_bundle))) {
                            ((ImageView) view).setImageResource(R.drawable.ic_gp_data_dundle);
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                }
                //Bellow is end of thumbnail Image
            } else if (componentType == PAGE_SEPARATOR_VIEW_KEY) {

                if (componentKey == PAGE_FIGHTER_SELECTOR_VIEW_KEY) {
//                    if(data.getFights().getFightId().equals(appCMSPresenter.getFocusedFightId())) {
                    if (data.isSelected()) {
                        (view).setBackgroundColor(Color.parseColor(
                                childComponent.getBackgroundColor()));

                    } else {
                        (view).setBackgroundColor(Color.parseColor(
                                "#00000000"));
                    }
                } else if (componentKey == PAGE_FIGHTER_SELECTOR_ARROW_VIEW_KEY) {
                    if (data.isSelected()) {
                        (view).setBackgroundResource(R.drawable.fight_select_bg);

                    } else {
                        (view).setBackgroundResource(0);
                    }
                }
            } else if (componentType == PAGE_BUTTON_KEY) {
                if (componentKey == PAGE_PLAY_IMAGE_KEY) {
                    ((TextView) view).setText("");
                    if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter)) {
                        view.setVisibility(View.GONE);
                    }
                } else if (componentKey == PAGE_PLAN_PURCHASE_BUTTON_KEY) {
                    /*if (data != null && data.getPlanDetails() != null
                    && data.getPlanDetails().size() >0 && data.getPlanDetails().get(0) != null
                    && data.getPlanDetails().get(0).getCallToAction() != null
                    && !TextUtils.isEmpty(data.getPlanDetails().get(0).getCallToAction())){
                        ((TextView) view).setText(data.getPlanDetails().get(0).getCallToAction());
                    } else {*/
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    //}
                    view.setBackgroundColor(ContextCompat.getColor(getContext(),
                            R.color.disabledButtonColor));
                    viewsToUpdateOnClickEvent.add(view);
                } else if (componentKey == PAGE_GAME_TICKETS_KEY) {
                    if (data.getGist() != null && data.getGist().getEventSchedule() != null &&
                            data.getGist().getEventSchedule().size() > 0 &&
                            data.getGist().getEventSchedule().get(0) != null && data.getGist().getEventSchedule().get(0).getEventTime() > 0) {
                        long eventDate = data.getGist().getEventSchedule().get(0).getEventTime();
                        long currentTimeMillis = System.currentTimeMillis();

                        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate * 1000L, "EEE MMM dd HH:mm:ss");

                        if (data != null && data.getGist() != null && data.getGist().getTicketUrl() != null &&
                                !TextUtils.isEmpty(data.getGist().getTicketUrl())) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.GONE);

                        }
                        ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                        ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                        viewsToUpdateOnClickEvent.add(view);
                        view.setOnClickListener(view1 -> {

                            String url = "";
                            if (data != null && data.getGist() != null &&
                                    data.getGist().getTicketUrl() != null) {
                                url = data.getGist().getTicketUrl();
                            }
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            appCMSPresenter.getCurrentActivity().startActivity(browserIntent);
                        });
                    }
                } else if (componentKey == PAGE_GRID_OPTION_KEY) {
                    if (viewTypeKey == PAGE_ARTICLE_TRAY_KEY) {
                        view.setBackground(context.getDrawable(R.drawable.dots_more_grey));
                        view.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
                        view.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                    }
                } else if (componentKey == PAGE_VIDEO_DOWNLOAD_BUTTON_KEY ||
                        componentKey == PAGE_AUDIO_DOWNLOAD_BUTTON_KEY) {

                    if (data.getGist() != null) {
                        long scheduleStartDate = (data.getGist().getScheduleStartDate());

                        //calculate remaining time from event date and current date
                        long timeToStart = CommonUtils.getTimeIntervalForEvent(scheduleStartDate, "yyyy MMM dd HH:mm:ss");
                        if (data != null
                                && data.getPricing() != null
                                && data.getPricing().getType() != null
                                && data.getPricing().getType().equalsIgnoreCase("PPV")) {
                            view.setVisibility(View.GONE);

                        } else if (scheduleStartDate > 0 && timeToStart > 0) {
                            view.setVisibility(View.GONE);

                        }
                        if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter)) {
                            view.setVisibility(View.GONE);
                        }
                        String userId = appCMSPresenter.getLoggedInUser();

                        if (appCMSUIcomponentViewType == PAGE_LIBRARY_01_MODULE_KEY) {

                            long eventDate = (data.getGist().getScheduleStartDate());

                            /**
                             * We have to hide the download button for PPV case or in TVOD if schedule start date is available and greater  than
                             * current time .2) download button  will be vivisble only for TVOD
                             * As of now ,no parameter is coming from API to differentiate b/w TVOD and PPV ,so adding these codition
                             */
                            long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate, "yyyy MMM dd HH:mm:ss");
                        /*
                        if schedule start time is available and if it is greater than current time then hide download button
                         */
                            if (remainingTime > 0) {
                                view.setVisibility(View.GONE);

                            } else if (data.getGist().getScheduleStartDate() > 0 && data.getGist().getScheduleEndDate() > 0) {
                                view.setVisibility(View.GONE);

                            } else if (data.getGist().getScheduleEndDate() > 0) {
                       /*
                        if schedule end time is available and if it is less than current time then hide download button
                         */
                                long endTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleEndDate(), "yyyy MMM dd HH:mm:ss");
                                if (endTime < 0) {
                                    view.setVisibility(View.GONE);
                                }
                            } else if (data.getGist().getScheduleStartDate() == 0 || data.getGist().getScheduleEndDate() == 0) {
                                view.setVisibility(View.VISIBLE);

                            }


//                        if (data.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE") || data.getGist().getScheduleEndDate() > 0) {
//                            ((ImageButton) view).setVisibility(View.GONE);
//
//                        }

                        }
                        if (!appCMSPresenter.isDownloadable()) {
                            view.setVisibility(View.GONE);

                        }
                        if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                            ((ImageButton) view).setImageResource(R.drawable.ic_downloaded_big);
                        } else {
                            try {
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
                                ViewCreator.UpdateDownloadImageIconAction updateDownloadImageIconAction =
                                        updateDownloadImageIconActionMap.get(data.getGist().getId());
                                if (updateDownloadImageIconAction == null) {
                                    updateDownloadImageIconAction = new ViewCreator.UpdateDownloadImageIconAction((ImageButton) view, appCMSPresenter,
                                            data, userId, radiusDifference, moduleId);
                                    updateDownloadImageIconActionMap.put(data.getGist().getId(), updateDownloadImageIconAction);
                                }

                                view.setTag(data.getGist().getId());

                                updateDownloadImageIconAction.updateDownloadImageButton((ImageButton) view);

                                appCMSPresenter.getUserVideoDownloadStatus(
                                        data.getGist().getId(), updateDownloadImageIconAction, userId);
                            } catch (Exception e) {

                            }
                        }

                        if (data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().equalsIgnoreCase(getResources().getString(R.string.season))) {
                            view.setVisibility(View.GONE);
                        }
                    }
                } /*else if (componentKey == PAGE_AUDIO_DOWNLOAD_BUTTON_KEY) {
                 *//*view.setOnClickListener(v -> onClickHandler.click(CollectionGridItemView.this,
                            childComponent, data, position));*//*
                    if (appCMSPresenter.isVideoDownloaded(data.getGist().getId())) {
                        ((ImageButton) view).setImageResource(R.drawable.ic_downloaded_big);
                        view.setOnClickListener(null);
                    } else if (appCMSPresenter.isVideoDownloading(data.getGist().getId())) {
                        int radiusDifference = 5;
                        if (BaseView.isTablet(context)) {
                            radiusDifference = 2;
                        }
                        appCMSPresenter.updateDownloadingStatus(
                                data.getGist().getId(),
                                (ImageButton) view,
                                appCMSPresenter,
                                new ViewCreator.UpdateDownloadImageIconAction(
                                        (ImageButton) view,
                                        appCMSPresenter,
                                        data,
                                        appCMSPresenter.getLoggedInUser(),
                                        radiusDifference,
                                        moduleId),
                                appCMSPresenter.getLoggedInUser(),
                                false,
                                radiusDifference,
                                moduleId);
                        view.setOnClickListener(null);
                    }
                } */ else {
                    if (onClickHandler != null) {
                        view.setOnClickListener(v -> onClickHandler.click(CollectionGridItemView.this,
                                childComponent, data, position));
                    }
                }
            } else if (componentType == PAGE_GRID_OPTION_KEY) {
                if (onClickHandler != null) {
                    view.setOnClickListener(v ->
                            onClickHandler.click(CollectionGridItemView.this,
                                    childComponent, data, position));
                }
            } else if (componentType == PAGE_LABEL_KEY &&
                    view instanceof TextView) {
//                if (TextUtils.isEmpty(((TextView) view).getText())) {
                if (componentKey == PAGE_CAROUSEL_TITLE_KEY &&
                        !TextUtils.isEmpty(data.getGist().getTitle())) {
                    ((TextView) view).setText(data.getGist().getTitle());
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    //((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    if (component != null &&
                            component.getView() != null &&
                            component.getView().equalsIgnoreCase(context.getResources().getString(R.string.app_cms_page_event_carousel_module_key))) {

                        if (BaseView.isTablet(view.getContext())) {
                            if (isLandscape(getContext()) == true) {
                                view.setBackgroundColor(Color.TRANSPARENT);
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                            } else {
                                setBorder(view);
                                ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
                            }
                        } else {
                            view.setBackgroundColor(Color.TRANSPARENT);
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        }
                    } else {
                        if (BaseView.isTablet(view.getContext()) && isLandscape(getContext())) {
                            if (appCMSPresenter.getAppCMSMain() != null &&
                                    appCMSPresenter.getAppCMSMain().getBrand() != null &&
                                    appCMSPresenter.getAppCMSMain().getBrand().getCta() != null &&
                                    appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary() != null &&
                                    appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor() != null
                            ) {
                                ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                            } else {
                                ((TextView) view).setTextColor(Color.parseColor(
                                        childComponent.getTextColor()));
                            }
                        } else {
                            ((TextView) view).setTextColor(Color.parseColor(
                                    childComponent.getTextColor()));
                        }
                    }
                    if (appCMSPresenter.isAFAApp())
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.white));
                } else if (componentKey == PAGE_CAROUSEL_INFO_KEY) {
                    if (data.getGist().getMediaType() != null && data.getGist().getMediaType().equalsIgnoreCase("AUDIO")) {
                        if (data.getCreditBlocks() != null && data.getCreditBlocks().size() > 0 && data.getCreditBlocks().get(0).getCredits() != null && data.getCreditBlocks().get(0).getCredits().size() > 0 && data.getCreditBlocks().get(0).getCredits().get(0).getTitle() != null) {
                            String artist = appCMSPresenter.getArtistNameFromCreditBlocks(data.getCreditBlocks());
                            ((TextView) view).setMaxLines(1);
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setText(artist);
                            view.setPadding(10,
                                    0,
                                    10,
                                    0);
                        }

                    } else if (data.getSeason() != null && 0 < data.getSeason().size()) {
                        ViewCreator.setViewWithShowSubtitle(appCMSPresenter, getContext(), data, view, false);
                    } else if (data.getGist().getBundleList() != null && 0 < data.getGist().getBundleList().size()) {
                        ViewCreator.setViewWithBundleSubtitle(appCMSPresenter, context, data,
                                view, false);
                    } else {
                        ViewCreator.setViewWithSubtitle(getContext(), data, view, appCMSPresenter);
                    }
                    if (TextUtils.isEmpty(((TextView) view).getText().toString())) {
                        view.setVisibility(INVISIBLE);
                    } else if ((settings != null && (settings.isNoInfo())))
                        view.setVisibility(INVISIBLE);
                    else {
                        view.setVisibility(VISIBLE);
                    }
                    if (BaseView.isTablet(view.getContext()) && BaseView.isLandscape(context)) {
                        if (appCMSPresenter.getAppCMSMain() != null &&
                                appCMSPresenter.getAppCMSMain().getBrand() != null &&
                                appCMSPresenter.getAppCMSMain().getBrand().getCta() != null &&
                                appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary() != null &&
                                appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor() != null
                        ) {
                            ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                        } else {
                            ((TextView) view).setTextColor(Color.parseColor(
                                    childComponent.getTextColor()));
                        }
                    } else {
                        ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
                    }
                    if (appCMSPresenter.isAFAApp())
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.white));
                } else if (componentKey == PAGE_THUMBNAIL_TITLE_KEY ||
                        componentKey == PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    if (componentKey == PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY) {
                        ((TextView) view).setGravity(Gravity.END);
                        StringBuffer publishDate = new StringBuffer();
                        if (data.getContentDetails().getAuthor().getPublishDate() != 0) {
                            publishDate.append("|");
                            publishDate.append(data.getContentDetails().getAuthor().getPublishDate());
                        }
                        ((TextView) view).setText(data.getContentDetails().getAuthor().getName() + publishDate.toString());
                    } else if (data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.content_type_person).toLowerCase())) {
                        StringBuilder personName = new StringBuilder();
                        if (data.getGist().getFirstName() != null) {
                            personName.append(data.getGist().getFirstName());
                            personName.append(" ");
                        }
                        if (data.getGist().getLastName() != null)
                            personName.append(data.getGist().getLastName());
                        ((TextView) view).setText(personName.toString());
                    } else if (data.getGist() != null && data.getGist().getTitle() != null) {
                        ((TextView) view).setText(data.getGist().getTitle());
                    }
                    ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        /*if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        }*/

                } else if (componentKey == PAGE_PLAN_FEATURE_TITLE_KEY && data != null && data.getTitle() != null) {
                    ((TextView) view).setText(data.getTitle());
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    }
                }/* else if (componentKey == PAGE_PLAN_FEATURE_TITLE_KEY &&
                        settings != null && settings.getItems() != null &&
                        !TextUtils.isEmpty(settings.getItems().get(position).getTitle())) {
//                    ((TextView) view).setText(settings.getItems().get(position).getTitle());
                    ((TextView) view).setText(data.getTitle());
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    }
                }*/ else if (componentKey == PAGE_PLAN_DESCRIPTION_KEY &&
                        !TextUtils.isEmpty(data.getDescription())) {
                    if (data.getPlanDetails() != null && data.getPlanDetails().size() > 0 && !Strings.isEmptyOrWhitespace(data.getPlanDetails().get(0).getDescription())) {
                        ((TextView) view).setText(data.getPlanDetails().get(0).getDescription());
                    } else {
                        ((TextView) view).setText(data.getDescription());
                    }
                    ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                    view.setVisibility(View.VISIBLE);

                } else if (componentKey == PAGE_THUMBNAIL_DESCRIPTION_KEY) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    ((TextView) view).setText(data.getGist().getDescription());
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    }
                } else if(componentKey == PAGE_SUBSCRIBE_BTN_FOR_PLAN_KEY){
                     if (metadataMap != null && metadataMap.getSubscribeNowCta() != null)
                    ((TextView) view).setText(metadataMap.getSubscribeNowCta());
                    ((TextView) view).setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
                    ((TextView) view).setPadding(10, 10, 10, 10);
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    }
                }

                else if (componentKey == PAGE_THUMBNAIL_READ_MORE_KEY) {
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    }
                } /*else if (componentKey == PAGE_HOME_TEAM_TITLE_KEY) {
                    if (data.getGist() != null && data.getGist().getHomeTeam() != null && data.getGist().getHomeTeam().getGist() != null && data.getGist().getHomeTeam().getGist().getTitle() != null) {

                        ((TextView) view).setText(data.getGist().getHomeTeam().getGist().getTitle());
                        ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());

                    }
                } else if (componentKey == PAGE_AWAY_TEAM_TITLE_KEY) {
                    if (data.getGist() != null && data.getGist().getAwayTeam() != null && data.getGist().getAwayTeam().getGist() != null && data.getGist().getAwayTeam().getGist().getTitle() != null) {
                        ((TextView) view).setText(data.getGist().getAwayTeam().getGist().getTitle());
                        ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                    }
                }*/ else if (componentKey == PAGE_GAME_DATE_KEY) {
                    if (data.getGist() != null && data.getGist().getEventSchedule() != null
                            && data.getGist().getEventSchedule().size() > 0
                            && data.getGist().getEventSchedule().get(0) != null
                            && data.getGist().getEventSchedule().get(0).getEventDate() != 0) {

                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }

                        StringBuilder thumbInfo = new StringBuilder();
                        thumbInfo.append(appCMSPresenter.getDateFormat(data.getGist().getEventSchedule().get(0).getEventDate() * 1000L, "EEEE"))
                                .append(" | ")
                                .append(appCMSPresenter.getDateFormat(data.getGist().getEventSchedule().get(0).getEventDate() * 1000L, "MMM dd"))
                                .append(", ")
                                .append(appCMSPresenter.getDateFormat(data.getGist().getEventSchedule().get(0).getEventDate() * 1000L, "yyyy"));
                        ((TextView) view).setText(thumbInfo);
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());

                        }
                    }
                } else if (componentKey == PAGE_TRAY_SCHEDULE_TITLE_KEY) {
                    if (data.getGist() != null && data.getGist().getTitle() != null) {

                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }

                        ((TextView) view).setText(data.getGist().getTitle());
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());

                        }

                    }
                } else if (componentKey == PAGE_PLAYER_TEAM_TITLE_TXT_KEY) {
                    if (data.getPlayersData() != null && data.getPlayersData().getFirstName() != null
                    ) {
                        StringBuilder strPlayerName = new StringBuilder();
                        strPlayerName.append(data.getPlayersData().getFirstName());
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }
                        if (data.getPlayersData().getLastName() != null) {
                            strPlayerName.append("\n" + data.getPlayersData().getLastName());
                        }
                        ((TextView) view).setText(strPlayerName);
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        }
                    }
                } else if (componentKey == PAGE_PLAYER_RECORD_LABEL_KEY) {
                    if (data.getPlayersData() != null && data.getPlayersData().getData() != null
                            && data.getPlayersData().getData().getMetadata() != null
                            && data.getPlayersData().getData().getMetadata().size() > 0
                            && data.getPlayersData().getData().getMetadata().get(0) != null
                            && data.getPlayersData().getData().getMetadata().get(0).getValue() != null
                    ) {

                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }

                        ((TextView) view).setText(data.getPlayersData().getData().getMetadata().get(0).getValue());
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        }

                    }
                } else if (componentKey == PAGE_HEIGHT_VALUE_TEXT) {
                    if (data.getPlayersData() != null && data.getPlayersData().getHeight() != null
                    ) {

                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }
                        ((TextView) view).setText(data.getPlayersData().getHeight());
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        }

                    }
                } else if (componentKey == PAGE_WEIGHT_VALUE_TEXT) {
                    if (data.getPlayersData() != null && data.getPlayersData().getWeight() != null
                    ) {

                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }
                        ((TextView) view).setText(data.getPlayersData().getWeight());
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        }
                    }
                } else if (componentKey == PAGE_PLAYER_SCORE_TEXT) {
                    if (data.getPlayersData() != null && data.getPlayersData().getData() != null
                            && data.getPlayersData().getData().getMetadata() != null && data.getPlayersData().getData().getMetadata().get(1) != null
                            && data.getPlayersData().getData().getMetadata().get(1).getValue() != null
                    ) {

                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        }
                        ((TextView) view).setText("(" + data.getPlayersData().getData().getMetadata().get(1).getValue() +
                                appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.player_points)) + ")");
                        if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getTextColor()));
                            ((TextView) view).setTextColor(textColor);
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        }

                    }
                } else if (componentKey == PAGE_HEIGHT_LABEL_TEXT || componentKey == PAGE_WEIGHT_LABEL_TEXT) {

                    if (!TextUtils.isEmpty(childComponent.getText())) {
                        ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    }
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    } else {
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                    }

                } else if (componentKey == PAGE_GAME_TIME_KEY) {
                    if (data.getGist() != null && data.getGist().getEventSchedule() != null
                            && data.getGist().getEventSchedule().size() > 0
                            && data.getGist().getEventSchedule().get(0) != null
                            && data.getGist().getEventSchedule().get(0).getEventDate() != 0) {
                        ((TextView) view).setText(appCMSPresenter.getDateFormat(data.getGist().getEventSchedule().get(0).getEventDate(), "hh:mm aa")
                                + " " + data.getGist().getEventSchedule().get(0).getEventTimeZone());
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());

                    }
                } else if (componentKey == PAGE_ARTICLE_TITLE_KEY && !TextUtils.isEmpty(data.getGist().getTitle())) {
                    ((TextView) view).setSingleLine(false);
                    ((TextView) view).setMaxLines(2);
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    ((TextView) view).setText(data.getGist().getTitle());

                    if (viewTypeKey == PAGE_AC_SEARCH_MODULE_KEY) {
                        ((TextView) view).setTextColor(
                                Color.parseColor(CommonUtils.getColor(context, childComponent.getTextColor())));
                    } else {
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                    }

                } else if (componentKey == PAGE_WATCHLIST_DURATION_KEY_BG) {

                    final int SECONDS_PER_MINS = 60;
                    String time_abberviation = "";
                    int duration = (int) (data.getGist().getRuntime() / SECONDS_PER_MINS);

                    if (duration >= 2) {
                        time_abberviation = appCMSPresenter.getLocalisedStrings().getMinsText();
                    } else if (duration == 1) {
                        time_abberviation = appCMSPresenter.getLocalisedStrings().getMinText();
                    } else if (duration == 0) {
                        duration = (int) data.getGist().getRuntime();
                        if (duration >= 2) {
                            time_abberviation = appCMSPresenter.getLocalisedStrings().getSecsText();
                        } else {
                            time_abberviation = appCMSPresenter.getLocalisedStrings().getSecText();
                        }
                    }

                    StringBuilder runtimeText = new StringBuilder()
                            .append(duration)
                            .append(" ")
                            .append(time_abberviation);

                    ((TextView) view).setText(runtimeText);
                    view.setBackgroundColor(Color.parseColor("#7D000000"));
                    ((TextView) view).setTextColor(Color.parseColor("#ffffff"));
                    if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter)) {
                        view.setVisibility(View.GONE);
                    }  //View is invisible if ContentType is Seriese
                } else if (componentKey == PAGE_ARTICLE_DESCRIPTION_KEY && !TextUtils.isEmpty(data.getGist().getDescription())) {
                    ((TextView) view).setSingleLine(false);
                    ((TextView) view).setMaxLines(3);
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    ((TextView) view).setText(data.getGist().getDescription());
                    if (viewTypeKey == PAGE_AC_SEARCH_MODULE_KEY) {
                        ((TextView) view).setTextColor(
                                Color.parseColor(CommonUtils.getColor(context, childComponent.getTextColor())));
                    } else {
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                    }
                } else if (componentKey == PAGE_API_SUMMARY_TEXT_KEY && !TextUtils.isEmpty(data.getGist().getSummaryText())) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    ((TextView) view).setText(data.getGist().getSummaryText());
                } else if (componentKey == PAGE_DELETE_DOWNLOAD_VIDEO_SIZE_KEY) {
                    ((TextView) view).setText(appCMSPresenter.getDownloadedFileSize(data.getGist().getId()));
                } else if (componentKey == PAGE_HISTORY_WATCHED_TIME_KEY) {
                    ((TextView) view).setSingleLine(false);
                    ((TextView) view).setMaxLines(2);
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    ((TextView) view).setText(appCMSPresenter.getLastWatchedTime(data, metadataMap));
                } else if (componentKey == PAGE_HISTORY_DURATION_KEY ||
                        componentKey == PAGE_DOWNLOAD_DURATION_KEY) {
                    final int SECONDS_PER_MINS = 60;
                    if ((data.getGist().getRuntime() / SECONDS_PER_MINS) < 2) {
                        StringBuilder runtimeText = new StringBuilder()
                                .append(data.getGist().getRuntime() / SECONDS_PER_MINS);
                        if (appCMSUIcomponentViewType != PAGE_SEASON_TRAY_MODULE_KEY) {
                            runtimeText.append(" ")
                                    .append(appCMSPresenter.getLocalisedStrings().getMinText());
                        }
                        ((TextView) view).setText(runtimeText);
                    } else {
                        StringBuilder runtimeText = new StringBuilder()
                                .append(data.getGist().getRuntime() / SECONDS_PER_MINS);
                        if (appCMSUIcomponentViewType != PAGE_SEASON_TRAY_MODULE_KEY) {
                            runtimeText.append(" ")
                                    .append(appCMSPresenter.getLocalisedStrings().getMinsText());
                        }
                        ((TextView) view).setText(runtimeText);
                    }

                    ((TextView) view).setTextColor(0x92000000 + appCMSPresenter.getGeneralTextColor());

                    if (appCMSUIcomponentViewType == PAGE_SEASON_TRAY_MODULE_KEY)
                        view.setVisibility(VISIBLE);

                } else if (componentKey == PAGE_WATCHLIST_DURATION_KEY) {
                    final int SECONDS_PER_MINS = 60;
                    if ((data.getGist().getRuntime() / SECONDS_PER_MINS) < 2) {
                        StringBuilder runtimeText = new StringBuilder()
                                .append(data.getGist().getRuntime() / SECONDS_PER_MINS)
                                .append(" ")
                                .append(appCMSPresenter.getLocalisedStrings().getMinText());
                        ((TextView) view).setText(runtimeText);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        StringBuilder runtimeText = new StringBuilder()
                                .append(data.getGist().getRuntime() / SECONDS_PER_MINS)
                                .append(" ")
                                .append(appCMSPresenter.getLocalisedStrings().getMinsText());
                        ((TextView) view).setText(runtimeText);
                        view.setVisibility(View.VISIBLE);
                    }
                } else if (componentKey == PAGE_AUDIO_DURATION_KEY) {
                    if (data != null && data.getGist() != null)
                        ((TextView) view).setText(appCMSPresenter.audioDuration((int) data.getGist().getRuntime()));
                } else if (componentKey == PAGE_WATCHLIST_DURATION_UNIT_KEY) {
                    if ((int) (data.getGist().getRuntime() / 60) < 1)
                        ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getMinText());
                    else
                        ((TextView) view).setText(appCMSPresenter.getLocalisedStrings().getMinsText());
                    ViewCreator.UpdateDownloadImageIconAction updateDownloadImageIconAction =
                            updateDownloadImageIconActionMap.get(data.getGist().getId());
                    if (updateDownloadImageIconAction != null) {
                        view.setClickable(true);
                        view.setOnClickListener(updateDownloadImageIconAction.getAddClickListener());
                    }
                    view.setVisibility(View.VISIBLE);
                    ((TextView) view).setTextColor(0x92000000 + appCMSPresenter.getGeneralTextColor());


                } else if (componentKey == PAGE_GRID_THUMBNAIL_INFO) {

                    if (data.getGist().getMediaType() != null && data.getGist().getMediaType().toLowerCase().contains(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())) {
                        StringBuilder thumbInfo = new StringBuilder();
                        if (data.getGist().getPublishDate() != null) {
                            try {
                                thumbInfo.append(appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getPublishDate()), "MMM dd"));
                            } catch (NumberFormatException e) {
                                thumbInfo.append(appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", data.getGist().getPublishDate()),
                                        "MMM dd"));
                            }
                        }
                        int noOfPhotos = 0;
                        if (data.getStreamingInfo() != null && data.getStreamingInfo().getPhotogalleryAssets() != null && data.getStreamingInfo().getPhotogalleryAssets().size() > 0) {
                            if (thumbInfo.length() > 0) {
                                thumbInfo.append(" | ");
                            }
                            noOfPhotos = data.getStreamingInfo().getPhotogalleryAssets().size();
                            thumbInfo.append(noOfPhotos);
                            if (noOfPhotos > 1)
                                thumbInfo.append(appCMSPresenter.getLocalisedStrings().getPhotosText());
                            else
                                thumbInfo.append(appCMSPresenter.getLocalisedStrings().getPhotoText());
                        }

                        ((TextView) view).setText(thumbInfo);
                    } else if ((appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS
                            || appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.FITNESS ||
                            appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.ENTERTAINMENT)
                            && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                            && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayDuration()) {
                        String thumbInfo = null;
                        if (appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayPublishedDate() && data.getGist().getPublishDate() != null) {
                            try {
                                thumbInfo = appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getPublishDate()), "MMM dd");
                            } catch (NumberFormatException e) {
                                thumbInfo = appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", data.getGist().getPublishDate()),
                                        "MMM dd");
                            }
                        }
                        if (data.getGist() != null && data.getGist().getReadTime() != null) {
                            StringBuilder readTimeText = new StringBuilder()
                                    .append(data.getGist().getReadTime().trim())
                                    .append("min")
                                    .append(" read ");

                            if (thumbInfo != null && thumbInfo.length() > 0) {
                                readTimeText.append("|")
                                        .append(" ")
                                        .append(thumbInfo);
                            }
                            ((TextView) view).setText(readTimeText);
                        } else {
                            long runtime = data.getGist().getRuntime();
                            if (thumbInfo != null && runtime > 0) {
                                ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime) + " | " + thumbInfo);
                            } else {
                                if (thumbInfo != null) {
                                    ((TextView) view).setText(thumbInfo);
                                } else if (runtime > 0) {
                                    ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime));
                                }
                            }

                        }
                    } else if (uiBlockName == UI_BLOCK_TRAY_02 && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                            && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayPublishedDate()) {
                        String thumbInfo = null;
                        if (data.getGist().getPublishDate() != null && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                                && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayPublishedDate()) {
                            try {
                                thumbInfo = appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getPublishDate()), "MMM dd");
                            } catch (NumberFormatException e) {
                                thumbInfo = appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", data.getGist().getPublishDate()),
                                        "MMM dd");
                            }
                        }
                        ((TextView) view).setText(thumbInfo);
                    } else if (uiBlockName == UI_BLOCK_TRAY_02 && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                            && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayDuration()) {
                        String thumbInfo = null;
                        long runtime = data.getGist().getRuntime();
                        if (thumbInfo != null && runtime > 0) {
                            ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime) + " | " + thumbInfo);
                        } else {
                            if (thumbInfo != null) {
                                ((TextView) view).setText(thumbInfo);
                            } else if (runtime > 0) {
                                ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime));
                            }
                        }
                    } else {
                        view.setVisibility(GONE);
                    }
                } else if (componentKey == PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO) {
                    StringBuilder thumbInfo = new StringBuilder();
                    if (data.getGist().getPublishDate() != null) {
                        try {
                            thumbInfo.append(appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getPublishDate()), "MMM dd"));
                        } catch (NumberFormatException e) {
                            thumbInfo.append(appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", data.getGist().getPublishDate()),
                                    "MMM dd"));
                        }
                    }
                    int noOfPhotos = 0;
                    if (data.getStreamingInfo() != null && data.getStreamingInfo().getPhotogalleryAssets() != null && data.getStreamingInfo().getPhotogalleryAssets().size() > 0) {
                        if (thumbInfo.length() > 0) {
                            thumbInfo.append(" | ");
                        }
                        noOfPhotos = data.getStreamingInfo().getPhotogalleryAssets().size();
                        thumbInfo.append(noOfPhotos);
                        if (noOfPhotos > 1)
                            thumbInfo.append(appCMSPresenter.getLocalisedStrings().getPhotosText());
                        else
                            thumbInfo.append(appCMSPresenter.getLocalisedStrings().getPhotoText());
                    }
                    ((TextView) view).setText(thumbInfo);
                } else if (componentKey == PAGE_API_TITLE ||
                        componentKey == PAGE_EPISODE_TITLE_KEY) {
                    if (data.getGist() != null && data.getGist().getTitle() != null) {
                        if (data.getGist().getEpisodeNum() != null
                                && !TextUtils.isEmpty(data.getGist().getEpisodeNum()) && moduleType != PAGE_DOWNLOAD_01_MODULE_KEY) {
                            ((TextView) view).setText(appCMSPresenter.getEpisodeTitlewithNumber(data.getGist().getTitle(), data.getGist().getEpisodeNum()));
                        } else {
                            ((TextView) view).setText(data.getGist().getTitle());
                        }
                        ((TextView) view).setSingleLine(true);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        view.setVisibility(View.VISIBLE);
//                            ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners,null));
//                            ((TextView) view).setTextColor(R.color.color_white);
                    } else if (data != null && data.getCategoryTitle() != null) {


                        view.setId(R.id.schedule_month);
                        ((TextView) view).setText(data.getCategoryTitle());
                        ((TextView) view).setSingleLine(true);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        view.setVisibility(View.VISIBLE);
//                            ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners,null));
                        if (Utils.catagoryPosition == position) {
                            ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getSettings().getStyle().getActiveTextColor()));
                        } else {
                            ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getSettings().getStyle().getTextColor()));
                        }

                    }
                } else if (componentKey == PAGE_EXPIRE_TIME_TITLE) {
                    if (data.getGist() != null && (data.getGist().getTransactionDateEpoch() > 0 || data.getGist().getTransactionEndDate() > 0)) {
                        ((TextView) view).setSingleLine(true);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        view.setVisibility(View.VISIBLE);
                        long eventDate = 0;
                        if (data.getGist().getTransactionEndDate() > 0) {
                            eventDate = data.getGist().getTransactionEndDate();
                        } else {
                            eventDate = data.getGist().getTransactionDateEpoch();
                        }

                        long remainingTime = CommonUtils.getTimeIntervalForEvent(eventDate * 1000L, "EEE MMM dd HH:mm:ss");

                        String expirationTime = appCMSPresenter.getRentExpirationFormat(remainingTime);
                        view.setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners, null));
                        ((TextView) view).setText(expirationTime);
                        if (remainingTime <= 0) {
                            view.setVisibility(View.GONE);
                        }

                    }
                    long eventDate = 1537256743L;
                    long remainingTime = appCMSPresenter.getTimeDifferenceFromCurrentTime(eventDate * 1000L, "EEE MMM dd HH:mm:ss");

                    String expirationTime = appCMSPresenter.getRentExpirationFormat(remainingTime);
                    view.setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners, null));
                    ((TextView) view).setText(expirationTime);
                    if (remainingTime <= 0) {
                        view.setVisibility(View.GONE);
                    }
                } else if (componentKey == PAGE_HISTORY_DESCRIPTION_KEY ||
                        componentKey == PAGE_WATCHLIST_DESCRIPTION_KEY ||
                        componentKey == PAGE_DOWNLOAD_DESCRIPTION_KEY) {
                    if (data != null && data.getGist() != null && data.getGist().getDescription() != null) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(2);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setText(data.getGist().getDescription());

                        try {
                            ViewCreator.setMoreLinkInDescription(appCMSPresenter,
                                    (TextView) view,
                                    data.getGist().getTitle(),
                                    data.getGist().getDescription(),
                                    50,
                                    (0x92000000 + appCMSPresenter.getGeneralTextColor()));

                            /*ViewTreeObserver titleTextVto = view.getViewTreeObserver();
                            ViewCreatorMultiLineLayoutListener viewCreatorTitleLayoutListener =
                                    new ViewCreatorMultiLineLayoutListener((TextView) view,
                                            data.getGist().getTitle(),
                                            data.getGist().getDescription(),
                                            appCMSPresenter,
                                            true,
                                            appCMSPresenter.getBrandPrimaryCtaColor(),
                                            (0x92000000 + appCMSPresenter.getGeneralTextColor()),
                                            false);
                            titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);*/
                        } catch (Exception e) {
                        }
                    }
                } else if (componentKey == PAGE_PLAYLIST_AUDIO_ARTIST_TITLE) {

                    String artist = appCMSPresenter.getArtistNameFromCreditBlocks(data.getCreditBlocks());
                    ((TextView) view).setText(artist);
                    ((TextView) view).setTextColor(Color.parseColor(childComponent.getTextColor()));

                } else if (componentKey == PAGE_API_DESCRIPTION) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setSingleLine(false);

                    }
                    ((TextView) view).setText(data.getGist().getDescription());
                    try {
                        ViewCreator.setMoreLinkInDescription(appCMSPresenter,
                                (TextView) view,
                                data.getGist().getTitle(),
                                data.getGist().getDescription(),
                                50,
                                (0x92000000 + appCMSPresenter.getGeneralTextColor()));
                       /* ViewTreeObserver titleTextVto = view.getViewTreeObserver();
                        ViewCreatorMultiLineLayoutListener viewCreatorTitleLayoutListener =
                                new ViewCreatorMultiLineLayoutListener((TextView) view,
                                        data.getGist().getTitle(),
                                        data.getGist().getDescription(),
                                        appCMSPresenter,
                                        false,
                                        appCMSPresenter.getBrandPrimaryCtaColor(),
                                        (0x92000000 + appCMSPresenter.getGeneralTextColor()),
                                        true);
                        viewCreatorTitleLayoutListener.setComponent(childComponent);
                        titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//
//                    if (appCMSUIcomponentViewType == PAGE_LIBRARY_01_MODULE_KEY) {
//                        if (data.getGist().getContentType().equalsIgnoreCase(getResources().getString(R.string.season))) {
//                            ((TextView) view).setVisibility(View.VISIBLE);
//                        } else {
//                            ((TextView) view).setVisibility(View.GONE);
//
//                        }
//
//                    } else {
//                        ((TextView) view).setVisibility(View.VISIBLE);
//
//                    }
                } else if (componentKey == PAGE_PLAN_FEATURE_TEXT_KEY) {
                    /*if (data != null && data.getPlanDetails() != null && data.getPlanDetails().get(0) != null &&
                            data.getPlanDetails().get(0).getFeatureDetails() != null &&
                            data.getPlanDetails().get(0).getFeatureDetails().size() != 0 &&
                            data.getPlanDetails().get(0).getFeatureDetails().get(0) != null &&
                            data.getPlanDetails().get(0).getFeatureDetails().get(0).getTextToDisplay() != null) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setText(data.getPlanDetails().get(0).getFeatureDetails().get(0).getTextToDisplay().toUpperCase());

                    }*/
                    view.setVisibility(View.VISIBLE);
                    StringBuilder planDuration = new StringBuilder();
                    if (data.getPlanDetails() != null && data.getPlanDetails().size() > 0 && Strings.isEmptyOrWhitespace(data.getPlanDetails().get(0).getTitle())) {
                        if (data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_monthly))) {

                            if (data.getRenewalCyclePeriodMultiplier() == 1) {
                                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                planDuration.append(" ");
                                if (metadataMap != null && metadataMap.getMonth() != null)
                                    planDuration.append(metadataMap.getMonth());
                                else
                                    planDuration.append(context.getString(R.string.plan_type_month));
                            } else {
                                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                planDuration.append(" ");
                                if (metadataMap != null && metadataMap.getMonths() != null)
                                    planDuration.append(metadataMap.getMonths());
                                else {
                                    planDuration.append(context.getString(R.string.plan_type_months));
                                }
                            /*if (data.getRenewalCyclePeriodMultiplier() > 1)
                                planDuration.append("s");*/
                            }
                        }
                        if (data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_yearly))) {
                            if (data.getRenewalCyclePeriodMultiplier() == 1) {
                                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                planDuration.append(" ");
                                if (metadataMap != null && metadataMap.getYear() != null)
                                    planDuration.append(metadataMap.getYear());
                                else
                                    planDuration.append(context.getString(R.string.plan_type_year));
                            } else {
                                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                planDuration.append(" ");
                                if (metadataMap != null && metadataMap.getYears() != null)
                                    planDuration.append(metadataMap.getYears());
                                else
                                    planDuration.append(context.getString(R.string.plan_type_years));
                            /*if (data.getRenewalCyclePeriodMultiplier() > 1)
                                planDuration.append("s");*/
                            }
                        }
                        if (data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_daily))) {
                            if (data.getRenewalCyclePeriodMultiplier() == 1) {
                                if (metadataMap != null && metadataMap.getDay() != null)
                                    planDuration.append(metadataMap.getDay());
                                else
                                    planDuration.append(context.getString(R.string.plan_type_day));
                            } else {
                                planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                planDuration.append(" ");
                                if (metadataMap != null && metadataMap.getDays() != null)
                                    planDuration.append(metadataMap.getDays());
                                else
                                    planDuration.append(context.getString(R.string.plan_type_days));

                            /*if (data.getRenewalCyclePeriodMultiplier() > 1)
                                planDuration.append("s");*/
                            }
                        }
                        ((TextView) view).setText(planDuration.toString().toUpperCase());
                    } else {
                        ((TextView) view).setText(data.getPlanDetails().get(0).getTitle());
                    }
                } else if (componentKey == PAGE_PLAN_TITLE_KEY) {
                    if (data.getPlanDetails() != null
                            && data.getPlanDetails().size() > 0
                            && data.getPlanDetails().get(0).getTitle() != null
                            && !TextUtils.isEmpty(data.getPlanDetails().get(0).getTitle())) {
                        ((TextView) view).setText(data.getPlanDetails().get(0).getTitle());
                    } else {
                        ((TextView) view).setText(data.getName());
                    }

                    if(uiBlockName== UI_BLOCK_SELECTPLAN_03||uiBlockName== UI_BLOCK_SELECTPLAN_01) {
                        ((TextView) view).setTextColor(themeColor);
                    } else {
                        ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                    }
                } else if (componentKey == PAGE_SINGLE_PLAN_SUBSCRIBE_TEXT_KEY) {
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                } else if (componentKey == PAGE_PLAN_PRICEINFO_KEY) {

                    if (data != null) {
                        if (data.getPlanDetails() != null && !data.getPlanDetails().isEmpty() && data.getPlanDetails().get(0) != null
                                && data.getPlanDetails().get(0).isHidePlanPrice()
                                && data.getPlanDetails().get(0).getCarrierBillingProviders() != null && !data.getPlanDetails().get(0).getCarrierBillingProviders().isEmpty()
                                && data.getPlanDetails().get(0).getCarrierBillingProviders().contains(appCMSPresenter.getCurrentActivity().getString(R.string.gp_data_bundle))) {
                            return;
                        }
                        view.setVisibility(View.VISIBLE);
                        int planIndex = 0;
                        for (int i = 0; i < data.getPlanDetails().size(); i++) {
                            if (data.getPlanDetails().get(i) != null &&
                                    data.getPlanDetails().get(i).isDefault()) {
                                planIndex = i;
                            }
                        }
                        Currency currency = null;
                        if (data.getPlanDetails() != null &&
                                !data.getPlanDetails().isEmpty() &&
                                data.getPlanDetails().get(planIndex) != null &&
                                data.getPlanDetails().get(planIndex).getRecurringPaymentCurrencyCode() != null) {
                            try {
                                currency = Currency.getInstance(data.getPlanDetails().get(planIndex).getRecurringPaymentCurrencyCode());
                            } catch (Exception e) {
                                //Log.e(TAG, "Could not parse locale");
                            }

                        }
                        if (data.getPlanDetails() != null &&
                                !data.getPlanDetails().isEmpty() &&
                                data.getPlanDetails().get(planIndex) != null &&
                                data.getPlanDetails().get(planIndex).getStrikeThroughPrice() != 0) {


                            Currency finalCurrency = currency;
                            int finalPlanIndex = planIndex;
                            double recurringPaymentAmount = data.getPlanDetails().get(finalPlanIndex).getRecurringPaymentAmount();

                            double strikeThroughPaymentAmount = data.getPlanDetails()
                                    .get(finalPlanIndex).getStrikeThroughPrice();
                            String formattedStrikeThroughPaymentAmount = context.getString(R.string.cost_with_fraction,
                                    strikeThroughPaymentAmount);
                            if (strikeThroughPaymentAmount - (int) strikeThroughPaymentAmount == 0) {
                                formattedStrikeThroughPaymentAmount = context.getString(R.string.cost_without_fraction,
                                        strikeThroughPaymentAmount);
                            }

                            StringBuilder stringBuilder = new StringBuilder();
                            if (finalCurrency != null) {
                                stringBuilder.append(finalCurrency.getCurrencyCode());
                                stringBuilder.append(" ");
                            }
                            stringBuilder.append(formattedStrikeThroughPaymentAmount);
                            if (CommonUtils.planViewPriceList != null
                                    && data.getPlanDetails() != null
                                    && CommonUtils.planViewPriceList.containsKey(data.getIdentifier())) {
                                if (CommonUtils.planViewPriceList.get(data.getIdentifier()).length() > 16) {
                                    ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                                }
                                ((TextView) view).setText(CommonUtils.planViewPriceList.get(data.getIdentifier()));

                            } else {
                                boolean isDefault = data.getPlanDetails().get(planIndex).isDefault();
                                BillingHelper.getInstance(appCMSPresenter).getSubsSKUDetail(data.getIdentifier(), skuDetails -> {

                                    String formattedRecurringPaymentAmount = context.getString(R.string.cost_with_fraction,
                                            recurringPaymentAmount);
                                    if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
                                        formattedRecurringPaymentAmount = context.getString(R.string.cost_without_fraction,
                                                recurringPaymentAmount);
                                    }

                                    if (skuDetails != null && !isDefault) {

                                        // extract digits only from strings
                                        String numberOnly = skuDetails.getPrice().replaceAll("[^0-9.,]+", "");

                                    /*Pattern pattern = Pattern.compile("[^0-9.]");
                                    String numberOnly = pattern.matcher(skuDetails.getPrice()).replaceAll("");*/
                                        formattedRecurringPaymentAmount = numberOnly;
                                        int strikeThroughLength = stringBuilder.length();
                                        stringBuilder.append("  ");
                                        if (finalCurrency != null) {
                                            stringBuilder.append(finalCurrency.getCurrencyCode());
                                            stringBuilder.append(" ");
                                        }
                                        stringBuilder.append(formattedRecurringPaymentAmount);

                                        SpannableString spannableString =
                                                new SpannableString(stringBuilder.toString());
                                        spannableString.setSpan(new StrikethroughSpan(), 0,
                                                strikeThroughLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        if (spannableString.length() > 16) {
                                            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                                        }
                                        ((TextView) view).setText(spannableString);
                                        CommonUtils.planViewPriceList.put(data.getIdentifier(), spannableString);
                                        //((TextView) view).setText(skuDetails.getPrice());
                                        CommonUtils.getAvailableCurrencies(skuDetails.getPriceCurrencyCode());
                                    } else {
                                        if (data.getPlanDetails().size() > 0 && data.getPlanDetails().get(0).getRecurringPaymentAmount() != 0) {
                                            int strikeThroughLength = stringBuilder.length();
                                            stringBuilder.append("  ");
                                            if (finalCurrency != null) {
                                                stringBuilder.append(finalCurrency.getCurrencyCode());
                                                stringBuilder.append(" ");
                                            }
                                            stringBuilder.append(formattedRecurringPaymentAmount);

                                            SpannableString spannableString =
                                                    new SpannableString(stringBuilder.toString());
                                            spannableString.setSpan(new StrikethroughSpan(), 0,
                                                    strikeThroughLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            if (spannableString.length() > 16) {
                                                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                                            }
                                            ((TextView) view).setText(spannableString);
                                            CommonUtils.planViewPriceList.put(data.getIdentifier(), spannableString);
                                        }
                                    }
                                });
                            }

                            FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) view.getLayoutParams();
                            layPar.gravity = Gravity.TOP;
                            view.setLayoutParams(layPar);
//                            }
                        } else {
                            double recurringPaymentAmount = data.getPlanDetails()
                                    .get(planIndex).getRecurringPaymentAmount();
                            String formattedRecurringPaymentAmount = context.getString(R.string.cost_with_fraction,
                                    recurringPaymentAmount);
                            if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
                                formattedRecurringPaymentAmount = context.getString(R.string.cost_without_fraction,
                                        recurringPaymentAmount);
                            }

                            StringBuilder planAmt = new StringBuilder();
                            if (currency != null) {
                                String currencySymbol = currency.getSymbol();
                                if (currencySymbol.contains("US$"))
                                    currencySymbol = "$";
                                planAmt.append(currency.getCurrencyCode());
                                planAmt.append(" ");
                            }

                            // StringBuilder planDuration = new StringBuilder();
                            if (appCMSUIcomponentViewType == PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY &&
                                    !appCMSPresenter.isMOTVApp() && uiBlockName != UI_BLOCK_SELECTPLAN_03) {


                                String finalFormattedRecurringPaymentAmount1 = formattedRecurringPaymentAmount;
                                if (CommonUtils.planViewPriceList != null
                                        && data.getPlanDetails() != null
                                        && CommonUtils.planViewPriceList.containsKey(data.getIdentifier())) {
                                    ((TextView) view).setText(CommonUtils.planViewPriceList.get(data.getIdentifier()));

                                } else {
                                    boolean isDefault = data.getPlanDetails().get(planIndex).isDefault();
                                    BillingHelper.getInstance(appCMSPresenter).getSubsSKUDetail(data.getIdentifier(), skuDetails -> {
                                        if (skuDetails != null && !isDefault) {
                                            String numberOnly = skuDetails.getPrice().replaceAll("[^0-9.]+", "");

                                    /*Pattern pattern = Pattern.compile("[^0-9.]");
                                    String numberOnly = pattern.matcher(skuDetails.getPrice()).replaceAll("");*/
                                            // formattedRecurringPaymentAmount = numberOnly;
                                            StringBuilder plan = new StringBuilder();
                                            plan.append(numberOnly);
                                            SpannableString spannableString = new SpannableString(plan.toString());
//                                float payFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_pay_font); //0.8f;
                                            float durationFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_duration_font); //0.8f;
                                            float priceFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_price_font); //1.0f;
                                            if (BaseView.isTablet(context)) {
//                                    payFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_pay_font);
                                                durationFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_duration_font);

                                                priceFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_price_font);
                                            }
                                            spannableString.setSpan(new RelativeSizeSpan(priceFont), 0, planAmt.toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, planAmt.toString().trim().length(),
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                            ((TextView) view).setText(spannableString, TextView.BufferType.SPANNABLE);
                                            CommonUtils.planViewPriceList.put(data.getIdentifier(), spannableString);
                                            CommonUtils.getAvailableCurrencies(skuDetails.getPriceCurrencyCode());
                                        } else {
                                            planAmt.append(finalFormattedRecurringPaymentAmount1);
                                            StringBuilder plan = new StringBuilder();
                                            plan.append(planAmt.toString().trim());
                                            SpannableString spannableString = new SpannableString(plan.toString());
//                                float payFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_pay_font); //0.8f;
                                            float durationFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_duration_font); //0.8f;
                                            float priceFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_price_font); //1.0f;
                                            if (BaseView.isTablet(context)) {
//                                    payFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_pay_font);
                                                durationFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_duration_font);

                                                priceFont = Utils.getFloatValue(context, R.dimen.app_cms_plan_price_font);
                                            }
                                            spannableString.setSpan(new RelativeSizeSpan(priceFont), 0, planAmt.toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, planAmt.toString().trim().length(),
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            ((TextView) view).setText(spannableString, TextView.BufferType.SPANNABLE);
                                            CommonUtils.planViewPriceList.put(data.getIdentifier(), spannableString);
                                        }
                                    });
                                }

//                                if (!appCMSPresenter.isSinglePlanFeatureAvailable()) {
                                FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) view.getLayoutParams();
                                layPar.gravity = Gravity.TOP;
                                view.setLayoutParams(layPar);
//                                }
                            } else {


                                String finalFormattedRecurringPaymentAmount = formattedRecurringPaymentAmount;
                                if (CommonUtils.planViewPriceList != null
                                        && data.getPlanDetails() != null
                                        && CommonUtils.planViewPriceList.containsKey(data.getIdentifier())) {
                                    ((TextView) view).setText(CommonUtils.planViewPriceList.get(data.getIdentifier()));

                                } else {
                                    boolean isDefault = data.getPlanDetails().get(planIndex).isDefault();
                                    BillingHelper.getInstance(appCMSPresenter).getSubsSKUDetail(data.getIdentifier(), skuDetails -> {
                                        if (skuDetails != null && !isDefault) {
                                            String numberOnly = skuDetails.getPrice().replaceAll("[^0-9.]+", "");

                                    /*Pattern pattern = Pattern.compile("[^0-9.]");
                                    String numberOnly = pattern.matcher(skuDetails.getPrice()).replaceAll("");*/
                                            // formattedRecurringPaymentAmount = numberOnly;
                                            planAmt.append(numberOnly);
                                            SpannableString spannableString = new SpannableString(planAmt.toString());
                                            ((TextView) view).setText(spannableString);
                                            CommonUtils.planViewPriceList.put(data.getIdentifier(), spannableString);
                                            CommonUtils.getAvailableCurrencies(skuDetails.getPriceCurrencyCode());
                                        } else {
                                            planAmt.append(finalFormattedRecurringPaymentAmount);
                                            StringBuilder plan = new StringBuilder();
                                            plan.append(planAmt.toString());
                                            if (appCMSPresenter.isMOTVApp()) {
                                                plan.append("*");
                                            }
                                   /* if (appCMSUIcomponentViewType == PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY)
                                        plan.append(planDuration.toString());*/
                                            SpannableString spannableString = new SpannableString(plan.toString());
                                            ((TextView) view).setText(spannableString);
                                            CommonUtils.planViewPriceList.put(data.getIdentifier(), spannableString);

                                        }
                                    });
                                }
//                                if (!appCMSPresenter.isSinglePlanFeatureAvailable()) {
                                FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) view.getLayoutParams();
                                layPar.gravity = Gravity.TOP;
                                view.setLayoutParams(layPar);
//                                }
                            }


                            ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags());
                        }
                        ((TextView) view).setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                    } else {
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());

                    }

                } /*else if (componentKey == PAGE_PLAN_BESTVALUE_KEY) {
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    ((TextView) view).setTextColor(Color.parseColor(childComponent.getTextColor()));
                }*/ else if (componentKey == PAGE_FIGHTER_LABEL_KEY) {
                    String fighter1Name = data.getFights().getFighter1_LastName();
                    String fighter2Name = data.getFights().getFighter2_LastName();
                    if (data.getFights().getWinnerId() != null && !TextUtils.isEmpty(data.getFights().getWinnerId())) {
                        if (data.getFights().getWinnerId().equalsIgnoreCase(data.getFights().getFighter1_Id())) {
                            fighter1Name = fighter1Name + "(Won)";
                        } else if (data.getFights().getWinnerId().equalsIgnoreCase(data.getFights().getFighter2Id())) {
                            fighter2Name = fighter2Name + "(Won)";
                        }
                    }
                    if (data.getFights().getFighter1_LastName() != null && data.getFights().getFighter2_LastName() != null) {
                        ((TextView) view).setText(data.getFights().getFightSerialNo() + " " + fighter1Name + "/" + fighter2Name);
                    }
                    ((TextView) view).setTextColor(Color.parseColor(
                            childComponent.getTextColor()));
                    ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                } else if (childComponent.getText() != null && !TextUtils.isEmpty(childComponent.getText())) {
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    ((TextView) view).setTextColor(Color.parseColor(
                            childComponent.getTextColor()));
                }

                setTypeFace(context,
                        appCMSPresenter,
                        jsonValueKeyMap,
                        component,
                        view);
//                }
            } else if (componentType == PAGE_LABEL_KEY) {
                if (componentKey == PAGE_RECORD_TYPE_KEY) {
                    String record = "";
                    String score = "";
                    if (data.getPlayersData() != null && data.getPlayersData().getData() != null
                            && data.getPlayersData().getData().getMetadata() != null) {
                        for (int j = 0; j < data.getPlayersData().getData().getMetadata().size(); j++) {
                            if (data.getPlayersData().getData().getMetadata().get(j).getName().equalsIgnoreCase("mma_record")) {
                                record = data.getPlayersData().getData().getMetadata().get(j).getValue();
                            } else if (data.getPlayersData().getData().getMetadata().get(j).getName().equalsIgnoreCase("pfl_record")) {
                                score = data.getPlayersData().getData().getMetadata().get(j).getValue();
                            }
                        }
                    }

                    for (int i = 0; i < childComponent.getComponents().size(); i++) {
                        TextView textView = new TextView(context);


                        if (jsonValueKeyMap.get(childComponent.getComponents().get(i).getKey()) == PAGE_PLAYER_SCORE_TEXT) {
                            if (score != null && !TextUtils.isEmpty(score)) {
                                textView.setText("(" + score + ")");
                            }
//                            textView.setText("(" + score + "pts)");
                        } else if (jsonValueKeyMap.get(childComponent.getComponents().get(i).getKey()) == PAGE_PLAYER_RECORD_LABEL_KEY) {
                            textView.setText(record);

                        }

                        if (childComponent.getComponents().get(i).getNumberOfLines() != 0) {
                            textView.setSingleLine(false);
                            textView.setMaxLines(childComponent.getComponents().get(i).getNumberOfLines());
                            textView.setEllipsize(TextUtils.TruncateAt.END);
                        }

                        textView.setTextColor(appCMSPresenter.getGeneralTextColor());
                        if (!TextUtils.isEmpty(childComponent.getComponents().get(i).getTextColor())) {
                            int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                    childComponent.getComponents().get(i).getTextColor()));
                            textView.setTextColor(textColor);
                        }
                        setTypeFace(context,
                                appCMSPresenter,
                                jsonValueKeyMap,
                                component,
                                textView);
                        if (getFontSize(context, childComponent.getComponents().get(i).getLayout()) > 0) {
                            textView.setTextSize(getFontSize(context, childComponent.getComponents().get(i).getLayout()));
                        }
                        ((LinearLayout) view).addView(textView);

                    }
                }

            } else if (componentType == PAGE_PLAN_META_DATA_VIEW_KEY) {
                if (view instanceof ViewPlansMetaDataView) {
                    ((ViewPlansMetaDataView) view).setData(data);
                }

                if (view instanceof SubscriptionMetaDataView) {
                    ((SubscriptionMetaDataView) view).setData(data);
                }
            } else if (componentType == PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                    componentType == PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY) {
                view.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else if (componentType == PAGE_PROGRESS_VIEW_KEY) {
                if (view instanceof ProgressBar) {
                    ContentDatum historyData = null;
                    if (data != null && data.getGist() != null && data.getGist().getId() != null) {
                        historyData = appCMSPresenter.getUserHistoryContentDatum(data.getGist().getId());
                    }

                    int progress = 0;

                    if (historyData != null) {
                        data.getGist().setWatchedPercentage(historyData.getGist().getWatchedPercentage());
                        data.getGist().setWatchedTime(historyData.getGist().getWatchedTime());
                        if (historyData.getGist().getWatchedPercentage() > 0) {
                            progress = historyData.getGist().getWatchedPercentage();
                            view.setVisibility(View.VISIBLE);
                            ((ProgressBar) view).setProgress(progress);
                        } else {
                            long watchedTime = historyData.getGist().getWatchedTime();
                            long runTime = historyData.getGist().getRuntime();
                            if (watchedTime > 0 && runTime > 0) {
                                long percentageWatched = (long) (((double) watchedTime / (double) runTime) * 100.0);
                                progress = (int) percentageWatched;
                                ((ProgressBar) view).setProgress(progress);
                                view.setVisibility(View.VISIBLE);
                            } else {
                                view.setVisibility(View.INVISIBLE);
                                ((ProgressBar) view).setProgress(0);
                            }
                        }
                    } else {
                        view.setVisibility(View.INVISIBLE);
                    }
                }
            }

            if (shouldShowView(childComponent) && bringToFront) {
                view.bringToFront();
            }
            view.forceLayout();
        } else {
            Log.d("bindView", "CollectionGridItemView else");
        }
    }

    private void setBorder(View itemView) {
        GradientDrawable planBorder = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.TRANSPARENT, Color.parseColor("#000000")});
        planBorder.setShape(GradientDrawable.RECTANGLE);
        planBorder.setCornerRadius(0f);
        itemView.setBackground(planBorder);
    }

    public Component matchComponentToView(View view) {
        for (ItemContainer itemContainer : childItems) {
            if (itemContainer.childView == view) {
                return itemContainer.component;
            }
        }
        return null;
    }

    public List<View> getViewsToUpdateOnClickEvent() {
        return viewsToUpdateOnClickEvent;
    }

    public List<ItemContainer> getChildItems() {
        return childItems;
    }

    public interface OnClickHandler {
        void click(CollectionGridItemView collectionGridItemView,
                   Component childComponent,
                   ContentDatum data, int clickPosition);

        void play(Component childComponent, ContentDatum data);
    }

    public static class ItemContainer {
        View childView;
        Component component;

        public View getChildView() {
            return childView;
        }

        public Component getComponent() {
            return component;
        }

        public static class Builder {
            private ItemContainer itemContainer;

            public Builder() {
                itemContainer = new ItemContainer();
            }

            Builder childView(View childView) {
                itemContainer.childView = childView;
                return this;
            }

            public Builder component(Component component) {
                itemContainer.component = component;
                return this;
            }

            public ItemContainer build() {
                return itemContainer;
            }
        }
    }

    public static class GradientTransformation extends BitmapTransformation {
        private final String ID;

        private int imageWidth, imageHeight;
        private AppCMSPresenter appCMSPresenter;
        private String imageUrl;

        public GradientTransformation(int imageWidth,
                                      int imageHeight,
                                      AppCMSPresenter appCMSPresenter,
                                      String imageUrl) {
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.appCMSPresenter = appCMSPresenter;
            this.imageUrl = imageUrl;
            this.ID = imageUrl;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof GradientTransformation;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            try {
                byte[] ID_BYTES = ID.getBytes(STRING_CHARSET_NAME);
                messageDigest.update(ID_BYTES);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "Could not update disk cache key: " + e.getMessage());
            }
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                   int outWidth, int outHeight) {
            int width = toTransform.getWidth();
            int height = toTransform.getHeight();

            boolean scaleImageUp = false;

            Bitmap sourceWithGradient;
            if (width < imageWidth &&
                    height < imageHeight) {
                scaleImageUp = true;
                float widthToHeightRatio =
                        (float) width / (float) height;
                width = (int) (imageHeight * widthToHeightRatio);
                height = imageHeight;
                sourceWithGradient =
                        Bitmap.createScaledBitmap(toTransform,
                                width,
                                height,
                                false);
            } else {
                sourceWithGradient =
                        Bitmap.createBitmap(width,
                                height,
                                Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(sourceWithGradient);
            if (!scaleImageUp) {
                canvas.drawBitmap(toTransform, 0, 0, null);
            }

            Paint paint = new Paint();
            LinearGradient shader = new LinearGradient(0,
                    0,
                    0,
                    height,
                    0xFFFFFFFF,
                    0xFF000000,
                    Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            canvas.drawRect(0, 0, width, height, paint);
            paint = null;
            return sourceWithGradient;
        }

        @Override
        public int hashCode() {
            return ID.hashCode();
        }
    }


}

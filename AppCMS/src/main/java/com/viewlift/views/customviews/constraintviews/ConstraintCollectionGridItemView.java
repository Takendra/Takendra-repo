package com.viewlift.views.customviews.constraintviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.offlinedrm.FetchOffineDownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.utils.IdUtils;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CategoryCompTray07;
import com.viewlift.views.customviews.DownloadComponent;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.plans.SubscriptionMetaDataView;
import com.viewlift.views.customviews.plans.ViewPlansMetaDataView;
import com.viewlift.views.utilities.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BADGE_DETAIL_PAGE_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BRAND_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BROWSE_CLASS_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_BADGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CAROUSEL_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EMPTY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EPISODE_DURATION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EPISODE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAY_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_BADGE_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_TRAY07_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_VIEW_GRID_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_HISTORY_04;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_01;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_WATCHLIST_03;
import static com.viewlift.views.customviews.BaseView.dpToPx;
import static com.viewlift.views.customviews.BaseView.getFontSize;
import static com.viewlift.views.customviews.BaseView.getGridHeight;
import static com.viewlift.views.customviews.BaseView.getGridWidth;
import static com.viewlift.views.customviews.BaseView.getViewHeight;
import static com.viewlift.views.customviews.BaseView.getViewWidth;
import static com.viewlift.views.customviews.BaseView.isLandscape;
import static com.viewlift.views.customviews.BaseView.isTablet;
import static com.viewlift.views.customviews.ViewCreator.setTypeFace;

public class ConstraintCollectionGridItemView extends ConstraintLayout {

    private final Layout parentLayout;
    private final boolean useParentLayout;
    private final Component component;
    private final String moduleId;
    protected int defaultWidth;
    protected int defaultHeight;
    AppCMSUIKeyType viewTypeKey;
    Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private List<ItemContainer> childItems;
    private List<View> viewsToUpdateOnClickEvent;
    private boolean createRoundedCorners;
    private Handler handler;
    private Runnable r;
    ContentTypeChecker contentTypeChecker;
    private boolean selectable;


    @Inject
    public ConstraintCollectionGridItemView(Context context,
                                            Layout parentLayout,
                                            boolean useParentLayout,
                                            Component component,
                                            String moduleId,
                                            int defaultWidth,
                                            int defaultHeight,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                            boolean createRoundedCorners,
                                            AppCMSUIKeyType viewTypeKey,
                                            String blockName) {
        super(context);
        this.parentLayout = parentLayout;
        this.useParentLayout = useParentLayout;
        this.component = component;
        this.moduleId = moduleId;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.viewsToUpdateOnClickEvent = new ArrayList<>();
        this.createRoundedCorners = createRoundedCorners;
        this.viewTypeKey = viewTypeKey;
        childItems = new ArrayList<>();
        if (contentTypeChecker == null)
            contentTypeChecker = new ContentTypeChecker(context);
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
        if (componentKey != null && componentKey == PAGE_WEEK_SCHEDULE_GRID_KEY || componentKey == PAGE_WEEK_SCHEDULE_VIEW_GRID_KEY) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else if (component.getKey().equalsIgnoreCase("carousel")) {
            setLayoutParams(new LayoutParams(defaultWidth, defaultHeight));
        } else {
            defaultHeight = (int) getGridHeight(getContext(),
                    component.getLayout(),
                    (int) getViewHeight(getContext(),
                            component.getLayout(),
                            defaultHeight));
            defaultWidth = (int) getGridWidth(getContext(),
                    component.getLayout(),
                    (int) getViewWidth(getContext(),
                            component.getLayout(),
                            defaultWidth));
            if (defaultWidth == -1)
                defaultWidth = BaseView.getDeviceWidth();
            String ratio = BaseView.getViewRatio(context, component.getLayout(), "");
            if (!ratio.equalsIgnoreCase("") && !TextUtils.isEmpty(ratio)) {
                defaultHeight = BaseView.getViewHeightByRatio(ratio, defaultWidth);
            }

            int trayMarginHorizontal = getTrayHorizontalMargin(context, component.getLayout(), 0);
            int trayMarginVertical = getTrayVerticalMargin(context, component.getLayout(), 0);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(defaultWidth, defaultHeight);
            layoutParams.setMargins(0, 0, trayMarginHorizontal, trayMarginVertical);
            setLayoutParams(new LayoutParams(layoutParams));

            if (getTrayPaddingHorizontal(getContext(), component.getLayout()) != -1.0f) {
                int trayPadding = (int) getTrayPaddingHorizontal(getContext(), component.getLayout());
                setPadding(trayPadding, 0, trayPadding, 0);
            } else if (getTrayPaddingVertical(getContext(), component.getLayout()) != -1.0f) {
                int trayPaddingVertical = (int) getTrayPaddingVertical(getContext(), component.getLayout());
                setPadding(0, trayPaddingVertical, 0, trayPaddingVertical);
            }
        }

        if ("NetBankingList".equalsIgnoreCase(component.getKey()) || "cardIconsList".equalsIgnoreCase(component.getKey()) || "upiIconsList".equalsIgnoreCase(component.getKey())) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (CommonUtils.isRecommendationTrayModule(blockName) || CommonUtils.isTrayModule(blockName)) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (componentKey != PAGE_CAROUSEL_VIEW_KEY) {
                setPadding(5, 5, 5, 5);
            }
        }

    }

    public static class ItemContainer {
        public View childView;
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

            ItemContainer.Builder childView(View childView) {
                itemContainer.childView = childView;
                return this;
            }

            public ItemContainer.Builder component(Component component) {
                itemContainer.component = component;
                return this;
            }

            public ItemContainer build() {
                return itemContainer;
            }
        }
    }

    public void addChild(ItemContainer itemContainer) {
        childItems.add(itemContainer);
        addView(itemContainer.childView);
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public interface OnClickHandler {
        void click(ConstraintCollectionGridItemView collectionGridItemView,
                   Component childComponent,
                   ContentDatum data, int clickPosition);

        void play(Component childComponent, ContentDatum data);
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
                          final ConstraintCollectionGridItemView.OnClickHandler onClickHandler,
                          final String componentViewType,
                          int themeColor,
                          AppCMSPresenter appCMSPresenter,
                          int position,
                          Settings settings,
                          String blockName, MetadataMap metadataMap) {

        final Component childComponent = matchComponentToView(view);
        appCMSPresenter.setShowDatum(data);
        AppCMSUIKeyType moduleType = jsonValueKeyMap.get(componentViewType);
        ConstraintSet set = new ConstraintSet();

        set.clone(this);
        if (moduleType == null) {
            moduleType = PAGE_EMPTY_KEY;
        }

        Map<String, ViewCreator.UpdateDownloadImageIconAction> updateDownloadImageIconActionMap =
                appCMSPresenter.getUpdateDownloadImageIconActionMap();

        if (childComponent != null && onClickHandler != null) {
            if (data != null) {
                final int pos = position;
                view.setOnClickListener(v -> onClickHandler.click(ConstraintCollectionGridItemView.this,
                        childComponent, data, pos));
            }
            AppCMSUIKeyType appCMSUIcomponentViewType = jsonValueKeyMap.get(componentViewType);
            AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
            AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
            AppCMSUIKeyType uiBlockName = jsonValueKeyMap.get(blockName);
            if (componentKey == null) {
                componentKey = PAGE_EMPTY_KEY;
            }
            if (componentType == null) {
                componentType = PAGE_EMPTY_KEY;
            }
            if ((componentKey == PAGE_PLAY_KEY || componentKey == PAGE_PLAY_IMAGE_KEY || appCMSUIcomponentViewType == PAGE_CAROUSEL_VIEW_KEY)
                    && data != null) {
                int finalPosition = position;
                view.setOnClickListener(v -> onClickHandler.click(null,
                        childComponent, data, finalPosition));


            }
            switch (componentType) {
                case PAGE_LINEAR_LAYOUT_KEY: {
                    switch (componentKey) {
                        case CATEGORY_TRAY_LAYOUT_07:
                            ((CategoryCompTray07) view).updateCategory(data, position);
                            break;
                    }
                    break;
                }

                case PAGE_SEPARATOR_VIEW_KEY:
                    switch (componentKey) {
                        case VIEW_SELECTION:
                            view.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                            break;
                    }
                    break;

                case PAGE_IMAGE_KEY: {
                    int childViewWidth = (int) getViewWidth(getContext(),
                            childComponent.getLayout(),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    int childViewHeight = (int) getViewHeight(getContext(),
                            childComponent.getLayout(),
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    String ratio = BaseView.getViewRatio(context, childComponent.getLayout(), "16:9");
                    boolean isSeason = false;
                    if (uiBlockName == UI_BLOCK_HISTORY_04 || uiBlockName == UI_BLOCK_WATCHLIST_03 || uiBlockName == UI_BLOCK_SHOW_DETAIL_06 || uiBlockName == UI_BLOCK_SHOW_DETAIL_01)
                        isSeason = true;
                    int placeholder = ImageUtils.getPlaceHolderByRatio(ratio, isSeason);

                    switch (componentKey) {
                        case PAGE_HOME_TRAY_THUMBNAIL_PLAY: {
                            ImageView imagePlay = (ImageView) view;
                            imagePlay.setBackgroundResource(R.drawable.neo_video_detail_play);
                            imagePlay.setOnClickListener(v -> {

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
                        case PAGE_LINK_THUMBNAIL_IMAGE_KEY: {
                            String imageUrl = "";
                            String placeURL = "";

                            if (data.getPages().get(0) != null) {
                                //if (data.getPages().get(0).getOgDetails() != null) {
                                try {
                                    placeholder = R.drawable.heigh_resolution_placeholder;
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getPages().get(0),
                                            //data.getPages().get(0).getOgDetails().getThumbnailUrl(),
                                            childViewWidth,
                                            childViewHeight);
                                    RequestOptions requestOptions = new RequestOptions()
                                            // .transform(gradientTransform)
                                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                            .override(childViewWidth, childViewHeight)
                                            .fitCenter()
                                            .placeholder(placeholder);
                                    Glide.with(context)
                                            .load(imageUrl)
                                            .apply(requestOptions)
                                            .into((ImageView) view);
                                    ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            view.setOnClickListener(v -> {
                                appCMSPresenter.navigateToPage(data.getPages().get(0).getId(),
                                        data.getPages().get(0).getTitle(),
                                        "",
                                        false,
                                        true,
                                        false,
                                        true,
                                        false,
                                        null);

                            });


                        }
                        break;
                        case PAGE_CAROUSEL_IMAGE_KEY:
                        case PAGE_CAROUSEL_BADGE_IMAGE_KEY:
                        case PAGE_THUMBNAIL_IMAGE_KEY:
                        case PAGE_THUMBNAIL_IMAGE_TRAY07_KEY:
                        case PAGE_BADGE_DETAIL_PAGE_IMAGE:
                        case PAGE_THUMBNAIL_BADGE_IMAGE:
                        case PAGE_BADGE_IMAGE_KEY: {
                            ((ImageView) view).setAdjustViewBounds(true);
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            if (uiBlockName == UI_BLOCK_SHOW_DETAIL_01) {
                                placeholder = R.drawable.vid_image_placeholder_16x9_season;
                            }
                            if (data != null &&
                                    data.getGist() != null) {
                                set.setDimensionRatio(view.getId(), ratio);
                                if (componentViewType != null &&
                                        !componentViewType.isEmpty() &&
                                        ((blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04)))
                                                || componentViewType.contains("AC FullWidthCarousel 01"))) {

                                    childViewWidth = BaseView.getDeviceWidth();

                                } /*else if (componentKey == PAGE_CAROUSEL_IMAGE_KEY) {
                                    childViewWidth = com.viewlift.views.customviews.BaseView.getDeviceWidth();
                                }*/

                               /* if (childViewWidth == -1)
                                    childViewWidth = BaseView.getViewWidthByRatio(ratio, childViewHeight);
                                else*/
                                if (childViewWidth == -1) {
                                    childViewWidth = BaseView.getDeviceWidth();
                                }
                                childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                //String ratio = BaseView.getViewRatio(context, childComponent.getLayout(), "16:9");
                                //int placeholder = ImageUtils.getPlaceHolderByRatio(ratio, false);

                                childViewWidth = (int) BaseView.getViewWidth(context, CommonUtils.getTrayWidth(ratio, context), ViewGroup.LayoutParams.WRAP_CONTENT);
                                childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                //Drawable placeholderDrawable = resizePlaceholder(getResources().getDrawable(placeholder), childViewWidth, childViewHeight);

                                //System.out.println("carousel_gradient dimen3-  PAGE_CAROUSEL_IMAGE_KEY height" + childViewHeight + " width" + childViewWidth);

                                String imageUrl = "";
                                if (appCMSUIcomponentViewType == PAGE_BRAND_TRAY_MODULE_KEY
                                        && data.getGist() != null
                                        && data.getGist().getImageGist() != null
                                        && data.getGist().getImageGist().get_3x4() != null) {
                                    childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                    imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                            data.getGist().getImageGist().get_3x4(),
                                            childViewWidth,
                                            childViewHeight);
                                } else if (componentKey == PAGE_THUMBNAIL_IMAGE_KEY
                                        || componentKey == PAGE_THUMBNAIL_IMAGE_TRAY07_KEY
                                        || componentKey == PAGE_CAROUSEL_IMAGE_KEY) {
                                    if (data.getGist() != null) {
                                        if (data.getGist().getImageGist() != null) {
                                            if (ratio.contains("3:4")
                                                    && data.getGist().getImageGist().get_3x4() != null) {
                                                childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getGist().getImageGist().get_3x4(),
                                                        childViewWidth,
                                                        childViewHeight);
                                            } else if (ratio.contains("1:1")
                                                    && data.getGist().getImageGist().get_1x1() != null) {
                                                childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getGist().getImageGist().get_1x1(),
                                                        childViewWidth,
                                                        childViewHeight);
                                            } else if (ratio.contains("32:9")
                                                    && data.getGist().getImageGist().get_32x9() != null
                                                    && componentKey == PAGE_CAROUSEL_IMAGE_KEY) {
                                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getGist().getImageGist().get_32x9(),
                                                        childViewWidth,
                                                        childViewHeight);
                                            } else if (ratio.contains("9:16")
                                                    && data.getGist().getImageGist().get_9x16() != null) {
//                                                 childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getGist().getImageGist().get_9x16(),
                                                        childViewWidth,
                                                        childViewHeight);
                                            } else if (ratio.contains("16:9")
                                                    && data.getGist().getImageGist().get_16x9() != null) {
                                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getGist().getImageGist().get_16x9(),
                                                        childViewWidth,
                                                        childViewHeight);
                                            } else if (((moduleType == PAGE_SCHEDULE_CAROUSEL_MODULE_KEY &&
                                                    isTablet(context))
                                                    || (moduleType == PAGE_BRAND_CAROUSEL_MODULE_TYPE &&
                                                    isLandscape(context)))
                                                    && data.getGist().getImageGist().get_32x9() != null) {
                                                imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                        data.getGist().getImageGist().get_32x9(),
                                                        childViewWidth,
                                                        childViewHeight);
                                            }
                                        } else if (data.getGist().getVideoImageUrl() != null) {
                                            childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                    data.getGist().getVideoImageUrl(),
                                                    childViewWidth,
                                                    childViewHeight);
                                        }
                                    }


                                } else if (componentKey == PAGE_CAROUSEL_BADGE_IMAGE_KEY
                                        || componentKey == PAGE_BADGE_IMAGE_KEY
                                        || componentKey == PAGE_THUMBNAIL_BADGE_IMAGE
                                        || componentKey == PAGE_BADGE_DETAIL_PAGE_IMAGE) {
                                    if (ratio.contains("3:4")
                                            && data.getGist() != null
                                            && data.getGist().getBadgeImages() != null
                                            && data.getGist().getBadgeImages().get_3x4() != null) {
                                        childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                        imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                data.getGist().getBadgeImages().get_3x4(),
                                                childViewWidth,
                                                childViewHeight);
                                    } else if (ratio.contains("1:1")
                                            && data.getGist() != null
                                            && data.getGist().getBadgeImages() != null
                                            && data.getGist().getBadgeImages().get_1x1() != null) {
                                        childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                        imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                data.getGist().getBadgeImages().get_1x1(),
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
                                    } else if (ratio.contains("9:16")
                                            && data.getGist() != null
                                            && data.getGist().getBadgeImages() != null
                                            && data.getGist().getBadgeImages().get_9x16() != null) {
                                        // childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                                        imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                data.getGist().getBadgeImages().get_9x16(),
                                                childViewWidth,
                                                childViewHeight);
                                    } else if (ratio.contains("16:9")
                                            && data.getGist() != null && data.getGist().getBadgeImages() != null
                                            && data.getGist().getBadgeImages().get_16x9() != null) {
                                        imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                                data.getGist().getBadgeImages().get_16x9(),
                                                childViewWidth,
                                                childViewHeight);
                                    } else {
                                        imageUrl = "";

                                    }
                                }
                                try {
                                    RequestOptions requestOptions = new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                            .override(childViewWidth, childViewHeight);
                                    if (componentKey != PAGE_CAROUSEL_BADGE_IMAGE_KEY
                                            && componentKey != PAGE_THUMBNAIL_BADGE_IMAGE
                                            && componentKey != PAGE_BADGE_IMAGE_KEY) {
                                        requestOptions.placeholder(placeholder);
                                    }
                                    if (childComponent.isCircular()) {
                                        requestOptions.circleCrop();
                                        ((CircularImageView) view).setAdjustViewBounds(true);
                                        ((CircularImageView) view).setBorderWidth(childComponent.getCircularBorderWidth());
                                        ((CircularImageView) view).setBorderColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                                    }
                                    else if (!appCMSPresenter.isNewsTemplate()&&ratio.contains("3:4") && componentKey == PAGE_THUMBNAIL_IMAGE_KEY) {
                                        requestOptions.centerCrop();
                                    }
                                    Glide.with(context)
                                            .asBitmap()
                                            .load(imageUrl)
                                            .apply(requestOptions)
                                            // .thumbnail(0.70f)
                                            .into(new BitmapImageViewTarget((ImageView) view) {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    super.onResourceReady(resource, transition);
                                                    if (childComponent.isCircular()) {
                                                        Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                                            @Override
                                                            public void onGenerated(Palette palette) {
                                                                // Here's your generated palette
                                                                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                                                                if (swatch != null) {
                                                                    int color = palette.getDarkMutedColor(swatch.getTitleTextColor());
                                                                    ((CircularImageView) view).setBorderColor(color);
                                                                }
                                                            }
                                                        });

                                                    }
                                                }
                                            });
                                    //((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);

                                } catch (IllegalArgumentException e) {
                                }
                            }
                        }
                        break;
                        case PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY: {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_add_to_cal));

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    appCMSPresenter.getAppCalendarEvent().checkCalendarEventExistsAndAddEvent(data, appCMSPresenter);
                                }
                            });
                            if (appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate()) > 0) {
                                (view).setVisibility(VISIBLE);
                            } else {
                                (view).setVisibility(INVISIBLE);
                            }
                        }
                        break;
                        case IMAGE_SERIES_INDICATOR:
                            if (data.getType() != null && data.getType().equalsIgnoreCase(context.getString(R.string.app_cms_series_content_type))
                                    && (uiBlockName == UI_BLOCK_BUNDLE_DETAIL_01 || uiBlockName == UI_BLOCK_BUNDLE_DETAIL_02)) {
                                ((ImageView) view).setImageResource(R.drawable.series_indicator);
                            }
                            break;
                        case PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY:
                            if (data != null && data.getGist() != null) {
                                long remainingTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleStartDate(), "yyyy MMM dd HH:mm:ss");
                                long remainingEndTime = CommonUtils.getTimeIntervalForEvent(data.getGist().getScheduleEndDate(), "yyyy MMM dd HH:mm:ss");
                                if (remainingTime > 0 && data.getGist().getScheduleStartDate() > 0) {

                                } else if (remainingEndTime < 0 && data.getGist().getScheduleEndDate() > 0) {

                                } else {
                                    ((ImageView) view).setImageResource(R.drawable.neo_video_detail_play);
                                    ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    view.setElevation(2f);
                                    ((ImageView) view).getDrawable().setColorFilter(new PorterDuffColorFilter(appCMSPresenter.getGeneralTextColor(), PorterDuff.Mode.MULTIPLY));
                                    view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                                    view.setOnClickListener(v -> {
                                    });
                                }
                            }
                            break;

                        case WALLET_IMAGE_KEY: {
                            if (data != null) {
                                if (!TextUtils.isEmpty(childComponent.getTintColor()))
                                    ((ImageView) view).setColorFilter(Color.parseColor(childComponent.getTintColor()), android.graphics.PorterDuff.Mode.SRC_IN);

                                if (IdUtils.getID(childComponent.getId()) == R.id.rightArrowImage) {
                                    ((ImageView) view).setImageResource(R.drawable.right_arrow);
                                    break;
                                }
                                String imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getImageUrl(),
                                        childViewWidth,
                                        childViewHeight);
                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .placeholder(R.drawable.vid_image_placeholder_square).override(childViewWidth, childViewHeight);
                                Glide.with(context)
                                        .load(imageUrl)
                                        .apply(requestOptions)
                                        .into((ImageView) view);
                            }
                        }
                        break;
                    }  // end of switch (componentKey) {

                    if (componentKey == PAGE_BROWSE_CLASS_IMAGE_KEY) {
                        view.setBackground(ContextCompat.getDrawable(context, R.drawable.browse_test_image));
                    }

                } // end of case PAGE_IMAGE_KEY:
                break;
                case PAGE_PLAN_META_DATA_VIEW_KEY: {
                    if (view instanceof ViewPlansMetaDataView) {
                        ((ViewPlansMetaDataView) view).setData(data);
                    }

                    if (view instanceof SubscriptionMetaDataView) {
                        ((SubscriptionMetaDataView) view).setData(data);
                    }
                }
                break;
                case PAGE_PROGRESS_VIEW_KEY: {
                    if (view instanceof ProgressBar) {
                        ContentDatum historyData = null;
                        if (data != null && data.getGist() != null && data.getGist().getId() != null) {
                            historyData = appCMSPresenter.getUserHistoryContentDatum(data.getGist().getId());
                        }

                        int progress = 0;

                        if (historyData != null && appCMSPresenter.isUserLoggedIn()) {
                            data.getGist().setWatchedPercentage(historyData.getGist().getWatchedPercentage());
                            data.getGist().setWatchedTime(historyData.getGist().getWatchedTime());
                            if (historyData.getGist().getWatchedPercentage() > 0) {
                                progress = historyData.getGist().getWatchedPercentage();
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                ((ProgressBar) view).setProgress(progress);
                            } else {
                                long watchedTime = historyData.getGist().getWatchedTime();
                                long runTime = historyData.getGist().getRuntime();
                                if (watchedTime > 0 && runTime > 0) {
                                    long percentageWatched = (long) (((double) watchedTime / (double) runTime) * 100.0);
                                    progress = (int) percentageWatched;
                                    ((ProgressBar) view).setProgress(progress);
                                    set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                                    ((ProgressBar) view).invalidate();
                                } else {
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                                    ((ProgressBar) view).setProgress(0);
                                }
                            }
                        } else {
                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                        }
                    }

                }
                break;
                case PAGE_LABEL_KEY: {
                    setTypeFace(context,
                            appCMSPresenter,
                            jsonValueKeyMap,
                            childComponent,
                            view);
                    switch (componentKey) {

                        /*case PAGE_PLAN_TITLE_KEY: {
                            if (data.getName() != null)
                                ((TextView) view).setText(data.getName());
                        }
                        break;*/
                        case PAGE_PLAN_PRICEINFO_KEY: {
                            if (data != null) {
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

                                double recurringPaymentAmount = data.getPlanDetails()
                                        .get(planIndex).getRecurringPaymentAmount();
                                double strike_price = data.getPlanDetails()
                                        .get(planIndex).getStrikeThroughPrice();
                                String formattedRecurringPaymentAmount = context.getString(R.string.cost_with_fraction,
                                        recurringPaymentAmount);
                                if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
                                    formattedRecurringPaymentAmount = context.getString(R.string.cost_without_fraction,
                                            recurringPaymentAmount);
                                }

                                StringBuilder planAmt = new StringBuilder();
                                if (currency != null) {
                                    String currencySymbol = currency.getSymbol();
                                    planAmt.append(currencySymbol);
                                    planAmt.append("");
                                }
                                planAmt.append(formattedRecurringPaymentAmount);
                                planAmt.append("/-");
                                StringBuilder planDuration = new StringBuilder();
                                if (data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_monthly))) {
                                    planDuration.append(" ");
                                    planDuration.append(context.getString(R.string.forward_slash));
                                    planDuration.append(" ");
                                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                                        planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_month)));
                                    } else {
                                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                        planDuration.append(" ");
                                        if (data.getRenewalCyclePeriodMultiplier() > 1)
                                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_months)));
                                        else
                                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_month)));
                                    }
                                }
                                if (data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_yearly))) {
                                    planDuration.append(" ");
                                    planDuration.append(context.getString(R.string.forward_slash));
                                    planDuration.append(" ");
                                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                                        planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_year)));
                                    } else {
                                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                        planDuration.append(" ");
                                        if (data.getRenewalCyclePeriodMultiplier() > 1)
                                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_years)));
                                        else
                                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_year)));
                                    }
                                }
                                if (data.getRenewalCycleType().contains(context.getString(R.string.app_cms_plan_renewal_cycle_type_daily))) {
                                    planDuration.append(" ");
                                    planDuration.append(context.getString(R.string.forward_slash));
                                    planDuration.append(" ");
                                    if (data.getRenewalCyclePeriodMultiplier() == 1) {
                                        planDuration.append(context.getString(R.string.plan_type_day));
                                    } else {
                                        planDuration.append(data.getRenewalCyclePeriodMultiplier());
                                        planDuration.append(" ");
                                        if (data.getRenewalCyclePeriodMultiplier() > 1)
                                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_days)));
                                        else
                                            planDuration.append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.plan_type_day)));
                                    }
                                }
                                StringBuilder plan = new StringBuilder();
                                plan.append(planAmt.toString());
                                //plan.append(planDuration.toString());
                                ((TextView) view).setText(plan.toString());
                                if (strike_price == 0) {
                                    set.constrainWidth(view.getId(), (BaseView.getDeviceWidth() - (BaseView.getDeviceWidth() * 25 / 100)));
                                    ((TextView) view).setGravity(Gravity.CENTER);
                                } else {
                                    set.constrainWidth(view.getId(), (BaseView.getDeviceWidth() / 2 - ((BaseView.getDeviceWidth() * 25 / 100) / 2)));
                                    ((TextView) view).setGravity(Gravity.LEFT);
                                    set.setMargin(view.getId(), ConstraintSet.START, 30);
                                }
                                TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(((TextView) view), 1, 30, 2,
                                        TypedValue.COMPLEX_UNIT_DIP);
                            }
                        }
                        break;
                        case TEXT_VIEW_VIDEO_DURATION: {
                            if (data != null && data.getGist() != null) {
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
                                if (uiBlockName == UI_BLOCK_SHOW_DETAIL_06 && data.getGist().getPublishDate() != null) {
                                    runtimeText.append(", ");
                                    runtimeText.append(appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getPublishDate()), "MM.dd.yy"));
                            }
                            ((TextView) view).setText(runtimeText);
                            if (data.getGist() != null && data.getGist().getContentType() != null &&
                                    data.getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.media_type_series).toLowerCase())
                                    && data.getGist().getMediaType() != null
                                    && data.getGist().getMediaType().toLowerCase().equalsIgnoreCase(context.getString(R.string.media_type_series).toLowerCase())) {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }
                        }
                    }
                    break;
                    case PAGE_THUMBNAIL_DESCRIPTION_KEY: {
                        if (data != null && data.getGist() != null && data.getGist().getDescription() != null) {
                            int descLength = data.getGist().getDescription().length();
                            if ((blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_history_04)))) {
                                int dis_max_length = 0;
                                if (BaseView.isTablet(context))
                                    dis_max_length = context.getResources().getInteger(R.integer.app_cms_tablet_list_discription_length);
                                else
                                    dis_max_length = context.getResources().getInteger(R.integer.app_cms_history_list_discription_length);
                                if (descLength > dis_max_length) {
                                    ((TextView) view).setSingleLine(false);
                                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                    ((TextView) view).setMaxLines(3);
                                    ViewCreator.setMoreLinkInDescription(appCMSPresenter, ((TextView) view), data.getGist().getTitle(), data.getGist().getDescription(), dis_max_length, (Color.parseColor("#ffffff")));
                                } else
                                    ((TextView) view).setText(data.getGist().getDescription());
                            } else if ((blockName != null &&
                                    blockName.equalsIgnoreCase(context.getString(R.string.ui_block_showDetail_06)) ||
                                    blockName.equalsIgnoreCase(context.getString(R.string.ui_block_watchlist_03)) ||
                                    blockName.equalsIgnoreCase(context.getString(R.string.ui_block_search_04)))) {
                                int dis_max_length = 0;
                                if (BaseView.isTablet(context))
                                    dis_max_length = context.getResources().getInteger(R.integer.app_cms_show_details_discription_length);
                                else
                                    dis_max_length = context.getResources().getInteger(R.integer.app_cms_show_details_discription_length_news_mobile);
                                ((TextView) view).setLineSpacing(0,0.9f);
                                ((TextView) view).setPadding(0, -7, 0, 0);
                                if (descLength > dis_max_length) {
                                    ((TextView) view).setSingleLine(false);
                                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                    // ((TextView) view).setMaxLines(3);
                                    //  ViewCreator.setMoreLinkInDescription(appCMSPresenter, ((TextView) view), data.getGist().getTitle(), data.getGist().getDescription(), dis_max_length, (Color.parseColor("#ffffff")));
                                    CommonUtils.setMoreLinkInDescription(((TextView) view), data.getGist().getDescription(), dis_max_length);
                                } else
                                    ((TextView) view).setText(data.getGist().getDescription());
                            } else {
                                int dis_max_length = 0;
                                if (BaseView.isTablet(context))
                                    dis_max_length = context.getResources().getInteger(R.integer.app_cms_tablet_list_discription_length);
                                else
                                    dis_max_length = context.getResources().getInteger(R.integer.app_cms_list_discription_length);
                                if (descLength > dis_max_length) {
                                    ((TextView) view).setSingleLine(false);
                                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                    ((TextView) view).setMaxLines(3);
                                    ViewCreator.setMoreLinkInDescription(appCMSPresenter, ((TextView) view), data.getGist().getTitle(), data.getGist().getDescription(), dis_max_length, (Color.parseColor("#ffffff")));
                                } else
                                    ((TextView) view).setText(data.getGist().getDescription());
                            }
                        }
                    }
                    case DATE_SEPARATOR_VIEW_KEY:
                        break;
                    case PAGE_VIDEO_PUBLISHDATE_KEY: {
                        if (data != null && data.getGist() != null && data.getGist().getPublishDate() != null) {
                            long publishDateMillseconds = Long.parseLong(data.getGist().getPublishDate());
                            ((TextView) view).setText(appCMSPresenter.getDateFormat(publishDateMillseconds, "MM/dd/yyyy"));
                        }
                    }
                    break;

                        case PAGE_SUBSCRIBE_BTN_FOR_PLAN_KEY: {
                            view.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
                            view.setPadding(30, 0, 30, 0);
                            ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
                            if(metadataMap!=null&& metadataMap.getSubscribeNowCta()!=null) {
                                ((TextView) view).setText(metadataMap.getSubscribeNowCta());
                            }

                    }
                    break;


                    case PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY: {
                        if (appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate()) > 0) {
                            (view).setVisibility(VISIBLE);
                        } else {
                            (view).setVisibility(GONE);
                        }
                        if (data != null && data.getGist() != null
                                && data.getGist().getScheduleStartDate() > 0) {
                            long millisLeft = appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate());
                            int hour = Math.round((millisLeft) / 1000 / 60 / 60);
                            String time;
                            if (hour < 1) {
                                (view).setVisibility(VISIBLE);
                                stopRunnable();
                                startRunnable(((TextView) view), data, context, appCMSPresenter);
                            } else {
                                (view).setVisibility(GONE);
                            }
                            String remainingTime = CommonUtils.getDateFormatByTimeZone(data.getGist().getScheduleStartDate(), "hh:mm a");
                            ((TextView) view).setText(remainingTime);
                            view.setPadding(5, 5, 5, 5);
                            PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.backgroundTextViewColor));
                            gdDefault.setCornerRadius(5f);
                            view.setBackground(gdDefault);

                        }
                        if (appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate()) > 0) {
                            (view).setVisibility(VISIBLE);
                        } else {
                            (view).setVisibility(GONE);
                        }
                        if (data != null && data.getGist() != null
                                && data.getGist().getScheduleStartDate() > 0) {
                            long millisLeft = appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate());
                            int hour = Math.round((millisLeft) / 1000 / 60 / 60);
                            String time;
                            if (hour < 1) {
                                (view).setVisibility(GONE);
                            } else {
                                (view).setVisibility(VISIBLE);
                                stopRunnable();
                                time = appCMSPresenter.getDateFormat(data.getGist().getScheduleStartDate(), "hh:mm a");

                                ((TextView) view).setText(time);
                                //setTextViewStyle(((TextView) view), context);
                            }
                        }

                    }
                    break;
                    case PAGE_ARTICLE_TITLE_KEY: {
                        if (!TextUtils.isEmpty(data.getGist().getTitle())) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(2);
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setText(data.getGist().getTitle());

                            if (viewTypeKey == PAGE_AC_SEARCH_MODULE_KEY || jsonValueKeyMap.get(blockName) == AppCMSUIKeyType.UI_BLOCK_ARTICLE_01) {
                                ((TextView) view).setTextColor(
                                        Color.parseColor(CommonUtils.getColor(context, childComponent.getTextColor())));
                            } else {
                                ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            }

                        }
                    }
                    break;
                    case PAGE_ARTICLE_DESCRIPTION_KEY: {
                        if (!TextUtils.isEmpty(data.getGist().getDescription())) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setMaxLines(3);
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setText(data.getGist().getDescription());
                            if (viewTypeKey == PAGE_AC_SEARCH_MODULE_KEY || jsonValueKeyMap.get(blockName) == AppCMSUIKeyType.UI_BLOCK_ARTICLE_01) {
                                ((TextView) view).setTextColor(Color.parseColor(CommonUtils.getColor(context, childComponent.getTextColor())));
                            } else {
                                ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            }
                        }
                    }
                    break;
                    case PAGE_WATCHLIST_DURATION_UNIT_KEY: {
                        int duration = (int) (data.getGist().getRuntime() / 60);
                        if (duration == 0) {
                            duration = (int) (data.getGist().getRuntime());
                            ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getQuantityString(R.plurals.sec_duration_unit,
                                    duration, duration)).trim());
                        } else
                            ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getQuantityString(R.plurals.min_duration_unit,
                                    duration, duration)).trim());

                        ViewCreator.UpdateDownloadImageIconAction updateDownloadImageIconAction =
                                updateDownloadImageIconActionMap.get(data.getGist().getId());
                        if (updateDownloadImageIconAction != null) {
                            view.setClickable(true);
                            view.setOnClickListener(updateDownloadImageIconAction.getAddClickListener());
                        }
                        ((TextView) view).setTextColor(0x92000000 + appCMSPresenter.getGeneralTextColor());
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                    case PAGE_LINK_TITLE_GRID_KEY: {
                        if (data != null && data.getTrayTitle() != null) {
                            ((TextView) view).setText(data.getTrayTitle());
                        } else {
                            if (data != null && data.getCategoryTitle() != null) {
                                ((TextView) view).setText(data.getCategoryTitle());
                            }
//                            ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners,null));
//                            ((TextView) view).setTextColor(R.color.color_white);
                        }
                        ((TextView) view).setTextColor(Color.parseColor("#ffffff"));
                        ((TextView) view).setSingleLine(true);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                    case PAGE_DOWNLOAD_DURATION_KEY:
                    case PAGE_HISTORY_DURATION_KEY:
                    case PAGE_WATCHLIST_DURATION_KEY: {
                        final int SECONDS_PER_MINS = 60;
                        if ((data.getGist().getRuntime() / SECONDS_PER_MINS) < 2) {
                            StringBuilder runtimeText = new StringBuilder()
                                    .append(data.getGist().getRuntime() / SECONDS_PER_MINS);
                            if (componentKey != PAGE_EPISODE_DURATION_KEY) {
                                runtimeText.append(" ")
                                        //min value is being set in unit tag under PAGE_WATCHLIST_DURATION_UNIT_KEY component key so removing
                                        //unit abbrevation from here .Its causing double visibilty of time unit
                                        .append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.min_abbreviation)).trim());
                            }
                            ((TextView) view).setText(runtimeText);
                            view.setVisibility(View.VISIBLE);

                            } else {
                                StringBuilder runtimeText = new StringBuilder()
                                        .append(data.getGist().getRuntime() / SECONDS_PER_MINS);
                                if (componentKey != PAGE_EPISODE_DURATION_KEY) {
                                    runtimeText.append(" ")
                                            .append(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.mins_abbreviation)));
                                }
                                ((TextView) view).setText(runtimeText);
                                view.setVisibility(View.VISIBLE);
                            }
                            ((TextView) view).setTextColor(0x92000000 + appCMSPresenter.getGeneralTextColor());
                        }
                        break;
                        case PAGE_API_TITLE:
                        case PAGE_EPISODE_TITLE_KEY: {
                            if (data.getGist() != null && data.getGist().getTitle() != null) {
                                if (data.getSeasonEpisodeNum() != null && !TextUtils.isEmpty(data.getSeasonEpisodeNum())) {
                                    ((TextView) view).setText(appCMSPresenter.getEpisodeTitlewithNumber(data.getGist().getTitle(), data.getSeasonEpisodeNum()));
                                } else if (data.getGist().getEpisodeNum() != null
                                        && !TextUtils.isEmpty(data.getGist().getEpisodeNum()) && moduleType != PAGE_DOWNLOAD_01_MODULE_KEY) {
                                    ((TextView) view).setText(appCMSPresenter.getEpisodeTitlewithNumber(data.getGist().getTitle(), data.getGist().getEpisodeNum()));
                                } else {
                                    ((TextView) view).setText(data.getGist().getTitle());
                                }
                                ((TextView) view).setSingleLine(true);
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                                view.setVisibility(View.VISIBLE);
                                if (componentKey == PAGE_EPISODE_TITLE_KEY) {
                                    view.setPadding(0, -dpToPx(3), 0, 0);
                                }
//                            ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners,null));
//                            ((TextView) view).setTextColor(R.color.color_white);
                        } else if (data != null && data.getPages() != null && data.getPages().size() > 0) {
                            for (int i = 0; i < data.getPages().size(); i++) {
                                ((TextView) view).setText(data.getPages().get(i).getTitle());
                            }
                        }
                    }
                    case PAGE_LINK_ITEM_TITLE_GRID_KEY: {
//                            if (data.getGist() != null && data.getGist().getOgDetails() != null
//                                    && data.getGist().getOgDetails().getTitle() != null) {
//                                ((TextView) view).setTextColor(R.color.color_white);
//                                ((TextView) view).setText(data.getGist().getOgDetails().getTitle());
//
//                            }

                        if (data != null && data.getPages() != null && data.getPages().size() > 0) {
                            for (int i = 0; i < data.getPages().size(); i++) {
                                if (data.getPages().get(i).getOgDetails() != null) {
                                    try {
                                        ((TextView) view).setText(data.getPages().get(i).getOgDetails().getTitle());
                                        ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }

                    }

                    break;
                    case PAGE_API_EPISODE_DESCRIPTION: {
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setSingleLine(false);
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());

                            }
                            ((TextView) view).setText(data.getGist().getDescription());
                            ((TextView) view).setTextColor(0x92000000 + appCMSPresenter.getGeneralTextColor());
                            view.setVisibility(View.VISIBLE);
                            String title = data.getGist().getTitle();
                            if (data.getSeasonEpisodeNum() != null) {
                                title = data.getSeasonEpisodeNum() + " " + title;
                            }
                            if (uiBlockName != null && uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_07) {
                                int spanCount = 50;
                                try {
                                    for (int charPostion = 0; charPostion < 50; charPostion++) {
                                        if (data.getGist().getDescription().charAt(charPostion) == '\n') {
                                            spanCount = charPostion + 10;
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ViewCreator.setMoreLinkInDescription(appCMSPresenter, (TextView) view, title, data.getGist().getDescription(), spanCount, (0x92000000 + appCMSPresenter.getGeneralTextColor()));
                            } else
                                ViewCreator.setMoreLinkInDescription(appCMSPresenter, (TextView) view, title, data.getGist().getDescription(), 100, (0x92000000 + appCMSPresenter.getGeneralTextColor()));
                            /*try {
                                ViewTreeObserver titleTextVto = view.getViewTreeObserver();
                                ViewCreatorMultiLineLayoutListener viewCreatorTitleLayoutListener =
                                        new ViewCreatorMultiLineLayoutListener((TextView) view,
                                                data.getGist().getTitle(),
                                                data.getGist().getDescription(),
                                                appCMSPresenter,
                                                false,
                                                appCMSPresenter.getBrandPrimaryCtaColor(),
                                                appCMSPresenter.getGeneralTextColor(),
                                                true);
                                viewCreatorTitleLayoutListener.setComponent(childComponent);
                                titleTextVto.addOnGlobalLayoutListener(viewCreatorTitleLayoutListener);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                    }
                    break;

                    case PAGE_WATCHLIST_DURATION_KEY_BG: {
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
                        if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter) || duration == 0) {
                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                        }  //View is invisible if ContentType is Seriese
                    }
                    break;
                    case PAGE_API_DESCRIPTION: {
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setSingleLine(false);

                        }
                        ((TextView) view).setText(data.getGist().getDescription());
                        if (blockName.contains("watchlist")) {
                            ViewCreator.setMoreLinkInDescription(appCMSPresenter,
                                    (TextView) view,
                                    data.getGist().getTitle(),
                                    data.getGist().getDescription(),
                                    BaseView.isTablet(context) ? 200 : 60,
                                    (0x92000000 + appCMSPresenter.getGeneralTextColor()));
                        }

                    }
                    break;
                    case PAGE_PLAN_STRIKE_OUT_PRICEINFO_KEY: {
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
                            double strikeThroughPaymentAmount = data.getPlanDetails()
                                    .get(planIndex).getStrikeThroughPrice();

                                String formattedStrikeThroughPaymentAmount = context.getString(R.string.cost_with_fraction,
                                        strikeThroughPaymentAmount);
                                if (strikeThroughPaymentAmount - (int) strikeThroughPaymentAmount == 0) {
                                    formattedStrikeThroughPaymentAmount = context.getString(R.string.cost_without_fraction,
                                            strikeThroughPaymentAmount);
                                }
                                ((TextView) view).setText(Html.fromHtml(currency.getSymbol() + "" + formattedStrikeThroughPaymentAmount));

                                ((TextView) view).setPaintFlags(((TextView) view).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                set.setVisibility(view.getId(), View.VISIBLE);
                                ((TextView) view).setGravity(Gravity.RIGHT);
                            } else {
                                set.setVisibility(view.getId(), View.GONE);
                            }
                            if (appCMSPresenter.getAppCMSMain() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getFooter().getBackgroundColor() != null)
                                ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getFooter().getTextColor()));
                            else {
                                ((TextView) view).setTextColor(context.getResources().getColor(R.color.zui_color_red_300));
                            }

                        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(((TextView) view), 1, 30, 2,
                                TypedValue.COMPLEX_UNIT_DIP);
                    }
                    break;

                    case PAGE_PLAN_TITLE_KEY: {
                        if (data.getPlanDetails() != null
                                && data.getPlanDetails().size() > 0
                                && data.getPlanDetails().get(0).getTitle() != null
                                && !TextUtils.isEmpty(data.getPlanDetails().get(0).getTitle())) {
                            ((TextView) view).setText(data.getPlanDetails().get(0).getTitle());
                        } else {
                            ((TextView) view).setText(data.getName());
                        }
                        ((TextView) view).setMaxLines(1);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setSingleLine(true);
                        if (componentType.equals(PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY) ||
                                componentType.equals(PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY)) {
                            ((TextView) view).setTextColor(themeColor);
                        } else {
                            ((TextView) view).setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
                        }
                        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(((TextView) view), 1, 26, 2,
                                TypedValue.COMPLEX_UNIT_DIP);
                    }
                    break;
                    case PAGE_HOME_TITLE_DESCRIPTION: {
                            /*if (data != null &&
                                    data.getTags() != null && brandAndTitle == null ) {
                                handleTagData(data.getTags());
                            }*/
                        brandAndTitle = brandAndTitle == null ? "" : brandAndTitle;
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getTitle() != null
                                && !brandAndTitle.contains(data.getGist().getTitle())) {
                            //brandAndTitle = brandAndTitle + " • " + data.getGist().getTitle();
                            brandAndTitle = data.getGist().getTitle();
                        }
                        ((TextView) view).setText(brandAndTitle);
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        ((TextView) view).setSingleLine();
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    } //end of PAGE_HOME_TITLE_DESCRIPTION:
                    break;
                    case PAGE_CAROUSEL_INFO_KEY:
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
                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                        } else {
                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                        }
                        if (isTablet(view.getContext()) && isLandscape(context)
                                && !(blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04)))) {
                            ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                        } else if (childComponent.getTextColor() != null &&
                                !TextUtils.isEmpty(childComponent.getTextColor())) {
                            ((TextView) view).setTextColor(Color.parseColor(
                                    childComponent.getTextColor()));
                        } else {
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        }

                        break;
                    case PAGE_CAROUSEL_TITLE_KEY:
                    case PAGE_THUMBNAIL_TITLE_KEY:
                    case PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY:
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getTitle() != null) {
                            ((TextView) view).setText(data.getGist().getTitle());
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            if (componentKey.equals(PAGE_CAROUSEL_TITLE_KEY)) {
                                ((TextView) view).setSingleLine();
                                //((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                                if (BaseView.isTablet(view.getContext()) && BaseView.isLandscape(getContext())
                                        && !(blockName != null && blockName.equalsIgnoreCase(context.getString(R.string.ui_block_carousel_04)))) {
                                    if (appCMSPresenter.getBrandSecondaryCtaTextColor() != 0) {
                                        ((TextView) view).setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                                    } else {
                                        ((TextView) view).setTextColor(Color.parseColor(
                                                childComponent.getTextColor()));
                                    }
                                } else if (childComponent.getTextColor() != null) {
                                    ((TextView) view).setTextColor(Color.parseColor(
                                            childComponent.getTextColor()));
                                } else {
                                    ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                                }
                                    /*if (appCMSPresenter.isAFAApp())
                                        ((TextView) view).setTextColor(ContextCompat.getColor(context, android.R.color.white));*/
                            } else if (childComponent.getNumberOfLines() == 1) {
                                ((TextView) view).setSingleLine();
                                ((TextView) view).setMaxLines(1);
                                ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            } else {
                                ((TextView) view).setMaxLines(2);
                            }

                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            if (component.getComponents().size() > 3) {
                                if (BaseView.isTablet(context)) {
                                    if (BaseView.isLandscape(context)) {
                                        if (componentViewType.contains("AC Carousel 01")
                                                || componentViewType.contains("AC Carousel 04")
                                                || componentViewType.contains("AC FullWidthCarousel 01")) {
                                            int width = BaseView.getDeviceWidth() - BaseView.getDeviceHeight();
                                            ((TextView) view).setWidth(width);
                                            ((TextView) view).setGravity(Gravity.CENTER);
                                        }
                                        if (childComponent.getLayout().getTabletLandscape().getFontSize() != 0)
                                            ((TextView) view).setTextSize(childComponent.getLayout().getTabletLandscape().getFontSize());
                                    } else {
                                        if (childComponent.getLayout().getTabletPortrait().getFontSize() != 0)
                                            ((TextView) view).setTextSize(childComponent.getLayout().getTabletPortrait().getFontSize());
                                    }
                                } else {
                                    if (childComponent.getLayout().getMobile().getFontSize() != 0)
                                        ((TextView) view).setTextSize(childComponent.getLayout().getMobile().getFontSize());
                                }
                            }
                        }
                        break;
                    case PAGE_PLAYLIST_AUDIO_ARTIST_TITLE:

                        if (data != null &&
                                data.getGist() != null &&
                                data.getCreditBlocks() != null && data.getCreditBlocks().size() > 0) {
                            String artist = appCMSPresenter.getArtistNameFromCreditBlocks(data.getCreditBlocks());

                            if (data.getCreditBlocks().get(0) != null && data.getCreditBlocks().get(0).getCredits() != null && data.getCreditBlocks().get(0).getCredits().size() > 0) {
                                ((TextView) view).setText(data.getCreditBlocks().get(0).getCredits().get(0).getTitle());
                            } else if (!TextUtils.isEmpty(artist)) {
                                ((TextView) view).setText(artist);
                            }
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            ((TextView) view).setSingleLine();
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);

                        } else {
                            set.setVisibility(view.getId(), ConstraintSet.GONE);
                        }
                        break;
                    case PAGE_HOME_TITLE_THUMBNAIL:
                    case PAGE_HOME_DURATION_CAROUSEL: {
                        if (data != null &&
                                data.getTags() != null && durationAndCategory == null) {
                            handleTagData(data.getTags());
                        }
                        durationAndCategory = durationAndCategory == null ? "" : durationAndCategory;
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getPrimaryCategory() != null &&
                                data.getGist().getPrimaryCategory().getTitle() != null
                                && !durationAndCategory.contains(data.getGist().getPrimaryCategory().getTitle())) {
                            if (!durationAndCategory.equalsIgnoreCase("0M") && durationAndCategory.length() != 0 && durationAndCategory != null)
                                durationAndCategory = durationAndCategory + " • " + data.getGist().getPrimaryCategory().getTitle();
                            else
                                durationAndCategory = data.getGist().getPrimaryCategory().getTitle();
                        }
                        ((TextView) view).setText(durationAndCategory);
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        if (componentKey == PAGE_HOME_DURATION_CAROUSEL) {
                            view.setPadding(5, 5, 5, 5);
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            if (moduleType != PAGE_WEEK_SCHEDULE_GRID_KEY) {
                                PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.backgroundTextViewColor));
                                gdDefault.setCornerRadius(5f);
                                view.setBackground(gdDefault);
                            }
                        }

                    } // end of case PAGE_HOME_DURATION_CAROUSEL:
                    break;
                    case PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY: {
                        if (data != null && data.getGist() != null) {
                            ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.live_now)));
                            ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLive));
                            view.setPadding(10, 10, 10, 10);
                            PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.colorLive));
                            gdDefault.setCornerRadius(5f);
                            view.setBackground(gdDefault);

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    appCMSPresenter.launchVideoPlayer(data,
                                            data.getGist().getId(),
                                            0, data.getContentDetails().getRelatedVideoIds(),
                                            data.getGist().getWatchedTime(),
                                            component.getAction());
                                }
                            });

                            if (appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate()) <= 0 && appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleEndDate()) > 0) {
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            } else {
                                set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            }
                        }

                    } // end of case PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY:
                    break;
                    case PAGE_GRID_THUMBNAIL_INFO:
                    case PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO:
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                        ((TextView) view).setTextColor(Color.WHITE);
                        view.setBackgroundColor(0x92000000 + Color.BLACK);
                        ((TextView) view).setGravity(Gravity.START);
                        view.setPadding(20, 5, 20, 5);
                        ((TextView) view).setMaxLines(1);

                        String thumbInfoStr = null;
                        if (data.getGist() != null && data.getGist().getMediaType() != null && data.getGist().getMediaType().toLowerCase().contains(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())) {
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
                                    && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayDuration()) {
                                String thumbInfo = null;
                                if (data.getGist().getPublishDate() != null) {
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
                                    } else {
                                        ((TextView) view).setVisibility(GONE);
                                        ((TextView) view).setBackground(null);
                                    }
                                }

                                }
                            } else if (uiBlockName == UI_BLOCK_TRAY_02 && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayPublishedDate()) {

                                if (data.getGist().getPublishDate() != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                                        && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayPublishedDate()) {
                                    try {
                                        thumbInfoStr = appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getPublishDate()), "MMM dd");
                                    } catch (NumberFormatException e) {
                                        thumbInfoStr = appCMSPresenter.getDateFormat(CommonUtils.getMillisecondFromDateString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", data.getGist().getPublishDate()),
                                                "MMM dd");
                                    }
                                }
                                ((TextView) view).setText(thumbInfoStr);

                            int visibility = TextUtils.isEmpty(thumbInfoStr) ? ConstraintSet.INVISIBLE : ConstraintSet.VISIBLE;
                            set.setVisibility(view.getId(), visibility);

                            } else if (uiBlockName == UI_BLOCK_TRAY_02 && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getBrand() != null && appCMSPresenter.getAppCMSMain().getBrand().getMetadata() != null
                                    && appCMSPresenter.getAppCMSMain().getBrand().getMetadata().isDisplayDuration()) {

                            long runtime = data.getGist().getRuntime();
                            if (thumbInfoStr != null && runtime > 0) {
                                ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime) + " | " + thumbInfoStr);
                            } else {
                                if (thumbInfoStr != null) {
                                    ((TextView) view).setText(thumbInfoStr);
                                } else if (runtime > 0) {
                                    ((TextView) view).setText(appCMSPresenter.convertSecondsToTime(runtime));
                                }
                            }

                            int visibility = (TextUtils.isEmpty(thumbInfoStr) && runtime <= 0) ? ConstraintSet.INVISIBLE : ConstraintSet.VISIBLE;
                            set.setVisibility(view.getId(), visibility);

                        } else {

                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                        }

                        break;
                    case PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY:
                        ((TextView) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                        ((TextView) view).setGravity(Gravity.CENTER);
                        if (data != null && data.getGist() != null && data.getGist().getScheduleStartDate() != 0)
                            ((TextView) view).setText(String.format("%s ", appCMSPresenter.getDateFormat(data.getGist().getScheduleStartDate(), "hh:mm a")));
                        break;
                    case PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY:
                        ((TextView) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setGravity(Gravity.START);
                        ((TextView) view).setText(data.getGist().getTitle());
                        break;
                    case PAGE_LIVE_SCHEDULE_TAG_CLASS_TITLE_KEY:
                        StringBuilder stringBuilder = new StringBuilder();
                        if (data != null) {
                            if (data.getGist() != null
                                    && data.getGist().getFeaturedTag() != null
                                    && data.getGist().getFeaturedTag().getTitle() != null) {
                                stringBuilder.append(data.getGist().getFeaturedTag().getTitle());
                            }
                            if (data.getGist() != null
                                    && data.getGist().getPersons() != null
                                    && data.getGist().getPersons().get(0) != null
                                    && data.getGist().getPersons().get(0).getTitle() != null) {
                                if (stringBuilder.length() > 0)
                                    stringBuilder.append(" with ");
                                stringBuilder.append(data.getGist().getPersons().get(0).getTitle());
                            }
                        }
                        ((TextView) view).setText(stringBuilder);
                        ((TextView) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setGravity(Gravity.START);
                        break;
                    case PAGE_TRAY_BOTTOM_TEXT1_KEY: {
                        String category = null;
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getPrimaryCategory() != null &&
                                data.getGist().getPrimaryCategory().getTitle() != null) {
                            category = data.getGist().getPrimaryCategory().getTitle();
                        }
                        if (category == null) {
                            view.setVisibility(INVISIBLE);
                        } else {
                            ((TextView) view).setText(category);
                            ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                            ((TextView) view).setGravity(Gravity.CENTER);
                            view.setPadding(10, 0, 10, 0);
                            PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.backgroundTextViewColor));
                            gdDefault.setCornerRadius(10f);
                            view.setBackground(gdDefault);
                        }
                    }
                    break;
                    case PAGE_THUMBNAIL_TIME_AND_CATEGORY:
                        if (data != null &&
                                data.getTags() != null && durationAndCategory == null) {
                            handleTagData(data.getTags());
                        }
                        durationAndCategory = durationAndCategory == null ? "" : durationAndCategory;
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getPrimaryCategory() != null &&
                                data.getGist().getPrimaryCategory().getTitle() != null
                                && !durationAndCategory.contains(data.getGist().getPrimaryCategory().getTitle())) {
                            if (!durationAndCategory.equalsIgnoreCase("0M") && durationAndCategory.length() != 0 && durationAndCategory != null)
                                durationAndCategory = durationAndCategory + " • " + data.getGist().getPrimaryCategory().getTitle();
                            else
                                durationAndCategory = data.getGist().getPrimaryCategory().getTitle();
                        }
                        ((TextView) view).setText(durationAndCategory);
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        break;
                    case PAGE_THUMBNAIL_BRAND_AND_TITLE: {
                        brandAndTitle = brandAndTitle == null ? "" : brandAndTitle;
                        if (data != null &&
                                data.getGist() != null &&
                                data.getGist().getTitle() != null
                                && !brandAndTitle.contains(data.getGist().getTitle())) {
                            brandAndTitle = TextUtils.isEmpty(brandAndTitle) ? data.getGist().getTitle() : brandAndTitle + " • " + data.getGist().getTitle();
                            //brandAndTitle =  data.getGist().getTitle();
                        }
                        ((TextView) view).setText(brandAndTitle);
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());
                        ((TextView) view).setSingleLine();
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);


                        ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
                    } // end of PAGE_THUMBNAIL_BRAND_AND_TITLE
                    break;
                    case PAGE_LIVE_SCHEDULE_ITEM_TITLE_KEY: {
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

                        ((TextView) view).setText(text.toUpperCase());
                        ((TextView) view).setTextSize(getFontSize(context, childComponent.getLayout()));
                        ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.color_white));
                        ((TextView) view).setGravity(Gravity.START);
                        ((TextView) view).setSingleLine(true);
                    }// end of PAGE_LIVE_SCHEDULE_ITEM_TITLE_KEY

                    break;

                    case WALLET_TITLE_KEY: {
                        if (!TextUtils.isEmpty(childComponent.getBackgroundColor()))
                            view.setBackgroundColor(Color.parseColor(childComponent.getBackgroundColor()));
                        if (childComponent.getPadding() > 0) {
                            int padding = (int) BaseView.convertDpToPixel(childComponent.getPadding(), context);
                            view.setPadding(padding, padding, padding, padding);
                        }

                        if (data != null && !TextUtils.isEmpty(data.getTitle()))
                            ((TextView) view).setText(data.getTitle());
                    }

                    break;

                    case WALLET_OFFER_TEXT: {
                        if (!TextUtils.isEmpty(childComponent.getBackgroundColor()))
                            view.setBackgroundColor(Color.parseColor(childComponent.getBackgroundColor()));
                        if (childComponent.getPadding() > 0) {
                            int padding = (int) BaseView.convertDpToPixel(childComponent.getPadding(), context);
                            view.setPadding(padding, padding, padding, padding);
                        }

                        if (data != null && !TextUtils.isEmpty(data.getDescription())) {
                            ((TextView) view).setText(data.getDescription());
                            set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                        } else {
                            set.setVisibility(view.getId(), ConstraintSet.GONE);
                        }
                    }

                    break;
                }
            }

            break;
            case PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY: {

                    //  ((ImageButton) componentViewResult.componentView).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    if ((data != null &&
                            data.getGist() != null &&
                            data.getGist().isLiveStream()) ||
                            !appCMSPresenter.isDownloadEnable()) {
                        ((DownloadComponent) view).setChildVisibility(INVISIBLE);
                    } else {
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
                                    set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
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

                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            //break;

                        } else if (ViewCreator.isVideoIsSchedule(data, appCMSPresenter)) {

                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                            // break;

                        }

                        if (appCMSPresenter.isDownloadEnable()) {
                            if (data.isDRMEnabled()) {
                                appCMSPresenter.getUserOfflineVideoDownloadStatus(data.getGist().getId(), new FetchOffineDownloadStatus((DownloadComponent) view, appCMSPresenter,
                                        data, userId, 0,
                                        data.getGist().getId()), userId);
                            } else {
                                appCMSPresenter.getUserVideoDownloadStatus(
                                        data.getGist().getId(), new DownloadComponent.UpdateDownloadImageIconAction((DownloadComponent) view, appCMSPresenter,
                                                data, userId), userId);
                                set.setVisibility(view.getId(), ConstraintSet.VISIBLE);
                            }
                        } else {
                            set.setVisibility(view.getId(), ConstraintSet.INVISIBLE);
                        }

                    }
                }
            }// end PAGE_VIDEO_DOWNLOAD_COMPONENT_KEY
            case PAGE_BUTTON_KEY:
                switch (componentKey) {

                    case PAGE_DELETE_DOWNLOAD_KEY:
                    case PAGE_DELETE_WATCHLIST_KEY:
                    case PAGE_DELETE_HISTORY_KEY:
                    case PAGE_WATCHLIST_DELETE_ITEM_BUTTON: {
                        view.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_deleteicon));
                        view.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                        view.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        ((ImageButton) view).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                    break;
                    case PAGE_PLAY_KEY:
                    case PAGE_PLAY_IMAGE_KEY: {
                        int padding = 40;
                        ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && data.getMonetizationModels() != null) {
                            boolean isUserSubscribed = appCMSPresenter.isUserSubscribed();
                            boolean isTveUser = appCMSPresenter.getAppPreference().getTVEUserId() != null;
                            boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && (appCMSPresenter.getAppPreference().getUserPurchases() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                                    && (contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), data.getGist().getId())
                                    || (data.getSeasonId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), data.getSeasonId()))
                                    || (data.getSeriesId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), data.getSeriesId()))));

                                if (contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels()) &&
                                        contentTypeChecker.isContentTVEMonetization(data.getMonetizationModels()) &&
                                        contentTypeChecker.isContentTVODMonetization(data.getMonetizationModels())) {
                                    if (!isContentPurchased && !isUserSubscribed && !isTveUser) {
                                        ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                        padding = 30;
                                    }
                                } else if (contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels()) &&
                                        contentTypeChecker.isContentTVEMonetization(data.getMonetizationModels())) {
                                    if (!isUserSubscribed && !isTveUser) {
                                        ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                        padding = 30;
                                    }
                                } else if (contentTypeChecker.isContentTVEMonetization(data.getMonetizationModels()) &&
                                        contentTypeChecker.isContentTVODMonetization(data.getMonetizationModels())) {
                                    if (!isTveUser && !isContentPurchased) {
                                        ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                        padding = 30;
                                    }
                                } else if (contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels()) &&
                                        contentTypeChecker.isContentTVODMonetization(data.getMonetizationModels())) {
                                    if (!isContentPurchased && !isUserSubscribed) {
                                        ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                        padding = 30;
                                    }
                                } else if (contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels()) &&
                                        contentTypeChecker.isContentFREEMonetization(data.getMonetizationModels())) {
                                    ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.play_icon));
                                } else if (contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels()) && !isUserSubscribed) {
                                    ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                    padding = 30;
                                } else if (contentTypeChecker.isContentTVEMonetization(data.getMonetizationModels()) && !isTveUser) {
                                    ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                    padding = 30;
                                } else if (contentTypeChecker.isContentTVODMonetization(data.getMonetizationModels()) && !isContentPurchased) {
                                    ((ImageButton) view).setBackground(ContextCompat.getDrawable(context, R.drawable.ic_lock));
                                    padding = 30;
                                }
                            }
                            view.setPadding(padding, padding, padding, padding);
                            view.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                            view.getBackground().setTintMode(PorterDuff.Mode.SRC_IN);
                        }
                        break;
                        case PAGE_EXPAND_DETAILS_KEY:
                            view.setPadding(1, 80, 5, 0);
                            int dis_max_length = 0;
                            if (BaseView.isTablet(context))
                                dis_max_length = context.getResources().getInteger(R.integer.app_cms_show_details_discription_length);
                            else
                                dis_max_length = context.getResources().getInteger(R.integer.app_cms_show_details_discription_length_news_mobile);
                            if (data != null && data.getGist() != null &&
                                    data.getGist().getDescription() != null &&
                                    !data.getGist().getDescription().isEmpty() &&
                                    data.getGist().getDescription().length() > dis_max_length) {

                            ((ImageButton) view).setVisibility(VISIBLE);
                            ((ImageButton) view).setImageResource(R.drawable.ic_arrow_down);
                            ((ImageButton) view).setBackground(null);

                        } else {
                            ((ImageButton) view).setImageResource(0);
                            ((ImageButton) view).setVisibility(GONE);
                        }
                        break;
                    case PAGE_GRID_OPTION_KEY: {
                        view.setBackground(ContextCompat.getDrawable(context, R.drawable.dots_more));
                        view.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
                        view.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        appCMSPresenter.setMoreIconAvailable();
                        view.setLayoutParams(new Constraints.LayoutParams(20, 20));
                        set.setDimensionRatio(view.getId(), "1:1");

                        view.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (data != null && data.getGist() != null
                                        && data.getGist().getPermalink() != null) {
                                    if (!appCMSPresenter.launchButtonSelectedAction(data.getGist().getPermalink(),
                                            childComponent.getAction(),
                                            data.getGist().getTitle(),
                                            null,
                                            data,
                                            false,
                                            -1,
                                            null)) {
                                        //Log.e(TAG, "Could not launch action: " +
//                                                " permalink: " +
//                                                permalink +
//                                                " action: " +
//                                                action);
                                    }
                                }
                            }
                        });

                    }
                    break; // end of PAGE_GRID_OPTION_KEY

                }
                break;
        }// end of switch (componentType)
        //setViewPotion(context, set, childComponent, view);
    }
        set.applyTo(this);
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

    public List<ItemContainer> getChildItems() {
        return childItems;
    }

    public Component matchComponentToView(View view) {
        for (ConstraintCollectionGridItemView.ItemContainer itemContainer : childItems) {
            if (itemContainer.childView == view) {
                return itemContainer.component;
            }
        }
        return null;
    }

    String durationAndCategory = null;
    String brandAndTitle = null;

    public void handleTagData(List<Tag> tags) {
        for (int i = 0; i < tags.size(); i++) {
            if (tags.get(i) != null &&
                    tags.get(i).getTagType() != null &&
                    tags.get(i).getTitle() != null) {
                if (tags.get(i).getTagType().equals("classDuration")) {
                    durationAndCategory = tags.get(i).getTitle().replaceAll("\\s+", "").replaceAll("min", "M").replaceAll("MIN", "M");
                }
                if (tags.get(i).getTagType().equals("brand")) {
                    brandAndTitle = tags.get(i).getTitle();
                }
            }
        }
    }

    public void startRunnable(TextView view, ContentDatum data, Context context, AppCMSPresenter appCMSPresenter) {
        handler = new Handler();
        r = new Runnable() {
            long millisLeft;

            public void run() {
                millisLeft = appCMSPresenter.getTimeLeftToStartEvent(data.getGist().getScheduleStartDate());

                String hms = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisLeft) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisLeft)),
                        TimeUnit.MILLISECONDS.toSeconds(millisLeft) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisLeft)));
                if (millisLeft <= 0) {
                    handler.removeCallbacks(r);
                    view.setVisibility(INVISIBLE);
                } else {
                    handler.postDelayed(r, 1000);
                }
                view.setText("STARTS IN " + hms);
                //setTextViewStyle(((TextView) view), context);
            }
        };
        handler.postDelayed(r, 0000);
    }

    private void stopRunnable() {
        if (handler != null && r != null) {
            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    public BitmapDrawable convertVectorDrawableToBitmapDrawable(final @NonNull Drawable vd, int width, int height) {
        return new BitmapDrawable(getContext().getResources(), createBitmapFromVectorDrawable(vd, width, height));
    }

    public Bitmap createBitmapFromVectorDrawable(final @NonNull Drawable vd, int width, int height) {
        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(vd.getIntrinsicWidth(), vd.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vd.setBounds(0, 0, width, height);
        vd.draw(canvas);
        return bitmap;

    }


    private void createTextviewsForCategoryConceptTray(Context context, String title, LinearLayout view, AppCMSPresenter appCMSPresenter) {
        TextView category = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 10);
        category.setLayoutParams(layoutParams);
        category.setText(title);
        category.setTextColor(appCMSPresenter.getGeneralTextColor());
        category.setEllipsize(TextUtils.TruncateAt.END);
        category.setGravity(Gravity.CENTER);
        category.setPadding(10, 0, 10, 0);
        PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(context, R.color.backgroundTextViewColor));
        gdDefault.setCornerRadius(10f);
        category.setBackground(gdDefault);
        view.addView(category);
        Component component = new Component();
        setTypeFace(context,
                appCMSPresenter,
                appCMSPresenter.getJsonValueKeyMap(),
                component,
                category);
    }

    protected float getTrayPaddingHorizontal(Context context, Layout layout) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTrayPadding() != 0f) {
                        return layout.getTabletLandscape().getTrayPadding();
                    }
                } else {
                    if (layout.getTabletPortrait().getTrayPadding() != 0f) {
                        return layout.getTabletPortrait().getTrayPadding();
                    }
                }
            } else {
                if (layout.getMobile().getTrayPadding() != 0f) {
                    return layout.getMobile().getTrayPadding();
                }
            }
        }
        return -1.0f;
    }

    protected float getTrayPaddingVertical(Context context, Layout layout) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTrayPaddingVertical() != 0f) {
                        return layout.getTabletLandscape().getTrayPaddingVertical();
                    }
                } else {
                    if (layout.getTabletPortrait().getTrayPaddingVertical() != 0f) {
                        return layout.getTabletPortrait().getTrayPaddingVertical();
                    }
                }
            } else {
                if (layout.getMobile().getTrayPaddingVertical() != 0f) {
                    return layout.getMobile().getTrayPaddingVertical();
                }
            }
        }
        return -1.0f;
    }


    public int getTrayHorizontalMargin(Context context, Layout layout, int defaultMargin) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTrayMarginHorizontal() != 0) {
                        return layout.getTabletLandscape().getTrayMarginHorizontal();
                    }
                } else {
                    if (layout.getTabletPortrait().getTrayMarginHorizontal() != 0) {
                        return layout.getTabletPortrait().getTrayMarginHorizontal();
                    }
                }
            } else {
                if (layout.getMobile().getTrayMarginHorizontal() != 0) {
                    return layout.getMobile().getTrayMarginHorizontal();
                }
            }
        }
        return defaultMargin;
    }

    public int getTrayVerticalMargin(Context context, Layout layout, int defaultMargin) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTrayMarginVertical() != 0) {
                        return layout.getTabletLandscape().getTrayMarginVertical();
                    }
                } else {
                    if (layout.getTabletPortrait().getTrayMarginVertical() != 0) {
                        return layout.getTabletPortrait().getTrayMarginVertical();
                    }
                }
            } else {
                if (layout.getMobile().getTrayMarginVertical() != 0) {
                    return layout.getMobile().getTrayMarginVertical();
                }
            }
        }
        return defaultMargin;
    }


    private Drawable resizePlaceholder(Drawable image, int width, int height) {
        image.setBounds(0, 0, width, height);
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        // Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        Drawable d = new BitmapDrawable(getResources(), b);
        return d;
    }
}

package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.Strings;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Mobile;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.models.data.appcms.ui.page.TabletLandscape;
import com.viewlift.models.data.appcms.ui.page.TabletPortrait;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.adapters.AppCMSPlaylistAdapter;
import com.viewlift.views.utilities.ImageUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.BUTTON_MANAGE_DOWNLOAD;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.MANAGE_LANGUAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_API_SUMMARY_TEXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_ARTICLE_TRAY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AUDIO_DURATION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_AUDIO_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_BUTTON_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_02_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_EMPTY_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_FIGHTER_SELECTOR_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GAME_DATE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_OPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_GRID_THUMBNAIL_INFO;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LABEL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_NULL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_GALLERY_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_PLAYER_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_TEAM_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_FEATURE_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAN_FEATURE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAYLIST_AUDIO_ARTIST_TITLE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLAY_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PROGRESS_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_BADGE_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_DESCRIPTION_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_READ_MORE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TRAY_02_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TRAY_03_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TRAY_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TRAY_SCHEDULE_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_LAYER;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYER_MODULE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY_VALUE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_WATCHLIST_DURATION_UNIT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_STANDALONE_PLAYER_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_TRAY_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_BOTTOM_CENTER;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_CENTER_HORIZONTAL;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_CENTER_VERTICAL;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_END;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.VIEW_GRAVITY_START;
import static com.viewlift.views.customviews.ViewCreator.setTypeFace;


@SuppressLint("ViewConstructor")
public class TrayCollectionGridItemView extends LinearLayout {
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
    private Context mContext;
    private String mBlockName;
    private FrameLayout mFrameLayout;

    public TrayCollectionGridItemView(Context context,
                                      Layout parentLayout,
                                      boolean useParentLayout,
                                      Component component,
                                      String moduleId,
                                      int defaultWidth,
                                      int defaultHeight,
                                      boolean createMultipleContainersForChildren,
                                      boolean createRoundedCorners,
                                      AppCMSUIKeyType viewTypeKey,
                                      String blockName) {
        super(context);
        this.mContext = context;
        this.mBlockName = blockName;
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

        initView();
    }


    public void initView() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(VERTICAL);
        setPadding(10, 10, 20, 10);
        childItems = new ArrayList<>();
        if (CommonUtils.isFrameTray(mBlockName)) {
            mFrameLayout = new FrameLayout(mContext);
            mFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            addView(mFrameLayout);
        }
    }


    public void addChild(ItemContainer itemContainer) {

        childItems.add(itemContainer);
        if (CommonUtils.isFrameTray(mBlockName) && mFrameLayout != null) {

            mFrameLayout.addView(itemContainer.childView);
        } else {
            addView(itemContainer.childView);
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

        if (childComponent != null && data != null && data.getGist() != null) {
            if (data != null && onClickHandler != null) {
                view.setOnClickListener(v -> onClickHandler.click(TrayCollectionGridItemView.this,
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
                        componentKey == PAGE_VIDEO_IMAGE_KEY ||
                        componentKey == PAGE_PLAN_FEATURE_IMAGE_KEY ||
                        componentKey == PAGE_BADGE_IMAGE_KEY ||
                        componentKey == PAGE_PLAY_IMAGE_KEY ||
                        componentKey == PAGE_PHOTO_PLAYER_IMAGE ||
                        componentKey == PAGE_PHOTO_TEAM_IMAGE ||
                        componentKey == PAGE_THUMBNAIL_BADGE_IMAGE ||
                        componentKey == PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY ||
                        componentKey == PAGE_PHOTO_GALLERY_IMAGE_KEY) {
                    String ratio = BaseView.getViewRatio(context, childComponent.getLayout(), "16:9");
                    int placeholder = ImageUtils.getPlaceHolderByRatio(ratio, false);

                    int childViewWidth = (int) BaseView.getViewWidth(context, CommonUtils.getTrayWidth(ratio, context), ViewGroup.LayoutParams.WRAP_CONTENT);
                    int childViewHeight = BaseView.getViewHeightByRatio(ratio, childViewWidth);
                    String imageUrl = "";

                    if (componentKey == PAGE_THUMBNAIL_IMAGE_KEY) {

                        if (ratio.equalsIgnoreCase("16:9") && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_16x9() != null
                                && data.getGist().getImageGist().get_16x9().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_16x9(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("9:16") && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_9x16() != null
                                && data.getGist().getImageGist().get_9x16().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_9x16(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("3:4") && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_3x4() != null
                                && data.getGist().getImageGist().get_3x4().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_3x4(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("32:9") && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_32x9() != null
                                && data.getGist().getImageGist().get_32x9().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_32x9(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("1:1") && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_1x1() != null
                                && data.getGist().getImageGist().get_1x1().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_1x1(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("4:3") && data.getGist().getImageGist() != null
                                && data.getGist().getImageGist().get_4x3() != null
                                && data.getGist().getImageGist().get_4x3().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getImageGist().get_4x3(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (data.getGist().getImageGist() != null
                                && data.getGist().getVideoImageUrl() != null
                                && data.getGist().getVideoImageUrl().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getVideoImageUrl(),
                                    childViewWidth,
                                    childViewHeight);
                        }
                    }
                    if (componentKey == PAGE_THUMBNAIL_BADGE_IMAGE) {

                        if (ratio.equalsIgnoreCase("16:9") && data.getGist().getBadgeImages() != null
                                && data.getGist().getBadgeImages().get_16x9() != null
                                && data.getGist().getBadgeImages().get_16x9().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_16x9(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("9:16") && data.getGist().getBadgeImages() != null
                                && data.getGist().getBadgeImages().get_9x16() != null
                                && data.getGist().getBadgeImages().get_9x16().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_9x16(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("3:4") && data.getGist().getBadgeImages() != null
                                && data.getGist().getBadgeImages().get_3x4() != null
                                && data.getGist().getBadgeImages().get_3x4().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_3x4(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("32:9") && data.getGist().getBadgeImages() != null
                                && data.getGist().getBadgeImages().get_32x9() != null
                                && data.getGist().getBadgeImages().get_32x9().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_32x9(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("1:1") && data.getGist().getBadgeImages() != null
                                && data.getGist().getBadgeImages().get_1x1() != null
                                && data.getGist().getBadgeImages().get_1x1().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_1x1(),
                                    childViewWidth,
                                    childViewHeight);
                        } else if (ratio.equalsIgnoreCase("4:3") && data.getGist().getBadgeImages() != null
                                && data.getGist().getBadgeImages().get_4x3() != null
                                && data.getGist().getBadgeImages().get_4x3().contains("http")) {
                            imageUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                    data.getGist().getBadgeImages().get_4x3(),
                                    childViewWidth,
                                    childViewHeight);
                        } else {
                            imageUrl = "";
                        }
                    }

                    try {
                        if (!TextUtils.isEmpty(imageUrl) && imageUrl.length() > 0) {
                            ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                            RequestOptions requestOptions = new RequestOptions()
                                    .override(childViewWidth, childViewHeight)
                                    .placeholder(placeholder);
                            Glide.with(context)
                                    .load(imageUrl)
                                    .apply(requestOptions)
                                    .into((ImageView) view);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (moduleType == PAGE_SEASON_TRAY_MODULE_KEY) {
                        if (onClickHandler != null) {
                            view.setOnClickListener(v -> onClickHandler.click(TrayCollectionGridItemView.this,
                                    childComponent, data, position));
                        }
                    }
                }
            } //Bellow is end of thumbnail Image
            else if (componentType == PAGE_SEPARATOR_VIEW_KEY) {

                if (componentKey == PAGE_FIGHTER_SELECTOR_VIEW_KEY) {
//                    if(data.getFights().getFightId().equals(appCMSPresenter.getFocusedFightId())) {
                    if (data.isSelected()) {
                        (view).setBackgroundColor(Color.parseColor(
                                childComponent.getBackgroundColor()));

                    } else {
                        (view).setBackgroundColor(Color.parseColor(
                                "#00000000"));
                    }
                }
            }  // end of else if (componentType == PAGE_SEPARATOR_VIEW_KEY) {
            else if (componentType == PAGE_BUTTON_KEY) {
                if (componentKey == PAGE_PLAY_IMAGE_KEY) {
                    ((TextView) view).setText("");
                    if (!ViewCreator.isScheduleContentVisible(data, appCMSPresenter)) {
                        view.setVisibility(View.GONE);
                    }
                } else if (componentKey == PAGE_GRID_OPTION_KEY) {
                    if (viewTypeKey == PAGE_ARTICLE_TRAY_KEY) {
                        view.setBackground(context.getDrawable(R.drawable.dots_more_grey));
                        view.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
                        view.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                    }
                } else {
                    if (onClickHandler != null) {
                        view.setOnClickListener(v -> onClickHandler.click(TrayCollectionGridItemView.this,
                                childComponent, data, position));
                    }
                }
            } // end off else if (componentType == PAGE_BUTTON_KEY) {
            else if (componentType == PAGE_GRID_OPTION_KEY) {
                if (onClickHandler != null) {
                    view.setOnClickListener(v ->
                            onClickHandler.click(TrayCollectionGridItemView.this,
                                    childComponent, data, position));
                }
            } else if (componentType == PAGE_LABEL_KEY
                    && view instanceof TextView) {
                if (componentKey == PAGE_THUMBNAIL_TITLE_KEY ||
                        componentKey == PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setSingleLine(false);
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    if (componentKey == PAGE_ARTICLE_FEED_BOTTOM_TEXT_KEY) {
                        ((TextView) view).setGravity(Gravity.END);
                        StringBuffer publishDate = new StringBuffer();
                        publishDate.append("|");
                        publishDate.append(data.getContentDetails().getAuthor().getPublishDate());
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

                } else if (componentKey == PAGE_PLAN_FEATURE_TITLE_KEY && data != null && data.getTitle() != null) {
                    ((TextView) view).setText(data.getTitle());
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        if (appCMSPresenter.isKrononApp())
                            ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
                        else
                            ((TextView) view).setTextColor(textColor);
                    }
                } else if (componentKey == PAGE_PLAN_DESCRIPTION_KEY &&
                        !TextUtils.isEmpty(data.getDescription())) {
                    if (data.getPlanDetails() != null && data.getPlanDetails().size() > 0 && !Strings.isEmptyOrWhitespace(data.getPlanDetails().get(0).getDescription())) {
                        ((TextView) view).setText(data.getPlanDetails().get(0).getDescription());
                    } else {
                        ((TextView) view).setText(data.getDescription());
                    }
                    if (appCMSPresenter.isKrononApp())
                        ((TextView) view).setTextColor(Color.parseColor("#FFFFFF"));
                    else
                        ((TextView) view).setTextColor(appCMSPresenter.getGeneralTextColor());


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
                } else if (componentKey == PAGE_THUMBNAIL_READ_MORE_KEY) {
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    if (!TextUtils.isEmpty(childComponent.getTextColor())) {
                        int textColor = Color.parseColor(CommonUtils.getColor(getContext(),
                                childComponent.getTextColor()));
                        ((TextView) view).setTextColor(textColor);
                    }
                } else if (componentKey == PAGE_GAME_DATE_KEY) {
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
                } else if (componentKey == PAGE_AUDIO_DURATION_KEY) {
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
                } else if (componentKey == PAGE_PLAYLIST_AUDIO_ARTIST_TITLE) {

                    String artist = appCMSPresenter.getArtistNameFromCreditBlocks(data.getCreditBlocks());
                    ((TextView) view).setText(artist);
                    ((TextView) view).setTextColor(Color.parseColor(childComponent.getTextColor()));

                } else if (childComponent.getText() != null && !TextUtils.isEmpty(childComponent.getText())) {
                    ((TextView) view).setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(childComponent.getText()));
                    ((TextView) view).setTextColor(Color.parseColor(
                            childComponent.getTextColor()));
                }

                if (!TextUtils.isEmpty(component.getFontFamily())) {
                    setTypeFace(context,
                            appCMSPresenter,
                            jsonValueKeyMap,
                            component,
                            view);
                }
//                }
            }  // end off else if (componentType == PAGE_LABEL_KEY
            else if (componentType == PAGE_PROGRESS_VIEW_KEY) {
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

            view.forceLayout();
        } else {
            Log.d("bindView", "CollectionGridItemView else");
        }
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
        void click(TrayCollectionGridItemView collectionGridItemView,
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

    public FrameLayout getFrameChildContainer() {
        return mFrameLayout;
    }

    public void setViewMarginsFromComponent(Component childComponent,
                                            View view,
                                            Layout parentLayout,
                                            View parentView,
                                            boolean firstMeasurement,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                            boolean useMarginsAsPercentages,
                                            boolean useWidthOfScreen,
                                            String viewType, String blockName) {
        Layout layout = childComponent.getLayout();


        view.setPadding(0, 0, 0, 0);

        int lm = 0, tm = 0, rm = 0, bm = 0;
        int deviceHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        int deviceWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        int viewWidth = (int) BaseView.getViewWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
        int viewHeight = (int) BaseView.getViewHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);

        AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType viewGravity = jsonValueKeyMap.get(childComponent.getViewGravity());
        if (viewGravity == null) {
            viewGravity = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType uiBlockName = jsonValueKeyMap.get(blockName);
        if (componentKey == null) {
            uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType componentViewType = jsonValueKeyMap.get(viewType);
        if (componentViewType == null) {
            componentViewType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY ||
                componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY) {
            viewWidth = (int) BaseView.convertWidthDpToPixel(layout, getContext());
            if (viewWidth == 0) {
                viewWidth = FrameLayout.LayoutParams.MATCH_PARENT;
            }
            viewHeight = (int) BaseView.convertHeightDpToPixel(layout, getContext());
            if (viewHeight == 0) {
                viewHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
            }
        }

        int parentViewWidth = (int) BaseView.getViewWidth(getContext(),
                parentLayout,
                parentView.getMeasuredWidth());
        int parentViewHeight = (int) BaseView.getViewHeight(getContext(),
                parentLayout,
                parentView.getMeasuredHeight());
        int maxViewWidth = (int) BaseView.getViewMaximumWidth(getContext(), layout, -1);
        int measuredHeight = parentViewHeight != 0 ? parentViewHeight : deviceHeight;
        int measuredWidth = parentViewWidth != 0 ? parentViewWidth : deviceWidth;
        int gravity = Gravity.NO_GRAVITY;

        if (BaseView.isTablet(getContext())) {
            if (BaseView.isLandscape(getContext())) {
                TabletLandscape tabletLandscape = layout.getTabletLandscape();
                if (tabletLandscape != null) {
                    if (tabletLandscape.getXAxis() != 0f) {
                        float scaledX = BaseView.DEVICE_WIDTH * (tabletLandscape.getXAxis() / BaseView.STANDARD_TABLET_HEIGHT_PX);
                        lm = Math.round(scaledX);
                    }

                    if (tabletLandscape.getYAxis() != 0f) {
                        float scaledY = BaseView.DEVICE_HEIGHT * (tabletLandscape.getYAxis() / BaseView.STANDARD_TABLET_WIDTH_PX);
                        tm = Math.round(scaledY);
                    }

                    if (BaseView.getViewWidth(tabletLandscape) == -1f && tabletLandscape.getXAxis() != 0f) {
                        float scaledX = BaseView.DEVICE_WIDTH * (tabletLandscape.getXAxis() / BaseView.STANDARD_TABLET_HEIGHT_PX);
                        lm = Math.round(scaledX);
                    } else if (tabletLandscape.getLeftMargin() != 0f) {
                        lm = Math.round(BaseView.DEVICE_WIDTH * (tabletLandscape.getLeftMargin() / BaseView.STANDARD_TABLET_HEIGHT_PX));
                    }
                    if (tabletLandscape.getRightMargin() != 0f) {
                        rm = Math.round(BaseView.DEVICE_WIDTH * (tabletLandscape.getRightMargin() / BaseView.STANDARD_TABLET_WIDTH_PX));
                    }
                    if (useMarginsAsPercentages) {
                        if (tabletLandscape.getTopMargin() != 0f) {
                            tm = Math.round((tabletLandscape.getTopMargin() / 100.0f) * measuredHeight);
                        }
                        if (tabletLandscape.getBottomMargin() != 0f) {
                            bm = Math.round(BaseView.DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / BaseView.STANDARD_TABLET_HEIGHT_PX));
                        }
                    } else {
                        if (tabletLandscape.getTopMargin() != 0f) {
                            tm = (int) (BaseView.DEVICE_HEIGHT * (tabletLandscape.getTopMargin() / BaseView.STANDARD_TABLET_WIDTH_PX));
                        } else if (tabletLandscape.getBottomMargin() != 0f && tabletLandscape.getBottomMargin() > 0) {
                            int tmDiff = viewHeight;
                            if (tmDiff < 0) {
                                tmDiff = 0;
                            }

                            if (parentViewHeight > 0) {
                                tm = parentViewHeight -
                                        (int) (BaseView.DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / BaseView.STANDARD_TABLET_WIDTH_PX)) -
                                        tmDiff;
                            } else {
                                gravity = Gravity.BOTTOM;
                                bm = Math.round(BaseView.DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / BaseView.STANDARD_TABLET_WIDTH_PX));
                            }
                        }
                    }
                }
            } else {
                TabletPortrait tabletPortrait = layout.getTabletPortrait();
                if (tabletPortrait != null) {
                    if (tabletPortrait.getXAxis() != 0f) {
                        float scaledX = BaseView.DEVICE_WIDTH * (tabletPortrait.getXAxis() / BaseView.STANDARD_TABLET_WIDTH_PX);
                        lm = Math.round(scaledX);
                    }

                    if (tabletPortrait.getYAxis() != 0f) {
                        float scaledY = BaseView.DEVICE_HEIGHT * (tabletPortrait.getYAxis() / BaseView.STANDARD_TABLET_HEIGHT_PX);
                        tm = Math.round(scaledY);
                    }

                    if (BaseView.getViewWidth(tabletPortrait) == -1 && tabletPortrait.getXAxis() != 0f) {
                        float scaledX = BaseView.DEVICE_WIDTH * (tabletPortrait.getXAxis() / BaseView.STANDARD_TABLET_WIDTH_PX);
                        lm = Math.round(scaledX);
                    } else if (tabletPortrait.getLeftMargin() != 0f) {
                        lm = Math.round(BaseView.DEVICE_WIDTH * (tabletPortrait.getLeftMargin() / BaseView.STANDARD_TABLET_WIDTH_PX));
                    }
                    if (tabletPortrait.getRightMargin() != 0f) {
                        rm = Math.round(BaseView.DEVICE_WIDTH * (tabletPortrait.getRightMargin() / BaseView.STANDARD_TABLET_WIDTH_PX));
                    }
                    if (useMarginsAsPercentages) {
                        if (tabletPortrait.getTopMargin() != 0f) {
                            tm = Math.round((tabletPortrait.getTopMargin() / 100.0f) * measuredHeight);
                        }
                        if (tabletPortrait.getBottomMargin() != 0f) {
                            bm = Math.round(BaseView.DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / BaseView.STANDARD_TABLET_HEIGHT_PX));
                        }
                    } else {
                        if (tabletPortrait.getTopMargin() != 0f) {
                            tm = (int) (BaseView.DEVICE_HEIGHT * (tabletPortrait.getTopMargin() / BaseView.STANDARD_TABLET_HEIGHT_PX));
                        } else if (tabletPortrait.getBottomMargin() != 0f && tabletPortrait.getBottomMargin() > 0) {
                            int tmDiff = viewHeight;
                            if (tmDiff < 0) {
                                tmDiff = 0;
                            }
                            if (parentViewHeight > 0) {
                                tm = parentViewHeight -
                                        (int) (BaseView.DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / BaseView.STANDARD_TABLET_HEIGHT_PX)) -
                                        tmDiff;
                            } else {
                                gravity = Gravity.BOTTOM;
                                bm = Math.round(BaseView.DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / BaseView.STANDARD_TABLET_HEIGHT_PX));
                            }
                        }
                    }
                }
            }
        } else {
            Mobile mobile = layout.getMobile();
            if (mobile != null) {
                if (mobile.getXAxis() != 0f) {
                    float scaledX = BaseView.DEVICE_WIDTH * (mobile.getXAxis() / BaseView.STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledX);
                }
                if (componentKey == AppCMSUIKeyType.PAGE_LAST_NAME || componentKey == AppCMSUIKeyType.PAGE_LAST_NAME_INPUT) {
                    float scaledX = BaseView.DEVICE_WIDTH / 2;
                    lm = Math.round(scaledX);
                }
                if (mobile.getYAxis() != 0f) {
                    float scaledY = BaseView.DEVICE_HEIGHT * (mobile.getYAxis() / BaseView.STANDARD_MOBILE_HEIGHT_PX);
                    tm = Math.round(scaledY);
                }

                if (BaseView.getViewWidth(mobile) == -1 && mobile.getXAxis() != 0f) {
                    float scaledX = BaseView.DEVICE_WIDTH * (mobile.getXAxis() / BaseView.STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledX);
                } else if (mobile.getLeftMargin() != 0f) {
                    float scaledLm = BaseView.DEVICE_WIDTH * (mobile.getLeftMargin() / BaseView.STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledLm);
                    if (mobile.getRightMargin() != 0f) {
                        float scaledRm = BaseView.DEVICE_WIDTH * (mobile.getRightMargin() / BaseView.STANDARD_MOBILE_WIDTH_PX);
                        rm = Math.round(scaledRm);
                    }
                }
                if (mobile.getRightMargin() != 0f) {
                   /* int lmDiff = viewWidth;
                    if (lmDiff < 0) {
                        lmDiff = 0;
                    }

                    if (parentViewWidth > 0) {
                        float scaledRm = BaseView.DEVICE_WIDTH * (mobile.getRightMargin() / BaseView.STANDARD_MOBILE_WIDTH_PX);
                        lm = parentViewWidth - Math.round(scaledRm) - lmDiff;
                    } else {
                        gravity = Gravity.END;*/
                    rm = Math.round(BaseView.DEVICE_WIDTH * (mobile.getRightMargin() / BaseView.STANDARD_MOBILE_WIDTH_PX));
                    // }
                }
                if (useMarginsAsPercentages) {
                    if (mobile.getTopMargin() != 0f) {
                        tm = Math.round((mobile.getTopMargin() / 100.0f) * measuredHeight);
                    }
                    if (mobile.getBottomMargin() != 0f) {
                        int marginDiff = viewHeight;
                        if (marginDiff < 0) {
                            marginDiff = 0;
                        }
                        bm = Math.round(BaseView.DEVICE_HEIGHT * (mobile.getBottomMargin() / BaseView.STANDARD_MOBILE_HEIGHT_PX));
//                        bm = Math.round(((100.0f - mobile.getBottomMargin()) / 100.0f) * measuredHeight) -
//                                Math.round(convertDpToPixel(marginDiff, getContext()));
                    }
                } else {
                    if (mobile.getTopMargin() != 0f) {
                        tm = Math.round(BaseView.DEVICE_HEIGHT * (mobile.getTopMargin() / BaseView.STANDARD_MOBILE_HEIGHT_PX));
                    }
                    if (mobile.getBottomMargin() != 0f && mobile.getBottomMargin() > 0) {
                        int tmDiff = viewHeight;
                        if (tmDiff < 0) {
                            tmDiff = 0;
                        }
                        if (parentViewHeight > 0) {
                            tm = parentViewHeight -
                                    (int) (BaseView.DEVICE_HEIGHT * (mobile.getBottomMargin() / BaseView.STANDARD_MOBILE_HEIGHT_PX)) -
                                    tmDiff;
                        } else {
                            gravity = Gravity.BOTTOM;
                            bm = Math.round(BaseView.DEVICE_HEIGHT * (mobile.getBottomMargin() / BaseView.STANDARD_MOBILE_HEIGHT_PX));
                        }
                    }
                }
            }
        }

        int lp = 0, rp = 0, tp = 0, bp = 0;
        if (childComponent.getLeftpadding() != 0) {
            lp = (int) BaseView.convertDpToPixel(childComponent.getLeftpadding(), getContext());
        }
        if (childComponent.getTopPadding() != 0) {
            tp = (int) BaseView.convertDpToPixel(childComponent.getTopPadding(), getContext());
        }
        if (childComponent.getRightPadding() != 0) {
            rp = (int) BaseView.convertDpToPixel(childComponent.getRightPadding(), getContext());
        }
        if (childComponent.getBottomPadding() != 0) {
            bp = (int) BaseView.convertDpToPixel(childComponent.getBottomPadding(), getContext());
        }

        view.setPadding(lp, tp, rp, bp);

        if (viewGravity == VIEW_GRAVITY_CENTER_HORIZONTAL) {
            gravity = Gravity.CENTER_HORIZONTAL;
        }

        if (viewGravity == VIEW_GRAVITY_BOTTOM_CENTER) {
            gravity = Gravity.BOTTOM | Gravity.CENTER;
        }
        if (viewGravity == PAGE_NULL_KEY && componentKey == AppCMSUIKeyType.PAGE_PLAYLIST_AUDIO_ARTIST_TITLE) {
            gravity = Gravity.LEFT;
        }
        if (viewGravity == PAGE_NULL_KEY && componentKey == AppCMSUIKeyType.PAGE_API_TITLE) {
            gravity = Gravity.LEFT;
        }
        if (viewGravity == VIEW_GRAVITY_START) {
            gravity = Gravity.START;
        }

        if (viewGravity == VIEW_GRAVITY_CENTER_VERTICAL) {
            gravity = Gravity.CENTER_VERTICAL;
        }
        if (viewGravity == VIEW_GRAVITY_END) {
            gravity = Gravity.END;
        }

        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY ||
                componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY) {
            if (viewWidth < 0) {
                viewWidth = FrameLayout.LayoutParams.MATCH_PARENT;
            }
            if (jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_GRID_THUMBNAIL_INFO
                    || jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO
                    || componentKey == BUTTON_MANAGE_DOWNLOAD
                    || componentKey == MANAGE_LANGUAGE_KEY) {
                viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            if (jsonValueKeyMap.get(childComponent.getTextAlignment()) == AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_KEY) {
                ((TextView) view).setGravity(Gravity.CENTER);
            }

            if (jsonValueKeyMap.get(childComponent.getTextAlignment()) == AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY) {
                ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
            }
            if (jsonValueKeyMap.get(childComponent.getTextAlignment()) == AppCMSUIKeyType.PAGE_TEXTALIGNMENT_RIGHT_KEY) {
                ((TextView) view).setGravity(Gravity.RIGHT);
                ((TextView) view).setGravity(Gravity.END);
            }
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY &&
                    view instanceof Spinner) {
                viewHeight = FrameLayout.LayoutParams.WRAP_CONTENT;
                viewWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
            }


            switch (componentKey) {
                case PAGE_AGE_ERROR_LABEL_KEY:
                    if (BaseView.isLandscape(getContext())) {
                        gravity = Gravity.START;
                    }
                    break;
                case PAGE_PLAY_IMAGE1_KEY:
                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_EVENT_CAROUSEL_02) {
                        gravity = Gravity.CENTER;
                    }
                    break;
                case PAGE_BRANDS_TRAY_TITLE_BRAND_KEY:
                    viewWidth = FrameLayout.LayoutParams.MATCH_PARENT;
                    break;
                case PAGE_TRAY_TITLE_KEY:
                    if (BaseView.isTablet(getContext())) {
                        if (BaseView.isLandscape(getContext())) {
                            tm -= viewHeight / 6;
                            viewHeight *= 1.5;
                        }
                    }
                    break;
                case PAGE_WATCH_LIVE_BUTTON_KEY:
                    if (!BaseView.isTablet(getContext())) {
                        if (childComponent.getTextAlignment().equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_text_alignment_center_horizontal_key))) {
                            ((Button) view).setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                    break;
                case PAGE_VIDEO_PLAY_BUTTON_KEY:
                    if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY) {
                        lm -= 8;
                        rm -= 8;
                        bm -= 8;
                        tm -= 8;
                    }
                    break;

                case PAGE_PLAY_IMAGE_KEY:
                    if (AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_HISTORY_02_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_WATCHLIST_01_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_WATCHLIST_02_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_HISTORY_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY != jsonValueKeyMap.get(viewType)

                            && AppCMSUIKeyType.PAGE_WATCHLIST_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && componentViewType != AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY) {
                        gravity = Gravity.CENTER;
                        lm -= 40;
                        rm -= 40;
                        bm -= 40;
                        tm -= 40;
                    }
                    break;

                case PAGE_PLAN_PURCHASE_BUTTON_KEY:
                case PAGE_LAUNCH_LOGIN_BUTTON_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case PAGE_VIDEO_CLOSE_KEY:
                    lm -= 8;
                    bm -= 8;
                    gravity = Gravity.END;
                    break;
                case PAGE_WATCHLIST_DELETE_ITEM_BUTTON:
                case PAGE_DELETE_DOWNLOAD_KEY:
                case PAGE_DELETE_WATCHLIST_KEY:
                case PAGE_DELETE_HISTORY_KEY:
                    gravity = Gravity.END;
                    break;
                case LABEL_SHOW_CLASSES:
                    ViewCompat.setTranslationZ(view, 2);
                    break;
                case PAGE_DOWNLOAD_SETTING_TITLE_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    bm = viewHeight / 2;
                    break;
                case PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY:
                case PAGE_LIVE_SCHEDULE_TAG_CLASS_TITLE_KEY:
                case PAGE_LIVE_SCHEDULE_ITEM_VERTICAL_HEIGHT_KEY:
                case PAGE_INSTRUCTOR_CLASS_DURATION_RECENT_KEY:
                case PAGE_BRANDS_CAROUSEL_NAME_KEY:
                case PAGE_BRANDS_CAROUSEL_DESCRIPTION_KEY:
                case PAGE_HOME_DURATION_CAROUSEL:
                case PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY:
                case PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY:
                case PAGE_SCHEDULE_CAROUSEL_NEXT_CLASS_KEY:
                case PAGE_HOME_CAROUSEL_EVENT_TIMER_KEY:
                case PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY:
                case PAGE_CAROUSEL_VIDEO_DURATION_SEPARATION_KEY:
                case PAGE_CAROUSEL_TITLE_KEY: {
                    int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                    int heightByRatio = 0;
                    if (BaseView.isTablet(getContext())) {
                        if (BaseView.isLandscape(getContext())) {
                            rm = Math.round(BaseView.DEVICE_HEIGHT * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                                    BaseView.STANDARD_MOBILE_WIDTH_PX));
                        } else {
                            rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getTabletPortrait().getRightMargin() /
                                    BaseView.STANDARD_MOBILE_WIDTH_PX));
                        }
                    } else {
                        rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getMobile().getRightMargin() /
                                BaseView.STANDARD_MOBILE_WIDTH_PX));
                    }

                    /*if (componentViewType != AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE &&
                            componentViewType != AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_PERSON_DETAIL_03_MODULE_KEY)
                        gravity = Gravity.CENTER_HORIZONTAL;
                    if (viewType != null &&
                            viewType.equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_event_carousel_module_key))
                            ) {
                        if (BaseView.isLandscape(getContext())) {
                            tm -= viewHeight * 5;
                        } else if (BaseView.isTablet(getContext()) && !BaseView.isLandscape(getContext())) {
                            tm -= viewHeight * 2;
                        } else {
                            tm -= viewHeight * 3;
                        }
                        viewHeight *= 2;
                    } else if ((BaseView.isLandscape(getContext()) || !BaseView.isTablet(getContext()))) {
                        if (BaseView.isLandscape(getContext())) {
                            tm -= viewHeight * 5;
                        } else {
                            tm -= viewHeight * 2;
                        }
                        viewHeight *= 2;
                    } else {
                        tm -= viewHeight * 3;
                        viewHeight *= 2;
                    }
                    if (componentViewType != AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE &&
                            componentViewType != AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_PERSON_DETAIL_03_MODULE_KEY)
                        lm = maxViewWidth / 2 - viewWidth / 2;

                    if (componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL ||
                            componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_CLASS_DURATION_RECENT_KEY) {
                        viewWidth = (int) BaseView.getViewWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);
                    }*/


                    if (componentViewType == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_TRAY_NETFLIX_MODULE_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY) {
                        heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        bm = viewHeight / 2;
                    }
                    if (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY) {
                        if (BaseView.isTablet(getContext())) {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 32.0f);
                        } else {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        }
                    }
                    if (blockName.equalsIgnoreCase("carousel01"))
                        tm -= viewHeight * 2;
                    else
                        tm = (heightByRatio - (bm + viewHeight));
                    viewWidth = (int) BaseView.getViewWidth(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);

                    if (componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_NEXT_CLASS_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY) {
                        lm = (thumbnailWidth - (viewWidth + rm));
                    }

                    if (componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL ||
                            componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_TAG_CLASS_TITLE_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY) {
                        if (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY) {
                        } else {
                            viewWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                        }
                    }

                    break;
                }
                case PAGE_BROWSE_TAG_CLASS_TITLE_KEY:
                case PAGE_BROWSE_TAG_BRAND_TITLE_KEY:
                case PAGE_BROWSE_DURATION_KEY:
                case PAGE_CLOSE:
                case LABEL_PAGE_NAME:
                case LABEL_CLEAR_FILTER:
                case PAGE_LIVE_SCHEDULE_CLASS_DURATION_KEY:
                case VIEW_FILTER_BUTTON:
                    viewWidth = FrameLayout.LayoutParams.WRAP_CONTENT;
                    break;

                case PAGE_HOME_CLASS_DURATION_NETFLIX_KEY:
                    viewWidth = (int) BaseView.getViewWidth(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                    break;

                case PAGE_CAROUSEL_INFO_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    if (viewType != null &&
                            viewType.equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_event_carousel_module_key))
                    ) {
                        tm -= viewHeight * 2;
                    } else if (BaseView.isTablet(getContext())) {
                        if (BaseView.isLandscape(getContext())) {
                            tm -= viewHeight * 9;
                        } else {
                            tm -= viewHeight * 5;
                        }
                    } else {
                        tm -= viewHeight * 2;
                    }
                    viewHeight *= 2;
                    lm = maxViewWidth / 2 - viewWidth / 2;
                    break;

                case PAGE_WATCH_VIDEO_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    lm = maxViewWidth / 2;
                    if (BaseView.isTablet(getContext()) && !BaseView.isLandscape(getContext())) {
                        tm -= viewHeight / 2;
                    } else if (BaseView.isLandscape(getContext())) {
                        tm -= viewHeight * 2;
                    }
                    break;

                case PAGE_VIDEO_CAST_KEY:
                case PAGE_VIDEO_SHARE_KEY:
                    if (BaseView.isTablet(getContext())) {
                        lm -= viewWidth / 2;
                        tm -= (int) (viewWidth * 0.25);
                        viewHeight = (int) (viewWidth * 1.25);
                    } else {
                        lm -= viewWidth / 3;
                    }
                    break;

                case PAGE_API_TITLE:
                    viewHeight *= 1.1;
                    break;

                case PAGE_ADD_TO_WATCHLIST_KEY:
                    if (BaseView.isTablet(getContext())) {
                        lm -= viewWidth * 0.5;
                    }
                    gravity = Gravity.TOP;
                    break;

                case PAGE_VIDEO_DOWNLOAD_BUTTON_KEY:
                    if (BaseView.isTablet(getContext())) {
                        lm -= viewWidth * 0.7;
                    }
                    viewWidth = viewHeight;
                    break;

                case PAGE_PLAN_TITLE_KEY:
                    if (childComponent.getTextAlignment().equalsIgnoreCase("right")) {
                        gravity = Gravity.END;
                        rm += BaseView.convertDpToPixel(8, getContext());
                        view.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
                    } else {
                        gravity = Gravity.START;
                    }
                    break;

                case PAGE_VENUE_LABEL_KEY:
                case PAGE_GAME_DATE_KEY:
                    if (childComponent.getTextAlignment().equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_text_alignment_right_key))) {
                        gravity = Gravity.END;
                        rm += BaseView.convertDpToPixel(10, getContext());
                        view.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
                    } else {
                        gravity = Gravity.START;
                    }
                    break;

                case PAGE_PLAN_PRICEINFO_KEY:
                    lm += BaseView.convertDpToPixel(8, getContext());
                    if (componentViewType == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        gravity = Gravity.CENTER_VERTICAL;
                    }
                    break;
                case PAGE_PLAN_FEATURE_TEXT_KEY:
                    if (componentViewType == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                        if (BaseView.isTablet(getContext())) {
                            if (BaseView.isLandscape(getContext())) {
                                rm = Math.round(BaseView.DEVICE_WIDTH * (layout.getTabletLandscape().getRightMargin() / BaseView.STANDARD_TABLET_WIDTH_PX));
                            }
                        }
                    }
                    break;

                case PAGE_SINGLE_PLAN_SUBSCRIBE_TEXT_KEY:
                    if (componentViewType == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        gravity = Gravity.CENTER;
                    }
                    break;
                case PAGE_PLAN_FEATURE_TITLE_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    break;

                case PAGE_SETTINGS_PLAN_VALUE_KEY:
                    if (BaseView.isTablet(getContext())) {
                        if (BaseView.isLandscape(getContext())) {
                            rm = Math.round(BaseView.DEVICE_HEIGHT * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                                    BaseView.STANDARD_MOBILE_WIDTH_PX));
                        } else {
                            rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getTabletPortrait().getRightMargin() /
                                    BaseView.STANDARD_MOBILE_WIDTH_PX));
                        }
                    } else {
                        rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getMobile().getRightMargin() /
                                BaseView.STANDARD_MOBILE_WIDTH_PX));
                    }

                    if (0 < lm && 0 < rm) {
                        viewWidth = measuredWidth - rm - lm;
                    }
                    break;

                case PAGE_PLAYLIST_AUDIO_ARTIST_TITLE:
                case PAGE_ARTICLE_TITLE_KEY:
                case PAGE_THUMBNAIL_TITLE_KEY:
                   /* if (jsonValueKeyMap.get(viewType) != null &&
                            (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_MODULE_KEY)) {
                        int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) BaseView.getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                        if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                            if (thumbnailHeight < thumbnailWidth) {
                                int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                                tm = heightByRatio + 4;
                            } else {
                                int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                                tm = heightByRatio + 4;
                            }
                        }
                    }*/

                    if (jsonValueKeyMap.get(viewType) != null &&
                            (jsonValueKeyMap.get(viewType) == PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_SEE_ALL_CATEGORY_02_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_SEE_ALL_CATEGORY_01_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_CONTINUE_WATCHING_02_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_AUDIO_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_ARTICLE_TRAY_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_TRAY_02_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_TRAY_03_MODULE_KEY)) {
                        int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) BaseView.getThumbnailHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                        if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_AUDIO_TRAY_MODULE_KEY) {
                                int heightByRatio = (int) ((float) thumbnailWidth * 1.0f / 1.0f);
                                tm = heightByRatio + 4;
                            } else if (thumbnailHeight < thumbnailWidth) {
                                int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                                tm = heightByRatio + 4;
                            } else {
                                int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                                tm = heightByRatio + 4;
                            }
                        }
                    }

                    break;


                case PAGE_VIDEO_AGE_LABEL_KEY:
                    viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
                    view.setPadding(10, 0, 10, 0);
                    break;
                case PAGE_WATCHLIST_DURATION_KEY_BG:
                case PAGE_HISTORY_DURATION_KEY:
                case PAGE_DOWNLOAD_DURATION_KEY:
                case PAGE_GRID_THUMBNAIL_INFO:
                case PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO:
                case PAGE_LIBRARY_01_MODULE_KEY:

                    int padding = childComponent.getPadding();
                    view.setPadding(padding, 0, padding, 0);
                    if (jsonValueKeyMap.get(viewType) != null &&
                            (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_ARTICLE_TRAY_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_02_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_03_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_MODULE_KEY)) {
                        int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) BaseView.getThumbnailHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                        if (thumbnailHeight < thumbnailWidth) {
                            int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                            tm = heightByRatio - viewHeight;
                        } else {
                            int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                            tm = heightByRatio - viewHeight;
                        }
                    }
                    if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_WATCHLIST_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_WATCHLIST_02_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY) {
                        int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) BaseView.getThumbnailHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                        if (thumbnailHeight < thumbnailWidth) {
                            int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                            tm += heightByRatio - viewHeight;
                        } else {
                            int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                            tm += heightByRatio - viewHeight;
                        }

                        if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY ||
                                jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY ||
                                jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY ||
                                jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY) {
                            tm -= 12;
                        }

                        lm += thumbnailWidth - viewWidth;
                    }
                    break;

                default:
                    break;
            }

            if (maxViewWidth != -1) {
                ((TextView) view).setMaxWidth(maxViewWidth);
            }
        } else if (componentType == AppCMSUIKeyType.PAGE_TEXTFIELD_KEY) {
            viewHeight *= 1.2;
        } else if (componentType == PAGE_SEPARATOR_VIEW_KEY
                && componentViewType == PAGE_VIDEO_PLAYER_MODULE_KEY
                && !BaseView.isLandscape(getContext())) {
            tm = (int) ((float) deviceWidth * 9.0f / 16.0f);
        } else if (componentType == PAGE_VIDEO_PLAYER_VIEW_KEY) {
            if (uiBlockName == UI_BLOCK_STANDALONE_PLAYER_02 && BaseView.isTablet(getContext())) {
            } else
                viewWidth = deviceWidth;
            viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
        } else if (componentType == PAGE_TABLE_VIEW_KEY) {
            int padding = childComponent.getPadding();
            view.setPadding(0, 0, 0, (int) BaseView.convertDpToPixel(padding, getContext()));
            if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_TRAY_06_MODULE_KEY) {

                if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_EVENT_DETAIL_MODULE_KEY) {
                    viewHeight = (int) Math.round(getContext().getResources().getDisplayMetrics().heightPixels / 1.125);
                }

                RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    }
                };
                ((RecyclerView) view).addOnItemTouchListener(mScrollTouchListener);
            }
            if ((((RecyclerView) view).getAdapter() instanceof AppCMSPlaylistAdapter)) {
                padding = 20;
                view.setPadding(0, 0, 0, (int) BaseView.convertDpToPixel(padding, getContext()));
            }

            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_AC_TEAM_SCHEDULE_MODULE_KEY) {
                padding = 70;
                view.setPadding(0, 0, 0, (int) BaseView.convertDpToPixel(padding, getContext()));
            }
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_SETTING_MODULE_KEY ||
                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_LANGUAGE_SETTING_MODULE_KEY) {
                padding = 200;
                view.setPadding(0, 0, 0, (int) BaseView.convertDpToPixel(padding, getContext()));
            }
        } else if (componentType == AppCMSUIKeyType.PAGE_EVENT_DETAIL_MODULE_KEY) {
            int padding = childComponent.getPadding();
            view.setPadding(0, 0, 0, (int) BaseView.convertDpToPixel(padding, getContext()));
            viewHeight = (int) Math.round(getContext().getResources().getDisplayMetrics().heightPixels / 1.125);

        } else if (componentType == AppCMSUIKeyType.PAGE_PROGRESS_VIEW_KEY) {
            if (jsonValueKeyMap.get(viewType) != null) {
                if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_02_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                    int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                    int thumbnailHeight = (int) BaseView.getThumbnailHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                    if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                        int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                        if (thumbnailHeight < thumbnailWidth) {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        }

                        tm = heightByRatio - viewHeight;
                    }
                } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_VIDEO_DETAILS_KEY) {
                    viewWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                    gravity = Gravity.CENTER_HORIZONTAL;
                } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_02_MODULE_KEY) {
                    int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                    int thumbnailHeight = (int) BaseView.getThumbnailHeight(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                    if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                        int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                        if (thumbnailHeight < thumbnailWidth) {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        }

                        tm += heightByRatio - viewHeight;
                    }
                }

            }
        } /*else if (componentType == AppCMSUIKeyType.PAGE_PAGE_CONTROL_VIEW_03_KEY) {

            int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
            int heightByRatio = 0;
            if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_03_MODULE_TYPE &&
                     BaseView.isLandscape(getContext())) {
                heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 32.0f);
            } else {
                heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
            }
            tm += heightByRatio;
        }*/ else if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
            if ((componentKey == AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_CAROUSEL_BADGE_IMAGE_KEY)
            ) {
                if (0 < viewWidth && viewHeight <= 0
                ) {
                    if (BaseView.isTablet(getContext())
                            && (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY)) {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 32.0f);
                    } else if (BaseView.isLandscape(getContext())
                            && (componentViewType == AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE)) {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 32.0f);
                    } else {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
                    }
                }
            } else if (componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY) {
                int thumbnailWidth = (int) BaseView.getThumbnailWidth(getContext(), layout, FrameLayout.LayoutParams.MATCH_PARENT);
                int heightByRatio = 0;
                if (BaseView.isTablet(getContext())) {
                    if (BaseView.isLandscape(getContext())) {
                        rm = Math.round(BaseView.DEVICE_HEIGHT * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                                BaseView.STANDARD_MOBILE_WIDTH_PX));
                    } else {
                        rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getTabletPortrait().getRightMargin() /
                                BaseView.STANDARD_MOBILE_WIDTH_PX));
                    }
                } else {
                    rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getMobile().getRightMargin() /
                            BaseView.STANDARD_MOBILE_WIDTH_PX));
                }
                if (componentViewType == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY
                        || componentViewType == AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY
                        || componentViewType == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_KEY
                        || componentViewType == AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY) {
                    heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                    bm = viewHeight / 2;
                }
                if (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY) {
                    if (BaseView.isTablet(getContext())) {
                        heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 32.0f);
                    } else {
                        heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                    }
                }

                viewWidth = (int) BaseView.getViewWidth(getContext(), layout, FrameLayout.LayoutParams.WRAP_CONTENT);
                lm = (thumbnailWidth - (viewWidth + rm));
                if (componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY) {
                    tm = (heightByRatio - (bm + viewHeight));
                }
            } else if (componentKey == AppCMSUIKeyType.PAGE_PLAN_FEATURE_IMAGE_KEY) {
                gravity = Gravity.CENTER_HORIZONTAL;
            } else if (componentKey == AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_IMAGE_KEY) {
                gravity = Gravity.LEFT;
            } else if (componentKey == AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY) {
                gravity = Gravity.CENTER_VERTICAL;
            } else if (componentKey == AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY ||
                    componentKey == AppCMSUIKeyType.VIEW_BROWSE_SORT) {
                viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
                viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            } /*else if (componentKey == AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY) {
                if (getResources().getBoolean(R.bool.video_detail_page_plays_video)) {
                    if (!BaseView.isTablet(getContext())) {
                        if (BaseView.isLandscape(getContext())) {
                            viewWidth = viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }
                    } else {
                        if (ViewCreator.playerViewFullScreenEnabled()) {
                            viewWidth = viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }
                    }
                }
            }*/ else if (componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_THUMBNAIL_IMAGE_RECENT_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_BADGE_IMAGE_RECENT_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY
            ) {
                if (0 < viewWidth && 0 < viewHeight) {
                    if ((viewWidth < viewHeight) && jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_PLAYLIST_MODULE_KEY) {
                        viewHeight = (int) ((float) viewWidth * 4.0f / 3.0f);
                    } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_PLAYLIST_MODULE_KEY) {
                        viewHeight = (int) ((float) viewWidth * 1.0f / 1.0f);
                    } else {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
                    }
                }
            }

            if (componentKey == AppCMSUIKeyType.PAGE_BROWSE_VOD_PLAY_BUTTON_KEY) {
                if (BaseView.isLandscape(getContext())) {
//                    Math.round(dpToPx((int)(childComponent.getLayout().getTabletLandscape().getRightMargin())));
                    rm = Math.round(BaseView.DEVICE_WIDTH * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                            BaseView.STANDARD_MOBILE_WIDTH_PX));
                }
            }
        }

        if ((useWidthOfScreen || componentKey == PAGE_VIDEO_PLAYER_VIEW_KEY_VALUE) && uiBlockName != UI_BLOCK_STANDALONE_PLAYER_02) {
            viewWidth = BaseView.DEVICE_WIDTH;
        }

        if (componentType == PAGE_VIDEO_LAYER) {
            viewHeight = BaseView.DEVICE_HEIGHT;
            viewWidth = BaseView.DEVICE_WIDTH;
        }
        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(viewWidth, viewHeight);
        marginLayoutParams.setMargins(lm, tm, rm, bm);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginLayoutParams);
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;

        if (componentType == PAGE_LABEL_KEY ||
                uiBlockName == UI_BLOCK_STANDALONE_PLAYER_02 ||
                componentType == PAGE_BUTTON_KEY ||
                componentType == PAGE_IMAGE_KEY) {
            layoutParams.gravity = gravity;
        }


        if (componentType == AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY) {
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY) {
                layoutParams.height = BaseView.getDeviceHeight() - tm;
            }
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SUB_NAV_MODULE_KEY) {
                layoutParams.height = layoutParams.height - 100;
            }
        }

        view.setLayoutParams(layoutParams);
    }

}

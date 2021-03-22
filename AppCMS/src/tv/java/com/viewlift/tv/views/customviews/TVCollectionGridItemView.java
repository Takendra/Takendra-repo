package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.Resources;
import com.viewlift.models.data.appcms.ui.android.MetaPage;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsBaseActivity;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.fragment.ClearDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LINK_GRID_IMAGE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PLANMETA_DATA_TITLE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.SUBSCRIPTION_ITEM_BACKGROUND;
import static com.viewlift.tv.utility.Utils.getItemViewHeight;
import static com.viewlift.tv.utility.Utils.getItemViewWidth;
import static com.viewlift.tv.utility.Utils.getViewHeight;
import static com.viewlift.tv.utility.Utils.getViewWidth;

/**
 * Created by anas.azeem on 9/8/2017.
 * Owned by ViewLift, NYC
 */

public class TVCollectionGridItemView extends TVBaseView {
    private static final String TAG = "CollectionItemView";

    private final Layout parentLayout;
    private final boolean userParentLayout;
    private final Component component;
    private final String borderColor;
    protected int defaultWidth;
    protected int defaultHeight;
    private List<TVCollectionGridItemView.ItemContainer> childItems;
    private List<View> viewsToUpdateOnClickEvent;
    private boolean selectable;
    private CardView childrenContainer;
    private static int mPosition = 0;
    public static int thumbNailClickPosition = 0;
    private int selectedLanguageIndex = 0;
    private Resources resources;
    private AppCMSPresenter appCMSPresenter;

    @Inject
    public TVCollectionGridItemView(Context context,
                                    Layout parentLayout,
                                    boolean useParentLayout,
                                    Component component,
                                    int defaultWidth,
                                    int defaultHeight,
                                    String borderColor) {
        super(context);
        this.parentLayout = parentLayout;
        this.userParentLayout = useParentLayout;
        this.component = component;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.viewsToUpdateOnClickEvent = new ArrayList<>();
        this.borderColor = borderColor;
        appCMSPresenter =
                ((AppCMSApplication) context.getApplicationContext())
                        .getAppCMSPresenterComponent().appCMSPresenter();
        init();
    }

    @Override
    public void init() {
        int width = (int) getItemViewWidth(getContext(),
                component.getLayout(),
                defaultWidth);
        int height = (int) getItemViewHeight(getContext(),
                component.getLayout(),
                defaultHeight);

        FrameLayout.LayoutParams layoutParams;
        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(width, height);
        /*marginLayoutParams.setMargins(horizontalMargin,
                verticalMargin,
                horizontalMargin,
                verticalMargin);*/
        layoutParams = new FrameLayout.LayoutParams(marginLayoutParams);
        setLayoutParams(layoutParams);
        childItems = new ArrayList<>();
        if (component.getComponents() != null) {
            initializeComponentHasViewList(component.getComponents().size());
        }

        resources = appCMSPresenter.getLanguageResourcesFile();
    }

    @Override
    protected ViewGroup createChildrenContainer() {
        childrenContainer = new CardView(getContext());
        CardView.LayoutParams childContainerLayoutParams =
                new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        childrenContainer.setLayoutParams(childContainerLayoutParams);
        childrenContainer.setBackgroundResource(android.R.color.transparent);
        addView(childrenContainer);
        return childrenContainer;
    }

    public void addChild(TVCollectionGridItemView.ItemContainer itemContainer) {
        if (childrenContainer == null) {
            createChildrenContainer();
        }
        childItems.add(itemContainer);
        childrenContainer.addView(itemContainer.childView);
    }


    public View getChild(int index) {
        if (0 <= index && index < childItems.size()) {
            return childItems.get(index).childView;
        }
        return null;
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
    public Layout getLayout() {
        return component.getLayout();
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

    public void bindChild(Context context,
                          View view,
                          final ContentDatum data,
                          Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                          final OnClickHandler onClickHandler,
                          final AppCMSUIKeyType viewTypeKey,
                          int position,
                          Module module,
                          int currentSelectedLanguageIndex, Settings settings) {

        final Component childComponent = matchComponentToView(view);
        if (childComponent != null) {
            boolean bringToFront = true;
            AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
            AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
            if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
                if (componentKey == AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_WATCHLIST_THUMBNAIL_IMAGE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_EXPANDED_THUMBNAIL_IMAGE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_SEGMENT_THUMBNAIL_IMAGE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_EPISODE_THUMBNAIL_IMAGE_KEY ||
                        componentKey == PAGE_LINK_GRID_IMAGE) {
                    int childViewWidth = (int) getViewWidth(getContext(),
                            childComponent.getLayout(),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    int childViewHeight = (int) getViewHeight(getContext(),
                            childComponent.getLayout(),
                            getViewHeight(getContext(),
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (userParentLayout) {
                        childViewWidth = (int) getViewWidth(getContext(),
                                parentLayout,
                                defaultWidth);
                        childViewHeight = (int) getViewHeight(getContext(),
                                parentLayout,
                                defaultHeight);
                    }
                    if (data.getGist() != null && !TextUtils.isEmpty(data.getGist().getVideoImageUrl())) {
                        String imageUrl =
                                context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getVideoImageUrl(),
                                        childViewWidth,
                                        childViewHeight);
                        //Log.d(TAG, "Loading image: " + imageUrl);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(context)
                                .load(imageUrl)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .placeholder(R.drawable.video_image_placeholder)
                                        .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                .into((ImageView) view);

                    } else if (data.getGist() != null
                            && data.getGist().getContentType() != null
                            && data.getGist().getContentType().equalsIgnoreCase("season")
                            && data.getGist().getImages() != null
                            && data.getGist().getImages().get_16x9Image() != null
                            && data.getGist().getImages().get_16x9Image().getUrl() != null) {
                        Glide.with(context)
                                .load(data.getGist().getImages().get_16x9Image().getUrl())
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                                        .placeholder(R.drawable.video_image_placeholder)
                                        .error(ContextCompat.getDrawable(context, R.drawable.video_image_placeholder)))
                                .into((ImageView) view);

                    } else {
                        ((ImageView) view).setImageResource(R.drawable.video_image_placeholder);
                    }
                    bringToFront = false;
                    view.setFocusable(true);
                    if ((componentKey == AppCMSUIKeyType.PAGE_EPISODE_THUMBNAIL_IMAGE_KEY)) {
                        RecyclerView.LayoutManager layoutManager = ((RecyclerView) ((AppCmsHomeActivity) context).findViewById(R.id.season_table_view)).getLayoutManager();
                        View seasonTextView = ((TVCollectionGridItemView) layoutManager.findViewByPosition(module.getItemPosition())).getChild(0);
                        view.setOnFocusChangeListener((view1, hasFocus) -> {
                            try {
                                if (hasFocus) {
                                    setBackground(Utils.getNewsNavigationSelectedState(context, appCMSPresenter, Color.parseColor(borderColor)));
                                    if (((AppCmsHomeActivity) context).findViewById(R.id.segmentTableView) != null) {
                                        ((AppCMSTVTrayAdapter) ((RecyclerView) ((AppCmsHomeActivity) context).findViewById(R.id.segmentTableView)).getAdapter()).setContentData(data.getRelatedVideos());
                                        setLayoutParams(context, parentLayout, Utils.AnimationType.RIGHT);
                                        ((AppCmsHomeActivity) context).findViewById(R.id.episode_table_view_background).setVisibility(View.INVISIBLE);
                                        ((AppCmsHomeActivity) context).findViewById(R.id.season_table_view_background).setVisibility(View.VISIBLE);
                                        ((AppCmsHomeActivity) context).findViewById(R.id.segment_table_view_background).setVisibility(View.VISIBLE);
                                    }
                                    ((TextView) seasonTextView).setTextColor(Color.parseColor(Utils.getFocusBorderColor(context, appCMSPresenter)));
                                } else {
                                    setBackground(Utils.getNewsNavigationSelectedState(context, appCMSPresenter, Color.TRANSPARENT));
                                    ((TextView) seasonTextView).setTextColor(Utils.getTextColorDrawable(context, appCMSPresenter));
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        });
                        view.setOnClickListener(v -> {
                            appCMSPresenter.showLoadingDialog(true);
                            onClickHandler.play(childComponent,
                                    data);
                            view.setClickable(false);
                            new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);
                            mPosition = position;
                        });
                        view.setPadding(3, 3, 3, 3);
                    } else if ((componentKey == AppCMSUIKeyType.PAGE_SEGMENT_THUMBNAIL_IMAGE_KEY)) {
                        view.setPadding(3, 3, 3, 3);
                        view.setNextFocusLeftId(R.id.episode_table_view);
                        view.setOnFocusChangeListener((View view1, boolean b) -> {
                            if (b) {
                                setBackground(Utils.getNewsNavigationSelectedState(context, appCMSPresenter, Color.parseColor(borderColor)));
                                setLayoutParams(context, parentLayout, Utils.AnimationType.RIGHT);
                                ((AppCmsHomeActivity) context).findViewById(R.id.episode_table_view_background).setVisibility(View.VISIBLE);
                                ((AppCmsHomeActivity) context).findViewById(R.id.season_table_view_background).setVisibility(View.VISIBLE);
                                ((AppCmsHomeActivity) context).findViewById(R.id.segment_table_view_background).setVisibility(View.INVISIBLE);
                            } else {
                                setBackground(Utils.getNewsNavigationSelectedState(context, appCMSPresenter, Color.TRANSPARENT));
                            }
                        });
                        view.setOnClickListener(v -> {
                            if (data.getGist() != null) {
                                appCMSPresenter.showLoadingDialog(true);
                                onClickHandler.play(childComponent,
                                        data);
                                view.setClickable(false);
                                new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);
                                mPosition = position;
                            }
                        });
                    } else if ((componentKey == AppCMSUIKeyType.PAGE_WATCHLIST_THUMBNAIL_IMAGE_KEY)) {
                        view.requestFocus();
                        view.setBackground(Utils.getTrayBorder(context, borderColor, component));
                        view.setPadding(3, 3, 3, 3);
                        view.setOnClickListener(v -> {
                            appCMSPresenter.showLoadingDialog(true);
                            appCMSPresenter.navigateToExpandedDetailPage(module.getTitle(), module.getContentData(), position);
                            view.setClickable(false);
                            new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);
                            mPosition = position;
                        });

                        /*view.setOnFocusChangeListener((view1, b) -> {
                            if (null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(b);
                            }
                        });*/

                    } else if ((componentKey == AppCMSUIKeyType.PAGE_EXPANDED_THUMBNAIL_IMAGE_KEY)) {
                        view.setBackground(Utils.getTrayBorder(context, borderColor, component));
                        view.setPadding(3, 3, 3, 3);
                        view.setOnClickListener(v -> {
                            appCMSPresenter.showLoadingDialog(true);
                            appCMSPresenter.navigateToExpandedDetailPage(module.getTitle(), module.getContentData(), position);
                            view.setClickable(false);
                            new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);
                            mPosition = position;
                        });

                    } else {
                        view.setOnClickListener(v -> {
                            appCMSPresenter.showLoadingDialog(true);
                            onClickHandler.click(
                                    TVCollectionGridItemView.this,
                                    childComponent,
                                    data);
                            view.setClickable(false);
                            new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);
                            thumbNailClickPosition = position;
                        });

                        final boolean[] clickable = {true};
                        view.setOnKeyListener((v, keyCode, event) -> {
                            if (event.getAction() == KeyEvent.ACTION_DOWN
                                    && keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                                    && clickable[0]) {
                                appCMSPresenter.showLoadingDialog(true);
                                if ("SERIES".equalsIgnoreCase(data.getGist().getContentType())) {
                                    onClickHandler.click(
                                            TVCollectionGridItemView.this,
                                            childComponent,
                                            data);
                                } else {
                                    onClickHandler.play(
                                            childComponent,
                                            data);
                                }
                                clickable[0] = false;
                                new android.os.Handler().postDelayed(() -> clickable[0] = true, 3000);
                                return true;
                            } else if (event.getAction() == KeyEvent.ACTION_DOWN
                                    && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                                if (((AppCmsHomeActivity) context).findViewById(R.id.appcms_removeall) != null) {
                                    ((AppCmsHomeActivity) context).findViewById(R.id.appcms_removeall).setFocusable(false);
                                }
                            } else if (event.getAction() == KeyEvent.ACTION_DOWN
                                    && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                if (((AppCmsHomeActivity) context).findViewById(R.id.appcms_removeall) != null) {
                                    ((AppCmsHomeActivity) context).findViewById(R.id.appcms_removeall).setFocusable(true);
                                }
                            }
                            return false;
                        });
                        view.setBackground(Utils.getTrayBorder(context, borderColor, component));
                        view.setPadding(1, 3, 1, 3);

                        if (appCMSPresenter.isLeftNavigationEnabled()) {
                            view.setOnFocusChangeListener((view1, b) -> {
                                if (null != appCMSPresenter.getCurrentActivity()
                                        && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                    if (!appCMSPresenter.isNewsLeftNavigationEnabled()) {
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(b);
                                    }
                                }
                            });
                        }

                        if (componentKey == PAGE_LINK_GRID_IMAGE) {
                            view.setOnFocusChangeListener((view1, b) -> {
                                if (null != appCMSPresenter.getCurrentActivity()
                                        && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                    ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(position == 0);
                                }
                            });

                        } else if (componentKey == PAGE_THUMBNAIL_IMAGE_KEY) {
                            view.setPadding(6, 6, 6, 6);
                        }

                        if (position == thumbNailClickPosition) {
                            if (appCMSPresenter.isLeftNavigationEnabled()) {
                                view.requestFocus();
                            } else {
                                new android.os.Handler().postDelayed(view::requestFocus,10);
                            }
                        }
                }
                } else if (componentKey == AppCMSUIKeyType.PAGE_ICON_IMAGE_KEY) {
                    int childViewWidth = (int) getViewWidth(getContext(),
                            childComponent.getLayout(),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    int childViewHeight = (int) getViewHeight(getContext(),
                            childComponent.getLayout(),
                            getViewHeight(getContext(),
                                    component.getLayout(),
                                    ViewGroup.LayoutParams.WRAP_CONTENT));

                    if (!TextUtils.isEmpty(data.getGist().getVideoImageUrl())
                            && data.getGist().getVideoImageUrl().contains("http")) {
                        String imageUrl =
                                context.getString(R.string.app_cms_image_with_resize_query,
                                        data.getGist().getVideoImageUrl(),
                                        childViewWidth,
                                        childViewHeight);
                      /*  Log.d(TAG, "Loading image Title: " + data.getGist().getTitle());
                        Log.d(TAG, "Loading image: " + imageUrl);*/
                        Glide.with(context)
                                .load(imageUrl)
                                .apply(new RequestOptions().override(childViewWidth, childViewHeight))
//                                .centerCrop()
                                .into((ImageView) view);

                        bringToFront = false;
                    } else if (!TextUtils.isEmpty(data.getGist().getVideoImageUrl())) {
                        view.setPadding(0, 0, 0, 0);
                        ((ImageView) view).setImageResource(Utils.getIcon(data.getGist().getVideoImageUrl(), context, appCMSPresenter));
                    } else {
                        ((ImageView) view).setImageResource(android.R.color.transparent);
                    }

                    view.setFocusable(true);
                    view.setBackground(Utils.getMenuSelector(context, appCMSPresenter.getAppCtaBackgroundColor(),
                            appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getColor()));
                    if (!appCMSPresenter.isAutoPlayEnable()
                            && data.getGist().getTitle().contains("AUTOPLAY")) {
                        view.setFocusable(false);
                        view.setEnabled(false);
                        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        if (mPosition == 0)
                            mPosition = 1; // change this value to set the focus on next item.
                    }

                    view.setOnClickListener(v ->
                    {
                        String pageFunctionValue = appCMSPresenter.getPageFunctionValue(data.getGist().getId(), data.getGist().getTitle());

                        String title = data.getGist().getTitle();
                        String displayPath = data.getGist().getDisplayPath();
                        if (displayPath != null && displayPath.equals("autoPlay")) {
                            if (appCMSPresenter.getAutoplayEnabledUserPref(context)) {
                                data.getGist().setTitle(appCMSPresenter.getLocalisedStrings().getAutoplayOffMenu());
                                appCMSPresenter.setAutoplayEnabledUserPref(false);
                            } else {
                                data.getGist().setTitle(appCMSPresenter.getLocalisedStrings().getAutoplayOnMenu());
                                appCMSPresenter.setAutoplayEnabledUserPref(true);
                            }
                            appCMSPresenter.setAutoplayDefaultValueChangedPref(true);
                            onClickHandler.notifyData();
                        } else if (displayPath != null && displayPath.equalsIgnoreCase("closedCaptions")) {
                            if (appCMSPresenter.getClosedCaptionPreference()) {
                                data.getGist().setTitle(appCMSPresenter.getLocalisedStrings().getClosedCaptionOffMenu());
                                appCMSPresenter.setClosedCaptionPreference(false);
                            } else {
                                data.getGist().setTitle(appCMSPresenter.getLocalisedStrings().getClosedCaptionOnMenu());
                                appCMSPresenter.setClosedCaptionPreference(true);
                            }
                            onClickHandler.notifyData();
                        } else if (displayPath != null && displayPath.equals("manageSubscription")) {
                            if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed()) {
                                appCMSPresenter.showLoadingDialog(true);
                                appCMSPresenter.getSubscriptionData(
                                        appCMSUserSubscriptionPlanResult -> {

                                            String errorMsg;
                                            appCMSPresenter.showLoadingDialog(false);
                                            String platform;
                                            String status;
                                            if (appCMSUserSubscriptionPlanResult != null
                                                    && appCMSUserSubscriptionPlanResult.getSubscriptionInfo() != null
                                                    && appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform() != null) {
                                                platform = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getPlatform();
                                                status = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();

                                                if (status.equalsIgnoreCase("COMPLETED") ||
                                                        status.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
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
                                                    } else {
                                                        errorMsg = resources.getUIresource(context.getString(R.string.subscription_purchased_from_unknown_msg));
                                                    }
                                                } else {
                                                    errorMsg = appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText();
                                                }
                                            } else {
                                                errorMsg = appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText();
                                            }
                                            appCMSPresenter.openTVErrorDialog(errorMsg,
                                                    appCMSPresenter.getLocalisedStrings().getSubscriptionMsgHeaderText(), false);
                                        }, false);
                            } else {

                                MetaPage viewPlanPage = appCMSPresenter.getSubscriptionPage();
                                appCMSPresenter.navigateToTVPage(viewPlanPage.getPageId(),
                                        viewPlanPage.getPageName(),
                                        null,
                                        false,
                                        Uri.EMPTY,
                                        false,
                                        true, true, true, false, false);

                                /*String errorMsg = appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText();
                                String errorDialogHeader = appCMSPresenter.getLocalisedStrings().getSubscriptionMsgHeaderText();
                                if (!appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isNetworkConnected()) {
                                    appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.NAVIGATE_TO_HOME_FROM_LOGIN_DIALOG);
                                    String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
                                    String negativeButtonText = appCMSPresenter.getLocalisedStrings().getCancelCta();
                                    ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                            context,
                                            appCMSPresenter,
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                            errorDialogHeader,
                                            errorMsg,
                                            positiveButtonText,
                                            negativeButtonText,
                                             14
                                    );

                                    newFragment.setOnPositiveButtonClicked(s -> {
                                        NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                                        appCMSPresenter.navigateToTVPage(
                                                navigationUser.getPageId(),
                                                navigationUser.getTitle(),
                                                navigationUser.getUrl(),
                                                false,
                                                Uri.EMPTY,
                                                false,
                                                false,
                                                true, false, false, false);
                                    });
                                } else {
                                    appCMSPresenter.openTVErrorDialog(
                                            errorMsg,
                                            errorDialogHeader, false);
                                }*/
                            }
                        } else if (title.toUpperCase().contains("SIGN")
                                || title.toUpperCase().contains(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_sign_out_label)).toUpperCase())
                                || title.toUpperCase().contains(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.sign_in_text)).toUpperCase())
                                || title.toUpperCase().equalsIgnoreCase(appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getLoginCtaText())
                                || title.toUpperCase().equalsIgnoreCase(appCMSPresenter.getNavigation().getSettings().getPrimaryCta().getLogoutCtaText())) {
                            if (!appCMSPresenter.isUserLoggedIn()) {
                                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.NAVIGATE_TO_HOME_FROM_LOGIN_DIALOG);
                                NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                                if (navigationUser != null) {
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
                            } else {
                                appCMSPresenter.logoutTV();
                            }
                            //  navigationVisibilityListener.showSubNavigation(false, false);
                        } else if (title.toUpperCase().contains("ACCOUNT")) {
                            if (appCMSPresenter.isUserLoggedIn()) {
                                // navigationVisibilityListener.showSubNavigation(false, false);
                                appCMSPresenter.navigateToTVPage(
                                        data.getGist().getId(),
                                        data.getGist().getTitle(),
                                        data.getGist().getPermalink(),
                                        false,
                                        Uri.EMPTY,
                                        true,
                                        false,
                                        false, false, false, false);
                            } else {
                                ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                        context,
                                        appCMSPresenter,
                                        context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                        context.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                        resources.getUIresource(context.getString(R.string.sign_in_text)),
                                        resources.getUIresource(context.getString(R.string.open_account_dialog_text)),
                                        resources.getUIresource(context.getString(R.string.sign_in_text)),
                                        resources.getUIresource(context.getString(R.string.cancel)),
                                         14

                                );
                                newFragment.setOnPositiveButtonClicked(s -> {

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
                                });
                            }
                        } else if ((pageFunctionValue.equalsIgnoreCase(context.getString(R.string.app_cms_page_watchlist_title))
                                || pageFunctionValue.contains("Watchlist"))) {
                            if (appCMSPresenter.isUserLoggedIn()) {
                                //navigationVisibilityListener.showNavigation(false);
                                appCMSPresenter.showLoader();
                                appCMSPresenter.navigateToWatchlistPage(
                                        data.getGist().getId(),
                                        data.getGist().getTitle(),
                                        data.getGist().getPermalink(),
                                        false, false, false);
                            } else /*user not logged in*/ {
                                appCMSPresenter.showLoader();
                                String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
                                String negativeButtonText = appCMSPresenter.getLocalisedStrings().getCancelCta();
                                String dialogMessage = appCMSPresenter.getLocalisedStrings().getLoginToSeeWatchlistLabel();
                                ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                        context,
                                        appCMSPresenter,
                                        context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                        context.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                        positiveButtonText,
                                        dialogMessage,
                                        positiveButtonText,
                                        negativeButtonText,
                                         14

                                );
                                newFragment.setOnPositiveButtonClicked(s -> {

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
                                });
                            }
                        }

                        /*History*/
                        else if ((pageFunctionValue.equalsIgnoreCase(context.getString(R.string.app_cms_page_history_title))
                                || pageFunctionValue.contains("History"))) {
                            if (appCMSPresenter.isUserLoggedIn()) {
                                //navigationVisibilityListener.showNavigation(false);
                                appCMSPresenter.showLoader();
                                appCMSPresenter.navigateToHistoryPage(
                                        data.getGist().getId(),
                                        data.getGist().getTitle(),
                                        data.getGist().getPermalink(),
                                        false, false);
                            } else /*user not logged in*/ {
                                appCMSPresenter.showLoader();
                                String positiveButtonText = appCMSPresenter.getLocalisedStrings().getSignInText();
                                String negativeButtonText = appCMSPresenter.getLocalisedStrings().getCancelCta();
                                String dialogMessage = appCMSPresenter.getLocalisedStrings().getLoginToSeeHistoryLabel();
                                ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                        context,
                                        appCMSPresenter,
                                        context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                        context.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                        positiveButtonText,
                                        dialogMessage,
                                        positiveButtonText,
                                        negativeButtonText,
                                         14

                                );
                                newFragment.setOnPositiveButtonClicked(s -> {

                                    NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
                                    appCMSPresenter.navigateToTVPage(
                                            navigationUser.getPageId(),
                                            navigationUser.getTitle(),
                                            navigationUser.getUrl(),
                                            false,
                                            Uri.EMPTY,
                                            false,
                                            false,
                                            true, false, false, false);
                                });
                            }
                        } else {
                            appCMSPresenter.showLoadingDialog(true);
                            appCMSPresenter.navigateToTVPage(
                                    data.getGist().getId(),
                                    data.getGist().getTitle(),
                                    data.getGist().getPermalink(),
                                    false,
                                    Uri.EMPTY,
                                    true,
                                    false,
                                    false, false, false, false);
                        }
                        mPosition = position;
                        new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);

                    });


                    try {
                        ((ImageView) view).getDrawable().setTint(Utils.getComplimentColor(appCMSPresenter.getGeneralBackgroundColor()));
                        ((ImageView) view).getDrawable().setTintMode(PorterDuff.Mode.MULTIPLY);

                    } catch (Exception e) {
                    }

                    view.setOnFocusChangeListener(new OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean hasFocus) {
                            if (null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(position == 0);
                            }

                            /*if (view != null && ((ImageView) view).getDrawable() != null) {
                                if (hasFocus) {
                                    ((ImageView) view).getDrawable().setTint(Utils.getComplimentColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor())));
                                } else {
                                    ((ImageView) view).getDrawable().setTint(Utils.getComplimentColor(appCMSPresenter.getGeneralBackgroundColor()));
                                }
                            }*/
                        }
                    });
                    if (position == mPosition) {
                        view.requestFocus();
                    }


                }
            } else if (componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY) {
                if (componentKey == AppCMSUIKeyType.PAGE_PLAY_IMAGE_KEY) {
                    ((TextView) view).setText("");
                    view.setOnClickListener(v -> onClickHandler.click(
                            TVCollectionGridItemView.this,
                            childComponent,
                            data));
                    view.setFocusable(false);

                    if ("SERIES".equalsIgnoreCase(data.getGist().getContentType())
                            || "SEASON".equalsIgnoreCase(data.getGist().getContentType())
                            || (("PURCHASE".equalsIgnoreCase(data.getGist().getPurchaseType())
                            || "RENT".equalsIgnoreCase(data.getGist().getPurchaseType()))
                            && data.getGist().getScheduleStartDate() > System.currentTimeMillis())) {
                        view.setVisibility(View.INVISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                } else if (componentKey == AppCMSUIKeyType.PAGE_WATCHLIST_DELETE_ITEM_BUTTON) {
                    view.setNextFocusUpId(R.id.appcms_removeall);
                    view.setOnClickListener(v -> onClickHandler.delete(childComponent, data));
                    view.setFocusable(true);
                    view.setOnFocusChangeListener((v, hasFocus) -> {
                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(false);
                        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(!hasFocus);
                        }
                    });

                } else if (componentKey == AppCMSUIKeyType.CHANGE_LANGUAGE_KEY) {
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (position == currentSelectedLanguageIndex) {
                                return;
                            }
                            String changeLanguageText = appCMSPresenter.getLocalisedStrings().getLanguageAlertMessage();
                            String confirmMessage = resources.getStringValue(context.getString(R.string.changelanguageMessage), data.getGist().getTitle());
                            if (appCMSPresenter.getLocalisedStrings() != null
                                    && appCMSPresenter.getLocalisedStrings().getLanguageSelectionConfirmMessage() != null) {
                                confirmMessage = appCMSPresenter.getLocalisedStrings().getLanguageSelectionConfirmMessage();

                            }
                            String okText = appCMSPresenter.getLocalisedStrings().getContinueCtaText();
                            String cancelText = appCMSPresenter.getLocalisedStrings().getCancelCta();
                            ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                                    context,
                                    appCMSPresenter,
                                    getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                    getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                                    changeLanguageText,
                                    confirmMessage,
                                    okText,
                                    cancelText,
                                     14
                            );

                            newFragment.setOnPositiveButtonClicked(s -> {
                                onClickHandler.changeLanguage(childComponent, data);
                            });
                        }
                    });

                    view.setFocusable(true);
                    if (position == currentSelectedLanguageIndex) {
                        view.requestFocus();
                        ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getSelectedCta());
                    } else {
                        view.clearFocus();
                        ((Button) view).setText(appCMSPresenter.getLocalisedStrings().getSelectCta());
                    }
                    view.invalidate();
                }else if (componentKey == AppCMSUIKeyType.PAGE_LIVE_PLAYER_COMPONENT_TITLE_LABEL_KEY) {
                    if(data.getTitle() == null || data.getTitle().length() == 0){
                        return;
                    }
                    view.setFocusable(true);
                    view.setTag(R.id.nav_item_label, data);
                    ((Button) view).setText(data.getTitle());
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                    }
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    view.setBackground(
                            Utils.setButtonBackgroundSelectorForNewsTemplate(context,
                                    Utils.getFocusBorderColor(context, appCMSPresenter),
                                    Utils.getFocusColor(context, appCMSPresenter),
                                    component.getComponents().get(0),
                                    appCMSPresenter));

                    Glide.with(context)
                            .load(data.getImageUrl())
                            .into(new CustomTarget<Drawable>(24,24) {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    Utils.setImageColorFilter(null, resource, appCMSPresenter);
                                    resource.setBounds(component.getComponents().get(0).getLeftPadding(), 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                                    (((Button) view)).setCompoundDrawables(resource, null, null, null);
                                    (((Button) view)).setCompoundDrawablePadding(10);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    ((Button) view).setText(data.getTitle());
                                }
                            });

                    view.setOnClickListener(view1 -> {
                        ContentDatum data1 = (ContentDatum) view1.getTag(R.id.nav_item_label);
                        ContentDatum data_ = data1.getContentData().get(position);
                        appCMSPresenter.setSelectedSchedulePosition(position);
                        TVStandAlonePlayer02 tvStandAlonePlayer02 = (((AppCmsHomeActivity) context).findViewById(R.id.live_player_component_id));
                        tvStandAlonePlayer02.refreshData(data1);

                    });
                    ImageView rightArrowImageView = (((AppCmsHomeActivity) context).findViewById(R.id.right_arrow_id));
                    ImageView leftArrowImageView = (((AppCmsHomeActivity) context).findViewById(R.id.left_arrow_id));
                    view.setOnFocusChangeListener((OnFocusChangeListener) (v, hasFocus) -> {
                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(!hasFocus);
                        if(hasFocus){
                            if (null != appCMSPresenter
                                    && null != appCMSPresenter.getCurrentActivity()
                                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                if(position == 0) {
                                   ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                                    leftArrowImageView.setVisibility(GONE);
                                    rightArrowImageView.setVisibility(VISIBLE);
                                }else if (data != null && data.getContentData() != null &&
                                        data.getContentData().size() > 0 &&
                                        position == data.getContentData().size() -1){
                                    rightArrowImageView.setVisibility(GONE);
                                    leftArrowImageView.setVisibility(VISIBLE);
                                }
                            }
                        }else{
                            if(leftArrowImageView != null &&
                                    rightArrowImageView != null) {
                                leftArrowImageView.setVisibility(VISIBLE);
                                rightArrowImageView.setVisibility(VISIBLE);
                            }
                        }
                    });

                    view.setOnKeyListener((v, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                                && event.getAction() == KeyEvent.ACTION_DOWN) {
                            Utils.isPlayerSelected = false;
                            appCMSPresenter.sendBroadcastToHandleMiniPlayer(true);
                            v.setNextFocusDownId(R.id.browse_first_row);
                            if (appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id) != null) {
                                appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id).setVisibility(VISIBLE);
                                ((TVExpandedViewModule) appCMSPresenter.getCurrentActivity().findViewById(R.id.expanded_view_id)).updateTrailerID();
                            }

                        }
                        return false;
                    });

                }

            } else if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY) {
                if (componentKey == AppCMSUIKeyType.PAGE_EXPIRE_TIME_TITLE) {
                    if (data.getGist() != null) {
                        ((TextView) view).setSingleLine(true);
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setVisibility(View.VISIBLE);
                        ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners, null));
                        ((TextView) view).setText(data.getGist().getTitle());
                        ((TextView) view).setBackground(context.getResources().getDrawable(R.drawable.rectangle_with_round_corners, null));
//                        String epoch = data.getGist().getTransactionEndDate();
//                        long longEpoch = Long.parseLong(epoch);
                        long longEpoch = data.getGist().getTransactionEndDate();
                        Date endDate = new Date(longEpoch * 1000);
                        String timeDiff = calculateTimeDiff(endDate);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        if (!TextUtils.isEmpty(timeDiff)) {
                            ((TextView) view).setText(timeDiff);
                            ((TextView) view).setVisibility(View.VISIBLE);
                        } else {
                            ((TextView) view).setVisibility(View.GONE);
                        }

                    }
                } else if (componentKey == AppCMSUIKeyType.PAGE_ICON_LABEL_KEY) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                    }
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    ((TextView) view).setTypeface(Utils.getTypeFace(context,
                            appCMSPresenter,
                            childComponent));
                    view.setFocusable(false);
                    String textCase = childComponent.getTextCase();
                    if (textCase != null && !TextUtils.isEmpty(data.getGist().getTitle())) {
                        String title = data.getGist().getTitle();
                        if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_caps))) {
                            title = title.toUpperCase();
                        } else if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_small))) {
                            title = title.toLowerCase();
                        } else if (textCase.equalsIgnoreCase(context.getResources().getString(R.string.text_case_sentence))) {
                            String text = com.viewlift.Utils.convertStringIntoCamelCase(title);
                            if (text != null) {
                                title = text;
                            }
                        }
                        ((TextView) view).setText(title);
                    }
                } else if (componentKey == AppCMSUIKeyType.PAGE_THUMBNAIL_TITLE_KEY) {
                    if (childComponent.getNumberOfLines() != 0) {
                        ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                    }
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    ((TextView) view).setTypeface(Utils.getTypeFace(
                            context,
                            appCMSPresenter,
                            childComponent));
                    view.setFocusable(false);
                    //String textCase = childComponent.getTextCase();
                    if (data.getGist() != null && !TextUtils.isEmpty(data.getGist().getTitle())) {
                        String title = data.getGist().getTitle();
                        ((TextView) view).setText(title);
                    }
                }
                if (componentKey == AppCMSUIKeyType.PAGE_WATCHLIST_TITLE_LABEL) {
                    if (!TextUtils.isEmpty(data.getGist().getTitle())) {
                        ((TextView) view).setText(data.getGist().getTitle());
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        }
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setTypeface(Utils.getTypeFace(
                                context,
                                appCMSPresenter,
                                childComponent));
                        if (childComponent.getFontSize() != 0) {
                            ((TextView) view).setTextSize(childComponent.getFontSize());
                        }
                    } else {
                        ((TextView) view).setText(context.getString(R.string.blank_string));
                    }
                    view.setFocusable(false);

                } else if (componentKey == AppCMSUIKeyType.NAVIGATION_TITLE_LABEL) {
                    if (!TextUtils.isEmpty(data.getGist().getTitle())) {
                        ((TextView) view).setText(data.getGist().getTitle());
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        }
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);

                        ((TextView) view).setTypeface(Utils.getTypeFace(
                                context,
                                appCMSPresenter,
                                childComponent));

                    } else {
                        ((TextView) view).setText(context.getString(R.string.blank_string));
                    }

                    view.setTag(R.id.nav_item_label, data);
                    view.setBackground(Utils.getNavigationSelector(context, appCMSPresenter, false, Color.parseColor(appCMSPresenter.getAppBackgroundColor()), true));
                    ((TextView) view).setTextColor(Color.parseColor(Utils.getTextColor(context, appCMSPresenter)));

                    ((TextView) view).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ContentDatum data = (ContentDatum) v.getTag(R.id.nav_item_label);
                            if (data.getGist().getPermalink().contains("subnav") && data.getContentData() != null && data.getContentData().size() > 0) {
                                appCMSPresenter.navigateToSubNavigationPage(data.getId(), data.getGist().getTitle(), data.getGist().getPermalink(), null, null, data.getContentData(), false);
                            } else if (data.getGist().getPermalink().contains("watchlist")) {
                                appCMSPresenter.navigateToWatchlistPage(data.getId(), data.getGist().getTitle(), data.getGist().getPermalink(), false, false, false);
                            } else {
                                appCMSPresenter.navigateToTVPage(data.getId(), data.getGist().getTitle(), data.getGist().getPermalink(),
                                        false, Uri.EMPTY, true, false, false, false, false, false);
                            }
                        }
                    });
             ((TextView) view).setAlpha(.6f);
             view.setOnFocusChangeListener(new OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View v, boolean hasFocus) {
                     if(hasFocus){
                         ((TextView) view).setAlpha(1f);
                     }else{
                         ((TextView) view).setAlpha(.6f);
                     }
                 }
             });
             view.setFocusable(true);
             if(position == 0){
                 view.requestFocus();
             }

       }else if (componentKey == AppCMSUIKeyType.PAGE_LIBRARY_ITEM_TITLE_LABEL) {
                     if (data.getGist() != null
                            && data.getGist().getContentType().equalsIgnoreCase("season")){
                         if (!TextUtils.isEmpty(data.getGist().getSeriesTitle())) {
                             ((TextView) view).setText(data.getGist().getSeriesTitle());
                             if (childComponent.getNumberOfLines() != 0) {
                                 ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                             }
                             ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                         }
                         ((TextView) view).setTypeface(Utils.getTypeFace(
                                 context,
                                 appCMSPresenter,
                                 childComponent));
                         view.setFocusable(false);
                     }
                    if (!TextUtils.isEmpty(data.getGist().getTitle())) {
                        ((TextView) view).setText(data.getGist().getTitle());
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        }
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                    }
                    ((TextView) view).setTypeface(Utils.getTypeFace(
                            context,
                            appCMSPresenter,
                            childComponent));
                    view.setFocusable(false);

                } else if (componentKey == AppCMSUIKeyType.PAGE_WATCHLIST_DESCRIPTION_LABEL) {
                    if (data.getGist() != null && !TextUtils.isEmpty(data.getGist().getDescription())) {
                        ((TextView) view).setText(Html.fromHtml(data.getGist().getDescription()));
                        if (childComponent.getNumberOfLines() != 0) {
                            ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                        }
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setTypeface(Utils.getTypeFace(
                                context,
                                appCMSPresenter,
                                childComponent));
                        if (childComponent.getFontSize() != 0) {
                            ((TextView) view).setTextSize(childComponent.getFontSize());
                        }
                    } else {
                        ((TextView) view).setText(context.getString(R.string.blank_string));
                    }


                    view.setFocusable(false);
                } else if (componentKey == AppCMSUIKeyType.PAGE_HISTORY_LAST_ADDED_LABEL_KEY) {
                    if (data.getGist().getUpdateDate() != null
                        /*&& data.getUpdateDate() < System.currentTimeMillis()*/) {
                        try {
                            Calendar thatDay = Calendar.getInstance();
                            Date date = new Date(Long.parseLong(data.getGist().getUpdateDate()));
                            thatDay.setTime(date);
                            Calendar today = Calendar.getInstance();
                            long diff = today.getTimeInMillis() - thatDay.getTimeInMillis(); //result in millis
                            String timeFormat = appCMSPresenter.getTimeFormatForHistory(diff, module);
                            // long days = diff / (24 * 60 * 60 * 1000);
                            // String fmt = getResources().getText(R.string.item_shop).toString();
                            ((TextView) view).setText(com.viewlift.Utils.convertStringIntoCamelCase(timeFormat)/*MessageFormat.format(fmt, days)*/);
                        } catch (Exception e) {

                        }
                    }
                } else if (componentKey == AppCMSUIKeyType.PAGE_HISTORY_LAST_ADDED_DATE_LABEL_KEY) {
                    if (data.getGist().getUpdateDate() != null)
                        try {
                            ((TextView) view).setText(appCMSPresenter.getDateFormat(Long.parseLong(data.getGist().getUpdateDate()), "M/d/yyyy").replaceAll("/", "."));
                        } catch (Exception e) {

                        }
                } else if (componentKey == AppCMSUIKeyType.PAGE_TRAY_TITLE_KEY) {
                    if (data.getTitle() != null)
                        try {
                            ((TextView) view).setText(data.getTitle());
                            if (childComponent.getNumberOfLines() != 0) {
                                ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            }
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        } catch (Exception e) {

                        }
                } else if (componentKey == AppCMSUIKeyType.PAGE_SETTINGS_TITLE_KEY) {
                    if (data.getTitle() != null) {
                        try {
                            view.setFocusable(true);
                            view.setPadding(component.getPadding(), component.getPadding(), component.getPadding(), component.getPadding());
                            //setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(Utils.getSecondaryBackgroundColor(context,appCMSPresenter)),0,0, component.getCornerRadius()));
                            setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(component.getBackgroundColor()),0,0, component.getCornerRadius()));

                            ((TextView) view).setTypeface(Utils.getTypeFace(context,
                                    appCMSPresenter,
                                    childComponent));

                            ((TextView) view).setText(data.getTitle());

                            if (childComponent.getNumberOfLines() != 0) {
                                ((TextView) view).setMaxLines(childComponent.getNumberOfLines());
                            }
                            ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);

                            view.setOnFocusChangeListener((View view1, boolean b) -> {
                                if (b) {
                                    if (null != appCMSPresenter.getCurrentActivity()
                                            && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity) {
                                        ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(position == 0);
                                    }
                                    setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(borderColor),0,0, component.getCornerRadius()));
                                    Utils.setTextViewColor(appCMSPresenter,b,((TextView) view));
                                } else {
                                    //setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(Utils.getSecondaryBackgroundColor(context,appCMSPresenter)), 0,0,component.getCornerRadius()));
                                    setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(component.getBackgroundColor()),0,0, component.getCornerRadius()));

                                    Utils.setTextViewColor(appCMSPresenter,b,((TextView) view));
                                }
                            });

                            view.setOnClickListener(v -> {
                                onClickHandleOfSettingPage(context,data);

                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else if (componentKey == AppCMSUIKeyType.PAGE_TRAY_SEASON_TITLE_KEY) {
                    if (data.getTitle() != null)
                        try {
                            ((TextView) view).setText(data.getTitle().toUpperCase());
                            ((TextView) view).setTextColor(Utils.getTextColorDrawable(context, appCMSPresenter));
                            view.setFocusable(true);
                            view.requestFocus();
                            view.setNextFocusRightId(R.id.episode_table_view);
                            view.setOnClickListener(v -> {
                                if (((AppCmsHomeActivity) context).findViewById(R.id.episode_table_view) != null &&
                                        ((AppCmsHomeActivity) context).findViewById(R.id.segmentTableView) != null) {
                                    module.setItemPosition(position);
                                    ((AppCMSTVTrayAdapter) ((RecyclerView) ((AppCmsHomeActivity) context).findViewById(R.id.episode_table_view)).getAdapter()).setContentData(data.getSeason().get(position).getEpisodes());
                                    List<ContentDatum> segmentList = null;
                                    if (data.getSeason().get(position).getEpisodes() != null && data.getSeason().get(position).getEpisodes().size() > 0) {
                                        segmentList = data.getSeason().get(position).getEpisodes().get(0).getRelatedVideos();
                                    }
                                    ((AppCMSTVTrayAdapter) ((RecyclerView) ((AppCmsHomeActivity) context).findViewById(R.id.segmentTableView)).getAdapter()).setContentData(segmentList);
                                    view.setClickable(false);
                                    new android.os.Handler().postDelayed(() -> view.setClickable(true), 3000);
                                    mPosition = position;

                                }
                            });

                            view.setOnFocusChangeListener((view1, hasFocus) -> {
                                try {
                                    if (hasFocus) {
                                        ((AppCmsHomeActivity) context).findViewById(R.id.episode_table_view_background).setVisibility(View.VISIBLE);
                                        ((AppCmsHomeActivity) context).findViewById(R.id.season_table_view_background).setVisibility(View.INVISIBLE);
                                        ((AppCmsHomeActivity) context).findViewById(R.id.segment_table_view_background).setVisibility(View.VISIBLE);
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            });

                        } catch (Exception e) {

                        }
                } else if (componentKey == AppCMSUIKeyType.PAGE_WATCHLIST_SUBTITLE_LABEL) {
                    StringBuilder stringBuilder = new StringBuilder();

                    if (data.getGist() != null && data.getGist().getRuntime() > 0) {
                        stringBuilder.append(Utils.convertSecondsToTime(data.getGist().getRuntime()));
                    }

                    if (data.getContentDetails() != null
                            && data.getContentDetails().getAuthor() != null) {
                        if (stringBuilder.length() > 0) stringBuilder.append(" | ");
                        stringBuilder.append(data.getContentDetails().getAuthor());
                    }

                    if (data.getGist() != null && data.getGist().getPublishDate() != null) {
                        try {
                            SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy", new Locale(appCMSPresenter.getLanguage().getLanguageCode()));
                            // Create a calendar object that will convert the date and time value in milliseconds to date.
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(Long.parseLong(data.getGist().getPublishDate()));
                            String date = formatter.format(calendar.getTime());
                            if (stringBuilder.length() > 0) stringBuilder.append(" | ");
                            String publishtext = "Published on ";
                            if (null != resources) {
                                publishtext = resources.getUIresource("Published on ");
                            }
                            stringBuilder.append(publishtext);
                            stringBuilder.append(date);
                        } catch (Exception e) {
                        }
                    }

                    ((TextView) view).setText(stringBuilder);
                } else if (componentKey == AppCMSUIKeyType.PAGE_PLAN_TITLE_KEY) {
                    ((TextView) view).setText(data.getName());
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    ((TextView) view).setSelected(true);
                } else if (componentKey == AppCMSUIKeyType.PAGE_PLAN_PRICEINFO_KEY) {
                    //((TextView) view).setText(String.valueOf(data.getPlanDetails().get(0).getRecurringPaymentAmount()));
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

                        if (data.getPlanDetails() != null &&
                                !data.getPlanDetails().isEmpty() &&
                                data.getPlanDetails().get(planIndex) != null &&
                                data.getPlanDetails().get(planIndex).getStrikeThroughPrice() != 0) {

                            double recurringPaymentAmount = data.getPlanDetails().get(planIndex).getRecurringPaymentAmount();
                            String formattedRecurringPaymentAmount = context.getString(R.string.cost_with_fraction,
                                    recurringPaymentAmount);
                            if (recurringPaymentAmount - (int) recurringPaymentAmount == 0) {
                                formattedRecurringPaymentAmount = context.getString(R.string.cost_without_fraction,
                                        recurringPaymentAmount);
                            }

                            double strikeThroughPaymentAmount = data.getPlanDetails()
                                    .get(planIndex).getStrikeThroughPrice();
                            String formattedStrikeThroughPaymentAmount = context.getString(R.string.cost_with_fraction,
                                    strikeThroughPaymentAmount);
                            if (strikeThroughPaymentAmount - (int) strikeThroughPaymentAmount == 0) {
                                formattedStrikeThroughPaymentAmount = context.getString(R.string.cost_without_fraction,
                                        strikeThroughPaymentAmount);
                            }

                            StringBuilder stringBuilder = new StringBuilder();
                            if (currency != null) {
                                stringBuilder.append(currency.getCurrencyCode()).append(" ");
                            }
                            stringBuilder.append(formattedStrikeThroughPaymentAmount);

                            if (data.getPlanDetails().get(0).getRecurringPaymentAmount() != 0) {
                                int strikeThroughLength = stringBuilder.length();
                                stringBuilder.append(" ");
                                if (currency != null) {
                                    stringBuilder.append(currency.getCurrencyCode()).append(" ");
                                }
                                stringBuilder.append(String.valueOf(formattedRecurringPaymentAmount));

                                SpannableString spannableString =
                                        new SpannableString(stringBuilder.toString());
                                spannableString.setSpan(new StrikethroughSpan(), 0,
                                        strikeThroughLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                ((TextView) view).setText(spannableString);
                            } else {
                                ((TextView) view).setText(stringBuilder.toString());
                            }
//                            if (!appCMSPresenter.isSinglePlanFeatureAvailable()) {
                          /*  FrameLayout.LayoutParams layPar = (FrameLayout.LayoutParams) ((TextView) view).getLayoutParams();
                            layPar.gravity = Gravity.TOP;
                            view.setLayoutParams(layPar);*/
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
                                planAmt.append(currency.getCurrencyCode()).append(" ");
                            }
                            planAmt.append(formattedRecurringPaymentAmount);
                            ((TextView) view).setText(planAmt.toString());
                        }

                    }
                } else if (componentKey == PAGE_PLANMETA_DATA_TITLE_KEY) {
                    ((TextView) view).setText(data.getName());
                    ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);

                }
            } else if (componentKey.equals(AppCMSUIKeyType.PAGE_NEWS_THUMBNAIL_TIME_AND_DATE_KEY)) {
                if (data.getGist() != null && data.getGist().getContentType() != null &&
                        !(data.getGist().getContentType().equalsIgnoreCase("series")
                                || data.getGist().getContentType().equalsIgnoreCase("show"))) {
                    int padding = Integer.parseInt(
                            childComponent.getLayout().getTv().getPadding() != null
                                    ? childComponent.getLayout().getTv().getPadding()
                                    : "0");
                    view.setPadding(padding, padding, padding, padding);
                    final int SECONDS_PER_MINS = 60;
                    int duration = 0;
                    String suffixText = appCMSPresenter.getLocalisedStrings().getMinText();
                    duration = (int) (data.getGist().getRuntime() / SECONDS_PER_MINS);
                    if (duration == 0) {
                        suffixText = appCMSPresenter.getLocalisedStrings().getSecText();
                        duration = (int) ((data.getGist().getRuntime()) % SECONDS_PER_MINS);
                    }

                    StringBuilder runtimeText = new StringBuilder()
                            .append(duration)
                            .append(" ")
                            .append(suffixText);
                    ((TextView) view).setText(runtimeText);
                    ((TextView) view).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) view).setVisibility(View.GONE);
                }
            } else if (componentKey == AppCMSUIKeyType.PAGE_PROGRESS_VIEW_KEY) {
                int gridImagePadding = Integer.parseInt(
                        childComponent.getLayout().getTv().getPadding() != null
                                ? childComponent.getLayout().getTv().getPadding()
                                : "0");
                view.setPadding(gridImagePadding, 0, gridImagePadding, 0);
                ((ProgressBar) view).setProgressDrawable(Utils.getProgressDrawable(
                        context,
                        childComponent.getUnprogressColor(),
                        appCMSPresenter));
                ((ProgressBar) view).setMax(100);
                int progress = (int) Math.ceil(Utils.getPercentage(data.getGist().getRuntime(),
                        data.getGist().getWatchedTime()));
                //Log.d(TAG , "Progress Bar = "+progress);
                ((ProgressBar) view).setProgress(0);

                if (progress > 0) {
                    ((ProgressBar) view).setProgress(progress);
                    ((ProgressBar) view).setVisibility(View.VISIBLE);
                } else {
                    ((ProgressBar) view).setVisibility(View.INVISIBLE);
                }
                view.setFocusable(false);
            } else if (componentKey == SUBSCRIPTION_ITEM_BACKGROUND) {
                view.setFocusable(true);
                view.setTag(data);
                view.setBackground(context.getResources().getDrawable(R.drawable.rounded_gray_rectangle));
                view.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            view.setBackground(context.getResources().getDrawable(R.drawable.red_rounded_bg));
                            ((GradientDrawable) view.getBackground()).setColor(Color.parseColor(Utils.getFocusColor(context, appCMSPresenter)));
                        } else {
                            view.setBackground(context.getResources().getDrawable(R.drawable.rounded_gray_rectangle));
                        }
                    }
                });

                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentDatum contentDatum = (ContentDatum) v.getTag();
                        ((AppCmsBaseActivity) appCMSPresenter.getCurrentActivity()).initiateAmazonPurchase(contentDatum.getIdentifier(), contentDatum);
                    }
                });
                if (position == 0) {
                    view.requestFocus();
                }
            }
            view.forceLayout();
        }
    }

    public Component matchComponentToView(View view) {
        Component result = null;
        for (TVCollectionGridItemView.ItemContainer itemContainer : childItems) {
            if (itemContainer.childView == view) {
                return itemContainer.component;
            }
        }
        return result;
    }

    public List<View> getViewsToUpdateOnClickEvent() {
        return viewsToUpdateOnClickEvent;
    }

    public interface OnClickHandler {
        void click(TVCollectionGridItemView collectionGridItemView,
                   Component childComponent,
                   ContentDatum data);

        void play(Component childComponent, ContentDatum data);

        void delete(Component childComponent, ContentDatum data);

        void changeLanguage(Component childComponent, ContentDatum data);

        void notifyData();
    }

    public static class ItemContainer {
        View childView;
        Component component;

        public static class Builder {
            private TVCollectionGridItemView.ItemContainer itemContainer;

            public Builder() {
                itemContainer = new TVCollectionGridItemView.ItemContainer();
            }

            public TVCollectionGridItemView.ItemContainer.Builder childView(View childView) {
                itemContainer.childView = childView;
                return this;
            }

            public TVCollectionGridItemView.ItemContainer.Builder component(Component component) {
                itemContainer.component = component;
                return this;
            }

            public TVCollectionGridItemView.ItemContainer build() {
                return itemContainer;
            }
        }
    }


    private String calculateTimeDiff(Date endDate) {

        Date d1 = new Date();

        long diff = endDate.getTime() - d1.getTime();

        return appCMSPresenter.getRentExpirationFormat(diff);
        /*long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        long diffInDays = diff / (1000 * 60 * 60 * 24);

        if (diffInDays > 0)
            return diffInDays + " Days Remaining";
        else if (diffHours > 0)
            return diffHours + " Hours Remaining";
        else if (diffMinutes > 0)
            return diffMinutes + " Minutes Remaining";
        else if (diffSeconds > 0)
            return diffSeconds + " Seconds Remaining";
        else
            return null;*/

    }

    public ViewGroup getChildrenContainer() {
        if (childrenContainer == null) {
            return createChildrenContainer();
        }
        return childrenContainer;
    }

    private void setLayoutParams(Context context, Layout parentLayout, Utils.AnimationType animationType) {
        View recyclerView = ((AppCmsHomeActivity) context).findViewById(R.id.segmentTableView);
        View segmentTitle = ((AppCmsHomeActivity) context).findViewById(R.id.segment_tray_title);

        if (recyclerView != null) {
            recyclerView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
            FrameLayout.LayoutParams parentLayoutParams = (FrameLayout.LayoutParams) (recyclerView.getLayoutParams());
            parentLayoutParams.setMargins(parentLayout.getTv().getSwipeLeftMargin(), ((FrameLayout.LayoutParams) recyclerView.getLayoutParams()).topMargin, 0, 0);
            recyclerView.setLayoutParams(parentLayoutParams);
        }

        if (segmentTitle != null) {
            FrameLayout.LayoutParams titleLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            titleLayoutParams.setMargins(parentLayout.getTv().getSwipeLeftMargin(), ((FrameLayout.LayoutParams) segmentTitle.getLayoutParams()).topMargin, 0, 0);
            segmentTitle.setLayoutParams(titleLayoutParams);
        }
        //Utils.startAnimation(recyclerView,context,animationType);
    }

    private void onClickHandleOfSettingPage(Context context, ContentDatum data) {

        if (data.getId().equalsIgnoreCase(context.getResources().getString(R.string.app_cms_setting_page_log_out_key))) {
            appCMSPresenter.showLoader();
            appCMSPresenter.logoutTV();
        } else if (data.getId().equalsIgnoreCase(context.getResources().getString(R.string.app_cms_setting_page_personalization_view_key))) {
            appCMSPresenter.showLoader();
            String currentUserId = appCMSPresenter.getLoggedInUser() != null ? appCMSPresenter.getLoggedInUser() : "guest-user-id";
            appCMSPresenter.getUserRecommendedGenres(currentUserId, s -> {
                appCMSPresenter.setSelectedGenreString(s);
                appCMSPresenter.navigateToTVPage(
                        appCMSPresenter.getAccountSettingsPage().getPageId(),
                        data.getId(),
                        null,
                        false,
                        Uri.EMPTY,
                        false,
                        false,
                        false, false, false,false);
            },true, true);
        } else if (data.getId().equalsIgnoreCase(context.getResources().getString(R.string.app_cms_setting_page_subscription_view_key)) && appCMSPresenter.isFireTVSubscriptionEnabled() && !appCMSPresenter.isUserSubscribed()) {
            Utils.pageLoading(true, appCMSPresenter.getCurrentActivity());
            MetaPage viewPlanPage = appCMSPresenter.getSubscriptionPage();
            appCMSPresenter.navigateToTVPage(viewPlanPage.getPageId(),
                    viewPlanPage.getPageName(),
                    null,
                    false,
                    Uri.EMPTY,
                    false,
                    true, true, true, false, false);
            appCMSPresenter.setViewPlanPageOpenFromADialog(true);
        } else if (!com.viewlift.Utils.isFireTVDevice(context)) {
            appCMSPresenter.openTVErrorDialog(appCMSPresenter.getLocalisedStrings().getGuestUserSubsctiptionMsgText(), "", false);
        }else {
            appCMSPresenter.navigateToTVPage(
                    appCMSPresenter.getAccountSettingsPage().getPageId(),
                    data.getId(),
                    null,
                    false,
                    Uri.EMPTY,
                    false,
                    false,
                    false, false, false,false);
        }
    }

}


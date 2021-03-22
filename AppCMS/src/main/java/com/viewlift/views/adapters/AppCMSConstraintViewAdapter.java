package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.util.Log;
import com.viewlift.AppCMSApplication;
import com.viewlift.CMSColorUtils;
import com.viewlift.R;
import com.viewlift.audio.playback.AudioPlaylistHelper;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.android.LocalizationResult;
import com.viewlift.models.data.appcms.ui.main.GenericMessages;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.ShowDetailsPromoHandler;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.customviews.expandable.ExpandableLayout;
import com.viewlift.views.dialog.CustomShape;
import com.viewlift.views.rxbus.AppBus;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.functions.Consumer;

import static android.view.View.INVISIBLE;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_NEWS_TRAY_02;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06;
import static com.viewlift.views.customviews.BaseView.getFontSize;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSConstraintViewAdapter extends RecyclerView.Adapter<AppCMSConstraintViewAdapter.ViewHolder>
        implements AppCMSBaseAdapter {
    private static final String TAG = "AppCMSConstraintViewAdapter";
    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    protected Settings settings;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    ConstraintCollectionGridItemView.OnClickHandler onClickHandler;
    int defaultWidth;
    int defaultHeight;
    int priviosPosition = 0;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    private AppCMSUIKeyType viewTypeKey;
    private boolean isClickable;
    private String videoAction;
    private String showAction;
    private String watchTrailerAction;
    private MotionEvent lastTouchDownEvent;
    private int adapterSize = 0;
    String blockName;
    LocalizationResult localizationResult = null;
    GenericMessages genericMessages = null;
    private ConstraintViewCreator constraintViewCreator;
    private final String episodicContentType;
    private final String seasonContentType;
    private final String fullLengthFeatureType;
    private String openOptionsAction;
    private String personAction;
    private String bundleDetailAction;
    private final String person;
    int seeAllWidth = 0;
    int seeAllHeight = 0;
    float seeAllTextSize = 0;
    private final int TYPE_SEE_ALL = 1;
    private final int TYPE_DEFAULT = 2;
    private AppCMSUIKeyType uiBlockName;
    RecyclerView mRecyclerView;
    PageView pageView;
    String trayClickAction;
    ExpandableLayout expandableLayout;
    private int mTotalDy;
    private int selectedColor;
    private String currentPlayingVideoId = null;

    public AppCMSConstraintViewAdapter(Context context,
                                       Settings settings,
                                       Layout parentLayout,
                                       boolean useParentSize,
                                       Component component,
                                       Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                       Module moduleAPI,
                                       int defaultWidth,
                                       int defaultHeight,
                                       String viewType,
                                       AppCMSAndroidModules appCMSAndroidModules,
                                       String blockName,
                                       ConstraintViewCreator constraintViewCreator,
                                       RecyclerView mRecyclerView, PageView pageView, String trayClickAction) {

        this.mContext = context;
        this.trayClickAction = trayClickAction;
        this.constraintViewCreator = constraintViewCreator;
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.parentLayout = parentLayout;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.componentViewType = viewType;
        this.blockName = blockName;
        this.mRecyclerView = mRecyclerView;
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        this.uiBlockName = jsonValueKeyMap.get(blockName);
        this.pageView = pageView;
        this.selectedColor = appCMSPresenter.getButtonBorderColor();
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        SeasonTabSelectorBus.instanceOf().getSelectedTab().subscribe(new Consumer<List<ContentDatum>>() {
            @Override
            public void accept(List<ContentDatum> contentData) throws Throwable {
                adapterData = contentData;
                updateUserHistory(appCMSPresenter, adapterData);
                updateData(mRecyclerView, adapterData);
            }
        });
        SeasonTabSelectorBus.instanceOf().getCurrentPlayingVideoIdl().subscribe(new Consumer<String>() {
            @Override
            public void accept(String currentVideoId) throws Throwable {
                currentPlayingVideoId = currentVideoId;
                notifyDataSetChanged();
            }
        });
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            if ((uiBlockName == AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_01 ||
                    uiBlockName == AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_02)
                    && moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getBundleList() != null) {
                this.adapterData = moduleAPI.getContentData().get(0).getGist().getBundleList();
            } else if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                appCMSPresenter.setBottomSheetDialogdata(moduleAPI.getContentData().get(0).getSeason());
                if (appCMSPresenter.getSegmentListSelcted())
                    this.adapterData = appCMSPresenter.getEpisodeRelatedVideos();
                else if (appCMSPresenter.getEpisodeListSelcted())
                    this.adapterData = appCMSPresenter.getSeasonEpisodeAdapterData();
                else {
                    if (moduleAPI.getContentData().get(0) != null &&
                            moduleAPI.getContentData().get(0).getSeason() != null &&
                            moduleAPI.getContentData().get(0).getSeason().size() != 0 &&
                            moduleAPI.getContentData().get(0).getSeason().get(0) != null &&
                            moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes() != null)
                        this.adapterData = moduleAPI.getContentData().get(0).getSeason().get(0).getEpisodes();
                    if (appCMSPresenter.getseriesID() == null)
                        appCMSPresenter.setSeasonEpisodeAdapterData(adapterData);
                    if (adapterData == null && appCMSPresenter.getseriesID() == null)
                        this.adapterData = new ArrayList<>();
                    if (adapterData != null && adapterData.size() != 0)
                        appCMSPresenter.setRelatedVideos(adapterData.get(0).getRelatedVideos());
                    if (appCMSPresenter.getseriesID() != null) {
                        this.adapterData = appCMSPresenter.getSeasonEpisodeAdapterData();
                    }
                    appCMSPresenter.setEpisodeListSelcted(true);
                }
            } else
                this.adapterData = moduleAPI.getContentData();
        } else {
            this.adapterData = new ArrayList<>();
        }
        updateUserHistory(appCMSPresenter, adapterData);
        this.episodicContentType = context.getString(R.string.app_cms_episodic_key_type);
        this.seasonContentType = context.getString(R.string.app_cms_episodic_season_prefix);
        this.fullLengthFeatureType = context.getString(R.string.app_cms_full_length_feature_key_type);
        this.person = context.getString(R.string.app_cms_person_key_type);

        if (blockName == null) {
            this.uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (adapterData != null)
            adapterSize = adapterData.size();
        else
            adapterSize = 0;
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.videoAction = getVideoAction(context);
        this.showAction = getShowAction(context);
        this.personAction = getPersonAction(context);
        this.bundleDetailAction = getBundleDetailAction(context);
        this.watchTrailerAction = getWatchTrailerAction(context);
        this.openOptionsAction = getOpenOptionsAction(context);
        this.isClickable = true;
        this.setHasStableIds(false);
        this.appCMSAndroidModules = appCMSAndroidModules;
        if (appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getGenericMessages() != null
                && appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap() != null
                && appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap().size() > 0
                && appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()) != null) {
            localizationResult = appCMSPresenter.getAppCMSMain().getGenericMessages().getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode());
        }
        if (appCMSPresenter.getAppCMSMain().getGenericMessages() != null) {
            genericMessages = appCMSPresenter.getAppCMSMain().getGenericMessages();
        }

        setupCollapsibleView();


    }

    void setupCollapsibleView() {

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) pageView.findViewById(R.id.fight_scroll_id);
        expandableLayout = (ExpandableLayout) pageView.findViewById(R.id.collapsible_view);
        RecyclerView parent = pageView.findViewById(R.id.home_nested_scroll_view);
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        ConstraintLayout constraintLayoutfromheader = pageView.findViewById(R.id.page_view_constraint_header);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == event.ACTION_MOVE && expandableLayout != null && !expandableLayout.isExpanded()) {
                        expandableLayout.expand();
                    }
                    return false;
                }
            });
        }
        if (expandableLayout != null) {
            TextView title = expandableLayout.findViewById(R.id.contentTitle);
            TextView description = expandableLayout.findViewById(R.id.contentDescription);

            if (title != null && description != null) {
                title.setVisibility(View.VISIBLE);
                description.setVisibility(View.VISIBLE);
            }
        }

        new Handler().postDelayed(() -> {
            parent.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mTotalDy += dy;
                    if (layoutManager != null && expandableLayout != null) {
                        if (dy > 0 && mTotalDy >= expandableLayout.getHeight()) {
                            expandableLayout.collapse(true);

                        } else if (dy < 0 && (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING || recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) && !expandableLayout.isExpanded()) {
                            int pos = layoutManager.findFirstVisibleItemPosition();

                            if (layoutManager.findViewByPosition(pos).getTop() == 0 && pos == 0) {
                                expandableLayout.expand(true);

                            }
                            mTotalDy = 0;
                        }
                    }
                }
            });
        }, 1000);

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_DEFAULT) {
            ConstraintCollectionGridItemView view = constraintViewCreator.createConstraintCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    true,
                    component,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    useMarginsAsPercentages,
                    false,
                    this.componentViewType != null ? this.componentViewType : component.getView(),
                    true,
                    false,
                    viewTypeKey, blockName, moduleAPI.getMetadataMap());
            if (isPlanPage()) {
                GradientDrawable shape = new GradientDrawable();
                int cornerRadius = 0;
                shape.setCornerRadius(cornerRadius);
                shape.setStroke(2, selectedColor);
                view.setBackgroundDrawable(shape);
            }

            if (isPlanPage()) {
                ConstraintLayout.LayoutParams layParValue = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, defaultHeight);
                int constraint_MarginBottom = (int) mContext.getResources().getDimension(R.dimen.app_cms_view_plan_cell_margin_10);
                layParValue.setMargins(constraint_MarginBottom, constraint_MarginBottom / 2, constraint_MarginBottom, constraint_MarginBottom);
                view.setLayoutParams(layParValue);
            }

            return new ViewHolder(view);
        } else {

            if (component != null && component.getComponents() != null
                    && component.getComponents().size() > 0) {
                for (Component componentItem : component.getComponents()) {
                    if (componentItem.getKey().equalsIgnoreCase(mContext.getString(R.string.app_cms_page_thumbnail_image_key))) {
                        String ratio = BaseView.getViewRatio(mContext, componentItem.getLayout(), "16:9");
                        seeAllWidth = (int) BaseView.getViewWidth(mContext, componentItem.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
                        seeAllHeight = BaseView.getViewHeightByRatio(ratio, seeAllWidth);
                    } else if (componentItem.getKey().equalsIgnoreCase(mContext.getString(R.string.app_cms_page_thumbnail_title_key))) {
                        seeAllTextSize = getFontSize(mContext, componentItem.getLayout());
                    }
                }
            }

            TextView seeAllView = new TextView(mContext);
            ConstraintLayout.LayoutParams layPar = new ConstraintLayout.LayoutParams(seeAllWidth, seeAllHeight);
            seeAllView.setLayoutParams(layPar);
            seeAllView.setTextColor(appCMSPresenter.getGeneralTextColor());
            seeAllView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
            seeAllView.setTextSize(seeAllTextSize);
            /*seeAllView.setBackgroundColor(Color.parseColor("#444444"));*/
            seeAllView.setGravity(Gravity.CENTER);

            if (moduleAPI.getTitle() != null) {
                String seeAllTrayTitle = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.app_cms_see_all_tray_title), moduleAPI.getTitle());
                if (localizationResult != null && localizationResult.getSeeAllTray() != null)
                    seeAllTrayTitle = localizationResult.getSeeAllTray() + "\n" + moduleAPI.getTitle();
                else if (genericMessages != null && genericMessages.getSeeAllTray() != null)
                    seeAllTrayTitle = genericMessages.getSeeAllTray() + "\n" + moduleAPI.getTitle();

                Spannable text = new SpannableString(seeAllTrayTitle);
                text.setSpan(new RelativeSizeSpan(1.5f), 8, seeAllTrayTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                seeAllView.setText(text);
            }

            return new ViewHolder(seeAllView);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position < adapterSize) {
            return TYPE_DEFAULT;
        } else {
            if (this.adapterData.size() < adapterSize || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY || appCMSPresenter.getSeeAllPage() == null || !isSeeAllTray())
                return TYPE_DEFAULT;
            else
                return TYPE_SEE_ALL;
        }
    }

    @Override
    public int getItemCount() {
        if (this.adapterData == null)
            return 0;
        else if (this.adapterData.size() < adapterSize || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_01_KEY
                || this.viewTypeKey == AppCMSUIKeyType.PAGE_SEE_ALL_CATEGORY_02_KEY || appCMSPresenter.getSeeAllPage() == null || !isSeeAllTray())
            return adapterData.size();
        else
            return adapterSize + 1;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (0 <= position && position < adapterSize && holder.componentView != null) {
            bindView(holder.componentView, adapterData.get(position), position);
            if (isPlanPage()) {
                holder.componentView.setSelectable(true);
            }
            if (uiBlockName == UI_BLOCK_SHOW_DETAIL_06) {
                setSelectedEpisode(holder.componentView, adapterData.get(position));
            }
        }
        if (holder.seeAllView != null) {
            holder.seeAllView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String action = mContext.getString(R.string.app_cms_see_all_category_action);
                    if (moduleAPI != null && moduleAPI.getSettings() != null
                            && moduleAPI.getSettings().getSeeAllPermalink() != null
                            && moduleAPI.getId() != null) {
                        appCMSPresenter.setSeeAllModule(moduleAPI);
                        appCMSPresenter.setLastPage(false);
                        appCMSPresenter.setOffset(0);
                        appCMSPresenter.launchButtonSelectedAction(moduleAPI.getSettings().getSeeAllPermalink(),
                                action,
                                moduleAPI.getTitle(),
                                null,
                                null,
                                false,
                                0,
                                null);
                    }
                }
            });
        }
    }

    private void setSelectedEpisode(ConstraintCollectionGridItemView componentView, ContentDatum contentDatum) {
        View selector = null;
        TextView title = null;
        for (int i = 0; i < componentView.getChildItems().size(); i++) {
            ConstraintCollectionGridItemView.ItemContainer itemContainer = componentView.getChildItems().get(i);
            if (itemContainer.getComponent().getKey() != null) {
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_selection_view))) {
                    selector = (View) itemContainer.getChildView();
                }
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_thumbnail_title_key))) {
                    title = (TextView) itemContainer.getChildView();
                }
            }
        }
        if (selector != null && title != null) {
            String contentId = currentPlayingVideoId;
            if (contentId == null&&appCMSPresenter.getshowdetailsContenData()!=null&&appCMSPresenter.getshowdetailsContenData().getGist()!=null&&appCMSPresenter.getshowdetailsContenData().getGist().getId()!=null) {
                contentId = appCMSPresenter.getshowdetailsContenData().getGist().getId();
            } else {
                currentPlayingVideoId = null;
            }

            if (contentDatum.getGist().getId().equalsIgnoreCase(contentId)) {
                selector.setVisibility(View.VISIBLE);
                title.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
            } else {
                selector.setVisibility(View.GONE);
                title.setTextColor(ContextCompat.getColor(mContext, android.R.color.white));
            }
        }
    }


    @Override
    public void resetData(RecyclerView listView) {
        notifyDataSetChanged();
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        listView.setAdapter(null);
        adapterData = null;
        notifyDataSetChanged();
        adapterData = contentData;
        adapterSize = adapterData.size();
        notifyDataSetChanged();
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {
            onClickHandler = new ConstraintCollectionGridItemView.OnClickHandler() {
                @Override
                public void click(ConstraintCollectionGridItemView collectionGridItemView,
                                  Component childComponent,
                                  ContentDatum data, int clickPosition) {
                    if (isClickable) {
                        String action = "";
                        if (childComponent != null && !TextUtils.isEmpty(childComponent.getAction())) {
                            action = childComponent.getAction();
                        }
                        if ((uiBlockName == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_02)) {
                            subcriptionPlanClick(collectionGridItemView, data);
                        } else if (pageView!=null&&data.getGist() != null && data.getGist().getTitle() != null && (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) && !action.equalsIgnoreCase(mContext.getString(R.string.app_cms_hide_show_details_action))) {
                            appCMSPresenter.setshowdetailsClickPostionDate(data);
                            appCMSPresenter.setDefaultTrailerPlay(true);
                            data.setSelected(true);
                            ImageView imageView = null;
                            LinearLayout linearLayout = pageView.findViewById(R.id.page_view_linear_layout);
                            if (linearLayout != null) {
                                ConstraintLayout constraintLayoutfromheader = linearLayout.findViewById(R.id.page_view_constraint_header);
                                if (constraintLayoutfromheader != null) {
                                    CustomVideoPlayerView customVideoPlayerView = constraintLayoutfromheader.findViewById(R.id.videoTrailer);
                                    imageView = constraintLayoutfromheader.findViewById(R.id.trailerthumbnailImage);
                                    appCMSPresenter.setPlayshareControl(true);
                                    if (customVideoPlayerView != null && constraintViewCreator != null) {
                                        customVideoPlayerView.setVisibility(View.INVISIBLE);
                                        appCMSPresenter.refreshVideoData(data.getGist().getId(), contentDatum -> {
                                            {
                                                if (contentDatum != null) {
                                                    try {
                                                        String episodeTrailerID = null;
                                                        String episodePromoID = null;
                                                        if (contentDatum.getContentDetails() != null && contentDatum.getContentDetails().getTrailers() != null &&
                                                                contentDatum.getContentDetails().getTrailers().size() > 0 &&
                                                                contentDatum.getContentDetails().getTrailers().get(0).getId() != null) {
                                                            appCMSPresenter.setEpisodeTrailerID(contentDatum.getContentDetails().getTrailers().get(0).getId());
                                                            episodeTrailerID = contentDatum.getContentDetails().getTrailers().get(0).getId();
                                                        }
                                                        if (contentDatum.getContentDetails() != null && contentDatum.getContentDetails().getPromos() != null &&
                                                                contentDatum.getContentDetails().getPromos().size() > 0 &&
                                                                contentDatum.getContentDetails().getPromos().get(0).getId() != null) {
                                                            appCMSPresenter.setEpisodePromoID(contentDatum.getContentDetails().getPromos().get(0).getId());
                                                            episodePromoID = contentDatum.getContentDetails().getPromos().get(0).getId();
                                                        }
                                                        boolean isLiveStream = data.getGist() != null && data.getGist().isLiveStream();
                                                        if (((episodeTrailerID != null && !episodeTrailerID.isEmpty())) && isLiveStream && episodePromoID == null) {
                                                            constraintViewCreator.playVideo(customVideoPlayerView, true, episodePromoID);
                                                        } else if ((episodeTrailerID != null && !episodeTrailerID.isEmpty()) || ((episodePromoID != null && !episodePromoID.isEmpty()))) {
                                                            ShowDetailsPromoHandler.getInstance().openTrailer(customVideoPlayerView, (ImageView) constraintLayoutfromheader.findViewById(R.id.trailerthumbnailImage), episodeTrailerID, episodePromoID, isLiveStream, constraintViewCreator.getModuleApiData(), appCMSPresenter, data);
                                                            //  constraintViewCreator.openTrailer(customVideoPlayerView, (ImageView) constraintLayoutfromheader.findViewById(R.id.trailerthumbnailImage),episodeTrailerID ,episodePromoID, isLiveStream);
                                                        } else if (episodeTrailerID == null && episodePromoID == null) {
                                                            constraintViewCreator.playVideo(customVideoPlayerView, false, data.getGist().getId());
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }, null, true, false, null);
                                    }
                                    if (imageView != null)
                                        if (imageView.getVisibility() == View.GONE || imageView.getVisibility() == View.INVISIBLE)
                                            imageView.setVisibility(View.VISIBLE);
                                    if (constraintLayoutfromheader.findViewById(R.id.play) != null && ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.play).getParent()) != null) {
                                        ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.play).getParent()).setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                                    }
                                    if (constraintLayoutfromheader.findViewById(R.id.shareButton) != null && ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.shareButton).getParent()) != null){
                                        ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.shareButton).getParent()).setBackground(CustomShape.createRoundedRectangleDrawable(CMSColorUtils.INSTANCE.lightenColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()), 0.1f)));
                                    }
                                    if (constraintLayoutfromheader.findViewById(R.id.save) != null && ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.save).getParent()) != null){
                                        ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.save).getParent()).setBackground(CustomShape.createRoundedRectangleDrawable(CMSColorUtils.INSTANCE.lightenColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()), 0.1f)));
                                    }
                                    if (constraintLayoutfromheader.findViewById(R.id.downloadButton) != null && ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.downloadButton).getParent()) != null){
                                        ((ViewGroup) constraintLayoutfromheader.findViewById(R.id.downloadButton).getParent()).setBackground(CustomShape.createRoundedRectangleDrawable(CMSColorUtils.INSTANCE.lightenColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()), 0.1f)));
                                    }
                                   constraintLayoutfromheader.findViewById(R.id.play).setVisibility(View.VISIBLE);
                                    constraintLayoutfromheader.findViewById(R.id.shareButton).setVisibility(View.VISIBLE);
                                    constraintLayoutfromheader.findViewById(R.id.save).setVisibility(View.VISIBLE);
                                    if (appCMSPresenter.getTrailerPlayerView() != null)
                                        appCMSPresenter.getTrailerPlayerView().stopPlayer();
                                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                                        if (appCMSPresenter.isDownloadEnable())
                                            constraintLayoutfromheader.findViewById(R.id.downloadButton).setVisibility(View.VISIBLE);
                                        else
                                            constraintLayoutfromheader.findViewById(R.id.downloadButton).setVisibility(INVISIBLE);
                                    }
                                    appCMSPresenter.setShowDetailsGist(data.getGist());
                                    TextView title = constraintLayoutfromheader.findViewById(R.id.contentTitle);
                                    title.setText(data.getGist().getTitle());
                                    TextView contentDescription = constraintLayoutfromheader.findViewById(R.id.contentDescription);
                                    ImageButton expandDescription = constraintLayoutfromheader.findViewById(R.id.expandDescription);
                                    if (data.getGist().getDescription() != null && data.getGist().getTitle() != null) {
                                        int descLength = data.getGist().getDescription().length();
                                        int dis_max_length;
                                        if (BaseView.isTablet(mContext))
                                            dis_max_length = mContext.getResources().getInteger(R.integer.app_cms_beacon_buffering_timeout_msec);
                                        else
                                            dis_max_length = mContext.getResources().getInteger(R.integer.app_cms_mobile_show_details_discription);
                                        if (descLength > dis_max_length) {
                                            expandDescription.setVisibility(View.VISIBLE);
                                            data.getGist().setFullDescription(false);
                                            expandDescription.setImageResource(R.drawable.ic_arrow_down);
                                            expandDescription.setBackground(null);
                                            data.getGist().setFullDescription(false);
                                            CommonUtils.setMoreLinkInDescription(contentDescription, data.getGist().getDescription(), dis_max_length);
                                            // contentDescription.setSingleLine(false);
                                            //contentDescription.setEllipsize(TextUtils.TruncateAt.END);
                                            //contentDescription.setMaxLines(2);
                                            //ViewCreator.setMoreLinkInDescription(appCMSPresenter, contentDescription, data.getGist().getTitle(), data.getGist().getDescription(), dis_max_length, (Color.parseColor("#ffffff")));
                                        } else {
                                            contentDescription.setText(data.getGist().getDescription());
                                            expandDescription.setVisibility(View.GONE);
                                        }
                                    }
                                    if (data.getRelatedVideos() != null && !appCMSPresenter.getSegmentListSelcted())
                                        appCMSPresenter.setRelatedVideos(data.getRelatedVideos());
                                    String imageUrl = data.getGist().getVideoImageUrl();
                                    if (data.getGist() != null && data.getGist().getImageGist() != null
                                            && data.getGist().getImageGist().get_16x9() != null) {
                                        imageUrl = itemView.getContext().getString(R.string.app_cms_image_with_resize_query,
                                                data.getGist().getImageGist().get_16x9(),
                                                imageView.getWidth(), imageView.getHeight());
                                    }
                                    ContentDatum contentDatum = appCMSPresenter.getshowdetailsContenData();
                                    TextView downloadtext = constraintLayoutfromheader.findViewById(R.id.downloadButton);
                                    if (appCMSPresenter.getVideoPlayerViewCache(appCMSPresenter.getShowDeatil06TrailerPlayerKey()) != null) {
                                        appCMSPresenter.getVideoPlayerViewCache(appCMSPresenter.getShowDeatil06TrailerPlayerKey()).pausePlayer();
                                    }
                                    if (contentDatum != null && contentDatum.getGist() != null && contentDatum.getGist().getId() != null) {
                                        appCMSPresenter.setDownloadStatus(localisedStrings.getDownloadLowerCaseText());
                                        if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06) {
                                            appCMSPresenter.getUserVideoDownloadStatus(contentDatum.getGist().getId(),
                                                    videoDownloadStatus -> {
                                                        boolean isDownloaded = false, isPending = false;
                                                        if (videoDownloadStatus != null) {
                                                            if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_COMPLETED ||
                                                                    videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_SUCCESSFUL) {
                                                                isDownloaded = true;
                                                                appCMSPresenter.setUserAbleToDownload(isDownloaded);
                                                            }
                                                            if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_RUNNING ||
                                                                    videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PAUSED ||
                                                                    videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING) {
                                                                isPending = true;
                                                                appCMSPresenter.setUserAbleToDownload(isDownloaded);
                                                            }
                                                            if (!isDownloaded && !isPending) {
                                                                appCMSPresenter.setUserAbleToDownload(isDownloaded);
                                                            } else {
                                                                (downloadtext).setText(isDownloaded ? localisedStrings.getDownloadedLabelText() : localisedStrings.getDownloadingLabelText());
                                                                (downloadtext).setActivated(false);
                                                                appCMSPresenter.setUserAbleToDownload(isDownloaded);
                                                                appCMSPresenter.setDownloadStatus(isDownloaded ? localisedStrings.getDownloadedLabelText() : localisedStrings.getDownloadingLabelText());
                                                            }
                                                            downloadtext.setTag(null);
                                                        } else {
                                                            (downloadtext).setText(localisedStrings.getDownloadLowerCaseText());
                                                            downloadtext.setTag("download");
                                                            // (downloadtext).setText("download");
                                                            appCMSPresenter.setUserAbleToDownload(false);
                                                        }
                                                    },
                                                    appCMSPresenter.getAppPreference().getLoggedInUser());
                                        }
                                        String videoId = contentDatum.getGist().getOriginalObjectId();
                                        TextView saveUpdate = constraintLayoutfromheader.findViewById(R.id.save);
                                        ((TextView) constraintLayoutfromheader.findViewById(R.id.play)).setText(localisedStrings.getPlayText());
                                        if (videoId == null)
                                            videoId = contentDatum.getGist().getId();
                                        if (appCMSPresenter.isFilmAddedToWatchlist(videoId))
                                            saveUpdate.setText(localisedStrings.getRemoveFromWatchlistText());
                                        else
                                            saveUpdate.setText(localisedStrings.getAddToWatchlistText());
                                    }

                                    try {
                                        if (!TextUtils.isEmpty(imageUrl)) {
                                            RequestOptions requestOptions = new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                    .skipMemoryCache(true)
                                                    .override(((ImageView) imageView).getWidth(), ((ImageView) imageView).getHeight());
                                            Glide.with(mContext)
                                                    .load(imageUrl)
                                                    .dontAnimate()
                                                    .apply(requestOptions)
                                                    .into((ImageView) imageView);
                                        }
                                    } catch (IllegalArgumentException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            notifyDataSetChanged();
                        } else {
                            if (data.getGist() != null) {
                                appCMSPresenter.setPlaySource("");
                                if (moduleAPI.getContentData().get(0).getGist() != null &&
                                        moduleAPI.getContentData().get(0).getGist().getContentType() != null &&
                                        !TextUtils.isEmpty(moduleAPI.getContentData().get(0).getGist().getContentType()) && moduleAPI.getContentData().get(0).getGist().getContentType().contains("SERIES"))
                                    appCMSPresenter.setPlaySource(moduleAPI.getContentData().get(0).getGist().getTitle());
                                else
                                    appCMSPresenter.setPlaySource(moduleAPI.getTitle());
                                String permalink = data.getGist().getPermalink();
                                if (data.getGist() != null &&
                                        data.getGist().getRelatedVideos() != null &&
                                        data.getGist().getRelatedVideos().size() > clickPosition &&
                                        data.getGist().getRelatedVideos().get(clickPosition) != null &&
                                        data.getGist().getRelatedVideos().get(clickPosition).getGist() != null &&
                                        data.getGist().getRelatedVideos().get(clickPosition).getGist().getPermalink() != null) {
                                    permalink = data.getGist().getRelatedVideos().get(clickPosition).getGist().getPermalink();
                                }
                                action = videoAction;
                                if (childComponent != null && !TextUtils.isEmpty(childComponent.getAction())) {
                                    action = childComponent.getAction();
                                }
                                String title = data.getGist().getTitle();
                                String hlsUrl = getHlsUrl(data);

                                @SuppressWarnings("MismatchedReadAndWriteOfArray")
                                String[] extraData = new String[3];
                                extraData[0] = permalink;
                                extraData[1] = hlsUrl;
                                extraData[2] = data.getGist().getId();
                                //Log.d(TAG, "Launching " + permalink + ": " + action);
                                List<String> relatedVideoIds = null;
                                if (data.getContentDetails() != null &&
                                        data.getContentDetails().getRelatedVideoIds() != null) {
                                    relatedVideoIds = data.getContentDetails().getRelatedVideoIds();
                                }
                                int currentPlayingIndex = -1;
                                if (relatedVideoIds == null) {
                                    currentPlayingIndex = 0;
                                }

                                String contentType = "";

                                if (data.getGist() != null && data.getGist().getContentType() != null) {
                                    contentType = data.getGist().getContentType();
                                }

                                if (action.equalsIgnoreCase(mContext.getString(R.string.app_cms_hide_show_details_action))) {
                                    RecyclerView childcollectionGridItemView = pageView.findViewById(R.id.collectionGrid);
                                    if (childcollectionGridItemView != null) {
                                        try {
                                            RecyclerView collectionGridItemView1 = childcollectionGridItemView.findViewById(R.id.collectionGrid);
                                            RecyclerView.LayoutManager layoutManager = collectionGridItemView1.getLayoutManager();
                                            View view = layoutManager.findViewByPosition(clickPosition);
                                            TextView textView = view.findViewById(R.id.description);
                                            ImageButton moreButton = view.findViewById(R.id.expandableGrid);
                                            moreButton.setImageResource(R.drawable.ic_arrow_up);
                                            String showsdiscription = data.getGist().getDescription();
                                            if (showsdiscription != null && !showsdiscription.isEmpty()) {
                                                showsdiscription = showsdiscription.replaceAll("\\r|\\n", "");
                                                textView.setText(showsdiscription);
                                            }
                                            if (CommonUtils.getPosition() != null && Integer.parseInt(CommonUtils.getPosition()) != clickPosition) {
                                                notifyItemChanged(Integer.parseInt(CommonUtils.getPosition()));
                                                CommonUtils.setFullDiscription(false);
                                            } else if (CommonUtils.getPosition() != null && Integer.parseInt(CommonUtils.getPosition()) == clickPosition) {
                                                if (CommonUtils.getFullDiscription() == false) {
                                                    notifyItemChanged(Integer.parseInt(CommonUtils.getPosition()));
                                                    CommonUtils.setFullDiscription(true);
                                                } else {
                                                    CommonUtils.setFullDiscription(false);
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    CommonUtils.setpostion(String.valueOf(clickPosition));
                                    return;
                                }
                                if (action.contains(videoAction) && data.getGist() != null && data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase()))
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");
                                if (action.contains(videoAction) && data.getGist() != null && data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase()))
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");

                                if (action.contains(videoAction) && data.getGist() != null &&
                                        data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_series).toLowerCase()) &&
                                        data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_series).toLowerCase())) {
                                    action = showAction;
                                    trayClickAction = showAction;
                                    appCMSPresenter.setPlaySource("");
                                    appCMSPresenter.setPlaySource(childComponent.getType());
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Show Detail");
                                } else if (data.getGist() != null &&
                                        data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_bundle).toLowerCase()) &&
                                        data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_bundle).toLowerCase())) {
                                    action = bundleDetailAction;
                                    trayClickAction = bundleDetailAction;
                                } else if (action.contains(videoAction) && data.getGist() != null &&
                                        data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_season).toLowerCase())) {
                                    appCMSPresenter.setPlaySource("");
                                    appCMSPresenter.setPlaySource(childComponent.getType());
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Show Detail");
                                } else if (action.contains(videoAction) && data.getGist() != null &&
                                        data.getGist().getMediaType() != null &&
                                        !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                        data.getGist().getContentType() != null &&
                                        !data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()) &&
                                        !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");
                                } else if (data.getGist() != null &&
                                        data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                        data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Music");
                                } else if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Playlist Screen - " + data.getGist().getTitle());
                                }

                                if (action.contains("watchVideo") && moduleAPI.getTitle().contains("Continue Watching")) {
                                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Home Page");
                                    /*open show detail page*/
                                    if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null
                                            && appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() && data.getSeriesData() != null && data.getSeriesData().size() > 0
                                            && data.getSeriesData().get(0).getGist() != null && data.getSeriesData().get(0).getGist().getPermalink() != null && data.getGist() != null) {
                                        action = mContext.getString(R.string.app_cms_action_showvideopage_key);
                                        if (data.getSeriesData().get(0).getGist().getPermalink() != null) {
                                            permalink = data.getSeriesData().get(0).getGist().getPermalink();
                                        }
                                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                                action,
                                                title,
                                                null,
                                                data,
                                                false,
                                                currentPlayingIndex,
                                                relatedVideoIds);
                                    } else {
                                        data.setFromStandalone(false);
                                        if (!appCMSPresenter.launchVideoPlayer(data,
                                                data.getGist().getId(),
                                                currentPlayingIndex,
                                                relatedVideoIds,
                                                -1,
                                                action)) {
                                        }
                                    }
                                    return;
                                }
                                /*get audio details on tray click item and play song*/
                                if (data.getGist() != null &&
                                        data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                        data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                                    List<String> audioPlaylistId = new ArrayList<String>();
                                    audioPlaylistId.add(data.getGist().getId());
                                    AudioPlaylistHelper.getInstance().setCurrentPlaylistId(data.getGist().getId());
                                    AudioPlaylistHelper.getInstance().setCurrentPlaylistData(null);
                                    AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId);
                                    appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter
                                            .PRESENTER_PAGE_LOADING_ACTION));
                                    AudioPlaylistHelper.getInstance().playAudioOnClickItem(data.getGist().getId(), 0);
                                    return;
                                }

                                /*Get playlist data and open playlist page*/
                                if (data.getGist() != null && data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()) &&
                                        data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                                    appCMSPresenter.navigateToPlaylistPage(data.getGist().getId());
                                    return;
                                }
                                /*open show detail page*/
                                if (appCMSPresenter.getAppCMSMain() != null &&
                                        appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                                        appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() && action != null &&
                                        action.equalsIgnoreCase(mContext.getString(R.string.app_cms_action_detailvideopage_key)) &&
                                        data.getSeriesData() != null &&
                                        data.getSeriesData().size() > 0 && data.getSeriesData().get(0).getGist() != null &&
                                        data.getSeriesData().get(0).getGist().getPermalink() != null &&
                                        data.getGist() != null &&
                                        data.getGist().getMediaType() != null &&
                                        data.getGist().getMediaType().equalsIgnoreCase(mContext.getResources().getString(R.string.media_type_episode))) {
                                    action = mContext.getString(R.string.app_cms_action_showvideopage_key);
                                    if (data.getSeriesData().get(0).getGist().getPermalink() != null) {
                                        permalink = data.getSeriesData().get(0).getGist().getPermalink();
                                    }
                                    appCMSPresenter.launchButtonSelectedAction(permalink,
                                            action,
                                            title,
                                            null,
                                            data,
                                            false,
                                            currentPlayingIndex,
                                            relatedVideoIds);
                                }
                                if ((data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().contains(itemView.getContext().getString(R.string.media_type_episode).toLowerCase()))
                                        || (!TextUtils.isEmpty(contentType)
                                        && contentType.equalsIgnoreCase(itemView.getContext().getString(R.string.media_type_video)))) {
                                    trayClickAction = videoAction;
                                }
                                if (uiBlockName == UI_BLOCK_NEWS_TRAY_02) {
                                    appCMSPresenter.setIsPromoPage(true);
                                    action = trayClickAction = showAction;
                                } else {
                                    appCMSPresenter.setIsPromoPage(false);
                                }
                                /** Any change in bellow must be well tested on RTA tray Episode info should open Show details page*/
                                if (data.getSeriesData() != null &&
                                        data.getSeriesData().size() > 0 && data.getSeriesData().get(0).getGist() != null &&
                                        data.getSeriesData().get(0).getGist().getPermalink() != null && (appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail()
                                        || appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)) {
                                    permalink = data.getSeriesData().get(0).getGist().getPermalink();
                                    appCMSPresenter.setEpisodeId(data.getGist().getId());
                                    action = trayClickAction = showAction;
                                } else {
                                    appCMSPresenter.setEpisodeId(null);
                                }
                                if (data.getContentDetails() != null && data.getContentDetails().getPromos() != null &&
                                        data.getContentDetails().getPromos().size() > 0 &&
                                        data.getContentDetails().getPromos().get(0).getId() != null) {
                                    appCMSPresenter.setEpisodePromoID(data.getContentDetails().getPromos().get(0).getId());
                                } else {
                                    appCMSPresenter.setEpisodePromoID(null);
                                }
                                if (data.getContentDetails() != null && data.getContentDetails().getTrailers() != null &&
                                        data.getContentDetails().getTrailers().size() > 0 &&
                                        data.getContentDetails().getTrailers().get(0).getId() != null) {
                                    appCMSPresenter.setEpisodeTrailerID(data.getContentDetails().getTrailers().get(0).getId());

                                } else {
                                    appCMSPresenter.setEpisodeTrailerID(null);
                                }
                                if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null) {
                                    if (appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() && appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                                            && data.getGist() != null && data.getGist().getMediaType() != null
                                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase())) {
                                        trayClickAction = action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                    } else if (appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() && !appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                                            && data.getGist() != null && data.getGist().getMediaType() != null
                                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase())) {
                                        trayClickAction = action = mContext.getString(R.string.app_cms_action_detailvideopage_key);
                                    } else if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay() &&
                                            !appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail()
                                            && data.getGist() != null && data.getGist().getMediaType() != null
                                            && !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_series).toLowerCase())) {
                                        trayClickAction = action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                    }
                                }
                                if (trayClickAction != null && appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                                        && !appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail()
                                        && data.getGist() != null && data.getGist().getContentType() != null
                                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_video).toLowerCase())) {
                                    action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                    if (!appCMSPresenter.launchVideoPlayer(data,
                                            data.getGist().getId(),
                                            currentPlayingIndex,
                                            relatedVideoIds,
                                            -1,
                                            action)) {

                                    }
                                    return;
                                } else if (trayClickAction != null) {
                                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_01 || uiBlockName == AppCMSUIKeyType.UI_BLOCK_BUNDLE_DETAIL_02) {
                                       /* ContentTypeChecker contentTypeChecker = new ContentTypeChecker(mContext);
                                        boolean isTveContent = appCMSPresenter.getAppPreference().getTVEUserId() != null
                                                && contentTypeChecker.isContentTVEMonetization(data.getMonetizationModels());
                                        boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getUserPurchases() != null
                                                && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                                                && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), data.getGist().getId());
                                        boolean isContentFree = contentTypeChecker.isContentFREEMonetization(data.getMonetizationModels()) || contentTypeChecker.isContentAVODMonetization(data.getMonetizationModels());
                                        boolean isContentSvod = appCMSPresenter.isUserSubscribed() && contentTypeChecker.isContentSVODMonetization(data.getMonetizationModels());
                                        if (isTveContent || isContentPurchased || isContentFree || isContentSvod) {
                                            action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                            appCMSPresenter.launchButtonSelectedAction(permalink,
                                                    action,
                                                    title,
                                                    null,
                                                    data,
                                                    false,
                                                    currentPlayingIndex,
                                                    relatedVideoIds);
                                        } else {*/
                                        action = mContext.getString(R.string.app_cms_action_detailvideopage_key);
                                        if (data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_series).toLowerCase()))
                                            action = showAction;
                                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                                action,
                                                title,
                                                null,
                                                data,
                                                false,
                                                currentPlayingIndex,
                                                relatedVideoIds);
//                                        }

                                    } else {
                                        CommonUtils.recommendationPlay(appCMSPresenter, moduleAPI);
                                        appCMSPresenter.launchButtonSelectedAction(permalink,
                                                trayClickAction,
                                                title,
                                                null,
                                                data,
                                                false,
                                                currentPlayingIndex,
                                                null);
                                    }
                                    return;
                                }
                                //ARTICAL PAGE
                                if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_article_key_type).toLowerCase())) {
                                    appCMSPresenter.setCurrentArticleIndex(-1);
                                    appCMSPresenter.navigateToArticlePage(data.getGist().getId(), data.getGist().getTitle(), false, null, false);
                                    return;
                                }
                                //EVENT PAGE
                                if (data.getGist() != null && data.getGist().getContentType() != null
                                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_event_key_type).toLowerCase())) {
                                    appCMSPresenter.navigateToEventDetailPage(data.getGist().getPermalink());
                                    return;
                                }
                                if (data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_person).toLowerCase())) {
                                    appCMSPresenter.navigateToPersonDetailPage(permalink);
                                    return;
                                }
                                //PHOTOGALLERY
                                if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().contains("PHOTOGALLERY")) {
                                    appCMSPresenter.setCurrentPhotoGalleryIndex(clickPosition);
                                    appCMSPresenter.navigateToPhotoGalleryPage(data.getGist().getId(), data.getGist().getTitle(), adapterData, false);
                                    return;
                                }


                                if (trayClickAction != null && appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                                        && data.getGist() != null && data.getGist().getContentType() != null
                                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_video).toLowerCase())) {
                                    action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                    if (!appCMSPresenter.launchVideoPlayer(data,
                                            data.getGist().getId(),
                                            currentPlayingIndex,
                                            relatedVideoIds,
                                            -1,
                                            action)) {

                                    }
                                    return;
                                } else if (trayClickAction != null) {
                                    appCMSPresenter.launchButtonSelectedAction(permalink,
                                            trayClickAction,
                                            title,
                                            null,
                                            data,
                                            false,
                                            currentPlayingIndex,
                                            null);
                                    return;
                                }

                                if ((data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().contains(itemView.getContext().getString(R.string.media_type_episode).toLowerCase()))
                                        || (!TextUtils.isEmpty(contentType)
                                        && contentType.equalsIgnoreCase(itemView.getContext().getString(R.string.media_type_video)))) {
                                    trayClickAction = videoAction;
                                }

                                if (trayClickAction != null) {
                                    appCMSPresenter.launchButtonSelectedAction(permalink,
                                            trayClickAction,
                                            title,
                                            null,
                                            data,
                                            false,
                                            currentPlayingIndex,
                                            null);
                                    return;
                                }

                                if ((viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY)
                                        && (((data.getGist().getMediaType() == null || data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_bundle_key_type).toLowerCase()))
                                        && data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_bundle_key_type).toLowerCase())))) {
                                    action = itemView.getContext().getString(R.string.app_cms_action_detailbundlepage_key);
                                    if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                                            action,
                                            title,
                                            null,
                                            null,
                                            false,
                                            0,
                                            null)) {
                                    }
                                    return;
                                }

                                if ((viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH_MODULE_KEY
                                        || viewTypeKey == AppCMSUIKeyType.PAGE_AC_SEARCH02_MODULE_KEY
                                        || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY
                                        || viewTypeKey == AppCMSUIKeyType.PAGE_LIBRARY_02_MODULE_KEY)
                                        && ((data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_episode).toLowerCase())
                                        && data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_bundle_key_type).toLowerCase())))) {
                                    action = "lectureDetailPage";
                                }

                                if (data.getGist() != null && data.getGist().getContentType() != null
                                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_team_label).toLowerCase())) {
                                    appCMSPresenter.navigateToTeamDetailPage("acd7eac5-8bba-46bb-b337-b475df5ef680", data.getGist().getTitle(), false);
                                    return;
                                }
                                if (data.getGist() != null && data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()) &&
                                        data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                                    appCMSPresenter.navigateToPlaylistPage(data.getGist().getId());
                                    return;
                                }
                                if (action.contains(openOptionsAction)) {
                                    appCMSPresenter.launchButtonSelectedAction(permalink,
                                            openOptionsAction,
                                            title,
                                            null,
                                            data,
                                            false,
                                            currentPlayingIndex,
                                            relatedVideoIds);
                                    return;
                                }
                                if (contentType.equals(episodicContentType) || contentType.equalsIgnoreCase(seasonContentType) || uiBlockName == AppCMSUIKeyType.UI_BLOCK_SEARCH_04)
                                    action = showAction;
                                else if (contentType.equals(fullLengthFeatureType))
                                    action = action != null && action.equalsIgnoreCase("openOptionDialog") ? action : videoAction;
                                else if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_bundle).toLowerCase()))
                                    action = bundleDetailAction;
                                if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_article_key_type).toLowerCase())) {
                                    appCMSPresenter.setCurrentArticleIndex(-1);
                                    appCMSPresenter.navigateToArticlePage(data.getGist().getId(), data.getGist().getTitle(), false, null, false);
                                    return;
                                }
                                if (data.getGist() != null && data.getGist().getContentType() != null
                                        && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_event_key_type).toLowerCase())) {
                                    appCMSPresenter.navigateToEventDetailPage(data.getGist().getPermalink());
                                    return;
                                }
                                //PHOTOGALLERY
                                if (data.getGist() != null && data.getGist().getMediaType() != null
                                        && data.getGist().getMediaType().contains("PHOTOGALLERY")) {
                                    appCMSPresenter.setCurrentPhotoGalleryIndex(clickPosition);
                                    appCMSPresenter.navigateToPhotoGalleryPage(data.getGist().getId(), data.getGist().getTitle(), adapterData, false);
                                    return;
                                }
                                if (data.getGist() == null ||
                                        data.getGist().getContentType() == null) {
                                    if ((data.getPricing() != null && data.getPricing().getType() != null && ((data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_TVOD))
                                            || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_PPV))) ||
                                            (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                                    || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))))) {
                                        int finalCurrentPlayingIndex = currentPlayingIndex;
                                        List<String> finalRelatedVideoIds = relatedVideoIds;
                                        String finalAction = action;
                                        appCMSPresenter.getTransactionData(data.getGist().getId(), updatedContentDatum -> {
                                            boolean isPlayable = true;
                                            AppCMSTransactionDataValue objTransactionData = null;

                                            boolean isPurchased = false;

                                            if (updatedContentDatum != null &&
                                                    updatedContentDatum.size() > 0) {
                                                if (updatedContentDatum.get(0).size() == 0) {
                                                    isPlayable = false;
                                                } else {
                                                    objTransactionData = updatedContentDatum.get(0).get(data.getGist().getId());

                                                }
                                            } else {
                                                isPlayable = false;

                                            }

                                            /**
                                             * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
                                             * content is purchased by user ,if both conditions are false then show subscribe message.
                                             */
                                            if (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                                    || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {


                                                if (appCMSPresenter.isUserSubscribed() || isPurchased) {
                                                    isPlayable = true;
                                                } else {

                                                    if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                                                        appCMSPresenter.showSubscribeMessage();
                                                        isPlayable = false;
                                                        return;
                                                    }

                                                }
                                                if (isPlayable) {
                                                    appCMSPresenter.launchVideoPlayer(data,
                                                            data.getGist().getId(),
                                                            finalCurrentPlayingIndex,
                                                            finalRelatedVideoIds,
                                                            -1,
                                                            finalAction);
                                                    return;
                                                }
                                            }
                                            if (!isPlayable) {
                                                if (localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                                                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName()));
                                                else
                                                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));
                                            } else {

                                                String rentalPeriod = "";
                                                if (data.getPricing() != null && data.getPricing().getRent() != null &&
                                                        data.getPricing().getRent().getRentalPeriod() > 0) {
                                                    rentalPeriod = String.valueOf(data.getPricing().getRent().getRentalPeriod());
                                                }
                                                if (objTransactionData != null) {
                                                    rentalPeriod = String.valueOf(objTransactionData.getRentalPeriod());
                                                }

                                                boolean isShowRentalPeriodDialog = true;
                                                /**
                                                 * if transaction getdata api containf transaction end date .It means Rent API called before
                                                 * and we have shown rent period dialog before so dont need to show rent dialog again. else sow rent period dilaog
                                                 */
                                                isShowRentalPeriodDialog = (objTransactionData != null && objTransactionData.getPurchaseStatus() != null && objTransactionData.getPurchaseStatus().equalsIgnoreCase("RENTED"));
                                                if (isShowRentalPeriodDialog) {

                                                    if (rentalPeriod == null || TextUtils.isEmpty(rentalPeriod)) {
                                                        rentalPeriod = "0";
                                                    }
                                                    String msg = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.rent_time_dialog_mssg),
                                                            rentalPeriod);
                                                    if (localisedStrings.getRentTimeDialogMsg(rentalPeriod) != null)
                                                        msg = localisedStrings.getRentTimeDialogMsg(rentalPeriod);
                                                    appCMSPresenter.showRentTimeDialog(retry -> {
                                                        if (retry) {
                                                            appCMSPresenter.getRentalData(moduleAPI.getContentData().get(0).getGist().getId(), getRentalData -> {
                                                                if (!appCMSPresenter.launchVideoPlayer(data,
                                                                        data.getGist().getId(),
                                                                        finalCurrentPlayingIndex,
                                                                        finalRelatedVideoIds,
                                                                        -1,
                                                                        finalAction)) {

                                                                }
                                                            }, false, 0);
                                                        } else {
                                                        }
                                                    }, msg, "", "", "", true, true);
                                                } else {
                                                    if (!appCMSPresenter.launchVideoPlayer(data,
                                                            data.getGist().getId(),
                                                            finalCurrentPlayingIndex,
                                                            finalRelatedVideoIds,
                                                            -1,
                                                            finalAction)) {
                                                    }
                                                }

                                            }

                                        }, "Video");
                                    } else {
                                        if (!appCMSPresenter.isNetworkConnected() && !appCMSPresenter.isVideoDownloaded(data.getGist().getId())) { //checking isVideoOffline here to fix SVFA-1431 in offline mode
                                            appCMSPresenter.launchButtonSelectedAction(permalink,
                                                    action,
                                                    title,
                                                    null,
                                                    action.equalsIgnoreCase("openOptionDialog") ? data : null,
                                                    false,
                                                    currentPlayingIndex,
                                                    relatedVideoIds);


                                        } else if (!appCMSPresenter.launchVideoPlayer(data,
                                                data.getGist().getId(),
                                                currentPlayingIndex,
                                                relatedVideoIds,
                                                -1,
                                                action)) {
                                        }
                                    }
                                } else {
                                    if (data.getGist() != null && data.getGist().getContentType() != null &&
                                            data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_video).toLowerCase()) &&
                                            data.getGist().getMediaType() != null
                                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                                        action = mContext.getString(R.string.app_cms_video_playlist_page_action);
                                    }

                                    if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                                            && data.getGist() != null && data.getGist().getContentType() != null
                                            && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_video).toLowerCase())) {
                                        action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                                        if (!appCMSPresenter.launchVideoPlayer(data,
                                                data.getGist().getId(),
                                                currentPlayingIndex,
                                                relatedVideoIds,
                                                -1,
                                                action)) {
                                        }
                                    } else {
                                        if (data.getGist().getContentType() != null && data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_person).toLowerCase()))
                                            appCMSPresenter.navigateToPersonDetailPage(permalink);
                                        else {
                                            appCMSPresenter.launchButtonSelectedAction(permalink,
                                                    action,
                                                    title,
                                                    null,
                                                    /*action.equalsIgnoreCase("openOptionDialog") ? data : null*/data,
                                                    false,
                                                    currentPlayingIndex,
                                                    relatedVideoIds);
                                        }
                                    }
                                }
                            }
                        }

                    }
                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_PAYMENT_01 && !TextUtils.isEmpty(data.getTitle())) {
                        appCMSPresenter.launchButtonSelectedAction(null,
                                component.getTrayClickAction(),
                                "",
                                null,
                                data,
                                false,
                                -1,
                                null);
                        notifyDataSetChanged();
                    }
                }


                @Override
                public void play(Component childComponent, ContentDatum data) {
                    if (isClickable) {
                        if (data.getGist() != null) {
                            String filmId = null;
                            List<String> relatedVideoIds = null;
                            if (childComponent.getAction() != null && childComponent.getAction().contains(watchTrailerAction)) {
                                if (moduleAPI.getContentData().get(0) != null &&
                                        data.getShowDetails() != null &&
                                        data.getShowDetails().getTrailers() != null &&
                                        data.getShowDetails().getTrailers().get(0) != null &&
                                        data.getShowDetails().getTrailers().get(0).getId() != null) {
                                    filmId = moduleAPI.getContentData().get(0).getShowDetails().getTrailers().get(0).getId();
                                }
                            } else {
                                filmId = data.getGist().getId();
                                if (data.getContentDetails() != null &&
                                        data.getContentDetails().getRelatedVideoIds() != null) {
                                    relatedVideoIds = data.getContentDetails().getRelatedVideoIds();
                                }
                            }
                            String permaLink = data.getGist().getPermalink();
                            String title = data.getGist().getTitle();

                            int currentPlayingIndex = -1;
                            if (relatedVideoIds == null) {
                                currentPlayingIndex = 0;
                            }
                            if (!appCMSPresenter.launchVideoPlayer(data,
                                    filmId,
                                    currentPlayingIndex,
                                    relatedVideoIds,
                                    -1,
                                    null)) {
                            }
                        }
                    }
                }
            };
        }
        itemView.setOnTouchListener((View v, MotionEvent event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastTouchDownEvent = event;
            }

            return false;
        });
        itemView.setOnClickListener(v -> {
            if (isClickable && data != null && data.getGist() != null) {
                if (v instanceof CollectionGridItemView) {
                    try {
                        int eventX = (int) lastTouchDownEvent.getX();
                        int eventY = (int) lastTouchDownEvent.getY();
                        ViewGroup childContainer = ((CollectionGridItemView) v).getChildrenContainer();
                        int childrenCount = childContainer.getChildCount();
                        for (int i = 0; i < childrenCount; i++) {
                            View childView = childContainer.getChildAt(i);
                            if (childView instanceof Button) {
                                int[] childLocation = new int[2];
                                childView.getLocationOnScreen(childLocation);
                                int childX = childLocation[0] - 8;
                                int childY = childLocation[1] - 8;
                                int childWidth = childView.getWidth() + 8;
                                int childHeight = childView.getHeight() + 8;
                                if (childX <= eventX && eventX <= childX + childWidth) {
                                    if (childY <= eventY && eventY <= childY + childHeight) {
                                        childView.performClick();
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                String permalink = data.getGist().getPermalink();
                String title = data.getGist().getTitle();
                String action = videoAction;

                String contentType = "";

                if (data.getGist() != null && data.getGist().getContentType() != null) {
                    contentType = data.getGist().getContentType();
                }

                if (contentType.equals(episodicContentType)) {
                    action = showAction;
                } else if (contentType.equalsIgnoreCase(person)) {
                    action = personAction;
                } else if (contentType.equals(fullLengthFeatureType)) {
                    action = videoAction;
                }
                /*get audio details on tray click item and play song*/
                if (data.getGist() != null &&
                        data.getGist().getMediaType() != null &&
                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                        data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                    List<String> audioPlaylistId = new ArrayList<String>();
                    audioPlaylistId.add(data.getGist().getId());
                    AudioPlaylistHelper.getInstance().setCurrentPlaylistId(data.getGist().getId());
                    AudioPlaylistHelper.getInstance().setCurrentPlaylistData(null);
                    AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId);
                    appCMSPresenter.getCurrentActivity().sendBroadcast(new Intent(AppCMSPresenter
                            .PRESENTER_PAGE_LOADING_ACTION));
                    AudioPlaylistHelper.getInstance().playAudioOnClickItem(data.getGist().getId(), 0);
                    return;
                }

                /*Get playlist data and open playlist page*/
                if (data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_playlist).toLowerCase())) {
                    appCMSPresenter.navigateToPlaylistPage(data.getGist().getId());
                    return;
                }

                List<String> relatedVideoIds = null;
                if (data.getContentDetails() != null &&
                        data.getContentDetails().getRelatedVideoIds() != null) {
                    relatedVideoIds = data.getContentDetails().getRelatedVideoIds();
                }
                int currentPlayingIndex = -1;
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0;
                }

                if (data.getGist() == null ||
                        data.getGist().getContentType() == null) {
                    if (!appCMSPresenter.launchVideoPlayer(data,
                            data.getGist().getId(),
                            currentPlayingIndex,
                            relatedVideoIds,
                            -1,
                            action)) {

                    }
                } else if (viewTypeKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NO_DATA_TYPE) {
                } else {

                    if (uiBlockName != AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06 && !appCMSPresenter.launchButtonSelectedAction(permalink,
                            action,
                            title,
                            null,
                            null,
                            false,
                            currentPlayingIndex,
                            relatedVideoIds)) {
                    }

                }
            }

        });


        for (int i = 0; i < ((ConstraintCollectionGridItemView) itemView).getNumberOfChildren(); i++) {
            View childView = ((ConstraintCollectionGridItemView) itemView).getChild(i);
            if (component.getSettings() != null && component.getSettings().isNoInfo()
                    && componentViewType.contains("carousel") && !(childView instanceof ImageView)) {
                continue;
            }
            data.setModuleApi(moduleAPI);
            ((ConstraintCollectionGridItemView) itemView).bindChild(itemView.getContext(),
                    ((ConstraintCollectionGridItemView) itemView).getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.getBrandPrimaryCtaColor(),
                    appCMSPresenter,
                    position,
                    null,
                    blockName, moduleAPI.getMetadataMap());
        }
    }


    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }


    private String getShowAction(Context context) {
        return context.getString(R.string.app_cms_action_showvideopage_key);
    }


    private String getVideoAction(Context context) {
        return context.getString(R.string.app_cms_action_detailvideopage_key);
    }

    private String getWatchTrailerAction(Context context) {
        return context.getString(R.string.app_cms_action_watchtrailervideo_key);
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.componentView != null && holder.componentView.getChildItems() != null) {
            int childCount = holder.componentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = holder.componentView.getChild(i);
                if (child instanceof ImageView) {
                    try {
                        Glide.with(child.getContext()).clear(child);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintCollectionGridItemView componentView;
        TextView seeAllView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof ConstraintLayout)
                this.componentView = (ConstraintCollectionGridItemView) itemView;
        }

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.seeAllView = itemView;
        }

    }


    private String getPersonAction(Context context) {
        return context.getString(R.string.app_cms_instructor_details_action);
    }

    private String getBundleDetailAction(Context context) {
        return context.getString(R.string.app_cms_action_detailbundlepage_key);
    }

    private String getOpenOptionsAction(Context context) {
        return context.getString(R.string.app_cms_action_open_option_dialog);
    }


    private boolean isSeeAllTray() {
        return moduleAPI != null && moduleAPI.getSettings() != null &&
                (moduleAPI.getSettings().isSeeAll() ||
                        moduleAPI.getSettings().isSeeAllCard());
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

    private void subcriptionPlanClick(View itemView, ContentDatum data) {

        if (itemView instanceof CollectionGridItemView) {
            if (((CollectionGridItemView) itemView).isSelectable()) {
                double price = data.getPlanDetails().get(0).getStrikeThroughPrice();
                if (price == 0) {
                    price = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                }

                double discountedPrice = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                double discountedAmount = data.getPlanDetails().get(0).getDiscountedPrice();

                boolean upgradesAvailable = false;
                for (ContentDatum plan : adapterData) {
                    if (plan != null &&
                            plan.getPlanDetails() != null &&
                            !plan.getPlanDetails().isEmpty() &&
                            ((plan.getPlanDetails().get(0).getStrikeThroughPrice() != 0 &&
                                    price < plan.getPlanDetails().get(0).getStrikeThroughPrice()) ||
                                    (plan.getPlanDetails().get(0).getRecurringPaymentAmount() != 0 &&
                                            price < plan.getPlanDetails().get(0).getRecurringPaymentAmount()))) {
                        upgradesAvailable = true;
                    }
                }

                appCMSPresenter.initiateSignUpAndSubscription(data.getIdentifier(),
                        data.getId(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getName(),
                        price,
                        discountedPrice,
                        data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getRenewable(),
                        data.getRenewalCycleType(),
                        upgradesAvailable, discountedAmount,
                        data.getPlanDetails().get(0).getAllowedPayMethods(),
                        data.getPlanDetails().get(0).getCarrierBillingProviders());
            } else {
                itemView.performClick();
            }
        } else if (itemView instanceof ConstraintCollectionGridItemView) {
            if (((ConstraintCollectionGridItemView) itemView).isSelectable()) {
                double price = data.getPlanDetails().get(0).getStrikeThroughPrice();
                if (price == 0) {
                    price = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                }

                double discountedPrice = data.getPlanDetails().get(0).getRecurringPaymentAmount();
                double discountedAmount = data.getPlanDetails().get(0).getDiscountedPrice();

                boolean upgradesAvailable = false;
                for (ContentDatum plan : adapterData) {
                    if (plan != null &&
                            plan.getPlanDetails() != null &&
                            !plan.getPlanDetails().isEmpty() &&
                            ((plan.getPlanDetails().get(0).getStrikeThroughPrice() != 0 &&
                                    price < plan.getPlanDetails().get(0).getStrikeThroughPrice()) ||
                                    (plan.getPlanDetails().get(0).getRecurringPaymentAmount() != 0 &&
                                            price < plan.getPlanDetails().get(0).getRecurringPaymentAmount()))) {
                        upgradesAvailable = true;
                    }
                }

                appCMSPresenter.initiateSignUpAndSubscription(data.getIdentifier(),
                        data.getId(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getName(),
                        price,
                        discountedPrice,
                        data.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                        data.getPlanDetails().get(0).getCountryCode(),
                        data.getRenewable(),
                        data.getRenewalCycleType(),
                        upgradesAvailable, discountedAmount,
                        data.getPlanDetails().get(0).getAllowedPayMethods(),
                        data.getPlanDetails().get(0).getCarrierBillingProviders());
            } else {
                itemView.performClick();
            }
        }
    }

    private boolean isPlanPage() {
        return mContext.getString(R.string.ui_block_selectplan_02).equals(blockName);
    }

}

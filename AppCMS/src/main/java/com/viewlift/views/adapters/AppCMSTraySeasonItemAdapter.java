package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class AppCMSTraySeasonItemAdapter extends RecyclerView.Adapter<AppCMSTraySeasonItemAdapter.ViewHolder>
        implements OnInternalEvent, AppCMSBaseAdapter {

    private static final String TAG = "TraySeasonItemAdapter";
    private final String episodicContentType;
    private final String fullLengthFeatureType;
    protected List<ContentDatum> adapterData;
    protected List<Component> components;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    protected String defaultAction;
    private List<OnInternalEvent> receivers;
    private List<String> allEpisodeIds;
    private String moduleId;
    private ViewCreator.CollectionGridItemViewCreator collectionGridItemViewCreator;
    private CollectionGridItemView.OnClickHandler onClickHandler;
    private ConstraintCollectionGridItemView.OnClickHandler onClickHandlerConstraint;
    private ConstraintCollectionGridItemView constraintCollectionGridItemView;
    private boolean isClickable;
    boolean constrainteView;
    private ViewCreator viewCreator;
    private MotionEvent lastTouchDownEvent;
    AppCMSAndroidModules appCMSAndroidModules;
    Layout parentLayout;
    Settings settings;
    Component component;
    AppCMSUIKeyType parentViewType;
    String viewType;

    String componentViewType, seriesName;
    List<Season_> seasonList;
    RecyclerView mRecyclerView;
    private Map<String, Boolean> filmDownloadIconUpdatedMap;
    Context mContext;
    Module moduleAPI;
    String blockName;
    String trayClickAction;
    ConstraintViewCreator constraintViewCreator;

    public AppCMSTraySeasonItemAdapter(Context context,
                                       ViewCreator.CollectionGridItemViewCreator collectionGridItemViewCreator,
                                       AppCMSAndroidModules appCMSAndroidModules,
                                       Layout parentLayout,
                                       Settings settings,
                                       Component component,
                                       AppCMSUIKeyType parentViewType,
                                       Module moduleAPI,
                                       List<Component> components,
                                       List<String> allEpisodeIds,
                                       List<ContentDatum> allEpisodes,
                                       Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                       String viewType,
                                       RecyclerView mRecyclerView,
                                       String blockName,
                                       String trayClickAction,
                                       boolean constrainteView,
                                       ConstraintViewCreator constraintViewCreator) {
        this.collectionGridItemViewCreator = collectionGridItemViewCreator;
        seasonList = new ArrayList<>();
        seasonList.addAll(moduleAPI.getContentData().get(0).getSeason());
        Collections.reverse(seasonList);
        this.adapterData = seasonList.get(0).getEpisodes();
        this.sortData();
        this.mContext = context;
        this.moduleAPI = moduleAPI;
        this.blockName = blockName;
        this.seriesName = moduleAPI.getContentData().get(0).getGist().getTitle();
        this.constrainteView = constrainteView;
        this.appCMSAndroidModules = appCMSAndroidModules;
        this.parentLayout = parentLayout;
        this.component = component;
        this.settings = settings;
        this.parentViewType = parentViewType;
        this.viewType = viewType;
        this.constraintViewCreator = constraintViewCreator;
        if (viewType.contains(mContext.getResources().getString(R.string.app_cms_page_show_details_04_module_key))) {
            adapterData = new ArrayList<>();
            this.adapterData.addAll(allEpisodes);
        } else {
            seasonList.get(0).getEpisodes();
        }

        this.components = components;
        this.allEpisodeIds = allEpisodeIds;
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.defaultAction = getDefaultAction(context);
        this.trayClickAction = trayClickAction;
        this.receivers = new ArrayList<>();

        this.isClickable = true;
        this.mRecyclerView = mRecyclerView;

        this.episodicContentType = context.getString(R.string.app_cms_episodic_key_type);
        this.fullLengthFeatureType = context.getString(R.string.app_cms_full_length_feature_key_type);

        this.componentViewType = viewType;
        this.filmDownloadIconUpdatedMap = new HashMap<>();
        SeasonTabSelectorBus.instanceOf().getSelectedTab().subscribe(new Observer<List<ContentDatum>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<ContentDatum> adapterDataSeason) {
                adapterData = adapterDataSeason;
                updateData(mRecyclerView, adapterData);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void sortData() {
        if (adapterData != null) {
            // TODO: 10/3/17 Positioning of elements in adapter will be sorted at a later date.
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (adapterData.size() == 0) {
            return new ViewHolder(new View(parent.getContext()));
        }

        if (!constrainteView) {
            View view = collectionGridItemViewCreator.createView(parent.getContext());
            return new AppCMSTraySeasonItemAdapter.ViewHolder(view);

        } else {
            MetadataMap metadataMap = null;
            if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
                metadataMap = moduleAPI.getMetadataMap();
            constraintCollectionGridItemView = constraintViewCreator.createConstraintCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    true,
                    component,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    false,
                    true,
                    this.viewType,
                    true,
                    false,
                    parentViewType,
                    blockName,
                    metadataMap);
            return new AppCMSTraySeasonItemAdapter.ViewHolder(constraintCollectionGridItemView);
        }


    }


    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (adapterData != null && !adapterData.isEmpty()) {
            adapterData.get(position).setSeriesName(seriesName);
            if (0 <= position && position < adapterData.size() && holder.componentView != null) {
                for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                    if (holder.componentView.getChild(i) instanceof TextView) {
                        ((TextView) holder.componentView.getChild(i)).setText("");
                    }
                }
                bindView(holder.componentView, adapterData.get(position), position);
            } else if (0 <= position && position < adapterData.size() && holder.constraintComponentView != null) {
                for (int i = 0; i < holder.constraintComponentView.getNumberOfChildren(); i++) {
                    if (holder.constraintComponentView.getChild(i) instanceof TextView) {
                        ((TextView) holder.constraintComponentView.getChild(i)).setText("");
                    }
                }
                bindView(holder.constraintComponentView, adapterData.get(position), position);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return adapterData != null && !adapterData.isEmpty() ? adapterData.size() : 1;
    }

    @Override
    public void addReceiver(OnInternalEvent e) {
        //
    }

    @Override
    public void sendEvent(InternalEvent<?> event) {
        for (OnInternalEvent internalEvent : receivers) {
            internalEvent.receiveEvent(event);
        }
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
    public void receiveEvent(InternalEvent<?> event) {
        if (event.getEventData() instanceof List<?>) {
            try {
                adapterData = (List<ContentDatum>) event.getEventData();
            } catch (Exception e) {

            }
        } else {
            adapterData.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public void cancel(boolean cancel) {
        //
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }

    private void bindView(View itemView,
                          final ContentDatum data,
                          int position) {
        if (onClickHandlerConstraint == null) {
            onClickHandlerConstraint = new ConstraintCollectionGridItemView.OnClickHandler() {
                @Override
                public void click(ConstraintCollectionGridItemView collectionGridItemView, Component childComponent, ContentDatum data, int clickPosition) {
                    trayClick(data, childComponent);
                }

                @Override
                public void play(Component childComponent, ContentDatum data) {

                    playClick(data);
                }
            };
        }

        if (onClickHandler == null) {
            onClickHandler = new CollectionGridItemView.OnClickHandler() {
                @Override
                public void click(CollectionGridItemView collectionGridItemView,
                                  Component childComponent,
                                  ContentDatum data,
                                  int position) {
                    trayClick(data, childComponent);
                }

                @Override
                public void play(Component childComponent, ContentDatum data) {
                    playClick(data);
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
            if (isClickable) {
                if (v instanceof CollectionGridItemView) {
                    try {
                        int eventX = (int) lastTouchDownEvent.getX();
                        int eventY = (int) lastTouchDownEvent.getY();
                        int childrenCount = 0;
                        ViewGroup childContainer = ((CollectionGridItemView) v).getChildrenContainer();
                        childrenCount = childContainer.getChildCount();
                        childrenCount = ((ConstraintCollectionGridItemView) v).getChildItems().size();
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

                    }
                }

                String permalink = data.getGist().getPermalink();
                String title = data.getGist().getTitle();
                String action = defaultAction;

                //Log.d(TAG, "Launching " + permalink + ":" + action);
                List<String> relatedVideoIds = allEpisodeIds;
                int currentPlayingIndex = -1;
                if (allEpisodeIds != null) {
                    int currentEpisodeIndex = allEpisodeIds.indexOf(data.getGist().getId());
                    if (currentEpisodeIndex < allEpisodeIds.size()) {
                        currentPlayingIndex = currentEpisodeIndex;
                    }
                }
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0;
                }

                if (v instanceof ConstraintCollectionGridItemView) {
                    // launchScreeenPlayer(data, currentPlayingIndex, relatedVideoIds, action, title, permalink);
                    int eventX = (int) lastTouchDownEvent.getX();
                    int eventY = (int) lastTouchDownEvent.getY();
                    View childView = v.findViewById(R.id.playImage);

                    if (childView instanceof ImageButton) {
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
            }
        });

        /*for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    Color.parseColor(appCMSPresenter.getAppTextColor()),
                    appCMSPresenter,
                    position, null,blockName);
        }*/
        if (itemView instanceof ConstraintCollectionGridItemView) {
            for (int i = 0; i < ((ConstraintCollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                View childView = ((ConstraintCollectionGridItemView) itemView).getChild(i);
                data.setModuleApi(moduleAPI);
                ((ConstraintCollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        childView,
                        data,
                        jsonValueKeyMap,
                        onClickHandlerConstraint,
                        componentViewType,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        appCMSPresenter,
                        position,
                        null,
                        blockName,
                        moduleAPI.getMetadataMap());
            }
        } else {
            for (int i = 0; i < ((CollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                ((CollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        ((CollectionGridItemView) itemView).getChild(i),
                        data,
                        jsonValueKeyMap,
                        onClickHandler,
                        componentViewType,
                        appCMSPresenter.getBrandPrimaryCtaColor(),
                        appCMSPresenter,
                        position,
                        null,
                        blockName,
                        moduleAPI.getMetadataMap());
            }
        }
    }

    @Override
    public void resetData(RecyclerView listView) {
        //
    }


    void launchScreeenPlayer(ContentDatum data, int finalCurrentPlayingIndex, List<String> relatedVideoIds, String finalAction, String title, String permalink) {
        data.setModuleApi(moduleAPI);
        if (data.getGist() == null ||
                data.getGist().getContentType() == null) {
            if (!appCMSPresenter.launchVideoPlayer(data,
                    data.getGist().getId(),
                    finalCurrentPlayingIndex,
                    relatedVideoIds,
                    -1,
                    finalAction)) {
                //Log.e(TAG, "Could not launch action: " +
                //                                                " permalink: " +
                //                                                permalink +
                //                                                " action: " +
                //                                                action);
            }
        } else {
            if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                    finalAction,
                    title,
                    null,
                    data,
                    false,
                    finalCurrentPlayingIndex,
                    relatedVideoIds)) {
                //Log.e(TAG, "Could not launch action: " +
                //                                                " permalink: " +
                //                                                permalink +
                //                                                " action: " +
                //                                                action);
            }
        }
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        listView.setAdapter(null);
        adapterData = null;
        adapterData = contentData;
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();

    }

    @Override
    public void setClickable(boolean clickable) {

    }

    private String getDefaultAction(Context context) {
        return context.getString(R.string.app_cms_action_videopage_key);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;
        ConstraintCollectionGridItemView constraintComponentView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof CollectionGridItemView) {
                this.componentView = (CollectionGridItemView) itemView;
            } else if (itemView instanceof ConstraintLayout) {
                this.constraintComponentView = (ConstraintCollectionGridItemView) itemView;
            }
        }
    }

    void trayClick(ContentDatum data, Component childComponent) {
        if (isClickable && !appCMSPresenter.isAddOnFragmentVisible()) {
            AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
            /**
             * if click happened from description text then no need to show play screen as more fragment open
             */
            if (componentKey == AppCMSUIKeyType.PAGE_API_DESCRIPTION) {
                return;
            }
            if (moduleAPI.getContentData().get(0).getGist() != null && moduleAPI.getContentData().get(0).getGist().getParentalRating() != null)
                data.setShowParentalRating(moduleAPI.getContentData().get(0).getGist().getParentalRating());
            appCMSPresenter.setShowDatum(data);
            if (data.getGist() != null) {
                //Log.d(TAG, "Clicked on item: " + data.getGist().getTitle());
                String permalink = data.getGist().getPermalink();
                String action = defaultAction;
                if (childComponent != null && !TextUtils.isEmpty(childComponent.getAction())) {
                    action = childComponent.getAction();
                }
                String title = data.getGist().getTitle();
                String hlsUrl = getHlsUrl(data);

                if (componentKey != AppCMSUIKeyType.PAGE_PLAY_IMAGE1_KEY &&
                        trayClickAction != null && trayClickAction.contains(mContext.getResources().getString(R.string.app_cms_action_detailvideopage_key))) {
                    appCMSPresenter.launchButtonSelectedAction(permalink,
                            trayClickAction,
                            title,
                            null,
                            null,
                            false,
                            0,
                            null);
                    return;
                }

                @SuppressWarnings("MismatchedReadAndWriteOfArray")
                String[] extraData = new String[3];
                extraData[0] = permalink;
                extraData[1] = hlsUrl;
                extraData[2] = data.getGist().getId();
                //Log.d(TAG, "Launching " + permalink + ": " + action);
                List<String> relatedVideoIds = allEpisodeIds;
                int currentPlayingIndex = -1;
                if (allEpisodeIds != null) {
                    int currentEpisodeIndex = allEpisodeIds.indexOf(data.getGist().getId());
                    if (currentEpisodeIndex < allEpisodeIds.size()) {
                        currentPlayingIndex = currentEpisodeIndex;
                    }
                }
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0;
                }
                /**
                 * if pricing type is svod+tvod or svod+PPV
                 * then first check if user is subscribed or not ,if not then show error message,
                 * If yes ,then check tvod or ppv purchased conditions as other tvod/ppv contents
                 */
                if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled() && (data != null &&
                        data.getPricing() != null &&
                        data.getPricing().getType() != null) &&
                        ((data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_TVOD))
                                || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_PPV))) ||
                                (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                        || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))))) {
                    int finalCurrentPlayingIndex = currentPlayingIndex;
                    List<String> finalRelatedVideoIds = relatedVideoIds;
                    String finalAction = action;


//                                if(data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
//                                        || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))){
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

                    appCMSPresenter.getTransactionData(data.getGist().getId(), updatedContentDatum -> {
                        appCMSPresenter.stopLoader();
                        boolean isPlayable = true;
                        long expirationDate = 0;
                        boolean isPurchased = false;
                        AppCMSTransactionDataValue objTransactionData = null;

                        if (updatedContentDatum != null &&
                                updatedContentDatum.size() > 0) {
                            if (updatedContentDatum.get(0).size() == 0) {
                                isPlayable = false;
                            } else {
                                isPurchased = true;
                                objTransactionData = updatedContentDatum.get(0).get(data.getGist().getId());

                                /**
                                 * get the transaction end date and compare with current time if end date is less than current date
                                 * playable will be false
                                 */
                                expirationDate = objTransactionData.getTransactionEndDate();
                            }
                        } else {
                            isPlayable = false;
                        }

                        long timeToExpire = CommonUtils.getTimeIntervalForEvent(expirationDate * 1000, "EEE MMM dd HH:mm:ss");


                        /**
                         * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
                         * content is purchased by user ,if both conditions are false then show subscribe message.
                         */
                        if (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {

//                                        if (!appCMSPresenter.isUserLoggedIn()) {
//                                            appCMSPresenter.showSubscribeMessage();
//                                            return;
//                                        }else{
//                                            launchScreeenPlayer(data, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);
//
//                                            return;
//                                        }
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


                        /**
                         * if end time is expired then show no purchase dialog message
                         */
                        if (expirationDate > 0 && timeToExpire < 0) {
                            isPlayable = false;
                        }
//                                    if(updatedContentDatum==null){
//                                        isPlayable=true;
//                                    }
                        if (!isPlayable) {
                            if (localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                                appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.cannot_purchase_item_msg), appCMSPresenter.getAppCMSMain().getDomainName()));
                            else
                                appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.rental_title)), localisedStrings.getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));
                        } else {

                                        /*
                                        Check if schedule start date is greater then current date than show message and cannot play
                                         */
                            if (data != null &&
                                    data.getGist() != null && (data.getGist().getScheduleStartDate() > 0 || data.getGist().getScheduleEndDate() > 0)) {
                                if (appCMSPresenter.isScheduleVideoPlayable(data.getGist().getScheduleStartDate(), data.getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                                    launchScreeenPlayer(data, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);
                                }
                            } else {
                                String rentalPeriod = "";
                                if (data.getPricing().getRent() != null &&
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
                                            appCMSPresenter.getRentalData(data.getGist().getId(), rentalResponse -> {
                                                launchScreeenPlayer(data, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);
                                            }, false, 0);
                                        } else {
//                                                appCMSPresenter.sendCloseOthersAction(null, true, false);
                                        }
                                    }, msg, "", "", "", true, true);
                                } else {
                                    launchScreeenPlayer(data, finalCurrentPlayingIndex, relatedVideoIds, finalAction, title, permalink);

                                }
                            }
                        }
                    }, "Video");
                } else if (data != null &&
                        data.getGist() != null && (data.getGist().getScheduleStartDate() > 0 || data.getGist().getScheduleEndDate() > 0)) {
                    data.setModuleApi(moduleAPI);
                    appCMSPresenter.launchButtonSelectedAction(permalink,
                            action,
                            title,
                            null,
                            data,
                            false,
                            currentPlayingIndex,
                            relatedVideoIds);
                    /*if (appCMSPresenter.isScheduleVideoPlayable(data.getGist().getScheduleStartDate(), data.getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                        launchScreeenPlayer(data, currentPlayingIndex, relatedVideoIds, action, title, permalink);

                    }*/

                } else if (data.getGist() == null ||
                        data.getGist().getContentType() == null) {
                    if (!appCMSPresenter.launchVideoPlayer(data,
                            data.getGist().getId(),
                            currentPlayingIndex,
                            relatedVideoIds,
                            -1,
                            action)) {
                    }
                } else {
                    data.setModuleApi(moduleAPI);
                    if (!appCMSPresenter.launchButtonSelectedAction(permalink,
                            action,
                            title,
                            null,
                            data,
                            false,
                            currentPlayingIndex,
                            relatedVideoIds)) {
                        //Log.e(TAG, "Could not launch action: " +
                        //                                                " permalink: " +
                        //                                                permalink +
                        //                                                " action: " +
                        //                                                action);
                    }
                }
            }
        }
    }

    void playClick(ContentDatum data) {
        if (isClickable) {
            if (data.getGist() != null) {
                //Log.d(TAG, "Playing item: " + data.getGist().getTitle());
                List<String> relatedVideoIds = allEpisodeIds;
                int currentPlayingIndex = -1;
                if (allEpisodeIds != null) {
                    int currentEpisodeIndex = allEpisodeIds.indexOf(data.getGist().getId());
                    if (currentEpisodeIndex < allEpisodeIds.size()) {
                        currentPlayingIndex = currentEpisodeIndex;
                    }
                }
                if (relatedVideoIds == null) {
                    currentPlayingIndex = 0;
                }
                if (!appCMSPresenter.launchVideoPlayer(data,
                        data.getGist().getId(),
                        currentPlayingIndex,
                        relatedVideoIds,
                        -1,
                        null)) {
                    //Log.e(TAG, "Could not launch play action: " +
                    //                                            " filmId: " +
                    //                                            filmId +
                    //                                            " permaLink: " +
                    //                                            permaLink +
                    //                                            " title: " +
                    //                                            title);
                }
            }
        }
    }
}

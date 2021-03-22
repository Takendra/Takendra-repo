package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.views.adapters.AppCMSSeeAllAdapter;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anas.azeem on 9/7/2017.
 * Owned by ViewLift, NYC
 */

public class AppCMSTVSeeAllAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnInternalEvent {

    private static final String TAG = AppCMSSeeAllAdapter.class.getSimpleName();
    private final Settings settings;
    private List<ContentDatum> adapterData;
    private final AppCMSPresenter appCMSPresenter;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final String viewType;
    private final TVViewCreator tvViewCreator;
    private final Context context;
    private final Module module;
    private final Layout parentLayout;
    private final Component component;
    private AppCMSUIKeyType viewTypeKey;
    protected String defaultAction;
    private TVCollectionGridItemView.OnClickHandler onClickHandler;
    private boolean isClickable;
    private List<OnInternalEvent> receivers;
    int currentSelectedLanguageIndex = 0;
    private boolean isLoadingAdded = false;
    public static final int ITEM = 0;
    public static final int LOADING = 1;
    private boolean retryPageLoad = false;

    public AppCMSTVSeeAllAdapter(Context context,
                                 List<ContentDatum> adapterData,
                                 Component component,
                                 Layout parentLayout,
                                 AppCMSPresenter appCMSPresenter,
                                 Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                 String viewType,
                                 TVViewCreator tvViewCreator,
                                 Module moduleAPI,
                                 Settings settings) {
        this.context = context;
        this.adapterData = adapterData;
        this.component = component;
        this.parentLayout = parentLayout;
        this.appCMSPresenter = appCMSPresenter;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.defaultAction = getDefaultAction(context, component);
        this.viewType = viewType;
        this.tvViewCreator = tvViewCreator;
        this.module = moduleAPI;
        this.viewTypeKey = jsonValueKeyMap.get(viewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.isClickable = true;
        this.receivers = new ArrayList<>();

        if (this.adapterData == null) {
            this.adapterData = new ArrayList<>();
        }
        this.settings = settings;
    }

    private String getDefaultAction(Context context, Component component) {
        if (null != component.getItemClickAction()) {
            return component.getItemClickAction();
        }
        return context.getString(R.string.app_cms_action_videopage_key);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM) {
            TVCollectionGridItemView collectionGridItemView;
            FrameLayout parentLayout = new FrameLayout(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parentLayout.setLayoutParams(params);

            TVViewCreator.ComponentViewResult componentViewResult =
                    tvViewCreator.getComponentViewResult();
            collectionGridItemView = new TVCollectionGridItemView(
                    context,
                    this.parentLayout,
                    false,
                    component,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Utils.getFocusColor(context, appCMSPresenter));

            List<OnInternalEvent> onInternalEvents = new ArrayList<>();

            for (int i = 0; i < component.getComponents().size(); i++) {
                Component childComponent = component.getComponents().get(i);
                if (null != childComponent) {
                    tvViewCreator.createComponentView(context,
                            childComponent,
                            this.parentLayout,
                            module,
                            null,
                            childComponent.getSettings(),
                            jsonValueKeyMap,
                            appCMSPresenter,
                            false,
                            this.viewType,
                            false);

                    if (componentViewResult.onInternalEvent != null) {
                        onInternalEvents.add(componentViewResult.onInternalEvent);
                    }

                    View componentView = componentViewResult.componentView;
                    if (componentView != null) {
                        TVCollectionGridItemView.ItemContainer itemContainer =
                                new TVCollectionGridItemView.ItemContainer.Builder()
                                        .childView(componentView)
                                        .component(childComponent)
                                        .build();
                        collectionGridItemView.addChild(itemContainer);
                        collectionGridItemView.setComponentHasView(i, true);
                        collectionGridItemView.setViewMarginsFromComponent(childComponent,
                                componentView,
                                collectionGridItemView.getLayout(),
                                collectionGridItemView.getChildrenContainer(),
                                jsonValueKeyMap,
                                false,
                                false,
                                this.viewType);
                    } else {
                        collectionGridItemView.setComponentHasView(i, false);
                    }
                }
            }

            viewHolder = new ViewHolder(collectionGridItemView);
        } else {
            View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
            viewHolder = new LoadingVH(viewLoading);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                final ViewHolder viewHolder = (ViewHolder) holder;
                if (0 <= position && adapterData != null && position < adapterData.size()) {
                    bindView(viewHolder.componentView, adapterData.get(position), position, currentSelectedLanguageIndex);
                }
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;
                loadingVH.mErrorLayout.setVisibility(View.GONE);
                loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }

    protected void bindView(TVCollectionGridItemView itemView,
                            final ContentDatum data, int position, int currentSelectedLanguageIndex) throws IllegalArgumentException {
        if (onClickHandler == null) {
            onClickHandler = new TVCollectionGridItemView.OnClickHandler() {
                @Override
                public void click(TVCollectionGridItemView collectionGridItemView,
                                  Component childComponent,
                                  ContentDatum data) {
                    if (isClickable) {
                        //Log.d(TAG, "Clicked on item: " + data.getGist().getTitle());
                        String permalink = data.getGist().getPermalink();
                        boolean typeSeries = "SERIES".equalsIgnoreCase(data.getGist().getContentType())
                                || "SEASON".equalsIgnoreCase(data.getGist().getContentType());
                        String action = typeSeries ? "showDetailPage" : defaultAction;
                        if (permalink == null && typeSeries) {
                            permalink = data.getGist().getSeriesPermalink();
                        }
                        String title = data.getGist().getTitle();
                        String hlsUrl = getHlsUrl(data);
                        String[] extraData = new String[4];
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

                        if (defaultAction.equalsIgnoreCase(context.getString(R.string.app_cms_action_watchvideo_key))) {
                            play(childComponent, data);
                        } else if (!appCMSPresenter.launchTVButtonSelectedAction(permalink,
                                action/*"lectureDetailPage"*/,
                                title,
                                extraData,
                                data,
                                false,
                                -1,
                                null,
                                null)) {
                         /*   Log.e(TAG, "Could not launch action: " + " permalink: " + permalink
                                    + " action: " + action + " hlsUrl: " + hlsUrl);  */
                        }
                    }
                }

                @Override
                public void play(Component childComponent, ContentDatum contentDatum) {
                    if (isClickable) {
                        //Log.d(TAG, "Clicked on item: " + contentDatum.getGist().getTitle());
                        boolean typeSeries = "SERIES".equalsIgnoreCase(contentDatum.getGist().getContentType())
                                || "SEASON".equalsIgnoreCase(contentDatum.getGist().getContentType());
                        if (typeSeries) {
                            click(itemView, childComponent, contentDatum);
                        } else {
                            if ((data.getPricing() != null && data.getPricing().getType() != null && (data.getPricing().getType().equalsIgnoreCase("TVOD") ||
                                    data.getPricing().getType().equalsIgnoreCase("PPV"))) ||
                                    (data.getGist().getPurchaseType() != null && (data.getGist().getPurchaseType().equalsIgnoreCase("RENT")))) {
                                appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                    if (maps != null
                                            && maps.get(0) != null
                                            && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                                        AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());

                                        if (appCMSTransactionDataValue.getTransactionEndDate() != 0) {
                                            ClearDialogFragment clearDialogFragment = Utils.getClearDialogFragment(
                                                    context,
                                                    appCMSPresenter,
                                                    context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                    context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                                    null,
                                                    appCMSPresenter.getLocalisedStrings().getRentTimeDialogMsg(String.valueOf(appCMSTransactionDataValue.getRentalPeriod())),
                                                    appCMSPresenter.getLocalisedStrings().getPlayText(),
                                                    appCMSPresenter.getLocalisedStrings().getCancelCta(),
                                                    13f
                                            );

                                            clearDialogFragment.setOnPositiveButtonClicked(s -> {
                                                appCMSPresenter.launchTVVideoPlayer(
                                                        contentDatum,
                                                        0,
                                                        null,
                                                        contentDatum.getGist().getWatchedTime(),
                                                        null);
                                            });
                                        } else {
                                            appCMSPresenter.launchTVVideoPlayer(
                                                    contentDatum,
                                                    0,
                                                    null,
                                                    contentDatum.getGist().getWatchedTime(),
                                                    null);
                                        }


                                    } else {
                                        Utils.getClearDialogFragment(
                                                context,
                                                appCMSPresenter,
                                                context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                                null,
                                                appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()),
                                                appCMSPresenter.getLocalisedStrings().getBackCta(),
                                                null,
                                                10f
                                        );
                                    }
                                }, contentDatum.getContentType());
                            } else if ((data.getPricing() != null && data.getPricing().getType() != null && (data.getPricing().getType().equalsIgnoreCase("TVOD") ||
                                    data.getPricing().getType().equalsIgnoreCase("PPV"))) ||
                                    (data.getGist().getPurchaseType() != null && (data.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")))) {
                                appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                    if (maps != null
                                            && maps.get(0) != null
                                            && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                                        AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());
                                        if (contentDatum.getGist().getScheduleStartDate() > System.currentTimeMillis()) {
                                            click(itemView, childComponent, contentDatum);
                                        } else {
                                            appCMSPresenter.launchTVVideoPlayer(
                                                    contentDatum,
                                                    0,
                                                    null,
                                                    contentDatum.getGist().getWatchedTime(),
                                                    null);
                                        }
                                    } else {
                                        Utils.getClearDialogFragment(
                                                context,
                                                appCMSPresenter,
                                                context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                context.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                                null,
                                                appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()),
                                                appCMSPresenter.getLocalisedStrings().getBackCta(),
                                                null,
                                                10f
                                        );
                                    }
                                }, contentDatum.getContentType());
                            } else if(jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_MODULE_KEY_SHOWDETAILS_06){
                                List<String> relatedVideoId;
                                int index;
                                contentDatum.setModuleApi(module);
                                relatedVideoId = com.viewlift.Utils.getRelatedVideoId(contentDatum.getModuleApi());
                                String episodeId = com.viewlift.Utils.findEpisodeFromSegment(contentDatum.getModuleApi(),contentDatum.getGist().getId());
                                if(episodeId != null){
                                    index = com.viewlift.Utils.findCurrentIndexOfEpisode(relatedVideoId,episodeId);
                                }else{
                                    index = com.viewlift.Utils.findCurrentIndexOfEpisode(relatedVideoId,contentDatum.getGist().getId());
                                }

                                appCMSPresenter.launchTVVideoPlayer(
                                        contentDatum,
                                        index,
                                        relatedVideoId,
                                        contentDatum.getGist().getWatchedTime(),
                                        null);
                            }else {
                                if (!appCMSPresenter.isScheduleVideoPlayable(data.getGist().getScheduleStartDate(), data.getGist().getScheduleEndDate(), null)) {
                                    return;
                                }
                                appCMSPresenter.launchTVVideoPlayer(
                                        contentDatum,
                                        0,
                                        null,
                                        contentDatum.getGist().getWatchedTime(),
                                        null);
                            }
                        }
                    }
                }

                @Override
                public void delete(Component childComponent, ContentDatum data) {

                }

                @Override
                public void changeLanguage(Component childComponent, ContentDatum data) {
                }

                @Override
                public void notifyData() {
                    notifyDataSetChanged();
                }
            };
        }

        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    viewTypeKey,
                    position,
                    module,
                    currentSelectedLanguageIndex, settings);
        }
    }

    @Override
    public int getItemCount() {
        return adapterData != null && adapterData.size() > 0 ? adapterData.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return /*(position == adapterData.size() - 1 && isLoadingAdded) ? LOADING : */ITEM;
    }

    @Override
    public void addReceiver(OnInternalEvent e) {
        receivers.add(e);
    }

    @Override
    public void sendEvent(InternalEvent<?> event) {
        for (OnInternalEvent internalEvent : receivers) {
            internalEvent.receiveEvent(event);
        }
    }

    @Override
    public void receiveEvent(InternalEvent<?> event) {
        adapterData.clear();
        notifyDataSetChanged();
    }

    @Override
    public void cancel(boolean cancel) {

    }

    @Override
    public void setModuleId(String moduleId) {

    }

    @Override
    public String getModuleId() {
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TVCollectionGridItemView componentView;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof TVCollectionGridItemView) {
                this.componentView = (TVCollectionGridItemView) itemView;
            }
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    //showRetry(false, null);
                    //mCallback.retryPageLoad();


                    break;
            }
        }
    }


    public void addAll(List objectList) {
        adapterData.addAll(objectList);
        notifyItemInserted(adapterData.size() - 1);
        setClickable(true);
    }

    public void add(Object object) {
        ContentDatum contentDatum = new ContentDatum();
        adapterData.add(contentDatum);
        notifyItemInserted(adapterData.size() - 1);
    }

    public void remove(Object object) {
        int position = adapterData.indexOf(object);
        if (position > -1) {
            adapterData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        adapterData.clear();
    }

    public void addLoadingFooter() {
        /*isLoadingAdded = true;
        ContentDatum contentDatum = new ContentDatum();
        add(contentDatum);*/
    }

    public void removeLoadingFooter() {
        /*isLoadingAdded = false;

        int position = adapterData.size() - 1;
        {
            adapterData.remove(position);
            notifyItemRemoved(position);
        }*/
    }
}

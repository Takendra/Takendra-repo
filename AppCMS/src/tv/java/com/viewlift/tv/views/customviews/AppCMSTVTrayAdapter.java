package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.ContentDetails;
import com.viewlift.models.data.appcms.api.Language;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.utils.LocaleUtils;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by anas.azeem on 9/7/2017.
 * Owned by ViewLift, NYC
 */

public class AppCMSTVTrayAdapter
        extends RecyclerView.Adapter<AppCMSTVTrayAdapter.ViewHolder>
        implements OnInternalEvent {

    private static final String TAG = AppCMSTVTrayAdapter.class.getCanonicalName();
    private static final int ITEM_TYPE_DATA = 10001;
    private static final int ITEM_TYPE_NO_DATA = 10002;
    private final Settings settings;
    private boolean isWatchlist;
    private boolean isHistory;
    private boolean isLibrary;
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


    public AppCMSTVTrayAdapter(Context context,
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
        if(jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_MODULE_KEY_SHOWDETAILS_06){
            this.adapterData = convertSeasonToContentDatum(adapterData);
        }else{
            this.adapterData = adapterData;
        }

        if (this.adapterData == null) {
            this.adapterData = new ArrayList<>();
        }

        if (null != jsonValueKeyMap.get(viewType)) {
            switch (jsonValueKeyMap.get(viewType)) {
                case PAGE_HISTORY_01_MODULE_KEY:
                case PAGE_HISTORY_02_MODULE_KEY:
                    this.isHistory = true;
                    break;

                case PAGE_WATCHLIST_01_MODULE_KEY:
                case PAGE_WATCHLIST_02_MODULE_KEY:
                case PAGE_WATCHLIST_03_MODULE_KEY:
                    this.isWatchlist = true;
                    break;
                case PAGE_LIBRARY_01_MODULE_KEY:
                    this.isLibrary = true;
                    break;
                default:
                    break;
            }
        }
        this.settings = settings;
        if(viewType != null && viewType.equalsIgnoreCase(context.getResources().getString(R.string.languageSetting_module))){
            ArrayList<Language> languageArrayList = (ArrayList<Language>)appCMSPresenter.getLanguageArrayList();
            currentSelectedLanguageIndex = languageArrayList.indexOf(appCMSPresenter.getLanguage());
        }

        sortData();
    }

    private RecyclerView recyclerView;
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        try {
            if (viewType != null && viewType.equalsIgnoreCase(context.getResources().getString(R.string.languageSetting_module))) {
                recyclerView.scrollToPosition(currentSelectedLanguageIndex);   }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentData(List<ContentDatum> adapterData) {
        if(jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_MODULE_KEY_SHOWDETAILS_06){
            getRelatedVideoId(adapterData);
        }
        this.adapterData = adapterData;
        sortData();
        notifyDataSetChanged();
    }

    private String getDefaultAction(Context context, Component component) {
        if (null != component.getItemClickAction()) {
            return component.getItemClickAction();
        }
        return context.getString(R.string.app_cms_action_videopage_key);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_DATA) {
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
//                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Utils.getPrimaryHoverBorderColor(context, appCMSPresenter));

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

            if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_SEGMENT_TABLE_VIEW_KEY){
                if(context instanceof AppCmsHomeActivity && ((AppCmsHomeActivity) context).findViewById(R.id.segment_tray_title) != null) {
                    ((AppCmsHomeActivity) context).findViewById(R.id.segment_tray_title).setVisibility(View.GONE);
                }
            }

            return new ViewHolder(collectionGridItemView);
        } else {

            RelativeLayout relativeLayout = new RelativeLayout(context);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            relativeLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(context);
            String emptyListText = "";
            if (this.viewType.equalsIgnoreCase(context.getString(R.string.app_cms_page_watchlist_module_key)) ||
                this.viewType.equalsIgnoreCase(context.getString(R.string.app_cms_page_watchlist_module_key3))) {
                if(appCMSPresenter.isUserLoggedIn()){
                    emptyListText = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.add_to_watchlist_text_for_guest_user));
                }else{
                    emptyListText = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.no_data_in_watchlist_text));
                }

                if (module.getMetadataMap() != null
                        && module.getMetadataMap().getkStrWatchlistEmpty() != null) {
                    emptyListText = module.getMetadataMap().getkStrWatchlistEmpty();
                }
            } else if (this.viewType.equalsIgnoreCase(context.getString(R.string.app_cms_page_history_module_key))
                    ||this.viewType.equalsIgnoreCase(context.getString(R.string.app_cms_page_history_module_key2))
                    ||this.viewType.equalsIgnoreCase(context.getString(R.string.app_cms_page_history_module_key4))) {
                emptyListText = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.no_data_in_history_text));
                if (module.getMetadataMap() != null
                        && module.getMetadataMap().getkStrHistoryEmpty() != null) {
                    emptyListText = module.getMetadataMap().getkStrHistoryEmpty();
                }
            } else if (this.viewType.equalsIgnoreCase(context.getString(R.string.app_cms_page_mylibrary_module_key))) {
                emptyListText = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.no_data_in_library_text));
            }
            textView.setText(emptyListText);

            textView.setGravity(Gravity.CENTER);
            textView.setTypeface(Utils.getSpecificTypeface(context,
                    appCMSPresenter,
                    context.getString(R.string.app_cms_page_font_semibold_key)));
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            relativeLayout.addView(textView);

            if(null != appCMSPresenter
                    && null != appCMSPresenter.getCurrentActivity()
                    && appCMSPresenter.getCurrentActivity() instanceof AppCmsHomeActivity){
                ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowSubLeftNavigation(true);
            }

            return new ViewHolder(relativeLayout);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (0 <= position && adapterData != null && position < adapterData.size()) {
            bindView(holder.componentView,
                    adapterData.get(position),
                    position,
                    module,
                    currentSelectedLanguageIndex);
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
                            final ContentDatum data,
                            int position,
                            Module module,
                            int currentSelectedLanguageIndex) throws IllegalArgumentException {
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
                            if ((contentDatum.getPricing() != null && contentDatum.getPricing().getType() != null && (contentDatum.getPricing().getType().equalsIgnoreCase("TVOD") ||
                                    contentDatum.getPricing().getType().equalsIgnoreCase("PPV"))) ||
                                    (contentDatum.getGist().getPurchaseType() != null && (contentDatum.getGist().getPurchaseType().equalsIgnoreCase("RENT")))) {
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
                                        appCMSPresenter.showLoadingDialog(false);
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
                            } else if ((contentDatum.getPricing() != null && contentDatum.getPricing().getType() != null &&
                                    (contentDatum.getPricing().getType().equalsIgnoreCase("TVOD") || contentDatum.getPricing().getType().equalsIgnoreCase("PPV"))) ||
                                    (contentDatum.getGist().getPurchaseType() != null && (contentDatum.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")))) {
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
                                if (!appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(), contentDatum.getGist().getScheduleEndDate(), null)) {
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
                    //Log.d(TAG, "Deleting watchlist item: " + data.getGist().getTitle());
                    if (appCMSPresenter.isNetworkConnected()) {
                        appCMSPresenter.editWatchlist( data,
                                addToWatchlistResult -> {
                                    adapterData.remove(data);
                                    View view = null;
                                    try {
                                        view = ((View) itemView.getParent().getParent()).findViewById(R.id.appcms_removeall);
                                    } catch (Exception e) {
                                        if (context instanceof AppCmsHomeActivity) {
                                            view = ((AppCmsHomeActivity) context).findViewById(R.id.appcms_removeall);
                                        }
                                    }
                                    if (view != null) {
                                        view.setFocusable(adapterData.size() != 0);
                                        view.setVisibility(adapterData.size() != 0 ? View.VISIBLE : View.INVISIBLE);
                                    }
                                    notifyDataSetChanged();
                                }, false, false, module);
                    } else {
                        appCMSPresenter.openErrorDialog(data,
                                true,
                                appCMSAddToWatchlistResult -> {
                                    adapterData.remove(data);
                                    View view = null;
                                    try {
                                        view = ((View) itemView.getParent().getParent()).findViewById(R.id.appcms_removeall);
                                    } catch (Exception e) {
                                        if (context instanceof AppCmsHomeActivity) {
                                            view = ((AppCmsHomeActivity) context).findViewById(R.id.appcms_removeall);
                                        }
                                    }
                                    if (view != null) {
                                        view.setFocusable(adapterData.size() != 0);
                                        view.setVisibility(adapterData.size() != 0 ? View.VISIBLE : View.INVISIBLE);
                                    }
                                    notifyDataSetChanged();
                                });
                    }
                }

                @Override
                public void changeLanguage(Component childComponent, ContentDatum data) {
                    Language language = new Language();
                    language.setLanguageCode(data.getGist().getDataId());
                    language.setLanguageName(data.getGist().getTitle());
                    LocaleUtils.setLocale(appCMSPresenter.getCurrentContext(),language.getLanguageCode());
                    //LanguageHelper.setLocale(appCMSPresenter.getCurrentContext(),language.getLanguageCode());
                    appCMSPresenter.clearPageAPIAndUI();
                    appCMSPresenter.setLanguage(language);
                    appCMSPresenter.navigateToHomePage(true);
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
        return adapterData != null && adapterData.size() > 0 ? ITEM_TYPE_DATA : ITEM_TYPE_NO_DATA;
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

    private void sortData() {
        if (adapterData != null) {
            if (isWatchlist) {
                /*Collections.sort(adapterData, (o1, o2)
                        -> Long.compare(o2.getAddedDate(), o1.getAddedDate()));*/
            } else if (isHistory) {
                Collections.sort(adapterData, (o1, o2)
                        -> Long.compare((long) o1.getUpdateDate(), (long) o2.getUpdateDate()));
                //Collections.reverse(adapterData);
            }
        }
    }

    public List<ContentDatum> convertSeasonToContentDatum(List<ContentDatum> contentDatumList) {
        List<ContentDatum> seasonContentList = new ArrayList<>();
        if (contentDatumList != null && contentDatumList.get(0).getSeason() != null && contentDatumList.get(0).getSeason().size() > 0) {
            List<Season_> seasonList = contentDatumList.get(0).getSeason();
            /*try {
                Collections.sort(seasonList,
                        (o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
            }catch (Exception ex){
                ex.printStackTrace();
            }*/

            if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_SEASON_TABLE_VIEW_KEY) {
                for (int i = 0; i < seasonList.size(); i++) {
                    if (!seasonList.get(i).isPromo()) {
                        if(seasonList.get(i).getEpisodes() != null && seasonList.get(i).getEpisodes().size() > 0) {
                            removePromoData(seasonList.get(i).getEpisodes());
                            if (seasonList.get(i).getEpisodes().size() > 0) {
                                ContentDatum contentDatum = new ContentDatum();
                                contentDatum.setTitle(seasonList.get(i).getTitle());
                                contentDatum.setSeason(seasonList);
                                seasonContentList.add(contentDatum);

                            }
                        }
                    }
                }
            } else if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_EPISODE_TABLE_VIEW_KEY &&
                    seasonList.get(0).getEpisodes() != null && seasonList.get(0).getEpisodes().size() > 0) {
                if (!seasonList.get(0).isPromo()) {
                    List<ContentDatum> episodeContentDatumList = seasonList.get(0).getEpisodes();
                    removePromoData(episodeContentDatumList);
                    seasonContentList.addAll(episodeContentDatumList);
                    getRelatedVideoId(seasonContentList);
                }
            } else if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_SEGMENT_TABLE_VIEW_KEY && seasonList.get(0).getEpisodes() != null &&
                    seasonList.get(0).getEpisodes().size() > 0 && seasonList.get(0).getEpisodes().get(0).getRelatedVideos() != null &&
                    seasonList.get(0).getEpisodes().get(0).getRelatedVideos().size() > 0) {
                if (!seasonList.get(0).isPromo()) {
                    List<ContentDatum> segmentContentDatumList = seasonList.get(0).getEpisodes().get(0).getRelatedVideos();
                    removePromoData(segmentContentDatumList);
                    seasonContentList.addAll(segmentContentDatumList);
                    getRelatedVideoId(seasonContentList);
                }
            }
        }
        return seasonContentList;
    }

    private void getRelatedVideoId(List<ContentDatum> contentDatumList){
        if(contentDatumList != null) {
            int size = contentDatumList.size();
            for (int i = 0; i < size; i++) {
                List<String> relatedVideoID = new ArrayList<>();
                for (int relatedVideoIndex = i; relatedVideoIndex < size; relatedVideoIndex++) {
                    if (contentDatumList.get(relatedVideoIndex).getGist() != null && contentDatumList.get(relatedVideoIndex).getGist().getId() != null) {
                        relatedVideoID.add(contentDatumList.get(relatedVideoIndex).getGist().getId());
                    }
                }
                ContentDetails contentDetails = new ContentDetails();
                contentDetails.setRelatedVideoIds(relatedVideoID);
                contentDatumList.get(i).setContentDetails(contentDetails);
            }
        }
    }

    private void removePromoData(List<ContentDatum> contentData){
        if(contentData != null && contentData.size() > 0) {
            for (int i = 0; i < contentData.size(); i++) {
                if ((contentData.get(i).getTags() != null &&
                        contentData.get(i).getTags().size() > 0 &&
                        contentData.get(i).getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                    contentData.remove(i);
                }
            }
        }
    }

}

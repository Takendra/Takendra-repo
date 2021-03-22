package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.audio.playback.AudioPlaylistHelper;
import com.viewlift.audio.playback.PlaybackManager;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.StreamingInfo;
import com.viewlift.models.data.appcms.audio.AppCMSAudioDetailResult;
import com.viewlift.models.data.appcms.audio.AudioAssets;
import com.viewlift.models.data.appcms.audio.Mp3;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.downloads.DownloadVideoRealm;
import com.viewlift.models.data.appcms.history.AppCMSHistoryResult;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.offlinedrm.FetchOffineDownloadStatus;
import com.viewlift.offlinedrm.OfflineDownloadService;
import com.viewlift.presenters.AppCMSActionType;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPlayAudioActivity;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.viewlift.audio.ui.PlaybackControlsFragment.EXTRA_CURRENT_MEDIA_DESCRIPTION;
import static com.viewlift.models.data.appcms.downloads.DownloadStatus.STATUS_RUNNING;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSUserWatHisDowAdapter extends RecyclerView.Adapter<AppCMSUserWatHisDowAdapter.ViewHolder>
        implements AppCMSBaseAdapter, OnInternalEvent {
    private static final String TAG = "UserWatHisDowAdapter";


    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
    private ConstraintCollectionGridItemView.OnClickHandler onClickHandlerConstraint;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private boolean isClickable;
    private String videoAction;
    private String trayAction;
    private String deleteSingleItemHistoryAction;
    private String deleteSingleItemWatchlistAction;
    private String deleteSingleItemDownloadAction;
    boolean emptyList = false;

    private List<OnInternalEvent> receivers;
    private InternalEvent<Integer> hideRemoveAllButtonEvent;
    private InternalEvent<Integer> showRemoveAllButtonEvent;
    CollectionGridItemView view = null;
    private String moduleId;
    RecyclerView mRecyclerView;
    private boolean isHistoryPage;
    private boolean isDownloadPage;
    private boolean isWatchlistPage;
    private boolean isLibraryPage;
    private boolean isFollowPage;
    private Map<String, Boolean> filmDownloadIconUpdatedMap;
    String blockName;
    AppCMSUIKeyType uiBlockName;
    boolean constrainteView;
    private ConstraintViewCreator constraintViewCreator;

    public AppCMSUserWatHisDowAdapter(Context context,
                                      ViewCreator viewCreator,
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
                                      boolean constrainteView,
                                      ConstraintViewCreator constraintViewCreator) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.constraintViewCreator = constraintViewCreator;
        this.constrainteView = constrainteView;
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.blockName = blockName;
        this.moduleAPI = moduleAPI;
        this.receivers = new ArrayList<>();
        this.hideRemoveAllButtonEvent = new InternalEvent<>(View.GONE);
        this.showRemoveAllButtonEvent = new InternalEvent<>(View.VISIBLE);

        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            this.adapterData = moduleAPI.getContentData();
        } else {
            this.adapterData = new ArrayList<>();
        }
        if (this.adapterData.size() == 0) {
            emptyList = true;
        }

        this.componentViewType = viewType;
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        this.uiBlockName = jsonValueKeyMap.get(blockName);
        if (this.uiBlockName == null) {
            this.uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.videoAction = getVideoAction(context);
        this.trayAction = getTrayAction(context);
        this.deleteSingleItemHistoryAction = getDeleteSingleItemHistoryAction(context);
        this.deleteSingleItemWatchlistAction = getDeleteSingleItemWatchlistAction(context);
        this.deleteSingleItemDownloadAction = getDeleteSingleItemDownloadAction(context);
        this.isClickable = true;
        this.setHasStableIds(false);
        this.appCMSAndroidModules = appCMSAndroidModules;
        detectViewTypes(jsonValueKeyMap, viewType);
        sortData();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    private void detectViewTypes(Map<String, AppCMSUIKeyType> jsonValueKeyMap, String viewType) {

        switch (uiBlockName) {
            case UI_BLOCK_DOWNLOADS_03:
                this.filmDownloadIconUpdatedMap = new HashMap<>();
                this.isDownloadPage = true;
                break;
            case UI_BLOCK_WATCHLIST_01:
            case UI_BLOCK_WATCHLIST_02:
            case UI_BLOCK_WATCHLIST_03:
            case UI_BLOCK_WATCHLIST_04:
                this.isWatchlistPage = true;
                break;

        }
        if (jsonValueKeyMap.get(viewType) != null) {
            switch (jsonValueKeyMap.get(viewType)) {
                case PAGE_HISTORY_01_MODULE_KEY:
                case PAGE_HISTORY_02_MODULE_KEY:
                    this.isHistoryPage = true;
                    break;

                case PAGE_DOWNLOAD_01_MODULE_KEY:
                case PAGE_DOWNLOAD_02_MODULE_KEY:
                    this.filmDownloadIconUpdatedMap = new HashMap<>();
                    this.isDownloadPage = true;
                    break;

                case PAGE_WATCHLIST_01_MODULE_KEY:
                case PAGE_WATCHLIST_02_MODULE_KEY:
                    this.isWatchlistPage = true;
                    break;

                case PAGE_LIBRARY_01_MODULE_KEY:
                    this.isLibraryPage = true;
                    break;

                case PAGE_FOLLOW_01_MODULE_KEY:
                    isFollowPage = true;

                default:
                    break;
            }
        }
    }

    private void sortData() {
        if (adapterData != null) {
            if (isWatchlistPage || isDownloadPage || isFollowPage) {
                sortByAddedDate();
                if (isWatchlistPage) {
                    Collections.reverse(adapterData);
                }
            } else if (isHistoryPage) {
                sortByUpdateDate();
            }
        }
    }

    private void sortByAddedDate() {
        try {
            Collections.sort(adapterData, (o1, o2) -> Long.compare((Long) o1.getAddedDate(),
                    (Long) o2.getAddedDate()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void sortByUpdateDate() {
        try {
            Collections.sort(adapterData, (o1, o2) -> Long.compare((Long) o1.getUpdateDate(),
                    (Long) o2.getUpdateDate()));
            if (adapterData != null && adapterData.size() >= 2) {
                if (Long.compare((Long) adapterData.get(0).getUpdateDate(), (Long) adapterData.get(1).getUpdateDate()) == 1) {
                    Collections.reverse(adapterData);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!constrainteView) {


            CollectionGridItemView view = viewCreator.createCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    useParentSize,
                    component,
                    appCMSPresenter,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    useMarginsAsPercentages,
                    true,
                    this.componentViewType,
                    false,
                    false, viewTypeKey, blockName);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 5, 5, 5);
            view.setLayoutParams(lp);

            if (emptyList) {
                TextView emptyView = new TextView(mContext);
                emptyView.setTextColor(appCMSPresenter.getGeneralTextColor());
                emptyView.setTextSize(24f);
                emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                emptyView.setGravity(Gravity.CENTER);
                if (isHistoryPage) {
                    if (AppCMSHistoryResult.failure) {
                        emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.response_failure_message)));
                    } else {
                        String emptyText = appCMSPresenter.getCurrentActivity().getString(R.string.empty_history_list_message);
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrHistoryEmpty() != null)
                            emptyText = moduleAPI.getMetadataMap().getkStrHistoryEmpty();
                        emptyView.setText(emptyText);
                    }
                    return new ViewHolder(emptyView);
                }
                if (isWatchlistPage) {
                    String emptyText = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.empty_watchlist_message), appCMSPresenter.getWatchlistPage() != null ? appCMSPresenter.getWatchlistPage().getPageName() : "Page!");
                    if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrWatchlistEmpty() != null)
                        emptyText = moduleAPI.getMetadataMap().getkStrWatchlistEmpty();
                    emptyView.setText(emptyText);
                    return new ViewHolder(emptyView);
                }
                if (isLibraryPage) {
                    emptyView.setText(localisedStrings.getNotPurchasedText());
                    return new ViewHolder(emptyView);
                }
                if (isDownloadPage) {
                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_03) {
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id) != null) {
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id).setVisibility(View.VISIBLE);
                        }
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept) != null) {
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept).setVisibility(View.VISIBLE);
                        }
                        return new ViewHolder(emptyView);
                    } else {
                        String emptyText = appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.empty_download_video_message));
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getNoDownload() != null)
                            emptyText = moduleAPI.getMetadataMap().getNoDownload();
                        emptyView.setText(emptyText);
                        return new ViewHolder(emptyView);
                    }
                }

                if (isFollowPage) {
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id) != null) {
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id).setVisibility(View.VISIBLE);
                    }
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept) != null) {
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept).setVisibility(View.VISIBLE);
                    }
                    return new ViewHolder(emptyView);
                }
            }
            return new ViewHolder(view);
        } else {

            MetadataMap metadataMap = null;
            if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
                metadataMap = moduleAPI.getMetadataMap();
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
                    viewTypeKey, blockName, metadataMap);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 5, 5, 5);
            view.setLayoutParams(lp);

            if (emptyList) {
                TextView emptyView = new TextView(mContext);
                emptyView.setTextColor(appCMSPresenter.getGeneralTextColor());
                emptyView.setTextSize(24f);
                if (isHistoryPage) {
                    if (AppCMSHistoryResult.failure) {
                        emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.response_failure_message)));
                    } else {
                        String emptyText = appCMSPresenter.getCurrentActivity().getString(R.string.empty_history_list_message);
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrHistoryEmpty() != null)
                            emptyText = moduleAPI.getMetadataMap().getkStrHistoryEmpty();
                        emptyView.setText(emptyText);
                    }
                    return new ViewHolder(emptyView);
                }
                if (isWatchlistPage) {
                    String emptyText = appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.empty_watchlist_message), appCMSPresenter.getWatchlistPage() != null ? appCMSPresenter.getWatchlistPage().getPageName() : "Page!");
                    if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrWatchlistEmpty() != null)
                        emptyText = moduleAPI.getMetadataMap().getkStrWatchlistEmpty();
                    emptyView.setText(emptyText);
                    return new ViewHolder(emptyView);
                }
                if (isLibraryPage) {
                    emptyView.setText(localisedStrings.getNotPurchasedText());
                    return new ViewHolder(emptyView);
                }
                if (isDownloadPage) {
                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_DOWNLOADS_03) {
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id) != null) {
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id).setVisibility(View.VISIBLE);
                        }
                        if (appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept) != null) {
                            appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept).setVisibility(View.VISIBLE);
                        }
                        return new ViewHolder(emptyView);
                    } else {
                        String emptyText = appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.empty_download_video_message));
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getNoDownload() != null)
                            emptyText = moduleAPI.getMetadataMap().getNoDownload();
                        emptyView.setText(emptyText);
                        return new ViewHolder(emptyView);
                    }
                }

                if (isFollowPage) {
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id) != null) {
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.follow_Empty_Label_Id).setVisibility(View.VISIBLE);
                    }
                    if (appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept) != null) {
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.browseConcept).setVisibility(View.VISIBLE);
                    }
                    return new ViewHolder(emptyView);
                }
            }
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (adapterData != null && adapterData.size() == 0) {
            sendEvent(hideRemoveAllButtonEvent);
        }
        if (!emptyList) {
            if (0 <= position && position < adapterData.size() && holder.componentView != null) {

                for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                    if (holder.componentView.getChild(i) instanceof TextView) {
                        ((TextView) holder.componentView.getChild(i)).setText("");
                    }
                }
                bindView(holder.componentView, adapterData.get(position), position);

                if (isDownloadPage) {
                    downloadView(adapterData.get(position), holder.componentView, position);
                }
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

    private void downloadView(ContentDatum contentDatum, CollectionGridItemView componentView, int position) {
        String userId = appPreference.getLoggedInUser();
        TextView videoSize = null;
        ImageButton deleteDownloadButton = null;
        ImageView thumbnailImage = null;

        for (int i = 0; i < componentView.getChildItems().size(); i++) {
            CollectionGridItemView.ItemContainer itemContainer = componentView.getChildItems().get(i);
            if (itemContainer.getComponent().getKey() != null) {
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_download_size_key))) {
                    videoSize = (TextView) itemContainer.getChildView();
                }
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_delete_download_key))) {
                    deleteDownloadButton = (ImageButton) itemContainer.getChildView();
                }
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_thumbnail_image_key))) {
                    thumbnailImage = (ImageView) itemContainer.getChildView();
                }
            }
        }

        if (videoSize != null && deleteDownloadButton != null && thumbnailImage != null && contentDatum.isDRMEnabled()) {
            Download download =  appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().getDowloadedVideoObject(contentDatum.getGist().getId());
     if(download!=null) {
    String sizeValue = appCMSPresenter.getOfflineDownloadedFileSize(download);
    videoSize.setText(localisedStrings.getCancelText().toUpperCase());
    videoSize.setOnClickListener(v -> deleteDownloadVideo(contentDatum, position));
    View.OnClickListener deleteClickListener = v -> deleteDownloadVideo(contentDatum, position);
    if (download.state == Download.STATE_COMPLETED) {
        deleteDownloadButton.setImageBitmap(null);
        deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_deleteicon));
        deleteDownloadButton.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
        deleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
        deleteDownloadButton.invalidate();
//                if(appCMSPresenter.getAppPreference()!=null && contentDatum.getGist().getId()!=null)
//                    sizeValue = appCMSPresenter.getDownloadedFileSize(appCMSPresenter.getAppPreference().getofflineExoSize(contentDatum.getGist().getId()));
       if(sizeValue!=null)
        videoSize.setText(sizeValue);
    } else if (download.state == Download.STATE_QUEUED) {
        deleteDownloadButton.setImageBitmap(null);
        deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_downloaded_40x));
        deleteDownloadButton.invalidate();
        int radiusDifference = 5;
        if (BaseView.isTablet(componentView.getContext())) {
            radiusDifference = 3;
        }
        appCMSPresenter.getUserOfflineVideoDownloadStatus(contentDatum.getGist().getId(), new FetchOffineDownloadStatus(deleteDownloadButton, appCMSPresenter,
                contentDatum, userId, radiusDifference, contentDatum.getGist().getId(), true, videoSize, deleteClickListener), userId);
    } else {
        int radiusDifference = 5;
        if (BaseView.isTablet(componentView.getContext())) {
            radiusDifference = 3;
        }
        deleteDownloadButton.getBackground().setTint(ContextCompat.getColor(mContext, R.color.transparentColor));
        deleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
        appCMSPresenter.getUserOfflineVideoDownloadStatus(contentDatum.getGist().getId(), new FetchOffineDownloadStatus(deleteDownloadButton, appCMSPresenter,
                contentDatum, userId, radiusDifference, contentDatum.getGist().getId(), true, videoSize, deleteClickListener), userId);
    }
}
        }else {
            if (videoSize != null && deleteDownloadButton != null && thumbnailImage != null) {
                videoSize.setVisibility(View.VISIBLE);
                videoSize.setText(appCMSPresenter.getDownloadedFileSize(contentDatum.getGist().getId()));

                int radiusDifference = 5;
                if (BaseView.isTablet(componentView.getContext())) {
                    radiusDifference = 3;
                }
                if (contentDatum.getGist() != null) {
                    final ImageButton deleteButton = deleteDownloadButton;
                    final TextView videoSizeText = videoSize;
                    /**
                     * if content already downloaded then just update the status , no need to call update status for running progress
                     */
                    if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId())) {
                        deleteDownloadButton.setImageBitmap(null);
                        deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                R.drawable.ic_deleteicon));
                        deleteDownloadButton.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                        deleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        contentDatum.getGist().setDownloadStatus(DownloadStatus.STATUS_COMPLETED);
                        deleteDownloadButton.invalidate();

                        if (contentDatum.getGist() != null && contentDatum.getGist().getMediaType() != null && contentDatum.getGist().getPosterImageUrl() != null && contentDatum.getGist().getMediaType().equalsIgnoreCase(mContext.getResources().getString(R.string.media_type_audio))) {
                            loadImage(mContext, contentDatum.getGist().getPosterImageUrl(), thumbnailImage);

                        } else if (contentDatum.getGist() != null && contentDatum.getGist().getVideoImageUrl() != null) {
                            loadImage(mContext, contentDatum.getGist().getVideoImageUrl(), thumbnailImage);
                        }
                        deleteDownloadButton.postInvalidate();
                    } else {
                        videoSize.setText(localisedStrings.getCancelText().toUpperCase());

                        appCMSPresenter.getUserVideoDownloadStatus(contentDatum.getGist().getId(),
                                videoDownloadStatus -> {
                                    if (videoDownloadStatus != null) {
                                        if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PAUSED ||
                                                videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING ||
                                                (!appCMSPresenter.isNetworkConnected() &&
                                                        videoDownloadStatus.getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                                                        videoDownloadStatus.getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL)) {
                                            deleteButton.setImageBitmap(null);
                                            deleteButton.setBackground(ContextCompat.getDrawable(componentView.getContext(),
                                                    R.drawable.ic_download_queued));
                                            videoSizeText.setText(localisedStrings.getCancelText().toUpperCase());
                                        }
                                        if (videoDownloadStatus != null && videoDownloadStatus.getDownloadStatus() != null) {
                                            contentDatum.getGist().setDownloadStatus(videoDownloadStatus.getDownloadStatus());
                                        }
                                    }
                                },
                                appPreference.getLoggedInUser());
                        switch (contentDatum.getGist().getDownloadStatus()) {
                            case STATUS_PENDING:
                                deleteDownloadButton.setImageBitmap(null);
                                deleteDownloadButton.setBackground(ContextCompat.getDrawable(componentView.getContext(),
                                        R.drawable.ic_download_queued));
                                videoSizeText.setText(localisedStrings.getCancelText().toUpperCase());
                                videoSize.setOnClickListener(v -> deleteDownloadVideo(contentDatum, position));
                                break;
                            case STATUS_RUNNING:
                                int finalRadiusDifference = radiusDifference;
                                if (contentDatum.getGist() != null && deleteDownloadButton != null) {
                                    if (deleteDownloadButton.getBackground() != null) {
                                        deleteDownloadButton.getBackground().setTint(ContextCompat.getColor(mContext, R.color.transparentColor));
                                        deleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                                    }

                                    ImageButton finalDeleteDownloadButton = deleteDownloadButton;
                                    ImageView finalThumbnailImage = thumbnailImage;
                                    TextView finalVideoSize = videoSize;

                                    Log.e(TAG, "Film downloading: " + contentDatum.getGist().getId());
                                    Boolean filmDownloadUpdated = filmDownloadIconUpdatedMap.get(contentDatum.getGist().getId());
                                    if (filmDownloadUpdated == null || !filmDownloadUpdated) {
                                        filmDownloadIconUpdatedMap.put(contentDatum.getGist().getId(), true);

                                        appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                                                deleteDownloadButton,
                                                appCMSPresenter,
                                                userVideoDownloadStatus -> {
                                                    if (userVideoDownloadStatus != null) {
                                                        if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_SUCCESSFUL) {
                                                            finalDeleteDownloadButton.setImageBitmap(null);
                                                            finalDeleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                                                    R.drawable.ic_deleteicon));
                                                            finalDeleteDownloadButton.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                                                            finalDeleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                                                            finalDeleteDownloadButton.invalidate();
                                                            finalDeleteDownloadButton.bringToFront();
                                                            if (contentDatum.getGist().getMediaType() != null && contentDatum.getGist().getMediaType().equalsIgnoreCase(mContext.getResources().getString(R.string.media_type_audio))) {
                                                                contentDatum.getGist().setPosterImageUrl(userVideoDownloadStatus.getPosterUri());
                                                                loadImage(mContext, userVideoDownloadStatus.getPosterUri(), finalThumbnailImage);
                                                            } else {
                                                                loadImage(mContext, userVideoDownloadStatus.getThumbUri(), finalThumbnailImage);
                                                            }
                                                            try {
                                                                finalVideoSize.setText(appCMSPresenter.getDownloadedFileSize(userVideoDownloadStatus.getVideoSize()));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                finalVideoSize.setVisibility(View.GONE);
                                                            }
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
                                                            notifyItemChanged(position);
                                                        } else if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_INTERRUPTED) {
                                                            finalDeleteDownloadButton.setImageBitmap(null);
                                                            finalDeleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                                                    android.R.drawable.stat_sys_warning));
                                                            finalVideoSize.setText(localisedStrings.getRetryText());
                                                            finalDeleteDownloadButton.setOnClickListener(v -> restartDownloadVideo(contentDatum, position, finalDeleteDownloadButton, finalRadiusDifference));
                                                            finalVideoSize.setOnClickListener(v -> restartDownloadVideo(contentDatum, position, finalDeleteDownloadButton, finalRadiusDifference));
                                                        } else if (userVideoDownloadStatus.getDownloadStatus() == STATUS_RUNNING) {
                                                            finalVideoSize.setText(localisedStrings.getCancelText().toUpperCase());
                                                        } else if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING) {
                                                            finalDeleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                                                    R.drawable.ic_download_queued));
                                                            finalVideoSize.setText(localisedStrings.getCancelText().toUpperCase());
                                                        }
                                                        finalDeleteDownloadButton.bringToFront();
                                                        contentDatum.getGist().setDownloadStatus(userVideoDownloadStatus.getDownloadStatus());
                                                        notifyItemChanged(position);
                                                    }
                                                },
                                                userId, true, radiusDifference, appPreference.getDownloadPageId());
                                    } else {
                                        appCMSPresenter.updateDownloadTimerTask(contentDatum.getGist().getId(),
                                                appPreference.getDownloadPageId(),
                                                deleteDownloadButton);

                                    }
//                                finalVideoSize.setText("Cancel".toUpperCase());
                                    finalVideoSize.setOnClickListener(v -> deleteDownloadVideo(contentDatum, position));

                                }
                                break;

                            case STATUS_FAILED:
                                Log.e(TAG, "Film download failed: " + contentDatum.getGist().getId());
                                deleteDownloadButton.setImageBitmap(null);
                                deleteDownloadButton.setBackground(ContextCompat.getDrawable(componentView.getContext(),
                                        android.R.drawable.stat_notify_error));
                                videoSize.setOnClickListener(v -> deleteDownloadVideo(contentDatum, position));
                                break;

                            case STATUS_SUCCESSFUL:
                                Log.e(TAG, "Film download successful: " + contentDatum.getGist().getId());
                                deleteDownloadButton.setImageBitmap(null);
                                deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_deleteicon));
                                deleteDownloadButton.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                                deleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                                contentDatum.getGist().setDownloadStatus(DownloadStatus.STATUS_COMPLETED);
                                break;

                            case STATUS_INTERRUPTED:
                                Log.e(TAG, "Film download interrupted: " + contentDatum.getGist().getId());
                                deleteDownloadButton.setImageBitmap(null);
                                deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                        android.R.drawable.stat_sys_warning));
                                videoSize.setText(localisedStrings.getRetryText());
                                int finalRadiusDifference1 = radiusDifference;
                                ImageButton finaldeleteDownloadButton1 = deleteDownloadButton;
                                finaldeleteDownloadButton1.setOnClickListener(v -> restartDownloadVideo(contentDatum, position,
                                        finaldeleteDownloadButton1, finalRadiusDifference1));
                                videoSize.setOnClickListener(v -> restartDownloadVideo(contentDatum, position,
                                        finaldeleteDownloadButton1, finalRadiusDifference1));

//                        videoSize.setText("Remove".toUpperCase());
//                        videoSize.setOnClickListener(v -> deleteDownloadVideo(contentDatum, position));
                                break;

                            case STATUS_PAUSED:
                                deleteDownloadButton.setImageBitmap(null);
                                deleteDownloadButton.setBackground(ContextCompat.getDrawable(componentView.getContext(),
                                        R.drawable.ic_download_queued));
                                videoSizeText.setText(localisedStrings.getCancelText());
                                deleteDownloadButton.bringToFront();
                                break;
                            default:
                                Log.e(TAG, "Film download status unknown: " + contentDatum.getGist().getId());
                                deleteDownloadButton.setImageBitmap(null);
                                break;
                        }
                    }
                    DownloadVideoRealm downloadVideoRealm = appCMSPresenter.getRealmController()
                            .getDownloadByIdBelongstoUser(contentDatum.getGist().getId(), userId);
                    if (downloadVideoRealm != null && contentDatum != null && contentDatum.getGist() != null) {
                        if (downloadVideoRealm.getWatchedTime() > contentDatum.getGist().getWatchedTime()) {
                            contentDatum.getGist().setWatchedTime(downloadVideoRealm.getWatchedTime());
                        }
                    }
                }
            }
        }
    }

    private synchronized void restartDownloadVideo(final ContentDatum contentDatum, int position,
                                                   final ImageButton downloadProgressImage, final int radiusDifference) {


        if ((isDownloadPage) && (contentDatum.getGist() != null)) {
            String msg = appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_re_download_item_message));
            String title = appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_download_retry_alert_title));
            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getRetryDownloadMessage() != null)
                msg = moduleAPI.getMetadataMap().getRetryDownloadMessage();
            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getDownloadNotFinish() != null)
                title = moduleAPI.getMetadataMap().getDownloadNotFinish();
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RE_START_DOWNLOAD_ITEM,
                    msg,
                    true, () ->
                            appCMSPresenter.reStartDownloadedFile(contentDatum.getGist().getId(),
                                    userVideoDownloadStatus -> {
                                        notifyItemChanged(position);
                                        //notifyItemRangeChanged(position, getItemCount());
                                        if (adapterData.size() == 0) {
                                            sendEvent(hideRemoveAllButtonEvent);
                                            notifyItemRangeChanged(position, getItemCount());
                                            notifyDataSetChanged();
                                            updateData(mRecyclerView, adapterData);
                                        }
                                    }, downloadProgressImage, radiusDifference)
                    ,
                    () ->  // cancelButton Action code
                            appCMSPresenter.removeDownloadedFile(contentDatum.getGist().getId(),
                                    userVideoDownloadStatus -> {
//                                    ((AppCMSWatchlistItemAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position))
//                                            .appCMSContinueWatchingDeleteButton.setImageBitmap(null);
                                        notifyItemRangeRemoved(position, getItemCount());
                                        adapterData.remove(contentDatum);
                                        notifyItemRangeChanged(position, getItemCount());
                                        if (adapterData.size() == 0) {
                                            sendEvent(hideRemoveAllButtonEvent);
                                            notifyDataSetChanged();
                                            updateData(mRecyclerView, adapterData);
                                        }
                                    }), title);
        }
    }

    private void deleteDownloadVideo(final ContentDatum contentDatum, int position) {
        String deleteMsg = appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_delete_one_download_video_item_message)));
        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getStrDeleteSingleContentFromDownloadAlertMessage() != null)
            deleteMsg = moduleAPI.getMetadataMap().getStrDeleteSingleContentFromDownloadAlertMessage();
        if (contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && contentDatum.getGist().getContentType().toLowerCase().equalsIgnoreCase(mContext.getString(R.string.media_type_audio).toLowerCase())) {
            deleteMsg = appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_delete_one_download_audio_item_message)));
            if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getDeleteAudioMessage() != null)
                deleteMsg = moduleAPI.getMetadataMap().getDeleteAudioMessage();
        }
        String dialogTitle = null;
        if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getStrDeleteDownloadAlertTitle() != null)
            dialogTitle = moduleAPI.getMetadataMap().getStrDeleteDownloadAlertTitle();
        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DELETE_ONE_DOWNLOAD_ITEM,
                deleteMsg, true, () ->{
                    if(contentDatum!=null && contentDatum.isDRMEnabled()){
                        appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().removeDownlodedObject(contentDatum.getGist().getId());
//                        DownloadService.sendRemoveDownload(mContext, OfflineDownloadService.class, contentDatum.getGist().getId(), /* foreground= */ false);
                        notifyItemRangeRemoved(position, getItemCount());
                        adapterData.remove(contentDatum);
                        notifyItemRangeChanged(position, getItemCount());
                        if (adapterData.size() == 0) {
                            emptyList = true;
                            sendEvent(hideRemoveAllButtonEvent);
                            notifyDataSetChanged();
                            updateData(mRecyclerView, adapterData);
                        }
                    }else {
                        appCMSPresenter.removeDownloadedFile(contentDatum.getGist().getId(),
                                userVideoDownloadStatus -> {
//                                    ((AppCMSWatchlistItemAdapter.ViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position))
//                                            .appCMSContinueWatchingDeleteButton.setImageBitmap(null);
                                    notifyItemRangeRemoved(position, getItemCount());
                                    adapterData.remove(contentDatum);
                                    notifyItemRangeChanged(position, getItemCount());
                                    if (adapterData.size() == 0) {
                                        emptyList = true;
                                        sendEvent(hideRemoveAllButtonEvent);
                                        notifyDataSetChanged();
                                        updateData(mRecyclerView, adapterData);
                                    }
                        });
                    }
                },
                null, dialogTitle);
    }

    private void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(Uri.decode(url))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if (isHistoryPage || isDownloadPage || isWatchlistPage || isLibraryPage || isFollowPage) {
            if (emptyList || adapterData.size() == 0)
                return 1;
        }
        return (adapterData != null ? adapterData.size() : 0);
    }

    @Override
    public void resetData(RecyclerView listView) {

    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        emptyList = contentData.size() == 0;

        listView.setAdapter(null);
        adapterData = null;
        adapterData = contentData;
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
        if (adapterData == null || adapterData.isEmpty() || adapterData.size() == 0) {
            sendEvent(hideRemoveAllButtonEvent);
            if (isDownloadPage)
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id) != null)
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id).setVisibility(View.GONE);
        } else {
            sendEvent(showRemoveAllButtonEvent);
            if (isDownloadPage)
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id) != null)
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id).setVisibility(View.VISIBLE);

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {

            onClickHandler = new CollectionGridItemView.OnClickHandler() {
                @Override
                public void click(CollectionGridItemView collectionGridItemView,
                                  Component childComponent,
                                  ContentDatum data, int clickPosition) {
                    trayClick(itemView, data, childComponent, position, clickPosition);
                }

                @Override
                public void play(Component childComponent, ContentDatum data) {

                }
            };

        }
        if (onClickHandlerConstraint == null) {
            onClickHandlerConstraint = new ConstraintCollectionGridItemView.OnClickHandler() {
                @Override
                public void click(ConstraintCollectionGridItemView collectionGridItemView, Component childComponent, ContentDatum data, int clickPosition) {
                    trayClick(itemView, data, childComponent, position, clickPosition);
                }

                @Override
                public void play(Component childComponent, ContentDatum data) {

                }
            };
        }

       /* for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter, position, null, blockName, moduleAPI.getMetadataMap());
        }*/

        if (itemView instanceof ConstraintCollectionGridItemView) {
            for (int i = 0; i < ((ConstraintCollectionGridItemView) itemView).getNumberOfChildren(); i++) {
                View childView = ((ConstraintCollectionGridItemView) itemView).getChild(i);
                if (component.getSettings() != null && component.getSettings().isNoInfo()
                        && componentViewType.contains("carousel") && !(childView instanceof ImageView)) {
                    continue;
                }
                ((ConstraintCollectionGridItemView) itemView).bindChild(itemView.getContext(),
                        ((ConstraintCollectionGridItemView) itemView).getChild(i),
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
                        blockName, moduleAPI.getMetadataMap());
            }
        }
    }


    private String getDeleteSingleItemHistoryAction(Context context) {
        return context.getString(R.string.app_cms_delete_single_history_action);
    }

    private String getDeleteSingleItemWatchlistAction(Context context) {
        return context.getString(R.string.app_cms_delete_single_watchlist_action);
    }

    private String getDeleteSingleItemDownloadAction(Context context) {
        return context.getString(R.string.app_cms_delete_single_download_action);
    }

    private String getTrayAction(Context context) {
        return context.getString(R.string.app_cms_action_detailvideopage_key);
    }

    private String getVideoAction(Context context) {
        return context.getString(R.string.app_cms_action_watchvideo_key);
    }

    private String getHlsUrl(ContentDatum data) {
        if (data.getStreamingInfo() != null &&
                data.getStreamingInfo().getVideoAssets() != null &&
                data.getStreamingInfo().getVideoAssets().getHls() != null) {
            return data.getStreamingInfo().getVideoAssets().getHls();
        }
        return null;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;
        ConstraintCollectionGridItemView constraintComponentView;
        TextView emptyView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.emptyView = itemView;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof FrameLayout)
                this.componentView = (CollectionGridItemView) itemView;
            else if (itemView instanceof ConstraintLayout) {
                this.constraintComponentView = (ConstraintCollectionGridItemView) itemView;
            }

        }

    }


    @Override
    public void addReceiver(OnInternalEvent e) {
        receivers.add(e);
        if (adapterData == null || adapterData.isEmpty() || adapterData.size() == 0) {
            sendEvent(hideRemoveAllButtonEvent);
        } else {
            sendEvent(showRemoveAllButtonEvent);
        }
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
        if (adapterData.size() == 0) {
            emptyList = true;
            sendEvent(hideRemoveAllButtonEvent);
            updateData(mRecyclerView, adapterData);
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public void cancel(boolean cancel) {

    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    private List<String> getListOfUpcomingMovies(int position, Object downloadStatus) {
        if (position + 1 == adapterData.size()) {
            return Collections.emptyList();
        }
        List<String> contentDatumList = new ArrayList<>();
        for (int i = position + 1; i < adapterData.size(); i++) {
            ContentDatum contentDatum = adapterData.get(i);
            if (contentDatum.getGist() != null &&
                    contentDatum.getGist().getDownloadStatus().equals(downloadStatus)
                    && contentDatum.getGist().getContentType() != null
                    && contentDatum.getGist().getContentType().toLowerCase().equalsIgnoreCase(mContext.getString(R.string.media_type_video).toLowerCase())) {
                contentDatumList.add(contentDatum.getGist().getId());
            }
            if (contentDatum.getGist() != null
                    && contentDatum.getGist().getContentType() != null
                    && contentDatum.getGist().getContentType().toLowerCase().equalsIgnoreCase(mContext.getString(R.string.media_type_video).toLowerCase())) {
                if((appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().isVideoOfflineDownloaded(contentDatum.getGist().getId()))!=null){
                    contentDatumList.add(contentDatum.getGist().getId());
                }
            }
        }
        return contentDatumList;
    }

    private void playDownloaded(ContentDatum data, int position) {
        List<String> relatedVideoIds = new ArrayList<>();
        String hlsUrl = data.getGist().getLocalFileUrl();

        boolean isNonDRMDownLoaded = appCMSPresenter.isVideoDownloaded(data.getGist().getId());
        boolean isDRMDownloaded = false;
        DownloadRequest download = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker().isVideoOfflineDownloaded(data.getGist().getId());

        if(download != null) {
            isDRMDownloaded = true;
        }

        if (!isDRMDownloaded && !isNonDRMDownLoaded) {
            if (data.getGist().getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                    data.getGist().getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL) {
                String title = null;
                String msg = null;

                if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getIncompleteDownload() != null)
                    title = moduleAPI.getMetadataMap().getIncompleteDownload();

                if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getIncompleteDownloadMessage() != null)
                    msg = moduleAPI.getMetadataMap().getIncompleteDownloadMessage();
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_INCOMPLETE,
                        msg,
                        false,
                        null,
                        null, title);
                return;
            }
        }
        else if(isDRMDownloaded) {
            data.setDRMEnabled(true);
            data.getGist().setLocalFileUrl(" ");
        }
        else if (data.getGist().getDownloadStatus() == DownloadStatus.STATUS_COMPLETED
                && data.getGist().getLocalFileUrl().contains("data")) {
            DownloadVideoRealm videoRealm = appCMSPresenter.getRealmController().getDownloadById(data.getGist().getId());
            data.getGist().setLocalFileUrl(videoRealm.getLocalURI());
        }
        else if (hlsUrl != null && hlsUrl.length() >= 5 && !new File(hlsUrl.substring(7)).exists()) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_INCOMPLETE,
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.app_cms_download_file_error_message)),
                    false,
                    null,
                    null, null);
            return;
        }

        String permalink = data.getGist().getPermalink();
        String action = mContext.getString(R.string.app_cms_action_watchvideo_key);
        String title = data.getGist() != null ? data.getGist().getTitle() : null;
        //String hlsUrl = data.getGist().getLocalFileUrl();

        String[] extraData = new String[4];
        extraData[0] = permalink;
        extraData[1] = hlsUrl;
        extraData[2] = data.getGist() != null ? data.getGist().getId() : null;
        extraData[3] = "true"; // to know that this is an offline video

        if (Boolean.parseBoolean(extraData[3])) {
            relatedVideoIds = getListOfUpcomingMovies(position, DownloadStatus.STATUS_COMPLETED);
            if (relatedVideoIds != null && relatedVideoIds.size() != 0) {
                /*remove current playing film id from the list*/
                relatedVideoIds.remove(data.getGist().getId());
            }
        }

        if (permalink == null ||
                hlsUrl == null ||
                extraData[2] == null ||
                !appCMSPresenter.launchButtonSelectedAction(
                        permalink,
                        action,
                        title,
                        extraData,
                        data,
                        false,
                        -1,
                        relatedVideoIds)) {
        }
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    private void playDownloadedAudio(ContentDatum contentDatum, int clickPosition) {
        try {
            if (contentDatum.getGist().getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                    contentDatum.getGist().getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL) {
                String title = null;
                String msg = null;
                if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getIncompleteDownload() != null)
                    title = moduleAPI.getMetadataMap().getIncompleteDownload();
                if (moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getIncompleteDownloadMessage() != null)
                    msg = moduleAPI.getMetadataMap().getIncompleteDownloadMessage();

                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_INCOMPLETE,
                        msg,
                        false,
                        null,
                        null, title);
                return;
            }
            appCMSPresenter.showLoadingDialog(true);

            if (contentDatum.getGist().getDownloadStatus() == DownloadStatus.STATUS_COMPLETED
                    && contentDatum.getGist().getLocalFileUrl().contains("data")) {
                DownloadVideoRealm videoRealm = appCMSPresenter.getRealmController().getDownloadById(contentDatum.getGist().getId());
                contentDatum.getGist().setPosterImageUrl(videoRealm.getPosterFileURL());
                contentDatum.getGist().setLocalFileUrl(videoRealm.getLocalURI());
//                notifyItemChanged(clickPosition);
            }
            AppCMSAudioDetailResult appCMSAudioDetailResult = convertToAudioResult(contentDatum);
        /*
        if at the time of click from download list device already connected to casatin device than get audio details from server
        and cast audio url to casting device
         */
            if (CastServiceProvider.getInstance(mContext).isCastingConnected()) {
                AudioPlaylistHelper.getInstance().playAudioOnClickItem(appCMSAudioDetailResult.getId(), 0);
            } else {
                AppCMSPageAPI audioApiDetail = appCMSAudioDetailResult.convertToAppCMSPageAPI(appCMSAudioDetailResult.getId());
                AudioPlaylistHelper.getInstance().createMediaMetaDataForAudioItem(appCMSAudioDetailResult);
                PlaybackManager.setCurrentMediaData(AudioPlaylistHelper.getInstance().getMetadata(appCMSAudioDetailResult.getId()));
                if (appCMSPresenter.getCallBackPlaylistHelper() != null) {
                    appCMSPresenter.getCallBackPlaylistHelper().onPlaybackStart(AudioPlaylistHelper.getInstance().getMediaMetaDataItem(appCMSAudioDetailResult.getId()), 0);
                } else if (appCMSPresenter.getCurrentActivity() != null) {
                    AudioPlaylistHelper.getInstance().onMediaItemSelected(AudioPlaylistHelper.getInstance().getMediaMetaDataItem(appCMSAudioDetailResult.getId()), 0);
                }
                AudioPlaylistHelper.getInstance().setCurrentAudioPLayingData(audioApiDetail.getModules().get(0).getContentData().get(0));

                List<String> audioPlaylistId = new ArrayList<String>();
                audioPlaylistId.add(appCMSAudioDetailResult.getGist().getId());
                AudioPlaylistHelper.getInstance().setCurrentPlaylistData(null);
                AudioPlaylistHelper.getInstance().setCurrentPlaylistId(appCMSAudioDetailResult.getGist().getId());
                AudioPlaylistHelper.getInstance().setPlaylist(audioPlaylistId);
                try {
                    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                    int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
                    if (resultCode == ConnectionResult.SUCCESS) {

                        Intent intent = new Intent(mContext, AppCMSPlayAudioActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        MediaControllerCompat controller = MediaControllerCompat.getMediaController(appCMSPresenter.getCurrentActivity());
                        if (controller != null) {
                            MediaMetadataCompat metadata = controller.getMetadata();
                            if (metadata != null) {
                                intent.putExtra(EXTRA_CURRENT_MEDIA_DESCRIPTION,
                                        metadata);
                            }
                        }
                        mContext.startActivity(intent);
                    } else {
                        int PLAY_SERVICES_RESOLUTION_REQUEST = 1001;
                        if (apiAvailability.isUserResolvableError(resultCode)) {
                            if (BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                                apiAvailability.getErrorDialog((Activity) mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                                        .show();
                            }
                        } /*else {
                        Log.i(TAG, "This device is not supported.");
                        Toast.makeText(mContext, "This device is not supported.", Toast.LENGTH_SHORT).show();
                    }*/
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            appCMSPresenter.showLoadingDialog(false);
        }
    }

    private AppCMSAudioDetailResult convertToAudioResult(ContentDatum contentDatum) {
        AppCMSAudioDetailResult appCMSAudioDetailResult = new AppCMSAudioDetailResult();
        appCMSAudioDetailResult.setId(contentDatum.getGist().getId());
        StreamingInfo streamingInfo = new StreamingInfo();
        streamingInfo.setAudioAssets(new AudioAssets(new Mp3(contentDatum.getGist().getLocalFileUrl())));
        appCMSAudioDetailResult.setGist(contentDatum.getGist());
        appCMSAudioDetailResult.setStreamingInfo(streamingInfo);
        return appCMSAudioDetailResult;
    }

    void trayClick(View itemView, ContentDatum data, Component childComponent, int position, int clickPosition) {
        if (isClickable) {

            if (data.getGist() != null && data.getGist().getContentType() != null && data.getGist().getContentType().equalsIgnoreCase(mContext.getString(R.string.content_type_event))) {
                appCMSPresenter.navigateToEventDetailPage(data.getGist().getPermalink());
            } else if (data.getGist() != null) {
                //Log.d(TAG, "Clicked on item: " + data.getGist().getTitle());
                String permalink = data.getGist().getPermalink();
                String action = trayAction;
                if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null) {
                    if (appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() && appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                            && data.getGist() != null && data.getGist().getMediaType() != null
                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase())) {
                        action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                    } else if (appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() && !appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()
                            && data.getGist() != null && data.getGist().getMediaType() != null
                            && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase())) {
                        action = mContext.getString(R.string.app_cms_action_detailvideopage_key);
                    } else if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay() &&
                            !appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail()
                            && data.getGist() != null && data.getGist().getMediaType() != null
                            && !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_series).toLowerCase())) {
                        action = mContext.getString(R.string.app_cms_action_watchvideo_key);
                    }
                }
                if (childComponent != null && !TextUtils.isEmpty(childComponent.getAction())) {
                    if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                            data.getGist() != null && data.getGist().getMediaType() != null &&
                            appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() &&
                            childComponent.getAction().equalsIgnoreCase(mContext.getString(R.string.app_cms_action_watchvideo_key))
                            && !data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_video).toLowerCase()))
                        action = mContext.getString(R.string.app_cms_action_detailvideopage_key);
                    else
                        action = childComponent.getAction();
                }
//
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
                /*open show detail page*/
                if (appCMSPresenter.getAppCMSMain() != null &&
                        appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                        appCMSPresenter.getAppCMSMain().getFeatures().isOpenShowDetail() &&
                        action != null &&
                        action.equalsIgnoreCase(mContext.getString(R.string.app_cms_action_detailvideopage_key)) &&
                        data.getSeriesData() != null &&
                        data.getSeriesData().size() > 0 && data.getSeriesData().get(0).getGist() != null &&
                        data.getSeriesData().get(0).getGist().getPermalink() != null &&
                        data.getGist() != null) {
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
                /*navigate to article detail page*/
                if (data.getGist() != null && data.getGist().getMediaType() != null
                        && data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.app_cms_article_key_type).toLowerCase())) {
                    appCMSPresenter.setCurrentArticleIndex(-1);
                    appCMSPresenter.navigateToArticlePage(data.getGist().getId(), data.getGist().getTitle(), false, null, false);
                    return;
                }

                if (action.contains(deleteSingleItemDownloadAction)) {
                    /*delete a single downloaded video*/
                    deleteDownloadVideo(data, position);
                    return;
                }
                if (action.contains(videoAction)) {
                    appCMSPresenter.setPlaySource("");
                    appCMSPresenter.setPlaySource(moduleAPI.getTitle());
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_Video Detail");
                }

                if (action.contains("watchVideo") || (data.getGist() != null &&
                        data.getGist().getMediaType() != null &&
                        data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                        data.getGist().getContentType() != null &&
                        data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase()))) {
                    appCMSPresenter.setPlaySource("");
                    appCMSPresenter.setPlaySource(appCMSPresenter.getPlaySource() + "_" + moduleAPI.getTitle());
                }
                if (action.contains(deleteSingleItemWatchlistAction)) {
                    String dialogTitle = "";
                    if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrDeleteWatchlistAlertTitle() != null)
                        dialogTitle = moduleAPI.getMetadataMap().getkStrDeleteWatchlistAlertTitle();
                    String dialogMsg = appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_delete_one_watchlist_item_message);
                    if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrDeleteSingleVideoFromWatchlistAlertMessage() != null)
                        dialogMsg = moduleAPI.getMetadataMap().getkStrDeleteSingleVideoFromWatchlistAlertMessage();

                    /*delete video from user watchlist*/
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DELETE_ONE_WATCHLIST_ITEM,
                            (appCMSPresenter.getLanguageResourcesFile().getStringValue(dialogMsg, appCMSPresenter.getWatchlistPage() != null ? appCMSPresenter.getWatchlistPage().getPageName() : "Page!")),
                            true, () ->
                                    appCMSPresenter.editWatchlist(moduleAPI.getMetadataMap(), data,
                                            addToWatchlistResult -> {
                                                appCMSPresenter.sendRemoveWatchlistEvent(data);
                                                adapterData.remove(data);

                                                if (adapterData.size() == 0) {
                                                    emptyList = true;
                                                    sendEvent(hideRemoveAllButtonEvent);
                                                    updateData(mRecyclerView, adapterData);
                                                }
                                                notifyDataSetChanged();
                                            }, false,
                                            false, null, null),
                            null, dialogTitle);
                    return;
                }
                if (action.contains(deleteSingleItemHistoryAction)) {
                    String dialogTitle = "";
                    if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrDeleteHistoryAlertTitle() != null)
                        dialogTitle = moduleAPI.getMetadataMap().getkStrDeleteHistoryAlertTitle();
                    String dialogMsg = appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.app_cms_delete_one_history_item_message));
                    if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getkStrDeleteSingleVideoFromHistoryAlertMessage() != null)
                        dialogMsg = moduleAPI.getMetadataMap().getkStrDeleteSingleVideoFromHistoryAlertMessage();

                    /*delete video from user history*/
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DELETE_ONE_HISTORY_ITEM, dialogMsg
                            ,
                            true, () -> {
                                String id = data.getGist().getOriginalObjectId();
                                if (id == null) {
                                    id = data.getGist().getId();
                                }
                                String seriesId = data.getGist().getSeriesId();
                                appCMSPresenter.editHistory(id, seriesId,
                                        appCMSDeleteHistoryResult -> {
                                            adapterData.remove(data);
                                            if (adapterData.size() == 0) {
                                                emptyList = true;
                                                sendEvent(hideRemoveAllButtonEvent);
                                                updateData(mRecyclerView, adapterData);
                                            }
                                            notifyDataSetChanged();
                                        }, false);
                            },
                            null, dialogTitle);
                    return;
                }

                if (action != null && !TextUtils.isEmpty(action) &&
                        data.getGist() != null &&
                        data.getGist().getContentType() != null &&
                        (data.getGist().getContentType().equalsIgnoreCase("SERIES") || data.getGist().getContentType().equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_episodic_season_prefix)))) {
                    action = mContext.getString(R.string.app_cms_action_showvideopage_key);
                    if (data.getGist().getSeriesPermalink() != null) {
                        permalink = data.getGist().getSeriesPermalink();
                    }
                }


                if (action.contains(videoAction)) {

                    if (isDownloadPage) {
                        if (data.getGist() != null &&
                                data.getGist().getMediaType() != null &&
                                data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                data.getGist().getContentType() != null &&
                                data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                            /*play audio if already downloaded*/
                            playDownloadedAudio(data, clickPosition);

                            return;
                        } else {
                            /*play movie if already downloaded*/
                            playDownloaded(data, clickPosition);
                            return;
                        }
                    } else {

//                                    if (isLibraryPage) {

                        int finalCurrentPlayingIndex = currentPlayingIndex;
                        List<String> finalRelatedVideoIds = relatedVideoIds;
                        String finalAction = action;

                        if ((data.getPricing() != null && data.getPricing().getType() != null && ((data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_TVOD))
                                || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_PPV))) ||
                                (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                        || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))))
                                || (data.getGist().getPurchaseType() != null && (data.getGist().getPurchaseType().equalsIgnoreCase("RENT")))
                        ) {


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
                                        isPurchased = true;

                                    }
                                } else {
                                    isPlayable = false;

                                }


                                if ((data.getPricing() != null && data.getPricing().getType() != null) && (data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                        || data.getPricing().getType().equalsIgnoreCase(mContext.getString(R.string.PURCHASE_TYPE_SVOD_PPV)))) {

//                                                if (!appCMSPresenter.isUserLoggedIn()) {
//                                                    appCMSPresenter.showSubscribeMessage();
//                                                    return;
//                                                }else{
//                                                    appCMSPresenter.launchVideoPlayer(data,
//                                                            data.getGist().getId(),
//                                                            finalCurrentPlayingIndex,
//                                                            finalRelatedVideoIds,
//                                                            -1,
//                                                            finalAction);
//                                                    return;
//                                                }
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

                                    /**
                                     * if schedule time is available for video then compare start time
                                     * and end time and check validation in isScheduleVideoPlayable()
                                     */

                                    if (!appCMSPresenter.isScheduleVideoPlayable(data.getGist().getScheduleStartDate(), data.getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {
                                        return;
                                    }
                                    boolean isShowRentalPeriodDialog = true;
                                    /**
                                     * if transaction getdata api containf transaction end date .It means Rent API called before
                                     * and we have shown rent period dialog before so dont need to show rent dialog again. else show rent period dilaog
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
                                                    appCMSPresenter.launchVideoPlayer(data,
                                                            data.getGist().getId(),
                                                            finalCurrentPlayingIndex,
                                                            finalRelatedVideoIds,
                                                            -1,
                                                            finalAction);
                                                }, false, 0);
                                            } else {
//                                                appCMSPresenter.sendCloseOthersAction(null, true, false);
                                            }
                                        }, msg, "", "", "", true, true);
                                    } else {
                                        appCMSPresenter.setPricingContent(true);
                                        appCMSPresenter.launchVideoPlayer(data,
                                                data.getGist().getId(),
                                                finalCurrentPlayingIndex,
                                                finalRelatedVideoIds,
                                                -1,
                                                finalAction);
                                    }

                                }

                            }, "Video");
                        } else if (((data.getGist() != null && data.getGist().getPurchaseStatus() != null && data.getGist().getPurchaseStatus().equalsIgnoreCase("PURCHASED")))) {

                            appCMSPresenter.setPricingContent(true);
                            appCMSPresenter.launchVideoPlayer(data,
                                    data.getGist().getId(),
                                    finalCurrentPlayingIndex,
                                    finalRelatedVideoIds,
                                    -1,
                                    finalAction);
                        } else if (appCMSPresenter.isScheduleVideoPlayable(data.getGist().getScheduleStartDate(), data.getGist().getScheduleEndDate(), moduleAPI.getMetadataMap())) {


                            appCMSPresenter.launchVideoPlayer(data,
                                    data.getGist().getId(),
                                    finalCurrentPlayingIndex,
                                    finalRelatedVideoIds,
                                    -1,
                                    finalAction);
                        }

                    }
                } else if (action != null && !TextUtils.isEmpty(action)) {

                    if (isDownloadPage && action.contains(trayAction)) {
                        if (data.getGist() != null &&
                                data.getGist().getMediaType() != null &&
                                data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                data.getGist().getContentType() != null &&
                                data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                            /*play audio if already downloaded*/
                            playDownloadedAudio(data, clickPosition);
                            return;
                        } else {
                            /*play movie if already downloaded*/
                            playDownloaded(data, clickPosition);
                            return;
                        }
                    }

                    /*open video detail page*/
                    appCMSPresenter.launchButtonSelectedAction(permalink,
                            action,
                            title,
                            null,
                            data,
                            false,
                            currentPlayingIndex,
                            relatedVideoIds);

                }

            }
        }
    }
}

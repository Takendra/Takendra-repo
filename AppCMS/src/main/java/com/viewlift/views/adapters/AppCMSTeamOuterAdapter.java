package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import androidx.core.content.ContextCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.audio.playback.AudioPlaylistHelper;
import com.viewlift.audio.playback.PlaybackManager;
import com.viewlift.R;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.StreamingInfo;
import com.viewlift.models.data.appcms.audio.AppCMSAudioDetailResult;
import com.viewlift.models.data.appcms.audio.AudioAssets;
import com.viewlift.models.data.appcms.audio.Mp3;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.downloads.DownloadVideoRealm;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPlayAudioActivity;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.ViewCreator;

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

public class AppCMSTeamOuterAdapter extends RecyclerView.Adapter<AppCMSTeamOuterAdapter.ViewHolder>
        implements AppCMSBaseAdapter, OnInternalEvent {
    private static final String TAG = "UserWatHisDowAdapter";


    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
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
    private boolean isDonwloadPage;
    private boolean isWatchlistPage;
    private Map<String, Boolean> filmDownloadIconUpdatedMap;
    String blockName;
    public AppCMSTeamOuterAdapter(Context context,
                                  ViewCreator viewCreator,
                                  AppCMSPresenter appCMSPresenter,
                                  Layout parentLayout,
                                  boolean useParentSize,
                                  Component component,
                                  Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                  Module moduleAPI,
                                  int defaultWidth,
                                  int defaultHeight,
                                  String viewType,
                                  AppCMSAndroidModules appCMSAndroidModules,String blockName) {
        this.mContext = context;
        this.viewCreator = viewCreator;
//        this.appCMSPresenter = appCMSPresenter;
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

        switch (jsonValueKeyMap.get(viewType)) {
            case PAGE_HISTORY_01_MODULE_KEY:
            case PAGE_HISTORY_02_MODULE_KEY:
                this.isHistoryPage = true;
                break;

            case PAGE_DOWNLOAD_01_MODULE_KEY:
            case PAGE_DOWNLOAD_02_MODULE_KEY:
                this.filmDownloadIconUpdatedMap = new HashMap<>();
                this.isDonwloadPage = true;
                break;

            case PAGE_WATCHLIST_01_MODULE_KEY:
            case PAGE_WATCHLIST_02_MODULE_KEY:
                this.isWatchlistPage = true;
                break;

            default:
                break;
        }
    }

    private void sortData() {
        if (adapterData != null) {
            if (isWatchlistPage || isDonwloadPage) {
                sortByAddedDate();
            } else if (isHistoryPage) {
                sortByUpdateDate();
            }
        }
    }

    private void sortByAddedDate() {
        try {
        Collections.sort(adapterData, (o1, o2) -> Double.compare((Double) o1.getAddedDate(),
                (Double) o2.getAddedDate()));
        } catch (Exception ex) {
        ex.printStackTrace();
        }
    }

    private void sortByUpdateDate() {
        try {
            Collections.sort(adapterData, (o1, o2) -> Long.compare((Long) o1.getUpdateDate(),
                    (Long) o2.getUpdateDate()));
            if (adapterData!= null && adapterData.size()>=2) {
                if (Long.compare((Long)adapterData.get(0).getUpdateDate(),(Long)adapterData.get(1).getUpdateDate())==1) {
                    Collections.reverse(adapterData);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = viewCreator.createCollectionGridItemView(parent.getContext(),
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
                false, viewTypeKey,blockName);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        view.setLayoutParams(lp);

        if (emptyList) {
            TextView emptyView = new TextView(mContext);
            emptyView.setTextColor(appCMSPresenter.getGeneralTextColor());
            emptyView.setTextSize(24f);
            if (isHistoryPage) {
                emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.empty_history_list_message)));
                return new ViewHolder(emptyView);
            }
            if (isWatchlistPage) {
                //emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.empty_watchlist_message)));
                emptyView.setText(appCMSPresenter.getLanguageResourcesFile().getStringValue(mContext.getString(R.string.empty_watchlist_message),appCMSPresenter.getWatchlistPage()!= null?appCMSPresenter.getWatchlistPage().getPageName():"Page!"));
                return new ViewHolder(emptyView);
            }

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (adapterData != null && adapterData.size() == 0) {
            sendEvent(hideRemoveAllButtonEvent);
        }
        if (!emptyList) {
            if (0 <= position && position < adapterData.size()) {
                for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                    if (holder.componentView.getChild(i) instanceof TextView) {
                        ((TextView) holder.componentView.getChild(i)).setText("");
                    }
                }
                bindView(holder.componentView, adapterData.get(position), position);

                if (isDonwloadPage) {
                    downloadView(adapterData.get(position), holder.componentView, position);
                }
            }
        }
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
        if (videoSize != null && deleteDownloadButton != null && thumbnailImage != null) {
            videoSize.setVisibility(View.VISIBLE);
            videoSize.setText(appCMSPresenter.getDownloadedFileSize(contentDatum.getGist().getId()));
            int radiusDifference = 5;
            if (BaseView.isTablet(componentView.getContext())) {
                radiusDifference = 3;
            }
            if (contentDatum.getGist() != null) {
                deleteDownloadButton.setTag(contentDatum.getGist().getId());
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
                    videoSize.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.app_cms_cancel_alert_dialog_button_text)));

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
                                        videoSizeText.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.app_cms_cancel_alert_dialog_button_text)));

                                    }
                                    if (videoDownloadStatus != null && videoDownloadStatus.getDownloadStatus() != null) {
                                        contentDatum.getGist().setDownloadStatus(videoDownloadStatus.getDownloadStatus());
                                    }
                                }
                            },
                            appPreference.getLoggedInUser());

                    switch (contentDatum.getGist().getDownloadStatus()) {
                        case STATUS_PENDING:
                            deleteButton.setImageBitmap(null);
                            deleteButton.setBackground(ContextCompat.getDrawable(componentView.getContext(),
                                    R.drawable.ic_download_queued));
                            videoSizeText.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.app_cms_cancel_alert_dialog_button_text)));

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
                                                        finalDeleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                                                R.drawable.ic_deleteicon));
                                                        finalDeleteDownloadButton.invalidate();

                                                        if (contentDatum.getGist().getMediaType().equalsIgnoreCase(mContext.getResources().getString(R.string.media_type_audio))) {
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

                                                        notifyDataSetChanged();
//                                                        notifyItemChanged(position);

                                                    } else if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_INTERRUPTED) {
                                                        finalDeleteDownloadButton.setImageBitmap(null);
                                                        finalDeleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                                                android.R.drawable.stat_sys_warning));
                                                        finalVideoSize.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.title_re_try)));
                                                        finalVideoSize.setOnClickListener(v -> restartDownloadVideo(contentDatum, position, finalDeleteDownloadButton, finalRadiusDifference));
                                                    } else if (userVideoDownloadStatus.getDownloadStatus() == STATUS_RUNNING) {
                                                        finalVideoSize.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.app_cms_cancel_alert_dialog_button_text)));
                                                    } else if (userVideoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING) {
                                                        finalDeleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                                                R.drawable.ic_download_queued));
                                                        finalVideoSize.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.app_cms_cancel_alert_dialog_button_text)));

                                                    }
                                                    contentDatum.getGist().setDownloadStatus(userVideoDownloadStatus.getDownloadStatus());
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
                            break;

                        case STATUS_SUCCESSFUL:
                            Log.e(TAG, "Film download successful: " + contentDatum.getGist().getId());
                            deleteDownloadButton.setImageBitmap(null);
                            deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                    R.drawable.ic_deleteicon));
                            deleteDownloadButton.getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                            deleteDownloadButton.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                            contentDatum.getGist().setDownloadStatus(DownloadStatus.STATUS_COMPLETED);
                            break;

                        case STATUS_INTERRUPTED:
                            Log.e(TAG, "Film download interrupted: " + contentDatum.getGist().getId());
                            deleteDownloadButton.setImageBitmap(null);
                            deleteDownloadButton.setBackground(ContextCompat.getDrawable(mContext,
                                    android.R.drawable.stat_sys_warning));
                            videoSize.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.title_re_try)));
                            int finalRadiusDifference1 = radiusDifference;
                            ImageButton finaldeleteDownloadButton1 = deleteDownloadButton;
                            videoSize.setOnClickListener(v -> restartDownloadVideo(contentDatum, position,
                                    finaldeleteDownloadButton1, finalRadiusDifference1));

//                        videoSize.setText("Remove".toUpperCase());
//                        videoSize.setOnClickListener(v -> deleteDownloadVideo(contentDatum, position));
                            break;

                        case STATUS_PAUSED:
                            deleteDownloadButton.setImageBitmap(null);
                            deleteDownloadButton.setBackground(ContextCompat.getDrawable(componentView.getContext(),
                                    R.drawable.ic_download_queued));
                            videoSizeText.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getResources().getString(R.string.app_cms_cancel_alert_dialog_button_text)));

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

    private synchronized void restartDownloadVideo(final ContentDatum contentDatum, int position,
                                                   final ImageButton downloadProgressImage, final int radiusDifference) {


        if ((isDonwloadPage) && (contentDatum.getGist() != null)) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RE_START_DOWNLOAD_ITEM,
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_re_download_item_message)),
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
                                    }),null);
        }
    }

    private void deleteDownloadVideo(final ContentDatum contentDatum, int position) {
        String deleteMsg = appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_delete_one_download_video_item_message));
        if (contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && contentDatum.getGist().getContentType().toLowerCase().equalsIgnoreCase(mContext.getString(R.string.media_type_audio).toLowerCase())) {
            deleteMsg = appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_delete_one_download_audio_item_message));
        }
        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DELETE_ONE_DOWNLOAD_ITEM,
                deleteMsg, true, () ->
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
                                }),
                null,null);
    }

    private void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(Uri.decode(url))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if (isHistoryPage || isDonwloadPage || isWatchlistPage) {
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
            if (isDonwloadPage)
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id) != null)
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id).setVisibility(View.GONE);
        } else {
            sendEvent(showRemoveAllButtonEvent);
            if (isDonwloadPage)
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id) != null)
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.remove_all_download_id).setVisibility(View.VISIBLE);

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(CollectionGridItemView itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {

            onClickHandler = new CollectionGridItemView.OnClickHandler() {
                @Override
                public void click(CollectionGridItemView collectionGridItemView,
                                  Component childComponent,
                                  ContentDatum data, int clickPosition) {
                    if (isClickable) {

                        if (data.getGist() != null) {
                            //Log.d(TAG, "Clicked on item: " + data.getGist().getTitle());
                            String permalink = data.getGist().getPermalink();
                            String action = trayAction;
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
                            if (action.contains(deleteSingleItemWatchlistAction)) {
                                /*delete video from user watchlist*/
                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DELETE_ONE_WATCHLIST_ITEM,
                                        (appCMSPresenter.getLanguageResourcesFile().getStringValue(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_delete_one_watchlist_item_message),appCMSPresenter.getWatchlistPage()!= null?appCMSPresenter.getWatchlistPage().getPageName():"Page!")),
                                        true, () ->
                                                appCMSPresenter.editWatchlist(moduleAPI.getMetadataMap(),data,
                                                        addToWatchlistResult -> {
                                                            adapterData.remove(data);

                                                            if (adapterData.size() == 0) {
                                                                emptyList = true;
                                                                sendEvent(hideRemoveAllButtonEvent);
                                                                updateData(mRecyclerView, adapterData);
                                                            }
                                                            notifyDataSetChanged();
                                                        }, false,
                                                        false,
                                                        null,null),
                                        null,null);
                                return;
                            }
                            if (action.contains(deleteSingleItemHistoryAction)) {
                                /*delete video from user history*/
                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DELETE_ONE_HISTORY_ITEM,
                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(mContext.getString(R.string.app_cms_delete_one_history_item_message)),
                                        true, () -> {
                                            String id = data.getGist().getOriginalObjectId();
                                            if (id == null) {
                                                id = data.getGist().getId();
                                            }
                                            String seriesId = data.getGist().getSeriesId();
                                            appCMSPresenter.editHistory(id,seriesId,
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
                                        null,null);
                                return;
                            }
                            if (action.contains(videoAction)) {
                                if (isDonwloadPage) {
                                    if (data.getGist() != null &&
                                            data.getGist().getMediaType() != null &&
                                            data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                            data.getGist().getContentType() != null &&
                                            data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                                        /*play audio if already downloaded*/
                                        playDownloadedAudio(data);

                                        return;
                                    } else {
                                        /*play movie if already downloaded*/
                                        playDownloaded(data, clickPosition);
                                        return;
                                    }
                                } else {
                                    /*play movie from web URL*/
                                    appCMSPresenter.launchVideoPlayer(data,
                                            data.getGist().getId(),
                                            currentPlayingIndex,
                                            relatedVideoIds,
                                            -1,
                                            action);
                                }
                            }
                            if (action != null && !TextUtils.isEmpty(action)) {

                                if (isDonwloadPage && action.contains(trayAction)) {
                                    if (data.getGist() != null &&
                                            data.getGist().getMediaType() != null &&
                                            data.getGist().getMediaType().toLowerCase().contains(itemView.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                                            data.getGist().getContentType() != null &&
                                            data.getGist().getContentType().toLowerCase().contains(itemView.getContext().getString(R.string.content_type_audio).toLowerCase())) {
                                        /*play audio if already downloaded*/
                                        playDownloadedAudio(data);
                                        return;
                                    } else {
                                        /*play movie if already downloaded*/
                                        playDownloaded(data, clickPosition);
                                        return;
                                    }
                                }
                                if (action.contains(trayAction) &&
                                        data.getGist() != null &&
                                        data.getGist().getContentType() != null &&
                                        data.getGist().getContentType().equalsIgnoreCase("SERIES")) {
                                    action = mContext.getString(R.string.app_cms_action_showvideopage_key);
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

                @Override
                public void play(Component childComponent, ContentDatum data) {
                }
            };

        }

        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter, position, settings, blockName,moduleAPI.getMetadataMap());
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
        TextView emptyView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            this.emptyView = itemView;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.componentView = (CollectionGridItemView) itemView;
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
        }

        return contentDatumList;
    }

    private void playDownloaded(ContentDatum data, int position) {
        List<String> relatedVideoIds = new ArrayList<>();
        if (data.getGist().getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                data.getGist().getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_INCOMPLETE,
                    null,
                    false,
                    null,
                    null,null);
            return;
        }

        String permalink = data.getGist().getPermalink();
        String action = mContext.getString(R.string.app_cms_action_watchvideo_key);
        String title = data.getGist() != null ? data.getGist().getTitle() : null;
        String hlsUrl = data.getGist().getLocalFileUrl();

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

    private void playDownloadedAudio(ContentDatum contentDatum) {
        try {


            if (contentDatum.getGist().getDownloadStatus() != DownloadStatus.STATUS_COMPLETED &&
                    contentDatum.getGist().getDownloadStatus() != DownloadStatus.STATUS_SUCCESSFUL) {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_INCOMPLETE,
                        null,
                        false,
                        null,
                        null,null);
                return;
            }
            appCMSPresenter.showLoadingDialog(true);
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
                        if(BuildConfig.FLAVOR.equalsIgnoreCase(AppCMSPresenter.MOBILE_BUILD_VARIENT)) {
                            apiAvailability.getErrorDialog((Activity) mContext, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                                    .show();
                        }
                    } else {
                        Log.i(TAG, "This device is not supported.");
                        Toast.makeText(mContext, "This device is not supported.", Toast.LENGTH_SHORT).show();
                    }
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
}

package com.viewlift.views.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.audio.AppCMSAudioDetailResult;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.downloads.UserVideoDownloadStatus;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.ContentTypeChecker;
import com.viewlift.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import rx.functions.Action1;

import static com.viewlift.presenters.AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE_ALERT;

public class DownloadComponent extends ConstraintLayout {

    Context mContext;
    AppCMSPresenter appCMSPresenter;
    ImageButton imageButtonDownloadStatus;
    ProgressBar progressBarDownload;

    Component mComponent;
    ContentDatum mData;
    Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Map<String, UpdateDownloadImageIconAction> updateDownloadImageIconActionMap;


    public DownloadComponent(Context context) {
        super(context);
    }

    public DownloadComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DownloadComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DownloadComponent(Context context, AppCMSPresenter appCMSPresenter, Component component, ContentDatum data, Map<String, AppCMSUIKeyType> jsonValueKeyMap) {
        this(context);
        this.mContext = context;
        this.appCMSPresenter = appCMSPresenter;
        mData = data;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.mComponent = component;
        updateDownloadImageIconActionMap = appCMSPresenter.getUpdateDownloadComponentImageIconActionMap();
        initView();
    }


    public void initView() {

        int width = (int) BaseView.getViewWidth(mContext, mComponent.getLayout(), ViewGroup.LayoutParams.WRAP_CONTENT);
        String ratio = BaseView.getViewRatio(mContext, mComponent.getLayout(), "1:1");
        int height = BaseView.getViewHeightByRatio(ratio, width);

        setId(R.id.downloadButton);
        imageButtonDownloadStatus = new ImageButton(mContext);
        ConstraintLayout.LayoutParams lpImage = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lpImage.rightToRight = ConstraintSet.PARENT_ID;
        lpImage.height = height;
        lpImage.width = width;
        imageButtonDownloadStatus.setLayoutParams(lpImage);
        imageButtonDownloadStatus.setBackgroundColor(Color.parseColor("#00000000"));
        // imageButtonDownloadStatus.setBackgroundResource(R.drawable.ic_download_big);
        imageButtonDownloadStatus.setId(R.id.imageButtonDownloadStatus);
        //imageButtonDownloadStatus.setScaleType(ImageView.ScaleType.FIT_XY);
        imageButtonDownloadStatus.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //imageButtonDownloadStatus.setScaleType(ImageView.ScaleType.FIT_START);


        progressBarDownload = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        //progressBarDownload = new ProgressBar(mContext);
        progressBarDownload.setId(R.id.progressBarDownload);
        //progressBarDownload.setVisibility(INVISIBLE);
        progressBarDownload.setProgressDrawable(mContext.getDrawable(R.drawable.circular));
        progressBarDownload.setSecondaryProgress(100); // Secondary Progress
        progressBarDownload.setMax(100);
        progressBarDownload.setIndeterminate(false);
        int[][] states1 = {{android.R.attr.state_checked}, {}};
        int[] colors1 = {appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getGeneralTextColor()};
        progressBarDownload.setProgressTintList(new ColorStateList(states1, colors1));

        ConstraintSet set = new ConstraintSet();
        set.clone(this);

        set.connect(progressBarDownload.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 5);
        set.connect(progressBarDownload.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 5);
        set.connect(progressBarDownload.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 5);
        set.connect(progressBarDownload.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 5);
        set.connect(progressBarDownload.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 5);
        set.connect(progressBarDownload.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 5);

        addView(progressBarDownload);
        addView(imageButtonDownloadStatus);

        set.applyTo(this);

        progressBarDownload.setVisibility(INVISIBLE);

        if (!ViewCreator.isScheduleContentVisible(mData, appCMSPresenter)) {
            setVisibility(View.INVISIBLE);
        } else if (ViewCreator.isVideoIsSchedule(mData, appCMSPresenter)) {
            setVisibility(View.INVISIBLE);
        }
        try {

            if (mData != null &&
                    mData.getGist() != null &&
                    mData.getGist().isLiveStream()) {
                setVisibility(View.INVISIBLE);
            } else {

                try {
                    UpdateDownloadImageIconAction updateDownloadImageIconAction =
                            updateDownloadImageIconActionMap.get(mData.getGist().getId());
                    if (updateDownloadImageIconAction == null) {
                        updateDownloadImageIconAction = new UpdateDownloadImageIconAction(this, appCMSPresenter,
                                mData, appCMSPresenter.getLoggedInUser());
                        updateDownloadImageIconActionMap.put(mData.getGist().getId(), updateDownloadImageIconAction);
                    }

                    imageButtonDownloadStatus.setTag(mData.getGist().getId());

                    updateDownloadImageIconAction.updateDownloadImageButton(this);

                    appCMSPresenter.getUserVideoDownloadStatus(
                            mData.getGist().getId(), updateDownloadImageIconAction, appCMSPresenter.getLoggedInUser());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setChildVisibility(int isVisible) {
        imageButtonDownloadStatus.setVisibility(isVisible);
        progressBarDownload.setVisibility(isVisible);
    }

    public ImageButton getImageButtonDownloadStatus() {
        return imageButtonDownloadStatus;
    }

    public ProgressBar getProgressBarDownload() {
        return progressBarDownload;
    }

    public static class UpdateDownloadImageIconAction implements Action1<UserVideoDownloadStatus> {
        private final AppCMSPresenter appCMSPresenter;
        private ContentDatum contentDatum;
        private final String userId;
        private View.OnClickListener addClickListener;
        private DownloadComponent mDownloadComponent;

        public UpdateDownloadImageIconAction(DownloadComponent downloadComponent, AppCMSPresenter presenter,
                                             ContentDatum contentDatum, String userId) {
            this.appCMSPresenter = presenter;
            this.contentDatum = contentDatum;
            this.userId = userId;

            this.mDownloadComponent = downloadComponent;

            addClickListener = v -> {
                if (!appCMSPresenter.isNetworkConnected()) {
                    if (!appCMSPresenter.isUserLoggedIn()) {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK, null, false,
                                appCMSPresenter::launchBlankPage,
                                null, null);
                        return;
                    }
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.NETWORK,
                            appCMSPresenter.getLocalisedStrings().getInternetConnectionMsg(),
                            true,
                            () -> appCMSPresenter.navigateToDownloadPage(appCMSPresenter.getDownloadPageId()),
                            null, null);
                    return;
                }


                if (contentDatum.isDRMEnabled()) {
                 /* int right=  DrmUtils.acquireRights(appCMSPresenter.getCurrentActivity(),
                            "https://hoichoihlsns.akamaized.net/vhoichoiindia2/Renditions/20191024/1569979625801_paap_ep_02/dash/1569979625801_paap_ep_02.mpd?hdnts=exp=1579522043~acl=/vhoichoiindia2/Renditions/20191024/1569979625801_paap_ep_02/dash/*~hmac=2e06063797acfd5f06d55f6ccf92fcfe1a7835504e7db736ec4c573c45655705",
                            "https://ca39a8ba-drm-widevine-licensing.axprod.net/AcquireLicense");*/


                    //System.out.println("right "+right);
                    //DrmUtils.downloadDasMedia(appCMSPresenter);
                    //DrmUtils.downloadLicense(contentDatum,appCMSPresenter);
                    //DrmUtils.renewLicense(contentDatum,appCMSPresenter);
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DRM_NOT_DOWNLOAD,
                            appCMSPresenter.getLocalisedStrings().getDRMMessage(),
                            false,
                            appCMSPresenter::launchBlankPage,
                            null, null);
                    return;
                }

//                contentDatum.getGist().setRentalPeriod(48);
//                contentDatum.getGist().setTransactionEndDate(1534247577000L);

                /**
                 * First check if content is TVOD type and it have pricing info. Then check it purchased info
                 * by calling getData API.IF it has purchased info than play else show rental message.Else for SVOd
                 * Content ,Run the flow same as before.
                 */
                if (!appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()&&(!contentDatum.getGist().isFree() && contentDatum.getPricing() != null &&
                        contentDatum.getPricing().getType() != null &&
                        ((contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_TVOD))
                                || contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_PPV))) ||
                                (contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                        || contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_PPV)))
                        )) ||
                        (contentDatum.getGist() != null && contentDatum.getGist().getPurchaseType() != null
                                && (contentDatum.getGist().getPurchaseType().equalsIgnoreCase("Rent")
                                || contentDatum.getGist().getPurchaseType().equalsIgnoreCase("PURCHASE")))) {


                    contentDatum.setTvodPricing(true);

                    appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), updatedContentDatum -> {

                        boolean isPlayable = true;
                        boolean isPurchased = false;


                        if (updatedContentDatum != null &&
                                updatedContentDatum.size() > 0 &&
                                updatedContentDatum.get(0).size() > 0) {
                            AppCMSTransactionDataValue objTransactionData = updatedContentDatum.get(0).get(contentDatum.getGist().getId());
                            contentDatum.getGist().setRentalPeriod(objTransactionData.getRentalPeriod());
                            String endDate = "";
                            Date date = new Date(contentDatum.getGist().getTransactionEndDate());
                            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                            endDate = df2.format(date);
                            contentDatum.getGist().setTransactionEndDate(endDate);
                        } else {
                            isPlayable = false;
                        }

                        /**
                         * If pricing type is svod+tvod or svod+tvod then check if user is subscribed or
                         * content is purchased by user ,if both conditions are false then show subscribe message.
                         */

                        if (contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_TVOD))
                                || contentDatum.getPricing().getType().equalsIgnoreCase(appCMSPresenter.getCurrentContext().getString(R.string.PURCHASE_TYPE_SVOD_PPV))) {

                            if (appCMSPresenter.isUserSubscribed() || isPurchased) {
                                isPlayable = true;
                            } else {

                                if (!appCMSPresenter.getAppCMSMain().getFeatures().getFreePreview().isFreePreview()) {
                                    appCMSPresenter.showSubscribeMessage();
                                    isPlayable = false;
                                    return;
                                }

                            }

                        }
                        /**
                         * get the transaction end date and compare with current time if end date is less than current date
                         * playable will be false
                         */
                        long expirationDate = contentDatum.getGist().getTransactionEndDate();
                        long remainingTime = CommonUtils.getTimeIntervalForEvent(expirationDate * 1000, "EEE MMM dd HH:mm:ss");


                        /**
                         * if end time is expired then show no purchase dialog message
                         */
                        if (expirationDate > 0 && remainingTime < 0) {
                            isPlayable = false;
                        }
                        if (!isPlayable) {
                            appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getCurrentContext().getString(R.string.rental_title), appCMSPresenter.getCurrentContext().getString(R.string.rental_description));

                        } else {
                            updateForDownload(contentDatum, appCMSPresenter, mDownloadComponent.imageButtonDownloadStatus, this, addClickListener);
                        }
                    }, "Video");


                }

                /**
                 * Check if event start date is in future date
                 */
                else if (contentDatum != null &&
                        contentDatum.getGist() != null && (contentDatum.getGist().getScheduleStartDate() > 0 || contentDatum.getGist().getScheduleEndDate() > 0)) {

                    /**
                     * if schedule start date is in fiture than show dialog else sstart download
                     */
                    if (appCMSPresenter.isScheduleVideoPlayable(contentDatum.getGist().getScheduleStartDate(), contentDatum.getGist().getScheduleEndDate(), null)) {
                        updateForDownload(contentDatum, appCMSPresenter, mDownloadComponent.imageButtonDownloadStatus, this, addClickListener);
                    }


                } else {
                    updateForDownload(contentDatum, appCMSPresenter, mDownloadComponent.imageButtonDownloadStatus, this, addClickListener);

                }


                /*if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed())
                    mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);*/
            };
            mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(addClickListener);

        }

        @Override
        public void call(UserVideoDownloadStatus userVideoDownloadStatus) {
            if (userVideoDownloadStatus != null) {
                switch (userVideoDownloadStatus.getDownloadStatus()) {
                    case STATUS_FAILED:
                        mDownloadComponent.getImageButtonDownloadStatus().setImageResource(android.R.drawable.stat_sys_warning);
                        mDownloadComponent.getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        // mDownloadComponent.getImageButtonDownloadStatus().setScaleType(ImageView.ScaleType.FIT_XY);
                        appCMSPresenter.setDownloadInProgress(false);
                        appCMSPresenter.startNextDownload();
                        break;

                    case STATUS_PAUSED:
                        mDownloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_queued_40x);
                        mDownloadComponent.getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);
                        break;

                    case STATUS_PENDING:
                        appCMSPresenter.setDownloadInProgress(false);
                        mDownloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_queued_40x);
                        mDownloadComponent.getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                                mDownloadComponent, appCMSPresenter, this, userId, false, 0,
                                contentDatum.getGist().getId());
                        mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);
                        break;

                    case STATUS_RUNNING:
                        appCMSPresenter.setDownloadInProgress(true);
                        mDownloadComponent.getImageButtonDownloadStatus().setVisibility(INVISIBLE);
                        mDownloadComponent.getProgressBarDownload().setVisibility(VISIBLE);
                        appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                                mDownloadComponent, appCMSPresenter, this, userId, false, 0, contentDatum.getGist().getId());
                        mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);
                        break;

                    case STATUS_SUCCESSFUL:
                        mDownloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_downloaded_40x);
                        mDownloadComponent.getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        mDownloadComponent.getProgressBarDownload().setVisibility(INVISIBLE);
                        mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);
                        if (appCMSPresenter.downloadTaskRunning(contentDatum.getGist().getId())) {
                            appCMSPresenter.setDownloadInProgress(false);
                            appCMSPresenter.cancelDownloadIconTimerTask(contentDatum.getGist().getId());
                            contentDatum.getGist().setDownloadStatus(DownloadStatus.STATUS_COMPLETED);
                            appCMSPresenter.notifyDownloadHasCompleted();
                        }
                        break;

                    case STATUS_INTERRUPTED:
                        appCMSPresenter.setDownloadInProgress(false);
                        mDownloadComponent.getImageButtonDownloadStatus().setImageResource(android.R.drawable.stat_sys_warning);
                        mDownloadComponent.getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        mDownloadComponent.getProgressBarDownload().setVisibility(INVISIBLE);
                        mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);
                        break;

                    default:
                        //Log.d(TAG, "No download Status available ");
                        break;
                }
                int fillColor = appCMSPresenter.getGeneralTextColor();
                mDownloadComponent.getImageButtonDownloadStatus().getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
            } else {
                appCMSPresenter.updateDownloadingStatus(contentDatum.getGist().getId(),
                        UpdateDownloadImageIconAction.this.mDownloadComponent, appCMSPresenter, this, userId, false,
                        0, contentDatum.getGist().getId());

                if (appCMSPresenter.isVideoDownloading(contentDatum.getGist().getId())) {
                    mDownloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_queued_40x);

                } else if (appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId())) {
                    mDownloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_downloaded_40x);

                } else {
                    mDownloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_40x);
                }
                int fillColor = appCMSPresenter.getGeneralTextColor();
                mDownloadComponent.getImageButtonDownloadStatus().getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.SRC_IN));
                mDownloadComponent.getImageButtonDownloadStatus().requestLayout();
                mDownloadComponent.getImageButtonDownloadStatus().setOnClickListener(addClickListener);
            }
        }

        public void updateDownloadImageButton(DownloadComponent downloadComponent) {
            this.mDownloadComponent = downloadComponent;
        }

        public void updateContentData(final ContentDatum data) {
            this.contentDatum = data;
        }

        public View.OnClickListener getAddClickListener() {
            return addClickListener;
        }
    }

    public static void updateForDownload(ContentDatum contentDatum, AppCMSPresenter appCMSPresenter, ImageButton imageButton, UpdateDownloadImageIconAction updateDownloadImageIconAction, View.OnClickListener addClickListener) {
        ContentTypeChecker contentTypeChecker = new ContentTypeChecker(appCMSPresenter.getCurrentActivity());
        if (appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserSubscribed() && contentDatum.getGist() != null &&
                contentDatum.getGist().getMediaType() != null &&
                contentDatum.getGist().getMediaType().toLowerCase().contains(imageButton.getContext().getString(R.string.media_type_audio).toLowerCase()) &&
                contentDatum.getGist().getContentType() != null &&
                contentDatum.getGist().getContentType().toLowerCase().contains(imageButton.getContext().getString(R.string.content_type_audio).toLowerCase())) {
            if (contentDatum.getStreamingInfo() == null ||
                    contentDatum.getStreamingInfo().getAudioAssets() == null ||
                    contentDatum.getStreamingInfo().getAudioAssets().getMp3() == null ||
                    contentDatum.getStreamingInfo().getAudioAssets().getMp3().getUrl() == null ||
                    TextUtils.isEmpty(contentDatum.getStreamingInfo().getAudioAssets().getMp3().getUrl())) {
                appCMSPresenter.getAudioDetail(updateDownloadImageIconAction.contentDatum.getGist().getId(),
                        0, null, false, false, 0,
                        new AppCMSPresenter.AppCMSAudioDetailAPIAction(false,
                                false,
                                false,
                                null,
                                updateDownloadImageIconAction.contentDatum.getGist().getId(),
                                updateDownloadImageIconAction.contentDatum.getGist().getId(),
                                null,
                                updateDownloadImageIconAction.contentDatum.getGist().getId(),
                                false, null) {
                            @Override
                            public void call(AppCMSAudioDetailResult appCMSAudioDetailResult) {
                                AppCMSPageAPI audioApiDetail = appCMSAudioDetailResult.convertToAppCMSPageAPI(updateDownloadImageIconAction.contentDatum.getGist().getId());
                                if (audioApiDetail.getModules().get(0).getContentData().get(0) != null) {
                                    appCMSPresenter.editDownload(null, audioApiDetail.getModules().get(0).getContentData().get(0), updateDownloadImageIconAction, null, null);
                                }
                            }
                        });
            } else {
                appCMSPresenter.editDownload(null, updateDownloadImageIconAction.contentDatum, updateDownloadImageIconAction, new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean)
                            imageButton.setOnClickListener(addClickListener);
                    }
                }, null);
            }
        } else {
            appCMSPresenter.videoEntitlementDownload(contentDatum, entitlementContentDatum -> {
                boolean isContentPurchased = appCMSPresenter.isUserLoggedIn() && (appCMSPresenter.getAppPreference().getUserPurchases() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getUserPurchases())
                        && (contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), entitlementContentDatum.getGist().getId())
                        || (entitlementContentDatum.getSeasonId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), entitlementContentDatum.getSeasonId()))
                        || (entitlementContentDatum.getSeriesId() != null && contentTypeChecker.isContentPurchased(appCMSPresenter.getAppPreference().getUserPurchases(), entitlementContentDatum.getSeriesId()))));
                boolean isTveContentDownload = appCMSPresenter.getAppPreference().getTVEUserId() != null && entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentTVE(entitlementContentDatum.getSubscriptionPlans());
                if ((appCMSPresenter.isUserSubscribed() /*&& checkContentType.isContentSVOD(entitlementContentDatum)*/)
                        || (appCMSPresenter.isAppAVOD() && appCMSPresenter.isDownloadEnable() && appCMSPresenter.isUserLoggedIn())
                        || isContentPurchased
                        || isTveContentDownload
                        || (entitlementContentDatum.getSubscriptionPlans() != null && (contentTypeChecker.isContentAVOD(entitlementContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentFree(entitlementContentDatum.getSubscriptionPlans())) && appCMSPresenter.isUserLoggedIn())) {
                    if (entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentTVOD(entitlementContentDatum.getSubscriptionPlans()) && !entitlementContentDatum.getSubscriptionPlans().get(0).getFeatureSetting().isDownloadAllowed()) {
                        appCMSPresenter.showDialog(VIDEO_NOT_AVAILABLE_ALERT, appCMSPresenter.getLocalisedStrings().getDownloadUnavailableMsg(), false, null, null, appCMSPresenter.getLocalisedStrings().getAlertTitle());
                    } else if (!appCMSPresenter.getAppPreference().isUserAllowedDownload() && !appCMSPresenter.isAppAVOD()&& entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentSVOD(entitlementContentDatum.getSubscriptionPlans())) {
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PLAN_UPGRADE,
                                () -> {
                                    imageButton.setOnClickListener(addClickListener);
                                }, null);
                    } else if (appCMSPresenter.isDownloadQualityScreenShowBefore()) {
                        appCMSPresenter.editDownload(entitlementContentDatum, updateDownloadImageIconAction.contentDatum, updateDownloadImageIconAction, new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (!aBoolean)
                                    imageButton.setOnClickListener(addClickListener);
                            }
                        }, null);
                    } else {
                        appCMSPresenter.showDownloadQualityScreen(entitlementContentDatum, updateDownloadImageIconAction);
                    }
                } else if (!appCMSPresenter.isUserLoggedIn()) {
                    if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled())
                        appCMSPresenter.openEntitlementScreen(contentDatum, false);
                    else {
                        AppCMSPresenter.DialogType dialogType = AppCMSPresenter.DialogType.LOGIN_AND_SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED;
                        if ((entitlementContentDatum.getSubscriptionPlans() != null && contentTypeChecker.isContentAVOD(entitlementContentDatum.getSubscriptionPlans()) || contentTypeChecker.isContentFree(entitlementContentDatum.getSubscriptionPlans()))
                                || (appCMSPresenter.isAppAVOD() && appCMSPresenter.isDownloadEnable())) {
                            dialogType = AppCMSPresenter.DialogType.LOGIN_REQUIRED;
                        }
                        appCMSPresenter.showEntitlementDialog(dialogType,
                                () -> {
                                    appCMSPresenter.setAfterLoginAction(() -> {
                                    });
                                }, null);
                    }
                } else if (!appCMSPresenter.isUserSubscribed()) {
                    if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled())
                        appCMSPresenter.openEntitlementScreen(contentDatum, false);
                    else
                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_CONTENT_REQUIRED,
                                () -> {
                                    appCMSPresenter.setAfterLoginAction(() -> {
                                    });
                                }, null);
                } else {
                    appCMSPresenter.openEntitlementScreen(entitlementContentDatum, false);
                }
            });
        }
    }


}

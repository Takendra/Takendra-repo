package com.viewlift.offlinedrm;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.offline.Download;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.DownloadComponent;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.functions.Action1;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FetchOffineDownloadStatus implements Action1<ConstraintViewCreator.OfflineVideoStatusHandler> {

    private final AppCMSPresenter appCMSPresenter;
    private final String userId;
    private final int radiusDifference;
    private final String id;
    private ContentDatum contentDatum;
    private View view;
    private View.OnClickListener addClickListener;
    private boolean isFromDownloadPage;
    private TextView videoSizeBox;
    View.OnClickListener deleteClickListener;

    public FetchOffineDownloadStatus(View view, AppCMSPresenter presenter, ContentDatum contentDatum, String userId, int radiusDifference, String id, boolean isFromDownloadPage, TextView videoSizeBox
            , View.OnClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
        this.view = view;
        this.appCMSPresenter = presenter;
        this.contentDatum = contentDatum;
        this.userId = userId;
        this.radiusDifference = radiusDifference;
        this.id = id;
        this.isFromDownloadPage=isFromDownloadPage;
        this.videoSizeBox = videoSizeBox;
        addClickListener = v -> {
            addDownloadClickListener();
        };
    }

    public FetchOffineDownloadStatus(View view, AppCMSPresenter presenter, ContentDatum contentDatum, String userId, int radiusDifference, String id) {
        this.view = view;
        this.appCMSPresenter = presenter;
        this.contentDatum = contentDatum;
        this.userId = userId;
        this.radiusDifference = radiusDifference;
        this.id = id;
        addClickListener = v -> {
            addDownloadClickListener();
        };
    }

    @Override
    public void call(ConstraintViewCreator.OfflineVideoStatusHandler offlineVideoStatusHandler) {
        ImageButton imageButton = null;
        DownloadComponent downloadComponent=null;

        if(view instanceof ImageButton){
            imageButton=(ImageButton) view;
        }
        if(view instanceof DownloadComponent){
            downloadComponent=(DownloadComponent) view;
        }

        if(offlineVideoStatusHandler!=null){
            switch (offlineVideoStatusHandler.getOfflineVideoDownloadStatus()){
                case Download.STATE_STOPPED:
                    break;
                case Download.STATE_DOWNLOADING:
                    appCMSPresenter.updateOfflineVideoStatus(offlineVideoStatusHandler.getVideoId(), view, radiusDifference, this.isFromDownloadPage, addClickListener, videoSizeBox,
                            this.deleteClickListener);
                    break;
                case Download.STATE_COMPLETED:

                    if(imageButton!=null){
                        if(isFromDownloadPage){
                            imageButton.setBackground(ContextCompat.getDrawable(appCMSPresenter.getCurrentContext(), R.drawable.ic_deleteicon));
                        }else {
                            imageButton.setImageResource(R.drawable.ic_downloaded_big);
                            imageButton.setOnClickListener(null);
                        }
                    }

                    if(downloadComponent!=null){
                        downloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_downloaded_40x);
                        downloadComponent.getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        downloadComponent.getProgressBarDownload().setVisibility(INVISIBLE);
                        downloadComponent.getImageButtonDownloadStatus().setOnClickListener(null);
                    }
                    break;
                case Download.STATE_FAILED:
                    break;
                case Download.STATE_QUEUED:
                    if(imageButton!=null){
                        imageButton.setImageResource(R.drawable.ic_download_queued);
                        imageButton.setOnClickListener(null);
                    }
                    if(downloadComponent!=null){
                        appCMSPresenter.setDownloadInProgress(false);
                        ((DownloadComponent)view).getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_queued_40x);
                        ((DownloadComponent)view).getImageButtonDownloadStatus().setVisibility(VISIBLE);
                        ((DownloadComponent)view).setOnClickListener(null);
                    }
                    break;
                default:
                    if(imageButton!=null){
                        imageButton.setImageResource(R.drawable.ic_download_big);
                    }
                    if(downloadComponent!=null){
                        downloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_40x);
                        int fillColor = appCMSPresenter.getGeneralTextColor();
                        downloadComponent.getImageButtonDownloadStatus().getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.SRC_IN));
                        downloadComponent.getImageButtonDownloadStatus().requestLayout();
                        downloadComponent.getImageButtonDownloadStatus().setOnClickListener(addClickListener);
                    }
                    break;
            }
        }else {
            if(imageButton!=null){
                imageButton.setImageResource(R.drawable.ic_download_big);
                imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                int fillColor = appCMSPresenter.getGeneralTextColor();
                imageButton.getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.MULTIPLY));
                imageButton.requestLayout();
                imageButton.setOnClickListener(addClickListener);
            }

            if(downloadComponent!=null){
                downloadComponent.getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_40x);
                int fillColor = appCMSPresenter.getGeneralTextColor();
                downloadComponent.getImageButtonDownloadStatus().getDrawable().setColorFilter(new PorterDuffColorFilter(fillColor, PorterDuff.Mode.SRC_IN));
                downloadComponent.getImageButtonDownloadStatus().requestLayout();
                downloadComponent.getImageButtonDownloadStatus().setOnClickListener(addClickListener);
            }
        }
    }

    public void addDownloadClickListener(){
        appCMSPresenter.setFetchingVideoQualities(true);
        appCMSPresenter.showDownloadProgressDialog();
        if (!appCMSPresenter.isNetworkConnected()) {
            if (!appCMSPresenter.isUserLoggedIn()) {
                appCMSPresenter.stopDownloadProgressDialog();
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

        /**
         * First check if content is TVOD type and it have pricing info. Then check it purchased info
         * by calling getData API.IF it has purchased info than play else show rental message.Else for SVOd
         * Content ,Run the flow same as before.
         */
        if ((!contentDatum.getGist().isFree() && contentDatum.getPricing() != null &&
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
                    if (appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()) == null)
                        if(view instanceof ImageButton){
                            appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getCurrentContext().getString(R.string.rental_title), appCMSPresenter.getCurrentActivity().getString(R.string.cannot_purchase_item_msg, appCMSPresenter.getAppCMSMain().getDomainName()));
                        }
                        else
                            appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getCurrentContext().getString(R.string.rental_title), appCMSPresenter.getLocalisedStrings().getCannotPurchaseItemMsg(appCMSPresenter.getAppCMSMain().getDomainName()));

                } else {
                    updateOfflineDrmDownload(contentDatum, appCMSPresenter, view, addClickListener);
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
                updateOfflineDrmDownload(contentDatum, appCMSPresenter, view, addClickListener);
            }

        } else {
            updateOfflineDrmDownload(contentDatum, appCMSPresenter, view, addClickListener);
        }

        if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.isUserSubscribed())
            view.setOnClickListener(null);
    }


    public void updateOfflineDrmDownload(ContentDatum contentDatum, AppCMSPresenter appCMSPresenter, View updateView, View.OnClickListener addClickListener) {

        if ((appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserSubscribed()) || contentDatum.isTvodPricing()
                || (!appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserLoggedIn())) {
            appCMSPresenter.editOfflineExoDownload(contentDatum, true, new Action1<Boolean>() {
                @Override
                public void call(Boolean aBoolean) {
                    if (!aBoolean)
                        updateView.setOnClickListener(addClickListener);
                }
            }, updateView, addClickListener);
        } else {
            if (appCMSPresenter.isAppSVOD()) {
                if (appCMSPresenter.isUserLoggedIn()) {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_DOWNLOAD,
                            () -> {
                                appCMSPresenter.setAfterLoginAction(() -> {
                                    //
                                });
                            }, null);
                } else {
                    appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PREMIUM_DOWNLOAD,
                            () -> {
                                appCMSPresenter.setAfterLoginAction(() -> {
                                    //
                                });
                            }, null);
                }
            } else if (!(appCMSPresenter.isAppSVOD() && appCMSPresenter.isUserLoggedIn())) {
                appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.LOGIN_REQUIRED,
                        () -> {
                            //
                        }, null);
            }
        }
    }

    public static class OfflineVideoStatusHandler{
        public int getOfflineVideoDownloadStatus() {
            return offlineVideoDownloadStatus;
        }

        public void setOfflineVideoDownloadStatus(int offlineVideoDownloadStatus) {
            this.offlineVideoDownloadStatus = offlineVideoDownloadStatus;
        }

        int offlineVideoDownloadStatus;
        String videoId;
        Download downloadObject;

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public Download getDownloadObject() {
            return downloadObject;
        }

        public void setDownloadObject(Download downloadObject) {
            this.downloadObject = downloadObject;
        }
    }

}
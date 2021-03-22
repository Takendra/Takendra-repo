package com.viewlift.offlinedrm;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.util.Log;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.downloads.UserVideoDownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.FileUtils;
import com.viewlift.views.customviews.DownloadComponent;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import rx.functions.Action1;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class OfflineDownloadTimerTask extends TimerTask {
    public String getFilmId() {
        return filmId;
    }

    final String filmId;
    final boolean isTablet;
    final Action1<UserVideoDownloadStatus> responseAction;
    final Timer timer;
    final int radiusDifference;
    public void setView(View view) {
        this.view = view;
    }
    volatile View view;
    volatile boolean cancelled;
    volatile boolean finished;
    volatile boolean running;
    public Context activeContext;
    View.OnClickListener onClickListener;
    @Inject
    AppCMSPresenter appCMSPresenter;

    public void setDeleteClickListener(View.OnClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    View.OnClickListener deleteClickListener;

    public boolean isFromDownloadPage() {
        return isFromDownloadPage;
    }

    public void setFromDownloadPage(boolean fromDownloadPage) {
        isFromDownloadPage = fromDownloadPage;
    }

    boolean isFromDownloadPage;

    public void setViewSizeBox(TextView viewSizeBox) {
        this.viewSizeBox = viewSizeBox;
    }

    TextView viewSizeBox;

    public OfflineDownloadTimerTask(String filmId,
                             Context context,
                             boolean isTablet,
                             View updateView,
                             Action1<UserVideoDownloadStatus> responseAction,
                             Timer timer,
                             int radiusDifference, boolean isFromDownloadPage, View.OnClickListener onClickListener, TextView viewSizeBox, View.OnClickListener deleteClickListener) {
        this.filmId = filmId;
        this.isTablet = isTablet;
        this.view = updateView;
        this.responseAction = responseAction;
        this.timer = timer;
        this.radiusDifference = radiusDifference;
        this.cancelled = false;
        this.finished = false;
        this.running = false;
        this.activeContext = context;
        this.isFromDownloadPage = isFromDownloadPage;
        this.onClickListener = onClickListener;
        this.viewSizeBox = viewSizeBox;
        this.deleteClickListener = deleteClickListener;
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }


    @Override
    public void run() {
        DownloadTracker downloadTracker = appCMSPresenter.getAppCMSApplication().getOfflineDRMManager().getDownloadTracker();
        Map<String, Download> listOfflineDownloads = downloadTracker.getAllOfflineDownloads();
        Download download= downloadTracker.getDowloadedVideoObject(filmId);
        //Video in Database. It may be Downloaded, In Progress or Faile

        if(download !=null){
            Download finalDownload = download;
            ImageView imageView = null;
            DownloadComponent downloadComponent=null;

            if(view instanceof ImageButton){
                imageView=(ImageButton) view;
            }
            if(view instanceof DownloadComponent){
                downloadComponent=(DownloadComponent) view;
            }

            switch (download.state){
                case Download.STATE_STOPPED:
                    running=false;
                    finished=true;
                    break;
                case Download.STATE_DOWNLOADING:
                    running=true;
                    finished=false;
                    if (appCMSPresenter.getCurrentActivity() != null) {
                        appCMSPresenter.getCurrentActivity().runOnUiThread(() -> {
                            updateOfflineDownloadView(view, finalDownload);
                        });
                    }
                    break;
                case Download.STATE_COMPLETED:
                    if (this != null) {
                        running=false;
                        finished=true;
                        ImageView finalImageView = imageView;
                        DownloadComponent finalDownloadComponent = downloadComponent;

                        if (appCMSPresenter.getCurrentActivity() != null) {
                            appCMSPresenter.getCurrentActivity().runOnUiThread(() -> {
//                            doStuff(view, finalDownload);
                                if (view instanceof ImageView) {
                                    if (isFromDownloadPage) {
                                        ((ImageView) view).setImageBitmap(null);
                                        ((ImageView) view).setBackground(ContextCompat.getDrawable(activeContext, R.drawable.ic_deleteicon));
                                        ((ImageView) view).getBackground().setTint(appCMSPresenter.getBrandPrimaryCtaColor());
                                        ((ImageView) view).getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                                        ((ImageView) view).invalidate();
                                        if (deleteClickListener != null)
                                            ((ImageView) view).setOnClickListener(deleteClickListener);
                                        try {
                                            String sizeValue = appCMSPresenter.getOfflineDownloadedFileSize(download);
                                            //String sizeValue = appCMSPresenter.getDownloadedFileSize(appCMSPresenter.getAppPreference().getofflineExoSize(filmId));
                                            if (viewSizeBox != null)
                                                viewSizeBox.setText(sizeValue);
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        ((ImageView) view).setImageResource(R.drawable.ic_downloaded_big);
                                        ((ImageView) view).setOnClickListener(null);
                                    }
                                }

                                if (view instanceof DownloadComponent) {
                                    ((DownloadComponent) view).getImageButtonDownloadStatus().setImageResource(R.drawable.ic_downloaded_40x);
                                    ((DownloadComponent) view).getImageButtonDownloadStatus().setVisibility(VISIBLE);
                                    ((DownloadComponent) view).getProgressBarDownload().setVisibility(INVISIBLE);
                                    ((DownloadComponent) view).getImageButtonDownloadStatus().setOnClickListener(null);
                                }
                            });
                        }
                        appCMSPresenter.getDownloadProgressTimerList().remove(this);
                        this.cancel();
                    }
                    break;
                case Download.STATE_FAILED:
                    try {
                        running=false;
                        finished=true;
                        downloadTracker.removeDownlodedObject(filmId);
                        appCMSPresenter.getCurrentActivity().runOnUiThread(() -> {
                            if(view instanceof ImageView){
                                ((ImageView)view).setImageResource(R.drawable.ic_download_big);
                            }

                            if(view instanceof DownloadComponent){
                                ((DownloadComponent)view).getImageButtonDownloadStatus().setImageResource(android.R.drawable.stat_sys_warning);
                                ((DownloadComponent)view).getImageButtonDownloadStatus().setVisibility(VISIBLE);
                                appCMSPresenter.setDownloadInProgress(false);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Download.STATE_QUEUED:
                    try {
                        new Handler(Looper.getMainLooper()).post(()->{
                            if(view instanceof ImageView){
                                ((ImageView)view).setImageResource(R.drawable.ic_download_queued);
                                ((ImageView)view).setOnClickListener(null);
                            }else {
                                appCMSPresenter.setDownloadInProgress(false);
                                ((DownloadComponent)view).getImageButtonDownloadStatus().setImageResource(R.drawable.ic_download_queued_40x);
                                ((DownloadComponent)view).getImageButtonDownloadStatus().setVisibility(VISIBLE);
                                ((DownloadComponent)view).setOnClickListener(null);
                                ((DownloadComponent)view).getParent().bringChildToFront(((DownloadComponent)view).getImageButtonDownloadStatus());
                                view.invalidate();
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isRunning(){
        return running;
    }

    public void updateOfflineDownloadView(View view, Download download){
        //do stuff here
        int downloadProgress = (int) (download.getPercentDownloaded());

        if(!FileUtils.isMemoryOfflineAvailable(activeContext, appCMSPresenter.getAppPreference(), download.contentLength)){
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.DOWNLOAD_FAILED, appCMSPresenter.getLocalisedStrings().getDownloadSpaceText(), false, null, null, null);
            DownloadService.sendRemoveDownload(activeContext, OfflineDownloadService.class, download.request.id, /* foreground= */ false);

            if(view instanceof ImageView){
                ((ImageView)view).setImageResource(R.drawable.ic_download_big);
                ((ImageView)view).setOnClickListener(onClickListener);
            }

            if(view instanceof DownloadComponent){
                ((DownloadComponent)view).getImageButtonDownloadStatus().setImageResource(android.R.drawable.stat_sys_warning);
                ((DownloadComponent)view).getImageButtonDownloadStatus().setVisibility(VISIBLE);
                appCMSPresenter.setDownloadInProgress(false);
            }
            this.finished=true;
            this.running=false;
            this.cancel();
            return;
        }

        if(view instanceof ImageView){
            ImageView imageView = ((ImageView)view);
            appCMSPresenter.circularImageBar(imageView, downloadProgress , radiusDifference);
            ((ImageView)view).setOnClickListener(null);
        }

        if(view instanceof DownloadComponent) {
            DownloadComponent mDownloadComponent = ((DownloadComponent)view);
            mDownloadComponent.getProgressBarDownload().setVisibility(VISIBLE);
            mDownloadComponent.getProgressBarDownload().setProgress(downloadProgress);
            appCMSPresenter.setDownloadInProgress(true);
            mDownloadComponent.getImageButtonDownloadStatus().setVisibility(INVISIBLE);
            mDownloadComponent.setOnClickListener(null);
        }
    }
}

package com.viewlift.downloadtask;

import android.app.DownloadManager;
import android.database.Cursor;
import android.database.StaleDataException;
import android.util.Log;
import android.widget.TextView;

import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.downloads.DownloadVideoRealm;
import com.viewlift.models.data.appcms.downloads.UserVideoDownloadStatus;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;

public class DownloadTimerTaskTextUpdate extends TimerTask {

    final String filmId;
    final long videoId;
    final OnRunOnUIThread onRunOnUIThread;
    final AppCMSPresenter appCMSPresenter;
    final Action1<UserVideoDownloadStatus> responseAction;
    public TextView textView;
    public volatile boolean cancelled;
    volatile boolean finished;
    public boolean running;

    public DownloadTimerTaskTextUpdate(String filmId,
                                       long videoId,
                                       OnRunOnUIThread onRunOnUIThread,
                                       AppCMSPresenter appCMSPresenter,
                                       TextView textView,
                                       Action1<UserVideoDownloadStatus> responseAction) {
        this.filmId = filmId;
        this.videoId = videoId;
        this.onRunOnUIThread = onRunOnUIThread;
        this.appCMSPresenter = appCMSPresenter;
        this.textView = textView;
        this.responseAction = responseAction;
        this.cancelled = false;
        this.finished = false;
        this.running = false;
    }

    @Override
    public boolean cancel() {
        this.cancelled = true;
        this.running = false;
        return super.cancel();
    }

    @Override
    public void run() {
        this.running = true;
        try {
            DownloadManager.Query query = new DownloadManager.Query();
            DownloadManager.Request request;
            query.setFilterById(videoId);
            Cursor c = appCMSPresenter.downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                long totalSize = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                long downloaded = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                int downloadStatus = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int reason = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_REASON));
                String url = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
                String filmId =
                        c.getString(c.getColumnIndex(DownloadManager.COLUMN_TITLE));
                Log.e(TAG, reason + " :Updating download status for: " + url + " : " + filmId);

                c.close();


                int downloadPercent = (int) (downloaded * 100.0 / totalSize + 0.5);
                //Log.d(TAG, "download progress =" + downloaded + " total-> " + totalSize + " " + downloadPercent);
                //Log.d(TAG, "download getCanonicalName " + filmId);
                   /* if(downloadPercent>=0 && downloaded<=0){
                        imageView.setBackground(ContextCompat.getDrawable(appCMSPresenter.currentContext,
                                R.drawable.ic_download_queued));
                    }
                    else*/
                if (onRunOnUIThread != null &&
                        appCMSPresenter.runUpdateDownloadIconTimer &&
                        appCMSPresenter.isUserLoggedIn()) {
                    if ((downloaded >= totalSize ||
                            downloadPercent > 100 ||
                            downloadStatus == DownloadManager.STATUS_SUCCESSFUL ||
                            downloadStatus == DownloadManager.STATUS_FAILED) && totalSize > 0) {
                        cancelled = true;
                        this.cancel();
                    }
                    onRunOnUIThread.runOnUiThread(() -> {
                        if ((downloaded >= totalSize ||
                                downloadPercent > 100 ||
                                downloadStatus == DownloadManager.STATUS_SUCCESSFUL) &&
                                totalSize > 0 &&
                                !finished) {
                            textView.setText(appCMSPresenter.getLocalisedStrings().getDownloadedLabelText().toUpperCase());
                            appCMSPresenter.appCMSUserDownloadVideoStatusCall
                                    .call(filmId, appCMSPresenter, responseAction, appCMSPresenter.getLoggedInUser());
                            finished = true;
                            Log.e(TAG, "Film download completed: " + filmId);
                        } else if (downloadStatus == DownloadManager.STATUS_RUNNING) {
                            try {
                                String download=null;
                                if(textView!=null)
                                download= String.valueOf(textView.getTag());
                                if (!cancelled&& download!=null&& download.equalsIgnoreCase("download")) {
                                    textView.setText(appCMSPresenter.getLocalisedStrings().getDownloadLowerCaseText().toUpperCase());
                                } else
                                    textView.setText(appCMSPresenter.getLocalisedStrings().getDownloadingLabelText().toUpperCase());
                            } catch (Exception e) {
                                //Log.e(TAG, "Error rendering circular image bar");
                            }
                            Log.e(TAG, "Updating film download progress: " + filmId);
                        } else if (downloadStatus == DownloadManager.STATUS_FAILED ||
                                //downloadStatus == DownloadManager.STATUS_PAUSED ||
                                downloadStatus == 403 ||
                                downloadStatus == 195) {
                            Log.e(TAG, downloadStatus + " Failed to download film: " + filmId);
                            textView.setText(appCMSPresenter.getLocalisedStrings().getDownloadLowerCaseText().toUpperCase());
                        } else if (downloadStatus == DownloadManager.STATUS_PAUSED) {
                            appCMSPresenter.appCMSUserDownloadVideoStatusCall
                                    .call(filmId, appCMSPresenter, responseAction, appCMSPresenter.getLoggedInUser());
                        }
                    });
                }
            } else {
                //noinspection ConstantConditions
                Log.e(TAG, " Downloading failed: " + c.getLong(c.getColumnIndex(DownloadManager.COLUMN_STATUS)));
                textView.setText(appCMSPresenter.getLocalisedStrings().getDownloadLowerCaseText().toUpperCase());
//                    imageView.setImageBitmap(null);
//                    imageView.setBackground(ContextCompat.getDrawable(imageView.getContext(),
//                            android.R.drawable.stat_sys_warning));
            }
        } catch (StaleDataException exception) {

        } catch (Exception exception) {
            Log.e(TAG, filmId + " Removed from top +++ " + exception.getMessage());
            updateDownloadStatusException();
        }


    }

    private void updateDownloadStatusException() {
        this.cancel();
        UserVideoDownloadStatus statusResponse = new UserVideoDownloadStatus();
        statusResponse.setDownloadStatus(DownloadStatus.STATUS_INTERRUPTED);

        if (onRunOnUIThread != null) {
            try {
                onRunOnUIThread.runOnUiThread(() -> {
                    try {
                        DownloadVideoRealm downloadVideoRealm = appCMSPresenter.realmController.getRealm()
                                .copyFromRealm(
                                        appCMSPresenter.realmController
                                                .getDownloadByIdBelongstoUser(filmId, appCMSPresenter.getLoggedInUser()));
                        downloadVideoRealm.setDownloadStatus(statusResponse.getDownloadStatus());
                        appCMSPresenter.realmController.updateDownload(downloadVideoRealm);

                        Observable.just(statusResponse)
                                .onErrorResumeNext(throwable -> Observable.empty())
                                .subscribe(responseAction);
                        //   removeDownloadedFile(filmId);
                    } catch (Exception e) {
                        //Log.e(TAG, "Error rendering circular image bar");
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    public interface OnRunOnUIThread {
        void runOnUiThread(Action0 runOnUiThreadAction);
    }
}



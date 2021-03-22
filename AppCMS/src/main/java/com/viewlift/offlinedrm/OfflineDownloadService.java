package com.viewlift.offlinedrm;

import android.app.Notification;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/** A service for downloading media. */
public class OfflineDownloadService extends DownloadService {

    private static final String CHANNEL_ID = "appcms_content_download";
    private static final int JOB_ID = 1;
    private static final int FOREGROUND_NOTIFICATION_ID = 1;

    private static int nextNotificationId = FOREGROUND_NOTIFICATION_ID + 1;

    private DownloadNotificationHelper notificationHelper;

    public OfflineDownloadService() {
        super(FOREGROUND_NOTIFICATION_ID, DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL, CHANNEL_ID, R.string.exo_download_notification_channel_name,
                /* channelDescriptionResourceId= */ R.string.offline_channel_description);
        nextNotificationId = FOREGROUND_NOTIFICATION_ID + 1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationHelper = new DownloadNotificationHelper(this, CHANNEL_ID);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        return ((AppCMSApplication) getApplication()).getOfflineDRMManager().getDownloadManager();
    }

    @Override
    protected PlatformScheduler getScheduler() {
        return Util.SDK_INT >= 21 ? new PlatformScheduler(this, JOB_ID) : null;
    }

    @Override
    protected Notification getForegroundNotification(List<Download> downloads) {
//        ((AppCMSApplication) getApplication()).getDownloadTracker();
//        ((AppCMSApplication) getApplication()).getDownloadManager().getDownloadIndex();
//        ((AppCMSApplication) getApplication()).getDownloadManager().getCurrentDownloads();
        return notificationHelper.buildProgressNotification(R.drawable.ic_download, /* contentIntent= */ null, /* message= */ null, downloads);
    }

    @Override
    protected void onDownloadChanged(Download download) {
        Notification notification;
        if (download.state == Download.STATE_COMPLETED) {
            String messageString = getString(R.string.exo_download_completed);
            try {
                OfflineVideoData offlineVideoData = deserialize(download.request.data);
                if(offlineVideoData!=null && offlineVideoData.getVideoTitle()!=null)
                    messageString = offlineVideoData.getVideoTitle();
            }catch (Exception e){ }
            notification =
                    notificationHelper.buildDownloadCompletedNotification(
                            R.drawable.ic_downloaded,
                            /* contentIntent= */ null,
                            messageString);
        } else if (download.state == Download.STATE_FAILED) {
            String messageString = getString(R.string.exo_download_failed);
            notification =
                    notificationHelper.buildDownloadFailedNotification(
                            R.drawable.ic_download,
                            /* contentIntent= */ null,
                            messageString);
        } else {
            return;
        }
        NotificationUtil.setNotification(this, nextNotificationId++, notification);
    }

    public OfflineVideoData deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (OfflineVideoData) is.readObject();
    }
}

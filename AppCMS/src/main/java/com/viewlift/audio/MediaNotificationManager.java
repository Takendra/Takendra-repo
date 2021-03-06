/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.viewlift.audio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.media.app.NotificationCompat.MediaStyle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.audio.playback.AudioPlaylistHelper;
import com.viewlift.audio.utils.ResourceHelper;
import com.viewlift.casting.CastHelper;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.activity.AppCMSPageActivity;


/**
 * Keeps track of a notification and updates it automatically for a given
 * MediaSession. Maintaining a visible notification (usually) guarantees that the music service
 * won't be killed during playback.
 */
public class MediaNotificationManager extends BroadcastReceiver {
    public static final String EXTRA_CURRENT_MEDIA_DESCRIPTION =
            "CURRENT_MEDIA_DESCRIPTION";
    public static final String ACTION_PAUSE = "com.viewlift.Audio.pause";
    public static final String ACTION_PLAY = "com.viewlift.Audio.play";
    public static final String ACTION_PREV = "com.viewlift.Audio.prev";
    public static final String ACTION_NEXT = "com.viewlift.Audio.next";
    public static final String ACTION_STOP = "com.viewlift.Audio.stop";
    public static final String ACTION_STOP_CASTING = "com.viewlift.Audio.stop_cast";
    private static final String CHANNEL_ID = "com.viewlift.Audio.MUSIC_CHANNEL_ID";
    private static final int NOTIFICATION_ID = 412;
    private static final int REQUEST_CODE = 100;
    private final MusicService mService;
    private final NotificationManager mNotificationManager;
    private final PendingIntent mPlayIntent;
    private final PendingIntent mPauseIntent;
    private final PendingIntent mPreviousIntent;
    private final PendingIntent mNextIntent;
    private final PendingIntent mStopIntent;
    private final PendingIntent mStopCastIntent;
    private final int mNotificationColor;
    private MediaSessionCompat.Token mSessionToken;
    private MediaControllerCompat mController;
    private MediaControllerCompat.TransportControls mTransportControls;
    private PlaybackStateCompat mPlaybackState;
    private MediaMetadataCompat mMetadata;
    private boolean mStarted = false;
    private final MediaControllerCompat.Callback mCb = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            mPlaybackState = state;
            if (state.getState() == PlaybackStateCompat.STATE_STOPPED ||
                    state.getState() == PlaybackStateCompat.STATE_NONE) {
                stopNotification();
            } else {
                if (mController.getMetadata() == null && AudioPlaylistHelper.getInstance().getCurrentMediaId() != null && AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId()) != null) {
                    mMetadata = AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId());//controller.getMetadata();
                } else {
                    mMetadata = mController.getMetadata();
                }
                Notification notification = createNotification();
                if (notification != null) {
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            mMetadata = metadata;
            if (mController.getMetadata() == null && AudioPlaylistHelper.getInstance().getCurrentMediaId() != null && AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId()) != null) {
                mMetadata = AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId());//controller.getMetadata();
            } else {
                mMetadata = mController.getMetadata();
            }
            Notification notification = createNotification();
            if (notification != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            try {
                updateSessionToken();
            } catch (RemoteException e) {
            }
        }
    };

    public MediaNotificationManager(MusicService service) throws RemoteException {
        mService = service;
        updateSessionToken();

        mNotificationColor = ResourceHelper.getThemeColor(mService, R.attr.colorPrimary,
                R.color.colorAccent);

        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

        String pkg = mService.getPackageName();
        mPauseIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPlayIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPreviousIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mNextIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mStopIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_STOP).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mStopCastIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_STOP_CASTING).setPackage(pkg),
                PendingIntent.FLAG_CANCEL_CURRENT);

        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        if (mNotificationManager != null)
            mNotificationManager.cancelAll();
    }


    public void notifyMedia() {
        Notification notification = createNotification();
        if (notification != null) {
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * Posts the notification and starts tracking the session to keep it
     * updated. The notification will automatically be removed if the session is
     * destroyed before {@link #stopNotification} is called.
     */
    public void startNotification() {
        if (!mStarted) {
            if (mController.getMetadata() == null && AudioPlaylistHelper.getInstance().getCurrentMediaId() != null && AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId()) != null) {
                mMetadata = AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId());//controller.getMetadata();
            } else {
                mMetadata = mController.getMetadata();
            }
//            mMetadata = mController.getMetadata();
            mPlaybackState = mController.getPlaybackState();

            // The notification must be updated after setting started to true
            Notification notification = createNotification();
            if (notification != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }
            if (notification != null) {
                mController.registerCallback(mCb);
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_NEXT);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PREV);
                filter.addAction(ACTION_STOP_CASTING);
                mService.registerReceiver(this, filter);
                if (!mService.isServiceInStartedState()) {
                    mService.setServiceInStartedState(true);
                    ContextCompat.startForegroundService(mService.getApplicationContext(), new Intent(mService.getApplicationContext(), MusicService.class));
                    mService.startForeground(NOTIFICATION_ID, notification);
                }
                mStarted = true;
            }
        } else {
            if (mController != null && (mController.getPlaybackState() == null ||
                    mController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED)) {
                mService.setServiceInStartedState(false);
                mService.stopForeground(true);
                mStarted = false;
                if (mController.getPlaybackState() != null && mController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    CommonUtils.isServiceInStartedState = true;
                }
            }
        }
    }

    /**
     * Removes the notification and stops tracking the session. If the session
     * was destroyed this has no effect.
     */
    public void stopNotification() {
        mStarted = false;
        try {
            mController.unregisterCallback(mCb);
        } catch (IllegalArgumentException ex) {
            // ignore if the receiver is not registered.
        }
        try {
            mNotificationManager.cancel(NOTIFICATION_ID);
            mService.unregisterReceiver(this);
        } catch (IllegalArgumentException ex) {
            // ignore if the receiver is not registered.
        }

        try {
            mService.setServiceInStartedState(false);
            mService.stopForeground(true);
        } catch (Exception ex) {}
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        /*if(mPlaybackState.getState() == PlaybackStateCompat.STATE_PAUSED){
            action=ACTION_PLAY;
        }else if(mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING){
            action=ACTION_PAUSE;
        }*/
        switch (action) {
            case ACTION_PAUSE:
                mTransportControls.pause();
                break;
            case ACTION_PLAY:
                mTransportControls.play();
                break;
            case ACTION_NEXT:
                mTransportControls.skipToNext();
                break;
            case ACTION_PREV:
                mTransportControls.skipToPrevious();
                break;
            case ACTION_STOP_CASTING:
                Intent i = new Intent(context, MusicService.class);
                i.setAction(MusicService.ACTION_CMD);
                i.putExtra(MusicService.CMD_NAME, MusicService.CMD_STOP_CASTING);
                Utils.startService(mService, i);
                break;
            default:
        }
    }

    /**
     * Update the state based on a change on the session token. Called either when
     * we are running for the first time or when the media session owner has destroyed the session
     * (see {@link android.media.session.MediaController.Callback#onSessionDestroyed()})
     */
    private void updateSessionToken() throws RemoteException {
        MediaSessionCompat.Token freshToken = mService.getSessionToken();
        if (mSessionToken == null && freshToken != null ||
                mSessionToken != null && !mSessionToken.equals(freshToken)) {
            if (mController != null) {
                mController.unregisterCallback(mCb);
            }
            mSessionToken = freshToken;
            if (mSessionToken != null) {
                mController = new MediaControllerCompat(mService, mSessionToken);
                mTransportControls = mController.getTransportControls();
                if (mStarted) {
                    mController.registerCallback(mCb);
                }
            }
        }
    }

    private PendingIntent createContentIntent(MediaDescriptionCompat description) {
        Intent openUI = new Intent(mService.getApplicationContext(), AppCMSPageActivity.class);
//        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        openUI.putExtra(AppCMSPageActivity.EXTRA_START_FULLSCREEN, true);
//        if (description != null) {
        openUI.putExtra(AppCMSPresenter.EXTRA_OPEN_AUDIO_PLAYER, true);
        if (mController.getMetadata() == null && AudioPlaylistHelper.getInstance().getCurrentMediaId() != null && AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId()) != null) {
            mMetadata = AudioPlaylistHelper.getInstance().getMetadata(AudioPlaylistHelper.getInstance().getCurrentMediaId());//controller.getMetadata();
        } else {
            mMetadata = mController.getMetadata();
        }
        openUI.putExtra(EXTRA_CURRENT_MEDIA_DESCRIPTION, mMetadata);

//        }
//        openUI.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        return PendingIntent.getActivity(mService.getApplicationContext(), REQUEST_CODE, openUI,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private Notification createNotification() {
        if (mMetadata == null || mPlaybackState == null) {
            return null;
        }

        MediaDescriptionCompat description = mMetadata.getDescription();

        String fetchArtUrl = null;
        Bitmap art = null;
        if (description.getIconUri() != null) {
            // This sample assumes the iconUri will be a valid URL formatted String, but
            // it can actually be any valid Android Uri formatted String.
            // async fetch the album art icon
            String artUrl = mService.getResources().getString(R.string.app_cms_image_with_resize_query,
                    description.getIconUri().toString(), 120, 120);
            art = AlbumArtCache.getInstance().getBigImage(artUrl);

            if (art == null) {
                fetchArtUrl = artUrl;
                // use a placeholder art while the remote art is being downloaded
                art = BitmapFactory.decodeResource(mService.getResources(),
                        R.mipmap.app_logo);
            }
        }

        // Notification channels are only supported on Android O+.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mService, CHANNEL_ID);
        //final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mService);

        final int playPauseButtonPosition = addActions(notificationBuilder);
        notificationBuilder
                .setStyle(new MediaStyle()
                        // show only play/pause in compact view
                        .setShowActionsInCompactView(playPauseButtonPosition)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(mStopIntent)
                        .setMediaSession(mSessionToken))
                .setDeleteIntent(mStopIntent)
                .setColor(mNotificationColor)
                .setSmallIcon(R.drawable.ic_skylight_notification_d)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentIntent(createContentIntent(description))
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setLargeIcon(art);

        if (mService != null
                && CastHelper.getInstance(mService.getApplicationContext()) != null
                && CastHelper.getInstance(mService.getApplicationContext()).getDeviceName() != null
                && !TextUtils.isEmpty(CastHelper.getInstance(mService.getApplicationContext()).getDeviceName())) {
            String castName = CastHelper.getInstance(mService.getApplicationContext()).getDeviceName();
            String castInfo = AudioPlaylistHelper.getInstance().getAppCmsPresenter().getLocalisedStrings().getCastingToText() + " "+castName;
            notificationBuilder.setSubText(castInfo);
        }

        setNotificationPlaybackState(notificationBuilder);
        if (fetchArtUrl != null) {
            fetchBitmapFromURLAsync(fetchArtUrl, notificationBuilder);
        }

        return notificationBuilder.build();
    }

    private int addActions(final NotificationCompat.Builder notificationBuilder) {

        int playPauseButtonPosition = 0;
        // If skip to previous action is enabled
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            notificationBuilder.addAction(R.drawable.notification_prev,
                    mService.getString(R.string.label_previous), mPreviousIntent);

            // If there is a "skip to previous" button, the play/pause button will
            // be the second one. We need to keep track of it, because the MediaStyle notification
            // requires to specify the index of the buttons (actions) that should be visible
            // when in compact view.
            playPauseButtonPosition = 1;
        }

        // Play or pause button, depending on the current state.
        final String label;
        final int icon;
        final PendingIntent intent;
        if (mService != null && mService.localPlayback != null && mService.localPlayback.isPlaying()) {
            label = mService.getString(R.string.label_pause);
            icon = R.drawable.notification_pause;
            intent = mPauseIntent;
        } else {
            label = mService.getString(R.string.label_play);
            icon = R.drawable.notification_play;
            intent = mPlayIntent;
        }
        notificationBuilder.addAction(new NotificationCompat.Action(icon, label, intent));

        // If skip to next action is enabled
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            notificationBuilder.addAction(R.drawable.notification_next,
                    mService.getString(R.string.label_next), mNextIntent);
        }

        return playPauseButtonPosition;
    }

    private void setNotificationPlaybackState(NotificationCompat.Builder builder) {
        if (mPlaybackState == null || mPlaybackState.getState() == PlaybackStateCompat.STATE_PAUSED) {
            //mService.stopForeground(true);
            return;
        }

        // Make sure that the notification can be dismissed by the user when we are not playing:
        builder.setOngoing(mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING);
    }


    private void fetchBitmapFromURLAsync(final String bitmapUrl,
                                         final NotificationCompat.Builder builder) {
//        AlbumArtCache.getInstance().fetch(bitmapUrl, new AlbumArtCache.FetchListener() {
//            @Override
//            public void onFetched(String artUrl, Bitmap bitmap, Bitmap icon) {
//                if (mMetadata != null && mMetadata.getDescription().getIconUri() != null &&
//                        mMetadata.getDescription().getIconUri().toString().equals(artUrl)) {
//                    // If the media is still the same, update the notification:
//                    builder.setLargeIcon(bitmap);
////                    addActions(builder);
//                    mNotificationManager.notify(NOTIFICATION_ID, builder.build());
//                }
//            }
//        });
        Glide.with(mService.getApplicationContext()).asBitmap().load(bitmapUrl).into(new SimpleTarget<Bitmap>() {

            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                if (bitmap != null) {
                    builder.setLargeIcon(bitmap);

                    mNotificationManager.notify(NOTIFICATION_ID, builder.build());

                }
            }
        });
    }

    /**
     * Creates Notification Channel. This is required in Android O+ to display notifications.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        try {
            if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel notificationChannel =
                        new NotificationChannel(CHANNEL_ID,
                                mService.getString(R.string.notification_channel),
                                NotificationManager.IMPORTANCE_LOW);

                notificationChannel.setDescription(
                        mService.getString(R.string.notification_channel_description));

                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
package com.viewlift.models.data.appcms.beacon;

import android.os.Handler;

import com.google.android.exoplayer2.util.Log;
import com.viewlift.views.customviews.VideoPlayerView;


public class BeaconRunnable implements Runnable {
    private String TAG = "BeaconRunnable";
    private BeaconPing mBeaconPing;
    private BeaconBuffer mBeaconBuffer;
    private Handler mCurrentHandler;
    private VideoPlayerView mVideoPlayerView;

    public BeaconRunnable(BeaconPing beaconPing,
                          BeaconBuffer beaconBuffer,
                          Handler currentHandler,
                          VideoPlayerView videoPlayerView) {
        this.mBeaconPing = beaconPing;
        this.mBeaconBuffer = beaconBuffer;
        this.mCurrentHandler = currentHandler;
        this.mVideoPlayerView = videoPlayerView;

    }

    @Override
    public void run() {
        if (mVideoPlayerView != null) {
            if (mBeaconPing != null) {
                mBeaconPing.updatedPlayerValues(
                        mVideoPlayerView.getPlayer().getPlayWhenReady(),
                        mVideoPlayerView.getBitrate(),
                        mVideoPlayerView.getCurrentPosition(),
                        mVideoPlayerView.getDuration(),
                        mVideoPlayerView.getPlayer().getPlaybackState(),
                        mVideoPlayerView.getVideoHeight(),
                        mVideoPlayerView.getVideoWidth());
            }
            if (mBeaconBuffer != null) {
                mBeaconBuffer.updatedPlayerValues(mVideoPlayerView.getPlayer().getPlayWhenReady(),
                        mVideoPlayerView.getBitrate(),
                        mVideoPlayerView.getCurrentPosition(),
                        mVideoPlayerView.getPlayer().getPlaybackState(),
                        mVideoPlayerView.getVideoHeight(),
                        mVideoPlayerView.getVideoWidth());
            }
        } else {

            Log.d(TAG, "updateProgress mVideoPlayerView is null ");
        }

        if (mCurrentHandler != null) {
            mCurrentHandler.postDelayed(this, 1000);
        }
    }

    public void onDestroyView() {
        mVideoPlayerView = null;
    }
}

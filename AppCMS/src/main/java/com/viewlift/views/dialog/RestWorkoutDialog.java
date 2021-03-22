package com.viewlift.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.fragments.AppCMSPlayVideoFragment;

public class RestWorkoutDialog  extends Dialog {
AppCMSPlayVideoFragment.OnClosePlayerEvent onClosePlayerEvent;
    VideoPlayerView videoPlayerView;
    Module moduleApi;
    AppCMSPresenter appCMSPresenter;
    Activity activity;
    public RestWorkoutDialog(@NonNull Activity activity, AppCMSPresenter appCMSPresenter, Module moduleApi, VideoPlayerView videoPlayerView, AppCMSPlayVideoFragment.OnClosePlayerEvent onClosePlayerEvent) {
        super(activity);
        this.appCMSPresenter=appCMSPresenter;
        this.moduleApi=moduleApi;
        this.videoPlayerView=videoPlayerView;
        this.onClosePlayerEvent=onClosePlayerEvent;
        this.activity=activity;
    }

    public void open() {
        try {
            if (getContext() != null) {
                ((AppCompatActivity) activity).getSupportActionBar().hide();
                Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.setContentView(R.layout.workout_video_end);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                activity.getWindow().setEnterTransition(null);
                dialog.show();
                Button btn_startWorkout = dialog.findViewById(R.id.btn_startWorkout);
                Button btn_replyVideo = dialog.findViewById(R.id.btn_replyVideo);
                int buttonColor = appCMSPresenter.getGeneralTextColor();
                btn_startWorkout.setBackgroundColor(activity.getResources().getColor(R.color.rest_workout_bg_color));
                btn_startWorkout.setTextColor(buttonColor);
                btn_startWorkout.setTypeface(appCMSPresenter.getBoldTypeFace());
                btn_replyVideo.setTypeface(appCMSPresenter.getBoldTypeFace());
                if (moduleApi != null && moduleApi.getMetadataMap() != null &&
                        moduleApi.getMetadataMap().getReplayVideoCTA() != null) {
                    btn_replyVideo.setText(moduleApi.getMetadataMap().getReplayVideoCTA());
                }
                if (moduleApi != null && moduleApi.getMetadataMap() != null &&
                        moduleApi.getMetadataMap().getStartWorkoutCTA() != null) {
                    btn_startWorkout.setText(moduleApi.getMetadataMap().getStartWorkoutCTA());
                }
                btn_startWorkout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        onClosePlayerEvent.onMovieFinished();
                    }
                });
                btn_replyVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        videoPlayerView.setCurrentPosition(0);
                    }
                });
            }

        } catch (Exception e) {
            Log.e("dialog error", String.valueOf(e));
        }
    }

}

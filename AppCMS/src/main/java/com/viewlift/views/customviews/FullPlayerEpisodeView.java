package com.viewlift.views.customviews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.viewlift.presenters.AppCMSPresenter;

/**
 * Created by sandeep.singh on 11/16/2017.
 */

public class FullPlayerEpisodeView extends RelativeLayout {

    private AppCMSPresenter appCMSPresenter;
    private LayoutParams lpView;

    /**
     * this is a genralize constructor . Mobile or TV can use it.
     *
     * @param context
     * @param appCMSPresenter
     */
    public FullPlayerEpisodeView(Context context,
                                 AppCMSPresenter appCMSPresenter) {
        super(context);
        this.appCMSPresenter = appCMSPresenter;
        init();
    }

    public void init() {
        lpView = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(lpView);
        setBackgroundColor(Color.BLACK);

        if (appCMSPresenter.getTrailerPlayerView().getParent() != null) {
            appCMSPresenter.episodePlayerViewParent = (ViewGroup) appCMSPresenter.getTrailerPlayerView().getParent();
            ((ViewGroup) appCMSPresenter.getTrailerPlayerView().getParent()).removeView(appCMSPresenter.getTrailerPlayerView());
        }
        appCMSPresenter.getTrailerPlayerView().setLayoutParams(lpView);
        appCMSPresenter.getTrailerPlayerView().updateFullscreenButtonState(Configuration.ORIENTATION_LANDSCAPE);
        appCMSPresenter.getTrailerPlayerView().getPlayerView().getController().hide();
        setVisibility(VISIBLE);
        appCMSPresenter.getTrailerPlayerView().setClickable(true);
        addView(appCMSPresenter.getTrailerPlayerView());
    }
}
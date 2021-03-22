package com.viewlift.tv.views.presenter;

import android.content.Context;
import androidx.leanback.widget.Presenter;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.customviews.CustomTVVideoPlayerView;
import com.viewlift.tv.views.customviews.ToggleSwitchView;

import java.util.Map;

/**
 * Created by nitin.tyagi on 11/2/2017.
 */

public class PlayerPresenter extends Presenter {

    private static int DEVICE_HEIGHT, DEVICE_WIDTH = 0;
    private final Context context;
    private final AppCMSPresenter appCmsPresenter;
    private int mHeight = -1;
    private int mWidth = -1;
    private Component component;
    private Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private ModuleList module;
    private FrameLayout parentFrameLayout;

    public PlayerPresenter(Context context, AppCMSPresenter appCMSPresenter,
                           int height, int width, Component component, Map<String, AppCMSUIKeyType> jsonValueKeyMap, ModuleList module) {
        this.context = context;
        this.appCmsPresenter = appCMSPresenter;
        this.component = component;
        mHeight = height;
        mWidth = width;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.module = module;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        DEVICE_WIDTH = parent.getContext().getResources().getDisplayMetrics().widthPixels;
        DEVICE_HEIGHT = parent.getContext().getResources().getDisplayMetrics().heightPixels;
        //Log.d("Presenter" , " CardPresenter onCreateViewHolder******");
        parentFrameLayout = new FrameLayout(parent.getContext());
        parentFrameLayout.setId(R.id.parent_layout);

        final FrameLayout frameLayout = new FrameLayout(parent.getContext());
        frameLayout.setId(R.id.video_player_id);
        if (mCustomVideoPlayerView == null) {
            mCustomVideoPlayerView = playerView(context);
            setVideoPlayerView(mCustomVideoPlayerView, true);
        }

        FrameLayout.LayoutParams parentLayoutParams;
        parentLayoutParams = new FrameLayout.LayoutParams(DEVICE_WIDTH,
                Utils.getViewYAxisAsPerScreen(context, mHeight));
        parentFrameLayout.setLayoutParams(parentLayoutParams);

        FrameLayout.LayoutParams layoutParams;
        layoutParams = new FrameLayout.LayoutParams(Utils.getViewXAxisAsPerScreen(context, mWidth),
                Utils.getViewYAxisAsPerScreen(context, mHeight));
        if (component.getLayout().getTv().getLeftMargin() != null) {
            layoutParams.setMargins(Integer.parseInt(component.getLayout().getTv().getLeftMargin()), 0, 0, 0);
        }
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        frameLayout.setLayoutParams(layoutParams);

        FrameLayout.LayoutParams playerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        mCustomVideoPlayerView.setLayoutParams(playerParams);

        if (mCustomVideoPlayerView != null && mCustomVideoPlayerView.getParent() != null) {
            ((ViewGroup) mCustomVideoPlayerView.getParent()).removeView(mCustomVideoPlayerView);
        }

        frameLayout.addView(mCustomVideoPlayerView);
        parentFrameLayout.addView(frameLayout);
        parentFrameLayout.setFocusable(true);

       // createToggleView();

        return new ViewHolder(parentFrameLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        BrowseFragmentRowData rowData = (BrowseFragmentRowData) item;
        ContentDatum contentData = rowData.contentData;

        FrameLayout cardView = (FrameLayout) viewHolder.view;
        if (shouldStartPlayer) {
            String videoId = contentData.getGist().getOriginalObjectId();
            String title = contentData.getGist().getTitle();
            if (videoId == null)
                videoId = contentData.getGist().getId();
            mCustomVideoPlayerView.setVideoUri(videoId, title,contentData,false,false);
            shouldStartPlayer = false;
        }

        mCustomVideoPlayerView.requestFocusOnLogin();
        if (jsonValueKeyMap.get(module.getView()) == AppCMSUIKeyType.PAGE_STAND_ALONE_VIDEO_PLAYER02){
            cardView.getChildAt(0).setBackground(Utils.getTrayBorder(context, Utils.getFocusBorderColor(context, appCmsPresenter), component));
            int paddig = component.getPadding();
            cardView.getChildAt(0).setPadding(paddig,paddig,paddig,paddig);
        }else{
            cardView.getChildAt(0).setBackground(Utils.getGradientTrayBorder(
                    context,
                    Utils.getFocusBorderColor(context, appCmsPresenter),
                    appCmsPresenter.getAppBackgroundColor()
                    /*Utils.getSecondaryHoverColor(context, appCmsPresenter)*/));
        }

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
      /*  try {
            if (null != viewHolder && null != viewHolder.view) {
                ((FrameLayout) viewHolder.view).removeAllViews();
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }


    public CustomTVVideoPlayerView playerView(Context context) {
        CustomTVVideoPlayerView videoPlayerView = new CustomTVVideoPlayerView(context,
                appCmsPresenter);
        videoPlayerView.init(context);
        videoPlayerView.getPlayerView().hideController();
        return videoPlayerView;
    }

    private CustomTVVideoPlayerView mCustomVideoPlayerView;
    private boolean shouldStartPlayer;
    public void setVideoPlayerView(CustomTVVideoPlayerView customVideoPlayerView , boolean shouldStartPlayer){
        this.mCustomVideoPlayerView = customVideoPlayerView;
        this.shouldStartPlayer = shouldStartPlayer;
    }

    private void createToggleView() {
        if (jsonValueKeyMap.get(module.getView()) == AppCMSUIKeyType.PAGE_STAND_ALONE_VIDEO_PLAYER02) {
            ToggleSwitchView toggleSwitchView = new ToggleSwitchView(
                    context,
                    module.getComponents().get(0),
                    jsonValueKeyMap,
                    appCmsPresenter,
                    null,
                    null);
            if (toggleSwitchView != null && toggleSwitchView.getChildAt(0) != null
                    && ((RelativeLayout) toggleSwitchView.getChildAt(0)).getChildAt(0) != null) {
                parentFrameLayout.addView(toggleSwitchView);
                View switchView = ((RelativeLayout) toggleSwitchView.getChildAt(0)).getChildAt(0);
                switchView.setFocusable(true);
                parentFrameLayout.setNextFocusLeftId(switchView.getId());
                switchView.setNextFocusRightId(R.id.parent_layout);
                parentFrameLayout.setOnFocusChangeListener((v, hasFocus) -> {
                    if(hasFocus) {
                        ((AppCmsHomeActivity) context).shouldShowLeftNavigation(false);
                    }
                });
            }

        }
    }

}

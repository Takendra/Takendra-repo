package com.viewlift.tv.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.listener.TrailerCompletedCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.LIVE_PLAYER_COMPONENT;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_VIDEO_PLAYER_VIEW_KEY_VALUE;
import static com.viewlift.tv.utility.Utils.setCustomTVVideoPlayerView;


@SuppressLint("ViewConstructor")
public class TVStandAlonePlayer02 extends TVBaseView {

    private final TVPageView pageView;
    private TVModuleView moduleList;
    private Context mContext;
    private Component mComponent;
    private Settings settings;
    private Map<String, AppCMSUIKeyType> mJsonValueKeyMap;
    private final TVViewCreator tvViewCreator;
    private final Module moduleAPI;
    AppCMSPresenter appCMSPresenter;
    private ContentDatum contentDatum;
    private String viewType;


    public TVStandAlonePlayer02(TVModuleView moduleList,
                                Context context,
                                Component component,
                                Module moduleAPI,
                                TVViewCreator tvViewCreator,
                                Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                AppCMSPresenter appCMSPresenter,
                                MetadataMap metadataMap,
                                TVPageView pageView,
                                Settings settings,
                                String viewType) {
        super(context);
        mContext = context;
        mComponent = component;
        mJsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.tvViewCreator = tvViewCreator;
        this.moduleAPI = moduleAPI;
        this.moduleList = moduleList;
        this.pageView = pageView;
        this.settings = settings;
        this.viewType = viewType;
        init();
        setFocusable(false);
    }

    @Override
    public void init() {
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();

        for (Component component : mComponent.getComponents()) {
            tvViewCreator.createComponentView(mContext,
                    component,
                    component.getLayout(),
                    moduleAPI,
                    pageView,
                    settings,
                    mJsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    viewType,
                    false);

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }

            View componentView = componentViewResult.componentView;

            if (componentView != null) {
                setViewMarginsFromComponent(component,
                        componentView,
                        component.getLayout(),
                        this,
                        mJsonValueKeyMap,
                        false,
                        false,
                        mContext.getString(R.string.app_cms_page_video_player_02_module_key));

            }
            addView(componentView);
        }
    }


    @Override
    protected Component getChildComponent(int index) {
        if (mComponent.getComponents() != null &&
                0 <= index &&
                index < mComponent.getComponents().size()) {
            return mComponent.getComponents().get(index);
        }
        return null;
    }

    @Override
    protected Layout getLayout() {
        return mComponent.getLayout();
    }

    public void refreshData(ContentDatum contentDatum){
        setContentData(contentDatum);
        for (Component component : mComponent.getComponents()) {
            AppCMSUIKeyType componentKey = mJsonValueKeyMap.get(component.getKey());
            if (componentKey == null) {
                componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
            }

            switch (componentKey) {
                case PAGE_LIVE_PLAYER_TITLE_KEY:
                    if (contentDatum!= null &&
                            contentDatum.getGist() != null &&
                            !TextUtils.isEmpty(contentDatum.getGist().getTitle())) {
                        if(pageView.findViewById(R.id.live_player_view_title) != null) {
                            ((TextView) pageView.findViewById(R.id.live_player_view_title)).setText(contentDatum.getGist().getTitle().trim());
                            if(settings != null && settings.getTabs() != null && settings.getTabs().size() >0) {
                                Glide.with(mContext)
                                        .load(settings.getTabs().get(0).getTabIcon())
                                        .into(new CustomTarget<Drawable>(48, 48) {
                                            @Override
                                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                DrawableCompat.setTint(resource, appCMSPresenter.getBrandPrimaryCtaTextColor());
                                                resource.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                                                ((TextView) pageView.findViewById(R.id.live_player_view_title)).setCompoundDrawables(resource, null, null, null);
                                                ((TextView) pageView.findViewById(R.id.live_player_view_title)).setCompoundDrawablePadding(15);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }

                                        });
                            }
                        }
                    }
                    break;

                case PAGE_LIVE_PLAYER_DESCRIPTION_KEY:
                    if (contentDatum!= null &&
                            contentDatum.getGist() != null &&
                            !TextUtils.isEmpty(contentDatum.getGist().getDescription())) {
                        if(pageView.findViewById(R.id.live_player_view_description) != null) {
                            ((TextView) pageView.findViewById(R.id.live_player_view_description)).setText(contentDatum.getGist().getDescription());
                        }
                    }
                    break;

            }
        }

        AppCMSUIKeyType componentKey = mJsonValueKeyMap.get( moduleList.getChildComponent(0).getKey());
       // appCMSPresenter.getAppAdsURL(contentDatum);

        if (componentKey != null && componentKey == LIVE_PLAYER_COMPONENT) {
            if( Utils.getCustomTVVideoPlayerView() != null) {
              //  Utils.getCustomTVVideoPlayerView().setAdsUrl(null);
                 Runnable carouselPlayerTask = null;
                 Handler handler = new Handler();
                Utils.getCustomTVVideoPlayerView().releasePreviousAdsPlayer();
                Utils.getCustomTVVideoPlayerView().setTabIsClicked(true);
//                if (Utils.getCustomTVVideoPlayerView() != null) {
//                    Utils.getCustomTVVideoPlayerView().stopPlayer();
//                    (((FrameLayout) pageView.findViewById(R.id.video_player_id))).removeAllViews();
//                    Utils.setCustomTVVideoPlayerView(null);
//
//
//                }
//                if(Utils.getCustomTVVideoPlayerView()==null){
//                    CustomTVVideoPlayerView mCustomVideoPlayerView=null;
//                    mCustomVideoPlayerView = playerView(mContext, appCMSPresenter);
//                    Utils.setCustomTVVideoPlayerView(mCustomVideoPlayerView);
//                }
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Utils.getCustomTVVideoPlayerView().setHardPause(false);
//                        if (Utils.getCustomTVVideoPlayerView().getParent() != null)
//                            ((FrameLayout) Utils.getCustomTVVideoPlayerView().getParent()).removeAllViews();
//                        (((FrameLayout) pageView.findViewById(R.id.video_player_id))).addView(Utils.getCustomTVVideoPlayerView());
//                       // Utils.getCustomTVVideoPlayerView().resumePlayer();
//                    }
//                    }, 500);
                if(contentDatum!=null&&contentDatum.getContentDetails()!=null
                        &&contentDatum.getContentDetails().getPromos()!=null&&
                        contentDatum.getContentDetails().getPromos().size()>0&&
                        contentDatum.getContentDetails().getPromos().get(0).getId() != null){
                    promId=contentDatum.getContentDetails().getPromos().get(0).getId();
                }else {
                    promId=null;
                }

                if(promId!=null) {

                    Utils.getCustomTVVideoPlayerView().setVideoUri(promId,
                            contentDatum.getGist().getTitle(), contentDatum, true,true);
                    Utils.getCustomTVVideoPlayerView().setTrailerCompletedCallback(new TrailerCompletedCallback() {
                        @Override
                        public void videoCompleted() {
                            {
                                Utils.getCustomTVVideoPlayerView().setVideoUri(contentDatum.getGist().getId(),
                                        contentDatum.getGist().getTitle(), contentDatum, false,false);
                            }
                        }

                        @Override
                        public void videoStarted() {
//                            Utils.getCustomTVVideoPlayerView().setVideoUri(promId,
//                                    contentDatum.getGist().getTitle(), contentDatum, true);
                        }
                    });
                }
                else {
                    Utils.getCustomTVVideoPlayerView().setVideoUri(contentDatum.getGist().getId(),
                            contentDatum.getGist().getTitle(), contentDatum, false,true);
                }
            }
        }

    }
    public void setTrailerCompletedCallback(TrailerCompletedCallback trailerCompletedCallback) {
        this.trailerCompletedCallback = trailerCompletedCallback;
    }
    private TrailerCompletedCallback trailerCompletedCallback;
    String promId=null;
    private void setContentData(ContentDatum contentDatum) {
        this.contentDatum = contentDatum;
    }

    public CustomTVVideoPlayerView playerView(Context context, AppCMSPresenter appCMSPresenter) {
        CustomTVVideoPlayerView videoPlayerView = new CustomTVVideoPlayerView(context,
                appCMSPresenter);
        videoPlayerView.init(context);
        videoPlayerView.getPlayerView().hideController();
        return videoPlayerView;
    }

}

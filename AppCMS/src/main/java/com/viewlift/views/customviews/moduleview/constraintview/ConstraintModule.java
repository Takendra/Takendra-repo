package com.viewlift.views.customviews.moduleview.constraintview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Gist;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.ModuleView;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewWithComponentId;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import java.util.List;
import java.util.Map;


public class ConstraintModule extends ModuleView {

    private final ModuleWithComponents moduleInfo;
    private Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;
    ConstraintLayout mConstraintLayout;
    ConstraintViewCreator constraintViewCreator;

    public ConstraintModule(Context context,
                            ModuleWithComponents moduleInfo,
                            Module moduleAPI,
                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                            AppCMSPresenter appCMSPresenter,
                            PageView pageView,
                            ConstraintViewCreator constraintViewCreator,
                            AppCMSAndroidModules appCMSAndroidModules) {
        super(context, moduleInfo, false);
        this.moduleInfo = moduleInfo;
        this.moduleAPI = moduleAPI == null ? new Module() : moduleAPI;
        this.constraintViewCreator = constraintViewCreator;
        this.appCMSAndroidModules = appCMSAndroidModules;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.context = context;
        this.pageView = pageView;
        mConstraintLayout = new ConstraintLayout(this.context);
        mConstraintLayout.setId(R.id.mConstraintLayout);

        if (this.moduleAPI != null
                && (this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_recommendation_01))
                || this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_news_recommendation_01))
                || this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_recommendPopular_01))
                || this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_continueWatching_01))
                || this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_continueWatching_02))
                || jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_TRAY_11)
                && (this.moduleAPI.getContentData() == null || this.moduleAPI.getContentData().size() == 0)) {
            //((View)this.getParent()).setLayoutParams(new LayoutParams(0,0));
           // mConstraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(0, 0));
            return;
        }
        if (this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_carousel_08))) {
            handleGenericCarouselSetting();
        }
        if (this.moduleInfo.getBlockName().equalsIgnoreCase(this.context.getString(R.string.ui_block_watchlist_01)) ||
                jsonValueKeyMap.get(this.moduleInfo.getView()) == AppCMSUIKeyType.PAGE_DOWNLOAD_SETTING_MODULE_KEY ||
                jsonValueKeyMap.get(this.moduleInfo.getView()) == AppCMSUIKeyType.PAGE_LANGUAGE_SETTING_MODULE_KEY ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_AUTHENTICATION_14 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_RESET_PASSWORD_03 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_USER_MANAGEMENT_03 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_REFERRAL_PLANS_01) {
            if (appCMSPresenter.getCurrentActivity() instanceof AppCMSPageActivity)
                setLayoutParams(new LayoutParams(BaseView.getDeviceWidth(), ((AppCMSPageActivity) appCMSPresenter.getCurrentActivity()).getModuleHeight()));
            else
                setLayoutParams(new LayoutParams(BaseView.getDeviceWidth(), BaseView.getDeviceHeight()));
        } else if (this.moduleInfo.getBlockName().equalsIgnoreCase("tray")) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else if (jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_01 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_02 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_03 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_AUTO_PLAY_04) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else if (jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_SELECTPLAN_02) {
//            setLayoutParams(new LayoutParams(BaseView.getDeviceWidth(), BaseView.getDeviceHeight() +00));
        }
        mConstraintLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        if (jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_CARD_PAYMENT_01 ||
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_PAYMENT_01) {
            setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mConstraintLayout.setBackgroundColor(Color.WHITE);
            RecyclerView view = pageView.findViewById(R.id.home_nested_scroll_view);
            if (view != null) {
                view.setBackgroundColor(Color.WHITE);
            }
        }
        try {
            setDataForShowDetailBlock06();
            setDataForShowDetailBlock07();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            initViewConstraint();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setModuleAPI(Module moduleAPI) {
        this.moduleAPI = moduleAPI;
    }

    public void initViewConstraint() {
        ViewGroup childContainer = getChildrenContainer();
        if (moduleInfo.getComponents() == null)
            return;

        int size = moduleInfo.getComponents().size();
        if (moduleAPI == null) {
            moduleAPI = new Module();
            moduleAPI.setId(moduleInfo.getId());
        }


        ContentDatum contentDatum;
        if (moduleAPI != null && (moduleAPI.getContentData() == null || moduleAPI.getContentData().size() == 0))
            contentDatum = new ContentDatum();
        else
            contentDatum = moduleAPI.getContentData().get(0);

        MetadataMap metadataMap = null;
        boolean headerView = false;
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
            metadataMap = moduleAPI.getMetadataMap();
        for (int i = 0; i < size; i++) {
            Component childComponent = moduleInfo.getComponents().get(i);
            moduleInfo.getComponents().get(i).setSettings(moduleInfo.getSettings());
            View chieldView = constraintViewCreator.createComponentView(context,
                    childComponent,
                    moduleAPI,
                    jsonValueKeyMap,
                    childComponent.getType(),
                    moduleAPI.getId(),
                    moduleInfo.getBlockName(),
                    pageView,
                    moduleInfo,
                    appCMSAndroidModules);

            if (chieldView != null) {
                if (constraintViewCreator.getComponentViewResult().addToPageView) {
                    headerView = true;
                    pageView.addToConstraintHeaderView(chieldView);
                    constraintViewCreator.setComponentViewRelativePosition(context,
                            chieldView,
                            contentDatum,
                            jsonValueKeyMap,
                            childComponent.getType(),
                            childComponent,
                            pageView.getHeaderConstraintView(), moduleInfo.getBlockName(),moduleInfo,
                            metadataMap);
                } else if (constraintViewCreator.getComponentViewResult().addBottomFloatingButton) {
                    pageView.addBottomFloatingButton((ExtendedFloatingActionButton) chieldView);
                    constraintViewCreator.getComponentViewResult().addBottomFloatingButton=false;
                } else {
                    mConstraintLayout.addView(chieldView);
                    constraintViewCreator.setComponentViewRelativePosition(context,
                            chieldView,
                            contentDatum,
                            jsonValueKeyMap,
                            childComponent.getType(),
                            childComponent,
                            mConstraintLayout, moduleInfo.getBlockName(),moduleInfo,
                            metadataMap);
                }

            }
            if (pageView != null) {
                pageView.addViewWithComponentId(new ViewWithComponentId.Builder()
                        .id(moduleAPI.getId() + childComponent.getKey())
                        .view(chieldView)
                        .build());
            }
        }

        if (headerView)
            if (pageView != null) {
                pageView.reorderConstraintHeader();
            }
        List<OnInternalEvent> presenterOnInternalEvents = appCMSPresenter.getOnInternalEvents();
        if (presenterOnInternalEvents != null) {
            for (OnInternalEvent onInternalEvent : presenterOnInternalEvents) {
                for (OnInternalEvent receiverInternalEvent : presenterOnInternalEvents) {
                    if (receiverInternalEvent != onInternalEvent) {
                        if (!TextUtils.isEmpty(onInternalEvent.getModuleId()) &&
                                onInternalEvent.getModuleId().equals(receiverInternalEvent.getModuleId())) {
                            onInternalEvent.addReceiver(receiverInternalEvent);
                        }
                    }
                }
            }
        }
        childContainer.addView(mConstraintLayout);
    }

    private void setDataForShowDetailBlock06() {
        if(jsonValueKeyMap.get(this.moduleInfo.getBlockName()) != AppCMSUIKeyType.UI_BLOCK_NEWS_RECOMENDATION_01 &&
                jsonValueKeyMap.get(this.moduleInfo.getBlockName()) != AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06 ) {
            appCMSPresenter.setPlayshareControl(false);
        }
        appCMSPresenter.setDownloadStatus("");
      //  appCMSPresenter.setShowDetailsGist(null);
        CommonUtils.setpostion(null);
        CommonUtils.setShowDetailsPage(false);
        CommonUtils.setFullDiscription(false);
        if (this.moduleInfo.getBlockName().equalsIgnoreCase(this.context.getString(R.string.ui_block_showDetail_06))||
                this.moduleInfo.getBlockName().contains(context.getString(R.string.ui_block_news_recommendation_01))) {
            appCMSPresenter.setDefaultTrailerPlay(true);
        } else {
            appCMSPresenter.setDefaultTrailerPlay(false);
        }
        if (this.moduleInfo.getBlockName().equalsIgnoreCase(this.context.getString(R.string.ui_block_showDetail_06)) &&
                appCMSPresenter.videoPlayerView != null && appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            appCMSPresenter.videoPlayerView.onDestroyNotification();
        }
//        else
//            appCMSPresenter.setPlayTrailerOnMainPlayer(true);
     if(!this.moduleInfo.getBlockName().equalsIgnoreCase(context.getString(R.string.ui_block_news_recommendation_01))) {
         if (jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06 ||
                 jsonValueKeyMap.get(this.moduleInfo.getView()) == AppCMSUIKeyType.PAGE_DOWNLOAD_SETTING_MODULE_KEY ||
                 this.moduleInfo.getBlockName().equalsIgnoreCase(this.context.getString(R.string.ui_block_autoplay_01)) ||
                 this.moduleInfo.getBlockName().equalsIgnoreCase(this.context.getString(R.string.ui_block_autoplay_04))) {
             if (appCMSPresenter.getseriesID() != null) {
                 try {
                     if (jsonValueKeyMap.get(this.moduleInfo.getBlockName()) == AppCMSUIKeyType.UI_BLOCK_SHOW_DETAIL_06 &&
                             moduleAPI != null && moduleAPI.getContentData() != null && moduleAPI.getContentData() != null &&
                             moduleAPI.getContentData().size() > 0 && moduleAPI.getContentData().get(0).getSeason() != null &&
                             moduleAPI.getContentData().get(0).getSeason().size() > 0) {
                         boolean isEpisode = false;
                         ContentDatum showdetailsContenData = null;
                         Gist showDetailsGist = null;
                         List<ContentDatum> relatedVideosAdapterData = null;
                         List<ContentDatum> seasonEpisodeAdapterData = null;
                         int selectedSeason = 0;
                         for (int seasonCount = 0; seasonCount < moduleAPI.getContentData().get(0).getSeason().size(); seasonCount++) {
                             for (int episodeCount = 0; episodeCount < moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().size(); episodeCount++) {
                                 String episdoeId = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getId();
                                 if (appCMSPresenter.getseriesID().equalsIgnoreCase(episdoeId)) {
                                     isEpisode = true;
                                     showdetailsContenData = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount);
                                     showDetailsGist = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getGist();
                                     relatedVideosAdapterData = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos();
                                     seasonEpisodeAdapterData = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes();
                                     selectedSeason = seasonCount;
                                     break;
                                 }
                                 if (!isEpisode && moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos() != null) {
                                     for (int segmentCount = 0; segmentCount < moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos().size(); segmentCount++) {
                                         String segmentId = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos().get(segmentCount).getId();
                                         if (appCMSPresenter.getseriesID().equalsIgnoreCase(segmentId)) {
                                             isEpisode = true;
                                             showdetailsContenData = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos().get(segmentCount);
                                             showDetailsGist = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos().get(segmentCount).getGist();
                                             relatedVideosAdapterData = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes().get(episodeCount).getRelatedVideos();
                                             seasonEpisodeAdapterData = moduleAPI.getContentData().get(0).getSeason().get(seasonCount).getEpisodes();
                                             selectedSeason = seasonCount;
                                             appCMSPresenter.setSegmentListSelcted(true);
                                             break;
                                         }
                                     }
                                 }
                             }
                         }
                         appCMSPresenter.setshowdetailsClickPostionDate(showdetailsContenData);
                         appCMSPresenter.setShowDetailsGist(showDetailsGist);
                         appCMSPresenter.setRelatedVideos(relatedVideosAdapterData);
                         appCMSPresenter.setSeasonEpisodeAdapterData(seasonEpisodeAdapterData);
                         appCMSPresenter.setSelectedSeason(selectedSeason);
                         appCMSPresenter.setPlayshareControl(true);
                     }


                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         } else {
             appCMSPresenter.setShowDetailsGist(null);
             appCMSPresenter.setSegmentListSelcted(false);
             appCMSPresenter.setEpisodeListSelcted(false);
             appCMSPresenter.setPlayshareControl(false);
         }
       //  appCMSPresenter.setPlayshareControl(true);
     }
    }

    private void setDataForShowDetailBlock07() {
        if (this.moduleInfo.getBlockName().equalsIgnoreCase(this.context.getString(R.string.ui_block_showDetail_07))) {
            appCMSPresenter.setRestWorkoutDialog(true);
        } else {
            appCMSPresenter.setRestWorkoutDialog(false);
        }
    }
    
    public void handleGenericCarouselSetting() {

        if (BaseView.isTablet(getContext())
                && moduleInfo.getSettings().get16x9().getTablet().isEnable()) {
            if (moduleInfo.getSettings().get16x9().getTablet().isPortrait()) {
                moduleInfo.getComponents().get(0).getComponents().get(0).getLayout().getTabletPortrait().setRatio("16:9");
                moduleInfo.getComponents().get(0).getComponents().get(1).getLayout().getTabletPortrait().setRatio("16:9");
            }
            if (moduleInfo.getSettings().get16x9().getTablet().isLandscape()
                    && BaseView.isLandscape(getContext())) {
                moduleInfo.getComponents().get(0).getComponents().get(0).getLayout().getTabletLandscape().setRatio("16:9");
                moduleInfo.getComponents().get(0).getComponents().get(1).getLayout().getTabletLandscape().setRatio("16:9");
                moduleInfo.getComponents().get(0).getComponents().get(0).getLayout().getTabletLandscape().setWidth((float) 720);
                moduleInfo.getComponents().get(0).getComponents().get(1).getLayout().getTabletLandscape().setWidth((float) 720);

                moduleInfo.getComponents().get(0).getComponents().get(2).getLayout().getTabletLandscape().setTopTo_bottomOf("carouselTitle");
                moduleInfo.getComponents().get(0).getComponents().get(2).getLayout().getTabletLandscape().setLeftTo_rightOf("carouselImage");
                moduleInfo.getComponents().get(0).getComponents().get(2).getLayout().getTabletLandscape().setBottomTo_bottomOf(null);
                moduleInfo.getComponents().get(0).getComponents().get(2).getLayout().getTabletLandscape().setLeftTo_leftOf(null);


                moduleInfo.getComponents().get(0).getComponents().get(3).getLayout().getTabletLandscape().setTopTo_topOf("parent");
                moduleInfo.getComponents().get(0).getComponents().get(3).getLayout().getTabletLandscape().setBottomTo_bottomOf("parent");
                moduleInfo.getComponents().get(0).getComponents().get(3).getLayout().getTabletLandscape().setLeftTo_rightOf("carouselImage");
                moduleInfo.getComponents().get(0).getComponents().get(3).getLayout().getTabletLandscape().setRightTo_rightOf("parent");

                moduleInfo.getComponents().get(0).getComponents().get(3).getLayout().getTabletLandscape().setBottomTo_topOf(null);
                moduleInfo.getComponents().get(0).getComponents().get(3).getLayout().getTabletLandscape().setLeftTo_leftOf(null);
                /**
                 * Bellow is commented : if we need to show info i case it is off but because
                 * of the blank screen looks bad as per user experience this ca be uncommented
                 */
               /* if (moduleInfo.getSettings().isNoInfo()) {
                    moduleInfo.getSettings().setNoInfo(false);
                }*/
            }

        } else if (moduleInfo.getSettings() != null && moduleInfo.getSettings().get16x9() != null
                && moduleInfo.getSettings().get16x9().getMobile() != null && moduleInfo.getSettings().get16x9().getMobile().isEnable()) {
            if (moduleInfo.getSettings().get16x9().getMobile().isPortrait()) {
                moduleInfo.getComponents().get(0).getComponents().get(0).getLayout().getMobile().setRatio("16:9");
                moduleInfo.getComponents().get(0).getComponents().get(1).getLayout().getMobile().setRatio("16:9");
            }
        }
    }
}

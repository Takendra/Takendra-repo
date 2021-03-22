package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.models.network.background.tasks.SearchQuery;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.customviews.moduleview.constraintview.ConstraintModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class SearchViewModule extends ModuleView {

    private static final String TAG = SearchViewModule.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final ViewCreator viewCreator;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;
    ConstraintViewCreator constraintViewCreator;
    private boolean isFromLibrary;

    private List<ContentDatum> seriesList = new ArrayList<>();
    private List<ContentDatum> audioList = new ArrayList<>();
    private List<ContentDatum> articleList = new ArrayList<>();
    private List<ContentDatum> bundleList = new ArrayList<>();

    private List<ContentDatum> photosList = new ArrayList<>();
    private List<ContentDatum> videoList = new ArrayList<>();
    private List<ContentDatum> videoPlaylist = new ArrayList<>();
    private List<ContentDatum> episodelist = new ArrayList<>();
    private List<ContentDatum> playerList = new ArrayList<>();
    private List<Module> listData;

    @SuppressWarnings("unchecked")
    public SearchViewModule(Context context,
                            ModuleWithComponents moduleInfo,
                            Module moduleAPI,
                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                            AppCMSPresenter appCMSPresenter,
                            ViewCreator viewCreator,
                            ConstraintViewCreator constraintViewCreator,
                            AppCMSAndroidModules appCMSAndroidModules,
                            PageView pageView) {
        super(context, moduleInfo, false);
        this.moduleInfo = moduleInfo;
        this.moduleAPI = moduleAPI;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.viewCreator = viewCreator;
        this.constraintViewCreator = constraintViewCreator;
        this.context = context;
        this.appCMSAndroidModules = appCMSAndroidModules;

        this.pageView = pageView;
        this.isFromLibrary = false;
        init();
    }


    public void init() {
        if (moduleAPI == null) {
            moduleAPI = new Module();
        }
        if (moduleInfo != null &&
                moduleAPI != null &&
                jsonValueKeyMap != null &&
                appCMSPresenter != null &&
                viewCreator != null) {

            ViewGroup childContainer = getChildrenContainer();
            childContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            LinearLayout topLayoutContainer = new LinearLayout(getContext());
            MarginLayoutParams topLayoutContainerLayoutParams =
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            topLayoutContainer.setLayoutParams(topLayoutContainerLayoutParams);
            topLayoutContainer.setPadding(0, 0, 0, 0);
            topLayoutContainer.setOrientation(LinearLayout.VERTICAL);


            ModuleWithComponents module = moduleInfo;
            if (module.getBlockName().equalsIgnoreCase("search01") ||
                    module.getBlockName().equalsIgnoreCase("search03")) {
                filterData(moduleAPI);
                if (listData != null && listData.size() < 1) {
                    new SearchQuery().updateMessage(appCMSPresenter, true, appCMSPresenter.getLocalisedStrings().getNoSearchResultText());
                }
            }


            if ((module.getBlockName().equalsIgnoreCase("library01") || module.getBlockName().equalsIgnoreCase("library02")) && moduleAPI != null) {
                this.isFromLibrary = true;
                filterData(moduleAPI);
                if (moduleAPI.getContentData() != null && moduleAPI.getContentData().size() < 1) {
                    SearchQuery objSearchQuery = new SearchQuery();
                    objSearchQuery.searchInstance(appCMSPresenter);
                    objSearchQuery.libraryEmptyQuery("");
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.search_layout).setVisibility(View.VISIBLE);
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.no_search).setVisibility(VISIBLE);
                    ((TextView) appCMSPresenter.getCurrentActivity().findViewById(R.id.no_search)).setText(appCMSPresenter.getLocalisedStrings().getNotPurchasedText());
                }
            }

            if (module != null && module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    ModuleWithComponents module1 = component;

                    if (jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_SEARCH_TRAY_MODULE_KEY) {

                        if (listData != null) {
                            for (int j = 0; j < listData.size(); j++) {
                                appCMSPresenter.getCurrentActivity().findViewById(R.id.search_layout).setVisibility(View.GONE);
                                drawSearchViewList(component, topLayoutContainer, listData.get(j));
                            }
                        }
                    } else if (jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_SEARCH_TRAY_02_MODULE_KEY) {
                        drawSearchGridViewList(component, topLayoutContainer, moduleAPI);
                    } else {
                        ModuleView moduleView1 = new ModuleView<>(context, module1, true);
                        addChildComponents(moduleView1, component, appCMSAndroidModules, i, moduleAPI, component);
                        topLayoutContainer.addView(moduleView1);
                    }


                }
            }
            addView(topLayoutContainer);

        }
    }

    private void drawSearchGridViewList(Component component, LinearLayout topLayoutContainer, Module module) {
        Component subComp = null;
        subComp = component.getComponents().get(0);
        Component subCompGrid = subComp;
        ModuleWithComponents module2 = subComp;
        ModuleView moduleView1 = new ModuleView<>(context, module2, true);
        addChildComponents(moduleView1, subCompGrid, appCMSAndroidModules, 0, module, subComp);
        topLayoutContainer.addView(moduleView1);
    }

    private void drawSearchViewList(Component component, LinearLayout topLayoutContainer, Module module) {
        Component subComp = null;
        if (module.getTitle().equalsIgnoreCase(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_audio_title)))) {
            subComp = component.getComponents().get(1);
        } else if (module.getTitle().equalsIgnoreCase(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_player_title)))) {
            subComp = component.getComponents().get(3);
        } else if (module.getTitle().equalsIgnoreCase(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_article_title)))) {
            subComp = component.getComponents().get(2);
        } else {
            subComp = component.getComponents().get(0);
        }

        if (subComp.isConstrainteView()) {
            ModuleWithComponents moduleInfo = subComp;

            ModuleView constraintModule = new ConstraintModule(context,
                    moduleInfo,
                    module,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    pageView,
                    constraintViewCreator,
                    appCMSAndroidModules);

            /*View constraintModule = constraintViewCreator.createComponentView(context,
                    subComp,
                    module,
                    jsonValueKeyMap,
                    appCMSPresenter,
                    subComp.getType(),
                    module.getId(),
                    moduleInfo.getBlockName(),
                    pageView,
                    moduleInfo,
                    appCMSAndroidModules);*/
            if (constraintModule != null) {
                topLayoutContainer.addView(constraintModule);
            }
        } else {
            for (int k = 0; k < subComp.getComponents().size(); k++) {
                Component subCompGrid = subComp.getComponents().get(k);
                ModuleWithComponents module2 = subCompGrid;
                ModuleView moduleView1 = new ModuleView<>(context, module2, true);
                addChildComponents(moduleView1, subCompGrid, appCMSAndroidModules, k, module, subComp);


                topLayoutContainer.addView(moduleView1);
            }
        }
    }

    private void filterData(Module appCMSSearchResults) {
        if (appCMSSearchResults != null && appCMSSearchResults.getContentData() != null && appCMSSearchResults.getContentData().size() > 0) {
            List<ContentDatum> getContentData = appCMSSearchResults.getContentData();
            listData = new ArrayList<>();
            for (int i = 0; i < getContentData.size(); i++) {

                if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && (getContentData.get(i).getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.app_cms_article_key_type).toLowerCase()))) {
                    articleList.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && (getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_series))
                        || getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_season))
                        || getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_show)))) {
                    if (getContentData.get(i).getShowDetails() != null && getContentData.get(i).getShowDetails().getStatus() != null &&
                            !getContentData.get(i).getShowDetails().getStatus().equalsIgnoreCase("closed")) {
                        seriesList.add(getContentData.get(i));
                    }
                    if (isFromLibrary)
                        seriesList.add(getContentData.get(i));
                    //seriesList.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_audio))) {
                    audioList.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_bundle))) {
                    if (isFromLibrary) {
                        bundleList.add(getContentData.get(i));
                    } else {
                        if (getContentData.get(i).getContentDetails() != null && getContentData.get(i).getContentDetails().getStatus() != null &&
                                !getContentData.get(i).getContentDetails().getStatus().equalsIgnoreCase("closed"))
                            bundleList.add(getContentData.get(i));
                    }
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && (getContentData.get(i).getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())
                        || (getContentData.get(i).getGist().getContentType().toLowerCase().equalsIgnoreCase(context.getString(R.string.app_cms_photo_image_key_type).toLowerCase())))) {
                    photosList.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_video))
                        && getContentData.get(i).getGist().getMediaType() != null
                        && getContentData.get(i).getGist().getMediaType().equalsIgnoreCase(context.getString(R.string.media_type_playlist))) {
                    videoPlaylist.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_person))
                        && getContentData.get(i).getGist().getMediaType() != null
                        && getContentData.get(i).getGist().getMediaType().equalsIgnoreCase(context.getString(R.string.media_type_player))) {
                    playerList.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_video))
                        && getContentData.get(i).getGist().getMediaType() != null
                        && getContentData.get(i).getGist().getMediaType().equalsIgnoreCase(context.getString(R.string.media_type_episode))
                        && isFromLibrary) {
                    episodelist.add(getContentData.get(i));
                } else if (getContentData.get(i).getGist() != null
                        && getContentData.get(i).getGist().getContentType() != null
                        && getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_video))
                        && isFromLibrary) {
                    videoList.add(getContentData.get(i));
                } else {
                    /**
                     * if not Episode and not PlayList then it is only Video List
                     */
                    if (getContentData.get(i).getGist() != null
                            && getContentData.get(i).getGist().getContentType() != null
                            && !getContentData.get(i).getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_event))) {
                        if (getContentData.get(i).getContentDetails() != null) {
                            // && getContentData.get(i).getContentDetails().getStatus() != null
                            // && !getContentData.get(i).getContentDetails().getStatus().equalsIgnoreCase("closed")) {
                            videoList.add(getContentData.get(i));
                        }
                    }
                }
            }
        }
        Module articleModule = null;
        Module audioModule = null;
        Module bundleModule = null;

        Module photoModule = null;
        Module seriesModule = null;
        Module videoModule = null;
        Module playerModule = null;
        Module videoPlaylistModule = null;

        if (articleList != null && articleList.size() > 0) {
            articleModule = new Module();
            articleModule.setContentData(articleList);
            articleModule.setTitle(appCMSPresenter.getLocalisedStrings().getArticleHeaderText());
            listData.add(articleModule);
        }
        if (audioList != null && audioList.size() > 0) {
            audioModule = new Module();
            audioModule.setContentData(audioList);
            audioModule.setTitle(appCMSPresenter.getLocalisedStrings().getAudioHeaderText());
            listData.add(audioModule);
        }
        if (bundleList != null && bundleList.size() > 0) {
            bundleModule = new Module();
            bundleModule.setContentData(bundleList);
            bundleModule.setTitle(appCMSPresenter.getLocalisedStrings().getBundleHeaderText());
            listData.add(bundleModule);
        }
        if (photosList != null && photosList.size() > 0) {
            photoModule = new Module();
            photoModule.setContentData(photosList);
            photoModule.setTitle(appCMSPresenter.getLocalisedStrings().getGalleryHeaderText());
            listData.add(photoModule);
        }
        if (seriesList != null && seriesList.size() > 0) {
            seriesModule = new Module();
            seriesModule.setContentData(seriesList);
//            if (this.isFromLibrary)
//                seriesModule.setTitle(appCMSPresenter.getLocalisedStrings().getBundleSeriesHeaderText());
//            else
            seriesModule.setTitle(appCMSPresenter.getLocalisedStrings().getSeriesHeaderText());
            listData.add(seriesModule);
        }
        if (episodelist != null && episodelist.size() > 0) {
            videoModule = new Module();
            videoModule.setContentData(episodelist);
            String title = appCMSPresenter.isMovieSpreeApp() ? appCMSPresenter.getLocalisedStrings().getProgramsHeaderText() : appCMSPresenter.getLocalisedStrings().getEpisodesHeaderText();
            videoModule.setTitle(title);
            listData.add(videoModule);
        }
        if (videoList != null && videoList.size() > 0) {
            videoModule = new Module();
            videoModule.setContentData(videoList);
            videoModule.setTitle(appCMSPresenter.getLocalisedStrings().getVideosHeaderText());
            listData.add(videoModule);
        }
        if (playerList != null && playerList.size() > 0) {
            playerModule = new Module();
            playerModule.setContentData(playerList);
            playerModule.setTitle(appCMSPresenter.getLocalisedStrings().getPlayersHeaderText());
            listData.add(playerModule);
        }
        if (videoPlaylist != null && videoPlaylist.size() > 0) {
            videoPlaylistModule = new Module();
            videoPlaylistModule.setContentData(videoPlaylist);
            videoPlaylistModule.setTitle(appCMSPresenter.getLocalisedStrings().getVideoPlaylistHeaderText());
            listData.add(videoPlaylistModule);
        }
    }

    private void addChildComponents(ModuleView moduleView,
                                    Component subComponent,
                                    final AppCMSAndroidModules appCMSAndroidModules,
                                    int i,
                                    Module subTrayModuleAPI,
                                    Component component) {

        ViewCreator.ComponentViewResult componentViewResult = viewCreator.getComponentViewResult();
        if (componentViewResult.onInternalEvent != null) {
            appCMSPresenter.addInternalEvent(componentViewResult.onInternalEvent);
        }
        ViewGroup subComponentChildContainer = moduleView.getChildrenContainer();
        if (componentViewResult != null && subComponentChildContainer != null) {
            viewCreator.createComponentView(getContext(),
                    subComponent,
                    subComponent.getLayout(),
                    subTrayModuleAPI,
                    appCMSAndroidModules,
                    null,
                    moduleInfo.getSettings(),
                    jsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    moduleInfo.getView(),
                    moduleInfo.getId(),
                    moduleInfo.getBlockName(),
                    moduleInfo.isConstrainteView());
            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                if (componentViewResult.addToPageView) {
                    pageView.addView(componentView);
                    moduleView.setComponentHasView(i, true);
                    setViewMarginsFromComponent(subComponent,
                            componentView,
                            subComponent.getLayout(),
                            subComponentChildContainer,
                            false,
                            jsonValueKeyMap,
                            componentViewResult.useMarginsAsPercentagesOverride,
                            componentViewResult.useWidthOfScreen,
                            component.getView(), moduleInfo.getBlockName());
                } else {
                    subComponentChildContainer.addView(componentView);
                    moduleView.setComponentHasView(i, true);
                    setViewMarginsFromComponent(subComponent,
                            componentView,
                            subComponent.getLayout(),
                            subComponentChildContainer,
                            false,
                            jsonValueKeyMap,
                            componentViewResult.useMarginsAsPercentagesOverride,
                            componentViewResult.useWidthOfScreen,
                            component.getView(), moduleInfo.getBlockName());
                }
            } else {
                moduleView.setComponentHasView(i, false);
            }
        }
    }


}

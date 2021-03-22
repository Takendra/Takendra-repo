package com.viewlift.tv.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;

import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.views.listener.TrailerCompletedCallback;

import java.util.Map;


@SuppressLint("ViewConstructor")
public class TVShowModule extends TVModuleView implements TrailerCompletedCallback {

    private static final String TAG = TVShowModule.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final TVViewCreator tvViewCreator;
    private Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    private TVPageView tvPageView;

    @SuppressWarnings("unchecked")
    public TVShowModule(Context context,
                        ModuleWithComponents moduleInfo,
                        Module moduleAPI,
                        Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                        AppCMSPresenter appCMSPresenter,
                        TVViewCreator tvViewCreator,
                        AppCMSAndroidModules appCMSAndroidModules, TVPageView tvPageView) {
        super(context, moduleInfo);
        this.moduleInfo = moduleInfo;
        this.moduleAPI = moduleAPI;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.tvViewCreator = tvViewCreator;
        this.context = context;
        this.appCMSAndroidModules = appCMSAndroidModules;
        this.tvPageView = tvPageView;
        init();
    }


    public void init() {
        if (moduleInfo != null &&
                moduleAPI != null &&
                jsonValueKeyMap != null &&
                appCMSPresenter != null &&
                tvViewCreator != null) {
            ViewGroup childContainer = getChildrenContainer();
            childContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            ModuleWithComponents module = appCMSAndroidModules.getModuleListMap().get(moduleInfo.getBlockName());
            module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "rta_carousel07.json"), ModuleList.class);

            if (module == null) {
                module = moduleInfo;
            }
            TVModuleView moduleView1 = new TVModuleView<>(context, module);


            if (module != null && module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    if (jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY ||
                            jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_EPISODE_TRAY_MODULE_KEY ||
                            jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_SEGMENT_TRAY_MODULE_KEY) {
                        for (int j = 0; j < component.getComponents().size(); j++) {
                            Component subComp = component.getComponents().get(j);
                            addChildComponents(moduleView1, subComp, appCMSAndroidModules, i, false);
                        }
                    } else {
                        addChildComponents(moduleView1, component, appCMSAndroidModules, i, false);
                    }
                }
            }

            childContainer.addView(moduleView1);
            if (module.getSettings() != null && !module.getSettings().isHidden()) {
                tvPageView.addModuleViewWithModuleId(module.getId(), moduleView1);
            }
            if (((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1() != null) {
                ((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1().setTrailerCompletedCallback(this);
            }
        }
    }

    private void addChildComponents(TVModuleView moduleView,
                                    Component subComponent,
                                    final AppCMSAndroidModules appCMSAndroidModules, int i, boolean gridElement) {
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        if (componentViewResult.onInternalEvent != null) {
            appCMSPresenter.addInternalEvent(componentViewResult.onInternalEvent);
        }
        ViewGroup subComponentChildContainer = moduleView.getChildrenContainer();
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(subComponent.getType());
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(subComponent.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (componentType == AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY) {
            gridElement = true;
        }

        if (componentViewResult != null && subComponentChildContainer != null) {
            tvViewCreator.createComponentView(getContext(),
                    subComponent,
                    subComponent.getLayout(),
                    moduleAPI,
                    null,
                    moduleInfo.getSettings(),
                    jsonValueKeyMap,
                    appCMSPresenter,
                    gridElement,
                    moduleInfo.getView(),
                    false);
            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                switch (componentKey) {
                    case PAGE_THUMBNAIL_VIDEO_IMAGE_KEY:
                    case PAGE_SHOW_IMAGE_THUMBNAIL_KEY:
                    case PAGE_PLAY_EPISODE_BUTTON_KEY:
                    case PAGE_SHOW_DETAIL_BUTTON_KEY:
                    case PAGE_CAROUSEL_ADD_TO_WATCHLIST_KEY:
                        componentView.setOnKeyListener((view, i1, keyEvent) -> {
                            int keyCode = keyEvent.getKeyCode();
                            int action = keyEvent.getAction();
                            if (action == KeyEvent.ACTION_DOWN) {
                                int position = moduleAPI.getItemPosition();

                                switch (keyCode) {
                                    case KeyEvent.KEYCODE_DPAD_LEFT:
                                        if (position <= 1)
                                            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                                        if (position > 0) {
                                            position = position - 1;
                                            moduleAPI.setItemPosition(position);
                                            tvViewCreator.refreshComponentView(
                                                    context,
                                                    TVShowModule.this,
                                                    moduleInfo,
                                                    moduleAPI,
                                                    tvViewCreator,
                                                    appCMSPresenter,
                                                    jsonValueKeyMap
                                            );

                                            if (position == 0) {
                                                (tvPageView.findViewById(R.id.left_arrow_id)).setVisibility(View.GONE);
                                            } else {
                                                (tvPageView.findViewById(R.id.left_arrow_id)).setVisibility(View.VISIBLE);
                                                (tvPageView.findViewById(R.id.right_arrow_id)).setVisibility(View.VISIBLE);
                                            }
                                            if (((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1() != null) {
                                                ((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1().setTrailerCompletedCallback(this);
                                            }
                                            Utils.startAnimation(this, context, Utils.AnimationType.LEFT);
                                            return true;
                                        }
                                        break;
                                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                                        updateComponent(position);

                                        break;

                                }
                            }
                            return false;
                        });
                        break;
                }

                addChildComponentAndView(componentView, subComponent);
                setViewMarginsFromComponent(subComponent,
                        componentView,
                        subComponent.getLayout(),
                        this,
                        jsonValueKeyMap,
                        false,
                        false,
                        subComponent.getView());

                this.getChildrenContainer().addView(componentView);
            }

        } else {
            moduleView.setComponentHasView(i, false);
        }
    }

    private void updateComponent(int position) {
        if (!((AppCmsHomeActivity) context).isNavigationExpanded()) {
            ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(false);

            if (moduleAPI.getContentData() != null /*&& position < moduleAPI.getContentData().size() - 1*/) {
                if (position == moduleAPI.getContentData().size() - 1) {
                    position = -1;
                    ((AppCmsHomeActivity) appCMSPresenter.getCurrentActivity()).shouldShowLeftNavigation(true);
                }
                position = position + 1;
                moduleAPI.setItemPosition(position);
                tvViewCreator.refreshComponentView(
                        context,
                        TVShowModule.this,
                        moduleInfo,
                        moduleAPI,
                        tvViewCreator,
                        appCMSPresenter,
                        jsonValueKeyMap
                );
                if (position == moduleAPI.getContentData().size() - 1) {
                    (tvPageView.findViewById(R.id.right_arrow_id)).setVisibility(View.GONE);
                } else if (position == 0) {
                    (tvPageView.findViewById(R.id.left_arrow_id)).setVisibility(View.GONE);
                    (tvPageView.findViewById(R.id.right_arrow_id)).setVisibility(View.VISIBLE);
                } else {
                    (tvPageView.findViewById(R.id.left_arrow_id)).setVisibility(View.VISIBLE);
                    (tvPageView.findViewById(R.id.right_arrow_id)).setVisibility(View.VISIBLE);
                }
                if (((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1() != null) {
                    ((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1().setTrailerCompletedCallback(this);
                }
                Utils.startAnimation(this, context, Utils.AnimationType.RIGHT);
            }
        }
    }

    @Override
    public void videoCompleted() {
        if ((tvPageView.findViewById(R.id.trailer_view_id)) != null) {
            (((FrameLayout) tvPageView.findViewById(R.id.trailer_view_id))).removeAllViews();
            (((FrameLayout) tvPageView.findViewById(R.id.trailer_view_id))).setBackgroundColor(Color.TRANSPARENT);
            if (((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1() != null) {
                ((AppCmsHomeActivity) context).getCustomVideoVideoPlayerView1().stopPlayer();
            }
            updateComponent(moduleAPI.getItemPosition());
        }
    }

    @Override
    public void videoStarted() {
    }
}

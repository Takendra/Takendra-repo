package com.viewlift.tv.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.google.gson.GsonBuilder;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;

import java.util.Map;


@SuppressLint("ViewConstructor")
public class TVSeasonModule extends TVModuleView {

    private static final String TAG = TVSeasonModule.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final TVViewCreator tvViewCreator;
    private Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    private TVPageView tvPageView;

    @SuppressWarnings("unchecked")
    public TVSeasonModule(Context context,
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
            module = new GsonBuilder().create().fromJson(Utils.loadJsonFromAssets(context, "rta_showdetail.json"), ModuleList.class);

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
                subComponentChildContainer.addView(componentView);
                moduleView.setComponentHasView(i, true);
                setViewMarginsFromComponent(subComponent,
                        componentView,
                        subComponent.getLayout(),
                        this,
                        jsonValueKeyMap,
                        false,
                        false,
                        subComponent.getView());
            }

        } else {
            moduleView.setComponentHasView(i, false);
        }
    }

}

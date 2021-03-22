package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.core.content.ContextCompat;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by viewlift on 6/28/17.
 */

@SuppressLint("ViewConstructor")
public class BrowseViewModule extends ModuleView {

    private static final String TAG = BrowseViewModule.class.getSimpleName();
    private static final int NUM_CHILD_VIEWS = 2;
    private final ModuleWithComponents moduleInfo;
    private Module moduleAPI;
    private Module moduleAPIRecords;
    private Module moduleAPIClasses;

    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final ViewCreator viewCreator;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;

    private List<ContentDatum> seriesList = new ArrayList<>();
    private List<ContentDatum> audioList = new ArrayList<>();
    private List<ContentDatum> articleList = new ArrayList<>();
    private List<ContentDatum> bundleList = new ArrayList<>();

    private List<ContentDatum> photosList = new ArrayList<>();
    private List<ContentDatum> videoList = new ArrayList<>();
    private List<Module> listData = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public BrowseViewModule(Context context,
                            ModuleWithComponents moduleInfo,
                            Module moduleAPI,
                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                            AppCMSPresenter appCMSPresenter,
                            ViewCreator viewCreator,
                            AppCMSAndroidModules appCMSAndroidModules, PageView pageView) {
        super(context, moduleInfo, false);
        this.moduleInfo = moduleInfo;
        this.moduleAPI = moduleAPI;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.viewCreator = viewCreator;
        this.context = context;
        this.appCMSAndroidModules = appCMSAndroidModules;

        this.pageView = pageView;
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

            if (module != null && module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    ModuleWithComponents module1 = component;

                    if (component.getType().equalsIgnoreCase("concepttray") ||
                            component.getType().equalsIgnoreCase("classtable")) {

                        ModuleView moduleView1 = new ModuleView<>(context, module1, true);
                        for (int j = 0; j < component.getComponents().size(); j++) {
                            Component subComponent = component.getComponents().get(j);

                            if (component.getType().equalsIgnoreCase("concepttray") &&
                                    moduleAPI.getConceptaData() != null && moduleAPI.getConceptaData().size() > 0) {
                                moduleAPIRecords = new Module();
                                moduleAPIRecords.setContentData(moduleAPI.getConceptaData());
                                addChildComponents(moduleView1, subComponent, appCMSAndroidModules, j, moduleAPIRecords, subComponent);
                            } else if (component.getType().equalsIgnoreCase("classtable") &&
                                    moduleAPI.getClassessData() != null && moduleAPI.getClassessData().size() > 0) {
                                moduleAPIClasses = new Module();
                                moduleAPIClasses.setContentData(moduleAPI.getClassessData());
                                if(moduleAPI.getConceptaData() == null || (moduleAPI.getConceptaData() != null && moduleAPI.getConceptaData().size() == 0)){
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.setMargins(0, (int)context.getResources().getDimension(R.dimen.browse_page_filter_top_margin), 0, 0);
                                    moduleView1.setLayoutParams(layoutParams);
                                }
                                addChildComponents(moduleView1, subComponent, appCMSAndroidModules, j, moduleAPIClasses, subComponent);
                            }
                        }
                        topLayoutContainer.addView(moduleView1);
                    } else {
                        ModuleView moduleView1 = new ModuleView<>(context, module1, true);
                        addChildComponents(moduleView1, component, appCMSAndroidModules, i, moduleAPI, component);
//                        topLayoutContainer.addView(moduleView1);
                    }

                }
            }
            addView(topLayoutContainer);
        }
    }


    private void addChildComponents(ModuleView moduleView,
                                    Component subComponent,
                                    final AppCMSAndroidModules appCMSAndroidModules, int i, Module subTrayModuleAPI, Component component) {
        ViewCreator.ComponentViewResult componentViewResult = viewCreator.getComponentViewResult();
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
        if (componentViewResult != null && subComponentChildContainer != null) {
            viewCreator.createComponentView(getContext(),
                    subComponent,
                    subComponent.getLayout(),
                    subTrayModuleAPI,
                    appCMSAndroidModules,
                    pageView,
                    moduleInfo.getSettings(),
                    jsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    moduleInfo.getView(),
                    moduleInfo.getId(), moduleInfo.getBlockName(), subComponent.isConstrainteView());
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
                    switch (componentType) {
                        case PAGE_LABEL_KEY:
                            switch (componentKey) {
                                case VIEW_FILTER_BUTTON:
                                    FrameLayout.LayoutParams filterParams = (FrameLayout.LayoutParams) componentView.getLayoutParams();
                                    filterParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                                    componentView.setLayoutParams(filterParams);
                                    break;
                            }
                            break;
                    }
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

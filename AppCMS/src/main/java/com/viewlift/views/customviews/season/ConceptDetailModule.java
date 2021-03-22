package com.viewlift.views.customviews.season;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ModuleView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;

import java.util.Map;

import static com.viewlift.Utils.loadJsonFromAssets;

/**
 * Created by viewlift on 6/28/17.
 */

@SuppressLint("ViewConstructor")
public class ConceptDetailModule extends ModuleView {

    private static final String TAG = ConceptDetailModule.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final ViewCreator viewCreator;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;
    LinearLayout linearLayout;
    TextView textView;

    @SuppressWarnings("unchecked")
    public ConceptDetailModule(Context context,
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
        if (moduleInfo != null &&
                moduleAPI != null &&
                jsonValueKeyMap != null &&
                appCMSPresenter != null &&
                viewCreator != null) {

            ViewGroup childContainer = getChildrenContainer();
            childContainer.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

            ModuleWithComponents module = appCMSAndroidModules.getModuleListMap().get(moduleInfo.getBlockName());
            AppCMSPageUI appCMSPageUI = new GsonBuilder().create().fromJson(
                    loadJsonFromAssets(context, "concept_detail.json"),
                    AppCMSPageUI.class);
            module = appCMSPageUI.getModuleList().get(0);

            if (module == null) {
                module = moduleInfo;
            } else if (moduleInfo != null) {
                module.setId(moduleInfo.getId());
                module.setSettings(moduleInfo.getSettings());
                module.setSvod(moduleInfo.isSvod());
                module.setType(moduleInfo.getType());
                module.setView(moduleInfo.getView());
                module.setBlockName(moduleInfo.getBlockName());
            }

            ModuleView moduleView = new ModuleView<>(context, module, true);
            if (module != null && module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    addChildComponents(moduleView, component, appCMSAndroidModules, i);
                }
            }
            childContainer.addView(moduleView);
            if (module.getSettings() != null && !module.getSettings().isHidden()) {
                pageView.addModuleViewWithModuleId(module.getId(), moduleView, false,jsonValueKeyMap.get(module.getBlockName()));
            }

        }
    }

    private void addChildComponents(ModuleView moduleView,
                                    Component subComponent,
                                    final AppCMSAndroidModules appCMSAndroidModules, int i) {
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
                    moduleAPI,
                    appCMSAndroidModules,
                    null,
                    moduleInfo.getSettings(),
                    jsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    moduleInfo.getView(),
                    moduleInfo.getId(), moduleInfo.getBlockName(), moduleInfo.isConstrainteView());
            View componentView = componentViewResult.componentView;
            if (componentView != null) {
                if (linearLayout != null) {
                if (componentKey == AppCMSUIKeyType.PAGE_EXPENDABLE_LIST ||
                        componentKey == AppCMSUIKeyType.PAGE_RECENT_CLASS_TITLE_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_COLLECTIONGRID_KEY ||
                        componentKey == AppCMSUIKeyType.PAGE_SEPARATOR_VIEW_KEY) {

                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        linearLayout.addView(componentView);
                    }
                } else {
                    subComponentChildContainer.addView(componentView);
                    moduleView.setComponentHasView(i, true);
                    moduleView.setViewMarginsFromComponent(subComponent,
                            componentView,
                            subComponent.getLayout(),
                            subComponentChildContainer,
                            false,
                            jsonValueKeyMap,
                            componentViewResult.useMarginsAsPercentagesOverride,
                            componentViewResult.useWidthOfScreen,
                            context.getString(R.string.app_cms_page_show_details_04_module_key), moduleInfo.getBlockName());
                }
            }
            switch (componentType) {
                case PAGE_LINEAR_LAYOUT_KEY:
                    linearLayout = (LinearLayout) componentView;
                    break;
            }
        } else {
            moduleView.setComponentHasView(i, false);
        }
    }

}

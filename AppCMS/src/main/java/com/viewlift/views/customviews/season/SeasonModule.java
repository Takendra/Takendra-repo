package com.viewlift.views.customviews.season;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;

import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.ModuleView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.viewlift.Utils.loadJsonFromAssets;

/**
 * Created by viewlift on 6/28/17.
 */

@SuppressLint("ViewConstructor")
public class SeasonModule extends ModuleView {

    private static final String TAG = SeasonModule.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final ViewCreator viewCreator;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;
    TabLayout seasonTab;
    List<Season_> seasonList;

    @SuppressWarnings("unchecked")
    public SeasonModule(Context context,
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
        seasonList = new ArrayList<>();
        seasonList.addAll(moduleAPI.getContentData().get(0).getSeason());
        addShowDetailsToEpisode(seasonList);
        Collections.reverse(seasonList);
        init();
    }


    void addShowDetailsToEpisode(List<Season_> seasonList) {
        if (seasonList != null && seasonList.size() != 0) {
            for (int i = 0; i < seasonList.size(); i++) {
                for (int j = 0; j < seasonList.get(i).getEpisodes().size(); j++) {
                    ContentDatum episode = seasonList.get(i).getEpisodes().get(j);
                    episode.getGist().setEpisodeNum(j + 1 + "");
                    episode.getGist().setSeasonNum(i + 1 + "");
                    episode.getGist().setShowName(moduleAPI.getContentData().get(0).getGist().getTitle());
                }
            }
        }
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
            AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                    loadJsonFromAssets(context, "showdetail01.json"),
                    AppCMSPageUI.class);
            module = appCMSPageUI1.getModuleList().get(1);

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
            ModuleView moduleView1 = new ModuleView<>(context, module, true);


            if (module != null && module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    if (jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY) {
                        for (int j = 0; j < component.getComponents().size(); j++) {
                            Component subComp = component.getComponents().get(j);
                            addChildComponents(moduleView1, subComp, appCMSAndroidModules, i);
                        }
                    }
                }
            }

//            seasonTab.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
//            seasonTab.setSelectedTabIndicatorHeight((int) (3 * getResources().getDisplayMetrics().density));


            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_selected}, // not selected
                    new int[]{android.R.attr.state_selected}  // selected
            };

            int[] colors = new int[]{
                    Color.WHITE,
                    appCMSPresenter.getBrandPrimaryCtaColor()
            };

            ColorStateList myList = new ColorStateList(states, colors);
            if (moduleAPI != null && moduleAPI.getContentData() != null &&
                    moduleAPI.getContentData().size() > 0 &&
                    moduleAPI.getContentData().get(0).getSeason() != null) {

//                if (moduleAPI.getContentData().get(0).getSeason().size() > 2) {
                seasonTab.setTabMode(TabLayout.MODE_SCROLLABLE);
//                } else {
//                    seasonTab.setTabMode(TabLayout.MODE_FIXED);
//                }
                seasonTab.setTabGravity(Gravity.LEFT);
                seasonTab.setId(R.id.seasonTabs);
                for (int i = 0; i < seasonList.size(); i++) {
                    TextView tab = new TextView(context);
                    tab.setText(seasonList.get(i).getTitle());
                    if (BaseView.isTablet(context)) {
                        tab.setTextSize(20f);
                    } else {
                        tab.setTextSize(18f);
                    }
                    tab.setTextColor(myList);
                    tab.setSingleLine(false);
                    tab.setEllipsize(TextUtils.TruncateAt.END);
                    tab.setLines(1);
                    TabLayout.Tab firstTab = seasonTab.newTab();
                    seasonTab.addTab(firstTab);
                    seasonTab.getTabAt(i).setCustomView(tab);
                }
                seasonTab.setSelectedTabIndicatorHeight(0);
                seasonTab.setTabTextColors(Color.parseColor("#ffffff"),
                        appCMSPresenter.getBrandPrimaryCtaColor());
            }

            reduceMarginsInTabs(seasonTab, 10);
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (seasonTab != null && appCMSPresenter.getSelectedSeason() != 0) {
                                seasonTab.setScrollX(seasonTab.getWidth());
                                seasonTab.getTabAt(appCMSPresenter.getSelectedSeason()).select();
                            }
                        }
                    }, 1);


            List<ContentDatum> adapterData = seasonList.get(appCMSPresenter.getSelectedSeason()).getEpisodes();
            SeasonTabSelectorBus.instanceOf().setTab(adapterData);
            seasonTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    appCMSPresenter.setSelectedSeason(position);
                    List<ContentDatum> adapterData = seasonList.get(position).getEpisodes();
                    SeasonTabSelectorBus.instanceOf().setTab(adapterData);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            /*View root = seasonTab.getChildAt(0);
            if (root instanceof LinearLayout) {
                ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(getResources().getColor(android.R.color.darker_gray));
                drawable.setSize(2, 1);
                ((LinearLayout) root).setDividerPadding(10);
                ((LinearLayout) root).setDividerDrawable(drawable);
            }*/
            childContainer.addView(moduleView1);
            if (module.getSettings() != null && !module.getSettings().isHidden()) {
                pageView.addModuleViewWithModuleId(module.getId(), moduleView1, false, jsonValueKeyMap.get(module.getBlockName()));
            }

        }
    }

    private void reduceMarginsInTabs(TabLayout tabLayout, int marginOffset) {

        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            for (int i = 0; i < ((ViewGroup) tabStrip).getChildCount(); i++) {
                View tabView = tabStripGroup.getChildAt(i);
                if (tabView.getLayoutParams() instanceof MarginLayoutParams) {
                    ((MarginLayoutParams) tabView.getLayoutParams()).leftMargin = marginOffset;
                    ((MarginLayoutParams) tabView.getLayoutParams()).rightMargin = marginOffset;
                }
            }
            tabLayout.requestLayout();
        }
    }


    Component recyclerViewComponent;

    private void addChildComponents(ModuleView moduleView,
                                    Component subComponent,
                                    final AppCMSAndroidModules appCMSAndroidModules, int i) {
        ViewCreator.ComponentViewResult componentViewResult = viewCreator.getComponentViewResult();
        if (componentViewResult.onInternalEvent != null) {
            appCMSPresenter.addInternalEvent(componentViewResult.onInternalEvent);
        }
        ViewGroup subComponentChildContainer = moduleView.getChildrenContainer();
        float parentYAxis = 2 * getYAxis(getContext(), subComponent.getLayout(), 0.0f);
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(subComponent.getType());
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(subComponent.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
       /* if (componentType == AppCMSUIKeyType.PAGE_COLLECTIONGRID_KEY) {
            recyclerViewComponent = subComponent;
            return;
        }*/

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

                float componentYAxis = getYAxis(getContext(),
                        subComponent.getLayout(),
                        0.0f);
                if (!subComponent.isyAxisSetManually()) {
                    setYAxis(getContext(),
                            subComponent.getLayout(),
                            componentYAxis - parentYAxis);
                    subComponent.setyAxisSetManually(true);
                }
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
                        context.getString(R.string.app_cms_page_season_tray_module_key), moduleInfo.getBlockName());
            }
            switch (componentType) {
                case PAGE_TABLAYOUT_KEY:
                    seasonTab = (TabLayout) componentView;
                    break;
            }
        } else {
            moduleView.setComponentHasView(i, false);
        }
    }

}

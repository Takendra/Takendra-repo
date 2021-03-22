package com.viewlift.views.customviews.season;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ExpandableItem;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.ExpandableGridAdapter;
import com.viewlift.views.adapters.RecentClassesAdapter;
import com.viewlift.views.customviews.AppCMSBackgroundImageView;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.ModuleView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.customviews.listdecorator.SeparatorDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.viewlift.Utils.loadJsonFromAssets;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_PHOTO_PLAYER_IMAGE;

/**
 * Created by viewlift on 6/28/17.
 */

@SuppressLint("ViewConstructor")
public class PersonDetailModule extends ModuleView {

    private static final String TAG = PersonDetailModule.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final ViewCreator viewCreator;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;
    View containerView, carouselSpace, carouselDivider, recyclerviewDivider, recentDivider;
    TextView recentClassesTitle;
    RecyclerView recyclerview, list;
    ConstraintLayout constraintLayout;
    RelativeLayout rootView;
    ConstraintViewCreator constraintViewCreator;

    @SuppressWarnings("unchecked")
    public PersonDetailModule(Context context,
                              ModuleWithComponents moduleInfo,
                              Module moduleAPI,
                              Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                              AppCMSPresenter appCMSPresenter,
                              ViewCreator viewCreator,
                              AppCMSAndroidModules appCMSAndroidModules, PageView pageView,ConstraintViewCreator constraintViewCreator) {
        super(context, moduleInfo, false);
        this.moduleInfo = moduleInfo;
        this.moduleAPI = moduleAPI;
        this.constraintViewCreator = constraintViewCreator;

        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.viewCreator = viewCreator;
        this.context = context;
        this.appCMSAndroidModules = appCMSAndroidModules;
        this.pageView = pageView;

//        containerView = LayoutInflater.from(context).inflate(R.layout.person_detail_module, this);

        constraintLayout = new ConstraintLayout(this.context);
        constraintLayout.setId(R.id.mConstraintLayout);

        constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView = new RelativeLayout(this.context);
        initViewConstraint();
//        initView();
//        init();
    }

    public void initViewConstraint() {

        ViewGroup childContainer = getChildrenContainer();
        MetadataMap metadataMap = null;
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
            metadataMap = moduleAPI.getMetadataMap();
        int size = moduleInfo.getComponents().size();
        for (int i = 0; i < size; i++) {
            Component childComponent = moduleInfo.getComponents().get(i);
            View chieldView = constraintViewCreator.createComponentView(context,
                    childComponent,
                    moduleAPI,
                    jsonValueKeyMap,
                    childComponent.getType(),
                    moduleAPI.getId(),moduleInfo.getBlockName(),pageView,moduleInfo,appCMSAndroidModules);
            if (chieldView != null) {
                constraintLayout.addView(chieldView);

                constraintViewCreator.setComponentViewRelativePosition(context,
                        chieldView,
                        moduleAPI.getContentData().get(0),
                        jsonValueKeyMap,
                        childComponent.getType(),
                        childComponent,
                        constraintLayout,moduleInfo.getBlockName(),moduleInfo,
                        metadataMap);

            }


        }
        childContainer.addView(constraintLayout);
    }

    public void initView() {
        constraintLayout = containerView.findViewById(R.id.constraint_layout);
        carouselSpace = containerView.findViewById(R.id.carouselSpace);
        carouselDivider = containerView.findViewById(R.id.carouselDivider);
        recyclerview = containerView.findViewById(R.id.recyclerview);
        recyclerviewDivider = containerView.findViewById(R.id.recyclerviewDivider);
        recentClassesTitle = containerView.findViewById(R.id.recent_classes_title);
        recentDivider = containerView.findViewById(R.id.recentDivider);
        list = containerView.findViewById(R.id.list);

        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                ConstraintLayout.LayoutParams carouselSpaceParams = new ConstraintLayout.LayoutParams(dpToPx(471), dpToPx(265));
                carouselSpace.setLayoutParams(carouselSpaceParams);

                ConstraintLayout.LayoutParams carouselDividerParams = new ConstraintLayout.LayoutParams(dpToPx(471), carouselDivider.getLayoutParams().height);
                carouselDivider.setLayoutParams(carouselDividerParams);

                ConstraintLayout.LayoutParams recyclerviewParams = new ConstraintLayout.LayoutParams(dpToPx(471), ViewGroup.LayoutParams.WRAP_CONTENT);
                recyclerview.setLayoutParams(recyclerviewParams);

                ConstraintLayout.LayoutParams recyclerviewDividerParams = new ConstraintLayout.LayoutParams(dpToPx(490), recyclerviewDivider.getLayoutParams().height);
                recyclerviewDivider.setLayoutParams(recyclerviewDividerParams);

                ConstraintLayout.LayoutParams recentClassesTitleParams = new ConstraintLayout.LayoutParams(dpToPx(500), recentClassesTitle.getLayoutParams().height);
                recentClassesTitle.setLayoutParams(recentClassesTitleParams);

                ConstraintLayout.LayoutParams recentDividerParams = new ConstraintLayout.LayoutParams(dpToPx(490), carouselDivider.getLayoutParams().height);
                recentDivider.setLayoutParams(recentDividerParams);

                ConstraintLayout.LayoutParams listParams = new ConstraintLayout.LayoutParams(dpToPx(500), list.getLayoutParams().height);
                list.setLayoutParams(listParams);
            } else {
                ConstraintLayout.LayoutParams carouselSpaceParams = new ConstraintLayout.LayoutParams(dpToPx(344), dpToPx(255));
                carouselSpace.setLayoutParams(carouselSpaceParams);

                ConstraintLayout.LayoutParams carouselDividerParams = new ConstraintLayout.LayoutParams(dpToPx(344), carouselDivider.getLayoutParams().height);
                carouselDivider.setLayoutParams(carouselDividerParams);

                ConstraintLayout.LayoutParams recyclerviewParams = new ConstraintLayout.LayoutParams(dpToPx(344), ViewGroup.LayoutParams.WRAP_CONTENT);
                recyclerview.setLayoutParams(recyclerviewParams);

                ConstraintLayout.LayoutParams recyclerviewDividerParams = new ConstraintLayout.LayoutParams(dpToPx(365), recyclerviewDivider.getLayoutParams().height);
                recyclerviewDivider.setLayoutParams(recyclerviewDividerParams);

                ConstraintLayout.LayoutParams recentClassesTitleParams = new ConstraintLayout.LayoutParams(dpToPx(375), recentClassesTitle.getLayoutParams().height);
                recentClassesTitle.setLayoutParams(recentClassesTitleParams);

                ConstraintLayout.LayoutParams recentDividerParams = new ConstraintLayout.LayoutParams(dpToPx(365), carouselDivider.getLayoutParams().height);
                recentDivider.setLayoutParams(recentDividerParams);

                ConstraintLayout.LayoutParams listParams = new ConstraintLayout.LayoutParams(dpToPx(375), list.getLayoutParams().height);
                list.setLayoutParams(listParams);
            }
            setConstraints();
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void setConstraints() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(R.id.carouselSpace, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(10));
        constraintSet.connect(R.id.carouselSpace, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dpToPx(10));
        constraintSet.connect(R.id.carouselSpace, ConstraintSet.BOTTOM, R.id.carouselDivider, ConstraintSet.TOP, 0);

        constraintSet.connect(R.id.carouselDivider, ConstraintSet.TOP, R.id.carouselSpace, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.carouselDivider, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dpToPx(10));
        constraintSet.connect(R.id.carouselDivider, ConstraintSet.BOTTOM, R.id.recyclerview, ConstraintSet.TOP, 0);

        constraintSet.connect(R.id.recyclerview, ConstraintSet.TOP, R.id.carouselDivider, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.recyclerview, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, dpToPx(10));

        constraintSet.connect(R.id.recyclerviewDivider, ConstraintSet.TOP, R.id.carouselSpace, ConstraintSet.TOP, 0);
        constraintSet.connect(R.id.recyclerviewDivider, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dpToPx(10));
        constraintSet.connect(R.id.recyclerviewDivider, ConstraintSet.BOTTOM, R.id.recent_classes_title, ConstraintSet.TOP, 0);

        constraintSet.connect(R.id.recent_classes_title, ConstraintSet.TOP, R.id.recyclerviewDivider, ConstraintSet.BOTTOM, dpToPx(10));
        constraintSet.connect(R.id.recent_classes_title, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dpToPx(10));
        constraintSet.connect(R.id.recent_classes_title, ConstraintSet.BOTTOM, R.id.recentDivider, ConstraintSet.TOP, dpToPx(10));

        constraintSet.connect(R.id.recentDivider, ConstraintSet.TOP, R.id.recent_classes_title, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.recentDivider, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dpToPx(10));
        constraintSet.connect(R.id.recentDivider, ConstraintSet.BOTTOM, R.id.list, ConstraintSet.TOP, 0);

        constraintSet.connect(R.id.list, ConstraintSet.TOP, R.id.recentDivider, ConstraintSet.BOTTOM, 0);
        constraintSet.connect(R.id.list, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dpToPx(10));

        constraintSet.applyTo(constraintLayout);
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
                    loadJsonFromAssets(context, "brand_detail.json"),
                    AppCMSPageUI.class);
            module = appCMSPageUI1.getModuleList().get(0);

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

            int imageHeight = getDeviceHeight();
            int imageWidth = getDeviceWidth();

            if (module != null && module.getComponents() != null) {
                for (int i = 0; i < module.getComponents().size(); i++) {
                    Component component = module.getComponents().get(i);
                    AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
                    if (componentKey == null) {
                        componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                    }
                    if (componentKey == PAGE_PHOTO_PLAYER_IMAGE) {
                        AppCMSBackgroundImageView playerImage = new AppCMSBackgroundImageView(context);

                        String playerImgUrl = "";
                        playerImage.setImageResource(R.drawable.vid_image_placeholder_square);

                        if (BaseView.isLandscape(context)) {
                            if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getImageGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getImageGist().get_1x1() != null) {
                                playerImgUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(0).getGist().getImageGist().get_1x1(),
                                        imageWidth,
                                        imageHeight);
                            }
                        } else {
                            if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getPosterImageUrl() != null) {
                                playerImgUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(0).getGist().getPosterImageUrl(),
                                        imageWidth,
                                        imageHeight);
                            } else if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getImageGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getImageGist().get_3x4() != null) {
                                playerImgUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(0).getGist().getImageGist().get_3x4(),
                                        imageWidth,
                                        imageHeight);
                            } else if (moduleAPI != null &&
                                    moduleAPI.getContentData() != null &&
                                    moduleAPI.getContentData().size() != 0 &&
                                    moduleAPI.getContentData().get(0) != null &&
                                    moduleAPI.getContentData().get(0).getGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getImageGist() != null &&
                                    moduleAPI.getContentData().get(0).getGist().getImageGist().get_1x1() != null) {
                                playerImgUrl = context.getString(R.string.app_cms_image_with_resize_query,
                                        moduleAPI.getContentData().get(0).getGist().getImageGist().get_1x1(),
                                        imageWidth,
                                        imageHeight);
                            }
                        }

                        Glide.with(context)
                                .load(playerImgUrl)
                                .into(playerImage);

                        pageView.reorderViews(playerImage);

                    } else if (componentKey == AppCMSUIKeyType.PAGE_TRAY_TITLE_KEY) {
                        if (moduleAPI != null &&
                                moduleAPI.getContentData() != null &&
                                moduleAPI.getContentData().size() > 0 &&
                                moduleAPI.getContentData().get(0) != null &&
                                moduleAPI.getContentData().get(0).getGist() != null &&
                                moduleAPI.getContentData().get(0).getGist().getRelatedVideos() != null &&
                                moduleAPI.getContentData().get(0).getGist().getRelatedVideos().size() > 0) {
                            boolean vodsInRelatedVideos = true;
                            for (int j = 0; j < moduleAPI.getContentData().get(0).getGist().getRelatedVideos().size(); j++) {
                                if (!moduleAPI.getContentData().get(0).getGist().getRelatedVideos().get(j).getGist().isLiveStream()) {
                                    vodsInRelatedVideos = true;
                                    break;
                                } else {
                                    vodsInRelatedVideos = false;
                                }
                            }
                            if (vodsInRelatedVideos) {
                                recentClassesTitle.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getKey()) != null ?
                                        appCMSPresenter.getLanguageResourcesFile().getUIresource(component.getText()).toUpperCase() : component.getText().toUpperCase());
                            }
                        } else {
                            recentClassesTitle.setVisibility(View.INVISIBLE);
                        }
                    } else if (componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_RECENT_GRID_KEY) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1,
                                GridLayoutManager.VERTICAL, false);
                        list.setLayoutManager(gridLayoutManager);
                        RecentClassesAdapter recentClassesAdapter = new RecentClassesAdapter(component, appCMSPresenter, moduleAPI.getContentData().get(0));
                        list.setAdapter(recentClassesAdapter);
                    } else if (componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_SEPARATOR_KEY) {
                    } else if (componentKey == AppCMSUIKeyType.PAGE_BRAND_DETAIL_EXPANDABLE_GRID_KEY) {
                        List<ExpandableItem> emptyList = new ArrayList<>();
                        emptyList.add(null);
                        ExpandableItem expandableItem = new ExpandableItem(null, emptyList);
                        List<ExpandableItem> expandableItemList = new ArrayList<>();
                        expandableItemList.add(expandableItem);
                        expandableItemList.add(expandableItem);

                        RecyclerView recyclerView = findViewById(R.id.recyclerview);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        ExpandableGridAdapter adapter = new ExpandableGridAdapter(expandableItemList, component, appCMSPresenter, moduleAPI.getContentData().get(0),context);
                        SeparatorDecoration separatorDecoration = new SeparatorDecoration(recyclerView.getContext(), Color.DKGRAY, 1f);
                        recyclerView.addItemDecoration(separatorDecoration);
                        recyclerView.setAdapter(adapter);
                    } else {
                        addChildComponents(moduleView1, component, appCMSAndroidModules, i);
                    }
                }
            }
            childContainer.addView(moduleView1);
            if (module.getSettings() != null && !module.getSettings().isHidden()) {
                pageView.addModuleViewWithModuleId(module.getId(), moduleView1, false,jsonValueKeyMap.get(module.getBlockName()));
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
        float parentYAxis = 2 * getYAxis(getContext(), subComponent.getLayout(), 0.0f);
        AppCMSUIKeyType componentType = jsonValueKeyMap.get(subComponent.getType());
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(subComponent.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (componentViewResult != null && subComponentChildContainer != null && componentKey != PAGE_PHOTO_PLAYER_IMAGE) {
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
                        context.getString(R.string.app_cms_page_person_details_03_module_key), moduleInfo.getBlockName());
            }
        } else {
            moduleView.setComponentHasView(i, false);
        }
    }

}

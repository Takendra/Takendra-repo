package com.viewlift.views.customviews.moduleview.constraintview;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ModuleView;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;

public class VideoPlayerInfo04 extends ModuleView {

    private static final String TAG = VideoPlayerInfo04.class.getSimpleName();
    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final ViewCreator viewCreator;
    Context context;
    private AppCMSAndroidModules appCMSAndroidModules;
    PageView pageView;
    ConstraintLayout mConstraintLayout;
    RelativeLayout rootView;
    ConstraintViewCreator constraintViewCreator;

    public VideoPlayerInfo04(Context context,
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


        mConstraintLayout = new ConstraintLayout(this.context);
        mConstraintLayout.setId(R.id.mConstraintLayout);

        mConstraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rootView = new RelativeLayout(this.context);
        appCMSPresenter.showLoadingDialog(true);
        String category = getCategory(moduleAPI);
        appCMSPresenter.getConceptDataFromCategory(category, new Action1<List<Person>>() {
            @Override
            public void call(List<Person> concepts) {

                if (concepts != null) {
                    Module conceptModule = new Module();
                    List<ContentDatum> dataConcept = new ArrayList<>();
                    for (Person records : concepts) {
                        if (records.getGist() != null) {
                            dataConcept.add(convertToContentDatum(records));
                        }
                    }

                    conceptModule.setContentData(dataConcept);
                    conceptModule.setTitle("More Like this === ");
                    moduleAPI.setConceptModule(conceptModule);
                }

                appCMSPresenter.getClassDataFromEpisode(moduleAPI.getContentData().get(0).getGist().getId(), appCMSSearchRelatedEpisode -> {
                    Module moreLikeThisModule = new Module();
                    if (appCMSSearchRelatedEpisode != null
                            && appCMSSearchRelatedEpisode.getEpisodes() != null
                            && appCMSSearchRelatedEpisode.getEpisodes().size() > 0) {
                        moreLikeThisModule.setContentData(appCMSSearchRelatedEpisode.getEpisodes());
                        moduleAPI.setRelatedVODModule(moreLikeThisModule);
                    }

                    initViewConstraint();
                });
               /* appCMSPresenter.getClassDataFromEpisode(moduleAPI.getContentData().get(0).getGist().getId(), new Action1<AppCMSSearøchRelatedEpisode>() {
                    @Override
                    public void call(AppCMSSearchRelatedEpisode appCMSSearchRelatedEpisode) {
                        Module moreLikeThisModule = new Module();
                        if (appCMSSearchRelatedEpisode != null
                                && appCMSSearchRelatedEpisode.getEpisodes() != null
                                && appCMSSearchRelatedEpisode.getEpisodes().size() > 0) {
                            moreLikeThisModule.setContentData(appCMSSearchRelatedEpisode.getEpisodes());
                            moduleAPI.setRelatedVODModule(moreLikeThisModule);
                        }

                        initViewConstraint();
                    }
                });*/


            }
        });


    }


    public void initViewConstraint() {

        ViewGroup childContainer = getChildrenContainer();

        MetadataMap metadataMap = null;
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
            metadataMap = moduleAPI.getMetadataMap();

        int size = moduleInfo.getComponents().size();
        constraintViewCreator.clearText();
        for (int i = 0; i < size; i++) {
            Component childComponent = moduleInfo.getComponents().get(i);
            View chieldView = constraintViewCreator.createComponentView(context,
                    childComponent,
                    moduleAPI,
                    jsonValueKeyMap,
                    childComponent.getType(),
                    moduleAPI.getId(),moduleInfo.getBlockName(),pageView,moduleInfo,appCMSAndroidModules);
            if (chieldView != null) {
                mConstraintLayout.addView(chieldView);

                constraintViewCreator.setComponentViewRelativePosition(context,
                        chieldView,
                        moduleAPI.getContentData().get(0),
                        jsonValueKeyMap,
                        childComponent.getType(),
                        childComponent,
                        mConstraintLayout,moduleInfo.getBlockName(),moduleInfo,
                        metadataMap);

            }


        }
        childContainer.addView(mConstraintLayout);
        appCMSPresenter.showLoadingDialog(false);
    }


    private String getCategory(Module moduleAPI) {
        if (moduleAPI != null && moduleAPI.getContentData() != null
                && moduleAPI.getContentData().get(0) != null
                && moduleAPI.getContentData().get(0).getGist() != null
                && moduleAPI.getContentData().get(0).getGist().getPrimaryCategory() != null
                && moduleAPI.getContentData().get(0).getGist().getPrimaryCategory().getTitle() != null) {
            return moduleAPI.getContentData().get(0).getGist().getPrimaryCategory().getTitle();

        }
        return null;
    }

    public ContentDatum convertToContentDatum(Person records) {
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setGist(records.getGist());
        contentDatum.setId(records.getId());
        contentDatum.setTitle(records.getTitle());
        return contentDatum;
    }

}

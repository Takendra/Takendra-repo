package com.viewlift.views.modules;

import android.content.Context;

import androidx.annotation.Nullable;

import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;

import dagger.Module;
import dagger.Provides;

import com.viewlift.R;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

/**
 * Created by viewlift on 5/5/17.
 */

@Module
public class AppCMSPageViewModule {
    private final Context context;
    private final AppCMSPageUI appCMSPageUI;
    private final AppCMSPageAPI appCMSPageAPI;
    private final AppCMSAndroidModules appCMSAndroidModules;
    private final String screenName;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private final AppCMSPresenter appCMSPresenter;
    private final List<String> modulesToIgnoreList;
    private ViewCreator viewCreator;
    ConstraintViewCreator constraintViewCreator;
    String pageId;

    public AppCMSPageViewModule(Context context,
                                String pageId,
                                AppCMSPageUI appCMSPageUI,
                                AppCMSPageAPI appCMSPageAPI,
                                AppCMSAndroidModules appCMSAndroidModules,
                                String screeeName,
                                Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                AppCMSPresenter appCMSPresenter) {
        this.context = context;
        this.appCMSPageUI = appCMSPageUI;
        this.pageId = pageId;
        this.appCMSPageAPI = appCMSPageAPI;
        this.appCMSAndroidModules = appCMSAndroidModules;
        this.screenName = screeeName;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        if (appCMSPresenter.getAppCMSMain().isMonetizationModelEnabled()) {
            this.modulesToIgnoreList = Arrays.asList(context.getResources().getStringArray(R.array.app_cms_deprecate_modules_ignore));
        } else {
            this.modulesToIgnoreList = Arrays.asList(context.getResources().getStringArray(R.array.app_cms_modules_to_ignore));
        }
    }

    @Provides
    @Singleton
    public ViewCreator providesViewCreator() {
        if (viewCreator == null) {
            viewCreator = new ViewCreator();
        }
        return viewCreator;
    }

    @Provides
    @Singleton
    public ConstraintViewCreator providesConstraintViewCreator() {
        if (constraintViewCreator == null) {
            constraintViewCreator = new ConstraintViewCreator(appCMSPresenter);
        }
        return constraintViewCreator;
    }

    @Provides
    @Singleton
    public List<String> providesModulesToIgnoreList() {
        return modulesToIgnoreList;
    }

    @Provides
    @Singleton
    @Nullable
    public PageView providesViewFromPage(ViewCreator viewCreator, ConstraintViewCreator constraintViewCreator) {
        return viewCreator.generatePage(context,
                pageId,
                appCMSPageUI,
                appCMSPageAPI,
                appCMSAndroidModules,
                screenName,
                jsonValueKeyMap,
                appCMSPresenter,
                modulesToIgnoreList, constraintViewCreator);
    }
}

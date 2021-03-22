package com.viewlift.tv.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.views.customviews.OnInternalEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@SuppressLint("ViewConstructor")
public class TVSettingModuleView extends TVBaseView {

    private final TVPageView pageView;
    private final String viewType;
    private TVModuleView moduleList;
    private Context mContext;
    private Component mComponent;
    private Map<String, AppCMSUIKeyType> mJsonValueKeyMap;
    private final TVViewCreator tvViewCreator;
    private final Module moduleAPI;
    private boolean isFromLoginDialog;
    AppCMSPresenter appCMSPresenter;

    public TVSettingModuleView(TVModuleView moduleList,
                               Context context,
                               Component component,
                               Module moduleAPI,
                               TVViewCreator tvViewCreator,
                               Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                               AppCMSPresenter appCMSPresenter,
                               MetadataMap metadataMap,
                               TVPageView pageView,
                               String viewType, boolean isFromLoginDialog) {
        super(context);
        mContext = context;
        mComponent = component;
        mJsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.tvViewCreator = tvViewCreator;
        this.moduleAPI = moduleAPI;
        this.moduleList = moduleList;
        this.pageView = pageView;
        this.viewType = viewType;
        this.isFromLoginDialog = isFromLoginDialog;
        moduleAPI.setContentData(convertMapToContentDatum());
        init();
        setFocusable(false);
    }


    @Override
    public void init() {
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();

        tvViewCreator.createComponentView(mContext,
                mComponent.getComponents().get(findComponentIndex()),
                mComponent.getComponents().get(findComponentIndex()).getLayout(),
                moduleAPI,
                pageView,
                mComponent.getComponents().get(findComponentIndex()).getSettings(),
                mJsonValueKeyMap,
                appCMSPresenter,
                false,
                viewType,
                isFromLoginDialog);

        if (componentViewResult.onInternalEvent != null) {
            onInternalEvents.add(componentViewResult.onInternalEvent);
        }

        View componentView = componentViewResult.componentView;

        if (componentView != null) {
            setViewMarginsFromComponent(mComponent.getComponents().get(findComponentIndex()),
                    componentView,
                    mComponent.getComponents().get(findComponentIndex()).getLayout(),
                    this,
                    mJsonValueKeyMap,
                    false,
                    false,
                    mComponent.getComponents().get(findComponentIndex()).getView());
            addView(componentView);
        }
    }


    @Override
    protected Component getChildComponent(int index) {
        if (mComponent.getComponents() != null &&
                0 <= index &&
                index < mComponent.getComponents().size()) {
            return mComponent.getComponents().get(index);
        }
        return null;
    }

    @Override
    protected Layout getLayout() {
        return mComponent.getLayout();
    }

    private LinkedHashMap<String,String> createSettingPageList(){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        List<String> keys = Arrays.asList(getResources().getStringArray(R.array.app_cms_setting_page_content_key));
        List<String> values = localisedList();
        for(int index = 0 ; index < keys.size() ; index ++){
            if((keys.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_setting_page_personalization_view_key)) && appCMSPresenter.isRecommendationEnabled()) ||
                    (keys.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_setting_page_subscription_view_key)) && appCMSPresenter.isAppSVOD())){
                map.put(keys.get(index),values.get(index));
            }else if( !(keys.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_setting_page_personalization_view_key)) ||
                    keys.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_setting_page_subscription_view_key))) ){
                map.put(keys.get(index),values.get(index));
            }

        }
        return map;
    }

    private List<String> localisedList() {
        String settingLabel;
        List<String> values = Arrays.asList(getResources().getStringArray(R.array.app_cms_setting_page_content_value));
        for (int index = 0; index < values.size(); index++) {
            if (values.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_setting_page_personalization_view_key))) {
                settingLabel = mContext.getResources().getString(R.string.app_cms_setting_page_personalization_view_key);
                if(moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getPersonalizationtTitle() != null){
                    settingLabel = moduleAPI.getMetadataMap().getPersonalizationtTitle();
                }
                values.set(index,settingLabel);
            }else if (values.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.account_details))) {
                settingLabel = mContext.getResources().getString(R.string.account_details);
                if(moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getAccountDetailsLabel() != null){
                    settingLabel = moduleAPI.getMetadataMap().getAccountDetailsLabel();
                }
                values.set(index,settingLabel);
            }else if (values.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_settings))) {
                settingLabel = mContext.getResources().getString(R.string.app_settings);
                if(moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getAppSettingsLabel() != null){
                    settingLabel = moduleAPI.getMetadataMap().getAppSettingsLabel();
                }
                values.set(index,settingLabel);
            }else if (values.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.app_cms_subscription_title))) {
                settingLabel = mContext.getResources().getString(R.string.app_cms_subscription_title);
                if(moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSubscriptionLabel() != null){
                    settingLabel = moduleAPI.getMetadataMap().getSubscriptionLabel();
                }
                values.set(index,settingLabel);

            }else if (values.get(index).equalsIgnoreCase(mContext.getResources().getString(R.string.log_out_label))) {
                settingLabel = mContext.getResources().getString(R.string.log_out_label);
                if(moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLogoutCta() != null){
                    settingLabel = moduleAPI.getMetadataMap().getLogoutCta();
                }
                values.set(index,settingLabel);
            }
        }
        return values;
    }


    private List<ContentDatum> convertMapToContentDatum(){
        List<ContentDatum> contentData = new ArrayList<>();
        LinkedHashMap<String, String> map = createSettingPageList();
        for (Map.Entry<String,String> entry : map.entrySet()){
            ContentDatum contentDatum = new ContentDatum();
            contentDatum.setTitle(entry.getValue());
            contentDatum.setId(entry.getKey());
            contentData.add(contentDatum);
        }
        return contentData;
    }

    private int findComponentIndex(){
        if(mComponent != null && mComponent.getComponents() != null && moduleAPI != null && moduleAPI.getModuleType() != null) {
            for (int index = 0; index < mComponent.getComponents().size(); index++) {
                if (mComponent.getComponents().get(index).getKey().equalsIgnoreCase(((AppCmsHomeActivity) mContext).getPagName())) {
                    return index;
                }
            }
        }
        return 0;
    }
}

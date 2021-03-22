package com.viewlift.tv.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.fragment.AccountDetailsEditDialogFragment;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_COMPONENT_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_EMAIL_CONTAINER_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_NAME_CONTAINER_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_SETTING_ACCOUNT_DETAIL_PASSWORD_CONTAINER_VIEW;

@SuppressLint("ViewConstructor")
public class SettingModuleComponentView extends TVBaseView {

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
    private int position;
    private String userName;
    private String userEmail;
    private String dialogTitle;

    public SettingModuleComponentView(TVModuleView moduleList,
                                      Context context,
                                      Component component,
                                      Module moduleAPI,
                                      TVViewCreator tvViewCreator,
                                      Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                      AppCMSPresenter appCMSPresenter,
                                      MetadataMap metadataMap,
                                      TVPageView pageView,
                                      String viewType,
                                      boolean isFromLoginDialog) {
        super(context);
        mContext = context;
        mComponent = component;
        this.mJsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.tvViewCreator = tvViewCreator;
        this.moduleAPI = moduleAPI;
        this.moduleList = moduleList;
        this.pageView = pageView;
        this.viewType = viewType;
        this.isFromLoginDialog = isFromLoginDialog;
        setFocusable(false);
        init();
    }


    @Override
    public void init() {
        AppCMSUIKeyType componentKey = mJsonValueKeyMap.get(mComponent.getKey());
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();
        String backgroundColor = mComponent.getBackgroundColor();

        for (Component component : mComponent.getComponents()) {
            tvViewCreator.createComponentView(mContext,
                    component,
                    component.getLayout(),
                    moduleAPI,
                    pageView,
                    component.getSettings(),
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
                setViewMarginsFromComponent(component,
                        componentView,
                        component.getLayout(),
                        this,
                        mJsonValueKeyMap,
                        false,
                        false,
                        component.getView());
                addView(componentView);
            }

            if (backgroundColor != null) {
                //setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(Utils.getSecondaryBackgroundColor(mContext,appCMSPresenter)), 0, 0, mComponent.getCornerRadius()));
                setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(backgroundColor), 0, 0, mComponent.getCornerRadius()));

            }

            if ((componentKey == PAGE_SETTING_ACCOUNT_DETAIL_COMPONENT_VIEW ||
                    componentKey == PAGE_SETTING_ACCOUNT_DETAIL_NAME_CONTAINER_VIEW ||
                    componentKey == PAGE_SETTING_ACCOUNT_DETAIL_PASSWORD_CONTAINER_VIEW ||
                    componentKey == PAGE_SETTING_ACCOUNT_DETAIL_EMAIL_CONTAINER_VIEW)) {

                appCMSPresenter.setModuleApi(moduleAPI);
                setFocusable(true);
                setOnFocusChangeListener((View view1, boolean hasFocus) -> setViewBackground(view1, hasFocus));
            }

            setOnClickListener(v -> {
                switch (componentKey) {
                    case PAGE_SETTING_ACCOUNT_DETAIL_NAME_CONTAINER_VIEW:
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEditNameLabel() != null) {
                            dialogTitle = moduleAPI.getMetadataMap().getEditNameLabel();
                        } else {
                            dialogTitle = mContext.getResources().getString(R.string.edit_name);
                        }
                        openEditDialogFragment(mContext.getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_width),
                                mContext.getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_height),
                                dialogTitle,
                                mContext.getResources().getString(R.string.app_cms_page_setting_account_name_container_view));
                        break;

                    case PAGE_SETTING_ACCOUNT_DETAIL_PASSWORD_CONTAINER_VIEW:
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEditPasswordLabel() != null) {
                            dialogTitle = moduleAPI.getMetadataMap().getEditPasswordLabel();
                        } else {
                            dialogTitle = mContext.getResources().getString(R.string.app_cms_change_password_page_title);
                        }
                        openEditDialogFragment(mContext.getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_width),
                                mContext.getResources().getDimensionPixelSize(R.dimen.edit_overlay_dialog_height),
                                dialogTitle,
                                mContext.getResources().getString(R.string.app_cms_page_setting_account_password_container_view));
                        break;

                    case PAGE_SETTING_ACCOUNT_DETAIL_EMAIL_CONTAINER_VIEW:
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getEditEmailLabel() != null) {
                            dialogTitle = moduleAPI.getMetadataMap().getEditEmailLabel();
                        } else {
                            dialogTitle = mContext.getResources().getString(R.string.edit_email);
                        }
                        openEditDialogFragment(mContext.getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_width),
                                mContext.getResources().getDimensionPixelSize(R.dimen.preview_overlay_dialog_height),
                                dialogTitle,
                                mContext.getResources().getString(R.string.app_cms_page_setting_account_email_container_view));
                        break;
                }
            });
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

    private void openEditDialogFragment(int Width, int height, String dialogTitle, String dialogType) {

        if (((TextView) pageView.findViewById(R.id.user_name)).getText() != null) {
            userName = ((TextView) pageView.findViewById(R.id.user_name)).getText().toString();
        }

        if (((TextView) pageView.findViewById(R.id.user_email)).getText() != null) {
            userEmail = ((TextView) pageView.findViewById(R.id.user_email)).getText().toString();
        }

        AccountDetailsEditDialogFragment clearDialogFragment = Utils.getAccountEditDialogFragment(
                mContext,
                appCMSPresenter,
                Width,
                height,
                dialogTitle,
                userName,
                userEmail,
                appCMSPresenter.getLocalisedStrings().getSaveText(),
                appCMSPresenter.getLocalisedStrings().getCancelText(),
                null,
                12f,
                mComponent.getBackgroundColor(),
                dialogType
        );

        clearDialogFragment.setOnNeutralButtonClicked(s -> {

            if (dialogType.equals(mContext.getResources().getString(R.string.app_cms_page_setting_account_name_container_view))) {
                ((TextView) pageView.findViewById(R.id.user_name)).setText(appCMSPresenter.getAppPreference().getLoggedInUserName());
            } else if (dialogType.equals(mContext.getResources().getString(R.string.app_cms_page_setting_account_email_container_view))) {
                ((TextView) pageView.findViewById(R.id.user_email)).setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail());
            } else if (dialogType.equals(mContext.getResources().getString(R.string.app_cms_page_setting_account_password_container_view))) {

            }

        });
    }

    private void setViewBackground(View view, boolean hasFocus) {
        if (hasFocus) {
            setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(Utils.getFocusBorderColor(mContext, appCMSPresenter)), 0, 0, mComponent.getCornerRadius()));
        } else {
            setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(mComponent.getBackgroundColor()), 0, 0, mComponent.getCornerRadius()));
            // setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(Utils.getSecondaryBackgroundColor(mContext,appCMSPresenter)), 0,0,mComponent.getCornerRadius()));
        }
        try {
            for (int i = 0; i < ((FrameLayout) view).getChildCount(); i++) {
                if (((FrameLayout) view).getChildAt(i) instanceof TextView)
                    Utils.setTextViewColor(appCMSPresenter, hasFocus, ((TextView) ((FrameLayout) view).getChildAt(i)));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

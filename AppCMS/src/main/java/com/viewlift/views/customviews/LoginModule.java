package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.util.Strings;
import com.google.gson.GsonBuilder;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.android.NavigationFooter;
import com.viewlift.models.data.appcms.ui.main.AppCMSMain;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleWithComponents;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.adapters.CountryAdapter;
import com.viewlift.views.adapters.CountryCodes;
import com.viewlift.views.fragments.PhoneUpdationLoginFragment;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import static com.viewlift.Utils.loadJsonFromAssets;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.ALREADY_HAVE_AN_ACCOUNT_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.LOGIN_WITH_EMAIL_PASWWORD;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_LOGIN_PASSWORD_HINT_VIEW;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTFIELD_KEY;

/**
 * Created by viewlift on 6/28/17.
 */

@SuppressLint("ViewConstructor")
public class LoginModule extends ModuleView {
    private static final String TAG = "LoginModule";

    private static final int NUM_CHILD_VIEWS = 2;

    private final ModuleWithComponents moduleInfo;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    private final ViewCreator viewCreator;
    private final AppCMSPresenter.LaunchType launchType;
    Context context;
    private Button[] buttonSelectors;
    private LoginModuleTabButton[] loginModuleTabButtons;
    private ModuleView[] childViews;
    private GradientDrawable[] underlineViews;
    private EditText[] emailInputViews;
    private EditText[] passwordInputViews;
    private int underlineColor;
    private int transparentColor;
    private int bgColor;
    private int ctaTextColor;
    private int ctaBgColor;
    private int loginBorderPadding;
    private EditText visibleEmailInputView;
    private EditText visiblePasswordInputView;
    private AppCMSAndroidModules appCMSAndroidModules;
    //    private String loginAction;
    private String loginInSignUpAction;
    private boolean emailConsent;
    private boolean whatsappConsent;
    // variable to track event time

    @SuppressWarnings("unchecked")
    public LoginModule(Context context,
                       ModuleWithComponents moduleInfo,
                       Module moduleAPI,
                       Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                       ViewCreator viewCreator,
                       AppCMSAndroidModules appCMSAndroidModules) {
        super(context, moduleInfo, false);
        this.context = context;
        ((AppCMSApplication) this.context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.moduleInfo = moduleInfo;
        this.moduleAPI = moduleAPI;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.viewCreator = viewCreator;
        this.buttonSelectors = new Button[NUM_CHILD_VIEWS];
        this.loginModuleTabButtons = new LoginModuleTabButton[NUM_CHILD_VIEWS];
        this.childViews = new ModuleView[NUM_CHILD_VIEWS];
        this.underlineViews = new GradientDrawable[NUM_CHILD_VIEWS];
        this.emailInputViews = new EditText[NUM_CHILD_VIEWS];
        this.passwordInputViews = new EditText[NUM_CHILD_VIEWS];
        this.loginBorderPadding = context.getResources().getInteger(R.integer.app_cms_login_underline_padding);
        this.launchType = appCMSPresenter.getLaunchType();
        this.appCMSAndroidModules = appCMSAndroidModules;
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        if (moduleInfo != null &&
                moduleAPI != null &&
                jsonValueKeyMap != null &&
                appCMSPresenter != null &&
                viewCreator != null) {
            AppCMSMain appCMSMain = appCMSPresenter.getAppCMSMain();
            underlineColor = Color.parseColor(appCMSMain.getBrand().getGeneral().getPageTitleColor());
            transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
            bgColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
            ctaTextColor = Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta()
                    .getPrimary().getTextColor());
            ctaBgColor = Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta()
                    .getPrimary().getBackgroundColor());
            int textColor = Color.parseColor(appCMSMain.getBrand().getGeneral().getTextColor());
            ViewGroup childContainer = getChildrenContainer();
            childContainer.setBackgroundColor(bgColor);

            LinearLayout topLayoutContainer = new LinearLayout(getContext());
            MarginLayoutParams topLayoutContainerLayoutParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            topLayoutContainerLayoutParams.setMargins(0, 0, 0, 0);
            topLayoutContainer.setLayoutParams(topLayoutContainerLayoutParams);
            topLayoutContainer.setPadding(0, 0, 0, 0);
            topLayoutContainer.setOrientation(LinearLayout.VERTICAL);

            LinearLayout loginModuleSwitcherContainer = new LinearLayout(getContext());
            loginModuleSwitcherContainer.setOrientation(LinearLayout.HORIZONTAL);
            MarginLayoutParams loginModuleContainerLayoutParams =
                    new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loginModuleContainerLayoutParams.setMargins((int) convertDpToPixel(getContext().getResources().getInteger(R.integer.app_cms_login_selector_margin), getContext()),
                    0,
                    (int) convertDpToPixel(getContext().getResources().getInteger(R.integer.app_cms_login_selector_margin), getContext()),
                    0);
            loginModuleSwitcherContainer.setLayoutParams(loginModuleContainerLayoutParams);
            loginModuleSwitcherContainer.setBackgroundColor(bgColor);
            loginModuleSwitcherContainer.setPadding(0, 0, 0, 0);

            topLayoutContainer.addView(loginModuleSwitcherContainer);

            ModuleWithComponents module = null;
            if (appCMSPresenter.isMovieSpreeApp() && moduleInfo.getBlockName().contains("authentication01_activate_device")) {
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication_age.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(0);
            } else if (appCMSMain != null /*&&
                    (appCMSMain.getDomainName().equalsIgnoreCase("www.hoichoi.tv") ||
                        appCMSMain.getDomainName().equalsIgnoreCase("staging-hoichoitv.viewlift.com") ||
                            appCMSMain.getDomainName().equalsIgnoreCase("qa.hoichoi.tv") ||
                            appCMSMain.getDomainName().equalsIgnoreCase("staging-ahatv.viewlift.com") ||
                            appCMSMain.getDomainName().equalsIgnoreCase("aha.video") ||
                            appCMSMain.getDomainName().equalsIgnoreCase("www.aha.video") )*/) {
                /*AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue()!=null &&
                                true ?
                                false ? "mobile_otp.json" : "mobile_otp_email.json"
                                : "home.json"),
                        AppCMSPageUI.class);*/
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue()!=null &&
                                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled() ?
                                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isEmailRequired() ? "mobile_otp.json" : "mobile_otp_email.json"
                                : "home.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(0);

                if (moduleInfo.getSettings()!=null && moduleInfo.getSettings().isHideSocialSignup() && BaseView.isLandscape(context)){
                    for (int i= 0; i < module.getComponents().get(1).getComponents().size(); i++){
                        if (module.getComponents().get(1).getComponents().get(i) != null &&
                                module.getComponents().get(1).getComponents().get(i).getKey() != null) {
                            if (module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("phoneTextField") ||
                                    module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("emailTextField") ||
                                    module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("passwordTextField") ||
                                    module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("emailConsentCheckbox") ||
                                    module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("whatsappConsentCheckbox")) {
                                module.getComponents().get(1).getComponents().get(i).getLayout().getTabletLandscape().setXAxis(340);
                            } else if (module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("signup button")) {
                                module.getComponents().get(1).getComponents().get(i).getLayout().getTabletLandscape().setXAxis(426);
                            } else if (module.getComponents().get(1).getComponents().get(i).getKey().equalsIgnoreCase("alreadyHaveAnAccountText")) {
                                module.getComponents().get(1).getComponents().get(i).getLayout().getTabletLandscape().setXAxis(380);
                            }
                        }
                    }
                }
            } else if (moduleInfo.getBlockName().contains("authentication01_activate_device")) {
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication_screen.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(0);
            } else if (moduleInfo.getBlockName().contains("authentication01")) {
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication_screen.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(1);
            }

            /*else if (moduleInfo.getBlockName().contains("authentication01")) {
                AppCMSPageUI appCMSPageUI1 = new GsonBuilder().create().fromJson(
                        loadJsonFromAssets(context, "authentication_01.json"),
                        AppCMSPageUI.class);
                module = appCMSPageUI1.getModuleList().get(0);

            }*/
            else {
                module = appCMSAndroidModules.getModuleListMap().get(moduleInfo.getBlockName());
//                module = appCMSAndroidModules.getModuleListMap().get("authentication01");
            }


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

            if (module != null && module.getComponents() != null) {
                for (Component component : module.getComponents()) {
                    if (((jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_LOGIN_COMPONENT_KEY) ||
                            jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_CREATE_LOGIN_COMPONENT_KEY) &&
                            (launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP ||
                                    launchType == AppCMSPresenter.LaunchType.INIT_SIGNUP ||
                                    launchType == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW ||
                                    launchType == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW ||
                                    launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SUBSCRIBE)) {
                        LinearLayout.LayoutParams loginSelectorLayoutParams =
                                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                        loginSelectorLayoutParams.weight = 1;
                        /*buttonSelectors[0] = new Button(getContext());
                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLoginTab() != null) {
                            buttonSelectors[0].setText(moduleAPI.getMetadataMap().getLoginTab().toUpperCase());
                        }
                        else {
                            buttonSelectors[0].setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_log_in_pager_title)));
                        }
                        buttonSelectors[0].setTextColor(textColor);
                        buttonSelectors[0].setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                        buttonSelectors[0].setLayoutParams(loginSelectorLayoutParams);
                        buttonSelectors[0].setLayoutParams(loginSelectorLayoutParams);
                        buttonSelectors[0].setTypeface(ResourcesCompat.getFont(context,R.font.font_semi_bold));
                        buttonSelectors[0].setOnClickListener((v) -> {
                            selectChild(0);
                            unselectChild(1);
                        });

                        underlineViews[0] = new GradientDrawable();
                        underlineViews[0].setShape(GradientDrawable.LINE);
                        buttonSelectors[0].setCompoundDrawablePadding(loginBorderPadding);
                        Rect textBounds = new Rect();
                        Paint textPaint = buttonSelectors[0].getPaint();
                        textPaint.getTextBounds(buttonSelectors[0].getText().toString(),
                                0,
                                buttonSelectors[0].getText().length(),
                                textBounds);
                        Rect bounds = new Rect(0,
                                textBounds.top,
                                textBounds.width() + loginBorderPadding,
                                textBounds.bottom);
                        underlineViews[0].setBounds(bounds);
                        buttonSelectors[0].setCompoundDrawables(null, null, null, underlineViews[0]);
                        loginModuleSwitcherContainer.addView(buttonSelectors[0]);
*/
                        loginModuleTabButtons[0] = new LoginModuleTabButton(context, appCMSPresenter);

                        if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLoginTab() != null)
                            loginModuleTabButtons[0].setTitle(moduleAPI.getMetadataMap().getLoginTab().toUpperCase(), textColor);
                        else
                            loginModuleTabButtons[0].setTitle(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_log_in_pager_title)), textColor);

                        if (launchType == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW || launchType == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                            loginModuleTabButtons[0].setGravity(Gravity.CENTER);
                            loginModuleTabButtons[0].setVisibility(View.GONE);
                        } else {
                            loginModuleTabButtons[0].setGravity(Gravity.RIGHT);
                            loginModuleTabButtons[0].setVisibility(View.VISIBLE);
                        }
                        loginModuleTabButtons[0].setViewOriantaion(ConstraintSet.END);
                        loginModuleTabButtons[0].setLayoutParams(loginSelectorLayoutParams);
                        loginModuleTabButtons[0].setOnClickListener((v) -> {
                            selectChild(0);
                            unselectChild(1);
                        });

                        loginModuleSwitcherContainer.addView(loginModuleTabButtons[0]);

                        ModuleView moduleView = new ModuleView<>(getContext(), component, false);
                        setViewHeight(getContext(), component.getLayout(), LayoutParams.MATCH_PARENT);
                        childViews[0] = moduleView;
                        addChildComponents(moduleView, component, 0, appCMSAndroidModules);
                        topLayoutContainer.addView(moduleView);
                    } else if (jsonValueKeyMap.get(component.getType()) == AppCMSUIKeyType.PAGE_SIGNUP_COMPONENT_KEY &&
                            (launchType == AppCMSPresenter.LaunchType.SUBSCRIBE ||
                                    launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP ||
                                    launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SUBSCRIBE ||
                                    launchType == AppCMSPresenter.LaunchType.INIT_SIGNUP ||
                                    launchType == AppCMSPresenter.LaunchType.SIGNUP)) {
                        if (launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP ||
                                launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SUBSCRIBE ||
                                launchType == AppCMSPresenter.LaunchType.INIT_SIGNUP ||
                                launchType == AppCMSPresenter.LaunchType.SIGNUP) {
                            LinearLayout.LayoutParams signupSelectorLayoutParams =
                                    new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                            signupSelectorLayoutParams.weight = 1;
                           /* buttonSelectors[1] = new Button(getContext());

                            if (appCMSPresenter.isKrononApp())
                                buttonSelectors[1].setText(appCMSPresenter.getSubscribeNowText());
                            else {
                                if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSignUpTab() != null)
                                    buttonSelectors[1].setText(moduleAPI.getMetadataMap().getSignUpTab().toUpperCase());
                                else
                                    buttonSelectors[1].setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_sign_up_pager_title)));
                            }
                            buttonSelectors[1].setTextColor(textColor);
                            buttonSelectors[1].setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
                            signupSelectorLayoutParams.gravity = Gravity.END;
                            buttonSelectors[1].setLayoutParams(signupSelectorLayoutParams);
                            buttonSelectors[1].setTypeface(ResourcesCompat.getFont(context,R.font.font_semi_bold));
                            buttonSelectors[1].setOnClickListener((v) -> {

                                if (appCMSPresenter.isAppSVOD() && !appCMSPresenter.isShowDialogForWebPurchase()) {

                                    if (TextUtils.isEmpty(appPreference.getRestoreSubscriptionReceipt())) {
                                        //appCMSPresenter.sendCloseOthersAction(null,
                                        //true,
                                        //false);
                                        appCMSPresenter.navigateToSubscriptionPlansPage(appCMSPresenter.getLoginFromNavPage());

                                    } else {
                                        selectChild(1);
                                        unselectChild(0);
                                        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.SUBSCRIBE);
                                    }
                                } else if (appCMSPresenter.isShowDialogForWebPurchase()) {
                                    selectChild(1);
                                    unselectChild(0);
                                } else if (!appCMSPresenter.isAppSVOD()) {
                                    selectChild(1);
                                    unselectChild(0);
                                }
                            });

                            underlineViews[1] = new GradientDrawable();
                            underlineViews[1].setShape(GradientDrawable.LINE);
                            buttonSelectors[1].setCompoundDrawablePadding(loginBorderPadding);
                            Rect textBounds = new Rect();
                            Paint textPaint = buttonSelectors[1].getPaint();
                            textPaint.getTextBounds(buttonSelectors[1].getText().toString(),
                                    0,
                                    buttonSelectors[1].getText().length(),
                                    textBounds);
                            Rect bounds = new Rect(0,
                                    textBounds.top,
                                    textBounds.width() + loginBorderPadding,
                                    textBounds.bottom);
                            underlineViews[1].setBounds(bounds);
                            buttonSelectors[1].setCompoundDrawables(null, null, null, underlineViews[1]);
                            loginModuleSwitcherContainer.addView(buttonSelectors[1]);
*/
                            loginModuleTabButtons[1] = new LoginModuleTabButton(context, appCMSPresenter);

                            if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSignUpTab() != null)
                                loginModuleTabButtons[1].setTitle(moduleAPI.getMetadataMap().getSignUpTab().toUpperCase(), textColor);
                            else
                                loginModuleTabButtons[1].setTitle(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_sign_up_pager_title)), textColor);

                            // loginModuleTabButtons[1].setTitle(" PAGE_SIGNUP_COMPONENT_KEY ",textColor);
                            loginModuleTabButtons[1].setGravity(Gravity.LEFT);
                            loginModuleTabButtons[1].setViewOriantaion(ConstraintSet.START);
                            loginModuleTabButtons[1].setLayoutParams(signupSelectorLayoutParams);
                            loginModuleTabButtons[1].setOnClickListener((v) -> {
                                if (appCMSPresenter.isAppSVOD() && !appCMSPresenter.isShowDialogForWebPurchase()) {

                                    if (TextUtils.isEmpty(appPreference.getRestoreSubscriptionReceipt())) {
                                        //appCMSPresenter.sendCloseOthersAction(null,
                                        //true,
                                        //false);
                                        appCMSPresenter.navigateToSubscriptionPlansPage(appCMSPresenter.getLoginFromNavPage());

                                    } else {
                                        selectChild(1);
                                        unselectChild(0);
                                        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.SUBSCRIBE);
                                    }
                                } else if (appCMSPresenter.isShowDialogForWebPurchase()) {
                                    selectChild(1);
                                    unselectChild(0);
                                } else if (!appCMSPresenter.isAppSVOD()) {
                                    selectChild(1);
                                    unselectChild(0);
                                }
                            });

                            loginModuleSwitcherContainer.addView(loginModuleTabButtons[1]);
                        } else {
                            TextView signUpTitle = new TextView(getContext());
                            LinearLayout.LayoutParams signUpSelectorLayoutParams =
                                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                            signUpSelectorLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                            signUpTitle.setLayoutParams(signUpSelectorLayoutParams);
                            if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSignUpTab() != null)
                                signUpTitle.setText(moduleAPI.getMetadataMap().getSignUpTab().toUpperCase());
                            else
                                signUpTitle.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.app_cms_sign_up_pager_title)));
                            signUpTitle.setTextColor(textColor);
                            signUpTitle.setBackgroundColor(bgColor);
                            signUpTitle.setGravity(Gravity.CENTER_HORIZONTAL);
                            signUpTitle.setTypeface(ResourcesCompat.getFont(context, R.font.font_semi_bold));
                            loginModuleSwitcherContainer.addView(signUpTitle);
                        }

                        ModuleView moduleView = new ModuleView<>(getContext(), component, false);
                        setViewHeight(getContext(), component.getLayout(), LayoutParams.MATCH_PARENT);
                        childViews[1] = moduleView;
                        addChildComponents(moduleView, component, 1, appCMSAndroidModules);
                        topLayoutContainer.addView(moduleView);
                    }
                }
            }

            childContainer.addView(topLayoutContainer);

            if (launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP || launchType == AppCMSPresenter.LaunchType.LOGIN_AND_SUBSCRIBE) {
                selectChild(0);
                unselectChild(1);
            } else if (launchType == AppCMSPresenter.LaunchType.INIT_SIGNUP ||
                    launchType == AppCMSPresenter.LaunchType.SIGNUP) {
                selectChild(1);
                unselectChild(0);
                if (loginModuleTabButtons[0] == null
                        && loginModuleTabButtons[1] != null) {
                    loginModuleTabButtons[1].setGravity(Gravity.CENTER);
                }
            } else if (launchType == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW ||
                    launchType == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                selectChild(0);
            }
        }
    }

    private void selectChild(int childIndex) {
        if (childViews != null &&
                childIndex < childViews.length &&
                childViews[childIndex] != null) {
            childViews[childIndex].setVisibility(VISIBLE);
            loginModuleTabButtons[childIndex].setUnderlineVisibility(View.VISIBLE);
//            buttonSelectors[childIndex].setAlpha(1.0f);
            //setAlphaTextColorForSelector(buttonSelectors[childIndex], 200);
            //applyUnderlineToComponent(underlineViews[childIndex], underlineColor);
            visibleEmailInputView = emailInputViews[childIndex];
            visiblePasswordInputView = passwordInputViews[childIndex];

            if (childIndex == 1 && visibleEmailInputView != null && visiblePasswordInputView != null) {
                visibleEmailInputView.setText("");
                visiblePasswordInputView.setText("");
            }
        }

    }

    private void unselectChild(int childIndex) {
        if (childViews != null &&
                childIndex < childViews.length &&
                childViews[childIndex] != null) {
            childViews[childIndex].setVisibility(GONE);
            loginModuleTabButtons[childIndex].setUnderlineVisibility(View.INVISIBLE);
//            buttonSelectors[childIndex].setAlpha(0.6f);
            // setAlphaTextColorForSelector(buttonSelectors[childIndex], 100);
            //applyUnderlineToComponent(underlineViews[childIndex], bgColor);
        }

    }

    /**
     * Creating values for login/Signup Screen UI component
     *
     * @param moduleView
     * @param subComponent
     * @param childIndex
     * @param appCMSAndroidModules
     */
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    private void addChildComponents(ModuleView moduleView,
                                    Component subComponent,
                                    final int childIndex,
                                    final AppCMSAndroidModules appCMSAndroidModules) {
        ViewCreator.ComponentViewResult componentViewResult = viewCreator.getComponentViewResult();
        ViewGroup subComponentChildContainer = moduleView.getChildrenContainer();
        float parentYAxis = 2 * getYAxis(getContext(), subComponent.getLayout(), 0.0f);
        if (componentViewResult != null && subComponentChildContainer != null) {
            for (int i = 1; i < subComponent.getComponents().size(); i++) {
                View componentView = null;
                final Component component = subComponent.getComponents().get(i);
                AppCMSUIKeyType componentType = jsonValueKeyMap.get(component.getType());
                AppCMSUIKeyType componentKey = jsonValueKeyMap.get(component.getKey());
                if (componentType == null) {
                    componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                if (componentKey == null) {
                    componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                if (componentType != PAGE_TEXTFIELD_KEY) {
                    viewCreator.createComponentView(getContext(),
                            component,
                            component.getLayout(),
                            moduleAPI,
                            appCMSAndroidModules,
                            null,
                            moduleInfo.getSettings(),
                            jsonValueKeyMap,
                            appCMSPresenter,
                            false,
                            "",
                            moduleInfo.getId(), moduleInfo.getBlockName(), moduleInfo.isConstrainteView());
                    componentView = componentViewResult.componentView;
                } else {
                    InputView inputView = new InputView(context, appCMSPresenter, componentKey, moduleAPI);
                    componentView = inputView;
                }
                if (componentView != null) {
                    float componentYAxis = getYAxis(getContext(),
                            component.getLayout(),
                            0.0f);
                    if (!component.isyAxisSetManually()) {
                        setYAxis(getContext(),
                                component.getLayout(),
                                componentYAxis - parentYAxis);
                        component.setyAxisSetManually(true);
                    }
                    subComponentChildContainer.addView(componentView);
                    moduleView.setComponentHasView(i, true);
                    moduleView.setViewMarginsFromComponent(component,
                            componentView,
                            subComponent.getLayout(),
                            subComponentChildContainer,
                            false,
                            jsonValueKeyMap,
                            componentViewResult.useMarginsAsPercentagesOverride,
                            componentViewResult.useWidthOfScreen,
                            "", moduleInfo.getBlockName());

                    switch (componentType) {
                        case PAGE_BUTTON_KEY:
                            if (componentKey == AppCMSUIKeyType.PAGE_FORGOTPASSWORD_KEY) {
                                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                                        || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                    if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getLoginType() != null
                                            && appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_email))) {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    }
                                }
                            }

                            if (componentKey == AppCMSUIKeyType.PAGE_LOGIN_BUTTON_KEY ||
                                    (componentKey == AppCMSUIKeyType.PAGE_SIGNUP_BUTTON_KEY)) {
                                loginInSignUpAction = component.getAction();
                                ((TextView) componentView).setTextColor(ctaTextColor);
                                componentView.setBackgroundColor(ctaBgColor);
                            }
                            if (componentKey == AppCMSUIKeyType.PAGE_LOGIN_BUTTON_KEY) {
                                if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getLoginCta() != null)
                                    ((Button) componentView).setText(moduleAPI.getMetadataMap().getLoginCta().toUpperCase());
                                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                                        || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                    if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getLoginType() != null
                                            && appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_email))) {
                                        componentViewResult.componentView.setVisibility(View.VISIBLE);
                                    } else {
                                        componentViewResult.componentView.setVisibility(View.GONE);
                                    }
                                }
                            }
                            if (componentKey == AppCMSUIKeyType.PAGE_SIGNUP_BUTTON_KEY) {
                                if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSignUpCtaText() != null)
                                    ((Button) componentView).setText(moduleAPI.getMetadataMap().getSignUpCtaText().toUpperCase());
                            }

                           /* if (appCMSPresenter.getAppCMSMain() != null &&
                                    (appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("www.hoichoi.tv") ||
                                            appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("staging-hoichoitv.viewlift.com") ||
                                            appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("staging-ahatv.viewlift.com") ||
                                            appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("aha.video") ||
                                            appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("www.aha.video")) &&
                                    component.getText().equalsIgnoreCase("SIGN UP") && componentKey == AppCMSUIKeyType.PAGE_SIGNUP_BUTTON_KEY) {
                                componentView.setId(R.id.appCMS_sign_up_button);

                                if (moduleInfo.getSettings() != null && moduleInfo.getSettings().isShowAudienceAgeMessage()) {
                                    componentView.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                    componentView.setEnabled(true);
                                } else {
                                    componentView.setBackgroundColor(Color.LTGRAY);
                                    componentView.setEnabled(false);
                                }

                            }*/
                            if (componentKey == AppCMSUIKeyType.PAGE_CHECKBOX_KEY) {
                                if (moduleInfo.getSettings() != null && moduleInfo.getSettings().isShowAudienceAgeMessage()) {
                                    ((AppCompatCheckBox) componentView).setChecked(true);
                                } else {
                                    ((AppCompatCheckBox) componentView).setChecked(false);
                                }

                                ((AppCompatCheckBox) componentView).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        Button btnSignup = findViewById(R.id.appCMS_sign_up_button);
                                        if (btnSignup != null) {
                                            if (isChecked) {
                                                btnSignup.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                                btnSignup.setEnabled(true);
                                            } else {
                                                btnSignup.setBackgroundColor(Color.LTGRAY);
                                                btnSignup.setEnabled(false);
                                            }
                                        }
                                    }
                                });
                            } else if (!TextUtils.isEmpty(component.getKey()) && jsonValueKeyMap.get(component.getKey()) == LOGIN_WITH_EMAIL_PASWWORD) {
                                componentView.setVisibility(View.VISIBLE);
                                View finalComponentView = componentView;
                                componentView.setOnClickListener(v -> {
                                    finalComponentView.setEnabled(false);
                                    new Handler().postDelayed(() -> {
                                        finalComponentView.setEnabled(true);
                                        appCMSPresenter.phoneObjectRequest.setMetadataMap(moduleAPI.getMetadataMap());
                                        FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.add(PhoneUpdationLoginFragment.newInstance(appCMSPresenter.getCurrentActivity(), appCMSPresenter.phoneObjectRequest, false), "PhoneUpdationLoginFragment");
                                        fragmentTransaction.commitAllowingStateLoss();
                                    }, 100);
                                });
                            } else if (appCMSPresenter.getAppCMSMain().getFeatures() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null
                                    && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()
                                    && (component.getAction().equals(getContext().getString(R.string.app_cms_action_forgotpassword_key))
                                    || component.getAction().equals(getContext().getString(R.string.app_cms_action_logingoogle_key))
                                    || component.getAction().equals(getContext().getString(R.string.app_cms_action_loginfacebook_key))
                                    || component.getAction().equals(getContext().getString(R.string.app_cms_action_signupfacebook_key))
                                    || component.getAction().equals(getContext().getString(R.string.app_cms_action_signupgoogle_key)))) {
                                componentView.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String[] authData = new String[3];
                                        authData[0] = "";
                                        appCMSPresenter.launchButtonSelectedAction(null,
                                                component.getAction(),
                                                null,
                                                authData,
                                                null,
                                                true,
                                                0,
                                                null);
                                    }
                                });
                            } else {
                                //todo temp info for Hoichoi- need to be removed after stable solution
                                if (appCMSPresenter.getAppCMSMain() != null &&
                                        (appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("www.hoichoi.tv") ||
                                                appCMSPresenter.getAppCMSMain().getDomainName().equalsIgnoreCase("qa.hoichoi.tv")) &&
                                        appCMSPresenter.isMOTVApp() && loginInSignUpAction != null && loginInSignUpAction.equalsIgnoreCase("signup") &&
                                        component.getText() != null &&
                                        !TextUtils.isEmpty(component.getText()) &&
                                        component.getText().equalsIgnoreCase("SIGN UP") &&
                                        moduleInfo.getBlockName().equalsIgnoreCase("authentication01_activate_device")) {
                                    componentView.setEnabled(false);
                                    componentView.setBackgroundColor(Color.LTGRAY);
                                }

                                componentView.setOnClickListener(v -> {
                                    // appCMSPresenter.showLoader();
                                    if(appCMSPresenter.getAppCMSMain().getFeatures()!=null
                                            && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue()!=null&&
                                            appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()){

                                        appCMSPresenter.showLoadingDialog(true);
                                        //EditText phoneEditText = (EditText) moduleView.findViewById(R.id.phone_input_box);
                                        //EditText phoneCountryEditText = (EditText) moduleView.findViewById(R.id.phone_country_code);

                                        EditText phoneEditText = moduleView.findViewById(R.id.textInputEditField);
                                        EditText phoneCountryEditText = moduleView.findViewById(R.id.textInputEditFieldCountryCode);

                                        String phoneFormatted = phoneCountryEditText.getText().toString() + phoneEditText.getText().toString();
                                        appCMSPresenter.phoneObjectRequest.setPhone(phoneFormatted);

                                        if (visibleEmailInputView != null) {
                                            appCMSPresenter.phoneObjectRequest.setEmail(visibleEmailInputView.getText().toString());
                                        } else {
                                            appCMSPresenter.phoneObjectRequest.setEmail(null);
                                        }

                                        appCMSPresenter.phoneObjectRequest.setName(null);
                                        appCMSPresenter.phoneObjectRequest.setScreenName(component.getAction());

                                        appCMSPresenter.phoneObjectRequest.setMetadataMap(moduleAPI.getMetadataMap());
                                        appCMSPresenter.phoneObjectRequest.setRequestType("send");
                                        appCMSPresenter.phoneObjectRequest.setFromVerify(true);
                                        if (((AppCompatCheckBox) moduleView.findViewById(R.id.whatsappConsentCheckbox)) != null) {
                                            whatsappConsent = ((AppCompatCheckBox) moduleView.findViewById(R.id.whatsappConsentCheckbox)).isChecked();
                                        }
                                        appCMSPresenter.phoneObjectRequest.setWhatsAppConsent(whatsappConsent);
                                        if (((AppCompatCheckBox) moduleView.findViewById(R.id.emailConsentCheckbox)) != null) {
                                            emailConsent = ((AppCompatCheckBox) moduleView.findViewById(R.id.emailConsentCheckbox)).isChecked();
                                        }
                                        appCMSPresenter.phoneObjectRequest.setEmailConsent(emailConsent);
                                        appCMSPresenter.sendPhoneOTP(appCMSPresenter.getPhoneObjectRequest(), null);

                                    } else {
                                        if (!appCMSPresenter.isPageLoading() &&
                                                visibleEmailInputView != null &&
                                                visiblePasswordInputView != null) {

                                            if (moduleView.findViewById(R.id.ageConsentCheckbox) != null) {
                                                boolean ageConsent = ((AppCompatCheckBox) moduleView.findViewById(R.id.ageConsentCheckbox)).isChecked();
                                                if (!ageConsent) {
                                                    moduleView.findViewById(R.id.ageConsentError).setVisibility(VISIBLE);
                                                    return;
                                                }
                                                moduleView.findViewById(R.id.ageConsentError).setVisibility(INVISIBLE);
                                            }

                                            appCMSPresenter.showLoadingDialog(true);

                                            CheckBox emailConsentCheckbox = ((AppCompatCheckBox) moduleView.findViewById(R.id.emailConsentCheckbox));

                                            if (emailConsentCheckbox != null) {
                                                emailConsent = ((AppCompatCheckBox) moduleView.findViewById(R.id.emailConsentCheckbox)).isChecked();
                                            }


                                            String[] authData = new String[4];
                                            authData[0] = visibleEmailInputView.getText().toString();
                                            authData[1] = visiblePasswordInputView.getText().toString();
                                            authData[2] = String.valueOf(emailConsent);
                                            if (emailConsentCheckbox != null && emailConsentCheckbox.getVisibility() == VISIBLE)
                                                authData[3] = "Y";
                                            else
                                                authData[3] = "N";
//                                            if (((AppCompatCheckBox) moduleView.findViewById(R.id.emailConsentCheckbox)).getVisibility() == VISIBLE)
//                                                authData[3] = "Y";
//                                            else
//                                                authData[3] = "N";
                                            appCMSPresenter.launchButtonSelectedAction(null,
                                                    component.getAction(),
                                                    null,
                                                    authData,
                                                    null,
                                                    true,
                                                    0,
                                                    null);
                                        }

                                    }
                                });
                            }

                            break;

                        case PAGE_LABEL_KEY:
                            if (component.getKey() != null &&
                                    !TextUtils.isEmpty(component.getKey()) &&
                                    component.getKey().equalsIgnoreCase("signupAgrement")) {

                                String agreementText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getContext().getString(R.string.app_cms_signup_agreement_text));
                                if (moduleAPI != null && moduleAPI.getMetadataMap() != null && moduleAPI.getMetadataMap().getSignUpTermsAgreementLabel() != null) {
                                    agreementText = moduleAPI.getMetadataMap().getSignUpTermsAgreementLabel();
                                }
                                String signupAgreement = agreementText
                                        + " " + appCMSPresenter.getLocalisedStrings().getTermsOfUsesText()
                                        + " " + appCMSPresenter.getLocalisedStrings().getAndLabel()
                                        + " " + appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText();
                                /*if (appCMSPresenter.getGenericMessages() != null && !TextUtils.isEmpty(appCMSPresenter.getGenericMessages().getAgreeTOSPrivacyPolicy()))
                                    ((TextView) componentView).setText(appCMSPresenter.getGenericMessages().getAgreeTOSPrivacyPolicy());
                                else
                                    ((TextView) componentView).setText(context.getResources().getString(R.string.i_agree_with_terms_of_services_and_privacy_policy));*/
                                ((TextView) componentView).setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
                                ((TextView) componentView).setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                ((TextView) componentView).setText(signupAgreement);
                                ((TextView) componentView).setGravity(Gravity.CENTER_HORIZONTAL);

                                ViewCreator.setTypeFace(context,
                                        appCMSPresenter,
                                        jsonValueKeyMap,
                                        component,
                                        componentViewResult.componentView);

                                if (component.getFontSize() > 0) {
                                    ((TextView) componentViewResult.componentView).setTextSize(component.getFontSize());
                                } else if (component.getLayout().getMobile().getFontSize() > 0) {
                                    ((TextView) componentViewResult.componentView).setTextSize(component.getLayout().getMobile().getFontSize());
                                }

                                ClickableSpan tosClick = new ClickableSpan() {
                                    @Override
                                    public void onClick(View view) {
                                        hideKeyboard((Activity) view.getContext());
                                        String tempEmail    = "";
                                        String tempPassword = "";
                                        if (visibleEmailInputView != null && visibleEmailInputView.getText() != null && !TextUtils.isEmpty(visibleEmailInputView.getText().toString()))
                                            tempEmail = visibleEmailInputView.getText().toString();

                                        if (visiblePasswordInputView != null && visiblePasswordInputView.getText() != null && !TextUtils.isEmpty(visiblePasswordInputView.getText().toString()))
                                            tempPassword = visiblePasswordInputView.getText().toString();
                                        if (!Strings.isEmptyOrWhitespace(context.getResources().getString(R.string.terms_of_service)) && appCMSPresenter.getNavigation().getNavigationFooter() != null) {
                                            for (int i = 0; i < appCMSPresenter.getNavigation().getNavigationFooter().size(); i++) {
                                                NavigationFooter navigationFooter = appCMSPresenter.getNavigation().getNavigationFooter().get(i);
                                                if (navigationFooter.getTitle().equalsIgnoreCase(  context.getResources().getString(R.string.terms_of_service))) {
                                                    if (appCMSPresenter.isAncillaryPage(navigationFooter.getPageId())) {
                                                        appCMSPresenter.openFooterPage(navigationFooter, navigationFooter.getTitle());
                                                    } else {
                                                        appCMSPresenter.navigatToTOSPage(tempEmail, tempPassword);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                      //  appCMSPresenter.navigatToTOSPage(tempEmail, tempPassword);
                                    }
                                };

                                ClickableSpan privacyClick = new ClickableSpan() {
                                    @Override
                                    public void onClick(View view) {
                                        hideKeyboard((Activity) view.getContext());
                                        String tempEmail    = "";
                                        String tempPassword = "";
                                        if (visibleEmailInputView != null && visibleEmailInputView.getText() != null && !TextUtils.isEmpty(visibleEmailInputView.getText().toString()))
                                            tempEmail = visibleEmailInputView.getText().toString();

                                        if (visiblePasswordInputView != null && visiblePasswordInputView.getText() != null && !TextUtils.isEmpty(visiblePasswordInputView.getText().toString()))
                                            tempPassword = visiblePasswordInputView.getText().toString();
                                        if (!Strings.isEmptyOrWhitespace(appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText()) && appCMSPresenter.getNavigation().getNavigationFooter() != null) {
                                            for (int i = 0; i < appCMSPresenter.getNavigation().getNavigationFooter().size(); i++) {
                                                NavigationFooter navigationFooter = appCMSPresenter.getNavigation().getNavigationFooter().get(i);
                                                if (navigationFooter.getTitle().equalsIgnoreCase(appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText())) {
                                                    if (appCMSPresenter.isAncillaryPage(navigationFooter.getPageId())) {
                                                        appCMSPresenter.openFooterPage(navigationFooter, navigationFooter.getTitle());
                                                    } else {
                                                        appCMSPresenter.navigateToPrivacyPolicy(tempEmail, tempPassword);
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                       // appCMSPresenter.navigateToPrivacyPolicy(tempEmail, tempPassword);
                                    }

                                };
                                try {
                                    appCMSPresenter.makeTextViewLinks(((TextView) componentView), new String[]{
                                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.terms_of_use)),
                                                    appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getResources().getString(R.string.privacy_policy))},
                                            new ClickableSpan[]{tosClick, privacyClick}, true);
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }

                                /*SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                                        "Terms of Service");
                                spanTxt.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                    }
                                }, spanTxt.length() - "Term of Services".length(), spanTxt.length(), 0);
                                spanTxt.append(" and");
                                spanTxt.setSpan(new ForegroundColorSpan(appCMSPresenter.getGeneralTextColor()), 17, spanTxt.length(), 0);
                                spanTxt.append(" Privacy Policy");
                                spanTxt.setSpan(new ClickableSpan() {
                                    @Override
                                    public void onClick(View widget) {
                                    }
                                }, spanTxt.length() - "Privacy Policy".length(), spanTxt.length(), 0);
                                ((TextView) componentView).setMovementMethod(LinkMovementMethod.getInstance());
                                ((TextView) componentView).setText(spanTxt, TextView.BufferType.SPANNABLE);
*/

                            }

                            if (!TextUtils.isEmpty(component.getKey()) && jsonValueKeyMap.get(component.getKey()) == ALREADY_HAVE_AN_ACCOUNT_KEY) {
                                //if (appCMSPresenter.isHoichoiApp()) {
                                componentView.setVisibility(View.VISIBLE);
                                ((TextView) componentView).setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                                componentView.setOnClickListener(v -> {
                                    if (appCMSPresenter.getLaunchType() != AppCMSPresenter.LaunchType.LOGIN_AND_SUBSCRIBE)
                                        appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
                                    appCMSPresenter.navigateToLoginPage(true);
                                });

                                ClickableSpan loginClick = new ClickableSpan() {
                                    @Override
                                    public void onClick(View view) {
                                        hideKeyboard((Activity) view.getContext());
                                        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                                            if (moduleInfo.getSettings()!=null && moduleInfo.getSettings().isHideSocialSignup()){
                                                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
                                            } else {
                                                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SUBSCRIBE);
                                            }
                                        }else
                                            appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.LOGIN_AND_SIGNUP);
                                        appCMSPresenter.navigateToLoginPage(true);
                                    }
                                };

                                try {
                                    appCMSPresenter.makeTextViewLinks(((TextView) componentView), new String[]{
                                                    context.getResources().getString(R.string.app_cms_login)},
                                            new ClickableSpan[]{loginClick}, true);
                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }
                                /*} else
                                    componentView.setVisibility(View.GONE);*/
                            }

                            if (!TextUtils.isEmpty(component.getKey()) && jsonValueKeyMap.get(component.getKey()) == PAGE_LOGIN_PASSWORD_HINT_VIEW) {
                                if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                                        || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                    componentViewResult.componentView.setVisibility(View.VISIBLE);
                                } else {
                                    componentViewResult.componentView.setVisibility(View.GONE);
                                }
                            }
                            break;
                        case PAGE_TEXTFIELD_KEY:
                            switch (componentKey) {
                                case PAGE_EMAILTEXTFIELD_KEY:
                                case PAGE_EMAILTEXTFIELD2_KEY:
                                    //emailInputViews[childIndex] = ((TextInputLayout) componentView).getEditText();
                                    emailInputViews[childIndex] = ((InputView) componentView).getEditText();
                                    emailInputViews[childIndex].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                                    //appCMSPresenter.setCursorDrawableColor(emailInputViews[childIndex], 0);
                                    //emailInputViews[childIndex].setBackgroundResource(R.drawable.text_input_layout_bottom_underline);
                                    if (launchType == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                                        visibleEmailInputView = emailInputViews[1];
                                    }
                                    if (appCMSPresenter != null &&
                                            appCMSPresenter.getTempEmail() != null &&
                                            !TextUtils.isEmpty(appCMSPresenter.getTempEmail()) &&
                                            visibleEmailInputView != null) {
                                        visibleEmailInputView.setText(appCMSPresenter.getTempEmail());
                                    }
                                    if (appCMSPresenter != null && appCMSPresenter.getLoggedInUserEmail() != null &&
                                            (launchType == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW || launchType == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW)) {
                                        emailInputViews[childIndex].setText(appCMSPresenter.getLoggedInUserEmail());
                                    }

                                    if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW ||
                                            appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                        componentView.setVisibility(View.GONE);
                                    } else {
                                        componentView.setVisibility(View.VISIBLE);
                                    }
                                    //In Case OTP is Enabled and Email Required is OFF From Tools. Only Phone Field will be shown.
                                    break;
                                case PAGE_PHONETEXTFIELD_KEY:
                                    EditText editTextPhone = ((InputView) componentView).getEditText();
                                    editTextPhone.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                                    if (component.getMaxLength() > 0) {
                                        editTextPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(component.getMaxLength())});
                                    }
                                    if (!appCMSPresenter.isHintPickerOpen) {
                                        appCMSPresenter.isHintPickerOpen = true;
                                        appCMSPresenter.firePhoneHintReceiver();
                                    }
                                    break;
                                case PAGE_PHONE_COUNTRY_KEY: {
                                    //EditText editText = ((TextInputLayout) componentView).getEditText();
                                    EditText editText = ((InputView) componentView).getEditText();
                                    if (editText != null) {
                                        String code = CommonUtils.getCountryCode(context);
                                        if (!TextUtils.isEmpty(code))
                                            editText.setText("+" + code);
                                        editText.setFocusable(false);
                                        editText.setFocusableInTouchMode(false);
                                        editText.setOnTouchListener((v, event) -> {
                                            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                                                new AlertDialog.Builder(getContext()).setAdapter(new CountryAdapter(appCMSPresenter), (dialog, which) -> {
                                                    editText.setText("+" + CountryCodes.getCode(which));
                                                    dialog.dismiss();
                                                }).create().show();
                                            }
                                            return true;
                                        });
                                    }
                                }
                                break;
                                case PAGE_PASSWORDTEXTFIELD_KEY:
                                case PAGE_PASSWORDTEXTFIELD2_KEY:
                                    //passwordInputViews[childIndex] = ((TextInputLayout) componentView).getEditText();
                                    passwordInputViews[childIndex] = ((InputView) componentView).getEditText();
                                    passwordInputViews[childIndex]
                                            .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    passwordInputViews[childIndex]
                                            .setImeOptions(EditorInfo.IME_ACTION_SEND | EditorInfo.IME_ACTION_GO);
                                    passwordInputViews[childIndex]
                                            .setTransformationMethod(new AsteriskPasswordTransformation());
                                    /*passwordInputViews[childIndex]
                                            .setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    appCMSPresenter.setCursorDrawableColor(passwordInputViews[childIndex], 0);*/
                                    if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                                            || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                        if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getLoginType() != null
                                                && appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_email))) {
                                            componentView.setVisibility(View.VISIBLE);
                                        } else {
                                            componentView.setVisibility(View.GONE);
                                        }
                                    }
                                    passwordInputViews[childIndex].setOnEditorActionListener((v, actionId, event) -> {
                                        boolean isImeActionSent = false;
                                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                                            if (!appCMSPresenter.isPageLoading() &&
                                                    visibleEmailInputView != null &&
                                                    visiblePasswordInputView != null) {
                                                appCMSPresenter.showLoadingDialog(true);
                                                String[] authData = new String[2];
                                                authData[0] = visibleEmailInputView.getText().toString();
                                                authData[1] = visiblePasswordInputView.getText().toString();

                                                appCMSPresenter.launchButtonSelectedAction(null,
                                                        loginInSignUpAction,
                                                        null,
                                                        authData,
                                                        null,
                                                        true,
                                                        0,
                                                        null);
                                            }
                                            isImeActionSent = true;
                                        }
                                        return isImeActionSent;
                                    });
                                    appCMSPresenter.noSpaceInEditTextFilter(passwordInputViews[childIndex], context);
                                    if (launchType == AppCMSPresenter.LaunchType.SUBSCRIBE) {
                                        visiblePasswordInputView = passwordInputViews[1];
                                    }
                                    if (appCMSPresenter != null &&
                                            appCMSPresenter.getTempPassword() != null &&
                                            !TextUtils.isEmpty(appCMSPresenter.getTempPassword()) &&
                                            visiblePasswordInputView != null) {
                                        visiblePasswordInputView.setText(appCMSPresenter.getTempPassword());
                                    }
                                    break;

                                default:
                                    break;
                            }
                            break;

                        case PAGE_SEPARATOR_VIEW_KEY:
                            if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIEWING_RESTRICTIONS_VIEW
                                    || appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.VIDEO_PIN_VIEW) {
                                componentViewResult.componentView.setVisibility(View.GONE);
                            } else if(appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.SUBSCRIBE && moduleInfo.getSettings()!=null && moduleInfo.getSettings().isHideSocialSignup()){
                                componentViewResult.componentView.setVisibility(View.GONE);
                            }else
                                componentViewResult.componentView.setVisibility(View.VISIBLE);
                            break;

                        default:
                            componentView.setBackgroundColor(bgColor);
                            break;
                    }
                } else {
                    moduleView.setComponentHasView(i, false);
                }
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void applyUnderlineToComponent(GradientDrawable underline, int color) {
        underline.setStroke((int) convertDpToPixel(2, getContext()), color);
        underline.setColor(transparentColor);
    }

    void setAlphaTextColorForSelector(Button button, int alpha) {
        String textColor = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor();
        int color = Color.parseColor(textColor);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        button.setTextColor(Color.argb(alpha, r, g, b));
    }
}

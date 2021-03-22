package com.viewlift.tv.views.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.Resources;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.AppCmsTVSplashActivity;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCMSTVPlayVideoActivity;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.tv.views.component.AppCMSTVViewComponent;
import com.viewlift.tv.views.component.DaggerAppCMSTVViewComponent;
import com.viewlift.tv.views.customviews.TVModuleView;
import com.viewlift.tv.views.customviews.TVPageView;
import com.viewlift.tv.views.module.AppCMSTVPageViewModule;
import com.viewlift.views.binders.AppCMSBinder;

import java.util.List;

import rx.functions.Action1;

public class AppCmsLoginDialogFragment extends DialogFragment {

    private AppCMSPresenter appCMSPresenter;
    private AppCMSTVViewComponent appCmsViewComponent;
    private TVPageView tvPageView;
    private AppCmsSubNavigationFragment appCmsSubNavigationFragment;
    private Context mContext;
    FrameLayout pageHolder;
    RelativeLayout subNavContaineer;
    private TextView loginView;
    private TextView signupView;
    private ImageView loginIcon;
    private ImageView signupIcon;
    private View loginContaineer;
    private View signupContaineer;
    private Module module;
    private final String TAG = AppCmsLoginDialogFragment.class.getSimpleName();

    public AppCmsLoginDialogFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static AppCmsLoginDialogFragment newInstance(AppCMSBinder appCMSBinder) {
        AppCmsLoginDialogFragment fragment = new AppCmsLoginDialogFragment();
        Bundle args = new Bundle();
        args.putBinder("app_cms_binder_key", appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    private AppCMSBinder appCMSBinder;
    private boolean isLoginPage;
    private TextView subscriptionTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCMSPresenter =
                ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Resources resources = appCMSPresenter.getLanguageResourcesFile();
        String loginText = resources.getUIresource("LOG IN");
        String signuptext = resources.getUIresource("SIGN UP");
        IBinder binder = getArguments().getBinder("app_cms_binder_key");

        if (!(binder instanceof AppCMSBinder)) {
            Log.e(TAG, "onCreateView: Binder not instance of AppCMSBinder. Restarting Application.");
            restartApplication();
            return null;
        }

        if (getArguments() != null) {
            appCMSBinder = (AppCMSBinder) getArguments().getBinder("app_cms_binder_key");
            isLoginPage = getArguments().getBoolean("isLoginPage");
        }
        if (appCmsViewComponent == null) {
            appCmsViewComponent = buildAppCMSViewComponent();
        }

        if (appCmsViewComponent != null) {
            tvPageView = appCmsViewComponent.appCMSTVPageView();
        } else {
            tvPageView = null;
        }

        if (tvPageView != null) {
            if (tvPageView.getParent() != null) {
                ((ViewGroup) tvPageView.getParent()).removeAllViews();
            }
        }
        if (container != null) {
            container.removeAllViews();
        }

        int layoutResourceId = R.layout.app_cms_login_dialog_fragment;
        if (appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate()) {
            layoutResourceId = R.layout.app_cms_left_nav_login_dialog_fragment;
        }
        View view = inflater.inflate(layoutResourceId, null);

        subscriptionTitle = (TextView) view.findViewById(R.id.nav_top_line);

        if (subscriptionTitle != null) {
            if (appCMSPresenter.getTemplateType()
                    .equals(AppCMSPresenter.TemplateType.SPORTS)) {
                updateSubscriptionStrip();
            } else {
                subscriptionTitle.setVisibility(View.GONE);
            }
        }
        /*if(!appCMSPresenter.isLeftNavigationEnabled())
        view.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBackgroundColor()));*/


            LinearLayout subNavHolder = (LinearLayout) view.findViewById(R.id.sub_navigation_placholder);
            subNavContaineer = (RelativeLayout) view.findViewById(R.id.sub_navigation_containeer);

            String backGroundColor = Utils.getBackGroundColor(getActivity(), appCMSPresenter);
            view.setBackgroundColor(Color.parseColor(backGroundColor));
            loginView = (TextView) view.findViewById(R.id.textView_login);
            signupView = (TextView) view.findViewById(R.id.textview_signup);
            loginIcon = (ImageView) view.findViewById(R.id.nav_item_login_image);
            signupIcon = (ImageView) view.findViewById(R.id.nav_item_logout_image);
            loginView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
            signupView.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));


        loginView.setTypeface(Utils.getSpecificTypeface(mContext,
                appCMSPresenter,
                getString(R.string.app_cms_page_font_regular_key)));
        signupView.setTypeface(Utils.getSpecificTypeface(mContext,
                appCMSPresenter,
                getString(R.string.app_cms_page_font_regular_key)));

            if(appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate()) {
                loginView.setTextSize(getResources().getDimension(R.dimen.appcms_tv_leftnavigation_textSize));
                signupView.setTextSize(getResources().getDimension(R.dimen.appcms_tv_leftnavigation_textSize));
            }
        List<NavigationUser> navigationUser = appCMSPresenter.getNavigation().getNavigationUser();


            if(null != loginText){
                loginView.setText(loginText);
            }

            if(null != signuptext){
                signupView.setText(signuptext);
            }

        for (NavigationUser navigation : navigationUser) {
            String title = navigation.getTitle().toUpperCase();
            if (navigation.getLocalizationMap() != null
                    && navigation.getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()) != null
                    && navigation.getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getTitle() != null) {
                title = navigation.getLocalizationMap().get(appCMSPresenter.getLanguage().getLanguageCode()).getTitle().toUpperCase();
            }
            if (navigation.getDisplayedPath() != null
                    && navigation.getDisplayedPath().toLowerCase().contains("Authentication Screen".toLowerCase())) {
                loginView.setText(title);
            } else if (navigation.getDisplayedPath() != null
                    && navigation.getDisplayedPath().toLowerCase().contains("Create Login Screen".toLowerCase())) {
                signupView.setText(title);
            }
        }


            loginContaineer = view.findViewById(R.id.nav_item_login_layout);
            signupContaineer = view.findViewById(R.id.nav_item_logout_layout);

            if (appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate()) {
                subNavHolder.setOrientation(LinearLayout.VERTICAL);
                if(subNavContaineer != null)
                subNavContaineer.getBackground().setTint(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));

                for (NavigationUser navigation : navigationUser) {
                    String title = navigation.getTitle();
                    if (("Log In".equalsIgnoreCase(title) || "Sign In".equalsIgnoreCase(title)) && navigation.getIcon() != null) {
                        loginIcon.setImageResource(Utils.getIcon(navigation.getIcon(), mContext,appCMSPresenter));
                        if (null != loginIcon.getDrawable()) {
                            loginIcon.getDrawable().setTint(Utils.getComplimentColor(appCMSPresenter.getGeneralBackgroundColor()));
                            loginIcon.getDrawable().setTintMode(PorterDuff.Mode.MULTIPLY);
                        }
                    }

                    if ("Sign Up".equalsIgnoreCase(title) && navigation.getIcon() != null) {
                        signupIcon.setImageResource(Utils.getIcon(navigation.getIcon(), mContext,appCMSPresenter));
                        if (null != signupIcon.getDrawable()) {
                            signupIcon.getDrawable().setTint(Utils.getComplimentColor(appCMSPresenter.getGeneralBackgroundColor()));
                            signupIcon.getDrawable().setTintMode(PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }
            }


            if(!appCMSPresenter.isNewsTemplate()) {
                loginView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            subNavHolder.setAlpha(1f);
                        } else {
                            subNavHolder.setAlpha(0.52f);
                        }
                    }
                });
        if(appCMSPresenter.getNavigation() != null
                && appCMSPresenter.getSubscriptionPage() != null
                && !appCMSPresenter.isUserLoggedIn()
                && !appCMSPresenter.isAmazonPurchaseInitiated()
                && appCMSPresenter.isAppSVOD()
                && appCMSPresenter.isFireTVSubscriptionEnabled()){
            signupView.setText(getString(R.string.view_plans_label));

            if(appCMSPresenter.getGenericMessages() != null
                && appCMSPresenter.getGenericMessages().getViewPlansCta() != null){
                signupView.setText(appCMSPresenter.getGenericMessages().getViewPlansCta());
            }
        }

            loginView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        subNavHolder.setAlpha(1f);
                    } else {
                        subNavHolder.setAlpha(0.52f);
                    }
                }
            });

                signupView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            subNavHolder.setAlpha(1f);
                        } else {
                            subNavHolder.setAlpha(0.52f);
                        }
                    }
                });
            }


            loginView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {

                    int keyCode = keyEvent.getKeyCode();
                    int action = keyEvent.getAction();
                    if (action == KeyEvent.ACTION_DOWN) {
                        if (appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate()) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_LEFT:
                                case KeyEvent.KEYCODE_DPAD_RIGHT:
                                    toogleLeftnavPanel(false);
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_UP:
                                    focusLoginView(signupView, loginView);
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_DOWN:
                                    focusSignupView(signupView, loginView);
                                    return true;
                            }
                        } else {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_LEFT:
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_RIGHT:
                                    focusSignupView(signupView, loginView);
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_UP:
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_DOWN:
                                    focusLoginView(signupView, loginView);
                                    return false;
                            }
                        }
                    }
                    return false;
                }
            });

            signupView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    int keyCode = keyEvent.getKeyCode();
                    int action = keyEvent.getAction();
                    if (action == KeyEvent.ACTION_DOWN) {
                        if (appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate()) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_LEFT:
                                case KeyEvent.KEYCODE_DPAD_RIGHT:
                                    toogleLeftnavPanel(false);
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_UP:
                                    focusLoginView(signupView, loginView);
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_DOWN:
                                    focusSignupView(signupView, loginView);
                                    return true;
                            }
                        } else {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_LEFT:
                                    focusLoginView(signupView, loginView);
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_RIGHT:
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_UP:
                                    return true;
                                case KeyEvent.KEYCODE_DPAD_DOWN:
                                    focusLoginView(signupView, loginView);
                                    return false;
                            }
                        }
                    }
                    return false;
                }
            });

            focusLoginView(signupView, loginView);

            signupView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(signupView.getText().toString().equalsIgnoreCase(getString(R.string.view_plans_label))){
                        String pageId = appCMSPresenter.getSubscriptionPage().getPageId();
                        String title = getString(R.string.view_plans_label);
                        if(appCMSPresenter.getGenericMessages() != null
                                && appCMSPresenter.getGenericMessages().getViewPlansCta() != null){
                            title = appCMSPresenter.getGenericMessages().getViewPlansCta();
                        }
                        appCMSPresenter.setViewPlanPageOpenFromADialog(true);

                        appCMSPresenter.navigateToTVPage(
                                pageId,
                                 title,
                                 null,
                                false,
                                Uri.EMPTY,
                                true,
                                false,
                                true, true, false, false);
                    }else{
                        NavigationUser navigationUser = appCMSPresenter.getSignUpNavigation();
                        appCMSPresenter.navigateToTVPage(
                                navigationUser.getPageId(),
                                navigationUser.getTitle(),
                                navigationUser.getUrl(),
                                false,
                                Uri.EMPTY,
                                false,
                                false,
                                true, false, false, false);
                    }
                    }


            });


            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (null != onBackKeyListener)
                            onBackKeyListener.call("");
                    }
                    return false;
                }
            });

            pageHolder = (FrameLayout) view.findViewById(R.id.profile_placeholder);
            pageHolder.addView(tvPageView);


            if (null != tvPageView && tvPageView.getChildrenContainer().getChildAt(0) instanceof TVModuleView) {
                TVModuleView tvModuleView = (TVModuleView) tvPageView.getChildrenContainer().getChildAt(0);
                EditText emailBox = ((EditText) tvModuleView.findViewById(R.id.email_edit_box));
                EditText passwordBox = ((EditText) tvModuleView.findViewById(R.id.password_edit_box));
                Button activateDevice = ((Button) tvModuleView.findViewById(R.id.btn_activate_device));
                Button loginButton = ((Button) tvModuleView.findViewById(R.id.btn_login));
                if(emailBox != null)
                emailBox.setOnKeyListener(leftNavigationListener);
                if(passwordBox != null)
                passwordBox.setOnKeyListener(leftNavigationListener);
                if(null != activateDevice)
                activateDevice.setOnKeyListener(leftNavigationListener);
                if(loginButton != null)
                loginButton.setOnKeyListener(leftNavigationListener);

            }

            if(appCMSBinder.getAppCMSPageUI() != null
                    && appCMSBinder.getAppCMSPageUI().getModuleList() != null
            && appCMSBinder.getAppCMSPageUI().getModuleList().size() > 0){
                ModuleList moduleList = appCMSBinder.getAppCMSPageUI().getModuleList().get(0);
                if(moduleList != null
                        && moduleList.getBlockName() != null
                && moduleList.getBlockName().equalsIgnoreCase("entitlement01")){
                    if(signupView != null)
                        signupView.setVisibility(View.GONE);
                    if(loginView != null)
                        loginView.setVisibility(View.GONE);
                }
            }
        return view;
    }



    View.OnKeyListener leftNavigationListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate())
                    toogleLeftnavPanel(true);
            }
            return false;
        }
    };

    private void updateSubscriptionStrip() {
        /*Check Subscription in case of SPORTS TEMPLATE*/
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS && !appCMSPresenter.getAppCMSMain().getFeatures().isWebSubscriptionOnly()) {
            if (appCMSPresenter.isAppSVOD()) {
                if (!appCMSPresenter.isUserLoggedIn()) {
                    setSubscriptionText(false);
                } else {
                    appCMSPresenter.getSubscriptionData(appCMSUserSubscriptionPlanResult -> {
                        try {
                            if (appCMSUserSubscriptionPlanResult != null) {
                                String subscriptionStatus = appCMSUserSubscriptionPlanResult.getSubscriptionInfo().getSubscriptionStatus();
                                if (subscriptionStatus.equalsIgnoreCase("COMPLETED") ||
                                        subscriptionStatus.equalsIgnoreCase("DEFERRED_CANCELLATION")) {
                                    setSubscriptionText(true);
                                } else {
                                    setSubscriptionText(false);
                                }
                            } else {
                                setSubscriptionText(false);
                            }
                        } catch (Exception e) {
                            setSubscriptionText(false);
                        }
                    }, false);
                }
            } else {
                setSubscriptionText(true);
            }
        }
    }

    private void setSubscriptionText(boolean isSubscribe) {
        String message = getResources().getString(R.string.blank_string);
        if (!isSubscribe) {
            if (null != appCMSPresenter) {
                message = appCMSPresenter.getTopBannerText();
            }
            if(message.length() == 0) {
                message = appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.watch_live_text));
            }
        }
        subscriptionTitle.setText(message);
        subscriptionTitle.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
        subscriptionTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
        if(appCMSPresenter.isLeftNavigationEnabled() && !appCMSPresenter.isNewsTemplate()){
            RelativeLayout.LayoutParams textLayoutParams = (RelativeLayout.LayoutParams) subscriptionTitle.getLayoutParams();
            if (message.length() == 0) {
                textLayoutParams.height = 10;
            } else {
                textLayoutParams.height = 40;
            }
            subscriptionTitle.setLayoutParams(textLayoutParams);
        }else{
            LinearLayout.LayoutParams textLayoutParams = (LinearLayout.LayoutParams) subscriptionTitle.getLayoutParams();
            if (message.length() == 0) {
                textLayoutParams.height = 10;
            } else {
                textLayoutParams.height = 40;
            }
            subscriptionTitle.setLayoutParams(textLayoutParams);
        }


    }


    private Action1<String> onBackKeyListener;

    public void setBackKeyListener(Action1<String> onBackKeyListener) {
        this.onBackKeyListener = onBackKeyListener;
    }


    private void focusSignupView(TextView signupView, TextView loginView) {

        if (appCMSPresenter.isNewsTemplate()) {
            signupView.setBackground(Utils.getNewsTemplateNavigationSelector(mContext, appCMSPresenter));
            signupView.setTextColor(Utils.getButtonTextColorDrawable(
                    Utils.getSecondaryTextColor(mContext, appCMSPresenter),
                    Utils.getPrimaryTextColor(mContext,appCMSPresenter),appCMSPresenter));
            signupView.setPadding(20,0,20,0);
            loginView.setBackground(null);
        } else {
            if (appCMSPresenter.isLeftNavigationEnabled()) {
                signupContaineer.setBackground(
                        Utils.getNavigationSelectedState(mContext, appCMSPresenter, true, Color.parseColor(appCMSPresenter.getAppBackgroundColor()),false));
            } else {
                signupView.setBackground(Utils.getNavigationSelectedState(mContext, appCMSPresenter, true, Color.parseColor("#000000"), false));
            }

            if (appCMSPresenter.isLeftNavigationEnabled()) {
                signupContaineer.setAlpha(1.0f);
            } else {
                signupView.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_extrabold_key)));
            }
            if (appCMSPresenter.isLeftNavigationEnabled()) {
                loginContaineer.setBackground(null);
            } else {
                loginView.setBackground(null);
            }

            if (appCMSPresenter.isLeftNavigationEnabled()) {
                loginContaineer.setAlpha(0.3f);
            } else {
                loginView.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_semibold_key)));
            }
        }
        signupView.requestFocus();
    }

    private void focusLoginView(TextView signupView, TextView loginView) {

        if (appCMSPresenter.isNewsTemplate()) {
            loginView.setBackground(Utils.getNewsTemplateNavigationSelector(mContext, appCMSPresenter));
            loginView.setTextColor(Utils.getButtonTextColorDrawable(
                    Utils.getSecondaryTextColor(mContext, appCMSPresenter),
                    Utils.getPrimaryTextColor(mContext,appCMSPresenter),appCMSPresenter));
            loginView.setPadding(20,0,20,0);
            signupView.setBackground(null);
        } else {
            if (appCMSPresenter.isLeftNavigationEnabled()) {
                loginContaineer.setBackground(
                        Utils.getNavigationSelectedState(mContext, appCMSPresenter, true, Color.parseColor(appCMSPresenter.getAppBackgroundColor()), false));
            } else {
                loginView.setBackground(Utils.getNavigationSelectedState(mContext, appCMSPresenter, true, Color.parseColor("#000000"), false));
            }
            if (appCMSPresenter.isLeftNavigationEnabled()) {
                loginContaineer.setAlpha(1.0f);
            } else {
                loginView.setTypeface(Utils.getSpecificTypeface(
                        mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_extrabold_key)));
            }
            if (appCMSPresenter.isLeftNavigationEnabled()) {
                signupContaineer.setBackground(null);
            } else {
                signupView.setBackground(null);
            }

            if (appCMSPresenter.isLeftNavigationEnabled()) {
                signupContaineer.setAlpha(0.3f);
            } else {
                signupView.setTypeface(Utils.getSpecificTypeface(mContext,
                        appCMSPresenter,
                        mContext.getString(R.string.app_cms_page_font_semibold_key)));
            }
        }
        loginView.requestFocus();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

    }
/*


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
*/

    @Override
    public void onResume() {
        super.onResume();
        if (null != getActivity() && getActivity() instanceof AppCmsHomeActivity) {
            ((AppCmsHomeActivity) getActivity()).closeSignUpDialog();
        } else if (null != getActivity() && getActivity() instanceof AppCMSTVPlayVideoActivity) {
            ((AppCMSTVPlayVideoActivity) getActivity()).closeSignUpDialog();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        super.onActivityCreated(bundle);
    }


    public AppCMSTVViewComponent buildAppCMSViewComponent() {
        return DaggerAppCMSTVViewComponent.builder()
                .appCMSTVPageViewModule(new AppCMSTVPageViewModule(mContext,
                        appCMSBinder != null ? appCMSBinder.getAppCMSPageUI()  : null,
                        appCMSBinder != null ? appCMSBinder.getAppCMSPageAPI() : null,
                        appCMSPresenter.getJsonValueKeyMap(),
                        appCMSPresenter,
                        true,
                        appCMSBinder.getPageId()
                ))
                .build();
    }


    private void toogleLeftnavPanel(boolean show) {
        if (null != subNavContaineer) {
            subNavContaineer.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        if (show) {
            focusLoginView(signupView, loginView);
        } else {
            loginView.clearFocus();
            subNavContaineer.clearFocus();
        }
    }


    /**
     * In case of older Fire TVs where the system memory is comparatively low, the app when resuming
     * from background doesn't always carry the intended payload (AppCMSBinder). In those, sporadic
     * cases, we restart the application.
     */
    private void restartApplication() {
        Intent mStartActivity = new Intent(getActivity(), AppCmsTVSplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}

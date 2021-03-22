package com.viewlift.tv.views.fragment;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.AppCmsTVSplashActivity;
import com.viewlift.tv.views.component.AppCMSTVViewComponent;
import com.viewlift.tv.views.component.DaggerAppCMSTVViewComponent;
import com.viewlift.tv.views.customviews.TVPageView;
import com.viewlift.tv.views.module.AppCMSTVPageViewModule;
import com.viewlift.views.binders.AppCMSBinder;

import static com.viewlift.presenters.AppCMSPresenter.SHOW_SUBSCRIPTION_MESSAGE_ON_VIDEO_PLAYER_ACTION;

public class AppCmsGenericDialogFragment extends DialogFragment {

    private AppCMSPresenter appCMSPresenter;
    private AppCMSTVViewComponent appCmsViewComponent;
    private TVPageView tvPageView;
    private TextView subscriptionTitle;
    private final String TAG = AppCmsGenericDialogFragment.class.getSimpleName();

    public AppCmsGenericDialogFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
     }

    public static AppCmsGenericDialogFragment newInstance(AppCMSBinder appCMSBinder) {
        AppCmsGenericDialogFragment fragment = new AppCmsGenericDialogFragment();
        Bundle args = new Bundle();
        args.putBinder("app_cms_binder_key", appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    private AppCMSBinder appCMSBinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCMSPresenter =
                ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IBinder binder = getArguments().getBinder("app_cms_binder_key");
        if (!(binder instanceof AppCMSBinder)) {
            Log.e(TAG, "onCreateView: Binder not instance of AppCMSBinder. Restarting Application.");
            restartApplication();
            return null;
        }

        if (getArguments() != null) {
            appCMSBinder = (AppCMSBinder) getArguments().getBinder("app_cms_binder_key");
        }
        if(null != appCMSBinder)
            appCMSPresenter.sendGaScreen(appCMSBinder.getScreenName());

        if (appCmsViewComponent == null ) {
            appCmsViewComponent = buildAppCMSViewComponent();
        }

        if (appCmsViewComponent != null) {
            tvPageView = appCmsViewComponent.appCMSTVPageView();
        } else {
            tvPageView = null;
        }

        subscriptionTitle = new TextView(getActivity());
        subscriptionTitle.setId(R.id.subscription_text);
        subscriptionTitle.setText(getResources().getString(R.string.blank_string));
        subscriptionTitle.setGravity(Gravity.CENTER);
        subscriptionTitle.setFocusable(false);
        subscriptionTitle.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
        subscriptionTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
               10
        );
        subscriptionTitle.setLayoutParams(layoutParams);

        if(subscriptionTitle.getParent() != null){
            ((FrameLayout)subscriptionTitle.getParent()).removeView(subscriptionTitle);
        }

        if (tvPageView.getChildAt(0).getId() == R.id.subscription_text){
            tvPageView.removeViewAt(0);
        }
        tvPageView.addView(subscriptionTitle,0);
        updateSubscriptionStrip();

        if (tvPageView != null) {
            if (tvPageView.getParent() != null) {
                ((ViewGroup) tvPageView.getParent()).removeAllViews();
            }
        }
        if (container != null) {
            container.removeAllViews();
        }
        tvPageView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBackgroundColor()));
        return tvPageView;
    }

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
        }else{
            subscriptionTitle.setVisibility(View.GONE);
        }
    }

    private void setSubscriptionText(boolean isSubscribe) {
        try {
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

            FrameLayout.LayoutParams textLayoutParams = (FrameLayout.LayoutParams) subscriptionTitle.getLayoutParams();
            if (message.length() == 0) {
                textLayoutParams.height = 10;
            } else {
                textLayoutParams.height = 40;
            }
            subscriptionTitle.setLayoutParams(textLayoutParams);
        }catch (Exception e){

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (tvPageView != null && subscriptionTitle != null) {
            tvPageView.removeView(subscriptionTitle);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        super.onActivityCreated(bundle);
    }

    public AppCMSTVViewComponent buildAppCMSViewComponent() {
        return DaggerAppCMSTVViewComponent.builder()
                .appCMSTVPageViewModule(new AppCMSTVPageViewModule(getActivity(),
                        appCMSBinder.getAppCMSPageUI(),
                        appCMSBinder.getAppCMSPageAPI(),
                        appCMSPresenter.getJsonValueKeyMap(),
                        appCMSPresenter,
                        appCMSBinder.getPageId()
                ))
                .build();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (appCMSPresenter.isViewPlanPage(appCMSBinder.getPageId())) {
            appCMSPresenter.setAmazonPurchaseInitiated(false);
        }
        Intent subscriptionMessageIntent = new Intent(SHOW_SUBSCRIPTION_MESSAGE_ON_VIDEO_PLAYER_ACTION);
        appCMSPresenter.getCurrentActivity().sendBroadcast(subscriptionMessageIntent);
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

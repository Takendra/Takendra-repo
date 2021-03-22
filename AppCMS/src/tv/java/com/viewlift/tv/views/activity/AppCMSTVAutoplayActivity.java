package com.viewlift.tv.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.fragment.AppCMSTVAutoplayFragment;
import com.viewlift.tv.views.fragment.AppCmsLinkYourAccountFragment;
import com.viewlift.tv.views.fragment.AppCmsLoginDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsSignUpDialogFragment;
import com.viewlift.tv.views.fragment.AppCmsTvErrorFragment;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.binders.AppCMSVideoPageBinder;

import static com.viewlift.presenters.AppCMSPresenter.DIALOG_FRAGMENT_TAG;


public class AppCMSTVAutoplayActivity extends AppCmsBaseActivity
        implements AppCMSTVAutoplayFragment.FragmentInteractionListener,
        AppCMSTVAutoplayFragment.OnPageCreation, AppCmsTvErrorFragment.ErrorFragmentListener {

    private static final String TAG = "TVAutoPlayActivity";
    AppCMSPresenter appCMSPresenter;
    private AppCMSVideoPageBinder binder;
    private BroadcastReceiver handoffReceiver;
    private AppCMSTVAutoplayFragment autoplayFragment;
    private BroadcastReceiver presenterActionReceiver;
    private AppCmsLoginDialogFragment loginDialog;
    private AppCmsLinkYourAccountFragment appCmsLinkYourAccountFragment;
    private AppCmsSignUpDialogFragment signUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handoffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String sendingPage
                        = intent.getStringExtra(getString(R.string.app_cms_closing_page_name));
                if (intent.getBooleanExtra(getString(R.string.close_self_key), true) &&
                        (sendingPage == null || getString(R.string.app_cms_video_page_tag).equals(sendingPage))) {
                    Log.d(TAG, "Closing activity");
                    finish();
                }
            }
        };

        presenterActionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle args = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
                if (intent.getAction() != null
                        && intent.getAction().equals(AppCMSPresenter.ERROR_DIALOG_ACTION)) {
                    openErrorDialog(intent);
                } else if (intent.getAction().equals(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG)
                        || intent.getAction().equals(AppCMSPresenter.SUBSCRIPTION_DIALOG)) {
                    openEntitleMentDialog();
                } else if (intent.getAction().equals(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION)) {
                    AppCMSBinder binder = (AppCMSBinder) args.getBinder(getString(R.string.app_cms_binder_key));
                    if (binder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_log_in_pager_title))) {
                        openLoginDialog(intent, false);
                    } else if (binder.getPageName().equalsIgnoreCase(getString(R.string.app_cms_sign_up_pager_title)) || (binder.getPageName().equalsIgnoreCase("Create Login Screen"))) {
                        openSignUpDialog(intent, false);
                    }
                } else if (intent.getAction().equals(AppCMSPresenter.ACTION_LINK_YOUR_ACCOUNT)) {
                    openLinkYourAccountScreen(intent);
                }
            }
        };

        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_SCREEN_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ERROR_DIALOG_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ENTITLEMENT_LOGIN_DIALOG));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_NAVIGATE_ACTION));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.ACTION_LINK_YOUR_ACCOUNT));
        registerReceiver(presenterActionReceiver, new IntentFilter(AppCMSPresenter.SUBSCRIPTION_DIALOG));
        appCMSPresenter = ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();

        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(getString(R.string.app_cms_video_player_bundle_binder_key));
        binder = (AppCMSVideoPageBinder)
                bundleExtra.getBinder(getString(R.string.app_cms_video_player_binder_key));
        setContentView(R.layout.activity_app_cms_tv_auto_play);

        //Restore the fragment's instance
        autoplayFragment = (AppCMSTVAutoplayFragment) getSupportFragmentManager()
                .findFragmentByTag(binder.getContentData().getGist().getId());
        if (autoplayFragment == null) {
            createFragment(binder);
        }
    }

    private void openErrorDialog(Intent intent) {
        Utils.pageLoading(false, this);
        Bundle bundle = intent.getBundleExtra(getString(R.string.retryCallBundleKey));
        bundle.putBoolean(getString(R.string.retry_key), bundle.getBoolean(getString(R.string.retry_key)));
        bundle.putBoolean(getString(R.string.register_internet_receiver_key), bundle.getBoolean(getString(R.string.register_internet_receiver_key)));
        bundle.putString(getString(R.string.tv_dialog_msg_key), bundle.getString(getString(R.string.tv_dialog_msg_key)));
        bundle.putString(getString(R.string.tv_dialog_header_key), bundle.getString(getString(R.string.tv_dialog_header_key)));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        AppCmsTvErrorFragment newFragment = AppCmsTvErrorFragment.newInstance(
                bundle);
        newFragment.setErrorListener(this);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
    }

    private void createFragment(AppCMSVideoPageBinder appCMSBinder) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            autoplayFragment = AppCMSTVAutoplayFragment.newInstance(this, appCMSBinder);
            fragmentTransaction.replace(R.id.app_cms_fragment, autoplayFragment, appCMSBinder.getContentData().getGist().getId());
            fragmentTransaction.addToBackStack(appCMSBinder.getContentData().getGist().getId());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to add Fragment to back stack");
        }
    }

    @Override
    public void onSuccess(AppCMSVideoPageBinder binder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(handoffReceiver);
        unregisterReceiver(presenterActionReceiver);
        autoplayFragment = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    @Override
    public void onError(AppCMSVideoPageBinder binder) {

    }

    @Override
    public void onCountdownFinished() {
        appCMSPresenter.playNextVideo(binder,
                binder.getCurrentPlayingVideoIndex() + 1,
                0);
        binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() + 1);
//        finish();
    }

    @Override
    public void closeActivity() {
        appCMSPresenter.sendCloseOthersAction(null, true, false);
        finish();
    }

    @Override
    public int getNavigationContainer() {
        return 0;
    }

    @Override
    public void onErrorScreenClose(boolean closeActivity) {
        finish();
    }

    @Override
    public void onRetry(Bundle bundle) {

    }

    public void openEntitleMentDialog() {
        String dialogMessage = "You have to be a subscriber to watch this.";
        /*String positiveButtonText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_login));
        if (appCMSPresenter.getAppCMSAndroid() != null
                && appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent() != null) {
            if (appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage() != null) {
                dialogMessage = appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getOverlayMessage();
            }
            if (appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getLoginButtonText() != null) {
                positiveButtonText = appCMSPresenter.getAppCMSAndroid().getSubscriptionFlowContent().getLoginButtonText();
            }
        }*/
        ClearDialogFragment newFragment = Utils.getClearDialogFragment(
                this,
                appCMSPresenter,
                getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.subscription_required)),
                dialogMessage,
                null,
                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.close)),
                 14
        );

        newFragment.setOnPositiveButtonClicked(s -> {
            Utils.pageLoading(true, AppCMSTVAutoplayActivity.this);
            NavigationUser navigationUser = appCMSPresenter.getLoginNavigation();
            appCMSPresenter.navigateToTVPage(
                    navigationUser.getPageId(),
                    navigationUser.getTitle(),
                    navigationUser.getUrl(),
                    false,
                    Uri.EMPTY,
                    false,
                    false,
                    true, false, false, false);
        });

        newFragment.setOnNegativeButtonClicked(s -> {
            newFragment.dismiss();
            AppCMSTVAutoplayActivity.this.finish();
        });

        newFragment.setOnBackKeyListener(s -> {
            newFragment.dismiss();
            AppCMSTVAutoplayActivity.this.finish();
        });
    }

    private void openLoginDialog(Intent intent, boolean isLoginPage) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                loginDialog = AppCmsLoginDialogFragment.newInstance(
                        appCMSBinder);
                loginDialog.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
            }
        }
    }


    private void openSignUpDialog(Intent intent, boolean isLoginPage) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                bundle.putBoolean("isLoginPage", isLoginPage);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                signUpDialog = AppCmsSignUpDialogFragment.newInstance(
                        appCMSBinder);
                signUpDialog.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
            }
        }
    }

    private void openLinkYourAccountScreen(Intent intent) {
        if (null != intent) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.app_cms_bundle_key));
            if (null != bundle) {
                AppCMSBinder appCMSBinder = (AppCMSBinder) bundle.get(getString(R.string.app_cms_binder_key));
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                appCmsLinkYourAccountFragment = AppCmsLinkYourAccountFragment.newInstance(
                        appCMSBinder);
                appCmsLinkYourAccountFragment.show(ft, DIALOG_FRAGMENT_TAG);
                Utils.pageLoading(false, this);
            }
        }
    }
}

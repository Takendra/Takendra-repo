package com.viewlift.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSVideoPageBinder;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.fragments.AutoplayFragment;

public class AutoplayActivity
        extends AppCompatActivity
        implements AutoplayFragment.FragmentInteractionListener,
        AutoplayFragment.OnPageCreation {

    private static final String TAG = "AutoplayActivity";
    AppCMSPresenter appCMSPresenter;
    private AppCMSVideoPageBinder binder;
    private BroadcastReceiver handoffReceiver;
    private AutoplayFragment autoplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.activity_autoplay);
        appCMSPresenter = ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();

        try {
            Intent intent = getIntent();
            Bundle bundleExtra = intent.getBundleExtra(getString(R.string.app_cms_video_player_bundle_binder_key));
            binder = (AppCMSVideoPageBinder)
                    bundleExtra.getBinder(getString(R.string.app_cms_video_player_binder_key));

            //Restore the fragment's instance
            autoplayFragment = (AutoplayFragment) getSupportFragmentManager()
                    .findFragmentByTag(binder.getContentData().getGist().getId());
            if (autoplayFragment == null) {
                createFragment(binder);
            }
        } catch (Exception e) {

        }


        handoffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null &&
                        intent.getStringExtra(getString(R.string.app_cms_package_name_key)) != null &&
                        !intent.getStringExtra(getString(R.string.app_cms_package_name_key)).equals(getPackageName())) {
                    return;
                }
                String sendingPage
                        = intent.getStringExtra(getString(R.string.app_cms_closing_page_name));
                if (intent.getBooleanExtra(getString(R.string.close_self_key), true) &&
                        (sendingPage == null || getString(R.string.app_cms_video_page_tag).equals(sendingPage))) {
                    //Log.d(TAG, "Closing activity");
                    finish();
                }
            }
        };

        registerReceiver(handoffReceiver, new IntentFilter(AppCMSPresenter.PRESENTER_CLOSE_AUTOPLAY_SCREEN));

        if (appCMSPresenter != null) {
            appCMSPresenter.setCancelAllLoads(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Utils.isColorDark(appCMSPresenter.getGeneralBackgroundColor())) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

    }

    private void createFragment(AppCMSVideoPageBinder appCMSBinder) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            autoplayFragment = AutoplayFragment.newInstance(this, appCMSBinder);
            fragmentTransaction.replace(R.id.app_cms_fragment, autoplayFragment, appCMSBinder.getContentData().getGist().getId());
            fragmentTransaction.addToBackStack(appCMSBinder.getContentData().getGist().getId());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "Failed to add Fragment to back stack");
        }
    }

    @Override
    public void onCountdownFinished() {

        binder.setCurrentPlayingVideoIndex(binder.getCurrentPlayingVideoIndex() + 1);
        appCMSPresenter.playNextVideo(binder,
                binder.getCurrentPlayingVideoIndex(),
                binder.getContentData().getGist().getWatchedTime());
        finish();
    }


    @Override
    public void cancelCountdown() {
        binder.setAutoplayCancelled(true);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BaseView.isTablet(this)) {
            if (binder != null && binder.getAppCMSPageUI() != null && binder.getAppCMSPageUI().getModuleList() != null &&
                    binder.getAppCMSPageUI().getModuleList().size() > 0 && binder.getAppCMSPageUI().getModuleList().get(0).getBlockName().equalsIgnoreCase(getString(R.string.ui_block_autoplay_04)))
                appCMSPresenter.restrictLandscapeOnly();
            else
                appCMSPresenter.restrictPortraitOnly();
        } else {
            if (!BaseView.isTablet(this)) {
                appCMSPresenter.restrictPortraitOnly();
            } else if (BaseView.isTablet(this)) {
                if (appCMSPresenter.getAppCMSMain() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getOrientation() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getOrientation().getTablet() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getOrientation().getTablet().isLandscape())
                    appCMSPresenter.restrictLandscapeOnly();
                else if (appCMSPresenter.getAppCMSMain() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getOrientation() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getOrientation().getTablet() != null
                        && appCMSPresenter.getAppCMSMain().getFeatures().getOrientation().getTablet().isPortrait())
                    appCMSPresenter.restrictPortraitOnly();
                else
                    appCMSPresenter.unrestrictPortraitOnly();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(handoffReceiver);
        autoplayFragment = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onSuccess(AppCMSVideoPageBinder binder) {

    }

    @Override
    public void onError(AppCMSVideoPageBinder binder) {

    }
}

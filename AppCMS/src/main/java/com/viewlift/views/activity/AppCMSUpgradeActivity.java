package com.viewlift.views.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.fragments.AppCMSUpgradeFragment;

/**
 * Created by viewlift on 10/2/17.
 */

public class AppCMSUpgradeActivity extends AppCompatActivity {
    private static final String UPGRADE_TAG = "upgrade_app_tag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appcms_upgrade_page);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AppCMSUpgradeFragment appCMSUpgradeActivity = AppCMSUpgradeFragment.newInstance();
        fragmentTransaction.add(R.id.upgrade_fragment, appCMSUpgradeActivity, UPGRADE_TAG);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getApplication() instanceof AppCMSApplication) {
            AppCMSPresenter appCMSPresenter = ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
            appCMSPresenter.sendCloseOthersAction(null, false, false);
            appCMSPresenter.refreshAppCMSMain(appCMSMain -> {
                appCMSPresenter.updateAppCMSMain(appCMSMain);
                if (!appCMSPresenter.isAppBelowMinVersion()) {
                    Intent relaunchApp = getPackageManager().getLaunchIntentForPackage(getPackageName());
                    relaunchApp.putExtra(getString(R.string.force_reload_from_network_key), true);
                    startActivity(relaunchApp);
                    finish();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (Utils.isColorDark(appCMSPresenter.getGeneralBackgroundColor())){
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }else {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }
}

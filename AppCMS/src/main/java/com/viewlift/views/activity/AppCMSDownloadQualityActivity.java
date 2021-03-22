package com.viewlift.views.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSDownloadQualityBinder;
import com.viewlift.views.fragments.AppCMSDownloadQualityFragment;

public class AppCMSDownloadQualityActivity extends AppCompatActivity {

    private static final String TAG = AppCMSDownloadQualityActivity.class.getSimpleName();
    private AppCMSPresenter appCMSPresenter;
    private AppCMSDownloadQualityBinder binder;
    private AppCMSDownloadQualityFragment downloadQualityFragment;

    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.onAttach(base));
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.activity_download_quality);
        appCMSPresenter = ((AppCMSApplication) getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        //getWindow().setBackgroundDrawable(new ColorDrawable(appCMSPresenter.getGeneralBackgroundColor()));
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra(getString(R.string.app_cms_download_setting_bundle_key));
        try {
            if (bundleExtra.getBinder(getString(R.string.app_cms_download_setting_binder_key)) != null)
                binder = (AppCMSDownloadQualityBinder)
                        bundleExtra.getBinder(getString(R.string.app_cms_download_setting_binder_key));
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        //Restore the fragment's instance
        downloadQualityFragment = (AppCMSDownloadQualityFragment) getSupportFragmentManager()
                .findFragmentByTag(TAG);

        if (downloadQualityFragment == null) {
            createFragment(binder);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Utils.isColorDark(appCMSPresenter.getGeneralBackgroundColor())) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    private void createFragment(AppCMSDownloadQualityBinder appCMSBinder) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            downloadQualityFragment = AppCMSDownloadQualityFragment.newInstance(this, appCMSBinder);
            fragmentTransaction.replace(R.id.app_cms_fragment, downloadQualityFragment, TAG);
            fragmentTransaction.addToBackStack(TAG);
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            //Log.e(TAG, "Failed to add Fragment to back stack");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downloadQualityFragment = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

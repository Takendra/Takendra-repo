package com.viewlift.views.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by viewlift on 10/2/17.
 */

public class AppCMSUpgradeFragment extends Fragment {
    public static AppCMSUpgradeFragment newInstance() {
        return new AppCMSUpgradeFragment();
    }

    @BindView(R.id.app_cms_upgrade_textview)
    TextView upgradeTextView;

    @BindView(R.id.app_cms_upgrade_button)
    Button upgradeButton;

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    StringBuilder appUpdate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade_page, container, false);

        ButterKnife.bind(this, view);

        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);


        if (appCMSPresenter != null) {
            int textColor = appCMSPresenter.getGeneralTextColor();
            int bgColor = ContextCompat.getColor(getActivity(), R.color.splashbackgroundColor);
            upgradeTextView.setTextColor(textColor);
            upgradeButton.setTextColor(textColor);
            upgradeButton.setBackgroundColor(bgColor);
            appUpdate = new StringBuilder();
            if (localisedStrings.getAppUpdatePrefixText() != null) {
                appUpdate.append(localisedStrings.getAppUpdatePrefixText());
                appUpdate.append(" ");
                appUpdate.append(getString(R.string.app_cms_app_version));
                appUpdate.append(" ");
                appUpdate.append(localisedStrings.getAppUpdateSuffixText());
                appUpdate.append(" ");
                appUpdate.append(Utils.getProperty("AppName", getActivity()));
                upgradeTextView.setText(appUpdate.toString());
            } else
                upgradeTextView.setText(getString(R.string.app_cms_upgrade_textview_text,
                        getString(R.string.app_cms_app_version),
                        Utils.getProperty("AppName", getActivity())));
        }
            upgradeButton.setText(localisedStrings.getAppUpdateCtaText());
        upgradeButton.setOnClickListener((v) -> {
            Intent googlePlayStoreUpgradeAppIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.google_play_store_upgrade_app_url,
                            getString(R.string.package_name))));
            startActivity(googlePlayStoreUpgradeAppIntent);
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (localisedStrings.getAppUpdatePrefixText() != null)
            upgradeTextView.setText(appUpdate.toString());
        else
            upgradeTextView.setText(getString(R.string.app_cms_upgrade_textview_text,
                    getString(R.string.app_cms_app_version),
                    Utils.getProperty("AppName", getActivity())));

    }
}

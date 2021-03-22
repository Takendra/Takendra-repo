package com.viewlift.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.otpView.OtpView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppCMSParentalPINFragment extends Fragment {

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    AppPreference appPreference;

    private Module module;

    @BindView(R.id.containerView)
    ConstraintLayout containerView;
    @BindView(R.id.pageTitle)
    TextView pageTitle;
    @BindView(R.id.pinView)
    OtpView pinView;
    @BindView(R.id.setPinBtn)
    Button setPinBtn;

    public static AppCMSParentalPINFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSParentalPINFragment fragment = new AppCMSParentalPINFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_cms_parental_pin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        appCMSPresenter.showLoadingDialog(false);
        int bgColor         = appCMSPresenter.getGeneralBackgroundColor();
        int buttonColor     = appCMSPresenter.getBrandPrimaryCtaColor();
        int buttonTextColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        int textColor       = appCMSPresenter.getGeneralTextColor();

        containerView.setBackgroundColor(bgColor);
        setPinBtn.setBackgroundColor(buttonColor);
        setPinBtn.setTextColor(buttonTextColor);
        pageTitle.setTextColor(textColor);
        pinView.setItemCount(4);
        pinView.setLineColor(textColor);
        pinView.setTextColor(textColor);
        pinView.setCursorColor(textColor);
        pinView.setMaskingChar("*");
        pinView.setBackgroundColor(Color.TRANSPARENT);

        try {
            if (getArguments() != null) {
                AppCMSBinder appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getString(R.string.fragment_page_bundle_key)));
                if (appCMSPresenter != null && appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null) {
                    module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_01)), appCMSBinder.getAppCMSPageAPI());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLocalisedStrings();
    }

    private void setLocalisedStrings() {
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getEnterPinLabel() != null)
            pageTitle.setText(module.getMetadataMap().getEnterPinLabel());
        else
            pageTitle.setText(getString(R.string.enter_pin));

        if (appPreference != null && !TextUtils.isEmpty(appPreference.getParentalPin())) {
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getResetPinCTA() != null)
                setPinBtn.setText(module.getMetadataMap().getResetPinCTA());
            else
                setPinBtn.setText(getString(R.string.reset_pin_cta));
        } else {
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSetupPinCTA() != null)
                setPinBtn.setText(module.getMetadataMap().getSetupPinCTA());
            else
                setPinBtn.setText(getString(R.string.setup_pin_cta));
        }
    }

    @OnClick(R.id.setPinBtn)
    void onClick() {
        if (appCMSPresenter != null) {
            appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_saveVideoPin), null,
                    new String[]{pinView.getText().toString()}, null, false, -1, null);
        }
    }
}

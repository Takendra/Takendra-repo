package com.viewlift.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppCMSReauthoriseUserFragment extends Fragment {

    @Inject
    AppCMSPresenter appCMSPresenter;

    private AppCMSBinder appCMSBinder;
    private Module module;

    @BindView(R.id.containerView)
    ConstraintLayout containerView;
    @BindView(R.id.emailLabelText)
    TextView emailLabelText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.continueWithPassword)
    Button continueWithPassword;
    @BindView(R.id.loginWithGoogle)
    Button loginWithGoogle;
    @BindView(R.id.loginWithFacebook)
    Button loginWithFacebook;
    @BindView(R.id.emailLoginGroup)
    Group emailLoginGroup;

    public static AppCMSReauthoriseUserFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSReauthoriseUserFragment fragment = new AppCMSReauthoriseUserFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_cmsreauthenticate_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        int bgColor         = appCMSPresenter.getGeneralBackgroundColor();
        int buttonColor     = appCMSPresenter.getBrandPrimaryCtaColor();
        int buttonTextColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        int textColor       = appCMSPresenter.getGeneralTextColor();

        containerView        .setBackgroundColor(bgColor);
        continueWithPassword .setBackgroundColor(buttonColor);
        loginWithGoogle      .setBackgroundColor(buttonColor);
        loginWithFacebook    .setBackgroundColor(buttonColor);
        continueWithPassword .setTextColor(buttonTextColor);
        loginWithGoogle      .setTextColor(buttonTextColor);
        loginWithFacebook    .setTextColor(buttonTextColor);
        emailLabelText       .setTextColor(textColor);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    continueWithPassword.setEnabled(false);
                    continueWithPassword.setAlpha(0.5f);
                } else {
                    continueWithPassword.setEnabled(true);
                    continueWithPassword.setAlpha(1f);
                }
            }
        });

        if (!TextUtils.isEmpty(appCMSPresenter.getFacebookAccessToken()) ||
                (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                        appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(getString(R.string.facebook_auth_provider_name_key)))) {
            loginWithFacebook.setVisibility(View.VISIBLE);
            loginWithGoogle.setVisibility(View.GONE);
            emailLoginGroup.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(appCMSPresenter.getGoogleAccessToken()) ||
                (!TextUtils.isEmpty(appCMSPresenter.getUserAuthProviderName()) &&
                        appCMSPresenter.getUserAuthProviderName().equalsIgnoreCase(getString(R.string.google_auth_provider_name_key)))) {
            loginWithGoogle.setVisibility(View.VISIBLE);
            loginWithFacebook.setVisibility(View.GONE);
            emailLoginGroup.setVisibility(View.GONE);
        } else {
            loginWithGoogle.setVisibility(View.GONE);
            loginWithFacebook.setVisibility(View.GONE);
            emailLoginGroup.setVisibility(View.VISIBLE);
        }


        Bundle args = getArguments();
        try {
            appCMSBinder = ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
            if (appCMSPresenter != null && appCMSBinder.getAppCMSPageUI() != null) {
                module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_authentication_01)), appCMSBinder.getAppCMSPageAPI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLocalisedStrings();
    }

    private void setLocalisedStrings() {
        if (appCMSPresenter.getLocalisedStrings() != null && appCMSPresenter.getLocalisedStrings().getContinueCtaText() != null)
            continueWithPassword.setText(appCMSPresenter.getLocalisedStrings().getContinueCtaText());
        else
            continueWithPassword.setText(R.string.continue_button);

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getAuthWithGoogle() != null)
            loginWithGoogle.setText(module.getMetadataMap().getAuthWithGoogle());
        else
            loginWithGoogle.setText(getString(R.string.auth_with_google));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getAuthWithFacebook() != null)
            loginWithFacebook.setText(module.getMetadataMap().getAuthWithFacebook());
        else
            loginWithFacebook.setText(getString(R.string.auth_with_facebook));
    }

    @OnClick(R.id.continueWithPassword)
    void onClickContinueButton() {
        appCMSPresenter.showLoadingDialog(true);
        String[] authData = new String[2];
        authData[0] = appCMSPresenter.getLoggedInUserEmail();
        authData[1] = passwordEditText.getText().toString();
        if (appCMSPresenter.phoneObjectRequest != null)
            appCMSPresenter.phoneObjectRequest.setFromVerify(false);
        appCMSPresenter.isHintPickerOpen=true;
        appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_login_key), null,
                authData, null, true, 0, null);
    }

    @OnClick(R.id.loginWithGoogle)
    void onClickGoogleButton() {
        appCMSPresenter.showLoadingDialog(true);
        appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_logingoogle_key), null,
                null, null, true, 0, null);
    }

    @OnClick(R.id.loginWithFacebook)
    void onClickFacebookButton() {
        appCMSPresenter.showLoadingDialog(true);
        appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_loginfacebook_key), null,
                null, null, true, 0, null);
    }
}

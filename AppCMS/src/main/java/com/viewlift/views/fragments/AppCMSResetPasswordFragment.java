package com.viewlift.views.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.dialog.CustomShape;
import com.viewlift.views.customviews.ViewCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by viewlift on 7/6/17.
 */

public class AppCMSResetPasswordFragment extends Fragment {

    private AppCMSBinder appCMSBinder;
    private AppCMSPageAPI appCMSPageAPI;

    @BindView(R.id.app_cms_reset_password_page_title)
    AppCompatTextView titleTextView;

    @BindView(R.id.app_cms_reset_password_text_input_description)
    AppCompatTextView appCMSResetPasswordTextInputDescription;

    @BindView(R.id.app_cms_submit_reset_password_button)
    AppCompatButton appCMSSubmitResetPasswordButton;
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;

    @BindView(R.id.email)
    AppCompatEditText emailinput;
    @BindView(R.id.emailContainer)
    ConstraintLayout emailContainer;
    @BindView(R.id.emailTitle)
    AppCompatTextView emailTitle;

    private String buttonCTA;
    private MetadataMap metadataMap;
    private String pageTitle;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    public static AppCMSResetPasswordFragment newInstance(Context context, AppCMSBinder appCMSBinder, String email) {
        AppCMSResetPasswordFragment fragment = new AppCMSResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.app_cms_password_reset_email_key), email);
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);

        int textColor = appCMSPresenter.getGeneralTextColor();

        Bundle args = getArguments();
        try {
            appCMSBinder =
                    ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
            appCMSPageAPI = appCMSBinder.getAppCMSPageAPI();
        } catch (Exception e) {
            Log.e("ResetPasswordFragment", "Failed to extract appCMSBinder from args ");
            e.printStackTrace();
        }

        String email = args.getString(getContext().getString(R.string.app_cms_password_reset_email_key));
        if (appCMSPageAPI != null && appCMSPageAPI.getModules() != null
                && appCMSPageAPI.getModules().size() > 0 && appCMSPageAPI.getModules().get(0).getMetadataMap() != null) {
            metadataMap = appCMSPageAPI.getModules().get(0).getMetadataMap();
            String hint = metadataMap.getEmailInput() == null ?
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_email_input_text_hint))
                    : metadataMap.getEmailInput();
            emailinput.setHint(hint);
            String textDescription = metadataMap.getHeaderText() == null ?
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.please_input_your_email_address_to_send_a_reset_password_link_text))
                    : metadataMap.getHeaderText();
            appCMSResetPasswordTextInputDescription.setText(textDescription);
            buttonCTA = metadataMap.getEmailPasswordCta() == null ?
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.reset_password_text))
                    : metadataMap.getEmailPasswordCta().toUpperCase();
            appCMSSubmitResetPasswordButton.setText(buttonCTA);
            pageTitle = metadataMap.getMobileHeaderText() == null ?
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_reset_password_title))
                    : metadataMap.getMobileHeaderText();
            titleTextView.setText(pageTitle);
        }

        titleTextView.setTextColor(appCMSPresenter.getGeneralTextColor());
        appCMSResetPasswordTextInputDescription.setTextColor(textColor);
        appCMSPresenter.setCursorDrawableColor(emailinput, 0);

        if (email != null && !TextUtils.isEmpty(email.trim())) {
            emailinput.setText(email);
        }
        setViewStyles();
        setFont();
        return view;
    }

    private void setViewStyles() {
        appCMSSubmitResetPasswordButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        appCMSSubmitResetPasswordButton.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        int appTextColor = appCMSPresenter.getGeneralTextColor();
        int appBackgroundColor = appCMSPresenter.getGeneralBackgroundColor();
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        parentLayout.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, emailContainer, rectangleStroke, appTextColor);
        emailinput.setTextColor(appTextColor);
        emailinput.setHintTextColor(appTextColor);
        emailTitle.setTextColor(appTextColor);
        emailTitle.setBackgroundColor(appBackgroundColor);
    }

    @OnClick(R.id.app_cms_submit_reset_password_button)
    void emailPasswordClick() {
        if (emailinput.getText().toString().length() == 0) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RESET_PASSWORD,
                    localisedStrings.getEmptyEmailValidationText(),
                    false,
                    null,
                    null, pageTitle);
        } else if (!appCMSPresenter.isValidEmail(emailinput.getText().toString())) {

            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RESET_PASSWORD,
                    localisedStrings.getEmailFormatValidationMsg(),
                    false,
                    null,
                    null, pageTitle);
        } else {
            appCMSPresenter.resetPassword(emailinput.getText().toString(), pageTitle, metadataMap);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        onDestroy();
    }

    private void setFont() {
        Component component = new Component();
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, titleTextView);
        component.setFontWeight(null);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, emailTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, emailinput);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appCMSResetPasswordTextInputDescription);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appCMSSubmitResetPasswordButton);

    }
}

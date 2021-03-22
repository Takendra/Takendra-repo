package com.viewlift.views.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.authentication.PhoneObjectRequest;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.adapters.CountrySpinnerAdapter;
import com.viewlift.views.customviews.AsteriskPasswordTransformation;
import com.viewlift.views.dialog.CustomShape;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnItemSelected;
import butterknife.Optional;

public class PhoneUpdationLoginFragment extends DialogFragment {

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    @Nullable
    @BindView(R.id.editTextEmail)
    AppCompatEditText editTextEmail;
    @Nullable
    @BindView(R.id.editTextPassword)
    AppCompatEditText editTextPassword;
    @Nullable
    @BindView(R.id.editTextLogin)
    AppCompatButton editTextLogin;
    @Nullable
    @BindView(R.id.addForgotPassword)
    AppCompatTextView addForgotPassword;

    @Nullable
    @BindView(R.id.progressVerify)
    ProgressBar progressBar;
    @Nullable
    @BindView(R.id.mobilePhoneHeader)
    AppCompatTextView mobilePhoneHeader;
    @Nullable
    @BindView(R.id.closeDialogButtton)
    AppCompatImageView dialogCloseButton;
    @Nullable
    @BindView(R.id.phone_country_code)
    AppCompatEditText textMobileCountryCode;
    @Nullable
    @BindView(R.id.mobileUpdateSubmit)
    AppCompatButton mobileUpdateSubmit;
    @Nullable
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;
    @Nullable
    @BindView(R.id.mobileContainer)
    ConstraintLayout mobileContainer;
    @Nullable
    @BindView(R.id.mobileTitle)
    AppCompatTextView mobileTitle;
    @Nullable
    @BindView(R.id.countryCode)
    AppCompatSpinner countryCode;
    @Nullable
    @BindView(R.id.countryCodeContainer)
    ConstraintLayout countryCodeContainer;

    @Nullable
    @BindView(R.id.mobile)
    AppCompatEditText mobile;

    @Nullable
    @BindView(R.id.textViewTitlePassword)
    AppCompatTextView textViewTitlePassword;

    @Nullable
    @BindView(R.id.id_cb_whatsapp_consent)
    AppCompatCheckBox whatsAppConsent;

    @Nullable
    @BindView(R.id.textViewTitleEmail)
    AppCompatTextView textViewTitleEmail;

    @Nullable
    @BindView(R.id.textInputLayoutEmail)
    TextInputLayout textInputLayoutEmail;

    @Nullable
    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout textInputLayoutPassword;

    @Nullable
    @BindView(R.id.textInputEmailContainer)
    ConstraintLayout textInputEmailContainer;

    @Nullable
    @BindView(R.id.textInputPasswordContainer)
    ConstraintLayout textInputPasswordContainer;

    String selectedCountryCode;

    private PhoneObjectRequest phoneObjectRequest;
    private boolean isFromUpdate;

    public PhoneUpdationLoginFragment() {
        // Required empty public constructor
    }

    public PhoneObjectRequest getPhoneObjectRequest() {
        return phoneObjectRequest;
    }

    public static PhoneUpdationLoginFragment newInstance(Context context, PhoneObjectRequest phoneObjectRequest, boolean isFromUpdate) {
        PhoneUpdationLoginFragment fragment = new PhoneUpdationLoginFragment();
        Bundle args = new Bundle();
        args.putSerializable("phoneObject", phoneObjectRequest);
        args.putBoolean("isFromUpdate", isFromUpdate);
        fragment.setArguments(args);
        return fragment;
    }

    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isFromUpdate = getArguments().getBoolean("isFromUpdate");
        if (!isFromUpdate) {
            view = inflater.inflate(R.layout.fragment_mobile_login, container, false);
        } else
            view = inflater.inflate(R.layout.fragment_mobile_update, container, false);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        new Handler().postDelayed(() -> {
            if (isFromUpdate) {
                isHintOpen = true;
                appCMSPresenter.firePhoneHintReceiver();
            }
        }, 400);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appCMSPresenter.restrictPortraitOnly();
        progressBar.setVisibility(View.GONE);
        phoneObjectRequest = (PhoneObjectRequest) getArguments().getSerializable("phoneObject");
        setViewStyle();
        String code = CommonUtils.getCountryCode(getContext());
        if (!TextUtils.isEmpty(code) && textMobileCountryCode != null) {
            textMobileCountryCode.setText("+" + code);
            textMobileCountryCode.setHint("+" + code);
        }
        if (!isFromUpdate) {
            initializeLoginEmailFlow();
        } else {
            initializePhoneUpdateFlow();
        }
        appCMSPresenter.handleWhatsAppConsent(whatsAppConsent);
    }

    @OnClick(R.id.closeDialogButtton)
    void closeScreen() {
        dismiss();
    }

    private void setViewStyle() {
        int appTextColor       = appCMSPresenter.getGeneralTextColor();
        int appBackgroundColor = appCMSPresenter.getGeneralBackgroundColor();
        int transparentColor   = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius       = 10;
        int rectangleStroke    = 2;
        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {appCMSPresenter.getBrandPrimaryCtaColor(), appTextColor};

        parentLayout.setBackgroundColor(appBackgroundColor);
        if (mobilePhoneHeader != null)
            mobilePhoneHeader.setTextColor(appTextColor);
        if (textMobileCountryCode != null) {
            CustomShape.makeRoundCorner(transparentColor, cornerRadius, textMobileCountryCode, rectangleStroke, appTextColor);
            textMobileCountryCode.setTextColor(appTextColor);
            textMobileCountryCode.setHintTextColor(appTextColor);
        }
        if (mobileUpdateSubmit != null)
            mobileUpdateSubmit.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        if (countryCodeContainer != null)
            CustomShape.makeRoundCorner(transparentColor, cornerRadius, countryCodeContainer, rectangleStroke, appTextColor);
        if (mobileContainer != null)
            CustomShape.makeRoundCorner(transparentColor, cornerRadius, mobileContainer, rectangleStroke, appTextColor);
        if (mobile != null) {
            mobile.setTextColor(appTextColor);
            mobile.setHintTextColor(appTextColor);
        }
        if (mobileTitle != null) {
            mobileTitle.setTextColor(appTextColor);
            mobileTitle.setBackgroundColor(appBackgroundColor);
        }

        if (!isFromUpdate) {
            CustomShape.makeRoundCorner(transparentColor, cornerRadius, textInputEmailContainer, rectangleStroke, appTextColor);
            editTextEmail.setTextColor(appTextColor);
            editTextEmail.setHintTextColor(appTextColor);
            textInputLayoutEmail.setEndIconTintList(new ColorStateList(states, colors));
            textViewTitleEmail.setTextColor(appTextColor);
            textViewTitleEmail.setBackgroundColor(appBackgroundColor);

            CustomShape.makeRoundCorner(transparentColor, cornerRadius, textInputPasswordContainer, rectangleStroke, appTextColor);
            editTextPassword.setTextColor(appTextColor);
            editTextPassword.setHintTextColor(appTextColor);
            editTextPassword.setTransformationMethod(new AsteriskPasswordTransformation());
            textInputLayoutPassword.setEndIconTintList(new ColorStateList(states, colors));
            textViewTitlePassword.setTextColor(appTextColor);
            textViewTitlePassword.setBackgroundColor(appBackgroundColor);

            if (phoneObjectRequest.getMetadataMap() != null &&
                    phoneObjectRequest.getMetadataMap().getEmailLabel() != null) {
                textViewTitleEmail.setText(phoneObjectRequest.getMetadataMap().getEmailLabel());
            } else {
                textViewTitleEmail.setText("Email");
            }

            if (phoneObjectRequest.getMetadataMap() != null &&
                    phoneObjectRequest.getMetadataMap().getPasswordLabel() != null) {
                textViewTitlePassword.setText(phoneObjectRequest.getMetadataMap().getPasswordLabel());
            } else {
                textViewTitlePassword.setText("Password");
            }
        }
        if (countryCode != null) {
            setCountryCodeSpinner();
        }
    }

    private CountrySpinnerAdapter adapter;

    private void setCountryCodeSpinner() {
        adapter = new CountrySpinnerAdapter(appCMSPresenter);
        countryCode.setAdapter(adapter);
        int index = adapter.getCountryCodeIndex(CommonUtils.getCountryCode(getContext()));
        countryCode.setSelection(index == -1 ? 1 : index);
    }

    @Optional
    @OnItemSelected(R.id.countryCode)
    public void planSelected(Spinner spinner, int position) {
        selectedCountryCode = adapter.getCountryCode(position).getCountryCode();
    }

    private void initializeLoginEmailFlow() {


        if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getLoginEmailPassword() != null)
            mobilePhoneHeader.setText(appCMSPresenter.getModuleApi().getMetadataMap().getLoginEmailPassword());

        if (phoneObjectRequest.getMetadataMap() != null &&
                phoneObjectRequest.getMetadataMap().getEmailInput() != null
                && phoneObjectRequest.getMetadataMap().getPasswordInput() != null
                && phoneObjectRequest.getMetadataMap().getForgotPasswordCtaText() != null) {
            editTextEmail.setHint(phoneObjectRequest.getMetadataMap().getEmailInput());
            editTextPassword.setHint(phoneObjectRequest.getMetadataMap().getPasswordInput());
            addForgotPassword.setHint(phoneObjectRequest.getMetadataMap().getForgotPasswordCtaText());
        }

        editTextLogin.setOnClickListener(view -> {
            String[] authData = new String[3];
            authData[0] = editTextEmail.getText().toString();
            authData[1] = editTextPassword.getText().toString();
            phoneObjectRequest.setFromVerify(false);
            appCMSPresenter.isHintPickerOpen = true;
            dismiss();
            progressBar.setVisibility(View.VISIBLE);
            appCMSPresenter.launchButtonSelectedAction(null,
                    getContext().getString(R.string.app_cms_action_login_key),
                    null,
                    authData,
                    null,
                    true,
                    0,
                    null);
        });

        addForgotPassword.setOnClickListener(view -> {
            dismiss();
            String[] authData = new String[3];
            authData[0] = editTextEmail.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            appCMSPresenter.launchButtonSelectedAction(null,
                    getContext().getString(R.string.app_cms_action_forgotpassword_key),
                    null,
                    authData,
                    null,
                    true,
                    0,
                    null);
        });
    }

    private boolean isHintOpen;

    @Optional
    @OnItemClick(R.id.phone_country_code)
    void textMobileCountryCodeClick() {
        if (!isHintOpen) {
            new Handler().postDelayed(() -> {
                isHintOpen = true;
                appCMSPresenter.firePhoneHintReceiver();
            }, 400);
        }
    }

    private PhoneUpdationLoginFragment phoneUpdationLoginFragment = this;

    @Optional
    @OnClick(R.id.mobileUpdateSubmit)
    void mobileUpdateSubmitClick() {

        if (appCMSPresenter.getAppPreference().getLoggedInUserPhone() == null || (appCMSPresenter.getAppPreference().getLoggedInUserPhone() != null && !appCMSPresenter.getAppPreference().getLoggedInUserPhone().equalsIgnoreCase("+" + selectedCountryCode + mobile.getText().toString()))) {
            mobileUpdateSubmit.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            appCMSPresenter.phoneObjectRequest.setScreenName("update");
            appCMSPresenter.phoneObjectRequest.setRequestType("send");
            appCMSPresenter.phoneObjectRequest.setFragmentName("phoneUpdationLoginFragment");
            appCMSPresenter.phoneObjectRequest.setPhone("+" + selectedCountryCode + mobile.getText().toString());
            appCMSPresenter.phoneObjectRequest.setWhatsAppConsent(whatsAppConsent != null && whatsAppConsent.isChecked());
            appCMSPresenter.sendPhoneOTP(phoneObjectRequest, s -> {
                if (s.equalsIgnoreCase("enable")) {
                    this.enableLayout();
                } else {
                    dismiss();
                }
            });
        } else {
            appCMSPresenter.showToast(localisedStrings.getUpdateValidNumberMessage(), Toast.LENGTH_LONG);
        }
    }

    @Optional
    @OnClick(R.id.mobile)
    void textMobileUpdateClick() {
        if (!isHintOpen) {
            new Handler().postDelayed(() -> {
                isHintOpen = true;
                appCMSPresenter.firePhoneHintReceiver();
            }, 400);
        }
    }

    private void initializePhoneUpdateFlow() {
        MetadataMap metadataMap = null;
        if (phoneObjectRequest != null && phoneObjectRequest.getMetadataMap() != null) {
            metadataMap = phoneObjectRequest.getMetadataMap();
        }

        if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getUPDATE_PHONE_NUMBER())) {
            mobilePhoneHeader.setText(metadataMap.getUPDATE_PHONE_NUMBER());
        } else {
            mobilePhoneHeader.setText(getString(R.string.update_phone_field));
        }

        if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getPhoneInput())) {
            mobile.setHint(metadataMap.getPhoneInput());
        } else {
            mobile.setHint(getString(R.string.enter_number));
        }

        if (metadataMap != null && !TextUtils.isEmpty(metadataMap.getSEND_OTP_CTA_TEXT())) {
            mobileUpdateSubmit.setText(metadataMap.getSEND_OTP_CTA_TEXT());
        } else {
            mobileUpdateSubmit.setText(getString(R.string.update_phone_number));
        }


        if (textMobileCountryCode != null && getContext() != null) {
            String code = CommonUtils.getCountryCode(getContext());
            if (!TextUtils.isEmpty(code)) {
                textMobileCountryCode.setText("+" + code);
            } else {
                textMobileCountryCode.setHint("+91");
            }
            mobile.requestFocus();
        }
    }

    public void enableLayout() {
        progressBar.setVisibility(View.GONE);
        if (mobileUpdateSubmit != null)
            mobileUpdateSubmit.setEnabled(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (mobile != null && isHintOpen) {
            mobile.setText(appCMSPresenter.getUnformattedPhone());
            if (textMobileCountryCode != null)
                textMobileCountryCode.setText(appCMSPresenter.getUnformattedPhoneCountryCode());
        }

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}

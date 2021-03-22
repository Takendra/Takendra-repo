package com.viewlift.views.fragments;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.LoginSignup;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.adapters.CountrySpinnerAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.CustomShape;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.nativeLoginContainer)
    ConstraintLayout nativeLoginContainer;
    @BindView(R.id.socialLoginContainer)
    ConstraintLayout socialLoginContainer;
    @BindView(R.id.emailFieldsContainer)
    ConstraintLayout emailFieldsContainer;
    @BindView(R.id.mobileFieldsContainer)
    ConstraintLayout mobileFieldsContainer;
    @BindView(R.id.passwordFieldsContainer)
    ConstraintLayout passwordFieldsContainer;
    @BindView(R.id.passwordContainer)
    ConstraintLayout passwordContainer;
    @BindView(R.id.emailContainer)
    ConstraintLayout emailContainer;
    @BindView(R.id.mobileContainer)
    ConstraintLayout mobileContainer;
    @BindView(R.id.emailTitle)
    AppCompatTextView emailTitle;
    @BindView(R.id.email)
    AppCompatEditText email;
    @BindView(R.id.passwordTitle)
    AppCompatTextView passwordTitle;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.mobileTitle)
    AppCompatTextView mobileTitle;
    @BindView(R.id.countryCode)
    AppCompatSpinner countryCode;
    @BindView(R.id.countryCodeContainer)
    ConstraintLayout countryCodeContainer;
    @BindView(R.id.mobile)
    AppCompatEditText mobile;
    @BindView(R.id.signInButton)
    AppCompatButton signInButton;
    @BindView(R.id.separator)
    ConstraintLayout separator;
    @BindView(R.id.orLabel)
    AppCompatTextView orLabel;
    @BindView(R.id.socialButton1)
    ConstraintLayout socialButton1;
    @BindView(R.id.socialImage1)
    AppCompatImageView socialImage1;
    @BindView(R.id.socialText1)
    AppCompatTextView socialText1;
    @BindView(R.id.socialButton2)
    ConstraintLayout socialButton2;
    @BindView(R.id.socialImage2)
    AppCompatImageView socialImage2;
    @BindView(R.id.socialText2)
    AppCompatTextView socialText2;
    @BindView(R.id.socialButton3)
    ConstraintLayout socialButton3;
    @BindView(R.id.socialImage3)
    AppCompatImageView socialImage3;
    @BindView(R.id.socialText3)
    AppCompatTextView socialText3;
    @BindView(R.id.socialButton4)
    ConstraintLayout socialButton4;
    @BindView(R.id.socialImage4)
    AppCompatImageView socialImage4;
    @BindView(R.id.socialText4)
    AppCompatTextView socialText4;
    @BindView(R.id.leftseparatorView)
    View leftseparatorView;
    @BindView(R.id.rightseparatorView)
    View rightseparatorView;

    @BindView(R.id.terms)
    AppCompatCheckBox terms;
    @BindView(R.id.forgotPassword)
    AppCompatTextView forgotPassword;
    String selectedCountryCode;
    private AppCMSBinder appCMSBinder;
    private Unbinder unbinder;
    private ArrayList<String> socialLoginSignUp = new ArrayList<>();
    private final String SOCIAL_MEDIA_FACEBOOK = "Facebook".toLowerCase();
    private final String SOCIAL_MEDIA_GOOGLE = "Google".toLowerCase();
    private final String SOCIAL_MEDIA_TVE = "TVE".toLowerCase();
    private final String SOCIAL_MEDIA_OTP = "OTP".toLowerCase();
    private int facebookPos = 0;
    private int goolgePos = 0;
    private int tvePos = 0;
    private int otpPos = 0;
    @Inject
    AppCMSPresenter appCMSPresenter;
    private MetadataMap metadataMap = null;
    private boolean isOtpEnabled;
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;
    ConstraintLayout.LayoutParams signinButtonParams;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        signinButtonParams = (ConstraintLayout.LayoutParams) signInButton.getLayoutParams();
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        if (appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null)
            email.setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail());
        appCMSPresenter.noSpaceInEditTextFilter(password, getActivity());
        appCMSPresenter.setCursorDrawableColor(password, 0);
        setFont();
        setViewStyles();
        handleModuleSettings();
        setCountryCodeSpinner();
    }
    private void setFont() {
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), email);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), emailTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), password);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), passwordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), mobile);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), mobileTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), forgotPassword);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), signInButton);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), orLabel);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText1);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText2);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText3);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText4);
    }

    private void setViewStyles() {
        signInButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        int appTextColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        int appBackgroundColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {appCMSPresenter.getBrandPrimaryCtaColor(), appTextColor};
        CompoundButtonCompat.setButtonTintList(terms, new ColorStateList(states, colors));
        parentLayout.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, emailContainer, rectangleStroke, appTextColor);
        email.setTextColor(appTextColor);
        email.setHintTextColor(appTextColor);
        emailTitle.setTextColor(appTextColor);
        emailTitle.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, passwordContainer, rectangleStroke, appTextColor);
        password.setTextColor(appTextColor);
        passwordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        password.setHintTextColor(appTextColor);
        passwordTitle.setTextColor(appTextColor);
        passwordTitle.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, countryCodeContainer, rectangleStroke, appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, mobileContainer, rectangleStroke, appTextColor);
        mobile.setTextColor(appTextColor);
        mobile.setHintTextColor(appTextColor);
        mobileTitle.setTextColor(appTextColor);
        mobileTitle.setBackgroundColor(appBackgroundColor);
        orLabel.setTextColor(appTextColor);
        leftseparatorView.setBackgroundColor(appTextColor);
        rightseparatorView.setBackgroundColor(appTextColor);
        terms.setTextColor(appTextColor);
        forgotPassword.setTextColor(appTextColor);
        signInButton.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
    }

    CountrySpinnerAdapter adapter;

    private void setCountryCodeSpinner() {
        adapter = new CountrySpinnerAdapter(appCMSPresenter);
        countryCode.setAdapter(adapter);
        countryCode.setSelection(1);
        int index = adapter.getCountryCodeIndex(CommonUtils.getCountryCode(getContext()));
        countryCode.setSelection(index == -1 ? 1 : index);
    }

    @OnItemSelected(R.id.countryCode)
    public void planSelected(Spinner spinner, int position) {
        selectedCountryCode = adapter.getCountryCode(position).getCountryCode();
    }

    private void handleModuleSettings() {
        ArrayList<LoginSignup> socialLoginSignupOptions = new ArrayList<>();
        ArrayList<LoginSignup> emailLoginSignupOptions = new ArrayList<>();
        for (int i = 0; i < appCMSBinder.getAppCMSPageUI().getModuleList().size(); i++) {
            ModuleList moduleList = appCMSBinder.getAppCMSPageUI().getModuleList().get(i);
            if (moduleList.getBlockName().equalsIgnoreCase(getString(R.string.ui_block_authentication_17)) && moduleList.getSettings() != null && moduleList.getSettings().getOptions() != null) {
                socialLoginSignupOptions = moduleList.getSettings().getOptions().getSocialLoginSignup();
                emailLoginSignupOptions = moduleList.getSettings().getOptions().getEmailLoginSignup();
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    Module moduleApi = appCMSPresenter.matchModuleAPIToModuleUI(moduleList, appCMSBinder.getAppCMSPageAPI());
                    if (moduleApi != null)
                        metadataMap = moduleApi.getMetadataMap();
                }
                /*if (moduleList.getSettings().isShowLoginAgreementText()) {
                    terms.setVisibility(View.VISIBLE);
                    signInButton.setEnabled(false);
                    signInButton.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
                }*/

                if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                        appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                    isOtpEnabled = true;
                }
                setTextToViews();
                break;
            }
        }


        for (int i = 0; i < socialLoginSignupOptions.size(); i++) {
            if (socialLoginSignupOptions.get(i).isEnable()) {
                if (!socialLoginSignupOptions.get(i).getTitle().contains("Apple"))
                    socialLoginSignUp.add(socialLoginSignupOptions.get(i).getTitle());
            }
        }

        handleEmailLoginSignupVisibility(emailLoginSignupOptions);
        setSocialLoginButtonStyle(socialLoginSignUp);
    }

    private void setTextToViews() {
        if (metadataMap != null) {
            if (metadataMap.getForgotPasswordCtaText() != null)
                forgotPassword.setText(metadataMap.getForgotPasswordCtaText());
            if (metadataMap.getLoginCta() != null)
                signInButton.setText(metadataMap.getLoginCta());
            if (metadataMap.getPasswordInput() != null) {
                password.setHint(metadataMap.getPasswordInput());
            }
            if (metadataMap.getEmailInput() != null) {
                email.setHint(metadataMap.getEmailInput());
            }
        }
    }

    private void handleEmailLoginSignupVisibility(ArrayList<LoginSignup> emailLoginSignupOptions) {
        for (int i = 0; i < emailLoginSignupOptions.size(); i++) {
            LoginSignup loginSignup = emailLoginSignupOptions.get(i);
            if (loginSignup.getTitle().toLowerCase().equalsIgnoreCase("Login".toLowerCase()) && !loginSignup.isEnable()) {
                nativeLoginContainer.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            }
        }
    }

    private void setSocialLoginButtonStyle(ArrayList<String> socialLoginSignUp) {
        for (int i = 0; i < socialLoginSignUp.size(); i++) {
            if (socialLoginSignUp.get(i).toLowerCase().equalsIgnoreCase(SOCIAL_MEDIA_FACEBOOK)) {
                facebookPos = i + 1;
            }
            if (socialLoginSignUp.get(i).toLowerCase().equalsIgnoreCase(SOCIAL_MEDIA_GOOGLE)) {
                goolgePos = i + 1;
            }
            if (socialLoginSignUp.get(i).toLowerCase().equalsIgnoreCase(SOCIAL_MEDIA_TVE)) {
                tvePos = i + 1;
            }

        }
        if (tvePos == 1) {
            tveButtonStyle(socialButton1, socialText1, socialImage1);
        } else if (tvePos == 2) {
            tveButtonStyle(socialButton2, socialText2, socialImage2);
        } else if (tvePos == 3) {
            tveButtonStyle(socialButton3, socialText3, socialImage3);
        }

        if (goolgePos == 1) {
            googleButtonStyle(socialButton1, socialText1, socialImage1);
        } else if (goolgePos == 2) {
            googleButtonStyle(socialButton2, socialText2, socialImage2);
        } else if (goolgePos == 3) {
            googleButtonStyle(socialButton3, socialText3, socialImage3);
        }

        if (facebookPos == 1) {
            facebookButtonStyle(socialButton1, socialText1, socialImage1);
        } else if (facebookPos == 2) {
            facebookButtonStyle(socialButton2, socialText2, socialImage2);
        } else if (facebookPos == 3) {
            facebookButtonStyle(socialButton3, socialText3, socialImage3);
        }

        if (isOtpEnabled) {
            if (socialLoginSignUp.size() == 3)
                otpButtonStyle(socialButton4, socialText4, socialImage4);
            else if (socialLoginSignUp.size() == 2) {
                otpButtonStyle(socialButton3, socialText3, socialImage3);
                socialButton4.setVisibility(View.GONE);
            } else if (socialLoginSignUp.size() == 1) {
                otpButtonStyle(socialButton2, socialText2, socialImage3);
                socialButton3.setVisibility(View.GONE);
                socialButton4.setVisibility(View.GONE);
            } else if (socialLoginSignUp.size() == 0) {
                otpButtonStyle(socialButton1, socialText1, socialImage1);
                socialButton2.setVisibility(View.GONE);
                socialButton3.setVisibility(View.GONE);
                socialButton4.setVisibility(View.GONE);
            } else if (socialLoginSignUp.size() == 0) {
                socialLoginContainer.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            }
        } else {
            if (socialLoginSignUp.size() == 3)
                socialButton4.setVisibility(View.GONE);
            else if (socialLoginSignUp.size() == 2) {
                socialButton3.setVisibility(View.GONE);
                socialButton4.setVisibility(View.GONE);
            } else if (socialLoginSignUp.size() == 1) {
                socialButton2.setVisibility(View.GONE);
                socialButton3.setVisibility(View.GONE);
                socialButton4.setVisibility(View.GONE);
            } else if (socialLoginSignUp.size() == 0) {
                socialLoginContainer.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            }
        }
        setSocialButton1and2Style();
        setSocialButton3and4Style();
    }

    private void tveButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, appCMSPresenter.getBrandPrimaryCtaColor());
        if (metadataMap != null && metadataMap.getLoginTveCta() != null)
            buttonName.setText(metadataMap.getLoginTveCta());
        else
            buttonName.setText(getString(R.string.app_cms_tve_log_in));
        buttonName.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
        buttonImage.setImageResource(R.drawable.login_tve);
        buttonImage.setColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(), PorterDuff.Mode.SRC_ATOP);
    }

    private void otpButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        if (metadataMap != null && metadataMap.getMobileLoginCta() != null)
            buttonName.setText(metadataMap.getMobileLoginCta());
        else buttonName.setText(getString(R.string.mobile));
        buttonName.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        buttonImage.setImageResource(R.drawable.login_mobile);
    }

    private void facebookButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, ContextCompat.getColor(getContext(), R.color.facebookBlue));
        if (metadataMap != null) {
            if (metadataMap.getFacebookLoginCta() != null)
                buttonName.setText(metadataMap.getFacebookLoginCta());
        } else {
            buttonName.setText(getString(R.string.login_facebook));
        }
        buttonName.setTextColor(ContextCompat.getColor(getContext(), R.color.facebookBlue));
        buttonImage.setImageResource(R.drawable.login_facebook);

    }

    private void googleButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, ContextCompat.getColor(getContext(), R.color.googleRed));
        if (metadataMap != null) {
            if (metadataMap.getGoogleSignInCta() != null)
                buttonName.setText(metadataMap.getGoogleSignInCta());
        } else {
            buttonName.setText(getString(R.string.login_google));
        }
        buttonName.setTextColor(ContextCompat.getColor(getContext(), R.color.googleRed));
        buttonImage.setImageResource(R.drawable.login_google);
    }


    @OnClick(R.id.signInButton)
    void signInClick() {
        if (mobileFieldsContainer.getVisibility() == View.VISIBLE) {
            otpLogin();
        } else {
            appCMSPresenter.phoneObjectRequest.setFromVerify(false);
            if (terms.getVisibility() == View.VISIBLE) {
                if (terms.isChecked())
                    callAction(email.getText().toString().trim(), password.getText().toString().trim(),
                            false, getString(R.string.app_cms_action_login_key));
            } else
                callAction(email.getText().toString().trim(), password.getText().toString().trim(),
                        false, getString(R.string.app_cms_action_login_key));
        }
    }

    @OnCheckedChanged(R.id.terms)
    void termsCheck(boolean checked) {
        if (checked) {
            signInButton.setEnabled(true);
            signInButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        } else {
            signInButton.setEnabled(false);
            signInButton.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        }

    }

    @OnClick(R.id.socialButton1)
    void socialButton1Click() {
        if (facebookPos == 1) {
            callAction(null, null, false, getString(R.string.app_cms_action_loginfacebook_key));
        } else if (goolgePos == 1) {
            callAction(null, null, false, getString(R.string.app_cms_action_loginfacebook_key));
        } else if (tvePos == 1) {
            appCMSPresenter.openTvProviderScreen();
        } else
            showOtpField();

    }

    @OnClick(R.id.socialButton2)
    void socialButton2Click() {
        if (facebookPos == 2) {
            callAction(null, null, false, getString(R.string.app_cms_action_loginfacebook_key));
        } else if (goolgePos == 2) {
            callAction(null, null, false, getString(R.string.app_cms_action_logingoogle_key));
        } else if (tvePos == 2) {
            appCMSPresenter.openTvProviderScreen();
        } else
            showOtpField();
    }

    @OnClick(R.id.socialButton3)
    void socialButton3Click() {
        if (facebookPos == 3) {
            callAction(null, null, false, getString(R.string.app_cms_action_loginfacebook_key));
        } else if (goolgePos == 3) {
            callAction(null, null, false, getString(R.string.app_cms_action_logingoogle_key));
        } else if (tvePos == 3) {
            appCMSPresenter.openTvProviderScreen();
        } else
            showOtpField();
    }

    private void showOtpField() {
        emailFieldsContainer.setVisibility(View.GONE);
        passwordFieldsContainer.setVisibility(View.GONE);
        mobileFieldsContainer.setVisibility(View.VISIBLE);
        if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isEmailRequired())
            emailFieldsContainer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.socialButton4)
    void socialButton4Click() {
        showOtpField();
    }

    @OnClick(R.id.forgotPassword)
    void forgotPasswordClick() {
        callAction(null, null, false, getString(R.string.app_cms_action_forgotpassword_key));
    }

    private void callAction(String username, String password, boolean emailConsent, String action) {
        String[] authData = new String[3];
        authData[0] = username;
        authData[1] = password;
        authData[2] = String.valueOf(emailConsent);

        appCMSPresenter.launchButtonSelectedAction(null,
                action,
                null,
                authData,
                null,
                true,
                0,
                null);

    }

    private void otpLogin() {
        if (appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
            appCMSPresenter.showLoadingDialog(true);
            String phoneFormatted = "+" + selectedCountryCode + mobile.getText().toString();
            appCMSPresenter.phoneObjectRequest.setPhone(phoneFormatted);
            if (emailFieldsContainer.getVisibility() == View.VISIBLE)
                appCMSPresenter.phoneObjectRequest.setEmail(email.getText().toString());
            appCMSPresenter.phoneObjectRequest.setRequestType("send");
            appCMSPresenter.phoneObjectRequest.setMetadataMap(metadataMap);
            appCMSPresenter.phoneObjectRequest.setScreenName("login");
            appCMSPresenter.phoneObjectRequest.setFromVerify(true);
            appCMSPresenter.sendPhoneOTP(appCMSPresenter.getPhoneObjectRequest(), null);

        }
    }

    private void setSocialButton1and2Style() {
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        if (socialButton1.getVisibility() == View.GONE)
            socialButton2.setLayoutParams(params);
        else if (socialButton2.getVisibility() == View.GONE)
            socialButton1.setLayoutParams(params);
    }

    private void setSocialButton3and4Style() {
        LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        if (socialButton3.getVisibility() == View.GONE)
            socialButton4.setLayoutParams(params);
        else if (socialButton4.getVisibility() == View.GONE)
            socialButton3.setLayoutParams(params);
    }
}

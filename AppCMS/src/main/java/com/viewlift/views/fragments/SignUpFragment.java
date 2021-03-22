package com.viewlift.views.fragments;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.style.ClickableSpan;
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

import com.google.android.material.textfield.TextInputLayout;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.api.AppCMSEmailConsentValue;
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
import java.util.Map;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    @BindView(R.id.nativeSignupContainer)
    ConstraintLayout nativeSignupContainer;
    @BindView(R.id.socialSignupContainer)
    ConstraintLayout socialSignupContainer;
    @BindView(R.id.passwordContainer)
    ConstraintLayout passwordContainer;
    @BindView(R.id.emailContainer)
    ConstraintLayout emailContainer;
    @BindView(R.id.mobileContainer)
    ConstraintLayout mobileContainer;
    @BindView(R.id.emailFieldsContainer)
    ConstraintLayout emailFieldsContainer;
    @BindView(R.id.mobileFieldsContainer)
    ConstraintLayout mobileFieldsContainer;
    @BindView(R.id.passwordFieldsContainer)
    ConstraintLayout passwordFieldsContainer;
    @BindView(R.id.emailTitle)
    AppCompatTextView emailTitle;
    @BindView(R.id.email)
    AppCompatEditText email;
    @BindView(R.id.passwordTitle)
    AppCompatTextView passwordTitle;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.passwordInputLayout)
    TextInputLayout passwordInputLayout;
    @BindView(R.id.mobileTitle)
    AppCompatTextView mobileTitle;
    @BindView(R.id.countryCode)
    AppCompatSpinner countryCode;
    @BindView(R.id.countryCodeContainer)
    ConstraintLayout countryCodeContainer;
    @BindView(R.id.mobile)
    AppCompatEditText mobile;
    @BindView(R.id.signUpButton)
    AppCompatButton signUpButton;
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

    @BindView(R.id.whatsAppConsent)
    AppCompatCheckBox whatsAppConsent;
    @BindView(R.id.emailConsent)
    AppCompatCheckBox emailConsent;
    @BindView(R.id.termsText)
    AppCompatTextView termsText;
    @Inject
    AppCMSPresenter appCMSPresenter;
    private AppCMSBinder appCMSBinder;
    private ArrayList<String> socialLoginSignUp = new ArrayList<>();
    private final String SOCIAL_MEDIA_FACEBOOK = "Facebook".toLowerCase();
    private final String SOCIAL_MEDIA_GOOGLE = "Google".toLowerCase();
    private final String SOCIAL_MEDIA_TVE = "TVE".toLowerCase();
    private final String SOCIAL_MEDIA_OTP = "OTP".toLowerCase();
    private int facebookPos = 0;
    private int goolgePos = 0;
    private int tvePos = 0;
    private int otpPos = 0;
    private MetadataMap metadataMap = null;
    private boolean isOtpEnabled;
    private boolean isOtpEmailEnabled;
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;
    String selectedCountryCode;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        if (appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null)
            email.setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail());
        appCMSPresenter.noSpaceInEditTextFilter(password, getActivity());
        appCMSPresenter.setCursorDrawableColor(password, 0);
        handleModuleSettings();
        setViewStyles();
        setFont();
        setCountryCodeSpinner();
        setEmailConsent();
        if (whatsAppConsent != null) {
            appCMSPresenter.handleWhatsAppConsent(whatsAppConsent);
        }
    }

    private void setFont() {
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), email);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), emailTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), password);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), passwordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), mobile);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), mobileTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), termsText);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), signUpButton);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), orLabel);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText1);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText2);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText3);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), socialText4);
    }

    private void setEmailConsent() {
        String countryCode = Utils.getCountryCode();
        appCMSPresenter.getEmailConsentAPI(appCMSEmailConsentValueMap -> {
            if (appCMSEmailConsentValueMap != null && appCMSEmailConsentValueMap.size() > 0) {
                if (appCMSEmailConsentValueMap.keySet().contains(countryCode)) {
                    for (Map.Entry<String, AppCMSEmailConsentValue> entry : appCMSEmailConsentValueMap.entrySet()) {
                        if (entry.getKey().equalsIgnoreCase(countryCode)) {
                            AppCMSEmailConsentValue appCMSEmailConsentValue = entry.getValue();
                            if (appCMSEmailConsentValue.isVisible()) {
                                emailConsent.setText(appCMSEmailConsentValue.getCopy());
                                emailConsent.setChecked(appCMSEmailConsentValue.isChecked());
                                emailConsent.setVisibility(View.VISIBLE);
                            } else {
                                emailConsent.setVisibility(View.GONE);
                            }
                            break;
                        }
                    }
                } else {
                    AppCMSEmailConsentValue defaultAppCMSEmailConsentValue =
                            appCMSEmailConsentValueMap.get("default");
                    if (defaultAppCMSEmailConsentValue != null
                            && defaultAppCMSEmailConsentValue.isVisible()) {
                        emailConsent.setText(defaultAppCMSEmailConsentValue.getCopy());
                        emailConsent.setChecked(defaultAppCMSEmailConsentValue.isChecked());
                        emailConsent.setVisibility(View.VISIBLE);
                    } else {
                        emailConsent.setVisibility(View.GONE);
                    }
                }
            } else {
                emailConsent.setVisibility(View.GONE);
            }
        });
    }

    private void setViewStyles() {
        int appTextColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        int appBackgroundColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {appCMSPresenter.getBrandPrimaryCtaColor(), appTextColor};
        CompoundButtonCompat.setButtonTintList(emailConsent, new ColorStateList(states, colors));
        signUpButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        signUpButton.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
        parentLayout.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, emailContainer, rectangleStroke, appTextColor);
        email.setTextColor(appTextColor);
        email.setHintTextColor(appTextColor);
        emailTitle.setTextColor(appTextColor);
        emailTitle.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, passwordContainer, rectangleStroke, appTextColor);
        password.setTextColor(appTextColor);
        password.setHintTextColor(appTextColor);
        passwordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
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
        emailConsent.setTextColor(appTextColor);
        termsText.setTextColor(appTextColor);
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
                /*if (moduleList.getSettings().isEmailConsent()) {
                    emailConsent.setVisibility(View.VISIBLE);
                    emailConsent.setText(moduleList.getSettings().getDescription());
                }*/

                if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                        appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
                    isOtpEnabled = true;
                    isOtpEmailEnabled = moduleList.getSettings().isEmailRequiredWithOTP();
                }

                if (moduleList.getSettings().isShowSignupAgreementText()) {
                    termsText.setVisibility(View.VISIBLE);
//                    signUpButton.setEnabled(false);
                    signUpButton.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
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

    private void handleEmailLoginSignupVisibility(ArrayList<LoginSignup> emailLoginSignupOptions) {
        for (int i = 0; i < emailLoginSignupOptions.size(); i++) {
            LoginSignup loginSignup = emailLoginSignupOptions.get(i);
            if (loginSignup.getTitle().toLowerCase().equalsIgnoreCase("Signup".toLowerCase()) && !loginSignup.isEnable()) {
                nativeSignupContainer.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            }
        }
    }

    private void setTextToViews() {
        if (metadataMap != null) {
            if (metadataMap.getSignUpCtaText() != null)
                signUpButton.setText(metadataMap.getSignUpCtaText());
            if (metadataMap.getPasswordInput() != null) {
                password.setHint(metadataMap.getPasswordInput());
            }
            if (metadataMap.getEmailInput() != null) {
                email.setHint(metadataMap.getEmailInput());
            }

            if (metadataMap.getSignUpTermsAgreementLabel() != null && metadataMap.getPrivacyPolicyLabel() != null && metadataMap.getTermsOfUseLabel() != null) {
                StringBuilder agreement = new StringBuilder();
                agreement.append(metadataMap.getSignUpTermsAgreementLabel());
                agreement.append(metadataMap.getTermsOfUseLabel());
                agreement.append(appCMSPresenter.getLocalisedStrings().getAndLabel());
                agreement.append(metadataMap.getPrivacyPolicyLabel());
                termsText.setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
                termsText.setText(agreement.toString());
                ClickableSpan tosClick = new ClickableSpan() {
                    @Override
                    public void onClick(@Nonnull View view) {
                        appCMSPresenter.navigatToTOSPage(null, null);
                    }
                };
                ClickableSpan privacyClick = new ClickableSpan() {
                    @Override
                    public void onClick(@Nonnull View view) {
                        appCMSPresenter.navigateToPrivacyPolicy(null, null);
                    }
                };

                appCMSPresenter.makeTextViewLinks(termsText, new String[]{
                        metadataMap.getPrivacyPolicyLabel(), metadataMap.getTermsOfUseLabel()}, new ClickableSpan[]{tosClick, privacyClick}, true);
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
                socialSignupContainer.setVisibility(View.GONE);
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
                socialSignupContainer.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            }
        }
        setSocialButton1and2Style();
        setSocialButton3and4Style();
    }

    private void tveButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, appCMSPresenter.getBrandPrimaryCtaColor());
        if (metadataMap != null && metadataMap.getProvidersignUpTveCta() != null)
            buttonName.setText(metadataMap.getProvidersignUpTveCta());
        else buttonName.setText(getString(R.string.app_cms_tve_log_in));
        buttonName.setTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
        buttonImage.setImageResource(R.drawable.login_tve);
        buttonImage.setColorFilter(appCMSPresenter.getBrandPrimaryCtaColor(), PorterDuff.Mode.SRC_ATOP);
    }

    private void otpButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        if (metadataMap != null && metadataMap.getMobileSignupCta() != null)
            buttonName.setText(metadataMap.getMobileSignupCta());
        else
            buttonName.setText(getString(R.string.mobile));
        buttonImage.setImageResource(R.drawable.login_mobile);
        buttonName.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
    }

    private void facebookButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, ContextCompat.getColor(getContext(), R.color.facebookBlue));
        if (metadataMap != null) {
            if (metadataMap.getFacebookSignUpCta() != null)
                buttonName.setText(metadataMap.getFacebookSignUpCta());
        } else {
            buttonName.setText(getString(R.string.signup_facebook));
        }
        buttonName.setTextColor(ContextCompat.getColor(getContext(), R.color.facebookBlue));
        buttonImage.setImageResource(R.drawable.login_facebook);

    }

    private void googleButtonStyle(ConstraintLayout buttonLayout, AppCompatTextView buttonName, AppCompatImageView buttonImage) {
        CustomShape.makeRoundCorner(ContextCompat.getColor(getContext(), android.R.color.transparent), 7, buttonLayout, 2, ContextCompat.getColor(getContext(), R.color.googleRed));
        if (metadataMap != null) {
            if (metadataMap.getGoogleSignUpCta() != null)
                buttonName.setText(metadataMap.getGoogleSignUpCta());
        } else {
            buttonName.setText(getString(R.string.signup_google));
        }
        buttonName.setTextColor(ContextCompat.getColor(getContext(), R.color.googleRed));
        buttonImage.setImageResource(R.drawable.login_google);
    }

    @OnClick(R.id.signUpButton)
    void signupClick() {
        if (mobileFieldsContainer.getVisibility() == View.VISIBLE) {
            otpSignup();
        } else {
            appCMSPresenter.phoneObjectRequest.setFromVerify(false);
            String consent = "N";
            if (emailConsent.getVisibility() == View.VISIBLE)
                consent = "Y";
            appCMSPresenter.signUpNativeUser(email.getText().toString().trim(), password.getText().toString().trim(), consent, emailConsent.isChecked(), metadataMap, null);
        }

        /*if (terms.getVisibility() == View.VISIBLE) {
            if (terms.isChecked())
                callAction(email.getText().toString(), password.getText().toString(),
                        emailConsent.isChecked(), getString(R.string.app_cms_action_signup_key));
        } else
            callAction(email.getText().toString(), password.getText().toString(),
                    emailConsent.isChecked(), getString(R.string.app_cms_action_signup_key));*/
    }

    @OnClick(R.id.socialButton1)
    void socialButton1Click() {
        if (facebookPos == 1) {
            callAction(null, null, false, getString(R.string.app_cms_action_signupfacebook_key));
        } else if (goolgePos == 1) {
            callAction(null, null, false, getString(R.string.app_cms_action_signupgoogle_key));
        } else if (tvePos == 1) {
            appCMSPresenter.openTvProviderScreen();
        } else
            showOtpField();
    }


    @OnClick(R.id.socialButton2)
    void socialButton2Click() {
        if (facebookPos == 2) {
            callAction(null, null, false, getString(R.string.app_cms_action_signupfacebook_key));
        } else if (goolgePos == 2) {
            callAction(null, null, false, getString(R.string.app_cms_action_signupgoogle_key));
        } else if (tvePos == 2) {
            appCMSPresenter.openTvProviderScreen();
        } else
            showOtpField();
    }

    @OnClick(R.id.socialButton3)
    void socialButton3Click() {
        if (facebookPos == 3) {
            callAction(null, null, false, getString(R.string.app_cms_action_signupfacebook_key));
        } else if (goolgePos == 3) {
            callAction(null, null, false, getString(R.string.app_cms_action_signupgoogle_key));
        } else if (tvePos == 3) {
            appCMSPresenter.openTvProviderScreen();
        } else
            showOtpField();
    }

    @OnClick(R.id.socialButton4)
    void socialButton4Click() {
        showOtpField();
    }

    private void showOtpField() {
        emailFieldsContainer.setVisibility(View.GONE);
        passwordFieldsContainer.setVisibility(View.GONE);
        mobileFieldsContainer.setVisibility(View.VISIBLE);
        if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isEmailRequired())
            emailFieldsContainer.setVisibility(View.VISIBLE);
        if (isOtpEmailEnabled)
            emailFieldsContainer.setVisibility(View.VISIBLE);
    }

    private void otpSignup() {
        if (appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
            appCMSPresenter.showLoadingDialog(true);
            String phoneFormatted = "+" + selectedCountryCode + mobile.getText().toString();
            appCMSPresenter.phoneObjectRequest.setPhone(phoneFormatted);
            if (emailFieldsContainer.getVisibility() == View.VISIBLE)
                appCMSPresenter.phoneObjectRequest.setEmail(email.getText().toString());
            appCMSPresenter.phoneObjectRequest.setMetadataMap(metadataMap);
            appCMSPresenter.phoneObjectRequest.setScreenName(getString(R.string.app_cms_action_signup_key));
            appCMSPresenter.phoneObjectRequest.setRequestType("send");
            appCMSPresenter.phoneObjectRequest.setFromVerify(true);
            appCMSPresenter.phoneObjectRequest.setEmailConsent(emailConsent.isChecked());
            appCMSPresenter.phoneObjectRequest.setWhatsAppConsent(whatsAppConsent != null && whatsAppConsent.isChecked());
            if (appCMSPresenter.getSelectedPlanId() != null)
                appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.V2_SUBSCRIPTION_FLOW);
            appCMSPresenter.sendPhoneOTP(appCMSPresenter.getPhoneObjectRequest(), null);

        }
    }

    private void callAction(String username, String password, boolean emailConsent, String action) {
        String[] authData = new String[3];
        authData[0] = username;
        authData[1] = password;
        authData[2] = String.valueOf(emailConsent);
        if ((action.equals(getString(R.string.app_cms_action_signupfacebook_key)) || action.equals(getString(R.string.app_cms_action_signupgoogle_key)))
                && appCMSPresenter.getSelectedPlanId() != null)
            appCMSPresenter.setLaunchType(AppCMSPresenter.LaunchType.V2_SUBSCRIPTION_FLOW);
        appCMSPresenter.launchButtonSelectedAction(null,
                action,
                null,
                authData,
                null,
                true,
                0,
                null);
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

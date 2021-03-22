package com.viewlift.views.dialog;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.BiometricManagerUtils;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.otpView.OtpView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppCMSVerifyVideoPinDialog extends DialogFragment {

    private static final String NEED_TO_OPEN_BIOMETRIC = "needToOpenBiometric";

    @Inject
    public AppCMSPresenter appCMSPresenter;
    @Inject
    public AppPreference appPreference;
    @Inject
    LocalisedStrings localisedStrings;
    @BindView(R.id.containerView)
    ConstraintLayout containerView;
    @BindView(R.id.pinMsgTitle)
    protected TextView pinMsgTitle;
    @BindView(R.id.pinMsg)
    protected TextView pinMsg;
    @BindView(R.id.pinView)
    protected OtpView pinView;
    @BindView(R.id.confirmBtn)
    protected Button confirmBtn;
    @BindView(R.id.cancelBtn)
    protected Button cancelBtn;
    @BindView(R.id.pinError)
    protected TextView pinError;
    @BindView(R.id.pinAuthGroup)
    Group pinAuthGroup;
    @BindView(R.id.touchErrorGroup)
    Group touchErrorGroup;
    @BindView(R.id.touchErrorTxt)
    TextView touchErrorMsg;
    @BindView(R.id.usePinBtn)
    Button usePinBtn;
    @BindView(R.id.touchCancelBtn)
    Button touchCancelBtn;

    private boolean needToOpenBiometric;


    protected OnVerifyVideoPinListener listener;

    public static AppCMSVerifyVideoPinDialog newInstance(OnVerifyVideoPinListener listener, boolean needToOpenTouchId) {
        AppCMSVerifyVideoPinDialog pinDialog = new AppCMSVerifyVideoPinDialog();
        Bundle args = new Bundle();
        args.putBoolean(NEED_TO_OPEN_BIOMETRIC, needToOpenTouchId);
        pinDialog.setArguments(args);
        pinDialog.listener = listener;
        return pinDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        setCancelable(false);
        if (getArguments() != null) {
            needToOpenBiometric = getArguments().getBoolean(NEED_TO_OPEN_BIOMETRIC, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_cms_verify_video_pin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().inject(this);
        view.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        int buttonTextColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        int textColor       = appCMSPresenter.getGeneralTextColor();

        int buttonBorderColor = R.color.color_white;
        try {
            buttonBorderColor = Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getColor());
        } catch (Exception e) {
            buttonBorderColor = R.color.color_white;
        }


        cancelBtn.setTextColor(buttonTextColor);
        confirmBtn.setTextColor(buttonTextColor);
        usePinBtn.setTextColor(buttonTextColor);
        touchCancelBtn.setTextColor(buttonTextColor);

        Component component=new Component();
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, cancelBtn);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmBtn);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, usePinBtn);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, touchCancelBtn);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, pinMsg);
        component.setFontWeight(getContext().getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, pinMsgTitle);

        pinView.setItemCount(4);
        pinView.setLineColor(buttonBorderColor);
        pinView.setTextColor(textColor);
        pinView.setCursorColor(textColor);
        pinView.setMaskingChar("*");
        pinView.setBackgroundColor(Color.TRANSPARENT);

        pinMsgTitle.setTextColor(textColor);
        pinMsgTitle.setText(localisedStrings.getViewingRestrictionsEnabledText());
        pinMsg.setText(localisedStrings.getEnterVideoPinText());
        confirmBtn.setText(localisedStrings.getConfirmCTAText());
        cancelBtn.setText(localisedStrings.getCancelText());
        touchCancelBtn.setText(localisedStrings.getCancelText());
        usePinBtn.setText(localisedStrings.getUsePinText());
        pinError.setText(localisedStrings.getInCorrectPinMessageText());
        touchErrorMsg.setText(localisedStrings.getInCorrectPinMessageText());

        if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
            CommonUtils.applyBorderToComponent(confirmBtn, 1, buttonBorderColor);
            CommonUtils.applyBorderToComponent(cancelBtn, 1, buttonBorderColor);
            CommonUtils.applyBorderToComponent(usePinBtn, 1, buttonBorderColor);
            CommonUtils.applyBorderToComponent(touchCancelBtn, 1, buttonBorderColor);
            pinView.setOnTouchListener((v, event) -> {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    appCMSPresenter.showSoftKeyboard(v);
                }
                return false;
            });

            touchCancelBtn.setOnClickListener(v -> {
                if(listener != null)
                    listener.onVerifyVideoPin(false);
                dismissAllowingStateLoss();
            });

            usePinBtn.setOnClickListener(v -> {
                pinAuthGroup.setVisibility(View.VISIBLE);
                touchErrorGroup.setVisibility(View.GONE);
            });


        }

        confirmBtn.setOnClickListener(v -> {
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID)
                appCMSPresenter.closeSoftKeyboardNoView();
            if (appPreference != null) {
                String pin = appPreference.getParentalPin();
                if (pinView.getText() != null && pinView.getText().toString().equalsIgnoreCase(pin) && listener != null) {
                    listener.onVerifyVideoPin(true);
                    dismissAllowingStateLoss();
                } else {
                    pinError.setVisibility(View.VISIBLE);
                }
            }
        });

        cancelBtn.setOnClickListener(v -> {
            if(listener != null)
                listener.onVerifyVideoPin(false);
            dismissAllowingStateLoss();
        });

        if (needToOpenBiometric && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext() != null
                && appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.ANDROID) {
            BiometricManagerUtils biometricManagerUtils = new BiometricManagerUtils(getContext());
            if (biometricManagerUtils.isHardwareDetected() && biometricManagerUtils.hasEnrolledFingerprints() && biometricManagerUtils.canAuthenticate()
                    && appPreference.isBiometricPinEnable() && !TextUtils.isEmpty(appPreference.getParentalPin())) {
                biometricManagerUtils.setAuthenticationCallback(this::onResult);
                containerView.setVisibility(View.GONE);
                biometricManagerUtils.startAuth(this, localisedStrings);
            } else {
                showPinView();
            }
        } else {
            showPinView();
        }
    }

    private void showPinView() {
        containerView.setVisibility(View.VISIBLE);
    }

    private void onResult(BiometricManagerUtils.AuthStatus status, String errMsg) {
        switch (status) {
            case NEGATIVE_BUTTON: showPinView(); break;
            case USER_CANCELED: {
                if (listener != null)
                    listener.onVerifyVideoPin(false);
                dismissAllowingStateLoss();
            }
            break;
            case SUCCESS: {
                if (listener != null)
                    listener.onVerifyVideoPin(true);
                dismissAllowingStateLoss();
            }
            break;
            case OTHER: {
                showTouchMaximumFailureView(errMsg);
            }
        }
    }

    private void showTouchMaximumFailureView(String errMsg) {
        touchErrorMsg.setText(errMsg);
        pinAuthGroup.setVisibility(View.INVISIBLE);
        touchErrorGroup.setVisibility(View.VISIBLE);
        containerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnVerifyVideoPinListener {
        void onVerifyVideoPin(boolean isVerified);
    }
}

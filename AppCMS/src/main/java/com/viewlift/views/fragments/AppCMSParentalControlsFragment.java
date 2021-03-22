package com.viewlift.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.BiometricManagerUtils;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppCMSParentalControlsFragment extends Fragment {

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    AppPreference appPreference;
    @Inject
    LocalisedStrings localisedStrings;

    private Module module;

    @BindView(R.id.containerView)
    ConstraintLayout containerView;
    @BindView(R.id.pageTitle)
    TextView pageTitle;
    @BindView(R.id.viewingRestrictionsButton)
    Button viewingRestrictionsButton;
    @BindView(R.id.changeVideoPinButton)
    Button changeVideoPinButton;
    @BindView(R.id.biometricIdBtn)
    Button biometricIdBtn;
    @BindView(R.id.biometricIdSwitch)
    Switch biometricIdSwitch;
    @BindView(R.id.biometricGroup)
    Group biometricGroup;
    @BindView(R.id.biometricIdMsg)
    TextView biometricIdMsg;
    private BiometricManagerUtils biometricManagerUtils;

    public static AppCMSParentalControlsFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSParentalControlsFragment fragment = new AppCMSParentalControlsFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_cmsparental_controls, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        int bgColor        = appCMSPresenter.getGeneralBackgroundColor();
        int textColor      = appCMSPresenter.getGeneralTextColor();

        containerView.setBackgroundColor(bgColor);
        pageTitle.setTextColor(textColor);
        viewingRestrictionsButton.setTextColor(textColor);
        changeVideoPinButton.setTextColor(textColor);
        biometricIdMsg.setTextColor(textColor);

        Bundle args = getArguments();
        try {
            if (args != null && getContext() != null) {
                AppCMSBinder appCMSBinder = ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
                if (appCMSPresenter != null && appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null) {
                    module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_01)), appCMSBinder.getAppCMSPageAPI());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLocalisedStrings();
        appCMSPresenter.setEnableBiometric(false);
        new Handler().postDelayed(this::checkFingerPrints, 100);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void checkFingerPrints() {
        if (this.isAdded()&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && appCMSPresenter.getCurrentContext() != null) {
            biometricManagerUtils = new BiometricManagerUtils(appCMSPresenter.getCurrentContext());
            if (biometricManagerUtils.isHardwareDetected()) {
                ColorStateList colorStateList = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}},
                        new int[]{appCMSPresenter.getBrandPrimaryCtaColor(), appCMSPresenter.getBrandSecondaryCtaTextColor()});
                if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getEnableTouchId() != null)
                    biometricIdBtn.setText(module.getMetadataMap().getEnableTouchId());
                else
                    biometricIdBtn.setText(getString(R.string.enable_touch_id));

                if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getEnableTouchIdMessage() != null)
                    biometricIdMsg.setText(module.getMetadataMap().getEnableTouchIdMessage());
                else
                    biometricIdMsg.setText(getString(R.string.enable_touch_id_message));
                biometricGroup.setVisibility(View.VISIBLE);
                biometricIdSwitch.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
                biometricIdSwitch.setThumbTintList(colorStateList);
                if (biometricManagerUtils.hasEnrolledFingerprints() && biometricManagerUtils.canAuthenticate()
                        && appPreference.isBiometricPinEnable() && !TextUtils.isEmpty(appPreference.getParentalPin())) {
                    biometricIdSwitch.setChecked(true);
                } else {
                    biometricIdSwitch.setChecked(false);
                }
                biometricIdSwitch.setOnTouchListener((v, event) -> {
                    if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                        onSwitchChangeListener(!biometricIdSwitch.isChecked());
                        return true;
                    }
                    return false;
                });
                biometricIdSwitch.invalidate();
            } else {
                biometricGroup.setVisibility(View.GONE);
            }
        } else {
            biometricGroup.setVisibility(View.GONE);
        }
    }

    private void setLocalisedStrings() {
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getParentalControlHeader() != null)
            pageTitle.setText(module.getMetadataMap().getParentalControlHeader());
        else
            pageTitle.setText(getString(R.string.viewing_restrictions_cta));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getViewingRestrictionsCTA() != null)
            viewingRestrictionsButton.setText(module.getMetadataMap().getViewingRestrictionsCTA());
        else
            viewingRestrictionsButton.setText(getString(R.string.viewing_restrictions_cta));

        if (appPreference != null && !TextUtils.isEmpty(appPreference.getParentalPin())) {
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getResetPinCTA() != null)
                changeVideoPinButton.setText(module.getMetadataMap().getResetPinCTA());
            else
                changeVideoPinButton.setText(getString(R.string.reset_pin_cta));
        } else {
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getSetupPinCTA() != null)
                changeVideoPinButton.setText(module.getMetadataMap().getSetupPinCTA());
            else
                changeVideoPinButton.setText(getString(R.string.setup_pin_cta));
        }
    }

    @OnClick(R.id.viewingRestrictionsButton)
    void onClickViewingRestrictions() {
        if (appCMSPresenter != null) {
            appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_launchViewingRestrictionsPage),
                    null, null, null, false, -1, null);
        }
    }

    @OnClick(R.id.changeVideoPinButton)
    void onClickChangeVideoPinButton() {
        if (appCMSPresenter != null) {
            appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_launchChangeVideoPinPage),
                    null, null, null, false, -1, null);
        }
    }


    private void onSwitchChangeListener(boolean isChecked) {
        if (isChecked) {
            if (biometricManagerUtils != null && biometricManagerUtils.hasEnrolledFingerprints() && biometricManagerUtils.canAuthenticate()) {
                if (TextUtils.isEmpty(appPreference.getParentalPin())) {
                    appCMSPresenter.setEnableBiometric(true);
                    appCMSPresenter.navigateToChangeVideoPinPage();
                } else {
                    if (getActivity() != null) {
                        AppCMSVerifyVideoPinDialog.newInstance(isVerified -> {
                            if (isVerified) {
                                biometricIdSwitch.setChecked(true);
                                appPreference.setBiometricPinEnable(true);
                            } else {
                                disableChecked();
                            }
                        }, false).show(getActivity().getSupportFragmentManager(), AppCMSVerifyVideoPinDialog.class.getSimpleName());
                    }
                }
            } else {
                disableChecked();
                String touchIdNotEnrolled = getString(R.string.touch_id_not_enrolled);
                if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getTouchIdNotEnrolled() != null)
                    touchIdNotEnrolled = module.getMetadataMap().getTouchIdNotEnrolled();
                appCMSPresenter.showToast(touchIdNotEnrolled, Toast.LENGTH_LONG);
            }
        } else
            disableChecked();
    }

    private void disableChecked() {
        biometricIdSwitch.setChecked(false);
        appPreference.setBiometricPinEnable(false);
    }
}

package com.viewlift.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;

public class BiometricManagerUtils extends BiometricPrompt.AuthenticationCallback {

    private Context context;
    private BiometricManager biometricManager;
    private BiometricAuthenticationCallback callback;

    public enum AuthStatus { SUCCESS, FAILED, NEGATIVE_BUTTON, USER_CANCELED, OTHER}
    
    public BiometricManagerUtils(Context context) {
        this.context     = context;
        biometricManager = BiometricManager.from(context);
    }

    public boolean isHardwareDetected() {
        return biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE;
    }

    public boolean hasEnrolledFingerprints() {
        return biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED;
    }

    public boolean canAuthenticate() {
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        notifyResult(AuthStatus.SUCCESS, result.toString());
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        notifyResult(AuthStatus.FAILED, "FAILED");
    }

    @SuppressLint("SwitchIntDef")
    @Override
    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        switch (errorCode) {
            case BiometricPrompt.ERROR_NEGATIVE_BUTTON : notifyResult(AuthStatus.NEGATIVE_BUTTON, errString.toString()); break;
            case BiometricPrompt.ERROR_USER_CANCELED   : notifyResult(AuthStatus.USER_CANCELED, errString.toString());   break;
            default: notifyResult(AuthStatus.OTHER, errString.toString()); break;
        }
    }

    public void setAuthenticationCallback(BiometricAuthenticationCallback callback) {
        this.callback = callback;
    }

    public void startAuth(Fragment fragment, LocalisedStrings localisedStrings) {
        BiometricPrompt biometricPrompt = new BiometricPrompt(fragment, ContextCompat.getMainExecutor(context), this);
        biometricPrompt.authenticate(new BiometricPrompt.PromptInfo.Builder()
                .setTitle(localisedStrings.getTouchIdText())
                .setSubtitle(localisedStrings.getTouchIdEnabledTitle())
                .setNegativeButtonText(localisedStrings.getUsePinText())
                .setConfirmationRequired(true)
                .build());
    }


    private void notifyResult(AuthStatus status, String errMsg) {
        if (callback != null)
            callback.onAuthenticationSuccess(status, errMsg);
    }

    public interface BiometricAuthenticationCallback {
        void onAuthenticationSuccess(AuthStatus status, String errMsg);
    }
}

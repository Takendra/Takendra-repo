package com.viewlift.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import static android.content.Context.FINGERPRINT_SERVICE;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintUtils extends FingerprintManager.AuthenticationCallback {

    private FingerprintManager fingerprintManager;

    private BiometricAuthenticationCallback callback;

    public FingerprintUtils(Context context) {
        fingerprintManager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
    }

    public boolean isHardwareDetected() {
        return fingerprintManager.isHardwareDetected();
    }

    public boolean hasEnrolledFingerprints() {
        return fingerprintManager.hasEnrolledFingerprints();
    }

    public void startAuth() {
        fingerprintManager.authenticate(null, null, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        notifyResult(false);
    }

    @Override
    public void onAuthenticationFailed() {
        notifyResult(false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        notifyResult(false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        notifyResult(true);
    }

    public void setAuthenticationCallback(BiometricAuthenticationCallback callback) {
        this.callback = callback;
    }

    private void notifyResult(boolean isSuccess) {
        if (callback != null)
            callback.onAuthenticationSuccess(isSuccess);
    }

    public interface BiometricAuthenticationCallback {
        void onAuthenticationSuccess(boolean isSuccess);
    }
}
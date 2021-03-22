package com.viewlift.juspay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.viewlift.AppCMSApplication;
import com.viewlift.BuildConfig;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.OrderStatus;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.ArrayList;

import in.juspay.godel.core.PaymentConstants;
import in.juspay.godel.ui.PaymentFragment;
import static in.juspay.godel.core.PaymentConstants.PAYLOAD;

public class JuspayPaymentActivity extends AppCompatActivity {

    private AppCMSPresenter appCMSPresenter;
    private AppPreference appPreference;
    private PaymentFragment fragment;
    private String merchantId;
    private String payLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setJusPayScreenOrientation(this);
        setContentView(R.layout.activity_juspay_payment);

        appCMSPresenter = ((AppCMSApplication) getApplicationContext()).getAppCMSPresenterComponent().appCMSPresenter();
        appPreference = appCMSPresenter.getAppPreference();
        merchantId = appCMSPresenter.getMerchantId();
        Intent intent = getIntent();
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra(getString(R.string.app_cms_plan_id)))
                && !TextUtils.isEmpty(intent.getStringExtra(getString(R.string.juspay_payload)))
                && !TextUtils.isEmpty(merchantId)
                && appPreference != null && !TextUtils.isEmpty(appCMSPresenter.getUserPhoneNumber())) {
            payLoad = intent.getStringExtra(getString(R.string.juspay_payload));
            createOrder(intent.getStringExtra(getString(R.string.app_cms_plan_id)));
        } else {
            finish();
        }
    }

    /***
     * This method is used to set the orientation of the screen,
     * In this method  check android version and set orientation,
     * If using translucent property in style in {@link JuspayPaymentActivity} in theme{@Theme.AppCompat.Translucent}.
     * @param mContext
     */
    @SuppressLint("SourceLockedOrientationActivity")
    private void setJusPayScreenOrientation(Activity mContext) {
        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void createOrder(String planId) {
        if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.TVOD_PURCHASE) {
            appCMSPresenter.createJusPayOrderTVOD(response -> {
                if (response.getStatus() != null && response.getStatus().equals("success") && response.getDetails() != null &&
                        response.getDetails().getOrderId() != null && response.getDetails().getJuspay() != null && response.getDetails().getJuspay().getClientToken() != null) {
                    proceedToPayment(response.getDetails().getOrderId(), response.getDetails().getJuspay().getClientToken());
                } else {
                    appCMSPresenter.showToast(response.getError(), Toast.LENGTH_SHORT);
                    finish();
                }
            });
        } else {
            appCMSPresenter.createJusPayOrder(appCMSPresenter.getUserPhoneNumber(), planId, result -> {
                if (result != null && result.getSubscriptionInfo() != null && result.getSubscriptionInfo().getPaymentHandlerResponse() != null
                        && !TextUtils.isEmpty(result.getSubscriptionInfo().getPaymentHandlerResponse().getOrderId())) {
                    proceedToPayment(result.getSubscriptionInfo().getPaymentHandlerResponse().getOrderId(), result.getSubscriptionInfo().getPaymentHandlerResponse().getClientAuthToken());
                } else {
                    String msg = appCMSPresenter.getGenericMessages() != null && !TextUtils.isEmpty(appCMSPresenter.getGenericMessages().getFailMessage()) ? appCMSPresenter.getGenericMessages().getFailMessage() : getString(R.string.fail_message);
                    appCMSPresenter.showToast(msg, Toast.LENGTH_SHORT);
                    finish();
                }
            });
        }
    }

    private void proceedToPayment(String orderId, String clientToken) {
        Bundle juspayBundle = new Bundle();
        juspayBundle.putString(PaymentConstants.MERCHANT_ID, merchantId);
        juspayBundle.putString(PaymentConstants.CLIENT_ID, merchantId + "_android");
        juspayBundle.putString(PaymentConstants.ORDER_ID, orderId);
        juspayBundle.putString(PaymentConstants.CUSTOMER_ID, appPreference.getLoggedInUser());
        juspayBundle.putString(PaymentConstants.CLIENT_EMAIL, appPreference.getLoggedInUserEmail());
        juspayBundle.putString(PaymentConstants.CLIENT_MOBILE_NO, appCMSPresenter.getUserPhoneNumber());
        if (appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppCMSMain().getApiBaseUrl().contains("staging-api")) {
            juspayBundle.putString(PaymentConstants.ENV, PaymentConstants.ENVIRONMENT.SANDBOX);
        } else {
            juspayBundle.putString(PaymentConstants.ENV, PaymentConstants.ENVIRONMENT.PRODUCTION);
        }

        // Service Parameters for in.juspay.ec
        juspayBundle.putString(PaymentConstants.SERVICE, "in.juspay.ec");
        juspayBundle.putString(PaymentConstants.CLIENT_AUTH_TOKEN, clientToken);
        ArrayList<String> list = new ArrayList<>();
        list.add(appCMSPresenter.getAppCMSMain().getDomainName());
        juspayBundle.putStringArrayList(PaymentConstants.END_URLS, list);
        juspayBundle.putString(PaymentConstants.PAYLOAD, payLoad);

        fragment = new PaymentFragment();
        fragment.setArguments(juspayBundle);
        fragment.setJuspayCallback((requestCode, resultCode, intent) -> {
            removeFragment(fragment);
            fragment = null;
            onResponse(intent);
        });
        showFragment(fragment);
        if (BuildConfig.DEBUG)
            WebView.setWebContentsDebuggingEnabled(true);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(android.R.id.content, fragment)
                .commitAllowingStateLoss();
    }

    private void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(fragment)
                .commitAllowingStateLoss();
    }

    private void onResponse(Intent data) {
        Intent intent = new Intent();
        try {
            Log.d("TAG", "JuspayPaymentActivity" + data.getStringExtra(PAYLOAD));
            OrderStatus status = new Gson().fromJson(data.getStringExtra(PAYLOAD), OrderStatus.class);
            intent.putExtra(getString(R.string.status), status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onResponse(data);
    }

    @Override
    public void onBackPressed() {
        if (fragment != null && fragment.isAdded()) {
            fragment.backPressHandler(true);
        } else {
            super.onBackPressed();
        }
    }
}

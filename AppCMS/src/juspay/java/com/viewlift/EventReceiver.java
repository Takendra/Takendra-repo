package com.viewlift;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.viewlift.db.AppPreference;
import com.viewlift.juspay.JuspayPaymentActivity;
import com.viewlift.presenters.AppCMSPresenter;
import in.juspay.godel.PaymentActivity;
import static com.viewlift.presenters.AppCMSPresenter.JUSPAY_REQUEST_CODE;

public class EventReceiver extends BroadcastReceiver {

    AppCMSPresenter appCMSPresenter;

    @Override
    public void onReceive(Context context, Intent intent) {
        appCMSPresenter = ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().appCMSPresenter();
        if (intent != null && !TextUtils.isEmpty(intent.getAction()) && intent.getAction().equalsIgnoreCase(AppCMSPresenter.ACTION_LAUNCH_JUSPAY)) {
            launchJusPay(context, intent);
        }
    }

    private void launchJusPay(Context context, Intent intent) {
        if (intent != null && appCMSPresenter.getCurrentActivity() != null) {
            String planId  = intent.getStringExtra(context.getString(R.string.app_cms_plan_id));
            String payLoad = intent.getStringExtra(context.getString(R.string.juspay_payload));
            AppPreference appPreference = appCMSPresenter.getAppPreference();
            if (!TextUtils.isEmpty(planId) && !TextUtils.isEmpty(payLoad) && appPreference != null && !TextUtils.isEmpty(appPreference.getLoggedInUser())) {
                String merchantId = appCMSPresenter.getMerchantId();
                PaymentActivity.preFetch(appCMSPresenter.getCurrentActivity(), merchantId + "_android");
                Intent juspayIntent = new Intent(context, JuspayPaymentActivity.class);
                juspayIntent.putExtra(context.getString(R.string.app_cms_plan_id), planId);
                juspayIntent.putExtra(context.getString(R.string.juspay_payload), payLoad);
                appCMSPresenter.getCurrentActivity().startActivityForResult(juspayIntent, JUSPAY_REQUEST_CODE);
            }
        }
    }
}

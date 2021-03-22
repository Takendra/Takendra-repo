package com.viewlift.tv.views.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.amazon.device.iap.PurchasingService;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.analytics.AppsFlyerUtils;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.android.NavigationUser;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.iap.AppCmsPurchasingListener;
import com.viewlift.tv.iap.IapCallBacks;
import com.viewlift.tv.iap.IapManager;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.fragment.AppCmsNavigationFragment;
import com.viewlift.tv.views.fragment.RecommendationFragment;

import rx.functions.Action1;

import static com.viewlift.presenters.AppCMSPresenter.DIALOG_FRAGMENT_TAG;

/**
 * Created by nitin.tyagi on 6/27/2017.
 */

public abstract class AppCmsBaseActivity extends AppCompatActivity implements IapCallBacks {
    private static final int UPGRADE_DIALOG_REQUEST_CODE = 9876;
    AppCMSPresenter appCMSPresenter;
    private boolean isProfileFirstTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCMSPresenter = ((AppCMSApplication) getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
    }

    public void setNavigationFragment(AppCmsNavigationFragment navigationFragment) {
        getFragmentManager().beginTransaction().replace(getNavigationContainer(), navigationFragment, "nav").commit();
    }

    public abstract int getNavigationContainer();

    public boolean isProfileFirstTime() {
        return isProfileFirstTime;
    }

    public void setProfileFirstTime(boolean profileFirstTime) {
        isProfileFirstTime = profileFirstTime;
    }
    // public abstract int getSubNavigationContainer();

    protected IapManager iapManager;

    protected void setupIAPOnCreate() {
        iapManager = new IapManager(this, appCMSPresenter);
        iapManager.activate();
        AppCmsPurchasingListener purchasingListener = new AppCmsPurchasingListener(iapManager, appCMSPresenter, this);
        PurchasingService.registerListener(this.getApplicationContext(), purchasingListener);
        PurchasingService.getUserData();
        PurchasingService.getPurchaseUpdates(true);
    }

    protected void setUpIAPOnResume() {
      //  iapManager.activate();
        //  PurchasingService.getPurchaseUpdates(false);

       /* final Set<String> productSkus = new HashSet<String>();
        for (final AppCmsSku mySku : AppCmsSku.values()) {
            System.out.println("TAG , SKU information = "+mySku.getSku());
            productSkus.add(mySku.getSku());
        }
        PurchasingService.getProductData(productSkus);*/
    }


    protected String skuToPurchase = "";
    protected ContentDatum contentDatum = null;

    @Override
    public ContentDatum getContentDataForAmazonPurchase(){
        return contentDatum;
    }

    public void initiateAmazonPurchase(String sku, ContentDatum data) {
        //TODO:- 1. check login first. If user is logged in then initiate purchase.
        //TODO:- 2. If User is not logged in then then open signup screen then after sucessfully signup initiate purchase.
       /* String sku1 = sku + "_test";
        System.out.println(TAG +"initiateAmazonPurchase========="+sku1);
        PurchasingService.purchase("standard.sub.monthly");*/
        appCMSPresenter.checkForExistingSubscription(true, new Action1<String>() {
            @Override
            public void call(String s) {
                if (s.equalsIgnoreCase("NotRegistered")) {
                    skuToPurchase = sku;
                    contentDatum = data;
                    appCMSPresenter.setSignupFlag(false);
                    if (appCMSPresenter.isUserLoggedIn() && appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null) {
                        appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(
                                appCMSPresenter.IAP_AMAZON_INITIATED,
                                appCMSPresenter.AMAZON_IAP,
                                appCMSPresenter.getLoggedInUser(),
                                appCMSPresenter.getAmazonUserId(),
                                skuToPurchase,
                                "",
                                "")
                        );
                        if (!TextUtils.isEmpty(appCMSPresenter.getAppPreference().getAppsFlyerKey())) {
                            sendAppsFlyerSubscriptionInitiatedEvent();
                        }
                        appCMSPresenter.setAmazonPurchaseInitiated(false);
                        PurchasingService.purchase(sku);
                    } else {
                        //Open Sinup page here.
                        appCMSPresenter.setAmazonPurchaseInitiated(true);
                        NavigationUser navigationUser = appCMSPresenter.getSignUpNavigation();
                        appCMSPresenter.navigateToTVPage(
                                navigationUser.getPageId(),
                                navigationUser.getTitle(),
                                navigationUser.getUrl(),
                                false,
                                Uri.EMPTY,
                                false,
                                false,
                                true, false, false, false);
                    }
                }
            }
        });


    }

    @Override
    public void finalizeSignupAfterSubscription(String recieptId, String sku, String amazonIAPuserId) {
        Utils.pageLoading(true, AppCmsBaseActivity.this);
        String purchasedPlan = null;
        String currencyCode = null;
        if (contentDatum != null) {
            purchasedPlan = contentDatum.getId();
            currencyCode = contentDatum.getPlanDetails().get(0).getRecurringPaymentCurrencyCode();
        }
        appCMSPresenter.finalizeSignUpAfterSubscriptionForFireTV(recieptId, purchasedPlan, sku, currencyCode, amazonIAPuserId);
    }

    @Override
    public void consumableItemPurchased(String reciptId, String sku, String amazonIAPUserId) {
        Utils.pageLoading(true, AppCmsBaseActivity.this);
        String purchasedPlan = null;
        String currencyCode = null;
        if (contentDatum != null) {
            purchasedPlan = contentDatum.getId();
            currencyCode = contentDatum.getPlanDetails().get(0).getRecurringPaymentCurrencyCode();
        }
        appCMSPresenter.finalizeProductPurchase(0, reciptId, amazonIAPUserId);
    }

    @Override
    public void navigateToHomePage() {
        if (appCMSPresenter.isViewPlanPageOpenFromADialog()) {

            appCMSPresenter.setViewPlanPageOpenFromADialog(false);
            Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_FRAGMENT_TAG);
            if (prev != null) {
                DialogFragment df = (DialogFragment) prev;
                df.dismiss();
            }
            if (appCMSPresenter.isSignupFlag()
                    && appCMSPresenter.isPersonalizationEnabled()
                    && !appCMSPresenter.isRecommendationOnlyForSubscribedEnabled()) {
                appCMSPresenter.openRecommendationDialog(false);
            }

        } else if (appCMSPresenter.isSignupFlag()) {
            appCMSPresenter.navigateToHomePage(true);
        }

    }

    protected void sendAppsFlyerSubscriptionInitiatedEvent() {
        AppsFlyerUtils.setEventAddToCart(
                appCMSPresenter,
                appCMSPresenter.AMAZON_IAP,
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getCountryCode(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                contentDatum.getPlanDetails().get(0).getTitle(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice() - contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                Utils.getSubscriptionEndDateFromRenewalCycleType(contentDatum.getRenewalCycleType(), contentDatum.getRenewalCyclePeriodMultiplier()));

        AppsFlyerUtils.setEventSubscriptionInitiated(
                appCMSPresenter,
                appCMSPresenter.AMAZON_IAP,
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getCountryCode(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                contentDatum.getPlanDetails().get(0).getTitle(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice() - contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                Utils.getSubscriptionEndDateFromRenewalCycleType(contentDatum.getRenewalCycleType(), contentDatum.getRenewalCyclePeriodMultiplier()));
    }

    protected void sendAppsFlyerSubscriptionSuccessEvent() {
        AppsFlyerUtils.setEventSubscriptionCompleted(
                appCMSPresenter,
                appCMSPresenter.AMAZON_IAP,
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getCountryCode(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                contentDatum.getPlanDetails().get(0).getTitle(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice() - contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                Utils.getSubscriptionEndDateFromRenewalCycleType(contentDatum.getRenewalCycleType(), contentDatum.getRenewalCyclePeriodMultiplier()));
    }

    protected void sendAppsFlyerSubscriptionFailedEvent() {
        AppsFlyerUtils.setEventSubscriptionFailed(
                appCMSPresenter,
                appCMSPresenter.AMAZON_IAP,
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getCountryCode(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice(),
                contentDatum.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                contentDatum.getPlanDetails().get(0).getTitle(),
                contentDatum.getPlanDetails().get(0).getStrikeThroughPrice() - contentDatum.getPlanDetails().get(0).getRecurringPaymentAmount(),
                Utils.getSubscriptionEndDateFromRenewalCycleType(contentDatum.getRenewalCycleType(), contentDatum.getRenewalCyclePeriodMultiplier()));
    }

    public void onAmazonPurchaseSuccess() {
        sendAppsFlyerSubscriptionSuccessEvent();
    }

    public void onAmazonPurchaseFailed() {
        try {
            sendAppsFlyerSubscriptionFailedEvent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showRecommendationDialog(Intent intent) {
        if(intent != null) {
            RecommendationFragment newFragment = Utils.getRecommendationFragment(
                    this, appCMSPresenter,
                    this.getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                    this.getResources().getDimensionPixelSize(R.dimen.text_add_to_watchlist_sign_in_dialog_height),
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(this.getString(R.string.add_to_watchlist)),
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(this.getString(R.string.add_to_watchlist_dialog_text)),
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(this.getString(R.string.sign_in_text)),
                    appCMSPresenter.getLanguageResourcesFile().getUIresource(this.getString(R.string.cancel)), 14,
                    intent.getBooleanExtra(getString(R.string.tv_generic_dialog_key), false));
        }
    }

    protected void setUpUpdate() {
        showAppUpdateDialog();
    }
    private void showAppUpdateDialog() {
        String dialogMessage = appCMSPresenter.getLocalisedStrings().getAppUpdatePrefixText() +
                " " +
                appCMSPresenter.getLocalisedStrings().getAppUpdateSuffixText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(appCMSPresenter.getLocalisedStrings().getAppUpdateAvailableText())
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(appCMSPresenter.getLocalisedStrings().getAppUpdateCtaText().toUpperCase(), (dialog, id) -> {
                    Uri uri = null;
                    if (appCMSPresenter.getAppCMSMain().getAppVersions() != null) {
                        if (com.viewlift.Utils.isFireTVDevice(this)) {
                            if (appCMSPresenter.getAppCMSMain().getAppVersions().getFireTVAppVersion() != null) {
                                uri = Uri.parse(appCMSPresenter.getAppCMSMain().getAppVersions().getFireTVAppVersion().getUpdateUrl());
                            }
                        } else {
                            if (appCMSPresenter.getAppCMSMain().getAppVersions().getAndroidTVAppVersion() != null) {
                                uri = Uri.parse(appCMSPresenter.getAppCMSMain().getAppVersions().getAndroidTVAppVersion().getUpdateUrl());
                            }
                        }
                    }

                    if (uri == null) {
                        String packageName = getPackageName();
                        if (com.viewlift.Utils.isFireTVDevice(this)) {
                            uri = Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=" + packageName);
                        } else /*Android TV*/{
                            uri = Uri.parse("market://details?id=" + packageName);
                        }
                    }

                    startActivityForResult(new Intent(Intent.ACTION_VIEW, uri), UPGRADE_DIALOG_REQUEST_CODE);
                })
                .setNegativeButton(appCMSPresenter.getLocalisedStrings().getCloseText(), (dialog, id) -> {
                    if (appCMSPresenter.isAppBelowMinVersion()) {
                        finish();
                    }
                });
        builder.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (appCMSPresenter.isAppBelowMinVersion()) {
                    finish();
                }
                return true;
            }
            return false;
        });

        builder.create();
        builder.show().getButton(DialogInterface.BUTTON_POSITIVE).requestFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPGRADE_DIALOG_REQUEST_CODE && appCMSPresenter.isAppBelowMinVersion()) {
            showAppUpdateDialog();
        }
    }
}

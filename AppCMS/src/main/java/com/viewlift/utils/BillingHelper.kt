package com.viewlift.utils

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.android.billingclient.api.*
import com.viewlift.R
import com.viewlift.db.AppPreference
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.activity.AppCMSPageActivity
import com.viewlift.views.rxbus.AppBus
import rx.functions.Action0
import rx.functions.Action1
import java.util.*

/**
 * This class is for handling in app subscriptions
 *
 * @author Himanshu
 * @since 2020-04-27
 */
class BillingHelper(val appCMSPresenter: AppCMSPresenter) : PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null

    init {
        billingClient = if (appCMSPresenter.currentActivity is AppCMSPageActivity) {
            BillingClient.newBuilder(appCMSPresenter.currentContext.applicationContext)
                    .enablePendingPurchases()
                    .setListener(appCMSPresenter.currentActivity as AppCMSPageActivity)
                    .build()
        } else {
            BillingClient.newBuilder(appCMSPresenter.currentContext.applicationContext)
                    .enablePendingPurchases()
                    .setListener(this)
                    .build()
        }
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                // Logic from ServiceConnection.onServiceConnected should be moved here.
                if (isSuccess(billingResult) && appCMSPresenter.isUserLoggedIn && appCMSPresenter.isAppSVOD
                        && appCMSPresenter.appPreference != null) {
                    appCMSPresenter.checkForExistingSubscription(false)
                    onRestorePurchase(false, appCMSPresenter.appPreference)
                }
            }

            override fun onBillingServiceDisconnected() {
                // Logic from ServiceConnection.onServiceDisconnected should be moved here.
            }
        })
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        // Logic from onActivityResult should be moved here.
        println("onPurchasesUpdated   in BillingHelper ")
        if (purchases != null && !purchases.isEmpty() && purchases[0] != null) {
            appCMSPresenter.finalizeSignupAfterSubscription(purchases[0].originalJson)
        } else {
            showErrorDialog(billingResult.responseCode, false)
        }
    }

    fun onRestorePurchase(showErrorDialogIfSubscriptionExists: Boolean, appPreference: AppPreference) {
        try {
            if (!appCMSPresenter.isNetworkConnected) {
                return
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (billingClient != null && billingClient!!.isReady) {
            if (!appCMSPresenter.isUserSubscribed) {
                appPreference.setActiveSubscriptionPrice(null)
                appPreference.setActiveSubscriptionId(null)
                appPreference.setActiveSubscriptionSku(null)
                appPreference.setActiveSubscriptionCountryCode(null)
                appPreference.setActiveSubscriptionPlanName(null)
                appPreference.setActiveSubscriptionReceipt(null)
            }
            val purchasesResult = billingClient?.queryPurchases(BillingClient.SkuType.SUBS)
            purchasesResult?.purchasesList?.forEach { purchase ->
                val skuList: MutableList<String> = ArrayList()
                skuList.add(purchase.sku)
                val params = SkuDetailsParams.newBuilder()
                params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
                billingClient!!.querySkuDetailsAsync(params.build()) { billingResult: BillingResult, skuDetailsList: List<SkuDetails>? ->
                    if (isSuccess(billingResult) && skuDetailsList != null) {
                        for (skuDetails in skuDetailsList) {
                            appPreference.setExistingGooglePlaySubscriptionDescription(skuDetails.title)
                            appPreference.setExistingGooglePlaySubscriptionPrice(skuDetails.price)
                            appPreference.setExistingGooglePlaySubscriptionId(skuDetails.sku)
                            appPreference.setExistingGooglePlaySubscriptionPurchaseToken(purchase.purchaseToken)

                            appCMSPresenter.checkForExistingSubscription(showErrorDialogIfSubscriptionExists,
                                    purchase.originalJson,
                                    purchase.purchaseToken,
                                    skuDetails.subscriptionPeriod)
                        }
                    }
                }
                if (TextUtils.isEmpty(appCMSPresenter.skuToPurchase) || appCMSPresenter.skuToPurchase == purchase.sku) {
                    appPreference.setActiveSubscriptionReceipt(purchase.originalJson)
                } else {
                    appPreference.setActiveSubscriptionReceipt(null)
                }
            }

            appPreference.setExistingGooglePlaySubscriptionSuspended(false)

            purchasesResult?.purchasesList?.let { itemList ->
                if (itemList.size > 0 && appCMSPresenter.platformType != null && appCMSPresenter.platformType == AppCMSPresenter.PlatformType.ANDROID) {
                    appCMSPresenter.sendUAAboutLapsedUser(appPreference.getLoggedInUser())
                }
            }
        }
    }

    fun purchaseItemFromGoogleAppStore(skuToSell: String, purchaseFromRestore: Boolean) {
        if (appCMSPresenter.currentActivity != null && billingClient != null && billingClient!!.isReady) {
            val skuList: MutableList<String> = ArrayList()
            skuList.add(skuToSell)
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
            billingClient!!.querySkuDetailsAsync(params.build()) { billingResult: BillingResult, skuDetailsList: List<SkuDetails>? ->
                if (isSuccess(billingResult)) {
                    if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (skuDetails in skuDetailsList) {
                            if (skuToSell.equals(skuDetails.sku, ignoreCase = true)) {
                                if (appCMSPresenter.appPreference.getExistingGooglePlaySubscriptionId() != null
                                        && appCMSPresenter.appPreference.getExistingGooglePlaySubscriptionPurchaseToken() != null) {
                                    lanchBillingFlowModification(skuDetails,
                                            appCMSPresenter.appPreference.getExistingGooglePlaySubscriptionId()!!,
                                            appCMSPresenter.appPreference.getExistingGooglePlaySubscriptionPurchaseToken()!!)
                                } else {
                                    launchBillingFlow(skuDetails)
                                }
                                break
                            }
                        }
                    } else {
                        appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.localisedStrings.itemUnavailableMsg,
                                false, Action0 { appCMSPresenter.stopLoader() }, null, appCMSPresenter.localisedStrings.errorTitle)
                        return@querySkuDetailsAsync
//                        appCMSPresenter.showToast(appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.app_cms_cancel_subscription_subscription_not_valid_message)),
//                                Toast.LENGTH_LONG)
                    }
                } else {
                    showErrorDialog(billingResult.responseCode, purchaseFromRestore)
                }
            }
        }
    }

    private fun lanchBillingFlowModification(skuDetailsUpgrade: SkuDetails, previousSku: String, purchaseTokenOfOriginalSubscription: String) {
        if (appCMSPresenter.currentActivity != null) {
            val flowParams = BillingFlowParams.newBuilder()
                    .setOldSku(previousSku, purchaseTokenOfOriginalSubscription)
                    .setReplaceSkusProrationMode(BillingFlowParams.ProrationMode.IMMEDIATE_WITHOUT_PRORATION)
                    .setSkuDetails(skuDetailsUpgrade)
                    .setObfuscatedAccountId(appCMSPresenter.appPreference.getLoggedInUser()!!)
                    .build();
            val responseCode = billingClient!!.launchBillingFlow(appCMSPresenter.currentActivity, flowParams)
        }
    }

    private fun launchBillingFlow(skuDetails: SkuDetails) {
        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        if (appCMSPresenter.currentActivity != null) {
            billingClient!!.launchBillingFlow(appCMSPresenter.currentActivity, BillingFlowParams.newBuilder()
                    .setObfuscatedAccountId(appCMSPresenter.appPreference.getLoggedInUser()!!)
                    .setSkuDetails(skuDetails)
                    .build())
        }
    }

    private fun isSuccess(billingResult: BillingResult): Boolean {
        return billingResult.responseCode == BillingClient.BillingResponseCode.OK
    }

    fun showErrorDialog(resultCode: Int, purchaseFromRestore: Boolean) {
        if (appCMSPresenter.currentActivity == null || appCMSPresenter.localisedStrings == null) {
            return
        }
        if (resultCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_user_canceled)), false, null, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        } else if (resultCode == BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_service_unavailable)), false, null, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        } else if (resultCode == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE) {
            appCMSPresenter.addGoogleAccountToDevice()
        } else if (resultCode == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_item_unavailable)), false, null, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        } else if (resultCode == BillingClient.BillingResponseCode.DEVELOPER_ERROR) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_developer_error)), false, null, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        } else if (resultCode == BillingClient.BillingResponseCode.ERROR) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.RETRY_SUBSCRIPTION, appCMSPresenter.localisedStrings.billingResponseErrorText, true,
                    Action0 {
                        try {
                            appCMSPresenter.initiateItemPurchase(purchaseFromRestore)
                        } catch (e: Exception) {
                            //Log.e(TAG, "Error handling request permissions result: " + e.getMessage());
                        }
                    }, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        } else if (resultCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_item_already_purchased)), false, null, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        } else if (resultCode == BillingClient.BillingResponseCode.ITEM_NOT_OWNED) {
            appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.languageResourcesFile.getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_item_not_owned)), false, null, null, appCMSPresenter.localisedStrings.subscriptionMsgHeaderText)
        }
    }

    fun getSubsSKUDetail(sku: String, skuDetailsAction1: Action1<SkuDetails?>) {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(sku)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient!!.querySkuDetailsAsync(params.build()
        ) { billingResult, skuDetailsList ->
            // Process the result.
            if (skuDetailsList != null && skuDetailsList.size > 0) {
                for (skuDetails in skuDetailsList) {
                    if (skuDetails.sku.equals(sku, ignoreCase = true)) {
                        skuDetailsAction1.call(skuDetails)
                    }
                    //System.out.println("https Name: " + skuDetails.getTitle() + " Price: " + skuDetails.getPrice() + "  Country: " + skuDetails.getPriceCurrencyCode());
                }
            } else {
                skuDetailsAction1.call(null)
            }
        }
    }

    fun onDestroy() {
        if (billingClient != null) {
            billingClient!!.endConnection()
            billingClient = null
        }
    }

    companion object {
        private var instance: BillingHelper? = null

        @JvmStatic
        fun getInstance(appCMSPresenter: AppCMSPresenter): BillingHelper? {
            if (instance == null) {
                synchronized(BillingHelper::class.java) {
                    if (instance == null) {
                        instance = BillingHelper(appCMSPresenter)
                    }
                }
            }
            return instance
        }
    }


}
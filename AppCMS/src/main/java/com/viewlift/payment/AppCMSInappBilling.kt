package com.viewlift.payment

import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.google.android.exoplayer2.util.Log
import com.viewlift.R
import com.viewlift.presenters.AppCMSPresenter
import rx.functions.Action0

class AppCMSInappBilling(val activity: AppCompatActivity, val appCMSPresenter: AppCMSPresenter) : PurchasesUpdatedListener {
    private lateinit var skuToBuy: String
    private var billingClient = BillingClient.newBuilder(activity)
            .setListener(this)
            .enablePendingPurchases()
            .build()

    /** Start the payment flow
     *  @param skuToBuy - product identfier to purchsae
     **/
    fun launchPaymentFlow(skuToBuy: String) {
        this.skuToBuy = skuToBuy
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    val skuList = ArrayList<String>()
                    skuList.add(skuToBuy)
                    val params = SkuDetailsParams.newBuilder()
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
                    billingClient.querySkuDetailsAsync(params.build()) { _: BillingResult?, skuDetailsList: List<SkuDetails?>? ->
                        if (skuDetailsList != null) {
                            if (skuDetailsList.size == 0) {
                                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.localisedStrings.itemUnavailableMsg,
                                        false, Action0 { appCMSPresenter.stopLoader() }, null, appCMSPresenter.localisedStrings.errorTitle)
                                return@querySkuDetailsAsync
                            }
                            for (sku in skuDetailsList) {
                                if (sku != null) {
                                    if (sku.sku.equals(skuToBuy, true)) {
                                        openPaymentScreen(sku)
                                    }
                                }
                            }
                        }
                    }

                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun openPaymentScreen(skuDetails: SkuDetails) {
        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .setObfuscatedAccountId(appCMSPresenter.appPreference.getLoggedInUser()!!)
                .build()
        billingClient.launchBillingFlow(activity, flowParams)
    }


    override fun onPurchasesUpdated(billingResult: BillingResult, purcahses: MutableList<Purchase>?) {
            when (billingResult.responseCode) {
                BillingClient.BillingResponseCode.OK -> {
                    purcahses?.get(0)?.purchaseToken?.let { consumeProduct(it) }
                    Handler().postDelayed({
                        purcahses?.get(0)?.originalJson.let { appCMSPresenter.finalizeProductPurchase(0, it, null) }
                    }, 400)


                }
                BillingClient.BillingResponseCode.USER_CANCELED -> {
                    appCMSPresenter.stopLoader()
//                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_user_canceled)),
//                        false, null, null, appCMSPresenter.localisedStrings.getSubscriptionMsgHeaderText())
                }
                BillingClient.BillingResponseCode.DEVELOPER_ERROR -> {
                    appCMSPresenter.stopLoader()
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_developer_error)),
                            false, null, null, appCMSPresenter.localisedStrings.getSubscriptionMsgHeaderText())
                }
                BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    appCMSPresenter.stopLoader()
                    fetchUserPurchases();
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_item_already_purchased)),
                            false, null, null, appCMSPresenter.localisedStrings.getSubscriptionMsgHeaderText())
                }
                BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> {
                    appCMSPresenter.stopLoader()
//                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_item_unavailable)),
//                        false, null, null, appCMSPresenter.localisedStrings.getSubscriptionMsgHeaderText())
                }
                BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE -> {
                    appCMSPresenter.stopLoader()
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_result_service_unavailable)),
                            false, null, null, appCMSPresenter.localisedStrings.getSubscriptionMsgHeaderText())
                }
                BillingClient.BillingResponseCode.ITEM_NOT_OWNED -> {
                    appCMSPresenter.stopLoader()
                    appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLanguageResourcesFile().getUIresource(appCMSPresenter.currentActivity.getString(R.string.subscription_billing_response_item_not_owned)),
                            false, null, null, appCMSPresenter.localisedStrings.getSubscriptionMsgHeaderText())
                }

            }
    }

    private fun consumeProduct(purchaseToken: String) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    billingClient.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(purchaseToken).build()) { billingResult, _ ->
                        billingClient.endConnection()
                        when (billingResult.responseCode) {

                            BillingClient.BillingResponseCode.OK -> {
                            }

                        }
                    }

                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    /**
     * To make the product available for repurchase we are fetching user's in-app purchases and check if it is not acknowledged then call consume
     * **/
    private fun fetchUserPurchases() {
        val purchaseResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        purchaseResult.purchasesList?.forEach { purchase ->
//            if (purchase.sku.equals(skuToBuy, true) && !purchase.isAcknowledged) {
                consumeProduct(purchase.purchaseToken)
//            }

        }


    }


}



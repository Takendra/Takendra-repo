package com.viewlift.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.viewlift.R
import com.viewlift.extensions.AppCMSSingletonHolder
import com.viewlift.presenters.AppCMSPresenter

/**
 * Used to log firebase analytics events
 * */
class AppCMSFirebaseAnalytics(private val appCMSPresenter: AppCMSPresenter) {
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(appCMSPresenter.currentContext).apply { this.setAnalyticsCollectionEnabled(true) }
    private val context: Context by lazy { appCMSPresenter.currentContext }

    companion object : AppCMSSingletonHolder<AppCMSFirebaseAnalytics, AppCMSPresenter>(::AppCMSFirebaseAnalytics)

    /** Purchase event
     * @param planId - plan ID of the plan purchased
     * @param planName - plan Name of the plan purchased
     * @param currency - plan currency of the plan purchased
     * @param planPrice - plan price of the plan purchased
     * @param transactionId - transaction id of the plan purchased
     * */
    fun ecommPurchaseEvent(planId: String?, planName: String, currency: String, planPrice: Double, transactionId: String?) {
        val bundle = Bundle()
        if (planId != null)
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, planId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, planName)
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, planPrice)
        if (transactionId != null)
            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, transactionId)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }

    /** Purchase event for TVOD
     * @param planId - plan ID of the plan purchased
     * @param planName - plan Name of the plan purchased
     * @param currency - plan currency of the plan purchased
     * @param planPrice - plan price of the plan purchased
     * @param contentId - id of content purchase
     * @param contentName - name of content purchase
     * @param purchaseType - type of purchase - rent/purchase
     * */
    fun ecommPurchaseTvodEvent(planId: String, planName: String, currency: String, planPrice: Double, contentId: String, contentName: String, purchaseType: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, contentId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, contentName)
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, planPrice)
        bundle.putString(context.getString(R.string.param_plan_id), planId)
        bundle.putString(context.getString(R.string.param_plan_name), planName)
        bundle.putString(context.getString(R.string.param_purchase_type), purchaseType)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }

    /** Purchase complete event for TVOD
     * @param planId - plan ID of the plan purchased
     * @param planName - plan Name of the plan purchased
     * @param currency - plan currency of the plan purchased
     * @param planPrice - plan price of the plan purchased
     * @param contentId - id of content purchase
     * @param contentName - name of content purchase
     * @param purchaseType - type of purchase - rent/purchase
     * */
    fun tvodPurchaseCompleteEvent(planId: String, planName: String, currency: String, planPrice: Double, contentId: String, contentName: String, purchaseType: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, contentId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, contentName)
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, planPrice)
        bundle.putString(context.getString(R.string.param_plan_id), planId)
        bundle.putString(context.getString(R.string.param_plan_name), planName)
        bundle.putString(context.getString(R.string.param_purchase_type), purchaseType)
        firebaseAnalytics.logEvent(context.getString(R.string.event_tvod_purchase_complete), bundle)
    }

    /** Add to cart event TVOD
     * @param planId - plan ID of the plan purchased
     * @param planName - plan Name of the plan purchased
     * @param currency - plan currency of the plan purchased
     * @param planPrice - plan price of the plan purchased
     * @param contentId - id of content purchase
     * @param contentName - name of content purchase
     * @param purchaseType - type of purchase - rent/purchase
     * */
    fun addToCartTVODEvent(planId: String, planName: String, currency: String, planPrice: Double, contentId: String, contentName: String, purchaseType: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, contentId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, contentName)
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, planPrice)
        bundle.putString(context.getString(R.string.param_plan_id), planId)
        bundle.putString(context.getString(R.string.param_plan_name), planName)
        bundle.putString(context.getString(R.string.param_purchase_type), purchaseType)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle)
    }

    /** Add to cart event
     * @param planId - plan ID of the plan purchased
     * @param planName - plan Name of the plan purchased
     * @param currency - plan currency of the plan purchased
     * @param planPrice - plan price of the plan purchased
     * */
    fun addToCartEvent(planId: String, planName: String, currency: String, planPrice: Double) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, planId)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, planName)
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, planPrice)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle)
    }

    /** Cancel subscription event
     * @param planId - plan ID of the plan purchased
     * @param planName - plan Name of the plan purchased
     * @param currency - plan currency of the plan purchased
     * @param planPrice - plan price of the plan purchased
     * */
    fun cancelSubscriptionEvent(planId: String?, planName: String?, currency: String?, planPrice: String?) {
        val bundle = Bundle()
        if (planId?.isNotBlank() == true)
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, planId)
        if (planName?.isNotBlank() == true)
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, planName)
        if (currency?.isNotBlank() == true)
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency)
        if (planPrice?.isNotBlank() == true)
            bundle.putString(FirebaseAnalytics.Param.VALUE, planPrice)
        firebaseAnalytics.logEvent(context.getString(R.string.event_cancel_subscription), bundle)
    }


    /**
     * Signup event
     * @param signupValue - value can be google / facebook / email
     * */
    fun signupEvent(signupValue: String) {
        val bundle = Bundle()
        bundle.putString(context.getString(R.string.param_signup_method), signupValue)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
    }

    /**
     * Signin event
     * @param signupValue - value can be google / facebook / email
     * */
    fun signinEvent(signinValue: String) {
        val bundle = Bundle()
        bundle.putString(context.getString(R.string.param_signin_method), signinValue)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)
    }

    /**
     * TV Provider selected event
     * @param provider - selected TV provider user trying to login with
     * */
    fun selectedTVProviderEvent(provider: String) {
        val bundle = Bundle()
        bundle.putString(context.getString(R.string.param_tve_provider_name), provider)
        firebaseAnalytics.logEvent(context.getString(R.string.event_tve_provider_selected), bundle)
    }

    /**
     * Screen view event
     * @param screenName - name of the screen user is on
     * */
    fun screenViewEvent(screenName: String) {
        val bundle = Bundle()
        bundle.putString(context.getString(R.string.param_screen_view), screenName)
        firebaseAnalytics.setCurrentScreen(appCMSPresenter.currentActivity, screenName, null);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
    }

    /**
     * Screen view item event
     * @param screenName - name of the screen user is on
     * */
    fun viewItemEvent(screenName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen")
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle)
    }

    /**
     * Search query event
     * @param screenName - name of the screen user is on
     * */
    fun searchQueryEvent(searchQuery: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    /**
     * Begin checkout event
     * */
    fun beginCheckoutEvent() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Event.BEGIN_CHECKOUT, FirebaseAnalytics.Event.BEGIN_CHECKOUT)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle)
    }

    /**
     * User log out event
     * */
    fun logoutEvent() {
        val bundle = Bundle()
        bundle.putString(context.getString(R.string.event_logout), context.getString(R.string.value_logout))
        firebaseAnalytics.logEvent(context.getString(R.string.event_logout), bundle)
    }

    /**
     * Sets user property for logged in status of the app
     * @param value - will be logged_in / not_logged_in
     * */
    fun userPropertyLoginStatus(value: String) {
        firebaseAnalytics.setUserProperty(context.getString(R.string.property_login_status), value)
    }

    /**
     * Sets user property for planId in case of subscribed user
     * @param planId - will be null for unsubscribed user
     * */
    fun userPropertyPlanId(planId: String?) {
        firebaseAnalytics.setUserProperty(context.getString(R.string.property_plan_id), planId)
    }

    /**
     * Sets user property for planName in case of subscribed user
     * @param planName - will be null for unsubscribed user
     * */
    fun userPropertyPlanName(planName: String?) {
        firebaseAnalytics.setUserProperty(context.getString(R.string.property_plan_name), planName)
    }

    /**
     * Sets user property for planName in case of subscribed user
     * @param status - will be unsubscribed / subscribed
     * */
    fun userPropertySubscriptionStatus(status: String) {
        firebaseAnalytics.setUserProperty(context.getString(R.string.property_subscription_status), status)
    }

    /**
     * Sets user id on analytics
     * @param userId - will be null for guest user
     * */
    fun analyticsUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

}
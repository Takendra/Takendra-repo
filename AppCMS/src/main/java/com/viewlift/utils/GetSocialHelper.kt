package com.viewlift.utils

import android.text.TextUtils
import android.util.Log
import com.viewlift.db.AppPreference
import com.viewlift.presenters.AppCMSPresenter
import com.viewlift.views.activity.AppCMSPageActivity
import im.getsocial.sdk.Analytics
import im.getsocial.sdk.GetSocial
import im.getsocial.sdk.Invites
import im.getsocial.sdk.common.PagingQuery
import im.getsocial.sdk.communities.Identity
import im.getsocial.sdk.communities.UserId
import im.getsocial.sdk.communities.UserUpdate
import im.getsocial.sdk.invites.InviteContent
import im.getsocial.sdk.invites.ReferralData
import im.getsocial.sdk.invites.ReferralUsersQuery
import java.util.*

object GetSocialHelper {

    private const val TAG = "GetSocialHelper"
    const val KEY_EMAIL_ID = "email"
    const val KEY_PHONE = "phone"
    const val KEY_APP_PLATFORM = "app_platform"
    const val USER_SUBSCRIPTION_COMPLETED = "user_subscription_completed"
    private const val KEY_NAME = "name"
    private const val KEY_USER_ID = "userId"
    private const val KEY_PROVIDER = "viewlift"
    private const val KEY_IS_OTP_ENABLED = "isOTPEnabled"
    private const val KEY_PLAN_ID = "planId"

    private var referralData: ReferralData? = null

    @JvmField
    var needToProcessReferralData: Boolean = false


    @JvmStatic
    fun init(appId: String?, appCMSPresenter: AppCMSPresenter) {
        GetSocial.addOnInitializeListener {
            Log.d(TAG, "GetSocial is initialized")
            if (appCMSPresenter.isUserLoggedIn) {
                onSetUserIdentity(appCMSPresenter.appPreference)
            }
        }

        Invites.setReferralDataListener { referralData: ReferralData ->
            this.referralData = referralData
            needToProcessReferralData = true
            if (appCMSPresenter.isLaunched && appCMSPresenter.currentActivity != null
                    && appCMSPresenter.currentActivity is AppCMSPageActivity) {
                appCMSPresenter.sendRefreshPageAction()
            }
            Log.d(TAG, "App started with referral data: $referralData")
        }

        GetSocial.init(appId)
    }

    @JvmStatic
    fun onLoginSuccess(appPreference: AppPreference?) {
        onSetUserIdentity(appPreference)
    }

    private fun onSetUserIdentity(appPreference: AppPreference?, callBack: (() -> Unit)? = null) {
        if (GetSocial.isInitialized() && appPreference != null) {
            val userId = appPreference.getLoggedInUser()
            val user = GetSocial.getCurrentUser() ?: return
            if (user.identities?.get(KEY_PROVIDER.toLowerCase(Locale.ROOT)) != userId)
                addAuthIdentity(Identity.custom(KEY_PROVIDER, userId, userId), appPreference, callBack)
            else
                setUserDetails(appPreference, callBack)
        }
    }

    private fun addAuthIdentity(identity: Identity, appPreference: AppPreference, callBack: (() -> Unit)? = null) {
        GetSocial.getCurrentUser()?.addIdentity(identity, {
            Log.d(TAG, "on user login")
            setUserDetails(appPreference, callBack)
        }, {
            GetSocial.switchUser(identity, {
                Log.d(TAG, "switch user success")
                setUserDetails(appPreference, callBack)
                setReferrerData()
            }, {
                Log.d(TAG, "Failed to switch user: $it")
            })
        }, {
            Log.d(TAG, "Failed to log into $it")
        })
    }

    private fun setUserDetails(appPreference: AppPreference, callBack: (() -> Unit)? = null) {
        val name: String?
        val userId = appPreference.getLoggedInUser()
        name = if (!TextUtils.isEmpty(appPreference.getLoggedInUserName())) {
            appPreference.getLoggedInUserName()
        } else if (!TextUtils.isEmpty(appPreference.getLoggedInUserEmail())) {
            appPreference.getLoggedInUserEmail()
        } else if (!TextUtils.isEmpty(appPreference.getLoggedInUserPhone())) {
            appPreference.getLoggedInUserPhone()
        } else {
            userId
        }
        val batchUpdate = UserUpdate().updateDisplayName(name)
        if (!TextUtils.isEmpty(appPreference.getLoggedInUserEmail())) {
            batchUpdate.setPrivateProperty(KEY_EMAIL_ID, appPreference.getLoggedInUserEmail())
            batchUpdate.setPublicProperty(KEY_EMAIL_ID, appPreference.getLoggedInUserEmail())
        }
        if (!TextUtils.isEmpty(appPreference.getLoggedInUserPhone())) {
            batchUpdate.setPrivateProperty(KEY_PHONE, appPreference.getLoggedInUserPhone())
            batchUpdate.setPublicProperty(KEY_PHONE, appPreference.getLoggedInUserPhone())
        }
        GetSocial.getCurrentUser()?.updateDetails(batchUpdate, {
            Log.d(TAG, "User details were successfully updated: ${GetSocial.getCurrentUser()?.displayName}")
            callBack?.invoke()
        }, {
            Log.d(TAG, "Failed to update user details, error: $it")
        })
    }

    private fun setReferrerData() {
        referralData?.let {
            Invites.setReferrer(UserId.create(it.referrerUserId), USER_SUBSCRIPTION_COMPLETED, it.linkParams, {
                Log.d(TAG, "Successfully set referrer")
                referralData = null
            }, { error ->
                Log.d(TAG, "Failed to set referrer: $error")
            })
        }
    }

    fun openChannel(channelID: String, appCMSPresenter: AppCMSPresenter) {
        val user = GetSocial.getCurrentUser() ?: return
        if (user.identities?.get(KEY_PROVIDER.toLowerCase(Locale.ROOT))?.isNotBlank() == true) {
            cannelCallBack(channelID, appCMSPresenter)
        } else {
            onSetUserIdentity(appCMSPresenter.appPreference) {
                cannelCallBack(channelID, appCMSPresenter)
            }
        }
    }

    private fun cannelCallBack(channelID: String, appCMSPresenter: AppCMSPresenter) {
        val pagingQuery = PagingQuery(ReferralUsersQuery.usersForEvent(USER_SUBSCRIPTION_COMPLETED))
        Invites.getReferredUsers(pagingQuery, {
            val count = it.entries.size
            appCMSPresenter.sendInviteEvent(channelID, "" + count, "" + count, "" + count)
        }, {

        })
        Invites.send(InviteContent(), channelID, {
            Log.i(TAG, "Invitation with referral data via $channelID was sent")
        }, {
            Log.i(TAG, "Invitation with referral data via $channelID was cancelled")
        }, {
            Log.e(TAG, "Invitation with referral data via $channelID failed, error:$it")
        })
    }

    @JvmStatic
    fun onLogoutSuccess() {
        if (GetSocial.isInitialized()) {
            referralData = null
            GetSocial.resetUser({
                Log.d(TAG, "Successfully logout into")
            }, {
                Log.d(TAG, "Failed to logout into  $it")
            })
        }
    }

    @JvmStatic
    fun trackGetSocialEvent(presenter: AppCMSPresenter?) {
        if (GetSocial.isInitialized()) {
            referralData = null
            Analytics.trackCustomEvent(USER_SUBSCRIPTION_COMPLETED, HashMap<String, String>().apply {
                if (presenter != null) {
                    put(KEY_USER_ID, presenter.loggedInUser)
                    put(KEY_NAME, presenter.loggedInUserName ?: "")
                    put(KEY_EMAIL_ID, presenter.loggedInUserEmail ?: "")
                    put(KEY_PHONE, presenter.loggedInPhone ?: "")
                    put(KEY_PLAN_ID, presenter.planToPurchase)
                    put(KEY_APP_PLATFORM, "Android")
                    put(KEY_IS_OTP_ENABLED, CommonUtils.isSiteOTPEnabled(presenter).toString())
                    put(USER_SUBSCRIPTION_COMPLETED, "Thanks for subscribing")
                }
            })
            Log.d(TAG, "user_subscription_completed event trigger success")
        }
    }
}
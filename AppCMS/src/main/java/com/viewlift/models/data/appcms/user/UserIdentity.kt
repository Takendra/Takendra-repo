package com.viewlift.models.data.appcms.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.subscriptions.AppCMSUserSubscriptionPlanResult
import com.viewlift.models.data.appcms.ui.authentication.MonetizationPlan
import com.viewlift.models.data.appcms.ui.authentication.Phone

data class UserIdentity(@SerializedName("phone")
                        @Expose
                        private val phone: Any? = null,
                        @SerializedName("phoneNumber")
                        @Expose
                        val phoneNumber: String? = null,
                        @SerializedName("mvpdProvider")
                        @Expose
                        val tvProviderName: String? = null,
                        @SerializedName("passwordEnabled")
                        @Expose
                        val isPasswordEnabled: Boolean = true,
                        @SerializedName("userId")
                        @Expose
                        val userId: String? = null,
                        @SerializedName("provider")
                        @Expose
                        val provider: String? = null,
                        @SerializedName("idpLogo")
                        @Expose
                        val idpLogo: String? = null,
                        @SerializedName("id")
                        @Expose
                        var id: String? = null,
                        @SerializedName("email")
                        @Expose
                        var email: String? = null,
                        @SerializedName("monetizationPlan")
                        @Expose
                        val userSubscribedplan: MonetizationPlan? = null,
                        @SerializedName("picture")
                        @Expose
                        val picture: String? = null,
                        @SerializedName("country")
                        @Expose
                        val country: String? = null,
                        @SerializedName("name")
                        @Expose
                        var name: String? = null,
                        @SerializedName("password")
                        @Expose
                        var password: String? = null,
                        @SerializedName("authorizationToken")
                        @Expose
                        val authorizationToken: String? = null,
                        @SerializedName("refreshToken")
                        @Expose
                        val refreshToken: String? = null,
                        @SerializedName("isSubscribed")
                        @Expose
                        val isSubscribed: Boolean = false,
                        @SerializedName("error")
                        @Expose
                        val error: String? = null,
                        @SerializedName("code")
                        @Expose
                        val code: String? = null,
                        @SerializedName("subscription")
                        @Expose
                        val subscription: AppCMSUserSubscriptionPlanResult? = null,
                        @SerializedName("parentalControl")
                        @Expose
                        val isParentalControlEnable: Boolean = false,
                        @SerializedName("parentalPin")
                        @Expose
                        val parentalPin: String? = null,
                        @SerializedName("parentalRating")
                        @Expose
                        val parentalRating: String? = null,
                        @SerializedName("purchasedItems")
                        @Expose
                        val userPurchases: List<UserPurchases> = emptyList(),
                        @SerializedName("subscriptionStatus")
                        @Expose
                        val subscriptionStatus: String? = null,
                        @SerializedName("whatsappConsent")
                        @Expose
                        val isWhatsAppConsent: Boolean = false,
                        @SerializedName("emailConsent")
                        @Expose
                        val isEmailConsent: Boolean = false) {

    fun getPhone(): Phone? {
        return phone as? Phone
    }
}
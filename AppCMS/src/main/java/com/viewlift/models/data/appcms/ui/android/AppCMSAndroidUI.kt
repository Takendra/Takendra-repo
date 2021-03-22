package com.viewlift.models.data.appcms.ui.android

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppCMSAndroidUI(@SerializedName("advertising") @Expose
                           var advertising: Advertising? = null,

                           @SerializedName("tveSettings")
                           @Expose
                           val tveSettings: TveSettings? = null,

                           @SerializedName("navigation")
                           @Expose
                           val navigation: Navigation? = null,

                           @SerializedName("headers")
                           @Expose
                           val headers: List<Headers>? = null,

                           @SerializedName("pages")
                           @Expose
                           val metaPages: List<MetaPage>? = null,

                           @SerializedName("analytics")
                           @Expose
                           val analytics: Analytics? = null,

                           @SerializedName("version")
                           @Expose
                           val version: String? = null,

                           @SerializedName("appName")
                           @Expose
                           val appName: String? = null,

                           @SerializedName("shortAppName")
                           @Expose
                           val shortAppName: String? = null,

                           @SerializedName("getSocialAppId")
                           @Expose
                           val getSocialAppId: String? = null,

                           @SerializedName("blocks")
                           @Expose
                           val blocks: List<Blocks>? = null,

                           @SerializedName("blocksVersion")
                           @Expose
                           val blocksVersion: Int = 0,

                           @SerializedName("pagesUpdated")
                           @Expose
                           val pagesUpdated: Long = 0,

                           @SerializedName("blocksBundleUrl")
                           @Expose
                           val blocksBundleUrl: String? = null,

                           @SerializedName("blocksBaseUrl")
                           @Expose
                           val blocksBaseUrl: String? = null,

                           @SerializedName("subscription_flow_content")
                           @Expose
                           val subscriptionFlowContent: SubscriptionFlowContent? = null,

                           @SerializedName(value = "geoRestrictedCountries", alternate = ["geoRestrict"])
                           @Expose
                           val geoRestrictedCountries: List<String>? = null,

                           @SerializedName("subscription_flow_audio_content")
                           @Expose
                           val subscriptionAudioFlowContent: SubscriptionAudioFlowContent? = null,

                           @SerializedName("enablePayment")
                           @Expose
                           val isPaymentEnabled: Boolean = false): Serializable
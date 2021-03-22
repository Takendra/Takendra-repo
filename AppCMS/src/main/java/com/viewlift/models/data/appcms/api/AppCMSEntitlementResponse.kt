package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class AppCMSEntitlementResponse(@SerializedName("success") @Expose
                                     var isSuccess: Boolean = false,
                                     @SerializedName("siteName")
                                     @Expose
                                     val siteName: String? = null,
                                     @SerializedName("siteId")
                                     @Expose
                                     val siteId: String? = null,
                                     @SerializedName("video")
                                     @Expose
                                     val videoContentDatum: ContentDatum? = null,
                                     @SerializedName("playable")
                                     @Expose
                                     val isPlayable: Boolean = false,
                                     @SerializedName("dfp")
                                     @Expose
                                     val dfp: List<DfpAds>? = null,
                                     @SerializedName("plans")
                                     @Expose
                                     val subscriptionPlans: List<ContentDatum>? = null,
                                     @SerializedName("errorCode")
                                     @Expose
                                     val errorCode: String? = null,
                                     @SerializedName("errorMessage")
                                     @Expose
                                     val errorMessage: String? = null,
                                     @SerializedName("ads")
                                     @Expose
                                     val ads: Ads? = null,
                                     var appCMSSignedURLResult: AppCMSSignedURLResult? = null,
                                     var code: Int = 0,
                                     var message: String? = null) {

    fun convertToAppCMSPageAPI(Id: String?, moduleType: String?): AppCMSPageAPI? {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        val data: MutableList<ContentDatum> = ArrayList()
        if (videoContentDatum != null) {
            data.add(videoContentDatum)
        }
        module.setContentData(data)
        module.setModuleType(moduleType)
        appCMSPageAPI.id = Id
        val moduleList: MutableList<Module> = ArrayList()
        moduleList.add(module)
        appCMSPageAPI.modules = moduleList
        return appCMSPageAPI
    }

}
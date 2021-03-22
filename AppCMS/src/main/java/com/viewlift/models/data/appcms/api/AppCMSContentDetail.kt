package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class AppCMSContentDetail(@SerializedName("gist") @Expose
                               var gist: Gist,
                               @SerializedName("contentDetails")
                               @Expose
                               val contentDetails: ContentDetails,
                               @SerializedName("streamingInfo")
                               @Expose
                               val streamingInfo: StreamingInfo,
                               @SerializedName("categories")
                               @Expose
                               val categories: List<Category>,
                               @SerializedName("tags")
                               @Expose
                               val tags: List<Tag>,
                               @SerializedName("parentalRating")
                               @Expose
                               var parentalRating: String,
                               @SerializedName("pricing") @Expose
                               var pricing: Pricing) {
    fun convertToContentDatum(moduleApi: Module?): ContentDatum {
        val contentDatum = ContentDatum()
        contentDatum.setGist(gist)
        contentDatum.setContentDetails(contentDetails)
        contentDatum.setStreamingInfo(streamingInfo)
        contentDatum.setCategories(categories)
        contentDatum.setTags(tags)
        contentDatum.setParentalRating(parentalRating)
        contentDatum.setPricing(pricing)
        contentDatum.setModuleApi(moduleApi)
        return contentDatum
    }

    fun convertToAppCMSPageAPI(Id: String?, moduleType: String?, moduleApi: Module?): AppCMSPageAPI? {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        val data: MutableList<ContentDatum> = ArrayList()
        data.add(convertToContentDatum(moduleApi))
        module.setContentData(data)
        module.setModuleType(moduleType)
        appCMSPageAPI.id = Id
        val moduleList: MutableList<Module> = ArrayList()
        moduleList.add(module)
        appCMSPageAPI.modules = moduleList
        return appCMSPageAPI
    }
}
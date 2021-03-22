package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class AppCMSScheduleResult(@SerializedName("id") @Expose
                                val id: String,

                                @SerializedName("gist")
                                @Expose
                                val gist: Gist,

                                @SerializedName("contentDetails")
                                val contentDetails: ContentDetails,

                                @SerializedName("categories")
                                @Expose
                                val categories: List<Category>,

                                @SerializedName("scheduleResult")
                                @Expose
                                val scheduleResult: HashMap<String, MutableList<ContentDatum>>) {
    fun convertToAppCMSPageAPI(monthlySchedule: HashMap<String, List<ContentDatum>>): AppCMSPageAPI? {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        val data: MutableList<ContentDatum> = ArrayList()
        val contentDatum = ContentDatum()
        contentDatum.monthlySchedule = monthlySchedule
        data.add(contentDatum)
        module.setContentData(data)
        //appCMSPageAPI.setId(Id);
        val moduleList: MutableList<Module> = ArrayList()
        moduleList.add(module)
        appCMSPageAPI.modules = moduleList
        return appCMSPageAPI
    }

    fun convertToAppCMSPageAPI(data: List<ContentDatum?>?): AppCMSPageAPI? {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        module.setContentData(data)
        val moduleList: MutableList<Module> = ArrayList()
        moduleList.add(module)
        appCMSPageAPI.modules = moduleList
        return appCMSPageAPI
    }
}
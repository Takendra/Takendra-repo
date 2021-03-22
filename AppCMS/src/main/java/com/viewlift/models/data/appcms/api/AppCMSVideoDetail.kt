package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class AppCMSVideoDetail(@SerializedName("records") @Expose
                             val records: List<ContentDatum>? = null) {

    fun convertToAppCMSPageAPI(Id: String, moduleType: String): AppCMSPageAPI {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        val data: MutableList<ContentDatum> = ArrayList()
        if (records != null) {
            data.addAll(records)
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
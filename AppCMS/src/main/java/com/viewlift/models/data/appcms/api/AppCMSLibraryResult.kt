package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.history.Record
import java.util.*

data class AppCMSLibraryResult(@SerializedName("records") @Expose
                               val records: List<Record>? = null,
                               @SerializedName("nextOffset")
                               @Expose
                               val nextOffset: Int,
                               @SerializedName("limit")
                               @Expose
                               val limit: Int) {

    fun convertToAppCMSPageAPI(Id: String?, monetizationEnable: Boolean): AppCMSPageAPI? {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        val data: MutableList<ContentDatum> = ArrayList()
        if (records != null) {
            for (record in records) {
                if (monetizationEnable) {
                    data.add(record.convertToContentDatumPurchase())
                } else {
                    if (record.contentResponse != null) {
                        data.add(record.convertToContentDatum())
                    }
                }
            }
        }
        module.setContentData(data)
        appCMSPageAPI.id = Id
        val moduleList: MutableList<Module> = ArrayList()
        moduleList.add(module)
        appCMSPageAPI.modules = moduleList
        return appCMSPageAPI
    }
}
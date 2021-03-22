package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class AppCMSTeamRoasterResult(@SerializedName("seasonData") @Expose
                                   val seasonData: SeasonData) {
    fun convertToAppCMSPageAPI(Id: String?): AppCMSPageAPI? {
        val appCMSPageAPI = AppCMSPageAPI()
        val module = Module()
        val data: MutableList<ContentDatum> = ArrayList()
        if (seasonData.getStandings().team != null) {
            for (records in seasonData.getStandings().team!!) {
                val contentDatum1 = ContentDatum()
                contentDatum1.team = records
                data.add(contentDatum1)
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
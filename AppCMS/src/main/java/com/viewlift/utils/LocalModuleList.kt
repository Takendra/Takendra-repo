package com.viewlift.utils

import android.content.Context
import com.google.gson.GsonBuilder
import com.viewlift.Utils
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI
import com.viewlift.models.data.appcms.ui.page.ModuleList

object LocalModuleList {

    private lateinit var appCMSLocalModuleList: List<ModuleList>

    @JvmStatic
    fun getModuleList(context: Context): List<ModuleList> {
        if (!::appCMSLocalModuleList.isInitialized) {
            appCMSLocalModuleList = GsonBuilder().create()
                    .fromJson(Utils.loadJsonFromAssets(context, "trays.json"), AppCMSPageUI::class.java)
                    .moduleList
        }
        return appCMSLocalModuleList
    }
}
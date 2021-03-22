package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSEventArchieveResult(@SerializedName("LiveEvents") @Expose
                                     val LiveEvents: List<LiveEvents>) {

    fun convertToAppCMSPageModule(appCMSPageAPI: AppCMSPageAPI): AppCMSPageAPI? {
        if(appCMSPageAPI.modules!=null){
            if(appCMSPageAPI.modules!!.size>0){
                for (i in appCMSPageAPI.modules!!.indices) {
                    if (appCMSPageAPI.modules!![i].getModuleType().equals("EventDetailModule", ignoreCase = true)) {
                        appCMSPageAPI.modules!![i].getContentData()[0].liveEvents = LiveEvents
                        break
                    }
                }
            }
        }

        return appCMSPageAPI
    }
}
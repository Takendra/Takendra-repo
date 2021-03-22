package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AppCMSRosterResult(@SerializedName("teams") @Expose
                              val teams: List<Team>,
                              @SerializedName("name")
                              @Expose
                              val name: String)
package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.vimeo.stag.UseStag
import java.io.Serializable


data class ContentPlans(@SerializedName("planIds") @Expose
                        val id: List<String>,
                        @SerializedName("planGroupIds") @Expose
                        val groupIds:List<String>?,

                        @SerializedName("type")
                        @Expose
                        val type: String):Serializable

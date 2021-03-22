package com.viewlift.models.data.appcms.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CreditBlock(@SerializedName("id") @Expose
                       val id: Any? = null,

                       @SerializedName("title")
                       @Expose
                       val title: String? = null,

                       @SerializedName("credits")
                       @Expose
                       val credits: List<Credit>? = null,

                       @SerializedName("containsHollywoodCelebrities")
                       @Expose
                       val containsHollywoodCelebrities: Boolean = false,

                       @SerializedName("containsTVCelebrities")
                       @Expose
                       val containsTVCelebrities: Boolean = false) : Serializable

data class Credit(@SerializedName("title") @Expose
                  val title: String? = null) : Serializable
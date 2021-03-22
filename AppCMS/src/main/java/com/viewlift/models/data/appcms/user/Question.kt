package com.viewlift.models.data.appcms.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Question(@SerializedName("question") @Expose
                    val question: String? = null,
                    @SerializedName("answer")
                    @Expose
                    val answer: String? = null)
package com.viewlift.models.data.appcms.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserIdentityPassword(@SerializedName("resetToken") @Expose
                                var resetToken: String?=null,
                                @SerializedName("newPassword")
                                @Expose
                                var newPassword: String?=null,
                                @SerializedName("oldPassword")
                                @Expose
                                var oldPassword: String?=null,
                                @SerializedName("error")
                                @Expose
                                val error: String?=null,
                                @SerializedName("code")
                                @Expose
                                val code: String?=null,
                                @SerializedName("passwordUpdated")
                                @Expose
                                val isPasswordUpdated: Boolean=false,
                                @SerializedName("addPassword")
                                @Expose
                                var isAddPassword: Boolean=false)
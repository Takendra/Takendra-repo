package com.viewlift.models.data.appcms.ui.android

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AccessLevels(@SerializedName("loggedOut") @Expose
                        var isLoggedOut: Boolean=false,

                        @SerializedName("loggedIn")
                        @Expose
                        var isLoggedIn: Boolean=false,

                        @SerializedName("subscribed")
                        @Expose
                        var isSubscribed: Boolean=false): Serializable
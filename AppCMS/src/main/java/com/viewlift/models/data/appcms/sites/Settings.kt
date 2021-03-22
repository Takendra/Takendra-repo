package com.viewlift.models.data.appcms.sites

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.viewlift.models.data.appcms.ui.android.Analytics
import com.viewlift.models.data.appcms.ui.main.CustomerService
import com.viewlift.models.data.appcms.ui.main.SocialMedia

class Settings(@SerializedName("customerService")
               @Expose
               val customerService: CustomerService? = null,

               @SerializedName("analytics")
               @Expose
               val analytics: Analytics? = null,

               @SerializedName("social")
               @Expose
               val social: SocialMedia? = null)
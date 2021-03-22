package com.viewlift.models.data.appcms.ui.authentication

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Model class for error details in API response
 *
 * @author Jonathan
 * @since 2017-09-29
 * @param error - error message
 * @param code - error code
 * @param message - error message
 */
data class ErrorResponse(@SerializedName("error") @Expose
                         val error: String? = null,

                         @SerializedName("code")
                         @Expose
                         val code: String? = null,

                         @SerializedName("message")
                         @Expose
                         val message: String? = null)
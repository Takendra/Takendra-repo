package com.viewlift.models.data.appcms.api

import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppCMSSignedURLResult(@SerializedName("signed") @Expose
                                 val signed: String? = null,
                                 var policy: String? = null,
                                 var signature: String? = null,
                                 var keyPairId: String? = null) : Serializable {
    fun parseKeyValuePairs() {
        if (!TextUtils.isEmpty(signed)) {
            if (TextUtils.isEmpty(signature) &&
                    TextUtils.isEmpty(policy) &&
                    TextUtils.isEmpty(keyPairId)) {
                if (signed != null) {
                    var valueIndex = signed.indexOf("Policy=")
                    var paramIndex = signed.indexOf("&")
                    if (0 < paramIndex && 0 <= valueIndex) {
                        policy = signed.substring(valueIndex + "Policy=".length, paramIndex)
                        valueIndex = signed.indexOf("Signature=", paramIndex)
                        paramIndex = signed.indexOf("&", paramIndex + 1)
                        if (0 < paramIndex && 0 <= valueIndex) {
                            signature = signed.substring(valueIndex + "Signature=".length, paramIndex)
                            valueIndex = signed.indexOf("Key-Pair-Id=", paramIndex)
                            if (0 <= valueIndex) {
                                keyPairId = signed.substring(valueIndex + "Key-Pair-Id=".length)
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.viewlift.models.data.appcms.api

import java.io.Serializable

data class HlsDetail(val url: String, val keypairId: String, val policy: String, val signature: String) : Serializable
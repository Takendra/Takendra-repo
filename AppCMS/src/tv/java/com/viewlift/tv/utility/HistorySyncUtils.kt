package com.viewlift.tv.utility

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.viewlift.R
import com.viewlift.models.data.appcms.api.ContentDatum

object HistorySyncUtils {

    @JvmStatic
    fun sendBroadCastForPlayHistorySync(context: Context, contentDatum: ContentDatum) {
        val intent = Intent("com.oneplus.tv.action.USER_DATA_INTERACTION")
        intent.setPackage("com.oneplus.tv.launcher")
        intent.putExtra("launch_mode"  , 0x00) // 0x00,0x01
        intent.putExtra("package_name" , context.packageName)
        intent.putExtra("sub_action"   , "add_history")
        intent.putExtra("media_id"     , contentDatum.gist.id)
        intent.putExtra("media_title"  , contentDatum.gist.title)
        intent.putExtra("title"        , contentDatum.title)
        intent.putExtra("position"     , contentDatum.gist.watchedTime)
        intent.putExtra("duration"     , contentDatum.gist.runtime)
        intent.putExtra("poster_ver"   , contentDatum.gist.posterImageUrl)
        intent.putExtra("poster_hor"   , contentDatum.gist.landscapeImageUrl)
        intent.putExtra("cp"           , context.getString(R.string.app_name))
        intent.putExtra("intent_uri"   , context.getString(R.string.video_player_deep_link) + "://" + contentDatum.gist.permalink)
        intent.extras!!.putBundle("params"   , Bundle())
        context.sendBroadcast(intent)

        //intent.putExtra("class_name", "com.example.app.PlayActivity")
    }
}
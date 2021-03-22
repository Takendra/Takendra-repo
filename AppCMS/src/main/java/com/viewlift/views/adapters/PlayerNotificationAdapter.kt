package com.viewlift.views.adapters

import android.app.PendingIntent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.Log
import com.viewlift.models.data.appcms.api.ContentDatum
import java.io.IOException
import java.net.URL

class PlayerNotificationAdapter : PlayerNotificationManager.MediaDescriptionAdapter {

    private lateinit var contentDatum: ContentDatum;

    override fun getCurrentContentTitle(player: Player): String {
        return contentDatum?.gist?.title ?: ""
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return null
    }

    override fun getCurrentContentText(player: Player): String? {
     //   return contentDatum?.gist?.description ?: ""
        return contentDatum?.seriesData?.get(0)?.gist?.title ?: ""
    }

    override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
        return playerImage()
    }

    fun setData(contentDatum: ContentDatum) {
        this.contentDatum = contentDatum
    }
    fun playerImage(): Bitmap? {
        var image: Bitmap? = null
        try {
            var url = URL(contentDatum.gist.imageGist._16x9)
            if (contentDatum.gist.imageGist._3x4 != null)
                url = URL(contentDatum.gist.imageGist._3x4)
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            System.out.println(e)
        }
        return image
    }


}
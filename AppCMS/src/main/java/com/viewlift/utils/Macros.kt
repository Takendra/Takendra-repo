package com.viewlift.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.viewlift.R
import com.viewlift.Utils
import com.viewlift.models.data.appcms.api.ContentDatum
import com.viewlift.presenters.AppCMSPresenter
import java.net.URLEncoder
import java.util.*

/**
 * Singleton class used for macros replacement for video play URL and Ads URL
 *
 * @author Abhishek
 * @since 2020-09-23
 */

object Macros {

    private lateinit var VIEWLIFT_APP_BUNDLE: String
    private lateinit var VIEWLIFT_APP_NAME: String
    private lateinit var VIEWLIFT_BRAND_NAME: String
    private lateinit var VIEWLIFT_WIDTH: String
    private lateinit var VIEWLIFT_HEIGHT: String
    private lateinit var VIEWLIFT_SIZE: String
    private lateinit var VIEWLIFT_DOMAIN: String
    private lateinit var VIEWLIFT_USER_AGENT: String
    private lateinit var VIEWLIFT_AUTOPLAY: String
    private lateinit var VIEWLIFT_DEVICE_MAKE: String
    private lateinit var VIEWLIFT_DEVICE_MODEL: String
    private lateinit var VIEWLIFT_OS_VER: String
    private var VIEWLIFT_CACHEBUSTER: String? = null
    private var VIEWLIFT_ENCODED_URL: String? = null
    private var VIEWLIFT_URL: String? = null
    private var VIEWLIFT_DEVICE_ID: String? = null
    private var VIEWLIFT_DESCRIPTION: String? = null
    private var VIEWLIFT_IP: String? = null
    private var VIEWLIFT_MIN_DURATION: String? = null
    private var VIEWLIFT_MAX_DURATION: String? = null
    private var VIEWLIFT_APP_STORE_URL: String? = null
    private var VIEWLIFT_AD_POS: String? = null
    private var VIEWLIFT_LAT: String? = null
    private var VIEWLIFT_LON: String? = null
    private var VIEWLIFT_IAB: String? = null
    private var VIEWLIFT_KEYWORDS: String? = null
    private var VIEWLIFT_CONTENT_ID: String? = null
    private var VIEWLIFT_VIDEO_ID: String? = null
    private var VIEWLIFT_ENCODED_VIDEO_TITLE: String? = null
    private var VIEWLIFT_CONTENT_TITLE: String? = null
    private var VIEWLIFT_MEDIA_ID: String? = null
    private var VIEWLIFT_GDPR: String? = null
    private var VIEWLIFT_CONSENT: String? = null
    private var VIEWLIFT_US_PRIVACY: String? = null
    private var VIEWLIFT_COPPA: String? = null
    private var VIEWLIFT_CONTENT_EPISODE: String? = null
    private var VIEWLIFT_CONTENT_SEASON: String? = null
    private var VIEWLIFT_CONTENT_SERIES_TITLE: String? = null
    private var VIEWLIFT_CONTENT_GENRE: String? = null
    private var VIEWLIFT_CONTENT_DURATION_MS: String? = null
    private var VIEWLIFT_CONTENT_DURATION: String? = null
    private lateinit var context: Context

    //private var context: Context = TODO()
    private var VIEWLIFT_OS = "Android"
    private var VIEWLIFT_MAX_POD_DURATION = "120"
    private var VIEWLIFT_MAX_POD_DURATION_MS = "120000"
    private const val UTF_8_ENCODE: String = "UTF-8"

    fun setParameters(appCMSPresenter: AppCMSPresenter, contentDatum: ContentDatum?) {

        VIEWLIFT_APP_BUNDLE = context.resources.getString(R.string.package_name)
        VIEWLIFT_APP_NAME = URLEncoder.encode(Utils.getProperty("AppName", context), UTF_8_ENCODE)
        VIEWLIFT_BRAND_NAME = VIEWLIFT_APP_NAME
        VIEWLIFT_WIDTH = Utils.getDeviceWidth(context).toString()
        VIEWLIFT_HEIGHT = Utils.getDeviceHeight(context).toString()
        VIEWLIFT_HEIGHT = VIEWLIFT_WIDTH.plus("x").plus(VIEWLIFT_HEIGHT)
        VIEWLIFT_CACHEBUSTER = (Math.random() * 100000 * 100000 + 1).toString()
        VIEWLIFT_DOMAIN = appCMSPresenter.appCMSMain.domainName
        VIEWLIFT_USER_AGENT = URLEncoder.encode(CommonUtils.getUserAgent(appCMSPresenter), UTF_8_ENCODE)
        if (Utils.getIPAddress() != null)
            VIEWLIFT_IP = Utils.getIPAddress()
        VIEWLIFT_AUTOPLAY = appCMSPresenter.isAutoPlayEnable.toString()
        VIEWLIFT_DEVICE_MAKE = URLEncoder.encode(Build.MANUFACTURER, UTF_8_ENCODE)
        VIEWLIFT_DEVICE_MODEL = URLEncoder.encode(Build.MODEL, UTF_8_ENCODE)
        VIEWLIFT_OS_VER = Build.VERSION.RELEASE
        if (appCMSPresenter.appCMSMain != null && appCMSPresenter.appCMSMain.emailConsent != null && appCMSPresenter.appCMSMain.emailConsent.defaultMessage != null)
            VIEWLIFT_CONSENT = URLEncoder.encode(appCMSPresenter.appCMSMain.emailConsent.defaultMessage, UTF_8_ENCODE)

        if (appCMSPresenter.appCMSMain.compliance != null) {
            VIEWLIFT_GDPR = appCMSPresenter.appCMSMain.compliance.isGdpr.toString()
            VIEWLIFT_COPPA = appCMSPresenter.appCMSMain.compliance.isCoppa.toString()
        }

        if (appCMSPresenter.appCMSMain != null && contentDatum?.gist != null) {
            VIEWLIFT_CONTENT_ID = contentDatum.gist.id
            VIEWLIFT_VIDEO_ID = VIEWLIFT_CONTENT_ID
            VIEWLIFT_MEDIA_ID = VIEWLIFT_CONTENT_ID
            VIEWLIFT_CONTENT_DURATION = contentDatum.gist.runtime.toString()
            VIEWLIFT_CONTENT_DURATION_MS = (contentDatum.gist.runtime * 1000).toString()

            if (contentDatum?.gist?.description != null)
                VIEWLIFT_DESCRIPTION = URLEncoder.encode(contentDatum.gist.description, UTF_8_ENCODE)

            if (contentDatum?.gist?.permalink != null) {
                val filmUrl = appCMSPresenter.appCMSMain.domainName.plus(contentDatum.gist.permalink)
                val httpsScheme = context.getString(R.string.https_scheme)
                VIEWLIFT_ENCODED_URL = if (filmUrl.startsWith(httpsScheme)) filmUrl else httpsScheme.plus(filmUrl)
            }
            if (contentDatum?.gist?.seriesTitle != null)
                VIEWLIFT_CONTENT_SERIES_TITLE = URLEncoder.encode(contentDatum.gist.seriesTitle, UTF_8_ENCODE)
            if(contentDatum?.gist?.primaryCategory?.title != null)
                VIEWLIFT_CONTENT_GENRE = URLEncoder.encode(contentDatum.gist.primaryCategory.title, UTF_8_ENCODE)
            if (contentDatum?.gist?.title != null) {
                VIEWLIFT_ENCODED_VIDEO_TITLE = URLEncoder.encode(contentDatum.gist.title, UTF_8_ENCODE)
                VIEWLIFT_CONTENT_TITLE = VIEWLIFT_ENCODED_VIDEO_TITLE
            }
        }
        VIEWLIFT_US_PRIVACY = "1YNN"
        if (appCMSPresenter.isUserLoggedIn && !appCMSPresenter.appPreference.getEmailConsentChecked()) {
            VIEWLIFT_US_PRIVACY = "1YYN"
        }

        if (Utils.isFireTVDevice(context)) {
            var advertisingID: String? = null
            try {
                advertisingID = Settings.Secure.getString(context.contentResolver, "advertising_id")
            } catch (ex: Settings.SettingNotFoundException) {
                advertisingID = null
            }
            if (advertisingID == null) {
                advertisingID = Utils.getAddId()
            }
            if (advertisingID == null) {
                advertisingID = appCMSPresenter.deviceId
            }
            VIEWLIFT_DEVICE_ID = advertisingID
            VIEWLIFT_OS = "Fire_OS"
        } else {
            var advertisingID = Utils.getAddId()
            if (advertisingID == null) {
                advertisingID = Utils.getAdvertisingID(context)
            }
            if (advertisingID == null) {
                advertisingID = appCMSPresenter.deviceId
            }
            VIEWLIFT_DEVICE_ID = advertisingID
            VIEWLIFT_APP_STORE_URL = context.resources.getString(R.string.google_play_store_upgrade_app_url,
                    context.getString(R.string.package_name))
        }
    }

    /**
     * Replace macros with respective values
     *
     * @author Abhishek
     * @since 2020-09-23
     * @param url - adurl or video play url
     */

    fun replaceURl(url: String): String {
        var replacedURL = url
        if (replacedURL.contains(context.getString(R.string.device_os)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_os), VIEWLIFT_OS)

        val today = Date()
        if (replacedURL.contains(context.getString(R.string.time_stamp)))
            replacedURL = replacedURL.replace(context.getString(R.string.time_stamp), (today.time / 1000).toString())

        if (replacedURL.contains(context.getString(R.string.time_stamp_ms)))
            replacedURL = replacedURL.replace(context.getString(R.string.time_stamp_ms), today.time.toString())

        if (replacedURL.contains(context.getString(R.string.device_make)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_make), VIEWLIFT_DEVICE_MAKE)

        if (replacedURL.contains(context.getString(R.string.dnt)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_make), "1")

        if (replacedURL.contains(context.getString(R.string.device_model)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_model), VIEWLIFT_DEVICE_MODEL)

        if (replacedURL.contains(context.getString(R.string.device_os_version)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_os_version), VIEWLIFT_OS_VER)

        if (replacedURL.contains(context.getString(R.string.device_width)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_width), VIEWLIFT_WIDTH)

        if (replacedURL.contains(context.getString(R.string.device_height)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_height), VIEWLIFT_HEIGHT)

        if (replacedURL.contains(context.getString(R.string.device_dimens)))
            replacedURL = replacedURL.replace(context.getString(R.string.device_dimens), VIEWLIFT_SIZE)

        if (replacedURL.contains(context.getString(R.string.domain)))
            replacedURL = replacedURL.replace(context.getString(R.string.domain), VIEWLIFT_DOMAIN)

        if (replacedURL.contains(context.getString(R.string.user_agent)))
            replacedURL = replacedURL.replace(context.getString(R.string.user_agent), VIEWLIFT_USER_AGENT)

        if (replacedURL.contains(context.getString(R.string.application_bundle)))
            replacedURL = replacedURL.replace(context.getString(R.string.application_bundle), VIEWLIFT_APP_BUNDLE)

        if (replacedURL.contains(context.getString(R.string.application_name)))
            replacedURL = replacedURL.replace(context.getString(R.string.application_name), VIEWLIFT_APP_NAME)

        if (replacedURL.contains(context.getString(R.string.brand_name)))
            replacedURL = replacedURL.replace(context.getString(R.string.brand_name), VIEWLIFT_BRAND_NAME)

        if (replacedURL.contains(context.getString(R.string.macro_autoplay)))
            replacedURL = replacedURL.replace(context.getString(R.string.macro_autoplay), VIEWLIFT_AUTOPLAY)

        if (replacedURL.contains(context.getString(R.string.max_pod_duration)))
            replacedURL = replacedURL.replace(context.getString(R.string.max_pod_duration), VIEWLIFT_MAX_POD_DURATION)

        if (replacedURL.contains(context.getString(R.string.mute)))
            replacedURL = replacedURL.replace(context.getString(R.string.mute), "0")

        if (replacedURL.contains(context.getString(R.string.max_pod_duration_ms)))
            replacedURL = replacedURL.replace(context.getString(R.string.max_pod_duration_ms), VIEWLIFT_MAX_POD_DURATION_MS)

        if (replacedURL.contains(context.getString(R.string.encoded_url)))
            VIEWLIFT_ENCODED_URL?.let { replacedURL = replacedURL.replace(context.getString(R.string.encoded_url), URLEncoder.encode(it, UTF_8_ENCODE)) }

        if (replacedURL.contains(context.getString(R.string.double_encoded_url)))
            VIEWLIFT_ENCODED_URL?.let { replacedURL = replacedURL.replace(context.getString(R.string.encoded_url), URLEncoder.encode(URLEncoder.encode(it, UTF_8_ENCODE), UTF_8_ENCODE)) }

        if (replacedURL.contains(context.getString(R.string.viewlift_url)))
            VIEWLIFT_URL?.let { replacedURL = replacedURL.replace(context.getString(R.string.viewlift_url), URLEncoder.encode(it, UTF_8_ENCODE)) }

        if (replacedURL.contains(context.getString(R.string.content_description)))
            VIEWLIFT_DESCRIPTION?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_description), it) }

        if (replacedURL.contains(context.getString(R.string.device_ip)))
            VIEWLIFT_IP?.let { replacedURL = replacedURL.replace(context.getString(R.string.device_ip), it) }

        if (replacedURL.contains(context.getString(R.string.min_duration)))
            VIEWLIFT_MIN_DURATION?.let { replacedURL = replacedURL.replace(context.getString(R.string.min_duration), it) }

        if (replacedURL.contains(context.getString(R.string.max_duration)))
            VIEWLIFT_MAX_DURATION?.let { replacedURL = replacedURL.replace(context.getString(R.string.max_duration), it) }

        if (replacedURL.contains(context.getString(R.string.cache_buster)))
            VIEWLIFT_CACHEBUSTER?.let { replacedURL = replacedURL.replace(context.getString(R.string.cache_buster), it) }

        if (replacedURL.contains(context.getString(R.string.device_id)))
            VIEWLIFT_DEVICE_ID?.let { replacedURL = replacedURL.replace(context.getString(R.string.device_id), it) }

        if (replacedURL.contains(context.getString(R.string.device_latitude)))
            VIEWLIFT_LAT?.let { replacedURL = replacedURL.replace(context.getString(R.string.device_latitude), it) }

        if (replacedURL.contains(context.getString(R.string.device_longitude)))
            VIEWLIFT_LON?.let { replacedURL = replacedURL.replace(context.getString(R.string.device_longitude), it) }

        if (replacedURL.contains(context.getString(R.string.iab)))
            VIEWLIFT_IAB?.let { replacedURL = replacedURL.replace(context.getString(R.string.iab), it) }

        if (replacedURL.contains(context.getString(R.string.ad_pos)))
            VIEWLIFT_AD_POS?.let { replacedURL = replacedURL.replace(context.getString(R.string.ad_pos), it) }

        if (replacedURL.contains(context.getString(R.string.app_store_url)))
            VIEWLIFT_APP_STORE_URL?.let { replacedURL = replacedURL.replace(context.getString(R.string.app_store_url), it) }

        if (replacedURL.contains(context.getString(R.string.keywords)))
            VIEWLIFT_KEYWORDS?.let { replacedURL = replacedURL.replace(context.getString(R.string.keywords), it) }

        if (replacedURL.contains(context.getString(R.string.content_id)))
            VIEWLIFT_CONTENT_ID?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_id), it) }

        if (replacedURL.contains(context.getString(R.string.media_id)))
            VIEWLIFT_MEDIA_ID?.let { replacedURL = replacedURL.replace(context.getString(R.string.media_id), it) }

        if (replacedURL.contains(context.getString(R.string.content_encoded_title)))
            VIEWLIFT_ENCODED_VIDEO_TITLE?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_encoded_title), it) }

        if (replacedURL.contains(context.getString(R.string.content_title)))
            VIEWLIFT_CONTENT_TITLE?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_title), it) }

        if (replacedURL.contains(context.getString(R.string.video_id)))
            VIEWLIFT_VIDEO_ID?.let { replacedURL = replacedURL.replace(context.getString(R.string.video_id), it) }

        if (replacedURL.contains(context.getString(R.string.consent)))
            VIEWLIFT_GDPR?.let { replacedURL = replacedURL.replace(context.getString(R.string.consent), it) }

        if (replacedURL.contains(context.getString(R.string.gdpr)))
            VIEWLIFT_CONSENT?.let { replacedURL = replacedURL.replace(context.getString(R.string.gdpr), it) }

        if (replacedURL.contains(context.getString(R.string.us_privacy)))
            VIEWLIFT_US_PRIVACY?.let { replacedURL = replacedURL.replace(context.getString(R.string.us_privacy), it) }

        if (replacedURL.contains(context.getString(R.string.coppa)))
            VIEWLIFT_COPPA?.let { replacedURL = replacedURL.replace(context.getString(R.string.coppa), it) }

        if (replacedURL.contains(context.getString(R.string.content_season)))
            VIEWLIFT_CONTENT_SEASON?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_season), it) }

        if (replacedURL.contains(context.getString(R.string.content_episode)))
            VIEWLIFT_CONTENT_EPISODE?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_episode), it) }

        if (replacedURL.contains(context.getString(R.string.content_series_title)))
            VIEWLIFT_CONTENT_SERIES_TITLE?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_series_title), it) }

        if (replacedURL.contains(context.getString(R.string.content_genre)))
            VIEWLIFT_CONTENT_GENRE?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_genre), it) }

        if (replacedURL.contains(context.getString(R.string.content_genre)))
            VIEWLIFT_CONTENT_DURATION_MS?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_duration_ms), it) }

        if (replacedURL.contains(context.getString(R.string.content_genre)))
            VIEWLIFT_CONTENT_DURATION?.let { replacedURL = replacedURL.replace(context.getString(R.string.content_duration), it) }

        if (replacedURL.contains(context.getString(R.string.duration)))
            VIEWLIFT_CONTENT_DURATION?.let { replacedURL = replacedURL.replace(context.getString(R.string.duration), it) }

        return replacedURL
    }

    fun setContext(con: Context) {
        context = con
    }

}

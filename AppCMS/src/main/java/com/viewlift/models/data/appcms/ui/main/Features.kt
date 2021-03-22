package com.viewlift.models.data.appcms.ui.main

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Features(@SerializedName("coppa_enabled")
                    @Expose
                    val isCoppaEnabled: Boolean,
                    @SerializedName("mobile_app_downloads")
                    @Expose
                    val isMobileAppDownloads: Boolean,
                    @SerializedName("showDetailToggle")
                    @Expose
                    val isOpenShowDetail: Boolean,
                    @SerializedName("enable_article_hardwall")
                    @Expose
                    val isArticleHardwallEnabled: Boolean,
                    @SerializedName("user_content_rating")
                    @Expose
                    val isUserContentRating: Boolean,
                    @SerializedName("audio_preview")
                    @Expose
                    val audioPreview: AudioPreview? = null,
                    @SerializedName("ratings")
                    @Expose
                    val ratings: Ratings? = null,
                    @SerializedName("watched_history")
                    @Expose
                    val watchedHistory: WatchedHistory? = null,
                    @SerializedName("analytics_beacon")
                    @Expose
                    val analyticsBeacon: AnalyticsBeacon? = null,
                    @SerializedName("enableQOS")
                    @Expose
                    val isEnableQOS: Boolean,
                    @SerializedName("free_preview")
                    @Expose
                    val freePreview: FreePreview? = null,
                    @SerializedName("auto_play")
                    @Expose
                    val isAutoPlay: Boolean,
                    @SerializedName("otp_value")
                    @Expose
                    val otpValue: OtpValue? = null,
                    @SerializedName("parental_control")
                    @Expose
                    val isParentalControlEnable: Boolean,
                    @SerializedName("portrait_viewing")
                    @Expose
                    val isPortraitViewing: Boolean,
                    @SerializedName("web_subscription_only")
                    @Expose
                    val isWebSubscriptionOnly: Boolean,
                    @SerializedName("mute_sound")
                    @Expose
                    val isMuteSound: Boolean,
                    @SerializedName("casting")
                    @Expose
                    val isCasting: Boolean,
                    @SerializedName("customReceiverId")
                    @Expose
                    val customReceiverId: String,
                    @SerializedName("hls")
                    @Expose
                    val isHls: Boolean,
                    @SerializedName("trick_play")
                    @Expose
                    val isTrickPlay: Boolean,
                    @SerializedName("orientation")
                    @Expose
                    val orientation: Orientation? = null,
                    @SerializedName("extendSubscriptionBanner")
                    @Expose
                    val isExtendSubscriptionBanner: Boolean,
                    @SerializedName("math_problem")
                    @Expose
                    val isMathProblemEnabled: Boolean,
                    @SerializedName(value = "login_module", alternate = ["splash_module_mobile"])
                    @Expose
                    val isSplashModule: Boolean,
                    @SerializedName("navigation")
                    @Expose
                    val navigationType: String? = null,
                    @SerializedName("enable_mini_player")
                    @Expose
                    val isEnableMiniPlayer: Boolean,
                    @SerializedName("showDetailOnlyOnApps")
                    @Expose
                    val isShowDetailOnlyOnApps: Boolean,
                    @SerializedName("enableSubscribeNow")
                    @Expose
                    val isEnableSubscribeNow: Boolean,

                    @SerializedName("login_module_enabled")
                    @Expose
                    val isLoginModuleEnable: Boolean,

                    @SerializedName("signup_module_enabled")
                    @Expose
                    val isSignupModuleEnable: Boolean,
                    @SerializedName("hideWatchlist")
                    @Expose
                    val isWatchlistHidden: Boolean,

                    @SerializedName("whatsappConsent")
                    @Expose
                    var whatsAppConsent: WhatsAppConsent? = null,
                    @SerializedName("resendOtpTimer")
                    @Expose
                    val resendOtpTimer: Int?) : Serializable

data class FreePreview(@SerializedName("isFreePreview") @Expose
                       val isFreePreview: Boolean,
                       @SerializedName("length")
                       @Expose
                       val length: Length? = null,
                       @SerializedName("per_video")
                       @Expose
                       val isPerVideo: Boolean) : Serializable

data class AudioPreview(@SerializedName("isAudioPreview") @Expose
                        val isAudioPreview: Boolean,

                        @SerializedName("length")
                        @Expose
                        val length: Length) : Serializable

data class Length(@SerializedName("unit") @Expose
                  val unit: String,
                  @SerializedName("multiplier")
                  val multiplier: String) : Serializable

data class OtpValue(@SerializedName("otp_enabled") @Expose
                    val isOtpEnabled: Boolean,
                    @SerializedName("email_required")
                    @Expose
                    val isEmailRequired: Boolean,
                    @SerializedName("excludedCountry")
                    @Expose
                    val excludedCountry: List<String>,
                    @SerializedName("manageProfile")
                    @Expose
                    val isManageProfile: Boolean,
                    @SerializedName("mobile_updation")
                    @Expose
                    val isMobileUpdateRequired: Boolean) : Serializable

data class Orientation(@SerializedName("tablet") @Expose
                       val tablet: Tablet? = null) : Serializable

data class Tablet(@SerializedName("landscape") @Expose
                  val isLandscape: Boolean,
                  @SerializedName("portrait")
                  @Expose
                  val isPortrait: Boolean) : Serializable
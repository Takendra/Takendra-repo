package com.viewlift.models.data.appcms.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.ui.android.NavigationLocalizationMap;
import com.viewlift.models.data.appcms.ui.page.PrimaryCta;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class GenericMessages extends NavigationLocalizationMap implements Serializable {
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("forgotPasswordCtaText")
    @Expose
    String forgotPasswordCtaText;
    @SerializedName("primaryCta")
    @Expose
    PrimaryCta primaryCta;
    @SerializedName("internetErrorMessageHeader")
    @Expose
    String internetErrorMessageHeader;
    @SerializedName("errorDialogCloseCta")
    @Expose
    String errorDialogCloseCta;
    @SerializedName("errorDialogCancelCta")
    @Expose
    String errorDialogCancelCta;
    @SerializedName("errorDialogNoCta")
    @Expose
    String errorDialogNoCta;
    @SerializedName("errorDialogYesCta")
    @Expose
    String errorDialogYesCta;
    @SerializedName("errorDialogSignInCta")
    @Expose
    String errorDialogSignInCta;
    @SerializedName("errorDialogRetryCta")
    @Expose
    String errorDialogRetryCta;
    @SerializedName("errorDialogOkCta")
    @Expose
    String errorDialogOkCta;
    @SerializedName("logoutDialogMessage")
    @Expose
    String logoutDialogMessage;
    @SerializedName("logoutVideoDownloadDialogTitle")
    @Expose
    String logoutVideoDownloadDialogTitle;
    @SerializedName("logoutVideoDownloadDialogMessage")
    @Expose
    String logoutVideoDownloadDialogMessage;
    @SerializedName("tncDetails")
    @Expose
    String tncDetails;
    @SerializedName("privacyPolicyLabel")
    @Expose
    String privacyPolicyLabel;
    @SerializedName("termsOfUseLabel")
    @Expose
    String termsOfUseLabel;
    @SerializedName("kInternetConnectionPostString")
    @Expose
    String kInternetConnectionPostString;
    @SerializedName("kError")
    @Expose
    String kError;
    @SerializedName("contentRatingTextWarningLabel")
    @Expose
    String contentRatingTextWarningLabel;
    @SerializedName("contentRatingDescriptionLabel")
    @Expose
    String contentRatingDescriptionLabel;
    @SerializedName("contentRatingViewerDiscretionLabel")
    @Expose
    String contentRatingViewerDiscretionLabel;
    @SerializedName("guestUserSubscriptionMsg")
    @Expose
    String guestUserSubscriptionMsg;
    @SerializedName("touchToCastMsg")
    @Expose
    String touchToCastMsg;
    @SerializedName("seeAllTray")
    @Expose
    String seeAllTray;
    @SerializedName("downloadUnavailableDialogTitle")
    @Expose
    String downloadUnavailableDialogTitle;
    @SerializedName("downloadPLayLimitDialogMessage")
    @Expose
    String downloadPLayLimitDialogMessage;
    @SerializedName("downloadSpaceDialogTitle")
    @Expose
    String downloadSpaceDialogTitle;
    @SerializedName("downloadSpaceDialogMessage")
    @Expose
    String downloadSpaceDialogMessage;
    @SerializedName("videoNotPlayableDialogTitle")
    @Expose
    String videoNotPlayableDialogTitle;
    @SerializedName("alertDialogTitle")
    @Expose
    String alertDialogTitle;
    @SerializedName("subscribeNowDialogButton")
    @Expose
    String subscribeNowDialogButton;
    @SerializedName("subscriptionRequiredDialogTitle")
    @Expose
    String subscriptionRequiredDialogTitle;
    @SerializedName("subscriptionRequiredDialogMessage")
    @Expose
    String subscriptionRequiredDialogMessage;
    @SerializedName("premiumContentDialogTitle")
    @Expose
    String premiumContentDialogTitle;
    @SerializedName("premiumContentLoggedInUserDialogMessage")
    @Expose
    String premiumContentLoggedInUserDialogMessage;
    @SerializedName("invalidEmailDialogTitle")
    @Expose
    String invalidEmailDialogTitle;
    @SerializedName("livePreviewMessageSportsFitness")
    @Expose
    String livePreviewMessageSportsFitness;
    @SerializedName("livePreviewMessageOther")
    @Expose
    String livePreviewMessageOther;
    @SerializedName("previewEndDialogTitle")
    @Expose
    String previewEndDialogTitle;
    @SerializedName("audioPreviewMessage")
    @Expose
    String audioPreviewMessage;
    @SerializedName("castOverlayMessage")
    @Expose
    String castOverlayMessage;
    @SerializedName("stopCasting")
    @Expose
    String stopCasting;
    @SerializedName("castMsgSuffix")
    @Expose
    String castMsgSuffix;
    @SerializedName("moreLabel")
    @Expose
    String moreLabel;
    @SerializedName("kStrTryAgain")
    String kStrTryAgain;
    @SerializedName("andLabel")
    String andLabel;
    @SerializedName("noSubscriptionMsg")
    String noSubscriptionMsg;
    @SerializedName("serverErrorMsgText")
    String serverErrorMsgText;
    @SerializedName("subscriptionFromAndroidMsg")
    String subscriptionFromAndroidMsg;
    @SerializedName("continueButton")
    String continueButton;
    @SerializedName("errorDialogBackCta")
    String errorDialogBackCta;
    @SerializedName("recommendationTitle")
    String recommendationTitle;
    @SerializedName("recommendationSubTitle")
    String recommendationSubTitle;
    @SerializedName("watchNowCtaNbc")
    @Expose
    String watchNowCtaNbc;
    @SerializedName("doneCta")
    @Expose
    String doneCta;
    @SerializedName("findAdultForHelpMessage")
    @Expose
    String findAdultForHelpMessage;
    @SerializedName("grabGrownUpMessage")
    @Expose
    String grabGrownUpMessage;
    @SerializedName("showForBigKidsMessage")
    @Expose
    String showForBigKidsMessage;
    @SerializedName("answerMathProblemMessage")
    String answerMathProblemMessage;
    @SerializedName("recommendationSelectionEmptyMessage")
    String recommendationSelectionEmptyMessage;

    @SerializedName("recommendSkipButtonText")
    @Expose
    String recommendSkipButtonText;
    @SerializedName("pressHoldContinue")
    @Expose
    String pressHoldContinue;
    @SerializedName("recommendSaveButtonText")
    String recommendSaveButtonText;

    @SerializedName("scheduleToBeCancelledText")
    @Expose
    String subscriptionScheduleToCancelOn;

    @SerializedName("openBrowserText")
    @Expose
    String openBrowserText;

    @SerializedName("kResubscribeAlertTitle")
    @Expose
    String reSubscribe;

    @SerializedName("editAccount")
    @Expose
    String editAccount;

    @SerializedName("week")
    @Expose
    String week;

    @SerializedName("dayUpper")
    @Expose
    String dayUpper;

    @SerializedName("kResubscribeAppleAlert")
    @Expose
    String previousSubscriptionPlatform_iosMSG;

    @SerializedName("kResubscribeAndroidAlert")
    @Expose
    String previousSubscriptionPlatform_androidMSG;

    @SerializedName("kResubscribeWebAlert")
    @Expose
    String previousSubscriptionPlatform_webMSG;

    @SerializedName("kResubscribeRokuAlert")
    @Expose
    String previousSubscriptionPlatform_rokuMSG;

    @SerializedName("kResubscribeFireTVAlert")
    @Expose
    String previousSubscriptionPlatform_amazonMSG;

    @SerializedName("kResubscribeWindowsAlert")
    @Expose
    String previousSubscriptionPlatform_windowsMSG;

    @SerializedName("kResubscribeSmartTVAlert")
    @Expose
    String previousSubscriptionPlatform_smartTvMSG;

    @SerializedName("kResubscribeOtherAlert")
    @Expose
    String previousSubscriptionPlatform_otherMSG;

    @SerializedName("resubscribeAndroidWithOtherProcessorAlert")
    @Expose
    String previousSubscriptionPlatform_androidWithOtherProcessorMSG;

    @SerializedName("kResubscribePs4Alert")
    @Expose
    String previousSubscriptionPlatform_ps4MSG;

    @SerializedName("watchNowCta")
    @Expose
    String watchNowCta;

    @SerializedName("hoverSeasonsLabel")
    @Expose
    String hoverSeasonsLabel;

    @SerializedName("timePeriodVerbiage")
    @Expose
    String timePeriodVerbiage;

    @SerializedName("downloadAlreadyErrorTitle")
    @Expose
    String downloadAlreadyErrorTitle;

    @SerializedName("alreadyDownloadedSameQuality")
    @Expose
    String alreadyDownloadedSameQuality;

    @SerializedName("highQualityDownload")
    @Expose
    String highQualityDownload;

    @SerializedName("mediumDownloadQuality")
    @Expose
    String mediumDownloadQuality;

    @SerializedName("lowDownloadQuality")
    @Expose
    String lowDownloadQuality;

    @SerializedName("recommendStartBrowsingText")
    String recommendStartBrowsingText;
    @SerializedName("personalizeSettingsHeader")
    @Expose
    String personalizeSettingsHeader;
    @SerializedName("managePersonalization")
    String managePersonalization;
    @SerializedName("drmNotDownloaded")
    String drmNotDownloaded;
    @SerializedName("drmNotCasted")
    String drmNotCasted;
    @SerializedName("serverErrorMessageText")
    String serverErrorMessageText;
    @SerializedName("viewPlansCta")
    String viewPlansCta;
    @SerializedName("selectLanguage")
    String selectLanguage;
    @SerializedName("kNoResponseErrorTitle")
    String kNoResponseErrorTitle;
    @SerializedName("menuTitle")
    String menuTitle;
    @SerializedName("noSubscriptionMessage")
    String noSubscriptionMessage;
    @SerializedName("subscriptionFromAndroidMessage")
    String subscriptionFromAndroidMessage;
    @SerializedName("subscriptionFromWebsiteMsg")
    String kStrUnableFetchData;
    @SerializedName("guestUserSubscriptionMessage")
    String guestUserSubscriptionMessage;
    @SerializedName("kNoResponseErrorMessage")
    String kNoResponseErrorMessage;
    @SerializedName("continueCta")
    String continueCta;
    @SerializedName("titleDownloadSettingLabel")
    String titleDownloadSettingLabel;
    @SerializedName("secondLabel")
    @Expose
    String secondLabel;
    @SerializedName("secondsLabel")
    @Expose
    String secondsLabel;
    @SerializedName("secondLabelFull")
    @Expose
    String secondLabelFull;
    @SerializedName("secondsLabelFull")
    @Expose
    String secondsLabelFull;
    @SerializedName("NAME_NOT_VALID")
    @Expose
    String NAME_NOT_VALID;
    @SerializedName("finishedTitleLabel")
    @Expose
    String finishedTitleLabel;
    @SerializedName("playCta")
    @Expose
    String playCta;
    @SerializedName("playInLabel")
    @Expose
    String playInLabel;
    @SerializedName("contentNotEnabledMessagePrefix")
    @Expose
    String contentNotEnabledMessagePrefix;
    @SerializedName("contentNotEnabledMessageSuffix")
    @Expose
    String contentNotEnabledMessageSuffix;
    @SerializedName("rentStartMessagePrefix")
    @Expose
    String rentStartMessagePrefix;
    @SerializedName("rentStartMessageSuffix")
    @Expose
    String rentStartMessageSuffix;
    @SerializedName("geoRestrictErrorMessage")
    @Expose
    String geoRestrictErrorMessage;
    @SerializedName("cellularDisabledErrorMsg")
    @Expose
    String cellularDisabledErrorMsg;
    @SerializedName("shareVideo")
    @Expose
    String shareVideo;
    @SerializedName("castingLoadingMessage")
    @Expose
    String castingLoadingMessage;
    @SerializedName("loginSubscriptionDialogTitle")
    @Expose
    String loginSubscriptionDialogTitle;
    @SerializedName("loginSubscriptionDialogMessage")
    @Expose
    String loginSubscriptionDialogMessage;
    @SerializedName("loginRequiredDialogTitle")
    @Expose
    String loginRequiredDialogTitle;
    @SerializedName("loginRequiredDialogMessage")
    @Expose
    String loginRequiredDialogMessage;
    @SerializedName("entitlementLoginErrorMessage")
    @Expose
    String entitlementLoginErrorMessage;
    @SerializedName("streamingInfoErrorTitle")
    @Expose
    String streamingInfoErrorTitle;
    @SerializedName("streamingInfoErrorMessage")
    @Expose
    String streamingInfoErrorMessage;
    @SerializedName("appUpdatePrefix")
    @Expose
    String appUpdatePrefix;
    @SerializedName("appUpdateSuffix")
    @Expose
    String appUpdateSuffix;
    @SerializedName("updateAppCta")
    @Expose
    String updateAppCta;
    @SerializedName("updateAvailable")
    @Expose
    String updateAvailable;
    @SerializedName("cellularDisabledErrorTitle")
    @Expose
    String cellularDisabledErrorTitle;
    @SerializedName("downloadExternalStorageTitle")
    @Expose
    String downloadExternalStorageTitle;
    @SerializedName("downloadExternalStorageMessage")
    @Expose
    String downloadExternalStorageMessage;
    @SerializedName("minutesLabel")
    @Expose
    String minutesLabel;
    @SerializedName("minuteLabel")
    @Expose
    String minuteLabel;
    @SerializedName("errorDialogSignUpCta")
    @Expose
    String errorDialogSignUpCta;
    @SerializedName("searchHeaderLabel")
    @Expose
    String searchHeaderLabel;
    @SerializedName("playerSettingsUnavailable")
    @Expose
    String playerSettingsUnavailable;
    @SerializedName("audioLanguage")
    @Expose
    String audioLanguage;
    @SerializedName("closedCaptions")
    @Expose
    String closedCaptions;
    @SerializedName("playbackQuality")
    @Expose
    String playbackQuality;
    @SerializedName("loadingVideo")
    @Expose
    String loadingVideo;
    @SerializedName("castErrTitle")
    @Expose
    String castErrTitle;
    @SerializedName("articleTrayHeader")
    @Expose
    String articleTrayHeader;
    @SerializedName("audioTrayHeader")
    @Expose
    String audioTrayHeader;
    @SerializedName("bundleTrayHeader")
    @Expose
    String bundleTrayHeader;
    @SerializedName("galleryTrayHeader")
    @Expose
    String galleryTrayHeader;
    @SerializedName("bundleSeriesTrayHeader")
    @Expose
    String bundleSeriesTrayHeader;
    @SerializedName("seriesTrayHeader")
    @Expose
    String seriesTrayHeader;
    @SerializedName("programsTrayHeader")
    @Expose
    String programsTrayHeader;
    @SerializedName("episodesTrayHeader")
    @Expose
    String episodesTrayHeader;
    @SerializedName("videosTrayHeader")
    @Expose
    String videosTrayHeader;
    @SerializedName("videoplaylistTrayHeader")
    @Expose
    String videoplaylistTrayHeader;
    @SerializedName("goCta")
    @Expose
    String goCta;
    @SerializedName("itemQueueUnavailable")
    @Expose
    String itemQueueUnavailable;
    @SerializedName("alreadyDownloaded")
    @Expose
    String alreadyDownloaded;
    @SerializedName("downloadStarted")
    @Expose
    String downloadStarted;
    @SerializedName("addToDownloadQueue")
    @Expose
    String addToDownloadQueue;
    @SerializedName("fetchVideoQualities")
    @Expose
    String fetchVideoQualities;
    @SerializedName("emptySearchField")
    @Expose
    String emptySearchField;
    @SerializedName("noSearchresult")
    @Expose
    String noSearchresult;
    @SerializedName("songs")
    @Expose
    String songsHeader;
    @SerializedName("videoNotLoaded")
    @Expose
    String videoNotLoaded;
    @SerializedName("publishedOn")
    @Expose
    String publishedOn;
    @SerializedName("notPurchased")
    @Expose
    String notPurchased;
    @SerializedName("autoHlsResolution")
    @Expose
    String autoHlsResolution;
    @SerializedName("castingTo")
    @Expose
    String castingTo;
    @SerializedName("encourageUserToLoginLabel")
    @Expose
    String encourageUserToLoginLabel;
    @SerializedName("noResult")
    @Expose
    String noResult;
    @SerializedName("noPreviousAudio")
    @Expose
    String noPreviousAudio;
    @SerializedName("noNextAudio")
    @Expose
    String noNextAudio;
    @SerializedName("liveStreaming")
    @Expose
    String liveStreaming;
    @SerializedName("premiumContentGuestUserDialogMessage")
    @Expose
    String premiumContentGuestUserDialogMessage;
    @SerializedName("autoPlayoffMessage")
    @Expose
    String autoPlayoffMessage;
    @SerializedName("noVideoInQueue")
    @Expose
    String noVideoInQueue;
    @SerializedName("castMsgPrefix")
    @Expose
    String castMsgPrefix;
    @SerializedName("entitlementErrorMessageForDownload")
    @Expose
    String entitlementErrorMessageForDownload;
    @SerializedName("alreadyDownloadedOtherUser")
    @Expose
    String alreadyDownloadedOtherUser;
    @SerializedName("userOnlineTimeAlert")
    @Expose
    String userOnlineTimeAlert;
    @SerializedName("episodeLabel")
    @Expose
    String episodeLabel;
    @SerializedName("song")
    @Expose
    String song;
    @SerializedName("photos")
    @Expose
    String photos;
    @SerializedName("photo")
    @Expose
    String photo;
    @SerializedName("noFight")
    @Expose
    String noFight;
    @SerializedName("faceOff")
    @Expose
    String faceOff;
    @SerializedName("by")
    @Expose
    String by;
    @SerializedName("timerLabel")
    @Expose
    String timerLabel;
    @SerializedName("downloaded")
    @Expose
    String downloaded;
    @SerializedName("downloadLowerCase")
    @Expose
    String downloadLowerCase;
    @SerializedName("addToWatchlist")
    @Expose
    String addToWatchlist;
    @SerializedName("addToSavedCTA")
    @Expose
    String addToSaved;
    @SerializedName("removeFromWatchlist")
    @Expose
    String removeFromWatchlist;
    @SerializedName("removeFromSavedCTA")
    @Expose
    String removeFromSaved;
    @SerializedName("checkExistingSubscription")
    @Expose
    String checkExistingSubscription;
    @SerializedName("existingSubscriptionDoesNotExist")
    @Expose
    String existingSubscriptionDoesNotExist;
    @SerializedName("existingUserLogin")
    @Expose
    String existingUserLogin;
    @SerializedName("billingResponseError")
    @Expose
    String billingResponseError;
    @SerializedName("manageCta")
    @Expose
    String manageCta;
    @SerializedName("playersTrayHeader")
    @Expose
    String playersTrayHeader;
    @SerializedName("downloadedLabel")
    @Expose
    String downloadedLabel;
    @SerializedName("downloadingLabel")
    @Expose
    String downloadingLabel;
    @SerializedName("removedFromWatchlist")
    @Expose
    String removedFromWatchlist;
    @SerializedName("addedToWatchlistLabel")
    @Expose
    String addedToWatchlistLabel;
    @SerializedName("failedToAddToWatchlist")
    @Expose
    String failedToAddToWatchlist;
    @SerializedName("failedToRemoveFromWatchlist")
    @Expose
    String failedToRemoveFromWatchlist;
    @SerializedName("offLabel")
    @Expose
    String offLabel;
    @SerializedName("webSubscriptionMessagePrefix")
    @Expose
    String webSubscriptionMessagePrefix;
    @SerializedName("webSubscriptionMessageSuffix")
    @Expose
    String webSubscriptionMessageSuffix;
    @SerializedName("seasonLabel")
    @Expose
    String seasonLabel;
    @SerializedName("seasonsLabel")
    @Expose
    String seasonsLabel;
    @SerializedName("loginCtaText")
    @Expose
    String loginCtaText;
    @SerializedName("cancelCta")
    @Expose
    String cancelCta;
    @SerializedName("emptyEmailValidationMessage")
    @Expose
    String emptyEmailValidationMessage;
    @SerializedName("emptyPasswordValidationMessage")
    @Expose
    String emptyPasswordValidationMessage;
    @SerializedName("passwordFormatValidationMessage")
    @Expose
    String passwordFormatValidationMessage;
    @SerializedName("emailFormatValidationMessage")
    @Expose
    String emailFormatValidationMessage;
    @SerializedName("internetErrorMessageText")
    @Expose
    String internetErrorMessageText;
    @SerializedName("subscriptionFromRokuMessage")
    @Expose
    String subscriptionFromRokuMessage;
    @SerializedName("subscriptionFromAppleMessage")
    @Expose
    String subscriptionFromAppleMessage;
    @SerializedName("subscriptionFromWebsiteMessage")
    @Expose
    String subscriptionFromWebsiteMessage;
    @SerializedName("subscriptionMessageHeader")
    @Expose
    String subscriptionMessageHeader;
    @SerializedName("pleaseLoginToViewMyAha")
    @Expose
    String pleaseLoginToViewMyAha;
    @SerializedName("alreadyLoggedIn")
    @Expose
    String alreadyLoggedIn;

    @SerializedName("updateValidNumber")
    @Expose
    String updateValidNumber;

    @SerializedName("pageAvailabilityForLoggedInUser")
    @Expose
    String pageAvailabilityForLoggedInUser;

    @SerializedName("cancelCountdownCta")
    @Expose
    String cancelCountdownCta;
    @SerializedName("justFinishedLabel")
    @Expose
    String justFinishedLabel;
    @SerializedName("upNextLabel")
    @Expose
    String upNextLabel;
    @SerializedName("backCta")
    @Expose
    String backCta;
    @SerializedName("countdownCancelledLabel")
    @Expose
    String countdownCancelledLabel;
    @SerializedName("emailUsAtLabel")
    @Expose
    String emailUsAtLabel;
    @SerializedName("callUsAtLabel")
    @Expose
    String callUsAtLabel;
    @SerializedName("contactUsLabel")
    @Expose
    String contactUsLabel;
    @SerializedName("languageAlertMessage")
    @Expose
    String languageAlertMessage;
    @SerializedName("selectCta")
    @Expose
    String selectCta;
    @SerializedName("selectedCta")
    @Expose
    String selectedCta;
    @SerializedName("languageSelectionConfirmMessage")
    @Expose
    String languageSelectionConfirmMessage;
    @SerializedName("pressDownForMoreContentLabel")
    @Expose
    String pressDownForMoreContentLabel;
    @SerializedName("manageSubsubcription")
    @Expose
    String manageSubsubcription;
    @SerializedName("previousSearchlabel")
    @Expose
    String previousSearchlabel;
    @SerializedName("clearHistoryCta")
    @Expose
    String clearHistoryCta;
    @SerializedName("noResultForLabel")
    @Expose
    String noResultForLabel;
    @SerializedName("resultTitleLabel")
    @Expose
    String resultTitleLabel;
    @SerializedName("myNavItemPrefix")
    @Expose
    String myNavItemPrefix;
    @SerializedName("loadingMessage")
    @Expose
    String loadingMessage;
    @SerializedName("autoplayOnMenu")
    @Expose
    String autoplayOnMenu;
    @SerializedName("autoplayOffMenu")
    @Expose
    String autoplayOffMenu;
    @SerializedName("closedCaptionOnMenu")
    @Expose
    String closedCaptionOnMenu;
    @SerializedName("closedCaptionOffMenu")
    @Expose
    String closedCaptionOffMenu;

    @SerializedName("hoverEpisodesLabel")
    @Expose
    String hoverEpisodesLabel;
    @SerializedName("hoverEpisodeLabel")
    @Expose
    String hoverEpisodeLabel;
    @SerializedName("loginToSeeWatchlistLabel")
    @Expose
    String loginToSeeWatchlistLabel;
    @SerializedName("loginToSeeHistoryLabel")
    @Expose
    String loginToSeeHistoryLabel;
    @SerializedName("tvodContentERRORPrefix")
    @Expose
    String tvodContentERRORPrefix;
    @SerializedName("tvodContentERRORSuffix")
    @Expose
    String tvodContentERRORSuffix;
    @SerializedName("subscriptionLeftMessage")
    @Expose
    String subscriptionLeftMessage;
    @SerializedName("subscriptionExpiredMessage")
    @Expose
    String subscriptionExpiredMessage;
    @SerializedName("subscriptionInitiatedMessage")
    @Expose
    String subscriptionInitiatedMessage;
    @SerializedName("failMessage")
    @Expose
    String failMessage;
    @SerializedName("toPayText")
    @Expose
    String toPayText;
    @SerializedName("retryButton")
    @Expose
    String retryButton;
    @SerializedName("backToHomeButton")
    @Expose
    String backToHomeButton;

    @SerializedName("successMessage")
    @Expose
    String successMessage;
    @SerializedName("failMessageTitle")
    @Expose
    String failMessageTitle;
    @SerializedName("mediaSessionError")
    @Expose
    String mediaSessionError;
    @SerializedName("mediaSessionErrorHeader")
    @Expose
    String mediaSessionErrorHeader;
    @SerializedName("drmNotSupportHeader")
    @Expose
    String drmNotSupportHeader;
    @SerializedName("pendingMessage")
    @Expose
    String pendingMessage;
    @SerializedName("successMessageTitle")
    @Expose
    String successMessageTitle;
    @SerializedName("pendingMessageTitle")
    @Expose
    String pendingMessageTitle;
    @SerializedName("loginTveCta")
    @Expose
    String tveLogin;
    @SerializedName("waysToWatch")
    @Expose
    String waysToWatch;
    @SerializedName("waysToWatchMessage")
    @Expose
    String waysToWatchMessage;
    @SerializedName("becomeAmemberCta")
    @Expose
    String becomeAmemberCta;
    @SerializedName("chooseTvProviderCta")
    @Expose
    String chooseTvProviderCta;
    @SerializedName("ownCta")
    @Expose
    String ownCta;
    @SerializedName("haveAccount")
    @Expose
    String haveAccount;
    @SerializedName("loginTextCta")
    @Expose
    String loginTextCta;
    @SerializedName("planUpgrade")
    @Expose
    String planUpgrade;
    @SerializedName("I agree with Terms of Services and Privacy Policy")
    @Expose
    String agreeTOSPrivacyPolicy;

    @SerializedName("viewingRestrictionsEnabled")
    @Expose
    String viewingRestrictionsEnabled;
    @SerializedName("enterVideoPin")
    @Expose
    String enterVideoPin;
    @SerializedName("inCorrectPinMessage")
    @Expose
    String inCorrectPinMessage;
    @SerializedName("confirmCTA")
    @Expose
    String confirmCTA;
    @SerializedName("faceIdEnabledTitle")
    @Expose
    String faceIdEnabledTitle;
    @SerializedName("touchIdEnabledTitle")
    @Expose
    String touchIdEnabledTitle;
    @SerializedName("faceId")
    @Expose
    String faceId;
    @SerializedName("touchId")
    @Expose
    String touchId;
    @SerializedName("usePin")
    @Expose
    String usePin;
    @SerializedName("ratingPromptConfirmationMessage")
    @Expose
    String ratingPromptConfirmationMessage;

    @SerializedName("ratingProceedAllow")
    @Expose
    String ratingProceedAllow;

    @SerializedName("ratingProceedDeny")
    @Expose
    String ratingProceedDeny;

    @SerializedName("congratulations")
    @Expose
    String congratulations;

    @SerializedName("appExitAlertMessage")
    @Expose
    String appExitAlertMessage;

    @SerializedName("invite_success_message")
    @Expose
    String inviteSuccessMessage;

    @SerializedName("getSocialFreeMessage")
    @Expose
    String getSocialFreeMessage;

    @SerializedName("getSocialShareviaText")
    @Expose
    String getSocialShareviaText;

    @SerializedName("getSocialTermsAndConditions")
    @Expose
    String getSocialTermsAndConditions;

    @SerializedName("getSocialSignInButtonText")
    @Expose
    String getSocialSignInButtonText;

    @SerializedName("getSocialSubscribeButtonText")
    @Expose
    String getSocialSubscribeButtonText;

    @SerializedName("ratingPromptMessage")
    @Expose
    String ratingPromptMessage;
    @SerializedName("MAX_STREAMS_ERROR")
    @Expose
    String maxStreamError;
    @SerializedName("waysToWatchMessageForTVEGuestUser")
    @Expose
    String waysToWatchMessageForTVEGuestUser;
    @SerializedName("enterEmailLabel")
    @Expose
    String enterEmailLabel;
    @SerializedName("EnterEmailAddressMessageForFreePlan")
    @Expose
    String enterEmailAddressMessageForFreePlan;
    @SerializedName("subscriptionFromAmazonMessage")
    @Expose
    String subscriptionFromAmazonMessage;
    @SerializedName("smallFont")
    @Expose
    String smallFont;
    @SerializedName("regularFont")
    @Expose
    String regularFont;
    @SerializedName("largeFont")
    @Expose
    String largeFont;
    @SerializedName("nextDateLabel")
    @Expose
    String nextDateLabel;
    @SerializedName("ContentNotAvailableforTVProvider")
    @Expose
    String contentNotAvailableTVProvider;

    public String getContentNotAvailableTVProvider() {
        return contentNotAvailableTVProvider;
    }

    @SerializedName("inAppUpdateDownloadMessage")
    @Expose
    String inAppUpdateDownloadMessage;

    @SerializedName("inAppUpdateInstallLabel")
    @Expose
    String inAppUpdateInstallLabel;
    @SerializedName("rentOptionsLabel")
    @Expose
    String rentOptionsLabel;
    @SerializedName("rentOverlayLabel")
    @Expose
    String rentOverlayLabel;
    @SerializedName("purchaseOverlayLabel")
    @Expose
    String purchaseOverlayLabel;
    @SerializedName("purchaseOptionsLabel")
    @Expose
    String purchaseOptionsLabel;
    @SerializedName("rentedLabel")
    @Expose
    String rentedLabel;
    @SerializedName("purchasedLabel")
    @Expose
    String purchasedLabel;
    @SerializedName("freeLabel")
    @Expose
    String freeLabel;
    @SerializedName("noSeason")
    @Expose
    String noSeason;
    @SerializedName("noEpisodes")
    @Expose
    String noEpisodes;
    @SerializedName("noVideos")
    @Expose
    String noVideos;
    @SerializedName("itemUnavailableMsg")
    @Expose
    String itemUnavailableMsg;
    @SerializedName("downloadUnavilableMsg")
    @Expose
    String downloadUnavilableMsg;
    @SerializedName("videoUnavailableOnPlatformMsg")
    @Expose
    String videoUnavailableOnPlatformMsg;
    @SerializedName("castUnavilableMsg")
    @Expose
    String castUnavilableMsg;
    @SerializedName("hdStreamUnavailableMsg")
    @Expose
    String hdStreamUnavailableMsg;

    @SerializedName("seriesIncludeLabel")
    @Expose
    String seriesIncludeTitle;

    @SerializedName("cancelSubscriptionButtonText")
    @Expose
    String cancelSubscriptionButtonText;

    @SerializedName("alreadySubscribedUser")
    @Expose
    String alreadySubscribedUser;

    @SerializedName("failedPaymentErrorTitle")
    @Expose
    String failedPaymentErrorTitle;

    @SerializedName("strPaymentProcessCanceled")
    @Expose
    String strPaymentProcessCanceled;

    @SerializedName("jusPayInitErrorMessage")
    @Expose
    String jusPayInitErrorMessage;

    @SerializedName("sslCommerzInitErrorMessage")
    @Expose
    String sslCommerzInitErrorMessage;

    @SerializedName("getSocialReferredFriendsList")
    @Expose
    String getSocialReferredFriendsList;

    @SerializedName("getSocialSerialNumberText")
    @Expose
    String getSocialSerialNumberText;

    @SerializedName("getSocialReferredFriendsTitle")
    @Expose
    String getSocialReferredFriendsTitle;

    @SerializedName("getSocialInstallChannelTitle")
    @Expose
    String getSocialInstallChannelTitle;

    @SerializedName("getSocialSubscriptionStatusTitle")
    @Expose
    String getSocialSubscriptionStatusTitle;

    @SerializedName("getSocialSubscriptionDateTitle")
    @Expose
    String getSocialSubscriptionDateTitle;

    @SerializedName("getSocialSubscribedText")
    @Expose
    String getSocialSubscribedText;

    @SerializedName("getSocialNoRecordFoundText")
    @Expose
    String getSocialNoRecordFoundText;

    @SerializedName("transactionTitle")
    @Expose
    String transactionTitle;

    @SerializedName("transactionSuccessMsg")
    @Expose
    String transactionSuccessMsg;
    @SerializedName("SkipRecapButtonText")
    @Expose
    String skipRecapButtonText;

    @SerializedName("SkipIntroButtonText")
    @Expose
    String skipIntroButtonText;

    @SerializedName("startFromBeginningText")
    @Expose
    String startFromBeginningText;

    @SerializedName("nextLabel")
    @Expose
    String nextLabel;
    @SerializedName("upgradeMembership")
    @Expose
    String upgradeMembership;
    @SerializedName("iapReferralPurchaseWeb")
    @Expose
    String iapReferralPurchaseWeb;

    public String getIapReferralPurchaseWeb() {
        return iapReferralPurchaseWeb;
    }

    public String getUpgradeMembership() {
        return upgradeMembership;
    }
    public String getTransactionTitle() {
        return transactionTitle;
    }

    public String getTransactionSuccessMsg() {
        return transactionSuccessMsg;
    }

    public String getCancelSubscriptionButtonText() {
        return cancelSubscriptionButtonText;
    }

    public String getFailedPaymentErrorTitle() {
        return failedPaymentErrorTitle;
    }

    public String getStrPaymentProcessCanceled() {
        return strPaymentProcessCanceled;
    }

    public String getJusPayInitErrorMessage() {
        return jusPayInitErrorMessage;
    }

    public String getSslCommerzInitErrorMessage() {
        return sslCommerzInitErrorMessage;
    }

    public String getEnterEmailLabel() {
        return enterEmailLabel;
    }

    public String getEnterEmailAddressMessageForFreePlan() {
        return enterEmailAddressMessageForFreePlan;
    }

    public String getForgotPasswordCtaText() {
        return forgotPasswordCtaText;
    }

    public String getInternetErrorMessageHeader() {
        return internetErrorMessageHeader;
    }

    public String getErrorDialogCloseCta() {
        return errorDialogCloseCta;
    }

    public String getErrorDialogCancelCta() {
        return errorDialogCancelCta;
    }

    public String getErrorDialogNoCta() {
        return errorDialogNoCta;
    }

    public String getErrorDialogYesCta() {
        return errorDialogYesCta;
    }

    public String getErrorDialogSignInCta() {
        return errorDialogSignInCta;
    }

    public String getErrorDialogRetryCta() {
        return errorDialogRetryCta;
    }

    public String getErrorDialogOkCta() {
        return errorDialogOkCta;
    }

    public String getLogoutDialogMessage() {
        return logoutDialogMessage;
    }

    public String getLogoutVideoDownloadDialogTitle() {
        return logoutVideoDownloadDialogTitle;
    }

    public String getLogoutVideoDownloadDialogMessage() {
        return logoutVideoDownloadDialogMessage;
    }

    public String getTncDetails() {
        return tncDetails;
    }

    public String getPrivacyPolicyLabel() {
        return privacyPolicyLabel;
    }

    public String getTermsOfUseLabel() {
        return termsOfUseLabel;
    }

    public String getkInternetConnectionPostString() {
        return kInternetConnectionPostString;
    }

    public String getkError() {
        return kError;
    }

    public String getContentRatingTextWarningLabel() {
        return contentRatingTextWarningLabel;
    }

    public String getContentRatingDescriptionLabel() {
        return contentRatingDescriptionLabel;
    }

    public String getContentRatingViewerDiscretionLabel() {
        return contentRatingViewerDiscretionLabel;
    }

    public String getGuestUserSubscriptionMsg() {
        return guestUserSubscriptionMsg;
    }

    public String getTouchToCastMsg() {
        return touchToCastMsg;
    }

    public String getSeeAllTray() {
        return seeAllTray;
    }

    public String getDownloadUnavailableDialogTitle() {
        return downloadUnavailableDialogTitle;
    }

    public String getDownloadPLayLimitDialogMessage() {
        return downloadPLayLimitDialogMessage;
    }

    public String getDownloadSpaceDialogTitle() {
        return downloadSpaceDialogTitle;
    }

    public String getDownloadSpaceDialogMessage() {
        return downloadSpaceDialogMessage;
    }

    public String getVideoNotPlayableDialogTitle() {
        return videoNotPlayableDialogTitle;
    }

    public String getAlertDialogTitle() {
        return alertDialogTitle;
    }

    public String getSubscribeNowDialogButton() {
        return subscribeNowDialogButton;
    }

    public String getSubscriptionRequiredDialogTitle() {
        return subscriptionRequiredDialogTitle;
    }

    public String getSubscriptionRequiredDialogMessage() {
        return subscriptionRequiredDialogMessage;
    }

    public String getPremiumContentDialogTitle() {
        return premiumContentDialogTitle;
    }

    public String getPremiumContentLoggedInUserDialogMessage() {
        return premiumContentLoggedInUserDialogMessage;
    }

    public String getInvalidEmailDialogTitle() {
        return invalidEmailDialogTitle;
    }

    public String getLivePreviewMessageSportsFitness() {
        return livePreviewMessageSportsFitness;
    }

    public String getLivePreviewMessageOther() {
        return livePreviewMessageOther;
    }

    public String getPreviewEndDialogTitle() {
        return previewEndDialogTitle;
    }

    public String getAudioPreviewMessage() {
        return audioPreviewMessage;
    }

    public String getCastOverlayMessage() {
        return castOverlayMessage;
    }

    public String getStopCasting() {
        return stopCasting;
    }

    public String getCastMsgSuffix() {
        return castMsgSuffix;
    }

    public String getMoreLabel() {
        return moreLabel;
    }

    public String getkStrTryAgain() {
        return kStrTryAgain;
    }

    public String getAndLabel() {
        return andLabel;
    }

    public String getNoSubscriptionMsg() {
        return noSubscriptionMsg;
    }

    public String getServerErrorMsgText() {
        return serverErrorMsgText;
    }

    public String getSubscriptionFromAndroidMsg() {
        return subscriptionFromAndroidMsg;
    }


    public String getErrorDialogBackCta() {
        return errorDialogBackCta;
    }

    public String getRecommendationTitle() {
        return recommendationTitle;
    }

    public String getRecommendationSubTitle() {
        return recommendationSubTitle;
    }

    public String getWatchNowCtaNbc() {
        return watchNowCtaNbc;
    }


    public String getRecommendationSelectionEmptyMessage() {
        return recommendationSelectionEmptyMessage;
    }

    public String getRecommendSkipButtonText() {
        return recommendSkipButtonText;
    }


    public String getRecommendSaveButtonText() {
        return recommendSaveButtonText;
    }

    public String getRecommendStartBrowsingText() {
        return recommendStartBrowsingText;
    }

    public String getPersonalizeSettingsHeader() {
        return personalizeSettingsHeader;
    }

    public String getManagePersonalization() {
        return managePersonalization;
    }

    public String getDrmNotDownloaded() {
        return drmNotDownloaded;
    }

    public String getDrmNotCasted() {
        return drmNotCasted;
    }

    public String getServerErrorMessageText() {
        return serverErrorMessageText;
    }

    public String getViewPlansCta() {
        return viewPlansCta;
    }

    public String getSelectLanguage() {
        return selectLanguage;
    }

    public String getkNoResponseErrorTitle() {
        return kNoResponseErrorTitle;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public String getNoSubscriptionMessage() {
        return noSubscriptionMessage;
    }

    public String getSubscriptionFromAndroidMessage() {
        return subscriptionFromAndroidMessage;
    }

    public String getkStrUnableFetchData() {
        return kStrUnableFetchData;
    }

    public String getGuestUserSubscriptionMessage() {
        return guestUserSubscriptionMessage;
    }

    public String getkNoResponseErrorMessage() {
        return kNoResponseErrorMessage;
    }

    public String getContinueCta() {
        return continueCta;
    }

    public String getTitleDownloadSettingLabel() {
        return titleDownloadSettingLabel;
    }

    public String getSecondLabel() {
        return secondLabel;
    }

    public String getSecondsLabel() {
        return secondsLabel;
    }

    public String getSecondLabelFull() {
        return secondLabelFull;
    }

    public String getSecondsLabelFull() {
        return secondsLabelFull;
    }

    public String getNAME_NOT_VALID() {
        return NAME_NOT_VALID;
    }

    public String getFinishedTitleLabel() {
        return finishedTitleLabel;
    }

    public String getPlayCta() {
        return playCta;
    }

    public String getPlayInLabel() {
        return playInLabel;
    }

    public String getContentNotEnabledMessagePrefix() {
        return contentNotEnabledMessagePrefix;
    }

    public String getContentNotEnabledMessageSuffix() {
        return contentNotEnabledMessageSuffix;
    }

    public String getRentStartMessagePrefix() {
        return rentStartMessagePrefix;
    }

    public String getRentStartMessageSuffix() {
        return rentStartMessageSuffix;
    }

    public String getGeoRestrictErrorMessage() {
        return geoRestrictErrorMessage;
    }

    public String getCellularDisabledErrorMsg() {
        return cellularDisabledErrorMsg;
    }

    public String getShareVideo() {
        return shareVideo;
    }

    public String getCastingLoadingMessage() {
        return castingLoadingMessage;
    }

    public String getLoginSubscriptionDialogTitle() {
        return loginSubscriptionDialogTitle;
    }

    public String getLoginSubscriptionDialogMessage() {
        return loginSubscriptionDialogMessage;
    }

    public String getLoginRequiredDialogTitle() {
        return loginRequiredDialogTitle;
    }

    public String getLoginRequiredDialogMessage() {
        return loginRequiredDialogMessage;
    }

    public String getEntitlementLoginErrorMessage() {
        return entitlementLoginErrorMessage;
    }

    public String getStreamingInfoErrorTitle() {
        return streamingInfoErrorTitle;
    }

    public String getStreamingInfoErrorMessage() {
        return streamingInfoErrorMessage;
    }

    public String getAppUpdatePrefix() {
        return appUpdatePrefix;
    }

    public String getAppUpdateSuffix() {
        return appUpdateSuffix;
    }

    public String getUpdateAppCta() {
        return updateAppCta;
    }

    public String getUpdateAvailable() {
        return updateAvailable;
    }

    public String getCellularDisabledErrorTitle() {
        return cellularDisabledErrorTitle;
    }

    public String getDownloadExternalStorageTitle() {
        return downloadExternalStorageTitle;
    }

    public String getDownloadExternalStorageMessage() {
        return downloadExternalStorageMessage;
    }

    public String getMinutesLabel() {
        return minutesLabel;
    }

    public String getMinuteLabel() {
        return minuteLabel;
    }

    public String getErrorDialogSignUpCta() {
        return errorDialogSignUpCta;
    }

    public String getSearchHeaderLabel() {
        return searchHeaderLabel;
    }

    public String getPlayerSettingsUnavailable() {
        return playerSettingsUnavailable;
    }

    public String getAudioLanguage() {
        return audioLanguage;
    }

    public String getClosedCaptions() {
        return closedCaptions;
    }

    public String getPlaybackQuality() {
        return playbackQuality;
    }

    public String getLoadingVideo() {
        return loadingVideo;
    }

    public String getCastErrTitle() {
        return castErrTitle;
    }

    public String getArticleTrayHeader() {
        return articleTrayHeader;
    }

    public String getAudioTrayHeader() {
        return audioTrayHeader;
    }

    public String getBundleTrayHeader() {
        return bundleTrayHeader;
    }

    public String getGalleryTrayHeader() {
        return galleryTrayHeader;
    }

    public String getBundleSeriesTrayHeader() {
        return bundleSeriesTrayHeader;
    }

    public String getSeriesTrayHeader() {
        return seriesTrayHeader;
    }

    public String getProgramsTrayHeader() {
        return programsTrayHeader;
    }

    public String getEpisodesTrayHeader() {
        return episodesTrayHeader;
    }

    public String getVideosTrayHeader() {
        return videosTrayHeader;
    }

    public String getVideoplaylistTrayHeader() {
        return videoplaylistTrayHeader;
    }

    public String getGoCta() {
        return goCta;
    }

    public String getItemQueueUnavailable() {
        return itemQueueUnavailable;
    }

    public String getAlreadyDownloaded() {
        return alreadyDownloaded;
    }

    public String getDownloadStarted() {
        return downloadStarted;
    }

    public String getAddToDownloadQueue() {
        return addToDownloadQueue;
    }

    public String getEmptySearchField() {
        return emptySearchField;
    }

    public String getNoSearchresult() {
        return noSearchresult;
    }

    public String getSongsHeader() {
        return songsHeader;
    }

    public String getVideoNotLoaded() {
        return videoNotLoaded;
    }

    public String getPublishedOn() {
        return publishedOn;
    }

    public String getNotPurchased() {
        return notPurchased;
    }

    public String getAutoHlsResolution() {
        return autoHlsResolution;
    }

    public String getCastingTo() {
        return castingTo;
    }

    public String getEncourageUserToLoginLabel() {
        return encourageUserToLoginLabel;
    }

    public String getNoResult() {
        return noResult;
    }

    public String getNoPreviousAudio() {
        return noPreviousAudio;
    }

    public String getNoNextAudio() {
        return noNextAudio;
    }

    public String getLiveStreaming() {
        return liveStreaming;
    }

    public String getPremiumContentGuestUserDialogMessage() {
        return premiumContentGuestUserDialogMessage;
    }

    public String getAutoPlayoffMessage() {
        return autoPlayoffMessage;
    }

    public String getNoVideoInQueue() {
        return noVideoInQueue;
    }

    public String getCastMsgPrefix() {
        return castMsgPrefix;
    }

    public String getEntitlementErrorMessageForDownload() {
        return entitlementErrorMessageForDownload;
    }

    public String getAlreadyDownloadedOtherUser() {
        return alreadyDownloadedOtherUser;
    }

    public String getUserOnlineTimeAlert() {
        return userOnlineTimeAlert;
    }

    public String getEpisodeLabel() {
        return episodeLabel;
    }

    public String getSong() {
        return song;
    }

    public String getPhotos() {
        return photos;
    }

    public String getPhoto() {
        return photo;
    }

    public String getNoFight() {
        return noFight;
    }

    public String getFaceOff() {
        return faceOff;
    }

    public String getBy() {
        return by;
    }

    public String getTimerLabel() {
        return timerLabel;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public String getDownloadLowerCase() {
        return downloadLowerCase;
    }

    public String getAddToWatchlist() {
        return addToWatchlist;
    }

    public String getRemoveFromWatchlist() {
        return removeFromWatchlist;
    }

    public String getCheckExistingSubscription() {
        return checkExistingSubscription;
    }

    public String getExistingSubscriptionDoesNotExist() {
        return existingSubscriptionDoesNotExist;
    }

    public String getExistingUserLogin() {
        return existingUserLogin;
    }

    public String getBillingResponseError() {
        return billingResponseError;
    }

    public String getManageCta() {
        return manageCta;
    }

    public String getPlayersTrayHeader() {
        return playersTrayHeader;
    }

    public String getDownloadedLabel() {
        return downloadedLabel;
    }

    public String getDownloadingLabel() {
        return downloadingLabel;
    }

    public String getRemovedFromWatchlist() {
        return removedFromWatchlist;
    }

    public String getAddedToWatchlistLabel() {
        return addedToWatchlistLabel;
    }

    public String getFailedToAddToWatchlist() {
        return failedToAddToWatchlist;
    }

    public String getFailedToRemoveFromWatchlist() {
        return failedToRemoveFromWatchlist;
    }

    public String getOffLabel() {
        return offLabel;
    }

    public String getWebSubscriptionMessagePrefix() {
        return webSubscriptionMessagePrefix;
    }

    public String getWebSubscriptionMessageSuffix() {
        return webSubscriptionMessageSuffix;
    }

    public String getSeasonLabel() {
        return seasonLabel;
    }

    public String getSeasonsLabel() {
        return seasonsLabel;
    }

    public String getLoginCtaText() {
        return loginCtaText;
    }

    public String getEmptyEmailValidationMessage() {
        return emptyEmailValidationMessage;
    }

    public String getEmptyPasswordValidationMessage() {
        return emptyPasswordValidationMessage;
    }

    public String getPasswordFormatValidationMessage() {
        return passwordFormatValidationMessage;
    }

    public String getEmailFormatValidationMessage() {
        return emailFormatValidationMessage;
    }

    public String getInternetErrorMessageText() {
        return internetErrorMessageText;
    }

    public String getSubscriptionFromRokuMessage() {
        return subscriptionFromRokuMessage;
    }

    public String getSubscriptionFromAppleMessage() {
        return subscriptionFromAppleMessage;
    }

    public String getSubscriptionFromWebsiteMessage() {
        return subscriptionFromWebsiteMessage;
    }

    public String getSubscriptionMessageHeader() {
        return subscriptionMessageHeader;
    }

    public String getTvodContentERRORPrefix() {
        return tvodContentERRORPrefix;
    }

    public String getTvodContentERRORSuffix() {
        return tvodContentERRORSuffix;
    }

    public String getSubscriptionLeftMessage() {
        return subscriptionLeftMessage;
    }

    public String getSubscriptionExpiredMessage() {
        return subscriptionExpiredMessage;
    }

    public String getSubscriptionInitiatedMessage() {
        return subscriptionInitiatedMessage;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public String getToPayTitleText() {
        return toPayText;
    }

    public String getRetryButtonTitle() {
        return retryButton;
    }

    public String getBackToHomeButton() {
        return backToHomeButton;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getFailMessageTitle() {
        return failMessageTitle;
    }

    public String getWatchNowCta() {
        return watchNowCta;
    }

    public String getTveLogin() {
        return tveLogin;
    }

    public String getWaysToWatch() {
        return waysToWatch;
    }

    public String getWaysToWatchMessage() {
        return waysToWatchMessage;
    }

    public String getBecomeAmemberCta() {
        return becomeAmemberCta;
    }

    public String getChooseTvProviderCta() {
        return chooseTvProviderCta;
    }

    public String getOwnCta() {
        return ownCta;
    }

    public String getHaveAccount() {
        return haveAccount;
    }

    public String getLoginTextCta() {
        return loginTextCta;
    }

    public String getPlanUpgrade() {
        return planUpgrade;
    }

    public String getAgreeTOSPrivacyPolicy() {
        return agreeTOSPrivacyPolicy;
    }

    public String getViewingRestrictionsEnabled() {
        return viewingRestrictionsEnabled;
    }

    public String getEnterVideoPin() {
        return enterVideoPin;
    }

    public String getInCorrectPinMessage() {
        return inCorrectPinMessage;
    }

    public String getConfirmCTA() {
        return confirmCTA;
    }

    public String getFaceIdEnabledTitle() {
        return faceIdEnabledTitle;
    }

    public String getTouchIdEnabledTitle() {
        return touchIdEnabledTitle;
    }

    public String getFaceId() {
        return faceId;
    }

    public String getTouchId() {
        return touchId;
    }

    public String getUsePin() {
        return usePin;
    }

    public String getCongratulations() {
        return congratulations;
    }

    public String getInviteSuccessMessage() {
        return inviteSuccessMessage;
    }

    public String getHoverSeasonsLabel() {
        return hoverSeasonsLabel;
    }

    public String getMediaSessionError() {
        return mediaSessionError;
    }

    public String getMediaSessionErrorHeader() {
        return mediaSessionErrorHeader;
    }

    public String getDrmNotSupportHeader() {
        return drmNotSupportHeader;
    }

    public String getPendingMessage() {
        return pendingMessage;
    }

    public String getSuccessMessageTitle() {
        return successMessageTitle;
    }

    public String getPendingMessageTitle() {
        return pendingMessageTitle;
    }

    public String getRatingPromptConfirmationMessage() {
        return ratingPromptConfirmationMessage;
    }

    public String getRatingProceedAllow() {
        return ratingProceedAllow;
    }

    public String getRatingProceedDeny() {
        return ratingProceedDeny;
    }

    public String getRatingPromptMessage() {
        return ratingPromptMessage;
    }

    public String getMaxStreamError() {
        return maxStreamError;
    }

    public String getWaysToWatchMessageForTVEGuestUser() {
        return waysToWatchMessageForTVEGuestUser;
    }

    public String getMyNavItemPrefix() {
        return myNavItemPrefix;
    }

    public String getTitle() {
        return title;
    }

    public PrimaryCta getPrimaryCta() {
        return primaryCta;
    }

    public String getFetchVideoQualities() {
        return fetchVideoQualities;
    }

    public String getContinueButton() {
        return continueButton;
    }

    public String getDoneCta() {
        return doneCta;
    }

    public String getFindAdultForHelpMessage() {
        return findAdultForHelpMessage;
    }

    public String getGrabGrownUpMessage() {
        return grabGrownUpMessage;
    }

    public String getShowForBigKidsMessage() {
        return showForBigKidsMessage;
    }

    public String getAnswerMathProblemMessage() {
        return answerMathProblemMessage;
    }

    public String getPressHoldContinue() {
        return pressHoldContinue;
    }

    public String getCancelCta() {
        return cancelCta;
    }

    public String getSubscriptionFromAmazonMessage() {
        return subscriptionFromAmazonMessage;
    }

    public String getCancelCountdownCta() {
        return cancelCountdownCta;
    }

    public String getJustFinishedLabel() {
        return justFinishedLabel;
    }

    public String getUpNextLabel() {
        return upNextLabel;
    }

    public String getBackCta() {
        return backCta;
    }

    public String getCountdownCancelledLabel() {
        return countdownCancelledLabel;
    }

    public String getEmailUsAtLabel() {
        return emailUsAtLabel;
    }

    public String getCallUsAtLabel() {
        return callUsAtLabel;
    }

    public String getContactUsLabel() {
        return contactUsLabel;
    }

    public String getLanguageAlertMessage() {
        return languageAlertMessage;
    }

    public String getSelectCta() {
        return selectCta;
    }

    public String getSelectedCta() {
        return selectedCta;
    }

    public String getLanguageSelectionConfirmMessage() {
        return languageSelectionConfirmMessage;
    }

    public String getPressDownForMoreContentLabel() {
        return pressDownForMoreContentLabel;
    }

    public String getManageSubsubcription() {
        return manageSubsubcription;
    }

    public String getPreviousSearchlabel() {
        return previousSearchlabel;
    }

    public String getClearHistoryCta() {
        return clearHistoryCta;
    }

    public String getNoResultForLabel() {
        return noResultForLabel;
    }

    public String getResultTitleLabel() {
        return resultTitleLabel;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public String getAutoplayOnMenu() {
        return autoplayOnMenu;
    }

    public String getAutoplayOffMenu() {
        return autoplayOffMenu;
    }

    public String getClosedCaptionOnMenu() {
        return closedCaptionOnMenu;
    }

    public String getClosedCaptionOffMenu() {
        return closedCaptionOffMenu;
    }

    public String getHoverEpisodesLabel() {
        return hoverEpisodesLabel;
    }

    public String getHoverEpisodeLabel() {
        return hoverEpisodeLabel;
    }

    public String getLoginToSeeWatchlistLabel() {
        return loginToSeeWatchlistLabel;
    }

    public String getLoginToSeeHistoryLabel() {
        return loginToSeeHistoryLabel;
    }

    public String getAppExitAlertMessage() {
        return appExitAlertMessage;
    }

    public String getAddToSaved() {
        return addToSaved;
    }

    public String getRemoveFromSaved() {
        return removeFromSaved;
    }

    public String getPleaseLoginToViewMyAha() {
        return pleaseLoginToViewMyAha;
    }

    public String getAlreadyLoggedIn() {
        return alreadyLoggedIn;
    }

    public String getPageAvailabilityForLoggedInUser() {
        return pageAvailabilityForLoggedInUser;
    }

    public String getGetSocialFreeMessage() {
        return getSocialFreeMessage;
    }

    public String getGetSocialShareviaText() {
        return getSocialShareviaText;
    }

    public String getSocialSignInButtonText() {
        return getSocialSignInButtonText;
    }

    public String getSocialSubscribeButtonText() {
        return getSocialSubscribeButtonText;
    }

    public String getSmallFont() {
        return smallFont;
    }

    public String getRegularFont() {
        return regularFont;
    }

    public String getLargeFont() {
        return largeFont;
    }

    public String getNextDateLabel() {
        return nextDateLabel;
    }

    public String getUpdateValidNumber() {
        return updateValidNumber;
    }

    public String getSubscriptionScheduleToCancelOn() {
        return subscriptionScheduleToCancelOn;
    }

    public String getOpenBrowserText() {
        return openBrowserText;
    }

    public String getReSubscribe() {
        return reSubscribe;
    }

    public String getPreviousSubscriptionPlatform_iosMSG() {
        return previousSubscriptionPlatform_iosMSG;
    }

    public String getPreviousSubscriptionPlatform_androidMSG() {
        return previousSubscriptionPlatform_androidMSG;
    }

    public String getPreviousSubscriptionPlatform_webMSG() {
        return previousSubscriptionPlatform_webMSG;
    }

    public String getPreviousSubscriptionPlatform_rokuMSG() {
        return previousSubscriptionPlatform_rokuMSG;
    }

    public String getPreviousSubscriptionPlatform_amazonMSG() {
        return previousSubscriptionPlatform_amazonMSG;
    }

    public String getPreviousSubscriptionPlatform_windowsMSG() {
        return previousSubscriptionPlatform_windowsMSG;
    }

    public String getPreviousSubscriptionPlatform_smartTvMSG() {
        return previousSubscriptionPlatform_smartTvMSG;
    }

    public String getPreviousSubscriptionPlatform_otherMSG() {
        return previousSubscriptionPlatform_otherMSG;
    }

    public String getPreviousSubscriptionPlatform_ps4MSG() {
        return previousSubscriptionPlatform_ps4MSG;
    }

    public String getPreviousSubscriptionPlatform_androidWithOtherProcessorMSG() {
        return previousSubscriptionPlatform_androidWithOtherProcessorMSG;
    }

    public String getDayUpper() {
        return dayUpper;
    }

    public String getWeek() {
        return week;
    }

    public String getEditAccount() {
        return editAccount;
    }

    public String getInAppUpdateDownloadMessage() {
        return inAppUpdateDownloadMessage;
    }

    public String getInAppUpdateInstallLabel() {
        return inAppUpdateInstallLabel;
    }

    public String getAlreadySubscribedUser() {
        return alreadySubscribedUser;
    }

    public String getTimePeriodVerbiage() {
        return timePeriodVerbiage;
    }

    public String getGetSocialReferredFriendsList() {
        return getSocialReferredFriendsList;
    }

    public String getGetSocialSerialNumberText() {
        return getSocialSerialNumberText;
    }

    public String getGetSocialReferredFriendsTitle() {
        return getSocialReferredFriendsTitle;
    }

    public String getGetSocialInstallChannelTitle() {
        return getSocialInstallChannelTitle;
    }

    public String getGetSocialSubscriptionStatusTitle() {
        return getSocialSubscriptionStatusTitle;
    }

    public String getGetSocialSubscriptionDateTitle() {
        return getSocialSubscriptionDateTitle;
    }

    public String getGetSocialSubscribedText() {
        return getSocialSubscribedText;
    }

    public String getGetSocialNoRecordFoundText() {
        return getSocialNoRecordFoundText;
    }
    public String getHighQualityDownload() {
        return highQualityDownload;
    }

    public String getMediumDownloadQuality() {
        return mediumDownloadQuality;
    }

    public String getLowDownloadQuality() {
        return lowDownloadQuality;
    }

    public String getDownloadAlreadyErrorTitle() {
        return downloadAlreadyErrorTitle;
    }

    public String getAlreadyDownloadedSameQuality() {
        return alreadyDownloadedSameQuality;
    }

    public String getSeriesIncludeTitle() {
        return seriesIncludeTitle;
    }

    public String getHdStreamUnavailableMsg() {
        return hdStreamUnavailableMsg;
    }

    public String getCastUnavilableMsg() {
        return castUnavilableMsg;
    }

    public String getVideoUnavailableOnPlatformMsg() {
        return videoUnavailableOnPlatformMsg;
    }

    public String getNoSeason() {
        return noSeason;
    }

    public String getNoEpisodes() {
        return noEpisodes;
    }

    public String getNoVideos() {
        return noVideos;
    }

    public String getItemUnavailableMsg() {
        return itemUnavailableMsg;
    }

    public String getDownloadUnavilableMsg() {
        return downloadUnavilableMsg;
    }

    public String getFreeLabel() {
        return freeLabel;
    }

    public String getRentedLabel() {
        return rentedLabel;
    }

    public String getPurchasedLabel() {
        return purchasedLabel;
    }

    public String getRentOptionsLabel() {
        return rentOptionsLabel;
    }

    public String getRentOverlayLabel() {
        return rentOverlayLabel;
    }

    public String getPurchaseOverlayLabel() {
        return purchaseOverlayLabel;
    }

    public String getPurchaseOptionsLabel() {
        return purchaseOptionsLabel;
    }

    public String getGetSocialTermsAndConditions() {
        return getSocialTermsAndConditions;
    }

    public String getSkipRecapButtonText() {
        return skipRecapButtonText;
    }

    public String getSkipIntroButtonText() {
        return skipIntroButtonText;
    }

    public String getNextLabel() {
        return nextLabel;
    }

    public String getStartFromBeginningText(){
        return startFromBeginningText;
    }

}
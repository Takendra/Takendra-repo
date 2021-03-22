package com.viewlift.models.data.appcms.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class MetadataMap implements Serializable {
    @SerializedName("phoneInput")
    @Expose
    String phoneInput;
    @SerializedName("adTag")
    @Expose
    String adTag;
    @SerializedName("signUpTab")
    @Expose
    String signUpTab;
    @SerializedName("loginTab")
    @Expose
    String loginTab;
    @SerializedName("facebookLoginCta")
    @Expose
    String facebookLoginCta;
    @SerializedName("orSeparator")
    @Expose
    String orSeparator;
    @SerializedName("loginCta")
    @Expose
    String loginCta;
    @SerializedName("forgotPasswordCtaText")
    @Expose
    String forgotPasswordCtaText;

    @SerializedName("emailInput")
    @Expose
    String emailInput;

    @SerializedName("emailPasswordCta")
    @Expose
    String emailPasswordCta;

    @SerializedName("googleSignInCta")
    @Expose
    String googleSignInCta;
    @SerializedName("googleLoginCta")
    @Expose
    String googleLoginCta;


    @SerializedName("headerText")
    @Expose
    String headerText;

    @SerializedName("mobileHeaderText")
    @Expose
    String mobileHeaderText;

    @SerializedName("forgotPasswordError")
    @Expose
    String forgotPasswordError;

    @SerializedName("INCORRECT_CURRENT_PASSWORD")
    @Expose
    String INCORRECT_CURRENT_PASSWORD;
    @SerializedName("MISMATCH_PASSWORD")
    @Expose
    String MISMATCH_PASSWORD;
    @SerializedName("passwordMismatchError")
    @Expose
    String passwordMismatchError;
    @SerializedName("changePassword")
    @Expose
    String changePassword;
    @SerializedName("profileText")
    @Expose
    String profileText;
    @SerializedName("email")
    @Expose
    String email;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("facebookSignUpCta")
    @Expose
    String facebookSignUpCta;
    @SerializedName("EMAIL_NOT_REGISTERED")
    @Expose
    String EMAIL_NOT_REGISTERED;
    @SerializedName("accountHeader")
    @Expose
    String accountHeader;
    @SerializedName("EMAIL_OR_PASSWORD_INCORRECT")
    @Expose
    String EMAIL_OR_PASSWORD_INCORRECT;
    @SerializedName("NAME_NOT_VALID")
    @Expose
    String NAME_NOT_VALID;
    @SerializedName("signUpCtaText")
    @Expose
    String signUpCtaText;
    @SerializedName("forgotPasswordSuccess")
    @Expose
    String forgotPasswordSuccess;
    @SerializedName("EMAIL_NOT_EXIST")
    @Expose
    String EMAIL_NOT_EXIST;
    @SerializedName("passwordLengthError")
    @Expose
    String passwordLengthError;
    @SerializedName("forgotPasswordProcessError")
    @Expose
    String forgotPasswordProcessError;
    @SerializedName("EMAIL_ALREADY_LINKED")
    @Expose
    String EMAIL_ALREADY_LINKED;
    @SerializedName("PASSWORD_NOT_VALID")
    @Expose
    String PASSWORD_NOT_VALID;
    @SerializedName("defaultError")
    @Expose
    String defaultError;
    @SerializedName("passwordInput")
    @Expose
    String passwordInput;
    @SerializedName("autoplayLabel")
    @Expose
    String autoplayLabel;
    @SerializedName("kOldPassword")
    @Expose
    String kOldPassword;
    @SerializedName("appSettingsLabel")
    @Expose
    String appSettingsLabel;
    @SerializedName("passwordPopupNewPassword")
    @Expose
    String passwordPopupNewPassword;
    @SerializedName("accountLabel")
    @Expose
    String accountLabel;
    @SerializedName("downloadQualityLabel")
    @Expose
    String downloadQualityLabel;
    @SerializedName("cellularDataLabel")
    @Expose
    String cellularDataLabel;
    @SerializedName("changeLabel")
    @Expose
    String changeLabel;
    @SerializedName("paymentProcessorHeader")
    @Expose
    String paymentProcessorHeader;
    @SerializedName("subscriptionAndBillingLabel")
    @Expose
    String subscriptionAndBillingLabel;
    @SerializedName("profileUpdateSuccessMessage")
    @Expose
    String profileUpdateSuccessMessage;
    @SerializedName("passwordUpdateSuccessMessage")
    @Expose
    String passwordUpdateSuccessMessage;
    @SerializedName("downloadSettingsLabel")
    @Expose
    String downloadSettingsLabel;
    @SerializedName("appVersionLabel")
    @Expose
    String appVersionLabel;
    @SerializedName("profilePopupUpdate")
    @Expose
    String profilePopupUpdate;
    @SerializedName("cancelYesLabel")
    @Expose
    String cancelYesLabel;
    @SerializedName("cancelNoLabel")
    @Expose
    String cancelNoLabel;
    @SerializedName("subscribeNowButtonText")
    @Expose
    String subscribeNowButtonText;
    @SerializedName("subscriptionPlanHeader")
    @Expose
    String subscriptionPlanHeader;
    @SerializedName("EMAIL_NOT_VALID")
    @Expose
    String EMAIL_NOT_VALID;
    @SerializedName("upgradePlanButtonText")
    @Expose
    String upgradePlanButtonText;
    @SerializedName("useSdCardForDownloadsLabel")
    @Expose
    String useSdCardForDownloadsLabel;
    @SerializedName("kNotSubscribed")
    @Expose
    String kNotSubscribed;
    @SerializedName("invalidNameMsg")
    @Expose
    String invalidNameMsg;
    @SerializedName("subscribedLabel")
    @Expose
    String subscribedLabel;
    @SerializedName("verifyPasswordPopUpTitle")
    @Expose
    String verifyPasswordPopUpTitle;
    @SerializedName("INCORRECT_PASSWORD")
    @Expose
    String INCORRECT_PASSWORD;
    @SerializedName("languageSettingsLabel")
    @Expose
    String languageSettingsLabel;
    @SerializedName("DownloadQualityTitle")
    @Expose
    String DownloadQualityTitle;
    @SerializedName("continueButtonLabel")
    @Expose
    String continueButtonLabel;
    @SerializedName("cancelButton")
    @Expose
    String cancelButton;
    @SerializedName("kStrDeleteHistoryAlertTitle")
    @Expose
    String kStrDeleteHistoryAlertTitle;
    @SerializedName("kStrDeleteAllVideosFromHistoryAlertMessage")
    @Expose
    String kStrDeleteAllVideosFromHistoryAlertMessage;
    @SerializedName("kStrDeleteSingleVideoFromHistoryAlertMessage")
    @Expose
    String kStrDeleteSingleVideoFromHistoryAlertMessage;
    @SerializedName("kStrDeleteWatchlistAlertTitle")
    @Expose
    String kStrDeleteWatchlistAlertTitle;
    @SerializedName("kStrDeleteAllVideosFromWatchlistAlertMessage")
    @Expose
    String kStrDeleteAllVideosFromWatchlistAlertMessage;
    @SerializedName("kStrDeleteSingleVideoFromWatchlistAlertMessage")
    @Expose
    String kStrDeleteSingleVideoFromWatchlistAlertMessage;
    @SerializedName("removeAllHistoryLabel")
    @Expose
    String removeAllHistoryLabel;
    @SerializedName("removeAllWatchlistLabel")
    @Expose
    String removeAllWatchlistLabel;
    @SerializedName("kStrWatchlistEmpty")
    @Expose
    String kStrWatchlistEmpty;
    @SerializedName("kStrHistoryEmpty")
    @Expose
    String kStrHistoryEmpty;
    @SerializedName("kStrYearsAgo")
    @Expose
    String kStrYearsAgo;
    @SerializedName("kStrYearAgo")
    @Expose
    String kStrYearAgo;
    @SerializedName("kStrMonthsAgo")
    @Expose
    String kStrMonthsAgo;
    @SerializedName("kStrMonthAgo")
    @Expose
    String kStrMonthAgo;
    @SerializedName("kStrWeeksAgo")
    @Expose
    String kStrWeeksAgo;
    @SerializedName("kStrWeekAgo")
    @Expose
    String kStrWeekAgo;
    @SerializedName("kStrDaysAgo")
    @Expose
    String kStrDaysAgo;
    @SerializedName("kStrDayAgo")
    @Expose
    String kStrDayAgo;
    @SerializedName("kStrHoursAgo")
    @Expose
    String kStrHoursAgo;
    @SerializedName("kStrHourAgo")
    @Expose
    String kStrHourAgo;
    @SerializedName("kStrMinutesAgo")
    @Expose
    String kStrMinutesAgo;
    @SerializedName("kStrMinuteAgo")
    @Expose
    String kStrMinuteAgo;
    @SerializedName("kStrSecondsAgo")
    @Expose
    String kStrSecondsAgo;
    @SerializedName("kStrJustNow")
    @Expose
    String kStrJustNow;
    @SerializedName("settingsLabel")
    @Expose
    String settingsLabel;
    @SerializedName("failedToRemoveFromWatchlist")
    @Expose
    String failedToRemoveFromWatchlist;
    @SerializedName("failedToAddToWatchlist")
    @Expose
    String failedToAddToWatchlist;
    @SerializedName("removedFromWatchlist")
    @Expose
    String removedFromWatchlist;
    @SerializedName("addedToWatchlistLabel")
    @Expose
    String addedToWatchlistLabel;
    @SerializedName("addToWatchlistDialogHeader")
    @Expose
    String addToWatchlistDialogHeader;
    @SerializedName("aboutThisConceptLabel")
    @Expose
    String aboutThisConceptLabel;
    @SerializedName("completeDetailsCTA")
    @Expose
    String completeDetailsCTA;
    @SerializedName("meetYourInstructorCTA")
    @Expose
    String meetYourInstructorCTA;
    @SerializedName("allClassesLabel")
    @Expose
    String allClassesLabel;
    @SerializedName("addedToBookmarksLabel")
    @Expose
    String addedToBookmarksLabel;
    @SerializedName("failedToAddToBookmarks")
    @Expose
    String failedToAddToBookmarks;
    @SerializedName("removedFromBookmarks")
    @Expose
    String removedFromBookmarks;
    @SerializedName("failedToRemoveFromBookmarks")
    @Expose
    String failedToRemoveFromBookmarks;
    @SerializedName("addToWatchlistDialogMessage")
    @Expose
    String addToWatchlistDialogMessage;
    @SerializedName("watchlistPageTitle")
    @Expose
    String watchlistPageTitle;
    @SerializedName("historyPageTitle")
    @Expose
    String historyPageTitle;
    @SerializedName("watchTrailerCTA")
    @Expose
    String watchTrailerCTA;
    @SerializedName("timerLabel")
    @Expose
    String timerLabel;
    @SerializedName("seasonLabel")
    @Expose
    String seasonLabel;
    @SerializedName("seasonsLabel")
    @Expose
    String seasonsLabel;
    @SerializedName("episodeLabel")
    @Expose
    String episodeLabel;
    @SerializedName("programsLabel")
    @Expose
    String programsLabel;
    @SerializedName("episodesLabel")
    @Expose
    String episodesLabel;
    @SerializedName("programLabel")
    @Expose
    String programLabel;
    @SerializedName("profilePopupText")
    @Expose
    String profilePopupText;
    @SerializedName("profilePopupEmail")
    @Expose
    String profilePopupEmail;
    @SerializedName("passwordPopupHeader")
    @Expose
    String passwordPopupHeader;
    @SerializedName("passwordPopupCurrentPassword")
    @Expose
    String passwordPopupCurrentPassword;
    @SerializedName("passwordPopupConfirmPassword")
    @Expose
    String passwordPopupConfirmPassword;
    @SerializedName("passwordPopupUpdate")
    @Expose
    String passwordPopupUpdate;
    @SerializedName("subscriptionHeader")
    @Expose
    String subscriptionHeader;
    @SerializedName("kFillDetails")
    @Expose
    String kFillDetails;
    @SerializedName("profilePopupName")
    @Expose
    String profilePopupName;
    @SerializedName("noDownload")
    @Expose
    String noDownload;
    @SerializedName("strDeleteSingleContentFromDownloadAlertMessage")
    @Expose
    String strDeleteSingleContentFromDownloadAlertMessage;
    @SerializedName("strDeleteAllContentFromDownloadAlertMessage")
    @Expose
    String strDeleteAllContentFromDownloadAlertMessage;
    @SerializedName("strDeleteAudiosFromDownloadAlertMessage")
    @Expose
    String strDeleteAudiosFromDownloadAlertMessage;
    @SerializedName("myDownloadLowerCase")
    @Expose
    String myDownloadLowerCase;
    @SerializedName("strDeleteDownloadAlertTitle")
    @Expose
    String strDeleteDownloadAlertTitle;
    @SerializedName("retryDownloadMessage")
    @Expose
    String retryDownloadMessage;
    @SerializedName("deleteAudioMessage")
    @Expose
    String deleteAudioMessage;
    @SerializedName("removeAllDownloadCta")
    @Expose
    String removeAllDownloadCta;
    @SerializedName("directorLabel")
    @Expose
    String directorLabel;
    @SerializedName("starringLabel")
    @Expose
    String starringLabel;
    @SerializedName("incompleteDownload")
    @Expose
    String incompleteDownload;
    @SerializedName("incompleteDownloadMessage")
    @Expose
    String incompleteDownloadMessage;
    @SerializedName("downloadNotFinish")
    @Expose
    String downloadNotFinish;
    @SerializedName("DEVICE_LIMIT_EXCEEDED")
    @Expose
    String DEVICE_LIMIT_EXCEEDED;
    @SerializedName("month")
    @Expose
    String month;
    @SerializedName("months")
    @Expose
    String months;
    @SerializedName("years")
    @Expose
    String years;
    @SerializedName("year")
    @Expose
    String year;
    @SerializedName("day")
    @Expose
    String day;
    @SerializedName("days")
    @Expose
    String days;
    @SerializedName("week")
    @Expose
    String week;
    @SerializedName("weeks")
    @Expose
    String weeks;

    @SerializedName("addToWatchlistCTA")
    @Expose
    String addToWatchlistCTA;
    @SerializedName("removeFromWatchlistCTA")
    @Expose
    String removeFromWatchlistCTA;

    @SerializedName("googleSignUpCta")
    @Expose
    String googleSignUpCta;

    @SerializedName("bank")
    @Expose
    String banks;

    @SerializedName("wallets")
    @Expose
    String wallets;

    @SerializedName("cards")
    @Expose
    String cards;

    @SerializedName("upi")
    @Expose
    String upi;

    @SerializedName("header")
    @Expose
    String header;


    @SerializedName("activateDeviceCta")
    @Expose
    String activateDeviceCta;
    @SerializedName("linkDeviceToAccountLabel")
    @Expose
    String linkDeviceToAccountLabel;
    @SerializedName("noAccountLabel")
    @Expose
    String noAccountLabel;
    @SerializedName("continueCta")
    @Expose
    String continueCta;
    @SerializedName("cancelCta")
    @Expose
    String cancelCta;
    @SerializedName("enterEmailToResetPasswordLabel")
    @Expose
    String enterEmailToResetPasswordLabel;
    @SerializedName("logoutCta")
    @Expose
    String logoutCta;
    @SerializedName("manageSubscriptionCta")
    @Expose
    String manageSubscriptionCta;
    @SerializedName("closeCaptioningLabel")
    @Expose
    String closeCaptioningLabel;
    @SerializedName("loggedInAsLabel")
    @Expose
    String loggedInAsLabel;
    @SerializedName("signUpTermsAgreementLabel")
    @Expose
    String signUpTermsAgreementLabel;
    @SerializedName("termsOfUseLabel")
    @Expose
    String termsOfUseLabel;
    @SerializedName("andLabel")
    @Expose
    String andLabel;
    @SerializedName("privacyPolicyLabel")
    @Expose
    String privacyPolicyLabel;
    @SerializedName("deferredSubscriptionCancelDateLabel")
    @Expose
    String deferredSubscriptionCancelDateLabel;
    @SerializedName("activateDevice")
    @Expose
    String activateDevice;
    @SerializedName("activateDeviceLabel")
    @Expose
    String activateDeviceLabel;
    @SerializedName("enterCodeToLinkDevice")
    @Expose
    String enterCodeToLinkDevice;
    @SerializedName("enterCodeToLinkDeviceLabel_1")
    @Expose
    String enterCodeToLinkDeviceLabel_1;
    @SerializedName("enterCodeToLinkDeviceLabel_2")
    @Expose
    String enterCodeToLinkDeviceLabel_2;
    @SerializedName("enterCodeToLinkDeviceLabel_3")
    @Expose
    String enterCodeToLinkDeviceLabel_3;
    @SerializedName("startWatchingCTA")
    @Expose
    String startWatchingCTA;
    @SerializedName("resumeWatchingCTA")
    @Expose
    String resumeWatchingCTA;
    @SerializedName("watchNowCTA")
    @Expose
    String watchNowCTA;
    @SerializedName("historyTitleLabel")
    @Expose
    String historyTitleLabel;
    @SerializedName("clearWatchlistAlertTitle")
    @Expose
    String clearWatchlistAlertTitle;
    @SerializedName("deleteItemButton")
    @Expose
    String deleteItemButton;
    @SerializedName("clearWatchlist")
    @Expose
    String clearWatchlist;
    @SerializedName("clearAllHistoryButton")
    @Expose
    String clearAllHistoryButton;
    @SerializedName("kAddedText")
    @Expose
    String kAddedText;
    @SerializedName("kStrMinuteAgoiOS")
    @Expose
    String kStrMinuteAgoiOS;
    @SerializedName("kStrMinutesAgoiOS")
    @Expose
    String kStrMinutesAgoiOS;
    @SerializedName("clearHistoryAlertTitle")
    @Expose
    String clearHistoryAlertTitle;
    @SerializedName("youAreSignInAsLabel")
    @Expose
    String youAreSignInAsLabel;
    @SerializedName("selectPlanLabel")
    @Expose
    String selectPlanLabel;
    @SerializedName("kAmazonSubscriptionMetadata1")
    @Expose
    String kAmazonSubscriptionMetadata1;
    @SerializedName("kAmazonSubscriptionMetadata2")
    @Expose
    String kAmazonSubscriptionMetadata2;
    @SerializedName("kAmazonSubscriptionMetadata3")
    @Expose
    String kAmazonSubscriptionMetadata3;
    @SerializedName("kAmazonSubscriptionMetadata4")
    @Expose
    String kAmazonSubscriptionMetadata4;
    @SerializedName("kAmazonSubscriptionMetadata5")
    @Expose
    String kAmazonSubscriptionMetadata5;
    @SerializedName("kAmazonSubscriptionMetadata6")
    @Expose
    String kAmazonSubscriptionMetadata6;
    @SerializedName("image")
    private String image;
    @SerializedName("shortParagraph")
    private String shortParagraph;
    @SerializedName("buttonText")
    private String buttonText;
    @SerializedName("backgroundColor")
    private String backgroundColor;
    @SerializedName("buttonLink")
    private String buttonLink;
    @SerializedName("dataSaverLabel")
    @Expose
    String dataSaverLabel;
    @SerializedName("goodQualityLabel")
    @Expose
    String goodQualityLabel;
    @SerializedName("bestQualityLabel")
    @Expose
    String bestQualityLabel;
    @SerializedName("parentalControlHeader")
    @Expose
    String parentalControlHeader;
    @SerializedName("manageParentalFromMobileOrWeb")
    @Expose
    String manageParentalFromMobileOrWeb;
    @SerializedName("viewingRestrictionsCTA")
    @Expose
    String viewingRestrictionsCTA;
    @SerializedName("saveRatingCTA")
    @Expose
    String saveRatingCTA;
    @SerializedName("setupPinCTA")
    @Expose
    String setupPinCTA;
    @SerializedName("resetPinCTA")
    @Expose
    String resetPinCTA;
    @SerializedName("enterPinLabel")
    @Expose
    String enterPinLabel;
    @SerializedName("allRatingCategory")
    @Expose
    String allRatingCategory;
    @SerializedName("allRatingMessage")
    @Expose
    String allRatingMessage;
    @SerializedName("sevenRatingCategory")
    @Expose
    String sevenRatingCategory;
    @SerializedName("sevenRatingMessage")
    @Expose
    String sevenRatingMessage;
    @SerializedName("thirteenRatingCategory")
    @Expose
    String thirteenRatingCategory;
    @SerializedName("thirteenRatingMessage")
    @Expose
    String thirteenRatingMessage;
    @SerializedName("sixteenRatingCategory")
    @Expose
    String sixteenRatingCategory;

    @SerializedName("sixteenRatingMessage")
    @Expose
    String sixteenRatingMessage;
    @SerializedName("eighteenRatingCategory")
    @Expose
    String eighteenRatingCategory;
    @SerializedName("eighteenRatingMessage")
    @Expose
    String eighteenRatingMessage;
    @SerializedName("tapAgeMessage")
    @Expose
    String tapAgeMessage;
    @SerializedName("authWithFacebook")
    @Expose
    String authWithFacebook;
    @SerializedName("authWithGoogle")
    @Expose
    String authWithGoogle;
    @SerializedName("enableFaceId")
    @Expose
    String enableFaceId;
    @SerializedName("enableTouchId")
    @Expose
    String enableTouchId;
    @SerializedName("enableTouchIdMessage")
    @Expose
    String enableTouchIdMessage;
    @SerializedName("enableFaceIdMessage")
    @Expose
    String enableFaceIdMessage;
    @SerializedName("touchIdNotEnrolled")
    @Expose
    String touchIdNotEnrolled;
    @SerializedName("mobileLabel")
    @Expose
    String mobileLabel;

    @SerializedName("verifyOTPText")
    @Expose
    String verifyOTPText;

    @SerializedName("verifyOTPSubmit")
    @Expose
    String verifyOTPSubmit;

    @SerializedName("resentOTPText")
    @Expose
    String resentOTPText;

    @SerializedName("VERIFY_OTP_FAILED")
    @Expose
    String VERIFY_OTP_FAILED;

    @SerializedName("OTP_SENT_FAILED")
    @Expose
    String OTP_SENT_FAILED;

    @SerializedName("PHONE_NOT_LINKED")
    @Expose
    String PHONE_NOT_LINKED;

    @SerializedName("INVALID_REQUEST_PARAMS")
    @Expose
    String INVALID_REQUEST_PARAMS;

    @SerializedName("PHONE_NOT_VALID")
    @Expose
    String PHONE_NOT_VALID;

    @SerializedName("PHONE_ALREADY_LINKED")
    @Expose
    String PHONE_ALREADY_LINKED;

    @SerializedName("UPDATE_PHONE_NUMBER")
    @Expose
    String UPDATE_PHONE_NUMBER;

    @SerializedName("NOT_RECEIVED_OTP")
    @Expose
    String NOT_RECEIVED_OTP;

    @SerializedName("SEND_OTP_CTA_TEXT")
    @Expose
    String SEND_OTP_CTA_TEXT;

    @SerializedName("loginEmailPassword")
    @Expose
    String loginEmailPassword;
    @SerializedName("nextDateLabel")
    @Expose
    String nextDateLabel;
    @SerializedName("subscribeNowCta")
    @Expose
    String subscribeNowCta;
    @SerializedName("kStrPassword")
    @Expose
    String kStrPassword;
    @SerializedName("loginTveCta")
    @Expose
    String loginTveCta;
    @SerializedName("providersignUpTveCta")
    @Expose
    String providersignUpTveCta;
    @SerializedName("mobileSignupCta")
    @Expose
    String mobileSignupCta;
    @SerializedName("mobileLoginCta")
    @Expose
    String mobileLoginCta;
    @SerializedName("priceTrialEnd")
    @Expose
    String priceTrialEnd;
    @SerializedName("purchaseTitle")
    @Expose
    String purchaseTitle;
    @SerializedName("recurrinngPurchaseLabel")
    @Expose
    String recurrinngPurchaseLabel;
    @SerializedName("recurringBillingLabel")
    @Expose
    String recurringBillingLabel;
    @SerializedName("personalizationtTitle")
    @Expose
    String personalizationtTitle;
    @SerializedName("addTopicCta")
    @Expose
    String addTopicCta;
    @SerializedName("connectedAccountPlaceHolder")
    @Expose
    String connectedAccountPlaceHolder;
    @SerializedName("needHelpTitleLabel")
    @Expose
    String needHelpTitleLabel;
    @SerializedName("needHelpDescriptionLabel")
    @Expose
    String needHelpDescriptionLabel;
    @SerializedName("facebookError")
    @Expose
    String facebookError;
    @SerializedName("googleError")
    @Expose
    String googleError;
    @SerializedName("accountDetailsLabel")
    @Expose
    String accountDetailsLabel;
    @SerializedName("nameLabel")
    @Expose
    String nameLabel;
    @SerializedName("phoneLabel")
    @Expose
    String phoneLabel;
    @SerializedName("emailLabel")
    @Expose
    String emailLabel;
    @SerializedName("passwordLabel")
    @Expose
    String passwordLabel;
    @SerializedName("editPhoneNumberLabel")
    @Expose
    String editPhoneNumberLabel;
    @SerializedName("editLabel")
    @Expose
    String editLabel;
    @SerializedName("editEmailLabel")
    @Expose
    String editEmailLabel;
    @SerializedName("editNameLabel")
    @Expose
    String editNameLabel;
    @SerializedName("editPasswordLabel")
    @Expose
    String editPasswordLabel;
    @SerializedName("yourInterests")
    @Expose
    String yourInterests;
    @SerializedName("subscriptionLabel")
    @Expose
    String subscriptionLabel;

    @SerializedName("backToVideoCTA")
    @Expose
    String backToVideoCTA;

    @SerializedName("restHeader")
    @Expose
    String restHeader;

    @SerializedName("restMessage")
    @Expose
    String restMessage;

    @SerializedName("replayVideoCTA")
    @Expose
    String replayVideoCTA;

    @SerializedName("repeatCircuitCTA")
    @Expose
    String repeatCircuitCTA;

    @SerializedName("startWorkoutCTA")
    @Expose
    String startWorkoutCTA;

    @SerializedName("restCTA")
    @Expose
    String restCTA;

    @SerializedName("nextCircuitCTA")
    @Expose
    String nextCircuitCTA;

    @SerializedName("resumeWorkoutCTA")
    @Expose
    String resumeWorkoutCTA;

    @SerializedName("timesLabel")
    @Expose
    String timesLabel;

    @SerializedName("repeatLabel")
    @Expose
    String repeatLabel;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("billingHistoryYearLocalization")
    @Expose
    String billingHistoryYearLocalization;

    @SerializedName("billingHistoryMonthLocalization")
    @Expose
    String billingHistoryMonthLocalization;

    @SerializedName("addPhoneNo")
    @Expose
    String addPhoneNo;
    @SerializedName("OnDemandPurchaseLabel")
    @Expose
    String onDemandPurchaseLabel;
    @SerializedName("seeAllPuchaseCTA")
    @Expose
    String seeAllPuchaseCTA;
    @SerializedName("billingLabel")
    @Expose
    String billingLabel;
    @SerializedName("confirmationLabel")
    @Expose
    String confirmationLabel;
    @SerializedName("subscribeLabel")
    @Expose
    String subscribeLabel;
    @SerializedName("selectPaymentMethod")
    @Expose
    String selectPaymentMethod;
    @SerializedName("createPassword")
    @Expose
    String createPassword;

    public String getCreatePassword() {
        return createPassword;
    }

    public String getSelectPaymentMethod() {
        return selectPaymentMethod;
    }

    public String getBillingLabel() {
        return billingLabel;
    }

    public String getConfirmationLabel() {
        return confirmationLabel;
    }

    public String getSubscribeLabel() {
        return subscribeLabel;
    }

    public String getOnDemandPurchaseLabel() {
        return onDemandPurchaseLabel;
    }

    public String getSeeAllPuchaseCTA() {
        return seeAllPuchaseCTA;
    }

    @SerializedName("billingHistoryButtonText")
    @Expose
    String billingHistoryButtonText;

    @SerializedName("billingHistoryJoinDatelabel")
    @Expose
    String billingHistoryJoinDatelabel;

    @SerializedName("billingHistoryLastChargeLabel")
    @Expose
    String billingHistoryLastChargeLabel;

    @SerializedName("billingHistoryNextChargeLabel")
    @Expose
    String billingHistoryNextChargeLabel;

    @SerializedName("billingHistoryTableDate")
    @Expose
    String billingHistoryTableDate;

    @SerializedName("billingHistoryTableOffers")
    @Expose
    String billingHistoryTableOffers;

    @SerializedName("billingHistoryTableServicePeriod")
    @Expose
    String billingHistoryTableServicePeriod;

    @SerializedName("billingHistoryTableTitle")
    @Expose
    String billingHistoryTableTitle;

    @SerializedName("billingHistoryTableTotal")
    @Expose
    String billingHistoryTableTotal;

    @SerializedName("billingHistoryTableType")
    @Expose
    String billingHistoryTableType;

    @SerializedName("billingHistoryTableDescription")
    @Expose
    String billingHistoryTableDescription;


    @SerializedName("cancelSubscriptionButtonText")
    @Expose
    String cancelSubscriptionButtonText;

    @SerializedName("proceedButton")
    @Expose
    String proceedButton;

    @SerializedName("plans")
    @Expose
    String plans;
    @SerializedName("createPasswordToAddEmail")
    @Expose
    String createPasswordToAddEmail;
    @SerializedName("createPasswordToAddName")
    @Expose
    String createPasswordToAddName;

    public String getCreatePasswordToAddEmail() {
        return createPasswordToAddEmail;
    }

    public String getCreatePasswordToAddName() {
        return createPasswordToAddName;
    }

    @SerializedName("description")
    @Expose
    String description;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getCardGenericMessage() {
        return cardGenericMessage;
    }

    public void setCardGenericMessage(String cardGenericMessage) {
        this.cardGenericMessage = cardGenericMessage;
    }

    public String getNetbankingGenericMessage() {
        return netbankingGenericMessage;
    }

    public void setNetbankingGenericMessage(String netbankingGenericMessage) {
        this.netbankingGenericMessage = netbankingGenericMessage;
    }

    @SerializedName("cardGenericMessage")
    @Expose
    String cardGenericMessage;

    @SerializedName("netbankingGenericMessage")
    @Expose
    String netbankingGenericMessage;

    public String getUpiGenericMessage() {
        return upiGenericMessage;
    }

    public void setUpiGenericMessage(String upiGenericMessage) {
        this.upiGenericMessage = upiGenericMessage;
    }

    @SerializedName("upiGenericMessage")
    @Expose
    String upiGenericMessage;


    @SerializedName("addBilling")
    @Expose
    String addBilling;

    @SerializedName("verifyFirstPay")
    @Expose
    String verifyFirstPay;

    @SerializedName("ONSeparator")
    @Expose
    String onSeparator;

    @SerializedName("promoHeading")
    @Expose
    String promoHeading;

    @SerializedName("addPromo")
    @Expose
    String addPromo;

    @SerializedName("applyPromo")
    @Expose
    String applyPromo;

    @SerializedName("removePromo")
    @Expose
    String removePromo;

    @SerializedName("discountOfApplied")
    @Expose
    String discountOfApplied;

    @SerializedName("enterPromoCode")
    @Expose
    String enterPromoCode;

    @SerializedName("planSubscription")
    @Expose
    String planSubscription;

    @SerializedName("total")
    @Expose
    String total;

    @SerializedName("discountApplied")
    @Expose
    String discountApplied;

    @SerializedName("totalBilling")
    @Expose
    String totalBilling;

    @SerializedName("CODE_NOT_VALID_PLAN")
    @Expose
    String CODE_NOT_VALID_PLAN;

    @SerializedName("NOT_VALID_CODE")
    @Expose
    String NOT_VALID_CODE;

    @SerializedName("NOT_VALID_CODE_SUBMITTED")
    @Expose
    String NOT_VALID_CODE_SUBMITTED;

    @SerializedName("NO_PREPAID_CODE")
    @Expose
    String NO_PREPAID_CODE;

    @SerializedName("OFFER_ALREADY_USED")
    @Expose
    String OFFER_ALREADY_USED;

    @SerializedName("OFFER_CODE_EXPIRED")
    @Expose
    String OFFER_CODE_EXPIRED;

    @SerializedName("popupHeadingText")
    @Expose
    String popupHeadingText;

    @SerializedName("popupDescription")
    @Expose
    String popupDescription;

    @SerializedName("segmentsLabel")
    @Expose
    String segmentsLabel;

    @SerializedName("shareLabel")
    @Expose
    String shareLabel;

    public String getShareLabel() {
        return shareLabel;
    }

    public String getSegmentsLabel() {
        return segmentsLabel;
    }

    public String getPopupHeadingText() {
        return popupHeadingText;
    }

    public void setPopupHeadingText(String popupHeadingText) {
        this.popupHeadingText = popupHeadingText;
    }

    public String getPopupDescription() {
        return popupDescription;
    }

    public void setPopupDescription(String popupDescription) {
        this.popupDescription = popupDescription;
    }

    public String getPlans() {
        return plans;
    }

    public void setPlans(String plans) {
        this.plans = plans;
    }

    public String getProceedButton() {
        return proceedButton;
    }

    public void setProceedButton(String proceedButton) {
        this.proceedButton = proceedButton;
    }

    public String getBillingHistoryTableDate() {
        return billingHistoryTableDate;
    }

    public void setBillingHistoryTableDate(String billingHistoryTableDate) {
        this.billingHistoryTableDate = billingHistoryTableDate;
    }

    public String getCancelSubscriptionButtonText() {
        return cancelSubscriptionButtonText;
    }

    public void setCancelSubscriptionButtonText(String cancelSubscriptionButtonText) {
        this.cancelSubscriptionButtonText = cancelSubscriptionButtonText;
    }

    public String getBillingHistoryTableDescription() {
        return billingHistoryTableDescription;
    }

    public void setBillingHistoryTableDescription(String billingHistoryTableDescription) {
        this.billingHistoryTableDescription = billingHistoryTableDescription;
    }

    public String getBillingHistoryTableType() {
        return billingHistoryTableType;
    }

    public void setBillingHistoryTableType(String billingHistoryTableType) {
        this.billingHistoryTableType = billingHistoryTableType;
    }

    public String getBillingHistoryTableTotal() {
        return billingHistoryTableTotal;
    }

    public void setBillingHistoryTableTotal(String billingHistoryTableTotal) {
        this.billingHistoryTableTotal = billingHistoryTableTotal;
    }

    public String getBillingHistoryTableTitle() {
        return billingHistoryTableTitle;
    }

    public void setBillingHistoryTableTitle(String billingHistoryTableTitle) {
        this.billingHistoryTableTitle = billingHistoryTableTitle;
    }

    public String getBillingHistoryTableServicePeriod() {
        return billingHistoryTableServicePeriod;
    }

    public void setBillingHistoryTableServicePeriod(String billingHistoryTableServicePeriod) {
        this.billingHistoryTableServicePeriod = billingHistoryTableServicePeriod;
    }

    public String getBillingHistoryTableOffers() {
        return billingHistoryTableOffers;
    }

    public void setBillingHistoryTableOffers(String billingHistoryTableOffers) {
        this.billingHistoryTableOffers = billingHistoryTableOffers;
    }

    public String getBillingHistoryNextChargeLabel() {
        return billingHistoryNextChargeLabel;
    }

    public void setBillingHistoryNextChargeLabel(String billingHistoryNextChargeLabel) {
        this.billingHistoryNextChargeLabel = billingHistoryNextChargeLabel;
    }

    public String getBillingHistoryLastChargeLabel() {
        return billingHistoryLastChargeLabel;
    }

    public void setBillingHistoryLastChargeLabel(String billingHistoryLastChargeLabel) {
        this.billingHistoryLastChargeLabel = billingHistoryLastChargeLabel;
    }

    public String getBillingHistoryJoinDatelabel() {
        return billingHistoryJoinDatelabel;
    }

    public void setBillingHistoryJoinDatelabel(String billingHistoryJoinDatelabel) {
        this.billingHistoryJoinDatelabel = billingHistoryJoinDatelabel;
    }



    public String getBillingHistoryButtonText() {
        return billingHistoryButtonText;
    }

    public void setBillingHistoryButtonText(String billingHistoryButtonText) {
        this.billingHistoryButtonText = billingHistoryButtonText;
    }

    @SerializedName("getSocialFreeMessage")
    @Expose
    String getSocialFreeMessage;

    @SerializedName("getSocialShareviaText")
    @Expose
    String getSocialShareViaText;

    @SerializedName("getSocialReferredFriendsLink")
    @Expose
    String getSocialReferredFriendsLink;

    @SerializedName("getSocialSignInButtonText")
    @Expose
    String getSocialSignInButtonText;

    @SerializedName("items")
    @Expose
    String items;

    @SerializedName("getSocialTermsAndConditionsPageURL")
    @Expose
    String getSocialTermsAndConditionsPageURL;

    @SerializedName("getSocialTermsAndConditions")
    @Expose
    String getSocialTermsAndConditions;



    public String getSocialTermsAndConditions() {
        return getSocialTermsAndConditions;
    }

    public void setGetSocialTermsAndConditions(String getSocialTermsAndConditions) {
        this.getSocialTermsAndConditions = getSocialTermsAndConditions;
    }

    public String getGetSocialTermsAndConditionsPageURL() {
        return getSocialTermsAndConditionsPageURL;
    }

    public void setGetSocialTermsAndConditionsPageURL(String getSocialTermsAndConditionsPageURL) {
        this.getSocialTermsAndConditionsPageURL = getSocialTermsAndConditionsPageURL;
    }

    public String getAddPhoneNo() { return addPhoneNo; }

    public void setAddPhoneNo(String addPhoneNo) { this.addPhoneNo = addPhoneNo; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getBillingHistoryMonthLocalization() {
        return billingHistoryMonthLocalization;
    }

    public String getBillingHistoryYearLocalization() {
        return billingHistoryYearLocalization;
    }


    public String getNeedHelpTitleLabel() {
        return needHelpTitleLabel;
    }

    public String getNeedHelpDescriptionLabel() {
        return needHelpDescriptionLabel;
    }

    public String getConnectedAccountPlaceHolder() {
        return connectedAccountPlaceHolder;
    }

    public String getPersonalizationtTitle() {
        return personalizationtTitle;
    }

    public String getAddTopicCta() {
        return addTopicCta;
    }

    public String getPurchaseTitle() {
        return purchaseTitle;
    }

    public String getRecurrinngPurchaseLabel() {
        return recurrinngPurchaseLabel;
    }

    public String getRecurringBillingLabel() {
        return recurringBillingLabel;
    }

    public String getPriceTrialEnd() {
        return priceTrialEnd;
    }

    public String getMobileSignupCta() {
        return mobileSignupCta;
    }

    public String getMobileLoginCta() {
        return mobileLoginCta;
    }

    public String getLoginTveCta() {
        return loginTveCta;
    }

    public String getProvidersignUpTveCta() {
        return providersignUpTveCta;
    }

    public String getkStrPassword() {
        return kStrPassword;
    }

    public String getNextDateLabel() {
        return nextDateLabel;
    }

    public String getGoogleSignUpCta() {
        return googleSignUpCta;
    }

    public void setGoogleSignUpCta(String googleSignUpCta) {
        this.googleSignUpCta = googleSignUpCta;
    }

    public String getRemoveFromWatchlistCTA() {
        return removeFromWatchlistCTA;
    }

    public String getAddToWatchlistCTA() {
        return addToWatchlistCTA;
    }

    public String getMonth() {
        return month;
    }

    public String getMonths() {
        return months;
    }

    public String getYears() {
        return years;
    }

    public String getYear() {
        return year;
    }

    public String getDay() {
        return day;
    }

    public String getDays() {
        return days;
    }
    public String getWeek() {
        return week;
    }

    public String getWeeks() {
        return weeks;
    }

    public String getAdTag() {
        return adTag;
    }

    public String getSignUpTab() {
        return signUpTab;
    }

    public String getLoginTab() {
        return loginTab;
    }

    public String getFacebookLoginCta() {
        return facebookLoginCta;
    }

    public String getOrSeparator() {
        return orSeparator;
    }

    public String getLoginCta() {
        return loginCta;
    }

    public String getForgotPasswordCtaText() {
        return forgotPasswordCtaText;
    }

    public String getPhoneInput() {
        return phoneInput;
    }

    public String getEmailInput() {
        return emailInput;
    }

    public String getEmailPasswordCta() {
        return emailPasswordCta;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getMobileHeaderText() {
        return mobileHeaderText;
    }

    public String getForgotPasswordError() {
        return forgotPasswordError;
    }

    public String getINCORRECT_CURRENT_PASSWORD() {
        return INCORRECT_CURRENT_PASSWORD;
    }

    public String getMISMATCH_PASSWORD() {
        return MISMATCH_PASSWORD;
    }

    public String getPasswordMismatchError() {
        return passwordMismatchError;
    }

    public String getChangePassword() {
        return changePassword;
    }

    public String getProfileText() {
        return profileText;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getFacebookSignUpCta() {
        return facebookSignUpCta;
    }

    public String getEMAIL_NOT_REGISTERED() {
        return EMAIL_NOT_REGISTERED;
    }

    public String getAccountHeader() {
        return accountHeader;
    }

    public String getEMAIL_OR_PASSWORD_INCORRECT() {
        return EMAIL_OR_PASSWORD_INCORRECT;
    }

    public String getNAME_NOT_VALID() {
        return NAME_NOT_VALID;
    }

    public String getSignUpCtaText() {
        return signUpCtaText;
    }

    public String getForgotPasswordSuccess() {
        return forgotPasswordSuccess;
    }

    public String getEMAIL_NOT_EXIST() {
        return EMAIL_NOT_EXIST;
    }

    public String getPasswordLengthError() {
        return passwordLengthError;
    }

    public String getForgotPasswordProcessError() {
        return forgotPasswordProcessError;
    }

    public String getEMAIL_ALREADY_LINKED() {
        return EMAIL_ALREADY_LINKED;
    }

    public String getPASSWORD_NOT_VALID() {
        return PASSWORD_NOT_VALID;
    }

    public String getDefaultError() {
        return defaultError;
    }

    public String getPasswordInput() {
        return passwordInput;
    }

    public String getAutoplayLabel() {
        return autoplayLabel;
    }

    public String getkOldPassword() {
        return kOldPassword;
    }

    public String getAppSettingsLabel() {
        return appSettingsLabel;
    }

    public String getPasswordPopupNewPassword() {
        return passwordPopupNewPassword;
    }

    public String getAccountLabel() {
        return accountLabel;
    }

    public String getDownloadQualityLabel() {
        return downloadQualityLabel;
    }

    public String getCellularDataLabel() {
        return cellularDataLabel;
    }

    public String getChangeLabel() {
        return changeLabel;
    }

    public String getPaymentProcessorHeader() {
        return paymentProcessorHeader;
    }

    public String getSubscriptionAndBillingLabel() {
        return subscriptionAndBillingLabel;
    }

    public String getProfileUpdateSuccessMessage() {
        return profileUpdateSuccessMessage;
    }

    public String getPasswordUpdateSuccessMessage() {
        return passwordUpdateSuccessMessage;
    }

    public String getDownloadSettingsLabel() {
        return downloadSettingsLabel;
    }

    public String getAppVersionLabel() {
        return appVersionLabel;
    }

    public String getProfilePopupUpdate() {
        return profilePopupUpdate;
    }

    public String getCancelYesLabel() {
        return cancelYesLabel;
    }

    public String getCancelNoLabel() {
        return cancelNoLabel;
    }

    public String getSubscribeNowButtonText() {
        return subscribeNowButtonText;
    }

    public String getSubscriptionPlanHeader() {
        return subscriptionPlanHeader;
    }

    public String getEMAIL_NOT_VALID() {
        return EMAIL_NOT_VALID;
    }

    public String getUpgradePlanButtonText() {
        return upgradePlanButtonText;
    }

    public String getUseSdCardForDownloadsLabel() {
        return useSdCardForDownloadsLabel;
    }

    public String getkNotSubscribed() {
        return kNotSubscribed;
    }

    public String getInvalidNameMsg() {
        return invalidNameMsg;
    }

    public String getSubscribedLabel() {
        return subscribedLabel;
    }

    public String getVerifyPasswordPopUpTitle() {
        return verifyPasswordPopUpTitle;
    }

    public String getINCORRECT_PASSWORD() {
        return INCORRECT_PASSWORD;
    }

    public String getLanguageSettingsLabel() {
        return languageSettingsLabel;
    }

    public String getDownloadQualityTitle() {
        return DownloadQualityTitle;
    }

    public String getContinueButtonLabel() {
        return continueButtonLabel;
    }

    public String getCancelButton() {
        return cancelButton;
    }

    public String getkStrDeleteHistoryAlertTitle() {
        return kStrDeleteHistoryAlertTitle;
    }

    public String getkStrDeleteAllVideosFromHistoryAlertMessage() {
        return kStrDeleteAllVideosFromHistoryAlertMessage;
    }

    public String getkStrDeleteSingleVideoFromHistoryAlertMessage() {
        return kStrDeleteSingleVideoFromHistoryAlertMessage;
    }

    public String getkStrDeleteWatchlistAlertTitle() {
        return kStrDeleteWatchlistAlertTitle;
    }

    public String getkStrDeleteAllVideosFromWatchlistAlertMessage() {
        return kStrDeleteAllVideosFromWatchlistAlertMessage;
    }

    public String getkStrDeleteSingleVideoFromWatchlistAlertMessage() {
        return kStrDeleteSingleVideoFromWatchlistAlertMessage;
    }

    public String getRemoveAllHistoryLabel() {
        return removeAllHistoryLabel;
    }

    public String getRemoveAllWatchlistLabel() {
        return removeAllWatchlistLabel;
    }

    public String getkStrWatchlistEmpty() {
        return kStrWatchlistEmpty;
    }

    public String getkStrHistoryEmpty() {
        return kStrHistoryEmpty;
    }

    public String getkStrYearsAgo() {
        return kStrYearsAgo;
    }

    public String getkStrYearAgo() {
        return kStrYearAgo;
    }

    public String getkStrMonthsAgo() {
        return kStrMonthsAgo;
    }

    public String getkStrMonthAgo() {
        return kStrMonthAgo;
    }

    public String getkStrWeeksAgo() {
        return kStrWeeksAgo;
    }

    public String getkStrWeekAgo() {
        return kStrWeekAgo;
    }

    public String getkStrDaysAgo() {
        return kStrDaysAgo;
    }

    public String getkStrDayAgo() {
        return kStrDayAgo;
    }

    public String getkStrHoursAgo() {
        return kStrHoursAgo;
    }

    public String getkStrHourAgo() {
        return kStrHourAgo;
    }

    public String getkStrMinutesAgo() {
        return kStrMinutesAgo;
    }

    public String getkStrMinuteAgo() {
        return kStrMinuteAgo;
    }

    public String getkStrSecondsAgo() {
        return kStrSecondsAgo;
    }

    public String getkStrJustNow() {
        return kStrJustNow;
    }

    public String getSettingsLabel() {
        return settingsLabel;
    }

    public String getFailedToRemoveFromWatchlist() {
        return failedToRemoveFromWatchlist;
    }

    public String getFailedToAddToWatchlist() {
        return failedToAddToWatchlist;
    }

    public String getRemovedFromWatchlist() {
        return removedFromWatchlist;
    }

    public String getAddedToWatchlistLabel() {
        return addedToWatchlistLabel;
    }

    public String getAddToWatchlistDialogHeader() {
        return addToWatchlistDialogHeader;
    }

    public String getAboutThisConceptLabel() {
        return aboutThisConceptLabel;
    }

    public String getCompleteDetailsCTA() {
        return completeDetailsCTA;
    }

    public String getMeetYourInstructorCTA() {
        return meetYourInstructorCTA;
    }

    public String getAllClassesLabel() {
        return allClassesLabel;
    }

    public String getAddedToBookmarksLabel() {
        return addedToBookmarksLabel;
    }

    public String getFailedToAddToBookmarks() {
        return failedToAddToBookmarks;
    }

    public String getRemovedFromBookmarks() {
        return removedFromBookmarks;
    }

    public String getFailedToRemoveFromBookmarks() {
        return failedToRemoveFromBookmarks;
    }

    public String getAddToWatchlistDialogMessage() {
        return addToWatchlistDialogMessage;
    }

    public String getWatchlistPageTitle() {
        return watchlistPageTitle;
    }

    public String getHistoryPageTitle() {
        return historyPageTitle;
    }

    public String getWatchTrailerCTA() {
        return watchTrailerCTA;
    }

    public String getTimerLabel() {
        return timerLabel;
    }

    public String getSeasonLabel() {
        return seasonLabel;
    }

    public String getSeasonsLabel() {
        return seasonsLabel;
    }

    public String getEpisodeLabel() {
        return episodeLabel;
    }

    public String getProgramsLabel() {
        return programsLabel;
    }

    public String getEpisodesLabel() {
        return episodesLabel;
    }

    public String getProgramLabel() {
        return programLabel;
    }

    public String getProfilePopupText() {
        return profilePopupText;
    }

    public String getProfilePopupEmail() {
        return profilePopupEmail;
    }

    public String getPasswordPopupHeader() {
        return passwordPopupHeader;
    }

    public String getPasswordPopupCurrentPassword() {
        return passwordPopupCurrentPassword;
    }

    public String getPasswordPopupConfirmPassword() {
        return passwordPopupConfirmPassword;
    }

    public String getPasswordPopupUpdate() {
        return passwordPopupUpdate;
    }

    public String getSubscriptionHeader() {
        return subscriptionHeader;
    }

    public String getkFillDetails() {
        return kFillDetails;
    }

    public String getProfilePopupName() {
        return profilePopupName;
    }

    public String getNoDownload() {
        return noDownload;
    }

    public String getGoogleSignInCta() {
        if (googleSignInCta == null)
            return googleLoginCta;
        return googleSignInCta;
    }


    public String getStrDeleteSingleContentFromDownloadAlertMessage() {
        return strDeleteSingleContentFromDownloadAlertMessage;
    }

    public String getStrDeleteAllContentFromDownloadAlertMessage() {
        return strDeleteAllContentFromDownloadAlertMessage;
    }

    public String getStrDeleteAudiosFromDownloadAlertMessage() {
        return strDeleteAudiosFromDownloadAlertMessage;
    }

    public String getMyDownloadLowerCase() {
        return myDownloadLowerCase;
    }

    public String getStrDeleteDownloadAlertTitle() {
        return strDeleteDownloadAlertTitle;
    }

    public String getRetryDownloadMessage() {
        return retryDownloadMessage;
    }

    public String getDeleteAudioMessage() {
        return deleteAudioMessage;
    }

    public String getRemoveAllDownloadCta() {
        return removeAllDownloadCta;
    }

    public String getDirectorLabel() {
        return directorLabel;
    }

    public String getStarringLabel() {
        return starringLabel;
    }

    public String getIncompleteDownload() {
        return incompleteDownload;
    }

    public String getIncompleteDownloadMessage() {
        return incompleteDownloadMessage;
    }

    public String getDownloadNotFinish() {
        return downloadNotFinish;
    }

    public String getDEVICE_LIMIT_EXCEEDED() {
        return DEVICE_LIMIT_EXCEEDED;
    }

    public String getUpi() {
        return upi;
    }

    public String getHeader() {
        return header;
    }

    public String getActivateDeviceCta() {
        return activateDeviceCta;
    }

    public String getLinkDeviceToAccountLabel() {
        return linkDeviceToAccountLabel;
    }

    public String getNoAccountLabel() {
        return noAccountLabel;
    }

    public String getContinueCta() {
        return continueCta;
    }

    public String getCancelCta() {
        return cancelCta;
    }

    public String getEnterEmailToResetPasswordLabel() {
        return enterEmailToResetPasswordLabel;
    }

    public String getLogoutCta() {
        return logoutCta;
    }

    public String getManageSubscriptionCta() {
        return manageSubscriptionCta;
    }

    public String getCloseCaptioningLabel() {
        return closeCaptioningLabel;
    }

    public String getLoggedInAsLabel() {
        return loggedInAsLabel;
    }

    public String getSignUpTermsAgreementLabel() {
        return signUpTermsAgreementLabel;
    }

    public String getTermsOfUseLabel() {
        return termsOfUseLabel;
    }

    public String getAndLabel() {
        return andLabel;
    }

    public String getPrivacyPolicyLabel() {
        return privacyPolicyLabel;
    }

    public String getDeferredSubscriptionCancelDateLabel() {
        return deferredSubscriptionCancelDateLabel;
    }

    public String getActivateDevice() {
        return activateDevice;
    }

    public String getActivateDeviceLabel() {
        return activateDeviceLabel;
    }

    public String getEnterCodeToLinkDevice() {
        return enterCodeToLinkDevice;
    }

    public String getEnterCodeToLinkDeviceLabel_1() {
        return enterCodeToLinkDeviceLabel_1;
    }

    public String getEnterCodeToLinkDeviceLabel_2() {
        return enterCodeToLinkDeviceLabel_2;
    }

    public String getEnterCodeToLinkDeviceLabel_3() {
        return enterCodeToLinkDeviceLabel_3;
    }

    public String getStartWatchingCTA() {
        return startWatchingCTA;
    }

    public String getResumeWatchingCTA() {
        return resumeWatchingCTA;
    }

    public String getWatchNowCTA() {
        return watchNowCTA;
    }

    public String getHistoryTitleLabel() {
        return historyTitleLabel;
    }

    public String getClearWatchlistAlertTitle() {
        return clearWatchlistAlertTitle;
    }

    public String getDeleteItemButton() {
        return deleteItemButton;
    }

    public String getClearWatchlist() {
        return clearWatchlist;
    }

    public String getClearAllHistoryButton() {
        return clearAllHistoryButton;
    }

    public String getkAddedText() {
        return kAddedText;
    }

    public String getkStrMinuteAgoiOS() {
        return kStrMinuteAgoiOS;
    }

    public String getkStrMinutesAgoiOS() {
        return kStrMinutesAgoiOS;
    }

    public String getClearHistoryAlertTitle() {
        return clearHistoryAlertTitle;
    }

    public String getYouAreSignInAsLabel() {
        return youAreSignInAsLabel;
    }

    public String getSelectPlanLabel() {
        return selectPlanLabel;
    }

    public String getKAmazonSubscriptionMetadata1() {
        return kAmazonSubscriptionMetadata1;
    }

    public String getKAmazonSubscriptionMetadata2() {
        return kAmazonSubscriptionMetadata2;
    }

    public String getKAmazonSubscriptionMetadata3() {
        return kAmazonSubscriptionMetadata3;
    }

    public String getKAmazonSubscriptionMetadata4() {
        return kAmazonSubscriptionMetadata4;
    }

    public String getKAmazonSubscriptionMetadata5() {
        return kAmazonSubscriptionMetadata5;
    }

    public String getKAmazonSubscriptionMetadata6() {
        return kAmazonSubscriptionMetadata6;
    }

    public String getDataSaverLabel() {
        return dataSaverLabel;
    }

    public String getGoodQualityLabel() {
        return goodQualityLabel;
    }

    public String getBestQualityLabel() {
        return bestQualityLabel;
    }

    public String getParentalControlHeader() {
        return parentalControlHeader;
    }

    public String getManageParentalFromMobileOrWeb() {
        return manageParentalFromMobileOrWeb;
    }

    public String getViewingRestrictionsCTA() {
        return viewingRestrictionsCTA;
    }

    public String getSaveRatingCTA() {
        return saveRatingCTA;
    }

    public String getSetupPinCTA() {
        return setupPinCTA;
    }

    public String getResetPinCTA() {
        return resetPinCTA;
    }

    public String getEnterPinLabel() {
        return enterPinLabel;
    }

    public String getAllRatingCategory() {
        return allRatingCategory;
    }

    public String getAllRatingMessage() {
        return allRatingMessage;
    }

    public String getSevenRatingCategory() {
        return sevenRatingCategory;
    }

    public String getSevenRatingMessage() {
        return sevenRatingMessage;
    }

    public String getThirteenRatingCategory() {
        return thirteenRatingCategory;
    }

    public String getThirteenRatingMessage() {
        return thirteenRatingMessage;
    }

    public String getSixteenRatingCategory() {
        return sixteenRatingCategory;
    }

    public String getSixteenRatingMessage() {
        return sixteenRatingMessage;
    }

    public String getEighteenRatingCategory() {
        return eighteenRatingCategory;
    }

    public String getEighteenRatingMessage() {
        return eighteenRatingMessage;
    }

    public String getTapAgeMessage() {
        return tapAgeMessage;
    }

    public String getAuthWithFacebook() {
        return authWithFacebook;
    }

    public String getAuthWithGoogle() {
        return authWithGoogle;
    }

    public String getEnableFaceId() {
        return enableFaceId;
    }

    public String getEnableTouchId() {
        return enableTouchId;
    }

    public String getEnableTouchIdMessage() {
        return enableTouchIdMessage;
    }

    public String getEnableFaceIdMessage() {
        return enableFaceIdMessage;
    }

    public String getTouchIdNotEnrolled() {
        return touchIdNotEnrolled;
    }

    public String getFacebookError() {
        return facebookError;
    }

    public String getGoogleError() {
        return googleError;
    }


    private String getGoogleLoginCta() {
        return googleLoginCta;
    }

    public String getkAmazonSubscriptionMetadata1() {
        return kAmazonSubscriptionMetadata1;
    }

    public String getkAmazonSubscriptionMetadata2() {
        return kAmazonSubscriptionMetadata2;
    }

    public String getkAmazonSubscriptionMetadata3() {
        return kAmazonSubscriptionMetadata3;
    }

    public String getkAmazonSubscriptionMetadata4() {
        return kAmazonSubscriptionMetadata4;
    }

    public String getkAmazonSubscriptionMetadata5() {
        return kAmazonSubscriptionMetadata5;
    }

    public String getkAmazonSubscriptionMetadata6() {
        return kAmazonSubscriptionMetadata6;
    }

    public String getMobileLabel() {
        return mobileLabel;
    }

    public String getVerifyOTPText() {
        return verifyOTPText;
    }

    public String getVerifyOTPSubmit() {
        return verifyOTPSubmit;
    }

    public String getResentOTPText() {
        return resentOTPText;
    }

    public String getVERIFY_OTP_FAILED() {
        return VERIFY_OTP_FAILED;
    }

    public String getOTP_SENT_FAILED() {
        return OTP_SENT_FAILED;
    }

    public String getPHONE_NOT_LINKED() {
        return PHONE_NOT_LINKED;
    }

    public String getINVALID_REQUEST_PARAMS() {
        return INVALID_REQUEST_PARAMS;
    }

    public String getPHONE_NOT_VALID() {
        return PHONE_NOT_VALID;
    }

    public String getPHONE_ALREADY_LINKED() {
        return PHONE_ALREADY_LINKED;
    }

    public String getUPDATE_PHONE_NUMBER() {
        return UPDATE_PHONE_NUMBER;
    }

    public String getNOT_RECEIVED_OTP() {
        return NOT_RECEIVED_OTP;
    }

    public String getSEND_OTP_CTA_TEXT() {
        return SEND_OTP_CTA_TEXT;
    }

    public String getLoginEmailPassword() {
        return loginEmailPassword;
    }

    public String getAccountDetailsLabel() {
        return accountDetailsLabel;
    }
    public String getNameLabel() {
        return nameLabel;
    }
    public String getPhoneLabel() {
        return phoneLabel;
    }
    public String getEmailLabel() {
        return emailLabel;
    }
    public String getPasswordLabel() {
        return passwordLabel;
    }
    public String getEditPhoneNumberLabel() {
        return editPhoneNumberLabel;
    }
    public String getEditLabel() {
        return editLabel;
    }
    public String getYourInterests() {
        return yourInterests;
    }
    public String getEditEmailLabel() {
        return editEmailLabel;
    }
    public String getEditNameLabel() {
        return editNameLabel;
    }
    public String getEditPasswordLabel() {
        return editPasswordLabel;
    }
    public String getSubscriptionLabel() {
        return subscriptionLabel;
    }

    public String getTimesLabel() {
        return timesLabel;
    }

    public void setTimesLabel(String timesLabel) {
        this.timesLabel = timesLabel;
    }

    public String getRepeatLabel() {
        return repeatLabel;
    }

    public void setRepeatLabel(String repeatLabel) {
        this.repeatLabel = repeatLabel;
    }
    public String getResumeWorkoutCTA() {
        return resumeWorkoutCTA;
    }

    public void setResumeWorkoutCTA(String resumeWorkoutCTA) {
        this.resumeWorkoutCTA = resumeWorkoutCTA;
    }
    public String getRestMessage() {
        return restMessage;
    }

    public void setRestMessage(String restMessage) {
        this.restMessage = restMessage;
    }
    public String getRestHeader() {
        return restHeader;
    }

    public void setRestHeader(String restHeader) {
        this.restHeader = restHeader;
    }

    public String getBackToVideoCTA() {
        return backToVideoCTA;
    }

    public void setBackToVideoCTA(String backToVideoCTA) {
        this.backToVideoCTA = backToVideoCTA;
    }

    public String getReplayVideoCTA() {
        return replayVideoCTA;
    }

    public void setReplayVideoCTA(String replayVideoCTA) {
        this.replayVideoCTA = replayVideoCTA;
    }

    public String getRepeatCircuitCTA() {
        return repeatCircuitCTA;
    }

    public void setRepeatCircuitCTA(String repeatCircuitCTA) {
        this.repeatCircuitCTA = repeatCircuitCTA;
    }

    public String getStartWorkoutCTA() {
        return startWorkoutCTA;
    }

    public void setStartWorkoutCTA(String startWorkoutCTA) {
        this.startWorkoutCTA = startWorkoutCTA;
    }


    public String getRestCTA() {
        return restCTA;
    }

    public void setRestCTA(String restCTA) {
        this.restCTA = restCTA;
    }

    public String getNextCircuitCTA() {
        return nextCircuitCTA;
    }

    public void setNextCircuitCTA(String nextCircuitCTA) {
        this.nextCircuitCTA = nextCircuitCTA;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShortParagraph() {
        return shortParagraph;
    }

    public void setShortParagraph(String shortParagraph) {
        this.shortParagraph = shortParagraph;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getButtonLink() {
        return buttonLink;
    }

    public void setButtonLink(String buttonLink) {
        this.buttonLink = buttonLink;
    }

    public String getImage() {
        return image;
    }

    public String getGetSocialFreeMessage() {
        return getSocialFreeMessage;
    }

    public String getGetSocialShareViaText() {
        return getSocialShareViaText;
    }

    public String getGetSocialReferredFriendsLink() {
        return getSocialReferredFriendsLink;
    }

    public String getGetSocialSignInButtonText() {
        return getSocialSignInButtonText;
    }

    public String getAddBilling() {
        return addBilling;
    }

    public String getVerifyFirstPay() {
        return verifyFirstPay;
    }

    public String getOnSeparator() {
        return onSeparator;
    }

    public String getPromoHeading() {
        return promoHeading;
    }

    public String getAddPromo() {
        return addPromo;
    }

    public String getApplyPromo() {
        return applyPromo;
    }

    public String getRemovePromo() {
        return removePromo;
    }

    public String getEnterPromoCode() {
        return enterPromoCode;
    }

    public String getDiscountOfApplied() {
        return discountOfApplied;
    }

    public String getPlanSubscription() {
        return planSubscription;
    }

    public String getTotal() {
        return total;
    }

    public String getDiscountApplied() {
        return discountApplied;
    }

    public String getTotalBilling() {
        return totalBilling;
    }


    public String getCODE_NOT_VALID_PLAN() {
        return CODE_NOT_VALID_PLAN;
    }

    public String getNOT_VALID_CODE() {
        return NOT_VALID_CODE;
    }

    public String getNOT_VALID_CODE_SUBMITTED() {
        return NOT_VALID_CODE_SUBMITTED;
    }

    public String getNO_PREPAID_CODE() {
        return NO_PREPAID_CODE;
    }

    public String getOFFER_ALREADY_USED() {
        return OFFER_ALREADY_USED;
    }

    public String getOFFER_CODE_EXPIRED() {
        return OFFER_CODE_EXPIRED;
    }

    public List<Bank> getBanks() {
        try {
            return new Gson().fromJson(banks, new TypeToken<List<Bank>>(){}.getType());
        } catch (Exception e) {
            Log.e("error", ":"+e);
        }
        return null;
    }

    public List<Wallet> getWallets() {
        try {
            return new Gson().fromJson(wallets, new TypeToken<List<Wallet>>(){}.getType());
        } catch (Exception e) {
            Log.e("error", ":"+e);
        }
        return null;
    }

    public List<Card> getCards() {
        try {
            return new Gson().fromJson(cards, new TypeToken<List<Card>>(){}.getType());
        } catch (Exception e) {
            Log.e("error", ":"+e);
        }
        return null;
    }

    public List<UPI> getUPI() {
        try {
            return new Gson().fromJson(upi, new TypeToken<List<UPI>>(){}.getType());
        } catch (Exception e) {
            Log.e("error", ":"+e);
        }
        return null;
    }

    public List<Item> getItems() {
        try {
            return new Gson().fromJson(items, new TypeToken<List<Item>>(){}.getType());
        } catch (Exception e) {
            Log.e("error", ":"+e);
        }
        return null;
    }

    public String getSubscribeNowCta() {
        return subscribeNowCta;
    }

    public class Wallet {
        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("imageUrl")
        @Expose
        String imageUrl;

        @SerializedName("offer")
        @Expose
        String offer;

        @SerializedName("key")
        @Expose
        String key;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getOffer() {
            return offer;
        }

        public void setOffer(String offer) {
            this.offer = offer;
        }

        public String getKey() {
            return key;
        }
    }

    public class Card {
        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("imageUrl")
        @Expose
        String imageUrl;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public class Bank {
        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("imageUrl")
        @Expose
        String imageUrl;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public class UPI {
        @SerializedName("title")
        @Expose
        String title;

        @SerializedName("imageUrl")
        @Expose
        String imageUrl;

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class Item {
        @SerializedName("imageUrl")
        @Expose
        String imageUrl;

        @SerializedName("description")
        @Expose
        String description;

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDescription() {
            return description;
        }
    }


}

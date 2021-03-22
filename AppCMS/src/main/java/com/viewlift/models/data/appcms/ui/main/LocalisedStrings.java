package com.viewlift.models.data.appcms.ui.main;

import android.content.Context;

import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.android.LocalizationResult;
import com.viewlift.utils.CommonUtils;

import javax.inject.Inject;

public class LocalisedStrings {
    LocalizationResult localizationResult = null;
    GenericMessages genericMessages = null;
    Context context = null;

    @Inject
    public LocalisedStrings(Context context) {
        this.context = context;
    }

    public void setObjects(LocalizationResult localizationResult, GenericMessages genericMessages) {
        this.localizationResult = localizationResult;
        this.genericMessages = genericMessages;
    }

    public String getCannotPurchaseItemMsg(String domain) {
        StringBuilder builder = new StringBuilder();

        if (localizationResult != null && localizationResult.getContentNotEnabledMessagePrefix() != null)
            builder.append(localizationResult.getContentNotEnabledMessagePrefix());
        else if (genericMessages != null && genericMessages.getContentNotEnabledMessagePrefix() != null)
            builder.append(genericMessages.getContentNotEnabledMessagePrefix());
        if (CommonUtils.isEmpty(builder.toString()))
            return context.getString(R.string.cannot_purchase_item_msg, domain);
        builder.append(" ");
//        builder.append(getAppCMSMain().getDomainName());
        builder.append(domain);
        builder.append(" ");
        if (localizationResult != null && localizationResult.getContentNotEnabledMessageSuffix() != null)
            builder.append(localizationResult.getContentNotEnabledMessageSuffix());
        else if (genericMessages != null && genericMessages.getContentNotEnabledMessageSuffix() != null)
            builder.append(genericMessages.getContentNotEnabledMessageSuffix());
        return builder.toString();
    }

    public String getRentTimeDialogMsg(String rentalPeriod) {
        StringBuilder builder = new StringBuilder();
        if (localizationResult != null && localizationResult.getRentStartMessagePrefix() != null)
            builder.append(localizationResult.getRentStartMessagePrefix());
        else if (genericMessages != null && genericMessages.getRentStartMessagePrefix() != null)
            builder.append(genericMessages.getRentStartMessagePrefix());
        if (builder.toString().length() == 0)
            return null;
        builder.append(" ");
        builder.append(rentalPeriod);
        builder.append(" ");
        if (localizationResult != null && localizationResult.getRentStartMessageSuffix() != null)
            builder.append(localizationResult.getRentStartMessageSuffix());
        else if (genericMessages != null && genericMessages.getRentStartMessageSuffix() != null)
            builder.append(genericMessages.getRentStartMessageSuffix());
        return builder.toString();
    }

    public String getBackCta() {
        String back = context.getString(R.string.back);
        if (localizationResult != null && localizationResult.getErrorDialogBackCta() != null)
            back = localizationResult.getErrorDialogBackCta();
        else if (genericMessages != null && genericMessages.getErrorDialogBackCta() != null)
            back = genericMessages.getErrorDialogBackCta();
        return back;
    }

    public String getContentNotAvailable() {
        String unavailable = context.getString(R.string.content_time_not_available_in_for_rent);
        if (localizationResult != null && localizationResult.getVideoNotPlayableDialogTitle() != null)
            unavailable = localizationResult.getVideoNotPlayableDialogTitle();
        else if (genericMessages != null && genericMessages.getVideoNotPlayableDialogTitle() != null)
            unavailable = genericMessages.getVideoNotPlayableDialogTitle();
        return unavailable;
    }

    public String getDrmNotSupport() {
        String unavailable = context.getString(R.string.drm_not_message);
        if (localizationResult != null && localizationResult.getDrmNotSupportHeader() != null)
            unavailable = localizationResult.getDrmNotSupportHeader();
        else if (genericMessages != null && genericMessages.getDrmNotSupportHeader() != null)
            unavailable = genericMessages.getDrmNotSupportHeader();
        return unavailable;
    }

    public String getVideoPlaybackHeaderMessage() {
        String unavailable = context.getString(R.string.playback_header_drm);
        if (localizationResult != null && localizationResult.getMediaSessionErrorHeader() != null)
            unavailable = localizationResult.getMediaSessionErrorHeader();
        else if (genericMessages != null && genericMessages.getMediaSessionErrorHeader() != null)
            unavailable = genericMessages.getMediaSessionErrorHeader();
        return unavailable;
    }

    public String getErrorTitle() {
        String error = context.getString(R.string.app_cms_video_not_available_error_title);
        if (localizationResult != null && localizationResult.getkError() != null)
            error = localizationResult.getkError();
        else if (genericMessages != null && genericMessages.getkError() != null)
            error = genericMessages.getkError();
        return error;
    }

    public String getAlertTitle() {
        String alert = context.getString(R.string.app_cms_rent_time_dialog_button_title);
        if (localizationResult != null && localizationResult.getAlertDialogTitle() != null)
            alert = localizationResult.getAlertDialogTitle();
        else if (genericMessages != null && genericMessages.getAlertDialogTitle() != null)
            alert = genericMessages.getAlertDialogTitle();
        return alert;
    }

    public String getInternetErrorHeader() {
        String error = context.getString(R.string.app_cms_network_connectivity_error_title);
        if (localizationResult != null && localizationResult.getInternetErrorMessageHeader() != null)
            error = localizationResult.getInternetErrorMessageHeader();
        else if (genericMessages != null && genericMessages.getInternetErrorMessageHeader() != null)
            error = genericMessages.getInternetErrorMessageHeader();
        return error;
    }

    public String getInternetErrorMsg() {
        String msg = context.getString(R.string.app_cms_network_connectivity_error_message);
        if (localizationResult != null && localizationResult.getInternetErrorMessageText() != null)
            msg = localizationResult.getInternetErrorMessageText();
        else if (genericMessages != null && genericMessages.getInternetErrorMessageText() != null)
            msg = genericMessages.getInternetErrorMessageText();
        return msg;
    }

    public String getOkText() {
        String ok = context.getString(R.string.ok);
        if (localizationResult != null && localizationResult.getErrorDialogOkCta() != null)
            ok = localizationResult.getErrorDialogOkCta();
        else if (genericMessages != null && genericMessages.getErrorDialogOkCta() != null)
            ok = genericMessages.getErrorDialogOkCta();
        return ok;
    }

    public String getYesText() {
        String ok = context.getString(R.string.yes);
        if (localizationResult != null && localizationResult.getErrorDialogYesCta() != null)
            ok = localizationResult.getErrorDialogYesCta();
        else if (genericMessages != null && genericMessages.getErrorDialogYesCta() != null)
            ok = genericMessages.getErrorDialogYesCta();
        return ok;
    }

    public String getNoText() {
        String no = context.getString(R.string.app_cms_negative_confirmation_button_text);
        if (localizationResult != null && localizationResult.getErrorDialogNoCta() != null)
            no = localizationResult.getErrorDialogNoCta();
        else if (genericMessages != null && genericMessages.getErrorDialogNoCta() != null)
            no = genericMessages.getErrorDialogNoCta();
        return no;
    }

    public String getCancelText() {
        String cancel = context.getString(R.string.cancel);
        if (localizationResult != null && localizationResult.getErrorDialogCancelCta() != null)
            cancel = localizationResult.getErrorDialogCancelCta();
        else if (genericMessages != null && genericMessages.getErrorDialogCancelCta() != null)
            cancel = genericMessages.getErrorDialogCancelCta();
        return cancel;

    }

    public String getCancelSubscriptionButtonText() {
        String cancel = context.getString(R.string.cancel);
        if (localizationResult != null && localizationResult.getCancelSubscriptionButtonText() != null)
            cancel = localizationResult.getCancelSubscriptionButtonText();
        else if (genericMessages != null && genericMessages.getCancelSubscriptionButtonText() != null)
            cancel = genericMessages.getCancelSubscriptionButtonText();
        return cancel;

    }

    public String getRetryText() {
        String retry = context.getString(R.string.app_cms_retry_text);
        if (localizationResult != null && localizationResult.getErrorDialogRetryCta() != null)
            retry = localizationResult.getErrorDialogRetryCta();
        else if (genericMessages != null && genericMessages.getErrorDialogRetryCta() != null)
            retry = genericMessages.getErrorDialogRetryCta();
        return retry;
    }

    public String getCloseText() {
        String close = context.getString(R.string.app_cms_close_text);
        if (localizationResult != null && localizationResult.getErrorDialogCloseCta() != null)
            close = localizationResult.getErrorDialogCloseCta();
        else if (genericMessages != null && genericMessages.getErrorDialogCloseCta() != null)
            close = genericMessages.getErrorDialogCloseCta();
        return close;
    }

    public String getSubscribeNowText() {
        String subscribe = context.getString(R.string.app_cms_subscribe_now);
        if (localizationResult != null && localizationResult.getSubscribeNowDialogButton() != null)
            subscribe = localizationResult.getSubscribeNowDialogButton();
        else if (genericMessages != null && genericMessages.getSubscribeNowDialogButton() != null)
            subscribe = genericMessages.getSubscribeNowDialogButton();
        return subscribe;
    }

    public String getSubscriptionRequiredText() {
        String subscription = context.getString(R.string.app_cms_subscription_required_title);
        if (localizationResult != null && localizationResult.getSubscriptionRequiredDialogTitle() != null)
            subscription = localizationResult.getSubscriptionRequiredDialogTitle();
        else if (genericMessages != null && genericMessages.getSubscriptionRequiredDialogTitle() != null)
            subscription = genericMessages.getSubscriptionRequiredDialogTitle();
        return subscription;
    }

    public String getPremiumContentText() {
        String premium = context.getString(R.string.preview_content);
        if (localizationResult != null && localizationResult.getPremiumContentDialogTitle() != null)
            premium = localizationResult.getPremiumContentDialogTitle();
        else if (genericMessages != null && genericMessages.getPremiumContentDialogTitle() != null)
            premium = genericMessages.getPremiumContentDialogTitle();
        return premium;
    }

    public String getPremiumLoggedInUserMsg() {
        String msg = context.getString(R.string.unsubscribe_text);
        if (localizationResult != null && localizationResult.getPremiumContentLoggedInUserDialogMessage() != null)
            msg = localizationResult.getPremiumContentLoggedInUserDialogMessage();
        else if (genericMessages != null && genericMessages.getPremiumContentLoggedInUserDialogMessage() != null)
            msg = genericMessages.getPremiumContentLoggedInUserDialogMessage();
        return msg;
    }

    public String getInvalidEmailText() {
        String msg = context.getString(R.string.invalid_email);
        if (localizationResult != null && localizationResult.getInvalidEmailDialogTitle() != null)
            msg = localizationResult.getInvalidEmailDialogTitle();
        else if (genericMessages != null && genericMessages.getInvalidEmailDialogTitle() != null)
            msg = genericMessages.getInvalidEmailDialogTitle();
        return msg;
    }

    public String getDownloadVideoLogoutTitle() {
        String msg = context.getString(R.string.app_cms_logout_with_running_download_title);
        if (localizationResult != null && localizationResult.getLogoutVideoDownloadDialogTitle() != null)
            msg = localizationResult.getLogoutVideoDownloadDialogTitle();
        else if (genericMessages != null && genericMessages.getLogoutVideoDownloadDialogTitle() != null)
            msg = genericMessages.getLogoutVideoDownloadDialogTitle();
        return msg;
    }

    public String getEmailFormatValidationMsg() {
        String msg = context.getString(R.string.email_format_msg);
        if (localizationResult != null && localizationResult.getEmailFormatValidationMessage() != null)
            msg = localizationResult.getEmailFormatValidationMessage();
        else if (genericMessages != null && genericMessages.getEmailFormatValidationMessage() != null)
            msg = genericMessages.getEmailFormatValidationMessage();
        return msg;
    }

    public String getDownloadVideoLogoutMsg() {
        String msg = context.getString(R.string.app_cms_logout_with_running_download_message);
        if (localizationResult != null && localizationResult.getLogoutVideoDownloadDialogMessage() != null)
            msg = localizationResult.getLogoutVideoDownloadDialogMessage();
        else if (genericMessages != null && genericMessages.getLogoutVideoDownloadDialogMessage() != null)
            msg = genericMessages.getLogoutVideoDownloadDialogMessage();
        return msg;
    }

    public String getLivePreviewSportsFitnessMsg() {
        String msg = context.getString(R.string.app_cms_live_preview_text_message);
        if (localizationResult != null && localizationResult.getLivePreviewMessageSportsFitness() != null)
            msg = localizationResult.getLivePreviewMessageSportsFitness();
        else if (genericMessages != null && genericMessages.getLivePreviewMessageSportsFitness() != null)
            msg = genericMessages.getLivePreviewMessageSportsFitness();
        return msg;
    }

    public String getLivePreviewOtherMsg() {
        String msg = context.getString(R.string.app_cms_login_and_subscription_premium_content_required_message);
        if (localizationResult != null && localizationResult.getLivePreviewMessageOther() != null)
            msg = localizationResult.getLivePreviewMessageOther();
        else if (genericMessages != null && genericMessages.getLivePreviewMessageOther() != null)
            msg = genericMessages.getLivePreviewMessageOther();
        return msg;
    }

    public String getLivePreviewEndedText() {
        String msg = context.getString(R.string.app_cms_login_and_subscription_audio_preview_title);
        if (localizationResult != null && localizationResult.getPreviewEndDialogTitle() != null)
            msg = localizationResult.getPreviewEndDialogTitle();
        else if (genericMessages != null && genericMessages.getPreviewEndDialogTitle() != null)
            msg = genericMessages.getPreviewEndDialogTitle();
        return msg;
    }

    public String getSubscriptionMsgHeaderText() {
        String msg = context.getString(R.string.app_cms_subscription_title);
        if (localizationResult != null && localizationResult.getSubscriptionMessageHeader() != null)
            msg = localizationResult.getSubscriptionMessageHeader();
        else if (genericMessages != null && genericMessages.getSubscriptionMessageHeader() != null)
            msg = genericMessages.getSubscriptionMessageHeader();
        return msg;
    }

    public String getSubscriptionWebsiteMsg() {
        String msg = context.getString(R.string.app_cms_subscription_upgrade_for_web_user_dialog);
        if (localizationResult != null && localizationResult.getSubscriptionFromWebsiteMessage() != null)
            msg = localizationResult.getSubscriptionFromWebsiteMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionFromWebsiteMessage() != null)
            msg = genericMessages.getSubscriptionFromWebsiteMessage();
        return msg;
    }

    public String getSubscriptionAppleMsg() {
        String msg = context.getString(R.string.app_cms_subscription_upgrade_for_ios_user_dialog);
        if (localizationResult != null && localizationResult.getSubscriptionFromAppleMessage() != null)
            msg = localizationResult.getSubscriptionFromAppleMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionFromAppleMessage() != null)
            msg = genericMessages.getSubscriptionFromAppleMessage();
        return msg;
    }

    public String getSubscriptionRokuMsg() {
        String msg = context.getString(R.string.app_cms_subscription_upgrade_for_roku_user_dialog);
        if (localizationResult != null && localizationResult.getSubscriptionFromRokuMessage() != null)
            msg = localizationResult.getSubscriptionFromRokuMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionFromRokuMessage() != null)
            msg = genericMessages.getSubscriptionFromRokuMessage();
        return msg;
    }


    public String getSubscriptionAndroidMsg() {
        String msg = context.getString(R.string.subscription_purchased_from_android_msg);
        if (localizationResult != null && localizationResult.getSubscriptionFromAndroidMessage() != null)
            msg = localizationResult.getSubscriptionFromAndroidMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionFromAndroidMessage() != null)
            msg = genericMessages.getSubscriptionFromAndroidMessage();
        return msg;
    }

    public String getSubscriptionFireTVMsg() {
        String msg = context.getString(R.string.subscription_purchased_from_fire_tv_msg);
        if (localizationResult != null && localizationResult.getSubscriptionFromAmazonMessage() != null)
            msg = localizationResult.getSubscriptionFromAmazonMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionFromAmazonMessage() != null)
            msg = genericMessages.getSubscriptionFromAmazonMessage();
        return msg;
    }

    public String getInternetConnectionMsg() {
        String msg = context.getString(R.string.no_network_connectivity_message);
        if (localizationResult != null && localizationResult.getkInternetConnectionPostString() != null)
            msg = localizationResult.getkInternetConnectionPostString();
        else if (genericMessages != null && genericMessages.getkInternetConnectionPostString() != null)
            msg = genericMessages.getkInternetConnectionPostString();
        return msg;
    }

    public String getDRMMessage() {
        String msg = context.getString(R.string.no_download_drm_message);
        if (localizationResult != null && localizationResult.getDrmNotDownloaded() != null)
            msg = localizationResult.getDrmNotDownloaded();
        else if (genericMessages != null && genericMessages.getDrmNotDownloaded() != null)
            msg = genericMessages.getDrmNotDownloaded();
        return msg;
    }

    public String getCastDRMMessage() {
        String msg = context.getString(R.string.no_cast_drm_message);
        if (localizationResult != null && localizationResult.getDrmNotCasted() != null)
            msg = localizationResult.getDrmNotCasted();
        else if (genericMessages != null && genericMessages.getDrmNotCasted() != null)
            msg = genericMessages.getDrmNotCasted();
        return msg;
    }


    public String getTermsOfUsesText() {
        String msg = context.getString(R.string.terms_of_uses);
        if (localizationResult != null && localizationResult.getTermsOfUseLabel() != null)
            msg = localizationResult.getTermsOfUseLabel();
        else if (genericMessages != null && genericMessages.getTermsOfUseLabel() != null)
            msg = genericMessages.getTermsOfUseLabel();
        return msg;
    }

    public String getPrivacyPolicyText() {
        String msg = context.getString(R.string.privacy_policy);
        if (localizationResult != null && localizationResult.getPrivacyPolicyLabel() != null)
            msg = localizationResult.getPrivacyPolicyLabel();
        else if (genericMessages != null && genericMessages.getPrivacyPolicyLabel() != null)
            msg = genericMessages.getPrivacyPolicyLabel();
        return msg;
    }

    public String getTnCext() {
        String msg = context.getString(R.string.benefit_terms);
        if (localizationResult != null && localizationResult.getTncDetails() != null)
            msg = localizationResult.getTncDetails();
        else if (genericMessages != null && genericMessages.getTncDetails() != null)
            msg = genericMessages.getTncDetails();
        return msg;
    }

    public String getEmptyPasswordValidationText() {
        String msg = context.getString(R.string.empty_password_message);
        if (localizationResult != null && localizationResult.getEmptyPasswordValidationMessage() != null)
            msg = localizationResult.getEmptyPasswordValidationMessage();
        else if (genericMessages != null && genericMessages.getEmptyPasswordValidationMessage() != null)
            msg = genericMessages.getEmptyPasswordValidationMessage();
        return msg;
    }

    public String getEmptyEmailValidationText() {
        String msg = context.getString(R.string.empty_email_message);
        if (localizationResult != null && localizationResult.getEmptyEmailValidationMessage() != null)
            msg = localizationResult.getEmptyEmailValidationMessage();
        else if (genericMessages != null && genericMessages.getEmptyEmailValidationMessage() != null)
            msg = genericMessages.getEmptyEmailValidationMessage();
        return msg;
    }

    public String getNameValidationText() {
        String msg = context.getString(R.string.name_validation);
        if (localizationResult != null && localizationResult.getNAME_NOT_VALID() != null)
            msg = localizationResult.getNAME_NOT_VALID();
        else if (genericMessages != null && genericMessages.getNAME_NOT_VALID() != null)
            msg = genericMessages.getNAME_NOT_VALID();
        return msg;
    }

    public String getContentRatingDescText() {
        String msg = context.getString(R.string.content_rating_description);
        if (localizationResult != null && localizationResult.getContentRatingDescriptionLabel() != null)
            msg = localizationResult.getContentRatingDescriptionLabel();
        else if (genericMessages != null && genericMessages.getContentRatingDescriptionLabel() != null)
            msg = genericMessages.getContentRatingDescriptionLabel();
        return msg;
    }

    public String getPlayText() {
        String msg = context.getString(R.string.label_play);
        if (localizationResult != null && localizationResult.getPlayCta() != null)
            msg = localizationResult.getPlayCta();
        else if (genericMessages != null && genericMessages.getPlayCta() != null)
            msg = genericMessages.getPlayCta();
        return msg;
    }

    public String getFinishedText(String text) {
        String msg = text;
        if (localizationResult != null && localizationResult.getFinishedTitleLabel() != null)
            msg = localizationResult.getFinishedTitleLabel();
        else if (genericMessages != null && genericMessages.getFinishedTitleLabel() != null)
            msg = genericMessages.getFinishedTitleLabel();
        return msg;
    }

    public String getPlayInText(String text) {
        String msg = text;
        if (localizationResult != null && localizationResult.getPlayInLabel() != null)
            msg = localizationResult.getPlayInLabel();
        else if (genericMessages != null && genericMessages.getPlayInLabel() != null)
            msg = genericMessages.getPlayInLabel();
        return msg;
    }

    public String getDownloadSpaceText() {
        String msg = context.getString(R.string.app_cms_download_failed_error_message);
        if (localizationResult != null && localizationResult.getDownloadSpaceDialogMessage() != null)
            msg = localizationResult.getDownloadSpaceDialogMessage();
        else if (genericMessages != null && genericMessages.getDownloadSpaceDialogMessage() != null)
            msg = genericMessages.getDownloadSpaceDialogMessage();
        return msg;
    }

    public String getHoverSeasonsLabelText() {
        String msg = context.getString(R.string.days_title);
        if (localizationResult != null && localizationResult.getHoverSeasonsLabel() != null)
            msg = localizationResult.getHoverSeasonsLabel();
        else if (genericMessages != null && genericMessages.getHoverSeasonsLabel() != null)
            msg = genericMessages.getHoverSeasonsLabel();
        return msg;
    }

    public String getGeoRestrictText() {
        String msg = context.getString(R.string.geo_restrict_error_message);
        if (localizationResult != null && localizationResult.getGeoRestrictErrorMessage() != null)
            msg = localizationResult.getGeoRestrictErrorMessage();
        else if (genericMessages != null && genericMessages.getGeoRestrictErrorMessage() != null)
            msg = genericMessages.getGeoRestrictErrorMessage();
        return msg;
    }

    public String getDownloadSettingsText() {
        String msg = context.getString(R.string.app_cms_download_quality_title);
        if (localizationResult != null && localizationResult.getTitleDownloadSettingLabel() != null)
            msg = localizationResult.getTitleDownloadSettingLabel();
        else if (genericMessages != null && genericMessages.getTitleDownloadSettingLabel() != null)
            msg = genericMessages.getTitleDownloadSettingLabel();
        return msg;
    }

    public String getSelectLanguageText() {
        String msg = context.getString(R.string.language_setting_title_key);
        if (localizationResult != null && localizationResult.getSelectLanguage() != null)
            msg = localizationResult.getSelectLanguage();
        else if (genericMessages != null && genericMessages.getSelectLanguage() != null)
            msg = genericMessages.getSelectLanguage();
        return msg;
    }

    public String getContinueCtaText() {
        String msg = context.getString(R.string.continue_button);
        if (localizationResult != null && localizationResult.getContinueCta() != null)
            msg = localizationResult.getContinueCta();
        else if (genericMessages != null && genericMessages.getContinueCta() != null)
            msg = genericMessages.getContinueCta();
        return msg;
    }

    public String getServerErrorText() {
        String msg = context.getString(R.string.error_message_please_try_again);
        if (localizationResult != null && localizationResult.getServerErrorMessageText() != null)
            msg = localizationResult.getServerErrorMessageText();
        else if (genericMessages != null && genericMessages.getServerErrorMessageText() != null)
            msg = genericMessages.getServerErrorMessageText();
        return msg;
    }

    public String getLogoutText() {
        String msg = context.getString(R.string.app_cms_signout_error_msg);
        if (localizationResult != null && localizationResult.getLogoutDialogMessage() != null)
            msg = localizationResult.getLogoutDialogMessage();
        else if (genericMessages != null && genericMessages.getLogoutDialogMessage() != null)
            msg = genericMessages.getLogoutDialogMessage();
        return msg;
    }

    public String getCellularDisableText() {
        String msg = context.getString(R.string.app_cms_download_over_cellular_disabled_error_message);
        if (localizationResult != null && localizationResult.getCellularDisabledErrorMsg() != null)
            msg = localizationResult.getCellularDisabledErrorMsg();
        else if (genericMessages != null && genericMessages.getCellularDisabledErrorMsg() != null)
            msg = genericMessages.getCellularDisabledErrorMsg();
        return msg;
    }

    public String getShareVideoText() {
        String msg = context.getResources().getString(R.string.send_to);
        if (localizationResult != null && localizationResult.getShareVideo() != null)
            msg = localizationResult.getShareVideo();
        else if (genericMessages != null && genericMessages.getShareVideo() != null)
            msg = genericMessages.getShareVideo();
        return msg;
    }

    public String getLoadingOnCastingText() {
        String msg = context.getString(R.string.loading_vid_on_casting);
        if (localizationResult != null && localizationResult.getCastingLoadingMessage() != null)
            msg = localizationResult.getCastingLoadingMessage();
        else if (genericMessages != null && genericMessages.getCastingLoadingMessage() != null)
            msg = genericMessages.getCastingLoadingMessage();
        return msg;
    }

    public String getLoginSubscriptionRequiredText() {
        String msg = context.getString(R.string.app_cms_login_and_subscription_required_title);
        if (localizationResult != null && localizationResult.getLoginSubscriptionDialogTitle() != null)
            msg = localizationResult.getLoginSubscriptionDialogTitle();
        else if (genericMessages != null && genericMessages.getLoginSubscriptionDialogTitle() != null)
            msg = genericMessages.getLoginSubscriptionDialogTitle();
        return msg;
    }

    public String getLoginSubscriptionRequiredMsgText() {
        String msg = context.getString(R.string.app_cms_login_and_subscription_required_message);
        if (localizationResult != null && localizationResult.getLoginSubscriptionDialogMessage() != null)
            msg = localizationResult.getLoginSubscriptionDialogMessage();
        else if (genericMessages != null && genericMessages.getLoginSubscriptionDialogMessage() != null)
            msg = genericMessages.getLoginSubscriptionDialogMessage();
        return msg;
    }

    public String getLoginRequiredText() {
        String msg = context.getString(R.string.app_cms_login_required_title);
        if (localizationResult != null && localizationResult.getLoginRequiredDialogTitle() != null)
            msg = localizationResult.getLoginRequiredDialogTitle();
        else if (genericMessages != null && genericMessages.getLoginRequiredDialogTitle() != null)
            msg = genericMessages.getLoginRequiredDialogTitle();
        return msg;
    }

    public String getLoginRequiredMsgText() {
        String msg = context.getString(R.string.app_cms_login_required_message);
        if (localizationResult != null && localizationResult.getLoginRequiredDialogMessage() != null)
            msg = localizationResult.getLoginRequiredDialogMessage();
        else if (genericMessages != null && genericMessages.getLoginRequiredDialogMessage() != null)
            msg = genericMessages.getLoginRequiredDialogMessage();
        return msg;
    }

    public String getEntitlementLoginErrorMessageText() {
        String msg = context.getString(R.string.app_cms_login_required_message);
        if (localizationResult != null && localizationResult.getEntitlementLoginErrorMessage() != null)
            msg = localizationResult.getEntitlementLoginErrorMessage();
        else if (genericMessages != null && genericMessages.getEntitlementLoginErrorMessage() != null)
            msg = genericMessages.getEntitlementLoginErrorMessage();
        return msg;
    }


    public String getStreamingInfoErrorText() {
        String msg = context.getString(R.string.app_cms_download_stream_info_error_title);
        if (localizationResult != null && localizationResult.getStreamingInfoErrorTitle() != null)
            msg = localizationResult.getStreamingInfoErrorTitle();
        else if (genericMessages != null && genericMessages.getStreamingInfoErrorTitle() != null)
            msg = genericMessages.getStreamingInfoErrorTitle();
        return msg;
    }

    public String getStreamingInfoMsgText() {
        String msg = context.getString(R.string.app_cms_download_streaming_info_error_message);
        if (localizationResult != null && localizationResult.getStreamingInfoErrorMessage() != null)
            msg = localizationResult.getStreamingInfoErrorMessage();
        else if (genericMessages != null && genericMessages.getStreamingInfoErrorMessage() != null)
            msg = genericMessages.getStreamingInfoErrorMessage();
        return msg;
    }

    public String getAppUpdatePrefixText() {
        String msg = context.getString(R.string.app_update_prefix);
        if (localizationResult != null && localizationResult.getAppUpdatePrefix() != null)
            msg = localizationResult.getAppUpdatePrefix();
        else if (genericMessages != null && genericMessages.getAppUpdatePrefix() != null)
            msg = genericMessages.getAppUpdatePrefix();
        return msg;
    }

    public String getAppUpdateSuffixText() {
        String msg = context.getString(R.string.app_update_suffix);
        if (localizationResult != null && localizationResult.getAppUpdateSuffix() != null)
            msg = localizationResult.getAppUpdateSuffix();
        else if (genericMessages != null && genericMessages.getAppUpdateSuffix() != null)
            msg = genericMessages.getAppUpdateSuffix();
        return msg;
    }

    public String getAppUpdateCtaText() {
        String msg = context.getString(R.string.app_cms_upgrade_button_text);
        if (localizationResult != null && localizationResult.getUpdateAppCta() != null)
            msg = localizationResult.getUpdateAppCta();
        else if (genericMessages != null && genericMessages.getUpdateAppCta() != null)
            msg = genericMessages.getUpdateAppCta();
        return msg;
    }

    public String getAppUpdateAvailableText() {
        String msg = context.getString(R.string.app_cms_upgrade_available_text);
        if (localizationResult != null && localizationResult.getUpdateAvailable() != null)
            msg = localizationResult.getUpdateAvailable();
        else if (genericMessages != null && genericMessages.getUpdateAvailable() != null)
            msg = genericMessages.getUpdateAvailable();
        return msg;
    }

    public String getMinText() {
        String msg = context.getString(R.string.min_abbreviation);
        if (localizationResult != null && localizationResult.getMinuteLabel() != null)
            msg = localizationResult.getMinuteLabel();
        else if (genericMessages != null && genericMessages.getMinuteLabel() != null)
            msg = genericMessages.getMinuteLabel();
        return msg;
    }

    public String getMinsText() {
        String msg = context.getString(R.string.mins_abbreviation);
        if (localizationResult != null && localizationResult.getMinutesLabel() != null)
            msg = localizationResult.getMinutesLabel();
        else if (genericMessages != null && genericMessages.getMinutesLabel() != null)
            msg = genericMessages.getMinutesLabel();
        return msg;
    }

    public String getSignInText() {
        String msg = context.getString(R.string.app_cms_signin_error_title);
        if (localizationResult != null && localizationResult.getErrorDialogSignInCta() != null)
            msg = localizationResult.getErrorDialogSignInCta();
        else if (genericMessages != null && genericMessages.getErrorDialogSignInCta() != null)
            msg = genericMessages.getErrorDialogSignInCta();
        return msg;
    }

    public String getSignUpText() {
        String msg = context.getString(R.string.app_cms_sign_up_pager_title);
        if (localizationResult != null && localizationResult.getErrorDialogSignUpCta() != null)
            msg = localizationResult.getErrorDialogSignUpCta();
        else if (genericMessages != null && genericMessages.getErrorDialogSignUpCta() != null)
            msg = genericMessages.getErrorDialogSignUpCta();
        return msg;
    }

    public String getSearchLabelText() {
        String msg = context.getString(R.string.search_hint_text);
        if (localizationResult != null && localizationResult.getSearchHeaderLabel() != null)
            msg = localizationResult.getSearchHeaderLabel();
        else if (genericMessages != null && genericMessages.getSearchHeaderLabel() != null)
            msg = genericMessages.getSearchHeaderLabel();
        return msg;
    }

    public String getGuestUserSubsctiptionMsgText() {
        String msg = context.getString(R.string.subscription_not_purchased);
        if (localizationResult != null && localizationResult.getGuestUserSubscriptionMessage() != null)
            msg = localizationResult.getGuestUserSubscriptionMessage();
        else if (genericMessages != null && genericMessages.getGuestUserSubscriptionMessage() != null)
            msg = genericMessages.getGuestUserSubscriptionMessage();
        else if (localizationResult != null && localizationResult.getGuestUserSubscriptionMsg() != null)
            msg = localizationResult.getGuestUserSubscriptionMsg();
        else if (genericMessages != null && genericMessages.getGuestUserSubscriptionMsg() != null)
            msg = genericMessages.getGuestUserSubscriptionMessage();
        return msg;
    }

    public String getLoadingVideoText() {
        String msg = context.getString(R.string.loading_video_text);
        if (localizationResult != null && localizationResult.getLoadingVideo() != null)
            msg = localizationResult.getLoadingVideo();
        else if (genericMessages != null && genericMessages.getLoadingVideo() != null)
            msg = genericMessages.getLoadingVideo();
        return msg;
    }

    public String getPlayerSettingsUnavailbleText() {
        String msg = context.getString(R.string.no_settings_available);
        if (localizationResult != null && localizationResult.getPlayerSettingsUnavailable() != null)
            msg = localizationResult.getPlayerSettingsUnavailable();
        else if (genericMessages != null && genericMessages.getPlayerSettingsUnavailable() != null)
            msg = genericMessages.getPlayerSettingsUnavailable();
        return msg;
    }

    public String getPlaybackQualityText() {
        String msg = context.getString(R.string.playback_quality);
        if (localizationResult != null && localizationResult.getPlaybackQuality() != null)
            msg = localizationResult.getPlaybackQuality();
        else if (genericMessages != null && genericMessages.getPlaybackQuality() != null)
            msg = genericMessages.getPlaybackQuality();
        return msg;
    }

    public String getClosedCaptionText() {
        String msg = context.getString(R.string.closed_captions);
        if (localizationResult != null && localizationResult.getClosedCaptions() != null)
            msg = localizationResult.getClosedCaptions();
        else if (genericMessages != null && genericMessages.getClosedCaptions() != null)
            msg = genericMessages.getClosedCaptions();
        return msg;
    }

    public String getAudioLanguageText() {
        String msg = context.getString(R.string.audio_language);
        if (localizationResult != null && localizationResult.getAudioLanguage() != null)
            msg = localizationResult.getAudioLanguage();
        else if (genericMessages != null && genericMessages.getAudioLanguage() != null)
            msg = genericMessages.getAudioLanguage();
        return msg;
    }

    public String getSomethingWentWrongText() {
        String msg = context.getString(R.string.something_wrong);
        if (localizationResult != null && localizationResult.getCastErrTitle() != null)
            msg = localizationResult.getCastErrTitle();
        else if (genericMessages != null && genericMessages.getCastErrTitle() != null)
            msg = genericMessages.getCastErrTitle();
        return msg;
    }

    public String getArticleHeaderText() {
        String msg = context.getString(R.string.app_cms_article_title);
        if (localizationResult != null && localizationResult.getArticleTrayHeader() != null)
            msg = localizationResult.getArticleTrayHeader();
        else if (genericMessages != null && genericMessages.getArticleTrayHeader() != null)
            msg = genericMessages.getArticleTrayHeader();
        return msg;
    }

    public String getAudioHeaderText() {
        String msg = context.getString(R.string.app_cms_audio_title);
        if (localizationResult != null && localizationResult.getAudioTrayHeader() != null)
            msg = localizationResult.getAudioTrayHeader();
        else if (genericMessages != null && genericMessages.getAudioTrayHeader() != null)
            msg = genericMessages.getAudioTrayHeader();
        return msg;
    }

    public String getBundleHeaderText() {
        String msg = context.getString(R.string.app_cms_bundle_title);
        if (localizationResult != null && localizationResult.getBundleTrayHeader() != null)
            msg = localizationResult.getBundleTrayHeader();
        else if (genericMessages != null && genericMessages.getBundleTrayHeader() != null)
            msg = genericMessages.getBundleTrayHeader();
        return msg;
    }

    public String getGalleryHeaderText() {
        String msg = context.getString(R.string.app_cms_gallery_title);
        if (localizationResult != null && localizationResult.getGalleryTrayHeader() != null)
            msg = localizationResult.getGalleryTrayHeader();
        else if (genericMessages != null && genericMessages.getGalleryTrayHeader() != null)
            msg = genericMessages.getGalleryTrayHeader();
        return msg;
    }

    public String getBundleSeriesHeaderText() {
        String msg = context.getString(R.string.app_cms_bundle_series_title);
        if (localizationResult != null && localizationResult.getBundleSeriesTrayHeader() != null)
            msg = localizationResult.getBundleSeriesTrayHeader();
        else if (genericMessages != null && genericMessages.getBundleSeriesTrayHeader() != null)
            msg = genericMessages.getBundleSeriesTrayHeader();
        return msg;
    }

    public String getSeriesHeaderText() {
        String msg = context.getString(R.string.app_cms_series_title);
        if (localizationResult != null && localizationResult.getSeriesTrayHeader() != null)
            msg = localizationResult.getSeriesTrayHeader();
        else if (genericMessages != null && genericMessages.getSeriesTrayHeader() != null)
            msg = genericMessages.getSeriesTrayHeader();
        return msg;
    }

    public String getProgramsHeaderText() {
        String msg = context.getString(R.string.app_cms_programs_title);
        if (localizationResult != null && localizationResult.getProgramsTrayHeader() != null)
            msg = localizationResult.getProgramsTrayHeader();
        else if (genericMessages != null && genericMessages.getProgramsTrayHeader() != null)
            msg = genericMessages.getProgramsTrayHeader();
        return msg;
    }

    public String getEpisodesHeaderText() {
        String msg = context.getString(R.string.runtime_episodes_abbreviation);
        if (localizationResult != null && localizationResult.getEpisodesTrayHeader() != null)
            msg = localizationResult.getEpisodesTrayHeader();
        else if (genericMessages != null && genericMessages.getEpisodesTrayHeader() != null)
            msg = genericMessages.getEpisodesTrayHeader();
        return msg;
    }

    public String getEpisodeText() {
        String msg = context.getString(R.string.episode);
        if (localizationResult != null && localizationResult.getEpisodeLabel() != null)
            msg = localizationResult.getEpisodeLabel();
        else if (genericMessages != null && genericMessages.getEpisodeLabel() != null)
            msg = genericMessages.getEpisodeLabel();
        return msg;
    }

    public String getSongText() {
        String msg = context.getString(R.string.episode);
        if (localizationResult != null && localizationResult.getSong() != null)
            msg = localizationResult.getSong();
        else if (genericMessages != null && genericMessages.getSong() != null)
            msg = genericMessages.getSong();
        return msg;
    }

    public String getVideosHeaderText() {
        String msg = context.getString(R.string.app_cms_video_title);
        if (localizationResult != null && localizationResult.getVideosTrayHeader() != null)
            msg = localizationResult.getVideosTrayHeader();
        else if (genericMessages != null && genericMessages.getVideosTrayHeader() != null)
            msg = genericMessages.getVideosTrayHeader();
        return msg;
    }

    public String getPlayersHeaderText() {
        String msg = context.getString(R.string.app_cms_player_title);
        if (localizationResult != null && localizationResult.getPlayersTrayHeader() != null)
            msg = localizationResult.getPlayersTrayHeader();
        else if (genericMessages != null && genericMessages.getPlayersTrayHeader() != null)
            msg = genericMessages.getPlayersTrayHeader();
        return msg;
    }

    public String getVideoPlaylistHeaderText() {
        String msg = context.getString(R.string.app_cms_video_playlist_title);
        if (localizationResult != null && localizationResult.getVideoplaylistTrayHeader() != null)
            msg = localizationResult.getVideoplaylistTrayHeader();
        else if (genericMessages != null && genericMessages.getVideoplaylistTrayHeader() != null)
            msg = genericMessages.getVideoplaylistTrayHeader();
        return msg;
    }

    public String getGoText() {
        String msg = context.getString(R.string.search_button_text);
        if (localizationResult != null && localizationResult.getGoCta() != null)
            msg = localizationResult.getGoCta();
        else if (genericMessages != null && genericMessages.getGoCta() != null)
            msg = genericMessages.getGoCta();
        return msg;
    }

    public String getSongsHeaderText() {
        String msg = context.getString(R.string.songs_abbreviation);
        if (localizationResult != null && localizationResult.getSongsHeader() != null)
            msg = localizationResult.getSongsHeader();
        else if (genericMessages != null && genericMessages.getSongsHeader() != null)
            msg = genericMessages.getSongsHeader();
        return msg;
    }

    public String getSecText() {
        String msg = context.getString(R.string.sec_abbreviation);
        if (localizationResult != null && localizationResult.getSecondLabel() != null)
            msg = localizationResult.getSecondLabel();
        else if (genericMessages != null && genericMessages.getSecondLabel() != null)
            msg = genericMessages.getSecondLabel();
        return msg;
    }

    public String getSecsText() {
        String msg = context.getString(R.string.secs_abbreviation);
        if (localizationResult != null && localizationResult.getSecondsLabel() != null)
            msg = localizationResult.getSecondsLabel();
        else if (genericMessages != null && genericMessages.getSecondsLabel() != null)
            msg = genericMessages.getSecondsLabel();
        return msg;
    }

    public String getItemQueueUnavailableText() {
        String msg = context.getString(R.string.not_item_available_inqueue);
        if (localizationResult != null && localizationResult.getItemQueueUnavailable() != null)
            msg = localizationResult.getItemQueueUnavailable();
        else if (genericMessages != null && genericMessages.getItemQueueUnavailable() != null)
            msg = genericMessages.getItemQueueUnavailable();
        return msg;
    }

    public String getAddToDownloadQueueText() {
        String msg = context.getString(R.string.add_to_download_queue);
        if (localizationResult != null && localizationResult.getAddToDownloadQueue() != null)
            msg = localizationResult.getAddToDownloadQueue();
        else if (genericMessages != null && genericMessages.getAddToDownloadQueue() != null)
            msg = genericMessages.getAddToDownloadQueue();
        return msg;
    }

    public String getVideoQualitiesText() {
        String msg = context.getString(R.string.offline_video_qualities);
        if (localizationResult != null && localizationResult.getFetchVideoQualities() != null)
            msg = localizationResult.getFetchVideoQualities();
        else if (genericMessages != null && genericMessages.getFetchVideoQualities() != null)
            msg = genericMessages.getFetchVideoQualities();
        return msg;
    }

    public String getEmptySearchText() {
        String msg = context.getString(R.string.search_blank_toast_msg);
        if (localizationResult != null && localizationResult.getEmptySearchField() != null)
            msg = localizationResult.getEmptySearchField();
        else if (genericMessages != null && genericMessages.getEmptySearchField() != null)
            msg = genericMessages.getEmptySearchField();
        return msg;
    }

    public String getDownloadStartedText(String title) {
        String msg = context.getString(R.string.app_cms_download_started_message, title);
        if (localizationResult != null && localizationResult.getDownloadStarted() != null)
            msg = title + " " + localizationResult.getDownloadStarted();
        else if (genericMessages != null && genericMessages.getDownloadStarted() != null)
            msg = title + " " + genericMessages.getDownloadStarted();
        return msg;
    }

    public String getAlreadyDownloadedText(String title) {
        String msg = context.getString(R.string.app_cms_download_available_already_message, title);
        if (localizationResult != null && localizationResult.getAlreadyDownloaded() != null)
            msg = title + " " + localizationResult.getAlreadyDownloaded();
        else if (genericMessages != null && genericMessages.getAlreadyDownloaded() != null)
            msg = title + " " + genericMessages.getAlreadyDownloaded();
        return msg;
    }

    public String getVideoNotloadedText() {
        String msg = context.getString(R.string.app_cms_video_not_available_error_message);
        if (localizationResult != null && localizationResult.getVideoNotLoaded() != null)
            msg = localizationResult.getVideoNotLoaded();
        else if (genericMessages != null && genericMessages.getVideoNotLoaded() != null)
            msg = genericMessages.getVideoNotLoaded();
        return msg;
    }

    public String getPlaybackErrorText() {
        String msg = context.getString(R.string.playback_error_message);
        if (localizationResult != null && localizationResult.getMediaSessionError() != null)
            msg = localizationResult.getMediaSessionError();
        else if (genericMessages != null && genericMessages.getMediaSessionError() != null)
            msg = genericMessages.getMediaSessionError();
        return msg;
    }

    public String getPublishedText() {
        String msg = context.getString(R.string.published_on);
        if (localizationResult != null && localizationResult.getPublishedOn() != null)
            msg = localizationResult.getPublishedOn();
        else if (genericMessages != null && genericMessages.getPublishedOn() != null)
            msg = genericMessages.getPublishedOn();
        return msg;
    }

    public String getNotPurchasedText() {
        String msg = context.getString(R.string.empty_library_message);
        if (localizationResult != null && localizationResult.getNotPurchased() != null)
            msg = localizationResult.getNotPurchased();
        else if (genericMessages != null && genericMessages.getNotPurchased() != null)
            msg = genericMessages.getNotPurchased();
        return msg;
    }

    public String getAutoHlsText() {
        String msg = context.getString(R.string.auto);
        if (localizationResult != null && localizationResult.getAutoHlsResolution() != null)
            msg = localizationResult.getAutoHlsResolution();
        else if (genericMessages != null && genericMessages.getAutoHlsResolution() != null)
            msg = genericMessages.getAutoHlsResolution();
        return msg;
    }

    public String getCastingToText() {
        String msg = context.getString(R.string.casting_to_device);
        if (localizationResult != null && localizationResult.getCastingTo() != null)
            msg = localizationResult.getCastingTo();
        else if (genericMessages != null && genericMessages.getCastingTo() != null)
            msg = genericMessages.getCastingTo();
        return msg;
    }

    public String getEncourageUserLoginText() {
        String msg = context.getString(R.string.encourage_user_to_login);
        if (localizationResult != null && localizationResult.getEncourageUserToLoginLabel() != null)
            msg = localizationResult.getEncourageUserToLoginLabel();
        else if (genericMessages != null && genericMessages.getEncourageUserToLoginLabel() != null)
            msg = genericMessages.getEncourageUserToLoginLabel();
        return msg;
    }

    public String getMaxStreamErrorText() {
        String msg = context.getString(R.string.max_stream_error_text);
        if (localizationResult != null && localizationResult.getMaxStreamError() != null)
            msg = localizationResult.getMaxStreamError();
        else if (genericMessages != null && genericMessages.getMaxStreamError() != null)
            msg = genericMessages.getMaxStreamError();
        return msg;
    }

    public String getGuestTveLoginMessageText() {
        String msg = context.getString(R.string.guest_tve_login_message);
        if (localizationResult != null && localizationResult.getWaysToWatchMessageForTVEGuestUser() != null)
            msg = localizationResult.getWaysToWatchMessageForTVEGuestUser();
        else if (genericMessages != null && genericMessages.getWaysToWatchMessageForTVEGuestUser() != null)
            msg = genericMessages.getWaysToWatchMessageForTVEGuestUser();
        return msg;
    }

    public String getNoSearchResultText() {
        String msg = context.getString(R.string.no_results);
        if (localizationResult != null && localizationResult.getNoResult() != null)
            msg = localizationResult.getNoResult();
        else if (genericMessages != null && genericMessages.getNoResult() != null)
            msg = genericMessages.getNoResult();
        return msg;
    }

    public String getNoPreviousSongText() {
        String msg = context.getString(R.string.no_previous_audio);
        if (localizationResult != null && localizationResult.getNoPreviousAudio() != null)
            msg = localizationResult.getNoPreviousAudio();
        else if (genericMessages != null && genericMessages.getNoPreviousAudio() != null)
            msg = genericMessages.getNoPreviousAudio();
        return msg;
    }

    public String getNoNextSongText() {
        String msg = context.getString(R.string.no_next_audio);
        if (localizationResult != null && localizationResult.getNoNextAudio() != null)
            msg = localizationResult.getNoNextAudio();
        else if (genericMessages != null && genericMessages.getNoNextAudio() != null)
            msg = genericMessages.getNoNextAudio();
        return msg;
    }

    public String getLiveStreamingText() {
        String msg = context.getString(R.string.live_streaming);
        if (localizationResult != null && localizationResult.getLiveStreaming() != null)
            msg = localizationResult.getLiveStreaming();
        else if (genericMessages != null && genericMessages.getLiveStreaming() != null)
            msg = genericMessages.getLiveStreaming();
        return msg;
    }

    public String getPremiumContentGuestUserDialogMessageText() {
        String msg = context.getString(R.string.unsubscribe_text_with_login);
        if (localizationResult != null && localizationResult.getPremiumContentGuestUserDialogMessage() != null)
            msg = localizationResult.getPremiumContentGuestUserDialogMessage();
        else if (genericMessages != null && genericMessages.getPremiumContentGuestUserDialogMessage() != null)
            msg = genericMessages.getPremiumContentGuestUserDialogMessage();
        return msg;
    }

    public String getAutoPlayoffMessageText() {
        String msg = context.getString(R.string.autoplay_off_msg);
        if (localizationResult != null && localizationResult.getAutoPlayoffMessage() != null)
            msg = localizationResult.getAutoPlayoffMessage();
        else if (genericMessages != null && genericMessages.getAutoPlayoffMessage() != null)
            msg = genericMessages.getAutoPlayoffMessage();
        return msg;
    }

    public String getNoVideoInQueueText() {
        String msg = context.getString(R.string.app_cms_video_ended_text_message);
        if (localizationResult != null && localizationResult.getNoVideoInQueue() != null)
            msg = localizationResult.getNoVideoInQueue();
        else if (genericMessages != null && genericMessages.getNoVideoInQueue() != null)
            msg = genericMessages.getNoVideoInQueue();
        return msg;
    }

    public String getCastMsgPrefixText() {
        String msg = context.getString(R.string.app_cms_cast_msg_prefix);
        if (localizationResult != null && localizationResult.getCastMsgPrefix() != null)
            msg = localizationResult.getCastMsgPrefix();
        else if (genericMessages != null && genericMessages.getCastMsgPrefix() != null)
            msg = genericMessages.getCastMsgPrefix();
        return msg;
    }

    public String getCastMsgSuffixText() {
        String msg = context.getString(R.string.app_cms_cast_msg_to);
        if (localizationResult != null && localizationResult.getCastMsgSuffix() != null)
            msg = localizationResult.getCastMsgSuffix();
        else if (genericMessages != null && genericMessages.getCastMsgSuffix() != null)
            msg = genericMessages.getCastMsgSuffix();
        return msg;
    }

    public String getTouchToCastMsgText() {
        String msg = context.getString(R.string.app_cms_touch_to_cast_msg);
        if (localizationResult != null && localizationResult.getTouchToCastMsg() != null)
            msg = localizationResult.getTouchToCastMsg();
        else if (genericMessages != null && genericMessages.getTouchToCastMsg() != null)
            msg = genericMessages.getTouchToCastMsg();
        return msg;
    }

    public String getUnsubscribedDownloadMsgText() {
        String msg = context.getString(R.string.unsubscribe_text_with_subscribe_and_login);
        if (localizationResult != null && localizationResult.getEntitlementErrorMessageForDownload() != null)
            msg = localizationResult.getEntitlementErrorMessageForDownload();
        else if (genericMessages != null && genericMessages.getEntitlementErrorMessageForDownload() != null)
            msg = genericMessages.getEntitlementErrorMessageForDownload();
        return msg;
    }

    public String getAlreadyDownloadedOtherUserText(String title) {
        String msg = context.getString(R.string.app_cms_download_available_already_message_other_user, title);
        if (localizationResult != null && localizationResult.getAlreadyDownloadedOtherUser() != null)
            msg = title + " " + localizationResult.getAlreadyDownloadedOtherUser();
        else if (genericMessages != null && genericMessages.getAlreadyDownloadedOtherUser() != null)
            msg = title + " " + genericMessages.getAlreadyDownloadedOtherUser();
        return msg;
    }

    public String getUserOnlineTimeAlertText() {
        String msg = context.getString(R.string.app_cms_download_limit_message_30);
        if (localizationResult != null && localizationResult.getUserOnlineTimeAlert() != null)
            msg = localizationResult.getUserOnlineTimeAlert();
        else if (genericMessages != null && genericMessages.getUserOnlineTimeAlert() != null)
            msg = genericMessages.getUserOnlineTimeAlert();
        return msg;
    }

    public String getMoreLabelText() {
        String msg = context.getString(R.string.more);
        if (localizationResult != null && localizationResult.getMoreLabel() != null)
            msg = localizationResult.getMoreLabel();
        else if (genericMessages != null && genericMessages.getMoreLabel() != null)
            msg = genericMessages.getMoreLabel();
        return msg;
    }

    public String getPhotoText() {
        String msg = context.getString(R.string.photo);
        if (localizationResult != null && localizationResult.getPhoto() != null)
            msg = localizationResult.getPhoto();
        else if (genericMessages != null && genericMessages.getPhoto() != null)
            msg = genericMessages.getPhoto();
        return " " + msg;
    }

    public String getPhotosText() {
        String msg = context.getString(R.string.photos);
        if (localizationResult != null && localizationResult.getPhotos() != null)
            msg = localizationResult.getPhotos();
        else if (genericMessages != null && genericMessages.getPhotos() != null)
            msg = genericMessages.getPhotos();
        return " " + msg;
    }

    public String getByText() {
        String msg = context.getString(R.string.photo_gallery_by);
        if (localizationResult != null && localizationResult.getBy() != null)
            msg = localizationResult.getBy();
        else if (genericMessages != null && genericMessages.getBy() != null)
            msg = genericMessages.getBy();
        return msg + " ";
    }

    public String getNoFightFoundText() {
        String msg = context.getString(R.string.no_fight_records);
        if (localizationResult != null && localizationResult.getNoFight() != null)
            msg = localizationResult.getNoFight();
        else if (genericMessages != null && genericMessages.getNoFight() != null)
            msg = genericMessages.getNoFight();
        return msg;
    }

    public String getFaceOffText() {
        String msg = context.getString(R.string.timer_until_face_off);
        if (localizationResult != null && localizationResult.getFaceOff() != null)
            msg = localizationResult.getFaceOff();
        else if (genericMessages != null && genericMessages.getFaceOff() != null)
            msg = genericMessages.getFaceOff();
        return msg;
    }

    public String getContentAvailableText() {
        String msg = context.getString(R.string.timer_until_pay_per_view_event);
        if (localizationResult != null && localizationResult.getTimerLabel() != null)
            msg = localizationResult.getTimerLabel();
        else if (genericMessages != null && genericMessages.getTimerLabel() != null)
            msg = genericMessages.getTimerLabel();
        return msg;
    }

    public String getRemoveFromWatchlistText() {
        String msg = context.getString(R.string.remove_from_watchlist);
        if (localizationResult != null && localizationResult.getRemoveFromWatchlist() != null)
            msg = localizationResult.getRemoveFromWatchlist();
        else if (genericMessages != null && genericMessages.getRemoveFromWatchlist() != null)
            msg = genericMessages.getRemoveFromWatchlist();
        return msg;
    }

    public String getAddToWatchlistText() {
        String msg = context.getString(R.string.add_to_watchlist);
        if (localizationResult != null && localizationResult.getAddToWatchlist() != null)
            msg = localizationResult.getAddToWatchlist();
        else if (genericMessages != null && genericMessages.getAddToWatchlist() != null)
            msg = genericMessages.getAddToWatchlist();
        return msg;
    }

    public String getAddToSaveText() {
        String msg = context.getString(R.string.add_to_saved);
        if (localizationResult != null && localizationResult.getAddToSaved() != null)
            msg = localizationResult.getAddToSaved();
        else if (genericMessages != null && genericMessages.getAddToSaved() != null)
            msg = genericMessages.getAddToSaved();
        return msg;
    }

    public String getRemoveFromSaveText() {
        String msg = context.getString(R.string.remove_from_saved);
        if (localizationResult != null && localizationResult.getRemoveFromSaved() != null)
            msg = localizationResult.getRemoveFromSaved();
        else if (genericMessages != null && genericMessages.getRemoveFromSaved() != null)
            msg = genericMessages.getRemoveFromSaved();
        return msg;
    }

    public String getCheckExistingSubscriptionText() {
        String msg = context.getString(R.string.checking_for_existing_subscription_toast_message);
        if (localizationResult != null && localizationResult.getCheckExistingSubscription() != null)
            msg = localizationResult.getCheckExistingSubscription();
        else if (genericMessages != null && genericMessages.getCheckExistingSubscription() != null)
            msg = genericMessages.getCheckExistingSubscription();
        return msg;
    }

    public String getExistingSubscriptionDoesNotExistText() {
        String msg = context.getString(R.string.existing_subscription_does_not_exist_toast_message);
        if (localizationResult != null && localizationResult.getExistingSubscriptionDoesNotExist() != null)
            msg = localizationResult.getExistingSubscriptionDoesNotExist();
        else if (genericMessages != null && genericMessages.getExistingSubscriptionDoesNotExist() != null)
            msg = genericMessages.getExistingSubscriptionDoesNotExist();
        return msg;
    }

    public String getExistingUserLoginText() {
        String msg = context.getString(R.string.logging_in_using_existing_subscription);
        if (localizationResult != null && localizationResult.getExistingUserLogin() != null)
            msg = localizationResult.getExistingUserLogin();
        else if (genericMessages != null && genericMessages.getExistingUserLogin() != null)
            msg = genericMessages.getExistingUserLogin();
        return msg;
    }

    public String getBillingResponseErrorText() {
        String msg = context.getString(R.string.subscription_billing_response_result_error);
        if (localizationResult != null && localizationResult.getBillingResponseError() != null)
            msg = localizationResult.getBillingResponseError();
        else if (genericMessages != null && genericMessages.getBillingResponseError() != null)
            msg = genericMessages.getBillingResponseError();
        return msg;
    }

    public String getClosedCaptionOffText() {
        String msg = context.getString(R.string.closed_caption_off);
        if (localizationResult != null && localizationResult.getOffLabel() != null)
            msg = localizationResult.getOffLabel();
        else if (genericMessages != null && genericMessages.getOffLabel() != null)
            msg = genericMessages.getOffLabel();
        return msg;
    }

    public String getDownloadSpaceDialogTitleText() {
        String msg = context.getString(R.string.app_cms_download_failed_error_title);
        if (localizationResult != null && localizationResult.getDownloadSpaceDialogTitle() != null)
            msg = localizationResult.getDownloadSpaceDialogTitle();
        else if (genericMessages != null && genericMessages.getDownloadSpaceDialogTitle() != null)
            msg = genericMessages.getDownloadSpaceDialogTitle();
        return msg;
    }

    public String getManageCtaText() {
        String msg = context.getString(R.string.settings_manage_subscription_text);
        if (localizationResult != null && localizationResult.getManageCta() != null)
            msg = localizationResult.getManageCta();
        else if (genericMessages != null && genericMessages.getManageCta() != null)
            msg = genericMessages.getManageCta();
        return msg;
    }

    public String getDownloadedLabelText() {
        String msg = context.getString(R.string.downloaded);
        if (localizationResult != null && localizationResult.getDownloadedLabel() != null)
            msg = localizationResult.getDownloadedLabel();
        else if (genericMessages != null && genericMessages.getDownloadedLabel() != null)
            msg = genericMessages.getDownloadedLabel();
        return msg;
    }

    public String getDownloadLowerCaseText() {
        String msg = context.getString(R.string.download_title);
        if (localizationResult != null && localizationResult.getDownloadLowerCase() != null)
            msg = localizationResult.getDownloadLowerCase();
        else if (genericMessages != null && genericMessages.getDownloadLowerCase() != null)
            msg = genericMessages.getDownloadLowerCase();
        return msg;
    }

    public String getDownloadingLabelText() {
        String msg = context.getString(R.string.downloading);
        if (localizationResult != null && localizationResult.getDownloadingLabel() != null)
            msg = localizationResult.getDownloadingLabel();
        else if (genericMessages != null && genericMessages.getDownloadingLabel() != null)
            msg = genericMessages.getDownloadingLabel();
        return msg;
    }

    public String getRemovedFromWatchlistText() {
        String msg = context.getString(R.string.removed_from_watchlist);
        if (localizationResult != null && localizationResult.getRemovedFromWatchlist() != null)
            msg = localizationResult.getRemovedFromWatchlist();
        else if (genericMessages != null && genericMessages.getRemovedFromWatchlist() != null)
            msg = genericMessages.getRemovedFromWatchlist();
        return msg;
    }

    public String getAddedToWatchlistLabelText() {
        String msg = context.getString(R.string.added_to_watchlist);
        if (localizationResult != null && localizationResult.getAddedToWatchlistLabel() != null)
            msg = localizationResult.getAddedToWatchlistLabel();
        else if (genericMessages != null && genericMessages.getAddedToWatchlistLabel() != null)
            msg = genericMessages.getAddedToWatchlistLabel();
        return msg;
    }

    public String getFailedToAddToWatchlistText() {
        String msg = context.getString(R.string.failed_to_add_to_watchlist);
        if (localizationResult != null && localizationResult.getFailedToAddToWatchlist() != null)
            msg = localizationResult.getFailedToAddToWatchlist();
        else if (genericMessages != null && genericMessages.getFailedToAddToWatchlist() != null)
            msg = genericMessages.getFailedToAddToWatchlist();
        return msg;
    }

    public String getFailedToRemoveFromWatchlistText() {
        String msg = context.getString(R.string.failed_to_remove_from_watchlist);
        if (localizationResult != null && localizationResult.getFailedToRemoveFromWatchlist() != null)
            msg = localizationResult.getFailedToRemoveFromWatchlist();
        else if (genericMessages != null && genericMessages.getFailedToRemoveFromWatchlist() != null)
            msg = genericMessages.getFailedToRemoveFromWatchlist();
        return msg;
    }

    public String getSeasonsLabelText() {
        String msg = context.getString(R.string.seasons);
        if (localizationResult != null && localizationResult.getSeasonsLabel() != null)
            msg = localizationResult.getSeasonsLabel();
        else if (genericMessages != null && genericMessages.getSeasonsLabel() != null)
            msg = genericMessages.getSeasonsLabel();
        return msg;
    }

    public String getFailText() {
        String msg = context.getString(R.string.please_try_again);
        if (localizationResult != null && localizationResult.getFailMessage() != null)
            msg = localizationResult.getFailMessage();
        else if (genericMessages != null && genericMessages.getFailMessage() != null)
            msg = genericMessages.getFailMessage();
        return msg;
    }

    public String getRetryButtonText() {
        String msg = context.getString(R.string.retry_button_text);
        if (localizationResult != null && localizationResult.getRetryButtonTitle() != null)
            msg = localizationResult.getRetryButtonTitle();
        else if (genericMessages != null && genericMessages.getRetryButtonTitle() != null)
            msg = genericMessages.getRetryButtonTitle();
        return msg;
    }

    public String getBackToHomeButtonText() {
        String msg = context.getString(R.string.backtohome_button_text);
        if (localizationResult != null && localizationResult.getBackToHomeButton() != null)
            msg = localizationResult.getBackToHomeButton();
        else if (genericMessages != null && genericMessages.getBackToHomeButton() != null)
            msg = genericMessages.getBackToHomeButton();
        return msg;
    }




    public String getSecondText() {
        String msg = "";
        if (localizationResult != null && localizationResult.getSecondLabelFull() != null)
            msg = localizationResult.getSecondLabelFull();
        else if (genericMessages != null && genericMessages.getSecondLabelFull() != null)
            msg = genericMessages.getSecondLabelFull();
        return msg;
    }

    public String getSecondsText() {
        String msg = "";
        if (localizationResult != null && localizationResult.getSecondsLabelFull() != null)
            msg = localizationResult.getSecondsLabelFull();
        else if (genericMessages != null && genericMessages.getSecondsLabelFull() != null)
            msg = genericMessages.getSecondsLabelFull();
        return msg;
    }

    public String getSuccessMessageTitleText() {
        String msg = context.getString(R.string.success_message_title);
        if (localizationResult != null && localizationResult.getSuccessMessageTitle() != null)
            msg = localizationResult.getSuccessMessageTitle();
        else if (genericMessages != null && genericMessages.getSuccessMessageTitle() != null)
            msg = genericMessages.getSuccessMessageTitle();
        return msg;
    }

    public String getPendingMessageTitleText() {
        String msg = context.getString(R.string.pending_message_title);
        if (localizationResult != null && localizationResult.getPendingMessageTitle() != null)
            msg = localizationResult.getPendingMessageTitle();
        else if (genericMessages != null && genericMessages.getPendingMessageTitle() != null)
            msg = genericMessages.getPendingMessageTitle();
        return msg;
    }

    public String getFailMessageTitleText() {
        String msg = context.getString(R.string.fail_message_title);
        if (localizationResult != null && localizationResult.getFailMessageTitle() != null)
            msg = localizationResult.getFailMessageTitle();
        else if (genericMessages != null && genericMessages.getFailMessageTitle() != null)
            msg = genericMessages.getFailMessageTitle();
        return msg;
    }

    public String getToPayText() {
        String msg = context.getString(R.string.to_pay_title_text);
        if (localizationResult != null && localizationResult.getToPayTitleText() != null)
            msg = localizationResult.getToPayTitleText();
        else if (genericMessages != null && genericMessages.getToPayTitleText() != null)
            msg = genericMessages.getToPayTitleText();
        return msg;
    }

    public String getSuccessText() {
        String msg = context.getString(R.string.success_message);
        if (localizationResult != null && localizationResult.getSuccessMessage() != null)
            msg = localizationResult.getSuccessMessage();
        else if (genericMessages != null && genericMessages.getSuccessMessage() != null)
            msg = genericMessages.getSuccessMessage();
        return msg;
    }

    public String getPendingText() {
        String msg = context.getString(R.string.pending_message);
        if (localizationResult != null && localizationResult.getPendingMessage() != null)
            msg = localizationResult.getPendingMessage();
        else if (genericMessages != null && genericMessages.getPendingMessage() != null)
            msg = genericMessages.getPendingMessage();
        return msg;
    }

    public String getSeasonLabelText() {
        String msg = context.getString(R.string.season);
        if (localizationResult != null && localizationResult.getSeasonLabel() != null)
            msg = localizationResult.getSeasonLabel();
        else if (genericMessages != null && genericMessages.getSeasonLabel() != null)
            msg = genericMessages.getSeasonLabel();
        return msg;
    }

    public String getWebSubscriptionMessagePrefixText() {
        String msg = context.getString(R.string.web_subscription_msg_prefix);
        if (localizationResult != null && localizationResult.getWebSubscriptionMessagePrefix() != null)
            msg = localizationResult.getWebSubscriptionMessagePrefix();
        else if (genericMessages != null && genericMessages.getWebSubscriptionMessagePrefix() != null)
            msg = genericMessages.getWebSubscriptionMessagePrefix();
        return msg; //+ " " + context.getString(R.string.web_view_plans_path, domain);
    }

    public String getWebSubscriptionMessageSuffixText() {
        String msg = context.getString(R.string.web_subscription_msg_suffix);
        if (localizationResult != null && localizationResult.getWebSubscriptionMessageSuffix() != null)
            msg = localizationResult.getWebSubscriptionMessageSuffix();
        else if (genericMessages != null && genericMessages.getWebSubscriptionMessageSuffix() != null)
            msg = genericMessages.getWebSubscriptionMessageSuffix();
        return msg; //+ " " + context.getString(R.string.web_view_plans_path, domain);
    }

    public String getSubscriptionLeftText() {
        String msg = context.getString(R.string.subscriptionLeftMessage);
        if (localizationResult != null && localizationResult.getSubscriptionLeftMessage() != null)
            msg = localizationResult.getSubscriptionLeftMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionLeftMessage() != null)
            msg = genericMessages.getSubscriptionLeftMessage();
        return msg;
    }

    public String getSubscriptionExpiredText() {
        String msg = context.getString(R.string.subscriptionExpiredMessage);
        if (localizationResult != null && localizationResult.getSubscriptionExpiredMessage() != null)
            msg = localizationResult.getSubscriptionExpiredMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionExpiredMessage() != null)
            msg = genericMessages.getSubscriptionExpiredMessage();
        return msg;
    }

    public String getSubscriptionInitiatedText() {
        String msg = context.getString(R.string.subscriptionInitiatedMessage);
        if (localizationResult != null && localizationResult.getSubscriptionInitiatedMessage() != null)
            msg = localizationResult.getSubscriptionInitiatedMessage();
        else if (genericMessages != null && genericMessages.getSubscriptionInitiatedMessage() != null)
            msg = genericMessages.getSubscriptionInitiatedMessage();
        return msg;
    }

    public String getExternalStorageTitleText() {
        String msg = context.getString(R.string.app_cms_download_external_storage_write_permission_info_error_title);
        if (localizationResult != null && localizationResult.getDownloadExternalStorageTitle() != null)
            msg = localizationResult.getDownloadExternalStorageTitle();
        else if (genericMessages != null && genericMessages.getDownloadExternalStorageTitle() != null)
            msg = genericMessages.getDownloadExternalStorageTitle();
        return msg;
    }

    public String getDownloadCellularTitleText() {
        String msg = context.getString(R.string.app_cms_download_over_cellular_disabled_error_title);
        if (localizationResult != null && localizationResult.getCellularDisabledErrorTitle() != null)
            msg = localizationResult.getCellularDisabledErrorTitle();
        else if (genericMessages != null && genericMessages.getCellularDisabledErrorTitle() != null)
            msg = genericMessages.getCellularDisabledErrorTitle();
        return msg;
    }

    public String getPasswordFormatValidationText() {
        String msg = context.getString(R.string.password_space_error);
        if (localizationResult != null && localizationResult.getPasswordFormatValidationMessage() != null)
            msg = localizationResult.getPasswordFormatValidationMessage();
        else if (genericMessages != null && genericMessages.getPasswordFormatValidationMessage() != null)
            msg = genericMessages.getPasswordFormatValidationMessage();
        return msg;
    }

    public String getTVODContentError(String domain) {
        String msg = context.getString(R.string.cannot_purchase_item_msg, domain);
        if (localizationResult != null && localizationResult.getTvodContentERRORPrefix() != null)
            msg = localizationResult.getTvodContentERRORPrefix()
                    + " " + domain + " "
                    + localizationResult.getTvodContentERRORSuffix();
        else if (genericMessages != null && genericMessages.getTvodContentERRORPrefix() != null)
            msg = genericMessages.getTvodContentERRORPrefix()
                    + " " + domain + " "
                    + genericMessages.getTvodContentERRORSuffix();
        return msg;
    }

    public String getSaveText() {
        String msg = context.getString(R.string.save);
        if (localizationResult != null && localizationResult.getRecommendSaveButtonText() != null)
            msg = localizationResult.getRecommendSaveButtonText();
        else if (genericMessages != null && genericMessages.getRecommendSaveButtonText() != null)
            msg = genericMessages.getRecommendSaveButtonText();
        return msg;
    }


    public String getSkipText() {
        String msg = context.getString(R.string.skip);
        if (localizationResult != null && localizationResult.getRecommendSkipButtonText() != null)
            msg = localizationResult.getRecommendSkipButtonText();
        else if (genericMessages != null && genericMessages.getRecommendSkipButtonText() != null)
            msg = genericMessages.getRecommendSkipButtonText();
        return msg;
    }

    public String getStartBrowsingText() {
        String msg = context.getString(R.string.start_browsing);
        if (localizationResult != null && localizationResult.getRecommendStartBrowsingText() != null)
            msg = localizationResult.getRecommendStartBrowsingText();
        else if (genericMessages != null && genericMessages.getRecommendStartBrowsingText() != null)
            msg = genericMessages.getRecommendStartBrowsingText();
        return msg;
    }

    public String getRecommendationTitle() {
        String msg = context.getString(R.string.user_personlization_title);
        if (localizationResult != null && localizationResult.getRecommendationTitle() != null)
            msg = localizationResult.getRecommendationTitle();
        else if (genericMessages != null && genericMessages.getRecommendationTitle() != null)
            msg = genericMessages.getRecommendationTitle();
        return msg;
    }

    public String getRecommendationSubTitle() {
        String msg = context.getString(R.string.user_personlization_sub_title);
        if (localizationResult != null && localizationResult.getRecommendationSubTitle() != null)
            msg = localizationResult.getRecommendationSubTitle();
        else if (genericMessages != null && genericMessages.getRecommendationSubTitle() != null)
            msg = genericMessages.getRecommendationSubTitle();
        return msg;
    }

    public String getEmptyGenerListMessage() {
        String msg = "Please select a genre!";
        if (localizationResult != null && localizationResult.getRecommendationSelectionEmptyMessage() != null)
            msg = localizationResult.getRecommendationSelectionEmptyMessage();
        else if (genericMessages != null && genericMessages.getRecommendationSelectionEmptyMessage() != null)
            msg = genericMessages.getRecommendationSelectionEmptyMessage();
        return msg;
    }

    public String getPersonalizeSettingsHeader() {
        String msg = context.getString(R.string.personalize_settings_header);
        if (localizationResult != null && localizationResult.getPersonalizeSettingsHeader() != null)
            msg = localizationResult.getPersonalizeSettingsHeader();
        else if (genericMessages != null && genericMessages.getPersonalizeSettingsHeader() != null)
            msg = genericMessages.getPersonalizeSettingsHeader();
        return msg;
    }

    public String getManagePersonalizationText() {
        String msg = context.getString(R.string.manage_personalization);
        if (localizationResult != null && localizationResult.getManagePersonalization() != null)
            msg = localizationResult.getManagePersonalization();
        else if (genericMessages != null && genericMessages.getManagePersonalization() != null)
            msg = genericMessages.getManagePersonalization();
        return msg;
    }

    public String getViewingRestrictionsEnabledText() {
        String msg = context.getString(R.string.viewing_restrictions_enabled);
        if (localizationResult != null && localizationResult.getViewingRestrictionsEnabled() != null)
            msg = localizationResult.getViewingRestrictionsEnabled();
        else if (genericMessages != null && genericMessages.getViewingRestrictionsEnabled() != null)
            msg = genericMessages.getViewingRestrictionsEnabled();
        return msg;
    }

    public String getEnterVideoPinText() {
        String msg = context.getString(R.string.enter_video_pin);
        if (localizationResult != null && localizationResult.getEnterVideoPin() != null)
            msg = localizationResult.getEnterVideoPin();
        else if (genericMessages != null && genericMessages.getEnterVideoPin() != null)
            msg = genericMessages.getEnterVideoPin();
        return msg;
    }

    public String getInCorrectPinMessageText() {
        String msg = context.getString(R.string.in_correct_pin_message);
        if (localizationResult != null && localizationResult.getInCorrectPinMessage() != null)
            msg = localizationResult.getInCorrectPinMessage();
        else if (genericMessages != null && genericMessages.getInCorrectPinMessage() != null)
            msg = genericMessages.getInCorrectPinMessage();
        return msg;
    }

    public String getConfirmCTAText() {
        String msg = context.getString(R.string.confirm);
        if (localizationResult != null && localizationResult.getConfirmCTA() != null)
            msg = localizationResult.getConfirmCTA();
        else if (genericMessages != null && genericMessages.getConfirmCTA() != null)
            msg = genericMessages.getConfirmCTA();
        return msg;
    }

    public String getFaceIdText() {
        String msg = context.getString(R.string.face_id);
        if (localizationResult != null && localizationResult.getFaceId() != null)
            msg = localizationResult.getFaceId();
        else if (genericMessages != null && genericMessages.getFaceId() != null)
            msg = genericMessages.getFaceId();
        return msg;

    }

    public String getTouchIdText() {
        String msg = context.getString(R.string.touch_id);
        if (localizationResult != null && localizationResult.getTouchId() != null)
            msg = localizationResult.getTouchId();
        else if (genericMessages != null && genericMessages.getTouchId() != null)
            msg = genericMessages.getTouchId();
        return msg;

    }

    public String getFaceIdEnabledTitle() {
        String msg = context.getString(R.string.face_id_enabled_title);
        if (localizationResult != null && localizationResult.getFaceIdEnabledTitle() != null)
            msg = localizationResult.getFaceIdEnabledTitle();
        else if (genericMessages != null && genericMessages.getFaceIdEnabledTitle() != null)
            msg = genericMessages.getFaceIdEnabledTitle();
        return msg;

    }

    public String getTouchIdEnabledTitle() {
        String msg = context.getString(R.string.touch_id_enabled_title);
        if (localizationResult != null && localizationResult.getTouchIdEnabledTitle() != null)
            msg = localizationResult.getTouchIdEnabledTitle();
        else if (genericMessages != null && genericMessages.getTouchIdEnabledTitle() != null)
            msg = genericMessages.getTouchIdEnabledTitle();
        return msg;

    }

    public String getUsePinText() {
        String msg = context.getString(R.string.use_pin);
        if (localizationResult != null && localizationResult.getUsePin() != null)
            msg = localizationResult.getUsePin();
        else if (genericMessages != null && genericMessages.getUsePin() != null)
            msg = genericMessages.getUsePin();
        return msg;

    }

    public String getRatingPromptMessage() {
        String msg = context.getString(R.string.rating_prompt_message);
        if (localizationResult != null && localizationResult.getRatingPromptMessage() != null)
            msg = localizationResult.getRatingPromptMessage();
        else if (genericMessages != null && genericMessages.getRatingPromptMessage() != null)
            msg = genericMessages.getRatingPromptMessage();
        return msg;
    }

    public String getRatingPromptConfirmationMessage() {
        String msg = context.getString(R.string.rating_prompt_confirmation_message);
        if (localizationResult != null && localizationResult.getRatingPromptConfirmationMessage() != null)
            msg = localizationResult.getRatingPromptConfirmationMessage();
        else if (genericMessages != null && genericMessages.getRatingPromptConfirmationMessage() != null)
            msg = genericMessages.getRatingPromptConfirmationMessage();
        return msg;
    }

    public String getRatingProceedAllowText() {
        String msg = context.getString(R.string.yes);
        if (localizationResult != null && localizationResult.getRatingProceedAllow() != null)
            msg = localizationResult.getRatingProceedAllow();
        else if (genericMessages != null && genericMessages.getRatingProceedAllow() != null)
            msg = genericMessages.getRatingProceedAllow();
        return msg;
    }

    public String getRatingProceedDenyText() {
        String msg = context.getString(R.string.no);
        if (localizationResult != null && localizationResult.getRatingProceedDeny() != null)
            msg = localizationResult.getRatingProceedDeny();
        else if (genericMessages != null && genericMessages.getRatingProceedDeny() != null)
            msg = genericMessages.getRatingProceedDeny();
        return msg;
    }

    public String getPageAvailabilityText() {
        String msg = context.getString(R.string.page_availability);
        if (localizationResult != null && localizationResult.getPageAvailabilityForLoggedInUser() != null)
            msg = localizationResult.getPageAvailabilityForLoggedInUser();
        else if (genericMessages != null && genericMessages.getPageAvailabilityForLoggedInUser() != null)
            msg = genericMessages.getPageAvailabilityForLoggedInUser();
        return msg;
    }

    public String getAlreadyLoggedInText() {
        String msg = context.getString(R.string.alreadyLoggedIn);
        if (localizationResult != null && localizationResult.getAlreadyLoggedIn() != null)
            msg = localizationResult.getAlreadyLoggedIn();
        else if (genericMessages != null && genericMessages.getAlreadyLoggedIn() != null)
            msg = genericMessages.getAlreadyLoggedIn();
        return msg;
    }

    public String getAlreadySubscribed() {
        String msg = context.getString(R.string.already_subscribe_msg_deep_link);
        if (localizationResult != null && localizationResult.getAlreadySubscribedUser() != null)
            msg = localizationResult.getAlreadyLoggedIn();
        else if (genericMessages != null && genericMessages.getAlreadyLoggedIn() != null)
            msg = genericMessages.getAlreadyLoggedIn();
        return msg;
    }

    public String getPleaseLoginToViewMyAha() {
        String msg = context.getString(R.string.pleaseLoginToViewMyAha);
        if (localizationResult != null && localizationResult.getPleaseLoginToViewMyAha() != null)
            msg = localizationResult.getPleaseLoginToViewMyAha();
        else if (genericMessages != null && genericMessages.getPleaseLoginToViewMyAha() != null)
            msg = genericMessages.getPleaseLoginToViewMyAha();
        return msg;
    }

    public String getUpdateValidNumberMessage() {
        String msg = context.getString(R.string.updateValidNumber);
        if (localizationResult != null && localizationResult.getUpdateValidNumber() != null)
            msg = localizationResult.getUpdateValidNumber();
        else if (genericMessages != null && genericMessages.getUpdateValidNumber() != null)
            msg = genericMessages.getUpdateValidNumber();
        return msg;
    }


    public String getTveLoginText() {
        String msg = context.getString(R.string.app_cms_tve_log_in);
        if (localizationResult != null && localizationResult.getTveLogin() != null)
            msg = localizationResult.getTveLogin();
        else if (genericMessages != null && genericMessages.getTveLogin() != null)
            msg = genericMessages.getTveLogin();
        return msg;
    }

    public String getContentRatingDiscretion() {
        String msg = context.getString(R.string.content_rating_viewer_discretion);
        if (localizationResult != null && localizationResult.getContentRatingViewerDiscretionLabel() != null)
            msg = localizationResult.getContentRatingViewerDiscretionLabel();
        else if (genericMessages != null && genericMessages.getContentRatingViewerDiscretionLabel() != null)
            msg = genericMessages.getContentRatingViewerDiscretionLabel();
        return msg;
    }

    public String getContentRatingWarning() {
        String msg = context.getString(R.string.content_rating_text_warning);
        if (localizationResult != null && localizationResult.getContentRatingViewerDiscretionLabel() != null)
            msg = localizationResult.getContentRatingViewerDiscretionLabel();
        else if (genericMessages != null && genericMessages.getContentRatingViewerDiscretionLabel() != null)
            msg = genericMessages.getContentRatingViewerDiscretionLabel();
        return msg;
    }

    public String getWaysToWatchText() {
        String msg = context.getString(R.string.ways_to_watch);
        if (localizationResult != null && localizationResult.getWaysToWatch() != null)
            msg = localizationResult.getWaysToWatch();
        else if (genericMessages != null && genericMessages.getWaysToWatch() != null)
            msg = genericMessages.getWaysToWatch();
        return msg;
    }

    public String getWaysToWatchMessageText() {
        String msg = context.getString(R.string.ways_to_watch_msg);
        if (localizationResult != null && localizationResult.getWaysToWatchMessage() != null)
            msg = localizationResult.getWaysToWatchMessage();
        else if (genericMessages != null && genericMessages.getWaysToWatchMessage() != null)
            msg = genericMessages.getWaysToWatchMessage();
        return msg;
    }

    public String getBecomeMemberText() {
        String msg = context.getString(R.string.become_member);
        if (localizationResult != null && localizationResult.getBecomeAmemberCta() != null)
            msg = localizationResult.getBecomeAmemberCta();
        else if (genericMessages != null && genericMessages.getBecomeAmemberCta() != null)
            msg = genericMessages.getBecomeAmemberCta();
        return msg;
    }

    public String getChooseTVProviderText() {
        String msg = context.getString(R.string.choose_tv);
        if (localizationResult != null && localizationResult.getChooseTvProviderCta() != null)
            msg = localizationResult.getChooseTvProviderCta();
        else if (genericMessages != null && genericMessages.getChooseTvProviderCta() != null)
            msg = genericMessages.getChooseTvProviderCta();
        return msg;
    }

    public String getOwnText() {
        String msg = context.getString(R.string.own);
        if (localizationResult != null && localizationResult.getOwnCta() != null)
            msg = localizationResult.getOwnCta();
        else if (genericMessages != null && genericMessages.getOwnCta() != null)
            msg = genericMessages.getOwnCta();
        return msg;
    }

    public String getHaveAccountText() {
        String msg = context.getString(R.string.have_account);
        if (localizationResult != null && localizationResult.getHaveAccount() != null)
            msg = localizationResult.getHaveAccount();
        else if (genericMessages != null && genericMessages.getHaveAccount() != null)
            msg = genericMessages.getHaveAccount();
        return msg;
    }

    public String getLoginText() {
        String msg = context.getString(R.string.app_cms_login_button_text);
        if (localizationResult != null && localizationResult.getLoginTextCta() != null)
            msg = localizationResult.getLoginTextCta();
        else if (genericMessages != null && genericMessages.getLoginTextCta() != null)
            msg = genericMessages.getLoginTextCta();
        return msg;
    }

    public String getPlanUpgradeText() {
        String msg = context.getString(R.string.upgrade_plan);
        if (localizationResult != null && localizationResult.getPlanUpgrade() != null)
            msg = localizationResult.getPlanUpgrade();
        else if (genericMessages != null && genericMessages.getPlanUpgrade() != null)
            msg = genericMessages.getPlanUpgrade();
        return msg;
    }

    public String getEmailText() {
        String msg = context.getString(R.string.email_required);
        if (localizationResult != null && localizationResult.getEnterEmailLabel() != null)
            msg = localizationResult.getEnterEmailLabel();
        else if (genericMessages != null && genericMessages.getEnterEmailLabel() != null)
            msg = genericMessages.getEnterEmailLabel();
        return msg;
    }

    public String getEmailRequiredMsgText() {
        String msg = context.getString(R.string.email_required_msg);
        if (localizationResult != null && localizationResult.getAndLabel() != null)
            msg = localizationResult.getAndLabel();
        else if (genericMessages != null && genericMessages.getAndLabel() != null)
            msg = genericMessages.getAndLabel();
        return msg;
    }

    public String getContentRatingTextWarningLabel() {
        String msg = context.getString(R.string.content_rating_text_warning);
        if (localizationResult != null && localizationResult.getContentRatingTextWarningLabel() != null)
            msg = localizationResult.getContentRatingTextWarningLabel();
        else if (genericMessages != null && genericMessages.getContentRatingTextWarningLabel() != null)
            msg = genericMessages.getContentRatingTextWarningLabel();
        return msg;
    }

    public String getContentRatingViewerDiscretionLabel() {
        String msg = context.getString(R.string.content_rating_viewer_discretion);
        if (localizationResult != null && localizationResult.getContentRatingViewerDiscretionLabel() != null)
            msg = localizationResult.getContentRatingViewerDiscretionLabel();
        else if (genericMessages != null && genericMessages.getContentRatingViewerDiscretionLabel() != null)
            msg = genericMessages.getContentRatingViewerDiscretionLabel();
        return msg;
    }

    public String getSeeAllTray() {
        String msg = context.getString(R.string.see_all);
        if (localizationResult != null && localizationResult.getSeeAllTray() != null)
            msg = localizationResult.getSeeAllTray();
        else if (genericMessages != null && genericMessages.getSeeAllTray() != null)
            msg = genericMessages.getSeeAllTray();
        return msg;
    }

    public String getLanguageAlertMessage() {
        String msg = context.getString(R.string.changelanguage);
        if (localizationResult != null && localizationResult.getLanguageAlertMessage() != null)
            msg = localizationResult.getLanguageAlertMessage();
        else if (genericMessages != null && genericMessages.getLanguageAlertMessage() != null)
            msg = genericMessages.getLanguageAlertMessage();
        return msg;
    }

    public String getLanguageSelectionConfirmMessage() {
        String msg = context.getString(R.string.changelanguage);
        if (localizationResult != null && localizationResult.getLanguageSelectionConfirmMessage() != null)
            msg = localizationResult.getLanguageSelectionConfirmMessage();
        else if (genericMessages != null && genericMessages.getLanguageSelectionConfirmMessage() != null)
            msg = genericMessages.getLanguageSelectionConfirmMessage();
        return msg;
    }

    public String getStartWatchingText() {
        String msg = context.getString(R.string.start_watching);
        if (localizationResult != null && localizationResult.getWatchNowCta() != null)
            msg = localizationResult.getWatchNowCta();
        else if (genericMessages != null && genericMessages.getWatchNowCta() != null)
            msg = genericMessages.getWatchNowCta();
        return msg;
    }

    public String getMenuTitle() {
        String msg = context.getString(R.string.menu);
        if (localizationResult != null && localizationResult.getMenuTitle() != null)
            msg = localizationResult.getMenuTitle();
        else if (genericMessages != null && genericMessages.getMenuTitle() != null)
            msg = genericMessages.getMenuTitle();
        return msg;
    }

    public String getCancelCta() {
        String msg = context.getString(R.string.cancel);
        if (localizationResult != null && localizationResult.getCancelCta() != null)
            msg = localizationResult.getCancelCta();
        else if (genericMessages != null && genericMessages.getErrorDialogCancelCta() != null)
            msg = genericMessages.getErrorDialogCancelCta();
        return msg;
    }

    public String getMyNavItemPrefix() {
        String msg = context.getString(R.string.my);
        if (localizationResult != null && localizationResult.getMyNavItemPrefix() != null)
            msg = localizationResult.getMyNavItemPrefix();
        else if (genericMessages != null && genericMessages.getMyNavItemPrefix() != null)
            msg = genericMessages.getMyNavItemPrefix();
        return msg;
    }

    public String getLoginToSeeWatchlistLabel() {
        String msg = context.getString(R.string.open_watchlist_dialog_text);
        if (localizationResult != null && localizationResult.getLoginToSeeWatchlistLabel() != null)
            msg = localizationResult.getLoginToSeeWatchlistLabel();
        else if (genericMessages != null && genericMessages.getLoginToSeeWatchlistLabel() != null)
            msg = genericMessages.getLoginToSeeWatchlistLabel();
        return msg;
    }

    public String getLoginToSeeHistoryLabel() {
        String msg = context.getString(R.string.open_history_dialog_text);
        if (localizationResult != null && localizationResult.getLoginToSeeHistoryLabel() != null)
            msg = localizationResult.getLoginToSeeHistoryLabel();
        else if (genericMessages != null && genericMessages.getLoginToSeeHistoryLabel() != null)
            msg = genericMessages.getLoginToSeeHistoryLabel();
        return msg;
    }

    public String getManageSubscriptionText() {
        String msg = context.getString(R.string.manage_subscription);
        if (localizationResult != null && localizationResult.getManageSubsubcription() != null)
            msg = localizationResult.getManageSubsubcription();
        else if (genericMessages != null && genericMessages.getManageSubsubcription() != null)
            msg = genericMessages.getManageSubsubcription();
        return msg;
    }

    public String getAutoplayOffMenu() {
        String msg = context.getString(R.string.autoplay_off);
        if (localizationResult != null && localizationResult.getAutoplayOffMenu() != null)
            msg = localizationResult.getAutoplayOffMenu();
        else if (genericMessages != null && genericMessages.getAutoplayOffMenu() != null)
            msg = genericMessages.getAutoplayOffMenu();
        return msg;
    }

    public String getAutoplayOnMenu() {
        String msg = context.getString(R.string.autoplay_on);
        if (localizationResult != null && localizationResult.getAutoplayOnMenu() != null)
            msg = localizationResult.getAutoplayOnMenu();
        else if (genericMessages != null && genericMessages.getAutoplayOnMenu() != null)
            msg = genericMessages.getAutoplayOnMenu();
        return msg;
    }

    public String getClosedCaptionOffMenu() {
        String msg = context.getString(R.string.closed_caption_off_key);
        if (localizationResult != null && localizationResult.getClosedCaptionOffMenu() != null)
            msg = localizationResult.getClosedCaptionOffMenu();
        else if (genericMessages != null && genericMessages.getClosedCaptionOffMenu() != null)
            msg = genericMessages.getClosedCaptionOffMenu();
        return msg;
    }

    public String getClosedCaptionOnMenu() {
        String msg = context.getString(R.string.closed_caption_on_key);
        if (localizationResult != null && localizationResult.getClosedCaptionOnMenu() != null)
            msg = localizationResult.getClosedCaptionOnMenu();
        else if (genericMessages != null && genericMessages.getClosedCaptionOnMenu() != null)
            msg = genericMessages.getClosedCaptionOnMenu();
        return msg;
    }

    public String getWatchNowCtaNbc() {
        String msg = context.getString(R.string.watch_now);
        if (localizationResult != null && localizationResult.getWatchNowCtaNbc() != null)
            msg = localizationResult.getWatchNowCtaNbc();
        else if (genericMessages != null && genericMessages.getWatchNowCtaNbc() != null)
            msg = genericMessages.getWatchNowCtaNbc();
        return msg;
    }

    public String getLoadingMessage() {
        String msg = context.getString(R.string.loading);
        if (localizationResult != null && localizationResult.getLoadingMessage() != null)
            msg = localizationResult.getLoadingMessage();
        else if (genericMessages != null && genericMessages.getLoadingMessage() != null)
            msg = genericMessages.getLoadingMessage();
        return msg;
    }

    public String getSelectCta() {
        String msg = context.getString(R.string.select);
        if (localizationResult != null && localizationResult.getSelectCta() != null)
            msg = localizationResult.getSelectCta();
        else if (genericMessages != null && genericMessages.getSelectCta() != null)
            msg = genericMessages.getSelectCta();
        return msg;
    }

    public String getSelectedCta() {
        String msg = context.getString(R.string.selected);
        if (localizationResult != null && localizationResult.getSelectedCta() != null)
            msg = localizationResult.getSelectedCta();
        else if (genericMessages != null && genericMessages.getSelectedCta() != null)
            msg = genericMessages.getSelectedCta();
        return msg;
    }

    public String getContactUsLabel() {
        String msg = context.getString(R.string.contact_us);
        if (localizationResult != null && localizationResult.getContactUsLabel() != null)
            msg = localizationResult.getContactUsLabel();
        else if (genericMessages != null && genericMessages.getContactUsLabel() != null)
            msg = genericMessages.getContactUsLabel();
        return msg;
    }

    public String getEmailUsAtLabel() {
        String msg = context.getString(R.string.email_us_at);
        if (localizationResult != null && localizationResult.getEmailUsAtLabel() != null)
            msg = localizationResult.getEmailUsAtLabel();
        else if (genericMessages != null && genericMessages.getEmailUsAtLabel() != null)
            msg = genericMessages.getEmailUsAtLabel();
        return msg;
    }

    public String getCallUsAtLabel() {
        String msg = context.getString(R.string.call_us_at);
        if (localizationResult != null && localizationResult.getCallUsAtLabel() != null)
            msg = localizationResult.getCallUsAtLabel();
        else if (genericMessages != null && genericMessages.getCallUsAtLabel() != null)
            msg = genericMessages.getCallUsAtLabel();
        return msg;
    }

    public String getAndLabel() {
        String msg = context.getString(R.string.and);
        if (localizationResult != null && localizationResult.getAndLabel() != null)
            msg = localizationResult.getAndLabel();
        else if (genericMessages != null && genericMessages.getAndLabel() != null)
            msg = genericMessages.getAndLabel();
        return msg;
    }

    public String getJustFinishedLabel() {
        String msg = context.getString(R.string.just_finished);
        if (localizationResult != null && localizationResult.getJustFinishedLabel() != null)
            msg = localizationResult.getJustFinishedLabel();
        else if (genericMessages != null && genericMessages.getJustFinishedLabel() != null)
            msg = genericMessages.getJustFinishedLabel();
        return msg;
    }

    public String getCountdownCancelledLabel() {
        String msg = context.getString(R.string.countdown_cancelled);
        if (localizationResult != null && localizationResult.getCountdownCancelledLabel() != null)
            msg = localizationResult.getCountdownCancelledLabel();
        else if (genericMessages != null && genericMessages.getCountdownCancelledLabel() != null)
            msg = genericMessages.getCountdownCancelledLabel();
        return msg;
    }

    public String getNoSubscriptionMessage() {
        String msg = context.getString(R.string.no_active_subscription);
        if (localizationResult != null && localizationResult.getNoSubscriptionMessage() != null)
            msg = localizationResult.getNoSubscriptionMessage();
        else if (genericMessages != null && genericMessages.getNoSubscriptionMessage() != null)
            msg = genericMessages.getNoSubscriptionMessage();
        return msg;
    }

    public String getHoverSeasonsLabel() {
        String msg = context.getString(R.string.seasons);
        if (localizationResult != null && localizationResult.getHoverSeasonsLabel() != null)
            msg = localizationResult.getHoverSeasonsLabel();
        else if (genericMessages != null && genericMessages.getHoverSeasonsLabel() != null)
            msg = genericMessages.getHoverSeasonsLabel();
        return msg;
    }

    public String getHoverEpisodeLabel() {
        String msg = context.getString(R.string.episode);
        if (localizationResult != null && localizationResult.getHoverEpisodeLabel() != null)
            msg = localizationResult.getHoverEpisodeLabel();
        else if (genericMessages != null && genericMessages.getHoverEpisodeLabel() != null)
            msg = genericMessages.getHoverEpisodeLabel();
        return msg;
    }

    public String getHoverEpisodesLabel() {
        String msg = context.getString(R.string.episodes);
        if (localizationResult != null && localizationResult.getHoverEpisodesLabel() != null)
            msg = localizationResult.getHoverEpisodesLabel();
        else if (genericMessages != null && genericMessages.getHoverEpisodesLabel() != null)
            msg = genericMessages.getHoverEpisodesLabel();
        return msg;
    }

    public String getCancelCountdownCta() {
        String msg = context.getString(R.string.cancel_countdown);
        if (localizationResult != null && localizationResult.getCancelCountdownCta() != null)
            msg = localizationResult.getCancelCountdownCta();
        else if (genericMessages != null && genericMessages.getCancelCountdownCta() != null)
            msg = genericMessages.getCancelCountdownCta();
        return msg;
    }

    public String getGrabGrownUpMessage() {
        String msg = context.getString(R.string.grab_a_grown_up);
        if (localizationResult != null && localizationResult.getGrabGrownUpMessage() != null)
            msg = localizationResult.getGrabGrownUpMessage();
        else if (genericMessages != null && genericMessages.getGrabGrownUpMessage() != null)
            msg = genericMessages.getGrabGrownUpMessage();
        return msg;
    }

    public String getPressHoldContinue() {
        String msg = context.getString(R.string.press_and_hold_item);
        if (localizationResult != null && localizationResult.getPressHoldContinue() != null)
            msg = localizationResult.getPressHoldContinue();
        else if (genericMessages != null && genericMessages.getPressHoldContinue() != null)
            msg = genericMessages.getPressHoldContinue();
        return msg;
    }

    public String getPreviousSearchlabel() {
        String msg = context.getString(R.string.previous_searches);
        if (localizationResult != null && localizationResult.getPreviousSearchlabel() != null)
            msg = localizationResult.getPreviousSearchlabel();
        else if (genericMessages != null && genericMessages.getPreviousSearchlabel() != null)
            msg = genericMessages.getPreviousSearchlabel();
        return msg;
    }

    public String getClearHistoryCta() {
        String msg = context.getString(R.string.clear_history);
        if (localizationResult != null && localizationResult.getClearHistoryCta() != null)
            msg = localizationResult.getClearHistoryCta();
        else if (genericMessages != null && genericMessages.getClearHistoryCta() != null)
            msg = genericMessages.getClearHistoryCta();
        return msg;
    }

    public String getNoResultForLabel() {
        String msg = context.getString(R.string.app_cms_no_search_result);
        if (localizationResult != null && localizationResult.getNoResultForLabel() != null)
            msg = localizationResult.getNoResultForLabel();
        else if (genericMessages != null && genericMessages.getNoResultForLabel() != null)
            msg = genericMessages.getNoResultForLabel();
        return msg;
    }

    public String getResultTitleLabel() {
        String msg = context.getString(R.string.app_cms_no_search_result);
        if (localizationResult != null && localizationResult.getResultTitleLabel() != null)
            msg = localizationResult.getResultTitleLabel();
        else if (genericMessages != null && genericMessages.getResultTitleLabel() != null)
            msg = genericMessages.getResultTitleLabel();
        return msg;
    }

    public String getShowForBigKidsMessage() {
        String msg = context.getString(R.string.show_for_big_kids);
        if (localizationResult != null && localizationResult.getShowForBigKidsMessage() != null)
            msg = localizationResult.getShowForBigKidsMessage();
        else if (genericMessages != null && genericMessages.getShowForBigKidsMessage() != null)
            msg = genericMessages.getShowForBigKidsMessage();
        return msg;
    }

    public String getAnswerMathProblemMessage() {
        String msg = context.getString(R.string.answer_math_problem_message);
        if (localizationResult != null && localizationResult.getAnswerMathProblemMessage() != null)
            msg = localizationResult.getAnswerMathProblemMessage();
        else if (genericMessages != null && genericMessages.getAnswerMathProblemMessage() != null)
            msg = genericMessages.getAnswerMathProblemMessage();
        return msg;
    }

    public String getFindAdultForHelpMessage() {
        String msg = context.getString(R.string.find_an_adult);
        if (localizationResult != null && localizationResult.getFindAdultForHelpMessage() != null)
            msg = localizationResult.getFindAdultForHelpMessage();
        else if (genericMessages != null && genericMessages.getFindAdultForHelpMessage() != null)
            msg = genericMessages.getFindAdultForHelpMessage();
        return msg;
    }

    public String getEnterEmailAddressMsgText() {
        String msg = context.getString(R.string.please_enter_your_email_to_continue);
        if (localizationResult != null && localizationResult.getEnterEmailAddressMessageForFreePlan() != null)
            msg = localizationResult.getEnterEmailAddressMessageForFreePlan();
        else if (genericMessages != null && genericMessages.getEnterEmailAddressMessageForFreePlan() != null)
            msg = genericMessages.getEnterEmailAddressMessageForFreePlan();
        return msg;
    }

    public String getCongratulationsText() {
        String msg = context.getString(R.string.congratulations);
        if (localizationResult != null && localizationResult.getCongratulations() != null)
            msg = localizationResult.getCongratulations();
        else if (genericMessages != null && genericMessages.getCongratulations() != null)
            msg = genericMessages.getCongratulations();
        return msg;
    }

    public String getInviteSuccessText() {
        String msg = context.getString(R.string.invite_success_message);
        if (localizationResult != null && localizationResult.getInviteSuccessMessage() != null)
            msg = localizationResult.getInviteSuccessMessage();
        else if (genericMessages != null && genericMessages.getInviteSuccessMessage() != null)
            msg = genericMessages.getInviteSuccessMessage();
        return msg;
    }

    public String getSocialFreeMessage() {
        String msg = context.getString(R.string.get_social_free_message);
        if (localizationResult != null && localizationResult.getGetSocialFreeMessage() != null)
            msg = localizationResult.getGetSocialFreeMessage();
        else if (genericMessages != null && genericMessages.getGetSocialFreeMessage() != null)
            msg = genericMessages.getGetSocialFreeMessage();
        return msg;
    }

    public String getSocialShareviaText() {
        String msg = context.getString(R.string.get_social_share_via_text);
        if (localizationResult != null && localizationResult.getGetSocialShareviaText() != null)
            msg = localizationResult.getGetSocialShareviaText();
        else if (genericMessages != null && genericMessages.getGetSocialShareviaText() != null)
            msg = genericMessages.getGetSocialShareviaText();
        return msg;
    }

    public String getSocialTermsAndConditions() {
        String msg = context.getString(R.string.get_social_terms_and_condition_text);
        if (localizationResult != null && localizationResult.getGetSocialTermsAndConditions() != null)
            msg = localizationResult.getGetSocialTermsAndConditions();
        else if (genericMessages != null && genericMessages.getGetSocialTermsAndConditions() != null)
            msg = genericMessages.getGetSocialTermsAndConditions();
        return msg;
    }

    public String getSocialSignInButtonText() {
        String msg = context.getString(R.string.get_social_sign_in_button_text);
        if (localizationResult != null && localizationResult.getSocialSignInButtonText() != null)
            msg = localizationResult.getSocialSignInButtonText();
        else if (genericMessages != null && genericMessages.getSocialSignInButtonText() != null)
            msg = genericMessages.getSocialSignInButtonText();
        return msg;
    }

    public String getSocialSubscribeButtonText() {
        String msg = context.getString(R.string.get_social_subscribe_button_text);
        if (localizationResult != null && localizationResult.getSocialSubscribeButtonText() != null)
            msg = localizationResult.getSocialSubscribeButtonText();
        else if (genericMessages != null && genericMessages.getSocialSubscribeButtonText() != null)
            msg = genericMessages.getSocialSubscribeButtonText();
        return msg;
    }

    public String getSmallFontText() {
        String msg = context.getString(R.string.small_font);
        if (localizationResult != null && localizationResult.getSmallFont() != null)
            msg = localizationResult.getSmallFont();
        else if (genericMessages != null && genericMessages.getSmallFont() != null)
            msg = genericMessages.getSmallFont();
        return msg;
    }

    public String getRegularFontText() {
        String msg = context.getString(R.string.regular_font);
        if (localizationResult != null && localizationResult.getRegularFont() != null)
            msg = localizationResult.getRegularFont();
        else if (genericMessages != null && genericMessages.getRegularFont() != null)
            msg = genericMessages.getRegularFont();
        return msg;
    }

    public String getLargeFontText() {
        String msg = context.getString(R.string.large_font);
        if (localizationResult != null && localizationResult.getLargeFont() != null)
            msg = localizationResult.getLargeFont();
        else if (genericMessages != null && genericMessages.getLargeFont() != null)
            msg = genericMessages.getLargeFont();
        return msg;
    }

    public String getNextBillText() {
        String msg = context.getString(R.string.next_billing);
        if (localizationResult != null && localizationResult.getNextDateLabel() != null)
            msg = localizationResult.getNextDateLabel();
        else if (genericMessages != null && genericMessages.getNextDateLabel() != null)
            msg = genericMessages.getNextDateLabel();
        return msg;
    }

    public String getDownloadHighText() {
        String msg = context.getString(R.string.dowload_high);
        if (localizationResult != null && localizationResult.getHighQualityDownload() != null)
            msg = localizationResult.getHighQualityDownload();
        else if (genericMessages != null && genericMessages.getHighQualityDownload() != null)
            msg = genericMessages.getHighQualityDownload();
        return msg;
    }

    public String getDowloadMediumText() {
        String msg = context.getString(R.string.download_medium);
        if (localizationResult != null && localizationResult.getMediumDownloadQuality() != null)
            msg = localizationResult.getMediumDownloadQuality();
        else if (genericMessages != null && genericMessages.getMediumDownloadQuality() != null)
            msg = genericMessages.getMediumDownloadQuality();
        return msg;
    }

    public String getDownloadLowText() {
        String msg = context.getString(R.string.download_low);
        if (localizationResult != null && localizationResult.getLowDownloadQuality() != null)
            msg = localizationResult.getLowDownloadQuality();
        else if (genericMessages != null && genericMessages.getLowDownloadQuality() != null)
            msg = genericMessages.getLowDownloadQuality();
        return msg;
    }

    public String getDownloadExistsOfflineHeaderText() {
        String msg = context.getString(R.string.download_exists_offline_header);
        if (localizationResult != null && localizationResult.getDownloadAlreadyErrorTitle() != null)
            msg = localizationResult.getDownloadAlreadyErrorTitle();
        else if (genericMessages != null && genericMessages.getDownloadAlreadyErrorTitle() != null)
            msg = genericMessages.getDownloadAlreadyErrorTitle();
        return msg;
    }

    public String getDownloadExistsOfflineMessageText() {
        String msg = context.getString(R.string.download_exists_offline_message);
        if (localizationResult != null && localizationResult.getAlreadyDownloadedSameQuality() != null)
            msg = localizationResult.getAlreadyDownloadedSameQuality();
        else if (genericMessages != null && genericMessages.getAlreadyDownloadedSameQuality() != null)
            msg = genericMessages.getAlreadyDownloadedSameQuality();
        return msg;
    }
    public String getContentNotAvailableTVProviderText() {
        String msg = context.getString(R.string.content_unavailable_tv_provider);
        if (localizationResult != null && localizationResult.getContentNotAvailableTVProvider() != null)
            msg = localizationResult.getContentNotAvailableTVProvider();
        else if (genericMessages != null && genericMessages.getContentNotAvailableTVProvider() != null)
            msg = genericMessages.getContentNotAvailableTVProvider();
        return msg;
    }

    public String getSchedulToCancelMSG() {
        String msg = context.getString(R.string.subscription_schedule_to_cancel_on);
        if (localizationResult != null && localizationResult.getSubscriptionScheduleToCancelOn() != null)
            msg = localizationResult.getSubscriptionScheduleToCancelOn();
        else if (genericMessages != null && genericMessages.getSubscriptionScheduleToCancelOn() != null)
            msg = genericMessages.getSubscriptionScheduleToCancelOn();
        return msg;
    }

    public String getOpenBrawser() {
        String cancel = context.getString(R.string.open_browser);
        if (localizationResult != null && localizationResult.getOpenBrowserText() != null)
            cancel = localizationResult.getOpenBrowserText();
        else if (genericMessages != null && genericMessages.getOpenBrowserText() != null)
            cancel = genericMessages.getOpenBrowserText();
        return cancel;
    }

    public String getRe_subscribe() {
        String cancel = context.getString(R.string.re_subscribe);
        if (localizationResult != null && localizationResult.getReSubscribe() != null)
            cancel = localizationResult.getReSubscribe();
        else if (genericMessages != null && genericMessages.getReSubscribe() != null)
            cancel = genericMessages.getReSubscribe();
        return cancel;
    }

    public String getReSubscribeMessage_iTune() {
        String cancel = context.getString(R.string.previous_subscription_platform_ios_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_iosMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_iosMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_iosMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_iosMSG();
        return cancel;
    }

    public String getReSubscribeMessage_android() {
        String cancel = context.getString(R.string.previous_subscription_platform_android_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_androidMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_androidMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_androidMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_androidMSG();
        return cancel;
    }

    public String getReSubscribeMessage_web() {
        String cancel = context.getString(R.string.previous_subscription_platform_web_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_webMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_webMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_webMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_webMSG();
        return cancel;
    }

    public String getReSubscribeMessage_roku() {
        String cancel = context.getString(R.string.previous_subscription_platform_roku_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_rokuMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_rokuMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_rokuMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_rokuMSG();
        return cancel;
    }

    public String getReSubscribeMessage_amazon() {
        String cancel = context.getString(R.string.previous_subscription_platform_amazon_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_amazonMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_amazonMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_amazonMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_amazonMSG();
        return cancel;
    }

    public String getReSubscribeMessage_smartTV() {
        String cancel = context.getString(R.string.previous_subscription_platform_smartTv_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_smartTvMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_smartTvMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_smartTvMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_smartTvMSG();
        return cancel;
    }

    public String getReSubscribeMessage_windows() {
        String cancel = context.getString(R.string.previous_subscription_platform_windows_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_windowsMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_windowsMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_windowsMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_windowsMSG();
        return cancel;
    }

    public String getReSubscribeMessage_ps4() {
        String cancel = context.getString(R.string.previous_subscription_platform_ps4_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_ps4MSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_ps4MSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_ps4MSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_ps4MSG();
        return cancel;
    }

    public String getReSubscribeMessage_other() {
        String cancel = context.getString(R.string.previous_subscription_platform_otherDevice_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_otherMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_otherMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_otherMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_otherMSG();
        return cancel;
    }

    public String getReSubscribeMessage_fromAndroidWithOtherProcess() {
        String cancel = context.getString(R.string.previous_subscription_platform_anndroid_from_other_processer_user_dialog);
        if (localizationResult != null && localizationResult.getPreviousSubscriptionPlatform_androidWithOtherProcessorMSG() != null)
            cancel = localizationResult.getPreviousSubscriptionPlatform_androidWithOtherProcessorMSG();
        else if (genericMessages != null && genericMessages.getPreviousSubscriptionPlatform_androidWithOtherProcessorMSG() != null)
            cancel = genericMessages.getPreviousSubscriptionPlatform_androidWithOtherProcessorMSG();
        return cancel;
    }

    public String getWeek() {
        String msg = context.getString(R.string.app_cms_plan_renewal_cycle_type_week);
        if (localizationResult != null && localizationResult.getWeek() != null)
            msg = localizationResult.getWeek();
        else if (genericMessages != null && genericMessages.getWeek() != null)
            msg = genericMessages.getWeek();
        return msg;
    }

    public String getDayUpper() {
        String msg = context.getString(R.string.app_cms_plan_renewal_cycle_type_daily);
        if (localizationResult != null && localizationResult.getDayUpper() != null)
            msg = localizationResult.getDayUpper();
        else if (genericMessages != null && genericMessages.getDayUpper() != null)
            msg = genericMessages.getDayUpper();
        return msg;
    }

    public String getEditAccount() {
        String msg = context.getString(R.string.edit_account);
        if (localizationResult != null && localizationResult.getEditAccount() != null)
            msg = localizationResult.getEditAccount();
        else if (genericMessages != null && genericMessages.getEditAccount() != null)
            msg = genericMessages.getEditAccount();
        return msg;
    }

    public String getInAppUpdateDownloadMsg() {
        String msg = context.getString(R.string.in_app_update_download_msg);
        if (localizationResult != null && localizationResult.getInAppUpdateDownloadMessage() != null)
            msg = localizationResult.getInAppUpdateDownloadMessage();
        else if (genericMessages != null && genericMessages.getInAppUpdateDownloadMessage() != null)
            msg = genericMessages.getInAppUpdateDownloadMessage();
        return msg;
    }

    public String getInAppUpdateInstallLabel() {
        String msg = context.getString(R.string.in_app_update_install_label);
        if (localizationResult != null && localizationResult.getInAppUpdateInstallLabel() != null)
            msg = localizationResult.getInAppUpdateInstallLabel();
        else if (genericMessages != null && genericMessages.getInAppUpdateInstallLabel() != null)
            msg = genericMessages.getInAppUpdateInstallLabel();
        return msg;
    }

    public String getFailedPaymentErrorTitle() {
        String msg = context.getString(R.string.app_cms_payment_cancelled_dialog_title);
        if (localizationResult != null && localizationResult.getFailedPaymentErrorTitle() != null)
            msg = localizationResult.getFailedPaymentErrorTitle();
        else if (genericMessages != null && genericMessages.getFailedPaymentErrorTitle() != null)
            msg = genericMessages.getFailedPaymentErrorTitle();
        return msg;
    }

    public String getStrPaymentProcessCanceled() {
        String msg = context.getString(R.string.app_cms_payment_canceled_body);
        if (localizationResult != null && localizationResult.getStrPaymentProcessCanceled() != null)
            msg = localizationResult.getStrPaymentProcessCanceled();
        else if (genericMessages != null && genericMessages.getStrPaymentProcessCanceled() != null)
            msg = genericMessages.getStrPaymentProcessCanceled();
        return msg;
    }

    public String getSSLCommerzInitErrorMessage() {
        String msg = context.getString(R.string.ssl_commerz_initialization_error);
        if (localizationResult != null && localizationResult.getSslCommerzInitErrorMessage() != null)
            msg = localizationResult.getSslCommerzInitErrorMessage();
        else if (genericMessages != null && genericMessages.getSslCommerzInitErrorMessage() != null)
            msg = genericMessages.getSslCommerzInitErrorMessage();
        return msg;
    }

    public String getJusPayInitErrorMessage() {
        String msg = context.getString(R.string.juspay_initialization_error);
        if (localizationResult != null && localizationResult.getJusPayInitErrorMessage() != null)
            msg = localizationResult.getJusPayInitErrorMessage();
        else if (genericMessages != null && genericMessages.getJusPayInitErrorMessage() != null)
            msg = genericMessages.getJusPayInitErrorMessage();
        return msg;
    }

    public String getRentLabel() {
        String msg = context.getString(R.string.rent);
        if (localizationResult != null && localizationResult.getRentOverlayLabel() != null)
            msg = localizationResult.getRentOverlayLabel();
        else if (genericMessages != null && genericMessages.getRentOverlayLabel() != null)
            msg = genericMessages.getRentOverlayLabel();
        return msg;
    }

    public String getRentOptionsLabel() {
        String msg = context.getString(R.string.rent_options);
        if (localizationResult != null && localizationResult.getRentOptionsLabel() != null)
            msg = localizationResult.getRentOptionsLabel();
        else if (genericMessages != null && genericMessages.getRentOptionsLabel() != null)
            msg = genericMessages.getRentOptionsLabel();
        return msg;
    }

    public String getBuyLabel() {
        String msg = context.getString(R.string.buy);
        if (localizationResult != null && localizationResult.getPurchaseOverlayLabel() != null)
            msg = localizationResult.getPurchaseOverlayLabel();
        else if (genericMessages != null && genericMessages.getPurchaseOverlayLabel() != null)
            msg = genericMessages.getPurchaseOverlayLabel();
        return msg;
    }

    public String getBuyOptionsLabel() {
        String msg = context.getString(R.string.buy_options);
        if (localizationResult != null && localizationResult.getPurchaseOptionsLabel() != null)
            msg = localizationResult.getPurchaseOptionsLabel();
        else if (genericMessages != null && genericMessages.getPurchaseOptionsLabel() != null)
            msg = genericMessages.getPurchaseOptionsLabel();
        return msg;
    }

    public String getRentedLabel() {
        String msg = context.getString(R.string.rented);
        if (localizationResult != null && localizationResult.getRentedLabel() != null)
            msg = localizationResult.getRentedLabel();
        else if (genericMessages != null && genericMessages.getRentedLabel() != null)
            msg = genericMessages.getRentedLabel();
        return msg;
    }

    public String getPurchasedLabel() {
        String msg = context.getString(R.string.purchased);
        if (localizationResult != null && localizationResult.getPurchasedLabel() != null)
            msg = localizationResult.getPurchasedLabel();
        else if (genericMessages != null && genericMessages.getPurchasedLabel() != null)
            msg = genericMessages.getPurchasedLabel();
        return msg;
    }

    public String getFreeLabel() {
        String msg = context.getString(R.string.pricing_model_FREE);
        if (localizationResult != null && localizationResult.getFreeLabel() != null)
            msg = localizationResult.getFreeLabel();
        else if (genericMessages != null && genericMessages.getFreeLabel() != null)
            msg = genericMessages.getFreeLabel();
        return msg;
    }

    public String getNoSeasonMsg() {
        String msg = context.getString(R.string.no_season);
        if (localizationResult != null && localizationResult.getNoSeason() != null)
            msg = localizationResult.getNoSeason();
        else if (genericMessages != null && genericMessages.getNoSeason() != null)
            msg = genericMessages.getNoSeason();
        return msg;
    }

    public String getNoEpisodeMsg() {
        String msg = context.getString(R.string.no_episode);
        if (localizationResult != null && localizationResult.getNoEpisodes() != null)
            msg = localizationResult.getNoEpisodes();
        else if (genericMessages != null && genericMessages.getNoEpisodes() != null)
            msg = genericMessages.getNoEpisodes();
        return msg;
    }

    public String getItemUnavailableMsg() {
        String msg = context.getString(R.string.product_not_available);
        if (localizationResult != null && localizationResult.getItemUnavailableMsg() != null)
            msg = localizationResult.getItemUnavailableMsg();
        else if (genericMessages != null && genericMessages.getItemUnavailableMsg() != null)
            msg = genericMessages.getItemUnavailableMsg();
        return msg;
    }

    public String getNoVideoMsg() {
        String msg = context.getString(R.string.no_video);
        if (localizationResult != null && localizationResult.getNoVideos() != null)
            msg = localizationResult.getNoVideos();
        else if (genericMessages != null && genericMessages.getNoVideos() != null)
            msg = genericMessages.getNoVideos();
        return msg;
    }

    public String getDownloadUnavailableMsg() {
        String msg = context.getString(R.string.download_unavailable);
        if (localizationResult != null && localizationResult.getDownloadUnavilableMsg() != null)
            msg = localizationResult.getDownloadUnavilableMsg();
        else if (genericMessages != null && genericMessages.getDownloadUnavilableMsg() != null)
            msg = genericMessages.getDownloadUnavilableMsg();
        return msg;
    }

    public String getCastUnavailableMsg() {
        String msg = context.getString(R.string.cast_unavailable);
        if (localizationResult != null && localizationResult.getCastUnavilableMsg() != null)
            msg = localizationResult.getCastUnavilableMsg();
        else if (genericMessages != null && genericMessages.getCastUnavilableMsg() != null)
            msg = genericMessages.getCastUnavilableMsg();
        return msg;
    }

    public String getVideoUnavailablePlatformMsg() {
        String msg = context.getString(R.string.video_unavailable_platform);
        if (localizationResult != null && localizationResult.getVideoUnavailableOnPlatformMsg() != null)
            msg = localizationResult.getVideoUnavailableOnPlatformMsg();
        else if (genericMessages != null && genericMessages.getVideoUnavailableOnPlatformMsg() != null)
            msg = genericMessages.getVideoUnavailableOnPlatformMsg();
        return msg;
    }

    public String getHDStreamUnavailableMsg() {
        String msg = context.getString(R.string.hd_stream_unavailable);
        if (localizationResult != null && localizationResult.getHdStreamUnavailableMsg() != null)
            msg = localizationResult.getHdStreamUnavailableMsg();
        else if (genericMessages != null && genericMessages.getHdStreamUnavailableMsg() != null)
            msg = genericMessages.getHdStreamUnavailableMsg();
        return msg;
    }

    public String getSeriesInclude() {
        String msg = context.getString(R.string.series_include);
        if (localizationResult != null && localizationResult.getSeriesIncludeTitle() != null)
            msg = localizationResult.getSeriesIncludeTitle();
        else if (genericMessages != null && genericMessages.getSeriesIncludeTitle() != null)
            msg = genericMessages.getSeriesIncludeTitle();
        return msg;
    }

    public String getTimePeriodVerbiage() {
        String msg = context.getString(R.string.time_period_verbiage);
        if (localizationResult != null && localizationResult.getTimePeriodVerbiage() != null)
            msg = localizationResult.getTimePeriodVerbiage();
        else if (genericMessages != null && genericMessages.getTimePeriodVerbiage() != null)
            msg = genericMessages.getTimePeriodVerbiage();
        return msg;
    }

    public String getGetSocialReferredFriendsList() {
        String msg = context.getString(R.string.get_social_referred_friends_list);
        if (localizationResult != null && localizationResult.getGetSocialReferredFriendsList() != null)
            msg = localizationResult.getGetSocialReferredFriendsList();
        else if (genericMessages != null && genericMessages.getGetSocialReferredFriendsList() != null)
            msg = genericMessages.getGetSocialReferredFriendsList();
        return msg;
    }

    public String getGetSocialSerialNumberText() {
        String msg = context.getString(R.string.get_social_serial_number_text);
        if (localizationResult != null && localizationResult.getGetSocialSerialNumberText() != null)
            msg = localizationResult.getGetSocialSerialNumberText();
        else if (genericMessages != null && genericMessages.getGetSocialSerialNumberText() != null)
            msg = genericMessages.getGetSocialSerialNumberText();
        return msg;
    }

    public String getGetSocialReferredFriendsTitle() {
        String msg = context.getString(R.string.get_social_referred_friends_title);
        if (localizationResult != null && localizationResult.getGetSocialReferredFriendsTitle() != null)
            msg = localizationResult.getGetSocialReferredFriendsTitle();
        else if (genericMessages != null && genericMessages.getGetSocialReferredFriendsTitle() != null)
            msg = genericMessages.getGetSocialReferredFriendsTitle();
        return msg;
    }

    public String getGetSocialInstallChannelTitle() {
        String msg = context.getString(R.string.get_social_install_channel_title);
        if (localizationResult != null && localizationResult.getGetSocialInstallChannelTitle() != null)
            msg = localizationResult.getGetSocialInstallChannelTitle();
        else if (genericMessages != null && genericMessages.getGetSocialInstallChannelTitle() != null)
            msg = genericMessages.getGetSocialInstallChannelTitle();
        return msg;
    }

    public String getGetSocialSubscriptionStatusTitle() {
        String msg = context.getString(R.string.get_social_subscription_status_title);
        if (localizationResult != null && localizationResult.getGetSocialSubscriptionStatusTitle() != null)
            msg = localizationResult.getGetSocialSubscriptionStatusTitle();
        else if (genericMessages != null && genericMessages.getGetSocialSubscriptionStatusTitle() != null)
            msg = genericMessages.getGetSocialSubscriptionStatusTitle();
        return msg;
    }

    public String getGetSocialSubscriptionDateTitle() {
        String msg = context.getString(R.string.get_social_subscription_date_title);
        if (localizationResult != null && localizationResult.getGetSocialSubscriptionDateTitle() != null)
            msg = localizationResult.getGetSocialSubscriptionDateTitle();
        else if (genericMessages != null && genericMessages.getGetSocialSubscriptionDateTitle() != null)
            msg = genericMessages.getGetSocialSubscriptionDateTitle();
        return msg;
    }

    public String getGetSocialSubscribedText() {
        String msg = context.getString(R.string.get_social_subscribed_text);
        if (localizationResult != null && localizationResult.getGetSocialSubscribedText() != null)
            msg = localizationResult.getGetSocialSubscribedText();
        else if (genericMessages != null && genericMessages.getGetSocialSubscribedText() != null)
            msg = genericMessages.getGetSocialSubscribedText();
        return msg;
    }

    public String getGetSocialNoRecordFoundText() {
        String msg = context.getString(R.string.get_social_no_record_found_text);
        if (localizationResult != null && localizationResult.getGetSocialNoRecordFoundText() != null)
            msg = localizationResult.getGetSocialNoRecordFoundText();
        else if (genericMessages != null && genericMessages.getGetSocialNoRecordFoundText() != null)
            msg = genericMessages.getGetSocialNoRecordFoundText();
        return msg;
    }

    public String getAppExitAlertMessage() {
        String msg = context.getString(R.string.app_exit_alert_message);
        if (localizationResult != null && localizationResult.getAppExitAlertMessage() != null)
            msg = localizationResult.getAppExitAlertMessage();
        else if (genericMessages != null && genericMessages.getAppExitAlertMessage() != null)
            msg = genericMessages.getAppExitAlertMessage();
        return msg;
    }
    public String getTransactionTitle() {
        String msg = context.getString(R.string.transaction_title);
        if (localizationResult != null && localizationResult.getTransactionTitle() != null)
            msg = localizationResult.getTransactionTitle();
        else if (genericMessages != null && genericMessages.getTransactionTitle() != null)
            msg = genericMessages.getTransactionTitle();
        return msg;
    }

    public String getTransactionSuccessMsg() {
        String msg = context.getString(R.string.transaction_success_msg);
        if (localizationResult != null && localizationResult.getTransactionSuccessMsg() != null)
            msg = localizationResult.getTransactionSuccessMsg();
        else if (genericMessages != null && genericMessages.getTransactionSuccessMsg() != null)
            msg = genericMessages.getTransactionSuccessMsg();
        return msg;
    }

    public String getSkipRecapButtonText() {
        String msg = context.getString(R.string.skipRecapButtonText);
        if (localizationResult != null && localizationResult.getSkipRecapButtonText() != null)
            msg = localizationResult.getSkipRecapButtonText();
        else if (genericMessages != null && genericMessages.getSkipRecapButtonText() != null)
            msg = genericMessages.getSkipRecapButtonText();
        return msg;
    }

    public String getSkipIntroButtonText() {
        String msg = context.getString(R.string.skipIntroButtonText);
        if (localizationResult != null && localizationResult.getSkipIntroButtonText() != null)
            msg = localizationResult.getSkipIntroButtonText();
        else if (genericMessages != null && genericMessages.getSkipIntroButtonText() != null)
            msg = genericMessages.getSkipIntroButtonText();
        return msg;
    }

    public String getNextLabel() {
        String msg = context.getString(R.string.label_next);
        if (localizationResult != null && localizationResult.getNextLabel() != null)
            msg = localizationResult.getNextLabel();
        else if (genericMessages != null && genericMessages.getNextLabel() != null)
            msg = genericMessages.getNextLabel();
        return msg;
    }

    public String getStartFromBeginningText() {
        String msg = context.getString(R.string.startFromBeginningText);
        if (localizationResult != null && localizationResult.getStartFromBeginningText() != null)
            msg = localizationResult.getStartFromBeginningText();
        else if (genericMessages != null && genericMessages.getStartFromBeginningText() != null)
            msg = genericMessages.getStartFromBeginningText();
        return msg;
    }

    public String getUpgradeSubscriptionText() {
        String msg = context.getString(R.string.upgrade_subscription);
        if (localizationResult != null && localizationResult.getUpgradeMembership() != null)
            msg = localizationResult.getUpgradeMembership();
        else if (genericMessages != null && genericMessages.getUpgradeMembership() != null)
            msg = genericMessages.getUpgradeMembership();
        return msg;
    }

    public String getIapWebPurchaseText() {
        String msg = context.getString(R.string.web_purchase_msg);
        if (localizationResult != null && localizationResult.getIapReferralPurchaseWeb() != null)
            msg = localizationResult.getIapReferralPurchaseWeb();
        else if (genericMessages != null && genericMessages.getIapReferralPurchaseWeb() != null)
            msg = genericMessages.getIapReferralPurchaseWeb();
        return msg;
    }

}

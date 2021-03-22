package com.viewlift.views.components;

import com.viewlift.audio.playback.LocalPlayback;
import com.viewlift.casting.CastServiceProvider;
import com.viewlift.ccavenue.screens.WebViewActivity;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.network.rest.AppCMSCarrierBillingCall;
import com.viewlift.offlinedrm.DownloadTracker;
import com.viewlift.offlinedrm.OfflineDownloadTimerTask;
import com.viewlift.offlinedrm.TrackSelectionDialog;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPageActivity;
import com.viewlift.views.activity.AppCMSPlayAudioActivity;
import com.viewlift.views.activity.AppCMSPlayVideoActivity;
import com.viewlift.views.adapters.AppCMSBenefitPlanPageAdapter;
import com.viewlift.views.adapters.AppCMSConstraintViewAdapter;
import com.viewlift.views.adapters.AppCMSDownloadQualityAdapter;
import com.viewlift.views.adapters.AppCMSPlansAdapter;
import com.viewlift.views.adapters.AppCMSPlaylistAdapter;
import com.viewlift.views.adapters.AppCMSSavedCardsAdapter;
import com.viewlift.views.adapters.AppCMSSearchItemAdapter;
import com.viewlift.views.adapters.AppCMSTeamOuterAdapter;
import com.viewlift.views.adapters.AppCMSTraySeasonItemAdapter;
import com.viewlift.views.adapters.AppCMSUserPagesAdapter;
import com.viewlift.views.adapters.AppCMSUserWatHisDowAdapter;
import com.viewlift.views.adapters.AppCMSViewAdapter;
import com.viewlift.views.adapters.CountryCodeSpinnerAdapter;
import com.viewlift.views.adapters.ListSelectionDialogAdapter;
import com.viewlift.views.adapters.PaymentOptionsAdapter;
import com.viewlift.views.adapters.Plan04Adapter;
import com.viewlift.views.adapters.TVProvidersAdapter;
import com.viewlift.views.adapters.plans.PlansSpinnerAdapter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.customviews.LoginModule;
import com.viewlift.views.customviews.PlanFeaturesLayout;
import com.viewlift.views.customviews.PlayerSettingsView;
import com.viewlift.views.customviews.VideoPlayerView;
import com.viewlift.views.customviews.constraintviews.TimerViewFutureContent;
import com.viewlift.views.customviews.exoplayerview.CustomPlaybackControlView;
import com.viewlift.views.customviews.exoplayerview.CustomPlayerControlView;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;
import com.viewlift.views.dialog.ListSelectionDialog;
import com.viewlift.views.fragments.AccountLoginFragment;
import com.viewlift.views.fragments.AppCMSBillingHistoryFragment;
import com.viewlift.views.fragments.AppCMSDownloadQualityFragment;
import com.viewlift.views.fragments.AppCMSEditProfileFragment;
import com.viewlift.views.fragments.AppCMSGetSocialFragment;
import com.viewlift.views.fragments.AppCMSNavItemsFragment;
import com.viewlift.views.fragments.AppCMSNoPurchaseFragment;
import com.viewlift.views.fragments.AppCMSParentalControlsFragment;
import com.viewlift.views.fragments.AppCMSParentalPINFragment;
import com.viewlift.views.fragments.AppCMSParentalRatingFragment;
import com.viewlift.views.fragments.AppCMSPlayAudioFragment;
import com.viewlift.views.fragments.AppCMSPlayVideoFragment;
import com.viewlift.views.fragments.AppCMSReauthoriseUserFragment;
import com.viewlift.views.fragments.AppCMSResetPasswordFragment;
import com.viewlift.views.fragments.AppCMSRestDeatilsFragment;
import com.viewlift.views.fragments.AppCMSSearchFragment;
import com.viewlift.views.fragments.AppCMSThankYouFragment;
import com.viewlift.views.fragments.AppCMSTrayMenuDialogFragment;
import com.viewlift.views.fragments.AppCMSUpgradeFragment;
import com.viewlift.views.fragments.AppCMSWebviewFragment;
import com.viewlift.views.fragments.BillingFragment;
import com.viewlift.views.fragments.GenericAuthenticationFragment;
import com.viewlift.views.fragments.LoginFragment;
import com.viewlift.views.fragments.MathProblemFragment;
import com.viewlift.views.fragments.PhoneUpdationLoginFragment;
import com.viewlift.views.fragments.SignUpFragment;
import com.viewlift.views.fragments.TVProviderFragment;
import com.viewlift.views.fragments.UserProfileSettingsFragment;
import com.viewlift.views.fragments.VerifyOTPPhoneFragment;
import com.viewlift.views.modules.AppCMSPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by viewlift on 5/22/17.
 */

@Singleton
@Component(modules = {AppCMSPresenterModule.class})
public interface AppCMSPresenterComponent {
    AppCMSPresenter appCMSPresenter();

    LocalisedStrings localisedStrings();

    AppPreference appPreference();

    AppCMSCarrierBillingCall getAppCMSCarrierBillingCall();

    void inject(AppCMSPageActivity activity);

    void inject(AppCMSPlayAudioActivity activity);

    void inject(AppCMSPlayVideoActivity activity);

    void inject(WebViewActivity activity);

    void inject(AppCMSTrayMenuDialogFragment fragment);

    void inject(PlansSpinnerAdapter adapter);

    void inject(PaymentOptionsAdapter adapter);

    void inject(AppCMSPlayVideoFragment fragment);

    void inject(AppCMSWebviewFragment fragment);

    void inject(TVProviderFragment fragment);

    void inject(AppCMSPlayAudioFragment fragment);

    void inject(UserProfileSettingsFragment fragment);

    void inject(AppCMSSearchFragment fragment);

    void inject(AppCMSDownloadQualityFragment fragment);

    void inject(BillingFragment fragment);

    void inject(MathProblemFragment fragment);

    void inject(VerifyOTPPhoneFragment fragment);

    void inject(PhoneUpdationLoginFragment fragment);

    void inject(AppCMSNavItemsFragment fragment);

    void inject(AppCMSEditProfileFragment fragment);

    void inject(AppCMSGetSocialFragment fragment);

    void inject(AppCMSRestDeatilsFragment fragment);

    void inject(AppCMSNoPurchaseFragment fragment);

    void inject(AppCMSUpgradeFragment fragment);

    void inject(AppCMSThankYouFragment fragment);

    void inject(AppCMSParentalControlsFragment fragment);

    void inject(AppCMSParentalPINFragment fragment);

    void inject(AppCMSParentalRatingFragment fragment);

    void inject(AppCMSVerifyVideoPinDialog fragment);

    void inject(AppCMSReauthoriseUserFragment fragment);

    void inject(AppCMSResetPasswordFragment fragment);

    void inject(GenericAuthenticationFragment fragment);

    void inject(AccountLoginFragment fragment);

    void inject(LoginFragment fragment);

    void inject(SignUpFragment fragment);

    void inject(AppCMSPlaylistAdapter adapter);

    void inject(AppCMSSavedCardsAdapter adapter);

    void inject(AppCMSUserPagesAdapter adapter);

    void inject(AppCMSTeamOuterAdapter adapter);

    void inject(AppCMSTraySeasonItemAdapter adapter);

    void inject(CountryCodeSpinnerAdapter adapter);

    void inject(AppCMSPlansAdapter adapter);

    void inject(AppCMSViewAdapter adapter);

    void inject(AppCMSConstraintViewAdapter adapter);

    void inject(AppCMSBenefitPlanPageAdapter adapter);

    void inject(AppCMSSearchItemAdapter adapter);

    void inject(AppCMSDownloadQualityAdapter adapter);

    void inject(AppCMSUserWatHisDowAdapter adapter);

    void inject(TVProvidersAdapter adapter);

    void inject(CustomVideoPlayerView view);

    void inject(VideoPlayerView view);

    void inject(PlayerSettingsView view);

    void inject(CastServiceProvider serviceProvider);

    void inject(LoginModule module);

    void inject(LocalPlayback playback);

    void inject(BaseView view);

    void inject(ListSelectionDialog dialog);

    void inject(PlanFeaturesLayout view);

    void inject(ListSelectionDialogAdapter adapter);

    void inject(Plan04Adapter adapter);

    void inject(CustomPlaybackControlView customPlaybackControlView);

    void inject(TimerViewFutureContent timerViewFutureContent);

    void inject(CustomPlayerControlView customPlayerControlView);

    void inject(DownloadTracker downloadTracker);

    void inject(OfflineDownloadTimerTask offlineDownloadTimerTask);

    void inject(AppCMSBillingHistoryFragment appCMSBillingHistoryFragment);

    void inject(TrackSelectionDialog trackSelectionDialog);
}

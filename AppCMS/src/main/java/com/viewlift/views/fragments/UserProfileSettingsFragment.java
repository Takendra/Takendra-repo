package com.viewlift.views.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.viewlift.AppCMSApplication;
import com.viewlift.CMSColorUtils;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.FileUtils;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;
import com.viewlift.views.dialog.CustomShape;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileSettingsFragment extends Fragment {
    @BindView(R.id.parentLayout)
    NestedScrollView parentLayout;
    @BindView(R.id.pageTitle)
    AppCompatTextView pageTitle;
    @BindView(R.id.autoplayLayout)
    ConstraintLayout autoplayLayout;
    @BindView(R.id.sdCardlayout)
    ConstraintLayout sdCardlayout;
    @BindView(R.id.billingContainer)
    ConstraintLayout billingContainer;
    @BindView(R.id.purchaseSection)
    ConstraintLayout purchaseSection;
    @BindView(R.id.personalizationSection)
    ConstraintLayout personalizationSection;
    @BindView(R.id.helpSection)
    ConstraintLayout helpSection;
    @BindView(R.id.accountDetailsSection)
    ConstraintLayout accountDetailsSection;
    @BindView(R.id.nameContainer)
    ConstraintLayout nameContainer;
    @BindView(R.id.emailContainer)
    ConstraintLayout emailContainer;
    @BindView(R.id.mobileContainer)
    ConstraintLayout mobileContainer;
    @BindView(R.id.passwordContainer)
    ConstraintLayout passwordContainer;
    @BindView(R.id.mobileFieldsContainer)
    ConstraintLayout mobileFieldsContainer;
    @BindView(R.id.connectedAccountFieldsContainer)
    ConstraintLayout connectedAccountFieldsContainer;
    @BindView(R.id.connectedAccountContainer)
    ConstraintLayout connectedAccountContainer;
    @BindView(R.id.paymentProcessorContainer)
    ConstraintLayout paymentProcessorContainer;
    @BindView(R.id.appSettingsSection)
    ConstraintLayout appSettingsSection;
    @BindView(R.id.downloadSettingsSection)
    ConstraintLayout downloadSettingsSection;
    @BindView(R.id.parentalControlSection)
    ConstraintLayout parentalControlSection;
    @BindView(R.id.parentalControlTitle)
    AppCompatTextView parentalControlTitle;
    @BindView(R.id.parentalControls)
    AppCompatTextView parentalControls;
    @BindView(R.id.parentalControlsline)
    View parentalControlsline;
    @BindView(R.id.parentalControlsToggle)
    SwitchCompat parentalControlsToggle;
    @BindView(R.id.billingPaymentSection)
    ConstraintLayout billingPaymentSection;
    @BindView(R.id.accountDetailTitle)
    AppCompatTextView accountDetailTitle;
    @BindView(R.id.name)
    AppCompatEditText name;
    @BindView(R.id.nameTitle)
    AppCompatTextView nameTitle;
    @BindView(R.id.nameEdit)
    AppCompatImageView nameEdit;
    @BindView(R.id.email)
    AppCompatEditText email;
    @BindView(R.id.emailTitle)
    AppCompatTextView emailTitle;
    @BindView(R.id.emailEdit)
    AppCompatImageView emailEdit;
    @BindView(R.id.mobile)
    AppCompatEditText mobile;
    @BindView(R.id.mobileTitle)
    AppCompatTextView mobileTitle;
    @BindView(R.id.password)
    AppCompatEditText password;
    @BindView(R.id.passwordTitle)
    AppCompatTextView passwordTitle;
    @BindView(R.id.passwordEdit)
    AppCompatImageView passwordEdit;
    @BindView(R.id.connectedAccountTitle)
    AppCompatTextView connectedAccountTitle;
    @BindView(R.id.tvProviderImage)
    AppCompatImageView tvProviderImage;
    @BindView(R.id.billingPaymentTitle)
    AppCompatTextView billingPaymentTitle;
    @BindView(R.id.recurringTitle)
    AppCompatTextView recurringTitle;
    @BindView(R.id.billingHistoryTitle)
    AppCompatTextView billingHistoryTitle;
    @BindView(R.id.billingTitle)
    AppCompatTextView billingTitle;
    @BindView(R.id.nextBillingTitle)
    AppCompatTextView nextBillingTitle;
    @BindView(R.id.nextBillingValue)
    AppCompatTextView nextBillingValue;
    @BindView(R.id.paymentProcessor)
    AppCompatTextView paymentProcessor;
    @BindView(R.id.paymentProcessorTitle)
    AppCompatTextView paymentProcessorTitle;
    @BindView(R.id.appSettingsTitle)
    AppCompatTextView appSettingsTitle;
    @BindView(R.id.autoplayTitle)
    AppCompatTextView autoplayTitle;
    @BindView(R.id.useSdCardTitle)
    AppCompatTextView useSdCardTitle;
    @BindView(R.id.appVersionValue)
    AppCompatTextView appVersionValue;
    @BindView(R.id.appVersionTitle)
    AppCompatTextView appVersionTitle;
    @BindView(R.id.downloadSettingsTitle)
    AppCompatTextView downloadSettingsTitle;
    @BindView(R.id.downloadQualityTitle)
    AppCompatTextView downloadQualityTitle;
    @BindView(R.id.downloadQualityValue)
    AppCompatTextView downloadQualityValue;
    @BindView(R.id.changeDownloadQuality)
    AppCompatTextView changeDownloadQuality;
    @BindView(R.id.cellularDataTitle)
    AppCompatTextView cellularDataTitle;
    @BindView(R.id.purchaseTitle)
    AppCompatTextView purchaseTitle;
    @BindView(R.id.subscribeButton)
    AppCompatButton subscribeButton;
    @BindView(R.id.helpTitle)
    AppCompatTextView helpTitle;
    @BindView(R.id.helpValue)
    AppCompatTextView helpValue;
    @BindView(R.id.addTopic)
    AppCompatTextView addTopic;
    @BindView(R.id.personalizationTitle)
    AppCompatTextView personalizationTitle;
    @BindView(R.id.recommendationFieldsContainer)
    ConstraintLayout recommendationFieldsContainer;
    @BindView(R.id.recommendationContainer)
    ConstraintLayout recommendationContainer;
    @BindView(R.id.interestView)
    FlexboxLayout interestView;
    @BindView(R.id.interestTitle)
    AppCompatTextView interestTitle;
    @BindView(R.id.cancelSubscription)
    AppCompatTextView cancelSubscription;
    @BindView(R.id.upgradeSubscription)
    AppCompatTextView upgradeSubscription;
    @BindView(R.id.recurringPurchasesTitle)
    AppCompatTextView recurringPurchasesTitle;
    @BindView(R.id.purchasedPlan)
    AppCompatTextView purchasedPlan;
    @BindView(R.id.onDemandPurchasesTitle)
    AppCompatTextView onDemandPurchasesTitle;
    @BindView(R.id.seePurchases)
    AppCompatTextView seePurchases;
    @BindView(R.id.purchaseUnderline)
    View purchaseUnderline;
    @BindView(R.id.seeFullHistory)
    AppCompatTextView seeFullHistory;
    @BindView(R.id.historyUnderline)
    View historyUnderline;
    @BindView(R.id.autoplayToggle)
    SwitchCompat autoplayToggle;
    @BindView(R.id.useSDCardForDownloadsToggle)
    SwitchCompat useSDCardForDownloadsToggle;
    @BindView(R.id.cellularDataToggle)
    SwitchCompat cellularDataToggle;
    @BindView(R.id.mobileEditB)
    AppCompatImageView mobileEdit;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    AppPreference appPreference;
    private AppCMSBinder appCMSBinder;
    private MetadataMap metadataMap = null;

    public UserProfileSettingsFragment() {
        // Required empty public constructor
    }

    public static UserProfileSettingsFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        UserProfileSettingsFragment fragment = new UserProfileSettingsFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        if (appCMSBinder.getAppCMSPageUI() != null) {
            Module module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_04)), appCMSBinder.getAppCMSPageAPI());
            if (module != null)
                metadataMap = module.getMetadataMap();
        }

        setViewStyle();
        setFont();
        setSwitchStyle();
        setLocalizedTitle();
        setData();
        setHelpText();
        handleMobileVisiblity();
        handlePersonalizationVisiblity();
        handleConectedAccountVisiblity();
        handleAutoplayVisiblity();
        handleParentalControlVisiblity();
        handleDownloadSectionVisiblity();
        handleSDCardVisiblity();
        handleCellularVisiblity();
        handleCancelSubscriptionVisibility();
        handleUpgradeSubscriptionVisiblity();
        handleBillingContainerVisiblity();
        handlePurchaseContainerVisiblity();
        handleHelpSectionVisiblity();
    }

    private void setFont() {
        Component component = new Component();
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, name);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, nameTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, email);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, emailTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, mobile);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, mobileTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, password);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, passwordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, connectedAccountTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, subscribeButton);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, recurringPurchasesTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, purchasedPlan);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, upgradeSubscription);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, onDemandPurchasesTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, seePurchases);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, seeFullHistory);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, nextBillingTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, nextBillingValue);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, cancelSubscription);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, paymentProcessor);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, paymentProcessorTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, addTopic);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, interestTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, autoplayTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, useSdCardTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appVersionTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appVersionValue);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, downloadQualityTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, downloadQualityValue);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, changeDownloadQuality);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, parentalControls);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, cellularDataTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, helpValue);
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, pageTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, accountDetailTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, purchaseTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, billingPaymentTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, recurringTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, billingHistoryTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, billingTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, personalizationTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appSettingsTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, downloadSettingsTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, parentalControlTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, helpTitle);

    }

    private void setHelpText() {
        if (appCMSPresenter.getAppCMSMain().getCustomerService() != null && appCMSPresenter.getAppCMSMain().getCustomerService().getEmail() != null) {
            String helpEmail = appCMSPresenter.getAppCMSMain().getCustomerService().getEmail();
            String help = getString(R.string.help_prefix, helpEmail, getString(R.string.help_suffix));
            helpValue.setText(help);
            helpValue.setLinkTextColor(appCMSPresenter.getBrandPrimaryCtaColor());
            ClickableSpan helpClick = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Intent intent = new  Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", helpEmail, null));
                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            };
            appCMSPresenter.makeTextViewLinks(helpValue, new String[]{helpEmail}, new ClickableSpan[]{helpClick}, true);
        }
    }

    private void setLocalizedTitle() {
        if (metadataMap != null) {
            if (metadataMap.getAccountHeader() != null)
                accountDetailTitle.setText(metadataMap.getAccountHeader());
            if (metadataMap.getName() != null)
                nameTitle.setText(metadataMap.getName());
            if (metadataMap.getEmail() != null)
                emailTitle.setText(metadataMap.getEmail());
            if (metadataMap.getMobileLabel() != null)
                mobileTitle.setText(metadataMap.getMobileLabel());
            if (metadataMap.getkStrPassword() != null)
                passwordTitle.setText(metadataMap.getkStrPassword());
            if (metadataMap.getConnectedAccountPlaceHolder() != null)
                connectedAccountTitle.setText(metadataMap.getConnectedAccountPlaceHolder());
            if (metadataMap.getPurchaseTitle() != null)
                purchaseTitle.setText(metadataMap.getPurchaseTitle());
            if (metadataMap.getRecurrinngPurchaseLabel() != null)
                recurringPurchasesTitle.setText(metadataMap.getRecurrinngPurchaseLabel());
            if (metadataMap.getSubscriptionAndBillingLabel() != null)
                billingPaymentTitle.setText(metadataMap.getSubscriptionAndBillingLabel());
            if (metadataMap.getRecurringBillingLabel() != null)
                recurringTitle.setText(metadataMap.getRecurringBillingLabel());
            if (metadataMap.getOnDemandPurchaseLabel() != null)
                onDemandPurchasesTitle.setText(metadataMap.getOnDemandPurchaseLabel());
            if (metadataMap.getSeeAllPuchaseCTA() != null)
                seePurchases.setText(metadataMap.getSeeAllPuchaseCTA());
            if (metadataMap.getBillingHistoryButtonText() != null)
                billingHistoryTitle.setText(metadataMap.getBillingHistoryButtonText());
            if (metadataMap.getNextDateLabel() != null)
                nextBillingTitle.setText(metadataMap.getNextDateLabel());
            if (metadataMap.getPaymentProcessorHeader() != null)
                paymentProcessorTitle.setText(metadataMap.getPaymentProcessorHeader());
            if (metadataMap.getPersonalizationtTitle() != null)
                personalizationTitle.setText(metadataMap.getPersonalizationtTitle());
            if (metadataMap.getAddTopicCta() != null)
                addTopic.setText(metadataMap.getAddTopicCta());
            if (metadataMap.getAppSettingsLabel() != null)
                appSettingsTitle.setText(metadataMap.getAppSettingsLabel());
            if (metadataMap.getUseSdCardForDownloadsLabel() != null)
                useSdCardTitle.setText(metadataMap.getUseSdCardForDownloadsLabel());
            if (metadataMap.getAutoplayLabel() != null)
                autoplayTitle.setText(metadataMap.getAutoplayLabel());
            if (metadataMap.getAppVersionLabel() != null)
                appVersionTitle.setText(metadataMap.getAppVersionLabel());
            if (metadataMap.getDownloadSettingsLabel() != null)
                downloadSettingsTitle.setText(metadataMap.getDownloadSettingsLabel());
            if (metadataMap.getDownloadQualityLabel() != null)
                downloadQualityTitle.setText(metadataMap.getDownloadQualityLabel());
            if (metadataMap.getCellularDataLabel() != null)
                cellularDataTitle.setText(metadataMap.getCellularDataLabel());
            if (metadataMap.getNeedHelpTitleLabel() != null)
                helpTitle.setText(metadataMap.getNeedHelpTitleLabel());
            if (metadataMap.getUpgradePlanButtonText() != null)
                upgradeSubscription.setText(metadataMap.getUpgradePlanButtonText().toUpperCase());
            if (!TextUtils.isEmpty(metadataMap.getParentalControlHeader())) {
                parentalControlTitle.setText(metadataMap.getParentalControlHeader());
                parentalControls.setText(metadataMap.getParentalControlHeader());
                if (!appPreference.isSetUserPassword()) {
                    if (metadataMap.getCreatePassword() != null)
                        password.setText(metadataMap.getCreatePassword());
                }
            }
        }
        cancelSubscription.setText(appCMSPresenter.getLocalisedStrings().getCancelText());
    }

    private void setViewStyle() {
        int primaryCtaColor = appCMSPresenter.getBrandPrimaryCtaColor();
        int appTextColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        int appBackgroundColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
        boolean isColorDark = CMSColorUtils.INSTANCE.isDarkColor(appBackgroundColor);
        int sectionalColor = CMSColorUtils.INSTANCE.lightenColor(appBackgroundColor, 0.1f);
        if (!isColorDark)
            sectionalColor = CMSColorUtils.INSTANCE.darkenColor(appBackgroundColor);
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        parentLayout.setBackgroundColor(appBackgroundColor);
        pageTitle.setTextColor(appTextColor);

        accountDetailsSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        accountDetailTitle.setTextColor(primaryCtaColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, nameContainer, rectangleStroke, appTextColor);
        name.setTextColor(appTextColor);
        nameTitle.setTextColor(appTextColor);
        nameTitle.setBackgroundColor(sectionalColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, emailContainer, rectangleStroke, appTextColor);
        email.setTextColor(appTextColor);
        emailTitle.setTextColor(appTextColor);
        emailTitle.setBackgroundColor(sectionalColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, mobileContainer, rectangleStroke, appTextColor);
        mobile.setTextColor(appTextColor);
        mobileTitle.setTextColor(appTextColor);
        mobileTitle.setBackgroundColor(sectionalColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, passwordContainer, rectangleStroke, appTextColor);
        password.setTextColor(appTextColor);
        passwordTitle.setTextColor(appTextColor);
        passwordTitle.setBackgroundColor(sectionalColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, connectedAccountContainer, rectangleStroke, appTextColor);
        connectedAccountTitle.setTextColor(appTextColor);
        connectedAccountTitle.setBackgroundColor(sectionalColor);
        nameEdit.setColorFilter(appTextColor, android.graphics.PorterDuff.Mode.SRC_IN);
        emailEdit.setColorFilter(appTextColor, android.graphics.PorterDuff.Mode.SRC_IN);
        mobileEdit.setColorFilter(appTextColor, android.graphics.PorterDuff.Mode.SRC_IN);
        passwordEdit.setColorFilter(appTextColor, android.graphics.PorterDuff.Mode.SRC_IN);

        billingPaymentSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        billingPaymentTitle.setTextColor(primaryCtaColor);
        recurringTitle.setTextColor(appTextColor);
        billingHistoryTitle.setTextColor(appTextColor);
        billingTitle.setTextColor(appTextColor);
        nextBillingTitle.setTextColor(appTextColor);
        nextBillingValue.setTextColor(appTextColor);
        cancelSubscription.setTextColor(appTextColor);
        upgradeSubscription.setTextColor(appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, paymentProcessorContainer, rectangleStroke, appTextColor);
        paymentProcessor.setTextColor(appTextColor);
        paymentProcessorTitle.setTextColor(appTextColor);
        paymentProcessorTitle.setBackgroundColor(sectionalColor);

        personalizationSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        personalizationTitle.setTextColor(primaryCtaColor);
        addTopic.setTextColor(primaryCtaColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, addTopic, rectangleStroke, appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, recommendationContainer, rectangleStroke, appTextColor);
        interestTitle.setTextColor(appTextColor);
        interestTitle.setBackgroundColor(sectionalColor);


        appSettingsSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        appSettingsTitle.setTextColor(primaryCtaColor);
        autoplayTitle.setTextColor(appTextColor);
        useSdCardTitle.setTextColor(appTextColor);
        appVersionTitle.setTextColor(appTextColor);
        appVersionValue.setTextColor(appTextColor);

        downloadSettingsSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        downloadSettingsTitle.setTextColor(primaryCtaColor);
        downloadQualityTitle.setTextColor(appTextColor);
        downloadQualityValue.setTextColor(appTextColor);
        changeDownloadQuality.setTextColor(appTextColor);
        cellularDataTitle.setTextColor(appTextColor);

        parentalControlSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        parentalControlTitle.setTextColor(primaryCtaColor);
        parentalControls.setTextColor(primaryCtaColor);
        parentalControlsline.setBackgroundColor(primaryCtaColor);

        purchaseSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        purchaseTitle.setTextColor(primaryCtaColor);
        subscribeButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        subscribeButton.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaTextColor()));
        recurringPurchasesTitle.setTextColor(appTextColor);
        onDemandPurchasesTitle.setTextColor(appTextColor);
        seePurchases.setTextColor(primaryCtaColor);
        seeFullHistory.setTextColor(primaryCtaColor);
        purchaseUnderline.setBackgroundColor(primaryCtaColor);
        historyUnderline.setBackgroundColor(primaryCtaColor);
        purchasedPlan.setTextColor(appTextColor);

        helpSection.setBackground(CustomShape.createRoundedRectangleDrawable(sectionalColor));
        helpTitle.setTextColor(primaryCtaColor);
        helpValue.setTextColor(appTextColor);


    }

    private void handleBillingContainerVisiblity() {
        if (!appCMSPresenter.isUserSubscribed() || appPreference.getActiveSubscriptionProcessor() == null)
            billingPaymentSection.setVisibility(View.GONE);
        else
            billingPaymentSection.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(appPreference.getUserPurchases())) {
            billingPaymentSection.setVisibility(View.VISIBLE);
            if (!appCMSPresenter.isUserSubscribed()) {
                recurringTitle.setVisibility(View.GONE);
                billingContainer.setVisibility(View.GONE);
            }
        }
    }

    private void handlePurchaseContainerVisiblity() {
        if (appCMSPresenter.isUserSubscribed() && appPreference.getActiveSubscriptionPlanName() != null) {
            subscribeButton.setVisibility(View.GONE);
            recurringPurchasesTitle.setVisibility(View.VISIBLE);
            purchasedPlan.setVisibility(View.VISIBLE);
        } else {
            subscribeButton.setVisibility(View.VISIBLE);
            recurringPurchasesTitle.setVisibility(View.GONE);
            purchasedPlan.setVisibility(View.GONE);
        }

        if (!appCMSPresenter.isAppSVOD()) {
            subscribeButton.setVisibility(View.GONE);
            recurringPurchasesTitle.setVisibility(View.GONE);
            purchasedPlan.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(appPreference.getUserPurchases())) {
            if (appCMSPresenter.isAppTVOD()||appCMSPresenter.isAppAVOD()) {
                purchaseSection.setVisibility(View.GONE);
            } else {
                onDemandPurchasesTitle.setVisibility(View.GONE);
                seePurchases.setVisibility(View.GONE);
                purchaseUnderline.setVisibility(View.GONE);
            }
        }

    }

    private void handleHelpSectionVisiblity() {
        if ((appCMSPresenter.getAppCMSMain().getCustomerService() == null)
                || (appCMSPresenter.getAppCMSMain().getCustomerService() != null && appCMSPresenter.getAppCMSMain().getCustomerService().getEmail() == null))
            helpSection.setVisibility(View.GONE);
    }

    private void handleCancelSubscriptionVisibility() {
        String paymentProcessor = appPreference.getActiveSubscriptionProcessor();
        if (appCMSPresenter.isUserSubscribed()
                && (paymentProcessor != null && (paymentProcessor.toLowerCase().equalsIgnoreCase(getString(R.string.subscription_android_payment_processor).toLowerCase())
                        || paymentProcessor.toLowerCase().equalsIgnoreCase(getString(R.string.subscription_android_payment_processor_friendly).toLowerCase())
                        || paymentProcessor.equalsIgnoreCase(getString(R.string.robi))))) {
            cancelSubscription.setVisibility(View.VISIBLE);
        } else {
            cancelSubscription.setVisibility(View.GONE);
        }
    }

    private void handleUpgradeSubscriptionVisiblity() {
        if (appCMSPresenter.isUserSubscribed() && appCMSPresenter.upgradesAvailableForUser() &&
                (appPreference.getActiveSubscriptionProcessor() != null && (appPreference.getActiveSubscriptionProcessor().toLowerCase().equalsIgnoreCase(getString(R.string.subscription_android_payment_processor).toLowerCase()) ||
                        appPreference.getActiveSubscriptionProcessor().toLowerCase().equalsIgnoreCase(getString(R.string.subscription_android_payment_processor_friendly).toLowerCase())))) {
            upgradeSubscription.setVisibility(View.VISIBLE);
        } else {
            upgradeSubscription.setVisibility(View.GONE);
        }
    }

    private void handleCellularVisiblity() {
        if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
            cellularDataToggle.setChecked(appCMSPresenter.getDownloadOverCellularEnabled());
            cellularDataToggle.setEnabled(true);
            cellularDataToggle.setChecked(appCMSPresenter.getDownloadOverCellularEnabled());
        }
    }

    private void handleDownloadSectionVisiblity() {
        if (!appCMSPresenter.isDownloadable())
            downloadSettingsSection.setVisibility(View.GONE);

    }

    private void handleSDCardVisiblity() {
        if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                appCMSPresenter.getAppCMSMain().getFeatures().isMobileAppDownloads()) {
            useSDCardForDownloadsToggle.setChecked(appCMSPresenter.getUserDownloadLocationPref());
            if (FileUtils.isExternalStorageAvailable(appCMSPresenter.getCurrentActivity())) {
                useSDCardForDownloadsToggle.setEnabled(true);
            } else {
                useSDCardForDownloadsToggle.setEnabled(false);
                useSDCardForDownloadsToggle.setChecked(false);
            }
        } else {
            useSDCardForDownloadsToggle.setEnabled(false);
            useSDCardForDownloadsToggle.setChecked(false);
            sdCardlayout.setVisibility(View.GONE);
        }
    }

    private void handleMobileVisiblity() {
        if (appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue() != null &&
                !appCMSPresenter.getAppCMSMain().getFeatures().getOtpValue().isOtpEnabled()) {
            if (!TextUtils.isEmpty(mobile.getText().toString()))
                mobileEdit.setVisibility(View.GONE);
            else
                mobileFieldsContainer.setVisibility(View.GONE);
        }
    }

    private void handlePersonalizationVisiblity() {
        if (appCMSPresenter.getAppCMSMain().getRecommendation() == null || (appCMSPresenter.getAppCMSMain().getRecommendation() != null
                && !appCMSPresenter.getAppCMSMain().getRecommendation().isRecommendation()
                && !appCMSPresenter.getAppCMSMain().getRecommendation().isPersonalization())) {
            personalizationSection.setVisibility(View.GONE);
        } else {
            if (appCMSPresenter.getAppCMSMain().getRecommendation().isSubscribed() && !appCMSPresenter.isUserSubscribed())
                personalizationSection.setVisibility(View.GONE);
            else
                createUserPersonalisationView();
        }
    }

    private void handleConectedAccountVisiblity() {
        if (appPreference.getTvProviderLogo() == null)
            connectedAccountFieldsContainer.setVisibility(View.GONE);
        else {
            Glide.with(this)
                    .load(appPreference.getTvProviderLogo())
                    .into(tvProviderImage);
        }
    }

    private void handleAutoplayVisiblity() {
        if (appCMSPresenter.getAppCMSMain().getFeatures() != null &&
                appCMSPresenter.isAutoPlayEnable()) {
            autoplayToggle.setChecked(appCMSPresenter.getAutoplayEnabledUserPref(getContext()));
        } else {
            autoplayToggle.setChecked(false);
            autoplayLayout.setVisibility(View.GONE);
        }
    }

    private void handleParentalControlVisiblity() {
        if (appCMSPresenter.getAppCMSMain().getFeatures() != null && appCMSPresenter.getAppCMSMain().getFeatures().isParentalControlEnable()) {
            parentalControlSection.setVisibility(View.VISIBLE);
            if (appPreference.isParentalControlsEnable())
                parentalControlsToggle.setChecked(true);
            else
                parentalControlsToggle.setChecked(false);
        } else {
            parentalControlSection.setVisibility(View.GONE);
        }
    }

    private void setSwitchStyle() {
        autoplayToggle.getTrackDrawable().setTint(appCMSPresenter.getGeneralTextColor());
        useSDCardForDownloadsToggle.getTrackDrawable().setTint(appCMSPresenter.getGeneralTextColor());
        cellularDataToggle.getTrackDrawable().setTint(appCMSPresenter.getGeneralTextColor());
        parentalControlsToggle.getTrackDrawable().setTint(appCMSPresenter.getGeneralTextColor());
        int switchOnColor = appCMSPresenter.getBrandPrimaryCtaColor();
        int switchOffColor = appCMSPresenter.getBrandSecondaryCtaTextColor();
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                }, new int[]{
                switchOnColor,
                switchOffColor
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            autoplayToggle.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
            autoplayToggle.setThumbTintList(colorStateList);
            useSDCardForDownloadsToggle.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
            useSDCardForDownloadsToggle.setThumbTintList(colorStateList);
            cellularDataToggle.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
            cellularDataToggle.setThumbTintList(colorStateList);
            parentalControlsToggle.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
            parentalControlsToggle.setThumbTintList(colorStateList);
        } else {
            autoplayToggle.setButtonTintList(colorStateList);
            useSDCardForDownloadsToggle.setButtonTintList(colorStateList);
            cellularDataToggle.setButtonTintList(colorStateList);
            parentalControlsToggle.setButtonTintList(colorStateList);
        }
    }

    private void setData() {
        if (appPreference.getLoggedInUserName() != null)
            name.setText(appPreference.getLoggedInUserName());
        if (appPreference.getLoggedInUserEmail() != null)
            email.setText(appPreference.getLoggedInUserEmail());
        if (appPreference.getActiveSubscriptionProcessor() != null)
            paymentProcessor.setText(appPreference.getActiveSubscriptionProcessor());
        if (appPreference.getLoggedInUserPhone() != null)
            mobile.setText(appPreference.getLoggedInUserPhone());
        if (appPreference.getActiveSubscriptionEndDate() != null)
            nextBillingValue.setText(CommonUtils.getDatebyDefaultZone(appCMSPresenter.getAppPreference().getActiveSubscriptionEndDate(), "MMMM dd, yyyy"));
        if (appPreference.getActiveSubscriptionPlanName() != null)
            purchasedPlan.setText(appPreference.getActiveSubscriptionPlanName());
        if (appPreference.getLoggedInUserPhone() != null)
            mobile.setText(appPreference.getLoggedInUserPhone());
        appVersionValue.setText(getString(R.string.app_cms_app_version));
        downloadQualityValue.setText(appCMSPresenter.getUserDownloadQualityPref());
    }

    @OnCheckedChanged(R.id.autoplayToggle)
    void autoplayToggleClick(CompoundButton button, boolean checked) {
        appCMSPresenter.setAutoplayEnabledUserPref(checked);
    }

    @OnCheckedChanged(R.id.useSDCardForDownloadsToggle)
    void sdCardToggleClick(CompoundButton button, boolean checked) {
        if (checked) {
            if (FileUtils.isRemovableSDCardAvailable(appCMSPresenter.getCurrentActivity())) {
                appCMSPresenter.setUserDownloadLocationPref(true);
            } else {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SD_CARD_NOT_AVAILABLE,
                        null,
                        false,
                        null,
                        null, null);
                useSDCardForDownloadsToggle.setChecked(false);
            }
        } else {
            appCMSPresenter.setUserDownloadLocationPref(false);
        }
    }

    @OnCheckedChanged(R.id.cellularDataToggle)
    void cellularToggleClick(CompoundButton button, boolean checked) {
        if (checked) {
            appCMSPresenter.setDownloadOverCellularEnabled(true);
        } else {
            appCMSPresenter.setDownloadOverCellularEnabled(false);
        }
    }

    @OnCheckedChanged(R.id.parentalControlsToggle)
    void parentalControlsToggleClick(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getParentalPin())) {
            AppCMSVerifyVideoPinDialog.newInstance(isVerified -> {
                if (isVerified) {
                    appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_manageParentalControls), null,
                            new String[]{Boolean.toString(isChecked)}, null, false, -1, null);
                } else {
                    buttonView.setEnabled(false);
                    buttonView.setChecked(!isChecked);
                    buttonView.setEnabled(true);
                }
            }, true).show(appCMSPresenter.getCurrentActivity().getSupportFragmentManager(), AppCMSVerifyVideoPinDialog.class.getSimpleName());
        } else {
            appCMSPresenter.launchButtonSelectedAction(null, getString(R.string.app_cms_action_manageParentalControls), null,
                    new String[]{Boolean.toString(isChecked)}, null, false, -1, null);
        }
    }

    @OnClick(R.id.seePurchases)
    void seePurchasesClick() {
        if (appCMSPresenter.getLibraryPage() != null)
            appCMSPresenter.navigateToLibraryPage(appCMSPresenter.getLibraryPage().getPageId(), appCMSPresenter.getLibraryPage().getPageName(), false, false);

    }

    @OnClick(R.id.parentalControls)
    void parentalControlsClick() {
        appCMSPresenter.launchButtonSelectedAction(
                null,
                getString(R.string.app_cms_action_launchParentalControls),
                null,
                null,
                null,
                false,
                0,
                null);

    }

    @OnClick(R.id.seeFullHistory)
    void seeFullHistoryClick() {
        FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(AppCMSBillingHistoryFragment.newInstance(metadataMap), "AppCMSBillingHistoryFragmentFragment");
        fragmentTransaction.commitAllowingStateLoss();

    }

    @OnClick(R.id.cancelSubscription)
    void cancelSubscriptionClick() {
        String[] extra = new String[1];
        extra[0] = getString(R.string.app_cms_page_cancel_subscription_key);
        appCMSPresenter.launchButtonSelectedAction(null,
                getString(R.string.app_cms_action_managesubscription_key),
                null,
                extra,
                null,
                false,
                0,
                null);
    }

    @OnClick(R.id.upgradeSubscription)
    void upgradeSubscriptionClick() {
        String[] extra = new String[1];
        extra[0] = getString(R.string.app_cms_page_upgrade_subscription_key);
        appCMSPresenter.launchButtonSelectedAction(null,
                getString(R.string.app_cms_action_managesubscription_key),
                null,
                extra,
                null,
                false,
                0,
                null);
    }

    @OnClick(R.id.changeDownloadQuality)
    void changeDownloadQualityClick() {
        appCMSPresenter.launchButtonSelectedAction(
                null,
                getString(R.string.app_cms_action_change_download_quality_key),
                null,
                null,
                null,
                false,
                0,
                null);
    }


    private void createUserPersonalisationView() {
        appCMSPresenter.showLoader();
        appCMSPresenter.getUserRecommendedGenres(appCMSPresenter.getLoggedInUser(), interest -> {
            appCMSPresenter.stopLoader();
            if (interest.equalsIgnoreCase("NOGENRE|") || TextUtils.isEmpty(interest)) {
                recommendationFieldsContainer.setVisibility(View.GONE);
            } else {
                recommendationFieldsContainer.setVisibility(View.VISIBLE);
                interestView.removeAllViews();
                String[] userInterest = interest.split("\\|");
                if (userInterest != null && getContext() != null) {
                    for (int i = 0; i < userInterest.length; i++) {
                        View itemView = LayoutInflater.from(getContext())
                                .inflate(R.layout.element_user_interest, recommendationContainer, false);
                        AppCompatTextView interestValue = itemView.findViewById(R.id.interest);
                        AppCompatImageButton removeInterest = itemView.findViewById(R.id.removeInterest);
                        ConstraintLayout parentLayout = itemView.findViewById(R.id.parentLayout);
                        parentLayout.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                        interestValue.setTextColor(appCMSPresenter.getGeneralTextColor());
                        interestValue.setText(userInterest[i]);
                        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), interestValue);
                        removeInterest.getBackground().setTint(appCMSPresenter.getGeneralTextColor());
                        removeInterest.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
                        final int pos = i;
                        removeInterest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                appCMSPresenter.sendUserRecommendationValues(
                                        appCMSPresenter.getLoggedInUser(),
                                        savedInteresets(userInterest[pos], userInterest),
                                        signInResponse -> createUserPersonalisationView()
                                );
                            }
                        });
                        interestView.addView(itemView);
                    }
                }
            }
        }, true, true);
    }

    private String savedInteresets(String interest, String[] userInterest) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < userInterest.length; i++) {
            if (!interest.equalsIgnoreCase(userInterest[i])) {
                sb.append(userInterest[i]);
                sb.append("|");
            }
        }
        if (sb.length() == 0)
            return "";
        return sb.toString().substring(0, sb.length() - 1);
    }

    @OnClick(R.id.addTopic)
    void addTopicClick() {
        if (appCMSPresenter.isPersonalizationEnabled()) {
            appCMSPresenter.showLoader();
            AppCMSPresenter.isFromSettings = true;
            appCMSPresenter.getUserRecommendedGenres(appCMSPresenter.getLoggedInUser(), s -> {
                appCMSPresenter.setSelectedGenreString(s);
                if (appCMSPresenter.isPersonalizationEnabled()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                appCMSPresenter.stopLoader();
                                appCMSPresenter.showRecommendationGenreDialog(signInResponse -> createUserPersonalisationView());
                            } catch (Exception e) {
                            }
                        }
                    }, 10);
                }
            }, true, true);
        }
    }

    @OnClick(R.id.emailEdit)
    void emailEditClick() {
        if (!appPreference.isSetUserPassword() && appPreference.getLoginType() != null && !appPreference.getLoginType().equalsIgnoreCase(getString(R.string.login_type_otp))) {
            String msg = getString(R.string.add_password_edit_email);
            if (metadataMap != null && metadataMap.getCreatePasswordToAddEmail() != null)
                msg = metadataMap.getCreatePasswordToAddEmail();
            appCMSPresenter.showToast(msg, Toast.LENGTH_SHORT);
            return;
        }
        appCMSPresenter.launchButtonSelectedAction(
                null,
                getString(R.string.app_cms_action_editprofile_key),
                null,
                null,
                null,
                false,
                0,
                null);
    }

    @OnClick(R.id.nameEdit)
    void nameEditClick() {
        if (!appPreference.isSetUserPassword() && appPreference.getLoginType() != null && !appPreference.getLoginType().equalsIgnoreCase(getString(R.string.login_type_otp))) {
            String msg = getString(R.string.add_password_edit_email);
            if (metadataMap != null && metadataMap.getCreatePasswordToAddName() != null)
                msg = metadataMap.getCreatePasswordToAddName();
            appCMSPresenter.showToast(msg, Toast.LENGTH_SHORT);
            return;
        }
        appCMSPresenter.launchButtonSelectedAction(
                null,
                getString(R.string.app_cms_action_editprofile_key),
                null,
                null,
                null,
                false,
                0,
                null);
    }

    @OnClick(R.id.mobileEditB)
    void mobileEditBClick() {
        appCMSPresenter.phoneObjectRequest.setFromVerify(true);
        appCMSPresenter.phoneObjectRequest.setMetadataMap(metadataMap);
        FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(PhoneUpdationLoginFragment.newInstance(appCMSPresenter.getCurrentActivity(), appCMSPresenter.phoneObjectRequest, true), "PhoneUpdationLoginFragment");
        fragmentTransaction.commitAllowingStateLoss();
    }

    @OnClick(R.id.passwordEdit)
    void passwordEditClick() {
        appCMSPresenter.launchButtonSelectedAction(
                null,
                getString(R.string.app_cms_action_change_password_key),
                null,
                null,
                null,
                false,
                0,
                null);

    }

    @OnClick(R.id.subscribeButton)
    void subscribeClick() {
        appCMSPresenter.navigateToSubscriptionPlansPage(false);
    }
}

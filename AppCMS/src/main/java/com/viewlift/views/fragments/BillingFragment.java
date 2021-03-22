package com.viewlift.views.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;

import com.viewlift.models.billing.utils.Payment;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.PaymentOptionsAdapter;
import com.viewlift.views.adapters.plans.PlansSpinnerAdapter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.CustomShape;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import rx.functions.Action0;

import static com.viewlift.presenters.AppCMSPresenter.LaunchType.TVOD_PURCHASE;


/**
 * Fragment displays list of available options to make payment
 *
 * @author Wishy
 * @since 2020-03-05
 */
public class BillingFragment extends Fragment implements PaymentOptionsAdapter.PaymentOptionSelected {

    @BindView(R.id.paymentOptions)
    RecyclerView paymentOptions;
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;
    @BindView(R.id.paymentMethodTitle)
    AppCompatTextView paymentMethodTitle;
    @BindView(R.id.termsView)
    AppCompatTextView termsView;
    private AppCMSBinder appCMSBinder;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @BindView(R.id.plan)
    AppCompatSpinner plan;
    ContentDatum selectedPlan;
    @BindView(R.id.planContainer)
    ConstraintLayout planContainer;

    public BillingFragment() {
    }

    public static BillingFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        BillingFragment fragment = new BillingFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_billing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        setViewStyles();
        setPlanDetails();
        initRecyclerView();
    }

    /**
     * Set textcolor, backgroundcolor, fonts, any style to views displayed
     *
     * @author Wishy
     */
    private void setViewStyles() {
        int appTextColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        int appBackgroundColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        parentLayout.setBackgroundColor(appBackgroundColor);
        paymentMethodTitle.setTextColor(appTextColor);
        termsView.setTextColor(appTextColor);
        termsView.setLinkTextColor(appTextColor);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), paymentMethodTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), termsView);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, planContainer, rectangleStroke, appTextColor);
        if (appCMSBinder.getAppCMSPageUI() != null)
            setTextToViews();
    }

    /**
     * Sets localised text picked from generic messages and metadatamap in pageApi
     *
     * @author Wishy
     */
    private void setTextToViews() {
        MetadataMap metadataMap = null;
        for (int i = 0; i < appCMSBinder.getAppCMSPageUI().getModuleList().size(); i++) {
            ModuleList moduleList = appCMSBinder.getAppCMSPageUI().getModuleList().get(i);
            if (moduleList.getBlockName().equalsIgnoreCase(getString(R.string.ui_block_authentication_17)) && moduleList.getSettings() != null && moduleList.getSettings().getOptions() != null) {
                if (appCMSBinder.getAppCMSPageAPI() != null) {
                    Module moduleApi = appCMSPresenter.matchModuleAPIToModuleUI(moduleList, appCMSBinder.getAppCMSPageAPI());
                    if (moduleApi != null) {
                        metadataMap = moduleApi.getMetadataMap();
                        break;
                    }
                }
            }
        }
        if (metadataMap != null) {
            if (metadataMap.getAccountLabel() != null)
                paymentMethodTitle.setText(metadataMap.getSelectPaymentMethod());
        }

        termsView.setText(appCMSPresenter.getLocalisedStrings().getTnCext());
        ClickableSpan tosClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                appCMSPresenter.navigatToTOSPage(null, null);
            }
        };
        ClickableSpan privacyClick = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                appCMSPresenter.navigateToPrivacyPolicy(null, null);
            }
        };
        appCMSPresenter.makeTextViewLinks(termsView, new String[]{
                appCMSPresenter.getLocalisedStrings().getTermsOfUsesText(), appCMSPresenter.getLocalisedStrings().getPrivacyPolicyText()}, new ClickableSpan[]{tosClick, privacyClick}, true);

    }


    private void initRecyclerView() {
        paymentOptions.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));
        List<Payment> paymentProviders = createPaymentList();
        PaymentOptionsAdapter adapter = new PaymentOptionsAdapter(getContext(), paymentProviders);
        adapter.setPaymentOptionSelected(this);
        paymentOptions.setAdapter(adapter);

    }


    public enum PaymentMethod {
        IN_APP_PURCHASE("In App Billing"),
        CC_AVENUE("CCAvenue"),
        SSL_COMMERZ("Ssl Commerz"),
        JUSPAY("Juspay");

        private final String text;

        PaymentMethod(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    private List<Payment> createPaymentList() {
        List<Payment> paymentProviders = new ArrayList<>();
        if (appCMSPresenter.getSelectedPlanId() != null || appCMSPresenter.getLaunchType() == TVOD_PURCHASE) {
            paymentProviders.add(new Payment(PaymentMethod.IN_APP_PURCHASE, R.drawable.ic_google_play));
            if (appCMSPresenter.useCCAvenue())
                paymentProviders.add(new Payment(PaymentMethod.CC_AVENUE, R.drawable.ic_google_play));
            if (appCMSPresenter.useSSLCommerz())
                paymentProviders.add(new Payment(PaymentMethod.SSL_COMMERZ, R.drawable.ic_google_play));
            if (appCMSPresenter.useJusPay())
                paymentProviders.add(new Payment(PaymentMethod.JUSPAY, R.drawable.ic_google_play));
        }
        return paymentProviders;
    }

    @Override
    public void billingSelected(Payment payment) {
        appCMSPresenter.setSelectedPaymentMethod(payment.getName());
        appCMSPresenter.setBilllingOptionsShown(true);

//        if (payment.getName() == PaymentMethod.IN_APP_PURCHASE) {
        if (appCMSPresenter.getLaunchType() == TVOD_PURCHASE) {
            appCMSPresenter.showLoader();
            appCMSPresenter.initiateTvodPurchase();
        } else if (selectedPlan != null) {
            if (payment.getName() == PaymentMethod.IN_APP_PURCHASE && appCMSPresenter.isReferralPlanPurchase()) {

                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.SUBSCRIBE, appCMSPresenter.getLocalisedStrings().getIapWebPurchaseText(), false, new Action0() {
                    @Override
                    public void call() {
                        appCMSPresenter.setReferralPlanPurchase(false);
                        appCMSPresenter.sendCloseOthersAction(null, true, false);
                    }
                }, null, null);
                return;
            }

            double price = selectedPlan.getPlanDetails().get(0).getStrikeThroughPrice();
            if (price == 0) {
                price = selectedPlan.getPlanDetails().get(0).getRecurringPaymentAmount();
            }
            double discountedPrice = selectedPlan.getPlanDetails().get(0).getRecurringPaymentAmount();
            double discountPrice = selectedPlan.getPlanDetails().get(0).getDiscountedPrice();
            boolean upgradesAvailable = false;
            if (selectedPlan.getPlanDetails() != null &&
                    !selectedPlan.getPlanDetails().isEmpty() &&
                    ((selectedPlan.getPlanDetails().get(0).getStrikeThroughPrice() != 0 &&
                            price < selectedPlan.getPlanDetails().get(0).getStrikeThroughPrice()) ||
                            (selectedPlan.getPlanDetails().get(0).getRecurringPaymentAmount() != 0 &&
                                    price < selectedPlan.getPlanDetails().get(0).getRecurringPaymentAmount()))) {
                upgradesAvailable = true;
            }
            appCMSPresenter.initiateSignUpAndSubscription(selectedPlan.getIdentifier(),
                    selectedPlan.getId(),
                    selectedPlan.getPlanDetails().get(0).getCountryCode(),
                    selectedPlan.getName(),
                    price,
                    discountedPrice,
                    selectedPlan.getPlanDetails().get(0).getRecurringPaymentCurrencyCode(),
                    selectedPlan.getPlanDetails().get(0).getCountryCode(),
                    selectedPlan.getRenewable(),
                    selectedPlan.getRenewalCycleType(),
                    upgradesAvailable, discountPrice,
                    selectedPlan.getPlanDetails().get(0).getAllowedPayMethods(),
                    selectedPlan.getPlanDetails().get(0).getCarrierBillingProviders());
        }
        /*} else if (payment.getName() == PaymentMethod.SSL_COMMERZ) {
        }*/
    }

    @OnItemSelected(R.id.plan)
    public void planSelected(Spinner spinner, int position) {
        appCMSPresenter.setSelectedPlan(appCMSPresenter.getPlans().get(position).getId(), appCMSPresenter.getPlans());
        selectedPlan = appCMSPresenter.getPlans().get(position);
    }

    /**
     * Set dropdown for plans available to purchase
     * Handle visibility in case of TVOD purchase
     *
     * @author Wishy
     */
    private void setPlanDetails() {
        if (appCMSPresenter.getLaunchType() == TVOD_PURCHASE) {
            planContainer.setVisibility(View.GONE);
            return;
        }

        if (appCMSPresenter.getPlans() != null) {
            PlansSpinnerAdapter adapter = new PlansSpinnerAdapter(getContext(), appCMSPresenter.getPlans());
            plan.setAdapter(adapter);
            for (int i = 0; i < appCMSPresenter.getPlans().size(); i++) {
                if (appCMSPresenter.getPlans().get(i).getId().equalsIgnoreCase(appCMSPresenter.getSelectedPlanId())) {
                    plan.setSelection(i);
                    selectedPlan = appCMSPresenter.getPlans().get(i);
                    break;
                }
            }
        }
    }


}

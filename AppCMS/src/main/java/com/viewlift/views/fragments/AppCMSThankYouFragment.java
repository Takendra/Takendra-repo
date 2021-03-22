package com.viewlift.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.OrderStatus;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;

import javax.inject.Inject;

public class AppCMSThankYouFragment extends DialogFragment {

    private static final String STATUS = "status";

    private OrderStatus status;
    private boolean isPaymentSuccess;

    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    public static AppCMSThankYouFragment newInstance(OrderStatus status) {
        AppCMSThankYouFragment fragment = new AppCMSThankYouFragment();
        Bundle args = new Bundle();
        args.putSerializable(STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = (OrderStatus) getArguments().getSerializable(STATUS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        return inflater.inflate(R.layout.thankyou_page_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        appCMSPresenter.restrictPortraitOnly();
        setCancelable(false);

        ImageView statusIcon = view.findViewById(R.id.statusIcon);
        TextView tvStatusTitle = view.findViewById(R.id.tvStatusTitle);
        TextView tvStatusMessage = view.findViewById(R.id.tvStatusMessage);
        tvStatusTitle.setTextColor(Color.BLACK);
        tvStatusMessage.setTextColor(Color.BLACK);

        if (status != null && getString(R.string.charged).equalsIgnoreCase(status.getStatus())) {
            isPaymentSuccess = true;
            statusIcon.setImageResource(R.drawable.ic_payment_success);
            tvStatusTitle.setText(localisedStrings.getSuccessMessageTitleText());
            tvStatusMessage.setText(localisedStrings.getSuccessText());
            if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.TVOD_PURCHASE) {
                tvStatusTitle.setText(localisedStrings.getTransactionTitle());
                tvStatusMessage.setText(localisedStrings.getTransactionSuccessMsg());
            }
        } else if (status != null && (getString(R.string.authorizing).equalsIgnoreCase(status.getStatus())
                || (getString(R.string.pending_vbv).equalsIgnoreCase(status.getStatus()) && status.getResponseMessage() != null && status.getResponseMessage().getCode() != 0))) {
            //statusIcon.setVisibility(View.GONE);
            statusIcon.setImageResource(R.drawable.ic_payment_pending);
            tvStatusTitle.setText(localisedStrings.getPendingMessageTitleText());
            tvStatusMessage.setText(localisedStrings.getPendingText());
        } else {
            statusIcon.setImageResource(R.drawable.ic_payment_failure);
            tvStatusTitle.setText(localisedStrings.getFailMessageTitleText());
            tvStatusMessage.setText(localisedStrings.getFailText());
        }

        new Handler().postDelayed(() -> {
            if (isPaymentSuccess) {
//                appCMSPresenter.navigateToHomePage(false);
                appCMSPresenter.finalizeSignupAfterCCAvenueSubscription(true);
                //Show Personalization Screen Post User Plan Purchase
                if (appCMSPresenter.isPersonalizationEnabled() && appCMSPresenter.getLaunchType() != AppCMSPresenter.LaunchType.TVOD_PURCHASE) {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        try {
                            appCMSPresenter.showPersonalizationscreenIfEnabled(false, true);
                        } catch (Exception e) {
                        }
                    }, 1000);
                }
            }
            dismissAllowingStateLoss();

        }, 3000);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
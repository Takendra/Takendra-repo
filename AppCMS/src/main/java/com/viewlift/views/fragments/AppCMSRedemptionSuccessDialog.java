package com.viewlift.views.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AppCMSRedemptionSuccessDialog extends DialogFragment {

    public static AppCMSRedemptionSuccessDialog newInstance(Context context, String title, String message) {
        AppCMSRedemptionSuccessDialog fragment = new AppCMSRedemptionSuccessDialog();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.app_cms_more_title_key), title);
        args.putString(context.getString(R.string.app_cms_more_text_key), message);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.app_cms_title_text)
    TextView appCMSTitleText;


    @BindView(R.id.app_cms_message_text)
    TextView appCMSMessageText;

    @BindView(R.id.btn_done)
    Button appCMSDoneButton;

    @BindView(R.id.app_cms_close_button)
    ImageButton appCMSCloseButton;

    private AppCMSPresenter appCMSPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redemption_dialog, container, false);


        ButterKnife.bind(this, view);

        Bundle args = getArguments();

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        appCMSDoneButton.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());
        appCMSDoneButton.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        appCMSDoneButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.done)));

        appCMSTitleText.setTextColor(appCMSPresenter.getGeneralTextColor());
        appCMSTitleText.setText(args.getString(getContext().getString(R.string.app_cms_more_title_key)));

        appCMSMessageText.setTextColor(appCMSPresenter.getGeneralTextColor());
        appCMSMessageText.setText(args.getString(getContext().getString(R.string.app_cms_more_text_key)));

        appCMSPresenter.dismissOpenDialogs(null);

        try {
            setBgColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        } catch (Exception e) {
            setBgColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }

        appCMSCloseButton.setOnClickListener((v) -> {
            sendDismissAction();
            if (appCMSPresenter != null) {
                appCMSPresenter.popActionInternalEvents();
                appCMSPresenter.setNavItemToCurrentAction(getActivity());
                appCMSPresenter.showMainFragmentView(true);
            }
        });

        appCMSDoneButton.setOnClickListener((v) -> {
            sendDismissAction();
            if (appCMSPresenter != null) {
                appCMSPresenter.popActionInternalEvents();
                appCMSPresenter.navigateToHomePage(true);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setWindow();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setWindow();
    }

    public void sendDismissAction() {
        dismiss();
        if (appCMSPresenter != null) {
            appCMSPresenter.showMainFragmentView(true);
        }
    }

    private void setBgColor(int bgColor) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(bgColor));
        }
    }


    private void setWindow() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Window window = dialog.getWindow();
            window.setLayout(width, height);

            window.setGravity(Gravity.START);
        }
    }

}

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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by viewlift on 7/17/17.
 */

public class AppCMSMoreFragment extends DialogFragment {
    private static final String TAG = "MoreFragment";

    public static AppCMSMoreFragment newInstance(Context context, String title, String moreText) {
        AppCMSMoreFragment fragment = new AppCMSMoreFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.app_cms_more_title_key), title);
        args.putString(context.getString(R.string.app_cms_more_text_key), moreText);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.app_cms_close_button)
    ImageButton appCMSCloseButton;

    @BindView(R.id.app_cms_more_text)
    TextView appCMSMoreText;


    @BindView(R.id.app_cms_more_title_text)
    TextView appCMSMoreTitleText;

    private AppCMSPresenter appCMSPresenter;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        unbinder = ButterKnife.bind(this, view);

        Bundle args = getArguments();

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
        Component component=new Component();
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appCMSMoreText);
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, appCMSMoreTitleText);

        String textColor = "#ffffffff";
        try {
            textColor = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor();
        } catch (Exception e) {
            //Log.e(TAG, "Could not retrieve text color from AppCMS Brand: " + e.getMessage());
        }

        appCMSCloseButton.setOnClickListener((v) -> {
            dismiss();
            if (appCMSPresenter != null) {
                appCMSPresenter.popActionInternalEvents();
                appCMSPresenter.setNavItemToCurrentAction(getActivity());
                appCMSPresenter.showMainFragmentView(true);
            }
        });

        try {
            appCMSMoreText.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                    .getBrand().getGeneral().getTextColor()));
        } catch (Exception e) {
            appCMSMoreText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }
        appCMSMoreText.setText(args.getString(getContext().getString(R.string.app_cms_more_text_key)));
//        appCMSMoreText.setText(Html.fromHtml(getContext().getString(R.string.text_with_color,
//                Integer.toHexString(Color.parseColor(textColor)).substring(2),
//                args.getString(getContext().getString(R.string.app_cms_more_text_key)))));


        appCMSMoreTitleText.setText(args.getString(getContext().getString(R.string.app_cms_more_title_key)));
//        appCMSMoreTitleText.setText(Html.fromHtml(getContext().getString(R.string.text_with_color,
//                Integer.toHexString(Color.parseColor(textColor)).substring(2),
//                args.getString(getContext().getString(R.string.app_cms_more_title_key)))));
        try {
            appCMSMoreText.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                    .getBrand().getGeneral().getTextColor()));
            appCMSMoreTitleText.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain()
                    .getBrand().getGeneral().getTextColor()));
        } catch (Exception e) {
            appCMSMoreText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
            appCMSMoreTitleText.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        }

        appCMSPresenter.dismissOpenDialogs(null);

        try {
            setBgColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        } catch (Exception e) {
            setBgColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setWindow();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setWindow();
    }

    public void sendDismissAction() {
        dismissAllowingStateLoss();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}

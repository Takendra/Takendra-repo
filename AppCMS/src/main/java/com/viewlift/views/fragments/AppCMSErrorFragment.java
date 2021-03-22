package com.viewlift.views.fragments;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSErrorActivity;

/**
 * Created by viewlift on 5/4/17.
 */

public class AppCMSErrorFragment extends Fragment {
    static String message = null;

    AppCMSPresenter appCMSPresenter;

    public static AppCMSErrorFragment newInstance(String errorMessage) {
        message = errorMessage;
        return new AppCMSErrorFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error_page, container, false);
        TextView errorTextView = view.findViewById(R.id.app_cms_error_textview);
        Button button = view.findViewById(R.id.app_cms_error_dismiss);

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        if (message != null) {
            if (message.contains(appCMSPresenter.getLocalisedStrings().getGeoRestrictText())) {
                button.setVisibility(View.VISIBLE);
                errorTextView.setTextColor(appCMSPresenter.getGeneralTextColor());
                button.setTextColor(appCMSPresenter.getGeneralTextColor());

                GradientDrawable loginBorder = new GradientDrawable();
                loginBorder.setShape(GradientDrawable.RECTANGLE);
                loginBorder.setStroke(getContext().getResources().getInteger(R.integer.app_cms_border_stroke_width), appCMSPresenter.getBrandPrimaryCtaColor());
                loginBorder.setColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
                button.setBackground(loginBorder);

            } else {
                message = message + "!\nPlease try again later!";
                button.setVisibility(View.GONE);
            }

            errorTextView.setText(message);
        } else {
            errorTextView.setText(Html.fromHtml(getString(R.string.error_loading_page)));
        }

        button.setOnClickListener(v -> ((AppCMSErrorActivity) getActivity()).finishTask());

        return view;
    }
}

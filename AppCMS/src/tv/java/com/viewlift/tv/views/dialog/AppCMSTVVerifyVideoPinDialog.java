package com.viewlift.tv.views.dialog;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.viewlift.tv.utility.Utils;
import com.viewlift.R;
import com.viewlift.views.dialog.AppCMSVerifyVideoPinDialog;

public class AppCMSTVVerifyVideoPinDialog extends AppCMSVerifyVideoPinDialog {

    public static AppCMSVerifyVideoPinDialog newInstance(OnVerifyVideoPinListener listener) {
        AppCMSTVVerifyVideoPinDialog pinDialog = new AppCMSTVVerifyVideoPinDialog();
        pinDialog.listener = listener;
        return pinDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getContext() != null) {
            int height = Utils.getViewYAxisAsPerScreen(getContext(), 60);
            if (confirmBtn.getLayoutParams() != null) {
                confirmBtn.getLayoutParams().height = height;
            }

            if (cancelBtn.getLayoutParams() != null) {
                cancelBtn.getLayoutParams().height  = height;
            }

            confirmBtn.setTextSize(11.5f);
            cancelBtn.setTextSize(11.5f);
            confirmBtn.setTypeface(Utils.getSpecificTypeface(getContext(), appCMSPresenter, getString(R.string.app_cms_page_font_extrabold_key)));
            cancelBtn.setTypeface(Utils.getSpecificTypeface(getContext(), appCMSPresenter, getString(R.string.app_cms_page_font_extrabold_key)));
            pinMsgTitle.setTypeface(Utils.getSpecificTypeface(getContext(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));
            pinMsg.setTypeface(Utils.getSpecificTypeface(getContext(), appCMSPresenter, getString(R.string.app_cms_page_font_regular_key)));
            pinError.setTypeface(Utils.getSpecificTypeface(getContext(), appCMSPresenter, getString(R.string.app_cms_page_font_regular_key)));
        }

        confirmBtn.setBackground(Utils.setButtonBackgroundSelector(getContext(),
                    Color.parseColor(Utils.getFocusColor(getContext(), appCMSPresenter)), null, appCMSPresenter));
        cancelBtn.setBackground(Utils.setButtonBackgroundSelector(getContext(),
                    Color.parseColor(Utils.getFocusColor(getContext(), appCMSPresenter)), null, appCMSPresenter));

       /* pinView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_PREVIOUS || actionId == EditorInfo.IME_ACTION_NEXT) {
                appCMSPresenter.closeSoftKeyboardNoView();
                return true;
            }
            return false;
        });*/
    }


}


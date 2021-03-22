package com.viewlift.tv.views.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.AsteriskPasswordTransformation;

import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import rx.functions.Action1;

/**
 * Created by anas.azeem on 9/13/2017.
 * Owned by ViewLift, NYC
 */

public class AccountDetailsEditDialogFragment extends AbsDialogFragment {

    public static final String DIALOG_HEIGHT_KEY = "dialog_height_key";
    public static final String DIALOG_WIDTH_KEY = "dialog_width_key";
    public static final String DIALOG_TITLE_KEY = "dialog_title_key";
    public static final String DIALOG_MESSAGE_KEY = "dialog_message_key";
    public static final String DIALOG_USER_EMAIL_KEY = "dialog_email_key";
    public static final String DIALOG_TITLE_SIZE_KEY = "dialog_title_size_key";
    public static final String DIALOG_MESSAGE__SIZE_KEY = "dialog_message_size_key";
    public static final String DIALOG_TITLE_TEXT_COLOR_KEY = "dialog_title_text_color_key";
    public static final String DIALOG_MESSAGE_TEXT_COLOR_KEY = "dialog_message_text_color_key";
    public static final String DIALOG_POSITIVE_BUTTON_TEXT_KEY = "dialog_positive_button_text_key";
    public static final String DIALOG_NEGATIVE_BUTTON_TEXT_KEY = "dialog_negative_button_text_key";
    public static final String DIALOG_NEUTRAL_BUTTON_TEXT_KEY = "dialog_neutral_button_text_key";
    public static final String DIALOG_TITLE_VISIBILITY_KEY = "dialog_title_visibility_key";
    public static final String DIALOG_MESSAGE_VISIBILITY_KEY = "dialog_message_visibility_key";
    public static final String DIALOG_POSITIVE_BUTTON_VISIBILITY_KEY
            = "dialog_positive_button_visibility_key";
    public static final String DIALOG_NEGATIVE_BUTTON_VISIBILITY_KEY
            = "dialog_negative_button_visibility_key";
    public static final String DIALOG_BACKGROUND_COLOR = "dialog_background_color";
    public static final String DIALOG_TYPE_TEXT_KEY = "dialog_type_text_key";
    private Action1<String> onPositiveButtonClicked;
    private Action1<String> onNegativeButtonClicked;
    private Action1<String> onNeutralButtonClicked;
    private Action1<String> onBackKeyListener;

    private boolean isFocusOnPositiveButton = false;
    private boolean isFocusOnNegativeButton = false;
    private boolean isFocusOnNeutralButton = false;
    private Button positiveButton;
    private Button negativeButton;
    private Button neutralButton;
    private String userName = null;
    private String userEmail = null;
    private EditText editText, editTextSecond,editTextThird;
    private TextView tvTitle;
    private AppCMSPresenter appCMSPresenter;

    public AccountDetailsEditDialogFragment() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.blurDialog);
    }

    public static AccountDetailsEditDialogFragment newInstance(Bundle bundle) {
        AccountDetailsEditDialogFragment fragment = new AccountDetailsEditDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    public void setOnPositiveButtonClicked (Action1<String> onPositiveButtonClicked){
        this.onPositiveButtonClicked = onPositiveButtonClicked;
    }

    public void setOnNegativeButtonClicked (Action1<String> onNegativeButtonClicked){
        this.onNegativeButtonClicked = onNegativeButtonClicked;
    }

    public void setOnNeutralButtonClicked (Action1<String> onNeutralButtonClicked){
        this.onNeutralButtonClicked = onNeutralButtonClicked;
    }

    public void setOnBackKeyListener(Action1<String> onBackKeyListener){
       this.onBackKeyListener = onBackKeyListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.edit_account_details_dialog, container, false);

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        String backGroundColor = appCMSPresenter.getAppBackgroundColor();

        mView.setFocusable(false);
        /*Bind Views*/
        negativeButton = mView.findViewById(R.id.btn_cancel);
        positiveButton = mView.findViewById(R.id.btn_yes);
        neutralButton = mView.findViewById(R.id.btn_neutral);

        tvTitle = mView.findViewById(R.id.text_overlay_title);
        tvTitle.setFocusable(false);
        tvTitle.setEnabled(false);
        tvTitle.setClickable(false);
        tvTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));

        editText = mView.findViewById(R.id.edit_text);
        editTextSecond = mView.findViewById(R.id.edit_text_second);
        editTextThird = mView.findViewById(R.id.edit_text_third);

        /*Request focus on the userName */
        editText.requestFocus();
        Bundle arguments = getArguments();
        String title = arguments.getString(DIALOG_TITLE_KEY, null);
        userName = arguments.getString(DIALOG_MESSAGE_KEY, null);
        userEmail = arguments.getString(DIALOG_USER_EMAIL_KEY, null);
        String textColor = arguments.getString(DIALOG_MESSAGE_TEXT_COLOR_KEY, null);
        String positiveBtnText = arguments.getString(DIALOG_POSITIVE_BUTTON_TEXT_KEY,null);
        String negativeBtnText = arguments.getString(DIALOG_NEGATIVE_BUTTON_TEXT_KEY, null);
        String neutralBtnText = arguments.getString(DIALOG_NEUTRAL_BUTTON_TEXT_KEY, null);
        String dialogType = arguments.getString(DIALOG_TYPE_TEXT_KEY, null);
        if(arguments.getString(DIALOG_BACKGROUND_COLOR, null) != null)
        backGroundColor = arguments.getString(DIALOG_BACKGROUND_COLOR, null);
        mView.findViewById(R.id.fragment_clear_overlay).setBackgroundColor(Color.parseColor(backGroundColor));
        float messageSize = arguments.getFloat(DIALOG_MESSAGE__SIZE_KEY);

        positiveButton.setText(positiveBtnText);
        negativeButton.setText(negativeBtnText);
        neutralButton.setText(neutralBtnText);

        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);
        editTextSecond.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);
        editTextThird.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);

        if(dialogType.equals(getString(R.string.app_cms_page_setting_account_password_container_view))){
            editTextSecond.setVisibility(View.VISIBLE);
            editTextThird.setVisibility(View.VISIBLE);

            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getPasswordPopupCurrentPassword() != null)
                editText.setHint(appCMSPresenter.getModuleApi().getMetadataMap().getPasswordPopupCurrentPassword());
            else
                editText.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_old_password_input_text_hint)));

            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getPasswordPopupNewPassword() != null)
                editTextSecond.setHint(appCMSPresenter.getModuleApi().getMetadataMap().getPasswordPopupNewPassword());
            else
                editTextSecond.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_new_password_input_text_hint)));

            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getPasswordPopupConfirmPassword() != null)
                editTextThird.setHint(appCMSPresenter.getModuleApi().getMetadataMap().getPasswordPopupConfirmPassword());
            else
                editTextThird.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_new_password_input_text_hint)));

            editText.setTransformationMethod(new AsteriskPasswordTransformation());
            editTextSecond.setTransformationMethod(new AsteriskPasswordTransformation());
            editTextThird.setTransformationMethod(new AsteriskPasswordTransformation());

        }else if(dialogType.equals(getString(R.string.app_cms_page_setting_account_email_container_view))){
            editText.setHint(getString(R.string.enter_email));
            editText.setText(userEmail);
        }else if(dialogType.equals(getString(R.string.app_cms_page_setting_account_name_container_view))){
            editText.setHint(getString(R.string.enter_name));
            editText.setText(userName);
        }else if (dialogType != null && dialogType.equals(getResources().getString(R.string.app_cms_page_dialog_type_email_update))){
            editText.setHint(getString(R.string.enter_email));
        }

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        textColor = appCMSPresenter.getAppTextColor();

        editText.setTextColor(Color.parseColor(textColor));
        editTextSecond.setTextColor(Color.parseColor(textColor));
        editTextThird.setTextColor(Color.parseColor(textColor));

        editText.setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter,Color.parseColor(backGroundColor),Color.parseColor(textColor),2,8));
        editTextSecond.setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter,Color.parseColor(backGroundColor),Color.parseColor(textColor),2,8));
        editTextThird.setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter,Color.parseColor(backGroundColor),Color.parseColor(textColor),2,8));

        tvTitle.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_semibold_key)));
        editText.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));
        editTextSecond.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));
        editTextThird.setTypeface(Utils.getSpecificTypeface(getActivity(), appCMSPresenter, getString(R.string.app_cms_page_font_bold_key)));
        //tvDescription.setTextSize(R.dimen.text_ovelay_dialog_desc_font_size);

        Component btnComponent1 = new Component();
        btnComponent1.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        btnComponent1.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        btnComponent1.setCornerRadius(4);
        btnComponent1.setBorderColor(CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                R.color.btn_color_with_opacity))));
        btnComponent1.setBorderWidth(4);

        neutralButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        neutralButton.setTextColor(Color.WHITE);


        neutralButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, btnComponent1));

        negativeButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        negativeButton.setTextColor(Color.WHITE);


        Typeface semiBoldTypeface = Utils.getSpecificTypeface(getActivity(),
                appCMSPresenter,
                getString(R.string.app_cms_page_font_semibold_key));
        negativeButton.setTypeface(semiBoldTypeface);

        positiveButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        positiveButton.setTextColor(Color.WHITE);


        positiveButton.setTypeface(semiBoldTypeface);

        //positiveButton.requestFocus();

        if(TextUtils.isEmpty(positiveBtnText)){
            positiveButton.setVisibility(View.GONE);
            //negativeButton.requestFocus();
        }

        if(TextUtils.isEmpty(negativeBtnText)){
            negativeButton.setVisibility(View.GONE);
            //positiveButton.requestFocus();
        }

        if(TextUtils.isEmpty(neutralBtnText)){
            neutralButton.setVisibility(View.GONE);
            //positiveButton.requestFocus();
        }

        if(TextUtils.isEmpty(positiveBtnText) && TextUtils.isEmpty(negativeBtnText)){
            //neutralButton.requestFocus();
        }
        /*Set click listener*/
        negativeButton.setOnClickListener(v -> {
            if(null != onNegativeButtonClicked) {
                onNegativeButtonClicked.call("");
            }
            dismiss();
        });


        positiveButton.setOnClickListener(v -> {
            if (dialogType != null && dialogType.equals(getResources().getString(R.string.app_cms_page_setting_account_name_container_view))) {
                validateUserName();
            } else if (dialogType != null && dialogType.equals(getResources().getString(R.string.app_cms_page_setting_account_email_container_view))){
                validateUserEmail();
            } else if (dialogType != null && dialogType.equals(getResources().getString(R.string.app_cms_page_setting_account_password_container_view))){
                validateAndUpdatePassword(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean result) {
                        if(result) dismiss();
                    }
                });

            }else if (dialogType != null && dialogType.equals(getResources().getString(R.string.app_cms_page_dialog_type_email_update))){
                dismiss();
                validateAndUpdateUserEmail();
            }
            if(null != onPositiveButtonClicked) {
                onPositiveButtonClicked.call("");
            }
        });

        neutralButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                appCMSPresenter.showToast(appCMSPresenter.getLocalisedStrings().getEmptyPasswordValidationText(), Toast.LENGTH_SHORT);
                return;
            }
            appCMSPresenter.updateUserProfile(userName,
                    userEmail,
                    editText.getText().toString().trim(),
                    userIdentity -> {
                        if(null != onNeutralButtonClicked) {
                            onNeutralButtonClicked.call(userName);
                            dismiss();
                        }
                    }, appCMSPresenter.getModuleApi());

        });

        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if(keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN){
                if(null != onBackKeyListener)
                    onBackKeyListener.call("");
            }
            return false;
        });

        return mView;
    }

    public void validateUserName() {
        userName = editText.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || !doesValidNameExist(userName)) {
            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getInvalidNameMsg() != null)
                Toast.makeText(getContext(), appCMSPresenter.getModuleApi().getMetadataMap().getInvalidNameMsg(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.edit_profile_name_message)), Toast.LENGTH_LONG).show();
            return;
        }
        if (userName.length() < 3) {
            Toast.makeText(getContext(), appCMSPresenter.getLocalisedStrings().getNameValidationText(), Toast.LENGTH_LONG).show();
            return;
        }
        openVerifyPasswordDialog();
    }

    public void validateUserEmail() {
        userEmail = editText.getText().toString().trim();
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(getContext(), appCMSPresenter.getLocalisedStrings().getEmptyEmailValidationText(), Toast.LENGTH_LONG).show();
            return;
        }
        if (!appCMSPresenter.isValidEmail(userEmail)) {
            Toast.makeText(getContext(), appCMSPresenter.getLocalisedStrings().getEmailFormatValidationMsg(), Toast.LENGTH_LONG).show();
            return;
        }
        openVerifyPasswordDialog();
    }

    public void validateAndUpdatePassword(Action1<Boolean> resultAction) {
        String oldPassword = editText.getText().toString().trim();
        String newPassword = editTextSecond.getText().toString().trim();
        String confirmPassword = editTextThird.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null &&
                    appCMSPresenter.getModuleApi().getMetadataMap().getkFillDetails() != null) {
                appCMSPresenter.showToast(appCMSPresenter.getModuleApi().getMetadataMap().getkFillDetails(), Toast.LENGTH_LONG);
                return;
            }
        }
        if (!newPassword.equalsIgnoreCase(confirmPassword)) {
            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null &&
                    appCMSPresenter.getModuleApi().getMetadataMap().getPasswordMismatchError() != null) {
                appCMSPresenter.showToast(appCMSPresenter.getModuleApi().getMetadataMap().getPasswordMismatchError(), Toast.LENGTH_LONG);
                return;
            }
        }

        if (oldPassword.equalsIgnoreCase(newPassword) || oldPassword.equalsIgnoreCase(confirmPassword)) {
            if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null &&
                    appCMSPresenter.getModuleApi().getMetadataMap().getMISMATCH_PASSWORD() != null) {
                appCMSPresenter.showToast(appCMSPresenter.getModuleApi().getMetadataMap().getMISMATCH_PASSWORD(), Toast.LENGTH_LONG);
                return;
            }
        }
        appCMSPresenter.closeSoftKeyboard();
        appCMSPresenter.updateUserPassword(oldPassword, newPassword, appCMSPresenter.getModuleApi(), new Action1<Boolean>() {
            @Override
            public void call(Boolean result) {
                    resultAction.call(result);
            }
        });
    }

    public void validateAndUpdateUserEmail() {
        userEmail = editText.getText().toString().trim();
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(getContext(), appCMSPresenter.getLocalisedStrings().getEmptyEmailValidationText(), Toast.LENGTH_LONG).show();
            return;
        }
        if (!appCMSPresenter.isValidEmail(userEmail)) {
            Toast.makeText(getContext(), appCMSPresenter.getLocalisedStrings().getEmailFormatValidationMsg(), Toast.LENGTH_LONG).show();
            return;
        }
        appCMSPresenter.updateUserEmail(userEmail);
    }

    private void openVerifyPasswordDialog() {
        editText.getText().clear();
        editText.setHint(getResources().getString(R.string.enter_password));
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getVerifyPasswordPopUpTitle() != null)
            tvTitle.setText(appCMSPresenter.getModuleApi().getMetadataMap().getVerifyPasswordPopUpTitle());
        else
            tvTitle.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.verify_your_password)));

        String positiveText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.button_text_proceed));
        if (appCMSPresenter.getModuleApi() != null && appCMSPresenter.getModuleApi().getMetadataMap() != null && appCMSPresenter.getModuleApi().getMetadataMap().getCancelYesLabel() != null)
            positiveText = appCMSPresenter.getModuleApi().getMetadataMap().getCancelYesLabel();


        positiveButton.setVisibility(View.GONE);
        neutralButton.setVisibility(View.VISIBLE);
        neutralButton.setText(positiveText);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        int width = getArguments().getInt(DIALOG_WIDTH_KEY);
        int height = getArguments().getInt(DIALOG_HEIGHT_KEY);
        bundle.putInt(getString(R.string.tv_dialog_width_key), width);
        bundle.putInt(getString(R.string.tv_dialog_height_key), height);

        super.onActivityCreated(bundle);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (negativeButton != null && negativeButton.hasFocus()){
            isFocusOnNegativeButton = true;
        } else if (positiveButton != null && positiveButton.hasFocus()) {
            isFocusOnPositiveButton = true;
        }else if (neutralButton != null && neutralButton.hasFocus()) {
            isFocusOnNeutralButton = true;
        }
        Log.d("ANSA onPause" , "isFocusOnPositiveButton = "+isFocusOnPositiveButton);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean doesValidNameExist(String input) {
        String regex = "[a-zA-Z\\s]+";
        return !TextUtils.isEmpty(input) && Pattern.matches(regex, input);
    }
}

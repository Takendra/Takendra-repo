package com.viewlift.views.fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.AsteriskPasswordTransformation;
import com.viewlift.views.customviews.InputView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.CustomShape;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/*
 * Created by viewlift on 7/27/17.
 */

public class AppCMSEditProfileFragment extends DialogFragment {

    @BindView(R.id.app_cms_edit_profile_page_title)
    AppCompatTextView titleTextView;

    @BindView(R.id.name)
    AppCompatEditText name;

    @BindView(R.id.email)
    AppCompatEditText email;

    @BindView(R.id.edit_profile_confirm_change_button)
    AppCompatButton editProfileConfirmChangeButton;

    @BindView(R.id.app_cms_edit_profile_main_layout)
    ConstraintLayout appCMSEditProfileMainLayout;
    @BindView(R.id.emailContainer)
    ConstraintLayout emailContainer;
    @BindView(R.id.nameContainer)
    ConstraintLayout nameContainer;

    @BindView(R.id.nameTitle)
    AppCompatTextView nameTitle;

    @BindView(R.id.emailTitle)
    AppCompatTextView emailTitle;

    private AppCMSBinder appCMSBinder;
    Module module;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    Unbinder unbinder;

    public static AppCMSEditProfileFragment newInstance(Context context,
                                                        String username,
                                                        String email, AppCMSBinder appCMSBinder) {
        AppCMSEditProfileFragment fragment = new AppCMSEditProfileFragment();
        Bundle args = new Bundle();
        args.putString(context.getString(R.string.app_cms_edit_profile_username_key), username);
        args.putString(context.getString(R.string.app_cms_password_reset_email_key), email);
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceSxtate) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        Bundle args = getArguments();
        try {
            appCMSBinder =
                    ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appCMSBinder.getAppCMSPageUI() != null) {
            module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_01)), appCMSBinder.getAppCMSPageAPI());
            if (module == null)
                module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_02)), appCMSBinder.getAppCMSPageAPI());
            if (module == null)
                module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_03)), appCMSBinder.getAppCMSPageAPI());
            if (module == null)
                module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_04)), appCMSBinder.getAppCMSPageAPI());
        }

        if (appCMSPresenter.getAppPreference().getLoggedInUserName() != null)
            name.setText(appCMSPresenter.getAppPreference().getLoggedInUserName());
        if (appCMSPresenter.getAppPreference().getLoggedInUserEmail() != null)
            email.setText(appCMSPresenter.getAppPreference().getLoggedInUserEmail());


        setViewStyles();
        setData();
        return view;
    }

    private void setViewStyles() {
        int appTextColor = appCMSPresenter.getGeneralTextColor();
        int appBackgroundColor = appCMSPresenter.getGeneralBackgroundColor();
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        titleTextView.setTextColor(appTextColor);
        titleTextView.setTypeface(appCMSPresenter.getBoldTypeFace());
        editProfileConfirmChangeButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        editProfileConfirmChangeButton.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        appCMSEditProfileMainLayout.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, nameContainer, rectangleStroke, appTextColor);
        name.setTextColor(appTextColor);
        name.setHintTextColor(appTextColor);
        nameTitle.setTextColor(appTextColor);
        nameTitle.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, emailContainer, rectangleStroke, appTextColor);
        email.setTextColor(appTextColor);
        email.setHintTextColor(appTextColor);
        emailTitle.setTextColor(appTextColor);
        emailTitle.setBackgroundColor(appBackgroundColor);

        appCMSPresenter.setCursorDrawableColor(name, 0);
        appCMSPresenter.setCursorDrawableColor(email, 0);
    }

    private void setData() {
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getProfilePopupUpdate() != null)
            editProfileConfirmChangeButton.setText(module.getMetadataMap().getProfilePopupUpdate());
        else
            editProfileConfirmChangeButton.setText(getString(R.string.edit_profile_confirm_change_button_text));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getProfilePopupText() != null)
            titleTextView.setText(module.getMetadataMap().getProfilePopupText());
        else
            titleTextView.setText(getString(R.string.app_cms_edit_profile_dialog_title));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getProfilePopupName() != null)
            nameTitle.setText(module.getMetadataMap().getProfilePopupName());
        else
            nameTitle.setText(getString(R.string.app_cms_username_input_text_hint));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getProfilePopupEmail() != null)
            emailTitle.setText(module.getMetadataMap().getProfilePopupEmail());
        else
            emailTitle.setText(getString(R.string.app_cms_email_input_text_hint));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick(R.id.edit_profile_confirm_change_button)
    void updateProfileClick() {
        String userName = name.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || !doesValidNameExist(userName)) {
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getInvalidNameMsg() != null)
                Toast.makeText(getContext(), module.getMetadataMap().getInvalidNameMsg(), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), getString(R.string.edit_profile_name_message), Toast.LENGTH_LONG).show();
            return;
        }
        if (userName.length() < 3) {
            Toast.makeText(getContext(), localisedStrings.getNameValidationText(), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(getContext(), localisedStrings.getEmptyEmailValidationText(), Toast.LENGTH_LONG).show();
            return;
        }
        if (!appCMSPresenter.isValidEmail(email.getText().toString())) {
            Toast.makeText(getContext(), localisedStrings.getEmailFormatValidationMsg(), Toast.LENGTH_LONG).show();
            return;
        }
        if (appCMSPresenter.getAppPreference() != null && appCMSPresenter.getAppPreference().getLoginType()!=null && appCMSPresenter.getAppPreference().getLoginType().equalsIgnoreCase(appCMSPresenter.getCurrentActivity().getString(R.string.login_type_otp))) {
            appCMSPresenter.showLoader();
            appCMSPresenter.phoneObjectRequest.setScreenName("update");
            appCMSPresenter.phoneObjectRequest.setRequestType("send");
            appCMSPresenter.phoneObjectRequest.setFragmentName("appCMSEditProfileFragment");
            appCMSPresenter.phoneObjectRequest.setEmail(email.getText().toString());
            appCMSPresenter.phoneObjectRequest.setName(userName);
            appCMSPresenter.phoneObjectRequest.setPhone(appCMSPresenter.getAppPreference().getLoggedInUserPhone());
            appCMSPresenter.sendPhoneOTP(appCMSPresenter.phoneObjectRequest, s -> {
                if (s.equalsIgnoreCase("dismiss")) {
                    appCMSPresenter.sendCloseOthersAction(null,
                            true,
                            false);
                }
            });
        } else {

            InputView inputView = new InputView(getContext(), appCMSPresenter, AppCMSUIKeyType.PAGE_PASSWORDTEXTFIELD_KEY, null);
            TextInputEditText password = inputView.getEditText();
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            password.setImeOptions(EditorInfo.IME_ACTION_DONE);
            password.setTransformationMethod(new AsteriskPasswordTransformation());
            inputView.setPadding(30, 0, 30, 0);

            int appTextColor = appCMSPresenter.getGeneralTextColor();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(inputView);
            builder.setCancelable(false);
            String title = "";
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getVerifyPasswordPopUpTitle() != null) {
                title = module.getMetadataMap().getVerifyPasswordPopUpTitle();
            } else {
                title = getResources().getString(R.string.verify_your_password);
            }

            builder.setTitle(Html.fromHtml(getActivity().getString(R.string.text_with_color,
                    Integer.toHexString(appTextColor).substring(2),
                    title)));
            String positveText = getString(R.string.button_text_proceed);
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getCancelYesLabel() != null)
                positveText = module.getMetadataMap().getCancelYesLabel();

            String negativeText = getString(R.string.app_cms_cancel_alert_dialog_button_text);
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getCancelNoLabel() != null)
                negativeText = module.getMetadataMap().getCancelNoLabel();

            builder.setPositiveButton(
                    positveText,
                    (dialog, id) -> {
                        appCMSPresenter.closeSoftKeyboardNoView();
                        if (TextUtils.isEmpty(password.getText().toString())) {
                            appCMSPresenter.showToast(localisedStrings.getEmptyPasswordValidationText(), Toast.LENGTH_SHORT);
                            return;
                        }
                        appCMSPresenter.updateUserProfile(userName,
                                email.getText().toString(),
                                password.getText().toString(),
                                userIdentity -> {
                                    //
                                }, module);
                    });

            builder.setNegativeButton(
                    negativeText,
                    (dialog, id) -> {
                        dialog.cancel();
                        appCMSPresenter.sendCloseOthersAction(null,
                                true,
                                false);
                        appCMSPresenter.closeSoftKeyboardNoView();
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
            Component component = new Component();
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, ((TextView) dialog.findViewById(android.R.id.message)));
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, ((TextView) dialog.findViewById(android.R.id.title)));
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, dialog.getButton(AlertDialog.BUTTON_NEGATIVE));
            ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, dialog.getButton(AlertDialog.BUTTON_POSITIVE));
            setFont();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        appCMSPresenter.getGeneralBackgroundColor()));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(appTextColor);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(appTextColor);
            }
        }

    }


    private static void setCursorColor(AppCompatEditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = AppCompatTextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = AppCompatTextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    private boolean doesValidNameExist(String input) {
        String regex = "[a-zA-Z\\s]+";
        return !TextUtils.isEmpty(input) && Pattern.matches(regex, input);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }

    }

    private void setFont() {
        Component component = new Component();
        component.setFontWeight(getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, titleTextView);
        component.setFontWeight(null);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, name);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, nameTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, email);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, emailTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, editProfileConfirmChangeButton);
    }
}

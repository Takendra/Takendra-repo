package com.viewlift.views.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.AsteriskPasswordTransformation;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.dialog.CustomShape;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AppCMSChangePasswordFragment extends Fragment {

    @BindView(R.id.app_cms_change_password_main_layout)
    RelativeLayout appCMSChangePasswordMainLayout;

    @BindView(R.id.pageTitle)
    AppCompatTextView pageTitle;

    @BindView(R.id.oldPassword)
    TextInputEditText oldPasswordInput;
    @BindView(R.id.oldPasswordInputLayout)
    TextInputLayout oldPasswordInputLayout;
    @BindView(R.id.oldPasswordTitle)
    AppCompatTextView oldPasswordTitle;
    @BindView(R.id.oldPasswordContainer)
    ConstraintLayout oldPasswordContainer;

    @BindView(R.id.newPassword)
    AppCompatEditText newPasswordInput;
    @BindView(R.id.newPasswordInputLayout)
    TextInputLayout newPasswordInputLayout;
    @BindView(R.id.newPasswordTitle)
    AppCompatTextView newPasswordTitle;
    @BindView(R.id.newPasswordContainer)
    ConstraintLayout newPasswordContainer;

    @BindView(R.id.confirmPassword)
    AppCompatEditText confirmPasswordInput;
    @BindView(R.id.confirmPasswordInputLayout)
    TextInputLayout confirmPasswordInputLayout;
    @BindView(R.id.confirmPasswordTitle)
    AppCompatTextView confirmPasswordTitle;
    @BindView(R.id.confirmPasswordContainer)
    ConstraintLayout confirmPasswordContainer;

    @BindView(R.id.addPassword)
    AppCompatEditText addPassword;
    @BindView(R.id.addPasswordInputLayout)
    TextInputLayout addPasswordInputLayout;
    @BindView(R.id.addPasswordTitle)
    AppCompatTextView addPasswordTitle;
    @BindView(R.id.addPasswordContainer)
    ConstraintLayout addPasswordContainer;
    @BindView(R.id.confirmNewPassword)
    AppCompatEditText confirmNewPassword;
    @BindView(R.id.confirmNewPasswordInputLayout)
    TextInputLayout confirmNewPasswordInputLayout;
    @BindView(R.id.confirmNewPasswordTitle)
    AppCompatTextView confirmNewPasswordTitle;
    @BindView(R.id.confirmNewPasswordContainer)
    ConstraintLayout confirmNewPasswordContainer;

    @BindView(R.id.changePasswordContainer)
    LinearLayoutCompat changePasswordContainer;

    @BindView(R.id.addPasswordView)
    LinearLayoutCompat addPasswordView;

    @BindView(R.id.app_cms_change_password_button)
    AppCompatButton confirmPasswordButton;
    @BindView(R.id.setUpPassword)
    AppCompatButton setUpPassword;
    Module module;
    Unbinder unbinder;
    AppCMSPresenter appCMSPresenter;

    public static AppCMSChangePasswordFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        AppCMSChangePasswordFragment fragment = new AppCMSChangePasswordFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        ButterKnife.bind(this, view);

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        try {
            AppCMSBinder appCMSBinder = ((AppCMSBinder) getArguments().getBinder(getString(R.string.fragment_page_bundle_key)));
            if (appCMSBinder != null && appCMSBinder.getAppCMSPageUI() != null) {
                module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_01)), appCMSBinder.getAppCMSPageAPI());
                if (module == null) {
                    module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_02)), appCMSBinder.getAppCMSPageAPI());
                }
                if (module == null) {
                    module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_03)), appCMSBinder.getAppCMSPageAPI());
                }
                if (module == null) {
                    module = appCMSPresenter.matchModuleAPIToModuleUI(appCMSPresenter.getRelatedModuleForBlock(appCMSBinder.getAppCMSPageUI().getModuleList(), getString(R.string.ui_block_user_management_04)), appCMSBinder.getAppCMSPageAPI());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (appCMSPresenter.getAppPreference().isSetUserPassword()) {
            changePasswordContainer.setVisibility(View.VISIBLE);
            addPasswordView.setVisibility(View.GONE);
        } else {
            changePasswordContainer.setVisibility(View.GONE);
            addPasswordView.setVisibility(View.VISIBLE);
        }
        setViewStyles();
        setData();
        return view;
    }

    private void setData() {
        String title = getString(R.string.app_cms_change_password_page_title);
        if (appCMSPresenter.getAppPreference().isSetUserPassword()) {
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getPasswordPopupHeader() != null)
                title = module.getMetadataMap().getPasswordPopupHeader();
        } else {
            title = getString(R.string.app_cms_create_password_page_title);
            if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getCreatePassword() != null)
                title = module.getMetadataMap().getCreatePassword();
        }

        pageTitle.setText(title);

        pageTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor()));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getPasswordPopupCurrentPassword() != null)
            oldPasswordInput.setHint(module.getMetadataMap().getPasswordPopupCurrentPassword());
        else
            oldPasswordInput.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_old_password_input_text_hint)));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getPasswordPopupNewPassword() != null)
            newPasswordInput.setHint(module.getMetadataMap().getPasswordPopupNewPassword());
        else
            newPasswordInput.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_new_password_input_text_hint)));
        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getPasswordPopupConfirmPassword() != null)
            confirmPasswordInput.setHint(module.getMetadataMap().getPasswordPopupConfirmPassword());
        else
            confirmPasswordInput.setHint(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_new_password_input_text_hint)));

        if (module != null && module.getMetadataMap() != null && module.getMetadataMap().getPasswordPopupUpdate() != null) {
            confirmPasswordButton.setText(module.getMetadataMap().getPasswordPopupUpdate());
            setUpPassword.setText(module.getMetadataMap().getPasswordPopupUpdate());
        } else {
            confirmPasswordButton.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_confirm_password_button_text)));
            setUpPassword.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.app_cms_confirm_password_button_text)));
        }

    }

    private void setViewStyles() {
        int appTextColor = Color.parseColor(appCMSPresenter.getAppTextColor());
        int appBackgroundColor = Color.parseColor(appCMSPresenter.getAppBackgroundColor());
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {appCMSPresenter.getBrandPrimaryCtaColor(), appTextColor};
        appCMSChangePasswordMainLayout.setBackgroundColor(appBackgroundColor);
        pageTitle.setTextColor(appTextColor);
        pageTitle.setTypeface(appCMSPresenter.getBoldTypeFace());
        confirmPasswordButton.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        confirmPasswordButton.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        setUpPassword.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        setUpPassword.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());

        CustomShape.makeRoundCorner(transparentColor, cornerRadius, oldPasswordContainer, rectangleStroke, appTextColor);
        oldPasswordInput.setTextColor(appTextColor);
        oldPasswordInput.setHintTextColor(appTextColor);
        oldPasswordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        oldPasswordTitle.setTextColor(appTextColor);
        oldPasswordTitle.setBackgroundColor(appBackgroundColor);

        CustomShape.makeRoundCorner(transparentColor, cornerRadius, newPasswordContainer, rectangleStroke, appTextColor);
        newPasswordInput.setTextColor(appTextColor);
        newPasswordInput.setHintTextColor(appTextColor);
        newPasswordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        newPasswordTitle.setTextColor(appTextColor);
        newPasswordTitle.setBackgroundColor(appBackgroundColor);

        CustomShape.makeRoundCorner(transparentColor, cornerRadius, confirmPasswordContainer, rectangleStroke, appTextColor);
        confirmPasswordInput.setTextColor(appTextColor);
        confirmPasswordInput.setHintTextColor(appTextColor);
        confirmPasswordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        confirmPasswordTitle.setTextColor(appTextColor);
        confirmPasswordTitle.setBackgroundColor(appBackgroundColor);

        CustomShape.makeRoundCorner(transparentColor, cornerRadius, addPasswordContainer, rectangleStroke, appTextColor);
        addPassword.setTextColor(appTextColor);
        addPassword.setHintTextColor(appTextColor);
        addPasswordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        addPasswordTitle.setTextColor(appTextColor);
        addPasswordTitle.setBackgroundColor(appBackgroundColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, confirmNewPasswordContainer, rectangleStroke, appTextColor);
        confirmNewPassword.setTextColor(appTextColor);
        confirmNewPassword.setHintTextColor(appTextColor);
        confirmNewPasswordInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        confirmNewPasswordTitle.setTextColor(appTextColor);
        confirmNewPasswordTitle.setBackgroundColor(appBackgroundColor);

        oldPasswordInput.setTransformationMethod(new AsteriskPasswordTransformation());
        newPasswordInput.setTransformationMethod(new AsteriskPasswordTransformation());
        confirmPasswordInput.setTransformationMethod(new AsteriskPasswordTransformation());
        addPassword.setTransformationMethod(new AsteriskPasswordTransformation());
        confirmNewPassword.setTransformationMethod(new AsteriskPasswordTransformation());


        appCMSPresenter.noSpaceInEditTextFilter(oldPasswordInput, getActivity());
        appCMSPresenter.noSpaceInEditTextFilter(newPasswordInput, getActivity());
        appCMSPresenter.noSpaceInEditTextFilter(confirmPasswordInput, getActivity());
        appCMSPresenter.noSpaceInEditTextFilter(addPassword, getActivity());
        appCMSPresenter.noSpaceInEditTextFilter(confirmNewPassword, getActivity());

    }

    @OnClick(R.id.setUpPassword)
    void setUpPasswordClick() {
        appCMSPresenter.closeSoftKeyboard();

        if (TextUtils.isEmpty(addPassword.getText().toString()) || TextUtils.isEmpty(confirmNewPassword.getText().toString())) {
            if (module != null && module.getMetadataMap() != null &&
                    module.getMetadataMap().getkFillDetails() != null) {
                appCMSPresenter.showToast(module.getMetadataMap().getkFillDetails(), Toast.LENGTH_LONG);
                return;
            }
        }
        if (!addPassword.getText().toString().equalsIgnoreCase(confirmNewPassword.getText().toString()) && module != null && module.getMetadataMap() != null &&
                module.getMetadataMap().getMISMATCH_PASSWORD() != null) {
            appCMSPresenter.showToast(module.getMetadataMap().getMISMATCH_PASSWORD(), Toast.LENGTH_LONG);
            return;
        }
        appCMSPresenter.addPassword(addPassword.getText().toString(), module);
    }

    @OnClick(R.id.app_cms_change_password_button)
    void changePasswordClick() {
        String oldPassword = oldPasswordInput.getText().toString().trim();
        String newPassword = newPasswordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            if (module != null && module.getMetadataMap() != null &&
                    module.getMetadataMap().getkFillDetails() != null) {
                appCMSPresenter.showToast(module.getMetadataMap().getkFillDetails(), Toast.LENGTH_LONG);
                return;
            }
        }
        if (!newPassword.equalsIgnoreCase(confirmPassword)) {
            if (module != null && module.getMetadataMap() != null &&
                    module.getMetadataMap().getPasswordMismatchError() != null) {
                appCMSPresenter.showToast(module.getMetadataMap().getPasswordMismatchError(), Toast.LENGTH_LONG);
                return;
            }
        }
        if (oldPassword.equalsIgnoreCase(newPassword) || oldPassword.equalsIgnoreCase(confirmPassword)) {
            if (module != null && module.getMetadataMap() != null &&
                    module.getMetadataMap().getMISMATCH_PASSWORD() != null) {
                appCMSPresenter.showToast(module.getMetadataMap().getMISMATCH_PASSWORD(), Toast.LENGTH_LONG);
                return;
            }
        }
        appCMSPresenter.closeSoftKeyboard();
        appCMSPresenter.updateUserPassword(oldPassword, newPassword, module, null);
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
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, pageTitle);
        component.setFontWeight(null);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, oldPasswordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, oldPasswordInput);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, newPasswordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, newPasswordInput);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmPasswordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmPasswordInput);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmPasswordButton);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, addPassword);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, addPasswordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmNewPassword);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, confirmNewPasswordTitle);
        ViewCreator.setTypeFace(getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, setUpPassword);
    }
}

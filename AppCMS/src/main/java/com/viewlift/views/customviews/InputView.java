package com.viewlift.views.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.viewlift.R;
import com.viewlift.models.data.appcms.CountryCodeData;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.adapters.CountryAdapter;
import com.viewlift.views.dialog.CustomShape;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputView extends ConstraintLayout {
    Context mContext;


    @BindView(R.id.textInputContainer)
    ConstraintLayout textInputContainer;

    @BindView(R.id.textInputLayoutCountryCode)
    TextInputLayout textInputLayoutCountryCode;

    @BindView(R.id.textInputLayout)
    TextInputLayout textInputLayout;

    @BindView(R.id.textInputEditField)
    TextInputEditText textInputEditField;

    @BindView(R.id.textInputEditFieldCountryCode)
    TextInputEditText textInputEditFieldCountryCode;

    @BindView(R.id.separatorViewCountry)
    View separatorViewCountry;

    @BindView(R.id.textViewTitle)
    AppCompatTextView textViewTitle;

    AppCMSPresenter appCMSPresenter;
    AppCMSUIKeyType componentKey;
    Module moduleAPI;
    MetadataMap metadataMap;

    public InputView(Context context,
                     AppCMSPresenter appCMSPresenter,
                     AppCMSUIKeyType componentKey,
                     Module moduleAPI) {
        super(context);
        inflate(getContext(), R.layout.custom_edittext_component, this);
        ButterKnife.bind(this);
        mContext = context;
        this.appCMSPresenter = appCMSPresenter;
        this.componentKey = componentKey;
        this.moduleAPI = moduleAPI;
        if (moduleAPI != null && moduleAPI.getMetadataMap() != null) {
            metadataMap = moduleAPI.getMetadataMap();
        }
        initView();
    }

    public void initView() {
        int appTextColor = appCMSPresenter.getGeneralTextColor();
        int appBackgroundColor = appCMSPresenter.getGeneralBackgroundColor();
        int transparentColor = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius = 10;
        int rectangleStroke = 2;
        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {appCMSPresenter.getBrandPrimaryCtaColor(), appTextColor};

        CustomShape.makeRoundCorner(transparentColor, cornerRadius, textInputContainer, rectangleStroke, appTextColor);
        textInputEditField.setTextColor(appTextColor);
        textInputEditField.setHintTextColor(appTextColor);
        textInputLayout.setEndIconTintList(new ColorStateList(states, colors));
        textViewTitle.setTextColor(appTextColor);
        textViewTitle.setBackgroundColor(appBackgroundColor);

        textInputEditFieldCountryCode.setTextColor(appTextColor);
        separatorViewCountry.setBackgroundColor(appTextColor);

        ConstraintSet set = new ConstraintSet();
        set.clone(textInputContainer);



        switch (componentKey) {
            case PAGE_EMAILTEXTFIELD_KEY:
            case PAGE_EMAILTEXTFIELD2_KEY: {
                if (metadataMap != null && metadataMap.getEditEmailLabel() != null) {
                    textViewTitle.setText(metadataMap.getEmailLabel());
                } else {
                    textViewTitle.setText("Email");
                }
                if (metadataMap != null && metadataMap.getEmailInput() != null) {
                    textInputEditField.setHint(moduleAPI.getMetadataMap().getEmailInput());
                }
                //textInputEditField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                textInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                set.setVisibility(textInputLayoutCountryCode.getId(),ConstraintSet.GONE);
                set.setVisibility(separatorViewCountry.getId(),ConstraintSet.GONE);
            }
            break;
            case PAGE_PHONETEXTFIELD_KEY:{
                EditText editText = textInputEditFieldCountryCode;
                if (editText != null) {
                    String code = CommonUtils.getCountryCode(mContext);
                    if (!TextUtils.isEmpty(code))
                        editText.setText("+"+code);
                    editText.setFocusable(false);
                    editText.setFocusableInTouchMode(false);
                    editText.setOnTouchListener((v, event) -> {
                        if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                            new AlertDialog.Builder(getContext()).setAdapter(new CountryAdapter(appCMSPresenter), (dialog, which) -> {
                                ListView listView = ((AlertDialog) dialog).getListView();
                                if (listView != null && listView.getAdapter() != null) {
                                    Object selectedItem = listView.getAdapter().getItem(which);
                                    if (selectedItem instanceof CountryCodeData)
                                        editText.setText("+"+ ((CountryCodeData)selectedItem).getCountryCode());
                                }
                                dialog.dismiss();
                            }).create().show();
                        }
                        return true;
                    });
                }
                if (metadataMap != null && metadataMap.getPhoneLabel() != null) {
                    textViewTitle.setText(metadataMap.getPhoneLabel());
                } else {
                    textViewTitle.setText("Phone");
                }
                if (metadataMap != null && metadataMap.getPhoneInput() != null) {
                    textInputEditField.setHint(moduleAPI.getMetadataMap().getPhoneInput());
                }
                //textInputEditField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                textInputEditFieldCountryCode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
                if (appCMSPresenter.unformattedPhone != null) {
                    textInputEditField.setText(appCMSPresenter.unformattedPhone);
                }
                textInputEditField.requestFocus();
                textInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
                textInputLayoutCountryCode.setEndIconMode(TextInputLayout.END_ICON_NONE);
                set.setVisibility(textInputLayoutCountryCode.getId(),ConstraintSet.VISIBLE);
                set.setVisibility(separatorViewCountry.getId(),ConstraintSet.VISIBLE);
            }
            break;
            case PAGE_PASSWORDTEXTFIELD_KEY:
            case PAGE_PASSWORDTEXTFIELD2_KEY: {
                if (metadataMap != null && metadataMap.getPasswordLabel() != null) {
                    textViewTitle.setText(metadataMap.getPasswordLabel());
                } else {
                    textViewTitle.setText("Password");
                }
                if (metadataMap != null && metadataMap.getPasswordInput() != null) {
                    textInputEditField.setHint(moduleAPI.getMetadataMap().getPasswordInput());
                }
                textInputEditField.setTransformationMethod(new AsteriskPasswordTransformation());
                //appCMSPresenter.setCursorDrawableColor(textInputEditField, 0);
                appCMSPresenter.noSpaceInEditTextFilter(textInputEditField, getContext());
                textInputLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                set.setVisibility(textInputLayoutCountryCode.getId(),ConstraintSet.GONE);
                set.setVisibility(separatorViewCountry.getId(),ConstraintSet.GONE);
            }
            break;
        }

        set.applyTo(textInputContainer);

    }

    public String getText() {
        return textInputEditField.getText().toString();
    }

    public TextInputEditText getEditText() {
        return textInputEditField;
    }
    public TextInputEditText getCountryCodeEditText() { return textInputEditFieldCountryCode; }
    public View getSeparatorViewCountry() { return separatorViewCountry; }

    public void setHint(String hint){
        textInputEditField.setHint(hint);
    }
    public void setTitle(String title){
        textViewTitle.setText(title);
    }

    public void setText(String text){
        textInputEditField.setText(text);
    }
    public void hideTitleView() { textViewTitle.setVisibility(View.GONE); }
    public void hideCountryCodeLayout() {
        textInputLayoutCountryCode.setVisibility(View.GONE);
        separatorViewCountry.setVisibility(View.GONE);
    }

}

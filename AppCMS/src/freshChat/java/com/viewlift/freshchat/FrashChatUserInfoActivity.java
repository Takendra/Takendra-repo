package com.viewlift.freshchat;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FrashChatUserInfoActivity extends AppCompatActivity {

    AppCMSPresenter appCMSPresenter;

    AppPreference appPreference;
    LocalisedStrings localisedStrings;

    @BindView(R.id.textViewEmail)
    TextView textViewEmail;

    @BindView(R.id.textViewPhone)
    TextView textViewPhone;

    @BindView(R.id.textViewPhoneError)
    TextView textViewPhoneError;

    @BindView(R.id.editTextPhoneCountryCode)
    Spinner editTextPhoneCountryCode;

    @BindView(R.id.editTextPhone)
    EditText editTextPhone;

    @BindView(R.id.textViewEmailError)
    TextView textViewEmailError;

    @BindView(R.id.editTextEmail)
    EditText editTextEmail;

    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;

    private HashMap<String, String> countryCodeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appCMSPresenter =
                ((AppCMSApplication) getApplicationContext()).getAppCMSPresenterComponent().appCMSPresenter();

        appPreference = appCMSPresenter.getAppPreference();

        localisedStrings = appCMSPresenter.getLocalisedStrings();

        setContentView(R.layout.activity_appcms_freshchat_user_info_page);
        ButterKnife.bind(this);

        getWindow().setBackgroundDrawableResource(R.color.transparentColor);

        if (BaseView.isTablet(this)) {
            appCMSPresenter.unrestrictPortraitOnly();
        } else {
            appCMSPresenter.restrictPortraitOnly();
        }

        countryCodeList = parseStringArray(R.array.countryCodes);

        TextViewCompat.setTextAppearance(textViewPhoneError,
                androidx.appcompat.R.style.TextAppearance_AppCompat_Caption);
        textViewPhoneError.setTextColor(ContextCompat.getColor(this,
                androidx.appcompat.R.color.error_color_material_light));
        textViewPhoneError.setVisibility(View.INVISIBLE);

        TextViewCompat.setTextAppearance(textViewEmailError,
                androidx.appcompat.R.style.TextAppearance_AppCompat_Caption);
        textViewEmailError.setTextColor(ContextCompat.getColor(this,
                androidx.appcompat.R.color.error_color_material_light));
        textViewEmailError.setVisibility(View.GONE);


        CountryCodes cc = new CountryCodes(this);
        editTextPhoneCountryCode.setAdapter(cc);

        TelephonyManager man = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int index = CountryCodes.getIndex(man.getSimCountryIso());
        if (index > -1) {
            editTextPhoneCountryCode.setSelection(index);
        }

        textViewEmail.setTextColor(appCMSPresenter.getGeneralTextColor());
        textViewPhone.setTextColor(appCMSPresenter.getGeneralTextColor());

        editTextEmail.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
        editTextEmail.setTextColor(appCMSPresenter.getGeneralBackgroundColor());

        editTextPhone.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
        editTextPhone.setTextColor(appCMSPresenter.getGeneralBackgroundColor());

        editTextPhoneCountryCode.setBackgroundColor(appCMSPresenter.getGeneralTextColor());
        //editTextPhoneCountryCode.setTextColor(appCMSPresenter.getGeneralBackgroundColor());
        editTextPhoneCountryCode.setFocusable(true);


        buttonSubmit.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        buttonSubmit.setBackgroundColor(appCMSPresenter.getBrandPrimaryCtaColor());

        textViewEmail.setText("Enter Email");
        textViewPhone.setText("Enter Phone number with country code.");
        // editTextPhoneCountryCode.setHint("Code");
        editTextPhone.setHint("Phone Number");
        editTextEmail.setHint("Email");
        buttonSubmit.setText("Submit");

        editTextEmail.setText(appPreference.getLoggedInUserEmail());
        //editTextPhoneCountryCode.setText(getCountryCode());

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewEmailError.setVisibility(View.GONE);
                textViewPhoneError.setVisibility(View.INVISIBLE);
                setEditTextPhoneCountryCode(getCountryCode());
                setEditTextPhone(editTextPhone.getText().toString());
                setEditTextEmail(editTextEmail.getText().toString());

                if (setEditTextPhone(editTextPhone.getText().toString())
                        && setEditTextEmail(editTextEmail.getText().toString())) {

                    FreshChatSDKUtil.launchFreshChat(appCMSPresenter.getCurrentActivity(),
                            appPreference.getLoggedInUserEmail(),
                            appPreference.getLoggedInUserName(),
                            appPreference.getLoggedInUserPhone(),
                            appPreference.getLoggedInUserPhoneCounntryCode());
                    finish();

                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Utils.isColorDark(appCMSPresenter.getGeneralBackgroundColor())) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FreshChatSDKUtil.launchTime = "";
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (BaseView.isTablet(this)) {
            appCMSPresenter.unrestrictPortraitOnly();
        } else {
            appCMSPresenter.restrictPortraitOnly();
        }

    }

    public boolean setEditTextPhoneCountryCode(String editTextPhoneCountryCode) {
        if (editTextPhoneCountryCode == null || TextUtils.isEmpty(editTextPhoneCountryCode)) {

        } else {
            appPreference.setLoggedInUserPhoneCountryCode(editTextPhoneCountryCode);
            appPreference.setFreshchatPhoneCountryCode(editTextPhoneCountryCode);
            return true;
        }
        return false;
    }

    public boolean setEditTextPhone(String editTextPhone) {
        if (editTextPhone == null || TextUtils.isEmpty(editTextPhone)) {
            textViewPhoneError.setVisibility(View.VISIBLE);
            textViewPhoneError.setText("Phone number can not be empty!");
        } else {
            appPreference.setLoggedInUserPhone(editTextPhone);
            appPreference.setFreshchatPhone(editTextPhone);
            return true;
        }
        return false;
    }

    public boolean setEditTextEmail(String editTextEmail) {
        if (editTextEmail == null || TextUtils.isEmpty(editTextEmail) ||
                !Utils.isEmailValid(editTextEmail)) {
            textViewEmailError.setText(localisedStrings.getEmailFormatValidationMsg());
            textViewEmailError.setVisibility(View.VISIBLE);
        } else {
            appPreference.setLoggedInUserEmail(editTextEmail);
            return true;
        }
        return false;
    }


    public HashMap<String, String> parseStringArray(int stringArrayResourceId) {
        String[] stringArray = getResources().getStringArray(stringArrayResourceId);

        countryCodeList = new HashMap<>();
        for (String entry : stringArray) {
            String[] splitResult = entry.split(",", 2);

            countryCodeList.put(splitResult[1], splitResult[0]);
        }
        return countryCodeList;
    }

    public String getCountryCode() {
        String code = null;
        if (editTextPhoneCountryCode.getAdapter() != null) {
            code = CountryCodes.getCode(editTextPhoneCountryCode.getSelectedItemPosition());
        }
        return code;
    }


}

package com.viewlift.views.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.facebook.common.Common;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.authentication.PhoneObjectRequest;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.receivers.SMSBroadcastReceiver;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.dialog.CustomShape;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.viewlift.Utils.isFireTVDevice;

public class VerifyOTPPhoneFragment extends DialogFragment implements SMSBroadcastReceiver.OTPReceiveListener {
    @Inject
    AppCMSPresenter appCMSPresenter;
    @BindView(R.id.input_one)
    AppCompatEditText inputTextOne;
    @BindView(R.id.input_two)
    AppCompatEditText inputTextTwo;
    @BindView(R.id.input_three)
    AppCompatEditText inputTextThree;
    @BindView(R.id.input_four)
    AppCompatEditText inputTextFour;
    @BindView(R.id.resendOTP)
    AppCompatButton resendOTP;
    @BindView(R.id.verifyOTP)
    AppCompatButton verifyOTP;
    @BindView(R.id.progressVerify)
    ProgressBar progressBar;
    @BindView(R.id.verify_otp_header)
    AppCompatTextView verifyOTPHeader;
    @BindView(R.id.closeDialogButtton)
    AppCompatImageView dialogCloseButton;
    private int COUNTER_VALUE_TIMER = 30; // hardcoding resend timer value to 30 sec # support - 40125

    private int counter;
    private PhoneObjectRequest phoneObjectRequest;
    @BindView(R.id.parentLayout)
    LinearLayoutCompat parentLayout;
    private VerifyOTPPhoneFragment verifyOTPPhoneFragment;
    // Get an instance of SmsRetrieverClient, used to start listening for a matching
    // SMS message.
    SmsRetrieverClient client;
    private MetadataMap metadataMap;
    private CountDownTimer countDownTimer;

    public VerifyOTPPhoneFragment() {
        // Required empty public constructor
    }

    public static VerifyOTPPhoneFragment newInstance(Context context, PhoneObjectRequest phoneObjectRequest) {
        VerifyOTPPhoneFragment fragment = new VerifyOTPPhoneFragment();
        Bundle args = new Bundle();
        args.putSerializable("phoneObject", phoneObjectRequest);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        phoneObjectRequest = (PhoneObjectRequest) getArguments().getSerializable("phoneObject");
        if (phoneObjectRequest.getMetadataMap() != null)
            metadataMap = phoneObjectRequest.getMetadataMap();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        appCMSPresenter.restrictPortraitOnly();
        setCancelable(false);
        inputTextOne.requestFocus();
        progressBar.setVisibility(View.GONE);
        verifyOTPPhoneFragment = this;
        startSMSListener();

        setViewStyles();
        setData();

        inputTextOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputTextTwo.requestFocus();
                }
            }
        });

        inputTextTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputTextThree.requestFocus();
                }
            }
        });

        inputTextThree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    inputTextFour.requestFocus();
                }
            }
        });

        inputTextFour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1) {
                    verifyOTP.requestFocus();
                }
            }
        });

        //Start the timer so that user cannot click on resend otp button.
        startTimer();
    }


    private void setData() {
        verifyOTPHeader.setText(getString(R.string.otp_header, phoneObjectRequest.getPhone()));
        if (metadataMap != null) {
            if (metadataMap.getVerifyOTPText() != null && phoneObjectRequest.getPhone() != null)
                verifyOTPHeader.setText(metadataMap.getVerifyOTPText() + phoneObjectRequest.getPhone());

            if (metadataMap.getVerifyOTPSubmit() != null)
                verifyOTP.setText(metadataMap.getVerifyOTPSubmit());
            if (metadataMap.getResentOTPText() != null) {
                resendOTP.setText(metadataMap.getResentOTPText());
            }
        }

        // uncomment code if we need to read resend otp timer from backend . Hardcoded as of now . Support - 40125
        /*if (appCMSPresenter != null && appCMSPresenter.getAppCMSMain() != null
                && appCMSPresenter.getAppCMSMain().getFeatures() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getResendOtpTimer() != null
                && appCMSPresenter.getAppCMSMain().getFeatures().getResendOtpTimer() > 0) {
            COUNTER_VALUE_TIMER = appCMSPresenter.getAppCMSMain().getFeatures().getResendOtpTimer();
        }*/
    }

    private void setViewStyles() {
        int appTextColor       = appCMSPresenter.getGeneralTextColor();
        int appBackgroundColor = appCMSPresenter.getGeneralBackgroundColor();
        int transparentColor   = ContextCompat.getColor(getContext(), android.R.color.transparent);
        int cornerRadius       = 10;
        int rectangleStroke    = 2;
        parentLayout.setBackgroundColor(appBackgroundColor);
        if (!CommonUtils.isEmpty(appCMSPresenter.getAppTextColor())) {
            verifyOTPHeader.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        }
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, inputTextOne, rectangleStroke, appTextColor);
        inputTextOne.setTextColor(appTextColor);
        inputTextOne.setHintTextColor(appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, inputTextTwo, rectangleStroke, appTextColor);
        inputTextTwo.setTextColor(appTextColor);
        inputTextTwo.setHintTextColor(appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, inputTextThree, rectangleStroke, appTextColor);
        inputTextThree.setTextColor(appTextColor);
        inputTextThree.setHintTextColor(appTextColor);
        CustomShape.makeRoundCorner(transparentColor, cornerRadius, inputTextFour, rectangleStroke, appTextColor);
        inputTextFour.setTextColor(appTextColor);
        inputTextFour.setHintTextColor(appTextColor);
        verifyOTP.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        resendOTP.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        verifyOTP.setTextColor(appTextColor);
        resendOTP.setTextColor(appTextColor);
    }

    public void enableLayoutClick() {
        progressBar.setVisibility(View.GONE);
        verifyOTP.setEnabled(true);
        resendOTP.setEnabled(true);
        resendOTP.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
        verifyOTP.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
    }

    @OnClick(R.id.closeDialogButtton)
    void close() {
        dismiss();
    }

    @OnClick(R.id.verifyOTP)
    void verifyOTPClick() {
        appCMSPresenter.closeSoftKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        verifyOTP.setEnabled(false);
        verifyOTP.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        resendOTP.setEnabled(false);
        resendOTP.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        String otpValue = inputTextOne.getText().toString() + inputTextTwo.getText().toString() + inputTextThree.getText().toString() + inputTextFour.getText().toString();
        if (otpValue.isEmpty())
            otpValue = " ";
        phoneObjectRequest.setRequestType("verify");
        phoneObjectRequest.setFromVerify(true);
        phoneObjectRequest.setOtpValue(otpValue);
        appCMSPresenter.verifyOTPRequestCreation(otpValue, phoneObjectRequest, phoneObjectRequest.getUrl(), verifyOTPPhoneFragment, false);
    }

    @OnClick(R.id.resendOTP)
    void resendOTPClick() {
        String deviceName;
        String platform;
        if (appCMSPresenter.getPlatformType().equals(AppCMSPresenter.PlatformType.ANDROID)){
            deviceName = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_phone);
            platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_platform);
        } else {
            if (isFireTVDevice(appCMSPresenter.getCurrentContext())) {
                deviceName = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_fire_tv);
                platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_amazon_platform);
            } else {
                deviceName = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_tv);
                platform = appCMSPresenter.getCurrentContext().getString(R.string.app_cms_query_param_android_platform);
            }
        }

        String resendUrl = appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_resend_otp_api_url,
                appCMSPresenter.getAppCMSMain().getApiBaseUrl(),
                platform,
                deviceName,
                appCMSPresenter.getAppCMSMain().getInternalName(),
                appCMSPresenter.getDeviceId(),
                CommonUtils.getDeviceName());
        resendOTP.setEnabled(false);
        resendOTP.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        inputTextOne.setText("");
        inputTextTwo.setText("");
        inputTextThree.setText("");
        inputTextFour.setText("");
        inputTextOne.requestFocus();
        phoneObjectRequest.setRequestType("resend");
        appCMSPresenter.verifyOTPRequestCreation(null, phoneObjectRequest, resendUrl, verifyOTPPhoneFragment, true);
        startTimer();
    }

    public void startTimer() {
        resendOTP.setEnabled(false);
        resendOTP.setBackground(CustomShape.createRoundedRectangleDrawable(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(COUNTER_VALUE_TIMER * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                resendOTP.setText((COUNTER_VALUE_TIMER - counter) + " Seconds");
                if (COUNTER_VALUE_TIMER - counter == 1)
                    onFinish();
                counter++;
            }

            @Override
            public void onFinish() {

                try {
                    resendOTP.setEnabled(true);
                    resendOTP.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getBrandPrimaryCtaColor()));
                    if (metadataMap != null &&
                            metadataMap.getResentOTPText() != null) {
                        resendOTP.setText(metadataMap.getResentOTPText());
                    } else {
                        resendOTP.setText(appCMSPresenter.getCurrentActivity().getApplicationContext().getString(R.string.resend_otp));
                    }
                    counter = 0;
                    cancel();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    SMSBroadcastReceiver mySMSBroadcastReceiver;

    private void startSMSListener() {

        mySMSBroadcastReceiver = new SMSBroadcastReceiver();
        mySMSBroadcastReceiver.setOTPListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(mySMSBroadcastReceiver, intentFilter);

        client = SmsRetriever.getClient(getActivity());
        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();
        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mySMSBroadcastReceiver);
    }

    @Override
    public void onOTPReceived(String otp) {
        try {
            inputTextOne.setText("");
            inputTextTwo.setText("");
            inputTextThree.setText("");
            inputTextFour.setText("");
            inputTextOne.requestFocus();
            inputTextOne.setText(Character.toString(otp.charAt(0)));
            inputTextTwo.setText(Character.toString(otp.charAt(1)));
            inputTextThree.setText(Character.toString(otp.charAt(2)));
            inputTextFour.setText(Character.toString(otp.charAt(3)));
            inputTextOne.requestFocus();
            verifyOTPClick();
        } catch (Exception e) {
        }
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}

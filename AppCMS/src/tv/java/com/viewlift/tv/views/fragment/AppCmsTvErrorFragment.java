package com.viewlift.tv.views.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCMSTVPlayVideoActivity;
import com.viewlift.utils.CommonUtils;


/**
 * A placeholder fragment containing a simple view.
 */
public class AppCmsTvErrorFragment extends AbsDialogFragment {

    AppCMSPresenter appCMSPresenter;
    private ErrorFragmentListener mErrorFragmentListener;
    Button btnRetry;
    boolean shouldRegisterInternetReciever = true;
    private boolean shouldNavigateToLogin;

    public AppCmsTvErrorFragment(){

    }

    public static AppCmsTvErrorFragment newInstance(Bundle bundle) {
        AppCmsTvErrorFragment appCmsTvErrorActivityFragment = new AppCmsTvErrorFragment();
        appCmsTvErrorActivityFragment.setArguments(bundle);
        return appCmsTvErrorActivityFragment;
    }

    public void setErrorListener(ErrorFragmentListener errorFragmentListener){
        mErrorFragmentListener = errorFragmentListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_cms_tv_error, container, false);
        TextView errorTextView = (TextView) view.findViewById(R.id.app_cms_error_textview);
        TextView headerView = (TextView)view.findViewById(R.id.title);

        RelativeLayout parentLayout = (RelativeLayout)view.findViewById(R.id.dialog_parent);

        final Bundle bundle = getArguments();
        final boolean shouldRetry = bundle.getBoolean(getString(R.string.retry_key));
        shouldRegisterInternetReciever = bundle.getBoolean(getString(R.string.register_internet_receiver_key));
        String errorMsg = bundle.getString(getString(R.string.tv_dialog_msg_key));
        String headerTitle = bundle.getString(getString(R.string.tv_dialog_header_key));
        shouldNavigateToLogin = bundle.getBoolean(getString(R.string.shouldNavigateToLogin));
        boolean showHeader = bundle.getBoolean(getString(R.string.show_header));
        String closeBtnTxt = bundle.getString(getString(R.string.close_btn_txt));


        appCMSPresenter =
                ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        errorTextView.setText(appCMSPresenter.getLanguageResourcesFile().getStringValue(getString(R.string.no_inernet_error),getString(R.string.app_name)));

        if(null != errorMsg){
            errorTextView.setText(errorMsg);
        } else {
            errorTextView.setText(appCMSPresenter.getLocalisedStrings().getInternetErrorMsg());
        }

        if(null != headerTitle){
            headerView.setText(headerTitle);
        } else {
            headerView.setText(appCMSPresenter.getLocalisedStrings().getInternetErrorHeader());
        }

        if(!showHeader){
            headerView.setVisibility(View.GONE);
        }else{
            headerView.setVisibility(View.VISIBLE);
        }

        String textColor = appCMSPresenter.getAppTextColor();
        String backGroundColor = appCMSPresenter.getAppBackgroundColor();
        String focusColor = Utils.getFocusColor(getActivity(),appCMSPresenter);

        errorTextView.setTextColor(Color.parseColor(textColor != null ? textColor : "#FFFFFF"));
        headerView.setTextColor(Color.parseColor(textColor != null ? textColor : "#FFFFFF"));

        Button btnClose = (Button) view.findViewById(R.id.btn_close);
        Component btnComponent1 = new Component();
        btnComponent1.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        btnComponent1.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        btnComponent1.setBorderColor("#ffffff");
        btnComponent1.setBorderWidth(4);
        //btnClose.setTextColor(Color.parseColor(textColor != null ? textColor : "#000000"));
        btnClose.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))),appCMSPresenter
        ));


        btnClose.setBackground(Utils.setButtonBackgroundSelector(getActivity() ,
                Color.parseColor(focusColor != null ? focusColor : "#000000"),
                btnComponent1 , appCMSPresenter));

        btnClose.setTypeface(Utils.getTypeFace(getActivity() , appCMSPresenter, btnComponent1));

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(shouldNavigateToLogin) {
                    mErrorFragmentListener.onErrorScreenClose(false);
                //}
                dismiss();
                if (appCMSPresenter.getCurrentActivity() instanceof AppCMSTVPlayVideoActivity) {
                    appCMSPresenter.getCurrentActivity().finish();
                }
            }
        });


        btnRetry = (Button) view.findViewById(R.id.btn_retry);
        btnRetry.setText(appCMSPresenter.getLocalisedStrings().getRetryText());
        Component btnRetryComp = new Component();
        btnRetryComp.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        btnRetryComp.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        btnRetryComp.setBorderColor("#ffffff");
        btnRetryComp.setBorderWidth(4);
       // btnRetry.setTextColor(Color.parseColor(textColor != null ? textColor : "#FFFFFF"));

        btnRetry.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))),appCMSPresenter
        ));


        btnRetry.setBackground(Utils.setButtonBackgroundSelector(getActivity() ,
                Color.parseColor(focusColor != null ? focusColor : "#000000"),
                btnRetryComp,
                appCMSPresenter));

        btnRetry.setTypeface(Utils.getTypeFace(getActivity() , appCMSPresenter, btnRetryComp));

        btnRetry.requestFocus();
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mErrorFragmentListener.onRetry(bundle);
                dismiss();
            }
        });

        if(!shouldRetry) {
            btnRetry.setVisibility(View.INVISIBLE);
            String btnCloseText = appCMSPresenter.getLanguageResourcesFile().getUIresource(getResources().getString(R.string.app_cms_ok_alert_dialog_button_text));
            btnClose.setText(btnCloseText);
//            if(closeBtnTxt != null && closeBtnTxt.length() >0) // adding this condition for showing Dismiss button in case of GeoRestrict dialog.
//                btnClose.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(closeBtnTxt));
        } else {
            btnClose.setText(appCMSPresenter.getLocalisedStrings().getCloseText());
        }
        if(null != backGroundColor)
        parentLayout.setBackgroundColor(Color.parseColor(backGroundColor));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        int width =  getResources().getDimensionPixelSize(R.dimen.text_overlay_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.text_overlay_dialog_height);
        bundle.putInt( getString(R.string.tv_dialog_width_key) , width);
        bundle.putInt( getString(R.string.tv_dialog_height_key) , height);
        super.onActivityCreated(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                switch(keyEvent.getKeyCode()){
                    case KeyEvent.KEYCODE_BACK:
                        if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                            mErrorFragmentListener.onErrorScreenClose(true);
                            dismiss();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        if(shouldRegisterInternetReciever)
        getActivity().registerReceiver(networkReciever ,
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }

    @Override
    public void onPause() {
        if(shouldRegisterInternetReciever)
        getActivity().unregisterReceiver(networkReciever);
        super.onPause();
    }

    public interface ErrorFragmentListener{
        public void onErrorScreenClose(boolean closeActivity);
        public void onRetry(Bundle bundle);
    }


    BroadcastReceiver networkReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")){
                if(appCMSPresenter.isNetworkConnected()){
                    //TODO:resume the video.
                    btnRetry.callOnClick();
                }
            }
        }
    };
}

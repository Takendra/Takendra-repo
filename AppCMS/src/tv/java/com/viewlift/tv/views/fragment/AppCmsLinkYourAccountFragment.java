package com.viewlift.tv.views.fragment;


import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.GetLinkCode;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.verimatrix.VerimatrixResponse;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.AppCmsTVSplashActivity;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.component.AppCMSTVViewComponent;
import com.viewlift.tv.views.component.DaggerAppCMSTVViewComponent;
import com.viewlift.tv.views.customviews.TVModuleView;
import com.viewlift.tv.views.customviews.TVPageView;
import com.viewlift.tv.views.module.AppCMSTVPageViewModule;
import com.viewlift.views.binders.AppCMSBinder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class AppCmsLinkYourAccountFragment extends AbsDialogFragment {

    private static final String TAG = AppCmsLinkYourAccountFragment.class.getSimpleName();
    private AppCMSPresenter appCMSPresenter;
    private AppCMSTVViewComponent appCmsViewComponent;
    private TVPageView tvPageView;
    private TextView textLine2;
    private String finalCodeText = null;
    final String[] text2 = {null};
    private boolean isDestory;

    public AppCmsLinkYourAccountFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static AppCmsLinkYourAccountFragment newInstance(AppCMSBinder appCMSBinder) {
        AppCmsLinkYourAccountFragment fragment = new AppCmsLinkYourAccountFragment();
        Bundle args = new Bundle();
        args.putBinder("app_cms_binder_key", appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    private AppCMSBinder appCMSBinder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestory = false;
        if (getArguments() != null && getArguments().getBinder("app_cms_binder_key") instanceof AppCMSBinder) {
            appCMSBinder = (AppCMSBinder) getArguments().getBinder("app_cms_binder_key");
        }
        appCMSPresenter =
                ((AppCMSApplication) getActivity().getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        IBinder binder = getArguments().getBinder("app_cms_binder_key");
        if (!(binder instanceof AppCMSBinder)) {
            Log.e(TAG, "onCreateView: Binder not instance of AppCMSBinder. Restarting Application.");
            restartApplication();
            return null;
        }

        if (getArguments() != null) {
            appCMSBinder = (AppCMSBinder) getArguments().getBinder("app_cms_binder_key");
        }

        if (appCmsViewComponent == null ) {
            appCmsViewComponent = buildAppCMSViewComponent();
        }

        if (appCmsViewComponent != null) {
            tvPageView = appCmsViewComponent.appCMSTVPageView();
        } else {
            tvPageView = null;
        }

        if (tvPageView != null) {
            if (tvPageView.getParent() != null) {
                ((ViewGroup) tvPageView.getParent()).removeAllViews();
            }
        }
        if (container != null) {
            container.removeAllViews();
        }

        if(null != tvPageView && tvPageView.getChildrenContainer().getChildAt(0) instanceof TVModuleView){
            TVModuleView tvModuleView = (TVModuleView)tvPageView.getChildrenContainer().getChildAt(0);

            textLine2 = (TextView)tvModuleView.findViewById(R.id.code_sync_text_line_2);
            TextView headerTextLine = (TextView)tvModuleView.findViewById(R.id.code_sync_text_line_header);


            String headerText = null;
            String codeText = null;
            if(null != textLine2.getText()){
                text2[0] = textLine2.getText().toString();
                codeText= textLine2.getText().toString();
            }
            if(null != headerTextLine.getText()) {
                headerText = headerTextLine.getText().toString()
                        .replace("$App$", getResources().getString(R.string.app_name))
                        .replace("$app_web_url$", appCMSPresenter.getAppCMSMain().getDomainName());
            }

            if(null != text2[0])
                textLine2.setText(text2[0]);
            if(null != headerText) {
                headerTextLine.setText(headerText);
                setSpan(headerTextLine, headerText, appCMSPresenter.getModuleApi());
            }
            finalCodeText = codeText;
            appCMSPresenter.getDeviceLinkCode((GetLinkCode getSyncCode) -> {
                if(null != getSyncCode) {
                    appCMSPresenter.stopLoader();
                    setSyncCode(getSyncCode.getActivationCode(),null);
                }else{
                    appCMSPresenter.stopLoader();
                    textLine2.setText(text2[0].replace("XXXXXX","Some Error Occured."));
                }
            },(VerimatrixResponse verimatrixResponse) -> {
                if(null != verimatrixResponse) {
                    appCMSPresenter.stopLoader();
                    setSyncCode(verimatrixResponse.getShortCode(),verimatrixResponse.getPolling_url());
                }else{
                    appCMSPresenter.stopLoader();
                    textLine2.setText(text2[0].replace("XXXXXX","Some Error Occured."));
                }
            });
        }
        tvPageView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        return tvPageView;
    }

    private void setSyncCode(String activationCode, String url) {
        //appCMSPresenter.stopLoader();
        text2[0] = finalCodeText.replace("XXXXXX", activationCode);
        textLine2.setText(text2[0]);
        appCMSPresenter.syncCode(syncDeviceCode -> {
            if (null != syncDeviceCode && !isDestory) {
                //   Log.d("TAG", "syncDeviceCode Name = " + syncDeviceCode.getName());
                if (!isStateSaved()) {
                    dismiss();
                }
            }
        }, (VerimatrixResponse verimatrixResponse) -> {
            if (null != verimatrixResponse && !isDestory) {
                //   Log.d("TAG", "syncDeviceCode Name = " + syncDeviceCode.getName());
                if (!isStateSaved()) {
                    dismiss();
                }
            }
        }, url);
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener((dialogInterface, i, keyEvent) -> {
            switch(keyEvent.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                        appCMSPresenter.stopSyncCodeAPI();
                        dismiss();
                        return true;
                    }
                    break;
            }
            return false;
        });
    }

    public void setSpan(TextView textView , String text, Module module) {
        String replacementString = appCMSPresenter.getAppCMSMain().getDomainName();
        if (module != null && module.getSettings() != null && !TextUtils.isEmpty(module.getSettings().getActivateDeviceUrl()))
            replacementString = module.getSettings().getActivateDeviceUrl();

        Spannable wordToSpan = new SpannableString(text);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(
                Color.parseColor(Utils.getPrimaryHoverBorderColor(getContext(),appCMSPresenter))
        );

        int startIndex = text.indexOf(replacementString);
        int indexOfFirstSpaceAfterURL = text.indexOf(" ", text.indexOf(replacementString));
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), startIndex, indexOfFirstSpaceAfterURL, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(foregroundColorSpan, startIndex, indexOfFirstSpaceAfterURL, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(wordToSpan);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        bundle.putInt( getString(R.string.tv_dialog_width_key) , MATCH_PARENT);
        bundle.putInt( getString(R.string.tv_dialog_height_key) , MATCH_PARENT);
        super.onActivityCreated(bundle);
    }

    public AppCMSTVViewComponent buildAppCMSViewComponent() {
        return DaggerAppCMSTVViewComponent.builder()
                .appCMSTVPageViewModule(new AppCMSTVPageViewModule(getActivity(),
                        appCMSBinder != null ? appCMSBinder.getAppCMSPageUI()  : null,
                        appCMSBinder != null ? appCMSBinder.getAppCMSPageAPI() : null,
                        appCMSPresenter.getJsonValueKeyMap(),
                        appCMSPresenter,
                        appCMSBinder.getPageId()
                ))
                .build();
    }

    /**
     * In case of older Fire TVs where the system memory is comparatively low, the app when resuming
     * from background doesn't always carry the intended payload (AppCMSBinder). In those, sporadic
     * cases, we restart the application.
     */
    private void restartApplication() {
        Intent mStartActivity = new Intent(getActivity(), AppCmsTVSplashActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getActivity(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestory = true;
    }
}
package com.viewlift.views.fragments;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.binders.AppCMSBinder;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppCMSRestDeatilsFragment extends DialogFragment {
    @BindView(R.id.tv_Countdown)
    TextView tvCountdown;
    @BindView(R.id.tv_RepeatTimer)
    TextView tv_RepeatTimer;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_equipmentDetails)
    TextView tv_equipmentDetails;
    @BindView(R.id.btn_backVideo)
    Button btn_backVideo;
    @BindView(R.id.btn_rest)
    Button btn_rest;
    @BindView(R.id.btn_repeatCircuit)
    Button btn_repeatCircuit;
    @BindView(R.id.btn_nextCircuit)
    Button btn_nextCircuit;
    @BindView(R.id.timer_background)
    View timer_background;
    @BindView(R.id.closeButton)
    ImageView closeButton;

    Context context=null;
    private AppCMSBinder appCMSBinder;
    Module module;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;
    String second, seconds;
    private static final int COUNTDOWN_INTERVAL_IN_MILLIS = 1000;
    private static final String TAG = "AutoplayFragment";
    private int totalCountdownInMillis;
    private CountDownTimer countdownTimer;
    Boolean defaultTimer=true;
    String restTimer="60";
    String repeatTime="4";
    public static Fragment newInstance(Context context, String loggedInUserName, String loggedInUserEmail, AppCMSBinder appCMSBinder) {

        AppCMSRestDeatilsFragment fragment = new AppCMSRestDeatilsFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceSxtate) {
        View view = inflater.inflate(R.layout.fragment_app_cms_rest_deatils, container, false);
         ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        Bundle args = getArguments();
        try {
            appCMSBinder =
                    ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(appCMSBinder!=null &&appCMSBinder.getAppCMSVideoPageBinder()!=null&&
                appCMSBinder.getAppCMSVideoPageBinder().getContentData()!=null&&
                appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist()!=null) {
            if (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getTitle() != null)
                tv_title.setText(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getTitle());
            if (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getDescription() != null)
                tv_equipmentDetails.setText(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getDescription());
            tvCountdown.setTypeface(appCMSPresenter.getBoldTypeFace());
            tvCountdown.setTypeface(appCMSPresenter.getBoldTypeFace());
            btn_repeatCircuit.setTypeface(appCMSPresenter.getBoldTypeFace());
            btn_backVideo.setTypeface(appCMSPresenter.getBoldTypeFace());
            btn_nextCircuit.setTypeface(appCMSPresenter.getBoldTypeFace());
            btn_rest.setTypeface(appCMSPresenter.getBoldTypeFace());
            tv_equipmentDetails.setTypeface(ResourcesCompat.getFont(getContext(), R.font.font_regular));
            tv_RepeatTimer.setTypeface(ResourcesCompat.getFont(getContext(), R.font.font_regular));
            int buttonColor = appCMSPresenter.getBrandPrimaryCtaColor();
            int textColor = appCMSPresenter.getGeneralTextColor();
            tv_title.setTextColor(textColor);
            btn_repeatCircuit.setBackgroundColor(Color.parseColor("#ffe7d6"));
            btn_nextCircuit.setBackgroundColor(buttonColor);
            btn_backVideo.setBackgroundColor(Color.parseColor("#ffe7d6"));
            timer_background.setBackgroundColor(buttonColor);
            btn_rest.setBackgroundColor(buttonColor);
            context = getContext();
      if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getBackToVideoCTA()!=null ){
          btn_backVideo.setText(  appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getBackToVideoCTA());
      }
      if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRestCTA()!=null ){
          btn_rest.setText(  appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRestCTA());
      }
      if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRepeatCircuitCTA()!=null ){
          btn_repeatCircuit.setText(  appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRepeatCircuitCTA());
      }
      if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null&&
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getNextCircuitCTA()!=null ){
          btn_nextCircuit.setText(  appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getNextCircuitCTA());
      }

                if (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata() != null && appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata().size() > 0) {
                    for (int i = 0; i < appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata().size(); i++) {
                        if (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata().get(i).getName().equalsIgnoreCase("restTimer")) {
                            restTimer = appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata().get(i).getValue();
                            Log.e("restTimer", restTimer);
                        } else if (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata().get(i).getName().equalsIgnoreCase("repeatTime")) {
                            repeatTime = appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getMetadata().get(i).getValue();
                            Log.e("repeatTime", repeatTime);
                        }

                    }
                }

            btn_backVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeCurrentPage();
                    replayVideo();
                }
            });
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeCurrentPage();
                }
            });
            btn_nextCircuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    defaultTimer = false;
                    checkForTvodContent();
                }

            });
            btn_repeatCircuit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeCurrentPage();
                    replayVideo();
                }
            });
            btn_rest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn_repeatCircuit.setVisibility(View.VISIBLE);
                    btn_nextCircuit.setVisibility(View.VISIBLE);
                    btn_backVideo.setVisibility(View.GONE);
                    btn_rest.setVisibility(View.GONE);
                    timer_background.setVisibility(View.VISIBLE);
                    tv_RepeatTimer.setVisibility(View.VISIBLE);
                    tvCountdown.setVisibility(View.VISIBLE);
                    startCountdown(restTimer);
                    if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                            appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null&&
                            appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRestHeader()!=null )
                        tv_title.setText(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRestHeader());
                    else
                    tv_title.setText(getResources().getString(R.string.rest_btn_txt));
                    if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                            appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null&&
                            appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRestMessage()!=null )
                        tv_equipmentDetails.setText(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRestMessage());
                    else
                    tv_equipmentDetails.setText(getResources().getString(R.string.rest_details));
                    String repeatTimesData=repeatTime;
                    if(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi()!=null&&
                            appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap()!=null)
                    {
                        if( appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRepeatLabel()!=null)
                            repeatTimesData=appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getRepeatLabel()+" "+repeatTimesData;
                        if( appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getTimesLabel()!=null)
                            repeatTimesData=repeatTimesData+" "+appCMSBinder.getAppCMSVideoPageBinder().getContentData().getModuleApi().getMetadataMap().getTimesLabel();
                    }
                    tv_RepeatTimer.setText(repeatTimesData);
                }
            });
        }

        return view;
    }
    private void replayVideo() {
        defaultTimer = false;
        if(appCMSBinder.getAppCMSVideoPageBinder()!=null &&appCMSBinder.getAppCMSVideoPageBinder().getRelateVideoIds()!=null) {
            appCMSPresenter.playNextVideo(appCMSBinder.getAppCMSVideoPageBinder(),
                    appCMSBinder.getAppCMSVideoPageBinder().getCurrentPlayingVideoIndex(),
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getWatchedTime());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
            appCMSPresenter.restrictLandscapeOnly();
    }

    private void startCountdown(String restTimer) {
        totalCountdownInMillis = Integer.valueOf(appCMSPresenter.getCurrentActivity().getResources()
                .getString(R.string.app_cms_rest_countdown_timer))*Integer.valueOf(restTimer);

        if (appCMSPresenter.getGenericMessages() != null) {
            second = appCMSPresenter.getGenericMessages().getSecondLabelFull();
            seconds = appCMSPresenter.getGenericMessages().getSecondsLabelFull();
        }
        if (appCMSPresenter.getLocalizationResult() != null) {
            second = appCMSPresenter.getLocalizationResult().getSecondLabelFull();
            seconds = appCMSPresenter.getLocalizationResult().getSecondsLabelFull();
        }
        second="00:";
        seconds="00:";



        countdownTimer = new CountDownTimer(totalCountdownInMillis, COUNTDOWN_INTERVAL_IN_MILLIS) {

            @Override
            public void onTick(long millisUntilFinished) {
              if ( tvCountdown != null)
                {
                    int quantity = (int) (millisUntilFinished / 1000);
                    StringBuilder thumbInfo = new StringBuilder();


                    long millis = millisUntilFinished;
                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d",  TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    tvCountdown.setText(hms);
                }
            }

            @Override
            public void onFinish() {
               if(defaultTimer)
               checkForTvodContent();
               else
                   closeCurrentPage();
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            if (countdownTimer != null) {
                countdownTimer.cancel();
                countdownTimer = null;
            }
    }

    private void closeCurrentPage()
    { appCMSPresenter.sendCloseOthersAction(null,
                true,
                false);
    }


    private void callCountDownFinished() {
        closeCurrentPage();
        if(appCMSBinder.getAppCMSVideoPageBinder()!=null &&appCMSBinder.getAppCMSVideoPageBinder().getRelateVideoIds()!=null &&
                (appCMSBinder.getAppCMSVideoPageBinder().getCurrentPlayingVideoIndex()+1)<appCMSBinder.getAppCMSVideoPageBinder().getRelateVideoIds().size()) {
            appCMSBinder.getAppCMSVideoPageBinder().setCurrentPlayingVideoIndex(appCMSBinder.getAppCMSVideoPageBinder().getCurrentPlayingVideoIndex() + 1);
            appCMSPresenter.playNextVideo(appCMSBinder.getAppCMSVideoPageBinder(),
                    appCMSBinder.getAppCMSVideoPageBinder().getCurrentPlayingVideoIndex(),
                    appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getWatchedTime());
        }
    }

    boolean isPurchaseDialog = false;
    boolean isPlayable = true;
    private void checkForTvodContent() {
        if ((appCMSBinder.getAppCMSVideoPageBinder() != null &&appCMSBinder.getAppCMSVideoPageBinder()!=null
                && appCMSBinder.getAppCMSVideoPageBinder().getContentData() != null
                && appCMSBinder.getAppCMSVideoPageBinder().getContentData().getPricing() != null
                && appCMSBinder.getAppCMSVideoPageBinder().getContentData().getPricing().getType() != null
                && (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_TVOD))
                || appCMSBinder.getAppCMSVideoPageBinder().getContentData().getPricing().getType().equalsIgnoreCase(context.getString(R.string.PURCHASE_TYPE_PPV))))) {
            appCMSPresenter.getTransactionData(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getId(), updatedContentDatum -> {
                if (updatedContentDatum != null &&
                        updatedContentDatum.size() > 0 &&
                        updatedContentDatum.get(0).size() > 0) {
                    AppCMSTransactionDataValue objTransactionData = updatedContentDatum.get(0).get(appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getId());
                    long expirationDate = objTransactionData.getTransactionEndDate();
                    long remainingTime = CommonUtils.getTimeIntervalForEvent(expirationDate, "EEE MMM dd HH:mm:ss");

                    long scheduleStartDate = 0;
                    long scheduleEndDate = 0;
                    long scheduleRemainTime = 0;

                    if (appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist() != null && appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getScheduleStartDate() > 0) {
                        scheduleStartDate = appCMSBinder.getAppCMSVideoPageBinder().getContentData().getGist().getScheduleStartDate();
                        scheduleRemainTime = CommonUtils.getTimeIntervalForEvent(scheduleStartDate, "yyyy MMM dd HH:mm:ss");
                    }


                    if (remainingTime < 0 && expirationDate > 0) {
                        isPlayable = false;
                    } else {
                        isPlayable = true;
                    }

                } else {
                    isPlayable = false;

                }

                if (isPlayable) {
                    callCountDownFinished();
                } else {
                    isPurchaseDialog = true;
                    appCMSPresenter.showNoPurchaseDialog(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.rental_title)), appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.cannot_purchase_msg)));

                }
            }, "Video");
        } else {
            callCountDownFinished();
        }
    }
}

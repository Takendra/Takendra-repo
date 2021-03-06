package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;

import rx.functions.Action0;


public class VisualTimer extends RelativeLayout {
    private static final String TAG = VisualTimer.class.getSimpleName();
    private final Context context;
    private Component mComponent;
    private AppCMSPresenter appCMSPresenter;
    private Module moduleAPI;
    private CountDownTimer countDownTimer;
    private final int countDownIntervalInMillis = 1000;
    private LinearLayout outerLayout;
    private long mRemainintTime;
    private TextView textMsg;
    private String contentId;
    private String contentType;

    public VisualTimer(Context context) {
        super(context);
        this.context = context;
    }

    public VisualTimer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public VisualTimer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public VisualTimer(Context context, Module moduleAPI, Component component, AppCMSPresenter appCMSPresenter) {
        super(context);
        this.context = context;
        this.moduleAPI = moduleAPI;
        this.mComponent = component;
        this.appCMSPresenter = appCMSPresenter;
        init();
    }

    Action0 mCallBack;
    public void setCallBack(Action0 callBack){
        this.mCallBack = null;
        this.mCallBack = callBack;
    }

    private void init() {
        LayoutParams layoutParam =  new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParam);


        this.setGravity(Gravity.CENTER);

        textMsg = new TextView(context);
        LayoutParams textViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(CENTER_HORIZONTAL);
        textViewParams.setMargins(0,0,0, 20);
        textMsg.setLayoutParams(textViewParams);
        textMsg.setTextSize(14);
        textMsg.setTextColor(appCMSPresenter.getGeneralTextColor());
        textMsg.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(mComponent.getText()));
        textMsg.setId(R.id.timer_until_face_off);
        textMsg.setShadowLayer(2, 1, 1, android.R.color.black);
        textMsg.setTypeface(Utils.getSpecificTypeface(context, appCMSPresenter, context.getString(R.string.app_cms_page_font_regular_key)));
        this.addView(textMsg);

        outerLayout = new LinearLayout(context);
        LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params1.addRule(BELOW, textMsg.getId());
        params1.addRule(CENTER_HORIZONTAL);
        outerLayout.setLayoutParams(params1);
        outerLayout.setOrientation(LinearLayout.HORIZONTAL);
        outerLayout.setGravity(Gravity.CENTER);
        outerLayout.setId(R.id.timer_id);

        for (int count = 0; count < 4; count++) {
            LinearLayout innerLayout = new LinearLayout(context);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(3, 0, 3, 0);
            innerLayout.setLayoutParams(params);

            innerLayout.setGravity(Gravity.CENTER);
            innerLayout.setBackgroundColor(Color.parseColor("#000000"));
            innerLayout.setAlpha(0.9f);
            for (int textView = 0; textView < 2; textView++) {
                TextView text = new TextView(context);
                text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                text.setGravity(Gravity.CENTER);
                text.setPadding(3, 0, 3, 0);
                text.setTextSize(26);
                text.setTextColor(appCMSPresenter.getGeneralTextColor());
                innerLayout.addView(text);
            }
            outerLayout.addView(innerLayout);
        }
        this.addView(outerLayout);
        setBackgroundColor(Color.parseColor("#BF000000"));
    }

    public void startTimer(long remainingTime) {
        mRemainintTime = remainingTime;
        String[] timerText = context.getResources().getStringArray(R.array.timer_text);
        countDownTimer = new CountDownTimer(remainingTime, countDownIntervalInMillis) {
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 0) {
                    onFinish();
                } else {
                    mRemainintTime = mRemainintTime - 1000;

                    String[] scheduleTime = Utils.formatTimeAndDate(mRemainintTime).split(":");
                    textMsg.setVisibility(VISIBLE);

                    if (outerLayout != null) {
                        for (int i = 0; i < outerLayout.getChildCount(); i++) {
                            LinearLayout childLinearLayout = (LinearLayout) outerLayout.getChildAt(i);
                            TextView time = ((TextView) childLinearLayout.getChildAt(0));
                            TextView timeFormat = ((TextView) childLinearLayout.getChildAt(1));

                            time.setText(scheduleTime[i]);
                            time.setTypeface(Utils.getSpecificTypeface(context,
                                    appCMSPresenter,
                                    context.getString(R.string.app_cms_page_font_bold_key)));
                            time.setTextColor(appCMSPresenter.getGeneralTextColor());

                            timeFormat.setText(timerText[i]);
                            timeFormat.setTextSize(14);
                            time.setTextColor(appCMSPresenter.getGeneralTextColor());
                        }
                    } else {
                        if (countDownTimer != null)
                            countDownTimer.onFinish();
                    }
                }
            }

            public void onFinish() {
                if (appCMSPresenter.getCurrentActivity() != null
                        && appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_start_watching) != null
                        && mCallBack == null) {
                    Button startWatchingBtn = appCMSPresenter.getCurrentActivity().findViewById(R.id.btn_start_watching);
                    startWatchingBtn.setEnabled(true);
                    startWatchingBtn.setFocusable(true);
                    startWatchingBtn.requestFocus();
                }
                if (appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_summary_module_id) != null) {
                    appCMSPresenter.getCurrentActivity().findViewById(R.id.fight_summary_module_id).setVisibility(View.VISIBLE);
                }
                if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off) != null) {
                    TextView timerTile = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_until_face_off);
                    LinearLayout linearLayout = appCMSPresenter.getCurrentActivity().findViewById(R.id.timer_id);
                    timerTile.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                }
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                //By refreshing the page ,It will check all conditions again and set the data
                appCMSPresenter.sendRefreshPageAction();
                if(mCallBack != null){
                    mCallBack.call();
                }
            }
        }.start();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        if (countDownTimer != null) {
            System.out.println("TestVinit onDetachedFromWindow timer has stopped....");
            countDownTimer.cancel();
        }
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}

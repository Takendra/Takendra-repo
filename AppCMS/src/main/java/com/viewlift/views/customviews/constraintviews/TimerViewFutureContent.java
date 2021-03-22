package com.viewlift.views.customviews.constraintviews;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.ViewCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimerViewFutureContent extends ConstraintLayout {


    public interface FutureEventCountdownListener {
        void countDownOver();

        void closeClick();
    }

    FutureEventCountdownListener countdownListener;
    TimerViewFutureContent timerViewFutureContent;

    @Inject
    AppPreference appPreference;

    @Inject
    AppCMSPresenter appCMSPresenter;

    @BindView(R.id.textViewDayCount)
    AppCompatTextView textViewDayCount;

    @BindView(R.id.textViewHourCount)
    AppCompatTextView textViewHourCount;

    @BindView(R.id.textViewMinuteCount)
    AppCompatTextView textViewMinuteCount;

    @BindView(R.id.textViewSecondCount)
    AppCompatTextView textViewSecondCount;

    @BindView(R.id.textViewDay)
    AppCompatTextView textViewDay;

    @BindView(R.id.textViewHour)
    AppCompatTextView textViewHour;

    @BindView(R.id.textViewMinute)
    AppCompatTextView textViewMinute;

    @BindView(R.id.textViewSecond)
    AppCompatTextView textViewSecond;

    @BindView(R.id.textViewTimerMessage)
    AppCompatTextView textViewTimerMessage;

    @BindView(R.id.day_container)
    LinearLayoutCompat day_container;

    @BindView(R.id.hour_container)
    LinearLayoutCompat hour_container;

    @BindView(R.id.minute_container)
    LinearLayoutCompat minute_container;

    @BindView(R.id.second_container)
    LinearLayoutCompat second_container;

    @BindView(R.id.timerBgImage)
    AppCompatImageView timerBgImage;

    @BindView(R.id.app_cms_video_player_done_button)
    AppCompatImageButton closePlayer;

    @BindView(R.id.timerOverlay)
    View timerOverlay;

    private final int countDownIntervalInMillis = 1000;

    Context mContext;

    public TimerViewFutureContent(Context context) {
        super(context);
        mContext = context;
    }

    public TimerViewFutureContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TimerViewFutureContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    public void initializeView(FutureEventCountdownListener futureEventCountdownListener, String coverImage) {
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);

        int timerViewId = R.layout.future_content_timer;
        LayoutInflater.from(mContext).inflate(timerViewId, this);
        ButterKnife.bind(this);

        this.countdownListener = futureEventCountdownListener;
        timerViewFutureContent = this;

        String message = appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.SPORTS ?
                appCMSPresenter.getLocalisedStrings().getFaceOffText() :
                appCMSPresenter.getLocalisedStrings().getContentAvailableText();
        textViewTimerMessage.setText(message);
        textViewTimerMessage.setTextColor(appCMSPresenter.getGeneralTextColor());

        textViewDayCount.setTextColor(appCMSPresenter.getGeneralTextColor());
        textViewHourCount.setTextColor(appCMSPresenter.getGeneralTextColor());
        textViewMinuteCount.setTextColor(appCMSPresenter.getGeneralTextColor());
        textViewSecondCount.setTextColor(appCMSPresenter.getGeneralTextColor());

        textViewDay.setTextColor(appCMSPresenter.getGeneralTextColor() + 0x92000000);
        textViewHour.setTextColor(appCMSPresenter.getGeneralTextColor() + 0x92000000);
        textViewMinute.setTextColor(appCMSPresenter.getGeneralTextColor() + 0x92000000);
        textViewSecond.setTextColor(appCMSPresenter.getGeneralTextColor() + 0x92000000);

        day_container.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        hour_container.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        minute_container.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        second_container.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());

        Component component = new Component();
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewDay);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewHour);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewMinute);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewSecond);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewTimerMessage);
        component.setFontWeight(mContext.getString(R.string.app_cms_page_font_bold_key));
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewDayCount);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewHourCount);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewMinuteCount);
        ViewCreator.setTypeFace(mContext, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, textViewSecondCount);

        try {
            if (coverImage != null && !TextUtils.isEmpty(coverImage)) {
                Glide.with(mContext).load(coverImage).into(timerBgImage);
            } else {
                this.setBackgroundColor(Color.TRANSPARENT);
                timerOverlay.setBackgroundColor(Color.TRANSPARENT);
                timerBgImage.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        // startTimer(mContext, 1578790800000l, 7485418869l);

        //startTimer(mContext, eventDate, remainingTime, isEventSchedule);

        closePlayer.setOnClickListener(v -> {
            if (countdownListener != null)
                countdownListener.closeClick();
        });
    }

    public void startTimer(Context context, long eventDate, long remainingTime, boolean isEventSchedule) {
        ViewCreator.stopCountdownTimer();
        ViewCreator.countDownTimer = new CountDownTimer(remainingTime, countDownIntervalInMillis) {
            public void onTick(long millisUntilFinished) {
                long different = isEventSchedule
                        ? CommonUtils.getTimeIntervalForEventSchedule(eventDate * 1000L, "EEE MMM dd HH:mm:ss")
                        : CommonUtils.getTimeIntervalForEvent(eventDate, "EEE MMM dd HH:mm:ss");

                if (different < 0 && ViewCreator.countDownTimer != null) {

                    onFinish();
                } else if (different > 0) {
                    String[] scheduleTime = appCMSPresenter.geTimeFormat(different, false).split(":");
                    String[] timerText = context.getResources().getStringArray(R.array.timer_text);


                    textViewDayCount.setText(scheduleTime[0]);
                    textViewHourCount.setText(scheduleTime[1]);
                    textViewMinuteCount.setText(scheduleTime[2]);
                    textViewSecondCount.setText(scheduleTime[3]);

                    textViewDay.setText(timerText[0]);
                    textViewHour.setText(timerText[1]);
                    textViewMinute.setText(timerText[2]);
                    textViewSecond.setText(timerText[3]);

                }
            }

            public void onFinish() {
                if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null &&
                        appCMSPresenter.getCurrentActivity().findViewById(R.id.watch_live_button) != null) {
                    Button watchLive = appCMSPresenter.getCurrentActivity().findViewById(R.id.watch_live_button);
                    watchLive.setVisibility(View.VISIBLE);
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
                if (ViewCreator.countDownTimer != null) {
                    ViewCreator.stopCountdownTimer();
                    ViewCreator.countDownTimer = null;
                }
                if (countdownListener != null) {
                    countdownListener.countDownOver();
                } else {
                    timerViewFutureContent.setVisibility(GONE);
                }
                //By refreshing the page ,It will check all conditions again and set the data
                appCMSPresenter.sendRefreshPageAction();

                if (appCMSPresenter.videoPlayerView != null) {
                    appCMSPresenter.videoPlayerView.refreshVideo();
                }

            }
        }.start();
    }

    public void showCloseIcon(boolean show) {
        if (!show)
            closePlayer.setVisibility(GONE);
        else
            closePlayer.setVisibility(VISIBLE);

    }
}

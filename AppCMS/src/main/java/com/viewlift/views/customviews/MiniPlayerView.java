package com.viewlift.views.customviews;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;

/**
 * Created by sandeep.singh on 11/16/2017.
 */

public class MiniPlayerView extends RelativeLayout implements Animation.AnimationListener {

    ImageButton closePlayer;
    ImageButton volumeButton;
    private AppCMSPresenter appCMSPresenter;
    private Context context;
    private LayoutParams lpPipView;
    private RecyclerView mRecyclerView;
    private Animation animBottomSlide;
    String pageId;


    public MiniPlayerView(Context context,
                          AppCMSPresenter appCMSPresenter, final View recyclerView, String pageId) {
        super(context);
        mRecyclerView = (RecyclerView) recyclerView;
        this.context = context;
        this.pageId = pageId;
        this.appCMSPresenter = appCMSPresenter;
        setLayoutParams(new LayoutParams(BaseView.dpToPx(R.dimen.app_cms_mini_player_width, context),
                BaseView.dpToPx(R.dimen.app_cms_mini_player_height, context)));
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFF000000); //black background
        border.setStroke(1, 0xFFFFFFFF); //white border with full opacity
        setBackground(border);
        setPadding(2, 2, 2, 2);
        addCloseButton();
        if (appCMSPresenter.isNewsTemplate()) {
            addVolumeButton();
        }
    }


    public void init() {
        appCMSPresenter.setAppOrientation();
        lpPipView = new LayoutParams(BaseView.dpToPx(R.dimen.app_cms_mini_player_width, context),
                BaseView.dpToPx(R.dimen.app_cms_mini_player_height, context));
        lpPipView.rightMargin = BaseView.dpToPx(R.dimen.app_cms_mini_player_margin, context);
        lpPipView.bottomMargin = BaseView.dpToPx(R.dimen.app_cms_mini_player_margin, context);
        lpPipView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lpPipView.addRule(RelativeLayout.ABOVE, R.id.app_cms_tab_nav_container);
        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFF000000); //black background
        border.setStroke(1, 0xFFFFFFFF); //white border with full opacity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(border);
        } else {
            setBackground(border);
        }
        setPadding(2, 2, 2, 2);
        setLayoutParams(lpPipView);
        setVisibility(GONE);
    }

    public void addPlayerToView() {
        if (appCMSPresenter != null &&
                appCMSPresenter.videoPlayerView != null &&
                appCMSPresenter.videoPlayerView.getParent() != null) {
            appCMSPresenter.videoPlayerViewParent = (ViewGroup) appCMSPresenter.videoPlayerView.getParent();
            ((ViewGroup) appCMSPresenter.videoPlayerView.getParent()).removeView(appCMSPresenter.videoPlayerView);
        }
        removeAllViews();
        appCMSPresenter.videoPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        appCMSPresenter.videoPlayerView.setClickable(false);
        addView(appCMSPresenter.videoPlayerView);
        addCloseButton();
        if (appCMSPresenter.isNewsTemplate()) {
            addVolumeButton();
        }
        animBottomSlide = AnimationUtils.loadAnimation(context, R.anim.mini_player_slide_bottom);
        this.startAnimation(animBottomSlide);

    }

    private void addCloseButton() {
        int tintColor = appCMSPresenter.getBrandPrimaryCtaColor();
        closePlayer = new ImageButton(context);
        closePlayer.setId(R.id.closeButtonMiniPlayer);
        LayoutParams buttonParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(ALIGN_PARENT_TOP | ALIGN_PARENT_RIGHT);
        closePlayer.setBackground(context.getDrawable(R.drawable.ic_deleteicon));
        closePlayer.getBackground().setTint(tintColor);
        closePlayer.getBackground().setTintMode(PorterDuff.Mode.MULTIPLY);
        addView(closePlayer, buttonParams);
        closePlayer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeWithPause();
            }
        });
    }

    private void addVolumeButton() {
        int tintColor = appCMSPresenter.getBrandPrimaryCtaColor();
        volumeButton = new ImageButton(context);
        volumeButton.setId(R.id.volumeButtonMiniPlayer);
        LayoutParams buttonParams = new LayoutParams(BaseView.dpToPx(R.dimen.app_cms_mini_player_unmute_button_size, getContext()), BaseView.dpToPx(R.dimen.app_cms_mini_player_unmute_button_size, getContext()));
        buttonParams.addRule(ALIGN_PARENT_RIGHT);
        buttonParams.addRule(BELOW, R.id.closeButtonMiniPlayer);
        buttonParams.setMargins(10, 10, 10, 10);
        volumeButton.setBackground(ContextCompat.getDrawable(context,R.drawable.player_mute));
        volumeButton.setBackgroundColor(context.getResources().getColor(R.color.transparentColor));

        addView(volumeButton, buttonParams);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        CommonUtils.DEFAULT_VOLUME_FROM_MINI_PLAYER = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        setPlayerVolumeImage(volumeButton, false, 0);
        volumeObserver = new VolumeObserver(context, new Handler());
        if (volumeObserver != null) {
            getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, volumeObserver);
        }
        volumeButton.setSelected(true);
        volumeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (volumeButton.isSelected()) {
                    volumeButton.setSelected(false);
                    setPlayerVolumeImage(volumeButton, true, CommonUtils.UNMUTE_DEFAULT_VOLUME);
                } else {
                    volumeButton.setSelected(true);
                    setPlayerVolumeImage(volumeButton, false, 0);
                }
            }
        });
    }

    public void pipClick() {
        //if (appCMSPresenter.isHomePage(pageId)) {
        mRecyclerView.smoothScrollToPosition(0);
        if (appCMSPresenter != null
                && appCMSPresenter.getCurrentActivity().findViewById(R.id.home_nested_scroll_view) != null) {
            ((RecyclerView) appCMSPresenter.getCurrentActivity().findViewById(R.id.home_nested_scroll_view)).smoothScrollToPosition(0);
        }
        // }
        //relativeLayoutEvent.startAnimation(animMoveUp);
    }

    private void removeWithPause() {
        if (appCMSPresenter.videoPlayerView != null &&
                appCMSPresenter.videoPlayerView.getPlayerView() != null) {
            appCMSPresenter.videoPlayerView.pausePlayer();
            appCMSPresenter.unrestrictPortraitOnly();
            appCMSPresenter.dismissPopupWindowPlayer(true);
            appCMSPresenter.setMiniPLayerVisibility(false);
        }
        if (volumeObserver != null && appCMSPresenter.isNewsTemplate()) {
            getContext().getContentResolver().unregisterContentObserver(volumeObserver);

        }
        if(appCMSPresenter.getMiniPlayerToggleView()!=null && appCMSPresenter.isNewsTemplate()){
            appCMSPresenter.getMiniPlayerToggleView().setChecked(false);
            appCMSPresenter.getAppPreference().setShowPIPVisibility(false);
        }
    }

    public void removeminiplayer() {
        if (appCMSPresenter.videoPlayerView != null &&
                appCMSPresenter.videoPlayerView.getPlayerView() != null) {
            appCMSPresenter.videoPlayerView.pausePlayer();
            appCMSPresenter.unrestrictPortraitOnly();
            appCMSPresenter.dismissPopupWindowPlayer(true);
            appCMSPresenter.setMiniPLayerVisibility(false);
        }
        if (volumeObserver != null && appCMSPresenter.isNewsTemplate()) {
            getContext().getContentResolver().unregisterContentObserver(volumeObserver);

        }
    }

    public void adjustAudio() {
        if (appCMSPresenter.isNewsTemplate()) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, CommonUtils.DEFAULT_VOLUME_FROM_MINI_PLAYER, 0);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public class OnSwipeTouchListener implements OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context ctx) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
    }

    AudioManager audioManager;
    VolumeObserver volumeObserver;

    public class VolumeObserver extends ContentObserver {

        Context mContext;

        public VolumeObserver(Context context, Handler handler) {
            super(handler);
            mContext = context;
            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            int currentVal = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVal == 0) {
                volumeButton.setSelected(true);
                volumeButton.setBackground(ContextCompat.getDrawable(context,R.drawable.player_mute));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                volumeButton.setSelected(false);
                volumeButton.setBackground(ContextCompat.getDrawable(context,R.drawable.player_volume));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVal, 0);
            }
        }

    }

    int phoneVolume;

    void setPlayerVolumeImage(ImageButton volumeImage, boolean defaultVolume, int defaultVolumeValue) {
        int currentVal = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        phoneVolume = currentVal;

        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


        if (defaultVolume) {
            if (defaultVolumeValue != 0) {
                defaultVolumeValue = (maxVol / 2);
                if (volumeButton != null) {
                    volumeButton.setSelected(true);
                    volumeButton.setBackground(ContextCompat.getDrawable(context,R.drawable.player_mute));
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, defaultVolumeValue, 0);
            } else if (currentVal == 0) {
                if (volumeButton != null) {
                    volumeButton.setSelected(true);
                    volumeButton.setBackground(ContextCompat.getDrawable(context,R.drawable.player_mute));
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                if (volumeButton != null) {
                    volumeButton.setSelected(false);
                    volumeButton.setBackground(ContextCompat.getDrawable(context,R.drawable.player_volume));
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVal, 0);
            }
        } else {
            if (defaultVolumeValue == 0) {
                volumeImage.setBackground(ContextCompat.getDrawable(context,R.drawable.player_mute));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else if (volumeImage.isSelected()) {
                volumeImage.setBackground(ContextCompat.getDrawable(context,R.drawable.player_mute));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            } else {
                volumeImage.setBackground(ContextCompat.getDrawable(context,R.drawable.player_volume));
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, phoneVolume, 0);
            }
        }

    }
}

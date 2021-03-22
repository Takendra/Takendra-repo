package com.viewlift.views.customviews.exoplayerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultControlDispatcher;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.RepeatModeUtil;
import com.google.android.exoplayer2.util.Util;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.customviews.CustomVideoPlayerView;
import com.viewlift.views.listener.PlayerPlayPauseState;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

/**
 * Created by sandeep.singh on 11/24/17.
 */

public class CustomPlayerControlView extends FrameLayout {

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ui");
    }

    /**
     * Listener to be notified about changes of the visibility of the UI control.
     */
    public interface VisibilityListener {

        /**
         * Called when the visibility changes.
         *
         * @param visibility The new visibility. Either {@link View#VISIBLE} or {@link View#GONE}.
         */
        void onVisibilityChange(int visibility);
    }

    /**
     * Listener to be notified when progress has been updated.
     */
    public interface ProgressUpdateListener {

        /**
         * Called when progress needs to be updated.
         *
         * @param position         The current position.
         * @param bufferedPosition The current buffered position.
         */
        void onProgressUpdate(long position, long bufferedPosition);
    }

    /**
     * The default fast forward increment, in milliseconds.
     */
    public static final int DEFAULT_FAST_FORWARD_MS = 15000;
    /**
     * The default rewind increment, in milliseconds.
     */
    public static final int DEFAULT_REWIND_MS = 15000;
    /**
     * The default show timeout, in milliseconds.
     */
    public static final int DEFAULT_SHOW_TIMEOUT_MS = 5000;
    /**
     * The default repeat toggle modes.
     */
    public static final @RepeatModeUtil.RepeatToggleModes
    int DEFAULT_REPEAT_TOGGLE_MODES =
            RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE;
    /**
     * The default minimum interval between time bar position updates.
     */
    public static final int DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS = 200;
    /**
     * The maximum number of windows that can be shown in a multi-window time bar.
     */
    public static final int MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR = 100;

    private static final long MAX_POSITION_FOR_SEEK_TO_PREVIOUS = 3000;
    /**
     * The maximum interval between time bar position updates.
     */
    private static final int MAX_UPDATE_INTERVAL_MS = 1000;

    private final ComponentListener componentListener;
    private final CopyOnWriteArrayList<VisibilityListener> visibilityListeners;
    private final View previousButton;
    private final View nextButton;
    private final View playButton;
    private final View pauseButton;
    private final View playButtonSecond;
    private final View pauseButtonSecond;
    private final View fastForwardButton;
    private final View rewindButton;
    private final View progressController;

    private final ImageView repeatToggleButton;
    private final ImageView shuffleButton;
    private final View vrButton;
    private final TextView durationView;
    private final TextView positionView;
    private final TimeBar timeBar;
    private final StringBuilder formatBuilder;
    private final Formatter formatter;
    private final Timeline.Period period;
    private final Timeline.Window window;
    private final Runnable updateProgressAction;
    private final Runnable hideAction;

    private final Drawable repeatOffButtonDrawable;
    private final Drawable repeatOneButtonDrawable;
    private final Drawable repeatAllButtonDrawable;
    private final String repeatOffButtonContentDescription;
    private final String repeatOneButtonContentDescription;
    private final String repeatAllButtonContentDescription;
    private final Drawable shuffleOnButtonDrawable;
    private final Drawable shuffleOffButtonDrawable;
    private final float buttonAlphaEnabled;
    private final float buttonAlphaDisabled;
    private final String shuffleOnContentDescription;
    private final String shuffleOffContentDescription;

    @Nullable
    private Player player;
    private com.google.android.exoplayer2.ControlDispatcher controlDispatcher;
    @Nullable
    private com.google.android.exoplayer2.ui.PlayerControlView.VisibilityListener visibilityListener;
    @Nullable
    private ProgressUpdateListener progressUpdateListener;
    @Nullable
    private PlaybackPreparer playbackPreparer;

    private boolean isAttachedToWindow;
    private boolean showMultiWindowTimeBar;
    private boolean multiWindowTimeBar;
    private boolean scrubbing;
    private boolean isPlayingLive;
    private int rewindMs;
    private int fastForwardMs;
    private int showTimeoutMs;
    private int timeBarMinUpdateIntervalMs;
    private @RepeatModeUtil.RepeatToggleModes
    int repeatToggleModes;
    private boolean showShuffleButton;
    private long hideAtMs;
    private long[] adGroupTimesMs;
    private boolean[] playedAdGroups;
    private long[] extraAdGroupTimesMs;
    private boolean[] extraPlayedAdGroups;
    private long currentWindowOffset;
    public static final int DEFAULT_REWIND_MS_NEWS = 10000;
    public static final int DEFAULT_FAST_FORWARD_MS_NEWS = 10000;
    @Inject
    AppCMSPresenter appCMSPresenter;

    public CustomPlayerControlView(Context context) {
        this(context, /* attrs= */ null);
    }

    public CustomPlayerControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPlayerControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, attrs);
    }

    public CustomPlayerControlView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr,
            @Nullable AttributeSet playbackAttrs) {
        super(context, attrs, defStyleAttr);
        // int controllerLayoutId = com.google.android.exoplayer2.ui.R.layout.exo_player_control_view;
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        int controllerLayoutId = R.layout.custom_exo_playback_control_view;
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            controllerLayoutId = R.layout.custom_exo_playback_control_view_news;
        }
        rewindMs = DEFAULT_REWIND_MS;
        fastForwardMs = DEFAULT_FAST_FORWARD_MS;
        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS) {
            rewindMs = DEFAULT_REWIND_MS_NEWS;
            fastForwardMs = DEFAULT_FAST_FORWARD_MS_NEWS;
        }
        showTimeoutMs = DEFAULT_SHOW_TIMEOUT_MS;
        repeatToggleModes = DEFAULT_REPEAT_TOGGLE_MODES;
        timeBarMinUpdateIntervalMs = DEFAULT_TIME_BAR_MIN_UPDATE_INTERVAL_MS;
        hideAtMs = C.TIME_UNSET;
        showShuffleButton = false;
        if (playbackAttrs != null) {
            TypedArray a =
                    context
                            .getTheme()
                            .obtainStyledAttributes(playbackAttrs, com.google.android.exoplayer2.ui.R.styleable.PlayerControlView, 0, 0);
            try {
                rewindMs = a.getInt(com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_rewind_increment, rewindMs);
                fastForwardMs =
                        a.getInt(com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_fastforward_increment, fastForwardMs);

                showTimeoutMs = a.getInt(com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_show_timeout, showTimeoutMs);
               /* controllerLayoutId =
                        a.getResourceId(com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_controller_layout_id, controllerLayoutId);*/
                repeatToggleModes = getRepeatToggleModes(a, repeatToggleModes);
                showShuffleButton =
                        a.getBoolean(com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_show_shuffle_button, showShuffleButton);
                setTimeBarMinUpdateInterval(
                        a.getInt(
                                com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_time_bar_min_update_interval,
                                timeBarMinUpdateIntervalMs));
            } finally {
                a.recycle();
            }
        }
        visibilityListeners = new CopyOnWriteArrayList<>();
        period = new Timeline.Period();
        window = new Timeline.Window();
        formatBuilder = new StringBuilder();
        formatter = new Formatter(formatBuilder, Locale.getDefault());
        adGroupTimesMs = new long[0];
        playedAdGroups = new boolean[0];
        extraAdGroupTimesMs = new long[0];
        extraPlayedAdGroups = new boolean[0];
        componentListener = new ComponentListener();
        controlDispatcher = new DefaultControlDispatcher();
        updateProgressAction = this::updateProgress;
        hideAction = this::hide;

        LayoutInflater.from(context).inflate(controllerLayoutId, this);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);

        TimeBar customTimeBar = findViewById(com.google.android.exoplayer2.ui.R.id.exo_progress);
        View timeBarPlaceholder = findViewById(com.google.android.exoplayer2.ui.R.id.exo_progress_placeholder);
        if (customTimeBar != null) {
            timeBar = customTimeBar;
        } else if (timeBarPlaceholder != null) {
            // Propagate attrs as timebarAttrs so that DefaultTimeBar's custom attributes are transferred,
            // but standard attributes (e.g. background) are not.
            DefaultTimeBar defaultTimeBar = new DefaultTimeBar(context, null, 0, playbackAttrs);
            defaultTimeBar.setId(com.google.android.exoplayer2.ui.R.id.exo_progress);
            defaultTimeBar.setLayoutParams(timeBarPlaceholder.getLayoutParams());
            ViewGroup parent = ((ViewGroup) timeBarPlaceholder.getParent());
            int timeBarIndex = parent.indexOfChild(timeBarPlaceholder);
            parent.removeView(timeBarPlaceholder);
            parent.addView(defaultTimeBar, timeBarIndex);
            timeBar = defaultTimeBar;
        } else {
            timeBar = null;
        }
        durationView = findViewById(com.google.android.exoplayer2.ui.R.id.exo_duration);
        positionView = findViewById(com.google.android.exoplayer2.ui.R.id.exo_position);

        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS){
            playButtonSecond = findViewById(R.id.custom_exo_play);
            if (playButtonSecond != null) {
                playButtonSecond.setVisibility(VISIBLE);
                playButtonSecond.setOnClickListener(componentListener);
            }
            pauseButtonSecond = findViewById(R.id.custom_exo_pause);
            if (pauseButtonSecond != null) {
                pauseButtonSecond.setVisibility(VISIBLE);
                pauseButtonSecond.setOnClickListener(componentListener);
            }
        }else {
            playButtonSecond = null;
            pauseButtonSecond = null;
        }
        if (timeBar != null) {
            timeBar.addListener(componentListener);
        }
        playButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_play);
        if (playButton != null) {
            playButton.setOnClickListener(componentListener);
        }
        pauseButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_pause);
        if (pauseButton != null) {
            pauseButton.setOnClickListener(componentListener);
        }
        previousButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_prev);
        if (previousButton != null) {
            previousButton.setOnClickListener(componentListener);
        }
        nextButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_next);
        if (nextButton != null) {
            nextButton.setOnClickListener(componentListener);
        }
        rewindButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_rew);
        if (rewindButton != null) {
            rewindButton.setOnClickListener(componentListener);
        }
        fastForwardButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_ffwd);
        if (fastForwardButton != null) {
            fastForwardButton.setOnClickListener(componentListener);
        }
        repeatToggleButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_repeat_toggle);
        if (repeatToggleButton != null) {
            repeatToggleButton.setOnClickListener(componentListener);
        }
        shuffleButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_shuffle);
        if (shuffleButton != null) {
            shuffleButton.setOnClickListener(componentListener);
        }
        vrButton = findViewById(com.google.android.exoplayer2.ui.R.id.exo_vr);
        setShowVrButton(false);

        progressController = findViewById(com.google.android.exoplayer2.ui.R.id.seek_bar_parent);

        Resources resources = context.getResources();

        buttonAlphaEnabled =
                (float) resources.getInteger(com.google.android.exoplayer2.ui.R.integer.exo_media_button_opacity_percentage_enabled) / 100;
        buttonAlphaDisabled =
                (float) resources.getInteger(com.google.android.exoplayer2.ui.R.integer.exo_media_button_opacity_percentage_disabled) / 100;

        repeatOffButtonDrawable = resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_off);
        repeatOneButtonDrawable = resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_one);
        repeatAllButtonDrawable = resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_controls_repeat_all);
        shuffleOnButtonDrawable = resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_controls_shuffle_on);
        shuffleOffButtonDrawable = resources.getDrawable(com.google.android.exoplayer2.ui.R.drawable.exo_controls_shuffle_off);
        repeatOffButtonContentDescription =
                resources.getString(com.google.android.exoplayer2.ui.R.string.exo_controls_repeat_off_description);
        repeatOneButtonContentDescription =
                resources.getString(com.google.android.exoplayer2.ui.R.string.exo_controls_repeat_one_description);
        repeatAllButtonContentDescription =
                resources.getString(com.google.android.exoplayer2.ui.R.string.exo_controls_repeat_all_description);
        shuffleOnContentDescription =
                resources.getString(com.google.android.exoplayer2.ui.R.string.exo_controls_shuffle_on_description);
        shuffleOffContentDescription =
                resources.getString(com.google.android.exoplayer2.ui.R.string.exo_controls_shuffle_off_description);
    }

    @SuppressWarnings("ResourceType")
    private static @RepeatModeUtil.RepeatToggleModes
    int getRepeatToggleModes(
            TypedArray a, @RepeatModeUtil.RepeatToggleModes int repeatToggleModes) {
        return a.getInt(com.google.android.exoplayer2.ui.R.styleable.PlayerControlView_repeat_toggle_modes, repeatToggleModes);
    }

    /**
     * Returns the {@link Player} currently being controlled by this view, or null if no player is
     * set.
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the {@link Player} to control.
     *
     * @param player The {@link Player} to control, or {@code null} to detach the current player. Only
     *               players which are accessed on the main thread are supported ({@code
     *               player.getApplicationLooper() == Looper.getMainLooper()}).
     */
    public void setPlayer(@Nullable Player player) {
        Assertions.checkState(Looper.myLooper() == Looper.getMainLooper());
        Assertions.checkArgument(
                player == null || player.getApplicationLooper() == Looper.getMainLooper());
        if (this.player == player) {
            return;
        }
        if (this.player != null) {
            this.player.removeListener(componentListener);
        }
        this.player = player;
        if (player != null) {
            player.addListener(componentListener);
        }
        updateAll();
    }

    /**
     * Sets whether the time bar should show all windows, as opposed to just the current one. If the
     * timeline has a period with unknown duration or more than {@link
     * #MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR} windows the time bar will fall back to showing a single
     * window.
     *
     * @param showMultiWindowTimeBar Whether the time bar should show all windows.
     */
    public void setShowMultiWindowTimeBar(boolean showMultiWindowTimeBar) {
        this.showMultiWindowTimeBar = showMultiWindowTimeBar;
        updateTimeline();
    }

    /**
     * Sets the millisecond positions of extra ad markers relative to the start of the window (or
     * timeline, if in multi-window mode) and whether each extra ad has been played or not. The
     * markers are shown in addition to any ad markers for ads in the player's timeline.
     *
     * @param extraAdGroupTimesMs The millisecond timestamps of the extra ad markers to show, or
     *                            {@code null} to show no extra ad markers.
     * @param extraPlayedAdGroups Whether each ad has been played. Must be the same length as {@code
     *                            extraAdGroupTimesMs}, or {@code null} if {@code extraAdGroupTimesMs} is {@code null}.
     */
    public void setExtraAdGroupMarkers(
            @Nullable long[] extraAdGroupTimesMs, @Nullable boolean[] extraPlayedAdGroups) {
        if (extraAdGroupTimesMs == null) {
            this.extraAdGroupTimesMs = new long[0];
            this.extraPlayedAdGroups = new boolean[0];
        } else {
            extraPlayedAdGroups = Assertions.checkNotNull(extraPlayedAdGroups);
            Assertions.checkArgument(extraAdGroupTimesMs.length == extraPlayedAdGroups.length);
            this.extraAdGroupTimesMs = extraAdGroupTimesMs;
            this.extraPlayedAdGroups = extraPlayedAdGroups;
        }
        updateTimeline();
    }

    /**
     * Adds a {@link VisibilityListener}.
     *
     * @param listener The listener to be notified about visibility changes.
     */
    public void addVisibilityListener(VisibilityListener listener) {
        visibilityListeners.add(listener);
    }

    /**
     * Removes a {@link VisibilityListener}.
     *
     * @param listener The listener to be removed.
     */
    public void removeVisibilityListener(VisibilityListener listener) {
        visibilityListeners.remove(listener);
    }

    /**
     * Sets the {@link ProgressUpdateListener}.
     *
     * @param listener The listener to be notified about when progress is updated.
     */
    public void setProgressUpdateListener(@Nullable ProgressUpdateListener listener) {
        this.progressUpdateListener = listener;
    }

    /**
     * Sets the {@link PlaybackPreparer}.
     *
     * @param playbackPreparer The {@link PlaybackPreparer}.
     */
    public void setPlaybackPreparer(@Nullable PlaybackPreparer playbackPreparer) {
        this.playbackPreparer = playbackPreparer;
    }

    /**
     * Sets the {@link com.google.android.exoplayer2.ControlDispatcher}.
     *
     * @param controlDispatcher The {@link com.google.android.exoplayer2.ControlDispatcher}, or null
     *                          to use {@link com.google.android.exoplayer2.DefaultControlDispatcher}.
     */
    public void setControlDispatcher(
            @Nullable com.google.android.exoplayer2.ControlDispatcher controlDispatcher) {
        this.controlDispatcher =
                controlDispatcher == null
                        ? new com.google.android.exoplayer2.DefaultControlDispatcher()
                        : controlDispatcher;
    }

    /**
     * Sets the rewind increment in milliseconds.
     *
     * @param rewindMs The rewind increment in milliseconds. A non-positive value will cause the
     *                 rewind button to be disabled.
     */
    public void setRewindIncrementMs(int rewindMs) {
        this.rewindMs = rewindMs;
        updateNavigation();
    }

    /**
     * Sets the fast forward increment in milliseconds.
     *
     * @param fastForwardMs The fast forward increment in milliseconds. A non-positive value will
     *                      cause the fast forward button to be disabled.
     */
    public void setFastForwardIncrementMs(int fastForwardMs) {
        this.fastForwardMs = fastForwardMs;
        updateNavigation();
    }

    /**
     * Returns the playback controls timeout. The playback controls are automatically hidden after
     * this duration of time has elapsed without user input.
     *
     * @return The duration in milliseconds. A non-positive value indicates that the controls will
     * remain visible indefinitely.
     */
    public int getShowTimeoutMs() {
        return showTimeoutMs;
    }

    /**
     * Sets the playback controls timeout. The playback controls are automatically hidden after this
     * duration of time has elapsed without user input.
     *
     * @param showTimeoutMs The duration in milliseconds. A non-positive value will cause the controls
     *                      to remain visible indefinitely.
     */
    public void setShowTimeoutMs(int showTimeoutMs) {
        this.showTimeoutMs = showTimeoutMs;
        if (isVisible()) {
            // Reset the timeout.
            hideAfterTimeout();
        }
    }

    /**
     * Returns which repeat toggle modes are enabled.
     *
     * @return The currently enabled {@link RepeatModeUtil.RepeatToggleModes}.
     */
    public @RepeatModeUtil.RepeatToggleModes
    int getRepeatToggleModes() {
        return repeatToggleModes;
    }

    /**
     * Sets which repeat toggle modes are enabled.
     *
     * @param repeatToggleModes A set of {@link RepeatModeUtil.RepeatToggleModes}.
     */
    public void setRepeatToggleModes(@RepeatModeUtil.RepeatToggleModes int repeatToggleModes) {
        this.repeatToggleModes = repeatToggleModes;
        if (player != null) {
            @Player.RepeatMode int currentMode = player.getRepeatMode();
            if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE
                    && currentMode != Player.REPEAT_MODE_OFF) {
                controlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_OFF);
            } else if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_ONE
                    && currentMode == Player.REPEAT_MODE_ALL) {
                controlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_ONE);
            } else if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL
                    && currentMode == Player.REPEAT_MODE_ONE) {
                controlDispatcher.dispatchSetRepeatMode(player, Player.REPEAT_MODE_ALL);
            }
        }
        updateRepeatModeButton();
    }

    /**
     * Returns whether the shuffle button is shown.
     */
    public boolean getShowShuffleButton() {
        return showShuffleButton;
    }

    /**
     * Sets whether the shuffle button is shown.
     *
     * @param showShuffleButton Whether the shuffle button is shown.
     */
    public void setShowShuffleButton(boolean showShuffleButton) {
        this.showShuffleButton = showShuffleButton;
        updateShuffleButton();
    }

    /**
     * Returns whether the VR button is shown.
     */
    public boolean getShowVrButton() {
        return vrButton != null && vrButton.getVisibility() == VISIBLE;
    }

    /**
     * Sets whether the VR button is shown.
     *
     * @param showVrButton Whether the VR button is shown.
     */
    public void setShowVrButton(boolean showVrButton) {
        if (vrButton != null) {
            vrButton.setVisibility(showVrButton ? VISIBLE : GONE);
        }
    }

    /**
     * Sets listener for the VR button.
     *
     * @param onClickListener Listener for the VR button, or null to clear the listener.
     */
    public void setVrButtonListener(@Nullable OnClickListener onClickListener) {
        if (vrButton != null) {
            vrButton.setOnClickListener(onClickListener);
        }
    }

    /**
     * Sets the minimum interval between time bar position updates.
     *
     * <p>Note that smaller intervals, e.g. 33ms, will result in a smooth movement but will use more
     * CPU resources while the time bar is visible, whereas larger intervals, e.g. 200ms, will result
     * in a step-wise update with less CPU usage.
     *
     * @param minUpdateIntervalMs The minimum interval between time bar position updates, in
     *                            milliseconds.
     */
    public void setTimeBarMinUpdateInterval(int minUpdateIntervalMs) {
        // Do not accept values below 16ms (60fps) and larger than the maximum update interval.
        timeBarMinUpdateIntervalMs =
                Util.constrainValue(minUpdateIntervalMs, 16, MAX_UPDATE_INTERVAL_MS);
    }

    /**
     * Shows the playback controls. If {@link #getShowTimeoutMs()} is positive then the controls will
     * be automatically hidden after this duration of time has elapsed without user input.
     */
    public void show() {
        if (!isVisible()) {
            setVisibility(VISIBLE);
            for (VisibilityListener visibilityListener : visibilityListeners) {
                visibilityListener.onVisibilityChange(getVisibility());
            }
            updateAll();
            requestPlayPauseFocus();
        }
        // Call hideAfterTimeout even if already visible to reset the timeout.
        hideAfterTimeout();
    }

    /**
     * Hides the controller.
     */
    public void hide() {
        if (isVisible()) {
            setVisibility(GONE);
            for (VisibilityListener visibilityListener : visibilityListeners) {
                visibilityListener.onVisibilityChange(getVisibility());
            }
            removeCallbacks(updateProgressAction);
            removeCallbacks(hideAction);
            hideAtMs = C.TIME_UNSET;
        }
    }

    /**
     * Returns whether the controller is currently visible.
     */
    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    private void hideAfterTimeout() {
        removeCallbacks(hideAction);
        if (showTimeoutMs > 0) {
            hideAtMs = SystemClock.uptimeMillis() + showTimeoutMs;
            if (isAttachedToWindow) {
                postDelayed(hideAction, showTimeoutMs);
            }
        } else {
            hideAtMs = C.TIME_UNSET;
        }
    }

    private void updateAll() {
        updatePlayPauseButton();
        updateNavigation();
        updateRepeatModeButton();
        updateShuffleButton();
        updateTimeline();
    }

    private void updatePlayPauseButton() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }
        boolean requestPlayPauseFocus = false;
        boolean shouldShowPauseButton = shouldShowPauseButton();
        if (playButton != null) {
            requestPlayPauseFocus |= shouldShowPauseButton && playButton.isFocused();
            playButton.setVisibility(shouldShowPauseButton ? GONE : VISIBLE);
        }
        if (pauseButton != null) {
            requestPlayPauseFocus |= !shouldShowPauseButton && pauseButton.isFocused();
            pauseButton.setVisibility(shouldShowPauseButton ? VISIBLE : GONE);
        }

        if (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS){
            if (playButtonSecond != null) {
                requestPlayPauseFocus |= shouldShowPauseButton && playButtonSecond.isFocused();
                playButtonSecond.setVisibility(shouldShowPauseButton ? GONE : VISIBLE);
            }
            if (pauseButtonSecond != null) {
                requestPlayPauseFocus |= !shouldShowPauseButton && pauseButtonSecond.isFocused();
                pauseButtonSecond.setVisibility(shouldShowPauseButton ? VISIBLE : GONE);
            }
            if (isPlayingLive){
                if (playButtonSecond != null) {
                    playButtonSecond.setVisibility(GONE);
                }
                if (pauseButtonSecond != null) {
                    pauseButtonSecond.setVisibility(GONE);
                }
            }
        }
        if (requestPlayPauseFocus) {
            requestPlayPauseFocus();
        }
    }

    private void updateNavigation() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }

        @Nullable Player player = this.player;
        boolean enableSeeking = false;
        boolean enablePrevious = false;
        boolean enableRewind = false;
        boolean enableFastForward = false;
        boolean enableNext = false;
        if (player != null) {
            Timeline timeline = player.getCurrentTimeline();
            if (!timeline.isEmpty() && !player.isPlayingAd()) {
                timeline.getWindow(player.getCurrentWindowIndex(), window);
                boolean isSeekable = window.isSeekable;
                enableSeeking = isSeekable;
                enablePrevious = isSeekable || !window.isDynamic || player.hasPrevious();
                enableRewind = isSeekable && rewindMs > 0;
                enableFastForward = isSeekable && fastForwardMs > 0;
                enableNext = window.isDynamic || player.hasNext();
            }
        }
        if((appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS)&&(playButton.getVisibility()==VISIBLE)&&!isPlayingLive) {
            setViewVisibility(true, fastForwardButton);
            setViewVisibility(true, rewindButton);
        }
        else {
            setButtonEnabled(enablePrevious, previousButton);
            setButtonEnabled(enableRewind, rewindButton);
            setButtonEnabled(enableFastForward, fastForwardButton);
            setButtonEnabled(enableNext, nextButton);
            setViewVisibility(isPlayingLive, fastForwardButton);
            setViewVisibility(isPlayingLive, rewindButton);
        }
        setProgressViewVisibility(isPlayingLive, progressController);

        if (timeBar != null) {
            timeBar.setEnabled(enableSeeking);
        }
    }

    private void updateRepeatModeButton() {
        if (!isVisible() || !isAttachedToWindow || repeatToggleButton == null) {
            return;
        }

        if (repeatToggleModes == RepeatModeUtil.REPEAT_TOGGLE_MODE_NONE) {
            repeatToggleButton.setVisibility(GONE);
            return;
        }

        @Nullable Player player = this.player;
        if (player == null) {
            setButtonEnabled(false, repeatToggleButton);
            repeatToggleButton.setImageDrawable(repeatOffButtonDrawable);
            repeatToggleButton.setContentDescription(repeatOffButtonContentDescription);
            return;
        }

        setButtonEnabled(true, repeatToggleButton);
        switch (player.getRepeatMode()) {
            case Player.REPEAT_MODE_OFF:
                repeatToggleButton.setImageDrawable(repeatOffButtonDrawable);
                repeatToggleButton.setContentDescription(repeatOffButtonContentDescription);
                break;
            case Player.REPEAT_MODE_ONE:
                repeatToggleButton.setImageDrawable(repeatOneButtonDrawable);
                repeatToggleButton.setContentDescription(repeatOneButtonContentDescription);
                break;
            case Player.REPEAT_MODE_ALL:
                repeatToggleButton.setImageDrawable(repeatAllButtonDrawable);
                repeatToggleButton.setContentDescription(repeatAllButtonContentDescription);
                break;
            default:
                // Never happens.
        }
        repeatToggleButton.setVisibility(VISIBLE);
    }

    private void updateShuffleButton() {
        if (!isVisible() || !isAttachedToWindow || shuffleButton == null) {
            return;
        }

        @Nullable Player player = this.player;
        if (!showShuffleButton) {
            shuffleButton.setVisibility(GONE);
        } else if (player == null) {
            setButtonEnabled(false, shuffleButton);
            shuffleButton.setImageDrawable(shuffleOffButtonDrawable);
            shuffleButton.setContentDescription(shuffleOffContentDescription);
        } else {
            setButtonEnabled(true, shuffleButton);
            shuffleButton.setImageDrawable(
                    player.getShuffleModeEnabled() ? shuffleOnButtonDrawable : shuffleOffButtonDrawable);
            shuffleButton.setContentDescription(
                    player.getShuffleModeEnabled()
                            ? shuffleOnContentDescription
                            : shuffleOffContentDescription);
        }
    }

    private void updateTimeline() {
        @Nullable Player player = this.player;
        if (player == null) {
            return;
        }
        multiWindowTimeBar =
                showMultiWindowTimeBar && canShowMultiWindowTimeBar(player.getCurrentTimeline(), window);
        currentWindowOffset = 0;
        long durationUs = 0;
        int adGroupCount = 0;
        Timeline timeline = player.getCurrentTimeline();
        if (!timeline.isEmpty()) {
            int currentWindowIndex = player.getCurrentWindowIndex();
            int firstWindowIndex = multiWindowTimeBar ? 0 : currentWindowIndex;
            int lastWindowIndex = multiWindowTimeBar ? timeline.getWindowCount() - 1 : currentWindowIndex;
            for (int i = firstWindowIndex; i <= lastWindowIndex; i++) {
                if (i == currentWindowIndex) {
                    currentWindowOffset = C.usToMs(durationUs);
                }
                timeline.getWindow(i, window);
                if (window.durationUs == C.TIME_UNSET) {
                    Assertions.checkState(!multiWindowTimeBar);
                    break;
                }
                for (int j = window.firstPeriodIndex; j <= window.lastPeriodIndex; j++) {
                    timeline.getPeriod(j, period);
                    int periodAdGroupCount = period.getAdGroupCount();
                    for (int adGroupIndex = 0; adGroupIndex < periodAdGroupCount; adGroupIndex++) {
                        long adGroupTimeInPeriodUs = period.getAdGroupTimeUs(adGroupIndex);
                        if (adGroupTimeInPeriodUs == C.TIME_END_OF_SOURCE) {
                            if (period.durationUs == C.TIME_UNSET) {
                                // Don't show ad markers for postrolls in periods with unknown duration.
                                continue;
                            }
                            adGroupTimeInPeriodUs = period.durationUs;
                        }
                        long adGroupTimeInWindowUs = adGroupTimeInPeriodUs + period.getPositionInWindowUs();
                        if (adGroupTimeInWindowUs >= 0 && adGroupTimeInWindowUs <= window.durationUs) {
                            if (adGroupCount == adGroupTimesMs.length) {
                                int newLength = adGroupTimesMs.length == 0 ? 1 : adGroupTimesMs.length * 2;
                                adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, newLength);
                                playedAdGroups = Arrays.copyOf(playedAdGroups, newLength);
                            }
                            adGroupTimesMs[adGroupCount] = C.usToMs(durationUs + adGroupTimeInWindowUs);
                            playedAdGroups[adGroupCount] = period.hasPlayedAdGroup(adGroupIndex);
                            adGroupCount++;
                        }
                    }
                }
                durationUs += window.durationUs;
            }
        }
        long durationMs = C.usToMs(durationUs);
        if (durationView != null) {
            durationView.setText(Util.getStringForTime(formatBuilder, formatter, durationMs));
        }
        if (timeBar != null) {
            timeBar.setDuration(durationMs);
            int extraAdGroupCount = extraAdGroupTimesMs.length;
            int totalAdGroupCount = adGroupCount + extraAdGroupCount;
            if (totalAdGroupCount > adGroupTimesMs.length) {
                adGroupTimesMs = Arrays.copyOf(adGroupTimesMs, totalAdGroupCount);
                playedAdGroups = Arrays.copyOf(playedAdGroups, totalAdGroupCount);
            }
            System.arraycopy(extraAdGroupTimesMs, 0, adGroupTimesMs, adGroupCount, extraAdGroupCount);
            System.arraycopy(extraPlayedAdGroups, 0, playedAdGroups, adGroupCount, extraAdGroupCount);
            timeBar.setAdGroupTimesMs(adGroupTimesMs, playedAdGroups, totalAdGroupCount);
        }
        updateProgress();
    }

    private void updateProgress() {
        if (!isVisible() || !isAttachedToWindow) {
            return;
        }

        @Nullable Player player = this.player;
        long position = 0;
        long bufferedPosition = 0;
        if (player != null) {
            position = currentWindowOffset + player.getContentPosition();
            bufferedPosition = currentWindowOffset + player.getContentBufferedPosition();
        }
        if (positionView != null && !scrubbing) {
            positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
        }
        if (timeBar != null) {
            timeBar.setPosition(position);
            timeBar.setBufferedPosition(bufferedPosition);
        }
        if (progressUpdateListener != null) {
            progressUpdateListener.onProgressUpdate(position, bufferedPosition);
        }

        // Cancel any pending updates and schedule a new one if necessary.
        removeCallbacks(updateProgressAction);
        int playbackState = player == null ? Player.STATE_IDLE : player.getPlaybackState();
        if (playbackState == Player.STATE_READY && player.getPlayWhenReady()) {
            long mediaTimeDelayMs =
                    timeBar != null ? timeBar.getPreferredUpdateDelay() : MAX_UPDATE_INTERVAL_MS;

            // Limit delay to the start of the next full second to ensure position display is smooth.
            long mediaTimeUntilNextFullSecondMs = 1000 - position % 1000;
            mediaTimeDelayMs = Math.min(mediaTimeDelayMs, mediaTimeUntilNextFullSecondMs);

            // Calculate the delay until the next update in real time, taking playbackSpeed into account.
            float playbackSpeed = player.getPlaybackParameters().speed;
            long delayMs =
                    playbackSpeed > 0 ? (long) (mediaTimeDelayMs / playbackSpeed) : MAX_UPDATE_INTERVAL_MS;

            // Constrain the delay to avoid too frequent / infrequent updates.
            delayMs = Util.constrainValue(delayMs, timeBarMinUpdateIntervalMs, MAX_UPDATE_INTERVAL_MS);
            postDelayed(updateProgressAction, delayMs);
        } else if (playbackState != Player.STATE_ENDED && playbackState != Player.STATE_IDLE) {
            postDelayed(updateProgressAction, MAX_UPDATE_INTERVAL_MS);
        }
    }

    private void requestPlayPauseFocus() {
        boolean shouldShowPauseButton = shouldShowPauseButton();
        if (!shouldShowPauseButton && playButton != null) {
            playButton.requestFocus();
        } else if (shouldShowPauseButton && pauseButton != null) {
            pauseButton.requestFocus();
        }
    }

    private void setButtonEnabled(boolean enabled, View view) {
        if (view == null) {
            return;
        }
        view.setEnabled(enabled);
        view.setAlpha(enabled ? 1f : 0.3f);
        view.setVisibility(VISIBLE);
    }

    private void setViewVisibility(boolean visibility, View view) {
        view.setVisibility(visibility ? GONE : VISIBLE);
    }

    private void setProgressViewVisibility(boolean visibility, View view) {
        view.findViewById(R.id.exo_position).setVisibility(visibility ? INVISIBLE : VISIBLE);
        view.findViewById(R.id.exo_progress).setVisibility(visibility ? INVISIBLE : VISIBLE);
        view.findViewById(R.id.exo_duration).setVisibility(visibility ? INVISIBLE : VISIBLE);
    }

    private void previous(Player player) {
        Timeline timeline = player.getCurrentTimeline();
        if (timeline.isEmpty() || player.isPlayingAd()) {
            return;
        }
        int windowIndex = player.getCurrentWindowIndex();
        timeline.getWindow(windowIndex, window);
        int previousWindowIndex = player.getPreviousWindowIndex();
        if (previousWindowIndex != C.INDEX_UNSET
                && (player.getCurrentPosition() <= MAX_POSITION_FOR_SEEK_TO_PREVIOUS
                || (window.isDynamic && !window.isSeekable))) {
            seekTo(player, previousWindowIndex, C.TIME_UNSET);
        } else {
            seekTo(player, windowIndex, /* positionMs= */ 0);
        }
    }

    private void next(Player player) {
        Timeline timeline = player.getCurrentTimeline();
        if (timeline.isEmpty() || player.isPlayingAd()) {
            return;
        }
        int windowIndex = player.getCurrentWindowIndex();
        int nextWindowIndex = player.getNextWindowIndex();
        if (nextWindowIndex != C.INDEX_UNSET) {
            seekTo(player, nextWindowIndex, C.TIME_UNSET);
        } else if (timeline.getWindow(windowIndex, window).isDynamic) {
            seekTo(player, windowIndex, C.TIME_UNSET);
        }
    }

    public void rewind(Player player) {
        if (player.isCurrentWindowSeekable() && rewindMs > 0) {
            seekToOffset(player, -rewindMs);
        }
    }

    public void fastForward(Player player) {
        if (player.isCurrentWindowSeekable() && fastForwardMs > 0) {
            seekToOffset(player, fastForwardMs);
        }
    }

    private void seekToOffset(Player player, long offsetMs) {
        long positionMs = player.getCurrentPosition() + offsetMs;
        long durationMs = player.getDuration();
        if (durationMs != C.TIME_UNSET) {
            positionMs = Math.min(positionMs, durationMs);
        }
        positionMs = Math.max(positionMs, 0);
        seekTo(player, player.getCurrentWindowIndex(), positionMs);
    }

    private void seekToTimeBarPosition(Player player, long positionMs) {
        int windowIndex;
        Timeline timeline = player.getCurrentTimeline();
        if (multiWindowTimeBar && !timeline.isEmpty()) {
            int windowCount = timeline.getWindowCount();
            windowIndex = 0;
            while (true) {
                long windowDurationMs = timeline.getWindow(windowIndex, window).getDurationMs();
                if (positionMs < windowDurationMs) {
                    break;
                } else if (windowIndex == windowCount - 1) {
                    // Seeking past the end of the last window should seek to the end of the timeline.
                    positionMs = windowDurationMs;
                    break;
                }
                positionMs -= windowDurationMs;
                windowIndex++;
            }
        } else {
            windowIndex = player.getCurrentWindowIndex();
        }
        boolean dispatched = seekTo(player, windowIndex, positionMs);
        if (!dispatched) {
            // The seek wasn't dispatched then the progress bar scrubber will be in the wrong position.
            // Trigger a progress update to snap it back.
            updateProgress();
        }
    }

    private boolean seekTo(Player player, int windowIndex, long positionMs) {
        return controlDispatcher.dispatchSeekTo(player, windowIndex, positionMs);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        if (hideAtMs != C.TIME_UNSET) {
            long delayMs = hideAtMs - SystemClock.uptimeMillis();
            if (delayMs <= 0) {
                hide();
            } else {
                postDelayed(hideAction, delayMs);
            }
        } else if (isVisible()) {
            hideAfterTimeout();
        }
        updateAll();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        removeCallbacks(updateProgressAction);
        removeCallbacks(hideAction);
    }

    @Override
    public final boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            removeCallbacks(hideAction);
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            hideAfterTimeout();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return dispatchMediaKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    /**
     * Called to process media key events. Any {@link KeyEvent} can be passed but only media key
     * events will be handled.
     *
     * @param event A key event.
     * @return Whether the key event was handled.
     */
    public boolean dispatchMediaKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        @Nullable Player player = this.player;
        if (player == null || !isHandledMediaKey(keyCode)) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                fastForward(player);
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                rewind(player);
            } else if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        controlDispatcher.dispatchSetPlayWhenReady(player, !player.getPlayWhenReady());
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PLAY:
                        controlDispatcher.dispatchSetPlayWhenReady(player, true);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PAUSE:
                        controlDispatcher.dispatchSetPlayWhenReady(player, false);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        next(player);
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        previous(player);
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }

    private boolean shouldShowPauseButton() {
        return player != null
                && player.getPlaybackState() != Player.STATE_ENDED
                && player.getPlaybackState() != Player.STATE_IDLE
                && player.getPlayWhenReady();
    }

    @SuppressLint("InlinedApi")
    private static boolean isHandledMediaKey(int keyCode) {
        return keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD
                || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
                || keyCode == KeyEvent.KEYCODE_MEDIA_NEXT
                || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS;
    }

    /**
     * Returns whether the specified {@code timeline} can be shown on a multi-window time bar.
     *
     * @param timeline The {@link Timeline} to check.
     * @param window   A scratch {@link Timeline.Window} instance.
     * @return Whether the specified timeline can be shown on a multi-window time bar.
     */
    private static boolean canShowMultiWindowTimeBar(Timeline timeline, Timeline.Window window) {
        if (timeline.getWindowCount() > MAX_WINDOWS_FOR_MULTI_WINDOW_TIME_BAR) {
            return false;
        }
        int windowCount = timeline.getWindowCount();
        for (int i = 0; i < windowCount; i++) {
            if (timeline.getWindow(i, window).durationUs == C.TIME_UNSET) {
                return false;
            }
        }
        return true;
    }

    private final class ComponentListener
            implements Player.EventListener, TimeBar.OnScrubListener, OnClickListener {

        @Override
        public void onScrubStart(TimeBar timeBar, long position) {
            scrubbing = true;
            if (positionView != null) {
                positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
            }
        }

        @Override
        public void onScrubMove(TimeBar timeBar, long position) {
            if (positionView != null) {
                positionView.setText(Util.getStringForTime(formatBuilder, formatter, position));
            }
        }

        @Override
        public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
            scrubbing = false;
            if (!canceled && player != null) {
                seekToTimeBarPosition(player, position);
            }
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, @Player.State int playbackState) {
            updatePlayPauseButton();
            updateProgress();
        }

        @Override
        public void onIsPlayingChanged(boolean isPlaying) {
            updateProgress();
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            updateRepeatModeButton();
            updateNavigation();
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            updateShuffleButton();
            updateNavigation();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            updateNavigation();
            updateTimeline();
        }

        @Override
        public void onTimelineChanged(Timeline timeline, @Player.TimelineChangeReason int reason) {
            updateNavigation();
            updateTimeline();
        }

        @Override
        public void onClick(View view) {
            Player player = CustomPlayerControlView.this.player;
            if (player == null) {
                return;
            }
            if (nextButton == view) {
                next(player);
            } else if (previousButton == view) {
                previous(player);
            } else if (fastForwardButton == view) {
                fastForward(player);
            } else if (rewindButton == view) {
                rewind(player);
            } else if (playButton == view || isSeconderyPlayButton(view)) {
                if (!CommonUtils.isEmpty(appCMSPresenter.getCurrentPlayingVideo()) && appCMSPresenter.isNetworkConnected()) {
                    appCMSPresenter.refreshVideoData(appCMSPresenter.getCurrentPlayingVideo(), (contentDatum) -> {
                        if (player.getPlaybackState() == Player.STATE_IDLE) {
                            if (playbackPreparer != null) {
                                playbackPreparer.preparePlayback();
                            }
                        } else if (player.getPlaybackState() == Player.STATE_ENDED) {
                            seekTo(player, player.getCurrentWindowIndex(), C.TIME_UNSET);
                            // controlDispatcher.dispatchSeekTo(player, player.getCurrentWindowIndex(), C.TIME_UNSET);
                        }
                        controlDispatcher.dispatchSetPlayWhenReady(player, true);
                        if (playerPlayPauseState != null && player.getPlaybackState() == Player.STATE_READY) {
                            playerPlayPauseState.playerState(false);
                        }
                        if (listenerEvents != null) {
                            listenerEvents.getIsVideoPaused(false);
                        }
                    }, null, false, true, null);

                }else {
                    if (player.getPlaybackState() == Player.STATE_IDLE) {
                        if (playbackPreparer != null) {
                            playbackPreparer.preparePlayback();
                        }
                    } else if (player.getPlaybackState() == Player.STATE_ENDED) {
                        seekTo(player, player.getCurrentWindowIndex(), C.TIME_UNSET);
                        // controlDispatcher.dispatchSeekTo(player, player.getCurrentWindowIndex(), C.TIME_UNSET);
                    }
                    controlDispatcher.dispatchSetPlayWhenReady(player, true);
                    if (playerPlayPauseState != null && player.getPlaybackState() == Player.STATE_READY) {
                        playerPlayPauseState.playerState(false);
                    }
                    if (listenerEvents != null) {
                        listenerEvents.getIsVideoPaused(false);
                    }
                }
                if ((appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS ) && !isPlayingLive){
                    rewindButton.setVisibility(VISIBLE);
                    fastForwardButton.setVisibility(VISIBLE);
                }
            } else if (pauseButton == view || isSeconderyPauseButton(view)) {
                controlDispatcher.dispatchSetPlayWhenReady(player, false);
                if (listenerEvents != null) {
                    listenerEvents.getIsVideoPaused(true);
                }
                if (playerPlayPauseState != null) {
                    playerPlayPauseState.playerState(true);
                }
                if ((appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS ) && !isPlayingLive){
                    rewindButton.setVisibility(GONE);
                    fastForwardButton.setVisibility(GONE);
                }
            } else if (repeatToggleButton == view) {
                controlDispatcher.dispatchSetRepeatMode(
                        player, RepeatModeUtil.getNextRepeatMode(player.getRepeatMode(), repeatToggleModes));
            } else if (shuffleButton == view) {
                controlDispatcher.dispatchSetShuffleModeEnabled(player, !player.getShuffleModeEnabled());
            }
        }
    }
    private boolean isSeconderyPlayButton(View view){
        return (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS && playButtonSecond != null && playButtonSecond == view);
    }

    private boolean isSeconderyPauseButton(View view){
        return (appCMSPresenter.getTemplateType() == AppCMSPresenter.TemplateType.NEWS && pauseButtonSecond != null && pauseButtonSecond == view);
    }

    CustomVideoPlayerView.IgetPlayerEvent listenerEvents;

    public void setPlayerEvents(CustomVideoPlayerView.IgetPlayerEvent listenerEvents) {
        this.listenerEvents = listenerEvents;
    }

    public boolean isPlayingLive() {
        return isPlayingLive;
    }

    public void setPlayingLive(boolean playingLive) {
        isPlayingLive = playingLive;
    }

    private PlayerPlayPauseState playerPlayPauseState;

    public void setPlayerPlayPauseState(PlayerPlayPauseState playerPlayPauseState) {
        this.playerPlayPauseState = playerPlayPauseState;
    }

}

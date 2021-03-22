package com.viewlift.views.customviews;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Mobile;
import com.viewlift.models.data.appcms.ui.page.TabletLandscape;
import com.viewlift.models.data.appcms.ui.page.TabletPortrait;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.AppCMSPlaylistAdapter;

import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.*;

/**
 * Created by viewlift on 5/17/17.
 */

public abstract class BaseView extends FrameLayout {
    public static final int STANDARD_MOBILE_WIDTH_PX = 375;
    public static final int STANDARD_MOBILE_HEIGHT_PX = 667;
    public static final int STANDARD_TABLET_WIDTH_PX = 768;
    public static final int STANDARD_TABLET_HEIGHT_PX = 1024;
    protected static final float TABLET_LANDSCAPE_HEIGHT_SCALE = 1.06f;
    protected static int DEVICE_WIDTH;
    protected static int DEVICE_HEIGHT;
    protected static int PLAYER_WIDTH;

    protected ViewGroup childrenContainer;
    protected boolean[] componentHasViewList;


    public BaseView(Context context) {
        super(context);
        PLAYER_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
        DEVICE_WIDTH = getContext().getResources().getDisplayMetrics().widthPixels;
        DEVICE_HEIGHT = getContext().getResources().getDisplayMetrics().heightPixels;
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }

    public static float convertDpToPixel(float dp, Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        return 0.0f;
    }

    public static float convertWidthDpToPixel(Layout layout, Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return convertDpToPixel(layout.getTabletLandscape().getWidth(), context);
            } else {
                return convertDpToPixel(layout.getTabletPortrait().getWidth(), context);
            }
        } else {
            return convertDpToPixel(layout.getMobile().getWidth(), context);
        }
    }

    public static float convertHeightDpToPixel(Layout layout, Context context) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                return convertDpToPixel(layout.getTabletLandscape().getHeight(), context);
            } else {
                return convertDpToPixel(layout.getTabletPortrait().getHeight(), context);
            }
        } else {
            return convertDpToPixel(layout.getMobile().getHeight(), context);
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            return dp;
        }
        return 0.0f;
    }

    public static int dpToPx(int dp, Context context) {
        return context.getResources().getDimensionPixelSize(dp);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean isTablet(Context context) {
        if (context != null) {
            int largeScreenLayout =
                    (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
            int xLargeScreenLayout =
                    (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);

            if (largeScreenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                    xLargeScreenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                float yInches = metrics.heightPixels / metrics.ydpi;
                float xInches = metrics.widthPixels / metrics.xdpi;
                double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

                return diagonalInches >= 6.5;
            }
        }
        return false;
    }

    public static boolean isLandscape(Context context) {
        return context != null
                && context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static float getLeftDrawableHeight(Context context,
                                              Layout layout,
                                              float defaultValue) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                if (0 < layout.getTabletLandscape().getLeftDrawableHeight()) {
                    return convertVerticalValue(context, layout.getTabletLandscape().getLeftDrawableHeight());
                }
            } else {
                if (0 < layout.getTabletPortrait().getLeftDrawableHeight()) {
                    return convertVerticalValue(context, layout.getTabletPortrait().getLeftDrawableHeight());
                }
            }
        } else {
            if (0 < layout.getMobile().getLeftDrawableHeight()) {
                return convertVerticalValue(context, layout.getMobile().getLeftDrawableHeight());
            }
        }
        return defaultValue;
    }

    public static float getLeftDrawableWidth(Context context,
                                             Layout layout,
                                             float defaultValue) {
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                if (0 < layout.getTabletLandscape().getLeftDrawableWidth()) {
                    return convertVerticalValue(context, layout.getTabletLandscape().getLeftDrawableWidth());
                }
            } else {
                if (0 < layout.getTabletPortrait().getLeftDrawableWidth()) {
                    return convertVerticalValue(context, layout.getTabletPortrait().getLeftDrawableWidth());
                }
            }
        } else {
            if (0 < layout.getMobile().getLeftDrawableWidth()) {
                return convertVerticalValue(context, layout.getMobile().getLeftDrawableWidth());
            }
        }
        return defaultValue;
    }

    public static float convertHorizontalValue(Context context, float value) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    return DEVICE_WIDTH * (value / STANDARD_TABLET_HEIGHT_PX);
                } else {
                    return DEVICE_WIDTH * (value / STANDARD_TABLET_WIDTH_PX);
                }
            }
            return DEVICE_WIDTH * (value / STANDARD_MOBILE_WIDTH_PX);
        }
        return 0.0f;
    }

    public static float convertVerticalValue(Context context, float value) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    return DEVICE_HEIGHT * (value / STANDARD_TABLET_WIDTH_PX);
                } else {
                    return DEVICE_HEIGHT * (value / STANDARD_TABLET_HEIGHT_PX);
                }
            }
            return DEVICE_HEIGHT * (value / STANDARD_MOBILE_HEIGHT_PX);
        }
        return 0.0f;
    }

    public static float getScaledHorizontalValue(Context context, float value) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    return DEVICE_WIDTH * (value / STANDARD_TABLET_HEIGHT_PX);
                } else {
                    return DEVICE_WIDTH * (value / STANDARD_TABLET_WIDTH_PX);
                }
            } else {
                return DEVICE_WIDTH * (value / STANDARD_MOBILE_WIDTH_PX);
            }
        }

        return -1.0f;
    }

    public static float getHorizontalMargin(Context context, Layout layout) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    if (tabletLandscape.getLeftMargin() != 0f) {
                        return DEVICE_WIDTH * (tabletLandscape.getLeftMargin() / STANDARD_TABLET_HEIGHT_PX);
                    } else if (tabletLandscape.getRightMargin() != 0f) {
                        return DEVICE_WIDTH * (tabletLandscape.getRightMargin() / STANDARD_TABLET_HEIGHT_PX);
                    } else {
                        return -1.0f;
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    if (tabletPortrait.getLeftMargin() != 0f) {
                        return DEVICE_WIDTH * (tabletPortrait.getLeftMargin() / STANDARD_TABLET_WIDTH_PX);
                    } else if (tabletPortrait.getRightMargin() != 0f) {
                        return DEVICE_WIDTH * (tabletPortrait.getRightMargin() / STANDARD_TABLET_WIDTH_PX);
                    } else {
                        return -1.0f;
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                if (mobile.getLeftMargin() != 0f) {
                    return DEVICE_WIDTH * (mobile.getLeftMargin() / STANDARD_MOBILE_WIDTH_PX);
                } else if (mobile.getRightMargin() != 0f) {
                    return DEVICE_WIDTH * (mobile.getRightMargin() / STANDARD_MOBILE_WIDTH_PX);
                } else {
                    return -1.0f;
                }
            }
        }
        return -1.0f;
    }

    public static float getVerticalMargin(Context context, Layout layout, int viewHeight, int parentViewHeight) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    if (tabletLandscape.getTopMargin() != 0f) {
                        return DEVICE_HEIGHT * (tabletLandscape.getTopMargin() / STANDARD_TABLET_WIDTH_PX);
                    } else if (tabletLandscape.getBottomMargin() != 0f) {
                        float bm = DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / STANDARD_TABLET_WIDTH_PX);
                        int tmDiff = viewHeight;
                        if (tmDiff < 0) {
                            tmDiff = 0;
                        }
                        return parentViewHeight - bm - tmDiff;
                    } else {
                        return -1.0f;
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    if (tabletPortrait.getTopMargin() != 0f) {
                        return DEVICE_HEIGHT * (tabletPortrait.getTopMargin() / STANDARD_TABLET_HEIGHT_PX);
                    } else if (tabletPortrait.getBottomMargin() != 0f) {
                        float bm = DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / STANDARD_TABLET_HEIGHT_PX);
                        int tmDiff = viewHeight;
                        if (tmDiff < 0) {
                            tmDiff = 0;
                        }
                        return parentViewHeight - bm - tmDiff;
                    } else {
                        return -1.0f;
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                if (mobile.getTopMargin() != 0f) {
                    return DEVICE_HEIGHT * (mobile.getTopMargin() / STANDARD_MOBILE_HEIGHT_PX);
                } else if (mobile.getBottomMargin() != 0f) {
                    float bm = DEVICE_HEIGHT * (mobile.getBottomMargin() / STANDARD_MOBILE_HEIGHT_PX);
                    int tmDiff = viewHeight;
                    if (tmDiff < 0) {
                        tmDiff = 0;
                    }
                    return parentViewHeight - bm - tmDiff;
                } else {
                    return -1.0f;
                }
            }
        }
        return -1.0f;
    }

    public static void setViewWidth(Context context, Layout layout, float width) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape() != null) {
                        layout.getTabletLandscape().setWidth(width);
                    }
                } else {
                    if (layout.getTabletPortrait() != null) {
                        layout.getTabletPortrait().setWidth(width);
                    }
                }
            } else {
                if (layout.getMobile() != null) {
                    layout.getMobile().setWidth(width);
                }
            }
        }
    }

    public static void setViewHeight(Context context, Layout layout, float height) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape() != null) {
                        layout.getTabletLandscape().setHeight(height);
                    }
                } else {
                    if (layout.getTabletPortrait() != null) {
                        layout.getTabletPortrait().setHeight(height);
                    }
                }
            } else {
                if (layout.getMobile() != null) {
                    layout.getMobile().setHeight(height);
                }
            }
        }
    }

    public static float getYAxis(Context context, Layout layout, float defaultYValue) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape() != null) {
                        return layout.getTabletLandscape().getYAxis();
                    }
                } else {
                    if (layout.getTabletPortrait() != null) {
                        return layout.getTabletPortrait().getYAxis();
                    }
                }
            } else {
                if (layout.getMobile() != null) {
                    return layout.getMobile().getYAxis();
                }
            }
        }
        return defaultYValue;
    }

    public static void setYAxis(Context context, Layout layout, float yValue) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape() != null) {
                        layout.getTabletLandscape().setYAxis(yValue);
                    }
                } else {
                    if (layout.getTabletPortrait() != null) {
                        layout.getTabletPortrait().setYAxis(yValue);
                    }
                }
            } else {
                if (layout.getMobile() != null) {
                    layout.getMobile().setYAxis(yValue);
                }
            }
        }
    }

    public static float getSavedViewWidth(Context context, Layout layout, float defaultWidth) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float width = 0.0f;
                    if (tabletLandscape != null) {
                        width = tabletLandscape.getSavedWidth();
                    }
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_HEIGHT_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float width = 0.0f;
                    if (tabletPortrait != null) {
                        width = tabletPortrait.getSavedWidth();
                    }
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float width = 0.0f;
                if (mobile != null) {
                    width = mobile.getSavedWidth();
                }
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
                }
            }
        }
        return defaultWidth;
    }

    public static float getViewWidth(Context context, float width , float defaultWidth) {
        if (width != -1.0f) {
            if (!isTablet(context)){
                return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
            } else if (isTablet(context) && isLandscape(context)) {
                return DEVICE_WIDTH * (width / STANDARD_TABLET_HEIGHT_PX);
            } else {
                return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
            }
        }
        return defaultWidth;
    }
    public static float getViewWidth(Context context, Layout layout, float defaultWidth) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float width = getViewWidth(tabletLandscape);
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_HEIGHT_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float width = getViewWidth(tabletPortrait);
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float width = getViewWidth(mobile);
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
                }
            }
        }
        return defaultWidth;
    }
    public static float getViewWidthConstraint(Context context, Layout layout, float defaultWidth) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float width = getViewWidth(tabletLandscape);
                    if (width != -1.0f) {
                        return width;
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float width = getViewWidth(tabletPortrait);
                    if (width != -1.0f) {
                        return width;
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float width = getViewWidth(mobile);
                if (width != -1.0f) {
                    return width;
                }
            }
        }
        return defaultWidth;
    }

    public static int getViewHeightByRatio(String ratio, int width) {
        switch (ratio) {
            case "32:9":
                return (int) ((float) width * 9.0f / 32.0f);
            case "16:9":
                return (int) ((float) width * 9.0f / 16.0f);
             case "16:11":
                return (int) ((float) width * 11.0f / 16.0f);
            case "9:16":
                return (int) ((float) width * 16.0f / 9.0f);
            case "9:17":
                return (int) ((float) width * 17.0f / 9.0f);
            case "3:4":
                return (int) ((float) width * 4.0f / 3.0f);
            case "3:5":
                return (int) ((float) width * 5.0f / 3.0f);
            case "1:1":
                return width;
            default:
                return 0;

        }
    }

    public static int getViewWidthByRatio(String ratio, int height) {
        switch (ratio) {
            case "32:9":
                return (int) ((float) height * 32.0f / 9.0f);
            case "16:9":
                return (int) ((float) height * 16.0f / 9.0f);
            case "9:16":
                return (int) ((float) height * 9.0f / 16.0f);
            case "3:4":
                return (int) ((float) height * 3.0f / 4.0f);
            case "1:1":
                return height;
            default:
                return 0;
        }
    }

    public static String getViewRatio(Context context, Layout layout, String defaultRatio) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getRatio() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getRatio())) {
                        return layout.getTabletLandscape().getRatio();
                    }
                } else {
                    if (layout.getTabletPortrait().getRatio() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getRatio())) {
                        return layout.getTabletPortrait().getRatio();
                    }
                }
            } else {

                if (layout.getMobile().getRatio() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getRatio())) {
                    return layout.getMobile().getRatio();
                }
            }
        }
        return defaultRatio;

    }

    public static int getConstraintMarginRight(Context context, Layout layout, int defaultMargin) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getConstraint_MarginRight() != 0) {
                        return layout.getTabletLandscape().getConstraint_MarginRight();
                    }
                } else {
                    if (layout.getTabletPortrait().getConstraint_MarginRight() != 0) {
                        return layout.getTabletPortrait().getConstraint_MarginRight();
                    }
                }
            } else {
                if (layout.getMobile().getConstraint_MarginRight() != 0) {
                    return layout.getMobile().getConstraint_MarginRight();
                }
            }
        }
        return defaultMargin;
    }

    public static int getConstraintMarginLeft(Context context, Layout layout, int defaultMargin) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getConstraint_MarginLeft() != 0) {
                        return layout.getTabletLandscape().getConstraint_MarginLeft();
                    }
                } else {
                    if (layout.getTabletPortrait().getConstraint_MarginLeft() != 0) {
                        return layout.getTabletPortrait().getConstraint_MarginLeft();
                    }
                }
            } else {

                if (layout.getMobile().getConstraint_MarginLeft() != 0) {
                    return layout.getMobile().getConstraint_MarginLeft();
                }
            }
        }
        return defaultMargin;
    }

    public static int getConstraintMarginTop(Context context, Layout layout, int defaultMargin) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getConstraint_MarginTop() != 0) {
                        return layout.getTabletLandscape().getConstraint_MarginTop();
                    }
                } else {
                    if (layout.getTabletPortrait().getConstraint_MarginTop() != 0) {
                        return layout.getTabletPortrait().getConstraint_MarginTop();
                    }
                }
            } else {

                if (layout.getMobile().getConstraint_MarginTop() != 0) {
                    return layout.getMobile().getConstraint_MarginTop();
                }
            }
        }
        return defaultMargin;
    }

    public static int getConstraintMarginBottom(Context context, Layout layout, int defaultMargin) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getConstraint_MarginBottom() != 0) {
                        return layout.getTabletLandscape().getConstraint_MarginBottom();
                    }
                } else {
                    if (layout.getTabletPortrait().getConstraint_MarginBottom() != 0) {
                        return layout.getTabletPortrait().getConstraint_MarginBottom();
                    }
                }
            } else {
                if (layout.getMobile().getConstraint_MarginBottom() != 0) {
                    return layout.getMobile().getConstraint_MarginBottom();
                }
            }
        }
        return defaultMargin;
    }

    public static String getTopToBottomOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTopTo_bottomOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getTopTo_bottomOf())) {
                        return layout.getTabletLandscape().getTopTo_bottomOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getTopTo_bottomOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getTopTo_bottomOf())) {
                        return layout.getTabletPortrait().getTopTo_bottomOf();
                    }
                }
            } else {

                if (layout.getMobile().getTopTo_bottomOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getTopTo_bottomOf())) {
                    return layout.getMobile().getTopTo_bottomOf();
                }
            }
        }
        return defaultId;
    }

    public static String getTopToTopOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTopTo_topOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getTopTo_topOf())) {
                        return layout.getTabletLandscape().getTopTo_topOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getTopTo_topOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getTopTo_topOf())) {
                        return layout.getTabletPortrait().getTopTo_topOf();
                    }
                }
            } else {

                if (layout.getMobile().getTopTo_topOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getTopTo_topOf())) {
                    return layout.getMobile().getTopTo_topOf();
                }
            }
        }
        return defaultId;
    }

    public static String getBottomToBottomOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getBottomTo_bottomOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getBottomTo_bottomOf())) {
                        return layout.getTabletLandscape().getBottomTo_bottomOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getBottomTo_bottomOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getBottomTo_bottomOf())) {
                        return layout.getTabletPortrait().getBottomTo_bottomOf();
                    }
                }
            } else {

                if (layout.getMobile().getBottomTo_bottomOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getBottomTo_bottomOf())) {
                    return layout.getMobile().getBottomTo_bottomOf();
                }
            }
        }
        return defaultId;
    }

    public static String getBottomToTopOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getBottomTo_topOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getBottomTo_topOf())) {
                        return layout.getTabletLandscape().getBottomTo_topOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getBottomTo_topOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getBottomTo_topOf())) {
                        return layout.getTabletPortrait().getBottomTo_topOf();
                    }
                }
            } else {

                if (layout.getMobile().getBottomTo_topOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getBottomTo_topOf())) {
                    return layout.getMobile().getBottomTo_topOf();
                }
            }
        }
        return defaultId;
    }

    public static String getRightToRightOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getRightTo_rightOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getRightTo_rightOf())) {
                        return layout.getTabletLandscape().getRightTo_rightOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getRightTo_rightOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getRightTo_rightOf())) {
                        return layout.getTabletPortrait().getRightTo_rightOf();
                    }
                }
            } else {

                if (layout.getMobile().getRightTo_rightOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getRightTo_rightOf())) {
                    return layout.getMobile().getRightTo_rightOf();
                }
            }
        }
        return defaultId;
    }

    public static String getRightToLeftOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getRightTo_leftOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getRightTo_leftOf())) {
                        return layout.getTabletLandscape().getRightTo_leftOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getRightTo_leftOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getRightTo_leftOf())) {
                        return layout.getTabletPortrait().getRightTo_leftOf();
                    }
                }
            } else {

                if (layout.getMobile().getRightTo_leftOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getRightTo_leftOf())) {
                    return layout.getMobile().getRightTo_leftOf();
                }
            }
        }
        return defaultId;
    }

    public static String getLeftToLeftOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getLeftTo_leftOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getLeftTo_leftOf())) {
                        return layout.getTabletLandscape().getLeftTo_leftOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getLeftTo_leftOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getLeftTo_leftOf())) {
                        return layout.getTabletPortrait().getLeftTo_leftOf();
                    }
                }
            } else {

                if (layout.getMobile().getLeftTo_leftOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getLeftTo_leftOf())) {
                    return layout.getMobile().getLeftTo_leftOf();
                }
            }
        }
        return defaultId;
    }

    public static String getLeftToRightOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getLeftTo_rightOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getLeftTo_rightOf())) {
                        return layout.getTabletLandscape().getLeftTo_rightOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getLeftTo_rightOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getLeftTo_rightOf())) {
                        return layout.getTabletPortrait().getLeftTo_rightOf();
                    }
                }
            } else {

                if (layout.getMobile().getLeftTo_rightOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getLeftTo_rightOf())) {
                    return layout.getMobile().getLeftTo_rightOf();
                }
            }
        }
        return defaultId;
    }


    public static String getBaselineToBaselineOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getBaseline_toBaselineOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getBaseline_toBaselineOf())) {
                        return layout.getTabletLandscape().getBaseline_toBaselineOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getBaseline_toBaselineOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getBaseline_toBaselineOf())) {
                        return layout.getTabletPortrait().getBaseline_toBaselineOf();
                    }
                }
            } else {

                if (layout.getMobile().getBaseline_toBaselineOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getBaseline_toBaselineOf())) {
                    return layout.getMobile().getBaseline_toBaselineOf();
                }
            }
        }
        return defaultId;
    }

    public static String getStartToEndOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getStart_toEndOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getStart_toEndOf())) {
                        return layout.getTabletLandscape().getStart_toEndOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getStart_toEndOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getStart_toEndOf())) {
                        return layout.getTabletPortrait().getStart_toEndOf();
                    }
                }
            } else {

                if (layout.getMobile().getStart_toEndOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getStart_toEndOf())) {
                    return layout.getMobile().getStart_toEndOf();
                }
            }
        }
        return defaultId;
    }

    public static String getStartToStartOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getStart_toStartOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getStart_toStartOf())) {
                        return layout.getTabletLandscape().getStart_toStartOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getStart_toStartOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getStart_toStartOf())) {
                        return layout.getTabletPortrait().getStart_toStartOf();
                    }
                }
            } else {

                if (layout.getMobile().getStart_toStartOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getStart_toStartOf())) {
                    return layout.getMobile().getStart_toStartOf();
                }
            }
        }
        return defaultId;
    }

    public static String getEndToStartOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getEnd_toStartOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getEnd_toStartOf())) {
                        return layout.getTabletLandscape().getEnd_toStartOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getEnd_toStartOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getEnd_toStartOf())) {
                        return layout.getTabletPortrait().getEnd_toStartOf();
                    }
                }
            } else {

                if (layout.getMobile().getEnd_toStartOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getEnd_toStartOf())) {
                    return layout.getMobile().getEnd_toStartOf();
                }
            }
        }
        return defaultId;
    }

    public static String getEndToEndOf(Context context, Layout layout, String defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getEnd_toEndOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletLandscape().getEnd_toEndOf())) {
                        return layout.getTabletLandscape().getEnd_toEndOf();
                    }
                } else {
                    if (layout.getTabletPortrait().getEnd_toEndOf() != null &&
                            !TextUtils.isEmpty(layout.getTabletPortrait().getEnd_toEndOf())) {
                        return layout.getTabletPortrait().getEnd_toEndOf();
                    }
                }
            } else {

                if (layout.getMobile().getEnd_toEndOf() != null &&
                        !TextUtils.isEmpty(layout.getMobile().getEnd_toEndOf())) {
                    return layout.getMobile().getEnd_toEndOf();
                }
            }
        }
        return defaultId;
    }


    public static int getGuidline(Context context, Layout layout, int defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    return layout.getTabletLandscape().getGuidLineOriantation();
                } else {
                    return layout.getTabletPortrait().getGuidLineOriantation();
                }
            } else {
                return layout.getMobile().getGuidLineOriantation();
            }
        }
        return defaultId;
    }

    public static float getGuideLinePositionPercent(Context context, Layout layout, float defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {

                    return layout.getTabletLandscape().getGuideLinePositionPercent();

                } else {

                    return layout.getTabletPortrait().getGuideLinePositionPercent();

                }
            } else {
                return layout.getMobile().getGuideLinePositionPercent();
            }
        }
        return defaultId;
    }

    public static int getVertical(Context context, Layout layout, int defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {

                    // return layout.getTabletLandscape().getGuidLineOriantation();

                } else {

                    return layout.getTabletPortrait().getGuidLineOriantation();

                }
            } else {
                // return layout.getMobile().getGuidLineOriantation();
            }
        }
        return defaultId;
    }

    public static int getTopMargin(Context context, Layout layout, int defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTopMargin() != 0) {
                        return (int) layout.getTabletLandscape().getTopMargin();
                    }
                } else {
                    if (layout.getTabletPortrait().getTopMargin() != 0) {
                        return (int) layout.getTabletPortrait().getTopMargin();
                    }
                }
            } else {
                if (layout.getMobile().getTopMargin() != 0) {
                    return (int) layout.getMobile().getTopMargin();
                }
            }
        }
        return defaultId;
    }

    public static int getleftMargin(Context context, Layout layout, int defaultId) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getLeftMargin() != 0) {
                        return (int) layout.getTabletLandscape().getLeftMargin();
                    }
                } else {
                    if (layout.getTabletPortrait().getLeftMargin() != 0) {
                        return (int) layout.getTabletPortrait().getLeftMargin();
                    }
                }
            } else {
                if (layout.getMobile().getLeftMargin() != 0) {
                    return (int) layout.getMobile().getLeftMargin();
                }
            }
        }
        return defaultId;
    }

    public static float getViewWidthDimen(Context context, float widthValue, float defaultWidth) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    float width = widthValue;
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_HEIGHT_PX);
                    }
                } else {
                    float width = widthValue;
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
                    }
                }
            } else {
                float width = widthValue;
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
                }
            }
        }
        return defaultWidth;
    }

    public static float getViewWidthSquareDimen(Context context, float widthValue, float defaultWidth) {
        if (context != null) {
            if (isTablet(context)) {
                float width = widthValue;
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
                }
            } else {
                float width = widthValue;
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
                }
            }
        }
        return defaultWidth;
    }

    public static float getViewHeightDimen(Context context, float heightValue, float defaultHeight) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    float height = heightValue;
                    if (height >= 0.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_WIDTH_PX);
                    }
                } else {
                    float height = heightValue;
                    if (height >= 0.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_HEIGHT_PX);
                    }
                }
            } else {
                float height = heightValue;
                if (height >= 0.0f) {
                    return DEVICE_HEIGHT * (height / STANDARD_MOBILE_HEIGHT_PX);
                }
            }
        }
        return defaultHeight;
    }

    public static float getThumbnailWidth(Context context, Layout layout, float defaultWidth) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float width = getThumbnailWidth(tabletLandscape);
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_HEIGHT_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float width = getThumbnailWidth(tabletPortrait);
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float width = getThumbnailWidth(mobile);
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
                }
            }
        }
        return defaultWidth;
    }

    public static float getViewMaximumWidth(Context context, Layout layout, float defaultMaximumWidth) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    float maximumWidth = getViewMaximumWidth(layout.getTabletLandscape());
                    if (maximumWidth >= 0.0f) {
                        return DEVICE_WIDTH * (maximumWidth / STANDARD_TABLET_HEIGHT_PX);
                    }
                } else {
                    float maximumWidth = getViewMaximumWidth(layout.getTabletPortrait());
                    if (maximumWidth >= 0.0f) {
                        return DEVICE_WIDTH * (maximumWidth / STANDARD_TABLET_WIDTH_PX);
                    }
                }
            } else {
                float maximumWidth = getViewMaximumWidth(layout.getMobile());
                if (maximumWidth >= 0.0f) {
                    return DEVICE_WIDTH * (maximumWidth / STANDARD_TABLET_WIDTH_PX);
                }
            }
        }
        return defaultMaximumWidth;
    }

    public static float getThumbnailHeight(Context context, Layout layout, float defaultHeight) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float height = getThumbnailHeight(tabletLandscape);
                    if (height >= 0.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_WIDTH_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float height = getThumbnailHeight(tabletPortrait);
                    if (height >= 0.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_HEIGHT_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float height = getThumbnailHeight(mobile);
                if (height >= 0.0f) {
                    return DEVICE_HEIGHT * (height / STANDARD_MOBILE_HEIGHT_PX);
                }
            }
        }
        return defaultHeight;
    }

    public static float getViewHeight(Context context, Layout layout, float defaultHeight) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float height = getViewHeight(tabletLandscape);
                    if (height >= 0.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_WIDTH_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float height = getViewHeight(tabletPortrait);
                    if (height >= 0.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_HEIGHT_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float height = getViewHeight(mobile);
                if (height >= 0.0f) {
                    return DEVICE_HEIGHT * (height / STANDARD_MOBILE_HEIGHT_PX);
                }
            }
        }
        return defaultHeight;
    }

    public static float getFontSize(Context context, Layout layout) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getFontSize() != 0f) {
                        return layout.getTabletLandscape().getFontSize();
                    }
                } else {
                    if (layout.getTabletPortrait().getFontSize() != 0f) {
                        return layout.getTabletPortrait().getFontSize();
                    }
                }
            } else {
                if (layout.getMobile().getFontSize() != 0f) {
                    return layout.getMobile().getFontSize();
                }
            }
        }

        return -1.0f;
    }

    public static float getFontSizeKey(Context context, Layout layout) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getFontSizeKey() != 0f) {
                        return layout.getTabletLandscape().getFontSizeKey();
                    }
                } else {
                    if (layout.getTabletPortrait().getFontSizeKey() != 0f) {
                        return layout.getTabletPortrait().getFontSizeKey();
                    }
                }
            } else {
                if (layout.getMobile().getFontSizeKey() != 0f) {
                    return layout.getMobile().getFontSizeKey();
                }
            }
        }
        return -1.0f;
    }

    public static float getFontSizeValue(Context context, Layout layout) {
        if (context != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getFontSizeValue() != 0f) {
                        return layout.getTabletLandscape().getFontSizeValue();
                    }
                } else {
                    if (layout.getTabletPortrait().getFontSizeValue() != 0f) {
                        return layout.getTabletPortrait().getFontSizeValue();
                    }
                }
            } else {
                if (layout.getMobile().getFontSizeValue() != 0f) {
                    layout.getMobile().getFontSizeValue();
                }
            }
        }
        return -1.0f;
    }

    public static int getDeviceWidth() {
        return DEVICE_WIDTH;
    }

    public static int getPlayerWidth() {
        return PLAYER_WIDTH;
    }

    public static void setDeviceWidth(int deviceWidth) {
        DEVICE_WIDTH = deviceWidth;
    }

    public static int getDeviceHeight() {
        return DEVICE_HEIGHT;
    }

    public static void setDeviceHeight(int deviceHeight) {
        DEVICE_HEIGHT = deviceHeight;
    }

    protected static float getViewWidth(TabletLandscape tabletLandscape) {
        if (tabletLandscape != null) {
            if (tabletLandscape.getWidth() != 0f) {
                return tabletLandscape.getWidth();
            }
        }
        return -1.0f;
    }

    protected static float getThumbnailWidth(TabletLandscape tabletLandscape) {
        if (tabletLandscape != null) {
            if (tabletLandscape.getThumbnailWidth() != 0f) {
                return tabletLandscape.getThumbnailWidth();
            }
        }
        return -1.0f;
    }

    private static float getViewMaximumWidth(TabletLandscape tabletLandscape) {
        if (tabletLandscape != null) {
            if (tabletLandscape.getMaximumWidth() != 0f) {
                return tabletLandscape.getMaximumWidth();
            }
        }
        return -1.0f;
    }

    protected static float getViewHeight(TabletLandscape tabletLandscape) {
        if (tabletLandscape != null) {
            if (tabletLandscape.getHeight() != 0f) {
                return tabletLandscape.getHeight();
            }
        }
        return -1.0f;
    }

    protected static float getThumbnailHeight(TabletLandscape tabletLandscape) {
        if (tabletLandscape != null) {
            if (tabletLandscape.getThumbnailHeight() != 0f) {
                return tabletLandscape.getThumbnailHeight();
            }
        }
        return -1.0f;
    }

    protected static float getViewWidth(TabletPortrait tabletPortrait) {
        if (tabletPortrait != null) {
            if (tabletPortrait.getWidth() != 0f) {
                return tabletPortrait.getWidth();
            }
        }
        return -1.0f;
    }

    protected static float getThumbnailWidth(TabletPortrait tabletPortrait) {
        if (tabletPortrait != null) {
            if (tabletPortrait.getThumbnailWidth() != 0f) {
                return tabletPortrait.getThumbnailWidth();
            }
        }
        return -1.0f;
    }

    private static float getViewMaximumWidth(TabletPortrait tabletPortrait) {
        if (tabletPortrait != null) {
            if (tabletPortrait.getMaximumWidth() != 0f) {
                return tabletPortrait.getMaximumWidth();
            }
        }
        return -1.0f;
    }

    protected static float getViewHeight(TabletPortrait tabletPortrait) {
        if (tabletPortrait != null) {
            if (tabletPortrait.getHeight() != 0f) {
                return tabletPortrait.getHeight();
            }
        }
        return -1.0f;
    }

    protected static float getThumbnailHeight(TabletPortrait tabletPortrait) {
        if (tabletPortrait != null) {
            if (tabletPortrait.getThumbnailHeight() != 0f) {
                return tabletPortrait.getThumbnailHeight();
            }
        }
        return -1.0f;
    }

    protected static float getViewWidth(Mobile mobile) {
        if (mobile != null) {
            if (mobile.getWidth() != 0f) {
                return mobile.getWidth();
            }
        }
        return -1.0f;
    }

    protected static float getThumbnailWidth(Mobile mobile) {
        if (mobile != null) {
            if (mobile.getThumbnailWidth() != 0f) {
                return mobile.getThumbnailWidth();
            }
        }
        return -1.0f;
    }

    private static float getViewMaximumWidth(Mobile mobile) {
        if (mobile != null) {
            if (mobile.getMaximumWidth() != 0f) {
                return mobile.getMaximumWidth();
            }
        }
        return -1.0f;
    }

    protected static float getViewHeight(Mobile mobile) {
        if (mobile != null) {
            if (mobile.getHeight() != 0f) {
                return mobile.getHeight();
            }
        }
        return -1.0f;
    }

    protected static float getThumbnailHeight(Mobile mobile) {
        if (mobile != null) {
            if (mobile.getThumbnailHeight() != 0f) {
                return mobile.getThumbnailHeight();
            }
        }
        return -1.0f;
    }

    public abstract void init();

    protected abstract Component getChildComponent(int index);

    protected abstract Layout getLayout();

    public ViewGroup getChildrenContainer() {
        if (childrenContainer == null) {
            return createChildrenContainer();
        }
        return childrenContainer;
    }
    public ViewGroup getTrayChildrenContainer() {
        if (childrenContainer == null) {
            return createTrayChildrenContainer();
        }
        return childrenContainer;
    }
    public void setComponentHasView(int index, boolean hasView) {
        if (componentHasViewList != null) {
            componentHasViewList[index] = hasView;
        }
    }

    public void setViewMarginsFromComponent(Component childComponent,
                                            View view,
                                            Layout parentLayout,
                                            View parentView,
                                            boolean firstMeasurement,
                                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                            boolean useMarginsAsPercentages,
                                            boolean useWidthOfScreen,
                                            String viewType, String blockName) {
        Layout layout = childComponent.getLayout();

        if (!shouldShowView(childComponent)) {
            view.setVisibility(GONE);
            return;
        }

        view.setPadding(0, 0, 0, 0);

        int lm = 0, tm = 0, rm = 0, bm = 0;
        int deviceHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        int deviceWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        int viewWidth = (int) getViewWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);

        AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType componentKey = jsonValueKeyMap.get(childComponent.getKey());
        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType viewGravity = jsonValueKeyMap.get(childComponent.getViewGravity());
        if (viewGravity == null) {
            viewGravity = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        AppCMSUIKeyType uiBlockName = jsonValueKeyMap.get(blockName);
        if (componentKey == null) {
            uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        AppCMSUIKeyType componentViewType = jsonValueKeyMap.get(viewType);
        if (componentViewType == null) {
            componentViewType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY ||
                componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY) {
            viewWidth = (int) convertWidthDpToPixel(layout, getContext());
            if (viewWidth == 0) {
                viewWidth = LayoutParams.MATCH_PARENT;
            }
            viewHeight = (int) convertHeightDpToPixel(layout, getContext());
            if (viewHeight == 0) {
                viewHeight = LayoutParams.WRAP_CONTENT;
            }
        }

        int parentViewWidth = (int) getViewWidth(getContext(),
                parentLayout,
                parentView.getMeasuredWidth());
        int parentViewHeight = (int) getViewHeight(getContext(),
                parentLayout,
                parentView.getMeasuredHeight());
        int maxViewWidth = (int) getViewMaximumWidth(getContext(), layout, -1);
        int measuredHeight = parentViewHeight != 0 ? parentViewHeight : deviceHeight;
        int measuredWidth = parentViewWidth != 0 ? parentViewWidth : deviceWidth;
        int gravity = Gravity.NO_GRAVITY;

        if (isTablet(getContext())) {
            if (isLandscape(getContext())) {
                TabletLandscape tabletLandscape = layout.getTabletLandscape();
                if (tabletLandscape != null) {
                    if (tabletLandscape.getXAxis() != 0f) {
                        float scaledX = DEVICE_WIDTH * (tabletLandscape.getXAxis() / STANDARD_TABLET_HEIGHT_PX);
                        lm = Math.round(scaledX);
                    }

                    if (tabletLandscape.getYAxis() != 0f) {
                        float scaledY = DEVICE_HEIGHT * (tabletLandscape.getYAxis() / STANDARD_TABLET_WIDTH_PX);
                        tm = Math.round(scaledY);
                    }

                    if (getViewWidth(tabletLandscape) == -1f && tabletLandscape.getXAxis() != 0f) {
                        float scaledX = DEVICE_WIDTH * (tabletLandscape.getXAxis() / STANDARD_TABLET_HEIGHT_PX);
                        lm = Math.round(scaledX);
                    } else if (tabletLandscape.getLeftMargin() != 0f) {
                        lm = Math.round(DEVICE_WIDTH * (tabletLandscape.getLeftMargin() / STANDARD_TABLET_HEIGHT_PX));
                    }
                    if (tabletLandscape.getRightMargin() != 0f) {
                        rm = Math.round(DEVICE_WIDTH * (tabletLandscape.getRightMargin() / STANDARD_TABLET_WIDTH_PX));
                    }
                    if (useMarginsAsPercentages) {
                        if (tabletLandscape.getTopMargin() != 0f) {
                            tm = Math.round((tabletLandscape.getTopMargin() / 100.0f) * measuredHeight);
                        }
                        if (tabletLandscape.getBottomMargin() != 0f) {
                            bm = Math.round(DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / STANDARD_TABLET_HEIGHT_PX));
                        }
                    } else {
                        if (tabletLandscape.getTopMargin() != 0f) {
                            tm = (int) (DEVICE_HEIGHT * (tabletLandscape.getTopMargin() / STANDARD_TABLET_WIDTH_PX));
                        } else if (tabletLandscape.getBottomMargin() != 0f && tabletLandscape.getBottomMargin() > 0) {
                            int tmDiff = viewHeight;
                            if (tmDiff < 0) {
                                tmDiff = 0;
                            }

                            if (parentViewHeight > 0) {
                                tm = parentViewHeight -
                                        (int) (DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / STANDARD_TABLET_WIDTH_PX)) -
                                        tmDiff;
                            } else {
                                gravity = Gravity.BOTTOM;
                                bm = Math.round(DEVICE_HEIGHT * (tabletLandscape.getBottomMargin() / STANDARD_TABLET_WIDTH_PX));
                            }
                        }
                    }
                }
            } else {
                TabletPortrait tabletPortrait = layout.getTabletPortrait();
                if (tabletPortrait != null) {
                    if (tabletPortrait.getXAxis() != 0f) {
                        float scaledX = DEVICE_WIDTH * (tabletPortrait.getXAxis() / STANDARD_TABLET_WIDTH_PX);
                        lm = Math.round(scaledX);
                    }

                    if (tabletPortrait.getYAxis() != 0f) {
                        float scaledY = DEVICE_HEIGHT * (tabletPortrait.getYAxis() / STANDARD_TABLET_HEIGHT_PX);
                        tm = Math.round(scaledY);
                    }

                    if (getViewWidth(tabletPortrait) == -1 && tabletPortrait.getXAxis() != 0f) {
                        float scaledX = DEVICE_WIDTH * (tabletPortrait.getXAxis() / STANDARD_TABLET_WIDTH_PX);
                        lm = Math.round(scaledX);
                    } else if (tabletPortrait.getLeftMargin() != 0f) {
                        lm = Math.round(DEVICE_WIDTH * (tabletPortrait.getLeftMargin() / STANDARD_TABLET_WIDTH_PX));
                    }
                    if (tabletPortrait.getRightMargin() != 0f) {
                        rm = Math.round(DEVICE_WIDTH * (tabletPortrait.getRightMargin() / STANDARD_TABLET_WIDTH_PX));
                    }
                    if (useMarginsAsPercentages) {
                        if (tabletPortrait.getTopMargin() != 0f) {
                            tm = Math.round((tabletPortrait.getTopMargin() / 100.0f) * measuredHeight);
                        }
                        if (tabletPortrait.getBottomMargin() != 0f) {
                            bm = Math.round(DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / STANDARD_TABLET_HEIGHT_PX));
                        }
                    } else {
                        if (tabletPortrait.getTopMargin() != 0f) {
                            tm = (int) (DEVICE_HEIGHT * (tabletPortrait.getTopMargin() / STANDARD_TABLET_HEIGHT_PX));
                        } else if (tabletPortrait.getBottomMargin() != 0f && tabletPortrait.getBottomMargin() > 0) {
                            int tmDiff = viewHeight;
                            if (tmDiff < 0) {
                                tmDiff = 0;
                            }
                            if (parentViewHeight > 0) {
                                tm = parentViewHeight -
                                        (int) (DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / STANDARD_TABLET_HEIGHT_PX)) -
                                        tmDiff;
                            } else {
                                gravity = Gravity.BOTTOM;
                                bm = Math.round(DEVICE_HEIGHT * (tabletPortrait.getBottomMargin() / STANDARD_TABLET_HEIGHT_PX));
                            }
                        }
                    }
                }
            }
        } else {
            Mobile mobile = layout.getMobile();
            if (mobile != null) {
                if (mobile.getXAxis() != 0f) {
                    float scaledX = DEVICE_WIDTH * (mobile.getXAxis() / STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledX);
                }
                if (componentKey == AppCMSUIKeyType.PAGE_LAST_NAME || componentKey == AppCMSUIKeyType.PAGE_LAST_NAME_INPUT) {
                    float scaledX = DEVICE_WIDTH / 2;
                    lm = Math.round(scaledX);
                }
                if (mobile.getYAxis() != 0f) {
                    float scaledY = DEVICE_HEIGHT * (mobile.getYAxis() / STANDARD_MOBILE_HEIGHT_PX);
                    tm = Math.round(scaledY);
                }

                if (getViewWidth(mobile) == -1 && mobile.getXAxis() != 0f) {
                    float scaledX = DEVICE_WIDTH * (mobile.getXAxis() / STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledX);
                } else if (mobile.getLeftMargin() != 0f) {
                    float scaledLm = DEVICE_WIDTH * (mobile.getLeftMargin() / STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledLm);
                    if (mobile.getRightMargin() != 0f) {
                        float scaledRm = DEVICE_WIDTH * (mobile.getRightMargin() / STANDARD_MOBILE_WIDTH_PX);
                        rm = Math.round(scaledRm);
                    }
                }
                if (mobile.getRightMargin() != 0f) {
                   /* int lmDiff = viewWidth;
                    if (lmDiff < 0) {
                        lmDiff = 0;
                    }

                    if (parentViewWidth > 0) {
                        float scaledRm = DEVICE_WIDTH * (mobile.getRightMargin() / STANDARD_MOBILE_WIDTH_PX);
                        lm = parentViewWidth - Math.round(scaledRm) - lmDiff;
                    } else {
                        gravity = Gravity.END;*/
                        rm = Math.round(DEVICE_WIDTH * (mobile.getRightMargin() / STANDARD_MOBILE_WIDTH_PX));
                   // }
                }
                if (useMarginsAsPercentages) {
                    if (mobile.getTopMargin() != 0f) {
                        tm = Math.round((mobile.getTopMargin() / 100.0f) * measuredHeight);
                    }
                    if (mobile.getBottomMargin() != 0f) {
                        int marginDiff = viewHeight;
                        if (marginDiff < 0) {
                            marginDiff = 0;
                        }
                        bm = Math.round(DEVICE_HEIGHT * (mobile.getBottomMargin() / STANDARD_MOBILE_HEIGHT_PX));
//                        bm = Math.round(((100.0f - mobile.getBottomMargin()) / 100.0f) * measuredHeight) -
//                                Math.round(convertDpToPixel(marginDiff, getContext()));
                    }
                } else {
                    if (mobile.getTopMargin() != 0f) {
                        tm = Math.round(DEVICE_HEIGHT * (mobile.getTopMargin() / STANDARD_MOBILE_HEIGHT_PX));
                    }
                    if (mobile.getBottomMargin() != 0f && mobile.getBottomMargin() > 0) {
                        int tmDiff = viewHeight;
                        if (tmDiff < 0) {
                            tmDiff = 0;
                        }
                        if (parentViewHeight > 0) {
                            tm = parentViewHeight -
                                    (int) (DEVICE_HEIGHT * (mobile.getBottomMargin() / STANDARD_MOBILE_HEIGHT_PX)) -
                                    tmDiff;
                        } else {
                            gravity = Gravity.BOTTOM;
                            bm = Math.round(DEVICE_HEIGHT * (mobile.getBottomMargin() / STANDARD_MOBILE_HEIGHT_PX));
                        }
                    }
                }
            }
        }

        int lp = 0, rp = 0, tp = 0, bp = 0;
        if (childComponent.getLeftpadding() != 0) {
            lp = (int) convertDpToPixel(childComponent.getLeftpadding(), getContext());
        }
        if (childComponent.getTopPadding() != 0) {
            tp = (int) convertDpToPixel(childComponent.getTopPadding(), getContext());
        }
        if (childComponent.getRightPadding() != 0) {
            rp = (int) convertDpToPixel(childComponent.getRightPadding(), getContext());
        }
        if (childComponent.getBottomPadding() != 0) {
            bp = (int) convertDpToPixel(childComponent.getBottomPadding(), getContext());
        }

        view.setPadding(lp, tp, rp, bp);

        if (viewGravity == VIEW_GRAVITY_CENTER_HORIZONTAL) {
            gravity = Gravity.CENTER_HORIZONTAL;
        }

        if (viewGravity == VIEW_GRAVITY_BOTTOM_CENTER) {
            gravity = Gravity.BOTTOM | Gravity.CENTER;
        }
        if (viewGravity == PAGE_NULL_KEY && componentKey==AppCMSUIKeyType.PAGE_PLAYLIST_AUDIO_ARTIST_TITLE) {
            gravity = Gravity.LEFT;
        }
        if (viewGravity == PAGE_NULL_KEY && componentKey==AppCMSUIKeyType.PAGE_API_TITLE) {
            gravity = Gravity.LEFT;
        }
        if (viewGravity == VIEW_GRAVITY_START) {
            gravity = Gravity.START;
        }

        if (viewGravity == VIEW_GRAVITY_CENTER_VERTICAL) {
            gravity = Gravity.CENTER_VERTICAL;
        }
        if (viewGravity == VIEW_GRAVITY_END) {
            gravity = Gravity.END;
        }

        if (componentType == null) {
            componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY ||
                componentType == AppCMSUIKeyType.PAGE_BUTTON_KEY) {
            if (viewWidth < 0) {
                viewWidth = LayoutParams.MATCH_PARENT;
            }
            if (jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_GRID_THUMBNAIL_INFO
                    || jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO
                    || componentKey == BUTTON_MANAGE_DOWNLOAD
                    || componentKey == MANAGE_LANGUAGE_KEY) {
                viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
            }

            if (jsonValueKeyMap.get(childComponent.getTextAlignment()) == AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_KEY) {
                ((TextView) view).setGravity(Gravity.CENTER);
            }

            if (jsonValueKeyMap.get(childComponent.getTextAlignment()) == AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY) {
                ((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
            }
            if (jsonValueKeyMap.get(childComponent.getTextAlignment()) == AppCMSUIKeyType.PAGE_TEXTALIGNMENT_RIGHT_KEY) {
                ((TextView) view).setGravity(Gravity.RIGHT);
                ((TextView) view).setGravity(Gravity.END);
            }
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY &&
                    view instanceof Spinner) {
                viewHeight = LayoutParams.WRAP_CONTENT;
                viewWidth = LayoutParams.WRAP_CONTENT;
            }


            switch (componentKey) {
                case PAGE_AGE_ERROR_LABEL_KEY:
                    if (isLandscape(getContext())) {
                        gravity = Gravity.START;
                    }
                    break;
                case PAGE_PLAY_IMAGE1_KEY:
                    if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_EVENT_CAROUSEL_02) {
                        gravity = Gravity.CENTER;
                    }
                    break;
                case PAGE_BRANDS_TRAY_TITLE_BRAND_KEY:
                    viewWidth = LayoutParams.MATCH_PARENT;
                    break;
                case PAGE_TRAY_TITLE_KEY:
                    if (isTablet(getContext())) {
                        if (isLandscape(getContext())) {
                            tm -= viewHeight / 6;
                            viewHeight *= 1.5;
                        }
                    }
                    break;
                case PAGE_WATCH_LIVE_BUTTON_KEY:
                    if (!isTablet(getContext())) {
                        if (childComponent.getTextAlignment().equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_text_alignment_center_horizontal_key))) {
                            ((Button) view).setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                    break;
                case PAGE_VIDEO_PLAY_BUTTON_KEY:
                    if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY) {
                        lm -= 8;
                        rm -= 8;
                        bm -= 8;
                        tm -= 8;
                    }
                    break;

                case PAGE_PLAY_IMAGE_KEY:
                    if (AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_HISTORY_02_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_WATCHLIST_01_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_WATCHLIST_02_MODULE_KEY != jsonValueKeyMap.get(viewType) &&
                            AppCMSUIKeyType.PAGE_HISTORY_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY != jsonValueKeyMap.get(viewType)

                            && AppCMSUIKeyType.PAGE_WATCHLIST_MODULE_KEY != jsonValueKeyMap.get(viewType)
                            && componentViewType != AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY) {
                        gravity = Gravity.CENTER;
                        lm -= 40;
                        rm -= 40;
                        bm -= 40;
                        tm -= 40;
                    }
                    break;

                case PAGE_PLAN_PURCHASE_BUTTON_KEY:
                case PAGE_LAUNCH_LOGIN_BUTTON_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case PAGE_VIDEO_CLOSE_KEY:
                    lm -= 8;
                    bm -= 8;
                    gravity = Gravity.END;
                    break;
                case PAGE_WATCHLIST_DELETE_ITEM_BUTTON:
                case PAGE_DELETE_DOWNLOAD_KEY:
                case PAGE_DELETE_WATCHLIST_KEY:
                case PAGE_DELETE_HISTORY_KEY:
                    gravity = Gravity.END;
                    break;
                case LABEL_SHOW_CLASSES:
                    ViewCompat.setTranslationZ(view, 2);
                    break;
                case PAGE_DOWNLOAD_SETTING_TITLE_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    bm = viewHeight / 2;
                    break;
                case PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY:
                case PAGE_LIVE_SCHEDULE_TAG_CLASS_TITLE_KEY:
                case PAGE_LIVE_SCHEDULE_ITEM_VERTICAL_HEIGHT_KEY:
                case PAGE_INSTRUCTOR_CLASS_DURATION_RECENT_KEY:
                case PAGE_BRANDS_CAROUSEL_NAME_KEY:
                case PAGE_BRANDS_CAROUSEL_DESCRIPTION_KEY:
                case PAGE_HOME_DURATION_CAROUSEL:
                case PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY:
                case PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY:
                case PAGE_SCHEDULE_CAROUSEL_NEXT_CLASS_KEY:
                case PAGE_HOME_CAROUSEL_EVENT_TIMER_KEY:
                case PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY:
                case PAGE_CAROUSEL_VIDEO_DURATION_SEPARATION_KEY:
                case PAGE_CAROUSEL_TITLE_KEY: {
                    int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                    int heightByRatio = 0;
                    if (isTablet(getContext())) {
                        if (isLandscape(getContext())) {
                            rm = Math.round(DEVICE_HEIGHT * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                                    STANDARD_MOBILE_WIDTH_PX));
                        } else {
                            rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getTabletPortrait().getRightMargin() /
                                    STANDARD_MOBILE_WIDTH_PX));
                        }
                    } else {
                        rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getMobile().getRightMargin() /
                                STANDARD_MOBILE_WIDTH_PX));
                    }

                    /*if (componentViewType != AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE &&
                            componentViewType != AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_PERSON_DETAIL_03_MODULE_KEY)
                        gravity = Gravity.CENTER_HORIZONTAL;
                    if (viewType != null &&
                            viewType.equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_event_carousel_module_key))
                            ) {
                        if (isLandscape(getContext())) {
                            tm -= viewHeight * 5;
                        } else if (isTablet(getContext()) && !isLandscape(getContext())) {
                            tm -= viewHeight * 2;
                        } else {
                            tm -= viewHeight * 3;
                        }
                        viewHeight *= 2;
                    } else if ((isLandscape(getContext()) || !isTablet(getContext()))) {
                        if (isLandscape(getContext())) {
                            tm -= viewHeight * 5;
                        } else {
                            tm -= viewHeight * 2;
                        }
                        viewHeight *= 2;
                    } else {
                        tm -= viewHeight * 3;
                        viewHeight *= 2;
                    }
                    if (componentViewType != AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE &&
                            componentViewType != AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY &&
                            componentViewType != AppCMSUIKeyType.PAGE_PERSON_DETAIL_03_MODULE_KEY)
                        lm = maxViewWidth / 2 - viewWidth / 2;

                    if (componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL ||
                            componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_CLASS_DURATION_RECENT_KEY) {
                        viewWidth = (int) getViewWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);
                    }*/


                    if (componentViewType == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_TRAY_NETFLIX_MODULE_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_KEY
                            || componentViewType == AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY) {
                        heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        bm = viewHeight / 2;
                    }
                    if (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY) {
                        if (isTablet(getContext())) {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 32.0f);
                        } else {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        }
                    }
                    if (blockName.equalsIgnoreCase("carousel01"))
                        tm -= viewHeight * 2;
                    else
                        tm = (heightByRatio - (bm + viewHeight));
                    viewWidth = (int) getViewWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);

                    if (componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_NEXT_CLASS_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY
                            || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY) {
                        lm = (thumbnailWidth - (viewWidth + rm));
                    }

                    if (componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_NEXT_CLASS_TIME_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_HOME_DURATION_CAROUSEL ||
                            componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_TAG_CLASS_TITLE_KEY ||
                            componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_TAG_BRAND_TITLE_KEY) {
                        if (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY) {
                        } else {
                            viewWidth = LayoutParams.WRAP_CONTENT;
                        }
                    }

                    break;
                }
                case PAGE_BROWSE_TAG_CLASS_TITLE_KEY:
                case PAGE_BROWSE_TAG_BRAND_TITLE_KEY:
                case PAGE_BROWSE_DURATION_KEY:
                case PAGE_CLOSE:
                case LABEL_PAGE_NAME:
                case LABEL_CLEAR_FILTER:
                case PAGE_LIVE_SCHEDULE_CLASS_DURATION_KEY:
                case VIEW_FILTER_BUTTON:
                    viewWidth = LayoutParams.WRAP_CONTENT;
                    break;

                case PAGE_HOME_CLASS_DURATION_NETFLIX_KEY:
                    viewWidth = (int) getViewWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);
                    break;

                case PAGE_CAROUSEL_INFO_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    if (viewType != null &&
                            viewType.equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_event_carousel_module_key))
                    ) {
                        tm -= viewHeight * 2;
                    } else if (isTablet(getContext())) {
                        if (isLandscape(getContext())) {
                            tm -= viewHeight * 9;
                        } else {
                            tm -= viewHeight * 5;
                        }
                    } else {
                        tm -= viewHeight * 2;
                    }
                    viewHeight *= 2;
                    lm = maxViewWidth / 2 - viewWidth / 2;
                    break;

                case PAGE_WATCH_VIDEO_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    lm = maxViewWidth / 2;
                    if (isTablet(getContext()) && !isLandscape(getContext())) {
                        tm -= viewHeight / 2;
                    } else if (isLandscape(getContext())) {
                        tm -= viewHeight * 2;
                    }
                    break;

                case PAGE_VIDEO_CAST_KEY:
                case PAGE_VIDEO_SHARE_KEY:
                    if (isTablet(getContext())) {
                        lm -= viewWidth / 2;
                        tm -= (int) (viewWidth * 0.25);
                        viewHeight = (int) (viewWidth * 1.25);
                    } else {
                        lm -= viewWidth / 3;
                    }
                    break;

                case PAGE_API_TITLE:
                    viewHeight *= 1.1;
                    break;

                case PAGE_ADD_TO_WATCHLIST_KEY:
                    if (isTablet(getContext())) {
                        lm -= viewWidth * 0.5;
                    }
                    gravity = Gravity.TOP;
                    break;

                case PAGE_VIDEO_DOWNLOAD_BUTTON_KEY:
                    if (isTablet(getContext())) {
                        lm -= viewWidth * 0.7;
                    }
                    viewWidth = viewHeight;
                    break;

                case PAGE_PLAN_TITLE_KEY:
                    if (childComponent.getTextAlignment().equalsIgnoreCase("right")) {
                        gravity = Gravity.END;
                        rm += convertDpToPixel(8, getContext());
                        view.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
                    } else {
                        gravity = Gravity.START;
                    }
                    break;

                case PAGE_VENUE_LABEL_KEY:
                case PAGE_GAME_DATE_KEY:
                    if (childComponent.getTextAlignment().equalsIgnoreCase(getContext().getResources().getString(R.string.app_cms_page_text_alignment_right_key))) {
                        gravity = Gravity.END;
                        rm += convertDpToPixel(10, getContext());
                        view.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
                    } else {
                        gravity = Gravity.START;
                    }
                    break;

                case PAGE_PLAN_PRICEINFO_KEY:
                    lm += convertDpToPixel(8, getContext());
                    if (componentViewType == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        gravity = Gravity.CENTER_VERTICAL;
                    }
                    break;
                case PAGE_PLAN_FEATURE_TEXT_KEY:
                    if (componentViewType == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                        if (isTablet(getContext())) {
                            if (isLandscape(getContext())) {
                                rm = Math.round(DEVICE_WIDTH * (layout.getTabletLandscape().getRightMargin() / STANDARD_TABLET_WIDTH_PX));
                            }
                        }
                    }
                    break;

                case PAGE_SINGLE_PLAN_SUBSCRIBE_TEXT_KEY:
                    if (componentViewType == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                        gravity = Gravity.CENTER;
                    }
                    break;
                case PAGE_PLAN_FEATURE_TITLE_KEY:
                    gravity = Gravity.CENTER_HORIZONTAL;
                    break;

                case PAGE_SETTINGS_PLAN_VALUE_KEY:
                    if (isTablet(getContext())) {
                        if (isLandscape(getContext())) {
                            rm = Math.round(DEVICE_HEIGHT * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                                    STANDARD_MOBILE_WIDTH_PX));
                        } else {
                            rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getTabletPortrait().getRightMargin() /
                                    STANDARD_MOBILE_WIDTH_PX));
                        }
                    } else {
                        rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getMobile().getRightMargin() /
                                STANDARD_MOBILE_WIDTH_PX));
                    }

                    if (0 < lm && 0 < rm) {
                        viewWidth = measuredWidth - rm - lm;
                    }
                    break;

                case PAGE_PLAYLIST_AUDIO_ARTIST_TITLE:
                case PAGE_ARTICLE_TITLE_KEY:
                case PAGE_THUMBNAIL_TITLE_KEY:
                   /* if (jsonValueKeyMap.get(viewType) != null &&
                            (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_MODULE_KEY)) {
                        int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                        if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                            if (thumbnailHeight < thumbnailWidth) {
                                int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                                tm = heightByRatio + 4;
                            } else {
                                int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                                tm = heightByRatio + 4;
                            }
                        }
                    }*/

                    if (jsonValueKeyMap.get(viewType) != null &&
                            (jsonValueKeyMap.get(viewType) == PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_SEE_ALL_CATEGORY_02_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_SEE_ALL_CATEGORY_01_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_CONTINUE_WATCHING_02_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_AUDIO_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_ARTICLE_TRAY_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_TRAY_02_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == PAGE_TRAY_03_MODULE_KEY)) {
                        int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                        if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_AUDIO_TRAY_MODULE_KEY) {
                                int heightByRatio = (int) ((float) thumbnailWidth * 1.0f / 1.0f);
                                tm = heightByRatio + 4;
                            } else if (thumbnailHeight < thumbnailWidth) {
                                int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                                tm = heightByRatio + 4;
                            } else {
                                int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                                tm = heightByRatio + 4;
                            }
                        }
                    }

                    break;


                case PAGE_VIDEO_AGE_LABEL_KEY:
                    viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
                    view.setPadding(10, 0, 10, 0);
                    break;
                case PAGE_WATCHLIST_DURATION_KEY_BG:
                case PAGE_HISTORY_DURATION_KEY:
                case PAGE_DOWNLOAD_DURATION_KEY:
                case PAGE_GRID_THUMBNAIL_INFO:
                case PAGE_GRID_PHOTO_GALLERY_THUMBNAIL_INFO:
                case PAGE_LIBRARY_01_MODULE_KEY:

                    int padding = childComponent.getPadding();
                    view.setPadding(padding, 0, padding, 0);
                    if (jsonValueKeyMap.get(viewType) != null &&
                            (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_ARTICLE_TRAY_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_02_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_03_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_AC_BUNDLEDETAIL_TRAY_MODULE_KEY ||
                                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_TRAY_MODULE_KEY)) {
                        int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                        if (thumbnailHeight < thumbnailWidth) {
                            int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                            tm = heightByRatio - viewHeight;
                        } else {
                            int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                            tm = heightByRatio - viewHeight;
                        }
                    }
                    if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_WATCHLIST_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_WATCHLIST_02_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_LIBRARY_01_MODULE_KEY ||
                            jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY) {
                        int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                        int thumbnailHeight = (int) getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                        if (thumbnailHeight < thumbnailWidth) {
                            int heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                            tm += heightByRatio - viewHeight;
                        } else {
                            int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                            tm += heightByRatio - viewHeight;
                        }

                        if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY ||
                                jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_02_MODULE_KEY ||
                                jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY ||
                                jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY) {
                            tm -= 12;
                        }

                        lm += thumbnailWidth - viewWidth;
                    }
                    break;

                default:
                    break;
            }

            if (maxViewWidth != -1) {
                ((TextView) view).setMaxWidth(maxViewWidth);
            }
        } else if (componentType == AppCMSUIKeyType.PAGE_TEXTFIELD_KEY) {
            viewHeight *= 1.2;
        } else if (componentType == PAGE_SEPARATOR_VIEW_KEY
                && componentViewType == PAGE_VIDEO_PLAYER_MODULE_KEY
                && !isLandscape(getContext())) {
            tm = (int) ((float) deviceWidth * 9.0f / 16.0f);
        } else if (componentType == PAGE_VIDEO_PLAYER_VIEW_KEY) {
            if(uiBlockName==UI_BLOCK_STANDALONE_PLAYER_02&&isTablet(getContext())) {
            }
            else
                viewWidth = deviceWidth;
            viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
        } else if (componentType == PAGE_TABLE_VIEW_KEY) {
            int padding = childComponent.getPadding();
            view.setPadding(0, 0, 0, (int) convertDpToPixel(padding, getContext()));
            if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_TRAY_06_MODULE_KEY) {

                if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_EVENT_DETAIL_MODULE_KEY) {
                    viewHeight = (int) Math.round(getContext().getResources().getDisplayMetrics().heightPixels / 1.125);
                }

                RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                        if (MotionEventCompat.getActionMasked(e) == MotionEvent.ACTION_UP) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        } else {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    }
                };
                ((RecyclerView) view).addOnItemTouchListener(mScrollTouchListener);
            }
            if ((((RecyclerView) view).getAdapter() instanceof AppCMSPlaylistAdapter)) {
                padding = 20;
                view.setPadding(0, 0, 0, (int) convertDpToPixel(padding, getContext()));
            }

            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_AC_TEAM_SCHEDULE_MODULE_KEY) {
                padding = 70;
                view.setPadding(0, 0, 0, (int) convertDpToPixel(padding, getContext()));
            }
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_SETTING_MODULE_KEY ||
                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_LANGUAGE_SETTING_MODULE_KEY) {
                padding = 200;
                view.setPadding(0, 0, 0, (int) convertDpToPixel(padding, getContext()));
            }
        } else if (componentType == AppCMSUIKeyType.PAGE_EVENT_DETAIL_MODULE_KEY) {
            int padding = childComponent.getPadding();
            view.setPadding(0, 0, 0, (int) convertDpToPixel(padding, getContext()));
            viewHeight = (int) Math.round(getContext().getResources().getDisplayMetrics().heightPixels / 1.125);

        } else if (componentType == AppCMSUIKeyType.PAGE_PROGRESS_VIEW_KEY) {
            if (jsonValueKeyMap.get(viewType) != null) {
                if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_02_MODULE_KEY ||
                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                    int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                    int thumbnailHeight = (int) getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                    if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                        int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                        if (thumbnailHeight < thumbnailWidth) {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        }

                        tm = heightByRatio - viewHeight;
                    }
                } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_VIDEO_DETAILS_KEY) {
                    viewWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                    gravity = Gravity.CENTER_HORIZONTAL;
                } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_DOWNLOAD_01_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_01_MODULE_KEY ||
                        jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_HISTORY_02_MODULE_KEY) {
                    int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                    int thumbnailHeight = (int) getThumbnailHeight(getContext(), layout, LayoutParams.WRAP_CONTENT);
                    if (0 < thumbnailHeight && 0 < thumbnailWidth) {
                        int heightByRatio = (int) ((float) thumbnailWidth * 4.0f / 3.0f);
                        if (thumbnailHeight < thumbnailWidth) {
                            heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                        }

                        tm += heightByRatio - viewHeight;
                    }
                }

            }
        } /*else if (componentType == AppCMSUIKeyType.PAGE_PAGE_CONTROL_VIEW_03_KEY) {

            int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
            int heightByRatio = 0;
            if (jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_03_MODULE_TYPE &&
                     isLandscape(getContext())) {
                heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 32.0f);
            } else {
                heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
            }
            tm += heightByRatio;
        }*/else if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
            if ((componentKey == AppCMSUIKeyType.PAGE_CAROUSEL_IMAGE_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_CAROUSEL_BADGE_IMAGE_KEY)
            ) {
                if (0 < viewWidth && viewHeight <= 0
                ) {
                    if (isTablet(getContext())
                            && (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY)) {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 32.0f);
                    } else if (isLandscape(getContext())
                            && (componentViewType == AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE)) {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 32.0f);
                    } else {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
                    }
                }
            } else if (componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_ADD_TO_CALENDAR_BUTTON_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY) {
                int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.MATCH_PARENT);
                int heightByRatio = 0;
                if (isTablet(getContext())) {
                    if (isLandscape(getContext())) {
                        rm = Math.round(DEVICE_HEIGHT * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                                STANDARD_MOBILE_WIDTH_PX));
                    } else {
                        rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getTabletPortrait().getRightMargin() /
                                STANDARD_MOBILE_WIDTH_PX));
                    }
                } else {
                    rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getMobile().getRightMargin() /
                            STANDARD_MOBILE_WIDTH_PX));
                }
                if (componentViewType == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY
                        || componentViewType == AppCMSUIKeyType.PAGE_EVENT_CAROUSEL_03_MODULE_KEY
                        || componentViewType == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_GRID_KEY
                        || componentViewType == AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY) {
                    heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                    bm = viewHeight / 2;
                }
                if (componentViewType == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY) {
                    if (isTablet(getContext())) {
                        heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 32.0f);
                    } else {
                        heightByRatio = (int) ((float) thumbnailWidth * 9.0f / 16.0f);
                    }
                }

                viewWidth = (int) getViewWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);
                lm = (thumbnailWidth - (viewWidth + rm));
                if (componentKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_LIVE_BUTTON_KEY) {
                    tm = (heightByRatio - (bm + viewHeight));
                }
            } else if (componentKey == AppCMSUIKeyType.PAGE_PLAN_FEATURE_IMAGE_KEY) {
                gravity = Gravity.CENTER_HORIZONTAL;
            } else if (componentKey == AppCMSUIKeyType.PAGE_AUTOPLAY_MOVIE_IMAGE_KEY) {
                gravity = Gravity.LEFT;
            }else if (componentKey == AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_SUBMENU_KEY) {
                gravity = Gravity.CENTER_VERTICAL;
            } else if (componentKey == AppCMSUIKeyType.PAGE_BADGE_IMAGE_KEY||
                    componentKey == AppCMSUIKeyType.VIEW_BROWSE_SORT) {
                viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
                viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            } /*else if (componentKey == AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY) {
                if (getResources().getBoolean(R.bool.video_detail_page_plays_video)) {
                    if (!BaseView.isTablet(getContext())) {
                        if (BaseView.isLandscape(getContext())) {
                            viewWidth = viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }
                    } else {
                        if (ViewCreator.playerViewFullScreenEnabled()) {
                            viewWidth = viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }
                    }
                }
            }*/ else if (componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_THUMBNAIL_IMAGE_RECENT_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_INSTRUCTOR_BADGE_IMAGE_RECENT_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_THUMBNAIL_IMAGE_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_VIDEO_IMAGE_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_LIVE_SCHEDULE_LIVE_PLAY_BUTTON_KEY
            ) {
                if (0 < viewWidth && 0 < viewHeight) {
                    if ((viewWidth < viewHeight) && jsonValueKeyMap.get(viewType) != AppCMSUIKeyType.PAGE_PLAYLIST_MODULE_KEY) {
                        viewHeight = (int) ((float) viewWidth * 4.0f / 3.0f);
                    } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_PLAYLIST_MODULE_KEY) {
                        viewHeight = (int) ((float) viewWidth * 1.0f / 1.0f);
                    } else {
                        viewHeight = (int) ((float) viewWidth * 9.0f / 16.0f);
                    }
                }
            }

            if (componentKey == AppCMSUIKeyType.PAGE_BROWSE_VOD_PLAY_BUTTON_KEY) {
                if (isLandscape(getContext())) {
//                    Math.round(dpToPx((int)(childComponent.getLayout().getTabletLandscape().getRightMargin())));
                    rm = Math.round(DEVICE_WIDTH * (childComponent.getLayout().getTabletLandscape().getRightMargin() /
                            STANDARD_MOBILE_WIDTH_PX));
                }
            }
        }

        if ((useWidthOfScreen || componentKey == PAGE_VIDEO_PLAYER_VIEW_KEY_VALUE) && uiBlockName != UI_BLOCK_STANDALONE_PLAYER_02) {
            viewWidth = DEVICE_WIDTH;
        }

        if (componentType == PAGE_VIDEO_LAYER) {
            viewHeight = DEVICE_HEIGHT;
            viewWidth = DEVICE_WIDTH;
        }
        MarginLayoutParams marginLayoutParams = new MarginLayoutParams(viewWidth, viewHeight);
        marginLayoutParams.setMargins(lm, tm, rm, bm);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginLayoutParams);
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;

        if (componentType == PAGE_LABEL_KEY ||
                uiBlockName == UI_BLOCK_STANDALONE_PLAYER_02 ||
                componentType == PAGE_BUTTON_KEY ||
                componentType == PAGE_IMAGE_KEY) {
            layoutParams.gravity = gravity;
        }


        if (componentType == AppCMSUIKeyType.PAGE_TABLE_VIEW_KEY) {
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_VIDEO_PLAYLIST_MODULE_KEY) {
                layoutParams.height=BaseView.getDeviceHeight()-tm;
            }
            if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SUB_NAV_MODULE_KEY) {
                layoutParams.height = layoutParams.height - 100;
            }
        }
        if (componentType == AppCMSUIKeyType.PAGE_COLLECTIONGRID_KEY) {
          if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                    jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY) {
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.setMargins(0, tm, 0, bm);
            } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SUBSCRIPTION_IMAGEROW_02_KEY) {
                view.setPadding(0, tm, 0, 40);
            } else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_02_KEY) {
                if (isTablet(getContext())) {
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.setMargins(0, tm, 0, bm);
                } else {
                    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    Mobile mobile = layout.getMobile();
                    if (mobile.getLeftMargin() != 0f) {
                        lm = Math.round(DEVICE_WIDTH * (mobile.getLeftMargin() / STANDARD_MOBILE_WIDTH_PX));
                    }
                    if (mobile.getRightMargin() != 0f) {
                        rm = Math.round(DEVICE_WIDTH * (mobile.getRightMargin() / STANDARD_MOBILE_WIDTH_PX));
                    }
                    layoutParams.setMargins(lm, tm, rm, bm);
                    if (appCMSPresenter != null && !appCMSPresenter.isSinglePlanFeatureAvailable()) {
                        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.setMargins(0, tm, 0, bm);
                    }

                    if (appCMSPresenter != null && appCMSPresenter.isMOTVApp()) {
                        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        layoutParams.setMargins(0, tm, 0, bm);
                    }
                }
            }/*else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_SEASON_TRAY_MODULE_KEY) {
                layoutParams.height=BaseView.getDeviceHeight();

            }*/ else if (jsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_CONTINUE_WATCHING_MODULE_KEY) {
                tm *= +1.6;
                layoutParams.setMargins(lm, tm, rm, bm);
            } else if (componentKey == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_GRID_KEY
                    || componentKey == AppCMSUIKeyType.PAGE_EDIT_PROFILE_GRID_KEY) {
                //Special case of Live Page UI
                int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);
                int width = getDeviceWidth();
                layoutParams.width = (width - thumbnailWidth);
            } else if (componentKey == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_VIEW_GRID_KEY) {
                //Special case of Live Page UI
                int thumbnailWidth = (int) getThumbnailWidth(getContext(), layout, LayoutParams.WRAP_CONTENT);
                int width = getDeviceWidth();
                layoutParams.width = (width - thumbnailWidth);
                layoutParams.setMargins(0, 0, 0, (int) convertDpToPixel(40, getContext()));
                layoutParams.height = isTablet(getContext()) ? isLandscape(getContext()) ? (int) (getDeviceHeight() * 0.63) : (int) (getDeviceHeight() * 0.75) : (int) (getDeviceHeight() * 0.595);
            } else if (componentKey == AppCMSUIKeyType.PAGE_BROWSE_TOP_GRID_KEY) {
                layoutParams.width = (int) (getDeviceWidth() * 0.85733);
            }
        }

        view.setLayoutParams(layoutParams);
    }

    protected void initializeComponentHasViewList(int size) {
        componentHasViewList = new boolean[size];
    }

    protected ViewGroup createChildrenContainer() {
        childrenContainer = new FrameLayout(getContext());
        int viewWidth = (int) getViewWidth(getContext(), getLayout(), (float) LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(getContext(), getLayout(), (float) LayoutParams.MATCH_PARENT);
        if (isLandscape(getContext())) {
            viewHeight *= TABLET_LANDSCAPE_HEIGHT_SCALE;
        }
        FrameLayout.LayoutParams childContainerLayoutParams =
                new FrameLayout.LayoutParams(viewWidth, viewHeight);
        childrenContainer.setLayoutParams(childContainerLayoutParams);
        addView(childrenContainer);
        return childrenContainer;
    }
    protected ViewGroup createTrayChildrenContainer() {
        childrenContainer = new FrameLayout(getContext());
        FrameLayout.LayoutParams childContainerLayoutParams =
                new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        childContainerLayoutParams.setMargins(15,10,15,10);
        childrenContainer.setLayoutParams(childContainerLayoutParams);
        addView(childrenContainer);
        return childrenContainer;
    }

    protected int getFontsize(Context context, Component component) {
        if (context != null && component != null) {
            if (component.getFontSize() > 0) {
                return component.getFontSize();
            }
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (component.getLayout().getTabletLandscape().getFontSize() > 0) {
                        return component.getLayout().getTabletLandscape().getFontSize();
                    }
                } else {
                    if (component.getLayout().getTabletPortrait().getFontSize() > 0) {
                        return component.getLayout().getTabletPortrait().getFontSize();
                    }
                }
            } else {
                if (component.getLayout().getMobile().getFontSize() > 0) {
                    return component.getLayout().getMobile().getFontSize();
                }
            }
        }
        return 0;
    }

    protected boolean shouldShowView(Component component) {
        if (component != null) {
            if (isTablet(getContext())) {
                return component.isVisibleForTablet() || !component.isVisibleForPhone();
            } else {
                return !component.isVisibleForTablet() || component.isVisibleForPhone();
            }
        }
        return true;
    }

    public static float getGridWidth(Context context, Layout layout, int defaultWidth) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float width = tabletLandscape.getGridWidth() != 0f ? tabletLandscape.getGridWidth() : -1.0f;
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_HEIGHT_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float width = tabletPortrait.getGridWidth() != 0f ? tabletPortrait.getGridWidth() : -1.0f;
                    if (width != -1.0f) {
                        return DEVICE_WIDTH * (width / STANDARD_TABLET_WIDTH_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float width = mobile.getGridWidth() != 0f ? mobile.getGridWidth() : -1.0f;
                if (width != -1.0f) {
                    return DEVICE_WIDTH * (width / STANDARD_MOBILE_WIDTH_PX);
                }
            }
        }
        return defaultWidth;
    }

    public static float getGridHeight(Context context, Layout layout, int defaultHeight) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    TabletLandscape tabletLandscape = layout.getTabletLandscape();
                    float height = tabletLandscape.getGridHeight() != 0f ? tabletLandscape.getGridHeight() : -1.0f;
                    if (height != -1.0f) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_WIDTH_PX);
                    }
                } else {
                    TabletPortrait tabletPortrait = layout.getTabletPortrait();
                    float height = tabletPortrait.getGridHeight() != 0f ? tabletPortrait.getGridHeight() : -1.0f;
                    if (height != -1) {
                        return DEVICE_HEIGHT * (height / STANDARD_TABLET_HEIGHT_PX);
                    }
                }
            } else {
                Mobile mobile = layout.getMobile();
                float height = mobile.getGridHeight() != 0f ? mobile.getGridHeight() : -1.0f;
                if (height != -1.0f) {
                    return DEVICE_HEIGHT * (height / STANDARD_MOBILE_HEIGHT_PX);
                }
            }
        }
        return defaultHeight;
    }

    protected float getTrayPadding(Context context, Layout layout) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTrayPadding() != 0f) {
                        return layout.getTabletLandscape().getTrayPadding();
                    }
                } else {
                    if (layout.getTabletPortrait().getTrayPadding() != 0f) {
                        return layout.getTabletPortrait().getTrayPadding();
                    }
                }
            } else {
                if (layout.getMobile().getTrayPadding() != 0f) {
                    return layout.getMobile().getTrayPadding();
                }
            }
        }
        return -1.0f;
    }

    protected float getTrayPaddingVertical(Context context, Layout layout) {
        if (context != null && layout != null) {
            if (isTablet(context)) {
                if (isLandscape(context)) {
                    if (layout.getTabletLandscape().getTrayPaddingVertical() != 0f) {
                        return layout.getTabletLandscape().getTrayPaddingVertical();
                    }
                } else {
                    if (layout.getTabletPortrait().getTrayPaddingVertical() != 0f) {
                        return layout.getTabletPortrait().getTrayPaddingVertical();
                    }
                }
            } else {
                if (layout.getMobile().getTrayPaddingVertical() != 0f) {
                    return layout.getMobile().getTrayPaddingVertical();
                }
            }
        }
        return -1.0f;
    }

    static AppCMSPresenter appCMSPresenter;

    public static void setPreseneter(AppCMSPresenter presenter) {
        appCMSPresenter = presenter;
    }

    public int getTopMargin(Context context, Layout layout) {
        int topMargin = 0;
        if (isTablet(context)) {
            if (isLandscape(context)) {
                topMargin = (int) layout.getTabletLandscape().getTopMargin();
            } else {
                topMargin = (int) layout.getTabletPortrait().getTopMargin();
            }
        } else {

            topMargin = (int) layout.getMobile().getTopMargin();
        }
        return topMargin;
    }

}

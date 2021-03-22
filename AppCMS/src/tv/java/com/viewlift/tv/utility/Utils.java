package com.viewlift.tv.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.widget.ListRow;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.LoginSignup;
import com.viewlift.models.data.appcms.ui.tv.FireTV;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.views.customviews.CustomTVVideoPlayerView;
import com.viewlift.tv.views.fragment.AccountDetailsEditDialogFragment;
import com.viewlift.tv.views.fragment.ClearDialogFragment;
import com.viewlift.tv.views.fragment.PreviewDialogFragment;
import com.viewlift.tv.views.fragment.RecommendationFragment;
import com.viewlift.tv.views.fragment.SwitchSeasonsDialogFragment;
import com.viewlift.tv.views.fragment.TVODPurchaseDialogFragment;
import com.viewlift.views.binders.AppCMSSwitchSeasonBinder;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.viewlift.presenters.AppCMSPresenter.DIALOG_FRAGMENT_TAG;
import static com.viewlift.utils.CommonUtils.getColor;

public class Utils {

    public static final int STANDARD_TABLET_HEIGHT_PX = 1080;
    public static final int STANDARD_TABLET_WIDTH_PX = 1920;
    private static final int DEAFULT_PADDING = 0;
    public static final String DEGREE = "\u00b0";
    public static boolean isPlayerComponentClicked;
    private static Animation animLeftSlide, animRightSlide;
    public static boolean isPlayerSelected = false;
    public static boolean isMiniPlayer = true;
    public static CustomTVVideoPlayerView customTVVideoPlayerView = null;

    public static HashMap<String, ListRow> recommendationTraysData = new HashMap<>();
    public static HashMap<String, Integer> recommendationTraysDataInndex = new HashMap<>();

    public static void setBrowseFragmentViewParameters(View browseFragmentView, int marginLeft,
                                                       int marginTop) {
        //View browseContainerDoc = browseFragmentView.findViewById(R.id.browse_container_dock);
        View browseContainerDoc = browseFragmentView.findViewById(R.id.browse_frame);
        Log.d("Utils.java", "BrowseFragment Margin Left = " + marginLeft + "marginTop = " + marginTop);
        if (null != browseContainerDoc) {
            browseContainerDoc.setBackgroundColor(Color.TRANSPARENT);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) browseContainerDoc
                    .getLayoutParams();
            params.leftMargin = marginLeft;// -80;
            params.topMargin = marginTop;// -225;
            params.bottomMargin = 0;
            browseContainerDoc.setLayoutParams(params);

        }

        View browseHeaders = browseFragmentView.findViewById(R.id.browse_headers);
        if (null != browseHeaders) {
            browseHeaders.setBackgroundColor(Color.TRANSPARENT);
        }

        View containerList = browseFragmentView.findViewById(R.id.container_list);
        if (null != containerList) {
            containerList.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    public static String loadJsonFromAssets(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public static float getViewHeight(Context context, Layout layout, float defaultHeight) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            float height = getViewHeight(fireTV);
            if (height != -1.0f) {
                return getViewYAxisAsPerScreen(context, (int) height);
            }
        }
        return defaultHeight;
    }


    public static float getViewWidth(Context context, Layout layout, float defaultWidth) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            float width = getViewWidth(fireTV);
            if (width != -1.0f) {
                return getViewXAxisAsPerScreen(context, (int) width);
                // return width;
            }
        }
        return defaultWidth;
    }

    public static float getItemViewHeight(Context context, Layout layout, float defaultHeight) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            float height = getItemViewHeight(fireTV);
            if (height != -1.0f) {
                return getViewYAxisAsPerScreen(context, (int) height);
            }
        }
        return defaultHeight;
    }

    public static float getItemViewWidth(Context context, Layout layout, float defaultWidth) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            float width = getItemViewWidth(fireTV);
            if (width != -1.0f) {
                return getViewXAxisAsPerScreen(context, (int) width);
                // return width;
            }
        }
        return defaultWidth;
    }


    public static int getViewXAxisAsPerScreen(Context context, int dimension) {
        float dim = context.getResources().getDisplayMetrics().widthPixels
                * ((float) dimension / STANDARD_TABLET_WIDTH_PX);
        return Math.round(dim);
    }


    public static int getViewYAxisAsPerScreen(Context context, int dimension) {
        float dim = context.getResources().getDisplayMetrics().heightPixels
                * ((float) dimension / STANDARD_TABLET_HEIGHT_PX);
        return Math.round(dim);
    }


    public static int getLeftPadding(Context context, Layout layout) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            if (null != fireTV && null != fireTV.getLeftMargin()) {
                return Integer.valueOf(layout.getTv().getLeftMargin());
            } else {
                return DEAFULT_PADDING;
            }
        }
        return DEAFULT_PADDING;
    }

    public static int getRightPadding(Context context, Layout layout) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            if (null != fireTV && null != fireTV.getRightMargin()) {
                return Integer.valueOf(layout.getTv().getRightMargin());
            } else {
                return DEAFULT_PADDING;
            }
        }
        return DEAFULT_PADDING;
    }

    public static int getTopPadding(Context context, Layout layout) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            if (null != fireTV && null != fireTV.getTopMargin()) {
                return Integer.valueOf(layout.getTv().getTopMargin());
            } else {
                return DEAFULT_PADDING;
            }
        }
        return DEAFULT_PADDING;
    }

    public static int getBottomPadding(Context context, Layout layout) {
        if (layout != null) {
            FireTV fireTV = layout.getTv();
            if (null != fireTV && null != fireTV.getBottomMargin()) {
                return Integer.valueOf(layout.getTv().getBottomMargin());
            } else {
                return DEAFULT_PADDING;
            }
        }
        return DEAFULT_PADDING;
    }


    public static float getViewHeight(FireTV fireTV) {
        if (fireTV != null) {
            if (fireTV.getHeight() != null) {
                return Float.valueOf(fireTV.getHeight());
            }
        }
        return -1.0f;
    }


    public static float getViewWidth(FireTV fireTV) {
        if (fireTV != null) {
            if (fireTV.getWidth() != null) {
                return Float.valueOf(fireTV.getWidth());
            }
        }
        return -1.0f;
    }


    public static float getItemViewHeight(FireTV fireTV) {
        if (fireTV != null) {
            if (fireTV.getItemHeight() != null) {
                return Float.valueOf(fireTV.getItemHeight());
            }
        }
        return -1.0f;
    }


    public static float getItemViewWidth(FireTV fireTV) {
        if (fireTV != null) {
            if (fireTV.getItemWidth() != null) {
                return Float.valueOf(fireTV.getItemWidth());
            }
        }
        return -1.0f;
    }


    public static float getFontSizeKey(Context context, Layout layout) {
        {
            if (layout.getTv().getFontSizeKey() != null) {
                return layout.getTv().getFontSizeKey();
            }
        }
        return -1.0f;
    }


    public static float getFontSizeValue(Context context, Layout layout) {
        if (layout.getTv().getFontSizeValue() != null) {
            return layout.getTv().getFontSizeValue();
        }
        return -1.0f;
    }

    public static StateListDrawable getNavigationSelector(Context context, AppCMSPresenter appCMSPresenter, boolean isSubNavigation, int selectedColor, boolean shouldShowLeftFocus) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getNavigationSelectedState(context, appCMSPresenter, isSubNavigation, selectedColor, shouldShowLeftFocus));
        res.addState(new int[]{android.R.attr.state_pressed}, getNavigationSelectedState(context, appCMSPresenter, isSubNavigation, selectedColor, shouldShowLeftFocus));
        res.addState(new int[]{android.R.attr.state_selected}, getNavigationSelectedState(context, appCMSPresenter, isSubNavigation, selectedColor, shouldShowLeftFocus));
        res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        return res;
    }

    public static Drawable getProgressDrawable(Context context, String unProgressColor, AppCMSPresenter appCMSPresenter) {
        ShapeDrawable shape = new ShapeDrawable();
        shape.getPaint().setStyle(Paint.Style.FILL);
        shape.getPaint().setColor(Color.parseColor(getColor(context, unProgressColor)));
        ShapeDrawable shapeD = new ShapeDrawable();
        shapeD.getPaint().setStyle(Paint.Style.FILL);
        shapeD.getPaint().setColor(
                Color.parseColor(getFocusColor(context, appCMSPresenter)));
        ClipDrawable clipDrawable = new ClipDrawable(shapeD, Gravity.LEFT,
                ClipDrawable.HORIZONTAL);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                shape, clipDrawable});
        return layerDrawable;
    }


    public static GradientDrawable getSelectedMenuState(Context context, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(2, color);
        return gradientDrawable;
    }

    public static GradientDrawable getUnSelectedMenuState(Context context, String borderColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(ContextCompat.getColor(context, android.R.color.transparent));
        if (null != borderColor)
            gradientDrawable.setStroke(2, Color.parseColor(borderColor));
        return gradientDrawable;
    }

    public static StateListDrawable getMenuSelector(Context context, String selectedBackgroundColor, String borderColor) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getSelectedMenuState(context, Color.parseColor(selectedBackgroundColor)));
        res.addState(new int[]{android.R.attr.state_pressed}, getSelectedMenuState(context, Color.parseColor(selectedBackgroundColor)));
        res.addState(new int[]{android.R.attr.state_selected}, getUnSelectedMenuState(context, borderColor));
        res.addState(new int[]{}, getUnSelectedMenuState(context, borderColor));
        return res;
    }

    public static LayerDrawable getNavigationSelectedState(Context context, AppCMSPresenter appCMSPresenter,
                                                           boolean isSubNavigation, int selectorColor, boolean showLeftFocus) {
        GradientDrawable focusedLayer = new GradientDrawable();
        focusedLayer.setShape(GradientDrawable.RECTANGLE);
        focusedLayer.setColor(Color.parseColor(getFocusColor(context, appCMSPresenter)));

        GradientDrawable transparentLayer = new GradientDrawable();
        transparentLayer.setShape(GradientDrawable.RECTANGLE);
        if (appCMSPresenter.isNewsLeftNavigationEnabled()) {
            transparentLayer.setColor(selectorColor);
        } else if (appCMSPresenter.isLeftNavigationEnabled()) {
            transparentLayer.setColor(ContextCompat.getColor(context, R.color.transparentColor));
        } else if (isSubNavigation) {
            transparentLayer.setColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        } else {
            transparentLayer.setColor(selectorColor);
        }

        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                focusedLayer,
                transparentLayer
        });

        if (appCMSPresenter.isLeftNavigationEnabled()) {
            layerDrawable.setLayerInset(0, 0, 0, (int) convertDpToPixel(296, context), 0);
        } else if (isSubNavigation) {
            layerDrawable.setLayerInset(1, 0, 0, 0, 5);
        } else {
            if (showLeftFocus) {
                layerDrawable.setLayerInset(1, 5, 0, 0, 0);
            } else {
                layerDrawable.setLayerInset(1, 0, 5, 0, 0);
            }

        }
        return layerDrawable;
    }

    /**
     * this method is use for setting the tray border.
     *
     * @param context
     * @param selectedColor
     * @param component
     * @return
     */
    public static StateListDrawable getTrayBorder(Context context, String selectedColor, Component component) {
        boolean isEditText = false;
        boolean isWeatherWidget = false;
        if (null != component) {
            isEditText = context.getString(R.string.app_cms_page_textfield_key).equalsIgnoreCase(component.getType());
            isWeatherWidget = context.getString(R.string.weatherWidgetView).equalsIgnoreCase(component.getKey());
        }

        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, false));
        res.addState(new int[]{android.R.attr.state_pressed}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, false));
        res.addState(new int[]{android.R.attr.state_selected}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, false));
        if (isEditText || isWeatherWidget)
            res.addState(new int[]{}, getBorder(context, selectedColor, isEditText, isWeatherWidget, component, true));
        else
            res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(
                    context,
                    android.R.color.transparent
            )));
        return res;
    }

    /**
     * this method is use for setting the tray round border.
     *
     * @param context
     * @param selectedColor
     * @param component
     * @return
     */
    public static StateListDrawable getTrayRoundBorder(Context context, String selectedColor, Component component) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getBorderOval(context, selectedColor, component, false));
        res.addState(new int[]{android.R.attr.state_pressed}, getBorderOval(context, selectedColor, component, false));
        res.addState(new int[]{android.R.attr.state_selected}, getBorderOval(context, selectedColor, component, false));
            res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(
                    context,
                    android.R.color.transparent
            )));
        return res;
    }

    public static StateListDrawable getTrayBorder(Context context, String primaryHover, String secondaryHover, boolean isSameTextandPrimaryHoverColor) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getGradientDrawable(primaryHover, secondaryHover, isSameTextandPrimaryHoverColor));
        res.addState(new int[]{android.R.attr.state_pressed}, getGradientDrawable(primaryHover, secondaryHover, isSameTextandPrimaryHoverColor));
        res.addState(new int[]{android.R.attr.state_selected}, getGradientDrawable(primaryHover, secondaryHover, isSameTextandPrimaryHoverColor));
        res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        return res;
    }

    public static StateListDrawable getGradientTrayBorder(Context context, String primaryHover, String secondaryHover) {
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getGradientDrawable(context, primaryHover, secondaryHover));
        res.addState(new int[]{android.R.attr.state_pressed}, getGradientDrawable(context, primaryHover, secondaryHover));
        res.addState(new int[]{android.R.attr.state_selected}, getGradientDrawable(context, primaryHover, secondaryHover));
        res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(
                context,
                android.R.color.transparent
        )));
        return res;
    }

    private static Drawable getGradientDrawable(Context context, String primaryHover, String secondaryHover) {

        LayerDrawable layerDrawable = (LayerDrawable) context.getResources().getDrawable(R.drawable.player_border);
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.getDrawable(0);
        gradientDrawable.setColors(new int[]{Color.parseColor(primaryHover), Color.parseColor(secondaryHover)});
        return layerDrawable;
    }

    private static GradientDrawable getBorder(Context context, String borderColor, boolean isEditText, boolean isWeatherWidget, Component component, boolean isNormalState) {
        GradientDrawable ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.RECTANGLE);

        if (isEditText && component != null)
            ageBorder.setCornerRadius(component.getCornerRadius());

        if (!isNormalState && component != null && component.isBorderEnable())
            ageBorder.setStroke(6, Color.parseColor(borderColor));

        if (isEditText && isNormalState) {
            ageBorder.setStroke(1, Color.parseColor(borderColor));
        }

        if (component != null && component.getStroke() != 0) {
            ageBorder.setStroke(component.getStroke(), Color.parseColor(borderColor));
        }

        ageBorder.setColor(ContextCompat.getColor(
                context,
                isEditText ? android.R.color.white : android.R.color.transparent
        ));

        if (isWeatherWidget) {
            ageBorder.setColor(Color.parseColor(component.getBackgroundColor()));
        }

        return ageBorder;
    }

    private static GradientDrawable getBorderOval(Context context, String borderColor, Component component, boolean isNormalState) {
        GradientDrawable ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.OVAL);


        if (!isNormalState && component != null && component.isBorderEnable())
            ageBorder.setStroke(6, Color.parseColor(borderColor));

        if (isNormalState) {
            ageBorder.setStroke(1, Color.parseColor(borderColor));
        }

        if (component != null && component.getStroke() != 0) {
            ageBorder.setStroke(component.getStroke(), Color.parseColor(borderColor));
        }

        ageBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent
        ));

        return ageBorder;
    }

    private static GradientDrawable getGradientDrawable(String primaryHover, String secondaryHover, boolean isSameTextandPrimaryHoverColor) {
        return new GradientDrawable(
                isSameTextandPrimaryHoverColor ? GradientDrawable.Orientation.TOP_BOTTOM : GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{Color.parseColor(primaryHover),
                        Color.parseColor(secondaryHover)});
    }


    public static StateListDrawable setPageLinkButtonSelector(Context context,
                                                              int selectedColor) {
        GradientDrawable unfocusDrawable = new GradientDrawable();
        unfocusDrawable.setShape(GradientDrawable.RECTANGLE);
        unfocusDrawable.setColor(ContextCompat.getColor(context, android.R.color.transparent));
        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(selectedColor));
        res.addState(new int[]{android.R.attr.state_pressed}, unfocusDrawable);
        res.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(selectedColor));
        res.addState(new int[]{android.R.attr.state_enabled}, unfocusDrawable);
        res.addState(new int[]{}, unfocusDrawable);
        return res;
    }

    public static ColorStateList setPageLinkButtonTextColor(String defaultColor, String focusedColor) {

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };


        int[] colors = new int[]{
                Color.parseColor(focusedColor),
                Color.parseColor(focusedColor),
                Color.parseColor(focusedColor),
                Color.parseColor(defaultColor)
        };
        ColorStateList myList = new ColorStateList(states, colors);
        return myList;
    }

    /**
     * this method is use for setting the button background selector.
     *
     * @param context
     * @param selectedColor
     * @param component
     * @return
     */
    public static StateListDrawable setButtonBackgroundSelector(Context context,
                                                                int selectedColor,
                                                                Component component,
                                                                AppCMSPresenter appCMSPresenter) {

        String focusStateColor = null;
        String unFocusStateBorderColor = null;
        String focusStateBorderColor = null;
        String borderWidth = null;
        if (null != appCMSPresenter &&
                null != appCMSPresenter.getAppCMSMain() &&
                null != appCMSPresenter.getAppCMSMain().getBrand() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta()) {
            if (null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
                focusStateColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor();
            }
            if (null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary()) {
                unFocusStateBorderColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getColor();
                if(!component.isBorderEnable()){
                    unFocusStateBorderColor = "#00000000";
                }
                borderWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth();
                if (null != borderWidth) {
                    if (borderWidth.contains("px")) {
                        String[] bdWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth().split("px");
                        borderWidth = bdWidth[0];
                    }
                }
            }
            if (null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
                focusStateBorderColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBorder().getColor();
                borderWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth();
                if (null != borderWidth) {
                    if (borderWidth.contains("px")) {
                        String[] bdWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth().split("px");
                        borderWidth = bdWidth[0];
                    }
                }
            }
        }

        if (null != focusStateColor) {
            selectedColor = Color.parseColor(focusStateColor);
        }

        StateListDrawable res = new StateListDrawable();
        if (appCMSPresenter.isNewsTemplate())
            res.addState(new int[]{android.R.attr.state_focused}, getButtonNormalState(context, appCMSPresenter, component, focusStateBorderColor, borderWidth));
        else
        res.addState(new int[]{android.R.attr.state_focused},  getButtonFocusedState(context,appCMSPresenter, component, focusStateColor, borderWidth));

        res.addState(new int[]{android.R.attr.state_pressed}, getButtonDefaultState(context,appCMSPresenter, unFocusStateBorderColor, borderWidth, component != null ? component.getCornerRadius():0));
        res.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(selectedColor));
        res.addState(new int[]{android.R.attr.state_enabled}, getButtonNormalState(context, appCMSPresenter, component, unFocusStateBorderColor, borderWidth));
        res.addState(new int[]{-android.R.attr.state_enabled}, new ColorDrawable(context.getResources().getColor(android.R.color.darker_gray)));

        if (null != component) {
            GradientDrawable gradientDrawable = getButtonNormalState(context, appCMSPresenter, component, unFocusStateBorderColor, borderWidth);
            if (null != gradientDrawable)
                res.addState(new int[]{}, gradientDrawable);
        } else {
            GradientDrawable gradientDrawable = getButtonDefaultState(context,appCMSPresenter,unFocusStateBorderColor, borderWidth, 0);
            if (null != gradientDrawable)
                res.addState(new int[]{}, gradientDrawable);
        }
        return res;
    }

    private static GradientDrawable getButtonDefaultState(Context context,AppCMSPresenter appCMSPresenter, String unFocusStateBorderColor, String borderWidth, int cornerRadius) {
        GradientDrawable
                ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.RECTANGLE);
        ageBorder.setCornerRadius(cornerRadius);
        if(appCMSPresenter.isNewsTemplate()) ageBorder.setCornerRadius(4);
        ageBorder.setStroke(null != borderWidth ? Integer.valueOf(borderWidth) : 1,
                Color.parseColor(unFocusStateBorderColor != null ? unFocusStateBorderColor : "#000000"));
        ageBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
        return ageBorder;
    }

    private static GradientDrawable getButtonNormalState(Context context, AppCMSPresenter appCMSPresenter, Component component, String unFocusStateBorderColor, String borderWidth) {
        GradientDrawable ageBorder = null;
        if (null != component && component.getBorderWidth() != 0 && component.getBorderColor() != null) {
            if (component.getBorderWidth() > 0 && !TextUtils.isEmpty(component.getBorderColor())) {
                ageBorder = new GradientDrawable();
                ageBorder.setShape(GradientDrawable.RECTANGLE);
                ageBorder.setCornerRadius(component.getCornerRadius());
                if (appCMSPresenter.isNewsTemplate()) ageBorder.setCornerRadius(4);
                ageBorder.setStroke(null != borderWidth ? Integer.valueOf(borderWidth) : component.getBorderWidth(),
                        Color.parseColor(unFocusStateBorderColor != null ? unFocusStateBorderColor : getColor(context, component.getBorderColor())));
                ageBorder.setColor(ContextCompat.getColor(context, android.R.color.transparent));
            }
        }
        return ageBorder;
    }

    private static GradientDrawable getButtonFocusedState(Context context, AppCMSPresenter appCMSPresenter, Component component, String focusStateBorderColor, String borderWidth) {
        GradientDrawable ageBorder = null;
        ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.RECTANGLE);
        if (component != null) ageBorder.setCornerRadius(component.getCornerRadius());
        if (focusStateBorderColor != null)
            ageBorder.setColor(Color.parseColor(focusStateBorderColor));
        return ageBorder;
    }


    public static ColorStateList getButtonTextColorDrawable(String defaultColor, String focusedColor, AppCMSPresenter appCMSPresenter) {
        String focusStateTextColor = null;
        String unFocusStateTextColor = null;
        if (null != appCMSPresenter &&
                null != appCMSPresenter.getAppCMSMain() &&
                null != appCMSPresenter.getAppCMSMain().getBrand() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            focusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor();
            unFocusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getTextColor();
        }

        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };

        if (null != focusStateTextColor) {
            focusedColor = focusStateTextColor;
        }
        if (null != unFocusStateTextColor) {
            defaultColor = unFocusStateTextColor;
        }
        int[] colors = new int[]{
                Color.parseColor(focusedColor),
                Color.parseColor(focusedColor),
                Color.parseColor(focusedColor),
                Color.parseColor(defaultColor)
        };
        ColorStateList myList = new ColorStateList(states, colors);
        return myList;
    }

    /**
     * this method is use for setting the textCoo
     *
     * @param context
     * @param appCMSPresenter
     * @return
     */
    public static ColorStateList getTextColorDrawable(Context context, AppCMSPresenter appCMSPresenter) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };
        String focusBorderColor = getFocusBorderColor(context, appCMSPresenter);
        String focusColor = getFocusColor(context, appCMSPresenter);
        String textColor = getTextColor(context, appCMSPresenter);
        int[] colors = new int[]{
                Color.parseColor(focusColor),
                Color.parseColor(focusColor),
                Color.parseColor(focusColor),
                Color.parseColor(textColor)
        };
        ColorStateList myList = new ColorStateList(states, colors);
        return myList;
    }

    public static Typeface getTypeFace(Context context,
                                       AppCMSPresenter appCMSPresenter,
                                       Component component) {
        Typeface face;

        Map<String, AppCMSUIKeyType> jsonValueKeyMap = appCMSPresenter.getJsonValueKeyMap();

        // make sure the returned font is never null
        face = Typeface.createFromAsset(context.getAssets(),
                context.getString(R.string.opensans_regular_ttf));

        try {
            AppCMSUIKeyType fontFamilyKey = jsonValueKeyMap.get(appCMSPresenter.getFontFamily());
            if (fontFamilyKey == null) {
                // if we get unsupported font, fallback to open sans
                fontFamilyKey = AppCMSUIKeyType.PAGE_TEXT_OPENSANS_FONTFAMILY_KEY;
            }
            if (fontFamilyKey == AppCMSUIKeyType.PAGE_TEXT_OPENSANS_FONTFAMILY_KEY) {
                AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(component.getFontWeight());
                if (fontWeight == null) {
                    fontWeight = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                switch (fontWeight) {
                    case PAGE_TEXT_BOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.opensans_bold_ttf));
                        break;
                    case PAGE_TEXT_SEMIBOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.opensans_semibold_ttf));
                        break;
                    case PAGE_TEXT_EXTRABOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.opensans_extrabold_ttf));
                        break;
                    default:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.opensans_regular_ttf));
                }
            } else if (fontFamilyKey == AppCMSUIKeyType.PAGE_TEXT_LATO_FONTFAMILY_KEY) {
                AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(component.getFontWeight());
                if (fontWeight == null) {
                    fontWeight = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                switch (fontWeight) {
                    case PAGE_TEXT_BOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.lato_bold));
                        break;
                    case PAGE_TEXT_MEDIUM_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.lato_medium));
                        break;
                    case PAGE_TEXT_LIGHT_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.lato_light));
                        break;
                    default:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.lato_regular));
                }
            } else if (fontFamilyKey == AppCMSUIKeyType.PAGE_TEXT_MONTSERRAT_FONT_FAMILY_KEY) {
                AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(component.getFontWeight());
                if (fontWeight == null) {
                    fontWeight = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                switch (fontWeight) {
                    case PAGE_TEXT_BOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.montserrat_bold_ttf));
                        break;
                    case PAGE_TEXT_SEMIBOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.montserrat_semibold_ttf));
                        break;
                    case PAGE_TEXT_ITALIC_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.montserrat_extra_light_italic_ttf));
                        break;
                    default:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.montserrat_regular_ttf));
                }
            } else if (fontFamilyKey == AppCMSUIKeyType.PAGE_TEXT_PROXIMA_NOVA_FONT_FAMILY_KEY) {
                AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(component.getFontWeight());
                if (fontWeight == null) {
                    fontWeight = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                switch (fontWeight) {
                    case PAGE_TEXT_BOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.proxima_nova_bold_ttf));
                        break;
                    case PAGE_TEXT_SEMIBOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.proxima_nova_semibold_ttf));
                        break;
                    case PAGE_TEXT_LIGHT_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.proxima_nova_light_ttf));
                        break;
                    default:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.proxima_nova_regular_ttf));
                }
            } else if (fontFamilyKey == AppCMSUIKeyType.PAGE_TEXT_DIN_PRO_FONT_FAMILY_KEY) {
                AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(component.getFontWeight());
                if (fontWeight == null) {
                    fontWeight = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                switch (fontWeight) {
                    case PAGE_TEXT_BOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.din_pro_bold_otf));
                        break;
                    case PAGE_TEXT_SEMIBOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.din_pro_semibold_otf));
                        break;
                    case PAGE_TEXT_EXTRABOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.din_pro_extrabold_otf));
                        break;
                    case PAGE_TEXT_ITALIC_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.din_pro_italic_otf));
                        break;
                    default:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.din_pro_regular_otf));
                }
            } else if (fontFamilyKey == AppCMSUIKeyType.PAGE_TEXT_TITILLIUM_WEB_FONT_FAMILY_KEY) {
                AppCMSUIKeyType fontWeight = jsonValueKeyMap.get(component.getFontWeight());
                if (fontWeight == null) {
                    fontWeight = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                }
                switch (fontWeight) {
                    case PAGE_TEXT_BOLD_KEY:
                    case PAGE_TEXT_EXTRABOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_bold_ttf));
                        break;
                    case PAGE_TEXT_SEMIBOLD_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_semibold_ttf));
                        break;
                    case PAGE_TEXT_SEMIBOLD_ITALIC_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_semibold_italic_ttf));
                        break;
                    case PAGE_TEXT_ITALIC_KEY:
                    case PAGE_TEXT_LIGHT_ITALIC_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_light_italic_ttf));
                        break;
                    case PAGE_TEXT_LIGHT_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_light_ttf));
                        break;
                    case PAGE_TEXT_BLACK_KEY:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_black_ttf));
                        break;
                    default:
                        face = Typeface.createFromAsset(context.getAssets(),
                                context.getString(R.string.titillium_web_regular_ttf));
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return face;
    }

    public static String getSubscriptionEndDateFromRenewalCycleType(String renewalCycleType,
                                                                    int renewalCyclePeriodMultiplier) {
        try {
            Calendar cal = Calendar.getInstance();
            if (renewalCycleType.equalsIgnoreCase("MONTH")) {
                cal.add(Calendar.MONTH, renewalCyclePeriodMultiplier);
            } else if (renewalCycleType.equalsIgnoreCase("YEAR")) {
                cal.add(Calendar.YEAR, renewalCyclePeriodMultiplier);
            }
            return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public enum AnimationType {
        LEFT, RIGHT, TOP, BOTTOM, FADE_IN, FADE_OUT;
    }

    public static Typeface getSpecificTypeface(Context context,
                                               AppCMSPresenter appCMSPresenter,
                                               String fontWeight) {
        Component component = new Component();
        component.setFontWeight(fontWeight);
        return getTypeFace(context, appCMSPresenter, component);
    }


    public static String getTextColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, android.R.color.white)));
        //Log.d("Utils.java" , "getTextColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getTextColor();
        }
        return color;
    }


    public static String getTitleColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, android.R.color.white)));
        //Log.d("Utils.java" , "getTitleColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getPageTitleColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getPageTitleColor();
        }
        return color;
    }

    public static String getTitleColorForST(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, android.R.color.white)));
        //Log.d("Utils.java" , "getTitleColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBlockTitleColor();
        }
        return color;
    }

    public static String getBackGroundColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.dialog_bg_color)));
        //Log.d("Utils.java" , "getBackGroundColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBackgroundColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getGeneral().getBackgroundColor();
        }
        return color;
    }

    public static String getFocusColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        //Log.d("Utils.java" , "getFocusColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor();
        }
        return color;
    }

    public static String getPrimaryHoverColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        //Log.d("Utils.java" , "getFocusColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimaryHover()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimaryHover().getBackgroundColor();
        }
        return color;
    }

    public static String getPrimaryHoverBorderColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        //Log.d("Utils.java" , "getFocusColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimaryHover()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimaryHover().getBorder()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimaryHover().getBorder().getColor();
        }
        return color;
    }

    public static String getSecondaryHoverColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        //Log.d("Utils.java" , "getFocusColor = "+color);
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondaryHover()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondaryHover().getBackgroundColor();
        }
        return color;
    }

    public static double getPercentage(long runtime, long watchedTime) {
        double percentage = 0;
        percentage = ((double) watchedTime / (double) runtime) * 100;
        return percentage;
    }


    /**
     * Used to open the Season switch dialog.
     *
     * @param appCMSSwitchSeasonBinder data
     */
    public static void showSwitchSeasonsDialog(AppCMSSwitchSeasonBinder appCMSSwitchSeasonBinder,
                                               AppCMSPresenter appCMSPresenter) {
        FragmentTransaction ft =
                appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        SwitchSeasonsDialogFragment switchSeasonsDialogFragment =
                SwitchSeasonsDialogFragment.newInstance(appCMSSwitchSeasonBinder);
        switchSeasonsDialogFragment.show(ft, DIALOG_FRAGMENT_TAG);

    }

    @NonNull
    public static ClearDialogFragment getClearDialogFragment(Context context,
                                                             AppCMSPresenter appCMSPresenter,
                                                             int dialogWidth,
                                                             int dialogHeight,
                                                             String dialogTitle,
                                                             String dialogMessage,
                                                             String positiveButtonText,
                                                             String negativeButtonText,float messageSize) {
        Bundle bundle = new Bundle();
        bundle.putInt(ClearDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(ClearDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(ClearDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY, Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(ClearDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(ClearDialogFragment.DIALOG_POSITIVE_BUTTON_TEXT_KEY,
                positiveButtonText);
        bundle.putString(ClearDialogFragment.DIALOG_NEGATIVE_BUTTON_TEXT_KEY,
                negativeButtonText);
//        bundle.putString(ClearDialogFragment.DIALOG_NEUTRAL_BUTTON_TEXT_KEY,
//                neutralButtonText);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        ClearDialogFragment newFragment = ClearDialogFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }

    @NonNull
    public static PreviewDialogFragment getPreviewDialogFragment(Context context,
                                                                 AppCMSPresenter appCMSPresenter,
                                                                 int dialogWidth,
                                                                 int dialogHeight,
                                                                 String dialogTitle,
                                                                 String dialogMessage,
                                                                 String positiveButtonText,
                                                                 String negativeButtonText,
                                                                 String neutralButtonText,
                                                                 String extraButtonText, float messageSize) {
        Bundle bundle = new Bundle();
        bundle.putInt(PreviewDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(PreviewDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(PreviewDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(PreviewDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY,
                Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(PreviewDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(PreviewDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(PreviewDialogFragment.DIALOG_POSITIVE_BUTTON_TEXT_KEY,
                positiveButtonText);
        bundle.putString(PreviewDialogFragment.DIALOG_NEGATIVE_BUTTON_TEXT_KEY,
                negativeButtonText);
        bundle.putString(PreviewDialogFragment.DIALOG_NEUTRAL_BUTTON_TEXT_KEY,
                neutralButtonText);
        bundle.putString(PreviewDialogFragment.DIALOG_EXTRA_BUTTON_TEXT_KEY,
                extraButtonText);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft =
                appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        PreviewDialogFragment newFragment =
                PreviewDialogFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }

    @NonNull
    public static AccountDetailsEditDialogFragment getAccountEditDialogFragment(Context context,
                                                                                AppCMSPresenter appCMSPresenter,
                                                                                int dialogWidth,
                                                                                int dialogHeight,
                                                                                String dialogTitle,
                                                                                String dialogMessage,
                                                                                String userEmail,
                                                                                String positiveButtonText,
                                                                                String negativeButtonText,
                                                                                String neutralButtonText,
                                                                                float messageSize,
                                                                                String backgroundColor, String dialogType) {
        Bundle bundle = new Bundle();
        bundle.putInt(AccountDetailsEditDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(AccountDetailsEditDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(AccountDetailsEditDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY,
                Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_USER_EMAIL_KEY, userEmail);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_POSITIVE_BUTTON_TEXT_KEY,
                positiveButtonText);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_NEGATIVE_BUTTON_TEXT_KEY,
                negativeButtonText);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_NEUTRAL_BUTTON_TEXT_KEY,
                neutralButtonText);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_BACKGROUND_COLOR,
                backgroundColor);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_TYPE_TEXT_KEY,
                dialogType);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft =
                appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        AccountDetailsEditDialogFragment newFragment =
                AccountDetailsEditDialogFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }

    @NonNull
    public static TVODPurchaseDialogFragment getTVODDialogFragment(Context context,
                                                                   AppCMSPresenter appCMSPresenter,
                                                                   int dialogWidth,
                                                                   int dialogHeight,
                                                                   String dialogTitle,
                                                                   String dialogMessage,
                                                                   float messageSize,
                                                                   String dialogType) {
        Bundle bundle = new Bundle();
        bundle.putInt(TVODPurchaseDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(TVODPurchaseDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(TVODPurchaseDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(TVODPurchaseDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY,
                Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(TVODPurchaseDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(TVODPurchaseDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(AccountDetailsEditDialogFragment.DIALOG_TYPE_TEXT_KEY,
                dialogType);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft =
                appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        TVODPurchaseDialogFragment newFragment =
                TVODPurchaseDialogFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }


    @NonNull
    public static ClearDialogFragment getLanguageConfirmationDialog(Context context,
                                                                    AppCMSPresenter appCMSPresenter,
                                                                    int dialogWidth,
                                                                    int dialogHeight,
                                                                    String dialogTitle,
                                                                    String dialogMessage,
                                                                    String positiveButtonText,
                                                                    String negativeButtonText,
                                                                    float messageSize) {
        Bundle bundle = new Bundle();
        bundle.putInt(ClearDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(ClearDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(ClearDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY,
                Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(ClearDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(ClearDialogFragment.DIALOG_POSITIVE_BUTTON_TEXT_KEY,
                positiveButtonText);
        bundle.putString(ClearDialogFragment.DIALOG_NEGATIVE_BUTTON_TEXT_KEY,
                negativeButtonText);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft =
                appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        ClearDialogFragment newFragment =
                ClearDialogFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }


    public static void pageLoading(final boolean shouldShowProgress, Activity activity) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (shouldShowProgress) {
                    CustomProgressBar.getInstance(activity).showProgressDialog(activity, "Loading...");
                } else {
                    CustomProgressBar.getInstance(activity).dismissProgressDialog();
                }
            }
        });
    }


    public static String convertSecondsToTime(long runtime) {
        StringBuilder timeInString = new StringBuilder();
        runtime = runtime * 1000;

        long days = TimeUnit.MILLISECONDS.toDays(runtime);
        runtime -= TimeUnit.DAYS.toMillis(days);
        if (days != 0) {
            timeInString.append(Long.toString(days));
        }

        long hours = TimeUnit.MILLISECONDS.toHours(runtime);
        runtime -= TimeUnit.HOURS.toMillis(hours);
        if (hours != 0 || timeInString.length() > 0) {
            if (timeInString.length() > 0) {
                timeInString.append(":");
            }
            /*if (hours < 10) {
                timeInString.append("0");
            }*/
            timeInString.append(Long.toString(hours));
        } else {
            timeInString.append("0");
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(runtime);
        runtime -= TimeUnit.MINUTES.toMillis(minutes);
//        if (minutes != 0 || timeInString.length() > 0){
        if (timeInString.length() > 0) {
            timeInString.append(":");
        }
        if (minutes < 10) {
            timeInString.append("0");
        }
        timeInString.append(Long.toString(minutes));
//        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(runtime);
//        if (seconds != 0 || timeInString.length() > 0){
        if (timeInString.length() > 0) {
            timeInString.append(":");
        }
        if (seconds < 10) {
            timeInString.append("0");
        }
        timeInString.append(Long.toString(seconds));
//        }
        return timeInString.toString();
    }

    public static List<String> getRelatedVideosInShow(List<Season_> season, int showNumber, int episodeNumber) {
        List<String> relatedVids = new ArrayList<>();
        for (int i = showNumber; i < season.size(); i++) {
            if (i == showNumber) {
                for (int j = episodeNumber + 1; j < season.get(i).getEpisodes().size(); j++) {
                    if (season.get(i) != null && season.get(i).getEpisodes() != null
                            && season.get(i).getEpisodes().get(j) != null
                            && season.get(i).getEpisodes().get(j).getGist() != null
                            && season.get(i).getEpisodes().get(j).getGist().getId() != null) {
                        relatedVids.add(season.get(i).getEpisodes().get(j).getGist().getId());
                    }
                }
            } else {
                for (int j = 0; j < season.get(i).getEpisodes().size(); j++) {
                    if (season.get(i) != null && season.get(i).getEpisodes() != null
                            && season.get(i).getEpisodes().get(j) != null
                            && season.get(i).getEpisodes().get(j).getGist() != null
                            && season.get(i).getEpisodes().get(j).getGist().getId() != null) {
                        relatedVids.add(season.get(i).getEpisodes().get(j).getGist().getId());
                    }
                }
            }
        }
        return relatedVids;
    }


    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Returns the complimentary (opposite) color.
     *
     * @param color int RGB color to return the compliment of
     * @return int RGB of compliment color
     */
    public static int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }

    public static int getIcon(String icon , Context context,AppCMSPresenter appCMSPresenter) {
        int iconResId = 0;
        if (null != icon) {
            if (icon.equalsIgnoreCase(context.getString(R.string.st_autoplay_icon_key))) {
                iconResId = R.drawable.st_settings_icon_autoplay;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_closed_caption_icon_key))) {
                iconResId = R.drawable.st_settings_icon_cc;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_manage_subscription_icon_key))) {
                iconResId = R.drawable.st_settings_icon_manage_subscription;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_account_icon_key))
                    || icon.equalsIgnoreCase(context.getString(R.string.st_user_icon_key))) {
                if (appCMSPresenter.isNewsTemplate()) {
                    iconResId = R.drawable.icon_news_gear;
                } else {
                    iconResId = R.drawable.st_settings_icon_account;
                }
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_faq_icon_key))) {
                iconResId = R.drawable.st_settings_icon_faq;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_contact_icon_key))) {
                iconResId = R.drawable.st_settings_icon_contact;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_signin_icon_key))) {
                iconResId = R.drawable.st_settings_icon_signin;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_signout_icon_key))) {
                iconResId = R.drawable.st_settings_icon_signout;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_about_us_icon_key))) {
                iconResId = R.drawable.st_settings_icon_about_us;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_privacy_policy_icon_key))
                    || icon.equalsIgnoreCase(context.getString(R.string.st_privacy_policy_icon_key1))) {
                iconResId = R.drawable.st_setting_icon_privacy_policy;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_home_icon_key))) {
                iconResId = R.drawable.st_menu_icon_home;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_tos_icon_key))) {
                iconResId = R.drawable.icon_tos;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_show_icon_key))) {
                iconResId = R.drawable.st_menu_icon_grid;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_teams_icon_key))) {
                iconResId = R.drawable.st_menu_icon_bracket;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_tick_icon_key))) {
                iconResId = R.drawable.st_menu_icon_tick;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_watchlist_icon_key))) {
                iconResId = R.drawable.st_menu_icon_watchlist;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_history_icon_key))) {
                iconResId = R.drawable.st_menu_icon_clock;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_settings_icon_key))) {
                iconResId = R.drawable.st_menu_icon_gear;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_search_icon_key))) {
                if (appCMSPresenter.isNewsTemplate()) {
                    iconResId = R.drawable.icon_news_search;
                } else {
                    iconResId = R.drawable.st_menu_icon_search;
                }
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_live_icon_key))) {
                iconResId = R.drawable.st_menu_icon_live;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_signup_icon_key))) {
                iconResId = R.drawable.icon_signup;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_menu_icon_key))) {
                iconResId = R.drawable.tv_icon_menu;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_movies_icon_key))) {
                iconResId = R.drawable.st_menu_icon_movies;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_language_icon_key))) {
                iconResId = R.drawable.st_menu_icon_language;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.news_live_icon_key))) {
                iconResId = R.drawable.icon_news_home;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.news_saved_icon_key))) {
                iconResId = R.drawable.icon_news_saved;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.news_shows_icon_key))) {
                iconResId = R.drawable.icon_news_shows;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.news_gear_icon_key))) {
                iconResId = R.drawable.icon_news_gear;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.news_user_login_icon_key))) {
                iconResId = R.drawable.icon_news_login;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_genre_icon_key))) {
                iconResId = R.drawable.icon_genre;
            } else if (icon.equalsIgnoreCase(context.getString(R.string.st_generees_icon_key))) {
                iconResId = R.drawable.icon_genre;
            }
        }
        return iconResId;
    }

    public static float convertDpToPixel(float dp, Context context) {
        if (context != null) {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        }
        return 0.0f;
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


    public static void broadcastCapabilities(Context context) {

        boolean userIsSignedIn = false;
       /* AppCMSPresenter appCMSPresenter = ((AppCMSApplication) ((Activity) context).getApplication()).getAppCMSPresenterComponent().appCMSPresenter();
        if (appCMSPresenter != null) {
            userIsSignedIn = appCMSPresenter.isUserLoggedIn();
        }*/
        Intent intent = new Intent();
        intent.setPackage("com.amazon.tv.launcher");
        intent.setAction("com.amazon.device.CAPABILITIES");
//        if (userIsSignedIn) {
        intent.putExtra("amazon.intent.extra.PLAY_INTENT_ACTION", "air.com.snagfilms.PLAY");
        intent.putExtra("amazon.intent.extra.PLAY_INTENT_PACKAGE", "air.com.snagfilms");
        intent.putExtra("amazon.intent.extra.PLAY_INTENT_CLASS", "com.viewlift.tv.AppCmsTVSplashActivity");
//            intent.putExtra("amazon.intent.extra.PLAY_INTENT_FLAGS", Intent.FLAG_ACTIVITY_NEW_TASK);
        /*} else {
            intent.putExtra("amazon.intent.extra.SIGNIN_INTENT_ACTION", "android.intent.action.VIEW");
            intent.putExtra("amazon.intent.extra.PLAY_INTENT_PACKAGE", "air.com.snagfilms");
            intent.putExtra("amazon.intent.extra.PLAY_INTENT_CLASS", "com.viewlift.tv.views.activity.AppCMSTVPlayVideoActivity");
            intent.putExtra("amazon.intent.extra.SIGNIN_INTENT_FLAGS", Intent.FLAG_ACTIVITY_NEW_TASK);
        }*/
        intent.putExtra("amazon.intent.extra.PARTNER_ID", "SNAEI_SF");

        //Send the intent to the Launcher
        context.sendBroadcast(intent);
    }

    public static String calculateTimeDiff(Date otherDate) {

        Date d1 = new Date();

        long diff = otherDate.getTime() - d1.getTime();
        /*long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        int diffInDays = (int) diff / (1000 * 60 * 60 * 24);*/

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        return String.format(Locale.getDefault(), "%02d", elapsedDays)
                + ":" + String.format(Locale.getDefault(), "%02d", elapsedHours)
                + ":" + String.format(Locale.getDefault(), "%02d", elapsedMinutes)
                + ":" + String.format(Locale.getDefault(), "%02d", elapsedSeconds);

        /*if (diffInDays > 0)
            return diffInDays + " Days Remaining";
        else if (diffHours > 0)
            return diffHours + " Hours Remaining";
        else if (diffMinutes > 0)
            return diffMinutes + " Minutes Remaining";
        else
            return diffSeconds + " Seconds Remaining";*/
    }

    public static String formatTimeAndDate(long diff) {

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = diff / daysInMilli;
        diff = diff % daysInMilli;

        long elapsedHours = diff / hoursInMilli;
        diff = diff % hoursInMilli;

        long elapsedMinutes = diff / minutesInMilli;
        diff = diff % minutesInMilli;

        long elapsedSeconds = diff / secondsInMilli;

        return String.format(Locale.getDefault(), "%02d", elapsedDays)
                + ":" + String.format(Locale.getDefault(), "%02d", elapsedHours)
                + ":" + String.format(Locale.getDefault(), "%02d", elapsedMinutes)
                + ":" + String.format(Locale.getDefault(), "%02d", elapsedSeconds);

        /*if (diffInDays > 0)
            return diffInDays + " Days Remaining";
        else if (diffHours > 0)
            return diffHours + " Hours Remaining";
        else if (diffMinutes > 0)
            return diffMinutes + " Minutes Remaining";
        else
            return diffSeconds + " Seconds Remaining";*/
    }

    @NonNull
    public static ClearDialogFragment getExitDialogFragment(Context context,
                                                            AppCMSPresenter appCMSPresenter,
                                                            int dialogWidth,
                                                            int dialogHeight,
                                                            String dialogTitle,
                                                            String dialogMessage,
                                                            String positiveButtonText,
                                                            float messageSize) {
        Bundle bundle = new Bundle();
        bundle.putInt(ClearDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(ClearDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(ClearDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY,
                Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(ClearDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(ClearDialogFragment.DIALOG_POSITIVE_BUTTON_TEXT_KEY,
                positiveButtonText);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft =
                appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        ClearDialogFragment newFragment =
                ClearDialogFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }

    public static boolean isEmailValid(String email) {
        return (!TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

    }

    public static StateListDrawable setButtonBackgroundSelectorForNewsTemplate(Context context,
                                                                               String focusStateBorderColor,
                                                                               String unFocusStateBorderColor,
                                                                               Component component,
                                                                               AppCMSPresenter appCMSPresenter) {

        String borderWidth = null;
        if (null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            borderWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth();
            if (null != borderWidth) {
                if (borderWidth.contains("px")) {
                    String[] bdWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth().split("px");
                    borderWidth = bdWidth[0];
                }
            }
        }

        StateListDrawable res = new StateListDrawable();
        res.addState(new int[]{android.R.attr.state_focused}, getButtonNormalState(context, appCMSPresenter, component, focusStateBorderColor, borderWidth));
        res.addState(new int[]{android.R.attr.state_pressed}, getButtonDefaultState(context, appCMSPresenter, unFocusStateBorderColor, borderWidth,component != null ? component.getCornerRadius():0));
        res.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(Color.parseColor(unFocusStateBorderColor)));
        return res;
    }

    public static GradientDrawable getNewsNavigationSelectedState(Context context, AppCMSPresenter appCMSPresenter, int selectorColor) {
        GradientDrawable ageBorder = new GradientDrawable();
        ageBorder.setShape(GradientDrawable.RECTANGLE);
        ageBorder.setStroke(3, selectorColor);
        return ageBorder;
    }

    public static StateListDrawable getNewsTemplateNavigationSelector(Context context, AppCMSPresenter appCMSPresenter) {
        StateListDrawable res = new StateListDrawable();
        String borderWidth = null, selectedColor = null;
        if (null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            selectedColor = getFocusBorderColor(context, appCMSPresenter);
            borderWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth();
            if (null != borderWidth) {
                if (borderWidth.contains("px")) {
                    String[] bdWidth = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBorder().getWidth().split("px");
                    borderWidth = bdWidth[0];
                }
            }
        }
        res.addState(new int[]{android.R.attr.state_focused}, getButtonDefaultState(context, appCMSPresenter, selectedColor, borderWidth,0));
        res.addState(new int[]{android.R.attr.state_pressed}, getButtonDefaultState(context, appCMSPresenter, selectedColor, borderWidth,0));
        res.addState(new int[]{android.R.attr.state_selected}, getButtonDefaultState(context, appCMSPresenter, selectedColor, borderWidth,0));
        res.addState(new int[]{}, new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
        return res;
    }

    public static String getFocusBorderColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBorder().getColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBorder().getColor();
        }
        return color;
    }

    public static String getPrimaryTextColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor();
        }
        return color;
    }

    public static String getSecondaryTextColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getTextColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getTextColor();
        }
        return color;
    }

    public static String getSecondaryBackgroundColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBackgroundColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getBackgroundColor();
        }
        return color;
    }

    public static void setSpanColor(TextView view, String fulltext, String subtext, int color) {
        try {
            view.setText(fulltext, TextView.BufferType.SPANNABLE);
            Spannable str = (Spannable) view.getText();
            int i = fulltext.indexOf(subtext);
            str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {

        }
    }

    public static String getProgressBarColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        //Log.d("Utils.java" , "getFocusColor = "+color);
        if ((null != appCMSPresenter) && (null != appCMSPresenter.getAppCMSMain())
                && (null != appCMSPresenter.getAppCMSMain().getBrand())
                && (null != appCMSPresenter.getAppCMSMain().getBrand().getPlayer())
                && (null != appCMSPresenter.getAppCMSMain().getBrand().getPlayer().getProgressBarColor())) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getPlayer().getProgressBarColor();
        }
        return color;
    }

    public static void startAnimation(View view, Context context, AnimationType animationType) {
        switch (animationType) {
            case LEFT:
                animLeftSlide = AnimationUtils.loadAnimation(context, R.anim.slide_left);
                break;

            case RIGHT:
                animLeftSlide = AnimationUtils.loadAnimation(context, R.anim.slide_right);
                break;

            case BOTTOM:
                animLeftSlide = AnimationUtils.loadAnimation(context, R.anim.mini_player_slide_bottom);
                break;
            case FADE_IN:
                animLeftSlide = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                break;

            case FADE_OUT:
                animLeftSlide = AnimationUtils.loadAnimation(context, R.anim.fade_out);
                break;
        }
        view.startAnimation(animLeftSlide);
    }

    public static void setToggleViewStyle(View view, View liveSeparatorView, Context mContext, AppCMSPresenter appCMSPresenter, TextView aSwitch) {
        liveSeparatorView.setBackgroundColor(Color.parseColor(getFocusBorderColor(mContext, appCMSPresenter)));
        aSwitch.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparentColor));
        aSwitch.setTypeface(Utils.getSpecificTypeface(
                mContext,
                appCMSPresenter,
                mContext.getString(R.string.app_cms_page_font_regular_key)));
        aSwitch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                view.setVisibility(View.VISIBLE);
                liveSeparatorView.setVisibility(View.VISIBLE);
                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.left_navigation_unFocused_color));
            } else {
                view.setVisibility(View.GONE);
                liveSeparatorView.setVisibility(View.GONE);
            }
        });

    }

    public static String getNavigationBackgroundColor(Context context, AppCMSPresenter appCMSPresenter) {
        String color = getColor(context, Integer.toHexString(ContextCompat.getColor(context, R.color.colorAccent)));
        if (null != appCMSPresenter && null != appCMSPresenter.getAppCMSMain()
                && null != appCMSPresenter.getAppCMSMain().getBrand()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getNavigation()
                && null != appCMSPresenter.getAppCMSMain().getBrand().getNavigation().getBackgroundColor()) {
            color = appCMSPresenter.getAppCMSMain().getBrand().getNavigation().getBackgroundColor();
        }
        return color;
    }

    public static void setImageColorFilter(@NonNull ImageView button, Drawable drawable, AppCMSPresenter appCMSPresenter) {
        String focusStateTextColor = null;
        String unFocusStateTextColor = null;
        if (null != appCMSPresenter &&
                null != appCMSPresenter.getAppCMSMain() &&
                null != appCMSPresenter.getAppCMSMain().getBrand() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta() &&
                null != appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary()) {
            if (appCMSPresenter.isNewsTemplate()) {
                focusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor();
                unFocusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getSecondary().getTextColor();
            } else {
                focusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getBackgroundColor();
                unFocusStateTextColor = appCMSPresenter.getAppCMSMain().getBrand().getCta().getPrimary().getTextColor();
            }
        }
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };
        int[] colors = new int[]{
                Color.parseColor(focusStateTextColor),
                Color.parseColor(focusStateTextColor),
                Color.parseColor(focusStateTextColor),
                Color.parseColor(unFocusStateTextColor)
        };
        ColorStateList myList = new ColorStateList(states, colors);

        if (button != null) {
            drawable = DrawableCompat.wrap(button.getDrawable());
            DrawableCompat.setTintList(drawable, myList);
            button.setImageDrawable(drawable);
        } else {
            DrawableCompat.setTintList(drawable, myList);
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
                return (int) height;
            default:
                return 0;
        }
    }

    public static void setCustomTVVideoPlayerView(CustomTVVideoPlayerView customVideoPlayerView) {
        customTVVideoPlayerView = customVideoPlayerView;
    }

    public static CustomTVVideoPlayerView getCustomTVVideoPlayerView() {
        return customTVVideoPlayerView;
    }

    @NonNull
    public static RecommendationFragment getRecommendationFragment(Context context,
                                                                   AppCMSPresenter appCMSPresenter,
                                                                   int dialogWidth,
                                                                   int dialogHeight,
                                                                   String dialogTitle,
                                                                   String dialogMessage,
                                                                   String positiveButtonText,
                                                                   String negativeButtonText,
                                                                   float messageSize,
                                                                   boolean isUserSettingPage) {
        Bundle bundle = new Bundle();
        bundle.putInt(ClearDialogFragment.DIALOG_WIDTH_KEY, dialogWidth);
        bundle.putInt(ClearDialogFragment.DIALOG_HEIGHT_KEY, dialogHeight);
        bundle.putFloat(ClearDialogFragment.DIALOG_MESSAGE__SIZE_KEY, messageSize);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_TEXT_COLOR_KEY,
                com.viewlift.tv.utility.Utils.getTextColor(context, appCMSPresenter));
        bundle.putString(ClearDialogFragment.DIALOG_TITLE_KEY, dialogTitle);
        bundle.putString(ClearDialogFragment.DIALOG_MESSAGE_KEY, dialogMessage);
        bundle.putString(ClearDialogFragment.DIALOG_POSITIVE_BUTTON_TEXT_KEY,
                positiveButtonText);
        bundle.putString(ClearDialogFragment.DIALOG_NEGATIVE_BUTTON_TEXT_KEY,
                negativeButtonText);
        bundle.putBoolean(RecommendationFragment.OPEN_DIALOG_FROM_SETTING_PAGE,
                isUserSettingPage);
        Intent args = new Intent(AppCMSPresenter.PRESENTER_DIALOG_ACTION);
        args.putExtra(context.getString(R.string.dialog_item_key), bundle);
        FragmentTransaction ft = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
        RecommendationFragment newFragment = RecommendationFragment.newInstance(bundle);
        newFragment.show(ft, DIALOG_FRAGMENT_TAG);
        return newFragment;
    }

    public static int findIndexOfStringFromList(List<LoginSignup> list, String title) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i)).getTitle().equalsIgnoreCase(title)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int findIndexFromList(List<String> list, String title) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i)).equalsIgnoreCase(title)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static GradientDrawable setBackgroundWithRoundCorner(AppCMSPresenter appCMSPresenter,
                                                                int selectorColor, int strokeColor,
                                                                int strokeWidth, int cornerRadius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(selectorColor);
        shape.setStroke(strokeWidth, strokeColor);
        shape.setCornerRadius(cornerRadius);
        return shape;
    }

    public static void setTextViewColor(AppCMSPresenter appCMSPresenter, boolean isVisible, TextView view) {
        view.setTextColor(isVisible ? appCMSPresenter.getBrandPrimaryCtaTextColor() : appCMSPresenter.getBrandSecondaryCtaTextColor());
    }

    public static boolean getContentType(Context context, ContentDatum contentDatum, String contentType) {
        if (contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_video))) {
            return true;
        } else if (contentDatum != null && contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && (contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_series))
                || contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_season))
                || contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_show)))) {
            return true;
        } else if (contentDatum != null && contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_bundle))) {
            return true;
        }
        return false;
    }

    public static boolean isContentTypeVideo(Context context, ContentDatum contentDatum) {
        if (contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_video))) {
            return true;
        }
        return false;
    }

    public static boolean isContentTypeSeries(Context context, ContentDatum contentDatum) {
        if (contentDatum != null && contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && (contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_series))
                || contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_season))
                || contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_show)))) {
            return true;
        }
        return false;
    }

    public static boolean isContentTypeBundle(Context context, ContentDatum contentDatum) {
        if (contentDatum != null && contentDatum.getGist() != null
                && contentDatum.getGist().getContentType() != null
                && contentDatum.getGist().getContentType().equalsIgnoreCase(context.getString(R.string.content_type_bundle))) {
            return true;
        }
        return false;
    }

}

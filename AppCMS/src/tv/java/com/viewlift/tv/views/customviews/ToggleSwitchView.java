package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.Resources;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.activity.AppCmsHomeActivity;
import com.viewlift.utils.CommonUtils;

import java.util.Map;

/**
 * Created by anas.azeem on 9/4/2017.
 * Owned by ViewLift, NYC
 */

public class ToggleSwitchView extends TVBaseView {
    private final MetadataMap metadataMap;
    private final String viewType;
    private Context mContext;
    private Component mComponent;
    private int DEFAULT_HEIGHT = LayoutParams.WRAP_CONTENT;
    private int DEFAULT_WIDTH = LayoutParams.MATCH_PARENT;
    private Map<String, AppCMSUIKeyType> mJsonValueKeyMap;
    private TextView textView;
    private ImageView imageView;
    boolean isEnabled = false;
    AppCMSPresenter appCMSPresenter;

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public ToggleSwitchView(Context context,
                            Component component,
                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                            AppCMSPresenter appCMSPresenter,
                            MetadataMap metadataMap,
                            String viewType) {
        super(context);
        mContext = context;
        mComponent = component;
        mJsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.metadataMap = metadataMap;
        this.viewType = viewType;
        /*isEnabled = ((AppCMSApplication) mContext.getApplicationContext()).
                getAppCMSPresenterComponent().appCMSPresenter()
                .getAutoplayEnabledUserPref(mContext);*/
        init();
        initComponent();
        setFocusable(false);
    }

    @Override
    public void init() {
        float viewHeight = Utils.getViewHeight(mContext, mComponent.getLayout(), DEFAULT_HEIGHT);
        float viewWidth = Utils.getViewWidth(mContext, mComponent.getLayout(), DEFAULT_WIDTH);
        LayoutParams layoutParams = new LayoutParams((int) viewWidth, (int) viewHeight);

        layoutParams.gravity = Gravity.CENTER_VERTICAL;

        if(mComponent.getLayout().getTv().getLeftMargin() != null && mComponent.getLayout().getTv().getTopMargin() != null)
        layoutParams.setMargins(Integer.parseInt(mComponent.getLayout().getTv().getLeftMargin()),Integer.parseInt(mComponent.getLayout().getTv().getTopMargin()),0,0);
        setLayoutParams(layoutParams);
        if(mComponent.getBackgroundColor() != null) {
            setBackground(Utils.setBackgroundWithRoundCorner(appCMSPresenter, Color.parseColor(Utils.getSecondaryBackgroundColor(mContext,appCMSPresenter)),0,0, mComponent.getCornerRadius()));
        }
    }

    private void initComponent() {
        if (null != mComponent) {
            if(!((mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_LIVE_TOGGLE_SWITCH_KEY ||
                    mJsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY3))) {
                addView(createTextView(mComponent));
            }
            addView(createToggleView(mComponent));
        }
    }


    private View createToggleView(Component mComponent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View settingsToggleView = inflater.inflate(R.layout.settings_switch, null);
        Switch aSwitch = settingsToggleView.findViewById(R.id.setting_switch);

        if(mJsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY3) {
            aSwitch.setTrackResource(R.drawable.track_selector);
            aSwitch.setThumbResource(R.drawable.thumb_selector);
            aSwitch.setSwitchPadding(20);
        }
        int switchOnColor = appCMSPresenter.getBrandPrimaryCtaTextColor();
        int switchOffColor = appCMSPresenter.getBrandSecondaryCtaTextColor();


        setToggleViewStyle(mComponent,aSwitch,switchOnColor,switchOffColor);
        /*setTextViewStyle(mComponent, settingsToggleView.findViewById(R.id.setting_switch_tv_off));
        setTextViewStyle(mComponent, settingsToggleView.findViewById(R.id.setting_switch_tv_on));
        int switchOnColor = this.appCMSPresenter.getGeneralTextColor();
        int switchOffColor = 0x9f000000 +this.appCMSPresenter.getGeneralTextColor();

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                }, new int[]{
                switchOnColor,
                switchOffColor
        });

        aSwitch.getTrackDrawable().setTintList(colorStateList);
        aSwitch.getTrackDrawable().setTint(switchOffColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            aSwitch.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
            aSwitch.setThumbTintList(colorStateList);
        } else {
            aSwitch.setButtonTintList(colorStateList);

        }*/

        aSwitch.getTrackDrawable().setTint(
                appCMSPresenter.getGeneralTextColor());
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                }, new int[]{
                switchOnColor,
                switchOffColor
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            aSwitch.setTrackTintMode(PorterDuff.Mode.MULTIPLY);
            aSwitch.setThumbTintList(colorStateList);
        } else {
            aSwitch.setButtonTintList(colorStateList);
        }

        if (mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_SETTING_AUTOPLAY_TOGGLE_SWITCH_KEY) {
            isEnabled = ((AppCMSApplication) mContext.getApplicationContext()).
                    getAppCMSPresenterComponent().appCMSPresenter()
                    .getAutoplayEnabledUserPref(mContext);

            aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isEnabled = isChecked;
                ((AppCMSApplication) mContext.getApplicationContext()).getAppCMSPresenterComponent()
                        .appCMSPresenter().setAutoplayEnabledUserPref( isEnabled);
                ((AppCMSApplication) mContext.getApplicationContext()).getAppCMSPresenterComponent()
                        .appCMSPresenter().setAutoplayDefaultValueChangedPref(true);
                setToggleViewStyle(mComponent,aSwitch,switchOnColor,switchOffColor);
            });
        } else if (mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_SETTING_CLOSED_CAPTION_TOGGLE_SWITCH_KEY) {
            isEnabled = ((AppCMSApplication) mContext.getApplicationContext()).
                    getAppCMSPresenterComponent().appCMSPresenter()
                    .getClosedCaptionPreference();

            aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isEnabled = isChecked;
                ((AppCMSApplication) mContext.getApplicationContext()).getAppCMSPresenterComponent()
                        .appCMSPresenter().setClosedCaptionPreference(isEnabled);
                setToggleViewStyle(mComponent,aSwitch,switchOnColor,switchOffColor);
                if( Utils.getCustomTVVideoPlayerView() != null)
                    Utils.getCustomTVVideoPlayerView().setSubtitleViewVisibility(isChecked);
            });
        } else if (mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_LIVE_TOGGLE_SWITCH_KEY) {
            isEnabled = ((AppCMSApplication) mContext.getApplicationContext()).
                    getAppCMSPresenterComponent().appCMSPresenter()
                    .getLivePlayerPreference();

            aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isEnabled = isChecked;
                ((AppCMSApplication) mContext.getApplicationContext()).getAppCMSPresenterComponent()
                        .appCMSPresenter().setLivePlayerPreference(isEnabled);
                setToggleViewStyle(mComponent,aSwitch,switchOnColor,switchOffColor);
            });
        }
        aSwitch.setChecked(isEnabled);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        settingsToggleView.setLayoutParams(params);
        settingsToggleView.setPadding(mComponent.getLeftPadding(),mComponent.getTopPadding(),mComponent.getRightPadding(),mComponent.getBottomPadding());

        return settingsToggleView;
    }
    private void setBorder(View itemView,
                           int color) {
        GradientDrawable planBorder = new GradientDrawable();
        planBorder.setShape(GradientDrawable.RECTANGLE);
        planBorder.setCornerRadius(mComponent.getCornerRadius());
        planBorder.setStroke(2, color);
        planBorder.setColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
        itemView.setBackground(planBorder);
    }

    private View createTextView(Component component) {
        TextView componentView = new TextView(mContext);
        setTextViewStyle(component, componentView);

        if (metadataMap != null) {
            if (mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_SETTING_AUTOPLAY_TOGGLE_SWITCH_KEY) {
                if (metadataMap.getAutoplayLabel() != null) {
                    componentView.setText(metadataMap.getAutoplayLabel());
                } else {
                    ((TextView) componentView).setText(component.getText());
                    Resources resources = appCMSPresenter.getLanguageResourcesFile();
                    if(null != resources) {
                        String text = resources.getUIresource(component.getText());
                        componentView.setText(text);
                    }

                }
            } else if (mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_SETTING_CLOSED_CAPTION_TOGGLE_SWITCH_KEY) {
                if (metadataMap.getCloseCaptioningLabel() != null) {
                    componentView.setText(metadataMap.getCloseCaptioningLabel());
                } else {
                    ((TextView) componentView).setText(component.getText());
                    Resources resources = appCMSPresenter.getLanguageResourcesFile();
                    if(null != resources) {
                        String text = resources.getUIresource(component.getText());
                        componentView.setText(text);
                    }
                }
            }
        } else {
            componentView.setText(component.getText());
            Resources resources = appCMSPresenter.getLanguageResourcesFile();
            if(null != resources){
                String text = resources.getUIresource(component.getText());
                if(null != text){
                    componentView.setText(text);
                }
            }
        }
        componentView.setGravity(Gravity.CENTER_VERTICAL);

        textView = componentView;
        textView.setPadding(component.getLeftPadding(),component.getTopPadding(),component.getRightPadding(),component.getBottomPadding());
        return componentView;
    }

    private void setTextViewStyle(Component component, TextView componentView) {
        int textColor = ContextCompat.getColor(mContext, R.color.colorAccent);
        if (!TextUtils.isEmpty(component.getTextColor())) {
            textColor = Color.parseColor(CommonUtils.getColor(mContext, component.getTextColor()));
        } else if (component.getStyles() != null) {
            if (!TextUtils.isEmpty(component.getStyles().getColor())) {
                textColor = Color.parseColor(CommonUtils.getColor(mContext, component.getStyles().getColor()));
            } else if (!TextUtils.isEmpty(component.getStyles().getTextColor())) {
                textColor = Color.parseColor(CommonUtils.getColor(mContext, component.getStyles().getTextColor()));
            }
        }

        String txtColor = appCMSPresenter.getAppTextColor();
        if(txtColor != null){
            textColor = Color.parseColor(txtColor);
        }

        componentView.setTextColor(textColor);
        Typeface typeface = Utils.getTypeFace(mContext, appCMSPresenter, component);
        if (null != typeface) {
            componentView.setTypeface(typeface);
        }
        if (component.getLayout().getTv().getFontSize() != 0) {
            componentView.setTextSize(component.getLayout().getTv().getFontSize());
        }
    }

    private void setToggleViewStyle(Component component,TextView aSwitch, int switchOnColor, int switchOfColor){
        if(component != null && (mJsonValueKeyMap.get(mComponent.getKey()) == AppCMSUIKeyType.PAGE_LIVE_TOGGLE_SWITCH_KEY ||
                mJsonValueKeyMap.get(viewType) == AppCMSUIKeyType.PAGE_USER_MANAGEMENT_MODULE_KEY3)) {
            aSwitch.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparentColor));

            aSwitch.setText(component.getText());
            Typeface typeface = Utils.getTypeFace(mContext, appCMSPresenter, component);
            if (null != typeface) {
                aSwitch.setTypeface(typeface);
            }
            if (component.getLayout().getTv().getFontSize() != 0) {
                aSwitch.setTextSize(component.getLayout().getTv().getFontSize());
            }

            if (isEnabled) {
                aSwitch.setTextColor(switchOnColor);
            } else {
                aSwitch.setTextColor(switchOfColor);
            }

            aSwitch.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    setBorder(this, switchOnColor);
                } else {
                    setBorder(this, Color.TRANSPARENT);
                }
                ((AppCmsHomeActivity) mContext).shouldShowLeftNavigation(true);
            });
        }

    }

    @Override
    protected Component getChildComponent(int index) {
        return null;
    }

    @Override
    protected Layout getLayout() {
        return null;
    }
}

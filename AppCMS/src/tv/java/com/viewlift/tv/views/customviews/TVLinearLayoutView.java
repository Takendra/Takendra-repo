package com.viewlift.tv.views.customviews;

import android.content.Context;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.models.data.appcms.ui.tv.FireTV;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.OnInternalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_HORIZONTAL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY;
import static com.viewlift.models.data.appcms.ui.AppCMSUIKeyType.PAGE_TEXTALIGNMENT_RIGHT_KEY;
import static com.viewlift.tv.utility.Utils.getViewHeight;
import static com.viewlift.tv.utility.Utils.getViewWidth;
import static com.viewlift.tv.views.customviews.TVBaseView.DEVICE_HEIGHT;
import static com.viewlift.tv.views.customviews.TVBaseView.DEVICE_WIDTH;
import static com.viewlift.tv.views.customviews.TVBaseView.STANDARD_MOBILE_HEIGHT_PX;
import static com.viewlift.tv.views.customviews.TVBaseView.STANDARD_MOBILE_WIDTH_PX;


public class TVLinearLayoutView extends LinearLayout {
    private final String viewType;
    private Context mContext;
    private Component mComponent;
    private Map<String, AppCMSUIKeyType> mJsonValueKeyMap;
    AppCMSPresenter appCMSPresenter;
    private final TVPageView pageView;
    private Settings settings;
    private boolean leftMarginEnable;
    private final boolean isFromLoginDialog;
    String orientation;

    public TVLinearLayoutView(TVModuleView moduleList,
                              Context context,
                              Component component,
                              Module moduleAPI,
                              TVViewCreator tvViewCreator,
                              Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                              AppCMSPresenter appCMSPresenter,
                              MetadataMap metadataMap,
                              TVPageView pageView,
                              Settings settings,
                              String viewType,
                              boolean isFromLoginDialog) {
        super(context);
        mContext = context;
        mComponent = component;
        mJsonValueKeyMap = jsonValueKeyMap;
        this.appCMSPresenter = appCMSPresenter;
        this.pageView = pageView;
        this.settings = settings;
        this.isFromLoginDialog = isFromLoginDialog;
        if(null != component.getLayout().getTv().getOrientation()) {
            orientation = component.getLayout().getTv().getOrientation();
            if (orientation.equalsIgnoreCase("vertical")) {
                setOrientation(VERTICAL);
            }
        }
        this.viewType = viewType;


        setViewMarginsFromComponent(component, this);
        initComponent(moduleAPI, tvViewCreator);

        setFocusable(false);
    }

    private void initComponent(Module moduleAPI, TVViewCreator tvViewCreator) {
        TVViewCreator.ComponentViewResult componentViewResult =
                tvViewCreator.getComponentViewResult();
        List<OnInternalEvent> onInternalEvents = new ArrayList<>();
        for (Component component : mComponent.getComponents()) {
            tvViewCreator.createComponentView(mContext,
                    component,
                    component.getLayout(),
                    moduleAPI,
                    pageView,
                    settings,
                    mJsonValueKeyMap,
                    appCMSPresenter,
                    false,
                    viewType,
                    isFromLoginDialog);

            if (componentViewResult.onInternalEvent != null) {
                onInternalEvents.add(componentViewResult.onInternalEvent);
            }

            View componentView = componentViewResult.componentView;

            if (componentView != null) {
                setViewMarginsFromComponent(component, componentView);
                addView(componentView);
            }

        }
    }


    public void setViewMarginsFromComponent(Component childComponent, View view) {
        Layout layout = childComponent.getLayout();

        int lm = 0, tm = 0, rm = 0, bm = 0;
        int viewWidth = (int) getViewWidth(getContext(), layout, LinearLayout.LayoutParams.MATCH_PARENT);
        int viewHeight = (int) getViewHeight(getContext(), layout, LayoutParams.MATCH_PARENT);

        int gravity = Gravity.NO_GRAVITY;
        if (viewWidth < 0) {
            viewWidth = LinearLayout.LayoutParams.MATCH_PARENT;
        } if (viewHeight < 0) {
            viewHeight = LinearLayout.LayoutParams.MATCH_PARENT;
        }else if (viewWidth == 0) {
            viewWidth = LayoutParams.WRAP_CONTENT;
        }else if (viewHeight == 0) {
            viewHeight = LayoutParams.WRAP_CONTENT;
        }

        AppCMSUIKeyType componentKey = mJsonValueKeyMap.get(childComponent.getKey());
        AppCMSUIKeyType viewGravity = mJsonValueKeyMap.get(childComponent.getViewGravity());

        if (componentKey == null) {
            componentKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (viewGravity == null) {
            viewGravity = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        if (viewGravity == PAGE_TEXTALIGNMENT_CENTER_HORIZONTAL_KEY) {
            gravity = Gravity.CENTER_HORIZONTAL;
        }else if (viewGravity == PAGE_TEXTALIGNMENT_CENTER_VERTICAL_KEY) {
            gravity = Gravity.CENTER_VERTICAL;
        }else if (viewGravity == PAGE_TEXTALIGNMENT_CENTER_KEY) {
            gravity = Gravity.CENTER;
        }else if (viewGravity == PAGE_TEXTALIGNMENT_RIGHT_KEY) {
            gravity = Gravity.RIGHT;
        }

        if(componentKey == AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_WITH_SHORT_CODE_VIEW_KEY &&
                childComponent != null && childComponent.getComponents() != null &&
                childComponent.getComponents().size() == 1){
            leftMarginEnable = true;
        }

        FireTV mobile = layout.getTv();
        if (mobile != null) {
            if (getViewWidth(mobile) != -1) {
                if (mobile.getXAxis() != null) {
                    float scaledX = DEVICE_WIDTH * (Float.valueOf(mobile.getXAxis()) / STANDARD_MOBILE_WIDTH_PX);
                    lm = Math.round(scaledX);
                }
            }

            if (getViewHeight(mobile) != -1) {
                if (mobile.getYAxis() != null) {
                    float scaledY = DEVICE_HEIGHT * ((Float.valueOf(mobile.getYAxis()) / STANDARD_MOBILE_HEIGHT_PX));
                    tm = Math.round(scaledY);
                }
            }

            if (mobile.getLeftMargin() != null && (Float.valueOf(mobile.getLeftMargin()) != 0)) {
                float scaledLm = DEVICE_WIDTH * ((Float.valueOf(mobile.getLeftMargin()) / STANDARD_MOBILE_WIDTH_PX));
                if(componentKey == AppCMSUIKeyType.PAGE_LINK_YOUR_ACCOUNT_WITH_TV_PROVIDER_VIEW_KEY && settings != null && !settings.isShowActivateDevice()){
                    lm = 0;
                }else{
                    lm = Math.round(scaledLm);
                }
            }

            if (mobile.getTopMargin() != null && (Float.valueOf(mobile.getTopMargin())) != 0) {
                float scaledLm = DEVICE_HEIGHT * ((Float.valueOf(mobile.getTopMargin()) / STANDARD_MOBILE_HEIGHT_PX));
                tm = Math.round(scaledLm);
            }

            if (mobile.getRightMargin() != null && (Float.valueOf(mobile.getRightMargin())) != 0) {
                float scaledLm = DEVICE_WIDTH * ((Float.valueOf(mobile.getRightMargin()) / STANDARD_MOBILE_WIDTH_PX));
                rm = Math.round(scaledLm);
            }
        }

        view.setPadding(childComponent.getLeftPadding(), 0, childComponent.getRightPadding(), 0);
        int fontSize = getFontSize(mContext, childComponent);
        if (fontSize > 0) {
            if(view instanceof TextView) {
                ((TextView) view).setTextSize((float) fontSize);
            }
        }

        setViewGravity(view,gravity);

        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(viewWidth, viewHeight);
        marginLayoutParams.setMargins(lm, tm, rm, bm);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginLayoutParams);
        layoutParams.width = viewWidth;
        layoutParams.height = viewHeight;
        if(childComponent.isLayoutGravity()) layoutParams.gravity = gravity;
        view.setLayoutParams(layoutParams);
    }

    private void setViewGravity(View view, int gravity) {
        if(view instanceof LinearLayout) {
            ((LinearLayout) view).setGravity(gravity);
        }else if(view instanceof TextView) {
            ((TextView) view).setGravity(gravity);
        }else if(view instanceof Button) {
            ((TextView) view).setGravity(gravity);
        }
    }


    protected int getFontSize(Context context, Component component) {
        if (component.getFontSize() > 0) {
            return component.getFontSize();
        }
        if (component.getLayout().getTv().getFontSize() > 0) {
            return component.getLayout().getTv().getFontSize();
        }

        return 0;
    }

}

package com.viewlift.tv.views.customviews;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NavigationPlaceholderFramelayout extends FrameLayout {

    private NavigationState navigationState = NavigationState.NAVIGATION_STATE_CLOSED;

    public NavigationPlaceholderFramelayout(@NonNull Context context) {
        super(context);
    }

    public NavigationPlaceholderFramelayout(@NonNull Context context,
                                            @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationPlaceholderFramelayout(@NonNull Context context,
                                            @Nullable AttributeSet attrs,
                                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NavigationPlaceholderFramelayout(@NonNull Context context,
                                            @Nullable AttributeSet attrs,
                                            int defStyleAttr,
                                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NavigationState getNavigationState() {
        System.out.println("ANAS: get NavigationState: " + this.navigationState.name());
        return navigationState;
    }

    public void setNavigationState(NavigationState navigationState) {
        this.navigationState = navigationState;
        System.out.println("ANAS: set NavigationState: " + this.navigationState.name());

        if (navigationState.equals(NavigationState.NAVIGATION_STATE_EXPANDED)) {
            setExpandedWidth();
        } else {
            setClosedWidth();
        }
    }

    private void setClosedWidth() {
        disableEnableControls(false, this);
//        getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        RelativeLayout parent = (RelativeLayout) this.getParent();
        ViewGroup.LayoutParams parentLayoutParams = parent.getLayoutParams();
        parentLayoutParams.width = 93;
        parent.setLayoutParams(parentLayoutParams);

        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = 93;
        this.setLayoutParams(layoutParams);
    }

    private void setExpandedWidth() {
        disableEnableControls(true, this);
        getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        RelativeLayout parent = (RelativeLayout) this.getParent();
        ViewGroup.LayoutParams parentLayoutParams = parent.getLayoutParams();
        parentLayoutParams.width = 200;
        parent.setLayoutParams(parentLayoutParams);

        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = 200;
        this.setLayoutParams(layoutParams);
    }

    public enum NavigationState {
        NAVIGATION_STATE_EXPANDED,
        NAVIGATION_STATE_CLOSED
    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            child.setFocusable(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }
}

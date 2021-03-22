package com.viewlift.views.customviews;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by viewlift on 10/26/17.
 */

public class ResponsiveButton extends androidx.appcompat.widget.AppCompatImageButton {
    private static final String TAG = "ResponsiveButton";

    public ResponsiveButton(Context context) {
        super(context);
    }

    public ResponsiveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResponsiveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            setPressed(true);
//            return performClick();
//        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}

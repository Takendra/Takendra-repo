package com.viewlift.views.customviews;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

public class AppCMSBackgroundImageView extends AppCompatImageView {

    Context mContext = null;

    public AppCMSBackgroundImageView(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public AppCMSBackgroundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setup();
    }

    public AppCMSBackgroundImageView(Context context, AttributeSet attrs,
                                     int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setup();
    }

    private void setup() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (BaseView.isLandscape(mContext)) {
            height = ((width * 1) / 1);
        }
        setMeasuredDimension(width, height);
    }
}
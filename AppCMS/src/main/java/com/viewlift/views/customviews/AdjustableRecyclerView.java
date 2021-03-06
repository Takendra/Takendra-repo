package com.viewlift.views.customviews;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class AdjustableRecyclerView extends RecyclerView {

    public AdjustableRecyclerView(Context context) {
        super(context);
    }

    public AdjustableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec;

        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            // The great Android "hackatlon", the love, the magic.
            // The two leftmost bits in the height measure spec have
            // a special meaning, hence we can't use them to describe height.
            heightSpec = View.MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        } else {
            // Any other height should be respected as is.
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}

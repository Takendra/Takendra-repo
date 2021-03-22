package com.viewlift.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemClickListner implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListner;
    private GestureDetector gestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View vw, int position);
    }

    public RecyclerItemClickListner(Context context, OnItemClickListener itmclick) {
        mListner = itmclick;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childview = rv.findChildViewUnder(e.getX(), e.getY());
        if (childview != null && mListner != null && gestureDetector.onTouchEvent(e)) {
            mListner.onItemClick(childview, rv.getChildAdapterPosition(childview));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
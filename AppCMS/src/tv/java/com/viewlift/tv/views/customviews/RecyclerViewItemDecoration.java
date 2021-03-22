package com.viewlift.tv.views.customviews;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing = 0;
    private int displayMode;
    private int spanCount = 4;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    public RecyclerViewItemDecoration(int spacing) {
        this(spacing, -1);
    }

    public RecyclerViewItemDecoration(int spacing, int displayMode) {
        this.spacing = spacing;
        this.displayMode = displayMode;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        switch (displayMode) {
            case HORIZONTAL:
                outRect.left = spacing;
                outRect.right = position == itemCount - 1 ? spacing : 0;
                outRect.top = spacing;
                outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.left = 0;
                outRect.right = 0;
                outRect.top = 0;
                outRect.bottom = spacing;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int spanCount = gridLayoutManager.getSpanCount();
                    //int rows = itemCount / cols;

                    int column = position % spanCount; // item column


                    outRect.left = spacing;
                    outRect.right = position % spanCount == spanCount - 1 ? spacing : 0;

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing;


                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}

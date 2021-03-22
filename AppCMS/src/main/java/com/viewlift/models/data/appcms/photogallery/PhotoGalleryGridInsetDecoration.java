package com.viewlift.models.data.appcms.photogallery;

import android.graphics.Rect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by ram.kailash on 2/16/2018.
 */

public class PhotoGalleryGridInsetDecoration extends RecyclerView.ItemDecoration {

    private int insetHorizontal;
    private int insetVertical;
    private int verticalSpace;

    public PhotoGalleryGridInsetDecoration(int insetHorizontal ,int insetVertical, int verticalSpace) {
        this.insetHorizontal = insetHorizontal;
        this.insetVertical = insetVertical;
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        if(view.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {

            GridLayoutManager.LayoutParams layoutParams
                    = (GridLayoutManager.LayoutParams) view.getLayoutParams();

            int position = layoutParams.getViewPosition();
            if (position == RecyclerView.NO_POSITION) {
                outRect.set(0, 0, 0, 0);
                return;
            }

            // add edge margin only if item edge is not the grid edge
            int itemSpanIndex = layoutParams.getSpanIndex();
            // is left grid edge?
            outRect.left = itemSpanIndex == 0 ? 0 : insetHorizontal;
            // is top grid edge?
            outRect.top = itemSpanIndex == position ? 0 : insetVertical;
            outRect.right = 0;
            outRect.bottom = 0;
        }
    }
}
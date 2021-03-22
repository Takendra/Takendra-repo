package com.viewlift.views.adapters;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;

public class SwipeView extends ItemTouchHelper.SimpleCallback {
    private AppCMSUserPagesAdapter mAdapter;
    //    private Drawable icon;
    private final ColorDrawable background;

    public SwipeView(AppCMSUserPagesAdapter adapter,int swipeColor) {
        super(0, ItemTouchHelper.LEFT/* | ItemTouchHelper.RIGHT*/);
        mAdapter = adapter;
//        icon = ContextCompat.getDrawable(mAdapter.mContext,
//                R.drawable.ic_deleteicon);
        background = new ColorDrawable(swipeColor);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        int iconMargin = (itemView.getHeight() /*- icon.getIntrinsicHeight()*/) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight()/* - icon.getIntrinsicHeight()*/) / 2;
        int iconBottom = iconTop /*+ icon.getIntrinsicHeight()*/;

        /*if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else*/
        if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin /*- icon.getIntrinsicWidth()*/;
            int iconRight = itemView.getRight() - iconMargin;
//            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(itemView.getRight() + ((int) dX),
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c);
//        icon.draw(c);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }
}

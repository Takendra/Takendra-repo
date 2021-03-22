package com.viewlift.views.listener;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.Utils;


public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private final RecyclerView childRecyclerView;
    private Context context;
    private GridLayoutManager layoutManager;

    public PaginationScrollListener(Context context, GridLayoutManager layoutManager, RecyclerView childRecyclerView) {
        this.layoutManager = layoutManager;
        this.childRecyclerView = childRecyclerView;
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        System.out.println("isLastPage:- "+ isLastPage() +",   isLoading:- "+ isLoading());
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                childRecyclerView.post(() -> {
                    if (Utils.isNetworkAvailable(context)) {
                        loadMoreItems();
                    }
                });
            }
        }

    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();


}

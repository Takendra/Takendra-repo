package com.viewlift.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.android.NavigationPrimary;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.activity.AppCMSPageActivity;

import java.util.ArrayList;

public class AppCMSLeftNavigationMenuAdapter extends RecyclerView.Adapter<AppCMSLeftNavigationMenuAdapter.CustomViewHolder> {
    Context context;
    private ArrayList<NavigationPrimary> darwerList;
    AppCMSPageActivity listener;
    AppCMSPresenter appCMSPresenter;


    public AppCMSLeftNavigationMenuAdapter(Context context, ArrayList<NavigationPrimary> walletList, AppCMSPageActivity clickListener, AppCMSPresenter appCMSPresenter) {
        this.context = context;
        this.darwerList = walletList;
        this.appCMSPresenter = appCMSPresenter;
        this.listener = clickListener;
    }


    @Override
    public AppCMSLeftNavigationMenuAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_nav_item, null);
        view.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        AppCMSLeftNavigationMenuAdapter.CustomViewHolder viewHolder = new AppCMSLeftNavigationMenuAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppCMSLeftNavigationMenuAdapter.CustomViewHolder holder, int position) {
        holder.nav_item_label.setText(darwerList.get(position).getTitle());
        holder.nav_item_label.setMaxLines(1);
        holder.nav_item_label.setTextSize(18);
        if (darwerList.get(position).getUrl() != null) {
            Glide.with(holder.nav_item_logo.getContext()).load(darwerList.get(position).getIcon()).into(holder.nav_item_logo);
            holder.nav_item_logo.setVisibility(View.VISIBLE);
        } else {
            holder.nav_item_logo.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listener.handleWalletClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return darwerList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nav_item_label;
        ImageView nav_item_logo;

        public CustomViewHolder(View itemView) {
            super(itemView);
            nav_item_label = itemView.findViewById(R.id.nav_item_label);
            nav_item_logo = itemView.findViewById(R.id.nav_item_logo);
            nav_item_logo.setVisibility(View.VISIBLE);
        }


    }
}

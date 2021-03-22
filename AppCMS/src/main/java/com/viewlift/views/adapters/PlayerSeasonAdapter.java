package com.viewlift.views.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Season_;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerSeasonAdapter extends RecyclerView.Adapter<PlayerSeasonAdapter.MyViewHolder> {
    List<Season_> seasons = new ArrayList<>();
    AppCMSPresenter appCMSPresenter;
    int currentPlayingSeason;
    SeasonClickListener seasonClickListener;

    public PlayerSeasonAdapter(List<Season_> season, AppCMSPresenter appCMSPresenter, int currentPlayingSeason) {
        if (season == null)
            season = new ArrayList<>();
        this.seasons.addAll(season);
        this.appCMSPresenter = appCMSPresenter;
        this.currentPlayingSeason = currentPlayingSeason;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_player_season, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(seasons.get(position).getTitle());
        if (seasons.get(position).getEpisodes().size() == 0)
            holder.title.setVisibility(View.GONE);

        if (currentPlayingSeason == position) {
            holder.title.requestFocus();
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)
                holder.title.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
            else
                holder.title.setTextColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
        } else {
            if (appCMSPresenter.getPlatformType() == AppCMSPresenter.PlatformType.TV)
                holder.title.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
            else
                holder.title.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.white));
        }

        ViewCreator.setTypeFace(holder.itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), holder.title);
        holder.title.setClickable(true);
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeasonTabSelectorBus.instanceOf().setTab(seasons.get(position).getEpisodes());
                currentPlayingSeason = position;
                notifyDataSetChanged();
                if (seasonClickListener != null)
                    seasonClickListener.selectedSeason(position);

            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeasonTabSelectorBus.instanceOf().setTab(seasons.get(position).getEpisodes());
                currentPlayingSeason = position;
                notifyDataSetChanged();
                if (seasonClickListener != null)
                    seasonClickListener.selectedSeason(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        AppCompatTextView title;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            title.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    title.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
                } else {
                    title.setTextColor(appCMSPresenter.getBrandSecondaryCtaTextColor());
                }
            });
        }
    }

    public void setSeasonClickListener(SeasonClickListener seasonClickListener) {
        this.seasonClickListener = seasonClickListener;
    }

    public   interface SeasonClickListener {
        void selectedSeason(int position);
    }
}

package com.viewlift.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.verimatrix.TVProvider;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.views.dialog.CustomShape;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TVProvidersAdapter extends RecyclerView.Adapter<TVProvidersAdapter.MyViewHolder> implements Filterable {
    @Inject
    AppCMSPresenter appCMSPresenter;

    List<TVProvider> tvProviders;
    private List<TVProvider> filteredTvProviders;

    public TVProvidersAdapter(List<TVProvider> tvProviders) {
        this.tvProviders = tvProviders;
        this.filteredTvProviders = tvProviders;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_provider, parent, false);
        ((AppCMSApplication) parent.getContext().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.parentLayout.setBackground(CustomShape.createRoundedRectangleDrawable(appCMSPresenter.getGeneralTextColor()));
        if (filteredTvProviders.get(position).getImages() != null && filteredTvProviders.get(position).getImages().getImageUrl() != null)
            Glide.with(holder.itemView.getContext())
                    .load(filteredTvProviders.get(position).getImages().getImageUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.vid_image_placeholder_1x1))
                    .into(holder.providerLogo);
        else Glide.with(holder.itemView.getContext())
                .load(R.drawable.vid_image_placeholder_1x1)
                .into(holder.providerLogo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.getVerimatrixPartnerId(appCMSPresenter.getAppCMSAndroid(), holder.itemView.getContext().getString(R.string.tvprovider_verimatrix)) != null) {
                    appCMSPresenter.getFirebaseAnalytics().selectedTVProviderEvent(filteredTvProviders.get(position).getDisplayName());
                    String url = holder.itemView.getContext().getString(R.string.verimatrix_init, holder.itemView.getContext().getString(R.string.verimatrix__base_url),
                            CommonUtils.getVerimatrixPartnerId(appCMSPresenter.getAppCMSAndroid(), holder.itemView.getContext().getString(R.string.tvprovider_verimatrix)), filteredTvProviders.get(position).getTvProviderIdp());
                    appCMSPresenter.openAppcmsWebView(url);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredTvProviders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mvpd_logo)
        AppCompatImageView providerLogo;
        @BindView(R.id.parentLayout)
        ConstraintLayout parentLayout;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredTvProviders = tvProviders;
                } else {
                    List<TVProvider> filteredList = new ArrayList<>();
                    for (TVProvider row : tvProviders) {
                        if (row.getName().toLowerCase().startsWith(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    filteredTvProviders = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredTvProviders;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredTvProviders = (ArrayList<TVProvider>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}

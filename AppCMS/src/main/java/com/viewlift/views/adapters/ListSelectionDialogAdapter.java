package com.viewlift.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class ListSelectionDialogAdapter extends RecyclerView.Adapter<ListSelectionDialogAdapter.MyViewHolder> {
    Action1<String> selectedValue;
    List<String> adapterData;
    @Inject
    AppCMSPresenter appCMSPresenter;

    public ListSelectionDialogAdapter() {
    }

    public void setData(List<String> adapterData, Action1<String> selectedValue) {
        if (adapterData == null)
            adapterData = new ArrayList<>();
        this.adapterData = adapterData;
        this.selectedValue = selectedValue;
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_list_selection_view, parent, false);
        ((AppCMSApplication) parent.getContext().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(adapterData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedValue.call(adapterData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Component component = new Component();
            ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, name);
        }
    }
}

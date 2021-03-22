package com.viewlift.views.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Category;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterContentAdapter extends RecyclerView.Adapter<FilterContentAdapter.MyViewHolder> {

    List<?> filterContent;
    UpdateFromFilterContentAdapter update;
    int filterPosition;
    int selectedFilterCount = 0;
    String selectedColor = "#50a1ff";
    String unselectedColor = "#ededed";
    AppCMSPresenter appCMSPresenter;

    public void setUpdate(UpdateFromFilterContentAdapter update) {
        this.update = update;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.filterContent)
        TextView filterExpanTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Component component = new Component();
            ViewCreator.setTypeFace(itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, filterExpanTitle);

        }
    }

    public FilterContentAdapter(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
    }

    public void setData(List<?> filterContent, int filterPosition) {
        this.filterContent = filterContent;
        this.filterPosition = filterPosition;
        selectedFilterCount = 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_expandable_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (filterContent.get(position) instanceof Tag) {
            holder.filterExpanTitle.setText(((Tag) filterContent.get(position)).getTitle());
            if (((Tag) filterContent.get(position)).isSelected()) {
                selectedFilterCount++;
                holder.filterExpanTitle.setBackgroundColor(Color.parseColor(selectedColor));
            } else {
                if (selectedFilterCount > 0)
                    selectedFilterCount--;
                holder.filterExpanTitle.setBackgroundColor(Color.parseColor(unselectedColor));
            }
        }
        if (filterContent.get(position) instanceof Person) {
            holder.filterExpanTitle.setText(((Person) filterContent.get(position)).getTitle());
            if (((Person) filterContent.get(position)).isSelected()) {
                selectedFilterCount++;
                holder.filterExpanTitle.setBackgroundColor(Color.parseColor(selectedColor));
            } else {
                if (selectedFilterCount > 0)
                    selectedFilterCount--;
                holder.filterExpanTitle.setBackgroundColor(Color.parseColor(unselectedColor));
            }
        }
        if (filterContent.get(position) instanceof Category) {
            holder.filterExpanTitle.setText(((Category) filterContent.get(position)).getGist().getTitle());
            if (((Category) filterContent.get(position)).isSelected()) {
                selectedFilterCount++;
                holder.filterExpanTitle.setBackgroundColor(Color.parseColor(selectedColor));
            } else {
                if (selectedFilterCount > 0)
                    selectedFilterCount--;
                holder.filterExpanTitle.setBackgroundColor(Color.parseColor(unselectedColor));
            }
        }
        holder.filterExpanTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterContent.get(position) instanceof Tag) {
                    if (((Tag) filterContent.get(position)).isSelected()) {
                        if (selectedFilterCount > 0)
                            selectedFilterCount--;
                        ((Tag) filterContent.get(position)).setSelected(false);
                        holder.filterExpanTitle.setBackgroundColor(Color.parseColor(unselectedColor));
                    } else {
                        selectedFilterCount++;
                        ((Tag) filterContent.get(position)).setSelected(true);
                        holder.filterExpanTitle.setBackgroundColor(Color.parseColor(selectedColor));
                    }
                }
                if (filterContent.get(position) instanceof Person) {
                    if (((Person) filterContent.get(position)).isSelected()) {
                        if (selectedFilterCount > 0)
                            selectedFilterCount--;
                        ((Person) filterContent.get(position)).setSelected(false);
                        holder.filterExpanTitle.setBackgroundColor(Color.parseColor(unselectedColor));
                    } else {
                        selectedFilterCount++;
                        ((Person) filterContent.get(position)).setSelected(true);
                        holder.filterExpanTitle.setBackgroundColor(Color.parseColor(selectedColor));
                    }
                }
                if (filterContent.get(position) instanceof Category) {
                    if (((Category) filterContent.get(position)).isSelected()) {
                        if (selectedFilterCount > 0)
                            selectedFilterCount--;
                        ((Category) filterContent.get(position)).setSelected(false);
                        holder.filterExpanTitle.setBackgroundColor(Color.parseColor(unselectedColor));
                    } else {
                        selectedFilterCount++;
                        ((Category) filterContent.get(position)).setSelected(true);
                        holder.filterExpanTitle.setBackgroundColor(Color.parseColor(selectedColor));
                    }
                }
                update.onSelected(filterPosition, selectedFilterCount);
                update.onUpdate(filterContent, filterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (filterContent == null)
            return 0;
        return filterContent.size();
    }

    public interface UpdateFromFilterContentAdapter {
        void onUpdate(List<?> filterList, int filterPosition);

        void onSelected(int position, int count);

    }
}

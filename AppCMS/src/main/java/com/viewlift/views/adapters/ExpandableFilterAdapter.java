package com.viewlift.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Category;
import com.viewlift.models.data.appcms.api.FilterGroupsModel;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.api.Tag;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpandableFilterAdapter extends RecyclerView.Adapter<ExpandableFilterAdapter.ViewHolder> {

    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private List<FilterGroupsModel> filterGroupList = new ArrayList<>();
    private UpdateFromExpandableFilterAdapter update;
    private int[] countS;
    AppCMSPresenter appCMSPresenter;

    public void setListener(UpdateFromExpandableFilterAdapter update) {
        this.update = update;
    }

    public ExpandableFilterAdapter(AppCMSPresenter appCMSPresenter) {
        this.appCMSPresenter = appCMSPresenter;
        expansionsCollection.openOnlyOne(true);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_expansion_cell, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
        expansionsCollection.add(holder.expansionLayout);
    }

    @Override
    public int getItemCount() {
        return filterGroupList.size();
    }

    public void setItems(List<FilterGroupsModel> filterGroupList) {
        if (filterGroupList == null)
            this.filterGroupList = new ArrayList<>();
        else
            this.filterGroupList.addAll(filterGroupList);
        countS = new int[filterGroupList.size()];
        calculateSelectedFilters(filterGroupList);
        notifyDataSetChanged();
    }


    private void calculateSelectedFilters(List<FilterGroupsModel> filterGroupList) {
        int count = 0;
        for (int i = 0; i < filterGroupList.size(); i++) {
            count = 0;
            if (filterGroupList.get(i).getFilterContent().size() != 0) {
                if (filterGroupList.get(i).getFilterContent().get(0) instanceof Tag) {
                    for (int j = 0; j < filterGroupList.get(i).getFilterContent().size(); j++) {
                        if (((Tag) filterGroupList.get(i).getFilterContent().get(j)).isSelected()) {
                            count++;
                        }
                    }
                }
                if (filterGroupList.get(i).getFilterContent().get(0) instanceof Person) {
                    for (int j = 0; j < filterGroupList.get(i).getFilterContent().size(); j++) {
                        if (((Person) filterGroupList.get(i).getFilterContent().get(j)).isSelected()) {
                            count++;
                        }
                    }
                }
                if (filterGroupList.get(i).getFilterContent().get(0) instanceof Category) {
                    for (int j = 0; j < filterGroupList.get(i).getFilterContent().size(); j++) {
                        if (((Category) filterGroupList.get(i).getFilterContent().get(j)).isSelected()) {
                            count++;
                        }
                    }
                }
                countS[i] = count;
            }
        }
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements FilterContentAdapter.UpdateFromFilterContentAdapter {
        @BindView(R.id.expansionLayout)
        ExpansionLayout expansionLayout;
        @BindView(R.id.filterHeader)
        TextView filterTitle;
        @BindView(R.id.filterSelection)
        TextView filterSelection;
        @BindView(R.id.expandableContent)
        RecyclerView expandableContentView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Component component = new Component();
            component.setFontWeight(itemView.getContext().getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, filterTitle);
            component.setFontWeight(null);
            ViewCreator.setTypeFace(itemView.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, filterSelection);

        }

        public void bind(int position) {
            filterTitle.setText(filterGroupList.get(position).getFilterTitle().toUpperCase());
            expandableContentView
                    .setLayoutManager(new GridLayoutManager(expandableContentView.getContext(),
                            3,
                            LinearLayoutManager.VERTICAL,
                            false));
            FilterContentAdapter filterContentAdapter = new FilterContentAdapter(appCMSPresenter);
            filterContentAdapter.setUpdate(this);
            expandableContentView.setAdapter(filterContentAdapter);
            filterContentAdapter.setData(filterGroupList.get(position).getFilterContent(), position);
            filterContentAdapter.notifyDataSetChanged();
            expansionLayout.collapse(false);
            try {
                if (countS[position] == 0)
                    filterSelection.setText("");
                else
                    filterSelection.setText(countS[position] + " selected");
            } catch (Exception e) {
                filterSelection.setText("");
                e.printStackTrace();
            }
        }

        @Override
        public void onUpdate(List<?> filterList, int filterPosition) {
            update.onUpdate(filterGroupList.get(filterPosition).getFilterTitle(), filterList);
        }

        @Override
        public void onSelected(int position, int count) {
            countS[position] = count;
            if (countS[position] == 0)
                filterSelection.setText("");
            else
                filterSelection.setText(countS[position] + " selected");
        }
    }

    public int getSelectedFilterCount() {
        int count = 0;
        for (int i = 0; i < countS.length; i++) {
            count=count+countS[i];
        }
        return count;

    }

    public interface UpdateFromExpandableFilterAdapter {
        void onUpdate(String filterType, List<?> filterList);
    }

    public void resetFilter() {
        for (int i = 0; i < filterGroupList.size(); i++) {
            if (filterGroupList.get(i).getFilterContent().size() != 0) {
                if (filterGroupList.get(i).getFilterContent().get(0) instanceof Tag) {
                    for (int j = 0; j < filterGroupList.get(i).getFilterContent().size(); j++) {
                        ((Tag) filterGroupList.get(i).getFilterContent().get(j)).setSelected(false);
                    }
                }
                if (filterGroupList.get(i).getFilterContent().get(0) instanceof Person) {
                    for (int j = 0; j < filterGroupList.get(i).getFilterContent().size(); j++) {
                        ((Person) filterGroupList.get(i).getFilterContent().get(j)).setSelected(false);
                    }
                }
                if (filterGroupList.get(i).getFilterContent().get(0) instanceof Category) {
                    for (int j = 0; j < filterGroupList.get(i).getFilterContent().size(); j++) {
                        ((Category) filterGroupList.get(i).getFilterContent().get(j)).setSelected(false);
                    }
                }
            }
        }
        calculateSelectedFilters(filterGroupList);
        notifyDataSetChanged();
    }

    public List<FilterGroupsModel> getContent() {
        return filterGroupList;
    }
}

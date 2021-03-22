package com.viewlift.models.data.appcms.api;

import java.util.List;

public class FilterGroupsModel {
    String filterTitle;
    List<?> filterContent;

    public FilterGroupsModel(String filterTitle, List<?> filterContent) {
        this.filterTitle = filterTitle;
        this.filterContent = filterContent;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }

    public List<?> getFilterContent() {
        return filterContent;
    }

    public void setFilterContent(List<?> filterContent) {
        this.filterContent = filterContent;
    }
}

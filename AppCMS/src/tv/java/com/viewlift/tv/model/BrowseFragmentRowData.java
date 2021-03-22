package com.viewlift.tv.model;

import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.tv.views.customviews.CustomHeaderItem;
import com.viewlift.tv.views.presenter.CardPresenter;

import java.util.List;

/**
 * Created by nitin.tyagi on 7/1/2017.
 */

public class BrowseFragmentRowData {
    public String moduleTitle;
    public ContentDatum contentData;
    public List<String> relatedVideoIds;
    public List<Component> uiComponentList;
    public String action;
    public String blockName;
    public Object weatherHour;
    public String weatherInterval;

    //thisproperty will be use in case component is a player component.
    public boolean isPlayerComponent;
    public boolean isSearchPage;
    public int rowNumber;
    public int itemPosition;
    public boolean infoHover;
    public CardPresenter trayCardPresenter;
    public CustomHeaderItem  customHeaderItem;
    public List<ContentDatum> contentDatumList;
    public Module moduleApi;
}
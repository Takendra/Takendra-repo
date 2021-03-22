package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.ViewCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSBrowseAllAdapter extends RecyclerView.Adapter<AppCMSBrowseAllAdapter.ViewHolder>
        implements AppCMSBaseAdapter {
    private static final String TAG = "AppCMSViewAdapter";
    static int selectedPosition = -1;
    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    protected AppCMSPresenter appCMSPresenter;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    CollectionGridItemView.OnClickHandler onClickHandler;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private AppCMSUIKeyType uiBlockName;
    int adapterSize = 0;
    String blockName;
    boolean constrainteView;

    public AppCMSBrowseAllAdapter(Context context,
                                  ViewCreator viewCreator,
                                  AppCMSPresenter appCMSPresenter,
                                  Settings settings,
                                  Layout parentLayout,
                                  boolean useParentSize,
                                  Component component,
                                  Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                  Module moduleAPI,
                                  int defaultWidth,
                                  int defaultHeight,
                                  String viewType,
                                  AppCMSAndroidModules appCMSAndroidModules,
                                  String blockName,
                                  boolean constrainteView) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.appCMSPresenter = appCMSPresenter;
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.componentViewType = viewType;
        this.constrainteView = constrainteView;

        this.blockName = blockName;
        this.uiBlockName = jsonValueKeyMap.get(blockName);

        if (blockName == null) {
            this.uiBlockName = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            this.adapterData = moduleAPI.getContentData();
        } else {
            this.adapterData = new ArrayList<>();
        }
        adapterSize = adapterData.size();
        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;
        this.appCMSAndroidModules = appCMSAndroidModules;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CollectionGridItemView view = viewCreator.createCollectionGridItemView(parent.getContext(),
                parentLayout,
                useParentSize,
                component,
                appCMSPresenter,
                moduleAPI,
                appCMSAndroidModules,
                settings,
                jsonValueKeyMap,
                defaultWidth,
                defaultHeight,
                useMarginsAsPercentages,
                true,
                this.componentViewType,
                false,
                false, this.viewTypeKey, blockName);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (0 <= position && position < adapterSize && holder.componentView != null) {
            bindView(holder.componentView, adapterData.get(position), position);
        }

        if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_FILTER_02) {
            View background = null;
            for (int i = 0; i < holder.componentView.getChildItems().size(); i++) {
                CollectionGridItemView.ItemContainer itemContainer = holder.componentView.getChildItems().get(i);
                if (itemContainer.getComponent().getKey() != null) {
                    if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.view_browse_category_background))) {
                        background = itemContainer.getChildView();
                    }
                }
            }
            if (background != null) {
                if (appCMSPresenter.getBrowseCategorySelection() == null) {
                    background.setBackgroundColor(ContextCompat.getColor(mContext, R.color.browse_category_selection));
                } else {
                    background.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.black));
                }
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (appCMSPresenter.getBrowseCategorySelection() != null) {
            recyclerView.scrollToPosition(Integer.parseInt(appCMSPresenter.getBrowseCategorySelection()));
        }
    }

    @Override
    public void resetData(RecyclerView listView) {
        notifyDataSetChanged();
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {
        listView.setAdapter(null);
        adapterData = null;
        notifyDataSetChanged();
        adapterData = contentData;
        notifyDataSetChanged();
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {
            if (uiBlockName == AppCMSUIKeyType.UI_BLOCK_FILTER_02) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView,
                                      Component childComponent, ContentDatum data, int clickPosition) {
                        if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_BROWSE_ALL_GRID_KEY) {
                            appCMSPresenter.setBrowseCategorySelection(null);
                            appCMSPresenter.setBrowseCategorySelected(null);
                            appCMSPresenter.navigateToBrowsePage(null,null);
                            return;
                        }
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {

                    }
                };
            }
        }
        for (int i = 0; i < ((CollectionGridItemView) itemView).getNumberOfChildren(); i++) {
            ((CollectionGridItemView) itemView).bindChild(itemView.getContext(),
                    ((CollectionGridItemView) itemView).getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.getBrandPrimaryCtaColor(),
                    appCMSPresenter,
                    position,
                    null,
                    blockName,moduleAPI.getMetadataMap());
        }
    }


    @Override
    public void setClickable(boolean clickable) {
    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.componentView != null && holder.componentView.getChildItems() != null) {
            int childCount = holder.componentView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = holder.componentView.getChild(i);
                if (child instanceof ImageView) {
                    try {
                        Glide.with(child.getContext()).clear(child);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.componentView = (CollectionGridItemView) itemView;

        }


    }


}

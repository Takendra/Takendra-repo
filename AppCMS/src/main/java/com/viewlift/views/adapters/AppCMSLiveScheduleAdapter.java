package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.viewlift.views.customviews.PageView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/*
 * Created by viewlift on 5/5/17.
 */

public class AppCMSLiveScheduleAdapter extends RecyclerView.Adapter<AppCMSLiveScheduleAdapter.ViewHolder>
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
    private View background, itemLine;
    private int row_index = -1;
    private PageView pageView;

    public AppCMSLiveScheduleAdapter(Context context,
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
                                     boolean constrainteView,
                                     PageView pageView,
                                     RecyclerView recyclerView) {
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

        this.pageView = pageView;
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

        SeasonTabSelectorBus.instanceOf().getTimeCarousel().subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer pos) {
                row_index = pos;
                notifyDataSetChanged();
                recyclerView.scrollToPosition(row_index);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
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
        //setBorder(view, ContextCompat.getColor(mContext, R.color.weekviewGridNodataold));
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

        for (int i = 0; i < holder.componentView.getChildItems().size(); i++) {
            CollectionGridItemView.ItemContainer itemContainer = holder.componentView.getChildItems().get(i);
            if (itemContainer.getComponent().getKey() != null) {
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.view_browse_category_background))) {
                    background = itemContainer.getChildView();

                } else if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_item_line))) {
                    itemLine = itemContainer.getChildView();

                }
            }
        }
        setBorder(itemLine, ContextCompat.getColor(mContext, R.color.weekviewGridNodataold));
        if (background != null) {
            if (row_index == position) {
                background.setBackgroundColor(ContextCompat.getColor(mContext, R.color.browse_category_selection));
            } else if (position == 0 && row_index == -1) {
                background.setBackgroundColor(ContextCompat.getColor(mContext, R.color.browse_category_selection));
            } else {
                background.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.black));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(row_index);

    }

    @Override
    public void resetData(RecyclerView listView) {
        // notifyDataSetChanged();
    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {

    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(View itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {
            onClickHandler = new CollectionGridItemView.OnClickHandler() {
                @Override
                public void click(CollectionGridItemView collectionGridItemView,
                                  Component childComponent, ContentDatum data, int clickPosition) {
                    if (jsonValueKeyMap.get(component.getKey()) == AppCMSUIKeyType.PAGE_WEEK_SCHEDULE_DATE_GRID_KEY) {
                        appCMSPresenter.setItemViewClicked(true);
                        row_index = clickPosition;
                        notifyDataSetChanged();

                        RecyclerView recyclerView = pageView.findViewById(R.id.scheduleWeekViewGrid);
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(clickPosition, 0);
                    }
                }

                @Override
                public void play(Component childComponent, ContentDatum data) {

                }
            };
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
                    blockName, moduleAPI.getMetadataMap());
        }
    }


    @Override
    public void setClickable(boolean clickable) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CollectionGridItemView componentView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.componentView = (CollectionGridItemView) itemView;

        }
    }

    private void setBorder(View itemView,
                           int color) {
        GradientDrawable planBorder = new GradientDrawable();
        planBorder.setShape(GradientDrawable.RECTANGLE);
        planBorder.setStroke(2, color);
        itemView.setPadding(2, 2, 2, 2);

        planBorder.setColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));

        itemView.setBackground(planBorder);
    }

}

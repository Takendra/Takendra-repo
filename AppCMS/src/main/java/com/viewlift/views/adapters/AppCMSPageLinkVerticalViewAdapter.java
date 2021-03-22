package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;
import com.viewlift.views.customviews.CollectionGridItemView;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintCollectionGridItemView;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;

import java.util.List;
import java.util.Map;

public class AppCMSPageLinkVerticalViewAdapter extends RecyclerView.Adapter<AppCMSViewAdapter.ViewHolder>
        implements AppCMSBaseAdapter {
    private static final String TAG = "AppCMSPageLinkVerticalViewAdapter";

    protected Context mContext;
    protected Layout parentLayout;
    protected Component component;
    protected AppCMSPresenter appCMSPresenter;
    protected Settings settings;
    protected ViewCreator viewCreator;
    protected Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    Module moduleAPI;
    List<ContentDatum> adapterData;
    ConstraintCollectionGridItemView.OnClickHandler onClickHandler;
    int defaultWidth;
    int defaultHeight;
    boolean useMarginsAsPercentages;
    String componentViewType;
    AppCMSAndroidModules appCMSAndroidModules;
    CollectionGridItemView[] planItemView;
    private boolean useParentSize;
    private int unselectedColor;
    private int selectedColor;
    int day = -1;
    String blockName;
    private AppCMSUIKeyType viewTypeKey;
    boolean constrainteView;
    ConstraintViewCreator constraintViewCreator;
    public static final int itemAvailable = 0;
    public static final int noPageLinked = 1;

    public AppCMSPageLinkVerticalViewAdapter(Context context,
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
                                             boolean constrainteView
            , ConstraintViewCreator constraintViewCreator) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.constraintViewCreator = constraintViewCreator;
        this.appCMSPresenter = appCMSPresenter;
        this.blockName = blockName;
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        this.componentViewType = viewType;
        this.componentViewType = "weekScheduleGrid";
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.adapterData = moduleAPI.getContentData();
        this.unselectedColor = ContextCompat.getColor(context, android.R.color.white);
        this.selectedColor = appCMSPresenter.getGeneralBackgroundColor();
        this.constrainteView = constrainteView;
        setHasStableIds(true);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int getDay() {
        return day += 1;
    }

    private int getIndex() {
        return day += 1;
    }

    @NonNull
    @Override
    public AppCMSViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (!constrainteView) {
            CollectionGridItemView view = viewCreator.createCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    useParentSize,
                    component,
                    appCMSPresenter,
                    ViewCreator.setPageLinkCategoryDataInList(moduleAPI, getIndex()),
                    // viewCreator.removePastEventsAndShowSpecificDayEvents(moduleAPI, getDay()),
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

            // view.setBackgroundColor(Color.BLUE);

            return new AppCMSViewAdapter.ViewHolder(view);
        } else {
            MetadataMap metadataMap = null;
            if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
                metadataMap = moduleAPI.getMetadataMap();
            ConstraintCollectionGridItemView view = constraintViewCreator.createConstraintCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    true,
                    component,
                    viewCreator.setPageLinkCategoryDataInList(moduleAPI, getIndex()),
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    useMarginsAsPercentages,
                    false,
                    this.componentViewType != null ? this.componentViewType : component.getView(),
                    true,
                    false,
                    viewTypeKey, blockName, metadataMap);

            // view.setBackgroundColor(Color.BLUE);

            return new AppCMSViewAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull AppCMSViewAdapter.ViewHolder holder, int position) {
        if (holder.componentView != null) {
            bindView(holder.componentView, null, position);
        } else if (holder.constraintComponentView != null) {
            bindView(holder.constraintComponentView, adapterData.get(position), position);
        }
    }

    void bindView(CollectionGridItemView itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            View view = itemView.getChild(i);
            if (view instanceof TextView) {
                /*if (viewCreator.removePastEventsAndShowSpecificDayEvents( moduleAPI, position).getContentData().size()==0){
                    view.setVisibility(View.VISIBLE);
                }else {
                    view.setVisibility(View.GONE);
                }*/
            }

        }


    }

    void bindView(ConstraintCollectionGridItemView itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {

        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
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
       /* for (int i=0; i< itemView.getNumberOfChildren(); i++){
            View view =itemView.getChild(i);
            if (view instanceof TextView ){
                *//*if (viewCreator.removePastEventsAndShowSpecificDayEvents( moduleAPI, position).getContentData().size()==0){
                    view.setVisibility(View.VISIBLE);
                }else {
                    view.setVisibility(View.GONE);
                }*//*
            }

        }*/


    }

    @Override
    public int getItemCount() {
        return adapterData.size();
    }

    @Override
    public void resetData(RecyclerView listView) {

    }

    @Override
    public void updateData(RecyclerView listView, List<ContentDatum> contentData) {

    }

    @Override
    public void setClickable(boolean clickable) {

    }

    private void applyBgColorToChildren(ViewGroup viewGroup, int bgColor) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                if (child instanceof CardView) {
                    ((CardView) child).setUseCompatPadding(true);
                    ((CardView) child).setPreventCornerOverlap(false);
                    ((CardView) child).setCardBackgroundColor(bgColor);
                } else {
                    child.setBackgroundColor(bgColor);
                }
                applyBgColorToChildren((ViewGroup) child, bgColor);
            }
        }
    }

    private void setBorder(View itemView,
                           int color) {
        GradientDrawable planBorder = new GradientDrawable();
        planBorder.setShape(GradientDrawable.RECTANGLE);
        if (BaseView.isTablet(mContext)) {
            planBorder.setStroke(5, color);
            itemView.setPadding(3, 3, 3, 3);
        } else {
            planBorder.setStroke(7, color);
            itemView.setPadding(7, 7, 7, 7);
        }
        planBorder.setColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));

        itemView.setBackground(planBorder);
    }
}

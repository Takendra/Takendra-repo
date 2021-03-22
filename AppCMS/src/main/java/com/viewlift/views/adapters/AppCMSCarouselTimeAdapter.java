package com.viewlift.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class AppCMSCarouselTimeAdapter extends RecyclerView.Adapter<AppCMSCarouselTimeAdapter.ViewHolder>
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
    CollectionGridItemView[] collectionItemView;
    private boolean useParentSize;
    private AppCMSUIKeyType viewTypeKey;
    private boolean isClickable;

    private MotionEvent lastTouchDownEvent;
    String blockName;
    public AppCMSCarouselTimeAdapter(Context context,
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
                                     AppCMSAndroidModules appCMSAndroidModules,String blockName) {
        this.mContext = context;
        this.viewCreator = viewCreator;
        this.appCMSPresenter = appCMSPresenter;
        this.parentLayout = parentLayout;
        this.useParentSize = useParentSize;
        this.blockName = blockName;
        this.component = component;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.moduleAPI = moduleAPI;
        if (moduleAPI != null && moduleAPI.getContentData() != null) {
            this.adapterData = moduleAPI.getContentData();
        } else {
            this.adapterData = new ArrayList<>();
        }

        this.componentViewType = viewType;
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }

        this.defaultWidth = defaultWidth;
        this.defaultHeight = defaultHeight;
        this.useMarginsAsPercentages = true;

        this.isClickable = true;

        this.setHasStableIds(false);


        this.appCMSAndroidModules = appCMSAndroidModules;

        collectionItemView = new CollectionGridItemView[adapterData.size()];
    }


    public static int getSelectedPosition() {
        return selectedPosition;
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
                false, this.viewTypeKey,blockName);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (0 <= position && position < adapterData.size()) {
            for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                if (holder.componentView.getChild(i) instanceof TextView) {
                    ((TextView) holder.componentView.getChild(i)).setText("");
                }
            }
            collectionItemView[position] = holder.componentView;
            bindView(holder.componentView, adapterData.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return (adapterData != null ? adapterData.size() : 0);
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

        //sortPlan(); as per MSEAN-1434

        notifyDataSetChanged();
        listView.setAdapter(this);
        listView.invalidate();
        notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    void bindView(CollectionGridItemView itemView,
                  final ContentDatum data, int position) throws IllegalArgumentException {
        if (onClickHandler == null) {
            if (viewTypeKey == AppCMSUIKeyType.PAGE_SCHEDULE_CAROUSEL_MODULE_KEY ||
                    viewTypeKey == AppCMSUIKeyType.PAGE_BRAND_CAROUSEL_MODULE_TYPE) {
                onClickHandler = new CollectionGridItemView.OnClickHandler() {
                    @Override
                    public void click(CollectionGridItemView collectionGridItemView, Component childComponent,
                                      ContentDatum data, int clickPosition) {
//                        CollectionGridItemClick collectionGridItemClick = new CollectionGridItemClick(collectionGridItemView,
//                                childComponent, data, position);
                      //  changeArrowImage(data);
                        //TODO: Switch carousel to te specific view clicked
                        Toast.makeText(mContext," Hello ",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void play(Component childComponent, ContentDatum data) {
                    }
                };
            }

        }


        if (viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_03_KEY ||
                viewTypeKey == AppCMSUIKeyType.PAGE_SUBSCRIPTION_SELECTPLAN_01_KEY
                || viewTypeKey == AppCMSUIKeyType.PAGE_PHOTO_TRAY_MODULE_KEY) {
            //
        } else {
            itemView.setOnTouchListener((View v, MotionEvent event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    lastTouchDownEvent = event;
                }

                return false;
            });
            itemView.setOnClickListener(v -> {
                if (isClickable && data != null && data.getGist() != null) {
                    if (v instanceof CollectionGridItemView) {
                        try {
                            int eventX = (int) lastTouchDownEvent.getX();
                            int eventY = (int) lastTouchDownEvent.getY();
                            ViewGroup childContainer = ((CollectionGridItemView) v).getChildrenContainer();
                            int childrenCount = childContainer.getChildCount();
                            for (int i = 0; i < childrenCount; i++) {
                                View childView = childContainer.getChildAt(i);
                                if (childView instanceof Button) {
                                    int[] childLocation = new int[2];
                                    childView.getLocationOnScreen(childLocation);
                                    int childX = childLocation[0] - 8;
                                    int childY = childLocation[1] - 8;
                                    int childWidth = childView.getWidth() + 8;
                                    int childHeight = childView.getHeight() + 8;
                                    if (childX <= eventX && eventX <= childX + childWidth) {
                                        if (childY <= eventY && eventY <= childY + childHeight) {
                                            childView.performClick();
                                            return;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            //
                        }
                    }


                }

            });

        }

        for (int i = 0; i < itemView.getNumberOfChildren(); i++) {
            itemView.bindChild(itemView.getContext(),
                    itemView.getChild(i),
                    data,
                    jsonValueKeyMap,
                    onClickHandler,
                    componentViewType,
                    appCMSPresenter.getBrandPrimaryCtaColor(),
                    appCMSPresenter,
                    position, null,
                    "",moduleAPI.getMetadataMap());
        }
    }

    private void changeArrowImage(ContentDatum data) {
        for (int i = 0; i < collectionItemView.length; i++) {
            CollectionGridItemView item = collectionItemView[i];
            for (int j = 0; j < item.getChildItems().size(); j++) {
                CollectionGridItemView.ItemContainer itemContainer = item.getChildItems().get(j);
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_carousel_arrow_image_key))) {
                    ImageView arrow = (ImageView) itemContainer.getChildView();
                    if (adapterData.get(i).getGist().getId().contains(data.getGist().getId())) {
                        arrow.setImageResource(R.drawable.arrow_up);
                    } else {
                        arrow.setImageResource(R.drawable.arrow_left);
                    }
                }
                if (itemContainer.getComponent().getKey().contains(mContext.getString(R.string.app_cms_page_carousel_time_key))) {
                    TextView time = (TextView) itemContainer.getChildView();
                    if (adapterData.get(i).getGist().getId().contains(data.getGist().getId())) {
                        time.setTextColor(Color.parseColor("#DD343B"));
                    } else {
                        time.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }

        }


    }

    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }


    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        int childCount = holder.componentView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = holder.componentView.getChild(i);
            if (child instanceof ImageView) {
                Glide.with(child.getContext()).clear(child);
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

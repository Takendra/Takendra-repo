package com.viewlift.views.adapters;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.android.AppCMSAndroidModules;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.Settings;
import com.viewlift.views.customviews.InternalEvent;
import com.viewlift.views.customviews.OnInternalEvent;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.constraintviews.ConstraintViewCreator;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by viewlift on 5/25/17.
 */

public class AppCMSCarouselItemAdapter extends AppCMSViewAdapter implements OnInternalEvent {
    private static String TAG = "CarouselItemAdapter";
    private long UPDATE_CAROUSEL_TO = 5000L;

    private  RecyclerView listView;
    private final Handler carouselHandler;
    private final Runnable carouselUpdater;
    private final boolean loop;
    private Set<OnInternalEvent> internalEventReceivers;
    private volatile Integer updatedIndex;
    private volatile boolean cancelled;
    private volatile boolean started;
    private boolean scrolled;
    private RecyclerView.OnScrollListener scrollListener;
    private AppCMSUIKeyType viewTypeKey;
    private String moduleId;
    String blockName;
    Settings settings;
    ConstraintViewCreator constraintViewCreator;
    PagerSnapHelper pagerSnapHelper;

    public AppCMSCarouselItemAdapter(Context context,
                                     ViewCreator viewCreator,
                                     Settings settings,
                                     Layout parentLayout,
                                     final Component component,
                                     Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                                     Module moduleAPI,
                                     final RecyclerView listView,
                                     boolean loop,
                                     AppCMSAndroidModules appCMSAndroidModules, String viewType, String blockName, boolean constrainteView,
                                     ConstraintViewCreator constraintViewCreator) {
        super(context,
                viewCreator,
                settings,
                parentLayout,
                false,
                component,
                jsonValueKeyMap,
                moduleAPI,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                viewType,
                appCMSAndroidModules, blockName, constrainteView, constraintViewCreator);


        this.listView = listView;
        this.constraintViewCreator = constraintViewCreator;
        this.loop = loop;
        this.blockName = blockName;
        this.updatedIndex = getDefaultIndex();
        ((AppCMSApplication) this.mContext.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        this.settings = settings;
        this.internalEventReceivers = new ArraySet<>();
        this.cancelled = false;
        this.started = false;
        this.scrolled = false;
        this.componentViewType = viewType;
        this.viewTypeKey = jsonValueKeyMap.get(componentViewType);
        if (this.viewTypeKey == null) {
            this.viewTypeKey = AppCMSUIKeyType.PAGE_EMPTY_KEY;
        }
        this.listView.getLayoutManager().scrollToPosition(updatedIndex);
        if (settings.getTimeDelay() != null && !settings.getTimeDelay().isEmpty()) {
            UPDATE_CAROUSEL_TO = Integer.valueOf(settings.getTimeDelay()) * 1000;
        }
        this.carouselHandler = new Handler();
        this.carouselUpdater = () -> {
            if (adapterData.size() > 1 && !cancelled && (loop || (!loop && updatedIndex < adapterData.size()))) {
                int firstVisibleIndex =
                        ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleIndex =
                        ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findLastVisibleItemPosition();
                if (updatedIndex < firstVisibleIndex) {
                    updatedIndex = firstVisibleIndex;
                }
                if (lastVisibleIndex < updatedIndex) {
                    updatedIndex = lastVisibleIndex;
                }
                if (0 <= updatedIndex) {
                    updateCarousel(updatedIndex + 1, false);
                }
                postUpdateCarousel();
            } else if (cancelled) {
                updatedIndex = getDefaultIndex();
            }
        };

        this.scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    synchronized (listView) {
                        int firstVisibleIndex =
                                ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findFirstVisibleItemPosition();
                        int lastVisibleIndex =
                                ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findLastVisibleItemPosition();
                        if (firstVisibleIndex != lastVisibleIndex) {
                            listView.removeOnScrollListener(this);
                            int nextVisibleViewIndex = lastVisibleIndex;
                            if (updatedIndex != firstVisibleIndex) {
                                nextVisibleViewIndex = firstVisibleIndex;
                            }

                            try {
                                listView.smoothScrollToPosition(nextVisibleViewIndex);
                            } catch (Exception e) {
                                //Log.e(TAG, "Error scrolling to position: " + nextVisibleViewIndex);
                            }
                            sendEvent(new InternalEvent<Object>(nextVisibleViewIndex));
                            setUpdatedIndex(nextVisibleViewIndex);
                        }
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    scrolled = true;
                }
            }
        };

         pagerSnapHelper = new PagerSnapHelper(){
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                synchronized (listView) {
                    int firstVisibleIndex =
                            ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findFirstVisibleItemPosition();
                    int lastVisibleIndex =
                            ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findLastVisibleItemPosition();
                    if (firstVisibleIndex != lastVisibleIndex) {
                        int nextVisibleViewIndex = lastVisibleIndex;
                        if (updatedIndex != firstVisibleIndex) {
                            nextVisibleViewIndex = firstVisibleIndex;
                        }

                        try {
                            listView.smoothScrollToPosition(nextVisibleViewIndex);
                        } catch (Exception e) {
                            //Log.e(TAG, "Error scrolling to position: " + nextVisibleViewIndex);
                        }
                        sendEvent(new InternalEvent<Object>(nextVisibleViewIndex));
                        setUpdatedIndex(nextVisibleViewIndex);
                    }
                    reInit();
                return super.onFling(velocityX, velocityY);
            }
        }};
        pagerSnapHelper.attachToRecyclerView(this.listView);
        resetData(this.listView);
       /* this.listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    synchronized (listView) {
                        int firstVisibleIndex =
                                ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findFirstVisibleItemPosition();
                        int lastVisibleIndex =
                                ((LinearLayoutManager) AppCMSCarouselItemAdapter.this.listView.getLayoutManager()).findLastVisibleItemPosition();
                        if (firstVisibleIndex != lastVisibleIndex) {
                            listView.removeOnScrollListener(this);
                            int nextVisibleViewIndex = lastVisibleIndex;
                            if (updatedIndex != firstVisibleIndex) {
                                nextVisibleViewIndex = firstVisibleIndex;
                            }

                            try {
                                listView.smoothScrollToPosition(nextVisibleViewIndex);
                            } catch (Exception e) {
                                //Log.e(TAG, "Error scrolling to position: " + nextVisibleViewIndex);
                            }
                            sendEvent(new InternalEvent<Object>(nextVisibleViewIndex));
                            setUpdatedIndex(nextVisibleViewIndex);
                        }
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    scrolled = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
*/
        /*this.listView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                int[] parentLocation = new int[2];
                int childIndex = -1;
                recyclerView.getLocationOnScreen(parentLocation);
                int eventX = (int) motionEvent.getX() + parentLocation[0];
                int eventY = (int) motionEvent.getY() + parentLocation[1];
                int firstVisibleIndex =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                int lastVisibleIndex =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                for (int i = firstVisibleIndex; i <= lastVisibleIndex; i++) {
                    View childView = recyclerView.getLayoutManager().findViewByPosition(firstVisibleIndex);
                    //if (childView instanceof CollectionGridItemView) {
                    if (childView instanceof ConstraintCollectionGridItemView) {
                        ConstraintCollectionGridItemView collectionGridItemView = (ConstraintCollectionGridItemView) childView;
                        // ViewGroup childContainer = collectionGridItemView.getChildrenContainer();
                        for (int j = 0; j < collectionGridItemView.getChildItems().size(); j++) {
                            ConstraintCollectionGridItemView.ItemContainer itemContainer = collectionGridItemView.getChildItems().get(j);
                            if (itemContainer.getComponent().getKey() != null && itemContainer.getComponent().getKey().equalsIgnoreCase(context.getString(R.string.app_cms_page_schedule_carousel_add_to_calendar_button_key))) {
                                ImageView calendar = (ImageView) itemContainer.getChildView();
                                if (calendar != null) {
                                    int[] calendarLoacation = new int[2];
                                    calendar.getLocationOnScreen(calendarLoacation);
                                    int xLocHalt = calendarLoacation[0] + calendar.getWidth();
                                    int yLocHalt = calendarLoacation[1] + calendar.getHeight();
                                    if (calendarLoacation[0] < eventX &&
                                            eventX < xLocHalt &&
                                            calendarLoacation[1] < yLocHalt &&
                                            eventY < yLocHalt) {
                                        ContentDatum data = adapterData.get(i % adapterData.size());
                                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                                            appCMSPresenter.getAppCalendarEvent().checkCalendarEventExistsAndAddEvent(data, appCMSPresenter);
                                        }
                                        return true;
                                    }
                                }
                            }

                        }


                        int[] collectionGridLocation = new int[2];
                        collectionGridItemView.getLocationOnScreen(collectionGridLocation);
                        int collectionGridItemWidth = collectionGridItemView.getWidth();
                        int collectionGridItemHeight = collectionGridItemView.getHeight();
                        if (collectionGridLocation[0] <= eventX && eventX <= collectionGridLocation[0] + collectionGridItemWidth) {
                            if (collectionGridLocation[1] <= eventY && eventY <= collectionGridLocation[1] + collectionGridItemHeight) {
                                childIndex = i;
                            }
                        }
                        *//*if (BaseView.isLandscape(context) &&
                                childContainer instanceof LinearLayout &&
                                childContainer.getChildCount() > 1 &&
                                childContainer.getChildAt(1) instanceof ViewGroup) {
                            childContainer = (ViewGroup) childContainer.getChildAt(1);
                        }
                        for (int j = 0; j < childContainer.getChildCount(); j++) {
                            View gridItemChildView = childContainer.getChildAt(j);
                            if (gridItemChildView instanceof Button) {
                                int[] childLocation = new int[2];
                                gridItemChildView.getLocationOnScreen(childLocation);
                                int childWidth = gridItemChildView.getWidth();
                                int childHeight = gridItemChildView.getHeight();
                                if (childLocation[0] <= eventX && eventX <= childLocation[0] + childWidth) {
                                    if (childLocation[1] <= eventY && eventY <= childLocation[1] + childHeight) {
                                        if (adapterData.size() != 0) {
                                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                                onClickHandler.play(collectionGridItemView.matchComponentToView(gridItemChildView),
                                                        adapterData.get(i % adapterData.size()));
                                            }
                                        }
                                        return true;
                                    }
                                }
                            }
                        }*//*
                    } else if (childView instanceof CollectionGridItemView) {
                        CollectionGridItemView collectionGridItemView = (CollectionGridItemView) childView;
                        ViewGroup childContainer = collectionGridItemView.getChildrenContainer();
                        int[] collectionGridLocation = new int[2];
                        collectionGridItemView.getLocationOnScreen(collectionGridLocation);
                        int collectionGridItemWidth = collectionGridItemView.getWidth();
                        int collectionGridItemHeight = collectionGridItemView.getHeight();
                        if (collectionGridLocation[0] <= eventX && eventX <= collectionGridLocation[0] + collectionGridItemWidth) {
                            if (collectionGridLocation[1] <= eventY && eventY <= collectionGridLocation[1] + collectionGridItemHeight) {
                                childIndex = i;
                            }
                        }
                        if (BaseView.isLandscape(context) &&
                                childContainer instanceof LinearLayout &&
                                childContainer.getChildCount() > 1 &&
                                childContainer.getChildAt(1) instanceof ViewGroup) {
                            childContainer = (ViewGroup) childContainer.getChildAt(1);
                        }
                        for (int j = 0; j < collectionGridItemView.getChildItems().size(); j++) {
                            CollectionGridItemView.ItemContainer itemContainer = collectionGridItemView.getChildItems().get(j);
                            if (itemContainer.getComponent().getKey() != null && itemContainer.getComponent().getKey().equalsIgnoreCase(context.getString(R.string.app_cms_page_schedule_carousel_add_to_calendar_button_key))) {
                                ImageView calendar = (ImageView) itemContainer.getChildView();
                                if (calendar != null) {
                                    int[] calendarLoacation = new int[2];
                                    calendar.getLocationOnScreen(calendarLoacation);
                                    int xLocHalt = calendarLoacation[0] + calendar.getWidth();
                                    int yLocHalt = calendarLoacation[1] + calendar.getHeight();
                                    if (calendarLoacation[0] < eventX &&
                                            eventX < xLocHalt &&
                                            calendarLoacation[1] < yLocHalt &&
                                            eventY < yLocHalt) {
                                        ContentDatum data = adapterData.get(i % adapterData.size());
                                        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                                            appCMSPresenter.getAppCalendarEvent().checkCalendarEventExistsAndAddEvent(data, appCMSPresenter);
                                        }
                                        return true;
                                    }
                                }
                            }

                        }
                        for (int j = 0; j < childContainer.getChildCount(); j++) {
                            View gridItemChildView = childContainer.getChildAt(j);
                            if (gridItemChildView instanceof Button) {
                                int[] childLocation = new int[2];
                                gridItemChildView.getLocationOnScreen(childLocation);
                                int childWidth = gridItemChildView.getWidth();
                                int childHeight = gridItemChildView.getHeight();
                                if (childLocation[0] <= eventX && eventX <= childLocation[0] + childWidth) {
                                    if (childLocation[1] <= eventY && eventY <= childLocation[1] + childHeight) {
                                        if (adapterData.size() != 0) {
                                            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                                onClickHandler.play(collectionGridItemView.matchComponentToView(gridItemChildView),
                                                        adapterData.get(i % adapterData.size()));
                                            }
                                        }
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    carouselHandler.removeCallbacks(carouselUpdater);
                    listView.setOnScrollListener(scrollListener);
                    return true;
                } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                    if (!scrolled && childIndex != -1 && adapterData.size() != 0 && viewTypeKey != AppCMSUIKeyType.PAGE_SHOW_DETAIL_04_MODULE_KEY) {
                        if (onConstraintClickHandler != null) {
                            onConstraintClickHandler.click(null,
                                    component,
                                    adapterData.get(childIndex % adapterData.size()), childIndex);
                        }
                        if (onClickHandler != null) {
                            onClickHandler.click(null,
                                    component,
                                    adapterData.get(childIndex % adapterData.size()), childIndex);
                        }
                    } else {
                        listView.removeOnScrollListener(scrollListener);
                        postUpdateCarousel();
                    }
                    scrolled = false;
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });*/

        this.useMarginsAsPercentages = false;
        SeasonTabSelectorBus.instanceOf().getTimeCarousel().subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer pos) {
                setUpdatedIndex(pos);
                if (listView != null)
                    listView.scrollToPosition(pos);
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

        View view = null;

        if (constrainteView) {
            MetadataMap metadataMap = null;
            if (moduleAPI != null && moduleAPI.getMetadataMap() != null)
                metadataMap = moduleAPI.getMetadataMap();
            view = constraintViewCreator.createConstraintCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    true,
                    component,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    useMarginsAsPercentages,
                    false,
                    this.componentViewType.trim().length() > 2 ? this.componentViewType : component.getView(),
                    true,
                    false,
                    viewTypeKey, blockName, metadataMap);
        } else {
            view = viewCreator.createCollectionGridItemView(parent.getContext(),
                    parentLayout,
                    true,
                    component,
                    appCMSPresenter,
                    moduleAPI,
                    appCMSAndroidModules,
                    settings,
                    jsonValueKeyMap,
                    defaultWidth,
                    defaultHeight,
                    useMarginsAsPercentages,
                    false,
                    this.componentViewType.trim().length() > 2 ? this.componentViewType : component.getView(),
                    true,
                    false, viewTypeKey, blockName);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //if (!loop) {

        if (holder.constraintComponentView != null) {
            for (int i = 0; i < holder.constraintComponentView.getNumberOfChildren(); i++) {
                Component childComponent =
                        holder.constraintComponentView.matchComponentToView(holder.constraintComponentView.getChild(i));
                if (childComponent != null) {
                    AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
                    if (componentType == null) {
                        componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                    }
                    if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY) {
                        ((TextView) holder.constraintComponentView.getChild(i)).setText("");
                    } else if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
                        ((ImageView) holder.constraintComponentView.getChild(i)).setImageResource(android.R.color.transparent);
                    }
                }
            }


            if (adapterData.size() != 0) {
                if (loop) {
                    bindView(holder.constraintComponentView,
                            adapterData.get(position % adapterData.size()), position);
                } else {
                    bindView(holder.constraintComponentView, adapterData.get(position), position);
                }
            }
        } else {
            for (int i = 0; i < holder.componentView.getNumberOfChildren(); i++) {
                Component childComponent =
                        holder.componentView.matchComponentToView(holder.componentView.getChild(i));
                if (childComponent != null) {
                    AppCMSUIKeyType componentType = jsonValueKeyMap.get(childComponent.getType());
                    if (componentType == null) {
                        componentType = AppCMSUIKeyType.PAGE_EMPTY_KEY;
                    }
                    if (componentType == AppCMSUIKeyType.PAGE_LABEL_KEY) {
                        ((TextView) holder.componentView.getChild(i)).setText("");
                    } else if (componentType == AppCMSUIKeyType.PAGE_IMAGE_KEY) {
                        ((ImageView) holder.componentView.getChild(i)).setImageResource(android.R.color.transparent);
                    }
                }
            }


            if (adapterData.size() != 0) {
                if (loop) {
                    bindView(holder.componentView,
                            adapterData.get(position % adapterData.size()), position);
                } else {
                    bindView(holder.componentView, adapterData.get(position), position);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        if (loop) {
            return Integer.MAX_VALUE;
        }
        return adapterData.size();
    }

    @Override
    public void addReceiver(OnInternalEvent e) {
        internalEventReceivers.add(e);
    }

    @Override
    public void sendEvent(InternalEvent<?> event) {
        for (OnInternalEvent receiver : internalEventReceivers) {
            receiver.receiveEvent(event);
        }
    }

    @Override
    public void receiveEvent(InternalEvent<?> event) {
        if (!cancelled) {
            if (event.getEventData() instanceof Integer) {
                updateCarousel(calculateUpdateIndex((Integer) event.getEventData()), true);
            }
        }
    }

    @Override
    public void cancel(boolean cancel) {
        cancelled = cancel;
        if (!cancelled && !started) {
            carouselHandler.removeCallbacks(carouselUpdater);
            updatedIndex = getDefaultIndex();
            listView.scrollToPosition(updatedIndex);
            postUpdateCarousel();
            started = true;
        } else if (cancel) {
            carouselHandler.removeCallbacks(carouselUpdater);
            started = false;
            updatedIndex = getDefaultIndex();
        }
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public void postUpdateCarousel() {
        carouselHandler.postDelayed(carouselUpdater, UPDATE_CAROUSEL_TO);
    }

    public void updateCarousel(int index, boolean fromEvent) {
        synchronized (listView) {
//            index = calculateUpdateIndex(index);
            setUpdatedIndex(index);
            try {
                listView.smoothScrollToPosition(updatedIndex);
            } catch (Exception e) {
            }
            if (!fromEvent) {
                sendEvent(new InternalEvent<Object>(updatedIndex));
            }
        }
    }

    @Override
    public void resetData(RecyclerView listView) {
        super.resetData(listView);
        updatedIndex = getDefaultIndex();
        sendCancelEventToReceivers(cancelled);
        sendEvent(new InternalEvent<Object>(updatedIndex));
        listView.scrollToPosition(updatedIndex);
        cancel(false);
    }

    void reInit(){
       // updatedIndex = getDefaultIndex();
        sendCancelEventToReceivers(cancelled);
        sendEvent(new InternalEvent<Object>(updatedIndex));
        cancel(false);
    }

    private int getDefaultIndex() {
        if (adapterData.size() != 0 && loop) {
            return Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % adapterData.size());
        }
        return 0;
    }

    private void setUpdatedIndex(int index) {
        this.updatedIndex = index;
    }

    private int calculateUpdateIndex(int index) {
        int firstVisibleIndex =
                ((LinearLayoutManager) listView.getLayoutManager()).findFirstVisibleItemPosition();
        int lastVisibleIndex =
                ((LinearLayoutManager) listView.getLayoutManager()).findLastVisibleItemPosition();

        if (index < firstVisibleIndex && adapterData.size() < (firstVisibleIndex - index)) {
            if ((firstVisibleIndex % adapterData.size()) < (index % adapterData.size())) {
                index = (firstVisibleIndex + (index % adapterData.size()));
            } else {
                index = (firstVisibleIndex - (index % adapterData.size()));
            }
        } else if (lastVisibleIndex < index && adapterData.size() < (index - lastVisibleIndex)) {
            if ((lastVisibleIndex % adapterData.size()) < (index % adapterData.size())) {
                index = ((index % adapterData.size()) + lastVisibleIndex);
            } else {
                index = ((index % adapterData.size()) - lastVisibleIndex);
            }
        }

        if (adapterData.size() < Math.abs(index - firstVisibleIndex) ||
                adapterData.size() < Math.abs(index - lastVisibleIndex)) {
            index = firstVisibleIndex;
        }

        return index;
    }

    private void sendCancelEventToReceivers(boolean cancel) {
        for (OnInternalEvent receiver : internalEventReceivers) {
            receiver.cancel(cancel);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listView = null;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        resetData(this.listView);
    }
}

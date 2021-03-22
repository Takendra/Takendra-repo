package com.viewlift.views.customviews;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.views.rxbus.SeasonTabSelectorBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeSelectorView extends BaseView implements OnInternalEvent {
    private static final String TAG = "TimeSelectorView";

    private Component component;
    private final int numDot;
    private final int selectedColor;
    private final int deselectedColor;
    private final Module moduleAPI;
    private final Map<String, AppCMSUIKeyType> jsonValueKeyMap;
    private List<View> childViews;
    private List<OnInternalEvent> internalEventReceivers;
    private volatile int selectedViewIndex;
    private volatile boolean cancelled;
    private ArrayList<TextView> timeList = new ArrayList<>();
    private ArrayList<ImageView> imageList = new ArrayList<>();

    private String moduleId;
    HorizontalScrollView carouselView;

    public TimeSelectorView(Context context,
                            Component component,
                            Module moduleApi,
                            Map<String, AppCMSUIKeyType> jsonValueKeyMap,
                            int selectedColor,
                            int deselectedColor) {
        super(context);
        this.component = component;
        this.moduleAPI = moduleApi;
        this.jsonValueKeyMap = jsonValueKeyMap;
        this.selectedColor = selectedColor;
        this.deselectedColor = deselectedColor;
        this.selectedViewIndex = 0;
        this.cancelled = false;
        this.numDot = moduleAPI != null ? moduleAPI.getContentData() != null ? moduleAPI.getContentData().size() : 0 : 0;

        init();
    }

    @Override
    public void init() {
        Context context = getContext();
        childrenContainer = new LinearLayout(context);
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        RelativeLayout.LayoutParams childrenLayoutParams =
                new RelativeLayout.LayoutParams(width, height);
        childrenLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        childrenLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        childrenContainer.setLayoutParams(childrenLayoutParams);
        ((LinearLayout) childrenContainer).setOrientation(LinearLayout.HORIZONTAL);

        carouselView = new HorizontalScrollView(context);
        carouselView.setLayoutParams(new HorizontalScrollView.LayoutParams(width, height));
        carouselView.setSmoothScrollingEnabled(true);
        carouselView.setScrollBarSize(0);
        carouselView.addView(childrenContainer);
        //  carouselView.setBackgroundColor(deselectedColor);
        addView(carouselView);
        childViews = new ArrayList<>();
        internalEventReceivers = new ArrayList<>();
        addTimeBars(numDot);
    }

    public boolean dotsInitialized() {
        try {
            return childViews.size() > 0;
        } catch (Exception e) {

        }
        return false;
    }

    public void addTimeBars(int size) {
        if (childrenContainer != null) {
            childrenContainer.removeAllViews();
        }
        for (int i = 0; i < size; i++) {
            addTimeView(i);
        }
    }

    public void addTimeView(int dataIndex) {
        LinearLayout timeView = createTimeView(getContext());
        for (Component childComponent : component.getComponents()) {
            if (jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_CAROUSEL_ARROW_IMAGE_KEY) {
                ImageView imageView = createPointerImageView(getContext());
                timeView.addView(imageView);
                imageList.add(imageView);
            }
            if (jsonValueKeyMap.get(childComponent.getKey()) == AppCMSUIKeyType.PAGE_CAROUSEL_TIME_KEY) {
                TextView tvTime = createTimeTextView(getContext());
                String day = appCMSPresenter.getDateFormat(moduleAPI.getContentData().get(dataIndex).getGist().getScheduleStartDate(), "E");
                if(day.equals(appCMSPresenter.getDateFormat(System.currentTimeMillis(), "E"))){
                    day = "TODAY";
                }
                String time = appCMSPresenter.getDateFormat(moduleAPI.getContentData().get(dataIndex).getGist().getScheduleStartDate(), "hh:mm a");
                String separationChar = BaseView.isTablet(getContext())?" ":"\n";
                tvTime.setText(day.toUpperCase() +  separationChar + time.toUpperCase());
                //tvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
                tvTime.setTextColor(deselectedColor);
                ViewCreator.setTypeFace(getContext(), appCMSPresenter, jsonValueKeyMap, childComponent, tvTime);
                timeView.addView(tvTime);
                timeList.add(tvTime);
            }
        }


        childrenContainer.addView(timeView);
        childViews.add(timeView);

        final int index = childViews.size() - 1;
        timeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedViewIndex != index) {
                    deselect(selectedViewIndex);
                    select(index);
//                    sendEvent(new InternalEvent<>(selectedViewIndex));
                    SeasonTabSelectorBus.instanceOf().setTimeCarousel(selectedViewIndex);

                }
            }


        });
    }

    public void select(int index) {
        if (0 <= index && index < childViews.size()) {
            // childViews.get(index).setBackgroundColor(selectedColor);
           // timeList.get(index).setTextColor(ContextCompat.getColor(getContext(), R.color.colorLive));
            timeList.get(index).setTextColor(selectedColor);
            if (imageList != null && imageList.size() > 0) {
                imageList.get(index).setImageResource(R.drawable.arrow_up);
            }
            //((GradientDrawable) childViews.get(index).getBackground()).setColor(selectedColor);
            //carouselView.smoothScrollBy(selectedViewIndex,index);
            int x, y;
            x = childViews.get(index).getLeft();
            y = childViews.get(index).getTop();
            carouselView.scrollTo(x, y);
            selectedViewIndex = index;


        }
    }

    public void deSelectAll() {
        for (int i = 0; i < childViews.size(); i++) {
            deselect(i);
        }
    }

    public void deselect(int index) {
        if (0 <= index && index < childViews.size()) {
            //  childViews.get(index).setBackgroundColor(deselectedColor);
            //timeList.get(index).setTextColor(ContextCompat.getColor(getContext(), R.color.color_white));
            timeList.get(index).setTextColor(deselectedColor);
            if (imageList != null && imageList.size() > 0) {
                imageList.get(index).setImageResource(R.drawable.arrow_left);
            }
            //((GradientDrawable) childViews.get(index).getBackground()).setColor(deselectedColor);
        }
    }

    private ImageView createPointerImageView(Context context) {
        ImageView dotImageView = new ImageView(context);
        dotImageView.setImageResource(R.drawable.arrow_left);
        //((GradientDrawable) dotImageView.getBackground()).setColor(deselectedColor);

        int imageWidth = (int) getViewWidth(context,
                component.getLayout(),
                (int) context.getResources().getDimension(R.dimen.dot_selector_listview_height));
        int imageHeight = (int) getViewHeight(context,
                component.getLayout(),
                (int) context.getResources().getDimension(R.dimen.dot_selector_listview_height));
        LayoutParams dotSelectorLayoutParams =
                new LayoutParams(imageWidth, imageHeight);
        dotImageView.setLayoutParams(dotSelectorLayoutParams);
        dotImageView.setPadding(3, 3, 3, 3);
        return dotImageView;
    }

    private TextView createTimeTextView(Context context) {
        TextView tvTime = new TextView(context);
        if (BaseView.isTablet(context)) {
            if (BaseView.isLandscape(context)) {
                tvTime.setPadding(25, 3, 25, 3);
            } else {
                tvTime.setPadding(25, 3, 25, 3);
            }
        } else {
            tvTime.setPadding(60, 3, 60, 3);
        }
        tvTime.setGravity(Gravity.CENTER);
        tvTime.setTextSize(10);
        if (BaseView.isTablet(getContext())) {
            tvTime.setTextSize(15);
        }
        return tvTime;
    }

    private LinearLayout createTimeView(Context context) {
        LinearLayout timeSelectorView = new LinearLayout(context);
        int viewWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int viewHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams viewLayoutParams =
                new LinearLayout.LayoutParams(viewWidth, viewHeight);
        //viewLayoutParams.weight = 1f;
        timeSelectorView.setLayoutParams(viewLayoutParams);
        // timeSelectorView.setBackgroundColor(deselectedColor);
        timeSelectorView.setGravity(Gravity.CENTER);
        timeSelectorView.setOrientation(LinearLayout.HORIZONTAL);
        timeSelectorView.setPadding(10, 3, 10, 3);
        return timeSelectorView;
    }

    @Override
    public void addReceiver(OnInternalEvent e) {
        internalEventReceivers.add(e);
        getParent().bringChildToFront(this);
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
            if (event.getEventData() instanceof Integer && childViews.size() > 0) {
                int index = (Integer) event.getEventData() % childViews.size();
                deselect(selectedViewIndex);
                select(index);
            }
        }
    }

    @Override
    public void cancel(boolean cancel) {
        this.cancelled = cancel;
        if (this.cancelled) {
            deselect(selectedViewIndex);
            selectedViewIndex = 0;
            select(selectedViewIndex);
        }
    }

    @Override
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    public String getModuleId() {
        return moduleId;
    }

    @Override
    protected Component getChildComponent(int index) {
        return null;
    }

    @Override
    protected Layout getLayout() {
        return component.getLayout();
    }
}


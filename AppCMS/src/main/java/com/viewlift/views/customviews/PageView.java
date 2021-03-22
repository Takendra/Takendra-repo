package com.viewlift.views.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;

import android.view.MotionEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.viewlift.R;
import com.viewlift.Utils;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.models.data.appcms.ui.page.AppCMSPageUI;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.models.data.appcms.ui.page.Layout;
import com.viewlift.models.data.appcms.ui.page.ModuleList;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.AppCMSBaseAdapter;
import com.viewlift.views.adapters.AppCMSPageViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;

public class PageView extends BaseView {
    private static final String TAG = "PageView";

    private final AppCMSPageUI appCMSPageUI;
    private List<ListWithAdapter> adapterList;
    private List<ViewWithComponentId> viewsWithComponentIds;
    private boolean userLoggedIn;
    private Map<String, ModuleView> moduleViewMap;
    private AppCMSPresenter appCMSPresenter;
    private SwipeRefreshLayout mainView,swipeRefreshLayout;
    private AppCMSPageViewAdapter appCMSPageViewAdapter;

    private ViewDimensions fullScreenViewOriginalDimensions;

    public boolean shouldRefresh;

    private boolean reparentChromecastButton;

    private OnScrollChangeListener onScrollChangeListener;

    private boolean ignoreScroll;
    private FrameLayout headerView;
    private ConstraintLayout headerConstraintView;
    String pageId;
    private ViewDragHelper dragHelper;
    private PageView.Listener listener;
    private float minFlingVelocity;
    private float verticalTouchSlop;
    private boolean animateAlpha;

    @Inject
    public PageView(Context context,
                    AppCMSPageUI appCMSPageUI,
                    AppCMSPresenter appCMSPresenter, String pageId) {
        super(context);
        this.appCMSPageUI = appCMSPageUI;
        this.viewsWithComponentIds = new ArrayList<>();
        this.moduleViewMap = new HashMap<>();
        this.appCMSPresenter = appCMSPresenter;
        this.appCMSPageViewAdapter = new AppCMSPageViewAdapter(context);
        this.shouldRefresh = true;
        this.ignoreScroll = false;
        this.pageId = pageId;
        init();
    }

    public void openViewInFullScreen(View view, ViewGroup viewParent) {
        shouldRefresh = false;
        if (fullScreenViewOriginalDimensions == null) {
            fullScreenViewOriginalDimensions = new ViewDimensions();
        }
        try {
            fullScreenViewOriginalDimensions.width = view.getLayoutParams().width;
            fullScreenViewOriginalDimensions.height = view.getLayoutParams().height;
        } catch (Exception e) {
            //
        }

        view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        view.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        childrenContainer.setVisibility(GONE);
        viewParent.removeView(view);

        LayoutParams adjustedLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        view.setLayoutParams(adjustedLayoutParams);

        addView(view);

        view.forceLayout();

        Log.d(TAG, "Video Player opened in fullscreen");
    }

    public void closeViewFromFullScreen(View view, ViewGroup viewParent) {
        shouldRefresh = true;
        if (view.getParent() == this && fullScreenViewOriginalDimensions != null) {
            removeView(view);

            view.getLayoutParams().width = fullScreenViewOriginalDimensions.width;
            view.getLayoutParams().height = fullScreenViewOriginalDimensions.height;

            viewParent.addView(view);
            childrenContainer.setVisibility(VISIBLE);

            getRootView().forceLayout();

            Log.d(TAG, "Video Player closed out fullscreen");
        }
    }

    @Override
    public void init() {
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        adapterList = new CopyOnWriteArrayList<>();
        createHeaderView();
        createConstraintHeaderView();
        if (!isInEditMode()) {
            ViewConfiguration vc = ViewConfiguration.get(getContext());
            minFlingVelocity = (float) vc.getScaledMinimumFlingVelocity();
            dragHelper = ViewDragHelper.create(this, new ViewDragCallback(this));
        }
    }

    private void createHeaderView() {
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        headerView = new FrameLayout(getContext());
        headerView.setLayoutParams(layoutParams);
    }

    private void createConstraintHeaderView() {
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        headerConstraintView = new ConstraintLayout(getContext());
        headerConstraintView.setId(R.id.page_view_constraint_header);
        headerConstraintView.setLayoutParams(layoutParams);
    }

    public void addListWithAdapter(ListWithAdapter listWithAdapter) {
        for (ListWithAdapter listWithAdapter1 : adapterList) {
            if (listWithAdapter.id.equals(listWithAdapter1.id)) {
                adapterList.remove(listWithAdapter1);
            }
        }
        adapterList.add(listWithAdapter);
    }

    public void notifyAdaptersOfUpdate() {
        for (ListWithAdapter listWithAdapter : adapterList) {
            if (listWithAdapter.getAdapter() instanceof AppCMSBaseAdapter) {
                ((AppCMSBaseAdapter) listWithAdapter.getAdapter())
                        .resetData(listWithAdapter.getListView());
            }
        }
    }

    public void updateDataList(List<ContentDatum> contentData, String id) {
        for (int i = 0; i < adapterList.size(); i++) {
            if (adapterList.get(i).id != null &&
                    adapterList.get(i).id.equals(id)) {
                if (adapterList.get(i).adapter instanceof AppCMSBaseAdapter) {
                    ((AppCMSBaseAdapter) adapterList.get(i).adapter)
                            .updateData(adapterList.get(i).listView, contentData);
                }
            }
        }
    }

    public void showModule(ModuleList module) {
        for (int i = 0; i < childrenContainer.getChildCount(); i++) {
            View child = childrenContainer.getChildAt(i);
            if (child instanceof ModuleView) {
                if (module == ((ModuleView) child).getModule()) {
                    child.setVisibility(VISIBLE);
                }
            }
        }
    }

    public void setAllChildrenVisible(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).setVisibility(VISIBLE);
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                setAllChildrenVisible((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    protected Component getChildComponent(int index) {
        return null;
    }

    @Override
    protected Layout getLayout() {
        return null;
    }

    public void reorderViews(View view) {
        removeView(mainView);
        addView(view);
        addView(mainView);

    }

    public void refreshApp() {
        appCMSPresenter.setMiniPLayerVisibility(true);
        if (shouldRefresh) {
            appCMSPresenter.clearPageAPIData(() -> {
                        Utils.position = 0;
                        mainView.setRefreshing(false);
                    },
                    true);
        }
    }

    @Override
    protected ViewGroup createChildrenContainer() {

        childrenContainer = new RecyclerView(getContext());
        childrenContainer.setId(R.id.home_nested_scroll_view);

       /* final Context context = childrenContainer.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        childrenContainer.setLayoutAnimation(controller);
        childrenContainer.scheduleLayoutAnimation();*/

        // childrenContainer.setDescendantFocusability(RecyclerView.FOCUS_BLOCK_DESCENDANTS);
        //childrenContainer.setFocusableInTouchMode(true);
        FrameLayout.LayoutParams nestedScrollViewLayoutParams =
                new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
        childrenContainer.setLayoutParams(nestedScrollViewLayoutParams);
        LinearLayoutManager parentManager=new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false);
        ((RecyclerView) childrenContainer).setLayoutManager(parentManager);
        ((RecyclerView) childrenContainer).setAdapter(appCMSPageViewAdapter);
        ((RecyclerView) childrenContainer).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onScrollChangeListener != null &&
                        recyclerView.isLaidOut() &&
                        !ignoreScroll) {
                    onScrollChangeListener.onScroll(dx, dy);
                    int currentPosition =
                            parentManager.findLastCompletelyVisibleItemPosition();
                    if (currentPosition < 0) {
                        currentPosition =
                                parentManager.findLastVisibleItemPosition();
                    }
                    if (0 <= currentPosition) {
                        onScrollChangeListener.setCurrentPosition(currentPosition);
                    }
                }

                ignoreScroll = false;


            }
        });

        mainView = new SwipeRefreshLayout(getContext());
        mainView.setId(R.id.fight_scroll_id);
        SwipeRefreshLayout.LayoutParams swipeRefreshLayoutParams =
                new SwipeRefreshLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT);
        mainView.setLayoutParams(swipeRefreshLayoutParams);
        mainView.addView(childrenContainer);
        mainView.setOnRefreshListener(() -> {
            if(appCMSPresenter.getAppPreference()!=null&&appCMSPresenter.getAppPreference().getShowPIPVisibility() ){
            appCMSPresenter.setMiniPLayerVisibility(true);}
            if (shouldRefresh) {
                appCMSPresenter.clearPageAPIData(() -> {
                            Utils.position = 0;
                            mainView.setRefreshing(false);
                        },
                        true);

            }
        });
        addView(mainView);
        return childrenContainer;
    }

    public boolean isUserLoggedIn() {
        return userLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        this.userLoggedIn = userLoggedIn;
    }

    public void addViewWithComponentId(ViewWithComponentId viewWithComponentId) {
        viewsWithComponentIds.add(viewWithComponentId);
    }

    public View findViewFromComponentId(String id) {
        if (!TextUtils.isEmpty(id)) {
            for (ViewWithComponentId viewWithComponentId : viewsWithComponentIds) {
                if (!TextUtils.isEmpty(viewWithComponentId.id) && viewWithComponentId.id.equals(id)) {
                    return viewWithComponentId.view;
                }
            }
        }
        return null;
    }

    public void clearExistingViewLists() {
        moduleViewMap.clear();
        viewsWithComponentIds.clear();
        appCMSPageViewAdapter.removeAllViews();
    }

    public void addModuleViewWithModuleId(String moduleId,
                                          ModuleView moduleView,
                                          boolean useModuleViewAsHeader,
                                          AppCMSUIKeyType blockName) {
        moduleViewMap.put(moduleId, moduleView);
        appCMSPageViewAdapter.addView(moduleView, blockName);
        if (useModuleViewAsHeader) {
            addView(moduleView);
        }
    }

    public ModuleView getModuleViewWithModuleId(String moduleId) {
        if (moduleViewMap.containsKey(moduleId)) {
            return moduleViewMap.get(moduleId);
        }
        return null;
    }

    public AppCMSPageUI getAppCMSPageUI() {
        return appCMSPageUI;
    }

    public void removeAllAddOnViews() {
        int index = 0;
        boolean removedChild = false;
        while (index < getChildCount() && !removedChild) {
            View child = getChildAt(index);
            if (child != mainView) {
                removeView(child);
                removedChild = true;
                removeAllAddOnViews();
            }
            index++;
        }
    }

    public void notifyAdapterDataSetChanged() {
        if (appCMSPageViewAdapter != null) {
            try {
                appCMSPageViewAdapter.notifyItemRangeChanged(1, appCMSPageViewAdapter.getItemCount());
                if (appCMSPageViewAdapter.getChildViews() != null && appCMSPageViewAdapter.getChildViews().size() > 0 &&
                        appCMSPageViewAdapter.getChildViews().get(0) != null &&
                        appCMSPageViewAdapter.getChildViews().get(0).getModule() != null &&
                        appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName() != null) {

                    if (!(appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_standalone_player_01))
                                    || appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_standalone_player_02))
                                    || appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_standalone_player_04))) &&
                            !appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_autoplay_04))) {
                        if (appCMSPresenter.relativeLayoutPIP != null && !appCMSPresenter.isPageSplashPage(pageId)) {
                            if (!appCMSPresenter.getIsMiniPlayerPlaying())
                                appCMSPresenter.showPopupWindowPlayer(false);
                            else
                                appCMSPresenter.showPopupWindowPlayer(true);
                        }

                        if (appCMSPresenter.videoPlayerView != null && appCMSPresenter.isPageLoginPage(pageId)) {
                            appCMSPresenter.videoPlayerView.hideMiniPlayer = true;
                            appCMSPresenter.dismissPopupWindowPlayer(true);
                        }
                    } else {
                        boolean pause = true;
                        if (appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_standalone_player_01))
                                || appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_standalone_player_02))
                                || appCMSPageViewAdapter.getChildViews().get(0).getModule().getBlockName().equalsIgnoreCase(getContext().getString(R.string.ui_block_standalone_player_04)))
                            pause = false;
                        appCMSPresenter.dismissPopupWindowPlayer(pause);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public View findChildViewById(int id) {
        if (appCMSPageViewAdapter != null) {
            return appCMSPageViewAdapter.findChildViewById(id);
        }
        return null;
    }

    public boolean shouldReparentChromecastButton() {
        return reparentChromecastButton;
    }

    public void setReparentChromecastButton(boolean reparentChromecastButton) {
        this.reparentChromecastButton = reparentChromecastButton;
    }

    public interface OnScrollChangeListener {
        void onScroll(int dx, int dy);

        void setCurrentPosition(int position);
    }

    public OnScrollChangeListener getOnScrollChangeListener() {
        return onScrollChangeListener;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public void scrollToPosition(int dx, int dy) {
        if (childrenContainer != null) {
            ignoreScroll = true;
            childrenContainer.scrollBy(dx, dy);
        }
    }

    public void scrollToPosition(int position) {
        if (childrenContainer != null) {
            if (position == 0) {
                childrenContainer.scrollBy(position, position);
            } else {
                ((RecyclerView) childrenContainer).smoothScrollToPosition(position);
            }
        }
    }

    private static class ViewDimensions {
        int width;
        int height;
    }

    public void addToHeaderView(View view) {
        headerView.addView(view);
        if (headerView.getParent() == null) {
            addView(headerView);
            headerView.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        }
    }

    public ConstraintLayout getHeaderConstraintView() {
        return headerConstraintView;
    }

    public void addToConstraintHeaderView(View view) {
        if (view instanceof CustomVideoPlayerView) {
            if (view.getParent() != null) {
                ViewGroup cl = ((ViewGroup) view.getParent());
                if(cl!=null)
                    cl.removeView(view);
            }
        }
        headerConstraintView.addView(view);
        if (headerConstraintView.getParent() == null) {
            addView(headerConstraintView);
            headerView.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        }
    }

    public void reorderConstraintHeader() {
        removeAllViews();
        LinearLayout layout = new LinearLayout(getContext());
        layout.setId(R.id.page_view_linear_layout);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(layoutParams);
        if(!appCMSPresenter.isNewsTemplate()) {
            LinearLayout swipeLayout = new LinearLayout(getContext());
            swipeLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            swipeLayout.setOrientation(LinearLayout.VERTICAL);
            mainView.removeView(childrenContainer);
            mainView.addView(swipeLayout);
            swipeLayout.addView(headerConstraintView);
            swipeLayout.addView(childrenContainer);
        }else {
            layout.addView(headerConstraintView);
        }
        layout.addView(mainView);
        addView(layout);
    }

    public View getMainView() {
        return mainView;
    }

    public  void disableSwipe() {
         mainView.setEnabled(false);
    }

    public void enableSwipe() {
        mainView.setEnabled(true);
    }

    public AppCMSPageViewAdapter getAppCMSPageViewAdapter() {
        return appCMSPageViewAdapter;
    }

    public void setAppCMSPageViewAdapter(AppCMSPageViewAdapter appCMSPageViewAdapter) {
        this.appCMSPageViewAdapter = appCMSPageViewAdapter;
    }

    public String getPageId() {
        return pageId;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper != null && dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    boolean isTouchOnHeaderView;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        boolean pullingDown = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                verticalTouchSlop = event.getY();
            case MotionEvent.ACTION_MOVE:
                final float dy = event.getY() - verticalTouchSlop;
                if (dy > dragHelper.getTouchSlop()) {
                    pullingDown = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                verticalTouchSlop   = 0.0f;
                break;
        }
        if (!dragHelper.shouldInterceptTouchEvent(event) && pullingDown) {
            if (dragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE &&
                    dragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL)) {
                View child = getChildAt(0);
                if (child != null && listener != null && !listener.onShouldInterceptTouchEvent()) {
                    dragHelper.captureChildView(child, event.getPointerId(0));
                    return dragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING;
                }
            }
        }
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                View view = findViewById(R.id.appLogoImage);
                isTouchOnHeaderView = view != null && view.getTop() <= event.getY() && view.getBottom() >= event.getY();
                Log.e("okk",""+isTouchOnHeaderView);
            }
            break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouchOnHeaderView = false;
                break;
        }
        return  isTouchOnHeaderView && dragHelper.getCapturedView() != null;
    }
    public void setMinFlingVelocity(float velocity) {
        minFlingVelocity = velocity;
    }
    public void setAnimateAlpha(boolean b) {
        animateAlpha = b;
    }
    public void setListener(Listener l) {
        listener = l;
    }
    private static class ViewDragCallback extends ViewDragHelper.Callback {
        private PageView pullDismissLayout;
        private int startTop;
        private float dragPercent;
        private View capturedView;
        private boolean dismissed;
        private ViewDragCallback(PageView layout) {
            pullDismissLayout = layout;
            dragPercent = 0.0F;
            dismissed = false;
        }
        public boolean tryCaptureView(@NonNull View view, int i) {
            return true;
        }
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return Math.max(0, top);
        }
        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return pullDismissLayout.getHeight();
        }
        public void onViewCaptured(View view, int activePointerId) {
            capturedView = view;
            startTop = view.getTop();
            dragPercent = 0.0F;
            dismissed = false;
        }
        @SuppressLint({"NewApi"})
        public void onViewPositionChanged(@NonNull View view, int left, int top, int dx, int dy) {
            int range = pullDismissLayout.getHeight();
            int moved = Math.abs(top - startTop);
            if (range > 0) {
                dragPercent = (float) moved / (float) range;
            }
            if (pullDismissLayout.animateAlpha) {
                //view.setAlpha(1.0F - dragPercent);
                view.getBackground().setAlpha(Math.round((1 - dragPercent) * 255));
                //pullDismissLayout.invalidate();
            }
        }
        public void onViewDragStateChanged(int state) {
            if (capturedView != null && dismissed && state == ViewDragHelper.STATE_IDLE) {
                pullDismissLayout.removeView(capturedView);
                if (pullDismissLayout.listener != null) {
                    pullDismissLayout.listener.onDismissed();
                }
            }
        }
        public void onViewReleased(View view, float xv, float yv) {
            dismissed = dragPercent >= 0.50F || (Math.abs(xv) > pullDismissLayout.minFlingVelocity && dragPercent > 0.20F);
            int finalTop = dismissed ? pullDismissLayout.getHeight() : startTop;
            pullDismissLayout.dragHelper.settleCapturedViewAt(0, finalTop);
            pullDismissLayout.invalidate();
        }
    }
    public interface Listener {
        /**
         * Layout is pulled down to dismiss
         * Good time to finish activity, remove fragment or any view
         */
        void onDismissed();
        /**
         * Convenient method to avoid layout overriding event
         * If you have a RecyclerView or ScrollerView in our layout your can
         * avoid PullDismissLayout to handle event.
         *
         * @return true when ignore pull down event, f
         * false for allow PullDismissLayout handle event
         */
        boolean onShouldInterceptTouchEvent();
    }


    public void addBottomFloatingButton(ExtendedFloatingActionButton button) {
        FrameLayout.LayoutParams params = new LayoutParams(button.getWidth(), button.getHeight());
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        params.setMargins(0,0,0,20);
        button.setLayoutParams(params);
        addView(button);
    }

}

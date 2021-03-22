package com.viewlift.tv.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.AppCMSTransactionDataValue;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.model.BrowseFragmentRowData;
import com.viewlift.tv.utility.Utils;
import com.viewlift.tv.views.customviews.RecyclerViewItemDecoration;
import com.viewlift.tv.views.customviews.TVPageView;

public class AppCMSVerticalGridFragment extends VerticalGridSupportFragment {

    private final String TAG = AppCMSVerticalGridFragment.class.getName();
    private static final int NUM_COLUMNS = 1;
    private ArrayObjectAdapter mRowsAdapter;
    private TVPageView pageView;
    private View view;
    private AppCMSPresenter appCMSPresenter;
    private ContentDatum data = null;
    private BrowseFragmentRowData rowData = null;
    private String screenName;
    long clickedTime;


    public static AppCMSVerticalGridFragment newInstance(Context context) {
        AppCMSVerticalGridFragment appCMSVerticalGridFragment = new AppCMSVerticalGridFragment();
        return appCMSVerticalGridFragment;
    }

    public void setPageView(TVPageView tvPageView) {
        pageView = tvPageView;
    }

    public void setmRowsAdapter(ArrayObjectAdapter rowsAdapter) {
        this.mRowsAdapter = rowsAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        View browseGrid = view.findViewById(R.id.browse_grid);
        if (browseGrid != null) browseGrid.setFocusable(false);
        View gridFrame = view.findViewById(R.id.grid_frame);
        if (gridFrame != null) gridFrame.setFocusable(false);
        ((RecyclerView) browseGrid).addItemDecoration(new RecyclerViewItemDecoration(12, RecyclerViewItemDecoration.VERTICAL));
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false);
        gridPresenter.setShadowEnabled(false);
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != mRowsAdapter) {
            setAdapter(mRowsAdapter);
        }

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder,
                                  Object item,
                                  RowPresenter.ViewHolder rowViewHolder,
                                  Row row) {
            if (null != item && item instanceof BrowseFragmentRowData) {
                BrowseFragmentRowData rowData = (BrowseFragmentRowData) item;
                ContentDatum data = rowData.contentData;
                String action = rowData.action;
                if (appCMSPresenter.getAppCMSMain().getFeatures().isTrickPlay()) {
                    action = getString(R.string.app_cms_action_watchvideo_key);
                }

                if (rowData.contentData.getGist() == null)
                    return;

                if (action != null && action.equalsIgnoreCase(getString(R.string.app_cms_action_watchvideo_key))) {
                    pushedPlayKey();
                } else {
                    if ((null != rowData.contentData.getGist().getContentType() &&
                            rowData.contentData.getGist().getContentType().equalsIgnoreCase("SERIES"))) {
                        action = "showDetailPage";
                    } else if (rowData.contentData.getGist().getContentType() != null
                            && rowData.contentData.getGist().getContentType().equalsIgnoreCase("BUNDLE")) {
                        action = "bundleDetailPage";
                    }
                    String permalink = data.getGist().getPermalink();

                    String title = data.getGist().getTitle();

                    if (!appCMSPresenter.launchTVButtonSelectedAction(permalink,
                            action,
                            title,
                            null,
                            data,
                            false,
                            -1,
                            null,
                            null)) {

                    }
                }
                itemViewHolder.view.setClickable(false);
                new Handler().postDelayed(() -> itemViewHolder.view.setClickable(true), 2000);
            }

            /*mRowsAdapter.remove(item);
            mRowsAdapter.notifyArrayItemRangeChanged(0, mRowsAdapter.size());*/

        }
    }

    class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder,
                                   Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            /*if (item instanceof BrowseFragmentRowData) {
                rowData = (BrowseFragmentRowData) item;
                if (rowData != null) {
                    if (getActivity() instanceof AppCmsHomeActivity) {
                        ((AppCmsHomeActivity) getActivity()).shouldShowLeftNavigation(true);
                    }
                }
            }*/
        }
    }

    public void pushedPlayKey() {
        if (null != rowData
                && null != rowData.contentData
                && null != rowData.contentData.getGist()
                && !rowData.isPlayerComponent) {
            Utils.pageLoading(true, getActivity());
            ContentDatum contentDatum = rowData.contentData;
            String filmId = contentDatum.getGist().getId();
            String permaLink = contentDatum.getGist().getPermalink();
            String title = contentDatum.getGist().getTitle();

            long diff = System.currentTimeMillis() - clickedTime;
            if (diff > 2000) {
                clickedTime = System.currentTimeMillis();
                if (null != contentDatum.getGist().getContentType() &&
                        contentDatum.getGist().getContentType().equalsIgnoreCase("SERIES")) {
                    appCMSPresenter.launchTVButtonSelectedAction(
                            contentDatum.getGist().getPermalink(),
                            "showDetailPage",
                            contentDatum.getGist().getTitle(),
                            null,
                            contentDatum,
                            false,
                            -1,
                            null,
                            null);
                } else {
                    if (contentDatum.getPricing() != null) {
                        if ("TVOD".equalsIgnoreCase(contentDatum.getPricing().getType())) {
                            appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                if (maps != null
                                        && maps.get(0) != null
                                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                                    AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());


                                    if (appCMSTransactionDataValue.getTransactionEndDate() != 0) {
                                        ClearDialogFragment clearDialogFragment = Utils.getClearDialogFragment(
                                                getContext(),
                                                appCMSPresenter,
                                                getContext().getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                                getContext().getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                                null,
                                                appCMSPresenter.getLanguageResourcesFile().getStringValue(getString(R.string.rent_start_msg), String.valueOf((int) appCMSTransactionDataValue.getRentalPeriod())),
                                                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.label_play)),
                                                appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cancel)),
                                                 13f
                                        );

                                        clearDialogFragment.setOnPositiveButtonClicked(s -> {
                                            appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                    0,
                                                    rowData.relatedVideoIds,
                                                    contentDatum.getGist().getWatchedTime(),
                                                    null);
                                        });
                                        appCMSPresenter.showLoadingDialog(false);
                                    } else {
                                        appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                                0,
                                                rowData.relatedVideoIds,
                                                contentDatum.getGist().getWatchedTime(),
                                                null);
                                    }
                                } else {
                                    Utils.getClearDialogFragment(
                                            getContext(),
                                            appCMSPresenter,
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                            null,
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cannot_purchase_msg)),
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.back)),
                                            null,
                                             10f
                                    );
                                    appCMSPresenter.showLoadingDialog(false);
                                }
                            }, contentDatum.getGist().getContentType());
                        } else if ("PPV".equalsIgnoreCase(contentDatum.getPricing().getType())) {
                            appCMSPresenter.getTransactionData(contentDatum.getGist().getId(), maps -> {
                                if (maps != null
                                        && maps.get(0) != null
                                        && maps.get(0).get(contentDatum.getGist().getId()) != null) {
                                    AppCMSTransactionDataValue appCMSTransactionDataValue = maps.get(0).get(contentDatum.getGist().getId());
                                    appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                            0,
                                            rowData.relatedVideoIds,
                                            contentDatum.getGist().getWatchedTime(),
                                            null);
                                } else {
                                    Utils.getClearDialogFragment(
                                            getContext(),
                                            appCMSPresenter,
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_width),
                                            getResources().getDimensionPixelSize(R.dimen.text_clear_dialog_height),
                                            null,
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.cannot_purchase_msg)),
                                            appCMSPresenter.getLanguageResourcesFile().getUIresource(getString(R.string.back)),
                                            null,
                                             10f
                                    );
                                    appCMSPresenter.showLoadingDialog(false);
                                }
                            }, contentDatum.getGist().getContentType());
                        } else {
                            appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                    0,
                                    rowData.relatedVideoIds,
                                    contentDatum.getGist().getWatchedTime(),
                                    null);
                        }
                    } else {
                        appCMSPresenter.launchTVVideoPlayer(contentDatum,
                                0,
                                rowData.relatedVideoIds,
                                contentDatum.getGist().getWatchedTime(),
                                null);
                    }
                }
            } else {
                appCMSPresenter.showLoadingDialog(false);
            }
        }
    }

    public boolean hasFocus() {
        return (null != view && view.hasFocus());
    }

}

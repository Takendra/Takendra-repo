package com.viewlift.views.fragments;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.downloads.DownloadStatus;
import com.viewlift.models.data.appcms.ui.main.LocalisedStrings;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;

import javax.inject.Inject;

/*
 * Created by ram.kailash on 10/10/2017.
 */

public class AppCMSTrayMenuDialogFragment extends DialogFragment implements View.OnClickListener {

    @Inject
    AppPreference appPreference;
    @Inject
    AppCMSPresenter appCMSPresenter;
    @Inject
    LocalisedStrings localisedStrings;

    private ContentDatum contentDatum;
    private boolean isAdded, isDownloaded, isPending;
    private TrayMenuClickListener trayMenuClickListener;
    AppCompatButton addToWatchList;
    AppCompatButton downloadBtn;
    AppCompatButton closeBtn;

    public static AppCMSTrayMenuDialogFragment newInstance(boolean isAdded, ContentDatum contentDatum) {
        AppCMSTrayMenuDialogFragment appCMSTrayMenuDialogFragment = new AppCMSTrayMenuDialogFragment();
        appCMSTrayMenuDialogFragment.isAdded = isAdded;
        appCMSTrayMenuDialogFragment.contentDatum = contentDatum;
        return appCMSTrayMenuDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    }

    public void setMoreClickListener(TrayMenuClickListener moreClickListener) {
        this.trayMenuClickListener = moreClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.more_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        addToWatchList = view.findViewById(R.id.moreDialogAddToWatchListBtn);
        downloadBtn = view.findViewById(R.id.moreDialogDownloadBtn);
        closeBtn = view.findViewById(R.id.moreDialogCloseBtn);
        closeBtn.setText(localisedStrings.getCloseText());
        //addToWatchList.setText(isAdded ? "REMOVE TO WATCHLIST" : "ADD TO WATCHLIST");
        addToWatchList.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        addToWatchList.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));

        if (isAdded)
            addToWatchList.setText(localisedStrings.getRemoveFromWatchlistText());
        else
            addToWatchList.setText(localisedStrings.getAddToWatchlistText());
        addToWatchList.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                .getCta().getPrimary().getBackgroundColor()));
        addToWatchList.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                .getCta().getPrimary().getTextColor()));
        //isDownloaded = appCMSPresenter.isVideoDownloaded(contentDatum.getGist().getId());
        //downloadBtn.setVisibility(isDownloaded?View.GONE:View.VISIBLE);

        if (contentDatum != null && contentDatum.getGist() != null && contentDatum.getGist().getId() != null) {
            appCMSPresenter.getUserVideoDownloadStatus(contentDatum.getGist().getId(),
                    videoDownloadStatus -> {
                        if (videoDownloadStatus != null) {
                            if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_COMPLETED ||
                                    videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_SUCCESSFUL) {
                                isDownloaded = true;
                            }
                            if (videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_RUNNING ||
                                    videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PAUSED ||
                                    videoDownloadStatus.getDownloadStatus() == DownloadStatus.STATUS_PENDING) {

                                isPending = true;
                            }

                        }
                    },
                    appPreference.getLoggedInUser());
        }

        if (!isDownloaded && !isPending) {
            downloadBtn.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                    .getCta().getPrimary().getBackgroundColor()));
            downloadBtn.setTextColor(Color.parseColor(appCMSPresenter.getAppCMSMain().getBrand()
                    .getCta().getPrimary().getTextColor()));
            downloadBtn.setOnClickListener(this);
        } else {
            downloadBtn.setBackgroundColor(Color.GRAY);
            downloadBtn.setText(isDownloaded ? localisedStrings.getDownloadedLabelText() : localisedStrings.getDownloadingLabelText());
            downloadBtn.setActivated(false);
            downloadBtn.setOnClickListener(null);
        }
        //downloadBtn.setVisibility(isDownloaded ? View.GONE : View.VISIBLE);
        //downloadBtn.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppBackgroundColor()));
        //downloadBtn.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        closeBtn.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));

        if (!appCMSPresenter.isDownloadable()) {
            downloadBtn.setVisibility(View.GONE);
        }
        addToWatchList.setOnClickListener(this);


        GradientDrawable gd = new GradientDrawable();
        gd.setColor(appCMSPresenter.getGeneralBackgroundColor()); // Changes this drawbale to use a single color instead of a gradient
        gd.setStroke(5, appCMSPresenter.getGeneralTextColor());

        closeBtn.setTextColor(appCMSPresenter.getGeneralTextColor());
        closeBtn.setBackground(gd);
        closeBtn.setOnClickListener(this);
        setFont();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.moreDialogAddToWatchListBtn) {
            dismiss();
            if (trayMenuClickListener != null)
                trayMenuClickListener.addToWatchListClick(!isAdded, contentDatum);
        } else if (v.getId() == R.id.moreDialogDownloadBtn) {
            dismiss();
            if (trayMenuClickListener != null)
                trayMenuClickListener.downloadClick(contentDatum);
        } else {
            dismiss();
        }
    }

    private void setFont() {
        Component component = new Component();
        ViewCreator.setTypeFace(closeBtn.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, downloadBtn);
        ViewCreator.setTypeFace(closeBtn.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, addToWatchList);
        ViewCreator.setTypeFace(closeBtn.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, closeBtn);
    }

    public interface TrayMenuClickListener {

        void addToWatchListClick(boolean isAddedOrNot, ContentDatum contentDatum);

        void downloadClick(ContentDatum contentDatum);
    }
}

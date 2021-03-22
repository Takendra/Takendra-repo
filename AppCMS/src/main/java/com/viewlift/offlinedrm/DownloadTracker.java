/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viewlift.offlinedrm;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadCursor;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadIndex;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashUtil;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.WideVine;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.utils.CommonUtils;
import com.viewlift.utils.FileUtils;
import com.viewlift.views.customviews.BaseView;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.inject.Inject;

import static com.google.android.exoplayer2.offline.DownloadHelper.getDefaultTrackSelectorParameters;

/** Tracks media that has been downloaded. */
public class DownloadTracker {

  /** Listens for changes in the tracked downloads. */
  public interface Listener {

    /** Called when the tracked downloads changed. */
    void onDownloadsChanged();
  }

  private static final String TAG = "DownloadTracker";

  private final Context context;
  private final DataSource.Factory dataSourceFactory;
  private final CopyOnWriteArraySet<Listener> listeners;
  private final HashMap<Uri, Download> downloads;
  private final HashMap<String, Download> offlineDownloads;
  private final DownloadIndex downloadIndex;
  @Inject
  AppCMSPresenter appCMSPresenter;

  @Nullable private StartDownloadDialogHelper startDownloadDialogHelper;

  public DownloadTracker(
          Context context, DataSource.Factory dataSourceFactory, DownloadManager downloadManager) {
    this.context = context.getApplicationContext();
    this.dataSourceFactory = dataSourceFactory;
    listeners = new CopyOnWriteArraySet<>();
    downloads = new HashMap<>();
    offlineDownloads = new HashMap<>();
    downloadIndex = downloadManager.getDownloadIndex();
    downloadManager.addListener(new DownloadManagerListener());
    ((AppCMSApplication) context.getApplicationContext()).getAppCMSPresenterComponent().inject(this);
    loadDownloads();
  }

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  public void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  public DownloadRequest isVideoOfflineDownloaded(String videoId){
    String videoDownloadID = videoId+"-"+appCMSPresenter.getLoggedInUser();
    Download download = offlineDownloads.get(videoDownloadID);
    return download != null && download.state != Download.STATE_FAILED && download.state == Download.STATE_COMPLETED ? download.request : null;
  }

  public Download getDowloadedVideoObject(String videoId){
    String videoDownloadID = videoId+"-"+appCMSPresenter.getLoggedInUser();
    Download  download = offlineDownloads.get(videoDownloadID);
    return download;
  }

  //Remove Video Object which is Failed or Corrupted.
  public void removeDownlodedObject(String removeVideoID){
    String removeFilmID = removeVideoID+"-"+appCMSPresenter.getLoggedInUser();
    DownloadService.sendRemoveDownload(context, OfflineDownloadService.class, removeFilmID, /* foreground= */ false);
  }

  //Remove Video Object which is Failed or Corrupted.
  public void videoSearchDownlodedObject(String removeVideoID){}

  public byte[] getOfflicenseKeyID(String videoID){
      Download download = offlineDownloads.get(videoID);
      return download.request.data;
  }

  public HashMap<String, Download> getAllOfflineDownloads(){
    return offlineDownloads;
  }

  ContentDatum contentDatum;
  AppCompatActivity currentActivity;
  View updateView;
  View.OnClickListener onClickListener;
  public void toggleDownload(
          AppCompatActivity appCompatActivity,
          ContentDatum contentDatum,
          FragmentManager fragmentManager,
          String name,
          Uri uri,
          String extension,
          RenderersFactory renderersFactory,
          View view, View.OnClickListener onClickListener) {

      this.currentActivity=appCompatActivity;
      this.contentDatum = contentDatum;
      this.updateView =view;
      this.onClickListener = onClickListener;
      downloadOfflineLicense(fragmentManager, name, uri, extension, renderersFactory, contentDatum.getGist().getId());
  }

  OfflineLicenseHelper<FrameworkMediaCrypto> offlineLicenseHelper = null;
  byte[] offlineLicenseKeySetId;

  public void downloadOfflineLicense(FragmentManager fragmentManager, String name, Uri uri, String extension, RenderersFactory renderersFactory, String mediaId){
    new Thread() {
      @Override
      public void run() {
        {
          try {
            WideVine wideVine = contentDatum.getStreamingInfo().getVideoAssets().getWideVine();
            DefaultHttpDataSourceFactory httpDataSourceFactory;
            httpDataSourceFactory = new DefaultHttpDataSourceFactory(CommonUtils.getUserAgent(appCMSPresenter));
            HashMap<String, String> keyRequestProperties = new HashMap<>();
            keyRequestProperties.put(context.getString(R.string.drm_token_key_axinom), wideVine.getLicenseToken());
            try {
              offlineLicenseHelper = OfflineLicenseHelper.newWidevineInstance(wideVine.getLicenseUrl(), false, httpDataSourceFactory, keyRequestProperties);
            } catch (Exception e) {
              e.printStackTrace();
            }
            DrmInitData drmInitData = null;
            DataSource dataSource = httpDataSourceFactory.createDataSource();
            DashManifest dashManifest;
            dashManifest = DashUtil.loadManifest(dataSource, Uri.parse(wideVine.getUrl()));
            drmInitData = DashUtil.loadDrmInitData(dataSource, dashManifest.getPeriod(0));
            offlineLicenseKeySetId = offlineLicenseHelper.downloadLicense(drmInitData);
            //Continue After License is Downloaded.
            currentActivity.runOnUiThread(() -> {
              try {

                appCMSPresenter.saveOfflineVideoKeys(mediaId, offlineLicenseKeySetId);
                //Thread Ends POST LICENSE DOWNLOAADED
                if (startDownloadDialogHelper != null) {
                  startDownloadDialogHelper.release();
                }
                appCMSPresenter.stopDownloadProgressDialog();
                startDownloadDialogHelper = new StartDownloadDialogHelper(fragmentManager, getDownloadHelper(uri, extension, renderersFactory), name, onClickListener);
              } catch (Exception ignored) {
              }
            });
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }.start();
  }


  private void loadDownloads() {
    try (DownloadCursor loadedDownloads = downloadIndex.getDownloads()) {
      while (loadedDownloads.moveToNext()) {
        Download download = loadedDownloads.getDownload();
        downloads.put(download.request.uri, download);
        offlineDownloads.put(download.request.id, download);
      }
    } catch (IOException e) {
      Log.w(TAG, "Failed to query downloads", e);
    }
  }

  private DownloadHelper getDownloadHelper(Uri uri, String extension, RenderersFactory renderersFactory) {
    int type = Util.inferContentType(uri, extension);
    switch (type) {
      case C.TYPE_DASH:
        UUID drmSchemeUuid = Util.getDrmUuid(context.getString(R.string.drm_scheme));
        try {
//          return DownloadHelper.forDash(uri, dataSourceFactory, renderersFactory);
          return DownloadHelper.forDash(uri, dataSourceFactory, renderersFactory, buildDrmSessionManagerV18(drmSchemeUuid,
              contentDatum.getStreamingInfo().getVideoAssets().getWideVine().getLicenseUrl(), null, false), getDefaultTrackSelectorParameters(context), context);
        } catch (Exception e) {
          e.printStackTrace();
        }
      case C.TYPE_SS:
        return DownloadHelper.forSmoothStreaming(uri, dataSourceFactory, renderersFactory);
      case C.TYPE_HLS:
        return DownloadHelper.forHls(uri, dataSourceFactory, renderersFactory);
      case C.TYPE_OTHER:
        return DownloadHelper.forProgressive(uri);
      default:
        throw new IllegalStateException("Unsupported type: " + type);
    }
  }

  private DefaultDrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
      throws UnsupportedDrmException {
    FrameworkMediaDrm mediaDrm;
    HttpDataSource.Factory licenseDataSourceFactory = new DefaultHttpDataSourceFactory(CommonUtils.getUserAgent(appCMSPresenter));

    HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, licenseDataSourceFactory);
    if (keyRequestPropertiesArray != null) {
      for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
        drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1]);
      }
    }

    //drmCallback.setKeyRequestProperty("X-AxDRM-Message","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ2ZXJzaW9uIjoxLCJjb21fa2V5X2lkIjoiZDQ5YTEwZjktZmZjMy00ODhjLWI2NGEtYWIwNjAwYTE1NmJkIiwibWVzc2FnZSI6eyJ0eXBlIjoiZW50aXRsZW1lbnRfbWVzc2FnZSIsInZlcnNpb24iOjIsImxpY2Vuc2UiOnsic3RhcnRfZGF0ZXRpbWUiOiIyMDIwLTA0LTA4VDAxOjIzOjQyLjE3M1oiLCJleHBpcmF0aW9uX2RhdGV0aW1lIjoiMjEzNC0wNi0wN1QxNzoyMzo0Mi4xODJaIiwiYWxsb3dfcGVyc2lzdGVuY2UiOnRydWV9LCJjb250ZW50X2tleXNfc291cmNlIjp7ImlubGluZSI6W3siaWQiOiIwNjA5NmIyMi0xNWE2LTQ2M2YtOTUxYi02ZTY4N2I4N2MwY2YiLCJ1c2FnZV9wb2xpY3kiOiJQb2xpY3kgQSJ9XX0sImNvbnRlbnRfa2V5X3VzYWdlX3BvbGljaWVzIjpbeyJuYW1lIjoiUG9saWN5IEEiLCJwbGF5cmVhZHkiOnsibWluX2RldmljZV9zZWN1cml0eV9sZXZlbCI6MjAwMCwicGxheV9lbmFibGVycyI6WyI3ODY2MjdEOC1DMkE2LTQ0QkUtOEY4OC0wOEFFMjU1QjAxQTciXX0sIndpZGV2aW5lIjp7ImRldmljZV9zZWN1cml0eV9sZXZlbCI6IlNXX1NFQ1VSRV9DUllQVE8ifX1dfSwiYmVnaW5fZGF0ZSI6IjIwMjAtMDQtMDhUMDE6MjM6NDIuMTczWiIsImV4cGlyYXRpb25fZGF0ZSI6IjIwMjAtMDQtMDhUMDE6Mjg6NDIuMTc0WiJ9.N1DU0DOQVD9AvZtWEMSimSLVvOIvYhq1t866F3o9BNc");
    mediaDrm = FrameworkMediaDrm.newInstance(uuid);
    return new DefaultDrmSessionManager<>(uuid, mediaDrm, drmCallback, null, multiSession);
  }




  private class DownloadManagerListener implements DownloadManager.Listener {
    @Override
    public void onDownloadChanged(DownloadManager downloadManager, Download download) {
      downloads.put(download.request.uri, download);
      offlineDownloads.put(download.request.id, download);
      for (Listener listener : listeners) {
        listener.onDownloadsChanged();
      }
    }

    @Override
    public void onDownloadRemoved(DownloadManager downloadManager, Download download) {
      downloads.remove(download.request.uri);
      offlineDownloads.remove(download.request.id);
      for (Listener listener : listeners) {
        listener.onDownloadsChanged();
      }
    }

    @Override
    public void onInitialized(DownloadManager downloadManager) {

    }

  }

  private final class StartDownloadDialogHelper
      implements DownloadHelper.Callback,
          DialogInterface.OnClickListener,
          DialogInterface.OnDismissListener {

    private final FragmentManager fragmentManager;
    private final DownloadHelper downloadHelper;
    private final String name;

    private TrackSelectionDialog trackSelectionDialog;
    private MappedTrackInfo mappedTrackInfo;
    View.OnClickListener onClickListener;

    public StartDownloadDialogHelper(
            FragmentManager fragmentManager, DownloadHelper downloadHelper, String name, View.OnClickListener onClickListener) {
      this.fragmentManager = fragmentManager;
      this.downloadHelper = downloadHelper;
      this.name = name;
      this.onClickListener = onClickListener;
      downloadHelper.prepare(this);
    }

    public void release() {
      downloadHelper.release();
      if (trackSelectionDialog != null) {
        trackSelectionDialog.dismiss();
      }
    }

    // DownloadHelper.Callback implementation.

    @Override
    public void onPrepared(DownloadHelper helper) {

      Log.d("offlinedrm", "START");
      for (int i = 0; i < downloadHelper.getPeriodCount(); i++) {
        TrackGroupArray trackGroups = downloadHelper.getTrackGroups(i);
        for (int j = 0; j < trackGroups.length; j++) {
          TrackGroup trackGroup = trackGroups.get(j);
          for (int k = 0; k < trackGroup.length; k++) {
            Format track = trackGroup.getFormat(k);
            Log.d("offlinedrm", i+"."+j+"."+k);
          }
        }
      }
      Log.d("offlinedrm", "END");

      if (helper.getPeriodCount() == 0) {
        Log.d(TAG, "No periods found. Downloading entire stream.");
        startDownload();
        downloadHelper.release();
        return;
      }


      mappedTrackInfo = downloadHelper.getMappedTrackInfo(/* periodIndex= */ 0);

      if (!TrackSelectionDialog.willHaveContent(mappedTrackInfo)) {
        Log.d(TAG, "No dialog content. Downloading entire stream.");
        startDownload();
        downloadHelper.release();
        return;
      }

      trackSelectionDialog =
          TrackSelectionDialog.createForMappedTrackInfoAndParameters(
              /* titleId= */ com.google.android.exoplayer2.ui.R.string.exo_download_description,
              mappedTrackInfo,
              /* initialParameters= */ DownloadHelper.DEFAULT_TRACK_SELECTOR_PARAMETERS,
              /* allowAdaptiveSelections =*/ false,
              /* allowMultipleOverrides= */ true,
              /* onClickListener= */ this,
              /* onDismissListener= */ this);
      trackSelectionDialog.show(fragmentManager, /* tag= */ null);
    }

    @Override
    public void onPrepareError(DownloadHelper helper, IOException e) {
      Toast.makeText(
              context.getApplicationContext(), R.string.download_start_error, Toast.LENGTH_LONG)
          .show();
      Log.e(TAG, "Failed to start download", e);
    }

    // DialogInterface.OnClickListener implementation.

    @Override
    public void onClick(DialogInterface dialog, int which) {
      for (int periodIndex = 0; periodIndex < downloadHelper.getPeriodCount(); periodIndex++) {
        downloadHelper.clearTrackSelections(periodIndex);
        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
          if (!trackSelectionDialog.getIsDisabled(/* rendererIndex= */ i)) {
            downloadHelper.addTrackSelectionForSingleRenderer(
                periodIndex,
                /* rendererIndex= */ i,
                DownloadHelper.DEFAULT_TRACK_SELECTOR_PARAMETERS,
                trackSelectionDialog.getOverrides(/* rendererIndex= */ i));
          }
        }
      }

      if(contentDatum!=null && contentDatum.getGist().getId()!=null)
        removeDownlodedObject(contentDatum.getGist().getId());

      DownloadRequest downloadRequest = buildDownloadRequest();
      if (downloadRequest.streamKeys.isEmpty()) {
        // All tracks were deselected in the dialog. Don't start the download.
        return;
      }
      new Handler(Looper.getMainLooper()).post(()->{appCMSPresenter.showDownloadProgressDialog();});

      startDownload(downloadRequest);
    }

    // DialogInterface.OnDismissListener implementation.
    @Override
    public void onDismiss(DialogInterface dialogInterface) {
      trackSelectionDialog = null;
      updateView.setOnClickListener(onClickListener);
      downloadHelper.release();
    }

    // Internal methods.

    private void startDownload() {
      startDownload(buildDownloadRequest());
    }

    private void startDownload(DownloadRequest downloadRequest) {
      DownloadService.sendAddDownload(context, OfflineDownloadService.class, downloadRequest, /* foreground= */ false);
      int radiusDifference=7;
      if (BaseView.isTablet(context)) {
        radiusDifference = 4;
      }
      if (contentDatum.getMediaType() != null &&
              contentDatum.getContentData().get(0).getGist().getMediaType().toLowerCase().contains(context.getString(R.string.media_type_audio).toLowerCase())) {
        radiusDifference = 5;
        if (BaseView.isTablet(context)) {
          radiusDifference = 3;
        }
      }
      if(!appCMSPresenter.isNewsTemplate()) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
          appCMSPresenter.sendRefreshPageAction();
          appCMSPresenter.stopDownloadProgressDialog();
        }, 3000);
      }else {
        appCMSPresenter.stopDownloadProgressDialog();
      }

      appCMSPresenter.showToast(appCMSPresenter.getLocalisedStrings().getDownloadStartedText(contentDatum.getGist().getTitle()), Toast.LENGTH_LONG);
      // appCMSPresenter.showToast(appCMSPresenter.getLocalisedStrings().getDownloadStartedText(contentDatum.getGist().getTitle()), Toast.LENGTH_LONG);
      appCMSPresenter.updateOfflineVideoStatus(contentDatum.getGist().getId(), updateView, radiusDifference, false, onClickListener, null, null);
    }

    private DownloadRequest buildDownloadRequest() {
      OfflineVideoData offlineVideoData = appCMSPresenter.createOfflineDataObject(contentDatum, offlineLicenseKeySetId);
      byte[] offlineVideoDataBytes = new byte[10];
      try {
        offlineVideoDataBytes = appCMSPresenter.serialize(offlineVideoData);
      } catch (IOException e) {
        e.printStackTrace();
      }
      String videoDownloadID = contentDatum.getGist().getId()+"-"+appCMSPresenter.getLoggedInUser();
      return downloadHelper.getDownloadRequest(videoDownloadID, offlineVideoDataBytes);
    }

  }
}

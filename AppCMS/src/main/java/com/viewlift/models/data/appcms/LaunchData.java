package com.viewlift.models.data.appcms;

import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.verimatrix.VerimatrixResponse;
import com.viewlift.presenters.AppCMSActionType;

import java.util.List;

public class LaunchData {
    ContentDatum contentDatum;
    boolean isTrailer;
    boolean isVideoOffline;
    String pagePath;
    String action;
    String filmTitle;
    String[] extraData;
    boolean closeLauncher;
    int currentlyPlayingIndex;
    List<String> relateVideoIds;
    AppCMSActionType actionType;
    VerimatrixResponse verimatrixResponse;



    public LaunchData(ContentDatum contentDatum, boolean isTrailer, boolean isVideoOffline, String pagePath, String action, String filmTitle, String[] extraData, boolean closeLauncher, int currentlyPlayingIndex, List<String> relateVideoIds, AppCMSActionType actionType,
                      VerimatrixResponse verimatrixResponse) {
        this.contentDatum = contentDatum;
        this.isTrailer = isTrailer;
        this.isVideoOffline = isVideoOffline;
        this.pagePath = pagePath;
        this.action = action;
        this.filmTitle = filmTitle;
        this.extraData = extraData;
        this.closeLauncher = closeLauncher;
        this.currentlyPlayingIndex = currentlyPlayingIndex;
        this.relateVideoIds = relateVideoIds;
        this.actionType = actionType;
        this.verimatrixResponse = verimatrixResponse;
    }


    public ContentDatum getContentDatum() {
        return contentDatum;
    }

    public boolean isTrailer() {
        return isTrailer;
    }

    public boolean isVideoOffline() {
        return isVideoOffline;
    }

    public String getPagePath() {
        return pagePath;
    }

    public String getAction() {
        return action;
    }

    public String getFilmTitle() {
        return filmTitle;
    }

    public String[] getExtraData() {
        return extraData;
    }

    public boolean isCloseLauncher() {
        return closeLauncher;
    }

    public int getCurrentlyPlayingIndex() {
        return currentlyPlayingIndex;
    }

    public List<String> getRelateVideoIds() {
        return relateVideoIds;
    }

    public AppCMSActionType getActionType() {
        return actionType;
    }

    public VerimatrixResponse getVerimatrixResponse() {
        return verimatrixResponse;
    }
}

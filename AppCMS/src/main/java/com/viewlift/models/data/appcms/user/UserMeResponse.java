package com.viewlift.models.data.appcms.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.vimeo.stag.UseStag;

import java.util.ArrayList;
import java.util.List;

@UseStag
public class UserMeResponse {
    @SerializedName("profile")
    @Expose
    UserDescriptionResponse profile;
    @SerializedName("history")
    @Expose
    History history;
    @SerializedName("follows")
    @Expose
    Follows follows;
    @SerializedName("bookmarked")
    @Expose
    Bookmark bookmarked;
    int numOfDownload;


    public UserDescriptionResponse getProfile() {
        return profile;
    }

    public History getHistory() {
        return history;
    }

    public Follows getFollows() {
        return follows;
    }

    public Bookmark getBookmarked() {
        return bookmarked;
    }

    public int getNumOfDownload() {
        return numOfDownload;
    }

    public void setNumOfDownload(int numOfDownload) {
        this.numOfDownload = numOfDownload;
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(String id, UserMeResponse userMeResponse, int numOfDownload) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();
        List<ContentDatum> data = new ArrayList<>();

        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setId(id);
        userMeResponse.setNumOfDownload(numOfDownload);
        contentDatum.setUserMeResponse(userMeResponse);
        data.add(contentDatum);
        module.setContentData(data);
        appCMSPageAPI.setId(id);
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);

        return appCMSPageAPI;
    }
}

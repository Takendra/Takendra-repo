package com.viewlift.models.data.appcms.history;

/*
 * Created by Viewlift on 7/5/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.vimeo.stag.UseStag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UseStag
public class AppCMSHistoryResult {

    @SerializedName("records")
    @Expose
    List<Record> records = null;

    @SerializedName("moduleId")
    @Expose
    String moduleId = null;

    @SerializedName("userId")
    @Expose
    String userId = null;

    @SerializedName("siteName")
    @Expose
    String siteName = null;

    @SerializedName("limit")
    @Expose
    int limit;

    public static boolean failure;


    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(String apiId, boolean isPromoValidationRequired) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();
        List<ContentDatum> data = new ArrayList<>();
        if (getRecords() != null) {
            for (Record records : getRecords()) {
                if (records.getContentResponse() != null) {
                    if (isPromoValidationRequired) {
                        if (!(records.getContentResponse().getTags() != null &&
                                records.getContentResponse().getTags().size() > 0 &&
                                records.getContentResponse().getTags().get(0).getTitle() != null &&
                                records.getContentResponse().getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                            data.add(records.convertToContentDatum());
                        }
                    } else {
                        data.add(records.convertToContentDatum());
                    }
                }
            }
        }

        module.setContentData(data);
        module.setMetadataMap(getMetadataMap());
        module.setId(apiId);
        appCMSPageAPI.setId(apiId);
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);

        return appCMSPageAPI;
    }

    public AppCMSPageAPI convertToRecommendationAppCMSPageAPI(String apiId) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();

        List<ContentDatum> data = new ArrayList<>();

        if (getRecords() != null) {
            for (Record records : getRecords()) {
                if (records.getGist() != null) {
                    ContentDatum contentDatum = new ContentDatum();
                    contentDatum.setGist(records.getGist());
                    contentDatum.setSeriesData(records.getSeriesData());
                    data.add(contentDatum);
                }
            }
        }

       /* try {
            Collections.sort(data, (o1, o2) -> o1.getGist().getPublishDate().compareTo(o2.getGist().getPublishDate()));
        }catch (Exception ex){
            ex.printStackTrace();
        }*/

        module.setContentData(data);
        appCMSPageAPI.setId(apiId);
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);

        return appCMSPageAPI;
    }

    MetadataMap metadataMap;

    public MetadataMap getMetadataMap() {
        return metadataMap;
    }

    public void setMetadataMap(MetadataMap metadataMap) {
        this.metadataMap = metadataMap;
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(AppCMSPageAPI appCMSPageAPI, Module module, boolean isPromoValidationRequired) {
        List<ContentDatum> data = new ArrayList<>();

        if (getRecords() != null) {
            for (Record records : getRecords()) {
                if (records.getContentResponse() != null) {
                    if (isPromoValidationRequired) {
                        if (!(records.getContentResponse().getTags() != null &&
                                records.getContentResponse().getTags().size() > 0 &&
                                records.getContentResponse().getTags().get(0).getTitle() != null &&
                                records.getContentResponse().getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                            data.add(records.convertToContentDatum());
                        }
                    } else {
                        data.add(records.convertToContentDatum());
                    }
                }
            }
        }

        if (module != null) {
            module.setContentData(data);
            module.setMetadataMap(getMetadataMap());
        }
        return appCMSPageAPI;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public static boolean isFailure() {
        return failure;
    }

    public static void setFailure(boolean failure) {
        AppCMSHistoryResult.failure = failure;
    }
}
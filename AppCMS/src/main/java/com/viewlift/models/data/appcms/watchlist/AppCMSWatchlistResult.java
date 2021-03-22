package com.viewlift.models.data.appcms.watchlist;

/*
 * Created by Viewlift on 6/28/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.MetadataMap;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.history.Record;
import com.vimeo.stag.UseStag;

import java.util.ArrayList;
import java.util.List;

@UseStag
public class AppCMSWatchlistResult {

    @SerializedName("records")
    @Expose
    List<Record> records = null;

    @SerializedName("nextOffset")
    @Expose
    int nextOffset;

    @SerializedName("limit")
    @Expose
    int limit;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public int etNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(int nextOffset) {
        this.nextOffset = nextOffset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public AppCMSPageAPI convertToAppCMSPageAPI(String Id, boolean isPromoValidationRequired) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();
        List<ContentDatum> data = new ArrayList<>();

        if (getRecords() != null) {
            for (Record records : getRecords()) {
                if (records.getContentResponse() != null) {
                    if(isPromoValidationRequired) {
                        if (!(records.getContentResponse().getTags() != null &&
                                records.getContentResponse().getTags().size() > 0 &&
                                records.getContentResponse().getTags().get(0).getTitle() != null &&
                                records.getContentResponse().getTags().get(0).getTitle().equalsIgnoreCase("promo"))) {
                            data.add(records.convertToContentDatum());
                        }
                    }else{
                        data.add(records.convertToContentDatum());
                    }
                }
            }
        }

        module.setContentData(data);
        module.setMetadataMap(getMetadataMap());
        appCMSPageAPI.setId(Id);
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
}

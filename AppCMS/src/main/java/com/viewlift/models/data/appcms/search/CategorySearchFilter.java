package com.viewlift.models.data.appcms.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.AppCMSPageAPI;
import com.viewlift.models.data.appcms.api.Category;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Module;
import com.viewlift.models.data.appcms.api.Person;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@UseStag
public class CategorySearchFilter implements Serializable {

    @SerializedName("records")
    @Expose
    List<com.viewlift.models.data.appcms.api.Category> records;

    @SerializedName("limit")
    @Expose
    int limit;

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setRecords(List<com.viewlift.models.data.appcms.api.Category> records) {
        this.records = records;
    }

    public int getLimit() {
        return limit;
    }

    public List<Category> getRecords() {
        return records;
    }

    public  AppCMSPageAPI convertToAppCMSPageAPI(List<Person> concepts, List<Person> classes) {
        AppCMSPageAPI appCMSPageAPI = new AppCMSPageAPI();
        Module module = new Module();

        List<ContentDatum> data = new ArrayList<>();
        if (getRecords() != null) {
            for (int i = 0 ;i < getRecords().size() ; i ++/*Category records : getRecords()*/) {
                if (records.get(i).getGist() !=null) {
                    data.add(convertToContentDatum(records.get(i)));
                }
            }
        }

        module.setContentData(data);

        List<ContentDatum> dataConcept = new ArrayList<>();

        if (getRecords() != null && concepts != null) {
            for (Person records : concepts) {
                if (records.getGist() !=null) {
                    dataConcept.add(convertToContentDatum(records));
                }
            }
        }
        module.setConceptaData(dataConcept);

        List<ContentDatum> dataClassess = new ArrayList<>();

        if (getRecords() != null && classes != null) {
            for (Person records : classes) {
                if (records.getGist() !=null) {
                    dataClassess.add(convertToContentDatum(records));
                }
            }
        }
        module.setClassessData(dataClassess);

        List<Module> moduleList = new ArrayList<>();
        moduleList.add(module);
        appCMSPageAPI.setModules(moduleList);

        return appCMSPageAPI;
    }

    public ContentDatum convertToContentDatum(Category records) {
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setGist(records.getGist());
        contentDatum.setId(records.getId());
        contentDatum.setTitle(records.getTitle());
        return contentDatum;
    }
    public ContentDatum convertToContentDatum(Person records) {
        ContentDatum contentDatum = new ContentDatum();
        contentDatum.setTags(records.getTags());
        contentDatum.setStreamingInfo(records.getStreamingInfo());
        contentDatum.setGist(records.getGist());
        contentDatum.setId(records.getId());
        contentDatum.setTitle(records.getTitle());
        return contentDatum;
    }

    public String getPosition(String browseCategory){
        for (int i = 0 ;i < getRecords().size() ; i++) {
            if (records.get(i).getGist() !=null) {
                if (browseCategory != null && records.get(i).getGist().getTitle() != null &&
                        records.get(i).getGist().getTitle().equalsIgnoreCase(browseCategory)) {
                    return String.valueOf(i);
                }
            }
        }
        return null;
    }
}
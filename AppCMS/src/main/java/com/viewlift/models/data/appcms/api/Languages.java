package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class Languages implements Serializable,BaseInterface {


    @SerializedName("default")
    @Expose
    Language defaultlanguage;

    @SerializedName("languages")
    @Expose
    List<Language> languageList;

    public Language getDefaultlanguage() {
        return defaultlanguage;
    }

    public void setDefaultlanguage(Language defaultlanguage) {
        this.defaultlanguage = defaultlanguage;
    }

    public List<Language> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<Language> languageList) {
        this.languageList = languageList;
    }

    public void swapPositionOfDefaultLanguageToFirst(){
        try {
            if(languageList != null && defaultlanguage != null) {
                int defaultLangIndex = languageList.indexOf(defaultlanguage);
                Language language = languageList.remove(defaultLangIndex);
                languageList.add(0, language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.viewlift.models.data.appcms.api;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.utils.LanguageHelper;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class Language implements Serializable, BaseInterface {

    @SerializedName("code")
    @Expose
    String languageCode;

    @SerializedName("name")
    @Expose
    String languageName;

    @SerializedName("localizedTitle")
    @Expose
    String localizedTitle;

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }


    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }


    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj != null && obj instanceof Language) {
            isEqual = ((Language) obj).languageCode.equalsIgnoreCase(this.getLanguageCode());
        }
        return isEqual;
    }

    public ContentDatum convertToContentDatum() {
        ContentDatum contentDatum = new ContentDatum();
        Gist gist = new Gist();
        String langName = TextUtils.isEmpty(getLocalizedTitle()) ?
                LanguageHelper.getLanguageName(languageCode) : localizedTitle;
        gist.setTitle(langName);
        gist.setDataId(languageCode);
        contentDatum.setGist(gist);
        return contentDatum;
    }

    public String getLocalizedTitle() {
        return localizedTitle != null ? localizedTitle : "";
    }

    public void setLocalizedTitle(String localizedTitle) {
        this.localizedTitle = localizedTitle;
    }
}

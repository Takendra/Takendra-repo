package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.ui.page.Items;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.ArrayList;

@UseStag
public class ExtendedMap implements Serializable {
    @SerializedName("items")
    @Expose
    ArrayList<Items> items;
    @SerializedName("formData")
    @Expose
    FormData formData;

    public ArrayList<Items> getItems() {
        return items;
    }

    public FormData getFormData() {
        return formData;
    }
}

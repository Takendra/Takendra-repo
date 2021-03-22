package com.viewlift.models.data.appcms.api;

import android.annotation.SuppressLint;

import com.viewlift.views.customviews.expendablerecyclerview.models.ExpandableGroup;
import com.vimeo.stag.UseStag;

import java.util.List;

@UseStag
public class ConceptData extends ExpandableGroup<Person> {

    @SuppressLint("ParcelCreator")
    public ConceptData() {
        super();
    }

    @SuppressLint("ParcelCreator")
    public ConceptData(String title, List<Person> items) {
        super(title, items);
    }
}

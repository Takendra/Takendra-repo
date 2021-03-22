package com.viewlift.models.data.appcms.api;

import java.io.Serializable;
import java.util.List;

public class AppCMSParentalRatingMapResponse implements Serializable {

    private String key;
    private List<String> value;
    public String getKey() {
        return key;
    }
    public List<String> getValue() {
        return value;
    }

}

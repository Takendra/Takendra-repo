package com.viewlift.models.data.appcms.ui;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Resources implements Serializable {
    private List<UiResource> resources;
    private boolean loadedFromNetwork;
    private String matchedKey;
    private final String regEx = "@####@";
    private final String regExForDoubleQuotes = "@@####@@";


    public CustomHashmap<String, String> getResourcesMap() {
        return resourcesMap;
    }

    public void setResourcesMap(CustomHashmap<String, String> resourcesMap) {
        this.resourcesMap = resourcesMap;
    }

    @SerializedName("resourcesMap")
    @Expose
    CustomHashmap<String, String> resourcesMap;



    public List<UiResource> getResources ()
    {
        return resources;
    }

    public void setResources (List<UiResource>resources)
    {
        this.resources = resources;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [resources = "+resources+"]";
    }

    public String getUIresource(String key){
        try {
           /*HashMap<String,String> hashMap = new HashMap<>();
           matchedKey = null;
           rx.Observable<UiResource> filter = rx.Observable.from(resources).
                   filter(uiResource1 ->{
                       hashMap.put(uiResource1.getValue(),uiResource1.getValue());
                       return uiResource1.getKey().toLowerCase().replaceAll("\\s","")
                               .equalsIgnoreCase(key.trim().replaceAll("\\s","").toLowerCase());});

           if (null != filter) {
               filter.subscribe(uiResource -> {
                   matchedKey = uiResource.getValue();
               });
           } else {
               for (int i = 0; i < resources.size(); i++) {
                   UiResource res = resources.get(i);
                   if (res.getKey().toLowerCase().replaceAll(" ", "").equalsIgnoreCase(key.trim().replaceAll(" ", "").toLowerCase())) {
                       matchedKey = res.getValue();
                       break;
                   }
               }
           }*/

            if(resourcesMap != null)
                matchedKey = resourcesMap.get(key.toLowerCase().replaceAll("\\s",""));

        }catch(Exception e){

        }
        if(matchedKey == null){
            matchedKey = key;
        }
        return matchedKey;
    }


    public String getStringValue(String key , String... values){
        String strValue = getUIresource(key);
        try {
            for (int i = 0; i < values.length; i++) {
                strValue = strValue.replaceFirst(regEx, getUIresource(values[i]));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return strValue;
    }


    public String getStringValueWithDoubleQuotes(String key , String... values){
        String strValue = getUIresource(key);
        for(int i=0;i<values.length;i++){
            strValue =  strValue.replaceFirst(regExForDoubleQuotes,"\""+getUIresource(values[i])+"\"");
        }
        return strValue;
    }

    public void setLoadedFromNetwork(boolean loadedFromNetwork) {
        this.loadedFromNetwork = loadedFromNetwork;
    }

    public boolean isLoadedFromNetwork() {
        return loadedFromNetwork;
    }


    public static class CustomHashmap<S, S1> extends HashMap<String,String> {
        @Override
        public String put(String key, String value) {
            this.remove(key.toLowerCase().replaceAll("\\s",""));
            return super.put(key.toLowerCase().replaceAll("\\s",""), value);
        }

    }
}
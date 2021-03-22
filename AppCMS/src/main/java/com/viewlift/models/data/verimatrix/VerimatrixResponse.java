package com.viewlift.models.data.verimatrix;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by viewlift on 12/21/17.
 */

@UseStag
public class VerimatrixResponse {
    @SerializedName("authenticated")
    @Expose
    boolean authenticated;

    @SerializedName("_type")
    @Expose
    String apiType;

    @SerializedName(value = "possible_idps", alternate = {"idps"})
    @Expose
    Map<String, TVProvider> providers;

    @SerializedName("querytoken")
    @Expose
    String queryToken;
    @SerializedName("aisuid")
    @Expose
    String userId;
    @SerializedName("security_token")
    @Expose
    String resourceAccessToken;

    List<TVProvider> tvProviders;

    @SerializedName("expires")
    @Expose
    String expires;

    @SerializedName("servertime")
    @Expose
    String serverTime;

    @SerializedName("platform_id")
    @Expose
    String platformId;

    @SerializedName("serial")
    @Expose
    String serial;

    @SerializedName("shortcode")
    @Expose
    String shortCode;

    @SerializedName("polling_url")
    @Expose
    String polling_url;

    @SerializedName("result")
    @Expose
    boolean result;

    public boolean isAuthenticated() {
        return authenticated;
    }

    @SerializedName("documents")
    @Expose
    List<ResourceAccess> resourceAccess;

    public String getApiType() {
        return apiType;
    }

    public Map<String, TVProvider> getProviders() {
        return providers;
    }

    public void setProviderIdp(Map<String, TVProvider> providers) {
        tvProviders = new ArrayList<>();
        for (String key : providers.keySet()) {
            TVProvider provider = providers.get(key);
            provider.setTvProviderIdp(key);
            tvProviders.add(provider);
        }
        setTvProviders(tvProviders);
    }

    public List<TVProvider> getTvProviders() {
        return tvProviders;
    }

    public void setTvProviders(List<TVProvider> tvProviders) {
        this.tvProviders = tvProviders;
    }

    public String getQueryToken() {
        return queryToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getResourceAccessToken() {
        return resourceAccessToken;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getPolling_url() {
        return polling_url;
    }

    public void setPolling_url(String polling_url) {
        this.polling_url = polling_url;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<ResourceAccess> getResourceAccess() {
        return resourceAccess;
    }

    public void setResourceAccess(List<ResourceAccess> resourceAccess) {
        this.resourceAccess = resourceAccess;
    }

    public Map<String, Boolean> convertResourceId(List<ResourceAccess> resourceAccess) {
        Map<String, Boolean> resource = new HashMap<>();
        for (int i = 0; i < resourceAccess.size(); i++) {
            resource.put(resourceAccess.get(i).getResource(), resourceAccess.get(i).getAuthenticated());
        }
        return resource;
    }
}

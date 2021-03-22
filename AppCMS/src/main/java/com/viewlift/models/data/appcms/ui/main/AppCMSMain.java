package com.viewlift.models.data.appcms.ui.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.viewlift.models.data.appcms.api.Languages;
import com.vimeo.stag.UseStag;

import java.io.Serializable;
import java.util.List;

@UseStag
public class AppCMSMain implements Serializable {

    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("templateName")
    @Expose
    String templateName;

    @SerializedName("accessKey")
    @Expose
    String accessKey;

    @SerializedName("apiBaseUrl")
    @Expose
    String apiBaseUrl;

    @SerializedName("pageEndpoint")
    @Expose
    String pageEndpoint;

    @SerializedName("internalName")
    @Expose
    String internalName;

    @SerializedName("apiKeys")
    @Expose
    List<AppCMSXAPIKey> x_ApiKeys;

    @SerializedName("faqUrl")
    @Expose
    String faqUrl;

    @SerializedName("beacon")
    @Expose
    Beacon beacon;

    @SerializedName("site")
    @Expose
    String site;

    @SerializedName("serviceType")
    @Expose
    String serviceType;

    /*@SerializedName("serviceTypes")
    @Expose
    String[] serviceTypes;*/

    @SerializedName("apiBaseUrlCached")
    @Expose
    String apiBaseUrlCached;

    @SerializedName("cachedAPIToken")
    @Expose
    String cachedAPIToken;

    @SerializedName("cacheInterval")
    @Expose
    long cacheInterval;

    @SerializedName("domainName")
    @Expose
    String domainName;

    @SerializedName(value = "brand")
    @Expose
    Brand brand;
    @SerializedName(value = "brandApps")
    @Expose
    Brand brandApps;

    @SerializedName("compliance")
    @Expose
    Compliance compliance;

    @SerializedName("brandOtt")
    @Expose
    Brand brandOtt;

    @SerializedName("version")
    @Expose
    String version;

    @SerializedName("old_version")
    @Expose
    String oldVersion;

    @SerializedName("Web")
    @Expose
    String web;

    @SerializedName("iOS")
    @Expose
    String iOS;

    @SerializedName("Android")
    @Expose
    String android;

    @SerializedName("features")
    @Expose
    Features features;

    @SerializedName("appVersions")
    @Expose
    AppVersions appVersions;

    @SerializedName("companyName")
    @Expose
    String companyName;

    boolean loadFromFile;

    @SerializedName("fireTv")
    @Expose
    private String fireTv;
    @SerializedName("timestamp")
    @Expose
    long timestamp;

    @SerializedName("socialMedia")
    @Expose
    SocialMedia socialMedia;

    @SerializedName("forceLogin")
    @Expose
    boolean forceLogin;

    @SerializedName("isDownloadable")
    @Expose
    boolean isDownloadable;

    @SerializedName("hls")
    @Expose
    boolean hls;

    @SerializedName("casting")
    @Expose
    boolean casting;
    @SerializedName("isMonetizationModelEnabled")
    @Expose
    boolean isMonetizationModelEnabled;

    @SerializedName("paymentProviders")
    @Expose
    PaymentProviders paymentProviders;

    @SerializedName("languages")
    @Expose
    Languages languages;

    @SerializedName("customerService")
    @Expose
    CustomerService customerService;

    @SerializedName("emailConsent")
    @Expose
    EmailConsent emailConsent;

    @SerializedName("genericMessages")
    @Expose
    GenericMessages genericMessages;

    @SerializedName("recommendation")
    @Expose
    Recommendation recommendation;

    @SerializedName("cleverTapAnalyticsId")
    @Expose
    String cleverTapAnalyticsId;
    @SerializedName("cleverTapToken")
    @Expose
    String cleverTapToken;
    @SerializedName("appInbox")
    @Expose
    boolean isAppInboxEnable = true;

    @SerializedName("analytics")
    @Expose
    Analytics analytics;

    boolean isFireTV;

    public String getFireTv() {
        return fireTv;
    }

    public void setFireTv(String fireTv) {
        this.fireTv = fireTv;
    }

    public CustomerService getCustomerService() {
        return customerService;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public Languages getLanguages() {
        return languages;
    }

    public void setLanguages(Languages languages) {
        this.languages = languages;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }

    public String getPageEndpoint() {
        return pageEndpoint;
    }

    public void setPageEndpoint(String pageEndpoint) {
        this.pageEndpoint = pageEndpoint;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getFaqUrl() {
        return faqUrl;
    }

    public void setFaqUrl(String faqUrl) {
        this.faqUrl = faqUrl;
    }

    public Beacon getBeacon() {
        return beacon;
    }

    public void setBeacon(Beacon beacon) {
        this.beacon = beacon;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Brand getBrand() {
        if (isFireTV) {
            if (getBrandOtt() != null && !getBrandOtt().isUseDefault())
                return brandOtt;
            else
                return brand;
        } else {
            if (getBrandApps() != null && !getBrandApps().isUseDefault())
                return brandApps;
            else
                return brand;
        }
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Brand getBrandOtt() {
        return brandOtt;
    }

    public void setBrandOtt(Brand brandOtt) {
        this.brandOtt = brandOtt;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getIOS() {
        return iOS;
    }

    public void setIOS(String iOS) {
        this.iOS = iOS;
    }

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getiOS() {
        return iOS;
    }

    public void setiOS(String iOS) {
        this.iOS = iOS;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isForceLogin() {
        return forceLogin;
    }

    public void setForceLogin(boolean forceLogin) {
        this.forceLogin = forceLogin;
    }

    public SocialMedia getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    public boolean shouldLoadFromFile() {
        return loadFromFile;
    }

    public void setLoadFromFile(boolean loadFromFile) {
        this.loadFromFile = loadFromFile;
    }

    public Features getFeatures() {
        return features;
    }

    public void setFeatures(Features features) {
        this.features = features;
    }

    public PaymentProviders getPaymentProviders() {
        return paymentProviders;
    }

    public void setPaymentProviders(PaymentProviders paymentProviders) {
        this.paymentProviders = paymentProviders;
    }

    public boolean isDownloadable() {
        return isDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        isDownloadable = downloadable;
    }

    public AppVersions getAppVersions() {
        return appVersions;
    }

    public void setAppVersions(AppVersions appVersions) {
        this.appVersions = appVersions;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getApiBaseUrlCached() {
        return apiBaseUrlCached;
    }

    public void setApiBaseUrlCached(String apiBaseUrlCached) {
        this.apiBaseUrlCached = apiBaseUrlCached;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<AppCMSXAPIKey> getX_ApiKeys() {
        return x_ApiKeys;
    }

    public void setX_ApiKeys(List<AppCMSXAPIKey> x_ApiKeys) {
        this.x_ApiKeys = x_ApiKeys;
    }

    public boolean isHls() {
        return this.hls;
    }

    public void setHls(boolean hls) {
        this.hls = hls;
    }

    public boolean isCasting() {
        return casting;
    }

    public void setCasting(boolean casting) {
        this.casting = casting;
    }

    public String getCachedAPIToken() {
        return cachedAPIToken;
    }

    public void setCachedAPIToken(String cachedAPIToken) {
        this.cachedAPIToken = cachedAPIToken;
    }

    public long getCacheInterval() {
        return cacheInterval;
    }

    public GenericMessages getGenericMessages() {
        return genericMessages;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    private Brand getBrandApps() {
        return brandApps;
    }

    public String getCleverTapAnalyticsId() {
        return cleverTapAnalyticsId;
    }

    public String getCleverTapToken() {
        return cleverTapToken;
    }

    public boolean isAppInboxEnable() {
        return isAppInboxEnable;
    }

    public boolean isFireTV() {
        return isFireTV;
    }

    public void setFireTV(boolean fireTV) {
        isFireTV = fireTV;
    }

    public Compliance getCompliance() {
        return compliance;
    }

    public void setCompliance(Compliance compliance) {
        this.compliance = compliance;
    }

    public EmailConsent getEmailConsent() {
        return emailConsent;
    }

    public void setEmailConsent(EmailConsent emailConsent) {
        this.emailConsent = emailConsent;
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public boolean isMonetizationModelEnabled() {
        return isMonetizationModelEnabled;
    }

    /*public String[] getServiceTypes() {
        return serviceTypes;
    }*/
}
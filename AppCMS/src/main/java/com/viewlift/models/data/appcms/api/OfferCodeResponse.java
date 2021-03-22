package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OfferCodeResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("marketing")
    @Expose
    private Marketing marketing;

    @SerializedName("offerDetails")
    @Expose
    private OfferDetails offerDetails;

    @SerializedName("scheduleDetails")
    @Expose
    private ScheduleDetails scheduleDetails;

    @SerializedName("renewalCyclePeriodMultiplier")
    @Expose
    private String renewalCyclePeriodMultiplier;

    @SerializedName("renewalCycleType")
    @Expose
    private String renewalCycleType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Marketing getMarketing() {
        return marketing;
    }

    public void setMarketing(Marketing marketing) {
        this.marketing = marketing;
    }

    public OfferDetails getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(OfferDetails offerDetails) {
        this.offerDetails = offerDetails;
    }

    public ScheduleDetails getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(ScheduleDetails scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public String getRenewalCyclePeriodMultiplier() {
        return renewalCyclePeriodMultiplier;
    }

    public void setRenewalCyclePeriodMultiplier(String renewalCyclePeriodMultiplier) {
        this.renewalCyclePeriodMultiplier = renewalCyclePeriodMultiplier;
    }

    public String getRenewalCycleType() {
        return renewalCycleType;
    }

    public void setRenewalCycleType(String renewalCycleType) {
        this.renewalCycleType = renewalCycleType;
    }

    public class Marketing {

        @SerializedName("cookieValidDays")
        @Expose
        private Integer cookieValidDays;

        @SerializedName("campaignType")
        @Expose
        private Object campaignType;

        public Integer getCookieValidDays() {
            return cookieValidDays;
        }

        public void setCookieValidDays(Integer cookieValidDays) {
            this.cookieValidDays = cookieValidDays;
        }

        public Object getCampaignType() {
            return campaignType;
        }

        public void setCampaignType(Object campaignType) {
            this.campaignType = campaignType;
        }
    }

    public class OfferDetails {

        @SerializedName("offerStrategyType")
        @Expose
        private String offerStrategyType;
        @SerializedName("offerLimit")
        @Expose
        private OfferLimit offerLimit;
        @SerializedName("reduceCharge")
        @Expose
        private ReduceCharge reduceCharge;
        @SerializedName("freeTrial")
        @Expose
        private FreeTrial freeTrial;
        @SerializedName("freeTrialUntil")
        @Expose
        private Object freeTrialUntil;

        public String getOfferStrategyType() {
            return offerStrategyType;
        }

        public void setOfferStrategyType(String offerStrategyType) {
            this.offerStrategyType = offerStrategyType;
        }

        public OfferLimit getOfferLimit() {
            return offerLimit;
        }

        public void setOfferLimit(OfferLimit offerLimit) {
            this.offerLimit = offerLimit;
        }

        public ReduceCharge getReduceCharge() {
            return reduceCharge;
        }

        public void setReduceCharge(ReduceCharge reduceCharge) {
            this.reduceCharge = reduceCharge;
        }

        public FreeTrial getFreeTrial() {
            return freeTrial;
        }

        public void setFreeTrial(FreeTrial freeTrial) {
            this.freeTrial = freeTrial;
        }

        public Object getFreeTrialUntil() {
            return freeTrialUntil;
        }

        public void setFreeTrialUntil(Object freeTrialUntil) {
            this.freeTrialUntil = freeTrialUntil;
        }

    }

    public class ScheduleDetails {

        @SerializedName("scheduledFromDate")
        @Expose
        private String scheduledFromDate;
        @SerializedName("scheduledToDate")
        @Expose
        private Object scheduledToDate;

        public String getScheduledFromDate() {
            return scheduledFromDate;
        }

        public void setScheduledFromDate(String scheduledFromDate) {
            this.scheduledFromDate = scheduledFromDate;
        }

        public Object getScheduledToDate() {
            return scheduledToDate;
        }

        public void setScheduledToDate(Object scheduledToDate) {
            this.scheduledToDate = scheduledToDate;
        }

    }

    public class OfferLimit {

        @SerializedName("offerLimitType")
        @Expose
        private String offerLimitType;
        @SerializedName("campaignTag")
        @Expose
        private String campaignTag;
        @SerializedName("numberOfCoupons")
        @Expose
        private Integer numberOfCoupons;

        public String getOfferLimitType() {
            return offerLimitType;
        }

        public void setOfferLimitType(String offerLimitType) {
            this.offerLimitType = offerLimitType;
        }

        public String getCampaignTag() {
            return campaignTag;
        }

        public void setCampaignTag(String campaignTag) {
            this.campaignTag = campaignTag;
        }

        public Integer getNumberOfCoupons() {
            return numberOfCoupons;
        }

        public void setNumberOfCoupons(Integer numberOfCoupons) {
            this.numberOfCoupons = numberOfCoupons;
        }

    }

    public class ReduceCharge {

        @SerializedName("redemptionValue")
        @Expose
        private int redemptionValue;
        @SerializedName("redemptionType")
        @Expose
        private String redemptionType;
        @SerializedName("redemptionLimit")
        @Expose
        private double redemptionLimit;

        public int getRedemptionValue() {
            return redemptionValue;
        }

        public void setRedemptionValue(int redemptionValue) {
            this.redemptionValue = redemptionValue;
        }

        public String getRedemptionType() {
            return redemptionType;
        }

        public void setRedemptionType(String redemptionType) {
            this.redemptionType = redemptionType;
        }

        public double getRedemptionLimit() {
            return redemptionLimit;
        }

        public void setRedemptionLimit(double redemptionLimit) {
            this.redemptionLimit = redemptionLimit;
        }

    }

    public class FreeTrial {

        @SerializedName("renewalCycleType")
        @Expose
        private String renewalCycleType;

        @SerializedName("renewalCycleMultiplier")
        @Expose
        private Object renewalCycleMultiplier;

        public String getRenewalCycleType() {
            return renewalCycleType;
        }

        public void setRenewalCycleType(String renewalCycleType) {
            this.renewalCycleType = renewalCycleType;
        }

        public Object getRenewalCycleMultiplier() {
            return renewalCycleMultiplier;
        }

        public void setRenewalCycleMultiplier(Object renewalCycleMultiplier) {
            this.renewalCycleMultiplier = renewalCycleMultiplier;
        }
    }
}

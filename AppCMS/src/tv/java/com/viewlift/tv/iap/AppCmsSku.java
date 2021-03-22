package com.viewlift.tv.iap;

public enum AppCmsSku {

    //The only subscription product used in this sample app
    APPCMS_SUBS("viewlift.motv.Subscription.test", "US"),
    APPCMS_SUBS_MONTH("standard.sub.monthly", "US"),
    APPCMS_SUBS_YEARLY("standard.sub.annual.092018", "US");

    private final String sku;
    private final String availableMarkpetplace;

    /**
     * Returns the Sku string of the MySku object
     * @return
     */
    public String getSku() {
        return this.sku;
    }

    /**
     * Returns the Available Marketplace of the MySku object
     * @return
     */
    public String getAvailableMarketplace() {
        return this.availableMarkpetplace;
    }

    private AppCmsSku(final String sku, final String availableMarkpetplace) {
        this.sku = sku;
        this.availableMarkpetplace = availableMarkpetplace;
    }

    /**
     * Returns the MySku object from the specified Sku and marketplace value.
     * @param sku
     * @param marketplace
     * @return
     */
    public static AppCmsSku fromSku(final String sku, final String marketplace) {
      return APPCMS_SUBS.getSku().equals(sku) && (null == marketplace || APPCMS_SUBS.getAvailableMarketplace()
                .equals(marketplace)) ? APPCMS_SUBS :

                (APPCMS_SUBS_MONTH.getSku().equals(sku) && (marketplace == null || APPCMS_SUBS_MONTH.getAvailableMarketplace().equals(marketplace)))
                        ? APPCMS_SUBS_MONTH : (APPCMS_SUBS_YEARLY.getSku().equals(sku) && (marketplace == null || APPCMS_SUBS_YEARLY.getAvailableMarketplace().equals(marketplace))) ? APPCMS_SUBS_YEARLY : null;

    }

}

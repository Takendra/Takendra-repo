package com.viewlift.tv.iap;

import com.viewlift.models.data.appcms.api.ContentDatum;

public interface IapCallBacks {

    public void finalizeSignupAfterSubscription(String recieptId, String sku, String amazonIAPuserId);
    public void consumableItemPurchased(String reciptId , String sku , String amazonIAPUserId);
    public void navigateToHomePage();
    public ContentDatum getContentDataForAmazonPurchase();
}

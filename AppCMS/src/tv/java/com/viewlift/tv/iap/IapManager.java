package com.viewlift.tv.iap;

import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserData;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.views.activity.AppCmsBaseActivity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IapManager {

    private static final String TAG = "IAPSubscription";
    private final AppCMSPresenter appCMSPresenter;

    private UserIapData userIapData;

    private IapCallBacks iapCallBacks;
    private final SubscriptionDataSource dataSource;
    private boolean isSubscriptionAvailable;

    public boolean isSubscriptionAvailable() {
        return isSubscriptionAvailable;
    }

    public void setSubscriptionAvailable(boolean subscriptionAvailable) {
        isSubscriptionAvailable = subscriptionAvailable;
    }

    public void handleReceipt(PurchaseUpdatesResponse purchaseUpdatesResponse) {

        if (purchaseUpdatesResponse.getReceipts() != null && purchaseUpdatesResponse.getReceipts().size() > 1) {
            Collections.sort(purchaseUpdatesResponse.getReceipts(), new ReceiptSorter(this));
        }

        //If user is loggedIn and not subscribed.
        //Check if amazonUserId is not registered on Viewlift System. If not registered then proceed next.
        // Check loggedIn email Id and database emailId and if both are same then call subscription api.
        saveSubscriptionRecord(purchaseUpdatesResponse.getReceipts().get(0),purchaseUpdatesResponse.getUserData().getUserId());
        String latestReciptId = purchaseUpdatesResponse.getReceipts().get(0).getReceiptId();
        if(appCMSPresenter.isUserLoggedIn() && !appCMSPresenter.isUserSubscribed()){
            final List<SubscriptionRecord> subsRecords = dataSource.getSubscriptionRecords(purchaseUpdatesResponse.getUserData().getUserId());
            if(subsRecords != null && subsRecords.size() > 0){
                SubscriptionRecord subscriptionRecord = subsRecords.get(0);
                for(int i=0 ;i <subsRecords.size()-1 ; i++){
                    SubscriptionRecord record = subsRecords.get(i);
                    if(record.getAmazonReceiptId().equalsIgnoreCase(latestReciptId)){
                        subscriptionRecord = record;
                        break;
                    }
                }

                SubscriptionRecord finalSubscriptionRecord = subscriptionRecord;
                if(appCMSPresenter.checkForExistingSubscription(false, status ->{
                    if(status.equalsIgnoreCase("NotRegistered")){
                        String userId = finalSubscriptionRecord.getVlUserId() != null ? finalSubscriptionRecord.getVlUserId() : appCMSPresenter.getLoggedInUser();
                        if(appCMSPresenter.getLoggedInUser().equalsIgnoreCase(userId)){
                            if(finalSubscriptionRecord.getCurrencyCode() != null && finalSubscriptionRecord.getCurrencyCode().length() > 0){
                                appCMSPresenter.restoreAmazonSubscription(finalSubscriptionRecord.getAmazonReceiptId(), finalSubscriptionRecord.getSku(),
                                        finalSubscriptionRecord.getCurrencyCode(), finalSubscriptionRecord.getAmazonUserId());
                            }else{
                               appCMSPresenter.getCurrencyCodeFromApi(currencyCode -> {
                                    if(currencyCode != null && currencyCode.length() > 0){
                                        appCMSPresenter.restoreAmazonSubscription(finalSubscriptionRecord.getAmazonReceiptId(), finalSubscriptionRecord.getSku(),
                                                currencyCode, finalSubscriptionRecord.getAmazonUserId());
                                    }
                               });
                            }
                        }
                    }
                }));
            }
        }
        PurchasingService.notifyFulfillment(purchaseUpdatesResponse.getReceipts().get(0).getReceiptId(), FulfillmentResult.FULFILLED);

       // handleReceipt(purchaseUpdatesResponse.getRequestId().toString(),purchaseUpdatesResponse.getReceipts().get(0),purchaseUpdatesResponse.getUserData(),false);
    }


    public void handleReceipt(){
        List<SubscriptionRecord> subscriptionRecords = userIapData.getSubscriptionRecords();
        if(subscriptionRecords != null && subscriptionRecords.size() > 0){
           SubscriptionRecord subscriptionRecord = subscriptionRecords.get(0);
           if(subscriptionRecord != null){
               iapCallBacks.finalizeSignupAfterSubscription(subscriptionRecord.getAmazonReceiptId(),subscriptionRecord.getSku(), subscriptionRecord.getAmazonUserId());

           }
        }

    }
    /* renamed from: com.eros.now.upgrade.IAPManager$1 */
    class ReceiptSorter implements Comparator<Receipt> {
          IapManager iapManager;

        ReceiptSorter(IapManager iAPManager) {
            this.iapManager = iAPManager;
        }

        public int compare(Receipt receipt, Receipt receipt2) {
            return receipt2.getPurchaseDate().compareTo(receipt.getPurchaseDate());
        }
    }


    public IapManager(AppCmsBaseActivity activity , AppCMSPresenter appCMSPresenter) {
        this.iapCallBacks = activity;
        this.dataSource = new SubscriptionDataSource(appCMSPresenter.getCurrentActivity().getApplicationContext());
        this.appCMSPresenter = appCMSPresenter;

    }

    /**
     * Method to set the app's amazon user id and marketplace from IAP SDK
     * responses.
     *
     * @param newAmazonUserId
     * @param newAmazonMarketplace
     */
    public void setAmazonUserId(final String newAmazonUserId, final String newAmazonMarketplace) {
        // Reload everything if the Amazon user has changed.
        if (newAmazonUserId == null) {
            // A null user id typically means there is no registered Amazon
            // account.
            if (userIapData != null) {
                userIapData = null;
                //refreshMagazineSubsAvailability();
            }
        } else if (userIapData == null || !newAmazonUserId.equals(userIapData.getAmazonUserId())) {
            // If there was no existing Amazon user then either no customer was
            // previously registered or the application has just started.

            // If the user id does not match then another Amazon user has
            // registered.
            userIapData = new UserIapData(newAmazonUserId, newAmazonMarketplace);
            //efreshMagazineSubsAvailability();
        }
        if(userIapData != null && userIapData.getAmazonUserId() != null){
            appCMSPresenter.setAmazonPurchaseDetails(null,userIapData.getAmazonUserId(),userIapData.getAmazonMarketplace());
        }
    }




    /**
     * Enable the magazine subscription.
     *
     * @param productData
     */
    public void enablePurchaseForSkus(final Map<String, Product> productData) {
        if (productData.containsKey(AppCmsSku.APPCMS_SUBS.getSku())
                || productData.containsKey(AppCmsSku.APPCMS_SUBS_MONTH.getSku())
                || productData.containsKey(AppCmsSku.APPCMS_SUBS_YEARLY.getSku() )) {
            isSubscriptionAvailable = true;
            printMessgae("enablePurchaseForSkus**************");
        }
    }

    /**
     * Disable the magazine subscription.
     *    reasons for product not available can be:
     *         * Item not available for this country
     *         * Item pulled off from Appstore by developer
     *         * Item pulled off from Appstore by Amazon
     * @param unavailableSkus
     */
    public void disablePurchaseForSkus(final Set<String> unavailableSkus) {
        if (unavailableSkus.contains(AppCmsSku.APPCMS_SUBS.getSku())
                || unavailableSkus.contains(AppCmsSku.APPCMS_SUBS_MONTH.getSku())
                || unavailableSkus.contains(AppCmsSku.APPCMS_SUBS_YEARLY.getSku() )) {
            isSubscriptionAvailable = false;
            try {
                printMessgae("disablePurchaseForSkus**************" + unavailableSkus.toArray().toString());
            }catch(Exception e){
                e.printStackTrace();
            }
         }
    }


    private void printMessgae(String message){
        System.out.println(TAG + " "+message);
    }

    public void activate() {
        dataSource.open();
    }

    public void refreshSubscriptionAvailablity() {
        final boolean available = isSubscriptionAvailable && userIapData!=null;
        //TODO:- Enable/Disable subscription button based on thsi boolean.
    }

    public void disableAllPurchases() {
        setSubscriptionAvailable(false);
        refreshSubscriptionAvailablity();
    }

    /**
     * Method to handle receipt
     *
     * @param requestId
     * @param receipt
     * @param userData
     */
    public void handleReceipt(final String requestId, final Receipt receipt, final UserData userData) {
        switch (receipt.getProductType()) {
            case CONSUMABLE:
                // check consumable sample for how to handle consumable purchases
                PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
                iapCallBacks.consumableItemPurchased(receipt.getReceiptId(),receipt.getSku(),userData.getUserId());
                //TODO:- Need to handle consumble flow.
                break;
            case ENTITLED:
                // check entitlement sample for how to handle consumable purchases
                break;
            case SUBSCRIPTION:
                handleSubscriptionPurchase(receipt, userData);
                break;
        }
    }

    /**
     * This method contains the business logic to fulfill the customer's
     * purchase based on the receipt received from InAppPurchase SDK's
     * {@link PurchasingListener#onPurchaseResponse} or
     * {@link PurchasingListener#onPurchaseUpdates} method.
     *
     *  @param requestId
     * @param receiptId
      */
    public void handleSubscriptionPurchase(final Receipt receipt, final UserData userData) {
        try {
            if (receipt.isCanceled()) {
                // Check whether this receipt is for an expired or canceled
                // subscription
                appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_RECEIPT_CANCEL,
                        appCMSPresenter.AMAZON_IAP,
                        appCMSPresenter.getLoggedInUser(),
                        userData.getUserId(),
                        receipt.getSku(),
                        receipt.getReceiptId(), ""));
                revokeSubscription(receipt, userData.getUserId());
            } else {
                grantSubscriptionPurchase(receipt, userData);
            }
            return;
        } catch (final Throwable e) {
            appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_EXCEPTION,
                    appCMSPresenter.AMAZON_IAP,
                    appCMSPresenter.getLoggedInUser(),
                    userData.getUserId(),
                    receipt.getSku(),
                    receipt.getReceiptId(), ""));
            printMessgae("Purchase cannot be completed, please retry");
        }
    }


    /**
     * Private method to revoke a subscription purchase from the customer
     *
     * Please implement your application-specific logic to handle the revocation
     * of a subscription purchase.
     *
     *
     * @param receipt
     * @param userId
     */
    private void revokeSubscription(final Receipt receipt, final String userId) {
        final String receiptId = receipt.getReceiptId();
        dataSource.cancelSubscription(receiptId, receipt.getCancelDate().getTime());
    }


    /**
     * We strongly recommend verifying the receipt on your own server side
     * first. The server side verification ideally should include checking with
     * Amazon RVS (Receipt Verification Service) to verify the receipt details.
     *
     * @see <a href=
     *      "https://developer.amazon.com/appsandservices/apis/earn/in-app-purchasing/docs/rvs"
     *      >Appstore's Receipt Verification Service</a>
     *
     * @param receiptId
     * @return
     */
    private boolean verifyReceiptFromYourService(final String receiptId, final UserData userData) {
        // TODO Add your own server side accessing and verification code
        return true;
    }


    private void grantSubscriptionPurchase(final Receipt receipt, final UserData userData) {
        try {
             PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
             iapCallBacks.finalizeSignupAfterSubscription(receipt.getReceiptId(), receipt.getSku(), userData.getUserId());

        } catch (final Throwable e) {
            printMessgae("Failed to grant entitlement purchase, with error " + e.getMessage());
            appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_EXCEPTION,
                    appCMSPresenter.AMAZON_IAP,
                    appCMSPresenter.getLoggedInUser(),
                    userData.getUserId(),
                    receipt.getSku(),
                    receipt.getReceiptId(), ""));
        }
        saveSubscriptionRecord(receipt, userData.getUserId());
    }

    /**
     *
     * This sample app includes a simple SQLite implementation for save
     * subscription purchase detail locally.
     *
     * We strongly recommend that you save the purchase information on a server.
     *
     *
     *
     * @param receipt
     * @param userId
     */
    private void saveSubscriptionRecord(final Receipt receipt, final String userId) {
        try {
            dataSource
                    .insertOrUpdateSubscriptionRecord(receipt.getReceiptId(),
                            userId,
                            receipt.getPurchaseDate().getTime(),
                            receipt.getCancelDate() == null ? SubscriptionRecord.TO_DATE_NOT_SET
                                    : receipt.getCancelDate().getTime(),
                            receipt.getSku(), appCMSPresenter.getLoggedInUser(), iapCallBacks.getContentDataForAmazonPurchase());
        }catch (Exception e){
            appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_EXCEPTION,
                    appCMSPresenter.AMAZON_IAP,
                    appCMSPresenter.getLoggedInUser(),
                    userId,
                    receipt.getSku(),
                    receipt.getReceiptId(), ""));
            e.printStackTrace();
        }

    }


    /**
     * Reload the subscription history from database
     */
    public void reloadSubscriptionStatus() {
        final List<SubscriptionRecord> subsRecords = dataSource.getSubscriptionRecords(userIapData.getAmazonUserId());
        userIapData.setSubscriptionRecords(subsRecords);
        userIapData.reloadSubscriptionStatus();


        List<SubscriptionRecord> subscriptionRecords = userIapData.getSubscriptionRecords();
        if(subscriptionRecords != null && subscriptionRecords.size() > 0){
            SubscriptionRecord subscriptionRecord = subscriptionRecords.get(0);
            if(subscriptionRecord != null){
                appCMSPresenter.setAmazonPurchaseDetails(subscriptionRecord.getAmazonReceiptId(), subscriptionRecord.getAmazonUserId(),userIapData.getAmazonMarketplace());
            }
        }






        refreshSubscriptionAvailablity();
    }


    public void purchaseFailed() {
        iapCallBacks.navigateToHomePage();
    }
}

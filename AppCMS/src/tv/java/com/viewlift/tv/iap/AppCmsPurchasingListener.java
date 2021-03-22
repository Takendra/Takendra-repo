package com.viewlift.tv.iap;

import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.views.activity.AppCmsBaseActivity;

import java.util.HashSet;
import java.util.Set;

public class AppCmsPurchasingListener implements PurchasingListener {

    private static final String TAG = "IAPSubscription";
    private final AppCMSPresenter appCMSPresenter;
    private final AppCmsBaseActivity activity;

    private IapManager iapManager;

    public AppCmsPurchasingListener(IapManager iapManager, AppCMSPresenter appCMSPresenter, AppCmsBaseActivity appCmsBaseActivity) {
        this.iapManager = iapManager;
        this.appCMSPresenter = appCMSPresenter;
        activity = appCmsBaseActivity;
    }

    @Override
    public void onUserDataResponse(UserDataResponse userDataResponse) {
        printMessgae("onGetUserDataResponse: requestId (" + userDataResponse.getRequestId()
                + ") userIdRequestStatus: "
                + userDataResponse.getRequestStatus()
                + ")");

        UserDataResponse.RequestStatus status = userDataResponse.getRequestStatus();

        switch (status) {
            case SUCCESSFUL:
                printMessgae(" onUserDataResponse: get user id (" + userDataResponse.getUserData().getUserId()
                        + ", marketplace ("
                        + userDataResponse.getUserData().getMarketplace()
                        + ") ");
                iapManager.setAmazonUserId(userDataResponse.getUserData().getUserId(), userDataResponse.getUserData().getMarketplace());
                break;

            case FAILED:
            case NOT_SUPPORTED:
                printMessgae("onUserDataResponse failed, status code is " + status);
                iapManager.setAmazonUserId(null, null);
                break;
        }


    }

    @Override
    public void onProductDataResponse(ProductDataResponse productDataResponse) {

        final ProductDataResponse.RequestStatus status = productDataResponse.getRequestStatus();
        System.out.println(TAG + " onProductDataResponse: RequestStatus (" + status + ")");

        switch (status) {
            case SUCCESSFUL:
                System.out.println(TAG + "onProductDataResponse: successful.  The item data map in this response includes the valid SKUs");
                final Set<String> unavailableSkus = productDataResponse.getUnavailableSkus();
                System.out.println(TAG + " onProductDataResponse: " + unavailableSkus.size() + " unavailable skus");
                System.out.println(TAG + " onProductDataResponse: " + productDataResponse.getProductData().size() + " available skus");
                System.out.println(TAG + " onProductDataResponse---------: " + productDataResponse.getProductData().toString());
                iapManager.enablePurchaseForSkus(productDataResponse.getProductData());
                iapManager.disablePurchaseForSkus(productDataResponse.getUnavailableSkus());
                iapManager.refreshSubscriptionAvailablity();

                break;
            case FAILED:
            case NOT_SUPPORTED:
                System.out.println(TAG + "onProductDataResponse: failed, should retry request");
                iapManager.disableAllPurchases();
                break;
        }

    }

    @Override
    public void onPurchaseResponse(PurchaseResponse purchaseResponse) {
        final String requestId = purchaseResponse.getRequestId().toString();
        final String userId = purchaseResponse.getUserData().getUserId();
        final PurchaseResponse.RequestStatus status = purchaseResponse.getRequestStatus();
        printMessgae(" onPurchaseResponse: requestId (" + requestId
                + ") userId ("
                + userId
                + ") purchaseRequestStatus ("
                + status
                + ")");

        switch (status) {
            case SUCCESSFUL:
                final Receipt receipt = purchaseResponse.getReceipt();
                System.out.println(TAG + " onPurchaseResponse: receipt json:" + receipt.toJSON());
                appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_SUCCESS,
                                                                        appCMSPresenter.AMAZON_IAP,
                                                                        appCMSPresenter.getLoggedInUser(),
                                                                        purchaseResponse.getUserData().getUserId(),
                                                                        receipt.getSku(),
                                                                        receipt.getReceiptId(), ""));
                iapManager.handleReceipt(purchaseResponse.getRequestId().toString(), receipt, purchaseResponse.getUserData());
                activity.onAmazonPurchaseSuccess();
               /*  PurchasingService.notifyFulfillment(purchaseResponse.getReceipt().getReceiptId(),
                        FulfillmentResult.FULFILLED);*/
                //  iapManager.handleReceipt(purchaseResponse.getRequestId().toString(), receipt, purchaseResponse.getUserData());
                //iapManager.reloadSubscriptionStatus();

                break;
            case ALREADY_PURCHASED:
                System.out.println(TAG +
                        "onPurchaseResponse: already purchased, you should verify the subscription purchase on your side and make sure the purchase was granted to customer");
                appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_ALREADY_PURCHASED,
                         appCMSPresenter.AMAZON_IAP,
                         appCMSPresenter.getLoggedInUser()
                        ,appCMSPresenter.getAmazonUserId(),
                        (purchaseResponse != null && purchaseResponse.getReceipt() != null) ? purchaseResponse.getReceipt().getSku() : "",
                        (purchaseResponse.getReceipt() != null ? purchaseResponse.getReceipt().getReceiptId() : null), ""));

                iapManager.reloadSubscriptionStatus();
                iapManager.handleReceipt();
                //iapManager.handleReceipt(purchaseResponse.getRequestId().toString(), receipt, purchaseResponse.getUserData());
                //iapManager.handleReceipt(purchaseResponse.getRequestId().toString(), purchaseResponse.getReceipt(), purchaseResponse.getUserData());
                break;
            case INVALID_SKU:
                System.out.println(TAG +
                        "onPurchaseResponse: invalid SKU!  onProductDataResponse should have disabled buy button already.");
                iapManager.purchaseFailed();
                final Set<String> unavailableSkus = new HashSet<String>();
                unavailableSkus.add(purchaseResponse.getReceipt().getSku());
                iapManager.disablePurchaseForSkus(unavailableSkus);
                activity.onAmazonPurchaseFailed();
                break;
            case FAILED:
            case NOT_SUPPORTED:
                appCMSPresenter.sendGaIAPEvent(com.viewlift.Utils.getIapEventBundle(appCMSPresenter.IAP_AMAZON_FAILED,
                        appCMSPresenter.AMAZON_IAP,
                        appCMSPresenter.getLoggedInUser(),
                        appCMSPresenter.getAmazonUserId(),
                        (purchaseResponse != null && purchaseResponse.getReceipt() != null) ? purchaseResponse.getReceipt().getSku() : "",
                        (purchaseResponse.getReceipt() != null ? purchaseResponse.getReceipt().getReceiptId() : null), ""));
                activity.onAmazonPurchaseFailed();

                System.out.println(TAG + "onPurchaseResponse: failed so remove purchase request from local storage");
                if (purchaseResponse != null && purchaseResponse.getReceipt() != null) {
                    // iapManager.purchaseFailed(purchaseResponse.getReceipt().getSku());

                }
                iapManager.purchaseFailed();
                break;
        }

    }

    @Override
    public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse) {

        printMessgae(" onPurchaseUpdatesResponse: requestId (" + purchaseUpdatesResponse.getRequestId()
                + ") purchaseUpdatesResponseStatus ("
                + purchaseUpdatesResponse.getRequestStatus()
                + ") userId ("
                + purchaseUpdatesResponse.getUserData().getUserId()
                + ")");
        final PurchaseUpdatesResponse.RequestStatus status = purchaseUpdatesResponse.getRequestStatus();
        switch (status) {
            case SUCCESSFUL:
                iapManager.setAmazonUserId(purchaseUpdatesResponse.getUserData().getUserId(), purchaseUpdatesResponse.getUserData().getMarketplace());
                if (purchaseUpdatesResponse != null && purchaseUpdatesResponse.getReceipts() != null && purchaseUpdatesResponse.getReceipts().size() > 0) {
                    iapManager.handleReceipt(purchaseUpdatesResponse);
                }

                if (purchaseUpdatesResponse.hasMore()) {
                    PurchasingService.getPurchaseUpdates(false);
                }
                iapManager.reloadSubscriptionStatus();
                break;
            case FAILED:
            case NOT_SUPPORTED:
                printMessgae("onProductDataResponse: failed, should retry request");
                iapManager.disableAllPurchases();
                break;
        }


    }

    private void printMessgae(String message) {
        System.out.println(TAG + " " + message);
    }
}

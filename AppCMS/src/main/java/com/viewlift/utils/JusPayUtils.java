package com.viewlift.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.fragment.app.FragmentTransaction;

import com.viewlift.R;
import com.viewlift.db.AppPreference;
import com.viewlift.models.data.appcms.api.AppCMSDeleteCardResponse;
import com.viewlift.models.data.appcms.api.CardData;
import com.viewlift.models.data.appcms.api.JuspayOrderRequest;
import com.viewlift.models.data.appcms.api.OfferCodeResponse;
import com.viewlift.models.data.appcms.api.OfferDiscount;
import com.viewlift.models.data.appcms.api.OrderStatus;
import com.viewlift.models.data.appcms.subscriptions.AppCMSUserSubscriptionPlanResult;
import com.viewlift.models.data.appcms.ui.authentication.ErrorResponse;
import com.viewlift.models.network.rest.AppCMSJuspayCall;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.AppCMSSavedCardsAdapter;
import com.viewlift.views.fragments.AppCMSRetryPromptFragment;
import com.viewlift.views.fragments.AppCMSThankYouFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import rx.functions.Action1;

import static com.viewlift.presenters.AppCMSPresenter.ACTION_LAUNCH_JUSPAY;

public class JusPayUtils {

    private final AppCMSPresenter appCMSPresenter;
    private final AppCMSJuspayCall appCMSJuspayCall;
    private String paymentMethod;
    private String offerCode;

    public JusPayUtils(AppCMSPresenter appCMSPresenter, AppCMSJuspayCall appCMSJuspayCall) {
        this.appCMSPresenter = appCMSPresenter;
        this.appCMSJuspayCall = appCMSJuspayCall;
    }

    public void upiTransaction(String upiId) {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            Context context = appCMSPresenter.getCurrentActivity();
            paymentMethod = context.getString(R.string.upi);
            JSONObject juspayPayload = new JSONObject();
            try {
                juspayPayload.put(context.getString(R.string.op_name), context.getString(R.string.upi_txn));
                juspayPayload.put(context.getString(R.string.payment_method), context.getString(R.string.upi));
                juspayPayload.put(context.getString(R.string.upi_sdk_present), false);
                juspayPayload.put(context.getString(R.string.cust_vpa), upiId);
                onStartPayment(juspayPayload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onCardPayment(String cardNo, String mm, String yy, String cvv, String name, boolean isSaved) {
        if (appCMSPresenter == null || appCMSPresenter.getCurrentActivity() == null)
            return;
        appCMSPresenter.showLoader();
        Context context = appCMSPresenter.getCurrentActivity();
        appCMSJuspayCall.getCardInfo(context.getString(R.string.app_cms_juspay_card_info_api_url, cardNo.substring(0, 6),
                appCMSPresenter.getMerchantId()), appCMSCardInfoResponse -> {
            boolean shouldCreateMandate = false;
            if (appCMSCardInfoResponse != null) {
                shouldCreateMandate = appCMSCardInfoResponse.isMandateSupport();
            }

            if (!shouldCreateMandate && appCMSPresenter.getAllowedPayMethods() != null) {
                appCMSPresenter.showDialog(AppCMSPresenter.DialogType.CARD_NOT_SUPPORT_RECURRING_PAYMENT,
                        appCMSPresenter.getCurrentActivity().getString(R.string.recurring_payment_msg),
                        false, null, null, appCMSPresenter.getCurrentActivity().getString(R.string.card));
                appCMSPresenter.stopLoader();
                return;
            }

            paymentMethod = context.getString(R.string.card);
            try {
                JSONObject juspayPayload = new JSONObject();
                juspayPayload.put(context.getString(R.string.op_name), context.getString(R.string.card_txn));
                juspayPayload.put(context.getString(R.string.cardNumber), cardNo);
                juspayPayload.put(context.getString(R.string.card_exp_month), mm);
                juspayPayload.put(context.getString(R.string.card_exp_year), yy);
                juspayPayload.put(context.getString(R.string.name_on_card), name);
                juspayPayload.put(context.getString(R.string.card_security_code), cvv);
                juspayPayload.put(context.getString(R.string.save_to_locker), isSaved);
                juspayPayload.put(context.getString(R.string.should_create_mandate), shouldCreateMandate);
                onStartPayment(juspayPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void onPayCard(CardData card, String cvv) {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            Context context = appCMSPresenter.getCurrentActivity();
            paymentMethod = context.getString(R.string.save_card);
            try {
                JSONObject juspayPayload = new JSONObject();
                juspayPayload.put(context.getString(R.string.op_name), context.getString(R.string.card_txn));
                juspayPayload.put(context.getString(R.string.card_token), card.getCardToken());
                juspayPayload.put(context.getString(R.string.card_security_code), cvv);
                onStartPayment(juspayPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void urlBasedPayments(String type, String method) {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            Context context = appCMSPresenter.getCurrentActivity();
            paymentMethod = method;
            JSONObject juspayPayload = new JSONObject();
            try {
                juspayPayload.put(context.getString(R.string.op_name), type.equalsIgnoreCase(context.getString(R.string.nb)) ? context.getString(R.string.nb_txn) : context.getString(R.string.wallet_txn));
                juspayPayload.put(context.getString(R.string.payment_method), method);
                if (method.contains(context.getString(R.string.paypal))) {
                    juspayPayload.put(context.getString(R.string.sdk_present), context.getString(R.string.android_paypal));
                } else if (method.contains(context.getString(R.string.amazon_pay))) {
                    juspayPayload.put(context.getString(R.string.sdk_present), context.getString(R.string.android_amazon_pay_non_tokenized));
                } else if (method.contains(context.getString(R.string.phonepe))) {
                    juspayPayload.put(context.getString(R.string.sdk_present), context.getString(R.string.android_phonepe));
                } else if (method.contains(context.getString(R.string.google_pay))) {
                    juspayPayload.put(context.getString(R.string.sdk_present), context.getString(R.string.android_google_pay));
                    if (appCMSPresenter.getAppPreference() != null && !TextUtils.isEmpty(appCMSPresenter.getAppPreference().getLoggedInUserPhone())) {
                        juspayPayload.put(context.getString(R.string.wallet_mobile_number), appCMSPresenter.getAppPreference().getLoggedInUserPhone());
                    }
                } else if (method.contains(context.getString(R.string.jp_paytm))) {
                    juspayPayload.put(context.getString(R.string.mandate_type), context.getString(R.string.emandate));
                    juspayPayload.put(context.getString(R.string.should_create_mandate), true);
                }
                onStartPayment(juspayPayload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onStartPayment(JSONObject juspayPayload) {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            appCMSPresenter.showLoader();
            Intent juspayIntent = new Intent(ACTION_LAUNCH_JUSPAY);
            juspayIntent.putExtra(appCMSPresenter.getCurrentActivity().getString(R.string.juspay_payload), juspayPayload.toString());
            juspayIntent.putExtra(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_plan_id), appCMSPresenter.getPlanToPurchase());
            appCMSPresenter.getCurrentActivity().sendBroadcast(juspayIntent);
        }
    }

    public void endSubscriptionCall(Intent data) {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            try {
                OrderStatus status = (OrderStatus) data.getSerializableExtra(appCMSPresenter.getCurrentActivity().getString(R.string.status));
                if (status != null && appCMSPresenter.getCurrentActivity().getString(R.string.charged).equalsIgnoreCase(status.getStatus())) {
//                    appCMSPresenter.finalizeSignupAfterCCAvenueSubscription(null);
                    moveToThankYouPage(status);
                } else {
                    if (status != null && !TextUtils.isEmpty(status.getOrderId()))
                        appCMSPresenter.sendEventSubscriptionFailure(status.getOrderId());
                    else
                        appCMSPresenter.sendEventSubscriptionFailure(null);
                    moveToRetryPaymentPRomtPage();
                    //moveToThankYouPage(status);
                }
            } catch (Exception e) {
                e.printStackTrace();
                appCMSPresenter.sendEventSubscriptionFailure(null);
                //moveToThankYouPage(null);
                moveToRetryPaymentPRomtPage();
            }
        }
    }

    private void moveToThankYouPage(OrderStatus status) {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            new Handler().postDelayed(() -> {
                FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(AppCMSThankYouFragment.newInstance(status), "AppCMSThankYouFragment");
                fragmentTransaction.commitAllowingStateLoss();
            }, 100);
        }
    }


    private void moveToRetryPaymentPRomtPage() {
        if (appCMSPresenter != null && appCMSPresenter.getCurrentActivity() != null) {
            new Handler().postDelayed(() -> {
                FragmentTransaction fragmentTransaction = appCMSPresenter.getCurrentActivity().getSupportFragmentManager().beginTransaction();
                AppCMSRetryPromptFragment appCMSRetryPromptFragment = AppCMSRetryPromptFragment.newInstance("AppCMSRetryPromptFragment");
                appCMSRetryPromptFragment.show(fragmentTransaction, "appCMSRetryPromptFragment");
            }, 100);
        }
    }

    public void createJusPayOrder(String mobileNumber, String planId, String skuToPurchase, String currencyCode, Action1<AppCMSUserSubscriptionPlanResult> juspayOrderAction) {
        if (appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppPreference() != null) {
            AppPreference appPreference = appCMSPresenter.getAppPreference();
            JuspayOrderRequest orderRequest = new JuspayOrderRequest(appPreference.getLoggedInUser(), appPreference.getLoggedInUserEmail(),
                    mobileNumber, planId, skuToPurchase, currencyCode, appCMSPresenter.getAppCMSMain().getDomainName(), offerCode);
            Log.d("orderRequest", ": " + orderRequest.toString());
            appCMSJuspayCall.createOrder(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_juspay_create_order_api_url,
                    appCMSPresenter.getAppCMSMain().getApiBaseUrl()), appCMSPresenter.getAuthToken(), appCMSPresenter.getApiKey(), juspayOrderAction, orderRequest);
        }
    }

    public void getSavedCardList(AppCMSSavedCardsAdapter appCMSSavedCardsAdapter) {
        if (appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppPreference() != null) {
            appCMSJuspayCall.getSavedCards(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_juspay_card_list_api_url,
                    appCMSPresenter.getAppCMSMain().getApiBaseUrl(), appCMSPresenter.getAppPreference().getLoggedInUser()),
                    appCMSPresenter.getAuthToken(), appCMSPresenter.getApiKey(), appCMSCardsList -> {
                        if (appCMSSavedCardsAdapter != null && appCMSCardsList != null && appCMSCardsList.getCardDataResult() != null && appCMSCardsList.getCardDataResult().getCardsList() != null) {
                            appCMSSavedCardsAdapter.addAll(appCMSCardsList.getCardDataResult().getCardsList());
                        }
                    });
        }
    }

    public void deleteSavedCard(String cardToken, Action1<AppCMSDeleteCardResponse> cardDeleteAction) {
        if (appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getAppCMSMain() != null) {
            appCMSPresenter.showLoadingDialog(true);
            HashMap<String, String> params = new HashMap<>();
            params.put(appCMSPresenter.getCurrentActivity().getString(R.string.card_token), cardToken);
            appCMSJuspayCall.deleteSavedCard(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_juspay_delete_card_api_url,
                    appCMSPresenter.getAppCMSMain().getApiBaseUrl()), appCMSPresenter.getAuthToken(), appCMSPresenter.getApiKey(), params, cardDeleteAction);
        }
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }


    public void validateOfferCode(String offerCode, String planId, Action1<Pair<OfferCodeResponse, ErrorResponse>> validateOfferCodeAction) {
        if (appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getAppCMSMain() != null) {
            appCMSPresenter.showLoadingDialog(true);
            HashMap<String, String> params = new HashMap<>();
            params.put(appCMSPresenter.getCurrentActivity().getString(R.string.offer_code), offerCode);
            params.put(appCMSPresenter.getCurrentActivity().getString(R.string.subscription_plan_id), planId);
            params.put(appCMSPresenter.getCurrentActivity().getString(R.string.site), appCMSPresenter.getAppCMSMain().getInternalName());
            appCMSJuspayCall.validateOfferCode(appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_offer_validate_api_url,
                    appCMSPresenter.getAppCMSMain().getApiBaseUrl(), appCMSPresenter.getAppCMSMain().getInternalName()), appCMSPresenter.getAuthToken(), appCMSPresenter.getApiKey(), params, validateOfferCodeAction);
        }
    }

    public void calculateDiscount(String offerCode, String planId,Action1<Pair<OfferDiscount, ErrorResponse>> offerDiscountAction1) {
        if (appCMSPresenter.getCurrentActivity() != null && appCMSPresenter.getAppCMSMain() != null && appCMSPresenter.getAppPreference() != null) {
            appCMSPresenter.showLoadingDialog(true);
            String authToken = appCMSPresenter.getAuthToken();
            String url = appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_offer_discount_api_url,
                    appCMSPresenter.getAppCMSMain().getApiBaseUrl(), appCMSPresenter.getAppPreference().getLoggedInUser(),
                    CommonUtils.getCountryCodeFromAuthToken(authToken), planId, appCMSPresenter.getAppCMSMain().getInternalName(),
                    offerCode);
            if (appCMSPresenter.getLaunchType() == AppCMSPresenter.LaunchType.TVOD_PURCHASE) {
                String purchaseType = appCMSPresenter.getCurrentActivity().getResources().getStringArray(R.array.purchase_type)[0];
                if (appCMSPresenter.getContentToPurchase().isRent()) {
                    purchaseType = appCMSPresenter.getCurrentActivity().getResources().getStringArray(R.array.purchase_type)[1];
                }
                url = appCMSPresenter.getCurrentActivity().getString(R.string.app_cms_tvod_offer_discount_api_url,
                        appCMSPresenter.getAppCMSMain().getApiBaseUrl(), appCMSPresenter.getAppPreference().getLoggedInUser(),
                        CommonUtils.getCountryCodeFromAuthToken(authToken), planId, appCMSPresenter.getAppCMSMain().getInternalName(),
                        offerCode, purchaseType);
            }
            appCMSJuspayCall.calculateDiscount(url, authToken, appCMSPresenter.getApiKey(), offerDiscountAction1);
        }
    }

    public void setOfferCode(String offerCode) {
        this.offerCode = offerCode;
    }

    public String getOfferCode() {
        return offerCode;
    }
}

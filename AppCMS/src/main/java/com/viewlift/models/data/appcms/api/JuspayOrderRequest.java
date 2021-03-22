package com.viewlift.models.data.appcms.api;

import android.util.Base64;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JuspayOrderRequest {

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("customer_email")
    @Expose
    private String customerEmail;

    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;

    @SerializedName("udf4")
    @Expose
    private  String udf4;

    @SerializedName("udf7")
    @Expose
    private String udf7;

    @SerializedName("udf8")
    @Expose
    private String udf8;

    @SerializedName("udf9")
    @Expose
    private String udf9;

    @SerializedName("udf10")
    @Expose
    private String udf10;

    @SerializedName("options.get_client_auth_token")
    @Expose
    private boolean clientAuthToken;

    @SerializedName("options.create_mandate")
    @Expose
    private String createMandate;

    public JuspayOrderRequest(String custId, String custEmail, String mobileNumber, String planId, String planIdentifier,
                              String currency, String endUrl, String offerCode) {
        this.currency      = currency;
        this.customerId    = custId;
        this.customerEmail = custEmail;
        this.mobileNumber  = mobileNumber.replace("+", "");
        this.udf4          = planIdentifier;
        this.udf7          = "android_phone";
        this.udf8          = planId;
        this.udf9          = Base64.encodeToString(endUrl.getBytes(), Base64.NO_WRAP);
        this.udf10         = offerCode;
        clientAuthToken    = true;
        createMandate      = "OPTIONAL";
    }

    @NonNull
    @Override
    public String toString() {
        return "JuspayOrderRequest{currency='" + currency + '\'' +
                ", customer_id='" + customerId + '\'' +
                ", customer_email='" + customerEmail + '\'' +
                ", mobile_number='" + mobileNumber + '\'' +
                ", udf4='" + udf4 + '\'' +
                ", udf7='" + udf7 + '\'' +
                ", udf8='" + udf8 + '\'' +
                ", udf9='" + udf9 + '\'' +
                ", options.get_client_auth_token=" + clientAuthToken +
                ", options.create_mandate=" + createMandate +
                '}';
    }
}
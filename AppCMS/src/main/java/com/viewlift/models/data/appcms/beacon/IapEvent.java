package com.viewlift.models.data.appcms.beacon;

import android.os.Build;
import android.os.Bundle;

public class IapEvent {
    private String action;
    private String category;
    private String vlUser;
    private String amazonUser;
    private String planIdentifier;
    private String receiptId;
    private String errorMsg;

    public IapEvent(String action, String category, String vlUser, String amazonUser,String planIdentifier , String receiptId, String errorMsg){
        this.action = action;
        this.category = category;
        this.vlUser = vlUser;
        this.amazonUser = amazonUser;
        this.planIdentifier = planIdentifier;
        this.receiptId = receiptId;
        this.errorMsg = errorMsg;
    }

   public Bundle getIapEventBundle(){
        Bundle params = new Bundle();
        params.putString("category", this.category);
        params.putString("action", this.action);
        params.putString("vl_user", this.vlUser);
        params.putString("amazon_user", this.amazonUser);
        params.putString("plan_identifier",this.planIdentifier);
        params.putString("receiptid",this.receiptId);
        if(errorMsg != null && errorMsg.length() < 130){
            params.putString("error_msg",this.errorMsg);
        }else if(errorMsg != null){
            params.putString("error_msg",errorMsg.substring(0,130));
        }
        params.putString("device_detail", Build.MODEL + "/ SDK :"+Build.VERSION.SDK_INT);
        return params;
    }
}

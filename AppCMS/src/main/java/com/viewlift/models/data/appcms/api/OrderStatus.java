package com.viewlift.models.data.appcms.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vimeo.stag.UseStag;

import java.io.Serializable;

@UseStag
public class OrderStatus implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("orderId")
    @Expose
    private String orderId;

    @SerializedName("responseMessage")
    @Expose
    private ResponseMessage responseMessage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(ResponseMessage responseMessage) {
        this.responseMessage = responseMessage;
    }

    public class ResponseMessage implements Serializable {

        @SerializedName("code")
        @Expose
        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }
}
package com.viewlift.models.network.rest;

import com.viewlift.models.data.appcms.api.VerimatrixShortCodeApiRequest;
import com.viewlift.models.data.verimatrix.VerimatrixResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by viewlift on 12/21/17.
 */

public class VerimatrixCall {
    private static final String TAG = "VerimatrixCall";

    private final VerimatrixRest verimatrixRest;

    @Inject
    public VerimatrixCall(VerimatrixRest verimatrixRest) {
        this.verimatrixRest = verimatrixRest;
    }

    public void getTvProviders(String url, final Action1<VerimatrixResponse> verimatrixResponseAction) {
        verimatrixRest.chooser(url).enqueue(new Callback<VerimatrixResponse>() {
            @Override
            public void onResponse(Call<VerimatrixResponse> call, Response<VerimatrixResponse> response) {
                VerimatrixResponse verimatrixResponse = response.body();
                if (verimatrixResponse != null && verimatrixResponse.getProviders() != null)
                    verimatrixResponse.setProviderIdp(verimatrixResponse.getProviders());
                Observable.just(verimatrixResponse)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(verimatrixResponseAction);
            }

            @Override
            public void onFailure(Call<VerimatrixResponse> call, Throwable t) {
                verimatrixResponseAction.call(null);
            }
        });

    }

    public void call(String url, final Action1<VerimatrixResponse> verimatrixResponseAction,String type , String fedData) {
        verimatrixRest.shortCode(url,new VerimatrixShortCodeApiRequest(type,fedData)).enqueue(new Callback<VerimatrixResponse>() {
            @Override
            public void onResponse(Call<VerimatrixResponse> call, Response<VerimatrixResponse> response) {
                VerimatrixResponse verimatrixResponse = response.body();
                if (verimatrixResponse != null && verimatrixResponse.getProviders() != null)
                    verimatrixResponse.setProviderIdp(verimatrixResponse.getProviders());
                Observable.just(verimatrixResponse)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(verimatrixResponseAction);
            }

            @Override
            public void onFailure(Call<VerimatrixResponse> call, Throwable t) {
                verimatrixResponseAction.call(null);
            }
        });
    }

    public void verimatrixPollingApiCall(String url, final Action1<VerimatrixResponse> verimatrixResponseAction) {
        verimatrixRest.polling(url).enqueue(new Callback<VerimatrixResponse>() {
            @Override
            public void onResponse(Call<VerimatrixResponse> call, Response<VerimatrixResponse> response) {
                VerimatrixResponse verimatrixResponse = response.body();
                if (verimatrixResponse != null && verimatrixResponse.getProviders() != null)
                    verimatrixResponse.setProviderIdp(verimatrixResponse.getProviders());
                Observable.just(verimatrixResponse)
                        .onErrorResumeNext(throwable -> Observable.empty())
                        .subscribe(verimatrixResponseAction);
            }

            @Override
            public void onFailure(Call<VerimatrixResponse> call, Throwable t) {
                verimatrixResponseAction.call(null);
            }
        });
    }

    public void callVerimatrixSloApi(String url,String type , String fedData) {
        verimatrixRest.shortCode(url,new VerimatrixShortCodeApiRequest(type,fedData)).enqueue(new Callback<VerimatrixResponse>() {

            @Override
            public void onResponse(Call<VerimatrixResponse> call, Response<VerimatrixResponse> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<VerimatrixResponse> call, Throwable t) {

            }
        });
    }


}

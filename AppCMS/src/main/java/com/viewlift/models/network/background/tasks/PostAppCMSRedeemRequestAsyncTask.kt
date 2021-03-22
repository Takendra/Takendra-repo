package com.viewlift.models.network.background.tasks

import com.viewlift.models.data.appcms.api.RedeemApiResponse
import com.viewlift.models.network.rest.AppCMSRedeemCall
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * This class is responsible for API calls to check redemption codes
 * @author  Vinay
 * @since   2019-01-21
 */

class PostAppCMSRedeemRequestAsyncTask(val call: AppCMSRedeemCall,
                                       val readyAction: Consumer<RedeemApiResponse>, val apiKey: String, val authToken: String) {

    class Params {
        var url: String? = null
        var redemptionCode: String? = null
        var site: String? = null
        var platform: String? = null
        var siteId: String? = null
        var transaction: String? = null
        var userId: String? = null
        var purchaseType: String? = null
        var currencyCode: String? = null

        class Builder(val params: Params = Params()) {
            fun url(url: String): Builder {
                params.url = url
                return this
            }

            fun redemptionCode(redemptionCode: String): Builder {
                params.redemptionCode = redemptionCode
                return this
            }

            fun site(site: String): Builder {
                params.site = site
                return this
            }

            fun platform(platform: String): Builder {
                params.platform = platform
                return this
            }

            fun siteId(siteId: String): Builder {
                params.siteId = siteId
                return this
            }

            fun transaction(transaction: String): Builder {
                params.transaction = transaction
                return this
            }

            fun userId(userId: String): Builder {
                params.userId = userId
                return this
            }

            fun purchaseType(purchaseType: String): Builder {
                params.purchaseType = purchaseType
                return this
            }

            fun currencyCode(currencyCode: String): Builder {
                params.currencyCode = currencyCode
                return this
            }

            fun build(): Params {
                return params
            }

        }
    }


    /**
     * API call to redeem the validated coupon
     * @param params - contains API url, redemption code, site, platform, siteid, transaction, userid, purchaseType, currencyCode
     * @author  Vinay
     * */
    fun redeemCoupon(params: Params) {
        Observable.fromCallable {
            return@fromCallable call.redeemCoupon(params.url, params.redemptionCode, params.site, params.platform,
                    params.siteId, params.transaction, params.userId, params.purchaseType,
                    params.currencyCode, apiKey, authToken)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { Observable.empty() }
                .subscribe { result: RedeemApiResponse? -> Observable.just(result).subscribe { readyAction.accept(it) } }
    }

    /**
     * API call to validate coupon code
     * @param params - contains API url, redemption code
     * @author  Vinay
     * @deprecated use [validateOffer]
     */
    @Deprecated("Not to be used after 15 Oct 2020")
    fun validateCoupon(params: Params) {
        Observable.fromCallable { return@fromCallable call.validateCoupon(params.url, params.redemptionCode, apiKey, authToken) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { Observable.empty() }
                .subscribe { result: RedeemApiResponse? -> Observable.just(result).subscribe { readyAction.accept(it) } }
    }

    /**
     * API call to validate offer code
     * @param params - contains API url, redemption code
     * @author Wishy
     * */
    fun validateOffer(params: Params) {
        Observable.fromCallable { return@fromCallable call.validateOffer(params.url, params.redemptionCode, apiKey, authToken) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { Observable.empty() }
                .subscribe { result: RedeemApiResponse? -> Observable.just(result).subscribe { readyAction.accept(it) } }
    }


}
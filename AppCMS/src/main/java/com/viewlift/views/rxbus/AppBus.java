package com.viewlift.views.rxbus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class AppBus {
    private static AppBus instance;

    public static AppBus instanceOf() {
        if (instance == null) {
            instance = new AppBus();
        }
        return instance;
    }

    private PublishSubject<Boolean> showPaymentOption = PublishSubject.create();

    public void setShowPaymentOption(boolean cancel) {
        try {
            showPaymentOption.onNext(cancel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<Boolean> getShowPaymentOption() {
        return showPaymentOption;
    }

}

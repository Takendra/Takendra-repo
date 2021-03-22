package com.viewlift.models.data.appcms.beacon;

import android.os.Handler;
import android.os.Looper;

public class BeaconHandler extends Handler {

    private BeaconRunnable mBeaconRunnable;

    public BeaconHandler(Looper mLooper) {
        super(mLooper);
    }

    public void handle(BeaconRunnable runnable) {
        this.mBeaconRunnable = runnable;
        postDelayed(mBeaconRunnable, 1000);
    }
}

package com.viewlift.views.airship;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.urbanairship.iam.DisplayHandler;
import com.urbanairship.iam.InAppMessage;
import com.urbanairship.iam.InAppMessageActivity;
import com.urbanairship.iam.InAppMessageAdapter;
import com.urbanairship.iam.assets.Assets;

public class AirshipInAppMessageAdapter implements InAppMessageAdapter {
    private InAppMessage message;
    private Assets assets;

    public AirshipInAppMessageAdapter(InAppMessage message) {
        this.message = message;
    }

    @Override
    @WorkerThread
    @PrepareResult
    public int onPrepare(@NonNull Context context, @NonNull Assets assets) {
        // Do any additional prepare steps
        this.assets = assets;
        return OK; // RETRY, CANCEL
    }

    @Override
    @MainThread
    public boolean isReady(@NonNull Context context) {
        // Called right before display.
        return true;
    }

    @Override
    @MainThread
    public void onDisplay(@NonNull Context context, @NonNull DisplayHandler displayHandler) {
        Intent intent = new Intent(context, AirshipInAppMessageActivity.class)
                .putExtra(InAppMessageActivity.IN_APP_MESSAGE_KEY, message)
                .putExtra(InAppMessageActivity.IN_APP_ASSETS, assets)
                .putExtra(InAppMessageActivity.DISPLAY_HANDLER_EXTRA_KEY, displayHandler);

        context.startActivity(intent);
    }

    @Override
    @WorkerThread
    public void onFinish(@NonNull Context context) {
        // Perform any clean up
    }
}

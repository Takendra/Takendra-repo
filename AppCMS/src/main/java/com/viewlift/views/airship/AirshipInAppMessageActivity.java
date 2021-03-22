package com.viewlift.views.airship;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.urbanairship.iam.InAppMessage;
import com.urbanairship.iam.InAppMessageActivity;
import com.urbanairship.iam.ResolutionInfo;
import com.urbanairship.iam.banner.BannerDisplayContent;

public class AirshipInAppMessageActivity extends InAppMessageActivity implements View.OnClickListener {


    // Called during onCreate
    @Override
    protected void onCreateMessage(@Nullable Bundle savedInstanceState) {
        // Bind message
        InAppMessage message = getMessage();
        BannerDisplayContent displayContent = message.getDisplayContent();
        if (displayContent == null) {
            Log.e("CustomActivity", "Started with wrong message type");
            finish();
        }

        // Bind view to message
    }

    @Override
    public void onClick(View v) {
        // Notify the display handler of any message resolutions before finishing the activity
        getDisplayHandler().finished(ResolutionInfo.messageClicked(), getDisplayTime());
        finish();
    }
}
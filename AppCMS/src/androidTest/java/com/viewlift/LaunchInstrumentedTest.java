package com.viewlift;

import android.content.Intent;
import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by viewlift on 5/9/17.
 */
@RunWith(AndroidJUnit4.class)
public class LaunchInstrumentedTest {
   /* @Rule
    public ActivityTestRule<AppCMSLaunchTestActivity> launchTestActivityActivityTestRule =
            new ActivityTestRule<>(AppCMSLaunchTestActivity.class, true, false);

    @Test
    public void test_launchActivity() throws Exception {
        launchTestActivityActivityTestRule.launchActivity(
                new Intent(InstrumentationRegistry.getTargetContext(), AppCMSLaunchTestActivity.class));
        onView(withId(R.id.error_fragment)).check(doesNotExist());
    }*/
}

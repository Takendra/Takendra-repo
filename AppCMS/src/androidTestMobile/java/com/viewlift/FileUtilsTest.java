package com.viewlift;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.viewlift.db.AppPreference;
import com.viewlift.utils.FileUtils;
import com.viewlift.views.activity.AppCMSErrorActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.assertTrue;

/**
 *test cases for FileUtils class
 */

@RunWith(AndroidJUnit4.class)
public class FileUtilsTest {

    Context appContext, context;

    @Mock
    AppPreference pref;

    @Rule
    public TemporaryFolder storageDirectory = new TemporaryFolder();

    @Rule
    public ActivityTestRule<AppCMSErrorActivity> mActivityTestRule = new ActivityTestRule<>(AppCMSErrorActivity.class);

    @Before
    public void setup() {
         appContext = InstrumentationRegistry.getTargetContext();
         context = InstrumentationRegistry.getContext();

         pref = new AppPreference(appContext);
    }

    @Test
    public void test_getStorageDirectories() {
        assertTrue(FileUtils.getStorageDirectories(appContext.getApplicationContext()).length > 0);
    }

    @Test
    public void test_getMegabytesAvailable() {
        assertTrue(FileUtils.getMegabytesAvailable(appContext.getApplicationContext(), false) > 0);
    }

    @Test
    public void test_isRemovableSDCardAvailable() {
        assertTrue(FileUtils.isRemovableSDCardAvailable(appContext.getApplicationContext()));
    }

    @Test
    public void test_getMegabytesAvailableInFile() {
        assertTrue(FileUtils.getMegabytesAvailable(storageDirectory.getRoot())> 0);
    }

    @Test
    public void test_isMemorySpaceAvailable() {
        assertTrue(FileUtils.isMemorySpaceAvailable(mActivityTestRule.getActivity(), pref));
    }


}

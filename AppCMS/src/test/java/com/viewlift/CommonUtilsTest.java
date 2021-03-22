package com.viewlift;

import android.content.Context;
import android.os.Build;

import com.viewlift.utils.CommonUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommonUtilsTest {

    @Rule
    public TemporaryFolder storageDirectory = new TemporaryFolder();

    @Test
    public void test_getDateFormatByTimeZone() {
        assertEquals(CommonUtils.getDateFormatByTimeZone(1568375408289L, "dd-MM-yy"), "13-09-19");
    }

    @Test
    public void test_addHourToMs() {
        long oneHrMs = 3600000; //1000 * 60 * 60;
        assertEquals(CommonUtils.addHourToMs(oneHrMs, 1), 7200000);
    }

    @Test
    public void test_getTimeIntervalForEventSchedule() {
        assertEquals(CommonUtils.getTimeIntervalForEventSchedule(System.currentTimeMillis(), "dd-MM-yy"), 0);
    }

    @Test
    public void test_isValidPhoneNumber() {
        assertTrue(CommonUtils.isValidPhoneNumber("+919999898988"));
        assertTrue(CommonUtils.isValidPhoneNumber("9999898988"));
        assertFalse(CommonUtils.isValidPhoneNumber("NA"));
        assertFalse(CommonUtils.isValidPhoneNumber("null"));
        assertFalse(CommonUtils.isValidPhoneNumber(null));
        assertFalse(CommonUtils.isValidPhoneNumber(""));
        assertFalse(CommonUtils.isValidPhoneNumber(" "));
    }

    @Test
    public void test_getDeviceName() {
        mock(Build.class);
        try {
            setFinalStatic(Build.class.getField("MANUFACTURER"), "test");
            setFinalStatic(Build.class.getField("MODEL"), "device");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(CommonUtils.getDeviceName(), "Test device");

        try {
            setFinalStatic(Build.class.getField("MANUFACTURER"), "test");
            setFinalStatic(Build.class.getField("MODEL"), "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(CommonUtils.getDeviceName(), "Test");
    }

    @Test
    public void test_getColor() {
        Context context = mock(Context.class);
        when(context.getString(R.string.color_hash_prefix)).thenReturn("#");
        assertEquals(CommonUtils.getColor(context, "000000"), "#000000");
        assertEquals(CommonUtils.getColor(context, null), "#d8d8d8");
    }

    @Test
    public void test_getMillisecondFromDateString() {
        assertEquals(CommonUtils.getMillisecondFromDateString("dd-MM-yy", "13-09-19"), 1568313000000L);
        // this will throw exception but will increase code coverage
        assertEquals(CommonUtils.getMillisecondFromDateString("dd-MM-yy", "0"), 0);
    }


    @Test
    public void test_getTimeIntervalForEvent() {
        assertTrue(CommonUtils.getTimeIntervalForEvent(System.currentTimeMillis(), "dd-MM-yy") <= 0);
    }



    /*
     * Utility method for mocking final fields
     */
    static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }




}

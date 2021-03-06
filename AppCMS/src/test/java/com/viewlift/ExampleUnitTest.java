package com.viewlift;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Map<String, String> testMap;

    public enum ExampleEnum {
        FIRST,
        SECOND,
        THIRD
    }

    @Before
    public void initializeMembers() {
        testMap = new HashMap<>();
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testMap_keyNotFound() throws Exception {
        assertNull(testMap.get("BadKey"));
    }

    @Test
    public void testExampleEnum_fromInt() throws Exception {
        ExampleEnum firstValue = ExampleEnum.FIRST;
        int i = firstValue.ordinal();
        ExampleEnum exampleEnum = ExampleEnum.values()[i];
        assertTrue(exampleEnum == ExampleEnum.FIRST);
    }
}
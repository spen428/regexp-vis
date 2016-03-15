package test.controller;

import org.junit.Test;

@SuppressWarnings({ "unused", "static-method" })
public class ActivityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgs0() throws Throwable {
        new TestActivity(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgs1() throws Throwable {
        new TestActivity(null, null, null);
    }

    @Test
    public void testDefaultMethods() {
        // Activity activity = new TestActivity(null, null);
        // TODO
    }

}

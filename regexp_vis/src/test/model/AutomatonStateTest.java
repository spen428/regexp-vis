package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import model.AutomatonState;

@SuppressWarnings("static-method")
public class AutomatonStateTest {

    @Test
    public void test0() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-275));
        boolean boolean0 = automatonState0.isFinal();
        assertFalse(boolean0);
        assertEquals(-275, automatonState0.getId());
    }

    @Test
    public void test1() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(0, true);
        int int0 = automatonState0.getId();
        assertTrue(automatonState0.isFinal());
        assertEquals(0, int0);
    }

    @Test
    public void test2() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-275));
        int int0 = automatonState0.getId();
        assertFalse(automatonState0.isFinal());
        assertEquals((-275), int0);
    }

    @Test
    public void test3() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-275));
        String string0 = automatonState0.toString();
        assertEquals("-275", string0);
    }

    @Test
    public void test4() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-275));
        assertFalse(automatonState0.isFinal());

        automatonState0.setFinal(true);
        automatonState0.toString();
        assertTrue(automatonState0.isFinal());
    }

    @Test
    public void test5() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(436);
        int int0 = automatonState0.getId();
        assertEquals(436, int0);
        assertFalse(automatonState0.isFinal());
    }

    @Test
    public void test6() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(0, true);
        boolean boolean0 = automatonState0.isFinal();
        assertTrue(boolean0);
        assertEquals(0, automatonState0.getId());
    }
}

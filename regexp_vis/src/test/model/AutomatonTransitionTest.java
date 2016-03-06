package test.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import model.AutomatonState;
import model.AutomatonTransition;
import model.BasicRegexp;

@SuppressWarnings({ "unused", "static-method" })
public class AutomatonTransitionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArg0() throws Throwable {
        new AutomatonTransition(0, null, new AutomatonState(0),
                new BasicRegexp('c'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArg1() throws Throwable {
        new AutomatonTransition(0, null, null, null);
    }

    @Test
    public void test00() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(0);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-1080), automatonState0, automatonState0,
                new BasicRegexp('c'));
        automatonTransition0.getTo();
        assertEquals(-1080, automatonTransition0.getId());
    }

    @Test
    public void test01() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(43, true);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(0,
                automatonState0, automatonState0, new BasicRegexp('c'));
        automatonTransition0.getTo();
        assertEquals(0, automatonTransition0.getId());
    }

    @Test
    public void test02() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-1240));
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-1240), automatonState0, automatonState0,
                new BasicRegexp('c'));
        automatonTransition0.getTo();
        assertEquals(-1240, automatonTransition0.getId());
    }

    @Test
    public void test03() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(0);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(0,
                automatonState0, automatonState0, new BasicRegexp('c'));
        int int0 = automatonTransition0.getId();
        assertEquals(0, int0);
    }

    @Test
    public void test04() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(0);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-1080), automatonState0, automatonState0,
                new BasicRegexp('c'));
        int int0 = automatonTransition0.getId();
        assertEquals((-1080), int0);
    }

    @Test
    public void test05() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(0, true);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(0,
                automatonState0, automatonState0, new BasicRegexp('c'));
        automatonTransition0.getFrom();
        assertEquals(0, automatonTransition0.getId());
    }

    @Test
    public void test06() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState(43, true);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(0,
                automatonState0, automatonState0, new BasicRegexp('c'));
        automatonTransition0.getFrom();
        assertEquals(0, automatonTransition0.getId());
    }

    @Test
    public void test07() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-1240));
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-1240), automatonState0, automatonState0,
                new BasicRegexp('c'));
        automatonTransition0.getFrom();
        assertEquals(-1240, automatonTransition0.getId());
    }

    @Test
    public void test08() throws Throwable {
        AutomatonState automatonState0 = new AutomatonState((-5492));
        AutomatonState automatonState1 = new AutomatonState((-3572), true);
        AutomatonTransition automatonTransition0 = new AutomatonTransition(
                (-2871), automatonState0, automatonState1,
                new BasicRegexp('c'));
        assertEquals(-2871, automatonTransition0.getId());
    }

    @Test
    public void test09() throws Throwable {
        AutomatonTransition automatonTransition0 = new AutomatonTransition(5141,
                new AutomatonState(0), new AutomatonState(1),
                new BasicRegexp('c'));
        int int0 = automatonTransition0.getId();
        assertEquals(5141, int0);
    }

    @Test
    public void test10() throws Throwable {
        AutomatonTransition automatonTransition0 = new AutomatonTransition(5141,
                new AutomatonState(0), new AutomatonState(1),
                new BasicRegexp('c'));
        automatonTransition0.getData();
        assertEquals(5141, automatonTransition0.getId());
    }

    @Test
    public void test11() throws Throwable {
        AutomatonTransition automatonTransition0 = new AutomatonTransition(5141,
                new AutomatonState(0), new AutomatonState(1),
                new BasicRegexp('c'));
        automatonTransition0.getFrom();
        assertEquals(5141, automatonTransition0.getId());
    }

    @Test
    public void test12() throws Throwable {
        AutomatonTransition automatonTransition0 = new AutomatonTransition(5141,
                new AutomatonState(0), new AutomatonState(1),
                new BasicRegexp('c'));
        automatonTransition0.getTo();
        assertEquals(5141, automatonTransition0.getId());
    }
}

/*
 * Copyright (c) 2015, 2016 Matthew J. Nicholls, Samuel Pengelly,
 * Parham Ghassemi, William R. Dix
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

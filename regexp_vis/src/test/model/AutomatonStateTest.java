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

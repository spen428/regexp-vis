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

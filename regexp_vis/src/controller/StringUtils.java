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
package controller;

import java.util.List;

import model.AutomatonTransition;

public class StringUtils {
    /**
     * For a list of transitions, turn that in to a string in natural English,
     * e.g. [a, b, c, d] into "a, b, c and d".
     *
     * @param list The list of transitions
     * @return A string describing the list in natural English
     */
    public static String transitionListToEnglish(
            List<AutomatonTransition> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).getData().toString());
            if (list.size() != 2 && i < list.size() - 1) {
                builder.append(",");
            }

            if (i == list.size() - 2) {
                /* Penultimate */
                builder.append(" and ");
            } else {
                builder.append(" ");
            }
        }

        return builder.toString();
    }
}

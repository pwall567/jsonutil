/*
 * @(#) JSONValue.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.pwall.json;

import java.io.IOException;
import java.io.Serializable;

/**
 * Main interface implemented by all forms of JSON value.
 *
 * @author Peter Wall
 */
public interface JSONValue extends Serializable {

    /**
     * Append the appropriate external representation for this JSON value to a given
     * {@link Appendable}.
     *
     * @param   a   the {@link Appendable}
     * @throws  IOException     if thrown by the {@link Appendable}
     */
    void appendJSON(Appendable a) throws IOException;

    /**
     * Create the appropriate external representation for this JSON value.
     *
     * @return  the JSON representation for this value
     */
    default String toJSON() {
        StringBuilder sb = new StringBuilder(12);
        try {
            appendJSON(sb);
        }
        catch (IOException e) {
            // can't happen - StringBuilder does not throw IOException
        }
        return sb.toString();
    }

}

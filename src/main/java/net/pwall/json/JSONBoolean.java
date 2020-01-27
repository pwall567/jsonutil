/*
 * @(#) JSONBoolean.java
 *
 * jsonutil JSON Utility Library
 * Copyright (c) 2014, 2015, 2020 Peter Wall
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
import java.util.Objects;

/**
 * A JSON boolean value.
 *
 * @author Peter Wall
 */
public class JSONBoolean implements JSONValue {

    private static final long serialVersionUID = -3294980363221183247L;

    /** Constant {@code JSONBoolean} value for FALSE */
    public static final JSONBoolean FALSE = new JSONBoolean(false);
    /** Constant {@code JSONBoolean} value for TRUE */
    public static final JSONBoolean TRUE = new JSONBoolean(true);

    private final boolean value;

    public JSONBoolean(boolean value) {
        this.value = value;
    }

    public JSONBoolean(Boolean value) {
        this.value = Objects.requireNonNull(value);
    }

    public boolean get() {
        return value;
    }

    public boolean booleanValue() {
        return value;
    }

    @Override
    public String toJSON() {
        return value ? "true" : "false";
    }

    @Override
    public void appendJSON(Appendable a) throws IOException {
        a.append(toJSON());
    }

    @Override
    public String toString() {
        return toJSON();
    }

    @Override
    public int hashCode() {
        return value ? 1 : 0;
    }

    @Override
    public boolean equals(Object other) {
        return other == this ||
                other instanceof JSONBoolean && value == ((JSONBoolean)other).get();
    }

    public static JSONBoolean valueOf(boolean b) {
        return (b ? TRUE : FALSE);
    }

}

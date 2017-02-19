/*
 * @(#) TestParse.java
 */

package net.pwall.json;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import net.pwall.util.ReaderBuffer;

/**
 * Test various forms of parsing.
 */
public class TestParse {

    private static final String exampleString = "{\"aaa\":1234}";

    @Test
    public void test() throws IOException {
        JSONValue result = JSON.parse(exampleString);
        assertTrue(result instanceof JSONObject);
        JSONObject object = (JSONObject)result;
        JSONValue value = object.get("aaa");
        assertTrue(value instanceof JSONInteger);
        assertEquals(new JSONInteger(1234), value);

        result = JSON.parse(new ReaderBuffer(new StringReader(exampleString)));
        assertTrue(result instanceof JSONObject);
        object = (JSONObject)result;
        value = object.get("aaa");
        assertTrue(value instanceof JSONInteger);
        assertEquals(new JSONInteger(1234), value);
    }

}

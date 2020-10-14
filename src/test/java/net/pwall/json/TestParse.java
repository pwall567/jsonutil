/*
 * @(#) TestParse.java
 */

package net.pwall.json;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test various forms of parsing.
 */
class TestParse {

    private static final String exampleString = "{\"aaa\":1234,\"bbb\":true}";

    @Test
    void test() throws IOException {
        JSONValue result = JSON.parse(exampleString);
        assertTrue(result instanceof JSONObject);
        JSONObject object = (JSONObject)result;
        JSONValue value = object.get("aaa");
        assertTrue(value instanceof JSONInteger);
        assertEquals(new JSONInteger(1234), value);
        value = object.get("bbb");
        assertTrue(value instanceof JSONBoolean);
        assertEquals(JSONBoolean.TRUE, value);

        result = JSON.parse(new StringReader(exampleString));
        assertTrue(result instanceof JSONObject);
        object = (JSONObject)result;
        value = object.get("aaa");
        assertTrue(value instanceof JSONInteger);
        assertEquals(new JSONInteger(1234), value);
        value = object.get("bbb");
        assertTrue(value instanceof JSONBoolean);
        assertEquals(JSONBoolean.TRUE, value);
    }

}

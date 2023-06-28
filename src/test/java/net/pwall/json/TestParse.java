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

    @Test
    void shouldAllowMassivelyNestedArray() {
        int testDepth = 1000;
        StringBuilder sb = new StringBuilder(testDepth * 2 + 1);
        for (int i = 0; i < testDepth; i++)
            sb.append('[');
        sb.append('1');
        for (int i = 0; i < testDepth; i++)
            sb.append(']');
        JSONValue result = JSON.parse(sb);
        assertTrue(result instanceof JSONArray);
        for (int i = 0; i < testDepth; i++)
            result = ((JSONArray)result).get(0);
        assertTrue(result instanceof JSONInteger);
        assertEquals(1, ((JSONInteger)result).getValue());
    }

    @Test
    void shouldThrowExceptionOnExcessiveNesting() {
        int testDepth = 1001;
        StringBuilder sb = new StringBuilder(testDepth);
        for (int i = 0; i < testDepth; i++)
            sb.append('[');
        JSONException e = assertThrows(JSONException.class, () -> JSON.parse(sb));
        assertEquals(JSON.MAX_DEPTH_EXCEEDED, e.getMessage());
        sb.setLength(0);
        for (int i = 0; i < testDepth; i++)
            sb.append("{\"a\":");
        e = assertThrows(JSONException.class, () -> JSON.parse(sb));
        assertEquals(JSON.MAX_DEPTH_EXCEEDED, e.getMessage());
    }

    @Test
    void shouldAllowNestingDepthToBeIncreased() {
        int oldDepth = JSON.getMaxDepth();
        int testDepth = 1200;
        JSON.setMaxDepth(testDepth);
        StringBuilder sb = new StringBuilder(testDepth * 2 + 1);
        for (int i = 0; i < testDepth; i++)
            sb.append('[');
        sb.append('1');
        for (int i = 0; i < testDepth; i++)
            sb.append(']');
        JSONValue result = JSON.parse(sb);
        assertTrue(result instanceof JSONArray);
        for (int i = 0; i < testDepth; i++)
            result = ((JSONArray)result).get(0);
        assertTrue(result instanceof JSONInteger);
        assertEquals(1, ((JSONInteger)result).getValue());
        JSON.setMaxDepth(oldDepth);
    }

}

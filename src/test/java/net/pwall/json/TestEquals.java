package net.pwall.json;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for equality and hash code functions..
 *
 * @author Peter Wall
 */

class TestEquals {

    @Test
    void shouldCompareNumbersAsEqual() {
        JSONValue int1 = new JSONInteger(1);
        JSONValue long1 = new JSONLong(1L);
        JSONValue bigDecimal1 = new JSONDecimal(BigDecimal.ONE);
        JSONValue bigDecimal10 = new JSONDecimal(new BigDecimal("1.0"));
        assertEquals(int1, long1);
        assertEquals(int1, bigDecimal1);
        assertEquals(int1, bigDecimal10);
        assertEquals(long1, bigDecimal1);
        assertEquals(long1, bigDecimal10);
        assertEquals(bigDecimal1, bigDecimal10);
    }

    @Test
    void shouldCompareLongerNumbersAsEqual() {
        JSONValue int12345 = new JSONInteger(12345);
        JSONValue long12345 = new JSONLong(12345L);
        JSONValue bigDecimal12345 = new JSONDecimal(new BigDecimal(12345));
        JSONValue bigDecimal1234500 = new JSONDecimal(new BigDecimal("12345.00"));
        assertEquals(int12345, long12345);
        assertEquals(int12345, bigDecimal12345);
        assertEquals(int12345, bigDecimal1234500);
        assertEquals(long12345, bigDecimal12345);
        assertEquals(long12345, bigDecimal1234500);
        assertEquals(bigDecimal12345, bigDecimal1234500);
    }

    @Test
    void shouldCompareHashCodesAsEqual() {
        JSONValue int1 = new JSONInteger(1);
        JSONValue long1 = new JSONLong(1L);
        JSONValue bigDecimal1 = new JSONDecimal(BigDecimal.ONE);
        JSONValue bigDecimal10 = new JSONDecimal(new BigDecimal("1.0"));
        assertEquals(int1.hashCode(), long1.hashCode());
        assertEquals(int1.hashCode(), bigDecimal1.hashCode());
        assertEquals(int1.hashCode(), bigDecimal10.hashCode());
        assertEquals(long1.hashCode(), bigDecimal1.hashCode());
        assertEquals(long1.hashCode(), bigDecimal10.hashCode());
        assertEquals(bigDecimal1.hashCode(), bigDecimal10.hashCode());
    }

    @Test
    void shouldCompareLongerNumberHashCodesAsEqual() {
        JSONValue int12345 = new JSONInteger(12345);
        JSONValue long12345 = new JSONLong(12345L);
        JSONValue bigDecimal12345 = new JSONDecimal(new BigDecimal(12345));
        JSONValue bigDecimal1234500 = new JSONDecimal(new BigDecimal("12345.00"));
        assertEquals(int12345.hashCode(), long12345.hashCode());
        assertEquals(int12345.hashCode(), bigDecimal12345.hashCode());
        assertEquals(int12345.hashCode(), bigDecimal1234500.hashCode());
        assertEquals(long12345.hashCode(), bigDecimal12345.hashCode());
        assertEquals(long12345.hashCode(), bigDecimal1234500.hashCode());
        assertEquals(bigDecimal12345.hashCode(), bigDecimal1234500.hashCode());
    }

}

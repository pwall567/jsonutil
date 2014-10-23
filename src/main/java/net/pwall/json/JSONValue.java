/*
 * @(#) JSONValue.java
 */

package net.pwall.json;

/**
 * Interface for JSON values.
 *
 * @author Peter Wall
 */
public interface JSONValue {

    /**
     * Create the appropriate external representation for this JSON value.
     *
     * @return  the JSON representation for this value
     */
    String toJSON();

}

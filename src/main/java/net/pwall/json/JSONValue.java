/*
 * @(#) JSONValue.java
 */

package net.pwall.json;

import java.io.IOException;

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

    /**
     * Append the appropriate external representation for this JSON value to a given
     * {@link Appendable}.
     *
     * @param   a   the {@link Appendable}
     * @throws  IOException     if thrown by the {@link Appendable}
     */
    void appendJSON(Appendable a) throws IOException;

}

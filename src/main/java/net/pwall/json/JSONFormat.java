/*
 * @(#) JSONFormat.java
 */

package net.pwall.json;

import java.io.IOException;

import net.pwall.util.Strings;

/**
 * Class to hold formatting options for JSON output.
 */
public class JSONFormat {

    public static final int DEFAULT_INDENTATION = 2;
    public static final String LINE_TERMINATOR = System.getProperty("line.separator");

    private int currentIndentation;
    private int indentationIncrement;
    private boolean newlineRequired;

    private static JSONFormat instance = new JSONFormat();

    public JSONFormat(int currentIndentation, int indentationIncrement,
            boolean newlineRequired) {
        this.currentIndentation = currentIndentation;
        this.indentationIncrement = indentationIncrement;
        this.newlineRequired = newlineRequired;
    }

    public JSONFormat() {
        this(0, DEFAULT_INDENTATION, false);
    }

    public int getCurrentIndentation() {
        return currentIndentation;
    }

    public int getIndentationIncrement() {
        return indentationIncrement;
    }

    public static JSONFormat create() {
        return instance;
    }

    public boolean isNewlineRequired() {
        return newlineRequired;
    }

    public JSONFormat newLineAfter() {
        return new JSONFormat(currentIndentation, indentationIncrement, true);
    }

    public JSONFormat noNewLineAfter() {
        return new JSONFormat(currentIndentation, indentationIncrement, false);
    }

    public JSONFormat indent() {
        return new JSONFormat(currentIndentation + indentationIncrement, indentationIncrement,
                newlineRequired);
    }

    public String format(JSONValue value) {
        StringBuilder sb = new StringBuilder();
        try {
            appendTo(sb, value);
        }
        catch (IOException e) {
            // can't happen
        }
        return sb.toString();
    }

    public void appendTo(Appendable a, JSONValue value) throws IOException {
        appendTo(a, value, currentIndentation, currentIndentation);
        if (newlineRequired)
            a.append(LINE_TERMINATOR);
    }

    private void appendTo(Appendable a, JSONValue value, int indent, int initialIndex)
            throws IOException {
        for (int i = initialIndex; i > 0; --i)
            a.append(' ');
        if (value == null)
            a.append("null");
        else if (value instanceof JSONObject) {
            JSONObject object = (JSONObject)value;
            a.append('{');
            if (object.size() > 0) {
                indent += indentationIncrement;
                a.append(LINE_TERMINATOR);
                int i = 0;
                for (;;) {
                    JSONObject.Entry entry = object.getEntry(i++);
                    for (int j = indent; j > 0; --j)
                        a.append(' ');
                    a.append('"');
                    Strings.appendEscaped(a, entry.getKey(), JSON.charMapper);
                    a.append('"').append(':');
                    appendTo(a, entry.getValue(), indent, 1);
                    if (i >= object.size())
                        break;
                    a.append(',');
                    a.append(LINE_TERMINATOR);
                }
                a.append(LINE_TERMINATOR);
                indent -= indentationIncrement;
                for (int j = indent; j > 0; --j)
                    a.append(' ');
            }
            a.append('}');
        }
        else if (value instanceof JSONArray) {
            JSONArray array = (JSONArray)value;
            a.append('[');
            if (array.size() > 0) {
                indent += indentationIncrement;
                a.append(LINE_TERMINATOR);
                int i = 0;
                for (;;) {
                    appendTo(a, array.get(i++), indent, indent);
                    if (i >= array.size())
                        break;
                    a.append(',');
                    a.append(LINE_TERMINATOR);
                }
                a.append(LINE_TERMINATOR);
                indent -= indentationIncrement;
                for (int j = indent; j > 0; --j)
                    a.append(' ');
            }
            a.append(']');
        }
        else
            value.appendJSON(a);
    }

}

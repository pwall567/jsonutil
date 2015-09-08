/*
 * @(#) CheckFormat.java
 */

package net.pwall.json;

import java.io.IOException;

/**
 * Check formatting.
 */
public class CheckFormat {

    public static void main(String[] args) {
        try {
            JSONObject object = new JSONObject();
            object.putValue("first", 1);
            object.putValue("second", "abc");
            JSONArray array = new JSONArray();
            array.addValue(0);
            array.addValue(false);
            array.addNull();
            object.put("third", array);
            object.put("fourth", new JSONObject());
            System.out.println(object.toJSON());
            JSONFormat.create().newLineAfter().appendTo(System.out, object);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}

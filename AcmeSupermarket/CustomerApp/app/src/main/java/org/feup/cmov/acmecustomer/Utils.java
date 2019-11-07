package org.feup.cmov.acmecustomer;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Utils {

    public static String encode(byte[] byteArray) {
        try {
            return new String(byteArray, "UTF8"); // Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decode(String string) {
        try {
            return string.getBytes("UTF8"); // Base64.decode(string, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

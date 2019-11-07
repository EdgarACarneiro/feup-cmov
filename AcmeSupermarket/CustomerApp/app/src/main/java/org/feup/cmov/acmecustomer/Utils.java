package org.feup.cmov.acmecustomer;

import android.util.Base64;

public class Utils {

    public static String encode(byte[] byteArray) {
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static byte[] decode(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }
}

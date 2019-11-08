package org.feup.cmov.acmecustomer;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class Utils {

    public static String encode(byte[] byteArray) {
        try {
            return new String(byteArray, "ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decode(String string) {
        try {
            return string.getBytes("ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] concaByteArrays(byte[] a, byte[] b) {
        byte[] res = new byte[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);

        return res;
    }
}

package org.feup.cmov.acmecustomer;

import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

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

    public static byte[] toBase64(byte[] array) {
        return Base64.encode(array, Base64.DEFAULT);
    }

    public static byte[] fromBase64(byte[] array) {
        return Base64.decode(array, Base64.DEFAULT);
    }

    public static byte[] concaByteArrays(byte[] a, byte[] b) {
        byte[] res = new byte[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);

        return res;
    }

    public static void createSnackbar(View root, String snackTitle) {
        Snackbar snackbar = Snackbar.make(root, snackTitle, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
    }
}

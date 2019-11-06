package org.feup.cmov.acmecustomer.services;

import android.content.Context;
import android.content.SharedPreferences;

import org.feup.cmov.acmecustomer.R;

public class LocalStorage {

    private static SharedPreferences getLocalStorage(Context context) {
        return context.getSharedPreferences(
                String.valueOf(R.string.app_name_key), Context.MODE_PRIVATE
        );
    }

    public static void read(Context context, String key, String value) {
        SharedPreferences storage = LocalStorage.getLocalStorage(context);
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String write(Context context, String key) {
        return LocalStorage.getLocalStorage(context).getString(key, null);
    }
}

package org.feup.cmov.acmecustomer.services;

import android.content.Context;
import android.content.SharedPreferences;

import org.feup.cmov.acmecustomer.Constants;
import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.Utils;

public class LocalStorage {

    private static SharedPreferences getLocalStorage(Context context) {
        return context.getSharedPreferences(
                String.valueOf(R.string.app_name_key), Context.MODE_PRIVATE
        );
    }

    public static void write(Context context, String key, String value) {
        SharedPreferences storage = LocalStorage.getLocalStorage(context);
        SharedPreferences.Editor editor = storage.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String read(Context context, String key) {
        return LocalStorage.getLocalStorage(context).getString(key, null);
    }


    public static void setAcmePublicKey(Context context, String key) {
        LocalStorage.write(context, String.valueOf(Constants.ACME_KEY), key);
    }

    public static byte[] getAcmePublicKey(Context context) {
        return Utils.fromBase64(
                Utils.decode(LocalStorage.read(context, Constants.ACME_KEY))
        );
    }

    public static void setCurrentUuid(Context context, String uuid) {
        LocalStorage.write(context, String.valueOf(R.string.current_uuid), uuid);
    }

    public static String getCurrentUuid(Context context) {
        return LocalStorage.read(context, String.valueOf(R.string.current_uuid));
    }

}

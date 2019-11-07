package org.feup.cmov.acmecustomer.services;

import org.feup.cmov.acmecustomer.Constants;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyStoreHandler {

    private static KeyStore.Entry getKeyStoreEntry(String keyName) {
        KeyStore.Entry entry = null;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            entry = ks.getEntry(keyName, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return entry;
        }
    }

    public static PrivateKey getUserPrivateKey(String username) {
        KeyStore.Entry entry = getKeyStoreEntry(username);
        return entry != null? ((KeyStore.PrivateKeyEntry)entry).getPrivateKey() : null;
    }

    public static PublicKey getUserPublicKey(String username) {
        KeyStore.Entry entry = getKeyStoreEntry(username);
        return entry != null? ((KeyStore.PrivateKeyEntry)entry).getCertificate().getPublicKey() : null;
    }
}

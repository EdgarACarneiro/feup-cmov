package org.feup.cmov.acmecustomer.services;

import org.feup.cmov.acmecustomer.Constants;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class KeyStoreHandler {

    // TODO- Check if not needed, if so clean
    private static Key getKeyStoreEntry(String keyName) {
        Key entry = null;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            entry = ks.getKey(keyName, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return entry;
        }
    }

    public static PrivateKey getUserPrivateKey(String username) {
        PrivateKey entry = null;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            entry = (PrivateKey) ks.getKey(username, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return entry;
        }
    }

    public static PublicKey getUserPublicKey(String username) {
        PublicKey entry = null;
        try {
            KeyStore ks = KeyStore.getInstance(Constants.ANDROID_KEYSTORE);
            ks.load(null);
            entry = ks.getCertificate(username).getPublicKey();
        }
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            return entry;
        }
    }

    public static PublicKey getKeyFromBytes(byte[] publicKeyBytes) {
        PublicKey key = null;
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return key;
        }
    }
}

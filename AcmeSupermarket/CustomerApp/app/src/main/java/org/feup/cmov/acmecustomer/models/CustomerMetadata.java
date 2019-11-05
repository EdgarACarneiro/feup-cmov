package org.feup.cmov.acmecustomer.models;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

public class CustomerMetadata implements Serializable {
    private Context context;
    @Expose
    private String name;
    @Expose
    private String username;
    @Expose
    private String password;
    private KeyPair keyPair;
    @Expose
    private byte[] publicKey;

    protected CustomerMetadata(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        // Needed for json serialization
        //this.publicKey = this.keyPair.getPublic().getEncoded();
    }

    public void generateKeyPair(Context context) {
        final String ANDROID_KEYSTORE = "AndroidKeyStore";
        final int KEY_SIZE = 512;
        final String KEY_ALGO = "RSA";
        final int CERT_SERIAL = 12121212;
        String keyName = "AcmeKey";

        try {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.YEAR, 20);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGO, ANDROID_KEYSTORE);

            AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setKeySize(KEY_SIZE)
                    .setAlias(keyName)
                    .setSubject(new X500Principal("CN=" + keyName))
                    .setSerialNumber(BigInteger.valueOf(CERT_SERIAL))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            keyPairGenerator.initialize(spec);
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.publicKey = this.keyPair.getPublic().getEncoded();
    }

    protected String getName() {
        return this.name;
    }

    protected String getUsername() {
        return this.username;
    }

    protected String getPassword() {
        return this.password;
    }

    public KeyPair getKeyPair() {
        return this.keyPair;
    }
}

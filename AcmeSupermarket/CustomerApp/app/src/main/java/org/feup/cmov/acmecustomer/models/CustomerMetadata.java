package org.feup.cmov.acmecustomer.models;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import com.google.gson.annotations.Expose;

import org.feup.cmov.acmecustomer.Constants;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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
    }

    public void generateKeyPair(Context context) {
        // Generating KeyPair
        try {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.YEAR, 20);
            // This will also store in KeyStore
            KeyPairGenerator keyPairGenerator =
                    KeyPairGenerator.getInstance(Constants.KEY_ALGO, Constants.ANDROID_KEYSTORE);

            AlgorithmParameterSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setKeySize(Constants.KEY_SIZE)
                    .setAlias(this.username)
                    .setSubject(new X500Principal("CN=" + this.username))
                    .setSerialNumber(BigInteger.valueOf(Constants.CERT_SERIAL))
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            keyPairGenerator.initialize(spec);
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Needed for json serialization on Registration
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

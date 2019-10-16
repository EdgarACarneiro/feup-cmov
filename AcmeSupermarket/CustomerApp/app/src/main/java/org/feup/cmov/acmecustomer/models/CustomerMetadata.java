package org.feup.cmov.acmecustomer.models;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class CustomerMetadata {
    private String name;
    private String username;
    private String password;
    private KeyPair keyPair;

    protected CustomerMetadata(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            this.keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
}

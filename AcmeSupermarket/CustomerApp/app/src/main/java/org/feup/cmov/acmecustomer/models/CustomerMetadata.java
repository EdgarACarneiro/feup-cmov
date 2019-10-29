package org.feup.cmov.acmecustomer.models;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class CustomerMetadata {
    private UUID UUID;
    private String name;
    private String username;
    private String password;
    private KeyPair keyPair;

    protected CustomerMetadata(String name, String username, String password) {
        this.UUID = UUID.randomUUID();
        this.name = name;
        this.username = username;
        this.password = password;
        this.generateKeyPair();
    }

    protected CustomerMetadata(String uuid, String name, String username, String password) {
        this.UUID = UUID.fromString(uuid);
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

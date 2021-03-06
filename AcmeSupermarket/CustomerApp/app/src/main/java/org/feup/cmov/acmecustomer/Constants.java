package org.feup.cmov.acmecustomer;

public class Constants {
    public static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    public static final int KEY_SIZE = 512;
    public static final String KEY_ALGO = "RSA";
    public static final String DECRYPT_ALGO = "RSA/NONE/PKCS1Padding";
    public static final int CERT_SERIAL = 12121212;
    public static final String ENC_ALGO = "RSA/NONE/PKCS8";
    public static final String ACME_KEY= "ACME_SERVER_KEY";
    public static final int ACME_TAG_ID = 0x41636D65; // equal to "Acme"
    public static final String SERVER_ENDPOINT = "0ae2f0d9.ngrok.io";
}

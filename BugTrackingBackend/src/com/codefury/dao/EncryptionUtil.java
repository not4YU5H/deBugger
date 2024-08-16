package com.codefury.dao;
import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.util.Base64;

//This is responsible for all the Encryption that happens

public class EncryptionUtil {

    private static final String ALGORITHM = "RSA";
    private static final String KEYSTORE_TYPE = "JKS";
    private static final String KEYSTORE_PATH = "keystore.jks";
    private static final String KEYSTORE_PASSWORD = "changeit";
    private static final String KEY_ALIAS = "mykey";
    private static final String KEY_PASSWORD = "changeit";

    //Loading the keystore
    private KeyStore getKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }
        return keyStore;
    }

    //Fetching Private key
    private PrivateKey getPrivateKey() throws Exception {
        KeyStore keyStore = getKeyStore();
        Key key = keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        if (key instanceof PrivateKey) {
            return (PrivateKey) key;
        } else {
            throw new RuntimeException("Key is not a PrivateKey.");
        }
    }

    //Fetching Public key
    private PublicKey getPublicKey() throws Exception {
        KeyStore keyStore = getKeyStore();
        Key key = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        if (key instanceof PublicKey) {
            return (PublicKey) key;
        } else{
            throw new RuntimeException("Key is not a PublicKey.");
        }
    }

    //Public function to encrypt to be called before data entry into database
    public String encrypt(String data) throws Exception {
        PublicKey publicKey = getPublicKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    //Function to decrypt to be called when data is read
    public String decrypt(String encryptedData) throws Exception {
        PrivateKey privateKey = getPrivateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
}

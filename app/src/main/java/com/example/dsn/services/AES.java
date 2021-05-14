package com.example.dsn.services;

import java.security.MessageDigest;
import java.security.SecureRandom;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * This class enables to carry out the service of AES SHA-256 encryption and decryption.
 * @author Mohammed Ehtesham Ahmed
 * @version 1.0.0
 */
public class AES {

    /**
     * Carries out the task of encrypting given any string.
     * IV is randomized to generate different outputs for the same input. This prevents giving away any information related to the original string.
     * @param data to be encrypted
     * @param key provided by user
     * @return base64 encoded encrypted string
     * @throws Exception e upon failing to carry out Cipher class built-in methods
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] clean = data.getBytes();

        // Generating IV
        int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Hashing key
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(key.getBytes("UTF-8"));
        byte[] keyBytes = new byte[16];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Encrypt
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        // Combine IV and encrypted part
        byte[] encryptedIVAndText = new byte[ivSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize);
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.length);

        return Base64.encodeToString(encryptedIVAndText, Base64.DEFAULT);
    }


    /**
     * Carries out the task of decrypting given any base64 encoded string.
     * @param encryptedData to be decrypted
     * @param key provided by user to decrypt data
     * @return original decrypted string
     * @throws Exception e upon failing to carry out Cipher class built-in methods
     */
    public static String decrypt(String encryptedData, String key) throws Exception {
        int ivSize = 16;
        int keySize = 16;

        byte[] encryptedIvTextBytes = Base64.decode(encryptedData, Base64.DEFAULT);

        // Extract IV
        byte[] iv = new byte[ivSize];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract encrypted part
        int encryptedSize = encryptedIvTextBytes.length - ivSize;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, ivSize, encryptedBytes, 0, encryptedSize);

        // Hash key
        byte[] keyBytes = new byte[keySize];
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(key.getBytes());
        System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        // Decrypt
        Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

        return new String(decrypted);
    }
}
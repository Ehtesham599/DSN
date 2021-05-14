package com.example.dsn.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppUtils {

    public static String encodeSHA256(String input){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            return bytesToHex(hash);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * This method returns the user AES key for encryption and decryption of data.
     * @param context to the activity
     * @return Peer key
     */
    public static String getAESKey(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("PEER_KEY","");
    }

    /**
     * A method that returns the peer ID
     * @param context to the activity
     * @return peer ID
     */
    public static String getPeerID(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("PEER_ID","");
    }
}

package com.example.dsn.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.dsn.R;
import com.example.dsn.utils.AppUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;


/**
 * Enables an intro for the user and help navigate to the appropriate screen
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        @SuppressLint("HardwareIds")
        String peerID = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        savePeerID(peerID);
        generateAndSaveKey(peerID);
        navigateToMainActivity();

    }

    /**
     * A method that generates and saves the key used for encryption/decryption of data
     * @param peerID to be used to hash and obtain key
     */
    private void generateAndSaveKey(String peerID){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PEER_KEY", AppUtils.encodeSHA256(peerID));
        editor.apply();
    }

    /**
     * A method that saves peer ID to local shared preferences
     * @param peerID peer Id
     */
    private void savePeerID(String peerID) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PEER_ID",peerID);
        editor.apply();
    }

//    private void authenticatePeer(String peerID){
//
//    }

    /**
     * A method that navigates to MainActivity with a delay of 4 seconds
     */
    private void navigateToMainActivity(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 4000);
    }

}
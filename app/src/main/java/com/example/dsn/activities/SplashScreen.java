package com.example.dsn.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.dsn.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


/**
 * Enables an intro for the user and help navigate to the appropriate screen
 * TODO : check user Auth status
 * TODO : initialize node
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        navigateToMainActivity();

    }

    private void navigateToMainActivity(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 4000);
    }


}
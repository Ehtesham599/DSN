package com.example.dsn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dsn.R;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 10;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bGetFile = findViewById(R.id.button_retrieve);
        Button bUploadFile = findViewById(R.id.button_upload);

        bUploadFile.setOnClickListener(
                view -> {
                    showFileChooser();
                }
        );
    }

    /**
     * This method allows user to pick a file from the device's external storage
     */
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * This method gets the file object from the file path obtained after user selects the file
     * and passes it to the data handling functionality.
     * @param requestCode rq
     * @param resultCode rc
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                String path = data.getData().getPath();
                Log.d(TAG, "File Path: " + path);
                File file = new File(path);
                Toast.makeText(this, (int) file.length(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
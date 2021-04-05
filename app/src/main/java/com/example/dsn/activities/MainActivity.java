package com.example.dsn.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dsn.R;
import com.example.dsn.services.DataHandler;
import com.example.dsn.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;

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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                String path = data.getData().getPath();
                Log.d(TAG, "File Path: " + path);
                File file = new File(path);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


//        List<ArrayList<Byte>> chunkList = chunks(fileToByteArray(file));
//
//        System.out.println(Base64.getEncoder().encodeToString(toByteArray(chunkList.get(2))));
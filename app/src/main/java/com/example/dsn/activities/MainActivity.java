package com.example.dsn.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dsn.R;
import com.example.dsn.services.DataHandler;
import com.example.dsn.utils.AppUtils;
import com.example.dsn.utils.UriUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
                Uri uri = data.getData();
                String path = UriUtils.getPathFromUri(this, uri);
                assert path != null;
                File file = new File(path);
                String peerID = AppUtils.getPeerID(this);
                String key = AppUtils.getAESKey(this);
                JSONObject result = DataHandler.constructLinkedDataObject(file, peerID, key);
                try {
                    System.out.println(result.toString(4));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
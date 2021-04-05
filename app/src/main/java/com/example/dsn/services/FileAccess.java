package com.example.dsn.services;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileAccess {

    private static final String TAG = "FileAccess";

    /**
     * This method loads the contents a file from app-specific storage
     * @param file to look for
     * @param context with regard to the activity
     * @return data - contents of the file
     */
    public static String loadJsonFromStorage(String file, Context context){
        String data = null;
        if(!file.isEmpty()) {
            try {
                InputStream inputStream = context.openFileInput(file);
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    data = stringBuilder.toString();
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found!");
            } catch (IOException e) {
                Log.e(TAG, "Cannot read file contents!");
            }
        }
        return data;
    }

    /**
     * This method gets current external storage state of the device.
     * @return true if accessible
     */
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }
}

package com.example.dsn.services;

import android.util.Base64;

import com.example.dsn.utils.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.dsn.constants.AppConstants.*;

/**
 * This class enables the operations to be performed for any given data.
 * @author Mohammed Ehtesham Ahmed
 * @version 1.0.0
 */

public class DataHandler {

    /**
     * This method creates the linked data object for a given file
     * Converts file to byte array
     * Gets List of lists of type Byte - chunk list - each list being of a fixed size
     * Encrypts these chunks of data
     * @param file file to be pushed
     */
    public static JSONObject constructLinkedDataObject(File file, String peerID, String key){
        List<ArrayList<Byte>> chunkList = getChunks(fileToByteArray(file));

        List<String> encryptedChunks = encryptedDataList(chunkList, key);

        JSONArray chunks = new JSONArray();

        for(int i=0;i<encryptedChunks.size();i++){
            String id = AppUtils.encodeSHA256(encryptedChunks.get(i));
            int size = encryptedChunks.get(i).length();
            String previous = "";
            if(i>0) previous = AppUtils.encodeSHA256(encryptedChunks.get(i-1));
            String next = "";
            if(i< encryptedChunks.size()-1) next = AppUtils.encodeSHA256(encryptedChunks.get(i+1));
            String data = encryptedChunks.get(i);

            JSONObject chunk = new JSONObject();
            try{
                chunk.put("id", id);
                chunk.put("size", size);
                chunk.put("previous", previous);
                chunk.put("next", next);
                chunk.put("data", data);

                chunks.put(chunk);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String cid = getCID(file);
        String filename = file.getName();
        long size = file.length();
        String extension = null;
        int i = filename.lastIndexOf(".");
        if(i>0) extension = filename.substring(i+1);

        JSONObject result = new JSONObject();

        try{
            result.put("file_name", filename);
            result.put("CID", cid);
            result.put("size", size);
            result.put("extension", extension);
            result.put("source", peerID);
            result.put("chunks", chunks);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This method encrypts the given chunks of data following the AES encryption provided by a key
     * @param chunkList data to be encrypted
     * @param key to be used in encryption
     * @return list of encrypted strings
     */
    public static List<String> encryptedDataList(List<ArrayList<Byte>> chunkList, String key){
        ArrayList<String> result = new ArrayList<>();
        try {
            for (ArrayList<Byte> arr : chunkList) {
                byte[] temp = listToByteArray(arr);
                String encodedData = Base64.encodeToString(temp, Base64.DEFAULT);
                result.add(AES.encrypt(encodedData, key));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * This method returns the content ID generated for a given file
     * @param file for which CID is to be generated - could be of any format
     * @return CID
     */
    public static String getCID(File file){
        List<ArrayList<Byte>> chunkList = getChunks(fileToByteArray(file));
        ArrayList<String> stringArrayChunk = new ArrayList<>();
        for(ArrayList<Byte> byteArray : chunkList){
            byte[] res = listToByteArray(byteArray);
            stringArrayChunk.add(AppUtils.encodeSHA256(Base64.encodeToString(res, Base64.DEFAULT)));
        }
        StringBuilder str = new StringBuilder();
        for(String s : stringArrayChunk){
            str.append(s);
        }

        return AppUtils.encodeSHA256(str.toString());
    }

    /**
     * This method converts List of type Byte to array of byte.
     * @param in input list of type Byte
     * @return array of type byte
     */
    public static byte[] listToByteArray(List<Byte> in) {
        final int n = in.size();
        byte[] ret = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }

    /**
     * This method takes a byte array and divides it into multiple Byte array list of lists of a fixed chunk size
     * @param data byte array to be divided
     * @return list of lists of type Byte
     */
    public static List<ArrayList<Byte>> getChunks(byte[] data){
        List<Byte> byteArrayList = new ArrayList<>();

        for(byte b : data){
            byteArrayList.add(b);
        }
        List<ArrayList<Byte>> chunksList = new ArrayList<>();
        int limit = data.length-1;
        for(int i=0;i<limit;i+=CHUNK_SIZE){
            chunksList.add(new ArrayList<>(byteArrayList.subList(i, Math.min(limit, i + CHUNK_SIZE))));
        }

        return chunksList;
    }

    /**
     * @param file to be converted to byte array
     * @return byte array of the given file
     */
    public static byte[] fileToByteArray(File file){
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }


}

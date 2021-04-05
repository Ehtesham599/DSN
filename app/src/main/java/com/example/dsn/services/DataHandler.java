package com.example.dsn.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.dsn.constants.AppConstants.CHUNK_SIZE;

/**
 * This class enables the operations to be performed for any given data.
 * @author Mohammed Ehtesham Ahmed
 * @version 1.0.0
 */

public class DataHandler {

    /**
     * This method converts List of type Byte to array of byte.
     * @param in input list of type Byte
     * @return array of type byte
     */
    public static byte[] toByteArray(List<Byte> in) {
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
    public static List<ArrayList<Byte>> getChunks (byte[] data){
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
    public byte[] fileToByteArray(File file){
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

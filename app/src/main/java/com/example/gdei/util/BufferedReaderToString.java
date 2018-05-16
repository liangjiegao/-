package com.example.gdei.util;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by gdei on 2018/5/15.
 */

public class BufferedReaderToString {

    public static String brToString(BufferedReader br){
        String str = "";
        StringBuffer result = new StringBuffer();
        try {
            while ((str = br.readLine()) != null) {
                result.append(str);
            }
            return result.toString();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}

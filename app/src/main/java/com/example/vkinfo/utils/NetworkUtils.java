package com.example.vkinfo.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {
    private static final String VK_API_BASE_URL = "https://api.vk.com";
    private static final String VK_USERS_GET = "/method/users.get";
    private static final String PARAM_USER_ID = "user_ids";
    private static final String PARAM_VERSION= "v";
    private static final String PARAM_TOKEN= "access_token";
    public static URL generateURL(String userId){
        Uri buildUri = Uri.parse(VK_API_BASE_URL+VK_USERS_GET)
                .buildUpon()
                .appendQueryParameter(PARAM_USER_ID, userId)
                .appendQueryParameter(PARAM_VERSION, "5.21")
                .appendQueryParameter(PARAM_TOKEN, "8bdc1cdb8bdc1cdb8bdc1cdbf48ba412f488bdc8bdc1cdbeb689e3e6df7b004e0b8b490")
                .build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromURL (URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A ");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return  scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }


    }
}

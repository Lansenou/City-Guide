package com.hva.group8.cityguide.Loaders;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Lansenou on 19-5-2015.
 */
public class DBConnect {
    public static void Insert(ArrayList<NameValuePair> values, String URL) {
        // set up threadpolicy
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            httppost.setEntity(new UrlEncodedFormEntity(values));
            httpclient.execute(httppost);
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
    }

    public static String Read(ArrayList<NameValuePair> values, String URL) {
        String result = "";
        InputStream is = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // http post
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(URL);
            if (values != null)
                httppost.setEntity(new UrlEncodedFormEntity(values));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception f) {
            Log.e("log_tag", "Error converting result " + f.toString());
        }
        return result;
    }
}

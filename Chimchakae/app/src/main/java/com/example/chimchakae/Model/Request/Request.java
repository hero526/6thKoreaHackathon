package com.example.chimchakae.Model.Request;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Request extends AsyncTask<String, Void, String> {

    String serviceKey = "cAjUOsFMleHI7z7rim0ez2EYUmE2R8hu%2FLpIvOfzDZYMMPLx6RvpV1ck2wq7xuqauoUL7zKoxCKxd2cchZeVYQ%3D%3D";
    private String str, receiveMsg;


    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("http://apis.data.go.kr/6260000/BusanWaterImrsnInfoService/getWaterImrsnInfo?serviceKey="+serviceKey);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", serviceKey);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}

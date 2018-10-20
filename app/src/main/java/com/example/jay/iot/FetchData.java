package com.example.jay.iot;

import android.os.AsyncTask;
import android.text.Editable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.text.TextWatcher;

class FetchData extends AsyncTask<Void,Void,Void> {
    String dataTemp = "";
    String dataHum = "";
    String dataPh = "";
    String temp = "";
    String hum = "";
    String ph = "";



    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.netpie.io/topic/Aeroponics/temp?retain&auth=KhxS40H8waRbigs:Wx7TjSQiLjhka7jDn4fjsLVk9");
            URL url2 = new URL("https://api.netpie.io/topic/Aeroponics/hum?retain&auth=KhxS40H8waRbigs:Wx7TjSQiLjhka7jDn4fjsLVk9");
            URL url3 = new URL("https://api.netpie.io/topic/Aeroponics/ph?retain&auth=KhxS40H8waRbigs:Wx7TjSQiLjhka7jDn4fjsLVk9");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
            HttpURLConnection httpURLConnection3 = (HttpURLConnection) url3.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStream inputStream2 = httpURLConnection2.getInputStream();
            InputStream inputStream3 = httpURLConnection3.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));
            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(inputStream3));

            String line = "";
            String line2 = "";
            String line3 = "";


            while (line != null) {
                line = bufferedReader.readLine();
                dataTemp = dataTemp + line;

            }

            while (line2 != null) {
                line2 = bufferedReader2.readLine();
                dataHum = dataHum + line2;
            }

            while (line3 != null) {
                line3 = bufferedReader3.readLine();
                dataPh = dataPh + line3;
            }


            JSONArray JA = new JSONArray(dataTemp);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                temp = "" + JO.get("payload");
            }

            JSONArray JA2 = new JSONArray(dataHum);
            for (int i = 0; i < JA2.length(); i++) {
                JSONObject JO2 = (JSONObject) JA2.get(i);
                hum = "" + JO2.get("payload");
            }

            JSONArray JA3 = new JSONArray(dataPh);
            for (int i = 0; i < JA2.length(); i++) {
                JSONObject JO3 = (JSONObject) JA3.get(i);
                ph = "" + JO3.get("payload");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivity.temp_fetch.setText(this.temp);
        MainActivity.hum_fetch.setText(this.hum);
        MainActivity.ph_fetch.setText(this.ph);


    }
}

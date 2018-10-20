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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import android.text.TextWatcher;

class SentOnLight extends AsyncTask<Void,Void,Void> {


    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.netpie.io/microgear/Aeroponics/AeroGear?retain&auth=dSQZ3k4PIcxpvOj:riDNB22nk4TSUTU71zYzK6fqw");
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write("OnLight");
            out.close();
            httpCon.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);


    }
}

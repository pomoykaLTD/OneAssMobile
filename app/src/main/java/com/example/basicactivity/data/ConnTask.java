package com.example.basicactivity.data;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class ConnTask{
    private Activity activity;
    private String sendDataJSON="";
    private String url;

    public ConnTask(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
    }
    public ConnTask(Activity activity,  String url, String sendDataJSON) {
        this.activity = activity;
        this.url = url;
        this.sendDataJSON = sendDataJSON;
    }

    private void startBackground() {
        new Thread(new Runnable() {
            public void run() {
                doInBackground();
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        onPostExecute();
                    }
                });
            }
        }).start();
    }
    public void execute(){
        startBackground();
    }

    public void doInBackground(){

        ServConnector servConn=((ServConnector)activity);
        servConn.setResult("");
        servConn.setStatus(false);

        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(sendDataJSON.length()));
            conn.setRequestProperty ("Authorization", ((ServConnector) activity).getLogIn());
            conn.setUseCaches(false);

            //отправка данных на сервер
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(sendDataJSON);
            }

            servConn.setStatus( (conn.getResponseCode()==200)?true:false );

            if(servConn.getStatus()){
                //чтение ответа сервера
                String result = "";
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))){
                    String line = "";
                    while ((line = br.readLine()) != null) {
                        result+=line;
                    }
                }
                servConn.setResult(result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

  /*      try {

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) (new URL(url)).openConnection();
                urlConnection.setRequestProperty ("Authorization", ((ServConnector) activity).getLogIn());
                servConn.setStatus( (urlConnection.getResponseCode()==200)?true:false );

                if(servConn.getStatus()){
                    String result = "";
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
                    }
                    servConn.setResult(result);
                }

            } catch (Exception e) {
                e.printStackTrace();
                servConn.setResult(e.getMessage().toString());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            servConn.setResult("Exception: " + e.getMessage());
        }*/
    };
    public abstract void onPostExecute();

}

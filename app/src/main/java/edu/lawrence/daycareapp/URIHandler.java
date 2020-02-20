package edu.lawrence.daycareapp;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URIHandler {
    public static final String HOST_NAME = "143.44.68.130:8085";
    private static final int SUCCESS = 200;
    public static String doGet(String URI){
        try{
            URL url = new URL(URI);
            Log.d("doGetURI", URI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d("Response Code", String.valueOf(responseCode));
            Log.d("Connection", responseCode == 200 ? "Success!" : "Failure!");
            if(responseCode != SUCCESS) return null;
            byte[] response = toByteArray(connection.getInputStream(), 3000);
            String result = new String(response, "UTF8");
            Log.d("Network", result);
            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return null;
    }
    public static String doPost(String URI, String data){
        try {
            URL url = new URL(URI);
            Log.d("doPostURI", URI);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            Log.d("Data", data);
            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d("Response Code", String.valueOf(responseCode));
            Log.d("Connection", responseCode == 200 ? "Success!" : "Failure!");
            if (responseCode != SUCCESS) return "";


            byte[] response = toByteArray(connection.getInputStream(), 3000);
            String result = new String(response, "UTF8");
            Log.d("Network", result);

            return result;

        }catch (MalformedURLException e) {
            Log.e("MalformedURLException", "doPost failed");
        }catch (IOException IOE){
            Log.e("IOException", "doPost failed");
        }
        return "";
    }

    public static String doPut(String URI, String data){
        try {
            URL url = new URL(URI);
            Log.d("doPutURI", URI);
            Log.d("Data", data);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes());
            os.flush();

            connection.connect();
            int responseCode = connection.getResponseCode();
            Log.d("Response Code", String.valueOf(responseCode));
            Log.d("Connection", responseCode == 200 ? "Success!" : "Failure!");
            if (responseCode != SUCCESS) return "";

            byte[] response = toByteArray(connection.getInputStream(), 3000);
            String result = new String(response, "UTF8");
            return result;
        }catch (MalformedURLException e) {
            Log.e("MalformedURLException", "doPost failed");
        }catch (IOException IOE){
            Log.e("IOException", "doPost failed");
        }
        return "";

    }
    private static byte[] toByteArray(InputStream in, int length){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(length);
        byte[] buffer = new byte[length];
        int bytesRead = 0;
        try {
            while ((bytesRead = in.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }catch (IOException IOE){
            Log.println(Log.DEBUG, "Error", IOE.getMessage());
        }
        finally {
            try {
                if(in != null)
                in.close();
            }catch (IOException IOE){
                Log.e("Error", IOE.getMessage() );
            }
        }
        return null;
    }
}

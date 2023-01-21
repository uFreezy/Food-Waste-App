package com.f83260.foodwaste.common;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Request implements Runnable {
    private final String url;
    private final Map<String, String> headers;
    private final String payload;
    private final String requestMethod;
    private JSONObject value;
    private Integer statusCode;

    public Request(String url, String requestMethod, Map<String, String> headers, String data) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.headers = headers;
        this.payload = data;
    }

    @Override
    public void run() {
        try {
            URL urlObj = new URL(this.url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            con.setRequestMethod(requestMethod);
            con.setRequestProperty("Accept", "application/json");

            for (Map.Entry<String, String> entry: this.headers.entrySet()){
                con.setRequestProperty(entry.getKey(), this.headers.get(entry.getKey()));
            }

            if (payload != null) {
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = this.payload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }


            con.connect();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();


            this.value = new JSONObject(content.toString());
            this.statusCode = con.getResponseCode();


        } catch (Exception ex) {
            ex.printStackTrace();
            this.value = new JSONObject();
        }


    }

    public JSONObject getValue() {
        return this.value;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }
}

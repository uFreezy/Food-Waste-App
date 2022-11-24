package com.f83260.foodwaste.common;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestExecutor implements Runnable {
    private String url;
    private String requestMethod;
    private Map<String, String> headers;
    private JSONObject value;
    private String payload;
    private Integer statusCode;

    public RequestExecutor(String url, String requestMethod, Map<String, String> headers, String data) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.headers = headers;
        this.payload = data;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestMethod);
            con.setRequestProperty("Accept", "application/json");


            for (String key : this.headers.keySet()) {
                con.setRequestProperty(key, this.headers.get(key));
            }


            if (payload != null) {
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = this.payload.toString().getBytes(StandardCharsets.UTF_8);
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
            // TODO: log failure

            Exception ex2 = ex;
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

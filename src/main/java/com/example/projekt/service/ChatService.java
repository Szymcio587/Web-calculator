package com.example.projekt.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    public String getChatGptResponse(String prompt) {
        OkHttpClient client = new OkHttpClient();
        JSONObject requestBody = new JSONObject()
                .put("model", "gpt-4o-mini")
                .put("messages", new JSONArray().put(new JSONObject().put("role", "user").put("content", prompt)));

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBody.toString(), MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject responseObject = new JSONObject(response.body().string());
                return responseObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            } else {
                throw new RuntimeException("API Request Failed: " + response.body().string());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with ChatGPT API", e);
        }
    }
}

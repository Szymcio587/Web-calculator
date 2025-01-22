package com.example.projekt.service;

import com.example.projekt.model.data.InterpolationData;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ChatService {

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    public String GeneratePolynomialInterpolationResponse(InterpolationData interpolationData, double result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces obliczania interpolacji wielomianowej, mając podane następujące dane wejściowe: "
                + interpolationData + " oraz zakładając, że obliczony rezultat wynosi: " + result + ". W odpowiedzi nie opisuj samej metody interpolacji, tylko krok po kroku" +
                "sposób uzyskania wyznaczonego wyniku. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GetChatGptResponse(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

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
                return responseObject.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            } else {
                String errorBody = response.body() != null ? response.body().string() : "No error body";
                throw new RuntimeException("API Request Failed. Status: " + response.code() + ", Body: " + errorBody);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with ChatGPT API", e);
        }
    }
}

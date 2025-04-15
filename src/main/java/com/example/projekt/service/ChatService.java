package com.example.projekt.service;

import com.example.projekt.model.data.IntegrationData;
import com.example.projekt.model.data.InterpolationData;
import com.example.projekt.model.data.SystemOfEquationsData;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public String GenerateTrigonometricInterpolationResponse(InterpolationData interpolationData, double result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces obliczania interpolacji trygonometrycznej, mając podane następujące dane wejściowe: "
                + interpolationData + " oraz zakładając, że obliczony rezultat wynosi: " + result + ". W odpowiedzi nie opisuj samej metody interpolacji, tylko krok po kroku" +
                "sposób uzyskania wyznaczonego wyniku. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateMidpointIntegrationResponse(IntegrationData integrationData, double result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces obliczania całkowania numerycznego metodą punktu środkowego, mając podane następujące dane wejściowe: "
                + integrationData + " oraz zakładając, że obliczony rezultat wynosi: " + result + ". W odpowiedzi nie opisuj samej metody całkowania, tylko krok po kroku" +
                "sposób uzyskania wyznaczonego wyniku. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateSimpsonsIntegrationResponse(IntegrationData integrationData, double result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces obliczania całkowania numerycznego metodą Simpsona, mając podane następujące dane wejściowe: "
                + integrationData + " oraz zakładając, że obliczony rezultat wynosi: " + result + ". W odpowiedzi nie opisuj samej metody całkowania, tylko krok po kroku" +
                "sposób uzyskania wyznaczonego wyniku. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateTrapezoidalIntegrationResponse(IntegrationData integrationData, double result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces obliczania całkowania numerycznego metodą trapezów, mając podane następujące dane wejściowe: "
                + integrationData + " oraz zakładając, że obliczony rezultat wynosi: " + result + ". W odpowiedzi nie opisuj samej metody całkowania, tylko krok po kroku" +
                "sposób uzyskania wyznaczonego wyniku. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateGaussKronrodIntegrationResponse(IntegrationData integrationData, double result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces obliczania całkowania numerycznego metodą Gaussa-Kronroda, mając podane następujące dane wejściowe: "
                + integrationData + " oraz zakładając, że obliczony rezultat wynosi: " + result + ". W odpowiedzi nie opisuj samej metody całkowania, tylko krok po kroku" +
                "sposób uzyskania wyznaczonego wyniku. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateCramerSystemOfEquationsResponse(SystemOfEquationsData systemOfEquationsData, List<Double> result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces rozwiązywania układu równań wzorami Kramera, mając podane następujące dane wejściowe: "
                + systemOfEquationsData + " oraz zakładając, że wyznaczone niewiadome wynoszą: " + result + ". W odpowiedzi nie opisuj samych wzorów Kramera, tylko krok po kroku" +
                "sposób wyznaczenia podanych zmiennych. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateMultigridSystemOfEquationsResponse(SystemOfEquationsData systemOfEquationsData, List<Double> result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces rozwiązywania układu równań metodą wielosiatkową, mając podane następujące dane wejściowe: "
                + systemOfEquationsData + " oraz zakładając, że wyznaczone niewiadome wynoszą: " + result + ". W odpowiedzi nie opisuj samej metody wielosiatkowej, tylko krok po kroku" +
                "sposób wyznaczenia podanych zmiennych. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GenerateLUSystemOfEquationsResponse(SystemOfEquationsData systemOfEquationsData, List<Double> result) {
        String prompt = "Wytłumacz w szczegółowy sposób proces rozwiązywania układu równań metodą faktoryzacji LU, mając podane następujące dane wejściowe: "
                + systemOfEquationsData + " oraz zakładając, że wyznaczone niewiadome wynoszą: " + result + ". W odpowiedzi nie opisuj samej metody faktoryzacji LU, tylko krok po kroku" +
                "sposób wyznaczenia podanych zmiennych. Sformatuj tekst tak, aby był przystępny do przeczytania, a w szczególności pisz poszczególne podpunkty w jednej linii" +
                " bez zbędnych wcięć w tekście";
        return GetChatGptResponse(prompt);
    }

    public String GetChatGptResponse(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
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

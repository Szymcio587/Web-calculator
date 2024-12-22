package com.example.projekt.model.data;


public class ChatRequest {
    private String prompt;

    public ChatRequest() {}

    public ChatRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}

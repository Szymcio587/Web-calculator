package com.example.projekt.controllers;

import com.example.projekt.model.data.ChatRequest;
import com.example.projekt.model.results.ChatResponse;
import com.example.projekt.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatgpt")
public class ChatController {

    @Autowired
    private ChatService chatGptService;

    @PostMapping("/prompt")
    public ChatResponse sendPrompt(@RequestBody ChatRequest request) {
        String response = chatGptService.GetChatGptResponse(request.getPrompt());
        return new ChatResponse(response);
    }
}

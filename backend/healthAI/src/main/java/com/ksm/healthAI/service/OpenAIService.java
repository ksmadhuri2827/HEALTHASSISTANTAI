package com.ksm.healthAI.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    private OpenAiChatModel chatModel;

    public OpenAIService(OpenAiChatModel chatModel)
    {
        this.chatModel=chatModel;
    }

    public String getDietSuggestions(String prompt){

        String gptresponse=chatModel.call(prompt);
        return gptresponse;
    }
}

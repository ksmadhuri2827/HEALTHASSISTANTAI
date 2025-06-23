package com.ksm.healthAI.controller;

import com.ksm.healthAI.dto.UserInput;
import com.ksm.healthAI.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/healthai")
@CrossOrigin(origins = "http://localhost:4200")
public class HealthAIController {

    @Autowired
    OpenAIService openAIService;

    @PostMapping("/diet")
    public ResponseEntity<Map<String, Object>> getDietPlan(@RequestBody UserInput userInput) {
        double BMI=calculateBMIIndex(userInput.getHeight(), userInput.getWeight());
        String prompt = buildPrompt(BMI, userInput.getFoodchoice());
        String gptResponse=openAIService.getDietSuggestions(prompt);
        Map<String, Object> response=new HashMap<>();
        response.put("bmi",BMI);
        response.put("dietRecommendations", gptResponse);
        return ResponseEntity.ok(response) ;
    }

    private double calculateBMIIndex(double heightCM, double weightKg)
    {
       double heightM=heightCM/100;
       double bmi=Math.round((weightKg/(heightM*heightM))*100)/100;
       return bmi;
    }

    private String buildPrompt(double bmi, String foodchoice) {
        return "A person with BMI " + bmi + " and dietary preference '" + foodchoice +
                "' needs a structured, healthy weekly meal plan. " +
                "Respond with ONLY a raw JSON object (no markdown, no explanations, no text before or after). " +
                "The structure must be strictly:\n" +
                "{\n" +
                "  \"dietPlan\": {\n" +
                "    \"Monday\": {\n" +
                "      \"breakfast\": {\n" +
                "        \"recipe\": \"string\",\n" +
                "        \"ingredients\": [\"string\", \"string\", ...],\n" +
                "        \"instructions\": [\"string\", \"string\", ...]\n" +
                "      },\n" +
                "      \"lunch\": { ... },\n" +
                "      \"dinner\": { ... }\n" +
                "    },\n" +
                "    \"Tuesday\": { ... },\n" +
                "    ... (through Sunday)\n" +
                "  }\n" +
                "}\n" +
                "All meals must follow this exact format and contain simple, healthy recipes.";
    }

}


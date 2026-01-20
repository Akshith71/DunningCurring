package com.example.DCMS.Servicelayer; 

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.example.DCMS.Entity.Customer;
import com.example.DCMS.Entity.TelecomService;
import com.example.DCMS.Entity.Ticket;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.base-url}")
    private String baseUrl;

    private String url() {
        return baseUrl + "?key=" + apiKey;
    }

    private final OkHttpClient client;

    public GeminiService() {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    // accepts Services and Tickets lists
    public String callGemini(String userMessage, Customer customer, List<TelecomService> services, List<Ticket> tickets) {
        try {
            // 1. Format Services into a String
            String serviceInfo = services.isEmpty() 
                ? "None" 
                : services.stream()
                    .map(s -> s.getServiceType() + " (" + s.getStatus() + ")")
                    .collect(Collectors.joining(", "));

            // 2. Format Tickets into a String
            String ticketInfo = tickets.isEmpty() 
                ? "None" 
                : tickets.stream()
                    .map(t -> "Ticket #" + t.getId() + ": " + t.getSubject() + " [" + t.getStatus() + "]")
                    .collect(Collectors.joining("\n- "));

            // 3. Construct the "All-Knowing" Prompt
            String systemPrompt = String.format(
                "STRICT SYSTEM INSTRUCTIONS:\n" +
                "You are the AI Assistant for 'DCMS Telecom'.\n" +
                "You have access to the user's live database records below. Use them to answer.\n" +
                "\n" +
                "--- USER DATABASE RECORD ---\n" +
                "Name: %s\n" +
                "Account Status: %s\n" +
                "Bill Overdue: $%.2f\n" +
                "Active Services: %s\n" +
                "Recent Tickets:\n- %s\n" +
                "----------------------------\n" +
                "\n" +
                "RULES:\n" +
                "- If they ask 'What services do I have?', read from the 'Active Services' list.\n" +
                "- If they ask 'Do I have pending complaints?', check the 'Recent Tickets' section.\n" +
                "- Keep answers short and helpful.\n" +
                "\n" +
                "USER SAYS: %s",
                customer.getName(), customer.getStatus(), customer.getOverdueAmount(), 
                serviceInfo, ticketInfo, userMessage
            );

            // 4. Build Request (Same as before)
            JSONObject textPart = new JSONObject();
            textPart.put("text", systemPrompt); 
            JSONObject parts = new JSONObject();
            parts.put("parts", new JSONArray().put(textPart));
            JSONObject json = new JSONObject();
            json.put("contents", new JSONArray().put(parts));

            RequestBody body = RequestBody.create(
                json.toString(), MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder().url(url()).post(body).build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) return "⚠️ AI Error: " + response.code();
                String resBody = response.body().string();
                return new JSONObject(resBody).getJSONArray("candidates")
                        .getJSONObject(0).getJSONObject("content")
                        .getJSONArray("parts").getJSONObject(0).getString("text");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return " Connection Error.";
        }
    }
}
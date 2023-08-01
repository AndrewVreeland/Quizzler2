package com.study.quizzler2.helpers.chatGPT;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.study.quizzler2.fragments.ChatFragment;
import com.study.quizzler2.interfaces.ChatAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAPIClient {
    // Media type for API request and response
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final ChatFragment chatFragment;
    private final ChatAPI chatAPI;
    private final Handler mainHandler;
    private boolean isFirstResponse = true;

    private String instruction1 = "Please provide a clear and concise response in 2 sentences or less.";
    private String instruction2 = "Do not include any prefixes in your response.";
    private String instruction3 = "Tell me more about this fact.";

    public ChatAPIClient(ChatFragment chatFragment) {
        this.chatFragment = chatFragment;
        this.mainHandler = new Handler(chatFragment.requireActivity().getMainLooper());

        // Create the Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        // Create the API service using the Retrofit instance
        chatAPI = retrofit.create(ChatAPI.class);
    }

    public void callAPI(String question, Context context) {
        // Append instructions 1 and 2 to the user's question
        String userMessage = instruction1 + "\n" + instruction2 + "\n" + question;

        // Prepare the conversation history by adding instruction 3 and user's question
        StringBuilder conversationHistory = new StringBuilder();
        if (isFirstResponse) {
            conversationHistory.append(instruction3).append("\n");
            isFirstResponse = false;
        }
        conversationHistory.append(userMessage).append("\n");

        // Append previous user and bot messages to the conversation history
        for (Message message : chatFragment.getMessageList()) {
            if (message.getSentBy().equals(Message.SENT_BY_ME)) {
                conversationHistory.append("User: ").append(message.getMessage()).append("\n");
            } else if (message.getSentBy().equals(Message.SENT_BY_BOT)) {
                conversationHistory.append("Bot: ").append(message.getMessage()).append("\n");
            }
        }

        // Prepare the API request JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-16k");
            jsonBody.put("messages", new JSONArray().put(new JSONObject().put("role", "system").put("content", conversationHistory.toString())));
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObject jsonObject = JsonParser.parseString(jsonBody.toString()).getAsJsonObject();

        // Call the API using the Retrofit service
        makeAPIRequest(jsonObject, context);
    }

    private void makeAPIRequest(JsonObject jsonObject, Context context) {
        // Get the API key from Config
        String apiKey = Config.getApiKey(context);

        // Call the API using the Retrofit service
        Call<ChatAPIResponse> call = chatAPI.getChatCompletion("Bearer " + apiKey, jsonObject);
        call.enqueue(new Callback<ChatAPIResponse>() {
            @Override
            public void onResponse(@NonNull Call<ChatAPIResponse> call, @NonNull Response<ChatAPIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatAPIResponse chatAPIResponse = response.body();
                    String result = chatAPIResponse.getResult();
                    mainHandler.post(() -> chatFragment.addResponse(result.trim()));
                } else {
                    mainHandler.post(() -> {
                        try {
                            chatFragment.addResponse("Failed to load response due to " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChatAPIResponse> call, @NonNull Throwable t) {
                mainHandler.post(() -> chatFragment.addResponse("Failed to load response due to " + t.getMessage()));
            }
        });
    }
}
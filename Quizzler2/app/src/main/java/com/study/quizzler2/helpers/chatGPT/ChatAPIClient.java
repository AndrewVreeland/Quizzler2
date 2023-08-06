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

    private String instruction1 = "Please respond briefly with no more than 2 sentences.";
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
        // Prepare the messages list
        JSONArray messagesList = new JSONArray();

        JSONObject jsonBody;
        if (question == null || question.trim().isEmpty()) {
            return; // Exit the method if the input is empty or null
        }
        try {
            // Add instructions to the messages list only for the first API call
            if (isFirstResponse) {
                messagesList.put(new JSONObject().put("role", "system").put("content", instruction3));
                messagesList.put(new JSONObject().put("role", "system").put("content", instruction2));
                isFirstResponse = false;
            }

            // Add instruction1 to every API call
            messagesList.put(new JSONObject().put("role", "system").put("content", instruction1));

            // Add previous user and bot messages to the messages list
            for (Message message : chatFragment.getMessageList()) {
                String role = message.getSentBy().equals(Message.SENT_BY_ME) ? "user" : "assistant";
                messagesList.put(new JSONObject().put("role", role).put("content", message.getMessage()));
            }

            // Add the new user's question to the messages list
            messagesList.put(new JSONObject().put("role", "user").put("content", question));

            // Prepare the API request JSON
            jsonBody = new JSONObject();
            jsonBody.put("model", "gpt-3.5-turbo-16k");
            jsonBody.put("messages", messagesList);
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
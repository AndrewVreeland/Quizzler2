package com.study.quizzler2.managers;

import android.content.Context;

import com.study.quizzler2.helpers.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTManager {

    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient();

    // Constants for topic indices
    public static final int TOPIC_SCIENCE = 0;
    public static final int TOPIC_ANIMALS = 1;
    public static final int TOPIC_HISTORY = 2;
    public static final int TOPIC_GAMES = 3;
    public static final int TOPIC_MUSIC = 4;

    // Method to generate a random fact based on the provided index
    public static void generateRandomFact(int index, Context context, RandomFactListener listener) {
        String apiKey = Config.getApiKey(context);

        String prompt = "Generate a random fact about ";
        String[] topics = { "Science", "Animals", "History", "Games", "Music" };
        if (index >= 0 && index < topics.length) {
            prompt += topics[index];
        } else {
            if (listener != null) {
                listener.onRandomFactGenerated("Invalid index"); // Return a default message for an invalid index
            }
            return;
        }

        try {
            // Make the API call to generate a random fact
            callAPI(prompt, apiKey, listener);
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onRandomFactGenerated("Failed to generate a random fact."); // Return a default message in case of an error
            }
        }
    }

    private static void callAPI(String question, String apiKey, RandomFactListener listener) throws IOException, JSONException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String apiUrl = "https://api.openai.com/v1/completions?timestamp=" + timestamp;
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("model", "text-davinci-003");
        jsonBody.put("prompt", question);
        jsonBody.put("max_tokens", 4000);
        jsonBody.put("temperature", 1);

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onRandomFactGenerated("Failed to generate a random fact.");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String randomFact = jsonArray.getJSONObject(0).getString("text").trim();
                        if (listener != null) {
                            listener.onRandomFactGenerated(randomFact);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (listener != null) {
                            listener.onRandomFactGenerated("Failed to generate a random fact.");
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onRandomFactGenerated("Failed to generate a random fact.");
                    }
                }
            }
        });
    }

    // Listener interface for receiving the generated random fact
    public interface RandomFactListener {
        void onRandomFactGenerated(String randomFact);
    }
}
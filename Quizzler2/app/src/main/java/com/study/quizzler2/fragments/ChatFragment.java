package com.study.quizzler2.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.study.quizzler2.R;
import com.study.quizzler2.adapters.MessageAdapter;
import com.study.quizzler2.helpers.Config;
import com.study.quizzler2.helpers.DelayUtils;
import com.study.quizzler2.helpers.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatFragment extends Fragment {
    // Media type for API request and response
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private boolean isFirstResponse = true;

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    OkHttpClient client = new OkHttpClient();
    private String initialMessage; // Variable to store the initial message

    public ChatFragment() {
        // Required empty public constructor
    }

    // Method to set the initial message when creating the fragment
    public static ChatFragment newInstance(String initialMessage) {
        ChatFragment fragment = new ChatFragment();
        fragment.initialMessage = initialMessage;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);

        // If there is an initial message, add it to the chat and call the API
        if (initialMessage != null && !initialMessage.isEmpty()) {
            addToChat(initialMessage, Message.SENT_BY_ME, "system");
            callAPI(initialMessage, requireContext());
        }

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME, "user");
            messageEditText.setText("");
            callAPI(question, requireContext());
        });
    }

    // Method to add a message to the chat
    void addToChat(String message, String sentBy, String role) {
        // Check if messageAdapter is null before calling notifyDataSetChanged()
        if (messageAdapter != null) {
            messageList.add(new Message(message, sentBy, role));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }

    // Method to add a response from the API to the chat
    void addResponse(String response) {
        if (!messageList.isEmpty()) {
            messageList.remove(messageList.size() - 1);
        }

        if (isFirstResponse) {
            // Send the custom greeting message for the first response
            sendCustomGreetingMessage();
            isFirstResponse = false; // Set the flag to false after the first response

            // Add a 1-second delay before sending the actual response
            DelayUtils.delayAction(new Runnable() {
                @Override
                public void run() {
                    sendActualResponse(response.trim());
                }
            }, 1000); // 1000 milliseconds = 1 second
        } else {
            // Send the actual response from Chat GPT without any delay
            sendActualResponse(response.trim());
        }
    }

    private void sendCustomGreetingMessage() {
        String greetingMessage = "Hello user, here is the additional information you requested:";
        addToChat(greetingMessage, Message.SENT_BY_BOT, "system");
    }

    private void sendActualResponse(String response) {
        // Define a regular expression pattern to match common prefixes

        // Send the actual response from Chat GPT
        addToChat(response, Message.SENT_BY_BOT, "system");
    }

    // Method to make the API call to Chat GPT
    void callAPI(String question, Context context) {
        String instructionAndQuestion = "Please provide a clear and concise response in 2 sentences or less.\n" +
                "do not include any prefixes in your response.\n" +
                "only greet me once\n" +
                "Tell me more about this fact.\n" +
                question + "\n";

        // Build the conversation history from previous messages
        StringBuilder conversationHistory = new StringBuilder(instructionAndQuestion);
        for (Message message : messageList) {
            if (message.getSentBy().equals(Message.SENT_BY_ME)) {
                conversationHistory.append("User: ").append(message.getMessage()).append("\n");
            } else if (message.getSentBy().equals(Message.SENT_BY_BOT)) {
                conversationHistory.append("Bot: ").append(message.getMessage()).append("\n");
            }
        }

        // Add a "Typing..." indicator
        addToChat("Bot: Typing...", Message.SENT_BY_BOT, "system");

        // Prepare the API request JSON
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-0613");
            jsonBody.put("messages", new JSONArray().put(new JSONObject().put("role", "system").put("content", conversationHistory.toString())));
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

        // Get the API key from Config
        String apiKey = Config.getApiKey(context);

        // Build the API request
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() -> addResponse("Failed to load response due to " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("API_Response", responseBody); // Print the entire API response to the log
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray choicesArray = jsonObject.getJSONArray("choices");

                        if (choicesArray.length() > 0) {
                            // Get the first choice from the array and check if it contains the "content" field
                            JSONObject choiceObject = choicesArray.getJSONObject(0);
                            if (choiceObject.has("message")) {
                                JSONObject messageObject = choiceObject.getJSONObject("message");
                                if (messageObject.has("content")) {
                                    String result = messageObject.getString("content");
                                    requireActivity().runOnUiThread(() -> addResponse(result.trim()));
                                } else {
                                    Log.e("API_Response", "No 'content' field in the message object.");
                                }
                            } else {
                                Log.e("API_Response", "No 'message' object in the choice object.");
                            }
                        } else {
                            Log.e("API_Response", "The 'choices' array is empty.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    requireActivity().runOnUiThread(() -> {
                        try {
                            addResponse("Failed to load response due to " + response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }
}
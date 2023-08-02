package com.study.quizzler2.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.study.quizzler2.R;
import com.study.quizzler2.adapters.MessageAdapter;
import com.study.quizzler2.helpers.chatGPT.ChatAPIClient;
import com.study.quizzler2.helpers.DelayUtils;
import com.study.quizzler2.helpers.chatGPT.Message;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

public class ChatFragment extends Fragment {

    // Media type for API request and response
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    private boolean isFirstResponse = true;

    private boolean isTypingMessageDisplayed = false;
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private ChatAPIClient chatAPIClient;

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

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(requireContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        chatAPIClient = new ChatAPIClient(this);

        // If there is an initial message, add it to the chat and call the API
        if (initialMessage != null && !initialMessage.isEmpty()) {
            addToChat(initialMessage, Message.SENT_BY_ME, "system");
            chatAPIClient.callAPI(initialMessage, requireContext());
        }

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) {
                // Disable the send button to prevent the user from sending another message
                sendButton.setEnabled(false);

                addToChat(question, Message.SENT_BY_ME, "user");
                messageEditText.setText("");
                chatAPIClient.callAPI(question, requireContext());
            }
        });
    }

    // Method to add a message to the chat
    void addToChat(String message, String sentBy, String role) {
        // Check if messageAdapter is null before calling notifyDataSetChanged()
        if (messageAdapter != null) {
            // Check if the message is empty or null
            if (message == null || message.trim().isEmpty()) {
                return; // Exit the method if the message is empty or null
            }

            if (isTypingMessageDisplayed) {
                // If the "Typing..." message is already displayed, remove it before adding the new message
                messageList.remove(messageList.size() - 1); // Remove the last message, i.e., "Typing..."
                isTypingMessageDisplayed = false;
            }

            // Add the new message to the messageList
            messageList.add(new Message(message, sentBy, role));
            Log.d("ChatFragment", "addToChat: messageList size = " + messageList.size());

            // Notify the adapter about the changes to the data
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }

    // Method to add a response from the API to the chat
    public void addResponse(String response) {
        // Show the "Typing..." message immediately after the user's message is sent
        addToChat("Typing...", Message.SENT_BY_BOT, "system");
        isTypingMessageDisplayed = true;

        if (isFirstResponse) {
            // Send the custom greeting message for the first response
            sendCustomGreetingMessage();
            isFirstResponse = false; // Set the flag to false after the first response
        }

        // Remove the "Typing..." message just before sending the actual response
        removeTypingMessage();

        // Send the actual response from Chat GPT
        sendActualResponse(response.trim());

        // Enable the send button after sending the actual response
        sendButton.setEnabled(true);
    }
    private void sendCustomGreetingMessage() {
        String greetingMessage = "Hello user, here is the additional information you requested:";
        addToChat(greetingMessage, Message.SENT_BY_BOT, "system");
    }

    private void sendActualResponse(String response) {


        // Send the actual response from Chat GPT
        addToChat(response, Message.SENT_BY_BOT, "system");
        Log.d("ChatFragment", "sendActualResponse: messageList size = " + messageList.size());
    }

    private void removeTypingMessage() {
        if (isTypingMessageDisplayed) {
            messageList.remove(messageList.size() - 1); // Remove the last message, i.e., "Typing..."
            isTypingMessageDisplayed = false;

            // Notify the adapter about the changes to the data
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }

    public List<Message> getMessageList() {
        return messageList;
    }

}
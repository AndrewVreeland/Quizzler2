package com.study.quizzler2.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Message;
import com.study.quizzler2.R;
import com.study.quizzler2.adapters.MessageAdapter;
import com.study.quizzler2.helpers.chatGPT.ChatAPIClient;
import com.study.quizzler2.helpers.chatGPT.LocalMessage;
import com.study.quizzler2.helpers.DatabaseHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import okhttp3.MediaType;

public class ChatFragment extends Fragment {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private boolean isFirstResponse = true;
    private boolean isTypingMessageDisplayed = false;
    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private List<LocalMessage> messageList;
    private MessageAdapter messageAdapter;
    private ChatAPIClient chatAPIClient;
    private List<Message> messagesList = new ArrayList<>();
    private String conversationID; // Variable to store the conversation ID
    private String initialMessage; // Variable to store the initial message
    int messagePosition = 0;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String initialMessage, String conversationID) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("initialMessage", initialMessage);
        args.putString("conversationID", conversationID);
        fragment.setArguments(args);
        return fragment;
    }

    // Overloaded method for newInstance
    public static ChatFragment newInstance(String initialMessage) {
        return newInstance(initialMessage, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

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

        if (getArguments() != null) {
            initialMessage = getArguments().getString("initialMessage");
            conversationID = getArguments().getString("conversationID");
        }

        if (conversationID != null) {
            DatabaseHelper.fetchMessagesForConversation(requireContext(), conversationID,
                    messages -> {
                        if (messages != null) {
                            for (Message message : messages) {
                                messageList.add(LocalMessage.fromAmplifyMessageBySequence(message, messagePosition));
                                messagePosition++;
                            }

                            requireActivity().runOnUiThread(() -> {
                                messageAdapter.notifyDataSetChanged();
                            });
                        }
                    },
                    error -> {
                        Log.e("ChatFragment", "Failed to fetch messages.", error);
                    }
            );
        }

        if (initialMessage != null && !initialMessage.isEmpty()) {
            addToChat(initialMessage, LocalMessage.SENT_BY_ME, "user");
            addToChat("Typing...", LocalMessage.SENT_BY_BOT, "system");
            isTypingMessageDisplayed = true;
            chatAPIClient.callAPI(initialMessage, conversationID, requireContext());
        }

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            if (!question.isEmpty()) {
                sendButton.setEnabled(false);
                addToChat(question, LocalMessage.SENT_BY_ME, "user");
                addToChat("Typing...", LocalMessage.SENT_BY_BOT, "system");
                isTypingMessageDisplayed = true;
                messageEditText.setText("");
                chatAPIClient.callAPI(question, conversationID, requireContext());
            }
        });
    }

    void addToChat(String message, String sentBy, String role) {
        if (messageAdapter != null) {
            if (message == null || message.trim().isEmpty()) {
                return;
            }
            if (isTypingMessageDisplayed) {
                messageList.remove(messageList.size() - 1);
                isTypingMessageDisplayed = false;
            }
            messageList.add(new LocalMessage(message, sentBy, role));
            Log.d("ChatFragment", "addToChat: messageList size = " + messageList.size());
            if(sentBy.equals(LocalMessage.SENT_BY_ME)) {
                DatabaseHelper.saveMessageToDynamoDB(message, conversationID != null ? conversationID : "fallbackID");
            }
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }

    public void addResponse(String response) {
        removeTypingMessage();

        if (isFirstResponse) {
            sendCustomGreetingMessage();
            isFirstResponse = false;
        }

        sendActualResponse(response.trim());
        sendButton.setEnabled(true);
        DatabaseHelper.saveMessageToDynamoDB(response.trim(), conversationID != null ? conversationID : "fallbackID"); // Save the bot's response
    }

    private void sendCustomGreetingMessage() {
        String greetingMessage = "Hello user, here is the additional information you requested:";
        addToChat(greetingMessage, LocalMessage.SENT_BY_BOT, "system");
    }

    private void sendActualResponse(String response) {
        addToChat(response, LocalMessage.SENT_BY_BOT, "system");
        Log.d("ChatFragment", "sendActualResponse: messageList size = " + messageList.size());
    }

    private void removeTypingMessage() {
        if (isTypingMessageDisplayed) {
            messageList.remove(messageList.size() - 1);
            isTypingMessageDisplayed = false;
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        }
    }



    public List<LocalMessage> getMessageList() {
        return messageList;
    }


}
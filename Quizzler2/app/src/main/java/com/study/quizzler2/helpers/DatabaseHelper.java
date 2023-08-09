package com.study.quizzler2.helpers;

import android.util.Log;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.Conversation;

public class DatabaseHelper {

    public static void saveMessageToDynamoDB(String content, String conversationID) {
        // Build a Conversation object using the conversationID
        Conversation conversation = Conversation.justId(conversationID);

        // Use the Message builder to create a Message object
        Message message = Message.builder()
                .content(content)                   // Set content
                .version(1)                         // Set version
                .lastChangedAt(Temporal.DateTime.now())   // Use Temporal.DateTime for lastChangedAt
                .createdAt(Temporal.DateTime.now())       // Use Temporal.DateTime for createdAt
                .updatedAt(Temporal.DateTime.now())       // Use Temporal.DateTime for updatedAt
                .conversation(conversation)         // Set conversation
                .build();                           // Build the Message object

        // Use Amplify DataStore to save the message
        Amplify.DataStore.save(
                message,
                success -> Log.i("Amplify", "Saved item: " + success.item().getContent()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );
    }
    public static void ensureConversationExistsAndThenSaveMessage(String conversationID, String messageContent) {
        Amplify.DataStore.query(Conversation.class,
                Conversation.ID.eq(conversationID),
                conversationList -> {
                    if (!conversationList.hasNext()) {
                        // Conversation doesn't exist, so create it
                        Conversation newConversation = Conversation.builder()
                                .id(conversationID)
                                // Add any other necessary fields here
                                .build();

                        Amplify.DataStore.save(newConversation,
                                success -> {
                                    Log.i("Amplify", "Saved conversation: " + success.item().getId());
                                    saveMessageToDynamoDB(messageContent, conversationID);
                                },
                                error -> Log.e("Amplify", "Could not save conversation", error));
                    } else {
                        // Conversation exists, so proceed to save the message
                        saveMessageToDynamoDB(messageContent, conversationID);
                    }
                },
                error -> Log.e("Amplify", "Error querying for conversation", error));
    }
}
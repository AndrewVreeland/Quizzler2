package com.study.quizzler2.helpers;


import android.util.Log;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.Conversation;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class DatabaseHelper {

    public static void saveMessageToDynamoDB(String content, String conversationID) {
        Temporal.Timestamp timestamp = getCurrentAmplifyTimestamp();
        Temporal.DateTime dateTime = getCurrentAmplifyDateTime();

        // Build a Conversation object using the conversationID
        Conversation conversation = Conversation.justId(conversationID);

        // Use the Message builder to create a Message object
        Message message = Message.builder()
                .content(content)
                .version(1)
                .lastChangedAt(timestamp)  // Using the Temporal.Timestamp
                .createdAt(dateTime)       // Using the Temporal.DateTime
                .updatedAt(dateTime)      // Using the Temporal.Timestamp again
                .conversation(conversation)
                .build();

        // Use Amplify DataStore to save the message
        Amplify.DataStore.save(
                message,
                success -> Log.i("Amplify", "Saved item: " + success.item().getContent()),
                error -> Log.e("Amplify", "Could not save item to DataStore", error)
        );
    }
    public static void ensureConversationExistsAndThenSaveMessage(String conversationID, String messageContent) {
        saveMessageToDynamoDB(messageContent, conversationID);
    }
    private static Temporal.Timestamp getCurrentAmplifyTimestamp() {
        return new Temporal.Timestamp(new Date());
    }
    private static Temporal.DateTime getCurrentAmplifyDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        return new Temporal.DateTime(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
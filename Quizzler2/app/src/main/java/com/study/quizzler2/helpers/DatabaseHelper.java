package com.study.quizzler2.helpers;

import android.util.Log;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.amplifyframework.datastore.generated.model.Message;

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
                .lastChangedAt(timestamp)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .conversation(conversation)
                .build();

        // Use Amplify API to save the message
        Amplify.API.mutate(
                ModelMutation.create(message),
                success -> Log.i("Amplify", "Saved item: " + success.getData().getContent()),
                error -> Log.e("Amplify", "Could not save item", error)
        );
    }

    public static void saveMessageAfterConversation(String conversationID, String messageContent) {
        saveMessageToDynamoDB(messageContent, conversationID);
    }

    private static Temporal.Timestamp getCurrentAmplifyTimestamp() {
        return new Temporal.Timestamp(new Date());
    }

    public static Temporal.DateTime getCurrentAmplifyDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        return new Temporal.DateTime(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }
}
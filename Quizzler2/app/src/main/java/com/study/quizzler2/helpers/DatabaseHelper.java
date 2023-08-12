package com.study.quizzler2.helpers;

import android.util.Log;

import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.Consumer;
import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.datastore.generated.model.Conversation;
import com.amplifyframework.datastore.generated.model.Message;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public static void getConversations(int limit, Consumer<List<Conversation>> onSuccess, Consumer<Throwable> onError) {
        Amplify.API.query(
                ModelQuery.list(Conversation.class, ModelPagination.limit(limit)),
                response -> {
                    if (response.hasData()) {
                        List<Conversation> conversationList = StreamSupport.stream(response.getData().getItems().spliterator(), false)
                                .collect(Collectors.toList());
                        onSuccess.accept(conversationList);
                    }

                    if (response.hasErrors()) {
                        for (GraphQLResponse.Error error : response.getErrors()) {
                            Log.e("Amplify", error.getMessage());
                        }
                    }
                },
                failure -> {
                    Log.e("Amplify", "Query failed.", failure);
                    onError.accept(failure);
                }
        );
    }

    public static void fetchMessagesForConversation(String conversationId, Consumer<List<Message>> onSuccess, Consumer<Throwable> onError) {
        // Query messages based on the conversationID
        Amplify.API.query(
                ModelQuery.list(Message.class, Message.CONVERSATION.eq(conversationId)),
                response -> {
                    if (response.hasData()) {
                        List<Message> messagesList = StreamSupport.stream(response.getData().getItems().spliterator(), false)
                                .collect(Collectors.toList());
                        onSuccess.accept(messagesList);
                    }

                    if (response.hasErrors()) {
                        for (GraphQLResponse.Error error : response.getErrors()) {
                            Log.e("Amplify", error.getMessage());
                        }
                    }
                },
                failure -> {
                    Log.e("Amplify", "Query failed.", failure);
                    onError.accept(failure);
                }
        );
    }


}

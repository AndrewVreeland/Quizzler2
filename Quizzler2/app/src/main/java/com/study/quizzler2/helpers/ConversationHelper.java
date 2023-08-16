package com.study.quizzler2.helpers;

import com.amplifyframework.datastore.generated.model.Conversation;
import com.study.quizzler2.utils.ConversationItem;

import java.util.ArrayList;
import java.util.List;

public class ConversationHelper {

    /**
     * Convert a list of Conversation objects into a list of ConversationItem objects.
     *
     * @param conversations - the list of Conversation objects.
     * @return a list of ConversationItem objects.
     */
    public static List<ConversationItem> convertToConversationItemList(List<Conversation> conversations) {
        List<ConversationItem> conversationItems = new ArrayList<>();
        for (Conversation conversation : conversations) {
            ConversationItem item = convertSingleConversationToItem(conversation);
            conversationItems.add(item);
        }
        return conversationItems;
    }

    /**
     * Convert a single Conversation object into a ConversationItem object.

     * @param conversation - the Conversation object.
     * @return a ConversationItem object.
     */
    public static ConversationItem convertSingleConversationToItem(Conversation conversation) {
        String id = conversation.getId();


        String snippet = (conversation.getMessages() != null && !conversation.getMessages().isEmpty())
                ? conversation.getMessages().get(0).getContent()
                : "";  // Default snippet if there are no messages.

        return new ConversationItem(id, snippet);
    }
}
package com.study.quizzler2.helpers;

import com.amplifyframework.datastore.generated.model.Conversation;
import com.study.quizzler2.utils.ConversationItem;

import java.util.ArrayList;
import java.util.List;

public class ConversationHelper {

    public static List<ConversationItem> convertToConversationItemList(List<Conversation> conversations) {
        List<ConversationItem> conversationItems = new ArrayList<>();
        for (Conversation conversation : conversations) {
            String id = conversation.getId();

            // Assuming you want to use the first message as the snippet, if available.
            String snippet = (conversation.getMessages() != null && !conversation.getMessages().isEmpty())
                    ? conversation.getMessages().get(0).getContent()  // Replace `.getContent()` with appropriate getter if different.
                    : "";  // Default snippet if there are no messages.

            ConversationItem item = new ConversationItem(id, snippet);
            conversationItems.add(item);
        }
        return conversationItems;
    }
}
package com.study.quizzler2.helpers;

import com.amplifyframework.datastore.generated.model.Conversation;
import com.study.quizzler2.utils.ConversationItem;

import java.util.ArrayList;
import java.util.List;

public class ConversationHelper {

    public static List<ConversationItem> convertToConversationItemList(List<Conversation> conversations) {
        List<ConversationItem> conversationItems = new ArrayList<>();
        for (Conversation conversation : conversations) {
            ConversationItem item = // ... Convert 'conversation' to 'ConversationItem'
                    conversationItems.add(item);
        }
        return conversationItems;
    }
}
package com.study.quizzler2.utils;

import com.study.quizzler2.helpers.chatGPT.ChatGPTRandomFact;
import com.amplifyframework.datastore.generated.model.ConversationTypeEnum;

public class TopicUtility {

    public static int getTopicIndex(String topic) {
        switch (topic) {
            case "Science":
                return ChatGPTRandomFact.TOPIC_SCIENCE;
            case "Animals":
                return ChatGPTRandomFact.TOPIC_ANIMALS;
            case "History":
                return ChatGPTRandomFact.TOPIC_HISTORY;
            case "Games":
                return ChatGPTRandomFact.TOPIC_GAMES;
            case "Music":
                return ChatGPTRandomFact.TOPIC_MUSIC;
            default:
                return -1; // Invalid index
        }
    }

    public static ConversationTypeEnum getEnumFromCategory(String category) {
        switch(category) {
            case "Music":
                return ConversationTypeEnum.Music;
            case "Animals":
                return ConversationTypeEnum.Animals;
            case "Games":
                return ConversationTypeEnum.Games;
            case "History":
                return ConversationTypeEnum.History;
            case "Science":
                return ConversationTypeEnum.Science;
            default:
                throw new IllegalArgumentException("Invalid category: " + category);
        }
    }
}
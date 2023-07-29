package com.study.quizzler2.helpers;

import com.study.quizzler2.managers.ChatGPTManager;

public class TopicUtility {
    public static int getTopicIndex(String topic) {
        switch (topic) {
            case "Science":
                return ChatGPTManager.TOPIC_SCIENCE;
            case "Animals":
                return ChatGPTManager.TOPIC_ANIMALS;
            case "History":
                return ChatGPTManager.TOPIC_HISTORY;
            case "Games":
                return ChatGPTManager.TOPIC_GAMES;
            case "Music":
                return ChatGPTManager.TOPIC_MUSIC;
            default:
                return -1; // Invalid index
        }
    }
}

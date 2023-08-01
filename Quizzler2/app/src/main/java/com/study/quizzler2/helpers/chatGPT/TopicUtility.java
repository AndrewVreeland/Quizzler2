package com.study.quizzler2.helpers.chatGPT;

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
}

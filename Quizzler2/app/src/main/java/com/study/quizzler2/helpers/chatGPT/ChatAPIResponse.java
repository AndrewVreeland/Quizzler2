package com.study.quizzler2.helpers.chatGPT;

import com.google.gson.annotations.SerializedName;

public class ChatAPIResponse {
    @SerializedName("choices")
    private ChatAPIChoice[] choices;

    public String getResult() {
        if (choices != null && choices.length > 0 && choices[0].message != null) {
            return choices[0].message.getContent();
        }
        return null;
    }

    private static class ChatAPIChoice {
        @SerializedName("message")
        private ChatAPIMessage message;

        private static class ChatAPIMessage {
            @SerializedName("content")
            private String content;

            public String getContent() {
                return content;
            }
        }
    }
}
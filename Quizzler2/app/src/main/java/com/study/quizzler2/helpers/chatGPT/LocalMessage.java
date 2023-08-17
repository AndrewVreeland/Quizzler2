package com.study.quizzler2.helpers.chatGPT;

import com.amplifyframework.datastore.generated.model.Message;

public class LocalMessage {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT="bot";


    String message;
    String sentBy;
    String role;

    public LocalMessage(String message, String sentBy, String role) {
        this.message = message;
        this.sentBy = sentBy;
        this.role = role;
    }

    public static LocalMessage fromAmplifyMessageBySequence(Message amplifyMessage, int position) {
        String sentBy;

        if (position == 0) { // first message
            sentBy = LocalMessage.SENT_BY_ME;
        } else if (position == 1 || position == 2) { // second and third messages
            sentBy = LocalMessage.SENT_BY_BOT;
        } else {

            sentBy = (position % 2 == 0) ? LocalMessage.SENT_BY_BOT : LocalMessage.SENT_BY_ME;
        }

        return new LocalMessage(amplifyMessage.getContent(), sentBy, "role"); // Replace "role" with actual role if needed.
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}


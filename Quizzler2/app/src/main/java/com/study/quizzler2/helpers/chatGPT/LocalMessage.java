package com.study.quizzler2.helpers.chatGPT;
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


package com.study.quizzler2.utils;

public class ConversationItem {
    private String id; // ID of the conversation
    private String snippet; // Snippet of the First message or title of the conversation

    // Constructor, getters, setters, etc.

    public ConversationItem(String id, String snippet) {
        this.id = id;
        this.snippet = snippet;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getSnippet() {
        return snippet;
    }


    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

}
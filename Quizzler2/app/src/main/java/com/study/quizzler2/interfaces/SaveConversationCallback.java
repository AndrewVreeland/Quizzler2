package com.study.quizzler2.interfaces;

public interface SaveConversationCallback {
    void onSuccess(String conversationId);
    void onError(Exception e);
}
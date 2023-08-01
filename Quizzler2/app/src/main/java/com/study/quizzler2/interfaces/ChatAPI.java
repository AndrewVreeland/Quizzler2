package com.study.quizzler2.interfaces;

import com.google.gson.JsonObject;
import com.study.quizzler2.helpers.chatGPT.ChatAPIResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChatAPI {
    @POST("chat/completions")
    Call<ChatAPIResponse> getChatCompletion(@Header("Authorization") String authorization,
                                            @Body JsonObject body);
}
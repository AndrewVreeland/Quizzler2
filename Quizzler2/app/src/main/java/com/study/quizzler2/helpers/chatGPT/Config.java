package com.study.quizzler2.helpers.chatGPT;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static String getApiKey(Context context) {
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = readApiKeyFromConfigFile(context);
        }

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("OpenAI API key is not set.");
        }

        return apiKey;
    }

    private static String readApiKeyFromConfigFile(Context context) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open("config.properties");
            properties.load(inputStream);
            return properties.getProperty("openai.api.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

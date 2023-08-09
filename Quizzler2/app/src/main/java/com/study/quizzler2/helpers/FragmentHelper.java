package com.study.quizzler2.helpers;

import android.os.Handler;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.amplifyframework.datastore.generated.model.ConversationTypeEnum;
import com.study.quizzler2.helpers.chatGPT.ChatGPTRandomFact;
import com.study.quizzler2.utils.TopicUtility;
import com.study.quizzler2.interfaces.updateTriviaTextInterface;

public class FragmentHelper {

    public static void replaceFragmentWithDelay(FragmentManager fragmentManager, int index, updateTriviaTextInterface.OnTextUpdateListener listener, View fragmentView, FragmentReplaceListener fragmentReplaceListener) {
        final int DELAY_MILLISECONDS = 500; // Adjust this value as needed (in milliseconds)

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                generateRandomFactAndNotifyListener(fragmentManager, index, listener, fragmentView);

                if (fragmentReplaceListener != null) {
                    fragmentReplaceListener.onFragmentReplaced();
                }
            }
        }, DELAY_MILLISECONDS);
    }

    private static void generateRandomFactAndNotifyListener(FragmentManager fragmentManager, int index, updateTriviaTextInterface.OnTextUpdateListener listener, View fragmentView) {
        ChatGPTRandomFact.generateRandomFact(index, fragmentView.getContext(), new ChatGPTRandomFact.RandomFactListener() {
            @Override
            public void onRandomFactGenerated(String category, String randomFact) {
                // Print the generated fact to the console
                System.out.println("Category: " + category + " | Generated Random Fact: " + randomFact);

                // Convert the category string into the corresponding enum
                ConversationTypeEnum categoryEnum = TopicUtility.getEnumFromCategory(category);

                // You can now use categoryEnum for further processing or storage

                // Update the text with the random fact on the UI thread
                fragmentView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.updateText(randomFact, category);
                        }
                    }
                });
            }
        });
    }

    public interface FragmentReplaceListener {
        void onFragmentReplaced();
    }
}
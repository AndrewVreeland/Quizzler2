package com.study.quizzler2.helpers;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentManager;

import com.study.quizzler2.R;
import com.study.quizzler2.helpers.chatGPT.ChatGPTRandomFact;
import com.study.quizzler2.helpers.chatGPT.TopicUtility;
import com.study.quizzler2.interfaces.updateTriviaTextInterface;

public class FragmentHelper {

    public static void replaceFragmentWithDelay(FragmentManager fragmentManager, int index, updateTriviaTextInterface.OnTextUpdateListener listener, View fragmentView, FragmentReplaceListener fragmentReplaceListener) {
        final int DELAY_MILLISECONDS = 500; // Adjust this value as needed (in milliseconds)

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    switch (index) {
                        case 0:
                            // Replace the current fragment with the HomeFragment
                            generateRandomFactAndNotifyListener(fragmentManager, "Science", listener, fragmentView);
                            break;
                        case 1:
                            // Replace the current fragment with the AnimalsFragment
                            generateRandomFactAndNotifyListener(fragmentManager, "Animals", listener, fragmentView);
                            break;
                        case 2:
                            // Replace the current fragment with the HistoryFragment
                            generateRandomFactAndNotifyListener(fragmentManager, "History", listener, fragmentView);
                            break;
                        case 3:
                            // Replace the current fragment with the GamesFragment
                            generateRandomFactAndNotifyListener(fragmentManager, "Games", listener, fragmentView);
                            break;
                        case 4:
                            // Replace the current fragment with the MusicFragment
                            generateRandomFactAndNotifyListener(fragmentManager, "Music", listener, fragmentView);
                            break;
                    }
                }

                if (fragmentReplaceListener != null) {
                    fragmentReplaceListener.onFragmentReplaced();
                }
            }
        }, DELAY_MILLISECONDS);
    }

    private static void generateRandomFactAndNotifyListener(FragmentManager fragmentManager, String topic, updateTriviaTextInterface.OnTextUpdateListener listener, View fragmentView) {
        // Generate a random fact for the provided topic
        ChatGPTRandomFact.generateRandomFact(TopicUtility.getTopicIndex(topic), fragmentView.getContext(), new ChatGPTRandomFact.RandomFactListener() {
            @Override
            public void onRandomFactGenerated(String randomFact) {
                // Print the generated fact to the console
                System.out.println("Generated Random Fact: " + randomFact);

                // Update the text with the random fact on the UI thread
                fragmentView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.updateText(randomFact);
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